/*
 * Created on Feb 22, 2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package fi.verknippen;

import java.awt.AWTEventMulticaster;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.ItemSelectable;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;

import javax.swing.JPanel;

/**
 * @author wim
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class OpdrachtSelector extends Container
							  implements ActionListener, ItemSelectable
{
    int nrOpdracht, selected;
    OpdrachtIF[] opdracht;
    ItemListener listener;

    public static final int INITIAL = 0;
    public static final int GREEN   = 1;
    public static final int ORANGE  = 2;
    public static final int RED = 3;

    private static OpdrachtIF prototype = new OpdrachtImpl();

    public static void setPrototype(OpdrachtIF p)
    {	prototype = p;
    }

    private OpdrachtIF newPrototype()
    {   return (OpdrachtIF) prototype.clone();
    }

    public void addItemListener(ItemListener il)
    {	listener = AWTEventMulticaster.add(listener, il);
    }

    private static final long serialVersionUID = 1L;

    /**
     * @return Returns the nrOpdracht.
     */
    public int getNrOpdracht()
    {	return nrOpdracht;
    }

    /**
     * @param nrOpdracht The nrOpdracht to set.
     */
    public void setNrOpdracht(int nrOpdracht)
    {   this.nrOpdracht = nrOpdracht;
        if(opdracht == null)
        {   setLayout(new FlowLayout());
            opdracht = new OpdrachtIF[nrOpdracht];
            for (int i = 0; i < opdracht.length; i++)
            {   opdracht[i] = newPrototype();
                opdracht[i].addActionListener(this);
                opdracht[i].setText(String.valueOf(i + 1));
                add(opdracht[i].getComponent());
                opdracht[i].setSelected(i == selected);
            }
        }
    }

    /**
     * @param opdracht
     */
    public OpdrachtSelector(int opdracht)
    {    setNrOpdracht(opdracht);
    }

    public void actionPerformed(ActionEvent e)
    {
        int i = Integer.parseInt(e.getActionCommand());
        OpdrachtIF opdracht = this.opdracht[i-1];
        if(opdracht.isEnabled() && !opdracht.isSelected())
        {
            this.opdracht[selected].setSelected(false);
            opdracht.setSelected(true);
            selected  = i-1;
            if(listener!=null)
            {
                ItemEvent ie = new ItemEvent(this, ItemEvent.ITEM_STATE_CHANGED, new Integer(selected), ItemEvent.SELECTED);
                listener.itemStateChanged(ie);
            }
        }
    }

    public void removeItemListener(ItemListener l)
    {	listener = AWTEventMulticaster.remove(listener, l);
    }

    public Object[] getSelectedObjects()
    {	return new Object[] { new Integer(selected) };
    }

    public void select(int i)
    {
        if(selected != i)
        {
            opdracht[selected].setSelected(false);
            opdracht[selected = i].setSelected(true);
        }
    }

    public void setState(int i, int state)
    {	opdracht[i].setState(state);
    }
    public int getState(int i)
    {
        return opdracht[i].getState();
    }

    public void setEnabled(int i, boolean enable)
    {
        opdracht[i].setEnabled(enable);
    }

    public boolean isEnabled(int i)
    {
        return opdracht[i].isEnabled();
    }
    
/*    
    public void paint(Graphics g)
    {	g.setColor(Color.black);
    	g.drawRect(0, 0, getSize().width - 1, getSize().height - 1);
    }
*/    
}
