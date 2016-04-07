package features;

import com.phidgets.*;

public class LightManager {
	
	int room;
	int sensorValue;
	int simplifiedSensorValue;
	//Power of the light when it's On (from 0 to 4) - Output scale
	int ledPower = 0;
	//Light On or Off
	boolean lightOn = false;

	public LightManager (int room, int value) throws PhidgetException{
		
		this.room = room;
		sensorValue = value;
		simplifiedSensorValue = SimplifyValue(sensorValue);
		
	}
	
	//Create a simplified abstract light value
	private int SimplifyValue(int val){
		
		if (val<0 || val > 1000) return 0;
		/*
		 * Abstract Light Scale:
		 * Raw Value | Abstract Value
		 * 0-50 : 0-Dark
		 * 51-100: 1-Light Dark
		 * 101-200: 2-Low Light Level
		 * 201-400: 3-Normal Light Level
		 * 401-600: 4-Room Light Level
		 * 601-850: 5-Workspace Light Level
		 * 851-999: 6-High Light Level
		 */
		if (val < 51){
			return 0;
		}
		else{
			if(val < 101){
				return 1;
			}
			else {
				if(val < 201){
					return 2;
				}
				else{
					if(val < 401){
						return 3;
					}
					else{
						if(val < 601){
							return 4;
						}
						else{
							if(val < 851){
								return 5;
							}
							else{
								return 6;
							}
						}
					}
				}
			}
		}
	}
	
	//TODO: Add args for LED management and time (night or day mode). RETURN INT (nbr of leds)
	
	//Return int represents the number of leds that will be ON. Intensity of the light.
	public int setLedPower(boolean night){
		if(!lightOn){
						
			if (night){
				return 1;
			}
			

			/* WHEN LIGHT IS OFF:
			 * Dark, Light Dark: 4/4 LEDs
			 * LLL: 3/4 LEDs
			 * NLL: 2/4 LEDs
			 * RLL: 1/4 LEDs 
			 */
			
			switch(simplifiedSensorValue){
			 case 0: return 4; 
			 case 1: return 4; 
			 case 2: return 3; 
			 case 3: return 2; 
			 case 4: return 1; 
			 case 5: return 0; 
			 case 6: return 0; 
			 default: return 0; 
			}
		}
		
		else{
			
			/* WHEN LIGHT IS ON:
			 * Dark, Light Dark: 4/4 LEDs
			 * LLL: 3/4 LEDs
			 * NLL: 2/4 LEDs
			 * RLL: 1/4 LEDs 
			 */
			//Compare old and new values
			switch(simplifiedSensorValue){
			 case 0: return 4; 
			 case 1: return 4; 
			 case 2: return 3; 
			 case 3: return 2; 
			 case 4: return 1; 
			 case 5: return 0; 
			 case 6: return 0; 
			 default: return 0; 
			}
		}
			
			
	}
	
	
	
}
