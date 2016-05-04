package commonsObjects;


import com.phidgets.PhidgetException;
import com.phidgets.RFIDPhidget;
import com.phidgets.event.*;
import listeners.RFIDTagGainListener;
import listeners.RFIDTagLossListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Created by Axel on 29-04-16.
 */
public class RFIDReader {

    private RFIDPhidget rfid;
    private RoueEpices re;

    private Logger logger= LoggerFactory.getLogger(RFIDReader.class);

    public RFIDReader (RoueEpices re) throws PhidgetException{
        try {
            this.re = re;
            logger.info("Begining of initialisation (RFID)");
            rfid = new RFIDPhidget();
            rfid.openAny();
            rfid.waitForAttachment(1000);
            addListeners();
        } catch (Exception e){
            logger.info(e.getMessage());
        }
    }

    public void activate(){
        try{
            rfid.setAntennaOn(true);
            rfid.setLEDOn(true);
        }
        catch(Exception e){logger.info(e.getMessage());}
    }

    public void deactivate(){
        try{
            rfid.setAntennaOn(false);
            rfid.setLEDOn(false);
        } catch(Exception e){logger.info(e.getMessage());}
    }


    public void addListeners(){

        //Set up listeners
        RFIDTagGainListener rfidtgl = new RFIDTagGainListener(re);
        RFIDTagLossListener rfidtll = new RFIDTagLossListener(re);

        //Ajout des listeners
        rfid.addTagGainListener(rfidtgl);
        rfid.addTagLossListener(rfidtll);
        rfid.addAttachListener(new AttachListener() {
                                   @Override
                                   public void attached(AttachEvent attachEvent) {
                                       try {
                                           ((RFIDPhidget) attachEvent.getSource()).setAntennaOn(true);
                                           ((RFIDPhidget) attachEvent.getSource()).setLEDOn(true);
                                       } catch (PhidgetException ex) {
                                       }
                                       System.out.println("attachment of " + attachEvent);
                                   }
                               });


        rfid.addDetachListener(new DetachListener() {
            @Override
            public void detached(DetachEvent detachEvent) {
                logger.info("RFID Reader dettached");
            }
        });
        rfid.addErrorListener(new ErrorListener() {
            @Override
            public void error(ErrorEvent errorEvent) {
                logger.info("RFID Reader error");
                logger.info(errorEvent.toString());
            }
        });
    }

    public String getTag() throws PhidgetException{

        if (rfid.getTagStatus() == true ){
            logger.info("getTagStatus = true");
            logger.info(rfid.getLastTag());
            try{Thread.sleep(1000);}catch(Exception e){logger.info(e.getMessage());}
            return rfid.getLastTag();
        }
        else return "";
    }

    public void setRoueEpices(RoueEpices re) {
        this.re = re;
    }
}
