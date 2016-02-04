package fi.tegels;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;
import java.util.HashMap;
import java.util.Vector;
import java.util.ArrayList;

import javax.swing.*;

import fi.beans.base64code.StringCodeObject;
import fi.beans.wiskopdrbeans.WiskOpdrApplet;
// deze moet vanwege interface WiskOpdrApplet
import fi.beans.wiskopdrbeans.InteractiePanel;
// deze moet vanwege interface InteractiePanel
import fi.beans.wiskopdrbeans.InteractieEditPanel;

public class TegelsInteractiePanel extends JPanel implements InteractiePanel, InteractieEditPanel,
							         ActionListener
									
{
	TegelsPanel tegelsPanel;
	
	int score = 0;
	int scoreMax = 10;
	
	boolean noSetBounds = false;
	
	boolean transVersion = false;
	boolean demoVersion = false;
	
	public TegelsInteractiePanel()
	{
		setLayout(null);
		// echte initiatie vind pas plaats na setBounds
		
		
	}

	public void zetTransVersion(boolean b)
	{
		transVersion = b;
		tegelsPanel.zetTransVersion(transVersion);
		
	}

	public void zetDemoVersion(boolean b)
	{
		demoVersion = b;
		tegelsPanel.zetDemoVersion(demoVersion);		
	}
	
	public void zetOpdracht(Hashtable b, String[] randomVars, Hashtable randomValues)
	{
System.out.println("tip zetOpdracht");		
		
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

		zetTransVersion(transVersion);
		zetDemoVersion(demoVersion);
		
		if (b.containsKey("appletEditState"))
		{
System.out.println("aES found");
			String appletEditState = (String) b.get("appletEditState");
			
			// decodeer de string
			Object o = StringCodeObject.decodeStringToObject(appletEditState);
			// cast
			Hashtable h = (Hashtable) o;

			Vector schuifStukkenVector = new Vector();
			if ((h!= null) && h.containsKey("schuifstukken"))
				schuifStukkenVector = (Vector) h.get("schuifstukken");
		
			tegelsPanel.aantalSs = schuifStukkenVector.size();
			for (int sCnt = 0; sCnt < tegelsPanel.aantalSs; sCnt++)
			{	tegelsPanel.ss[sCnt] = (SchuifStuk) schuifStukkenVector.elementAt(sCnt);
			}

			Vector basisVormenVector = new Vector();
			if ((h!=null) && h.containsKey("basisvormen"))
				basisVormenVector = (Vector) h.get("basisvormen");

			tegelsPanel.basisVormen = new Vector();
			for (int bCnt = 0; bCnt < basisVormenVector.size(); bCnt++)
			{	tegelsPanel.basisVormen.addElement((SchuifStuk) basisVormenVector.elementAt(bCnt));
			}
			
			if (tegelsPanel.basisVormen.size() >= 1)
			{
				tegelsPanel.zetBasisVorm((SchuifStuk) tegelsPanel.basisVormen.elementAt(0));
				if (tegelsPanel.basisVormen.size() > 1)
					tegelsPanel.cp.downButton.setEnabled(true);
			}
			
		}
		else
		{
			Vector schuifStukkenVector = new Vector();
			if (b.containsKey("schuifStukkenVector"))
				schuifStukkenVector = (Vector) b.get("schuifStukkenVector");
		
			tegelsPanel.aantalSs = schuifStukkenVector.size();
			for (int sCnt = 0; sCnt < tegelsPanel.aantalSs; sCnt++)
			{	tegelsPanel.ss[sCnt] = (SchuifStuk) schuifStukkenVector.elementAt(sCnt);
			}

			Vector basisVormenVector = new Vector();
			if (b.containsKey("basisVormenVector"))
				basisVormenVector = (Vector) b.get("basisVormenVector");

			tegelsPanel.basisVormen = new Vector();
			for (int bCnt = 0; bCnt < basisVormenVector.size(); bCnt++)
			{	tegelsPanel.basisVormen.addElement((SchuifStuk) basisVormenVector.elementAt(bCnt));
			}
			
			if (tegelsPanel.basisVormen.size() >= 1)
			{
				tegelsPanel.zetBasisVorm((SchuifStuk) tegelsPanel.basisVormen.elementAt(0));
				if (tegelsPanel.basisVormen.size() > 1)
					tegelsPanel.cp.downButton.setEnabled(true);
			}
			
		}
		
	}
	
	public void setState(Hashtable b)
	{
		if (b.containsKey("appletState"))
		{
System.out.println("aES found");
			String appletState = (String) b.get("appletState");
			
			// decodeer de string
			Object o = StringCodeObject.decodeStringToObject(appletState);
			// cast
			Hashtable h = (Hashtable) o;

			Vector schuifStukkenVector = new Vector();
			if ((h!=null) && h.containsKey("schuifstukken"))
				schuifStukkenVector = (Vector) h.get("schuifstukken");
		
			tegelsPanel.aantalSs = schuifStukkenVector.size();
			for (int sCnt = 0; sCnt < tegelsPanel.aantalSs; sCnt++)
			{	tegelsPanel.ss[sCnt] = (SchuifStuk) schuifStukkenVector.elementAt(sCnt);
			}

			Vector basisVormenVector = new Vector();
			if ((h!=null) && h.containsKey("basisvormen"))
				basisVormenVector = (Vector) h.get("basisvormen");

			tegelsPanel.basisVormen = new Vector();
			for (int bCnt = 0; bCnt < basisVormenVector.size(); bCnt++)
			{	tegelsPanel.basisVormen.addElement((SchuifStuk) basisVormenVector.elementAt(bCnt));
			}
			
			if (tegelsPanel.basisVormen.size() >= 1)
			{
				tegelsPanel.zetBasisVorm((SchuifStuk) tegelsPanel.basisVormen.elementAt(0));
				if (tegelsPanel.basisVormen.size() > 1)
					tegelsPanel.cp.downButton.setEnabled(true);
			}
			
		}	
		else
		{
			Vector schuifStukkenVector = new Vector();
			if (b.containsKey("schuifStukkenVector"))
				schuifStukkenVector = (Vector) b.get("schuifStukkenVector");
		
			tegelsPanel.aantalSs = schuifStukkenVector.size();
			for (int sCnt = 0; sCnt < tegelsPanel.aantalSs; sCnt++)
			{	tegelsPanel.ss[sCnt] = (SchuifStuk) schuifStukkenVector.elementAt(sCnt);
			}

			Vector basisVormenVector = new Vector();
			if (b.containsKey("basisVormenVector"))
				basisVormenVector = (Vector) b.get("basisVormenVector");

			tegelsPanel.basisVormen = new Vector();
			for (int bCnt = 0; bCnt < basisVormenVector.size(); bCnt++)
			{	tegelsPanel.basisVormen.addElement((SchuifStuk) basisVormenVector.elementAt(bCnt));
			}
			
			if (tegelsPanel.basisVormen.size() >= 1)
			{
				tegelsPanel.zetBasisVorm((SchuifStuk) tegelsPanel.basisVormen.elementAt(0));
				if (tegelsPanel.basisVormen.size() > 1)
					tegelsPanel.cp.downButton.setEnabled(true);
			}
			
		}

	}
	
	public void setEditState(Hashtable b)
	{
System.out.println("tip setEditState");
		
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

		zetTransVersion(transVersion);
		zetDemoVersion(demoVersion);
		
		if (b.containsKey("appletEditState"))
		{
System.out.println("aES found");
			String appletEditState = (String) b.get("appletEditState");
			
			// decodeer de string
			Object o = StringCodeObject.decodeStringToObject(appletEditState);
			// cast
			Hashtable h = (Hashtable) o;

			Vector schuifStukkenVector = new Vector();
			if (h.containsKey("schuifstukken"))
				schuifStukkenVector = (Vector) h.get("schuifstukken");
		
			tegelsPanel.aantalSs = schuifStukkenVector.size();
			for (int sCnt = 0; sCnt < tegelsPanel.aantalSs; sCnt++)
			{	tegelsPanel.ss[sCnt] = (SchuifStuk) schuifStukkenVector.elementAt(sCnt);
			}

			Vector basisVormenVector = new Vector();
			if (h.containsKey("basisvormen"))
				basisVormenVector = (Vector) h.get("basisvormen");

			tegelsPanel.basisVormen = new Vector();
			for (int bCnt = 0; bCnt < basisVormenVector.size(); bCnt++)
			{	tegelsPanel.basisVormen.addElement((SchuifStuk) basisVormenVector.elementAt(bCnt));
			}
			
			if (tegelsPanel.basisVormen.size() >= 1)
			{
				tegelsPanel.zetBasisVorm((SchuifStuk) tegelsPanel.basisVormen.elementAt(0));
				if (tegelsPanel.basisVormen.size() > 1)
					tegelsPanel.cp.downButton.setEnabled(true);
			}

		}
		else
		{
			Vector schuifStukkenVector = new Vector();
			if (b.containsKey("schuifStukkenVector"))
				schuifStukkenVector = (Vector) b.get("schuifStukkenVector");
		
			tegelsPanel.aantalSs = schuifStukkenVector.size();
			for (int sCnt = 0; sCnt < tegelsPanel.aantalSs; sCnt++)
			{	tegelsPanel.ss[sCnt] = (SchuifStuk) schuifStukkenVector.elementAt(sCnt);
			}

			Vector basisVormenVector = new Vector();
			if (b.containsKey("basisVormenVector"))
				basisVormenVector = (Vector) b.get("basisVormenVector");

			tegelsPanel.basisVormen = new Vector();
			for (int bCnt = 0; bCnt < basisVormenVector.size(); bCnt++)
			{	tegelsPanel.basisVormen.addElement((SchuifStuk) basisVormenVector.elementAt(bCnt));
			}
			
			if (tegelsPanel.basisVormen.size() >= 1)
			{
				tegelsPanel.zetBasisVorm((SchuifStuk) tegelsPanel.basisVormen.elementAt(0));
				if (tegelsPanel.basisVormen.size() > 1)
					tegelsPanel.cp.downButton.setEnabled(true);
			}
			
		}
		
	}
	
	public Hashtable getState()
	{
		Hashtable h = new Hashtable();
		
		Vector schuifStukkenVector = new Vector();
		for (int sCnt = 0; sCnt < tegelsPanel.aantalSs; sCnt++)
		{	schuifStukkenVector.addElement(tegelsPanel.ss[sCnt]);
		}
		h.put("schuifStukkenVector", schuifStukkenVector);
		
		Vector basisVormenVector = new Vector();
		for (int bCnt = 0; bCnt < tegelsPanel.basisVormen.size(); bCnt++)
		{	basisVormenVector.addElement((SchuifStuk) tegelsPanel.basisVormen.elementAt(bCnt));
		}
		h.put("basisVormenVector", basisVormenVector);
		
		
		return h;
		
	}
	
	public Hashtable getEditState()
	{
		Hashtable h = new Hashtable();
		
		h.put("transVersion", new Boolean(transVersion));
		h.put("demoVersion", new Boolean(demoVersion));

		Vector schuifStukkenVector = new Vector();
		for (int sCnt = 0; sCnt < tegelsPanel.aantalSs; sCnt++)
		{	schuifStukkenVector.addElement(tegelsPanel.ss[sCnt]);
		}
		h.put("schuifStukkenVector", schuifStukkenVector);
		
		Vector basisVormenVector = new Vector();
		for (int bCnt = 0; bCnt < tegelsPanel.basisVormen.size(); bCnt++)
		{	basisVormenVector.addElement((SchuifStuk) tegelsPanel.basisVormen.elementAt(bCnt));
		}
		h.put("basisVormenVector", basisVormenVector);

		
		//GWT
		ArrayList<HashMap> schuifStukkenList = new ArrayList<HashMap>();
		for (int sCnt = 0; sCnt < tegelsPanel.aantalSs; sCnt++)
		{	schuifStukkenList.add(NoSer.getSSState(tegelsPanel.ss[sCnt]));
		}
		h.put("schuifStukkenList", schuifStukkenList);
		
		ArrayList<HashMap> basisVormenList = new ArrayList<HashMap>();
		for (int bCnt = 0; bCnt < tegelsPanel.basisVormen.size(); bCnt++)
		{	basisVormenList.add(NoSer.getSSState((SchuifStuk)tegelsPanel.basisVormen.elementAt(bCnt)));
		}
		h.put("basisVormenList", basisVormenList);
		
		
		return h;
	}
	
	
	
	public InteractieEditPanel getEditPanel()
	{
		return new TegelsInteractieEditPanel();
	}
		
	public void setBounds(int x, int y, int b, int h)
	{
		
//		System.out.println("tip set bounds " + b + " " + h);
		
		if (h == 1)
			return;
		
		super.setBounds(x, y, b, h);
		
		
		if (tegelsPanel == null) 
		{	tegelsPanel = new TegelsPanel(b, h, this);
			add(tegelsPanel);
//System.out.println("tegelsPanel created");			
		}
		else
		{	tegelsPanel.setSize(b, h);

//System.out.println("tegelsPanel sized");		
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
	{}
}
