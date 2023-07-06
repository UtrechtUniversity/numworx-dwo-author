package fi.sliderwidget;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.Hashtable;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import fi.beans.wiskopdrbeans.InteractieEditPanel;

public class SliderWidgetInteractieEditPanel extends JPanel implements InteractieEditPanel, ActionListener, FocusListener {

	private SliderWidgetInteractiePanel interactiePanel;
	
	private JTextField naamTF, onderGrensTF, bovenGrensTF, beginStandTF, stapGrootteTF, lengteTF;
	SchuifParameter param;
	
	private Font theFont = new Font("SansSerif", Font.PLAIN, 12);
	
	private JButton okButton; 
	private JButton cancelButton;
	
	private JPanel optiesPanel;
	
	private int defaultWidth = 800;
	private int defaultHeight = 500;
	private int defaultIpWidth = 500;
	private int defaultOpWidth = 260;
	
	//// Te vervangen voorbeeldcode:
	private JTextField opdrachtTekstTF;
	//// Einde voorbeelcode
	
	public SliderWidgetInteractieEditPanel() {
		setLayout(null);
		
		interactiePanel = new SliderWidgetInteractiePanel();
		interactiePanel.setBounds(10,20,defaultIpWidth,defaultHeight);
		add(interactiePanel);
		
		optiesPanel = new JPanel();
		optiesPanel.setLayout(null);
		optiesPanel.setBounds(defaultIpWidth+30,20,defaultOpWidth,defaultHeight);
		add(optiesPanel);
		
		param = interactiePanel.geefParam();
		
		makeGUI();
		
		//// Te vervangen voorbeeldcode:
		
		//// Einde voorbeelcode
	}
	
	public void makeGUI(){
	 	
        
		optiesPanel.setLayout(null);
		optiesPanel.setPreferredSize(new Dimension(200, 180));
		int currentY = 20;
		int offset = 5;
		int height = 20;
		
		JLabel paramNaamLabel = new JLabel(SliderWidget.rb.getString("param_naam"));
		paramNaamLabel.setBounds(2 * offset, currentY, 80, height);
		paramNaamLabel.setFont(theFont);
		optiesPanel.add(paramNaamLabel);
		
		naamTF = new JTextField(param.geefNaam());
		naamTF.setHorizontalAlignment(JTextField.RIGHT);
		naamTF.setBounds(4 * offset + 80, currentY, 50, height);
		naamTF.addActionListener(this);
		optiesPanel.add(naamTF);
		
		currentY += height + offset;
		
		JLabel paramOndergrensLabel = new JLabel(SliderWidget.rb.getString("param_ondergrens"));
		paramOndergrensLabel.setBounds(2 * offset, currentY, 80, height);
		paramOndergrensLabel.setFont(theFont);
		optiesPanel.add(paramOndergrensLabel);
		
		String onderGrensTekst = Double.toString(param.geefOnderGrens());
		if(onderGrensTekst.endsWith(".0"))
			onderGrensTekst = onderGrensTekst.substring(0, onderGrensTekst.length() - 2);
		onderGrensTF = new JTextField(onderGrensTekst);
		onderGrensTF.setHorizontalAlignment(JTextField.RIGHT);
		onderGrensTF.setBounds(4 * offset + 80, currentY, 50, height);
		onderGrensTF.addActionListener(this);
		optiesPanel.add(onderGrensTF);
		
		currentY += height + offset;
		
		JLabel bovengrensLabel = new JLabel(SliderWidget.rb.getString("param_bovengrens"));
		bovengrensLabel.setBounds(2 * offset, currentY, 80, height);
		bovengrensLabel.setFont(theFont);
		optiesPanel.add(bovengrensLabel);
		
		String bovenGrensTekst = Double.toString(param.geefBovenGrens());
		if(bovenGrensTekst.endsWith(".0"))
			bovenGrensTekst = bovenGrensTekst.substring(0, bovenGrensTekst.length() - 2);
		bovenGrensTF = new JTextField(bovenGrensTekst);
		bovenGrensTF.setHorizontalAlignment(JTextField.RIGHT);
		bovenGrensTF.setBounds(4 * offset + 80, currentY, 50, height);
		bovenGrensTF.addActionListener(this);
		optiesPanel.add(bovenGrensTF);
		
		currentY += height + offset;
		
		JLabel beginstandLabel = new JLabel(SliderWidget.rb.getString("param_beginstand"));
		beginstandLabel.setBounds(2 * offset, currentY, 80, height);
		beginstandLabel.setFont(theFont);
		optiesPanel.add(beginstandLabel);
		
		String beginStandTekst = Double.toString(param.geefDoubleStand());
		if(beginStandTekst.endsWith(".0"))
			beginStandTekst = beginStandTekst.substring(0, beginStandTekst.length() - 2);
		
		beginStandTF = new JTextField(beginStandTekst);
		beginStandTF.setHorizontalAlignment(JTextField.RIGHT);
		beginStandTF.setBounds(4 * offset + 80, currentY, 50, height);
		beginStandTF.addActionListener(this);
		optiesPanel.add(beginStandTF);
		
		currentY += height + offset;
		
		JLabel stapGrootteLabel = new JLabel(SliderWidget.rb.getString("param_stapgrootte"));
		stapGrootteLabel.setBounds(2 * offset, currentY, 80, height);
		stapGrootteLabel.setFont(theFont);
		optiesPanel.add(stapGrootteLabel);
		
		String stapGrootteTekst = Double.toString(param.geefStapGrootte());
		if(stapGrootteTekst.endsWith(".0"))
			stapGrootteTekst = stapGrootteTekst.substring(0, stapGrootteTekst.length() - 2);
		
		stapGrootteTF = new JTextField(stapGrootteTekst);
		stapGrootteTF.setHorizontalAlignment(JTextField.RIGHT);
		stapGrootteTF.setBounds(4 * offset + 80, currentY, 50, height);
		stapGrootteTF.addActionListener(this);
		optiesPanel.add(stapGrootteTF);
		
		currentY += height + offset;
		
		JLabel sliderLengteLabel = new JLabel(SliderWidget.rb.getString("param_sliderlengte"));
		sliderLengteLabel.setBounds(2 * offset, currentY, 80, height);
		sliderLengteLabel.setFont(theFont);
		optiesPanel.add(sliderLengteLabel);
		
		lengteTF = new JTextField("" + param.geefLengte());
		lengteTF.setHorizontalAlignment(JTextField.RIGHT);
		lengteTF.setBounds(4 * offset + 80, currentY, 50, height);
		lengteTF.addActionListener(this);
		optiesPanel.add(lengteTF);
		
		
    }
	
	public void maakSchuifParameter()
	{
		int lengte = 100;
		try {
			lengte = Integer.parseInt(lengteTF.getText());
			if(lengte <= 0)
				lengte = 100;
			}
		catch(Exception e){}
		String naam = naamTF.getText();
		if(naam.length() == 0)
			naam = "a";
		param.zetNaam(naam);
		param.zetLengte(lengte);
		
		double onderGrens = param.geefOnderGrens();
		double bovenGrens = param.geefBovenGrens();
		try {onderGrens = Double.parseDouble(onderGrensTF.getText());}
		catch(Exception e){}
		try {bovenGrens = Double.parseDouble(bovenGrensTF.getText());}
		catch(Exception e){}
		param.zetGrensWaarden(onderGrens, bovenGrens);
		
		double waarde = param.geefWaarde();
		try{waarde = Double.parseDouble(beginStandTF.getText());}
		catch(Exception e){}
		param.zetWaarde(waarde, false);
		
		double stapGrootte = param.geefStapGrootte();
		try{stapGrootte = Double.parseDouble(stapGrootteTF.getText());}
		catch(Exception e){}
		param.zetStapGrootte(stapGrootte);
	}
	
		
	public Hashtable getEditState() {
		Hashtable h = interactiePanel.getEditState();
		return h;
	}
	
	
	public void setEditState(Hashtable h) {
		interactiePanel.setEditState(h);
		
		SchuifParameter param = interactiePanel.geefParam();
		naamTF.setText(param.geefNaam());
		onderGrensTF.setText(""+param.geefOnderGrens());
		bovenGrensTF.setText(""+param.geefBovenGrens());
		beginStandTF.setText(""+param.geefWaarde());
		stapGrootteTF.setText(""+param.geefStapGrootte());
		lengteTF.setText(""+param.geefLengte());		
	}
	
	public void setBounds(int x, int y, int b, int h) {
		super.setBounds(x,y,b,h);
		optiesPanel.setBounds(b-defaultOpWidth-10, 20, defaultOpWidth, defaultHeight);
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
		//if(e.getSource()==opdrachtTekstTF) {
		//	interactiePanel.zetOpdrachtTekst(opdrachtTekstTF.getText());
		//}
		//// Einde voorbeelcode
		maakSchuifParameter();
	}

	@Override
	public void focusGained(FocusEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void focusLost(FocusEvent e) {
		maakSchuifParameter();
		
	}

}
