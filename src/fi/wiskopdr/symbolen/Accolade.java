package fi.wiskopdr.symbolen;

import java.awt.Graphics;

public class Accolade extends Symbool{

	public Accolade(int richting)
	{
		super(richting);
	}
	
	public void paintComponent(Graphics g)
	{
		g.setColor(kleur);
		//uitzoeken hoe ik dikte goed kan verwerken. Meerdere lijnen naast elkaar? Of kan ik één dikke stroke maken?
		//dat zou fijn zijn. iets met rendering?
		
		//afkijken van accolade in formule.
		if(richting == Symbool.RICHTING_LINKS)
		{	int x = this.getWidth()/2;
			g.drawArc(x, 1, 10, 10, 90, 90);
			g.drawLine(x, 6, x, getSize().height / 2 - 3);
			g.drawArc(x - 5, getSize().height / 2 - 6, 5, 5, 270, 90);
			g.drawArc(x - 5, getSize().height / 2, 5, 5, 0, 90);
			g.drawLine(x, getSize().height / 2 + 3, x, getSize().height - 6);
			g.drawArc(x, getSize().height - 12, 10, 10, 180, 90);
		}
		else if(richting == Symbool.RICHTING_RECHTS)
		{
			int x = this.getWidth()/2;
			g.drawArc(x - 10, 1, 10, 10, 0, 90);
			g.drawLine(x, 6, x, getSize().height / 2 - 3);
			g.drawArc(x, getSize().height / 2 - 6, 5, 5, 180, 90);
			g.drawArc(x, getSize().height / 2, 5, 5, 90, 90);
			g.drawLine(x, getSize().height / 2 + 3, x, getSize().height - 6);
			g.drawArc(x - 10, getSize().height - 12, 10, 10, 270, 90);
			
		}
		
	}
	
	public int geefType()
	{
		return Symbool.ACCOLADE;
	}
}
