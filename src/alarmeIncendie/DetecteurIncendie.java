package alarmeIncendie;

import com.phidgets.InterfaceKitPhidget;
import com.phidgets.PhidgetException;

public class DetecteurIncendie {
	
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
	public DetecteurIncendie(Pompier pom, int limiteImpulsions, int mode)
	{
		// instanciation des variables
		this.pom = pom;
		this.nbrImpulsions = 0;
		this.limiteImpulsions = limiteImpulsions;
		this.activee = false;
		this.mode = mode;
	}
	
	public DetecteurIncendie(int limiteImpulsions){
		this(new PompierDefaut(), limiteImpulsions, 1);
	}
	
	public DetecteurIncendie(int limiteImpulsions, int mode, InterfaceKitPhidget ifk)
	{
		this(new PompierDefaut(), limiteImpulsions, mode);
		System.out.println("Instanciation d'un objet d�tecteur incendie avec une limite d'impulsions et un mode pr�cis�s");
	}
	
	public DetecteurIncendie(InterfaceKitPhidget ifk)
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
	
	public void setIFK(InterfaceKitPhidget ifk){
		this.ifkp = ifk;
	}
	
	public void changeMode(){
		try{
			
			if (ifkp != null && activee && (++nbrImpulsions >= limiteImpulsions)) 
			
			{
				switch (mode)
				{
				case 1 : pom.AlerteIncendie();
					ifkp.setOutputState(7, false);
					break;
				case 2 : nbrImpulsions = 0;
					pom.AlerteIncendie();
					ifkp.setOutputState(7, false);
					break;
				case 3 : activee = false;
					nbrImpulsions = 0;
					pom.AlerteIncendie();
					ifkp.setOutputState(7, false);
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						//
					}
					switchAlarmOff();
					break;
				default : 
					switchAlarmOff();
					System.out.println("Erreur : mode inconnu ! ");
					break;
			}
			}
		} catch (PhidgetException e) {
			//
		}
	}
	
	//Permet simplement d'arrêter l'alarme sans la désactiver.
	public void switchAlarmOff() {
		
		nbrImpulsions = 0;
		try {
			System.out.println("Success to switch the alarm off");
			ifkp.setOutputState(7, true);
		} catch (PhidgetException e) {
			System.out.println("Failed to switch off the alarm");
			// e.printStackTrace();
		}
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
