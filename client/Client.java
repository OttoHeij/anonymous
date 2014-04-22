package client;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import javax.swing.JOptionPane;

public class Client implements Runnable
{
	private BufferedReader br = null;
	private PrintWriter pw = null;
	private Socket s = null;

	public void sendMessage(String str)
	{
		pw.println(str);
	}
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
	@Override
	public void run()
	{
		// TODO Auto-generated method stub
		try
		{
			s = new Socket("zyl-me.xicp.net", 10000);
			pw = new PrintWriter(new OutputStreamWriter(s.getOutputStream(), "utf-8"),true);
			br = new BufferedReader(new InputStreamReader(s.getInputStream(), "utf-8"));
			String str = null;
			while ((str = br.readLine()) != null)
			{
				ClientUI.jTextArea1_message.append(str + "\r\n");
			}
		}  catch (IOException e)
		{
			JOptionPane.showMessageDialog(null, "未连接到主机！退出");
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(0);
		} finally
		{
			try
			{
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
}
