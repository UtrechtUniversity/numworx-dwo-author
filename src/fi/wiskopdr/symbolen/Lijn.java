package fi.wiskopdr.symbolen;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

public class Lijn extends Symbool {

	public Lijn(int richting)
	{
		super(richting);
	}
	
	public void paintComponent(Graphics g)
	{
		g.setColor(kleur);
		((Graphics2D)g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		((Graphics2D)g).setStroke(new BasicStroke(1.2f));
	    
		//uitzoeken hoe ik dikte goed kan verwerken. Meerdere lijnen naast elkaar? Of kan ik één dikke stroke maken?
		//iets met rendering?
		
		if(richting == RICHTING_LINKS || richting == RICHTING_RECHTS)
			g.drawLine(0, this.getHeight()/2, this.getWidth(), this.getHeight()/2);
		else if(richting == RICHTING_BOVEN || richting == RICHTING_BENEDEN)
			g.drawLine(this.getWidth()/2, 0, this.getWidth()/2, this.getHeight());
		else if(richting == RICHTING_LINKSBOVEN || richting == RICHTING_RECHTSONDER)
			g.drawLine(0, 0, this.getWidth(), this.getHeight());
		else
			g.drawLine(0, this.getHeight(), this.getWidth(), 0);
			
	}
	
	public int geefType()
	{
		return Symbool.LIJN;
	}
}
