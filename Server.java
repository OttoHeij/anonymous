import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Vector;

public class Server
{
	/**
	 * 服务端类
	 * 
	 * @author ZyL
	 */
	private ServerSocket ss;
	private Vector<Socket> vec;
	private PrintWriter pw_temp;

	public Server()
	{
		vec = new Vector<Socket>();
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
				String string = ip+"进入了房间,总人数"+vec.size()+"只";
				ServerUI.jTextArea1_message.append(string+"\r\n");
				pw.println("成功连接到服务器,你的ip：" + ip);
//				pw.println(string);
				broadcast(ip+"进入了房间,总人数"+vec.size()+"个");
				String str = null;
				while (true)
				{
					if ((str = br.readLine()) != null)
					{
						if (str.equals("over"))
						{
							vec.remove(s);
							String msg = ip + "离开了房间," + "还有" + vec.size() + "只";
							ServerUI.jTextArea1_message.append(msg+"\r\n");
							broadcast(msg);
							br.close();
							s.close();
							pw.close();
							break;
						} else
						{
							ServerUI.jTextArea1_message.append(ip + ":" + str + "\r\n");
							broadcast(str);
						}
					}
				}
			} catch (SocketException e)
			{
				ServerUI.jTextArea1_message.append(ip + "断开连接\r\n");
				vec.remove(s);
				e.printStackTrace();
			} catch (UnsupportedEncodingException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally
			{
				try
				{
					// if (Server.this.ss != null)// /
					// 巨坑~~~千万不要关闭，一关闭ss.accpet()就抛SocketisClosed
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
