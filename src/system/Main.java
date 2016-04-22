package system;

import java.io.IOException;
import java.util.Vector;

import listeners.IFKInputListener;
import listeners.IFKOutputListener;
import listeners.IFKSensorListener;

import com.phidgets.*;
import com.phidgets.event.AttachEvent;
import com.phidgets.event.AttachListener;
import com.phidgets.event.DetachEvent;
import com.phidgets.event.DetachListener;
import com.phidgets.event.ErrorEvent;
import com.phidgets.event.ErrorListener;
import com.phidgets.event.InputChangeEvent;
import com.phidgets.event.InputChangeListener;
import com.phidgets.event.OutputChangeEvent;
import com.phidgets.event.OutputChangeListener;
import com.phidgets.event.SensorChangeEvent;
import com.phidgets.event.SensorChangeListener;
import com.phidgets.event.SensorUpdateEvent;
import com.phidgets.event.SensorUpdateListener;

import alarmeIncendie.DetecteurIncendie;
import distributeurEpices.RoueEpices;

public class Main {

	public static void main(String args[]) throws Exception{
		
			IFKSensorListener ifkce = new IFKSensorListener();
			IFKOutputListener ifkol = new IFKOutputListener();
			IFKInputListener ifkil = new IFKInputListener();
		
			//Initialize the IFK
			InterfaceKitPhidget ifk = new InterfaceKitPhidget();
			
			ifk.addAttachListener(new AttachListener() {
				public void attached(AttachEvent ae) {
					System.out.println("attachment of: " + ae);
				}
			});
			
			ifk.addDetachListener(new DetachListener() {
				public void detached(DetachEvent ae) {
					System.out.println("detachment of: " + ae);
				}
			});
			
			ifk.addErrorListener(new ErrorListener() {
				public void error(ErrorEvent ee) {
					System.out.println("error event for: " + ee);
				}
			});
            
            ifk.addSensorChangeListener(ifkce);
            ifk.addInputChangeListener(ifkil);
            ifk.addOutputChangeListener(ifkol);
                                    
            ifk.open(319110);
			
			//Initialize the rooms of the house
			Vector<Integer> rooms  = new Vector<Integer>(2);
			
			rooms.addElement(0);
			rooms.addElement(1);
			rooms.addElement(2);
			rooms.addElement(3);
			rooms.addElement(4);

			//Launch the spice round.
			//RoueEpices re = new RoueEpices();
			System.out.println("test");
			//Launch and initialize the sensors and the managers
			//TempSensor.LaunchTempSensor(ifk, rooms.get(0));
			//LightSensor.LaunchLightSensor(ifk, rooms.get(0));
			//RFIDSensor.LaunchRFIDSensor(re); 

			//Launch Fire/smoke detector
			//DetecteurIncendie alarme = new DetecteurIncendie(20);
			//alarme.activer();
				
			/*
			 * Ici bas la m�canique de cloture du programme, sans int�ret pour l'alarme incendie mais n�cessaire quand m�me :
			 */
			
			System.out.println("Appuyer sur une touche pour terminer le programme...");
			try 
			{
				System.in.read();
			} 
			catch (IOException e)
			{
				System.out.println("Erreur");
			}
			
			//alarme.cloturer();
			System.out.println("Fin du programme.");
			System.exit(0);
	}
	
	
	
}
