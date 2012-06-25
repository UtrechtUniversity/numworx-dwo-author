package fi.verknippen;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class FiguurInputPanel extends JPanel  
{
	
	CheckComponent check;
	JTextField input;
	//int numInputs = 6;
	boolean rodeFiguur;
	
	String figuurString;
	
	int hOffset = 8;
	int vOffset = 8;
	
	int topOffset = 18;
	
	
	
	Font theFont = new Font("TimesRoman", Font.BOLD, 14);
	FontMetrics theFM;
	
	Font formuleFont = new Font("TimesRoman", Font.BOLD, 16);
	FontMetrics formuleFM;
	
    Color brownRed = new Color(214, 0, 0);	
    Font closeFont = new Font("Sanserif", Font.BOLD, 14);
    
    JButton closeButton;
    
    VerknippenInteractieEditPanel viep = null;
	
	public FiguurInputPanel(int x, int y, int w, int h, boolean rood)
	{
		setBounds(x, y, w, h);
		setLayout(null);

		rodeFiguur = rood;
		//numInputs = nInputs;
		
		theFM = getFontMetrics(theFont);
    	formuleFM = getFontMetrics(formuleFont);
    	int height1 = 3 * formuleFM.getHeight() / 2;
    	int height2 = height1 + vOffset;
    	
    	check = new CheckComponent(hOffset + 4, topOffset + vOffset + 4, height1 - 8, height1 - 8);
    	add(check);
    	input = new JTextField();
    	input.setFont(formuleFont);
    	input.setBounds(2 * hOffset + height1, topOffset + vOffset, w - height1 - 3 * hOffset, height1);
    	add(input);
		input.addActionListener(new InputAL());
		input.addFocusListener(new InputFL());
    	
    	figuurString = "";
    	
    	
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
    	
    	g.setFont(theFont);
    	
    	String title = Verknippen.rb.getString("maakRodeFiguurTekst");
    	if (!rodeFiguur)
    		title = Verknippen.rb.getString("maakGrijzeFiguurTekst");
    	
    	
    	g.drawString(title, input.getLocation().x, 18);
    		
    }
  
    public String getFiguurString()
    {
    	return figuurString;
    }
    
    public void zetFiguurString(String fString)
    {
    	figuurString = fString;
   		input.setText(figuurString);
   		//procesInput();
    }
    
    public String getCorrectFiguurString()
    {	String result = "";
   		if (check.status == CheckComponent.CORRECT)
   		{  	result = figuurString;
   		}
    	return result;
    }
    
    public void procesInput()
    {
    	String text = input.getText();
    	figuurString = text;
    	if (text.equals(""))
    	{	check.setNeutral();
    		return;
    	}
    	
    	Vector figuurCoordinaten = viep.vip.processFiguurString(text);
    	if (figuurCoordinaten == null)
    	{	check.setWrong();
			return;
    	}
    	

    	check.setCorrect();
    	
//System.out.println(exp.toString());    	
    	if (viep != null)
    	{	
    		if (rodeFiguur)
    		{	
    			viep.rodeFiguurPanel.setVisible(false);
    			viep.maakRodeFiguur();
    			viep.rodeFiguurPanel.setVisible(true);
    		}
    		else
    		{	
    			viep.grijzeFiguurPanel.setVisible(false);
    			viep.maakGrijzeFiguur();
    			viep.grijzeFiguurPanel.setVisible(true);
    		}
    			
    	}
    	
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
