package StorageManager;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Properties;

import org.apache.log4j.Logger;

import commonsObjects.Action;
import commonsObjects.Agenda;
import commonsObjects.LightSensorMonitoring;
import commonsObjects.List;
import commonsObjects.Request;
import commonsObjects.Room;
import commonsObjects.Spice;
import commonsObjects.TemperatureSensorMonitoring;
import commonsObjects.Wrapper;
import commonsObjects.Message;
import commonsObjects.Parameter;
import exception.StorageManagerException;

public class StorageClient {
	
	private static final Logger logger = Logger.getLogger(StorageClient.class);
	private String name;
	private int port;
	private InetAddress ip;
	private final String msgOK = "OK";
	
/* CONSTRUCTEURS : */
	
	public StorageClient (String name, int port, InetAddress ip)
	{
		this.name = name;
		this.port = port;
		this.ip = ip;
	}
	
	public StorageClient() throws StorageManagerException
	{
		this.loadParametersFromFile();
	}
	
/* METHODES D'INVOCATION AU SERVEUR : */
	
 // Room
	public String createRoom(Room room) throws StorageManagerException
	{
		try
		{
			Wrapper w = this.exchangeWrapper(new Wrapper(Request.createRoom, room));
			if (w.getString().equals(msgOK)) return msgOK;
			else return ((Message) w.getContainer()).getMessage();
		}
		catch (StorageManagerException e)
		{
			e.addException("Client in createRoom method ");
			throw e;
		}
	}
	
	public Room readRoomByName(String name) throws StorageManagerException
	{
		try
		{
			Room room = new Room();
			room.setName(name);
			Wrapper w = this.exchangeWrapper(new Wrapper(Request.readRoomByName, room));
		
			if (w.getString().equals(msgOK)) return (Room) w.getContainer();
			else throw new StorageManagerException(((Message) w.getContainer()).getMessage());
		}
		catch (StorageManagerException e)
		{
			e.addException("Client in readRoomByName method ");
			throw e;
		}
	}
	
	public Room readRoomById(String Id) throws StorageManagerException
	{
		try
		{
			Room room = new Room();
			room.setName(name);
			Wrapper w = this.exchangeWrapper(new Wrapper(Request.readRoomById, room));
		
			if (w.getString().equals(msgOK)) return (Room) w.getContainer();
			else throw new StorageManagerException(((Message) w.getContainer()).getMessage());
		}
		catch (StorageManagerException e)
		{
			e.addException("Client in readRoomByName method ");
			throw e;
		}
	}
	
	
	@SuppressWarnings("unchecked")
	public List<Room> readRoomList() throws StorageManagerException
	{
		try
		{
			Wrapper w = this.exchangeWrapper(new Wrapper(Request.readRoomList));
			if (w.getString().equals(msgOK)) return (List<Room>) w.getContainer();
			else throw new StorageManagerException(((Message) w.getContainer()).getMessage());
		}
		catch (StorageManagerException | ClassCastException e)
		{
			throw new StorageManagerException(" Client in readRoomList \n" + e.toString());
		}
	}
	
	public String updateRoom (Room room) throws StorageManagerException
	{
		try
		{
			Wrapper w = this.exchangeWrapper(new Wrapper(Request.updateRoom, room));
		
			if (w.getString().equals(msgOK)) return msgOK;
			else throw new StorageManagerException(((Message) w.getContainer()).getMessage());
		}
		catch (StorageManagerException e)
		{
			e.addException("Client in updateRoom method ");
			throw e;
		}
	}
	
	public String deleteRoom(Room room) throws StorageManagerException
	{
		try
		{
			Wrapper w = this.exchangeWrapper(new Wrapper(Request.deleteRoom, room));
		
			if (w.getString().equals(msgOK)) return msgOK;
			else throw new StorageManagerException(((Message) w.getContainer()).getMessage());
		}
		catch (StorageManagerException e)
		{
			e.addException("Client in deleteRoom method ");
			throw e;
		}
	}

//Action
	public String createAction(Action action) throws StorageManagerException
	{
		try
		{
			Wrapper w = this.exchangeWrapper(new Wrapper(Request.createAction, action));
			if (w.getString().equals(msgOK)) return msgOK;
			else return ((Message) w.getContainer()).getMessage();
		}
		catch (StorageManagerException e)
		{
			e.addException("Client in createAction method ");
			throw e;
		}
	}
	
	public Action readActionById (String Id) throws StorageManagerException
	{
		try
		{
			Action action = new Action();
			action.setActionId(Id);
			Wrapper w = this.exchangeWrapper(new Wrapper(Request.readRoomById, action));
		
			if (w.getString().equals(msgOK)) return (Action) w.getContainer();
			else throw new StorageManagerException(((Message) w.getContainer()).getMessage());
		}
		catch (StorageManagerException e)
		{
			e.addException("Client in readActionById method ");
			throw e;
		}
	}
	
	public Action readActionByName(String name) throws StorageManagerException
	{
		try
		{
			Action action = new Action();
			action.setName(name);
			Wrapper w = this.exchangeWrapper(new Wrapper(Request.readActionByName, action));
		
			if (w.getString().equals(msgOK)) return (Action) w.getContainer();
			else throw new StorageManagerException(((Message) w.getContainer()).getMessage());
		}
		catch (StorageManagerException e)
		{
			e.addException("Client in readActionByName method ");
			throw e;
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<Action> readActionList() throws StorageManagerException
	{
		try
		{
			Wrapper w = this.exchangeWrapper(new Wrapper(Request.readActionList));
			if (w.getString().equals(msgOK)) return (List<Action>) w.getContainer();
			else throw new StorageManagerException(((Message) w.getContainer()).getMessage());
		}
		catch (StorageManagerException | ClassCastException e)
		{
			throw new StorageManagerException(" Client in readActionList \n" + e.toString());
		}
	}
	
	public String updateAction(Action action) throws StorageManagerException
	{
		try
		{
			Wrapper w = this.exchangeWrapper(new Wrapper(Request.updateAction, action));
		
			if (w.getString().equals(msgOK)) return msgOK;
			else throw new StorageManagerException(((Message) w.getContainer()).getMessage());
		}
		catch (StorageManagerException e)
		{
			e.addException("Client in updateAction method ");
			throw e;
		}
	}
	
	public String deleteAction(Action action) throws StorageManagerException
	{
		try
		{
			Wrapper w = this.exchangeWrapper(new Wrapper(Request.deleteAction, action));
		
			if (w.getString().equals(msgOK)) return msgOK;
			else throw new StorageManagerException(((Message) w.getContainer()).getMessage());
		}
		catch (StorageManagerException e)
		{
			e.addException("Client in deleteAction method ");
			throw e;
		}
	}

//Agenda
	public String createAgenda(Agenda agenda) throws StorageManagerException
	{
		try
		{
			Wrapper w = this.exchangeWrapper(new Wrapper(Request.createAgenda, agenda));
		
			if (w.getString().equals(msgOK)) return msgOK;
			else throw new StorageManagerException(((Message) w.getContainer()).getMessage());
		}
		catch (StorageManagerException e)
		{
			e.addException("Client in createAgenda method ");
			throw e;
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<Agenda> readAgendaByDayNumber(String dayNumber) throws StorageManagerException
	{
		try
		{
			Wrapper w = this.exchangeWrapper(new Wrapper(Request.readAgendaByDayNumber));
			if (w.getString().equals(msgOK)) return (List<Agenda>) w.getContainer();
			else throw new StorageManagerException(((Message) w.getContainer()).getMessage());
		}
		catch (StorageManagerException | ClassCastException e)
		{
			throw new StorageManagerException(" Client in readAgendaByDayNumber \n" + e.toString());
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<Agenda> readAgendaByRoomId(String roomId) throws StorageManagerException
	{
		try
		{
			Wrapper w = this.exchangeWrapper(new Wrapper(Request.readAgendaByRoomId));
			if (w.getString().equals(msgOK)) return (List<Agenda>) w.getContainer();
			else throw new StorageManagerException(((Message) w.getContainer()).getMessage());
		}
		catch (StorageManagerException | ClassCastException e)
		{
			throw new StorageManagerException(" Client in readAgendaByRoomId \n" + e.toString());
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<Agenda> readWeekAgenda() throws StorageManagerException
	{ 
		try
		{
			Wrapper w = this.exchangeWrapper(new Wrapper(Request.readWeekAgenda));
			if (w.getString().equals(msgOK)) return (List<Agenda>) w.getContainer();
			else throw new StorageManagerException(((Message) w.getContainer()).getMessage());
		}
		catch (StorageManagerException | ClassCastException e)
		{
			throw new StorageManagerException(" Client in readWeekAgenda \n" + e.toString());
		}
	}
	
	public String deleteAgenda(Agenda agenda) throws StorageManagerException
	{
		try
		{
			Wrapper w = this.exchangeWrapper(new Wrapper(Request.deleteAgenda, agenda));
		
			if (w.getString().equals(msgOK)) return msgOK;
			else throw new StorageManagerException(((Message) w.getContainer()).getMessage());
		}
		catch (StorageManagerException e)
		{
			e.addException("Client in deleteAgenda method ");
			throw e;
		}
	}

//Spice
	public String createSpice(Spice spice) throws StorageManagerException
	{
		try
		{
			Wrapper w = this.exchangeWrapper(new Wrapper(Request.createSpice, spice));
		
			if (w.getString().equals(msgOK)) return msgOK;
			else throw new StorageManagerException(((Message) w.getContainer()).getMessage());
		}
		catch (StorageManagerException e)
		{
			e.addException("Client in createSpice method ");
			throw e;
		}
	}
	
	public Spice readSpiceById(String Id) throws StorageManagerException
	{
		try
		{
			Spice spice = new Spice();
			spice.setSpiceId(Id);
			Wrapper w = this.exchangeWrapper(new Wrapper(Request.readSpiceById, spice));
		
			if (w.getString().equals(msgOK)) return (Spice) w.getContainer();
			else throw new StorageManagerException(((Message) w.getContainer()).getMessage());
		}
		catch (StorageManagerException e)
		{
			e.addException("Client in readSpiceById method ");
			throw e;
		}
	}
	
	public Spice readSpiceByName (String name) throws StorageManagerException
	{
		try
		{
			Spice spice = new Spice();
			spice.setName(name);
			
			Wrapper w = this.exchangeWrapper(new Wrapper(Request.readSpiceById, spice));
		
			if (w.getString().equals(msgOK)) return (Spice) w.getContainer();
			else throw new StorageManagerException(((Message) w.getContainer()).getMessage());
		}
		catch (StorageManagerException e)
		{
			e.addException("Client in readSpiceByName method ");
			throw e;
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<Spice> readSpiceList() throws StorageManagerException
	{
		try
		{
			Wrapper w = this.exchangeWrapper(new Wrapper(Request.readSpiceList));
			
			if (w.getString().equals(msgOK)) return (List<Spice>) w.getContainer();
			else throw new StorageManagerException(((Message) w.getContainer()).getMessage());
		}
		catch (StorageManagerException | ClassCastException e)
		{
			throw new StorageManagerException(" Client in readSpiceList \n" + e.toString());
		}
	}
	
	public String updateSpice(Spice spice) throws StorageManagerException
	{
		try
		{
			Wrapper w = this.exchangeWrapper(new Wrapper(Request.updateSpice, spice));
		
			if (w.getString().equals(msgOK)) return msgOK;
			else throw new StorageManagerException(((Message) w.getContainer()).getMessage());
		}
		catch (StorageManagerException e)
		{
			e.addException("Client in createSpice method ");
			throw e;
		}
	}
	
	public String deleteSpice(Spice spice) throws StorageManagerException
	{
		try
		{
			Wrapper w = this.exchangeWrapper(new Wrapper(Request.deleteSpice, spice));
		
			if (w.getString().equals(msgOK)) return msgOK;
			else throw new StorageManagerException(((Message) w.getContainer()).getMessage());
		}
		catch (StorageManagerException e)
		{
			e.addException("Client in deleteSpice method ");
			throw e;
		}
	}

//Parameter
	public String createParameter(String key, String param) throws StorageManagerException
	{
		try
		{
			Parameter p = new Parameter();
			p.setKey(key);
			p.setParam(param);
			
			Wrapper w = this.exchangeWrapper(new Wrapper(Request.createParameter, p));
		
			if (w.getString().equals(msgOK)) return msgOK;
			else throw new StorageManagerException(((Message) w.getContainer()).getMessage());
		}
		catch (StorageManagerException e)
		{
			e.addException("Client in createParameter method ");
			throw e;
		}
	}
	
	public String readParameterByKey(String key) throws StorageManagerException
	{
		try
		{
			Parameter p = new Parameter();
			p.setKey(key);
			
			Wrapper w = this.exchangeWrapper(new Wrapper(Request.readParameterByKey, p));
		
			if (w.getString().equals(msgOK)) return ((Parameter) w.getContainer()).getParam();
			else throw new StorageManagerException(((Message) w.getContainer()).getMessage());
		}
		catch (StorageManagerException e)
		{
			e.addException("Client in createParameter method ");
			throw e;
		}
	}
	
	public String updateParameter(String key, String param) throws StorageManagerException
	{
		try
		{
			Parameter p = new Parameter();
			p.setKey(key);
			p.setParam(param);
			
			Wrapper w = this.exchangeWrapper(new Wrapper(Request.updateParameter, p));
		
			if (w.getString().equals(msgOK)) return msgOK;
			else throw new StorageManagerException(((Message) w.getContainer()).getMessage());
		}
		catch (StorageManagerException e)
		{
			e.addException("Client in updateParameter method ");
			throw e;
		}
	}
	
	public String deleteParameterByKey(String key) throws StorageManagerException
	{
		try
		{
			Parameter p = new Parameter();
			p.setKey(key);
			
			Wrapper w = this.exchangeWrapper(new Wrapper(Request.deleteParameterByKey, p));
		
			if (w.getString().equals(msgOK)) return msgOK;
			else throw new StorageManagerException(((Message) w.getContainer()).getMessage());
		}
		catch (StorageManagerException e)
		{
			e.addException("Client in deleteParameterByKey method ");
			throw e;
		}
	}

//SpiceBox
	public Spice readSpiceBoxContent(String boxNumber) throws StorageManagerException
	{
		try
		{
			Spice spice = new Spice();
			
			Wrapper w = this.exchangeWrapper(new Wrapper(Request.readSpiceBoxContent, spice, boxNumber ));
		
			if (w.getString().equals("Full_Box")) return (Spice) w.getContainer();
			else if (w.getString().equals("Empty_Box")) return new Spice("Empty", "Empty", "Empty", "Empty");
			else throw new StorageManagerException(((Message) w.getContainer()).getMessage());
		}
		catch (StorageManagerException e)
		{
			e.addException("Client in readSpiceBoxContent method ");
			throw e;
		}
	}
	
	public String updateSpiceBoxContent(String boxNumber, String spiceId) throws StorageManagerException
	{
		try
		{
			Spice spice = new Spice();
			spice.setSpiceId(spiceId);
			
			Wrapper w = this.exchangeWrapper(new Wrapper(Request.updateSpiceBoxContent, spice, boxNumber ));
		
			if (w.getString().equals("Full_Box")) return msgOK;
			else throw new StorageManagerException(((Message) w.getContainer()).getMessage());
		}
		catch (StorageManagerException e)
		{
			e.addException("Client in updateSpiceBoxContent method ");
			throw e;
		}
	}
	
	public String deleteSpiceBoxContent(String boxNumber) throws StorageManagerException
	{
		try
		{
			Spice spice = new Spice();
			
			Wrapper w = this.exchangeWrapper(new Wrapper(Request.deleteSpiceBoxContent, spice, boxNumber ));
		
			if (w.getString().equals("Full_Box")) return msgOK;
			else throw new StorageManagerException(((Message) w.getContainer()).getMessage());
		}
		catch (StorageManagerException e)
		{
			e.addException("Client in deleteSpiceBoxContent method ");
			throw e;
		}
	}

// LightSensor
	public String createLightSensorMonitoring(LightSensorMonitoring lsm) throws StorageManagerException
	{
		try
		{
			Wrapper w = this.exchangeWrapper(new Wrapper(Request.createLightSensorMonitoring, lsm));
		
			if (w.getString().equals(msgOK)) return msgOK;
			else throw new StorageManagerException(((Message) w.getContainer()).getMessage());
		}
		catch (StorageManagerException e)
		{
			e.addException("Client in createLightSensorMonitoring method ");
			throw e;
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<LightSensorMonitoring> readLightSensorMonitoringList() throws StorageManagerException
	{
		try
		{
			Wrapper w = this.exchangeWrapper(new Wrapper(Request.readLightSensorMonitoringList));
			
			if (w.getString().equals(msgOK)) return (List<LightSensorMonitoring>) w.getContainer();
			else throw new StorageManagerException(((Message) w.getContainer()).getMessage());
		}
		catch (StorageManagerException | ClassCastException e)
		{
			throw new StorageManagerException(" Client in readSpiceList \n" + e.toString());
		}
	}

//TemperatureSensor
	public String createTemperatureSensorMonitoring(TemperatureSensorMonitoring tsm) throws StorageManagerException
	{
		try
		{
			Wrapper w = this.exchangeWrapper(new Wrapper(Request.createTemperatureSensorMonitoring, tsm));
		
			if (w.getString().equals(msgOK)) return msgOK;
			else throw new StorageManagerException(((Message) w.getContainer()).getMessage());
		}
		catch (StorageManagerException e)
		{
			e.addException("Client in createTemperatureSensorMonitoring method ");
			throw e;
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<TemperatureSensorMonitoring> readTemperatureSensorMonitoringList (TemperatureSensorMonitoring tsm) throws StorageManagerException
	{
		try
		{
			Wrapper w = this.exchangeWrapper(new Wrapper(Request.readLightSensorMonitoringList));
			
			if (w.getString().equals(msgOK)) return (List<TemperatureSensorMonitoring>) w.getContainer();
			else throw new StorageManagerException(((Message) w.getContainer()).getMessage());
		}
		catch (StorageManagerException | ClassCastException e)
		{
			throw new StorageManagerException(" Client in readSpiceList \n" + e.toString());
		}
	}
	
// méthodes utilitaires :

	private synchronized void loadParametersFromFile() throws StorageManagerException
	{
		String path = "./../StorageClient.properties";
		Properties properties = new Properties();
		InputStream input = null;
		
		try
		{	
			logger.debug("TECHNICAL PIECES OF INFORMATION :");
			logger.debug("---------------------------------");
			logger.debug("  -> java.class.path : " + System.getProperty("java.class.path"));
			logger.debug("  -> java.home : " + System.getProperty("java.home"));
			logger.debug("  -> Operating system : " + System.getProperty("os.arch") + ", " + System.getProperty("os.version"));
			logger.debug("  -> Working directory : " +  System.getProperty("user.dir"));
			logger.debug("  -> User's name : " + System.getProperty("user.name"));
			logger.debug("----------------------------------");
			logger.debug("");
	        
			input = StorageClient.class.getResourceAsStream(path); 
			properties.load(input);
				
			// chargement des paramètres :
			logger.info("LOADED PARAMETERS :");
			logger.info("-------------------");
			
			this.name = properties.getProperty("name");
			logger.info("  -> Name : " + this.name);
			
			this.ip = InetAddress.getByName(properties.getProperty("ip"));
			logger.info("  -> IP : " + this.ip);
			
			this.port = Integer.parseInt(properties.getProperty("port"));
			logger.info("  -> Port : " + this.port);
				
			logger.info("-------------------");
			logger.info("");
			
		}
		catch (FileNotFoundException e)
		{
			throw new StorageManagerException(""
			+ " The  file '"+ path +"' was not found."
			+ " Abording creation of Storage client '" + name + "': \n" + e.getMessage() );
		}
		catch (UnknownHostException e)
		{
			throw new StorageManagerException(""
			+ " Unknown IP address specified in the file '"+ path +"'."
			+ " Abording creation of Storage client '" + name + "': \n" + e.getMessage() );
		}
		catch (IOException e)
		{
			throw new StorageManagerException(""
			+ " Input/Output error(s) with the file '"+ path +"' while opening the file."
			+ " Abording creation of Storage client'" + name + "': \n" + e.getMessage());
		}
		catch (NullPointerException e)
		{
			throw new StorageManagerException(""
			+ " Null pointer with the file '"+ path +"' while opening the file."
			+ " Abording creation of Storage client '" + name + "': \n" + e.getMessage());
		}
		catch (NumberFormatException e)
		{
			throw new StorageManagerException(""
			+ " Null pointer with the file '"+ path +"' during conversion of parameters from string to int."
			+ " Abording creation of Storage client '" + name + "': \n" + e.getMessage());
		}
		catch (Exception e)
		{
			throw new StorageManagerException(""
			+ " Global error with the file '"+ path +"'."
			+ " Abording creation of Storage client'" + name + "': \n" + e.getMessage());
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
					+ " Abording creation of Storage client '" + name + "': \n" + e.getMessage());
				}
			}
		}
	}
	
	private Wrapper exchangeWrapper(Wrapper wrapper) throws StorageManagerException
	{
		
		if (wrapper == null) throw new StorageManagerException("Client try to send a null value to Storage Server.");
		  
		try 
		{
			Socket socket;
			socket = new Socket(ip, port);
		
			System.out.println("Socket client: " + socket);

			ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
			out.flush();
			
			ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
			logger.info("["+this.name +"] Requierd streams to exchange data created");
			
			out.writeObject(wrapper);
			out.flush();
			logger.info("["+this.name +"] Request sended to server");
			
			wrapper = (Wrapper) in.readObject();
			logger.info("["+this.name +"] Answer got from server");
			
			in.close();
			out.close();
			socket.close();
		
			return wrapper;
		} 
		catch (IOException | ClassNotFoundException e)
		{
			throw new StorageManagerException(e.getMessage());
		}
	}
}
