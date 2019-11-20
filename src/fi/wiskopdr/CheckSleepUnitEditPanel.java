package fi.wiskopdr;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.Hashtable;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Image;
import java.awt.Point;

import javax.swing.*;

import fi.wiskopdr.tekstobjects.*;
import fi.wiskopdr.domainmodel.Constants;
import fi.wiskopdr.formuleobjects.*;
import fi.beans.iconan.Iconan;
import fi.beans.wiskopdrbeans.*;

public class CheckSleepUnitEditPanel extends JPanel implements InteractieEditPanel, ActionListener, FocusListener, HelpButtonPanelIF
{
	// Algemene attributen 
    private Font font = new Font("SansSerif",Font.PLAIN,12);//WiskOpdr.tekstFont;
    private int scoreMax;
    private int aantalSleepObjects;
	private int aantalDoelObjects;
    private Tablet tablet;
    private boolean tabletAdded;
    private FormuleVakHouder tabletUser;
   	
    // Basis GUI
    private JPanel mainPanel;
    
    //mainPanel  //juiste antwoord
  	private JLabel titleAntwoordLabel;
  	private FormuleEditor formuleEditor;
   	
  	//mainPanel  // instellingen
  	private JLabel titleSettingsLabel;
  	private JLabel aantalSleepObjectsLabel;
  	private JTextField aantalSleepObjectsTF;
  	private JCheckBox snapToTargetCB;
  	private JLabel acceptedMargeLabel;
  	private JTextField acceptedMargeTF;
  	private JCheckBox randomizePositionsCB;
  	private JCheckBox relocateCB;
  	private JLabel aantalDoelObjectsLabel;
	private JTextField aantalDoelObjectsTF;
	private JCheckBox checkVastCB;
	private JCheckBox checkFormuleCB;
	private JCheckBox verzamelDoelCB;
  	
  	private JLabel imageKnopLabel;
  	private FormuleButton knopImageButton;
  	private Image knopImage;
  	private Dialog imageDialog;
  	private Iconan iconman;
  	private String knopImageString = "";
  	
  	// logging /nakijken
 	private JLabel titleLoggingLabel;
 	private JLabel maxScoreLabel;
 	private JTextField maxScoreTF;
 	private JCheckBox checkCB;
 	private JCheckBox teltMeeCB;
 	private JCheckBox logCB;
 	private JTextField logIDField;
 	private JTextField logIDLabelField;
 	private JLabel logIDLabelLabel;
 	private ObjectiveChoiceButton logObjectivesButton;	
  	
 	// Hulp
 	private JLabel titleHulpLabel;
 	private JCheckBox viewCB;
  	
	
	public CheckSleepUnitEditPanel()
	{
		setLayout(new BorderLayout());
		setBounds(0,0,780,480);
		
		makeGUI();
	}
	
	private void makeGUI() {
		// Main
    	mainPanel = new JPanel(new BorderLayout());
		mainPanel.setBackground(WiskOpdr.colorGray3);
		//mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10));
		
		//mainPanel  //juiste antwoord
		titleAntwoordLabel = makeLabel(WiskOpdr.rb.getString("FEV_titleAntwoordLabel"), font.deriveFont(Font.BOLD, 16));
		titleAntwoordLabel.setVisible(false);
		formuleEditor = new FormuleEditor(true);
		formuleEditor.setMultiLine(true);
		formuleEditor.setPreferredSize(new Dimension(340,200));
		formuleEditor.setVisible(false);
		
		//mainPanel  //settings
		titleSettingsLabel = makeLabel(WiskOpdr.rb.getString("settingsLabel"), font.deriveFont(Font.BOLD, 16));
		aantalSleepObjectsLabel = makeLabel(WiskOpdr.rb.getString("aantalSleepObjLabel"), font);
		aantalSleepObjectsTF = makeTextField("0", 30, 22, this);
		aantalDoelObjectsLabel = makeLabel(WiskOpdr.rb.getString("aantalDoelObjLabel"), font);
		aantalDoelObjectsTF = makeTextField("0", 30, 22, this);
		
		randomizePositionsCB = makeCheckBox(WiskOpdr.rb.getString("randomPosLabel"),false,this);
		snapToTargetCB = makeCheckBox(WiskOpdr.rb.getString("snapToTargetLabel"),true,this);//("Snap to target");
		acceptedMargeLabel = makeLabel(WiskOpdr.rb.getString("snapMargeLabel"),font);
		acceptedMargeTF = makeTextField("10",40,20,this);
		relocateCB = makeCheckBox(WiskOpdr.rb.getString("relocateCBLabel"), false, this);//("Springt terug");
		checkVastCB = makeCheckBox(WiskOpdr.rb.getString("checkVasteDoelenLabel"), true, this);
		checkFormuleCB = makeCheckBox(WiskOpdr.rb.getString("checkWaardeOpDoelLabel"), false, this);
		verzamelDoelCB = makeCheckBox(WiskOpdr.rb.getString("verzamelDoelCBLabel"), false, this);
		
		imageKnopLabel = makeLabel(WiskOpdr.rb.getString("editImageKnopLabel"), font);
		
		knopImageButton = new FormuleButton(WiskOpdr.rb.getString("klaarKnopLabel"));
		knopImageButton.setPreferredSize(new Dimension(80,22));
		knopImageButton.addActionListener(this);
				
		//mainPanel  // logging / nakijken
		titleLoggingLabel = makeLabel(WiskOpdr.rb.getString("FEV_titleLoggingLabel"),font.deriveFont(Font.BOLD, 16));
		maxScoreLabel = makeLabel(WiskOpdr.rb.getString("score"), font);
		maxScoreTF = makeTextField("0",50,22,this);
		checkCB = makeCheckBox(WiskOpdr.rb.getString("checkCBLabel"),true, null);
		teltMeeCB = makeCheckBox(WiskOpdr.rb.getString("teltMeeCBLabel"), true, null);
		logCB = makeCheckBox(WiskOpdr.rb.getString("logCBLabel"), false, this);
		logIDField = makeTextField("",50,22,this);
		logIDField.setVisible(false);
		logIDLabelLabel = makeLabel(WiskOpdr.rb.getString("TVEP_logIDLabelLabel"),font);
		logIDLabelLabel.setVisible(false);
		logIDLabelField = makeTextField("",50,22,this);
		logIDLabelField.setVisible(false);
		logObjectivesButton = new ObjectiveChoiceButton(WiskOpdr.objectives, WiskOpdr.categorieString, WiskOpdr.studentModel);
        logObjectivesButton.setPreferredSize(new Dimension(120,22));
        logObjectivesButton.setMaximumSize(new Dimension(120,22));
        logObjectivesButton.setVisible(WiskOpdr.objectives!=null);
        
        // mainPanel    //Hulp
        titleHulpLabel = makeLabel(WiskOpdr.rb.getString("FEV_titleHulpLabel"), font.deriveFont(Font.BOLD, 16));
        viewCB = makeCheckBox(WiskOpdr.rb.getString("viewCBLabel"), false, this);
		
        removeAll();
	    plaatsGUI();
	    add(mainPanel);
	}
	
	public void plaatsGUI() {
		//plaats componenten mainPanel
		Component[] r11 = {titleAntwoordLabel, 	hgl()};
		Component[] r12 = {formuleEditor, 	hgl()};
			
		Component[] k1 = {hb(r11), vst(15), hb(r12),  vst(15), vgl()};
        
		Component[] r21 = {titleSettingsLabel, 		hgl()};
		Component[] r22 = {aantalSleepObjectsLabel, 			ra(10,10), 	hgl(), aantalSleepObjectsTF	};
		Component[] r23 = {aantalDoelObjectsLabel, 			ra(10,10), 	hgl(), aantalDoelObjectsTF	};
		Component[] r24 = {randomizePositionsCB, 	hgl()};
		Component[] r25 = {snapToTargetCB, 			hgl()};
		Component[] r26 = {acceptedMargeLabel, 		ra(10,10), 	hgl(), acceptedMargeTF	};
		Component[] r27 = {relocateCB, 				hgl()};
		Component[] r28 = {checkVastCB, 			hgl()};
		Component[] r29 = {checkFormuleCB, 		hgl()};
		Component[] r210 = {verzamelDoelCB, 			hgl()};
		Component[] r211 = {imageKnopLabel, 			ra(5,5), 	hgl(), 	knopImageButton};
		
		Component[] k2 = {hb(r21), vst(15), hb(r22), vst(5), hb(r23), vst(5), hb(r24), vst(5), 
				hb(r25), vst(5), hb(r26), vst(5), hb(r27), vst(5), hb(r28), vst(5), hb(r29), vst(5), hb(r210), vst(5), hb(r211), vst(5), vgl()};
		
		Component[] r31 = {titleLoggingLabel, 	hgl()};
		Component[] r32 = {maxScoreLabel, 		ra(5,10), maxScoreTF, hgl()};
		Component[] r33 = {checkCB, 			hgl()};
		Component[] r34 = {teltMeeCB, 			hgl()};
		Component[] r35 = {logCB, 				ra(5,10), logIDField, ra(5,10), logIDLabelLabel, ra(5,10), logIDLabelField, hgl()};
		Component[] r36 = {logObjectivesButton, hgl()};
		Component[] r37 = {titleHulpLabel, 		hgl()};
		Component[] r38 = {viewCB, 				hgl()};
		
		Component[] k3 = {hb(r31), vst(15), hb(r32), vst(5), hb(r33), vst(5), hb(r34), vst(5), hb(r35), vst(10), hb(r36), vst(30), hb(r37), vst(15), hb(r38),vgl()};
		
		Component[] main = {vb(k2), hst(50), vb(k1), hst(50), vb(k3)};
		mainPanel.add(hb(main));
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
		String[] smObjectives = null;
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
		if(h.containsKey(Constants.OBJECTIVES)) smObjectives = (String[]) h.get(Constants.OBJECTIVES);
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
        logObjectivesButton.setObjectives(smObjectives);
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
    	((EditInteractiePanelDialog)SwingUtilities.getAncestorOfClass(EditInteractiePanelDialog.class,(Component)mainPanel)).pack();

    }
	
	private JCheckBox makeCheckBox (String text, boolean selected, ActionListener al) {
		JCheckBox cb = new WiskOpdrCheckbox(text);
		if(al!=null) cb.addActionListener(al);
		cb.setSelected(selected);
		return cb;
	}
	
	private JButton makeButton (String text,  ActionListener al) {
		JButton bt = new WiskOpdrButton(text);
		if(al!=null) bt.addActionListener(al);
		return bt;
	}
	
	private JLabel makeLabel (String text, Font f) {
		JLabel lb = new JLabel(text);
		lb.setFont(f);
		lb.setForeground(WiskOpdr.colorBlue1);
		return lb;
	}
	
	private JTextField makeTextField (String text, int prefWidth, int prefHeight, ActionListener al) {
		JTextField tf = new WiskOpdrTextField(text);
		tf.setPreferredSize(new Dimension(prefWidth,prefHeight));
		if(al!=null) {
			tf.addActionListener(al);
			tf.addFocusListener(this);
		}
		return tf;
	}
	
	private Box hb(Component[] c) {
		Box box = Box.createHorizontalBox();
		for(int i=0 ; c!=null && i<c.length ; i++) 
			box.add(c[i]);
		return box;
	}
	
	private Box vb(Component[] c) {
		Box box = Box.createVerticalBox();
		for(int i=0 ; c!=null && i<c.length ; i++) 
			box.add(c[i]);
		return box;
	}
	
	private Component hgl() {
		return Box.createHorizontalGlue();
	}
	
	private Component vgl() {
		return Box.createVerticalGlue();
	}
	
	private Component hst(int n) {
		return Box.createHorizontalStrut(n);
	}
	
	private Component vst(int n) {
		return Box.createVerticalStrut(n);
	}
	
	private Component ra(int w, int h) {
		return Box.createRigidArea(new Dimension(w,h));
	}
	
	private int intFromText(int defaultInt, String text) {
		int i = defaultInt;
		try {
			i = Integer.parseInt(text);
		}
		catch(NumberFormatException e) {}
		return i;
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
		String[] smObjectives = null;
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
		smObjectives = logObjectivesButton.getObjectives();
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
		if(e.getSource()==aantalSleepObjectsTF)
		{	aantalSleepObjects = Integer.parseInt(aantalSleepObjectsTF.getText());
		}
		else if(e.getSource()==aantalDoelObjectsTF)
		{	aantalDoelObjects = Integer.parseInt(aantalDoelObjectsTF.getText());
		}
		else if(e.getSource()==checkVastCB)
		{	checkFormuleCB.setSelected(!checkVastCB.isSelected());
			titleAntwoordLabel.setVisible(!checkVastCB.isSelected());
			formuleEditor.setVisible(!checkVastCB.isSelected());
			viewCB.setVisible(checkVastCB.isSelected());
			titleHulpLabel.setVisible(checkVastCB.isSelected());
			if(!checkVastCB.isSelected())viewCB.setSelected(false);
			((EditInteractiePanelDialog)SwingUtilities.getAncestorOfClass(EditInteractiePanelDialog.class,(Component)mainPanel)).pack();

		}
		else if(e.getSource()==checkFormuleCB)
		{	checkVastCB.setSelected(!checkFormuleCB.isSelected());
			formuleEditor.setVisible(!checkVastCB.isSelected());
			titleAntwoordLabel.setVisible(!checkVastCB.isSelected());
			viewCB.setVisible(checkVastCB.isSelected());
			titleHulpLabel.setVisible(checkVastCB.isSelected());
			if(!checkVastCB.isSelected())viewCB.setSelected(false);
			((EditInteractiePanelDialog)SwingUtilities.getAncestorOfClass(EditInteractiePanelDialog.class,(Component)mainPanel)).pack();

		}
		else if(e.getSource()==logCB)
	    {   logIDField.setVisible(logCB.isSelected()); 
	    	//logObjectivesButton.setVisible(logCB.isSelected());
	    	((EditInteractiePanelDialog)SwingUtilities.getAncestorOfClass(EditInteractiePanelDialog.class,(Component)mainPanel)).pack();

	    }
		else if(e.getSource()==snapToTargetCB)
		{	relocateCB.setVisible(snapToTargetCB.isSelected());
			if(!snapToTargetCB.isSelected())relocateCB.setSelected(false);
		}
		else if(e.getSource()==verzamelDoelCB)
		{
			boolean b = verzamelDoelCB.isSelected();
			checkVastCB.setEnabled(!b);
			checkFormuleCB.setEnabled(!b);
			
			if (b)
				checkVastCB.setSelected(!b);
			if (b)
				checkFormuleCB.setSelected(b);
			
			formuleEditor.setVisible(!checkVastCB.isSelected());
			titleAntwoordLabel.setVisible(!checkVastCB.isSelected());
			viewCB.setVisible(checkVastCB.isSelected());
			titleHulpLabel.setVisible(checkVastCB.isSelected());
			if(!checkVastCB.isSelected())viewCB.setSelected(false);
			((EditInteractiePanelDialog)SwingUtilities.getAncestorOfClass(EditInteractiePanelDialog.class,(Component)mainPanel)).pack();

		}
		else if(e.getSource()==knopImageButton) {   
			iconman = new Iconan(WiskOpdr.applet, mainPanel, (Hashtable)TekstImageVak.getImageMap());
			iconman.editImage(knopImageString, mainPanel, this);
	    }
	    else if(e.getSource()==iconman) {
	    	String name = e.getActionCommand();
	        if(!"".equals(name)) {
	        	knopImageString = name;
	            this.knopImage = iconman.getImage(name);
	            knopImageButton.setPopupButtonImage(knopImage);
	            int imWidth = iconman.getWidth(knopImageString);
				int imHeight = iconman.getHeight(knopImageString);
				if(imWidth == -1) imWidth = 20;
				if(imHeight == -1) imHeight = 20;
				knopImageButton.setPreferredSize(new Dimension(Math.max(imWidth,80),Math.max(imHeight,22)));
	         }
	        else {
	    		knopImageButton.setPopupButtonImage(null);
	    		knopImageButton.setCode(WiskOpdr.rb.getString("klaarKnopLabel"));
	    		knopImageButton.setPreferredSize(new Dimension(80,22));
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

	@Override
	public void focusGained(FocusEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void focusLost(FocusEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void showHelpButtons(boolean b) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String geefHelpURL() {
		// TODO Auto-generated method stub
		return null;
	}
    
    // einde methode TabletOwner
}
