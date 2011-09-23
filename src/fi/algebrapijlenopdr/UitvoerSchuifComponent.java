package fi.algebrapijlenopdr;

import java.awt.*;
import java.awt.event.*;
import java.util.Hashtable;

import javax.swing.*;

import fi.algebrapijlenopdr.expressies_ap.*;

public class UitvoerSchuifComponent extends AlgebraSchuifComponent implements ActionListener, FocusListener
{	
	private JTextField tf;
	private Expressie expressie;
	private Expressie verborgenExpressie;
	private BasisExpressie beginw;
	private String waardeString;
	private boolean toonWaarde;
	private boolean labelZichtbaar;
	private InUitvoerLabel label;
	boolean tabelZichtbaar;
	private boolean tabelAan;
	
	boolean zoomInTabel;
	
	private boolean grafiek;
	boolean muisrechts;
	private GrafiekComponent grafiekComponent;
	private TabelComponent tabel;
	private int tabelCorr;
	Font f;
	FontMetrics fm;
	private JPopupMenu popup;
	
	private PlusMinKnop plusMinKnop;
	boolean scrollable = true;
	int scrollCorr = 0;
	
	public boolean kettingZichtbaar = true;
	
	private ZoomKnop zoomInKnop;
	private ZoomKnop zoomUitKnop;
	private double schaalFactorX=1;
	private int factorRijNummerX=99;
	private int beginwaarde;
	private int selectnummer;
	private double beginx;
	private String defaultVarnaam = "qq"+1000*Math.random();
	
	private Color vakKleur, vakKleurSoft;
	
	public UitvoerSchuifComponent(AlgebraSchuifVeld asv,int x, int y, int b, int h)
	{	super(1,asv,x,y,b,h);
		
		toonWaarde = !((AlgebraSchuifVeld) schuifveld).ip.isExpr();
		labelZichtbaar = false;
		tabelZichtbaar = false;
		
		zoomInTabel = true;
		
		waardeString = "";
		f = new Font("SansSerrif",Font.PLAIN,12);
		fm = getFontMetrics(f);
		
		label = new InUitvoerLabel();
		tabel = new TabelComponent();
		tabel.setDefaultVarnaam(defaultVarnaam);
		
		tf = new JTextField();
		tf.setFont(f);
		if (!links)
			tf.setBounds(12,0,35,20);
		else 
			tf.setBounds(2,0,35,20);
		tf.addActionListener(this);
		tf.addFocusListener(this);
		tf.setVisible(false);
		tf.setEnabled(false);
		//add(tf);
		
		popup = new JPopupMenu();
					
		JMenuItem mi = new JMenuItem(AlgebraPijlenOpdr.rb.getString("popup1Label1"));
		mi.addActionListener(this);
		popup.add(mi);
		
		mi = new JMenuItem(AlgebraPijlenOpdr.rb.getString("popup1Label2"));
		mi.addActionListener(this);
		popup.add(mi);
		
		popup.addSeparator();
		
		mi = new JMenuItem(AlgebraPijlenOpdr.rb.getString("popup1Label3"));
		mi.addActionListener(this);
		popup.add(mi);
		
		mi = new JMenuItem(AlgebraPijlenOpdr.rb.getString("popup1Label4"));
		mi.addActionListener(this);
		popup.add(mi);
		
		popup.addSeparator();
		
		mi = new JMenuItem(AlgebraPijlenOpdr.rb.getString("popup1Label5"));
		mi.addActionListener(this);
		popup.add(mi);
		
		mi = new JMenuItem(AlgebraPijlenOpdr.rb.getString("popup1Label6"));
		mi.addActionListener(this);
		popup.add(mi);
		
		add(popup);
		
		verborgenExpressie = new BasisExpressie(defaultVarnaam);
		
		if (!links)
		{	plusMinKnop = new PlusMinKnop(b-12,1,10,h-2, PlusMinKnop.VERTIKAAL);
		}
		else
		{	plusMinKnop = new PlusMinKnop(b-22,1,10,h-2, PlusMinKnop.VERTIKAAL);
		}
		plusMinKnop.addActionListener(this);
		add(plusMinKnop);
		
		zoomInKnop	= new ZoomKnop("zoominxsmal");
		zoomInKnop.setBounds(10,50,10,25);
		zoomInKnop.addActionListener(this);
		add(zoomInKnop);
		
		zoomUitKnop	= new ZoomKnop("zoomuitxsmal");
		zoomUitKnop.setBounds(10,100,10,25);
		zoomUitKnop.addActionListener(this);
		add(zoomUitKnop);
	}
	
	public Hashtable getState()
	{	String basisExp  = null;
		String defaultVarnaam = null;
		boolean tabelAan = false;
		boolean labelZichtbaar = false;
        boolean kettingZichtbaar = true;
		String labelTekst = null;
		
		if (beginw != null)
			basisExp = this.beginw.basisString;
		else 
			basisExp = "";
		defaultVarnaam = this.defaultVarnaam;
		tabelAan = this.tabelAan;
		labelZichtbaar = this.labelZichtbaar;
        kettingZichtbaar = this.kettingZichtbaar;
		labelTekst = label.geefTekst();
		
		Hashtable h = super.getState();
		
	    h.put("basisExp", basisExp);
	    h.put("defaultVarnaam", defaultVarnaam);
	    h.put("tabelAan", new Boolean(tabelAan));
	    h.put("labelZichtbaar", new Boolean(labelZichtbaar));
        h.put("kettingZichtbaar", new Boolean(kettingZichtbaar));
        h.put("labelTekst", labelTekst);
        
        h.put("scrollable", new Boolean(scrollable));
        h.put("zoomInTabel", new Boolean(zoomInTabel));
        
	    return h;
	}

    public void setState(Hashtable h)
    {	String basisExp  = null;
   		String defaultVarnaam = null;
        boolean tabelAan = false;
        boolean labelZichtbaar = false;
        boolean kettingZichtbaar = true;
        String labelTekst = null;
        
        boolean scrollable = true;
        boolean zoomInTabel = true;
        
    	if (h.containsKey("basisExp")) 
    		basisExp = (String) h.get("basisExp");
    	if (h.containsKey("defaultVarnaam")) 
    		defaultVarnaam = (String) h.get("defaultVarnaam");
        if (h.containsKey("tabelAan")) 
        	tabelAan = ((Boolean) h.get("tabelAan")).booleanValue();		
        if (h.containsKey("labelZichtbaar")) 
        	labelZichtbaar = ((Boolean) h.get("labelZichtbaar")).booleanValue();
        if (h.containsKey("kettingZichtbaar")) 
        	kettingZichtbaar = ((Boolean) h.get("kettingZichtbaar")).booleanValue();
        if (h.containsKey("labelTekst")) 
        	labelTekst = (String) h.get("labelTekst");
        
        if (h.containsKey("scrollable")) 
        	scrollable = ((Boolean) h.get("scrollable")).booleanValue();
        if (h.containsKey("zoomInTabel")) 
        	zoomInTabel = ((Boolean) h.get("zoomInTabel")).booleanValue();
        
		if (!basisExp.equals(""))
			beginw = new BasisExpressie(basisExp);
		if (defaultVarnaam != null) 
		{	this.defaultVarnaam = defaultVarnaam;
			verborgenExpressie = new BasisExpressie(defaultVarnaam);
		}
		zetTabelAan(tabelAan);
		zetLabel(labelZichtbaar);
		label.zetLabelTekst(labelTekst);
        if (!kettingZichtbaar)
        	zetKettingZichtbaarHier(kettingZichtbaar);
        
        zetScroll(scrollable);
        zetZoomInTabel(zoomInTabel);

        super.setState(h);
		
		zetMaat();
		
    }
    public void zetScroll(boolean b)
	{	scrollable = b;
	
		zetVeranderd(20);
	
		zetMaat();
	}
    
    public void zetZoomInTabel(boolean b)
    {
    	zoomInTabel = b;
    	zoomInKnop.setVisible(b);
    	zoomUitKnop.setVisible(b);
    }
    
	public void zetLinks(boolean b)
	{	links = b;
		label.zetLinks(b);
		if(!links)tf.setBounds(12,0,35,20);
		else tf.setBounds(2,0,35,20);
		for(int i=0 ; i<aantalPu ; i++)
		{	pijlUit[i].zetLinks(b);
			if(!links)pijlUit[i].zetPlaats(getLocation().x + getSize().width+9 ,getLocation().y + 10 );
			else pijlUit[i].zetPlaats(getLocation().x - 10 ,getLocation().y + 10 );
		}
		if(!links)
		{	plusMinKnop.setLocation(getSize().width-12,1);
		}
		else
		{	plusMinKnop.setLocation(getSize().width-22,1);
		}
		schuifveld.tekenOpnieuw();

	}
	
	public void zetVakKleur(Color color)
	{	vakKleur = color;
	 	vakKleurSoft = new Color((color.getRed()+765)/4, (color.getGreen()+765)/4, (color.getBlue()+765)/4);
		if(color==Color.black) vakKleur = null;
	}
	
	public void paint(Graphics g)
  	{ 	Color achtergrondkleur = Color.white;
		if (pijlIn1 != null)
			achtergrondkleur = new Color(220, 220, 220);
		
		if (!links)
		{	g.setColor(Color.gray);
			g.fillRect(10, 0, getSize().width - 11, getSize().height - 1);
			g.setColor(Color.black);
			g.drawRect(10, 0, getSize().width - 11, getSize().height - 1);
						
			super.paint(g);

			int labelCorr = 0;
			tabelCorr = 0;
			if (labelZichtbaar)
				labelCorr = 20;
			if (tabelZichtbaar)
				tabelCorr = 152;

			if (tf.isVisible())
				return;
			
			if (vakKleur != null) 
				g.setColor(vakKleurSoft);
			else 
				g.setColor(achtergrondkleur);
			
			g.fillRect(12, labelCorr + 2, getSize().width - 15 - scrollCorr, getSize().height - labelCorr - tabelCorr - 5);
			g.setColor(Color.black);
			g.drawRect(12, labelCorr + 2, getSize().width - 15 - scrollCorr, getSize().height - labelCorr - tabelCorr - 5);
		
			g.setFont(f);
			if (expressie != null && kettingZichtbaar)
			{	expressie.zetMaat(fm);
				if (toonWaarde && expressie.geefWaarde() != null)
					g.drawString(waardeString, 5+(getSize().width-scrollCorr-fm.stringWidth(waardeString))/2, 
								 getSize().height-tabelCorr-5);
				else 
					expressie.teken(g, 5+(getSize().width-scrollCorr-expressie.breedte)/2, 
							           7 + (labelCorr+getSize().height-tabelCorr-15 - expressie.hoogte)/2);
			}
			else if (expressie != null && expressie.geefVarNaam() != null && 
					 !(expressie instanceof Functie) && !kettingZichtbaar)
			{	Expressie functie = new Functie(new BasisExpressie(expressie.geefVarNaam()),expressie);
				functie.zetMaat(fm);
				functie.teken(g, 5+(getSize().width-scrollCorr-functie.breedte)/2, 
						         7 + (labelCorr+getSize().height-tabelCorr-15 - functie.hoogte)/2);
			}
				
		}
		else // links
		{	g.setColor(Color.gray);
			g.fillRect(0,0,getSize().width-11,getSize().height-1);
			g.setColor(Color.black);
			g.drawRect(0,0,getSize().width-11,getSize().height-1);
			
			super.paint(g);
		
			int labelCorr = 0;
			tabelCorr = 0;
			if (labelZichtbaar)
				labelCorr = 20;
			if (tabelZichtbaar)
				tabelCorr = 152;
			
			if (tf.isVisible())
				return;
			
			if (vakKleur != null) 
				g.setColor(vakKleur);
			else 
				g.setColor(achtergrondkleur);
			g.fillRect(2,labelCorr+2,getSize().width-15-scrollCorr,getSize().height-labelCorr-tabelCorr-5);
			g.setColor(Color.black);
			g.drawRect(2,labelCorr+2,getSize().width-15-scrollCorr,getSize().height-labelCorr-tabelCorr-5);
		
			g.setFont(f);
			if (expressie != null)
			{	expressie.zetMaat(fm);
				if (toonWaarde && expressie.geefWaarde() != null)
					g.drawString(waardeString, -5 + (getSize().width - scrollCorr - fm.stringWidth(waardeString)) / 2, 
							                   getSize().height - tabelCorr - 5);
				else 
					expressie.teken(g, -5 + (getSize().width - scrollCorr - expressie.breedte) / 2, 
							           7 + (labelCorr+getSize().height - tabelCorr - 15 - expressie.hoogte)/2);
			}
		}	
		
		
	}
	
	public void zetToonWaarde(boolean b)
	{	toonWaarde = b;
		zetMaat();
	}
	
	public void zetLabel(boolean b)
	{	labelZichtbaar = b;
		if(b)
		{	add(label);
			zetMaat();
		}
		else
		{	remove(label);
			zetMaat();
		}
		schuifveld.tekenOpnieuw();
	}
	
	public void toonLabel(boolean b)
	{	labelZichtbaar = b;
		super.toonLabel(b);
		if(b)
		{	add(label);
			zetMaat();
		}
		else
		{	remove(label);
			zetMaat();
		}
		schuifveld.tekenOpnieuw();
	}
	
	public void toonTabel(boolean b)
	{	tabelZichtbaar = b;
		if(b)
		{	add(tabel);
			zetMaat();
		}
		else 
		{	remove(tabel);
			zetMaat();
		}
		schuifveld.tekenOpnieuw();
	}
	
	public boolean isLabelZichtbaar()
	{	return labelZichtbaar;
	}
	
	
	public String geefLabelTekst()
	{	return label.geefTekst();
	}
		
	
	public void setSize(int b, int h)
	{	label.setSize(b-10,20);
		super.setSize(b,h);
	}
	
	public void zetMaat()
	{	
		int b = 50 + scrollCorr;
		int h = 20;
		int corr = 0;
		Expressie expFunctie = expressie;
		if (expressie != null && expressie.geefVarNaam() != null && !(expressie instanceof Functie) && !kettingZichtbaar) 
		{	expFunctie = new Functie(new BasisExpressie(expressie.geefVarNaam()),expressie);
			expFunctie.zetMaat(fm);
		}
		if (expressie != null)
		{	b = expFunctie.breedte + scrollCorr;
			h = expFunctie.hoogte;
			if (toonWaarde && expressie.geefWaarde() != null)
			{	b = fm.stringWidth(waardeString) + scrollCorr;
				h = 0;
			}
			if (b > 26)
				b = b + 24 + scrollCorr;
			else 
				b =  50 + scrollCorr;
			if (h > 12)
				h = 10+((h+5)/10)*10;
			else 
				h = 20;
		}
		if (labelZichtbaar)
		{	corr = 20;
			h=h+20;
			b = Math.max(b,label.geefBreedte()+10);
		}
		if (tabelZichtbaar)
		{	h=h+152;
			b = Math.max(b,tabel.geefBreedte()+10);
		}
		setSize(b,h);
		label.setSize(b-10,20);
		
		if(!links)
		{	tabel.setBounds(20,h-152,b-10,152);
			tf.setBounds(12, corr, b - 15 - scrollCorr, 20);
			plusMinKnop.setLocation(b-12,1+corr);
			zoomInKnop.setBounds(11,h-120,12,25);
			zoomUitKnop.setBounds(11,h-70,12,25);
			
		}
		else
		{	tabel.setBounds(10,h-152,b-10,152);
			tf.setBounds(2, corr, b - 15 - scrollCorr, 20);
			plusMinKnop.setLocation(b-22,1+corr);
			zoomInKnop.setBounds(b-1,h-120,12,25);
			zoomUitKnop.setBounds(b-1,h-70,12,25);
			
		}	

	}
	
	public Expressie geefUitvoer(int max)
	{	return expressie;
	}
	
	public Expressie geefVerborgenUitvoer(int max)
	{	return verborgenExpressie;
	}
	
	public void zetVeranderd(int max)
	{	if (pijlIn1 != null)
		{	remove(plusMinKnop);
			scrollCorr = 0;
			expressie = pijlIn1.zender.geefUitvoer(20);
			verborgenExpressie = pijlIn1.zender.geefVerborgenUitvoer(20);
			zoomInKnop.setVisible(false);
			zoomUitKnop.setVisible(false);
		}
		else 
		{	if (scrollable  && expressie != null && expressie.geefWaarde() != null)
			{	if (scrollCorr == 0)
					add(plusMinKnop);
				scrollCorr = 10;
			}
			else
			{	scrollCorr = 0;
				remove(plusMinKnop);
			}
			expressie = beginw;
			verborgenExpressie = new BasisExpressie(defaultVarnaam);
			zoomInKnop.setVisible(true);
			zoomUitKnop.setVisible(true);
		}
		
		if (!kettingZichtbaar)
		{	zoomInKnop.setVisible(true);
			zoomUitKnop.setVisible(true);
		}
		
		if (expressie != null && expressie.geefVarNaam() != null)
			tabel.zetExpressie(expressie);
		else 
			tabel.zetExpressie(verborgenExpressie);
		
		if (expressie != null)
		{	expressie.zetMaat(fm);
			Double waarde = expressie.geefWaarde();
			if (waarde != null)
				waardeString = Expressie.df.format(waarde);
			else 
				waardeString = "-";
		}
		zetMaat();
		zetTabelAan(tabelAan);
		
		ZoomState zs = null;
		String naam = null;
		if (expressie != null && expressie.geefVarNaam() != null && getParent() instanceof AlgebraSchuifVeld)
		{	zs = ((AlgebraSchuifVeld) getParent()).zoomStateHolder.getZoomState(expressie.geefVarNaam());
			naam  = expressie.geefVarNaam();
		}
		else if (verborgenExpressie != null && verborgenExpressie.geefVarNaam() != null && getParent() instanceof AlgebraSchuifVeld)
		{	zs =((AlgebraSchuifVeld) getParent()).zoomStateHolder.getZoomState(verborgenExpressie.geefVarNaam());
			naam  = verborgenExpressie.geefVarNaam();
		}
		if (zs != null && naam != null)
			setZoomState(naam, zs);
				
		super.zetVeranderd(max);
	}
	
	//public void zetGrafiek(boolean b, GrafiekComponent gc)
	//{	grafiek = b;
	//	//tabel.zetSelectMogelijk(b);
	//	if(b)grafiekComponent = gc;
	//	else grafiekComponent = null;
	//}
	
	/*public void zetTabel(int beginwaarde, int selectnummer, String varN, double schaalFactorX)
	{	this.beginwaarde = beginwaarde;
		this.selectnummer = selectnummer;
		if(tabelZichtbaar)
		{	
			tabel.zetTabel(beginwaarde, selectnummer, varN, schaalFactorX);
			if(grafiekComponent!=null)grafiekComponent.zetTabel(beginwaarde, selectnummer, varN, schaalFactorX);
		}
		zetMaat();
		
	}*/
	
	
	public void setZoomState(String varnaam, ZoomState zoomState)
	{	if(expressie!=null && expressie.geefVarNaam()!=null && expressie.geefVarNaam().equals(varnaam)
			|| verborgenExpressie!=null && verborgenExpressie.geefVarNaam()!=null && verborgenExpressie.geefVarNaam().equals(varnaam))
		{	this.beginwaarde = zoomState.getBeginwaarde();
			this.selectnummer = zoomState.getSelectnummer();
			this.schaalFactorX = zoomState.getSchaalFactorX();
			this.factorRijNummerX = zoomState.getFactorRijNummerX();
			this.beginx = zoomState.getBeginx();
			
			//if(tabelZichtbaar)
			{	tabel.zetTabel(beginwaarde, selectnummer, varnaam, schaalFactorX, beginx);
				//if(grafiekComponent!=null)grafiekComponent.zetTabel(beginwaarde, selectnummer, varnaam, schaalFactorX);
			}
			zetMaat();
		}
		
	}
	
	public void zetTabelAan(boolean b)
	{	tabelAan = b;
		//if(b && !isStapel && (expressie==null ||expressie.geefWaarde()==null))
		if (!isStapel) 
			toonTabel(b);
		//else 
		//toonTabel(false);
	}
	
	public void zetInvulWaarde()
	{	
		ZoomState zs = null;
		if (expressie != null && expressie.geefVarNaam() != null && getParent() instanceof AlgebraSchuifVeld)
		{	zs = ((AlgebraSchuifVeld) getParent()).zoomStateHolder.getZoomState(expressie.geefVarNaam());
			//naam  = expressie.geefVarNaam();
		}
		else if(verborgenExpressie!=null && verborgenExpressie.geefVarNaam()!=null && getParent() instanceof AlgebraSchuifVeld )
		{	zs =((AlgebraSchuifVeld) getParent()).zoomStateHolder.getZoomState(verborgenExpressie.geefVarNaam());
			//naam  = verborgenExpressie.geefVarNaam();
		}
				
		boolean isGeldigeInvoer = true;
		{	try
			{	String s = tf.getText();
				s = s.replace(',','.');
				tf.setText(s);
				Double w = Double.valueOf(tf.getText());
				//((AlgebraSchuifVeld)getParent()).zetTabellen((int)(w.doubleValue()),0,"x",1);
				toonTabel(false);
			}
			catch(NumberFormatException ex)
			{	for (int i = 0; i < tf.getText().length(); i++)
				{	if (!Character.isLetter(tf.getText().charAt(i)))
					{	isGeldigeInvoer = false;
						break;
					}
				}
				if (tf.getText().equals(""))
					isGeldigeInvoer = false;
				if (!isGeldigeInvoer)
				{	tf.setText("");
				}
			}
		}
		if (isGeldigeInvoer)
		{	beginw = new BasisExpressie(tf.getText());
			beginw.zetMaat(fm);
		}
		else
		{	beginw = null;
		}
		expressie = beginw;
		
		if (expressie != null && expressie.geefVarNaam() != null)
			tabel.zetExpressie(expressie);
		else 
			tabel.zetExpressie(verborgenExpressie);
		
		if (zs != null && expressie != null && expressie.geefVarNaam() != null)
			((AlgebraSchuifVeld) getParent()).zoomStateHolder.copyZoomState(expressie.geefVarNaam(), zs);
		else if(zs != null) 
			((AlgebraSchuifVeld) getParent()).zoomStateHolder.copyZoomState(defaultVarnaam, zs);
			
		//zetMaat();
		//zetVeranderd(20);
		tf.setEnabled(false);
		remove(tf);
		tf.setVisible(false);
		
		zetMaat();
		zetVeranderd(20);
		
		schuifveld.tekenOpnieuw();
	}
    
    public void zetKettingZichtbaarHier(boolean b)
    {   if (pijlIn1 != null)
    		pijlIn1.zender.zetKettingZichtbaar(b);
        open = b;
        kettingZichtbaar = b;
        tabel.zetDubbel(!b);
        zetMaat();
        schuifveld.tekenOpnieuw();
    }
	
	public void actionPerformed(ActionEvent e)
	{	if (e.getActionCommand().equals("focus")) 
			schuifveld.tekenOpnieuw();
		else if (e.getSource() == tf)
		{	zetInvulWaarde();
		}		
		else if (e.getSource() == plusMinKnop)
		{	if (beginw != null && beginw.geefWaarde() != null)
			{	double w = beginw.geefWaarde().doubleValue();
				if (e.getActionCommand().equals("min"))
					w -= 1;
				if (e.getActionCommand().equals("plus"))
					w += 1;
				waardeString = Expressie.df.format(w);
				beginw = new BasisExpressie(waardeString);
				tf.setText(waardeString);
				zetVeranderd(20);
			}
		}
		else if (e.getSource() == zoomUitKnop)
		{	if (!e.getActionCommand().equals("knop") || factorRijNummerX > 120) 
				return;
			if (factorRijNummerX % 3 == 1)
			{	schaalFactorX *= 2.5;
				beginx = beginx / 2.5;
			}
			else 
			{	schaalFactorX *= 2;
				beginx = beginx / 2;
			}
			beginx = Math.round(beginx / 14) * 14;
			beginwaarde = -(int) Math.round(beginx / 14);
			selectnummer = 999;
			
			factorRijNummerX++;
            String varnaam = null;
            if (expressie != null) 
            	varnaam = expressie.geefVarNaam();
            if (varnaam == null && verborgenExpressie != null) 
            	varnaam = verborgenExpressie.geefVarNaam();
            
//System.out.println("vn = " + varnaam);            
            
            ((AlgebraSchuifVeld) getParent()).zoomStateHolder.setBeginwaarde(varnaam, beginwaarde);
            ((AlgebraSchuifVeld) getParent()).zoomStateHolder.setSchaalFactorX(varnaam, schaalFactorX);
            ((AlgebraSchuifVeld) getParent()).zoomStateHolder.setFactorRijNummerX(varnaam, factorRijNummerX);
            ((AlgebraSchuifVeld) getParent()).zoomStateHolder.setBeginx(varnaam, beginx);
            ((AlgebraSchuifVeld) getParent()).zoomStateHolder.setZoomStates(varnaam);
            
            schuifveld.tekenOpnieuw();
		}
		else if (e.getSource() == zoomInKnop)
		{	if (!e.getActionCommand().equals("knop") || factorRijNummerX < 87) 
				return;
			if (factorRijNummerX % 3 == 2)
			{	schaalFactorX /= 2.5;
				beginx = beginx * 2.5;
			}
			else 
			{	schaalFactorX /= 2;
				beginx = beginx * 2;
			}
			beginx = Math.round(beginx / 14) * 14;
			beginwaarde = -(int) Math.round(beginx / 14);
			selectnummer = 999;
			
			factorRijNummerX--;
			//((AlgebraSchuifVeld)getParent()).zetTabellen(beginwaarde,selectnummer, "x", schaalFactorX);
            String varnaam = null;
            if (expressie != null) 
            	varnaam = expressie.geefVarNaam();
            if (varnaam == null && verborgenExpressie != null) 
            	varnaam = verborgenExpressie.geefVarNaam();
            ((AlgebraSchuifVeld)getParent()).zoomStateHolder.setBeginwaarde(varnaam, beginwaarde);
            ((AlgebraSchuifVeld)getParent()).zoomStateHolder.setSelectnummer(varnaam, selectnummer);
            ((AlgebraSchuifVeld)getParent()).zoomStateHolder.setSchaalFactorX(varnaam, schaalFactorX);
            ((AlgebraSchuifVeld)getParent()).zoomStateHolder.setFactorRijNummerX(varnaam, factorRijNummerX);
            ((AlgebraSchuifVeld)getParent()).zoomStateHolder.setBeginx(varnaam, beginx);
            ((AlgebraSchuifVeld)getParent()).zoomStateHolder.setZoomStates(varnaam);
            //System.out.println("test1"+varnaam);
            schuifveld.tekenOpnieuw();
		}
		else if(((JMenuItem)e.getSource()).getText().equals(AlgebraPijlenOpdr.rb.getString("popup1Label1")))
		{	toonLabel(true);
		}
		else if(((JMenuItem)e.getSource()).getText().equals(AlgebraPijlenOpdr.rb.getString("popup1Label2")))
		{	toonLabel(false);
		}
		else if(((JMenuItem)e.getSource()).getText().equals(AlgebraPijlenOpdr.rb.getString("popup1Label3")))
		{	zetTabelAan(true);
		}
		else if(((JMenuItem)e.getSource()).getText().equals(AlgebraPijlenOpdr.rb.getString("popup1Label4")))
		{	zetTabelAan(false);
		}
		else if(((JMenuItem)e.getSource()).getText().equals(AlgebraPijlenOpdr.rb.getString("popup1Label5")))
		{	zetKettingZichtbaarHier(true);
			zoomInKnop.setVisible(false);
			zoomUitKnop.setVisible(false);
		}
		else if(((JMenuItem)e.getSource()).getText().equals(AlgebraPijlenOpdr.rb.getString("popup1Label6")))
		{	zetKettingZichtbaarHier(false);
			zoomInKnop.setVisible(true);
			zoomUitKnop.setVisible(true);
		}
	}
	
	public void mousePressed(MouseEvent e)
	{	if (((AlgebraSchuifVeld)schuifveld).fixed)
			return;
		if (((AlgebraSchuifVeld)schuifveld).isDemo)
			return;
	
		requestFocus();
		muisrechts = false;
		if (e.getModifiers()== e.BUTTON3_MASK || e.isControlDown())
		{	
			muisrechts = true;			

			if (((AlgebraSchuifVeld)schuifveld).alleenInvullen)
			{	return;
			}
			if (isStapel)
				return;
			
			//muisrechts = true;
			popup.show(this, e.getX(), e.getY());
			return;
		}
		super.mousePressed(e);
	}
	public void mouseClicked(MouseEvent e)
	{	if (((AlgebraSchuifVeld)schuifveld).fixed)
			return;
		if (((AlgebraSchuifVeld)schuifveld).isDemo)
			return;
	
		if (!muisrechts && pijlIn1 == null)
		{	
			if (new Rectangle(tf.getLocation().x, tf.getLocation().y,
					          tf.getSize().width, tf.getSize().height).contains(e.getX(), e.getY())
				)
			{
				add(tf);
				tf.setVisible(true)	;
				tf.setEnabled(true);
				tf.selectAll();
				tf.requestFocus();
			
//				if (scrollCorr == 0)
//					remove(plusMinKnop);
		
				schuifveld.tekenOpnieuw();
			}
		}
	}
	public void mouseReleased(MouseEvent e)
	{	if (((AlgebraSchuifVeld)schuifveld).fixed)
			return;
		if (((AlgebraSchuifVeld)schuifveld).isDemo)
			return;
		super.mouseReleased(e);
		
		
	}
	
	public void focusLost(FocusEvent e)
	{	zetInvulWaarde();
	}
	public void focusGained(FocusEvent e){;	}
	
}
