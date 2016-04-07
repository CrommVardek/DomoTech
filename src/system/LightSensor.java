package system;

import com.phidgets.InterfaceKitPhidget;
import com.phidgets.PhidgetException;
import com.phidgets.event.AttachEvent;
import com.phidgets.event.AttachListener;
import com.phidgets.event.DetachEvent;
import com.phidgets.event.DetachListener;
import com.phidgets.event.ErrorEvent;
import com.phidgets.event.ErrorListener;
import com.phidgets.event.SensorChangeEvent;
import com.phidgets.event.SensorChangeListener;

import features.LightManager;

public class LightSensor {
	
	protected static void LaunchLightSensor(InterfaceKitPhidget IFK, int room) throws PhidgetException{
		
				
		IFK.addAttachListener(new AttachListener() {
			public void attached(AttachEvent ae) {
				System.out.println("attachment of " + ae);
			}
		});
		
		IFK.addDetachListener(new DetachListener() {
			public void detached(DetachEvent ae) {
				System.out.println("detachment of " + ae);
			}
		});
		
		IFK.addErrorListener(new ErrorListener() {
			public void error(ErrorEvent ee) {
				System.out.println("error event for " + ee);
			}
		});
		
		
		//Get Light Value (port number 1)
		int lightValue = IFK.getSensorValue(1);
		
		//TODO create lightmanager
		LightManager lm = new LightManager(room, lightValue);
		
		//Lis les données toutes les 100ms.
		IFK.setDataRate(1, 100);

		IFK.addSensorChangeListener(new SensorChangeListener()
		{
			public void sensorChanged(SensorChangeEvent oe)
			{
				try {
					
					//TODO
					int lightVal = IFK.getSensorValue(1);
					lm.onChangeLight(lightVal);
					System.out.println(oe);
				} catch (PhidgetException e) {
					e.printStackTrace();
				}
			}
		});
		
		/*
		 * LEDs Output:
		 * 01 - 1 ON 
		 * 23 - 3 ON
		 * 45 - 5 ON
		 * 67 - 7 ON	
		 */		
		
	}

}
