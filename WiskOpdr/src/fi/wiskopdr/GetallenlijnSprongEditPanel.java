package fi.wiskopdr;

import java.awt.AWTEvent;
import java.awt.Event;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.Hashtable;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import fi.beans.wiskopdrbeans.InteractieEditPanel;
import fi.wiskopdr.expressies.Expressie;
import fi.wiskopdr.formuleobjects.FormuleParser;

public class GetallenlijnSprongEditPanel extends JPanel implements ActionListener, FocusListener, InteractieEditPanel {

	private GetallenlijnSprongPanel getallenlijnSprongPanel;
	
	private int minWaarde = -3;
	private int maxWaarde = 4;
	
	private JLabel minWaardeLabel;
	private JTextField minWaardeTF;
	
	private JLabel maxWaardeLabel;
	private JTextField maxWaardeTF;
	
	private JLabel eenheidWaardeLabel;
	private JTextField eenheidWaardeTF;
	
	private JCheckBox eenhedenZichtbaarCB;
	private JCheckBox nulZichtbaarCB;
	private JCheckBox pijlOmlaagCB;
	private JCheckBox pijlZichtbaarCB;
	private JCheckBox vijftallenZichtbaarCB;
	private JCheckBox tientallenZichtbaarCB;
	private JCheckBox horizontaalCB;
	private JCheckBox eenhedenNummersCB;
	private JCheckBox tientallenNummersCB;
	private JCheckBox vijftallenNummersCB;
	
	private JPanel cp;
	
	public GetallenlijnSprongEditPanel()
	{
		setLayout(null);
		
		cp = new JPanel();
		cp.setLayout(null);
		cp.setOpaque(false);
		cp.setBounds(400,150,400,650);
		add(cp);
		
		getallenlijnSprongPanel = new GetallenlijnSprongPanel();
		getallenlijnSprongPanel.setBounds(40,30,40,300);
		add(getallenlijnSprongPanel);
		
		minWaardeLabel = new JLabel(WiskOpdr.rb.getString("GSEP_minValue"));//"kleinste waarde");
		minWaardeLabel.setBounds(0,40,140,20);
		cp.add(minWaardeLabel);
		
		minWaardeTF = new JTextField("-3");
		minWaardeTF.setBounds(140,40,80,20);
		minWaardeTF.addActionListener(this);
		minWaardeTF.addFocusListener(this);
		cp.add(minWaardeTF);
		
		maxWaardeLabel = new JLabel(WiskOpdr.rb.getString("GSEP_maxValue"));//"grootste waarde");
		maxWaardeLabel.setBounds(0,70,140,20);
		cp.add(maxWaardeLabel);
		
		maxWaardeTF = new JTextField("4");
		maxWaardeTF.setBounds(140,70,80,20);
		maxWaardeTF.addActionListener(this);
		maxWaardeTF.addFocusListener(this);
		cp.add(maxWaardeTF);
		
		eenhedenZichtbaarCB = new JCheckBox(WiskOpdr.rb.getString("GSEP_eenheden"));//"eenheden zichtbaar");
		eenhedenZichtbaarCB.setBounds(0,100,150,20);
		eenhedenZichtbaarCB.setOpaque(false);
		eenhedenZichtbaarCB.addActionListener(this);
		cp.add(eenhedenZichtbaarCB);
		
		eenhedenNummersCB = new JCheckBox(WiskOpdr.rb.getString("GSEP_getallen"));//"getallen");
		eenhedenNummersCB.setBounds(150,100,180,20);
		eenhedenNummersCB.setOpaque(false);
		eenhedenNummersCB.addActionListener(this);
		cp.add(eenhedenNummersCB);
		
		tientallenZichtbaarCB = new JCheckBox(WiskOpdr.rb.getString("GSEP_10tallen"));//"10-tallen zichtbaar");
		tientallenZichtbaarCB.setBounds(0,130,150,20);
		tientallenZichtbaarCB.setOpaque(false);
		tientallenZichtbaarCB.addActionListener(this);
		cp.add(tientallenZichtbaarCB);
		
		tientallenNummersCB = new JCheckBox(WiskOpdr.rb.getString("GSEP_getallen"));//"getallen");
		tientallenNummersCB.setBounds(150,130,180,20);
		tientallenNummersCB.setOpaque(false);
		tientallenNummersCB.addActionListener(this);
		cp.add(tientallenNummersCB);
		
		vijftallenZichtbaarCB = new JCheckBox(WiskOpdr.rb.getString("GSEP_5tallen"));//"5-tallen zichtbaar");
		vijftallenZichtbaarCB.setBounds(0,160,150,20);
		vijftallenZichtbaarCB.setOpaque(false);
		vijftallenZichtbaarCB.addActionListener(this);
		cp.add(vijftallenZichtbaarCB);
		
		vijftallenNummersCB = new JCheckBox(WiskOpdr.rb.getString("GSEP_getallen"));//"getallen");
		vijftallenNummersCB.setBounds(150,160,180,20);
		vijftallenNummersCB.setOpaque(false);
		vijftallenNummersCB.addActionListener(this);
		cp.add(vijftallenNummersCB);
		
		nulZichtbaarCB = new JCheckBox(WiskOpdr.rb.getString("GSEP_nul"));//"nul zichtbaar");
		nulZichtbaarCB.setBounds(0,190,280,20);
		nulZichtbaarCB.setOpaque(false);
		nulZichtbaarCB.addActionListener(this);
		cp.add(nulZichtbaarCB);
		
		pijlZichtbaarCB = new JCheckBox(WiskOpdr.rb.getString("GSEP_pijl"));//"pijl zichtbaar");
		pijlZichtbaarCB.setBounds(0,220,280,20);
		pijlZichtbaarCB.setOpaque(false);
		pijlZichtbaarCB.setSelected(true);
		pijlZichtbaarCB.addActionListener(this);
		cp.add(pijlZichtbaarCB);
		
		pijlOmlaagCB = new JCheckBox(WiskOpdr.rb.getString("GSEP_pijlTerug"));//"pijl terug");
		pijlOmlaagCB.setBounds(0,250,280,20);
		pijlOmlaagCB.setOpaque(false);
		pijlOmlaagCB.addActionListener(this);
		cp.add(pijlOmlaagCB);
		
		horizontaalCB = new JCheckBox(WiskOpdr.rb.getString("GSEP_horizontaal"));//"horizontaal");
		horizontaalCB.setBounds(0,290,280,20);
		horizontaalCB.setOpaque(false);
		horizontaalCB.addActionListener(this);
		cp.add(horizontaalCB);
		
		eenheidWaardeLabel = new JLabel(WiskOpdr.rb.getString("GSEP_eenheid"));//"eenheid");
		eenheidWaardeLabel.setBounds(0,320,140,20);
		cp.add(eenheidWaardeLabel);
		
		eenheidWaardeTF = new JTextField("1");
		eenheidWaardeTF.setBounds(140,320,80,20);
		eenheidWaardeTF.addActionListener(this);
		eenheidWaardeTF.addFocusListener(this);
		cp.add(eenheidWaardeTF);
	}
	
	public void focusGained(FocusEvent e){
	}
	
	public void focusLost(FocusEvent e){
		invulActie(e);
	}
	
	public void invulActie(AWTEvent e) {
		if(e.getSource()==minWaardeTF)
		{	Expressie expressie = FormuleParser.geefExpressie("$f" + minWaardeTF.getText() + "@");
			if(expressie!=null) getallenlijnSprongPanel.zetMinWaarde(expressie.geefWaarde());
		}
		if(e.getSource()==maxWaardeTF)
		{	Expressie expressie = FormuleParser.geefExpressie("$f" + maxWaardeTF.getText() + "@");
			if(expressie!=null) getallenlijnSprongPanel.zetMaxWaarde(expressie.geefWaarde());
		}
		if(e.getSource()==eenheidWaardeTF)
		{	Expressie expressie = FormuleParser.geefExpressie("$f" + eenheidWaardeTF.getText() + "@");
			if(Math.abs(expressie.geefWaarde())<0.0000000001){
				eenheidWaardeTF.setText("1");
				expressie = FormuleParser.geefExpressie("$f" + eenheidWaardeTF.getText() + "@");
			}
			if(expressie!=null) getallenlijnSprongPanel.zetEenheidWaarde(expressie.geefWaarde());
		}
	}
	
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==minWaardeTF)
		{	invulActie(e);
		}
		if(e.getSource()==maxWaardeTF)
		{	invulActie(e);
		}
		if(e.getSource()==eenheidWaardeTF)
		{	invulActie(e);
		}
		if(e.getSource()==eenhedenZichtbaarCB)
		{	getallenlijnSprongPanel.zetEenhedenZichtbaar(eenhedenZichtbaarCB.isSelected());
		}
		if(e.getSource()==nulZichtbaarCB)
		{	getallenlijnSprongPanel.zetNulZichtbaar(nulZichtbaarCB.isSelected());
		}
		if(e.getSource()==pijlZichtbaarCB)
		{	getallenlijnSprongPanel.zetPijlZichtbaar(pijlZichtbaarCB.isSelected());
		}
		if(e.getSource()==pijlOmlaagCB)
		{	getallenlijnSprongPanel.zetPijlOmlaag(pijlOmlaagCB.isSelected());
		}
		if(e.getSource()==tientallenZichtbaarCB)
		{	getallenlijnSprongPanel.zetTientallenZichtbaar(tientallenZichtbaarCB.isSelected());
		}
		if(e.getSource()==vijftallenZichtbaarCB)
		{	getallenlijnSprongPanel.zetVijftallenZichtbaar(vijftallenZichtbaarCB.isSelected());
		}
		if(e.getSource()==eenhedenNummersCB)
		{	getallenlijnSprongPanel.zetEenhedenNummers(eenhedenNummersCB.isSelected());
		}
		if(e.getSource()==vijftallenNummersCB)
		{	getallenlijnSprongPanel.zetVijftallenNummers(vijftallenNummersCB.isSelected());
		}
		if(e.getSource()==tientallenNummersCB)
		{	getallenlijnSprongPanel.zetTientallenNummers(tientallenNummersCB.isSelected());
		}
		if(e.getSource()==horizontaalCB)
		{	getallenlijnSprongPanel.zetHorizontaal(horizontaalCB.isSelected());
		}

	}

	public void addActionListener(ActionListener al) {
		// TODO Auto-generated method stub

	}

	public Hashtable getEditState() {
		boolean eenhedenZichtbaar;
		boolean vijftallenZichtbaar;
		boolean tientallenZichtbaar;
		boolean eenhedenNummers;
		boolean vijftallenNummers;
		boolean tientallenNummers;
		boolean horizontaal;
	    boolean nulZichtbaar;
	    boolean pijlOmlaag;
	    boolean pijlZichtbaar;
	    String minWaardeString;
	    String maxWaardeString;
	    String eenheidWaardeString;
		
	    eenhedenZichtbaar = eenhedenZichtbaarCB.isSelected();
	    vijftallenZichtbaar = vijftallenZichtbaarCB.isSelected();
	    tientallenZichtbaar = tientallenZichtbaarCB.isSelected();
	    eenhedenNummers = eenhedenNummersCB.isSelected();
	    vijftallenNummers = vijftallenNummersCB.isSelected();
	    tientallenNummers = tientallenNummersCB.isSelected();
	    horizontaal = horizontaalCB.isSelected();
	    nulZichtbaar = nulZichtbaarCB.isSelected();
	    pijlZichtbaar = pijlZichtbaarCB.isSelected();
	    pijlOmlaag = pijlOmlaagCB.isSelected();
	    minWaardeString = minWaardeTF.getText();
	    maxWaardeString = maxWaardeTF.getText();
	    eenheidWaardeString = eenheidWaardeTF.getText();
	    
		Hashtable h = new Hashtable();
		h.put("eenhedenZichtbaar", new Boolean(eenhedenZichtbaar));
		h.put("vijftallenZichtbaar", new Boolean(vijftallenZichtbaar));
		h.put("tientallenZichtbaar", new Boolean(tientallenZichtbaar));
		h.put("eenhedenNummers", new Boolean(eenhedenNummers));
		h.put("vijftallenNummers", new Boolean(vijftallenNummers));
		h.put("tientallenNummers", new Boolean(tientallenNummers));
		h.put("horizontaal", new Boolean(horizontaal));
		h.put("nulZichtbaar", new Boolean(nulZichtbaar));
		h.put("pijlZichtbaar", new Boolean(pijlZichtbaar));
		h.put("pijlOmlaag", new Boolean(pijlOmlaag));
		h.put("minWaardeString", new String(minWaardeString));
		h.put("maxWaardeString", new String(maxWaardeString));
		h.put("eenheidWaardeString", new String(eenheidWaardeString));
		
		return h;
	}

	public void setBounds(int x, int y, int b, int h) {
		super.setBounds(x,y,b,h);

	}

	public void setEditState(Hashtable h) {
		boolean eenhedenZichtbaar = false;
		boolean vijftallenZichtbaar = false;
		boolean tientallenZichtbaar = false;
		boolean eenhedenNummers = false;
		boolean vijftallenNummers = false;
		boolean tientallenNummers = false;
		boolean horizontaal = false;
	    boolean nulZichtbaar = true;
	    boolean pijlOmlaag = false;
	    boolean pijlZichtbaar = true;
	    String minWaardeString = "-3";
	    String maxWaardeString = "-4";
	    String eenheidWaardeString = "1";
		
	    if(h.containsKey("eenhedenZichtbaar")) eenhedenZichtbaar = ((Boolean)h.get("eenhedenZichtbaar")).booleanValue();
	    if(h.containsKey("vijftallenZichtbaar")) vijftallenZichtbaar = ((Boolean)h.get("vijftallenZichtbaar")).booleanValue();
	    if(h.containsKey("tientallenZichtbaar")) tientallenZichtbaar = ((Boolean)h.get("tientallenZichtbaar")).booleanValue();
	    if(h.containsKey("eenhedenNummers")) eenhedenNummers = ((Boolean)h.get("eenhedenNummers")).booleanValue();
	    if(h.containsKey("vijftallenNummers")) vijftallenNummers = ((Boolean)h.get("vijftallenNummers")).booleanValue();
	    if(h.containsKey("tientallenNummers")) tientallenNummers = ((Boolean)h.get("tientallenNummers")).booleanValue();
	    if(h.containsKey("horizontaal")) horizontaal = ((Boolean)h.get("horizontaal")).booleanValue();
	    if(h.containsKey("nulZichtbaar")) nulZichtbaar = ((Boolean)h.get("nulZichtbaar")).booleanValue();
	    if(h.containsKey("pijlZichtbaar")) pijlZichtbaar = ((Boolean)h.get("pijlZichtbaar")).booleanValue();
	    if(h.containsKey("pijlOmlaag")) pijlOmlaag = ((Boolean)h.get("pijlOmlaag")).booleanValue();
	    if(h.containsKey("minWaardeString")) minWaardeString = (String)h.get("minWaardeString");
	    if(h.containsKey("maxWaardeString")) maxWaardeString = (String)h.get("maxWaardeString");
	    if(h.containsKey("eenheidWaardeString")) eenheidWaardeString = (String)h.get("eenheidWaardeString");
	    
	    eenhedenZichtbaarCB.setSelected(eenhedenZichtbaar);
	    vijftallenZichtbaarCB.setSelected(vijftallenZichtbaar);
	    tientallenZichtbaarCB.setSelected(tientallenZichtbaar);
	    eenhedenNummersCB.setSelected(eenhedenNummers);
	    vijftallenNummersCB.setSelected(vijftallenNummers);
	    tientallenNummersCB.setSelected(tientallenNummers);
	    horizontaalCB.setSelected(horizontaal);
		nulZichtbaarCB.setSelected(nulZichtbaar);
		pijlZichtbaarCB.setSelected(pijlZichtbaar);
	    pijlOmlaagCB.setSelected(pijlOmlaag);
	    minWaardeTF.setText(minWaardeString);
	    maxWaardeTF.setText(maxWaardeString);
	    eenheidWaardeTF.setText(eenheidWaardeString);
	    
	    getallenlijnSprongPanel.zetEenhedenZichtbaar(eenhedenZichtbaarCB.isSelected());
	    getallenlijnSprongPanel.zetVijftallenZichtbaar(vijftallenZichtbaarCB.isSelected());
	    getallenlijnSprongPanel.zetTientallenZichtbaar(tientallenZichtbaarCB.isSelected());
	    getallenlijnSprongPanel.zetEenhedenNummers(eenhedenNummersCB.isSelected());
	    getallenlijnSprongPanel.zetVijftallenNummers(vijftallenNummersCB.isSelected());
	    getallenlijnSprongPanel.zetTientallenNummers(tientallenNummersCB.isSelected());
	    getallenlijnSprongPanel.zetHorizontaal(horizontaalCB.isSelected());
	    getallenlijnSprongPanel.zetNulZichtbaar(nulZichtbaarCB.isSelected());
	    getallenlijnSprongPanel.zetPijlZichtbaar(pijlZichtbaarCB.isSelected());
	    getallenlijnSprongPanel.zetPijlOmlaag(pijlOmlaagCB.isSelected());
	    Expressie expressie = FormuleParser.geefExpressie("$f" + minWaardeTF.getText() + "@");
		if(expressie!=null) getallenlijnSprongPanel.zetMinWaarde(expressie.geefWaarde());
		expressie = FormuleParser.geefExpressie("$f" + maxWaardeTF.getText() + "@");
		if(expressie!=null) getallenlijnSprongPanel.zetMaxWaarde(expressie.geefWaarde());
		expressie = FormuleParser.geefExpressie("$f" + eenheidWaardeTF.getText() + "@");
		if(expressie!=null) getallenlijnSprongPanel.zetEenheidWaarde(expressie.geefWaarde());
		
		

	}

	public void start() {
		// TODO Auto-generated method stub

	}

	public void stop() {
		// TODO Auto-generated method stub

	}

	public void wis() {
		// TODO Auto-generated method stub

	}

	public void zetBreedte(int b) {
		getallenlijnSprongPanel.setSize(b, getallenlijnSprongPanel.getHeight());

	}

	public void zetHoogte(int h) {
		getallenlijnSprongPanel.setSize(getallenlijnSprongPanel.getWidth(),h);

	}

	public void zetMode(int mode) {
		// TODO Auto-generated method stub

	}

}
