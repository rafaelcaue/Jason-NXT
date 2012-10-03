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

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import nxt.thread.SensorThread;
import nxt.thread.SensorThreadFactory;

import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.comm.Bluetooth;
import lejos.nxt.comm.NXTConnection;

public class LEGOJasonNXT {
	
	List<SensorThread> sensors;
	NXTConnection conn;
	DataInputStream dis;
	DataOutputStream dos;
	BTInputReader inputReader;
	BTOutputWriter outputWriter;
	
	public static void main(String[] args) throws IOException {
		new LEGOJasonNXT();
	}

	public LEGOJasonNXT() throws IOException {
		
		sensors = new ArrayList<SensorThread>();
		System.out.println("LEGO Jason NXT");
		System.out.println("Waiting for BT");

		conn = Bluetooth.waitForConnection(60000, NXTConnection.PACKET);

		if(conn == null){
			return;
		}
		
		System.out.println("Connected!");

		dis = conn.openDataInputStream();
		dos = conn.openDataOutputStream();

		outputWriter = new BTOutputWriter(dos);
		
		System.out.println("Loading...");
		int sleep = dis.readInt();
		SensorThreadFactory.setSleep(sleep);
		
		System.out.println(" - Motors...");
		Settings.motorA = dis.readBoolean() ? Motor.A : null;
		Settings.motorB = dis.readBoolean() ? Motor.B : null;
		Settings.motorC = dis.readBoolean() ? Motor.C : null;
		
		System.out.println(" - Sensors...");
		SensorThread st1 = SensorThreadFactory.createSensorThread(outputWriter, SensorPort.S1, dis.readInt());
		SensorThread st2 = SensorThreadFactory.createSensorThread(outputWriter, SensorPort.S2, dis.readInt());
		SensorThread st3 = SensorThreadFactory.createSensorThread(outputWriter, SensorPort.S3, dis.readInt());
		SensorThread st4 = SensorThreadFactory.createSensorThread(outputWriter, SensorPort.S4, dis.readInt());
		if(st1 != null) sensors.add(st1);
		if(st2 != null) sensors.add(st2);
		if(st3 != null) sensors.add(st3);
		if(st4 != null) sensors.add(st4);
		
		System.out.println("Done!");
		
		for(SensorThread sensor : sensors) {
			sensor.start();
		}
		
		inputReader = new BTInputReader(dis, this);
		inputReader.start();
		
	}
	
	public void terminate() {
		System.out.println("Terminating.");
		
		for(SensorThread sensor : sensors) {
			sensor.terminate();
		}
		
		try {
			dos.close();
		} catch (Exception e) {
		}
		try {
			dis.close();
		} catch (Exception e) {
		}
		conn.close();
	}
	
}
