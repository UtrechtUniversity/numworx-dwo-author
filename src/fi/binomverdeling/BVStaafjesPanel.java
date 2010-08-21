package fi.binomverdeling;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

/**
 * Dit is een panel dat een grafische weergave van een binomiale verdeling geeft
 * door middel van een staafdiagram.
 */
public class BVStaafjesPanel extends JPanel {
	private BVInteractiePanel interactiepanel;
	private double staafbreedte;

	public BVStaafjesPanel(BVInteractiePanel interactiepanel) {
		this.interactiepanel = interactiepanel;
	}
	
	/**
	 * Bereken de breedte van de staafjes aan de hand van het aantal staafjes en 
	 * de breedte van het panel
	 */
	private void berekenStaafBreedte() {
		this.staafbreedte = (double)this.getWidth()/(double)(this.interactiepanel.getN()+1);
		//this.staafbreedte = this.getWidth()/(2*this.interactiepanel.getN()+3);
	}

	/**
	 * Paint een enkel staafje
	 * @param g Het Graphics object waarin het staafje getekend moet worden
	 * @param k Het nummer van het te tekenen staafje
	 */
	public void paintStaafje(Graphics g, int k) {
		if(k<=this.interactiepanel.getSuccessen()) {
			g.setColor(Color.BLACK);
		}
		else {
			g.setColor(Color.GRAY);
		}
		g.fillRect((int)(k*this.staafbreedte), this.getHeight() - (int)(this.interactiepanel.berekenKansK(k)*(this.getHeight()-this.interactiepanel.EDITHEIGHT)+this.interactiepanel.EDITHEIGHT),(int)((k+1)*this.staafbreedte) - (int)(k*this.staafbreedte), (int)(this.interactiepanel.berekenKansK(k)*(this.getHeight()-this.interactiepanel.EDITHEIGHT)));
		//g.fillRoundRect((2*k+1)*this.staafbreedte, this.getHeight() - (int)(this.interactiepanel.berekenKansK(k)*(this.getHeight()-this.interactiepanel.EDITHEIGHT)+this.interactiepanel.EDITHEIGHT), this.staafbreedte, (int)(this.interactiepanel.berekenKansK(k)*(this.getHeight()-this.interactiepanel.EDITHEIGHT)),this.staafbreedte/3,this.staafbreedte/3);
	}
	
	/**
	 * Paint gehele component
	 */
	//Override
	public void paintComponent(Graphics g) {
        this.berekenStaafBreedte();
        g.clearRect(0, 0, this.getWidth(), this.getHeight());
		for(int k = 0; k <= this.interactiepanel.getN(); k++) {
			this.paintStaafje(g,k);
		}
	}
}