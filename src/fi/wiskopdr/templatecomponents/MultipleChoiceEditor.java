package fi.wiskopdr.templatecomponents;

import java.awt.AWTEventMulticaster;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import fi.beans.iconan.Iconan;
import fi.wiskopdr.DialogFacade;
import fi.wiskopdr.ObjectiveChoiceButton;
import fi.wiskopdr.WiskOpdr;
import fi.wiskopdr.WiskOpdrButton;
import fi.wiskopdr.WiskOpdrCheckbox;
import fi.wiskopdr.WiskOpdrComboBox;
import fi.wiskopdr.WiskOpdrTextField;
import fi.wiskopdr.formuleobjects.FormuleButton;
import fi.wiskopdr.tekstobjects.TekstImageVak;
import fi.wiskopdr.tekstobjects.TekstVak;

public class MultipleChoiceEditor implements TComponentEditor, ActionListener {

	private TekstVak tekstVak;
	private Font font = new Font("SansSerif",Font.PLAIN,12);//WiskOpdr.tekstFont;
	
	private DialogFacade frame;
	private JPanel preferencesPanel;
	private JPanel topPanel, mainPanel, bottomPanel;
	private JButton okButton, cancelButton;
	
	private JLabel titleLabel;
	private JLabel titleOpmaakLabel;
	private JLabel itemCountLabel;
	private JTextField itemCountTF;
	private JLabel listNumberTypeLabel;
	private JComboBox listNumberTypeComboBox;
	private JLabel tabWidthLabel;
	private JTextField tabWidthTF;
	private JLabel rowSpaceLabel;
	private JTextField rowSpaceTF;
	private JCheckBox hasPrefixCB;
	
	private JLabel titleLoggingLabel;
	private JCheckBox checkCB;
	private JCheckBox teltMeeCB;
	private JCheckBox logCB;
	private JTextField logIDField;
	private JTextField logIDLabelField;
	private JLabel logIDLabelLabel;
	private ObjectiveChoiceButton logObjectivesButton;
	private ObjectiveChoiceButton[] logMisconceptionsButtons;
	
	private JLabel titleSettingsLabel;
	private JCheckBox multiSelectionsCB;
	
	private JLabel titleAntwoordLabel;
	private JLabel maxScoreLabel;
	private JTextField maxScoreTF;
	private JCheckBox[] selectableCheckboxes;
	private Box selectableCBBox;
	
	private FormuleButton knopImageButton;
	private Dialog imageDialog;
	private Iconan iconman;
	private String knopImageString = "";
	private Image knopImage;
	
	private int scoreMax;
	private boolean multiselections;
	private int aantalSelectables = 4;
	private int aantalSelectablesMax = 30;
	private boolean[][][] logMisconceptions;
	
	
	public MultipleChoiceEditor(TekstVak tekstVak) {
		this.tekstVak = tekstVak;
		makeGUI();
		makeFrame();
	}
	
	public void setTekstVak(TekstVak tekstVak) {
		this.tekstVak = tekstVak;
	}
	
	private void makeGUI() {
		
		preferencesPanel = new JPanel(new BorderLayout());
		
		topPanel = new JPanel();
		topPanel.setBackground(WiskOpdr.colorBlue1);
		
		mainPanel = new JPanel(new BorderLayout());
		mainPanel.setBackground(WiskOpdr.colorGray3);
		mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
		
		bottomPanel = new JPanel();
		bottomPanel.setBackground(WiskOpdr.colorGray2);
		bottomPanel.setBorder(BorderFactory.createLineBorder(WiskOpdr.colorGray2, 2));
		
		titleLabel = new JLabel(WiskOpdr.rb.getString("TCOMP_multip_settings"));
		titleLabel.setForeground(WiskOpdr.colorGray3);
		titleLabel.setFont(new Font("SansSerif",Font.PLAIN, 24));
		topPanel.add(titleLabel);
		
		titleOpmaakLabel = new JLabel(WiskOpdr.rb.getString("FEV_titleOpmaakLabel"));
    	titleOpmaakLabel.setForeground(WiskOpdr.colorBlue1);
    	titleOpmaakLabel.setFont(font.deriveFont(Font.BOLD, 16));
    	
		itemCountLabel = new JLabel(WiskOpdr.rb.getString("TCOMP_multip_rowCount"));
		itemCountLabel.setFont(font);
		itemCountLabel.setForeground(WiskOpdr.colorBlue1);
		
		itemCountTF = new WiskOpdrTextField("");
		itemCountTF.addActionListener(this);
		itemCountTF.setPreferredSize(new Dimension(30,22));
		itemCountTF.setMaximumSize(new Dimension(30,22));
		
		listNumberTypeLabel = new JLabel(WiskOpdr.rb.getString("TCOMP_multip_numberType"));
		listNumberTypeLabel.setFont(font);
		listNumberTypeLabel.setForeground(WiskOpdr.colorBlue1);
		
		listNumberTypeComboBox = new WiskOpdrComboBox();
		listNumberTypeComboBox.setFont(font);
		listNumberTypeComboBox.setPreferredSize(new Dimension(80,22));
		listNumberTypeComboBox.addItem(WiskOpdr.rb.getString("TCOMP_multip_chooseType"));
		for(int i=0 ; i<MultipleChoiceGenerator.listNumbers.length ; i++) {
			listNumberTypeComboBox.addItem(MultipleChoiceGenerator.listNumbers[i][0]+" ,"+MultipleChoiceGenerator.listNumbers[i][1]+" ,"+MultipleChoiceGenerator.listNumbers[i][2]+" , ...");
		}
		
		tabWidthLabel = new JLabel(WiskOpdr.rb.getString("TCOMP_multip_tabWidth"));
		tabWidthLabel.setFont(font);
		tabWidthLabel.setForeground(WiskOpdr.colorBlue1);
		
		tabWidthTF = new WiskOpdrTextField("");
		tabWidthTF.addActionListener(this);
		tabWidthTF.setPreferredSize(new Dimension(30,22));
		tabWidthTF.setMaximumSize(new Dimension(30,22));
		
		rowSpaceLabel = new JLabel(WiskOpdr.rb.getString("TCOMP_multip_rowSpace"));
		rowSpaceLabel.setFont(font);
		rowSpaceLabel.setForeground(WiskOpdr.colorBlue1);
		
		rowSpaceTF = new WiskOpdrTextField("");
		rowSpaceTF.addActionListener(this);
		rowSpaceTF.setPreferredSize(new Dimension(30,22));
		rowSpaceTF.setMaximumSize(new Dimension(30,22));
		
		hasPrefixCB = new WiskOpdrCheckbox(WiskOpdr.rb.getString("TCOMP_multip_hasPrefix"));
		hasPrefixCB.setOpaque(false);
		hasPrefixCB.setSelected(true);
		hasPrefixCB.setFont(font);
		
		titleLoggingLabel = new JLabel(WiskOpdr.rb.getString("FEV_titleLoggingLabel"));
    	titleLoggingLabel.setForeground(WiskOpdr.colorBlue1);
    	titleLoggingLabel.setFont(font.deriveFont(Font.BOLD, 16));
		
		checkCB = new WiskOpdrCheckbox(WiskOpdr.rb.getString("checkCBLabel"));
		checkCB.setOpaque(false);
		checkCB.setSelected(false);
		checkCB.setFont(font);
		
		teltMeeCB = new WiskOpdrCheckbox(WiskOpdr.rb.getString("teltMeeCBLabel"));
		teltMeeCB.setOpaque(false);
		teltMeeCB.setSelected(false);
		teltMeeCB.setFont(font);
		
		logCB = new WiskOpdrCheckbox(WiskOpdr.rb.getString("logCBLabel"));
		logCB.setOpaque(false);
		logCB.setSelected(false);
		logCB.setFont(font);
		
		logIDField = new WiskOpdrTextField("");
		logIDField.addActionListener(this);
		logIDField.setPreferredSize(new Dimension(50,22));
		logIDField.setMaximumSize(new Dimension(50,22));
		
		logIDLabelLabel = new JLabel(WiskOpdr.rb.getString("TVEP_logIDLabelLabel"));
		logIDLabelLabel.setFont(font);
		logIDLabelLabel.setForeground(WiskOpdr.colorBlue1);
		
		logIDLabelField = new WiskOpdrTextField("");
		logIDLabelField.addActionListener(this);
		logIDLabelField.setPreferredSize(new Dimension(50,22));
		logIDLabelField.setMaximumSize(new Dimension(50,22));
		
		titleSettingsLabel = new JLabel(WiskOpdr.rb.getString("settingsLabel"));
		titleSettingsLabel.setForeground(WiskOpdr.colorBlue1);
		titleSettingsLabel.setFont(font.deriveFont(Font.BOLD, 16));
		
		
		maxScoreLabel = new JLabel(WiskOpdr.rb.getString("score"));//Score");
		maxScoreLabel.setBounds(300,50,80,20);
		maxScoreLabel.setFont(font);
		
		maxScoreTF = new WiskOpdrTextField("0");
		maxScoreTF.setPreferredSize(new Dimension(50,22));
		maxScoreTF.setBounds(380,50,40,20);
		maxScoreTF.addActionListener(this);
		
		multiSelectionsCB = new WiskOpdrCheckbox(WiskOpdr.rb.getString("meervSelectiesLabel"));//("Meervoudige selecties mogelijk");
		multiSelectionsCB.setBounds(300,120,280,20);
		multiSelectionsCB.setOpaque(false);
		multiSelectionsCB.setFont(font);
		
		titleAntwoordLabel = new JLabel(WiskOpdr.rb.getString("FEV_titleAntwoordLabel"));
		titleAntwoordLabel.setForeground(WiskOpdr.colorBlue1);
		titleAntwoordLabel.setFont(font.deriveFont(Font.BOLD, 16));
    	
    	selectableCheckboxes = new JCheckBox[aantalSelectablesMax];
    	logMisconceptionsButtons = new ObjectiveChoiceButton[aantalSelectablesMax];
    	selectableCBBox = Box.createVerticalBox();
    	
    	logObjectivesButton = new ObjectiveChoiceButton(WiskOpdr.objectives, WiskOpdr.categorieString, WiskOpdr.studentModel);
        logObjectivesButton.setVisible(WiskOpdr.objectives!=null);
        logObjectivesButton.setBounds(600,5,120,20);
        //if(WiskOpdr.objectives!=null)add(logObjectivesButton);
                
        knopImageButton = new FormuleButton(WiskOpdr.rb.getString("klaarKnopLabel"));
		knopImageButton.setBounds(550,50,80,20);
		knopImageButton.addActionListener(this);
		//add(knopImageButton);
	
	
	
		
		
		Box boxh = Box.createHorizontalBox();
		Box boxv0 = Box.createVerticalBox();
		Box boxv1 = Box.createVerticalBox();
		Box boxv2 = Box.createVerticalBox();
		
		Box regelBox = Box.createHorizontalBox();
		regelBox.add(titleAntwoordLabel);
		regelBox.add(Box.createHorizontalGlue());
		boxv0.add(regelBox);
		boxv0.add(Box.createVerticalStrut(15));
		
		maakCheckboxes();
		regelBox = Box.createHorizontalBox();
		regelBox.add(selectableCBBox);
		regelBox.add(Box.createHorizontalGlue());
		boxv0.add(regelBox);
		boxv0.add(Box.createVerticalGlue());
		
		
		regelBox = Box.createHorizontalBox();
		regelBox.add(titleOpmaakLabel);
		regelBox.add(Box.createHorizontalGlue());
		boxv1.add(regelBox);
		boxv1.add(Box.createVerticalStrut(15));
		
		regelBox = Box.createHorizontalBox();
		regelBox.add(itemCountLabel);
		regelBox.add(Box.createRigidArea(new Dimension(10,10)));
		regelBox.add(Box.createHorizontalGlue());
		regelBox.add(itemCountTF);
		
		boxv1.add(regelBox);
		boxv1.add(Box.createVerticalStrut(5));
		
		regelBox = Box.createHorizontalBox();
		regelBox.add(listNumberTypeLabel);
		regelBox.add(Box.createRigidArea(new Dimension(10,10)));
		regelBox.add(Box.createHorizontalGlue());
		regelBox.add(listNumberTypeComboBox);
		
		boxv1.add(regelBox);
		boxv1.add(Box.createVerticalStrut(5));
		
		regelBox = Box.createHorizontalBox();
		regelBox.add(tabWidthLabel);
		regelBox.add(Box.createRigidArea(new Dimension(10,10)));
		regelBox.add(Box.createHorizontalGlue());
		regelBox.add(tabWidthTF);
		
		boxv1.add(regelBox);
		boxv1.add(Box.createVerticalStrut(5));
		
		regelBox = Box.createHorizontalBox();
		regelBox.add(rowSpaceLabel);
		regelBox.add(Box.createRigidArea(new Dimension(10,10)));
		regelBox.add(Box.createHorizontalGlue());
		regelBox.add(rowSpaceTF);
				
		boxv1.add(regelBox);
		boxv1.add(Box.createVerticalStrut(5));
		
		regelBox = Box.createHorizontalBox();
		regelBox.add(hasPrefixCB);
		regelBox.add(Box.createHorizontalGlue());
				
		boxv1.add(regelBox);
		boxv1.add(Box.createVerticalGlue());
		
		regelBox = Box.createHorizontalBox();
    	regelBox.add(titleLoggingLabel);
    	regelBox.add(Box.createHorizontalGlue());
    	boxv2.add(regelBox);
    	boxv2.add(Box.createVerticalStrut(15));
    	
    	regelBox = Box.createHorizontalBox();
    	regelBox.add(checkCB);
    	regelBox.add(Box.createHorizontalGlue());
    	boxv2.add(regelBox);
    	
    	regelBox = Box.createHorizontalBox();
    	regelBox.add(teltMeeCB);
    	regelBox.add(Box.createHorizontalGlue());
    	boxv2.add(regelBox);
    	
    	regelBox = Box.createHorizontalBox();
    	regelBox.add(logCB);
    	regelBox.add(Box.createRigidArea(new Dimension(5,10)));
    	regelBox.add(logIDField);
    	regelBox.add(Box.createRigidArea(new Dimension(5,10)));
    	regelBox.add(logIDLabelLabel);
    	regelBox.add(Box.createRigidArea(new Dimension(5,10)));
    	regelBox.add(logIDLabelField);
    	regelBox.add(Box.createHorizontalGlue());
    	boxv2.add(regelBox);
//    	boxv2.add(Box.createRigidArea(new Dimension(5,5)));
//    	regelBox = Box.createHorizontalBox();
//    	regelBox.add(Box.createRigidArea(new Dimension(10,5)));
//    	regelBox.add(logObjectivesButton);
//    	regelBox.add(Box.createHorizontalGlue());
//    	boxv2.add(regelBox);
    	boxv2.add(Box.createVerticalStrut(20));
    	
    	regelBox = Box.createHorizontalBox();
    	regelBox.add(titleSettingsLabel);
    	regelBox.add(Box.createHorizontalGlue());
    	boxv2.add(regelBox);
    	boxv2.add(Box.createVerticalStrut(15));
    	
    	regelBox = Box.createHorizontalBox();
    	regelBox.add(multiSelectionsCB);
    	regelBox.add(Box.createHorizontalGlue());
    	boxv2.add(regelBox);
    	
//    	boxv2.add(Box.createVerticalStrut(20));
    	
    	boxv2.add(Box.createVerticalGlue());
		
    	boxh.add(boxv0);
		boxh.add(Box.createHorizontalStrut(50));
		boxh.add(boxv1);
		boxh.add(Box.createHorizontalStrut(50));
		boxh.add(boxv2);
		mainPanel.add(boxh);
		
		preferencesPanel.add(topPanel,BorderLayout.NORTH);
		preferencesPanel.add(bottomPanel,BorderLayout.SOUTH);
		preferencesPanel.add(mainPanel,BorderLayout.CENTER);
		
		topPanel.add(titleLabel);
		
		okButton = new WiskOpdrButton("Ok");//
		okButton.setPreferredSize(new Dimension(70,22));
		okButton.addActionListener(this);
		bottomPanel.add(okButton);
		
		cancelButton = new WiskOpdrButton("Cancel");//
		cancelButton.setPreferredSize(new Dimension(70,22));
		cancelButton.addActionListener(this);
		bottomPanel.add(cancelButton);
	}
	
	public void makeFrame(){
	   	frame = DialogFacade.newInstance(tekstVak, WiskOpdr.rb.getString("TCOMP_multip"), true);
	   	//Dimension preferredSize = new Dimension(400,320);
		//frame.setPreferredSize(preferredSize);
	    frame.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
	    //frame.setSize(preferredSize);
	    frame.getContentPane().setLayout(new BorderLayout());
	    frame.getContentPane().add(preferencesPanel);
	    frame.getContentPane().add(bottomPanel,BorderLayout.SOUTH);
	    frame.pack();
	    //frame.setLocation(tekstVak.getLocationOnScreen().x, tekstVak.getLocationOnScreen().y);
		//frame.setVisible(true);
		    
	}
	
	public void maakCheckboxes()
	{
		if(WiskOpdr.misconceptions!=null)
			logMisconceptions = new boolean[aantalSelectables][][];
				
		for(int i=0 ; i<aantalSelectables ; i++)
		{
			Box regelBox = Box.createHorizontalBox();
			if(selectableCheckboxes[i]==null)
            {   
				selectableCheckboxes[i] = new WiskOpdrCheckbox("Nr "+(i+1));
                regelBox.add(selectableCheckboxes[i]);
                regelBox.add(Box.createRigidArea(new Dimension(20,10)));
                
                
            }
		    if(logMisconceptionsButtons[i]==null)
		    {
		    	logMisconceptionsButtons[i] = new ObjectiveChoiceButton(WiskOpdr.rb.getString("OPT_misconceptions"),WiskOpdr.misconceptions, WiskOpdr.mccCategorieString);
		    	logMisconceptionsButtons[i].setPreferredSize(new Dimension(120,22));
		    	//if(WiskOpdr.misconceptions!=null)
		        	regelBox.add(logMisconceptionsButtons[i]);
		    }
		    selectableCBBox.add(regelBox);
		   
		}
		
	    selectableCBBox.add(Box.createVerticalGlue());
		enableMisconceptions();
		
	}
	
	private void enableMisconceptions()
	{
		for(int i=0 ; i<aantalSelectables ; i++)
		{
			boolean visible = WiskOpdr.misconceptions!=null 
					&& !multiSelectionsCB.isSelected()
					&& checkCB.isSelected();
		   	if(logMisconceptionsButtons[i]!=null)
		   		logMisconceptionsButtons[i].setVisible(visible);
		   
		}	
	}
	
	public void verwijderCheckboxes()
	{
//		for(int i=0 ; i<aantalSelectables ; i++)
//		{
//		    if(selectableCheckboxes[i]!=null) 
//		    {	remove(selectableCheckboxes[i]);
//		    	selectableCheckboxes[i] = null;
//		    	if(logMisconceptionsButtons[i]!=null) 
//		    	{	remove(logMisconceptionsButtons[i]);
//		    		logMisconceptionsButtons[i] = null;
//		    	}
//		    }
//		}	
//		aantalSelectables = 0;
//		repaint();
	}
	
	public void editImage() {
        if(imageDialog == null)
        {
        	
//        	Frame f = JOptionPane.getFrameForComponent(this);
//			imageDialog = new Dialog(f,"title", true);
//			imageDialog.setLayout(new BorderLayout());
//			iconman = new Iconan(WiskOpdr.applet, (Component)this, (Hashtable)TekstImageVak.getImageMap());
//            imageDialog.add(iconman);
//            imageDialog.pack();
//            iconman.addActionListener(this);
        }
        iconman.select(knopImageString);
        imageDialog.setVisible(true);
    }  
	
	@Override
	public Hashtable getPreferences() {
		
		int itemCount = MultipleChoiceGenerator.initialItemCount;
		int listNumberType = MultipleChoiceGenerator.initialListNumberType;
		int tabWidth = MultipleChoiceGenerator.initialTabWidth;
		int rowSpace = MultipleChoiceGenerator.initialRowSpace;
		boolean hasPrefix = MultipleChoiceGenerator.initialHasPrefix;
		
		itemCount = Integer.parseInt(itemCountTF.getText());
		listNumberType = listNumberTypeComboBox.getSelectedIndex()-1;
		tabWidth = Integer.parseInt(tabWidthTF.getText());
		rowSpace = Integer.parseInt(rowSpaceTF.getText());
		hasPrefix = hasPrefixCB.isSelected();
		
		Hashtable preferences = new Hashtable();
		
		preferences.put("itemCount", new Integer(itemCount));
		preferences.put("listNumberType", new Integer(listNumberType));
		preferences.put("tabWidth", new Integer(tabWidth));
		preferences.put("rowSpace", new Integer(rowSpace));
		preferences.put("hasPrefix", new Boolean(hasPrefix));
		return preferences;
	}

	@Override
	public void setPreferences(Hashtable preferences) {
		
		int itemCount = MultipleChoiceGenerator.initialItemCount;
		int listNumberType = MultipleChoiceGenerator.initialListNumberType;
		int tabWidth = MultipleChoiceGenerator.initialTabWidth;
		int rowSpace = MultipleChoiceGenerator.initialRowSpace;
		boolean hasPrefix = MultipleChoiceGenerator.initialHasPrefix;
		
		if(preferences.containsKey("itemCount")) itemCount = ((Integer)preferences.get("itemCount")).intValue();
		if(preferences.containsKey("listNumberType")) listNumberType = ((Integer)preferences.get("listNumberType")).intValue()+1;
		if(preferences.containsKey("tabWidth")) tabWidth = ((Integer)preferences.get("tabWidth")).intValue();
		if(preferences.containsKey("rowSpace")) rowSpace = ((Integer)preferences.get("rowSpace")).intValue();
		if(preferences.containsKey("hasPrefix")) hasPrefix = ((Boolean)preferences.get("hasPrefix")).booleanValue();
		
		itemCountTF.setText(""+itemCount);
		listNumberTypeComboBox.setSelectedIndex(listNumberType);
		tabWidthTF.setText(""+tabWidth);
		rowSpaceTF.setText(""+rowSpace);
		hasPrefixCB.setSelected(hasPrefix);
		
		frame.setVisible(true);
		frame.setLocation(tekstVak.getLocationOnScreen().x, tekstVak.getLocationOnScreen().y);
		
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
	
	public void produceThisAction(ActionEvent e)
	{	if (actionListener != null)
		{	actionListener.actionPerformed(e);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		if(e.getSource().equals(okButton)) {
			produceAction("ok");
			frame.setVisible(false);
		}
		if(e.getSource().equals(cancelButton)) {
			frame.setVisible(false);
		}
		
		if(e.getSource()==itemCountTF)
		{
			int aantal = Math.min(aantalSelectablesMax, Integer.parseInt(itemCountTF.getText()));
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
	           // repaint();
	        }
	    }

	    if(imageDialog!=null)
	        imageDialog.setVisible(false);
		
	}
}
