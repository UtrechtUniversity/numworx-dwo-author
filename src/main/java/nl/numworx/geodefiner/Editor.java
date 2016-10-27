package nl.numworx.geodefiner;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import org.cbook.cbookif.CBookContext;
import org.cbook.cbookif.CBookWidgetEditIF;

public class Editor extends JPanel implements CBookWidgetEditIF {

	private Instance instance;
	private DefinitionPanel definition;
	private CommandPanel command;
	private CBookContext context;

	Editor(CBookContext context) {
		super(new BorderLayout());
		this.context = context;
		setSize(700,500);
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
		Map randomvars = (Map) context.getProperty("randomVars");
		instance.setLaunchData(launchdata, randomvars);
	}

	public void start() {
		instance.start();
		
		instance.repaint();
//		command.editor.requestFocus();
//		boolean x = command.editor.requestDefaultFocus();
//		command.editor.addKeyListener(new KeyAdapter() {
//
//			@Override
//			public void keyTyped(KeyEvent e) {
//				// TODO Auto-generated method stub
//				super.keyTyped(e);
//			}
//			
//		});
//		System.out.println(x);
		createKeybindings();
		
	}

	public void stop() {
		instance.stop();
	}
	
	private void createKeybindings() {
		getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "Enter");
		    getActionMap().put("Enter", new AbstractAction() {
		        public void actionPerformed(ActionEvent ae) {
		            //do something on enter pressed
		        	System.out.println("hiero!");
		        }
		    });
		}
}
