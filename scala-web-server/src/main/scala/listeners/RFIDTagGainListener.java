package listeners;

import com.phidgets.event.TagGainEvent;
import com.phidgets.event.TagGainListener;

import commonsObjects.RFIDReader;
import commonsObjects.RoueEpices;

public class RFIDTagGainListener implements TagGainListener{

	RoueEpices re;
	
	public RFIDTagGainListener(RoueEpices re) {
		this.re = re;
	}
	
	public void tagGained(TagGainEvent tge) {

		try{Thread.sleep(2000);}catch(Exception e){}

		RFIDReader reader = re.getRfidReader();

		try{
			String tag = reader.getTag();
			if (tag == null || tag.equals("")) {

			} else{
				Thread.sleep(2000);
				re.majEmplacements();
			}
		} catch (Exception e){
			e.printStackTrace();
		}






		if(re.isRFIDActivated()){
			//re.majEmplacements();
		}
		
	}

}
