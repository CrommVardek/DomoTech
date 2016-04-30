package listeners;

import com.phidgets.event.TagGainEvent;
import com.phidgets.event.TagGainListener;

import commonsObjects.RoueEpices;

public class RFIDTagGainListener implements TagGainListener{

	RoueEpices re;
	
	public RFIDTagGainListener(RoueEpices re) {
		this.re = re;
	}
	
	public void tagGained(TagGainEvent tge) {
		
		if(re.isRFIDActivated()){
			re.majEmplacements();
		}
		
	}

}
