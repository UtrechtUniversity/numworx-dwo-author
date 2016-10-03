package fi.statsim;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;

import javax.swing.JPanel;

public class MuntenFrequentieGrafiek extends JPanel {

	private int aantalMunten = 2;
	private int aantalKeerGooien = 10;
	private int[] aantalMuntGegooid;
	
	private int bottomHeight = 82;
	private int leftMargin = 35;
	private int staafBreedte = 25;
	private int staafTussenruimte = 12;
	private int maxGraphHeight = 100;
	
	private String yTekst = "";
	
	private Font labelFont = new Font("SansSerif", Font.PLAIN, 12);
	private Font labelKleinFont = new Font("SansSerif", Font.PLAIN, 10);
		
	public MuntenFrequentieGrafiek () {
		aantalMuntGegooid = new int[5];
	}
	
	public void setSize(int w, int h) {
		maxGraphHeight = h-bottomHeight;
		staafBreedte = (w-leftMargin)/8;
		staafTussenruimte = staafBreedte/2;
		super.setSize(w, h);
		repaint();
	}
	
	public void setBounds(int x, int y, int w, int h) {
		maxGraphHeight = h-bottomHeight;
		staafBreedte = (w-leftMargin)/8;
		staafTussenruimte = staafBreedte/2;
		super.setBounds(x, y, w, h);
		repaint();
	}
	
	public void reset() {
		aantalMuntGegooid[0] = 0;
		aantalMuntGegooid[1] = 0;
		aantalMuntGegooid[2] = 0;
		aantalMuntGegooid[3] = 0;
		aantalMuntGegooid[4] = 0;
		repaint();
	}
	
	public void zetYtekst(String yTekst) {
		this.yTekst = yTekst;
	}
	
	public void zetGegooid(int[] aantalMuntGegooid)	{
		for(int i=0 ; i<aantalMunten+1 ; i++) {
			this.aantalMuntGegooid[i] = aantalMuntGegooid[i];
		}
		repaint();
	}
	
	public void zetAantalMunten(int aantalMunten) {
		this.aantalMunten = aantalMunten;
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
		String topLabel = ""+aantalKeerGooien;
		String nulLabel = "0";
		int topLabelWidth = fm.stringWidth(topLabel);
		int nulLabelWidth = fm.stringWidth(nulLabel);
		g2.drawString(topLabel, leftMargin-topLabelWidth-5, fm.getAscent()); 
		g2.drawString(nulLabel, leftMargin-nulLabelWidth-5, getHeight()-bottomHeight); 
		        
		g2.setStroke(new BasicStroke(1.5f));
		g2.draw(new Line2D.Double(leftMargin,0,leftMargin,getHeight()-bottomHeight));
		g2.draw(new Line2D.Double(leftMargin,getHeight()-bottomHeight,leftMargin+(aantalMunten+1)*(staafBreedte+staafTussenruimte)+staafTussenruimte,getHeight()-bottomHeight));
		
		g2.setFont(labelKleinFont);
		fm = g2.getFontMetrics();
		maxGraphHeight = getHeight()-bottomHeight;
		for(int i=0 ; i<aantalMunten+1 ;i++) {
			int barHeight = aantalMuntGegooid[i]*maxGraphHeight/aantalKeerGooien;
			int rectx = leftMargin+staafTussenruimte+(staafBreedte+staafTussenruimte)*(i);
			int recty = getHeight()-bottomHeight-barHeight;
			
			Rectangle2D rect = new Rectangle2D.Double(rectx,recty,staafBreedte,barHeight);
			g2.setPaint(Color.ORANGE);
		    g2.fill(rect);	
		    g2.setPaint(Color.BLACK);
			g2.draw(new Rectangle2D.Double(rectx,recty,staafBreedte,barHeight));
			
			for(int j=0 ; j<aantalMunten ;j++) {	
				String label = (i<=j ? "Kop" : "Munt");
				String en = "en";
				int labelWidth = fm.stringWidth(label);
				int enWidth = fm.stringWidth(en);
				int x = rectx + (staafBreedte-labelWidth)/2;
				int y = getHeight() - bottomHeight + fm.getAscent()*(2*j+1);
				int xen = rectx + (staafBreedte-enWidth)/2;
				int yen = getHeight() - bottomHeight + fm.getAscent()*(2*j+2);
				g2.drawString(label, x, y+2);
				if(j<aantalMunten-1)
					g2.drawString(en, xen, yen+2);
			}
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
}
