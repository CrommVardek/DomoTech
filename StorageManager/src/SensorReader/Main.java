package SensorReader;

import exception.SensorReaderException;

public class Main {

	public static void main(String[] args) {
		/* Classe de d�mo pour la lecture des valeurs provenant des phidgets � intervalle r�gulier
		 * La valeur des intervalles est � d�finir en dans le fichier de configuration.
		 */
		
		System.out.println("Programme D�mo du SensorReader");
		
		try
		{
			SensorReader sr = new SensorReader();
			sr.enable();
		} 
		catch (SensorReaderException e)
		{
			System.out.println("Error : " + e.toString());
		}
		
		
		System.out.println("Fin du programme.");
	}

}
