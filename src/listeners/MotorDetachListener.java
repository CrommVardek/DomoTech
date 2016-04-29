package listeners;

import com.phidgets.event.DetachEvent;
import com.phidgets.event.DetachListener;

import distributeurEpices.RoueEpices;

public class MotorDetachListener implements  DetachListener {

	RoueEpices re;
	
	public MotorDetachListener(RoueEpices re){
		
		this.re =re;
		
	}

	public void detached(DetachEvent de) {
		
		re.desactiver();
		
	}

}
