import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Vector;
import protocal.Protocol;

public class Server
{
	/**
	 * 服务端类
	 * 
	 * @author ZyL
	 */
	private ServerSocket ss;
	private Vector<Socket> vec;
	private Vector<User> vec_u;
	private PrintWriter pw_temp;

	public Server()
	{
		vec = new Vector<Socket>();
		vec_u = new Vector<User>();
		try
		{
			ss = new ServerSocket(10000);
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/*
	 * public void sendMessage(String str) { pw.println(str); }
	 */
	public void close()
	{
		try
		{
			ss.close();
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Server的内部类，主要是监听等待客户端连接
	 * 
	 * @author ZyL
	 * 
	 */
	class SocketLisener implements Runnable
	{
		Socket s;

		@Override
		public void run()
		{
			// TODO Auto-generated method stub
			try
			{
				while (true)
				{
					s = ss.accept();
					vec.add(s);
					new Thread(new socketHandler(s)).start();
				}
			} catch (IOException e2)
			{
				// TODO Auto-generated catch block
				e2.printStackTrace();
			} finally
			{
				try
				{
					if (ss != null)
						ss.close();
				} catch (IOException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 处理socket信息的类
	 * 
	 * @author ZyL
	 * 
	 */
	class socketHandler implements Runnable
	{
		private Socket s;
		private BufferedReader br;
		private PrintWriter pw;
		private String ip;

		public socketHandler(Socket s)
		{
			super();
			this.s = s;
		}
		@Override
		public void run()
		{
			try
			{
				pw = new PrintWriter(new OutputStreamWriter(s.getOutputStream(), "utf-8"), true);
				br = new BufferedReader(new InputStreamReader(s.getInputStream(), "utf-8"));
				ip = s.getInetAddress().getHostAddress();
				String tmpStr = br.readLine();
				System.out.println(tmpStr);
				String phoneNum = null;
				if (tmpStr != null)
				{
					phoneNum = tmpStr.substring(tmpStr.indexOf(Protocol.PHONE_NUM) + Protocol.PHONE_NUM.length());
					System.out.println(phoneNum);
					if (phoneNum.matches("\\d+"))
					{
						pw.println(Protocol.WHISPER + Protocol.PHONE_NUM + phoneNum + Protocol.MSG + "成功连接到服务器,你的ip：" + ip);
						System.out.println("vec_u添加了"+phoneNum);
						vec_u.add(new User(phoneNum));
					}
				}
				String string = ip + "进入了房间,总人数" + vec.size() + "个";
				ServerUI.jTextArea1_message.append(string + "\r\n");
				System.out.println(string);
//				pw.println(string);
				broadcast(Protocol.BROADCAST + Protocol.MSG + string);
				String str = null;
				while (true)
				{
					if ((str = br.readLine()) != null)
					{
						if (str.equals("over"))
						{
							vec.remove(s);
							vec_u.remove(new User(phoneNum));
							System.out.println("删除了"+phoneNum+",size:"+vec_u.size());
							String msg = ip + "离开了房间," + "还有" + vec.size() + "个";
							ServerUI.jTextArea1_message.append(msg + "\r\n");
							System.out.println(msg);
							broadcast(Protocol.BROADCAST + Protocol.MSG + msg);
							br.close();
							s.close();
							pw.close();
							break;
						} else
						{
							ServerUI.jTextArea1_message.append(ip + ":" + str + "\r\n");
							System.out.println(ip + ":" + str);
							if (checkIsRegistered(str))
							{
								pw.println(Protocol.IS_REGISTERED + "true");
							} else
							{
								pw.println(Protocol.IS_REGISTERED + "false");
								System.out.println("该人没有在Vector里面");
								if(phoneNum != null)
									pw.println(Protocol.WHISPER + Protocol.PHONE_NUM +phoneNum+Protocol.MSG + "该人木有在线，你该发短信勒~( ⊙ o ⊙ )");
							}
							broadcast(str);
						}
					}
				}
			} catch (SocketException e)
			{
				ServerUI.jTextArea1_message.append(ip + "断开连接\r\n");
				System.out.println(ip + "断开连接");
				vec.remove(s);
				e.printStackTrace();
			} catch (UnsupportedEncodingException e)
			{
				// TODO Auto-generated catch block
				vec.remove(s);
				e.printStackTrace();
			} catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
				vec.remove(s);
			} finally
			{
				try
				{
					// if (Server.this.ss != null)// /
					// 一关闭ss.accpet()就抛SocketisClosed
					// ss.close();
					if (s != null)
						s.close();
					if (br != null)
						br.close();
					if (pw != null)
						pw.close();
				} catch (IOException e)
				{ // TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	private boolean checkIsRegistered(String str)
	{
		if (str.startsWith(Protocol.WHISPER))
		{
			int num1 = str.indexOf(Protocol.PHONE_NUM) + Protocol.PHONE_NUM.length();
			int num2 = str.indexOf(Protocol.MSG);
			String temp = str.substring(num1, num2);
			System.out.println("号码"+temp);
			String numberList[] = temp.split("&");
			for (int i = 0; i < numberList.length; i++)
			{
				temp = new StringBuffer(str.substring(str.indexOf(Protocol.MSG) + Protocol.MSG.length())).toString();
				System.out.println("numberList[1]"+ numberList[1]);
				if (numberList.length != 1)
				{
					for (User user : vec_u)
					{
						System.out.println(user);
						if (numberList[1].equals(user.getPhoneNumber()))
						{
							return true;
						}
					}
				}
				System.out.println(numberList[i]);
			}
		}
		return false;
	}
	private void broadcast(String str) throws IOException
	{
		for (int i = 0; i < vec.size(); i++)
		{
			Socket s = vec.get(i);
			pw_temp = new PrintWriter(new OutputStreamWriter(s.getOutputStream(), "utf-8"), true);
			pw_temp.println(str);
		}
	}
	public static void main(String arguments[])
	{
		Server s = new Server();
		ServerUI serverUI = new ServerUI(s);
		SocketLisener sl = s.new SocketLisener();
		new Thread(sl).start();
	}
}
