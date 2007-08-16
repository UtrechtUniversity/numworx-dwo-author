package logotekenap;

import java.awt.*;
import java.awt.event.*;
import java.text.DecimalFormat;

/**
 * Class NumOnlyField assist the main components of the grnuminput package.
 * It provides a TextField with number checking features. It tells its users of changes
 * using the NumberListener interface.
 */


class NumOnlyField extends TextField 
	  implements KeyListener, FocusListener
{	private int waarde;
	private int initw;				// initial value
	private int minw;	
	private int maxw;
	private DecimalFormat df;

	private boolean enabled = true;
	private static Color enabledValueColor = Color.black;
	private static Color disabledValueColor = Color.gray;
	
	InvoerVar owner;
		
	public NumOnlyField(int mn, int mx, int val, InvoerVar o)
	{	super();
		addKeyListener(this);
		addFocusListener(this);
		
		setBackground(Color.white);
		setForeground(enabledValueColor);
		
		waarde = val;
		initw = val;
		minw = mn;
		maxw = mx;
		owner = o;
		df = new DecimalFormat("0");
		setColumns(3);
		setValue(val);
	}

	// Public methods: enable, set-, getValue....
	
	public void setEnabled(boolean b)
	{	enabled = b;
		setEditable(b);
		if ( b )
		{	setForeground(enabledValueColor);
		} else
		{	setForeground(disabledValueColor);
		}
		repaint();
	}

	public void setValue(int newval)
	{	if ( newval > maxw )
		{ 	waarde = maxw;
		} else if (newval < minw )
		{ 	waarde = minw;
		} else
		{ 	waarde = newval;
		}
		setText( String.valueOf(waarde) );
	}

	public int getValue()
	{	return waarde;
	}
	
	private void processTextChange(String s)
	{	try						// no need to test 'enabled', user can't type then
		{	Number f = df.parse( s.trim() );
			int w = f.intValue();
			if ( w < minw )
			{   throw new NumberFormatException("getal te klein");
			}
			if ( w > maxw )
			{   throw new NumberFormatException("getal te groot");
			}
			waarde = w;
			setText( String.valueOf(waarde) );
			owner.numberTyped(waarde);
		}
		catch (Exception exc)
		{	setText( String.valueOf(waarde) );
			// just reset previous input
		}
	}

	// Listeners, KeyListener
	
	public void keyReleased(KeyEvent e)
	{	if ( e.getKeyCode() == KeyEvent.VK_ENTER )		// Is it 'virtual key' Enter?
		{	processTextChange( getText() );
		}
	}
	
	public void keyTyped(KeyEvent e)
	{ 	// don't react
	}
	
	public void keyPressed(KeyEvent e)
	{ 	// don't react
	}
			
	// Listeners, FocusListener 
	public void focusLost(FocusEvent e)
	{	processTextChange( getText() );
	}
	
	public void focusGained(FocusEvent e)
	{ 	// don't react
	}

}
	
