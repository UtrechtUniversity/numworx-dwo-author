package fi.nabouwenaanzichten;

import java.applet.Applet;
import java.awt.event.*;
import java.awt.*;
import java.util.*;
import java.util.List;

import javax.swing.*;

import org.cbook.cbookif.CBookEvent;
import org.cbook.cbookif.CBookEventHandler;
import org.cbook.cbookif.CBookEventListener;

import fi.beans.scorm.*;
import fi.beans.copyright.*;
import fi.beans.base64code.*;
import fi.beans.appletutil.*;
import fi.beans.wiskopdrbeans.CBookAware;
import fi.beans.wiskopdrbeans.InteractieEditPanel;
import fi.beans.wiskopdrbeans.InteractiePanel;


/**
 * @author Peter Boon
 */

public class NabouwenAanzichtenInteractiePanel extends JPanel 
	   implements InteractiePanel, InteractieEditPanel, NabouwenAanzichtenIF, NumberListener, ActionListener, CBookAware
{	
	protected SCORM12APIInterface api;
	
	private FIButton fiButton;
	protected static ResourceBundle rb;
	private String langArg;
	private String bgColorArg;
//	private JButton volLeegKnop, aanzichtenKnop;
	Viewer3d v;
	Viewer3d docentV;
	private VaktekPanel vp;
	private KubusRooster kr;
	KubusRooster docentKr;
	private NumberArrow na;
//	private JLabel aantalKLabel;
	private boolean aanzichten;
	InvulKeuzePanel2 ip;
	
	KnoppenPanel kPanel;
	
	private Color bgcolor = Color.white;
    private boolean mobileVersion;
    
    boolean noSetBounds = false;
    boolean newViewer = false;
    boolean kCorrected = false;
	
	boolean rotatieVast = false;
	double beginHoekX = 30;
	double beginHoekY = -30;
	boolean nietBouwenSlopen = false;
	boolean keuzeBouwenSlopen = false;
	boolean perspectief = true;
	boolean volLeegOptie = false;
	boolean aantalBlokjes = false;
	
	boolean pijlAan = true;
	boolean balkAan = false;
	boolean bovenAanzichtMetHoogtes = false;
	boolean maakAanzicht = false;
	
    boolean blokkenBouwsel = true;
    boolean silhouet = false;
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
    boolean kIsVisible = false;
    
    boolean kijkNaActiefKlein = false;
    
    int score = 0;
    int scoreMax = 10;
    
    Vector listeners = new Vector();
    
	JButton kijkNaButton;
//	JPanel kijkNaPanel;
	JLabel groenVinkjeLabel;
	JLabel geelVinkjeLabel;
	JLabel kruisjeLabel;
	
	private boolean ingevuld;
	private boolean nagekeken;
	private int mode;
	
	NabouwenAanzichtenInteractieEditPanel naiep;
	
	private CBookEventHandler cbookEventHandler = new CBookEventHandler(this);	
	
	public NabouwenAanzichtenInteractiePanel()
	{	setLayout(null);
		super.setBounds(0, 0, 300, 300);
		
		setBackground(new Color(230, 240, 255));
		
		kr = new KubusRooster(4, 1);
		docentKr = new KubusRooster(4, 1);
		
		aanzichten = true;

		kPanel = new KnoppenPanel(0,300,300,24);
		kPanel.setBackground(bgcolor);
		kPanel.setOpaque(false);
		kPanel.setVisible(false);
		add(kPanel);
		
		// checkboxen bouwen/slopen
		ip = new InvulKeuzePanel2(0,300,300,24);
		ip.setBackground(bgcolor);
		ip.setOpaque(false);
        ip.setVisible(false);
		add(ip);

		// knop maak vol/maak leeg
		kPanel.volLeegKnop = new JButton(NabouwenAanzichten.rb.getString("volLeegKnopLabel1"));
		kPanel.volLeegKnop.setFont(new Font("SansSerif",Font.PLAIN, 12));
		kPanel.volLeegKnop.addActionListener(this);
		kPanel.volLeegKnop.setBounds(0, 0, 90, 24);
		kPanel.volLeegKnop.setVisible(false);
		kPanel.add(kPanel.volLeegKnop);
/*        
        if(mobileVersion) 
        {  volLeegKnop.setFont(new Font("SansSerif", Font.PLAIN, 10));
           volLeegKnop.setBounds(145,180,55,15);
        }
		//add(volLeegKnop);
*/		
		
        // label aantal kubusjes
		kPanel.aantalKLabel = new JLabel("" + kr.geefAantalK() + " " + NabouwenAanzichten.rb.getString("blokjesTekst"));
		kPanel.aantalKLabel.setBounds(200,0,90,24);
		kPanel.aantalKLabel.setFont(new Font("SansSerif",Font.PLAIN,14));
//kPanel.aantalKLabel.setOpaque(true);
//kPanel.aantalKLabel.setBackground(Color.green);
		kPanel.aantalKLabel.setVisible(false);
		kPanel.add(kPanel.aantalKLabel);
		
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
		
		kijkNaButton = new JButton(NabouwenAanzichten.rb.getString("kijkNaTekst"));
		kijkNaButton.setFont(new Font("SansSerif",Font.PLAIN, 12));
		kijkNaButton.setBounds(0, 0, 75, 24);
		kijkNaButton.addActionListener(new KijkNaAL());
		
		java.net.URL imageURL = NabouwenAanzichten.class.getResource("resources/goedkrul_en.gif");
		if (imageURL != null)
		{
		    groenVinkjeLabel = new JLabel(new ImageIcon(imageURL));
		}
		else 
		{
			System.out.println("Error reading goedkrul_en.gif.");
			groenVinkjeLabel = new JLabel();
		}
		groenVinkjeLabel.setBounds(76, 2, 20, 20);
		
		imageURL = NabouwenAanzichten.class.getResource("resources/goedkrulhalf.gif");
		if (imageURL != null) 
		{
		    geelVinkjeLabel = new JLabel(new ImageIcon(imageURL));
		}
		else 
		{
			System.out.println("Error reading goedkrulhalf.gif.");
			geelVinkjeLabel = new JLabel();
		}
		geelVinkjeLabel.setBounds(76, 2, 20, 20);
		
		
		imageURL = NabouwenAanzichten.class.getResource("resources/foutkruis.gif");
		if (imageURL != null) 
		{
		    kruisjeLabel = new JLabel(new ImageIcon(imageURL));
		}
		else 
		{
			System.out.println("Error reading foutkruis.gif.");
			kruisjeLabel = new JLabel();
		}
		kruisjeLabel.setBounds(76, 2, 20, 20);
		
		groenVinkjeLabel.setVisible(false);
		geelVinkjeLabel.setVisible(false);
		kruisjeLabel.setVisible(false);
		
		kPanel.kijkNaPanel = new JPanel(null);
		kPanel.kijkNaPanel.setOpaque(false);
		kPanel.kijkNaPanel.setBounds(90, 0, 95, 24);
		kPanel.kijkNaPanel.add(kijkNaButton);
		kPanel.kijkNaPanel.add(groenVinkjeLabel);
		kPanel.kijkNaPanel.add(geelVinkjeLabel);
		kPanel.kijkNaPanel.add(kruisjeLabel);
		kPanel.kijkNaPanel.setVisible(false);
		
		kPanel.add(kPanel.kijkNaPanel);
		
	}
	
	public void setBounds(int x, int y, int b, int h)
	{	
		
		if ((x == getLocation().x) && (y == getLocation().y) &&
			(b == getSize().width) && (h == getSize().height))
		{	
System.out.println("naip setBounds return");			
			return;
		}	
		
		if (noSetBounds)
		{	noSetBounds = false;	
System.out.println("naip noSetBounds");			
			return;
		}
		
		if (v != null)
		{	remove(v);
		}
/*
		int vpZijde = Math.min(b, h - 24 - 24);
		int vpX = (b - vpZijde) / 2;
		int vpY = (h - 24 - 24 - vpZijde) / 2;
*/		

		int vpZijde = Math.min(b, h);
		int vpX = (b - vpZijde) / 2;
		int vpY = (h - vpZijde) / 2;
		
//System.out.println("create vpX = " + vpX + " vpY = " + vpY);
//System.out.println("create vpZijde = " + vpZijde);
		
        v = new Viewer3d(kr, vpX, vpY, vpZijde, vpZijde, this);
        v.zetAchtergrond(bgcolor);
	    v.zetBeginHoeken(beginHoekX, beginHoekY);
		add(v);

		newViewer = true;
		kCorrected = false;
		
		ip.setBounds(vpX, vpY + vpZijde, vpZijde, 24);

		kPanel.setBounds(vpX, vpY + vpZijde + 24, vpZijde, 24);
		
		//kPanel.kijkNaPanel.setLocation((kPanel.getSize().width - kPanel.kijkNaPanel.getSize().width) / 2, 0);
		
		if (vp != null)
		{	remove(vp);
		}
		vp = new VaktekPanel(kr, 0, 0, b, h, 3, this);
		vp.zetAchtergrond(bgcolor);
		vp.setVisible(false);
		add(vp);
		
		if (docentV != null)
		{	remove(docentV);
		}

		int docentVpZijde = Math.min(b, h - 24);
        int docentVpX = (b - docentVpZijde) / 2;
        int docentVpY = (h - 24 - docentVpZijde) / 2;
		docentV = new Viewer3d(docentKr, docentVpX, docentVpY, docentVpZijde, docentVpZijde, this);
		docentV.zetAchtergrond(bgcolor);
		docentV.zetBeginHoeken(beginHoekX, beginHoekY);
		docentV.setVisible(false);
		add(docentV, 0);

		
		super.setBounds(x, y, Math.min(600, b), h);
	
		zetPerspectief(perspectief);
		zetRotatieVast(rotatieVast);
		zetNietBouwenSlopen(nietBouwenSlopen);

		zetKeuzeBouwenSlopen(keuzeBouwenSlopen);

//System.out.println("kbs vpX = " + v.getLocation().x + " vpY = " + v.getLocation().y);
//System.out.println("kbs vpZijde = " + v.getSize().width);
		
		zetVolLeegOptie(volLeegOptie);
		
//System.out.println("vlo vpX = " + v.getLocation().x + " vpY = " + v.getLocation().y);
//System.out.println("vlo vpZijde = " + v.getSize().width);
		
		zetAantalBlokjes(aantalBlokjes);
		
//System.out.println("abl vpX = " + v.getLocation().x + " vpY = " + v.getLocation().y);
//System.out.println("abl vpZijde = " + v.getSize().width);
		
		zetPijlAan(pijlAan);
		zetBalkAan(balkAan);

//System.out.println("pb vpX = " + v.getLocation().x + " vpY = " + v.getLocation().y);
//System.out.println("pb vpZijde = " + v.getSize().width);
		
		zetBovenAanzichtMetHoogtes(bovenAanzichtMetHoogtes);
		zetMaakAanzicht(maakAanzicht);

//System.out.println("bah vpX = " + v.getLocation().x + " vpY = " + v.getLocation().y);
//System.out.println("bah vpZijde = " + v.getSize().width);
		
		zetBlokkenBouwsel(blokkenBouwsel);
		zetSilhouet(silhouet);
		zetDrieAanzichten(drieAanzichten);
		zetVoorZijAanzicht(voorZijAanzicht);
		zetBovenAanzicht(bovenAanzicht);
		zetVoorAanzicht(voorAanzicht);
		zetRechtsAanzicht(rechtsAanzicht);

//System.out.println("rec vpX = " + v.getLocation().x + " vpY = " + v.getLocation().y);
//System.out.println("rec vpZijde = " + v.getSize().width);
		
		int tabIndex = 0;
		if ((naiep != null) && (naiep.tabbedPane != null))
			tabIndex = naiep.tabbedPane.getSelectedIndex();
		
//		if (kijkNaActiefKlein)
//		{	kijkNaActiefKlein = false;
//		}
//		else 
		if (kijkNaActief && !kPanel.isVisible() && v.isVisible() && (tabIndex == 0))
		{	noSetBounds = true;
			kPanel.setVisible(true);
			
//HIER
			
//System.out.println("kijkNa setVis true");			
			
		}
		else if (kijkNaActief && (tabIndex == 1))
		{
			toonDocentViewer(true);
		}
/*		
		else if (kijkNaActief && kijkNaPanel.isVisible() && docentV.isVisible())
		{	noSetBounds = true;
			kijkNaPanel.setVisible(false);
System.out.println("kijkNa setVis false");			
			
		}
*/		
System.out.println("setBounds naip b = " + b + " h = " + h);

//System.out.println("final vpX = " + v.getLocation().x + " vpY = " + v.getLocation().y);
//System.out.println("final vpZijde = " + v.getSize().width);

		
		newViewer = false;
	}
	
	public void zetBreedte(int b)
	{
		//setBounds(getLocation().x, getLocation().y, b, getSize().height);
	}
	
	public void zetHoogte(int h)
	{
		//setBounds(getLocation().x, getLocation().y, getSize().width, h);
	}
/*	
	public void paintComponent(Graphics g)
	{
		g.setColor(Color.red);
		g.drawRect(0, 0, getSize().width - 1, getSize().height - 1);
	}
*/	
	
	public void setBackground(Color color)
	{	bgcolor = color;
		if (v != null)
			v.zetAchtergrond(color);
		if (vp != null)
			vp.zetAchtergrond(color);
		if (ip != null)
			ip.setBackground(color);
		if (kPanel != null)
			kPanel.setBackground(color);
		
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
		
		if (keuzeBouwenSlopen)
		{	
			if (!ip.isVisible())
			{	

				int vSpace = getSize().height - v.getSize().height - 24;
				if (kPanel.isVisible())
					vSpace -= 24;
				if (vSpace > 0)
				{	
					noSetBounds = true;
					v.setBounds(v.getLocation().x, vSpace / 2, v.getSize().width, v.getSize().height);
				}
				else
				{	noSetBounds = true;
					v.setBounds(v.getLocation().x + 12, v.getLocation().y, v.getSize().width - 24, v.getSize().height - 24);
				}
					
				v.zetGetalRooster2(bovenAanzichtMetHoogtes);
				v.zetHoogtes();
				
				noSetBounds = true;
				ip.setBounds(v.getLocation().x, v.getLocation().y + v.getSize().height, v.getSize().width, 24);
			
				noSetBounds = true;
				ip.setVisible(true);
				
				if (kPanel.isVisible())
					kPanel.setBounds(v.getLocation().x, ip.getLocation().y + 24, v.getSize().width, 24);
				
				
			}
			else // ip.isVisible()
			{
				if (newViewer)
				{	
					int vSpace = getSize().height - v.getSize().height - 24;
					if (kPanel.isVisible())
						vSpace -= 24;
					if (vSpace > 0)
					{	
						noSetBounds = true;
						v.setBounds(v.getLocation().x, vSpace / 2, v.getSize().width, v.getSize().height);
					}
					else
					{	noSetBounds = true;
						v.setBounds(v.getLocation().x + 12, v.getLocation().y, v.getSize().width - 24, v.getSize().height - 24);
					}
					
				}
				
				noSetBounds = true;
				ip.setBounds(v.getLocation().x, v.getLocation().y + v.getSize().height, v.getSize().width, 24);

				if (kPanel.isVisible())
					kPanel.setBounds(v.getLocation().x, ip.getLocation().y + 24, v.getSize().width, 24);
				
			}
		}
		else
		{	if (ip.isVisible())
			{	
				int hSpace = getSize().width - v.getSize().width;
				int vSpace = getSize().height - v.getSize().height + 24;
				if (kPanel.isVisible())
					vSpace -= 24;
				if (hSpace > 24)
				{	
					noSetBounds = true;
					v.setBounds(v.getLocation().x - 12, v.getLocation().y, v.getSize().width + 24, v.getSize().height + 24);
				}
				else
				{
					//noSetBounds = true;
					//v.setBounds(v.getLocation().x - 12, v.getLocation().y, v.getSize().width + 24, v.getSize().height + 24);
				}
				v.zetGetalRooster2(bovenAanzichtMetHoogtes);
				v.zetHoogtes();
			
				noSetBounds = true;
				ip.setVisible(false);
				
				if (kPanel.isVisible())
					kPanel.setBounds(v.getLocation().x, v.getLocation().y + v.getSize().height, v.getSize().width, 24);
			}
		}
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
	
	public void zetVolLeegOptie(boolean b)
	{	volLeegOptie = b;
		
		noSetBounds = true; 
		kPanel.volLeegKnop.setVisible(volLeegOptie);
		kPanel.layoutComponents();
		
		boolean showKnoppenPanel = volLeegOptie || kijkNaActief || aantalBlokjes;
		if (showKnoppenPanel)// && !kPanel.isVisible())
		{
			
			if (!kPanel.isVisible() || (newViewer && !kCorrected))
			{
				int vSpace = getSize().height - v.getSize().height - 24;
				if (ip.isVisible())
					vSpace -= 24;
				if (vSpace > 0)
				{	noSetBounds = true;
					v.setBounds(v.getLocation().x, vSpace / 2, v.getSize().width, v.getSize().height);
				}
				else
				{	noSetBounds = true;
					v.setBounds(v.getLocation().x + 12, v.getLocation().y, v.getSize().width - 24, v.getSize().height - 24);
				}	
				v.zetGetalRooster2(bovenAanzichtMetHoogtes);
				v.zetHoogtes();
				
			}
			
			if (ip.isVisible())
			{	
				noSetBounds = true;
				ip.setBounds(v.getLocation().x, v.getLocation().y + v.getSize().height, v.getSize().width, 24);
				
				noSetBounds = true;
				kPanel.setBounds(v.getLocation().x, ip.getLocation().y + ip.getSize().height, v.getSize().width, 24);
			}
			else
			{	
				noSetBounds = true;
				kPanel.setBounds(v.getLocation().x, v.getLocation().y + v.getSize().height, v.getSize().width, 24);
			}
			
			if (!kPanel.isVisible())
			{	
				noSetBounds = true;
				kPanel.setVisible(true);
			}
			else
			{
				kCorrected = true;
			}
		}
		
		if (!showKnoppenPanel && kPanel.isVisible())
		{	

			int hSpace = getSize().width - v.getSize().width;
			int vSpace = getSize().height - v.getSize().height + 24;
			if (ip.isVisible())
				vSpace -= 24;
			if (hSpace > 24)
			{	
				noSetBounds = true;
				v.setBounds(v.getLocation().x - 12, v.getLocation().y, v.getSize().width + 24, v.getSize().height + 24);
			}
			else
			{
				//noSetBounds = true;
				//v.setBounds(v.getLocation().x - 12, v.getLocation().y, v.getSize().width + 24, v.getSize().height + 24);
			}
			v.zetGetalRooster2(bovenAanzichtMetHoogtes);
			v.zetHoogtes();
			
			noSetBounds = true;
			kPanel.setVisible(false);
			
			if (ip.isVisible())
			{	
				noSetBounds = true;
				ip.setBounds(v.getLocation().x, v.getLocation().y + v.getSize().height, v.getSize().width, 24);
			}
			
			
		}
	
	}
	
	public void zetAantalBlokjes(boolean b)
	{	aantalBlokjes = b;
	
		noSetBounds = true;
		kPanel.aantalKLabel.setVisible(aantalBlokjes);
		kPanel.layoutComponents();
		boolean showKnoppenPanel = volLeegOptie || kijkNaActief || aantalBlokjes;
		if (showKnoppenPanel)// && !kPanel.isVisible())
		{	
			if (!kPanel.isVisible() || (newViewer && !kCorrected))
			{
				int vSpace = getSize().height - v.getSize().height - 24;
				if (ip.isVisible())
					vSpace -= 24;
				if (vSpace > 0)
				{	noSetBounds = true;
					v.setBounds(v.getLocation().x, vSpace / 2, v.getSize().width, v.getSize().height);
				}
				else
				{	noSetBounds = true;
					v.setBounds(v.getLocation().x + 12, v.getLocation().y, v.getSize().width - 24, v.getSize().height - 24);
				}	
				v.zetGetalRooster2(bovenAanzichtMetHoogtes);
				v.zetHoogtes();
				
			}
			
			if (ip.isVisible())
			{	
				noSetBounds = true;
				ip.setBounds(v.getLocation().x, v.getLocation().y + v.getSize().height, v.getSize().width, 24);
				
				noSetBounds = true;
				kPanel.setBounds(v.getLocation().x, ip.getLocation().y + ip.getSize().height, v.getSize().width, 24);
			}
			else
			{	
				noSetBounds = true;
				kPanel.setBounds(v.getLocation().x, v.getLocation().y + v.getSize().height, v.getSize().width, 24);
			}
			if (!kPanel.isVisible())
			{	
				noSetBounds = true;
				kPanel.setVisible(true);
			}
			else
			{
				kCorrected = true;
			}
		}
		
		if (!showKnoppenPanel && kPanel.isVisible())
		{	

			int hSpace = getSize().width - v.getSize().width;
			int vSpace = getSize().height - v.getSize().height + 24;
			if (ip.isVisible())
				vSpace -= 24;
			if (hSpace > 24)
			{	
				noSetBounds = true;
				v.setBounds(v.getLocation().x - 12, v.getLocation().y, v.getSize().width + 24, v.getSize().height + 24);
			}
			else
			{
				//noSetBounds = true;
				//v.setBounds(v.getLocation().x - 12, v.getLocation().y, v.getSize().width + 24, v.getSize().height + 24);
			}
			v.zetGetalRooster2(bovenAanzichtMetHoogtes);
			v.zetHoogtes();
			
			noSetBounds = true;
			kPanel.setVisible(false);
			
			if (ip.isVisible())
			{	
				noSetBounds = true;
				ip.setBounds(v.getLocation().x, v.getLocation().y + v.getSize().height, v.getSize().width, 24);
			}
			
			
		}
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
			//zetSilhouet(false);
			kr.zetVulkleur("geel");
			//v.zetAfstand(1000000000);
			if (vp.isVisible())
			{	noSetBounds = true;
				vp.setVisible(false);
				noSetBounds = true;
				v.setVisible(true);
				if (keuzeBouwenSlopen)
				{	noSetBounds = true;
					ip.setVisible(true);
				}
				boolean showKnoppenPanel = volLeegOptie || kijkNaActief || aantalBlokjes;		
				if (showKnoppenPanel)
				{	noSetBounds = true;
					kPanel.setVisible(true);
				}			
				
			}	
			else if (v.isVisible() && silhouet && !newViewer)
			{
				//zetSilhouet(false);
				
				if (keuzeBouwenSlopen)
				{	
					int vSpace = getSize().height - v.getSize().height - 24;
					if (vSpace > 0)
					{	noSetBounds = true;
						v.setBounds(v.getLocation().x, vSpace / 2, v.getSize().width, v.getSize().height);
					}
					else
					{	noSetBounds = true;
						v.setBounds(v.getLocation().x + 12, v.getLocation().y, v.getSize().width - 24, v.getSize().height - 24);
					}	
					noSetBounds = true;
					ip.setBounds(v.getLocation().x, v.getLocation().y + v.getSize().height, v.getSize().width, 24);
					noSetBounds = true;
					ip.setVisible(true);
				}
				boolean showKnoppenPanel = volLeegOptie || kijkNaActief || aantalBlokjes;				
				if (showKnoppenPanel)
				{	
					int vSpace = getSize().height - v.getSize().height - 24;
					if (ip.isVisible())
						vSpace -= 24;
					if (vSpace > 0)
					{	noSetBounds = true;
						v.setBounds(v.getLocation().x, vSpace / 2, v.getSize().width, v.getSize().height);
					}
					else
					{	noSetBounds = true;
						v.setBounds(v.getLocation().x + 12, v.getLocation().y, v.getSize().width - 24, v.getSize().height - 24);
					}	
					if (ip.isVisible())
					{	noSetBounds = true;
						ip.setBounds(v.getLocation().x, v.getLocation().y + v.getSize().height, v.getSize().width, 24);
						noSetBounds = true;
						kPanel.setBounds(v.getLocation().x, ip.getLocation().y + 24, v.getSize().width, 24);
					}
					else
					{
						noSetBounds = true;
						kPanel.setBounds(v.getLocation().x, v.getLocation().y + v.getSize().height, v.getSize().width, 24);
					}	
					noSetBounds = true;
					kPanel.setVisible(true);
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
			newViewer = true;
			zetBlokkenBouwsel(blokkenBouwsel);
			newViewer = false; 
			zetSilhouet(silhouet);
			zetDrieAanzichten(drieAanzichten);
			zetVoorZijAanzicht(voorZijAanzicht);
			zetBovenAanzicht(bovenAanzicht);
			zetVoorAanzicht(voorAanzicht);
			zetRechtsAanzicht(rechtsAanzicht);

		}
		else
			v.zetHoogtes();
		v.tekenOpnieuw();
	}
	
	public void zetMaakAanzicht(boolean b)
	{	maakAanzicht = b;
		
		if (maakAanzicht)
		{	//zetSilhouet(false);
			kr.zetVulkleur("geel");
			//v.zetAfstand(1000000000);
			if (vp.isVisible())
			{	noSetBounds = true;
				vp.setVisible(false);
				noSetBounds = true;
				v.setVisible(true);
				if (keuzeBouwenSlopen)
				{	noSetBounds = true;
					ip.setVisible(true);
				}
				boolean showKnoppenPanel = volLeegOptie || kijkNaActief || aantalBlokjes;		
				if (showKnoppenPanel)
				{	noSetBounds = true;
					kPanel.setVisible(true);
				}			
				
			}	
			else if (v.isVisible() && silhouet && !newViewer)
			{
				//zetSilhouet(false);
				
				if (keuzeBouwenSlopen)
				{	
					int vSpace = getSize().height - v.getSize().height - 24;
					if (vSpace > 0)
					{	noSetBounds = true;
						v.setBounds(v.getLocation().x, vSpace / 2, v.getSize().width, v.getSize().height);
					}
					else
					{	noSetBounds = true;
						v.setBounds(v.getLocation().x + 12, v.getLocation().y, v.getSize().width - 24, v.getSize().height - 24);
					}	
					noSetBounds = true;
					ip.setBounds(v.getLocation().x, v.getLocation().y + v.getSize().height, v.getSize().width, 24);
					noSetBounds = true;
					ip.setVisible(true);
				}
				boolean showKnoppenPanel = volLeegOptie || kijkNaActief || aantalBlokjes;				
				if (showKnoppenPanel)
				{	
					int vSpace = getSize().height - v.getSize().height - 24;
					if (ip.isVisible())
						vSpace -= 24;
					if (vSpace > 0)
					{	noSetBounds = true;
						v.setBounds(v.getLocation().x, vSpace / 2, v.getSize().width, v.getSize().height);
					}
					else
					{	noSetBounds = true;
						v.setBounds(v.getLocation().x + 12, v.getLocation().y, v.getSize().width - 24, v.getSize().height - 24);
					}	
					if (ip.isVisible())
					{	noSetBounds = true;
						ip.setBounds(v.getLocation().x, v.getLocation().y + v.getSize().height, v.getSize().width, 24);
						noSetBounds = true;
						kPanel.setBounds(v.getLocation().x, ip.getLocation().y + 24, v.getSize().width, 24);
					}
					else
					{
						noSetBounds = true;
						kPanel.setBounds(v.getLocation().x, v.getLocation().y + v.getSize().height, v.getSize().width, 24);
					}	
					noSetBounds = true;
					kPanel.setVisible(true);
				}
				
			}
		}
	
		v.zetMaakAanzicht(maakAanzicht);
		docentV.zetMaakAanzicht(maakAanzicht);
		
		if (!maakAanzicht)
		{	v.zetSchaduw(true);
			
			if ((Math.abs(beginHoekX - 90) < NZERO) && (Math.abs(beginHoekY) < NZERO))
				zetBeginHoeken(30, -30);
			else	
				zetBeginHoeken(beginHoekX, beginHoekY);
			zetPerspectief(perspectief);
			zetRotatieVast(rotatieVast);
			newViewer = true;
			zetBlokkenBouwsel(blokkenBouwsel);
			newViewer = false; 
			zetSilhouet(silhouet);
			zetDrieAanzichten(drieAanzichten);
			zetVoorZijAanzicht(voorZijAanzicht);
			zetBovenAanzicht(bovenAanzicht);
			zetVoorAanzicht(voorAanzicht);
			zetRechtsAanzicht(rechtsAanzicht);

		}
		else
		if(bovenAanzichtMetHoogtes)	v.zetHoogtes();
		
		v.tekenOpnieuw();
	}
	
	public void zetBlokkenBouwsel(boolean b)
	{	blokkenBouwsel = b;
		if (blokkenBouwsel && !(bovenAanzichtMetHoogtes || maakAanzicht))
		{
			kr.zetVulkleur("geel");
			v.zetSchaduw(true);
			
			// laatste keuze was een of meer aanzichten
			// viewer en rest staan al op hun plaats
			if (vp.isVisible())
			{	noSetBounds = true;
				vp.setVisible(false);
				noSetBounds = true;
				v.setVisible(true);
				if (keuzeBouwenSlopen)
				{	noSetBounds = true;
					ip.setVisible(true);
				}
				boolean showKnoppenPanel = volLeegOptie || kijkNaActief || aantalBlokjes;				
				if (showKnoppenPanel)
				{	
					// kijk of er plaats is voor kPanel
					int plaats = getSize().height - v.getLocation().y - v.getSize().height;
					if (ip.isVisible())
						plaats -= 24;
					if (plaats < 24)
					{	int vSpace = getSize().height - v.getSize().height - 24;
						if (ip.isVisible())
							vSpace -= 24;
						if (vSpace > 0)
						{	noSetBounds = true;
							v.setBounds(v.getLocation().x, vSpace / 2, v.getSize().width, v.getSize().height);
						}
						else
						{	noSetBounds = true;
							v.setBounds(v.getLocation().x + 12, v.getLocation().y, v.getSize().width - 24, v.getSize().height - 24);
						}	
						if (ip.isVisible())
						{	noSetBounds = true;
							ip.setBounds(v.getLocation().x, v.getLocation().y + v.getSize().height, v.getSize().width, 24);
							noSetBounds = true;
							kPanel.setBounds(v.getLocation().x, ip.getLocation().y + 24, v.getSize().width, 24);
						}
						else
						{
							noSetBounds = true;
							kPanel.setBounds(v.getLocation().x, v.getLocation().y + v.getSize().height, v.getSize().width, 24);
						}	
						noSetBounds = true;
						kPanel.setVisible(true);
					}
					else
					{
						noSetBounds = true;
						kPanel.setVisible(true);
					}
				}
			}
			
			// laatste keuze was silhouet
			else if (v.isVisible() && !newViewer)
			{	
				if (keuzeBouwenSlopen)
				{	
					int vSpace = getSize().height - v.getSize().height - 24;
					if (vSpace > 0)
					{	noSetBounds = true;
						v.setBounds(v.getLocation().x, vSpace / 2, v.getSize().width, v.getSize().height);
					}
					else
					{	noSetBounds = true;
						v.setBounds(v.getLocation().x + 12, v.getLocation().y, v.getSize().width - 24, v.getSize().height - 24);
					}	
					noSetBounds = true;
					ip.setBounds(v.getLocation().x, v.getLocation().y + v.getSize().height, v.getSize().width, 24);
					noSetBounds = true;
					ip.setVisible(true);
				}
				boolean showKnoppenPanel = volLeegOptie || kijkNaActief || aantalBlokjes;				
				if (showKnoppenPanel)
				{	
					int vSpace = getSize().height - v.getSize().height - 24;
					if (ip.isVisible())
						vSpace -= 24;
					if (vSpace > 0)
					{	noSetBounds = true;
						v.setBounds(v.getLocation().x, vSpace / 2, v.getSize().width, v.getSize().height);
					}
					else
					{	noSetBounds = true;
						v.setBounds(v.getLocation().x + 12, v.getLocation().y, v.getSize().width - 24, v.getSize().height - 24);
					}	
					if (ip.isVisible())
					{	noSetBounds = true;
						ip.setBounds(v.getLocation().x, v.getLocation().y + v.getSize().height, v.getSize().width, 24);
						noSetBounds = true;
						kPanel.setBounds(v.getLocation().x, ip.getLocation().y + 24, v.getSize().width, 24);
					}
					else
					{
						noSetBounds = true;
						kPanel.setBounds(v.getLocation().x, v.getLocation().y + v.getSize().height, v.getSize().width, 24);
					}	
					noSetBounds = true;
					kPanel.setVisible(true);
				}
				
			}

			v.tekenOpnieuw();
			v.klikAan = !nietBouwenSlopen;
			
			// viewer kleiner maken voor ip en kPanel			
			
			silhouet = false;
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
	
	public void zetSilhouet(boolean b)
	{	silhouet = b;
		
		if (silhouet && !(bovenAanzichtMetHoogtes || maakAanzicht))
		{
			kr.zetVulkleur("zwart");
			v.klikAan = false;
			v.zetSchaduw(false);
			
			// laatste keuze was een of meer aanzichten
			if (vp.isVisible())
			{	noSetBounds = true;
				vp.setVisible(false);

				// viewer maximum afmeting
				int vpZijde = Math.min(getSize().width, getSize().height);
				int hSpace = getSize().width - vpZijde;
				int vSpace = getSize().height - vpZijde;
				
				v.setBounds(hSpace / 2, vSpace / 2, vpZijde, vpZijde);
/*				
				if (keuzeBouwenSlopen)
				{	noSetBounds = true;
					v.setBounds(v.getLocation().x - 12, v.getLocation().y, v.getSize().width + 24, v.getSize().height + 24);					
				}
				boolean showKnoppenPanel = volLeegOptie || kijkNaActief || aantalBlokjes;
				if (showKnoppenPanel)
				{	noSetBounds = true;
					v.setBounds(v.getLocation().x - 12, v.getLocation().y, v.getSize().width + 24, v.getSize().height + 24);					
				}
*/				
				
				noSetBounds = true;
				v.setVisible(true);
			}			
			
			// laatste keuze was blokkenbouwsel
			else if (v.isVisible())
			{
				if (ip.isVisible())
				{	noSetBounds = true;
					ip.setVisible(false);
//					noSetBounds = true;
//					v.setBounds(v.getLocation().x - 12, v.getLocation().y, v.getSize().width + 24, v.getSize().height + 24);					
				}
				
				if (kPanel.isVisible())
				{	noSetBounds = true;
					kPanel.setVisible(false);
//					noSetBounds = true;
//					v.setBounds(v.getLocation().x - 12, v.getLocation().y, v.getSize().width + 24, v.getSize().height + 24);					
				}
				
				// viewer maximum afmeting
				int vpZijde = Math.min(getSize().width, getSize().height);
				int hSpace = getSize().width - vpZijde;
				int vSpace = getSize().height - vpZijde;
				
				if (vpZijde != v.getSize().width)
					v.setBounds(hSpace / 2, vSpace / 2, vpZijde, vpZijde);
				
			}
			
//			v.klikAan = false;
//			v.zetSchaduw(false);
			v.tekenOpnieuw();
			
			blokkenBouwsel = false;
			drieAanzichten = false;
			voorZijAanzicht = false;
			bovenAanzicht = false;
			voorAanzicht = false;
			rechtsAanzicht = false;
			
		}
	}
	
	public void zetDrieAanzichten(boolean b)
	{	drieAanzichten = b;
		if (drieAanzichten && !(bovenAanzichtMetHoogtes || maakAanzicht))
		{	
			kr.zetVulkleur("geel");
			
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
				if (kPanel.isVisible())
				{	noSetBounds = true;
					kPanel.setVisible(false);
				}				
				
			}
			
			vp.zetDrieAanzichten();			
			
			blokkenBouwsel = false;
			silhouet = false;
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
		if (voorZijAanzicht && !(bovenAanzichtMetHoogtes || maakAanzicht))
		{	
			kr.zetVulkleur("geel");
			
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
				if (kPanel.isVisible())
				{	noSetBounds = true;
					kPanel.setVisible(false);
				}				
				
			}
		
			vp.zetVoorZijAanzicht();			
			
			blokkenBouwsel = false;
			silhouet = false;
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
		if (bovenAanzicht && !(bovenAanzichtMetHoogtes || maakAanzicht))
		{	
			kr.zetVulkleur("geel");
			
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
				if (kPanel.isVisible())
				{	noSetBounds = true;
					kPanel.setVisible(false);
				}				
				
			}
			
			vp.zetEenAanzicht(vp.BOVEN);
			
			blokkenBouwsel = false;
			silhouet = false;
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
		if (voorAanzicht && !(bovenAanzichtMetHoogtes || maakAanzicht))
		{	
			kr.zetVulkleur("geel");
			
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
				if (kPanel.isVisible())
				{	noSetBounds = true;
					kPanel.setVisible(false);
				}				
				
			}	
			
			vp.zetEenAanzicht(vp.VOOR);
			
			blokkenBouwsel = false;
			silhouet = false;
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
		if (rechtsAanzicht && !(bovenAanzichtMetHoogtes || maakAanzicht))
		{	
			kr.zetVulkleur("geel");
			
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
				if (kPanel.isVisible())
				{	noSetBounds = true;
					kPanel.setVisible(false);
				}				
				
			}
				
			vp.zetEenAanzicht(vp.RECHTS);				

			blokkenBouwsel = false;
			silhouet = false;
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
//				noSetBounds = true;
//				kPanel.setVisible(false);
			}
			else
				vIsVisible = false;
		
			if (kPanel.isVisible())
			{
				noSetBounds = true;
				kPanel.setVisible(false);
				kIsVisible = true;
			}
		
			if (vp.isVisible())
			{	noSetBounds = true;
				vp.setVisible(false);
				vpIsVisible = true;
			}
			else
				vpIsVisible = false;
	
			if (!ip.isVisible())
			{	
				//noSetBounds = true;
				//ip.setBounds(docentV.getLocation().x, docentV.getLocation().y + docentV.getSize().height, docentV.getSize().width, 24);
				noSetBounds = true;
				ip.setVisible(true);
				ipIsVisible = false;
			}
			else
				ipIsVisible = true;
			
			noSetBounds = true;
			ip.setBounds(docentV.getLocation().x, docentV.getLocation().y + docentV.getSize().height, docentV.getSize().width, 24);
			
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

			if (!ipIsVisible)
			{	noSetBounds = true;
				ip.setVisible(false);
			}
			
			if (kIsVisible && !vpIsVisible)
			{
				noSetBounds = true;
				kPanel.setVisible(true);
			}
		
			if (vIsVisible & !silhouet)
			{	//noSetBounds = true;
				//v.setVisible(true);
				noSetBounds = true;
				kPanel.kijkNaPanel.setVisible(kijkNaActief);
				kPanel.layoutComponents();
				
				boolean showKnoppenPanel = volLeegOptie || kijkNaActief || aantalBlokjes;
				if (showKnoppenPanel)// && !kPanel.isVisible())
				{	
					if (!kPanel.isVisible() || (newViewer && !kCorrected))
					{
						
//System.out.println("!kPanel.isVisible() || (newViewer && !kCorrected");						
						int vSpace = getSize().height - v.getSize().height - 24;
						if (ip.isVisible())
							vSpace -= 24;
					
						if (vSpace > 0)
						{	noSetBounds = true;
							v.setBounds(v.getLocation().x, vSpace / 2, v.getSize().width, v.getSize().height);
						}
						else
						{	noSetBounds = true;
							v.setBounds(v.getLocation().x + 12, v.getLocation().y, v.getSize().width - 24, v.getSize().height - 24);
						}	
						v.zetGetalRooster2(bovenAanzichtMetHoogtes);
						v.zetHoogtes();
						
					}
					
					if (ip.isVisible())
					{	
						noSetBounds = true;
						ip.setBounds(v.getLocation().x, v.getLocation().y + v.getSize().height, v.getSize().width, 24);
						
						noSetBounds = true;
						kPanel.setBounds(v.getLocation().x, ip.getLocation().y + ip.getSize().height, v.getSize().width, 24);
					}
					else
					{	
						noSetBounds = true;
						kPanel.setBounds(v.getLocation().x, v.getLocation().y + v.getSize().height, v.getSize().width, 24);
					}
					if (!kPanel.isVisible())
					{	
						noSetBounds = true;
						kPanel.setVisible(true);
						kIsVisible = true;
					}
					else
					{
						kCorrected = true;
					}
				}
				
				if (!showKnoppenPanel && kPanel.isVisible())
				{	

					int hSpace = getSize().width - v.getSize().width;
					int vSpace = getSize().height - v.getSize().height + 24;
					if (ip.isVisible())
						vSpace -= 24;
					if (hSpace > 24)
					{	
						noSetBounds = true;
						v.setBounds(v.getLocation().x - 12, v.getLocation().y, v.getSize().width + 24, v.getSize().height + 24);
					}
					else
					{
						//noSetBounds = true;
						//v.setBounds(v.getLocation().x - 12, v.getLocation().y, v.getSize().width + 24, v.getSize().height + 24);
					}
					v.zetGetalRooster2(bovenAanzichtMetHoogtes);
					v.zetHoogtes();
					
					noSetBounds = true;
					kPanel.setVisible(false);
					
					kIsVisible = false;
					
					if (ip.isVisible())
					{	
						noSetBounds = true;
						ip.setBounds(v.getLocation().x, v.getLocation().y + v.getSize().height, v.getSize().width, 24);
					}
					
					
				}
				
				noSetBounds = true;
				v.setVisible(true);
			}
			else if (vIsVisible && silhouet)
			{
				noSetBounds = true;
				v.setVisible(true);
			}

			if (vpIsVisible)
			{	
				kPanel.kijkNaPanel.setVisible(kijkNaActief);
				kPanel.layoutComponents();
				
				noSetBounds = true;
				vp.setVisible(true);
				
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
    {	scoreMax = ms;
    	
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
			
			
			if (silhouet &&  !(bovenAanzichtMetHoogtes || maakAanzicht))
			{
				kr.zetVulkleur("zwart"); 
			}
			/*cpfiw*/			
		} else if(h.containsKey("stateNew")) { // JSONArray from NabouwenAanzichtenGWT
			List rooster = (List) h.get("stateNew");
			boolean[][][] booleanKR = new boolean[rooster.size()][][];
			for (int i = 0; i < booleanKR.length; i++) {
				List roosteri = (List) rooster.get(i);
				boolean[][] booleanKRi = new boolean[roosteri.size()][];
				for (int j = 0; j < booleanKRi.length; j++) {
					List roosterij = (List) roosteri.get(j);
					boolean booleanKRij[] = new boolean[roosterij.size()];
					for (int k = 0; k < booleanKRij.length; k++) {
						booleanKRij[k] = Boolean.TRUE.equals(roosterij.get(k));
					}
					booleanKRi[j] = booleanKRij;					
				}
				booleanKR[i] = booleanKRi;				
			}
			{	kr = new KubusRooster(booleanKR, 1); //later uitbreiden naar meer kubusroosters
			}
			v.zetKubusRooster(kr);
			vp.zetKubusRooster(kr);
			na.setValue(kr.maxAantal);
			
			
			if (silhouet &&  !(bovenAanzichtMetHoogtes || maakAanzicht))
			{
				kr.zetVulkleur("zwart"); 
			}
			
			
			
		}
		
		
		
		if (h.containsKey("ingevuld")) 
			ingevuld = ((Boolean) h.get("ingevuld")).booleanValue();
	    if (h.containsKey("nagekeken")) 
	    	nagekeken = ((Boolean) h.get("nagekeken")).booleanValue();
	    if (ingevuld && (mode == 0 || nagekeken)) 
	    	kijkNa();
	    
	    
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
	    
	    //System.out.println("ingevuld: "+ingevuld);
	    //System.out.println("nagekeken: "+nagekeken);
	    
	}
	
	public void zetOpdracht(Hashtable h , String[] variables, Hashtable values)
	{
		
//System.out.println("zetOpdracht begin");	
//if (v!= null)
//System.out.println("vw = " + v.getSize().width);	
		
		
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

		boolean volLeegOptie = false;
		if (h.containsKey("volLeegOptie"))
			volLeegOptie = ((Boolean) h.get("volLeegOptie")).booleanValue();
		zetVolLeegOptie(volLeegOptie);
		
		boolean aantalBlokjes = false;
		if (h.containsKey("aantalBlokjes"))
			aantalBlokjes = ((Boolean) h.get("aantalBlokjes")).booleanValue();
		zetAantalBlokjes(aantalBlokjes);
		
		boolean pijlAan = true;
		if (h.containsKey("pijlAan"))
			pijlAan = ((Boolean) h.get("pijlAan")).booleanValue();
		zetPijlAan(pijlAan);
		
		boolean balkAan = false;
		if (h.containsKey("balkAan"))
			balkAan = ((Boolean) h.get("balkAan")).booleanValue();
		zetBalkAan(balkAan);
		
newViewer = true;

		boolean bovenAanzichtMetHoogtes = false;
		if (h.containsKey("bovenAanzichtMetHoogtes"))
			bovenAanzichtMetHoogtes = ((Boolean) h.get("bovenAanzichtMetHoogtes")).booleanValue();
		zetBovenAanzichtMetHoogtes(bovenAanzichtMetHoogtes);
		
		boolean maakAanzicht = false;
		if (h.containsKey("maakAanzicht"))
			maakAanzicht = ((Boolean) h.get("maakAanzicht")).booleanValue();
		zetMaakAanzicht(maakAanzicht);
		
		boolean blokkenBouwsel = true;
		if (h.containsKey("blokkenBouwsel"))
			blokkenBouwsel = ((Boolean) h.get("blokkenBouwsel")).booleanValue();
		zetBlokkenBouwsel(blokkenBouwsel);

		boolean silhouet = false;
		if (h.containsKey("silhouet"))
			silhouet = ((Boolean) h.get("silhouet")).booleanValue();
		zetSilhouet(silhouet);

newViewer = false;

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
		
		int scoreMax = 10;
		if (h.containsKey("scoreMax"))
			scoreMax = ((Integer) h.get("scoreMax")).intValue();
		this.scoreMax = scoreMax;
		
		
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
		
		if (kijkNaActief)
		{	noSetBounds = true;
			kPanel.kijkNaPanel.setVisible(true);
			kPanel.layoutComponents();
			if (!kPanel.isVisible())
			{
				int vSpace = getSize().height - v.getSize().height - 24;
				if (ip.isVisible())
					vSpace -= 24;
			
				if (vSpace > 0)
				{	noSetBounds = true;
					v.setBounds(v.getLocation().x, vSpace / 2, v.getSize().width, v.getSize().height);
				}
				else
				{	noSetBounds = true;
					v.setBounds(v.getLocation().x + 12, v.getLocation().y, v.getSize().width - 24, v.getSize().height - 24);
				}	
				v.zetGetalRooster2(bovenAanzichtMetHoogtes);
				v.zetHoogtes();

				if (ip.isVisible())
				{	
					noSetBounds = true;
					ip.setBounds(v.getLocation().x, v.getLocation().y + v.getSize().height, v.getSize().width, 24);
					
					noSetBounds = true;
					kPanel.setBounds(v.getLocation().x, ip.getLocation().y + ip.getSize().height, v.getSize().width, 24);
				}
				else
				{	
					noSetBounds = true;
					kPanel.setBounds(v.getLocation().x, v.getLocation().y + v.getSize().height, v.getSize().width, 24);
				}

				noSetBounds = true;
				kPanel.setVisible(true);

			}
		}
		
		String state = null;
		boolean[][][] stateNew = null;
		
		if (h.containsKey("state")) 
			state = (String) h.get("state");
		if (h.containsKey("stateNew"))
			stateNew = (boolean[][][]) h.get("stateNew");
		
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
			
			if (silhouet && !(bovenAanzichtMetHoogtes || maakAanzicht))
			{
				kr.zetVulkleur("zwart"); 
			}
		}
		else if (stateNew != null)
		{
			kr = new KubusRooster(stateNew, 1);
			v.zetKubusRooster(kr);
			vp.zetKubusRooster(kr);
			na.setValue(kr.maxAantal);
			v.zetAchtergrond(getBackground());
			
			if (silhouet && !(bovenAanzichtMetHoogtes || maakAanzicht))
			{
				kr.zetVulkleur("zwart"); 
			}
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
	    
//System.out.println("zetOpdracht einde");	
//if (v!= null)
//System.out.println("vw = " + v.getSize().width);

	    
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
	    
	    h.put("ingevuld", new Boolean(ingevuld));
        h.put("nagekeken", new Boolean(nagekeken));
        
        // Java-onafhankelijke codering
        boolean[][][] stateNew = null;
		stateNew = kr.geefBooleanRooster();
		h.put("stateNew", stateNew);
		
	    
	    return h;
	}
	
	public int getIpId()
	{	return 0;
	}
	
	public String getIpExpString()
	{	return null;
	}
	
	public int getScore()
	{	if (kijkNaActief)
			return score;
		return 0;
	}
	
	public int getScoreMax()
	{	return scoreMax;
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
		{	v.tekenOpnieuw();
		}
		
		kPanel.aantalKLabel.setText("" + kr.geefAantalK() + " " + NabouwenAanzichten.rb.getString("blokjesTekst"));
		ingevuld = kr.geefAantalK() != 0;
/*		
		if (aanzichten)
		{	vp.ra.tekenOpnieuw();
			vp.ba.tekenOpnieuw();
			vp.va.tekenOpnieuw();
		}
*/		
		if (docentV != null && docentV.isVisible())
			docentV.tekenOpnieuw();
		
		boolean[][][]  booleanKR = kr.geefBooleanRooster();
		Map<String,Object> map1 = new HashMap<String,Object>();
		map1.put("booleanKR", booleanKR);
		cbookEventHandler.fire("blockBuilding",map1);
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
		kPanel.volLeegKnop.setText(NabouwenAanzichten.rb.getString("volLeegKnopLabel1"));
		kPanel.aantalKLabel.setText("" + kr.geefAantalK() + " " + NabouwenAanzichten.rb.getString("blokjesTekst"));
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
		kPanel.volLeegKnop.setText(NabouwenAanzichten.rb.getString("volLeegKnopLabel1"));
		zetVeranderd();

	}
	
	public void actionPerformed(ActionEvent e)
	{	if(e.getSource() == kPanel.volLeegKnop)
		{	kPanel.volLeegKnop.transferFocus(); 
			if (kPanel.volLeegKnop.getText().equals(NabouwenAanzichten.rb.getString("volLeegKnopLabel1")))
			{	kr.maakVol();
				kPanel.volLeegKnop.setText(NabouwenAanzichten.rb.getString("volLeegKnopLabel2"));
				if(bovenAanzichtMetHoogtes)
					v.zetHoogtes();
				zetVeranderd();
			}
			else
			{	kr.maakLeeg();
				kPanel.volLeegKnop.setText(NabouwenAanzichten.rb.getString("volLeegKnopLabel1"));
				if(bovenAanzichtMetHoogtes)
					v.wisHoogtes();
				zetVeranderd();
			}
		}
/*	
		if(e.getSource()==aanzichtenKnop)
		{	aanzichtenKnop.transferFocus(); 
			if(aanzichtenKnop.getText().equals(NabouwenAanzichten.rb.getString("aanzichtenKnopLabel1")))
			{	vp.setVisible(true);
				aanzichten = true;
				aanzichtenKnop.setText(NabouwenAanzichten.rb.getString("aanzichtenKnopLabel2"));
				zetVeranderd();
			}
			else
			{	vp.setVisible(false);
				aanzichten = false;
				aanzichtenKnop.setText(NabouwenAanzichten.rb.getString("aanzichtenKnopLabel1"));
				zetVeranderd();
			}
		}
*/		
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
		
		boolean volLeegOptie = false;
		if (h.containsKey("volLeegOptie"))
			volLeegOptie = ((Boolean) h.get("volLeegOptie")).booleanValue();
		zetVolLeegOptie(volLeegOptie);
		
		boolean aantalBlokjes = false;
		if (h.containsKey("aantalBlokjes"))
			aantalBlokjes = ((Boolean) h.get("aantalBlokjes")).booleanValue();
		zetAantalBlokjes(aantalBlokjes);
			
		boolean pijlAan = true;
		if (h.containsKey("pijlAan"))
			pijlAan = ((Boolean) h.get("pijlAan")).booleanValue();
		zetPijlAan(pijlAan);
		
		boolean balkAan = false;
		if (h.containsKey("balkAan"))
			balkAan = ((Boolean) h.get("balkAan")).booleanValue();
		zetBalkAan(balkAan);

newViewer = true;

		boolean bovenAanzichtMetHoogtes = false;
		if (h.containsKey("bovenAanzichtMetHoogtes"))
			bovenAanzichtMetHoogtes = ((Boolean) h.get("bovenAanzichtMetHoogtes")).booleanValue();
		zetBovenAanzichtMetHoogtes(bovenAanzichtMetHoogtes);

		boolean maakAanzicht = false;
		if (h.containsKey("maakAanzicht"))
			maakAanzicht = ((Boolean) h.get("maakAanzicht")).booleanValue();
		zetMaakAanzicht(maakAanzicht);
		
		boolean blokkenBouwsel = true;
		if (h.containsKey("blokkenBouwsel"))
			blokkenBouwsel = ((Boolean) h.get("blokkenBouwsel")).booleanValue();
		zetBlokkenBouwsel(blokkenBouwsel);
		
		boolean silhouet = false;
		if (h.containsKey("silhouet"))
			silhouet = ((Boolean) h.get("silhouet")).booleanValue();
		zetSilhouet(silhouet);
//System.out.println("ses " + silhouet);		
newViewer = false;
		
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

		int scoreMax = 10;
		if (h.containsKey("scoreMax"))
			scoreMax = ((Integer) h.get("scoreMax")).intValue();
		this.scoreMax = scoreMax;
		
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

		if (kijkNaActief)
		{	noSetBounds = true;
			kPanel.kijkNaPanel.setVisible(true);
			kPanel.layoutComponents();
			if (!kPanel.isVisible())
			{
				int vSpace = getSize().height - v.getSize().height - 24;
				if (ip.isVisible())
					vSpace -= 24;
			
				if (vSpace > 0)
				{	noSetBounds = true;
					v.setBounds(v.getLocation().x, vSpace / 2, v.getSize().width, v.getSize().height);
				}
				else
				{	noSetBounds = true;
					v.setBounds(v.getLocation().x + 12, v.getLocation().y, v.getSize().width - 24, v.getSize().height - 24);
				}	
				v.zetGetalRooster2(bovenAanzichtMetHoogtes);
				v.zetHoogtes();

				if (ip.isVisible())
				{	
					noSetBounds = true;
					ip.setBounds(v.getLocation().x, v.getLocation().y + v.getSize().height, v.getSize().width, 24);
					
					noSetBounds = true;
					kPanel.setBounds(v.getLocation().x, ip.getLocation().y + ip.getSize().height, v.getSize().width, 24);
				}
				else
				{	
					noSetBounds = true;
					kPanel.setBounds(v.getLocation().x, v.getLocation().y + v.getSize().height, v.getSize().width, 24);
				}

				noSetBounds = true;
				kPanel.setVisible(true);

			}
		}
		
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
			
			if (silhouet && !bovenAanzichtMetHoogtes)
			{
				kr.zetVulkleur("zwart"); 
			}
			
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
	    
	    h.put("volLeegOptie", new Boolean(volLeegOptie));
	    h.put("aantalBlokjes", new Boolean(aantalBlokjes));
	    
	    h.put("pijlAan", new Boolean(pijlAan));
	    h.put("balkAan", new Boolean(balkAan));
	    
	    h.put("bovenAanzichtMetHoogtes", new Boolean(bovenAanzichtMetHoogtes));
	    h.put("maakAanzicht", new Boolean(maakAanzicht));
	    
	    h.put("blokkenBouwsel", new Boolean(blokkenBouwsel));
	    h.put("silhouet", new Boolean(silhouet));
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
	    
	    h.put("scoreMax", new Integer(scoreMax));
	    
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
	
	public InteractieEditPanel getEditPanel()
	{	return new NabouwenAanzichtenInteractieEditPanel();
	}
		
	public void wis()
	{}
	
	public int geefAsHoogte()
	{	return 0;
	}
	
	public boolean isCorrect()
	{	if (!kijkNaActief)
			return true;
		return 
			score == scoreMax;
	}
	
	public boolean isFout()
	{	if (!kijkNaActief)
			return false;
		return score == 0;
	}
	
	public void zetMode(int mode)
    {   this.mode = mode;
		kijkNaButton.setVisible(mode == 0 || mode == 1);
    }
	
	public void zetNagekeken(boolean b)
	{	if (ingevuld) 
			nagekeken = b;
	}
	
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
        		score = scoreMax;
        }
        else if (checkDrieAanzichten)
        {	// nog niet goed
        	if (!kr.isGelijkAanzichten(docentKr))
        		score = 0;
        	else
        	{	if (checkAantalKubus)
        		{	score = Math.max(scoreMax / 2, scoreMax - Math.abs(kr.aantalKubussen - docentKr.aantalKubussen));
        		}
        		else
        		{	score = scoreMax;
        		}
        	}
        	
        }
        else if (checkVoorZijAanzicht)
        {	// nog niet goed
        	if (!kr.isGelijkVoorEnRechtsAanzicht(docentKr))
        		score = 0;
        	else
        	{	if (checkAantalKubus)
        		{	score = Math.max(scoreMax / 2, scoreMax - Math.abs(kr.aantalKubussen - docentKr.aantalKubussen));
        		}
        		else
        		{	score = scoreMax;
        		}
        	}
        	
        }
        else if (checkBovenVoorAanzicht)
        {	// nog niet goed
        	if (!kr.isGelijkBovenEnVoorAanzicht(docentKr))
        		score = 0;
        	else
        	{	if (checkAantalKubus)
        		{	score = Math.max(scoreMax / 2, scoreMax - Math.abs(kr.aantalKubussen - docentKr.aantalKubussen));
        		}
        		else
        		{	score = scoreMax;
        		}
        	}
        	
        }	
        else if (checkBovenZijAanzicht)
        {	// nog niet goed
        	if (!kr.isGelijkBovenEnRechtsAanzicht(docentKr))
        		score = 0;
        	else
        	{	if (checkAantalKubus)
        		{	score = Math.max(scoreMax / 2, scoreMax - Math.abs(kr.aantalKubussen - docentKr.aantalKubussen));
        		}
        		else
        		{	score = scoreMax;
        		}
        	}
        	
        }	
        else if (checkBovenAanzicht)
        {	// nog niet goed
        	if (!kr.isGelijkBovenAanzicht(docentKr))
        		score = 0;
        	else
        	{	if (checkAantalKubus)
        		{	score = Math.max(scoreMax / 2, scoreMax - Math.abs(kr.aantalKubussen - docentKr.aantalKubussen));
        		}
        		else
        		{	score = scoreMax;
        		}
        	}
        	
        }	
        else if (checkVoorAanzicht)
        {	// nog niet goed
        	if (!kr.isGelijkVoorAanzicht(docentKr))
        		score = 0;
        	else
        	{	if (checkAantalKubus)
        		{	score = Math.max(scoreMax / 2, scoreMax - Math.abs(kr.aantalKubussen - docentKr.aantalKubussen));
        		}
        		else
        		{	score = scoreMax;
        		}
        	}
        	
        }	
        else if (checkRechtsAanzicht)
        {	// nog niet goed
        	if (!kr.isGelijkRechtsAanzicht(docentKr))
        		score = 0;
        	else
        	{	if (checkAantalKubus)
        		{	score = Math.max(scoreMax / 2, scoreMax - Math.abs(kr.aantalKubussen - docentKr.aantalKubussen));
        		}
        		else
        		{	score = scoreMax;
        		}
        	}
        	
        }	
        ingevuld = kr.geefAantalK() != 0;
        if (score == 0)
        {	kruisjeLabel.setVisible(true);
        	geelVinkjeLabel.setVisible(false);
        	groenVinkjeLabel.setVisible(false);
        }
        else if (score < scoreMax)
        {	kruisjeLabel.setVisible(false);
        	geelVinkjeLabel.setVisible(true);
        	groenVinkjeLabel.setVisible(false);
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
    
    public void kijkNa(int stapNr)
    { 	kijkNa();
    }

	class KijkNaAL implements ActionListener
	{	public void actionPerformed(ActionEvent e)
		{
			kijkNa();
		}
		
	}
    
    public void addActionListener(ActionListener al)
    {	listeners.addElement(al);
    }

    @Override
	public void addCBookEventListener(CBookEventListener listener, String command) {
		cbookEventHandler.addCBookEventListener(listener, command);
		
	}

	@Override
	public void removeCBookEventListener(CBookEventListener listener,String command) {
		cbookEventHandler.removeCBookEventListener(listener, command);
		
	}

	@Override
	public String[] getSendCmds() {
		String[] commands = {"blockBuilding"};
		return commands;
	}

	@Override
	public String[] getAcceptedCmds() {
		String[] commands = {"blockBuilding", "text.buildingProgram"};
		return commands;
	}

	@Override
	public void acceptCBookEvent(CBookEvent event) {
		String command = event.getCommand();
		if(command.startsWith("blockBuilding"))
		{
			Map map = (Map)event.getParameters();
			if(map!=null)
			{	boolean[][][] booleanKR = (boolean[][][])map.get("booleanKR");
				kr = new KubusRooster(booleanKR, 1); 
				v.zetKubusRooster(kr);
				vp.zetKubusRooster(kr);
				na.setValue(kr.maxAantal);
				kPanel.aantalKLabel.setText("" + kr.geefAantalK() + " " + NabouwenAanzichten.rb.getString("blokjesTekst"));	
			}
			
		}
		if(command.startsWith("text.buildingProgram"))
		{
			Map map = (Map)event.getParameters();
			if(map!=null)
			{
				String programText = (String)map.get("content");
				setCursor(new Cursor(Cursor.WAIT_CURSOR));
				kr.maakLeeg();
				Interpreter interpreter = new Interpreter(kr);
				interpreter.execute(programText);
				setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
				zetVeranderd();
			}
		}
	}
			
	
	@Override
	public String getLocalizedCmd(String cmd) {
		String localizedCmd = NabouwenAanzichten.rb.getString(CBA_PREFIX + cmd);
		if(localizedCmd==null)
			return cmd;
		return localizedCmd;
	}
	
}