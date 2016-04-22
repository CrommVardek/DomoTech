package be.unamur.commonsObjects;

import java.sql.Time;

/*
 * Dorian Lecomte
 */

public class Agenda implements Container {

/* VARIABLES */
	private static final long serialVersionUID = -18177855161331855L;
	private String dayId;
	private String roomId;
	private String actionId;
	private Time hour;
	
/* CONSTRUCTEURS */
	public Agenda(String dayId, String roomId, String actionId, Time hour)
	{
		this.dayId = dayId;
		this.roomId = roomId;
		this.actionId = actionId;
		this.hour = hour;
	}
	
	public Agenda()
	{
		this("0", "Empty", "Empty", new Time(0));
	}
	
/* GETTERS ET SETTERS */ 
	
 // GETTERS
	public String getDayId()
	{
		return dayId;
	}
	
	public String getRoomId()
	{
		return roomId;
	}
	
	public String getActionId()
	{
		return actionId;
	}
	
	public Time getHour()
	{
		return hour;
	}
	
 // SETTERS
	public void setDayId(String dayId)
	{
		this.dayId = dayId;
	}
	
	public void setRoomId(String roomId)
	{
		this.roomId = roomId;
	}
	
	public void setActionId(String actionId)
	{
		this.actionId = actionId;
	}
	
	public void setHour(Time hour)
	{
		this.hour = hour;
	}

 //TOSTRING
	public String toString() {
		return "Agenda [" + (dayId != null ? "dayId=" + dayId + ", " : "")
				+ (roomId != null ? "roomId=" + roomId + ", " : "")
				+ (actionId != null ? "actionId=" + actionId + ", " : "") + (hour != null ? "hour=" + hour : "") + "]";
	}
	
	

}
