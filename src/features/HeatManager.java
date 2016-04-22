package features;

public class HeatManager {

	int room;
	
	//Temp in Celsius
	double sensorValue;
	
	//Temp desired in Celsius
	double desiredTemp;
		
	//heaterOn = true 
	boolean heaterOn;
	
	//Power of the heater (0-5)
	int power;
	
	public HeatManager(int room, double value){
		this.room = room;
		this.sensorValue = value;
		this.desiredTemp = value;
		this.heaterOn = false;
		power = 0;
	}
	
	public void increaseTemp(double desiredTemp){
		
		this.desiredTemp = desiredTemp;
		if(!heaterOn && desiredTemp>sensorValue){
			turnOn();
			power = poweredValue(desiredTemp-sensorValue);
		}
		
	}
	
	private int poweredValue(double diff){
		//If the desired temp is lower than the actual temp, then, diff is negative
		if (diff < 0.0){
			return 0;
		}
		
		//Heater Power needed to efficiently heat the room. May differt depending on the heaters of the room, the room configuration and the surface of the room.
		if (diff < 1.5){
			return 1;
		}
		else{
			if (diff < 2.5){
				return 2;
			}
			else{
				if (diff < 3.0){
					return 3;
				}
				else {
					if (diff < 4.0){
						return 4;
					}
					else {
						return 5;
					}
				}
			}
		}
	}
	
	public void turnOn(){
		heaterOn = true;
		System.out.println("The heater is turned on");
	}
	
	public void turnOff(){
		heaterOn = false;
		System.out.println("The heater is turned off");
	}
	
	public double getDesiredTemp(){
		return desiredTemp;
	}
	
	public void onChangingTemp(double newTemp){
		
		sensorValue = newTemp;
		if (newTemp>desiredTemp){
			turnOff();
			System.out.println("The new temperature is:" + newTemp + "°C");
			power = 0;
			System.out.println("Power is at " + power);
		}
		else{
			turnOn();
			power = poweredValue(desiredTemp-sensorValue);
			System.out.println("The new temperature is:" + newTemp + "°C");
			System.out.println("Power is at " + power);
		}
	}
	
}
