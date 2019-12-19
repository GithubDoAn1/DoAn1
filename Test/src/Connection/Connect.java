package Connection;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Account;
import model.Permission;
public class Connect 
{
    public static void WriteFolder(String path, String name) throws SQLException
    {
        String dbURL = "jdbc:sqlserver://DESKTOP-93BVIUJ\\MSSQLSERVER:1433;databaseName=doan1;user=sa;password=1234";
        try (Connection conn = DriverManager.getConnection(dbURL)) 
        {
            PreparedStatement stmt = conn.prepareStatement("exec spWriteFolder ?,?");
            stmt.setString(1, path);
            stmt.setString(2, name);
            stmt.execute();
            stmt.close();
            conn.close();
        }
    }
    public static void InsertPermission(String path, int id, String per) throws SQLException
    {
        String dbURL = "jdbc:sqlserver://DESKTOP-93BVIUJ\\MSSQLSERVER:1433;databaseName=doan1;user=sa;password=1234";
        try (Connection conn = DriverManager.getConnection(dbURL)) 
        {
            PreparedStatement stmt = conn.prepareStatement("exec spWritePer ?,?,?");
            stmt.setString(1, path);
            stmt.setInt(2, id);
            stmt.setString(3, per);
            stmt.execute();
            stmt.close();
            conn.close();
        }
    }
    public static void InitPermission(int id) throws SQLException
    {
        String dbURL = "jdbc:sqlserver://DESKTOP-93BVIUJ\\MSSQLSERVER:1433;databaseName=doan1;user=sa;password=1234";
        try (Connection conn = DriverManager.getConnection(dbURL)) 
        {
            PreparedStatement stmt = conn.prepareStatement("exec spInitPer ?");
            stmt.setInt(1, id);
            stmt.execute();
            stmt.close();
            conn.close();
        }
    }
    public static List<Permission> GetPer (int userid) throws SQLException
    {
        List<Permission> per = new ArrayList<>();
        String dbURL = "jdbc:sqlserver://DESKTOP-93BVIUJ\\MSSQLSERVER:1433;databaseName=doan1;user=sa;password=1234";
        try (Connection conn = DriverManager.getConnection(dbURL)) 
        {
            PreparedStatement stmt = conn.prepareStatement("select * from dbo.managementfolder where userid = ?");
            stmt.setInt(1, userid);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) 
            {
                Permission p = new Permission();
                p.path = rs.getString("folderid");
                p.userid = rs.getInt("userid");
                p.per = rs.getString("permission");
                per.add(p);
            }
            stmt.close();
            conn.close();
        }
        return per;
    }
    public static Account Login (String username, String pass) throws SQLException
    {
        Account account = null;
        String dbURL = "jdbc:sqlserver://DESKTOP-93BVIUJ\\MSSQLSERVER:1433;databaseName=doan1;user=sa;password=1234";
        try (Connection conn = DriverManager.getConnection(dbURL)) 
        {
            PreparedStatement stmt = conn.prepareStatement("select * from dbo.account where username = ? and password = ?");
            stmt.setString(1, username);
            stmt.setString(2, pass);
            ResultSet rs = stmt.executeQuery();
            if (rs != null)
            {
                while (rs.next()) 
                {
                    account = new Account();
                    account.userid = rs.getInt("id");
                    account.username = rs.getString("username");
                    account.pass = rs.getString("password");
                    account.name = rs.getString("fullname");
                    account.role = rs.getString("role");
                    account.per = GetPer(account.userid);
                }
            } 
            stmt.close();
            conn.close();
            return account;
        }      
    }
    public static Account GetUser (String username) throws SQLException
    {
        Account account = null;
        String dbURL = "jdbc:sqlserver://DESKTOP-93BVIUJ\\MSSQLSERVER:1433;databaseName=doan1;user=sa;password=1234";
        try (Connection conn = DriverManager.getConnection(dbURL)) 
        {
            PreparedStatement stmt = conn.prepareStatement("select * from dbo.account where username = ?");
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs != null)
            {
                while (rs.next()) 
                {
                    account = new Account();
                    account.userid = rs.getInt("id");
                    account.per = GetPer(account.userid);
                }
            } 
            stmt.close();
            conn.close();
            return account;
        }      
    }
    public static List<Account> GetUsers () throws SQLException
    {
        List<Account> account = null;
        String dbURL = "jdbc:sqlserver://DESKTOP-93BVIUJ\\MSSQLSERVER:1433;databaseName=doan1;user=sa;password=1234";
        try (Connection conn = DriverManager.getConnection(dbURL)) 
        {
            PreparedStatement stmt = conn.prepareStatement("select * from dbo.account");
            ResultSet rs = stmt.executeQuery();
            if (rs != null)
            {
                account = new ArrayList<>();
                while (rs.next()) 
                {
                    Account a = new Account();
                    a.userid = rs.getInt("id");
                    a.username = rs.getString("username");
                    a.pass = rs.getString("password");
                    a.name = rs.getString("fullname");
                    a.role = rs.getString("role");
                    //a.per = GetPer(a.userid);
                    account.add(a);
                }
            } 
            stmt.close();
            conn.close();
            return account;
        }      
    }
}
