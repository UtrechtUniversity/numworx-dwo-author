package nl.numworx.geodefiner;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.Stroke;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.inject.Inject;
import javax.inject.Named;
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
import nl.numworx.geodefiner.common.LocusModelFX;
import nl.numworx.geodefiner.common.Randomizer;
import nl.numworx.geodefiner.common.locus.Builder;
import nl.numworx.geodefiner.module.Components;
import nl.numworx.geodefiner.module.DaggerComponents;
import nl.numworx.geodefiner.ui.TextModel;
import nl.numworx.geodefiner.ui.UIModelFactory;
import nl.numworx.geodefiner.ui.UserConfig;
import nl.uu.fi.dwo.interaction.client.json.ObjectList;
import nl.uu.fi.dwo.interaction.client.json.ObjectMap;

import org.cbook.cbookif.AssessmentMode;
import org.cbook.cbookif.CBookEvent;
import org.cbook.cbookif.CBookEventHandler;
import org.cbook.cbookif.CBookEventListener;
import org.cbook.cbookif.CBookWidgetInstanceIF;
import org.cbook.cbookif.Constants;
import org.cbook.cbookif.LessonMode;
import org.cbook.cbookif.SuccessStatus;

import dagger.Lazy;
import fi.euclides.model.Destroyable;
import fi.euclides.model.Label;
import fi.euclides.model.Locus;
import fi.euclides.model.Model;
import fi.euclides.model.Punt;
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
	public final JButton logBtn = new JButton(); 
	JLabel  checkLabel = new JLabel();

	@Inject CBookEventHandler handler;
	@Inject Provider<KijkNaAction> actionProvider;
	@Inject Lazy<LogAction> logProvider;
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
	
	
	void inject(WiskOpdrRandomizer r, JToolBar toolbar)
	{
		random = r;
		toolbox = toolbar;
		Components.Builder builder = DaggerComponents.builder();
		Components components = builder
				.instance(this)
				.randomizer(r)
				.toolbox(toolbar)
				.build();
		components.inject(this);
		
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
	
	public Instance(WiskOpdrRandomizer command, JToolBar toolbar) {
		inject(command, toolbar);
		//content.setBackground(Color.white);
		panel.setOpaque(false);panel.setBackground(null);
		//content.setBorder(BorderFactory.createEtchedBorder());
		selector.setTracker(viewer);
		panel.add(getViewer().content, BorderLayout.CENTER);
		toolbox.setFloatable(false);
		toolbox.setVisible(false);
		panel.add(toolbox, BorderLayout.NORTH);
		checkBtn.setVisible(false);
		checkLabel.setVisible(false);
		logBtn.setVisible(false);
		getViewer().statusLabel.setVisible(false);
		south.add(logBtn);
		south.add(checkBtn);
		south.add(checkLabel);
		south.setOpaque(false);
		{
			JPanel south2 = new JPanel(new BorderLayout());
			south2.add(south, BorderLayout.LINE_END);
			south2.add(getViewer().statusLabel, BorderLayout.CENTER);
			south2.setOpaque(false);
			panel.add(south2, BorderLayout.SOUTH);
		}
		if(GeoDefiner.isExperimental) {
		  
		}
	}

	public Instance() {
		this(new WiskOpdrRandomizer(new HashMap<String, Number>()), new JToolBar());
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
        definitions.readonly = viewer.getModel().getIndex(); // readonly moet gezet na init definitions, niet idempotent, na of voor setState
	}
	
	/**
	 * For Editor only.
	 * Don't set readonly to a high value
	 */
	void setLaunchData(Map<String, ? extends Object> launchData, RandomPanel random) {
	    super.setLaunchData(launchData, random.getRandomVars());
	}

	// Assume getSize() is okay.
	public void init() {
		checkBtn.setVisible(false);checkBtn.invalidate();
		checkLabel.setVisible(false);checkLabel.invalidate();
		toolbox.setVisible(false); started = false;
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
		expressions.clear();
	    selector.destroyContext(view);
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
	
	LessonMode lessonMode = LessonMode.normal;
	
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
		if (checkDWO.isLogOption() && lessonMode == LessonMode.review) {
			
			logProvider.get().setLogState(state);
			
		}
	}

	
	
	
	
	@Override
  protected void setModelState(ObjectList list, ObjectList toolbox) {
    super.setModelState(list, toolbox);
  }

  @Inject void setExpressions(@Named("expressions") Map<String,String> map) {
    expressions = map;
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
			return;
		}
		if (ev.getCommand().startsWith("expression.") ) {
          int dot = ev.getCommand().indexOf('.');
          String name = ev.getCommand().substring(dot+1);
          String expr = ev.getMessage(); 
          acceptExpressionEvent(name, expr);
          return;
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
			if (checkDWO.isLogOption() && lessonMode == LessonMode.review)
			{
			  logBtn.setAction(logProvider.get());
	          logProvider.get().setLogID(checkDWO.getLogID());
			  logBtn.setVisible(true);
			  logBtn.invalidate();
			}
			checkLabel.setIcon(action);
			checkLabel.setVisible(true);
			checkDWO.addObserver(action);
			action.update(checkDWO, null);
			checkBtn.invalidate();
			checkLabel.invalidate();
			panel.validate();
			return true;
		}
		if (checkDWO == null) 
		  checkDWO = new Check_DWO(viewer); // dummy
        if (checkDWO.isLogOption() && lessonMode == LessonMode.review)
        {
          logProvider.get().setLogID(checkDWO.getLogID());
          logBtn.setAction(logProvider.get());
          logBtn.setVisible(true);
          logBtn.invalidate();
        }
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
		{ 	ToolboxPanel p = getToolboxPanel();
			p.fromList(tools);
		} else {
			toolbox.removeAll();
			toolbox.setVisible(false);
		}		
		statusLabel.getParent().setVisible(statusLabel.isVisible()||checkBtn.isVisible()||checkLabel.isVisible()||logBtn.isVisible());
	}

	public ToolboxPanel getToolboxPanel() {
		return toolboxPanel.get();
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
