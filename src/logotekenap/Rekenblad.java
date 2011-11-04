package logotekenap;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Polygon;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import fi.javalogoweb.*;

public class Rekenblad extends JPanel implements ActionListener
{
	private JavaLogoWeb eigenaar;
	private TraceBeheerder trb;
	private JTextArea textArea;
	private JTextField inputField;
	private boolean wachtOpInvoer;
	private double[] inputValues = new double[100];
	private int aantalInputValues;
	private int invoerTeller;
	private String stringTotHier = "";
	  
	public Rekenblad(JavaLogoWeb ap)
	{	
		setLayout(new BorderLayout());
		eigenaar = ap;
		textArea = new JTextArea();
		textArea.setLineWrap(true);
		JScrollPane sbrText = new JScrollPane(textArea);
		sbrText.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

		add(sbrText, BorderLayout.CENTER);
		
		JPanel inputPanel = new JPanel();
		inputPanel.setLayout(new BorderLayout());
		JLabel inputLabel = new JLabel("Input:  ");
		
		inputField = new JTextField();
		inputField.addActionListener(this);
		inputPanel.add(inputLabel,BorderLayout.WEST);
		inputPanel.add(inputField,BorderLayout.CENTER);
		add(inputPanel, BorderLayout.SOUTH);
		
	}
	
	public void actionPerformed(ActionEvent e){
		String s = inputField.getText();
		double waarde;
		try {
			waarde = Double.parseDouble(s);
		}
		catch (Exception ex){
			waarde = Double.NaN;
		}
		if(!Double.isNaN(waarde)){
			inputValues[aantalInputValues] = waarde;
			aantalInputValues++;
		}
		inputField.setText("");
		invoerTeller = 0;
		tekenOpnieuw();
	}
	
	public void init()
	{	textArea.setText("");
		invoerTeller = 0;
		aantalInputValues = 0;
	}
	
	
	public void meldTraceBeheerder(TraceBeheerder trb)
	{	this.trb = trb;
	}
	  
  	public void tekenOpnieuw()
	{	textArea.setText("");
  		if(trb == null || !trb.geefTraceStatus())eigenaar.rekenprogramma();
	}
  	
  	void tekenTraceImage()
	{	textArea.setText(stringTotHier);
	}
	void tekenCursor()
	{	
	}
 	
	public boolean varAanpassing(String varNaam, String varValue)
	{	if(trb!=null && trb.geefTraceStatus())
			return trb.volgendeMethode(varNaam + " = " + varValue);
		else return false;
	}
	
	public boolean invoer(String varNaam)
	{	boolean inputDone = invoerTeller<aantalInputValues;
		boolean traceStop = false;
		if(trb!=null && trb.geefTraceStatus())traceStop = trb.volgendeMethode(varNaam + " = ...");
		return !inputDone || traceStop;
		
	}
	
	public double geefInvoer()
	{	double value = Double.NaN;
		if(invoerTeller<aantalInputValues)value = inputValues[invoerTeller];
		invoerTeller++;
		return value;
	}
	
	public boolean checkKeuze(String voorwaarde)
	{	if(trb!=null && trb.geefTraceStatus())
			return trb.volgendeMethode(voorwaarde);
		else return false;
	}

	public boolean print(String s)
	{	textArea.append(s);
		stringTotHier = textArea.getText();
	
		if(trb!=null && trb.geefTraceStatus())
			return trb.volgendeMethode("print("+s+")");
		else return false;
	}
	
	public boolean printl(String s)
	{	textArea.append(s+"\n");
		stringTotHier = textArea.getText();
		if(trb!=null && trb.geefTraceStatus())
			return trb.volgendeMethode("print("+s+")");
		else return false;
	}
	
}
