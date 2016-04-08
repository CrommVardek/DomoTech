package alarmeIncendie;

import com.phidgets.InterfaceKitPhidget;
import com.phidgets.PhidgetException;
import com.phidgets.event.InputChangeEvent;
import com.phidgets.event.InputChangeListener;


public class DetecteurIncendie {
	
	/* Dorian Lecomte
	 *  impulsion est le compteur du nombre de crêtes dans le signal
	 *  permettant d'identifier le moment auquel survient la détection d'un incendie
	 *  Si rien ne se passe, le signal est continu, ce n'est que lorsqu'un incendie est détecté
	 *  que l'on détecte un flux discontinu sur l'entrée du phidget. La variable booléenne permet
	 *  de savoir si l'alarme est activée ou pas. 
	 */
	private int nbrImpulsions;
	private int limiteImpulsions;
	private int mode; // 1 = limite atteinte => alarme lancée à chaque fois que la limite est franchie, 2 = remet les compteurs à zéros quand franchie, 3 désactive l'alarme quand limite franchie
	private boolean activee;
	private Pompier pom;
	private InterfaceKitPhidget ifkp;
	
/*
 * CONSTRUCTEURS :
 */
	public DetecteurIncendie(Pompier pom, int limiteImpulsions, int mode)
	{
		// instanciation des variables
		this.pom = pom;
		this.nbrImpulsions = 0;
		this.limiteImpulsions = limiteImpulsions;
		this.activee = false;
		this.mode = mode;
		try
		{
			ifkp = new InterfaceKitPhidget();
			
			// définition du listener pour détecter les modulations sur les entrées (= incendie)
			ifkp.addInputChangeListener(new InputChangeListener()
			{
				public void inputChanged(InputChangeEvent ice)
				{
					if (activee && (++nbrImpulsions >= limiteImpulsions)) 
					{
						switch (mode)
						{
						case 1 : pom.AlerteIncendie();
								 break;
						case 2 : nbrImpulsions = 0;
								 pom.AlerteIncendie();
								 break;
						case 3 : activee = false;
								 nbrImpulsions = 0;
								 pom.AlerteIncendie();
								 break;
						default : System.out.println("Erreur : mode inconnu ! ");
								break;
						}

					}
				}
			});
			
			// connexion à l'interface kit du phidget.
			ifkp.openAny();
			System.out.print("En attente de connexion de l'interface kit... ");
			ifkp.waitForAttachment();
			System.out.println("Périphérique connecté !");
		}
		catch (PhidgetException e)
		{
			System.out.println("Error lors de l'instanciation du détecteur à incendie : " + e.toString());
		}
	}
	
	public DetecteurIncendie(int limiteImpulsions)
	{
		this(new PompierDefaut(), limiteImpulsions, 1);
		System.out.println("Instanciation d'un objet détecteur incendie avec une limite d'impulsions précisée");
	}
	
	public DetecteurIncendie(int limiteImpulsions, int mode)
	{
		this(new PompierDefaut(), limiteImpulsions, mode);
		System.out.println("Instanciation d'un objet détecteur incendie avec une limite d'impulsions et un mode précisés");
	}
	
	public DetecteurIncendie()
	{
		this(new PompierDefaut(), 10, 1);
		System.out.println("Instanciation d'un objet détecteur incendie avec les paramètres par défaut");
	}

/*
 * METHODES
 */

	public void activer()
	{
		activee = true;
	}
	
	public void desactiver()
	{
		activee = false;
	}
	
	public void cloturer()
	{
		try 
		{
			ifkp.close();
		}
		catch (PhidgetException e)
		{
			// aucune opération a effectuer
		}
	}
	
	public void setPompier( Pompier pom)
	{
		this.pom = pom;
	}
	
	public Pompier getPompier()
	{
		return pom;
	}
	
	public int getNbrImpulsions()
	{
		return nbrImpulsions;
	}
	
	public void setNbrImpulsions(int nbrImpulsions)
	{
		if (nbrImpulsions < 0) System.out.println("La valeur spécifiée pour le nombre d'impulsion doit toujours être supérieure à zéro !");
		else this.nbrImpulsions = nbrImpulsions;
	}
	
	public int getLimiteImpulsions()
	{
		return this.limiteImpulsions;
	}
	
	public void setLimiteImpulsions(int limiteImpulsions)
	{
		if (limiteImpulsions < 0) System.out.println("La valeur spécifiée pour le nombre d'impulsion doit toujours être supérieure à zéro !");
		else this.limiteImpulsions = limiteImpulsions;
	}
	
	public int getMode()
	{
		return this.mode;
	}
	
	public void setMode( int mode)
	{
		if ((mode > 3) || (mode < 1)) System.out.println("Erreur : le mode spécifié est incorrect !");
		else this.mode = mode;
	}
	
	public void displayInfo()
	{
		try
		{
			System.out.println("Informations techniques :");
			System.out.println("-------------------------");
			
			System.out.println("-> Nombre d'entrées  : " + ifkp.getInputCount());
			System.out.println("-> Nombre de sorties : " + ifkp.getOutputCount());
			System.out.println("-> Nombre de senseurs supportés : " + ifkp.getSensorCount());
			
			System.out.println("-> Etats des entrées numériques :");
			for(int i = 0; i < ifkp.getInputCount(); i++) System.out.println("   +> Entrée " + i + " : " + ifkp.getInputState(i));
			
			System.out.println("-> Etats des sorties numériques :");
			for(int i = 0; i < ifkp.getInputCount(); i++) System.out.println("   +> Sortie " + i + " : " + ifkp.getOutputState(i));
			
			System.out.println("-------------------------");
		}
		catch (PhidgetException e)
		{
			System.out.println(" Erreur : " + e.toString());
		}
	}
}
