package fi.algebraexpressies;

import java.awt.Polygon;
import java.awt.*;
import java.awt.event.*;
import fi.algebraexpressies.expressies.*;

public class UitvoerSchuifComponent extends AlgebraSchuifComponent implements ActionListener, FocusListener
{	
	private TextField tf;
	private Expressie expressie;
	private BasisExpressie beginw;
	private String waardeString;
	private boolean toonWaarde;
	private boolean labelZichtbaar;
	private InUitvoerLabel label;
	private boolean tabelZichtbaar;
	private boolean grafiek;
	private boolean muisrechts;
	private GrafiekComponent grafiekComponent;
	private TabelComponent tabel;
	Font f;
	FontMetrics fm;
	private PopupMenu popup;
	
	public UitvoerSchuifComponent(AlgebraSchuifVeld asv,int x, int y, int b, int h)
	{	super(1,asv,x,y,b,h);
		//addFocusListener(this);
		
		toonWaarde = !((AlgebraSchuifVeld)schuifveld).ip.isExpr();
		labelZichtbaar = false;
		f = new Font("SansSerrif",Font.PLAIN,12);
		fm = getFontMetrics(f);
		
		label = new InUitvoerLabel();
		tabel = new TabelComponent();
		
		tf = new TextField();
		tf.setBounds(2,10,35,20);
		tf.addActionListener(this);
		tf.addFocusListener(this);
		tf.setVisible(false);
		tf.setEnabled(false);
		
		popup = new PopupMenu();
					
		MenuItem mi = new MenuItem(AlgebraExpressies.rb.getString("popup1Label1"));
		mi.addActionListener(this);
		popup.add(mi);
		
		mi = new MenuItem(AlgebraExpressies.rb.getString("popup1Label2"));
		mi.addActionListener(this);
		popup.add(mi);
		
		popup.addSeparator();
		
		mi = new MenuItem(AlgebraExpressies.rb.getString("popup1Label3"));
		mi.addActionListener(this);
		popup.add(mi);
		
		mi = new MenuItem(AlgebraExpressies.rb.getString("popup1Label4"));
		mi.addActionListener(this);
		popup.add(mi);
		
		popup.addSeparator();
		
		mi = new MenuItem(AlgebraExpressies.rb.getString("popup1Label5"));
		mi.addActionListener(this);
		popup.add(mi);
		
		mi = new MenuItem(AlgebraExpressies.rb.getString("popup1Label6"));
		mi.addActionListener(this);
		popup.add(mi);
		
		add(popup);
		
	}
	
	public void paint(Graphics g)
  	{ 	Color achtergrondkleur = Color.white;
		if(pijlIn1!=null)achtergrondkleur = new Color(220,220,220);
		
		g.setColor(Color.gray);
		g.fillRect(0,10,getSize().width-1,getSize().height-11);
		g.setColor(Color.black);
		g.drawRect(0,10,getSize().width-1,getSize().height-11);
		
		super.paint(g);
		
		int labelCorr = 0;
		int tabelCorr = 0;
		if(labelZichtbaar)labelCorr = 20;
		if(tabelZichtbaar)tabelCorr = 152;
		g.setColor(achtergrondkleur);
		g.fillRect(2,labelCorr+12,getSize().width-5,getSize().height-labelCorr-tabelCorr-15);
		g.setColor(Color.black);
		g.drawRect(2,labelCorr+12,getSize().width-5,getSize().height-labelCorr-tabelCorr-15);
		
		g.setFont(f);
		if(expressie!=null)
		{	if(toonWaarde && expressie.geefVarNaam()==null)g.drawString(waardeString, (getSize().width-fm.stringWidth(waardeString))/2, getSize().height-tabelCorr-5);
			else expressie.teken(g, (getSize().width-expressie.breedte)/2, 12 + (labelCorr+getSize().height-tabelCorr-15 - expressie.hoogte)/2);
		}
	}
	
	public void zetToonWaarde(boolean b)
	{	toonWaarde = b;
		zetMaat();
	}
	
	public void toonLabel(boolean b)
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
	
	public String geefLabelTekst()
	{	return label.geefTekst();
	}
		
	
	public void setSize(int b, int h)
	{	label.setSize(b,20);
		super.setSize(b,h);
	}
	
	public void zetMaat()
	{	int b = 40;
		int h = 30;
		int corr = 0;
		if(expressie!=null)
		{	b = expressie.breedte;
			h = expressie.hoogte;
			if(toonWaarde && expressie.geefVarNaam()==null && waardeString!=null)
			{	b = fm.stringWidth(waardeString);
				h = 0;
			}
			if(b > 26)b = b+14;
			else b = 40;
			if(h > 12)h = 20 + ((h+5)/10)*10;
			else h = 30;
		}
		if(labelZichtbaar)
		{	corr = 20;
			h=h+20;
			b = Math.max(b,label.geefBreedte());
		}
		if(tabelZichtbaar)
		{	h=h+152;
			b = Math.max(b,tabel.geefBreedte());
		}
		setSize(b,h);
		label.setSize(b,20);
		tabel.setBounds(0,h-152,b,152);
		tf.setBounds(2,corr+10,b-5,20);

	}
	
	public Expressie geefUitvoer(int max)
	{	return expressie;
	}
	
	public void zetVeranderd(int max)
	{	if(pijlIn1!=null)
		{	expressie = pijlIn1.zender.geefUitvoer(20);
		}
		else 
		{	expressie = beginw;
		}
		tabel.zetExpressie(expressie);
		if(expressie!=null)
		{	expressie.zetMaat(fm);
			Double waarde = expressie.geefWaarde();
			if(waarde!=null)waardeString = Expressie.df.format(waarde);
			else waardeString = "-";
		}
		zetMaat();
		super.zetVeranderd(max);
	}
	
	public void zetGrafiek(boolean b, GrafiekComponent gc)
	{	grafiek = b;
		tabel.zetSelectMogelijk(b);
		if(b)grafiekComponent = gc;
		else grafiekComponent = null;
	}
	
	public void zetTabel(int beginwaarde, int selectnummer, String varN, double schaalFactorX)
	{	if(grafiek)
		{	tabel.zetTabel(beginwaarde, selectnummer, varN, schaalFactorX);
			
		}
		zetMaat();
	}
	
	public void zetGrafiekTabel(int beginwaarde, int selectnummer, String varN, double schaalFactorX)
	{	if(grafiek)
		{	grafiekComponent.zetTabel(beginwaarde, selectnummer, varN, schaalFactorX);
			
		}
		zetMaat();
	}
	public void zetInvulWaarde()
	{	boolean isGeldigeInvoer=true;
		{	try
			{	Double w = Double.valueOf(tf.getText());
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
		else if(((MenuItem)e.getSource()).getLabel().equals(AlgebraExpressies.rb.getString("popup1Label1")))
		{	toonLabel(true);
		}
		else if(((MenuItem)e.getSource()).getLabel().equals(AlgebraExpressies.rb.getString("popup1Label2")))
		{	toonLabel(false);
		}
		else if(((MenuItem)e.getSource()).getLabel().equals(AlgebraExpressies.rb.getString("popup1Label3")))
		{	toonTabel(true);
		}
		else if(((MenuItem)e.getSource()).getLabel().equals(AlgebraExpressies.rb.getString("popup1Label4")))
		{	toonTabel(false);
		}
		else if(((MenuItem)e.getSource()).getLabel().equals(AlgebraExpressies.rb.getString("popup1Label5")))
		{	if(pijlIn1!=null)pijlIn1.zender.zetBoomZichtbaar(true);
			if(pijlIn2!=null)pijlIn2.zender.zetBoomZichtbaar(true);
			open = true;
		}
		else if(((MenuItem)e.getSource()).getLabel().equals(AlgebraExpressies.rb.getString("popup1Label6")))
		{	if(pijlIn1!=null)pijlIn1.zender.zetBoomZichtbaar(false);
			if(pijlIn2!=null)pijlIn2.zender.zetBoomZichtbaar(false);
			open = false;
		}
	}
	
	public void mousePressed(MouseEvent e)
	{	
		muisrechts = false;
		if(e.getModifiers()== e.BUTTON3_MASK || e.isControlDown())
		{	requestFocus();
			muisrechts = true;
			popup.show(this,e.getX(),e.getY());
			return;
		}
		super.mousePressed(e);
	}
	public void mouseClicked(MouseEvent e)
	{	if(!muisrechts && pijlIn1==null)
		{	add(tf);
			tf.setVisible(true)	;
			tf.setEnabled(true);
			tf.selectAll();
			tf.requestFocus();
		}
	}
	public void mouseReleased(MouseEvent e)
	{	super.mouseReleased(e);
	}
	
	public void focusLost(FocusEvent e)
	{	if(e.getSource() == tf)
		{	zetInvulWaarde();
			
		}
	}
	public void focusGained(FocusEvent e)
	{	
	}
}
