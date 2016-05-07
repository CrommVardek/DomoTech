package listeners;

import com.phidgets.event.TagLossEvent;
import com.phidgets.event.TagLossListener;
import commonsObjects.RoueEpices;
import commonsObjects.Spice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RFIDTagLossListener implements TagLossListener {

	RoueEpices re;

	private final Logger logger = LoggerFactory.getLogger(RFIDTagLossListener.class);

	public RFIDTagLossListener(RoueEpices re) {
		this.re = re;
	}

	public void tagLost(TagLossEvent tle) {
		// Set the tag read as the last one read in the spice wheel.
		re.setListenerTag(tle.getValue());
		re.setListenerTagGain(false);

		// If the Wheel is in init mode; nothing to do.
		if(!re.isInInitMode()){
			try{
				logger.info("Wheel not in init mode - Tag loss in treatment");
				//Thread.sleep(500);

				if (!re.getListenerTagGain() && re.getListenerTag().equals(tle.getValue())){
					re.majEmplacements(new Spice().getBarCode());
					logger.info("Tag: \""+tle.getValue()+"\" removed.");
				}
				else if(re.getListenerTagGain() && re.getListenerTag().equals(tle.getValue())){
					re.majEmplacements(tle.getValue());
					logger.info("Tag: \""+tle.getValue()+"\" put back.");
				}
				else{
					logger.info("RFID Tag Gain not treated....");
					logger.info("Tag = "+tle.getValue()+"; Tag gain = "+String.valueOf(re.getListenerTagGain()));
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
