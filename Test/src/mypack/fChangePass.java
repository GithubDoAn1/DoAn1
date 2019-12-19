package mypack;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import model.Account;
public class fChangePass extends JFrame 
{
	private static final long serialVersionUID = 1L;
        public Account a = new Account();
	private JPanel contentPane;
	private JPasswordField txtNewPass;
	private JPasswordField txtConfirmPass;
	public JTextField txtMenuCPUser;
	PreparedStatement ps;
	private JPasswordField txtCurPass;
	public static void main(String[] args)
	{
            EventQueue.invokeLater(new Runnable()
            {
                public void run()
                {
                    try
                    {
                        fChangePass frame = new fChangePass();
                        frame.setVisible(true);
                    } catch (Exception e) { e.printStackTrace(); }
                }
            });
	}
	public fChangePass()
	{
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 567, 496);
		contentPane = new JPanel();
		contentPane.setBackground(SystemColor.inactiveCaption);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);	
		JLabel lblChangePassword = new JLabel("CHANGE PASSWORD");
		lblChangePassword.setForeground(SystemColor.window);
		lblChangePassword.setFont(new Font("Century Gothic", Font.BOLD, 35));
		lblChangePassword.setBounds(104, 45, 366, 36);
		contentPane.add(lblChangePassword);		
		JLabel lblCurrentpass = new JLabel("Current Pass");
		lblCurrentpass.setForeground(Color.WHITE);
		lblCurrentpass.setFont(new Font("Century Gothic", Font.ITALIC, 20));
		lblCurrentpass.setBounds(65, 153, 127, 27);
		contentPane.add(lblCurrentpass);
		
		JLabel lblNewpass = new JLabel("New Pass");
		lblNewpass.setForeground(Color.WHITE);
		lblNewpass.setFont(new Font("Century Gothic", Font.ITALIC, 20));
		lblNewpass.setBounds(65, 226, 104, 27);
		contentPane.add(lblNewpass);
		
		JLabel lblEnteranewpass = new JLabel("Confirm Pass");
		lblEnteranewpass.setForeground(Color.WHITE);
		lblEnteranewpass.setFont(new Font("Century Gothic", Font.ITALIC, 20));
		lblEnteranewpass.setBounds(65, 301, 174, 27);
		contentPane.add(lblEnteranewpass);
		txtNewPass = new JPasswordField("");
		txtNewPass.setFont(new Font("Times New Roman", Font.PLAIN, 20));
		txtNewPass.setBackground(new Color(108, 122, 137));
		txtNewPass.setBounds(249, 226, 223, 27);
		contentPane.add(txtNewPass);		
		txtConfirmPass = new JPasswordField();
		txtConfirmPass.setFont(new Font("Times New Roman", Font.PLAIN, 20));
		txtConfirmPass.setBackground(new Color(108, 122, 137));
		txtConfirmPass.setBounds(249, 301, 223, 27);
		contentPane.add(txtConfirmPass);	
		JButton btnChange = new JButton("Change");
		btnChange.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e)
			{
	                String userName = txtMenuCPUser.getText();
	                String curPass = new String(txtCurPass.getPassword());
	                String newPass = new String(txtNewPass.getPassword());
	                String confirmPass = new String(txtConfirmPass.getPassword());
	                String query = "UPDATE account SET password = '"+newPass+"' WHERE username = '"+userName+"'";
	                try
	                {
	                    String dbURL = "jdbc:sqlserver://DESKTOP-93BVIUJ\\MSSQLSERVER:1433;databaseName=doan1;user=sa;password=1234";
	                    Connection conn = DriverManager.getConnection(dbURL);
	                    ps = conn.prepareStatement(query);
	                    if(curPass.equals("") && newPass.equals("") && confirmPass.equals(""))
	                            JOptionPane.showMessageDialog(null, "All information is empty!");
	                    else if(curPass.equals(""))
	                            JOptionPane.showMessageDialog(null, "Please enter your current password");
	                    else if(newPass.equals(""))
	                            JOptionPane.showMessageDialog(null, "Please enter new password");
	                    else if(confirmPass.equals(""))
	                            JOptionPane.showMessageDialog(null, "Please enter confirm a password");
	                    else
	                    {
	                        if(checkUserNameAndPass(userName, curPass))
	                        {
	                            if(newPass.equals(curPass)) JOptionPane.showMessageDialog(null, "The password is used choose another password!");
	                            else
	                            {
	                                if(newPass.equals(confirmPass))
	                                {
	                                    ps.executeUpdate();
	                                    JOptionPane.showMessageDialog(null, "Change password successfully!");
	                                    dispose();
	                                    fMenu menu = new fMenu();
	                                    menu.setVisible(true);
	                                    menu.setLocationRelativeTo(null);
	                                    conn.close();
	                                    ps.close();
	                                }
	                                else JOptionPane.showMessageDialog(null, "Password not match!");
	                            }
	                        }
	                        else JOptionPane.showMessageDialog(null, "The password current is false!");
	                    }					
	               } catch (SQLException ex) { }
			}
		});
		
		btnChange.setFont(new Font("Century Gothic", Font.PLAIN, 18));
		btnChange.setBounds(249, 369, 110, 35);
		contentPane.add(btnChange);
		
		txtMenuCPUser = new JTextField();
		txtMenuCPUser.setEnabled(false);
		txtMenuCPUser.setFont(new Font("Century Gothic", Font.ITALIC, 18));
		txtMenuCPUser.setColumns(10);
		txtMenuCPUser.setBackground(SystemColor.controlHighlight);
		txtMenuCPUser.setBounds(10, 11, 110, 25);
		contentPane.add(txtMenuCPUser);	
		JButton btnCancle = new JButton("Cancel");
		btnCancle.addActionListener(new ActionListener() 
		{
	        public void actionPerformed(ActionEvent arg0)
	        {
	            dispose();
	            fMenu menu = new fMenu();
	            menu.a = a;
	            menu.txtLoginUser.setText(a.username);
	            menu.setVisible(true);
	            menu.setLocationRelativeTo(null);
	        }
		});
		btnCancle.setFont(new Font("Century Gothic", Font.PLAIN, 18));
		btnCancle.setBounds(360, 369, 110, 35);
		contentPane.add(btnCancle);
		
		txtCurPass = new JPasswordField("");
		txtCurPass.setFont(new Font("Times New Roman", Font.PLAIN, 20));
		txtCurPass.setBackground(new Color(108, 122, 137));
		txtCurPass.setBounds(249, 153, 223, 27);
		contentPane.add(txtCurPass);
	}	
	public boolean checkUserNameAndPass(String user, String pass) throws SQLException
	{
            return a.pass.equals(pass);
	}
}
