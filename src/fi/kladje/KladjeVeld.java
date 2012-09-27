package fi.kladje;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.awt.image.*;
import java.io.Serializable;

public class KladjeVeld extends JPanel 
{
	boolean lijnen = false;
	boolean ruitjes = false;
	
	int lineDistance = 20;
	int gridSize = 20;
	
	Color lijnenKleur = new Color(150, 150, 255);
	Color ruitjesKleur = new Color(150, 150, 255);
	
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
	int mouseMode = tekenen;
	Vector draggPoints = new Vector();

	ColorBytes[][] pixels;
	int breedte, hoogte;
//	int penGrootte = 2; // oneven
	int gumGrootte = 7; // oneven
	
	Image offScreen = null;
	Graphics offGraphics = null;
	
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

	
	public void setSize(int w, int h)
	{
System.out.println("kladjeVeld.setSize");		
		
		int oudeBreedte = breedte;
		int oudeHoogte = hoogte;
		breedte = w;
		hoogte = h;
		if (pixels != null)
		{
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
					stateVector.addElement(pixels[hCnt][vCnt]);
				}
			}
		
		return stateVector;
	}
	
	public void setState(Vector stateVector)
	{
		for (int pCnt = 0; pCnt < stateVector.size(); pCnt++)
		{
			ColorBytes cb = (ColorBytes) stateVector.elementAt(pCnt);
			pixels[cb.x][cb.y] = cb;
		}
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
		
		g.setColor(Color.black);
		g.drawRect(0, 0, getSize().width - 1, getSize().height - 1);
		
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
			g.setColor(lijnenKleur);
			int vSteps = getSize().height / lineDistance;
			for (int vCnt = 1; vCnt <= vSteps; vCnt++)
			{
				g.drawLine(0, vCnt * lineDistance, getSize().width - 1, vCnt * lineDistance);
			}
			int hSteps = getSize().width / lineDistance;
			for (int hCnt = 1; hCnt <= hSteps; hCnt++)
			{
				g.drawLine(hCnt * lineDistance, 0, hCnt * lineDistance, getSize().width - 1);
			}
			
		}
		
		Graphics2D g2D = (Graphics2D) g;
		g2D.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL,RenderingHints.VALUE_STROKE_NORMALIZE);
		g2D.setStroke(new BasicStroke(2f));
		
		tekenProgramma(g2D, false);
		
	}
	
	
	void tekenProgramma(Graphics g, boolean outline)
	{
		if (outline)
		{	
			g.setColor(backgroundColor);
			g.fillRect(0, 0, getSize().width, getSize().height);
		
			g.setColor(Color.black);
			g.drawRect(0, 0, getSize().width - 1, getSize().height - 1);
		}
		
		
		for (int hCnt = 1; hCnt < getSize().width - 1; hCnt++)
			for (int vCnt = 1; vCnt < getSize().height - 1; vCnt++)
			{
				Color c = pixels[hCnt][vCnt].makeColor();
				if (!c.equals(backgroundColor))
				{
					tekenPunt(g, hCnt, vCnt);
				}
			}
		
	
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
					pixels[xCnt][yCnt].zetColor(backgroundColor);
			}
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
			else // mouseMode == gummen
			{
				gumPunt(e.getX(), e.getY());
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
			else // mouseMode == gummen
			{
				gumPunt(e.getX(), e.getY());	
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
			}	
		}
	}
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