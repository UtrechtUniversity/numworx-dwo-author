package fi.tekenveelvlakopdr;

import java.awt.*;
import java.awt.event.*;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

class VaktekPanel extends JPanel
{	
	TekenVeelvlakInteractiePanel tvip;
	
	int breedte, hoogte;
	int vakBreedte;
	Viewer3d va, ba, ra, la;
	VaktekRooster vr;
	
	boolean vlakkenKleurenOptie = false;
    boolean profielenKleurenOptie = true;
    //boolean viewerKleurenOptie = false;
    String vaktekKleuren = null;
    
    boolean docentModus = false;
    
	JButton kijkNaButton;
	JPanel kijkNaPanel;
	JLabel vinkjeLabel;
	JLabel kruisjeLabel;
	
	public VaktekPanel(int x, int y, int b, int h, TekenVeelvlakInteractiePanel tvip)
	{	
		this(new Veelvlak(),new Veelvlak(),new Veelvlak(),new Veelvlak(),x,y,b,h,tvip);
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
	
	public VaktekPanel(Veelvlak vva, Veelvlak vra, Veelvlak vla ,Veelvlak  vba, int x, int y, int b, int h,
					   TekenVeelvlakInteractiePanel tvip)
	{	
		this.tvip = tvip;
		
		setBounds(x,y,b,h);
		setLayout(null);
		setBackground(Color.white);
		
		breedte = b;
		hoogte = h;
		vakBreedte = Math.min((breedte-6)/3, (hoogte-6)/2);
				
		la = new Viewer3d(vla, breedte/2-3*vakBreedte/2+1, hoogte/2+1, vakBreedte-1, vakBreedte-1,tvip);
		la.k = 230;
		la.zetAfstand(10000000);
		la.zetSchaduw(false);
		la.zetBeginHoeken(0,90);
		la.zetMuisAan(false);
		add(la);
		
		ba = new Viewer3d(vba, breedte/2-vakBreedte/2+1, hoogte/2-vakBreedte+1, vakBreedte-1, vakBreedte-1,tvip);
		ba.k = 230;
		ba.zetAfstand(10000000);
		ba.zetSchaduw(false);
		ba.zetBeginHoeken(90,0);
		ba.zetMuisAan(false);
		add(ba);
		
		va = new Viewer3d(vva, breedte/2-vakBreedte/2+1, hoogte/2+1, vakBreedte-1, vakBreedte-1,tvip);
		va.k = 230;
		va.zetAfstand(10000000);
		va.zetSchaduw(false);
		va.zetBeginHoeken(0,0);
		va.zetMuisAan(false);
		add(va);
		
		ra = new Viewer3d(vra, breedte/2-vakBreedte/2+vakBreedte+1, hoogte/2+1, vakBreedte-1, vakBreedte-1,tvip);
		ra.k = 230;
		ra.zetAfstand(10000000);
		ra.zetSchaduw(false);
		ra.zetBeginHoeken(0,-90);
		ra.zetMuisAan(false);
		add(ra);
		
		vr = new VaktekRooster();
		add(vr);
		
		kijkNaButton = new JButton(TekenVeelvlakOpdr.rb.getString("kijkNaLabel"));
		//kijkNaButton.setFont(theFont);
		kijkNaButton.setBounds(0, 0, 100, 20);
		kijkNaButton.addActionListener(new KijkNaAL());
		
		java.net.URL imageURL = TekenVeelvlakOpdr.class.getResource("resources/goedkrul_en_klein.gif");
		if (imageURL != null) {
		    vinkjeLabel = new JLabel(new ImageIcon(imageURL));
		}
		else {
			System.out.println("Error reading goedkrul_en_klein.gif.");
			vinkjeLabel = new JLabel();
		}
		vinkjeLabel.setBounds(100, 0, 20, 20);
		imageURL = TekenVeelvlakOpdr.class.getResource("resources/foutkruis_klein.gif");
		if (imageURL != null) {
		    kruisjeLabel = new JLabel(new ImageIcon(imageURL));
		}
		else {
			System.out.println("Error reading foutkruis_klein.gif.");
			kruisjeLabel = new JLabel();
		}
		kruisjeLabel.setBounds(100, 0, 20, 20);
		
		vinkjeLabel.setVisible(false);
		kruisjeLabel.setVisible(false);
		
		kijkNaPanel = new JPanel(null);
		kijkNaPanel.setBackground(Color.WHITE);
		//kijkNaPanel.setBounds(getSize().width - 130, getSize().height - 30, 120, 20);
		kijkNaPanel.setBounds(5, 5, 120, 20);
		
		//kijkNaPanel.setSize(120, 20);
		kijkNaPanel.add(kijkNaButton);
		kijkNaPanel.add(vinkjeLabel);
		
		kijkNaPanel.add(kruisjeLabel);
		kijkNaPanel.setVisible(false);
		
		add(kijkNaPanel);

	}	

	class KijkNaAL implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
//System.out.println("vaktek kijkNa");			
			tvip.kijkNa();
			if (tvip.correct)
			{
				vinkjeLabel.setVisible(true);
				kruisjeLabel.setVisible(false);
			}
			else
			{
				vinkjeLabel.setVisible(false);
				kruisjeLabel.setVisible(true);
				
			}
		}
			
	}
	
	public void zetDocentModus(boolean b)
	{	docentModus = b;
		va.zetDocentModus(b);
		ra.zetDocentModus(b);
		la.zetDocentModus(b);
		ba.zetDocentModus(b);
		
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
    
    public void resetColors()
    {
    	va.resetColors();
    	ra.resetColors();
    	la.resetColors();
    	ba.resetColors();
    }
    
    public void resetLeerlingColors()
    {
    	va.resetLeerlingColors();
    	ra.resetLeerlingColors();
    	la.resetLeerlingColors();
    	ba.resetLeerlingColors();
    }

    //public void zetViewerKleurenOptie(boolean b)
    //{	viewerKleurenOptie = b;
    //	va.zetViewerKleurenOptie(b);
    //	ra.zetViewerKleurenOptie(b);
    //	la.zetViewerKleurenOptie(b);
    //	ba.zetViewerKleurenOptie(b);
    //}
	
    public String[] getKleuren()
    {
    	String[] vaKleuren = getVaKleuren();
    	String[] raKleuren = getVaKleuren();
    	String[] laKleuren = getVaKleuren();
    	String[] baKleuren = getVaKleuren();
    	String[] result = new String[vaKleuren.length];
    	for (int cCnt = 0; cCnt < vaKleuren.length; cCnt++)
    	{	result[cCnt] = vaKleuren[cCnt];
    		if (raKleuren[cCnt].equals("roodoranje"))
    			result[cCnt] = "roodoranje";
    		if (laKleuren[cCnt].equals("roodoranje"))
    			result[cCnt] = "roodoranje";
    		if (baKleuren[cCnt].equals("roodoranje"))
    			result[cCnt] = "roodoranje";
    		
    	}
    	
    	
    	return result;
    }
    
    public String[] getVaKleuren()
    {  	return va.getKleuren();
    }
    public String[] getRaKleuren()
    {  	return ra.getKleuren();
    }
    public String[] getLaKleuren()
    {  	return la.getKleuren();
    }
    public String[] getBaKleuren()
    {  	return ba.getKleuren();
    }

    public void setVaktekKleuren(String[] kleuren)
    {  	setVaKleuren(kleuren);
    	setRaKleuren(kleuren);
    	setLaKleuren(kleuren);
    	setBaKleuren(kleuren);
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
    
    public void updateViewerKleuren()
    {	va.updateViewerKleuren();
    	ra.updateViewerKleuren();
    	la.updateViewerKleuren();
    	ba.updateViewerKleuren();
    	
    }
    
    public boolean evalueer(int aantalVlakkenRood)
    {
    	
    	boolean correct = va.evalueer() && ra.evalueer() && la.evalueer() && ba.evalueer();
    	return correct;
    	
/*    	
		int roodoranjeroodCnt = 0;
		int oranjeroodCnt = 0;

    	String[] viewerKleuren = va.getViewerKleuren();
		for (int i = 0; i < viewerKleuren.length; i++)
		{	if (viewerKleuren[i].equals("roodoranjerood"))
				roodoranjeroodCnt++;
			if (viewerKleuren[i].equals("oranjerood"))
				oranjeroodCnt++;
			
		}
System.out.println("vaktek eval ror = va " + roodoranjeroodCnt + " or = " + oranjeroodCnt);
		
    	viewerKleuren = ra.getViewerKleuren();
		for (int i = 0; i < viewerKleuren.length; i++)
		{	if (viewerKleuren[i].equals("roodoranjerood"))
				roodoranjeroodCnt++;
			if (viewerKleuren[i].equals("oranjerood"))
				oranjeroodCnt++;
		}
System.out.println("vaktek eval ror = ra " + roodoranjeroodCnt + " or = " + oranjeroodCnt);		
    	viewerKleuren = la.getViewerKleuren();
		for (int i = 0; i < viewerKleuren.length; i++)
		{	if (viewerKleuren[i].equals("roodoranjerood"))
				roodoranjeroodCnt++;
			if (viewerKleuren[i].equals("oranjerood"))
				oranjeroodCnt++;
		}
System.out.println("vaktek eval ror = la " + roodoranjeroodCnt + " or = " + oranjeroodCnt);		
    	viewerKleuren = ba.getViewerKleuren();
		for (int i = 0; i < viewerKleuren.length; i++)
		{	if (viewerKleuren[i].equals("roodoranjerood"))
				roodoranjeroodCnt++;
			if (viewerKleuren[i].equals("oranjerood"))
				oranjeroodCnt++;
		}
System.out.println("vaktek eval ror = ba " + roodoranjeroodCnt + " or = " + oranjeroodCnt);
		
System.out.println("vaktek eval ror = " + roodoranjeroodCnt + " or = " + oranjeroodCnt);
System.out.println("vaktek eval avr = " + aantalVlakkenRood);

		//roodoranjeroodCnt = roodoranjeroodCnt / 4;
		//oranjeroodCnt = oranjeroodCnt / 4;
		
		return (roodoranjeroodCnt == aantalVlakkenRood) && (oranjeroodCnt == 0);    
*/		
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
