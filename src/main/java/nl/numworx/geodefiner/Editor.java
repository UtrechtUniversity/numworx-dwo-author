package nl.numworx.geodefiner;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.inject.Inject;
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
import nl.numworx.geodefiner.common.State;
import nl.numworx.geodefiner.ui.Axes;
import nl.uu.fi.dwo.interaction.client.JSONUtilities;
import nl.uu.fi.dwo.interaction.client.json.ObjectMap;

import org.cbook.cbookif.CBookContext;
import org.cbook.cbookif.CBookWidgetEditIF;

import fi.euclides.event.NameMapper;
import fi.euclides.expr.InterpretException;
import fi.euclides.formuleobjects.ParseException;
import fi.euclides.formuleobjects.Token;
import fi.euclides.formuleobjects.TokenMgrError;
import fi.euclides.model.AbstractViewer;
import fi.euclides.model.Coordinaten;
import fi.euclides.model.Destroyable;
import fi.euclides.model.Punt;
import fi.wiskopdr.TabletOwningLayeredPane;
import fi.wiskopdr.WiskOpdr;
import fi.wiskopdr.formuleobjects.TabletOwner;
import fi.wiskopdr.tekstobjects.FeedbackTekstArea;

/*
// objectives:
111	            logObjectivesButton = new ObjectiveChoiceButton(WiskOpdr.objectives, WiskOpdr.categorieString);
112	        logObjectivesButton.setVisible(WiskOpdr.objectives!=null);
113	        if(logObjectivesButton.isVisible())
114	                top.add(logObjectivesButton);
115	 
seteditstate
              boolean[][] logObjectives = OpdrNavStruct.toBooleanArrayArray(b.get(CBookInteractiePanel.LOG_OBJECTIVES));
140	        logObjectivesButton.setChoices(logObjectives);
geteditstate
              if(logObjectivesButton.isVisible())
161	                {
162	                        launchData.put(CBookInteractiePanel.LOG_OBJECTIVES, logObjectivesButton.getChoices());
163	                }
*/
public class Editor extends TabletOwningLayeredPane implements CBookWidgetEditIF , TabletOwner, ActionListener, PropertyChangeListener {

	private Instance instance;
	private DefinitionPanel definition;
	private RandomPanel random;
	private CheckDWOPanel checkDWO;
	
	void setChoices(boolean[][] choices) {
		checkDWO.setChoices(choices);
	}

	boolean[][] getChoices() {
		return checkDWO.getChoices();
	}

	private CheckObjectsPanel checkObjects;
	private ToolboxPanel toolbox;
	private CommandPanel command;
	private CBookContext context;
	private JPanel content;
	private Axes axes;
	private JTabbedPane tabs;
	private FeedbackTekstArea feedback;
	
	@Inject Editor(final CBookContext context, 
			final CommandPanel command, 
			final Instance instance,
			final DefinitionPanel definition,
			final RandomPanel random,
			final CheckObjectsPanel checkObjects,
			final CheckDWOPanel checkDWO,
			final ToolboxPanel toolbox,
			final Axes axes
			) {
		content = new JPanel(new BorderLayout());
		this.context = context;
		content.setBounds(0,0,800,600);setSize(content.getSize());
		content.setPreferredSize(getSize());
		setPreferredSize(getSize());
		JPanel flow = new JPanel(false);
		flow.setBackground(new Color(250,250,255));
		this.command = command;
		this.instance = instance;
		instance.installToolTip();
		JComponent component = instance.asComponent();
		component.setPreferredSize(instanceSize);
		component.setSize(instanceSize);
		component.setMaximumSize(instanceSize);
		component.setMinimumSize(instanceSize);
		component.setBorder(BorderFactory.createEtchedBorder());
		flow.add(component);
		tabs = new JTabbedPane();
		split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, new JScrollPane(flow), tabs);
		//split.setDividerLocation(0.7);
		content.add(split, BorderLayout.CENTER);
		this.definition = definition;
		this.checkDWO = checkDWO;
		this.checkObjects = checkObjects;
		this.toolbox = toolbox;
		this.random = random;
		tabs.addTab(definition.getName(), null, definition, definition.getToolTipText());
		tabs.addTab(checkDWO.getName(), null, checkDWO, checkDWO.getToolTipText());
		tabs.addTab(checkObjects.getName(), null, checkObjects, checkObjects.getToolTipText());
		tabs.addTab(toolbox.getName(), null, toolbox, toolbox.getToolTipText());
		tabs.addTab(random.getName(), null, random, random.getToolTipText());

// inject
		//definition.randomizer = command;
		//instance.randomizer = command;
		//checkObjects.randomizer = command;
//		toolbox.viewer = instance.getViewer();
//		toolbox.setToolbox(instance.toolbox, instance);
//		checkDWO.checkBtn = instance.checkBtn;
//		checkDWO.validator = component;
//		checkDWO.setTracker(instance.getViewer());
// 
		command.addPropertyChangeListener("command", definition);
		command.addPropertyChangeListener("config", definition);
		command.addPropertyChangeListener("feedback", this);
		checkObjects.addPropertyChangeListener("feedback", this);
		definition.addPropertyChangeListener("command", command);
		checkDWO.addPropertyChangeListener("feedback", this);
		content.add(command, BorderLayout.SOUTH);
		add(content, JLayeredPane.DEFAULT_LAYER);
		this.axes = axes;
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
			if(cell.item == null) // Komt voor?
				continue;
			strings.add(cell.text);
			if (cell.config != null) {
				configuration.put(viewer.toString(cell.item), cell.config.toMap());
			}
		}
		launchdata.put("definitions", strings);
		launchdata.put("order", definition.toList());
		launchdata.put("configuration", configuration);
		launchdata.put("axes", axes.toMap());
		instance.installPrepare(); // voor instance.getstate().get("model")
		launchdata.put("positions", filterCoordinaten((Map)instance.getState().get("positions")));
		launchdata.put("random", random.getText());
		launchdata.put("checkDWO", checkDWO.toMap());
		launchdata.put("toolbox", toolbox.toList());
		launchdata.put("checkObjects", checkObjects.toList());
		int div = split.getDividerLocation();
		if(div>0)launchdata.put("split", div);
		launchdata.put("command", command.toString());
		if (checkDWO.isLogOption()) {
		  launchdata.put("logOption", Boolean.TRUE);
		  launchdata.put("logID", checkDWO.getLogID());
		}
		
		return launchdata;
	}

	private Map filterCoordinaten(Map map) {
		NameMapper mapper = instance.getViewer().getMapper();
		for(Punt p : instance.getViewer().getModel().getPunten()) {
			if( State.INITIAL.equals(p.adapt(State.class))) {
				map.remove(mapper.toString(p));
			}
		}
		for(Destroyable p : instance.getViewer().getModel().getLijnen()) {
			if( State.INITIAL.equals(p.adapt(State.class))) {
				map.remove(mapper.toString(p));
			}
		}
		return map;
	}

	public String getLocalizedCmd(String cmd) {
		return cmd;
	}

	public int getMaxScore() {
		return checkDWO.getMaxScore() + checkObjects.getMaxScore();
	}

	public String[] getSendCmds() {
		return null;
	}

	public void setInstanceHeight(int h) {
		instanceSize.height = h;
		JComponent c = instance.asComponent();
		c.setSize(instanceSize);
		c.setPreferredSize(instanceSize);
		c.setMaximumSize(instanceSize);
		c.setMinimumSize(instanceSize);
		c.invalidate();
		c.getParent().validate();
	}

	public void setInstanceWidth(int w) {
		instanceSize.width = w;
		JComponent c = instance.asComponent();
		c.setSize(instanceSize);
		c.setPreferredSize(instanceSize);
		c.setMaximumSize(instanceSize);
		c.setMinimumSize(instanceSize);
		c.invalidate();
		c.getParent().validate();
	}

	public void setLaunchData(Map<String, ?> launchdata) {
		instance.init();
		random.setText((String)launchdata.get("random"));
		instance.setLaunchData(launchdata, random);
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
		else split.setDividerLocation((int)(0.7*getWidth())); // default waarde
		if(map.containsKey("command"))
			command.fromString(map.getString("command"));
		if(map.containsKey("order"))
			definition.fromList(map.getStringList("order"));
		if (map.containsKey("logOption")) 
		  checkDWO.setLogOption(map.getBoolean("logOption"));
		if (map.containsKey("logID"))
		  checkDWO.setLogID(map.getString("logID"));
		
		
	}

	public void start() {
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

	void setFeedback(String tekst, Object object) {
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
		}
		int x; int y;
		Component f = command;
		if (object instanceof Component) f = (Component) object;
		x = f.getLocationOnScreen().x + 10 - content.getLocationOnScreen().x;
		y = f.getLocationOnScreen().y - 10 - content.getLocationOnScreen().y;
		feedback.setLocation(x, y);
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
			} else if (t instanceof TokenMgrError) {
				TokenMgrError tme = (TokenMgrError) t;
				command += "\n" + tme.getLocalizedMessage();
				
			}
			setFeedback(command, evt.getSource());
		}
	}
}
