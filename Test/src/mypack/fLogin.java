package mypack;
import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import model.Account;
import java.awt.Color;
import javax.swing.JLabel;
import Connection.Connect;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JPasswordField;

public class fLogin extends JFrame 
{

	private static final long serialVersionUID = 6983975855943111787L;
	private JPanel contentPane;
	private JTextField txtUserName;
	private JPasswordField passwordField;

ActionListener listener = new ActionListener() {
		
		@Override
		public void actionPerformed(ActionEvent e) 
                {
                    String username = txtUserName.getText().trim();
                    String password = new String(passwordField.getPassword());
                    if(username.length() > 0 && password.length() > 0)
                    {
                        try
                        {
                            Account user = Connect.Login(username, password);
                            if (user != null)
                            {
                                dispose();
                                fMenu menu = new fMenu();
                                menu.a = user;
                                menu.txtLoginUser.setText(user.username);
                                menu.setVisible(true);				
                                menu.setLocationRelativeTo(null);	
                            }
                            else
                            {
                                Message.messageBox("Invalid user !!!!", "Error");
                            }						
                        } catch (SQLException e1)
                        {
                            e1.printStackTrace();
                        }
                    }
		}
	};
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() 
			{
				try {
					fLogin frame = new fLogin();
					frame.setVisible(true);
					frame.setLocationRelativeTo(null);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public fLogin() {		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 567, 496);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel panel_1 = new JPanel();
		panel_1.setLayout(null);
		panel_1.setBackground(new Color(248, 148, 6));
		panel_1.setBounds(0, 0, 549, 109);
		contentPane.add(panel_1);
		
		JLabel label_1 = new JLabel("LOGIN FORM");
		label_1.setForeground(Color.WHITE);
		label_1.setFont(new Font("Times New Roman", Font.BOLD, 30));
		label_1.setBounds(185, 26, 204, 55);
		panel_1.add(label_1);
		
		JPanel panel_2 = new JPanel();
		panel_2.setBounds(0, 109, 549, 340);
		contentPane.add(panel_2);
		panel_2.setLayout(null);
		panel_2.setBackground(new Color(44, 62, 80));
		
		JLabel label_2 = new JLabel("UserName");
		label_2.setForeground(Color.WHITE);
		label_2.setFont(new Font("Times New Roman", Font.PLAIN, 23));
		label_2.setBounds(92, 69, 104, 27);
		panel_2.add(label_2);
		
		JLabel label_3 = new JLabel("Password");
		label_3.setForeground(Color.WHITE);
		label_3.setFont(new Font("Times New Roman", Font.PLAIN, 23));
		label_3.setBounds(92, 144, 104, 27);
		panel_2.add(label_3);
		
		txtUserName = new JTextField();
		txtUserName.setFont(new Font("Times New Roman", Font.PLAIN, 20));
		txtUserName.setColumns(10);
		txtUserName.setBackground(new Color(108, 122, 137));
		txtUserName.setBounds(255, 64, 208, 27);
		panel_2.add(txtUserName);
		
		JButton button = new JButton("LOGIN");
		button.setFont(new Font("Times New Roman", Font.PLAIN, 25));
		button.setBounds(328, 209, 135, 37);
		panel_2.add(button);
		button.addActionListener(listener);
		
		passwordField = new JPasswordField();
		passwordField.setFont(new Font("Times New Roman", Font.PLAIN, 20));
		passwordField.setBackground(new Color(108, 122, 137));
		passwordField.setBounds(255, 144, 208, 27);
		panel_2.add(passwordField);
		
		JLabel lblThmTiKhon = new JLabel("Add Users");
		lblThmTiKhon.setForeground(Color.WHITE);
		lblThmTiKhon.setFont(new Font("Times New Roman", Font.ITALIC, 25));
		lblThmTiKhon.setBounds(223, 266, 117, 27);
		panel_2.add(lblThmTiKhon);
		lblThmTiKhon.addMouseListener(new MouseAdapter()  
		{  
		    public void mouseClicked(MouseEvent e)  
		    {  		      
		    	dispose();
		    	fAddusers add = new fAddusers();
		    	add.setVisible(true);
		    	add.setLocationRelativeTo(null);
		    }  
		}); 
	}
}
