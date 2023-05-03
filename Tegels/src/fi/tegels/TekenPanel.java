package fi.tegels;

import java.awt.*;

import javax.swing.*;

public class TekenPanel extends JPanel 
{
	Tegels owner;
	
	public TekenPanel(Tegels o)
	{	owner = o;
		
	}
	

	public void paintComponent(Graphics g)
	{
		tekenOpImage(g);
	}
	
  	public void tekenOpImage(Graphics g)
  	{ 	
  		
  		g.setColor(Color.white);
  		//g.setColor(Color.yellow);
    	//g.fillRect(0, 0, owner.breedte, owner.hoogte - owner.controlHoogte - 2);
  		g.fillRect(0, 0, owner.breedte, owner.hoogte - 2);
		tekenprogramma(g);
		
		
		g.setColor(Color.black);
		g.drawRect(0, 0, owner.breedte - 1, owner.hoogte - 1);

		// lijn boven control panel 
		//gIm.drawLine(181, owner.hoogte - 62, owner.breedte - 1, owner.hoogte - 62);
		if (!owner.demoVersion)
			g.drawLine(owner.hokBreedte + 1, owner.hoogte - owner.controlHoogte - 2, 
					   owner.breedte - 1, owner.hoogte - owner.controlHoogte - 2);
		
		//super.paint(g);
	}
	
 	void tekenOpnieuw()
	{	
/* 		
 		tekenOpImage();
		Graphics g = getGraphics();
		g.drawImage(im, 0, 0, null);
*/		
 		repaint();
	}	
/*	
 	public void update(Graphics g)
 	{
 		paint(g);
 	}
*/	
	public void tekenprogramma(Graphics g)
	{	tekenStukken(g);
	
		if (!owner.demoVersion)
		{	
			tekenHok(g);
			if (!owner.maakVorm && owner.basisv != null)
				tekenStapel(g);
		
			if (owner.actiefSs != null)
				tekenSs(owner.actiefSs, g);
		
			if (owner.maakVorm)
			{	tekenRoosterHok(g);
				tekenPunten(g);
				tekenLijnen(g);
			}
		}
	}
	
	void tekenStapel(Graphics g)
	{	tekenSs(new SchuifStuk(owner.transVersion, owner.basisv, owner.basisv.positie.x - 3, owner.basisv.positie.y - 3), g);
		if (!owner.transVersion)
			tekenSs(owner.basisv, g);
	}
	
	void tekenHok(Graphics gIm)
	{	gIm.setColor(Color.lightGray);
		//gIm.fillRect(0, owner.hoogte - 181, 180, 180);
		gIm.fillRect(0, owner.hoogte - owner.hokBreedte - 1, owner.hokBreedte, owner.hokBreedte);
		gIm.setColor(Color.black);
		//gIm.drawRect(0, owner.hoogte - 181, 180, 180);
		gIm.drawRect(0, owner.hoogte - owner.hokBreedte - 1, owner.hokBreedte, owner.hokBreedte);
	}
	
	void tekenRoosterHok(Graphics gIm)
	{	
		if (owner.transVersion)
		{
			gIm.setColor(Color.white);
			gIm.fillPolygon(owner.zeshok);
			gIm.setColor(Color.black);
			gIm.drawPolygon(owner.zeshok);
			
			if (owner.basisvOud != null)
			{	gIm.setColor(owner.basisvOud.kleur);
				gIm.fillPolygon(owner.basisvOud.pol);
			}
			
			gIm.setColor(Color.black);
			int n = 10 / Trans.factor;
			for (int i = -n; i < n+1; i++)
			{	for (int j = -n; j < n + 1; j++)
				{	int x = Trans.geefx(i, j);
					int y = Trans.geefy(i, j);
					if (Math.abs(i + j) <= n)
						gIm.drawLine(85 + x, owner.hoogte - 85 - y, 85 + x, owner.hoogte - 85 - y);
				}
			}
						
		}
		else
		{	
			gIm.setColor(Color.white);
			gIm.fillRect(15, owner.hoogte - 165, 160, 160);
		
			if (owner.basisvOud != null)
			{	gIm.setColor(owner.basisvOud.kleur);
				gIm.fillPolygon(owner.basisvOud.pol);
			}
		
			gIm.setColor(Color.black);
			for (int j = 0; j < 9; j++)	
				gIm.drawLine(15, owner.hoogte - 5 - 20 * j, 175, owner.hoogte - 5 - 20 * j);
			for (int j = 0; j < 9; j++)	
				gIm.drawLine(15 + 20 * j, owner.hoogte - 5, 15 + 20*j, owner.hoogte - 165);
		
			for (int i = 0; i < 9; i++)	
			{	gIm.drawString(owner.abc[i], 13 + 20 * i, owner.hoogte - 168);
			}
			for (int i = 0; i < 9 ; i++)	
			{	gIm.drawString(Integer.toString(i + 1), 5, owner.hoogte - 162 + 20 * i);
			}
		}
	}
	
	void tekenStukken(Graphics g)
	{	for(int i = owner.aantalSs - 1; i > -1; i--)
		{	tekenSs(owner.ss[i], g);
		}
	}	
	
	void tekenSs(SchuifStuk s, Graphics gIm)
	{	gIm.setColor(s.kleur);
		gIm.fillPolygon(s.pol);
		gIm.setColor(Color.black);
		gIm.drawPolygon(s.pol);
	}
	
	void tekenPunten(Graphics gIm)
	{	
		if (owner.transVersion)
		{	for (int i = 0; i < owner.aantalNieuwHp; i++)
			{	int x = Trans.geefx(owner.nieuwHp[i].x, owner.nieuwHp[i].y);
				int y = Trans.geefy(owner.nieuwHp[i].x, owner.nieuwHp[i].y);
			   
				gIm.fillOval(owner.posBasis.x + x - 3, owner.posBasis.y + y - 3, 6, 6);
			}
		}
		else
		{	
			for (int i = 0; i < owner.aantalNieuwHp; i++)
			{	gIm.fillOval(owner.posBasis.x + owner.nieuwHp[i].x - 3, 
							 owner.posBasis.y + owner.nieuwHp[i].y - 3, 6, 6);
			}
		}
	}
/*	
	void vermenigvuldigPunten(double factor)
	{	for (int i = 0; i < aantalNieuwHp; i++)
		{	nieuwHp[i].x *= factor;
			nieuwHp[i].y *= factor;
		}
		basisv = new SchuifStuk(aantalNieuwHp, nieuwHp, posBasis, Color.red);
	}
*/	
	void tekenLijnen(Graphics gIm)
	{	
		if (owner.transVersion)
		{	if (owner.aantalNieuwHp > 1)
			{	gIm.setColor(Color.red);
			for (int i = 0; i < owner.aantalNieuwHp - 1; i++)
			{	int x = Trans.geefx(owner.nieuwHp[i].x, owner.nieuwHp[i].y);
				int y = Trans.geefy(owner.nieuwHp[i].x, owner.nieuwHp[i].y);
				int xn = Trans.geefx(owner.nieuwHp[i + 1].x, owner.nieuwHp[i + 1].y);
				int yn = Trans.geefy(owner.nieuwHp[i + 1].x, owner.nieuwHp[i + 1].y);
				gIm.drawLine(owner.posBasis.x + xn , owner.posBasis.y + yn ,
							 owner.posBasis.x + x, owner.posBasis.y + y);
			}
			gIm.setColor(Color.black);
	}
			
		}
		else
		{
			if (owner.aantalNieuwHp > 1)
			{	gIm.setColor(Color.red);
				for (int i = 0; i < owner.aantalNieuwHp - 1; i++)
				{	gIm.drawLine(owner.posBasis.x + owner.nieuwHp[i + 1].x , 
								 owner.posBasis.y + owner.nieuwHp[i + 1].y ,
								 owner.posBasis.x + owner.nieuwHp[i].x, 
								 owner.posBasis.y + owner.nieuwHp[i].y);
				}
				gIm.setColor(Color.black);
			}
		}
	}
	
}

