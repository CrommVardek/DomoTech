package be.unamur.commonsObjects;

public class Action implements Container {

/* VARIABLES */
	private static final long serialVersionUID = -1393313466843389173L;
	private String actionId;
	private String name;
	private String description;
	
	
/* CONSTRUCTEURS */
	public Action(String actionId, String name, String description)
	{
		this.actionId = actionId;
		this.name = name;
		this.description = description;
	}
	
	public Action()
	{
		this("0", "Empty_Name", "Empty_Description");
	}
	
/* GETTERS ET SETTERS */

 // GETTERS	
	public String getActionId()
	{
		return actionId;
	}

	public String getName()
	{
		return name;
	}

	public String getDescription()
	{
		return description;
	}

 // SETTERS	
	public void setActionId(String actionId)
	{
		this.actionId = actionId;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

 // TOSTRING
	public String toString()
	{
		return "Action [" + (actionId != null ? "actionId=" + actionId + ", " : "")
				+ (name != null ? "name=" + name + ", " : "")
				+ (description != null ? "description=" + description + ", " : "")
				+ (getClass() != null ? "getClass()=" + getClass() + ", " : "") + "hashCode()=" + hashCode() + "]";
	}

 


	
	
}
