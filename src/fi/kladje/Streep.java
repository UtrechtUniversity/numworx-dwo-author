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
	AffineTransform at = new AffineTransform();

	// backward compatibility
	double rotation = 0;
	Rectangle handleBox;
	int hbFactor = 4;
	int breedte, hoogte;
	
	Polygon topRightHandle, bottomRightHandle, topLeftHandle, bottomLeftHandle;
	Rectangle topRightRect, bottomRightRect, topLeftRect, bottomLeftRect;
	Rectangle rotateEastHandle, rotateWestHandle;
	
	public Streep(Color c, ArrayList<DoublePoint> punten)
	{	kleur = c;
		puntenXD = new double[punten.size()];
		puntenYD = new double[punten.size()];

		for (int pCnt = 0; pCnt < punten.size(); pCnt++)
		{	DoublePoint pt = punten.get(pCnt);
			puntenXD[pCnt] = pt.x;
			puntenYD[pCnt] = pt.y;
			
			cx += puntenXD[pCnt];
			cy += puntenYD[pCnt];
		}
		
		cx /= puntenXD.length;
		cy /= puntenYD.length;

		maakStreep();
	}
	
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
		
		AffineTransform rot = new AffineTransform(Math.cos(rotateStep),- Math.sin(rotateStep),
												  Math.sin(rotateStep), Math.cos(rotateStep), 
												  cx - Math.cos(rotateStep) * cx + Math.sin(rotateStep) * cy, 
												  cy - Math.sin(rotateStep) * cx - Math.cos(rotateStep) * cy);
		at = at.leftMultiplyBy(rot);
		
		bb.rotate(rotateStep, cx, cy);
	
		makeHandleBox();
		
		
	}

	
	public void rotate(double rotateStep, double dx, double dy)
	{	
	
		for (int pCnt = 0; pCnt < puntenXD.length; pCnt++)
		{	
			double pXDNew = Math.cos(rotateStep) * (pXD[pCnt] - dx) - Math.sin(rotateStep) * (pYD[pCnt] - dy);
			double pYDNew = Math.sin(rotateStep) * (pXD[pCnt] - dx) + Math.cos(rotateStep) * (pYD[pCnt] - dy);
			pXD[pCnt] = pXDNew + dx;
			pYD[pCnt] = pYDNew + dy;
		}

		
		double cxNew = Math.cos(rotateStep) * (cx - dx) - Math.sin(rotateStep) * (cy - dy);
		double cyNew = Math.sin(rotateStep) * (cx - dx) + Math.cos(rotateStep) * (cy - dy);
		cx = cxNew + dx;
		cy = cyNew + dy;
		
		AffineTransform rot = new AffineTransform(Math.cos(rotateStep),- Math.sin(rotateStep),
												  Math.sin(rotateStep), Math.cos(rotateStep), 
												  dx - Math.cos(rotateStep) * dx + Math.sin(rotateStep) * dy, 
												  dy - Math.sin(rotateStep) * dx - Math.cos(rotateStep) * dy);
		at = at.leftMultiplyBy(rot);
		
		bb.rotate(rotateStep, dx, dy);
	
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
		

		AffineTransform rot = new AffineTransform(Math.cos(rotation),- Math.sin(rotation),
												  Math.sin(rotation), Math.cos(rotation), 
		  										  cx - Math.cos(rotation) * cx + Math.sin(rotation) * cy, 
		  										  cy - Math.sin(rotation) * cx - Math.cos(rotation) * cy);

		at = at.leftMultiplyBy(rot);
		
		
	}
	
	public void scale(double scaleStep)
	{
		for (int pCnt = 0; pCnt < puntenXD.length; pCnt++)
		{	
			pXD[pCnt] = scaleStep * pXD[pCnt] + (1 - scaleStep) * cx;
			pYD[pCnt] = scaleStep * pYD[pCnt] + (1 - scaleStep) * cy;
		}
		
		AffineTransform sc = new AffineTransform(scaleStep, 0, 0, scaleStep, (1 - scaleStep) * cx, (1 - scaleStep) * cy);
		at = at.leftMultiplyBy(sc);
		
		bb.scale(scaleStep, cx, cy);

		makeHandleBox();
		
	}
	
	public void scale(double scaleStep, double dx, double dy)
	{
		for (int pCnt = 0; pCnt < puntenXD.length; pCnt++)
		{	
			pXD[pCnt] = scaleStep * pXD[pCnt] + (1 - scaleStep) * dx;
			pYD[pCnt] = scaleStep * pYD[pCnt] + (1 - scaleStep) * dy;
		}
		
		cx = scaleStep * cx + (1 - scaleStep) * dx;
		cy = scaleStep * cy + (1 - scaleStep) * dy;
		
		AffineTransform sc = new AffineTransform(scaleStep, 0, 0, scaleStep, (1 - scaleStep) * dx, (1 - scaleStep) * dy);
		at = at.leftMultiplyBy(sc);
		
		bb.scale(scaleStep, dx, dy);

		makeHandleBox();
		
	}
	

	public void scale(double scaleStepX, double scaleStepY)
	{
		for (int pCnt = 0; pCnt < puntenXD.length; pCnt++)
		{	
			pXD[pCnt] = scaleStepX * pXD[pCnt] + (1 - scaleStepX) * cx;
			pYD[pCnt] = scaleStepY * pYD[pCnt] + (1 - scaleStepY) * cy;
		}
		
		
		AffineTransform sc = new AffineTransform(scaleStepX, 0, 0, scaleStepY, (1 - scaleStepX) * cx, (1 - scaleStepY) * cy);
		at = at.leftMultiplyBy(sc);
		
		
		bb.scale(scaleStepX, scaleStepY, cx, cy);
		
		
		makeHandleBox();
		
	}

	public void scale(double scaleStepX, double scaleStepY, double dx, double dy)
	{
		for (int pCnt = 0; pCnt < puntenXD.length; pCnt++)
		{	
			pXD[pCnt] = scaleStepX * pXD[pCnt] + (1 - scaleStepX) * dx;
			pYD[pCnt] = scaleStepY * pYD[pCnt] + (1 - scaleStepY) * dy;
		}
		
		cx = scaleStepX * cx + (1 - scaleStepX) * dx;
		cy = scaleStepY * cy + (1 - scaleStepY) * dy;
		
		AffineTransform sc = new AffineTransform(scaleStepX, 0, 0, scaleStepY, (1 - scaleStepX) * dx, (1 - scaleStepY) * dy);
		at = at.leftMultiplyBy(sc);
		
		
		bb.scale(scaleStepX, scaleStepY, dx, dy);
		
		
		makeHandleBox();
		
	}
	
	public Hashtable getState()
	{	Hashtable h = new Hashtable();
		
		h.put("kleur", kleur);
		h.put("kleurgwt", new String("rgb(" + kleur.getRed()+ "," + kleur.getGreen() + "," + kleur.getBlue() + ")"));
		
		h.put("puntenXD", puntenXD);
		h.put("puntenYD", puntenYD);
		
//		h.put("rotation", new Double(rotation));
		
		h.put("m00", new Double(at.m00));
		h.put("m10", new Double(at.m10));
		h.put("m01", new Double(at.m01));
		h.put("m11", new Double(at.m11));
		h.put("b0", new Double(at.b0));
		h.put("b1", new Double(at.b1));
		
	
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
		double b0 = 0;
		double b1 = 0;
		
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
		if (h.containsKey("b0"))
			b0 = ((Double) h.get("b0")).doubleValue();
		if (h.containsKey("b1"))
			b1 = ((Double) h.get("b1")).doubleValue();
		
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
		else if (h.containsKey("b0"))
		{	streep.transformBy(m00, m01, m10, m11, b0, b1);
		}
		else
		{	streep.transformBy(m00, m01, m10, m11);
		}
		
		return streep;
	}
	
	public void transformBy(double m00, double m01, double m10, double m11)
	{
		
		at = new AffineTransform(m00, m01, m10, m11, - m00*cx - m01*cy + cx, - m10*cx - m11*cy + cy);
		
		for (int pCnt = 0; pCnt < puntenXD.length; pCnt++)
		{	
			double pXDNew = at.m00 * pXD[pCnt] + at.m01 * pYD[pCnt] + at.b0;
			double pYDNew = at.m10 * pXD[pCnt] + at.m11 * pYD[pCnt] + at.b1;
			pXD[pCnt] = pXDNew;
			pYD[pCnt] = pYDNew;
			
		}
		
		//double cxNew = at.m00 * cx + at.m01 * cy + at.b0;
		//double cyNew = at.m10 * cx + at.m11 * cy + at.b1;
		//cx = cxNew;
		//cy = cyNew;
		
		bb.transformBy(at);
		
		makeHandleBox();
	}
	
	public void transformBy(double m00, double m01, double m10, double m11, double b0, double b1)
	{
		
		at = new AffineTransform(m00, m01, m10, m11, b0, b1);
		
		for (int pCnt = 0; pCnt < puntenXD.length; pCnt++)
		{	
			double pXDNew = at.m00 * pXD[pCnt] + at.m01 * pYD[pCnt] + at.b0;
			double pYDNew = at.m10 * pXD[pCnt] + at.m11 * pYD[pCnt] + at.b1;
			pXD[pCnt] = pXDNew;
			pYD[pCnt] = pYDNew;
			
		}
		
		double cxNew = at.m00 * cx + at.m01 * cy + at.b0;
		double cyNew = at.m10 * cx + at.m11 * cy + at.b1;
		cx = cxNew;
		cy = cyNew;
		
		bb.transformBy(at);
		
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
		
		//tekenBB(g);
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
		g.setStroke(new BasicStroke(0.8f));

		bb.draw(g, KladjeVeld.bbColor, null);
				
		g.setStroke(new BasicStroke(1.5f, 2, 0, 10.0f, null, 0.0f));
		
	}

/*	
	public double inverseRotX(double x, double y)
	{
		double rotX = Math.cos(-rotation) * x - Math.sin(- rotation) * y;
		
		return rotX;
		
	}
*/
/*	
	public double inverseRotY(double x, double y)
	{
		double rotY = Math.sin(-rotation) * x + Math.cos(- rotation) * y;
		
		return rotY;
	}
*/	
	public boolean bbContains(int x, int y)
	{

		return bb.contains(x, y);
	}
	
	public void translate(int dx, int dy)
	{
		for (int pCnt = 0; pCnt < puntenXD.length; pCnt++)
		{	//puntenXD[pCnt] += dx;
			//puntenYD[pCnt] += dy;
			pXD[pCnt] += dx;
			pYD[pCnt] += dy; 
		}

		AffineTransform trans = new AffineTransform(1,0,0,1,dx,dy);
		at = at.leftMultiplyBy(trans);
		
		cx += dx;
		cy += dy;

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


class Lijn
{
	Color kleur;
	int fromX, fromY, toX, toY;
	//int fX, fY, tX, tY;
	double fX, fY, tX, tY;
	int bbFactor = 4;
	KladjePolygon bb; 
	double cx, cy;
	AffineTransform at = new AffineTransform();

	// backwards compatibility
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
		fX = fXNew + cx;
		fY = fYNew + cy;
		tX = tXNew + cx;
		tY = tYNew + cy;
		
		AffineTransform rot = new AffineTransform(Math.cos(rotateStep),- Math.sin(rotateStep),
				  Math.sin(rotateStep), Math.cos(rotateStep), 
				  cx - Math.cos(rotateStep) * cx + Math.sin(rotateStep) * cy, 
				  cy - Math.sin(rotateStep) * cx - Math.cos(rotateStep) * cy);
		at = at.leftMultiplyBy(rot);

	
		bb.rotate(rotateStep, cx, cy);
		
		makeHandleBox();
	
	}

	public void rotate(double rotateStep, double dx, double dy)
	{	//rotation += rotateStep;
	
		double fXNew = Math.cos(rotateStep) * (fX - dx) - Math.sin(rotateStep) * (fY - dy);
		double fYNew = Math.sin(rotateStep) * (fX - dx) + Math.cos(rotateStep) * (fY - dy);
		double tXNew = Math.cos(rotateStep) * (tX - dx) - Math.sin(rotateStep) * (tY - dy);
		double tYNew = Math.sin(rotateStep) * (tX - dx) + Math.cos(rotateStep) * (tY - dy);
		fX = fXNew + dx;
		fY = fYNew + dy;
		tX = tXNew + dx;
		tY = tYNew + dy;
		
		double cxNew = Math.cos(rotateStep) * (cx - dx) - Math.sin(rotateStep) * (cy - dy);
		double cyNew = Math.sin(rotateStep) * (cx - dx) + Math.cos(rotateStep) * (cy - dy);
		cx = cxNew + dx;
		cy = cyNew + dy;
		
		AffineTransform rot = new AffineTransform(Math.cos(rotateStep),- Math.sin(rotateStep),
				  Math.sin(rotateStep), Math.cos(rotateStep), 
				  dx - Math.cos(rotateStep) * cx + Math.sin(rotateStep) * dy, 
				  dy - Math.sin(rotateStep) * cx - Math.cos(rotateStep) * dy);
		at = at.leftMultiplyBy(rot);

		bb.rotate(rotateStep, dx, dy);
		
		makeHandleBox();
	
	}
	
	public void rotateLijn(double rotation)
	{		
		double fXNew = Math.cos(rotation) * (fX - cx) - Math.sin(rotation) * (fY - cy);
		double fYNew = Math.sin(rotation) * (fX - cx) + Math.cos(rotation) * (fY - cy);
		double tXNew = Math.cos(rotation) * (tX - cx) - Math.sin(rotation) * (tY - cy);
		double tYNew = Math.sin(rotation) * (tX - cx) + Math.cos(rotation) * (tY - cy);
		fX = fXNew + cx;
		fY = fYNew + cy;
		tX = tXNew + cx;
		tY = tYNew + cy;

		AffineTransform rot = new AffineTransform(Math.cos(rotation),- Math.sin(rotation),
				  Math.sin(rotation), Math.cos(rotation), 
				  cx - Math.cos(rotation) * cx + Math.sin(rotation) * cy, 
				  cy - Math.sin(rotation) * cx - Math.cos(rotation) * cy);

		at = at.leftMultiplyBy(rot);
	}	
	
	public void scale(double scaleStep)
	{	
		fX = scaleStep * fX + (1 - scaleStep) * cx;
		fY = scaleStep * fY + (1 - scaleStep) * cy;
		tX = scaleStep * tX + (1 - scaleStep) * cx;
		tY = scaleStep * tY + (1 - scaleStep) * cy;
		
		AffineTransform sc = new AffineTransform(scaleStep, 0, 0, scaleStep, (1 - scaleStep) * cx, (1 - scaleStep) * cy);
		at = at.leftMultiplyBy(sc);

		bb.scale(scaleStep, scaleStep, cx, cy);

		makeHandleBox();
		
	}

	public void scale(double scaleStep, double dx, double dy)
	{	
		fX = scaleStep * fX + (1 - scaleStep) * dx;
		fY = scaleStep * fY + (1 - scaleStep) * dy;
		tX = scaleStep * tX + (1 - scaleStep) * dx;
		tY = scaleStep * tY + (1 - scaleStep) * dy;
		
		cx = scaleStep * cx + (1 - scaleStep) * dx;
		cy = scaleStep * cy + (1 - scaleStep) * dy;
		
		AffineTransform sc = new AffineTransform(scaleStep, 0, 0, scaleStep, (1 - scaleStep) * dx, (1 - scaleStep) * dy);
		at = at.leftMultiplyBy(sc);
		
		bb.scale(scaleStep, scaleStep, dx, dy);

		
		makeHandleBox();
		
	}
	
	public void scale(double sx, double sy)
	{	
		fX = sx * fX + (1 - sx) * cx;
		fY = sy * fY + (1 - sy) * cy;
		tX = sx * tX + (1 - sx) * cx;
		tY = sy * tY + (1 - sy) * cy;
		
		AffineTransform sc = new AffineTransform(sx, 0, 0, sy, (1 - sx) * cx, (1 - sy) * cy);
		at = at.leftMultiplyBy(sc);

		bb.scale(sx, sy, cx, cy);
		
		makeHandleBox();
		
	}

	public void scale(double sx, double sy, double dx, double dy)
	{	
		fX = sx * fX + (1 - sx) * dx;
		fY = sy * fY + (1 - sy) * dy;
		tX = sx * tX + (1 - sx) * dx;
		tY = sy * tY + (1 - sy) * dy;
		
		cx = sx * cx + (1 - sx) * dx;
		cy = sy * cy + (1 - sy) * dy;
		
		AffineTransform sc = new AffineTransform(sx, 0, 0, sy, (1 - sx) * dx, (1 - sy) * dy);
		at = at.leftMultiplyBy(sc);

		bb.scale(sx, sy, dx, dy);
		
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
		
		h.put("m00", new Double(at.m00));
		h.put("m10", new Double(at.m10));
		h.put("m01", new Double(at.m01));
		h.put("m11", new Double(at.m11));
		h.put("b0", new Double(at.b0));
		h.put("b1", new Double(at.b1));

	
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
		double b0 = 0;
		double b1 = 0;

		
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
		if (h.containsKey("b0"))
			b0 = ((Double) h.get("b0")).doubleValue();
		if (h.containsKey("b1"))
			b1 = ((Double) h.get("b1")).doubleValue();

		
		Lijn lijn = new Lijn(kleur, fromX, fromY, toX, toY);
		lijn.rotation = rotation;
		lijn.maakLijn();

		if (h.containsKey("rotation"))
		{	lijn.rotate(rotation);
		}
		else if (h.containsKey("b0"))
		{	lijn.transformBy(m00, m01, m10, m11, b0, b1);
		}
		else
		{	lijn.transformBy(m00, m01, m10, m11);
		}
		
		return lijn;
	}

	public void transformBy(double m00, double m01, double m10, double m11)
	{
		
		at = new AffineTransform(m00, m01, m10, m11, - m00*cx - m01*cy + cx, - m10*cx - m11*cy + cy);

		double fXNew = at.m00 * fX + at.m01 * fY + at.b0;
		double fYNew = at.m10 * fX + at.m11 * fY + at.b1;
		fX = fXNew;
		fY = fYNew;
		
		double tXNew = at.m00 * tX + at.m01 * tY + at.b0;
		double tYNew = at.m10 * tX + at.m11 * tY + at.b1;
		tX = tXNew;
		tY = tYNew;

		//double cxNew = at.m00 * cx + at.m01 * cy + at.b0;
		//double cyNew = at.m10 * cx + at.m11 * cy + at.b1;
		//cx = cxNew;
		//cy = cyNew;

		bb.transformBy(at);
		
		makeHandleBox();
		
		
	}
	
	public void transformBy(double m00, double m01, double m10, double m11, double b0, double b1)
	{
		
		at = new AffineTransform(m00, m01, m10, m11, b0, b1);

		double fXNew = at.m00 * fX + at.m01 * fY + at.b0;
		double fYNew = at.m10 * fX + at.m11 * fY + at.b1;
		fX = fXNew;
		fY = fYNew;
		
		double tXNew = at.m00 * tX + at.m01 * tY + at.b0;
		double tYNew = at.m10 * tX + at.m11 * tY + at.b1;
		tX = tXNew;
		tY = tYNew;

		double cxNew = at.m00 * cx + at.m01 * cy + at.b0;
		double cyNew = at.m10 * cx + at.m11 * cy + at.b1;
		cx = cxNew;
		cy = cyNew;

		bb.transformBy(at);
		
		makeHandleBox();
		
		
	}
		
	
	public void teken(Graphics2D g)
	{
		
		g.setColor(kleur);
		g.drawLine((int) Math.round(fX), (int) Math.round(fY), (int) Math.round(tX), (int) Math.round(tY));
	
		//tekenBB(g);
		
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

/*	
	public double inverseRotX(double x, double y)
	{
		double rotX = Math.cos(-rotation) * x - Math.sin(- rotation) * y;
		
		return rotX;
		
	}
*/
/*	
	public double inverseRotY(double x, double y)
	{
		double rotY = Math.sin(-rotation) * x + Math.cos(- rotation) * y;
		
		return rotY;
	}
*/	
	public boolean bbContains(int x, int y)
	{
		return bb.contains(x, y);
	}

	public void translate(int dx, int dy)
	{
		//fromX += dx;
		//fromY += dy;
		//toX += dx; 
		//toY += dy;
		cx += dx;
		cy += dy;

		fX += dx;
		fY += dy;
		tX += dx; 
		tY += dy;
		
		AffineTransform trans = new AffineTransform(1,0,0,1,dx,dy);
		at = at.leftMultiplyBy(trans);
		
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
	AffineTransform at = new AffineTransform();

	// backwards compatibility
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
		
		AffineTransform rot = new AffineTransform(Math.cos(rotateStep),- Math.sin(rotateStep),
				  Math.sin(rotateStep), Math.cos(rotateStep), 
				  cx - Math.cos(rotateStep) * cx + Math.sin(rotateStep) * cy, 
				  cy - Math.sin(rotateStep) * cx - Math.cos(rotateStep) * cy);
		at = at.leftMultiplyBy(rot);

		rechthoek.rotate(rotateStep, cx, cy);
		outerRechthoek.rotate(rotateStep, cx, cy);
		innerRechthoek.rotate(rotateStep, cx, cy);
		
		makeHandleBox();

	}
	public void rotate(double rotateStep, double dx, double dy)
	{	

		double cxNew = Math.cos(rotateStep) * (cx - dx) - Math.sin(rotateStep) * (cy - dy);
		double cyNew = Math.sin(rotateStep) * (cx - dx) + Math.cos(rotateStep) * (cy - dy);
		cx = cxNew + dx;
		cy = cyNew + dy;
		
		AffineTransform rot = new AffineTransform(Math.cos(rotateStep),- Math.sin(rotateStep),
				  Math.sin(rotateStep), Math.cos(rotateStep), 
				  dx - Math.cos(rotateStep) * dx + Math.sin(rotateStep) * dy, 
				  dy - Math.sin(rotateStep) * dx - Math.cos(rotateStep) * dy);
		at = at.leftMultiplyBy(rot);

		rechthoek.rotate(rotateStep, dx, dy);
		outerRechthoek.rotate(rotateStep, dx, dy);
		innerRechthoek.rotate(rotateStep, dx, dy);
		
		makeHandleBox();

	}
	
	public void scale(double scaleStep)
	{	

		AffineTransform sc = new AffineTransform(scaleStep, 0, 0, scaleStep, (1 - scaleStep) * cx, (1 - scaleStep) * cy);
		at = at.leftMultiplyBy(sc);

		rechthoek.scale(scaleStep, cx, cy);
		outerRechthoek.scale(scaleStep, cx, cy);
		innerRechthoek.scale(scaleStep, cx, cy);
		
		makeHandleBox();
		
	}

	public void scale(double scaleStep, double dx, double dy)
	{	
		cx = scaleStep * cx + (1 - scaleStep) * dx;
		cy = scaleStep * cy + (1 - scaleStep) * dy;
		
		AffineTransform sc = new AffineTransform(scaleStep, 0, 0, scaleStep, (1 - scaleStep) * dx, (1 - scaleStep) * dy);
		at = at.leftMultiplyBy(sc);

		rechthoek.scale(scaleStep, dx, dy);
		outerRechthoek.scale(scaleStep, dx, dy);
		innerRechthoek.scale(scaleStep, dx, dy);
		
		makeHandleBox();
		
	}
	
	public void scale(double sx, double sy)
	{

		AffineTransform sc = new AffineTransform(sx, 0, 0, sy, (1 - sx) * cx, (1 - sy) * cy);
		at = at.leftMultiplyBy(sc);

		rechthoek.scale(sx, sy, cx, cy);
		outerRechthoek.scale(sx, sy, cx, cy);
		innerRechthoek.scale(sx,sy, cx, cy);
		
		makeHandleBox();
		
	}

	public void scale(double sx, double sy, double dx, double dy)
	{
		cx = sx * cx + (1 - sx) * dx;
		cy = sy * cy + (1 - sy) * dy;

		AffineTransform sc = new AffineTransform(sx, 0, 0, sy, (1 - sx) * dx, (1 - sy) * dy);
		at = at.leftMultiplyBy(sc);

		rechthoek.scale(sx, sy, dx, dy);
		outerRechthoek.scale(sx, sy, dx, dy);
		innerRechthoek.scale(sx,sy, dx, dy);
		
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
		
		h.put("m00", new Double(at.m00));
		h.put("m10", new Double(at.m10));
		h.put("m01", new Double(at.m01));
		h.put("m11", new Double(at.m11));
		h.put("b0", new Double(at.b0));
		h.put("b1", new Double(at.b1));

//if (h.containsKey("b0"))
//{	double b0 = ((Double) h.get("b0")).doubleValue();
//System.out.println("h contains b0");
//System.out.println("b0 = " + b0);
//}
//if (h.containsKey("b1"))
//{	double b1 = ((Double) h.get("b1")).doubleValue();
//System.out.println("h contains b1");		
//System.out.println("b1 = " + b1);		
//}
		
//System.out.println("rh getstate");
//System.out.println("m00 = " + at.m00);
//System.out.println("m01 = " + at.m01);
//System.out.println("m10 = " + at.m10);
//System.out.println("m11 = " + at.m11);
//System.out.println("atb0 = " + at.b0);
//System.out.println("atb1 = " + at.b1);

		
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
		double b0 = 0;
		double b1 = 0;

		
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
		
		if (h.containsKey("b0"))
		{	b0 = ((Double) h.get("b0")).doubleValue();
//System.out.println("h contains b0");
//System.out.println("b0 = " + b0);
		}
		if (h.containsKey("b1"))
		{	b1 = ((Double) h.get("b1")).doubleValue();
//System.out.println("h contains b1");		
//System.out.println("b1 = " + b1);		
		}

//System.out.println("rh setstate");
//System.out.println("m00 = " + m00);
//System.out.println("m01 = " + m01);
//System.out.println("m10 = " + m10);
//System.out.println("m11 = " + m11);
//System.out.println("b0 = " + b0);
//System.out.println("b1 = " + b1);
		
		
		Rechthoek rechthoek = new Rechthoek(kleur, topLeftX, topLeftY, breedte, hoogte);
		rechthoek.rotation = rotation;
		rechthoek.maakRechthoek();

		
		if (h.containsKey("rotation"))
		{	rechthoek.rotate(rotation);
		}
		else if (h.containsKey("b0"))
		{	rechthoek.transformBy(m00, m01, m10, m11, b0, b1);
		}
		else
		{	rechthoek.transformBy(m00, m01, m10, m11);
		}
		
		return rechthoek;
	}

	public void transformBy(double m00, double m01, double m10, double m11)
	{
		
//System.out.println("rh tbo");
//System.out.println("m00 = " + m00);
//System.out.println("m01 = " + m01);
//System.out.println("m10 = " + m10);
//System.out.println("m11 = " + m11);
//System.out.println("cx = " + cx);
//System.out.println("cy = " + cy);


		at = new AffineTransform(m00, m01, m10, m11, - m00*cx - m01*cy + cx, - m10*cx - m11*cy + cy);
		
		rechthoek.transformBy(m00, m01, m10, m11, cx, cy);
		outerRechthoek.transformBy(m00, m01, m10, m11, cx, cy);
		innerRechthoek.transformBy(m00, m01, m10, m11, cx, cy);
		
		//double cxNew = at.m00 * cx + at.m01 * cy + at.b0;
		//double cyNew = at.m10 * cx + at.m11 * cy + at.b1;
		//cx = cxNew;
		//cy = cyNew;
		
		makeHandleBox();
		
	}
	
	public void transformBy(double m00, double m01, double m10, double m11, double b0, double b1)
	{
		
//System.out.println("rh tbn");
//System.out.println("m00 = " + m00);
//System.out.println("m01 = " + m01);
//System.out.println("m10 = " + m10);
//System.out.println("m11 = " + m11);
//System.out.println("b0 = " + b0);
//System.out.println("b1 = " + b1);

		at = new AffineTransform(m00, m01, m10, m11, b0, b1);
		
		rechthoek.transformBy(at);
		outerRechthoek.transformBy(at);
		innerRechthoek.transformBy(at);
		
		double cxNew = at.m00 * cx + at.m01 * cy + at.b0;
		double cyNew = at.m10 * cx + at.m11 * cy + at.b1;
		cx = cxNew;
		cy = cyNew;
		
		makeHandleBox();
		
	}
	
	public void teken(Graphics2D g)
	{
		g.setColor(kleur);
		rechthoek.draw(g, kleur, null);
		
		//tekenBB(g);

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
/*	
	public double inverseRotX(double x, double y)
	{
		double rotX = Math.cos(-rotation) * x - Math.sin(- rotation) * y;
		
		return rotX;
		
	}
*/
/*	
	public double inverseRotY(double x, double y)
	{
		double rotY = Math.sin(-rotation) * x + Math.cos(- rotation) * y;
		
		return rotY;
	}
*/	
	public boolean bbContains(int x, int y)
	{
		
		return outerRechthoek.contains(x, y) && !innerRechthoek.contains(x, y);
	}

	public void translate(int dx, int dy)
	{
		//topLeftX += dx;
		//topLeftY += dy;
		
		cx += dx;
		cy += dy;
		
		AffineTransform trans = new AffineTransform(1,0,0,1,dx,dy);
		at = at.leftMultiplyBy(trans);
		
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
	AffineTransform at = new AffineTransform();

	// backward compatibility
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

		AffineTransform rot = new AffineTransform(Math.cos(rotateStep),- Math.sin(rotateStep),
				  Math.sin(rotateStep), Math.cos(rotateStep), 
				  cx - Math.cos(rotateStep) * cx + Math.sin(rotateStep) * cy, 
				  cy - Math.sin(rotateStep) * cx - Math.cos(rotateStep) * cy);
		at = at.leftMultiplyBy(rot);

		ellips.rotate(rotateStep, cx, cy);
		outerEllips.rotate(rotateStep, cx, cy);
		innerEllips.rotate(rotateStep, cx, cy);

		makeHandleBox();

	}

	public void rotate(double rotateStep, double dx, double dy)
	{	//rotation += rotateStep;

		double cxNew = Math.cos(rotateStep) * (cx - dx) - Math.sin(rotateStep) * (cy - dy);
		double cyNew = Math.sin(rotateStep) * (cx - dx) + Math.cos(rotateStep) * (cy - dy);
		cx = cxNew + dx;
		cy = cyNew + dy;
		
		AffineTransform rot = new AffineTransform(Math.cos(rotateStep),- Math.sin(rotateStep),
				  Math.sin(rotateStep), Math.cos(rotateStep), 
				  dx - Math.cos(rotateStep) * dx + Math.sin(rotateStep) * dy, 
				  dy - Math.sin(rotateStep) * dx - Math.cos(rotateStep) * dy);
		at = at.leftMultiplyBy(rot);

		ellips.rotate(rotateStep, dx, dy);
		outerEllips.rotate(rotateStep, dx, dy);
		innerEllips.rotate(rotateStep, dx, dy);

		makeHandleBox();

	}
	
	public void scale(double scaleStep)
	{	
		AffineTransform sc = new AffineTransform(scaleStep, 0, 0, scaleStep, (1 - scaleStep) * cx, (1 - scaleStep) * cy);
		at = at.leftMultiplyBy(sc);

		ellips.scale(scaleStep, cx, cy);
		outerEllips.scale(scaleStep, cx, cy);
		innerEllips.scale(scaleStep, cx, cy);
		
		makeHandleBox();
		
	}

	public void scale(double scaleStep, double dx, double dy)
	{	
		cx = scaleStep * cx + (1 - scaleStep) * dx;
		cy = scaleStep * cy + (1 - scaleStep) * dy;
		
		AffineTransform sc = new AffineTransform(scaleStep, 0, 0, scaleStep, (1 - scaleStep) * dx, (1 - scaleStep) * dy);
		at = at.leftMultiplyBy(sc);

		ellips.scale(scaleStep, dx, dy);
		outerEllips.scale(scaleStep, dx, dy);
		innerEllips.scale(scaleStep, dx, dy);
		

		makeHandleBox();
		
	}
	
	public void scale(double sx, double sy)
	{	
		AffineTransform sc = new AffineTransform(sx, 0, 0, sy, (1 - sx) * cx, (1 - sy) * cy);
		at = at.leftMultiplyBy(sc);

		ellips.scale(sx, sy, cx, cy);
		outerEllips.scale(sx, sy, cx, cy);
		innerEllips.scale(sx, sy, cx, cy);
		
		
		makeHandleBox();
		
	}

	public void scale(double sx, double sy, double dx, double dy)
	{	
		cx = sx * cx + (1 - sx) * dx;
		cy = sy * cy + (1 - sy) * dy;

		AffineTransform sc = new AffineTransform(sx, 0, 0, sy, (1 - sx) * dx, (1 - sy) * dy);
		at = at.leftMultiplyBy(sc);

		ellips.scale(sx, sy, dx, dy);
		outerEllips.scale(sx, sy, dx, dy);
		innerEllips.scale(sx, sy, dx, dy);
		
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

		h.put("m00", new Double(at.m00));
		h.put("m10", new Double(at.m10));
		h.put("m01", new Double(at.m01));
		h.put("m11", new Double(at.m11));
		h.put("b0", new Double(at.b0));
		h.put("b1", new Double(at.b1));


		return h;
	}
	
	public static Ellips setState(Hashtable h)
	{
		
//System.out.println("el setstate");

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
		double b0 = 0;
		double b1 = 0;


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
		
		if (h.containsKey("b0"))
		{	b0 = ((Double) h.get("b0")).doubleValue();
//System.out.println("h contains b0");
//System.out.println("b0 = " + b0);
		}
		if (h.containsKey("b1"))
		{	b1 = ((Double) h.get("b1")).doubleValue();
//System.out.println("h contains b0");
//System.out.println("b1 = " + b1);
		}

		
		Ellips ellips = new Ellips(kleur, topLeftX, topLeftY, breedte, hoogte);
		ellips.rotation = rotation;
		ellips.makeEllips();
		
		if (h.containsKey("rotation"))
		{	ellips.rotate(rotation);
		}
		else if (h.containsKey("b0"))
		{	
			ellips.transformBy(m00, m01, m10, m11, b0, b1);
		}
		else
		{	ellips.transformBy(m00, m01, m10, m11);
		}
		
		return ellips;
	}

	public void transformBy(double m00, double m01, double m10, double m11)
	{
		at = new AffineTransform(m00, m01, m10, m11, - m00*cx - m01*cy + cx, - m10*cx - m11*cy + cy);
		
		ellips.transformBy(m00, m01, m10, m11, cx, cy);
		outerEllips.transformBy(m00, m01, m10, m11, cx, cy);
		innerEllips.transformBy(m00, m01, m10, m11, cx, cy);
		
		//double cxNew = at.m00 * cx + at.m01 * cy + at.b0;
		//double cyNew = at.m10 * cx + at.m11 * cy + at.b1;
		//cx = cxNew;
		//cy = cyNew;

		
		makeHandleBox();
		
	}

	public void transformBy(double m00, double m01, double m10, double m11, double b0, double b1)
	{
		at = new AffineTransform(m00, m01, m10, m11, b0, b1);
		
		ellips.transformBy(at);
		outerEllips.transformBy(at);
		innerEllips.transformBy(at);
		
		double cxNew = at.m00 * cx + at.m01 * cy + at.b0;
		double cyNew = at.m10 * cx + at.m11 * cy + at.b1;
		cx = cxNew;
		cy = cyNew;

		
		makeHandleBox();
		
	}

	public void teken(Graphics2D g)
	{
		
		g.setColor(kleur);
		ellips.draw(g, kleur, null);
		
		//tekenBB(g);
		
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

/*	
	public double inverseRotX(double x, double y)
	{
		double rotX = Math.cos(-rotation) * x - Math.sin(- rotation) * y;
		
		return rotX;
		
	}
*/
/*	
	public double inverseRotY(double x, double y)
	{
		double rotY = Math.sin(-rotation) * x + Math.cos(- rotation) * y;
		
		return rotY;
	}
*/	
	
	public boolean bbContains(int x, int y)
	{
		
		return outerEllips.contains(x, y) && !innerEllips.contains(x, y);
	}

	public void translate(int dx, int dy)
	{
		//topLeftX += dx;
		//topLeftY += dy;
		
		AffineTransform trans = new AffineTransform(1,0,0,1,dx,dy);
		at = at.leftMultiplyBy(trans);
		
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
	int bbFactor = 0;
	Rectangle bb;
	KladjePolygon bb2;
	double cx, cy;

	double rotation = 0;
	double scaleX = 1;
	double scaleY = 1;
	double oldScaleX, oldScaleY;
	
	//AffineTransform at = new AffineTransform();
	//AffineTransform atGWT = new AffineTransform();
	
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
		hoogte = KladjeVeld.tekstFM.getHeight() - 4;
		ascent = KladjeVeld.tekstFM.getAscent() - 2;
//System.out.println("hoogte = " + hoogte);		
				
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
		
		//makeBB();
		//bb2.transformBy(at);
		//setCenter();
		breedte = (int) Math.round(scaleX * breedte);
		//hoogte = (int) Math.round(scaleY * hoogte);
		
		makeBB();
		//bb2.scale(scaleX,scaleY,cx,cy);
		setCenter();
		bb2.rotate(rotation, cx, cy);
		makeHandleBox();
		
	}
	
	public void setCenter()
	{
		cx = (bb2.geefPuntX(0) + bb2.geefPuntX(2)) / 2;
		cy = (bb2.geefPuntY(0) + bb2.geefPuntY(2)) / 2;
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
		//topRightHandle = new Polygon();
		//topRightHandle.addPoint(handleBox.x + handleBox.width + hbFactor,
		//						handleBox.y - hbFactor);
		//topRightHandle.addPoint(handleBox.x + handleBox.width - 3 * hbFactor,
		//						handleBox.y - hbFactor);
		//topRightHandle.addPoint(handleBox.x + handleBox.width + hbFactor,
		//						handleBox.y + 3 * hbFactor);
		//topRightRect = new Rectangle(handleBox.x + handleBox.width - 3 * hbFactor,
		//							 handleBox.y - hbFactor, 4 * hbFactor, 4 * hbFactor);

		//topLeftHandle = new Polygon();
		//topLeftHandle.addPoint(handleBox.x - hbFactor, handleBox.y - hbFactor);
		//topLeftHandle.addPoint(handleBox.x + 3 * hbFactor, handleBox.y - hbFactor);
		//topLeftHandle.addPoint(handleBox.x - hbFactor, handleBox.y + 3 * hbFactor);
		//topLeftRect = new Rectangle(handleBox.x - hbFactor, handleBox.y - hbFactor, 4 * hbFactor, 4 * hbFactor);
		
		bottomRightHandle = new Polygon();
		bottomRightHandle.addPoint(handleBox.x + handleBox.width + hbFactor,
								   handleBox.y + handleBox.height + hbFactor);
		bottomRightHandle.addPoint(handleBox.x + handleBox.width - 3 * hbFactor,
								   handleBox.y + handleBox.height + hbFactor);
		bottomRightHandle.addPoint(handleBox.x + handleBox.width + hbFactor,
								   handleBox.y + handleBox.height - 3 * hbFactor);
		bottomRightRect = new Rectangle(handleBox.x + handleBox.width - 3 * hbFactor,
				   						handleBox.y + handleBox.height - 3 * hbFactor, 4 * hbFactor, 4 * hbFactor);		
		
		//bottomLeftHandle = new Polygon();
		//bottomLeftHandle.addPoint(handleBox.x - hbFactor, handleBox.y + handleBox.height + hbFactor);
		//bottomLeftHandle.addPoint(handleBox.x + 3 * hbFactor, handleBox.y + handleBox.height + hbFactor);
		//bottomLeftHandle.addPoint(handleBox.x - hbFactor, handleBox.y + handleBox.height - 3 * hbFactor);
		//bottomLeftRect = new Rectangle(handleBox.x - hbFactor, handleBox.y + handleBox.height - 3 * hbFactor, 
		//							   4 * hbFactor, 4 * hbFactor);		
		
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

		//rotateWestHandle = new Rectangle(handleBox.x - 4 * hbFactor,
		//		                         handleBox.y + handleBox.height/2 - 2 * hbFactor,
		//								 4 * hbFactor, 4 * hbFactor);
										 
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
		
		
		//AffineTransform rot = new AffineTransform(Math.cos(rotateStep),- Math.sin(rotateStep),
		//		  Math.sin(rotateStep), Math.cos(rotateStep), 
		//		  cx - Math.cos(rotateStep) * cx + Math.sin(rotateStep) * cy, 
		//		  cy - Math.sin(rotateStep) * cx - Math.cos(rotateStep) * cy);
		//at = at.leftMultiplyBy(rot);
		
		// nothing to do
		//atGWT = atGWT.leftMultiplyBy(rot);
		
		//makeBB();
		//bb2.rotate(rotateStep, cx, cy);
		//setCenter();
		//makeHandleBox();
		
	}

	public void rotate(double rotateStep, double dx, double dy)
	{	rotation += rotateStep;
		
		//AffineTransform rot = new AffineTransform(Math.cos(rotateStep),- Math.sin(rotateStep),
		//		  Math.sin(rotateStep), Math.cos(rotateStep), 
		//		  dx - Math.cos(rotateStep) * dx + Math.sin(rotateStep) * dy, 
		//		  dy - Math.sin(rotateStep) * dx - Math.cos(rotateStep) * dy);
		//at = at.leftMultiplyBy(rot);

		double cxCopy = cx;
		double cyCopy = cy;
		double rotatedCxCopy = Math.cos(rotateStep) * cxCopy - Math.sin(rotateStep) * cyCopy + 
			                   dx - Math.cos(rotateStep) * dx + Math.sin(rotateStep) * dy;
		double rotatedCyCopy = Math.sin(rotateStep) * cxCopy + Math.cos(rotateStep) * cyCopy + 
							   dy - Math.sin(rotateStep) * dx - Math.cos(rotateStep) * dy;
		int deltax = (int) Math.round(rotatedCxCopy - cxCopy);
		int deltay = (int) Math.round(rotatedCyCopy - cyCopy);
		
		translate(deltax, deltay);
		
		//AffineTransform trans = new AffineTransform (1,0,0,1,deltax,deltay);
		//atGWT = atGWT.leftMultiplyBy(trans);
		
		
		
		//makeBB();
		//bb2.rotate(rotateStep, dx, dy);
		//setCenter();
		//makeHandleBox();
		
	}
	
	
	public void scale(double scaleStep)
	{	
		
		scaleX *= scaleStep;
		scaleY *= scaleStep;
		
		breedte = (int) Math.round(scaleStep * breedte);
		hoogte = (int) Math.round(scaleStep * hoogte);

		tekstX = (int) Math.round((1/scaleStep) * tekstX);// + (1 - (1/scaleStep)) * cx);
		tekstY = (int) Math.round((1/scaleStep) * tekstY);// + (1 - (1/scaleStep)) * cy);
		

		//AffineTransform sc = new AffineTransform(scaleStep, 0, 0, scaleStep, (1 - scaleStep) * cx, (1 - scaleStep) * cy);
		//at = at.leftMultiplyBy(sc);

		//atGWT = atGWT.leftMultiplyBy(sc);
		
		//makeBB();
		//bb2.scale(scaleStep, cx, cy);
		//setCenter();
		//makeHandleBox();
	
	}

	public void scale(double scaleStep, double dx, double dy)
	{	
		
		//scaleX *= scaleStep;
		//scaleY *= scaleStep;

		//breedte = (int) Math.round(scaleStep * breedte);
		//hoogte = (int) Math.round(scaleStep * hoogte);

		//tekstX = (int) Math.round((1/scaleStep) * tekstX);// + (1 - (1/scaleStep)) * cx);
		//tekstY = (int) Math.round((1/scaleStep) * tekstY);// + (1 - (1/scaleStep)) * cy);
		
		//translate((int) Math.round((scaleStep - 1) * cx + (1 - scaleStep) * dx), 
		//		  (int) Math.round((scaleStep - 1) * cy + (1 - scaleStep) * dy));
		
		//AffineTransform sc = new AffineTransform(scaleStep, 0, 0, scaleStep, (1 - scaleStep) * dx, (1 - scaleStep) * dy);
		//at = at.leftMultiplyBy(sc);

		//atGWT = atGWT.leftMultiplyBy(sc);
		
		//makeBB();
		//bb2.scale(scaleStep, dx, dy);
		//setCenter();
		//makeHandleBox();
	
	}

	public void scale(double sx, double sy)
	{	
		scaleX *= sx;
		scaleY *= sy;
		
		breedte = (int) Math.round(sx * breedte);
		hoogte = (int) Math.round(sy * hoogte);
	
		tekstX = (int) Math.round((1/sx) * tekstX);// + (1 - (1/sx)) * cx);
		tekstY = (int) Math.round((1/sy) * tekstY);// + (1 - (1/sy)) * cy);
	
		//AffineTransform sc = new AffineTransform(sx, 0, 0, sy, (1 - sx) * cx, (1 - sy) * cy);
		//at = at.leftMultiplyBy(sc);
		
		//atGWT = atGWT.leftMultiplyBy(sc);
		
		//makeBB();
		//bb2.scale(sx, sy, cx, cy);
		//setCenter();
		//makeHandleBox();
	
	}

	public void scale(double sx, double sy, double dx, double dy)
	{	
		
		//scaleX *= sx;
		//scaleY *= sy;

		//breedte = (int) Math.round(sx * breedte);
		//hoogte = (int) Math.round(sy * hoogte);
	
		//tekstX = (int) Math.round((1/sx) * tekstX);// + (1 - (1/sx)) * cx);
		//tekstY = (int) Math.round((1/sy) * tekstY);// + (1 - (1/sy)) * cy);

		//translate((int) Math.round((sx - 1) * cx + (1 - sx) * dx), 
		//		  (int) Math.round((sy - 1) * cy + (1 - sy) * dy));
		
		//AffineTransform sc = new AffineTransform(sx, 0, 0, sy, (1 - sx) * dx, (1 - sy) * dy);
		//at = at.leftMultiplyBy(sc);

		//atGWT = atGWT.leftMultiplyBy(sc);
		
		//makeBB();
		//bb2.scale(sx, sy, dx, dy);
		//setCenter();
		//makeHandleBox();
	
	}
	
	public Hashtable getState()
	{	Hashtable h = new Hashtable();

//System.out.println("te getstate " + tekst);
if (tekst.indexOf("P") >= 0)
{		
//System.out.println("getState atGWT " + tekst + " = " + atGWT.toString());
//System.out.println("xPos = " + xPos);
//System.out.println("yPos = " + yPos);
//System.out.println("scaleX = " + scaleX);
//System.out.println("scaleY = " + scaleY);
//System.out.println("breedte = " + breedte);
//System.out.println("hoogte = " + hoogte);

}


		h.put("kleur", kleur);
		h.put("kleurgwt", new String("rgb(" + kleur.getRed()+ "," + kleur.getGreen() + "," + kleur.getBlue() + ")"));		
		h.put("tekst", new String(tekst));
		h.put("xPos", new Integer(xPos));
		h.put("yPos", new Integer(yPos));
		h.put("bGWT", new Integer(breedte));
		h.put("hGWT", new Integer(hoogte));
		
		
		h.put("rotation", new Double(rotation));
		h.put("scaleX", new Double(scaleX));
		h.put("scaleY", new Double(scaleY));
		
		//h.put("m00", new Double(at.m00));
		//h.put("m10", new Double(at.m10));
		//h.put("m01", new Double(at.m01));
		//h.put("m11", new Double(at.m11));
		//h.put("b0", new Double(at.b0));
		//h.put("b1", new Double(at.b1));

		//h.put("m00GWT", new Double(atGWT.m00));
		//h.put("m10GWT", new Double(atGWT.m10));
		//h.put("m01GWT", new Double(atGWT.m01));
		//h.put("m11GWT", new Double(atGWT.m11));
		//h.put("b0GWT", new Double(atGWT.b0));
		//h.put("b1GWT", new Double(atGWT.b1));
		
		return h;
	}

	public static TekstElement setState(Hashtable h)
	{
//System.out.println("te setstate");

		Color kleur = Color.black;
		String tekst = new String("");
		int xPos = 0;
		int yPos = 0;
		

		double rotation = 0;
		double scaleX = 1;
		double scaleY = 1;

		//double m00 = 1;
		//double m01 = 0;
		//double m10 = 0;
		//double m11 = 1;
		//double b0 = 0;
		//double b1 = 0;

		//double m00GWT = 1;
		//double m01GWT = 0;
		//double m10GWT = 0;
		//double m11GWT = 1;
		//double b0GWT = 0;
		//double b1GWT = 0;
		
		
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

		//if (h.containsKey("m00"))
		//	m00 = ((Double) h.get("m00")).doubleValue();
		//if (h.containsKey("m10"))
		//	m10 = ((Double) h.get("m10")).doubleValue();
		//if (h.containsKey("m01"))
		//	m01 = ((Double) h.get("m01")).doubleValue();
		//if (h.containsKey("m11"))
		//	m11 = ((Double) h.get("m11")).doubleValue();
		//if (h.containsKey("b0"))
		//	b0 = ((Double) h.get("b0")).doubleValue();
		//if (h.containsKey("b1"))
		//	b1 = ((Double) h.get("b1")).doubleValue();
		
		//if (h.containsKey("m00GWT"))
		//	m00GWT = ((Double) h.get("m00GWT")).doubleValue();
		//if (h.containsKey("m10GWT"))
		//	m10GWT = ((Double) h.get("m10GWT")).doubleValue();
		//if (h.containsKey("m01GWT"))
		//	m01GWT = ((Double) h.get("m01GWT")).doubleValue();
		//if (h.containsKey("m11GWT"))
		//	m11GWT = ((Double) h.get("m11GWT")).doubleValue();
		//if (h.containsKey("b0GWT"))
		//	b0GWT = ((Double) h.get("b0GWT")).doubleValue();
		//if (h.containsKey("b1GWT"))
		//	b1GWT = ((Double) h.get("b1GWT")).doubleValue();
		
		TekstElement tekstElement = new TekstElement(kleur, tekst, xPos, yPos);
		//tekstElement.rotation = rotation;
		
		//if (h.containsKey("rotation"))
		if (h.containsKey("rotation"))
		{	
//System.out.println("h cont rotation " + tekst);

			tekstElement.process(scaleX, scaleY, rotation);
			//tekstElement.transformBy(m00, m01, m10, m11);
		}
/*		
		else if (h.containsKey("b0"))
		{
System.out.println("h cont b0 " + tekst);		
AffineTransform atTemp = new AffineTransform(m00, m01, m10, m11, b0, b1);
System.out.println("atTemp = " + atTemp.toString());		


			tekstElement.transformBy(m00, m01, m10, m11, b0, b1);
			tekstElement.transformGWTBy(m00GWT, m01GWT, m10GWT, m11GWT, b0GWT, b1GWT);
		}
		else
		{	
System.out.println("else " + tekst);			
			tekstElement.transformBy(m00, m01, m10, m11);
			tekstElement.transformGWTBy(m00GWT, m01GWT, m10GWT, m11GWT);

		}
*/		
		
		
		return tekstElement;
	}

	public TekstElement updateState()
	{
		TekstElement tekstElement = new TekstElement(kleur, tekst, xPos, yPos);
		tekstElement.rotate(rotation);
		tekstElement.scale(scaleX, scaleY);
		
		return tekstElement;
	}

	public TekstElement updateState(TekstElement te)
	{
		TekstElement tekstElement = new TekstElement(te.kleur, te.tekst, te.xPos, te.yPos);
		tekstElement.rotate(te.rotation);
		tekstElement.scale(te.scaleX, te.scaleY);
		
		return tekstElement;
	}

	public boolean isEqualTo(TekstElement te)
	{
		return isEqualTo(te.xPos,xPos) && isEqualTo(te.yPos,yPos) &&
			   isEqualTo(te.breedte,breedte) && isEqualTo(te.hoogte,hoogte) &&
			   te.tekst.equals(tekst);
	}
	
	public boolean isEqualTo(int i1, int i2)
	{
		return ((int) Math.round(i1 - i2)) < 4;
	}
	
	public String printTekst()
	{
		return "" + xPos + " " + yPos + " " + breedte + " " + hoogte; 
	}
	
	public void process(double scaleX, double scaleY, double rotation)
	{
		
//System.out.println("proc rot = " + UF.format(rotation,2) + " sx = " + UF.format(scaleX,2) +
//														   " sy = " + UF.format(scaleY,2));

		rotate(rotation);
		scale(scaleX, scaleY);
		
	}
	public void transformBy(double m00, double m01, double m10, double m11)
	{
		
		//at = new AffineTransform(m00, m01, m10, m11, - m00*cx - m01*cy + cx, - m10*cx - m11*cy + cy);
	}
	
	public void transformBy(double m00, double m01, double m10, double m11, double b0, double b1)
	{
		//at = new AffineTransform(m00, m01, m10, m11, b0, b1);
//System.out.println("transformBy at = " + at.toString());		
	}

	public void transformGWTBy(double m00, double m01, double m10, double m11)
	{
		
		//atGWT = new AffineTransform(m00, m01, m10, m11, - m00*cx - m01*cy + cx, - m10*cx - m11*cy + cy);
	}

	public void transformGWTBy(double m00, double m01, double m10, double m11, double b0, double b1)
	{
		
		//atGWT = new AffineTransform(m00, m01, m10, m11, b0, b1);
//System.out.println("transformGWTBy at = " + atGWT.toString());		
	}
	
	//public void teken(Graphics2D g, boolean tekstEltIsbeingHandled)
	public void teken(Graphics2D g)
	{
		
//System.out.println("teken " + tekst);		
		makeBB();
		//bb2.transformBy(at);
		setCenter();
		bb2.rotate(rotation, cx, cy);
		makeHandleBox();
		
		java.awt.geom.AffineTransform oldAT = g.getTransform();

//System.out.println(oldAT.toString());

		//java.awt.geom.AffineTransform tempAT = 
		//	new java.awt.geom.AffineTransform(at.m00, at.m10, at.m01, at.m11, at.b0, at.b1); 
		
		//g.setTransform(tempAT);
		
		java.awt.geom.AffineTransform currAT = g.getTransform();
		currAT.rotate(rotation, cx, cy);
		//if (!tekstEltIsbeingHandled)
		currAT.scale(scaleX, scaleY);
		//else
		//	currAT.scale(oldScaleX, oldScaleY);
		
		g.setTransform(currAT);
		
		g.setFont(KladjeVeld.tekstFont);
		g.setColor(kleur);
		g.drawString(tekst, tekstX, tekstY + ascent);
		
		g.setTransform(oldAT);
		
		//tekenBB(g);
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
		g.setStroke(new BasicStroke(0.8f));
		g.setColor(KladjeVeld.bbColor);

		bb2.draw(g, KladjeVeld.bbColor, null);
				
		g.setStroke(new BasicStroke(1.5f, 2, 0, 10.0f, null, 0.0f));
		
	}

/*	
	public double inverseRotX(double x, double y)
	{
		double rotX = Math.cos(-rotation) * x - Math.sin(- rotation) * y;
		
		return rotX;
		
	}
*/
/*	
	public double inverseRotY(double x, double y)
	{
		double rotY = Math.sin(-rotation) * x + Math.cos(- rotation) * y;
		
		return rotY;
	}
*/	
	
/*	
	public int inverseTransformX(int x, int y)
	{
		double rotX = Math.cos(-rotation) * (x - cx) - Math.sin(- rotation) * (y - cy);
		int rx = (int) Math.round(cx + rotX);
		
		return rx;
	}
*/
/*	
	public int inverseTransformY(int x, int y)
	{
		double rotY = Math.sin(-rotation) * (x - cx) + Math.cos(- rotation) * (y - cy);
		int ry = (int) Math.round(cy + rotY);
		
		return ry;
	}
*/
	public boolean bbContains(int x, int y)
	{
		
		return bb2.contains(x, y);
	}

	public void translate(int dx, int dy)
	{
		
		xPos += dx;
		yPos += dy;
		
		tekstX = (int) Math.round(((double) xPos) / scaleX);
		tekstY = (int) Math.round(((double) yPos) / scaleY);
		
		
		//AffineTransform trans = new AffineTransform (1,0,0,1,dx,dy);
		//at = at.leftMultiplyBy(trans);
		
		//atGWT = atGWT.leftMultiplyBy(trans);
		
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

		boolean isContainedIn = true;
		
		int topLeftX = bb2.geefPuntX(0);
		int topRightX = bb2.geefPuntX(1);
		int bottomRightX = bb2.geefPuntX(2);
		int bottomLeftX = bb2.geefPuntX(3);

		int topLeftY = bb2.geefPuntY(0);
		int topRightY = bb2.geefPuntY(1);
		int bottomRightY = bb2.geefPuntY(2);
		int bottomLeftY = bb2.geefPuntY(3);
		
		int topMiddleX = (topLeftX + topRightX) / 2;
		int topMiddleY = (topLeftY + topRightY) / 2;
		int rightMiddleX = (topRightX + bottomRightX) / 2;
		int rightMiddleY = (topRightY + bottomRightY) / 2;
		int bottomMiddleX = (bottomLeftX + bottomRightX) / 2;
		int bottomMiddleY = (bottomLeftY + bottomRightY) / 2;
		int leftMiddleX = (topLeftX + bottomLeftX) / 2;
		int leftMiddleY = (topLeftY + bottomLeftY) / 2;
		
		isContainedIn = isContainedIn && r.contains(topMiddleX, topMiddleY);
		isContainedIn = isContainedIn && r.contains(rightMiddleX, rightMiddleY);
		isContainedIn = isContainedIn && r.contains(bottomMiddleX, bottomMiddleY);
		isContainedIn = isContainedIn && r.contains(leftMiddleX, leftMiddleY);
		
		//for (int pCnt = 0; pCnt < bb2.aantalPunten; pCnt++)
		//{
		//	isContainedIn = isContainedIn && r.contains(bb2.puntenX[pCnt], bb2.puntenY[pCnt]);
		//}
		
		return isContainedIn;
	}
	

}