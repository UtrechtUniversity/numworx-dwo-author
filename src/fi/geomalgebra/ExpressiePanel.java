package fi.geomalgebra;

import javax.swing.*;

import fi.geomalgebra.expressies.*;
import fi.geomalgebra.formuleobjects.*;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class ExpressiePanel extends JPanel  
{
	
	CheckComponent check;
	JTextField input;
	
	String expressieString = "", correctExpressieString = "";
	
	
	int hOffset = 2;
	int vOffset = 2;
	
	Font formuleFont = new Font("TimesRoman", Font.BOLD, 12);
	FontMetrics formuleFM;
	int height1;
    //GAInteractieEditPanel gaiep = null;
	
	public ExpressiePanel(int w, int h)
	{
		//setSize(w, h);
		setLayout(null);

    	formuleFM = getFontMetrics(formuleFont);
    	height1 = 3 * formuleFM.getHeight() / 2;
    	
    	check = new CheckComponent(hOffset, -1, height1, height1);
		add(check);
    	input = new JTextField();
		input.setFont(formuleFont);
		input.setBounds(2 * hOffset + height1, 0, w - height1 - 2 * hOffset, height1);
		add(input);
		input.addActionListener(new InputAL());
		input.addFocusListener(new InputFL());
    	
		setSize(w, h);
	}
	
	public void setBounds(int x, int y, int w, int h)
	{
		super.setBounds(x, y, w, h);
		input.setBounds(2 * hOffset + height1, 0, w - height1 - 2 * hOffset, height1);
	}
	
    public void paintComponent(Graphics g)
    {
    	g.setColor(Color.lightGray);    	
    	//g.setColor(Color.orange);
    	g.fillRect(0, 0, getSize().width, getSize().height);
    	
    	g.setColor(Color.black);
    	g.draw3DRect(0, 0, getSize().width - 1 , getSize().height - 1, true);
    		
    }
  
    public String getExpressieString()
    {
    	return expressieString;
    }
    
    public void zetExpressieString(String eString)
    {
    	expressieString = eString;
   		input.setText(expressieString);
   		procesInput();
    }
    
    public String getCorrectExpressieString()
    {	String result = "";
   		if (check.status == CheckComponent.CORRECT)
       		result = expressieString;
    	return result;
    }
    
    public void procesInput()
    {
    	String text = input.getText();
    	expressieString = text;
    	if (text.equals(""))
    	{	check.setNeutral();
    		return;
    	}
    	
    	String formuleText = "$f" + text + "@";
    	Expressie exp = FormuleParser.geefExpressie(formuleText); 
    	if (exp == null)
    	{	check.setWrong();
			return;
    	}
    	
    	String[] varNamen = Algebra.geefVarNamen(exp);
    	
//System.out.println("vn = " + varNamen.length);
//System.out.println(exp.toString());

    	if (varNamen.length > 1)
    	{	check.setWrong();
			return;
    	}

    	check.setCorrect();
    	
//System.out.println(exp.toString());
/*    	
    	if (apoiep != null)
    	{	
    		apoiep.beginExpressiePanel.setVisible(false);
    		apoiep.maakBeginExpressie();
    		apoiep.beginExpressiePanel.setVisible(true);
    		
    	}
*/    	
    	
    }
    
    class InputAL implements ActionListener
    {
    	public void actionPerformed(ActionEvent e)
    	{
    		procesInput();
    	}
    }

    class InputFL implements FocusListener
    {
    	
    	public void focusGained(FocusEvent e)
    	{}
    	
    	public void focusLost(FocusEvent e)
    	{
    		procesInput();
    	}
    	
    }
}
