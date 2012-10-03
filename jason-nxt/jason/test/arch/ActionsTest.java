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

import static org.junit.Assert.fail;
import static org.junit.Assert.assertEquals;
import jason.asSemantics.ActionExec;
import jason.asSemantics.Intention;
import jason.asSyntax.*;

import nxt.parsing.Constants;

import org.junit.Test;

public class ActionsTest {

	@Test
	public final void testGetAction() {
		Intention i = new Intention();
		Literal ac = new Structure(Constants.ACTION_FORWARD);
		ListTerm motors = new ListTermImpl();
		motors.add(new LiteralImpl("a"));
		motors.add(new LiteralImpl("b"));
		ac.addTerm(motors);
		ListTerm speeds = new ListTermImpl();
		speeds.add(new NumberTermImpl(60));
		speeds.add(new NumberTermImpl(30));
		ac.addTerm(speeds);
		ActionExec action = new ActionExec(ac, i);
		
		assertEquals("a" + Constants.ACTION_FORWARD + "(60),b" + Constants.ACTION_FORWARD + "(30)", Actions.getAction(action));
	}

	@Test
	public final void testParse2Arguments() {
		Structure lit = new Structure(Constants.ACTION_FORWARD);
		ListTerm motors = new ListTermImpl();
		motors.add(new LiteralImpl("a"));
		motors.add(new LiteralImpl("b"));
		lit.addTerm(motors);
		ListTerm speeds = new ListTermImpl();
		speeds.add(new NumberTermImpl(60));
		speeds.add(new NumberTermImpl(30));
		lit.addTerm(speeds);
		
		assertEquals("a" + Constants.ACTION_FORWARD + "(60),b" + Constants.ACTION_FORWARD + "(30)", Actions.parse2Arguments(lit));

		lit = new Structure(Constants.ACTION_FORWARD);
		motors = new ListTermImpl();
		motors.add(new LiteralImpl("a"));
		lit.addTerm(motors);
		speeds = new ListTermImpl();
		speeds.add(new NumberTermImpl(60));
		lit.addTerm(speeds);
		
		assertEquals("a" + Constants.ACTION_FORWARD + "(60)", Actions.parse2Arguments(lit));
		
		lit = new Structure(Constants.ACTION_FORWARD);
		motors = new ListTermImpl();
		motors.add(new LiteralImpl("a"));
		lit.addTerm(motors);
		speeds = new ListTermImpl();
		speeds.add(new NumberTermImpl(60));
		speeds.add(new NumberTermImpl(30));
		lit.addTerm(speeds);
		
		try {
			Actions.parse2Arguments(lit);
			fail("Should throw exception, because of different list size.");
		} catch (IllegalArgumentException e) {
			// OK and expected			
		}
		
		lit = new Structure(Constants.ACTION_FORWARD);
		motors = new ListTermImpl();
		motors.add(new LiteralImpl("a"));
		lit.addTerm(motors);

		try {
			Actions.parse2Arguments(lit);
			fail("Should throw exception, because of too few arguments.");
		} catch (IllegalArgumentException e) {
			// OK and expected			
		}

		lit = new Structure(Constants.ACTION_BACKWARD);
		motors = new ListTermImpl();
		motors.add(new LiteralImpl("a"));
		lit.addTerm(motors);
		speeds = new ListTermImpl();
		speeds.add(new NumberTermImpl(60));
		lit.addTerm(speeds);
		
		assertEquals("a" + Constants.ACTION_BACKWARD + "(60)", Actions.parse2Arguments(lit));

		lit = new Structure(Constants.ACTION_ROTATE);
		motors = new ListTermImpl();
		motors.add(new LiteralImpl("a"));
		lit.addTerm(motors);
		speeds = new ListTermImpl();
		speeds.add(new NumberTermImpl(60));
		lit.addTerm(speeds);
		
		assertEquals("a" + Constants.ACTION_ROTATE + "(60)", Actions.parse2Arguments(lit));

		lit = new Structure(Constants.ACTION_SPEED);
		motors = new ListTermImpl();
		motors.add(new LiteralImpl("a"));
		lit.addTerm(motors);
		speeds = new ListTermImpl();
		speeds.add(new NumberTermImpl(60));
		lit.addTerm(speeds);
		
		assertEquals("a" + Constants.ACTION_SPEED + "(60)", Actions.parse2Arguments(lit));
	}

	@Test
	public final void testParse1Argument() {
		Structure lit = new Structure(Constants.ACTION_REVERSE);
		ListTerm motors = new ListTermImpl();
		motors.add(new LiteralImpl("a"));
		motors.add(new LiteralImpl("b"));
		lit.addTerm(motors);
		
		assertEquals("a" + Constants.ACTION_REVERSE + ",b" + Constants.ACTION_REVERSE, Actions.parse1Argument(lit));
		
		lit = new Structure(Constants.ACTION_REVERSE);
		motors = new ListTermImpl();
		motors.add(new LiteralImpl("a"));
		lit.addTerm(motors);
		
		assertEquals("a" + Constants.ACTION_REVERSE, Actions.parse1Argument(lit));

		lit = new Structure(Constants.ACTION_REVERSE);
		
		try {
			Actions.parse1Argument(lit);
			fail("Should throw exception, since arguments are wrong.");
		} catch (IllegalArgumentException ex) {
			// Expected and ok
		}

		lit = new Structure(Constants.ACTION_STOP);
		motors = new ListTermImpl();
		motors.add(new LiteralImpl("a"));
		lit.addTerm(motors);
		
		assertEquals("a" + Constants.ACTION_STOP, Actions.parse1Argument(lit));

	}
}
