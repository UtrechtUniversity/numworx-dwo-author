package fi.sliderwidget;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JTextField;

import fi.beans.wiskopdrbeans.CBookAware;
import fi.beans.wiskopdrbeans.InteractieEditPanel;
import fi.beans.wiskopdrbeans.InteractiePanel;

import org.cbook.cbookif.*;

public class SliderWidgetInteractiePanel extends JPanel implements InteractiePanel, ActionListener, CBookAware {

	private SchuifParameter schuifParameter;
	private CBookEventHandler cbookEventHandler = new CBookEventHandler(this);	
	
	public SliderWidgetInteractiePanel() {
		setLayout(null);
		setBackground(Color.white);
		schuifParameter = new SchuifParameter(200,"a");
		schuifParameter.zetLocatie(0,0);
		schuifParameter.geefSlider().setBackground(getBackground());
		schuifParameter.geefSlider().addActionListener(this);
		
		add(schuifParameter.geefSlider(), 0);
		repaint();
		
		
		
	}
	
	public SchuifParameter geefParam()
	{
		return schuifParameter;
	}
	
	
	
	//// Einde voorbeeldcode
	
	public void setState(Hashtable h) {	
		String paramNaam = "a";
		double paramWaarde = 0;
		double paramOnderGrensWaarde= 0;
		double paramBovenGrensWaarde = 5;
		double paramStapGrootte = 0.1;
		int paramLengte = 100;
		
		if(h.containsKey("paramNaam")) paramNaam = (String)h.get("paramNaam");
		if(h.containsKey("paramWaarde")) paramWaarde = ((Double)h.get("paramWaarde")).doubleValue();
		if(h.containsKey("paramOnderGrensWaarde")) paramOnderGrensWaarde = ((Double)h.get("paramOnderGrensWaarde")).doubleValue();
		if(h.containsKey("paramBovenGrensWaarde")) paramBovenGrensWaarde = ((Double)h.get("paramBovenGrensWaarde")).doubleValue();
		if(h.containsKey("paramStapGrootte")) paramStapGrootte = ((Double)h.get("paramStapGrootte")).doubleValue();
		if(h.containsKey("paramLengte")) paramLengte = ((Integer)h.get("paramLengte")).intValue();
		
		schuifParameter.zetGrensWaarden(paramOnderGrensWaarde, paramBovenGrensWaarde);
		schuifParameter.zetWaarde(paramWaarde, false);
		schuifParameter.zetNaam(paramNaam);
		schuifParameter.zetStapGrootte(paramStapGrootte);
		schuifParameter.zetLengte(paramLengte); 
	}
	
	public Hashtable getState() {	
		String paramNaam = schuifParameter.geefNaam();
		double paramWaarde = schuifParameter.geefWaarde();
		double paramOnderGrensWaarde = schuifParameter.geefOnderGrens();
		double paramBovenGrensWaarde = schuifParameter.geefBovenGrens();
		double paramStapGrootte = schuifParameter.geefStapGrootte();
		int paramLengte = schuifParameter.geefLengte();
		
		Hashtable h = new Hashtable();
		
		h.put("paramNaam", paramNaam);
		h.put("paramWaarde", paramWaarde);
		h.put("paramOnderGrensWaarde", paramOnderGrensWaarde);
		h.put("paramBovenGrensWaarde", paramBovenGrensWaarde);
		h.put("paramStapGrootte", paramStapGrootte);
		h.put("paramLengte", paramLengte); 
	    return h;
	}
	
	
	public void zetOpdracht(Hashtable h, String[] randomVars, Hashtable randomValues) {
		setState(h);
	}

	public void setEditState(Hashtable h) {
		setState(h);
	}

	public Hashtable getEditState() {
		return getState();
	}

	public InteractieEditPanel getEditPanel() {
		return new SliderWidgetInteractieEditPanel();
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

	@Override
	public void addCBookEventListener(CBookEventListener listener, String command) {
		cbookEventHandler.addCBookEventListener(listener, command);
		
	}

	@Override
	public void removeCBookEventListener(CBookEventListener listener,String command) {
		cbookEventHandler.removeCBookEventListener(listener, command);
		
	}

	@Override
	public String[] getSendCmds() {
		String[] commands = {"parameterwaarde"};
		return commands;
	}

	@Override
	public String[] getAcceptedCmds() {
		String[] commands = {"parameterwaarde"};
		return commands;
	}

	@Override
	public void acceptCBookEvent(CBookEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == schuifParameter.geefSlider())
		{ 	double waarde = schuifParameter.geefDoubleStand();
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("name", schuifParameter.geefNaam());
			map.put("value", new Double(schuifParameter.geefDoubleStand()));
			cbookEventHandler.fire("parameterwaarde",map);
		}
		
	}

}
