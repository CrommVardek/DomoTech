package be.unamur.commonsObjects;

import java.sql.Timestamp;

/*
 * Dorian Lecomte
 */

public class TemperatureSensorMonitoring  implements Container{

/* VARIABLES */
	private static final long serialVersionUID = -8455019148851572674L;
	private Timestamp create_time;
	private String temperature;
	private String roomId;
	private boolean heatingState;
	
/* CONSTRUCTEURS */	
	public TemperatureSensorMonitoring(Timestamp create_time, String temperature, String roomId, boolean heatingState) {
		this.create_time = create_time;
		this.temperature = temperature;
		this.roomId = roomId;
		this.heatingState = heatingState;
	}
	
	public TemperatureSensorMonitoring()
	{
		this(new Timestamp(0), "Empty_Temperature", "0", false);
	}

/* GETTERS ET SETTERS */
	
 // GETTERS
	public Timestamp getCreate_time()
	{
		return create_time;
	}

	public String getTemperature() 
	{
		return temperature;
	}

	public String getRoomId()
	{
		return roomId;
	}

	public boolean getHeatingState()
	{
		return heatingState;
	}

 // SETTERS	
	public void setCreate_time(Timestamp create_time)
	{
		this.create_time = create_time;
	}

	public void setTemperature(String temperature) 
	{
		this.temperature = temperature;
	}

	public void setRoomId(String roomId)
	{
		this.roomId = roomId;
	}

	public void setHeatingState(boolean heatingState) 
	{
		this.heatingState = heatingState;
	}

/* TOSTRING */
	public String toString()
	{
		return "TemperatureSensorMonitoring [" + (create_time != null ? "create_time=" + create_time + ", " : "")
				+ (temperature != null ? "temperature=" + temperature + ", " : "")
				+ (roomId != null ? "room=" + roomId + ", " : "") + "heatingState=" + heatingState + "]";
	}
	
	
	
	

}
