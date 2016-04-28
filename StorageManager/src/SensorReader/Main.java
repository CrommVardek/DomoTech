package SensorReader;

import exception.SensorReaderException;

public class Main {

	public static void main(String[] args) {
		/* Classe de démo pour la lecture des valeurs provenant des phidgets à intervalle régulier
		 * La valeur des intervalles est à définir en dans le fichier de configuration.
		 */
		
		System.out.println("Programme Démo du SensorReader");
		
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
