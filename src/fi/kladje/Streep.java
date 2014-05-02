package fi.kladje;

import java.awt.*;
import java.util.*;
import java.awt.geom.*;


public class Streep 
{
	Color kleur;
	double[] puntenXD, puntenYD;
	double[] pXD, pYD;
	int bbFactor = 4;
	KladjePolygon bb;
	double cx = 0, cy = 0;
	double m00 = 1;
	double m01 = 0;
	double m10 = 0;
	double m11 = 1;

	double rotation = 0;
	Rectangle handleBox;
	int hbFactor = 4;
	int breedte, hoogte;
	
	Polygon topRightHandle, bottomRightHandle, topLeftHandle, bottomLeftHandle;
	Rectangle topRightRect, bottomRightRect, topLeftRect, bottomLeftRect;
	Rectangle rotateEastHandle, rotateWestHandle;
	
	
	public Streep(Color c, Vector punten)
	{	kleur = c;
		puntenXD = new double[punten.size()];
		puntenYD = new double[punten.size()];

		for (int pCnt = 0; pCnt < punten.size(); pCnt++)
		{	Point pt = (Point) punten.elementAt(pCnt);
			puntenXD[pCnt] = pt.x;
			puntenYD[pCnt] = pt.y;
			
			cx += puntenXD[pCnt];
			cy += puntenYD[pCnt];
		}
		
		cx /= puntenXD.length;
		cy /= puntenYD.length;

		maakStreep();
		//maakBBs();
		//makeHandleBox();
	}
	
	public Streep(Color c, int[] ptX, int[] ptY)
	{	kleur = c;
		puntenXD = new double[ptX.length];
		puntenYD = new double[ptY.length];

		for (int cnt = 0; cnt < ptX.length; cnt++) 
		{	puntenXD[cnt] = ptX[cnt];
			puntenYD[cnt] = ptY[cnt];
			
			cx += puntenXD[cnt];
			cy += puntenYD[cnt];
			
		}

		cx /= puntenXD.length;
		cy /= puntenYD.length;

		maakStreep();
		//maakBBs();
		//makeHandleBox();
	}

	public Streep(Color c, double[] ptXD, double[] ptYD)
	{	kleur = c;
		puntenXD = ptXD;
		puntenYD = ptYD;

		for (int cnt = 0; cnt < puntenXD.length; cnt++) 
		{				
			cx += puntenXD[cnt];
			cy += puntenYD[cnt];
			
		}

		cx /= puntenXD.length;
		cy /= puntenYD.length;
		
		maakStreep();
		//maakBBs();
		//makeHandleBox();
	}

	public void maakStreep()
	{
		pXD = new double[puntenXD.length];
		pYD = new double[puntenYD.length];
		double minX = 1000;
		double maxX = -100;
		double minY = 1000;
		double maxY = -100;
		for (int pCnt = 0; pCnt < puntenXD.length; pCnt++)
		{
			pXD[pCnt] = puntenXD[pCnt];
			pYD[pCnt] = puntenYD[pCnt];
			
			if (puntenXD[pCnt] < minX)
				minX = puntenXD[pCnt];
			if (puntenXD[pCnt] > maxX)
				maxX = puntenXD[pCnt];
			if (puntenYD[pCnt] < minY)
				minY = puntenYD[pCnt];
			if (puntenYD[pCnt] > maxY)
				maxY = puntenYD[pCnt];
		}
		breedte = (int) Math.round(maxX - minX);
		hoogte = (int) Math.round(maxY - minY);
		
		maakBBs();
		
		rotateStreep(rotation);
		
		bb.rotate(rotation, cx, cy);
		
		makeHandleBox();
	}
	
	
	public void makeHandleBox()
	{
		int minX = 1000;
		int maxX = -100;
		int minY = 1000;
		int maxY = -100;
		for (int pCnt = 0; pCnt < bb.aantalPunten; pCnt++)
		{
			if (bb.puntenX[pCnt] < minX)
				minX = bb.puntenX[pCnt];
			if (bb.puntenX[pCnt] > maxX)
				maxX = bb.puntenX[pCnt];
			if (bb.puntenY[pCnt] < minY)
				minY = bb.puntenY[pCnt];
			if (bb.puntenY[pCnt] > maxY)
				maxY = bb.puntenY[pCnt];
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
		rotateEastHandle = new Rectangle(handleBox.x + handleBox.width,// - 2 * hbFactor,
										 handleBox.y + handleBox.height/2 - 2 * hbFactor,
										 4 * hbFactor, 4 * hbFactor);
		rotateWestHandle = new Rectangle(handleBox.x - 4 * hbFactor, 
										 handleBox.y + handleBox.height/2 - 2 * hbFactor,
										 4 * hbFactor, 4 * hbFactor);
	}

	public void killRotateHandles()
	{
		rotateEastHandle = null; 
		rotateWestHandle = null;
	}
	
	
	public void maakBBs()
	{
		bb = new KladjePolygon();
		
		if (puntenXD.length == 1)
		{	bb.addPoint((int) Math.round(puntenXD[0] - bbFactor), (int) Math.round(puntenYD[0] - bbFactor));
			bb.addPoint((int) Math.round(puntenXD[0] + bbFactor), (int) Math.round(puntenYD[0] - bbFactor));
			bb.addPoint((int) Math.round(puntenXD[0] + bbFactor), (int) Math.round(puntenYD[0] + bbFactor));
			bb.addPoint((int) Math.round(puntenXD[0] - bbFactor), (int) Math.round(puntenYD[0] + bbFactor));
		}
		if (puntenXD.length > 1)
		{  
			
			// for loop 1
			for (int pCnt = 1; pCnt < puntenXD.length; pCnt++)
			{	
				double fromX = puntenXD[pCnt - 1];
				double fromY = puntenYD[pCnt - 1];
				double toX = puntenXD[pCnt];
				double toY = puntenYD[pCnt];
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
			for (int pCnt = puntenXD.length - 1; pCnt > 0; pCnt--)
			{	
				double fromX = puntenXD[pCnt - 1];
				double fromY = puntenYD[pCnt - 1];
				double toX = puntenXD[pCnt];
				double toY = puntenYD[pCnt];
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
		
		//bb2 = new Polygon(bb.xpoints,bb.ypoints,bb.npoints);
	}

	public void rotate(double rotateStep)
	{	rotation += rotateStep;
	
		
	
		for (int pCnt = 0; pCnt < puntenXD.length; pCnt++)
		{	
			double pXDNew = Math.cos(rotateStep) * (pXD[pCnt] - cx) - Math.sin(rotateStep) * (pYD[pCnt] - cy);
			double pYDNew = Math.sin(rotateStep) * (pXD[pCnt] - cx) + Math.cos(rotateStep) * (pYD[pCnt] - cy);
			pXD[pCnt] = pXDNew + cx;
			pYD[pCnt] = pYDNew + cy;
		}
		
		double m00New  = Math.cos(rotateStep) * m00 - Math.sin(rotateStep) * m10;
		double m10New  = Math.sin(rotateStep) * m00 + Math.cos(rotateStep) * m10;
		double m01New = Math.cos(rotateStep) * m01 - Math.sin(rotateStep) * m11;
		double m11New = Math.sin(rotateStep) * m01 + Math.cos(rotateStep) * m11;
		
		m00 = m00New;
		m01 = m01New;
		m10 = m10New;
		m11 = m11New;
		
		bb.rotate(rotateStep, cx, cy);
	
		//maakBBs();
		//bb = rotatePolygon(bb2, rotation, cx, cy);

		makeHandleBox();
		
		
		
	}
	
	public void rotateStreep(double rotation)
	{
		for (int pCnt = 0; pCnt < puntenXD.length; pCnt++)
		{	
			double pXDNew = Math.cos(rotation) * (pXD[pCnt] - cx) - Math.sin(rotation) * (pYD[pCnt] - cy);
			double pYDNew = Math.sin(rotation) * (pXD[pCnt] - cx) + Math.cos(rotation) * (pYD[pCnt] - cy);
			pXD[pCnt] = pXDNew + cx;
			pYD[pCnt] = pYDNew + cy;
		}
		
		double m00New  = Math.cos(rotation) * m00 - Math.sin(rotation) * m10;
		double m10New  = Math.sin(rotation) * m00 + Math.cos(rotation) * m10;
		double m01New = Math.cos(rotation) * m01 - Math.sin(rotation) * m11;
		double m11New = Math.sin(rotation) * m01 + Math.cos(rotation) * m11;
		
		m00 = m00New;
		m01 = m01New;
		m10 = m10New;
		m11 = m11New;
		
		
	}
	
	public void scale(double scaleStep)
	{
		for (int pCnt = 0; pCnt < puntenXD.length; pCnt++)
		{	
			pXD[pCnt] = scaleStep * pXD[pCnt] + (1 - scaleStep) * cx;
			pYD[pCnt] = scaleStep * pYD[pCnt] + (1 - scaleStep) * cy;
		}
		
		//maakBBs();
		//bb2 = rotatePolygon(bb2, rotation, cx, cy);
		
		//maakStreep();
		m00 *= scaleStep;
		m10 *= scaleStep;
		m01 *= scaleStep;
		m11 *= scaleStep;
		
		bb.scale(scaleStep, cx, cy);

		
		makeHandleBox();
		
	}

	public void scale(double scaleStepX, double scaleStepY)
	{
		for (int pCnt = 0; pCnt < puntenXD.length; pCnt++)
		{	
			pXD[pCnt] = scaleStepX * pXD[pCnt] + (1 - scaleStepX) * cx;
			pYD[pCnt] = scaleStepY * pYD[pCnt] + (1 - scaleStepY) * cy;
		}
		
		//maakBBs();
		//bb2 = rotatePolygon(bb2, rotation, cx, cy);
		
//		maakStreep();
		
		m00 *= scaleStepX;
		m10 *= scaleStepY;
		m01 *= scaleStepX;
		m11 *= scaleStepY;
		
		bb.scale(scaleStepX, scaleStepY, cx, cy);
		
		
		makeHandleBox();
		
	}
	
	public Hashtable getState()
	{	Hashtable h = new Hashtable();
		
		h.put("kleur", kleur);
		h.put("kleurgwt", new String("rgb(" + kleur.getRed()+ "," + kleur.getGreen() + "," + kleur.getBlue() + ")"));
		
		h.put("puntenXD", puntenXD);
		h.put("puntenYD", puntenYD);
		
//		h.put("rotation", new Double(rotation));
		
		h.put("m00", new Double(m00));
		h.put("m10", new Double(m10));
		h.put("m01", new Double(m01));
		h.put("m11", new Double(m11));
		
	
		return h;
	}
	
	public static Streep setState(Hashtable h)
	{
		Color kleur = Color.black;
		int[] puntenX = new int[0];
		int[] puntenY = new int[0];
		double[] puntenXD = null;
		double[] puntenYD = null;
		
		double rotation = 0;
		
		double m00 = 1;
		double m01 = 0;
		double m10 = 0;
		double m11 = 1;
		
		if (h.containsKey("kleur"))
			kleur = (Color) h.get("kleur");
		
		// backwards compatibility
		if (h.containsKey("puntenX"))
			puntenX = (int[]) h.get("puntenX");
		if (h.containsKey("puntenY"))
			puntenY = (int[]) h.get("puntenY");

		if (h.containsKey("puntenXD"))
			puntenXD = (double[]) h.get("puntenXD");
		if (h.containsKey("puntenYD"))
			puntenYD = (double[]) h.get("puntenYD");
		
		if (h.containsKey("rotation"))
			rotation = ((Double) h.get("rotation")).doubleValue();
		
		if (h.containsKey("m00"))
			m00 = ((Double) h.get("m00")).doubleValue();
		if (h.containsKey("m10"))
			m10 = ((Double) h.get("m10")).doubleValue();
		if (h.containsKey("m01"))
			m01 = ((Double) h.get("m01")).doubleValue();
		if (h.containsKey("m11"))
			m11 = ((Double) h.get("m11")).doubleValue();
		
		Streep streep = null;
		if (puntenXD != null)
		{	streep = new Streep(kleur, puntenXD, puntenYD);
		}
		else
			streep = new Streep(kleur, puntenX, puntenY);
		
		streep.rotation = rotation;
		streep.maakStreep();
		
		if (h.containsKey("rotation"))
		{	streep.rotate(rotation);
		}
		else if (h.containsKey("m00"))
		{	streep.transformBy(m00, m01, m10, m11);
		}
		
		return streep;
	}
	
	public void transformBy(double m00, double m01, double m10, double m11)
	{
		
		this.m00 = m00;
		this.m01 = m01;
		this.m10 = m10;
		this.m11 = m11;
		
		for (int pCnt = 0; pCnt < puntenXD.length; pCnt++)
		{	
			double pXDNew = m00 * (pXD[pCnt] - cx) + m01 * (pYD[pCnt] - cy);
			double pYDNew = m10 * (pXD[pCnt] - cx) + m11 * (pYD[pCnt] - cy);
			pXD[pCnt] = pXDNew + cx;
			pYD[pCnt] = pYDNew + cy;
			
		}
		
		bb.transformBy(m00, m01, m10, m11, cx, cy);
		
		makeHandleBox();
	}
	
	
	public void teken(Graphics2D g)
	{
		
		g.setColor(kleur);		
		
		if (puntenXD.length == 1)
		{	g.drawLine((int) Math.round(pXD[0]), (int) Math.round(pYD[0]), 
				       (int) Math.round(pXD[0]), (int) Math.round(pYD[0]));
		}
		if (puntenXD.length > 1)
		{	for (int pCnt = 1; pCnt < puntenXD.length; pCnt++)
			{	g.drawLine((int) Math.round(pXD[pCnt - 1]), (int) Math.round(pYD[pCnt - 1]), 
						   (int) Math.round(pXD[pCnt]), (int) Math.round(pYD[pCnt]));
			}
			
		}
		
		//g.setTransform(oldAT);
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
			g.drawOval(handleBox.x + handleBox.width, // - 2 * hbFactor,
					   handleBox.y + handleBox.height/2 - 2 * hbFactor, 
					   4 * hbFactor, 4 * hbFactor);
//g.setColor(Color.red);			
//g.drawRect(rotateEastHandle.x, rotateEastHandle.y, rotateEastHandle.width, rotateEastHandle.height);
		}
		if (rotateWestHandle != null)
		{	g.setColor(KladjeVeld.hbColor);
			g.drawOval(handleBox.x - 4 * hbFactor, 
					   handleBox.y + handleBox.height/2 - 2 * hbFactor, 
					   4 * hbFactor, 4 * hbFactor);
//g.setColor(Color.red);			
//g.drawRect(rotateWestHandle.x, rotateWestHandle.y, rotateWestHandle.width, rotateWestHandle.height);
		}

	}
	
	public void tekenBB(Graphics2D g)
	{
		//AffineTransform oldAT = g.getTransform();
		//AffineTransform at = g.getTransform();

		//at.rotate(rotation, cx, cy);
		
		//g.setTransform(at);

		//float[] dash = new float[2];
		//dash[0] = 2;
		//dash[1] = 2;
		//g.setStroke(new BasicStroke(1.0f, 2, 0, 10.0f, dash, 0.0f));
		g.setStroke(new BasicStroke(0.8f));
//		g.setColor(KladjeVeld.bbColor);

		bb.draw(g, KladjeVeld.bbColor, null);
				
		g.setStroke(new BasicStroke(1.5f, 2, 0, 10.0f, null, 0.0f));
		
		//g.setTransform(oldAT);		
	}

	public double inverseRotX(double x, double y)
	{
		double rotX = Math.cos(-rotation) * x - Math.sin(- rotation) * y;
		
		return rotX;
		
	}
	
	public double inverseRotY(double x, double y)
	{
		double rotY = Math.sin(-rotation) * x + Math.cos(- rotation) * y;
		
		return rotY;
	}
	
	public boolean bbContains(int x, int y)
	{

		return bb.contains(x, y);
	}
	
	public void translate(int dx, int dy)
	{
		for (int pCnt = 0; pCnt < puntenXD.length; pCnt++)
		{	puntenXD[pCnt] += dx;
			puntenYD[pCnt] += dy;
			pXD[pCnt] += dx;
			pYD[pCnt] += dy; 
		}

		cx += dx;
		cy += dy;

		bb.translate(dx, dy);
		
		//bb2.translate(dx, dy);
		makeHandleBox();
	}
	
	public boolean isContainedIn(Rectangle r)
	{
		if (r == null)
			return false;		
		
		boolean isContainedIn = true;
		
		for (int pCnt = 0; pCnt < bb.aantalPunten; pCnt++)
		{
			//int bbX = inverseTransformX(bb.xpoints[pCnt], bb.ypoints[pCnt]);
			//int bbY = inverseTransformY(bb.xpoints[pCnt], bb.ypoints[pCnt]);
			
			isContainedIn = isContainedIn && r.contains(bb.puntenX[pCnt], bb.puntenY[pCnt]);
			//isContainedIn = isContainedIn && r.contains(bb.xpoints[pCnt], bb.ypoints[pCnt]);
		}
		
		return isContainedIn;
	}


}


class Lijn
{
	Color kleur;
	int fromX, fromY, toX, toY;
	int fX, fY, tX, tY;
	int bbFactor = 4;
	KladjePolygon bb; 
	double cx, cy;
	double m00 = 1;
	double m01 = 0;
	double m10 = 0;
	double m11 = 1;

	double rotation = 0;
	Rectangle handleBox;
	int hbFactor = 4;
	
	Polygon topRightHandle, bottomRightHandle, topLeftHandle, bottomLeftHandle;
	Rectangle topRightRect, bottomRightRect, topLeftRect, bottomLeftRect;
	Rectangle rotateEastHandle, rotateWestHandle;
	
	
	public Lijn(Color c, int fromX, int fromY, int toX, int toY)
	{
		kleur = c;
		this.fromX = fromX;
		this.fromY = fromY;
		this.toX = toX;
		this.toY = toY;

		cx = ((double) fromX + (double) toX) / 2;
		cy = ((double) fromY + (double) toY) / 2;
		
		
		maakLijn();
		
		//makeBB();
		//makeHandleBox();
	}
	
	public void maakLijn()
	{
		fX = fromX;
		fY = fromY;
		tX = toX;
		tY = toY;
				
		makeBB();
		
		rotateLijn(rotation);
		
		bb.rotate(rotation, cx, cy);
		
		makeHandleBox();
	}
	
	public void makeHandleBox()
	{
		int minX = 1000;
		int maxX = -100;
		int minY = 1000;
		int maxY = -100;
		for (int pCnt = 0; pCnt < bb.aantalPunten; pCnt++)
		{
			if (bb.puntenX[pCnt] < minX)
				minX = bb.puntenX[pCnt];
			if (bb.puntenX[pCnt] > maxX)
				maxX = bb.puntenX[pCnt];
			if (bb.puntenY[pCnt] < minY)
				minY = bb.puntenY[pCnt];
			if (bb.puntenY[pCnt] > maxY)
				maxY = bb.puntenY[pCnt];
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
		rotateEastHandle = new Rectangle(handleBox.x + handleBox.width,// - 2 * hbFactor,
										 handleBox.y + handleBox.height/2 - 2 * hbFactor,
										 4 * hbFactor, 4 * hbFactor);
		rotateWestHandle = new Rectangle(handleBox.x - 4 * hbFactor, 
				                         handleBox.y + handleBox.height/2 - 2 * hbFactor,
										 4 * hbFactor, 4 * hbFactor);
	}

	public void killRotateHandles()
	{
		rotateEastHandle = null; 
		rotateWestHandle = null;
		
	}

	public void makeBB()
	{
		bb = new KladjePolygon();
		
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
	
		double fXNew = Math.cos(rotateStep) * (fX - cx) - Math.sin(rotateStep) * (fY - cy);
		double fYNew = Math.sin(rotateStep) * (fX - cx) + Math.cos(rotateStep) * (fY - cy);
		double tXNew = Math.cos(rotateStep) * (tX - cx) - Math.sin(rotateStep) * (tY - cy);
		double tYNew = Math.sin(rotateStep) * (tX - cx) + Math.cos(rotateStep) * (tY - cy);
		fX = (int) Math.round(fXNew + cx);
		fY = (int) Math.round(fYNew + cy);
		tX = (int) Math.round(tXNew + cx);
		tY = (int) Math.round(tYNew + cy);
		
		double m00New  = Math.cos(rotateStep) * m00 - Math.sin(rotateStep) * m10;
		double m10New  = Math.sin(rotateStep) * m00 + Math.cos(rotateStep) * m10;
		double m01New = Math.cos(rotateStep) * m01 - Math.sin(rotateStep) * m11;
		double m11New = Math.sin(rotateStep) * m01 + Math.cos(rotateStep) * m11;
		
		m00 = m00New;
		m01 = m01New;
		m10 = m10New;
		m11 = m11New;
		
	
		bb.rotate(rotateStep, cx, cy);
		
		//makeBB();
		//bb2 = rotatePolygon(bb2, rotation, cx, cy);

		makeHandleBox();
	
	}

	public void rotateLijn(double rotation)
	{		
		double fXNew = Math.cos(rotation) * (fX - cx) - Math.sin(rotation) * (fY - cy);
		double fYNew = Math.sin(rotation) * (fX - cx) + Math.cos(rotation) * (fY - cy);
		double tXNew = Math.cos(rotation) * (tX - cx) - Math.sin(rotation) * (tY - cy);
		double tYNew = Math.sin(rotation) * (tX - cx) + Math.cos(rotation) * (tY - cy);
		fX = (int) Math.round(fXNew + cx);
		fY = (int) Math.round(fYNew + cy);
		tX = (int) Math.round(tXNew + cx);
		tY = (int) Math.round(tYNew + cy);

		double m00New  = Math.cos(rotation) * m00 - Math.sin(rotation) * m10;
		double m10New  = Math.sin(rotation) * m00 + Math.cos(rotation) * m10;
		double m01New = Math.cos(rotation) * m01 - Math.sin(rotation) * m11;
		double m11New = Math.sin(rotation) * m01 + Math.cos(rotation) * m11;
		
		m00 = m00New;
		m01 = m01New;
		m10 = m10New;
		m11 = m11New;
		
	}	
	
	public void scale(double scaleStep)
	{	
		fX = (int) Math.round(scaleStep * fX + (1 - scaleStep) * cx);
		fY = (int) Math.round(scaleStep * fY + (1 - scaleStep) * cy);
		tX = (int) Math.round(scaleStep * tX + (1 - scaleStep) * cx);
		tY = (int) Math.round(scaleStep * tY + (1 - scaleStep) * cy);
		
		//makeBB();
		//bb2 = rotatePolygon(bb2, rotation, cx, cy);
		//maakLijn();
		
		m00 *= scaleStep;
		m10 *= scaleStep;
		m01 *= scaleStep;
		m11 *= scaleStep;
		
		bb.scale(scaleStep, scaleStep, cx, cy);

		
		makeHandleBox();
		
	}

	public void scale(double sx, double sy)
	{	
		fX = (int) Math.round(sx * fX + (1 - sx) * cx);
		fY = (int) Math.round(sy * fY + (1 - sy) * cy);
		tX = (int) Math.round(sx * tX + (1 - sx) * cx);
		tY = (int) Math.round(sy * tY + (1 - sy) * cy);
		
		//makeBB();
		//bb2 = rotatePolygon(bb2, rotation, cx, cy);
		
		//maakLijn();
		
		m00 *= sx;
		m10 *= sy;
		m01 *= sx;
		m11 *= sy;
		
		bb.scale(sx, sy, cx, cy);
		
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
//		h.put("rotation", new Double(rotation));
		
		h.put("m00", new Double(m00));
		h.put("m10", new Double(m10));
		h.put("m01", new Double(m01));
		h.put("m11", new Double(m11));
	
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

		double m00 = 1;
		double m01 = 0;
		double m10 = 0;
		double m11 = 1;
		
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

		if (h.containsKey("m00"))
			m00 = ((Double) h.get("m00")).doubleValue();
		if (h.containsKey("m10"))
			m10 = ((Double) h.get("m10")).doubleValue();
		if (h.containsKey("m01"))
			m01 = ((Double) h.get("m01")).doubleValue();
		if (h.containsKey("m11"))
			m11 = ((Double) h.get("m11")).doubleValue();
		
		Lijn lijn = new Lijn(kleur, fromX, fromY, toX, toY);
		lijn.rotation = rotation;
		lijn.maakLijn();

		if (h.containsKey("rotation"))
		{	lijn.rotate(rotation);
		}
		else if (h.containsKey("m00"))
		{	lijn.transformBy(m00, m01, m10, m11);
		}
		
		return lijn;
	}
	
	public void transformBy(double m00, double m01, double m10, double m11)
	{
		this.m00 = m00;
		this.m01 = m01;
		this.m10 = m10;
		this.m11 = m11;
		
		double fXNew = m00 * (fX - cx) + m01 * (fY - cy);
		double fYNew = m10 * (fX - cx) + m11 * (fY - cy);
		fX = (int) Math.round(fXNew + cx);
		fY = (int) Math.round(fYNew + cy);
		double tXNew = m00 * (tX - cx) + m01 * (tY - cy);
		double tYNew = m10 * (tX - cx) + m11 * (tY - cy);
		tX = (int) Math.round(tXNew + cx);
		tY = (int) Math.round(tYNew + cy);

		
		bb.transformBy(m00, m01, m10, m11, cx, cy);
		
		makeHandleBox();
		
		
	}
		
	
	public void teken(Graphics2D g)
	{
		
		g.setColor(kleur);
		g.drawLine(fX, fY, tX, tY);
		
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
		}
		if (topLeftHandle != null)
		{	g.setColor(KladjeVeld.hbColor);
			g.drawPolygon(topLeftHandle);
			
		}
		if (bottomRightHandle != null)
		{	g.setColor(KladjeVeld.hbColor);
			g.drawPolygon(bottomRightHandle);
			
		}
		if (bottomLeftHandle != null)
		{	g.setColor(KladjeVeld.hbColor);
			g.drawPolygon(bottomLeftHandle);
			
		}
		if (rotateEastHandle != null)
		{	g.setColor(KladjeVeld.hbColor);
			g.drawOval(handleBox.x + handleBox.width, // - 2 * hbFactor,
					   handleBox.y + handleBox.height/2 - 2 * hbFactor, 
					   4 * hbFactor, 4 * hbFactor);
		}
		if (rotateWestHandle != null)
		{	g.setColor(KladjeVeld.hbColor);
			g.drawOval(handleBox.x - 4 * hbFactor, 
					   handleBox.y + handleBox.height/2 - 2 * hbFactor, 
					   4 * hbFactor, 4 * hbFactor);
		}

	}
	
	public void tekenBB(Graphics2D g)
	{
		g.setStroke(new BasicStroke(0.8f));
		
		bb.draw(g, KladjeVeld.bbColor, null);
				
		g.setStroke(new BasicStroke(1.5f, 2, 0, 10.0f, null, 0.0f));
		
	}

	public double inverseRotX(double x, double y)
	{
		double rotX = Math.cos(-rotation) * x - Math.sin(- rotation) * y;
		
		return rotX;
		
	}
	
	public double inverseRotY(double x, double y)
	{
		double rotY = Math.sin(-rotation) * x + Math.cos(- rotation) * y;
		
		return rotY;
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
		cx += dx;
		cy += dy;

		fX += dx;
		fY += dy;
		tX += dx; 
		tY += dy;
		
		bb.translate(dx, dy);

		makeHandleBox();

	}	

	public boolean isContainedIn(Rectangle r)
	{
		if (r == null)
			return false;
		
		boolean isContainedIn = true;
		
		for (int pCnt = 0; pCnt < bb.aantalPunten; pCnt++)
		{
			
			isContainedIn = isContainedIn && r.contains(bb.puntenX[pCnt], bb.puntenY[pCnt]);

		}
		
		return isContainedIn;
	}	
	

}
class Rechthoek
{	Color kleur;
	int topLeftX, topLeftY, breedte, hoogte;
	KladjePolygon rechthoek;
	int bbFactor = 4;
	KladjePolygon outerRechthoek;
	KladjePolygon innerRechthoek;
	
	double cx, cy;
	double m00 = 1;
	double m01 = 0;
	double m10 = 0;
	double m11 = 1;

	double rotation = 0;
	
	Rectangle handleBox;
	int hbFactor = 4;

	Polygon topRightHandle, bottomRightHandle, topLeftHandle, bottomLeftHandle;
	Rectangle topRightRect, bottomRightRect, topLeftRect, bottomLeftRect;
	Rectangle rotateEastHandle, rotateWestHandle;
	
	public Rechthoek(Color c, int x, int y, int w, int h)
	{
		kleur = c;
		topLeftX = x;
		topLeftY = y;
		breedte = w;
		hoogte = h;
		
		cx = topLeftX + ((double) breedte) / 2;
		cy = topLeftY + ((double) hoogte) / 2;

		maakRechthoek();
		makeBB();
		makeHandleBox();
		
	}

	public void maakRechthoek()
	{
		rechthoek = new KladjePolygon();
		rechthoek.addPoint(topLeftX, topLeftY);
		rechthoek.addPoint(topLeftX + breedte, topLeftY);
		rechthoek.addPoint(topLeftX + breedte, topLeftY + hoogte);
		rechthoek.addPoint(topLeftX, topLeftY + hoogte);
		
		makeBB();
		
		rechthoek.rotate(rotation, cx, cy);
		outerRechthoek.rotate(rotation, cx, cy);
		innerRechthoek.rotate(rotation, cx, cy);
		
		makeHandleBox();
	}
	
	public void makeHandleBox()
	{
		int minX = 1000;
		int maxX = -100;
		int minY = 1000;
		int maxY = -100;
		for (int pCnt = 0; pCnt < outerRechthoek.aantalPunten; pCnt++)
		{
			if (outerRechthoek.puntenX[pCnt] < minX)
				minX = outerRechthoek.puntenX[pCnt];
			if (outerRechthoek.puntenX[pCnt] > maxX)
				maxX = outerRechthoek.puntenX[pCnt];
			if (outerRechthoek.puntenY[pCnt] < minY)
				minY = outerRechthoek.puntenY[pCnt];
			if (outerRechthoek.puntenY[pCnt] > maxY)
				maxY = outerRechthoek.puntenY[pCnt];
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
		rotateEastHandle = new Rectangle(handleBox.x + handleBox.width, // - 2 * hbFactor,
										 handleBox.y + handleBox.height/2 - 2 * hbFactor,
										 4 * hbFactor, 4 * hbFactor);
		rotateWestHandle = new Rectangle(handleBox.x - 4 * hbFactor, 
										 handleBox.y + handleBox.height/2 - 2 * hbFactor,
										 4 * hbFactor, 4 * hbFactor);
	}

	public void killRotateHandles()
	{
		rotateEastHandle = null; 
		rotateWestHandle = null;
		
	}

	public void makeBB()
	{
		outerRechthoek = new KladjePolygon();
		outerRechthoek.addPoint(topLeftX - bbFactor, topLeftY - bbFactor);
		outerRechthoek.addPoint(topLeftX + breedte + bbFactor, topLeftY - bbFactor);
		outerRechthoek.addPoint(topLeftX + breedte + bbFactor, topLeftY + hoogte + bbFactor);
		outerRechthoek.addPoint(topLeftX - bbFactor, topLeftY + hoogte + bbFactor);
		
		innerRechthoek = new KladjePolygon();
		innerRechthoek.addPoint(topLeftX + bbFactor, topLeftY + bbFactor);
		innerRechthoek.addPoint(topLeftX + breedte - bbFactor, topLeftY + bbFactor);
		innerRechthoek.addPoint(topLeftX + breedte - bbFactor, topLeftY + hoogte - bbFactor);
		innerRechthoek.addPoint(topLeftX + bbFactor, topLeftY + hoogte - bbFactor);		
		
	}
	
	public void rotate(double rotateStep)
	{	
		rotation += rotateStep;
		
		double m00New  = Math.cos(rotateStep) * m00 - Math.sin(rotateStep) * m10;
		double m10New  = Math.sin(rotateStep) * m00 + Math.cos(rotateStep) * m10;
		double m01New = Math.cos(rotateStep) * m01 - Math.sin(rotateStep) * m11;
		double m11New = Math.sin(rotateStep) * m01 + Math.cos(rotateStep) * m11;
		
		m00 = m00New;
		m01 = m01New;
		m10 = m10New;
		m11 = m11New;
		
		rechthoek.rotate(rotateStep, cx, cy);
		outerRechthoek.rotate(rotateStep, cx, cy);
		innerRechthoek.rotate(rotateStep, cx, cy);
		
		makeHandleBox();

	}
	
	public void scale(double scaleStep)
	{	
//		topLeftX = (int) Math.round(scaleStep * topLeftX + (1 - scaleStep) * cx);
//		topLeftY = (int) Math.round(scaleStep * topLeftY + (1 - scaleStep) * cy);
//		breedte = (int) Math.round(scaleStep * breedte);
//		hoogte = (int) Math.round(scaleStep * hoogte);
		
		//maakRechthoek();
		
		m00 *= scaleStep;
		m10 *= scaleStep;
		m01 *= scaleStep;
		m11 *= scaleStep;
		
		rechthoek.scale(scaleStep, cx, cy);
		outerRechthoek.scale(scaleStep, cx, cy);
		innerRechthoek.scale(scaleStep, cx, cy);
		
		makeHandleBox();
		
	}
	
	public void scale(double sx, double sy)
	{
		//topLeftX = (int) Math.round(sx * topLeftX + (1 - sx) * cx);
		//topLeftY = (int) Math.round(sy * topLeftY + (1 - sy) * cy);
		//breedte = (int) Math.round(sx * breedte);
		//hoogte = (int) Math.round(sy * hoogte);

		//maakRechthoek();

		m00 *= sx;
		m01 *= sx;
		m10 *= sy;
		m11 *= sy;
		
		rechthoek.scale(sx, sy, cx, cy);
		outerRechthoek.scale(sx, sy, cx, cy);
		innerRechthoek.scale(sx,sy, cx, cy);
		
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

		//h.put("rotation", new Double(rotation));
		
		h.put("m00", new Double(m00));
		h.put("m10", new Double(m10));
		h.put("m01", new Double(m01));
		h.put("m11", new Double(m11));
		
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

		double m00 = 1;
		double m01 = 0;
		double m10 = 0;
		double m11 = 1;
		
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
		
		// backwards compatibility
		if (h.containsKey("rotation"))
			rotation = ((Double) h.get("rotation")).doubleValue();
		
		if (h.containsKey("m00"))
			m00 = ((Double) h.get("m00")).doubleValue();
		if (h.containsKey("m10"))
			m10 = ((Double) h.get("m10")).doubleValue();
		if (h.containsKey("m01"))
			m01 = ((Double) h.get("m01")).doubleValue();
		if (h.containsKey("m11"))
			m11 = ((Double) h.get("m11")).doubleValue();
		
		Rechthoek rechthoek = new Rechthoek(kleur, topLeftX, topLeftY, breedte, hoogte);
		rechthoek.rotation = rotation;
		rechthoek.maakRechthoek();

		
		if (h.containsKey("rotation"))
		{	rechthoek.rotate(rotation);
		}
		else if (h.containsKey("m00"))
		{	rechthoek.transformBy(m00, m01, m10, m11);
		}
		
		return rechthoek;
	}

	public void transformBy(double m00, double m01, double m10, double m11)
	{
		this.m00 = m00;
		this.m01 = m01;
		this.m10 = m10;
		this.m11 = m11;
		
		rechthoek.transformBy(m00, m01, m10, m11, cx, cy);
		outerRechthoek.transformBy(m00, m01, m10, m11, cx, cy);
		innerRechthoek.transformBy(m00, m01, m10, m11, cx, cy);
		
		makeHandleBox();
		
	}
	
	public void teken(Graphics2D g)
	{
		g.setColor(kleur);
		rechthoek.draw(g, kleur, null);

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
		}
		if (topLeftHandle != null)
		{	g.setColor(KladjeVeld.hbColor);
			g.drawPolygon(topLeftHandle);
		}
		if (bottomRightHandle != null)
		{	g.setColor(KladjeVeld.hbColor);
			g.drawPolygon(bottomRightHandle);
		}
		if (bottomLeftHandle != null)
		{	g.setColor(KladjeVeld.hbColor);
			g.drawPolygon(bottomLeftHandle);
		}
		if (rotateEastHandle != null)
		{	g.setColor(KladjeVeld.hbColor);
			g.drawOval(handleBox.x + handleBox.width, // - 2 * hbFactor,
					   handleBox.y + handleBox.height/2 - 2 * hbFactor, 
					   4 * hbFactor, 4 * hbFactor);
		}
		if (rotateWestHandle != null)
		{	g.setColor(KladjeVeld.hbColor);
			g.drawOval(handleBox.x - 4 * hbFactor,
					   handleBox.y + handleBox.height/2 - 2 * hbFactor, 
					   4 * hbFactor, 4 * hbFactor);
		}
		

	}

	public void tekenBB(Graphics2D g)
	{
		
		g.setStroke(new BasicStroke(0.8f));

		outerRechthoek.draw(g, KladjeVeld.bbColor, null);
		innerRechthoek.draw(g, KladjeVeld.bbColor, null);
		
		g.setStroke(new BasicStroke(1.5f, 2, 0, 10.0f, null, 0.0f));
		
	}
	
	public double inverseRotX(double x, double y)
	{
		double rotX = Math.cos(-rotation) * x - Math.sin(- rotation) * y;
		
		return rotX;
		
	}
	
	public double inverseRotY(double x, double y)
	{
		double rotY = Math.sin(-rotation) * x + Math.cos(- rotation) * y;
		
		return rotY;
	}
	
	public boolean bbContains(int x, int y)
	{
		
		return outerRechthoek.contains(x, y) && !innerRechthoek.contains(x, y);
	}

	public void translate(int dx, int dy)
	{
		topLeftX += dx;
		topLeftY += dy;
		cx += dx;
		cy += dy;
		
		rechthoek.translate(dx, dy);
		outerRechthoek.translate(dx, dy);
		innerRechthoek.translate(dx, dy);
		
		makeHandleBox();

	}	

	public boolean isContainedIn(Rectangle r)
	{
		if (r == null)
			return false;
		
		boolean isContainedIn = true;
		for (int cnt = 0; cnt < outerRechthoek.aantalPunten; cnt++)
		{
			isContainedIn = isContainedIn && 
							r.contains(outerRechthoek.puntenX[cnt], outerRechthoek.puntenY[cnt]);
		}
		
		return isContainedIn;
	}

	
}
class Ellips
{	Color kleur;
	int topLeftX, topLeftY, breedte, hoogte;
	int bbFactor = 4;
	KladjePolygon ellips, outerEllips, innerEllips;
	double cx, cy;
	double m00 = 1;
	double m01 = 0;
	double m10 = 0;
	double m11 = 1;
	
	double rotation = 0;
	Rectangle handleBox;
	int hbFactor = 4;
	int steps = 75;

	Polygon topRightHandle, bottomRightHandle, topLeftHandle, bottomLeftHandle;
	Rectangle topRightRect, bottomRightRect, topLeftRect, bottomLeftRect;
	Rectangle rotateEastHandle, rotateWestHandle;
	
	public Ellips(Color c, int x, int y, int w, int h)
	{
		kleur = c;
		topLeftX = x;
		topLeftY = y;
		breedte = w;
		hoogte = h;
		
		cx = topLeftX + ((double) breedte) / 2;
		cy = topLeftY + ((double) hoogte) / 2;
		
		makeEllips();
	}
	
	public void makeEllips()
	{
		double angleStep = 2 * Math.PI / steps;
		
		ellips = new KladjePolygon();
		for (int pCnt = 0; pCnt <= steps; pCnt++)
		{
			double x = cx + (breedte / 2) * Math.cos(pCnt * angleStep);
			double y = cy - (hoogte / 2) * Math.sin(pCnt * angleStep);
			ellips.addPoint(x, y);
		}
		
		makeOuterInner();
		
		ellips.rotate(rotation, cx, cy);
		outerEllips.rotate(rotation, cx, cy);
		innerEllips.rotate(rotation, cx, cy);
		
		makeHandleBox();
	}
	

	public void makeHandleBox()
	{
		int minX = 1000;
		int maxX = -100;
		int minY = 1000;
		int maxY = -100;
		for (int pCnt = 0; pCnt < outerEllips.aantalPunten; pCnt++)
		{
			if (outerEllips.puntenX[pCnt] < minX)
				minX = outerEllips.puntenX[pCnt];
			if (outerEllips.puntenX[pCnt] > maxX)
				maxX = outerEllips.puntenX[pCnt];
			if (outerEllips.puntenY[pCnt] < minY)
				minY = outerEllips.puntenY[pCnt];
			if (outerEllips.puntenY[pCnt] > maxY)
				maxY = outerEllips.puntenY[pCnt];
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
		rotateEastHandle = new Rectangle(handleBox.x + handleBox.width, // - 2 * hbFactor,
										 handleBox.y + handleBox.height/2 - 2 * hbFactor,
										 4 * hbFactor, 4 * hbFactor);
		rotateWestHandle = new Rectangle(handleBox.x - 4 * hbFactor, 
				                         handleBox.y + handleBox.height/2 - 2 * hbFactor,
										 4 * hbFactor, 4 * hbFactor);
	}

	public void killRotateHandles()
	{
		rotateEastHandle = null; 
		rotateWestHandle = null;
		
		
	}
	
	
	public void makeOuterInner()
	{
		double angleStep = 2 * Math.PI / steps;
		
		outerEllips = new KladjePolygon();
		for (int pCnt = 0; pCnt <= steps; pCnt++)
		{
			double x = cx + (breedte/2 + bbFactor) * Math.cos(pCnt * angleStep);
			double y = cy - (hoogte/2 + bbFactor) * Math.sin(pCnt * angleStep);
			outerEllips.addPoint(x, y);
		}

		innerEllips = new KladjePolygon();
		for (int pCnt = 0; pCnt <= steps; pCnt++)
		{
			double x = cx + (breedte/2 - bbFactor) * Math.cos(pCnt * angleStep);
			double y = cy - (hoogte/2 - bbFactor) * Math.sin(pCnt * angleStep);
			innerEllips.addPoint(x, y);
		}
		
	}
	
	public void rotate(double rotateStep)
	{	rotation += rotateStep;

		double m00New  = Math.cos(rotateStep) * m00 - Math.sin(rotateStep) * m10;
		double m10New  = Math.sin(rotateStep) * m00 + Math.cos(rotateStep) * m10;
		double m01New = Math.cos(rotateStep) * m01 - Math.sin(rotateStep) * m11;
		double m11New = Math.sin(rotateStep) * m01 + Math.cos(rotateStep) * m11;
	
		m00 = m00New;
		m01 = m01New;
		m10 = m10New;
		m11 = m11New;
	
		ellips.rotate(rotateStep, cx, cy);
		outerEllips.rotate(rotateStep, cx, cy);
		innerEllips.rotate(rotateStep, cx, cy);

		makeHandleBox();

	}
	
	public void scale(double scaleStep)
	{	
		//topLeftX = (int) Math.round(scaleStep * topLeftX + (1 - scaleStep) * cx);
		//topLeftY = (int) Math.round(scaleStep * topLeftY + (1 - scaleStep) * cy);
		//breedte = (int) Math.round(scaleStep * breedte);
		//hoogte = (int) Math.round(scaleStep * hoogte);
		
		//makeEllips();

		m00 *= scaleStep;
		m01 *= scaleStep;
		m10 *= scaleStep;
		m11 *= scaleStep;

		ellips.scale(scaleStep, cx, cy);
		outerEllips.scale(scaleStep, cx, cy);
		innerEllips.scale(scaleStep, cx, cy);
		

		makeHandleBox();
		
	}
	
	public void scale(double sx, double sy)
	{	
		//topLeftX = (int) Math.round(sx * topLeftX + (1 - sx) * cx);
		//topLeftY = (int) Math.round(sy * topLeftY + (1 - sy) * cy);
		//breedte = (int) Math.round(sx * breedte);
		//hoogte = (int) Math.round(sy * hoogte);
		
		//makeEllips();

		m00 *= sx;
		m01 *= sx;
		m10 *= sy;
		m11 *= sy;

		ellips.scale(sx, sy, cx, cy);
		outerEllips.scale(sx, sy, cx, cy);
		innerEllips.scale(sx, sy, cx, cy);
		
		
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
		
		//h.put("rotation", new Double(rotation));

		h.put("m00", new Double(m00));
		h.put("m10", new Double(m10));
		h.put("m01", new Double(m01));
		h.put("m11", new Double(m11));

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

		double m00 = 1;
		double m01 = 0;
		double m10 = 0;
		double m11 = 1;

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

		if (h.containsKey("m00"))
			m00 = ((Double) h.get("m00")).doubleValue();
		if (h.containsKey("m10"))
			m10 = ((Double) h.get("m10")).doubleValue();
		if (h.containsKey("m01"))
			m01 = ((Double) h.get("m01")).doubleValue();
		if (h.containsKey("m11"))
			m11 = ((Double) h.get("m11")).doubleValue();
		
		Ellips ellips = new Ellips(kleur, topLeftX, topLeftY, breedte, hoogte);
		ellips.rotation = rotation;
		ellips.makeEllips();
		
		if (h.containsKey("rotation"))
		{	ellips.rotate(rotation);
		}
		else if (h.containsKey("m00"))
		{	ellips.transformBy(m00, m01, m10, m11);
		}
		
		return ellips;
	}

	public void transformBy(double m00, double m01, double m10, double m11)
	{
		this.m00 = m00;
		this.m01 = m01;
		this.m10 = m10;
		this.m11 = m11;
		
		ellips.transformBy(m00, m01, m10, m11, cx, cy);
		outerEllips.transformBy(m00, m01, m10, m11, cx, cy);
		innerEllips.transformBy(m00, m01, m10, m11, cx, cy);
		
		makeHandleBox();
		
	}

	public void teken(Graphics2D g)
	{
		
		g.setColor(kleur);
		ellips.draw(g, kleur, null);
		
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
		}
		if (topLeftHandle != null)
		{	g.setColor(KladjeVeld.hbColor);
			g.drawPolygon(topLeftHandle);
		}
		if (bottomRightHandle != null)
		{	g.setColor(KladjeVeld.hbColor);
			g.drawPolygon(bottomRightHandle);
		}
		if (bottomLeftHandle != null)
		{	g.setColor(KladjeVeld.hbColor);
			g.drawPolygon(bottomLeftHandle);
		}
		if (rotateEastHandle != null)
		{	g.setColor(KladjeVeld.hbColor);
			g.drawOval(handleBox.x + handleBox.width, // - 2 * hbFactor,
					   handleBox.y + handleBox.height/2 - 2 * hbFactor, 
					   4 * hbFactor, 4 * hbFactor);
		}
		if (rotateWestHandle != null)
		{	g.setColor(KladjeVeld.hbColor);
			g.drawOval(handleBox.x - 4 * hbFactor,
					   handleBox.y + handleBox.height/2 - 2 * hbFactor, 
					   4 * hbFactor, 4 * hbFactor);
		}
		

	}
	
	public void tekenBB(Graphics2D g)
	{
		g.setStroke(new BasicStroke(0.8f));
		
		outerEllips.draw(g, KladjeVeld.bbColor, null);
		innerEllips.draw(g, KladjeVeld.bbColor, null);
		
		g.setStroke(new BasicStroke(1.5f, 2, 0, 10.0f, null, 0.0f));

	}

	public double inverseRotX(double x, double y)
	{
		double rotX = Math.cos(-rotation) * x - Math.sin(- rotation) * y;
		
		return rotX;
		
	}
	
	public double inverseRotY(double x, double y)
	{
		double rotY = Math.sin(-rotation) * x + Math.cos(- rotation) * y;
		
		return rotY;
	}
	
	
	public boolean bbContains(int x, int y)
	{
		
		return outerEllips.contains(x, y) && !innerEllips.contains(x, y);
	}

	public void translate(int dx, int dy)
	{
		topLeftX += dx;
		topLeftY += dy;
		
		ellips.translate(dx, dy);
		outerEllips.translate(dx, dy);
		innerEllips.translate(dx, dy);
		cx += dx;
		cy += dy;
		
		makeHandleBox();

		
	}	

	public boolean isContainedIn(Rectangle r)
	{
		if (r == null)
			return false;
		
		boolean isContainedIn = true;
		for (int cnt = 0; cnt < outerEllips.aantalPunten; cnt++)
		{
			isContainedIn = isContainedIn && 
							r.contains(outerEllips.puntenX[cnt], outerEllips.puntenY[cnt]);
		}
		
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
	KladjePolygon bb2;
	double cx, cy;
	
	double rotation = 0;
	double scaleX = 1;
	double scaleY = 1;
	
	double m00 = 1;
	double m01 = 0;
	double m10 = 0;
	double m11 = 1;
	
	int tekstX, tekstY;
	Rectangle handleBox;
	int hbFactor = 4;
	
	Polygon topRightHandle, bottomRightHandle, topLeftHandle, bottomLeftHandle;
	Rectangle topRightRect, bottomRightRect, topLeftRect, bottomLeftRect;
	Rectangle rotateEastHandle, rotateWestHandle;
	
	
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
		for (int pCnt = 0; pCnt < bb2.aantalPunten; pCnt++)
		{
			if (bb2.puntenX[pCnt] < minX)
				minX = bb2.puntenX[pCnt];
			if (bb2.puntenX[pCnt] > maxX)
				maxX = bb2.puntenX[pCnt];
			if (bb2.puntenY[pCnt] < minY)
				minY = bb2.puntenY[pCnt];
			if (bb2.puntenY[pCnt] > maxY)
				maxY = bb2.puntenY[pCnt];
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
		
/*		
		topRightHandle = new Polygon();
		topRightHandle.addPoint(handleBox.x + handleBox.width + hbFactor,
								handleBox.y - hbFactor);
		topRightHandle.addPoint(handleBox.x + handleBox.width - 3 * hbFactor,
								handleBox.y - hbFactor);
		topRightHandle.addPoint(handleBox.x + handleBox.width + hbFactor,
								handleBox.y + 3 * hbFactor);
		topRightRect = new Rectangle(handleBox.x + handleBox.width - 3 * hbFactor,
									 handleBox.y - hbFactor, 4 * hbFactor, 4 * hbFactor);
*/
/*		
		topLeftHandle = new Polygon();
		topLeftHandle.addPoint(handleBox.x - hbFactor, handleBox.y - hbFactor);
		topLeftHandle.addPoint(handleBox.x + 3 * hbFactor, handleBox.y - hbFactor);
		topLeftHandle.addPoint(handleBox.x - hbFactor, handleBox.y + 3 * hbFactor);
		topLeftRect = new Rectangle(handleBox.x - hbFactor, handleBox.y - hbFactor, 4 * hbFactor, 4 * hbFactor);
*/		
		bottomRightHandle = new Polygon();
		bottomRightHandle.addPoint(handleBox.x + handleBox.width + hbFactor,
								   handleBox.y + handleBox.height + hbFactor);
		bottomRightHandle.addPoint(handleBox.x + handleBox.width - 3 * hbFactor,
								   handleBox.y + handleBox.height + hbFactor);
		bottomRightHandle.addPoint(handleBox.x + handleBox.width + hbFactor,
								   handleBox.y + handleBox.height - 3 * hbFactor);
		bottomRightRect = new Rectangle(handleBox.x + handleBox.width - 3 * hbFactor,
				   						handleBox.y + handleBox.height - 3 * hbFactor, 4 * hbFactor, 4 * hbFactor);		
/*		
		bottomLeftHandle = new Polygon();
		bottomLeftHandle.addPoint(handleBox.x - hbFactor, handleBox.y + handleBox.height + hbFactor);
		bottomLeftHandle.addPoint(handleBox.x + 3 * hbFactor, handleBox.y + handleBox.height + hbFactor);
		bottomLeftHandle.addPoint(handleBox.x - hbFactor, handleBox.y + handleBox.height - 3 * hbFactor);
		bottomLeftRect = new Rectangle(handleBox.x - hbFactor, handleBox.y + handleBox.height - 3 * hbFactor, 
									   4 * hbFactor, 4 * hbFactor);		
*/		
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
		rotateEastHandle = new Rectangle(handleBox.x + handleBox.width, // - 2 * hbFactor,
										 handleBox.y + handleBox.height/2 - 2 * hbFactor,
										 4 * hbFactor, 4 * hbFactor);
/*
		rotateWestHandle = new Rectangle(handleBox.x - 4 * hbFactor,
				                         handleBox.y + handleBox.height/2 - 2 * hbFactor,
										 4 * hbFactor, 4 * hbFactor);
*/										 
	}

	public void killRotateHandles()
	{
		rotateEastHandle = null; 
		rotateWestHandle = null;
		
	}

	
	public void makeBB()
	{
		bb = new Rectangle(xPos - bbFactor, yPos - bbFactor, 
						   breedte + 2 * bbFactor, hoogte + 2 * bbFactor);

		bb2 = new KladjePolygon();
		bb2.addPoint(xPos - bbFactor, yPos - bbFactor);
		bb2.addPoint(xPos - bbFactor + breedte + 2 * bbFactor, yPos - bbFactor);
		bb2.addPoint(xPos - bbFactor + breedte + 2 * bbFactor, yPos - bbFactor + hoogte + 2 * bbFactor);
		bb2.addPoint(xPos - bbFactor, yPos - bbFactor + hoogte + 2 * bbFactor);

	}

	public void rotate(double rotateStep)
	{	rotation += rotateStep;
		
		
		double m00New  = Math.cos(rotateStep) * m00 - Math.sin(rotateStep) * m10;
		double m10New  = Math.sin(rotateStep) * m00 + Math.cos(rotateStep) * m10;
		double m01New = Math.cos(rotateStep) * m01 - Math.sin(rotateStep) * m11;
		double m11New = Math.sin(rotateStep) * m01 + Math.cos(rotateStep) * m11;

		m00 = m00New;
		m01 = m01New;
		m10 = m10New;
		m11 = m11New;
	
	
	//makeBB();
		//bb2.rotate(rotateStep, cx, cy);
		//makeHandleBox();
		
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
		
		//tekstX = (int) Math.round(scaleStep * tekstX + (1 - scaleStep) * cx);
		//tekstY = (int) Math.round(scaleStep * tekstY + (1 - scaleStep) * cy);
		
		scaleX *= scaleStep;
		scaleY *= scaleStep;

		m00 *= scaleStep;
		m01 *= scaleStep;
		m10 *= scaleStep;
		m11 *= scaleStep;
		
		
		makeBB();
		//bb2.rotate(rotation, cx, cy);
		//makeHandleBox();
	
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
		
		//tekstX = (int) Math.round(sx * tekstX + (1 - sx) * cx);
		//tekstY = (int) Math.round(sy * tekstY + (1 - sy) * cy);
		
		
		scaleX *= sx;
		scaleY *= sy;
	
		m00 *= sx;
		m01 *= sx;
		m10 *= sy;
		m11 *= sy;
		
		
		//makeBB();
		//bb2.rotate(rotation, cx, cy);
		//makeHandleBox();
	
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
	
		h.put("m00", new Double(m00));
		h.put("m10", new Double(m10));
		h.put("m01", new Double(m01));
		h.put("m11", new Double(m11));
		
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

		double m00 = 1;
		double m01 = 0;
		double m10 = 0;
		double m11 = 1;
		
		
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

		if (h.containsKey("m00"))
			m00 = ((Double) h.get("m00")).doubleValue();
		if (h.containsKey("m10"))
			m10 = ((Double) h.get("m10")).doubleValue();
		if (h.containsKey("m01"))
			m01 = ((Double) h.get("m01")).doubleValue();
		if (h.containsKey("m11"))
			m11 = ((Double) h.get("m11")).doubleValue();
		
		
		TekstElement tekstElement = new TekstElement(kleur, tekst, xPos, yPos);
		tekstElement.rotation = rotation;
		
		if (h.containsKey("rotation"))
		{	tekstElement.scale(scaleX, scaleY);
		}
		else
		{
			tekstElement.transformBy(m00, m01, m10, m11);
		}
		
		
		return tekstElement;
	}
	
	public void transformBy(double m00, double m01, double m10, double m11)
	{
		this.m00 = m00;
		this.m01 = m01;
		this.m10 = m10;
		this.m11 = m11;
	}
	
	public void teken(Graphics2D g)
	{
		makeBB();
		bb2.rotate(rotation, cx, cy);
		makeHandleBox();
		
		AffineTransform oldAT = g.getTransform();
		
		AffineTransform at = g.getTransform();
		// hier!!
		at.rotate(rotation, cx, cy);
		at.scale(scaleX, scaleY);
		
//		AffineTransform at = new AffineTransform(m00,m10,m01,m11,0,0);
		
		g.setTransform(at);
		
		g.setFont(KladjeVeld.tekstFont);
		g.setColor(kleur);
		g.drawString(tekst, tekstX, tekstY + ascent);
		//g.drawString(tekst, xPos, yPos + ascent);
		
		g.setTransform(oldAT);
		
		tekenBB(g);
		//tekenHandleBox(g);
		
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
			g.drawOval(handleBox.x + handleBox.width, // - 2 * hbFactor,
					   handleBox.y + handleBox.height/2 - 2 * hbFactor, 
					   4 * hbFactor, 4 * hbFactor);
//g.setColor(Color.red);			
//g.drawRect(rotateEastHandle.x, rotateEastHandle.y, rotateEastHandle.width, rotateEastHandle.height);
		}
		if (rotateWestHandle != null)
		{	g.setColor(KladjeVeld.hbColor);
			g.drawOval(handleBox.x - 4 * hbFactor, 
					   handleBox.y + handleBox.height/2 - 2 * hbFactor, 
					   4 * hbFactor, 4 * hbFactor);
//g.setColor(Color.red);			
//g.drawRect(rotateWestHandle.x, rotateWestHandle.y, rotateWestHandle.width, rotateWestHandle.height);
		}
		

	}
	

	public void tekenBB(Graphics2D g)
	{

		
		AffineTransform oldAT = g.getTransform();
		AffineTransform at = g.getTransform();

		at.rotate(rotation, cx, cy);
		
		g.setTransform(at);
		
		g.setStroke(new BasicStroke(0.8f));
		g.setColor(KladjeVeld.bbColor);

		g.drawRect(bb.x, bb.y, bb.width, bb.height);
		//bb.draw(g, KladjeVeld.bbColor, null);
				
//g.setColor(Color.red);		
//g.drawRect(tekstBox.x, tekstBox.y, tekstBox.width, tekstBox.height);
		
		g.setStroke(new BasicStroke(1.5f, 2, 0, 10.0f, null, 0.0f));
		
		g.setTransform(oldAT);

	}

	public double inverseRotX(double x, double y)
	{
		double rotX = Math.cos(-rotation) * x - Math.sin(- rotation) * y;
		
		return rotX;
		
	}

	public double inverseRotY(double x, double y)
	{
		double rotY = Math.sin(-rotation) * x + Math.cos(- rotation) * y;
		
		return rotY;
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
		//int rx = inverseTransformX(x, y);
		//int ry = inverseTransformY(x, y);
		
		return bb2.contains(x, y);
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
	

}