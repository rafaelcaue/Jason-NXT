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
package gui;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.*;

import nxt.parsing.Constants;
import arch.LEGOAgArchitecture;

public class AgentInterface extends JFrame {

	private final static AgentInterface _INSTANCE = new AgentInterface();
	
	private JTabbedPane tabs;
	private ArrayList<JPanel> agentTabs;
	private Map<String, JList> perceptionMapping;
	private Map<String, JList> actionMapping;
	
	public static AgentInterface getInstance() {
		return _INSTANCE;
	}
	
	private AgentInterface() {
		setLocationByPlatform(true);
		setVisible(true);
		setSize(350, 600);
		initialize();
	}
	
	private void initialize() {
		tabs = new JTabbedPane();
		add(tabs);
		
		agentTabs = new ArrayList<JPanel>();
		perceptionMapping = new HashMap<String, JList>();
		actionMapping = new HashMap<String, JList>();
	}
	
	public void addAgentTab(LEGOAgArchitecture agent) {
		JPanel agentPanel = new JPanel();
		GridBagLayout layout = new GridBagLayout();
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = 0;
		gbc.gridy = 0;
		agentPanel.setLayout(layout);
		
		agentPanel.setName(agent.getAgName());
		agentTabs.add(agentPanel);
		tabs.add(agentPanel);
		
		
		{
			JLabel label = new JLabel("Argumentos do Agente");
			label.setFont(new Font("Serif", Font.BOLD, 16));
			agentPanel.add(label, gbc);
		}
		gbc.gridy++;
		
		agentPanel.add(new JLabel("Endereço Bluetooth: "), gbc);
		gbc.gridx++;
		agentPanel.add(new JLabel(agent.getBtAddress()), gbc);
		
		gbc.gridx = 0;
		gbc.gridy++;
		agentPanel.add(new JLabel("Sensor sleep: "), gbc);
		gbc.gridx++;
		agentPanel.add(new JLabel(agent.getSleep() + " ms"), gbc);
		
		gbc.gridx = 0;
		gbc.gridy++;
		agentPanel.add(new JLabel("Motores conectados: "), gbc);
		gbc.gridx++;
		agentPanel.add(new JLabel((agent.isMotorA() ? "A, " : "") + (agent.isMotorB() ? "B, " : "") + (agent.isMotorC() ? "C" : "")), gbc);
		
		gbc.gridx = 0;
		gbc.gridy++;
		{
			gbc.insets = new Insets(10, 0, 0, 0);
			JLabel label = new JLabel("Sensores");
			label.setFont(new Font("Serif", Font.BOLD, 16));
			agentPanel.add(label, gbc);
			gbc.insets = new Insets(0, 0, 0, 0);
		}
		gbc.gridy++;
		agentPanel.add(new JLabel(" > Porta 1 = " + getSensor(agent.getSensor1())), gbc);
		gbc.gridy++;
		agentPanel.add(new JLabel(" > Porta 2 = " + getSensor(agent.getSensor2())), gbc);
		gbc.gridy++;
		agentPanel.add(new JLabel(" > Porta 3 = " + getSensor(agent.getSensor3())), gbc);
		gbc.gridy++;
		agentPanel.add(new JLabel(" > Porta 4 = " + getSensor(agent.getSensor4())), gbc);
		gbc.gridy++;

		{
			gbc.insets = new Insets(10, 0, 0, 0);
			JLabel label = new JLabel("Percepções recebidas");
			label.setFont(new Font("Serif", Font.BOLD, 16));
			agentPanel.add(label, gbc);
			gbc.insets = new Insets(0, 0, 0, 0);
		}
		gbc.gridy++;
		gbc.gridwidth = 2;
		{
			DefaultListModel listModel = new DefaultListModel();
			JList perceptions = new JList(listModel);
			perceptions.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

			JScrollPane perceptionsView = new JScrollPane(perceptions);
			agentPanel.add(perceptionsView, gbc);
			
			perceptionMapping.put(agent.getAgName(), perceptions);
		}
		gbc.gridy++;
		gbc.gridwidth = 1;

		{
			gbc.insets = new Insets(10, 0, 0, 0);
			JLabel label = new JLabel("Ações executadas");
			label.setFont(new Font("Serif", Font.BOLD, 16));
			agentPanel.add(label, gbc);
			gbc.insets = new Insets(0, 0, 0, 0);
		}
		gbc.gridy++;
		gbc.gridwidth = 2;
		{
			DefaultListModel listModel = new DefaultListModel();
			JList actions = new JList(listModel);
			actions.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

			JScrollPane actionsView = new JScrollPane(actions);
			agentPanel.add(actionsView, gbc);
			
			actionMapping.put(agent.getAgName(), actions);
		}
		gbc.gridwidth = 1;
	}
	
	public void addAction(String agent, String action) {
		((DefaultListModel)actionMapping.get(agent).getModel()).addElement(action);
		actionMapping.get(agent).ensureIndexIsVisible(actionMapping.get(agent).getModel().getSize() - 1);
	}
	
	public void addPerception(String agent, String perception) {
		((DefaultListModel)perceptionMapping.get(agent).getModel()).addElement(perception);
		perceptionMapping.get(agent).ensureIndexIsVisible(perceptionMapping.get(agent).getModel().getSize() - 1);
	}

	private String getSensor(int sensor) {
		String result = "";
		switch(sensor) {
		case Constants.SENSOR_LIGHT:
			result = "Light sensor";
			break;
		case Constants.SENSOR_SOUND:
			result = "Sound sensor";
			break;
		case Constants.SENSOR_TOUCH:
			result = "Touch sensor";
			break;
		case Constants.SENSOR_ULTRA:
			result = "UltraSonic sensor";
			break;
		case Constants.SENSOR_NONE:
			result = "N/A";
			break;
		}
		return result;
	}

}
