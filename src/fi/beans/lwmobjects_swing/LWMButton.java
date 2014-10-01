package fi.beans.lwmobjects_swing;

import java.awt.*;
import java.awt.event.*;
// import fi.balansfruit.tekst.*;

/**
 * LWMButton is a 'lichtgewicht movable object' that will act as a Button.
 * <br>
 * It is a LWMComponent because it must be allowed to be added to a LWMContainer.
 * <br>
 * A LWMButton may have a background image, background Color or be transparant.
 * In all cases a label can be added on top (just do it when the LWMButton is transparant!)
 * By default a LWMButton is not movable, but it is not forbidden to make it movable.
 * <br>
 * You can also specify/change: border yes/no + arc size RoundRect, label, font, 
 * disabled and enabled text&border color.
 * <br>
 * The specified width and height are minimum sizes, when the LWMButton doesn't have
 * an image. An image button will have the image size, make sure the label fits. 
 * The size of text buttons without image is Math.max(specified size, size needed for the
 * label). This means that the button will be at least as big as your specification, but it
 * may be bigger if the label needs more space. Enter small numbers in the constructor
 * if you want to make the LWMButton use as much space as it needs.
 * 
 * @author Paul Bergervoet
 *
 * @version 1, 13 september 2000
 */

public class LWMButton extends LWMObject
	// implements MouseListener 
	// (no need to state expicitly, since LWMComponent implements Mouse(Motion)Listener)
{	// constanten
	public static Font defaultfont = new Font("SansSerif", Font.BOLD, 14);
	public static Color defaultColor = Color.black;
	
	// variables
/**
 *
 */
	protected ActionListener actionListener = null;
	private boolean hasLabel;
	private String label;
	private Font labelFont = defaultfont;
	private Color labelColor = defaultColor;
	protected int specWidth;				// specified width
	protected int specHeight;
	protected int labelx;					// horizontal position of label
	protected int labely;					// vertical position of label
	
/**
 * Constructs a LWMButton with the specified minimum dimension, label and background image.
 *
 * @param i The background Image
 * @param t The label (or empty String if you don't want a label)
 * @param w Width (int)
 * @param h Height (int)
 */
	public LWMButton(Image i, String t, int w, int h)
	{	super(i, w, h);
		setvars(t, w, h);
	}

/**
 * Constructs a LWMButton with the specified minimum dimension, label and background color.
 *
 * @param c The background Color
 * @param t The label (or empty String if you don't want a label)
 * @param w Width (int)
 * @param h Height (int)
 */
	public LWMButton(Color c, String t, int w, int h)
	{	super(c, w, h);
		setvars(t, w, h);
	}

/**
 * Constructs a transparant LWMButton with the specified minimum dimension and label.
 *
 * @param t The label (don't give an empty String, the button won't show at all!)
 * @param w Width (int)
 * @param h Height (int)
 */
	public LWMButton(String t, int w, int h)
	{	super(w, h);					// is er niet!
		setvars(t, w, h);
	}
	
/**
 * Set all variables: hasLabel, label, specDimensions, size, label position, isMovable
 * Common part of all constructors.
 */
	private void setvars(String t, int w, int h)
	{	if ( t.equals("") )				// no label?
		{	hasLabel = false;
		} else
		{	hasLabel = true;
			label = t;
		}
		specWidth = w;
		specHeight = h;
		isMovable = false;				// by default not movable
		// super.setFont(labelFont);		// hmmm????.... not needed since we implement paint ourselves....
		// addMouseListener(this);			// don't do this, already done in constructor LWMObject (super...)
		determineSize();
	}

/**
 * Determine size of non-image buttons, determine label position.
 */
	private void determineSize()
	{	if ( !hasLabel )
		{	return;
		}
		Toolkit tk = Toolkit.getDefaultToolkit();
		FontMetrics fm = tk.getFontMetrics(labelFont);
		if (!hasImage )							// image button has fixed size
		{	width = Math.max(specWidth, fm.stringWidth(label) );
			height = Math.max(specHeight, fm.getHeight() );
		}
		labelx = (width - fm.stringWidth(label) )/2;
		labely = (height +fm.getHeight())/2 - fm.getDescent();
		setSize(width, height);
	}

	public String getLabel()
	{	return label;
	}
	
	public void setLabel(String l)
	{	label = l;
		determineSize();
	}
	
	public Font getFont()
	{	return labelFont;
	}
	
	public void setFont(Font f)
	{	labelFont = f;
		determineSize();
	}
	
	public Color getLabelColor()
	{	return labelColor;
	}
	
	public void setLabelColor(Color c)
	{	labelColor = c;
	}
	
	public void addActionListener(ActionListener l) 
 	{	actionListener = AWTEventMulticaster.add(actionListener,l);
 	}
 	
 	public void removeActionListener(ActionListener l)
 	{	actionListener = AWTEventMulticaster.remove(actionListener, l);
 	}
  
	public void paint(Graphics g)
	{	super.paint(g);				// image/background color, borders
		if ( hasLabel )
		{	g.setFont(labelFont);
			g.setColor(labelColor);
			g.drawString(label, labelx, labely);
		}
	}
	
	// Event handlers: MouseListener
	// don't change pressed, etc.
	
	public void mouseReleased(MouseEvent e)
	{	// System.out.println("Klik: "+getName() );
		if ( isEnabled() )
 		{	if (actionListener != null)
 			{	actionListener.actionPerformed( new ActionEvent(this, 0, getActionCommand()) );
 			}
 		}
 	}
    
    // additional field: actionCommand
    
    private String actionCommand;

    /**
     * Sets the command name for the action event fired
     * by this button. By default this action command is
     * set to match the label of the button.
     * @param     command  A string used to set the button's
     *                  action command.
     *            If the string is <code>null</code> then the action command
     *            is set to match the label of the button.
     * @see       java.awt.event.ActionEvent
     */
    public void setActionCommand(String command) {
        actionCommand = command;
    }

    /**
     * Returns the command name of the action event fired by this button.
     * If the command name is <code>null</code> (default) then this method
     * returns the label of the button.
     */
    public String getActionCommand() {
        return (actionCommand == null? label : actionCommand);
    }
    
    
}
