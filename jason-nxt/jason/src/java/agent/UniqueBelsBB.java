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
package agent;

import jason.asSemantics.Agent;
import jason.asSemantics.Unifier;
import jason.asSyntax.Literal;
import jason.asSyntax.Term;
import jason.bb.DefaultBeliefBase;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Customised version of Belief Base where some beliefs are unique (with primary keys).
 * 
 * <p>E.g.:<br/>
 * <code>beliefBaseClass agent.UniqueBelsBB("student(key,_)", "depot(_,_,_)")</code>
 * <br/>
 * The belief "student/2" has the first argument as its key, so the BB will never has
 * two students with the same key. Or, two students in the BB will have two different keys.
 * The belief "depot/3" has no key, so there will be always only one "depot" in the BB.
 * 
 * @author jomi
 */
public class UniqueBelsBB extends DefaultBeliefBase {
    Map<String,Literal> uniqueBels = new HashMap<String,Literal>();
    Unifier             u = new Unifier();
    Agent               myAgent;
    
	@Override
    public void init(Agent ag, String[] args) {
        this.myAgent = ag;
    	for (int i=0; i<args.length; i++) {
    		Literal arg = Literal.parseLiteral(args[i]);
    		uniqueBels.put(arg.getFunctor(), arg);
    	}
    }

	@Override
	public boolean add(Literal bel) {
		Literal kb = uniqueBels.get(bel.getFunctor());
		if (kb != null && kb.getArity() == bel.getArity()) { // is a constrained bel?
			
			// find the bel in BB and eventually remove it
			u.clear();
			Literal linbb = null;
			boolean remove = false;

			Iterator<Literal> relevant = getCandidateBeliefs(bel, null);
			if (relevant != null) {
			    final int kbArity = kb.getArity();
				while (relevant.hasNext() && !remove) {
					linbb = relevant.next();

					// check equality of all terms that are "key"
					// if some key is different, no problem
					// otherwise, remove the current bel
					boolean equals = true;
					for (int i = 0; i<kbArity; i++) {
						Term kbt = kb.getTerm(i);
						if (!kbt.isVar()) { // is key?
							if (!u.unifies(bel.getTerm(i), linbb.getTerm(i))) {
								equals = false;
								break;
							}
						}
					}
					if (equals) {
						remove = true;
					}
				}
			}
			if (remove) {
				remove(linbb);
			}
		}
		return super.add(bel);
	}

}
