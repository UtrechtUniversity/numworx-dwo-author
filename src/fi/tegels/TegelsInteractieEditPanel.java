package fi.tegels;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;

import javax.swing.*;

import fi.beans.wiskopdrbeans.*;

public class TegelsInteractieEditPanel extends JPanel implements InteractieEditPanel,
																	ActionListener	
{	
	int editWidth = 190;
	int editHeight = 500; 
	int tipBreedte = 500; // startbreedte tip
	int tipHoogte = 450; // starthoogte tip
	
	Font theFont;
	FontMetrics theFM;
	Font theBoldFont;
	FontMetrics theBoldFM;
	
	int offset = 10;
	boolean componentsCreated = false;
	
	boolean noSetBounds = false;	

	protected TegelsInteractiePanel tip;

	int scoreMax = 0;
	
	JCheckBox transVersionBox, demoVersionBox;
	
	
	public TegelsInteractieEditPanel()
	{
		setLayout(null);
		tip = new TegelsInteractiePanel();
		add(tip);
		
		theFont = new Font("Dialog", Font.PLAIN, 12);
		theFM = getFontMetrics(theFont);
		theBoldFont = new Font("Dialog", Font.BOLD, 12);
		theBoldFM = getFontMetrics(theBoldFont);
		
		
		int width = editWidth - 3 * offset;
		int height = 3 * theFM.getHeight() / 2;
		int currentX = tip.getSize().width + offset;
		int currentX2 = offset;
		int currentY = offset;
		
		transVersionBox = new JCheckBox(Tegels.rb.getString("transVersionTekst"), false);
		transVersionBox.setFont(theFont);
		transVersionBox.setBackground(Color.white);
		transVersionBox.setBounds(currentX, currentY, width, height);
		add(transVersionBox);
		transVersionBox.addActionListener(this);
		
		currentY += height + offset;

		demoVersionBox = new JCheckBox(Tegels.rb.getString("demoVersionTekst"), false);
		demoVersionBox.setFont(theFont);
		demoVersionBox.setBackground(Color.white);
		demoVersionBox.setBounds(currentX, currentY, width, height);
		add(demoVersionBox);
		demoVersionBox.addActionListener(this);
		
		currentY += height + offset;
		
		componentsCreated = true;
	}	
	
	public void plaatsComponenten()
	{
		if (componentsCreated)
		{	
			
			transVersionBox.setLocation(tip.getSize().width + 2 * offset, transVersionBox.getLocation().y);
			demoVersionBox.setLocation(tip.getSize().width + 2 * offset, demoVersionBox.getLocation().y);
		}	
		
	}
	
	public void setEditState(Hashtable b)
	{
		
System.out.println("tiep setEditState");

		boolean transVersion = false;
		boolean demoVersion = false;

		if (b.containsKey("appletLaunchData"))
		{
System.out.println("aLD found");	

			Hashtable appletLaunchData = (Hashtable) b.get("appletLaunchData");

			String transVersionString = "false";
			String demoVersionString = "false";

			if (appletLaunchData.containsKey("transversion"))
				transVersionString = (String) appletLaunchData.get("transversion");
			if (transVersionString.equals("true") || transVersionString.equals("yes"))
				transVersion = true;
			if (appletLaunchData.containsKey("demoversion"))
				demoVersionString = (String) appletLaunchData.get("demoversion");
			if (demoVersionString.equals("true") || demoVersionString.equals("yes"))
				demoVersion = true;

		}
		else
		{
			if (b.containsKey("transVersion"))
				transVersion = ((Boolean) b.get("transVersion")).booleanValue();
			if (b.containsKey("demoVersion"))
				demoVersion = ((Boolean) b.get("demoVersion")).booleanValue();
			
		}
			
		transVersionBox.setSelected(transVersion);
		demoVersionBox.setSelected(demoVersion);
		
		
		if (b.containsKey("tipBreedte"))
			tipBreedte = ((Integer) b.get("tipBreedte")).intValue();
		if (b.containsKey("tipHoogte"))
			tipHoogte = ((Integer) b.get("tipHoogte")).intValue();
		
		setBounds(getLocation().x, getLocation().y, tipBreedte + editWidth, Math.max(tipHoogte, editHeight));
		
		// HIER !!
		tip.setEditState(b);		
		
	}
	
	public Hashtable getEditState()
	{
System.out.println("tiep getEditState");

		Hashtable h = tip.getEditState(); 
		
		h.put("scoreMax", new Integer(scoreMax));
		
		h.put("tipBreedte", new Integer(tipBreedte));
		h.put("tipHoogte", new Integer(tipHoogte));
		
		return h;
		
	}
		
	public void setBounds(int x, int y, int b, int h)
	{
		if (noSetBounds)
		{	noSetBounds = false;
			return;
		}
//System.out.println("tiep setBounds raw " + x + " " + y + " " + b + " " + h);

		if ((h <= 1) || (x < 0) || (b <= 1))
			return;
		
		super.setBounds(x, y, tipBreedte + editWidth, Math.max(tipHoogte, editHeight));
		
//System.out.println("tiep setBounds " + x + " " + y + " " + (tipBreedte + editWidth) + " " + 
//					Math.max(tipHoogte, editHeight));
	
		if (tip != null)
			tip.setBounds(0, 0, tipBreedte, tipHoogte);
		
		plaatsComponenten();
		
//System.out.println("setBounds " + x + " " + y + " " + b + " " + h);		
		
	}
	
	public void zetBreedte(int b)
	{	
		tipBreedte = b;
		
		setBounds(getLocation().x, getLocation().y, tipBreedte + editWidth, Math.max(tipHoogte, editHeight));		
		plaatsComponenten();
	}
	
	public void zetHoogte(int h)
	{	
		tipHoogte = h;
		
		setBounds(getLocation().x, getLocation().y, tipBreedte + editWidth, Math.max(tipHoogte, editHeight));		
	}
	
	public void wis()
	{}
    
	public void zetMode(int mode)
	{}
	
    public void stop()
    {}
    
    public void start()
    {}
    
    public void addActionListener(ActionListener al)
    {}
    
	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == transVersionBox)
		{
			tip.zetTransVersion(transVersionBox.isSelected());
		}
		else if (e.getSource() == demoVersionBox)
		{
			tip.zetDemoVersion(demoVersionBox.isSelected());
		}

	}

}
