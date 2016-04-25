package commonsObjects;

public enum Request {

	// Room
	createRoom,
	readRoomByName,
	readRoomById,
	readRoomList,
	updateRoom,
	deleteRoom,

	//Action
	createAction,
	readActionById,
	readActionByName,
	readActionList,
	updateAction,
	deleteAction,

	//Agenda
	createAgenda,
	readAgendaByDayNumber,
	readAgendaByRoomId,
	readWeekAgenda,
	deleteAgenda,

	//Spice
	createSpice,
	readSpiceById,
	readSpiceByName,
	readSpiceList,
	updateSpice,
	deleteSpice,

	//Parameter
	createParameter,
	readParameterByKey,
	updateParameter,
	deleteParameterByKey,

	//SpiceBox
	readSpiceBoxContent,
	updateSpiceBoxContent,
	deleteSpiceBoxContent,

	// LightSensor
	createLightSensorMonitoring,
	readLightSensorMonitoringList,

	//TemperatureSensor
	createTemperatureSensorMonitoring,
	readTemperatureSensorMonitoringList,
	
	//Exception
	StorageManagerException,
	MySqlDriverException,
	
	// Message
	Message
}
