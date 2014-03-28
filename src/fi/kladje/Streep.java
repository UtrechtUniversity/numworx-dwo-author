package fi.kladje;

import java.awt.*;
import java.util.*;
import java.awt.geom.*;


public class Streep 
{
	Color kleur;
	int[] puntenX, puntenY;
	int bbFactor = 4;
	Polygon bb, bb2;
	double cx = 0, cy = 0;
	double rotation = 0;
	Rectangle handleBox;
	int hbFactor = 4;
	
	Polygon topRightHandle, bottomRightHandle, topLeftHandle, bottomLeftHandle;
	Rectangle topRightRect, bottomRightRect, topLeftRect, bottomLeftRect;
	Rectangle rotateEastHandle, rotateNorthHandle, rotateWestHandle, rotateSouthHandle;
	
	
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
		makeHandleBox();
	}
	
	public Streep(Color c, int[] ptX, int[] ptY)
	{	kleur = c;
		puntenX = ptX;
		puntenY = ptY;
		
		maakBBs();
		makeHandleBox();
	}

	public void makeHandleBox()
	{
		int minX = 1000;
		int maxX = -100;
		int minY = 1000;
		int maxY = -100;
		for (int pCnt = 0; pCnt < bb2.npoints; pCnt++)
		{
			if (bb2.xpoints[pCnt] < minX)
				minX = bb2.xpoints[pCnt];
			if (bb2.xpoints[pCnt] > maxX)
				maxX = bb2.xpoints[pCnt];
			if (bb2.ypoints[pCnt] < minY)
				minY = bb2.ypoints[pCnt];
			if (bb2.ypoints[pCnt] > maxY)
				maxY = bb2.ypoints[pCnt];
		}
		
		int w = maxX - minX + 2 * hbFactor;
		int h = maxY - minY + 2 * hbFactor;
		int dw = w - KladjeVeld.minHandleBoxSize;
		int dh = h - KladjeVeld.minHandleBoxSize;
		if ((dw >= 0) && (dh >= 0))
			handleBox = new Rectangle(minX - hbFactor, minY - hbFactor,w, h);
		else if ((dw >= 0) && (dh < 0))
			handleBox = new Rectangle(minX - hbFactor, minY - hbFactor + dh/2, w, KladjeVeld.minHandleBoxSize);
		else if ((dw < 0) && (dh >= 0))
			handleBox = new Rectangle(minX - hbFactor + dw/2, minY - hbFactor, KladjeVeld.minHandleBoxSize, h);
		else if ((dw < 0) && (dh < 0))
			handleBox = new Rectangle(minX - hbFactor + dw/2, minY - hbFactor + dh/2, KladjeVeld.minHandleBoxSize, KladjeVeld.minHandleBoxSize);
		
		if (KladjeVeld.schalen)
			makeScaleHandles();
		if (KladjeVeld.roteren)
			makeRotateHandles();
		
	}
	
	public void makeScaleHandles()
	{
		topRightHandle = new Polygon();
		topRightHandle.addPoint(handleBox.x + handleBox.width + hbFactor,
								handleBox.y - hbFactor);
		topRightHandle.addPoint(handleBox.x + handleBox.width - 3 * hbFactor,
								handleBox.y - hbFactor);
		topRightHandle.addPoint(handleBox.x + handleBox.width + hbFactor,
								handleBox.y + 3 * hbFactor);
		topRightRect = new Rectangle(handleBox.x + handleBox.width - 3 * hbFactor,
									 handleBox.y - hbFactor, 4 * hbFactor, 4 * hbFactor);
		
		topLeftHandle = new Polygon();
		topLeftHandle.addPoint(handleBox.x - hbFactor, handleBox.y - hbFactor);
		topLeftHandle.addPoint(handleBox.x + 3 * hbFactor, handleBox.y - hbFactor);
		topLeftHandle.addPoint(handleBox.x - hbFactor, handleBox.y + 3 * hbFactor);
		topLeftRect = new Rectangle(handleBox.x - hbFactor, handleBox.y - hbFactor, 4 * hbFactor, 4 * hbFactor);
		
		bottomRightHandle = new Polygon();
		bottomRightHandle.addPoint(handleBox.x + handleBox.width + hbFactor,
								   handleBox.y + handleBox.height + hbFactor);
		bottomRightHandle.addPoint(handleBox.x + handleBox.width - 3 * hbFactor,
								   handleBox.y + handleBox.height + hbFactor);
		bottomRightHandle.addPoint(handleBox.x + handleBox.width + hbFactor,
								   handleBox.y + handleBox.height - 3 * hbFactor);
		bottomRightRect = new Rectangle(handleBox.x + handleBox.width - 3 * hbFactor,
				   						handleBox.y + handleBox.height - 3 * hbFactor, 4 * hbFactor, 4 * hbFactor);		
		
		bottomLeftHandle = new Polygon();
		bottomLeftHandle.addPoint(handleBox.x - hbFactor, handleBox.y + handleBox.height + hbFactor);
		bottomLeftHandle.addPoint(handleBox.x + 3 * hbFactor, handleBox.y + handleBox.height + hbFactor);
		bottomLeftHandle.addPoint(handleBox.x - hbFactor, handleBox.y + handleBox.height - 3 * hbFactor);
		bottomLeftRect = new Rectangle(handleBox.x - hbFactor, handleBox.y + handleBox.height - 3 * hbFactor, 
									   4 * hbFactor, 4 * hbFactor);		
		
	}
	
	public void killScaleHandles()
	{
		topRightHandle = null; 
		bottomRightHandle = null; 
		topLeftHandle = null;
		bottomLeftHandle = null;
		topRightRect = null;
		bottomRightRect = null;
		topLeftRect = null;
		bottomLeftRect = null;
		
	}

	public void makeRotateHandles()
	{
		rotateEastHandle = new Rectangle(handleBox.x + handleBox.width - 2 * hbFactor,
										 handleBox.y + handleBox.height/2 - 2 * hbFactor,
										 4 * hbFactor, 4 * hbFactor);
		rotateNorthHandle = new Rectangle(handleBox.x + handleBox.width/2 - 2 * hbFactor,
										  handleBox.y - 2 * hbFactor, 4 * hbFactor, 4 * hbFactor);
		rotateWestHandle = new Rectangle(handleBox.x - 2 * hbFactor, handleBox.y + handleBox.height/2 - 2 * hbFactor,
										 4 * hbFactor, 4 * hbFactor);
		rotateSouthHandle = new Rectangle(handleBox.x + handleBox.width/2 - 2 * hbFactor, 
                						  handleBox.y + handleBox.height - 2 * hbFactor,
                						  4 * hbFactor, 4 * hbFactor);
	}

	public void killRotateHandles()
	{
		rotateEastHandle = null; 
		rotateNorthHandle = null; 
		rotateWestHandle = null;
		rotateSouthHandle = null;
		
		
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
		
		bb2 = new Polygon(bb.xpoints,bb.ypoints,bb.npoints);
	}

	public void rotate(double rotateStep)
	{	rotation += rotateStep;
	
		maakBBs();
		bb2 = rotatePolygon(bb2, rotation, cx, cy);

		makeHandleBox();
		
		
		
	}
	
	public void scale(double scaleStep)
	{
		for (int pCnt = 0; pCnt < puntenX.length; pCnt++)
		{	
			puntenX[pCnt] = (int) Math.round(scaleStep * puntenX[pCnt] + (1 - scaleStep) * cx);
			puntenY[pCnt] = (int) Math.round(scaleStep * puntenY[pCnt] + (1 - scaleStep) * cy);
		}
		
		maakBBs();
		bb2 = rotatePolygon(bb2, rotation, cx, cy);
		makeHandleBox();
		
	}

	public void scale(double scaleStepX, double scaleStepY)
	{
		for (int pCnt = 0; pCnt < puntenX.length; pCnt++)
		{	
			puntenX[pCnt] = (int) Math.round(scaleStepX * puntenX[pCnt] + (1 - scaleStepX) * cx);
			puntenY[pCnt] = (int) Math.round(scaleStepY * puntenY[pCnt] + (1 - scaleStepY) * cy);
		}
		
		maakBBs();
		bb2 = rotatePolygon(bb2, rotation, cx, cy);
		makeHandleBox();
		
	}
	
	public Hashtable getState()
	{	Hashtable h = new Hashtable();
		
		h.put("kleur", kleur);
		h.put("kleurgwt", new String("rgb(" + kleur.getRed()+ "," + kleur.getGreen() + "," + kleur.getBlue() + ")"));
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
	
	public void tekenHandleBox(Graphics2D g)
	{

		float[] dash = new float[2];
		dash[0] = 2;
		dash[1] = 2;
		g.setStroke(new BasicStroke(1.0f, 2, 0, 10.0f, dash, 0.0f));
		
		g.setColor(KladjeVeld.hbColor);
		
		g.drawRect(handleBox.x, handleBox.y, handleBox.width, handleBox.height);
		
		g.setStroke(new BasicStroke(1.5f, 2, 0, 10.0f, null, 0.0f));
		
		tekenHandles(g);
	}
	
	public void tekenHandles(Graphics2D g)
	{
		if (topRightHandle != null)
		{	g.setColor(KladjeVeld.hbColor);
			g.drawPolygon(topRightHandle);
//g.setColor(Color.red);
//g.drawRect(topRightRect.x,topRightRect.y,topRightRect.width,topRightRect.height);
		}
		if (topLeftHandle != null)
		{	g.setColor(KladjeVeld.hbColor);
			g.drawPolygon(topLeftHandle);
//g.setColor(Color.red);
//g.drawRect(topLeftRect.x,topLeftRect.y,topLeftRect.width,topLeftRect.height);
			
		}
		if (bottomRightHandle != null)
		{	g.setColor(KladjeVeld.hbColor);
			g.drawPolygon(bottomRightHandle);
//g.setColor(Color.red);
//g.drawRect(bottomRightRect.x,bottomRightRect.y,bottomRightRect.width,bottomRightRect.height);
			
		}
		if (bottomLeftHandle != null)
		{	g.setColor(KladjeVeld.hbColor);
			g.drawPolygon(bottomLeftHandle);
//g.setColor(Color.red);
//g.drawRect(bottomLeftRect.x,bottomLeftRect.y,bottomLeftRect.width,bottomLeftRect.height);
			
		}
		if (rotateEastHandle != null)
		{	g.setColor(KladjeVeld.hbColor);
			g.drawOval(handleBox.x + handleBox.width - 2 * hbFactor , handleBox.y + handleBox.height/2 - 2 * hbFactor, 
					   4 * hbFactor, 4 * hbFactor);
//g.setColor(Color.red);			
//g.drawRect(rotateEastHandle.x, rotateEastHandle.y, rotateEastHandle.width, rotateEastHandle.height);
		}
		if (rotateNorthHandle != null)
		{	g.setColor(KladjeVeld.hbColor);
			g.drawOval(handleBox.x + handleBox.width/2 - 2 * hbFactor, handleBox.y - 2 * hbFactor, 
					   4 * hbFactor, 4 * hbFactor);
//g.setColor(Color.red);			
//g.drawRect(rotateNorthHandle.x, rotateNorthHandle.y, rotateNorthHandle.width, rotateNorthHandle.height);
		}
		if (rotateWestHandle != null)
		{	g.setColor(KladjeVeld.hbColor);
			g.drawOval(handleBox.x - 2 * hbFactor, handleBox.y + handleBox.height/2 - 2 * hbFactor, 
					   4 * hbFactor, 4 * hbFactor);
//g.setColor(Color.red);			
//g.drawRect(rotateWestHandle.x, rotateWestHandle.y, rotateWestHandle.width, rotateWestHandle.height);
		}
		if (rotateSouthHandle != null)
		{	g.setColor(KladjeVeld.hbColor);
			g.drawOval(handleBox.x +handleBox.width/2 - 2 * hbFactor, handleBox.y + handleBox.height - 2 * hbFactor, 
					   4 * hbFactor, 4 * hbFactor);
//g.setColor(Color.red);			
//g.drawRect(rotateSouthHandle.x, rotateSouthHandle.y, rotateSouthHandle.width, rotateSouthHandle.height);

		}
		

	}
	
	public void tekenBB(Graphics2D g)
	{
		AffineTransform oldAT = g.getTransform();
		AffineTransform at = g.getTransform();

		at.rotate(rotation, cx, cy);
		
		g.setTransform(at);

		//float[] dash = new float[2];
		//dash[0] = 2;
		//dash[1] = 2;
		//g.setStroke(new BasicStroke(1.0f, 2, 0, 10.0f, dash, 0.0f));
		g.setStroke(new BasicStroke(0.8f));
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
		bb2.translate(dx, dy);
		makeHandleBox();
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

	public Polygon rotatePolygon(Polygon p, double rotation, double cx, double cy)
	{	
		Polygon r = new Polygon();
		
		for (int pCnt = 0; pCnt < p.npoints; pCnt++)
		{
			double rotX = Math.cos(rotation) * (p.xpoints[pCnt] - cx) - Math.sin(rotation) * (p.ypoints[pCnt] - cy);
			double rotY = Math.sin(rotation) * (p.xpoints[pCnt] - cx) + Math.cos(rotation) * (p.ypoints[pCnt] - cy);
						
			//puntenX[pCnt] = (int) Math.round(cx + rotX);
			//puntenY[pCnt] = (int) Math.round(cy + rotY);
			
			r.addPoint((int) Math.round(cx + rotX), (int) Math.round(cy + rotY));
			//doubleX[pCnt] = cx + rotX;
			//doubleY[pCnt] = cy + rotY;
			
		}
		
		return r;
	}


}


class Lijn
{
	Color kleur;
	int fromX, fromY, toX, toY;
	int bbFactor = 4;
	Polygon bb,bb2;
	double cx, cy;
	double rotation = 0;
	Rectangle handleBox;
	int hbFactor = 4;
	
	Polygon topRightHandle, bottomRightHandle, topLeftHandle, bottomLeftHandle;
	Rectangle topRightRect, bottomRightRect, topLeftRect, bottomLeftRect;
	Rectangle rotateEastHandle, rotateNorthHandle, rotateWestHandle, rotateSouthHandle;
	
	
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
		makeHandleBox();
	}
	
	public void makeHandleBox()
	{
		int minX = 1000;
		int maxX = -100;
		int minY = 1000;
		int maxY = -100;
		for (int pCnt = 0; pCnt < bb2.npoints; pCnt++)
		{
			if (bb2.xpoints[pCnt] < minX)
				minX = bb2.xpoints[pCnt];
			if (bb2.xpoints[pCnt] > maxX)
				maxX = bb2.xpoints[pCnt];
			if (bb2.ypoints[pCnt] < minY)
				minY = bb2.ypoints[pCnt];
			if (bb2.ypoints[pCnt] > maxY)
				maxY = bb2.ypoints[pCnt];
		}
		
		int w = maxX - minX + 2 * hbFactor;
		int h = maxY - minY + 2 * hbFactor;
		int dw = w - KladjeVeld.minHandleBoxSize;
		int dh = h - KladjeVeld.minHandleBoxSize;
		if ((dw >= 0) && (dh >= 0))
			handleBox = new Rectangle(minX - hbFactor, minY - hbFactor,w, h);
		else if ((dw >= 0) && (dh < 0))
			handleBox = new Rectangle(minX - hbFactor, minY - hbFactor + dh/2, w, KladjeVeld.minHandleBoxSize);
		else if ((dw < 0) && (dh >= 0))
			handleBox = new Rectangle(minX - hbFactor + dw/2, minY - hbFactor, KladjeVeld.minHandleBoxSize, h);
		else if ((dw < 0) && (dh < 0))
			handleBox = new Rectangle(minX - hbFactor + dw/2, minY - hbFactor + dh/2, KladjeVeld.minHandleBoxSize, KladjeVeld.minHandleBoxSize);

		if (KladjeVeld.schalen)
			makeScaleHandles();
		if (KladjeVeld.roteren)
			makeRotateHandles();
		
	}
	
	public void makeScaleHandles()
	{
		topRightHandle = new Polygon();
		topRightHandle.addPoint(handleBox.x + handleBox.width + hbFactor,
								handleBox.y - hbFactor);
		topRightHandle.addPoint(handleBox.x + handleBox.width - 3 * hbFactor,
								handleBox.y - hbFactor);
		topRightHandle.addPoint(handleBox.x + handleBox.width + hbFactor,
								handleBox.y + 3 * hbFactor);
		topRightRect = new Rectangle(handleBox.x + handleBox.width - 3 * hbFactor,
									 handleBox.y - hbFactor, 4 * hbFactor, 4 * hbFactor);
		
		topLeftHandle = new Polygon();
		topLeftHandle.addPoint(handleBox.x - hbFactor, handleBox.y - hbFactor);
		topLeftHandle.addPoint(handleBox.x + 3 * hbFactor, handleBox.y - hbFactor);
		topLeftHandle.addPoint(handleBox.x - hbFactor, handleBox.y + 3 * hbFactor);
		topLeftRect = new Rectangle(handleBox.x - hbFactor, handleBox.y - hbFactor, 4 * hbFactor, 4 * hbFactor);
		
		bottomRightHandle = new Polygon();
		bottomRightHandle.addPoint(handleBox.x + handleBox.width + hbFactor,
								   handleBox.y + handleBox.height + hbFactor);
		bottomRightHandle.addPoint(handleBox.x + handleBox.width - 3 * hbFactor,
								   handleBox.y + handleBox.height + hbFactor);
		bottomRightHandle.addPoint(handleBox.x + handleBox.width + hbFactor,
								   handleBox.y + handleBox.height - 3 * hbFactor);
		bottomRightRect = new Rectangle(handleBox.x + handleBox.width - 3 * hbFactor,
				   						handleBox.y + handleBox.height - 3 * hbFactor, 4 * hbFactor, 4 * hbFactor);		
		
		bottomLeftHandle = new Polygon();
		bottomLeftHandle.addPoint(handleBox.x - hbFactor, handleBox.y + handleBox.height + hbFactor);
		bottomLeftHandle.addPoint(handleBox.x + 3 * hbFactor, handleBox.y + handleBox.height + hbFactor);
		bottomLeftHandle.addPoint(handleBox.x - hbFactor, handleBox.y + handleBox.height - 3 * hbFactor);
		bottomLeftRect = new Rectangle(handleBox.x - hbFactor, handleBox.y + handleBox.height - 3 * hbFactor, 
									   4 * hbFactor, 4 * hbFactor);		
		
	}

	public void killScaleHandles()
	{
		topRightHandle = null; 
		bottomRightHandle = null; 
		topLeftHandle = null;
		bottomLeftHandle = null;
		topRightRect = null;
		bottomRightRect = null;
		topLeftRect = null;
		bottomLeftRect = null;
		
	}
	
	public void makeRotateHandles()
	{
		rotateEastHandle = new Rectangle(handleBox.x + handleBox.width - 2 * hbFactor,
										 handleBox.y + handleBox.height/2 - 2 * hbFactor,
										 4 * hbFactor, 4 * hbFactor);
		rotateNorthHandle = new Rectangle(handleBox.x + handleBox.width/2 - 2 * hbFactor,
										  handleBox.y - 2 * hbFactor, 4 * hbFactor, 4 * hbFactor);
		rotateWestHandle = new Rectangle(handleBox.x - 2 * hbFactor, handleBox.y + handleBox.height/2 - 2 * hbFactor,
										 4 * hbFactor, 4 * hbFactor);
		rotateSouthHandle = new Rectangle(handleBox.x + handleBox.width/2 - 2 * hbFactor, 
                						  handleBox.y + handleBox.height - 2 * hbFactor,
                						  4 * hbFactor, 4 * hbFactor);
	}

	public void killRotateHandles()
	{
		rotateEastHandle = null; 
		rotateNorthHandle = null; 
		rotateWestHandle = null;
		rotateSouthHandle = null;
		
		
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
		
		bb2 = new Polygon(bb.xpoints,bb.ypoints,bb.npoints);
	}

	public void rotate(double rotateStep)
	{	rotation += rotateStep;
	
		makeBB();
		bb2 = rotatePolygon(bb2, rotation, cx, cy);

		makeHandleBox();
	
	}
	
	public void scale(double scaleStep)
	{	
		fromX = (int) Math.round(scaleStep * fromX + (1 - scaleStep) * cx);
		fromY = (int) Math.round(scaleStep * fromY + (1 - scaleStep) * cy);
		toX = (int) Math.round(scaleStep * toX + (1 - scaleStep) * cx);
		toY = (int) Math.round(scaleStep * toY + (1 - scaleStep) * cy);
		
		makeBB();
		bb2 = rotatePolygon(bb2, rotation, cx, cy);
		makeHandleBox();
		
	}

	public void scale(double sx, double sy)
	{	
		fromX = (int) Math.round(sx * fromX + (1 - sx) * cx);
		fromY = (int) Math.round(sy * fromY + (1 - sy) * cy);
		toX = (int) Math.round(sx * toX + (1 - sx) * cx);
		toY = (int) Math.round(sy * toY + (1 - sy) * cy);
		
		makeBB();
		bb2 = rotatePolygon(bb2, rotation, cx, cy);
		makeHandleBox();
		
	}
	
	public Hashtable getState()
	{	Hashtable h = new Hashtable();
		
		h.put("kleur", kleur);
		h.put("kleurgwt", new String("rgb(" + kleur.getRed()+ "," + kleur.getGreen() + "," + kleur.getBlue() + ")"));		
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

	public void tekenHandleBox(Graphics2D g)
	{
		float[] dash = new float[2];
		dash[0] = 2;
		dash[1] = 2;
		g.setStroke(new BasicStroke(1.0f, 2, 0, 10.0f, dash, 0.0f));

		g.setColor(KladjeVeld.hbColor);
		
		g.drawRect(handleBox.x, handleBox.y, handleBox.width, handleBox.height);
		
		g.setStroke(new BasicStroke(1.5f, 2, 0, 10.0f, null, 0.0f));
		
		tekenHandles(g);
	}

	public void tekenHandles(Graphics2D g)
	{
		if (topRightHandle != null)
		{	g.setColor(KladjeVeld.hbColor);
			g.drawPolygon(topRightHandle);
//g.setColor(Color.red);
//g.drawRect(topRightRect.x,topRightRect.y,topRightRect.width,topRightRect.height);
		}
		if (topLeftHandle != null)
		{	g.setColor(KladjeVeld.hbColor);
			g.drawPolygon(topLeftHandle);
//g.setColor(Color.red);
//g.drawRect(topLeftRect.x,topLeftRect.y,topLeftRect.width,topLeftRect.height);
			
		}
		if (bottomRightHandle != null)
		{	g.setColor(KladjeVeld.hbColor);
			g.drawPolygon(bottomRightHandle);
//g.setColor(Color.red);
//g.drawRect(bottomRightRect.x,bottomRightRect.y,bottomRightRect.width,bottomRightRect.height);
			
		}
		if (bottomLeftHandle != null)
		{	g.setColor(KladjeVeld.hbColor);
			g.drawPolygon(bottomLeftHandle);
//g.setColor(Color.red);
//g.drawRect(bottomLeftRect.x,bottomLeftRect.y,bottomLeftRect.width,bottomLeftRect.height);
			
		}
		if (rotateEastHandle != null)
		{	g.setColor(KladjeVeld.hbColor);
			g.drawOval(handleBox.x + handleBox.width - 2 * hbFactor , handleBox.y + handleBox.height/2 - 2 * hbFactor, 
					   4 * hbFactor, 4 * hbFactor);
//g.setColor(Color.red);			
//g.drawRect(rotateEastHandle.x, rotateEastHandle.y, rotateEastHandle.width, rotateEastHandle.height);
		}
		if (rotateNorthHandle != null)
		{	g.setColor(KladjeVeld.hbColor);
			g.drawOval(handleBox.x + handleBox.width/2 - 2 * hbFactor, handleBox.y - 2 * hbFactor, 
					   4 * hbFactor, 4 * hbFactor);
//g.setColor(Color.red);			
//g.drawRect(rotateNorthHandle.x, rotateNorthHandle.y, rotateNorthHandle.width, rotateNorthHandle.height);
		}
		if (rotateWestHandle != null)
		{	g.setColor(KladjeVeld.hbColor);
			g.drawOval(handleBox.x - 2 * hbFactor, handleBox.y + handleBox.height/2 - 2 * hbFactor, 
					   4 * hbFactor, 4 * hbFactor);
//g.setColor(Color.red);			
//g.drawRect(rotateWestHandle.x, rotateWestHandle.y, rotateWestHandle.width, rotateWestHandle.height);
		}
		if (rotateSouthHandle != null)
		{	g.setColor(KladjeVeld.hbColor);
			g.drawOval(handleBox.x +handleBox.width/2 - 2 * hbFactor, handleBox.y + handleBox.height - 2 * hbFactor, 
					   4 * hbFactor, 4 * hbFactor);
//g.setColor(Color.red);			
//g.drawRect(rotateSouthHandle.x, rotateSouthHandle.y, rotateSouthHandle.width, rotateSouthHandle.height);

		}
		

	}
	
	public void tekenBB(Graphics2D g)
	{
		AffineTransform oldAT = g.getTransform();
		AffineTransform at = g.getTransform();

		at.rotate(rotation, cx, cy);
		
		g.setTransform(at);

		//float[] dash = new float[2];
		//dash[0] = 2;
		//dash[1] = 2;
		//g.setStroke(new BasicStroke(1.0f, 2, 0, 10.0f, dash, 0.0f));
		g.setStroke(new BasicStroke(0.8f));
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
		bb2.translate(dx, dy);
		makeHandleBox();

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
	
	public Polygon rotatePolygon(Polygon p, double rotation, double cx, double cy)
	{	
		Polygon r = new Polygon();
		
		for (int pCnt = 0; pCnt < p.npoints; pCnt++)
		{
			double rotX = Math.cos(rotation) * (p.xpoints[pCnt] - cx) - Math.sin(rotation) * (p.ypoints[pCnt] - cy);
			double rotY = Math.sin(rotation) * (p.xpoints[pCnt] - cx) + Math.cos(rotation) * (p.ypoints[pCnt] - cy);
						
			//puntenX[pCnt] = (int) Math.round(cx + rotX);
			//puntenY[pCnt] = (int) Math.round(cy + rotY);
			
			r.addPoint((int) Math.round(cx + rotX), (int) Math.round(cy + rotY));
			//doubleX[pCnt] = cx + rotX;
			//doubleY[pCnt] = cy + rotY;
			
		}
		
		return r;
	}

}
class Rechthoek
{	Color kleur;
	int topLeftX, topLeftY, breedte, hoogte;
	int bbFactor = 4;
	Rectangle outerBB;
	Rectangle innerBB;
	Polygon bb2;
	double cx, cy;
	double rotation = 0;
	Rectangle handleBox;
	int hbFactor = 4;

	Polygon topRightHandle, bottomRightHandle, topLeftHandle, bottomLeftHandle;
	Rectangle topRightRect, bottomRightRect, topLeftRect, bottomLeftRect;
	Rectangle rotateEastHandle, rotateNorthHandle, rotateWestHandle, rotateSouthHandle;
	
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
		makeHandleBox();
		
	}

	public void makeHandleBox()
	{
		int minX = 1000;
		int maxX = -100;
		int minY = 1000;
		int maxY = -100;
		for (int pCnt = 0; pCnt < bb2.npoints; pCnt++)
		{
			if (bb2.xpoints[pCnt] < minX)
				minX = bb2.xpoints[pCnt];
			if (bb2.xpoints[pCnt] > maxX)
				maxX = bb2.xpoints[pCnt];
			if (bb2.ypoints[pCnt] < minY)
				minY = bb2.ypoints[pCnt];
			if (bb2.ypoints[pCnt] > maxY)
				maxY = bb2.ypoints[pCnt];
		}
		
		int w = maxX - minX + 2 * hbFactor;
		int h = maxY - minY + 2 * hbFactor;
		int dw = w - KladjeVeld.minHandleBoxSize;
		int dh = h - KladjeVeld.minHandleBoxSize;
		if ((dw >= 0) && (dh >= 0))
			handleBox = new Rectangle(minX - hbFactor, minY - hbFactor,w, h);
		else if ((dw >= 0) && (dh < 0))
			handleBox = new Rectangle(minX - hbFactor, minY - hbFactor + dh/2, w, KladjeVeld.minHandleBoxSize);
		else if ((dw < 0) && (dh >= 0))
			handleBox = new Rectangle(minX - hbFactor + dw/2, minY - hbFactor, KladjeVeld.minHandleBoxSize, h);
		else if ((dw < 0) && (dh < 0))
			handleBox = new Rectangle(minX - hbFactor + dw/2, minY - hbFactor + dh/2, KladjeVeld.minHandleBoxSize, KladjeVeld.minHandleBoxSize);	
		
		if (KladjeVeld.schalen)
			makeScaleHandles();
		if (KladjeVeld.roteren)
			makeRotateHandles();
		
	}
	
	public void makeScaleHandles()
	{
		topRightHandle = new Polygon();
		topRightHandle.addPoint(handleBox.x + handleBox.width + hbFactor,
								handleBox.y - hbFactor);
		topRightHandle.addPoint(handleBox.x + handleBox.width - 3 * hbFactor,
								handleBox.y - hbFactor);
		topRightHandle.addPoint(handleBox.x + handleBox.width + hbFactor,
								handleBox.y + 3 * hbFactor);
		topRightRect = new Rectangle(handleBox.x + handleBox.width - 3 * hbFactor,
									 handleBox.y - hbFactor, 4 * hbFactor, 4 * hbFactor);
		
		topLeftHandle = new Polygon();
		topLeftHandle.addPoint(handleBox.x - hbFactor, handleBox.y - hbFactor);
		topLeftHandle.addPoint(handleBox.x + 3 * hbFactor, handleBox.y - hbFactor);
		topLeftHandle.addPoint(handleBox.x - hbFactor, handleBox.y + 3 * hbFactor);
		topLeftRect = new Rectangle(handleBox.x - hbFactor, handleBox.y - hbFactor, 4 * hbFactor, 4 * hbFactor);
		
		bottomRightHandle = new Polygon();
		bottomRightHandle.addPoint(handleBox.x + handleBox.width + hbFactor,
								   handleBox.y + handleBox.height + hbFactor);
		bottomRightHandle.addPoint(handleBox.x + handleBox.width - 3 * hbFactor,
								   handleBox.y + handleBox.height + hbFactor);
		bottomRightHandle.addPoint(handleBox.x + handleBox.width + hbFactor,
								   handleBox.y + handleBox.height - 3 * hbFactor);
		bottomRightRect = new Rectangle(handleBox.x + handleBox.width - 3 * hbFactor,
				   						handleBox.y + handleBox.height - 3 * hbFactor, 4 * hbFactor, 4 * hbFactor);		
		
		bottomLeftHandle = new Polygon();
		bottomLeftHandle.addPoint(handleBox.x - hbFactor, handleBox.y + handleBox.height + hbFactor);
		bottomLeftHandle.addPoint(handleBox.x + 3 * hbFactor, handleBox.y + handleBox.height + hbFactor);
		bottomLeftHandle.addPoint(handleBox.x - hbFactor, handleBox.y + handleBox.height - 3 * hbFactor);
		bottomLeftRect = new Rectangle(handleBox.x - hbFactor, handleBox.y + handleBox.height - 3 * hbFactor, 
									   4 * hbFactor, 4 * hbFactor);		
		
	}

	public void killScaleHandles()
	{
		topRightHandle = null; 
		bottomRightHandle = null; 
		topLeftHandle = null;
		bottomLeftHandle = null;
		topRightRect = null;
		bottomRightRect = null;
		topLeftRect = null;
		bottomLeftRect = null;
		
	}

	public void makeRotateHandles()
	{
		rotateEastHandle = new Rectangle(handleBox.x + handleBox.width - 2 * hbFactor,
										 handleBox.y + handleBox.height/2 - 2 * hbFactor,
										 4 * hbFactor, 4 * hbFactor);
		rotateNorthHandle = new Rectangle(handleBox.x + handleBox.width/2 - 2 * hbFactor,
										  handleBox.y - 2 * hbFactor, 4 * hbFactor, 4 * hbFactor);
		rotateWestHandle = new Rectangle(handleBox.x - 2 * hbFactor, handleBox.y + handleBox.height/2 - 2 * hbFactor,
										 4 * hbFactor, 4 * hbFactor);
		rotateSouthHandle = new Rectangle(handleBox.x + handleBox.width/2 - 2 * hbFactor, 
                						  handleBox.y + handleBox.height - 2 * hbFactor,
                						  4 * hbFactor, 4 * hbFactor);
	}

	public void killRotateHandles()
	{
		rotateEastHandle = null; 
		rotateNorthHandle = null; 
		rotateWestHandle = null;
		rotateSouthHandle = null;
		
		
	}

	public void makeBB()
	{
		outerBB = new Rectangle(topLeftX - bbFactor, topLeftY - bbFactor, 
			    breedte + 2 * bbFactor, hoogte + 2 * bbFactor);
		innerBB = new Rectangle(topLeftX + bbFactor, topLeftY + bbFactor, 
			    breedte - 2 * bbFactor, hoogte - 2 * bbFactor);
		
		bb2 = new Polygon();
		bb2.addPoint(topLeftX - bbFactor, topLeftY - bbFactor);
		bb2.addPoint(topLeftX - bbFactor + breedte + 2 * bbFactor, topLeftY - bbFactor);
		bb2.addPoint(topLeftX - bbFactor + breedte + 2 * bbFactor, topLeftY - bbFactor + hoogte + 2 * bbFactor);
		bb2.addPoint(topLeftX - bbFactor, topLeftY - bbFactor + hoogte + 2 * bbFactor);
		
	}
	
	public void rotate(double rotateStep)
	{	rotation += rotateStep;
		
		makeBB();
		bb2 = rotatePolygon(bb2, rotation, cx, cy);
		makeHandleBox();

	}
	
	public void scale(double scaleStep)
	{	
		topLeftX = (int) Math.round(scaleStep * topLeftX + (1 - scaleStep) * cx);
		topLeftY = (int) Math.round(scaleStep * topLeftY + (1 - scaleStep) * cy);
		breedte = (int) Math.round(scaleStep * breedte);
		hoogte = (int) Math.round(scaleStep * hoogte);
		
		makeBB();
		bb2 = rotatePolygon(bb2, rotation, cx, cy);
		makeHandleBox();
		
	}
	
	public void scale(double sx, double sy)
	{	
		topLeftX = (int) Math.round(sx * topLeftX + (1 - sx) * cx);
		topLeftY = (int) Math.round(sy * topLeftY + (1 - sy) * cy);
		breedte = (int) Math.round(sx * breedte);
		hoogte = (int) Math.round(sy * hoogte);
		
		makeBB();
		bb2 = rotatePolygon(bb2, rotation, cx, cy);
		makeHandleBox();
		
	}
	
	public Hashtable getState()
	{	Hashtable h = new Hashtable();
		
		h.put("kleur", kleur);
		h.put("kleurgwt", new String("rgb(" + kleur.getRed()+ "," + kleur.getGreen() + "," + kleur.getBlue() + ")"));		
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

	public void tekenHandleBox(Graphics2D g)
	{
		float[] dash = new float[2];
		dash[0] = 2;
		dash[1] = 2;
		g.setStroke(new BasicStroke(1.0f, 2, 0, 10.0f, dash, 0.0f));

		g.setColor(KladjeVeld.hbColor);
		
		g.drawRect(handleBox.x, handleBox.y, handleBox.width, handleBox.height);
		
		g.setStroke(new BasicStroke(1.5f, 2, 0, 10.0f, null, 0.0f));
		
		tekenHandles(g);
	}

	public void tekenHandles(Graphics2D g)
	{
		if (topRightHandle != null)
		{	g.setColor(KladjeVeld.hbColor);
			g.drawPolygon(topRightHandle);
//g.setColor(Color.red);
//g.drawRect(topRightRect.x,topRightRect.y,topRightRect.width,topRightRect.height);
		}
		if (topLeftHandle != null)
		{	g.setColor(KladjeVeld.hbColor);
			g.drawPolygon(topLeftHandle);
//g.setColor(Color.red);
//g.drawRect(topLeftRect.x,topLeftRect.y,topLeftRect.width,topLeftRect.height);
			
		}
		if (bottomRightHandle != null)
		{	g.setColor(KladjeVeld.hbColor);
			g.drawPolygon(bottomRightHandle);
//g.setColor(Color.red);
//g.drawRect(bottomRightRect.x,bottomRightRect.y,bottomRightRect.width,bottomRightRect.height);
			
		}
		if (bottomLeftHandle != null)
		{	g.setColor(KladjeVeld.hbColor);
			g.drawPolygon(bottomLeftHandle);
//g.setColor(Color.red);
//g.drawRect(bottomLeftRect.x,bottomLeftRect.y,bottomLeftRect.width,bottomLeftRect.height);
			
		}
		if (rotateEastHandle != null)
		{	g.setColor(KladjeVeld.hbColor);
			g.drawOval(handleBox.x + handleBox.width - 2 * hbFactor , handleBox.y + handleBox.height/2 - 2 * hbFactor, 
					   4 * hbFactor, 4 * hbFactor);
//g.setColor(Color.red);			
//g.drawRect(rotateEastHandle.x, rotateEastHandle.y, rotateEastHandle.width, rotateEastHandle.height);
		}
		if (rotateNorthHandle != null)
		{	g.setColor(KladjeVeld.hbColor);
			g.drawOval(handleBox.x + handleBox.width/2 - 2 * hbFactor, handleBox.y - 2 * hbFactor, 
					   4 * hbFactor, 4 * hbFactor);
//g.setColor(Color.red);			
//g.drawRect(rotateNorthHandle.x, rotateNorthHandle.y, rotateNorthHandle.width, rotateNorthHandle.height);
		}
		if (rotateWestHandle != null)
		{	g.setColor(KladjeVeld.hbColor);
			g.drawOval(handleBox.x - 2 * hbFactor, handleBox.y + handleBox.height/2 - 2 * hbFactor, 
					   4 * hbFactor, 4 * hbFactor);
//g.setColor(Color.red);			
//g.drawRect(rotateWestHandle.x, rotateWestHandle.y, rotateWestHandle.width, rotateWestHandle.height);
		}
		if (rotateSouthHandle != null)
		{	g.setColor(KladjeVeld.hbColor);
			g.drawOval(handleBox.x +handleBox.width/2 - 2 * hbFactor, handleBox.y + handleBox.height - 2 * hbFactor, 
					   4 * hbFactor, 4 * hbFactor);
//g.setColor(Color.red);			
//g.drawRect(rotateSouthHandle.x, rotateSouthHandle.y, rotateSouthHandle.width, rotateSouthHandle.height);

		}
		

	}

	public void tekenBB(Graphics2D g)
	{
		AffineTransform oldAT = g.getTransform();
		AffineTransform at = g.getTransform();

		at.rotate(rotation, cx, cy);
		
		g.setTransform(at);
		
		//float[] dash = new float[2];
		//dash[0] = 2;
		//dash[1] = 2;
		//g.setStroke(new BasicStroke(1.0f, 2, 0, 10.0f, dash, 0.0f));
		g.setStroke(new BasicStroke(0.8f));
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
		
		bb2.translate(dx, dy);
		makeHandleBox();

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
	
	public Polygon rotatePolygon(Polygon p, double rotation, double cx, double cy)
	{	
		Polygon r = new Polygon();
		
		for (int pCnt = 0; pCnt < p.npoints; pCnt++)
		{
			double rotX = Math.cos(rotation) * (p.xpoints[pCnt] - cx) - Math.sin(rotation) * (p.ypoints[pCnt] - cy);
			double rotY = Math.sin(rotation) * (p.xpoints[pCnt] - cx) + Math.cos(rotation) * (p.ypoints[pCnt] - cy);
						
			//puntenX[pCnt] = (int) Math.round(cx + rotX);
			//puntenY[pCnt] = (int) Math.round(cy + rotY);
			
			r.addPoint((int) Math.round(cx + rotX), (int) Math.round(cy + rotY));
			//doubleX[pCnt] = cx + rotX;
			//doubleY[pCnt] = cy + rotY;
			
		}
		
		return r;
	}

	
}
class Ellips
{	Color kleur;
	int topLeftX, topLeftY, breedte, hoogte;
	int bbFactor = 4;
	Rectangle outerBB, innerBB;
	Polygon bb2;
	double cx, cy;
	double rotation = 0;
	Rectangle handleBox;
	int hbFactor = 4;

	Polygon topRightHandle, bottomRightHandle, topLeftHandle, bottomLeftHandle;
	Rectangle topRightRect, bottomRightRect, topLeftRect, bottomLeftRect;
	Rectangle rotateEastHandle, rotateNorthHandle, rotateWestHandle, rotateSouthHandle;
	
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
		makeHandleBox();
		
	}

	public void makeHandleBox()
	{
		int minX = 1000;
		int maxX = -100;
		int minY = 1000;
		int maxY = -100;
		for (int pCnt = 0; pCnt < bb2.npoints; pCnt++)
		{
			if (bb2.xpoints[pCnt] < minX)
				minX = bb2.xpoints[pCnt];
			if (bb2.xpoints[pCnt] > maxX)
				maxX = bb2.xpoints[pCnt];
			if (bb2.ypoints[pCnt] < minY)
				minY = bb2.ypoints[pCnt];
			if (bb2.ypoints[pCnt] > maxY)
				maxY = bb2.ypoints[pCnt];
		}
		
		int w = maxX - minX + 2 * hbFactor;
		int h = maxY - minY + 2 * hbFactor;
		int dw = w - KladjeVeld.minHandleBoxSize;
		int dh = h - KladjeVeld.minHandleBoxSize;
		if ((dw >= 0) && (dh >= 0))
			handleBox = new Rectangle(minX - hbFactor, minY - hbFactor,w, h);
		else if ((dw >= 0) && (dh < 0))
			handleBox = new Rectangle(minX - hbFactor, minY - hbFactor + dh/2, w, KladjeVeld.minHandleBoxSize);
		else if ((dw < 0) && (dh >= 0))
			handleBox = new Rectangle(minX - hbFactor + dw/2, minY - hbFactor, KladjeVeld.minHandleBoxSize, h);
		else if ((dw < 0) && (dh < 0))
			handleBox = new Rectangle(minX - hbFactor + dw/2, minY - hbFactor + dh/2, KladjeVeld.minHandleBoxSize, KladjeVeld.minHandleBoxSize);	
		
		if (KladjeVeld.schalen)
			makeScaleHandles();
		if (KladjeVeld.roteren)
			makeRotateHandles();
		
	}
	
	public void makeScaleHandles()
	{
		topRightHandle = new Polygon();
		topRightHandle.addPoint(handleBox.x + handleBox.width + hbFactor,
								handleBox.y - hbFactor);
		topRightHandle.addPoint(handleBox.x + handleBox.width - 3 * hbFactor,
								handleBox.y - hbFactor);
		topRightHandle.addPoint(handleBox.x + handleBox.width + hbFactor,
								handleBox.y + 3 * hbFactor);
		topRightRect = new Rectangle(handleBox.x + handleBox.width - 3 * hbFactor,
									 handleBox.y - hbFactor, 4 * hbFactor, 4 * hbFactor);
		
		topLeftHandle = new Polygon();
		topLeftHandle.addPoint(handleBox.x - hbFactor, handleBox.y - hbFactor);
		topLeftHandle.addPoint(handleBox.x + 3 * hbFactor, handleBox.y - hbFactor);
		topLeftHandle.addPoint(handleBox.x - hbFactor, handleBox.y + 3 * hbFactor);
		topLeftRect = new Rectangle(handleBox.x - hbFactor, handleBox.y - hbFactor, 4 * hbFactor, 4 * hbFactor);
		
		bottomRightHandle = new Polygon();
		bottomRightHandle.addPoint(handleBox.x + handleBox.width + hbFactor,
								   handleBox.y + handleBox.height + hbFactor);
		bottomRightHandle.addPoint(handleBox.x + handleBox.width - 3 * hbFactor,
								   handleBox.y + handleBox.height + hbFactor);
		bottomRightHandle.addPoint(handleBox.x + handleBox.width + hbFactor,
								   handleBox.y + handleBox.height - 3 * hbFactor);
		bottomRightRect = new Rectangle(handleBox.x + handleBox.width - 3 * hbFactor,
				   						handleBox.y + handleBox.height - 3 * hbFactor, 4 * hbFactor, 4 * hbFactor);		
		
		bottomLeftHandle = new Polygon();
		bottomLeftHandle.addPoint(handleBox.x - hbFactor, handleBox.y + handleBox.height + hbFactor);
		bottomLeftHandle.addPoint(handleBox.x + 3 * hbFactor, handleBox.y + handleBox.height + hbFactor);
		bottomLeftHandle.addPoint(handleBox.x - hbFactor, handleBox.y + handleBox.height - 3 * hbFactor);
		bottomLeftRect = new Rectangle(handleBox.x - hbFactor, handleBox.y + handleBox.height - 3 * hbFactor, 
									   4 * hbFactor, 4 * hbFactor);		
		
	}

	public void killScaleHandles()
	{
		topRightHandle = null; 
		bottomRightHandle = null; 
		topLeftHandle = null;
		bottomLeftHandle = null;
		topRightRect = null;
		bottomRightRect = null;
		topLeftRect = null;
		bottomLeftRect = null;
		
	}

	public void makeRotateHandles()
	{
		rotateEastHandle = new Rectangle(handleBox.x + handleBox.width - 2 * hbFactor,
										 handleBox.y + handleBox.height/2 - 2 * hbFactor,
										 4 * hbFactor, 4 * hbFactor);
		rotateNorthHandle = new Rectangle(handleBox.x + handleBox.width/2 - 2 * hbFactor,
										  handleBox.y - 2 * hbFactor, 4 * hbFactor, 4 * hbFactor);
		rotateWestHandle = new Rectangle(handleBox.x - 2 * hbFactor, handleBox.y + handleBox.height/2 - 2 * hbFactor,
										 4 * hbFactor, 4 * hbFactor);
		rotateSouthHandle = new Rectangle(handleBox.x + handleBox.width/2 - 2 * hbFactor, 
                						  handleBox.y + handleBox.height - 2 * hbFactor,
                						  4 * hbFactor, 4 * hbFactor);
	}

	public void killRotateHandles()
	{
		rotateEastHandle = null; 
		rotateNorthHandle = null; 
		rotateWestHandle = null;
		rotateSouthHandle = null;
		
		
	}
	
	public void makeBB()
	{
		outerBB = new Rectangle(topLeftX - bbFactor, topLeftY - bbFactor, 
			    breedte + 2 * bbFactor, hoogte + 2 * bbFactor);
		innerBB = new Rectangle(topLeftX + bbFactor, topLeftY + bbFactor, 
			    breedte - 2 * bbFactor, hoogte - 2 * bbFactor);

		bb2 = new Polygon();
		bb2.addPoint(topLeftX - bbFactor, topLeftY - bbFactor);
		bb2.addPoint(topLeftX - bbFactor + breedte + 2 * bbFactor, topLeftY - bbFactor);
		bb2.addPoint(topLeftX - bbFactor + breedte + 2 * bbFactor, topLeftY - bbFactor + hoogte + 2 * bbFactor);
		bb2.addPoint(topLeftX - bbFactor, topLeftY - bbFactor + hoogte + 2 * bbFactor);

	}
	
	public void rotate(double rotateStep)
	{	rotation += rotateStep;
		
		makeBB();
		bb2 = rotatePolygon(bb2, rotation, cx, cy);
		makeHandleBox();

	}
	
	public void scale(double scaleStep)
	{	
		topLeftX = (int) Math.round(scaleStep * topLeftX + (1 - scaleStep) * cx);
		topLeftY = (int) Math.round(scaleStep * topLeftY + (1 - scaleStep) * cy);
		breedte = (int) Math.round(scaleStep * breedte);
		hoogte = (int) Math.round(scaleStep * hoogte);
		
		makeBB();
		bb2 = rotatePolygon(bb2, rotation, cx, cy);
		makeHandleBox();
		
	}
	
	public void scale(double sx, double sy)
	{	
		topLeftX = (int) Math.round(sx * topLeftX + (1 - sx) * cx);
		topLeftY = (int) Math.round(sy * topLeftY + (1 - sy) * cy);
		breedte = (int) Math.round(sx * breedte);
		hoogte = (int) Math.round(sy * hoogte);
		
		makeBB();
		bb2 = rotatePolygon(bb2, rotation, cx, cy);
		makeHandleBox();
		
	}
	
	public Hashtable getState()
	{	Hashtable h = new Hashtable();
		
		h.put("kleur", kleur);
		h.put("kleurgwt", new String("rgb(" + kleur.getRed()+ "," + kleur.getGreen() + "," + kleur.getBlue() + ")"));		
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

	public void tekenHandleBox(Graphics2D g)
	{
		float[] dash = new float[2];
		dash[0] = 2;
		dash[1] = 2;
		g.setStroke(new BasicStroke(1.0f, 2, 0, 10.0f, dash, 0.0f));

		g.setColor(KladjeVeld.hbColor);
		
		g.drawRect(handleBox.x, handleBox.y, handleBox.width, handleBox.height);
		
		g.setStroke(new BasicStroke(1.5f, 2, 0, 10.0f, null, 0.0f));
		
		tekenHandles(g);
	}

	
	public void tekenHandles(Graphics2D g)
	{
		if (topRightHandle != null)
		{	g.setColor(KladjeVeld.hbColor);
			g.drawPolygon(topRightHandle);
//g.setColor(Color.red);
//g.drawRect(topRightRect.x,topRightRect.y,topRightRect.width,topRightRect.height);
		}
		if (topLeftHandle != null)
		{	g.setColor(KladjeVeld.hbColor);
			g.drawPolygon(topLeftHandle);
//g.setColor(Color.red);
//g.drawRect(topLeftRect.x,topLeftRect.y,topLeftRect.width,topLeftRect.height);
			
		}
		if (bottomRightHandle != null)
		{	g.setColor(KladjeVeld.hbColor);
			g.drawPolygon(bottomRightHandle);
//g.setColor(Color.red);
//g.drawRect(bottomRightRect.x,bottomRightRect.y,bottomRightRect.width,bottomRightRect.height);
			
		}
		if (bottomLeftHandle != null)
		{	g.setColor(KladjeVeld.hbColor);
			g.drawPolygon(bottomLeftHandle);
//g.setColor(Color.red);
//g.drawRect(bottomLeftRect.x,bottomLeftRect.y,bottomLeftRect.width,bottomLeftRect.height);
			
		}
		if (rotateEastHandle != null)
		{	g.setColor(KladjeVeld.hbColor);
			g.drawOval(handleBox.x + handleBox.width - 2 * hbFactor , handleBox.y + handleBox.height/2 - 2 * hbFactor, 
					   4 * hbFactor, 4 * hbFactor);
//g.setColor(Color.red);			
//g.drawRect(rotateEastHandle.x, rotateEastHandle.y, rotateEastHandle.width, rotateEastHandle.height);
		}
		if (rotateNorthHandle != null)
		{	g.setColor(KladjeVeld.hbColor);
			g.drawOval(handleBox.x + handleBox.width/2 - 2 * hbFactor, handleBox.y - 2 * hbFactor, 
					   4 * hbFactor, 4 * hbFactor);
//g.setColor(Color.red);			
//g.drawRect(rotateNorthHandle.x, rotateNorthHandle.y, rotateNorthHandle.width, rotateNorthHandle.height);
		}
		if (rotateWestHandle != null)
		{	g.setColor(KladjeVeld.hbColor);
			g.drawOval(handleBox.x - 2 * hbFactor, handleBox.y + handleBox.height/2 - 2 * hbFactor, 
					   4 * hbFactor, 4 * hbFactor);
//g.setColor(Color.red);			
//g.drawRect(rotateWestHandle.x, rotateWestHandle.y, rotateWestHandle.width, rotateWestHandle.height);
		}
		if (rotateSouthHandle != null)
		{	g.setColor(KladjeVeld.hbColor);
			g.drawOval(handleBox.x +handleBox.width/2 - 2 * hbFactor, handleBox.y + handleBox.height - 2 * hbFactor, 
					   4 * hbFactor, 4 * hbFactor);
//g.setColor(Color.red);			
//g.drawRect(rotateSouthHandle.x, rotateSouthHandle.y, rotateSouthHandle.width, rotateSouthHandle.height);

		}
		

	}
	
	public void tekenBB(Graphics2D g)
	{
		AffineTransform oldAT = g.getTransform();
		AffineTransform at = g.getTransform();

		at.rotate(rotation, cx, cy);
		
		g.setTransform(at);

		//float[] dash = new float[2];
		//dash[0] = 2;
		//dash[1] = 2;
		//g.setStroke(new BasicStroke(1.0f, 2, 0, 10.0f, dash, 0.0f));
		g.setStroke(new BasicStroke(0.8f));
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
		
		bb2.translate(dx, dy);
		makeHandleBox();

		
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
	
	public Polygon rotatePolygon(Polygon p, double rotation, double cx, double cy)
	{	
		Polygon r = new Polygon();
		
		for (int pCnt = 0; pCnt < p.npoints; pCnt++)
		{
			double rotX = Math.cos(rotation) * (p.xpoints[pCnt] - cx) - Math.sin(rotation) * (p.ypoints[pCnt] - cy);
			double rotY = Math.sin(rotation) * (p.xpoints[pCnt] - cx) + Math.cos(rotation) * (p.ypoints[pCnt] - cy);
						
			//puntenX[pCnt] = (int) Math.round(cx + rotX);
			//puntenY[pCnt] = (int) Math.round(cy + rotY);
			
			r.addPoint((int) Math.round(cx + rotX), (int) Math.round(cy + rotY));
			//doubleX[pCnt] = cx + rotX;
			//doubleY[pCnt] = cy + rotY;
			
		}
		
		return r;
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
	Polygon bb2;
	double cx, cy;
	double rotation = 0;
	double scaleX = 1;
	double scaleY = 1;
	int tekstX, tekstY;
	Rectangle handleBox;
	int hbFactor = 4;

	Polygon topRightHandle, bottomRightHandle, topLeftHandle, bottomLeftHandle;
	Rectangle topRightRect, bottomRightRect, topLeftRect, bottomLeftRect;
	Rectangle rotateEastHandle, rotateNorthHandle, rotateWestHandle, rotateSouthHandle;
	
	
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
		makeHandleBox();
	
	}

	public void zetTekst(String t)
	{
		tekst = new String(t);
		breedte = KladjeVeld.tekstFM.stringWidth(tekst);
		cx = xPos + ((double) breedte) / 2;
		cy = yPos + ((double) hoogte) / 2;
		
		makeBB();
		makeHandleBox();
		
	}

	public void makeHandleBox()
	{
		int minX = 1000;
		int maxX = -100;
		int minY = 1000;
		int maxY = -100;
		for (int pCnt = 0; pCnt < bb2.npoints; pCnt++)
		{
			if (bb2.xpoints[pCnt] < minX)
				minX = bb2.xpoints[pCnt];
			if (bb2.xpoints[pCnt] > maxX)
				maxX = bb2.xpoints[pCnt];
			if (bb2.ypoints[pCnt] < minY)
				minY = bb2.ypoints[pCnt];
			if (bb2.ypoints[pCnt] > maxY)
				maxY = bb2.ypoints[pCnt];
		}
		
		int w = maxX - minX + 2 * hbFactor;
		int h = maxY - minY + 2 * hbFactor;
		int dw = w - KladjeVeld.minHandleBoxSize;
		int dh = h - KladjeVeld.minHandleBoxSize;
		if ((dw >= 0) && (dh >= 0))
			handleBox = new Rectangle(minX - hbFactor, minY - hbFactor,w, h);
		else if ((dw >= 0) && (dh < 0))
			handleBox = new Rectangle(minX - hbFactor, minY - hbFactor + dh/2, w, KladjeVeld.minHandleBoxSize);
		else if ((dw < 0) && (dh >= 0))
			handleBox = new Rectangle(minX - hbFactor + dw/2, minY - hbFactor, KladjeVeld.minHandleBoxSize, h);
		else if ((dw < 0) && (dh < 0))
			handleBox = new Rectangle(minX - hbFactor + dw/2, minY - hbFactor + dh/2, KladjeVeld.minHandleBoxSize, KladjeVeld.minHandleBoxSize);	
		
		if (KladjeVeld.schalen)
			makeScaleHandles();
		if (KladjeVeld.roteren)
			makeRotateHandles();
		
	}
	
	public void makeScaleHandles()
	{
		topRightHandle = new Polygon();
		topRightHandle.addPoint(handleBox.x + handleBox.width + hbFactor,
								handleBox.y - hbFactor);
		topRightHandle.addPoint(handleBox.x + handleBox.width - 3 * hbFactor,
								handleBox.y - hbFactor);
		topRightHandle.addPoint(handleBox.x + handleBox.width + hbFactor,
								handleBox.y + 3 * hbFactor);
		topRightRect = new Rectangle(handleBox.x + handleBox.width - 3 * hbFactor,
									 handleBox.y - hbFactor, 4 * hbFactor, 4 * hbFactor);
		
		topLeftHandle = new Polygon();
		topLeftHandle.addPoint(handleBox.x - hbFactor, handleBox.y - hbFactor);
		topLeftHandle.addPoint(handleBox.x + 3 * hbFactor, handleBox.y - hbFactor);
		topLeftHandle.addPoint(handleBox.x - hbFactor, handleBox.y + 3 * hbFactor);
		topLeftRect = new Rectangle(handleBox.x - hbFactor, handleBox.y - hbFactor, 4 * hbFactor, 4 * hbFactor);
		
		bottomRightHandle = new Polygon();
		bottomRightHandle.addPoint(handleBox.x + handleBox.width + hbFactor,
								   handleBox.y + handleBox.height + hbFactor);
		bottomRightHandle.addPoint(handleBox.x + handleBox.width - 3 * hbFactor,
								   handleBox.y + handleBox.height + hbFactor);
		bottomRightHandle.addPoint(handleBox.x + handleBox.width + hbFactor,
								   handleBox.y + handleBox.height - 3 * hbFactor);
		bottomRightRect = new Rectangle(handleBox.x + handleBox.width - 3 * hbFactor,
				   						handleBox.y + handleBox.height - 3 * hbFactor, 4 * hbFactor, 4 * hbFactor);		
		
		bottomLeftHandle = new Polygon();
		bottomLeftHandle.addPoint(handleBox.x - hbFactor, handleBox.y + handleBox.height + hbFactor);
		bottomLeftHandle.addPoint(handleBox.x + 3 * hbFactor, handleBox.y + handleBox.height + hbFactor);
		bottomLeftHandle.addPoint(handleBox.x - hbFactor, handleBox.y + handleBox.height - 3 * hbFactor);
		bottomLeftRect = new Rectangle(handleBox.x - hbFactor, handleBox.y + handleBox.height - 3 * hbFactor, 
									   4 * hbFactor, 4 * hbFactor);		
		
	}

	public void killScaleHandles()
	{
		topRightHandle = null; 
		bottomRightHandle = null; 
		topLeftHandle = null;
		bottomLeftHandle = null;
		topRightRect = null;
		bottomRightRect = null;
		topLeftRect = null;
		bottomLeftRect = null;
		
	}

	public void makeRotateHandles()
	{
		rotateEastHandle = new Rectangle(handleBox.x + handleBox.width - 2 * hbFactor,
										 handleBox.y + handleBox.height/2 - 2 * hbFactor,
										 4 * hbFactor, 4 * hbFactor);
		rotateNorthHandle = new Rectangle(handleBox.x + handleBox.width/2 - 2 * hbFactor,
										  handleBox.y - 2 * hbFactor, 4 * hbFactor, 4 * hbFactor);
		rotateWestHandle = new Rectangle(handleBox.x - 2 * hbFactor, handleBox.y + handleBox.height/2 - 2 * hbFactor,
										 4 * hbFactor, 4 * hbFactor);
		rotateSouthHandle = new Rectangle(handleBox.x + handleBox.width/2 - 2 * hbFactor, 
                						  handleBox.y + handleBox.height - 2 * hbFactor,
                						  4 * hbFactor, 4 * hbFactor);
	}

	public void killRotateHandles()
	{
		rotateEastHandle = null; 
		rotateNorthHandle = null; 
		rotateWestHandle = null;
		rotateSouthHandle = null;
		
		
	}

	
	public void makeBB()
	{
		bb = new Rectangle(xPos - bbFactor, yPos - bbFactor, 
						   breedte + 2 * bbFactor, hoogte + 2 * bbFactor);
		
		bb2 = new Polygon();
		bb2.addPoint(xPos - bbFactor, yPos - bbFactor);
		bb2.addPoint(xPos - bbFactor + breedte + 2 * bbFactor, yPos - bbFactor);
		bb2.addPoint(xPos - bbFactor + breedte + 2 * bbFactor, yPos - bbFactor + hoogte + 2 * bbFactor);
		bb2.addPoint(xPos - bbFactor, yPos - bbFactor + hoogte + 2 * bbFactor);

	}

	public void rotate(double rotateStep)
	{	rotation += rotateStep;
		
		makeBB();
		bb2 = rotatePolygon(bb2, rotation, cx, cy);
		makeHandleBox();

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
		bb2 = rotatePolygon(bb2, rotation, cx, cy);
		makeHandleBox();
	
	}

	public void scale(double sx, double sy)
	{	
		// dit niet doen!!
		//xPos = (int) Math.round(sx * xPos + (1 - sx) * cx);
		//yPos = (int) Math.round(sy * yPos + (1 - sy) * cy);
		
		breedte = (int) Math.round(sx * breedte);
		hoogte = (int) Math.round(sy * hoogte);

		// dit niet doen!!
		//ascent = (int) Math.round(sy * ascent);
		
		tekstX = (int) Math.round((1/sx) * tekstX);// + (1 - (1/sx)) * cx);
		tekstY = (int) Math.round((1/sy) * tekstY);// + (1 - (1/sy)) * cy);
		
		scaleX *= sx;
		scaleY *= sy;
	
		makeBB();
		bb2 = rotatePolygon(bb2, rotation, cx, cy);
		makeHandleBox();
	
	}
	
	public Hashtable getState()
	{	Hashtable h = new Hashtable();
		
		h.put("kleur", kleur);
		h.put("kleurgwt", new String("rgb(" + kleur.getRed()+ "," + kleur.getGreen() + "," + kleur.getBlue() + ")"));		
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

	public void tekenHandleBox(Graphics2D g)
	{
		float[] dash = new float[2];
		dash[0] = 2;
		dash[1] = 2;
		g.setStroke(new BasicStroke(1.0f, 2, 0, 10.0f, dash, 0.0f));

		g.setColor(KladjeVeld.hbColor);
		
		g.drawRect(handleBox.x, handleBox.y, handleBox.width, handleBox.height);
		
		g.setStroke(new BasicStroke(1.5f, 2, 0, 10.0f, null, 0.0f));
		
		tekenHandles(g); 
	}
	
	public void tekenHandles(Graphics2D g)
	{
		if (topRightHandle != null)
		{	g.setColor(KladjeVeld.hbColor);
			g.drawPolygon(topRightHandle);
//g.setColor(Color.red);
//g.drawRect(topRightRect.x,topRightRect.y,topRightRect.width,topRightRect.height);
		}
		if (topLeftHandle != null)
		{	g.setColor(KladjeVeld.hbColor);
			g.drawPolygon(topLeftHandle);
//g.setColor(Color.red);
//g.drawRect(topLeftRect.x,topLeftRect.y,topLeftRect.width,topLeftRect.height);
			
		}
		if (bottomRightHandle != null)
		{	g.setColor(KladjeVeld.hbColor);
			g.drawPolygon(bottomRightHandle);
//g.setColor(Color.red);
//g.drawRect(bottomRightRect.x,bottomRightRect.y,bottomRightRect.width,bottomRightRect.height);
			
		}
		if (bottomLeftHandle != null)
		{	g.setColor(KladjeVeld.hbColor);
			g.drawPolygon(bottomLeftHandle);
//g.setColor(Color.red);
//g.drawRect(bottomLeftRect.x,bottomLeftRect.y,bottomLeftRect.width,bottomLeftRect.height);
			
		}
		if (rotateEastHandle != null)
		{	g.setColor(KladjeVeld.hbColor);
			g.drawOval(handleBox.x + handleBox.width - 2 * hbFactor , handleBox.y + handleBox.height/2 - 2 * hbFactor, 
					   4 * hbFactor, 4 * hbFactor);
//g.setColor(Color.red);			
//g.drawRect(rotateEastHandle.x, rotateEastHandle.y, rotateEastHandle.width, rotateEastHandle.height);
		}
		if (rotateNorthHandle != null)
		{	g.setColor(KladjeVeld.hbColor);
			g.drawOval(handleBox.x + handleBox.width/2 - 2 * hbFactor, handleBox.y - 2 * hbFactor, 
					   4 * hbFactor, 4 * hbFactor);
//g.setColor(Color.red);			
//g.drawRect(rotateNorthHandle.x, rotateNorthHandle.y, rotateNorthHandle.width, rotateNorthHandle.height);
		}
		if (rotateWestHandle != null)
		{	g.setColor(KladjeVeld.hbColor);
			g.drawOval(handleBox.x - 2 * hbFactor, handleBox.y + handleBox.height/2 - 2 * hbFactor, 
					   4 * hbFactor, 4 * hbFactor);
//g.setColor(Color.red);			
//g.drawRect(rotateWestHandle.x, rotateWestHandle.y, rotateWestHandle.width, rotateWestHandle.height);
		}
		if (rotateSouthHandle != null)
		{	g.setColor(KladjeVeld.hbColor);
			g.drawOval(handleBox.x +handleBox.width/2 - 2 * hbFactor, handleBox.y + handleBox.height - 2 * hbFactor, 
					   4 * hbFactor, 4 * hbFactor);
//g.setColor(Color.red);			
//g.drawRect(rotateSouthHandle.x, rotateSouthHandle.y, rotateSouthHandle.width, rotateSouthHandle.height);

		}
		

	}
	

	public void tekenBB(Graphics2D g)
	{

		AffineTransform oldAT = g.getTransform();
		AffineTransform at = g.getTransform();

		at.rotate(rotation, cx, cy);
		
		g.setTransform(at);
		
		//float[] dash = new float[2];
		//dash[0] = 2;
		//dash[1] = 2;
		//g.setStroke(new BasicStroke(1.0f, 2, 0, 10.0f, dash, 0.0f));
		g.setStroke(new BasicStroke(0.8f));
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
		
		bb2.translate(dx, dy);
		makeHandleBox();

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
	
	public Polygon rotatePolygon(Polygon p, double rotation, double cx, double cy)
	{	
		Polygon r = new Polygon();
		
		for (int pCnt = 0; pCnt < p.npoints; pCnt++)
		{
			double rotX = Math.cos(rotation) * (p.xpoints[pCnt] - cx) - Math.sin(rotation) * (p.ypoints[pCnt] - cy);
			double rotY = Math.sin(rotation) * (p.xpoints[pCnt] - cx) + Math.cos(rotation) * (p.ypoints[pCnt] - cy);
						
			//puntenX[pCnt] = (int) Math.round(cx + rotX);
			//puntenY[pCnt] = (int) Math.round(cy + rotY);
			
			r.addPoint((int) Math.round(cx + rotX), (int) Math.round(cy + rotY));
			//doubleX[pCnt] = cx + rotX;
			//doubleY[pCnt] = cy + rotY;
			
		}
		
		return r;
	}

}