package fi.tekenveelvlakopdr;

import java.awt.*;

import javax.swing.JPanel;

class VaktekPanel extends JPanel
{	
	int breedte, hoogte;
	int vakBreedte;
	Viewer3d va, ba, ra, la;
	VaktekRooster vr;
	
	boolean vlakkenKleurenOptie = false;
    boolean profielenKleurenOptie = true;
    //boolean viewerKleurenOptie = false;
	
	public VaktekPanel(int x, int y, int b, int h)
	{	
		
		this(new Veelvlak(),new Veelvlak(),new Veelvlak(),new Veelvlak(),x,y,b,h);
/*		
		setBounds(x,y,b,h);
		setLayout(null);
		setBackground(Color.white);
		
		breedte = b;
		hoogte = h;
		vakBreedte = Math.min((breedte-6)/3, (hoogte-6)/2);

		vr = new VaktekRooster();
		add(vr);
*/		
		//Veelvlak dummy = new Veelvlak();
		//this(dummy,dummy,dummy,dummy,x,y,b,h);
		
	}	
	
	public VaktekPanel(Veelvlak vva, Veelvlak vra, Veelvlak vla ,Veelvlak  vba, int x, int y, int b, int h)
	{	setBounds(x,y,b,h);
		setLayout(null);
		setBackground(Color.white);
		
		breedte = b;
		hoogte = h;
		vakBreedte = Math.min((breedte-6)/3, (hoogte-6)/2);
				
		la = new Viewer3d(vla, breedte/2-3*vakBreedte/2+1, hoogte/2+1, vakBreedte-1, vakBreedte-1);
		la.k = 230;
		la.zetAfstand(10000000);
		la.zetSchaduw(false);
		la.zetBeginHoeken(0,90);
		la.zetMuisAan(false);
		add(la);
		
		ba = new Viewer3d(vba, breedte/2-vakBreedte/2+1, hoogte/2-vakBreedte+1, vakBreedte-1, vakBreedte-1);
		ba.k = 230;
		ba.zetAfstand(10000000);
		ba.zetSchaduw(false);
		ba.zetBeginHoeken(90,0);
		ba.zetMuisAan(false);
		add(ba);
		
		va = new Viewer3d(vva, breedte/2-vakBreedte/2+1, hoogte/2+1, vakBreedte-1, vakBreedte-1);
		va.k = 230;
		va.zetAfstand(10000000);
		va.zetSchaduw(false);
		va.zetBeginHoeken(0,0);
		va.zetMuisAan(false);
		add(va);
		
		ra = new Viewer3d(vra, breedte/2-vakBreedte/2+vakBreedte+1, hoogte/2+1, vakBreedte-1, vakBreedte-1);
		ra.k = 230;
		ra.zetAfstand(10000000);
		ra.zetSchaduw(false);
		ra.zetBeginHoeken(0,-90);
		ra.zetMuisAan(false);
		add(ra);
		
		vr = new VaktekRooster();
		add(vr);
	}	
	
	public void zetVlakkenKleurenOptie(boolean b)
    {	vlakkenKleurenOptie = b;
    	va.zetVlakkenKleurenOptie(b);
    	ra.zetVlakkenKleurenOptie(b);
    	la.zetVlakkenKleurenOptie(b);
    	ba.zetVlakkenKleurenOptie(b);
    }	
    public void zetProfielenKleurenOptie(boolean b, boolean leerling)
    {	profielenKleurenOptie = b;
    	va.zetProfielenKleurenOptie(b,leerling);
    	ra.zetProfielenKleurenOptie(b,leerling);
    	la.zetProfielenKleurenOptie(b,leerling);
    	ba.zetProfielenKleurenOptie(b,leerling);
    }
    //public void zetViewerKleurenOptie(boolean b)
    //{	viewerKleurenOptie = b;
    //	va.zetViewerKleurenOptie(b);
    //	ra.zetViewerKleurenOptie(b);
    //	la.zetViewerKleurenOptie(b);
    //	ba.zetViewerKleurenOptie(b);
    //}
	
    public String[] getVaKleuren()
    {  	return va.getViewerKleuren();
    }
    public String[] getRaKleuren()
    {  	return ra.getViewerKleuren();
    }
    public String[] getLaKleuren()
    {  	return la.getViewerKleuren();
    }
    public String[] getBaKleuren()
    {  	return ba.getViewerKleuren();
    }

    public void setVaKleuren(String[] kleuren)
    {  	va.setViewerKleuren(kleuren);
    }
    public void setRaKleuren(String[] kleuren)
    {  	ra.setViewerKleuren(kleuren);
    }
    public void setLaKleuren(String[] kleuren)
    {  	la.setViewerKleuren(kleuren);
    }
    public void setBaKleuren(String[] kleuren)
    {  	ba.setViewerKleuren(kleuren);
    }
    
    public boolean evalueer(int aantalVlakkenRood)
    {
		int roodoranjeroodCnt = 0;
		int oranjeroodCnt = 0;

    	String[] viewerKleuren = va.getViewerKleuren();
		for (int i = 0; i < viewerKleuren.length; i++)
		{	if (viewerKleuren[i].equals("roodoranjerood"))
				roodoranjeroodCnt++;
			if (viewerKleuren[i].equals("oranjerood"))
				oranjeroodCnt++;
		}

    	viewerKleuren = ra.getViewerKleuren();
		for (int i = 0; i < viewerKleuren.length; i++)
		{	if (viewerKleuren[i].equals("roodoranjerood"))
				roodoranjeroodCnt++;
			if (viewerKleuren[i].equals("oranjerood"))
				oranjeroodCnt++;
		}
		
    	viewerKleuren = la.getViewerKleuren();
		for (int i = 0; i < viewerKleuren.length; i++)
		{	if (viewerKleuren[i].equals("roodoranjerood"))
				roodoranjeroodCnt++;
			if (viewerKleuren[i].equals("oranjerood"))
				oranjeroodCnt++;
		}
		
    	viewerKleuren = ba.getViewerKleuren();
		for (int i = 0; i < viewerKleuren.length; i++)
		{	if (viewerKleuren[i].equals("roodoranjerood"))
				roodoranjeroodCnt++;
			if (viewerKleuren[i].equals("oranjerood"))
				oranjeroodCnt++;
		}

//System.out.println("vaktek eval ror = " + roodoranjeroodCnt + " or = " + oranjeroodCnt);
//System.out.println("vaktek eval avr = " + aantalVlakkenRood);

		roodoranjeroodCnt = roodoranjeroodCnt / 4;
		oranjeroodCnt = oranjeroodCnt / 4;
		
		return (roodoranjeroodCnt == aantalVlakkenRood) && (oranjeroodCnt == 0);    
		
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
	public void zetVeelvlak(Veelvlak vva, Veelvlak vra, Veelvlak vla ,Veelvlak vba)
	{	la.zetVeelvlak(vla);	
		ba.zetVeelvlak(vba);	
		va.zetVeelvlak(vva);	
		ra.zetVeelvlak(vra);	
	}
						
	class VaktekRooster extends Component
	{	
		public VaktekRooster()
		{	setBounds(0,0,breedte,hoogte);
		}
		public void paint(Graphics g)
		{	g.drawRect(breedte/2-3*vakBreedte/2, hoogte/2, vakBreedte, vakBreedte);
			g.drawRect(breedte/2-vakBreedte/2, hoogte/2-vakBreedte, vakBreedte, vakBreedte);
			g.drawRect(breedte/2-vakBreedte/2, hoogte/2, vakBreedte, vakBreedte);
			g.drawRect(breedte/2-vakBreedte/2+vakBreedte, hoogte/2, vakBreedte, vakBreedte);
		}
	}
}
