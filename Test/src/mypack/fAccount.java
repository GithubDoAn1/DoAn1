package mypack;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JTextField;
import java.awt.SystemColor;
import java.util.ArrayList;
import java.util.List;
import model.Account;
import Connection.Connect;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
public class fAccount extends JFrame 
{
	private static final long serialVersionUID = 2492702324135442989L;
	private JPanel contentPane;
	public JTextField txtMenuAUser;
        public boolean isRoot;
        public static String username = "";
        public static Account a = new Account();
        List<Account> acc = new ArrayList();
		public static void main(String[] args) 
	        {
	            EventQueue.invokeLater(new Runnable() 
	            {
	                public void run() 
	                {
	                    try 
	                    {
	                        fAccount frame = new fAccount();
	                        frame.setVisible(true);
	                    } catch (Exception e) {e.printStackTrace();}
	                }
	            });
		}

		public fAccount() throws SQLException 
        {
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setBounds(100, 100, 619, 455);
            contentPane = new JPanel();
            contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
            setContentPane(contentPane);
            contentPane.setLayout(new FlowLayout());
            
            JButton button = new JButton("Exit");
            button.addActionListener(new ActionListener() {
            	public void actionPerformed(ActionEvent arg0) {
            		dispose();
                    fMenu menu = new fMenu();
                    menu.a = a;
                    menu.txtLoginUser.setText(a.username);
                    menu.setVisible(true);
                    menu.setLocationRelativeTo(null);
            	}
            });
            button.setForeground(Color.BLACK);
            button.setFont(new Font("Calibri", Font.PLAIN, 20));
            button.setBackground(Color.WHITE);
            contentPane.add(button);
            txtMenuAUser = new JTextField();
            txtMenuAUser.setEnabled(false);
            txtMenuAUser.setFont(new Font("Century Gothic", Font.ITALIC, 18));
            txtMenuAUser.setBackground(SystemColor.controlHighlight);
            txtMenuAUser.setBounds(10, 11, 110, 25);
            contentPane.add(txtMenuAUser);
            txtMenuAUser.setColumns(10);
			addWindowListener(new WindowAdapter() 
            {
                @Override
                public void windowOpened(WindowEvent arg0)
                {
                    txtMenuAUser.setText(username);
                }
            });
            try 
            {
                acc = Connect.GetUsers();
            } catch (SQLException ex) {
                Logger.getLogger(fAccount.class.getName()).log(Level.SEVERE, null, ex);
            }
            for (Account p : acc)
            {
                if (!p.role.equals("root") && !p.username.equals(username))
                {
                    JLabel lblNewLabel = new JLabel(p.username);
                    lblNewLabel.setFont(new Font("Calibri", Font.PLAIN, 25));
                    lblNewLabel.setHorizontalAlignment(SwingConstants.LEFT);
                    lblNewLabel.setIcon(new ImageIcon(fAccount.class.getResource("/resources/businessman_50px.png")));
                    lblNewLabel.setBounds(218, 106, 173, 59);
                    lblNewLabel.setBorder(BorderFactory.createLineBorder(Color.black, 2));
                    lblNewLabel.addMouseListener(new MouseAdapter() 
                    {
                        @Override
                        public void mouseClicked(MouseEvent e)
                        {
                            dispose();
                            Account p1 = new Account();
                            try 
                            {
                                p1 = Connect.GetUser(lblNewLabel.getText());
                                FileBrowser.Authorize = a;
                                FileBrowser.Authorized = p1;
                                FileBrowser.main(null);                            
                            } catch (SQLException ex) {Logger.getLogger(fAccount.class.getName()).log(Level.SEVERE, null, ex);}
                        }
                    });
                    contentPane.add(lblNewLabel);
                }
            }
		}
}
