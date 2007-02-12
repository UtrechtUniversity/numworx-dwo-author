package fi.algebrapijlenopdr;

import java.awt.Polygon;
import java.awt.*;
import java.awt.event.*;
import java.util.Hashtable;
import fi.algebrapijlenopdr.expressies_ap.*;

public class UitvoerSchuifComponent extends AlgebraSchuifComponent implements ActionListener, FocusListener
{	
	private TextField tf;
	private Expressie expressie, verborgenExpressie;
	private BasisExpressie beginw;
	private String waardeString;
	private boolean toonWaarde;
	private boolean labelZichtbaar;
	private InUitvoerLabel label;
	 boolean tabelZichtbaar;
	private boolean tabelAan;
	private boolean grafiek;
	private boolean muisrechts;
	private GrafiekComponent grafiekComponent;
	private TabelComponent tabel;
	private int tabelCorr;
	Font f;
	FontMetrics fm;
	private PopupMenu popup;
	private PlusMinKnop plusMinKnop;
	boolean scrollable;
	int scrollCorr = 0;
	
	public UitvoerSchuifComponent(AlgebraSchuifVeld asv,int x, int y, int b, int h)
	{	super(1,asv,x,y,b,h);
		
		toonWaarde = !((AlgebraSchuifVeld)schuifveld).ip.isExpr();
		labelZichtbaar = false;
		tabelZichtbaar = false;
		waardeString = "";
		f = new Font("SansSerrif",Font.PLAIN,12);
		fm = getFontMetrics(f);
		
		label = new InUitvoerLabel();
		tabel = new TabelComponent();
		
		tf = new TextField();
		if(!links)tf.setBounds(12,0,35,20);
		else tf.setBounds(2,0,35,20);
		tf.addActionListener(this);
		tf.addFocusListener(this);
		tf.setVisible(false);
		tf.setEnabled(false);
		
		popup = new PopupMenu();
					
		MenuItem mi = new MenuItem(AlgebraPijlenOpdr.rb.getString("popup1Label1"));
		mi.addActionListener(this);
		popup.add(mi);
		
		mi = new MenuItem(AlgebraPijlenOpdr.rb.getString("popup1Label2"));
		mi.addActionListener(this);
		popup.add(mi);
		
		popup.addSeparator();
		
		mi = new MenuItem(AlgebraPijlenOpdr.rb.getString("popup1Label3"));
		mi.addActionListener(this);
		popup.add(mi);
		
		mi = new MenuItem(AlgebraPijlenOpdr.rb.getString("popup1Label4"));
		mi.addActionListener(this);
		popup.add(mi);
		
		popup.addSeparator();
		
		mi = new MenuItem(AlgebraPijlenOpdr.rb.getString("popup1Label5"));
		mi.addActionListener(this);
		popup.add(mi);
		
		mi = new MenuItem(AlgebraPijlenOpdr.rb.getString("popup1Label6"));
		mi.addActionListener(this);
		popup.add(mi);
		
		add(popup);
		
		verborgenExpressie = new BasisExpressie("x");
		
		if(!links)
		{	plusMinKnop = new PlusMinKnop(b-12,1,10,h-2, PlusMinKnop.VERTIKAAL);
		}
		else
		{	plusMinKnop = new PlusMinKnop(b-22,1,10,h-2, PlusMinKnop.VERTIKAAL);
		}
		plusMinKnop.addActionListener(this);
		add(plusMinKnop);
	}
	
	public Hashtable getState()
	{	String basisExp  = null;
		boolean tabelAan = false;
		boolean labelZichtbaar = false;
		String labelTekst = null;
		Hashtable tabelState = null;
				
		if(beginw != null)basisExp = this.beginw.basisString;
		else basisExp = "";
		tabelAan = this.tabelAan;
		labelZichtbaar = this.labelZichtbaar;
		labelTekst = label.geefTekst();
		tabelState = tabel.getState();

		Hashtable h = super.getState();
	    h.put("basisExp", basisExp);
	    h.put("tabelAan", new Boolean(tabelAan));
	    h.put("labelZichtbaar", new Boolean(labelZichtbaar));
	    h.put("labelTekst", labelTekst);
	    h.put("tabelState", tabelState);
	    return h;
	}

    public void setState(Hashtable h)
    {	
    	
    	String basisExp = (String)h.get("basisExp");
		boolean tabelAan = ((Boolean)h.get("tabelAan")).booleanValue();		
		boolean labelZichtbaar = ((Boolean)h.get("labelZichtbaar")).booleanValue();
		String labelTekst = (String)h.get("labelTekst");
		Hashtable tabelState = (Hashtable)h.get("tabelState");	
		
		if(!basisExp.equals(""))beginw = new BasisExpressie(basisExp);
		zetTabelAan(tabelAan);
		zetLabel(labelZichtbaar);
		label.zetLabelTekst(labelTekst);
		tabel.setState(tabelState);
		
		super.setState(h);
		
		zetMaat();
		
    }
    public void zetScroll(boolean b)
	{	scrollable = b;
		zetMaat();
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
	
	public void paint(Graphics g)
  	{ 	Color achtergrondkleur = Color.white;
		if(pijlIn1!=null)achtergrondkleur = new Color(220,220,220);
		
		if(!links)
		{	g.setColor(Color.gray);
			g.fillRect(10,0,getSize().width-11,getSize().height-1);
			g.setColor(Color.black);
			g.drawRect(10,0,getSize().width-11,getSize().height-1);
						
			super.paint(g);
		
			int labelCorr = 0;
			tabelCorr = 0;
			if(labelZichtbaar)labelCorr = 20;
			if(tabelZichtbaar)tabelCorr = 152;
			
			g.setColor(achtergrondkleur);
			g.fillRect(12,labelCorr+2,getSize().width-15-scrollCorr,getSize().height-labelCorr-tabelCorr-5);
			g.setColor(Color.black);
			g.drawRect(12,labelCorr+2,getSize().width-15-scrollCorr,getSize().height-labelCorr-tabelCorr-5);
		
			g.setFont(f);
			if(expressie!=null)
			{	expressie.zetMaat(fm);
				if(toonWaarde && expressie.geefWaarde()!=null)g.drawString(waardeString, 5+(getSize().width-scrollCorr-fm.stringWidth(waardeString))/2, getSize().height-tabelCorr-5);
				else expressie.teken(g, 5+(getSize().width-scrollCorr-expressie.breedte)/2, 7 + (labelCorr+getSize().height-tabelCorr-15 - expressie.hoogte)/2);
			}
				
		}
		else
		{	g.setColor(Color.gray);
			g.fillRect(0,0,getSize().width-11,getSize().height-1);
			g.setColor(Color.black);
			g.drawRect(0,0,getSize().width-11,getSize().height-1);
			
			super.paint(g);
		
			int labelCorr = 0;
			tabelCorr = 0;
			if(labelZichtbaar)labelCorr = 20;
			if(tabelZichtbaar)tabelCorr = 152;
			g.setColor(achtergrondkleur);
			g.fillRect(2,labelCorr+2,getSize().width-15-scrollCorr,getSize().height-labelCorr-tabelCorr-5);
			g.setColor(Color.black);
			g.drawRect(2,labelCorr+2,getSize().width-15-scrollCorr,getSize().height-labelCorr-tabelCorr-5);
		
			g.setFont(f);
			if(expressie!=null)
			{	expressie.zetMaat(fm);
				if(toonWaarde && expressie.geefWaarde()!=null)g.drawString(waardeString, -5+(getSize().width-scrollCorr-fm.stringWidth(waardeString))/2, getSize().height-tabelCorr-5);
				else expressie.teken(g, -5+(getSize().width-scrollCorr-expressie.breedte)/2, 7 + (labelCorr+getSize().height-tabelCorr-15 - expressie.hoogte)/2);
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
		int b = 50+scrollCorr;
		int h = 20;
		int corr = 0;
		if(expressie!=null)
		{	b = expressie.breedte+scrollCorr;
			h = expressie.hoogte;
			if(toonWaarde && expressie.geefWaarde()!=null)
			{	b = fm.stringWidth(waardeString)+scrollCorr;
				h = 0;
			}
			if(b > 26)b = b+24;
			else b = 50+scrollCorr;
			if(h > 12)h = 10+((h+5)/10)*10;
			else h = 20;
		}
		if(labelZichtbaar)
		{	corr = 20;
			h=h+20;
			b = Math.max(b,label.geefBreedte()+10);
		}
		if(tabelZichtbaar)
		{	h=h+152;
			b = Math.max(b,tabel.geefBreedte()+10);
		}
		setSize(b,h);
		label.setSize(b-10,20);
		
		if(!links)
		{	tabel.setBounds(10,h-152,b-10,152);
			tf.setBounds(12,corr,b-15-scrollCorr,20);
			plusMinKnop.setLocation(b-12,1);
		}
		else
		{	tabel.setBounds(0,h-152,b-10,152);
			tf.setBounds(2,corr,b-15-scrollCorr,20);
			plusMinKnop.setLocation(b-22,1);
		}	

	}
	
	public Expressie geefUitvoer(int max)
	{	return expressie;
	}
	
	public Expressie geefVerborgenUitvoer(int max)
	{	return verborgenExpressie;
	}
	
	public void zetVeranderd(int max)
	{	if(pijlIn1!=null)
		{	remove(plusMinKnop);
			scrollCorr = 0;
			expressie = pijlIn1.zender.geefUitvoer(20);
			verborgenExpressie = pijlIn1.zender.geefVerborgenUitvoer(20);
			((AlgebraSchuifVeld)getParent()).zetTabellen(0,999,"x",1);
		}
		else 
		{	if(scrollable  && expressie!=null && expressie.geefWaarde()!=null)
			{	if(scrollCorr==0)
				add(plusMinKnop);
				scrollCorr = 10;
			}
			else
			{	scrollCorr = 0;
				remove(plusMinKnop);
			}
			expressie = beginw;
			verborgenExpressie = new BasisExpressie("x");
		}
		
		if(expressie!=null && expressie.geefVarNaam()!=null)tabel.zetExpressie(expressie);
		else tabel.zetExpressie(verborgenExpressie);
		
		if(expressie!=null)
		{	expressie.zetMaat(fm);
			Double waarde = expressie.geefWaarde();
			if(waarde!=null)waardeString = Expressie.df.format(waarde);
			else waardeString = "-";
		}
		zetMaat();
		zetTabelAan(tabelAan);
		super.zetVeranderd(max);
	}
	
	//public void zetGrafiek(boolean b, GrafiekComponent gc)
	//{	grafiek = b;
	//	//tabel.zetSelectMogelijk(b);
	//	if(b)grafiekComponent = gc;
	//	else grafiekComponent = null;
	//}
	
	public void zetTabel(int beginwaarde, int selectnummer, String varN, double schaalFactorX)
	{	if(tabelZichtbaar)
		{	tabel.zetTabel(beginwaarde, selectnummer, varN, schaalFactorX);
			if(grafiekComponent!=null)grafiekComponent.zetTabel(beginwaarde, selectnummer, varN, schaalFactorX);
		}
		zetMaat();
		
	}
	
	public void zetTabelAan(boolean b)
	{	tabelAan = b;
		//if(b && !isStapel && (expressie==null ||expressie.geefWaarde()==null))
		if(!isStapel)toonTabel(b);
		//else 
		//toonTabel(false);
	}
	
	public void zetInvulWaarde()
	{	boolean isGeldigeInvoer=true;
		{	try
			{	String s = tf.getText();
				s = s.replace(',','.');
				tf.setText(s);
				Double w = Double.valueOf(tf.getText());
				//((AlgebraSchuifVeld)getParent()).zetTabellen((int)(w.doubleValue()),0,"x",1);
				toonTabel(false);
			}
			catch(NumberFormatException ex)
			{	for(int i=0 ; i<tf.getText().length() ; i++)
				{	if(!Character.isLetter(tf.getText().charAt(i)))
					{	isGeldigeInvoer=false;
						break;
					}
				}
				if(tf.getText().equals(""))isGeldigeInvoer = false;
				if(!isGeldigeInvoer )
				{	tf.setText("");
				}
			}
		}
		if(isGeldigeInvoer)
		{	beginw = new BasisExpressie( tf.getText());
			beginw.zetMaat(fm);
		}
		else
		{	beginw = null;
		}
		expressie = beginw;
		zetMaat();
		zetVeranderd(20);
		tf.setEnabled(false);
		remove(tf);
		tf.setVisible(false);
		schuifveld.tekenOpnieuw();
	}
	
	public void actionPerformed(ActionEvent e)
	{	if(e.getSource()==tf)
		{	zetInvulWaarde();
		}		
		else if(e.getSource()==plusMinKnop)
		{	if(beginw.geefWaarde()!=null)
			{	double w = beginw.geefWaarde().doubleValue();
				if(e.getActionCommand().equals("min"))w -= 1;
				if(e.getActionCommand().equals("plus"))w += 1;
				waardeString = Expressie.df.format(w);
				beginw = new BasisExpressie(waardeString);
				tf.setText(waardeString);
				zetVeranderd(20);
			}
		}
		else if(((MenuItem)e.getSource()).getLabel().equals(AlgebraPijlenOpdr.rb.getString("popup1Label1")))
		{	toonLabel(true);
		}
		else if(((MenuItem)e.getSource()).getLabel().equals(AlgebraPijlenOpdr.rb.getString("popup1Label2")))
		{	toonLabel(false);
		}
		else if(((MenuItem)e.getSource()).getLabel().equals(AlgebraPijlenOpdr.rb.getString("popup1Label3")))
		{	zetTabelAan(true);
		}
		else if(((MenuItem)e.getSource()).getLabel().equals(AlgebraPijlenOpdr.rb.getString("popup1Label4")))
		{	zetTabelAan(false);
		}
		else if(((MenuItem)e.getSource()).getLabel().equals(AlgebraPijlenOpdr.rb.getString("popup1Label5")))
		{	if(pijlIn1!=null)pijlIn1.zender.zetKettingZichtbaar(true);
			open = true;
			tabel.zetDubbel(false);
			zetMaat();
			schuifveld.tekenOpnieuw();
		}
		else if(((MenuItem)e.getSource()).getLabel().equals(AlgebraPijlenOpdr.rb.getString("popup1Label6")))
		{	if(pijlIn1!=null)pijlIn1.zender.zetKettingZichtbaar(false);
			open = false;
			tabel.zetDubbel(true);
			zetMaat();
			schuifveld.tekenOpnieuw();
		}
	}
	
	public void mousePressed(MouseEvent e)
	{	if(((AlgebraSchuifVeld)schuifveld).fixed)return;
		requestFocus();
		muisrechts = false;
		if(e.getModifiers()== e.BUTTON3_MASK || e.isControlDown())
		{	muisrechts = true;
			popup.show(this,e.getX(),e.getY());
			return;
		}
		super.mousePressed(e);
	}
	public void mouseClicked(MouseEvent e)
	{	if(((AlgebraSchuifVeld)schuifveld).fixed)return;
		if(!muisrechts && pijlIn1==null)
		{	add(tf);
			tf.setVisible(true)	;
			tf.setEnabled(true);
			tf.selectAll();
			tf.requestFocus();
		}
	}
	public void mouseReleased(MouseEvent e)
	{	if(((AlgebraSchuifVeld)schuifveld).fixed)return;
		super.mouseReleased(e);
	}
	
	public void focusLost(FocusEvent e)
	{	zetInvulWaarde();
	}
	public void focusGained(FocusEvent e){;	}
	
}
