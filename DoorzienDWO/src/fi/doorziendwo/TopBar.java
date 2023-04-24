package fi.doorziendwo;

import java.awt.Dimension;
import java.awt.Panel;

import javax.swing.*;

public class TopBar extends JPanel
{   // applet frame    
    DoorzienFrame owner;
    DoorzienPanel owner2;
    
    public TopBar(DoorzienFrame o)
    {   owner = o;
    }    
    
    public TopBar(DoorzienPanel o)
    {   owner2 = o;
    }    
    
    // for BorderLayout    
    public Dimension getPreferredSize()
    {   if (owner2 == null)
    		return new Dimension(owner.getSize().width,
                             	 owner.totalTopHeight);
    	else
    		return new Dimension(owner2.getSize().width,
                	             owner2.totalTopHeight);
    
    }    

}


