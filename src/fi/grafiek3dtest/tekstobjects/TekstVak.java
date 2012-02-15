package fi.grafiek3dtest.tekstobjects;

// wat gesnoeid

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.awt.datatransfer.*;

import fi.grafiek3dtest.Grafiek3DTest;
import fi.grafiek3dtest.formuleobjects.*;
import fi.grafiek3dtest.InteractiePanelContainerIF;

//import fi.wiskopdr.*;
import fi.beans.wiskopdrbeans.*;

import javax.swing.*;

public class TekstVak extends JLayeredPane  implements TekstElement, ActionListener, MouseListener, MouseMotionListener,KeyListener, FocusListener, ClipboardOwner
{
	private TekstVak tekstVak;
	private int ashoogte;
	
	protected static String clipboard;
	
	private TekstBuffer tekst;
	private int breedte, hoogte;
	
	private int aantalRegels;
	private  TekstRegel[] regels;
	private TekstRegel actieveRegel;
	
    private Font fontGR = Grafiek3DTest.tekstFont;//new Font("SansSerif", Font.PLAIN, 12);//	
    //private Font font = new Font("TimesRoman",Font.PLAIN,16);
	private Font font = Grafiek3DTest.tekstFont;	//new Font("SansSerif", Font.PLAIN, 12);
	private FontMetrics fm;
	
	private boolean selectable = true;
	private boolean editable = true;
	protected  boolean selected = false;
		
	private int caretPos;
	private int kc;
	private int marge = 5;
	private int margeBoven = 0;
	
	private FormuleVak formuleVak;
	private boolean formMode; 
	
	private Clipboard systemClipboard;
	
	private boolean centerH = false;
	private boolean centerV = false;
	
	private int interlinie = 0;
	
	public TekstVak()
	{	setLayout(null);
		addMouseListener(this);
		addMouseMotionListener(this);
		addKeyListener(this);
		addFocusListener(this);
		try
		{	systemClipboard = getToolkit().getSystemClipboard ();
		}
		catch(Exception e)
		{	systemClipboard = null;
		}

		
		if(Grafiek3DTest.deployVariant!=null && Grafiek3DTest.deployVariant.equals("GR"))super.setFont(fontGR);
		else super.setFont(font);
		
		fm = getFontMetrics(getFont());
		
		if(Grafiek3DTest.deployVariant!=null && Grafiek3DTest.deployVariant.equals("MW")) marge = 0;
		
		regels = new TekstRegel[500];	
		regels[0] = new TekstRegel(this);
		regels[0].setLocation(marge,margeBoven);
		add(regels[0]);
		aantalRegels = 1;
		
		actieveRegel = regels[0];
					
		setSize(regels[0].getSize().width+2*marge, regels[0].getSize().height+2*margeBoven);
		ashoogte = regels[0].getAsHoogte();
		
		tekstVak = this;
		tekst = new TekstBuffer(this,"");
		vulVak(tekst.toString());
		
		//formules = new Vector();
		setOpaque(false);
		
		//setBorder(BorderFactory.createLineBorder(Color.red));
		
	}
	
	public void setBackground(Color c)
	{	super.setBackground(c);
		Component[] components = getComponents();
		for(int i=0 ; i<components.length ; i++)
		{
			components[i].setBackground(c);
		}
		
	}
	
	public void setFont(Font font)
	{	this.font = font;
		fm = getFontMetrics(font);
		super.setFont(font);
		for(int i=0 ; i<aantalRegels; i++)
		{	regels[i].setFont(font);
		}
	}
	
	public void setForeground(Color c)
	{
		super.setForeground(c);
		Component[] components = getComponents();
		for(int i=0 ; i<components.length ; i++)
		{
			components[i].setForeground(c);
		}
	}
	
	public Font getFont()
	{	return font;
	}
	
	public void zetTekst(String s)
	{	
		Vector tekstDeelVakken = tekst.geefDeelVakken();
		for(int i=0 ; i<tekstDeelVakken.size() ; i++)
		{	TekstDeelVak tdv = (TekstDeelVak)tekstDeelVakken.elementAt(i);
// snoei		
			//if(tdv instanceof TekstInteractiePanelVak)((TekstInteractiePanelVak)tdv).removeActionListener(this);
		}
		
		
		Vector v = tekst.geefInteractiePanels();
	    for(int i=0 ; i<v.size() ; i++)
		{	((InteractiePanelContainerIF)v.elementAt(i)).destroy();
		}
		tekst = new TekstBuffer(this,s);
		vulVak(tekst.toString());
	}
	
	public void zetTekst(String s, boolean setEditState)
	{	
		Vector tekstDeelVakken = tekst.geefDeelVakken();
		for(int i=0 ; i<tekstDeelVakken.size() ; i++)
		{	TekstDeelVak tdv = (TekstDeelVak)tekstDeelVakken.elementAt(i);
			//if(tdv instanceof TekstInteractiePanelVak)((TekstInteractiePanelVak)tdv).removeActionListener(this);
		}
		Vector v = tekst.geefInteractiePanels();
	    for(int i=0 ; i<v.size() ; i++)
		{	((InteractiePanelContainerIF)v.elementAt(i)).destroy();
		}
	    tekst = new TekstBuffer(this,s, setEditState);
		vulVak(tekst.toString());
	}
	
	public FormuleVak geefFormuleVak()
	{	return formuleVak;
	}
	
		
	public void vulVak(String s)
	{	//s = s+' ';
		for(int i=0 ; i<getComponentCount() ; i++)
		{	Component c = getComponent(i);
// snoei		
			//if(c instanceof TekstInteractiePanelVak) 
			//{	//((TekstInteractiePanelVak)c).removeActionListener(this);
			//	//((TekstInteractiePanelVak)c).destroy();
			//	remove(c);
			//	i--;
			//}
		}
		
		StringTokenizer tok = new StringTokenizer(s," \n@",true);
		boolean widthResizable = false;//getParent()instanceof TekstVakPanel && ((TekstVakPanel)getParent()).isWidthResizable();
		if(widthResizable) tok = new StringTokenizer(s,"\n",true);	
		
		String[] regelInhouden = new String[500];
		int[] regelLengten = new int[500];
		int regelNr = 0;
		int formNr = 0;
		while(tok.hasMoreTokens())
		{	String woord = tok.nextToken();
			int formVakBreedte = 0;
			if(woord.equals("@") && tekst.geefAantalFormules()>formNr)
			{	formVakBreedte = tekst.geefDeelVak(formNr).getSize().width; 
			
				TekstDeelVak tdv =  tekst.geefDeelVak(formNr);
// snoei				
//				if(tdv instanceof TekstInteractiePanelVak  
//						&& ((TekstInteractiePanelVak)tdv).getInteractiePanel() instanceof TekstVakPanel
//						&& ((TekstVakPanel)((TekstInteractiePanelVak)tdv).getInteractiePanel()).isZwevend())
//					formVakBreedte = 15;
			}
			if(regelInhouden[regelNr]==null)regelInhouden[regelNr]="";
			if(regelInhouden[regelNr+1]==null)regelInhouden[regelNr+1]="";
			
			if(woord.equals("\n") && tok.hasMoreTokens() )
			{	regelInhouden[regelNr] += woord;
				regelNr++;
			}
			else if(woord.equals("\n") && !tok.hasMoreTokens() )
			{	regelInhouden[regelNr] += woord;
				//regelNr++;
			}
			else if(regelLengten[regelNr]==0)// && !woord.equals(" "))
			{	regelInhouden[regelNr] += woord;
				if(woord.equals("@"))
				{	regelLengten[regelNr] += formVakBreedte;
					formNr++;
				}
				else
				regelLengten[regelNr] += fm.stringWidth(woord);
			}
			else if(widthResizable || regelLengten[regelNr] > 0 && (woord.equals("@")? 0 : fm.stringWidth(woord))+ formVakBreedte + 2*marge + 2< getSize().width - regelLengten[regelNr])
			{	regelInhouden[regelNr] += woord;
				if(woord.equals("@"))
				{	regelLengten[regelNr] += formVakBreedte;
					formNr++;
				}
				else
				regelLengten[regelNr] += fm.stringWidth(woord);
			}
			else if(regelLengten[regelNr] > 0 && (woord.equals("@")? 0 : fm.stringWidth(woord))+formVakBreedte + 2*marge + 2 >= getSize().width - regelLengten[regelNr])
			{	if(regelLengten[regelNr+1]==0 && woord.equals(" "))
				{	regelInhouden[regelNr] += woord;
					regelNr++;
				}
				else if(regelLengten[regelNr+1]==0)// && !woord.equals(" "))
				{	regelInhouden[regelNr+1] += woord;
					if(woord.equals("@"))
					{	regelLengten[regelNr+1] += formVakBreedte;
						formNr++;
					}
					else
					regelLengten[regelNr+1] += fm.stringWidth(woord);
					regelNr++;
				}
				else if(regelLengten[regelNr+1] > 0)
				{	regelInhouden[regelNr+1] += woord;
					if(woord.equals("@"))
					{	regelLengten[regelNr+1] += formVakBreedte;
						formNr++;
					}
					else
					regelLengten[regelNr+1] += fm.stringWidth(woord);
					regelNr++;
				}
			}
		}
		formNr = 0;
		int aantalGevuld = regelNr;
		actieveRegel.setCaretVisible(false);
		for(int i=0 ; i<aantalGevuld+1; i++)
	    {	if(regels[i]==null)
	    	{	regels[i] = new TekstRegel(this);
	    		regels[i].setBackground(getBackground());
	    		if(i==0)regels[i].setLocation(marge,margeBoven);
	    		else regels[i].setLocation(marge,regels[i-1].getLocation().y + regels[i-1].getSize().height);
				add(regels[i]);
				aantalRegels++;
				produceAction("resize");
			}
			regels[i].setVisible(false);
	    	regels[i].removeAll();
	    	for(int j=0 ; regelInhouden[i]!=null && j<regelInhouden[i].length(); j++)
			{	char c = regelInhouden[i].charAt(j);
				if(c=='@')
				{	
					TekstDeelVak tdv =  tekst.geefDeelVak(formNr);
// snoei					
					//if(tdv instanceof TekstInteractiePanelVak  
					//		&& ((TekstInteractiePanelVak)tdv).getInteractiePanel() instanceof TekstVakPanel
					//		&& ((TekstVakPanel)((TekstInteractiePanelVak)tdv).getInteractiePanel()).isZwevend())
					//	{		TekstVakPanel tvp = ((TekstVakPanel)((TekstInteractiePanelVak)tdv).getInteractiePanel());
					//			tdv.setLocation(tvp.geefLocatie());
					//			this.setLayer((Component)tdv, JLayeredPane.PALETTE_LAYER.intValue());
					//			this.add(tdv,0);
					//			TekstTeken tt = new TekstTeken('\u25cb');
					//			if(editable)regels[i].insert(tt);
					//			((TekstInteractiePanelVak)tdv).setAnchor(tt);
					//	}
					
					//else 
						regels[i].insert(tekst.geefDeelVak(formNr));
					formNr++;
				}
				else 
				{	TekstTeken tt = new TekstTeken(regelInhouden[i].charAt(j));
					tt.setForeground(getForeground());
					regels[i].insert(tt);
				}
			}
			regels[i].setVisible(true);
	    }
	    int laatsteRegel = aantalRegels;
	    for(int i=aantalGevuld+1 ; i<laatsteRegel; i++)
	    {	remove(regels[i]);
	    	regels[i] = null;
	    	aantalRegels--;
	    	produceAction("resize");
	    }
	    //setCaret(caretPos);
	    if(widthResizable)zetMaat();
	}
	
	public void setCaretPositionEnd()
	{	setCaretPosition(regels[aantalRegels-1], regels[aantalRegels-1].getComponentCount()-1);
	}
	
	public void setCaretPosition(TekstRegel tr, int pos)
	{	int rn = geefRegelNummer(tr);
		actieveRegel.setCaretVisible(false);
		int teller = 0;
		for(int i=0 ; i<rn; i++)
		{	teller += regels[i].getComponentCount();
		}
		setCaret(teller+pos);
		formMode = false;
		produceAction("tekst");
	}
		
	public void setCaret(int pos)
	{	for(int i=0 ; i<aantalRegels; i++)
		{	regels[i].setCaretVisible(false);
			regels[i].deSelect();
		}
		//actieveRegel.setCaretVisible(false);
		//actieveRegel.deSelect();
		caretPos = pos;
		int teller = 0;
		for(int i=0 ; i<aantalRegels; i++)
		{	int tellerOud = teller;
			teller += regels[i].getComponentCount();
			if(teller>pos)
			{	actieveRegel = regels[i];
				actieveRegel.setCaret(pos - tellerOud);
				actieveRegel.setCaretVisible(true);
				break;
			}
		}
	}
	
	public TekstElement elementAt(int pos)
	{	TekstElement te = null;
		int teller = 0;
		for(int i=0 ; i<aantalRegels; i++)
		{	int tellerOud = teller;
			teller += regels[i].getComponentCount();
			if(teller>pos)
			{	te = (TekstElement)regels[i].getComponent(pos - tellerOud);
				break;
			}
		}
		return te;
	}
	
	public void insert(String s)
	{	zetTekst(tekst.insertAndComplete(caretPos,s), true);
		repaint();
	}
	
	
	public void insertFormuleVak(int nr)
	{	tekst.insert(caretPos,'@');
		TekstFormuleVak tfvNieuw = null;
		if(nr==0) tfvNieuw = new TekstFormuleVak(this);
		if(nr==1) tfvNieuw = new TekstFormuleVak(this,true);
		//tfvNieuw.setBackground(getBackground());
		tfvNieuw.setForeground(getForeground());
		
		tekst.insertFormuleVak(caretPos,tfvNieuw);
		vulVak(tekst.toString());
		formuleVak = tfvNieuw.geefFormuleVak();
		actieveRegel.setCaretVisible(false);
		formuleVak.requestFocus();
		repaint();
	}
	
	public void insertAntwoordVak()
	{	tekst.insert(caretPos,'@');
		TekstAntwoordVak tfvNieuw = new TekstAntwoordVak(this);
		tfvNieuw.setEditMode(editable);
		tekst.insertAntwoordVak(caretPos,tfvNieuw);
		vulVak(tekst.toString());
		repaint();
		produceAction("resize");
	}

// snoei	
/*	
	public void insertTekstInteractiePanelVak(int setNr)
	{	tekst.insert(caretPos,'@');
		TekstInteractiePanelVak tfvNieuw = new TekstInteractiePanelVak(this, setNr);
		tfvNieuw.addActionListener(this); 
		tfvNieuw.setBackground(getBackground());
		tfvNieuw.setEditMode(editable);
		tekst.insertTekstInteractiePanelVak(caretPos,tfvNieuw);
		vulVak(tekst.toString());
		repaint();
		produceAction("resize");
	}
*/	
// snoei	
/*	
	// alleen nog te gebruiken voor een grafiekencomponent
	public void insertStudentTekstInteractiePanelVak(int soortInteractiePanel)
	{	tekst.insert(caretPos,'@');
	
		Hashtable grafiekEditState = new GrafiekEditPanel().getEditState();
		//grafiekEditState.put("newVersion", new Boolean(true));
		
		Hashtable launchData = new Hashtable();
		launchData.put("setNr", new Integer(2));
		launchData.put("soortInteractiePanel", new Integer(soortInteractiePanel));
		launchData.put("interactiePanelLaunchState", grafiekEditState);
		launchData.put("breedte", new Integer(250));
		launchData.put("hoogte", new Integer(350));
		launchData.put("popup", new Boolean(true));
		
		TekstInteractiePanelVak tfvNieuw = new TekstInteractiePanelVak(this, launchData);
		tfvNieuw.addActionListener(this); 
		tfvNieuw.setBackground(getBackground());
		tfvNieuw.setEditMode(false);
		tfvNieuw.setStudentEditor(true);
		tekst.insertTekstInteractiePanelVak(caretPos,tfvNieuw);
		vulVak(tekst.toString());
		repaint();
		produceAction("resize");
	}
*/
// snoei	
/*	
	public void insertLinkVak()
	{	tekst.insert(caretPos,'@');
		TekstLinkVak tfvNieuw = new TekstLinkVak(this);
		tekst.insertLinkVak(caretPos,tfvNieuw);
		tfvNieuw.editLink();
		vulVak(tekst.toString());
		tfvNieuw.requestFocus();
		repaint();
	}
*/
// snoei	
/*	
	public void insertImage()
	{	tekst.insert(caretPos,'@');
		TekstImageVak plNieuw = new TekstImageVak(this);
		plNieuw.setEditMode(editable);
		tekst.insertImageVak(caretPos,plNieuw);
		plNieuw.editImage();
		vulVak(tekst.toString());
		repaint();
		produceAction("resize");
	}
*/	
	//public void paint(Graphics g)
	//{	super.paint(g);
	//}
	
	public void layoutTekst()
	{	if(tekst!=null)vulVak(tekst.toString());
		produceAction("resize");
	
	}
	
	public void setBounds(int x, int y, int b, int h)
	{	breedte = b;
		hoogte = h;
		super.setBounds(x,y,b,h);
	}
	
	public int geefAantalregels()
	{	return aantalRegels;
	}
	
	public void setCenterH(boolean b)
	{
		centerH = b;
	}
	
	public void setCenterV(boolean b)
	{
		centerV = b;
	}
	
	public void layoutResize(int b, int h)
    {   breedte = b;
        hoogte = h;
	    setSize(breedte,hoogte);
        
	    fm = getFontMetrics(getFont());
		int regelafstand = fm.getAscent()+fm.getDescent()+interlinie;
		
		int hoogteRegels = 0;
        for(int i=0 ; i<aantalRegels; i++)
        {   int corr = 0;
        	if(i>0)corr = Math.max(regelafstand-(regels[i-1].getHeight()-regels[i-1].getAsHoogte()+regels[i].getAsHoogte()), 0);
		   	hoogteRegels += regels[i].getSize().height + corr;
        }
        
        int x = marge;
        if(centerH) x = breedte/2 - regels[0].getSize().width/2;
        int y = margeBoven + ashoogte-regels[0].getAsHoogte();
        if(centerV) y = hoogte/2 - hoogteRegels/2;
        
        regels[0].setLocation(x,y);
        for(int i=1 ; i<aantalRegels; i++)
        {   x = marge;
            if(centerH) x = breedte/2 - regels[i].getSize().width/2;
            int corr = Math.max(regelafstand-(regels[i-1].getHeight()-regels[i-1].getAsHoogte()+regels[i].getAsHoogte()), 0);
			regels[i].setLocation(x,regels[i-1].getLocation().y + regels[i-1].getSize().height + corr);
        }
        //if(regels[0]!=null)ashoogte = 1+regels[0].getAsHoogte();
    }
	
	public void resize()
	{
		fm = getFontMetrics(getFont());
		int regelafstand = fm.getAscent()+fm.getDescent()+interlinie;
		//System.out.println("regelafstand: "+(fm.getAscent()+fm.getDescent()));
		
		hoogte = 0;
		for(int i=0 ; i<aantalRegels; i++)
	    {	hoogte += regels[i].getSize().height;
	    }
		
		int x = marge;
		if(centerH) x = getSize().width/2 - regels[0].getSize().width/2;
		int y = margeBoven;
		if(centerV) y = getSize().height/2 - hoogte/2;
		
		regels[0].setLocation(x,y);
		hoogte = 2*margeBoven + Math.max(regelafstand,regels[0].getSize().height);
		int nieuweBreedte = 2*marge + regels[0].getSize().width;
		for(int i=1 ; i<aantalRegels; i++)
	    {	
			x = marge;
			if(centerH) x = getSize().width/2 - regels[i].getSize().width/2;
			int corr = Math.max(regelafstand-(regels[i-1].getHeight()-regels[i-1].getAsHoogte()+regels[i].getAsHoogte()), 0);
			regels[i].setLocation(x,regels[i-1].getLocation().y + regels[i-1].getSize().height + corr);
			hoogte += regels[i].getSize().height + corr;
			nieuweBreedte = Math.max(nieuweBreedte, 2*marge + regels[i].getSize().width);
	    }
		if(regels[0]!=null)ashoogte = regels[0].getAsHoogte();
		if(getParent()instanceof TekstElement)((TekstElement)getParent()).zetMaat();
		
		for(int i=0 ; i<getComponentCount() ; i++)
		{	hoogte = Math.max(hoogte, getComponent(i).getLocation().y + getComponent(i).getSize().height+1);
		}
// snoei		
//		if(getParent()instanceof TekstVakPanel && ((TekstVakPanel)getParent()).isWidthResizable()) setSize(nieuweBreedte,hoogte);	
//		else 
			setSize(breedte,hoogte);
	}
	public void zetMaat()
	{	
		resize();
		if(getParent()instanceof TekstArea)((TekstArea)getParent()).resize();
// snoei		
//		if(getParent()instanceof TekstVakPanel)((TekstVakPanel)getParent()).zetMaat();
		
	}
	
	public void setInterlinie(int interlinie)
	{	this.interlinie = interlinie;
	}
	
	public void zetMarge(int marge)
	{	this.marge = marge;
	}
	
	public void zetBovenMarge(int margeBoven)
    {   this.margeBoven = margeBoven;
    }
	
	public void zetFormuleVak(FormuleVak fv)
	{	formMode = true;
		actieveRegel.setCaretVisible(false);
		formuleVak = fv;
		produceAction("formule");
	}
	
	public void zetActieveRegel(TekstRegel fr)
	{	if(actieveRegel!=null)
		{	actieveRegel.deSelect();
			actieveRegel.knipper(false);
		}
		actieveRegel = fr;
		produceAction("focus");
	}
	
	private int geefRegelNummer(TekstRegel tr)
	{	for(int i=0 ; i<aantalRegels; i++)
		{	if(regels[i]==tr)return i;
		}
		return -1;
	}
	
	public TekstRegel geefVolgendeRegel(TekstRegel tr)
	{	TekstRegel volgende = null;
		int nr = geefRegelNummer(tr);
		if(nr<aantalRegels-1)volgende = regels[nr+1];
		return volgende;
	}
	
	public TekstRegel geefVorigeRegel(TekstRegel tr)
	{	TekstRegel vorige = null;
		int nr = geefRegelNummer(tr);
		if(nr>0)vorige = regels[nr-1];
		return vorige;
	}
	
	public boolean deleteSelection()
	{	
		int firstIndex = -1;
		int lastIndex = -1;
		for(int i=0 ; i<tekst.length()-1; i++)
	    {	boolean b1 = false;
	    	boolean b2 = false;
	    	if(elementAt(i)!=null)b1 = elementAt(i).isSelected();
	    	if(elementAt(i+1)!=null)b2 = elementAt(i+1).isSelected();
	    	if(b1 && i==0)
			{	firstIndex = 0;
			}
			else if(!b1 && b2)
			{ 	firstIndex = i+1;
			}
			else if(firstIndex>-1 && b1 && !b2)
			{	lastIndex = i;
			}
			else if(firstIndex>-1 && b2 && i==tekst.length()-2)
			{	lastIndex = i+1;
			}
		}
		
       
        
		/*System.out.println("start");
		int teller = -1;
		for(int i=0 ; i<aantalRegels; i++)
	    {	for(int j=0 ; j<regels[i].getComponentCount()-1; j++)
			{	teller++;
				boolean b1 = ((TekstElement)regels[i].getComponent(j)).isSelected();
				boolean b2 = ((TekstElement)regels[i].getComponent(j+1)).isSelected();
				if(b1 && teller==0)
				{	firstIndex = 0;
				}
				else if(!b1 && b2)
				{ 	firstIndex = teller+1;
				}
				else if(firstIndex>-1 && b1 && !b2)
				{	lastIndex = teller;
				}
				else if(firstIndex>-1 && b2 && i==aantalRegels-1 && j==regels[i].getComponentCount()-2)
				{	lastIndex = teller+1;
				}
			}
	    }
	    System.out.println(Integer.toString(firstIndex));
	    System.out.println(Integer.toString(lastIndex));*/
	    if(firstIndex>-1 && lastIndex>-1)
	    {	tekst.delete(firstIndex,lastIndex);
	    	vulVak(tekst.toString());
	    	setCaret(firstIndex);
	    	return true;
	    }
	    if(tekst.length()>0 && elementAt(0).isSelected())
		{	tekst.delete(0,0);
	    	vulVak(tekst.toString());
	    	setCaret(0);
	    	return true;
	    }
	    return false;	
	}
	
	public void setEditable(boolean b)
	{	editable = b;
		for(int i=0 ; i<aantalRegels; i++)
		{	if(regels[i]!=null)regels[i].setEditable(b);
		}
		
	}
	
	public void setSelectable(boolean b)
	{	selectable = b;
		for(int i=0 ; i<aantalRegels; i++)
		{	if(regels[i]!=null)regels[i].setSelectable(b);
		}
	}
	
	public boolean isEditable()
	{	return editable;
	}
	
	public boolean isSelectable()
	{	return selectable;
	}
	
	//public void requestFocus()
	//{	actieveRegel.requestFocus();
	//}
	
	public void finish()
	{	produceAction("ingevuld");
	}
	
	public void verwerkSelectie()
	{	if(selectable )
		{	if(actieveRegel.getComponentCount()>0 && ((TekstElement)actieveRegel.getComponent(0)).isSelected())
			{	produceAction("$t" + actieveRegel.toString() + "@");
			}
			else
			{	produceAction("");
			}
		}
	}
	
	public String toString()
	{	return tekst.toCompleteString();
	}
	
	public String toCompleteString()
	{	return tekst.toCompleteEditString();
	}
	
	public void requestFocus()
	{	if(formMode)formuleVak.requestFocus();
		else 
		{	super.requestFocus();
			
		}
		
	}
	
	public void zetTekstFocus()
	{	formMode = false;
		produceAction("tekst");
		requestFocus();
	}
	
	public Vector geefInteractiePanels()
	{	return tekst.geefInteractiePanels();
	}
	
	public Vector geefInteractiePanels(Vector v)
	{	return tekst.geefInteractiePanels(v);
	}
	
	public InteractiePanel zoekInteractiePanel(int ID)
	{
		return tekst.zoekInteractiePanel(ID);
	}
	
	/*
	public InteractiePanel zoekInteractiePanel(int ID)
	{
		InteractiePanel ip = tekst.zoekInteractiePanel(ID);
		if (ip!=null) return ip;
		else if(getParent()instanceof TekstVakPanel)
		{	return ((TekstInteractiePanelVak)getParent().getParent()).zoekInteractiePanel(ID);
		}
		return null;
		
		
	*/
	
	public void actionPerformed(ActionEvent e)
	{	
		if(e.getActionCommand().equals("tekst"))
		{	produceThisAction(e);
		//System.out.println("raak"+e.getSource().toString());
			return;
		}
		if(e.getActionCommand().equals("formule"))
		{	produceThisAction(e);
		//System.out.println("raak"+e.getSource().toString());
			return;
		}
	}
	public void mousePressed(MouseEvent e)
	{	int x = e.getX();
		int y = e.getY();
		
		for(int i=0 ; i<aantalRegels; i++)
		{	int rgx = regels[i].getLocation().x;
			int rgy = regels[i].getLocation().y;
			int rgb = regels[i].getSize().width;
			int rgh = regels[i].getSize().height;
			if(y > rgy  && y < rgy+rgh)
			{	if(x<rgx)
				{	setCaretPosition(regels[i],0);
					//System.out.println("setCaretPosition regelbegin"+i);
				}
				else if(x>rgx+rgb)
				{	setCaretPosition(regels[i],regels[i].getComponentCount()-1);
					//System.out.println("setCaretPosition regeleind"+i);
				}
			}
		}
		zetTekstFocus();
	}
	
	public void mouseClicked(MouseEvent e){;}
	public void mouseReleased(MouseEvent e){;}
	public void mouseEntered(MouseEvent e){;}
	public void mouseExited(MouseEvent e){;}
	
	public void mouseDragged(MouseEvent e)
	{
	}
	public void mouseMoved(MouseEvent e)
	{
	}
	
	public void lostOwnership (Clipboard parClipboard, Transferable parTransferable) 
	{	 //System.out.println ("Lost ownership");
	}
		 
	public boolean pasteFromSystemClipboard()
    {	if(systemClipboard==null)return false;
		Transferable clipboardContent = systemClipboard.getContents(this);
		 	
		 if ((clipboardContent != null) && (clipboardContent.isDataFlavorSupported (DataFlavor.stringFlavor))) 
		 {
		 	try 
		 	{ 	String tempString;
		 		tempString = (String) clipboardContent.getTransferData(DataFlavor.stringFlavor);
		 		tekstVak.insert(tempString);
		 		return true;
		    }
		    catch (Exception e) 
		    {  	 e.printStackTrace ();
		    		return false;
		    }
		 }
		 else return false;
    }
	
	public boolean copyToSystemClipboard(String s)
	{	if(s==null || systemClipboard==null)return false;
		StringSelection content = new StringSelection(s);
		systemClipboard.setContents(content, TekstVak.this);
		return true;

	}
	public void keyPressed(KeyEvent e)
	{   if (editable)
        {   kc = e.getKeyCode();
            if (e.isControlDown() && kc == KeyEvent.VK_V)
            {	deleteSelection();
              	if(!pasteFromSystemClipboard())tekstVak.insert(TekstVak.clipboard);
            }
            else if (e.isControlDown() && kc == KeyEvent.VK_C)
            {	copySelection();
            }
            else if (e.isControlDown() && kc == KeyEvent.VK_X)
            {	copySelection();
            	deleteSelection();
            }
            else if (e.isAltDown() && kc == KeyEvent.VK_F)
            {	insertFormuleVak(0);
            }
            else if (kc == KeyEvent.VK_LEFT)
            {   if (caretPos > 0 && tekst.charAt(caretPos-1)!='@')
                {   caretPos--;
                }	
	            else if (caretPos > 0)
	            {	TekstFormuleVak tfv = tekst.geefTekstFormuleVak(caretPos-1);
	            	if(tfv != null) tfv.neemFocus("links");
	            	else caretPos--;
	            }
			}
            else if (kc == KeyEvent.VK_RIGHT)
            {  
            	if (caretPos < tekst.length()-1 && tekst.charAt(caretPos)!='@')
                {   caretPos++;
				}
	            else if (caretPos < tekst.length()-1)
	            {	TekstFormuleVak tfv = tekst.geefTekstFormuleVak(caretPos);
	            	if(tfv != null) tfv.neemFocus("rechts");
	            	else caretPos++;
	            }
	        }
            else if (kc == KeyEvent.VK_UP)
            {   if(actieveRegel==null) return;
            	int rn = geefRegelNummer(actieveRegel);
            	if(rn>0)setCaretPosition(regels[rn-1],0);
            }
            else if (kc == KeyEvent.VK_DOWN)
            {   if(actieveRegel==null) return;
            	int rn = geefRegelNummer(actieveRegel);
            	if(rn<aantalRegels-1)setCaretPosition(regels[rn+1],0);
            }
			else if (kc == KeyEvent.VK_HOME)
            {   caretPos = 0;
            }
            else if (kc == KeyEvent.VK_END)
            {   caretPos = tekst.length()-1;
			}
            else if (kc == KeyEvent.VK_DELETE)
            {	boolean b = deleteSelection();
            	if(!b)tekst.deleteCharAt(caretPos);
            	produceAction("resize");
            } 
			else if (kc == KeyEvent.VK_BACK_SPACE)
            {   if (caretPos > 0 )
                {   boolean b = deleteSelection();
            		if(!b)
            		{	tekst.deleteCharAt(caretPos-1);
                		caretPos--;
                	}
                	produceAction("resize");
                }
	  		}
	  		
         	repaint();
         	
		}
	}
    public void keyReleased(KeyEvent e) {vulVak(tekst.toString());
            setCaret(caretPos);}
    public void keyTyped(KeyEvent e)
    {	int kt = e.getKeyChar();
    	if (editable)
		{   if (kt == KeyEvent.VK_ENTER)
            {	if(tekst.charAt(caretPos)==' ')tekst.replace(caretPos,'\n');
            	else if(caretPos>0 && tekst.charAt(caretPos-1)==' ')tekst.replace(caretPos-1,'\n');
            	else 
            	{	tekst.insert(caretPos,'\n');
            		caretPos++;
            	}
            	produceAction("resize");
            	
			}
    		
			else if ((kt != KeyEvent.VK_ESCAPE) &&
					(kt != KeyEvent.VK_DELETE) &&
					(kt != KeyEvent.VK_END) &&
					(kt != KeyEvent.VK_HOME) &&
	                (kt != KeyEvent.VK_BACK_SPACE) &&
               		(kc != KeyEvent.VK_ENTER) && 
               		(kc != KeyEvent.VK_SHIFT) && 
               		(kt != '@')
                    && !e.isControlDown()
                    && !(e.isAltDown() && kc == KeyEvent.VK_F)
                    
                   )
      		{	
			    if(e.isAltDown())
	            {

			    	 if(kc == KeyEvent.VK_A) kt = '\u03b1';
				        else if (kc == KeyEvent.VK_B) kt = '\u03b2';
				        else if (kc == KeyEvent.VK_G) kt = '\u03b3';
				        else if (kc == KeyEvent.VK_D) kt = '\u03b4';
				        else if (kc == KeyEvent.VK_E) kt = '\u03b5';
				        else if (kc == KeyEvent.VK_Z) kt = '\u03b6';
				        else if (kc == KeyEvent.VK_H) kt = '\u03b7';
				        else if (kc == KeyEvent.VK_Q) kt = '\u03b8';
				        else if (kc == KeyEvent.VK_I) kt = '\u03b9';
				        else if (kc == KeyEvent.VK_K) kt = '\u03ba';
				        else if (kc == KeyEvent.VK_L) kt = '\u03bb';
				        else if (kc == KeyEvent.VK_M) kt = '\u03bc';
				        else if (kc == KeyEvent.VK_N) kt = '\u03bd';
				        else if (kc == KeyEvent.VK_X) kt = '\u03be';
				        else if (kc == KeyEvent.VK_O) kt = '\u03bf';
				        else if (kc == KeyEvent.VK_P) kt = '\u03c0';
				        else if (kc == KeyEvent.VK_R) kt = '\u03c1';
				        else if (kc == KeyEvent.VK_R) kt = '\u03c2';
				        else if (kc == KeyEvent.VK_S) kt = '\u03c3';
				        else if (kc == KeyEvent.VK_T) kt = '\u03c4';
				        else if (kc == KeyEvent.VK_U) kt = '\u03c5';
				        else if (kc == KeyEvent.VK_V) kt = '\u03c6';
				        else if (kc == KeyEvent.VK_C) kt = '\u03c7';
				        else if (kc == KeyEvent.VK_Y) kt = '\u03c8';
				        else if (kc == KeyEvent.VK_W) kt = '\u03c9';
	                

	            }
			    deleteSelection();
      			tekst.insert(caretPos,(char)kt);
				caretPos++;
            } 
			
            
            repaint();
		}
	}
	
	public void copySelection()
	{	int firstIndex = -1;
		int lastIndex = -1;
		for(int i=0 ; i<tekst.length()-1; i++)
	    {	boolean b1 = false;
	    	boolean b2 = false;
	    	if(elementAt(i)!=null)b1 = elementAt(i).isSelected();//|| elementAt(i).toString().equals("\n");
	    	if(elementAt(i+1)!=null)b2 = elementAt(i+1).isSelected();//|| elementAt(i+1).toString().equals("\n");
	    	if(b1 && i==0)
			{	firstIndex = 0;
				lastIndex = 0;
			}
			else if(!b1 && b2)
			{ 	firstIndex = i+1;
			}
			else if(firstIndex>-1 && b1 && !b2)
			{	lastIndex = i;
			}
			else if(firstIndex>-1 && b2 && i==tekst.length()-2)
			{	lastIndex = i+1;
			}
		}
	    if(firstIndex>-1 && lastIndex>-1)
	    {	if(!copyToSystemClipboard(tekst.getSelection(firstIndex,lastIndex)))
	    	TekstVak.clipboard = tekst.getSelection(firstIndex,lastIndex);
	    }
	    System.out.println(""+firstIndex+" "+lastIndex+" ");
	}
	
	public void focusGained(FocusEvent e)
    {   if (selectable)
		{	actieveRegel.knipper(true);
		}  
	}
	public void focusLost(FocusEvent e)
	{   if (selectable)
		{	actieveRegel.knipper(false);
		}  
	}
	
	

	//ActionProducer
	private ActionListener actionListener = null;
	
	public void addActionListener(ActionListener l) 
 	{	actionListener = AWTEventMulticaster.add(actionListener,l);
 	}
 	
 	public void removeActionListener(ActionListener l)
 	{	actionListener = AWTEventMulticaster.remove(actionListener, l);
 	}	
 	
 	public void produceAction(String command)
 	{	if (actionListener != null)
 		{	actionListener.actionPerformed( new ActionEvent(this, 0, command) );
 		}
 	}
 	
 	public void produceThisAction(ActionEvent e)
 	{	if (actionListener != null)
 		{	actionListener.actionPerformed(e);
 		}
 	}
 	
 	//implementation of TekstElement
 	public Component add( Component comp ) 
	{	Component c = super.add(comp);
		if(getFont()!=null)comp.setFont(getFont());
		return c;
	}
	public Component add( Component comp, int index ) 
	{	Component c = super.add(comp, index);
		if(getFont()!=null)comp.setFont(getFont());
		return c;
	}
	
	
	public boolean isSelected()
  	{	return false;
	}
	
	public boolean isSpatie()
	{	return false;
	}
	
	public void setSelected(boolean b)
  	{	
	}
	
	public void neemFocus(String richting, TekstElement fe)
	{
	}
	public void neemFocus(String richting)
	{
	}
	public TekstVak getTekstVak()
	{
		return tekstVak;
	}
	
	public int getAsHoogte()
	{
		//if(regels[0]!=null)return regels[0].getAsHoogte();
		//else 
			return ashoogte;
	}
	public void setAsHoogte(int ashoogte)
	{
		this.ashoogte = ashoogte;
	}
	public int geefMarge()
	{
		return marge;
	}
}
