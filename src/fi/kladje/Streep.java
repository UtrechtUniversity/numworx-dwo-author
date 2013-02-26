package fi.kladje;

import java.awt.*;
import java.util.*;

public class Streep 
{
	Color kleur;
	int[] puntenX, puntenY;
	int bbFactor = 4;
	Polygon bb;
	
	public Streep(Color c, Vector punten)
	{	kleur = c;
		puntenX = new int[punten.size()];
		puntenY = new int[punten.size()];
		for (int pCnt = 0; pCnt < punten.size(); pCnt++)
		{	Point pt = (Point) punten.elementAt(pCnt);
			puntenX[pCnt] = pt.x;
			puntenY[pCnt] = pt.y;
		}
		
		maakBBs();
	}
	
	public Streep(Color c, int[] ptX, int[] ptY)
	{	kleur = c;
		puntenX = ptX;
		puntenY = ptY;
		
		maakBBs();
	}

	public void maakBBs()
	{
		bb = new Polygon();
		
		if (puntenX.length == 1)
		{	bb.addPoint(puntenX[0] - bbFactor, puntenY[0] - bbFactor);
			bb.addPoint(puntenX[0] + bbFactor, puntenY[0] - bbFactor);
			bb.addPoint(puntenX[0] + bbFactor, puntenY[0] + bbFactor);
			bb.addPoint(puntenX[0] - bbFactor, puntenY[0] + bbFactor);
		}
		if (puntenX.length > 1)
		{  
			
			// for loop 1
			for (int pCnt = 1; pCnt < puntenX.length; pCnt++)
			{	
				int fromX = puntenX[pCnt - 1];
				int fromY = puntenY[pCnt - 1];
				int toX = puntenX[pCnt];
				int toY = puntenY[pCnt];
				// richtingsvector
				double rX = toX - fromX;
				double rY = toY - fromY;
				// normaalvector
				double nx = 0;
				double ny = 0;
				boolean normal1 = true;
				if (Math.abs(rX) > Math.abs(rY))
				{	nx = rY;
					ny = -rX;
					normal1 = false;
				}
				else
				{	nx = -rY;
					ny = rX;
				}
				// eenheids normaalvector
				double nl = Math.sqrt(nx * nx + ny * ny);
				if (nl > 0)
				{	 
					if (normal1)
					{	
						// eerste punt
						double px = fromX + nx * bbFactor / nl;
						double py = fromY + ny * bbFactor / nl;
						bb.addPoint((int) Math.round(px), (int) Math.round(py)); 
						// 	tweede punt
						px = toX + nx * bbFactor / nl;
						py = toY + ny * bbFactor / nl;
						if (normal1)
							bb.addPoint((int) Math.round(px), (int) Math.round(py));
						}
					else
					{	
//					 	vierde punt
						double px = fromX - nx * bbFactor / nl;
						double py = fromY - ny * bbFactor / nl;
						bb.addPoint((int) Math.round(px), (int) Math.round(py));
						
						//derde punt
						px = toX - nx * bbFactor / nl;
						py = toY - ny * bbFactor / nl;
						bb.addPoint((int) Math.round(px), (int) Math.round(py));

						// 	vierde punt
						px = fromX - nx * bbFactor / nl;
						py = fromY - ny * bbFactor / nl;
						bb.addPoint((int) Math.round(px), (int) Math.round(py));
					}	
				}	
		
			}
		
			// for loop 2
			for (int pCnt = puntenX.length - 1; pCnt > 0; pCnt--)
			{	
				int fromX = puntenX[pCnt - 1];
				int fromY = puntenY[pCnt - 1];
				int toX = puntenX[pCnt];
				int toY = puntenY[pCnt];
				// richtingsvector
				double rX = toX - fromX;
				double rY = toY - fromY;
				// normaalvector
				double nx = 0;
				double ny = 0;
				boolean normal1 = true;
				if (Math.abs(rX) > Math.abs(rY))
				{	nx = rY;
					ny = -rX;
					normal1 = false;
				}
				else
				{	nx = -rY;
					ny = rX;
				}
				// eenheids normaalvector
				double nl = Math.sqrt(nx * nx + ny * ny);
				if (nl > 0)
				{	
					if (!normal1)
					{	
						// 	tweede punt
						double px = toX + nx * bbFactor / nl;
						double py = toY + ny * bbFactor / nl;
						bb.addPoint((int) Math.round(px), (int) Math.round(py));
					
						// eerste punt
						px = fromX + nx * bbFactor / nl;
						py = fromY + ny * bbFactor / nl;
						bb.addPoint((int) Math.round(px), (int) Math.round(py));
					}
					else
					{	
						
						// derde punt
						double px = toX - nx * bbFactor / nl;
						double py = toY - ny * bbFactor / nl;
						bb.addPoint((int) Math.round(px), (int) Math.round(py));
						// 	vierde punt
						px = fromX - nx * bbFactor / nl;
						py = fromY - ny * bbFactor / nl;
						bb.addPoint((int) Math.round(px), (int) Math.round(py));
					}
				}	
		
			}
		}
	}
	
	public Hashtable getState()
	{	Hashtable h = new Hashtable();
		
		h.put("kleur", kleur);
		h.put("puntenX", puntenX);
		h.put("puntenY", puntenY);
	
		return h;
	}
	
	public static Streep setState(Hashtable h)
	{
		Color kleur = Color.black;
		int[] puntenX = new int[0];
		int[] puntenY = new int[0];
		
		if (h.containsKey("kleur"))
			kleur = (Color) h.get("kleur");
		if (h.containsKey("puntenX"))
			puntenX = (int[]) h.get("puntenX");
		if (h.containsKey("puntenY"))
			puntenY = (int[]) h.get("puntenY");
		
		return new Streep(kleur, puntenX, puntenY);
	}
	
	
	public void teken(Graphics g)
	{
		g.setColor(kleur);		
		
		if (puntenX.length == 1)
		{	g.drawLine(puntenX[0], puntenY[0], puntenX[0], puntenY[0]);
		}
		if (puntenX.length > 1)
		{	for (int pCnt = 1; pCnt < puntenX.length; pCnt++)
			{	g.drawLine(puntenX[pCnt - 1], puntenY[pCnt - 1], puntenX[pCnt], puntenY[pCnt]);
			}
			
		}
		
		// testing
		//tekenBB(g);

	}
	
	public void tekenBB(Graphics g)
	{
		Graphics2D g2D = (Graphics2D) g;
		float[] dash = new float[2];
		dash[0] = 2;
		dash[1] = 2;
		g2D.setStroke(new BasicStroke(1.0f, 2, 0, 10.0f, dash, 0.0f));
		g.setColor(KladjeVeld.bbColor);

		g.drawPolygon(bb);
				
		g2D.setStroke(new BasicStroke(1.5f, 2, 0, 10.0f, null, 0.0f));
		
	}
	
	public boolean bbContains(int x, int y)
	{
		
		return bb.contains(x, y);
	}
	
	public void translate(int dx, int dy)
	{
		for (int pCnt = 0; pCnt < puntenX.length; pCnt++)
		{	puntenX[pCnt] += dx;
			puntenY[pCnt] += dy;
		}
		
		bb.translate(dx, dy);
	}
	
}
class Lijn
{
	Color kleur;
	int fromX, fromY, toX, toY;
	int bbFactor = 4;
	Polygon bb;
	
	public Lijn(Color c, int fromX, int fromY, int toX, int toY)
	{
		kleur = c;
		this.fromX = fromX;
		this.fromY = fromY;
		this.toX = toX;
		this.toY = toY;
		
		bb = new Polygon();
		
		// richtingsvector
		double rX = toX - fromX;
		double rY = toY - fromY;
		// normaalvector
		double nx = 0;
		double ny = 0;
		if (Math.abs(rX) > Math.abs(rY))
		{	nx = rY;
			ny = -rX;
		}
		else
		{	nx = -rY;
			ny = rX;
		}
		// eenheids normaalvector
		double nl = Math.sqrt(nx * nx + ny * ny);
		if (nl > 0)
		{	// eerste punt
			double px = fromX + nx * bbFactor / nl;
			double py = fromY + ny * bbFactor / nl;
			bb.addPoint((int) Math.round(px), (int) Math.round(py)); 
			// tweede punt
			px = toX + nx * bbFactor / nl;
			py = toY + ny * bbFactor / nl;
			bb.addPoint((int) Math.round(px), (int) Math.round(py));
			// derde punt
			px = toX - nx * bbFactor / nl;
			py = toY - ny * bbFactor / nl;
			bb.addPoint((int) Math.round(px), (int) Math.round(py));
			// vierde punt
			px = fromX - nx * bbFactor / nl;
			py = fromY - ny * bbFactor / nl;
			bb.addPoint((int) Math.round(px), (int) Math.round(py));
		
		
		}
		else
		{
			bb.addPoint(fromX - bbFactor, fromY - bbFactor);
			bb.addPoint(fromX + bbFactor, fromY - bbFactor);
			bb.addPoint(fromX + bbFactor, fromY + bbFactor);
			bb.addPoint(fromX - bbFactor, fromY + bbFactor);
		}
	}

	public Hashtable getState()
	{	Hashtable h = new Hashtable();
		
		h.put("kleur", kleur);
		h.put("fromX", new Integer(fromX));
		h.put("fromY", new Integer(fromY));
		h.put("toX", new Integer(toX));
		h.put("toY", new Integer(toY));
	
		return h;
	}
	
	public static Lijn setState(Hashtable h)
	{
		Color kleur = Color.black;
		int fromX = 0; 
		int fromY = 0;
		int toX = 0;
		int toY = 0;
		
		if (h.containsKey("kleur"))
			kleur = (Color) h.get("kleur");
		if (h.containsKey("fromX"))
			fromX = ((Integer) h.get("fromX")).intValue();
		if (h.containsKey("fromY"))
			fromY = ((Integer) h.get("fromY")).intValue();

		if (h.containsKey("toX"))
			toX = ((Integer) h.get("toX")).intValue();
		if (h.containsKey("toY"))
			toY = ((Integer) h.get("toY")).intValue();
		
		return new Lijn(kleur, fromX, fromY, toX, toY);
	}
	
	public void teken(Graphics g)
	{
		g.setColor(kleur);
		g.drawLine(fromX, fromY, toX, toY);
		
		// testing
		//tekenBB(g);
	}

	public void tekenBB(Graphics g)
	{
		Graphics2D g2D = (Graphics2D) g;
		float[] dash = new float[2];
		dash[0] = 2;
		dash[1] = 2;
		g2D.setStroke(new BasicStroke(1.0f, 2, 0, 10.0f, dash, 0.0f));
		g.setColor(KladjeVeld.bbColor);
		
		g.drawPolygon(bb);
				
		g2D.setStroke(new BasicStroke(1.5f, 2, 0, 10.0f, null, 0.0f));
		
	}
	
	public boolean bbContains(int x, int y)
	{
		return bb.contains(x, y);
	}

	public void translate(int dx, int dy)
	{
		fromX += dx;
		fromY += dy;
		toX += dx; 
		toY += dy;
		
		bb.translate(dx, dy);
	}	

}
class Rechthoek
{	Color kleur;
	int topLeftX, topLeftY, breedte, hoogte;
	int bbFactor = 4;
	Rectangle outerBB;
	Rectangle innerBB;
	
	public Rechthoek(Color c, int x, int y, int w, int h)
	{
		kleur = c;
		topLeftX = x;
		topLeftY = y;
		breedte = w;
		hoogte = h;
		
		outerBB = new Rectangle(topLeftX - bbFactor, topLeftY - bbFactor, 
							    breedte + 2 * bbFactor, hoogte + 2 * bbFactor);
		innerBB = new Rectangle(topLeftX + bbFactor, topLeftY + bbFactor, 
			    			    breedte - 2 * bbFactor, hoogte - 2 * bbFactor);
		
		
	}

	public Hashtable getState()
	{	Hashtable h = new Hashtable();
		
		h.put("kleur", kleur);
		h.put("topLeftX", new Integer(topLeftX));
		h.put("topLeftY", new Integer(topLeftY));
		h.put("breedte", new Integer(breedte));
		h.put("hoogte", new Integer(hoogte));
	
		return h;
	}
	
	public static Rechthoek setState(Hashtable h)
	{
		Color kleur = Color.black;
		int topLeftX = 0; 
		int topLeftY = 0;
		int breedte = 0;
		int hoogte = 0;
		
		if (h.containsKey("kleur"))
			kleur = (Color) h.get("kleur");
		if (h.containsKey("topLeftX"))
			topLeftX = ((Integer) h.get("topLeftX")).intValue();
		if (h.containsKey("topLeftY"))
			topLeftY = ((Integer) h.get("topLeftY")).intValue();

		if (h.containsKey("breedte"))
			breedte = ((Integer) h.get("breedte")).intValue();
		if (h.containsKey("hoogte"))
			hoogte = ((Integer) h.get("hoogte")).intValue();
		
		return new Rechthoek(kleur, topLeftX, topLeftY, breedte, hoogte);
	}
	
	public void teken(Graphics g)
	{
		g.setColor(kleur);
		g.drawRect(topLeftX, topLeftY, breedte, hoogte);
		
		// testing
		//tekenBB(g);
	}

	public void tekenBB(Graphics g)
	{
		Graphics2D g2D = (Graphics2D) g;
		float[] dash = new float[2];
		dash[0] = 2;
		dash[1] = 2;
		g2D.setStroke(new BasicStroke(1.0f, 2, 0, 10.0f, dash, 0.0f));
		g.setColor(KladjeVeld.bbColor);
		
		g.drawRect(outerBB.x, outerBB.y, outerBB.width, outerBB.height);
		g.drawRect(innerBB.x, innerBB.y, innerBB.width, innerBB.height);
		
		g2D.setStroke(new BasicStroke(1.5f, 2, 0, 10.0f, null, 0.0f));

	}
	
	public boolean bbContains(int x, int y)
	{
		return outerBB.contains(x, y) && !innerBB.contains(x, y);
	}

	public void translate(int dx, int dy)
	{
		topLeftX += dx;
		topLeftY += dy;
		outerBB.translate(dx, dy);
		innerBB.translate(dx, dy);
	}	
	
}
class Ellips
{	Color kleur;
	int topLeftX, topLeftY, breedte, hoogte;
	int bbFactor = 4;
	Rectangle outerBB, innerBB;
	
	public Ellips(Color c, int x, int y, int w, int h)
	{
		kleur = c;
		topLeftX = x;
		topLeftY = y;
		breedte = w;
		hoogte = h;
		
		outerBB = new Rectangle(topLeftX - bbFactor, topLeftY - bbFactor, 
			    breedte + 2 * bbFactor, hoogte + 2 * bbFactor);
		innerBB = new Rectangle(topLeftX + bbFactor, topLeftY + bbFactor, 
			    breedte - 2 * bbFactor, hoogte - 2 * bbFactor);
		
	}
	
	public Hashtable getState()
	{	Hashtable h = new Hashtable();
		
		h.put("kleur", kleur);
		h.put("topLeftX", new Integer(topLeftX));
		h.put("topLeftY", new Integer(topLeftY));
		h.put("breedte", new Integer(breedte));
		h.put("hoogte", new Integer(hoogte));
	
		return h;
	}
	
	public static Ellips setState(Hashtable h)
	{
		Color kleur = Color.black;
		int topLeftX = 0; 
		int topLeftY = 0;
		int breedte = 0;
		int hoogte = 0;
		
		if (h.containsKey("kleur"))
			kleur = (Color) h.get("kleur");
		if (h.containsKey("topLeftX"))
			topLeftX = ((Integer) h.get("topLeftX")).intValue();
		if (h.containsKey("topLeftY"))
			topLeftY = ((Integer) h.get("topLeftY")).intValue();

		if (h.containsKey("breedte"))
			breedte = ((Integer) h.get("breedte")).intValue();
		if (h.containsKey("hoogte"))
			hoogte = ((Integer) h.get("hoogte")).intValue();
		
		return new Ellips(kleur, topLeftX, topLeftY, breedte, hoogte);
	}

	public void teken(Graphics g)
	{
		g.setColor(kleur);
		g.drawOval(topLeftX, topLeftY, breedte, hoogte);
		
		// testing
		//tekenBB(g);
	}
	
	public void tekenBB(Graphics g)
	{
		Graphics2D g2D = (Graphics2D) g;
		float[] dash = new float[2];
		dash[0] = 2;
		dash[1] = 2;
		g2D.setStroke(new BasicStroke(1.0f, 2, 0, 10.0f, dash, 0.0f));
		g.setColor(KladjeVeld.bbColor);
		
		g.drawOval(outerBB.x, outerBB.y, outerBB.width, outerBB.height);
		g.drawOval(innerBB.x, innerBB.y, innerBB.width, innerBB.height);
		
		g2D.setStroke(new BasicStroke(1.5f, 2, 0, 10.0f, null, 0.0f));

	}
	
	boolean ellipsContains(int x, int y, Rectangle r)
	{ 
		double a = ((double) r.width) / 2;
		double b = ((double) r.height) / 2;
		double cx = r.x + a;
		double cy = r.y + b;
		double px = ((double) x) - cx;
		double py = ((double) y) - cy;
		
		//px^2/a^2+py^2/b^2<1
		
		double inside = px*px/(a*a) + py*py/(b*b);
		
		boolean contains = inside < 1;
		return contains;
	}
	
	public boolean bbContains(int x, int y)
	{
		boolean outer = ellipsContains(x, y, outerBB);
		boolean inner = ellipsContains(x, y, innerBB);
		
//System.out.println("outer " + outer);
//System.out.println("inner " + inner);
		
		return outer && !inner;
	}

	public void translate(int dx, int dy)
	{
		topLeftX += dx;
		topLeftY += dy;
		outerBB.translate(dx, dy);
		innerBB.translate(dx, dy);
		
	}	

}

class TekstElement
{
	Color kleur;
	String tekst;
	int xPos, yPos;
	int bbFactor = 4;
	Rectangle bb;
	
	public TekstElement(Color c, String t, int x, int y)
	{
		kleur = c;
		tekst = new String(t);
		xPos = x;
		yPos = y;
		
		int width = KladjeVeld.tekstFM.stringWidth(tekst) + 2 * bbFactor;
		int height = KladjeVeld.tekstFM.getHeight() + 2 * bbFactor;
		bb = new Rectangle(x - bbFactor, y - bbFactor - KladjeVeld.tekstFM.getAscent(), width, height);
	}

	public Hashtable getState()
	{	Hashtable h = new Hashtable();
		
		h.put("kleur", kleur);
		h.put("tekst", new String(tekst));
		h.put("xPos", new Integer(xPos));
		h.put("yPos", new Integer(yPos));
	
		return h;
	}

	public static TekstElement setState(Hashtable h)
	{
		Color kleur = Color.black;
		String tekst = new String("");
		int xPos = 0;
		int yPos = 0;
		
		if (h.containsKey("kleur"))
			kleur = (Color) h.get("kleur");
		if (h.containsKey("tekst"))
			tekst = (String) h.get("tekst");
		if (h.containsKey("xPos"))
			xPos = ((Integer) h.get("xPos")).intValue();
		if (h.containsKey("yPos"))
			yPos = ((Integer) h.get("yPos")).intValue();
		
		return new TekstElement(kleur, tekst, xPos, yPos);
	}
	
	public void teken(Graphics g)
	{
		g.setFont(KladjeVeld.tekstFont);
		g.setColor(kleur);
		g.drawString(tekst, xPos, yPos);
		
		// testing
		//tekenBB(g);
	}

	public void tekenBB(Graphics g)
	{
		Graphics2D g2D = (Graphics2D) g;
		float[] dash = new float[2];
		dash[0] = 2;
		dash[1] = 2;
		g2D.setStroke(new BasicStroke(1.0f, 2, 0, 10.0f, dash, 0.0f));
		g.setColor(KladjeVeld.bbColor);

		g.drawRect(bb.x, bb.y, bb.width, bb.height);
				
		g2D.setStroke(new BasicStroke(1.5f, 2, 0, 10.0f, null, 0.0f));

	}
	
	public boolean bbContains(int x, int y)
	{
		
		return bb.contains(x, y);
	}

	public void translate(int dx, int dy)
	{
		xPos += dx;
		yPos += dy;
		
		bb.translate(dx, dy);
	}	
	
}