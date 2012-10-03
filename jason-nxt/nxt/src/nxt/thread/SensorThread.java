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

import nxt.BTOutputWriter;

public abstract class SensorThread extends Thread {

	int portNumber;
	BTOutputWriter out;
	String oldPerception = "";
	int sleep;
	
	public SensorThread(BTOutputWriter dos, int portNumber) {
		out = dos;
		this.portNumber = portNumber;
	}
	
	public void setSleep(int sleep) {
		this.sleep = sleep;
	}
	
	boolean running = false;
	
	public abstract String perceive();
	
	public void run() {
		running = true;
		while(running) {
			String perception = perceive();
			if(perception != null && !perception.equals(oldPerception)) {
				oldPerception = perception;
				// send perception
				out.sendString(perception + "\n");
			}
			
			if (running) {
				try { Thread.sleep(sleep); } catch (InterruptedException e) { running = false; }
			}
		}
	}
	
	public void terminate() {
		running = false;
		interrupt();
	}
	

	public static void saveValue(int[] array, int place, int value) {
		array[place % array.length] = value;
	}
	
	public static int getMedian(int[] sortedArray) {
		int middle = sortedArray.length / 2;
		if(sortedArray.length % 2 == 1) {
			return sortedArray[middle];
		} else {
			return (sortedArray[middle-1] + sortedArray[middle]) / 2;
		}
	}
	
	public static int[] sort(int[] array) {
		int[] result = new int[array.length];
		for(int i = 0; i < result.length; i++) {
			result[i] = array[i];
		}
		for(int i = 0; i < result.length; i++) {
			int val = result[i];
			int j = i - 1;
			while(j >= 0 && result[j] > val) {
				result[j + 1] = result[j];
				j = j - 1;
			}
			result[j + 1] = val;
		}
		return result;
	}

}
