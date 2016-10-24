package nl.numworx.geodefiner;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.JComponent;
import javax.swing.JPanel;

import org.cbook.cbookif.CBookContext;
import org.cbook.cbookif.CBookWidgetEditIF;

public class Editor extends JPanel implements CBookWidgetEditIF {

	private Instance instance;
	private DefinitionPanel definition;
	private CommandPanel command;
	Editor(CBookContext context) {
		super(new BorderLayout());
		setSize(600,400);
		setPreferredSize(getSize());
		JPanel flow = new JPanel(false);
		instance = new Instance();
		instance.setPreferredSize(instanceSize);
		instance.setSize(instanceSize);
		flow.add(instance);
		add(flow, BorderLayout.CENTER);
		definition = new DefinitionPanel(instance.getDefinitions());
		add(definition, BorderLayout.EAST);
		command = new CommandPanel();
		command.addPropertyChangeListener("command", definition);
		add(command, BorderLayout.SOUTH);
	}

	private Dimension instanceSize = new Dimension(200,200);

	public JComponent asComponent() {
		return this;
	}

	public String[] getAcceptedCmds() {
		return null;
	}

	public Dimension getInstanceSize() {
		return instanceSize;
	}

	public Map<String, ?> getLaunchData() {
		Map<String,Object> launchdata = new TreeMap<String,Object>();
		List<String> strings = new ArrayList<String>();
		Definitions defs = instance.getDefinitions();
		Enumeration<CELL> e= defs.elements();
		while (e.hasMoreElements()) {
			CELL cell = e.nextElement();
			strings.add(cell.text);
		}
		launchdata.put("definitions", strings);
		return launchdata;
	}

	public String getLocalizedCmd(String cmd) {
		return cmd;
	}

	public int getMaxScore() {
		return 0;
	}

	public String[] getSendCmds() {
		return null;
	}

	public void setInstanceHeight(int h) {
		instanceSize.height = h;
		instance.setSize(instanceSize);
		instance.setPreferredSize(instanceSize);
		instance.invalidate();
	}

	public void setInstanceWidth(int w) {
		instanceSize.width = w;
		instance.setSize(instanceSize);
		instance.setPreferredSize(instanceSize);
		instance.invalidate();
	}

	public void setLaunchData(Map<String, ?> launchdata) {
		instance.init();
		instance.setLaunchData(launchdata, Collections.EMPTY_MAP);
	}

	public void start() {
		instance.start();
		
		instance.repaint();
	}

	public void stop() {
		instance.stop();
	}

}
