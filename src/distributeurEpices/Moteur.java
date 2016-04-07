package distributeurEpices;

import com.phidgets.AdvancedServoPhidget;
import com.phidgets.PhidgetException;
import com.phidgets.event.AttachEvent;
import com.phidgets.event.AttachListener;
import com.phidgets.event.DetachEvent;
import com.phidgets.event.DetachListener;
import com.phidgets.event.ErrorEvent;
import com.phidgets.event.ErrorListener;
import com.phidgets.event.ServoPositionChangeEvent;
import com.phidgets.event.ServoPositionChangeListener;

public class Moteur {

/* Dorian Lecomte
 * Classe permettant le pilotage et le déplacement du moteur commandant la roue à épice.
 */
	
/*
 * VARIABLES
 */
	private AdvancedServoPhidget servo;
	private double positionActuelle;
	
/*
 * CONSTRUCTEURS
 */
	public Moteur( double positionMin, double positionMax, double velocityLimit, double acceleration) throws PhidgetException
	{
		servo = new AdvancedServoPhidget();
		servo.openAny();
		servo.waitForAttachment();
		
		servo.setEngaged(0, false);
		servo.setPositionMin(0, positionMin);
		servo.setPositionMax(0, positionMax);
		servo.setVelocityLimit(0, velocityLimit);
		servo.setAcceleration(0, acceleration);
		servo.setSpeedRampingOn(0, true);
		this.defineListeners(servo);	
	}
	
	public Moteur() throws PhidgetException
	{
		this(0.0, 220.0, 250.0, 30.0);
	}
	
/*
 * METHODES
 */

	public void activer() throws PhidgetException
	{
		servo.setEngaged(0, true);
	}
	
	public boolean estActif() throws PhidgetException
	{
		return servo.getEngaged(0);
	}
	
	public void desactiver() throws PhidgetException
	{
		servo.setEngaged(0, false);
	}
	
	public void cloturer() throws PhidgetException
	{
		servo.close();
	}
	
	public void goToPositionMin() throws PhidgetException
	{
		servo.setPosition(0, servo.getPositionMin(0));
	}
	
	public void goToPositionMax() throws PhidgetException
	{
		servo.setPosition(0, servo.getPositionMax(0));
	}
	
	public void goToPosition(double position) throws PhidgetException
	{
		if ((position >= servo.getPositionMin(0)) && (position <= servo.getPositionMax(0)))  servo.setPosition(0, position);
		else System.out.println("Impossible de déplacer la roue à l'emplacement spécifié : position incorrecte.");
	}
	
	public double getPositionActuelle() throws PhidgetException
	{
		return servo.getPosition(0);
		
	}
	
	public double getPositionActuelleEcoutee()
	{
		return positionActuelle;
	}
	
	public void setAcceleration (double acceleration) throws PhidgetException
	{
		servo.setAcceleration(0, acceleration);
	}
	
	public double getAcceleration () throws PhidgetException
	{
		return servo.getAcceleration(0);
	}
	
	public void setVelocity(double velocity) throws PhidgetException
	{
		servo.setVelocityLimit(0, velocity);
	}
	
	public double getVelocity() throws PhidgetException
	{
		return servo.getVelocity(0);
	}
	
	public void displayInfo() throws PhidgetException
	{
		System.out.println("Informations techniques : " );
		System.out.println("--------------------------" );
		System.out.println("-> Moteur connecté   : " + servo.getMotorCount());
		System.out.println("-> Position minimale  : " + servo.getPositionMin(0));
		System.out.println("-> Position maximale : " + servo.getPositionMax(0));
		System.out.println("-> Position actuelle : " + servo.getPosition(0));
		System.out.println("-> Velocité minimale : " + servo.getVelocityMin(0));
		System.out.println("-> Vélocité maximale : " + servo.getVelocityMax(0));
		System.out.println("-> Vélocité actuelle : " + servo.getVelocity(0));
		System.out.println("-> Accélération minimale : " + servo.getAccelerationMin(0));
		System.out.println("-> Accélératino maximale : " + servo.getAccelerationMax(0));
		System.out.println("-> Accélération actuelle : " + servo.getAcceleration(0));
		System.out.println("--------------------------" );
	}
	
    	
	private void defineListeners( AdvancedServoPhidget servo){
		
		servo.addAttachListener(new AttachListener() {
			public void attached(AttachEvent ae) {
				System.out.println("Moteur connecté => " + ae);
			}
		});
		
		servo.addDetachListener(new DetachListener() {
			public void detached(DetachEvent ae) {
				System.out.println("Moteur déconnecté => " + ae);
			}
		});
		
		servo.addErrorListener(new ErrorListener() {
			public void error(ErrorEvent ee) {
				System.out.println("Erreur moteur => " + ee);
			}
		});
		
		servo.addServoPositionChangeListener(new ServoPositionChangeListener()
		{
			public void servoPositionChanged(ServoPositionChangeEvent oe)
			{
				positionActuelle = oe.getValue();
				System.out.println("Changement de position du moteur : " + positionActuelle);
			}
		});
	}

}
