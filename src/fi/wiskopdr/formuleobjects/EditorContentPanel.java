package fi.wiskopdr.formuleobjects;

import java.awt.*;

import javax.swing.*;


import fi.wiskopdr.AntwoordVergelijkingVak;
import fi.wiskopdr.WiskOpdr;
import fi.wiskopdr.AntwoordFormuleVak;
import fi.wiskopdr.stelselsvergelijkingen.StelselEditor;
import fi.wiskopdr.tekstobjects.TekstEditor;

public class EditorContentPanel extends JPanel //implements Scrollable
{	
	private int[] yGemiddeldeFormuleVakken = new int[20];
	private int[] hoogteFormuleVakken = new int[20];
	private int formVakTeller = 0;
	private FormuleEditor formuleEditor;
	private TekstEditor tekstEditor;
	boolean grafiekOfEdit;
	boolean hasPrefix;
	
	public EditorContentPanel(FormuleEditor formuleEditor)
	{	this.formuleEditor = formuleEditor;
	}
	
	public EditorContentPanel(TekstEditor tekstEditor)
	{	this.tekstEditor = tekstEditor;
	}
		public void zetMaat()
    {
		int maxX = 0; 
        int maxY = 0; 
        for(int i=0 ; i<getComponentCount() ; i++)
        {   Component c = getComponent(i);
            int b = c.getLocation().x + c.getSize().width;
            if(b>maxX) maxX = b;
            int h = c.getLocation().y + c.getSize().height + 20;
            if(h>maxY) maxY = h;
            
        }
        //System.out.println("kijk");
        setPreferredSize(new Dimension(maxX,maxY));
        if(formuleEditor!=null && !(formuleEditor instanceof StelselEditor))
        	scrollRectToVisible(new Rectangle(maxX-10,maxY-10, maxX, maxY));
        revalidate();
        doLayout();   
    }
	
	public void zetGrafiekOfEdit(boolean b)
	{		grafiekOfEdit=b;
	}
	public void paintComponent(Graphics g)
	{
		if(("MW".equals(WiskOpdr.deployVariant) || "GR".equals(WiskOpdr.deployVariant)) && !grafiekOfEdit)
		{
			super.paintComponent(g);
			int y = 0;
			g.setColor(new Color(240,240,240));
			g.fillRect(0,0,getWidth(),getHeight());
			int stapH = 10;
			if(formVakTeller>1)stapH = yGemiddeldeFormuleVakken[1]-yGemiddeldeFormuleVakken[0]-hoogteFormuleVakken[1]/2 - hoogteFormuleVakken[0]/2;
			for(int i=0 ; i<formVakTeller ; i++)
	        { 	int yNieuw = yGemiddeldeFormuleVakken[i]+hoogteFormuleVakken[i]/2 + stapH/2;
				if(i==formVakTeller-1)
	        	{	g.setColor(Color.white);
	        		g.fillRect(0,y+1,getWidth(),yNieuw-y-1);
	        	}
				y = yNieuw;
	        	
				g.setColor(new Color(210,210,210));
				g.drawLine(0,y,getWidth(), y);
	        }
			for(int i=0 ; i<10 ; i++)
	        {	y = y+30;
	        	g.drawLine(0,y,getWidth(), y);
	        }
			g.setColor(Color.red);
			if("GR".equals(WiskOpdr.deployVariant)) g.drawLine(20,0,20, getHeight());
			if("MW".equals(WiskOpdr.deployVariant)) g.drawLine(33,0,33, getHeight());
			
		}
		/*else if(formuleEditor instanceof AntwoordVergelijkingVak || formuleEditor instanceof AntwoordFormuleVak)
		{
			super.paintComponent(g);
			int y = 0;
			g.setColor(new Color(240,240,240));
			g.fillRect(0,0,getWidth(),getHeight());
			int stapH = 10;
			if(formVakTeller>1)stapH = yGemiddeldeFormuleVakken[1]-yGemiddeldeFormuleVakken[0]-hoogteFormuleVakken[1]/2 - hoogteFormuleVakken[0]/2;
			for(int i=0 ; i<formVakTeller ; i++)
	        { 	int yNieuw = yGemiddeldeFormuleVakken[i]+hoogteFormuleVakken[i]/2 + stapH/2;
				if(i==formVakTeller-1)
	        	{	g.setColor(new Color(255,255,255));
	        		g.fillRect(0,y+1,getWidth(),yNieuw-y-1);
	        	}
				y = yNieuw;
	        	
				g.setColor(Color.white);//new Color(210,210,210));
				g.drawLine(0,y,getWidth(), y);
				g.drawLine(0,y+1,getWidth(), y+1);
	        }
			for(int i=0 ; i<10 ; i++)
	        {	y = y+30;
	        	g.drawLine(0,y,getWidth(), y);
	        	g.drawLine(0,y+1,getWidth(), y+1);
	        }
			
			
		}*/
		else if(shadow && getComponent(0)!=null)
		{super.paintComponent(g);
		  drawShadow(g,getComponent(0).getBounds());
		}
		else super.paintComponent(g);
	}
	
	boolean shadow=false;
	public void setShadow(boolean b)
	{
	  shadow=b;
	}
	
	private void drawShadow(Graphics g, Rectangle r) {
      for(int i=0 ; i<10 ; i++) {
          g.setColor( new Color(150+5*i,150+5*i,150+10*i,255-25*i));
          g.drawRect(r.x-1*i, r.y-1*i, r.width+2*i, r.height+2*i);
      }
    }

	public Dimension getPreferredScrollableViewportSize() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void doLayout()
	{
		formVakTeller = 0;
		int vorigeY = 0;
		int thisY = 0;
		
		for(int i=0 ; i<getComponentCount() ; i++)
        {   Component c = getComponent(i);
			boolean isPrefixVak = c instanceof FormuleVak  
									&& formuleEditor!=null 
									&& formuleEditor instanceof AntwoordFormuleVak  
									&&((AntwoordFormuleVak)formuleEditor).isPrefix((FormuleVak)c);
			if(c instanceof FormuleVak  && !isPrefixVak && formVakTeller<20)
            {	
            	if(i==0)vorigeY = c.getLocation().y;
            	if(hasPrefix && i==1)vorigeY = c.getLocation().y; 
            	thisY = c.getLocation().y;
            	yGemiddeldeFormuleVakken[formVakTeller] = c.getLocation().y + c.getSize().height/2;
            	hoogteFormuleVakken[formVakTeller] = c.getSize().height;
            	formVakTeller++;
            	vorigeY = thisY;
            }
        }
		repaint();
		super.doLayout();
	}

	public int getScrollableBlockIncrement(Rectangle arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		return 0;
	}

	public boolean getScrollableTracksViewportHeight() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean getScrollableTracksViewportWidth() {
		// TODO Auto-generated method stub
		return false;
	}

	public int getScrollableUnitIncrement(Rectangle arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		return 0;
	}
}
