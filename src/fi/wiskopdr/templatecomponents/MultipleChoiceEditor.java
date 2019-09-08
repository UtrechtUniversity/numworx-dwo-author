package fi.wiskopdr.templatecomponents;

import java.awt.AWTEventMulticaster;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
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
import javax.swing.JPanel;
import javax.swing.JTextField;

import fi.wiskopdr.DialogFacade;
import fi.wiskopdr.WiskOpdr;
import fi.wiskopdr.WiskOpdrButton;
import fi.wiskopdr.WiskOpdrCheckbox;
import fi.wiskopdr.WiskOpdrComboBox;
import fi.wiskopdr.WiskOpdrTextField;
import fi.wiskopdr.tekstobjects.TekstVak;

public class MultipleChoiceEditor implements TComponentEditor, ActionListener {

	private TekstVak tekstVak;
	private Font font = new Font("SansSerif",Font.PLAIN,12);//WiskOpdr.tekstFont;
	
	private DialogFacade frame;
	private JPanel preferencesPanel;
	private JPanel topPanel, mainPanel, bottomPanel;
	private JButton okButton, cancelButton;
	
	private JLabel titleLabel;
	private JLabel itemCountLabel;
	private JTextField itemCountTF;
	private JLabel listNumberTypeLabel;
	private JComboBox listNumberTypeComboBox;
	private JLabel tabWidthLabel;
	private JTextField tabWidthTF;
	private JLabel rowSpaceLabel;
	private JTextField rowSpaceTF;
	private JCheckBox hasPrefixCB;
	
	
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
		
		mainPanel = new JPanel();
		mainPanel.setBackground(WiskOpdr.colorGray3);
		mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		
		bottomPanel = new JPanel();
		bottomPanel.setBackground(WiskOpdr.colorGray2);
		bottomPanel.setBorder(BorderFactory.createLineBorder(WiskOpdr.colorGray2, 2));
		

		titleLabel = new JLabel(WiskOpdr.rb.getString("TCOMP_multip_settings"));
		titleLabel.setForeground(WiskOpdr.colorGray3);
		titleLabel.setFont(font.deriveFont(Font.BOLD, 14));
		
		itemCountLabel = new JLabel(WiskOpdr.rb.getString("TCOMP_multip_rowCount"));
		itemCountLabel.setFont(font);
		
		itemCountTF = new WiskOpdrTextField("");
		itemCountTF.addActionListener(this);
		itemCountTF.setPreferredSize(new Dimension(30,22));
		itemCountTF.setMaximumSize(new Dimension(30,22));
		
		listNumberTypeLabel = new JLabel(WiskOpdr.rb.getString("TCOMP_multip_numberType"));
		listNumberTypeLabel.setFont(font);
		
		listNumberTypeComboBox = new WiskOpdrComboBox();
		listNumberTypeComboBox.setFont(font);
		listNumberTypeComboBox.setPreferredSize(new Dimension(80,22));
		listNumberTypeComboBox.addItem(WiskOpdr.rb.getString("TCOMP_multip_chooseType"));
		for(int i=0 ; i<MultipleChoiceGenerator.listNumbers.length ; i++) {
			listNumberTypeComboBox.addItem(MultipleChoiceGenerator.listNumbers[i][0]+" ,"+MultipleChoiceGenerator.listNumbers[i][1]+" ,"+MultipleChoiceGenerator.listNumbers[i][2]+" , ...");
		}
		
		tabWidthLabel = new JLabel(WiskOpdr.rb.getString("TCOMP_multip_tabWidth"));
		tabWidthLabel.setFont(font);
		
		tabWidthTF = new WiskOpdrTextField("");
		tabWidthTF.addActionListener(this);
		tabWidthTF.setPreferredSize(new Dimension(30,22));
		tabWidthTF.setMaximumSize(new Dimension(30,22));
		
		rowSpaceLabel = new JLabel(WiskOpdr.rb.getString("TCOMP_multip_rowSpace"));
		rowSpaceLabel.setFont(font);
		
		rowSpaceTF = new WiskOpdrTextField("");
		rowSpaceTF.addActionListener(this);
		rowSpaceTF.setPreferredSize(new Dimension(30,22));
		rowSpaceTF.setMaximumSize(new Dimension(30,22));
		
		hasPrefixCB = new WiskOpdrCheckbox(WiskOpdr.rb.getString("TCOMP_multip_hasPrefix"));
		hasPrefixCB.setOpaque(false);
		hasPrefixCB.setSelected(true);
		hasPrefixCB.setFont(font);
		
		Box boxv = Box.createVerticalBox();
		
		Box boxh = Box.createHorizontalBox();
		boxh.add(itemCountLabel);
		boxh.add(Box.createHorizontalStrut(10));
		boxh.add(Box.createHorizontalGlue());
		boxh.add(itemCountTF);
		
		boxv.add(boxh);
		boxv.add(Box.createVerticalStrut(5));
		
		boxh = Box.createHorizontalBox();
		boxh.add(listNumberTypeLabel);
		boxh.add(Box.createHorizontalStrut(10));
		boxh.add(Box.createHorizontalGlue());
		boxh.add(listNumberTypeComboBox);
		
		boxv.add(boxh);
		boxv.add(Box.createVerticalStrut(5));
		
		boxh = Box.createHorizontalBox();
		boxh.add(tabWidthLabel);
		boxh.add(Box.createHorizontalStrut(10));
		boxh.add(Box.createHorizontalGlue());
		boxh.add(tabWidthTF);
		
		boxv.add(boxh);
		boxv.add(Box.createVerticalStrut(5));
		
		boxh = Box.createHorizontalBox();
		boxh.add(rowSpaceLabel);
		boxh.add(Box.createHorizontalStrut(10));
		boxh.add(Box.createHorizontalGlue());
		boxh.add(rowSpaceTF);
				
		boxv.add(boxh);
		boxv.add(Box.createVerticalStrut(5));
		
		boxh = Box.createHorizontalBox();
		boxh.add(hasPrefixCB);
		boxh.add(Box.createHorizontalGlue());
				
		boxv.add(boxh);
		
		mainPanel.add(boxv);
		
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
		
	}
}
