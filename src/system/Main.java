package system;

import java.io.IOException;
import java.util.Vector;
import com.phidgets.*;

import alarmeIncendie.DetecteurIncendie;

import distributeurEpices.RoueEpices;

public class Main {

	public static void main(String args[]) throws Exception{
		
			InterfaceKitPhidget ifk = new InterfaceKitPhidget();
			
			//Initialize the rooms of the house
			Vector<Integer> rooms  = new Vector<Integer>(2);
			
			rooms.addElement(0);
			rooms.addElement(1);
			rooms.addElement(2);
			rooms.addElement(3);
			rooms.addElement(4);

			//Launch the spice round.
			RoueEpices re = new RoueEpices();
			
			//Launch and initialize the sensors and the managers
			TempSensor.LaunchTempSensor(rooms.get(0));
			LightSensor.LaunchLightSensor(ifk, rooms.get(0));
			RFIDSensor.LaunchRFIDSensor(re); 

			//Launch Fire/smoke detector
			DetecteurIncendie alarme = new DetecteurIncendie(20);
			alarme.activer();
				
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
			
			alarme.cloturer();
			System.out.println("Fin du programme.");
			System.exit(0);
	}
	
	
	
}
