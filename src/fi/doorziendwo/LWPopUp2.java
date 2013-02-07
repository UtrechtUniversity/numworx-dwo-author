package fi.doorziendwo;

import java.awt.*;
import java.awt.event.*;

public class LWPopUp2 extends Container
{   DrawingPanel2 parent;
    String titel;
    String[] items;
    LWItem[] menuItems;
    Font fo = new Font("Helvetica", Font.PLAIN, 11);    
    FontMetrics fm = getFontMetrics(fo);
    int inset = 3;
    public LWPopUp2(DrawingPanel2 p, String t, String[] its)
    {   parent = p;
        titel = t;
        items = its;
        menuItems = new LWItem[items.length];
        setFont(fo);
        setBackground(Color.lightGray);
        int width = fm.stringWidth(titel) + 3 * inset;
        for (int i = 0; i < items.length; i++)
            width = Math.max(width,
                        fm.stringWidth(items[i] + "  ") + 3 * inset);
        int currentY = inset;
        
        LWItem lwItem;
        
//        lwItem.setBounds(inset, currentY, width - 2 * inset,
//                         4 * fm.getHeight() / 3);
//        add(lwItem);
//        currentY += 2 * fm.getHeight() / 3;
        
        for (int j = 0; j < items.length; j++)
        {   // look for separator
            if (items[j].charAt(0) == '-')
            {   items[j] = items[j].substring(1);
                lwItem = new LWItem("");
                lwItem.setBounds(inset, currentY, width - 2 * inset,
                                 2 * fm.getHeight() / 3);
                add(lwItem);
                currentY += lwItem.getSize().height;
            }
            lwItem = new LWItem(items[j]);
            menuItems[j] = lwItem;
            lwItem.setBounds(inset, currentY, width - 2 * inset,
                                 4 * fm.getHeight() / 3);
            add(lwItem);
            lwItem.addMouseListener(new ML());
            currentY += lwItem.getSize().height;
        }
        setSize(width, currentY + inset);
        
    }

    public void paint(Graphics g)
    {   g.setColor(getBackground());
        g.fillRect(0, 0, getSize().width, getSize().height);
//        g.setColor(Color.white);
        
        g.setColor(Color.black);
//        g.drawLine(0, 0, getSize().width - 1, 0);
        g.drawLine(0, 0, 0, getSize(). height - 1);        
//        g.setColor(Color.black);
        g.drawLine(getSize().width - 1, 0, 
                   getSize().width - 1, getSize(). height - 1);
        g.drawLine(0, getSize(). height - 1, 
                   getSize().width - 1, getSize(). height - 1);        
        
        super.paint(g);
    }
    public int findIndex(String s)
    {   int index = 0;
        for (int i = 0; i < items.length; i++)
            if (items[i].equals(s))
                index = i;
        return index;
    }    
    
    public void setCheckable(String s, boolean c)
    {   int index = findIndex(s);
        menuItems[index].checkable = c;
    }    
    
    public void setCheckable(boolean b)
    {   for (int i = 0; i < menuItems.length; i++)
            menuItems[i].checkable = b;
        
    }    
    
    public void setChecked(String s, boolean c)
    {   int index = findIndex(s);
        if (menuItems[index].checkable)
            menuItems[index].checked = c;
    }    
    
    public void switchTo(String s)
    {   int index = findIndex(s);
        for (int i = 0; i < menuItems.length; i++)
        {   if (i == index)
                menuItems[i].checked = true;
            else
                menuItems[i].checked = false;            
        }    
    
    }
    public void setEnabled(int index, boolean b)
    {   menuItems[index].enabled = b;
    }
    class ML extends MouseAdapter
    {   public void mousePressed(MouseEvent e)
        {   LWItem lwi = (LWItem) e.getComponent();
            if (lwi.enabled)
            {   lwi.selected = false;            
                int i = findIndex(lwi.text);
                switchTo(lwi.text);
//parent.TICKSVISIBLE = true;
                parent.setHelpPoints(i + 1);
                parent.panel3D.remove(LWPopUp2.this);
                parent.panel3D.repaint();
            }
        }
        public void mouseEntered(MouseEvent e)
        {   LWItem lwi = (LWItem) e.getComponent();
            if (lwi.enabled)
            {   lwi.selected = true;
                repaint();
            }
        }
        public void mouseExited(MouseEvent e)
        {   LWItem lwi = (LWItem) e.getComponent();
            if (lwi.enabled)
            {   lwi.selected = false;
                repaint();
            }
        }
    }    

}
