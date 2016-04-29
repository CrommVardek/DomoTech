package StorageManager;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Properties;

import org.apache.log4j.Logger;

import commonsObjects.Action;
import commonsObjects.Agenda;
import commonsObjects.LightSensorMonitoring;
import commonsObjects.List;
import commonsObjects.Message;
import commonsObjects.Parameter;
import commonsObjects.Request;
import commonsObjects.Spice;
import commonsObjects.TemperatureSensorMonitoring;
import commonsObjects.Room;
import commonsObjects.Wrapper;
import driver.MySqlDriver;
import exception.MySqlDriverException;
import exception.StorageManagerException;


public class StorageServer implements Runnable {
	
	private static final Logger logger = Logger.getLogger(StorageServer.class);
	private String label;
	private int port;
	private int queueSize;
	private boolean isEnable;
	private InetAddress ipAddress;
	private ServerSocket serverSocket;
	private MySqlDriver sqlDriver;
	private String path = "./../StorageServer.properties";
	
/* CONSTRUCTEURS */
	
	public StorageServer( String label, int port, int queueSize, boolean isEnable, InetAddress ipAdress, ServerSocket serverSocket)
	{
		this.label = label;
		this.port = port;
		this.queueSize = queueSize;
		this.isEnable = isEnable;
		this.ipAddress = ipAdress;
		this.serverSocket = serverSocket;
		try
		{
			this.sqlDriver = new MySqlDriver();
		}
		catch (MySqlDriverException e)
		{
			logger.error(""
			+ "StorageServer : Initialisation of MySql Driver failed ! \n "
			+ "Please check driver's parameters and try to reload the StorageManager. \n"
			+ "The database in not accessible at the moment."
			+ "Details : " + e.toString());
		}
	}
	
	public StorageServer(String label)
	{
		try
		{
			this.label="Storage Server";
			this.loadParametersFromFile(this.label);
			this.sqlDriver = new MySqlDriver("MySQL Driver");
		}
		catch (MySqlDriverException | StorageManagerException e)
		{
			logger.error(""
			+ "StorageServer : Initialisation of MySql Driver failed ! \n "
			+ "Please check driver's parameters and try to reload the StorageManager. \n"
			+ "The database in not accessible at the moment."
			+ "Details : " + e.toString());
		}
	}
	
	public StorageServer()
	{
		this("Storage Server");
	}

/* METHODE RUN DE GESTION DES REQUETES CLIENT SERVEUR : */

	public void run()
	{
		/*
		 * Code assurant le traitemment des requêtes émises par les clients. La méthode run gère uniquement
		 * la mécanique de connexion : capture de la requête et envoi de la réponse à cette requête.
		 * Seule la méthode handler permet de définir quel action effectuer en fonction de quel objet.
		 */
		logger.info("[" +this.label + "] Listening on  " + serverSocket.getLocalSocketAddress());
		logger.info("[" + this.label + "]* MODULE STARTED");
		
		while (isEnable())
		{
			// try dans la boucle permet de ne pas couper la boucle si une requête échoue. On prendra la suivante.
			// sinon, c'est la boucle entière qui s'arrète et le traitement renvoie une exception.
			try 
			{
				Socket socket = serverSocket.accept();
				logger.info("[" +this.label + "]-> A client connection has been accepted on port " + socket);
				
				ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
				ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
				out.flush();
				logger.trace("[" +this.label + "]-> Streams requierd for exchanges created");
				
				Wrapper request = (Wrapper) in.readObject();
				logger.trace("[" +this.label + "]-> Request received from client");
				
				logger.trace("[" +this.label + "]-> Client's request dispatched to handler ");
				Wrapper answer = this.handler(request);
				logger.trace("[" +this.label + "]-> Client's answer got from handler ");
				
				out.writeObject(answer);
				out.flush();
				logger.trace("[" +this.label + "]-> Answer sended to client");
				
				in.close();
				out.close();
				socket.close();
				logger.info("[" +this.label + "]-> End of Connection");
			}
			catch (IOException | ClassCastException | ClassNotFoundException e) 
			{
				if (e.getMessage().compareToIgnoreCase("Socket Closed") != 0)
				logger.warn("Error(s) while dispatching an incoming request :\n" + e.toString());
			} 
		}
		
		logger.trace("[" +this.label + "] Stop order received and socket disabled.");
		
	}
	
 public Wrapper handler( Wrapper wrapper)
 {
	 /*
	  * La méthode handler permet d'associer une réponse à l'objet passé en argument qui représente une requête.
	  * Si la requête réussit alors handler renvoit la réponse associée. Sinon, c'est une exception qui est lancée. 
	  */
	String msgOK = "OK";
	String msgKO = "KO";
	switch (wrapper.getRequest())
	{
	// Room
		case createRoom :
			logger.info("[" +this.label + "]-> Creation Room demand received !");
			try
			{
				sqlDriver.createRoom( (Room) wrapper.getContainer());
				logger.info("[" +this.label + "]-> Creation Room demand answered !");
				return new Wrapper(Request.createRoom, new Message ("OK"), msgOK);
			}
			catch(MySqlDriverException e)
			{
				logger.info("[" +this.label + "]-> Creation Room demand ended by an error : " + e.toString());
				return new Wrapper(Request.createRoom, new Message("ERROR : " + e.getExceptionList()), msgKO);
			}
				
		case  readRoomByName :
			logger.info("[" +this.label + "]-> Read Room By Name demand received !");	
			try
			{
				Room room = sqlDriver.readRoomByName( ((Room) wrapper.getContainer()).getName());
				logger.info("[" +this.label + "]-> Read Room By Name demand answered !");
				return new Wrapper(Request.readRoomByName, room, msgOK);
			}
			catch(MySqlDriverException e)
			{
				logger.info("[" +this.label + "]-> Read Room By Name demand ended by an error : " + e.toString());
				return new Wrapper(Request.readRoomByName, new Message("ERROR : " + e.getExceptionList()), msgKO);
			}
			
		case readRoomById :
			logger.info("[" +this.label + "]-> Read Room By Id demand received !");	
			try
			{
				Room room = sqlDriver.readRoomById(((Room) wrapper.getContainer()).getRoomId());
				logger.info("[" +this.label + "]-> Read Room By Id demand answered !");
				return new Wrapper(Request.readRoomById, room, msgOK);
			}
			catch(MySqlDriverException e)
			{
				logger.info("[" +this.label + "]-> Read Room By Id demand ended by an error : " + e.toString());
				return new Wrapper(Request.readRoomById, new Message("ERROR : " + e.getExceptionList()), msgKO);
			}
				
		case readRoomList :
			logger.info("[" +this.label + "]-> Read Room By Name demand received !");	
			try
			{
				List<Room> list = sqlDriver.readRoomList();
				logger.info("[" +this.label + "]-> Read Room By Name demand answered !");
				return new Wrapper(Request.readRoomList, list, msgOK);
			}
			catch(MySqlDriverException e)
			{
				logger.info("[" +this.label + "]-> Read Room By Name demand ended by an error : " + e.toString());
				return new Wrapper(Request.readRoomList, new Message("ERROR : " + e.getExceptionList()), msgKO);
			}
				
		case updateRoom :
			logger.info("[" +this.label + "]-> Update Room demand received !");
			try
			{
				sqlDriver.updateRoom( (Room) wrapper.getContainer());
				logger.info("[" +this.label + "]-> Update Room demand answered !");
				return new Wrapper(Request.updateRoom, new Message ("OK"), msgKO);
			}
			catch(MySqlDriverException e)
			{
				logger.info("[" +this.label + "]-> Update Room demand ended by an error : " + e.toString());
				return new Wrapper(Request.updateRoom, new Message("ERROR : " + e.getExceptionList()), msgOK);
			}
				
		case deleteRoom :
			logger.info("[" +this.label + "]-> Delete Room demand received !");
			try
			{
				sqlDriver.deleteRoom( (Room) wrapper.getContainer());
				logger.info("[" +this.label + "]-> Delete Room demand answered !");
				return new Wrapper(Request.deleteRoom, new Message ("OK"), msgOK);
			}
			catch(MySqlDriverException e)
			{
				logger.info("[" +this.label + "]-> Delete Room demand ended by an error : " + e.toString());
				return new Wrapper(Request.deleteRoom, new Message("ERROR : " + e.getExceptionList()), msgKO);
			}
				
	//Action
		case createAction :
			logger.info("[" +this.label + "]-> Creation Action demand received !");
			try
			{
				sqlDriver.createAction((Action) wrapper.getContainer());
				logger.info("[" +this.label + "]-> Creation Action demand answered !");
				return new Wrapper(Request.createAction, new Message ("OK"), msgOK);	
			}
			catch(MySqlDriverException e)
			{
				logger.info("[" +this.label + "]-> Creation Action demand ended by an error : " + e.toString());
				return new Wrapper(Request.createAction, new Message("ERROR : " + e.getExceptionList()), msgKO);
			}
				
		case readActionById :
			logger.info("[" +this.label + "]-> Read Action By Id demand received !");	
			try
			{
				Action action = sqlDriver.readActionById(((Action) wrapper.getContainer()).getActionId());
				logger.info("[" +this.label + "]-> Read Action By Id demand answered !");
				return new Wrapper(Request.readActionById, action, msgOK);
			}
			catch(MySqlDriverException e)
			{
				logger.info("[" +this.label + "]-> Read Action By Id demand ended by an error : " + e.toString());
				return new Wrapper(Request.readActionById, new Message("ERROR : " + e.getExceptionList()), msgKO);
			}
				
		case readActionByName :
			logger.info("[" +this.label + "]-> Read Action By Name demand received !");	
			try
			{
				Action action = sqlDriver.readActionById(((Action) wrapper.getContainer()).getName());
				logger.info("[" +this.label + "]-> Read Action By Name demand answered !");
				return new Wrapper(Request.readActionByName, action, msgOK);
			}
			catch(MySqlDriverException e)
			{
				logger.info("[" +this.label + "]-> Read Action By Name demand ended by an error : " + e.toString());
				return new Wrapper(Request.readActionByName, new Message("ERROR : " + e.getExceptionList()), msgKO);
			}
				
		case readActionList :
			logger.info("[" +this.label + "]-> Read Action List demand received !");	
			try
			{
				List<Action> list = sqlDriver.readActionList();
				logger.info("[" +this.label + "]-> Read Action List demand answered !");
				return new Wrapper(Request.readActionList, list, msgOK);
			}
			catch(MySqlDriverException e)
			{
				logger.info("[" +this.label + "]-> Read Action List demand ended by an error : " + e.toString());
				return new Wrapper(Request.readActionList, new Message("ERROR : " + e.getExceptionList()), msgKO);
			}
				
		case updateAction :
			logger.info("[" +this.label + "]-> Update Action demand received !");
			try
			{
				sqlDriver.updateAction((Action) wrapper.getContainer());
				logger.info("[" +this.label + "]-> Update Action demand answered !");
				return new Wrapper(Request.updateAction, new Message ("OK"), msgOK);	
			}
			catch(MySqlDriverException e)
			{
				logger.info("[" +this.label + "]-> Update Action demand ended by an error : " + e.toString());
				return new Wrapper(Request.updateAction, new Message("ERROR : " + e.getExceptionList()), msgKO);
			}
				
		case deleteAction :
			logger.info("[" +this.label + "]-> Delete Action demand received !");
			try
			{
				sqlDriver.updateAction((Action) wrapper.getContainer());
				logger.info("[" +this.label + "]-> Delete Action demand answered !");
				return new Wrapper(Request.deleteAction, new Message ("OK"), msgOK);	
			}
			catch(MySqlDriverException e)
			{
				logger.info("[" +this.label + "]-> Delete Action demand ended by an error : " + e.toString());
				return new Wrapper(Request.deleteAction, new Message("ERROR : " + e.getExceptionList()), msgKO);
			}

	//Agenda
		case createAgenda :
			logger.info("[" +this.label + "]-> Creation Agenda demand received !");
			try
			{
				sqlDriver.createAgenda((Agenda) wrapper.getContainer());
				logger.info("[" +this.label + "]-> Creation Agenda demand answered !");
				return new Wrapper(Request.createAgenda, new Message ("OK"), msgOK);	
			}
			catch(MySqlDriverException e)
			{
				logger.info("[" +this.label + "]-> Creation Agenda demand ended by an error : " + e.toString());
				return new Wrapper(Request.createAgenda, new Message("ERROR : " + e.getExceptionList()), msgKO);
			}
				
		case readAgendaByDayNumber :
			logger.info("[" +this.label + "]-> Read Agenda By Day Number demand received !");	
			try
			{
				List<Agenda> list = sqlDriver.readAgendaByDayNumber(((Agenda) wrapper.getContainer()).getDayId());
				logger.info("[" +this.label + "]-> Read Agenda By Day Number demand answered !");
				return new Wrapper(Request.readAgendaByDayNumber, list, msgKO);
			}
			catch(MySqlDriverException e)
			{
				logger.info("[" +this.label + "]-> Read Agenda By Day Number demand ended by an error : " + e.toString());
				return new Wrapper(Request.readAgendaByDayNumber, new Message("ERROR : " + e.getExceptionList()), msgOK);
			}
				
		case readAgendaByRoomId :
			logger.info("[" +this.label + "]-> Read Agenda By Room Id demand received !");	
			try
			{
				List<Agenda> list = sqlDriver.readAgendaByRoomId(((Agenda) wrapper.getContainer()).getRoomId());
				logger.info("[" +this.label + "]-> Read Agenda By Room Id demand answered !");
				return new Wrapper(Request.readAgendaByRoomId, list, msgOK);
			}
			catch(MySqlDriverException e)
			{
				logger.info("[" +this.label + "]-> Read Agenda By Room Id demand ended by an error : " + e.toString());
				return new Wrapper(Request.readAgendaByRoomId, new Message("ERROR : " + e.getExceptionList()), msgKO);
			}
				
		case readWeekAgenda :
			logger.info("[" +this.label + "]-> Read Week Agenda demand received !");	
			try
			{
				List<Agenda> list = sqlDriver.readWeekAgenda();
				logger.info("[" +this.label + "]-> Read Week Agenda demand answered !");
				return new Wrapper(Request.readWeekAgenda, list, msgOK);
			}
			catch(MySqlDriverException e)
			{
				logger.info("[" +this.label + "]-> Read Week Agenda demand ended by an error : " + e.toString());
				return new Wrapper(Request.readWeekAgenda, new Message("ERROR : " + e.getExceptionList()), msgKO);
			}
				
		case deleteAgenda :
			logger.info("[" +this.label + "]-> Delete Agenda demand received !");
			try
			{
				sqlDriver.deleteAgenda((Agenda) wrapper.getContainer());
				logger.info("[" +this.label + "]-> Delete Agenda demand answered !");
				return new Wrapper(Request.deleteAgenda, new Message ("OK"), msgOK);	
			}
			catch(MySqlDriverException e)
			{
				logger.info("[" +this.label + "]-> Delete Agenda demand ended by an error : " + e.toString());
				return new Wrapper(Request.deleteAgenda, new Message("ERROR : " + e.getExceptionList()), msgKO);
			}
			
	//Spice
		case createSpice :
			logger.info("[" +this.label + "]-> Creation Spice demand received !");
			try
			{
				sqlDriver.createSpice((Spice) wrapper.getContainer());
				logger.info("[" +this.label + "]-> Creation Spice demand answered !");
				return new Wrapper(Request.createSpice, new Message ("OK"), msgOK);	
			}
			catch(MySqlDriverException e)
			{
				logger.info("[" +this.label + "]-> Creation Spice demand ended by an error : " + e.toString());
				return new Wrapper(Request.createSpice, new Message("ERROR : " + e.getExceptionList()), msgKO);
			}
				
		case readSpiceById :
			logger.info("[" +this.label + "]-> Read Spice By Id demand received !");	
			try
			{
				Spice spice = sqlDriver.readSpiceById(((Spice) wrapper.getContainer()).getSpiceId());
				logger.info("[" +this.label + "]-> Read Spice By Id demand answered !");
				return new Wrapper(Request.readSpiceById, spice, msgOK);
			}
			catch(MySqlDriverException e)
			{
				logger.info("[" +this.label + "]-> Read Spice By Id demand ended by an error : " + e.toString());
				return new Wrapper(Request.readSpiceById, new Message("ERROR : " + e.getExceptionList()), msgKO);
			}
				
		case readSpiceByName :
			logger.info("[" +this.label + "]-> Read Spice By Name demand received !");	
			try
			{
				Spice spice = sqlDriver.readSpiceByName(((Spice) wrapper.getContainer()).getName());
				logger.info("[" +this.label + "]-> Read Spice By Name demand answered !");
				return new Wrapper(Request.readSpiceByName, spice, msgOK);
			}
			catch(MySqlDriverException e)
			{
				logger.info("[" +this.label + "]-> Read Spice By Name demand ended by an error : " + e.toString());
				return new Wrapper(Request.readSpiceByName, new Message("ERROR : " + e.getExceptionList()), msgKO);
			}
		
		case readSpiceByBarCode :
			logger.info("[" +this.label + "]-> Read Spice By Name demand received !");	
			try
			{
				Spice spice = sqlDriver.readSpiceByBarCode(((Spice) wrapper.getContainer()).getBarCode());
				logger.info("[" +this.label + "]-> Read Spice By barCode demand answered !");
				return new Wrapper(Request.readSpiceByBarCode, spice, msgOK);
			}
			catch(MySqlDriverException e)
			{
				logger.info("[" +this.label + "]-> Read Spice By barCode demand ended by an error : " + e.toString());
				return new Wrapper(Request.readSpiceByBarCode, new Message("ERROR : " + e.getExceptionList()), msgKO);
			}
				
		case readSpiceList :
			logger.info("[" +this.label + "]-> Read Spice List demand received !");	
			try
			{
				List<Spice> list = sqlDriver.readSpiceList();
				logger.info("[" +this.label + "]-> Read Spice List demand answered !");
				return new Wrapper(Request.readSpiceList, list, msgOK);
			}
			catch(MySqlDriverException e)
			{
				logger.info("[" +this.label + "]-> Read Spice List demand ended by an error : " + e.toString());
				return new Wrapper(Request.readSpiceList, new Message("ERROR : " + e.getExceptionList()), msgKO);
			}
				
		case updateSpice :
			logger.info("[" +this.label + "]-> Update Spice demand received !");
			try
			{
				sqlDriver.updateSpice((Spice) wrapper.getContainer());
				logger.info("[" +this.label + "]-> Update Spice demand answered !");
				return new Wrapper(Request.updateSpice, new Message ("OK"), msgOK);	
			}
			catch(MySqlDriverException e)
			{
				logger.info("[" +this.label + "]-> Update Spice demand ended by an error : " + e.toString());
				return new Wrapper(Request.updateSpice, new Message("ERROR : " + e.getExceptionList()), msgKO);
			}
				
		case deleteSpice :
			logger.info("[" +this.label + "]-> Delete Spice demand received !");
			try
			{
				sqlDriver.deleteSpice((Spice) wrapper.getContainer());
				logger.info("[" +this.label + "]-> Delete Spice demand answered !");
				return new Wrapper(Request.deleteSpice, new Message ("OK"), msgOK);	
			}
			catch(MySqlDriverException e)
			{
				logger.info("[" +this.label + "]-> Delete Spice demand ended by an error : " + e.toString());
				return new Wrapper(Request.deleteSpice, new Message("ERROR : " + e.getExceptionList()), msgKO);
			}
			
	//Parameter
		case createParameter :
			logger.info("[" +this.label + "]-> Create parameter demand received !");
			try
			{
				Parameter p = (Parameter) wrapper.getContainer();
				sqlDriver.createParameter(p.getKey(), p.getParam());
				logger.info("[" +this.label + "]-> Create parameter demand answered !");
				return new Wrapper(Request.createParameter, new Message ("OK"), msgOK);	
			}
			catch(MySqlDriverException e)
			{
				logger.info("[" +this.label + "]-> Update parameter demand ended by an error : " + e.toString());
				return new Wrapper(Request.updateParameter, new Message("ERROR : " + e.getExceptionList()), msgKO);
			}
				
		case readParameterByKey :
			logger.info("[" +this.label + "]-> Read parameter by key demand received !");
			try
			{
				Parameter p = (Parameter) wrapper.getContainer();
				p.setParam(sqlDriver.readParameterByKey(p.getKey()));
				logger.info("[" +this.label + "]-> Read parameter by key demand answered !");
				return new Wrapper(Request.readParameterByKey, p, msgOK);	
			}
			catch(MySqlDriverException e)
			{
				logger.info("[" +this.label + "]-> Read parameter by key demand ended by an error : " + e.toString());
				return new Wrapper(Request.deleteSpice, new Message("ERROR : " + e.getExceptionList()), msgKO);
			}
				
		case updateParameter :
			logger.info("[" +this.label + "]-> Update parameter demand received !");
			try
			{
				Parameter p = (Parameter) wrapper.getContainer();
				sqlDriver.updateParameter(p.getKey(), p.getParam());
				logger.info("[" +this.label + "]-> Update parameter demand answered !");
				return new Wrapper(Request.updateParameter, new Message ("OK"), msgOK);	
			}
			catch(MySqlDriverException e)
			{
				logger.info("[" +this.label + "]-> Update parameter demand ended by an error : " + e.toString());
				return new Wrapper(Request.updateParameter, new Message("ERROR : " + e.getExceptionList()), msgKO);
			}
				
		case deleteParameterByKey :
			logger.info("[" +this.label + "]-> Delete parameter demand received !");
			try
			{
				Parameter p = (Parameter) wrapper.getContainer();
				sqlDriver.deleteParameterByKey(p.getKey());
				logger.info("[" +this.label + "]-> Delete parameter demand answered !");
				return new Wrapper(Request.deleteSpice, new Message ("OK"), msgOK);	
			}
			catch(MySqlDriverException e)
			{
				logger.info("[" +this.label + "]-> Delete parameter demand ended by an error : " + e.toString());
				return new Wrapper(Request.deleteSpice, new Message("ERROR : " + e.getExceptionList()), msgKO);
			}
			
	//SpiceBox
		case readSpiceBoxContent :
			/*
			 * Le champ string du wrapper contient le numéro de la box à analyser.
			 */
			logger.info("[" +this.label + "]-> Read Spice box content demand received !");
			try
			{
				Spice spice = sqlDriver.readSpiceBoxContent(wrapper.getString());
				logger.info("[" +this.label + "]-> Read Spice box content demand answered !");
				Wrapper w = new Wrapper(Request.readSpiceBoxContent, spice);
				
				if (spice.getName().equalsIgnoreCase("Empty_Name")) wrapper.setString("Empty_Box");
				else wrapper.setString("Full_Box");
				
				return w; 	
			}
			catch(MySqlDriverException e)
			{
				logger.info("[" +this.label + "]-> Read Spice box content demand ended by an error : " + e.toString());
				return new Wrapper(Request.readSpiceBoxContent, new Message("ERROR : " + e.getExceptionList()));
			}
				
		case updateSpiceBoxContent :
			/*
			 * Le champ string du wrapper contient le numéro de la box à mettre à jour
			 * Le champ container contient l'objet épice qui doit être placé dans la box.
			 */
			logger.info("[" +this.label + "]-> Update Spice box content demand received !");
			try
			{
				sqlDriver.updateSpiceBoxContent(wrapper.getString(), ((Spice) wrapper.getContainer()).getSpiceId());
				logger.info("[" +this.label + "]-> Update Spice box content demand answered !");		
				return new Wrapper(Request.updateSpiceBoxContent, new Message("OK"), msgOK); 	
			}
			catch(MySqlDriverException e)
			{
				logger.info("[" +this.label + "]-> Read Spice box content demand ended by an error : " + e.toString());
				return new Wrapper(Request.updateSpiceBoxContent, new Message("ERROR : " + e.getExceptionList()), msgKO);
			}
				
		case deleteSpiceBoxContent :
			/*
			 * Le champ string du wrapper contient l numéro de la box dont le contenu doit
			 * être supprimé.
			 */
			logger.info("[" +this.label + "]-> Delete Spice box content demand received !");
			try
			{
				sqlDriver.deleteSpiceBoxContent(wrapper.getString());
				logger.info("[" +this.label + "]-> Delete Spice box content demand answered !");		
				return new Wrapper(Request.updateSpiceBoxContent, new Message("OK"), msgOK); 	
			}
			catch(MySqlDriverException e)
			{
				logger.info("[" +this.label + "]-> Delete Spice box content demand ended by an error : " + e.toString());
				return new Wrapper(Request.updateSpiceBoxContent, new Message("ERROR : " + e.getExceptionList()), msgKO);
			}
			
	// LightSensor
		case createLightSensorMonitoring :
			logger.info("[" +this.label + "]-> Create Light Sensor Monitoring demand received !");
			try
			{
				sqlDriver.createLightSensorMonitoring((LightSensorMonitoring) wrapper.getContainer());
				logger.info("[" +this.label + "]-> Create Light Sensor Monitoring demand answered !");
				return new Wrapper(Request.createLightSensorMonitoring, new Message ("OK"), msgOK);	
			}
			catch(MySqlDriverException e)
			{
				logger.info("[" +this.label + "]-> Create Light Sensor Monitoring demand ended by an error : " + e.toString());
				return new Wrapper(Request.createLightSensorMonitoring, new Message("ERROR : " + e.getExceptionList()), msgKO);
			}
				
		case readLightSensorMonitoringList :
			logger.info("[" +this.label + "]-> Read Light Sensor Monitoring list demand received !");
			try
			{
				List<LightSensorMonitoring> list = sqlDriver.readLightSensorMonitoringList();
				logger.info("[" +this.label + "]-> Read Light Sensor Monitoring list  demand answered !");
				return new Wrapper(Request.readLightSensorMonitoringList, list, msgOK);	
			}
			catch(MySqlDriverException e)
			{
				logger.info("[" +this.label + "]-> Read Light Sensor Monitoring list demand ended by an error : " + e.toString());
				return new Wrapper(Request.readLightSensorMonitoringList, new Message("ERROR : " + e.getExceptionList()), msgKO);
			}

	//TemperatureSensor
		case createTemperatureSensorMonitoring :
			logger.info("[" +this.label + "]-> Create Temperature Sensor Monitoring demand received !");
			try
			{
				sqlDriver.createTemperatureSensorMonitoring((TemperatureSensorMonitoring) wrapper.getContainer());
				logger.info("[" +this.label + "]-> Create Temperature Sensor Monitoring demand answered !");
				return new Wrapper(Request.createTemperatureSensorMonitoring, new Message ("OK"), msgOK);	
			}
			catch(MySqlDriverException e)
			{
				logger.info("[" +this.label + "]-> Create Temperature Sensor Monitoring demand ended by an error : " + e.toString());
				return new Wrapper(Request.createTemperatureSensorMonitoring, new Message("ERROR : " + e.getExceptionList()), msgKO);
			}
				
		case readTemperatureSensorMonitoringList :
			logger.info("[" +this.label + "]-> Read Temperature Sensor Monitoring list demand received !");
			try
			{
				List<TemperatureSensorMonitoring> list = sqlDriver.readTemperatureSensorMonitoringList();
				logger.info("[" +this.label + "]-> Read Temperature Sensor Monitoring list  demand answered !");
				return new Wrapper(Request.readTemperatureSensorMonitoringList, list, msgOK);	
			}
			catch(MySqlDriverException e)
			{
				logger.info("[" +this.label + "]-> Read Temperature Sensor Monitoring list demand ended by an error : " + e.toString());
				return new Wrapper(Request.readTemperatureSensorMonitoringList, new Message("ERROR : " + e.getExceptionList()), msgKO);
			}
					
		default  :
			logger.warn(""
					+ "[" +this.label + "]-> Illegal request received, abording processing and wait for the next request ! \n"
					+ "Details : " + wrapper.toString());
			return new Wrapper(Request.StorageManagerException, new Message("ERROR : Illegal request received !"), msgKO);		
	}
 }
	
/* METHODES DE GESTION DU SERVEUR DE STOCKAGE : enable, disable et reload */
	
	public synchronized void enable() throws StorageManagerException
	{
		logger.info("[" + this.label + "]* STARTING STORAGE SERVER...");
		
		if(isEnable()) 
		{
			logger.warn("[" + this.label + "]* STORAGE SERVER ALREADY STARTED...");
			return;
		}
		
		try
		{
			this.sqlDriver.enable();
			this.serverSocket = new ServerSocket(this.port, this.queueSize, this.ipAddress);
			logger.info("[" +this.label + "] Storage server loaded on address " +  serverSocket.getLocalSocketAddress());
			this.setValueOfEnable(true);
			Thread t = new Thread(this);
			t.start();
		}
		catch (MySqlDriverException e)
		{
			throw new StorageManagerException("MySqlDriver error : " + e.getExceptionList());
		}
		catch (UnknownHostException e)
		{
			throw new StorageManagerException(""
			+ " Invalid IP address specified in the file 'storageServer.properties'."
			+ " Abording creation of Storage server '" + label + "':\n" + e.getMessage());
		}
		catch (IOException e)
		{
			throw new StorageManagerException(""
			+ " Input/Output error(s) during activation of server socket."
			+ " Abording creation of Storage server '" + label + "' : \n" + e.getMessage());
		}

		/*
		 * Un thread exécute le corps de la boucle (méthode run ici-dessus) pour pouvoir l'arrêter avec la méthode disable().
		 */
		
	}
	
	public synchronized void disable() throws StorageManagerException
	{
		logger.info("[" +this.label + "]* DISABLING STORAGE SERVER");
		if ( ! this.isEnable())
		{
			logger.warn("[" +this.label + "]* STORAGE SERVER ALREADY DISABLED");
			return;
		}		
		try
		{
			this.setValueOfEnable(false);
			this.serverSocket.close();
			this.sqlDriver.disable();
			logger.info("[" +this.label + "]* STORAGE SERVER DISABLED");
		}
		catch (MySqlDriverException | IOException e)
		{
			logger.error("[" +this.label + "]* STORAGE SERVER CAN'T BE PROPERLY DISABLED : " + e.toString());
			throw new StorageManagerException("Error(s) while disabling the storage server : " + e.getMessage());
		}	
	}
	
	public synchronized void reload() throws StorageManagerException
	{
		try
		{
			logger.info("[" +this.label + "]* RELOADING STORAGE SERVER");
			this.disable();
			this.loadParametersFromFile(this.label);
			this.sqlDriver.reload();
			this.enable();
			logger.info("[" +this.label + "]* STORAGE SERVER RELOADED");
		}
		catch(MySqlDriverException | StorageManagerException e)
		{
			logger.error("[" +this.label + "]* STORAGE SERVER CAN'T BE RELOADED : " + e.toString());
			throw new StorageManagerException("Error(s) while reloading the storage server : " + e.getMessage());
		}
	}
	
/* METHODES UTILITAIRES PRIVEES : */
	private synchronized void loadParametersFromFile(String label) throws StorageManagerException
	{
		
		Properties properties = new Properties();
		InputStream input = null;
		
		try
		{	
			logger.debug("[" +this.label + "] TECHNICAL PIECES OF INFORMATION :");
			logger.debug("-----------------------------------------------------");
		//	logger.debug("  -> java.class.path : " + System.getProperty("java.class.path"));
			logger.debug("  -> java.home : " + System.getProperty("java.home"));
			logger.debug("  -> Operating system : " + System.getProperty("os.arch") + ", " + System.getProperty("os.version"));
			logger.debug("  -> Working directory : " +  System.getProperty("user.dir"));
			logger.debug("  -> User's name : " + System.getProperty("user.name"));
			logger.debug("  -> Path to configuration file : " + path );
			logger.debug("----------------------------------");
			logger.debug("");
	        
			input = StorageServer.class.getResourceAsStream(path); 
			properties.load(input);
				
			// chargement des paramètres :
			logger.info("[" +this.label + "] LOADED PARAMETERS :");
			logger.info("---------------------------------------");
			
			this.label = properties.getProperty("label");
			logger.info("  -> label : " + this.label);
			
			this.port = Integer.parseInt(properties.getProperty("port"));
			logger.info("  -> port : " + this.port);
			
			this.ipAddress = InetAddress.getByName(properties.getProperty("ipAddress"));
			logger.info("  -> ipAddress : " + this.ipAddress);
			
			this.setValueOfEnable(Boolean.parseBoolean(properties.getProperty("isEnable")));
			logger.info("  -> isEnable : " + this.isEnable);
			
			this.queueSize = Integer.parseInt(properties.getProperty("queueSize"));
			logger.info("  -> queueSize : " + this.queueSize);
				
			logger.info("-------------------");
			logger.info("");
			
		}
		catch (FileNotFoundException e)
		{
			throw new StorageManagerException(""
			+ " The  file '"+ path +"' was not found."
			+ " Abording creation of Storage Server '" + label + "': \n" + e.getMessage() );
		}
		catch (UnknownHostException e)
		{
			throw new StorageManagerException(""
			+ " Unknown IP address specified in the file '"+ path +"'."
			+ " Abording creation of Storage Server '" + label + "': \n" + e.getMessage() );
		}
		catch (IOException e)
		{
			throw new StorageManagerException(""
			+ " Input/Output error(s) with the file '"+ path +"' while opening the file."
			+ " Abording creation of Storage Server '" + label + "': \n" + e.getMessage());
		}
		catch (NullPointerException e)
		{
			throw new StorageManagerException(""
			+ " Null pointer with the file '"+ path +"' while opening the file."
			+ " Abording creation of Storage Server '" + label + "': \n" + e.getMessage());
		}
		catch (NumberFormatException e)
		{
			throw new StorageManagerException(""
			+ " Null pointer with the file '"+ path +"' during conversion of parameters from string to int."
			+ " Abording creation of Storage Server '" + label + "': \n" + e.getMessage());
		}
		catch (Exception e)
		{
			throw new StorageManagerException(""
			+ " Global error with the file '"+ path +"'."
			+ " Abording creation of Storage Server '" + label + "': \n" + e.getMessage());
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
					throw new StorageManagerException(""
					+ " Input/Output error(s) with the file '"+ path +"' while closing the file."
					+ " Abording creation of TCP server '" + label + "': \n" + e.getMessage());
				}
			}
		}
	}

	public synchronized  boolean isEnable()
	{
		return this.isEnable;
	}
	private synchronized void setValueOfEnable(boolean b)
	{
		this.isEnable = b;
	}
}
