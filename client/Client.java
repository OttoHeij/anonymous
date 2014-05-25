package client;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import javax.swing.JOptionPane;
import protocal.Protocol;

public class Client implements Runnable
{
	private BufferedReader br = null;
	private PrintWriter pw = null;
	private Socket s = null;
	private String phonenum = ClientUI.jTextField1_myPhoneNum.getText();

	public void close()
	{
		try
		{
			if (s != null)
				s.close();
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public BufferedReader getBr()
	{
		return br;
	}
	@Override
	public void run()
	{
		// TODO Auto-generated method stub
		try
		{
			s = new Socket("zyl-me.xicp.net", 10000);
			pw = new PrintWriter(new OutputStreamWriter(s.getOutputStream(), "utf-8"), true);
			br = new BufferedReader(new InputStreamReader(s.getInputStream(), "utf-8"));
			String str = null;
			pw.println(Protocol.PHONE_NUM + phonenum);
			while ((str = br.readLine()) != null)
			{
				if (str.startsWith(Protocol.BROADCAST))
				{
					String tmpStr = str.substring(str.indexOf(Protocol.MSG) + Protocol.MSG.length());
					ClientUI.jTextArea1_message.append(tmpStr + "\r\n");
				} else if (str.startsWith(Protocol.WHISPER))
				{
					int num = str.indexOf(Protocol.PHONE_NUM) + Protocol.PHONE_NUM.length();
					int num2 = str.indexOf(Protocol.MSG);
					String phoneNums1 = str.substring(num, num2);
					String phoneNums2[] = phoneNums1.split("&");
					for (int i = 0; i < phoneNums2.length; i++)
					{
						if (phoneNums2[i].equals(phonenum))
						{
							String msg = str.substring(num2+Protocol.MSG.length()) + "\r\n";
							if(phoneNums2.length != 1)
							{
								msg = phoneNums2[0]+" : " + msg;
							}
							ClientUI.jTextArea1_message.append(msg);
						}
					}
				}
			}
		} catch (IOException e)
		{
			JOptionPane.showMessageDialog(null, "未连接到主机！退出");
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(0);
		} finally
		{
			try
			{
				sendMessage("over");
				if (s != null)
					s.close();
				if (br != null)
					br.close();
				if (pw != null)
					pw.close();
			} catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	public void sendMessage(String str)
	{
		pw.println(str);
	}
}
