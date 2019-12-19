package mypack;
import Connection.Connect;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.sql.SQLException;
import java.sql.Connection;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import java.sql.PreparedStatement;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Account;
import javax.swing.JPasswordField;
public class fAddusers extends JFrame {

	private static final long serialVersionUID = 7839342225804688799L;
	private final JPanel contentPane;
	private JTextField txtUsername;
	private JTextField txtFullname;
	private JTextField txtRole;
	PreparedStatement ps;
	private JPasswordField txtPass;
	public static void main(String[] args) 
        {
            EventQueue.invokeLater(() -> {
                try
                {
                    fAddusers frame = new fAddusers();
                    frame.setVisible(true);
                } catch (Exception e) {}
            });
	}
	public fAddusers() 
        {		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 551, 461);
		contentPane = new JPanel();
		contentPane.setBackground(SystemColor.activeCaption);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("UserName");
		lblNewLabel.setForeground(new Color(255, 255, 255));
		lblNewLabel.setFont(new Font("Calibri", Font.PLAIN, 25));
		lblNewLabel.setBounds(83, 102, 117, 21);
		contentPane.add(lblNewLabel);
		
		JLabel lblPassword = new JLabel("Password");
		lblPassword.setForeground(Color.WHITE);
		lblPassword.setFont(new Font("Calibri", Font.PLAIN, 25));
		lblPassword.setBounds(83, 159, 127, 21);
		contentPane.add(lblPassword);
		
		JLabel lblFullname = new JLabel("FullName");
		lblFullname.setForeground(Color.WHITE);
		lblFullname.setFont(new Font("Calibri", Font.PLAIN, 25));
		lblFullname.setBounds(83, 220, 127, 21);
		contentPane.add(lblFullname);
		
		JLabel lblRole = new JLabel("Role");
		lblRole.setForeground(Color.WHITE);
		lblRole.setFont(new Font("Calibri", Font.PLAIN, 25));
		lblRole.setBounds(83, 282, 62, 21);
		contentPane.add(lblRole);
		
		txtFullname = new JTextField();
		txtFullname.setFont(new Font("Calibri", Font.PLAIN, 21));
		txtFullname.setColumns(10);
		txtFullname.setBounds(244, 216, 193, 29);
		contentPane.add(txtFullname);
		
		txtRole = new JTextField();
		txtRole.setText("user");
		txtRole.setEnabled(false);
		txtRole.setFont(new Font("Calibri", Font.PLAIN, 21));
		txtRole.setColumns(10);
		txtRole.setBounds(244, 279, 193, 29);
		contentPane.add(txtRole);
		
		JLabel lblAddUsers = new JLabel("Add User");
		lblAddUsers.setForeground(Color.WHITE);
		lblAddUsers.setFont(new Font("Calibri", Font.PLAIN, 35));
		lblAddUsers.setBounds(197, 40, 146, 29);
		contentPane.add(lblAddUsers);				
		
		JButton btnAdduser = new JButton("Add");
		btnAdduser.addActionListener((ActionEvent arg0) -> {
                    String uname = txtUsername.getText();
                    String pass = new String(txtPass.getPassword());
                    String fname = txtFullname.getText();
                    String role = txtRole.getText();
                    String query = "INSERT INTO account(username, password, fullname, role) VALUES (?, ?, ?, ?)";
                    try {
                        String dbURL = "jdbc:sqlserver://DESKTOP-93BVIUJ\\MSSQLSERVER:1433;databaseName=doan1;user=sa;password=1234";
                        Connection conn = DriverManager.getConnection(dbURL);
                        ps = conn.prepareStatement(query);
                        ps.setString(1, uname);
                        ps.setString(2, pass);
                        ps.setString(3, fname);
                        ps.setString(4, role);
                        if (!checkUsername(uname)) {
                            JOptionPane.showMessageDialog(null, "Username already exists");
                        } else if (uname.equals("")) {
                            JOptionPane.showMessageDialog(null, "Please enter your username");
                        } else if (pass.equals("")) {
                            JOptionPane.showMessageDialog(null, "Please enter your password");
                        } else if (fname.equals("")) {
                            JOptionPane.showMessageDialog(null, "Please enter your fullname");
                        } else {
                            ps.execute();
                            JOptionPane.showMessageDialog(null, "Successfully add account");
                            List<Account> acc1 = new ArrayList<>();
                            acc1 = Connect.GetUsers();
                            Connect.InitPermission(acc1.get(acc1.size() - 1).userid);
                            dispose();
                            fLogin lg = new fLogin();
                            lg.setVisible(true);
                            lg.setLocationRelativeTo(null);
                            conn.close();
                            ps.close();
                        }
                    }catch (SQLException ex) { }
                });		
		
		JLabel lblusername = new JLabel("");
		lblusername.setForeground(Color.BLACK);
		lblusername.setFont(new Font("Calibri", Font.PLAIN, 18));
		lblusername.setBounds(244, 124, 193, 21);
		contentPane.add(lblusername);
		
		txtUsername = new JTextField();		
		txtUsername.addKeyListener(new KeyAdapter() {
			@Override
                    public void keyReleased(KeyEvent e) 
                    {
                        String uname = txtUsername.getText();				
                        try 
                        {
                            if(!checkUsername(uname))
                            {
                                lblusername.setText("Username is already used!");
                            }
                            else
                            {
                                lblusername.setText("");
                            }
                        } catch (SQLException ex) {
                            Logger.getLogger(fAddusers.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
		});
		
		txtUsername.setFont(new Font("Calibri", Font.PLAIN, 21));
		txtUsername.setBounds(244, 95, 193, 29);
		contentPane.add(txtUsername);
		txtUsername.setColumns(10);
		
		btnAdduser.setForeground(new Color(0, 0, 0));
		btnAdduser.setFont(new Font("Calibri", Font.PLAIN, 20));
		btnAdduser.setBackground(new Color(255, 255, 255));
		btnAdduser.setBounds(268, 336, 169, 35);
		contentPane.add(btnAdduser);
		
		JButton btnExit = new JButton("Exit");
		btnExit.addActionListener((ActionEvent arg0) -> {
                    dispose();
                    fLogin lg = new fLogin();
                    lg.setVisible(true);
                    lg.setLocationRelativeTo(null);
                });
		btnExit.setForeground(Color.BLACK);
		btnExit.setFont(new Font("Calibri", Font.PLAIN, 20));
		btnExit.setBackground(Color.WHITE);
		btnExit.setBounds(83, 336, 169, 35);
		contentPane.add(btnExit);			
		
		txtPass = new JPasswordField();
		txtPass.setBounds(244, 153, 193, 29);
		contentPane.add(txtPass);
	}

	public boolean checkUsername(String uname) throws SQLException
	{
            Account a = new Account();
            a = Connect.GetUser(uname);
            return a == null;
	}
}
