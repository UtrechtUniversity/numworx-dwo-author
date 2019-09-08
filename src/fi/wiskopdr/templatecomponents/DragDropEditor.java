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
import fi.wiskopdr.WiskOpdrTextField;
import fi.wiskopdr.tekstobjects.TekstVak;

public class DragDropEditor implements TComponentEditor, ActionListener {

	private TekstVak tekstVak;
	private Font font = new Font("SansSerif",Font.PLAIN,12);//WiskOpdr.tekstFont;
	
	private DialogFacade frame;
	private JPanel preferencesPanel;
	private JPanel topPanel, mainPanel, bottomPanel;
	private JButton okButton, cancelButton;
	
	private JLabel titleLabel;
	private JLabel itemCountLabel;
	private JTextField itemCountTF;
	private JLabel itemWidthLabel;
	private JTextField itemWidthTF;
	private JLabel itemHeightLabel;
	private JTextField itemHeightTF;
	private JLabel rowSpaceLabel;
	private JTextField rowSpaceTF;
	private JLabel descrWidthLabel;
	private JTextField descrWidthTF;
	
	
	
	
	public DragDropEditor(TekstVak tekstVak) {
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
		topPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));
		
		mainPanel = new JPanel();
		mainPanel.setBackground(WiskOpdr.bgcolorEditor);
		mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		
		bottomPanel = new JPanel();
		bottomPanel.setBackground(WiskOpdr.colorGray2);
		bottomPanel.setBorder(BorderFactory.createLineBorder(WiskOpdr.colorGray2, 2));
		
		titleLabel = new JLabel(WiskOpdr.rb.getString("TCOMP_drag_settings"));
		titleLabel.setForeground(WiskOpdr.colorGray3);
		titleLabel.setFont(font.deriveFont(Font.BOLD, 14));
		
		itemCountLabel = new JLabel(WiskOpdr.rb.getString("TCOMP_drag_rowCount"));
		itemCountLabel.setFont(font);
		
		itemCountTF = new WiskOpdrTextField("");
		itemCountTF.addActionListener(this);
		itemCountTF.setPreferredSize(new Dimension(30,24));
		itemCountTF.setMaximumSize(new Dimension(30,24));
		
		itemWidthLabel = new JLabel(WiskOpdr.rb.getString("TCOMP_drag_itemWidth"));
		itemWidthLabel.setFont(font);
		
		itemWidthTF = new WiskOpdrTextField("");
		itemWidthTF.addActionListener(this);
		itemWidthTF.setPreferredSize(new Dimension(30,24));
		itemWidthTF.setMaximumSize(new Dimension(30,24));
		
		itemHeightLabel = new JLabel(WiskOpdr.rb.getString("TCOMP_drag_itemHeight"));
		itemHeightLabel.setFont(font);
		
		itemHeightTF = new WiskOpdrTextField("");
		itemHeightTF.addActionListener(this);
		itemHeightTF.setPreferredSize(new Dimension(30,24));
		itemHeightTF.setMaximumSize(new Dimension(30,24));
		
		rowSpaceLabel = new JLabel(WiskOpdr.rb.getString("TCOMP_drag_rowSpace"));
		rowSpaceLabel.setFont(font);
		
		rowSpaceTF = new WiskOpdrTextField("");
		rowSpaceTF.addActionListener(this);
		rowSpaceTF.setPreferredSize(new Dimension(30,24));
		rowSpaceTF.setMaximumSize(new Dimension(30,24));
		
		descrWidthLabel = new JLabel(WiskOpdr.rb.getString("TCOMP_drag_descrWidth"));
		descrWidthLabel.setFont(font);
		
		descrWidthTF = new WiskOpdrTextField("");
		descrWidthTF.addActionListener(this);
		descrWidthTF.setPreferredSize(new Dimension(30,24));
		descrWidthTF.setMaximumSize(new Dimension(30,24));
		
		Box boxv = Box.createVerticalBox();
		
		Box boxh = Box.createHorizontalBox();
		boxh.add(itemCountLabel);
		boxh.add(Box.createHorizontalStrut(20));
		boxh.add(Box.createHorizontalGlue());
		boxh.add(itemCountTF);
		
		boxv.add(boxh);
		boxv.add(Box.createVerticalStrut(5));
		
		boxh = Box.createHorizontalBox();
		boxh.add(itemCountLabel);
		boxh.add(Box.createHorizontalStrut(20));
		boxh.add(Box.createHorizontalGlue());
		boxh.add(itemCountTF);
		
		boxv.add(boxh);
		boxv.add(Box.createVerticalStrut(5));
		
		boxh = Box.createHorizontalBox();
		boxh.add(itemWidthLabel);
		boxh.add(Box.createHorizontalStrut(20));
		boxh.add(Box.createHorizontalGlue());
		boxh.add(itemWidthTF);
		
		boxv.add(boxh);
		boxv.add(Box.createVerticalStrut(5));
		
		boxh = Box.createHorizontalBox();
		boxh.add(itemHeightLabel);
		boxh.add(Box.createHorizontalStrut(20));
		boxh.add(Box.createHorizontalGlue());
		boxh.add(itemHeightTF);
		
		boxv.add(boxh);
		boxv.add(Box.createVerticalStrut(5));
		
		boxh = Box.createHorizontalBox();
		boxh.add(rowSpaceLabel);
		boxh.add(Box.createHorizontalStrut(20));
		boxh.add(Box.createHorizontalGlue());
		boxh.add(rowSpaceTF);
				
		boxv.add(boxh);
		boxv.add(Box.createVerticalStrut(5));
		
		boxh = Box.createHorizontalBox();
		boxh.add(descrWidthLabel);
		boxh.add(Box.createHorizontalStrut(10));
		boxh.add(Box.createHorizontalGlue());
		boxh.add(descrWidthTF);
				
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
	   	frame = DialogFacade.newInstance(tekstVak, WiskOpdr.rb.getString("TCOMP_drag"), true);
	    frame.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
	    frame.getContentPane().setLayout(new BorderLayout());
	    frame.getContentPane().add(preferencesPanel);
	    frame.getContentPane().add(bottomPanel,BorderLayout.SOUTH);
	    frame.pack();
	    frame.setLocation(tekstVak.getLocationOnScreen().x, tekstVak.getLocationOnScreen().y);
	}
	
	@Override
	public Hashtable<String,Object> getPreferences() {
		
		int itemCount = DragDropGenerator.initialItemCount;
		int itemWidth = DragDropGenerator.initialItemWidth;
		int itemHeight = DragDropGenerator.initialItemHeight;
		int rowSpace = DragDropGenerator.initialRowSpace;
		int descrWidth = DragDropGenerator.initialDescrWidth;
		
		itemCount = Integer.parseInt(itemCountTF.getText());
		itemWidth = Integer.parseInt(itemWidthTF.getText());
		itemHeight = Integer.parseInt(itemHeightTF.getText());
		rowSpace = Integer.parseInt(rowSpaceTF.getText());
		descrWidth = Integer.parseInt(descrWidthTF.getText());
		
		Hashtable<String,Object> preferences = new Hashtable<String,Object>();
		
		preferences.put("itemCount", new Integer(itemCount));
		preferences.put("itemWidth", new Integer(itemWidth));
		preferences.put("itemHeight", new Integer(itemHeight));
		preferences.put("rowSpace", new Integer(rowSpace));
		preferences.put("descrWidth", new Integer(descrWidth));
		return preferences;
	}

	@Override
	public void setPreferences(Hashtable<String,Object> preferences) {
		
		int itemCount = DragDropGenerator.initialItemCount;
		int itemWidth = DragDropGenerator.initialItemWidth;
		int itemHeight = DragDropGenerator.initialItemHeight;
		int rowSpace = DragDropGenerator.initialRowSpace;
		int descrWidth = DragDropGenerator.initialDescrWidth;
		
		if(preferences.containsKey("itemCount")) itemCount = ((Integer)preferences.get("itemCount")).intValue();
		if(preferences.containsKey("itemWidth")) itemWidth = ((Integer)preferences.get("itemWidth")).intValue();
		if(preferences.containsKey("itemHeight")) itemHeight = ((Integer)preferences.get("itemHeight")).intValue();
		if(preferences.containsKey("rowSpace")) rowSpace = ((Integer)preferences.get("rowSpace")).intValue();
		if(preferences.containsKey("descrWidth")) descrWidth = ((Integer)preferences.get("descrWidth")).intValue();
		
		itemCountTF.setText(""+itemCount);
		itemWidthTF.setText(""+itemWidth);
		itemHeightTF.setText(""+itemHeight);
		rowSpaceTF.setText(""+rowSpace);
		descrWidthTF.setText(""+descrWidth);
	}
	
	public void show(){
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
