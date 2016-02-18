package fi.graphtool;

import java.awt.AWTEventMulticaster;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
//import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JComponent;

public class Slider extends JComponent implements MouseListener, MouseMotionListener{

	//private boolean resize;
	protected ActionListener actionListener = null;
	
	private int lengte;
	private int stand;
	private int minimum=-2;
	private int muisStartX;
	private boolean raak;
	private String naam = "";
	private double onderGrens, bovenGrens, stapGrootte;
	private int linksMarge = 5;
	private int bovenMarge = 5;
	
	public Slider(int aantalPix, int beginst)
	{	lengte = aantalPix;
		stand = beginst;
		addMouseListener(this);
		addMouseMotionListener(this);
		if(naam.length() > 0)
		{	linksMarge = 15;
			bovenMarge = 15;
		}
		setSize(lengte + 2 * linksMarge, bovenMarge + 8);
//		setSize(lengte + 30, 23);
			//}
			//else
			//	setSize(lengte + 10, 13);
			
	}
	
	public void zetGrenzen(double onderGrens, double bovenGrens)
	{
		this.onderGrens = onderGrens;
		this.bovenGrens = bovenGrens;
	}
	
	public void zetStapGrootte(double stapGrootte)
	{
		this.stapGrootte = stapGrootte;
	}
	
	public void zetMinimum(int min)
	{
		minimum = min;
	}
	
	public void zetLengte(int aantalPix) {	
		lengte = aantalPix;
		if(naam.length() > 0)
		{	linksMarge = 15;
			bovenMarge = 15;
		}
		setSize(lengte + 2 * linksMarge, bovenMarge + 8);
//			repaint();
	}
	
	public void zetNaam(String naam)
	{
		this.naam = naam;
		if(naam.length() > 0)
		{	linksMarge = 30;
			bovenMarge = 15;
		}
		setSize(lengte + 2 * linksMarge, bovenMarge + 8);
		repaint();
	}
	
	public boolean isRaak()
	{
		return raak;
	}
		
	public void paintComponent(Graphics g)
	{	g.setColor(Color.black);
		//int vertPositie = 5;
		//int horPositie = 5;
		//if(naam.length() > 0)
		//{	linksMarge = 15;
		//bovenMarge = 15;
		//}
		g.drawLine(linksMarge,bovenMarge,lengte+linksMarge,bovenMarge);
		g.setColor(Color.red);
		g.fillOval(linksMarge+stand-3, bovenMarge - 3, 6, 6);
		g.setColor(Color.black);
		g.drawOval(linksMarge+stand-3, bovenMarge - 3, 6, 6);
		if(naam.length() > 0)
		{	Font font = new Font("SansSerif", Font.PLAIN, 10);
			FontMetrics fm = getFontMetrics(font);
			double doubleStand = stand;
			double doubleLengte = lengte;
			double waarde = doubleStand/doubleLengte * (bovenGrens - onderGrens) + onderGrens;
			int aantalStappen = (int) ((bovenGrens - onderGrens)/stapGrootte);
			for(int i = 0; i < aantalStappen; i++)
			{	if(waarde - onderGrens < i * stapGrootte + stapGrootte/2)
				{	waarde = onderGrens + i * stapGrootte;
					break;
				}
			}
			if(waarde - onderGrens > (aantalStappen - 1) * stapGrootte + stapGrootte/2)
				waarde = bovenGrens;
			
			g.setFont(font);
			if(Math.round(stapGrootte) == stapGrootte)
			{
				int intWaarde = (int) Math.round(waarde);
				g.drawString(naam + "=" + intWaarde, stand + linksMarge - fm.stringWidth(naam), 10);
			}
			
			else {	
				waarde = (double) Math.round(1000*waarde)/1000;
				g.drawString(naam + "=" + waarde, stand + linksMarge - fm.stringWidth(naam), 10);
			}
			//
			//g.setFont(font);
			//String sliderString = naam +  "="  +waarde;
			//int width = fm.stringWidth(naam);
			
		}
  		
	}
	
	public void update(Graphics g)
	{	paint(g);
	}
	
	public void addActionListener(ActionListener l) 
 	{	actionListener = AWTEventMulticaster.add(actionListener,l);
 	}
 	
 	public void removeActionListener(ActionListener l)
 	{	actionListener = AWTEventMulticaster.remove(actionListener, l);
 	}
	
	public int geefStand()
	{	return stand;
	}
	
	public void zetStand(int std) {	
		if(std>lengte)stand = lengte;
		else if(std<minimum)stand = minimum;
		else stand = std;
	}
	
	public void mousePressed(MouseEvent e)
	{	raak = (new Rectangle(stand + linksMarge - 5,0,10,bovenMarge + 15)).contains(e.getX(), e.getY());
		muisStartX = e.getX();
		if (raak && actionListener != null)
		{	actionListener.actionPerformed( new ActionEvent(this, 0, "start") );
		}
		
	}
	
	public void mouseDragged(MouseEvent e)
	{	if(!raak && new Rectangle(stand + linksMarge - 5,0,10,bovenMarge + 15).contains(e.getX(), e.getY()))
		{	raak = true;
			muisStartX = e.getX();
			if (raak && actionListener != null)
			{	actionListener.actionPerformed( new ActionEvent(this, 0, "start") );
			}
		}
		if(raak)
		{	int x = e.getX();
			int dx = x - muisStartX;
			stand = stand + dx;
			if(stand>lengte) 
			{	stand = lengte;
			}
			else if(stand<minimum) 
			{	stand = minimum;
			}
			if(x<linksMarge || x>lengte+linksMarge)
			{	raak = false;
			}
			repaint();
			if (actionListener != null)
 			{	actionListener.actionPerformed( new ActionEvent(this, 0, "verschoven") );
 			}
			muisStartX = x;
			
		}
	}
	
	public void mouseReleased(MouseEvent e)
	{
		if(raak) {	
			int intStapGrootte = (int) Math.round(stapGrootte * lengte/(bovenGrens - onderGrens));
			if (intStapGrootte < 1) {
				intStapGrootte = 1;
			}
			int aantalStappen = (int) (lengte/intStapGrootte); 

// Below code is replaced by the above to overcome a divide by zero situation
//			int aantalStappen = lengte;
//			int intStapGrootte = 1;
//			if(stapGrootte != 0) {	
//				intStapGrootte = (int) Math.round(stapGrootte * lengte/(bovenGrens - onderGrens));
//				aantalStappen = (int) (lengte/intStapGrootte); // -> possible divide by zero
//			}

			stand = Math.round(stand / intStapGrootte) * intStapGrootte;
// above is a more efficient version of below code			
//			for(int i = 0; i < aantalStappen; i++) {	
//				if(stand < i * intStapGrootte + intStapGrootte/2) {	
//					stand = (int) (i * intStapGrootte);
//					break;
//				}
//			}
			if(stand > (aantalStappen - 1) * intStapGrootte + intStapGrootte/2) {
				stand = lengte;
			}
			repaint();
		}
	}
	
	
	public void mouseClicked(MouseEvent e){;}
	public void mouseExited(MouseEvent e){;}
	public void mouseEntered(MouseEvent e){;}
	public void mouseMoved(MouseEvent e){;}
}

