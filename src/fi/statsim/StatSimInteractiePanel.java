package fi.statsim;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.TextField;
import java.awt.event.ActionListener;
import java.util.Hashtable;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JTextField;

import fi.beans.wiskopdrbeans.InteractieEditPanel;
import fi.beans.wiskopdrbeans.InteractiePanel;

public class StatSimInteractiePanel extends JPanel implements InteractiePanel {

	//// Te vervangen voorbeeldcode:
	private JTextField textField;
	private String opdrachtTekst = "";
	//// Einde voorbeeldcode
	
	public StatSimInteractiePanel() {
		setLayout(null);
		setBackground(Color.white);
		setBorder(BorderFactory.createLineBorder(new Color(202,228,255), 7));
		
		//// Te vervangen voorbeeldcode:
		textField = new JTextField();
		textField.setBounds(50,100,200,25);
		add(textField);
		//// Einde voorbeeldcode
	}
	
	public void paintComponent(Graphics g) {	
		super.paintComponent(g);
		//// Te vervangen voorbeeldcode:
		g.drawString(StatSim.rb.getString("welkomTekst"), 50, 60 );
		g.drawString(opdrachtTekst, 50, 80);
		//// Einde voorbeeldcode
	}
	//// Te vervangen voorbeeldcode:
	public void zetOpdrachtTekst(String opdrachtTekst) {
		this.opdrachtTekst = opdrachtTekst;
		repaint();
	}
	//// Einde voorbeeldcode
	
	public void setState(Hashtable h) {	
		//// Te vervangen voorbeeldcode:
		//haal de data uit de hashtabel
		String text = (String)h.get("text");
		
	    //herstel de state 
	    textField.setText(text);
	    //// Einde voorbeeldcode
	}
	
	public Hashtable getState() {	
		Hashtable h = new Hashtable();
		//// Te vervangen voorbeeldcode:
		//vraag de state op
		String text = textField.getText();
		
	    //voeg de gegevens toe aan de hashtable
	    h.put("text", text);
	    //// Einde voorbeeldcode  
	    return h;
	}
	
	
	public void zetOpdracht(Hashtable h, String[] randomVars, Hashtable randomValues) {
		//// Te vervangen voorbeeldcode:
		String opdrachtTekst = "";
		String text = "";
		
		if(h.containsKey("opdrachtTekst")) opdrachtTekst = (String)h.get("opdrachtTekst");
		if(h.containsKey("text")) text = (String)h.get("text");

		this.opdrachtTekst = opdrachtTekst;
		textField.setText(text);
		//// Einde voorbeeldcode
	}

	public void setEditState(Hashtable h) {
		//// Te vervangen voorbeeldcode:
		String opdrachtTekst = "";
		String text = "";
		
		if(h.containsKey("opdrachtTekst")) opdrachtTekst = (String)h.get("opdrachtTekst");
		if(h.containsKey("text")) text = (String)h.get("text");

		this.opdrachtTekst = opdrachtTekst;
		textField.setText(text);
		//// Einde voorbeeldcode
	}

	public Hashtable getEditState() {
		return getState();
	}

	public InteractieEditPanel getEditPanel() {
		return new StatSimInteractieEditPanel();
	}

	public void wis() {
		
	}

	public void zetMaat() {
		
	}

	public int geefAsHoogte() {
		return 0;
	}

	public int getIpId() {
		return 0;
	}

	public int getScore() {
		return 0;
	}

	public int[][] getScoreObjectives() {
		return null;
	}

	public int getScoreMax() {
		return 0;
	}

	public boolean isCorrect() {
		return false;
	}

	public boolean isFout() {
		return false;
	}

	public void zetMode(int mode) {
		
	}

	public void zetNagekeken(boolean b) {
		
	}

	public void stop() {
		
	}

	public void start() {
		
	}

	public void destroy() {
		
	}

	public void opnieuw() {
		
	}

	public void kijkNa() {
		
	}

	public void kijkNa(int stapNr) {
		
	}

	public void addActionListener(ActionListener al) {
		
	}

}
