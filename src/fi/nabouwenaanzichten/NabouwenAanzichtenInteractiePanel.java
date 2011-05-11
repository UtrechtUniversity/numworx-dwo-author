package fi.nabouwenaanzichten;

import java.applet.Applet;
import java.awt.event.*;
import java.awt.*;
import java.util.*;

import javax.swing.*;

import fi.beans.scorm.*;
import fi.beans.copyright.*;
import fi.beans.base64code.*;

import fi.beans.wiskopdrbeans.InteractieEditPanel;
import fi.beans.wiskopdrbeans.InteractiePanel;

/**
 * @author Peter Boon
 */

public class NabouwenAanzichtenInteractiePanel extends JPanel 
	   implements InteractiePanel, InteractieEditPanel, NabouwenAanzichtenIF, NumberListener, ActionListener
{	
	protected SCORM12APIInterface api;
	
	private FIButton fiButton;
	protected static ResourceBundle rb;
	private String langArg;
	private String bgColorArg;
	private Button volLeegKnop, aanzichtenKnop;
	 Viewer3d v;
	private VaktekPanel vp;
	private KubusRooster kr;
	private NumberArrow na;
	private Label aantalKLabel;
	private boolean aanzichten;
	InvulKeuzePanel ip;
	private Color bgcolor = Color.white;
    private boolean mobileVersion;
    
    boolean noSetBounds = false;
	
	boolean rotatieVast = false;
	double beginHoekX = 30;
	double beginHoekY = -30;
	boolean nietBouwenSlopen = false;
	boolean keuzeBouwenSlopen = false;
	boolean perspectief = true;
	boolean pijlAan = true;
	boolean balkAan = false;
	boolean bovenAanzichtMetHoogtes = false;
	
    boolean blokkenBouwsel = true;
    boolean drieAanzichten = false;
    boolean bovenAanzicht = false;
    boolean voorAanzicht = false;
    boolean rechtsAanzicht = false;
    
    
    final double NZERO = 5e-1d;
    
	public NabouwenAanzichtenInteractiePanel()
	{	setLayout(null);
		super.setBounds(0, 0, 300, 300);
		
		setBackground(new Color(230, 240, 255));
		
		kr = new KubusRooster(4, 1);
		
		aanzichten = true;

		// checkboxen bouwen/slopen
        ip = new InvulKeuzePanel(40,360,100,50);
		ip.setBackground(bgcolor);

		ip.setOpaque(false);
		
        ip.setVisible(false);
		add(ip);
		
        // label aantal kubusjes
		aantalKLabel = new Label(NabouwenAanzichten.rb.getString("aantalKLabel")+ kr.geefAantalK());
		aantalKLabel.setBounds(40,460,200,20);
		aantalKLabel.setFont(new Font("SansSerif",Font.PLAIN,18));
		//add(aantalKLabel);
		
        v = new Viewer3d(kr, 0, 0, 300, 300, this);
        v.zetBeginHoeken(beginHoekX, beginHoekY);
		add(v);
		//v.add(ip);
		
		// panel met aanzichten boven
		//                      voor   rechts
		// loopt mee met vloertje
        if (mobileVersion) 
        	vp = new VaktekPanel(kr,0,175,130,130,3, this);
        else 
        	vp = new VaktekPanel(kr,0,20,240,240,3, this);
        vp.zetKlikAan(false);
		vp.zetPijlAan(false);
		vp.zetAchtergrond(bgcolor);
		vp.setVisible(false);
		add(vp);
		
		
		na = new NumberArrow(2,15,4,1,0,"","");
		na.setBounds(120,400,100,50);
		na.setColumns(1);
		na.addNumberListener(this);
        //if(!mobileVersion) add(na);
		
		// knop maak vol/maak leeg
		volLeegKnop = new Button(NabouwenAanzichten.rb.getString("volLeegKnopLabel1"));
        volLeegKnop.addActionListener(this);
        volLeegKnop.setBounds(40, 415, 80, 24);
        
        if(mobileVersion) 
        {  volLeegKnop.setFont(new Font("SansSerif", Font.PLAIN, 10));
           volLeegKnop.setBounds(145,180,55,15);
        }
		//add(volLeegKnop);
		
		
	}
	
	
	
	public void setBounds(int x, int y, int b, int h)
	{	
		
		if ((x == getLocation().x) && (y == getLocation().y) &&
			(b == getSize().width) && (h == getSize().height))
		{	
//System.out.println("setBounds return");			
			return;
		}	
		
		if (noSetBounds)
		{	noSetBounds = false;	
//System.out.println("noSetBounds");			
			return;
		}
		
		if (v != null)
		{	//v.remove(ip);
			remove(v);
		}
		v = new Viewer3d(kr, 0, 0, b, h, this);
		v.zetAchtergrond(bgcolor);
	    v.zetBeginHoeken(beginHoekX, beginHoekY);
		add(v);
		
		if (vp != null)
		{
			remove(vp);
		}
		vp = new VaktekPanel(kr, 0, 0, b, h, 3, this);
		vp.zetAchtergrond(bgcolor);
		vp.setVisible(false);
		add(vp);
		
		super.setBounds(x, y, b, h);
	
		remove(ip);
		ip.setLocation(20, getSize().height - 20 - ip.getSize().height);
		add(ip, 0);
		
		zetPerspectief(perspectief);
		zetRotatieVast(rotatieVast);
		zetNietBouwenSlopen(nietBouwenSlopen);
		
		zetPijlAan(pijlAan);
		zetBalkAan(balkAan);
		
		zetBovenAanzichtMetHoogtes(bovenAanzichtMetHoogtes);
		
		zetBlokkenBouwsel(blokkenBouwsel);
		zetDrieAanzichten(drieAanzichten);
		zetBovenAanzicht(bovenAanzicht);
		zetVoorAanzicht(voorAanzicht);
		zetRechtsAanzicht(rechtsAanzicht);
		
//System.out.println("setBounds b = " + b + " h = " + h);		
		
	}
	
	public void zetBreedte(int b)
	{
		//setBounds(getLocation().x, getLocation().y, b, getSize().height);
	}
	
	public void zetHoogte(int h)
	{
		//setBounds(getLocation().x, getLocation().y, getSize().width, h);
	}
	
	
	public void setBackground(Color color)
	{	bgcolor = color;
		if (v != null)
			v.zetAchtergrond(color);
		if (vp != null)
			vp.zetAchtergrond(color);
		if (ip != null)
			ip.setBackground(color);
		super.setBackground(color);
	}
	
	public void zetBeginHoeken(double hx, double hy)
	{	beginHoekX = hx;
		beginHoekY = hy;
		v.zetBeginHoeken(hx, hy);
	}
	
	public double getBeginHoekX()
	{	return v.getXHoek();
	}
	public double getBeginHoekY()
	{	return v.getYHoek();
	}
	
	public void zetRotatieVast(boolean b)
	{
		rotatieVast = b;
		v.muisAan = !b;
	}
	
	public void zetNietBouwenSlopen(boolean b)
	{
		nietBouwenSlopen = b;
		v.klikAan = !b;
	}
	
	public void zetKeuzeBouwenSlopen(boolean b)
	{
		keuzeBouwenSlopen = b;
//System.out.println("ip before");
/*
		if (keuzeBouwenSlopen)
		{	add(ip, 0);
			validate();
		}
		else
			remove(ip);
*/			
		noSetBounds = true;

		ip.setVisible(b);
//System.out.println("ip after");
	}
	
	public void zetPerspectief(boolean b)
	{
		perspectief = b;
		if (perspectief)
			v.zetAfstand(1000);
		else
			v.zetAfstand(10000000);
		
		v.tekenOpnieuw();
	}
	
	public void zetPijlAan(boolean b)
	{
		pijlAan = b;
		if (pijlAan)
			balkAan = false;
		v.zetPijlAan(b);
		v.tekenOpnieuw();
	}

	public void zetBalkAan(boolean b)
	{
		balkAan = b;
		if (balkAan)
			pijlAan = false;
		v.zetBalkAan(b);
		v.tekenOpnieuw();
	}
	
	public void zetBovenAanzichtMetHoogtes(boolean b)
	{	bovenAanzichtMetHoogtes = b;
		if (bovenAanzichtMetHoogtes)
		{	
			if (vp.isVisible())
			{	noSetBounds = true;
				vp.setVisible(false);
				noSetBounds = true;
				v.setVisible(true);
				if (keuzeBouwenSlopen)
				{	noSetBounds = true;
					ip.setVisible(true);
				}
			}	
		}
	
		v.zetGetalRooster2(bovenAanzichtMetHoogtes);
		
		
		if (!bovenAanzichtMetHoogtes)
		{	v.zetSchaduw(true);
			
			if ((Math.abs(beginHoekX - 90) < NZERO) && (Math.abs(beginHoekY) < NZERO))
				zetBeginHoeken(30, -30);
			else	
				zetBeginHoeken(beginHoekX, beginHoekY);
			zetPerspectief(perspectief);
			zetRotatieVast(rotatieVast);
			zetDrieAanzichten(drieAanzichten);
		}
		else
			v.zetHoogtes();
		v.tekenOpnieuw();
	}
	
	public void zetBlokkenBouwsel(boolean b)
	{	blokkenBouwsel = b;
		if (blokkenBouwsel && !bovenAanzichtMetHoogtes)
		{	
			if (vp.isVisible())
			{	noSetBounds = true;
				vp.setVisible(false);
				noSetBounds = true;
				v.setVisible(true);
				if (keuzeBouwenSlopen)
				{	noSetBounds = true;
					ip.setVisible(true);
				}
			}
			
			drieAanzichten = false;
			bovenAanzicht = false;
			voorAanzicht = false;
			rechtsAanzicht = false;
		}
		else
		{	
			
		}
		
	}
	
	public void zetDrieAanzichten(boolean b)
	{	drieAanzichten = b;
		if (drieAanzichten && !bovenAanzichtMetHoogtes)
		{	
			if (v.isVisible())
			{	
				noSetBounds = true;
				v.setVisible(false);
				//vp.zetDrieAanzichten();
				noSetBounds = true;
				vp.setVisible(true);
				//vp.repaint();
				if (ip.isVisible())
				{	noSetBounds = true;
					ip.setVisible(false);
				}
				
			}
			
			vp.zetDrieAanzichten();			
			
			blokkenBouwsel = false;
			bovenAanzicht = false;
			voorAanzicht = false;
			rechtsAanzicht = false;			
		}
		else
		{
			
		}
		
	}

	public void zetBovenAanzicht(boolean b)
	{	bovenAanzicht = b;
		if (bovenAanzicht && !bovenAanzichtMetHoogtes)
		{	

			if (v.isVisible())
			{	
				noSetBounds = true;
				v.setVisible(false);
				noSetBounds = true;
				//vp.zetEenAanzicht(vp.BOVEN);
				vp.setVisible(true);
				//vp.repaint();
				if (ip.isVisible())
				{	noSetBounds = true;
					ip.setVisible(false);
				}
				
			}
			
			vp.zetEenAanzicht(vp.BOVEN);
			
			blokkenBouwsel = false;
			drieAanzichten = false;
			voorAanzicht = false;
			rechtsAanzicht = false;			
			
		}
		else
		{
			
		}
		
	}

	public void zetVoorAanzicht(boolean b)
	{	voorAanzicht = b;
		if (voorAanzicht && !bovenAanzichtMetHoogtes)
		{	
			if (v.isVisible())
			{	
				noSetBounds = true;
				v.setVisible(false);
				noSetBounds = true;
				//vp.zetEenAanzicht(vp.VOOR);
				vp.setVisible(true);
				//vp.repaint();
				if (ip.isVisible())
				{	noSetBounds = true;
					ip.setVisible(false);
				}
				
			}	
			
			vp.zetEenAanzicht(vp.VOOR);
			
			blokkenBouwsel = false;
			bovenAanzicht = false;
			drieAanzichten = false;
			rechtsAanzicht = false;			
			
		}
		else
		{
			
		}
		
	}

	public void zetRechtsAanzicht(boolean b)
	{	rechtsAanzicht = b;
		if (rechtsAanzicht && !bovenAanzichtMetHoogtes)
		{	
			if (v.isVisible())
			{	
				noSetBounds = true;
				v.setVisible(false);
				noSetBounds = true;
				//vp.zetEenAanzicht(vp.RECHTS);
				vp.setVisible(true);
				//vp.repaint();
				if (ip.isVisible())
				{	noSetBounds = true;
					ip.setVisible(false);
				}
				
			}
				
			vp.zetEenAanzicht(vp.RECHTS);				

			blokkenBouwsel = false;
			bovenAanzicht = false;
			voorAanzicht = false;
			drieAanzichten = false;			
			
		}
		else
		{
			
		}
		
	}
	
	public void zetRoosterGrootte(int rGrootte)
	{
		kr = new KubusRooster(rGrootte, 1);
		
		zetKubusRooster(kr);
		
		
	}
	
	public void setState(Hashtable h)
	{	
		
		double beginHoekX = 30;
		double beginHoekY = -30;
		if (h.containsKey("beginHoekX"))
			beginHoekX = ((Double) h.get("beginHoekX")).doubleValue();
		if (h.containsKey("beginHoekY"))
			beginHoekY = ((Double) h.get("beginHoekY")).doubleValue();
		zetBeginHoeken(beginHoekX, beginHoekY);
		
		String state = null;
		
		if (h.containsKey("state")) 
			state = (String) h.get("state");
		
		if (state == null)
			return;
			
		Object o = StringCodeObject.decodeStringToObject(state);
		boolean[][][][] booleanKRs = (boolean[][][][]) o;
		
		for (int i = 0; i < booleanKRs.length; i++)
	    {	kr = new KubusRooster(booleanKRs[i], 1); //later uitbreiden naar meer kubusroosters
	    }
	    v.zetKubusRooster(kr);
	    vp.zetKubusRooster(kr);
	    na.setValue(kr.maxAantal);
	    
	}
	
	public void zetOpdracht(Hashtable h , String[] variables, Hashtable values)
	{
		boolean rotatieVast = false;
		if (h.containsKey("rotatieVast"))
			rotatieVast = ((Boolean) h.get("rotatieVast")).booleanValue();
		zetRotatieVast(rotatieVast);
		
		double beginHoekX = 30;
		double beginHoekY = -30;
		if (h.containsKey("beginHoekX"))
			beginHoekX = ((Double) h.get("beginHoekX")).doubleValue();
		if (h.containsKey("beginHoekY"))
			beginHoekY = ((Double) h.get("beginHoekY")).doubleValue();
		zetBeginHoeken(beginHoekX, beginHoekY);

		boolean nietBouwenSlopen = false;
		if (h.containsKey("nietBouwenSlopen"))
			nietBouwenSlopen = ((Boolean) h.get("nietBouwenSlopen")).booleanValue();
		zetNietBouwenSlopen(nietBouwenSlopen);
		
		boolean keuzeBouwenSlopen = false;
		if (h.containsKey("keuzeBouwenSlopen"))
			keuzeBouwenSlopen = ((Boolean) h.get("keuzeBouwenSlopen")).booleanValue();
		zetKeuzeBouwenSlopen(keuzeBouwenSlopen);
		
		boolean perspectief = true;
		if (h.containsKey("perspectief"))
			perspectief = ((Boolean) h.get("perspectief")).booleanValue();
		zetPerspectief(perspectief);
		
		boolean pijlAan = true;
		if (h.containsKey("pijlAan"))
			pijlAan = ((Boolean) h.get("pijlAan")).booleanValue();
		zetPijlAan(pijlAan);
		
		boolean balkAan = false;
		if (h.containsKey("balkAan"))
			balkAan = ((Boolean) h.get("balkAan")).booleanValue();
		zetBalkAan(balkAan);
		
		boolean bovenAanzichtMetHoogtes = false;
		if (h.containsKey("bovenAanzichtMetHoogtes"))
			bovenAanzichtMetHoogtes = ((Boolean) h.get("bovenAanzichtMetHoogtes")).booleanValue();
		zetBovenAanzichtMetHoogtes(bovenAanzichtMetHoogtes);
		
		boolean blokkenBouwsel = true;
		if (h.containsKey("blokkenBouwsel"))
			blokkenBouwsel = ((Boolean) h.get("blokkenBouwsel")).booleanValue();
		zetBlokkenBouwsel(blokkenBouwsel);

		boolean drieAanzichten = false;
		if (h.containsKey("drieAanzichten"))
			drieAanzichten = ((Boolean) h.get("drieAanzichten")).booleanValue();
		zetDrieAanzichten(drieAanzichten);
		
		boolean bovenAanzicht = false;
		if (h.containsKey("bovenAanzicht"))
			bovenAanzicht = ((Boolean) h.get("bovenAanzicht")).booleanValue();
		zetBovenAanzicht(bovenAanzicht);
		
		boolean voorAanzicht = false;
		if (h.containsKey("voorAanzicht"))
			voorAanzicht = ((Boolean) h.get("voorAanzicht")).booleanValue();
		zetVoorAanzicht(voorAanzicht);
		
		boolean rechtsAanzicht = false;
		if (h.containsKey("rechtsAanzicht"))
			rechtsAanzicht = ((Boolean) h.get("rechtsAanzicht")).booleanValue();
		zetRechtsAanzicht(rechtsAanzicht);
		
		
		String state = null;
		
		if (h.containsKey("state")) 
			state = (String) h.get("state");
		
		if (state == null)
			return;
		
		Object o = StringCodeObject.decodeStringToObject(state);
		boolean[][][][] booleanKRs = (boolean[][][][]) o;
		
		for (int i = 0; i < booleanKRs.length; i++)
	    {	kr = new KubusRooster(booleanKRs[i], 1); //later uitbreiden naar meer kubusroosters
	    }
	    v.zetKubusRooster(kr);
	    vp.zetKubusRooster(kr);
	    na.setValue(kr.maxAantal);
	    v.zetAchtergrond(getBackground());
	    
	}
	
	public Hashtable getState()
	{	int aantalKR = 0;
		boolean[][][][] booleanKRs = null;
		
		aantalKR = 1;
		booleanKRs = new boolean[aantalKR][][][];
		for (int i = 0; i <aantalKR; i++)
	    {	booleanKRs[i] = kr.geefBooleanRooster(); //later uitbreiden naar meer kubusroosters
	    }
	
	    String state = StringCodeObject.encodeObjectToString(booleanKRs);
	    Hashtable h = new Hashtable();
	    h.put("state", state);
	    
	    h.put("beginHoekX", new Double(getBeginHoekX()));
	    h.put("beginHoekY", new Double(getBeginHoekY()));
	    
	    return h;
	}
	
	public int getIpId()
	{	return 0;
	}
	
	public String getIpExpString()
	{	return null;
	}
	
	public int getScore()
	{	//hier wordt de score berekend
		return 0;
	}
	
	public int getScoreMax()
	{	//hier wordt de score berekend
		return 0;
	}
    
    public boolean hasEditMode()
    {   return false;
    }
    
    public ScormEditComponentIF getEditComponent(Hashtable launchData)
    {   return null;
    }
    
    public Parameter[] getEditableParameters()
    {   return null;
    }
    
    public Parameter[] getAllParameters()
    {   return null;
    }
    public boolean isFocusTraversable()
    {   return false;
    }
    
	
	public void zetVeranderd()
	{	v.tekenOpnieuw();
		aantalKLabel.setText(NabouwenAanzichten.rb.getString("aantalKLabel")+ kr.geefAantalK());
		if (aanzichten)
		{	vp.ra.tekenOpnieuw();
			vp.ba.tekenOpnieuw();
			vp.va.tekenOpnieuw();
		}
	}
	
	
	public boolean isBouwen()
	{	return ip.isBouwen();		
	}
	
	public KubusRooster geefKubusRooster()
	{	return kr;
	}
	
	public void zetKubusRooster(KubusRooster k)
	{	kr = k;
		v.zetKubusRooster(kr);
		vp.zetKubusRooster(kr);
		volLeegKnop.setLabel(NabouwenAanzichten.rb.getString("volLeegKnopLabel1"));
		zetVeranderd();
		na.setValue(kr.maxAantal);
	}
	
	public void numberChanged(String name, double val)
	{	na.transferFocus();
		kr = new KubusRooster((int)val,1);
		v.zetKubusRooster(kr);
		vp.zetKubusRooster(kr);
		volLeegKnop.setLabel(NabouwenAanzichten.rb.getString("volLeegKnopLabel1"));
		zetVeranderd();

	}
	
	public void actionPerformed(ActionEvent e)
	{	if(e.getSource()==volLeegKnop)
		{	volLeegKnop.transferFocus(); 
			if(volLeegKnop.getLabel().equals(NabouwenAanzichten.rb.getString("volLeegKnopLabel1")))
			{	kr.maakVol();
				volLeegKnop.setLabel(NabouwenAanzichten.rb.getString("volLeegKnopLabel2"));
				zetVeranderd();
			}
			else
			{	kr.maakLeeg();
				volLeegKnop.setLabel(NabouwenAanzichten.rb.getString("volLeegKnopLabel1"));
				zetVeranderd();
			}
		}
		if(e.getSource()==aanzichtenKnop)
		{	aanzichtenKnop.transferFocus(); 
			if(aanzichtenKnop.getLabel().equals(NabouwenAanzichten.rb.getString("aanzichtenKnopLabel1")))
			{	vp.setVisible(true);
				aanzichten = true;
				aanzichtenKnop.setLabel(NabouwenAanzichten.rb.getString("aanzichtenKnopLabel2"));
				zetVeranderd();
			}
			else
			{	vp.setVisible(false);
				aanzichten = false;
				aanzichtenKnop.setLabel(NabouwenAanzichten.rb.getString("aanzichtenKnopLabel1"));
				zetVeranderd();
			}
		}
	}
	
	public void setEditState(Hashtable h)
	{	
		boolean rotatieVast = false;
		if (h.containsKey("rotatieVast"))
			rotatieVast = ((Boolean) h.get("rotatieVast")).booleanValue();
		zetRotatieVast(rotatieVast);
		
		double beginHoekX = 30;
		double beginHoekY = -30;
		if (h.containsKey("beginHoekX"))
			beginHoekX = ((Double) h.get("beginHoekX")).doubleValue();
		if (h.containsKey("beginHoekY"))
			beginHoekY = ((Double) h.get("beginHoekY")).doubleValue();
		zetBeginHoeken(beginHoekX, beginHoekY);

		boolean nietBouwenSlopen = false;
		if (h.containsKey("nietBouwenSlopen"))
			nietBouwenSlopen = ((Boolean) h.get("nietBouwenSlopen")).booleanValue();
		zetNietBouwenSlopen(nietBouwenSlopen);
		
		boolean keuzeBouwenSlopen = false;
		if (h.containsKey("keuzeBouwenSlopen"))
			keuzeBouwenSlopen = ((Boolean) h.get("keuzeBouwenSlopen")).booleanValue();
		zetKeuzeBouwenSlopen(keuzeBouwenSlopen);

		boolean perspectief = true;
		if (h.containsKey("perspectief"))
			perspectief = ((Boolean) h.get("perspectief")).booleanValue();
		zetPerspectief(perspectief);

		boolean pijlAan = true;
		if (h.containsKey("pijlAan"))
			pijlAan = ((Boolean) h.get("pijlAan")).booleanValue();
		zetPijlAan(pijlAan);
		
		boolean balkAan = false;
		if (h.containsKey("balkAan"))
			balkAan = ((Boolean) h.get("balkAan")).booleanValue();
		zetBalkAan(balkAan);
		
		boolean bovenAanzichtMetHoogtes = false;
		if (h.containsKey("bovenAanzichtMetHoogtes"))
			bovenAanzichtMetHoogtes = ((Boolean) h.get("bovenAanzichtMetHoogtes")).booleanValue();
		zetBovenAanzichtMetHoogtes(bovenAanzichtMetHoogtes);

		boolean blokkenBouwsel = true;
		if (h.containsKey("blokkenBouwsel"))
			blokkenBouwsel = ((Boolean) h.get("blokkenBouwsel")).booleanValue();
		zetBlokkenBouwsel(blokkenBouwsel);

		boolean drieAanzichten = false;
		if (h.containsKey("drieAanzichten"))
			drieAanzichten = ((Boolean) h.get("drieAanzichten")).booleanValue();
		zetDrieAanzichten(drieAanzichten);
		
		boolean bovenAanzicht = false;
		if (h.containsKey("bovenAanzicht"))
			bovenAanzicht = ((Boolean) h.get("bovenAanzicht")).booleanValue();
		zetBovenAanzicht(bovenAanzicht);
		
		boolean voorAanzicht = false;
		if (h.containsKey("voorAanzicht"))
			voorAanzicht = ((Boolean) h.get("voorAanzicht")).booleanValue();
		zetVoorAanzicht(voorAanzicht);
		
		boolean rechtsAanzicht = false;
		if (h.containsKey("rechtsAanzicht"))
			rechtsAanzicht = ((Boolean) h.get("rechtsAanzicht")).booleanValue();
		zetRechtsAanzicht(rechtsAanzicht);
		
		
		String state = null;
		if (h.containsKey("state")) 
			state = (String)h.get("state");
		if (state == null)
			return;
		Object o = StringCodeObject.decodeStringToObject(state);
		boolean[][][][] booleanKRs = (boolean[][][][])o;
		for (int i = 0; i < booleanKRs.length; i++)
	    {	kr = new KubusRooster(booleanKRs[i], 1); //later uitbreiden naar meer kubusroosters
	    }
	    v.zetKubusRooster(kr);
	    vp.zetKubusRooster(kr);
	    na.setValue(kr.maxAantal);
	}
	
	public Hashtable getEditState()
	{	int aantalKR = 0;
		boolean[][][][] booleanKRs = null;
		
		aantalKR = 1;
		booleanKRs = new boolean[aantalKR][][][];
		for (int i = 0; i < aantalKR; i++)
	    {	booleanKRs[i] = kr.geefBooleanRooster(); //later uitbreiden naar meer kubusroosters
	    }
	
	    String state = StringCodeObject.encodeObjectToString(booleanKRs);
	    
	    Hashtable h = new Hashtable();
	    h.put("state", state);
	    
	    h.put("rotatieVast", new Boolean(rotatieVast));
	    
	    h.put("beginHoekX", new Double(getBeginHoekX()));
	    h.put("beginHoekY", new Double(getBeginHoekY()));
	    
	    h.put("nietBouwenSlopen", new Boolean(nietBouwenSlopen));
	    
	    h.put("keuzeBouwenSlopen", new Boolean(keuzeBouwenSlopen));
	    
	    h.put("perspectief", new Boolean(perspectief));
	    
	    h.put("pijlAan", new Boolean(pijlAan));
	    h.put("balkAan", new Boolean(balkAan));
	    
	    h.put("bovenAanzichtMetHoogtes", new Boolean(bovenAanzichtMetHoogtes));
	    
	    h.put("blokkenBouwsel", new Boolean(blokkenBouwsel));
	    h.put("drieAanzichten", new Boolean(drieAanzichten));
	    h.put("bovenAanzicht", new Boolean(bovenAanzicht));
	    h.put("voorAanzicht", new Boolean(voorAanzicht));
	    h.put("rechtsAanzicht", new Boolean(rechtsAanzicht));
	    
	    h.put("roosterGrootte", new Integer(v.kr.maxAantal));
	    
	    return h;
	}
	
	public InteractieEditPanel getEditPanel(){return new NabouwenAanzichtenInteractieEditPanel();}
		
	public void wis(){}
	
	public int geefAsHoogte(){return 0;}
	
	public boolean isCorrect(){return true;}
	
	public boolean isFout(){return false;}
	
	public void zetMode(int mode){}
	
	public void zetNagekeken(boolean b){}
	
    public void stop(){}
    
    public void destroy(){}
    
    public void zetMaat(){}
	
    public void start(){}
     
    public void opnieuw(){}
    
    public void kijkNa(){}
    
    public void kijkNa(int stapNr){}
    
    public void addActionListener(ActionListener al){}
	
}