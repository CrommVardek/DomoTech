package features;

public class HeatManager {

	int room;
	
	//In Celsius
	double sensorValue;
	
	//heat = true if the room temperature is too low, false else
	boolean heat;
	//difference between desired temperature and actual temperature of the room
	int increase;
	//heaterOn = true 
	boolean heaterOn;
	
	public HeatManager(int room, double value){
		this.room = room;
		this.sensorValue = value;
		this.heat = false;
		this.increase = 0;
		this.heaterOn = false;
	}
	
	
}
