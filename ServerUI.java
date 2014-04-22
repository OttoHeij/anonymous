
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

public class ServerUI extends javax.swing.JFrame
{
	/**
	 * @author ZyL
	 * 服务端的UI，按键监听类为ServerUIListener
	 */
	{
		// Set Look & Feel
		try
		{
			javax.swing.UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	static JTextField jTextField_send;
	static JScrollPane jScrollPane1;

	static JTextArea jTextArea1_message;

	static JButton jButton1_send;

	private ServerUIListener uiListener;

	private Server server;
	/**
	 * Auto-generated main method to display this JFrame
	 */
//	public static void main(String[] args)
//	{
//		SwingUtilities.invokeLater(new Runnable()
//		{
//			public void run()
//			{
//				ServerUI inst = new ServerUI();
//				inst.setLocationRelativeTo(null);
//				inst.setVisible(true);
//			}
//		});
//	}
	public ServerUI(Server server)
	{
		super();
		this.server = server;
		this.uiListener = new ServerUIListener(server, this);
		initGUI();
	}
	public JButton getjButton1_send()
	{
		return jButton1_send;
	}
	public JScrollPane getjScrollPane1()
	{
		return jScrollPane1;
	}
	public JTextArea getjTextArea1_message()
	{
		return jTextArea1_message;
	}
	public JTextField getjTextField_send()
	{
		return jTextField_send;
	}

	private void initGUI()
	{
		try
		{
			setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			getContentPane().setLayout(null);
			{
				jTextField_send = new JTextField();
				getContentPane().add(jTextField_send, "Center");
				jTextField_send.setBounds(10, 232, 299, 37);
			}
			{
				jButton1_send = new JButton();
				getContentPane().add(jButton1_send);
				jButton1_send.setBounds(330, 232, 94, 30);
				jButton1_send.setText("\u53d1\u9001");
			}
			{
				jScrollPane1 = new JScrollPane();
				getContentPane().add(jScrollPane1);
				jScrollPane1.setBounds(10, 20, 299, 180);
				{
					jTextArea1_message = new JTextArea();
					jScrollPane1.setViewportView(jTextArea1_message);
					jTextArea1_message.setBounds(10, 20, 299, 180);
				}
			}
//			pack();
			this.setSize(464, 324);
			jButton1_send.addActionListener(uiListener);
			jTextField_send.addKeyListener(uiListener);
			this.addWindowListener(uiListener);
			this.setVisible(true);
			this.setLocationRelativeTo(null);
			this.setTitle("服务端^_^");
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
