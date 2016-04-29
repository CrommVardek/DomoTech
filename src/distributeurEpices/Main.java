package distributeurEpices;


public class Main {

	public static void main(String[] args) {
		System.out.println("Debut du programme");
		RoueEpices roue = new RoueEpices();
		try
		{
			roue.activer();
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
			roue.desactiver();
			roue.cloturer();
		}
		System.out.println("Fin du programme");
	}

}
