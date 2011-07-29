package fi.nabouwenaanzichten;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

public class KnoppenPanel extends JPanel 
{	
	JButton volLeegKnop;
	JPanel kijkNaPanel;
	JLabel aantalKLabel;
	
	public KnoppenPanel(int x, int y, int b, int h)
	{	setLayout(null);
		//setBackground(Color.white);
	
	
		setBounds(x, y, b, h);
	}	

	public void setBounds(int x, int y, int b, int h)
	{
		super.setBounds(x, y, b, h);
		
		if (volLeegKnop != null)
		{
			layoutComponents();
		}
	}
	
	public void layoutComponents()
	{
		int hSpace = (getSize().width - 180) / 4;
		
		if (volLeegKnop.isVisible() && kijkNaPanel.isVisible() && aantalKLabel.isVisible())
		{
			volLeegKnop.setLocation(0, volLeegKnop.getLocation().y);
			kijkNaPanel.setLocation((getSize().width - kijkNaPanel.getSize().width - 20) / 2, kijkNaPanel.getLocation().y);
			aantalKLabel.setLocation(getSize().width - aantalKLabel.getSize().width, aantalKLabel.getLocation().y);
		}
		else if (!volLeegKnop.isVisible() && kijkNaPanel.isVisible() && aantalKLabel.isVisible())
		{
			kijkNaPanel.setLocation(hSpace, kijkNaPanel.getLocation().y);
			aantalKLabel.setLocation(getSize().width - aantalKLabel.getSize().width - hSpace, aantalKLabel.getLocation().y);
		}
		else if (volLeegKnop.isVisible() && !kijkNaPanel.isVisible() && aantalKLabel.isVisible())
		{
			volLeegKnop.setLocation(hSpace, volLeegKnop.getLocation().y);
			aantalKLabel.setLocation(getSize().width - aantalKLabel.getSize().width - hSpace, aantalKLabel.getLocation().y);
		}
		else if (volLeegKnop.isVisible() && kijkNaPanel.isVisible() && !aantalKLabel.isVisible())
		{
			volLeegKnop.setLocation(hSpace, volLeegKnop.getLocation().y);
			kijkNaPanel.setLocation(getSize().width - kijkNaPanel.getSize().width - 20 - hSpace, kijkNaPanel.getLocation().y);
		}
		else if (volLeegKnop.isVisible() && !kijkNaPanel.isVisible() && !aantalKLabel.isVisible())
		{
			volLeegKnop.setLocation((getSize().width - volLeegKnop.getSize().width) / 2, volLeegKnop.getLocation().y);
		}
		else if (!volLeegKnop.isVisible() && kijkNaPanel.isVisible() && !aantalKLabel.isVisible())
		{
			kijkNaPanel.setLocation((getSize().width - kijkNaPanel.getSize().width - 20) / 2, kijkNaPanel.getLocation().y);
		}
		else if (!volLeegKnop.isVisible() && !kijkNaPanel.isVisible() && aantalKLabel.isVisible())
		{
			aantalKLabel.setLocation((getSize().width - aantalKLabel.getSize().width) / 2, aantalKLabel.getLocation().y);
		}
	}
	
	public void setBackground(Color c)
	{
		super.setBackground(c);
		
		if (volLeegKnop != null)
		{
			volLeegKnop.setBackground(c);
			kijkNaPanel.setBackground(c);
		}
			
	}

	public void setOpaque(boolean b)
	{
		super.setOpaque(b);
		
		if (volLeegKnop != null)
		{
			volLeegKnop.setOpaque(b);
			kijkNaPanel.setOpaque(b);
		}	
		
	}
	
}
