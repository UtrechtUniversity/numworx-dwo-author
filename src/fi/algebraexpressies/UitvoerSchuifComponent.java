package fi.algebraexpressies;

import java.awt.*;
import java.awt.event.*;
import java.util.Hashtable;

import fi.algebraexpressies.expressies_ap.*;

import javax.swing.*;

public class UitvoerSchuifComponent extends AlgebraSchuifComponent implements ActionListener, FocusListener
{	
	private JTextField tf;
	private Expressie expressie;
	private BasisExpressie beginw;
	private Expressie verborgenExpressie;	
	private String waardeString;
	private boolean toonWaarde;
	private boolean labelZichtbaar;
	private InUitvoerLabel label;
	boolean tabelZichtbaar;


	boolean zoomInTabel;	
	
	private boolean grafiek;
	boolean muisrechts;
	private GrafiekComponent grafiekComponent;
	private TabelComponent tabel;
	private int tabelCorr;	
	Font f;
	FontMetrics fm;
	private JPopupMenu popup;
	private JPopupMenu popup2;
	private JPopupMenu popup3;

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
	private String defaultVarnaam = "qq" + 1000 * Math.random();
	
	private Color vakKleur, vakKleurSoft;
	
	AlgebraSchuifVeld asv;
	
	public UitvoerSchuifComponent(AlgebraSchuifVeld asv,int x, int y, int b, int h)
	{	super(1, asv, x, y, b, h);
		
		this.asv = asv;
	
		toonWaarde = !((AlgebraSchuifVeld) schuifveld).ip.isExpr();
		labelZichtbaar = false;
		tabelZichtbaar = false;
		
		zoomInTabel = true;
		
		waardeString = "";		
		f = new Font("SansSerrif",Font.PLAIN, 12);
		fm = getFontMetrics(f);
		
		label = new InUitvoerLabel();
		tabel = new TabelComponent();
		tabel.setDefaultVarnaam(defaultVarnaam);		
		
		tf = new JTextField();
		tf.setFont(f);		
		tf.setBounds(2, 10, 35, 20);
		tf.addActionListener(this);
		tf.addFocusListener(this);
		tf.setVisible(false);
		tf.setEnabled(false);
		add(tf);
		
		// popup met label(2x), tabel(2x), ketting(2x)
		popup = new JPopupMenu();

		JMenuItem mi = new JMenuItem(AlgebraExpressies.rb.getString("popup1Label1"));
		mi.addActionListener(this);
		popup.add(mi);
		mi = new JMenuItem(AlgebraExpressies.rb.getString("popup1Label2"));
		mi.addActionListener(this);
		popup.add(mi);
		
		popup.addSeparator();
		
		mi = new JMenuItem(AlgebraExpressies.rb.getString("popup1Label3"));
		mi.addActionListener(this);
		popup.add(mi);
		mi = new JMenuItem(AlgebraExpressies.rb.getString("popup1Label4"));
		mi.addActionListener(this);
		popup.add(mi);
		
		popup.addSeparator();
		
		mi = new JMenuItem(AlgebraExpressies.rb.getString("popup1Label5"));
		mi.addActionListener(this);
		popup.add(mi);
		mi = new JMenuItem(AlgebraExpressies.rb.getString("popup1Label6"));
		mi.addActionListener(this);
		popup.add(mi);
		
		add(popup);
		
		// popup met label(2x), ketting(2x)
		popup2 = new JPopupMenu();
		
		mi = new JMenuItem(AlgebraExpressies.rb.getString("popup1Label1"));
		mi.addActionListener(this);
		popup2.add(mi);
		mi = new JMenuItem(AlgebraExpressies.rb.getString("popup1Label2"));
		mi.addActionListener(this);
		popup2.add(mi);
		
		popup2.addSeparator();
		
		mi = new JMenuItem(AlgebraExpressies.rb.getString("popup1Label5"));
		mi.addActionListener(this);
		popup2.add(mi);
		mi = new JMenuItem(AlgebraExpressies.rb.getString("popup1Label6"));
		mi.addActionListener(this);
		popup2.add(mi);
		
		add(popup2);
		
		// popup met label(2x), ketting(2x)
		popup3 = new JPopupMenu();
		
		mi = new JMenuItem(AlgebraExpressies.rb.getString("popup1Label1"));
		mi.addActionListener(this);
		popup3.add(mi);
		mi = new JMenuItem(AlgebraExpressies.rb.getString("popup1Label2"));
		mi.addActionListener(this);
		popup3.add(mi);
		
		add(popup3);
		

		verborgenExpressie = new BasisExpressie(defaultVarnaam);
		
		plusMinKnop = new PlusMinKnop(b-12,11,10,h-11, PlusMinKnop.VERTIKAAL);
		plusMinKnop.addActionListener(this);
		plusMinKnop.setVisible(false);		
		add(plusMinKnop);
		
		zoomInKnop	= new ZoomKnop("zoominxsmal");
		zoomInKnop.setBounds(1,60,11,25);
		zoomInKnop.addActionListener(this);
		add(zoomInKnop);
		
		zoomUitKnop	= new ZoomKnop("zoomuitxsmal");
		zoomUitKnop.setBounds(1,110,11,25);
		zoomUitKnop.addActionListener(this);
		add(zoomUitKnop);
	}

	public Hashtable getState()
	{	String basisExp  = null;
		String defaultVarnaam = null;
		boolean tabelZichtbaar = false;
		boolean labelZichtbaar = false;
        boolean kettingZichtbaar = true;
		String labelTekst = null;
		
		if (beginw != null)
			basisExp = this.beginw.basisString;
		else 
			basisExp = "";
		defaultVarnaam = this.defaultVarnaam;
		tabelZichtbaar = this.tabelZichtbaar;
		labelZichtbaar = this.labelZichtbaar;
        kettingZichtbaar = this.kettingZichtbaar;
		labelTekst = label.geefTekst();
		
//System.out.println("get " + kettingZichtbaar);		
		Hashtable h = super.getState();

		h.put("basisExp", basisExp);
	    h.put("defaultVarnaam", defaultVarnaam);
	    h.put("tabelZichtbaar", new Boolean(tabelZichtbaar));
	    h.put("labelZichtbaar", new Boolean(labelZichtbaar));
        h.put("kettingZichtbaar", new Boolean(kettingZichtbaar));
        h.put("labelTekst", labelTekst);
        
        h.put("scrollable", new Boolean(scrollable));
        h.put("zoomInTabel", new Boolean(zoomInTabel));
        
	    return h;
	}

    public void setState(Hashtable h)
    {	String basisExp  = "";
 		String defaultVarnaam = null;
        boolean tabelZichtbaar = false;
        boolean labelZichtbaar = false;
        boolean kettingZichtbaar = true;
        String labelTekst = "";
        
        boolean scrollable = true;
        boolean zoomInTabel = true;
        
    	if (h.containsKey("basisExp")) 
    		basisExp = (String) h.get("basisExp");
    	if (h.containsKey("defaultVarnaam")) 
    		defaultVarnaam = (String) h.get("defaultVarnaam");
        if (h.containsKey("tabelZichtbaar")) 
        	tabelZichtbaar = ((Boolean) h.get("tabelZichtbaar")).booleanValue();		
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
		
		//zetTabelAan(tabelAan);
		toonTabel(tabelZichtbaar);
		//zetLabel(labelZichtbaar);
		toonLabel(labelZichtbaar);
		label.zetLabelTekst(labelTekst);

		//this.kettingZichtbaar = kettingZichtbaar;
		
		//if (!kettingZichtbaar)
        //	zetBoomZichtbaar(kettingZichtbaar);
        
		if (!kettingZichtbaar)
			zetBoomZichtbaarHier(kettingZichtbaar);

        zetScroll(scrollable);
        zetZoomInTabel(zoomInTabel);
		
		super.setState(h);
		
		zetMaat();
		
    }

    public void zetZoomInTabel(boolean b)
    {
    	zoomInTabel = b;
    	zoomInKnop.setVisible(b);
    	zoomUitKnop.setVisible(b);
    }

    public void zetScroll(boolean b)
	{	scrollable = b;
	
		zetVeranderd(20);
	
		zetMaat();
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
  		
		g.setColor(Color.gray);
		g.fillRect(0, 10, getSize().width - 1, getSize().height - 11);
		g.setColor(Color.black);
		g.drawRect(0, 10, getSize().width - 1, getSize().height - 11);
		
		super.paint(g);
		
//g.setColor(Color.red);
//g.drawRect(0, 0, getSize().width - 1, getSize().height - 1);
		
		int labelCorr = 0;
		int tabelCorr = 0;
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
		
		g.fillRect(2, labelCorr + 12, getSize().width - 5  - scrollCorr, getSize().height - labelCorr - tabelCorr - 15);
		g.setColor(Color.black);
		g.drawRect(2, labelCorr + 12, getSize().width - 5  - scrollCorr, getSize().height - labelCorr - tabelCorr - 15);
		
		g.setFont(f);
		if (expressie != null)
		{	expressie.zetMaat(fm);
			if (toonWaarde && expressie.geefVarNaam() == null)
				g.drawString(waardeString, (getSize().width - scrollCorr - fm.stringWidth(waardeString)) / 2, 
						                    getSize().height - tabelCorr - 5);
			else 
				expressie.teken(g, (getSize().width - scrollCorr - expressie.breedte) / 2, 
						        12 + (labelCorr + getSize().height - tabelCorr - 15 - expressie.hoogte) / 2);
		}
		

  		if ((soort == 1) && 
  			//(pijlIn1 == null) && 
  			labelZichtbaar)
  		{	g.setColor(Color.white);
  			g.fillRect(0, 0, 12, 5);
  		}
  		
		
	}
	
	public void zetToonWaarde(boolean b)
	{	toonWaarde = b;
		zetMaat();
	}
	
	public void toonLabel(boolean b)
	{	labelZichtbaar = b;
		if (b)
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
		if (b)
		{	add(tabel);
			zetMaat();
		}
		else
		{	remove(tabel);
			zetMaat();
		}
		schuifveld.tekenOpnieuw();
		
		GrafiekComponent gc = vindGrafiekComponent();
		if (gc != null)
			gc.zetVeranderd(20);
		
	}
	
	public boolean isLabelZichtbaar()
	{	return labelZichtbaar;
	}
	
	public String geefLabelTekst()
	{	return label.geefTekst();
	}
		
	
	public void setSize(int b, int h)
	{	label.setSize(b, 20);
		super.setSize(b, h);
	}
	
	public void zetMaat()
	{	
		plusMinKnop.setVisible(false);		
		int b = 40 + scrollCorr;
		int h = 30;
		int corr = 0;
		if (expressie != null)
		{	

			if (expressie.geefVarNaam() == null)
				plusMinKnop.setVisible(true);	
			
			b = expressie.breedte + scrollCorr;
			h = expressie.hoogte;
			if (toonWaarde && expressie.geefVarNaam() == null && waardeString != null)
			{	
//System.out.println("ttonw && varn==null && ws not null");				
				
				b = fm.stringWidth(waardeString) + scrollCorr;
				h = 0;
				//plusMinKnop.setVisible(true);
			}
			if (b > 26)
				b = b + 14 + scrollCorr;
			else 
				b = 40 + scrollCorr;
			if (h > 12)
				h = 20 + ((h + 5) / 10) * 10;
			else 
				h = 30;
		}
		
		if (labelZichtbaar)
		{	corr = 20;
			h = h + 20;
			b = Math.max(b, label.geefBreedte());
		}
		if (tabelZichtbaar)
		{	h = h + 152;
			b = Math.max(b, tabel.geefBreedte());
//System.out.println("tabelb = " + tabel.geefBreedte());			
		}
		setSize(b, h);
		label.setSize(b, 20);
		tabel.setBounds(10, h - 152, b - 10, 152);
		tf.setBounds(2, corr + 10, b - 5 - scrollCorr, 20);
		plusMinKnop.setLocation(b - 12, 11 + corr);
		zoomInKnop.setBounds(1, h- 120,12,25);
		zoomUitKnop.setBounds(1, h - 70,12,25);


	}
	
	public Expressie geefUitvoer(int max)
	{	return expressie;
	}

	public Expressie geefVerborgenUitvoer(int max)
	{	return verborgenExpressie;
	}

	public GrafiekComponent vindGrafiekComponent()
	{	GrafiekComponent gc = null;
		for (int pCnt = 0; pCnt < pijlUit.length; pCnt++)
		{
			if ((pijlUit[pCnt] != null) && (pijlUit[pCnt].ontvanger instanceof GrafiekComponent))
				gc = (GrafiekComponent) pijlUit[pCnt].ontvanger;
		}
	
		return gc;
	}
	
	public void zetVeranderd(int max)
	{	if (pijlIn1 != null)
		{	remove(plusMinKnop);
			scrollCorr = 0;
			expressie = pijlIn1.zender.geefUitvoer(20);
			verborgenExpressie = pijlIn1.zender.geefVerborgenUitvoer(20);
//if (expressie == null)
//System.out.println("USC e = null");
//else
//System.out.println("USC e = " + expressie.toString());
//if (verborgenExpressie == null)
//System.out.println("ve = null");
//else
//System.out.println("ve = " + verborgenExpressie.toString());	
			//zoomInKnop.setVisible(false);
			//zoomUitKnop.setVisible(false);
			
			pijlUit[0].im = null;
			
			if (asv.aeip.kijkNaActief)
				asv.aeip.answerChanged();

		}
		else 
		{	
			if (scrollable  && expressie != null && !Double.isNaN(expressie.geefWaarde().doubleValue()))
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
	
		//tabel.zetExpressie(expressie);
		
		if (expressie != null && expressie.geefVarNaam() != null)
			tabel.zetExpressie(expressie);
		else if (verborgenExpressie != null && verborgenExpressie.geefVarNaam() != null)
			tabel.zetExpressie(verborgenExpressie);
		
		if (expressie != null)
		{	expressie.zetMaat(fm);
			Double waarde = expressie.geefWaarde();
//if (waarde == null)
//System.out.println("w = null");	
			if (!Double.isNaN(waarde.doubleValue()))
				waardeString = Expressie.df.format(waarde);
			else 
				waardeString = "-";
		}

		zetMaat();
		
		if (expressie != null && !Double.isNaN(expressie.geefWaarde().doubleValue()))
			toonTabel(false);
		else if (verborgenExpressie != null && !Double.isNaN(verborgenExpressie.geefWaarde().doubleValue()))
			toonTabel(false);
		else if (expressie == null && verborgenExpressie == null)
			toonTabel(false);
		else
			toonTabel(tabelZichtbaar);
		
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
		
		GrafiekComponent gc = vindGrafiekComponent();
		if (gc != null)
			gc.zetVeranderd(20);
		
	}

// DIT MOET WEG
	public void zetGrafiek(boolean b, GrafiekComponent gc)
	{	grafiek = b;
		tabel.zetSelectMogelijk(b);
		if (b)
			grafiekComponent = gc;
		else 
			grafiekComponent = null;
	}
	
// DIT MOET WEG	
	public void zetTabel(int beginwaarde, int selectnummer, String varN, double schaalFactorX)
	{	if (grafiek)
		{	tabel.zetTabel(beginwaarde, selectnummer, varN, schaalFactorX);
			
		}
		zetMaat();
	}
/*	
	public void zetGrafiekTabel(int beginwaarde, int selectnummer, String varN, double schaalFactorX)
	{	if (grafiek)
		{	grafiekComponent.zetTabel(beginwaarde, selectnummer, varN, schaalFactorX);
			
		}
		zetMaat();
	}
*/	
	
	public void setZoomState(String varnaam, ZoomState zoomState)
	{	if (expressie != null && expressie.geefVarNaam() != null && expressie.geefVarNaam().equals(varnaam)
			|| 
			verborgenExpressie != null && verborgenExpressie.geefVarNaam() != null && verborgenExpressie.geefVarNaam().equals(varnaam))
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
		
		tf.setEnabled(false);
		//remove(tf);
		tf.setVisible(false);
		
		zetMaat();
		zetVeranderd(20);
		
		schuifveld.tekenOpnieuw();
	}
	
	public void actionPerformed(ActionEvent e)
	{	if (e.getSource() == tf)
		{	zetInvulWaarde();
		}	
		else if (e.getSource() == plusMinKnop)
		{	
			if (((AlgebraSchuifVeld) getParent()).isDemo)
				return;
			if (((AlgebraSchuifVeld) getParent()).frozen)
				return;
			

			if (beginw != null && !Double.isNaN(beginw.geefWaarde().doubleValue()))
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
		{	
			
			if (((AlgebraSchuifVeld) getParent()).isDemo)
				return;
			if (((AlgebraSchuifVeld) getParent()).frozen)
				return;
			
			
			if (!e.getActionCommand().equals("knop") || factorRijNummerX > 120) 
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
        
        	((AlgebraSchuifVeld) getParent()).zoomStateHolder.setBeginwaarde(varnaam, beginwaarde);
        	((AlgebraSchuifVeld)getParent()).zoomStateHolder.setSelectnummer(varnaam, selectnummer);
        	((AlgebraSchuifVeld) getParent()).zoomStateHolder.setSchaalFactorX(varnaam, schaalFactorX);
        	((AlgebraSchuifVeld) getParent()).zoomStateHolder.setFactorRijNummerX(varnaam, factorRijNummerX);
        	((AlgebraSchuifVeld) getParent()).zoomStateHolder.setBeginx(varnaam, beginx);
        	((AlgebraSchuifVeld) getParent()).zoomStateHolder.setZoomStates(varnaam);
        
        	schuifveld.tekenOpnieuw();
		}
		else if (e.getSource() == zoomInKnop)
		{	
			if (((AlgebraSchuifVeld) getParent()).isDemo)
				return;
			if (((AlgebraSchuifVeld) getParent()).frozen)
				return;

			
			if (!e.getActionCommand().equals("knop") || factorRijNummerX < 87) 
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
	
		else if (((JMenuItem)e.getSource()).getText().equals(AlgebraExpressies.rb.getString("popup1Label1")))
		{	toonLabel(true);
		}
		else if (((JMenuItem)e.getSource()).getText().equals(AlgebraExpressies.rb.getString("popup1Label2")))
		{	toonLabel(false);
		}
		else if (((JMenuItem)e.getSource()).getText().equals(AlgebraExpressies.rb.getString("popup1Label3")))
		{	toonTabel(true);
		}
		else if (((JMenuItem)e.getSource()).getText().equals(AlgebraExpressies.rb.getString("popup1Label4")))
		{	toonTabel(false);
		}
		else if (((JMenuItem)e.getSource()).getText().equals(AlgebraExpressies.rb.getString("popup1Label5")))
		{
			zetBoomZichtbaarHier(true);
/*			
			if (pijlIn1 != null)
				pijlIn1.zender.zetBoomZichtbaar(true);
			if (pijlIn2 != null)
				pijlIn2.zender.zetBoomZichtbaar(true);
			open = true;
			kettingZichtbaar = true;
*/			
		}
		else if (((JMenuItem)e.getSource()).getText().equals(AlgebraExpressies.rb.getString("popup1Label6")))
		{	
			zetBoomZichtbaarHier(false);
/*			
			if (pijlIn1 != null)
				pijlIn1.zender.zetBoomZichtbaar(false);
			if (pijlIn2 != null)
				pijlIn2.zender.zetBoomZichtbaar(false);
			open = false;
			kettingZichtbaar = false;
*/						
		}
	}
	
	public void zetBoomZichtbaarHier(boolean b)
	{
		if (pijlIn1 != null)
			pijlIn1.zender.zetBoomZichtbaar(b);
		if (pijlIn2 != null)
			pijlIn2.zender.zetBoomZichtbaar(b);

		for (int i = 0; i < aantalPu; i++)
		{	pijlUit[i].setVisible(b);
		}
		
        open = b;
        kettingZichtbaar = b;
        
        schuifveld.tekenOpnieuw();
	}
	
	public void mousePressed(MouseEvent e)
	{	
		
		if (((AlgebraSchuifVeld)schuifveld).isDemo)
			return;
		if (((AlgebraSchuifVeld) schuifveld).frozen)
			return;
		
		muisrechts = false;
		if (e.getModifiers() == e.BUTTON3_MASK || e.isControlDown())
		{	requestFocus();
			muisrechts = true;
			
			if (((AlgebraSchuifVeld)schuifveld).alleenInvullen)
			{	return;
			}
			if (isStapel)
				return;
			
			if (pijlIn1 == null)
				popup3.show(this, e.getX(), e.getY());
			else if (// getal
					 (expressie != null && expressie.geefVarNaam() == null) ||
					 // x
					 (expressie instanceof BasisExpressie))
				popup2.show(this, e.getX(), e.getY());
			else	
				popup.show(this, e.getX(), e.getY());
			return;
		}
		super.mousePressed(e);
	}
	public void mouseClicked(MouseEvent e)
	{	
		
		if (((AlgebraSchuifVeld)schuifveld).isDemo)
			return;
		
		if (((AlgebraSchuifVeld) schuifveld).frozen)
			return;
		
		if (!muisrechts && pijlIn1 == null && !isStapel)
		{	
			
			if (new Rectangle(tf.getLocation().x, tf.getLocation().y,
			          tf.getSize().width, tf.getSize().height).contains(e.getX(), e.getY())
				)
			{
			
				//add(tf);
				tf.setVisible(true)	;
				tf.setEnabled(true);
				tf.selectAll();
				tf.requestFocus();
			
			}
		}
	}
	public void mouseReleased(MouseEvent e)
	{	
		if (((AlgebraSchuifVeld)schuifveld).isDemo)
			return;
		if (((AlgebraSchuifVeld) schuifveld).frozen)
			return;
				
		super.mouseReleased(e);
	}
	
	public void focusLost(FocusEvent e)
	{	if (e.getSource() == tf)
		{	zetInvulWaarde();
			
		}
	}
	public void focusGained(FocusEvent e)
	{	
	}
}
