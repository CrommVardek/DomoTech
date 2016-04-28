package distributeurEpices;

import listeners.RFIDTagGainListener;
import listeners.RFIDTagLossListener;

import com.phidgets.PhidgetException;
import com.phidgets.RFIDPhidget;

public class RFIDReader {

	private RFIDPhidget rfid;
	
	public RFIDReader () throws PhidgetException{
		
		rfid = new RFIDPhidget();
		
		//Set up listeners
		RFIDTagGainListener rfidtgl = new RFIDTagGainListener();
		RFIDTagLossListener rfidtll = new RFIDTagLossListener();
				
		//Ajout des listeners
		rfid.addTagGainListener(rfidtgl);
		rfid.addTagLossListener(rfidtll);
	}
	
	public String getTag() throws PhidgetException{
		if (rfid.getTagStatus() == true ){
			return rfid.getLastTag();
		}
		else return null;
	}
}
