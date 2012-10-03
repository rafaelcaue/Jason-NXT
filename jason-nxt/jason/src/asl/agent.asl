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
// Agent sample in project LEGO-Jason

/* Initial beliefs and rules */

/* Initial goals */

!start.

/* Plans */

+!start : true <- backward([c],[200]); stop([a,b,c]).

+touching(_,true) <- forward([c],[100]).

+touching(_,false) <- stop([c]).

+obstacle(_,X) : X < 60 <- backward([a,b],[360,180]).

+obstacle(_,X) <- forward([a,b],[360,360]).