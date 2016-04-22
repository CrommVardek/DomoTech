package system;

import alarmeIncendie.Pompier;
import alarmeIncendie.PompierDefaut;

import com.phidgets.InterfaceKitPhidget;
import com.phidgets.PhidgetException;
import com.phidgets.event.InputChangeEvent;
import com.phidgets.event.InputChangeListener;

public class SmokeSensor {

	/* Dorian Lecomte
	 *  impulsion est le compteur du nombre de cr�tes dans le signal
	 *  permettant d'identifier le moment auquel survient la d�tection d'un incendie
	 *  Si rien ne se passe, le signal est continu, ce n'est que lorsqu'un incendie est d�tect�
	 *  que l'on d�tecte un flux discontinu sur l'entr�e du phidget. La variable bool�enne permet
	 *  de savoir si l'alarme est activ�e ou pas. 
	 */
	private int nbrImpulsions;
	private int limiteImpulsions;
	private int mode; // 1 = limite atteinte => alarme lanc�e � chaque fois que la limite est franchie, 2 = remet les compteurs � z�ros quand franchie, 3 d�sactive l'alarme quand limite franchie
	private boolean activee;
	private Pompier pom;
	private InterfaceKitPhidget ifkp;
	
/*
 * CONSTRUCTEURS :
 */
	public SmokeSensor(Pompier pom, int limiteImpulsions, int mode)
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
			
			// connexion � l'interface kit du phidget.
			ifkp.openAny();
			System.out.print("En attente de connexion de l'interface kit... ");
			ifkp.waitForAttachment();
			System.out.println("P�riph�rique connect� !");
		}
		catch (PhidgetException e)
		{
			System.out.println("Error lors de l'instanciation du d�tecteur � incendie : " + e.toString());
		}
	}
	
	public SmokeSensor(int limiteImpulsions)
	{
		this(new PompierDefaut(), limiteImpulsions, 1);
		System.out.println("Instanciation d'un objet d�tecteur incendie avec une limite d'impulsions pr�cis�e");
	}
	
	public SmokeSensor(int limiteImpulsions, int mode)
	{
		this(new PompierDefaut(), limiteImpulsions, mode);
		System.out.println("Instanciation d'un objet d�tecteur incendie avec une limite d'impulsions et un mode pr�cis�s");
	}
	
	public SmokeSensor()
	{
		this(new PompierDefaut(), 10, 1);
		System.out.println("Instanciation d'un objet d�tecteur incendie avec les param�tres par d�faut");
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
			// aucune op�ration a effectuer
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
		if (nbrImpulsions < 0) System.out.println("La valeur sp�cifi�e pour le nombre d'impulsion doit toujours �tre sup�rieure � z�ro !");
		else this.nbrImpulsions = nbrImpulsions;
	}
	
	public int getLimiteImpulsions()
	{
		return this.limiteImpulsions;
	}
	
	public void setLimiteImpulsions(int limiteImpulsions)
	{
		if (limiteImpulsions < 0) System.out.println("La valeur sp�cifi�e pour le nombre d'impulsion doit toujours �tre sup�rieure � z�ro !");
		else this.limiteImpulsions = limiteImpulsions;
	}
	
	public int getMode()
	{
		return this.mode;
	}
	
	public void setMode( int mode)
	{
		if ((mode > 3) || (mode < 1)) System.out.println("Erreur : le mode sp�cifi� est incorrect !");
		else this.mode = mode;
	}
	
	public void displayInfo()
	{
		try
		{
			System.out.println("Informations techniques :");
			System.out.println("-------------------------");
			
			System.out.println("-> Nombre d'entr�es  : " + ifkp.getInputCount());
			System.out.println("-> Nombre de sorties : " + ifkp.getOutputCount());
			System.out.println("-> Nombre de senseurs support�s : " + ifkp.getSensorCount());
			
			System.out.println("-> Etats des entr�es num�riques :");
			for(int i = 0; i < ifkp.getInputCount(); i++) System.out.println("   +> Entr�e " + i + " : " + ifkp.getInputState(i));
			
			System.out.println("-> Etats des sorties num�riques :");
			for(int i = 0; i < ifkp.getInputCount(); i++) System.out.println("   +> Sortie " + i + " : " + ifkp.getOutputState(i));
			
			System.out.println("-------------------------");
		}
		catch (PhidgetException e)
		{
			System.out.println(" Erreur : " + e.toString());
		}
	}
	
}
