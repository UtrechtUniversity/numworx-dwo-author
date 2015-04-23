package fi.wiskopdr.cbook;

import java.awt.AWTEventMulticaster;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JPanel;

import org.cbook.cbookif.AssessmentMode;
import org.cbook.cbookif.CBookContext;
import org.cbook.cbookif.CBookEvent;
import org.cbook.cbookif.CBookEventHandler;
import org.cbook.cbookif.CBookEventListener;
import org.cbook.cbookif.CBookWidgetEditIF;
import org.cbook.cbookif.CBookWidgetIF;
import org.cbook.cbookif.CBookWidgetInstanceIF;
import org.cbook.cbookif.Constants;
import org.cbook.cbookif.LessonMode;
import org.cbook.cbookif.SuccessStatus;

import fi.beans.wiskopdrbeans.CBookAware;
import fi.beans.wiskopdrbeans.InteractieEditPanel;
import fi.beans.wiskopdrbeans.InteractiePanel;
import fi.beans.wiskopdrbeans.ResourceManagerClient;
import fi.wiskopdr.WiskOpdr;
import fi.wiskopdr.opdrnav.OpdrNavStruct;

/**
 * Wrapper om een CBookWidget instance.
 * Maak van een CBook widget een InteractiePanel.
 * Bridge pattern.
 * @author velth101
 *
 */
public class CBookInteractiePanel extends JPanel implements InteractiePanel, CBookContext, CBookAware, ResourceManagerClient {
	
	private final Logger LOGGER = java.util.logging.Logger.getLogger(getClass().getName());
	private CBookWidgetInstanceIF instance;
	private CBookWidgetIF widget;
	private Hashtable launchData;
	private CBookEventHandler handler;
	private String uuid;
	private boolean[][] logObjectives;
	
	public CBookInteractiePanel(CBookWidgetIF widget, String uuid, ResourceManagerFactory factory) {
		super(null);
		setInstanceId(uuid);
		setFactory(factory);
		setLocale(WiskOpdr.language);
		this.widget = widget;
		instance = widget.getInstance(this);

		instance.addCBookEventListener(bridge, WidgetBridge.CHANGED);
		instance.addCBookEventListener(bridge, WidgetBridge.CHECKED);		
		instance.addCBookEventListener(bridge, WidgetBridge.LOGGING);
		handler = new CBookEventHandler(this);

		CBookEventListener l = instance.asEventListener();
		if(l != null) handler.addCBookEventListener(l, null);
			
		JComponent component = instance.asComponent();
		add(component);
		//setSize(component.getPreferredSize());
		component.setLocation(0, 0);
		component.addComponentListener(new ComponentAdapter() {

			@Override
			public void componentResized(ComponentEvent e) {
				e.getComponent().validate();
			}

			@Override
			public void componentShown(ComponentEvent ev) {
				ev.getComponent().validate();
			}});
	}
	

	public void zetOpdracht(Hashtable b, String[] randomVars,
			Hashtable randomValues) {
		this.launchData = b;
		this.logObjectives = OpdrNavStruct.toBooleanArrayArray(b.get(LOG_OBJECTIVES));
		Map m = (Map)b.get(WidgetBridge.LAUNCH_DATA);
		
		if(m == null)
			m = Collections.emptyMap();
		Map instanceLaunchData = new HashMap( m ) ; // XXX voor ESlate, should be collections.unmodifiableMap
// this order? context is valid.
		instance.init();
		instance.setLaunchData(instanceLaunchData, randomValues);
	}

	public void setState(Hashtable b) {
		instance.setState(b);
	}

	public boolean isTeltMee() {
		return !Boolean.FALSE.equals(getProperty(WidgetBridge.TELT_MEE));
	}
	
	@Override
	public void setBounds(int x, int y, int width, int height) {
		super.setBounds(x, y, width, height);
		getComponent(0).setSize(width, height);
	}


	/**
	 * 
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void setEditState(Hashtable b) {
		if(b == null) b = new Hashtable();
		launchData = (b);
		try {
			instance.init();
			Map<String,?> map = (Map<String,?>)launchData.get(WidgetBridge.LAUNCH_DATA);
			map = new HashMap(map);
			instance.setLaunchData(map, Collections.EMPTY_MAP);
		} catch (Exception e) {
			LOGGER.log(Level.WARNING,"setEditState()" , e);
		}
	}

	public Hashtable getState() {
		Hashtable h = new Hashtable();
		try {
			Map hh = instance.getState();
			h.putAll(hh); // conversie?
		} catch (Exception e) {
			LOGGER.log(Level.WARNING,"getState()" , e);
		}
		return h;
	}

	/**
	 * No edit mode in this interactiePanel.
	 * @deprecated not used?
	 */
	public Hashtable getEditState() {
		return launchData;
	}

	public InteractieEditPanel getEditPanel() {
		return getEditor();
	}

	/**
	 * Not used, context not yet valid.
	 * @see #zetOpdracht(Hashtable, String[], Hashtable)
	 */
	public void wis() {
		// instance.init() 
	}

	public void zetMaat() {
	}

	public int geefAsHoogte() {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getIpId() {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getScore() {
		if(isTeltMee())
			return instance.getScore();
		return 0;
	}

	public int[][] getScoreObjectives()
	{
		if (logObjectives == null)
			return null;
		int score = getScore();
		int[][] scoreObjectives = new int[logObjectives.length][];
		for (int i = 0; i < logObjectives.length; i++)
		{	scoreObjectives[i] = new int[logObjectives[i].length];
			for (int j = 0; j < logObjectives[i].length; j++)
			{
				if (logObjectives[i][j])
					scoreObjectives[i][j] = score;
			}
		}
		return scoreObjectives;
	}

	public int getScoreMax() {
		if(isTeltMee())
			return ((Number) launchData.get(Constants.MAX_SCORE)).intValue();
		return 0;
	}

	public boolean isCorrect() {
		return !isTeltMee() || SuccessStatus.PASSED.equals(instance.getSuccessStatus());
	}

	public boolean isFout() {
		return isTeltMee() && SuccessStatus.FAILED.equals(instance.getSuccessStatus());
	}

	public void zetMode(int mode) {
		instance.setAssessmentMode(AssessmentMode.values()[mode]);
	}

	public void zetNagekeken(boolean b) {
		handler.fire(WidgetBridge.CHECK, WidgetBridge.CHECKED, Boolean.valueOf(b));
	}

	public void stop() {
		instance.stop();
	}

	public void start() {
		instance.start();
	}

	public void destroy() {
		instance.destroy();
	}

	public void opnieuw() {
		instance.reset();
	}

	public void kijkNa() { // TODO ??? API?				
		handler.fire(WidgetBridge.CHECK);
	}

	public void kijkNa(int stapNr) { // TODO API?
		kijkNa();
	}

	private ActionListener listener;
	public void addActionListener(ActionListener al) {
		listener = AWTEventMulticaster.add(listener, al);
	}

	final private CBookEventListener bridge = new CBookEventListener() {
		public void acceptCBookEvent(CBookEvent event) {
			String command = event.getCommand();
			if(WidgetBridge.LOGGING.equals(command))
			{
				System.out.println("logging:" + event.getParameters());
				return;
			}		
			if(listener != null) {
				ActionEvent av = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, command);
				listener.actionPerformed(av);
			}
		}
	};
	private CBookInteractieEditPanel cBookInteractieEditPanel;
	private ResourceManagerFactory factory;
	static String LOG_OBJECTIVES = "logObjectives";

	public Object getProperty(String key) {
		if("locale".equals(key)) return getLocale();
		if(WidgetBridge.APPLET_CONTEXT.equals(key))
			return WidgetBridge.getAppletContext();
		if(WidgetBridge.JSOBJECT.equals(key))
			return WidgetBridge.getJSObject();
		if(WidgetBridge.LEARNER_ID.equals(key))
			return WiskOpdr.getLearner_id();
		if(WidgetBridge.LEARNER_NAME.equals(key))
			return WiskOpdr.getLearnerName();
		if(WidgetBridge.BACKGROUND.equals(key))
		{
			Color background = getBackground();
			return background;
		}
		if(WidgetBridge.FOREGROUND.equals(key))
		{
			Color foreground = getForeground();
			return foreground;
		}
		if(WidgetBridge.FONT.equals(key))
		{
			Font font = getFont();
			return font;
		}
		if(WidgetBridge.INSTANCE_SIZE.equals(key))
		{
			return new Dimension( getSize() );
		}
// conversie cbook key naar wiskopdr key
		if(WidgetBridge.UUID.equals(key))
			return WiskOpdr.getUnit_id() + "-" + WiskOpdr.getPageNr() + "-" + getInstanceId();
		if(WidgetBridge.RESOURCE_MANAGER.equals(key))
		{ 
			return factory.getResourceManager();
		}
		if(WidgetBridge.LESSON_MODE.equals(key))
			return WiskOpdr.getLessonMode();
		if(WidgetBridge.PREFERENCES.equals(key))
			return WidgetBridge.getPreferences(getClassName());

		if(launchData != null)
			return launchData.get(key);
		return null;
	}


	public String getInstanceId() {
		return uuid;
	}
	
	public Icon getIcon() {
		return widget.getIcon();
	}


	@Override
	public void acceptCBookEvent(CBookEvent event) {
		CBookEventListener eventListener = instance.asEventListener();
		if(eventListener != null)
			eventListener.acceptCBookEvent(event);
	}

	@Override
	public void addCBookEventListener(CBookEventListener listener,
			String command) {
		instance.addCBookEventListener(listener, command);
	}

	@Override
	public void removeCBookEventListener(CBookEventListener listener,
			String command) {
		instance.removeCBookEventListener(listener, command);
	}

	@Override
	public String[] getSendCmds() {
		return getEditor().getSendCmds();
	}


	@Override
	public String[] getAcceptedCmds() {
		return getEditor().getAcceptedCmds();
	}
	
	private CBookInteractieEditPanel getEditor() {
		if(cBookInteractieEditPanel == null)
			cBookInteractieEditPanel = new CBookInteractieEditPanel(widget, getLocale(), uuid);
		return cBookInteractieEditPanel;
	}
	
	@Override
	public String getLocalizedCmd(String cmd) {
		return getEditor().getLocalizedCmd(cmd);
	}
	
	@Override
	public void setInstanceId(String id) {
		uuid = id;
	}

	@Override
	public String getClassName() {
		return Service.getClassName(widget);
	}

	@Override
	public void setFactory(ResourceManagerFactory factory) {
		this.factory = factory;		
	}

}
