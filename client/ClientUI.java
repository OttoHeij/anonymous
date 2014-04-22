package client;

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

public class ClientUI extends javax.swing.JFrame
{
	/**
	 * @author ZyL
	 * 客户端的UI，按键监听类为ServerUIListener
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


	private Client client;
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
			client = new Client();
			new Thread(client).start();
			jButton1_send.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e)
				{
					String str = jTextField_send.getText();
					jTextField_send.setText("");
					client.sendMessage(str);
				}
			});
			jTextField_send.addKeyListener(new KeyAdapter()
			{
				@Override
				public void keyPressed(KeyEvent e)
				{
					if(e.getKeyCode() == KeyEvent.VK_ENTER)
					{
						String str = jTextField_send.getText();
						jTextField_send.setText("");
						client.sendMessage(str);
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
					System.exit(0);				}
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
