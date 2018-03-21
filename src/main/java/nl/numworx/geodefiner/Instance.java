package nl.numworx.geodefiner;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.Stroke;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.ToolTipManager;

import nl.numworx.geodefiner.common.CheckObjectList;
import nl.numworx.geodefiner.common.Check_DWO;
import nl.numworx.geodefiner.common.DefaultRandomizer;
import nl.numworx.geodefiner.common.Randomizer;
import nl.numworx.geodefiner.common.locus.Builder;
import nl.numworx.geodefiner.module.Components;
import nl.numworx.geodefiner.module.DaggerComponents;
import nl.numworx.geodefiner.ui.TextModel;
import nl.numworx.geodefiner.ui.UIModelFactory;
import nl.numworx.geodefiner.ui.UserConfig;
import nl.uu.fi.dwo.interaction.client.json.ObjectList;

import org.cbook.cbookif.AssessmentMode;
import org.cbook.cbookif.CBookEvent;
import org.cbook.cbookif.CBookEventHandler;
import org.cbook.cbookif.CBookEventListener;
import org.cbook.cbookif.CBookWidgetInstanceIF;
import org.cbook.cbookif.Constants;
import org.cbook.cbookif.SuccessStatus;

import dagger.Lazy;
import fi.euclides.model.Destroyable;
import fi.euclides.model.Label;
import fi.euclides.model.Locus;
import fi.euclides.model.PuntenLijn;
import fi.euclides.model.math.Numbers;
import fi.euclides.proof.Const;
import fi.euclides.proof.LabelDelegate;
import fi.euclides.swing.HitTester2;
import fi.euclides.util.Observable;
import fi.euclides.util.Observer;
import fi.wiskopdr.WiskOpdr;
import fi.wiskopdr.formuleobjects.FormuleParser;


public class Instance extends nl.numworx.geodefiner.common.Instance implements CBookWidgetInstanceIF, CBookEventListener, PropertyChangeListener  {

	static final Stroke DEFAULT_STROKE = new BasicStroke();

	
	private JPanel panel = new JPanel(new BorderLayout());
	JPanel south = new JPanel(new FlowLayout(FlowLayout.TRAILING, 2, 2));
	public final JButton checkBtn = new JButton(Messages.getString("kijkNa"));
	JLabel  checkLabel = new JLabel();

	@Inject CBookEventHandler handler;
	@Inject Provider<KijkNaAction> actionProvider;
	@Inject void setViewer(InstanceViewer viewer) {
		this.viewer = viewer;
	}
	@Inject void setCheckObjects(CheckObjectList checkObjects) {
		this.checkObjects = checkObjects;
	}
	@Inject void setDefinitions(Definitions definitions) {
		this.definitions = definitions;
	}
	@Inject void setUiModelFactory(UIModelFactory factory) {
		this.uiModelFactory = factory;
	}
	
	
	void inject(WiskOpdrRandomizer r)
	{
		random = r;
		Components components = DaggerComponents.builder()
				.instance(this)
				.randomizer(r)
				.build();
		components.inject(this);
		
		//viewer = new InstanceViewer(components.getNameMapper(), components.getExpression(), r);
		//checkObjects = new CheckObjectList(viewer); // Inject
		//checkObjects.setInstance(this);
		//definitions = new Definitions(viewer);
		//uiModelFactory = new UIModelFactory(viewer);
	}
	static {
		Locus.BUILDER = new Builder();
	}
	
	public Definitions getDefinitions() {
		return (Definitions) definitions;
	}

	public InstanceViewer getViewer() {
		return (InstanceViewer) viewer;
	}

	JToolBar toolbox;

	private KijkNaAction action;

	private AssessmentMode mode;
	
	public Instance(WiskOpdrRandomizer command) {
		inject(command);
		//content.setBackground(Color.white);
		panel.setOpaque(false);panel.setBackground(null);
		//content.setBorder(BorderFactory.createEtchedBorder());
		selector.setTracker(viewer);
		panel.add(getViewer().content, BorderLayout.CENTER);
		toolbox = new JToolBar();
		toolbox.setFloatable(false);
		toolbox.setVisible(false);
		panel.add(toolbox, BorderLayout.NORTH);
		checkBtn.setVisible(false);
		checkLabel.setVisible(false);
		getViewer().statusLabel.setVisible(false);
		south.add(checkBtn);
		south.add(checkLabel);
		south.setOpaque(false);
		if(true ||GeoDefiner.isExperimental) {
			JPanel south2 = new JPanel(new BorderLayout());
			south2.add(south, BorderLayout.LINE_END);
			south2.add(getViewer().statusLabel, BorderLayout.CENTER);
			south2.setOpaque(false);
			panel.add(south2, BorderLayout.SOUTH);
		} else 
			panel.add(south, BorderLayout.SOUTH);
	}

	public Instance() {
		this(new WiskOpdrRandomizer(new HashMap<String, Number>()));
	}

	public void addCBookEventListener(CBookEventListener listener, final String command) {
		handler.addCBookEventListener(listener, command);
		// command is double.NAAM
		if(command != null && command.startsWith("double.")) {
			int dot = command.indexOf('.');
			final String name = command.substring(dot+1);
			Destroyable f = viewer.getMapper().fromString(name);
			if(f == null) {
				System.err.println("addCBookEventListener " + command + " not found");
				return; 
			}
			final Label label = (Label) f;
			f.addObserver(new Observer() {

				public void update(Observable observable, Object arg) {
					if(arg == null)
					{	Map<String,Object> map = new TreeMap<String,Object>();
						map.put("value", label.value.doubleValue());
						map.put("name", name);
						handler.fire(command, map);
					}
				}});
		}
	}

	public JComponent asComponent() {
		return panel;
	}

	public CBookEventListener asEventListener() {
		return this;
	}

	public void destroy() {
		checkObjects.destroyAll();
		getViewer().getModel().destroyAll();
	}

	
	public Map<String,?> getState() {
		if(mode == AssessmentMode.EINDTOETS) fetchScore(); // no feedback!
		return getState(new Hashtable<String, Object>());
	}
	
	public SuccessStatus getSuccessStatus() {
		Boolean status = getStatus();
		if(status == null) return SuccessStatus.UNKNOWN;
		if(status)
			return SuccessStatus.PASSED;
		return SuccessStatus.FAILED;
	}

	@Override
	public void setLaunchData(Map<String, ? extends Object> launchData,
			Map<String, Number> random) {		
		super.setLaunchData(launchData, random);
	}

	// Assume getSize() is okay.
	public void init() {
		checkBtn.setVisible(false);checkBtn.invalidate();
		checkLabel.setVisible(false);checkLabel.invalidate();
		toolbox.setVisible(false);
		panel.doLayout();
		checkObjects.destroyAll();
		InstanceViewer view = getViewer();
		JComponent content = view.content;
		createModel(viewer.getModel(), content.getWidth(), content.getHeight());
		view.height = content.getHeight();
		view.width = content.getWidth();
		view.offX = view.offY = 0;
		selector.command();
		definitions.clear();
	}

	public void removeCBookEventListener(CBookEventListener listener, String command) {
		handler.removeCBookEventListener(listener, command);
	}

	public void reset() {
		stop();
		destroy();
		init();
		installLaunchData();
		start();
	}

	public void setAssessmentMode(AssessmentMode mode) {
		this.mode = mode;
		if(mode == AssessmentMode.ZELFTOETS||mode == AssessmentMode.EINDTOETS) checkBtn.setVisible(false);
	}

	public void stop() {
		checkObjects.stop();
		started = false;
	}

	@Override
	public void setState(Map<String, ?> state) {
		startToolbox();
		panel.validate();
		checkObjects.start();
		super.setState(state);
		observeNewItems(UserConfig.INSTANCE, new CheckObjectList.CheckVisitor(checkObjects, viewer.getModel()));
		if(isNagekeken() && action != null)
		{
			fetchScore();
			action.feedback();
		}
	}

	public void acceptCBookEvent(CBookEvent ev) {
		if(Constants.CHECK.equals(ev.getCommand()) && action != null)
		{
			action.actionPerformed(null); 
			return;
		}
		
		if(ev.getCommand().startsWith("double.")) {
			int dot = ev.getCommand().indexOf('.');
			String name = ev.getCommand().substring(dot+1);
			Number number = (Number)ev.getParameter("value");
			String message = ev.getMessage();
			Numbers value;
			if(number == null) {
				number = Double.valueOf(message);
				value = Numbers.createDouble(number.doubleValue());
			} else {
				value = Numbers.createDouble(number.doubleValue());
				message = Numbers.toString(value);
			}
			Label label = (Label) getViewer().getMapper().fromString(name);
			if(label.getSubKey() == Const.TYPE) { 
				label.setString(message);
				label.setValue(value);
				label.notifyObservers();
			}
		}
	}


	@Override
	protected boolean installCheckDWO() {
		if  (super.installCheckDWO())
		{
			if(action == null)
			{	action = actionProvider.get();
				checkBtn.addActionListener(action);
				action.addPropertyChangeListener(this);
			}
			checkBtn.setVisible(!checkDWO.isExtern());
			checkLabel.setIcon(action);
			checkLabel.setVisible(true);
			checkDWO.addObserver(action);
			action.update(checkDWO, null);
			checkBtn.invalidate();
			checkLabel.invalidate();
			panel.validate();
			return true;
		}
		checkDWO = new Check_DWO(viewer); // dummy
		panel.validate();
		return false;
	}

	@Override
	public void update(Observable observable, Object arg) {
		super.update(observable, arg);
		if(Constants.CHANGED.equals(arg)) {
			Map<String, ?> parameters = Collections.emptyMap();
			handler.fire(Constants.CHANGED, parameters);
		}
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if(Action.LARGE_ICON_KEY.equals(evt.getPropertyName())) checkLabel.repaint();
	}

	@Override
	public void start() {
		startToolbox();
		super.start();
		getDefinitions().readonly = viewer.getModel().getIndex();
		if(checkObjects != null)
			checkObjects.start();
		getViewer().getModel().addObserver(UserConfig.INSTANCE);
	}

	@Inject Lazy<ToolboxPanel> toolboxPanel;
	boolean started;
	void startToolbox() {
		if(started) return;
		started = true;
		ObjectList tools = launchData.getObjectList("toolbox");
		JLabel statusLabel = getViewer().statusLabel;
		statusLabel.setVisible(tools != null && tools.size() != 0);
		if(statusLabel.isVisible())
		{ 	ToolboxPanel p = toolboxPanel.get();
			p.setToolbox(toolbox);
			p.fromList(tools);
		} else {
			toolbox.removeAll();
			toolbox.setVisible(false);
		}		
		statusLabel.getParent().setVisible(statusLabel.isVisible()||checkBtn.isVisible()||checkLabel.isVisible());
	}	

	void installToolTip() {
		ToolTipManager.sharedInstance().registerComponent(getViewer().content);
	}

	@Override
	public void setNagekeken(boolean nagekeken) {
		if(!nagekeken&&action !=null)
			action.nofeedback();
		super.setNagekeken(nagekeken);
	}

	
}
