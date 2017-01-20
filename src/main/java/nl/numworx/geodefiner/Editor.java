package nl.numworx.geodefiner;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.KeyStroke;

import nl.numworx.geodefiner.common.CELL;
import nl.numworx.geodefiner.ui.Axes;
import nl.uu.fi.dwo.interaction.client.JSONUtilities;
import nl.uu.fi.dwo.interaction.client.json.ObjectMap;

import org.cbook.cbookif.CBookContext;
import org.cbook.cbookif.CBookWidgetEditIF;

import fi.euclides.expr.InterpretException;
import fi.euclides.formuleobjects.ParseException;
import fi.euclides.formuleobjects.Token;
import fi.euclides.model.AbstractViewer;
import fi.wiskopdr.TabletOwningLayeredPane;
import fi.wiskopdr.WiskOpdr;
import fi.wiskopdr.formuleobjects.FormuleVakHouder;
import fi.wiskopdr.formuleobjects.Tablet;
import fi.wiskopdr.formuleobjects.TabletOwner;
import fi.wiskopdr.tekstobjects.FeedbackTekstArea;

public class Editor extends TabletOwningLayeredPane implements CBookWidgetEditIF , TabletOwner, ActionListener, PropertyChangeListener {

	private Instance instance;
	private DefinitionPanel definition;
	private RandomPanel random;
	private CheckDWOPanel checkDWO;
	private CheckObjectsPanel checkObjects;
	private ToolboxPanel toolbox;
	private CommandPanel command;
	private CBookContext context;
	private JPanel content;
	private Axes axes;
	private JTabbedPane tabs;
	private FeedbackTekstArea feedback;
	
	Editor(CBookContext context) {
		content = new JPanel(new BorderLayout());
		this.context = context;
		content.setBounds(0,0,800,600);setSize(content.getSize());
		content.setPreferredSize(getSize());
		setPreferredSize(getSize());
		JPanel flow = new JPanel(false);
		flow.setBackground(new Color(250,250,255));
		instance = new Instance();
		JComponent component = instance.asComponent();
		component.setPreferredSize(instanceSize);
		component.setSize(instanceSize);
		component.setBorder(BorderFactory.createEtchedBorder());
		flow.add(component);
		tabs = new JTabbedPane();
		split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, new JScrollPane(flow), tabs);
		split.setDividerLocation(0.7);
		content.add(split, BorderLayout.CENTER);
		definition = new DefinitionPanel(instance.getDefinitions(), instance.getViewer());
		checkDWO = new CheckDWOPanel();
		checkObjects = new CheckObjectsPanel(instance.getViewer());
		toolbox = new ToolboxPanel();
		random = new RandomPanel();
		tabs.addTab(definition.getName(), null, definition, definition.getToolTipText());
		tabs.addTab(checkDWO.getName(), null, checkDWO, checkDWO.getToolTipText());
		tabs.addTab(checkObjects.getName(), null, checkObjects, checkObjects.getToolTipText());
		tabs.addTab(toolbox.getName(), null, toolbox, toolbox.getToolTipText());
		tabs.addTab(random.getName(), null, random, random.getToolTipText());

		command = new CommandPanel();
// inject
		command.random = random.getRandomVars();
		command.instance = instance;
		definition.randomizer = command;
		toolbox.viewer = instance.getViewer();
		toolbox.setToolbox(instance.toolbox);
		checkDWO.checkBtn = instance.checkBtn;
		checkDWO.validator = component;
// 
		command.addPropertyChangeListener("command", definition);
		command.addPropertyChangeListener("feedback", this);
		definition.addPropertyChangeListener("command", command);
		content.add(command, BorderLayout.SOUTH);
		add(content, JLayeredPane.DEFAULT_LAYER);
		axes = new Axes(instance.getViewer());
		tabs.addTab("Axes", null, axes, null);
	}

	@Override
	public void setBounds(int x, int y, int width, int height) {
		super.setBounds(x, y, width, height);
		content.setBounds(0,0,width, height); // contentlayer follows size
		content.invalidate(); 
	}

	private Dimension instanceSize = new Dimension(200,200);
	private JSplitPane split;
	
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
		Map<String,Object> configuration = new TreeMap<String,Object>();
		Definitions defs = instance.getDefinitions();
		AbstractViewer viewer = instance.getViewer();
		Iterator<CELL> e= defs.elements();
		while (e.hasNext()) {
			CELL cell = e.next();
			strings.add(cell.text);
			if (cell.config != null) {
				configuration.put(viewer.toString(cell.item), cell.config.toMap());
			}
		}
		launchdata.put("definitions", strings);
		launchdata.put("configuration", configuration);
		launchdata.put("axes", axes.toMap());
		launchdata.put("positions", instance.getState().get("positions"));
		launchdata.put("random", random.getText());
		launchdata.put("checkDWO", checkDWO.toMap());
		launchdata.put("toolbox", toolbox.toList());
		launchdata.put("checkObjects", checkObjects.toList());
		launchdata.put("split", split.getDividerLocation());
		launchdata.put("command", command.toString());
		return launchdata;
	}

	public String getLocalizedCmd(String cmd) {
		return cmd;
	}

	public int getMaxScore() {
		return checkDWO.getMaxScore();
	}

	public String[] getSendCmds() {
		return null;
	}

	public void setInstanceHeight(int h) {
		instanceSize.height = h;
		JComponent c = instance.asComponent();
		c.setSize(instanceSize);
		c.setPreferredSize(instanceSize);
		c.invalidate();
		c.getParent().validate();
	}

	public void setInstanceWidth(int w) {
		instanceSize.width = w;
		JComponent c = instance.asComponent();
		c.setSize(instanceSize);
		c.setPreferredSize(instanceSize);
		c.invalidate();
	}

	public void setLaunchData(Map<String, ?> launchdata) {
		instance.init();
		random.setText((String)launchdata.get("random"));
		Map randomvars = random.getRandomVars();
		instance.setLaunchData(launchdata, randomvars);
		axes.init();
// fill checkDWO for the editor		
		ObjectMap map = JSONUtilities.wrapMap(launchdata);
		if(map.containsKey("checkDWO"))
				checkDWO.fromMap(map.getObjectMap("checkDWO"));
		if(map.containsKey("toolbox"))
				toolbox.fromList(map.getObjectList("toolbox"));
		if(map.containsKey("checkObjects")) 
				checkObjects.fromList(map.getObjectList("checkObjects"));
		if(map.containsKey("split"))
			split.setDividerLocation(map.getInt("split"));
		if(map.containsKey("command"))
			command.fromString(map.getString("command"));
	}

	public void start() {
		//instance.start();		
		instance.asComponent().repaint();
		createKeybindings();
		axes.init();
	}

	public void stop() {
		instance.stop();
	}
	
	private void createKeybindings() {
		getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "Enter");
		    getActionMap().put("Enter", new AbstractAction() {
		        public void actionPerformed(ActionEvent ae) {
		            //do something on enter pressed
		        	//System.out.println("hiero!");
		        }
		    });
		}
	
	@Override
	public void paint(Graphics g) {
		// TODO Auto-generated method stub
		super.paint(g);
	}

	void setFeedback(String tekst) {
		if(feedback == null) {
			feedback = new FeedbackTekstArea();
			feedback.setSize(200,40);
			if(true) {	
				feedback.setBackground(new Color(255,255,200));
				if("MW".equals(WiskOpdr.deployVariant))feedback.setBackground(new Color(250,255,220));
				if("GR".equals(WiskOpdr.deployVariant))feedback.setBackground(new Color(255,255,255));
				feedback.setBorders(true);
			}
			feedback.setCloseable(true);
			feedback.addActionListener(this);
			feedback.setVisible(false);
			add(feedback, JLayeredPane.PALETTE_LAYER);
			int x; int y;
			x = command.getX() + 10;
			y = command.getY() - 10;
			feedback.setLocation(x, y);
		}
		feedback.setText(tekst);
		feedback.setVisible(true);
	}

	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == feedback) {
			feedback.setVisible(false);
			command.requestFocus();
		}

	}

	public void propertyChange(PropertyChangeEvent evt) {
		if("feedback".equals(evt.getPropertyName())) {
			String command = evt.getOldValue().toString();
			Object t = evt.getNewValue();
			if(t instanceof ParseException) {
				ParseException pe = (ParseException)t;
				int position = pe.currentToken.beginColumn;
				if(pe.currentToken == null) pe.currentToken = new Token(0, "start");
				command += "\nSyntax fout na " + pe.currentToken + " (positie " + position + ")"; 
			} else if (t instanceof InterpretException) {
				InterpretException ie = (InterpretException) t;
				command += "\n" + ie.getLocalizedMessage();
			}
			setFeedback(command);
		}
	}
}
