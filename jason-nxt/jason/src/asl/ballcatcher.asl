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
// Ball catcher agent for the LEGO Mindstorms NXT


/* Initial beliefs and rules */
// initial belif is that the goal is to search for an obstacle to grap
goal(searching).

/* Initial goals */
!init.

/* Plans */
+!init <- rotate([c],[-150]). // initialize the grabber, by rotating it backwards

// very simple implementation, where the robot goes forward when searching, and stops when searching is not a goal anymore.
+!search : goal(searching) <- forward([a,b],[60,60]).
+!search <- stop([a,b]).

// upon failure, start from scratch and reintroduce.
-!search <- .drop_all_desires; .abolish(touching(_,_)); .abolish(obstacle(_,_)); !!search.

// if no obstacle in range, keep searching. Otherwise remove search-goal and grab object
+obstacle(_, X) : 	X >= 15
				<-	!search.
+obstacle(_, X) : 	X < 15 & goal(searching) 
				<-  -goal(_); rotate([c],[150]).
				
// celebrate if the object is touching the touch-sensor!
+touching(_, true) <- 	stop([a,b,c]); speed([a,b,c],[360,360,360]); 
						rotate([a,b,c],[360,-360,-150]); 
						rotate([a,b,c],[360,-360,150]); exit. 