package listeners;

import com.phidgets.event.TagLossEvent;
import com.phidgets.event.TagLossListener;
import commonsObjects.RFIDReader;
import commonsObjects.RoueEpices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RFIDTagLossListener implements TagLossListener {

	RoueEpices re;

	private final Logger logger = LoggerFactory.getLogger(RFIDTagLossListener.class);

	public RFIDTagLossListener(RoueEpices re) {
		this.re = re;
	}

	public void tagLost(TagLossEvent tle) {

		try{Thread.sleep(3500);}catch(Exception e){}

		if (re.isInInitMode()){
			logger.info("Init mode: tag lost not treated");
		} else{
			logger.info("Tag is lost... Preparing to reset");

			RFIDReader reader = re.getRfidReader();

			try{
				String tag = reader.getTag();
				logger.info("The tag is: "+tag);
				if (tag == null || tag.equals("")){
					int emplacement = re.getEmplActuel();
					Thread.sleep(2000);
					re.marquerEmplacementVide(emplacement);

					logger.info("Spice at " + emplacement+ " removed --> reset");
				} else {logger.info("Still here");}
			} catch (Exception e){
				e.printStackTrace();
			}
		}
	}
}
