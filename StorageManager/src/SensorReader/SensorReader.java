package SensorReader;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Properties;

import org.apache.log4j.Logger;

import StorageManager.StorageClient;
import commonsObjects.LightSensorMonitoring;
import commonsObjects.Request;
import commonsObjects.TemperatureSensorMonitoring;
import commonsObjects.Wrapper;
import exception.SensorReaderException;
import exception.StorageManagerException;



public class SensorReader implements Runnable {
	
	private static final Logger logger = Logger.getLogger(SensorReader.class);
	private boolean status;
	private int cptLight;
	private int cptTemperature;
	private int cptRefreshParameter;
	private StorageClient stoCli;
	private String sensorReader = "./../SensorReader.properties";
	private String sensorReaderConnexion = "./../SensorReaderConnexion.properties";
	
	
/* CONSTRUCTEURS */
	public SensorReader(String name, int port, InetAddress ip, String path)
	{
		this.status = false;
		this.cptLight = 60;
		this.cptTemperature = 60;
		this.cptRefreshParameter = 60;
		this.stoCli = new StorageClient(name, port, ip);
	}
	
	public SensorReader () throws SensorReaderException
	{
		this.loadParametersFromFile(sensorReader);
		try
		{
			this.stoCli = new StorageClient(sensorReaderConnexion);
		} 
		catch (StorageManagerException e)
		{
			throw new SensorReaderException("Unable to load Storageclient properties : " + e.toString());
		}
	}
	
	
	
/* METHODES PUBLIQUES */	
	public synchronized void enable()
	{
		if (isEnable()) return;
		this.status = true;
		Thread t = new Thread(this);
		t.start();
	}
	
	public synchronized void disable()
	{
		if( ! isEnable()) return;
		this.status = false;
	}
	
	public synchronized void reload()
	{
		this.disable();
		this.enable();
	}
	
	public synchronized boolean isEnable()
	{
		return this.status;
	}

/* ORDONNANCEUR POUR LA GESTION DES EVENEMENTS  */
	public void run()
	{
		int lightCmptr = this.cptLight;
		int temperatureCmptr = this.cptTemperature;
		int refreshCmptr = this.cptRefreshParameter;
		
		while (isEnable())
		{
			try
			{
				Thread.sleep(1000);
			} 
			catch (InterruptedException e)
			{
				logger.error("Error in SensorReader Scheduler : " + e.toString());
			}
			
			if (refreshCmptr-- <= 0)
			{
				try
				{
					// chargement des param�tres depuis le fichier
					this.loadParametersFromFile(sensorReader);
					// r�initialisation de la valeur du compteur
					refreshCmptr = this.cptRefreshParameter;
				} 
				catch (SensorReaderException e)
				{
					logger.error("Error in SensorReader Scheduler (refresh Parameters) : " + e.toString());
				}
			}
			
			if (lightCmptr-- <= 0)
			{
				// placer ici un appel sur le capteur de lumi�re pour r�cup�rer la valeur
				
				try
				{
					Wrapper w = new Wrapper(Request.createLightSensorMonitoring, new LightSensorMonitoring());
					this.stoCli.createLightSensorMonitoring(w);
					lightCmptr = this.cptLight;
				}
				catch (StorageManagerException e)
				{
					logger.error("Error in Sensor Reader Scheduler (light sensor) : " + e.toString());
				}
			}
			
			if (temperatureCmptr-- <= 0)
			{
				// placer ici un appel sur le capteur de lumi�re pour r�cup�rer la valeur
				
				try
				{
					Wrapper w = new Wrapper(Request.createTemperatureSensorMonitoring, new TemperatureSensorMonitoring());
					this.stoCli.createTemperatureSensorMonitoring(w);
					temperatureCmptr = this.cptTemperature;
				}
				catch (StorageManagerException e)
				{
					logger.error("Error in Sensor Reader Scheduler (temperature sensor) : " + e.toString());
				}
			}
		}
	}
	
/* MMETHODES PRIVEES */	
	private synchronized void loadParametersFromFile(String path) throws SensorReaderException
	{
		Properties properties = new Properties();
		InputStream input = null;
		
		try
		{	      
			input = SensorReader.class.getResourceAsStream(path); 
			properties.load(input);
				
			// chargement des param�tres :
			logger.debug(" SENSOR READER : LOADED PARAMETERS  :");
			logger.debug("-------------------------------------");
			
			this.cptLight = Integer.parseInt(properties.getProperty("lightSensorIntervall"));
			logger.debug("  -> LightSensorIntervall : " + this.cptLight);
			
			this.cptTemperature = Integer.parseInt(properties.getProperty("temperatureSensorIntervall"));
			logger.debug("  -> temperatureSensorIntervall : " + this.cptLight);
			
			this.cptRefreshParameter = Integer.parseInt(properties.getProperty("refreshParameterIntervall"));
			logger.debug("  -> refreshParameterIntervall : " + this.cptRefreshParameter);
						
			logger.debug("-------------------");
			logger.debug("");
			
		}
		catch (FileNotFoundException e)
		{
			throw new SensorReaderException(""
			+ " The  file '"+ path +"' was not found."
			+ " Abording creation of Sensor Reader : " + e.toString() );
		}
		catch (UnknownHostException e)
		{
			throw new SensorReaderException(""
			+ " Unknown IP address specified in the file '"+ path +"'."
			+ " Abording creation of Sensor Reader : " + e.toString() );
		}
		catch (IOException e)
		{
			throw new SensorReaderException(""
			+ " Input/Output error(s) with the file '"+ path +"' while opening the file."
			+ " Abording creation of Sensor Reader : " + e.toString());
		}
		catch (NullPointerException e)
		{
			throw new SensorReaderException(""
			+ " Null pointer with the file '"+ path +"' while opening the file."
			+ " Abording creation of Sensor Reader  : " + e.toString());
		}
		catch (NumberFormatException e)
		{
			throw new SensorReaderException(""
			+ " Null pointer with the file '"+ path +"' during conversion of parameters from string to int."
			+ " Abording creation of Sensor Reader : " + e.toString());
		}
		catch (Exception e)
		{
			throw new SensorReaderException(""
			+ " Global error with the file '"+ path +"'."
			+ " Abording creation of Sensor Reader : " + e.toString());
		}
		
		finally
		{
			if (input != null)
			{
				try
				{
					input.close();
				}
				catch (IOException e)
				{
					throw new SensorReaderException(""
					+ " Input/Output error(s) with the file '"+ path +"' while closing the file."
					+ " Abording creation of Sensor Reader : " + e.getMessage());
				}
			}
		}
	}
}
