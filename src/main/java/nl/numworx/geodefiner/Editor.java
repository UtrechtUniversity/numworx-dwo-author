package nl.numworx.geodefiner;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.JComponent;
import javax.swing.JPanel;

import org.cbook.cbookif.CBookWidgetEditIF;

public class Editor extends JPanel implements CBookWidgetEditIF {

	private Instance instance;
	private DefinitionPanel definition;
	private CommandPanel command;
	Editor() {
		super(new BorderLayout());
		setSize(600,400);
		setPreferredSize(getSize());
		JPanel flow = new JPanel(false);
		instance = new Instance();
		instance.setPreferredSize(instanceSize);
		instance.setSize(instanceSize);
		flow.add(instance);
		add(flow, BorderLayout.CENTER);
		definition = new DefinitionPanel();
		definition.viewer = instance.getViewer();
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
		Map<String,?> launchdata = new TreeMap<String,Object>();
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
	}

	public void setInstanceWidth(int w) {
		instanceSize.width = w;
	}

	public void setLaunchData(Map<String, ?> arg0) {

	}

	public void start() {
		instance.setLaunchData(getLaunchData(), Collections.EMPTY_MAP);
		instance.init();
		instance.repaint();
	}

	public void stop() {
		instance.destroy();
		definition.model.clear();
	}

}
