package fi.binomverdeling;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

/**
 * Dit is een panel dat een grafische weergave van een binomiale verdeling geeft
 * door middel van een staafdiagram.
 */
public class BVStaafjesPanel extends JPanel {
	private BVInteractiePanel interactiePanel;
	private double staafBreedte;

	public static final int XASBALKHEIGHT = 20;
	public static final int YASBALKWIDTH = 25;

	private boolean showXAs;
	private boolean showYAs;

	/**
	 * Constructor
	 * @param interactiePanel Het BVInteractiePanel dat de gegevens geeft voor dit BVStaafjesPanel
	 */
	public BVStaafjesPanel(BVInteractiePanel interactiePanel) {
		this.interactiePanel = interactiePanel;
		this.showXAs = true;
		this.showYAs = true;
		
		this.setLayout(null);
	}
	
	/**
	 * Bereken de breedte van de staafjes aan de hand van het aantal staafjes en 
	 * de breedte van het panel
	 */
	private void berekenStaafBreedte() {
		//hou rekening met ruimte om de y-as in te tekenen
		int asRuimte = 0;
		if(this.showYAs) {
			asRuimte = BVStaafjesPanel.YASBALKWIDTH;
		}
		
		//teken staafje
		this.staafBreedte = (double)(this.getWidth()-asRuimte)/(double)(this.interactiePanel.getN()+1);
	}
	
	public boolean getShowXAs() {
		return this.showXAs;
	}
	public void setShowXAs(boolean b) {
		this.showXAs = b;
		this.repaint();
	}
	public boolean getShowYAs() {
		return this.showYAs;
	}
	public void setShowYAs(boolean b) {
		this.showYAs = b;
		this.repaint();
	}
	
	/**
	 * Paint de y-as met schaal 
	 * @param g Het Graphics object waarin de as getekend gaat worden
	 */
	private void paintYAs(Graphics g) {
		if(this.showYAs) {
			g.setColor(Color.CYAN);
			
			//teken de as zelf
			g.fillRect(BVStaafjesPanel.YASBALKWIDTH-2, 0, 2, this.getHeight());
			
			//bepaal de lengte van de as
			int asHoogte = this.getHeight();
			if(this.showXAs) {
				asHoogte -= BVStaafjesPanel.XASBALKHEIGHT;
			}
			
			//teken de streepjes op de as
			for(int i = 1; i <= 10; i++) {
				g.drawLine(BVStaafjesPanel.YASBALKWIDTH-6, asHoogte - (int)(i*(double)asHoogte/(double)10), BVStaafjesPanel.YASBALKWIDTH-2, asHoogte - (int)(i*(double)asHoogte/(double)10));
			}
			
			//teken de tekst bij de streepjes op de as
			g.setColor(Color.BLACK);
			for(int i = 1; i <= 10; i++) {
				g.drawString(Double.toString((double)i/10.0), 0, asHoogte - (int)(i*(double)asHoogte/(double)10)+9);
			}
		}
	}
	
	/**
	 * Paint de x-as met schaal 
	 * @param g Het Graphics object waarin de as getekend gaat worden
	 */
	private void paintXAs(Graphics g) {
		if(this.showXAs) {
			g.setColor(Color.CYAN);
			//teken de as zelf
			g.fillRect(0, this.getHeight()-BVStaafjesPanel.XASBALKHEIGHT, this.getWidth(), 2);
			
			//bepaal de lengte van de as en waar de as begint
			int asLengte = this.getWidth();
			int xOffset = 0;
			if(this.showYAs) {
				asLengte -= BVStaafjesPanel.YASBALKWIDTH;
				xOffset = BVStaafjesPanel.YASBALKWIDTH;
			}
			
			
			//bepaal per hoeveel staafjes er een streepje en tekst komt
			int streepjesFrequentie = 1;
			while ((double)(this.interactiePanel.getN()+1)/ (double)streepjesFrequentie >= 15) {
				streepjesFrequentie++;
			}
			
			//teken de streepjes op de as
			for(int i = 0; i < 15; i++) {
				g.drawLine(xOffset + (int)(((double)i*(double)streepjesFrequentie+0.5)*this.staafBreedte), this.getHeight()-BVStaafjesPanel.XASBALKHEIGHT+1, xOffset + (int)(((double)i*(double)streepjesFrequentie+0.5)*this.staafBreedte), this.getHeight()-BVStaafjesPanel.XASBALKHEIGHT+5);
			}
			
			//teken de tekst bij de streepjes
			g.setColor(Color.BLACK);
			for(int i = 0; i < 15; i++) {
				g.drawString(Integer.toString(i*streepjesFrequentie), xOffset + (int)(((double)i*(double)streepjesFrequentie+0.5)*this.staafBreedte), this.getHeight());
			}
		}
	}

	/**
	 * Paint een enkel staafje
	 * @param g Het Graphics object waarin het staafje getekend moet worden
	 * @param k Het nummer van het te tekenen staafje
	 */
	public void paintStaafje(Graphics g, int k) {
		//kies de kleur
		if(k<=this.interactiePanel.getSuccessen()) {
			g.setColor(Color.BLACK);
		}
		else {
			g.setColor(Color.GRAY);
		}
		
		//hou indien nodig ruimte vrij om de assen te tekenen
		int xOffset = 0;
		int yOffset = 0;
		if(this.showYAs) {
			xOffset = BVStaafjesPanel.YASBALKWIDTH;
		}
		if(this.showXAs) {
			yOffset = BVStaafjesPanel.XASBALKHEIGHT;
		}
		
		//teken het staafje
		g.fillRect((int)(k*this.staafBreedte+xOffset), this.getHeight() - (int)(this.interactiePanel.berekenKansK(k)*(this.getHeight()-yOffset)+yOffset),(int)((k+1)*this.staafBreedte+xOffset) - (int)(k*this.staafBreedte+xOffset), (int)(this.interactiePanel.berekenKansK(k)*(this.getHeight()-yOffset)));
	}
	
	/**
	 * Paint gehele component
	 */
	//Override
	public void paintComponent(Graphics g) {
        this.berekenStaafBreedte();
        g.clearRect(0, 0, this.getWidth(), this.getHeight());
        this.paintXAs(g);
        this.paintYAs(g);
        
		for(int k = 0; k <= this.interactiePanel.getN(); k++) {
			this.paintStaafje(g,k);
		}
	}
}