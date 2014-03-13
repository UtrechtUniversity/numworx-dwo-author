package fi.statsim;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;

import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;

import fi.beans.wiskopdrbeans.InteractieEditPanel;

public class StatSimInteractieEditPanel extends JPanel implements InteractieEditPanel, ActionListener {

	private StatSimInteractiePanel interactiePanel;
	private JPanel optionsPanel;
	
	private int defaultWidth = 790;
	private int defaultHeight = 450;
	private int defaultIpWidth = 500;
	private int defaultOpWidth = 200;

	private JRadioButton dobbelstenenRadio;
	private JRadioButton muntenRadio;
	
	public StatSimInteractieEditPanel() {
		setLayout(null);
		
		interactiePanel = new StatSimInteractiePanel();
		interactiePanel.setBounds(10,20,defaultIpWidth,defaultHeight);
		add(interactiePanel);
		
		optionsPanel = new JPanel();
		optionsPanel.setLayout(null);
		optionsPanel.setBounds(defaultIpWidth+30,20,defaultOpWidth,defaultHeight);
		add(optionsPanel);
		
		muntenRadio=new JRadioButton("Munten");
		muntenRadio.setLocation(10,10);
		muntenRadio.setSize(200,20);
		optionsPanel.add(muntenRadio);
		
		dobbelstenenRadio=new JRadioButton("Dobbelstenen");
		dobbelstenenRadio.setLocation(10,30);
		dobbelstenenRadio.setSize(200,20);
		optionsPanel.add(dobbelstenenRadio);
		
		ButtonGroup buttonGroup1=new ButtonGroup();
		buttonGroup1.add(muntenRadio);
		buttonGroup1.add(dobbelstenenRadio);
	}
	
	public Hashtable getEditState() {
		Hashtable h = interactiePanel.getEditState();
		h.put("muntenRadio", new Boolean(muntenRadio.isSelected()));
		h.put("dobbelstenenRadio", new Boolean(dobbelstenenRadio.isSelected()));
		return h;
	}
	
	
	public void setEditState(Hashtable h) {
		Boolean muntenRadioBool=false;
		if(h.containsKey("muntenRadio")) muntenRadioBool= ((Boolean)h.get("muntenRadio")).booleanValue();
		muntenRadio.setSelected(muntenRadioBool);
		Boolean dobbelstenenRadioBool=false;
		if(h.containsKey("dobbelstenenRadio")) dobbelstenenRadioBool= ((Boolean)h.get("dobbelstenenRadio")).booleanValue();
		dobbelstenenRadio.setSelected(dobbelstenenRadioBool);
		
		interactiePanel.setEditState(h);
		
	}
	
	public void setBounds(int x, int y, int b, int h) {
		super.setBounds(x,y,b,h);
		optionsPanel.setBounds(b-defaultOpWidth-10, 20, defaultOpWidth, defaultHeight);
	}
	
	
	public void zetBreedte(int b) {
		interactiePanel.setSize(b,interactiePanel.getHeight());	
	}
	
	public void zetHoogte(int h) {
		interactiePanel.setSize(interactiePanel.getWidth(), h);	
	}
	
	public void wis() {
			
	}
	
	public void zetMode(int mode) {
			
	}
	
	public void stop() {
			
	}
	
	public void start() {
			
	}
	
	public void addActionListener(ActionListener al) {
			
	}

	public void actionPerformed(ActionEvent e) {
	}

}
