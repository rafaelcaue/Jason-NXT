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

import java.io.DataOutputStream;
import java.io.IOException;

public class BTOutputWriter {

	private DataOutputStream out;
	
	public BTOutputWriter(DataOutputStream out) {
		this.out = out;
	}
	
	public synchronized void sendString(String string) {
		try {
			out.writeChars(string);
			out.flush();
		} catch (IOException e) {
			System.out.println("Could not send " + string + ".");
		}
	}
	
}
