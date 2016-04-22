package listeners;

import com.phidgets.event.SensorChangeEvent;
import com.phidgets.event.SensorChangeListener;

import features.HeatManager;
import features.LightManager;

public class IFKSensorListener implements SensorChangeListener{

	private HeatManager hm;
	private LightManager lm;
	
	public IFKSensorListener(HeatManager hM, LightManager lM){
		this.hm = hM;
		this.lm = lM;
	}

	public void sensorChanged(SensorChangeEvent sce) {
		
		switch (sce.getIndex()){
			case 1: 
				int val = sce.getValue();
				lm.onChangeLight(val);
				lm.turnOnLeds();
				break;
			case 2: 
				//Convert temp
				int val2 = sce.getValue();
				double roomtemp = Math.round(((val2 * 0.22222) - 61.11));
				hm.onChangingTemp(roomtemp);
				System.out.println(hm.getDesiredTemp());
				break;
			default: 
				System.out.println("Index = " + sce.getIndex());
				System.out.println("changing sensor val: " + sce);
				break;
		}
	}
	
		
}
