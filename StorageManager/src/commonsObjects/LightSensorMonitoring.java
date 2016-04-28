package commonsObjects;

import java.sql.Timestamp;

/* 
 * Dorian Lecomte
 */

public class LightSensorMonitoring implements Container {

/* VARIABLES */
	private static final long serialVersionUID = 1716655932610702676L;
	private Timestamp create_time; // Crée en bdd lors de l'insertion.
	private String outsideLight;
	private String insideLight;
	private String roomId;
	
/* CONSTRUCTEURS */	
	
	public LightSensorMonitoring(Timestamp create_time, String outsideLight, String insideLight, String roomId) {
		this.create_time = create_time;
		this.outsideLight = outsideLight;
		this.insideLight = insideLight;
		this.roomId = roomId;
	}
	
	public LightSensorMonitoring()
	{
		this(new Timestamp(0), "0", "0", "1");
	}


/* GETTERS AND SETTERS */
	
 // GETTERS
	public Timestamp getCreate_time()
	{
		return create_time;
	}

	public String getOutsideLight()
	{
		return outsideLight;
	}

	public String getInsideLight()
	{
		return insideLight;
	}

	public String getRoomId()
	{
		return roomId;
	}

 // SETTERS 
	public void setCreate_time(Timestamp create_time)
	{
		this.create_time = create_time;
	}

	public void setOutsideLight(String outsideLight)
	{
		this.outsideLight = outsideLight;
	}

	public void setInsideLight(String insideLight)
	{
		this.insideLight = insideLight;
	}

	public void setRoom(String roomId)
	{
		this.roomId = roomId;
	}
	
/* TOSTRING */
	
	public String toString() {
		return "LightSensorMonitoring [" + (create_time != null ? "create_time=" + create_time + ", " : "")
				+ (outsideLight != null ? "outsideLight=" + outsideLight + ", " : "")
				+ (insideLight != null ? "insideLight=" + insideLight + ", " : "")
				+ (roomId != null ? "room=" + roomId : "") + "]";
	}
	
	

	
	

}
