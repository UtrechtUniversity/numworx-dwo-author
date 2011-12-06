package fi.tegels;

import java.awt.*;
import java.util.*;
import java.awt.event.*;

import fi.beans.base64code.*;
import fi.beans.scorm.*;

import fi.beans.wiskopdrbeans.InteractiePanel;

import javax.swing.*;
/**
 * @author Peter Boon
 */

public class Tegels extends JApplet implements MouseListener, MouseMotionListener, ActionListener,
											   ScormAppletIF, WiskOpdrParamEditApplet	
{	
	// scormgebeuren
	protected static ResourceBundle rb;
	protected SCORM12APIInterface api;
	boolean scormed = false;
	boolean reviewMode = false;	
	
	// DWO-component-gebeuren
	boolean isDWOComponent = false;
	
	private ControlPanel cp;
	TekenPanel tekenPanel;
	private String langArg;
	
	int breedte, hoogte;
	
	int aantalSs;
	private int laatstex = 0;
	private int laatstey = 0;
	
	SchuifStuk[] ss;
	SchuifStuk actiefSs;
	SchuifStuk basisv;
	SchuifStuk basisvOud;
	Point posBasis;
	
	int aantalNieuwHp;
	Point[] nieuwHp;
	Polygon zeshok;

	boolean pak, maakVorm, tegelKlaar, wisTegelEerst;
	
	String[] abc = {"A","B","C","D","E","F","G","H","I"};
	String code;
	
	JPopupMenu popup;
	JMenuItem mi;
	
	// parametrisatie
	// toon de transversie (het vroegere applet TegelsTr)
	// default false
	boolean transVersion = false;
	// toon de demo-versie (alleen nuttig voor de DWO)
	boolean demoVersion = false;
	
	int hokBreedte = 180;
	int controlHoogte = 70;//60;

	Vector basisVormen = new Vector();
	int actualBasisVorm = 0;
	
	public static void main(String[] args)    
	{	int width = 700;
        int height = 450;
		
        Tegels tegels = new Tegels();
		ScormMainFrame mf = new ScormMainFrame(tegels, width, height);
		mf.setTitle("Tegels Scormed");
		mf.pack();
		mf.show();
		
		tegels.setLocation(mf.getInsets().left, mf.getInsets().top);		
		
		int framebreedte = width + mf.getInsets().left + mf.getInsets().right;
		int framehoogte = height + mf.getInsets().top + mf.getInsets().bottom;
		mf.setSize(framebreedte, framehoogte);

		
	}

    public Tegels()
    {	Locale language = new Locale ("nl", "");
    }
    public Tegels(Locale language)
    {
    }

	public void init()
	{	
		
		try
		{	api = Scorm.findAPI(this);
		}
		catch(Exception e){}
		
		hoogte = getSize().height;
		breedte = getSize().width;
		
		getContentPane().setLayout(null);
		
		addMouseListener(this);
		addMouseMotionListener(this);
		
		langArg = getParameter("language");
		if (langArg == null || !langArg.equals("en") || !langArg.equals("ca")) 
			langArg = "nl";
		Locale language = new Locale (langArg, "");
		rb = ResourceBundle.getBundle("fi.tegels.text.Text",language);
		
		String versionString = getParameter("transversion");
		if (versionString != null && 
			(versionString.equals("yes") || versionString.equals("true")))
			transVersion = true;
		
		String demoString = getParameter("demoversion");
		if (demoString != null && 
			(demoString.equals("yes") || demoString.equals("true")))
			demoVersion = true;
		
		
		tekenPanel = new TekenPanel(this);
		tekenPanel.setBounds(0, 0, breedte, hoogte);
//		getContentPane().add(tekenPanel);
		
		cp = new ControlPanel(this);
		cp.setLayout(null);
		//cp.setBounds(181, hoogte - 61, breedte - 182, 60);
		cp.setBounds(hokBreedte + 1, hoogte - controlHoogte - 1, breedte - hokBreedte - 2, controlHoogte);
		if (demoVersion)
			cp.setVisible(false);
		getContentPane().add(cp);
	
		// deze HIER!!
		getContentPane().add(tekenPanel);		
		
		
		pak = false;
		maakVorm = false;
		wisTegelEerst = false;
		aantalSs = 0;
		 
		if (transVersion)
			posBasis = new Point(85, hoogte - 85);
		else	
			posBasis = new Point(95, hoogte - 85);
		
		ss = new SchuifStuk[500];
		Point p1, p2, p3, p4;
		if (transVersion)
		{	p1 = new Point(-2, -2);
			p2 = new Point(2, -2);
			p3 = new Point(2, 2);
			p4 = new Point(-2, 2);
		}
		else
		{	p1 = new Point(-20, -20);
			p2 = new Point(20, -20);
			p3 = new Point(20, 40);
			p4 = new Point(-20, 40);
		}
		Point[] pnt = {p1, p2, p3, p4};
		
		basisv = new SchuifStuk(transVersion, 4, pnt, Color.red);
		basisvOud = new SchuifStuk(transVersion, 4, pnt, new Color(230, 230, 230));
		basisv.zetPositie(posBasis.x, posBasis.y);
		basisvOud.zetPositie(posBasis.x, posBasis.y);
		nieuwHp = new Point[50];
		aantalNieuwHp = 0;
		for (int i = 0; i < 4; i++)
		{	nieuwHp[i] = new Point(pnt[i]);
			aantalNieuwHp++;
		}
		nieuwHp[4] = new Point(pnt[0]);
		aantalNieuwHp++;
		tegelKlaar = true;
		maakCodeString();
		
		basisVormen.addElement(new SchuifStuk(transVersion, basisv.aantalPunten, basisv.punten,
				               posBasis, basisv.kleur));
		
		int n = 10 / Trans.factor;
		zeshok = new Polygon();
		zeshok.addPoint(posBasis.x + Trans.geefx(n, 0), posBasis.y + Trans.geefy(n, 0));
		zeshok.addPoint(posBasis.x + Trans.geefx(0, n), posBasis.y + Trans.geefy(0, n));
		zeshok.addPoint(posBasis.x + Trans.geefx(-n, n), posBasis.y + Trans.geefy(-n, n));
		zeshok.addPoint(posBasis.x + Trans.geefx(-n, 0), posBasis.y + Trans.geefy(-n, 0));
		zeshok.addPoint(posBasis.x + Trans.geefx(0, -n), posBasis.y + Trans.geefy(0, -n));
		zeshok.addPoint(posBasis.x + Trans.geefx(n, -n), posBasis.y + Trans.geefy(n, -n));
		
		popup = new JPopupMenu();
				
		mi = new JMenuItem(Tegels.rb.getString("menuDraaiLabel") + "  (Shift + Click)");
		mi.addActionListener(this);
		popup.add(mi);
		
		mi = new JMenuItem(Tegels.rb.getString("menuSpiegelLabel"));
		mi.addActionListener(this);
		popup.add(mi);
		
		mi = new JMenuItem(Tegels.rb.getString("menuKleurLabel") + "  (Alt + Click)");
		mi.addActionListener(this);
		popup.add(mi);
		
		popup.addSeparator();
		
		mi = new JMenuItem(Tegels.rb.getString("menuKopieerLabel"));
		mi.addActionListener(this);
		popup.add(mi);
		
		getContentPane().add(popup);
	}
	

 	void tekenOpnieuw()
	{	
 		repaint();
	}	
	
 	public void update(Graphics g)
 	{
 		paint(g);
 	}
	void vermenigvuldigPunten(double factor)
	{	for (int i = 0; i < aantalNieuwHp; i++)
		{	nieuwHp[i].x *= factor;
			nieuwHp[i].y *= factor;
		}
		basisv = new SchuifStuk(transVersion, aantalNieuwHp, nieuwHp, posBasis, Color.red);
	}

	void zetTekenen()
	{	maakVorm = true;
		//if(basisv!=null)
		//{	basisvOud = new SchuifStuk(basisv,basisv.positie.x,basisv.positie.y);
		//	basisvOud.zetKleur(new Color(230,230,230));
		//}
		tekenOpnieuw();
	}

	void zetLeggen()
	{	maakVorm = false;
		wisTegelEerst = false;
		if (tegelKlaar)
		{	basisvOud = new SchuifStuk(transVersion, aantalNieuwHp, nieuwHp, posBasis, new Color(230, 230, 230));
		
			if (!basisVormen.contains(basisv))
			{	basisVormen.addElement(
					new SchuifStuk(transVersion, basisv.aantalPunten, basisv.punten, posBasis, basisv.kleur));
				actualBasisVorm = basisVormen.size() - 1;
				cp.downButton.setEnabled(false);
				cp.upButton.setEnabled(true);
			
			}
		
		}
		tekenOpnieuw();
	}
	
	void vorigeBasisVorm()
	{	if (actualBasisVorm > 0)
		{	actualBasisVorm--;
			zetBasisVorm((SchuifStuk) basisVormen.elementAt(actualBasisVorm));
			if (actualBasisVorm == 0)
				cp.upButton.setEnabled(false);
			if (basisVormen.size() > 1)
				cp.downButton.setEnabled(true);
			tekenOpnieuw();
		}
		
	}
	
	void volgendeBasisVorm()
	{	if (actualBasisVorm < (basisVormen.size() - 1))
		{	actualBasisVorm++;
			zetBasisVorm((SchuifStuk) basisVormen.elementAt(actualBasisVorm));
			if (actualBasisVorm == (basisVormen.size() - 1))
				cp.downButton.setEnabled(false);
			if (basisVormen.size() > 1)
				cp.upButton.setEnabled(true);
			tekenOpnieuw();
		}
		
	}
	void draaiBasisvorm()
	{	if (basisv != null)
			basisv.draaiVorm();
		
		// dit ook niet doen in transverie!
		//if (basisvOud != null)
		//	basisvOud.draaiVorm();

		tekenOpnieuw();
	}
	
	void kleurBasisvorm(Color c)
	{	if (basisv != null)
			basisv.zetKleur(c);
		tekenOpnieuw();
	}
	
	void wisSs()
	{	aantalSs = 0;
		tekenOpnieuw();
	}
	
	void wisTegel()
	{	tegelKlaar = false;
		
		basisv = null;
		aantalNieuwHp = 0;
		maakCodeString();
		if (wisTegelEerst)
		{	basisvOud = null;
			wisTegelEerst = false;
		}
		else
		{	wisTegelEerst = true;
		}
		tekenOpnieuw();
	}
	
	void tekenStapTerug()
	{	if (aantalNieuwHp != 0)
		{	aantalNieuwHp--;
			tegelKlaar = false;
			maakCodeString();
			tekenOpnieuw();
		}
		if (aantalNieuwHp == 0)
		{	basisv = null;
		}
	}
	
	void maakCodeString()
	{	code = "";
		if (aantalNieuwHp != 0)
		{	for (int i = 0; i < aantalNieuwHp - 1; i++)
			{	code = code + abc[nieuwHp[i].x / 20 + 4] + Integer.toString(nieuwHp[i].y / 20 + 5);
				if (!tegelKlaar || i < aantalNieuwHp - 2) 
					code = code + ",";
			}
			if (!tegelKlaar)
				code = code + abc[nieuwHp[aantalNieuwHp - 1].x / 20 + 4] + Integer.toString(nieuwHp[aantalNieuwHp - 1].y / 20 + 5);
		}
		cp.codeveld.setText(code);
	}
	
	public void voegNieuwPuntToe(int x, int y)
	{	if (tegelKlaar)
			return;
		
		if ((aantalNieuwHp == 1 || aantalNieuwHp == 2) && x == nieuwHp[0].x && y == nieuwHp[0].y)
		{	aantalNieuwHp = 0;
			maakCodeString();
			return;
		}
		for (int i = 1; i < aantalNieuwHp; i++)
		{	if (x == nieuwHp[i].x && y == nieuwHp[i].y)
			{	aantalNieuwHp = 0;
				maakCodeString();
				return;
			}
		}
		nieuwHp[aantalNieuwHp] = new Point(x,y);
		aantalNieuwHp++;
		
		if (aantalNieuwHp > 2 && x == nieuwHp[0].x && y == nieuwHp[0].y)
		{	tegelKlaar = true;
			Point[] hp = new Point[aantalNieuwHp];
			for (int i = 0; i < aantalNieuwHp; i++)
			{	hp[i] = new Point(nieuwHp[i]);
			}
			basisv = new SchuifStuk(transVersion, aantalNieuwHp, hp, posBasis, Color.red);
			tekenOpnieuw();
			
			// toevoegen aan basisVormen mits nog niet gemaakt
//			if (!basisVormen.contains(basisv))
//				basisVormen.addElement(basisv);
			
		}
		maakCodeString();
	}
	
	public void zetBasisVorm(SchuifStuk bVorm)
	{
		aantalNieuwHp = bVorm.aantalPunten;
		for (int i = 0; i < bVorm.aantalPunten; i++)
		{	nieuwHp[i] = new Point(bVorm.punten[i]);
		}
		basisv = new SchuifStuk(transVersion, bVorm.aantalPunten, bVorm.punten, 
				                posBasis, bVorm.kleur);
		basisvOud = new SchuifStuk(transVersion, bVorm.aantalPunten, bVorm.punten, 
                				posBasis, new Color(230, 230, 230));
		
		maakCodeString();
	}
	
	
	public void mousePressed(MouseEvent e)
	{	
		
		if (demoVersion)
			return;
		
		laatstex = e.getX();
		laatstey = e.getY();
		
		if (transVersion)
		{
			if (maakVorm && zeshok.contains(e.getX(), e.getY()))
			{	int x = e.getX() - posBasis.x;
				int y = e.getY() - posBasis.y;
				
				int n = 10 / Trans.factor;
				for (int i = -n; i < n + 1; i++)
				{	for (int j = -n; j < n + 1; j++)
					{	int xp = Trans.geefx(i, j);
						int yp = Trans.geefy(i, j);
						if (Math.abs(x - xp) < 5 && Math.abs(y - yp) < 5)
						{	voegNieuwPuntToe(i, j);
							return;
						}
					}
				}
			}
		}
		else
		{	
			if (maakVorm && e.getX() < 180 && hoogte - e.getY() < 180)
			{	int x = e.getX() - posBasis.x + 200;
				int y = e.getY() - posBasis.y + 200;
				int ex = (x + 5) % 20;
				int ey = (y + 5) % 20;
				if (ex < 10 && ey < 10)
				{	voegNieuwPuntToe(x - 200 - ex + 5, y - 200 - ey + 5);
				}
				tekenOpnieuw();
				return;
			}
		}

		if (basisv != null && basisv.bevat(e.getX(), e.getY()))
		{	setCursor(new Cursor(Cursor.MOVE_CURSOR));
			pak = true;
			return;
		}
		for (int i = 0; i < aantalSs; i++)
		{	if (ss[i].bevat(e.getX(), e.getY()))
			{	setCursor(new Cursor(Cursor.MOVE_CURSOR));
				actiefSs = ss[i];
				for (int j = i; j > 0; j--)
				{	ss[j] = ss[j - 1];
				}
				ss[0] = actiefSs;
				if ((e.getModifiers() == e.BUTTON3_MASK || e.isControlDown()) && actiefSs.bevat(e.getX(), e.getY()))
				{	setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
					popup.show(this,e.getX(), e.getY());
				}
				else if ((e.isShiftDown()) && actiefSs.bevat(e.getX(), e.getY()))
				{	setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
					actiefSs.draaiVorm();
				}
				else if ((e.isAltDown()) && actiefSs.bevat(e.getX(), e.getY()))
				{	setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
					actiefSs.zetKleur(basisv.kleur);
				}
				return;
			}
		}
	}	
	public void mouseDragged(MouseEvent e)
	{	
		
		if (demoVersion)
			return;
		
		int dx = e.getX() - laatstex;
		int dy = e.getY() - laatstey;
		if (pak)
		{	ss[aantalSs] = new SchuifStuk(transVersion, basisv, posBasis.x, posBasis.y);
			actiefSs = ss[aantalSs];
			for (int j = aantalSs; j > 0; j--)
			{	ss[j] = ss[j - 1];
			}
			ss[0] = actiefSs;
			aantalSs++;
			pak = false;
		}
		if (actiefSs != null)
			actiefSs.veranderPositie(dx, dy);
		tekenOpnieuw();
		laatstex = e.getX();
		laatstey = e.getY();
	}
	
	public void mouseReleased(MouseEvent e)
	{	
		if (demoVersion)
			return;
		
		if (new Rectangle(0, hoogte - 180, 180, 180).contains(e.getX(), e.getY()) && actiefSs != null)
		{	for (int j = 1; j < aantalSs; j++)
			{	ss[j - 1] = ss[j];
			}
			aantalSs--;
		}
		if (actiefSs != null)	
			actiefSs.plaatsOpGrid();

		setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		pak = false;
		actiefSs = null;
		tekenOpnieuw();	
	}
	
	public void mouseMoved(MouseEvent e){;}
	public void mouseExited(MouseEvent e){;}
	public void mouseClicked(MouseEvent e){;}
	public void mouseEntered(MouseEvent e){;}
	
	public void actionPerformed(ActionEvent e)
	{	if (((JMenuItem) e.getSource()).getText().indexOf(Tegels.rb.getString("menuDraaiLabel")) > -1)
		{	ss[0].draaiVorm();
		}
		if (((JMenuItem) e.getSource()).getText().indexOf(Tegels.rb.getString("menuSpiegelLabel")) > -1)
		{	ss[0].spiegel();
		}
		if (((JMenuItem) e.getSource()).getText().indexOf(Tegels.rb.getString("menuKleurLabel")) > -1)
		{	ss[0].zetKleur(basisv.kleur);
		}
		if (((JMenuItem) e.getSource()).getText().indexOf(Tegels.rb.getString("menuKopieerLabel")) > -1)
		{	ss[aantalSs] = new SchuifStuk(transVersion, ss[0], ss[0].positie.x + 20 ,ss[0].positie.y - 20);
			actiefSs = ss[aantalSs];
			for (int j = aantalSs; j > 0; j--)
			{	ss[j] = ss[j - 1];
			}
			ss[0] = actiefSs;
			aantalSs++;
		}
		tekenOpnieuw();
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
	
		// decodeer de string
		Object o = StringCodeObject.decodeStringToObject(s);
		// cast
		Hashtable h = (Hashtable) o;
		
		boolean stateVersion = transVersion;
		
		if (h.containsKey("stateversion"))
			stateVersion = ((Boolean) h.get("stateversion")).booleanValue();
		
		if (stateVersion == transVersion)
		{	
		
			Vector schuifStukkenVector = new Vector();
			if (h.containsKey("schuifstukken"))
				schuifStukkenVector = (Vector) h.get("schuifstukken");
		
			aantalSs = schuifStukkenVector.size();
			for (int sCnt = 0; sCnt < aantalSs; sCnt++)
			{	ss[sCnt] = (SchuifStuk) schuifStukkenVector.elementAt(sCnt);
			}

			Vector basisVormenVector = new Vector();
			if (h.containsKey("basisvormen"))
				basisVormenVector = (Vector) h.get("basisvormen");

			basisVormen = new Vector();
			for (int bCnt = 0; bCnt < basisVormenVector.size(); bCnt++)
			{	basisVormen.addElement((SchuifStuk) basisVormenVector.elementAt(bCnt));
			}
			
			if (basisVormen.size() >= 1)
			{
				zetBasisVorm((SchuifStuk) basisVormen.elementAt(0));
				if (basisVormen.size() > 1)
					cp.downButton.setEnabled(true);
			}

		}
		
/*		
if (h == null)
{
System.out.println("set h = null");	
	return;

}
*/		
	}

	public String getState()
	{	
		// creeer de gegevens die de state bepalen
		Hashtable h = new Hashtable();
	
		boolean stateVersion = transVersion;
		
		h.put("stateversion", new Boolean(stateVersion));
		
		Vector schuifStukkenVector = new Vector();
		for (int sCnt = 0; sCnt < aantalSs; sCnt++)
		{	schuifStukkenVector.addElement(ss[sCnt]);
		}
		h.put("schuifstukken", schuifStukkenVector);
		
		Vector basisVormenVector = new Vector();
		for (int bCnt = 0; bCnt < basisVormen.size(); bCnt++)
		{	basisVormenVector.addElement((SchuifStuk) basisVormen.elementAt(bCnt));
		}
		h.put("basisvormen", basisVormenVector);
		
	    // codeer deze gegevens tot een string
	    String s = StringCodeObject.encodeObjectToString(h);

	    return s;
	}

	public double getScore()
	{	return 0.5;
	}
	
	public InteractiePanel getInteractiePanel()
	{
		return new InteractiePanelAdapter(this);
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
		DataType bType = new ScormBoolean();
		
		Parameter param = new Parameter("transversion", "versie TegelsTr", bType);
		param.setHelpText("vul in: yes of no");		
		parameters[0] = param;

		param = new Parameter("demoversion", "demo versie", bType);
		param.setHelpText("vul in: yes of no");		
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
    	
    	h.put("transversion", "no");
    	h.put("demoversion", "no");
    	
    	return h;
    }
    
	public void setSingleComponent()
	{	
		isDWOComponent = true;
		cp.fiButton.setVisible(false);
		
	}
	
}

