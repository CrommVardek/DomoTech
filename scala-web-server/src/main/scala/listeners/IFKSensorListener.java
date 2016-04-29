package listeners;

//import alarmeIncendie.DetecteurIncendie;

import com.phidgets.event.SensorChangeEvent;
import com.phidgets.event.SensorChangeListener;

import commonsObjects.HeatManager;
import commonsObjects.LightManager;

public class IFKSensorListener implements SensorChangeListener{

	private HeatManager hm;
	private LightManager lm;
	//private DetecteurIncendie di;
	
	public IFKSensorListener(HeatManager hM, LightManager lM/*, DetecteurIncendie di*/){
		this.hm = hM;
		this.lm = lM;
		//this.di = di;
	}

	/**
	 * Port 1: Senseur de lumière
	 * Port 2: Senseur de temp
	 * Port 3: Senseur de touché ou de force
	 */
	
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
			case 3: 
				if (sce.getValue() > 399){
					//di.switchAlarmOff();
				}	
				break;
			default: 
				System.out.println("Index = " + sce.getIndex());
				System.out.println("changing sensor val: " + sce);
				break;
		}
	}
	
		
}
