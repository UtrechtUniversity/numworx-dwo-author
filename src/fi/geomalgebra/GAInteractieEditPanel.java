package fi.geomalgebra;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;

import javax.swing.*;

import fi.beans.wiskopdrbeans.*;

public class GAInteractieEditPanel extends JPanel implements InteractieEditPanel,
																	ActionListener	
{	
	int editWidth = 190;
	int editHeight = 500; 
	int gaipBreedte = 500; // startbreedte gaip
	int gaipHoogte = 450; // starthoogte gaip
	
	Font theFont;
	FontMetrics theFM;
	Font theBoldFont;
	FontMetrics theBoldFM;
	
	int offset = 10;
	boolean componentsCreated = false;
	
	boolean noSetBounds = false;	

	protected GAInteractiePanel gaip;

	int scoreMax = 10;
	
	JCheckBox varWaardeBox, oppWaardeBox, formuleBox, constructieToolsBox, alleenOppervlaktesBox;
	
	
	
	
	public GAInteractieEditPanel()
	{
		setLayout(null);
		gaip = new GAInteractiePanel();
		add(gaip);
		
		theFont = new Font("Dialog", Font.PLAIN, 12);
		theFM = getFontMetrics(theFont);
		theBoldFont = new Font("Dialog", Font.BOLD, 12);
		theBoldFM = getFontMetrics(theBoldFont);
		
		
		int width = editWidth - 2 * offset;
		int height = 3 * theFM.getHeight() / 2;
		int currentX = gaip.getSize().width + offset;
		int currentY = offset;
		
		varWaardeBox = new JCheckBox(GeomAlgebra.rb.getString("varWaardeTekst"), false);
		varWaardeBox.setFont(theFont);
		varWaardeBox.setBackground(Color.white);
		varWaardeBox.setBounds(currentX, currentY, width, height);
		add(varWaardeBox);
		varWaardeBox.addActionListener(this);
		
		currentY += height + offset;
		
		oppWaardeBox = new JCheckBox(GeomAlgebra.rb.getString("oppWaardeTekst"), false);
		oppWaardeBox.setFont(theFont);
		oppWaardeBox.setBackground(Color.white);
		oppWaardeBox.setBounds(currentX, currentY, width, height);
		add(oppWaardeBox);
		oppWaardeBox.addActionListener(this);
		
		currentY += height + offset;

		formuleBox = new JCheckBox(GeomAlgebra.rb.getString("formuleTekst"), true);
		formuleBox.setFont(theFont);
		formuleBox.setBackground(Color.white);
		formuleBox.setBounds(currentX, currentY, width, height);
		add(formuleBox);
		formuleBox.addActionListener(this);
		
		currentY += height + offset;

		constructieToolsBox = new JCheckBox(GeomAlgebra.rb.getString("constructieToolsTekst"), true);
		constructieToolsBox.setFont(theFont);
		constructieToolsBox.setBackground(Color.white);
		constructieToolsBox.setBounds(currentX, currentY, width, height);
		add(constructieToolsBox);
		constructieToolsBox.addActionListener(this);
		
		currentY += height + offset;

		alleenOppervlaktesBox = new JCheckBox(GeomAlgebra.rb.getString("alleenOppervlaktesTekst"), false);
		alleenOppervlaktesBox.setFont(theFont);
		alleenOppervlaktesBox.setBackground(Color.white);
		alleenOppervlaktesBox.setBounds(currentX, currentY, width, height);
		add(alleenOppervlaktesBox);
		alleenOppervlaktesBox.addActionListener(this);
		
		currentY += height + offset;
		
		componentsCreated = true;
	}	
	
	public void plaatsComponenten()
	{
		if (componentsCreated)
		{	
			varWaardeBox.setLocation(gaip.getSize().width + offset, varWaardeBox.getLocation().y);
			oppWaardeBox.setLocation(gaip.getSize().width + offset, oppWaardeBox.getLocation().y);
			formuleBox.setLocation(gaip.getSize().width + offset, formuleBox.getLocation().y);
			constructieToolsBox.setLocation(gaip.getSize().width + offset, constructieToolsBox.getLocation().y);
			alleenOppervlaktesBox.setLocation(gaip.getSize().width + offset, alleenOppervlaktesBox.getLocation().y);
		}
	}
	
	public void setEditState(Hashtable b)
	{
		
System.out.println("gaiep setEditState");

		boolean varWaardeZichtbaar = false;
		boolean oppWaardeZichtbaar = false;
		boolean formuleZichtbaar = true;
		boolean constructieTools = true;
		boolean alleenOppervlaktes = false;
	

		if (b.containsKey("appletLaunchData"))
		{
System.out.println("aLD found");

			Hashtable appletLaunchData = (Hashtable) b.get("appletLaunchData");
			
			String varWaardeZichtbaarString = "false";
			String oppWaardeZichtbaarString = "false";
			String formuleZichtbaarString = "true";
			String constructieToolsString = "true";
			String alleenOppervlaktesString = "false";
			
			if (appletLaunchData.containsKey("varWaarde"))
				varWaardeZichtbaarString = (String) appletLaunchData.get("varWaarde");
			if (varWaardeZichtbaarString.equals("true") || varWaardeZichtbaarString.equals("yes"))
				varWaardeZichtbaar = true;
			if (appletLaunchData.containsKey("oppWaarde"))
				oppWaardeZichtbaarString = (String) appletLaunchData.get("oppWaarde");
			if (oppWaardeZichtbaarString.equals("true") || oppWaardeZichtbaarString.equals("yes"))
				oppWaardeZichtbaar = true;
			if (appletLaunchData.containsKey("formule"))
				formuleZichtbaarString = (String) appletLaunchData.get("formule");
			if (formuleZichtbaarString.equals("false") || formuleZichtbaarString.equals("no"))
				formuleZichtbaar = false;
			if (appletLaunchData.containsKey("constructieTools"))
				constructieToolsString = (String) appletLaunchData.get("constructieTools");
			if (constructieToolsString.equals("false") || constructieToolsString.equals("no"))
				constructieTools = false;
			if (appletLaunchData.containsKey("alleenOppervlaktes"))
				alleenOppervlaktesString = (String) appletLaunchData.get("alleenOppervlaktes");
			if (alleenOppervlaktesString.equals("true") || alleenOppervlaktesString.equals("yes"))
				alleenOppervlaktes = true;
						
			

		}
		else
		{
			if (b.containsKey("varWaardeZichtbaar"))
				varWaardeZichtbaar = ((Boolean) b.get("varWaardeZichtbaar")).booleanValue();
			if (b.containsKey("oppWaardeZichtbaar"))
				oppWaardeZichtbaar = ((Boolean) b.get("oppWaardeZichtbaar")).booleanValue();
			if (b.containsKey("formuleZichtbaar"))
				formuleZichtbaar = ((Boolean) b.get("formuleZichtbaar")).booleanValue();
			if (b.containsKey("constructieTools"))
				constructieTools = ((Boolean) b.get("constructieTools")).booleanValue();
			if (b.containsKey("alleenOppervlaktes"))
				alleenOppervlaktes = ((Boolean) b.get("alleenOppervlaktes")).booleanValue();
			
		}
		
		varWaardeBox.setSelected(varWaardeZichtbaar);
		oppWaardeBox.setSelected(oppWaardeZichtbaar);
		formuleBox.setSelected(formuleZichtbaar);
		constructieToolsBox.setSelected(constructieTools);
		alleenOppervlaktesBox.setSelected(alleenOppervlaktes);
		
		
		if (b.containsKey("gaipBreedte"))
			gaipBreedte = ((Integer) b.get("gaipBreedte")).intValue();
		if (b.containsKey("gaipHoogte"))
			gaipHoogte = ((Integer) b.get("gaipHoogte")).intValue();
		
		setBounds(getLocation().x, getLocation().y, gaipBreedte + editWidth, Math.max(gaipHoogte, editHeight));
		
		// HIER !!
		gaip.setEditState(b);		
		
	}
	
	public Hashtable getEditState()
	{
System.out.println("gaiep getEditState");

		Hashtable h = gaip.getEditState(); 
		
		h.put("scoreMax", new Integer(scoreMax));
		
		h.put("gaipBreedte", new Integer(gaipBreedte));
		h.put("gaipHoogte", new Integer(gaipHoogte));
		
		return h;
		
	}
		
	public void setBounds(int x, int y, int b, int h)
	{
		if (noSetBounds)
		{	noSetBounds = false;
			return;
		}
//System.out.println("spiep setBounds raw " + x + " " + y + " " + b + " " + h);

		if ((h <= 1) || (x < 0) || (b <= 1))
			return;
		
		super.setBounds(x, y, gaipBreedte + editWidth, Math.max(gaipHoogte, editHeight));
		
//System.out.println("spiep setBounds " + x + " " + y + " " + (spipBreedte + editWidth) + " " + 
//					Math.max(spipHoogte, editHeight));
	
		if (gaip != null)
			gaip.setBounds(0, 0, gaipBreedte, gaipHoogte);
		
		plaatsComponenten();
		
//System.out.println("setBounds " + x + " " + y + " " + b + " " + h);		
		
	}
	
	public void zetBreedte(int b)
	{	
		gaipBreedte = b;
		
		setBounds(getLocation().x, getLocation().y, gaipBreedte + editWidth, Math.max(gaipHoogte, editHeight));		
		plaatsComponenten();
	}
	
	public void zetHoogte(int h)
	{	
		gaipHoogte = h;
		
		setBounds(getLocation().x, getLocation().y, gaipBreedte + editWidth, Math.max(gaipHoogte, editHeight));		
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
		if (e.getSource() == varWaardeBox)
		{
			gaip.zetVarWaardeZichtbaar(varWaardeBox.isSelected());
		}
		else if (e.getSource() == oppWaardeBox)
		{
			gaip.zetOppWaardeZichtbaar(oppWaardeBox.isSelected());
		}
		else if (e.getSource() == formuleBox)
		{
			gaip.zetFormuleZichtbaar(formuleBox.isSelected());
		}
		else if (e.getSource() == constructieToolsBox)
		{
			gaip.zetConstructieTools(constructieToolsBox.isSelected());
		}
		else if (e.getSource() == alleenOppervlaktesBox)
		{
			gaip.zetAlleenOppervlaktes(alleenOppervlaktesBox.isSelected());
		}
					

			
		
	}

}
