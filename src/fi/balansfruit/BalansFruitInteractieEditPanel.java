package fi.balansfruit;

import java.awt.event.*;
import java.util.*;

import javax.swing.*;

public class BalansFruitInteractieEditPanel extends BalansFruitInteractiePanel implements ActionListener, FocusListener
{
	int aantalSoorten;
	private ImageComponent[] imageComponenten;
	private JTextField[] aantalFields;
	private JTextField[] gewichtFields;
	private String[] aantallen;
	private String[] gewichten;
	
	private JCheckBox variabelenCB;
	private JCheckBox eenhedenCB;
	
	private JCheckBox fixedOptieCB;
	private JCheckBox resetOptieCB;
	private JCheckBox bewaarOptieCB;
	private JCheckBox viewEquationCB;
	
	private JLabel aantalLabel, gewichtLabel;
	
	
	public BalansFruitInteractieEditPanel()
	{	super();
		editMode = true;
		
		setLayout(null);
		aantalSoorten = imageNames.length-4;
		
		aantallen = new String[aantalSoorten];
		gewichten = new String[aantalSoorten];
		
		aantalLabel = new JLabel("Number");
		aantalLabel.setBounds(540,5,60,20);
		add(aantalLabel);
		
		gewichtLabel = new JLabel("Weight");
		gewichtLabel.setBounds(710,5,60,20);
		add(gewichtLabel);
		
		imageComponenten = new ImageComponent[aantalSoorten];
		aantalFields = new JTextField[aantalSoorten];
		gewichtFields = new JTextField[aantalSoorten];
		
		variabelenCB = new JCheckBox("Variables");
		variabelenCB.setBounds(400,400,100,20);
		variabelenCB.addActionListener(this);
		add(variabelenCB);
		
		eenhedenCB = new JCheckBox("Show units");
		eenhedenCB.setBounds(400,420,100,20);
		eenhedenCB.addActionListener(this);
		eenhedenCB.setSelected(true);
		add(eenhedenCB);
		
		bewaarOptieCB = new JCheckBox("Store option");
		bewaarOptieCB.setBounds(20,400,180,20);
		bewaarOptieCB.addActionListener(this);
		add(bewaarOptieCB);
		
		fixedOptieCB = new JCheckBox("Fixed");
		fixedOptieCB.setBounds(20,430,180,20);
		fixedOptieCB.addActionListener(this);
		add(fixedOptieCB);
		
		resetOptieCB = new JCheckBox("Reset option");
		resetOptieCB.setBounds(20,460,180,20);
		resetOptieCB.addActionListener(this);
		add(resetOptieCB);
		
		viewEquationCB = new JCheckBox("View Equation");
		viewEquationCB.setBounds(20,490,180,20);
		viewEquationCB.addActionListener(this);
		add(viewEquationCB);
		
		int height = 10;
		int start = 0;
		for(int i=0 ; i<aantalSoorten ; i++)
		{
			imageComponenten[i] = new ImageComponent(getImage(imageNames[i]));
			imageComponenten[i].setLocation(600,height+start+25);
			add(imageComponenten[i]);
			int hc = imageComponenten[i].getHeight();
			
			aantalFields[i] = new JTextField("0");
			aantalFields[i].addActionListener(this);
			aantalFields[i].addFocusListener(this);
			aantalFields[i].setBounds(550,height+start+hc/2+15, 40,20);
			add(aantalFields[i]);
			
			gewichtFields[i] = new JTextField("0");
			gewichtFields[i].addActionListener(this);
			gewichtFields[i].addFocusListener(this);
			gewichtFields[i].setBounds(720,height+start+hc/2+15, 40,20);
			add(gewichtFields[i]);
			
			if(i<10) height = height + hc +21;
			else  height = height + hc +11;
		}
		
		launchData = defaultParamValues;
		vulTextFields();
		
		loadParameters();
		//Thread t = new Thread(this);
		//t.start();
		run();
	}
	
	public void vulTextFields()
	{
		for(int i=0 ; i<aantalSoorten ; i++)
		{
			if(launchData.containsKey("aantal"+i))aantallen[i] = (String)launchData.get("aantal"+i);
			else aantallen[i] = "0";
			if(launchData.containsKey("gewicht"+i))gewichten[i] = (String)launchData.get("gewicht"+i);
			else gewichten[i] = "0";
		}
		
		for(int i=0 ; i<aantalSoorten ; i++)
		{	aantalFields[i].setText(aantallen[i]);
			gewichtFields[i].setText(gewichten[i]);
		}
	}
	
	public Hashtable getEditState()
	{	
		int aantalFruitObjects;
		int[] stukFruitX;
		int[] containerNr;
		boolean fixedOptie;
		boolean bewaarOptie;
		boolean resetOptie;
		boolean viewEquation;
		boolean variabelen;
		boolean showEenheden;
		
	    
		
		aantalFruitObjects = this.aantalFruitObjects;
	    
		stukFruitX = new int[aantalFruitObjects];
		containerNr = new int[aantalFruitObjects];
		for(int i=0 ; i<aantalFruitObjects; i++)
	    {	stukFruitX[i] = fruitObjects[i].getLocation().x;
	    	int contNr = 0;
	    	boolean opLinks = fruitObjects[i].getParent()==links;
	    	boolean opRechts = fruitObjects[i].getParent()==rechts;
	    	if(opLinks) contNr=1;
	    	if(opRechts) contNr=2;
	    	containerNr[i] = contNr;
	    }
		
		fixedOptie = fixedOptieCB.isSelected();
		bewaarOptie = bewaarOptieCB.isSelected();
		resetOptie = resetOptieCB.isSelected();
		viewEquation = viewEquationCB.isSelected();
		variabelen = variabelenCB.isSelected();
		showEenheden = eenhedenCB.isSelected();
		
	     	
		launchData.put("aantalFruitObjects", new Integer(aantalFruitObjects));
		launchData.put("stukFruitX", stukFruitX);
		launchData.put("containerNr", containerNr);
		launchData.put("fixedOptie", new Boolean(fixedOptie));
		launchData.put("bewaarOptie", new Boolean(bewaarOptie));
		launchData.put("resetOptie", new Boolean(resetOptie));
		launchData.put("viewEquation", new Boolean(viewEquation));
		launchData.put("variabelen", new Boolean(variabelen));
		launchData.put("showEenheden", new Boolean(showEenheden));
		
		return launchData;
	}
	
	public void setEditState(Hashtable h)
	{
		launchData = h;
		vulTextFields();
		loadParameters();
		//Thread t = new Thread(this);
		//t.start();
		run();
		
		int aantalFruitObjects=0;
		int[] stukFruitX=null;
		int[] containerNr=null;
		boolean fixedOptie=false;
		boolean bewaarOptie=false;
		boolean resetOptie=false;
		boolean viewEquation=false;
		boolean variabelen = false;
		boolean showEenheden = true;
		
		if(h.containsKey("aantalFruitObjects")) aantalFruitObjects = ((Integer)h.get("aantalFruitObjects")).intValue();
		if(h.containsKey("stukFruitX")) stukFruitX = (int[])h.get("stukFruitX");
		if(h.containsKey("containerNr")) containerNr = (int[])h.get("containerNr");
		if(h.containsKey("fixedOptie")) fixedOptie = ((Boolean)h.get("fixedOptie")).booleanValue();
		if(h.containsKey("bewaarOptie")) bewaarOptie = ((Boolean)h.get("bewaarOptie")).booleanValue();
		if(h.containsKey("resetOptie")) resetOptie = ((Boolean)h.get("resetOptie")).booleanValue();
		if(h.containsKey("viewEquation")) viewEquation = ((Boolean)h.get("viewEquation")).booleanValue();
		if(h.containsKey("variabelen")) variabelen = ((Boolean)h.get("variabelen")).booleanValue();
		if(h.containsKey("showEenheden")) showEenheden = ((Boolean)h.get("showEenheden")).booleanValue();
		
		this.aantalFruitObjects = aantalFruitObjects;
		
		zetEenheden(showEenheden);
		
		
		for(int i = 11; i < 17; i++)
			imageComponenten[i].setImage(getImage(imageNames[i]));
		
		for(int i=0 ; i<aantalFruitObjects; i++)
	    {	if(containerNr[i]==0) fruitObjects[i].setLocation(stukFruitX[i],fruitObjects[i].getLocation().y);
	    	if(containerNr[i]==1) 
	    	{	links.addLWMComponent(fruitObjects[i],stukFruitX[i],fruitObjects[i].getLocation().y);
	    	}
	    	if(containerNr[i]==2) 
	    	{	rechts.addLWMComponent(fruitObjects[i],stukFruitX[i],fruitObjects[i].getLocation().y);
	    	}
   	    }
		main.setBalance();
		
		fixedOptieCB.setSelected(fixedOptie);
		bewaarOptieCB.setSelected(bewaarOptie);
		resetOptieCB.setSelected(resetOptie);
		viewEquationCB.setSelected(viewEquation);
		variabelenCB.setSelected(variabelen);
		eenhedenCB.setSelected(showEenheden);
		
		zetFixedOptie(fixedOptie);
		zetResetOptie(resetOptie);
		zetViewEquation(viewEquation);
//System.out.println("bfiep setEditState " + viewEquation);		
		
		int height = 10;
		int start = 0;
		if(variabelenCB.isSelected())start = -520;
		
		for(int i=0 ; i<aantalSoorten ; i++)
		{
			int hc = imageComponenten[i].getHeight();
			imageComponenten[i].setLocation(600,height+start+25);
			aantalFields[i].setBounds(550,height+start+hc/2+15, 40,20);
			gewichtFields[i].setBounds(720,height+start+hc/2+15, 40,20);
			if(i<10) height = height + hc +21;
			else  height = height + hc +11;
		}
		
	}
	
	public void verwerkInvoer(int nr)
	{
		aantallen[nr] = aantalFields[nr].getText();
		gewichten[nr] = gewichtFields[nr].getText();
		
		launchData.put("aantal"+nr, aantallen[nr]);
		launchData.put("gewicht"+nr, gewichten[nr]);
		
		
		for(int i=0 ; i<aantalFruitObjects; i++)
	    {	voorraad.addLWMComponent(fruitObjects[i],fruitObjects[i].getLocation().x,fruitObjects[i].getLocation().y);
	    	
   	    }
		main.setBalance();
		
		loadParameters();
		//Thread t = new Thread(this);
		//t.start();
		run();
			
	}
	

	public void actionPerformed(ActionEvent e)
	{
		super.actionPerformed(e);
		
		for(int i=0 ; i<aantalSoorten ; i++)
		{	if(aantalFields[i]==e.getSource() || gewichtFields[i]==e.getSource()) verwerkInvoer(i);
		}
		if(e.getSource()==variabelenCB) 
		{ 	int height = 10;
			int start = 0;
			if(variabelenCB.isSelected())start = -520;
			
			for(int i=0 ; i<aantalSoorten ; i++)
			{
				int hc = imageComponenten[i].getHeight();
				imageComponenten[i].setLocation(600,height+start+25);
				aantalFields[i].setBounds(550,height+start+hc/2+15, 40,20);
				gewichtFields[i].setBounds(720,height+start+hc/2+15, 40,20);
				if(i<10) height = height + hc +21;
				else  height = height + hc +11;
			}
		}
		if(e.getSource()==eenhedenCB) 
		{
			zetEenheden(eenhedenCB.isSelected());
			for(int i = 11; i < 17; i++)
				imageComponenten[i].setImage(getImage(imageNames[i]));
			                 
			repaint();
		}
		if (e.getSource() == resetOptieCB)
		{
			zetResetOptie(resetOptieCB.isSelected());
		}
		if (e.getSource() == fixedOptieCB)
		{
			zetFixedOptie(fixedOptieCB.isSelected());
		}
		if (e.getSource() == viewEquationCB)
		{
			zetViewEquation(viewEquationCB.isSelected());
		}

	}
	
	public void focusLost(FocusEvent e)
	{
		for(int i=0 ; i<aantalSoorten ; i++)
		{	//if(aantalFields[i]==e.getSource() || gewichtFields[i]==e.getSource()) verwerkInvoer(i);
		}
	}
	
	public void focusGained(FocusEvent e)
	{
		
	}
}
