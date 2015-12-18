package fi.graphtool;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.CubicCurve2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.vecmath.Vector2d;
import javax.swing.JComponent;

import fi.wiskopdr.expressies.Expressie;

class GrafiekVeld extends JComponent{
	private final int cMaxPiLinesOnScreen = 8;
		
	private final GraphToolInteractiePanel gtip;

	public GrafiekVeld(GraphToolInteractiePanel graphToolInteractiePanel, int x, int y, int b, int h)
	{	
		gtip = graphToolInteractiePanel;
		super.setBounds(x,y,b,h);
	}
	
	public boolean activateXAsNaam(int x, int y)
	{	return gtip.xAsNaamActivator.contains(x,y);
	}
	
	public boolean activateYAsNaam(int x, int y)
	{	return gtip.yAsNaamActivator.contains(x,y);
	}
	

	public double pixelsXtoValue(double pixelsX) { 
		// This function also needs to perform for values in between pixels, therefore a double is used to represent pixelsX 
		double scalingMultiplier;		
		if (gtip.manualScalingX) {
			scalingMultiplier = gtip.eenheidxValue;
		}
		else {
			scalingMultiplier = gtip.schaalFactorX;			
		}
		double valueX = (pixelsX-gtip.beginx)/gtip.eenheidxD*scalingMultiplier;
		if (gtip.xAsLog) {
			valueX = Math.pow(10, valueX);
		} 
		return valueX;
	}
	
	public double pixelsYtoValue(double pixelsY) { 
		// This function also needs to perform for values in between pixels, therefore a double is used to represent pixelsX 
		double scalingMultiplier;		
		if (gtip.manualScalingX) {
			scalingMultiplier = gtip.eenheidxValue;
		}
		else {
			scalingMultiplier = gtip.schaalFactorX;			
		}
//		double valueY = (pixelsX-gtip.beginx)/gtip.eenheidxD*scalingMultiplier;
		double valueY = (scalingMultiplier * (-gtip.beginy) / gtip.eenheidyD +
							scalingMultiplier * (getSize().height -pixelsY) / gtip.eenheidyD);
		if (gtip.yAsLog) {
			valueY = Math.pow(10, valueY);
		} 
		return valueY;
	}

	public double valueXtoPixels(double valueX) {
		double pixelsX;
		double scalingDivider;
		if (gtip.manualScalingX) {
			scalingDivider = gtip.eenheidxValue;
		}
		else {
			scalingDivider = gtip.schaalFactorX;			
		}
		
		double valX;
		if (gtip.xAsLog) {
			valX = Math.log10(valueX);		
//	        pixelsX = (int) Math.round(gtip.beginx + valX*gtip.eenheidxD/gtip.schaalFactorX);
		} else {
//			pixelsX = (int) Math.round(gtip.beginx + valueX*gtip.eenheidxD/gtip.eenheidxValue);
			valX = valueX;
		}
        pixelsX = gtip.beginx + valX*gtip.eenheidxD/scalingDivider;
		return pixelsX;
	}
	
	public double valueYtoPixels(double valueY) {
		double pixelsY;
		double scalingDivider;
		if (gtip.manualScalingY) {
			scalingDivider = gtip.eenheidyValue;
		}
		else {
			scalingDivider = gtip.schaalFactorY;			
		}
		double valY;
		if (gtip.yAsLog) {
			valY = Math.log10( valueY);
//	        pixelsY = getSize().height - (int) Math.round(gtip.beginy + valY*gtip.eenheidyD/gtip.schaalFactorY);
		} else {
//			pixelsY = getSize().height - (int) Math.round(gtip.beginy + valueY*gtip.eenheidyD/gtip.eenheidyValue);
			valY = valueY;
		}
        pixelsY = getSize().height - (gtip.beginy + valY*gtip.eenheidyD/scalingDivider);
		return pixelsY; 
	}
	
	private void calculateStream(Point2D.Double pStartScherm, int xIndex, int yIndex, FieldData fieldData, Expressie xAsExpressie, Expressie yAsExpressie) {
		final double cSampleDist = 0.25; // in pixels
		final int cMaxIter = 200;
		fieldData.startPad(xIndex, yIndex, pStartScherm);
		Point2D.Double pScherm = new Point2D.Double();
		pScherm.setLocation(pStartScherm);
		for (int i=0; i<cMaxIter; i++) {
			Vector2d vScherm = calculateVector(pScherm, xAsExpressie,  yAsExpressie);
			if (vScherm.length() > cSampleDist ) {
				vScherm.normalize();  // eenheidVector (1 pixel)
				vScherm.scale(cSampleDist); // zet op sterkte van sample afstand
			}
			pScherm.setLocation(pScherm.getX()+vScherm.x, pScherm.getY()+vScherm.y);
			if (i%50==0)
				fieldData.verlengPad(xIndex, yIndex, pScherm);
		}
	}
	
	private Vector2d calculateVector(Point2D.Double pScherm, Expressie xAsExpressie, Expressie yAsExpressie) { // TODO
		Point2D.Double pWerkelijk = new Point2D.Double();
		pWerkelijk.setLocation(pixelsXtoValue(pScherm.getX()), pixelsYtoValue(pScherm.getY()));
		
		Vector2d vWerkelijk = new Vector2d(	xAsExpressie.substitueer(pWerkelijk.getX(), gtip.xAsNaam).substitueer(pWerkelijk.getY(), gtip.yAsNaam).geefWaarde(),
											yAsExpressie.substitueer(pWerkelijk.getX(), gtip.xAsNaam).substitueer(pWerkelijk.getY(), gtip.yAsNaam).geefWaarde());
		
		Point2D.Double pEindWerkelijk = new Point2D.Double(pWerkelijk.getX()+vWerkelijk.x, pWerkelijk.getY()+vWerkelijk.y);
		Point2D.Double pEindScherm = new Point2D.Double(valueXtoPixels(pEindWerkelijk.getX()),valueYtoPixels(pEindWerkelijk.getY()));

		Vector2d vScherm = new Vector2d();
		vScherm.set(pEindScherm.getX()-pScherm.getX(), pEindScherm.getY()-pScherm.getY());
		return (vScherm);
	}
	
	private void tekenPijlpunt(Graphics2D g, Point vectorStartScherm, Point vectorEindScherm ) { 
	    double h = 3*Math.sqrt(3), w = 3;
	    Vector2d A = new Vector2d();
	    
	    Vector2d Vec = new Vector2d(vectorEindScherm.getX()-vectorStartScherm.getX(), vectorEindScherm.getY()-vectorStartScherm.getY());
	    
	    Vector2d U = new Vector2d(Vec.x/Vec.length(), Vec.y/Vec.length());
	    //U.set(Vec.x/Vec.length(), Vec.y/Vec.length());
	    
//		System.out.println("tekenPijlpunt :: Vec.x =" + Vec.x);
//		System.out.println("tekenPijlpunt :: Vec.y =" + Vec.y);
//		System.out.println("tekenPijlpunt :: U.x =" + U.x);
//		System.out.println("tekenPijlpunt :: U.y =" + U.y);
		Vector2d V = new Vector2d(-U.y, U.x);
		Vector2d v1 = new Vector2d(vectorEindScherm.getX() -h * U.x + w*V.x, vectorEindScherm.getY() -h * U.y + w*V.y);
		Vector2d v2 = new Vector2d(vectorEindScherm.getX() -h * U.x - w*V.x, vectorEindScherm.getY() -h * U.y - w*V.y);
		g.drawLine((int) v1.x, (int) v1.y, (int) vectorEindScherm.getX(), (int) vectorEindScherm.getY());
		g.drawLine((int) v2.x, (int) v2.y, (int) vectorEindScherm.getX(), (int) vectorEindScherm.getY());

	}
	
	/* EXAMPLE TODO	
	void arrowhead(vec A, vec B, vec& v1, vec& v2) {
	    float h = 10*sqrtf(3), w = 10;
	    vec U = (B - A)/(B - A).length();
	    vec V = vec(-U.y, U.x);
	    v1 = B - h*U + w*V;
	    v2 = B - h*U - w*V;
	}	
*/
	
	private Point tekenVector(Graphics2D g, Point vectorStartScherm, double maxLengte, boolean fixedSize, boolean tekenPijlpunt) {
		double vectorStartXWaarde = pixelsXtoValue(vectorStartScherm.getX()); 
		double vectorStartYWaarde = pixelsYtoValue(vectorStartScherm.getY());
		
		Expressie xAsExpressie = gtip.veldFuncties[0][0];
		Expressie yAsExpressie = gtip.veldFuncties[0][1];
		double vectorEindXWaarde = vectorStartXWaarde + xAsExpressie.substitueer(vectorStartXWaarde, gtip.xAsNaam).substitueer(vectorStartYWaarde, gtip.yAsNaam).geefWaarde();
		double vectorEindYWaarde = vectorStartYWaarde + yAsExpressie.substitueer(vectorStartXWaarde, gtip.xAsNaam).substitueer(vectorStartYWaarde, gtip.yAsNaam).geefWaarde();
				
	    Vector2d vectorScherm = new Vector2d(valueXtoPixels(vectorEindXWaarde)-vectorStartScherm.getX(), valueYtoPixels(vectorEindYWaarde)-vectorStartScherm.getY());

		if ( maxLengte > 0 && vectorScherm.length() > 0) {
			//Resize vector
			if ( (fixedSize) || (vectorScherm.length() > maxLengte) ) {
				vectorScherm.normalize();
				vectorScherm.scale(maxLengte);
			} 
		} 
		else {
			vectorScherm.scale(0.2);
		}

		Point vectorEindScherm = new Point();
		vectorEindScherm.setLocation(vectorStartScherm.getX() + vectorScherm.x, vectorStartScherm.getY() + vectorScherm.y);
		
		if ( ((int) vectorStartScherm.getX()!= (int) vectorEindScherm.getX()) || ((int) vectorStartScherm.getY()!= (int)vectorEindScherm.getY()) ) { 
			// alleen tekenen wanneer er lengte is
			g.drawLine((int) vectorStartScherm.getX(), (int) vectorStartScherm.getY(), (int) vectorEindScherm.getX(), (int) vectorEindScherm.getY());

			
			if (tekenPijlpunt) {
				tekenPijlpunt(g, vectorStartScherm, vectorEindScherm);
			}
		}

//		System.out.println("tekenVector :: vectorStartXWaarde =" + vectorStartXWaarde);
//		System.out.println("tekenVector :: vectorStartYWaarde =" + vectorStartYWaarde);
//		System.out.println("tekenVector :: vectorEindXWaarde =" + vectorEindXWaarde);
//		System.out.println("tekenVector :: vectorEindYWaarde =" + vectorEindYWaarde);
//		System.out.println("tekenVector :: vectorStartScherm.x =" + vectorStartScherm.getX());
//		System.out.println("tekenVector :: vectorStartScherm.y =" + vectorStartScherm.getY());
//		System.out.println("tekenVector :: vectorEindScherm.x =" + vectorEindScherm.getX());
//		System.out.println("tekenVector :: vectorEindScherm.y =" + vectorEindScherm.getY());

		
		return (vectorEindScherm);
	}
	
	private void tekenStroomlijn() {}

/* EXAMPLE TODO	
	void arrowhead(vec A, vec B, vec& v1, vec& v2) {
	    float h = 10*sqrtf(3), w = 10;
	    vec U = (B - A)/(B - A).length();
	    vec V = vec(-U.y, U.x);
	    v1 = B - h*U + w*V;
	    v2 = B - h*U - w*V;
	}	
*/
	
	public void	paintComponent(Graphics gr)
	{	Graphics2D g = (Graphics2D) gr;
	
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL,RenderingHints.VALUE_STROKE_NORMALIZE);
		
		int breedte = getSize().width;
		int hoogte = getSize().height;
		
		int bx = (int)Math.round(gtip.beginx);			
		int by = (int)Math.round(gtip.beginy);

		int ehx, ehy;
		double ehxD, ehyD;
		double origEenheidxD = gtip.eenheidxD;
		double origEenheidyD = gtip.eenheidyD;
		int origEenheidx = gtip.eenheidx;
		int origEenheidy = gtip.eenheidy;
		int manScalingMultiplyX=1, manScalingMultiplyY=1;

		if (gtip.xAsLog || !gtip.manualScalingX) {
			// scaling based on standard units based, schaalfactor is used
			if (gtip.xAsLog) {
				ehx = 2*gtip.eenheid; //twice the normal unit				
			}
			else {
				ehx = gtip.eenheid;
			}
			gtip.eenheidxD = (double) ehx; // very dirty programming to change this member here
			                               // but at this point inevitable, this member needs to 
										   // be reset at the end of this method!
			gtip.eenheidx = ehx;
			ehxD = (double) ehx;
		} else {
			// Manual scaling, adjustable scaling is used
			ehxD = gtip.eenheidxD;
			if (ehxD == 0) { // fail safe
				ehxD = 16.0;
			}

			while (ehxD * manScalingMultiplyX<0.2*gtip.eenheid) {
				manScalingMultiplyX*=2;
			}

			ehxD = manScalingMultiplyX * ehxD;
			ehx = (int) Math.round(ehxD);
		}			

		if (gtip.yAsLog || !gtip.manualScalingY) {
			// scaling based on standard units based, schaalfactor is used
			if (gtip.yAsLog) {
				ehy = 2*gtip.eenheid;							
			} 
			else {
				ehy = gtip.eenheid;			
			}				
			gtip.eenheidyD = (double) ehy; // very dirty programming to change this member here
			                               // but at this point inevitable, this member needs to 
										   // be reset at the end of this method!
			gtip.eenheidy = ehy;
			ehyD = (double) ehy;
		} else {
			// Manual scaling, adjustable scaling is used
			ehyD = gtip.eenheidyD;
			if (ehyD == 0) { // fail safe
					ehyD = 16.0;
			}

			while (ehyD * manScalingMultiplyY<0.2*gtip.eenheid) {
				manScalingMultiplyY*=2;
			}

			ehyD = manScalingMultiplyY * ehyD;
			ehy = (int) Math.round(ehyD);
		}		
		
		int maxWoordBreedteY = 0;
		int maxWoordHoogteX = 10;
		boolean witruimteX = by <= 12;
		boolean witruimteY = false;
		int maxHoogteLijn = hoogte-Math.max(witruimteX?maxWoordHoogteX:0,(gtip.yPositief?by:0));
				
		g.setFont(gtip.font);
		gtip.fm = g.getFontMetrics();

		int imin = -(int)Math.round(gtip.beginx/ehx); 
		int imax = 1+breedte/ehx-(int)Math.round(gtip.beginx/ehx);
		int jmin = -(int)Math.round(gtip.beginy/ehy); 
		int jmax = 1+hoogte/ehy-(int)Math.round(gtip.beginy/ehy);
		
		if (gtip.roosterZichtbaar || gtip.schaalZichtbaar) {

			for(int j=jmin+1 ; j<jmax-1 ; j++) {	
// 				String getal = gtip.df.format(gtip.schaalFactorY*(j)); // draw graph based on scale -> old
				String getal;
				if (gtip.manualScalingY) {
					getal = gtip.df.format(gtip.eenheidyValue*(j));					
				}
				else { // standard scaling, possibly logarithmic
					getal = gtip.df.format(gtip.schaalFactorY*(j));
					if(gtip.yAsLog) {
						getal = 10 + toSuperScript(getal);
					}					
				}
				int woordbreedte = gtip.fm.stringWidth(getal);
				if(j!=0 && j%2==0 && (!gtip.yPositief || j>0))
				{	maxWoordBreedteY = Math.max(maxWoordBreedteY, woordbreedte);
				}
			}
			witruimteY = maxWoordBreedteY >= bx - 2;
			//log-roosterlijnen tekenen (iets lichter dan gewone roosterlijnen):
			g.setColor(new Color(240, 240, 240));
			if(gtip.roosterX)
				for(int i = imin+1; i < imax; i++)
					if((!gtip.xPositief || i > 0) && gtip.xAsLog && !gtip.roosterGrof)
						for(int k = 1; k < 10; k++)
							g.drawLine((int) (bx + i*gtip.eenheidxD + Math.log10(k)*gtip.eenheidxD), 0, 
									(int) (bx + i*gtip.eenheidxD + Math.log10(k)*gtip.eenheidxD), maxHoogteLijn);
			
			if(gtip.roosterY)   
				for(int j = jmin; j < jmax; j++)
					if((!gtip.yPositief || j > 0) && gtip.yAsLog && !gtip.roosterGrof)
						for(int k = 1; k < 10; k++)
							g.drawLine(Math.max(witruimteY?maxWoordBreedteY:0, gtip.xPositief?bx:0), (int) (hoogte - (by + j*gtip.eenheidyD + Math.log10(k)*gtip.eenheidyD)), breedte, 
									(int) (hoogte - (by + j*gtip.eenheidyD + Math.log10(k)*gtip.eenheidyD)));
			
			//gewone roosterlijnen tekenen:
			Color lijnenKleur = new Color(210, 210, 210);
			g.setColor(new Color(210, 210, 210));

			for(int i=imin ; i<imax ; i++) {	// X-axis
				
// 			for(int i=imin+1 ; i<imax ; i++) {	
// 				String getal = gtip.df.format(gtip.schaalFactorX*(i)); // draw graph based on scale -> old
//				String getal = gtip.df.format(gtip.eenheidxValue*(i));
//				if(gtip.xAsLog)
//					getal = 10 + toSuperScript(getal);
				String getal;
				if (gtip.manualScalingX) {
					getal = gtip.df.format(gtip.eenheidxValue*(i)*manScalingMultiplyX);					
				}
				else { // standard scaling, possibly logarithmic
					getal = gtip.df.format(gtip.schaalFactorX*(i));					
					if(gtip.xAsLog) {
						getal = 10 + toSuperScript(getal);
					}
				}
				int woordbreedte = gtip.fm.stringWidth(getal);
//				int xLabel = (int)(gtip.beginx+i*gtip.eenheidxD-woordbreedte/2);
				int xLabel = (int)(gtip.beginx+i*ehxD-woordbreedte/2);
//				int yLabel = Math.min(hoogte-1, hoogte-by+11);
				int yLabel = Math.max(11, Math.min(hoogte-4, hoogte-by+11));
				
				boolean schaalTekenen = (i%2 == 0 || gtip.xAsLog) && gtip.schaalZichtbaar && gtip.schaalX;
				//witruimteX = yLabel==hoogte-1;
				if(gtip.roosterZichtbaar && gtip.roosterX && (!gtip.xPositief || i > 0))
				{	if(schaalTekenen ) 
					{	g.drawLine((int)(bx+i*ehxD), 0, (int)(bx+i*ehxD), Math.min(yLabel - 9, maxHoogteLijn));
						if(maxHoogteLijn > yLabel + 2) {
							g.drawLine((int)(bx+i*ehxD), yLabel + 2, (int)(bx+i*ehxD), maxHoogteLijn);
							
						}
					}
					else if(i%2 == 0 || !gtip.roosterGrof || gtip.xAsLog)
						g.drawLine((int)(bx+i*ehxD),0,(int)(bx+i*ehxD), maxHoogteLijn);
				}
				if((!gtip.xPositief || i > 0) && schaalTekenen && i != 0)
				{
					g.setColor(Color.black);
					g.drawString(getal,	xLabel,	yLabel);
					g.setColor(lijnenKleur);
					
				}
			}

			for(int j=jmin ; j<jmax ; j++) {	
// 				String getal = gtip.df.format(gtip.schaalFactorY*(j)); // draw graph based on scale -> old
//				String getal = gtip.df.format(gtip.eenheidyValue*(j));
//				if(gtip.yAsLog)
//					getal = 10 + toSuperScript(getal);
				String getal;
	
				if (gtip.manualScalingY) {
					getal = gtip.df.format(gtip.eenheidyValue*(j)*manScalingMultiplyY);					
				}
				else { // standard scaling, possibly logarithmic
					getal = gtip.df.format(gtip.schaalFactorY*(j));
					if(gtip.yAsLog) {
						getal = 10 + toSuperScript(getal);
					}					
				}
				int woordbreedte = gtip.fm.stringWidth(getal);
//				int xLabel = Math.max(maxWoordBreedteY-woordbreedte,bx-2-woordbreedte);
//				int xLabel = Math.max(maxWoordBreedteY-woordbreedte+4,bx-2-woordbreedte);
				int xLabel = Math.min(breedte-3-woordbreedte,Math.max(maxWoordBreedteY-woordbreedte+4,bx-2-woordbreedte));
				int yLabel = (int)(gtip.veldh+5-(gtip.beginy+j*ehyD));
				witruimteY = xLabel==maxWoordBreedteY-woordbreedte;
				int minimaalBegin = Math.max(witruimteY?maxWoordBreedteY:0, gtip.xPositief?bx:0);
				boolean schaalTekenen = (j%2 == 0 || gtip.yAsLog) && gtip.schaalZichtbaar && gtip.schaalY;
				if((!gtip.yPositief || j>0) && hoogte-(by+j*ehyD) <= maxHoogteLijn) 
				{
					if(schaalTekenen ) 
					{
						if(xLabel - 1 > minimaalBegin) {
							g.drawLine(minimaalBegin, (int)(hoogte-(by+j*ehyD)), xLabel - 1, (int)(hoogte-(by+j*ehyD)));
						}
						g.drawLine(Math.max(minimaalBegin, xLabel + woordbreedte + 1), (int)(hoogte-(by+j*ehyD)), breedte, (int)(hoogte-(by+j*ehyD)));
						
					}
					else if(j%2 == 0 || gtip.yAsLog || !gtip.roosterGrof)
						g.drawLine(minimaalBegin,(int)(hoogte-(by+j*ehyD)),breedte,(int)(hoogte-(by+j*ehyD)));
							
				}
				if((!gtip.yPositief || j>0) && schaalTekenen && j != 0)
				{
					g.setColor(Color.black);
					g.drawString(getal,	xLabel,yLabel);
					g.setColor(lijnenKleur);
				}
			}
		}
		
		if (gtip.piLijnenZichtbaar)
		{	
			
			double rangeX = pixelsXtoValue(breedte)-pixelsXtoValue(0);
			int piMultiplier = 1;
			while ( rangeX/(Math.PI*piMultiplier)>(double) cMaxPiLinesOnScreen) {
				piMultiplier++;
			}
			
			double scalingDivider = 0;
			if (gtip.manualScalingX ) {
				scalingDivider = gtip.eenheidxValue;
			}
			else {
				scalingDivider = gtip.schaalFactorX;				
			}				
 
			int dashStep = 5;
			int dashes = hoogte / dashStep;
			
			g.setColor(gtip.piColor);
			// 0 is in beeld
			if ((bx > 0) && (bx < breedte)) {	// Linkerkant
				int maxLCnt = (int) Math.round(gtip.beginx / (piMultiplier * Math.PI * gtip.eenheidxD / scalingDivider) );
				for (int lCnt = 1; lCnt <= maxLCnt; lCnt++)
				{	//int piX = (int) Math.round(beginx - lCnt * Math.PI * eenheidxD / schaalFactorX);
					int piX = (int) Math.round(gtip.beginx - (lCnt * piMultiplier * Math.PI * gtip.eenheidxD / scalingDivider));
						
					if ((piX > bx || !gtip.xPositief && piX > 0) && (piX < breedte))
					{	for (int dCnt = 0; dCnt < dashes; dCnt++)
						{	if ((dCnt % 2) == 0)
								if(dCnt * dashStep + dashStep < hoogte - by || !gtip.yPositief)
									g.drawLine(piX, dCnt * dashStep, piX, dCnt * dashStep + dashStep);
						}	
						g.setColor(Color.black);
//						double aantalPi = lCnt * gtip.schaalFactorX;
						double aantalPi = piMultiplier *lCnt;
						int aantalPiInt = (int) aantalPi;
						if(aantalPi == 0);
						else if(aantalPi == 1)
							g.drawString("-" + "\u03C0", piX-3, hoogte - by + 20);
						else
						{	if(aantalPiInt == aantalPi)
								g.drawString("-" + aantalPiInt + "\u03C0", piX - 3, hoogte - by + 20);
							else
								g.drawString("-" + Double.toString(aantalPi) + "\u03C0", piX - 3, hoogte - by + 20);
						}
						
						g.drawLine(piX,hoogte-by-2,piX,hoogte-by+2);
						g.setColor(gtip.piColor);
					}
				}
				int maxRCnt = (int) Math.round((breedte - gtip.beginx) / (piMultiplier * Math.PI * gtip.eenheidxD / scalingDivider));
				for (int rCnt = 1; rCnt <= maxRCnt; rCnt++) { // Rechterkant	
					int piX = (int) Math.round(gtip.beginx + rCnt * (piMultiplier *  Math.PI * gtip.eenheidxD / scalingDivider) );
					if ((piX > bx || !gtip.xPositief && piX > 0) && (piX < breedte))
					{	for (int dCnt = 0; dCnt < dashes; dCnt++)
						{	if ((dCnt % 2) == 0)
								if(dCnt * dashStep + dashStep < hoogte - by || !gtip.yPositief)
									g.drawLine(piX, dCnt * dashStep, piX, dCnt * dashStep + dashStep);
						}	
						g.setColor(Color.black);
// 						double aantalPi = rCnt * gtip.schaalFactorX;
						double aantalPi = piMultiplier *rCnt;
						int aantalPiInt = (int) aantalPi;
						if(aantalPi == 0);
						else if(aantalPi == 1)
							g.drawString("\u03C0", piX-3, hoogte - by + 20);
						else
						{	if(aantalPiInt == aantalPi)
								g.drawString(aantalPiInt + "\u03C0", piX - 3, hoogte - by + 20);
							else
								g.drawString(Double.toString(aantalPi) + "\u03C0", piX - 3, hoogte - by + 20);
						}
						
						g.drawLine(piX,hoogte-by-2,piX,hoogte-by+2);
						g.setColor(gtip.piColor);
					}
				}
				
			}	
			// 0 is links
			else if (bx <= 0)
			{	int maxRCnt = (int) Math.round((breedte - gtip.beginx) / (piMultiplier * Math.PI * gtip.eenheidxD / scalingDivider));
				for (int rCnt = 1; rCnt <= maxRCnt; rCnt++)
				{	//int piX = (int) Math.round(beginx + rCnt * Math.PI * eenheidxD / schaalFactorX);
					int piX = (int) Math.round(gtip.beginx + rCnt * (piMultiplier * Math.PI * gtip.eenheidxD / scalingDivider));
					if ((piX > 0) && (piX < breedte))
					{	for (int dCnt = 0; dCnt < dashes; dCnt++)
						{	if ((dCnt % 2) == 0)
								if(dCnt * dashStep + dashStep < hoogte - by || !gtip.yPositief)
									g.drawLine(piX, dCnt * dashStep, piX, dCnt * dashStep + dashStep);
						}	
						g.setColor(Color.black);
//						double aantalPi = rCnt * gtip.schaalFactorX;
						double aantalPi = piMultiplier *rCnt;
						int aantalPiInt = (int) aantalPi;
						if(aantalPi == 0);
						else if(aantalPi == 1)
							g.drawString("\u03C0", piX-3, hoogte - by + 20);
						else
						{	if(aantalPiInt == aantalPi)
								g.drawString(aantalPiInt + "\u03C0", piX - 3, hoogte - by + 20);
							else
								g.drawString(Double.toString(aantalPi) + "\u03C0", piX - 3, hoogte - by + 20);
						}
						
						g.drawLine(piX,hoogte-by-2,piX,hoogte-by+2);
						g.setColor(gtip.piColor);
					}
				}
			}		
			// 0 is rechts
			else if (bx >= breedte && !gtip.xPositief) {	
				int maxLCnt = (int) Math.round( gtip.beginx / (piMultiplier * Math.PI * gtip.eenheidxD / scalingDivider));
				for (int lCnt = 1; lCnt <= maxLCnt; lCnt++) {	
					//int piX = (int) Math.round(beginx - lCnt * Math.PI * eenheidxD / schaalFactorX);
					int piX = (int) Math.round(gtip.beginx - lCnt * (piMultiplier * Math.PI * gtip.eenheidxD / scalingDivider));
					if ((piX > 0) && (piX < breedte))
					{	for (int dCnt = 0; dCnt < dashes; dCnt++)
						{	if ((dCnt % 2) == 0)
								if(dCnt * dashStep + dashStep < hoogte - by || !gtip.yPositief)
									g.drawLine(piX, dCnt * dashStep, piX, dCnt * dashStep + dashStep);
						}
						g.setColor(Color.black);
//						double aantalPi = lCnt * gtip.schaalFactorX;
						double aantalPi = piMultiplier *lCnt;
						int aantalPiInt = (int) aantalPi;
						if(aantalPi == 0);
						else if(aantalPi == 1)
							g.drawString("-" + "\u03C0", piX-3, hoogte - by + 20);
						else
						{	if(aantalPiInt == aantalPi)
								g.drawString("-" + aantalPiInt + "\u03C0", piX - 3, hoogte - by + 20);
							else
								g.drawString("-" + Double.toString(aantalPi) + "\u03C0", piX - 3, hoogte - by + 20);
						}
						g.drawLine(piX,hoogte-by-2,piX,hoogte-by+2);
						g.setColor(gtip.piColor);
					}
				}
			}		
		}
		
		if (gtip.assenZichtbaar)
		{
//			if(bx>1 && bx<breedte) {
//			g.drawLine(bx-1,0,bx-1,maxHoogteLijn);
//			g.drawLine(bx,0,bx,maxHoogteLijn);
//		}
			g.setColor(Color.black);
			// assen moeten immer op het scherm blijven
			if (bx<=1) {
				g.drawLine(0,0,0,maxHoogteLijn);
				g.drawLine(1,0,1,maxHoogteLijn);				
			} 
			else { // bx >1
				if (bx<breedte) { //(bx>1 && bx<breedte)
					g.drawLine(bx-1,0,bx-1,maxHoogteLijn);
					g.drawLine(bx,0,bx,maxHoogteLijn);
				}
				else { //bx >=breedte
					g.drawLine(breedte-2,0,breedte-2,maxHoogteLijn);
					g.drawLine(breedte-1,0,breedte-1,maxHoogteLijn);
				}
			}
			if (by<=0) {// by<=0
				g.drawLine(Math.max(witruimteY?maxWoordBreedteY:0, gtip.xPositief?bx:0),hoogte-2,breedte,hoogte-2);
				g.drawLine(Math.max(witruimteY?maxWoordBreedteY:0, gtip.xPositief?bx:0),hoogte-1,breedte,hoogte-1);
			}
			else { // by>0
				if (by<hoogte) { // (by>0 && by<hoogte)
					g.drawLine(Math.max(witruimteY?maxWoordBreedteY:0, gtip.xPositief?bx:0),hoogte-(by+1),breedte,hoogte-(by+1));
					g.drawLine(Math.max(witruimteY?maxWoordBreedteY:0, gtip.xPositief?bx:0),hoogte-(by),breedte,hoogte-(by));
				} 
				else { // by>=hoogte
					g.drawLine(Math.max(witruimteY?maxWoordBreedteY:0, gtip.xPositief?bx:0),1,breedte,1);
					g.drawLine(Math.max(witruimteY?maxWoordBreedteY:0, gtip.xPositief?bx:0),0,breedte,0);
				}
			}
//			if(by>0 && by<hoogte) {	
//				g.drawLine(Math.max(witruimteY?maxWoordBreedteY:0, gtip.xPositief?bx:0),hoogte-(by+1),breedte,hoogte-(by+1));
//				g.drawLine(Math.max(witruimteY?maxWoordBreedteY:0, gtip.xPositief?bx:0),hoogte-(by),breedte,hoogte-(by));
//			}
			g.setFont(new Font ("SansSerif",Font.ITALIC,10 ));
			g.drawString("O",bx-11,hoogte-by+10);
			
			g.setColor(Color.black);
//			System.out.println("Hij komt hier" );

			g.setFont(new Font(gtip.font.getName(), Font.ITALIC, gtip.font.getSize()));
			FontMetrics fm = g.getFontMetrics();
			int woordbreedte = fm.stringWidth(gtip.grafiekXAsNaam);
			int formuleWoordbreedte = fm.stringWidth(gtip.grafiekYAsNaam);
			g.drawString(gtip.grafiekXAsNaam, breedte-woordbreedte-5,Math.min(hoogte-17, hoogte-(by)-5));
			g.drawString(gtip.grafiekYAsNaam, Math.max(18,bx+6), 9);
			
			gtip.xAsNaamActivator.setBounds(breedte-woordbreedte-5, Math.min(hoogte-17, hoogte-(by)-15), woordbreedte, 15);
			gtip.yAsNaamActivator.setBounds(Math.max(18,bx+6), 0, formuleWoordbreedte, 15);
			
			gtip.xAsNaamTF.setLocation(breedte-85,Math.min(hoogte-17, hoogte-(by)-15));
			gtip.yAsNaamTF.setLocation(Math.max(18,bx+6),0);
			
		}	
		
		if (gtip.tekenDocentFuncties != null && (gtip.typeOpdracht == GraphToolInteractiePanel.VINDFORMULEBIJGRAFIEK
				|| gtip.typeOpdracht == GraphToolInteractiePanel.TEKENPUNTENBIJFORMULE && gtip.score > 0 && (gtip.mode == 0 || gtip.mode == 1 || gtip.nagekeken)))
		{	

			for(int j = 0; j < gtip.tekenDocentFuncties.length; j++)
			{	if(gtip.tekenDocentFuncties[j] != null)
				{	g.setColor(gtip.docentColor);
					GeneralPath curve = new GeneralPath();
					int xMin = Math.max(witruimteY?maxWoordBreedteY:0, gtip.xPositief?bx:0);
					int xMax = breedte;
					if(gtip.docentDomeinen != null && gtip.docentDomeinen[j] != null) {	
						if(!Double.isInfinite(gtip.docentDomeinen[j][0])) {	
// 						int xMin2 = (int) Math.round(gtip.eenheidxD*(gtip.xAsLog?Math.log10(gtip.docentDomeinen[j][0]):gtip.docentDomeinen[j][0])
// 							/gtip.schaalFactorX + gtip.beginx);	
							int xMin2 = (int) Math.round(valueXtoPixels(gtip.docentDomeinen[j][0]));
							xMin = Math.max(xMin, xMin2);
						}
						if(!Double.isInfinite(gtip.docentDomeinen[j][1])) {	
//							int xMax2 = (int) Math.round(gtip.eenheidxD*(gtip.xAsLog?Math.log10(gtip.docentDomeinen[j][1]):gtip.docentDomeinen[j][1])
// 							/gtip.schaalFactorX + gtip.beginx);
							int xMax2 = (int) Math.round(valueXtoPixels(gtip.docentDomeinen[j][1]));
							xMax = Math.min(xMax, xMax2);
						}
					}
					for(int i=xMin ; i<xMax ; i++) {	
						double ii = i;
// 						double d0 = (gtip.tekenDocentFuncties[j].substitueer(gtip.xAsLog?Math.pow(10,gtip.schaalFactorX*(-gtip.beginx)/gtip.eenheidxD + gtip.schaalFactorX*ii
// 							/gtip.eenheidxD):gtip.schaalFactorX*(-gtip.beginx)/gtip.eenheidxD + gtip.schaalFactorX*ii/gtip.eenheidxD, gtip.xAsNaam)).geefWaarde();//dd0.doubleValue();
// 						double d1 = (gtip.tekenDocentFuncties[j].substitueer(gtip.xAsLog?Math.pow(10,gtip.schaalFactorX*(-gtip.beginx)/gtip.eenheidxD + gtip.schaalFactorX*(ii+1)
// 							/gtip.eenheidxD):gtip.schaalFactorX*(-gtip.beginx)/gtip.eenheidxD + gtip.schaalFactorX*(ii+1)/gtip.eenheidxD, gtip.xAsNaam)).geefWaarde();//dd0.doubleValue();
						double d0 = (gtip.tekenDocentFuncties[j].substitueer(pixelsXtoValue(ii), gtip.xAsNaam)).geefWaarde();
						double d1 = (gtip.tekenDocentFuncties[j].substitueer(pixelsXtoValue(ii), gtip.xAsNaam)).geefWaarde();
						double d0waarde = d0;
						double d1waarde = d1;
						if(Double.isNaN(d1waarde) && !Double.isNaN(d0))
						{	double newD1waarde = d0;
							for(int k = 1; k < 20; k++) {	
								double kd = k;
// 								double dt0 = (gtip.tekenDocentFuncties[j].substitueer(gtip.xAsLog?Math.pow(10,gtip.schaalFactorX*(-gtip.beginx)/gtip.eenheidxD + gtip.schaalFactorX*(ii+kd/20)
//									/gtip.eenheidxD):gtip.schaalFactorX*(-gtip.beginx)/gtip.eenheidxD + gtip.schaalFactorX*(ii+kd/20)/gtip.eenheidxD, gtip.xAsNaam)).geefWaarde();
								double dt0 = (gtip.tekenDocentFuncties[j].substitueer(pixelsXtoValue(ii+kd/20), gtip.xAsNaam)).geefWaarde();
								if(Double.isNaN(dt0))
								{	break;
								}
								else
									newD1waarde = dt0;
							}
							d1waarde = newD1waarde;
						}
						if(Double.isNaN(d0) && !Double.isNaN(d1))
						{	double newD0waarde = d1;
							for(int k = 19; k > 0; k--) {	
								double kd = k;
// 								double dt0 = (gtip.tekenDocentFuncties[j].substitueer(gtip.xAsLog?Math.pow(10,gtip.schaalFactorX*(-gtip.beginx)/gtip.eenheidxD + gtip.schaalFactorX*(ii+kd/20)
//									/gtip.eenheidxD):gtip.schaalFactorX*(-gtip.beginx)/gtip.eenheidxD + gtip.schaalFactorX*(ii+kd/20)/gtip.eenheidxD, gtip.xAsNaam)).geefWaarde();
								double dt0 = (gtip.tekenDocentFuncties[j].substitueer(pixelsXtoValue(ii+kd/20), gtip.xAsNaam)).geefWaarde();
								if(Double.isNaN(dt0))
								{	break;
								}
								else
									newD0waarde = dt0;
							}
							d0waarde = newD0waarde;
						}
					
						if(!(Double.isNaN(d0) && Double.isNaN(d1)) && (!gtip.yPositief || d0 >= 0 || d1 >= 0))
						{	int x0 = i;
							int x1 = i+1;
// 							double dy0 = hoogte -(gtip.beginy+gtip.eenheidyD*(gtip.yAsLog?Math.log10(d0waarde):d0waarde)/gtip.schaalFactorY);
//							double dy1 = hoogte -(gtip.beginy+gtip.eenheidyD*(gtip.yAsLog?Math.log10(d1waarde):d1waarde)/gtip.schaalFactorY);
							double dy0 = valueYtoPixels(d0waarde);
							double dy1 = valueYtoPixels(d1waarde);
							if(dy0>1000)dy0 = 1000;
							if(dy0<-1000)dy0 = -1000;
							if(dy1>1000)dy1 = 1000;
							if(dy1<-1000)dy1 = -1000;
							if(curve.getCurrentPoint()==null && (!gtip.yPositief || d0>=0))curve.moveTo((float)x0, (float)dy0);
							else if(curve.getCurrentPoint() == null) 
								curve.moveTo((float)x0, hoogte - gtip.beginy);
							if(!gtip.yPositief || d1>=0) curve.lineTo((float)x1, (float)dy1);
							else
								curve.lineTo((float)x1, hoogte - gtip.beginy); 
						}
						else if(curve.getCurrentPoint()!=null)
						{	g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL,RenderingHints.VALUE_STROKE_PURE);
							g.setStroke(new BasicStroke(1.2f));
							g.draw(curve);
							curve = new GeneralPath();
						}
						if(Double.isNaN(d1) || gtip.yPositief && d1<0)
						{	g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL,RenderingHints.VALUE_STROKE_PURE);
							g.setStroke(new BasicStroke(1.2f));
							if(curve.getCurrentPoint()!=null) g.draw(curve);
							curve = new GeneralPath();
						}
					}
					g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL,RenderingHints.VALUE_STROKE_PURE);
					g.setStroke(new BasicStroke(1.2f));
					g.draw(curve);
				}
			}
		}
		//if(//gtip.typeFormuleComponent != 1 && 
		//		gtip.nagekeken || gtip.mode == 0 || gtip.mode == 1)
		//	Dit klopt niet!!!
		//{
		//Alleen niet tekenen als aan alle drie de volgende voorwaarden is voldaan:
		//- modus is zelftoets of toets
		//- Er is nog niet nagekeken
		//- Opdrachttype is vindFormuleBijGrafiek of vindFormuleBijPunten

		if(!((gtip.mode == 2 || gtip.mode == 3) && !gtip.nagekeken && 
				(gtip.typeOpdracht == GraphToolInteractiePanel.VINDFORMULEBIJGRAFIEK || gtip.typeOpdracht == GraphToolInteractiePanel.VINDFORMULEBIJPUNTEN)))
			for(int j=0 ; j<gtip.functies.length ; j++)
			{	if(gtip.functies[j]!=null && gtip.yAsNaam.equals(gtip.grafiekYAsNaam))
				{	

				    g.setColor(Color.black);
					GeneralPath curve = new GeneralPath();
					int xMin = Math.max(witruimteY?maxWoordBreedteY:0, gtip.xPositief?bx:0);
					int xMax = breedte;
					if(gtip.domeinen != null && gtip.domeinen[j] != null) {	
						if(!Double.isInfinite(gtip.domeinen[j][0])) {	
//							int xMin2 = (int) Math.round(gtip.eenheidxD*(gtip.xAsLog?Math.log10(gtip.domeinen[j][0]):gtip.domeinen[j][0])
// 								/gtip.schaalFactorX + gtip.beginx);	
							int xMin2 = (int) Math.round(valueXtoPixels(gtip.domeinen[j][0]));
							xMin = Math.max(xMin, xMin2);
						}
						if(!Double.isInfinite(gtip.domeinen[j][1])) {	
//							int xMax2 = (int) Math.round(gtip.eenheidxD*(gtip.xAsLog?Math.log10(gtip.domeinen[j][1]):gtip.domeinen[j][1])
// 								/gtip.schaalFactorX + gtip.beginx);	
							int xMax2 = (int) Math.round(valueXtoPixels(gtip.domeinen[j][1]));
							xMax = Math.min(xMax, xMax2);
						}
					}
					//hier: waardes schuifparameters invullen! Even een nieuwe expressie maken en die verder gebruiken voor paint.
					Expressie ingevuldeExpressie = gtip.functies[j];
					if(gtip.schuifParameters != null)
					{	for(int i = 0; i < gtip.schuifParameters.length; i++)
						{	SchuifParameter p = gtip.schuifParameters[i];
							ingevuldeExpressie = ingevuldeExpressie.substitueer(p.geefWaarde(), p.geefNaam());
						}
						
					}
					for(int i=xMin; i<xMax ; i++)
					{	double ii = i;
// 						double d0 = (ingevuldeExpressie.substitueer(gtip.xAsLog?Math.pow(10,gtip.schaalFactorX*(-gtip.beginx)/gtip.eenheidxD + gtip.schaalFactorX*ii
//								/gtip.eenheidxD):gtip.schaalFactorX*(-gtip.beginx)/gtip.eenheidxD + gtip.schaalFactorX*ii/gtip.eenheidxD, gtip.grafiekXAsNaam)).geefWaarde();//dd0.doubleValue();
//						double d1 = (ingevuldeExpressie.substitueer(gtip.xAsLog?Math.pow(10,gtip.schaalFactorX*(-gtip.beginx)/gtip.eenheidxD + gtip.schaalFactorX*(ii+1)
//								/gtip.eenheidxD):gtip.schaalFactorX*(-gtip.beginx)/gtip.eenheidxD + gtip.schaalFactorX*(ii+1)/gtip.eenheidxD, gtip.grafiekXAsNaam)).geefWaarde();//dd0.doubleValue();

						double d0 = (ingevuldeExpressie.substitueer(pixelsXtoValue(i), gtip.grafiekXAsNaam)).geefWaarde();//dd0.doubleValue();
						double d1 = (ingevuldeExpressie.substitueer(pixelsXtoValue(i+1), gtip.grafiekXAsNaam)).geefWaarde();//dd0.doubleValue();
						double d0waarde = d0;
						double d1waarde = d1;
						if(Double.isNaN(d1) && !Double.isNaN(d0))
						{	double newD1waarde = d0;
							for(int k = 1; k < 20; k++)
							{	double kd = k;
// 								double dt0 = (ingevuldeExpressie.substitueer(gtip.xAsLog?Math.pow(10,gtip.schaalFactorX*(-gtip.beginx)/gtip.eenheidxD + gtip.schaalFactorX*(ii+kd/20)
//									/gtip.eenheidxD):gtip.schaalFactorX*(-gtip.beginx)/gtip.eenheidxD + gtip.schaalFactorX*(ii+kd/20)/gtip.eenheidxD, gtip.grafiekXAsNaam)).geefWaarde();
								double dt0 = (ingevuldeExpressie.substitueer(pixelsXtoValue(ii+kd/20), gtip.grafiekXAsNaam)).geefWaarde();
								if(Double.isNaN(dt0))
								{	break;
								}
								else
									newD1waarde = dt0;
							}
							d1waarde = newD1waarde;
						}
						if(Double.isNaN(d0) && !Double.isNaN(d1))
						{	double newD0waarde = d1;
							for(int k = 19; k > 0; k--)
							{	double kd = k;
// 								double dt0 = (ingevuldeExpressie.substitueer(gtip.xAsLog?Math.pow(10,gtip.schaalFactorX*(-gtip.beginx)/gtip.eenheidxD + gtip.schaalFactorX*(ii+kd/20)
//									/gtip.eenheidxD):gtip.schaalFactorX*(-gtip.beginx)/gtip.eenheidxD + gtip.schaalFactorX*(ii+kd/20)/gtip.eenheidxD, gtip.grafiekXAsNaam)).geefWaarde();
								double dt0 = (ingevuldeExpressie.substitueer(pixelsXtoValue(ii+kd/20), gtip.grafiekXAsNaam)).geefWaarde();
								if(Double.isNaN(dt0))
								{	break;
								}
								else
									newD0waarde = dt0;
							}
							d0waarde = newD0waarde;
						}
						if(!(Double.isNaN(d0) && Double.isNaN(d1)) && (!gtip.yPositief || d0 >= 0 || d1 >= 0))
						{	int x0 = i;
							int x1 = i+1;
// 							double dy0 = hoogte -(gtip.beginy+gtip.eenheidyD*(gtip.yAsLog?Math.log10(d0waarde):d0waarde)/gtip.schaalFactorY);
//							double dy1 = hoogte -(gtip.beginy+gtip.eenheidyD*(gtip.yAsLog?Math.log10(d1waarde):d1waarde)/gtip.schaalFactorY);
							double dy0 = valueYtoPixels(d0waarde);
							double dy1 = valueYtoPixels(d1waarde);
							if(dy0>1000)dy0 = 1000;
							if(dy0<-1000)dy0 = -1000;
							if(dy1>1000)dy1 = 1000;
							if(dy1<-1000)dy1 = -1000;
							
							if(curve.getCurrentPoint()==null && (!gtip.yPositief || d0>=0))curve.moveTo((float)x0, (float)dy0);
							else if(curve.getCurrentPoint() == null) 
								curve.moveTo((float)x0, hoogte - gtip.beginy);
							if(!gtip.yPositief || d1>=0) curve.lineTo((float)x1, (float)dy1);
							else
								curve.lineTo((float)x1, hoogte - gtip.beginy);
						}
						if(Double.isNaN(d1) || gtip.yPositief && d1<0)
						{	g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL,RenderingHints.VALUE_STROKE_PURE);
							if(gtip.grafiekKleuren)g.setColor(gtip.getTekenColor(j));
							else g.setColor(gtip.getTekenColor(0));
							g.setStroke(new BasicStroke(1.2f));
							if(curve.getCurrentPoint()!=null) g.draw(curve);
							curve = new GeneralPath();
						}
					}
					g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL,RenderingHints.VALUE_STROKE_PURE);
					if(gtip.grafiekKleuren)g.setColor(gtip.getTekenColor(j));
					else g.setColor(gtip.getTekenColor(0));
					g.setStroke(new BasicStroke(1.2f));
					g.draw(curve);
	
					if(gtip.traceOptie)	
					{	g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL,RenderingHints.VALUE_STROKE_NORMALIZE);
						g.setColor(new Color(100,100,100));
						double d = bx+1.0*((gtip.selectnummer+gtip.beginwaarde)*gtip.eenheidx);
						int x = (int) Math.round(d);
// 						double d0 = ingevuldeExpressie.geefWaarde(gtip.xAsLog?Math.pow(10, (gtip.selectnummer+gtip.beginwaarde)*gtip.schaalFactorX):
//							(gtip.selectnummer+gtip.beginwaarde)*gtip.schaalFactorX);
						double d0;
						if (gtip.manualScalingX) {
							d0 = ingevuldeExpressie.geefWaarde(gtip.xAsLog?Math.pow(10, (gtip.selectnummer+gtip.beginwaarde)*gtip.eenheidxValue):
											(gtip.selectnummer+gtip.beginwaarde)*gtip.eenheidxValue);
						} else {
							d0 = ingevuldeExpressie.geefWaarde(gtip.xAsLog?Math.pow(10, (gtip.selectnummer+gtip.beginwaarde)*gtip.schaalFactorX):
								(gtip.selectnummer+gtip.beginwaarde)*gtip.schaalFactorX);
							
						}
						if(!gtip.tracing && !Double.isNaN(d0) && gtip.selectnummer<8 && gtip.selectnummer>-1 &&
								x >= gtip.domeinen[j][0] && x <= gtip.domeinen[j][1]) {	
// 							int y = (int)Math.round(gtip.yAsLog?Math.log10(hoogte -(gtip.beginy+gtip.eenheidy*d0/gtip.schaalFactorY)):
//								hoogte -(gtip.beginy+gtip.eenheidy*d0/gtip.schaalFactorY));
							int y = (int) Math.round(valueYtoPixels(d0));
							g.fillOval(x-2,y-2,5,5);
							g.setStroke(new BasicStroke(1.0f));
							g.drawLine(x,y,x,Math.min(hoogte-by, hoogte));
							g.drawLine(x,y,Math.max(0,bx),y);
							g.setStroke(new BasicStroke(1.0f));
							gtip.tracexD = gtip.xAsLog?Math.log10(d):d;
							gtip.tracex = x;
							gtip.slider.zetStand(gtip.tracex);
							
//  						double dTraceX = gtip.schaalFactorX*(-gtip.beginx)/gtip.eenheidxD + gtip.schaalFactorX*gtip.tracexD/gtip.eenheidxD;
							double dTraceX;
							if (gtip.manualScalingX) {
								dTraceX = gtip.eenheidxValue*(-gtip.beginx)/gtip.eenheidxD + gtip.eenheidxValue*gtip.tracexD/gtip.eenheidxD;
							} else {
								dTraceX = gtip.schaalFactorX*(-gtip.beginx)/gtip.eenheidxD + gtip.schaalFactorX*gtip.tracexD/gtip.eenheidxD;
							}
							
							double dTraceY = ingevuldeExpressie.geefWaarde(dTraceX);
// 							int tracey = (int)Math.round(hoogte -(gtip.beginy+gtip.eenheidy*dTraceY/gtip.schaalFactorY));
							int tracey = (int) Math.round(valueYtoPixels(dTraceY));
							
							String xWaarde = gtip.dfTrace.format(dTraceX);
							String yWaarde = gtip.dfTrace.format(dTraceY);
							g.setFont(gtip.font);
							gtip.fm = g.getFontMetrics();
							int woordBreedteX = gtip.fm.stringWidth(xWaarde);
							int woordHoogteX = gtip.fm.getAscent();
							int woordBreedteY = gtip.fm.stringWidth(yWaarde);
							int woordHoogteY = gtip.fm.getAscent();
							g.setColor(new Color(255,255,200));
							g.fillRect(gtip.tracex-woordBreedteX/2-2, Math.min(hoogte-by, hoogte-woordHoogteX-2), woordBreedteX+4, woordHoogteX+2);
							g.fillRect(Math.max(0,bx-woordBreedteY), tracey-woordHoogteY/2-2, woordBreedteY+4, woordHoogteY+4);
							g.setColor(Color.black);
							g.drawRect(gtip.tracex-woordBreedteX/2-2, Math.min(hoogte-by, hoogte-woordHoogteX-2), woordBreedteX+4, woordHoogteX+2);
							g.drawRect(Math.max(0,bx-woordHoogteY), tracey-woordHoogteY/2-2, woordBreedteY+4, woordHoogteY+4);
							g.drawString(xWaarde, gtip.tracex-woordBreedteX/2, Math.min(hoogte-by+woordHoogteX, hoogte-2));
							g.drawString(yWaarde, Math.max(2,bx-woordBreedteY+2), tracey+woordHoogteY/2);
						}
						else {	
// 							double dTraceX = gtip.xAsLog?Math.pow(10, gtip.schaalFactorX*(-gtip.beginx)/gtip.eenheidxD + gtip.schaalFactorX*gtip.tracexD/gtip.eenheidxD):
//								gtip.schaalFactorX*(-gtip.beginx)/gtip.eenheidxD + gtip.schaalFactorX*gtip.tracexD/gtip.eenheidxD;
							double dTraceX = pixelsXtoValue(gtip.tracexD);
// 							double dTraceY = gtip.yAsLog?Math.log10((gtip.functies[j].substitueer(dTraceX, gtip.grafiekXAsNaam)).geefWaarde()):
//								(ingevuldeExpressie.substitueer(dTraceX, gtip.grafiekXAsNaam)).geefWaarde();
							double dTraceY = gtip.yAsLog?((gtip.functies[j].substitueer(dTraceX, gtip.grafiekXAsNaam)).geefWaarde()):
										(ingevuldeExpressie.substitueer(dTraceX, gtip.grafiekXAsNaam)).geefWaarde();

							if(!Double.isNaN(dTraceY) && gtip.tracex<gtip.veldb && gtip.tracex>-1 && (!gtip.xPositief || gtip.tracex>bx)
									&& dTraceX >= gtip.domeinen[j][0] && dTraceX <= gtip.domeinen[j][1]) {	
// 								int tracey = (int)Math.round(hoogte -(gtip.beginy+gtip.eenheidy*dTraceY/gtip.schaalFactorY));
								int tracey = (int) Math.round(valueYtoPixels(dTraceY));
								g.fillOval(gtip.tracex-2,tracey-2,5,5);
								g.drawLine(gtip.tracex,tracey,gtip.tracex,Math.min(hoogte-by, hoogte));
								g.drawLine(gtip.tracex,tracey,Math.max(bx, 0),tracey);
							
								String xWaarde = gtip.dfTrace.format(dTraceX);
// 								String yWaarde = gtip.dfTrace.format(gtip.yAsLog?Math.pow(10,dTraceY):dTraceY);
								String yWaarde = gtip.dfTrace.format(dTraceY);
								g.setFont(gtip.font);
								gtip.fm = g.getFontMetrics();
								int woordBreedteX = gtip.fm.stringWidth(xWaarde);
								int woordHoogteX = gtip.fm.getAscent();
								int woordBreedteY = gtip.fm.stringWidth(yWaarde);
								int woordHoogteY = gtip.fm.getAscent();
								g.setColor(new Color(255,255,200));
								g.fillRect(gtip.tracex-woordBreedteX/2-2, Math.min(hoogte-by, hoogte-woordHoogteX-2), woordBreedteX+4, woordHoogteX+2);
								g.fillRect(Math.max(0,bx-woordBreedteY-5), tracey-woordHoogteY/2-2, woordBreedteY+4, woordHoogteY+4);
								g.setColor(Color.black);
								g.drawRect(gtip.tracex-woordBreedteX/2-2, Math.min(hoogte-by, hoogte-woordHoogteX-2), woordBreedteX+4, woordHoogteX+2);
								g.drawRect(Math.max(0,bx-woordBreedteY-5), tracey-woordHoogteY/2-2, woordBreedteY+4, woordHoogteY+4);
								g.drawString(xWaarde, gtip.tracex-woordBreedteX/2, Math.min(hoogte-by+woordHoogteX, hoogte-2));
								g.drawString(yWaarde, Math.max(2,bx-woordBreedteY-3), tracey+woordHoogteY/2);
							}
						}
					}
		//		}
			}
		}
		//if(gtip.typeFormuleComponent == 1)
		tekenOngelijkheden(g);
		tekenVerticaleLijnen(g);
		tekenParametrisaties(g);
		
		//Punten en grafieken uit tekenEditor;
		if(gtip.docentGraphPoints != null && gtip.typeOpdracht == GraphToolInteractiePanel.VINDFORMULEBIJPUNTEN)
			tekenGraphPoints(gtip.getActiveIndex(), g, true, witruimteY, maxWoordBreedteY, bx, breedte, hoogte);
		//if (tekenComponentAan || (tabelComponentAan && tabelAlsTekenTool))
		if(gtip.graphPoints != null)
		{	// eerst de punten en verbindingen van de niet actieve grafieken	
			for (int index = 1; index <= gtip.getNumGraphs(); index++)
				if (index != gtip.getActiveIndex())
				{	tekenGraphPoints(index, g, false, witruimteY, maxWoordBreedteY, bx, breedte, hoogte);
				}
			// dan de punten en verbindingen van de actieve grafiek
			tekenGraphPoints(gtip.getActiveIndex(), g, false, witruimteY, maxWoordBreedteY, bx, breedte, hoogte);
		}
		if(gtip.traceOptie)
		{	int sliderLoc = Math.max(getY() - gtip.offset, getY() - gtip.offset + hoogte - by);
			sliderLoc = Math.min(sliderLoc,  getY() + hoogte - gtip.offset);
			gtip.slider.setLocation(0, sliderLoc);
		}
//		tekenVeldFunctie(manScalingMultiplyX, manScalingMultiplyY);
		if ( (gtip.veldFuncties[0][0] != null) && (gtip.veldFuncties[0][1] != null) ) { // TODO - criterium
			// roosterpunten aflopen
			FieldData fieldData = new FieldData(imin, imax, jmin, jmax);
			for(int i=imin ; i<imax ; i++) { // x-as aflopen
				for(int j=jmin ; j<jmax ; j++) { // y-as aflopen
					
					// bepaal roosterpositie = begin van vector / stream
					int vectorStartXScreen = (int) (bx+i*ehxD);
					int vectorStartYScreen = (int) (hoogte-(by+j*ehyD));

					// tekenvector
//					if ((i==0) && (j==0))
//						tekenVector(g, new Point(vectorStartXScreen, vectorStartYScreen), -1, false, true);
//						tekenVector(g, new Point(vectorStartXScreen, vectorStartYScreen), 12, true, true);
//						tekenVector(g, new Point(vectorStartXScreen, vectorStartYScreen), 12, true, true);

					if ((!gtip.roosterGrof) || ((i%2==0) && (j%2==0))) {
//						if ((i==2) && (j==2)) {
//						calculateStream(new Point2D.Double(vectorStartXScreen, vectorStartYScreen), i, j,  fieldData, gtip.veldFuncties[0][0], gtip.veldFuncties[0][1]);
						tekenVector(g, new Point(vectorStartXScreen, vectorStartYScreen), -1, false, true);
//						}

					}
				}
			}
			fieldData.tekenPaden(g);
		}

		
		/*
		if(gtip.schuifParameters != null)
		{
			for (int i = 0; i < schuifParameters.length; i++)
				
		}
		*/
		if (gtip.xAsLog || !gtip.manualScalingX) {
			gtip.eenheidxD = origEenheidxD; // Resetting of the original member in gtip, to be as save as possible
			gtip.eenheidx = origEenheidx; // Resetting of the original member in gtip, to be as save as possible
		}
		if (gtip.yAsLog || !gtip.manualScalingY) {
			gtip.eenheidyD = origEenheidyD; // Resetting of the original member in gtip, to be as save as possible
			gtip.eenheidy = origEenheidy; // Resetting of the original member in gtip, to be as save as possible
		}

	}
	
	public void tekenFunctie( )
	{
		
	}
	
	public void tekenGraphPoints(int index, Graphics g, boolean docent, boolean witruimteY, int maxWoordBreedteY, int bx, int breedte, int hoogte)
	{

		Vector indexPoints = gtip.getPoints(index, docent);
		if(docent)
			g.setColor(gtip.docentColor);
		else if(gtip.grafiekKleuren)
			g.setColor(gtip.getTekenColor(index - 1));
		else
			g.setColor(gtip.getTekenColor(0));
		for (int pCnt = 0; pCnt < indexPoints.size(); pCnt++)
		{	RealPoint rp = (RealPoint) indexPoints.elementAt(pCnt);
			Point pix = gtip.realPointToPixels(rp);
			if(index == gtip.getActiveIndex() && !docent && pix != null)// && tekenComponent.getCursorMode() != tekenComponent.NOCUR)
				g.fillOval(pix.x - GraphToolInteractiePanel.PRAD - 2, pix.y - GraphToolInteractiePanel.PRAD - 2,
						   2 * GraphToolInteractiePanel.PRAD + 3, 2 * GraphToolInteractiePanel.PRAD + 3);
			else if (pix != null)
				g.fillOval(pix.x - GraphToolInteractiePanel.PRAD, pix.y - GraphToolInteractiePanel.PRAD,
						   2 * GraphToolInteractiePanel.PRAD +1, 2 * GraphToolInteractiePanel.PRAD + 1);
		}
		// verbinden met lijnen
		if (gtip.tekenComponent.getConnectMode() == TekenComponent.LINES && indexPoints.size() > 1)
		{	RealPoint rp0 = (RealPoint) indexPoints.elementAt(0);
			Point pix0 = gtip.realPointToPixels(rp0);
			RealPoint rp1 = null;
			Point pix1 = null;
			for (int pCnt = 1; pCnt < indexPoints.size(); pCnt++)
			{	rp1 = (RealPoint) indexPoints.elementAt(pCnt);
				pix1 = gtip.realPointToPixels(rp1);
				if(pix0 != null && pix1 != null)
					g.drawLine(pix0.x, pix0.y, pix1.x, pix1.y);
				rp0 = rp1;
				pix0 = pix1;
			}
		}
		
		if (gtip.tekenComponent.getConnectMode() == TekenComponent.CURVE && indexPoints.size() == 2)
		{	RealPoint rp0 = (RealPoint) indexPoints.elementAt(0);
			RealPoint rp1 = (RealPoint) indexPoints.elementAt(1);
			Point pix0 = gtip.realPointToPixels(rp0);
			Point pix1 = gtip.realPointToPixels(rp1);
			if(pix0 != null && pix1 != null)
				g.drawLine(pix0.x, pix0.y, pix1.x, pix1.y);
		}    
		if (gtip.tekenComponent.getConnectMode() == TekenComponent.CURVE && indexPoints.size() > 2)
		{	Graphics2D g2D = (Graphics2D) g;
			g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			
			boolean puntenVerwijderd = false;
			if(!gtip.xAsLog && !gtip.yAsLog)
			{	indexPoints = sorteerNaarX(indexPoints);
				if(berekenLijn(indexPoints, gtip.tekenGrafiekNauwkeurigheid))
				{	for(int i = indexPoints.size() - 2; i > 0; i--)
						indexPoints.removeElementAt(i);
					puntenVerwijderd = true;
				}
				else if(indexPoints.size() > 3 && berekenParabool(indexPoints, gtip.tekenGrafiekNauwkeurigheid))
				{	int midden = indexPoints.size()/2;
					for(int i = indexPoints.size() - 2; i > 0; i--)
						if(i != midden)
						{	indexPoints.removeElementAt(i);
						}
					puntenVerwijderd = true;
				}
			}
			
			if(puntenVerwijderd)
			{	GeneralPath curve = new GeneralPath();
				double[] weights = berekenGewichten(indexPoints);
				RealPoint beginPunt = (RealPoint) indexPoints.elementAt(0);
				Point beginPuntPix = gtip.realPointToPixels(beginPunt);
				RealPoint eindPunt = (RealPoint) indexPoints.elementAt(indexPoints.size() - 1);
				Point eindPuntPix = gtip.realPointToPixels(eindPunt);
				if(beginPuntPix != null && eindPuntPix != null)
				{	for(int i=Math.max(0, beginPuntPix.x); i < Math.min(breedte, eindPuntPix.x) ; i++) {	
						double ii = i;
// 						double d0 = berekenLagrangeY(indexPoints, gtip.xAsLog?Math.pow(10,gtip.schaalFactorX*(-gtip.beginx)/gtip.eenheidxD + gtip.schaalFactorX*ii
//							/gtip.eenheidxD):gtip.schaalFactorX*(-gtip.beginx)/gtip.eenheidxD + gtip.schaalFactorX*ii/gtip.eenheidxD, weights);
//						double d1 = berekenLagrangeY(indexPoints, gtip.xAsLog?Math.pow(10,gtip.schaalFactorX*(-gtip.beginx)/gtip.eenheidxD + gtip.schaalFactorX*(ii+1)
//								/gtip.eenheidxD):gtip.schaalFactorX*(-gtip.beginx)/gtip.eenheidxD + gtip.schaalFactorX*(ii+1)/gtip.eenheidxD, weights);
						double d0 = berekenLagrangeY(indexPoints, pixelsXtoValue(ii), weights);
						double d1 = berekenLagrangeY(indexPoints, pixelsXtoValue(ii+1), weights);
						int x0 = i;
						int x1 = i+1;
// 						double dy0 = hoogte -(gtip.beginy+gtip.eenheidyD*(gtip.yAsLog?Math.log10(d0):d0)/gtip.schaalFactorY);
//						double dy1 = hoogte -(gtip.beginy+gtip.eenheidyD*(gtip.yAsLog?Math.log10(d1):d1)/htip.schaalFactorY);
						double dy0 = valueYtoPixels(d0);
						double dy1 = valueYtoPixels(d1);
						if(dy0>1000)dy0 = 1000;
						if(dy0<-1000)dy0 = -1000;
						if(dy1>1000)dy1 = 1000;
						if(dy1<-1000)dy1 = -1000;
						
						if(curve.getCurrentPoint()==null)
						{	curve.moveTo((float)x0, (float)dy0);
						}
						if(!gtip.yPositief || d1>0) 
						{	curve.lineTo((float)x1, (float)dy1);						
						}
					}
				}
				g2D.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL,RenderingHints.VALUE_STROKE_PURE);
				g2D.setStroke(new BasicStroke(1.2f));
				g2D.draw(curve);
			}
			else
			{	
				// het punt voor het startpunt p0, if any
				RealPoint p00 = null;
				// startpunt p0
				RealPoint rp0 = (RealPoint) indexPoints.elementAt(0);
				RealPoint p0 = gtip.realPointToRealPixels(rp0);
				// eindpunt p1
				RealPoint rp1 = null;
				RealPoint p1 = null;
				// het punt na het eindpunt p1, if any
				RealPoint rp11 = null;
				RealPoint p11 = null;
		
				double intervalFrac = 3;				
		
				for (int pCnt = 1; pCnt < indexPoints.size(); pCnt++)
				{	// vind eindpunt
					rp1 = (RealPoint) indexPoints.elementAt(pCnt);
					p1 = gtip.realPointToRealPixels(rp1);
					// kijk of p0-p1 in pixels vertikaal is, teken lijn
					if (Math.abs(p0.getX() - p1.getX()) < RealPoint.NZERO)
					{	Point pix0 = gtip.realPointToPixels(rp0);
						Point pix1 = gtip.realPointToPixels(rp1);
						if(pix0 != null && pix1 != null)
							g.drawLine(pix0.x, pix0.y, pix1.x, pix1.y);
					}
					else
					{	// vind het punt na p1, if any
						if (pCnt < (indexPoints.size() - 1))
						{	rp11 = (RealPoint) indexPoints.elementAt(pCnt + 1);
							p11 = gtip.realPointToRealPixels(rp11);
						}
						// vind nu de controle-punten tussen p0 en p1
						// controlepunt 0
						RealPoint c0 = null;
						// p0 is het eerste punt, p1 is het tweede punt
						if (p00 == null)
						{	// vector p0 -> p1
							RealPoint slp0p1 = new RealPoint(
								p1.getX() - p0.getX(), p1.getY() - p0.getY());
							RealPoint unitSlope0 =	slp0p1.standarize();
							double xLength = (p1.getX() - p0.getX()) / intervalFrac;
							double newLength = xLength / unitSlope0.getX();
							RealPoint dir0 = new RealPoint(unitSlope0.getX() * newLength,
								unitSlope0.getY() * newLength);
							c0 = new RealPoint(p0.getX() + dir0.getX(), p0.getY() + dir0.getY());		
						}
						else
						{	// vector p00 -> p0
							RealPoint slp00p0 = new RealPoint(p0.getX() - p00.getX(), p0.getY() - p00.getY());
							// vector p0 -> p1
							RealPoint slp0p1 = new RealPoint(p1.getX() - p0.getX(), p1.getY() - p0.getY());	
	//eerst middelen, dan standariseren of omgekeerd?																
							RealPoint meanSlope0 = new RealPoint((slp00p0.getX() + slp0p1.getX()) / 2,
								(slp00p0.getY() + slp0p1.getY()) / 2);	
							RealPoint unitSlope0 =	meanSlope0.standarize();	
							double xLength = (p1.getX() - p0.getX()) / intervalFrac;
							double newLength = xLength / unitSlope0.getX();
							RealPoint dir0 = new RealPoint(unitSlope0.getX() * newLength,
								unitSlope0.getY() * newLength);
							c0 = new RealPoint(p0.getX() + dir0.getX(), p0.getY() + dir0.getY());		
						}
	
						// controlepunt 1
						RealPoint c1 = null;
						// p1 is het laatste punt
						if (p11 == null)
						{	// vector p0 -> p1
							RealPoint slp0p1 = new RealPoint(p1.getX() - p0.getX(), p1.getY() - p0.getY());	
							RealPoint unitSlope1 =	slp0p1.standarize();	
							double xLength = (p1.getX() - p0.getX()) / intervalFrac;
							double newLength = xLength / unitSlope1.getX();
							RealPoint dir1 = new RealPoint(	- unitSlope1.getX() * newLength,
								- unitSlope1.getY() * newLength);
							c1 = new RealPoint(p1.getX() + dir1.getX(), p1.getY() + dir1.getY());		
						}
						else
						{	// vector p0 -> p1
							RealPoint slp0p1 = new RealPoint(p1.getX() - p0.getX(), p1.getY() - p0.getY());	
							// vector p1 -> p11	
							RealPoint slp1p11 = new RealPoint(p11.getX() - p1.getX(), p11.getY() - p1.getY());	
	//eerst middelen, dan standariseren of omgekeerd?																
							RealPoint meanSlope1 = new RealPoint((slp0p1.getX() + slp1p11.getX()) / 2,
								(slp0p1.getY() + slp1p11.getY()) / 2);
							RealPoint unitSlope1 =	meanSlope1.standarize();	
							double xLength = (p1.getX() - p0.getX()) / intervalFrac;
							double newLength = xLength / unitSlope1.getX();
							RealPoint dir1 = new RealPoint(- unitSlope1.getX() * newLength,
								- unitSlope1.getY() * newLength);
							c1 = new RealPoint(p1.getX() + dir1.getX(), p1.getY() + dir1.getY());		
						}
				
						if(index == gtip.getActiveIndex())
							g2D.setStroke(new BasicStroke(0.7f));
						CubicCurve2D bezier = new CubicCurve2D.Double();
						bezier.setCurve(p0.getX(), p0.getY(), c0.getX(), c0.getY(),
									    c1.getX(), c1.getY(), p1.getX(), p1.getY());
						g2D.draw(bezier);			    
					} // else niet vertikaal
					p00 = p0;
					p0 = p1;
				}
			}
		}
		
		if (gtip.tekenComponent.getConnectMode() == TekenComponent.CURVE_EXTRA && indexPoints.size() == 2)
		{	
			RealPoint rp0 = (RealPoint) indexPoints.elementAt(0);
			RealPoint rp1 = (RealPoint) indexPoints.elementAt(1);
			
			double helling = ((gtip.yAsLog?Math.log10(rp1.getY()):rp1.getY()) - (gtip.yAsLog?Math.log10(rp0.getY()):rp0.getY()))/
					((gtip.xAsLog?Math.log10(rp1.getX()):rp1.getX()) - (gtip.xAsLog?Math.log10(rp0.getX()):rp0.getX()));
// 			double linkerGrens = gtip.xPositief?0:(gtip.schaalFactorX*(-gtip.beginx)/gtip.eenheidxD);
			double linkerGrens;

			if (gtip.manualScalingX) {
				linkerGrens = gtip.xPositief?0:((-gtip.beginx)*gtip.eenheidxValue/gtip.eenheidxD); 				
			}
			else {
				linkerGrens = gtip.xPositief?0:((-gtip.beginx)*gtip.schaalFactorX/gtip.eenheidxD); 								
			}
//			System.out.println("linkerGrens="+linkerGrens);

			double xLinks = gtip.xAsLog?Math.pow(10, linkerGrens):linkerGrens; 
			double yLinks = gtip.yAsLog?Math.pow(10, helling * (linkerGrens - (gtip.xAsLog?Math.log10(rp0.getX()):rp0.getX())) + (gtip.yAsLog?Math.log10(rp0.getY()):rp0.getY())):
				(helling * (linkerGrens - (gtip.xAsLog?Math.log10(rp0.getX()):rp0.getX())) + (gtip.yAsLog?Math.log10(rp0.getY()):rp0.getY()));
			if(yLinks < 0 && gtip.yPositief)
			{	yLinks = 0;
				xLinks = gtip.xAsLog?Math.pow(10, (helling * (gtip.xAsLog?Math.log10(rp0.getX()):rp0.getX()) - (gtip.yAsLog?Math.log10(rp0.getY()):rp0.getY()))/helling): 
					(helling * (gtip.xAsLog?Math.log10(rp0.getX()):rp0.getX()) - (gtip.yAsLog?Math.log10(rp0.getY()):rp0.getY()))/helling;
			}
			
			double ii2 = breedte;
// 		double rechterGrens = gtip.schaalFactorX*(-gtip.beginx)/gtip.eenheidxD + gtip.schaalFactorX*ii2/gtip.eenheidxD; 
			double rechterGrens;
			if (gtip.manualScalingX) {
				rechterGrens = (-gtip.beginx)*gtip.eenheidxValue/gtip.eenheidxD + ii2*gtip.eenheidxValue/gtip.eenheidxD;
			}
			else {
				rechterGrens = (-gtip.beginx)*gtip.schaalFactorX/gtip.eenheidxD + ii2*gtip.schaalFactorX/gtip.eenheidxD;
			}
			double xRechts = gtip.xAsLog?Math.pow(10,rechterGrens):rechterGrens;
			double yRechts = gtip.yAsLog?Math.pow(10, helling * (rechterGrens - (gtip.xAsLog?Math.log10(rp0.getX()):rp0.getX())) + (gtip.yAsLog?Math.log10(rp0.getY()):rp0.getY())):
				(helling * (rechterGrens - (gtip.xAsLog?Math.log10(rp0.getX()):rp0.getX())) + (gtip.yAsLog?Math.log10(rp0.getY()):rp0.getY())); 
			if(yRechts < 0 && gtip.yPositief)
			{	yRechts = 0;
				xRechts = gtip.xAsLog?Math.pow(10, (helling * (gtip.xAsLog?Math.log10(rp0.getX()):rp0.getX()) - (gtip.yAsLog?Math.log10(rp0.getY()):rp0.getY()))/helling): 
					(helling * (gtip.xAsLog?Math.log10(rp0.getX()):rp0.getX()) - (gtip.yAsLog?Math.log10(rp0.getY()):rp0.getY()))/helling;
			}
			
			RealPoint linkerPunt = new RealPoint(xLinks, yLinks);
			RealPoint rechterPunt = new RealPoint(xRechts, yRechts);
			
			Point pix0 = gtip.realPointToPixels(linkerPunt);
			Point pix1 = gtip.realPointToPixels(rechterPunt);
			if(pix0 != null && pix1 != null)
				g.drawLine(pix0.x, pix0.y, pix1.x, pix1.y);
			
		}
		if (gtip.tekenComponent.getConnectMode() == TekenComponent.CURVE_EXTRA && indexPoints.size() > 2)
		{	Graphics2D g2D = (Graphics2D) g;
			g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			
			boolean puntenVerwijderd = false;
			if(!gtip.xAsLog && !gtip.yAsLog)
			{	indexPoints = sorteerNaarX(indexPoints);
				if(berekenLijn(indexPoints, gtip.tekenGrafiekNauwkeurigheid))
				{	for(int i = indexPoints.size() - 2; i > 0; i--)
						indexPoints.removeElementAt(i);
					puntenVerwijderd = true;
				}
				else if(indexPoints.size() > 3 && berekenParabool(indexPoints, gtip.tekenGrafiekNauwkeurigheid))
				{	int midden = indexPoints.size()/2;
					for(int i = indexPoints.size() - 2; i > 0; i--)
						if(i != midden)
						{	indexPoints.removeElementAt(i);
						}
					puntenVerwijderd = true;
				}
			}
			
			if(puntenVerwijderd) {	
				
				GeneralPath curve = new GeneralPath();
				double[] weights = berekenGewichten(indexPoints);
				for(int i=Math.max(witruimteY?maxWoordBreedteY:0, gtip.xPositief?bx:0) ; i<breedte ; i++)
				{	double ii = i;
// 				double d0 = berekenLagrangeY(indexPoints, gtip.xAsLog?Math.pow(10,gtip.schaalFactorX*(-gtip.beginx)/gtip.eenheidxD + gtip.schaalFactorX*ii
//						/gtip.eenheidxD):gtip.schaalFactorX*(-gtip.beginx)/gtip.eenheidxD + gtip.schaalFactorX*ii/gtip.eenheidxD, weights);
//					double d1 = berekenLagrangeY(indexPoints, gtip.xAsLog?Math.pow(10,gtip.schaalFactorX*(-gtip.beginx)/gtip.eenheidxD + gtip.schaalFactorX*(ii+1)
//							/gtip.eenheidxD):gtip.schaalFactorX*(-gtip.beginx)/gtip.eenheidxD + gtip.schaalFactorX*(ii+1)/gtip.eenheidxD, weights);
					double d0 = berekenLagrangeY(indexPoints, pixelsXtoValue(ii), weights);
					double d1 = berekenLagrangeY(indexPoints, pixelsXtoValue(ii+1), weights);

					int x0 = i;
					int x1 = i+1;
					
//					double dy0 = hoogte -(gtip.beginy+gtip.eenheidyD*(gtip.yAsLog?Math.log10(d0):d0)/gtip.schaalFactorY);
//					double dy1 = hoogte -(gtip.beginy+gtip.eenheidyD*(gtip.yAsLog?Math.log10(d1):d1)/gtip.schaalFactorY);
					double dy0 = valueYtoPixels(d0);
					double dy1 = valueYtoPixels(d1);

					if(dy0>1000)dy0 = 1000;
					if(dy0<-1000)dy0 = -1000;
					if(dy1>1000)dy1 = 1000;
					if(dy1<-1000)dy1 = -1000;
					
					if(curve.getCurrentPoint()==null && (!gtip.yPositief || d0>=0))
					{	curve.moveTo((float)x0, (float)dy0);
					}
					else if(curve.getCurrentPoint() == null && d1>=0)
						curve.moveTo((float)x1,  (float)dy1);
					if(!gtip.yPositief || d1>=0) 
					{	curve.lineTo((float)x1, (float)dy1);						
					}
				}
				g2D.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL,RenderingHints.VALUE_STROKE_PURE);
				g2D.setStroke(new BasicStroke(1.2f));
				g2D.draw(curve);
			}
			else
			{	
				// het punt voor het startpunt p0, if any
				RealPoint p00 = null; 
				
				RealPoint hp0 = (RealPoint) indexPoints.elementAt(0);
				RealPoint hp1 = (RealPoint) indexPoints.elementAt(1);
				RealPoint hp2 = (RealPoint) indexPoints.elementAt(2);
				
				//double helling 01 = (hp1.y - hp0.y)/(hp1.x - hp0.x);
				//double helling12 = (hp2.y - hp1.y)/(hp2.x - hp1.x);
				double helling01 = ((gtip.yAsLog?Math.log10(hp1.getY()):hp1.getY()) - (gtip.yAsLog?Math.log10(hp0.getY()):hp0.getY()))/
						((gtip.xAsLog?Math.log10(hp1.getX()):hp1.getX()) - (gtip.xAsLog?Math.log10(hp0.getX()):hp0.getX()));
				double helling12 = ((gtip.yAsLog?Math.log10(hp2.getY()):hp2.getY()) - (gtip.yAsLog?Math.log10(hp1.getY()):hp1.getY()))/
						((gtip.xAsLog?Math.log10(hp2.getX()):hp2.getX()) - (gtip.xAsLog?Math.log10(hp1.getX()):hp1.getX()));
				
				//hier aanpassen voor andere "extrapolatieregel", en iets verderop.
				double helling0 = - helling12/3 + 4 * helling01/3;
				
				
				//double ii = Math.max(witruimteY?maxWoordBreedteY:0, gtip.xPositief?bx:0);
				//double x0 = gtip.xAsLog?Math.pow(10,gtip.schaalFactorX*(-gtip.beginx)/gtip.eenheidxD + gtip.schaalFactorX*ii
				//		/gtip.eenheidxD):gtip.schaalFactorX*(-gtip.beginx)/gtip.eenheidxD + gtip.schaalFactorX*ii/gtip.eenheidxD;
// 				double linkerGrens = gtip.xPositief?0:(gtip.schaalFactorX*(-gtip.beginx)/gtip.eenheidxD);
				double linkerGrens;
				if (gtip.manualScalingX) {
					linkerGrens = gtip.xPositief?0:((-gtip.beginx)*gtip.eenheidxValue/gtip.eenheidxD); 
				}
				else {
					linkerGrens = gtip.xPositief?0:((-gtip.beginx)*gtip.schaalFactorX/gtip.eenheidxD); 
				}
//				System.out.println("linkerGrens 2="+linkerGrens);

				double x0 = gtip.xAsLog?Math.pow(10, linkerGrens):linkerGrens;
				double y0 = gtip.yAsLog?Math.pow(10, helling0 * (linkerGrens - (gtip.xAsLog?Math.log10(hp0.getX()):hp0.getX())) + (gtip.yAsLog?Math.log10(hp0.getY()):hp0.getY())):
					(helling0 * (linkerGrens - (gtip.xAsLog?Math.log10(hp0.getX()):hp0.getX())) + (gtip.yAsLog?Math.log10(hp0.getY()):hp0.getY()));
				if(y0 < 0 && gtip.yPositief)
				{
					y0 = 0;
					x0 = gtip.xAsLog?Math.pow(10, (helling0 * (gtip.xAsLog?Math.log10(hp0.getX()):hp0.getX()) - (gtip.yAsLog?Math.log10(hp0.getY()):hp0.getY()))/helling0): 
						(helling0 * (gtip.xAsLog?Math.log10(hp0.getX()):hp0.getX()) - (gtip.yAsLog?Math.log10(hp0.getY()):hp0.getY()))/helling0;
				}
				
				// startpunt p0
				//RealPoint rp0 = new RealPoint(this.graphToolInteractiePanel.beginx, helling0*(this.graphToolInteractiePanel.beginx - hp0.x) + hp0.y);
				RealPoint rp0 = new RealPoint(x0, y0);
				//RealPoint rp0 = (RealPoint) indexPoints.elementAt(0);
				RealPoint p0 = gtip.realPointToRealPixels(rp0);
				
				RealPoint hpLaatst0 = (RealPoint) indexPoints.elementAt(indexPoints.size() - 1);
				RealPoint hpLaatst1 = (RealPoint) indexPoints.elementAt(indexPoints.size() - 2);
				RealPoint hpLaatst2 = (RealPoint) indexPoints.elementAt(indexPoints.size() - 3);
				double helling10 = ((gtip.yAsLog?Math.log10(hpLaatst0.getY()):hpLaatst0.getY()) - (gtip.yAsLog?Math.log10(hpLaatst1.getY()):hpLaatst1.getY()))/
						((gtip.xAsLog?Math.log10(hpLaatst0.getX()):hpLaatst0.getX()) - (gtip.xAsLog?Math.log10(hpLaatst1.getX()):hpLaatst1.getX())); 
				double helling21 = ((gtip.yAsLog?Math.log10(hpLaatst1.getY()):hpLaatst1.getY()) - (gtip.yAsLog?Math.log10(hpLaatst2.getY()):hpLaatst2.getY()))/
						((gtip.xAsLog?Math.log10(hpLaatst1.getX()):hpLaatst1.getX()) - (gtip.xAsLog?Math.log10(hpLaatst2.getX()):hpLaatst2.getX()));
				
				//hier aanpassen voor andere "extrapolatieregel", en een stukje terug.
				double hellingLaatst = - helling21/3 + 4 * helling10/3;
				
				
				double ii2 = breedte;
// 				double rechterGrens = gtip.schaalFactorX*(-gtip.beginx)/gtip.eenheidxD + gtip.schaalFactorX*ii2/gtip.eenheidxD;
				double rechterGrens;
				if (gtip.manualScalingX) {
					rechterGrens = (-gtip.beginx)*gtip.eenheidxValue/gtip.eenheidxD + ii2*gtip.eenheidxValue/gtip.eenheidxD;					
				}
				else {
					rechterGrens = (-gtip.beginx)*gtip.schaalFactorX/gtip.eenheidxD + ii2*gtip.schaalFactorX/gtip.eenheidxD;
				}					

				double xLaatst = gtip.xAsLog?Math.pow(10,rechterGrens):rechterGrens;
				double yLaatst = gtip.yAsLog?Math.pow(10, hellingLaatst * (rechterGrens - (gtip.xAsLog?Math.log10(hpLaatst0.getX()):hpLaatst0.getX())) + (gtip.yAsLog?Math.log10(hpLaatst0.getY()):hpLaatst0.getY())):
					(hellingLaatst * (rechterGrens - (gtip.xAsLog?Math.log10(hpLaatst0.getX()):hpLaatst0.getX())) + (gtip.yAsLog?Math.log10(hpLaatst0.getY()):hpLaatst0.getY())); 
				if(yLaatst < 0 && gtip.yPositief)
				{	yLaatst = 0;
					xLaatst = gtip.xAsLog?Math.pow(10, (hellingLaatst * (gtip.xAsLog?Math.log10(hpLaatst0.getX()):hpLaatst0.getX()) - (gtip.yAsLog?Math.log10(hpLaatst0.getY()):hpLaatst0.getY()))/hellingLaatst): 
						(hellingLaatst * (gtip.xAsLog?Math.log10(hpLaatst0.getX()):hpLaatst0.getX()) - (gtip.yAsLog?Math.log10(hpLaatst0.getY()):hpLaatst0.getY()))/hellingLaatst;
					
				}
				
				RealPoint eindPunt = new RealPoint(xLaatst, yLaatst);
				//RealPoint eindPunt = gtip.realPointToRealPixels(realEindPunt);
				// eindpunt p1
				RealPoint rp1 = null;
				RealPoint p1 = null;
				// het punt na het eindpunt p1, if any
				RealPoint rp11 = null;
				RealPoint p11 = null;
		
				double intervalFrac = 3;				
		
				for (int pCnt = 0; pCnt < indexPoints.size() + 1; pCnt++) //deze teller al bij 0 laten beginnen. En bij indexPoints.size() + 1 laten eindigen.
				{	// vind eindpunt
					if(pCnt < indexPoints.size())
						rp1 = (RealPoint) indexPoints.elementAt(pCnt);
					else
						rp1 = eindPunt;
					p1 = gtip.realPointToRealPixels(rp1);
					// kijk of p0-p1 in pixels vertikaal is, teken lijn
					if (Math.abs(p0.getX() - p1.getX()) < RealPoint.NZERO)
					{	Point pix0 = gtip.realPointToPixels(rp0);
						Point pix1 = gtip.realPointToPixels(rp1);
						g.drawLine(pix0.x, pix0.y, pix1.x, pix1.y);
					}
					else
					{	// vind het punt na p1, if any
						if (pCnt < (indexPoints.size() - 1)) 
						{	rp11 = (RealPoint) indexPoints.elementAt(pCnt + 1);
							p11 = gtip.realPointToRealPixels(rp11);
						}
						else if (pCnt == indexPoints.size() - 1)
						{	rp11 = eindPunt;
							p11 = gtip.realPointToRealPixels(rp11);
						}
						// vind nu de controle-punten tussen p0 en p1
						// controlepunt 0
						RealPoint c0 = null;
						// p0 is het eerste punt, p1 is het tweede punt
						if (p00 == null)
						{	// vector p0 -> p1
							RealPoint slp0p1 = new RealPoint(
								p1.getX() - p0.getX(), p1.getY() - p0.getY());
							RealPoint unitSlope0 =	slp0p1.standarize();
							double xLength = (p1.getX() - p0.getX()) / intervalFrac;
							double newLength = xLength / unitSlope0.getX();
							RealPoint dir0 = new RealPoint(unitSlope0.getX() * newLength,
								unitSlope0.getY() * newLength);
							c0 = new RealPoint(p0.getX() + dir0.getX(), p0.getY() + dir0.getY());		
						}
						else
						{	// vector p00 -> p0
							RealPoint slp00p0 = new RealPoint(p0.getX() - p00.getX(), p0.getY() - p00.getY());
							// vector p0 -> p1
							RealPoint slp0p1 = new RealPoint(p1.getX() - p0.getX(), p1.getY() - p0.getY());	
	//eerst middelen, dan standariseren of omgekeerd?																
							RealPoint meanSlope0 = new RealPoint((slp00p0.getX() + slp0p1.getX()) / 2,
								(slp00p0.getY() + slp0p1.getY()) / 2);	
							RealPoint unitSlope0 =	meanSlope0.standarize();	
							double xLength = (p1.getX() - p0.getX()) / intervalFrac;
							double newLength = xLength / unitSlope0.getX();
							RealPoint dir0 = new RealPoint(unitSlope0.getX() * newLength,
								unitSlope0.getY() * newLength);
							c0 = new RealPoint(p0.getX() + dir0.getX(), p0.getY() + dir0.getY());		
						}
	
						// controlepunt 1
						RealPoint c1 = null;
						// p1 is het laatste punt
						if (p11 == null)
						{	// vector p0 -> p1
							RealPoint slp0p1 = new RealPoint(p1.getX() - p0.getX(), p1.getY() - p0.getY());	
							RealPoint unitSlope1 =	slp0p1.standarize();	
							double xLength = (p1.getX() - p0.getX()) / intervalFrac;
							double newLength = xLength / unitSlope1.getX();
							RealPoint dir1 = new RealPoint(	- unitSlope1.getX() * newLength,
								- unitSlope1.getY() * newLength);
							c1 = new RealPoint(p1.getX() + dir1.getX(), p1.getY() + dir1.getY());		
						}
						else
						{	// vector p0 -> p1
							RealPoint slp0p1 = new RealPoint(p1.getX() - p0.getX(), p1.getY() - p0.getY());	
							// vector p1 -> p11	
							RealPoint slp1p11 = new RealPoint(p11.getX() - p1.getX(), p11.getY() - p1.getY());	
	//eerst middelen, dan standariseren of omgekeerd?																
							RealPoint meanSlope1 = new RealPoint((slp0p1.getX() + slp1p11.getX()) / 2,
								(slp0p1.getY() + slp1p11.getY()) / 2);
							RealPoint unitSlope1 =	meanSlope1.standarize();	
							double xLength = (p1.getX() - p0.getX()) / intervalFrac;
							double newLength = xLength / unitSlope1.getX();
							RealPoint dir1 = new RealPoint(- unitSlope1.getX() * newLength,
								- unitSlope1.getY() * newLength);
							c1 = new RealPoint(p1.getX() + dir1.getX(), p1.getY() + dir1.getY());		
						}
				
						if(index == gtip.getActiveIndex())
							g2D.setStroke(new BasicStroke(0.7f));
						CubicCurve2D bezier = new CubicCurve2D.Double();
						bezier.setCurve(p0.getX(), p0.getY(), c0.getX(), c0.getY(),
									    c1.getX(), c1.getY(), p1.getX(), p1.getY());
						g2D.draw(bezier);			    
					} // else niet vertikaal
					p00 = p0;
					p0 = p1;
				} 
			}
		}
	}
	public double[] berekenGewichten(Vector points)
	{	double[] weights = new double[points.size()];
		for(int i = 0; i < points.size(); i++)
		{	weights[i] = 1;
			RealPoint rpi = (RealPoint) points.elementAt(i);
			for(int j = 0; j < points.size(); j++)
				if(i != j)
				{	RealPoint rpj = (RealPoint) points.elementAt(j);
					weights[i] *= 1/(rpi.getX() - rpj.getX());
				}
		}		
		return weights;
	}
	
	public double berekenLagrangeY(Vector points, double x, double[] weights)
	{	double teller = 0;
		double noemer = 0;
		double waarde;
		double lagrangeY;
		int ingevuldPunt = -1;
		for(int i = 0; i < points.size(); i++)
		{	RealPoint rpi = (RealPoint) points.elementAt(i);
			waarde = weights[i]/(x - rpi.getX());
			if(Double.isNaN(waarde) || Double.isInfinite(waarde))
			{	ingevuldPunt = i;
				break;
			}
			teller += waarde*rpi.getY();
			noemer += waarde;
		}
		if(noemer == 0)
			noemer = 1;
		if(ingevuldPunt > -1)
		{	RealPoint rpi = (RealPoint) points.elementAt(ingevuldPunt);
			lagrangeY = rpi.getY();
		}
		else
			lagrangeY = teller/noemer;
		return lagrangeY;
	
	}
	
	public Vector sorteerNaarX(Vector points)
	{	Vector gesorteerd = new Vector();
		while(!points.isEmpty())
		{	RealPoint rp0 = (RealPoint) points.elementAt(0);
			double xMin = rp0.getX();
			double xMax = rp0.getX();
			int minIndex = 0;
			int maxIndex = 0;
			for(int i = 1; i < points.size(); i++)
			{	RealPoint rpi = (RealPoint) points.elementAt(i);
				if(rpi.getX() > xMax)
				{	xMax = rpi.getX();
					maxIndex = i;
				}
				if(rpi.getX() < xMin)
				{	xMin = rpi.getX();
					minIndex = i;
				}
			}
			int insertPlek = gesorteerd.size()/2;
			RealPoint rpMin = (RealPoint) points.elementAt(minIndex);
			if(minIndex != maxIndex)
			{	RealPoint rpMax = (RealPoint) points.elementAt(maxIndex);
				points.removeElementAt(maxIndex);
				gesorteerd.insertElementAt(rpMax, insertPlek);
			}
			if(maxIndex < minIndex)
				points.removeElementAt(minIndex - 1);
			else
				points.removeElementAt(minIndex);
			gesorteerd.insertElementAt(rpMin, insertPlek);
		}
		return gesorteerd;
	}
	
	public boolean berekenLijn(Vector points, int nauwkeurigheid)
	{	
		double nauwkeurigDoubleX = gtip.schaalFactorX * nauwkeurigheid / gtip.eenheidxD;
		double nauwkeurigDoubleY = gtip.schaalFactorY * nauwkeurigheid / gtip.eenheidyD;
		
		RealPoint rpMin = (RealPoint) points.elementAt(0);
		RealPoint rpMax = (RealPoint) points.elementAt(points.size() - 1);
		double a = ((gtip.yAsLog?Math.log10(rpMax.getY()):rpMax.getY()) - (gtip.yAsLog?Math.log10(rpMin.getY()):rpMin.getY()))
				/((gtip.xAsLog?Math.log10(rpMax.getX()):rpMax.getX()) - (gtip.xAsLog?Math.log10(rpMin.getX()):rpMin.getX()));
		double b = (gtip.yAsLog?Math.log10(rpMax.getY()):rpMax.getY() - a*(gtip.xAsLog?Math.log10(rpMax.getX()):rpMax.getX()));
		boolean lijn = true;
		
		for(int i = 1; i < points.size() - 1 && lijn; i++)
		{	RealPoint rpi = (RealPoint) points.elementAt(i);
			double xs = (a*(gtip.yAsLog?Math.log10(rpi.getY()):rpi.getY()) + (gtip.xAsLog?Math.log10(rpi.getX()):rpi.getX()) - a*b)/(a*a + 1);
			double ys = (a*a*(gtip.yAsLog?Math.log10(rpi.getY()):rpi.getY()) + a*(gtip.xAsLog?Math.log10(rpi.getX()):rpi.getX()) + b)/(a*a + 1);
//				double afstand = ((gtip.xAsLog?Math.log10(rpi.getX()):rpi.getX()) - xs)*((gtip.xAsLog?Math.log10(rpi.getX()):rpi.getX()) - xs) + 
//						((gtip.yAsLog?Math.log10(rpi.getY()):rpi.getY()) - ys)*((gtip.yAsLog?Math.log10(rpi.getY()):rpi.getY()) - ys);
			boolean afstandxKleinGenoeg = Math.abs((gtip.xAsLog?Math.log10(rpi.getX()):rpi.getX()) - xs) <= nauwkeurigDoubleX;
			boolean afstandyKleinGenoeg = Math.abs((gtip.yAsLog?Math.log10(rpi.getY()):rpi.getY()) - ys) <= nauwkeurigDoubleY; 
			//afstand = Math.sqrt(afstand);
			if(!(afstandxKleinGenoeg && afstandyKleinGenoeg))
				lijn = false;
		}
		return lijn;
	}	
	
	public boolean berekenParabool(Vector points, int nauwkeurigheid)
	{	
		double nauwkeurigDoubleX = gtip.schaalFactorX * nauwkeurigheid / gtip.eenheidxD;
		double nauwkeurigDoubleY = gtip.schaalFactorY * nauwkeurigheid / gtip.eenheidyD;
		
		RealPoint rpMin = (RealPoint) points.elementAt(0);
		RealPoint rpMax = (RealPoint) points.elementAt(points.size() - 1);
		RealPoint rpMiddle = (RealPoint) points.elementAt(points.size()/2);
		double c1 = rpMin.getY()/((rpMin.getX() - rpMax.getX()) * (rpMin.getX() - rpMiddle.getX()));
		double c2 = rpMax.getY()/((rpMax.getX() - rpMiddle.getX()) * (rpMax.getX() - rpMin.getX()));
		double c3 = rpMiddle.getY()/((rpMiddle.getX() - rpMin.getX()) * (rpMiddle.getX() - rpMax.getX()));
		double a = c1 + c2 + c3;
		double b = -c1*rpMax.getX() - c1*rpMiddle.getX() - c2*rpMin.getX() - c2*rpMiddle.getX() - c3*rpMin.getX() - c3*rpMax.getX();
		double c = c1*rpMax.getX()*rpMiddle.getX() + c2*rpMiddle.getX()*rpMin.getX() + c3*rpMin.getX() * rpMax.getX();
		boolean parabool = true;
		
		for(int i = 1; i < points.size() - 1 && parabool; i++)
		{	if(i != points.size()/2)
			{	RealPoint rpi = (RealPoint) points.elementAt(i);
				double deel1 = 2-b*b+4*a*(c-rpi.getY());
				double wortel = Math.pow(a, 6)*(27*(b+2*a*rpi.getX())*(b+2*a*rpi.getX())+Math.pow(deel1, 3));
				wortel = Math.sqrt(3) * Math.sqrt(wortel);
				if(Double.isNaN(wortel))
				{	parabool = false;
					break;
				}
				double deel2 = 9*a*a*a*b + 18*a*a*a*a*rpi.getX() + wortel;
				double nieuweDeel2 = Math.pow(deel2, 1.0/3.0);
				if(Double.isNaN(nieuweDeel2))
					nieuweDeel2 = - Math.pow(- deel2, 1.0/3.0);
				deel2 = nieuweDeel2;
				double xs = (-3*a*b+Math.pow(3, 1.0/3.0)*deel2 - (Math.pow(3, 2.0/3.0)*a*a*deel1)/deel2)/(6*a*a);
				double ys = a*xs*xs + b*xs + c;
				boolean afstandxKleinGenoeg = Math.abs((gtip.xAsLog?Math.log10(rpi.getX()):rpi.getX()) - xs) <= nauwkeurigDoubleX;
				boolean afstandyKleinGenoeg = Math.abs((gtip.yAsLog?Math.log10(rpi.getY()):rpi.getY()) - ys) <= nauwkeurigDoubleY; 
//				
//				
//				
//				double afstand = (rpi.getX() - xs)*(rpi.getX() - xs) + (rpi.getY() - ys)*(rpi.getY() - ys);
//				afstand = Math.sqrt(afstand);
//				if(afstand > nauwkeurigDouble)
				if(!(afstandxKleinGenoeg && afstandyKleinGenoeg))
				{	parabool = false;
					break;
				}
			}
		}
		return parabool;
	}
	
	
	public void tekenOngelijkheden(Graphics gr)
	{	Graphics2D g = (Graphics2D) gr;
		Color[][] ongelijkheidKleuren;
		if(!gtip.yAsNaam.equals(gtip.grafiekYAsNaam))
			return;
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL,RenderingHints.VALUE_STROKE_NORMALIZE);
	
		Area[] areas = new Area[gtip.ongelijkheden.length];
		int breedte = getSize().width;
		int hoogte = getSize().height;
		int bx = (int)Math.round(gtip.beginx);			
		int maxWoordBreedteY = 0;
		boolean witruimteY = false;
		
		for(int j=0 ; j<gtip.ongelijkheden.length ; j++)
		{	if(gtip.ongelijkheden[j]!=null && gtip.isY[j])
			{	GeneralPath curve = new GeneralPath();
				int xMin = Math.max(witruimteY?maxWoordBreedteY:-1, gtip.xPositief?bx:-1);
				int xMax = breedte +1;
				double horizontaleGrens = -1;
				if(!gtip.isGroterGelijk[j])
					horizontaleGrens = gtip.yPositief?(hoogte - gtip.beginy):getHeight()+1;
				
				for(int i=xMin; i<xMax ; i++)
				{	double ii = i;
// 				double d0 = (gtip.ongelijkheden[j].substitueer(gtip.xAsLog?Math.pow(10, gtip.schaalFactorX*(-gtip.beginx)/gtip.eenheidxD + gtip.schaalFactorX*ii/gtip.eenheidxD):
//						gtip.schaalFactorX*(-gtip.beginx)/gtip.eenheidxD + gtip.schaalFactorX*ii/gtip.eenheidxD, gtip.grafiekXAsNaam)).geefWaarde();//dd0.doubleValue();
//					double d1 = (gtip.ongelijkheden[j].substitueer(gtip.xAsLog?Math.pow(10, gtip.schaalFactorX*(-gtip.beginx)/gtip.eenheidxD + gtip.schaalFactorX*(ii+1)/gtip.eenheidxD):
//						gtip.schaalFactorX*(-gtip.beginx)/gtip.eenheidxD + gtip.schaalFactorX*(ii+1)/gtip.eenheidxD, gtip.grafiekXAsNaam)).geefWaarde();//dd0.doubleValue();
					double d0 = (gtip.ongelijkheden[j].substitueer(pixelsXtoValue(ii), gtip.grafiekXAsNaam)).geefWaarde();//dd0.doubleValue();
					double d1 = (gtip.ongelijkheden[j].substitueer(pixelsXtoValue(ii+1), gtip.grafiekXAsNaam)).geefWaarde();//dd0.doubleValue();
					if(!Double.isNaN(d0) && !Double.isNaN(d1))
					{	int x0 = i;
						int x1 = i+1;
// 						double dy0 = hoogte -(gtip.beginy+gtip.eenheidyD*(gtip.yAsLog?Math.log10(d0):d0)/gtip.schaalFactorY);
//						double dy1 = hoogte -(gtip.beginy+gtip.eenheidyD*(gtip.yAsLog?Math.log10(d1):d1)/gtip.schaalFactorY);
						double dy0 = valueYtoPixels(d0);
						double dy1 = valueYtoPixels(d1);
						if(dy0>1000)dy0 = 1000;
						if(dy0<-1000)dy0 = -1000;
						if(dy1>1000)dy1 = 1000;
						if(dy1<-1000)dy1 = -1000;
						
						if(curve.getCurrentPoint()==null)
						{	
							curve.moveTo((float)x0, horizontaleGrens);
							//if(!gtip.yPositief || dy0 >= 0)
								curve.lineTo((float)x0, (float)dy0);
						}
						if(!gtip.yPositief || d1>0) 
							curve.lineTo((float)x1, (float)dy1); 
						else
							curve.lineTo((float)x1, (float)(hoogte - gtip.beginy));
					}
					
					if(Double.isNaN(d1))// || gtip.yPositief && d1<0)
					{	if(curve.getCurrentPoint()!=null)
							curve.lineTo(curve.getCurrentPoint().getX(), horizontaleGrens);
					}
					else if(Double.isNaN(d0))// || gtip.yPositief && d0<0)
						if(curve.getCurrentPoint()!=null)
						{	int x1 = i + 1;
							curve.lineTo((float)x1, horizontaleGrens);
// 							double dy1 = hoogte -(gtip.beginy+gtip.eenheidyD*(gtip.yAsLog?Math.log10(d1):d1)/gtip.schaalFactorY);
							double dy1 = valueYtoPixels(d1);
							if(dy1>1000)dy1 = 1000;
							if(dy1<-1000)dy1 = -1000;
							curve.lineTo((float)x1, (float)dy1);
						}
				}
				
				if(curve.getCurrentPoint() != null)
				{	curve.lineTo(curve.getCurrentPoint().getX(), horizontaleGrens);
					curve.lineTo(xMin, horizontaleGrens);
					
				}
				areas[j] = new Area(curve);
			}
			if(gtip.ongelijkheden[j] != null && !gtip.isY[j])
			{	double grens = gtip.ongelijkheden[j].geefWaarde();
// 				int pixelGrens = (int)((gtip.xAsLog?Math.log10(grens):grens)*gtip.eenheidxD/gtip.schaalFactorX + gtip.beginx);
				int pixelGrens = (int) Math.round(valueXtoPixels(grens));

				if(gtip.isGroterGelijk[j])
				{	Rectangle rechthoek = new Rectangle(pixelGrens, - 1, getWidth() - pixelGrens + 1, getHeight() + 2);
					areas[j] = new Area(rechthoek);
				}
				else
				{	Rectangle rechthoek = new Rectangle(-1, -1, pixelGrens + 1, getHeight() + 2);
					areas[j] = new Area(rechthoek);
				}
			}
		
		}
		
		int aantalAreaClusters = 0;
		boolean inCluster = false;
		
		for(int i = 0; i < areas.length; i++)
		{	if(!inCluster && areas[i] != null && !areas[i].isEmpty())
			{	inCluster = true;
				aantalAreaClusters++;
			}
			else if(inCluster && (areas[i] == null || areas[i].isEmpty()))
				inCluster = false;
		}
		if(aantalAreaClusters == 0)
			return;
		
		int[] clusterLengtes = new int[aantalAreaClusters];
		int clusterNr = -1;
		inCluster = false;
		for(int i = 0; i < areas.length; i++)
		{	if(!inCluster && areas[i] != null && !areas[i].isEmpty())
			{	inCluster = true;
				clusterNr++;
				clusterLengtes[clusterNr] = 1;
			}
			else if(inCluster && (areas[i] == null || areas[i].isEmpty()))
			{	inCluster = false;
			}
			else if(inCluster)
			{	clusterLengtes[clusterNr]++;
			}
		}
		
		
		boolean[] enOf = new boolean[areas.length];
		for(int i = 0; i < enOf.length; i++)
			enOf[i] = gtip.isEn[i];
		
		boolean[][] verwerkEnOf = new boolean[aantalAreaClusters][];
		Area[][] areaClusters = new Area[aantalAreaClusters][];
		ongelijkheidKleuren = new Color[aantalAreaClusters][];
		for(int i = 0; i < aantalAreaClusters; i++)
		{	verwerkEnOf[i] = new boolean[clusterLengtes[i]];
			areaClusters[i] = new Area[clusterLengtes[i]];
			ongelijkheidKleuren[i] = new Color[clusterLengtes[i]];
		}
		
		
		int teller = 0;
		clusterNr = 0;
		for(int i = 0; i < areas.length; i++)
			if(areas[i] != null && !areas[i].isEmpty())
			{	areaClusters[clusterNr][teller] = areas[i];
				if(gtip.grafiekKleuren)
					ongelijkheidKleuren[clusterNr][teller] = gtip.getTekenColor(i);
				else
					ongelijkheidKleuren[clusterNr][teller] = gtip.getTekenColor(0);
				teller++;
				if(teller >= areaClusters[clusterNr].length)
				{	clusterNr++;
					teller = 0;
				}
			}
			else
			{	enOf[i] = false;
				if(i>0)
					enOf[i-1] = false;
			}
		teller = 0;
		clusterNr = 0;
		for(int i = 0; i < enOf.length; i++)
			if(areas[i] != null && !areas[i].isEmpty())
			{	verwerkEnOf[clusterNr][teller] = enOf[i];
				teller++;
				if(teller >= areaClusters[clusterNr].length)
				{	clusterNr++;
					teller = 0;
				}
			}
		
		for(int j = 0; j < areaClusters.length; j++)
		{
			while(erIsNogEn(verwerkEnOf[j]))
			{	for(int i = 0; i < areaClusters[j].length; i++)
				{	if(verwerkEnOf[j][i])
					{	areaClusters[j][i].intersect(areaClusters[j][i+1]);
						Area[] areas2 = new Area[areaClusters[j].length - 1];
						boolean[] verwerkEnOf2 = new boolean[verwerkEnOf[j].length - 1];
						for(int k = 0; k < i; k++)
						{	areas2[k] = areaClusters[j][k];
							verwerkEnOf2[k] = verwerkEnOf[j][k];//dit moet wel false zijn.
						}
						areas2[i] = areaClusters[j][i];
						verwerkEnOf2[i] = verwerkEnOf[j][i+1];
						for(int k = i + 1; k < areas2.length; k++)
						{	areas2[k] = areaClusters[j][k+1];
							verwerkEnOf2[k] = verwerkEnOf[j][k+1];
						}
						areaClusters[j] = new Area[areas2.length];
						verwerkEnOf[j] = new boolean[verwerkEnOf2.length];
						for(int k = 0; k < areaClusters[j].length; k++)
							areaClusters[j][k] = areas2[k];
						for(int k = 0; k < verwerkEnOf2.length; k++)
							verwerkEnOf[j][k] = verwerkEnOf2[k];
						break;
					}
				}
			}
			
			g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL,RenderingHints.VALUE_STROKE_PURE);
			g.setStroke(new BasicStroke(1.2f));
			
			
			for(int i = areaClusters[j].length - 1; i > 0; i--)
			{	Area doorsnede = (Area) areaClusters[j][i].clone();
				doorsnede.intersect(areaClusters[j][i-1]);
				if(!doorsnede.intersects(0, 0, getWidth(), getHeight())) 
				{	g.setColor(new Color(ongelijkheidKleuren[j][i].getRed(), ongelijkheidKleuren[j][i].getGreen(), ongelijkheidKleuren[j][i].getBlue(), 128));//door
					g.fill(areaClusters[j][i]);
				}
				else
				{	areaClusters[j][i-1].add(areaClusters[j][i]);
				}
				
			}
			g.setColor(new Color(ongelijkheidKleuren[j][0].getRed(), ongelijkheidKleuren[j][0].getGreen(), ongelijkheidKleuren[j][0].getBlue(), 128));
			g.fill(areaClusters[j][0]);
		}
	}
	
	public boolean erIsNogEn(boolean[] enOfReeks)
	{
		for(int i = 0; i < enOfReeks.length - 1; i++)
			if(enOfReeks[i])
				return true;
		return false;
	}
	
	public void tekenVerticaleLijnen(Graphics gr)
	{	Graphics2D g = (Graphics2D) gr;
		for(int j=0 ; j<gtip.verticaleLijnen.length ; j++)
		{	if(gtip.verticaleLijnen[j]!=null)
			{	double xWaarde = gtip.verticaleLijnen[j].geefWaarde();
// 				double xWaardePixels = gtip.beginx + gtip.eenheidxD*(gtip.xAsLog?Math.log10(xWaarde):xWaarde)/gtip.schaalFactorX;
				double xWaardePixels = valueXtoPixels(xWaarde);

				GeneralPath curve = new GeneralPath();
				curve.moveTo(xWaardePixels,  0);
				curve.lineTo(xWaardePixels, gtip.yPositief?(getHeight() - gtip.beginy):getHeight());
				g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL,RenderingHints.VALUE_STROKE_PURE);
				g.setStroke(new BasicStroke(1.2f));
				g.setColor(gtip.getTekenColor(j));
				g.draw(curve);
			}
		}
	}
	
	public void tekenParametrisaties(Graphics gr)
	{	Graphics2D g = (Graphics2D) gr;
		int hoogte = getSize().height;
		for(int j = 0; j <gtip.parametrisaties.length; j++)
		{	if(gtip.parametrisaties[j] != null && gtip.parametrisaties[j][0] != null && gtip.parametrisaties[j][1] != null)
			{	String variabele = gtip.parametrisatieVariabelen[j];
				int schuifParameterIndex = -1;
				if(gtip.schuifParameters != null)
				{	for(int i = 0; i < gtip.schuifParameters.length; i++)
					{	if(variabele.equals(gtip.schuifParameters[i].geefNaam()))
						{	schuifParameterIndex = i;
							break;
						}
					}
				}
				if(schuifParameterIndex == -1)
					return;
				
				SchuifParameter p = gtip.schuifParameters[schuifParameterIndex];
				GeneralPath curve = new GeneralPath();
				for(double d = p.geefOnderGrens(); d < p.geefBovenGrens(); d += p.geefStapGrootte())
				{	//double ii = i;
					
					//parametrisaties[j][0] heeft op elk moment de waarde met de huidige stand van de parameter ingevuld...!
					double x0 = (gtip.parametrisaties[j][0].substitueer(d, variabele)).geefWaarde();
					double x1 = (gtip.parametrisaties[j][0].substitueer(d + p.geefStapGrootte(), variabele)).geefWaarde();
					double y0 = (gtip.parametrisaties[j][1].substitueer(d, variabele)).geefWaarde();
					double y1 = (gtip.parametrisaties[j][1].substitueer(d + p.geefStapGrootte(), variabele)).geefWaarde();
					
					double x0Waarde = x0;
					double x1Waarde = x1;
					double y0Waarde = y0;
					double y1Waarde = y1;
					if(Double.isNaN(x1) && !Double.isNaN(x0))
					{	double newX1waarde = x0;
						for(int k = 1; k < 20; k++)
						{	double kd = k;
							double dt0 = (gtip.parametrisaties[j][0].substitueer(d + p.geefStapGrootte()*kd/20, variabele)).geefWaarde();
							//double dt0 = (gtip.functies[j].substitueer(gtip.xAsLog?Math.pow(10,gtip.schaalFactorX*(-gtip.beginx)/gtip.eenheidxD + gtip.schaalFactorX*(ii+kd/20)
							//	/gtip.eenheidxD):gtip.schaalFactorX*(-gtip.beginx)/gtip.eenheidxD + gtip.schaalFactorX*(ii+kd/20)/gtip.eenheidxD, gtip.grafiekXAsNaam)).geefWaarde();
							if(Double.isNaN(dt0))
							{	break;
							}
							else
								newX1waarde = dt0;
						}
						x1Waarde = newX1waarde;
					}
					if(Double.isNaN(x0) && !Double.isNaN(x1))
					{	double newX0waarde = x1;
						for(int k = 19; k > 0; k--)
						{	double kd = k;
							double dt0 = (gtip.parametrisaties[j][0].substitueer(d + p.geefStapGrootte()*kd/20, variabele)).geefWaarde();
						
							//double dt0 = (gtip.functies[j].substitueer(gtip.xAsLog?Math.pow(10,gtip.schaalFactorX*(-gtip.beginx)/gtip.eenheidxD + gtip.schaalFactorX*(ii+kd/20)
							//	/gtip.eenheidxD):gtip.schaalFactorX*(-gtip.beginx)/gtip.eenheidxD + gtip.schaalFactorX*(ii+kd/20)/gtip.eenheidxD, gtip.grafiekXAsNaam)).geefWaarde();
							if(Double.isNaN(dt0))
							{	break;
							}
							else
								newX0waarde = dt0;
						}
						x0Waarde = newX0waarde;
					}
					
					if(Double.isNaN(y1) && !Double.isNaN(y0))
					{	double newY1waarde = y0;
						for(int k = 1; k < 20; k++)
						{	double kd = k;
							double dt0 = (gtip.parametrisaties[j][1].substitueer(d + p.geefStapGrootte()*kd/20, variabele)).geefWaarde();
							//double dt0 = (gtip.functies[j].substitueer(gtip.xAsLog?Math.pow(10,gtip.schaalFactorX*(-gtip.beginx)/gtip.eenheidxD + gtip.schaalFactorX*(ii+kd/20)
							//	/gtip.eenheidxD):gtip.schaalFactorX*(-gtip.beginx)/gtip.eenheidxD + gtip.schaalFactorX*(ii+kd/20)/gtip.eenheidxD, gtip.grafiekXAsNaam)).geefWaarde();
							if(Double.isNaN(dt0))
							{	break;
							}
							else
								newY1waarde = dt0;
						}
						y1Waarde = newY1waarde;
					}
					if(Double.isNaN(y0) && !Double.isNaN(y1))
					{	double newY0waarde = y1;
						for(int k = 19; k > 0; k--)
						{	double kd = k;
							double dt0 = (gtip.parametrisaties[j][1].substitueer(d + p.geefStapGrootte()*kd/20, variabele)).geefWaarde();
						
							//double dt0 = (gtip.functies[j].substitueer(gtip.xAsLog?Math.pow(10,gtip.schaalFactorX*(-gtip.beginx)/gtip.eenheidxD + gtip.schaalFactorX*(ii+kd/20)
							//	/gtip.eenheidxD):gtip.schaalFactorX*(-gtip.beginx)/gtip.eenheidxD + gtip.schaalFactorX*(ii+kd/20)/gtip.eenheidxD, gtip.grafiekXAsNaam)).geefWaarde();
							if(Double.isNaN(dt0))
							{	break;
							}
							else
								newY0waarde = dt0;
						}
						y0Waarde = newY0waarde;
					}
					
					//nu moet ik allerlei gevallen gaan onderscheiden; dat kunnen er nogal wat worden. Hieronder gebeurt het alleen voor y, omdat voor x
					//al is gezorgd dat het altijd een getal is en dat er alleen positieve x kunnen zijn als x positief moet zijn.
					
					//nu gaan doen: omrekenen naar pixels. 
					
					//onderstaande test: checken of minstens een van de twee punten getekend moet worden. Misschien kan ik ze beter uit elkaar trekken...
					
					if((!(Double.isNaN(x0) && Double.isNaN(y0)) && (!gtip.xPositief || x0 >= 0) && (!gtip.yPositief || y0 >= 0))
							|| (!(Double.isNaN(x1) && Double.isNaN(y1)) && (!gtip.xPositief || x1 >= 0) && (!gtip.yPositief || y1 >= 0))) {	
//						double dx0 = gtip.beginx + gtip.eenheidxD * (gtip.xAsLog?Math.log10(x0Waarde):x0Waarde)/gtip.schaalFactorX;
//						double dx1 = gtip.beginx + gtip.eenheidxD * (gtip.xAsLog?Math.log10(x1Waarde):x1Waarde)/gtip.schaalFactorX;
//						double dy0 = hoogte -(gtip.beginy+gtip.eenheidyD*(gtip.yAsLog?Math.log10(y0Waarde):y0Waarde)/gtip.schaalFactorY);
//						double dy1 = hoogte -(gtip.beginy+gtip.eenheidyD*(gtip.yAsLog?Math.log10(y1Waarde):y1Waarde)/gtip.schaalFactorY);
						double dx0 = valueXtoPixels(x0Waarde);
						double dx1 = valueXtoPixels(x1Waarde);
						double dy0 = valueYtoPixels(y0Waarde);
						double dy1 = valueYtoPixels(y1Waarde);
						
						if(dx0>1000)dx0 = 1000;
						if(dx0<-1000)dx0 = -1000;
						if(dx1>1000)dx1 = 1000;
						if(dx1<-1000)dx1 = -1000;
						
						if(dy0>1000)dy0 = 1000;
						if(dy0<-1000)dy0 = -1000;
						if(dy1>1000)dy1 = 1000;
						if(dy1<-1000)dy1 = -1000;
						
						if(curve.getCurrentPoint()==null && (!gtip.xPositief || dx0 >= 0) && (!gtip.yPositief || dy0>=0))
							curve.moveTo((float)dx0, (float)dy0);
						else if(curve.getCurrentPoint() == null && (!gtip.xPositief || dx0 >= 0)) 
							curve.moveTo((float)dx0, hoogte - gtip.beginy);
						else if(curve.getCurrentPoint() == null && (!gtip.yPositief || dy0 >= 0))
							curve.moveTo(gtip.beginx, (float) dy0);
						else if(curve.getCurrentPoint() == null)
							curve.moveTo(gtip.beginx, hoogte - gtip.beginy);
						if((!gtip.xPositief || dx1 >= 0) && (!gtip.yPositief || dy1>=0)) 
							curve.lineTo((float)dx1, (float)dy1);
						else if(!gtip.xPositief || dx1 >= 0) 
							curve.lineTo((float)dx1, hoogte - gtip.beginy);
						else if(!gtip.yPositief || dy1 >= 0)
							curve.lineTo(gtip.beginx, (float) dy1);
						else if(!Double.isNaN(dx1) && !Double.isNaN(dy1))
							curve.lineTo(gtip.beginx, hoogte - gtip.beginy);
					}
					if(Double.isNaN(x1)  || gtip.xPositief && x1 < 0 || Double.isNaN(y1) || gtip.yPositief && y1 < 0)
					{	g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL,RenderingHints.VALUE_STROKE_PURE);
						if(gtip.grafiekKleuren)g.setColor(gtip.getTekenColor(j));
						else g.setColor(gtip.getTekenColor(0));
						g.setStroke(new BasicStroke(1.2f));
						if(curve.getCurrentPoint()!=null) 
							g.draw(curve);
						curve = new GeneralPath();
					}
				}
				g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL,RenderingHints.VALUE_STROKE_PURE);
				if(gtip.grafiekKleuren)g.setColor(gtip.getTekenColor(j*2));
				else g.setColor(gtip.getTekenColor(0));
				g.setStroke(new BasicStroke(1.2f));
				g.draw(curve);
			
				double currentX = (gtip.parametrisaties[j][0].substitueer(p.geefWaarde(), variabele)).geefWaarde();
				double currentY = (gtip.parametrisaties[j][1].substitueer(p.geefWaarde(), variabele)).geefWaarde();
// 				double xPix = gtip.beginx + gtip.eenheidxD * (gtip.xAsLog?Math.log10(currentX):currentX)/gtip.schaalFactorX;
//				double yPix = hoogte -(gtip.beginy+gtip.eenheidyD*(gtip.yAsLog?Math.log10(currentY):currentY)/gtip.schaalFactorY);
				double xPix = valueXtoPixels(currentX);
				double yPix = valueYtoPixels(currentX);							
				
				if(!Double.isNaN(currentX) && !Double.isNaN(currentY))
				{
					g.fillOval((int) xPix-2,(int) yPix-2,5,5);
					
				}
				
			
			}
		
		
		
		//Hier: stuk uit tekenen functies kopieren om trace te laten werken. En dat moet dus de trace van de parametrisatie worden,
		//dus die gelijk loopt met de waarde van de parameter.
		//wat ik moet doen is een (rode) cirkel tekenen die overeenkomt met de huidige waarde van t.
			
		}
	}
	
	//om de schaalverdeling bij logschalen in orde te krijgen; 
	//deze methode werkt alleen goed bij strings die volledig uit getallen bestaan.
	public String toSuperScript(String s)
	{	s = s.replaceAll("0", "\u2070");
		s = s.replaceAll("1", "\u00B9");
		s = s.replaceAll("2", "\u00B2");
		s = s.replaceAll("3", "\u00B3");
		s = s.replaceAll("4", "\u2074");
		s = s.replaceAll("5", "\u2075");
		s = s.replaceAll("6", "\u2076");
		s = s.replaceAll("7", "\u2077");
		s = s.replaceAll("8", "\u2078");
		s = s.replaceAll("9", "\u2079"); 
		s = s.replaceAll("-", "\u207B");
		return s;
	}
	
	// local class to store & draw field plots
	private class FieldData {
		private int itXMin, itXMax, itXLength;
		private int itYMin, itYMax, itYLength;
		private ArrayList<List<GeneralPath>> Data;
		
		FieldData (int iMin, int iMax, int jMin, int jMax){
			itXMin = iMin; 	itXMax = iMax; 
			itYMin = jMin; 	itYMax = jMax; 
			itXLength = itXMax - itXMin ;
			itYLength = itYMax - itYMin ;
			
			// Initialize data structure
			Data = new ArrayList<List<GeneralPath>>();
			for(int i = 0; i < itXLength; i++)  {
				ArrayList<GeneralPath> DataI = new ArrayList<GeneralPath>();
				for (int j=0; j < itYLength; j++) {
					DataI.add(new GeneralPath());
				}
				Data.add(DataI);
			}
		}

		void startPad(int xIndex, int yIndex, Point2D.Double p) {
			if ((xIndex>=itXMin) && (xIndex<itXMax) && (yIndex>=itYMin) && (yIndex<itYMax)) {
				GeneralPath pad = new GeneralPath();
				pad.moveTo(p.getX(), p.getY());
				Data.get(xIndex-itXMin).set(yIndex-itYMin, pad);
			}
		}
			
		void verlengPad(int xIndex, int yIndex, Point2D.Double p) {
			if ((xIndex>=itXMin) && (xIndex<itXMax) && (yIndex>=itYMin) && (yIndex<itYMax)) {
				GeneralPath pad = Data.get(xIndex-itXMin).get(yIndex-itYMin);
				pad.lineTo(p.getX(), p.getY());		
			}
		}
			
		void tekenPaden(Graphics2D g) {
			for (int i=0; i<itXLength; i++) {
				for (int j=0; j<itYLength; j++) {
					GeneralPath pad = Data.get(i).get(j);
					if (pad!=null)
						g.draw(pad);
				}
			}			
		}
		
	}
	
}