package listeners;

import com.phidgets.event.TagLossEvent;
import com.phidgets.event.TagLossListener;

import commonsObjects.RoueEpices;

public class RFIDTagLossListener implements TagLossListener {

	RoueEpices re;
	
	public RFIDTagLossListener(RoueEpices re) {
		this.re = re;
	}

	public void tagLost(TagLossEvent tle) {
		
		if(re.isRFIDActivated()){
			re.marquerEmplacementVide(re.getEmplActuel());
		}
		
	}

}
