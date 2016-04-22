package commonsObjects;

public class Room implements Container {

/* VARIABLES */	
	private static final long serialVersionUID = 1L;
	private String roomId;
	private String name;
	private String description;
	
/* CONSTRUCTUEURS */
	
	public Room(String roomId, String name, String description) {
		this.roomId = roomId;
		this.name = name;
		this.description = description;
	}
	
	public Room()
	{
		this("Empty_Id", "Empty_Name", "Empty_Description");
	}

/* GETTERS ET SETTERS */
	
 // GETTERS
	public String getRoomId() {
		return roomId;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

 // SETTERS
	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setDescription(String description) {
		this.description = description;
	}

/* TOSTRING */
	public String toString() {
		return "Room [" + (roomId != null ? "roomId=" + roomId + ", " : "")
				+ (name != null ? "name=" + name + ", " : "")
				+ (description != null ? "description=" + description : "") + "]";
	}
	
	
	
	
}
