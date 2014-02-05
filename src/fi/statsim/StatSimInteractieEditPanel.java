package fi.statsim;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;

import javax.swing.JPanel;
import javax.swing.JTextField;

import fi.beans.wiskopdrbeans.InteractieEditPanel;

public class StatSimInteractieEditPanel extends JPanel implements InteractieEditPanel, ActionListener {

	private StatSimInteractiePanel interactiePanel;
	private JPanel optionsPanel;
	
	private int defaultWidth = 800;
	private int defaultHeight = 500;
	private int defaultIpWidth = 500;
	private int defaultOpWidth = 260;
	
	//// Te vervangen voorbeeldcode:
	private JTextField opdrachtTekstTF;
	//// Einde voorbeelcode
	
	public StatSimInteractieEditPanel() {
		setLayout(null);
		
		interactiePanel = new StatSimInteractiePanel();
		interactiePanel.setBounds(10,20,defaultIpWidth,defaultHeight);
		add(interactiePanel);
		
		optionsPanel = new JPanel();
		optionsPanel.setLayout(null);
		optionsPanel.setBounds(defaultIpWidth+30,20,defaultOpWidth,defaultHeight);
		add(optionsPanel);
		
		//// Te vervangen voorbeeldcode:
		opdrachtTekstTF = new JTextField();
		opdrachtTekstTF.setBounds(20,60,200,20);
		opdrachtTekstTF.addActionListener(this);
		optionsPanel.add(opdrachtTekstTF);
		//// Einde voorbeelcode
	}
	
	public Hashtable getEditState() {
		Hashtable h = interactiePanel.getEditState();
		//// Te vervangen voorbeeldcode:
		String opdrachtTekst = opdrachtTekstTF.getText();
		
		h.put("opdrachtTekst", opdrachtTekst);
		//// Einde voorbeelcode
		return h;
	}
	
	
	public void setEditState(Hashtable h) {
		//// Te vervangen voorbeeldcode:
		String opdrachtTekst = "";
		
		if(h.containsKey("opdrachtTekst")) opdrachtTekst = (String)h.get("opdrachtTekst");
		interactiePanel.setEditState(h);
		
		opdrachtTekstTF.setText(opdrachtTekst);
		//// Einde voorbeelcode
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
		//// Te vervangen voorbeeldcode:
		if(e.getSource()==opdrachtTekstTF) {
			interactiePanel.zetOpdrachtTekst(opdrachtTekstTF.getText());
		}
		//// Einde voorbeelcode
	}

}
