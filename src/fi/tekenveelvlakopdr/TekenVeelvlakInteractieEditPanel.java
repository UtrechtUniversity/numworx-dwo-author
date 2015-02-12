package fi.tekenveelvlakopdr;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Hashtable;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;

import fi.beans.wiskopdrbeans.InteractieEditPanel;

public class TekenVeelvlakInteractieEditPanel extends JPanel implements InteractieEditPanel, ActionListener, ItemListener
{
	int editWidth = 190;
	int editHeight = 450; 
	int tvipBreedte = 500; // startbreedte tvip
	int tvipHoogte = 450; // starthoogte tvip
	
	Font theFont;
	FontMetrics theFM;
	Font theBoldFont;
	FontMetrics theBoldFM;
	
	int offSet = 9;
	boolean componentsCreated = false;
	
	boolean noSetBounds = false;	

	protected TekenVeelvlakInteractiePanel tvip;
	
    private JCheckBox viewerOnlyCB;
    private JRadioButton moveableRB, frontViewRB, backViewRB, topViewRB, bottomViewRB, leftViewRB, rightViewRB;
    ButtonGroup viewGroup;
    
    private JCheckBox profilesOnlyCB;
    
    private JLabel hulppuntenLabel;
    private JTextField hulppuntenTF;
    
    JCheckBox frontArrowCB;
    
    JCheckBox vlakkenKleurenCB;
    JRadioButton profielenKleurenRB, viewerKleurenRB;
    ButtonGroup kleurGroup;
    
    
    public TekenVeelvlakInteractieEditPanel()
    {
        setLayout(null);
        
        tvip = new TekenVeelvlakInteractiePanel();
        add(tvip);
        tvip.editMode = this;
        
		theFont = new Font("Dialog", Font.PLAIN, 12);
		theFM = getFontMetrics(theFont);
		theBoldFont = new Font("Dialog", Font.BOLD, 12);
		theBoldFM = getFontMetrics(theBoldFont);
		
		int width = editWidth - 2 * offSet;
		int height = 3 * theFM.getHeight() / 2;
		int currentX = tvip.getSize().width + offSet;
		int currentY = offSet;

        hulppuntenLabel = new JLabel(TekenVeelvlakOpdr.rb.getString("hulpPuntenCBLabel"));
        hulppuntenLabel.setBounds(currentX,currentY,width,height);
		add(hulppuntenLabel);
		
		currentY += height + offSet / 3;
		
		hulppuntenTF = new JTextField("0");
		hulppuntenTF.setBounds(currentX+2*offSet,currentY,width/2,height);
		hulppuntenTF.addActionListener(this);
		//hulppuntenTF.addFocusListener(this);
		add(hulppuntenTF);
		
		currentY += height + offSet;

		frontArrowCB = new JCheckBox(TekenVeelvlakOpdr.rb.getString("vooraanzichtPijlCBLabel"));
		frontArrowCB.setBounds(currentX,currentY,width,height);
		frontArrowCB.setOpaque(false);
		frontArrowCB.setSelected(false);
        add(frontArrowCB);
        frontArrowCB.addItemListener(this);
        
		currentY += height + offSet;
		
        viewerOnlyCB = new JCheckBox(TekenVeelvlakOpdr.rb.getString("alleenViewerCBLabel"));
        viewerOnlyCB.setBounds(currentX,currentY,width,height);
        viewerOnlyCB.setOpaque(false);
        add(viewerOnlyCB);
        viewerOnlyCB.addItemListener(this);
        //viewerOnlyCB.addActionListener(this);
        
        currentY += height; // + offSet/2;
        
        viewGroup = new ButtonGroup();
        
        moveableRB = new JRadioButton(TekenVeelvlakOpdr.rb.getString("draaibaarRBLabel"),true);
        moveableRB.setBounds(currentX,currentY,width,height);
        moveableRB.setOpaque(false);
        add(moveableRB);
        moveableRB.addItemListener(this);
        viewGroup.add(moveableRB);
        
        currentY += height; // + offSet/4;
        
        frontViewRB = new JRadioButton(TekenVeelvlakOpdr.rb.getString("vooraanzichtRBLabel"),true);
        frontViewRB.setBounds(currentX,currentY,width,height);
        frontViewRB.setOpaque(false);
        add(frontViewRB);
        frontViewRB.addItemListener(this);
        viewGroup.add(frontViewRB);
        
        currentY += height; // + offSet/4;

        backViewRB = new JRadioButton(TekenVeelvlakOpdr.rb.getString("achteraanzichtRBLabel"),true);
        backViewRB.setBounds(currentX,currentY,width,height);
        backViewRB.setOpaque(false);
        add(backViewRB);
        backViewRB.addItemListener(this);
        viewGroup.add(backViewRB);
        
        currentY += height; // + offSet/2;
        
        topViewRB = new JRadioButton(TekenVeelvlakOpdr.rb.getString("bovenaanzichtRBLabel"),true);
        topViewRB.setBounds(currentX,currentY,width,height);
        topViewRB.setOpaque(false);
        add(topViewRB);
        topViewRB.addItemListener(this);
        viewGroup.add(topViewRB);
        
        currentY += height; // + offSet/2;
                
        bottomViewRB = new JRadioButton(TekenVeelvlakOpdr.rb.getString("onderaanzichtRBLabel"),true);
        bottomViewRB.setBounds(currentX,currentY,width,height);
        bottomViewRB.setOpaque(false);
        add(bottomViewRB);
        bottomViewRB.addItemListener(this);
        viewGroup.add(bottomViewRB);
        
        currentY += height; // + offSet/2;

        leftViewRB = new JRadioButton(TekenVeelvlakOpdr.rb.getString("linkeraanzichtRBLabel"),true);
        leftViewRB.setBounds(currentX,currentY,width,height);
        leftViewRB.setOpaque(false);
        add(leftViewRB);
        leftViewRB.addItemListener(this);
        viewGroup.add(leftViewRB);
        
        currentY += height; // + offSet/2;
        
        rightViewRB = new JRadioButton(TekenVeelvlakOpdr.rb.getString("rechteraanzichtRBLabel"),true);
        rightViewRB.setBounds(currentX,currentY,width,height);
        rightViewRB.setOpaque(false);
        add(rightViewRB);
        rightViewRB.addItemListener(this);
        viewGroup.add(rightViewRB);
        
        currentY += height + offSet;
        
        zetViewerOptiesEnabled(false);

        profilesOnlyCB = new JCheckBox(TekenVeelvlakOpdr.rb.getString("alleenProfielenCBLabel"));
        profilesOnlyCB.setBounds(currentX,currentY,width,height);
        profilesOnlyCB.setOpaque(false);
        add(profilesOnlyCB);
        profilesOnlyCB.addItemListener(this);
        //profilesOnlyCB.addActionListener(this);
        
        currentY += height + offSet;
        
        vlakkenKleurenCB = new JCheckBox(TekenVeelvlakOpdr.rb.getString("vlakkenKleurenCBLabel"));
        vlakkenKleurenCB.setBounds(currentX,currentY,width,height);
        vlakkenKleurenCB.setOpaque(false);
        add(vlakkenKleurenCB);
        //vlakkenKleurenCB.addItemListener(this);
        vlakkenKleurenCB.addActionListener(this);
        
        currentY += height; // + offSet;

        kleurGroup = new ButtonGroup();
        
        profielenKleurenRB = new JRadioButton(TekenVeelvlakOpdr.rb.getString("inProfielenRBLabel"),true);
        profielenKleurenRB.setBounds(currentX,currentY,width,height);
        profielenKleurenRB.setOpaque(false);
        add(profielenKleurenRB);
        profielenKleurenRB.addItemListener(this);
        kleurGroup.add(profielenKleurenRB);
        
        currentY += height; // + offSet/4;

        viewerKleurenRB = new JRadioButton(TekenVeelvlakOpdr.rb.getString("inFiguurRBLabel"),false);
        viewerKleurenRB.setBounds(currentX,currentY,width,height);
        viewerKleurenRB.setOpaque(false);
        add(viewerKleurenRB);
        viewerKleurenRB.addItemListener(this);
        kleurGroup.add(viewerKleurenRB);
        
        currentY += height; // + offSet/4;
        
        zetVlakkenKleurenOptiesEnabled(false);
        
		componentsCreated = true;
    }
    
    public void plaatsComponenten()
	{
		if (componentsCreated)
		{
			hulppuntenLabel.setLocation(tvip.getSize().width + offSet, hulppuntenLabel.getLocation().y);
			hulppuntenTF.setLocation(tvip.getSize().width + 3*offSet, hulppuntenTF.getLocation().y);
			
			frontArrowCB.setLocation(tvip.getSize().width + offSet, frontArrowCB.getLocation().y);
			
			viewerOnlyCB.setLocation(tvip.getSize().width + offSet, viewerOnlyCB.getLocation().y);
			moveableRB.setLocation(tvip.getSize().width + offSet, moveableRB.getLocation().y);
			frontViewRB.setLocation(tvip.getSize().width + offSet, frontViewRB.getLocation().y);
			backViewRB.setLocation(tvip.getSize().width + offSet, backViewRB.getLocation().y);
			topViewRB.setLocation(tvip.getSize().width + offSet, topViewRB.getLocation().y);
			bottomViewRB.setLocation(tvip.getSize().width + offSet, bottomViewRB.getLocation().y);
			leftViewRB.setLocation(tvip.getSize().width + offSet, leftViewRB.getLocation().y);
			rightViewRB.setLocation(tvip.getSize().width + offSet, rightViewRB.getLocation().y);
			
			profilesOnlyCB.setLocation(tvip.getSize().width + offSet, profilesOnlyCB.getLocation().y);
			
			vlakkenKleurenCB.setLocation(tvip.getSize().width + offSet, vlakkenKleurenCB.getLocation().y);
			profielenKleurenRB.setLocation(tvip.getSize().width + offSet, profielenKleurenRB.getLocation().y);
			viewerKleurenRB.setLocation(tvip.getSize().width + offSet, viewerKleurenRB.getLocation().y);
			
		}
	}
    
    
    public void itemStateChanged(ItemEvent e)
    {

    	if (e.getSource() == frontArrowCB)
    	{
    		tvip.toonVooraanzichtPijl(frontArrowCB.isSelected());
    	}
    	else if (e.getSource() == viewerOnlyCB)
    	{
    		tvip.setViewerOnly(viewerOnlyCB.isSelected());
    		zetViewerOptiesEnabled(viewerOnlyCB.isSelected());
    		if (viewerOnlyCB.isSelected())
    			profilesOnlyCB.setSelected(false);
    	}
    	else if (e.getSource() == moveableRB)
    	{
    		if (moveableRB.isSelected()) 
    			tvip.zetViewerPosition(TekenVeelvlakInteractiePanel.MOVEABLE);
    	}
    	else if (e.getSource() == frontViewRB)
    	{
    		if (frontViewRB.isSelected()) 
    			tvip.zetViewerPosition(TekenVeelvlakInteractiePanel.FRONTVIEW);
    	}
    	else if (e.getSource() == backViewRB)
    	{
    		if (backViewRB.isSelected()) 
    			tvip.zetViewerPosition(TekenVeelvlakInteractiePanel.BACKVIEW);
    	}
    	else if (e.getSource() == topViewRB)
    	{
    		if (topViewRB.isSelected()) 
    			tvip.zetViewerPosition(TekenVeelvlakInteractiePanel.TOPVIEW);
    	}
    	else if (e.getSource() == bottomViewRB)
    	{
    		if (bottomViewRB.isSelected()) 
    			tvip.zetViewerPosition(TekenVeelvlakInteractiePanel.BOTTOMVIEW);
    	}
    	else if (e.getSource() == leftViewRB)
    	{
    		if (leftViewRB.isSelected()) 
    			tvip.zetViewerPosition(TekenVeelvlakInteractiePanel.LEFTVIEW);
    	}
    	else if (e.getSource() == rightViewRB)
    	{
    		if (rightViewRB.isSelected()) 
    			tvip.zetViewerPosition(TekenVeelvlakInteractiePanel.RIGHTVIEW);
    	}
    	else if (e.getSource() == profilesOnlyCB)
    	{
    		tvip.setProfilesOnly(profilesOnlyCB.isSelected());
    		zetViewerOptiesEnabled(false);
    		if (profilesOnlyCB.isSelected())
    			viewerOnlyCB.setSelected(false);
    		
    	}
    	else if (e.getSource() == vlakkenKleurenCB)
    	{
    		tvip.zetVlakkenKleurenOptie(vlakkenKleurenCB.isSelected());
    		zetVlakkenKleurenOptiesEnabled(vlakkenKleurenCB.isSelected());
    		
    		if (vlakkenKleurenCB.isSelected())
    		{	
    			if (viewerOnlyCB.isSelected())
    			{	viewerOnlyCB.setSelected(false);
    				tvip.setViewerOnly(false);
    				zetViewerOptiesEnabled(false);
    			}
    			//viewerOnlyCB.setEnabled(false);
    			if (profilesOnlyCB.isSelected())
    			{	profilesOnlyCB.setSelected(false);
    				tvip.setProfilesOnly(false);
    			}
    			//profilesOnlyCB.setEnabled(false);
    		}
    		else
    		{	//profilesOnlyCB.setEnabled(true);
    			//viewerOnlyCB.setEnabled(true);
    		}

    	}
    	else if (e.getSource() == profielenKleurenRB)
    	{
    		tvip.zetProfielenKleurenOptie(profielenKleurenRB.isSelected());
    	}
    	else if (e.getSource() == viewerKleurenRB)
    	{
    		tvip.zetProfielenKleurenOptie(!viewerKleurenRB.isSelected());
    	}
    	
    	
    }
    
    public void actionPerformed(ActionEvent e) 
    {
    	if (e.getSource() == hulppuntenTF)
    	{
            int aantalHulppunten = 0;
            try
            {
            	aantalHulppunten = Integer.parseInt(hulppuntenTF.getText());
            }
            catch(Exception ex)
            {
            	aantalHulppunten = 0;
            }
            hulppuntenTF.setText("" + aantalHulppunten);
            tvip.zetAantalHulppunten(aantalHulppunten);

    	}
    	else if (e.getSource() == viewerOnlyCB)
    	{
    		tvip.setViewerOnly(viewerOnlyCB.isSelected());
    		zetViewerOptiesEnabled(viewerOnlyCB.isSelected());
    		if (viewerOnlyCB.isSelected())
    			profilesOnlyCB.setSelected(false);
    	}
    	else if (e.getSource() == profilesOnlyCB)
    	{
    		tvip.setProfilesOnly(profilesOnlyCB.isSelected());
    		zetViewerOptiesEnabled(false);
    		if (profilesOnlyCB.isSelected())
    			viewerOnlyCB.setSelected(false);
    		
    	}
    	else if (e.getSource() == vlakkenKleurenCB)
    	{
    		tvip.zetVlakkenKleurenOptie(vlakkenKleurenCB.isSelected());
    		zetVlakkenKleurenOptiesEnabled(vlakkenKleurenCB.isSelected());
    		
    		if (vlakkenKleurenCB.isSelected())
    		{	
    			if (viewerOnlyCB.isSelected())
    			{	viewerOnlyCB.setSelected(false);
    				tvip.setViewerOnly(false);
    				zetViewerOptiesEnabled(false);
    			}
    			//viewerOnlyCB.setEnabled(false);
    			if (profilesOnlyCB.isSelected())
    			{	profilesOnlyCB.setSelected(false);
    				tvip.setProfilesOnly(false);
    			}
    			//profilesOnlyCB.setEnabled(false);
    		}
    		else
    		{	//profilesOnlyCB.setEnabled(true);
    			//viewerOnlyCB.setEnabled(true);
    		}

    	}
 
    }

    public void zetViewerOptiesEnabled(boolean b)
    {
    	moveableRB.setEnabled(b);
		frontViewRB.setEnabled(b);
		backViewRB.setEnabled(b);
		topViewRB.setEnabled(b);
		bottomViewRB.setEnabled(b);
		leftViewRB.setEnabled(b);
		rightViewRB.setEnabled(b);
    	
    }

    public void zetVlakkenKleurenOptiesEnabled(boolean b)
    {
    	profielenKleurenRB.setEnabled(b);
		viewerKleurenRB.setEnabled(b);
    	
    }
    
    public void addActionListener(ActionListener al) 
    {
        // TODO Auto-generated method stub
        
    }

    
    public Hashtable getEditState() 
    {
    	
//System.out.println("tviep getEditState");
/*
        Hashtable tvState = new Hashtable();
        boolean viewerOnly = false; 
        boolean moveable = true;
        int basisFiguur = 1;
        int aantalHulppunten = 0;
        
        tvState = tekenVeelvlak.getState();
        viewerOnly = viewerOnlyCB.isSelected();
        moveable = moveableCB.isSelected();
        basisFiguur = tekenVeelvlak.geefBasisFiguur();
        try
        {
        	aantalHulppunten = Integer.parseInt(hulppuntenTF.getText());
        }
        catch(Exception e)
        {
        	aantalHulppunten = 0;
        }
*/        
        Hashtable h = tvip.getEditState();
/*        
        h.put("tvState", tvState);
        h.put("viewerOnly", new Boolean(viewerOnly));
        h.put("moveable", new Boolean(moveable));
        h.put("basisFiguur", new Integer(basisFiguur));
        h.put("aantalHulppunten", new Integer(aantalHulppunten));
*/        
		h.put("tvipBreedte", new Integer(tvipBreedte));
		h.put("tvipHoogte", new Integer(tvipHoogte));
		h.put("scoreMax", new Integer(10));
        
        return h;
    }

    
    public void setBounds(int x, int y, int b, int h) 
    {
    	if ((x < 0) || (y < 0) || (h < 5)) return;    	

//System.out.println("tviep setBounds " + x + " " + y + " " + b + " " + h);

        //super.setBounds(x,y,b,h);
        
        super.setBounds(x, y, tvipBreedte + editWidth, Math.max(tvipHoogte, editHeight));
		
//System.out.println("tviep setBounds " + x + " " + y + " " + (tvipBreedte + editWidth) + " " + 
//      					Math.max(tvipHoogte, editHeight));

      	if (tvip != null)
      		tvip.setBounds(0, 0, tvipBreedte, tvipHoogte);
      	
      	plaatsComponenten();
        
    }

    
    public void setEditState(Hashtable h) 
    {
    	
//System.out.println("tviep setEditState");    	

        boolean viewerOnly = false; 
        boolean profilesOnly = false;
        int viewerPosition = 0;
        int aantalHulppunten = 0;
        
        boolean vlakkenKleurenOptie = false;
        boolean profielenKleurenOptie = true;
        boolean viewerKleurenOptie = false;
        
        if(h.containsKey("viewerOnly"))
        	viewerOnly = ((Boolean)h.get("viewerOnly")).booleanValue();
        if(h.containsKey("profilesOnly"))
        	profilesOnly = ((Boolean)h.get("profilesOnly")).booleanValue();

//System.out.println("profilesOnly = " + profilesOnly);

        if(h.containsKey("viewerPosition"))
        	viewerPosition = ((Integer) h.get("viewerPosition")).intValue();
        
        //if(h.containsKey("basisFiguur"))
        //	basisFiguur = ((Integer)h.get("basisFiguur")).intValue();
        if(h.containsKey("aantalHulppunten"))
        	aantalHulppunten = ((Integer)h.get("aantalHulppunten")).intValue();

        if (h.containsKey("vlakkenKleurenOptie"))
        	vlakkenKleurenOptie = ((Boolean) h.get("vlakkenKleurenOptie")).booleanValue();
        if (h.containsKey("profielenKleurenOptie"))
        	profielenKleurenOptie = ((Boolean) h.get("profielenKleurenOptie")).booleanValue();
        if (h.containsKey("viewerKleurenOptie"))
        	viewerKleurenOptie = ((Boolean) h.get("viewerKleurenOptie")).booleanValue();
        
	    viewerOnlyCB.setSelected(viewerOnly);
	    profilesOnlyCB.setSelected(profilesOnly);
	    
	    vlakkenKleurenCB.setSelected(vlakkenKleurenOptie);
	    profielenKleurenRB.setSelected(profielenKleurenOptie);
	    viewerKleurenRB.setSelected(viewerKleurenOptie);
	    
	    zetVlakkenKleurenOptiesEnabled(vlakkenKleurenOptie);
	    	    
	    if (viewerPosition == TekenVeelvlakInteractiePanel.MOVEABLE)
	    	moveableRB.setSelected(true);
	    else if (viewerPosition == TekenVeelvlakInteractiePanel.FRONTVIEW)
	    	frontViewRB.setSelected(true);
	    else if (viewerPosition == TekenVeelvlakInteractiePanel.BACKVIEW)
	    	backViewRB.setSelected(true);
	    else if (viewerPosition == TekenVeelvlakInteractiePanel.TOPVIEW)
	    	topViewRB.setSelected(true);
	    else if (viewerPosition == TekenVeelvlakInteractiePanel.BOTTOMVIEW)
	    	bottomViewRB.setSelected(true);
	    else if (viewerPosition == TekenVeelvlakInteractiePanel.LEFTVIEW)
	    	leftViewRB.setSelected(true);
	    else if (viewerPosition == TekenVeelvlakInteractiePanel.RIGHTVIEW)
	    	rightViewRB.setSelected(true);
	    	
	    zetViewerOptiesEnabled(viewerOnly);
	    		
	    hulppuntenTF.setText("" + aantalHulppunten);
	    
	    setBounds(getLocation().x, getLocation().y, tvipBreedte + editWidth, Math.max(tvipHoogte, editHeight));
		
		// HIER !!
		tvip.setEditState(h);	    
    }

    
    public void start() {
        // TODO Auto-generated method stub
        
    }

    
    public void stop() {
        // TODO Auto-generated method stub
        
    }

    
    public void wis() {
        // TODO Auto-generated method stub
        
    }

    
    public void zetBreedte(int b) 
    {
		tvipBreedte = b;
		
		setBounds(getLocation().x, getLocation().y, tvipBreedte + editWidth, Math.max(tvipHoogte, editHeight));		
		
		plaatsComponenten();
    }

    
    public void zetHoogte(int h) 
    {
		tvipHoogte = h;
		
		setBounds(getLocation().x, getLocation().y, tvipBreedte + editWidth, Math.max(tvipHoogte, editHeight));		

        
    }

    
    public void zetMode(int mode) {
        // TODO Auto-generated method stub
        
    }

}
