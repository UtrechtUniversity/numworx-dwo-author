package fi.doorziendwo;

import javax.swing.*;

import fi.doorziendwo.DoorzienFrame.CL;
import fi.doorziendwo.DoorzienFrame.WL;

import java.awt.*;
import java.awt.event.*;

public class DoorzienFrame2 extends JFrame 
{
    // initial frame size, used only once
    public int minWidth = 700;
    public int minHeight = 500;
    
    DoorzienPanel owner;
    DoorzienPanel doorzienPanel;
    
    // constructor
	public DoorzienFrame2(DoorzienPanel o)
	{	owner = o;
		
		setTitle(owner.tt("titelText"));
		// default constructor super() gets called here
  	    setVisible(true);
  	    
		setLayout(null);
		
		setSize(minWidth, minHeight); 

//System.out.println("mw = " + getSize().width);
//System.out.println("mh = " + getSize().height);

//        int framebreedte = getSize().width + getInsets().left + getInsets().right;
//		int framehoogte = getSize().height + getInsets().top + getInsets().bottom;
		
//		setSize(framebreedte, framehoogte);

//System.out.println("fw = " + getSize().width);
//System.out.println("fh = " + getSize().height);
		
		// create and add GUI compoments
        doorzienPanel = new DoorzienPanel(0, 0, 
        	getSize().width  - (getInsets().left + getInsets().right), 
        	getSize().height - (getInsets().top + getInsets().bottom));
		
        add(doorzienPanel);        
        
        doorzienPanel.isInFrame = true;

//System.out.println("dpw = " + doorzienPanel.getSize().width);
//System.out.println("dph = " + doorzienPanel.getSize().height);
        
		WL wListener = new WL();
		addWindowListener(wListener);
		CL cListener = new CL();
		addComponentListener(cListener);

	}    
	
	
    class WL extends WindowAdapter
    {   public void windowClosing(WindowEvent e)
        {   
			owner.setState(doorzienPanel.getState());
    		owner.frameStarted = false;
    		owner.toolsButton.setEnabled(true);
    		owner.resetButton.setEnabled(true);
        	dispose();
            
        }
    }
    class CL extends ComponentAdapter
    {   public void componentResized(ComponentEvent e)
        {   
        	doorzienPanel.setSize( 
            	getSize().width  - (getInsets().left + getInsets().right), 
            	getSize().height - (getInsets().top + getInsets().bottom));

    		doorzienPanel.invalidate();
    		doorzienPanel.validate();
            repaint();
        }
    }

}
