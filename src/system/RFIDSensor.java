package system;

import java.io.IOException;

import listeners.RFIDTagGainListener;
import listeners.RFIDTagLossListener;

import com.phidgets.Phidget;
import com.phidgets.PhidgetException;
import com.phidgets.RFIDPhidget;
import com.phidgets.event.*;

import distributeurEpices.RoueEpices;

public class RFIDSensor {

	protected static void LaunchRFIDSensor(RoueEpices re) throws PhidgetException, IOException{
		
		RFIDPhidget rfid;

		System.out.println(Phidget.getLibraryVersion());

		rfid = new RFIDPhidget();
		rfid.addAttachListener(new AttachListener() {
			public void attached(AttachEvent ae)
			{
				try
				{
					((RFIDPhidget)ae.getSource()).setAntennaOn(true);
					((RFIDPhidget)ae.getSource()).setLEDOn(true);
				}
				catch (PhidgetException ex) { }
				System.out.println("attachment of " + ae);
			}
		});
		
		rfid.addDetachListener(new DetachListener() {
			public void detached(DetachEvent ae) {
				System.out.println("detachment of " + ae);
			}
		});
		
		rfid.addErrorListener(new ErrorListener() {
			public void error(ErrorEvent ee) {
				System.out.println("error event for " + ee);
			}
		});
		
		//Set up listeners
		RFIDTagGainListener rfidtgl = new RFIDTagGainListener();
		RFIDTagLossListener rfidtll = new RFIDTagLossListener();
		
		//Ajout des listeners
		rfid.addTagGainListener(rfidtgl);
		rfid.addTagLossListener(rfidtll);

		rfid.openAny();
		System.out.println("waiting for RFID attachment...");
		rfid.waitForAttachment(1000);

		System.out.println("Serial: " + rfid.getSerialNumber());
		System.out.println("Outputs: " + rfid.getOutputCount());
		
		//How to write a tag:
		//rfid.write("A TAG!!", RFIDPhidget.PHIDGET_RFID_PROTOCOL_PHIDGETS, false); 

		System.out.println("Outputting events.  Input to stop.");
		
		System.in.read();
		
		System.out.print("closing...");
		
		rfid.close();
		
		rfid = null;
		
		System.out.println(" ok");
		
		
	}
	
	
}
