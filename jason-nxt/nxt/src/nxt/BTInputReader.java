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
package nxt;

import java.io.DataInputStream;
import java.io.IOException;

import nxt.parsing.ActionParser;
import nxt.parsing.Constants;

public class BTInputReader extends Thread {

	LEGOJasonNXT robot;
	DataInputStream in;
	boolean running = false;
	
	public BTInputReader(DataInputStream in, LEGOJasonNXT robot) {
		this.in = in;
		this.robot = robot;
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void run() {
		running = true;
		while(running) {
			try {
				String value = in.readLine();
				
				if(value == null) {
					running = false;
					break;
				}
				
				String newValue = "";
				// clears the string for errors.
				for(char c : value.toCharArray()) {
					if(c != 0) {
						newValue += c;
					}
				}
				value = newValue;
				
				System.out.println("Rcvd: " + value);
				
				if(value.equals(Constants.ACTION_EXIT)) {
					running = false;
				} else { 				
					ActionParser.parseAndAct(value);
				}
			} catch (IOException e) {
				System.out.println("Could not read BT input.");
			}
		}
		
		robot.terminate();
	}
	
}
