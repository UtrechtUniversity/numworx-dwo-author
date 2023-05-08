package fi.javalogoweb.formuleobjects;

import java.awt.*;
import java.awt.event.*;
import java.util.Vector;
import fi.javalogoweb.expressies.*;
//import fi.wiskopdr.tekstobjects.*;

public class FormuleVak extends RegelVak implements MouseListener, ActionListener
{	
	protected static String clipboard;

	private Font defaultFont = new Font("TimesRoman",Font.PLAIN,16);
	private FontMetrics fm;
	
	private FormuleRegel actieveRegel;
	
	private boolean selectable = true;
	private boolean editable = true;
	private Vector states;
	private int stateNr;
	
	public PopupMenu popup;
	private MenuItem miCut, miCopy, miPaste;
	
	private ActionListener actionListener;
	
	public FormuleVak()
	{	setLayout(null);
		setBackground(getBackground());
		addMouseListener(this);
		super.setFont(defaultFont);
		fm = getFontMetrics(getFont());
			
		kind1 = new FormuleRegel(this);
		kind1.setLocation(0,0);
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
	
	public void setFont(Font f)
	{	super.setFont(f);
		defaultFont = f;
		fm = getFontMetrics(f);
		kind1.setFont(f);
		setSize(kind1.getSize().width, kind1.getSize().height);
		kind1.setLocation(0,0);
		ashoogte = kind1.ashoogte;
	}
	
	public Font getDefaultFont()
	{	return defaultFont;
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
	
	public void paint(Graphics g)
	{	super.paint(g);
	}
	
	public void zetMaat()
	{	setSize(kind1.getSize().width, kind1.getSize().height);
		kind1.setLocation(0,0);
		ashoogte = kind1.ashoogte;
		if(getParent()instanceof FormuleElement)((FormuleElement)getParent()).zetMaat();
		//if(getParent()instanceof TekstElement)((TekstElement)getParent()).zetMaat();
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
	
	public FormuleRegel geefActieveRegel()
	{	return actieveRegel;
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
	
	public void requestFocus()
	{	actieveRegel.requestFocus();
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
	
	public void verwerkSelectie()
	{	if(actionListener!=null && selectable )
		{	if(actieveRegel.getComponentCount()>0 && ((FormuleElement)actieveRegel.getComponent(0)).isSelected())
			{	actionListener.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "$f" + actieveRegel.toString() + "@"));
			}
			else
			{	actionListener.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, ""));
			}
		}
	}
	
	public Expressie geefExpressie()
	{	FormuleParser p = new FormuleParser();
		
		String s = toString();
		return p.parse(p.schoon(p.formuleString(s)));
	}
	
	public VergelijkingMeerv geefVergelijking()
	{	String s = toString();
		return FormuleParser.parseVergelijking(s);
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

