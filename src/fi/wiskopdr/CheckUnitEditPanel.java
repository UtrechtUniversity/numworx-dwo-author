package fi.wiskopdr;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.Hashtable;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Image;

import javax.swing.*;

import fi.wiskopdr.domainmodel.Constants;
import fi.wiskopdr.formuleobjects.*;
import fi.wiskopdr.tekstobjects.TekstImageVak;
import fi.beans.iconan.Iconan;
import fi.beans.wiskopdrbeans.*;

public class CheckUnitEditPanel extends JPanel implements InteractieEditPanel, ActionListener, FocusListener
{
	private FormuleEditor formuleEditor;
	private Font font = new Font("SansSerif",Font.PLAIN,12);
	
	private int aantalSelectables;
	private int aantalSelectablesMax = 30;
	private InteractiePanel[] selectables;
	
	private JLabel aantalSelectablesLabel;
	private JTextField aantalSelectablesTF;
	
	private JCheckBox[] selectableCheckboxes;
	private JLabel[] selectableLabels;
	
	private JLabel maxScoreLabel;
	private JTextField maxScoreTF;
	
	private JCheckBox randomizePositionsCB;
	private JCheckBox multiSelectionsCB;
	private JCheckBox checkFormuleCB;
	
	
	private int scoreMax;
	private boolean randomizePositions;
	private boolean multiselections;
	
	private JCheckBox logCB;
	private JTextField logIDField;
	private JCheckBox checkCB;
	private JCheckBox teltMeeCB;
	private ObjectiveChoiceButton logObjectivesButton;
	private ObjectiveChoiceButton[] logMisconceptionsButtons;
	private boolean[][][] logMisconceptions;
	
	private FormuleButton knopImageButton;
	private Dialog imageDialog;
	private Iconan iconman;
	private String knopImageString = "";
	private Image knopImage;
	
	public CheckUnitEditPanel()
	{
		setLayout(null);
		setBounds(0,0,780,480);
		
		selectableCheckboxes = new JCheckBox[aantalSelectablesMax];
		selectableLabels = new JLabel[aantalSelectablesMax];
		logMisconceptionsButtons = new ObjectiveChoiceButton[aantalSelectablesMax];
		
		formuleEditor = new FormuleEditor(true);
		formuleEditor.setMultiLine(true);
		formuleEditor.setBounds(300,180,480,200);
		formuleEditor.setVisible(false);
        add(formuleEditor);
		
		aantalSelectablesLabel = new JLabel(WiskOpdr.rb.getString("aantalSelectieObjLabel"));//"Aantal selectie-objecten");
		aantalSelectablesLabel.setBounds(10,50,180,20);
		aantalSelectablesLabel.setFont(font);
		add(aantalSelectablesLabel);
		
		aantalSelectablesTF = new JTextField("0");
		aantalSelectablesTF.setBounds(190,50,40,20);
		aantalSelectablesTF.addActionListener(this);
		aantalSelectablesTF.addFocusListener(this);
		add(aantalSelectablesTF);
		
		maxScoreLabel = new JLabel(WiskOpdr.rb.getString("score"));//Score");
		maxScoreLabel.setBounds(300,50,80,20);
		maxScoreLabel.setFont(font);
		add(maxScoreLabel);
		
		maxScoreTF = new JTextField("0");
		maxScoreTF.setBounds(380,50,40,20);
		maxScoreTF.addActionListener(this);
		add(maxScoreTF);
		
		randomizePositionsCB = new JCheckBox(WiskOpdr.rb.getString("randomPosLabel"));//("Randomiseer posities");
		randomizePositionsCB.setBounds(300,90,280,20);
		randomizePositionsCB.setOpaque(false);
		randomizePositionsCB.setFont(font);
		add(randomizePositionsCB);
		
		multiSelectionsCB = new JCheckBox(WiskOpdr.rb.getString("meervSelectiesLabel"));//("Meervoudige selecties mogelijk");
		multiSelectionsCB.setBounds(300,120,280,20);
		multiSelectionsCB.setOpaque(false);
		multiSelectionsCB.setFont(font);
		add(multiSelectionsCB);
		
		checkFormuleCB = new JCheckBox(WiskOpdr.rb.getString("checkViaFormuleLabel"));//("Check formule");
		checkFormuleCB.setBounds(300,150,280,20);
		checkFormuleCB.addActionListener(this);
		checkFormuleCB.setOpaque(false);
		checkFormuleCB.setFont(font);
		add(checkFormuleCB);
		

        logCB = new JCheckBox(WiskOpdr.rb.getString("logCBLabel"));
        logCB.setBounds(450,5,70,20);
        logCB.addActionListener(this);
        logCB.setOpaque(false);
        logCB.setFont(font);
		add(logCB);
		
		logIDField = new JTextField("0");
		logIDField.setBounds(520,5,60,20);
		logIDField.addActionListener(this);
		logIDField.setVisible(false);
		add(logIDField);
		
		checkCB = new JCheckBox(WiskOpdr.rb.getString("checkCBLabel"));
		checkCB.setBounds(5,5,200,20);
		checkCB.addActionListener(this);
		checkCB.setOpaque(false);
		checkCB.setFont(font);
		checkCB.setSelected(true);
		add(checkCB);
		
		teltMeeCB = new JCheckBox(WiskOpdr.rb.getString("teltMeeCBLabel"));
		teltMeeCB.setBounds(225,5,200,20);
		teltMeeCB.addActionListener(this);
		teltMeeCB.setOpaque(false);
		teltMeeCB.setFont(font);
		teltMeeCB.setSelected(true);
		add(teltMeeCB);
		
		logObjectivesButton = new ObjectiveChoiceButton(WiskOpdr.objectives, WiskOpdr.categorieString, WiskOpdr.studentModel);
        logObjectivesButton.setVisible(WiskOpdr.objectives!=null);
        logObjectivesButton.setBounds(600,5,120,20);
        if(WiskOpdr.objectives!=null)add(logObjectivesButton);
                
        knopImageButton = new FormuleButton(WiskOpdr.rb.getString("klaarKnopLabel"));
		knopImageButton.setBounds(550,50,80,20);
		knopImageButton.addActionListener(this);
		add(knopImageButton);
	}
	
	public void maakCheckboxes()
	{
		if(WiskOpdr.misconceptions!=null)
			logMisconceptions = new boolean[aantalSelectables][][];
				
		for(int i=0 ; i<aantalSelectables ; i++)
		{
		    if(selectableLabels[i]==null)
		    {   selectableLabels[i] = new JLabel("Nr "+(i+1));
		        selectableLabels[i].setBounds(10,70+i*25,50,20);
		        //add(selectableLabels[i]);
		    }
		    if(selectableCheckboxes[i]==null)
            {   selectableCheckboxes[i] = new JCheckBox("Nr "+(i+1));
                selectableCheckboxes[i].setOpaque(false);
                selectableCheckboxes[i].setBounds(10,80+i*25,60,20);
                add(selectableCheckboxes[i],0);
            }
		    if(logMisconceptionsButtons[i]==null)
		    {
		    	logMisconceptionsButtons[i] = new ObjectiveChoiceButton(WiskOpdr.rb.getString("OPT_misconceptions"),WiskOpdr.misconceptions, WiskOpdr.mccCategorieString);
		        logMisconceptionsButtons[i].setBounds(100,80+i*25,100,20);
		        if(WiskOpdr.misconceptions!=null)add(logMisconceptionsButtons[i]);
		    }
		}
		enableMisconceptions();
		repaint();
	}
	
	public void verwijderCheckboxes()
	{
		for(int i=0 ; i<aantalSelectables ; i++)
		{
		    if(selectableLabels[i]!=null) 
		    {	remove(selectableLabels[i]);
		    	selectableLabels[i] = null;
		    	remove(selectableCheckboxes[i]);
		    	selectableCheckboxes[i] = null;
		    	if(logMisconceptionsButtons[i]!=null) 
		    	{	remove(logMisconceptionsButtons[i]);
		    		logMisconceptionsButtons[i] = null;
		    	}
		    }
		}	
		aantalSelectables = 0;
		repaint();
	}
	
	private void enableMisconceptions()
	{
		for(int i=0 ; i<aantalSelectables ; i++)
		{
			boolean visible = WiskOpdr.misconceptions!=null 
					&& !checkFormuleCB.isSelected()
					&& !multiSelectionsCB.isSelected()
					&& checkCB.isSelected();
		   	if(logMisconceptionsButtons[i]!=null)
		   		logMisconceptionsButtons[i].setVisible(visible);
		   
		}	
	}
	
	public void setEditState(Hashtable h)
	{
	    boolean[] juisteSelecties = null;
	    int scoreMax = 0;
		boolean randomizePositions = false;
		boolean multiSelections = false;
		boolean logOption = false;
		String logID = "";
		boolean[][] logObjectives = null;
		String[] smObjectives = null;
		boolean check = true;
		boolean teltMee = true;
		boolean checkFormule = false;
		String[] formuleStrings = null;
		String knopImageString = "";
		boolean[][][] logMisconceptions = null;
				
	    if(h.containsKey("juisteSelecties")) juisteSelecties = (boolean[])h.get("juisteSelecties");
	    if(h.containsKey("scoreMax")) scoreMax = ((Integer)h.get("scoreMax")).intValue();
	    if(h.containsKey("randomizePositions")) randomizePositions = ((Boolean)h.get("randomizePositions")).booleanValue();
	    if(h.containsKey("multiSelections")) multiSelections = ((Boolean)h.get("multiSelections")).booleanValue();
	    if(h.containsKey("logOption")) logOption = ((Boolean)h.get("logOption")).booleanValue();
		if(h.containsKey("logID")) logID = (String)h.get("logID");
		if(h.containsKey("check")) check = ((Boolean)h.get("check")).booleanValue();
		if(h.containsKey("teltMee")) teltMee = ((Boolean)h.get("teltMee")).booleanValue();
		if(h.containsKey("checkFormule")) checkFormule = ((Boolean)h.get("checkFormule")).booleanValue();
		if(h.containsKey("formuleStrings")) formuleStrings = (String[])h.get("formuleStrings");
		if(h.containsKey("logObjectives")) logObjectives = (boolean[][])h.get("logObjectives");
		if(h.containsKey(Constants.OBJECTIVES)) smObjectives = (String[]) h.get(Constants.OBJECTIVES);
		if(h.containsKey("knopImageString")) knopImageString = (String)h.get("knopImageString");
		if(h.containsKey("logMisconceptions")) logMisconceptions = (boolean[][][])h.get("logMisconceptions");
		
	    if(juisteSelecties==null) return;
	    
	    aantalSelectables = juisteSelecties.length;
	    aantalSelectablesTF.setText(""+aantalSelectables);
	    maxScoreTF.setText(""+scoreMax);
	    randomizePositionsCB.setSelected(randomizePositions);
	    multiSelectionsCB.setSelected(multiSelections);
	    this.logMisconceptions = logMisconceptions;
	    
	    for(int i=0 ; i<aantalSelectables ; i++)
		{
		    selectableCheckboxes[i] = new JCheckBox("Nr "+(i+1));
		    selectableCheckboxes[i].setSelected(juisteSelecties[i]);
            selectableCheckboxes[i].setOpaque(false);
            selectableCheckboxes[i].setBounds(10,80+i*25,60,20);
            add(selectableCheckboxes[i],0);
            
            if(logMisconceptions!=null)
            {   logMisconceptionsButtons[i] = new ObjectiveChoiceButton(WiskOpdr.rb.getString("OPT_misconceptions"),WiskOpdr.misconceptions, WiskOpdr.mccCategorieString);
			    logMisconceptionsButtons[i].setBounds(100,80+i*25,100,20);
			    logMisconceptionsButtons[i].setChoices(logMisconceptions[i]);
			    add(logMisconceptionsButtons[i]);
		    }
		}
	    enableMisconceptions();
	    
	    logCB.setSelected(logOption);
        logIDField.setVisible(logOption);
        //logObjectivesButton.setVisible(logOption);
        logIDField.setText(logID);
        logObjectivesButton.setChoices(logObjectives);
        logObjectivesButton.setObjectives(smObjectives);
        checkCB.setSelected(check);
        teltMeeCB.setSelected(teltMee);
        checkFormuleCB.setSelected(checkFormule);
        if(formuleStrings!=null)formuleEditor.zetRegels(formuleStrings);
        formuleEditor.setVisible(checkFormule);
        
        knopImageButton.setPopupButtonImage(knopImage);
    	iconman = new Iconan(WiskOpdr.applet, (Component)this, (Hashtable)TekstImageVak.getImageMap());
    	if(knopImageString!=null && !"".equals(knopImageString)) {
    		knopImage = iconman.getImage(knopImageString);
    		knopImageButton.setPopupButtonImage(knopImage);
    	}
    	else {
    		knopImageButton.setCode(WiskOpdr.rb.getString("klaarKnopLabel"));
    	}
    	this.knopImageString = knopImageString;
	}
	
	public Hashtable getEditState()
	{
	    boolean[] juisteSelecties = null;
	    int scoreMax = 0;
	    int[][] scoreMaxObjectives = null;
	    boolean randomizePositions = false;
		boolean multiSelections = false;
		boolean logOption = false;
		String logID = "";
		boolean[][] logObjectives = null;
		String[] smObjectives = null;
		boolean check = true;
		boolean teltMee = true;
		boolean checkFormule = false;
		String[] formuleStrings = null;
		String knopImageString = "";
		boolean[][][] logMisconceptions = null;
	    
		knopImageString = this.knopImageString;
	    juisteSelecties = new boolean[aantalSelectables];
	    for(int i=0 ; i<aantalSelectables ; i++)
	    {  juisteSelecties[i] = selectableCheckboxes[i].isSelected();
	    }
	    
	    scoreMax = Integer.parseInt(maxScoreTF.getText());
	    randomizePositions = randomizePositionsCB.isSelected();
	    multiSelections = multiSelectionsCB.isSelected();
	    logOption = logCB.isSelected();
		logID = logIDField.getText();
		logObjectives = logObjectivesButton.getChoices();
		smObjectives = logObjectivesButton.getObjectives();
		check = checkCB.isSelected();
		teltMee = teltMeeCB.isSelected();
		checkFormule = checkFormuleCB.isSelected();
		formuleStrings = formuleEditor.geefRegels();
		logMisconceptions = this.logMisconceptions;
		
		if(logObjectives!=null)
		{	scoreMaxObjectives = new int[logObjectives.length][];
			for(int j=0 ; j<scoreMaxObjectives.length; j++)
			{	scoreMaxObjectives[j] = new int[logObjectives[j].length];
				for(int i=0 ; i<scoreMaxObjectives[j].length ; i++)
				{	if(logObjectives[j][i]) scoreMaxObjectives[j][i] = scoreMax;
				}
			}
		}
		
		if(logMisconceptions!=null)
		{	for(int i=0 ; i<aantalSelectables ; i++)
			{	logMisconceptions[i] = logMisconceptionsButtons[i].getChoices();
			}
		}
	    	    
		Hashtable h = new Hashtable();
		h.put("juisteSelecties", juisteSelecties);
		h.put("scoreMax", new Integer(scoreMax));
		h.put("randomizePositions", new Boolean(randomizePositions));
		h.put("multiSelections", new Boolean(multiSelections));
		h.put("logOption",new Boolean(logOption));
		h.put("logID",logID);
		h.put("check",new Boolean(check));
		h.put("teltMee",new Boolean(teltMee));
		h.put("checkFormule",new Boolean(checkFormule));
		h.put("formuleStrings", formuleStrings);
		if(logObjectives!=null)
	    {	h.put("logObjectives",logObjectives);
	    	h.put("scoreMaxObjectives",scoreMaxObjectives);
            try {
              h.put(Constants.OBJECTIVES, smObjectives);
            } catch(Exception e) {}
	    }
		if(logMisconceptions!=null)
        {	h.put("logMisconceptions",logMisconceptions);
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
    	if(iconman==null)
			iconman = new Iconan(WiskOpdr.applet, this, (Hashtable)TekstImageVak.getImageMap());
		iconman.editImage(knopImageString, this, this);
		
//        if(imageDialog == null)
//        {
//        	
//        	Frame f = JOptionPane.getFrameForComponent(this);
//			imageDialog = new Dialog(f,"title", true);
//			imageDialog.setLayout(new BorderLayout());
//			iconman = new Iconan(WiskOpdr.applet, (Component)this, (Hashtable)TekstImageVak.getImageMap());
//            imageDialog.add(iconman);
//            imageDialog.pack();
//            iconman.addActionListener(this);
//        }
//        iconman.select(knopImageString);
//        imageDialog.setVisible(true);
    }    
    
	public void actionPerformed(ActionEvent e)
	{
		if(e.getSource()==aantalSelectablesTF)
		{
			int aantal = Math.min(aantalSelectablesMax, Integer.parseInt(aantalSelectablesTF.getText()));
			if(aantal != aantalSelectables)
			{	verwijderCheckboxes();
				aantalSelectables = aantal;
				maakCheckboxes();
			}
			
		}
		else if(e.getSource()==logCB)
	    {   logIDField.setVisible(logCB.isSelected());   
	    	//logObjectivesButton.setVisible(logCB.isSelected());
	    }
		else if(e.getSource()==checkFormuleCB)
	    {   formuleEditor.setVisible(checkFormuleCB.isSelected()); 
	    	enableMisconceptions();
	    }
		else if(e.getSource()==multiSelectionsCB)
	    {   enableMisconceptions();
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

	@Override
	public void focusGained(FocusEvent e) {
		
	}

	@Override
	public void focusLost(FocusEvent e) {
		if(e.getSource()==aantalSelectablesTF)
		{
			int aantal = Math.min(aantalSelectablesMax, Integer.parseInt(aantalSelectablesTF.getText()));
			if(aantal != aantalSelectables)
			{	verwijderCheckboxes();
				aantalSelectables = aantal;
				maakCheckboxes();
			}
		}
	}
}
