package commonsObjects;

import com.phidgets.PhidgetException;

import exception.SpiceNotPresentException;

/**
 * Created by Axel on 29-04-16.
 */
public class RoueEpices {

    private final int MAXEPICES = 999;
    private final int MAX = 6;
    private final int MIN = 1;
    private Spice[] emplacements = new Spice[MAX];
    private double[] positions = new double[MAX];
    private Moteur servo;
    private RFIDReader rfid;
    //Base de Connaissances Epices / RFID ==> A récupérer de la BDD en priori.
    private Spice[] BDCEpices = new Spice[MAXEPICES];
    private boolean activatedRFID =false;

    public RoueEpices()
    {
        // initialisation du moteur
        try
        {
            servo = new Moteur();
            servo.activer();
        }
        catch (PhidgetException e)
        {
            System.out.println("Une erreur est survenue lors de l'initalisation du bloc moteur : " + e.toString());
        }

        // initialisation du lecteur RFID
        try
        {
            rfid = new RFIDReader(this);
            activatedRFID = true;
        }
        catch (PhidgetException e)
        {
            System.out.println("Une erreur est survenue lors de l'initalisation du lecteur RFID : " + e.toString());
        }

        // initialisation des emplacements et des positions :
        for(int i = 0; i < MAX; i++) emplacements[i] = null;

        positions[0] = 0.00;
        positions[1] = 50.00;
        positions[2] = 83.00;
        positions[3] = 114.00;
        positions[4] = 142.00;
        positions[5] = 180.00;

        //Initialisation des épices de la roue.
        try {
            initialisationEmplacements();
        } catch (Exception e) {
            //
        }

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
        //TODO: MàJ BDD
    }

    public void attribuerEpiceEmplacement(int emplacement, Spice epice)
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
        this.activer();
        if ((emplacement > MAX) || (emplacement < MIN))
        {
            System.out.println("goToEmplacement : La valeur de l'emplacement spécifiée est incorrecte !" + emplacement);
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
        this.desactiver();
    }

    //Renvoie l'emplacement "box" en fonction de la position du servomoteur et du nombre de "box" de la roue.
    public int getEmplActuel(){

        try{
            double posAct;
            posAct = servo.getPositionActuelle();
            double divVal = (180/MAX);

            if ((posAct/divVal)<MAX){
                return (int) ((posAct/divVal)+1);
            }
            //Could be dangerous
            else return -1;
        }
        catch (PhidgetException e)
        {
            System.out.println("Une Erreur moteur est survenue : " + e.toString());
            return -1;
        }

    }

    public Spice[] getEmplacement(){
        return emplacements;
    }

    public Boolean isInBDC(String tagRFID){
        for(int i=0; i < MAXEPICES; i++){
            if (BDCEpices[i].getBarCode() == tagRFID ){
                return true;
            }
        }
        return false;
    }

    public String getNomBDCE(String tagRFID){
        for(int i=0; i < MAXEPICES; i++){
            if (BDCEpices[i].getBarCode() == tagRFID ){
                return BDCEpices[i].getName();
            }
        }
        return null;
    }

    public String getDescBDCE(String tagRFID){
        for(int i=0; i < MAXEPICES; i++){
            if (BDCEpices[i].getBarCode() == tagRFID ){
                return BDCEpices[i].getDescription();
            }
        }
        return null;
    }

	/*
	 * Initialisation de la roue à épices, s'utilise à la création de la roue à épices et lors de mise à niveau de la roue
	 */

    public void initialisationEmplacements() throws Exception, PhidgetException{

        for(int i = 0; i < MAX; i++) {

            //Va à l'emplcement
            goToEmplacement(i+1);
            //MàJ des emplcements
            if (isInBDC(rfid.getTag())){
                emplacements[i].setName(getNomBDCE(rfid.getTag()));
                emplacements[i].setDescription(getDescBDCE(rfid.getTag()));
                emplacements[i].setBarCode(rfid.getTag());
            }
            //TODO: MàJ BDD
        }

    }

	/*
	 * Mets à jour le contenu d'un emplacement de la roue, s'utilise à chaque modification du contenu physique de la roue à épices.
	 */

    public void majEmplacements(){

        try {
            emplacements[getEmplActuel()].setName(getNomBDCE(rfid.getTag()));
            emplacements[getEmplActuel()].setDescription(getDescBDCE(rfid.getTag()));
            emplacements[getEmplActuel()].setBarCode(rfid.getTag());
        } catch (PhidgetException e) {
            System.out.println("Impossible de mettre à jour l'emplacement actuel");
            e.printStackTrace();
        }
        //TODO: MàJ BDD
    }

	/*
	 * Activer ou désactiver le lecteur RFID
	 */

    public void activateRFID(){
        activatedRFID = true;
    }

    public void desactivateRFID(){
        activatedRFID = false;
    }

    public boolean isRFIDActivated(){
        return activatedRFID;
    }

	/*
	 * A Appeller si on veut sélectionner une épice
	 */

    public void askEpice(String name) throws SpiceNotPresentException{
        desactivateRFID();
        for (int i = 0; i < MAX; i++){
            if (emplacements[i].getName() == name){

                try {
                    goToEmplacement(i+1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else{
                if(i == MAX - 1){
                    throw new SpiceNotPresentException();
                }
            }
        }
        activateRFID();
    }



}
