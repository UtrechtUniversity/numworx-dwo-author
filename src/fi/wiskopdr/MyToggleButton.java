package fi.wiskopdr;

import java.awt.*;
import java.awt.event.*;


import javax.swing.*;
import javax.swing.event.*;

public class MyToggleButton extends JToggleButton implements MouseListener
{
    private Icon icon;
    
    public MyToggleButton(Icon icon)
    {   //super(icon);
        this.icon = icon;
        setSize(30,30);
        setBackground(Color.lightGray);
        setBorder(null);
        
        addMouseListener(this);
    }
    
    
    public void paintComponent(Graphics g)
    {
            Color bgColor = Color.lightGray;//getBackground();
            g.setColor(bgColor);
            g.fillRect(0,0,getSize().width,getSize().height);
            if(getModel().isRollover())
            {                
                g.setColor(bgColor);
                g.fillRect(0,0,getSize().width,getSize().height);
                g.setColor(bgColor.brighter());
                g.drawLine(0,0,getSize().width-1,0);
                g.drawLine(0,0,0,getSize().height-1);
                g.setColor(bgColor.darker());
                g.drawLine(getSize().width-1,0,getSize().width-1,getSize().height-1);
                g.drawLine(0,getSize().height-1,getSize().width-1,getSize().height-1);
            }
            if(getModel().isPressed() || getModel().isSelected())
            {   
                g.setColor(bgColor.brighter());
                g.fillRect(0,0,getSize().width,getSize().height);
                g.setColor(bgColor.brighter());
                g.drawLine(0,0,getSize().width-1,0);
                g.drawLine(0,0,0,getSize().height-1);
                g.setColor(bgColor.brighter());
                g.drawLine(getSize().width-1,0,getSize().width-1,getSize().height-1);
                g.drawLine(0,getSize().height-1,getSize().width-1,getSize().height-1);
            }
            //icon.paintIcon(this,g,1,1);
        
    }
    
    public void stateChanged(ChangeEvent e)
    {
        repaint();
    }
    
    public void mousePressed(MouseEvent e)
    {   //super.mousePressed(e);
            repaint();
    }
    
    public void mouseReleased(MouseEvent e) 
    {   //super.mouseReleased(e);
    repaint();
    }  
    
    public void mouseEntered(MouseEvent e)
    {   //super.mouseEntered(e);
    repaint();
    }
    public void mouseExited(MouseEvent e)
    {   //super.mouseExited(e);
    repaint();
    }
    public void mouseClicked(MouseEvent e){//super.mouseClicked(e);
    repaint();}
    

}
