/*
 * Copyright 2010 Andreas Schmidt Jensen
 *
 * This file is part of LEGO-Jason-NXT.
 *
 * LEGO-Jason-NXT is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * LEGO-Jason-NXT is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with LEGO-Jason-NXT.  If not, see <http://www.gnu.org/licenses/>.
 */
package nxt.thread;

import lejos.nxt.*;
import nxt.BTOutputWriter;
import nxt.parsing.Constants;

public class SensorThreadFactory {

	private static int sleep;
	
	public static SensorThread createSensorThread(BTOutputWriter out, SensorPort port, int sensortype) {
		SensorThread result = null;
		
		int portNumber = 0;
		if(port == SensorPort.S1) {
			portNumber = 1;
		} else if(port == SensorPort.S2) {
			portNumber = 2;
		} else if(port == SensorPort.S3) {
			portNumber = 3;
		} else if(port == SensorPort.S4) {
			portNumber = 4;
		}
		
		switch(sensortype) {
		case Constants.SENSOR_LIGHT:
			System.out.println(" - Light");
			result = new LightSensorThread(out, new LightSensor(port), portNumber);
			result.setSleep(sleep);
			break;
		case Constants.SENSOR_SOUND:
			System.out.println(" - Sound");
			result = new SoundSensorThread(out, new SoundSensor(port), portNumber);
			result.setSleep(sleep);
			break;
		case Constants.SENSOR_TOUCH:
			System.out.println(" - Touch");
			result = new TouchSensorThread(out, new TouchSensor(port), portNumber);
			result.setSleep(sleep);
			break;
		case Constants.SENSOR_ULTRA:
			System.out.println(" - US");
			result = new UltrasonicSensorThread(out, new UltrasonicSensor(port), portNumber);
			result.setSleep(sleep);
			break;
		default:
			System.out.println(" - Unknown");
			result = null;
			break;
		}
		
		return result;
	}

	public static void setSleep(int sleep) {
		SensorThreadFactory.sleep = sleep;
	}
	
}
