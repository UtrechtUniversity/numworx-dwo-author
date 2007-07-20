package fi.tegels;

import java.awt.Polygon;
import java.awt.*;
import java.util.*;
import java.awt.event.*;
import fi.tegels.text.*;
import java.applet.Applet;
import fi.beans.copyright.*;
import fi.beans.mainframe.*;


/**
 * @author Peter Boon
 */

public class Tegels extends Applet implements MouseListener, MouseMotionListener, ActionListener
{	
	protected static ResourceBundle rb;
	private ControlPanel cp;
	private String langArg;
	
	private int breedte,hoogte;
	private Image im ;
  	private Graphics gIm ;
	
	private int aantalSs;
	private int laatstex = 0;
	private int laatstey = 0;
	
	private SchuifStuk[] ss;
	private SchuifStuk actiefSs,basisv, basisvOud ;
	private Image plaatje, plaatjeBasis;
	private Point posBasis;
	
	private int aantalNieuwHp;
	private Point[] nieuwHp;

	private boolean pak, maakVorm, tegelKlaar, wisTegelEerst;
	
	String[] abc = {"A","B","C","D","E","F","G","H","I"};
	String code;
	
	PopupMenu popup;
	MenuItem mi;

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
		
		setLayout(null);
		
		addMouseListener(this);
		addMouseMotionListener(this);
		
		String langArg = getParameter("language");
		if ( langArg == null) langArg = "nl";
		Locale language = new Locale (langArg, "");
		rb = ResourceBundle.getBundle("fi.tegels.text.Text",language);
		
		cp = new ControlPanel(this);
		cp.setLayout(null);
		cp.setBounds(181,hoogte-61,breedte-182,60);
		add(cp);
		
		pak = false;
		maakVorm = false;
		wisTegelEerst = false;
		aantalSs = 0;
		 
		posBasis = new Point(95,hoogte-85);
		
		ss = new SchuifStuk[500];
		Point p1 = new Point(-20,-20);
		Point p2 = new Point(20,-20);
		Point p3 = new Point(20,40);
		Point p4 = new Point(-20,40);
		Point[] pnt = {p1,p2,p3,p4};
		
		basisv = new SchuifStuk(4,pnt,Color.red);
		basisvOud = new SchuifStuk(4,pnt,new Color(230,230,230));
		basisv.zetPositie(posBasis.x,posBasis.y);
		basisvOud.zetPositie(posBasis.x,posBasis.y);
		nieuwHp = new Point[50];
		aantalNieuwHp = 0;
		for(int i=0 ; i<4 ; i++)
		{	nieuwHp[i] = new Point(pnt[i]);
			aantalNieuwHp++;
		}
		nieuwHp[4] = new Point(pnt[0]);
		aantalNieuwHp++;
		tegelKlaar = true;
		maakCodeString();
		
		popup = new PopupMenu();
				
		mi = new MenuItem(Tegels.rb.getString("menuDraaiLabel") + "  (Shift + Click)");
		mi.addActionListener(this);
		popup.add(mi);
		
		mi = new MenuItem(Tegels.rb.getString("menuSpiegelLabel"));
		mi.addActionListener(this);
		popup.add(mi);
		
		mi = new MenuItem(Tegels.rb.getString("menuKleurLabel") + "  (Alt + Click)");
		mi.addActionListener(this);
		popup.add(mi);
		
		popup.addSeparator();
		
		mi = new MenuItem(Tegels.rb.getString("menuKopieerLabel"));
		mi.addActionListener(this);
		popup.add(mi);
		
		add(popup);
	}
	
	public void paint(Graphics g)
  	{ 	if(im==null)
		{	im = createImage(breedte,hoogte);
  			gIm = im.getGraphics();
			tekenOpImage();
		}
    	g.drawImage(im, 0, 0, null);
  	}
	 
  	public void tekenOpImage()
  	{ 	gIm.setColor(Color.white);
    	gIm.fillRect(0, 0, breedte, hoogte);
		tekenprogramma();
		gIm.setColor(Color.black);
		gIm.drawRect(0, 0, breedte-1, hoogte-1);
		gIm.drawLine(181,hoogte-62,breedte-1,hoogte-62);
	}
	
 	void tekenOpnieuw()
	{	tekenOpImage();
		Graphics g = getGraphics();
		g.drawImage(im, 0, 0, null);
	}	
	
	public void tekenprogramma()
	{	tekenStukken();
		tekenHok();
		if(!maakVorm && basisv!=null)tekenStapel();
		
		if(actiefSs!=null)tekenSs(actiefSs);
		
		if(maakVorm)
		{	tekenRoosterHok();
			tekenPunten();
			tekenLijnen();
		}
		
	}
	
	void tekenStapel()
	{	tekenSs(new SchuifStuk(basisv,basisv.positie.x-3,basisv.positie.y-3));
		tekenSs(basisv);
	}
	
	void tekenHok()
	{	gIm.setColor(Color.lightGray);
		gIm.fillRect(0,hoogte-181,180,180);
		gIm.setColor(Color.black);
		gIm.drawRect(0,hoogte-181,180,180);
	}
	
	void tekenRoosterHok()
	{	gIm.setColor(Color.white);
		gIm.fillRect(15,hoogte-165,160,160);
		
		if(basisvOud!=null)
		{	gIm.setColor(basisvOud.kleur);
			gIm.fillPolygon(basisvOud.pol);
		}
		
		gIm.setColor(Color.black);
		for(int j=0 ; j<9 ; j++)	gIm.drawLine(15,hoogte-5-20*j,175,hoogte-5-20*j);
		for(int j=0 ; j<9 ; j++)	gIm.drawLine(15+20*j,hoogte-5,15+20*j,hoogte-165);
		
		for(int i=0 ; i<9 ; i++)	
		{	gIm.drawString(abc[i],13+20*i,hoogte-168);
		}
		for(int i=0 ; i<9 ; i++)	
		{	gIm.drawString(Integer.toString(i+1),5,hoogte-162+20*i);
		}
	}
	
	void tekenStukken()
	{	for(int i=aantalSs-1 ; i>-1 ; i--)
		{	tekenSs(ss[i]);
		}
	}	
	
	void tekenSs(SchuifStuk s)
	{	gIm.setColor(s.kleur);
		gIm.fillPolygon(s.pol);
		gIm.setColor(Color.black);
		gIm.drawPolygon(s.pol);
	}
	
	void tekenPunten()
	{	for(int i=0 ; i<aantalNieuwHp ; i++)
		{	gIm.fillOval(posBasis.x+nieuwHp[i].x-3,posBasis.y+nieuwHp[i].y-3,6,6 );
		}
	}
	
	void tekenLijnen()
	{	if(aantalNieuwHp>1)
		{	gIm.setColor(Color.red);
			for(int i=0 ; i<aantalNieuwHp-1 ; i++)
			{	gIm.drawLine(posBasis.x+nieuwHp[i+1].x ,posBasis.y+nieuwHp[i+1].y ,posBasis.x+nieuwHp[i].x,  posBasis.y+nieuwHp[i].y);
			}
			gIm.setColor(Color.black);
		}
	}
	
	void zetTekenen()
	{	maakVorm=true;
		//if(basisv!=null)
		//{	basisvOud = new SchuifStuk(basisv,basisv.positie.x,basisv.positie.y);
		//	basisvOud.zetKleur(new Color(230,230,230));
		//}
		tekenOpnieuw();
	}
	
	void zetLeggen()
	{	maakVorm=false;
		wisTegelEerst = false;
		if(tegelKlaar)
		{	basisvOud = new SchuifStuk(aantalNieuwHp,nieuwHp,posBasis,new Color(230,230,230));
		}
		tekenOpnieuw();
	}
	
	void draaiBasisvorm()
	{	if(basisv!=null)basisv.draaiVorm();
		tekenOpnieuw();
	}
	
	void kleurBasisvorm(Color c)
	{	if(basisv!=null)basisv.zetKleur(c);
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
		if(wisTegelEerst)
		{	basisvOud = null;
			wisTegelEerst = false;
		}
		else
		{	wisTegelEerst = true;
		}
		tekenOpnieuw();
	}
	
	void tekenStapTerug()
	{	if(aantalNieuwHp!=0)
		{	aantalNieuwHp--;
			tegelKlaar = false;
			maakCodeString();
			tekenOpnieuw();
		}
		if(aantalNieuwHp==0)
		{	basisv = null;
		}
	}
	
	void maakCodeString()
	{	code = "";
		if(aantalNieuwHp!=0)
		{	for(int i=0 ; i<aantalNieuwHp-1 ; i++)
			{	code = code + abc[nieuwHp[i].x/20+4]+Integer.toString(nieuwHp[i].y/20+5);
				if(!tegelKlaar || i<aantalNieuwHp-2)code = code + ",";
			}
			if(!tegelKlaar)code = code + abc[nieuwHp[aantalNieuwHp-1].x/20+4]+Integer.toString(nieuwHp[aantalNieuwHp-1].y/20+5);
		}
		cp.codeveld.setText(code);
	}
	
	public void voegNieuwPuntToe(int x,int y)
	{	if(tegelKlaar)return;
		
		if((aantalNieuwHp==1 || aantalNieuwHp==2) && x==nieuwHp[0].x && y==nieuwHp[0].y)
		{	aantalNieuwHp=0;
			maakCodeString();
			return;
		}
		for(int i=1 ; i<aantalNieuwHp ; i++)
		{	if(x==nieuwHp[i].x && y==nieuwHp[i].y)
			{	aantalNieuwHp=0;
				maakCodeString();
				return;
			}
		}
		nieuwHp[aantalNieuwHp] = new Point(x,y);
		aantalNieuwHp++;
		
		if(aantalNieuwHp>2 && x==nieuwHp[0].x && y==nieuwHp[0].y)
		{	tegelKlaar = true;
			Point[] hp = new Point[aantalNieuwHp];
			for(int i=0 ; i<aantalNieuwHp ; i++)
			{	hp[i] = new Point(nieuwHp[i]);
			}
			basisv = new SchuifStuk(aantalNieuwHp,hp,posBasis,Color.red);
			tekenOpnieuw();
		}
		maakCodeString();
	}
	
	public void mousePressed(MouseEvent e)
	{	laatstex = e.getX();
		laatstey = e.getY();
		if(maakVorm && e.getX()< 180 && hoogte-e.getY() < 180)
		{	int x = e.getX()-posBasis.x+200;
			int y = e.getY()-posBasis.y+200;
			int ex = (x+5)%20;
			int ey = (y+5)%20;
			if(ex<10 && ey<10)
			{	voegNieuwPuntToe(x-200-ex+5 , y-200-ey+5);
			}
			tekenOpnieuw();
			return;
		}
		if(basisv!=null && basisv.bevat(e.getX(),e.getY()))
		{	setCursor(new Cursor(Cursor.MOVE_CURSOR));
			pak = true;
			return;
		}
		for(int i=0 ; i<aantalSs ; i++)
		{	if(ss[i].bevat(e.getX(),e.getY()))
			{	setCursor(new Cursor(Cursor.MOVE_CURSOR));
				actiefSs = ss[i];
				for(int j=i ; j>0 ; j--)
				{	ss[j] = ss[j-1];
				}
				ss[0] = actiefSs;
				if((e.getModifiers()== e.BUTTON3_MASK || e.isControlDown()) && actiefSs.bevat(e.getX(),e.getY()))
				{	setCursor(new Cursor(Cursor.DEFAULT_CURSOR ));
					popup.show(this,e.getX(),e.getY());
				}
				else if((e.isShiftDown()) && actiefSs.bevat(e.getX(),e.getY()))
				{	setCursor(new Cursor(Cursor.DEFAULT_CURSOR ));
					actiefSs.draaiVorm();
				}
				else if((e.isAltDown()) && actiefSs.bevat(e.getX(),e.getY()))
				{	setCursor(new Cursor(Cursor.DEFAULT_CURSOR ));
					actiefSs.zetKleur(basisv.kleur);
				}
				return;
			}
		}
	}	
	public void mouseDragged(MouseEvent e)
	{	int dx = e.getX() - laatstex;
		int dy =  e.getY() - laatstey;
		if(pak)
		{	ss[aantalSs] = new SchuifStuk(basisv,posBasis.x,posBasis.y);
			actiefSs = ss[aantalSs];
			for(int j=aantalSs ; j>0 ; j--)
				{	ss[j] = ss[j-1];
				}
				ss[0] = actiefSs;
			aantalSs++;
			pak = false;
			
		}
		if(actiefSs != null)actiefSs.veranderPositie(dx,dy);
		tekenOpnieuw();
		laatstex = e.getX();
		laatstey = e.getY();
	}
	
	public void mouseReleased(MouseEvent e)
	{	if(new Rectangle(0,hoogte-180,180,180).contains(e.getX(),e.getY()) && actiefSs != null)
		{	for(int j=1 ; j<aantalSs ; j++)
				{	ss[j-1] = ss[j];
				}
			aantalSs--;
		}
		if(actiefSs!=null)	actiefSs.plaatsOpGrid();

		setCursor(new Cursor(Cursor.DEFAULT_CURSOR ));
		pak = false;
		actiefSs = null;
		tekenOpnieuw();	
	}
	
	public void mouseMoved(MouseEvent e){;}
	public void mouseExited(MouseEvent e){;}
	public void mouseClicked(MouseEvent e){;}
	public void mouseEntered(MouseEvent e){;}
	
	public void actionPerformed(ActionEvent e)
	{	if(((MenuItem)e.getSource()).getLabel().indexOf(Tegels.rb.getString("menuDraaiLabel")) > -1)
		{	ss[0].draaiVorm();
		}
		if(((MenuItem)e.getSource()).getLabel().indexOf(Tegels.rb.getString("menuSpiegelLabel")) > -1)
		{	ss[0].spiegel();
		}
		if(((MenuItem)e.getSource()).getLabel().indexOf(Tegels.rb.getString("menuKleurLabel")) > -1)
		{	ss[0].zetKleur(basisv.kleur);
		}
		if(((MenuItem)e.getSource()).getLabel().indexOf(Tegels.rb.getString("menuKopieerLabel")) > -1)
		{	ss[aantalSs] = new SchuifStuk(ss[0],ss[0].positie.x+20 ,ss[0].positie.y-20);
			actiefSs = ss[aantalSs];
			for(int j=aantalSs ; j>0 ; j--)
			{	ss[j] = ss[j-1];
			}
			ss[0] = actiefSs;
			aantalSs++;
		}
		tekenOpnieuw();
	}
}

