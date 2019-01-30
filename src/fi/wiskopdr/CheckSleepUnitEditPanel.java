package fi.wiskopdr;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Image;
import java.awt.Point;

import javax.swing.*;

import fi.wiskopdr.tekstobjects.*;
import fi.wiskopdr.formuleobjects.*;
import fi.beans.iconan.Iconan;
import fi.beans.wiskopdrbeans.*;

public class CheckSleepUnitEditPanel extends JPanel implements InteractieEditPanel, ActionListener, TabletOwner
{
	private TekstEditor tekstEditor;
	private Font font = new Font("SansSerif",Font.PLAIN,12);
	
	private int aantalSleepObjects;
	private int aantalDoelObjects;
	//private InteractiePanel[] sleepObjects;
	
	private JLabel aantalSleepObjectsLabel;
	private JTextField aantalSleepObjectsTF;
	
	private JLabel aantalDoelObjectsLabel;
	private JTextField aantalDoelObjectsTF;
	
	private JLabel maxScoreLabel;
	private JTextField maxScoreTF;
	
	private JCheckBox randomizePositionsCB;
	private JCheckBox snapToTargetCB;
	private JLabel acceptedMargeLabel;
	private JTextField acceptedMargeTF;
	
	private JCheckBox checkVastCB;
	private JCheckBox checkFormuleCB;
	
	private boolean checkFormule;
	
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
	
	private JCheckBox relocateCB;
	private JCheckBox viewCB;
	
	private JCheckBox verzamelDoelCB;
	
	//private int scoreMax;
	//private boolean randomizePositions;
	//private boolean snapToTarget;
	//private int acceptedMarge;
	
	private FormuleButton knopImageButton;
	private Dialog imageDialog;
	private Iconan iconman;
	private String knopImageString = "";
	private Image knopImage;
	
	public CheckSleepUnitEditPanel()
	{
		setLayout(null);
		setBounds(0,0,780,480);
		tekstEditor = new TekstEditor(true,false,false);
		tekstEditor.setBounds(300,130,465,365);
		//add(tekstEditor);
		
		aantalSleepObjectsLabel = new JLabel(WiskOpdr.rb.getString("aantalSleepObjLabel"));//("Aantal sleep-objecten");
		aantalSleepObjectsLabel.setBounds(10,30,180,20);
		aantalSleepObjectsLabel.setFont(font);
		add(aantalSleepObjectsLabel);
		
		aantalSleepObjectsTF = new JTextField("0");
		aantalSleepObjectsTF.setBounds(190,30,40,20);
		aantalSleepObjectsTF.addActionListener(this);
		add(aantalSleepObjectsTF);
		
		aantalDoelObjectsLabel = new JLabel(WiskOpdr.rb.getString("aantalDoelObjLabel"));//("Aantal doel-objecten");
		aantalDoelObjectsLabel.setBounds(10,60,180,20);
		aantalDoelObjectsLabel.setFont(font);
		add(aantalDoelObjectsLabel);
		
		aantalDoelObjectsTF = new JTextField("0");
		aantalDoelObjectsTF.setBounds(190,60,40,20);
		aantalDoelObjectsTF.addActionListener(this);
		add(aantalDoelObjectsTF);
		
		maxScoreLabel = new JLabel(WiskOpdr.rb.getString("score"));//("Score");
		maxScoreLabel.setBounds(300,30,80,20);
		maxScoreLabel.setFont(font);
		add(maxScoreLabel);
		
		maxScoreTF = new JTextField("0");
		maxScoreTF.setBounds(380,30,40,20);
		maxScoreTF.addActionListener(this);
		add(maxScoreTF);
		
		randomizePositionsCB = new JCheckBox(WiskOpdr.rb.getString("randomPosLabel"));//("Randomiseer posities");
		randomizePositionsCB.setBounds(300,70,280,20);
		randomizePositionsCB.setOpaque(false);
		randomizePositionsCB.setFont(font);
		add(randomizePositionsCB);
		
		snapToTargetCB = new JCheckBox(WiskOpdr.rb.getString("snapToTargetLabel"));//("Snap to target");
		snapToTargetCB.addActionListener(this);
		snapToTargetCB.setBounds(300,100,280,20);
		snapToTargetCB.setOpaque(false);
		snapToTargetCB.setFont(font);
		add(snapToTargetCB);
		
		acceptedMargeLabel = new JLabel(WiskOpdr.rb.getString("snapMargeLabel"));//("Afwijking");
		acceptedMargeLabel.setBounds(300,130,80,20);
		acceptedMargeLabel.setFont(font);
		add(acceptedMargeLabel);
		
		acceptedMargeTF = new JTextField("10");
		acceptedMargeTF.setBounds(380,130,40,20);
		acceptedMargeTF.addActionListener(this);
		add(acceptedMargeTF);
		
		checkVastCB = new JCheckBox(WiskOpdr.rb.getString("checkVasteDoelenLabel"));//("Check op vaste doelen voor sleepobjecten");
		checkVastCB.setBounds(300,170,280,20);
		checkVastCB.addActionListener(this);
		checkVastCB.setOpaque(false);
		checkVastCB.setFont(font);
		checkVastCB.setSelected(true);
		add(checkVastCB);
		
		checkFormuleCB = new JCheckBox(WiskOpdr.rb.getString("checkWaardeOpDoelLabel"));//("Check met waarden sleepobjecten");
		checkFormuleCB.setBounds(300,200,380,20);
		checkFormuleCB.addActionListener(this);
		checkFormuleCB.setOpaque(false);
		checkFormuleCB.setFont(font);
		add(checkFormuleCB);
		
		formuleEditor = new FormuleEditor(true);
		formuleEditor.setMultiLine(true);
		formuleEditor.setBounds(300,230,480,200);
		formuleEditor.setVisible(false);
        add(formuleEditor);
        
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
		
		logObjectivesButton = new ObjectiveChoiceButton(WiskOpdr.objectives, WiskOpdr.categorieString);
        logObjectivesButton.setVisible(WiskOpdr.objectives!=null);
        logObjectivesButton.setBounds(600,5,120,20);
        if(WiskOpdr.objectives!=null)add(logObjectivesButton);
		
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
		
		relocateCB = new JCheckBox(WiskOpdr.rb.getString("relocateCBLabel"));//("Springt terug");
		relocateCB.setBounds(580,100,280,20);
		relocateCB.setOpaque(false);
		relocateCB.setFont(font);
		relocateCB.setSelected(false);
		relocateCB.setVisible(false);
		add(relocateCB);
		
		viewCB = new JCheckBox(WiskOpdr.rb.getString("viewCBLabel"));//("View");
		viewCB.setBounds(580,170,280,20);
		viewCB.setOpaque(false);
		viewCB.setFont(font);
		viewCB.setSelected(false);
		viewCB.setVisible(true);
		add(viewCB);
		
		verzamelDoelCB = new JCheckBox(WiskOpdr.rb.getString("verzamelDoelCBLabel"));
		verzamelDoelCB.addActionListener(this);
		verzamelDoelCB.setBounds(10,90,240,20);
		verzamelDoelCB.setOpaque(false);
		verzamelDoelCB.setFont(font);
		verzamelDoelCB.setSelected(false);
		verzamelDoelCB.setVisible(true);
		add(verzamelDoelCB);
		
		knopImageButton = new FormuleButton(WiskOpdr.rb.getString("klaarKnopLabel"));
		knopImageButton.setBounds(10,120,80,20);
		knopImageButton.addActionListener(this);
		add(knopImageButton);
	}
	
	
	public void setEditState(Hashtable h)
	{
		int aantalSleepObjects = 0;
		int aantalDoelObjects = 0;
	    int scoreMax = 0;
		boolean randomizePositions = false;
		boolean snapToTarget = false;
		int acceptedMarge = 10;
		boolean checkFormule = false;
		String formuleString = "$f@";
		String[] formuleStrings = null;
		boolean logOption = false;
		String logID = "";
		boolean check = true;
		boolean teltMee = true;
		boolean relocate = false;
		boolean view = false;
		boolean verzamelDoel = false;
		boolean[][] logObjectives = null;
		String knopImageString = "";
		
	    if(h.containsKey("aantalSleepObjects")) aantalSleepObjects = ((Integer)h.get("aantalSleepObjects")).intValue();
	    if(h.containsKey("aantalDoelObjects")) aantalDoelObjects = ((Integer)h.get("aantalDoelObjects")).intValue();
	    if(h.containsKey("scoreMax")) scoreMax = ((Integer)h.get("scoreMax")).intValue();
	    if(h.containsKey("randomizePositions")) randomizePositions = ((Boolean)h.get("randomizePositions")).booleanValue();
	    if(h.containsKey("snapToTarget")) snapToTarget = ((Boolean)h.get("snapToTarget")).booleanValue();
	    if(h.containsKey("acceptedMarge")) acceptedMarge = ((Integer)h.get("acceptedMarge")).intValue();
	    if(h.containsKey("checkFormule")) checkFormule = ((Boolean)h.get("checkFormule")).booleanValue();
	    if(h.containsKey("formuleString")) formuleString = (String)h.get("formuleString");
	    if(h.containsKey("formuleStrings")) formuleStrings = (String[])h.get("formuleStrings");
	    if(h.containsKey("logOption")) logOption = ((Boolean)h.get("logOption")).booleanValue();
		if(h.containsKey("logID")) logID = (String)h.get("logID");
		if(h.containsKey("check")) check = ((Boolean)h.get("check")).booleanValue();
		if(h.containsKey("teltMee")) teltMee = ((Boolean)h.get("teltMee")).booleanValue();
		if(h.containsKey("relocate")) relocate = ((Boolean)h.get("relocate")).booleanValue();
		if(h.containsKey("view")) view = ((Boolean)h.get("view")).booleanValue();
		if(h.containsKey("verzamelDoel")) verzamelDoel = ((Boolean)h.get("verzamelDoel")).booleanValue();
		if(h.containsKey("logObjectives")) logObjectives = (boolean[][])h.get("logObjectives");
		if(h.containsKey("knopImageString")) knopImageString = (String)h.get("knopImageString");
		
		this.knopImageString = knopImageString;
	    this.aantalSleepObjects = aantalSleepObjects;
	    this.aantalDoelObjects = aantalDoelObjects;
	    aantalSleepObjectsTF.setText(""+aantalSleepObjects);
	    aantalDoelObjectsTF.setText(""+aantalDoelObjects);
	    maxScoreTF.setText(""+scoreMax);
	    randomizePositionsCB.setSelected(randomizePositions);
	    snapToTargetCB.setSelected(snapToTarget);
	    acceptedMargeTF.setText(""+acceptedMarge);
	    checkVastCB.setSelected(!checkFormule);
	    checkFormuleCB.setSelected(checkFormule);
	    formuleEditor.geefFormuleVak().vulVak(formuleString);
	    if(formuleStrings!=null)formuleEditor.zetRegels(formuleStrings);
	    formuleEditor.setVisible(checkFormule);
	    
	    logCB.setSelected(logOption);
        logIDField.setVisible(logOption);
        //logObjectivesButton.setVisible(logOption);
        logIDField.setText(logID);
        logObjectivesButton.setChoices(logObjectives);
        checkCB.setSelected(check);
        teltMeeCB.setSelected(teltMee);
        relocateCB.setVisible(snapToTarget);
        relocateCB.setSelected(relocate && snapToTarget);
        viewCB.setVisible(!checkFormule);
        viewCB.setSelected(view && !checkFormule);
        verzamelDoelCB.setSelected(verzamelDoel);
        
        checkVastCB.setEnabled(!verzamelDoel);
		
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
	    int aantalSleepObjects;
	    int aantalDoelObjects;
	    int scoreMax = 0;
	    int[][] scoreMaxObjectives = null;
	    boolean randomizePositions = false;
		boolean snapToTarget = false;
		int acceptedMarge = 0;
		boolean checkFormule = false;
		String formuleString = "$f@";
		String[] formuleStrings = null;
		boolean logOption = false;
		String logID = "";
		boolean[][] logObjectives = null;
		boolean check = true;
		boolean teltMee = true;
		boolean relocate = false;
		boolean view = false;
		boolean verzamelDoel = false;
		String knopImageString = "";
		
		knopImageString = this.knopImageString;
		aantalSleepObjects = Integer.parseInt(aantalSleepObjectsTF.getText());
		aantalDoelObjects = Integer.parseInt(aantalDoelObjectsTF.getText());
		scoreMax = Integer.parseInt(maxScoreTF.getText());
	    randomizePositions = randomizePositionsCB.isSelected();
	    snapToTarget = snapToTargetCB.isSelected();
	    acceptedMarge = Integer.parseInt(acceptedMargeTF.getText());
	    checkFormule = checkFormuleCB.isSelected();
	    formuleString = formuleEditor.geefFormuleVak().toString();
	    formuleStrings = formuleEditor.geefRegels();
	    logOption = logCB.isSelected();
		logID = logIDField.getText();
		logObjectives = logObjectivesButton.getChoices();
		check = checkCB.isSelected();
		teltMee = teltMeeCB.isSelected();
		relocate = relocateCB.isSelected();
		view = viewCB.isSelected();
		verzamelDoel = verzamelDoelCB.isSelected();
		
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
		h.put("aantalSleepObjects", new Integer(aantalSleepObjects));
		h.put("aantalDoelObjects", new Integer(aantalDoelObjects));
		h.put("scoreMax", new Integer(scoreMax));
		h.put("randomizePositions", new Boolean(randomizePositions));
		h.put("snapToTarget", new Boolean(snapToTarget));
		h.put("acceptedMarge", new Integer(acceptedMarge));
		h.put("checkFormule", new Boolean(checkFormule));
		h.put("formuleString", formuleString);
		h.put("formuleStrings", formuleStrings);
		h.put("logOption",new Boolean(logOption));
		h.put("logID",logID);
		h.put("check",new Boolean(check));
		h.put("teltMee",new Boolean(teltMee));
		h.put("relocate",new Boolean(relocate));
		h.put("view",new Boolean(view));
		h.put("verzamelDoel",new Boolean(verzamelDoel));
		if(logObjectives!=null)
        {	h.put("logObjectives",logObjectives);
        	h.put("scoreMaxObjectives",scoreMaxObjectives);
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
		if(e.getSource()==aantalSleepObjectsTF)
		{	aantalSleepObjects = Integer.parseInt(aantalSleepObjectsTF.getText());
		}
		else if(e.getSource()==aantalDoelObjectsTF)
		{	aantalDoelObjects = Integer.parseInt(aantalDoelObjectsTF.getText());
		}
		else if(e.getSource()==checkVastCB)
		{	checkFormuleCB.setSelected(!checkVastCB.isSelected());
			formuleEditor.setVisible(!checkVastCB.isSelected());
			viewCB.setVisible(checkVastCB.isSelected());
			if(!checkVastCB.isSelected())viewCB.setSelected(false);
		}
		else if(e.getSource()==checkFormuleCB)
		{	checkVastCB.setSelected(!checkFormuleCB.isSelected());
			formuleEditor.setVisible(!checkVastCB.isSelected());
			viewCB.setVisible(checkVastCB.isSelected());
			if(!checkVastCB.isSelected())viewCB.setSelected(false);
		}
		else if(e.getSource()==logCB)
	    {   logIDField.setVisible(logCB.isSelected()); 
	    	//logObjectivesButton.setVisible(logCB.isSelected());
	    }
		else if(e.getSource()==snapToTargetCB)
		{	relocateCB.setVisible(snapToTargetCB.isSelected());
			if(!snapToTargetCB.isSelected())relocateCB.setSelected(false);
		}
		else if(e.getSource()==verzamelDoelCB)
		{
			boolean b = verzamelDoelCB.isSelected();
			checkVastCB.setEnabled(!b);
			
			if (b)
				checkVastCB.setSelected(!b);
			if (b)
				checkFormuleCB.setSelected(b);
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
				if(imWidth == -1) imWidth = 80;
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
