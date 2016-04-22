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

import features.HeatManager;

public class TempSensor {

	protected static void LaunchTempSensor(InterfaceKitPhidget IFK, int room) throws PhidgetException{
				
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

		
		//Get Light Value (port number 2)
		int heatValue = IFK.getSensorValue(2);
				
		//Convert temp
		double roomtemp = Math.round(((heatValue * 0.22222) - 61.11));
		
		HeatManager hm = new HeatManager(room, roomtemp);
				
		//Lis les données toutes les 100ms.
		IFK.setDataRate(1, 100);

		IFK.addSensorChangeListener(new SensorChangeListener()
		{
			public void sensorChanged(SensorChangeEvent oe)
				{
					try {
						
						int heatVal = IFK.getSensorValue(2);
						double roomTemp = Math.round(((heatVal * 0.22222) - 61.11));
						hm.onChangingTemp(roomTemp);
						System.out.println(oe);
						} catch (PhidgetException e) {
							e.printStackTrace();
						}
					}
				});
		
	}
	
}
