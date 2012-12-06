package fi.kansbomen;

import java.awt.*;

import javax.swing.*;

public class LijntjeLabel extends JLabel
{
	Color lijnKleur = Color.black;
	
	public LijntjeLabel(Color c)
	{
		lijnKleur = c;
	}
	
	public void paintComponent(Graphics g)
	{
		g.setColor(lijnKleur);
		g.drawLine(2, getSize().height / 2, 
				   getSize().width - 2, getSize().height / 2);
		
	}
	
	public void setColor(Color c)
	{
		lijnKleur = c;
		repaint();
	}
	
}
