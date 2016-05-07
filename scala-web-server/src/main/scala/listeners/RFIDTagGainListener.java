package listeners;

import com.phidgets.event.TagGainEvent;
import com.phidgets.event.TagGainListener;
import commonsObjects.RoueEpices;
import commonsObjects.Spice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RFIDTagGainListener implements TagGainListener{

	private RoueEpices re;
	private static final Logger logger = LoggerFactory.getLogger(RFIDTagGainListener.class);
	
	public RFIDTagGainListener(RoueEpices re) {
		this.re = re;
	}
	
	public void tagGained(TagGainEvent tge) {
		// Set the tag read as the last one read in the spice wheel.
		re.setListenerTag(tge.getValue());
		re.setListenerTagGain(true);

		// If the Wheel is in init mode; nothing to do.
		if(!re.isInInitMode()){
			try{
				logger.info("Spice Wheel not in init mode - Tag Gain in treatment.");
				//Thread.sleep(500);
				// if the tag is still there update the spice.
				if (re.getListenerTagGain() && re.getListenerTag().equals(tge.getValue())){
					re.majEmplacements(tge.getValue());
				}
				// If tag is lost in the mean time, put empty spice value.
				else if(!re.getListenerTagGain() && re.getListenerTag().equals(tge.getValue())){
					re.majEmplacements(new Spice().getBarCode());
				}
				else{
					logger.info("RFID Tag Gain not treated....");
					logger.info("Tag = "+tge.getValue()+"; Tag gain = "+String.valueOf(re.getListenerTagGain()));
				}
			} catch (Exception e){
				logger.info("Error in RFIDTagGainListener...");
				e.printStackTrace();
			}
		} else{
			logger.debug("Spice Wheel in init mode: Tag Gained not treated");
		}
	}
}
