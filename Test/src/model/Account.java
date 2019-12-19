package model;
import java.util.ArrayList;
import java.util.List;
public class Account 
{
    public int getUserid() {
		return userid;
	}
	public void setUserid(int userid) {
		this.userid = userid;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPass() {
		return pass;
	}
	public void setPass(String pass) {
		this.pass = pass;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public List<Permission> getPer() {
		return per;
	}
	public void setPer(List<Permission> per) {
		this.per = per;
	}
	public int userid;
    public String username;
    public String pass;
    public String name;
    public String role;
    public List<Permission> per;
    public Account()
    {
        per = new ArrayList<>();
    }
}
