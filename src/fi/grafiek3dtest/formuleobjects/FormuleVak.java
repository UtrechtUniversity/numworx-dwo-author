package fi.grafiek3dtest.formuleobjects;

import java.awt.*;
import java.awt.event.*;
import java.util.Vector;
import javax.swing.*;
//import fi.wiskopdr.InteractiePanel;
import fi.grafiek3dtest.expressies.*;
import fi.grafiek3dtest.tekstobjects.*;
import fi.grafiek3dtest.Grafiek3DTest;

import fi.beans.wiskopdrbeans.InteractiePanel;

public class FormuleVak extends RegelVak implements MouseListener, ActionListener
{	
	protected static String clipboard;

	private static Font defaultFont = (!Grafiek3DTest.formTimes) || Grafiek3DTest.mac || Grafiek3DTest.zoefi ? Grafiek3DTest.formuleFont2Mac : Grafiek3DTest.formuleFont2; //new Font("TimesRoman",Font.PLAIN,14);
	private Font font = defaultFont;
	
	private FontMetrics fm;
	
	private FormuleRegel actieveRegel;
	
	private boolean selectable = true;
	private boolean editable = true;
	private Vector states;
	private int stateNr;
	
	public PopupMenu popup;
	private MenuItem miCut, miCopy, miPaste;
	
	private boolean hasBorder = true;
	
	private ActionListener actionListener;
	
	boolean outlined = false;
	
	public FormuleVak()
	{	setLayout(null);
		setBackground(Color.white);
        setOpaque(false);
		addMouseListener(this);
		super.setFont(defaultFont);
		fm = getFontMetrics(getFont());
			
		kind1 = new FormuleRegel(this);
		kind1.setLocation(0,0);
		//kind1.setBackground(getBackground());
		//kind1.setBorder(BorderFactory.createLineBorder(getBackground()));
		add(kind1);
		
		actieveRegel = kind1;
					
		setSize(kind1.getSize().width, kind1.getSize().height);
		ashoogte = kind1.ashoogte;
		
		formuleVak = this;
		
		states = new Vector();
		//states.addElement("$f@");
		stateNr=-1;
		
		popup = new PopupMenu();
		popup.setFont(new Font("SansSerif",Font.PLAIN,13));
		
		miCut = new MenuItem("cut");
		miCut.addActionListener(this);
		popup.add(miCut);
		
		miCopy = new MenuItem("copy");
		miCopy.addActionListener(this);
		popup.add(miCopy);
		
		miPaste = new MenuItem("paste");
		miPaste.addActionListener(this);
		popup.add(miPaste);
		
		add(popup);
	}
	
	public void setBackground(Color c)
	{	super.setBackground(c);
		Component[] components = getComponents();
		for(int i=0 ; i<components.length ; i++)
		{
			components[i].setBackground(c);
		}
		
	}
	
	public void setFont(Font f)
	{	super.setFont(f);
		font = f;
		fm = getFontMetrics(f);
		if(kind1==null)return;
		kind1.setFont(f);
		setSize(kind1.getSize().width, kind1.getSize().height);
		kind1.setLocation(0,0);
		ashoogte = kind1.ashoogte;
	}
	
	public static void setDefaultFont(Font f)
	{	defaultFont = f;
	}
	
	public Font getDefaultFont()
	{	return font;
	}
	
	public void zetStippels(boolean b)
	{	kind1.zetStippels(b);
	}
	
	public void vulVak(String s)
	{	super.vulVak(s.substring(2));
		
		kind1.setCaretPosition(kind1.getSize().width);
		kind1.requestFocus();
	}
	
	public void addState()
	{	for(int i=states.size()-1 ; i>stateNr; i--)
		{	states.removeElementAt(i);
		}
		states.addElement(toString());
		stateNr++;
	}
	
	public void undo()
	{	if(stateNr>0)
		{	vulVak((String)states.elementAt(stateNr-1));
			//states.removeElementAt(stateNr-1);
			stateNr--;
			
		}
	}
	
	public void redo()
	{	if(stateNr+1<states.size())
		{	vulVak((String)states.elementAt(stateNr+1));
			stateNr++;
		}
	}
	
	public void setOutlined(boolean b)
	{
		outlined = b;
	}
	
	public void paint(Graphics g)
	{	
		super.paint(g);
		
		if (outlined)
		{	g.setColor(Color.black);
			g.drawRect(0, 0, getSize().width - 1, getSize().height - 1);
		}	
		
	}
	
	public void zetMaat()
	{	setSize(kind1.getSize().width, kind1.getSize().height);
		kind1.setLocation(0,0);
		ashoogte = kind1.ashoogte;
		if(getParent()instanceof FormuleElement)((FormuleElement)getParent()).zetMaat();
		if(getParent()instanceof TekstElement)((TekstElement)getParent()).zetMaat();
		if(getParent()instanceof InteractiePanel)((InteractiePanel)getParent()).zetMaat();
        if(getParent()instanceof EditorContentPanel)((EditorContentPanel)getParent()).zetMaat();
        if(actionListener!=null)
		{	actionListener.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "zetMaat"));
		}
	}
	
	public void zetActieveRegel(FormuleRegel fr)
	{	actieveRegel.deSelect();
		actieveRegel = fr;
		if(actionListener!=null)
		{	actionListener.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "focus"));
		}
	}
	
	public void zetOpEind()
	{	kind1.zetOpEind();		
	}
	
	public void zetOpBegin()
	{	kind1.zetOpBegin();		
	}
	
	public FormuleRegel geefActieveRegel()
	{	return actieveRegel;
	}
	
	public void setBorder(boolean b)
	{	hasBorder = b;
	}
	
	public boolean hasBorder()
	{	return hasBorder;
	}
	
	public void insert(String s)
	{	actieveRegel.deleteSelection();
		for (int i=0 ; i<s.length() ; i++)
		{	actieveRegel.insert(new FormuleTeken(s.charAt(i)));
		}
		actieveRegel.zetMaat();   
        formuleVak.addState();
		actieveRegel.requestFocus();
	}
	
	public void delete()
	{	actieveRegel.delete();
		actieveRegel.requestFocus();
	}
	
	public void backspace()
	{	actieveRegel.backspace();
		actieveRegel.requestFocus();
	}
	
	public void focusLost(FormuleRegel fr)
	{	
		//if(actieveRegel==fr)
		//{	if(actionListener!=null)
		//	{	actionListener.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "focuslost"));
		//	}
		//}
		
	}
	
	public void deSelect()
	{	actieveRegel.deSelect();
	}
	
	public void setEditable(boolean b)
	{	editable = b;
		kind1.setEditable(b);
		if(b)
		{	selectable = b;
			kind1.setSelectable(b);
		}
	}
	
	public void setSelectable(boolean b)
	{	if(!editable)
		{	selectable = b;
			kind1.setSelectable(b);
		}
	}
	
	public boolean isEditable()
	{	return editable;
	}
	
	public boolean isSelectable()
	{	return selectable;
	}
	
	public void zetWortelVak()
	{	actieveRegel.zetWortelVak();
		actieveRegel.deSelect();
		formuleVak.addState();
	}
	
	public void zetNdeWortelVak()
	{	actieveRegel.zetNdeWortelVak();
		actieveRegel.deSelect();
		formuleVak.addState();
	}
	
	public void zetNdeLogVak()
	{	actieveRegel.zetNdeLogVak();
		actieveRegel.deSelect();
		formuleVak.addState();
	}
	
	public void zetDiffVak()
	{	actieveRegel.zetDiffVak();
		actieveRegel.deSelect();
		formuleVak.addState();
	}
	
	public void zetDiffPartialVak()
    {   actieveRegel.zetDiffPartialVak();
        actieveRegel.deSelect();
        formuleVak.addState();
    }
	
	//public void zetLimietVak()
	//{	actieveRegel.zetLimietVak();
	//	actieveRegel.deSelect();
	//	formuleVak.addState();
	//}
	
	public void zetLimietVak(int richting)
	{	actieveRegel.zetLimietVak(richting);
		actieveRegel.deSelect();
		formuleVak.addState();
	}
	
	public void zetIntegraalVak()
	{	actieveRegel.zetIntegraalVak();
		actieveRegel.deSelect();
		formuleVak.addState();
	}
	
	public void zetPrvVak()
	{	actieveRegel.zetPrvVak();
		actieveRegel.deSelect();
		formuleVak.addState();
	}
	
	public void zetAbsVak()
	{	actieveRegel.zetAbsVak();
		actieveRegel.deSelect();
		formuleVak.addState();
	}
	
	public void zetConjugVak()
    {   actieveRegel.zetConjugVak();
        actieveRegel.deSelect();
        formuleVak.addState();
    }
	
	public void zetPrimitieveVak()
	{	actieveRegel.zetPrimitieveVak();
		actieveRegel.deSelect();
		formuleVak.addState();
	}
	
	public void zetSigmaVak()
    {   actieveRegel.zetSigmaVak();
        actieveRegel.deSelect();
        formuleVak.addState();
    }
	
	public void zetHaakjesVak()
	{	actieveRegel.zetHaakjesVak();
		actieveRegel.deSelect();
		formuleVak.addState();
	}
	
	public void zetMachtVak()
	{	actieveRegel.zetMachtVak();
		actieveRegel.deSelect();
		formuleVak.addState();
	}
    
    public void zetSubscriptVak()
    {   actieveRegel.zetSubscriptVak();
        actieveRegel.deSelect();
        formuleVak.addState();
    }
    
	public void zetKwadraatVak()
	{	actieveRegel.zetKwadraatVak();
		actieveRegel.deSelect();
		formuleVak.addState();
	}
	
	public void zetBreukVak()
	{	actieveRegel.zetBreukVak();
		actieveRegel.deSelect();
		formuleVak.addState();
	}
	
	public void zetBinVak()
	{	actieveRegel.zetBinVak();
		actieveRegel.deSelect();
		formuleVak.addState();
	}
	
	public void requestFocus()
	{	actieveRegel.requestFocus();
		if(actionListener!=null)
		{	actionListener.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "focus"));
		}
	}
	
	public void showPopup(int x, int y)
	{	popup.show(actieveRegel,x,y);
	
	}
	
	public void finish()
	{	//vulVak("$f" + geefExpressie().toStringStrikt() + "@");
		if(actionListener!=null)
		{	actionListener.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "ingevuld"));
		}
	}
	
	public void changed()
	{	//vulVak("$f" + geefExpressie().toStringStrikt() + "@");
		if(actionListener!=null)
		{	actionListener.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "formChanged"));
		}
	}
	
	public void verwerkSelectie()
	{	if(actionListener!=null && selectable )
		{	if(actieveRegel.getComponentCount()>0 && ((FormuleElement)actieveRegel.getComponent(0)).isSelected())
			{	actionListener.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "$f" + actieveRegel.toString() + "=@"));
			}
			else
			{	actionListener.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, ""));
			}
		}
	}
	
	public Expressie geefExpressie()
	{	String s = toString();
		return FormuleParser.geefExpressie(s);
	}
	
	public VergelijkingMeerv geefVergelijking()
	{	String s = toString();
		return FormuleParser.parseVergelijking(s);
	}
	
	public boolean partEquationSelected(int nr)
	{	String s = toString();
		VergelijkingMeerv vergMeerv = FormuleParser.parseVergelijking(s);
		if(vergMeerv==null)return false;
		String ofLabel = Grafiek3DTest.rb.getString("ofLabel");
		int index = 0;
		int teller = 0;
		while (index !=-1)
		{	index = s.indexOf(ofLabel,index+1);
			teller++;
		}
		boolean[] selected = new boolean[teller+1];
		teller = 0;
		for(int i=0 ; i<kind1.getComponentCount()-1 ; i++)
		{	FormuleElement fc0 = ((FormuleElement)kind1.getComponent(i));
			FormuleElement fc1 = ((FormuleElement)kind1.getComponent(i+1));
			if(fc0 instanceof FormuleTeken && fc1 instanceof FormuleTeken
					&& ((FormuleTeken)fc0).geefChar()==ofLabel.charAt(0) && ((FormuleTeken)fc1).geefChar()==ofLabel.charAt(1))
				teller++;
			else if(fc0.isSelected())
			selected[teller] = true;
			
		}
		return selected[nr];
	}
	
	public void neemFocus(String richting,FormuleElement fe)
	{	if(getParent() instanceof TekstFormuleVak)
		{	((TekstFormuleVak)getParent()).neemFocus(richting,this);
		}
	}
	
	public void neemFocus(String richting)
	{	kind1.neemFocus(richting);
	}
	
	public String toString()
	{	//System.out.println(""+kind1.toString());
		return "$f" + kind1.toString() + "@";
	}
	
	public void addActionListener(ActionListener listener)
	{	actionListener = AWTEventMulticaster.add(actionListener, listener);
	}
	
	public void removeActionListener(ActionListener listener)
	{	actionListener = AWTEventMulticaster.remove(actionListener, listener);
	}
	
	public void mousePressed(MouseEvent e)
	{	kind1.zetOpEind();
	}
	
	public void mouseClicked(MouseEvent e){;}
	public void mouseReleased(MouseEvent e){;}
	public void mouseEntered(MouseEvent e){;}
	public void mouseExited(MouseEvent e){;}
	
	public void actionPerformed(ActionEvent e)
	{	if(e.getSource()==miCut && editable)
		{	geefActieveRegel().copySelection();
			geefActieveRegel().deleteSelection();
	    	addState();
		}
		else if(e.getSource()==miCopy)
		{	geefActieveRegel().copySelection();
			
		}
		else if(e.getSource()==miPaste && editable)
		{	geefActieveRegel().deleteSelection();
			geefActieveRegel().insert(formuleVak.clipboard);
	    	addState();
		}
	}
}

