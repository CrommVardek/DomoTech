package system;

import java.io.IOException;

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
		
		rfid.addTagGainListener(new TagGainListener()
		{
			public void tagGained(TagGainEvent oe)
			{
				System.out.println("Tag Gained: " +oe.getValue() + " (Proto:"+ oe.getProtocol()+")");
				//TODO Communication avec roue à épices
								
			}
		});
		
		rfid.addTagLossListener(new TagLossListener()
		{
			public void tagLost(TagLossEvent oe)
			{
				System.out.println(oe);
			}
		});
		
		rfid.addOutputChangeListener(new OutputChangeListener()
		{
			public void outputChanged(OutputChangeEvent oe)
			{
				System.out.println(oe);
			}
		});

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
