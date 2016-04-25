package commonsObjects;

import be.unamur.exception.StorageManagerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Properties;


public class StorageClient {

	private static final Logger logger = LoggerFactory.getLogger(StorageClient.class);

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
	public String createRoom(Wrapper w) throws StorageManagerException
	{
		try
		{
			if ((w.getRequest() != Request.createRoom) || ( ! w.getContainer().getClass().isInstance(Room.class))) throw new StorageManagerException("invalid wrapper !");
			
			w = this.exchangeWrapper(w);
			if (w.getString().equals(msgOK)) return msgOK;
			else return ((Message) w.getContainer()).getMessage();
		}
		catch (StorageManagerException e)
		{
			e.addException("Client in createRoom method ");
			throw e;
		}
	}
	
	public Room readRoomByName(Wrapper w) throws StorageManagerException
	{
		try
		{
			if ((w.getRequest() != Request.readRoomByName) || ( ! w.getContainer().getClass().isInstance(Room.class))) throw new StorageManagerException("invalid wrapper !");
			w = this.exchangeWrapper(w);
		
			if (w.getString().equals(msgOK)) return (Room) w.getContainer();
			else throw new StorageManagerException(((Message) w.getContainer()).getMessage());
		}
		catch (StorageManagerException e)
		{
			e.addException("Client in readRoomByName method ");
			throw e;
		}
	}
	
	public Room readRoomById(Wrapper w) throws StorageManagerException
	{
		try
		{
			if ((w.getRequest() != Request.readRoomById) || ( ! w.getContainer().getClass().isInstance(Room.class))) throw new StorageManagerException("invalid wrapper !");
			
			w = this.exchangeWrapper(w);
		
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
	
	public String updateRoom (Wrapper w) throws StorageManagerException
	{
		try
		{
			if ((w.getRequest() != Request.updateRoom) || ( ! w.getContainer().getClass().isInstance(Room.class))) throw new StorageManagerException("invalid wrapper !");
			w = this.exchangeWrapper(w);
		
			if (w.getString().equals(msgOK)) return msgOK;
			else throw new StorageManagerException(((Message) w.getContainer()).getMessage());
		}
		catch (StorageManagerException e)
		{
			e.addException("Client in updateRoom method ");
			throw e;
		}
	}
	
	public String deleteRoom(Wrapper w) throws StorageManagerException
	{
		try
		{
			if ((w.getRequest() != Request.deleteRoom) || ( ! w.getContainer().getClass().isInstance(Room.class))) throw new StorageManagerException("invalid wrapper !");
			
			w = this.exchangeWrapper(w);
		
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
	public String createAction(Wrapper w) throws StorageManagerException
	{
		try
		{
			//if ((w.getRequest() != Request.createAction) || ( ! w.getContainer().getClass().isInstance(Action.class))) throw new StorageManagerException("invalid wrapper !");
			
			w = this.exchangeWrapper(w);
			if (w.getString().equals(msgOK)) return msgOK;
			else return ((Message) w.getContainer()).getMessage();
		}
		catch (StorageManagerException e)
		{
			e.addException("Client in createAction method ");
			throw e;
		}
	}
	
	public Action readActionById (Wrapper w) throws StorageManagerException
	{
		try
		{

			if ((w.getRequest() != Request.readActionById) || ( ! w.getContainer().getClass().isInstance(Action.class))) throw new StorageManagerException("invalid wrapper !");
			
			w = this.exchangeWrapper(w);
		
			if (w.getString().equals(msgOK)) return (Action) w.getContainer();
			else throw new StorageManagerException(((Message) w.getContainer()).getMessage());
		}
		catch (StorageManagerException e)
		{
			e.addException("Client in readActionById method ");
			throw e;
		}
	}
	
	public Action readActionByName(Wrapper w) throws StorageManagerException
	{
		try
		{

			if ((w.getRequest() != Request.readActionByName) || ( ! w.getContainer().getClass().isInstance(Action.class))) throw new StorageManagerException("invalid wrapper !");
			
			w = this.exchangeWrapper(w);
		
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
	
	public String updateAction(Wrapper w) throws StorageManagerException
	{
		try
		{
			if ((w.getRequest() != Request.updateAction) || ( ! w.getContainer().getClass().isInstance(Action.class))) throw new StorageManagerException("invalid wrapper !");
			
			w = this.exchangeWrapper(w);
		
			if (w.getString().equals(msgOK)) return msgOK;
			else throw new StorageManagerException(((Message) w.getContainer()).getMessage());
		}
		catch (StorageManagerException e)
		{
			e.addException("Client in updateAction method ");
			throw e;
		}
	}
	
	public String deleteAction(Wrapper w) throws StorageManagerException
	{
		try
		{
			if ((w.getRequest() != Request.deleteAction) || ( ! w.getContainer().getClass().isInstance(Action.class))) throw new StorageManagerException("invalid wrapper !");
			w = this.exchangeWrapper(w);
		
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
	public String createAgenda(Wrapper w) throws StorageManagerException
	{
		try
		{
			if ((w.getRequest() != Request.createAgenda) || ( ! w.getContainer().getClass().isInstance(Agenda.class))) throw new StorageManagerException("invalid wrapper !");
			w = this.exchangeWrapper(w);
		
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
	public List<Agenda> readAgendaByDayNumber(Wrapper w) throws StorageManagerException
	{
		try
		{
			if ((w.getRequest() != Request.readAgendaByDayNumber) || ( ! w.getContainer().getClass().isInstance(Agenda.class))) throw new StorageManagerException("invalid wrapper !");
			
			w = this.exchangeWrapper(w);
			
			if (w.getString().equals(msgOK)) return (List<Agenda>) w.getContainer();
			else throw new StorageManagerException(((Message) w.getContainer()).getMessage());
		}
		catch (StorageManagerException | ClassCastException e)
		{
			throw new StorageManagerException(" Client in readAgendaByDayNumber \n" + e.toString());
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<Agenda> readAgendaByRoomId(Wrapper w) throws StorageManagerException
	{
		try
		{
			if ((w.getRequest() != Request.readAgendaByRoomId) || ( ! w.getContainer().getClass().isInstance(Agenda.class))) throw new StorageManagerException("invalid wrapper !");
			w = this.exchangeWrapper(w);
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
	
	public String deleteAgenda(Wrapper w) throws StorageManagerException
	{
		try
		{
			if ((w.getRequest() != Request.deleteAgenda) || ( ! w.getContainer().getClass().isInstance(Agenda.class))) throw new StorageManagerException("invalid wrapper !");
			w = this.exchangeWrapper(w);
		
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
	public String createSpice(Wrapper w) throws StorageManagerException
	{
		try
		{
			if ((w.getRequest() != Request.createSpice) || ( ! w.getContainer().getClass().isInstance(Spice.class))) throw new StorageManagerException("invalid wrapper !");
			w = this.exchangeWrapper(w);
		
			if (w.getString().equals(msgOK)) return msgOK;
			else throw new StorageManagerException(((Message) w.getContainer()).getMessage());
		}
		catch (StorageManagerException e)
		{
			e.addException("Client in createSpice method ");
			throw e;
		}
	}
	
	public Spice readSpiceById(Wrapper w) throws StorageManagerException
	{
		try
		{
			if ((w.getRequest() != Request.readSpiceById) || ( ! w.getContainer().getClass().isInstance(Spice.class))) throw new StorageManagerException("invalid wrapper !");

			w = this.exchangeWrapper(w);
		
			if (w.getString().equals(msgOK)) return (Spice) w.getContainer();
			else throw new StorageManagerException(((Message) w.getContainer()).getMessage());
		}
		catch (StorageManagerException e)
		{
			e.addException("Client in readSpiceById method ");
			throw e;
		}
	}
	
	public Spice readSpiceByName (Wrapper w) throws StorageManagerException
	{
		try
		{
			if ((w.getRequest() != Request.readSpiceByName) || ( ! w.getContainer().getClass().isInstance(Spice.class))) throw new StorageManagerException("invalid wrapper !");
			
			 w = this.exchangeWrapper(w);
		
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
	
	public String updateSpice(Wrapper w) throws StorageManagerException
	{
		try
		{
			if ((w.getRequest() != Request.updateSpice) || ( ! w.getContainer().getClass().isInstance(Spice.class))) throw new StorageManagerException("invalid wrapper !");
			w = this.exchangeWrapper(w);
		
			if (w.getString().equals(msgOK)) return msgOK;
			else throw new StorageManagerException(((Message) w.getContainer()).getMessage());
		}
		catch (StorageManagerException e)
		{
			e.addException("Client in createSpice method ");
			throw e;
		}
	}
	
	public String deleteSpice(Wrapper w) throws StorageManagerException
	{
		try
		{
			if ((w.getRequest() != Request.deleteSpice) || ( ! w.getContainer().getClass().isInstance(Spice.class))) throw new StorageManagerException("invalid wrapper !");
			w = this.exchangeWrapper(w);
		
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
	public String createParameter(Wrapper w) throws StorageManagerException
	{
		try
		{
			if ((w.getRequest() != Request.createParameter) || ( ! w.getContainer().getClass().isInstance(Parameter.class))) throw new StorageManagerException("invalid wrapper !");
			w = this.exchangeWrapper(w);
		
			if (w.getString().equals(msgOK)) return msgOK;
			else throw new StorageManagerException(((Message) w.getContainer()).getMessage());
		}
		catch (StorageManagerException e)
		{
			e.addException("Client in createParameter method ");
			throw e;
		}
	}
	
	public String readParameterByKey(Wrapper w) throws StorageManagerException
	{
		try
		{
			if ((w.getRequest() != Request.readParameterByKey) || ( ! w.getContainer().getClass().isInstance(Parameter.class))) throw new StorageManagerException("invalid wrapper !");

			w = this.exchangeWrapper(w);
		
			if (w.getString().equals(msgOK)) return ((Parameter) w.getContainer()).getParam();
			else throw new StorageManagerException(((Message) w.getContainer()).getMessage());
		}
		catch (StorageManagerException e)
		{
			e.addException("Client in createParameter method ");
			throw e;
		}
	}
	
	public String updateParameter(Wrapper w) throws StorageManagerException
	{
		try
		{
			if ((w.getRequest() != Request.updateParameter) || ( ! w.getContainer().getClass().isInstance(Parameter.class))) throw new StorageManagerException("invalid wrapper !");
			
			w = this.exchangeWrapper(w);
		
			if (w.getString().equals(msgOK)) return msgOK;
			else throw new StorageManagerException(((Message) w.getContainer()).getMessage());
		}
		catch (StorageManagerException e)
		{
			e.addException("Client in updateParameter method ");
			throw e;
		}
	}
	
	public String deleteParameterByKey(Wrapper w) throws StorageManagerException
	{
		try
		{
			if ((w.getRequest() != Request.deleteParameterByKey) || ( ! w.getContainer().getClass().isInstance(Parameter.class))) throw new StorageManagerException("invalid wrapper !");
			
			w = this.exchangeWrapper(w);
		
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
	public Spice readSpiceBoxContent(Wrapper w) throws StorageManagerException
	{
		try
		{
			if ((w.getRequest() != Request.readSpiceBoxContent) || ( ! w.getContainer().getClass().isInstance(Spice.class))) throw new StorageManagerException("invalid wrapper !");
			
			w = this.exchangeWrapper(w);
		
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
	
	public String updateSpiceBoxContent(Wrapper w) throws StorageManagerException
	{
		try
		{
			if ((w.getRequest() != Request.updateSpiceBoxContent) || ( ! w.getContainer().getClass().isInstance(Spice.class))) throw new StorageManagerException("invalid wrapper !");
			
			w = this.exchangeWrapper(w);
		
			if (w.getString().equals("Full_Box")) return msgOK;
			else throw new StorageManagerException(((Message) w.getContainer()).getMessage());
		}
		catch (StorageManagerException e)
		{
			e.addException("Client in updateSpiceBoxContent method ");
			throw e;
		}
	}
	
	public String deleteSpiceBoxContent(Wrapper w) throws StorageManagerException
	{
		try
		{
			if ((w.getRequest() != Request.deleteSpiceBoxContent) || (! w.getContainer().getClass().isInstance(Spice.class))) throw new StorageManagerException("invalid wrapper !");
			
			w = this.exchangeWrapper(w);
		
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
	public String createLightSensorMonitoring(Wrapper w) throws StorageManagerException
	{
		try
		{
			if ((w.getRequest() != Request.createLightSensorMonitoring) || (! w.getContainer().getClass().isInstance(LightSensorMonitoring.class))) throw new StorageManagerException("invalid wrapper !");
			w = this.exchangeWrapper(w);
		
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
	public String createTemperatureSensorMonitoring(Wrapper w) throws StorageManagerException
	{
		try
		{
			if ((w.getRequest() != Request.createTemperatureSensorMonitoring) || (! w.getContainer().getClass().isInstance(TemperatureSensorMonitoring.class))) throw new StorageManagerException("invalid wrapper !");
			w = this.exchangeWrapper(w);
		
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
	public List<TemperatureSensorMonitoring> readTemperatureSensorMonitoringList (Wrapper w) throws StorageManagerException
	{
		try
		{
			if (w.getRequest() != Request.readTemperatureSensorMonitoringList) throw new StorageManagerException("invalid wrapper !");
			
			w = this.exchangeWrapper(new Wrapper(Request.readTemperatureSensorMonitoringList));
			
			if (w.getString().equals(msgOK)) return (List<TemperatureSensorMonitoring>) w.getContainer();
			else throw new StorageManagerException(((Message) w.getContainer()).getMessage());
		}
		catch (StorageManagerException | ClassCastException e)
		{
			throw new StorageManagerException(" Client in readTemperatureSensorMonitoringList \n" + e.toString());
		}
	}
	
// m�thodes utilitaires :

	private synchronized void loadParametersFromFile() throws StorageManagerException
	{
		String path = "./../StorageClient.properties";
		Properties properties = new Properties();
		InputStream input = null;
		
		try
		{	
			logger.info("TECHNICAL PIECES OF INFORMATION :");
			logger.info("---------------------------------");
			logger.info("  -> java.class.path : " + System.getProperty("java.class.path"));
			logger.info("  -> java.home : " + System.getProperty("java.home"));
			logger.info("  -> Operating system : " + System.getProperty("os.arch") + ", " + System.getProperty("os.version"));
			logger.info("  -> Working directory : " +  System.getProperty("user.dir"));
			logger.info("  -> User's name : " + System.getProperty("user.name"));

			logger.debug("----------------------------------");
			logger.debug("");
	        
			input = StorageClient.class.getResourceAsStream(path); 
			properties.load(input);
				
			// chargement des param�tres :
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
