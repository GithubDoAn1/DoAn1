package model;
public class Permission 
{
    public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public int getUserid() {
		return userid;
	}
	public void setUserid(int userid) {
		this.userid = userid;
	}
	public String getPer() {
		return per;
	}
	public void setPer(String per) {
		this.per = per;
	}
	public String path;
    public int userid;
    public String per;
}
