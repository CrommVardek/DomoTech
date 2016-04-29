package system;

import java.io.IOException;

import listeners.IFKInputListener;
import listeners.IFKOutputListener;
import listeners.IFKSensorListener;
import alarmeIncendie.DetecteurIncendie;

import com.phidgets.InterfaceKitPhidget;
import com.phidgets.event.AttachEvent;
import com.phidgets.event.AttachListener;
import com.phidgets.event.DetachEvent;
import com.phidgets.event.DetachListener;
import com.phidgets.event.ErrorEvent;
import com.phidgets.event.ErrorListener;

import distributeurEpices.RoueEpices;
import features.HeatManager;
import features.LightManager;

public class Main {

	public static void main(String args[]) throws Exception{
		
		    /** 
		     * IFK informations:
		     * Analog Index: | Sensor Type:
		     *      1        |     Light
		     *      2        |  Temperature
		     *      3        |Touch or Force
		     * --------------------------------
		     * Digital Input |    Entries:
		     *      G        |   Black wire
		     *      0        |    Red wire
		     *      1        |  Yellow wire
		     * --------------------------------
		     * Digital Output|    Entries:
		     *      G/0		 |  Light Led #1
		     *      1/2      |  Light Led #2
		     *      3/4      |  Light Led #3
		     *      5/6      |  Light Led #4 
		     *      7/v5     |Alarm Led #1 (Red)
		     */
		
			//Initialize the managers for the room #1
			
			HeatManager heatManager = new HeatManager(1, 0.0);
			LightManager lightManager = new LightManager(1, 0);  

			//Launch Fire/smoke detector
            
			DetecteurIncendie alarme = new DetecteurIncendie(20);
            
			alarme.activer();
		
			//Initialize the IFK
		
			IFKSensorListener ifkce = new IFKSensorListener(heatManager, lightManager, alarme);
			IFKOutputListener ifkol = new IFKOutputListener();
			IFKInputListener ifkil = new IFKInputListener(alarme);
		
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
           
            //319110 is the serial number.
            ifk.open(319110);
            
            //Ajout des ifk aux managers ayant un output.
            lightManager.addIFK(ifk);
            
            Thread.sleep(2000);
            
            alarme.setIFK(ifk);
            
            alarme.switchAlarmOff();
            
            //Update desired temperature for the room of the heat manager
            heatManager.increaseTemp(25.0);
			
			//Launch the spice round.
            
			RoueEpices re = new RoueEpices();
			try
			{
				re.activer();
				re.goToEmplacement(1);
				re.goToEmplacement(2);
				re.goToEmplacement(3);
				re.goToEmplacement(4);
				re.goToEmplacement(5);
				re.goToEmplacement(6);
				re.goToEmplacement(5);
				re.goToEmplacement(4);
				re.goToEmplacement(3);
				re.goToEmplacement(2);
				re.goToEmplacement(1);
			}
			catch (Exception e)
			{
				System.out.println("Une erreur est survenue : " + e.toString());
			}
			finally
			{
				re.desactiver();
				re.cloturer();
			}
			System.out.println("Fin du programme");
            				
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
