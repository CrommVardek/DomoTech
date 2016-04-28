package distributeurEpices;

import com.phidgets.PhidgetException;

public class RoueEpices {
	
	private final int MAXEPICES = 999;
	private final int MAX = 6;
	private final int MIN = 1;
	private Epice[] emplacements = new Epice[MAX];
	private double[] positions = new double[MAX];
	private Moteur servo;
	private RFIDReader rfid;
	//Base de Connaissances Epices / RFID
	private Epice[] BDCEpices = new Epice[MAXEPICES];
	
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
		
		// initialisation du lecteur RFID
		try
		{
			rfid = new RFIDReader();
		} 
		catch (PhidgetException e)
		{
			System.out.println("Une erreur est survenue lors de l'initalisation du lecteur RFID : " + e.toString());
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
	
	//Renvoie l'emplacement "box" en fonction de la position du servomoteur et du nombre de "box" de la roue. 
	public int getEmplActuel() throws PhidgetException{
		
		double posAct;
		posAct = servo.getPositionActuelle();
		double divVal = (180/MAX);
		
		if ((posAct/divVal)<MAX){
			return (int) ((posAct/divVal)+1);
		}
		//Could be dangerous
		else return -1;
	}
	
	public Epice[] getEmplacement(){
		return emplacements;
	}
	
	public Boolean isInBDC(String tagRFID){
		for(int i=0; i < MAXEPICES; i++){
			if (BDCEpices[i].getRFID() == tagRFID ){
				return true;
			}
		}
		return false;
	}
	
	public String getNomBDCE(String tagRFID){
		for(int i=0; i < MAXEPICES; i++){
			if (BDCEpices[i].getRFID() == tagRFID ){
				return BDCEpices[i].getNom();
			}
		}
		return null;
	}
	
	public String getDescBDCE(String tagRFID){
		for(int i=0; i < MAXEPICES; i++){
			if (BDCEpices[i].getRFID() == tagRFID ){
				return BDCEpices[i].getDescription();
			}
		}
		return null;
	}
	
	public void initialisationEmplacements() throws Exception, PhidgetException{
		
		for(int i = 0; i < MAX; i++) {
			
			//Va à l'emplcement
			goToEmplacement(i);
			//MàJ des emplcements
			if (isInBDC(rfid.getTag())){
				emplacements[i].setNom(getNomBDCE(rfid.getTag()));
				emplacements[i].setDescription(getDescBDCE(rfid.getTag()));
				emplacements[i].setRFID(rfid.getTag());
			}
			//TODO: MàJ BDD			
		}
		
	}
	
	public void majEmplacements() throws PhidgetException {
		
		emplacements[getEmplActuel()].setNom(getNomBDCE(rfid.getTag()));
		emplacements[getEmplActuel()].setDescription(getDescBDCE(rfid.getTag()));
		emplacements[getEmplActuel()].setRFID(rfid.getTag());
		
		//TODO: MàJ BDD	
	}
	
	
}
