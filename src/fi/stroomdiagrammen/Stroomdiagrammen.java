/*  
	Stroomdiagrammen, v.20110302
	nieuwe versie van Flowdiagrams:
	0) geswingd
	1) applet met menu (via swing)
	2) inhangbaar in DWO ala Verknippen
*/

package fi.stroomdiagrammen;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;

import fi.beans.base64code.*;
import fi.beans.scorm.*;

import fi.beans.wiskopdrbeans.InteractiePanel;

// main applet class 
public class Stroomdiagrammen extends JApplet implements ScormAppletIF, WiskOpdrParamEditApplet
{   // attributes
    // language string
    static String langArg;
    // bundle
	static ResourceBundle rb;    

	protected SCORM12APIInterface api;
	boolean scormed = false;
	boolean reviewMode = false;	
	
	// DWO-component-gebeuren
	boolean isDWOComponent = false;
	
	InteractiePanelAdapter ipa = null;

	public static void main(String[] args)    
	{	int width = 700;
        int height = 500;
        Stroomdiagrammen stroomdiagrammen = new Stroomdiagrammen();
        stroomdiagrammen.scormed = true;
		ScormMainFrame mf = new ScormMainFrame(stroomdiagrammen, width, height);
		mf.setTitle("Stroomdiagrammen Scormed");
		mf.pack();
		mf.show();

		stroomdiagrammen.setLocation(mf.getInsets().left, mf.getInsets().top);		
		
		int framebreedte = width + mf.getInsets().left + mf.getInsets().right;
		int framehoogte = height + mf.getInsets().top + mf.getInsets().bottom;
		mf.setSize(framebreedte, framehoogte);

	}
	
	// kleuren
    static Color 
    	appletBackground = new Color(222, 222, 222),
    	bottomBackground = Color.lightGray,
    	workBackground = Color.white,
    	storageBackground = new Color(222, 222, 222),
    
    	edgeColor = Color.cyan, 
    	highEdgeColor = new Color(198, 239, 140),
    	zeroEdgeColor = Color.lightGray,
    	bubbleColor = DrawingPanel.hsbChange(Color.cyan, -2), 
    	highBubbleColor = DrawingPanel.hsbChange(new Color(198, 239, 140), -2),
    	buttonColor = Color.yellow; //Color.lightGray;
    
	// a copy of the flowdiagram
	DiagramCopy diagramCopy = null;
	Hashtable diagramState = null;

	// menu-gebeuren
	JMenuBar menuBar;
	JMenu berekeningenMenu, stroombreedteMenu, optiesMenu;
	JRadioButtonMenuItem rbDecimaalItem, rbBreukenItem;
	JRadioButtonMenuItem rbRelatiefItem, rbAbsoluutItem;
	JCheckBoxMenuItem cbLabelsItem;
	JMenuItem addRootItem;
	
    // constants   
    // bottom panel height
    static int bottomHeight = 50;
    // initial size
    public int minWidth = 700;
    public int minHeight = 500;

    // GUI components
    // workspace, result and storage
    DrawingPanel drawingPanel;
    // buffering drawing
    //BufferPanel bufferPanel;
    // bottom panel
    BottomPanel bPanel;
	
    // parametrisatie
    boolean toonBerekenMenu = true;
    boolean breuken = false;
    
    boolean toonStroomMenu = true;
    boolean absoluut = false;
    
/*    
    boolean toonOptiesMenu = true;
    boolean labels = false;
    boolean extraBron = false;
*/    
	public Stroomdiagrammen()
	{	langArg = "nl";
		Locale language = new Locale (langArg, "");
		rb = ResourceBundle.getBundle("fi.stroomdiagrammen.text.Text", language);
	}

	public Stroomdiagrammen(Locale language)
	{	langArg = language.getLanguage();
		rb = ResourceBundle.getBundle("fi.stroomdiagrammen.text.Text", language);
	}
	
	public void init()
	{  	
		
		try
		{	api = Scorm.findAPI(this);
		}
		catch(Exception e){}
		// size is 700 x 500
		
		getContentPane().setLayout(new BorderLayout());

        // get language parameter if any (nl is default)
        langArg = getParameter("language");
        // create language lookup table
        if (langArg == null)	
        	langArg = "nl";
		Locale language = new Locale (langArg, "");
		rb = ResourceBundle.getBundle("fi.stroomdiagrammen.text.Text", language);
        
		//instelling achtergrondkleur
		String kleurcode = getParameter("bgcolor");
		if (kleurcode != null)
			appletBackground = new Color(Integer.parseInt(kleurcode.substring(1), 16));
		setBackground(appletBackground);
		
		String toonBerekenMenuString = getParameter("berekenmenu");
		if ((toonBerekenMenuString != null) && toonBerekenMenuString.equals("no"))
			toonBerekenMenu = false;
		
		String breukenString = getParameter("breuken");
		if ((breukenString != null) && breukenString.equals("yes"))
			breuken = true;

		String toonStroomMenuString = getParameter("stroommenu");
		if ((toonStroomMenuString != null) && toonStroomMenuString.equals("no"))
			toonStroomMenu = false;
		
		String absoluutString = getParameter("absoluut");
		if ((absoluutString != null) && absoluutString.equals("yes"))
			absoluut = true;
		
		// menubalk
		menuBar = new JMenuBar();
		MenuListener menuListener = new MenuListener();
		
		// menu berekeningen
		berekeningenMenu = new JMenu(rb.getString("represText"));
		ButtonGroup berekeningenGroup = new ButtonGroup();
		rbDecimaalItem = new JRadioButtonMenuItem(rb.getString("decimalText"));
		if (!breuken)
			rbDecimaalItem.setSelected(true);
		berekeningenGroup.add(rbDecimaalItem);
		berekeningenMenu.add(rbDecimaalItem);
		rbDecimaalItem.addActionListener(menuListener);
		
		rbBreukenItem = new JRadioButtonMenuItem(rb.getString("fractionText"));
		if (breuken)
			rbBreukenItem.setSelected(true);
		berekeningenGroup.add(rbBreukenItem);
		berekeningenMenu.add(rbBreukenItem);
		rbBreukenItem.addActionListener(menuListener);
		
		//menu stroombreedte
		stroombreedteMenu = new JMenu(rb.getString("thicknessText"));
		ButtonGroup stroombreedteGroup = new ButtonGroup();
		rbRelatiefItem = new JRadioButtonMenuItem(rb.getString("relativeText"));
		if (!absoluut)
			rbRelatiefItem.setSelected(true);
		stroombreedteGroup.add(rbRelatiefItem);
		stroombreedteMenu.add(rbRelatiefItem);
		rbRelatiefItem.addActionListener(menuListener);
		
		rbAbsoluutItem = new JRadioButtonMenuItem(rb.getString("absoluteText"));
		if (absoluut)
			rbAbsoluutItem.setSelected(true);
		stroombreedteGroup.add(rbAbsoluutItem);
		stroombreedteMenu.add(rbAbsoluutItem);
		rbAbsoluutItem.addActionListener(menuListener);
		
		// optiesMenu
		optiesMenu = new JMenu(rb.getString("optionsText"));
		cbLabelsItem = new JCheckBoxMenuItem(rb.getString("labelsText"));
		optiesMenu.add(cbLabelsItem);
		cbLabelsItem.addActionListener(menuListener);
		addRootItem = new JMenuItem(rb.getString("addRootText"));
		optiesMenu.add(addRootItem);
		addRootItem.addActionListener(menuListener);
		
		if (toonBerekenMenu)
			menuBar.add(berekeningenMenu);
		if (toonStroomMenu)
			menuBar.add(stroombreedteMenu);
		menuBar.add(optiesMenu);
		
		setJMenuBar(menuBar);

		// create and add GUI compoments
        drawingPanel = new DrawingPanel(this);
        getContentPane().add(drawingPanel, BorderLayout.CENTER);
        
        bPanel = new BottomPanel(this);
        getContentPane().add(bPanel, BorderLayout.SOUTH);        
        // for BorderLayout
        validate();
/*        
        if (scormed)
        {	drawingPanel.setSize(700, 427);	
//        	System.out.println("dpw = " + drawingPanel.getSize().width);
//        	System.out.println("dph = " + drawingPanel.getSize().height);
        }
*/        
        // now sizes are known, so initialize
        drawingPanel.initialize();
        
        		
	} // init

	class MenuListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{	// berkeningenMenu
			if (e.getSource() == rbDecimaalItem)
			{	if (rbDecimaalItem.isSelected())
					drawingPanel.diagramManager.setFlowMode(DrawingPanel.decMode);
				//else
				//	drawingPanel.diagramManager.setFlowMode(DrawingPanel.fracMode);
			}
			else if (e.getSource() == rbBreukenItem)
			{	if (rbBreukenItem.isSelected())
					drawingPanel.diagramManager.setFlowMode(DrawingPanel.fracMode);
				//else
				//	drawingPanel.diagramManager.setFlowMode(DrawingPanel.decMode);
			}
			else if (e.getSource() == rbRelatiefItem)
			{	if (rbRelatiefItem.isSelected())
					drawingPanel.diagramManager.setEdgeThicknessMode(DrawingPanel.relMode);
				//else
				//	drawingPanel.diagramManager.setEdgeThicknessMode(DrawingPanel.absMode);
			}
			else if (e.getSource() == rbAbsoluutItem)
			{	if (rbAbsoluutItem.isSelected())
					drawingPanel.diagramManager.setEdgeThicknessMode(DrawingPanel.absMode);
				//else
				//	drawingPanel.diagramManager.setEdgeThicknessMode(DrawingPanel.relMode);
			}
			
			else if (e.getSource() == cbLabelsItem)
			{	if (cbLabelsItem.isSelected())
					drawingPanel.diagramManager.setVertexLabels(true);
				else
					drawingPanel.diagramManager.setVertexLabels(false);
			}
			else if (e.getSource() == addRootItem)
			{	drawingPanel.addNewRoot();
				
			}
		}
	}
	
	// scormgebeuren
	public void start()
	{	if (api != null)
		{	String s = api.LMSGetValue("cmi.suspend_data");
			if (s != null && !s.equals(""))
			{	setState(s);
//System.out.println("api setState");			
			}
		}
		else
		{
//System.out.println("api not setState");			
		}
	}
	
	public void stopSco()
	{	stop();
		api = null;
	}

	public void stop()
	{	if (api != null)
		{	String s = getState();
			String d = new Double(getScore()).toString();
			api.LMSSetValue("cmi.core.score.raw",d);
			api.LMSSetValue("cmi.suspend_data",s);
		}
	}
	
	public void setState(String s)
	{	
		// decodeer de string
		Object o = StringCodeObject.decodeStringToObject(s);
		// cast
		Hashtable h = (Hashtable) o;
		
		// haal de stateString uit de hashtable
		String stateString = "";
		if (h.containsKey("state"))
		{	stateString = (String) h.get("state");

//System.out.println("string found");		
		
		}
		// decodeer de string		
		if ((stateString != null) && !stateString.equals(""))
		{	
			Object ob = StringCodeObject.decodeStringToObject(stateString);
			diagramCopy = (DiagramCopy) ob;
			
//System.out.println("string decoded");
			drawingPanel.initialize();	
		}
		else 
			diagramCopy = null;	

	}
	
	public String getState()
	{	
		// codeer de diagramCopy tot string
		diagramCopy = drawingPanel.diagramManager.copyDiagram();		
		
		String stateString = "";
		if (diagramCopy != null)
		{	stateString = StringCodeObject.encodeObjectToString(diagramCopy);
	    	
System.out.println("diagramcopy encoded");	    	

		}	
		// stop de gecodeerde string in de hashtable
		diagramState = new Hashtable();
		diagramState.put("state", stateString);

	    // codeer deze gegevens tot een string
	    String s = StringCodeObject.encodeObjectToString(diagramState);

	    return s;
		
	}
	
	public double getScore()
	{	return 0.5;
	}
	
	public InteractiePanel getInteractiePanel()
	{	if (ipa == null)
			ipa = new InteractiePanelAdapter(this);
		
		return ipa;
	}
	
	public boolean hasEditMode()
	{	return false;
	}

    public ScormEditComponentIF getEditComponent(Hashtable launchdata)
    {	return null;
    }
	
    public Hashtable getDefaultParameters()
    {
    	Hashtable h = new Hashtable();

    	h.put("berekenmenu", "yes");
    	h.put("breuken", "no");
    	
    	h.put("stroommenu", "yes");
    	h.put("absoluut", "no");
    	
/*    	
    	h.put("optiesmenu", "yes");
    	h.put("labels", "no");
*/    	
    	
    	return h;
    }
    
    public Parameter[] getEditableParameters()
	{	
    	Parameter[] parameters = new Parameter[4];
		
		DataType type = new ScormString();
		
		Parameter param = new Parameter("berekenmenu", "menu Berekeningen", type);
		param.setHelpText("vul in: yes of no");		
		parameters[0] = param;
		
		param = new Parameter("breuken", "bereken in breuken", type);
		param.setHelpText("vul in: yes of no");		
		parameters[1] = param;

		param = new Parameter("stroommenu", "menu Stroombreedte", type);
		param.setHelpText("vul in: yes of no");		
		parameters[2] = param;
		
		param = new Parameter("absoluut", "strbreedte absoluut", type);
		param.setHelpText("vul in: yes of no");		
		parameters[3] = param;
		
		
		return parameters;
    }

    public Parameter[] getAllParameters()
    {	return null;
    }   
    
    public void setSingleComponent()
	{	

		isDWOComponent = true;
		bPanel.fiButton.setVisible(false);
		
	}		
	
}
