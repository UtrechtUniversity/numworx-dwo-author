package fi.spot_problems_dwo.wiskopdr.formuleobjects;

import java.awt.*;
import java.awt.event.*;
import java.util.Vector;
import fi.spot_problems_dwo.wiskopdr.expressies.*;
import fi.spot_problems_dwo.wiskopdr.tekstobjects.*;

public class FormuleVak extends RegelVak implements MouseListener
{	
	protected static String clipboard;

	private Font defaultFont = new Font("TimesRoman",Font.PLAIN,16);
	private FontMetrics fm;
	
	private FormuleRegel actieveRegel;
	
	private boolean selectable = true;
	private boolean editable = true;
	private Vector states;
	private int stateNr;
	
	private ActionListener actionListener;
	
	public FormuleVak()
	{	setLayout(null);
		setBackground(getBackground());
		addMouseListener(this);
		super.setFont(defaultFont);
		fm = getFontMetrics(getFont());
			
		kind1 = new FormuleRegel(this);
		kind1.setLocation(5,0);
		add(kind1);
		
		actieveRegel = kind1;
					
		setSize(kind1.getSize().width+10, kind1.getSize().height);
		ashoogte = kind1.ashoogte;
		
		formuleVak = this;
		
		states = new Vector();
		//states.addElement("$f@");
		stateNr=-1;
	}
	
	public void setFont(Font f)
	{	super.setFont(f);
		fm = getFontMetrics(getFont());
		kind1.setFont(f);
		setSize(kind1.getSize().width+10, kind1.getSize().height);
		kind1.setLocation(5,0);
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
		if(getParent()instanceof TekstElement)((TekstElement)getParent()).zetMaat();
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
	
	public String toString()
	{	return "$f" + kind1.toString() + "@";
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
}

