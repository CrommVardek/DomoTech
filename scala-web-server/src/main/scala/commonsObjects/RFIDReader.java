package commonsObjects;


import listeners.RFIDTagGainListener;
import listeners.RFIDTagLossListener;

import com.phidgets.PhidgetException;
import com.phidgets.RFIDPhidget;


/**
 * Created by Axel on 29-04-16.
 */
public class RFIDReader {

    private RFIDPhidget rfid;
    private RoueEpices re;

    public RFIDReader (RoueEpices re) throws PhidgetException{

        this.re = re;
        rfid = new RFIDPhidget();
        rfid.openAny();
        rfid.waitForAttachment();
        addListeners();
    }

    public void addListeners(){

        //Set up listeners
        RFIDTagGainListener rfidtgl = new RFIDTagGainListener(re);
        RFIDTagLossListener rfidtll = new RFIDTagLossListener(re);

        //Ajout des listeners
        rfid.addTagGainListener(rfidtgl);
        rfid.addTagLossListener(rfidtll);
    }

    public String getTag() throws PhidgetException{
        if (rfid.getTagStatus() == true ){
            return rfid.getLastTag();
        }
        else return "";
    }

    public void setRoueEpices(RoueEpices re) {
        this.re = re;
    }
}
