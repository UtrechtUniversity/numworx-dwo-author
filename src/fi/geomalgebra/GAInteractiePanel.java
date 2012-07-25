package fi.geomalgebra;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;

import javax.swing.*;

import fi.beans.wiskopdrbeans.WiskOpdrApplet;
// deze moet vanwege interface WiskOpdrApplet
import fi.beans.wiskopdrbeans.InteractiePanel;
// deze moet vanwege interface InteractiePanel
import fi.beans.wiskopdrbeans.InteractieEditPanel;

public class GAInteractiePanel extends JPanel implements InteractiePanel, InteractieEditPanel,
							         ActionListener
									
{
	AlgebraVeld av = null;	
	ControlPanel cp;
	LineaalHor lh;
	LineaalVer lv;
	
	boolean varWaardeZichtbaar;
	boolean oppWaardeZichtbaar;
	boolean formuleZichtbaar = true;
	boolean constructieTools = true;
	boolean alleenOppervlaktes;
	
	int score = 0;
	int scoreMax = 10;
	
	
	boolean noSetBounds = false;	
	
	public GAInteractiePanel()
	{
		setLayout(null);
		// echte initiatie vind pas plaats na setBounds
		
		
	}

	
	public void zetOpdracht(Hashtable b, String[] randomVars, Hashtable randomValues)
	{
System.out.println("gaip zetOpdracht");
		
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
		
		zetVarWaardeZichtbaar(varWaardeZichtbaar);
		zetOppWaardeZichtbaar(oppWaardeZichtbaar);
		zetFormuleZichtbaar(formuleZichtbaar);
		zetConstructieTools(constructieTools);
		zetAlleenOppervlaktes(alleenOppervlaktes);
		
		if (b.containsKey("appletEditState"))
		{	String appletEditState = (String) b.get("appletEditState");
			av.setState(appletEditState);
		}
		else if (b.containsKey("state"))
		{	State state = (State) b.get("state");
			av.setState(state);
		}
		
	}
	
	public void setState(Hashtable b)
	{
		if (b.containsKey("appletState"))
		{	String appletState = (String) b.get("appletState");
			av.setState(appletState);
		}
		else if (b.containsKey("state"))
		{	State state = (State) b.get("state");
			av.setState(state);
		}
			
	}
	
	public void setEditState(Hashtable b)
	{
System.out.println("gaip setEditState");

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

		zetVarWaardeZichtbaar(varWaardeZichtbaar);
		zetOppWaardeZichtbaar(oppWaardeZichtbaar);
		zetFormuleZichtbaar(formuleZichtbaar);
		zetConstructieTools(constructieTools);
		zetAlleenOppervlaktes(alleenOppervlaktes);
		
		if (b.containsKey("appletEditState"))
		{	String appletEditState = (String) b.get("appletEditState");
			av.setState(appletEditState);
		}
		else if (b.containsKey("state"))
		{	State state = (State) b.get("state");
			av.setState(state);
		}
		
		
	}
	
	public Hashtable getState()
	{
		Hashtable h = new Hashtable();
		
		State state = av.getStateState();
		if (state != null)
			h.put("state", state);
		
		return h;
		
	}
	
	public Hashtable getEditState()
	{
		Hashtable h = new Hashtable();
		
		h.put("varWaardeZichtbaar", new Boolean(varWaardeZichtbaar));
		h.put("oppWaardeZichtbaar", new Boolean(oppWaardeZichtbaar));
		h.put("formuleZichtbaar", new Boolean(formuleZichtbaar));
		h.put("constructieTools", new Boolean(constructieTools));
		h.put("alleenOppervlaktes", new Boolean(alleenOppervlaktes));
		
		State state = av.getStateState();
		if (state != null)
			h.put("state", state);
		
		return h;
	}
	
	
	
	public InteractieEditPanel getEditPanel()
	{
		return new GAInteractieEditPanel();
	}
	
	public void zetVarWaardeZichtbaar(boolean b)
	{
		varWaardeZichtbaar = b;
		av.zetVarWaardeZichtbaar(varWaardeZichtbaar);
	}

	public void zetOppWaardeZichtbaar(boolean b)
	{
		oppWaardeZichtbaar = b;
		av.zetOppWaardeZichtbaar(oppWaardeZichtbaar);
	}
	public void zetFormuleZichtbaar(boolean b)
	{
		formuleZichtbaar = b;
		av.zetFormuleZichtbaar(formuleZichtbaar);
	}
	public void zetConstructieTools(boolean b)
	{
		constructieTools = b;
		cp.setVisible(constructieTools);
		lh.setVisible(constructieTools);
		lv.setVisible(constructieTools);
		av.zetConstructieTools(constructieTools);
		Figuur.zetGeslotenVeld(!constructieTools || alleenOppervlaktes);
	}
	public void zetAlleenOppervlaktes(boolean b)
	{	
		alleenOppervlaktes = b;
		av.zetAlleenOppervlaktes(alleenOppervlaktes);
		Figuur.zetGeslotenVeld(!constructieTools || alleenOppervlaktes);
	}
	
	
	
	
	public void setBounds(int x, int y, int b, int h)
	{
		
//		System.out.println("spip set bounds " + b + " " + h);
		
		if (h == 1)
			return;
		
		super.setBounds(x, y, b, h);
		
		int breedte = b;
		int hoogte = h;
		
		if (av == null) 
		{	av = new AlgebraVeld(breedte, hoogte);
			av.setLocation(0,0);
			av.zetVarWaardeZichtbaar(varWaardeZichtbaar);
			av.zetOppWaardeZichtbaar(oppWaardeZichtbaar);
			av.zetFormuleZichtbaar(formuleZichtbaar);
			av.zetConstructieTools(constructieTools);
			av.zetAlleenOppervlaktes(alleenOppervlaktes);
			add(av);
		
			Figuur.zetGeslotenVeld(!constructieTools || alleenOppervlaktes);
			Figuur.zetVeldSizes(breedte, hoogte);
		
			cp = new ControlPanel(av);
			cp.setLayout(null);
			cp.setBounds(1, hoogte - 41, breedte - 2, 40);
			if (constructieTools) 
				av.add(cp, 0);
		
			lh = new LineaalHor(breedte, hoogte);
			lh.addActionListener(this);
			if (constructieTools) 
				av.add(lh, 0);
		
			lv = new LineaalVer(breedte, hoogte);
			lv.addActionListener(this);
			if (constructieTools) 
				av.add(lv, 0);

//System.out.println("av created");			
		}
		else
		{	av.setSize(b, h);

			Figuur.zetGeslotenVeld(!constructieTools || alleenOppervlaktes);
			Figuur.zetVeldSizes(breedte, hoogte);
		
			cp.setBounds(1, hoogte - 41, breedte - 2, 40);
			
			av.remove(lh);
			lh = new LineaalHor(breedte, hoogte);
			lh.addActionListener(this);
			if (constructieTools) 
				av.add(lh, 0);
			
			av.remove(lv);
			lv = new LineaalVer(breedte, hoogte);
			lv.addActionListener(this);
			if (constructieTools) 
				av.add(lv, 0);
			

//System.out.println("g3dc sized");		
		}
		
	}
	
	
	public void wis()
	{}
	
	public void zetMaat()
	{}
	
	public int geefAsHoogte()
	{	return 0;
	}
	
	public int getIpId()
	{	return 0;
	}
	
	public String getIpExpString()
	{	return null;
	}
	
	public int getScore()
	{	return score;
	}
	
	public int getScoreMax()
	{	return scoreMax;
	}
	
	public boolean isCorrect()
	{	return true;
	}
	
	public boolean isFout()
	{	return false;
	}
	
	public void zetMode(int mode)
	{}
	
	public void zetNagekeken(boolean b)
	{}
	
    public void stop()
    {}
    
    public void start()
    {}
    
    public void destroy()
    {}
    
    public void opnieuw()
    {}
    
    public void kijkNa()
    {}
    
    public void kijkNa(int stapNr)
    {}
    
    public void addActionListener(ActionListener al)
    {}

	public void zetBreedte(int b)
	{}
	
	public void zetHoogte(int h)
	{}
    
	public void actionPerformed(ActionEvent e)
	{
		String command = e.getActionCommand();
		int modifier = e.getModifiers();
		if (command.equals("maakBasis"))
		{	av.zetBasis(modifier);
		}
	}
}
