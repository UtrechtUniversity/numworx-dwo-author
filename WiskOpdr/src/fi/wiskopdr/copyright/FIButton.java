package fi.wiskopdr.copyright;

import java.awt.*;
import java.awt.event.*;

import fi.wiskopdr.DialogFacade;
/**
 * Custom implementatie voor WiskOpdr. Met DialogFacade 
 * @see fi.beans.copyright.FIButton
 * @author wim
 */
public class FIButton extends Component
{   InfoFrame infoFrame;
// listener class for button and window events

	class WL extends WindowAdapter
{   public void windowClosing(WindowEvent e)
    {   dialog.dispose();
    }
    public void windowDeactivated(WindowEvent e)
    {   dialog.dispose();
    }

} // class WAL


	DialogFacade dialog;
    String titel;
    String[] text;
    public FIButton(String titel, String[] text)
    {   this.titel = titel;
        this.text = text;
        addMouseListener(new FIML());
    }

    public void paint(Graphics g)
    {   // logo
        int logoHeight = (getSize().height / 20) * 20;
        int logoWidth = 3 * (logoHeight / 5);
        int bx = (getSize().width - logoWidth) / 2;
        int by = (getSize().height - logoHeight) / 2;
        InfoFrame.drawFILogo(g, bx, by, logoHeight);
        // button outline
        /*g.setColor(Color.white);
        g.drawLine(0, 0, getSize().width - 1, 0);
        g.drawLine(0, 0, 0, getSize().height - 1);
        g.setColor(Color.black);
        g.drawLine(getSize().width - 1, 0,
                   getSize().width - 1, getSize().height - 1);
        g.drawLine(0, getSize().height - 1,
                   getSize().width - 1, getSize().height - 1);*/
    }
    // inner class
    class FIML extends MouseAdapter
    {   public void mousePressed(MouseEvent e)
        {   infoFrame = new InfoFrame(text);
        	dialog = DialogFacade.newInstance(e.getComponent(), titel);
        	dialog.getContentPane().add(infoFrame);
        	Dimension size = infoFrame.getSize();
        	Insets inset = dialog.getInsets();
// maak van binnenmaten buitenmaten.
        	size.width += inset.left + inset.right;
        	size.height += inset.top + inset.bottom;
			dialog.setSize(size);
        	dialog.setLocation(20, 20);
        	dialog.addWindowListener(new WL());
        	dialog.setResizable(false); // de default is true;
            dialog.setVisible(true);
        }
    }
} // class FIButton
