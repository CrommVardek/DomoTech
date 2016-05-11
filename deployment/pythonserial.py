
import serial
import httplib
import json
import time
ser = serial.Serial(
    port='/dev/ttyUSB0',\
    baudrate=115200,\
    parity=serial.PARITY_NONE,\
    stopbits=serial.STOPBITS_ONE,\
    bytesize=serial.EIGHTBITS,\
    timeout=None)

print("connected to: " + ser.portstr)
while True:
	line = ser.readline();
	time.sleep(.2)
	if line:
		print("line read")
		conn = httplib.HTTPConnection("192.168.43.70", 8080)
		print("connection established")
	
		data_to_update = json.dumps({"name" : line})
		headers = { "Content-type": "application/json"}
		conn.request("POST", "/", data_to_update, headers)
		r1 = conn.getresponse()
		print r1.status, r1.reason,
		conn.close(),
		print(line),
ser.close()

