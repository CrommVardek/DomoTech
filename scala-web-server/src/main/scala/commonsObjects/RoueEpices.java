package commonsObjects;

import com.phidgets.PhidgetException;
import exception.SpiceNotPresentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Axel on 29-04-16.
 */
public class RoueEpices {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
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

    // Determines whether the Spice Wheel is in init mode.
    private boolean init = false;
    // The last tag read and whether it belongs to a tag gain event or tag lost event. (Used in the RFID listeners).
    private String listenerTag = new String();
    private boolean listenerTagGain = false;


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
            try{Thread.sleep(1000);} catch(Exception e){}
            activatedRFID = true;
        }
        catch (PhidgetException e)
        {
            logger.info("Une erreur est survenue lors de l'initalisation du lecteur RFID : " + e.toString());
        }

        // initialisation des emplacements et des positions :
        for(int i = 0; i < MAX; i++)emplacements[i] = new Spice();


        positions[0] = 0.00;
        positions[1] = 50.00;
        positions[2] = 83.00;
        positions[3] = 114.00;
        positions[4] = 142.00;
        positions[5] = 180.00;

        // Récupération de la liste des épices
        try {
            StorageClient storageClient = new StorageClient();

            List<Spice> liste = storageClient.readSpiceList();
            int i = 0;
            for (Spice spice:liste){
                logger.info(spice.toString());
                BDCEpices[i] = spice;
                i++;
            }

        } catch (Exception e){
            logger.info("Erreur de récupération de la liste des épices à l'initialisation");
            logger.info(e.toString());
        }

        //Initialisation des épices de la roue.
        init = true;
        try {
            activateRFID();
            Thread.sleep(1000);

            initialisationEmplacements();
            desactivateRFID();
        } catch (Exception e) {
            logger.info("initialisationEmplacements() Failed!");
            e.printStackTrace();
            logger.info(e.toString());
        } finally {
            init = false;
        }
    }

    /**
     * Activate the Spice wheel before use.
     * The Activation consists in activating the servo motor.
     */
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

    /**
     * Deactivate the Spice wheel after use.
     * The deactivation of the wheel is a simple deactivation of the servo motor.
     */
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
        else emplacements[emplacement - 1] = new Spice();

        try{
            Wrapper wrapper = new Wrapper(Request.updateSpiceBoxContent, emplacements[emplacement - 1], String.valueOf(emplacement));
            StorageClient storageClient = new StorageClient();
            storageClient.updateSpiceBoxContent(wrapper);}
        catch(Exception e){logger.info(e.getMessage());}

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

    /**
     * Move the head of the Spice Wheel to the specified spot.
     * emplacement value must be between MIN and MAX.
     *
     * @param emplacement Number of the spot to move to.
     * @throws Exception
     */
    public void goToEmplacement(int emplacement) throws Exception
    {
        // Activate motor and deactivate RFID Reader
        this.activer();
        //this.rfid.deactivate();

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
        // Deactivate motor and activate RFID Reader
        this.desactiver();
//        this.rfid.activate();
    }

    //Renvoie l'emplacement "box" en fonction de la position du servomoteur et du nombre de "box" de la roue.
    // 1 - 6
    public int getEmplActuel(){
        this.activer();
        try{
            double posAct = servo.getPositionActuelle();
            logger.info("PosAct brute= "+posAct);

            for(int i =0; i<MAX; i++){
                if (posAct <= positions[i]+5){
                    return i+1;
                }
            }

            return -1;
        }
        catch (PhidgetException e)
        {
            System.out.println("Une Erreur moteur est survenue : " + e.toString());
            return -1;
        }
        finally {
            this.desactiver();
        }
    }

    public Spice[] getEmplacement(){
        return emplacements;
    }

    public Boolean isInBDC(String tagRFID){
        for(int i=0; i < MAXEPICES; i++){
            if (BDCEpices[i] != null && BDCEpices[i].getBarCode().equals(tagRFID) ){
                return true;
            }
        }
        return false;
    }

    public String getNomBDCE(String tagRFID){
        for(int i=0; i < MAXEPICES; i++){
            if (BDCEpices[i].getBarCode().equals(tagRFID) ){
                return BDCEpices[i].getName();
            }
        }
        return " ";
    }

    public String getDescBDCE(String tagRFID){
        for(int i=0; i < MAXEPICES; i++){
            if (BDCEpices[i].getBarCode().equals(tagRFID) ){
                return BDCEpices[i].getDescription();
            }
        }
        return " ";
    }


    public String getSpiceIdBDCE(String tagRFID){
        for(int i=0; i < MAXEPICES; i++){
            if (BDCEpices[i].getBarCode().equals(tagRFID) ){
                return BDCEpices[i].getSpiceId();
            }
        }
        return " ";
    }

    public Spice getSpiceBDCE(String tagRFID){
        for (int i=0; i < MAXEPICES; i++){
            if(BDCEpices[i].getBarCode().equals(tagRFID)){
                return BDCEpices[i];
            }
        }
        return new Spice();
    }

	/*
	 * Initialisation de la roue à épices, s'utilise à la création de la roue à épices et lors de mise à niveau de la roue
	 */

    public void initialisationEmplacements() throws Exception, PhidgetException{

        for(int i = 0; i < MAX; i++) {
            logger.info("moving");
            //Va à l'emplcement
            goToEmplacement(i+1);

            logger.info("Emplacement reached");
            String tag = rfid.getTag();
            logger.info("Tag = "+tag);
            if(!tag.equals("") && i>0){
                if(tag.equals(emplacements[i-1].getBarCode())){
                    tag = "";
                }
            }
            //MàJ des emplcements
            if (isInBDC(tag)){
                logger.info("isInBDC true");
                emplacements[i].setName(getNomBDCE(tag));
                emplacements[i].setDescription(getDescBDCE(tag));
                emplacements[i].setBarCode(tag);
                emplacements[i].setSpiceId(getSpiceIdBDCE(tag));
            }
            logger.info("exited isInBDC");

            try{
                Wrapper wrapper = new Wrapper(Request.updateSpiceBoxContent, emplacements[i], String.valueOf(i+1));
                StorageClient storageClient = new StorageClient();
                storageClient.updateSpiceBoxContent(wrapper);}
            catch(Exception e){logger.info(e.getMessage());}
        }
    }

	/*
	 * Mets à jour le contenu d'un emplacement de la roue, s'utilise à chaque modification du contenu physique de la roue à épices.
	 */

    public void majEmplacements(String tag){

        int emplacementActuel = getEmplActuel()-1;
        Spice spice = getSpiceBDCE(tag);

        emplacements[emplacementActuel] = spice;



/*        logger.info("Emplacement actuel: "+getEmplActuel());
        emplacements[getEmplActuel()-1].setName(getNomBDCE(tag));
        emplacements[getEmplActuel()-1].setDescription(getDescBDCE(tag));
        emplacements[getEmplActuel()-1].setBarCode(tag);
        emplacements[getEmplActuel()-1].setSpiceId(getSpiceIdBDCE(tag));*/
        try{
            Wrapper wrapper = new Wrapper(Request.updateSpiceBoxContent, emplacements[emplacementActuel], String.valueOf(emplacementActuel+1));

            StorageClient storageClient = new StorageClient();
            storageClient.updateSpiceBoxContent(wrapper);}
        catch(Exception e){e.printStackTrace();}
    }

	/*
	 * Activer ou désactiver le lecteur RFID
	 */

    public void activateRFID(){
        activatedRFID = true;
        rfid.activate();
    }

    public void desactivateRFID(){
        activatedRFID = false;
        rfid.deactivate();
    }

    public boolean isRFIDActivated(){
        return activatedRFID;
    }

	/*
	 * A Appeller si on veut sélectionner une épice
	 */

    public boolean askEpice(String name) throws SpiceNotPresentException{
        desactivateRFID();
        try {
            logger.info("Asking for spice: " + name);
            for (int i = 0; i < MAX; i++) {
                logger.info("Contenu du tableau");
                logger.info(emplacements[i].toString());

                if (emplacements[i].getName().equalsIgnoreCase(name) ) {
                    try {
                        goToEmplacement(i + 1);
                        return true;
                    } catch (Exception e) {
                        logger.info("Error while moving toward the spice");
                        return false;
                    }
                }
            }
            activateRFID();
            return false;
        }
        catch (Exception e) {e.printStackTrace();}
        finally {
            activateRFID();
            return false;
        }

    }


    public RFIDReader getRfidReader(){return rfid;}

    public boolean isInInitMode(){return init;}


    public String getListenerTag(){return listenerTag;}
    public boolean getListenerTagGain(){return listenerTagGain;}
    public void setListenerTag(String listenerTag){this.listenerTag = listenerTag;}
    public void setListenerTagGain(boolean listenerTagRead){this.listenerTagGain = listenerTagRead;}
}
