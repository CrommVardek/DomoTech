package distributeurEpices;

import listeners.RFIDTagGainListener;
import listeners.RFIDTagLossListener;

import com.phidgets.PhidgetException;
import com.phidgets.RFIDPhidget;

public class RFIDReader {

	private RFIDPhidget rfid;
	private RoueEpices re;
	
	public RFIDReader (RoueEpices re) throws PhidgetException{
		
		this.re = re;
		rfid = new RFIDPhidget();
		addListeners();
	}
	
	public void addListeners(){

		//Set up listeners
		RFIDTagGainListener rfidtgl = new RFIDTagGainListener(re);
		RFIDTagLossListener rfidtll = new RFIDTagLossListener(re);
				
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
	
	public void setRoueEpices(RoueEpices re) {
		this.re = re;
	}
}
