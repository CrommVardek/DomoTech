package distributeurEpices;

import com.phidgets.PhidgetException;

public class RoueEpices {
	
	private final int MAX = 6;
	private final int MIN = 1;
	private Epice[] emplacements = new Epice[MAX];
	private double[] positions = new double[MAX];
	private Moteur servo;
	
	public RoueEpices()
	{
		// initialisation du moteur
		try
		{
			servo = new Moteur();
		} 
		catch (PhidgetException e)
		{
			System.out.println("Une erreur est survenue lors de l'initalisation du bloc moteur : " + e.toString());
		}
		
		// initialisation des emplacements et des positions :
		for(int i = 0; i < MAX; i++) emplacements[i] = null;
		
		positions[0] = 00.00;
		positions[1] = 30.00;
		positions[2] = 60.00;
		positions[3] = 90.00;
		positions[4] = 120.00;
		positions[5] = 150.00;
	}
	
	public void activer()
	{
		if (servo == null) System.out.println("Erreur moteur, impossible de l'activer : référence invalide !");
		else 
		{
			try
			{
				servo.activer();
				Thread.sleep(2000);
			}
			catch (PhidgetException | InterruptedException e)
			{
				System.out.println("Une Erreur moteur est survenue : " + e.toString());
			}
		}
	}
	
	public void desactiver()
	{
		if (servo == null) System.out.println("Erreur moteur, impossible de le désactiver : référence invalide !");
		else
		{
			try
			{
				servo.desactiver();
			}
			catch (PhidgetException e)
			{
				System.out.println("Une Erreur moteur est survenue : " + e.toString());
			}
		}
	}
	
	public void cloturer()
	{
		try
		{
			servo.cloturer();
		}
		catch (PhidgetException e)
		{
			// aucune opération a effectuer.
		}
	}
	
	public boolean emplacementVide(int emplacement)
	{
		if ((emplacement > MAX) || (emplacement < MIN))
		{
			System.out.println("La valeur de l'emplacement spécifiée est incorrecte !");
			return false;
		}
		else if (emplacements[emplacement -1] != null) return false;
		else return true;
	}
	
	public void marquerEmplacementVide(int emplacement)
	{
		if ((emplacement > MAX) || (emplacement < MIN))
		{
			System.out.println("La valeur de l'emplacement spécifiée est incorrecte !");
			return;
		}
		else emplacements[emplacement - 1] = null;
	}
	
	public void attribuerEpiceEmplacement(int emplacement, Epice epice)
	{
		if ((emplacement > MAX) || (emplacement < MIN))
		{
			System.out.println("La valeur de l'emplacement spécifiée est incorrecte !");
			return;
		}
		else emplacements[emplacement - 1 ] = epice;
	}
	
	public void goToEmplacement(int emplacement) throws Exception
	{
		if ((emplacement > MAX) || (emplacement < MIN))
		{
			System.out.println("goToEmplacement : La valeur de l'emplacement spécifiée est incorrecte !");
			return;
		}
		else if ( ! servo.estActif())
		{
			System.out.println("goToEmplacement : Le moteur de la roue n'est pas en marche !");
			return;
		}
		else 
		{
			servo.goToPosition(positions[emplacement -1]);
			Thread.sleep(8000);
		}
	}	
}
