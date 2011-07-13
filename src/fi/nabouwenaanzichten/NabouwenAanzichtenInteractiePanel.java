package fi.nabouwenaanzichten;

import java.applet.Applet;
import java.awt.event.*;
import java.awt.*;
import java.util.*;

import javax.swing.*;

import fi.beans.scorm.*;
import fi.beans.copyright.*;
import fi.beans.base64code.*;

import fi.beans.appletutil.*;

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
	Viewer3d docentV;
	private VaktekPanel vp;
	private KubusRooster kr;
	KubusRooster docentKr;
	private NumberArrow na;
	private Label aantalKLabel;
	private boolean aanzichten;
	InvulKeuzePanel2 ip;
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
    boolean voorZijAanzicht = false;
    boolean bovenAanzicht = false;
    boolean voorAanzicht = false;
    boolean rechtsAanzicht = false;

    
    boolean kijkNaActief = false;
    
    boolean checkBlokkenBouwsel = true;
    boolean checkDrieAanzichten = false;
    boolean checkVoorZijAanzicht = false;
    boolean checkBovenVoorAanzicht = false;
    boolean checkBovenZijAanzicht = false;
    boolean checkBovenAanzicht = false;
    boolean checkVoorAanzicht = false;
    boolean checkRechtsAanzicht = false;
    
    boolean checkAantalKubus = false;
    
    final double NZERO = 5e-1d;
    
    boolean vIsVisible = false;
    boolean vpIsVisible = false;
    boolean ipIsVisible = false;
    
    boolean kijkNaActiefKlein = false;
    
    int score = 0;
    int maxScore = 10;
    
    Vector listeners = new Vector();
    
	JButton kijkNaButton;
	JPanel kijkNaPanel;
	JLabel groenVinkjeLabel;
	JLabel geelVinkjeLabel;
	JLabel kruisjeLabel;
	
   
	public NabouwenAanzichtenInteractiePanel()
	{	setLayout(null);
		super.setBounds(0, 0, 300, 300);
		
		setBackground(new Color(230, 240, 255));
		
		kr = new KubusRooster(4, 1);
		docentKr = new KubusRooster(4, 1);
		
		aanzichten = true;

		// checkboxen bouwen/slopen
        //ip = new InvulKeuzePanel(40,360,100,50);
		ip = new InvulKeuzePanel2(0,300,300,24);
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

        docentV = new Viewer3d(docentKr, 0, 0, 300, 300, this);
        docentV.zetBeginHoeken(beginHoekX, beginHoekY);
        docentV.setVisible(false);
		add(docentV);
		
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
		
		kijkNaButton = new JButton(NabouwenAanzichten.rb.getString("kijkNaTekst"));
		kijkNaButton.setFont(new Font("SansSerif",Font.PLAIN, 12));
		kijkNaButton.setBounds(0, 0, 100, 20);
		kijkNaButton.addActionListener(new KijkNaAL());
		
		java.net.URL imageURL = NabouwenAanzichten.class.getResource("resources/groenvink.gif");
		if (imageURL != null)
		{
		    groenVinkjeLabel = new JLabel(new ImageIcon(imageURL));
		}
		else 
		{
			System.out.println("Error reading groenvink.gif.");
			groenVinkjeLabel = new JLabel();
		}
		groenVinkjeLabel.setBounds(100, 0, 20, 20);
		
		imageURL = NabouwenAanzichten.class.getResource("resources/geelvink.gif");
		if (imageURL != null) {
		    geelVinkjeLabel = new JLabel(new ImageIcon(imageURL));
		}
		else 
		{
			System.out.println("Error reading geelvink.gif.");
			geelVinkjeLabel = new JLabel();
		}
		geelVinkjeLabel.setBounds(100, 0, 20, 20);
		
		
		imageURL = NabouwenAanzichten.class.getResource("resources/foutkruis.gif");
		if (imageURL != null) {
		    kruisjeLabel = new JLabel(new ImageIcon(imageURL));
		}
		else {
			System.out.println("Error reading foutkruis.gif.");
			kruisjeLabel = new JLabel();
		}
		kruisjeLabel.setBounds(100, 0, 20, 20);
		
		groenVinkjeLabel.setVisible(false);
		geelVinkjeLabel.setVisible(false);
		kruisjeLabel.setVisible(false);
		
		kijkNaPanel = new JPanel(null);
		//kijkNaPanel.setBackground(Color.WHITE);
		kijkNaPanel.setSize(120, 20);
		kijkNaPanel.add(kijkNaButton);
		kijkNaPanel.add(groenVinkjeLabel);
		kijkNaPanel.add(geelVinkjeLabel);
		kijkNaPanel.add(kruisjeLabel);
		kijkNaPanel.setVisible(false);
		
		add(kijkNaPanel);
		
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
		//int vpX = Math.max(0,(b-h)/2);
        //int vpY = Math.max(0,(h-b)/2);
        int vpZijde = Math.min(b, h - 24 - 24);
        int vpX = (b - vpZijde) / 2;
        int vpY = (h - 24 - 24 - vpZijde) / 2;
		//if(bovenAanzichtMetHoogtes)
		    v = new Viewer3d(kr, vpX, vpY, vpZijde, vpZijde, this);
		//else v = new Viewer3d(kr, 0, 0, b, h, this);
		v.zetAchtergrond(bgcolor);
	    v.zetBeginHoeken(beginHoekX, beginHoekY);
		add(v);
		
		kijkNaPanel.setLocation(Math.max(0, (b - kijkNaPanel.getSize().width) / 2),
							    h - kijkNaPanel.getSize().height); 
		
		if (vp != null)
		{
			remove(vp);
		}
		vp = new VaktekPanel(kr, 0, 0, b, h, 3, this);
		vp.zetAchtergrond(bgcolor);
		vp.setVisible(false);
		add(vp);
		
		if (docentV != null)
		{	//v.remove(ip);
			remove(docentV);
		}
		//int vpX = Math.max(0,(b-h)/2);
        //int vpY = Math.max(0,(h-b)/2);
        vpZijde = Math.min(b,h - 24 - 24);
        vpX = (b - vpZijde) / 2;
        vpY = (h - 24 - 24 - vpZijde) / 2;
		//if(bovenAanzichtMetHoogtes)
		docentV = new Viewer3d(docentKr, vpX, vpY, vpZijde, vpZijde, this);
		//else v = new Viewer3d(kr, 0, 0, b, h, this);
		docentV.zetAchtergrond(bgcolor);
		docentV.zetBeginHoeken(beginHoekX, beginHoekY);
		docentV.setVisible(false);
		add(docentV, 0);

		
		super.setBounds(x, y, Math.min(600, b), h);
	
		//remove(ip);
		ip.setBounds(vpX, vpY + vpZijde, vpZijde, 24);
		//add(ip, 0);
		
		zetPerspectief(perspectief);
		zetRotatieVast(rotatieVast);
		zetNietBouwenSlopen(nietBouwenSlopen);
		
		zetPijlAan(pijlAan);
		zetBalkAan(balkAan);
		
		zetBovenAanzichtMetHoogtes(bovenAanzichtMetHoogtes);
		
		zetBlokkenBouwsel(blokkenBouwsel);
		zetDrieAanzichten(drieAanzichten);
		zetVoorZijAanzicht(voorZijAanzicht);
		zetBovenAanzicht(bovenAanzicht);
		zetVoorAanzicht(voorAanzicht);
		zetRechtsAanzicht(rechtsAanzicht);
		
		if (kijkNaActiefKlein)
		{	kijkNaActiefKlein = false;
		}
		else if (kijkNaActief && !kijkNaPanel.isVisible())
		{	noSetBounds = true;
			kijkNaPanel.setVisible(true);
			
		}
//System.out.println("setBounds naip b = " + b + " h = " + h);		
		
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
		if (kijkNaPanel != null)
			kijkNaPanel.setBackground(color);
		
		if (docentV != null)
			docentV.zetAchtergrond(color);
		
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
			v.zetAfstand(1000000000);
		
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
		{	zetPerspectief(false);
			if (vp.isVisible())
			{	noSetBounds = true;
				vp.setVisible(false);
				noSetBounds = true;
				v.setVisible(true);
				if (keuzeBouwenSlopen)
				{	noSetBounds = true;
					ip.setVisible(true);
				}
				if (kijkNaPanel.isVisible())
				{	noSetBounds = true;
					kijkNaPanel.setVisible(false);
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
				if (kijkNaActief)
				{	noSetBounds = true;
					kijkNaPanel.setVisible(true);
				}
			}
			
			drieAanzichten = false;
			voorZijAanzicht = false;
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
				if (kijkNaPanel.isVisible())
				{	noSetBounds = true;
					kijkNaPanel.setVisible(false);
				}				
				
			}
			
			vp.zetDrieAanzichten();			
			
			blokkenBouwsel = false;
			voorZijAanzicht = false;			
			bovenAanzicht = false;
			voorAanzicht = false;
			rechtsAanzicht = false;			
		}
		else
		{
			
		}
		
	}

	public void zetVoorZijAanzicht(boolean b)
	{	voorZijAanzicht = b;
		if (voorZijAanzicht && !bovenAanzichtMetHoogtes)
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
				if (kijkNaPanel.isVisible())
				{	noSetBounds = true;
					kijkNaPanel.setVisible(false);
				}				
				
			}
		
			vp.zetVoorZijAanzicht();			
			
			blokkenBouwsel = false;
			drieAanzichten = false;			
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
				if (kijkNaPanel.isVisible())
				{	noSetBounds = true;
					kijkNaPanel.setVisible(false);
				}				
				
			}
			
			vp.zetEenAanzicht(vp.BOVEN);
			
			blokkenBouwsel = false;
			drieAanzichten = false;
			voorZijAanzicht = false;			
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
				if (kijkNaPanel.isVisible())
				{	noSetBounds = true;
					kijkNaPanel.setVisible(false);
				}				
				
			}	
			
			vp.zetEenAanzicht(vp.VOOR);
			
			blokkenBouwsel = false;
			voorZijAanzicht = false;
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
				if (kijkNaPanel.isVisible())
				{	noSetBounds = true;
					kijkNaPanel.setVisible(false);
				}				
				
			}
				
			vp.zetEenAanzicht(vp.RECHTS);				

			blokkenBouwsel = false;
			voorZijAanzicht = false;			
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
		docentKr = new KubusRooster(rGrootte, 1);
		
		zetKubusRooster(kr);
		zetDocentKubusRooster(docentKr);
		
		
	}

	public void toonDocentViewer(boolean b)
	{	if (b)
		{	if (v.isVisible())
			{	noSetBounds = true;
				v.setVisible(false);
				vIsVisible = true;
				noSetBounds = true;
				kijkNaPanel.setVisible(false);
			}
			else
				vIsVisible = false;
		
			if (vp.isVisible())
			{	noSetBounds = true;
				vp.setVisible(false);
				vpIsVisible = true;
			}
			else
				vpIsVisible = false;
	
			if (!ip.isVisible())
			{	noSetBounds = true;
				ip.setVisible(true);
				ipIsVisible = false;
			}
			else
				ipIsVisible = true;
			
			if (!docentV.isVisible())
			{	noSetBounds = true;
				docentV.setVisible(true);
			}
		
		}
		else
		{	if (docentV.isVisible())
			{	noSetBounds = true;
				docentV.setVisible(false);
			}
		
			if (vIsVisible)
			{	noSetBounds = true;
				v.setVisible(true);
				noSetBounds = true;
				kijkNaPanel.setVisible(kijkNaActief);
			}

			if (vpIsVisible)
			{	noSetBounds = true;
				vp.setVisible(true);
			}
			
			if (!ipIsVisible)
			{	noSetBounds = true;
				ip.setVisible(false);
			}
		}
		
	}
	
    public void zetKijkNaActief(boolean b)
    {	kijkNaActief = b;
    
    	if (kijkNaActief)
    	{	toonDocentViewer(true);
    	}
    	else
    	{	toonDocentViewer(false);
    	}
    	
    }
    
    public void zetCheckBlokkenBouwsel(boolean b)
    {	checkBlokkenBouwsel = b;
    	if (checkBlokkenBouwsel)
    	{
    	    checkDrieAanzichten = false;
    	    checkVoorZijAanzicht = false;
    	    checkBovenVoorAanzicht = false;
    	    checkBovenZijAanzicht = false;
    	    checkBovenAanzicht = false;
    	    checkVoorAanzicht = false;
    	    checkRechtsAanzicht = false;
    		
    	}
    	
    }
    
    public void zetCheckDrieAanzichten(boolean b)
    {	checkDrieAanzichten = b;
		if (checkDrieAanzichten)
		{
			checkBlokkenBouwsel = false;
			checkVoorZijAanzicht = false;
			checkBovenVoorAanzicht = false;
			checkBovenZijAanzicht = false;
			checkBovenAanzicht = false;
			checkVoorAanzicht = false;
			checkRechtsAanzicht = false;
		
		}
    	
    }

    public void zetCheckVoorZijAanzicht(boolean b)
    {	checkVoorZijAanzicht = b;
    	if (checkVoorZijAanzicht)
    	{
    		checkBlokkenBouwsel = false;
    		checkDrieAanzichten = false;
    		checkBovenVoorAanzicht = false;
    		checkBovenZijAanzicht = false;
    		checkBovenAanzicht = false;
    		checkVoorAanzicht = false;
    		checkRechtsAanzicht = false;
		
    	}
    	
    }

    public void zetCheckBovenVoorAanzicht(boolean b)
    {	checkBovenVoorAanzicht = b;
    	if (b)
    	{
    		checkBlokkenBouwsel = false;
    		checkDrieAanzichten = false;
    		checkVoorZijAanzicht = false;
    		checkBovenZijAanzicht = false;
    		checkBovenAanzicht = false;
    		checkVoorAanzicht = false;
    		checkRechtsAanzicht = false;
		
    	}
    	
    }
    
    public void zetCheckBovenZijAanzicht(boolean b)
    {	checkBovenZijAanzicht = b;
    	if (b)
    	{
    		checkBlokkenBouwsel = false;
    		checkDrieAanzichten = false;
    		checkVoorZijAanzicht = false;
    		checkBovenVoorAanzicht = false;
    		checkBovenAanzicht = false;
    		checkVoorAanzicht = false;
    		checkRechtsAanzicht = false;
		
    	}
    	
    }
    
    public void zetCheckBovenAanzicht(boolean b)
    {	checkBovenAanzicht = b;
		if (checkBovenAanzicht)
		{
			checkBlokkenBouwsel = false;
			checkDrieAanzichten = false;
			checkVoorZijAanzicht = false;
			checkBovenVoorAanzicht = false;
			checkBovenZijAanzicht = false;
			checkVoorAanzicht = false;
			checkRechtsAanzicht = false;
		
		}
    	
    }
    
    public void zetCheckVoorAanzicht(boolean b)
    {	checkVoorAanzicht = b;
		if (checkVoorAanzicht)
		{
	    	checkBlokkenBouwsel = false;
	    	checkDrieAanzichten = false;
	    	checkVoorZijAanzicht = false;
	    	checkBovenVoorAanzicht = false;
	    	checkBovenZijAanzicht = false;
	    	checkBovenAanzicht = false;
	    	checkRechtsAanzicht = false;
		
		}
    	
    }
    
    public void zetCheckRechtsAanzicht(boolean b)
    {	checkRechtsAanzicht = b;
    	if (checkRechtsAanzicht)
    	{
    		checkBlokkenBouwsel = false;
    		checkDrieAanzichten = false;
    		checkVoorZijAanzicht = false;
    		checkBovenVoorAanzicht = false;
    		checkBovenZijAanzicht = false;
    		checkBovenAanzicht = false;
    		checkVoorAanzicht = false;
		
    	}
    	
    }
    
    public void zetCheckAantalKubus(boolean b)
    {	checkAantalKubus = b;
    	
    }

    public void zetMaxScore(int ms)
    {	maxScore = ms;
    	
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
		
		if (state != null)
		{	
			Object o = StringCodeObject.decodeStringToObject(state);
			boolean[][][][] booleanKRs = (boolean[][][][]) o;
		
			for (int i = 0; i < booleanKRs.length; i++)
			{	kr = new KubusRooster(booleanKRs[i], 1); //later uitbreiden naar meer kubusroosters
			}
			v.zetKubusRooster(kr);
			vp.zetKubusRooster(kr);
			na.setValue(kr.maxAantal);
		}
		
		String docentState = null;
		
		if (h.containsKey("docentState")) 
			docentState = (String) h.get("docentState");
		
		if (docentState == null)
			return;
			
		Object o = StringCodeObject.decodeStringToObject(docentState);
		boolean[][][][] booleanDocentKRs = (boolean[][][][]) o;
		
		for (int i = 0; i < booleanDocentKRs.length; i++)
	    {	docentKr = new KubusRooster(booleanDocentKRs[i], 1); //later uitbreiden naar meer kubusroosters
	    }
	    docentV.zetKubusRooster(docentKr);
	    
	}
	
	public void zetOpdracht(Hashtable h , String[] variables, Hashtable values)
	{
		// viewer opties
		
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
		
		boolean voorZijAanzicht = false;
		if (h.containsKey("voorZijAanzicht"))
			voorZijAanzicht = ((Boolean) h.get("voorZijAanzicht")).booleanValue();
		zetVoorZijAanzicht(voorZijAanzicht);
		
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
		
		// nakijk opties
		
		boolean kijkNaActief = false;
		if (h.containsKey("kijkNaActief"))
			kijkNaActief = ((Boolean) h.get("kijkNaActief")).booleanValue();
		
		boolean checkBlokkenBouwsel = true;
		if (h.containsKey("checkBlokkenBouwsel"))
			checkBlokkenBouwsel = ((Boolean) h.get("checkBlokkenBouwsel")).booleanValue();

		boolean checkDrieAanzichten = false;
		if (h.containsKey("checkDrieAanzichten"))
			checkDrieAanzichten = ((Boolean) h.get("checkDrieAanzichten")).booleanValue();

	    boolean checkVoorZijAanzicht = false;
		if (h.containsKey("checkVoorZijAanzicht"))
			checkVoorZijAanzicht = ((Boolean) h.get("checkVoorZijAanzicht")).booleanValue();
	    
	    boolean checkBovenVoorAanzicht = false;
		if (h.containsKey("checkBovenVoorAanzicht"))
			checkBovenVoorAanzicht = ((Boolean) h.get("checkBovenVoorAanzicht")).booleanValue();
	    
	    boolean checkBovenZijAanzicht = false;
		if (h.containsKey("checkBovenZijAanzicht"))
			checkBovenZijAanzicht = ((Boolean) h.get("checkBovenZijAanzicht")).booleanValue();
		
		boolean checkBovenAanzicht = false;
		if (h.containsKey("checkBovenAanzicht"))
			checkBovenAanzicht = ((Boolean) h.get("checkBovenAanzicht")).booleanValue();
		
		boolean checkVoorAanzicht = false;
		if (h.containsKey("checkVoorAanzicht"))
			checkVoorAanzicht = ((Boolean) h.get("checkVoorAanzicht")).booleanValue();
				
		boolean checkRechtsAanzicht = false;
		if (h.containsKey("checkRechtsAanzicht"))
			checkRechtsAanzicht = ((Boolean) h.get("checkRechtsAanzicht")).booleanValue();
				
		boolean checkAantalKubus = false;
		if (h.containsKey("checkAantalKubus"))
			checkAantalKubus = ((Boolean) h.get("checkAantalKubus")).booleanValue();
		
		int maxScore = 10;
		if (h.containsKey("maxScore"))
			maxScore = ((Integer) h.get("maxScore")).intValue();
		this.maxScore = maxScore;
		
		
		//zetKijkNaActief(kijkNaActief);
		this.kijkNaActief = kijkNaActief;
		zetCheckBlokkenBouwsel(checkBlokkenBouwsel);
		zetCheckDrieAanzichten(checkDrieAanzichten);
	    zetCheckVoorZijAanzicht(checkVoorZijAanzicht);
	    zetCheckBovenVoorAanzicht(checkBovenVoorAanzicht);
	    zetCheckBovenZijAanzicht(checkBovenZijAanzicht);
		zetCheckBovenAanzicht(checkBovenAanzicht);		
		zetCheckVoorAanzicht(checkVoorAanzicht);		
		zetCheckRechtsAanzicht(checkRechtsAanzicht);		
		zetCheckAantalKubus(checkAantalKubus);
		
		if (kijkNaActief && !kijkNaPanel.isVisible())
		{	noSetBounds = true;
			kijkNaPanel.setVisible(true);
			
		}
		
		String state = null;
		
		if (h.containsKey("state")) 
			state = (String) h.get("state");
		
		if (state != null)
		{	
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
		
		String docentState = null;
		
		if (h.containsKey("docentState")) 
			docentState = (String) h.get("docentState");
		
		if (docentState == null)
			return;
			
		Object o = StringCodeObject.decodeStringToObject(docentState);
		boolean[][][][] booleanDocentKRs = (boolean[][][][]) o;
		
		for (int i = 0; i < booleanDocentKRs.length; i++)
	    {	docentKr = new KubusRooster(booleanDocentKRs[i], 1); //later uitbreiden naar meer kubusroosters
	    }
		
	    docentV.zetKubusRooster(docentKr);
	    docentV.zetAchtergrond(getBackground());
	    
	}
	
	public Hashtable getState()
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
		return score;
	}
	
	public int getScoreMax()
	{	return maxScore;
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
	{	
		if (v.isVisible())
			v.tekenOpnieuw();
		
		//aantalKLabel.setText(NabouwenAanzichten.rb.getString("aantalKLabel")+ kr.geefAantalK());
/*		
		if (aanzichten)
		{	vp.ra.tekenOpnieuw();
			vp.ba.tekenOpnieuw();
			vp.va.tekenOpnieuw();
		}
*/		
		if (docentV != null && docentV.isVisible())
			docentV.tekenOpnieuw();
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

	public void zetDocentKubusRooster(KubusRooster k)
	{	docentKr = k;
		docentV.zetKubusRooster(docentKr);
		//vp.zetKubusRooster(kr);
		//volLeegKnop.setLabel(NabouwenAanzichten.rb.getString("volLeegKnopLabel1"));
		zetVeranderd();
		//na.setValue(kr.maxAantal);
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
		
		boolean voorZijAanzicht = false;
		if (h.containsKey("voorZijAanzicht"))
			voorZijAanzicht = ((Boolean) h.get("voorZijAanzicht")).booleanValue();
		zetVoorZijAanzicht(voorZijAanzicht);
		
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
		
		// nakijk opties
		
		boolean kijkNaActief = false;
		if (h.containsKey("kijkNaActief"))
			kijkNaActief = ((Boolean) h.get("kijkNaActief")).booleanValue();
		
		boolean checkBlokkenBouwsel = true;
		if (h.containsKey("checkBlokkenBouwsel"))
			checkBlokkenBouwsel = ((Boolean) h.get("checkBlokkenBouwsel")).booleanValue();

		boolean checkDrieAanzichten = false;
		if (h.containsKey("checkDrieAanzichten"))
			checkDrieAanzichten = ((Boolean) h.get("checkDrieAanzichten")).booleanValue();

	    boolean checkVoorZijAanzicht = false;
		if (h.containsKey("checkVoorZijAanzicht"))
			checkVoorZijAanzicht = ((Boolean) h.get("checkVoorZijAanzicht")).booleanValue();
	    
	    boolean checkBovenVoorAanzicht = false;
		if (h.containsKey("checkBovenVoorAanzicht"))
			checkBovenVoorAanzicht = ((Boolean) h.get("checkBovenVoorAanzicht")).booleanValue();
	    
	    boolean checkBovenZijAanzicht = false;
		if (h.containsKey("checkBovenZijAanzicht"))
			checkBovenZijAanzicht = ((Boolean) h.get("checkBovenZijAanzicht")).booleanValue();
		
		boolean checkBovenAanzicht = false;
		if (h.containsKey("checkBovenAanzicht"))
			checkBovenAanzicht = ((Boolean) h.get("checkBovenAanzicht")).booleanValue();
		
		boolean checkVoorAanzicht = false;
		if (h.containsKey("checkVoorAanzicht"))
			checkVoorAanzicht = ((Boolean) h.get("checkVoorAanzicht")).booleanValue();
				
		boolean checkRechtsAanzicht = false;
		if (h.containsKey("checkRechtsAanzicht"))
			checkRechtsAanzicht = ((Boolean) h.get("checkRechtsAanzicht")).booleanValue();
				
		boolean checkAantalKubus = false;
		if (h.containsKey("checkAantalKubus"))
			checkAantalKubus = ((Boolean) h.get("checkAantalKubus")).booleanValue();

		int maxScore = 10;
		if (h.containsKey("maxScore"))
			maxScore = ((Integer) h.get("maxScore")).intValue();
		this.maxScore = maxScore;
		
		//zetKijkNaActief(kijkNaActief);	
		this.kijkNaActief = kijkNaActief;
		zetCheckBlokkenBouwsel(checkBlokkenBouwsel);
		zetCheckDrieAanzichten(checkDrieAanzichten);
	    zetCheckVoorZijAanzicht(checkVoorZijAanzicht);
	    zetCheckBovenVoorAanzicht(checkBovenVoorAanzicht);
	    zetCheckBovenZijAanzicht(checkBovenZijAanzicht);
		zetCheckBovenAanzicht(checkBovenAanzicht);		
		zetCheckVoorAanzicht(checkVoorAanzicht);		
		zetCheckRechtsAanzicht(checkRechtsAanzicht);		
		zetCheckAantalKubus(checkAantalKubus);

		
		String state = null;
		if (h.containsKey("state")) 
			state = (String) h.get("state");
		
		if (state != null)
		{
			Object o = StringCodeObject.decodeStringToObject(state);
			boolean[][][][] booleanKRs = (boolean[][][][])o;
			for (int i = 0; i < booleanKRs.length; i++)
			{	kr = new KubusRooster(booleanKRs[i], 1); //later uitbreiden naar meer kubusroosters
			}
			v.zetKubusRooster(kr);
			vp.zetKubusRooster(kr);
			na.setValue(kr.maxAantal);
		}
		
		String docentState = null;
		
		if (h.containsKey("docentState")) 
			docentState = (String) h.get("docentState");
		
		if (docentState == null)
			return;
			
		Object o = StringCodeObject.decodeStringToObject(docentState);
		boolean[][][][] booleanDocentKRs = (boolean[][][][]) o;
		
		for (int i = 0; i < booleanDocentKRs.length; i++)
	    {	docentKr = new KubusRooster(booleanDocentKRs[i], 1); //later uitbreiden naar meer kubusroosters
	    }
		
	    docentV.zetKubusRooster(docentKr);
	    docentV.zetAchtergrond(getBackground());
		
	}
	
	public Hashtable getEditState()
	{	
		int aantalKR = 0;
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
	    h.put("voorZijAanzicht", new Boolean(voorZijAanzicht));
	    h.put("bovenAanzicht", new Boolean(bovenAanzicht));
	    h.put("voorAanzicht", new Boolean(voorAanzicht));
	    h.put("rechtsAanzicht", new Boolean(rechtsAanzicht));
	    
	    h.put("roosterGrootte", new Integer(v.kr.maxAantal));
	    
	    h.put("kijkNaActief", new Boolean(kijkNaActief));
	    
	    h.put("checkBlokkenBouwsel", new Boolean(checkBlokkenBouwsel));
	    h.put("checkDrieAanzichten", new Boolean(checkDrieAanzichten));
	    h.put("checkVoorZijAanzicht", new Boolean(checkVoorZijAanzicht));
	    h.put("checkBovenVoorAanzicht", new Boolean(checkBovenVoorAanzicht));
	    h.put("checkBovenZijAanzicht", new Boolean(checkBovenZijAanzicht));
	    h.put("checkBovenAanzicht", new Boolean(checkBovenAanzicht));
	    h.put("checkVoorAanzicht", new Boolean(checkVoorAanzicht));
	    h.put("checkRechtsAanzicht", new Boolean(checkRechtsAanzicht));
	    
	    h.put("checkAantalKubus", new Boolean(checkAantalKubus));	    
	    
	    h.put("maxScore", new Integer(maxScore));
	    
		int aantalDocentKR = 0;
		boolean[][][][] booleanDocentKRs = null;
		
		aantalDocentKR = 1;
		booleanDocentKRs = new boolean[aantalDocentKR][][][];
		for (int i = 0; i < aantalDocentKR; i++)
	    {	booleanDocentKRs[i] = docentKr.geefBooleanRooster(); //later uitbreiden naar meer kubusroosters
	    }
	
	    String docentState = StringCodeObject.encodeObjectToString(booleanDocentKRs);
	    
	    h.put("docentState", docentState);
	    
	    return h;
	}
	
	public InteractieEditPanel getEditPanel(){return new NabouwenAanzichtenInteractieEditPanel();}
		
	public void wis(){}
	
	public int geefAsHoogte(){return 0;}
	
	public boolean isCorrect()
	{	return score == maxScore;
	}
	
	public boolean isFout()
	{	return score == 0;
	}
	
	public void zetMode(int mode){}
	
	public void zetNagekeken(boolean b){}
	
    public void stop(){}
    
    public void destroy(){}
    
    public void zetMaat(){}
	
    public void start(){}
     
    public void opnieuw(){}
    
    public void kijkNa()
    {	
    	// niet nakijken
    	if (!kijkNaActief)
    		return;
    
        if (checkBlokkenBouwsel)
        {	// nog niet goed
        	if (!kr.isGelijk(docentKr))
        		score = 0;
        	else
        		score = maxScore;
        }
        else if (checkDrieAanzichten)
        {	// nog niet goed
        	if (!kr.isGelijkAanzichten(docentKr))
        		score = 0;
        	else
        	{	if (checkAantalKubus)
        		{	score = Math.max(maxScore / 2, maxScore - Math.abs(kr.aantalKubussen - docentKr.aantalKubussen));
        		}
        		else
        		{	score = maxScore;
        		}
        	}
        	
        }
        else if (checkVoorZijAanzicht)
        {	// nog niet goed
        	if (!kr.isGelijkVoorEnRechtsAanzicht(docentKr))
        		score = 0;
        	else
        	{	if (checkAantalKubus)
        		{	score = Math.max(maxScore / 2, maxScore - Math.abs(kr.aantalKubussen - docentKr.aantalKubussen));
        		}
        		else
        		{	score = maxScore;
        		}
        	}
        	
        }
        else if (checkBovenVoorAanzicht)
        {	// nog niet goed
        	if (!kr.isGelijkBovenEnVoorAanzicht(docentKr))
        		score = 0;
        	else
        	{	if (checkAantalKubus)
        		{	score = Math.max(maxScore / 2, maxScore - Math.abs(kr.aantalKubussen - docentKr.aantalKubussen));
        		}
        		else
        		{	score = maxScore;
        		}
        	}
        	
        }	
        else if (checkBovenZijAanzicht)
        {	// nog niet goed
        	if (!kr.isGelijkBovenEnRechtsAanzicht(docentKr))
        		score = 0;
        	else
        	{	if (checkAantalKubus)
        		{	score = Math.max(maxScore / 2, maxScore - Math.abs(kr.aantalKubussen - docentKr.aantalKubussen));
        		}
        		else
        		{	score = maxScore;
        		}
        	}
        	
        }	
        else if (checkBovenAanzicht)
        {	// nog niet goed
        	if (!kr.isGelijkBovenAanzicht(docentKr))
        		score = 0;
        	else
        	{	if (checkAantalKubus)
        		{	score = Math.max(maxScore / 2, maxScore - Math.abs(kr.aantalKubussen - docentKr.aantalKubussen));
        		}
        		else
        		{	score = maxScore;
        		}
        	}
        	
        }	
        else if (checkVoorAanzicht)
        {	// nog niet goed
        	if (!kr.isGelijkVoorAanzicht(docentKr))
        		score = 0;
        	else
        	{	if (checkAantalKubus)
        		{	score = Math.max(maxScore / 2, maxScore - Math.abs(kr.aantalKubussen - docentKr.aantalKubussen));
        		}
        		else
        		{	score = maxScore;
        		}
        	}
        	
        }	
        else if (checkRechtsAanzicht)
        {	// nog niet goed
        	if (!kr.isGelijkRechtsAanzicht(docentKr))
        		score = 0;
        	else
        	{	if (checkAantalKubus)
        		{	score = Math.max(maxScore / 2, maxScore - Math.abs(kr.aantalKubussen - docentKr.aantalKubussen));
        		}
        		else
        		{	score = maxScore;
        		}
        	}
        	
        }	
        
        if (score == 0)
        {	kruisjeLabel.setVisible(true);
        	geelVinkjeLabel.setVisible(false);
        	groenVinkjeLabel.setVisible(false);
        }
        else if (score < maxScore)
        {	kruisjeLabel.setVisible(false);
        	geelVinkjeLabel.setVisible(true);
        	groenVinkjeLabel.setVisible(true);
        }
        else // score==maxScore
        {	kruisjeLabel.setVisible(false);
        	geelVinkjeLabel.setVisible(false);
        	groenVinkjeLabel.setVisible(true);
        }
		
//System.out.println("score = " + score);		
		//fire actionEvent
		ActionEvent event = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "changed");
		for (int lCnt = 0; lCnt < listeners.size(); lCnt++)
		{
			((ActionListener) listeners.elementAt(lCnt)).actionPerformed(event);
		}
    
    	
    }
    
    public void kijkNa(int stapNr){}
    

	class KijkNaAL implements ActionListener
	{	public void actionPerformed(ActionEvent e)
		{
			kijkNa();
		}
		
	}
    
    public void addActionListener(ActionListener al)
    {	listeners.addElement(al);
    }
	
}