package fi.wiskopdr.cbook;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.text.html.HTMLDocument;

import org.cbook.cbookif.CBookContext;
import org.cbook.cbookif.CBookWidgetEditIF;
import org.cbook.cbookif.CBookWidgetIF;
import org.cbook.cbookif.Constants;
import org.cbook.cbookif.LessonMode;

import fi.beans.wiskopdrbeans.CBookAware;
import fi.beans.wiskopdrbeans.InteractieEditPanel;
import fi.wiskopdr.ObjectiveChoiceButton;
import fi.wiskopdr.WiskOpdr;
import fi.wiskopdr.formuleobjects.FormuleButton;
import fi.wiskopdr.opdrnav.OpdrNavStruct;

/**
 * Maak van een CBook widget een InteractieEditPanel.
 * Bridge pattern.
 * @author velth101
 *
 */
public class CBookInteractieEditPanel extends JPanel implements
		InteractieEditPanel, CBookContext {

	CBookWidgetEditIF editor;
	Hashtable launchData = new Hashtable();
	Object info;
	
	boolean canLog;
	JCheckBox logging;
	JTextField logid;
	
	JCheckBox teltMee;
	InfoButton   infoBtn;
	String clazzName, instance;
	private JComponent delegate;
	private ObjectiveChoiceButton logObjectivesButton;
	
	
	@Override
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		delegate.addPropertyChangeListener(listener);
	}

	@Override
	public void addPropertyChangeListener(String propertyName,
			PropertyChangeListener listener) {
		delegate.addPropertyChangeListener(propertyName, listener);
	}

	public CBookInteractieEditPanel(CBookWidgetIF widget, Locale locale, String uuid) {
		super(new BorderLayout());
		instance = uuid;
		info = widget;
		clazzName = Service.getClassName(widget);
		setLocale(locale);
		editor = widget.getEditor(this);
		String[] cmds = editor.getSendCmds();

		for (int i = 0; cmds!=null && i < cmds.length; i++) {
			if(WidgetBridge.LOGGING.equals(cmds[i])) {
				canLog = true;
				break;
			}
		}
		delegate = editor.asComponent();
		add(delegate, BorderLayout.CENTER);
/* Hier wordt de rest van de onafhankelijke parameters gezet
 * logid, logging
 * objectives
 * etc...
 */
		JPanel top = new JPanel();
		teltMee = new JCheckBox(WiskOpdr.rb.getString("teltMeeCBLabel"));
		teltMee.setSelected(true);
		top.add(teltMee);
		logging = new JCheckBox(WiskOpdr.rb.getString("logCBLabel"));
		logid = new JTextField();
		logid.setColumns(10);
		if(canLog)
		{  top.add(logging);
			top.add(logid);
		}
// objectives:
	    logObjectivesButton = new ObjectiveChoiceButton(WiskOpdr.objectives, WiskOpdr.categorieString);
        logObjectivesButton.setVisible(WiskOpdr.objectives!=null);
        if(logObjectivesButton.isVisible())
        	top.add(logObjectivesButton);
        infoBtn = new InfoButton(this);
        top.add(infoBtn);
		add(top, BorderLayout.NORTH);
		
	}

	public void setEditState(Hashtable b) {
		launchData = b;
		Map<String, ?> data = (Map<String, ?>) launchData.get(WidgetBridge.LAUNCH_DATA);
		if(data == null) data = Collections.emptyMap();
		data = new HashMap<String, Object>(data); // XXX Voor ESlate!!!!!
		editor.setLaunchData(data);
		if (b.containsKey(WidgetBridge.UUID)) {
			instance = String.valueOf(b.get(WidgetBridge.UUID));
		}
		teltMee.setSelected(!Boolean.FALSE.equals(getProperty(WidgetBridge.TELT_MEE)));
		if(canLog)
		{
			logid.setText(String.valueOf(getProperty(WidgetBridge.LOG_ID)));
			logging.setSelected(Boolean.TRUE.equals(getProperty(WidgetBridge.LOGGING)));
		}
		
		boolean[][] logObjectives = OpdrNavStruct.toBooleanArrayArray(b.get(CBookInteractiePanel.LOG_OBJECTIVES));
        logObjectivesButton.setChoices(logObjectives);

		
	}

	public Hashtable getEditState() {
		launchData = new Hashtable();
		Map<String, ?> data = editor.getLaunchData();
		if(data != null) launchData.put(WidgetBridge.LAUNCH_DATA, data);
// Extra
		int maxScore = editor.getMaxScore();
		if(! teltMee.isSelected())
			maxScore = 0;
		launchData.put(Constants.MAX_SCORE, maxScore);
		launchData.put(WidgetBridge.TELT_MEE, teltMee.isSelected());
		if(canLog)
		{	
			launchData.put(WidgetBridge.LOGGING, logging.isSelected());
			launchData.put(WidgetBridge.LOG_ID, logid.getText());
		}
		if(logObjectivesButton.isVisible())
		{
			launchData.put(CBookInteractiePanel.LOG_OBJECTIVES, logObjectivesButton.getChoices());
		}
		
		return launchData;
	}

	public void zetBreedte(int b) {
		editor.setInstanceWidth(b);
	}

	public void zetHoogte(int h) {
		editor.setInstanceHeight(h);
	}

	public void wis() {
	}

	public void stop() {
		editor.stop();
	}	
	public void start() {
		editor.start();
	}

	public Object getProperty(String key) {
		if(InfoButton.INFO.equals(key)) return info;
		if("locale".equals(key)) return getLocale();
		if(WidgetBridge.UUID.equals(key))
			return WiskOpdr.getUnit_id() + "-" + WiskOpdr.getEditPageNr() + "-" + instance;
//		if(WidgetBridge.RESOURCE_MANAGER.equals(key))
//		{ 
//			return WidgetBridge.getResourceManager(clazzName, WiskOpdr.getEditPageNr() + "/" + instance);
//		}
		if(WidgetBridge.LESSON_MODE.equals(key))
			return LessonMode.editable;
		if(WidgetBridge.PREFERENCES.equals(key))
			return WidgetBridge.getPreferences(clazzName);
		return launchData.get(key);
	}

	public Dimension getInstanceSize() {
		return editor.getInstanceSize();
	}

	String[] getAcceptedCmds() {
		return editor.getAcceptedCmds();
	}

	String[] getSendCmds() {
		return editor.getAcceptedCmds();
	}

	public String getLocalizedCmd(String cmd) {
		try {
			Method method = editor.getClass().getMethod("getLocalizedCmd", String.class);
			return String.valueOf(method.invoke(editor, cmd));			
		} catch (NoSuchMethodException e) {
		} catch (SecurityException e) {
		} catch (IllegalAccessException e) {
		} catch (IllegalArgumentException e) {
		} catch (InvocationTargetException e) {
		}
		
		try {
			String translated = WiskOpdr.rb.getString(CBookAware.CBA_PREFIX + cmd);
			if(!translated.startsWith(CBookAware.CBA_PREFIX)) cmd = translated;
		} catch (Exception e) {
		}
		return cmd;
	}

	public String toString() {
		return editor.toString();
	}
 
}
