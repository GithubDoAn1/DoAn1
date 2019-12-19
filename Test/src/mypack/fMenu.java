package mypack;
import mypack.fChangePass;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import model.Account;
import mypack.fAccount;
import mypack.fLogin;

public class fMenu extends JFrame 
{
        public Account a = new Account();
	private static final long serialVersionUID = 5171778317075624109L;
	private JPanel contentPane;
	public JTextField txtLoginUser;
	public static void main(String[] args) 
        {
            EventQueue.invokeLater(new Runnable() 
            {
                public void run() 
                {
                    try 
                    {
                        fMenu frame = new fMenu();
                        frame.setVisible(true);
                    } catch (Exception e) {e.printStackTrace();}
                }
            });
	}
	public fMenu() 
        {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 581, 477);
		contentPane = new JPanel();
		contentPane.setBackground(SystemColor.inactiveCaption);
		contentPane.setForeground(SystemColor.activeCaptionText);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		JButton btnNewButton = new JButton("Account");
		btnNewButton.addActionListener((ActionEvent e) -> 
                {
                    dispose();
                    try
                    {
                        fAccount.username = a.username;
                        fAccount.a = a;
                        fAccount ac = new fAccount();
                        ac.isRoot = a.userid == 1;
                        ac.setVisible(true);
                        ac.setLocationRelativeTo(null);
                    } catch (SQLException ex) {Logger.getLogger(fMenu.class.getName()).log(Level.SEVERE, null, ex);}
                });
		btnNewButton.setFont(new Font("Calibri", Font.PLAIN, 20));
		btnNewButton.setBounds(187, 98, 180, 35);
		contentPane.add(btnNewButton);		
		JButton btnFolder = new JButton("Folder");
		btnFolder.addActionListener(new ActionListener() 
                {
                    public void actionPerformed(ActionEvent arg0) 
                    {
                        dispose();
                        FileBrowser.Authorize = a;
                        FileBrowser.Authorized = a;
                        FileBrowser.main(null);	
                    }
		});
		btnFolder.setFont(new Font("Calibri", Font.PLAIN, 20));
		btnFolder.setBounds(187, 179, 180, 35);
		contentPane.add(btnFolder);		
		JButton btnChangePassword = new JButton("Change Password");
		btnChangePassword.addActionListener(new ActionListener() 
                {
                    public void actionPerformed(ActionEvent arg0)
                    {
                        dispose();
                        fChangePass chP = new fChangePass();
                        chP.txtMenuCPUser.setText(txtLoginUser.getText());
                        chP.a = a;
                        chP.setVisible(true);
                        chP.setLocationRelativeTo(null);
                    }
		});
		btnChangePassword.setFont(new Font("Calibri", Font.PLAIN, 20));
		btnChangePassword.setBounds(187, 261, 180, 35);
		contentPane.add(btnChangePassword);
		
		JLabel lblNewLabel = new JLabel("Menu");
		lblNewLabel.setForeground(Color.WHITE);
		lblNewLabel.setFont(new Font("Calibri", Font.BOLD, 35));
		lblNewLabel.setBounds(236, 29, 92, 36);
		contentPane.add(lblNewLabel);
		
		JButton btnLogout = new JButton("Logout");
		btnLogout.addActionListener(new ActionListener() 
                {
			public void actionPerformed(ActionEvent arg0) 
                        {
                            dispose();
                            fLogin lg = new fLogin();
                            lg.setVisible(true);
                            lg.setLocationRelativeTo(null);
			}
		});
		btnLogout.setFont(new Font("Calibri", Font.PLAIN, 20));
		btnLogout.setBounds(187, 342, 180, 35);
		contentPane.add(btnLogout);
		
		txtLoginUser = new JTextField();
		txtLoginUser.setEnabled(false);
		txtLoginUser.setFont(new Font("Century Gothic", Font.ITALIC, 18));
		txtLoginUser.setBackground(SystemColor.controlHighlight);
		txtLoginUser.setBounds(10, 11, 110, 25);
		contentPane.add(txtLoginUser);
		txtLoginUser.setColumns(10);
	}
}
