package fi.kladje;

import java.awt.*;
import java.util.*;
import java.awt.geom.*;

public class Streep 
{
	Color kleur;
	int[] puntenX, puntenY;
	int bbFactor = 4;
	Polygon bb;
	double cx = 0, cy = 0;
	double rotation = 0;
	
	public Streep(Color c, Vector punten)
	{	kleur = c;
		puntenX = new int[punten.size()];
		puntenY = new int[punten.size()];
		for (int pCnt = 0; pCnt < punten.size(); pCnt++)
		{	Point pt = (Point) punten.elementAt(pCnt);
			puntenX[pCnt] = pt.x;
			puntenY[pCnt] = pt.y;
			
			cx += puntenX[pCnt];
			cy += puntenY[pCnt];
		}
		
		cx /= puntenX.length;
		cy /= puntenY.length;
		
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

	public void rotate(double rotateStep)
	{	rotation += rotateStep;
	}
	
	public void scale(double scaleStep)
	{
		for (int pCnt = 0; pCnt < puntenX.length; pCnt++)
		{	
			puntenX[pCnt] = (int) Math.round(scaleStep * puntenX[pCnt] + (1 - scaleStep) * cx);
			puntenY[pCnt] = (int) Math.round(scaleStep * puntenY[pCnt] + (1 - scaleStep) * cy);
		}
		
		maakBBs();
		
	}
	
	public Hashtable getState()
	{	Hashtable h = new Hashtable();
		
		h.put("kleur", kleur);
		h.put("puntenX", puntenX);
		h.put("puntenY", puntenY);
		h.put("rotation", new Double(rotation));
	
		return h;
	}
	
	public static Streep setState(Hashtable h)
	{
		Color kleur = Color.black;
		int[] puntenX = new int[0];
		int[] puntenY = new int[0];
		double rotation = 0;
		
		if (h.containsKey("kleur"))
			kleur = (Color) h.get("kleur");
		if (h.containsKey("puntenX"))
			puntenX = (int[]) h.get("puntenX");
		if (h.containsKey("puntenY"))
			puntenY = (int[]) h.get("puntenY");

		if (h.containsKey("rotation"))
			rotation = ((Double) h.get("rotation")).doubleValue();
		
		Streep streep = new Streep(kleur, puntenX, puntenY);
		streep.rotation = rotation;
		
		return streep;
	}
	
	
	public void teken(Graphics2D g)
	{
		AffineTransform oldAT = g.getTransform();
		AffineTransform at = g.getTransform();

		at.rotate(rotation, cx, cy);
		
		g.setTransform(at);
		
		g.setColor(kleur);		
		
		if (puntenX.length == 1)
		{	g.drawLine(puntenX[0], puntenY[0], puntenX[0], puntenY[0]);
		}
		if (puntenX.length > 1)
		{	for (int pCnt = 1; pCnt < puntenX.length; pCnt++)
			{	g.drawLine(puntenX[pCnt - 1], puntenY[pCnt - 1], puntenX[pCnt], puntenY[pCnt]);
			}
			
		}
		
		g.setTransform(oldAT);
	}
	
	public void tekenBB(Graphics2D g)
	{
		AffineTransform oldAT = g.getTransform();
		AffineTransform at = g.getTransform();

		at.rotate(rotation, cx, cy);
		
		g.setTransform(at);

		float[] dash = new float[2];
		dash[0] = 2;
		dash[1] = 2;
		g.setStroke(new BasicStroke(1.0f, 2, 0, 10.0f, dash, 0.0f));
		g.setColor(KladjeVeld.bbColor);

		g.drawPolygon(bb);
				
		g.setStroke(new BasicStroke(1.5f, 2, 0, 10.0f, null, 0.0f));
		
		g.setTransform(oldAT);		
	}

	public int inverseTransformX(int x, int y)
	{
		double rotX = Math.cos(-rotation) * (x - cx) - Math.sin(- rotation) * (y - cy);
		int rx = (int) Math.round(cx + rotX);
		
		return rx;
	}

	public int inverseTransformY(int x, int y)
	{
		double rotY = Math.sin(-rotation) * (x - cx) + Math.cos(- rotation) * (y - cy);
		int ry = (int) Math.round(cy + rotY);
		
		return ry;
	}
	
	public boolean bbContains(int x, int y)
	{
		int rx = inverseTransformX(x, y);
		int ry = inverseTransformY(x, y);

		return bb.contains(rx, ry);
	}
	
	public void translate(int dx, int dy)
	{
		for (int pCnt = 0; pCnt < puntenX.length; pCnt++)
		{	puntenX[pCnt] += dx;
			puntenY[pCnt] += dy;
		}

		cx += dx;
		cy += dy;

		bb.translate(dx, dy);
	}
	
	public boolean isContainedIn(Rectangle r)
	{
		if (r == null)
			return false;		
		
		boolean isContainedIn = true;
		
		for (int pCnt = 0; pCnt < bb.npoints; pCnt++)
		{
			int bbX = inverseTransformX(bb.xpoints[pCnt], bb.ypoints[pCnt]);
			int bbY = inverseTransformY(bb.xpoints[pCnt], bb.ypoints[pCnt]);
			
			isContainedIn = isContainedIn && r.contains(bbX, bbY);
			//isContainedIn = isContainedIn && r.contains(bb.xpoints[pCnt], bb.ypoints[pCnt]);
		}
		
		return isContainedIn;
	}
}
class Lijn
{
	Color kleur;
	int fromX, fromY, toX, toY;
	int bbFactor = 4;
	Polygon bb;
	double cx, cy;
	double rotation = 0;
	
	public Lijn(Color c, int fromX, int fromY, int toX, int toY)
	{
		kleur = c;
		this.fromX = fromX;
		this.fromY = fromY;
		this.toX = toX;
		this.toY = toY;

		cx = ((double) fromX + (double) toX) / 2;
		cy = ((double) fromY + (double) toY) / 2;
		
		makeBB();
	}
	
	public void makeBB()
	{
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

	public void rotate(double rotateStep)
	{	rotation += rotateStep;
	}
	
	public void scale(double scaleStep)
	{	
		fromX = (int) Math.round(scaleStep * fromX + (1 - scaleStep) * cx);
		fromY = (int) Math.round(scaleStep * fromY + (1 - scaleStep) * cy);
		toX = (int) Math.round(scaleStep * toX + (1 - scaleStep) * cx);
		toY = (int) Math.round(scaleStep * toY + (1 - scaleStep) * cy);
		
		makeBB();
		
	}
	
	public Hashtable getState()
	{	Hashtable h = new Hashtable();
		
		h.put("kleur", kleur);
		h.put("fromX", new Integer(fromX));
		h.put("fromY", new Integer(fromY));
		h.put("toX", new Integer(toX));
		h.put("toY", new Integer(toY));
		h.put("rotation", new Double(rotation));
	
		return h;
	}
	
	public static Lijn setState(Hashtable h)
	{
		Color kleur = Color.black;
		int fromX = 0; 
		int fromY = 0;
		int toX = 0;
		int toY = 0;
		double rotation = 0;
		
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

		if (h.containsKey("rotation"))
			rotation = ((Double) h.get("rotation")).doubleValue();
		
		Lijn lijn = new Lijn(kleur, fromX, fromY, toX, toY);
		lijn.rotation = rotation;
		
		return lijn;
	}
	
	public void teken(Graphics2D g)
	{
		AffineTransform oldAT = g.getTransform();
		AffineTransform at = g.getTransform();

		at.rotate(rotation, cx, cy);
		
		g.setTransform(at);
		
		g.setColor(kleur);
		g.drawLine(fromX, fromY, toX, toY);
		
		g.setTransform(oldAT);
	}

	public void tekenBB(Graphics2D g)
	{
		AffineTransform oldAT = g.getTransform();
		AffineTransform at = g.getTransform();

		at.rotate(rotation, cx, cy);
		
		g.setTransform(at);

		float[] dash = new float[2];
		dash[0] = 2;
		dash[1] = 2;
		g.setStroke(new BasicStroke(1.0f, 2, 0, 10.0f, dash, 0.0f));
		g.setColor(KladjeVeld.bbColor);
		
		g.drawPolygon(bb);
				
		g.setStroke(new BasicStroke(1.5f, 2, 0, 10.0f, null, 0.0f));
		
		g.setTransform(oldAT);
		
	}

	public int inverseTransformX(int x, int y)
	{
		double rotX = Math.cos(-rotation) * (x - cx) - Math.sin(- rotation) * (y - cy);
		int rx = (int) Math.round(cx + rotX);
		
		return rx;
	}

	public int inverseTransformY(int x, int y)
	{
		double rotY = Math.sin(-rotation) * (x - cx) + Math.cos(- rotation) * (y - cy);
		int ry = (int) Math.round(cy + rotY);
		
		return ry;
	}
	
	public boolean bbContains(int x, int y)
	{
		int rx = inverseTransformX(x, y);
		int ry = inverseTransformY(x, y);
		
		return bb.contains(rx, ry);
	}

	public void translate(int dx, int dy)
	{
		fromX += dx;
		fromY += dy;
		toX += dx; 
		toY += dy;
		cx += dx;
		cy += dy;
		
		bb.translate(dx, dy);
	}	

	public boolean isContainedIn(Rectangle r)
	{
		if (r == null)
			return false;
		
		boolean isContainedIn = true;
		
		for (int pCnt = 0; pCnt < bb.npoints; pCnt++)
		{
			int bbX = inverseTransformX(bb.xpoints[pCnt], bb.ypoints[pCnt]);
			int bbY = inverseTransformY(bb.xpoints[pCnt], bb.ypoints[pCnt]);
			
			isContainedIn = isContainedIn && r.contains(bbX, bbY);
			//isContainedIn = isContainedIn && r.contains(bb.xpoints[pCnt], bb.ypoints[pCnt]);
		}
		
		return isContainedIn;
	}	
}
class Rechthoek
{	Color kleur;
	int topLeftX, topLeftY, breedte, hoogte;
	int bbFactor = 4;
	Rectangle outerBB;
	Rectangle innerBB;
	double cx, cy;
	double rotation = 0;
	
	public Rechthoek(Color c, int x, int y, int w, int h)
	{
		kleur = c;
		topLeftX = x;
		topLeftY = y;
		breedte = w;
		hoogte = h;
		
		cx = topLeftX + ((double) breedte) / 2;
		cy = topLeftY + ((double) hoogte) / 2;
		
		makeBB();
		
	}

	public void makeBB()
	{
		outerBB = new Rectangle(topLeftX - bbFactor, topLeftY - bbFactor, 
			    breedte + 2 * bbFactor, hoogte + 2 * bbFactor);
		innerBB = new Rectangle(topLeftX + bbFactor, topLeftY + bbFactor, 
			    breedte - 2 * bbFactor, hoogte - 2 * bbFactor);
		
	}
	
	public void rotate(double rotateStep)
	{	rotation += rotateStep;
	}
	
	public void scale(double scaleStep)
	{	
		topLeftX = (int) Math.round(scaleStep * topLeftX + (1 - scaleStep) * cx);
		topLeftY = (int) Math.round(scaleStep * topLeftY + (1 - scaleStep) * cy);
		breedte = (int) Math.round(scaleStep * breedte);
		hoogte = (int) Math.round(scaleStep * hoogte);
		
		makeBB();
		
	}
	
	
	public Hashtable getState()
	{	Hashtable h = new Hashtable();
		
		h.put("kleur", kleur);
		h.put("topLeftX", new Integer(topLeftX));
		h.put("topLeftY", new Integer(topLeftY));
		h.put("breedte", new Integer(breedte));
		h.put("hoogte", new Integer(hoogte));
		h.put("rotation", new Double(rotation));
	
		return h;
	}
	
	public static Rechthoek setState(Hashtable h)
	{
		Color kleur = Color.black;
		int topLeftX = 0; 
		int topLeftY = 0;
		int breedte = 0;
		int hoogte = 0;
		double rotation = 0;
		
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
		
		if (h.containsKey("rotation"))
			rotation = ((Double) h.get("rotation")).doubleValue();
		
		Rechthoek rechthoek = new Rechthoek(kleur, topLeftX, topLeftY, breedte, hoogte);
		rechthoek.rotation = rotation;
		
		return rechthoek;
	}
	
	public void teken(Graphics2D g)
	{
		AffineTransform oldAT = g.getTransform();
		AffineTransform at = g.getTransform();

		at.rotate(rotation, cx, cy);
		
		g.setTransform(at);
		
		g.setColor(kleur);
		g.drawRect(topLeftX, topLeftY, breedte, hoogte);

		g.setTransform(oldAT);
	}

	public void tekenBB(Graphics2D g)
	{
		AffineTransform oldAT = g.getTransform();
		AffineTransform at = g.getTransform();

		at.rotate(rotation, cx, cy);
		
		g.setTransform(at);
		
		float[] dash = new float[2];
		dash[0] = 2;
		dash[1] = 2;
		g.setStroke(new BasicStroke(1.0f, 2, 0, 10.0f, dash, 0.0f));
		g.setColor(KladjeVeld.bbColor);
		
		g.drawRect(outerBB.x, outerBB.y, outerBB.width, outerBB.height);
		g.drawRect(innerBB.x, innerBB.y, innerBB.width, innerBB.height);
		
		g.setStroke(new BasicStroke(1.5f, 2, 0, 10.0f, null, 0.0f));
		
		g.setTransform(oldAT);
		
	}
	
	public int inverseTransformX(int x, int y)
	{
		double rotX = Math.cos(-rotation) * (x - cx) - Math.sin(- rotation) * (y - cy);
		int rx = (int) Math.round(cx + rotX);
		
		return rx;
	}

	public int inverseTransformY(int x, int y)
	{
		double rotY = Math.sin(-rotation) * (x - cx) + Math.cos(- rotation) * (y - cy);
		int ry = (int) Math.round(cy + rotY);
		
		return ry;
	}
	
	public boolean bbContains(int x, int y)
	{
		int rx = inverseTransformX(x, y);
		int ry = inverseTransformY(x, y);
		
		return outerBB.contains(rx, ry) && !innerBB.contains(rx, ry);
	}

	public void translate(int dx, int dy)
	{
		topLeftX += dx;
		topLeftY += dy;
		outerBB.translate(dx, dy);
		innerBB.translate(dx, dy);
		cx += dx;
		cy += dy;
	}	

	public boolean isContainedIn(Rectangle r)
	{
		if (r == null)
			return false;

		int toBBx = inverseTransformX(outerBB.x, outerBB.y);
		int toBBy = inverseTransformY(outerBB.x, outerBB.y);
		int toBBx2 = inverseTransformX(outerBB.x + outerBB.width, outerBB.y + outerBB.height);
		int toBBy2 = inverseTransformY(outerBB.x + outerBB.width, outerBB.y + outerBB.height);
		
		boolean isContainedIn = r.contains(toBBx, toBBy) &&	r.contains(toBBx2, toBBy2);		
		
		return isContainedIn;
	}
	
}
class Ellips
{	Color kleur;
	int topLeftX, topLeftY, breedte, hoogte;
	int bbFactor = 4;
	Rectangle outerBB, innerBB;
	double cx, cy;
	double rotation = 0;
	
	public Ellips(Color c, int x, int y, int w, int h)
	{
		kleur = c;
		topLeftX = x;
		topLeftY = y;
		breedte = w;
		hoogte = h;
		
		cx = topLeftX + ((double) breedte) / 2;
		cy = topLeftY + ((double) hoogte) / 2;
		
		makeBB();
		
	}

	public void makeBB()
	{
		outerBB = new Rectangle(topLeftX - bbFactor, topLeftY - bbFactor, 
			    breedte + 2 * bbFactor, hoogte + 2 * bbFactor);
		innerBB = new Rectangle(topLeftX + bbFactor, topLeftY + bbFactor, 
			    breedte - 2 * bbFactor, hoogte - 2 * bbFactor);
		
	}
	
	public void rotate(double rotateStep)
	{	rotation += rotateStep;
	}
	
	public void scale(double scaleStep)
	{	
		topLeftX = (int) Math.round(scaleStep * topLeftX + (1 - scaleStep) * cx);
		topLeftY = (int) Math.round(scaleStep * topLeftY + (1 - scaleStep) * cy);
		breedte = (int) Math.round(scaleStep * breedte);
		hoogte = (int) Math.round(scaleStep * hoogte);
		
		makeBB();
		
	}
	
	
	public Hashtable getState()
	{	Hashtable h = new Hashtable();
		
		h.put("kleur", kleur);
		h.put("topLeftX", new Integer(topLeftX));
		h.put("topLeftY", new Integer(topLeftY));
		h.put("breedte", new Integer(breedte));
		h.put("hoogte", new Integer(hoogte));
		h.put("rotation", new Double(rotation));
	
		return h;
	}
	
	public static Ellips setState(Hashtable h)
	{
		Color kleur = Color.black;
		int topLeftX = 0; 
		int topLeftY = 0;
		int breedte = 0;
		int hoogte = 0;
		double rotation = 0;
		
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

		if (h.containsKey("rotation"))
			rotation = ((Double) h.get("rotation")).doubleValue();
		
		Ellips ellips = new Ellips(kleur, topLeftX, topLeftY, breedte, hoogte);
		ellips.rotation = rotation;
		
		return ellips;
	}

	public void teken(Graphics2D g)
	{
		AffineTransform oldAT = g.getTransform();
		AffineTransform at = g.getTransform();

		at.rotate(rotation, cx, cy);
		
		g.setTransform(at);
		
		g.setColor(kleur);
		g.drawOval(topLeftX, topLeftY, breedte, hoogte);
		
		g.setTransform(oldAT);	
	}
	
	public void tekenBB(Graphics2D g)
	{
		AffineTransform oldAT = g.getTransform();
		AffineTransform at = g.getTransform();

		at.rotate(rotation, cx, cy);
		
		g.setTransform(at);

		float[] dash = new float[2];
		dash[0] = 2;
		dash[1] = 2;
		g.setStroke(new BasicStroke(1.0f, 2, 0, 10.0f, dash, 0.0f));
		g.setColor(KladjeVeld.bbColor);
		
		g.drawOval(outerBB.x, outerBB.y, outerBB.width, outerBB.height);
		g.drawOval(innerBB.x, innerBB.y, innerBB.width, innerBB.height);
		
		g.setStroke(new BasicStroke(1.5f, 2, 0, 10.0f, null, 0.0f));

		g.setTransform(oldAT);
	}

	public int inverseTransformX(int x, int y)
	{
		double rotX = Math.cos(-rotation) * (x - cx) - Math.sin(- rotation) * (y - cy);
		int rx = (int) Math.round(cx + rotX);
		
		return rx;
	}

	public int inverseTransformY(int x, int y)
	{
		double rotY = Math.sin(-rotation) * (x - cx) + Math.cos(- rotation) * (y - cy);
		int ry = (int) Math.round(cy + rotY);
		
		return ry;
	}
	
	boolean ellipsContains(int x, int y, Rectangle r)
	{
		
		int rx = inverseTransformX(x, y);
		int ry = inverseTransformY(x, y);
		
		double a = ((double) r.width) / 2;
		double b = ((double) r.height) / 2;
		double cx = r.x + a;
		double cy = r.y + b;
		double px = ((double) rx) - cx;
		double py = ((double) ry) - cy;
		
		//px^2/a^2+py^2/b^2<1
		
		double inside = px*px/(a*a) + py*py/(b*b);
		
		boolean contains = inside < 1;
		return contains;
	}
	
	public boolean bbContains(int x, int y)
	{
		boolean outer = ellipsContains(x, y, outerBB);
		boolean inner = ellipsContains(x, y, innerBB);
		
		return outer && !inner;
	}

	public void translate(int dx, int dy)
	{
		topLeftX += dx;
		topLeftY += dy;
		outerBB.translate(dx, dy);
		innerBB.translate(dx, dy);
		cx += dx;
		cy += dy;
		
	}	

	public boolean isContainedIn(Rectangle r)
	{
		if (r == null)
			return false;
		
		int toBBx = inverseTransformX(outerBB.x, outerBB.y);
		int toBBy = inverseTransformY(outerBB.x, outerBB.y);
		int toBBx2 = inverseTransformX(outerBB.x + outerBB.width, outerBB.y + outerBB.height);
		int toBBy2 = inverseTransformY(outerBB.x + outerBB.width, outerBB.y + outerBB.height);
		
		boolean isContainedIn = r.contains(toBBx, toBBy) &&	r.contains(toBBx2, toBBy2);		
		
		return isContainedIn;
	}
	
}

class TekstElement
{
	Color kleur;
	String tekst;
	int xPos, yPos;
	int breedte, hoogte, ascent;
	int bbFactor = 4;
	Rectangle bb;
	double cx, cy;
	double rotation = 0;
	double scaleX = 1;
	double scaleY = 1;
	int tekstX, tekstY;
	
	public TekstElement(Color c, String t, int x, int y)
	{
		kleur = c;
		tekst = new String(t);
		xPos = x;
		yPos = y;
		tekstX = x;
		tekstY = y;

		breedte = KladjeVeld.tekstFM.stringWidth(tekst);
		hoogte = KladjeVeld.tekstFM.getHeight();
		ascent = KladjeVeld.tekstFM.getAscent();
				
		cx = xPos + ((double) breedte) / 2;
		cy = yPos + ((double) hoogte) / 2;
		
		makeBB();
	
	}

	public void makeBB()
	{
		bb = new Rectangle(xPos - bbFactor, yPos - bbFactor, 
						   breedte + 2 * bbFactor, hoogte + 2 * bbFactor);
	}

	public void rotate(double rotateStep)
	{	rotation += rotateStep;
	}

	public void scale(double scaleStep)
	{	
		// dit niet doen!!
		//xPos = (int) Math.round(scaleStep * xPos + (1 - scaleStep) * cx);
		//yPos = (int) Math.round(scaleStep * yPos + (1 - scaleStep) * cy);
		
		breedte = (int) Math.round(scaleStep * breedte);
		hoogte = (int) Math.round(scaleStep * hoogte);

		// dit niet doen!!
		//ascent = (int) Math.round(scaleStep * ascent);
		
		tekstX = (int) Math.round((1/scaleStep) * tekstX);// + (1 - (1/scaleStep)) * cx);
		tekstY = (int) Math.round((1/scaleStep) * tekstY);// + (1 - (1/scaleStep)) * cy);
		
		scaleX *= scaleStep;
		scaleY *= scaleStep;
	
		makeBB();
	
	}
	
	public Hashtable getState()
	{	Hashtable h = new Hashtable();
		
		h.put("kleur", kleur);
		h.put("tekst", new String(tekst));
		h.put("xPos", new Integer(xPos));
		h.put("yPos", new Integer(yPos));
		h.put("rotation", new Double(rotation));
		h.put("scaleX", new Double(scaleX));
		h.put("scaleY", new Double(scaleY));
	
		return h;
	}

	public static TekstElement setState(Hashtable h)
	{
		Color kleur = Color.black;
		String tekst = new String("");
		int xPos = 0;
		int yPos = 0;
		double rotation = 0;
		double scaleX = 1;
		double scaleY = 1;
		
		if (h.containsKey("kleur"))
			kleur = (Color) h.get("kleur");
		if (h.containsKey("tekst"))
			tekst = (String) h.get("tekst");
		if (h.containsKey("xPos"))
			xPos = ((Integer) h.get("xPos")).intValue();
		if (h.containsKey("yPos"))
			yPos = ((Integer) h.get("yPos")).intValue();

		if (h.containsKey("rotation"))
			rotation = ((Double) h.get("rotation")).doubleValue();

		if (h.containsKey("scaleX"))
			scaleX = ((Double) h.get("scaleX")).doubleValue();
		if (h.containsKey("scaleY"))
			scaleY = ((Double) h.get("scaleY")).doubleValue();
		
		TekstElement tekstElement = new TekstElement(kleur, tekst, xPos, yPos);
		tekstElement.rotation = rotation;
		tekstElement.scaleX = scaleX;
		tekstElement.scaleY = scaleY;
		tekstElement.tekstX = (int) Math.round(((double) xPos) / scaleX);
		tekstElement.tekstY = (int) Math.round(((double) yPos) / scaleY);
		
		
		return tekstElement;
	}
	
	public void teken(Graphics2D g)
	{
		AffineTransform oldAT = g.getTransform();
		AffineTransform at = g.getTransform();
		
		// hier!!
		at.rotate(rotation, cx, cy);
		
		at.scale(scaleX, scaleY);
		
		g.setTransform(at);
		
		g.setFont(KladjeVeld.tekstFont);
		g.setColor(kleur);
		g.drawString(tekst, tekstX, tekstY + ascent);
		
		g.setTransform(oldAT);
		
	}

	public void tekenBB(Graphics2D g)
	{

		AffineTransform oldAT = g.getTransform();
		AffineTransform at = g.getTransform();

		at.rotate(rotation, cx, cy);
		
		g.setTransform(at);
		
		float[] dash = new float[2];
		dash[0] = 2;
		dash[1] = 2;
		g.setStroke(new BasicStroke(1.0f, 2, 0, 10.0f, dash, 0.0f));
		g.setColor(KladjeVeld.bbColor);

		g.drawRect(bb.x, bb.y, bb.width, bb.height);
				
		g.setStroke(new BasicStroke(1.5f, 2, 0, 10.0f, null, 0.0f));
		
		g.setTransform(oldAT);

	}

	public int inverseTransformX(int x, int y)
	{
		double rotX = Math.cos(-rotation) * (x - cx) - Math.sin(- rotation) * (y - cy);
		int rx = (int) Math.round(cx + rotX);
		
		return rx;
	}

	public int inverseTransformY(int x, int y)
	{
		double rotY = Math.sin(-rotation) * (x - cx) + Math.cos(- rotation) * (y - cy);
		int ry = (int) Math.round(cy + rotY);
		
		return ry;
	}

	public boolean bbContains(int x, int y)
	{
		int rx = inverseTransformX(x, y);
		int ry = inverseTransformY(x, y);
		
		return bb.contains(rx, ry);
	}

	public void translate(int dx, int dy)
	{
		xPos += dx;
		yPos += dy;
		tekstX = (int) Math.round(((double) xPos) / scaleX);
		tekstY = (int) Math.round(((double) yPos) / scaleY);
		bb.translate(dx, dy);
		cx += dx;
		cy += dy;
	}	

	public boolean isContainedIn(Rectangle r)
	{
		if (r == null)
			return false;
		
		int toBBx = inverseTransformX(bb.x, bb.y);
		int toBBy = inverseTransformY(bb.x, bb.y);
		int toBBx2 = inverseTransformX(bb.x + bb.width, bb.y + bb.height);
		int toBBy2 = inverseTransformY(bb.x + bb.width, bb.y + bb.height);
		
		boolean isContainedIn = r.contains(toBBx, toBBy) &&	r.contains(toBBx2, toBBy2);		
		
		return isContainedIn;
	}
	
}