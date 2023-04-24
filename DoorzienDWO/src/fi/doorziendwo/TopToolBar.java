package fi.doorziendwo;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Panel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class TopToolBar  extends Panel
{   // owner
    DoorzienFrame owner;
    // components here
    // left to right
    ImageButton drawLineButton, deleteLineButton, 
                lengLinesButton, shortLinesButton,
    
                drawPlaneButton, parPlaneButton,
                deletePlaneButton,
                planesFilledButton, 
                transPlaneButton,  rotPlaneButton, 
                showCutButton, cutButton,
                undoButton, redoButton
                ;
    // button width
    int buttonWidth = 47;

    
    // constructor
    public TopToolBar(DoorzienFrame o)
    {   owner = o;
        setBackground(DoorzienFrame.appletBackground);                                   
        // allow using coordinates
        setLayout(null);
        // create and add components left to right
        int currentX = owner.offSet;
        int currentY = owner.offSet;
        
        // lines
        drawLineButton = new ImageButton(owner.drawLineImage, 
                                         owner.drawLineOffImage);
        drawLineButton.setLocation(currentX, currentY);
        if (owner.drawLines)
        {
            add(drawLineButton);
            // listener
            drawLineButton.addMouseListener(new DrawLineML());
            currentX += buttonWidth + owner.offSet;
        }
        deleteLineButton = new ImageButton(owner.deleteLineImage, 
                                           owner.deleteLineOffImage);
        deleteLineButton.setOn(false);                                                                                                                        
        deleteLineButton.setLocation(currentX, currentY);
        if (owner.removeLines)
        {
            add(deleteLineButton);
            // listener
            deleteLineButton.addMouseListener(new DeleteLineML());
            currentX += buttonWidth + owner.offSet;
        }
        lengLinesButton = new ImageButton(owner.lengLinesImage, 
                                          owner.lengLinesOffImage);
        lengLinesButton.setOn(false);                                                                             
        lengLinesButton.setLocation(currentX, currentY);
        if (owner.lengLines)
        {
            add(lengLinesButton);
            // listener
            lengLinesButton.addMouseListener(new LengLinesML());
            currentX += buttonWidth + owner.offSet;
        }
        shortLinesButton = new ImageButton(owner.shortLinesImage, 
                                           owner.shortLinesOffImage);
        shortLinesButton.setOn(false);                                                                             
        shortLinesButton.setLocation(currentX, currentY);
        if (owner.lengLines)
        {
            add(shortLinesButton);
            // listener
            shortLinesButton.addMouseListener(new ShortLinesML());
            currentX += buttonWidth + 4 * owner.offSet;
        }

        // planes
        drawPlaneButton = new ImageButton(owner.drawPlaneImage, 
                                          owner.drawPlaneOffImage);
        drawPlaneButton.setLocation(currentX, currentY);
        if (owner.drawPlanes)
        {
            add(drawPlaneButton);
            // listener
            drawPlaneButton.addMouseListener(new DrawPlaneML());
            currentX += buttonWidth + owner.offSet;
        }
        
        parPlaneButton = new ImageButton(owner.parPlaneImage, 
                                         owner.parPlaneOffImage);
        parPlaneButton.setLocation(currentX, currentY);
        if (owner.parPlanes)
        {
            add(parPlaneButton);
            // listener
            parPlaneButton.addMouseListener(new ParPlaneML());
            currentX += buttonWidth + owner.offSet;
        }
        
        deletePlaneButton = new ImageButton(owner.deletePlaneImage, 
                                            owner.deletePlaneOffImage);
        deletePlaneButton.setOn(false);                                                                                                                        
        deletePlaneButton.setLocation(currentX, currentY);
        if (owner.removePlanes)
        {
            add(deletePlaneButton);
            // listener
            deletePlaneButton.addMouseListener(new DeletePlaneML());
            currentX += buttonWidth + owner.offSet;
        }

        planesFilledButton = new ImageButton(owner.planesFilledImage, 
                                             owner.planesFilledOffImage);
        planesFilledButton.setOn(false);                                                                                                                        
        planesFilledButton.setLocation(currentX, currentY);
        if (owner.fillPlanes)
        {
            add(planesFilledButton);
            // listener
            planesFilledButton.addMouseListener(new PlanesFilledML());
            currentX += buttonWidth + owner.offSet;
        }
        transPlaneButton = new ImageButton(owner.transPlaneImage, 
                                           owner.transPlaneOffImage);
        transPlaneButton.setOn(false);                                   
        transPlaneButton.setLocation(currentX, currentY);
//ALLEEN VOOR FI        
        if (DoorzienDWO.version == DoorzienDWO.FI)
        {
            add(transPlaneButton);
            // listener
            transPlaneButton.addMouseListener(new TranslatePlaneML());
            currentX += buttonWidth + owner.offSet;
        }
        
        rotPlaneButton = new ImageButton(owner.rotPlaneImage, 
                                         owner.rotPlaneOffImage);
        rotPlaneButton.setOn(false);                                   
        rotPlaneButton.setLocation(currentX, currentY);

//ALLEEN VOOR FI        
        if (DoorzienDWO.version == DoorzienDWO.FI)
        {   add(rotPlaneButton);
            // listener
            rotPlaneButton.addMouseListener(new RotatePlaneML());
            currentX += buttonWidth + owner.offSet;
        }
        showCutButton = new ImageButton(owner.showCutImage, 
                                    owner.showCutOffImage);
        showCutButton.setOn(false);                                   
        showCutButton.setLocation(currentX, currentY);
        if (owner.showCut)
        {
            add(showCutButton);
            // listener
            showCutButton.addMouseListener(new ShowCutML());
            currentX += buttonWidth + owner.offSet;
        }
        cutButton = new ImageButton(owner.cutImage, 
                                    owner.cutOffImage);
        cutButton.setOn(false);                                   
        cutButton.setLocation(currentX, currentY);
        if (owner.cutObject)
        {
            add(cutButton);
            // listener
            cutButton.addMouseListener(new CutML());
            currentX += buttonWidth + 4 * owner.offSet;
        }

        undoButton = new ImageButton(owner.undoImage, 
                                     owner.undoOffImage);
        undoButton.setOn(false);                                   
        undoButton.setLocation(currentX, currentY);
        add(undoButton);
        // listener
        undoButton.addMouseListener(new UndoML());        
        
        currentX += buttonWidth + owner.offSet;        
        
        redoButton = new ImageButton(owner.redoImage, 
                                     owner.redoOffImage);
        redoButton.setOn(false);                                   
        redoButton.setLocation(currentX, currentY);
        add(redoButton);
        // listener
        redoButton.addMouseListener(new RedoML());        
        
    }
    
    public void initialize()
    {
        drawLineButton.setSize(buttonWidth,
                               getSize().height - 2 * owner.offSet); 
        deleteLineButton.setSize(buttonWidth,
                               getSize().height - 2 * owner.offSet); 
        lengLinesButton.setSize(buttonWidth,
                                getSize().height - 2 * owner.offSet); 
        shortLinesButton.setSize(buttonWidth,
                                getSize().height - 2 * owner.offSet); 
                                
        drawPlaneButton.setSize(buttonWidth,
                                getSize().height - 2 * owner.offSet); 
        parPlaneButton.setSize(buttonWidth,
                                getSize().height - 2 * owner.offSet); 
        deletePlaneButton.setSize(buttonWidth,
                                getSize().height - 2 * owner.offSet); 
        planesFilledButton.setSize(buttonWidth,
                                getSize().height - 2 * owner.offSet); 
                                
                                
        transPlaneButton.setSize(buttonWidth,
                                 getSize().height - 2 * owner.offSet); 
        rotPlaneButton.setSize(buttonWidth,
                               getSize().height - 2 * owner.offSet); 
        showCutButton.setSize(buttonWidth,
                          getSize().height - 2 * owner.offSet); 
        cutButton.setSize(buttonWidth,
                          getSize().height - 2 * owner.offSet); 
                          
        undoButton.setSize(buttonWidth,
                          getSize().height - 2 * owner.offSet); 
        redoButton.setSize(buttonWidth,
                          getSize().height - 2 * owner.offSet); 
                          
                               
    }    
    // situation: no lines, no planes
    public void resetDefaults()
    {   // just in case
        drawLineButton.setOn(true);
        deleteLineButton.setOn(false);
        lengLinesButton.setOn(false);
        shortLinesButton.setOn(false);

        drawPlaneButton.setOn(true);
        parPlaneButton.setOn(false);
        deletePlaneButton.setOn(false);        
        planesFilledButton.setOn(false);        
        transPlaneButton.setOn(false);                                       
        rotPlaneButton.setOn(false);                                       
        showCutButton.setOn(false);
        cutButton.setOn(false);                                       
        
        undoButton.setOn(false);
        redoButton.setOn(false);
    }
    // true: at least one line
    // false: no lines
    public void activateLineButtons(boolean b)
    {   // just in case
        drawLineButton.setOn(true);
        deleteLineButton.setOn(b);
        lengLinesButton.setOn(b);  
        if (DrawConstants.llFactor < Vector3D.NZero)
            shortLinesButton.setOn(false);
        else
            shortLinesButton.setOn(true);

        
    }
    
    public void disableLineButtons()
    {   // just in case
        drawLineButton.setOn(false);
        deleteLineButton.setOn(false);
        lengLinesButton.setOn(false);                                                                                         
//        if (owner.drawingPanel.llFactor < Vector3D.NZero)
            shortLinesButton.setOn(false);
//        else
//            shortLinesButton.setOn(true);

    }
    
    // true: at least one plane
    // false: no planes
    public void activatePlaneButtons(boolean b)
    {   drawPlaneButton.setOn(true);
        parPlaneButton.setOn(b);
        deletePlaneButton.setOn(b);        
        planesFilledButton.setOn(b);
        if (owner.drawingPanel.planesFilled && b)
            planesFilledButton.setImage(owner.planesEmptyImage);
        transPlaneButton.setOn(b);                                       
        rotPlaneButton.setOn(b);   
        showCutButton.setOn(b);
        if (owner.drawingPanel.showCut)
            showCutButton.setImage(owner.hideCutImage);
        cutButton.setOn(b);                                       
    }

    public void disablePlaneButtons()
    {   drawPlaneButton.setOn(false);
        parPlaneButton.setOn(false);
        deletePlaneButton.setOn(false);        
        planesFilledButton.setOn(false);
//        if (owner.drawingPanel.planesFilled && b)
//            planesFilledButton.setImage(owner.planesEmptyImage);
        transPlaneButton.setOn(false);                                       
        rotPlaneButton.setOn(false);   
        showCutButton.setOn(false);
//        if (owner.drawingPanel.showCut)
//            showCutButton.setImage(owner.hideCutImage);
        cutButton.setOn(false);                                       
    }
    
    
    // for BorderLayout    
    public Dimension getPreferredSize()
    {   return new Dimension(owner.getSize().width, 
                             owner.topHeight);
    }    
    
    public void update(Graphics g)
    {   paint(g);
    }    
    
	// draw offscreen
	public void paint(Graphics g)
	{   Image offscreen = createImage(getSize().width, getSize().height);
	    Graphics og = offscreen.getGraphics();
	    og.setClip(0, 0, getSize().width, getSize().height);
	    paintTop(og);
	    g.drawImage(offscreen, 0, 0, null);
	    og.dispose();
	}
    
    public void paintTop(Graphics g)
    {   
        
        g.setColor(DoorzienFrame.topBackground);
        g.fillRect(0, 0, getSize().width, getSize().height);
        g.setColor(Color.black);
        g.drawRect(0, 0, getSize().width - 1, getSize().height);
        super.paint(g);
    }    
    
    class DrawLineML extends MouseAdapter
    {   // remember last message
        String lastHelpMessage;
        public void mousePressed(MouseEvent e)
        {   if (drawLineButton.enabled)
            {   //if (owner.version == owner.FI)
                //{
                    if (owner.drawingPanel.mouseMode != 
                        DrawingPanel.DRAWLINE)
                        owner.drawingPanel.drawLine(0, true);
                    else
                    {   owner.drawingPanel.drawLine(0, false);
                    }
                //}
                //else if (owner.version == owner.EPN)
                //{   owner.drawingPanel.drawLine(0, true);
                    
                //}
                lastHelpMessage = owner.helpBar.text;
                owner.helpBar.setMessage(null, 0);
            }
        }  
        public void mouseEntered(MouseEvent e)
        {   if (drawLineButton.enabled)
            {   
                lastHelpMessage = owner.helpBar.text;             
                if (DoorzienDWO.version == DoorzienDWO.FI)
                {
                    if (owner.drawingPanel.mouseMode != 
                        DrawingPanel.DRAWLINE)
                    {    
                        //owner.helpBar.setText(owner.tt("drawLinesText"));
                        owner.helpBar.setMessage(owner.tt("drawLinesText"),
                                                 drawLineButton.getLocation().x + buttonWidth / 2);
                        
                    }    
                }                    
                else if (DoorzienDWO.version == DoorzienDWO.EPN)
                {
                    if (owner.drawingPanel.mouseMode != 
                        DrawingPanel.DRAWLINE)
                    {    
                        //owner.helpBar.setText(owner.tt("drawLineText"));
                        owner.helpBar.setMessage(owner.tt("drawLineText"),
                                                 drawLineButton.getLocation().x + buttonWidth / 2);
                    }    
                }                    
            }
        }    
        public void mouseExited(MouseEvent e)
        {   if (drawLineButton.enabled)
            {   owner.helpBar.setMessage(null, 0);
                owner.helpBar.setText(lastHelpMessage);
            }    
        }    
    }    

    
    class DeleteLineML extends MouseAdapter
    {   // remember last message
        String lastHelpMessage;
        public void mousePressed(MouseEvent e)
        {   if (deleteLineButton.enabled)
            {   
                if (owner.drawingPanel.mouseMode != 
                    DrawingPanel.DELETELINE)
                    owner.drawingPanel.deleteLine(0, true);
                else
                {   owner.drawingPanel.deleteLine(0, false);
                }

                lastHelpMessage = owner.helpBar.text;                
                owner.helpBar.setMessage(null, 0);
            }
        }  
        public void mouseEntered(MouseEvent e)
        {   if (deleteLineButton.enabled)
            {   lastHelpMessage = owner.helpBar.text;
            
                if (owner.drawingPanel.mouseMode != 
                    DrawingPanel.DELETELINE)
                owner.helpBar.setMessage(owner.tt("deleteLineText"),
                                         deleteLineButton.getLocation().x + buttonWidth / 2);
            }
        }    
        public void mouseExited(MouseEvent e)
        {   if (deleteLineButton.enabled)
            {   owner.helpBar.setMessage(null, 0);
                owner.helpBar.setText(lastHelpMessage);
            }    
        }    
    }    

    class LengLinesML extends MouseAdapter
    {   String lastHelpMessage;
        public void mousePressed(MouseEvent e)
        {   if (lengLinesButton.enabled)
            {   owner.drawingPanel.lengthenLines();
                shortLinesButton.setOn(true);
                if (DrawConstants.llFactor >= 
                    (owner.drawingPanel.MAXLLFACTOR - 
                     owner.drawingPanel.LLSTEP / 10))
                    lengLinesButton.setOn(false);    
                //lastHelpMessage = owner.helpBar.text;                            
                owner.helpBar.setMessage(null, 0);
                owner.helpBar.setText(owner.tt("rotateText"));                
                lastHelpMessage = owner.helpBar.text;                                            
            }
        }  
        public void mouseEntered(MouseEvent e)
        {   if (lengLinesButton.enabled)
            {   lastHelpMessage = owner.helpBar.text;                
                //owner.helpBar.setText(owner.tt("lengLinesText"));
                owner.helpBar.setMessage(owner.tt("lengLinesText"),
                                         lengLinesButton.getLocation().x + buttonWidth / 2);
                
            }
        }    
        public void mouseExited(MouseEvent e)
        {   if (lengLinesButton.enabled)
            {   owner.helpBar.setMessage(null, 0); 
                owner.helpBar.setText(lastHelpMessage);
            }
        }    
    }    
    class ShortLinesML extends MouseAdapter
    {   String lastHelpMessage;
        public void mousePressed(MouseEvent e)
        {   if (shortLinesButton.enabled)
            {   owner.drawingPanel.shortenLines();
                lengLinesButton.setOn(true);
                shortLinesButton.setOn(false);    
                //lastHelpMessage = owner.helpBar.text;                
                owner.helpBar.setMessage(null, 0);    
                owner.helpBar.setText(owner.tt("rotateText"));                
                lastHelpMessage = owner.helpBar.text;                                            
            }
            
        }  
        public void mouseEntered(MouseEvent e)
        {   if (shortLinesButton.enabled)
            {   lastHelpMessage = owner.helpBar.text;                
                //owner.helpBar.setText(owner.tt("shortLinesText"));
                owner.helpBar.setMessage(owner.tt("shortLinesText"),
                                         shortLinesButton.getLocation().x + buttonWidth / 2);
                
            }
        }    
        public void mouseExited(MouseEvent e)
        {   if (shortLinesButton.enabled)
            {   owner.helpBar.setMessage(null, 0);
                owner.helpBar.setText(lastHelpMessage);
            }
        }    
    }    
    
    
    
    class DrawPlaneML extends MouseAdapter
    {   // remember last message
        String lastHelpMessage;
        public void mousePressed(MouseEvent e)
        {   if (drawPlaneButton.enabled)
            {   
//                if (owner.version == owner.FI)
//                {
                    if (owner.drawingPanel.mouseMode != 
                        DrawingPanel.DRAWPLANE)
                        owner.drawingPanel.drawPlane(0, true);
                    else
                    {   owner.drawingPanel.drawPlane(0, false);
                    }
//                }
//                else if (owner.version == owner.EPN)
//                    owner.drawingPanel.drawPlane(0, true);
                lastHelpMessage = owner.helpBar.text;
                owner.helpBar.setMessage(null, 0);
            }
        }  
        public void mouseEntered(MouseEvent e)
        {   if (drawPlaneButton.enabled)
            {   
                lastHelpMessage = owner.helpBar.text;     
                if (DoorzienDWO.version == DoorzienDWO.FI)
                {
                    if (owner.drawingPanel.mouseMode != 
                        DrawingPanel.DRAWPLANE)
                    {    
                        //owner.helpBar.setText(owner.tt("drawPlanesText"));
                        owner.helpBar.setMessage(owner.tt("drawPlanesText"),
                                                 drawPlaneButton.getLocation().x + buttonWidth / 2);
                        
                    }    
                }    
                else if (DoorzienDWO.version == DoorzienDWO.EPN)
                {
                    if (owner.drawingPanel.mouseMode != 
                        DrawingPanel.DRAWPLANE)
                    {    
                        //owner.helpBar.setText(owner.tt("drawPlaneText"));
                        owner.helpBar.setMessage(owner.tt("drawPlaneText"),
                                                 drawPlaneButton.getLocation().x + buttonWidth / 2);
                    }    
                }    

            }
        }    
        public void mouseExited(MouseEvent e)
        {   if (drawPlaneButton.enabled)
            {   owner.helpBar.setMessage(null, 0);
                owner.helpBar.setText(lastHelpMessage);
            }    
        }    
    }    

    class ParPlaneML extends MouseAdapter
    {   // remember last message
        String lastHelpMessage;
        public void mousePressed(MouseEvent e)
        {   if (parPlaneButton.enabled)
            {   
                if (owner.drawingPanel.mouseMode != 
                    DrawingPanel.DRAWPARPLANE)
                    owner.drawingPanel.drawParPlane(0, true);
                else
                {   owner.drawingPanel.drawParPlane(0, false);
                }
                
                lastHelpMessage = owner.helpBar.text;                
                owner.helpBar.setMessage(null, 0);                
            }
        }  
        public void mouseEntered(MouseEvent e)
        {   if (parPlaneButton.enabled)
            {   lastHelpMessage = owner.helpBar.text;
                //owner.helpBar.setText(owner.tt("parPlaneText"));
                if (owner.drawingPanel.mouseMode != 
                    owner.drawingPanel.DRAWPARPLANE)
                    owner.helpBar.setMessage(owner.tt("parPlaneText"),
                                             parPlaneButton.getLocation().x + buttonWidth / 2);
                
            }
        }    
        public void mouseExited(MouseEvent e)
        {   if (parPlaneButton.enabled)
            {   owner.helpBar.setMessage(null, 0);
                owner.helpBar.setText(lastHelpMessage);
            }    
        }    
    }    

    class DeletePlaneML extends MouseAdapter
    {   // remember last message
        String lastHelpMessage;
        public void mousePressed(MouseEvent e)
        {   if (deletePlaneButton.enabled)
            {   
                if (owner.drawingPanel.mouseMode != 
                    DrawingPanel.DELETEPLANE)
                    owner.drawingPanel.deletePlane(0, true);
                else
                {   owner.drawingPanel.deletePlane(0, false);
                }
                lastHelpMessage = owner.helpBar.text;                
                owner.helpBar.setMessage(null, 0);                
            }
        }  
        public void mouseEntered(MouseEvent e)
        {   if (deletePlaneButton.enabled)
            {   lastHelpMessage = owner.helpBar.text;
                if (owner.drawingPanel.mouseMode != 
                    DrawingPanel.DELETEPLANE)
                    owner.helpBar.setMessage(owner.tt("deletePlaneText"),
                                             deletePlaneButton.getLocation().x + buttonWidth / 2);
                
            }
        }    
        public void mouseExited(MouseEvent e)
        {   if (deletePlaneButton.enabled)
            {   owner.helpBar.setMessage(null, 0);
                owner.helpBar.setText(lastHelpMessage);
            }    
        }    
    }    

    class PlanesFilledML extends MouseAdapter
    {   String lastHelpMessage;
        public void mousePressed(MouseEvent e)
        {   if (planesFilledButton.enabled)
            {
                if (owner.drawingPanel.planesFilled)
                {   owner.drawingPanel.fillPlanes(false);
                    planesFilledButton.setImage(owner.planesFilledImage);
                    //owner.helpBar.setText(owner.tt("planesFilledText"));                    
                    owner.helpBar.setMessage(null, 0);
//                    owner.helpBar.setMessage(owner.tt("planesFilledText"),
//                                             planesFilledButton.getLocation().x + buttonWidth / 2);
                    
                }
                else
                {   owner.drawingPanel.fillPlanes(true);
                    planesFilledButton.setImage(owner.planesEmptyImage);
                    //owner.helpBar.setText(owner.tt("planesEmptyText"));                    
                    owner.helpBar.setMessage(null, 0);                    
//                    owner.helpBar.setMessage(owner.tt("planesEmptyText"),
//                                             planesFilledButton.getLocation().x + buttonWidth / 2);
                    
                }    
                //lastHelpMessage = owner.helpBar.text;                
            }
        }  
        public void mouseEntered(MouseEvent e)
        {   if (planesFilledButton.enabled)
            {   lastHelpMessage = owner.helpBar.text;
                if (owner.drawingPanel.planesFilled)
                {   //owner.helpBar.setText(owner.tt("planesEmptyText"));
                    owner.helpBar.setMessage(owner.tt("planesEmptyText"),
                                             planesFilledButton.getLocation().x + buttonWidth / 2);
                
                }
                else
                {   //owner.helpBar.setText(owner.tt("planesFilledText"));
                    owner.helpBar.setMessage(owner.tt("planesFilledText"),
                                             planesFilledButton.getLocation().x + buttonWidth / 2);
                
                }
            }
        }    
        public void mouseExited(MouseEvent e)
        {   if (planesFilledButton.enabled)
            {   owner.helpBar.setMessage(null, 0);          
                owner.helpBar.setText(lastHelpMessage);
            }
        }    
    }    


    class RotatePlaneML extends MouseAdapter
    {   String lastHelpMessage;
        public void mousePressed(MouseEvent e)
        {   if (rotPlaneButton.enabled)
            {   
// dit werkt zowel als de knop is ingedrukt
// en wanneer je "gewoon stopt"
// tweede knop overbodig?
                if (owner.drawingPanel.mouseMode == 
                    DrawingPanel.ROTATEPLANE)
                {
                    owner.drawingPanel.rotatePlane(0, false);
                    rotPlaneButton.setImage(owner.rotPlaneImage);
                }
                else
                    owner.drawingPanel.rotatePlane(0, true);                
                lastHelpMessage = owner.helpBar.text;       
                owner.helpBar.setMessage(null, 0);                          
            }
        }  
        public void mouseEntered(MouseEvent e)
        {   if (rotPlaneButton.enabled)
            {   lastHelpMessage = owner.helpBar.text;                
                //owner.helpBar.setText(owner.tt("rotatePlaneText"));
                owner.helpBar.setMessage(owner.tt("rotatePlaneText"),
                                         rotPlaneButton.getLocation().x + buttonWidth / 2);
                
            }
        }    
        public void mouseExited(MouseEvent e)
        {   if (rotPlaneButton.enabled)
            {   owner.helpBar.setMessage(null, 0);           
                owner.helpBar.setText(lastHelpMessage);
            }
        }    
    }    

    class TranslatePlaneML extends MouseAdapter
    {   String lastHelpMessage;
        public void mousePressed(MouseEvent e)
        {   if (transPlaneButton.enabled)
            {   
                if (owner.drawingPanel.mouseMode == 
                    DrawingPanel.TRANSLATEPLANE)
                {
                    owner.drawingPanel.translatePlane(0, false);                    
                    transPlaneButton.setImage(owner.transPlaneImage);                    
                    
                }    
                else
                    owner.drawingPanel.translatePlane(0, true);                
                lastHelpMessage = owner.helpBar.text;        
                owner.helpBar.setMessage(null, 0);                           
            }
        }  
        public void mouseEntered(MouseEvent e)
        {   if (transPlaneButton.enabled)
            {   lastHelpMessage = owner.helpBar.text;                
                //owner.helpBar.setText(owner.tt("translatePlaneText"));
                owner.helpBar.setMessage(owner.tt("translatePlaneText"),
                                         transPlaneButton.getLocation().x + buttonWidth / 2);
                
            }
        }    
        public void mouseExited(MouseEvent e)
        {   if (transPlaneButton.enabled)
            {   owner.helpBar.setMessage(null, 0);            
                owner.helpBar.setText(lastHelpMessage);
            }
        }    
    }    

    class ShowCutML extends MouseAdapter
    {   String lastHelpMessage;
        public void mousePressed(MouseEvent e)
        {   if (showCutButton.enabled)
            {   // cut visible
                if (owner.drawingPanel.showCut)
                {   owner.drawingPanel.showCut(0, false);
                    showCutButton.setImage(owner.showCutImage);
                    owner.helpBar.setMessage(null, 0);                                         
//                    owner.helpBar.setText(owner.tt("showCutText"));                    
                }
                else
                {   // still choosing the plane
                    if (showCutButton.pressed)
                    {   owner.drawingPanel.showCut(0, false);
                    }
                    else
                    {
                        owner.drawingPanel.showCut(0, true);
//                        lastHelpMessage = owner.helpBar.text;                                
                        //showCutButton.setImage(owner.hideCutImage);
    //                    owner.helpBar.setText(owner.tt("hideCutText"));                    
                        owner.helpBar.setMessage(null, 0);                     
                    }
                }    
                lastHelpMessage = owner.helpBar.text;                
            }
        }  
        public void mouseEntered(MouseEvent e)
        {   if (showCutButton.enabled)
            {   lastHelpMessage = owner.helpBar.text;
                // cut visible
                if (owner.drawingPanel.showCut)
                {   
                    //owner.helpBar.setText(owner.tt("hideCutText"));
                    owner.helpBar.setMessage(owner.tt("hideCutText"),
                                             showCutButton.getLocation().x + buttonWidth / 2);                    
                }
                else
                {   if (showCutButton.pressed)
                    {   // geen message, er wordt nog een vlak gekozen
                    }
                    else // knop is inert
                    {
                        //owner.helpBar.setText(owner.tt("showCutText"));
                        owner.helpBar.setMessage(owner.tt("showCutText"),
                                             showCutButton.getLocation().x + buttonWidth / 2);
                    }
                }
            }
        }    
        public void mouseExited(MouseEvent e)
        {   if (showCutButton.enabled)
            {   owner.helpBar.setMessage(null, 0);                     
                owner.helpBar.setText(lastHelpMessage);
            }
        }    
    }    
    class CutML extends MouseAdapter
    {   String lastHelpMessage;
        public void mousePressed(MouseEvent e)
        {   if (cutButton.enabled)
            {
                if (owner.drawingPanel.mouseMode != DrawingPanel.CUTOBJECT)
                {   owner.drawingPanel.cutObject(0, true);
                    //cutButton.setImage(owner.glueImage);
                }
                else
                {   owner.drawingPanel.cutObject(0, false);
                    cutButton.setImage(owner.cutImage);
                }    
                lastHelpMessage = owner.helpBar.text;                
                owner.helpBar.setMessage(null, 0);                          
            }
        }  
        public void mouseEntered(MouseEvent e)
        {   if (cutButton.enabled)
            {   lastHelpMessage = owner.helpBar.text;
                if (owner.drawingPanel.mouseMode != DrawingPanel.CUTOBJECT)
                {   //owner.helpBar.setText(owner.tt("cutFigureText"));
                    owner.helpBar.setMessage(owner.tt("cutFigureText"),
                                             cutButton.getLocation().x + buttonWidth / 2);
                
                }
                else
                {   if (owner.drawingPanel.planeChoosen == null)
                    {    //owner.helpBar.setText(owner.tt("cutFigureText"));                
                    
                    }
                    else
                    {
                        //owner.helpBar.setText(owner.tt("glueFigureText"));
                        owner.helpBar.setMessage(owner.tt("glueFigureText"),
                                                 cutButton.getLocation().x + buttonWidth / 2);
                        
                    }    
                }
            }
        }    
        public void mouseExited(MouseEvent e)
        {   if (cutButton.enabled)
            {   owner.helpBar.setMessage(null, 0);          
                owner.helpBar.setText(lastHelpMessage);
            }
        }    
    }    

    
    class UndoML extends MouseAdapter
    {   // remember last message
        String lastHelpMessage;
        public void mousePressed(MouseEvent e)
        {   if (undoButton.enabled)
            {   owner.drawingPanel.undo();    
                lastHelpMessage = owner.helpBar.text;       
                owner.helpBar.setMessage(null, 0);          
            }
        }  
        public void mouseEntered(MouseEvent e)
        {   if (undoButton.enabled)
            {   lastHelpMessage = owner.helpBar.text;
                //owner.helpBar.setText(owner.tt("undoText"));
                owner.helpBar.setMessage(owner.tt("undoText"),
                                         undoButton.getLocation().x + buttonWidth / 2);
                
            }
        }    
        public void mouseExited(MouseEvent e)
        {   if (undoButton.enabled)
            {   owner.helpBar.setMessage(null, 0);          
                owner.helpBar.setText(lastHelpMessage);
            }    
        }    
    }    
    
    class RedoML extends MouseAdapter
    {   // remember last message
        String lastHelpMessage;
        public void mousePressed(MouseEvent e)
        {   if (redoButton.enabled)
            {   owner.drawingPanel.redo();    
                lastHelpMessage = owner.helpBar.text;       
                owner.helpBar.setMessage(null, 0);          
            }
        }  
        public void mouseEntered(MouseEvent e)
        {   if (redoButton.enabled)
            {   lastHelpMessage = owner.helpBar.text;
                //owner.helpBar.setText(owner.tt("redoText"));
                owner.helpBar.setMessage(owner.tt("redoText"),
                                         redoButton.getLocation().x + buttonWidth / 2);
                
            }
        }    
        public void mouseExited(MouseEvent e)
        {   if (redoButton.enabled)
            {   owner.helpBar.setMessage(null, 0);          
                owner.helpBar.setText(lastHelpMessage);
            }    
        }    
    }    
    
    
} // class TopToolBar

