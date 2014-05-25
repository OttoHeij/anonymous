import java.net.Socket;

public class User
{
	private String phoneNumber;
	public User(String phoneNumber)
	{
		super();
		this.phoneNumber = phoneNumber;
	}

	public String getPhoneNumber()
	{
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber)
	{
		this.phoneNumber = phoneNumber;
	}
	@Override
	public boolean equals(Object obj)
	{
		// TODO Auto-generated method stub
		return this.phoneNumber.equals(((User)obj).getPhoneNumber());
	}
}
