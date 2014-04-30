package fi.stroomdiagrammen;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;

import fi.beans.copyright.FIButton;

// panel for bottom part
public class BottomPanel extends JPanel
{   // owner
    //FlowFrame owner;
	Stroomdiagrammen owner;
    // effective area
    Rectangle rect;
    // font(s)
    Font fo1 = new Font("Dialog", Font.PLAIN, 11);
    FontMetrics fm1 = getFontMetrics(fo1);    
    Font fo2 = new Font("Dialog", Font.BOLD, 11);
    // components here
    // buttons
    JButton newButton, bubbleButton, previousButton;
    // copyright
    FIButton fiButton;

JTextField test;    

	boolean realDWO = false;
	SDInteractiePanel sdip;
    
    // constructor
    public BottomPanel(Stroomdiagrammen o)
    {   
    	owner = o;
        setBackground(Stroomdiagrammen.appletBackground);                                   
        // allow using coordinates
        setLayout(null);
        // create and add components
        // set location in initialize()
        newButton = new JButton(owner.rb.getString("newDiagramText"));
        newButton.setBackground(owner.buttonColor);
        newButton.setFont(fo1);
        add(newButton);
        newButton.addActionListener(new NewAL());
        
        bubbleButton = new JButton(owner.rb.getString("flowOnText"));
        bubbleButton.setBackground(owner.buttonColor);
        bubbleButton.setFont(fo1);
        add(bubbleButton);
        bubbleButton.addActionListener(new BubbleAL());
        
        previousButton = new JButton(owner.rb.getString("previousText"));
        previousButton.setBackground(owner.buttonColor);
        previousButton.setFont(fo1);
        add(previousButton);
        previousButton.setEnabled(false);
        previousButton.addActionListener(new PreviousAL());
  
//test = new JTextField();
//add(test);
        
        if (!realDWO)
        {	
        	//Fi-logo, copyright
        	fiButton = new FIButton("Info",
        		new String[]
        		{	owner.rb.getString("titelText"),
        			owner.rb.getString("versionText") + "20110412",
        			owner.rb.getString("authorText"),
        			owner.rb.getString("programText"),
        			owner.rb.getString("fiText"),
        			"www.fi.uu.nl",
        			""
        		});
        	add(fiButton);
        }	
		
        initialize();
        
    }
    
    public BottomPanel(Stroomdiagrammen o, boolean realDWO, SDInteractiePanel sdip)
    {	this.realDWO = realDWO;
    	this.sdip = sdip;
		owner = o;
		setBackground(Stroomdiagrammen.appletBackground);                                   
		//allow using coordinates
		setLayout(null);
		// create and add components
		//set location in initialize()
		newButton = new JButton(owner.rb.getString("newDiagramText"));
		newButton.setBackground(owner.buttonColor);
		newButton.setFont(fo1);
		add(newButton);
		newButton.addActionListener(new NewAL());
    
		bubbleButton = new JButton(owner.rb.getString("flowOnText"));
		bubbleButton.setBackground(owner.buttonColor);
		bubbleButton.setFont(fo1);
		add(bubbleButton);
		bubbleButton.addActionListener(new BubbleAL());
    
		previousButton = new JButton(owner.rb.getString("previousText"));
		previousButton.setBackground(owner.buttonColor);
		previousButton.setFont(fo1);
		add(previousButton);
		previousButton.setEnabled(false);
		previousButton.addActionListener(new PreviousAL());

//test = new JTextField();
//add(test);
    
	
		initialize();
    	
    	
    }
    // set locations at startup and after resizing    
    public void initialize()
    {   // determine effective area
    	if (realDWO)
    	{
    		rect = new Rectangle(DrawingPanel.GRIDSIZE, 0, 
					             getSize().width - 2 * DrawingPanel.GRIDSIZE,
					             owner.bottomHeight - DrawingPanel.GRIDSIZE);
    		
    	}
    	else
    	{	
    		rect = new Rectangle(DrawingPanel.GRIDSIZE, 0, 
    							 owner.getSize().width - 2 * DrawingPanel.GRIDSIZE,
    							 owner.bottomHeight - DrawingPanel.GRIDSIZE);
    	}
        // for layout        
        int vGap = DrawingPanel.GRIDSIZE;
        int hGap = DrawingPanel.GRIDSIZE; 
        int currentX = rect.x + hGap;
        int currentY = rect.y + vGap;
        int width = fm1.stringWidth(newButton.getText()) + 35;
        newButton.setBounds(currentX, currentY,
            //fm1.stringWidth(newButton.getLabel() + "XXX"),
        	width, 3 * fm1.getHeight() / 2);	
            //2 * vGap);
        currentX += newButton.getSize().width + 2 * hGap;
        
        width = fm1.stringWidth(bubbleButton.getText()) + 35;
        bubbleButton.setBounds(currentX, currentY,
            //fm1.stringWidth(owner.rb.getString("flowOffText") + "XXX"),
        	width, 3 * fm1.getHeight() / 2);	
            //2 * vGap);
        currentX += bubbleButton.getSize().width + 2 * hGap;
        
        width = fm1.stringWidth(previousButton.getText()) + 35;
        previousButton.setBounds(currentX, currentY,
            //fm1.stringWidth(owner.rb.getString("previousText") + "XXX"),
        	width, 3 * fm1.getHeight() / 2);	
            //2 * vGap);
        currentX += previousButton.getSize().width + 2 * hGap;
        
//test.setBounds(currentX, currentY, 2 * width, 3 * fm1.getHeight() / 2);        
        
        currentX = rect.x + rect.width - hGap - 20;
        currentY = rect.y + vGap;
        if (fiButton != null)
        	fiButton.setBounds(currentX, currentY, 20, 30);
        
        repaint();
    } 
    // for BorderLayout    
    public Dimension getPreferredSize()
    {   return new Dimension(owner.getSize().width, 
                             owner.bottomHeight);
    }    
    public void paintComponent(Graphics g)
    {   
    	if (realDWO)
    	{	
    		g.setColor(Stroomdiagrammen.appletBackground);
    		g.fillRect(0, 0, getSize().width, getSize().height);
    	}	
    	
    	// fill effective area with background and outline
        g.setColor(Stroomdiagrammen.bottomBackground);
        g.fillRect(rect.x, rect.y, rect.width, rect.height);
        g.setColor(Color.black);
        g.drawRect(rect.x, rect.y, rect.width - 1, rect.height - 1);
    }    

    // inner class for newButton
    class NewAL implements ActionListener
    {   public void actionPerformed(ActionEvent e)
        {   if (realDWO) 
        		sdip.drawingPanel.diagramManager.clearDiagram(true);
        	else
        		owner.drawingPanel.diagramManager.clearDiagram(true);
        }    
    }    
    // inner class for bubbleButton
    class BubbleAL implements ActionListener
    {   public void actionPerformed(ActionEvent e)
        {   
    		if (realDWO)
    		{
    			if (sdip.drawingPanel.flowOn)
    			{   sdip.drawingPanel.flowOn = false;
    				// sdip.drawingPanel.flowThread.suspend();
    				// 	for Netscape
    				// note: stop kills the Thread!
    				sdip.drawingPanel.flowThread.stop();
                	bubbleButton.setLabel(Stroomdiagrammen.rb.getString("flowOnText"));
    			}
    			else
    			{   sdip.drawingPanel.flowOn = true;
                	// sdip.drawingPanel.flowThread.resume();
                	// 	for Netscape
    				sdip.drawingPanel.flowThread = new Thread(sdip.drawingPanel);
    				sdip.drawingPanel.flowThread.start();                
                	bubbleButton.setLabel(Stroomdiagrammen.rb.getString("flowOffText"));
    			}
    			sdip.drawingPanel.addToHistory();
    			
    		}
    		else
    		{	
    			if (owner.drawingPanel.flowOn)
    			{   owner.drawingPanel.flowOn = false;
    				// owner.drawingPanel.flowThread.suspend();
    				// 	for Netscape
    				// note: stop kills the Thread!
                	owner.drawingPanel.flowThread.stop();
                	bubbleButton.setLabel(Stroomdiagrammen.rb.getString("flowOnText"));
    			}
    			else
    			{   owner.drawingPanel.flowOn = true;
                	// owner.drawingPanel.flowThread.resume();
                	// 	for Netscape
                	owner.drawingPanel.flowThread = new Thread(owner.drawingPanel);
                	owner.drawingPanel.flowThread.start();                
                	bubbleButton.setLabel(Stroomdiagrammen.rb.getString("flowOffText"));
    			}
    			owner.drawingPanel.addToHistory();
    		}
        }    
    } 
    
    class PreviousAL implements ActionListener
    {   public void actionPerformed(ActionEvent e)
        {   if (realDWO)
        	{	sdip.drawingPanel.previousDiagram();
    			if (sdip.drawingPanel.history.size() <= 1)
    				previousButton.setEnabled(false);
        	}
        	else
        	{	owner.drawingPanel.previousDiagram();
        		if (owner.drawingPanel.history.size() <= 1)
        			previousButton.setEnabled(false);
        	}
            
        }    
    }    
    
} // class BottomPanel   

