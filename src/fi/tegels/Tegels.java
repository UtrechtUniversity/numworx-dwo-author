package fi.tegels;

import java.awt.*;
import java.util.*;
import java.awt.event.*;
//import java.applet.Applet;
import fi.beans.mainframe.*;

import javax.swing.*;
/**
 * @author Peter Boon
 */

public class Tegels extends JApplet implements MouseListener, MouseMotionListener, ActionListener
{	
	protected static ResourceBundle rb;
	private ControlPanel cp;
	TekenPanel tekenPanel;
	private String langArg;
	
	int breedte, hoogte;
//	private Image im;
// 	private Graphics imGr;
	
	int aantalSs;
	private int laatstex = 0;
	private int laatstey = 0;
	
	SchuifStuk[] ss;
	SchuifStuk actiefSs, basisv, basisvOud;
//	private Image plaatje, plaatjeBasis;
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
	// toon de transversie (het vroegere applet TegelsTr
	// default false
	static boolean transVersion = false;
	
	int hokBreedte = 180;
	int controlHoogte = 70;//60;

	public static void main(String[] args)    
	{	int width = 710;
        int height = 500;
		Tegels g = new Tegels();
		MainFrame mf = new MainFrame(g,width, height);
		mf.setTitle("Tegels");
		mf.pack();
		mf.show();
		mf.setSize(width, height);
		
	}
	
	public void init()
	{	hoogte = getSize().height;
		breedte = getSize().width;
		
		getContentPane().setLayout(null);
		
		//getContentPane().setBackground(null);
		//setBackground(null);
		
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
		
		tekenPanel = new TekenPanel(this);
		tekenPanel.setBounds(0, 0, breedte, hoogte);
//		getContentPane().add(tekenPanel);
		
		cp = new ControlPanel(this);
		cp.setLayout(null);
		//cp.setBounds(181, hoogte - 61, breedte - 182, 60);
		cp.setBounds(hokBreedte + 1, hoogte - controlHoogte - 1, breedte - hokBreedte - 2, controlHoogte);
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
		
		basisv = new SchuifStuk(4, pnt, Color.red);
		basisvOud = new SchuifStuk(4, pnt, new Color(230, 230, 230));
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
	
//	public void paint(Graphics g)
//  	{ 	
			
/*		
		if (im == null)
		{	im = createImage(breedte, hoogte);
  			imGr = im.getGraphics();
			tekenOpImage(imGr);
		}
*/   

//		paintComponents(g);
//		tekenOpImage(g);
//  	}
	 
  	public void tekenOpImage(Graphics g)
  	{ 	
  		
  		g.setColor(Color.white);
  		g.setColor(Color.yellow);
    	g.fillRect(0, 0, breedte, hoogte - controlHoogte - 2);
  		//g.fillRect(0, 0, breedte, hoogte - 2);
		tekenprogramma(g);
		
		
		g.setColor(Color.black);
		g.drawRect(0, 0, breedte - 1, hoogte - 1);
		// lijn boven control panel 
		//gIm.drawLine(181, hoogte - 62, breedte - 1, hoogte - 62);
		g.drawLine(hokBreedte + 1, hoogte - controlHoogte - 2, breedte - 1, hoogte - controlHoogte - 2);
		
		//super.paint(g);
	}
	
 	void tekenOpnieuw()
	{	
/* 		
 		tekenOpImage();
		Graphics g = getGraphics();
		g.drawImage(im, 0, 0, null);
*/		
 		repaint();
	}	
	
 	public void update(Graphics g)
 	{
 		paint(g);
 	}
	
	public void tekenprogramma(Graphics g)
	{	tekenStukken(g);
		tekenHok(g);
		if (!maakVorm && basisv != null)
			tekenStapel(g);
		
		if (actiefSs != null)
			tekenSs(actiefSs, g);
		
		if (maakVorm)
		{	tekenRoosterHok(g);
			tekenPunten(g);
			tekenLijnen(g);
		}
		
	}
	
	void tekenStapel(Graphics g)
	{	tekenSs(new SchuifStuk(basisv, basisv.positie.x - 3, basisv.positie.y - 3), g);
		if (!transVersion)
			tekenSs(basisv, g);
	}
	
	void tekenHok(Graphics gIm)
	{	gIm.setColor(Color.lightGray);
		//gIm.fillRect(0, hoogte - 181, 180, 180);
		gIm.fillRect(0, hoogte - hokBreedte - 1, hokBreedte, hokBreedte);
		gIm.setColor(Color.black);
		//gIm.drawRect(0, hoogte - 181, 180, 180);
		gIm.drawRect(0, hoogte - hokBreedte - 1, hokBreedte, hokBreedte);
	}
	
	void tekenRoosterHok(Graphics gIm)
	{	
		if (transVersion)
		{
			gIm.setColor(Color.white);
			gIm.fillPolygon(zeshok);
			gIm.setColor(Color.black);
			gIm.drawPolygon(zeshok);
			
			if (basisvOud != null)
			{	gIm.setColor(basisvOud.kleur);
				gIm.fillPolygon(basisvOud.pol);
			}
			
			gIm.setColor(Color.black);
			int n = 10 / Trans.factor;
			for (int i = -n; i < n+1; i++)
			{	for (int j = -n; j < n + 1; j++)
				{	int x = Trans.geefx(i, j);
					int y = Trans.geefy(i, j);
					if (Math.abs(i + j) <= n)
						gIm.drawLine(85 + x, hoogte - 85 - y, 85 + x, hoogte - 85 - y);
				}
			}
						
		}
		else
		{	
			gIm.setColor(Color.white);
			gIm.fillRect(15, hoogte - 165, 160, 160);
		
			if (basisvOud != null)
			{	gIm.setColor(basisvOud.kleur);
				gIm.fillPolygon(basisvOud.pol);
			}
		
			gIm.setColor(Color.black);
			for (int j = 0; j < 9; j++)	
				gIm.drawLine(15, hoogte - 5 - 20 * j, 175, hoogte - 5 - 20 * j);
			for (int j = 0; j < 9; j++)	
				gIm.drawLine(15 + 20 * j, hoogte - 5, 15 + 20*j, hoogte - 165);
		
			for (int i = 0; i < 9; i++)	
			{	gIm.drawString(abc[i], 13 + 20 * i, hoogte - 168);
			}
			for (int i = 0; i < 9 ; i++)	
			{	gIm.drawString(Integer.toString(i + 1), 5, hoogte - 162 + 20 * i);
			}
		}
	}
	
	void tekenStukken(Graphics g)
	{	for(int i = aantalSs - 1; i > -1; i--)
		{	tekenSs(ss[i], g);
		}
	}	
	
	void tekenSs(SchuifStuk s, Graphics gIm)
	{	gIm.setColor(s.kleur);
		gIm.fillPolygon(s.pol);
		gIm.setColor(Color.black);
		gIm.drawPolygon(s.pol);
	}
	
	void tekenPunten(Graphics gIm)
	{	
		if (transVersion)
		{	for (int i = 0; i < aantalNieuwHp; i++)
			{	int x = Trans.geefx(nieuwHp[i].x, nieuwHp[i].y);
				int y = Trans.geefy(nieuwHp[i].x, nieuwHp[i].y);
			   
				gIm.fillOval(posBasis.x + x - 3, posBasis.y + y - 3, 6, 6);
			}
		}
		else
		{	
			for (int i = 0; i < aantalNieuwHp; i++)
			{	gIm.fillOval(posBasis.x + nieuwHp[i].x - 3, posBasis.y + nieuwHp[i].y - 3, 6, 6);
			}
		}
	}
	
	void vermenigvuldigPunten(double factor)
	{	for (int i = 0; i < aantalNieuwHp; i++)
		{	nieuwHp[i].x *= factor;
			nieuwHp[i].y *= factor;
		}
		basisv = new SchuifStuk(aantalNieuwHp, nieuwHp, posBasis, Color.red);
	}
	
	void tekenLijnen(Graphics gIm)
	{	
		if (transVersion)
		{	if (aantalNieuwHp > 1)
			{	gIm.setColor(Color.red);
			for (int i = 0; i < aantalNieuwHp - 1; i++)
			{	int x = Trans.geefx(nieuwHp[i].x, nieuwHp[i].y);
				int y = Trans.geefy(nieuwHp[i].x, nieuwHp[i].y);
				int xn = Trans.geefx(nieuwHp[i + 1].x, nieuwHp[i + 1].y);
				int yn = Trans.geefy(nieuwHp[i + 1].x, nieuwHp[i + 1].y);
				gIm.drawLine(posBasis.x + xn , posBasis.y + yn ,posBasis.x + x, posBasis.y + y);
			}
			gIm.setColor(Color.black);
	}
			
		}
		else
		{
			if (aantalNieuwHp > 1)
			{	gIm.setColor(Color.red);
				for (int i = 0; i < aantalNieuwHp - 1; i++)
				{	gIm.drawLine(posBasis.x + nieuwHp[i + 1].x ,posBasis.y + nieuwHp[i + 1].y ,
								 posBasis.x + nieuwHp[i].x, posBasis.y + nieuwHp[i].y);
				}
				gIm.setColor(Color.black);
			}
		}
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
		{	basisvOud = new SchuifStuk(aantalNieuwHp, nieuwHp, posBasis, new Color(230, 230, 230));
		}
		tekenOpnieuw();
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
			basisv = new SchuifStuk(aantalNieuwHp, hp, posBasis, Color.red);
			tekenOpnieuw();
		}
		maakCodeString();
	}
	
	public void mousePressed(MouseEvent e)
	{	laatstex = e.getX();
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
	{	int dx = e.getX() - laatstex;
		int dy = e.getY() - laatstey;
		if (pak)
		{	ss[aantalSs] = new SchuifStuk(basisv, posBasis.x, posBasis.y);
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
	{	if (new Rectangle(0, hoogte - 180, 180, 180).contains(e.getX(), e.getY()) && actiefSs != null)
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
		{	ss[aantalSs] = new SchuifStuk(ss[0], ss[0].positie.x + 20 ,ss[0].positie.y - 20);
			actiefSs = ss[aantalSs];
			for (int j = aantalSs; j > 0; j--)
			{	ss[j] = ss[j - 1];
			}
			ss[0] = actiefSs;
			aantalSs++;
		}
		tekenOpnieuw();
	}
}

