package exception;

import java.io.Serializable;

public class StorageManagerException extends Exception implements Serializable {

	private static final long serialVersionUID = 6867695987765515320L;
	boolean isEmpty;
	private String errorMessage;
	
	public StorageManagerException () 
	{
		errorMessage = "";
		isEmpty = true;
	}

	public StorageManagerException (String exception)
	{
		super(exception);
		errorMessage = "";
		isEmpty = true;
		this.addException(exception);
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

	public void displayListException ()
	{
		System.out.println("StorageManager's Exception(s) grabbed during execution :");
		System.out.println("-----------------------------------------------");
		if (this.isEmpty)
		{
			System.out.println("No Exception to display.");
			
			/*
			 * 
		 	logger.warn("\n"
					+ "\t Annomalies survenues dans le module Mail : \n"
					+ "\t " +  message);
			*/
					
		}
		else
		{
			System.out.println(this.errorMessage);
			
			/*
			logger.warn("\n"
					+ "\t Annomalies survenues dans le module Mail : \n"
					+ "\t " +  this.errorMessage);
			*/
		}
	}
	
	public boolean isEmpty ()
	{
		return this.isEmpty;
	}
	
	public String toString()
	{
		return this.getExceptionList();
	}
}
