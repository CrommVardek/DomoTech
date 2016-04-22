package commonsObjects;

public class Wrapper {
	
	private Request req;
	private Container container;
	private String string;
	
/* CONSTRUCTEURS */
	
	public Wrapper(Request req, Container container, String string)
	{
		this.req = req;
		this.container = container;
		this.string = string;
		
	}
	
	public Wrapper (Request req, Container container)
	{
		this(req, container, "Empty");
	}
	
	public Wrapper(Request req)
	{
		this(req, null, "Empty");
	}
	
/* GETTERS AND SETTERS */
	
 // GETTERS
	public Request getRequest()
	{
		return req;
	}
	
	public Container getContainer()
	{
		return container;
	}
	
	public String getString()
	{
		return string;
	}
 
 // SETTERS
	public void setRequest( Request request)
	{
		this.req = request; 
	}

	public void setContainer(Container container)
	{
		this.container = container;
	}

	public void setString (String string)
	{
		this.string = string;
	}


/* METHODE TOSTRING */
	
	public String toString()
	{
		return "Wrapper [req=" + req + ", containerClass=" + container.getClass().getName()+ ", getClass()=" + getClass() + ", hashCode()="
				+ hashCode() + ", toString()=" + super.toString() + "]";
	}
 
}
