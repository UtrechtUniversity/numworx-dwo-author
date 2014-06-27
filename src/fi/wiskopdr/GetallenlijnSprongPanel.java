package fi.wiskopdr;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Hashtable;

import javax.swing.JPanel;

import fi.beans.wiskopdrbeans.InteractieEditPanel;
import fi.beans.wiskopdrbeans.InteractiePanel;
import fi.wiskopdr.expressies.Expressie;
import fi.wiskopdr.formuleobjects.FormuleParser;


public class GetallenlijnSprongPanel extends JPanel implements InteractiePanel
{
	
	private int minLijn = 0;
	private int maxLijn = 300;
	private int margePerc = 20;
	private int min = (int)Math.round((double)minLijn - (minLijn-maxLijn)*margePerc/100);
	private int max = (int)Math.round(maxLijn + (minLijn-maxLijn)*margePerc/100);
	private double minWaarde = -3;
	private double maxWaarde=4;
	private double minWerkWaarde = -3;
	private double maxWerkWaarde=4;
	private double eenheidWaarde=1;
	double eenheid = (double)(max - min)/(maxWaarde - minWaarde);
	private double posNul = (double)min - (minWaarde*(max - min)/(maxWaarde - minWaarde));
	private boolean nulZichtbaar;
	private boolean eenhedenZichtbaar;
	private boolean pijlOmlaag;
	private boolean pijlZichtbaar = true;
	private boolean tientallenZichtbaar;
	private boolean vijftallenZichtbaar;
	private boolean eenhedenNummers;
	private boolean vijftallenNummers;
	private boolean tientallenNummers;
	
	
	private boolean horizontaal = false;
	private int marge = 10;
	
	private double factor = 0.10;
	static DecimalFormatSymbols dfs;
	public static DecimalFormat df;
	
	private FontMetrics fm;
	private Font font = new Font("SansSerif", Font.PLAIN, 12);
	
	public GetallenlijnSprongPanel() {
		
		dfs = new DecimalFormatSymbols();
		df = new DecimalFormat("0.#####", dfs);
		fm = getFontMetrics(font);
	}
	public void paintComponent(Graphics g)
	{	int d = 4;
		
		marge = berekenMargeGetallen();
		
		setFont(font);
		if(!horizontaal)
		{	g = (Graphics2D)g;
	        ((Graphics2D)g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	        g.setColor(getBackground());
	        g.fillRect(0, 0, getWidth(), getHeight());
	        g.setColor(Color.darkGray);
			g.drawLine(marge,maxLijn,marge,minLijn);
			//g.drawLine(marge+1,maxLijn,marge+1,minLijn);
			g.setColor(Color.darkGray);		
			
			
			for(int i=-1 ; pijlZichtbaar && i<2; i++)
		    {	g.drawLine(marge-d-1,max+i,marge+d+1,max+i);
		    }
			for(int i=-1 ; pijlZichtbaar && i<2; i++)
		    {	g.drawLine(marge-d-1,min+i,marge+d+1,min+i);
		    }
		 
		 	if(nulZichtbaar && posNul>max && posNul<min)
		 	{	for(int i=-1 ; i<2; i++)
			    {	g.drawLine(marge-d,(int)Math.round(posNul)+i,marge+d,(int)Math.round(posNul)+i);
			    }
			}
			double eenheid = (double)(max - min)/(maxWaarde - minWaarde);
			int extraEenheden = (int)Math.round((minLijn-min)/eenheid);
			for(int i = (int)Math.round(minWaarde+extraEenheden) ; i<maxWaarde-extraEenheden+1; i++)
			{	
			 	double sprong = eenheid*(double)i + posNul;
			  	if(eenhedenZichtbaar && sprong<minLijn && sprong>maxLijn)
			  	{	g.drawLine(marge-d/2,(int)Math.round(sprong),marge+d/2+1,(int)Math.round(sprong));
				}
			   	if(vijftallenZichtbaar && i%5==0)
			   	{	g.drawLine(marge-d/2-1,(int)Math.round(sprong),marge+d/2+2,(int)Math.round(sprong));
			   		if(eenhedenZichtbaar)g.drawLine(marge-d/2-1,(int)Math.round(sprong)+1,marge+d/2+2,(int)Math.round(sprong)+1);
			   	}
			   	if(tientallenZichtbaar && i%10==0)
			   	{	//g.drawLine(10-d/2-2,(int)Math.round(sprong)-1,10+d/2+2,(int)Math.round(sprong)-1);
			    	g.drawLine(marge-d/2-2,(int)Math.round(sprong),marge+d/2+3,(int)Math.round(sprong));
			    	if(eenhedenZichtbaar||vijftallenZichtbaar)g.drawLine(marge-d/2-2,(int)Math.round(sprong)+1,marge+d/2+3,(int)Math.round(sprong)+1);
			    }
			   	if(eenhedenNummers || vijftallenNummers && i%5==0 || tientallenNummers && i%10==0)
		   		{	String s = df.format((double)i*eenheidWaarde);
			   		int b = g.getFontMetrics().stringWidth(s);
			   		int h = g.getFontMetrics().getAscent();
			   		//g.drawString(s, marge-d-b-2,(int)Math.round(sprong)+h/2);
			   		if((int)Math.round(sprong)+h/2<getHeight() && (int)Math.round(sprong)-h/2>0)
			   			g.drawString(s, marge-d-b-2,(int)Math.round(sprong)+h/2);
			   	}
		   	}
				
		 	
			g.setColor(Color.darkGray);
			
		    if(pijlZichtbaar)
		    {	Polygon p = new Polygon();
			    if(!pijlOmlaag)
				{   p.addPoint(marge+20,max);
					//p.addPoint(121,max);
				    p.addPoint(marge+20-d,max+2*d);
				    p.addPoint(marge+20+d,max+2*d);
				    
				    g.drawLine(marge+20,min,marge+20,max+2);
				    g.drawLine(marge+21,min,marge+21,max+2);
				}
				else
				{   p.addPoint(marge+20,min);
				    p.addPoint(marge+20-d,min-2*d);
				    p.addPoint(marge+20+d,min-2*d);
				    
				    g.drawLine(marge+20,min-2,marge+20,max);
				    g.drawLine(marge+21,min-2,marge+21,max);
				}
			    g.drawPolygon(p);
			   	g.fillPolygon(p);
		    }
		}
		else
		{	g = (Graphics2D)g;
	        ((Graphics2D)g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	        g.setColor(getBackground());
	        g.fillRect(0, 0, getWidth(), getHeight());
	        g.setColor(Color.black);
			g.drawLine(maxLijn,marge,minLijn,marge);
					
			
			for(int i=0 ; pijlZichtbaar && i<1; i++)
		    {	g.drawLine(max+i,marge-d,max+i,marge+20+d);
		    }
			for(int i=0 ; pijlZichtbaar && i<1; i++)
		    {	g.drawLine(min+i,marge-d,min+i,marge+20+d);
		    }
		 
		 	if(nulZichtbaar && posNul<max && posNul>min)
		 	{	for(int i=-1 ; i<2; i++)
			    {	g.drawLine((int)Math.round(posNul)+i,marge-d,(int)Math.round(posNul)+i,marge+d);
			    }
			}
			double eenheid = (double)(max - min)/(maxWaarde - minWaarde);
			int extraEenheden = (int)((minLijn-min)/eenheid);
			for(int i = (int)minWaarde+extraEenheden ; i<maxWaarde-extraEenheden+1; i++)
			{	
			 	double sprong = eenheid*(double)i + posNul;
			   	if(eenhedenZichtbaar && sprong<maxLijn && sprong>minLijn)
			   	{	g.drawLine((int)Math.round(sprong),marge-d/2,(int)Math.round(sprong),marge+d/2+1);
			   	}
			   	if(vijftallenZichtbaar && i%5==0)
			   	{	g.drawLine((int)Math.round(sprong),marge-d/2-1,(int)Math.round(sprong),marge+d/2+2);
			   		if(eenhedenZichtbaar)g.drawLine((int)Math.round(sprong)+1,marge-d/2-1,(int)Math.round(sprong)+1,marge+d/2+2);
			   	}
			   	if(tientallenZichtbaar && i%10==0)
			   	{	g.drawLine((int)Math.round(sprong),marge-d/2-2,(int)Math.round(sprong),marge+d/2+3);
			   		if(eenhedenZichtbaar||vijftallenZichtbaar)g.drawLine((int)Math.round(sprong)+1,marge-d/2-2,(int)Math.round(sprong)+1,marge+d/2+3);
			   	}
			   	if(eenhedenNummers || vijftallenNummers && i%5==0 || tientallenNummers && i%10==0)
		   		{	String s = df.format((double)i*eenheidWaarde);
			   		int b = g.getFontMetrics().stringWidth(s);
			   		int h = g.getFontMetrics().getAscent();
			   		if((int)Math.round(sprong)+b/2<getWidth() && (int)Math.round(sprong)-b/2>0)
			   			g.drawString(s,(int)Math.round(sprong)-b/2, marge-d-h/2);
		   		}
			}
			
		 	
		    
		    if(pijlZichtbaar)
		    {	Polygon p = new Polygon();
			    if(!pijlOmlaag)
				{  
				    p.addPoint(max,marge+20);
				    p.addPoint(max-2*d,marge+20-d);
				    p.addPoint(max-2*d,marge+20+d);
				    
				    g.drawLine(min,marge+20,max-2,marge+20);
				    g.drawLine(min,marge+21,max-2,marge+21);
				}
				else
				{    p.addPoint(min,marge+20);
					p.addPoint(min+2*d,marge+20-d);
				    p.addPoint(min+2*d,marge+20+d);
				    
				    g.drawLine(min+2,marge+20,max,marge+20);
				    g.drawLine(min+2,marge+21,max,marge+21);
				}
			    g.drawPolygon(p);
			   	g.fillPolygon(p);
		    }
		}
	}
	
	public void setSize(int b, int h) {
		if(!horizontaal)
		{	minLijn = h;
			maxLijn = 0;
		}
		else 
		{	minLijn = 0;
			maxLijn = b;
		}
		min = (int)Math.round((double)minLijn - (minLijn-maxLijn)*margePerc/100);
		max = (int)Math.round(maxLijn + (minLijn-maxLijn)*margePerc/100);
		posNul = min - (minWaarde*(max - min)/(maxWaarde - minWaarde));
		super.setSize(b,h);
	}
	
	
	public void zetEenhedenZichtbaar(boolean b)
	{	eenhedenZichtbaar = b;	
		repaint();
	}
	
	public void zetTientallenZichtbaar(boolean b)
	{	tientallenZichtbaar = b;	
		repaint();
	}
	
	public void zetVijftallenZichtbaar(boolean b)
	{	vijftallenZichtbaar = b;	
		repaint();
	}
	
	private int berekenMargeGetallen()
	{
			int margeGetallen = 0;
			double eenheid = (double)(max - min)/(maxWaarde - minWaarde);
			int extraEenheden = (int)Math.round((minLijn-min)/eenheid);
			for(int i = (int)Math.round(minWaarde+extraEenheden) ; i<maxWaarde-extraEenheden+1; i++)
			{	double sprong = eenheid*(double)i + posNul;
			  	if(eenhedenNummers || vijftallenNummers && i%5==0 || tientallenNummers && i%10==0)
		   		{	String s = df.format((double)i*eenheidWaarde);
			   		int b = fm.stringWidth(s);
			   		margeGetallen = Math.max(margeGetallen,b);
			   		if(horizontaal)
			   		{	margeGetallen = fm.getHeight();
			   			break;
			   		}
			   	}
			  	
		   	}
			return 	margeGetallen+7;
	}
	
	public void zetEenhedenNummers(boolean b)
	{	eenhedenNummers = b;	
		repaint();
	}
	
	public void zetTientallenNummers(boolean b)
	{	tientallenNummers = b;	
		repaint();
	}
	
	public void zetVijftallenNummers(boolean b)
	{	vijftallenNummers = b;	
		repaint();
	}
	
	public void zetNulZichtbaar(boolean b)
	{	nulZichtbaar = b;
		repaint();
	}
	
	public void zetPijlOmlaag(boolean b)
	{	pijlOmlaag = b;	
		repaint();
	}
	
	public void zetHorizontaal(boolean b)
	{	horizontaal = b;	
		if(!horizontaal)
		{	minLijn = 300;
			maxLijn = 0;
		}
		else 
		{	minLijn = 0;
			maxLijn = 300;
		}
		min = (int)Math.round((double)minLijn - (minLijn-maxLijn)*margePerc/100);
		max = (int)Math.round(maxLijn + (minLijn-maxLijn)*margePerc/100);
		posNul = min - (minWaarde*(max - min)/(maxWaarde - minWaarde));
		repaint();
	}
	
	public void zetPijlZichtbaar(boolean b)
	{	pijlZichtbaar = b;	
	
		if(!b)
		{	margePerc = 0;
			min = (int)Math.round((double)minLijn - (minLijn-maxLijn)*margePerc/100);
			max = (int)Math.round(maxLijn + (minLijn-maxLijn)*margePerc/100);
			posNul = (double)min - (minWaarde*(max - min)/(maxWaarde - minWaarde));
		}
		else 
		{	margePerc = 20;
			min = (int)Math.round((double)minLijn - (minLijn-maxLijn)*margePerc/100);
			max = (int)Math.round(maxLijn + (minLijn-maxLijn)*margePerc/100);
			posNul = min - (minWaarde*(max - min)/(maxWaarde - minWaarde));
		}
		repaint();
	}
	
	public void zetEenheidWaarde(double d)
	{	eenheidWaarde = d;
		minWaarde = minWerkWaarde/eenheidWaarde;
		maxWaarde = maxWerkWaarde/eenheidWaarde;
		posNul = (double)min - (minWaarde*(max - min)/(maxWaarde - minWaarde));
		repaint();
	}
	
	public void zetMaxWaarde(double d)
	{	maxWerkWaarde = d;
		maxWaarde = maxWerkWaarde/eenheidWaarde;
		posNul = (double)min - (minWaarde*(max - min)/(maxWaarde - minWaarde));
		repaint();
	}
	
	public void zetMinWaarde(double d)
	{	minWerkWaarde = d;
		minWaarde = minWerkWaarde/eenheidWaarde;
		posNul = (double)min - (minWaarde*(max - min)/(maxWaarde - minWaarde));
		repaint();
	}

	public InteractieEditPanel getEditPanel()
	{	return new GetallenlijnSprongEditPanel();
	}

	public void addActionListener(ActionListener al) {
		// TODO Auto-generated method stub
		
	}

	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	public int geefAsHoogte() {
		// TODO Auto-generated method stub
		return 0;
	}

	public Hashtable getEditState() {
		return null;
	}

	public int getIpId() {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getScore() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	public int[][] getScoreObjectives()
	{	return null;
	}

	public int getScoreMax() {
		// TODO Auto-generated method stub
		return 0;
	}

	public Hashtable getState() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isCorrect() {
		// TODO Auto-generated method stub
		return true;
	}

	public boolean isFout() {
		// TODO Auto-generated method stub
		return false;
	}

	public void kijkNa() {
		// TODO Auto-generated method stub
		
	}

	public void kijkNa(int stapNr) {
		// TODO Auto-generated method stub
		
	}

	public void opnieuw() {
		// TODO Auto-generated method stub
		
	}

	public void setEditState(Hashtable h) {
		boolean eenhedenZichtbaar = false;
		boolean vijftallenZichtbaar = false;
		boolean tientallenZichtbaar = false;
		boolean eenhedenNummers = false;
		boolean vijftallenNummers = false;
		boolean tientallenNummers = false;
		boolean horizontaal = false;
	    boolean nulZichtbaar = true;
	    boolean pijlOmlaag = false;
	    boolean pijlZichtbaar = true;
	    String minWaardeString = "-3";
	    String maxWaardeString = "-4";
	    String eenheidWaardeString = "1";
		
	    if(h.containsKey("eenhedenZichtbaar")) eenhedenZichtbaar = ((Boolean)h.get("eenhedenZichtbaar")).booleanValue();
	    if(h.containsKey("vijftallenZichtbaar")) vijftallenZichtbaar = ((Boolean)h.get("vijftallenZichtbaar")).booleanValue();
	    if(h.containsKey("tientallenZichtbaar")) tientallenZichtbaar = ((Boolean)h.get("tientallenZichtbaar")).booleanValue();
	    if(h.containsKey("eenhedenNummers")) eenhedenNummers = ((Boolean)h.get("eenhedenNummers")).booleanValue();
	    if(h.containsKey("vijftallenNummers")) vijftallenNummers = ((Boolean)h.get("vijftallenNummers")).booleanValue();
	    if(h.containsKey("tientallenNummers")) tientallenNummers = ((Boolean)h.get("tientallenNummers")).booleanValue();
	    if(h.containsKey("horizontaal")) horizontaal = ((Boolean)h.get("horizontaal")).booleanValue();
	    if(h.containsKey("nulZichtbaar")) nulZichtbaar = ((Boolean)h.get("nulZichtbaar")).booleanValue();
	    if(h.containsKey("pijlZichtbaar")) pijlZichtbaar = ((Boolean)h.get("pijlZichtbaar")).booleanValue();
	    if(h.containsKey("pijlOmlaag")) pijlOmlaag = ((Boolean)h.get("pijlOmlaag")).booleanValue();
	    if(h.containsKey("minWaardeString")) minWaardeString = (String)h.get("minWaardeString");
	    if(h.containsKey("maxWaardeString")) maxWaardeString = (String)h.get("maxWaardeString");
	    if(h.containsKey("eenheidWaardeString")) eenheidWaardeString = (String)h.get("eenheidWaardeString");
	    
	    zetEenhedenZichtbaar(eenhedenZichtbaar);
	    zetVijftallenZichtbaar(vijftallenZichtbaar);
	    zetTientallenZichtbaar(tientallenZichtbaar);
	    zetEenhedenNummers(eenhedenNummers);
	    zetVijftallenNummers(vijftallenNummers);
	    zetTientallenNummers(tientallenNummers);
	    zetNulZichtbaar(nulZichtbaar);
	    zetPijlZichtbaar(pijlZichtbaar);
	    zetPijlOmlaag(pijlOmlaag);
	    zetHorizontaal(horizontaal);
	    
	    
		
	    Expressie expressie = FormuleParser.geefExpressie("$f" + minWaardeString + "@");
		if(expressie!=null) zetMinWaarde(expressie.geefWaarde());
		expressie = FormuleParser.geefExpressie("$f" + maxWaardeString + "@");
		if(expressie!=null) zetMaxWaarde(expressie.geefWaarde());
		expressie = FormuleParser.geefExpressie("$f" + eenheidWaardeString + "@");
		if(expressie!=null) zetEenheidWaarde(expressie.geefWaarde());
		
		
		
	}

	public void setState(Hashtable b) {
		// TODO Auto-generated method stub
		
	}

	public void start() {
		// TODO Auto-generated method stub
		
	}

	public void stop() {
		// TODO Auto-generated method stub
		
	}

	public void wis() {
		// TODO Auto-generated method stub
		
	}

	public void zetMaat() {
		// TODO Auto-generated method stub
		
	}

	public void zetMode(int mode) {
		// TODO Auto-generated method stub
		
	}

	public void zetNagekeken(boolean b) {
		// TODO Auto-generated method stub
		
	}

	public void zetOpdracht(Hashtable h, String[] randomVars, Hashtable randomValues) {
		boolean eenhedenZichtbaar = false;
		boolean vijftallenZichtbaar = false;
		boolean tientallenZichtbaar = false;
		boolean eenhedenNummers = false;
		boolean vijftallenNummers = false;
		boolean tientallenNummers = false;
		boolean horizontaal = false;
	    boolean nulZichtbaar = true;
	    boolean pijlOmlaag = false;
	    boolean pijlZichtbaar = true;
	    String minWaardeString = "-3";
	    String maxWaardeString = "-4";
	    String eenheidWaardeString = "1";
		
	    if(h.containsKey("eenhedenZichtbaar")) eenhedenZichtbaar = ((Boolean)h.get("eenhedenZichtbaar")).booleanValue();
	    if(h.containsKey("vijftallenZichtbaar")) vijftallenZichtbaar = ((Boolean)h.get("vijftallenZichtbaar")).booleanValue();
	    if(h.containsKey("tientallenZichtbaar")) tientallenZichtbaar = ((Boolean)h.get("tientallenZichtbaar")).booleanValue();
	    if(h.containsKey("eenhedenNummers")) eenhedenNummers = ((Boolean)h.get("eenhedenNummers")).booleanValue();
	    if(h.containsKey("vijftallenNummers")) vijftallenNummers = ((Boolean)h.get("vijftallenNummers")).booleanValue();
	    if(h.containsKey("tientallenNummers")) tientallenNummers = ((Boolean)h.get("tientallenNummers")).booleanValue();
	    if(h.containsKey("horizontaal")) horizontaal = ((Boolean)h.get("horizontaal")).booleanValue();
	    if(h.containsKey("nulZichtbaar")) nulZichtbaar = ((Boolean)h.get("nulZichtbaar")).booleanValue();
	    if(h.containsKey("pijlZichtbaar")) pijlZichtbaar = ((Boolean)h.get("pijlZichtbaar")).booleanValue();
	    if(h.containsKey("pijlOmlaag")) pijlOmlaag = ((Boolean)h.get("pijlOmlaag")).booleanValue();
	    if(h.containsKey("minWaardeString")) minWaardeString = (String)h.get("minWaardeString");
	    if(h.containsKey("maxWaardeString")) maxWaardeString = (String)h.get("maxWaardeString");
	    if(h.containsKey("eenheidWaardeString")) eenheidWaardeString = (String)h.get("eenheidWaardeString");
	    
	    try{
	    	minWaardeString = FormuleParser.randomizeString("$f" + minWaardeString + "@", randomVars, randomValues);
		}
		catch(Exception e){}	
		try{
			maxWaardeString = FormuleParser.randomizeString("$f" + maxWaardeString + "@", randomVars, randomValues);
		}
		catch(Exception e){}
		try{
			eenheidWaardeString = FormuleParser.randomizeString("$f" + eenheidWaardeString + "@", randomVars, randomValues);
		}
		catch(Exception e){}
		
		System.out.println(minWaardeString);
		
		
	    zetEenhedenZichtbaar(eenhedenZichtbaar);
	    zetVijftallenZichtbaar(vijftallenZichtbaar);
	    zetTientallenZichtbaar(tientallenZichtbaar);
	    zetEenhedenNummers(eenhedenNummers);
	    zetVijftallenNummers(vijftallenNummers);
	    zetTientallenNummers(tientallenNummers);
	    zetNulZichtbaar(nulZichtbaar);
	    zetPijlZichtbaar(pijlZichtbaar);
	    zetPijlOmlaag(pijlOmlaag);
	    zetHorizontaal(horizontaal);
	    
	    
	    Expressie expressie = FormuleParser.geefExpressie(minWaardeString);
		if(expressie!=null) zetMinWaarde(expressie.geefWaarde());
		expressie = FormuleParser.geefExpressie(maxWaardeString);
		if(expressie!=null) zetMaxWaarde(expressie.geefWaarde());
		expressie = FormuleParser.geefExpressie(eenheidWaardeString);
		if(expressie!=null) zetEenheidWaarde(expressie.geefWaarde());
		
		
	}
}
