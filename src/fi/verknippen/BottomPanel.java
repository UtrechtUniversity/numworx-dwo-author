package fi.verknippen;

import java.awt.*;


import javax.swing.*;

public class BottomPanel extends JPanel
{	
	Verknippen owner;
	
	boolean showGoed = false;
	boolean showFout = false;
	
	public BottomPanel(Verknippen o)
	{	owner = o;
		setLayout(null);
		setBackground(owner.bgColor);
	}

	public void paintComponent(Graphics g)
	{	g.setColor(owner.bgColor);
		g.fillRect(0, 0, getSize().width, getSize().height);
		
		int vOffSet = 0;
		
		if ((owner.taakNummer == 2) || (owner.taakNummer == 3))
			vOffSet = owner.offSet;
		else if (owner.taakNummer == 4)
			vOffSet = owner.offSet / 2;	
		
		if (showGoed)
		{	g.drawImage(owner.goedVink, 370, vOffSet, null);
		}
		
		if (showFout)
		{	g.drawImage(owner.foutKruis, 370, vOffSet, null);
		}
		
	}
	
}