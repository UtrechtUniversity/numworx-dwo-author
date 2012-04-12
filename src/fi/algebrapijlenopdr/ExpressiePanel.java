package fi.algebrapijlenopdr;

import javax.swing.*;

import fi.algebrapijlenopdr.expressies_ap.*;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class ExpressiePanel extends JPanel  
{
	
	CheckComponent[] checks;
	JTextField[] inputs;
	int numInputs = 6;
	
	String[] expressieStrings;
	
	int hOffset = 8;
	int vOffset = 8;
	
	int topOffset = 18;
	
	Font formuleFont = new Font("TimesRoman", Font.BOLD, 16);
	FontMetrics formuleFM;
	
    Color brownRed = new Color(214, 0, 0);	
    Font closeFont = new Font("Sanserif", Font.BOLD, 14);
    
    JButton closeButton;
    
    AlgebraPijlenOpdrInteractieEditPanel apoiep = null;
	
	public ExpressiePanel(int x, int y, int w, int h, int nInputs)
	{
		setBounds(x, y, w, h);
		setLayout(null);

		numInputs = nInputs;
		
    	formuleFM = getFontMetrics(formuleFont);
    	int height1 = 3 * formuleFM.getHeight() / 2;
    	int height2 = height1 + vOffset;
    	
    	checks = new CheckComponent[numInputs];
    	inputs = new JTextField[numInputs];
    	
    	expressieStrings = new String[numInputs];
    	
    	for (int i = 0; i < numInputs; i++)
    	{	checks[i] = new CheckComponent(hOffset + 4, topOffset + vOffset + 4 + i * height2, height1 - 8, height1 - 8);
    		add(checks[i]);
    		
    		inputs[i] = new JTextField();
    		inputs[i].setFont(formuleFont);
    		inputs[i].setBounds(2 * hOffset + height1, topOffset + vOffset + i * height2, w - height1 - 3 * hOffset, height1);
    		add(inputs[i]);
    		inputs[i].addActionListener(new InputAL(i));
    		inputs[i].addFocusListener(new InputFL(i));
    		
    		expressieStrings[i] = "";
    	}
    	
    	
    	closeButton = new JButton("X");
    	closeButton.setBounds(getSize().width - 55, 0, 45, 20);
    	closeButton.setBackground(brownRed);
    	closeButton.setForeground(Color.white);
    	closeButton.setFont(closeFont);
    	add(closeButton);
	}
	
    public void paintComponent(Graphics g)
    {
    	g.setColor(Color.lightGray);
    	g.fillRect(0, 0, getSize().width, getSize().height);
    	
    	g.setColor(Color.black);
    	g.draw3DRect(0, 0, getSize().width - 1 , getSize().height - 1, true);
    		
    }
  
    public String[] getExpressieStrings()
    {
    	return expressieStrings;
    }
    
    public void zetExpressieStrings(String[] eStrings)
    {
    	expressieStrings = eStrings;
    	for (int i = 0; i < expressieStrings.length; i++)
    	{
    		inputs[i].setText(expressieStrings[i]);
    		procesInput(i);
    	}
    }
    
    public Vector getCorrectExpressieStrings()
    {	Vector result = new Vector();
    	for (int i = 0; i < expressieStrings.length; i++)
    	{	if (checks[i].status == CheckComponent.CORRECT)
    		{  	//String formuleText = "$f" + expressieStrings[i] + "@";
        		//Expressie exp = FormuleParser_ap.geefExpressie(formuleText);
        		result.addElement(expressieStrings[i]);
    		}
    	}
    	
    	return result;
    }
    
    public void procesInput(int index)
    {
    	String text = inputs[index].getText();
    	expressieStrings[index] = text;
    	if (text.equals(""))
    	{	checks[index].setNeutral();
    		return;
    	}
    	
    	String formuleText = "$f" + text + "@";
    	Expressie exp = FormuleParser_ap.geefExpressie(formuleText); 
    	if (exp == null)
    	{	checks[index].setWrong();
			return;
    	}
    	
    	String[] varNamen = Algebra.geefVarNamen(exp);
    	
//System.out.println("vn = " + varNamen.length);
//System.out.println(exp.toString());

    	if ((varNamen.length == 0) || (varNamen.length > 1))
    	{	checks[index].setWrong();
			return;
    	}

    	checks[index].setCorrect();
    	
//System.out.println(exp.toString());    	
    	if (apoiep != null)
    	{	
    		apoiep.beginExpressiePanel.setVisible(false);
    		apoiep.maakBeginExpressie();
    		apoiep.beginExpressiePanel.setVisible(true);
    		
    	}
    	
    }
    
    class InputAL implements ActionListener
    {
    	int index = 0;
    	public InputAL(int ind)
    	{	index = ind;
    	}
    	public void actionPerformed(ActionEvent e)
    	{
    		procesInput(index);
    	}
    }

    class InputFL implements FocusListener
    {
    	int index = 0;
    	public InputFL(int ind)
    	{	index = ind;
    	}
    	
    	public void focusGained(FocusEvent e)
    	{}
    	
    	public void focusLost(FocusEvent e)
    	{
    		procesInput(index);
    	}
    	
    }
}
