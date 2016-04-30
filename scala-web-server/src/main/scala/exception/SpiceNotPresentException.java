package exception;

import java.io.Serializable;

/**
 * Created by Axel on 29-04-16.
 */
public class SpiceNotPresentException extends Exception implements Serializable{

    boolean isEmpty;
    private String errorMessage;

    public SpiceNotPresentException()
    {
        errorMessage = "The Spice is not on the Wheel";
        isEmpty = true;
    }


    public void addException (String exception)
    {
        errorMessage += ( exception + "\n");
        isEmpty = false;
    }

    public String getExceptionList()
    {
        return errorMessage;

    }

    public String toString()
    {
        return this.getExceptionList();
    }
}
