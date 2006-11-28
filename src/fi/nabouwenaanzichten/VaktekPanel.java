package fi.nabouwenaanzichten;

import java.awt.*;

class VaktekPanel extends Container
{	
	int breedte, hoogte;
	int vakBreedte;
	Viewer3d va, ba, ra, la;
	VaktekRooster vr;
	NabouwenAanzichtenIF eigenaar;
	KubusRooster kr;
	int aantalViews;
	
	public VaktekPanel(KubusRooster k, int x, int y, int b, int h, int aantalViews, NabouwenAanzichtenIF bd)
	{	this.aantalViews = aantalViews;
		if(aantalViews==2)
		{	setBounds(x,y,b,h);
			setLayout(null);
			setBackground(Color.white);
			kr = k;
			eigenaar = bd;
			breedte = b;
			hoogte = h;
			//vakBreedte = Math.min((breedte-6)/2, (hoogte-40)/2);
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
			
			//va = new Viewer3d(kr, breedte/2-vakBreedte+1, hoogte/2+1, vakBreedte-1, vakBreedte-1, bd);
			va = new Viewer3d(kr, breedte/2-vakBreedte+1,1, vakBreedte-1, vakBreedte-1, bd);
			va.zetAfstand(10000000);
			va.zetSchaduw(false);
			va.zetBeginHoeken(0,0);
			va.zetMuisAan(false);
			add(va);
			
			//ra = new Viewer3d(kr, breedte/2+1, hoogte/2+1, vakBreedte-1, vakBreedte-1, bd);
			ra = new Viewer3d(kr, breedte/2+1, 1, vakBreedte-1, vakBreedte-1, bd);
			ra.zetAfstand(10000000);
			ra.zetSchaduw(false);
			ra.zetBeginHoeken(0,-90);
			ra.zetMuisAan(false);
			add(ra);
			
			vr = new VaktekRooster();
			add(vr);
		}
		else if(aantalViews==3)
		{	setBounds(x,y,b,h);
			setLayout(null);
			setBackground(Color.white);
			kr = k;
			eigenaar = bd;
			breedte = b;
			hoogte = h;
			vakBreedte = Math.min((breedte-6)/2, (hoogte-40)/2);
					
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
	public void zetAchtergrond(Color c)
	{	setBackground(c);
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
		//la.zetPijlAan(b);
		ba.zetPijlAan(b);
	}
	public void zetKubusRooster(KubusRooster kur)
	{	//la.zetKubusRooster(kur);	
		ba.zetKubusRooster(kur);	
		va.zetKubusRooster(kur);	
		ra.zetKubusRooster(kur);
	}
	public void tekenOpnieuw()
	{	ra.tekenOpnieuw();
		ba.tekenOpnieuw();
		va.tekenOpnieuw();
	}
	
	class VaktekRooster extends Component
	{	
		public VaktekRooster()
		{	setBounds(0,0,breedte,hoogte);
		}
		public void paint(Graphics g)
		{	if(aantalViews==2)
			{	g.setColor(Color.black);
				//g.drawRect(breedte/2-vakBreedte, hoogte/2-vakBreedte, vakBreedte, vakBreedte);
				//g.drawRect(breedte/2-vakBreedte, hoogte/2, vakBreedte, vakBreedte);
				//g.drawRect(breedte/2, hoogte/2, vakBreedte, vakBreedte);
				g.drawRect(breedte/2-vakBreedte, 0, vakBreedte, vakBreedte);
				g.drawRect(breedte/2, 0, vakBreedte, vakBreedte);
				Font f = new Font("SansSerrif", Font.BOLD, 12);
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
				g.drawString(sRechts, (breedte+vakBreedte-wv)/2,vakBreedte+h);
			}
			else if(aantalViews==3)
			{	g.setColor(Color.black);
				//g.drawRect(breedte/2-3*vakBreedte/2, hoogte/2, vakBreedte, vakBreedte);
				g.drawRect(breedte/2-vakBreedte, hoogte/2-vakBreedte, vakBreedte, vakBreedte);
				g.drawRect(breedte/2-vakBreedte, hoogte/2, vakBreedte, vakBreedte);
				g.drawRect(breedte/2, hoogte/2, vakBreedte, vakBreedte);
				Font f = new Font("SansSerrif", Font.BOLD, 12);
				g.setFont(f);
				FontMetrics fm = getFontMetrics(f);
				String sVoor = NabouwenAanzichten.rb.getString("voorLabel");
				String sRechts = NabouwenAanzichten.rb.getString("rechtsLabel");
				String sBoven = NabouwenAanzichten.rb.getString("bovenLabel");
				int wv = fm.stringWidth(sVoor);
				int wr = fm.stringWidth(sRechts);
				int wb = fm.stringWidth(sBoven);
				int h = fm.getHeight();
				g.drawString(sBoven, (breedte-vakBreedte-wv)/2,hoogte/2-vakBreedte-3);
				g.drawString(sVoor, (breedte-vakBreedte-wv)/2,hoogte/2+vakBreedte+h);
				g.drawString(sRechts, (breedte+vakBreedte-wv)/2,hoogte/2+vakBreedte+h);
			}
			
		}
		
		
	}
}
