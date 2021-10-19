package fi.wiskopdr;

import java.awt.AWTEventMulticaster;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;

import fi.beans.wiskopdrbeans.*;
import fi.wiskopdr.AntwoordTekstVakEditPanel.EditorComponentListener;
import fi.wiskopdr.formuleobjects.*;
import fi.wiskopdr.opdrnav.OpdrNavStructEdit;
import fi.wiskopdr.tekstobjects.*;

public class TekstEditorEditPanel extends JPanel implements InteractieEditPanel , ActionListener, HelpButtonPanelIF
{
	// Algemene attributen 
    private Font font = new Font("SansSerif",Font.PLAIN,12);//WiskOpdr.tekstFont;
	 
    private Tablet tablet;
    private boolean tabletAdded;
    private FormuleVakHouder tabletUser;
    
    // Basis GUI
    private JPanel mainPanel;
    
    // StarEditor
	private TekstEditor startEditor;
	private JPanel startEditorPanel;
  	private JLabel titleStartLabel;
  	private Box startEditorBox;
  	
  	 // Logging/Nakijken
  	private Box settingsBox;
 	private JLabel titleLoggingLabel;
    private JCheckBox checkCB;
    private JLabel maxScoreLabel;
    private JTextField maxScoreField;
    
    private JCheckBox logCB;
 	private JTextField logIDField;
 	private JTextField logIDLabelField;
 	private JLabel logIDLabelLabel;
 	private ObjectiveChoiceButton logObjectivesButton;
  	
 	// Hulp setting
 	private JLabel titleHulpLabel;
	private JCheckBox formuleEditorCB;
	private JCheckBox rekenToolCB; 
	private JCheckBox grafToolCB; 
	
	// Opmaak
  	private JLabel titleOpmaakLabel;
  	private JCheckBox boxMetRandCB;
	private boolean formuleEditorAan, rekenTool, grafTool;
	
	// Helpbuttons
    private static String HELP_4_URL = WiskOpdr.rb.getString("HELP_4_URL");
    private static String HELP_4_URL_CHECK = WiskOpdr.rb.getString("HELP_4_URL_CHECK");
    private static String HELP_4_URL_LOGID = WiskOpdr.rb.getString("HELP_4_URL_LOGID");
    private static String HELP_4_URL_FORMINVOER = WiskOpdr.rb.getString("HELP_4_URL_FORMINVOER");
    private static String HELP_4_URL_REKENMACHINE = WiskOpdr.rb.getString("HELP_4_URL_REKENMACHINE");
    private static String HELP_4_URL_RAND = WiskOpdr.rb.getString("HELP_4_URL_RAND");
    
    private HelpButton hbCheck;
    private HelpButton hbLogID;
    private HelpButton hbFormInvoer;
    private HelpButton hbRekenmachine;
    private HelpButton hbRand;
    
	
	public TekstEditorEditPanel()
	{	
		setLayout(new BorderLayout());
		setBackground(Color.white);	
		
		formuleEditorAan = true;
		rekenTool = false;
		grafTool = false;
		
		makeGUI();
	}
	
	private void makeGUI() {
		// Main
    	mainPanel = new JPanel(new BorderLayout());
		mainPanel.setBackground(WiskOpdr.colorGray3);
		
		// GUI antwoordBox
		titleStartLabel = new JLabel(WiskOpdr.rb.getString("TEEP_titleStartLabel"));
		titleStartLabel.setForeground(WiskOpdr.colorBlue1);
		titleStartLabel.setFont(font.deriveFont(Font.BOLD, 16));
		
		startEditor = new TekstEditor();
		startEditor.setBounds(0,0,300,250);
		startEditor.setFont(font);
		startEditor.addActionListener(this);
		startEditor.setResizable(true);
        
        startEditorPanel = new JPanel();
        startEditorPanel.setLayout(null);
        startEditorPanel.add(startEditor);
        startEditorPanel.addComponentListener(new EditorComponentListener());
        startEditorPanel.setPreferredSize(new Dimension(300,250));
        startEditorPanel.setMaximumSize(new Dimension(300,250));
        startEditorPanel.setMinimumSize(new Dimension(300,250));
        
        // Logging/Nakijken
 		titleLoggingLabel = new JLabel(WiskOpdr.rb.getString("FEV_titleLoggingLabel"));
     	titleLoggingLabel.setForeground(WiskOpdr.colorBlue1);
     	titleLoggingLabel.setFont(font.deriveFont(Font.BOLD, 16));
     	
     	checkCB = makeCheckBox(5,5,200,20,WiskOpdr.rb.getString("TEEP_checkDocentCBLabel"),false,true);
     	maxScoreLabel = makeLabel(470,25,50,20,WiskOpdr.rb.getString("TEEP_maxScoreLabel"),false);   
 		maxScoreField = makeTextField(520,25,60,20,"0",false);
 		logCB = makeCheckBox(450,5,70,20,WiskOpdr.rb.getString("logCBLabel"),false,true);
        logIDField = makeTextField(520,5,60,20,"0",false);
        logIDLabelField = makeTextField(520,25,60,20,"",false);
        logIDLabelLabel = makeLabel(470,25,50,20,WiskOpdr.rb.getString("TVEP_logIDLabelLabel"),false);
        logObjectivesButton = new ObjectiveChoiceButton();
        logObjectivesButton.setVisible(false);
 		
        // Hulp setting
        titleHulpLabel = new JLabel(WiskOpdr.rb.getString("TEEP_editorOptiesLabel"));
    	titleHulpLabel.setForeground(WiskOpdr.colorBlue1);
    	titleHulpLabel.setFont(font.deriveFont(Font.BOLD, 16));
    	
    	formuleEditorCB = makeCheckBox(500,50,160,20,WiskOpdr.rb.getString("TEEP_menuBalkOptie"),  formuleEditorAan,true);
		rekenToolCB = makeCheckBox(500,80,160,20,WiskOpdr.rb.getString( "TEEP_rekenToolOptie"), rekenTool, true);
		
		// GUI Opmaak box
        titleOpmaakLabel = new JLabel(WiskOpdr.rb.getString("FEV_titleOpmaakLabel"));
    	titleOpmaakLabel.setForeground(WiskOpdr.colorBlue1);
    	titleOpmaakLabel.setFont(font.deriveFont(Font.BOLD, 16));
    	
    	boxMetRandCB = makeCheckBox(690,95,80,20,WiskOpdr.rb.getString("boxMetRand"),true,true);
    	
    	hbCheck = makeHelpButton(HELP_4_URL_CHECK);
	    hbLogID = makeHelpButton(HELP_4_URL_LOGID);
	    hbFormInvoer = makeHelpButton(HELP_4_URL_FORMINVOER);
	    hbRekenmachine = makeHelpButton(HELP_4_URL_REKENMACHINE);
	    hbRand = makeHelpButton(HELP_4_URL_RAND);
    	
    	removeAll();
	    plaatsGUI();
	    add(mainPanel);	
	}
	
	private void plaatsGUI() {
		// plaats compoenenten antwoordbox
		Component[] r31 = {titleStartLabel, hgl()};
		Component[] r32 = {startEditorPanel, hgl()};
		Component[] k3 = {hb(r31),vst(20), hb(r32), vgl()};
		startEditorBox = vb(k3);
		
		// plaatsComponenten settingBox
		Component[] r41 = {titleLoggingLabel, 	hgl()};
		Component[] r42 = {checkCB, 			ra(5,0),	hgl(),	hbCheck};
		Component[] r43 = {ra(25,0), 			maxScoreLabel, 		ra(5,0),	maxScoreField,	ra(10, 0), logObjectivesButton, hgl()};
		Component[] r44 = {logCB, 				ra(5,0), logIDField, ra(5,0), logIDLabelLabel, ra(5,10), logIDLabelField, ra(5,0),	hgl(),	hbLogID};
		Component[] r45 = {titleHulpLabel, 		hgl()};
		Component[] r46 = {formuleEditorCB, 	ra(5,0),	hgl(),	hbFormInvoer};
		Component[] r47 = {rekenToolCB, 		ra(5,0),	hgl(),	hbRekenmachine};
		Component[] r48 = {titleOpmaakLabel, 	hgl()};
		Component[] r49 = {boxMetRandCB, 		ra(5,0),	hgl(),	hbRand};
		
		Component[] k4 = {hb(r41),vst(10),hb(r42),hb(r43),hb(r44),vst(20),hb(r45),vst(10),hb(r46),hb(r47),
				vst(20),hb(r48),vst(10),hb(r49), vgl()};
		settingsBox = vb(k4);
		
		// boxes plaatsen
		Box boxh = Box.createHorizontalBox();
		mainPanel.add(boxh);
		
		boxh.add(startEditorBox);
		boxh.add(Box.createHorizontalStrut(20));
		boxh.add(settingsBox);
		
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
	
	private JCheckBox maakCheckBox(String s, int x, int y, int b, int h, boolean selected)
	{	JCheckBox checkbox = new JCheckBox(s);
		checkbox.setBounds(x,y,b,h);
		checkbox.setFont(font);
		checkbox.setBackground(getBackground());
		checkbox.setSelected(selected);
		checkbox.addActionListener(this);
		add(checkbox);
		
		return checkbox;
	}
	
	public JCheckBox makeCheckBox(int x, int y, int b, int h, String text, boolean selected, boolean visible)
	{	JCheckBox checkbox = new WiskOpdrCheckbox(text);
		checkbox.setBounds(x,y,b,h);
		checkbox.setFont(font);
		checkbox.setOpaque(false);
		checkbox.addActionListener(this);
		checkbox.setSelected(selected);
		checkbox.setVisible(visible);
		add(checkbox,0);
		return checkbox;
	}
	
	public JTextField makeTextField(int x, int y, int b, int h, String text, boolean visible)
	{	JTextField textField = new WiskOpdrTextField(text);
		textField.setBounds(x,y,b,h);
		textField.setPreferredSize(new Dimension(50,22));
	    textField.setMaximumSize(new Dimension(50,22));
		textField.setFont(font);
		textField.addActionListener(this);
		textField.setVisible(visible);
		add(textField,0);
		return textField;
	}
	
	public JLabel makeLabel(int x, int y, int b, int h, String text, boolean visible)
	{	JLabel label = new JLabel(text);
		label.setForeground(WiskOpdr.colorBlue1);
		label.setBounds(x,y,b,h);
		label.setFont(font);
		label.setVisible(visible);
		add(label,0);
		return label;
	}
	
	public HelpButton makeHelpButton(String url) {
		HelpButton helpButton = new HelpButton(url);
		helpButton.addActionListener(this);
 		helpButton.setFont(new Font("SansSerif",Font.BOLD,12));
 		helpButton.setPreferredSize(new Dimension(18,18));
 		helpButton.setMinimumSize(new Dimension(18,18));
 		helpButton.setMaximumSize(new Dimension(18,18));
 		helpButton.setVisible(false);
 		return helpButton;
	}
	
	@SuppressWarnings({"rawtypes", "unchecked"})
    public Hashtable getEditState()
	{	
		boolean balkZichtbaar = true;
		boolean rekenTool = false;
		boolean grafTool = false;
		boolean boxMetRand = true;
		boolean logOption;
		String  logID;
		boolean checkDocent = false;
		int scoreMax = 0;
		
		balkZichtbaar = this.formuleEditorAan;
		rekenTool = this.rekenTool;
		grafTool = this.grafTool;
		boxMetRand = boxMetRandCB.isSelected();
		logOption = logCB.isSelected();
		logID = logIDField.getText();
		checkDocent = checkCB.isSelected();
		scoreMax = Integer.parseInt(maxScoreField.getText());
		
		Hashtable h = startEditor.getEditState();
		
		h.put("balkZichtbaar", new Boolean(balkZichtbaar));
		h.put("rekenTool", new Boolean(rekenTool));
		h.put("grafTool", new Boolean(grafTool));
		h.put("boxMetRand", new Boolean(boxMetRand));
		if(logOption) {
			h.put("logOption", Boolean.TRUE);
		} else {
			h.remove("logOption");
		}
		h.put("logID", logID);
		h.put("checkDocent", new Boolean(checkDocent));
		h.put("scoreMax", new Integer(scoreMax));
		
		if (ObjectiveChoiceButton.hasObjectiveChoices()) {
		  h.putAll(logObjectivesButton.getEditState(scoreMax));
		}
		
		return h;
	}
	
	public void setEditState(Hashtable h)
	{
		boolean balkZichtbaar = true;
		boolean rekenTool = false;
		boolean grafTool = false;
		boolean checkDocent = false;
		boolean boxMetRand = true;
		boolean logOption = false;
		int scoreMax = 0;
		String logID = "";
				
		if(h.containsKey("balkZichtbaar")) balkZichtbaar = ((Boolean)h.get("balkZichtbaar")).booleanValue();
		if(h.containsKey("rekenTool")) rekenTool = ((Boolean)h.get("rekenTool")).booleanValue();
		if(h.containsKey("grafTool")) grafTool = ((Boolean)h.get("grafTool")).booleanValue();
		if(h.containsKey("boxMetRand")) boxMetRand = ((Boolean)h.get("boxMetRand")).booleanValue();
		if(h.containsKey("logOption")) logOption = ((Boolean)h.get("logOption")).booleanValue();
		if(h.containsKey("logID")) logID = (String)h.get("logID");
		if(h.containsKey("checkDocent")) checkDocent = ((Boolean)h.get("checkDocent")).booleanValue();
		if(h.containsKey("scoreMax")) scoreMax = ((Integer)h.get("scoreMax")).intValue();
		
		this.formuleEditorAan = balkZichtbaar;
		this.rekenTool = rekenTool;
		this.grafTool = grafTool;
//		this.formuleKnop = formuleKnop;
//		this.formuleToolPopup = formuleToolPopup;
//		this.buttonOptie = buttonOptie;
//		this.boxMetRand = boxMetRand;
		
		formuleEditorCB.setSelected(balkZichtbaar);
		rekenToolCB.setSelected(rekenTool);
//		grafToolCB.setSelected(grafTool);

		boxMetRandCB.setSelected(boxMetRand);
		
        logCB.setSelected(logOption);
        logIDField.setVisible(logOption);
        //logObjectivesButton.setVisible(logOption);
        logIDField.setText(logID);
        
        checkCB.setSelected(checkDocent);
        maxScoreLabel.setVisible(checkCB.isSelected());
    	maxScoreField.setVisible(checkCB.isSelected());
    	logObjectivesButton.setVisible(ObjectiveChoiceButton.hasObjectiveChoices() && checkCB.isSelected());
    	maxScoreField.setText(""+scoreMax);
    	if (ObjectiveChoiceButton.hasObjectiveChoices()) {
    	  logObjectivesButton.setEditState(h);
    	}

		
		//grafiekPanel.zetFormulesZichtbaar(formulesZichtbaar);
		//grafiekPanel.zetTraceOptie(traceOptie);
		//grafiekPanel.zetZoomOptie(zoomOptie);
		//grafiekPanel.zetDragOptie(dragOptie);
		
        startEditor.setEditState(h);
        startEditor.setButton(false);
		
	}
	
	public void zetBreedte(int b)
	{	startEditorPanel.setSize(b,startEditorPanel.getSize().height);
		startEditorPanel.setPreferredSize(new Dimension(b,startEditorPanel.getSize().height));
	    startEditorPanel.setMaximumSize(new Dimension(b,startEditorPanel.getSize().height));
	    startEditorPanel.setMinimumSize(new Dimension(b,startEditorPanel.getSize().height));
	    EditInteractiePanelDialog c = ((EditInteractiePanelDialog)SwingUtilities.getAncestorOfClass(EditInteractiePanelDialog.class,(Component)mainPanel));
	    if(c!=null)
	    	c.pack();
	}
	public void zetHoogte(int h)
	{	startEditorPanel.setSize(startEditorPanel.getSize().width, h);
		startEditorPanel.setPreferredSize(new Dimension(startEditorPanel.getSize().width, h));
	    startEditorPanel.setMaximumSize(new Dimension(startEditorPanel.getSize().width, h));
	    startEditorPanel.setMinimumSize(new Dimension(startEditorPanel.getSize().width, h));
	    EditInteractiePanelDialog c = ((EditInteractiePanelDialog)SwingUtilities.getAncestorOfClass(EditInteractiePanelDialog.class,(Component)mainPanel));
	    if(c!=null)
	    	c.pack();

	}
	
	public void setBounds(int x, int y, int b, int h)
	{
		super.setBounds(x,y,b,h);
	}
	
	public void wis(){}
    
	public void zetMode(int mode){}
	
    public void stop(){}
    
    public void start(){}
	
	public void actionPerformed(ActionEvent e)
	{	
		if(e.getSource() instanceof HelpButton)
		{
			OpdrNavStructEdit.helpBrowser.loadURL(((HelpButton)e.getSource()).getURL());
		}
		else if(e.getSource().equals(formuleEditorCB))
		{	formuleEditorAan = formuleEditorCB.isSelected();
			startEditor.zetBalkZichtbaar(formuleEditorAan);
			if(!formuleEditorAan) {
				rekenTool = false;
				rekenToolCB.setSelected(false);
			}
			startEditor.zetRekenTool(rekenTool);
		}
		if(e.getSource().equals(rekenToolCB))
		{	rekenTool = rekenToolCB.isSelected();
			startEditor.zetRekenTool(rekenTool);
			if(!formuleEditorAan) {
				formuleEditorAan = true;
				formuleEditorCB.setSelected(true);
				startEditor.zetBalkZichtbaar(formuleEditorAan);
			}
		}
		if(e.getSource().equals(grafToolCB))
		{	grafTool = grafToolCB.isSelected();
			startEditor.zetGrafTool(grafTool);
		}
		else if(e.getSource()==logCB)
	    {   logIDField.setVisible(logCB.isSelected());
	    	validate();
	    	//logObjectivesButton.setVisible(logCB.isSelected());
	    }
		else if(e.getSource()==checkCB)
	    {   maxScoreLabel.setVisible(checkCB.isSelected());
	    	maxScoreField.setVisible(checkCB.isSelected());
	    	logObjectivesButton.setVisible(ObjectiveChoiceButton.hasObjectiveChoices() && checkCB.isSelected());
	    	if(!checkCB.isSelected())
	    		maxScoreField.setText("0");
	    }

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
			
		}
		tablet.zetFormuleVakHouder(formuleVakHouder);
		tabletUser = formuleVakHouder;
		
		
	}
	
	public void addTablet(FormuleVakHouder formuleVakHouder, int x, int y)
	{	if(tablet==null) 
		{	tablet = new Tablet(formuleVakHouder);
			
			
		}
		if(!tabletAdded)
		{	add(tablet,0);
			tablet.setLocation(x,y);
			tabletAdded = true;
			//resize();
            repaint();
		}
		tablet.zetFormuleVakHouder(formuleVakHouder);
	}
	
	public void removeTablet()
	{	if(tablet==null)return;
        remove(tablet);
        //resize();
        repaint();
		tabletAdded = false;
	}
	
	public Tablet getTablet()
	{	return tablet;
	}
	
	//ActionProducer
	private ActionListener actionListener = null;
	
	public void addActionListener(ActionListener l) 
 	{	actionListener = AWTEventMulticaster.add(actionListener,l);
 	}
 	
 	public void removeActionListener(ActionListener l)
 	{	actionListener = AWTEventMulticaster.remove(actionListener, l);
 	}	
 	
 	public void produceAction(String command)
 	{	if (actionListener != null)
 		{	actionListener.actionPerformed( new ActionEvent(this, 0, command) );
 		}
 	}
 	//end ActionProducer
	
 	public class EditorComponentListener implements ComponentListener {

	      @Override
	      public void componentResized(ComponentEvent e) {
	    	  if(e.getSource()==startEditorPanel) {
	    		  int w = startEditorPanel.getWidth();
	    		  int h = startEditorPanel.getHeight();
	    		  startEditor.setBounds(0,0,w,h);
	    	  }
	      }
	     @Override
	      public void componentMoved(ComponentEvent e) {
	        // TODO Auto-generated method stub
	        
	      }

	      @Override
	      public void componentShown(ComponentEvent e) {
	        // TODO Auto-generated method stub
	        
	      }

	      @Override
	      public void componentHidden(ComponentEvent e) {
	        // TODO Auto-generated method stub
	        
	      }
	}
 	@Override
	public void showHelpButtons(boolean b) {
		hbCheck.setVisible(b);
    	hbLogID.setVisible(b);
    	hbFormInvoer.setVisible(b);
    	hbRekenmachine.setVisible(b);
    	hbRand.setVisible(b);	
    	
  	}

	@Override
	public String geefHelpURL() {
		return HELP_4_URL;
	}
}
