package fi.heks;

import java.awt.*;
import java.awt.event.*;

public class ImageButton extends Panel implements MouseListener
{
    private Font defaultfont = new Font("SansSerif", Font.BOLD, 12);
    private Color bgColor = new Color(180, 180, 180);       
    private Color darkColor = Color.black;
    private Color lightColor = Color.lightGray;
    
    private Polygon p0,p1,p2,p3;
    private Color bgBright, bgDark;
    
    protected ActionListener actionListener = null;
    protected String opschrift = "";
    private Image image;
    protected Font labelfont;
                    
    public double schaal;
    public double relx,rely,relb,relh;
    public boolean resized;
    

    public ImageButton(Image image)
    {   
        setLayout(null);
        addMouseListener(this);
        
        bgBright = bgColor.brighter();
        bgDark = bgColor.darker();
        
        this.image = image;
        
    }
    
    public void addActionListener(ActionListener l) 
    {   actionListener = AWTEventMulticaster.add(actionListener,l);
    }
    
    public void removeActionListener(ActionListener l)
    {   actionListener = AWTEventMulticaster.remove(actionListener, l);
    }
  
    public String getLabel()
    {   return opschrift;
    }
    
    
    public void setEnabled(boolean b)
    {   super.setEnabled(b);
        if ( isVisible() )
        {   repaint();
        }
    }
    
    public void setBackground(Color c)
    {   bgColor = c;
        bgBright = bgColor.brighter();
        bgDark = bgColor.darker();
    }
    
    public void paint(Graphics g)
    {   
        g.drawImage(image,0,0,null);
    }
    
    public void setResized(boolean b)
    {   resized = b;
    }
    
    
    
    public void mousePressed(MouseEvent e)
    {   bgBright = bgColor.darker();
        bgDark = bgColor.brighter();
        repaint();
    }
    
    public void mouseReleased(MouseEvent e) 
    {   if ( isEnabled() )
        {   if (actionListener != null)
            {   actionListener.actionPerformed( new ActionEvent(this, 0, opschrift) );
            }
        }
        bgBright = bgColor.brighter();
        bgDark = bgColor.darker();
        repaint();
    }  
    
    public void mouseEntered(MouseEvent e)
    {   darkColor = Color.yellow;
        repaint();
        
    }
    public void mouseExited(MouseEvent e)
    {   darkColor = Color.black;
        repaint();
    }
    public void mouseClicked(MouseEvent e){;}
 } // class ImageButton


