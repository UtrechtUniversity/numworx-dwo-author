package fi.wiskopdr.templatecomponents;

import java.awt.AWTEventMulticaster;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import fi.wiskopdr.DialogFacade;
import fi.wiskopdr.WiskOpdr;
import fi.wiskopdr.tekstobjects.TekstVak;

public class ListEditor implements TComponentEditor, ActionListener {

	private TekstVak tekstVak;
	private Font font = new Font("SansSerif",Font.PLAIN,12);//WiskOpdr.tekstFont;
	
	private DialogFacade frame;
	private JPanel preferencesPanel;
	private JPanel mainPanel, bottomPanel;
	private JButton okButton, cancelButton;
	
	private JLabel rowCountLabel;
	private JTextField rowCountTF;
	private JLabel listNumberTypeLabel;
	private JComboBox listNumberTypeComboBox;
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
		mainPanel = new JPanel();
		mainPanel.setBackground(WiskOpdr.bgcolorEditor);
		bottomPanel = new JPanel();
		
		rowCountLabel = new JLabel(WiskOpdr.rb.getString("TCOMP_list_rowCount"));
		rowCountLabel.setFont(font);
		
		rowCountTF = new JTextField();
		rowCountTF.addActionListener(this);
		rowCountTF.setPreferredSize(new Dimension(30,24));
		rowCountTF.setMaximumSize(new Dimension(30,24));
		
		listNumberTypeLabel = new JLabel(WiskOpdr.rb.getString("TCOMP_list_numberType"));
		listNumberTypeLabel.setFont(font);
		
		listNumberTypeComboBox = new JComboBox();
		listNumberTypeComboBox.setFont(font);
		listNumberTypeComboBox.addItem(WiskOpdr.rb.getString("TCOMP_list_chooseType"));
		for(int i=0 ; i<ListGenerator.listNumbers.length ; i++) {
			listNumberTypeComboBox.addItem(ListGenerator.listNumbers[i][0]+" ,"+ListGenerator.listNumbers[i][1]+" ,"+ListGenerator.listNumbers[i][2]+" , ...");
		}
		
		
		tabWidthLabel = new JLabel(WiskOpdr.rb.getString("TCOMP_list_tabWidth"));
		tabWidthLabel.setFont(font);
		
		tabWidthTF = new JTextField();
		tabWidthTF.addActionListener(this);
		tabWidthTF.setPreferredSize(new Dimension(30,24));
		tabWidthTF.setMaximumSize(new Dimension(30,24));
		
		rowSpaceLabel = new JLabel(WiskOpdr.rb.getString("TCOMP_list_rowSpace"));
		rowSpaceLabel.setFont(font);
		
		rowSpaceTF = new JTextField();
		rowSpaceTF.addActionListener(this);
		rowSpaceTF.setPreferredSize(new Dimension(30,24));
		rowSpaceTF.setMaximumSize(new Dimension(30,24));
		
		Box boxv = Box.createVerticalBox();
		
		Box boxh = Box.createHorizontalBox();
		boxh.add(rowCountLabel);
		boxh.add(Box.createHorizontalStrut(10));
		boxh.add(Box.createHorizontalGlue());
		boxh.add(rowCountTF);
		
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
		
		mainPanel.add(boxv);
		
		preferencesPanel.add(bottomPanel,BorderLayout.SOUTH);
		preferencesPanel.add(mainPanel,BorderLayout.CENTER);
		
		okButton = new JButton("Ok");//
		okButton.setFont(font);
		okButton.addActionListener(this);
		bottomPanel.add(okButton);
		
		cancelButton = new JButton("Cancel");//
		cancelButton.setFont(font);
		cancelButton.addActionListener(this);
		bottomPanel.add(cancelButton);
	}
	
	public void makeFrame(){
	   	frame = DialogFacade.newInstance(tekstVak, WiskOpdr.rb.getString("TCOMP_list"), true);
	   	//Dimension preferredSize = new Dimension(400,320);
		//frame.setPreferredSize(preferredSize);
	    frame.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
	    //frame.setSize(preferredSize);
	    frame.getContentPane().setLayout(new BorderLayout());
	    frame.getContentPane().add(preferencesPanel);
	    frame.getContentPane().add(bottomPanel,BorderLayout.SOUTH);
	    frame.pack();
	    frame.setLocation(tekstVak.getLocationOnScreen().x, tekstVak.getLocationOnScreen().y);
		//frame.setVisible(true);
		    
	}
	
	@Override
	public Hashtable<String,Object> getPreferences() {
		
		int rowCount = ListGenerator.initialRowCount;
		int listNumberType = ListGenerator.initialListNumberType;
		int tabWidth = ListGenerator.initialTabWidth;
		int rowSpace = ListGenerator.initialRowSpace;
		
		rowCount = Integer.parseInt(rowCountTF.getText());
		listNumberType = listNumberTypeComboBox.getSelectedIndex()-1;
		tabWidth = Integer.parseInt(tabWidthTF.getText());
		rowSpace = Integer.parseInt(rowSpaceTF.getText());
		
		Hashtable<String,Object> preferences = new Hashtable<String,Object>();
		
		preferences.put("rowCount", new Integer(rowCount));
		preferences.put("listNumberType", new Integer(listNumberType));
		preferences.put("tabWidth", new Integer(tabWidth));
		preferences.put("rowSpace", new Integer(rowSpace));
		return preferences;
	}

	@Override
	public void setPreferences(Hashtable<String,Object> preferences) {
		
		int rowCount = ListGenerator.initialRowCount;
		int listNumberType = ListGenerator.initialListNumberType;
		int tabWidth = ListGenerator.initialTabWidth;
		int rowSpace = ListGenerator.initialRowSpace;
		
		if(preferences.containsKey("rowCount")) rowCount = ((Integer)preferences.get("rowCount")).intValue();
		if(preferences.containsKey("listNumberType")) listNumberType = ((Integer)preferences.get("listNumberType")).intValue()+1;
		if(preferences.containsKey("tabWidth")) tabWidth = ((Integer)preferences.get("tabWidth")).intValue();
		if(preferences.containsKey("rowSpace")) rowSpace = ((Integer)preferences.get("rowSpace")).intValue();
		
		rowCountTF.setText(""+rowCount);
		listNumberTypeComboBox.setSelectedIndex(listNumberType);
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
		
	}
}
