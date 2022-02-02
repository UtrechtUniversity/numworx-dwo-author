package fi.graphtool;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import fi.beans.numworxlf.JButton;
import fi.beans.numworxlf.JCheckBox;
import fi.beans.numworxlf.JTextField;
import fi.wiskopdr.DialogFacade;

public class SchuifParameterInstellingenButton extends JButton implements ActionListener{
	private DialogFacade frame;
	private GraphToolInteractieEditPanel graphToolInteractieEditPanel;
	private JTextField naamTF, onderGrensTF, bovenGrensTF, beginStandTF, stapGrootteTF, lengteTF;
	private JCheckBox hideSliderCB;
	
	private Font theFont = new Font("SansSerif", Font.PLAIN, 12);
	
	private JButton okButton; 
	private JButton cancelButton;
	
	JPanel optiesPanel = new JPanel();
	JPanel bottomPanel = new JPanel();
	
	int selectedIndex = -1;
	
	SchuifParameter param;
	boolean wijzig = false;
	
	public SchuifParameterInstellingenButton(String tekst, boolean wijzig, GraphToolInteractieEditPanel gtiep)
	{	
		super(tekst);
		graphToolInteractieEditPanel = gtiep;
		this.wijzig = wijzig;
		addActionListener(this);
		//maakDefaultParameter();
		//setOptions(null);
		
	}
	
	public void maakDefaultParameter()
	{
		String naam = eersteVrijeNaam();
		param = new SchuifParameter(100, naam);
	}
	
	public String eersteVrijeNaam()
	{
		String[] namen = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"};
		SchuifParameter[] parameters = graphToolInteractieEditPanel.getInteractiePanel().schuifParameters;
		for(int i = 0; i < parameters.length; i++)
		{
			String naam = parameters[i].geefNaam();
			for(int j = 0; j < namen.length; j++)
			{
				if(namen[j] != null && naam.equals(namen[j]))
				{	namen[j] = null;
					break;
				}
			}
		}
		for(int i = 0; i < namen.length; i++)
		{
			if(namen[i] != null)
				return namen[i];
		}
		return "a";
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
			naam = eersteVrijeNaam();
		param = new SchuifParameter(lengte, naam);
		
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
		
		boolean hideSlider = param.geefHideSlider();
		hideSlider = hideSliderCB.isSelected();
		param.zetHideSlider(hideSlider);
	}
	
	public void makeGUI(){
	 	optiesPanel = new JPanel();
		bottomPanel = new JPanel();
        
		optiesPanel.setLayout(null);
		optiesPanel.setPreferredSize(new Dimension(200, 210));
		int currentY = 20;
		int offset = 5;
		int height = 20;
		
		JLabel paramNaamLabel = new JLabel(GraphTool.rb.getString("GTIEP_naam"));
		paramNaamLabel.setBounds(2 * offset, currentY, 80, height);
		paramNaamLabel.setFont(theFont);
		optiesPanel.add(paramNaamLabel);
		
		naamTF = new JTextField(param.geefNaam());
		naamTF.setHorizontalAlignment(JTextField.RIGHT);
		naamTF.setBounds(4 * offset + 80, currentY, 50, height);
		optiesPanel.add(naamTF);
		
		currentY += height + offset;
		
		JLabel paramOndergrensLabel = new JLabel(GraphTool.rb.getString("GTIEP_ondergrens"));
		paramOndergrensLabel.setBounds(2 * offset, currentY, 80, height);
		paramOndergrensLabel.setFont(theFont);
		optiesPanel.add(paramOndergrensLabel);
		
		String onderGrensTekst = Double.toString(param.geefOnderGrens());
		if(onderGrensTekst.endsWith(".0"))
			onderGrensTekst = onderGrensTekst.substring(0, onderGrensTekst.length() - 2);
		onderGrensTF = new JTextField(onderGrensTekst);
		onderGrensTF.setHorizontalAlignment(JTextField.RIGHT);
		onderGrensTF.setBounds(4 * offset + 80, currentY, 50, height);
		optiesPanel.add(onderGrensTF);
		
		currentY += height + offset;
		
		JLabel bovengrensLabel = new JLabel(GraphTool.rb.getString("GTIEP_bovengrens"));
		bovengrensLabel.setBounds(2 * offset, currentY, 80, height);
		bovengrensLabel.setFont(theFont);
		optiesPanel.add(bovengrensLabel);
		
		String bovenGrensTekst = Double.toString(param.geefBovenGrens());
		if(bovenGrensTekst.endsWith(".0"))
			bovenGrensTekst = bovenGrensTekst.substring(0, bovenGrensTekst.length() - 2);
		bovenGrensTF = new JTextField(bovenGrensTekst);
		bovenGrensTF.setHorizontalAlignment(JTextField.RIGHT);
		bovenGrensTF.setBounds(4 * offset + 80, currentY, 50, height);
		optiesPanel.add(bovenGrensTF);
		
		currentY += height + offset;
		
		JLabel beginstandLabel = new JLabel(GraphTool.rb.getString("GTIEP_beginstand"));
		beginstandLabel.setBounds(2 * offset, currentY, 80, height);
		beginstandLabel.setFont(theFont);
		optiesPanel.add(beginstandLabel);
		
		String beginStandTekst = Double.toString(param.geefDoubleStand());
		if(beginStandTekst.endsWith(".0"))
			beginStandTekst = beginStandTekst.substring(0, beginStandTekst.length() - 2);
		
		beginStandTF = new JTextField(beginStandTekst);
		beginStandTF.setHorizontalAlignment(JTextField.RIGHT);
		beginStandTF.setBounds(4 * offset + 80, currentY, 50, height);
		optiesPanel.add(beginStandTF);
		
		currentY += height + offset;
		
		JLabel stapGrootteLabel = new JLabel(GraphTool.rb.getString("GTIEP_stapgrootte"));
		stapGrootteLabel.setBounds(2 * offset, currentY, 80, height);
		stapGrootteLabel.setFont(theFont);
		optiesPanel.add(stapGrootteLabel);
		
		String stapGrootteTekst = Double.toString(param.geefStapGrootte());
		if(stapGrootteTekst.endsWith(".0"))
			stapGrootteTekst = stapGrootteTekst.substring(0, stapGrootteTekst.length() - 2);
		
		stapGrootteTF = new JTextField(stapGrootteTekst);
		stapGrootteTF.setHorizontalAlignment(JTextField.RIGHT);
		stapGrootteTF.setBounds(4 * offset + 80, currentY, 50, height);
		optiesPanel.add(stapGrootteTF);
		
		currentY += height + offset;
		
		JLabel sliderLengteLabel = new JLabel(GraphTool.rb.getString("GTIEP_sliderlengte"));
		sliderLengteLabel.setBounds(2 * offset, currentY, 80, height);
		sliderLengteLabel.setFont(theFont);
		optiesPanel.add(sliderLengteLabel);
		
		lengteTF = new JTextField("" + param.geefLengte());
		lengteTF.setHorizontalAlignment(JTextField.RIGHT);
		lengteTF.setBounds(4 * offset + 80, currentY, 50, height);
		optiesPanel.add(lengteTF);
		
		currentY += height + offset;
		hideSliderCB = new JCheckBox("Hide Slider");
		hideSliderCB.setBounds(2 * offset, currentY, 180, height);
		hideSliderCB.setSelected(param.geefHideSlider());
		hideSliderCB.setFont(theFont);
		optiesPanel.add(hideSliderCB);
		
		okButton = new JButton("Ok");
        okButton.addActionListener(this);
        bottomPanel.add(okButton);
        
        cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(this);
        bottomPanel.add(cancelButton);
    }
    
    public void makeFrame(){
    	frame = DialogFacade.newInstance(this, "", true);
    	frame.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        frame.getContentPane().setLayout(new BorderLayout());
        frame.getContentPane().add(optiesPanel);
        frame.getContentPane().add(bottomPanel,BorderLayout.SOUTH);
		frame.pack();
	    frame.setVisible(true);
	    
    }
	
	public void actionPerformed(ActionEvent e){
		if(e.getSource().equals(this) && frame==null){	
			if(wijzig)
			{	selectedIndex = graphToolInteractieEditPanel.getSelectedSchuifParamIndex();
				if(selectedIndex < 0)
					return;
				param = graphToolInteractieEditPanel.getInteractiePanel().schuifParameters[selectedIndex];
			}
			else
			{	selectedIndex = -1;
				maakDefaultParameter();
			}
			makeGUI();
			makeFrame();
		}
		else if(e.getSource().equals(okButton)) {   
			maakSchuifParameter();
			boolean gelijkeNaam = false;
			String[] namen = new String[graphToolInteractieEditPanel.getInteractiePanel().schuifParameters.length];
			for(int i = 0; i < namen.length; i++)		
			{	if((!wijzig || i != selectedIndex) && param.geefNaam().equals(graphToolInteractieEditPanel.getInteractiePanel().schuifParameters[i].geefNaam()))
				{	gelijkeNaam = true;
					break;
				}
			}
			if(gelijkeNaam)
			{
				//show dialog met foutmelding
				JOptionPane.showMessageDialog(new JFrame(), GraphTool.rb.getString("GTIEP_dubbeleNaam"), GraphTool.rb.getString("GTIEP_waarschuwing"), JOptionPane.WARNING_MESSAGE);
				return;
			}
			if(wijzig)
				graphToolInteractieEditPanel.wijzigSchuifParameter(selectedIndex, param);
			else
			{	graphToolInteractieEditPanel.voegSchuifParameterToe(param);
			}
			//graphToolInteractieEditPanel.zetFormuleEditorOpties(false);
        	frame.setVisible(false);
            frame.dispose();
            frame=null;
        }
		else if(e.getSource().equals(cancelButton)) {   
			frame.getContentPane().removeAll();
			frame.setVisible(false);
            frame.dispose();
            frame=null;
        }
	}
}
