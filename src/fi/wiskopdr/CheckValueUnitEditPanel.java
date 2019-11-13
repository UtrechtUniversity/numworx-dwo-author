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

public class CheckValueUnitEditPanel extends JPanel implements InteractieEditPanel, ActionListener, FocusListener
{
	// Algemene attributen 
    private Font font = new Font("SansSerif",Font.PLAIN,12);//WiskOpdr.tekstFont;
    private int aantalValueObjects;
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
  	private JLabel aantalValueObjectsLabel;
  	private JTextField aantalValueObjectsTF;
  	private JCheckBox checkAfzonderlijkCB;
	private JCheckBox checkSamenCB;
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
	
	
//	private boolean checkSamen;
//	private String formuleString;
//	private String[] formuleStrings;
	
	 
    
	
	public CheckValueUnitEditPanel()
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
		
		//mainPanel  //settings
		titleSettingsLabel = makeLabel(WiskOpdr.rb.getString("settingsLabel"), font.deriveFont(Font.BOLD, 16));
		aantalValueObjectsLabel = makeLabel(WiskOpdr.rb.getString("aantalValueObjectenLabel"), font);
		aantalValueObjectsTF = makeTextField("0", 30, 22, this);
		checkAfzonderlijkCB = makeCheckBox(WiskOpdr.rb.getString("checkAfzonderlijkLabel"), false, this);
		checkSamenCB = makeCheckBox(WiskOpdr.rb.getString("checkOpSamenhangLabel"), true, this);
		
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
        titleHulpLabel.setVisible(false);
        viewCB = makeCheckBox(WiskOpdr.rb.getString("viewCBLabel"), false, this);
        viewCB.setVisible(false);		
		

        
        removeAll();
	    plaatsGUI();
	    add(mainPanel);
	}
	
	private void plaatsGUI() {
		//plaats componenten mainPanel
		Component[] r11 = {titleAntwoordLabel, 	hgl()};
		Component[] r12 = {formuleEditor, 	hgl()};
			
		Component[] k1 = {hb(r11), vst(15), hb(r12),  vst(15), vgl()};
        
		Component[] r21 = {titleSettingsLabel, 		hgl()};
		Component[] r22 = {aantalValueObjectsLabel, 			ra(10,10), 	hgl(), aantalValueObjectsTF	};
		Component[] r23 = {checkAfzonderlijkCB, 			hgl()};
		Component[] r24 = {checkSamenCB, 		hgl()};
		Component[] r25 = {imageKnopLabel, 			ra(5,5), 	hgl(), 	knopImageButton};
		
		Component[] k2 = {hb(r21), vst(15), hb(r22), vst(5), hb(r23), vst(5), hb(r24), vst(5), hb(r25), vst(5), vgl()};
		
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
        titleHulpLabel.setVisible(!checkSamen);
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
    	((EditInteractiePanelDialog)SwingUtilities.getAncestorOfClass(EditInteractiePanelDialog.class,(Component)mainPanel)).pack();

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
		if(e.getSource()==aantalValueObjectsTF)
		{	aantalValueObjects = Integer.parseInt(aantalValueObjectsTF.getText());
		}
		else if(e.getSource()==checkAfzonderlijkCB)
		{	checkSamenCB.setSelected(!checkAfzonderlijkCB.isSelected());
			formuleEditor.setVisible(!checkAfzonderlijkCB.isSelected());
			viewCB.setVisible(checkAfzonderlijkCB.isSelected());
			titleHulpLabel.setVisible(checkAfzonderlijkCB.isSelected());
			if(!checkAfzonderlijkCB.isSelected())viewCB.setSelected(false);
			((EditInteractiePanelDialog)SwingUtilities.getAncestorOfClass(EditInteractiePanelDialog.class,(Component)mainPanel)).pack();

		}
		else if(e.getSource()==checkSamenCB)
		{	checkAfzonderlijkCB.setSelected(!checkSamenCB.isSelected());
			formuleEditor.setVisible(!checkAfzonderlijkCB.isSelected());
			viewCB.setVisible(checkAfzonderlijkCB.isSelected());
			titleHulpLabel.setVisible(checkAfzonderlijkCB.isSelected());
			if(!checkAfzonderlijkCB.isSelected())viewCB.setSelected(false);
			((EditInteractiePanelDialog)SwingUtilities.getAncestorOfClass(EditInteractiePanelDialog.class,(Component)mainPanel)).pack();

		}
		else if(e.getSource()==logCB)
	    {   logIDField.setVisible(logCB.isSelected());  
	    	//logObjectivesButton.setVisible(logCB.isSelected());
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
    
    // einde methode TabletOwner
}
