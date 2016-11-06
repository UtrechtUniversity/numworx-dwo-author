package nl.numworx.geodefiner;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
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
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.KeyStroke;

import nl.numworx.geodefiner.common.CELL;
import nl.numworx.geodefiner.ui.Axes;

import org.cbook.cbookif.CBookContext;
import org.cbook.cbookif.CBookWidgetEditIF;

import fi.euclides.model.AbstractViewer;
import fi.wiskopdr.formuleobjects.FormuleVakHouder;
import fi.wiskopdr.formuleobjects.Tablet;
import fi.wiskopdr.formuleobjects.TabletOwner;

public class Editor extends JLayeredPane implements CBookWidgetEditIF , TabletOwner {

	private Instance instance;
	private DefinitionPanel definition;
	private CommandPanel command;
	private CBookContext context;
	private JPanel content;
	private Axes axes;
	private JTabbedPane tabs;
	
	Editor(CBookContext context) {
		super();
		content = new JPanel(new BorderLayout());
		this.context = context;
		content.setBounds(0,0,700,500);setSize(content.getSize());
		content.setPreferredSize(getSize());
		setPreferredSize(getSize());
		JPanel flow = new JPanel(false);
		instance = new Instance();
		instance.setPreferredSize(instanceSize);
		instance.setSize(instanceSize);
		flow.add(instance);
		content.add(flow, BorderLayout.CENTER);
		tabs = new JTabbedPane();
		definition = new DefinitionPanel(instance.getDefinitions(), instance.getViewer());
		tabs.addTab("Objects", null, definition, null);
		content.add(tabs, BorderLayout.EAST);
		command = new CommandPanel();
// inject
		command.random = (Map<String, Number>) context.getProperty("randomVars");
		command.instance = instance;

		command.addPropertyChangeListener("command", definition);
		content.add(command, BorderLayout.SOUTH);
		add(content, JLayeredPane.DEFAULT_LAYER);
		axes = new Axes(instance.getViewer());
		tabs.addTab("Axes", null, axes, null);
	}

	private Dimension instanceSize = new Dimension(200,200);
	private Tablet tablet;
	private FormuleVakHouder tabletUser;

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
		Enumeration<CELL> e= defs.elements();
		while (e.hasMoreElements()) {
			CELL cell = e.nextElement();
			strings.add(cell.text);
			if (cell.config != null) {
				configuration.put(viewer.toString(cell.item), cell.config.toMap());
			}
		}
		launchdata.put("definitions", strings);
		launchdata.put("configuration", configuration);
		launchdata.put("axes", axes.toMap());
		launchdata.put("positions", instance.getState().get("positions"));
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
		instance.getParent().validate();
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

	public void zetTabletUser(FormuleVakHouder formuleVakHouder)
	{	if(tablet==null) return;
		tablet.zetFormuleVakHouder(formuleVakHouder);
		tabletUser = formuleVakHouder;
	}
	
	public void zetTablet(FormuleVakHouder formuleVakHouder, int x, int y)
	{	if(tablet==null) 
		{	tablet = new Tablet(formuleVakHouder);
			tablet.setLocation(x,y);
			add(tablet,JLayeredPane.PALETTE_LAYER);
		}
		tablet.zetFormuleVakHouder(formuleVakHouder);
		tabletUser = formuleVakHouder;
	}
	
	public void addTablet(FormuleVakHouder formuleVakHouder, int x, int y)
	{	if(tablet==null) 
		{	tablet = new Tablet(formuleVakHouder);
		}
		if(tablet.getParent() == null)
		{	add(tablet,0);
			tablet.setLocation(x,y);
			remove(content);
			//resize();
            repaint();
		}
		setLayer(tablet, PALETTE_LAYER.intValue());
		tablet.zetFormuleVakHouder(formuleVakHouder);
	}
	
	@Override
	public void paint(Graphics g) {
		// TODO Auto-generated method stub
		super.paint(g);
	}

	public void removeTablet()
	{	if(tablet==null)return;
        remove(tablet);
        repaint();
	}
	
	public Tablet getTablet()
	{	return tablet;
	}
}
