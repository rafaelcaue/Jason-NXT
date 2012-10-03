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

import gui.AgentInterface;
import jason.JasonException;
import jason.architecture.AgArch;
import jason.asSemantics.ActionExec;
import jason.asSyntax.Literal;
import jason.mas2j.ClassParameters;
import jason.runtime.Settings;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Logger;

import lejos.pc.comm.NXTComm;
import lejos.pc.comm.NXTCommException;
import lejos.pc.comm.NXTCommFactory;
import lejos.pc.comm.NXTInfo;
import nxt.parsing.Constants;

public class LEGOAgArchitecture extends AgArch {

	private final Logger LOG = Logger.getLogger(LEGOAgArchitecture.class + "." + getAgName());

	private final static HashMap<String, Integer> sensorTypes = new HashMap<String, Integer>() {
		{
			put("touch", Constants.SENSOR_TOUCH);
			put("light", Constants.SENSOR_LIGHT);
			put("ultrasonic", Constants.SENSOR_ULTRA);
			put("sound", Constants.SENSOR_SOUND);
			put("none", Constants.SENSOR_NONE);
		}
	};
	private final static List<String> endingActions = new Vector<String>() {
		{
			add(Constants.ACTION_ROTATE);
			add(Constants.ACTION_REVERSE);
		}
	};

	private String btName, btAddress;
	private boolean motorA, motorB, motorC;
	private int sensor1, sensor2, sensor3, sensor4;
	private int sleep = 50;
	
	private List<Literal> pendingBeliefs;
	
	private NXTComm comm;
	private NXTInfo nxt;
	private DataOutputStream out;
	private DataInputStream in;

	private BTInputReader inputReader;

	private Map<String, String> previousActions = new HashMap<String, String>();
	private String lastActionFunctor = "";
	
	@Override
	public void initAg(String agClass, ClassParameters bbPars, String asSrc,
			Settings stts) throws JasonException {
		super.initAg(agClass, bbPars, asSrc, stts);
		LOG.info(""+this);
		// Read parameters
		loadParameters(stts);

		pendingBeliefs = new Vector<Literal>();
		AgentInterface.getInstance().addAgentTab(this);
		
		try {
			LOG.info("Connecting to " + btName + " (" + btAddress + ").");
			openBluetooth();
			LOG.info("Connected to " + nxt.name + " (" + nxt.deviceAddress
					+ "). State = " + nxt.connectionState);
			
			// send settings
			out.writeInt(sleep);

			// send motor info
			out.writeBoolean(motorA);
			out.writeBoolean(motorB);
			out.writeBoolean(motorC);

			// send sensor info
			out.writeInt(sensor1);
			out.writeInt(sensor2);
			out.writeInt(sensor3);
			out.writeInt(sensor4);
			out.flush();

			// Listen for perceptions
			inputReader = new BTInputReader(in, pendingBeliefs);
			inputReader.start();
		} catch (NXTCommException e) {
			LOG.severe("Could not open bluetooth connection to NXT: " + btName
					+ "(" + btAddress + ").");
		} catch (IOException e) {
			LOG.severe("Could not communicate with " + btName + ".");
		}
	}

	private void loadParameters(Settings stts) {
		// Bluetooth info
		btName = stripQuotes(stts.getUserParameter("btname"));
		btAddress = stripQuotes(stts.getUserParameter("btaddress"));
		// motor info
		motorA = Boolean.parseBoolean(stripQuotes(stts
				.getUserParameter("motora")));
		motorB = Boolean.parseBoolean(stripQuotes(stts
				.getUserParameter("motorb")));
		motorC = Boolean.parseBoolean(stripQuotes(stts
				.getUserParameter("motorc")));
		// sensor info
		sensor1 = sensorTypes
				.get(stripQuotes(stts.getUserParameter("sensor1")));
		sensor2 = sensorTypes
				.get(stripQuotes(stts.getUserParameter("sensor2")));
		sensor3 = sensorTypes
				.get(stripQuotes(stts.getUserParameter("sensor3")));
		sensor4 = sensorTypes
				.get(stripQuotes(stts.getUserParameter("sensor4")));
		
		if(stts.getUserParameters().containsKey("sleep")) {
			sleep = Integer.parseInt(stripQuotes(stts.getUserParameter("sleep")));
		}
	}

	/**
	 * Opens BT communication to the NXT with Address and Name defined in the
	 * UserParameters. Also sets up data streams for input and output.
	 * 
	 * @throws NXTCommException
	 */
	private void openBluetooth() throws NXTCommException {
		comm = NXTCommFactory.createNXTComm(NXTCommFactory.BLUETOOTH);
		nxt = new NXTInfo(NXTCommFactory.BLUETOOTH, btName, btAddress);
		comm.open(nxt, NXTComm.PACKET);
		out = new DataOutputStream(comm.getOutputStream());
		in = new DataInputStream(comm.getInputStream());
	}

	@Override
	public void act(ActionExec action, List<ActionExec> feedback) {
		try {
			String res = Actions.getAction(action);
			String actionFunctor = action.getActionTerm().getFunctor();
			String actionString = action.getActionTerm().toString();
			if (endingActions.contains(actionFunctor)
					|| !actionFunctor.equals(lastActionFunctor) 
					|| !(previousActions.containsKey(actionFunctor)
							&& previousActions.get(actionFunctor).equals(actionString))) {
				
				previousActions.put(action.getActionTerm().getFunctor(), action.getActionTerm().toString());
				lastActionFunctor = action.getActionTerm().getFunctor();
				LOG.info("Sending " + res + " to " + btName + ".");
				out.writeChars(res + "\n");
				out.flush();
				AgentInterface.getInstance().addAction(getAgName(), res);
			}
		} catch (IOException e) {
			LOG.severe("Could not send action to agent: " + e);
		}
		action.setResult(true);
		feedback.add(action);
	}

	@Override
	public List<Literal> perceive() {
		List<Literal> percepts = super.perceive();
		if(percepts == null) {
			percepts = new Vector<Literal>();
		}
		List<Literal> copied = new Vector<Literal>(pendingBeliefs);
		pendingBeliefs.clear();
		percepts.addAll(copied);
		

		return percepts; 
	}
	
	private String stripQuotes(String string) {
		if (string.startsWith("\"")) {
			string = string.substring(1, string.length() - 1);
		}
		return string;
	}

	public boolean isMotorA() {
		return motorA;
	}

	public boolean isMotorB() {
		return motorB;
	}

	public boolean isMotorC() {
		return motorC;
	}

	public int getSensor1() {
		return sensor1;
	}

	public int getSensor2() {
		return sensor2;
	}

	public int getSensor3() {
		return sensor3;
	}

	public int getSensor4() {
		return sensor4;
	}
	
	public String getBtAddress() {
		return btAddress;
	}
	
	public int getSleep() {
		return sleep;
	}
}
