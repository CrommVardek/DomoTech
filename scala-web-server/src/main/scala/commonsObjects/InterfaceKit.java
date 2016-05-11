package commonsObjects;

import com.phidgets.InterfaceKitPhidget;
import com.phidgets.PhidgetException;
import com.phidgets.event.*;
import listeners.IFKInputListener;
import listeners.IFKOutputListener;
import listeners.IFKSensorListener;

/**
 * Created by Axel on 28-04-16.
 */
public class InterfaceKit {

    public InterfaceKit(HeatManager heatManager, LightManager lightManager, DetecteurIncendie detecteurIncendie){
        // TODO: Add Fire Alarm

        IFKSensorListener ifkce = new IFKSensorListener(heatManager, lightManager, detecteurIncendie);
        IFKOutputListener ifkol = new IFKOutputListener();
        IFKInputListener ifkil = new IFKInputListener(detecteurIncendie);

        InterfaceKitPhidget ifk = null;
        try {
            ifk = new InterfaceKitPhidget();
        } catch (PhidgetException e) {
            e.printStackTrace();
        }

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
        try{ifk.open(319110);}
        catch (Exception e) {System.out.println(e.getMessage());}

        //Ajout des ifk aux managers ayant un output.
        lightManager.addIFK(ifk);


        try{Thread.sleep(2000);}catch(Exception e){e.printStackTrace();}

        detecteurIncendie.setIFK(ifk);
        detecteurIncendie.switchAlarmOff();
    }
}
