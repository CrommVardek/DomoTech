package system;

import java.util.Vector;

import com.phidgets.*;

public class Main {

	public static void main(String args[]) throws Exception{
		
			InterfaceKitPhidget ifk = new InterfaceKitPhidget();
			
			//Initialize the rooms of the house
			Vector<Integer> rooms  = new Vector<Integer>(2);
			
			rooms.addElement(0);
			rooms.addElement(1);
			rooms.addElement(2);
			rooms.addElement(3);
			rooms.addElement(4);

			//Launch and init the sensors and the managers
			TempSensor.LaunchTempSensor(rooms.get(0));
			LightSensor.LaunchLightSensor(ifk, rooms.get(0));
			RFIDSensor.LaunchRFIDSensor(); 
			
	}
	
	
	
}
