package alarmeIncendie;

import java.io.IOException;

public class Main {

	public static void main(String[] args) {

		System.out.println("Début du programme");
		
		DetecteurIncendie alarme = new DetecteurIncendie(20);
		alarme.activer();
		
		
		
		/*
		 * Ici bas la mécanique de cloture du programme, sans intéret pour l'alarme incendie mais nécessaire quand même :
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
