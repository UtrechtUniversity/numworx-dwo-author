package fi.nabouwenaanzichten;

import java.awt.*;

import javax.swing.*;

class VaktekPanel extends JPanel //Container
{	
	int breedte, hoogte;
	int vakBreedte;
	Viewer3d va, ba, ra, la;
	VaktekRooster vr;
	NabouwenAanzichtenIF eigenaar;
	KubusRooster kr;
	int aantalViews;
	
	final int BOVEN = 0;
	final int VOOR = 1;
	final int RECHTS = 2;
	//final int LINKS = 3;
	int typeAanzicht = BOVEN;
	
	
	public VaktekPanel(KubusRooster k, int x, int y, int b, int h, int aantalViews, NabouwenAanzichtenIF bd)
	{	this.aantalViews = aantalViews;
		setBounds(x, y, b, h);
		setLayout(null);
		setOpaque(true);
		setBackground(Color.white);
		kr = k;
		eigenaar = bd;
		breedte = b;
		hoogte = h;
		
		if (aantalViews == 2)
		{	
			vakBreedte = Math.min((breedte-6)/2, (hoogte-40));
					
			la = new Viewer3d(kr, breedte/2-3*vakBreedte/2+1, hoogte/2+1, vakBreedte-1, vakBreedte-1, bd);
			la.zetAfstand(10000000);
			la.zetSchaduw(false);
			la.zetBeginHoeken(0,90);
			la.zetMuisAan(false);
			//add(la);
			
			ba = new Viewer3d(kr, breedte/2-vakBreedte+1, hoogte/2-vakBreedte+1, vakBreedte-1, vakBreedte-1, bd);
			ba.zetAfstand(10000000);
			ba.zetSchaduw(false);
			ba.zetBeginHoeken(90,0);
			ba.zetMuisAan(false);
			ba.zetPijlAan(false);
			//add(ba);
			
			va = new Viewer3d(kr, breedte / 2 - vakBreedte + 1, 1, vakBreedte - 1, vakBreedte - 1, bd);
			va.zetAfstand(10000000);
			va.zetSchaduw(false);
			va.zetBeginHoeken(0,0);
			va.zetMuisAan(false);
			add(va);
			
			//ra = new Viewer3d(kr, breedte/2+1, hoogte/2+1, vakBreedte-1, vakBreedte-1, bd);
			ra = new Viewer3d(kr, breedte/2 + 1, 1, vakBreedte - 1, vakBreedte-1, bd);
			ra.zetAfstand(10000000);
			ra.zetSchaduw(false);
			ra.zetBeginHoeken(0,-90);
			ra.zetMuisAan(false);
			add(ra);
			
			vr = new VaktekRooster();
			add(vr);
		}
		// boven, voor, rechts
		else if (aantalViews == 3)
		{	
			vakBreedte = Math.min((breedte-6)/2, (hoogte-40)/2);
					
			la = new Viewer3d(kr, breedte / 2-3 * vakBreedte/2+1, hoogte/2+1, vakBreedte-1, vakBreedte-1, bd);
			la.zetAfstand(10000000);
			la.zetSchaduw(false);
			la.zetBeginHoeken(0,90);
			la.zetMuisAan(false);
			//add(la);
			
			ba = new Viewer3d(kr, breedte/2-vakBreedte+1, hoogte/2-vakBreedte+1, vakBreedte-1, vakBreedte-1, bd);
			ba.zetAfstand(10000000);
			ba.zetSchaduw(false);
			ba.zetBeginHoeken(90,0);
			ba.zetMuisAan(false);
			ba.zetPijlAan(false);
			
			add(ba);
			
			va = new Viewer3d(kr, breedte/2-vakBreedte+1, hoogte/2+1, vakBreedte-1, vakBreedte-1, bd);
			va.zetAfstand(10000000);
			va.zetSchaduw(false);
			va.zetBeginHoeken(0,0);
			va.zetMuisAan(false);
			add(va);
			
			ra = new Viewer3d(kr, breedte/2+1, hoogte/2+1, vakBreedte-1, vakBreedte-1, bd);
			ra.zetAfstand(10000000);
			ra.zetSchaduw(false);
			ra.zetBeginHoeken(0,-90);
			ra.zetMuisAan(false);
			add(ra);
			
			vr = new VaktekRooster();
			add(vr);
		}
		
	}	
	
	public void zetDrieAanzichten()
	{	aantalViews = 3;
		
		remove(ba);
		remove(va);
		remove(ra);
		
		remove(vr);
		
		vakBreedte = Math.min((breedte-6)/2, (hoogte-40)/2);
	
		la = new Viewer3d(kr, breedte / 2-3 * vakBreedte/2+1, hoogte/2+1, vakBreedte-1, vakBreedte-1, eigenaar);
		la.zetAfstand(10000000);
		la.zetSchaduw(false);
		la.zetBeginHoeken(0,90);
		la.zetMuisAan(false);
		//add(la);
	
		ba = new Viewer3d(kr, breedte/2-vakBreedte+1, hoogte/2-vakBreedte+1, vakBreedte-1, vakBreedte-1, eigenaar);
		ba.zetAfstand(10000000);
		ba.zetSchaduw(false);
		ba.zetBeginHoeken(90,0);
		ba.zetMuisAan(false);
		ba.zetPijlAan(false);
		add(ba);
	
		va = new Viewer3d(kr, breedte/2-vakBreedte+1, hoogte/2+1, vakBreedte-1, vakBreedte-1, eigenaar);
		va.zetAfstand(10000000);
		va.zetSchaduw(false);
		va.zetBeginHoeken(0,0);
		va.zetMuisAan(false);
		add(va);
	
		ra = new Viewer3d(kr, breedte/2+1, hoogte/2+1, vakBreedte-1, vakBreedte-1, eigenaar);
		ra.zetAfstand(10000000);
		ra.zetSchaduw(false);
		ra.zetBeginHoeken(0,-90);
		ra.zetMuisAan(false);
		add(ra);
	
		vr = new VaktekRooster();
		add(vr);

		repaint();
	}
	
	public void zetEenAanzicht(int type)
	{	aantalViews = 1;
		typeAanzicht = type;
	
		remove(ba);
		remove(va);
		remove(ra);
	
		remove(vr);
		
		//vakBreedte = Math.min(breedte - 80, hoogte - 80);
		vakBreedte = Math.min(breedte, hoogte);
        
		if (type == BOVEN)
		{
			ba = new Viewer3d(kr, (breedte - vakBreedte) / 2, (hoogte - vakBreedte) /2, 
					   			   vakBreedte, vakBreedte, eigenaar);
			ba.zetAfstand(10000000);
			ba.zetSchaduw(false);
			ba.zetBeginHoeken(90,0);
			ba.zetMuisAan(false);
			ba.zetPijlAan(false);
			add(ba);
			
		}
		else if (type == VOOR)
		{
			va = new Viewer3d(kr, (breedte - vakBreedte) / 2, (hoogte - vakBreedte) /2, 
		   			   			   vakBreedte, vakBreedte, eigenaar);
			va.zetAfstand(10000000);
			va.zetSchaduw(false);
			va.zetBeginHoeken(0,0);
			va.zetMuisAan(false);
			add(va);
			
		}
		else if (type == RECHTS)
		{
			ra = new Viewer3d(kr, (breedte - vakBreedte) / 2, (hoogte - vakBreedte) /2, 
								   vakBreedte, vakBreedte, eigenaar);
			ra.zetAfstand(10000000);
			ra.zetSchaduw(false);
			ra.zetBeginHoeken(0,-90);
			ra.zetMuisAan(false);
			add(ra);
			
		}

		vr = new VaktekRooster();
		//add(vr);
		
		repaint();

	}
	public void zetAchtergrond(Color c)
	{	//setBackground(c);
		va.zetAchtergrond(c);
		ra.zetAchtergrond(c);
		la.zetAchtergrond(c);
		ba.zetAchtergrond(c);
	}
	public void zetKlikAan(boolean b)
	{	va.zetKlikAan(b);
		ra.zetKlikAan(b);
		la.zetKlikAan(b);
		ba.zetKlikAan(b);
	}
	public void zetPijlAan(boolean b)
	{	va.zetPijlAan(b);
		ra.zetPijlAan(b);
		la.zetPijlAan(b);
		ba.zetPijlAan(b);
	}
	public void zetKubusRooster(KubusRooster kur)
	{	kr = kur;
		la.zetKubusRooster(kur);	
		ba.zetKubusRooster(kur);	
		va.zetKubusRooster(kur);	
		ra.zetKubusRooster(kur);
	}
	public void tekenOpnieuw()
	{	ra.tekenOpnieuw();
		ba.tekenOpnieuw();
		va.tekenOpnieuw();
	}
	
	class VaktekRooster extends JComponent
	{	
		public VaktekRooster()
		{	setBounds(0,0,breedte,hoogte);
		}
		public void paint(Graphics g)
		{	Font f = new Font("SansSerrif", Font.BOLD, 12);
			if (vakBreedte < 70)
				f = new Font("SansSerrif", Font.PLAIN, 10);
			
			if (aantalViews == 1)
			{	g.drawRect((breedte - vakBreedte) / 2 - 1, (hoogte - vakBreedte) / 2 - 1, 
				   		   vakBreedte + 1, vakBreedte + 1);

				g.setFont(f);
				FontMetrics fm = getFontMetrics(f);
				String sVoor = NabouwenAanzichten.rb.getString("voorLabel");
				String sRechts = NabouwenAanzichten.rb.getString("rechtsLabel");
				String sBoven = NabouwenAanzichten.rb.getString("bovenLabel");
				int wv = fm.stringWidth(sVoor);
				int wr = fm.stringWidth(sRechts);
				int wb = fm.stringWidth(sBoven);
				int h = fm.getHeight();
				
				if (typeAanzicht == BOVEN)
				{
					g.drawString(sBoven, (breedte - wb) / 2, (hoogte - vakBreedte) / 2 - h - 3);
				}
				else if (typeAanzicht == VOOR)
				{
					g.drawString(sVoor, (breedte - wb) / 2, (hoogte - vakBreedte) / 2 - h - 3);
				}
				else if (typeAanzicht == RECHTS)
				{
					g.drawString(sRechts, (breedte - wb) / 2, (hoogte - vakBreedte) / 2 - h - 3);
				}
				
			}
			else if (aantalViews == 2)
			{	g.setColor(Color.black);
				//g.drawRect(breedte/2-vakBreedte, hoogte/2-vakBreedte, vakBreedte, vakBreedte);
				//g.drawRect(breedte/2-vakBreedte, hoogte/2, vakBreedte, vakBreedte);
				//g.drawRect(breedte/2, hoogte/2, vakBreedte, vakBreedte);
				g.drawRect(breedte/2-vakBreedte, 0, vakBreedte, vakBreedte);
				g.drawRect(breedte/2, 0, vakBreedte, vakBreedte);
				g.setFont(f);
				FontMetrics fm = getFontMetrics(f);
				String sVoor = NabouwenAanzichten.rb.getString("voorLabel");
				String sRechts = NabouwenAanzichten.rb.getString("rechtsLabel");
				String sBoven = NabouwenAanzichten.rb.getString("bovenLabel");
				int wv = fm.stringWidth(sVoor);
				int wr = fm.stringWidth(sRechts);
				int wb = fm.stringWidth(sBoven);
				int h = fm.getHeight();
				//g.drawString(sBoven, (breedte-vakBreedte-wv)/2,hoogte/2-vakBreedte-3);
				//g.drawString(sVoor, (breedte-vakBreedte-wv)/2,hoogte/2+vakBreedte+h);
				//g.drawString(sRechts, (breedte+vakBreedte-wv)/2,hoogte/2+vakBreedte+h);
				g.drawString(sVoor, (breedte-vakBreedte-wv)/2,vakBreedte+h);
				g.drawString(sRechts, (breedte+vakBreedte-wr)/2,vakBreedte+h);
			}
			else if (aantalViews == 3)
			{	g.setColor(Color.black);
				//g.drawRect(breedte/2-3*vakBreedte/2, hoogte/2, vakBreedte, vakBreedte);
				g.drawRect(breedte/2-vakBreedte, hoogte/2-vakBreedte, vakBreedte, vakBreedte);
				g.drawRect(breedte/2-vakBreedte, hoogte/2, vakBreedte, vakBreedte);
				g.drawRect(breedte/2, hoogte/2, vakBreedte, vakBreedte);
				g.setFont(f);
				FontMetrics fm = getFontMetrics(f);
				String sVoor = NabouwenAanzichten.rb.getString("voorLabel");
				String sRechts = NabouwenAanzichten.rb.getString("rechtsLabel");
				String sBoven = NabouwenAanzichten.rb.getString("bovenLabel");
				int wv = fm.stringWidth(sVoor);
				int wr = fm.stringWidth(sRechts);
				int wb = fm.stringWidth(sBoven);
				int h = fm.getAscent();
				g.drawString(sBoven, (breedte-vakBreedte-wb)/2,hoogte/2-vakBreedte-3);
				g.drawString(sVoor, (breedte-vakBreedte-wv)/2,hoogte/2+vakBreedte+h);
				g.drawString(sRechts, (breedte+vakBreedte-wr)/2,hoogte/2+vakBreedte+h);
			}
			
		}
		
		
	}
}
