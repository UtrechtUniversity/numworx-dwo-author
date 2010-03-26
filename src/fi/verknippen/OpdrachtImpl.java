/*
 * Created on Feb 22, 2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package fi.verknippen;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class OpdrachtImpl extends Component
						  implements OpdrachtIF, Cloneable
{
    public OpdrachtImpl()
    {
        enableEvents(MouseEvent.MOUSE_EVENT_MASK);
    }

    public Object clone()
    {
        try {
        return super.clone();
        } catch(CloneNotSupportedException e)
        {
            return new OpdrachtImpl();
        }
    }

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private ActionListener listener;
    private int state;
    private String number ="0";
    private boolean select;
    public Component getComponent()
    {
        return this;
    }

    public void addActionListener(ActionListener al)
    {
        listener = al;
    }

    /* (non-Javadoc)
     * @see java.awt.Component#paint(java.awt.Graphics)
     */
    public void paint(Graphics g)
    {
        Dimension size = getSize();
        g.setColor(getForeground());
        g.fillOval(0,0,size.width, size.height);
        int r = select ? 2: 1;
        switch(state)
        {
        case 0:
            g.setColor(getBackground());
            g.fillOval(r,r,size.width-2*r, size.height-2*r);
            break;
        case 1:
            g.setColor(Color.green);
            g.fillOval(r,r,size.width-2*r, size.height-2*r);
            break;
        case 2:
            g.setColor(Color.red);
            g.fillOval(r,r,size.width-2*r, size.height-2*r);
            g.setColor(Color.green);
            g.fillOval(r+2,r+2,size.width-4-2*r, size.height-4-2*r);
            break;
        case 3:
            g.setColor(Color.red);
            g.fillOval(r,r,size.width-2*r, size.height-2*r);
        }
        g.setColor(getForeground());
        //if(!select) g.drawOval(0,0,size.width-1, size.height-1);

        FontMetrics fm  = g.getFontMetrics();
        int h = fm.getAscent();
        int w = fm.stringWidth(number);
        g.drawString(number, (size.width-w)/2, (size.height+h)/2);

    }

    /**
     * @return Returns the state.
     */
    public int getState()
    {
        return state;
    }

    /**
     * @param state The state to set.
     */
    public void setState(int state)
    {
        this.state = state;
        repaint();
    }

    public void mouseClicked(MouseEvent e)
    {
        if(listener != null)
            listener.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, number, e.getModifiers()));

    }


    /**
     * @return Returns the number.
     */
    public String getText()
    {	return number;
    }

    /**
     * @param number The number to set.
     */
    public void setText(String number)
    {   this.number = number;
        repaint();
    }

    /* (non-Javadoc)
     * @see java.awt.Component#getPreferredSize()
     */
    public Dimension getPreferredSize()
    {
        Graphics g = getGraphics();
        FontMetrics fm = g.getFontMetrics();
        int h = fm.getHeight();
        //int w = fm.stringWidth(number);
        // TODO Auto-generated method stub
        return new Dimension(h+6, h+6);
    }

    public Dimension getMinimumSize()
    {
        return getPreferredSize();
    }

    /**
     * @return Returns the select.
     */
    public boolean isSelected()
    {
        return select;
    }

    /**
     * @param select The select to set.
     */
    public void setSelected(boolean select)
    {
        this.select = select;
        repaint();
    }

    /* (non-Javadoc)
     * @see java.awt.Component#processMouseEvent(java.awt.event.MouseEvent)
     */
    protected void processMouseEvent(MouseEvent e)
    {
        // TODO Auto-generated method stub
        super.processMouseEvent(e);
        if(e.getID() == MouseEvent.MOUSE_CLICKED)
            mouseClicked(e);
    }



}
