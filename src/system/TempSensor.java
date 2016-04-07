package system;

import com.phidgets.PhidgetException;
import com.phidgets.TemperatureSensorPhidget;
import com.phidgets.event.AttachEvent;
import com.phidgets.event.AttachListener;
import com.phidgets.event.DetachEvent;
import com.phidgets.event.DetachListener;
import com.phidgets.event.ErrorEvent;
import com.phidgets.event.ErrorListener;
import com.phidgets.event.TemperatureChangeEvent;
import com.phidgets.event.TemperatureChangeListener;

import features.HeatManager;

public class TempSensor {

	protected static void LaunchTempSensor(int room) throws PhidgetException{
		
		TemperatureSensorPhidget tempsensor;
		
		tempsensor = new TemperatureSensorPhidget();
		
		tempsensor.addAttachListener(new AttachListener() {
			public void attached(AttachEvent ae) {
				System.out.println("attachment of " + ae);
			}
		});
		
		tempsensor.addDetachListener(new DetachListener() {
			public void detached(DetachEvent ae) {
				System.out.println("detachment of " + ae);
			}
		});
		
		tempsensor.addErrorListener(new ErrorListener() {
			public void error(ErrorEvent ee) {
				System.out.println("error event for " + ee);
			}
		});

		tempsensor.openAny();
		
		tempsensor.waitForAttachment();
		
		tempsensor.setTemperatureChangeTrigger(0, 0.1);
		
		HeatManager hm = new HeatManager(room, tempsensor.getTemperatureChangeTrigger(0));
		
		//Lors d'un changement de température, le manager prend le relais.
		tempsensor.addTemperatureChangeListener(new TemperatureChangeListener()
		{
			public void temperatureChanged(TemperatureChangeEvent oe)
			{
				try {
					hm.onChangingTemp(tempsensor.getTemperatureChangeTrigger(0));
				} catch (PhidgetException e) {
					e.printStackTrace();
				}
				
				System.out.println(oe);
			}
		});
		
	}
	
}
