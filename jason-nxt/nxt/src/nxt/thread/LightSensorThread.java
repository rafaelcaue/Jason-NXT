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

import lejos.nxt.LightSensor;
import nxt.BTOutputWriter;

public class LightSensorThread extends SensorThread {

	LightSensor ls;
	int[] lastValues = new int[10];
	int arrayPlace = 0;
	
	public LightSensorThread(BTOutputWriter dos, LightSensor ls, int portNumber) {
		super(dos, portNumber);
		this.ls = ls;
		for(int i = 0; i < lastValues.length; i++) {
			lastValues[i] = -1; // no value
		}
	}

	@Override
	public String perceive() {
		int value = ls.readNormalizedValue();
		saveValue(lastValues, arrayPlace++, value);
		int result = getMedian(sort(lastValues));
		
		if(result != -1) {
			return "light(" + portNumber + "," + result + ")";
		}
		return null;
	}
	
}
