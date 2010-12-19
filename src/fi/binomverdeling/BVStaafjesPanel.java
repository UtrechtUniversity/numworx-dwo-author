package fi.binomverdeling;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;

/**
 * Dit is een panel dat een grafische weergave van een binomiale verdeling geeft
 * door middel van een staafdiagram.
 */
public class BVStaafjesPanel extends JPanel implements ActionListener {
	private BVInteractiePanel interactiePanel;
	private double staafBreedte;
	private double multiplier; //vermenigvuldigingsfactor tov de x-as;
	
	private static final double VULHOOGTE = 0.8; //welk deel van de hoogte van het staafjespanel het grootste staafje inneemt
	
	private int sliderOffset = 5;
	
	public static final int XASBALKHEIGHT = 25;
	public static final int YASBALKWIDTH = 35;
	public static final int MAX_STREEPJES = 10;
	public static final int YAS_INKORTEN = 10;

	private int grensLinks;
	private int grensRechts;
	
	private boolean tweeGrenzen; //true = twee grenzen, false = 1 grens
	private GrenzenOptie grenzenOptie;
	
	private boolean showXAs;
	private boolean showYAs;
	private boolean showGrensSlider;
	
	private Slider successenSlider;
	private DoubleSlider successenDoubleSlider;

	private int[] toegestaneSchaalverdelingen = {1, 2, 5, 10, 20, 50, 100, 200, 500, 1000};
	
	private Font font;
	private FontMetrics fontMetrics;
	
	private Rectangle lastBounds; //laatst gezette bounds, om een resize te kunnen merken
	
	
	/**
	 * Constructor
	 * @param interactiePanel Het BVInteractiePanel dat de gegevens geeft voor dit BVStaafjesPanel
	 */
	public BVStaafjesPanel(BVInteractiePanel interactiePanel, GrenzenOptie grenzenOptie) {
		this.lastBounds = new Rectangle(0,0,0,0);
		this.setLayout(null);
		this.interactiePanel = interactiePanel;
		
		this.font = new Font("Dialog", Font.PLAIN, 12);
		this.fontMetrics = getFontMetrics(this.font);
		
		this.showXAs = true;
		this.showYAs = true;
		this.grensLinks = 5;
		this.grensRechts = 10;
		
		this.tweeGrenzen = false;
		this.grenzenOptie = grenzenOptie;
		this.showGrensSlider = true;
		
		//maak de sliders aan, maar doe er verder nog niets mee
		this.successenDoubleSlider = new DoubleSlider(100,40,80);
		this.successenSlider = new Slider(100,40);
		
		this.add(this.successenDoubleSlider);
		this.add(this.successenSlider);
		
		this.successenDoubleSlider.addActionListener(this);
		this.successenSlider.addActionListener(this);
		
		this.multiplier = 1.0;

		this.addRightSlider();
		this.updateSuccessenSliderPosition();
	}
	
	/**
	 * Bereken de breedte van de staafjes aan de hand van het aantal staafjes en 
	 * de breedte van het panel
	 */
	public void berekenStaafBreedte() {
		//hou rekening met ruimte om de y-as in te tekenen
		int asRuimte = 0;
		if(this.showYAs) {
			asRuimte = BVStaafjesPanel.YASBALKWIDTH;
		}
		
		//bereken staafbreedte
		this.staafBreedte = (double)(this.getWidth()-asRuimte)/(double)(this.interactiePanel.getN()+1);
		
	}
	
	public void bepaalGrenzenMetSlider() {
		System.out.println("bepaal grenzen met sliders");
		if(this.showGrensSlider) {
			if(this.tweeGrenzen) {
	        	this.grensRechts = (int)((double)(this.successenDoubleSlider.geefStandRechts())/this.staafBreedte);
				this.grensLinks = (int)((double)(this.successenDoubleSlider.geefStandLinks())/this.staafBreedte);
	        }
	        else {
	        	this.grensRechts = (int)((double)(this.successenSlider.geefStand())/this.staafBreedte);
	        }
		
		}
	}
	
	/**
	 * @return De modus van de binomiale verdeling
	 */
	private int getModeBV() {
		if(this.interactiePanel.getP() == 1.0) {
			return this.interactiePanel.getN();
		}
		else {
			return (int)((this.interactiePanel.getN()+1)*this.interactiePanel.getP());
		}
	}
	
	private int getModeHyp() {
		if(this.interactiePanel.getM() >= this.interactiePanel.getPopulatie()) {
			return this.interactiePanel.getN();
		}
		else {
			return (int)((this.interactiePanel.getN() + 1) * (this.interactiePanel.getM() + 1)/(this.interactiePanel.getPopulatie()+2));
		}
	}
	
	/**
	 * Bepaal hoeveel alle staafjes vergroot moeten worden om het scherm goed te vullen
	 */
	private void berekenMultiplier() {
		int modus;
		double maxHoogte;
		if(!this.interactiePanel.getHypergeometrisch()) {
			modus = this.getModeBV();
			maxHoogte = this.interactiePanel.berekenKansK(modus);
		}
		else {
			modus = this.getModeHyp();
			maxHoogte = this.interactiePanel.berekenHyperKansK(modus);
		}
		
		this.multiplier = 1/(maxHoogte/BVStaafjesPanel.VULHOOGTE);
	}
	public void setTweeGrenzen(boolean tweeGrenzen) {
		this.tweeGrenzen = tweeGrenzen;
		this.updateSliderBounds();
		this.addRightSlider();
		this.updateSuccessenSliderPosition();
	}
	public boolean getTweeGrenzen() {
		return this.tweeGrenzen;
	}
	public void setGrenzenOptie(GrenzenOptie grenzenOptie) {
		this.grenzenOptie = grenzenOptie;
		this.repaint();
	}
	public GrenzenOptie getGrenzenOptie() {
		return this.grenzenOptie;
	}
	public boolean getShowXAs() {
		return this.showXAs;
	}
	public void setShowXAs(boolean b) {
		this.showXAs = b;
		this.updateSliderBounds();
		this.repaint();
	}
	public boolean getShowYAs() {
		return this.showYAs;
	}
	public void setShowYAs(boolean b) {
		this.showYAs = b;
		this.updateSliderBounds();
		this.repaint();
	}
	public void setShowGrensSlider(boolean b) {
		this.showGrensSlider = b;
		this.addRightSlider();
		this.repaint();
	}
	public boolean getShowGrensSlider() {
		return this.showGrensSlider;
	}
	public int getGrensLinks() {
		return this.grensLinks;
	}
		
	public int getGrensRechts() {
		return this.grensRechts;
	}
	public void setGrensLinks(int grensLinks) {
		this.grensLinks = grensLinks;
		this.updateSuccessenSliderPosition();
	}
	public void setGrensRechts(int grensRechts) {
		this.grensRechts = grensRechts;
		this.updateSuccessenSliderPosition();
	}
	
	/**
	 * zet de locatie en lengte voor de slider
	 */
	private void updateSliderBounds() {
		this.berekenStaafBreedte(); //update de staafbreedte
		
		int x;
		int y;
		int lengte;
		
		x = BVStaafjesPanel.YASBALKWIDTH-6;			
		y = this.getHeight()-BVStaafjesPanel.XASBALKHEIGHT-5;
		lengte = this.getWidth()-BVStaafjesPanel.YASBALKWIDTH;

		if (!this.showXAs) {
			y = this.getHeight() - 8;
		}
		if (!this.showYAs) {
			lengte += BVStaafjesPanel.YASBALKWIDTH;
			x = -6;
		}
			
		this.successenDoubleSlider.zetLengte(lengte);
		this.successenDoubleSlider.setLocation(x, y);
		
		this.successenDoubleSlider.zetStandRechts((int)((this.grensRechts)*this.staafBreedte) - this.successenDoubleSlider.getMinimumLinks());
		
		
		
		this.successenSlider.zetLengte(lengte);
		this.successenSlider.setLocation(x, y);
	}
	
	/**
	 * Zorgt dat de juiste slider aan het panel toegevoegd is.
	 */
	private void addRightSlider() {
		/*
		this.berekenStaafBreedte();
		if(this.showGrensSlider) {
			if (this.tweeGrenzen) {
				this.remove(this.successenSlider);
				this.successenDoubleSlider.zetStandRechts(this.successenSlider.geefStand());				
				this.add(this.successenDoubleSlider);
			}

			else {
				this.remove(this.successenDoubleSlider);
				this.successenSlider.zetStand(this.successenDoubleSlider.geefStandRechts());
				this.add(this.successenSlider);
			}
		}
		else {
			this.remove(this.successenDoubleSlider);
			this.remove(this.successenSlider);
		}*/
		this.berekenStaafBreedte();
		if(this.showGrensSlider) {
			if (this.tweeGrenzen) {
				this.successenSlider.setVisible(false);
				this.successenDoubleSlider.zetStandRechts(this.successenSlider.geefStand());				
				this.successenDoubleSlider.setVisible(true);
			}

			else {
				this.successenDoubleSlider.setVisible(false);
				this.successenSlider.zetStand(this.successenDoubleSlider.geefStandRechts());
				this.successenSlider.setVisible(true);
			}
		}
		else {
			this.successenDoubleSlider.setVisible(false);
			this.successenSlider.setVisible(false);
		}
		
	}
	
	/**
	 * Paint de y-as met schaal 
	 * @param g Het Graphics object waarin de as getekend gaat worden
	 * @param multiplier De factor waarmee de staafjes vermenigvuldigd zijn, en de schaal dus ook vermenigvuldigd moet worden
	 */
	private void paintYAs(Graphics g, double multiplier) {
		if(this.showYAs) {
			g.setColor(Color.BLACK);
			
			//bepaal de lengte van de as
			int asHoogte = this.getHeight() - BVStaafjesPanel.YAS_INKORTEN;
			if(this.showXAs) {
				asHoogte -= BVStaafjesPanel.XASBALKHEIGHT;
			}

			//teken de as zelf
			g.drawLine(BVStaafjesPanel.YASBALKWIDTH-1, BVStaafjesPanel.YAS_INKORTEN, BVStaafjesPanel.YASBALKWIDTH-1, BVStaafjesPanel.YAS_INKORTEN+asHoogte);
			//g.fillRect(BVStaafjesPanel.YASBALKWIDTH-1-BVStaafjesPanel.YAS_INKORTEN, 0, 1, asHoogte);
			
			//teken de streepjes op de as
			for(int i = 1; i <= 10; i++) {
				g.drawLine(BVStaafjesPanel.YASBALKWIDTH-6, asHoogte - (int)(i*(double)asHoogte/(double)10)+BVStaafjesPanel.YAS_INKORTEN, BVStaafjesPanel.YASBALKWIDTH-2, asHoogte - (int)(i*(double)asHoogte/(double)10)+BVStaafjesPanel.YAS_INKORTEN);
			}
			
			//teken de tekst bij de streepjes op de as
			g.setColor(Color.BLACK);
			for(int i = 1; i <= 10; i++) {
				int j = (int)(i*10.0*asHoogte/(asHoogte+BVStaafjesPanel.YAS_INKORTEN)/this.multiplier);
				g.drawString(Double.toString((double)j/100.0), 1, asHoogte + BVStaafjesPanel.YAS_INKORTEN - (int)(1.0*i*(double)asHoogte/10.0 - (1.0 * this.fontMetrics.getHeight() / 2.0))-2);
			}
		}
	}
	
	/**
	 * Paint de x-as met schaal 
	 * @param g Het Graphics object waarin de as getekend gaat worden
	 */
	private void paintXAs(Graphics g) {
		if(this.showXAs) {
			g.setColor(Color.BLACK);
			
			//teken de as zelf
			//als de slider niet getekend wordt, teken dan een lijn
			if(!this.showGrensSlider) {
				g.setColor(Color.BLACK);
				if(this.showYAs){
					g.fillRect(BVStaafjesPanel.YASBALKWIDTH-1, this.getHeight()-BVStaafjesPanel.XASBALKHEIGHT, this.getWidth()- BVStaafjesPanel.YASBALKWIDTH +1, 1);
				}
				else {
					g.fillRect(0, this.getHeight()-BVStaafjesPanel.XASBALKHEIGHT, this.getWidth(), 1);
				}
			}
			
			//bepaal de lengte van de as en waar de as begint
			int asLengte = this.getWidth();
			int xOffset = 0;
			if(this.showYAs) {
				asLengte -= BVStaafjesPanel.YASBALKWIDTH;
				xOffset = BVStaafjesPanel.YASBALKWIDTH;
			}
			
			//bepaal per hoeveel staafjes er een streepje en tekst komt
			int index = 0;
			int streepjesFrequentie = this.toegestaneSchaalverdelingen[index];
			while ((index < this.toegestaneSchaalverdelingen.length-1) && (double)(this.interactiePanel.getN()+1)/ (double)(streepjesFrequentie) >= BVStaafjesPanel.MAX_STREEPJES) {
				index++;
				streepjesFrequentie = this.toegestaneSchaalverdelingen[index];
			}
						
			//teken de streepjes op de as
			if(this.interactiePanel.getN() <= 100) {
				for(int i = 0; i < this.interactiePanel.getN()+1; i++) {
					g.drawLine(xOffset + (int)((double)(i+0.5)*this.staafBreedte), this.getHeight()-BVStaafjesPanel.XASBALKHEIGHT+1, xOffset + (int)((double)(i+0.5)*this.staafBreedte), this.getHeight()-BVStaafjesPanel.XASBALKHEIGHT+2);
				}
			}
			else {
				for(int i = 0; i < (this.interactiePanel.getN()+1)/5; i++) {
					g.drawLine(xOffset + (int)((double)(5*i+0.5)*this.staafBreedte), this.getHeight()-BVStaafjesPanel.XASBALKHEIGHT+1, xOffset + (int)((double)(5*i+0.5)*this.staafBreedte), this.getHeight()-BVStaafjesPanel.XASBALKHEIGHT+2);
				}
			}
			for(int i = 0; i < 15; i++) {
				g.drawLine(xOffset + (int)(((double)i*(double)streepjesFrequentie+0.5)*this.staafBreedte), this.getHeight()-BVStaafjesPanel.XASBALKHEIGHT+1, xOffset + (int)(((double)i*(double)streepjesFrequentie+0.5)*this.staafBreedte), this.getHeight()-BVStaafjesPanel.XASBALKHEIGHT+5);
			}
			
			//teken de tekst bij de streepjes
			g.setColor(Color.BLACK);
			g.setFont(this.font);
			for(int i = 0; i < 15; i++) {
				//teken alleen als de tekst helemaal op het paneel past
				if(!((this.fontMetrics.stringWidth(Integer.toString(i*streepjesFrequentie))+(xOffset + (int)(((double)i*(double)streepjesFrequentie+0.5)*this.staafBreedte) - (this.fontMetrics.stringWidth(Integer.toString(i*streepjesFrequentie))/2)))>this.getWidth())) {
					g.drawString(Integer.toString(i*streepjesFrequentie), xOffset + (int)(((double)i*(double)streepjesFrequentie+0.5)*this.staafBreedte) - (this.fontMetrics.stringWidth(Integer.toString(i*streepjesFrequentie))/2), this.getHeight()-BVStaafjesPanel.XASBALKHEIGHT+18);
				}
			}
		}
		else {
			//teken lijn als er geen slider getekend wordt
			if(!this.showGrensSlider) {
				g.setColor(Color.BLACK);
				if(this.showYAs) {
					g.drawLine(BVStaafjesPanel.YASBALKWIDTH, this.getHeight()-3, this.getWidth()-BVStaafjesPanel.YASBALKWIDTH, this.getHeight()-3);
				}
				else {
					g.drawLine(0, this.getHeight()-3, this.getWidth(), this.getHeight()-3);
				}
			}
		}
	}

	private Color bepaalStaafKleur(int k) {		
		if ((this.tweeGrenzen && k <= this.grensRechts && k >= this.grensLinks) ||
			(!this.tweeGrenzen && this.grenzenOptie == GrenzenOptie.LINKS && k<=this.grensRechts) ||
			(!this.tweeGrenzen && this.grenzenOptie == GrenzenOptie.GELIJK && k==this.grensRechts) ||
			(!this.tweeGrenzen && this.grenzenOptie == GrenzenOptie.RECHTS && k>=this.grensRechts))	{
			return this.interactiePanel.STAAFJE_TELT;
		}
		else {
			return this.interactiePanel.STAAFJE_TELT_NIET;
		}
	}
	
	/**
	 * Paint een enkel staafje
	 * @param g Het Graphics object waarin het staafje getekend moet worden
	 * @param k Het nummer van het te tekenen staafje
	 */
	public void paintStaafje(Graphics g, int k, double multiplier) {		
		//hou indien nodig ruimte vrij om de assen te tekenen
		int xOffset = 0;
		int yOffset = 3;
		if(this.showYAs) {
			xOffset = BVStaafjesPanel.YASBALKWIDTH;
		}
		if(this.showXAs) {
			yOffset = BVStaafjesPanel.XASBALKHEIGHT;
		}
		
		//bepaal grootte
		int x = (int)(k*this.staafBreedte+xOffset);
		int y;
		int height;
		if(!this.interactiePanel.getHypergeometrisch()) {
			y = this.getHeight() - (int)(this.interactiePanel.berekenKansK(k)*multiplier*(this.getHeight()-yOffset)+yOffset);
			height = (int)(this.interactiePanel.berekenKansK(k)*(this.getHeight()-yOffset)*this.multiplier);
		}
		else {
			y = this.getHeight() - (int)(this.interactiePanel.berekenHyperKansK(k)*multiplier*(this.getHeight()-yOffset)+yOffset);
			height = (int)(this.interactiePanel.berekenHyperKansK(k)*(this.getHeight()-yOffset)*this.multiplier);
		}
		int width = (int)((k+1)*this.staafBreedte+xOffset) - (int)(k*this.staafBreedte+xOffset)+1;
		
		
		if(width > 0 && height > 0) {
			//teken omleining
			g.setColor(Color.BLACK);
			g.drawRect(x, y, width-1, height-1);
			
			//kleur het staafje in
			g.setColor(this.bepaalStaafKleur(k));
			g.fillRect(x+1, y+1, width-2, height-2);
		}
	}
	
	/**
	 * Paint gehele component
	 */
	//Override
	public void paintComponent(Graphics g) {		
        this.berekenStaafBreedte();
        this.berekenMultiplier();
        
        g.clearRect(0, 0, this.getWidth(), this.getHeight());
        
        this.paintXAs(g);
        this.paintYAs(g,this.multiplier);
        
		for(int k = 0; k <= this.interactiePanel.getN(); k++) {
			this.paintStaafje(g,k,this.multiplier);
		}
	}
	
	//Override
	public void setBounds(int x, int y, int b, int h) {
		//check of de bounds daadwerkelijk worden veranderd
		Rectangle r = new Rectangle(x,y,b,h);
		if(!r.equals(this.lastBounds)) {
			
			super.setBounds(x,y,b,h);
			this.updateSliderBounds();
			this.successenDoubleSlider.zetStandRechts((int)((this.grensRechts+0.5)*this.staafBreedte) - this.successenDoubleSlider.getMinimumLinks());
			this.successenDoubleSlider.zetStandLinks((int)((this.grensLinks+0.5)*this.staafBreedte) - this.successenDoubleSlider.getMinimumLinks());
			this.successenSlider.zetStand((int)((this.grensRechts+0.5)*this.staafBreedte) - this.successenSlider.getMinimum());
		}
		
		//noteer dat deze bounds zijn gezet
		this.lastBounds = r;
		
	}
	
	/**
	 * Zet de slidergrenzen weer midden onder het staafje
	 */
	public void updateSuccessenSliderPosition() {
		/*
		this.successenSlider.zetStand((int)((1.0*this.grensRechts+0.5)*this.staafBreedte)+1);
		this.successenDoubleSlider.zetStandRechts((int)((1.0*this.grensRechts+0.5)*this.staafBreedte)+1);
		this.successenDoubleSlider.zetStandLinks((int)((1.0*this.grensLinks+0.5)*this.staafBreedte)+1);
		*/
		this.successenDoubleSlider.zetStandRechts((int)((this.grensRechts+0.5)*this.staafBreedte) - this.successenDoubleSlider.getMinimumLinks());
		this.successenDoubleSlider.zetStandLinks((int)((this.grensLinks+0.5)*this.staafBreedte) - this.successenDoubleSlider.getMinimumLinks());
		this.successenSlider.zetStand((int)((this.grensRechts+0.5)*this.staafBreedte) - this.successenSlider.getMinimum());
	}
	
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == this.successenSlider) {
			this.bepaalGrenzenMetSlider();
			if(e.getActionCommand().equals("stop")) {
				this.updateSuccessenSliderPosition();
			}
		}
		
		if(e.getSource() == this.successenDoubleSlider) {
			this.bepaalGrenzenMetSlider();
			if(e.getActionCommand().equals("stop")) {
				this.updateSuccessenSliderPosition();
			}
		}
		this.interactiePanel.updateKansBalk();
		this.repaint();
		
	}
}