package fi.doorziendwo;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Panel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

//toolbar on the right
public class RightToolBar extends Panel
{   // owner
    DoorzienFrame owner;
    
    // components here
    // top to bottom
    ImageButton rotateButton,
                wireSolidButton, zoomInButton, zoomOutButton,
                conDrawButton, dimButton, axesButton
                ;
    // button height
    int buttonHeight = 32;

    // constructor
    public RightToolBar(DoorzienFrame o)
    {   owner = o;
        setBackground(DoorzienFrame.appletBackground);                                   
        // allow using coordinates
        setLayout(null);
        // create and add components top to bottom
        int currentX = owner.offSet;
        int currentY = owner.offSet;
        
        rotateButton = new ImageButton(owner.rotateImage, null);
        rotateButton.setLocation(currentX, currentY);
        
//        add(rotateButton);
        // listener
//        rotateButton.addMouseListener(new RotateML());
//        currentY += buttonHeight + 2 * owner.offSet;
        
        wireSolidButton = new ImageButton(owner.solidImage, null);
        wireSolidButton.setLocation(currentX, currentY);
        add(wireSolidButton);
        // listener
        wireSolidButton.addMouseListener(new WireSolidML());
        currentY += buttonHeight + 2 * owner.offSet;
        
        zoomInButton = new ImageButton(owner.zoomInImage, 
                                       owner.zoomInOffImage);
        zoomInButton.setLocation(currentX, currentY);
        add(zoomInButton);
        // listener
        zoomInButton.addMouseListener(new ZoomInML());        
        currentY += buttonHeight + owner.offSet;
        
        zoomOutButton = new ImageButton(owner.zoomOutImage, 
                                       owner.zoomOutOffImage);
        zoomOutButton.setLocation(currentX, currentY);
        add(zoomOutButton);
        // listener
        zoomOutButton.addMouseListener(new ZoomOutML());                
        currentY += buttonHeight + 2 * owner.offSet;
        
        conDrawButton = new ImageButton(owner.conDrawImage, null);
//                                        owner.conDrawOffImage);
        conDrawButton.setLocation(currentX, currentY);

        
        if (owner.conDraw)
        {
            add(conDrawButton);
            // listener
            conDrawButton.addMouseListener(new ConDrawML());
            currentY += buttonHeight + 2 * owner.offSet;
        }
        
    }
    
    public void initialize()
    {
        
        rotateButton.setSize(getSize().width - 2 * owner.offSet, 
                             buttonHeight);    
        
        wireSolidButton.setSize(getSize().width - 2 * owner.offSet, 
                                buttonHeight);    
        zoomInButton.setSize(getSize().width - 2 * owner.offSet, 
                             buttonHeight);    
        zoomOutButton.setSize(getSize().width - 2 * owner.offSet, 
                              buttonHeight);    
        conDrawButton.setSize(getSize().width - 2 * owner.offSet, 
                              buttonHeight);    
    }
    
    public void resetDefaults()
    {
        wireSolidButton.setImage(owner.solidImage);    
        zoomInButton.setOn(true);
        zoomOutButton.setOn(true);

        conDrawButton.setImage(owner.conDrawImage);        
    }
    // for BorderLayout    
    public Dimension getPreferredSize()
    {   return new Dimension(owner.rightWidth,
                             owner.getSize().height);
    }    
    
    public void update(Graphics g)
    {   paint(g);
    }    
    
    
	// draw offscreen
	public void paint(Graphics g)
	{   Image offscreen = createImage(getSize().width, getSize().height);
	    Graphics og = offscreen.getGraphics();
	    og.setClip(0, 0, getSize().width, getSize().height);
	    paintRight(og);
	    g.drawImage(offscreen, 0, 0, null);
	    og.dispose();
	}
    
    public void paintRight(Graphics g)
    {   
        // fill effective area with background and outline
        g.setColor(DoorzienFrame.rightBackground);
        g.fillRect(0, 0, getSize().width, getSize().height);
        g.setColor(Color.black);
        g.drawRect(- 1, 0, getSize().width, getSize().height - 1);
        super.paint(g);
    }    
  
    
    // listeners for each imagebutton
    class WireSolidML extends MouseAdapter
    {   String lastHelpMessage;
        public void mousePressed(MouseEvent e)
        {   if (wireSolidButton.enabled)
            {
                if (owner.drawingPanel.filled)
                {   owner.drawingPanel.setFilled(false);
                    wireSolidButton.setImage(owner.solidImage);
                    //owner.helpBar.setText(owner.tt("solidText"));                    
                    owner.helpBar.setMessage(null, 0);
                }
                else
                {   owner.drawingPanel.setFilled(true);
                    wireSolidButton.setImage(owner.wireFrameImage);
                    //owner.helpBar.setText(owner.tt("wireFrameText"));                    
                    owner.helpBar.setMessage(null, 0);                    
                }    
                //lastHelpMessage = owner.helpBar.text;                
            }
        }  
        public void mouseEntered(MouseEvent e)
        {   if (wireSolidButton.enabled)
            {   lastHelpMessage = owner.helpBar.text;
                if (owner.drawingPanel.filled)
                {   //owner.helpBar.setText(owner.tt("wireFrameText"));
                    owner.helpBar.setMessage(owner.tt("wireFrameText"),
                                             owner.helpBar.getSize().width - 
                                             owner.topToolBar.buttonWidth / 2);
                
                }
                else
                {   //owner.helpBar.setText(owner.tt("solidText"));
                    owner.helpBar.setMessage(owner.tt("solidText"),
                                             owner.helpBar.getSize().width - 
                                             owner.topToolBar.buttonWidth / 2);
                
                }
            }
        }    
        public void mouseExited(MouseEvent e)
        {   if (wireSolidButton.enabled)
            {   owner.helpBar.setMessage(null, 0);          
                owner.helpBar.setText(lastHelpMessage);
            }
        }    
    }    
    class ZoomInML extends MouseAdapter
    {   String lastHelpMessage;
        public void mousePressed(MouseEvent e)
        {   if (zoomInButton.enabled)
            {   owner.drawingPanel.zoomIn();
                zoomOutButton.setOn(true);
                if (owner.drawingPanel.zoom >= 
                    (owner.drawingPanel.MAXZOOM - 
                     owner.drawingPanel.ZOOMSTEP / 10))
                    zoomInButton.setOn(false);    
                //lastHelpMessage = owner.helpBar.text;     
                owner.helpBar.setMessage(null, 0);          
            }
        }  
        public void mouseEntered(MouseEvent e)
        {   if (zoomInButton.enabled)
            {   lastHelpMessage = owner.helpBar.text;                
                //owner.helpBar.setText(owner.tt("zoomInText"));
                owner.helpBar.setMessage(owner.tt("zoomInText"),
                                         owner.helpBar.getSize().width - 
                                         owner.topToolBar.buttonWidth / 2);
                
            }
        }    
        public void mouseExited(MouseEvent e)
        {   if (zoomInButton.enabled)
            {    owner.helpBar.setMessage(null, 0);          
                 owner.helpBar.setText(lastHelpMessage);
            }
        }    
    }    
    class ZoomOutML extends MouseAdapter
    {   String lastHelpMessage;
        public void mousePressed(MouseEvent e)
        {   if (zoomOutButton.enabled)
            {   owner.drawingPanel.zoomOut();
                zoomInButton.setOn(true);
                if (owner.drawingPanel.zoom <= 
                    (owner.drawingPanel.MINZOOM + 
                     owner.drawingPanel.ZOOMSTEP / 10))
                {                    
                    zoomOutButton.setOn(false);    
                }
                //lastHelpMessage = owner.helpBar.text;      
                owner.helpBar.setMessage(null, 0);          
            }
            
        }  
        public void mouseEntered(MouseEvent e)
        {   if (zoomOutButton.enabled)
            {   lastHelpMessage = owner.helpBar.text;                
                //owner.helpBar.setText(owner.tt("zoomOutText"));
                owner.helpBar.setMessage(owner.tt("zoomOutText"),
                                         owner.helpBar.getSize().width - 
                                         owner.topToolBar.buttonWidth / 2);
                
            }
        }    
        public void mouseExited(MouseEvent e)
        {   if (zoomOutButton.enabled)
            {   owner.helpBar.setMessage(null, 0);           
                owner.helpBar.setText(lastHelpMessage);
            }
        }    
    }    
    class ConDrawML extends MouseAdapter
    {   String lastHelpMessage;
        public void mousePressed(MouseEvent e)
        {   if (conDrawButton.enabled)
            {   if (owner.drawingPanel.mouseMode != 
                    DrawingPanel.FOLDOUT)
                {   owner.drawingPanel.makeFoldOut(0, true);
                    // figureImage is set in stepNum == 1
                }
                else
                {   // back to whole figure
                    conDrawButton.setImage(owner.conDrawImage);
                    owner.drawingPanel.makeFoldOut(0, false);
                }    
                lastHelpMessage = owner.helpBar.text;       
                owner.helpBar.setMessage(null, 0);          
            }
        }  
        public void mouseEntered(MouseEvent e)
        {   if (conDrawButton.enabled)
            {   lastHelpMessage = owner.helpBar.text;                
                if (owner.drawingPanel.mouseMode != 
                    DrawingPanel.FOLDOUT)
                {    
                    //owner.helpBar.setText(owner.tt("conDrawText"));
                    owner.helpBar.setMessage(owner.tt("conDrawText"),
                                             owner.helpBar.getSize().width - 
                                             owner.topToolBar.buttonWidth / 2);
                    
                }    
                else
                {   if (owner.drawingPanel.startFacet == null)
                    {
                    }
                    else
                    {
                        //owner.helpBar.setText(owner.tt("wholeFigureText"));
                        owner.helpBar.setMessage(owner.tt("wholeFigureText"),
                                                 owner.helpBar.getSize().width - 
                                                 owner.topToolBar.buttonWidth / 2);
                    }
                    
                }    
            }
        }    
        public void mouseExited(MouseEvent e)
        {   if (conDrawButton.enabled)
            {   owner.helpBar.setMessage(null, 0);           
                owner.helpBar.setText(lastHelpMessage);
            }
        }    
    }    
    
    class RotateML extends MouseAdapter
    {   String lastHelpMessage;
        public void mousePressed(MouseEvent e)
        {   if (rotateButton.enabled)
            {   //if (owner.drawingPanel.escapeActive())
                //{
                    lastHelpMessage = owner.tt("rotateText");
                    owner.drawingPanel.rotate();
                    owner.helpBar.setMessage(null, 0);                      
                    owner.helpBar.setText(owner.tt("rotateText"));                
                //}
            }
        }  
        public void mouseEntered(MouseEvent e)
        {   if (rotateButton.enabled)
            {   
                lastHelpMessage = owner.helpBar.text;                
//                if (owner.drawingPanel.escapeActive())
//                {
 //                   lastHelpMessage = owner.helpBar.text;
                    owner.helpBar.setMessage(owner.tt("escapeText"),
                                             owner.helpBar.getSize().width - 
                                             owner.topToolBar.buttonWidth / 2);
//                }
            }
        }    
        public void mouseExited(MouseEvent e)
        {   if (rotateButton.enabled)
            {   owner.helpBar.setMessage(null, 0);          
//                if (owner.drawingPanel.escapeActive())
                    owner.helpBar.setText(lastHelpMessage);
                
            }
        }    
    }    


} // class RightToolBar   

