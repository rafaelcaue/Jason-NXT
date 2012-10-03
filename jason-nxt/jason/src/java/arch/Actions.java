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

import jason.asSemantics.ActionExec;
import jason.asSyntax.ListTerm;
import jason.asSyntax.Structure;
import jason.asSyntax.Term;

import java.util.List;

import nxt.parsing.Constants;

public class Actions {

	public static String getAction(ActionExec action) {
		String term = action.getActionTerm().getFunctor();

		String result = null;
		if (term.equals(Constants.ACTION_FORWARD)) {
			result = parse2Arguments(action.getActionTerm());
		} else if (term.equals(Constants.ACTION_BACKWARD)) {
			result = parse2Arguments(action.getActionTerm());
		} else if (term.equals(Constants.ACTION_ROTATE)) {
			result = parse2Arguments(action.getActionTerm());
		} else if (term.equals(Constants.ACTION_REVERSE)) {
			result = parse1Argument(action.getActionTerm());
		} else if (term.equals(Constants.ACTION_SPEED)) {
			result = parse2Arguments(action.getActionTerm());
		} else if (term.equals(Constants.ACTION_STOP)) {
			result = parse1Argument(action.getActionTerm());
		} else if (term.equals(Constants.ACTION_BLOCKING)) {
			result = parseBoolean(action.getActionTerm());
		} else if (term.equals(Constants.ACTION_EXIT)) {
			result = Constants.ACTION_EXIT;
		}

		return result;
	}

	protected static String parse2Arguments(Structure term) {
		List<Term> terms = term.getTerms();

		if (terms.size() != 2) {
			throw new IllegalArgumentException(
					"Term does not contain valid number of arguments (" + term.getFunctor() + "(Motorlist, Speedlist)): " + term);
		}

		ListTerm motors = (ListTerm) terms.get(0);
		ListTerm speeds = (ListTerm) terms.get(1);

		if (motors.size() != speeds.size()) {
			throw new IllegalArgumentException(
					"ListTerms does not agree on number of elements: " + motors	+ ", " + speeds);
		}

		String result = "";
		for (int i = 0; i < motors.size(); i++) {
			result += motors.get(i) + term.getFunctor() + "(" + speeds.get(i)
					+ "),";
		}
		result = result.substring(0, result.length() - 1);

		return result;
	}

	protected static String parse1Argument(Structure term) {
		List<Term> terms = term.getTerms();

		if (terms.size() != 1) {
			throw new IllegalArgumentException(
					"Term does not contain valid number of arguments (" + term.getFunctor() + "(Motorlist)): " + term);
		}

		ListTerm motors = (ListTerm) terms.get(0);

		String result = "";
		for (Term motor : motors) {
			result += motor + term.getFunctor() + ",";
		}
		result = result.substring(0, result.length() - 1);

		return result;
	}

	protected static String parseBoolean(Structure term) {
		String result = term.getFunctor();

		List<Term> terms = term.getTerms();

		if (terms.size() != 1) {
			throw new IllegalArgumentException(
					"Term does not contain valid number of arguments (" + term.getFunctor() + " (true/false)): " + term);
		}

		if (terms.get(0).toString().equals("true")) {
			result += "-true";
		} else {
			result += "-false";
		}

		return result;
	}
}
