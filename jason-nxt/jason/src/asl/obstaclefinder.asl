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
// Agent for finding obstacles and avoiding them.
// When an obstacle is found, the agent sends information
// to a blind agent about how to avoid the obstacle.

/* Initial beliefs and rules */
// How many bars have the agent currently passed
bars_passed(0).

// Assume we alternate between black and white bars
last_color(white).

// intially the agent searches for an obstacle
goal(search).

// The agent is on a bar if the sensors is reading a value less than 350
on_bar(Value) :- Value < 350.


/* Initial goals */
!init.

/* Plans */
+!init <- block(true); !!move.

// Increment bar-counter when on a black bar and last bar was white. Make note that the agent is on a black bar now.
+light(_, X)[source(percept)] : goal(search) & on_bar(X) & last_color(white) <- -+last_color(black); ?bars_passed(N); BarsPassed = N + 1; -+bars_passed(BarsPassed); .print("Passed black").
// Note that now the agent is on a white bar.
+light(_, X)[source(percept)] : goal(search) & not on_bar(X) & last_color(black) <- -+last_color(white); .print("Passed white").

// when an obstacle is near -- tell the blind agent and avoid!
+obstacle(_, X)[source(percept)]
			: 	goal(search) & X < 15 
			<- 	-+goal(avoid);
				?bars_passed(N);				 
				.send(blindagent, tell, obstacle_after(N));
				.print("Passed: ", N); 
				!!avoid.

// Avoiding the obstacle. It is assumed that an obstacle can only be of one size.
 +!avoid	<-	stop([a,b]); speed([a,b],[300,300]);
 				rotate([a,b],[-200,200]); rotate([a,b],[400,400]); 
 				rotate([a,b],[200,-200]); rotate([a,b],[800,800]); 
 				rotate([a,b],[200,-200]); rotate([a,b],[400,400]); 
 				rotate([a,b],[-200,200]); !!move.
 				
 // approach the obstacle slowly.
 +!move <- forward([a,b],[60,60]).