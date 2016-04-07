/*
 * Ceci est un exemple de code source pour l'utilisation de la roue à épice
*/

package distributeurEpices;


public class Main {

	public static void main(String[] args) {
		System.out.println("Debut du programme");
		RoueEpices roue = new RoueEpices();
		try
		{
		// toujours activer la roue avant de l'utiliser !
		roue.activer();
		
		// rotation selon un des 6 emplacements
		roue.goToEmplacement(1);
		roue.goToEmplacement(2);
		roue.goToEmplacement(3);
		roue.goToEmplacement(4);
		roue.goToEmplacement(5);
		roue.goToEmplacement(6);
		roue.goToEmplacement(5);
		roue.goToEmplacement(4);
		roue.goToEmplacement(3);
		roue.goToEmplacement(2);
		roue.goToEmplacement(1);
		}
		catch (Exception e)
		{
			System.out.println("Une erreur est survenue : " + e.toString());
		}
		finally
		{
			// desactiver la roue coupe simplement le courant du moteur
			roue.desactiver();
			// la cloture se fait toujours à la fin et une seule fois exactement
			roue.cloturer();
		}
		System.out.println("Fin du programme");
	}

}