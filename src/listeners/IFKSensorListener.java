package listeners;

import com.phidgets.event.SensorChangeEvent;
import com.phidgets.event.SensorChangeListener;

public class IFKSensorListener implements SensorChangeListener{

	public IFKSensorListener(){
		
	}

	public void sensorChanged(SensorChangeEvent sce) {
		System.out.println("Index = " + sce.getIndex());
		System.out.println("changing sensor val: " + sce);
	}
	
}
