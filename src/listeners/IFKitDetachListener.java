package listeners;

import com.phidgets.event.DetachEvent;
import com.phidgets.event.DetachListener;

import features.HeatManager;
import features.LightManager;

public class IFKitDetachListener implements DetachListener {

	private HeatManager hm;
	private LightManager lm;
	
	IFKitDetachListener(HeatManager hM, LightManager lM){
		hm = hM;
		lm = lM;
	}
	
	@Override
	public void detached(DetachEvent de) {
		
		
		
	}

}
