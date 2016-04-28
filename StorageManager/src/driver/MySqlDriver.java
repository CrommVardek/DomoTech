package driver;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.log4j.Logger;
import com.jolbox.bonecp.BoneCP;
import com.jolbox.bonecp.BoneCPConfig;

import commonsObjects.Action;
import commonsObjects.Agenda;
import commonsObjects.LightSensorMonitoring;
import commonsObjects.List;
import commonsObjects.Room;
import commonsObjects.Spice;
import commonsObjects.TemperatureSensorMonitoring;
import exception.MySqlDriverException;

/*
 * Driver de connexion à la base de données.
 * Il réalise l'abstraction entre les requêtes SQL et Une vue orientée Objet.
 * Codé par Dorian Lecomte.
 */

public class MySqlDriver {
	
	private static final Logger logger = Logger.getLogger(MySqlDriver.class);
	// paramètres de connnexion au serveur sql
	private boolean isEnable;
	private String label;
	private String url;
	private int port;
	private String userName;
	private String password;
	private String propertiesPath = "./../MySqlModule.properties";
	private String preparedStatementPath = "./../MySqlRequest.properties";
	
	// paramètres pour boneCP :
	private BoneCP connectionPool;
	private int minConnectionPerPartition;
	private int maxConnectionPerPartition;
	private int partitionCount;
	private Connection connexion;
	
	/* Variables pour les prepared statements. Ils sont tous initialisés dans la méthode privée "setPreparedStatement" */
	
	// ROOM
	private PreparedStatement createRoom;
	private PreparedStatement readRoomByName;
	private PreparedStatement readRoomById;
	private PreparedStatement readRoomList;
	private PreparedStatement updateRoom;
	private PreparedStatement deleteRoom;
	
	// ACTION
	private PreparedStatement createAction;
	private PreparedStatement readActionById;
	private PreparedStatement readActionByName;
	private PreparedStatement readActionList;
	private PreparedStatement updateAction;
	private PreparedStatement deleteAction;

	// AGENDA
	private PreparedStatement createAgenda;
	private PreparedStatement readAgendaByDayNumber;
	private PreparedStatement readAgendaByRoomId;
	private PreparedStatement readWeekAgenda;
	private PreparedStatement deleteAgenda;
	
	// SPICE
	private PreparedStatement createSpice;
	private PreparedStatement readSpiceById;
	private PreparedStatement readSpiceByName;
	private PreparedStatement readSpiceList;
	private PreparedStatement updateSpice;
	private PreparedStatement deleteSpice;
	
	// PARAMETER
	private PreparedStatement createParameter;
	private PreparedStatement readParameterByKey;
	private PreparedStatement updateParameter;
	private PreparedStatement deleteParameterByKey;
	
	// SPICE_BOX
	private PreparedStatement readSpiceBoxContent;
	private PreparedStatement updateSpiceBoxContent;
	private PreparedStatement deleteSpiceBoxContent;
	
	// LIGHT SENSOR
	private PreparedStatement createLightSensorMonitoring;
	private PreparedStatement readLightSensorMonitoringList;
	
	// TEMPERATURE SENSOR
	private PreparedStatement createTemperatureSensorMonitoring;
	private PreparedStatement readTemperatureSensorMonitoringList;
	

// CONSTRUCTORS
	public MySqlDriver(String label, String url, int port, String userName, String password, boolean isEnable, int minConnectionPerPartition, int maxConnectionPerPartition, int partitionCount) throws MySqlDriverException
	{
		this.label = label;
		this.isEnable = isEnable;
		this.url = url;
		this.port = port;
		this.userName = userName;
		this.password = password;
		this.minConnectionPerPartition = minConnectionPerPartition;
		this.maxConnectionPerPartition = maxConnectionPerPartition;
		this.partitionCount = partitionCount;
	}
	
	public MySqlDriver(String label) throws MySqlDriverException
	{
		this.label = label;
		this.loadParametersFromFile(label);
	}
	
	public MySqlDriver() throws MySqlDriverException
	{
		this.loadParametersFromFile("MySQL_defaultServer");
	}

// PUBLIC METHODS :
	
	
// ROOM
	public synchronized void createRoom(Room room) throws MySqlDriverException
	{
		try
		{
			this.createRoom.setString(1,room.getName());
			this.createRoom.setString(2, room.getDescription());
			int status  = this.createRoom.executeUpdate();
			
			if (status == 1) logger.info("MySqlDriver in createRoom, '"+ room.getName() +"' created.");
			else throw new MySqlDriverException("Failed to insert a new user in database, the state of request is : " + status);
		}
		catch (SQLException | MySqlDriverException e)
		{
			throw new MySqlDriverException ("MySqlDriver error in createRoom : " + e.toString());
		}
		finally
		{
			try
			{
				this.createRoom.clearParameters();
			}
			catch (SQLException e)
			{
				// ignore
			}
		}
	}
	
	public synchronized Room readRoomByName(String name) throws MySqlDriverException
	{
		Room room = new Room();
		ResultSet set; 
		
		try
		{
			this.readRoomByName.setString(1, name);
			set = this.readRoomByName.executeQuery();
			while (set.next())
			{
				room.setRoomId(set.getString(1));
				room.setName(set.getString(2));
				room.setDescription(set.getString(3));
			}
		}
		catch (SQLException e)
		{
			throw new MySqlDriverException ("MySqlDriver error in readRoomByName : " + e.toString());
		}
		finally
		{
			try 
			{
				this.readRoomByName.clearParameters();
			}
			catch (SQLException e)
			{
				// ignore
			}
		}
		return room;
	}
	
	public synchronized Room readRoomById(String roomId) throws MySqlDriverException
	{
		Room room = new Room();
		ResultSet set; 
		
		try
		{
			this.readRoomByName.setString(1, roomId);
			set = this.readRoomByName.executeQuery();
			while (set.next())
			{
				room.setRoomId(set.getString(1));
				room.setName(set.getString(2));
				room.setDescription(set.getString(3));
			}
		}
		catch (SQLException e)
		{
			throw new MySqlDriverException ("MySqlDriver error in readRoomById : " + e.toString());
		}
		finally
		{
			try 
			{
				this.readRoomByName.clearParameters();
			}
			catch (SQLException e)
			{
				// ignore
			}
		}
		return room;
	}
	
	public synchronized List<Room> readRoomList() throws MySqlDriverException
	{
		List<Room> list = new List<Room>();
		
		try
		{
			ResultSet set = this.readRoomList.executeQuery();
			while (set.next())
			{
				Room room = new Room();
				room.setRoomId(set.getString(1));
				room.setName(set.getString(2));
				room.setDescription(set.getString(3));
				list.add(room);
			}
		}
		catch (SQLException e)
		{
			throw new MySqlDriverException ("MySqlDriver error in readRoomList : " + e.toString());
		}
		
		// pas besoin de reseter les paramètres donc pas de bloc finally
		return list;
	}
	
	public synchronized void updateRoom(Room room) throws MySqlDriverException
	{
		try
		{
			this.updateRoom.setString(1, room.getName());
			this.updateRoom.setString(2, room.getDescription());
			this.updateRoom.setString(3, room.getRoomId());
			
			int status = this.updateRoom.executeUpdate();
			if (status == 1) logger.info("MySqlDriver in updateRoom, '"+ room.getName() +"' updated.");
			else throw new MySqlDriverException("Failed to update a room in database, the state of request is : " + status);
		}
		catch (SQLException| MySqlDriverException e)
		{
			throw new MySqlDriverException ("MySqlDriver error in updateRoom : " + e.toString());
		}
		finally
		{
			try
			{
				this.updateRoom.clearParameters();
			}
			catch (SQLException e)
			{
				// ignore
			}
		}
	}
	
	public synchronized void deleteRoom(Room room) throws MySqlDriverException
	{
		try
		{
			this.deleteRoom.setString(1, room.getRoomId());
			
			int status = this.deleteRoom.executeUpdate();
			if (status == 1) logger.info("MySqlDriver in deleteRoom, '"+ room.getName() +"' deleted.");
			else throw new MySqlDriverException("Failed to delete a room in database, the state of request is : " + status);
		}
		catch (SQLException | MySqlDriverException e)
		{
			throw new MySqlDriverException ("MySqlDriver error in deleteRoom : " + e.toString());
		}
		finally
		{
			try
			{
				this.deleteRoom.clearParameters();
			}
			catch (SQLException e)
			{
				// ignore
			}
		}
	}
	
// ACTION
	public synchronized void createAction(Action action) throws MySqlDriverException
	{
		try
		{
			this.createAction.setString(1,action.getName());
			this.createAction.setString(2, action.getDescription());
			int status  = this.createAction.executeUpdate();
			
			if (status == 1) logger.info("MySqlDriver in createAction, '"+ action.getName() +"' created.");
			else throw new MySqlDriverException("Failed to insert a new Action in database, the state of request is : " + status);
		}
		catch (SQLException | MySqlDriverException e)
		{
			throw new MySqlDriverException ("MySqlDriver error in createAction : " + e.toString());
		}
		finally
		{
			try
			{
				this.createAction.clearParameters();
			}
			catch (SQLException e)
			{
				// ignore
			}
		}
	}
	
	public synchronized Action readActionById(String actionId) throws MySqlDriverException
	{
		Action action= new Action();
		ResultSet set; 
		
		try
		{
			this.readActionById.setString(1, actionId);
			set = this.readActionById.executeQuery();
			while (set.next())
			{
				action.setActionId(set.getString(1));
				action.setName(set.getString(2));
				action.setDescription(set.getString(3));
			}
		}
		catch (SQLException e)
		{
			throw new MySqlDriverException ("MySqlDriver error in readActionById : " + e.toString());
		}
		finally
		{
			try 
			{
				this.readActionById.clearParameters();
			}
			catch (SQLException e)
			{
				// ignore
			}
		}
		return action;
	}
	
	public synchronized Action readActionByName(String name) throws MySqlDriverException
	{
		Action action= new Action();
		ResultSet set; 
		
		try
		{
			this.readActionByName.setString(1, name);
			set = this.readActionByName.executeQuery();
			while (set.next())
			{
				action.setActionId(set.getString(1));
				action.setName(set.getString(2));
				action.setDescription(set.getString(3));
			}
		}
		catch (SQLException e)
		{
			throw new MySqlDriverException ("MySqlDriver error in readActionByName : " + e.toString());
		}
		finally
		{
			try 
			{
				this.readActionByName.clearParameters();
			}
			catch (SQLException e)
			{
				// ignore
			}
		}
		return action;
	}
	
	public synchronized List<Action> readActionList() throws MySqlDriverException
	{
		List<Action> list = new List<Action>();
		
		try
		{
			ResultSet set = this.readActionList.executeQuery();
			while (set.next())
			{
				Action action = new Action();
				action.setActionId(set.getString(1));
				action.setName(set.getString(2));
				action.setDescription(set.getString(3));
				list.add(action);
			}
		}
		catch (SQLException e)
		{
			throw new MySqlDriverException ("MySqlDriver error in readActionList : " + e.toString());
		}
		
		// pas besoin de reseter les paramètres donc pas de bloc finally
		return list;
	}
	
	public synchronized void updateAction(Action action) throws MySqlDriverException
	{
		try
		{
			this.updateAction.setString(1, action.getName());
			this.updateAction.setString(2, action.getDescription());
			this.updateAction.setString(3, action.getActionId());
			
			int status = this.updateAction.executeUpdate();
			if (status == 1) logger.info("MySqlDriver in updateAction, '"+ action.getName() +"' updated.");
			else throw new MySqlDriverException("Failed to update a Action in database, the state of request is : " + status);
		}
		catch (SQLException | MySqlDriverException e)
		{
			throw new MySqlDriverException ("MySqlDriver error in updateAction : " + e.toString());
		}
		finally
		{
			try
			{
				this.updateAction.clearParameters();
			}
			catch (SQLException e)
			{
				// ignore
			}
		}
	}
	
	public synchronized void deleteAction(Action action) throws MySqlDriverException
	{
		try
		{
			this.deleteAction.setString(1, action.getActionId());
			
			int status = this.updateAction.executeUpdate();
			if (status == 1) logger.info("MySqlDriver in deleteAction, '"+ action.getName() +"' deleted.");
			else throw new MySqlDriverException("Failed to delete an Action in database, the state of request is : " + status);
		}
		catch (SQLException | MySqlDriverException e)
		{
			throw new MySqlDriverException ("MySqlDriver error in deleteAction : " + e.toString());
		}
		finally
		{
			try
			{
				this.deleteAction.clearParameters();
			}
			catch (SQLException e)
			{
				// ignore
			}
		}
	}
	
// AGENDA
	public synchronized void createAgenda (Agenda agenda) throws MySqlDriverException
	{
		try
		{
			this.createAgenda.setString(1, agenda.getDayId());
			this.createAgenda.setString(2,agenda.getRoomId());
			this.createAgenda.setString(3, agenda.getActionId());
			//  la ligne ici dessous peut flancher !
			this.createAgenda.setString(4, agenda.getHour().toString());
			
			int status  = this.createAgenda.executeUpdate();
			
			if (status == 1) logger.info("MySqlDriver in createAgenda, new agenda created.");
			else throw new MySqlDriverException("Failed to insert a new Agenda in database, the state of request is : " + status);
		}
		catch (SQLException | MySqlDriverException e)
		{
			throw new MySqlDriverException ("MySqlDriver error in createAgenda : " + e.toString());
		}
		finally
		{
			try
			{
				this.createAgenda.clearParameters();
			}
			catch (SQLException e)
			{
				// ignore
			}
		}
	}
	
	public synchronized List<Agenda> readAgendaByDayNumber (String dayNumber) throws MySqlDriverException
	{
		List<Agenda> list = new List<Agenda>();
		try
		{
			this.readAgendaByDayNumber.setString(1, dayNumber);
			ResultSet set = this.readAgendaByDayNumber.executeQuery();
			
			while (set.next())
			{
				Agenda agenda = new Agenda();
				agenda.setDayId(set.getString(1));
				agenda.setRoomId(set.getString(2));
				agenda.setActionId(set.getString(3));
				// l'appel se méthode ci-dessous peut-il flancher ?
				agenda.setHour(set.getTime(4));
				list.add(agenda);
			}
		}
		catch (SQLException e)
		{
			throw new MySqlDriverException ("MySqlDriver error in readAgendaByDayNumber : " + e.toString());
		}
		finally
		{
			try
			{
				this.readAgendaByDayNumber.clearParameters();
			}
			catch (SQLException e)
			{
				// ignore 
			}
		}
		return list;
	}
	
	public synchronized List<Agenda> readAgendaByRoomId (String roomId) throws MySqlDriverException
	{
		List<Agenda> list = new List<Agenda>();
		try
		{
			this.readAgendaByRoomId.setString(1, roomId);
			ResultSet set = this.readAgendaByRoomId.executeQuery();
			
			while (set.next())
			{
				Agenda agenda = new Agenda();
				agenda.setDayId(set.getString(1));
				agenda.setRoomId(set.getString(2));
				agenda.setActionId(set.getString(3));
				// l'appel se méthode ci-dessous peut-il flancher ?
				agenda.setHour(set.getTime(4));
				list.add(agenda);
			}
		}
		catch (SQLException e)
		{
			throw new MySqlDriverException ("MySqlDriver error in readAgendaByRoomId : " + e.toString());
		}
		finally
		{
			try
			{
				this.readAgendaByRoomId.clearParameters();
			}
			catch (SQLException e)
			{
				// ignore 
			}
		}
		return list;
	}

	public synchronized List<Agenda> readWeekAgenda () throws MySqlDriverException
	{
		List<Agenda> list = new List<Agenda>();
		try
		{
			ResultSet set = this.readWeekAgenda.executeQuery();
			while(set.next())
			{
				Agenda agenda = new Agenda();
				agenda.setDayId(set.getString(1));
				agenda.setHour(set.getTime(2));
				agenda.setRoomId(set.getString(3));
				agenda.setActionId(set.getString(4));
				list.add(agenda);
			}
		}
		catch (SQLException e)
		{
			throw new MySqlDriverException ("MySqlDriver error in readWeekAgenda : " + e.toString());
		}
		return list;
	}
	
	public synchronized void deleteAgenda (Agenda agenda) throws MySqlDriverException
	{
		try
		{
			this.deleteAgenda.setString(1, agenda.getDayId());
			this.deleteAgenda.setString(2, agenda.getRoomId());
			this.deleteAgenda.setString(3, agenda.getActionId());
			this.deleteAgenda.setString(4, agenda.getHour().toString());
			
			int status = this.deleteAgenda.executeUpdate();
			if (status == 1) logger.info("MySqlDriver in deleteAgenda, one Agenda deleted.");
			else throw new MySqlDriverException("Failed to delete an Agenda in database, the state of request is : " + status);
		}
		catch (SQLException | MySqlDriverException e)
		{
			throw new MySqlDriverException ("MySqlDriver error in deleteAgenda : " + e.toString());
		}
		finally
		{
			try
			{
				this.deleteAgenda.clearParameters();
			}
			catch (SQLException e)
			{
				// ignore
			}
		}
	}
	
// SPICE
	public synchronized void createSpice(Spice spice) throws MySqlDriverException
	{
		try
		{
			this.createSpice.setString(1, spice.getName());
			this.createSpice.setString(2,spice.getDescription());
			this.createSpice.setString(3, spice.getBarCode());
			
			int status  = this.createSpice.executeUpdate();
			
			if (status == 1) logger.info("MySqlDriver in createSpice, new Spice created.");
			else throw new MySqlDriverException("Failed to insert a new Spice in database, the state of request is : " + status);
		}
		catch (SQLException | MySqlDriverException e)
		{
			throw new MySqlDriverException ("MySqlDriver error in createSpice : " + e.toString());
		}
		finally
		{
			try
			{
				this.createSpice.clearParameters();
			}
			catch (SQLException e)
			{
				// ignore
			}
		}
	}
	
	public synchronized Spice readSpiceById (String spiceId) throws MySqlDriverException
	{
		Spice spice= new Spice();
			
		try
		{
			this.readSpiceById.setString(1, spiceId);
			ResultSet set = this.readSpiceById.executeQuery();
			while (set.next())
			{
				spice.setSpiceId(set.getString(1));
				spice.setName(set.getString(2));
				spice.setDescription(set.getString(3));
				spice.setBarCode(set.getString(4));
			}
		}
		catch (SQLException e)
		{
			throw new MySqlDriverException ("MySqlDriver error in readSpiceById : " + e.toString());
		}
		finally
		{
			try 
			{
				this.readSpiceById.clearParameters();
			}
			catch (SQLException e)
			{
				// ignore
			}
		}
		return spice;
	}
	
	public synchronized Spice readSpiceByName (String name) throws MySqlDriverException
	{
		Spice spice= new Spice();
		
		try
		{
			this.readSpiceByName.setString(1, name);
			ResultSet set = this.readSpiceByName.executeQuery();
			while (set.next())
			{
				spice.setSpiceId(set.getString(1));
				spice.setName(set.getString(2));
				spice.setDescription(set.getString(3));
				spice.setBarCode(set.getString(4));
			}
		}
		catch (SQLException e)
		{
			throw new MySqlDriverException ("MySqlDriver error in readSpiceByName : " + e.toString());
		}
		finally
		{
			try 
			{
				this.readSpiceByName.clearParameters();
			}
			catch (SQLException e)
			{
				// ignore
			}
		}
		return spice;
	}
	
	public synchronized List<Spice> readSpiceList () throws MySqlDriverException
	{
		List<Spice> list = new List<Spice>();
		try
		{
			ResultSet set = this.readSpiceList.executeQuery();
			while(set.next())
			{
				Spice spice = new Spice();
				spice.setSpiceId(set.getString(1));
				spice.setName(set.getString(2));
				spice.setDescription(set.getString(3));
				spice.setBarCode(set.getString(4));
				list.add(spice);
			}
		}
		catch (SQLException e)
		{
			throw new MySqlDriverException ("MySqlDriver error in readSpiceByName : " + e.toString());
		}
		
		return list;
	}
	
	public synchronized void updateSpice (Spice spice) throws MySqlDriverException
	{
		try
		{
			this.updateSpice.setString(1, spice.getName());
			this.updateSpice.setString(2, spice.getDescription());
			this.updateSpice.setString(3, spice.getBarCode());
			this.updateSpice.setString(4, spice.getSpiceId());
			
			int status = this.updateSpice.executeUpdate();
			if (status == 1) logger.info("MySqlDriver in updateSpice, Spice '" + spice.getName() +  "' updated.");
			else throw new MySqlDriverException("Failed to update a Spice in database, the state of request is : " + status);
		}
		catch (SQLException | MySqlDriverException e)
		{
			throw new MySqlDriverException ("MySqlDriver error in updateSpice : " + e.toString());
		}
		finally
		{
			try
			{
				this.updateSpice.clearParameters();
			}
			catch (SQLException e)
			{
				// ignore
			}
		}
	}
	
	public synchronized void deleteSpice (Spice spice) throws MySqlDriverException
	{
		try
		{
			this.deleteSpice.setString(1, spice.getSpiceId());
			
			int status = this.updateSpice.executeUpdate();
			if (status == 1) logger.info("MySqlDriver in deleteSpice, Spice '" + spice.getName() +  "' deleted.");
			else throw new MySqlDriverException("Failed to delete a Spice in database, the state of request is : " + status);
		}
		catch (SQLException| MySqlDriverException e)
		{
			throw new MySqlDriverException ("MySqlDriver error in deleteSpice : " + e.toString());
		}
		finally
		{
			try
			{
				this.deleteSpice.clearParameters();
			}
			catch (SQLException e)
			{
				// ignore
			}
		}
	}
	
// PARAMETER
	public synchronized void createParameter (String key, String param) throws MySqlDriverException
	{
		try
		{
			this.createParameter.setString(1, key);
			this.createParameter.setString(2, param);
			
			int status = this.createParameter.executeUpdate();
			if (status == 1) logger.info("MySqlDriver in createParameter, parameter (" + key +", " + param  +  ") created.");
			else throw new MySqlDriverException("Failed to inset a parameter in database, the state of request is : " + status);
		}
		catch(SQLException | MySqlDriverException e)
		{
			throw new MySqlDriverException ("MySqlDriver error in createParameter : " + e.toString());
		}
		finally
		{
			try
			{
				this.createParameter.clearParameters();
			}
			catch (SQLException e)
			{
				// ignore
			}
		}
	}
	
	public synchronized String readParameterByKey (String key) throws MySqlDriverException
	{
		String param = "";
		try
		{
			this.readParameterByKey.setString(1, key);
			ResultSet set = this.readParameterByKey.executeQuery();
			while (set.next())
			{
				param = set.getString(1);
			}
		}
		catch(SQLException e)
		{
			throw new MySqlDriverException ("MySqlDriver error in readParameterByKey : " + e.toString());
		}
		finally
		{
			try
			{
				this.readParameterByKey.clearParameters();
			}
			catch (SQLException e)
			{
				// ignore
			}
		}
		return param;
	}
	
	public synchronized void updateParameter (String key, String param) throws MySqlDriverException
	{
		try
		{
			this.updateParameter.setString(1, param);
			this.updateParameter.setString(2, key);
			
			int status = this.updateParameter.executeUpdate();
			if (status == 1) logger.info("MySqlDriver in updateParameter, parameter (" + key +", " + param  +  ") updated.");
			else throw new MySqlDriverException("Failed to update a parameter in database, the state of request is : " + status);
		}
		catch(SQLException | MySqlDriverException e)
		{
			throw new MySqlDriverException ("MySqlDriver error in updateParameter : " + e.toString());
		}
		finally
		{
			try
			{
				this.updateParameter.clearParameters();
			}
			catch (SQLException e)
			{
				// ignore
			}
		}
	}
	
	public synchronized void deleteParameterByKey (String key) throws MySqlDriverException
	{
		try
		{
			this.deleteParameterByKey.setString(1, key);
			
			int status = this.deleteParameterByKey.executeUpdate();
			if (status == 1) logger.info("MySqlDriver in deleteParameterByKey, parameter witk key '" + key +"' deleted.");
			else throw new MySqlDriverException("Failed to delete a parameter in database, the state of request is : " + status);
		}
		catch(SQLException | MySqlDriverException e)
		{
			throw new MySqlDriverException ("MySqlDriver error in deleteParameter : " + e.toString());
		}
		finally
		{
			try
			{
				this.deleteParameterByKey.clearParameters();
			}
			catch (SQLException e)
			{
				// ignore
			}
		}
	}
	

// SPICE_BOX
	public synchronized Spice readSpiceBoxContent(String boxNumber) throws MySqlDriverException
	{
		Spice spice = new Spice();
		try
		{
			this.readSpiceBoxContent.setString(1, boxNumber);
			ResultSet set = this.readSpiceBoxContent.executeQuery();
			while (set.next())
			{
				spice.setSpiceId(set.getString(1));
				spice.setName(set.getString(2));
				spice.setDescription(set.getString(3));
				spice.setBarCode(set.getString(4));
			}
		}
		catch(SQLException e)
		{
			throw new MySqlDriverException ("MySqlDriver error in readSpiceBoxContent : " + e.toString());
		}
		finally
		{
			try
			{
				this.readSpiceBoxContent.clearParameters();
			}
			catch (SQLException e)
			{
				// ignore
			}
		}
		return spice;
	}
	
	public synchronized void updateSpiceBoxContent(String boxNumber, String spiceId) throws MySqlDriverException
	{
		try
		{
			this.updateSpiceBoxContent.setString(1, spiceId);
			this.updateSpiceBoxContent.setString(2, boxNumber);
			
			int status = this.updateSpiceBoxContent.executeUpdate();
			if (status == 1) logger.info("MySqlDriver in updateSpiceBoxContent, Box '" + boxNumber +"' updated with spice.");
			else throw new MySqlDriverException("Failed to update a parameter in database, the state of request is : " + status);
		}
		catch(SQLException | MySqlDriverException e)
		{
			throw new MySqlDriverException ("MySqlDriver error in updateSpiceBoxContent : " + e.toString());
		}
		finally
		{
			try
			{
				this.updateSpiceBoxContent.clearParameters();
			}
			catch(SQLException e)
			{
				// Ignore
			}
		}
	}
	
	public synchronized void deleteSpiceBoxContent(String boxNumber) throws MySqlDriverException
	{
		try
		{
			this.deleteSpiceBoxContent.setString(1, boxNumber);
			
			int status = this.deleteSpiceBoxContent.executeUpdate();
			if (status == 1) logger.info("MySqlDriver in deleteSpiceBoxContent, Box '" + boxNumber +"' is now empty.");
			else throw new MySqlDriverException("Failed to update a parameter in database, the state of request is : " + status);
		}
		catch(SQLException | MySqlDriverException e)
		{
			throw new MySqlDriverException ("MySqlDriver error in deleteSpiceBoxContent : " + e.toString());
		}
	}
	
// LIGHT SENSOR
	public synchronized void createLightSensorMonitoring( LightSensorMonitoring lsm) throws MySqlDriverException
	{
		try
		{
			this.createLightSensorMonitoring.setString(1, lsm.getOutsideLight());
			this.createLightSensorMonitoring.setString(2, lsm.getInsideLight());
			this.createLightSensorMonitoring.setString(3, lsm.getRoomId());
			
			int status = this.createLightSensorMonitoring.executeUpdate();
			if (status == 1) logger.info("MySqlDriver in createLightSensorMonitoring, new line in LightSensorMonitoring table created.");
			else throw new MySqlDriverException("Failed to inset a parameter in database, the state of request is : " + status);
		}
		catch(SQLException | MySqlDriverException e)
		{
			throw new MySqlDriverException ("MySqlDriver error in createLightSensorMonitoring : " + e.toString());
		}
		finally
		{
			try
			{
				this.createLightSensorMonitoring.clearParameters();
			}
			catch (SQLException e)
			{
				// ignore
			}
		}
	}
	
	public synchronized List<LightSensorMonitoring> readLightSensorMonitoringList() throws MySqlDriverException
	{
		List<LightSensorMonitoring> list = new List<LightSensorMonitoring>();
		try
		{
			ResultSet set = this.readLightSensorMonitoringList.executeQuery();
			while(set.next())
			{
				LightSensorMonitoring lsm = new LightSensorMonitoring();
				lsm.setOutsideLight(set.getString(1));
				lsm.setInsideLight(set.getString(2));
				lsm.setRoom(set.getString(3));
				lsm.setCreate_time(set.getTimestamp(4));
				list.add(lsm);
			}
		}
		catch (SQLException e)
		{
			throw new MySqlDriverException ("MySqlDriver error in createLightSensorMonitoring : " + e.toString());
		}
		return list;
	}
	
// TEMPERATURE SENSOR
	
	public synchronized void createTemperatureSensorMonitoring( TemperatureSensorMonitoring tsm) throws MySqlDriverException
	{
		try
		{
			this.createTemperatureSensorMonitoring.setString(1, tsm.getTemperature());
			this.createTemperatureSensorMonitoring.setBoolean(2, tsm.getHeatingState());
			this.createTemperatureSensorMonitoring.setString(3, tsm.getRoomId());
						
			int status = this.createTemperatureSensorMonitoring.executeUpdate();
			if (status == 1) logger.info("MySqlDriver in createTemperatureSensorMonitoring, new line in temperatureSensorMonitoring table inserted.");
			else throw new MySqlDriverException("Failed to inset a parameter in database, the state of request is : " + status);
		}
		catch(SQLException | MySqlDriverException e)
		{
			throw new MySqlDriverException ("MySqlDriver error in createLightSensorMonitoring : " + e.toString());
		}
		finally
		{
			try
			{
				this.createTemperatureSensorMonitoring.clearParameters();
			}
			catch (SQLException e)
			{
				// ignore
			}
		}
	}
	
	public synchronized List<TemperatureSensorMonitoring> readTemperatureSensorMonitoringList() throws MySqlDriverException
	{
		List<TemperatureSensorMonitoring> list = new List<TemperatureSensorMonitoring>();
		try
		{
			ResultSet set = this.readTemperatureSensorMonitoringList.executeQuery();
			while(set.next())
			{
				TemperatureSensorMonitoring tsm = new TemperatureSensorMonitoring();
				tsm.setTemperature(set.getString(1));
				tsm.setHeatingState(set.getBoolean(2));
				tsm.setRoomId(set.getString(3));
				tsm.setCreate_time(set.getTimestamp(4));
				list.add(tsm);
			}
		}
		catch (SQLException e)
		{
			throw new MySqlDriverException ("MySqlDriver error in readTemperatureSensorMonitoringList : " + e.toString());
		}
		return list;
	}


	
// MANAGEMENT METHODS OF MYSQL MODULE :
	public void enable() throws MySqlDriverException
	{	
		logger.info("[" + this.label + "]* STARTING DRIVER...");
		
		if(isEnable()) 
		{
			logger.warn("[" + this.label + "]* DRIVER ALREADY STARTED...");
			return;
		}
		
		// tentative d'ouverture d'une connexion sur le serveur sql
		try
		{
			this.setBoneCP();
			this.connexion = this.connectionPool.getConnection();
			this.setPreparedStatement();
			this.setValueOfEnable(true);
		}
		catch (SQLException e)
		{
			throw new MySqlDriverException("Error(s) while opening a connection to MySqlServer ");
		}
		catch (MySqlDriverException e)
		{
			throw new MySqlDriverException ("Error(s) while creation of the prepared statements of driver" + e.getExceptionList());
		}
		logger.info ("[" + this.label + "] Loading a pool from "+ (this.partitionCount * this.minConnectionPerPartition) + " to " + (this.partitionCount * this.maxConnectionPerPartition) + " connection(s) to MySql server...");
		logger.info ("[" + this.label + "] Pool details => partition(s) : "+ this.partitionCount +", Min connexion : "+ this.minConnectionPerPartition + ", Max connexion : " + this.maxConnectionPerPartition);
		logger.info ("[" + this.label + "]* DRIVER STARTED");
	}
	
	public void disable() throws MySqlDriverException
	{
		logger.info("[" +this.label + "]* DISABLING DRIVER...");
		
		if ( ! this.isEnable())
		{
			logger.warn("[" +this.label + "]* DRIVER ALREADY DISABLED...");
			return;
		}
		this.setValueOfEnable(false);
		
		try
		{
			this.closeAllPreparedStatement();
			logger.info("[" +this.label + "] All prepared statements closed properly");
			this.connexion.close();
			this.connectionPool.close();
			logger.info("[" +this.label + "] Pool of connection(s) closed properly");
		}
		catch (Exception e)
		{
			throw new MySqlDriverException("Error(s) while disabling the MySql driver : " + e.getMessage());
		}
		
		logger.info("[" +this.label + "]* DRIVER DISABLED...");
	}
	
	public void reload() throws MySqlDriverException
	{
		logger.info("[" +this.label + "]* RELOADING DRIVER...");
		this.disable();
		this.loadParametersFromFile(this.label);
		this.enable();
		logger.info("[" +this.label + "]* DRIVER RELOADED...");
	}
	
// UTILITIES METHODS
	private synchronized void loadParametersFromFile(String label) throws MySqlDriverException
	{
		Properties properties = new Properties();
		InputStream input = null;
		
		try
		{	
			logger.debug("[" +label + "] TECHNICAL PIECES OF INFORMATION :");
			logger.debug("-----------------------------------------------------");
		//	logger.debug("  -> java.class.path : " + System.getProperty("java.class.path"));
			logger.debug("  -> java.home : " + System.getProperty("java.home"));
			logger.debug("  -> Operating system : " + System.getProperty("os.arch") + ", " + System.getProperty("os.version"));
			logger.debug("  -> Working directory : " +  System.getProperty("user.dir"));
			logger.debug("  -> User's name : " + System.getProperty("user.name"));
			logger.debug("-----------------------------------------------------");
			logger.debug("");
	        
			input = MySqlDriver.class.getResourceAsStream(propertiesPath); 
			properties.load(input);
				
			// chargement des paramètres :
			logger.info("[" +label + "]LOADED PARAMETERS :");
			logger.info("--------------------------------------");
			
			this.label = properties.getProperty("label");
			logger.info("  -> Label : " + this.label);
			
			this.url = properties.getProperty("url");
			logger.info("  -> Url : " + this.url);
			
			this.port = Integer.parseInt(properties.getProperty("port"));
			logger.info("  -> Port : " + this.port);
			
			this.userName = properties.getProperty("userName");
			logger.info("  -> UserName : " + this.userName);
			
			this.password = properties.getProperty("password");
			logger.info("  -> Password : " + this.password);
			
			this.setValueOfEnable(Boolean.parseBoolean(properties.getProperty("isEnable")));
			logger.info("  -> IsEnable : " + this.isEnable);
			
			this.minConnectionPerPartition = Integer.parseInt(properties.getProperty("minConnectionPerPartition"));
			logger.info("  -> Min connexion per partition : " + this.minConnectionPerPartition);
			
			this.maxConnectionPerPartition = Integer.parseInt(properties.getProperty("maxConnectionPerPartition"));
			logger.info("  -> Max connexion per partition : "  + this.maxConnectionPerPartition);
			
			this.partitionCount = Integer.parseInt(properties.getProperty("partitionCount"));
			logger.info("  -> Partition Count : " + this.partitionCount);

			logger.info("-------------------");
			logger.info("");
			
		}
		catch (FileNotFoundException e)
		{
			throw new MySqlDriverException(""
			+ " The  file '"+ propertiesPath +"' was not found.\n"
			+ " Abording creation of MySql driver '" + label + "': \n" + e.getMessage() );
		}
		catch (UnknownHostException e)
		{
			throw new MySqlDriverException(""
			+ " Unknown IP address specified in the file '"+ propertiesPath +"'.\n"
			+ " Abording creation of MySql driver '" + label + "': \n" + e.getMessage() );
		}
		catch (IOException e)
		{
			throw new MySqlDriverException(""
			+ " Input/Output error(s) with the file '"+ propertiesPath +"' while opening the file.\n"
			+ " Abording creation of MySql driver '" + label + "': \n" + e.getMessage());
		}
		catch (NullPointerException e)
		{
			throw new MySqlDriverException(""
			+ " Null pointer with the file '"+ propertiesPath +"' while opening the file.\n"
			+ " Abording creation of MySql driver '" + label + "': \n" + e.getMessage());
		}
		catch (NumberFormatException e)
		{
			throw new MySqlDriverException(""
			+ " Null pointer with the file '"+ propertiesPath +"' during conversion of parameters from string to int.\n"
			+ " Abording creation of MySql driver '" + label + "': \n" + e.getMessage());
		}
		catch (Exception e)
		{
			throw new MySqlDriverException(""
			+ " Global error with the file '"+ propertiesPath +"'.\n"
			+ " Abording creation of MySql driver '" + label + "': \n" + e.getMessage());
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
					throw new MySqlDriverException(""
					+ " Input/Output error(s) with the file '"+ propertiesPath +"' while closing the file.\n"
					+ " Abording creation of MySql driver '" + label + "': \n" + e.getMessage());
				}
			}
		}
	}
	
	private synchronized void setBoneCP() throws MySqlDriverException
	{
		logger.info("[" +this.label + "] Pool of connection(s) initialization...");
		// tous les paramètres de base doivent Ãªtre initialisés avant de lancer la config de boneCP
		try
		{
			Class.forName("com.mysql.jdbc.Driver");
			BoneCPConfig config = new BoneCPConfig();
			config.setJdbcUrl(this.url);
			config.setUsername(this.userName);
			config.setPassword(this.password);
			config.setPartitionCount(this.partitionCount);
			config.setMinConnectionsPerPartition(this.minConnectionPerPartition);
			config.setMaxConnectionsPerPartition(this.maxConnectionPerPartition);
			config.setDisableConnectionTracking(true);
			this.connexion = null;
			// création du pool de connexions :
			this.connectionPool = new BoneCP(config);
		} 
		catch (SQLException e)
		{
			throw new MySqlDriverException("Error(s) with the pool of SQL connections : " + e.getMessage());
		}
		catch (ClassNotFoundException e)
		{
			throw new MySqlDriverException("Impossible to load the MySql driver requierd by the pool of SQL connections : " + e.getMessage());
		}
		logger.info("[" +this.label + "] Pool of connection(s) initialized"); 
		
	}
	
	private synchronized  void setPreparedStatement() throws MySqlDriverException
	{
		
		Properties properties = new Properties();
		InputStream input = null;
		
		try
		{
			logger.info("[" +this.label + "] Begin compilation of all prepared statement by MySql server.");
			input = MySqlDriver.class.getResourceAsStream(preparedStatementPath); 
			properties.load(input);
			
			// ROOM			
			this.createRoom = connexion.prepareStatement(properties.getProperty("createRoom"));
			this.readRoomByName = connexion.prepareStatement(properties.getProperty("readRoomByName"));
			this.readRoomById = connexion.prepareStatement(properties.getProperty("readRoomById"));
			this.readRoomList = connexion.prepareStatement(properties.getProperty("readRoomList"));
			this.updateRoom = connexion.prepareStatement(properties.getProperty("updateRoom"));
			this.deleteRoom = connexion.prepareStatement(properties.getProperty("deleteRoom"));

			// ACTION
			this.createAction = connexion.prepareStatement(properties.getProperty("createAction"));
			this.readActionById = connexion.prepareStatement(properties.getProperty("readActionById"));
			this.readActionByName = connexion.prepareStatement(properties.getProperty("readActionByName"));
			this.readActionList = connexion.prepareStatement(properties.getProperty("readActionList"));
			this.updateAction = connexion.prepareStatement(properties.getProperty("updateAction"));
			this.deleteAction = connexion.prepareStatement(properties.getProperty("deleteAction"));

			// AGENDA
			this.createAgenda = connexion.prepareStatement(properties.getProperty("createAgenda"));
			this.readAgendaByDayNumber = connexion.prepareStatement(properties.getProperty("readAgendaByDayNumber"));
			this.readAgendaByRoomId = connexion.prepareStatement(properties.getProperty("readAgendaByRoomId"));
			this.readWeekAgenda = connexion.prepareStatement(properties.getProperty("readWeekAgenda"));
			this.deleteAgenda = connexion.prepareStatement(properties.getProperty("deleteAgenda"));	

			// SPICE
			this.createSpice = connexion.prepareStatement(properties.getProperty("createSpice"));
			this.readSpiceById = connexion.prepareStatement(properties.getProperty("readSpiceById"));
			this.readSpiceByName = connexion.prepareStatement(properties.getProperty("readSpiceByName"));
			this.readSpiceList = connexion.prepareStatement(properties.getProperty("readSpiceList"));
			this.updateSpice = connexion.prepareStatement(properties.getProperty("updateSpice"));
			this.deleteSpice = connexion.prepareStatement(properties.getProperty("deleteSpice"));
			
			// PARAMETER
			this.createParameter = connexion.prepareStatement(properties.getProperty("createParameter"));
			this.readParameterByKey = connexion.prepareStatement(properties.getProperty("readParameterByKey"));
			this.updateParameter = connexion.prepareStatement(properties.getProperty("updateParameter"));
			this.deleteParameterByKey = connexion.prepareStatement(properties.getProperty("deleteParameterByKey"));
						
			// SPICE_BOX
			this.readSpiceBoxContent = connexion.prepareStatement(properties.getProperty("readSpiceBoxContent"));
			this.updateSpiceBoxContent = connexion.prepareStatement(properties.getProperty("updateSpiceBoxContent"));
			this.deleteSpiceBoxContent = connexion.prepareStatement(properties.getProperty("deleteSpiceBoxContent"));
		
			// LIGHT SENSOR
			this.createLightSensorMonitoring = connexion.prepareStatement(properties.getProperty("createLightSensorMonitoring"));
			this.readLightSensorMonitoringList = connexion.prepareStatement(properties.getProperty("readLightSensorMonitoringList"));
			
			// TEMPERATURE SENSOR
			this.createTemperatureSensorMonitoring = connexion.prepareStatement(properties.getProperty("createTemperatureSensorMonitoring"));
			this.readTemperatureSensorMonitoringList = connexion.prepareCall(properties.getProperty("readTemperatureSensorMonitoringList"));
			
		}
		catch (FileNotFoundException e)
		{
			throw new MySqlDriverException(""
			+ " The  file '"+ preparedStatementPath +"' was not found."
			+ " Abording creation of the prepared statements of the MySql driver '" + label + "': \n" + e.getMessage() );
		}
		catch (SQLException e)
		{
			throw new MySqlDriverException ("Error(s) while loading prepared statements of MySql driver : " + e.getMessage());
		}
		catch (IOException e)
		{
			throw new MySqlDriverException(""
			+ " Input/Output error(s) with the file '"+ preparedStatementPath +"' while opening the file."
			+ " Abording creation of MySql driver '" + label + "': \n" + e.getMessage());
		}
		catch (Exception e)
		{
			throw new MySqlDriverException(""
			+ " Global error with the file '"+ preparedStatementPath +"'."
			+ " Abording creation of  prepared statements of MySql driver '" + label + "': \n" + e.getMessage());
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
					throw new MySqlDriverException(""
					+ " Input/Output error(s) with the file '"+ preparedStatementPath +"' while closing the file."
					+ " Abording creation of prepared statements of MySql driver '" + label + "': \n" + e.getMessage());
				}
			}
		}
		logger.info("[" +this.label + "] All prepared statements have been compiled by MySql Server.");

	}
	
	private synchronized void closeAllPreparedStatement() throws MySqlDriverException
	{
		try
		{
			// ROOM
			this.createRoom.close();
			this.readRoomByName.close();
			this.readRoomById.close();
			this.readRoomList.close();
			this.updateRoom.close();
			this.deleteRoom.close();
			
			// ACTION
			this.createAction.close();
			this.readActionById.close();
			this.readActionByName.close();
			this.readActionList.close();
			this.updateAction.close();
			this.deleteAction.close();

			// AGENDA
			this.createAgenda.close();
			this.readAgendaByDayNumber.close();
			this.readAgendaByRoomId.close();
			this.readWeekAgenda.close();
			this.deleteAgenda.close();
			
			// SPICE
			this.createSpice.close();
			this.readSpiceById.close();
			this.readSpiceByName.close();
			this.readSpiceList.close();
			this.updateSpice.close();
			this.deleteSpice.close();
			
			// PARAMETER
			this.createParameter.close();
			this.readParameterByKey.close();
			this.updateParameter.close();
			this.deleteParameterByKey.close();
			
			// SPICE_BOX
			this.readSpiceBoxContent.close();
			this.updateSpiceBoxContent.close();
			this.deleteSpiceBoxContent.close();
			
			// LIGHT SENSOR
			this.createLightSensorMonitoring.close();
			this.readLightSensorMonitoringList.close();
			
			// TEMPERATURE SENSOR
			this.createTemperatureSensorMonitoring.close();
			this.readTemperatureSensorMonitoringList.close();
		}
		catch (SQLException e)
		{
			throw new MySqlDriverException("Error(s) while closing all prepared statements of MySql driver : " + e.getMessage());
		}
	}
	
	private synchronized  boolean isEnable()
	{
		return this.isEnable;
	}
	
	private synchronized void setValueOfEnable(boolean b)
	{
		this.isEnable = b;
	}
}
