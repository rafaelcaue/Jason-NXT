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
package arch;

import jason.asSyntax.ASSyntax;
import jason.asSyntax.Literal;
import jason.asSyntax.parser.ParseException;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BTInputReader extends Thread {

	private static final Logger LOG = Logger.getLogger(BTInputReader.class.getName());

	private List<Literal> pendingBeliefs;
	private DataInputStream in;
	private boolean running = false;

	public BTInputReader(DataInputStream in, List<Literal> pendingBeliefs) {
		this.in = in;
		this.pendingBeliefs = pendingBeliefs;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void run() {
		running = true;
		while (running) {
			try {
				String value = in.readLine();

				if (value == null) {
					running = false;
					break;
				}

				String newValue = "";
				// clears the string for errors.
				for (char c : value.toCharArray()) {
					if (c != 0) {
						newValue += c;
					}
				}
				value = newValue;
				LOG.info("Received: " + value);
				Literal l = Literal.parseLiteral(value);
				try {
					l.addSource(ASSyntax.parseTerm("percept"));
				} catch (ParseException ex) {
					LOG.log(Level.SEVERE, "Could not add source", ex);
				}
				pendingBeliefs.add(l);
			} catch (IOException ex) {
				LOG.log(Level.SEVERE, "Could not read BT input.", ex);
			}
		}
	}

	public void stopRunning() {
		running = false;
		interrupt();
	}
}
