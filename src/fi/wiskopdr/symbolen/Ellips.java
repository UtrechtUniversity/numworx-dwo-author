package fi.wiskopdr.symbolen;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;

public class Ellips extends Symbool{
	
	public Ellips(int richting)
	{
		super(richting);
	}

	public void paintComponent(Graphics g)
	{
		g.setColor(kleur);
		//uitzoeken hoe ik dikte goed kan verwerken. Meerdere lijnen naast elkaar? Of kan ik één dikke stroke maken?
		//iets met rendering?
		
		Graphics2D g2 = (Graphics2D) g;
		g2.draw(new Ellipse2D.Double(0, 0, this.getWidth(), this.getHeight()));
                
	}
	
	public int geefType()
	{
		return Symbool.ELLIPS;
	}
}
