package fi.mozarch;

import java.awt.*;
import java.awt.event.*;

public class InfoButton extends LWButton implements ActionListener
{	
	private String[] tekst;
	private String titel;
	
	public InfoButton (String titel,String[] tekst) 
	{	super("Info");
		this.titel = titel;
		this.tekst = tekst;
		addActionListener(this);
	}
	
	public void actionPerformed(ActionEvent e)
	{	InfoFrame infoFrame = new InfoFrame(titel,tekst);
		infoFrame.setVisible(true);
	} 
	
	class InfoFrame extends Frame
	{	
		private String[] tekst;
		private String titel;
			public InfoFrame(String titel, String[] tekst)
		{	this.titel = titel;
			this.tekst = tekst;
			setTitle(titel);			setSize(250,100);
			setBackground(new Color(230, 230, 230));			setResizable(false);			addWindowListener(new WL());
		}				public void paint(Graphics g)		{	for(int i=0 ; i<tekst.length ; i++)			g.drawString(tekst[i], 20,40+20*i);		}
		
		class WL extends WindowAdapter		{   public void windowClosing(WindowEvent e)		    {   dispose();		    }		    public void windowDeactivated(WindowEvent e)		    {   dispose();		    }		}	}
}
