package be.unamur.commonsObjects;

public class Parameter implements Container {
	
	private static final long serialVersionUID = 4198459762318633180L;
	private String key;
	private String param;
	
/* CONSTRUCTEURS */
	
	public Parameter (String key, String param)
	{
		this.key = key;
		this.param = param;
	}
	
	public Parameter (String key)
	{
		this(key, "Empty");
	}
	
	public Parameter()
	{
		this("Empty", "Empty");
	}
	
	
/* GETTERS AND SETTERS */
	
 // GETTERS
	
	public String getKey()
	{ 
		return this.key;
	}
	
	public String getParam()
	{
		return this.param;
	}
	
 // SETTERS
	
	public void setKey(String key)
	{
		this.key = key;
	}
	
	public void setParam(String param)
	{
		this.param = param;
	}
	

}
