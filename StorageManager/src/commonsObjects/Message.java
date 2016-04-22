package commonsObjects;

public class Message implements Container {
	
	private static final long serialVersionUID = -816509686823430272L;
	private String message;

/* CONSTRUCTEURS */
	public Message (String msg)
	{
		this.message = msg;
	}
	
	public Message()
	{
		this("Empty");
	}
	
/* GETTERS AND SETTERS */

	public void setMessage(String message)
	{
		this.message = message;
	}
	
	public String getMessage()
	{
		return this.message;
	}
}
