package fi.kladje;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.awt.image.*;
import java.io.Serializable;

public class KladjeVeld extends JPanel 
{
	static double NZERO = 1e-5d;
	
	boolean lijnen = false;
	boolean ruitjes = false;
	
	int lineDistance = 20;
	int gridSize = 20;
	
	Color lijnenKleur = new Color(150, 150, 255);
	Color ruitjesKleur = new Color(210, 210, 210);
	
	Color drawingColor = Color.black;
	Color backgroundColor = Color.white;
	//int drawingColorCode = 10;
	//int backgroundColorCode = 11;
	
//	Color[] kleuren = {Color.black, Color.white, Color.gray, Color.lightGray, Color.red,     Color.orange,
//			           Color.yellow,Color.green, Color.cyan, Color.blue,      Color.magenta, Color.pink};
	
	
	Color[] kleuren = {Color.black, Color.lightGray, Color.red, Color.orange,
	   		           Color.green, Color.cyan, Color.blue, Color.magenta};

	final int tekenen = 0;
	final int gummen = 1;
	final int lijnTekenen = 2;
	final int rechthoekTekenen = 3;
	final int cirkelTekenen = 4;
	int mouseMode = tekenen;
	Vector draggPoints = new Vector();
	
	Vector gumPunten = new Vector();
	
	Point figuurStart = null;
	Point lijnEinde = null;
	Rectangle tekenRechthoek = null;

	ColorBytes[][] pixels = null;
	int breedte, hoogte;
//	int penGrootte = 2; // oneven
	int gumGrootte = 7; // oneven
	
	Image offScreen = null;
	Graphics offGraphics = null;
	
	int maxHistories = 5;
	int numHistories = 0;
	Vector[] histories = new Vector[maxHistories + 1];

	boolean shiftPressed = false;
	
	public KladjeVeld(int w, int h)
	{
		breedte = w;
		hoogte = h;
		setSize(breedte, hoogte);
		
/*		
		pixels = new ColorBytes[breedte][hoogte];
		for (int hCnt = 0; hCnt < breedte; hCnt++)
			for (int vCnt = 0; vCnt < hoogte; vCnt++)
			{
				pixels[hCnt][vCnt] = new ColorBytes(hCnt, vCnt, backgroundColor);
			}
*/			
		
		MLMML listener = new MLMML();
		
		addMouseListener(listener);
		addMouseMotionListener(listener);
		
	}

	void addToHistory()
	{
		Vector stateVector = getState();
		
//System.out.println("add " + stateVector.size());		
		
		histories[numHistories] = stateVector;
		numHistories++;
		if (numHistories > maxHistories)
		{	for (int i = 0; i < numHistories - 1; i++)
			{	histories[i] = histories[i + 1];
			}
			numHistories--;
		}		
	}
	
	public Vector getFromHistory()
	{	if (numHistories <= 1)
			return null;
		numHistories--;
		return histories[numHistories - 1];
	}
	
	public void setSize(int w, int h)
	{
//System.out.println("kladjeVeld.setSize");		
		
		int oudeBreedte = breedte;
		int oudeHoogte = hoogte;
		breedte = w;
		hoogte = h;
		if (pixels != null)
		{
//System.out.println("pixels != null");

			ColorBytes[][] oudePixels = new ColorBytes[oudeBreedte][oudeHoogte];
			for (int hCnt = 0; hCnt < oudeBreedte; hCnt++)
				for (int vCnt = 0; vCnt < oudeHoogte; vCnt++)
				{
					oudePixels[hCnt][vCnt] = pixels[hCnt][vCnt];
				}

			pixels = new ColorBytes[breedte][hoogte];
			for (int hCnt = 0; hCnt < breedte; hCnt++)
				for (int vCnt = 0; vCnt < hoogte; vCnt++)
				{	if ((hCnt < Math.min(breedte, oudeBreedte)) && 
						(vCnt < Math.min(hoogte, oudeHoogte)))
						pixels[hCnt][vCnt] = oudePixels[hCnt][vCnt];
					else
						pixels[hCnt][vCnt] = new ColorBytes(hCnt, vCnt,backgroundColor);
				}

			offScreen = null;
			
		}
		else
		{
//System.out.println("pixels == null");			
			pixels = new ColorBytes[breedte][hoogte];
			for (int hCnt = 0; hCnt < breedte; hCnt++)
				for (int vCnt = 0; vCnt < hoogte; vCnt++)
				{
					pixels[hCnt][vCnt] = new ColorBytes(hCnt, vCnt, backgroundColor);
				}
			
		}
		
	
		super.setSize(breedte, hoogte);
		
		repaint();
		
	}

	public Vector getState()
	{
		Vector stateVector = new Vector();
		for (int hCnt = 0; hCnt < breedte; hCnt++)
			for (int vCnt = 0; vCnt < hoogte; vCnt++)
			{
				Color c = pixels[hCnt][vCnt].makeColor();
				if (!c.equals(backgroundColor))
				{
					ColorBytes newCB = new ColorBytes(pixels[hCnt][vCnt].x, pixels[hCnt][vCnt].y, 
							                          pixels[hCnt][vCnt].red, pixels[hCnt][vCnt].green, pixels[hCnt][vCnt].blue);
					//stateVector.addElement(pixels[hCnt][vCnt]);
					stateVector.addElement(newCB);
				}
			}
//System.out.println("kladjeVeld getState " + stateVector.size());		
		return stateVector;
	}
	
	public void setState(Vector stateVector)
	{
		
//System.out.println("kladjeVeld setState " + stateVector.size());


		int cnt = 0;
		for (int pCnt = 0; pCnt < stateVector.size(); pCnt++)
		{
			ColorBytes cb = (ColorBytes) stateVector.elementAt(pCnt);
			
			
			if ((cb.x < breedte) && (cb.y < hoogte))
			{	pixels[cb.x][cb.y] = cb;
				Color c = pixels[cb.x][cb.y].makeColor();
				if (!c.equals(backgroundColor))
					cnt++;
			}
		}
		
//System.out.println("kladjeVeld pp " + cnt);		
		repaint();
	}
	
	
	public void zetDrawingColor(int code)
	{
		drawingColor = kleuren[code];
		
	}
	
	public void zetLijnen(boolean b)
	{	lijnen = b;
		repaint();
	}

	public void zetRuitjes(boolean b)
	{	ruitjes = b;
		repaint();
	}
	
	public void paintComponent(Graphics g)
	{
		if (offScreen == null)
		{
			offScreen = createImage(getSize().width, getSize().height);
			offGraphics = offScreen.getGraphics();
		}
		
		
		tekenProgramma(offGraphics, true);
		

		g.setColor(backgroundColor);
		g.fillRect(0, 0, getSize().width, getSize().height);
		
		//g.setColor(Color.black);
		//g.drawRect(0, 0, getSize().width - 1, getSize().height - 1);
		
		if (lijnen)
		{
			g.setColor(lijnenKleur);
			int steps = getSize().height / lineDistance;
			for (int lCnt = 1; lCnt <= steps; lCnt++)
			{
				g.drawLine(0, lCnt * lineDistance, getSize().width - 1, lCnt * lineDistance);
			}
			
		}
		if (ruitjes)
		{
			g.setColor(ruitjesKleur);
			int vSteps = getSize().height / lineDistance;
			for (int vCnt = 1; vCnt <= vSteps; vCnt++)
			{
				g.drawLine(0, vCnt * lineDistance, getSize().width - 1, vCnt * lineDistance);
			}
			int hSteps = getSize().width / lineDistance;
			for (int hCnt = 1; hCnt <= hSteps; hCnt++)
			{
				g.drawLine(hCnt * lineDistance, 0, hCnt * lineDistance, getSize().height - 5);
			}
			
		}
		
		Graphics2D g2D = (Graphics2D) g;
		g2D.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL,RenderingHints.VALUE_STROKE_NORMALIZE);
		g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2D.setStroke(new BasicStroke(1.5f));
		
		tekenProgramma(g2D, false);
		
	}
	
	
	void tekenProgramma(Graphics g, boolean wis)
	{
		if (wis)
		{	
			g.setColor(backgroundColor);
			g.fillRect(0, 0, getSize().width, getSize().height);
		
			//g.setColor(Color.black);
			//g.drawRect(0, 0, getSize().width - 1, getSize().height - 1);
		}
		
		int tpCnt = 0;
		
		for (int hCnt = 1; hCnt < getSize().width - 1; hCnt++)
			for (int vCnt = 1; vCnt < getSize().height - 1; vCnt++)
			{
				Color c = pixels[hCnt][vCnt].makeColor();
				if (!c.equals(backgroundColor))
				{
					tekenPunt(g, hCnt, vCnt);
					tpCnt++;
				}
			}
		
//System.out.println("wis = " + wis + " tp = " + tpCnt);		
	
		g.setColor(drawingColor);		
		
		if (draggPoints.size() == 1)
		{	Point p = (Point) draggPoints.elementAt(0);
			g.drawLine(p.x, p.y, p.x, p.y);
		}
		if (draggPoints.size() > 1)
		{	Point p1 = (Point) draggPoints.elementAt(0);
			for (int pCnt = 1; pCnt < draggPoints.size(); pCnt++)
			{	Point p2 = (Point) draggPoints.elementAt(pCnt);
				g.drawLine(p1.x, p1.y, p2.x, p2.y);
				p1 = p2;
			}
			
		}
		
		if ((mouseMode == lijnTekenen) && (figuurStart != null) && (lijnEinde != null))
		{	g.drawLine(figuurStart.x, figuurStart.y, lijnEinde.x, lijnEinde.y);
			
		}
		
		if ((mouseMode == rechthoekTekenen) && (tekenRechthoek != null))
		{
			g.drawRect(tekenRechthoek.x, tekenRechthoek.y, tekenRechthoek.width, tekenRechthoek.height);
		}
		
		if ((mouseMode == cirkelTekenen) && (tekenRechthoek != null))
		{
			g.drawOval(tekenRechthoek.x, tekenRechthoek.y, tekenRechthoek.width, tekenRechthoek.height);
		}
		
	}
	
	void updatePixelArray()
	{	int w = getSize().width;
		int h = getSize().height;
		
		int[] tempPixels = new int[w * h];
		PixelGrabber pg = new PixelGrabber(offScreen, 0, 0,  w, h, tempPixels, 0, w);
		try 
		{    pg.grabPixels();
		}
		catch (InterruptedException e) 
		{   System.err.println("interrupted waiting for pixels!");
		    return;
		}
		if ((pg.getStatus() & ImageObserver.ABORT) != 0) 
		{   System.err.println("image fetch aborted or errored");
		    return;
		}
		for (int j = 0; j < h; j++) 
		{   for (int i = 0; i < w; i++) 
			{
				pixels[i][j] = handlesinglepixel(i, j, tempPixels[j * w + i]);
		    }
		}
		
		
	}	
		
	public ColorBytes handlesinglepixel(int x, int y, int pixel) 
	{
		int alpha = (pixel >> 24) & 0xff;
		int red   = (pixel >> 16) & 0xff;
		int green = (pixel >>  8) & 0xff;
		int blue  = (pixel      ) & 0xff;
			
		return new ColorBytes(x, y, red, green, blue);
	}

		
	
	void tekenPunt(Graphics g, int x, int y)
	{
		g.setColor(pixels[x][y].makeColor());
		
		g.drawLine(x, y, x, y);
		
	}
	
	void gumPunt(int x, int y)
	{
		for (int xCnt = x - gumGrootte / 2; xCnt <= x + gumGrootte / 2; xCnt++)
			for (int yCnt = y - gumGrootte / 2; yCnt <= y + gumGrootte / 2; yCnt++)
			{
				if ((xCnt >= 0) && (xCnt < getSize().width - 1) &&
					(yCnt >= 0) && (yCnt < getSize().height - 1))
				{	pixels[xCnt][yCnt].zetColor(backgroundColor);
				
				}
				
			}
	}

	void undo()
	{
		wis();
		Vector lastState = getFromHistory();
		if (lastState != null)
		{	setState(lastState);
		}

		repaint();
	}
	
	void wis()
	{	for (int xCnt = 0; xCnt < breedte; xCnt++)
			for (int yCnt = 0; yCnt < hoogte; yCnt++)
			{
				pixels[xCnt][yCnt].zetColor(backgroundColor);
			}
		repaint();
	}
	
	class MLMML extends MouseAdapter implements MouseMotionListener
	{
//		int startX, startY;
		
		public void mousePressed(MouseEvent e)
		{
			if (mouseMode == tekenen)
			{
				//pixels[e.getX()][e.getY()] = (byte) drawingColorCode;
				draggPoints.addElement(new Point(e.getX(), e.getY()));
			}
			else if (mouseMode == gummen)
			{
				gumPunt(e.getX(), e.getY());
			}
			
			else
			{
				figuurStart = new Point(e.getX(), e.getY());
			}
			repaint();
			
//			startX = e.getX();
//			startY = e.getY();
			
		}
		
		public void mouseDragged(MouseEvent e)
		{
			if (mouseMode == tekenen)
			{
				//pixels[e.getX()][e.getY()] = (byte) drawingColorCode;
				draggPoints.addElement(new Point(e.getX(), e.getY()));
			}
			else if (mouseMode == gummen)
			{
				gumPunt(e.getX(), e.getY());	
			}
			else if (mouseMode == lijnTekenen)
			{
				if (e.isShiftDown())
				{
					if ((e.getX() > figuurStart.x) && (e.getY() > figuurStart.y))
					{	
						double xZijde = (double) e.getX() - figuurStart.x;
						double yZijde = (double) e.getY() - figuurStart.y;
						int min = Math.min(e.getX() - figuurStart.x, e.getY() - figuurStart.y);
						if (yZijde > xZijde - NZERO)
						{
							if (xZijde < yZijde / 2 + NZERO)
							{
								lijnEinde = new Point(figuurStart.x, e.getY());
							}
							else
							{
								lijnEinde = new Point(figuurStart.x + min, figuurStart.y + min);
							}
						}
						else
						{
							if (yZijde > xZijde / 2 - NZERO)
							{
								lijnEinde = new Point(figuurStart.x + min, figuurStart.y + min);
							}
							else
							{
								lijnEinde = new Point(e.getX(), figuurStart.y);
							}
						}

					}	
					else if ((e.getX() > figuurStart.x) && (e.getY() < figuurStart.y))
					{	
						double xZijde = (double) e.getX() - figuurStart.x;
						double yZijde = (double) figuurStart.y - e.getY();
						int min = Math.min(e.getX() - figuurStart.x, figuurStart.y - e.getY());
						if (yZijde > xZijde + NZERO)
						{
							if (xZijde < yZijde / 2 + NZERO)
							{
								lijnEinde = new Point(figuurStart.x, e.getY());
							}
							else
							{
								lijnEinde = new Point(figuurStart.x + min, figuurStart.y - min);
							}
						}
						else
						{
							if (yZijde > xZijde / 2 - NZERO)
							{
								lijnEinde = new Point(figuurStart.x + min, figuurStart.y - min);
							}
							else
							{
								lijnEinde = new Point(e.getX(), figuurStart.y);
							}
						}

 

					}	
					else if ((e.getX() < figuurStart.x) && (e.getY() > figuurStart.y))
					{	
						double xZijde = (double) figuurStart.x - e.getX();
						double yZijde = (double) e.getY() - figuurStart.y;
						int min = Math.min(figuurStart.x - e.getX(), e.getY() - figuurStart.y);
						if (yZijde > xZijde + NZERO)
						{
							if (xZijde < yZijde / 2 + NZERO)
							{
								lijnEinde = new Point(figuurStart.x, e.getY());
							}
							else
							{
								lijnEinde = new Point(figuurStart.x - min, figuurStart.y + min);
							}
						}
						else
						{
							if (yZijde > xZijde / 2 - NZERO)
							{
								lijnEinde = new Point(figuurStart.x - min, figuurStart.y + min);
							}
							else
							{
								lijnEinde = new Point(e.getX(), figuurStart.y);
							}
						}
						
						
					}	
					else if ((e.getX() < figuurStart.x) && (e.getY() < figuurStart.y))
					{	
						int xZijde = figuurStart.x - e.getX();
						int yZijde = figuurStart.y - e.getY();
						int min = Math.min(figuurStart.x - e.getX(), figuurStart.y - e.getY());
						if (yZijde > xZijde + NZERO)
						{
							if (xZijde < yZijde / 2 + NZERO)
							{
								lijnEinde = new Point(figuurStart.x, e.getY());
							}
							else
							{
								lijnEinde = new Point(figuurStart.x - min, figuurStart.y - min);
							}
						}
						else
						{
							if (yZijde > xZijde / 2 - NZERO)
							{
								lijnEinde = new Point(figuurStart.x - min, figuurStart.y - min);
							}
							else
							{
								lijnEinde = new Point(e.getX(), figuurStart.y);
							}
						}
						        
					}
					
				}
				else
				{	
					lijnEinde = new Point(e.getX(), e.getY());
				}	
			}
			else
			{
				if (figuurStart != null)
				{
					
					if (e.isShiftDown())
					{
//System.out.println("ShiftDown");
						if ((e.getX() > figuurStart.x) && (e.getY() > figuurStart.y))
						{	
							int zijde = Math.min(e.getX() - figuurStart.x, e.getY() - figuurStart.y);
							tekenRechthoek = new Rectangle(figuurStart.x, figuurStart.y, zijde, zijde); 

						}	
						else if ((e.getX() > figuurStart.x) && (e.getY() < figuurStart.y))
						{	
							int zijde = Math.min(e.getX() - figuurStart.x, figuurStart.y - e.getY());
							tekenRechthoek = new Rectangle(figuurStart.x, e.getY(), zijde, zijde); 
 
						}	
						else if ((e.getX() < figuurStart.x) && (e.getY() > figuurStart.y))
						{	
							int zijde = Math.min(figuurStart.x - e.getX(), e.getY() - figuurStart.y);
							tekenRechthoek = new Rectangle(e.getX(), figuurStart.y, zijde, zijde);
							
						}	
						else if ((e.getX() < figuurStart.x) && (e.getY() < figuurStart.y))
						{	
							int zijde = Math.min(figuurStart.x - e.getX(), figuurStart.y - e.getY());
							tekenRechthoek = new Rectangle(e.getX(), e.getY(), zijde, zijde); 
							        
						}
					}
					else
					{	
						if ((e.getX() > figuurStart.x) && (e.getY() > figuurStart.y))
						{	tekenRechthoek = new Rectangle(figuurStart.x, figuurStart.y, 
							                           	   e.getX() - figuurStart.x, e.getY() - figuurStart.y); 
						}	
						else if ((e.getX() > figuurStart.x) && (e.getY() < figuurStart.y))
						{	tekenRechthoek = new Rectangle(figuurStart.x, e.getY(), 
								                           e.getX() - figuurStart.x, figuurStart.y - e.getY()); 
						}	
						else if ((e.getX() < figuurStart.x) && (e.getY() > figuurStart.y))
						{	tekenRechthoek = new Rectangle(e.getX(), figuurStart.y, 
													       figuurStart.x - e.getX(), e.getY() - figuurStart.y); 
						}	
						else if ((e.getX() < figuurStart.x) && (e.getY() < figuurStart.y))
						{	tekenRechthoek = new Rectangle(e.getX(), e.getY(), 
													       figuurStart.x - e.getX(), figuurStart.y - e.getY()); 
						}
					}
				}	
				
			}
			repaint();
			
//			startX = e.getX();
//			startY = e.getY();
			
		}
		
		public void mouseReleased(MouseEvent e)
		{
			if (mouseMode == tekenen)
			{	
				updatePixelArray();
				draggPoints.removeAllElements();
				//addToHistory();
			}	
			else if (mouseMode == gummen)
			{
				updatePixelArray();
				//addToHistory();
				//updatePixelArray();
			}
			else
			{	
				updatePixelArray();
				figuurStart = null;
				lijnEinde = null;
				tekenRechthoek = null;
				//addToHistory();
			}
			addToHistory();
		}
	} //MLMML
	
}


class ColorBytes implements Serializable
{	
	int x, y;
	byte red, green, blue;
	
	public ColorBytes(int x, int y, int r, int g, int b)
	{	
		this.x = x;
		this.y = y;
		
		red = new Integer(r).byteValue(); 
		green = new Integer(g).byteValue();
		blue = new Integer(b).byteValue();;
	}
	
	public ColorBytes(int x, int y, Color c)
	{
		this.x = x;
		this.y = y;
		
		red = new Integer(c.getRed()).byteValue(); 
		green = new Integer(c.getGreen()).byteValue();
		blue = new Integer(c.getBlue()).byteValue();;
	}

	public Color makeColor()
	{	int intRed = red;
		if (intRed < 0)
			intRed += 256;
		int intGreen = green;
		if (intGreen < 0)
			intGreen += 256;
		int intBlue = blue;
		if (intBlue < 0)
			intBlue += 256;
		return new Color(intRed, intGreen, intBlue);
	}
	
	public void zetColor(Color c)
	{
		red = new Integer(c.getRed()).byteValue(); 
		green = new Integer(c.getGreen()).byteValue();
		blue = new Integer(c.getBlue()).byteValue();;
		
	}
}