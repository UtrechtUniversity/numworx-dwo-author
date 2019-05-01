package fi.wiskopdr;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Image;
import java.awt.Point;

import javax.swing.*;

import fi.wiskopdr.tekstobjects.*;
import fi.wiskopdr.domainmodel.Constants;
import fi.wiskopdr.formuleobjects.*;
import fi.beans.iconan.Iconan;
import fi.beans.wiskopdrbeans.*;

public class CheckValueUnitEditPanel extends JPanel implements InteractieEditPanel, ActionListener, TabletOwner
{
	private TekstEditor tekstEditor;
	
	private int aantalValueObjects;
	
	private JLabel aantalValueObjectsLabel;
	private JTextField aantalValueObjectsTF;
	
	private JLabel maxScoreLabel;
	private JTextField maxScoreTF;
	
	private JCheckBox checkAfzonderlijkCB;
	private JCheckBox checkSamenCB;
	private JCheckBox viewCB;
	
	private boolean checkSamen;
	
	private FormuleEditor formuleEditor;
	
	private String formuleString;
	private String[] formuleStrings;
	
	private Tablet tablet;
    private FormuleVakHouder tabletUser;
    private boolean tabletAdded;
    
    private JCheckBox logCB;
	private JTextField logIDField;
	private JCheckBox checkCB;
	private JCheckBox teltMeeCB;
	private ObjectiveChoiceButton logObjectivesButton;
	
	private FormuleButton knopImageButton;
	private Dialog imageDialog;
	private Iconan iconman;
	private String knopImageString = "";
	private Image knopImage;
	
	
	public CheckValueUnitEditPanel()
	{
		setLayout(null);
		setBounds(0,0,780,480);
		tekstEditor = new TekstEditor(true,false,false);
		tekstEditor.setBounds(300,130,465,365);
		//add(tekstEditor);
		
		aantalValueObjectsLabel = new JLabel(WiskOpdr.rb.getString("aantalValueObjectenLabel"));//"Aantal objecten");
		aantalValueObjectsLabel.setBounds(10,50,180,20);
		add(aantalValueObjectsLabel);
		
		aantalValueObjectsTF = new JTextField("0");
		aantalValueObjectsTF.setBounds(190,50,40,20);
		aantalValueObjectsTF.addActionListener(this);
		add(aantalValueObjectsTF);
		
		maxScoreLabel = new JLabel(WiskOpdr.rb.getString("score"));//"Score");
		maxScoreLabel.setBounds(300,50,80,20);
		add(maxScoreLabel);
		
		maxScoreTF = new JTextField("0");
		maxScoreTF.setBounds(380,50,40,20);
		maxScoreTF.addActionListener(this);
		add(maxScoreTF);
		
		checkAfzonderlijkCB = new JCheckBox(WiskOpdr.rb.getString("checkAfzonderlijkLabel"));//"Check afzonderlijk");
		checkAfzonderlijkCB.setBounds(300,170,280,20);
		checkAfzonderlijkCB.addActionListener(this);
		checkAfzonderlijkCB.setOpaque(false);
		add(checkAfzonderlijkCB);
		
		checkSamenCB = new JCheckBox(WiskOpdr.rb.getString("checkOpSamenhangLabel"));//"Check op samenhang");
        checkSamenCB.setBounds(300,200,280,20);
        checkSamenCB.addActionListener(this);
        checkSamenCB.setOpaque(false);
        checkSamenCB.setSelected(true);
        add(checkSamenCB);
        
		
		formuleEditor = new FormuleEditor(true);
		formuleEditor.setMultiLine(true);
		formuleEditor.setBounds(300,230,480,200);
		formuleEditor.setVisible(true);
        add(formuleEditor);
        
        logCB = new JCheckBox(WiskOpdr.rb.getString("logCBLabel"));
        logCB.setBounds(450,5,70,20);
        logCB.addActionListener(this);
        logCB.setOpaque(false);
		add(logCB);
		
		logIDField = new JTextField("0");
		logIDField.setBounds(520,5,60,20);
		logIDField.addActionListener(this);
		logIDField.setVisible(false);
		add(logIDField);
		
		logObjectivesButton = new ObjectiveChoiceButton(WiskOpdr.objectives, WiskOpdr.categorieString, WiskOpdr.studentModel);
        logObjectivesButton.setVisible(WiskOpdr.objectives!=null);
        logObjectivesButton.setBounds(600,5,120,20);
        if(WiskOpdr.objectives!=null)add(logObjectivesButton);
		
		checkCB = new JCheckBox(WiskOpdr.rb.getString("checkCBLabel"));
		checkCB.setBounds(5,5,200,20);
		checkCB.addActionListener(this);
		checkCB.setOpaque(false);
		checkCB.setSelected(true);
		add(checkCB);
		
		teltMeeCB = new JCheckBox(WiskOpdr.rb.getString("teltMeeCBLabel"));
		teltMeeCB.setBounds(225,5,200,20);
		teltMeeCB.addActionListener(this);
		teltMeeCB.setOpaque(false);
		teltMeeCB.setSelected(true);
		add(teltMeeCB);
		
		viewCB = new JCheckBox(WiskOpdr.rb.getString("viewCBLabel"));//("Springt terug");
		viewCB.setBounds(580,170,280,20);
		viewCB.setOpaque(false);
		viewCB.setSelected(false);
		viewCB.setVisible(false);
		add(viewCB);
		
		knopImageButton = new FormuleButton(WiskOpdr.rb.getString("klaarKnopLabel"));
		knopImageButton.setBounds(10,80,80,20);
		knopImageButton.addActionListener(this);
		add(knopImageButton);
	}
	
	
	public void setEditState(Hashtable h)
	{
		int aantalValueObjects = 0;
		int scoreMax = 0;
		boolean checkSamen = false;
		String formuleString = "$f@";
		String[] formuleStrings = null;
		boolean logOption = false;
		String logID = "";
		boolean check = true;
		boolean teltMee = true;
		boolean view = false;
		boolean[][] logObjectives = null;
		String[] smObjectives = null;
		String knopImageString = "";
		
	    if(h.containsKey("aantalValueObjects")) aantalValueObjects = ((Integer)h.get("aantalValueObjects")).intValue();
	    if(h.containsKey("scoreMax")) scoreMax = ((Integer)h.get("scoreMax")).intValue();
	    if(h.containsKey("checkSamen")) checkSamen = ((Boolean)h.get("checkSamen")).booleanValue();
	    if(h.containsKey("formuleString")) formuleString = (String)h.get("formuleString");
	    if(h.containsKey("formuleStrings")) formuleStrings = (String[])h.get("formuleStrings");
	    if(h.containsKey("logOption")) logOption = ((Boolean)h.get("logOption")).booleanValue();
		if(h.containsKey("logID")) logID = (String)h.get("logID");
		if(h.containsKey("check")) check = ((Boolean)h.get("check")).booleanValue();
		if(h.containsKey("teltMee")) teltMee = ((Boolean)h.get("teltMee")).booleanValue();
		if(h.containsKey("view")) view = ((Boolean)h.get("view")).booleanValue();
		if(h.containsKey("logObjectives")) logObjectives = (boolean[][])h.get("logObjectives");
		if(h.containsKey(Constants.OBJECTIVES)) smObjectives = (String[]) h.get(Constants.OBJECTIVES);
		if(h.containsKey("knopImageString")) knopImageString = (String)h.get("knopImageString");
		
	    this.aantalValueObjects = aantalValueObjects;
	    this.knopImageString = knopImageString;
	    aantalValueObjectsTF.setText(""+aantalValueObjects);
	    maxScoreTF.setText(""+scoreMax);
	    checkAfzonderlijkCB.setSelected(!checkSamen);
	    viewCB.setVisible(!checkSamen);
        viewCB.setSelected(view && !checkSamen);
	    checkSamenCB.setSelected(checkSamen);
	    formuleEditor.geefFormuleVak().vulVak(formuleString);
	    if(formuleStrings!=null)formuleEditor.zetRegels(formuleStrings);
	    formuleEditor.setVisible(checkSamen);
	    
	    logCB.setSelected(logOption);
        logIDField.setVisible(logOption);
        //logObjectivesButton.setVisible(logOption);
        logIDField.setText(logID);
        logObjectivesButton.setChoices(logObjectives);
        logObjectivesButton.setObjectives(smObjectives);
        checkCB.setSelected(check);
        teltMeeCB.setSelected(teltMee);
        
        
    	knopImageButton.setPopupButtonImage(knopImage);
    	iconman = new Iconan(WiskOpdr.applet, (Component)this, (Hashtable)TekstImageVak.getImageMap());
    	if(knopImageString!=null && !"".equals(knopImageString)) {
    		knopImage = iconman.getImage(knopImageString);
    		knopImageButton.setPopupButtonImage(knopImage);
    	}
    	else {
    		knopImageButton.setCode(WiskOpdr.rb.getString("klaarKnopLabel"));
    	}
       
	}
	
	public Hashtable getEditState()
	{
	    int aantalValueObjects;
	    int scoreMax = 0;
	    int[][] scoreMaxObjectives = null;
	    boolean checkSamen = false;
		String formuleString = "$f@";
		String[] formuleStrings = null;
		boolean logOption = false;
		String logID = "";
		boolean[][] logObjectives = null;
		String[] smObjectives = null;
		boolean check = true;
		boolean teltMee = true;
		boolean view = false;
		String knopImageString = "";
		
		knopImageString = this.knopImageString;
		aantalValueObjects = Integer.parseInt(aantalValueObjectsTF.getText());
		scoreMax = Integer.parseInt(maxScoreTF.getText());
		checkSamen = checkSamenCB.isSelected();
	    formuleString = formuleEditor.geefFormuleVak().toString();
	    formuleStrings = formuleEditor.geefRegels();
	    logOption = logCB.isSelected();
		logID = logIDField.getText();
		logObjectives = logObjectivesButton.getChoices();
		smObjectives = logObjectivesButton.getObjectives();
		check = checkCB.isSelected();
		teltMee = teltMeeCB.isSelected();
		view = viewCB.isSelected();
		
		if(logObjectives!=null)
		{	scoreMaxObjectives = new int[logObjectives.length][];
			for(int j=0 ; j<scoreMaxObjectives.length; j++)
			{	scoreMaxObjectives[j] = new int[logObjectives[j].length];
				for(int i=0 ; i<scoreMaxObjectives[j].length ; i++)
				{	if(logObjectives[j][i]) scoreMaxObjectives[j][i] = scoreMax;
				}
			}
		}
	    
		Hashtable h = new Hashtable();
		h.put("aantalValueObjects", new Integer(aantalValueObjects));
		h.put("scoreMax", new Integer(scoreMax));
		h.put("checkSamen", new Boolean(checkSamen));
		h.put("formuleString", formuleString);
		h.put("formuleStrings", formuleStrings);
		h.put("logOption",new Boolean(logOption));
		h.put("logID",logID);
		h.put("check",new Boolean(check));
		h.put("teltMee",new Boolean(teltMee));
		h.put("view",new Boolean(view));
		if(logObjectives!=null)
		{	h.put("logObjectives",logObjectives);
    		h.put("scoreMaxObjectives",scoreMaxObjectives);
            try {
              h.put(Constants.OBJECTIVES, smObjectives);
            } catch(Exception e) {}
		}
		h.put("knopImageString", knopImageString);
		
		return h;
	}
		
	public void setBounds(int x, int y, int b, int h)
	{
		super.setBounds(x,y,b,h);
	}
	
	public void zetBreedte(int b){}
	
	public void zetHoogte(int h){}
	
	public void wis(){}
    
	public void zetMode(int mode){}
	
    public void stop(){}
    
    public void start(){}
    
    public void addActionListener(ActionListener al){}
    
    public void editImage() {
        if(imageDialog == null)
        {
        	
        	Frame f = JOptionPane.getFrameForComponent(this);
			imageDialog = new Dialog(f,"title", true);
			imageDialog.setLayout(new BorderLayout());
			iconman = new Iconan(WiskOpdr.applet, (Component)this, (Hashtable)TekstImageVak.getImageMap());
            imageDialog.add(iconman);
            imageDialog.pack();
            iconman.addActionListener(this);
        }
        iconman.select(knopImageString);
        imageDialog.setVisible(true);
    }
    
    public void actionPerformed(ActionEvent e)
	{
		if(e.getSource()==aantalValueObjectsTF)
		{	aantalValueObjects = Integer.parseInt(aantalValueObjectsTF.getText());
		}
		else if(e.getSource()==checkAfzonderlijkCB)
		{	checkSamenCB.setSelected(!checkAfzonderlijkCB.isSelected());
			formuleEditor.setVisible(!checkAfzonderlijkCB.isSelected());
			viewCB.setVisible(checkAfzonderlijkCB.isSelected());
			if(!checkAfzonderlijkCB.isSelected())viewCB.setSelected(false);
		}
		else if(e.getSource()==checkSamenCB)
		{	checkAfzonderlijkCB.setSelected(!checkSamenCB.isSelected());
			formuleEditor.setVisible(!checkAfzonderlijkCB.isSelected());
			viewCB.setVisible(checkAfzonderlijkCB.isSelected());
			if(!checkAfzonderlijkCB.isSelected())viewCB.setSelected(false);
		}
		else if(e.getSource()==logCB)
	    {   logIDField.setVisible(logCB.isSelected());  
	    	//logObjectivesButton.setVisible(logCB.isSelected());
	    }
		else if(e.getSource()==knopImageButton)
	    {   editImage();
	            
	    }
	    else if(e.getSource()==iconman)
	    {
	    	String name = e.getActionCommand();
	        if(!"".equals(name))
	        {
	        	knopImageString = name;
	            this.knopImage = iconman.getImage(name);
	            knopImageButton.setPopupButtonImage(knopImage);
	            int imWidth = iconman.getWidth(knopImageString);
				int imHeight = iconman.getHeight(knopImageString);
				if(imWidth == -1) imWidth = 20;
				if(imHeight == -1) imHeight = 20;
				knopImageButton.setSize(imWidth,imHeight);
	            repaint();
	        }
	    }

	    if(imageDialog!=null)
	        imageDialog.setVisible(false);
	}
    
// methoden TabletOwner
    
    public void zetTabletUser(FormuleVakHouder formuleVakHouder)
    {   if(tablet==null) return;
        tablet.zetFormuleVakHouder(formuleVakHouder);
        tabletUser = formuleVakHouder;
        
    }
    
    public void zetTablet(FormuleVakHouder formuleVakHouder, int x, int y)
    {   if(tablet==null) 
        {   tablet = new Tablet(formuleVakHouder);
            tablet.setLocation(x,y);
            
        }
        tablet.zetFormuleVakHouder(formuleVakHouder);
        tabletUser = formuleVakHouder;
        
        
    }
    
    public void addTablet(FormuleVakHouder formuleVakHouder, int x, int y)
    {   if(tablet==null) 
        {   tablet = new Tablet(formuleVakHouder);
            
            
        }
        if(!tabletAdded)
        {   add(tablet,0);
            tablet.setLocation(x,y);
            tabletAdded = true;
            //resize();
            repaint();
        }
        tablet.zetFormuleVakHouder(formuleVakHouder);
    }
    
    public void removeTablet()
    {   if(tablet==null)return;
        remove(tablet);
        //resize();
        repaint();
        tabletAdded = false;
    }
    
    public Tablet getTablet()
    {   return tablet;
    }
    
    // einde methode TabletOwner
}
