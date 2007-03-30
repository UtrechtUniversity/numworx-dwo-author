package fi.algebrapijlenopdr.tekstobjects;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import fi.algebrapijlenopdr.formuleobjects.*;

public class TekstVak extends TekstElement implements MouseListener, MouseMotionListener,KeyListener, FocusListener
{
	protected static String clipboard;
	
	private TekstBuffer tekst;
	private int breedte, hoogte;
	
	private int aantalRegels;
	private  TekstRegel[] regels;
	private TekstRegel actieveRegel;
	
		
	private Font font = new Font("SansSerif",Font.PLAIN,12);
	private FontMetrics fm;
	
	private boolean selectable = true;
	private boolean editable = true;
	protected  boolean selected = false;
		
	private int caretPos;
	private int kc;
	
	private FormuleVak formuleVak;
	private boolean formMode; 
	
	private String accent;
	
	public TekstVak()
	{	setLayout(null);
		addMouseListener(this);
		addMouseMotionListener(this);
		addKeyListener(this);
		addFocusListener(this);
		super.setFont(font);
		fm = getFontMetrics(getFont());
		
		regels = new TekstRegel[100];	
		regels[0] = new TekstRegel(this);
		regels[0].setLocation(5,0);
		add(regels[0]);
		aantalRegels = 1;
		
		actieveRegel = regels[0];
					
		setSize(regels[0].getSize().width+10, regels[0].getSize().height);
		ashoogte = regels[0].ashoogte;
		
		tekstVak = this;
		tekst = new TekstBuffer(this,"");
		vulVak(tekst.toString());
		
		//formules = new Vector();
		
	}
	
	public void setFont(Font font)
	{	this.font = font;
		fm = getFontMetrics(font);
		super.setFont(font);
		for(int i=0 ; i<aantalRegels; i++)
		{	regels[i].setFont(font);
		}
	}
	public Font getFont()
	{	return font;
	}
	
	public void zetTekst(String s)
	{	tekst = new TekstBuffer(this,s);
		vulVak(tekst.toString());
	}
	
	public FormuleVak geefFormuleVak()
	{	return formuleVak;
	}
	
	public void vulVak(String s)
	{	//s = s+' ';
		StringTokenizer tok = new StringTokenizer(s," \n@",true);
		String[] regelInhouden = new String[100];
		int[] regelLengten = new int[100];
		int regelNr = 0;
		int formNr = 0;
		while(tok.hasMoreTokens())
		{	String woord = tok.nextToken();
			int formVakBreedte = 0;
			if(woord.equals("@") && tekst.geefAantalFormules()>formNr)formVakBreedte = tekst.geefDeelVak(formNr).getSize().width; 
			
			if(regelInhouden[regelNr]==null)regelInhouden[regelNr]="";
			if(regelInhouden[regelNr+1]==null)regelInhouden[regelNr+1]="";
			
			if(woord.equals("\n") && tok.hasMoreTokens() )
			{	regelInhouden[regelNr] += woord;
				regelNr++;
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
			else if(regelLengten[regelNr] > 0 && fm.stringWidth(woord)+ formVakBreedte + 10 < getSize().width - regelLengten[regelNr])
			{	regelInhouden[regelNr] += woord;
				if(woord.equals("@"))
				{	regelLengten[regelNr] += formVakBreedte;
					formNr++;
				}
				else
				regelLengten[regelNr] += fm.stringWidth(woord);
			}
			else if(regelLengten[regelNr] > 0 && fm.stringWidth(woord)+formVakBreedte + 10 >= getSize().width - regelLengten[regelNr])
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
	    		//regels[i].setFont(font);
	    		if(i==0)regels[i].setLocation(5,0);
	    		else regels[i].setLocation(5,regels[i-1].getLocation().y + regels[i-1].getSize().height);
				add(regels[i]);
				aantalRegels++;
				produceAction("resize");
			}
			regels[i].setVisible(false);
	    	regels[i].removeAll();
	    	for(int j=0 ; regelInhouden[i]!=null && j<regelInhouden[i].length(); j++)
			{	char c = regelInhouden[i].charAt(j);
				if(c=='@')
				{	regels[i].insert(tekst.geefDeelVak(formNr));
					formNr++;
				}
				else 
				regels[i].insert(new TekstTeken(regelInhouden[i].charAt(j)));
			}
			regels[i].setVisible(true);
	    }
	    int laatsteRegel = aantalRegels;
	    for(int i=aantalGevuld+1 ; i<laatsteRegel; i++)
	    {	remove(regels[i]);
	    	regels[i] = null;
	    	aantalRegels--;
	    	//produceAction("resize");
	    }
	    //setCaret(caretPos);
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
	{	zetTekst(tekst.insertAndComplete(caretPos,s));
		repaint();
	}
	public void insertFormuleVak()
	{	tekst.insert(caretPos,'@');
		TekstFormuleVak tfvNieuw = new TekstFormuleVak(this);
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
		tekst.insertAntwoordVak(caretPos,tfvNieuw);
		vulVak(tekst.toString());
		repaint();
	}
	
	public void insertLinkVak()
	{	tekst.insert(caretPos,'@');
		TekstLinkVak tfvNieuw = new TekstLinkVak(this);
		tekst.insertLinkVak(caretPos,tfvNieuw);
		vulVak(tekst.toString());
		tfvNieuw.requestFocus();
		repaint();
	}
	
	public void paint(Graphics g)
	{	super.paint(g);
	}
	
	public void setBounds(int x, int y, int b, int h)
	{	breedte = b;
		hoogte = h;
		super.setBounds(x,y,b,h);
	}
	
	public int geefAantalregels()
	{	return aantalRegels;
	}
	public void zetMaat()
	{	regels[0].setLocation(5,1);
		hoogte = 5+regels[0].getSize().height;
		for(int i=1 ; i<aantalRegels; i++)
	    {	regels[i].setLocation(5,regels[i-1].getLocation().y + regels[i-1].getSize().height);
	    	hoogte += regels[i].getSize().height;
	    }
		//ashoogte = regels[0].ashoogte;
		if(getParent()instanceof TekstElement)((TekstElement)getParent()).zetMaat();
		
		setSize(breedte,hoogte);
		if(getParent()instanceof TekstArea)((TekstArea)getParent()).resize();
	}
	
	public void zetFormuleVak(FormuleVak fv)
	{	formMode = true;
		actieveRegel.setCaretVisible(false);
		formuleVak = fv;
		produceAction("formule");
	}
	
	public void zetActieveRegel(TekstRegel fr)
	{	if(actieveRegel!=null)actieveRegel.deSelect();
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
	{	int firstIndex = -1;
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
	
	public void requestFocus()
	{	if(formMode)formuleVak.requestFocus();
		else 
		{	super.requestFocus();
			actieveRegel.knipper(true);
		}
		
	}
	
	public void zetTekstFocus()
	{	formMode = false;
		produceAction("tekst");
		requestFocus();
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
	
	public void keyPressed(KeyEvent e)
	{   if (editable)
        {   kc = e.getKeyCode();
            if (e.isControlDown() && kc == KeyEvent.VK_V)
            {	deleteSelection();
              	tekstVak.insert(TekstVak.clipboard);
            }
            else if (e.isControlDown() && kc == KeyEvent.VK_C)
            {	copySelection();
            }
            else if (e.isControlDown() && kc == KeyEvent.VK_X)
            {	copySelection();
            	deleteSelection();
            }
            else if (kc == KeyEvent.VK_LEFT)
            {   if (caretPos > 0 )
                {   caretPos--;
                }
				
			}
            else if (kc == KeyEvent.VK_RIGHT)
            {   if (caretPos < tekst.length())
                {   caretPos++;
				}
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
            	else tekst.insert(caretPos,'\n');
            	caretPos++;
            	accent = null;
			}
			else if(e.isControlDown())
			{	//if(kt == ',')
				{	accent = ",";
				tekst.insert(caretPos,'\u2264');
				caretPos++;
				accent = null;
				}
				
			}
			else if(accent!=null)
			{	if(kt == 'e')
				{	deleteSelection();
	      			tekst.insert(caretPos,'\u2264');
					caretPos++;
					accent = null;
				}
				
			}
			else if ((kt != '@') &&
					(kt != '$') &&
					(kt != KeyEvent.VK_ESCAPE) &&
					(kt != KeyEvent.VK_DELETE) &&
					(kt != KeyEvent.VK_END) &&
					(kt != KeyEvent.VK_HOME) &&
	                (kt != KeyEvent.VK_BACK_SPACE) &&
               		(kc != KeyEvent.VK_ENTER)
                    && (kc != KeyEvent.VK_SHIFT)
                    && !e.isControlDown()
                   )
      		{	deleteSelection();
      			tekst.insert(caretPos,(char)kt);
				caretPos++;
				accent = null;
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
	    if(firstIndex>-1 && lastIndex>-1)
	    {	TekstVak.clipboard = tekst.getSelection(firstIndex,lastIndex);
	    }
	}
	
	public void focusGained(FocusEvent e)
    {   if (selectable)
		{	
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

}
