package fi.wiskopdr.templatecomponents;

import java.awt.AWTEventMulticaster;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import fi.wiskopdr.DialogFacade;
import fi.wiskopdr.WiskOpdr;
import fi.wiskopdr.WiskOpdrButton;
import fi.wiskopdr.WiskOpdrComboBox;
import fi.wiskopdr.WiskOpdrTextField;
import fi.wiskopdr.tekstobjects.TekstVak;

public class ListEditor implements TComponentEditor, ActionListener {

	private TekstVak tekstVak;
	private Font font = new Font("SansSerif",Font.PLAIN,12);//WiskOpdr.tekstFont;
	
	private DialogFacade frame;
	private JPanel preferencesPanel;
	private JPanel topPanel, mainPanel, bottomPanel;
	private JButton okButton, cancelButton;
	
	private JLabel titleLabel;
	private JLabel rowCountLabel;
	private JTextField rowCountTF;
	private JLabel listNumberTypeLabel;
	private JComboBox listNumberTypeComboBox;
	private JLabel firstListNumberLabel;
	private JComboBox firstListNumberComboBox;
	private JLabel tabWidthLabel;
	private JTextField tabWidthTF;
	private JLabel rowSpaceLabel;
	private JTextField rowSpaceTF;
	
	
	public ListEditor(TekstVak tekstVak) {
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
		
		titleLabel = new JLabel(WiskOpdr.rb.getString("TCOMP_list_settings"));
		titleLabel.setForeground(WiskOpdr.colorGray3);
		titleLabel.setFont(font.deriveFont(Font.BOLD, 14));
		
		rowCountLabel = new JLabel(WiskOpdr.rb.getString("TCOMP_list_rowCount"));
		rowCountLabel.setFont(font);
		
		rowCountTF = new WiskOpdrTextField("");
		rowCountTF.addActionListener(this);
		rowCountTF.setPreferredSize(new Dimension(30,22));
		rowCountTF.setMaximumSize(new Dimension(30,22));
		
		listNumberTypeLabel = new JLabel(WiskOpdr.rb.getString("TCOMP_list_numberType"));
		listNumberTypeLabel.setFont(font);
		
		listNumberTypeComboBox = new WiskOpdrComboBox();
		listNumberTypeComboBox.setFont(font);
		listNumberTypeComboBox.setPreferredSize(new Dimension(80,22));
		listNumberTypeComboBox.addActionListener(this);
		listNumberTypeComboBox.addItem(WiskOpdr.rb.getString("TCOMP_list_chooseType"));
		for(int i=0 ; i<ListGenerator.listNumbers.length ; i++) {
			listNumberTypeComboBox.addItem(ListGenerator.listNumbers[i][0]+" ,"+ListGenerator.listNumbers[i][1]+" ,"+ListGenerator.listNumbers[i][2]+" , ...");
		}
		
		firstListNumberLabel = new JLabel(WiskOpdr.rb.getString("TCOMP_first_list_number"));
		firstListNumberLabel.setFont(font);
		
		firstListNumberComboBox = new WiskOpdrComboBox();
		firstListNumberComboBox.setFont(font);
		firstListNumberComboBox.setPreferredSize(new Dimension(80,22));
		for(int i=0 ; i<ListGenerator.listNumbers[0].length ; i++) {
			firstListNumberComboBox.addItem(ListGenerator.listNumbers[0][i]);
		}
		
		
		tabWidthLabel = new JLabel(WiskOpdr.rb.getString("TCOMP_list_tabWidth"));
		tabWidthLabel.setFont(font);
		
		tabWidthTF = new WiskOpdrTextField("");
		tabWidthTF.addActionListener(this);
		tabWidthTF.setPreferredSize(new Dimension(30,22));
		tabWidthTF.setMaximumSize(new Dimension(30,22));
		
		rowSpaceLabel = new JLabel(WiskOpdr.rb.getString("TCOMP_list_rowSpace"));
		rowSpaceLabel.setFont(font);
		
		rowSpaceTF = new WiskOpdrTextField("");
		rowSpaceTF.addActionListener(this);
		rowSpaceTF.setPreferredSize(new Dimension(30,22));
		rowSpaceTF.setMaximumSize(new Dimension(30,22));
		
		Box boxv = Box.createVerticalBox();
		
		Box boxh = Box.createHorizontalBox();
		boxh.add(rowCountLabel);
		boxh.add(Box.createHorizontalStrut(20));
		boxh.add(Box.createHorizontalGlue());
		boxh.add(rowCountTF);
		
		boxv.add(boxh);
		boxv.add(Box.createVerticalStrut(5));
		
		boxh = Box.createHorizontalBox();
		boxh.add(listNumberTypeLabel);
		boxh.add(Box.createHorizontalStrut(20));
		boxh.add(Box.createHorizontalGlue());
		boxh.add(listNumberTypeComboBox);
		
		boxv.add(boxh);
		boxv.add(Box.createVerticalStrut(5));
		
		boxh = Box.createHorizontalBox();
		boxh.add(firstListNumberLabel);
		boxh.add(Box.createHorizontalStrut(20));
		boxh.add(Box.createHorizontalGlue());
		boxh.add(firstListNumberComboBox);
		
		boxv.add(boxh);
		boxv.add(Box.createVerticalStrut(5));
		
		boxh = Box.createHorizontalBox();
		boxh.add(tabWidthLabel);
		boxh.add(Box.createHorizontalStrut(20));
		boxh.add(Box.createHorizontalGlue());
		boxh.add(tabWidthTF);
		
		boxv.add(boxh);
		boxv.add(Box.createVerticalStrut(5));
		
		boxh = Box.createHorizontalBox();
		boxh.add(rowSpaceLabel);
		boxh.add(Box.createHorizontalStrut(20));
		boxh.add(Box.createHorizontalGlue());
		boxh.add(rowSpaceTF);
				
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
		
		bottomPanel.add(Box.createHorizontalStrut(5));
		
		cancelButton = new WiskOpdrButton("Cancel");//
		cancelButton.setPreferredSize(new Dimension(70,22));
		cancelButton.addActionListener(this);
		bottomPanel.add(cancelButton);
	}
	
	public void makeFrame(){
	   	frame = DialogFacade.newInstance(tekstVak, WiskOpdr.rb.getString("TCOMP_list"), true);
	   	frame.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
	    frame.getContentPane().setLayout(new BorderLayout());
	    frame.getContentPane().add(preferencesPanel);
	    frame.pack();
	    frame.setLocation(tekstVak.getLocationOnScreen().x, tekstVak.getLocationOnScreen().y);
	}
	
	@Override
	public Hashtable<String,Object> getPreferences() {
		
		int rowCount = ListGenerator.initialRowCount;
		int listNumberType = ListGenerator.initialListNumberType;
		int firstListNumber = ListGenerator.initialFirstListNumber;
		int tabWidth = ListGenerator.initialTabWidth;
		int rowSpace = ListGenerator.initialRowSpace;
		
		rowCount = Integer.parseInt(rowCountTF.getText());
		listNumberType = listNumberTypeComboBox.getSelectedIndex()-1;
		firstListNumber = firstListNumberComboBox.getSelectedIndex();
		tabWidth = Integer.parseInt(tabWidthTF.getText());
		rowSpace = Integer.parseInt(rowSpaceTF.getText());
		
		Hashtable<String,Object> preferences = new Hashtable<String,Object>();
		
		preferences.put("rowCount", new Integer(rowCount));
		preferences.put("listNumberType", new Integer(listNumberType));
		preferences.put("firstListNumber", new Integer(firstListNumber));
		preferences.put("tabWidth", new Integer(tabWidth));
		preferences.put("rowSpace", new Integer(rowSpace));
		return preferences;
	}

	@Override
	public void setPreferences(Hashtable<String,Object> preferences) {
		
		int rowCount = ListGenerator.initialRowCount;
		int listNumberType = ListGenerator.initialListNumberType;
		int firstListNumber = ListGenerator.initialFirstListNumber;
		int tabWidth = ListGenerator.initialTabWidth;
		int rowSpace = ListGenerator.initialRowSpace;
		
		if(preferences.containsKey("rowCount")) rowCount = ((Integer)preferences.get("rowCount")).intValue();
		if(preferences.containsKey("listNumberType")) listNumberType = ((Integer)preferences.get("listNumberType")).intValue()+1;
		if(preferences.containsKey("firstListNumber")) firstListNumber = ((Integer)preferences.get("firstListNumber")).intValue();
		if(preferences.containsKey("tabWidth")) tabWidth = ((Integer)preferences.get("tabWidth")).intValue();
		if(preferences.containsKey("rowSpace")) rowSpace = ((Integer)preferences.get("rowSpace")).intValue();
		
		rowCountTF.setText(""+rowCount);
		listNumberTypeComboBox.setSelectedIndex(listNumberType);
		firstListNumberComboBox.setSelectedIndex(firstListNumber);
		tabWidthTF.setText(""+tabWidth);
		rowSpaceTF.setText(""+rowSpace);
		
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
		if(e.getSource().equals(listNumberTypeComboBox)) {
			int listNumberType = listNumberTypeComboBox.getSelectedIndex()-1;
			if(firstListNumberComboBox!=null) {
				firstListNumberComboBox.removeAllItems();
				for(int i=0 ; i<ListGenerator.listNumbers[listNumberType].length ; i++) {
					firstListNumberComboBox.addItem(ListGenerator.listNumbers[listNumberType][i]);
				}
			}
		}
		
	}
}
