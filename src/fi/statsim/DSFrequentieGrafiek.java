package fi.statsim;

import java.awt.AWTEventMulticaster;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;

import javax.swing.JPanel;

public class DSFrequentieGrafiek extends JPanel {

	public static int VERSCHIL = 0;
	public static int SOM = 1;
	
	private int somVerschilMode = 1;
			
	private int aantalDS = 2;
	private int aantalKeerGooien = 10;
	private int[] aantalDSGegooid;
	
	private int bottomHeight = 40;
	private int leftMargin = 45;
	private int staafBreedte = 16;
	private int staafTussenruimte = 8;
	private int maxGraphHeight = 100;
	
	
	private String yTekst = "";
	
	private Font labelFont = new Font("SansSerif", Font.PLAIN, 12);
	private Font labelKleinFont = new Font("SansSerif", Font.PLAIN, 10);
		
	public DSFrequentieGrafiek () {
		aantalDSGegooid = new int[19];
	}
	
	public void setSize(int w, int h) {
		maxGraphHeight = h-bottomHeight;
		super.setSize(w, h);
		berekenStaafbreedte();
		repaint();
	}
	
	public void setBounds(int x, int y, int w, int h) {
		maxGraphHeight = h-bottomHeight;
		super.setBounds(x, y, w, h);
		berekenStaafbreedte();
		repaint();
	}
	
	private void berekenStaafbreedte() {
		staafBreedte = (getWidth()-leftMargin)/(2+aantalDS*8);
		staafTussenruimte = staafBreedte/2;
	}
	
	public void reset() {
		for(int i=0 ; i<19 ; i++) {
			aantalDSGegooid[i] = 0;
		}
		repaint();
	}
	
	public void zetYtekst(String yTekst) {
		this.yTekst = yTekst;
	}
	
	public void zetGegooid(int[] aantalDSGegooid)	{
		for(int i=0 ; i<aantalDS*6+1 ; i++) {
			this.aantalDSGegooid[i] = aantalDSGegooid[i];
		}
		repaint();
	}
	
	public void zetAantalDS(int aantalDS) {
		this.aantalDS = aantalDS;
		berekenStaafbreedte();
		reset();
		
	}
	
	public void zetSomVerschil (int mode) {
		if(aantalDS==2 && mode==0)
			somVerschilMode = 0;
		else
			somVerschilMode = 1;
		reset();
	}
	
	public void zetAantalKeerGooien(int aantalKeerGooien) {
		this.aantalKeerGooien = aantalKeerGooien;
		
		reset();
	}
	
	public void paintComponent(Graphics g) {
		g.setColor(Color.WHITE);
		g.fillRect(0,0,getWidth(), getHeight());
		g.setColor(Color.BLACK);
		
		Graphics2D g2 = (Graphics2D)g;
		
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setFont(labelFont);
		FontMetrics fm = g2.getFontMetrics();
		String topLabel = ""+aantalKeerGooien;//(aantalDS);//correctie op schaal voor beter ruimtegebruik grafiek
		String nulLabel = "0";
		int topLabelWidth = fm.stringWidth(topLabel);
		int nulLabelWidth = fm.stringWidth(nulLabel);
		g2.drawString(topLabel, leftMargin-topLabelWidth-5, fm.getAscent()); 
		g2.drawString(nulLabel, leftMargin-nulLabelWidth-5, getHeight()-bottomHeight); 
		        
		g2.setStroke(new BasicStroke(1.5f));
		g2.draw(new Line2D.Double(leftMargin,0,leftMargin,getHeight()-bottomHeight));
		g2.draw(new Line2D.Double(leftMargin,getHeight()-bottomHeight,leftMargin+(5*aantalDS+1)*(staafBreedte+staafTussenruimte)+staafTussenruimte,getHeight()-bottomHeight));
		
		g2.setFont(labelKleinFont);
		fm = g2.getFontMetrics();
		maxGraphHeight = getHeight()-bottomHeight;
		if(aantalDS==2 && somVerschilMode==0)
			for(int i=0 ; i<13 ;i++) {
				int barHeight = aantalDSGegooid[i]*maxGraphHeight/(aantalKeerGooien);///(aantalDS)); //correctie op schaal voor beter ruimtegebruik grafiek
				int rectx = leftMargin+staafTussenruimte+(staafBreedte+staafTussenruimte)*(i);
				int recty = getHeight()-bottomHeight-barHeight;
				
				Rectangle2D rect = new Rectangle2D.Double(rectx,recty,staafBreedte,barHeight);
				g2.setPaint(Color.ORANGE);
			    g2.fill(rect);	
			    g2.setPaint(Color.BLACK);
				g2.draw(new Rectangle2D.Double(rectx,recty,staafBreedte,barHeight));
				
				String ogenLabel = ""+i;
				int ogenLabelWidth = fm.stringWidth(ogenLabel);
				g2.drawString(""+i, rectx+(staafBreedte-ogenLabelWidth)/2, getHeight()-bottomHeight+fm.getAscent()+2);
				
				String barHeightString = ""+aantalDSGegooid[i];
				int barHeightStringWidth = fm.stringWidth(barHeightString);
				//if(aantalDSGegooid[i]>0)
				//	g2.drawString(barHeightString, rectx+(staafBreedte-barHeightStringWidth)/2, getHeight()-bottomHeight-barHeight-3);
			}
		else
			for(int i=aantalDS ; i<aantalDS*6+1 ;i++) {
				//if(aantalDS==0) aantalDS++; // waarom nodig?
				int barHeight = aantalDSGegooid[i]*maxGraphHeight/(aantalKeerGooien);///(aantalDS));//correctie op schaal voor beter ruimtegebruik grafiek
				int rectx = leftMargin+staafTussenruimte+(staafBreedte+staafTussenruimte)*(i-aantalDS);
				int recty = getHeight()-bottomHeight-barHeight;
				
				Rectangle2D rect = new Rectangle2D.Double(rectx,recty,staafBreedte,barHeight);
				g2.setPaint(Color.ORANGE);
			    g2.fill(rect);	
			    g2.setPaint(Color.BLACK);
				g2.draw(new Rectangle2D.Double(rectx,recty,staafBreedte,barHeight));
				
				String ogenLabel = ""+i;
				int ogenLabelWidth = fm.stringWidth(ogenLabel);
				g2.drawString(""+i, rectx+(staafBreedte-ogenLabelWidth)/2, getHeight()-bottomHeight+fm.getAscent()+2);
				
				String barHeightString = ""+aantalDSGegooid[i];
				int barHeightStringWidth = fm.stringWidth(barHeightString);
				//if(aantalDSGegooid[i]>0)
				//	g2.drawString(barHeightString, rectx+(staafBreedte-barHeightStringWidth)/2, getHeight()-bottomHeight-barHeight-3);
			}
		
		g2.setFont(labelFont);
		fm = g2.getFontMetrics();
		
		String labelYas = yTekst;
		int labelYasWidth = fm.stringWidth(labelYas);
		AffineTransform rotation = AffineTransform.getRotateInstance(-Math.PI / 2, getHeight() / 2, getHeight() / 2);
		g2.transform(rotation);
		g2.drawString(labelYas, (getHeight()+bottomHeight-labelYasWidth)/2,  leftMargin-25);
		
		g2.setStroke(new BasicStroke(1.0f));
		g2.draw(new Line2D.Double( (getHeight()+bottomHeight-labelYasWidth)/2,leftMargin-15,(getHeight()+bottomHeight+labelYasWidth)/2,leftMargin-15));
		g2.draw(new Line2D.Double((getHeight()+bottomHeight+labelYasWidth)/2,leftMargin-15,(getHeight()+bottomHeight+labelYasWidth)/2-6,leftMargin-15+4));
		g2.draw(new Line2D.Double( (getHeight()+bottomHeight+labelYasWidth)/2,leftMargin-15,(getHeight()+bottomHeight+labelYasWidth)/2-6,leftMargin-15-4));
		
	}
	
	// ActionProducer
			private ActionListener actionListener = null;

			public void addActionListener(ActionListener l)	{
				actionListener = AWTEventMulticaster.add(actionListener, l);
			}

			public void removeActionListener(ActionListener l)	{
				actionListener = AWTEventMulticaster.remove(actionListener, l);
			}

			public void produceAction(String command) {
				if (actionListener != null)	{
					actionListener.actionPerformed(new ActionEvent(this, 0, command));
				}
			}
}
