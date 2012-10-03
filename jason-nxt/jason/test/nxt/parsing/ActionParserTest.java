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

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ActionParserTest {

	@Test
	public final void testContains() {
		String string = "arevdir";
		String substring = "forward";
		
		assertFalse(ActionParser.contains(string, substring));
		
		substring = "revdir";
		
		assertTrue(ActionParser.contains(string, substring));
		
		substring = "stop";
			
		assertFalse(ActionParser.contains(string, substring));
		
		string = "aforward(30)";
		substring = "forward";

		assertTrue(ActionParser.contains(string, substring));
		
		substring = "stop";
		
		assertFalse(ActionParser.contains(string, substring));
		
		substring = "backward";
		
		assertFalse(ActionParser.contains(string, substring));
	}

	@Test
	public final void testSplit() {
		String actions = "arevdir,brevdir";
		String[] actionList = new String[] { "arevdir", "brevdir" };
		
		assertArrayEquals(actionList, ActionParser.split(actions, ','));

		actions = "arevdir";
		actionList = new String[] { "arevdir" };
		
		assertArrayEquals(actionList, ActionParser.split(actions, ','));
		
		actions = "arevdir,brevdir,crevdir";
		actionList = new String[] { "arevdir", "brevdir", "crevdir" };
		
		assertArrayEquals(actionList, ActionParser.split(actions, ','));		
	}

	@Test
	public final void testGetValue() {
		String action = "aforward(30)";
		int value = 30;
		
		assertEquals(value, ActionParser.getValue(action));
		
		action = "aforward(-30)";
		value = -30;
		
		assertEquals(value, ActionParser.getValue(action));
	}

}
