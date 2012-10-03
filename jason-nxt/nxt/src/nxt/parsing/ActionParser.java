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
package nxt.parsing;

import java.util.ArrayList;

import lejos.nxt.Motor;
import nxt.Settings;

public class ActionParser {

	private static boolean immediateReturn = true;
	
	public static void parseAndAct(String actions) {
		String[] actionList = split(actions, ',');
		for (int i = 0; i < actionList.length; i++) {
			String action = actionList[i];
			Motor motor;
			int value;

			motor = getMotor(action.substring(0, 1));
			
			if(motor == null) {
				continue;
			}
			
			if (contains(action, Constants.ACTION_FORWARD)) {
				value = getValue(action);
				
				motor.setSpeed(value);
				motor.forward();
				
			} else if (contains(action, Constants.ACTION_BACKWARD)) {
				value = getValue(action);
				
				motor.setSpeed(value);
				motor.backward();
				
			} else if (contains(action, Constants.ACTION_ROTATE)) {
				value = getValue(action);
				
				boolean immReturn = immediateReturn;
				
				// immediate return when there are still more motors in the list, since otherwise it is impossible to rotate two motors and let them block at the same time.
				if(i != actionList.length - 1) {
					immReturn = true;
				}
				motor.rotate(value, immReturn);
				
			} else if (contains(action, Constants.ACTION_REVERSE)) {

				motor.reverseDirection();

			} else if (contains(action, Constants.ACTION_SPEED)) {
				value = getValue(action);
				
				motor.setSpeed(value);
				
			} else if (contains(action, Constants.ACTION_STOP)) {

				motor.stop();
				
			} else if (contains(action, Constants.ACTION_BLOCKING)) {
				boolean blocking = getBoolean(action);
				
				immediateReturn = !blocking;
			}
		}
	}

	private static boolean getBoolean(String action) {
		String bool = split(action, '-')[1];
		return bool.equals("true");
	}

	protected static boolean contains(String string, String substring) {
		if (substring.length() > string.length())
			return false;

		for (int i = 0; i < string.length() - substring.length() + 1; i++) {
			if (string.substring(i, substring.length() + i).equals(substring)) {
				return true;
			}
		}

		return false;
	}

	protected static String[] split(String string, char delimiter) {
		ArrayList<String> result = new ArrayList<String>();
		int pos;
		while ((pos = string.indexOf(delimiter)) > -1) {
			result.add(string.substring(0, pos));
			string = string.substring(pos + 1);
		}
		result.add(string);

		String[] res = new String[result.size()];
		for(int i = 0; i < result.size(); i++) {
			res[i] = result.get(i);
		}
		return res;
	}
	
	protected static int getValue(String string) {
		int lparen = string.indexOf('(');
		int rparen = string.indexOf(')');
		String val = string.substring(lparen + 1, rparen);
		return Integer.parseInt(val);
	}
	
	protected static Motor getMotor(String motor) {
		if(motor.equalsIgnoreCase("a")) {
			return Settings.motorA;
		} else if(motor.equalsIgnoreCase("b")) {
			return Settings.motorB;
		} else if(motor.equalsIgnoreCase("c")) {
			return Settings.motorC;
		}
		return null;
	}
}
