package fi.mozarch;

import java.awt.event.*;
import java.awt.*;
import java.io.*;
import java.util.*;

import fi.beans.base64code.*;
import fi.beans.copyright.*;
import fi.beans.scorm.*;

import fi.beans.wiskopdrbeans.InteractiePanel;

import javax.swing.*;

public class MozArch extends TekenApplet implements ActionListener,
													ScormAppletIF, WiskOpdrParamEditApplet				
{
	
	// scormgebeuren
	protected static ResourceBundle rb;
	protected static String langArg;
	protected SCORM12APIInterface api;
	boolean scormed = false;
	boolean reviewMode = false;	
	
	// DWO-component-gebeuren
	boolean isDWOComponent = false;
	
	private Vlakdeel[] vlakdelen;
	private Vlakdeel actiefVlakdeel; //,vorigActiefVlakdeel;

// aantal wordt niet gebruikt
	private int aantal;
	private int aantalVlakdelen, actiefVlakdeelNummer;
	double trek, trekx = 0,treky = 0; 
	double zijde;
	int[] volgorde;
	
	private FIButton infoButton;
	private LWButton wisKnop, inzendenKnop, bestandenKnop;
	private JPanel knopPanel;
	
	private BestandenFrame bestandenFrame;

	private int beginFigAantalHp;
	private int beginFigAantalPz = 1;
	private int beginFractielType;
	
	// parametrisatie
	private Color bgcolor = Color.white;
    private boolean beginFig;
    private boolean fractielen = false;
    
	public static void main(String[] args)    
	{	int width = 780;
        int height = 500;

        MozArch mozArch = new MozArch();
        mozArch.scormed = true;
		ScormMainFrame mf = new ScormMainFrame(mozArch, width, height);
		mf.setTitle("MozArch scormed");
		mf.pack();
		mf.show();
		
		mozArch.setLocation(mf.getInsets().left, mf.getInsets().top);		
		
		int framebreedte = width + mf.getInsets().left + mf.getInsets().right;
		int framehoogte = height + mf.getInsets().top + mf.getInsets().bottom;
		mf.setSize(framebreedte, framehoogte);
	}
	
	// schaalbaarheid
	// breedte stapelstrook
	private int rightWidth = 180;
    private int fractielRightWidth = 105;
    // centrum applet
    private int cX, cY;
    private int stapelX;
  
    // in normale(!) coordinaten
    private int veldXMin = 10, veldYMin = 10;
    private int veldXMax, veldYMax;
    
    private double dummyX, startX, startY;
    
    public MozArch()
    {	langArg = "nl";
		Locale language = new Locale (langArg, "");
		rb = ResourceBundle.getBundle("fi.mozarch.text.Text", language);
    }
    public MozArch(Locale language)
    {	
    	langArg = language.getLanguage();
    	rb = ResourceBundle.getBundle("fi.mozarch.text.Text", language);
    }
	
	public void initialiseer()
	{	
		try
		{	api = Scorm.findAPI(this);
		}
		catch(Exception e){}
		
		String langArg = getParameter("language");
		if (langArg == null || !langArg.equals("en")) 
			langArg = "nl";
		Locale language = new Locale (langArg, "");
		rb = ResourceBundle.getBundle("fi.mozarch.text.Text",language);
		
		bgcolor = Color.white;
		String kleurcode = getParameter("bgcolor");
		if (kleurcode != null)
		{	bgcolor = new Color(Integer.parseInt(kleurcode.substring(1), 16));
			//setBackground(bgcolor);
		}
		
		String fractielenString = getParameter("fractielen");
		if ((fractielenString != null) && 
			(fractielenString.equals("yes") || fractielenString.equals("true")))
			fractielen = true;

if (scormed)
{
	//fractielen = true;
}
/*
fiButton = new FIButton("Tegels",new String[]{"","versie-info: 20110331",
		"auteurs: Peter Boon, Frans van Galen",
		"programmeur: Peter Boon",
		"Freudenthal Instituut",
		"www.fi.uu.nl",""});
*/
		
		if (fractielen)
		{	infoButton = new FIButton("Info", new String[]{"Fractielen", "versie-info: 20110406",
		    						  "auteur: Peter Boon", "programmeurs: Peter Boon, Huub Nilwik",
		    						  "Freudenthal Instituut", "www.fi.uu.nl"});
		}
		else
		{	infoButton = new FIButton("Info", new String[]{"Mozaiek", "versie-info: 20110406",									  "auteur: Peter Boon", "programmeur: Peter Boon",
									  "Freudenthal Instituut", "www.fi.uu.nl"});
		}
		
		if (fractielen)
			rightWidth = fractielRightWidth;

		veldXMax = getSize().width - rightWidth;
		
		knopPanel = new JPanel();
		if (fractielen)
		{	knopPanel.setBounds(getSize().width - rightWidth + 1, getSize().height - 58, rightWidth - 2, 57);
			veldYMax = getSize().height - 10;
		}
		else	
		{	knopPanel.setBounds(1, getSize().height - 28, getSize().width - rightWidth, 27);
			veldYMax = getSize().height - 30;
		}
		knopPanel.setLayout(null);
		knopPanel.setBackground(new Color(220, 220, 160));
		getContentPane().add(knopPanel);
		//tb.add(knopPanel);
		
				
		if (fractielen)
			infoButton.setBounds((knopPanel.getSize().width - 20) / 2, 22, 20, 30);
		else	
			infoButton.setBounds(10, 1, 20, 30);
		//infoButton.setBackground(new Color(190, 190, 130));
		knopPanel.add(infoButton);

		wisKnop = new LWButton(MozArch.rb.getString("wisKnopLabel"));
		FontMetrics fm = getFontMetrics(wisKnop.getFont());
		int width = fm.stringWidth(wisKnop.getLabel()) + 55;
		if (fractielen)
			//wisKnop.setBounds(knopPanel.getSize().width - width - 5, 4, width, 20);
			wisKnop.setBounds((knopPanel.getSize().width - width) / 2, 4, width, 20);
		else	
			wisKnop.setBounds(knopPanel.getSize().width - width, 4, width, 20);
		wisKnop.setBackground(new Color(190,190,130));
		wisKnop.addActionListener(this);
		knopPanel.add(wisKnop);
		
		inzendenKnop = new LWButton(MozArch.rb.getString("opstuurKnopLabel"));
		inzendenKnop.addActionListener(this);
		inzendenKnop.setBounds(100,4,160,20);
		inzendenKnop.setBackground(new Color(190,190,130));
		//panel.add(inzendenKnop);
		
		bestandenKnop = new LWButton(MozArch.rb.getString("bestandenKnopLabel"));
		bestandenKnop.addActionListener(this);
		bestandenKnop.setBounds(280,4,240,20);
		bestandenKnop.setBackground(new Color(190,190,130));
		//panel.add(bestandenKnop);
		
		achtergrondkleur(bgcolor.getRed(), bgcolor.getGreen(), bgcolor.getBlue());
		
		
		maakMuisActieMogelijk();
		
		aantalVlakdelen = 0;
		// aantal wordt nergens gebruikt
		aantal = 10;
		zijde = 40;
		vlakdelen = new Vlakdeel[200];
		volgorde = new int[200];

		cX = getSize().width / 2;
		cY = getSize().height / 2;
		
		//tb.zetStartPunt(cX, cY);
		
		stapelX = getSize().width - rightWidth / 2 - cX;
		
		dummyX = - 2 * cX; 
		startX = - cX + (veldXMax + veldXMin) / 2; 	
		startY = cY - (veldYMax - veldYMin) / 2;
		
		// originele versie
		if (!fractielen)
		{	
			beginFig = true;
			beginFigAantalHp = 12;
			beginFigAantalPz = 2;
			String beginCode = getParameter("startFiguur");
			if (beginCode == null || beginCode.equals("")) 
				beginFig = false;
		
			if (beginFig)
			{	int grens = beginCode.indexOf(",");
				if (grens > 0)
				{	beginFigAantalHp = Integer.parseInt(beginCode.substring(0, grens));
					beginFigAantalPz = Integer.parseInt(beginCode.substring(grens + 1));
				}
				else
				{	
					beginFigAantalHp = Integer.parseInt(beginCode);
					beginFigAantalPz = 1;
				}
			}	
			
			if (!beginFig)
				maakVeelhoek(beginFigAantalPz, beginFigAantalHp, dummyX, startY, new Color(230, 230, 230));
			else 
				maakVeelhoek(beginFigAantalPz, beginFigAantalHp, startX, startY, new Color(230, 230, 230));
			
/*		
			maakVeelhoek(1,  3, stapelX,  220, new Color(255, 0, 0));
			maakVeelhoek(1,  4, stapelX,  160, new Color(255, 255, 0));
			maakVeelhoek(1,  6, stapelX,   80, new Color(0, 255, 0));
			maakVeelhoek(1,  8, stapelX,  -20, new Color(0, 255, 255));
			maakVeelhoek(1, 12, stapelX, -160, new Color(0, 0, 255));
*/
			maakVeelhoek(1,  3, stapelX,  cY - 30, new Color(255, 0, 0));
			maakVeelhoek(1,  4, stapelX,  cY - 90, new Color(255, 255, 0));
			maakVeelhoek(1,  6, stapelX,  cY - 170, new Color(0, 255, 0));
			maakVeelhoek(1,  8, stapelX,  cY - 270, new Color(0, 255, 255));
			maakVeelhoek(1, 12, stapelX,  cY - 410, new Color(0, 0, 255));
			
			if (beginFig)
				vlakdelen[0].nieuw = false;
		
			String startFile = getParameter("stfile");
			if (startFile != null && !startFile.equals(""))
				zetVlakdelen(startFile);
		}
		else // versie met fractielen
		{	
			// aanpassen
			zijde = 60;

			beginFig = true;
			beginFractielType = 3;
			beginFigAantalPz = 1;
			String beginCode = getParameter("startFiguur");
			if (beginCode == null || beginCode.equals("")) 
				beginFig = false;
		
			if (beginFig)
			{	int grens = beginCode.indexOf(",");
				if (grens > 0)
				{	beginFractielType = Integer.parseInt(beginCode.substring(0, grens));
					beginFigAantalPz = Integer.parseInt(beginCode.substring(grens + 1));
				}
				else
				{	beginFractielType = Integer.parseInt(beginCode);
					beginFigAantalPz = 1;
				}
			}
			
			if ((beginFractielType < 1) || (beginFractielType > 3))
			{	beginFractielType = 3;
				beginFig = false;
			}
			if (beginFigAantalPz != 1)
				beginFigAantalPz = 1;			
			
			if (!beginFig)
			{	maakFractiel(beginFractielType, beginFigAantalPz, dummyX, startY, new Color(230, 230, 230));
			}
			else 
			{	maakFractiel(beginFractielType, beginFigAantalPz, startX, startY, new Color(230, 230, 230));
			}
			
			// stukje 1 heeft ry=60, dus centrum op 10+60=70
			// stukje 2 heeft ry=55, dus centrum op 70+60+10+55=195
			// stukje 3 heeft ry=50, dus centrum op 195+55+10+50=310
			
			maakFractiel(1, 1, stapelX, cY - 70, new Color(255, 0, 0));
			maakFractiel(2, 1, stapelX, cY - 195, new Color(255, 255, 0));
			maakFractiel(3, 1, stapelX, cY - 310, new Color(0, 0, 255));
		
			if (beginFig)
				vlakdelen[0].nieuw = false;

		}
 	} // initialiseer
	
	
 	public void update(Graphics g)
 	{
 		paint(g);
 	}

	public void tekenprogramma()
	{	
		
//System.out.println("tekenprogramma");		
		for (int i = aantalVlakdelen - 1; i > -1; i--)
		{	if (volgorde[i] != -1 && !vlakdelen[volgorde[i]].nieuw)
				tekenVlakdeel(vlakdelen[volgorde[i]]);
		}
		tekenKader();
		for (int i = 0; i < aantalVlakdelen; i++)
		{	if (volgorde[i] != -1 && vlakdelen[volgorde[i]].nieuw)
				tekenVlakdeel(vlakdelen[volgorde[i]]);
		}
		if (actiefVlakdeel != null)
			tekenVlakdeel(actiefVlakdeel);
		
		
	}
	
	void tekenKader()
	{			
		
//System.out.println("tekenKader");		
		
		penUit();

		// pen staat in (390,250)
		// stap(-390, 250);
		
		// pen staat in (cX,cY)
		stap(-cX, cY);
		// pen nu in (0,0)

		penAan();
		vulAan(220, 220, 160);
		
		//stap(779, 0);
		// pen nu in (779,0)
		//stap(0, -499);
		// pen nu in (779,499)
		//stap(-779, 0);
		// pen nu in (0,499)
		//stap(0, 499);
		// pen nu in (0,0)

		stap(getSize().width - 1, 0);
		// pen nu in (width-1,0)
		stap(0, -(getSize().height - 1));
		// pen nu in (width-1,height-1)
		stap(-(getSize().width - 1), 0);
		// pen nu in (0,height-1)
		stap(0, getSize().height - 1);
		// pen nu in (0,0)

		//stap(10, -10);
		// pen nu in (10,10)
		//stap(0, -460);
		// pen nu in (10,470)
		//stap(590, 0);
		// pen nu in (600,470)
		//stap(0, 460);
		// pen nu in (600,10)
		//stap(-590, 0);
		// pen nu in (10, 10)
		//stap(-10, 10);
		// pen nu in (0,0)

		stap(veldXMin, -veldYMin);
		// pen nu in (veldXMin,veldYMin)
		stap(0, -(veldYMax - veldYMin));
		// pen nu in (veldXMin,veldYMax)
		stap((veldXMax - veldXMin), 0);
		// pen nu in (veldXMax,veldYMax)
		stap(0, (veldYMax - veldYMin));
		// pen nu in (veldXMax,veldYMin)
		stap(-(veldXMax - veldXMin), 0);
		// pen nu in (veldXMin, veldYMin)
		stap(-veldXMin, veldYMin);
		// pen nu in (0,0)

		vulUit();
		penUit();
		
		stap(1, -1);
		
		// correctie
		vulAan(220, 220, 160);
		stap(9, 0);
		stap(0, -9);
		stap(-9, 0);
		stap(0, 9);
		vulUit();
		
		stap(-1, 1);
		// terug naar (390,250)
		//stap(390, -250);
		
		//terug naar (cX,cY)
		stap(cX, -cY);
		
		penAan();
		
	}

	void maakFractiel(int type, int aantalPerZijde, double x, double y, Color k)
	{
		vlakdelen[aantalVlakdelen] = new Vlakdeel(this, aantalPerZijde * 4 + 1, x, y, k);
		vlakdelen[aantalVlakdelen].aantalHoekpunten = 4;
		vlakdelen[aantalVlakdelen].aantalPuntenPerZijde = aantalPerZijde;
		vlakdelen[aantalVlakdelen].fractielType = type;
		
//		if (((int) Math.round(x)) == stapelX)
//			vlakdelen[aantalVlakdelen].isHeap = true;
		
		volgorde[aantalVlakdelen] = aantalVlakdelen;
		vlakdelen[aantalVlakdelen].hoekpunten[0] = new HoekpuntMoz(0,0);
		
		double hoek = Math.PI / 4;
				
		if (type == 1)
		{	hoek = 6 * Math.PI / 14;
		}
		if (type == 2)
		{	hoek = 5 * Math.PI / 14;
		}
		if (type == 3)
		{	hoek = 4 * Math.PI / 14;
		}
		
		//int fZijde =
		
		double rx = zijde * Math.cos(hoek);
		double ry = zijde * Math.sin(hoek);
		
//System.out.println("rx = " + rx);		
//System.out.println("ry = " + ry);		
		
		for (int i = 0; i < 4; i++)
		{	
			double h = - 2 * i * Math.PI / 4;
			double a = rx * Math.cos(h);
			double b = ry * Math.sin(h);
			double hv = - 2 * (i + 1) * Math.PI / 4;
			double av = rx * Math.cos(hv);
			double bv = ry * Math.sin(hv);
			for (int j = 0; j < aantalPerZijde; j++)
			{	double an = a + j * (av - a) / aantalPerZijde;
				double bn = b + j * (bv - b) / aantalPerZijde;
				vlakdelen[aantalVlakdelen].hoekpunten[1 + aantalPerZijde * i + j] = new HoekpuntMoz(an,  bn);
			}
		}
		
		vlakdelen[aantalVlakdelen].hoekpunten[aantalPerZijde * 4 + 1] = vlakdelen[aantalVlakdelen].hoekpunten[1];
		aantalVlakdelen++;
	}
	
	void maakVeelhoek(int aantalPerZijde, int n, double x, double y, Color k)
	{	
		double r = zijde * aantalPerZijde / (2 * Math.sin(Math.PI / n));
		
		vlakdelen[aantalVlakdelen] = new Vlakdeel(this, aantalPerZijde * n + 1, x, y, k);
		vlakdelen[aantalVlakdelen].aantalHoekpunten = n;
		vlakdelen[aantalVlakdelen].aantalPuntenPerZijde = aantalPerZijde;
		
//		if (((int) Math.round(x)) == stapelX)
//			vlakdelen[aantalVlakdelen].isHeap = true;
		
		volgorde[aantalVlakdelen] = aantalVlakdelen;
		vlakdelen[aantalVlakdelen].hoekpunten[0] = new HoekpuntMoz(0,0);
	
		for (int i = 0; i < n; i++)
		{	
			double h = Math.PI /(n) - 2 * i * Math.PI / n;
			double a = r * Math.cos(h);
			double b = r * Math.sin(h);
			double hv = Math.PI /(n) - 2 * (i + 1) * Math.PI / n;
			double av = r * Math.cos(hv);
			double bv = r * Math.sin(hv);
			for (int j = 0; j < aantalPerZijde; j++)
			{	double an = a + j * (av - a) / aantalPerZijde;
				double bn = b + j * (bv - b) / aantalPerZijde;
				vlakdelen[aantalVlakdelen].hoekpunten[1 + aantalPerZijde * i + j] = new HoekpuntMoz(an,  bn);
			}
		}
		
		vlakdelen[aantalVlakdelen].hoekpunten[aantalPerZijde * n + 1] = vlakdelen[aantalVlakdelen].hoekpunten[1];
		aantalVlakdelen++;														
	}
	
	void maakAchtergrond()
	{
	}
	
	void tekenVlakdeel(Vlakdeel vd)
	{	
		int len = vd.hoekpunten.length;
		int num = vd.beginnummer;
		
		penUit(); 
		stap(vd.draaipunt.x, vd.draaipunt.y); 
		links(vd.orientatie);
		vulAan(vd.kleur);
		
		for (int i = 0; i < len; i++)
		{	stap(vd.hoekpunten[(i + 1 + num) % len].x - vd.hoekpunten[(i + num) % len].x , 
				 vd.hoekpunten[(i + 1 + num) % len].y - vd.hoekpunten[(i + num) % len].y);
			vd.hoekpunten[(i + 1 + num) % len].tekenpunt = new Punt(geefPunt());
		}
		vulUit();
		vd.tekenvlak = geefVlak();
		penAan();
		for (int i = 0 ; i < len; i++)
		{	if ((i + 1 + num) % len == 0 || (i + num) % len == 0)
				penUit();
			stap(vd.hoekpunten[(i + 1 + num) % len].x - vd.hoekpunten[(i + num) % len].x , 
				 vd.hoekpunten[(i + 1 + num) % len].y - vd.hoekpunten[(i + num) % len].y);
			penAan();
		}
		rechts(vd.orientatie);
		penUit();
		stap(-vd.draaipunt.x, -vd.draaipunt.y);
		tb.zetStart();
	}	
	
	public Vlakdeel[] geefVlakdelen()
	{	return vlakdelen;
	}
	
	public int geefAantalVlakdelen()
	{	return aantalVlakdelen;
	}
	
	public void zetVlakdelen(Vlakdeel[] vd, int[] vlg, int aantal)
	{	actiefVlakdeel = null;
		vlakdelen = vd;
		aantalVlakdelen = aantal;
		for (int i = 0; i < aantalVlakdelen; i++)
		{	volgorde[i] = vlg[i];
		}
		tekenOpnieuw();
	}


	public void zetVlakdelen(String s)
	{	actiefVlakdeel = null;
		Base64InputStream invoer;
		try
		{	ByteArrayInputStream bais = new ByteArrayInputStream(s.getBytes());
			invoer = new Base64InputStream(bais);
			aantalVlakdelen = invoer.readShort();
			//vlakdelen = new Vlakdeel[200];
			//volgorde = new int[200];
			for (int i = 0 ; i < aantalVlakdelen; i++)
			{	int aantalPunten = invoer.readShort();
				double posx = invoer.readDouble();
				double posy = invoer.readDouble();
				double orientatie = invoer.readDouble();
				boolean nieuw = invoer.readBoolean();
				int beginnummer = invoer.readShort();
				int rood = invoer.readByte() + 128;
				int groen = invoer.readByte() + 128;
				int blauw = invoer.readByte() + 128;
				volgorde[i] = invoer.readShort();
				Color c = new Color(rood,groen,blauw);
				vlakdelen[i] = new Vlakdeel(this, aantalPunten, posx, posy, c);
				vlakdelen[i].orientatie = orientatie;
				vlakdelen[i].beginnummer = beginnummer;
				vlakdelen[i].nieuw = nieuw;
				for (int j = 0; j < aantalPunten + 1; j++)
				{	double x = invoer.readDouble();
					double y = invoer.readDouble();
					vlakdelen[i].hoekpunten[j] = new HoekpuntMoz(x,y);
				}
			}
		}
		catch(IOException io){}
	}

	
	public String maakString()
	{	Base64OutputStream uitvoer = null;
		ByteArrayOutputStream baos = null;
		try
		{	
			baos = new ByteArrayOutputStream();
			uitvoer = new Base64OutputStream(baos);
			uitvoer.writeShort((short)(aantalVlakdelen));
			for(int i=0 ; i<aantalVlakdelen ; i++)
			{	uitvoer.writeShort((short)(vlakdelen[i].aantalPunten));
				uitvoer.writeDouble(vlakdelen[i].draaipunt.x);
				uitvoer.writeDouble(vlakdelen[i].draaipunt.y);
				uitvoer.writeDouble(vlakdelen[i].orientatie);
				uitvoer.writeBoolean(vlakdelen[i].nieuw);
				uitvoer.writeShort((short)(vlakdelen[i].beginnummer));
				uitvoer.writeByte((byte)(vlakdelen[i].kleur.getRed()-128));
				uitvoer.writeByte((byte)(vlakdelen[i].kleur.getGreen()-128));
				uitvoer.writeByte((byte)(vlakdelen[i].kleur.getBlue()-128));
				uitvoer.writeShort((short)(volgorde[i]));
				for(int j=0 ; j<vlakdelen[i].aantalPunten+1 ; j++)
				{	uitvoer.writeDouble(vlakdelen[i].hoekpunten[j].x);
					uitvoer.writeDouble(vlakdelen[i].hoekpunten[j].y);
				}
			}	
			uitvoer.close();
		}
		catch(IOException io){}
		return baos.toString();
	}
	
	public void muisDrukActie()
	{	
		actiefVlakdeel = null;
		for (int i = 0; i < aantalVlakdelen; i++)
		{	int ii = volgorde[i];
			if (ii > 0 && vlakdelen[ii].tekenvlak.contains(geefDrukx(), geefDruky()))
			{	actiefVlakdeel = vlakdelen[ii];
				for (int j = i; j > 0 ; j--)
				{	volgorde[j] = volgorde[j - 1];
				}
				volgorde[0] = ii;
				actiefVlakdeelNummer = ii;
				vlakdelen[ii].sleeppunt.x = geefDrukx();  
				vlakdelen[ii].sleeppunt.y = geefDruky();
				break;
			}
		}
		
	}
	
	public void muisSleepActie()
	{	
		if (actiefVlakdeel == null)
			return;

		int dx = geefSleepdx();  
		int dy = -geefSleepdy();
		double d = Math.sqrt(dx * dx + dy * dy);
		if (d < 0.0001)
			return;
		double mpx = actiefVlakdeel.sleeppunt.x - actiefVlakdeel.draaipunt.x - 0.5 * getSize().width ; 
		double mpy = actiefVlakdeel.sleeppunt.y + actiefVlakdeel.draaipunt.y - 0.5 * getSize().height;
		double mp = Math.sqrt(mpx * mpx + mpy * mpy);
		
		double tx;
		double ty;
		double dhoek;
		if (mp < 8)
		{	tx = dx;
			ty = dy;
			dhoek = 0;
		}
		else
		{	double cosa = (mpx * dx + mpy * dy) / (mp * d);
			double mppx = actiefVlakdeel.sleeppunt.x + dx - actiefVlakdeel.draaipunt.x - 0.5 * getSize().width; 
			double mppy = actiefVlakdeel.sleeppunt.y + dy + actiefVlakdeel.draaipunt.y - 0.5 * getSize().height;
			double mpp = Math.sqrt(mppx * mppx + mppy * mppy);
			if (mpp < 0.0001)
				mpp = 0.0001;
			double t = mpp - mp;
			tx = (t / mpp) * mppx; 
			ty = (t / mpp) * mppy;
			double dh = (Math.acos((mp + d * cosa) / mpp)) * 180 / Math.PI;
			if (mpx * dy - mpy * dx > 0) 
				dhoek = -dh;
			else 
				dhoek = dh;
		}
		
		// stapels aanvullen
		if (actiefVlakdeel.nieuw)
		{	
			
			// originele versie	
			if (!fractielen)
			{	
				if (actiefVlakdeel.aantalPunten == 4)
					maakVeelhoek(1, 3, stapelX, cY - 30, new Color(255, 0, 0));
				else if (actiefVlakdeel.aantalPunten == 5)
					maakVeelhoek(1, 4, stapelX, cY - 90, new Color(255, 255, 0));
				else if (actiefVlakdeel.aantalPunten == 7)
					maakVeelhoek(1, 6, stapelX, cY - 170, new Color(0, 255, 0));
				else if (actiefVlakdeel.aantalPunten == 9)
					maakVeelhoek(1, 8, stapelX, cY -270, new Color(0, 255, 255));
				else if (actiefVlakdeel.aantalPunten == 13)
					maakVeelhoek(1, 12, stapelX, cY - 410, new Color(0, 0, 255));
			}
			else // fractielen versie
			{	
				if (actiefVlakdeel.fractielType == 1)
					maakFractiel(1, 1, stapelX, cY - 70, new Color(255, 0, 0));
				else if (actiefVlakdeel.fractielType == 2)
					maakFractiel(2, 1, stapelX,  cY - 195, new Color(255, 255, 0));
				else if (actiefVlakdeel.fractielType == 3)
					maakFractiel(3, 1, stapelX, cY - 310, new Color(0, 0, 255));
				
			}
			actiefVlakdeel.nieuw = false;
		}
		
		
		actiefVlakdeel.sleeppunt.x = actiefVlakdeel.sleeppunt.x + dx; 
		actiefVlakdeel.sleeppunt.y = actiefVlakdeel.sleeppunt.y + dy;
		
		if (actiefVlakdeel.aantalHoekpuntenVast < 1)
		{	actiefVlakdeel.draaipunt.x = actiefVlakdeel.draaipunt.x + tx;
			actiefVlakdeel.draaipunt.y = actiefVlakdeel.draaipunt.y - ty;
			actiefVlakdeel.orientatie = actiefVlakdeel.orientatie + dhoek;
		}
		
		if (actiefVlakdeel.aantalHoekpuntenVast > 0)
		{	if (actiefVlakdeel.aantalHoekpuntenVast == 1)
			actiefVlakdeel.orientatie = actiefVlakdeel.orientatie + dhoek;
			trekx += tx;
			treky += ty;
			trek = Math.sqrt(trekx * trekx + treky * treky);
			if (actiefVlakdeel.aantalHoekpuntenVast > 1 && trek > 0 || actiefVlakdeel.aantalHoekpuntenVast == 1 && trek > 20)
			{	actiefVlakdeel.draaipunt.x = actiefVlakdeel.draaipunt.x + trekx;
				actiefVlakdeel.draaipunt.y = actiefVlakdeel.draaipunt.y - treky;
				for (int i = 1; i < actiefVlakdeel.aantalPunten; i++)
				{	for (int m = 0; m < actiefVlakdeel.hoekpunten[i].aantalVastgeklikt; m++)
					{	vlakdelen[actiefVlakdeel.hoekpunten[i].vlakdeelnummers[m]].klikLos(
							actiefVlakdeel.hoekpunten[i].hoeknummersVlakdeel[m],actiefVlakdeelNummer,i);
						
					}
				}
				actiefVlakdeel.klikAllesLos();
				trekx = 0 ; 
				treky = 0;
			}
		}
		tekenOpnieuw();
	}
	
	public void muisLosActie()
	{	trekx = 0; 
		treky = 0;
		// geen actief vlakdeel
		if (actiefVlakdeel == null)
			return;
		// centrum van actief vlakdeel is buiten speelveld
		// verwijder dit vlakdeel
		
		// coordinaten zijn t.o.v. (cX,cY)
		int relXMin = cX - veldXMin;
		int relXMax = veldXMax - cX;
		int relYMin = cY - veldYMin;
		int relYMax = veldYMax - cY;
		
		//if (!actiefVlakdeel.nieuw && (actiefVlakdeel.draaipunt.x < -400 || actiefVlakdeel.draaipunt.x > 200 ||
		//                              actiefVlakdeel.draaipunt.y < -250 || actiefVlakdeel.draaipunt.y > 250))
		if (!actiefVlakdeel.nieuw && (actiefVlakdeel.draaipunt.x < -relXMin || actiefVlakdeel.draaipunt.x > relXMax ||
		                              actiefVlakdeel.draaipunt.y < -relYMax || actiefVlakdeel.draaipunt.y > relYMin))
		{	for (int j = actiefVlakdeelNummer; j < aantalVlakdelen - 1; j++)
			{	vlakdelen[j] = vlakdelen[j + 1];
			}
			aantalVlakdelen--;
			for (int j = 0 ; j < aantalVlakdelen; j++)
			{	volgorde[j] = volgorde[j + 1];
				if (volgorde[j] > actiefVlakdeelNummer)
					volgorde[j]--;
			}
			
			//volgorde[0] = -1;
			actiefVlakdeel = null;
			tekenOpnieuw();
			return;
		}
		
		for (int m = 0 ; m < 2; m++)	
		{
			double bx = 0;
			double by = 0;
			double dhoek = 0;
			boolean maakAf = false;
			boolean raak = false;
		
			for (int i = 1; i < actiefVlakdeel.aantalPunten; i++)
			{	for (int j = aantalVlakdelen - 1; j > -1; j--)
				{	int jj = volgorde[j];
					for (int k = 1; jj != -1 && k < vlakdelen[jj].aantalPunten; k++)
					{	
						double ax = vlakdelen[jj].hoekpunten[k].tekenpunt.x - actiefVlakdeel.hoekpunten[i].tekenpunt.x;
						double ay = vlakdelen[jj].hoekpunten[k].tekenpunt.y - actiefVlakdeel.hoekpunten[i].tekenpunt.y;
						
						if ((ax < 15 && ax > -15) && (ay < 15 && ay > -15)&& (actiefVlakdeelNummer != jj))
						{	
							if ((actiefVlakdeel.aantalHoekpuntenVast < 1 || maakAf) && !vlakdelen[jj].nieuw)
							{	maakAf = true;
								actiefVlakdeel.hoekpunten[i].tekenpunt.x += ax;
								actiefVlakdeel.hoekpunten[i].tekenpunt.y += ay;
								actiefVlakdeel.klikVast(i, jj, k);
								vlakdelen[jj].klikVast(k, actiefVlakdeelNummer, i);
								raak = true;
							}
							else
							{	if (!actiefVlakdeel.hoekpunten[i].zitVast(jj, k))
								{	double dax = actiefVlakdeel.hoekpunten[i].tekenpunt.x - actiefVlakdeel.draaipunt.x - 0.5 * getSize().width ;
									double day = -actiefVlakdeel.hoekpunten[i].tekenpunt.y - actiefVlakdeel.draaipunt.y + 0.5 * getSize().height;
									double dpx = vlakdelen[jj].hoekpunten[k].tekenpunt.x - actiefVlakdeel.draaipunt.x - 0.5 * getSize().width ;
									double dpy = -vlakdelen[jj].hoekpunten[k].tekenpunt.y -  actiefVlakdeel.draaipunt.y + 0.5 * getSize().height;
									double da = Math.sqrt(dax * dax + day * day);
									double dp = Math.sqrt(dpx * dpx + dpy * dpy);
									if ((da-dp < 0.01) && (da-dp > -0.01))
									{	if (Math.abs(dax - dpx) < 0.0001 && Math.abs(day - dpy) < 0.0001)
										{	dhoek = 0;
										}
										else
										{	double dh = (Math.acos((dax * dpx + day * dpy) / (da * dp)) * 180 / Math.PI);
											if (dax * dpy - day * dpx > 0) 
												dhoek = dh;
											else 
												dhoek = -dh;
										}
										actiefVlakdeel.klikVastDraai(i, jj, k);
										vlakdelen[jj].klikVastDraai(k, actiefVlakdeelNummer, i);
									}
								}
							}/**/
						} // voldoende dichtbij 
					} // for
				}
				if(raak)
				{	raak = false;
					break;
				}
			}
			actiefVlakdeel.orientatie += dhoek ;
			if (m == 0)
				tekenOpnieuw();
		}
		actiefVlakdeel = null;
		tekenOpnieuw();
	}


	
	public void wis()
	{	aantalVlakdelen = 0;
		actiefVlakdeel = null;
        
		if (!fractielen)
		{
		
			if (!beginFig)
				maakVeelhoek(beginFigAantalPz, beginFigAantalHp, dummyX, startY, new Color(230, 230, 230));
			else 
				maakVeelhoek(beginFigAantalPz, beginFigAantalHp, startX, startY, new Color(230, 230, 230));
        
			maakVeelhoek(1,  3, stapelX,  cY - 30, new Color(255, 0, 0));
			maakVeelhoek(1,  4, stapelX,  cY - 90, new Color(255, 255, 0));
			maakVeelhoek(1,  6, stapelX,  cY - 170, new Color(0, 255, 0));
			maakVeelhoek(1,  8, stapelX,  cY - 270, new Color(0, 255, 255));
			maakVeelhoek(1, 12, stapelX,  cY - 410, new Color(0, 0, 255));
			tekenOpnieuw();
        
			if (beginFig)
				vlakdelen[0].nieuw = false;
		}
		else
		{

			if (!beginFig)
			{	maakFractiel(beginFractielType, beginFigAantalPz, dummyX, startY, new Color(230, 230, 230));
			}
			else 
			{	maakFractiel(beginFractielType, beginFigAantalPz, startX, startY, new Color(230, 230, 230));
			}
			
			maakFractiel(1, 1, stapelX, cY - 70, new Color(255, 0, 0));
			maakFractiel(2, 1, stapelX, cY - 195, new Color(255, 255, 0));
			maakFractiel(3, 1, stapelX, cY - 310, new Color(0, 0, 255));
			tekenOpnieuw();
		
			if (beginFig)
				vlakdelen[0].nieuw = false;
			
			
		}
	}
	
	public void actionPerformed(ActionEvent e)
	{	if (e.getSource() == wisKnop)
		{	wis();
		}
		// niet gebruikt
		else if(e.getSource()==inzendenKnop)
		{	if(bestandenFrame!=null)bestandenFrame.dispose();
			bestandenFrame = new BestandenFrame();
			bestandenFrame.show();
			bestandenFrame.slaOp(maakString());
		}
		// niet gebruikt
		else if(e.getSource()==bestandenKnop)
		{	if(bestandenFrame!=null)bestandenFrame.dispose();
			bestandenFrame = new BestandenFrame();
			bestandenFrame.addActionListener(this);
			bestandenFrame.show();
			bestandenFrame.bekijk();
		}
		// niet gebruikt
		else if(e.getSource()==bestandenFrame)
		{	String file = bestandenFrame.geefActieveFile();
			if(file!=null)zetVlakdelen(file);
			bestandenFrame.dispose();
			tekenOpnieuw();
		}
	}

	// scormgebeuren

	public void start()
	{	if (api != null)
		{	String s = api.LMSGetValue("cmi.suspend_data");
			if (s != null && !s.equals(""))
			{	setState(s);
//System.out.println("set state");			
			}
		}
	}
	
	public void stop()
	{	if (api != null)
		{	String s = getState();
			String d = new Double(getScore()).toString();
			api.LMSSetValue("cmi.core.score.raw",d);
			api.LMSSetValue("cmi.suspend_data",s);
//System.out.println("get state");			
		}
	}

	public void stopSco()
	{	stop();
		api = null;
	}

	public void setState(String s)
	{	
//System.out.println("set: sl = " + s.length());

//init2();		
		
		// decodeer de string
		Object o = StringCodeObject.decodeStringToObject(s);
		// cast
		Hashtable h = (Hashtable) o;
		
if (h == null)
{
System.out.println("set h = null");	
	return;

}
		
		int tempAantalVlakdelen = 0;
		Vector vlakdelenVector = new Vector();
		if (h.containsKey("vlakdelenVector"))
		{	vlakdelenVector = (Vector) h.get("vlakdelenVector");
			tempAantalVlakdelen = vlakdelenVector.size();
//System.out.println("set: h contains vv");			
		}
		
//System.out.println("set: tav = " + tempAantalVlakdelen);		
		
		if (tempAantalVlakdelen == 0)
			return;

		int[] tempVolgorde = new int[tempAantalVlakdelen];
		Vlakdeel[] tempVlakdelen = new Vlakdeel[tempAantalVlakdelen];
		for (int i = 0; i < tempAantalVlakdelen; i++)
		{	
			Hashtable hv = (Hashtable) vlakdelenVector.elementAt(i);
			
			int fractielType = 3;
			if (hv.containsKey("fractielType"))
				fractielType = ((Integer) hv.get("fractielType")).intValue();
			int aantalHoekpunten = 4;
			if (hv.containsKey("aantalHoekpunten"))
				aantalHoekpunten = ((Integer) hv.get("aantalHoekpunten")).intValue();
			
//System.out.println("set: ap - i = " + aantalHoekpunten);

			int aantalPuntenPerZijde = 1;
			if (hv.containsKey("aantalPuntenPerZijde"))
				aantalPuntenPerZijde = ((Integer) hv.get("aantalPuntenPerZijde")).intValue();

			double positiex = startX;
			if (hv.containsKey("positiex"))
				positiex = ((Double) hv.get("positiex")).doubleValue();
			double positiey = startY;
			if (hv.containsKey("positiey"))
				positiey = ((Double) hv.get("positiey")).doubleValue();
			Color kleur = Color.lightGray;
			if (hv.containsKey("kleur"))
				kleur = (Color) hv.get("kleur");
			
			aantalVlakdelen = 0;
			if (fractielType == 0)
			{	maakVeelhoek(aantalPuntenPerZijde, aantalHoekpunten, positiex, positiey, kleur);
				
			}
			else
			{	maakFractiel(fractielType, aantalPuntenPerZijde, positiex, positiey, kleur);
			}
			tempVlakdelen[i] = vlakdelen[0];
			aantalVlakdelen = 0;

/*			
			boolean isHeap = false;
			if (hv.containsKey("isHeap"))
				isHeap = ((Boolean) hv.get("isHeap")).booleanValue();
			tempVlakdelen[i].isHeap = isHeap;
*/			
			double orientatie = 0;
			if (hv.containsKey("orientatie"))
				orientatie = ((Double) hv.get("orientatie")).doubleValue();
			tempVlakdelen[i].orientatie = orientatie;

			boolean nieuw = true;
			if (hv.containsKey("nieuw"))
				nieuw = ((Boolean) hv.get("nieuw")).booleanValue();
			tempVlakdelen[i].nieuw = nieuw;
			
			int beginnummer = 0;
			if (hv.containsKey("beginnummer"))
				beginnummer = ((Integer) hv.get("beginnummer")).intValue();
			tempVlakdelen[i].beginnummer = beginnummer;

/*			
			int aantalHoekpuntenVast = 0;
			if (hv.containsKey("aantalHoekpuntenVast"))
				aantalHoekpuntenVast = ((Integer) hv.get("aantalHoekpuntenVast")).intValue();
			tempVlakdelen[i].aantalHoekpuntenVast = aantalHoekpuntenVast;
*/			
			int tVolgorde = 0; 
			if (hv.containsKey("volgorde"))
				tVolgorde = ((Integer) hv.get("volgorde")).intValue();
			tempVolgorde[i] = tVolgorde;
			
			Vector hoekpuntenVector = new Vector();
			if (hv.containsKey("hoekpuntenVector"))
				hoekpuntenVector = ((Vector) hv.get("hoekpuntenVector"));
			for (int j = 0; j < hoekpuntenVector.size(); j++)
			{
				// zonder5 plakken
				Punt punt = (Punt) hoekpuntenVector.elementAt(j);
				tempVlakdelen[i].hoekpunten[j] = new HoekpuntMoz(punt.x, punt.y);
				
				// met plakken
				//tempVlakdelen[i].hoekpunten[j] = (HoekpuntMoz) hoekpuntenVector.elementAt(j);
			}		
			
//System.out.println("set: hv = " + hoekpuntenVector.size());

		} // for

/*		
		for (int vCnt = 0; vCnt < tempAantalVlakdelen; vCnt++)
		{	volgorde[vCnt] = vCnt;
		}
*/		

		
		for (int tCnt = 0; tCnt < tempAantalVlakdelen; tCnt++)
		{
			vlakdelen[tCnt] = tempVlakdelen[tCnt];
			aantalVlakdelen++;
		
			
			//aantalVlakdelen++;
			//actiefVlakdeel = vlakdelen[tCnt];
			
			volgorde[tCnt] = tempVolgorde[tCnt];
		}
		
	}

	public String getState()
	{	
		// creeer de gegevens die de state bepalen
		Hashtable h = new Hashtable();
		
		Vector vlakdelenVector = new Vector();
		for (int i = 0; i < aantalVlakdelen; i++)
		{	Hashtable hv = new Hashtable();
			hv.put("fractielType", new Integer(vlakdelen[i].fractielType));
			hv.put("aantalHoekpunten", new Integer(vlakdelen[i].aantalHoekpunten));
			hv.put("aantalPuntenPerZijde", new Integer(vlakdelen[i].aantalPuntenPerZijde));
			hv.put("positiex", new Double(vlakdelen[i].draaipunt.x));
			hv.put("positiey", new Double(vlakdelen[i].draaipunt.y));
			hv.put("kleur", vlakdelen[i].kleur);
			hv.put("orientatie", new Double(vlakdelen[i].orientatie));
			hv.put("nieuw", new Boolean(vlakdelen[i].nieuw));
			hv.put("beginnummer", new Integer(vlakdelen[i].beginnummer));
//			hv.put("aantalHoekpuntenVast", new Integer(vlakdelen[i].aantalHoekpuntenVast));
//			hv.put("isHeap", new Boolean(vlakdelen[i].isHeap));
			
			hv.put("volgorde", new Integer(volgorde[i]));
			
			Vector hoekpuntenVector = new Vector();
			for (int j = 0; j < vlakdelen[i].aantalPunten + 1; j++)
			{	hoekpuntenVector.addElement(
					
					// zonder plakken
					new Punt(vlakdelen[i].hoekpunten[j].x, vlakdelen[i].hoekpunten[j].y));
			
					// met plakken
					//vlakdelen[i].hoekpunten[j]);
			}
			hv.put("hoekpuntenVector", hoekpuntenVector);

//System.out.println("get: hv = " + hoekpuntenVector.size());			
			
			vlakdelenVector.addElement(hv);
		}
//System.out.println("get: vv = " + vlakdelenVector.size());

		h.put("vlakdelenVector", vlakdelenVector);
		
	    // codeer deze gegevens tot een string
	    String s = StringCodeObject.encodeObjectToString(h);

	    return s;
	}

	public double getScore()
	{	return 0.5;
	}
	
	public InteractiePanel getInteractiePanel()
	{
		//return new InteractiePanelAdapter(this);
		
		return new MZInteractiePanel();
	}	
	
	public boolean hasEditMode()
	{	return false;
	}

    public ScormEditComponentIF getEditComponent(Hashtable launchdata)
    {	return null;
    }
	
	
    public Parameter[] getEditableParameters()
	{	
    	Parameter[] parameters = new Parameter[2];
		
		DataType type = new ScormString();
		
		Parameter param = new Parameter("fractielen", "Fractielen", type);
		param.setHelpText("vul in: yes of no");		
		parameters[0] = param;
		
		param = new Parameter("startFiguur", "Eerste stukje", type);
		param.setHelpText("vul in: niets of AantalHoekpunten/FractielType , AantalPerZijde");
		parameters[1] = param;
		
		return parameters;
    }
	
    public Parameter[] getAllParameters()
    {	return null;
    }
	
    // wiskOpdrParamEditApplet
    public Hashtable getDefaultParameters()
    {
    	Hashtable h = new Hashtable();
    	
    	h.put("fractielen", "no");
    	
    	return h;
    }
    
	public void setSingleComponent()
	{	
		isDWOComponent = true;
		infoButton.setVisible(false);
		
	}
    
}
