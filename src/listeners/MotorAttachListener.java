package listeners;

import com.phidgets.event.AttachEvent;
import com.phidgets.event.AttachListener;

import distributeurEpices.RoueEpices;

public class MotorAttachListener implements  AttachListener {

	RoueEpices re;
	
	public MotorAttachListener(RoueEpices re){
		
		this.re =re;
		
	}

	public void attached(AttachEvent de) {
		
		re.activer();
		
	}
	
}
