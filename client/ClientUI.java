package client;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import protocal.Protocal;

public class ClientUI extends javax.swing.JFrame
{
	/**
	 * @author ZyL 客户端的UI，按键监听类为ServerUIListener
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
	static JTextField jTextField1_myPhoneNum;
	private JLabel jLabel1;
	/**
	 * Auto-generated main method to display this JFrame
	 */
	public static void main(String[] args)
	{
		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				ClientUI inst = new ClientUI();
				inst.setLocationRelativeTo(null);
				inst.setVisible(true);
			}
		});
	}
	private JLabel jLabel1_phoneNum;
	private JTextField jTextField1_phoneNum;
	static JTextArea jTextArea1_message;
	static JButton jButton1_send;

	private Client client;
	public ClientUI()
	{
		super();
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
	private void sendMessage()
	{
		String str = jTextField_send.getText();
		jTextField_send.setText("");
		client.sendMessage(Protocal.WHISPER+Protocal.PHONE_NUM+jTextField1_myPhoneNum.getText()+"&"+jTextField1_phoneNum.getText()+Protocal.MSG+str);
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
			{
				jTextField1_phoneNum = new JTextField();
				getContentPane().add(jTextField1_phoneNum);
				jTextField1_phoneNum.setText("654321");
				jTextField1_phoneNum.setBounds(382, 101, 42, 21);
			}
			{
				jLabel1_phoneNum = new JLabel();
				getContentPane().add(jLabel1_phoneNum);
				jLabel1_phoneNum.setBounds(324, 104, 48, 15);
				jLabel1_phoneNum.setText("\u5bf9\u65b9\u7535\u8bdd");
			}
			{
				jTextField1_myPhoneNum = new JTextField();
				getContentPane().add(jTextField1_myPhoneNum);
				jTextField1_myPhoneNum.setText("111111");
				jTextField1_myPhoneNum.setBounds(382, 26, 42, 21);
			}
			{
				jLabel1 = new JLabel();
				getContentPane().add(jLabel1);
				jLabel1.setBounds(324, 27, 48, 19);
				jLabel1.setText("\u6211\u7684\u7535\u8bdd");
			}
//			pack();
			this.setSize(464, 324);
			client = new Client();
			new Thread(client).start();
			jButton1_send.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e)
				{
					sendMessage();
				}

			});
			jTextField_send.addKeyListener(new KeyAdapter()
			{
				@Override
				public void keyPressed(KeyEvent e)
				{
					if (e.getKeyCode() == KeyEvent.VK_ENTER)
					{
						sendMessage();
					}
				}
			});
			this.addWindowListener(new WindowAdapter()
			{
				@Override
				public void windowClosed(WindowEvent e)
				{
					super.windowClosed(e);
					client.sendMessage("over");
					client.close();
					System.exit(0);
				}
			});
			this.setVisible(true);
			this.setLocationRelativeTo(null);
			this.setTitle("客户端^_^");
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
