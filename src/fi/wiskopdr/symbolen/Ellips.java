package fi.wiskopdr.symbolen;

import java.awt.Graphics;

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
		
		//nog invullen.
	}
	
	public int geefType()
	{
		return Symbool.ELLIPS;
	}
}
