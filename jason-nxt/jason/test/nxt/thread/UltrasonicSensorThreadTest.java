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
package nxt.thread;

import nxt.thread.SensorThread;
import nxt.thread.UltrasonicSensorThread;

import org.junit.Test;
import static org.junit.Assert.*;

public class UltrasonicSensorThreadTest {

	@Test
	public void testSaveValue() {
		UltrasonicSensorThread thread = new UltrasonicSensorThread(null, null, 0);
		
		assertEquals(10, thread.lastDistances.length);

		assertArrayEquals(new int[]{255,255,255,255,255,255,255,255,255,255}, thread.lastDistances);
		
		SensorThread.saveValue(thread.lastDistances, thread.arrayPlace++, 30);
		
		assertArrayEquals(new int[]{30,255,255,255,255,255,255,255,255,255}, thread.lastDistances);
		
		SensorThread.saveValue(thread.lastDistances, thread.arrayPlace++, 35);
		
		assertArrayEquals(new int[]{30,35,255,255,255,255,255,255,255,255}, thread.lastDistances);
		
		SensorThread.saveValue(thread.lastDistances, thread.arrayPlace++, 40);
		
		assertArrayEquals(new int[]{30,35,40,255,255,255,255,255,255,255}, thread.lastDistances);
	}
	
	@Test
	public void testGetMedian() {
		UltrasonicSensorThread thread = new UltrasonicSensorThread(null, null, 0);
		
		assertEquals(10, thread.lastDistances.length);
		
		assertEquals(255, SensorThread.getMedian(SensorThread.sort(thread.lastDistances)));
		
		SensorThread.saveValue(thread.lastDistances, thread.arrayPlace++, 30);
		SensorThread.saveValue(thread.lastDistances, thread.arrayPlace++, 30);
		SensorThread.saveValue(thread.lastDistances, thread.arrayPlace++, 30);
		SensorThread.saveValue(thread.lastDistances, thread.arrayPlace++, 30);
		
		assertEquals(255, SensorThread.getMedian(SensorThread.sort(thread.lastDistances)));
		
		SensorThread.saveValue(thread.lastDistances, thread.arrayPlace++, 30);
		SensorThread.saveValue(thread.lastDistances, thread.arrayPlace++, 30);
		
		assertEquals(30, SensorThread.getMedian(SensorThread.sort(thread.lastDistances)));
		
		SensorThread.saveValue(thread.lastDistances, thread.arrayPlace++, 25);
		
		assertEquals(30, SensorThread.getMedian(SensorThread.sort(thread.lastDistances)));
	}
	
}
