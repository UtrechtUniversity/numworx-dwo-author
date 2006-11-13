package fi.doorziendwo;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import fi.beans.scorm.*;
import fi.beans.base64code.*;


public class ScormEditComponent extends Panel implements ScormEditComponentIF, ItemListener
{
	private Hashtable launchData;
	private Label titelLabel, tekstLabel;
	
	
	// alleen voor de DWO-test, zorg dat deze waarden
	// gelijk zijn aan die van de applet-html
	public static int startWidth = 780;
	public static int startHeight = 480;
	
	
	private ViewPanel viewPanel, viewPanelExample;
	private DoorzienDWO doorzienDWO;
	private TextArea textArea;
	
	private Checkbox exampleCB;
	private boolean example;
	
	private Checkbox muisCB;
	private boolean draaibaar;
	
	
	public ScormEditComponent(Hashtable launchData)
	{	setLayout(null);
		super.setSize(790,500); //voor dwo
		
		this.launchData = launchData;
				
		Color bgcolor = new Color(230,240,255);
		String kleurcode = getParameter("bgcolor");
		if(kleurcode!=null)bgcolor = new Color(Integer.parseInt(kleurcode.substring(1),16));
		setBackground(bgcolor);
			
		viewPanel = new ViewPanel(300,15,startWidth-315,startHeight-65);
		viewPanel.setLayout(null);
		viewPanel.setBackground(bgcolor);
		add(viewPanel);
			
		
			
		viewPanelExample = new ViewPanel(15,35,260,200);
		viewPanelExample.setLayout(null);
		viewPanelExample.setBackground(bgcolor);
		add(viewPanelExample);	
		
				
		
			
		textArea = new TextArea("",20,20,TextArea.SCROLLBARS_VERTICAL_ONLY );
		textArea.setBounds(15,240,260,200);
		textArea.setColumns(20);
		add(textArea);
		
		exampleCB = new Checkbox("Voorbeeldfiguur");
		exampleCB.setEnabled(true);
		exampleCB.setBounds(15,15,120,20);
		exampleCB.addItemListener(this);
		add(exampleCB);
		
		muisCB = new Checkbox("Draaibaar");
		muisCB.setEnabled(true);
		muisCB.setBounds(145,15,120,20);
		muisCB.addItemListener(this);
		add(muisCB);
		
		setState(launchData);
		
	}
	
	public void setApplet(DoorzienDWO d)
	{	doorzienDWO = d;
		viewPanel.setApplet(d);
		viewPanelExample.setApplet(d);
	}
	
	public void setSize(int b, int h)
	{	super.setSize(b,h);
	}
	
	public String getParameter(String name)
	{	String value = (String)launchData.get(name);
		return value;
	}
	
	public Hashtable getLaunchData()
    {   String language = getParameter("language");
    	String bgcolor = getParameter("bgcolor");
    	String editModeState = viewPanel.getState();
    	String exampleState = viewPanelExample.getState();
    	String tekst = textArea.getText();
    	String example = exampleCB.getState() ? "true" : "false";
    	String draaibaar = muisCB.getState() ? "true" : "false";
    	
    	Hashtable h = new Hashtable();
    	h.put("language",language);
    	h.put("bgcolor",bgcolor);
    	h.put("editModeState", editModeState);		
    	h.put("example",example);
    	h.put("draaibaar",draaibaar);
		h.put("exampleState", exampleState)	;	
		h.put("tekst", tekst);
    	return h;
	}
	
	public Component getComponent()
	{   return this;
	} 
	
	public void setState(Hashtable launchData)
	{			
		this.launchData = launchData;
		
		String editModeState = getParameter("editModeState");
		viewPanel.setState(editModeState);
		
		String exampleState = getParameter("exampleState");
		viewPanelExample.setState(exampleState);
			
		example = false;
		String exampleString = getParameter("example");
		if(exampleString!=null && exampleString.equals("true")) example = true;
		exampleCB.setState(example);
		viewPanelExample.setVisible(example);
		muisCB.setVisible(example);
		textArea.setLocation(15, example ? 240 : 35);
		muisCB.setVisible(example);
		
		
		draaibaar = false;
		String draaibaarString = getParameter("draaibaar");
		if(draaibaarString!=null && draaibaarString.equals("true")) draaibaar = true;
		muisCB.setState(draaibaar);
		
		String tekstString = getParameter("tekst");
		textArea.setText(tekstString);
	}
	
	public void end()
    {   
	}
	
    public void reset()
    {   
	}	
	
		public void itemStateChanged(ItemEvent e)
	{	if(e.getSource()==exampleCB)
		{	boolean b = exampleCB.getState();
			viewPanelExample.setVisible(b);
			muisCB.setVisible(b);
			textArea.setLocation(15, b ? 240 : 35);
		}
		
		
	}
	
}

