package fi.stroomdiagrammen;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

class LWTextField extends LWContainer
{   // actual and visible text
    String text = "", textVisible = "", oldText = "";
    boolean textValueChanged = false;
    // font and its metrics
    Font fo = null;
    FontMetrics fm;
    // colors
    Color bgColor = Color.white,
          fgColor = Color.black,
          ctColor = Color.black,
          olColor = Color.black;
    // flagg for caret
    boolean caretVisible = false;
    // caret position in pixels
    int caretX = 0;
    // caret position in characters
    int caretPos = 0;
    // left inset, baseline y
    int bx = 4, by;
    // combined listener inner class
    InL listener;
    // flagg for editing
    boolean editable = true;
    // appearance
    boolean rounded = false;
    // default constructor
    public LWTextField()
    {   listener = new InL();
        addMouseListener(listener);
        addKeyListener(listener);
        addFocusListener(listener);
    }
    // alternative
    public LWTextField(String s)
    {   text = s;
        textVisible = s;
        listener = new InL();
        addMouseListener(listener);
        addKeyListener(listener);
        addFocusListener(listener);
    }
    public boolean contains(int x, int y)
    {   return (x > 2) && (y > 2) && (x < getSize().width - 2) &&
               (y < getSize().height - 2);
    }
    // paint
    public void paint(Graphics g)
    {   // background
        g.setColor(bgColor);
        if (rounded)
            g.fillRoundRect(0, 0, getSize().width, getSize().height, DrawingPanel.roundWidth, DrawingPanel.roundHeight);        
        else
            g.fillRect(0, 0, getSize().width, getSize().height);
        // outline
        g.setColor(olColor);
        if (rounded)
            g.drawRoundRect(0, 0, getSize().width - 1, getSize().height - 1, DrawingPanel.roundWidth, DrawingPanel.roundHeight);
        else    
            g.drawRect(0, 0, getSize().width - 1, getSize().height - 1);
        // find font and metrics
        if (fo == null)
            fo = getFont();
        g.setFont(fo);
        fm = g.getFontMetrics(fo);
        // vertical centering
        int vSpace = (getSize().height - fm.getHeight()) / 2;
        if (vSpace < 0)
            by = fm.getHeight() - fm.getDescent();
        else
            by = vSpace + fm.getHeight() - fm.getDescent();
        // draw text
        g.setColor(fgColor);
        g.drawString(textVisible, bx, by);
        // draw caret
        g.setColor(ctColor);
        if (caretVisible)
        {    g.drawLine(caretX, vSpace, caretX, vSpace + fm.getHeight());
        }
    }
    public void setText(String t)
    {   text = t;
        textVisible = t;
        caretPos = 0;
        caretX = bx;
        repaint();
    }
    public String getText()
    {   return text;
    }
    public void setFont(Font f)
    {   fo = f;
        repaint();
    }
    public void setBackground(Color c)
    {   bgColor = c;
        repaint();
    }
    public void setForeground(Color c)
    {   fgColor = c;
        repaint();
    }
    public void setCaretColor(Color c)
    {   ctColor = c;
        repaint();
    }
    // finding caret position at mouse press
    public void setCaretPosition(int x)
    {   int index = 0;
        int xPos = bx;
        int delta = 0;
        boolean quit = false;
        // start at left inset and find last character position
        // xPos with xPos < x (mouse press)
        while ((!quit) && (index < textVisible.length()))
        {   delta = fm.charWidth(textVisible.charAt(index));
            if (xPos + delta < x)
            {   xPos += delta;
                index++;
            }
            else
                quit = true;
        }
        // if there is a next character put caret after this
        // when x is closer to "after"
        if (index < textVisible.length())
        {   delta = fm.charWidth(textVisible.charAt(index));
            if ((xPos + delta - x) <= (x - xPos))
            {   xPos += delta;
                index++;
            }
        }
        caretX = xPos;
        caretPos = index + text.length() - textVisible.length();
    }
    // wrapping
    public void setTextVisible()
    {   while (caretX > getSize().width - bx)
        {   caretX -= fm.charWidth(textVisible.charAt(0));
            textVisible = textVisible.substring(1);
        }
        while ((caretX < bx) && (textVisible.length() < text.length()))
        {   int temp = text.length() - textVisible.length() - 1;
            caretX += fm.charWidth(text.charAt(temp));
            textVisible = text.substring(temp);
        }

    }
    // setter for editable    
    public void setEditable(boolean b)
    {   editable = b;
    }    
    public boolean isInside(int x, int y)
    {   return contains(x, y);
    }    
    // combined listener inner class
    class InL extends MouseAdapter
              implements KeyListener, FocusListener
    {   int kc;
        public void mousePressed(MouseEvent e)
        {   // left button only
            if ((e.getModifiers() & e.BUTTON1_MASK) != 0)
            {   if (editable)
                {   requestFocus();
                    setCaretPosition(e.getX());
                    caretVisible = true;
                    repaint();
                }
            }    
        }
        public void mouseEntered(MouseEvent e)
        {   if (editable)
                setCursor(new Cursor(Cursor.TEXT_CURSOR));
        }
        public void mouseExited(MouseEvent e)
        {   if (editable)
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }
        public void keyPressed(KeyEvent e)
        {   if (editable)
            {   kc = e.getKeyCode();
            	// left arrow
                if (kc == KeyEvent.VK_LEFT)
                {   if (caretPos > 0)
                    {   caretPos--;
                        caretX -= fm.charWidth(text.charAt(caretPos));
                        setTextVisible();
                    }
                } // left arrow
                // right arrow
                else if (kc == KeyEvent.VK_RIGHT)
                {   if (caretPos < text.length())
                    {   caretPos++;
                        caretX += fm.charWidth(text.charAt(caretPos - 1));
                        setTextVisible();
                    }
                } // right arrow
                // home
                else if (kc == KeyEvent.VK_HOME)
                {   caretPos = 0;
                    caretX = bx;
                    textVisible = text;
                } // home
                // end
                else if (kc == KeyEvent.VK_END)
                {   caretPos = text.length();
                    caretX = bx + fm.stringWidth(text);
                    setTextVisible();
                } // end
                // delete
                else if (kc == KeyEvent.VK_DELETE)
                {   
//System.out.println("pressed " + kc);                
                	String temp1 = "";
                    String temp2 = "";
                    if (caretPos > 0)
                        temp1 = text.substring(0, caretPos);
                    if (caretPos < text.length() - 1)
                        temp2 = text.substring(caretPos + 1);
                    text = temp1 + temp2;
                    textVisible = text;
                    caretX = bx + fm.stringWidth(temp1);
                    setTextVisible();
                } // delete
            repaint();
            }
        }
        public void keyReleased(KeyEvent e) {}
        
// let op:
// DELETE geeft met Microsoft JVM alleen een keyPressed Event,
// DELETE geeft met SUN pluggin een keyPressed EN een KeyTyped
// Event, dit laatste laat een blokje verschijnen!!        
        public void keyTyped(KeyEvent e)
        {   if (editable)
            {   // kc initialized by keyPressed
                int kt = e.getKeyChar();
                // all typed keys except Esc, Backspace, Enter, Shift
                if ((kt != KeyEvent.VK_ESCAPE) &&
                    (kt != KeyEvent.VK_BACK_SPACE) &&
                    (kc != KeyEvent.VK_ENTER) &&
                    (kc != KeyEvent.VK_SHIFT)
                    && (kc != KeyEvent.VK_DELETE)
                   )
                {   
//System.out.println("typed " + kt);                
                	String temp1 = "";
                    String temp2 = "";
                    if (caretPos > 0)
                        temp1 = text.substring(0, caretPos);
                    if (caretPos < text.length())
                        temp2 = text.substring(caretPos);
                    text = temp1 + ((char) kt) + temp2;
                    textVisible = text;
                    caretPos++;
                    caretX = bx + fm.stringWidth(temp1 + ((char) kt));
                    setTextVisible();
                } // all typed keys except Esc, Backspace, Enter
                // backspace
                else if (kt == KeyEvent.VK_BACK_SPACE)
                {   String temp1 = "";
                    String temp2 = "";
                    char tchar = ' ';
                    if (caretPos > 0)
                    {   tchar = text.charAt(caretPos - 1);
                        if (caretPos > 1)
                            temp1 = text.substring(0, caretPos - 1);
                        if (caretPos < text.length())
                            temp2 = text.substring(caretPos);
                        text = temp1 + temp2;
                        textVisible = text;
                        caretPos--;
                        caretX = bx + fm.stringWidth(temp1);
                        setTextVisible();
                    }     
                } // backspace
            repaint();
            }
        }
        public void focusGained(FocusEvent e) {}
        public void focusLost(FocusEvent e)
        {   if (editable)
            {   caretVisible = false;
                if (!text.equals(oldText))
                {   oldText = text;
                    textValueChanged = true; 
                }    
                else
                    textValueChanged = false; 
                repaint();
            }    
        }
    }
}