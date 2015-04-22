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
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

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
    JRadioButton teacherViewRB;
    ButtonGroup viewGroup;
    
    private JCheckBox profilesOnlyCB;
    
    private JLabel hulppuntenLabel;
    private JTextField hulppuntenTF;
    
    JCheckBox frontArrowCB;
    
    JCheckBox vlakkenKleurenCB;
    
    JTabbedPane tabbedPane;
	
	JPanel tekenVVOptiesPanel, nakijkOptiesPanel;
    
	JCheckBox kijkDraaihoekNaCB;
	
	JRadioButton dezeDraaihoekRB, voorkantRB, achterkantRB, bovenkantRB, onderkantRB, linkerkantRB, rechterkantRB;
    ButtonGroup kijkDraaihoekNaGroup;
    
	JCheckBox kijkVlakkenNaCB;
	JLabel kijkVlakkenNaLabel;
	
	//JRadioButton profielenKleurenRB, viewerKleurenRB;
    //ButtonGroup kleurGroup;
    
	double laatsteDraaiX, laatsteDraaiY;
	
	double tvipDraaiX, tvipDraaiY;
	boolean tvipViewerOnly;
	boolean tvipProfilesOnly;
	int tvipViewerPosition;
    
    public TekenVeelvlakInteractieEditPanel()
    {
        setLayout(null);
        
        tvip = new TekenVeelvlakInteractiePanel();
        add(tvip);
        tvip.editMode = this;
        
        tabbedPane = new JTabbedPane();
		
		tekenVVOptiesPanel = new JPanel();
		tekenVVOptiesPanel.setLayout(null);
		tabbedPane.addTab(TekenVeelvlakOpdr.rb.getString("tekenVVOptiesLabel"), tekenVVOptiesPanel);
		
		nakijkOptiesPanel = new JPanel();
		nakijkOptiesPanel.setLayout(null);
		tabbedPane.addTab(TekenVeelvlakOpdr.rb.getString("nakijkOptiesLabel"), nakijkOptiesPanel);
		
		tabbedPane.setEnabledAt(1,false);
		
		tabbedPane.setBounds(getSize().width - editWidth, 0, editWidth, getSize().height);
		add(tabbedPane);
		
		tabbedPane.addChangeListener(new TabbedPaneCL());
		
		theFont = new Font("Dialog", Font.PLAIN, 12);
		theFM = getFontMetrics(theFont);
		theBoldFont = new Font("Dialog", Font.BOLD, 12);
		theBoldFM = getFontMetrics(theBoldFont);
		
		int width = editWidth - 2 * offSet;
		int height = 3 * theFM.getHeight() / 2;
		//int currentX = tvip.getSize().width + offSet;
		int currentX = offSet;
		int currentY = offSet;

        hulppuntenLabel = new JLabel(TekenVeelvlakOpdr.rb.getString("hulpPuntenCBLabel"));
        hulppuntenLabel.setBounds(currentX,currentY,width,height);
		tekenVVOptiesPanel.add(hulppuntenLabel);
		
		currentY += height + offSet / 3;
		
		hulppuntenTF = new JTextField("0");
		hulppuntenTF.setBounds(currentX+2*offSet,currentY,width/2,height);
		hulppuntenTF.addActionListener(this);
		//hulppuntenTF.addFocusListener(this);
		tekenVVOptiesPanel.add(hulppuntenTF);
		
		currentY += height + offSet;

		frontArrowCB = new JCheckBox(TekenVeelvlakOpdr.rb.getString("vooraanzichtPijlCBLabel"));
		frontArrowCB.setBounds(currentX,currentY,width,height);
		frontArrowCB.setOpaque(false);
		frontArrowCB.setSelected(false);
		tekenVVOptiesPanel.add(frontArrowCB);
		//frontArrowCB.addItemListener(this);
		frontArrowCB.addActionListener(this);
        
		currentY += height + offSet;
		
        viewerOnlyCB = new JCheckBox(TekenVeelvlakOpdr.rb.getString("alleenViewerCBLabel"));
        viewerOnlyCB.setBounds(currentX,currentY,width,height);
        viewerOnlyCB.setOpaque(false);
        tekenVVOptiesPanel.add(viewerOnlyCB);
        //viewerOnlyCB.addItemListener(this);
        viewerOnlyCB.addActionListener(this);
        
        currentY += height; // + offSet/2;
        
        viewGroup = new ButtonGroup();
        
        moveableRB = new JRadioButton(TekenVeelvlakOpdr.rb.getString("draaibaarRBLabel"),true);
        moveableRB.setBounds(currentX,currentY,width,height);
        moveableRB.setOpaque(false);
        tekenVVOptiesPanel.add(moveableRB);
        moveableRB.addItemListener(this);
        viewGroup.add(moveableRB);
        
        currentY += height; // + offSet/4;
        
        
        teacherViewRB = new JRadioButton(TekenVeelvlakOpdr.rb.getString("docentDraaihoekRBLabel"),false);
        teacherViewRB.setBounds(currentX,currentY,width,height);
        teacherViewRB.setOpaque(false);
//        tekenVVOptiesPanel.add(teacherViewRB);
//        teacherViewRB.addItemListener(this);
//        viewGroup.add(teacherViewRB);
        
//        currentY += height + offSet;

        
        frontViewRB = new JRadioButton(TekenVeelvlakOpdr.rb.getString("vooraanzichtRBLabel"),true);
        frontViewRB.setBounds(currentX,currentY,width,height);
        frontViewRB.setOpaque(false);
        tekenVVOptiesPanel.add(frontViewRB);
        frontViewRB.addItemListener(this);
        viewGroup.add(frontViewRB);
        
        currentY += height; // + offSet/4;

        backViewRB = new JRadioButton(TekenVeelvlakOpdr.rb.getString("achteraanzichtRBLabel"),true);
        backViewRB.setBounds(currentX,currentY,width,height);
        backViewRB.setOpaque(false);
        tekenVVOptiesPanel.add(backViewRB);
        backViewRB.addItemListener(this);
        viewGroup.add(backViewRB);
        
        currentY += height; // + offSet/2;
        
        topViewRB = new JRadioButton(TekenVeelvlakOpdr.rb.getString("bovenaanzichtRBLabel"),true);
        topViewRB.setBounds(currentX,currentY,width,height);
        topViewRB.setOpaque(false);
        tekenVVOptiesPanel.add(topViewRB);
        topViewRB.addItemListener(this);
        viewGroup.add(topViewRB);
        
        currentY += height; // + offSet/2;
                
        bottomViewRB = new JRadioButton(TekenVeelvlakOpdr.rb.getString("onderaanzichtRBLabel"),true);
        bottomViewRB.setBounds(currentX,currentY,width,height);
        bottomViewRB.setOpaque(false);
        tekenVVOptiesPanel.add(bottomViewRB);
        bottomViewRB.addItemListener(this);
        viewGroup.add(bottomViewRB);
        
        currentY += height; // + offSet/2;

        leftViewRB = new JRadioButton(TekenVeelvlakOpdr.rb.getString("linkeraanzichtRBLabel"),true);
        leftViewRB.setBounds(currentX,currentY,width,height);
        leftViewRB.setOpaque(false);
        tekenVVOptiesPanel.add(leftViewRB);
        leftViewRB.addItemListener(this);
        viewGroup.add(leftViewRB);
        
        currentY += height; // + offSet/2;
        
        rightViewRB = new JRadioButton(TekenVeelvlakOpdr.rb.getString("rechteraanzichtRBLabel"),true);
        rightViewRB.setBounds(currentX,currentY,width,height);
        rightViewRB.setOpaque(false);
        tekenVVOptiesPanel.add(rightViewRB);
        rightViewRB.addItemListener(this);
        viewGroup.add(rightViewRB);
        
        currentY += height + offSet;

        
        zetViewerOptiesEnabled(false);

        profilesOnlyCB = new JCheckBox(TekenVeelvlakOpdr.rb.getString("alleenProfielenCBLabel"));
        profilesOnlyCB.setBounds(currentX,currentY,width,height);
        profilesOnlyCB.setOpaque(false);
        tekenVVOptiesPanel.add(profilesOnlyCB);
        //profilesOnlyCB.addItemListener(this);
        profilesOnlyCB.addActionListener(this);
        
        currentY += height + 2 * offSet;
        
        vlakkenKleurenCB = new JCheckBox(TekenVeelvlakOpdr.rb.getString("vlakkenKleurenCBLabel"));
        vlakkenKleurenCB.setBounds(currentX,currentY,width,height);
        vlakkenKleurenCB.setOpaque(false);
        tekenVVOptiesPanel.add(vlakkenKleurenCB);
        vlakkenKleurenCB.addItemListener(this);
        //vlakkenKleurenCB.addActionListener(this);
        vlakkenKleurenCB.setEnabled(false);
        
        currentY += height; // + offSet;

		currentX = offSet;
		currentY = offSet;
        
        kijkDraaihoekNaCB = new JCheckBox(TekenVeelvlakOpdr.rb.getString("kijkDraaihoekNaCBLabel"));
        kijkDraaihoekNaCB.setBounds(currentX,currentY,width,height);
        kijkDraaihoekNaCB.setOpaque(false);
        nakijkOptiesPanel.add(kijkDraaihoekNaCB);
        kijkDraaihoekNaCB.addItemListener(this);
        //kijkDraaihoekNaCB.addActionListener(this);

        currentY += height; // + offSet/4;
        
        kijkDraaihoekNaGroup = new ButtonGroup();
        
        dezeDraaihoekRB = new JRadioButton(TekenVeelvlakOpdr.rb.getString("dezeDraaihoekRBLabel"),false);
        dezeDraaihoekRB.setBounds(currentX,currentY,width,height);
        dezeDraaihoekRB.setOpaque(false);
        nakijkOptiesPanel.add(dezeDraaihoekRB);
        dezeDraaihoekRB.addItemListener(this);
        kijkDraaihoekNaGroup.add(dezeDraaihoekRB);
        
        currentY += height; // + offSet/4;
        
        voorkantRB = new JRadioButton(TekenVeelvlakOpdr.rb.getString("voorkantRBLabel"),false);
        voorkantRB.setBounds(currentX,currentY,width,height);
        voorkantRB.setOpaque(false);
        nakijkOptiesPanel.add(voorkantRB);
        voorkantRB.addItemListener(this);
        kijkDraaihoekNaGroup.add(voorkantRB);
        
        currentY += height; // + offSet/4;

        achterkantRB = new JRadioButton(TekenVeelvlakOpdr.rb.getString("achterkantRBLabel"),false);
        achterkantRB.setBounds(currentX,currentY,width,height);
        achterkantRB.setOpaque(false);
        nakijkOptiesPanel.add(achterkantRB);
        achterkantRB.addItemListener(this);
        kijkDraaihoekNaGroup.add(achterkantRB);
        
        currentY += height; // + offSet/2;
        
        bovenkantRB = new JRadioButton(TekenVeelvlakOpdr.rb.getString("bovenkantRBLabel"),false);
        bovenkantRB.setBounds(currentX,currentY,width,height);
        bovenkantRB.setOpaque(false);
        nakijkOptiesPanel.add(bovenkantRB);
        bovenkantRB.addItemListener(this);
        kijkDraaihoekNaGroup.add(bovenkantRB);
        
        currentY += height; // + offSet/2;
                
        onderkantRB = new JRadioButton(TekenVeelvlakOpdr.rb.getString("onderkantRBLabel"),false);
        onderkantRB.setBounds(currentX,currentY,width,height);
        onderkantRB.setOpaque(false);
        nakijkOptiesPanel.add(onderkantRB);
        onderkantRB.addItemListener(this);
        kijkDraaihoekNaGroup.add(onderkantRB);
        
        currentY += height; // + offSet/2;

        linkerkantRB = new JRadioButton(TekenVeelvlakOpdr.rb.getString("linkerkantRBLabel"),false);
        linkerkantRB.setBounds(currentX,currentY,width,height);
        linkerkantRB.setOpaque(false);
        nakijkOptiesPanel.add(linkerkantRB);
        linkerkantRB.addItemListener(this);
        kijkDraaihoekNaGroup.add(linkerkantRB);
        
        currentY += height; // + offSet/2;
        
        rechterkantRB = new JRadioButton(TekenVeelvlakOpdr.rb.getString("rechterkantRBLabel"),false);
        rechterkantRB.setBounds(currentX,currentY,width,height);
        rechterkantRB.setOpaque(false);
        nakijkOptiesPanel.add(rechterkantRB);
        rechterkantRB.addItemListener(this);
        kijkDraaihoekNaGroup.add(rechterkantRB);
        
        zetDraaihoekOptiesEnabled(false);
        
        currentY += height + 2 * offSet;

        kijkVlakkenNaCB = new JCheckBox(TekenVeelvlakOpdr.rb.getString("kijkVlakkenNaCBLabel"));
        kijkVlakkenNaCB.setBounds(currentX,currentY,width,height);
        kijkVlakkenNaCB.setOpaque(false);
        nakijkOptiesPanel.add(kijkVlakkenNaCB);
        kijkVlakkenNaCB.addItemListener(this);
        //kijkVlakkenNaCB.addActionListener(this);
        
        currentY += height - 5;
        
        kijkVlakkenNaLabel = new JLabel(TekenVeelvlakOpdr.rb.getString("kijkVlakkenNaCBLabel2"));
        kijkVlakkenNaLabel.setBounds(currentX + 2 * offSet + 2 ,currentY,width,height);
        kijkVlakkenNaLabel.setOpaque(false);
        nakijkOptiesPanel.add(kijkVlakkenNaLabel);
                
        currentY += height;
  
/*        
        kleurGroup = new ButtonGroup();
        
        profielenKleurenRB = new JRadioButton(TekenVeelvlakOpdr.rb.getString("inProfielenRBLabel"),false);
        profielenKleurenRB.setBounds(currentX,currentY,width,height);
        profielenKleurenRB.setOpaque(false);
        //nakijkOptiesPanel.add(profielenKleurenRB);
        //profielenKleurenRB.addItemListener(this);
        //kleurGroup.add(profielenKleurenRB);
        
        currentY += height; // + offSet/4;
*/        
/*
        viewerKleurenRB = new JRadioButton(TekenVeelvlakOpdr.rb.getString("inFiguurRBLabel"),true);
        viewerKleurenRB.setBounds(currentX,currentY,width,height);
        viewerKleurenRB.setOpaque(false);
        //nakijkOptiesPanel.add(viewerKleurenRB);
        //viewerKleurenRB.addItemListener(this);
        //kleurGroup.add(viewerKleurenRB);
        
        currentY += height + 4 * offSet;
*/        
        zetVlakkenKleurenEnabled(false);
        
		componentsCreated = true;
		
    }

    public void showNakijkOpties(boolean b)
    {
    	tabbedPane.setEnabledAt(1,b);
    	if (b)
    	{
    	}
    	else
    	{	kijkDraaihoekNaCB.setSelected(false);
    		kijkDraaihoekNaCB.setEnabled(false);
    		dezeDraaihoekRB.setSelected(true);
    		zetDraaihoekOptiesEnabled(false);
    		tvip.docentDraaihoekX = 1e-5d;
    		tvip.docentDraaihoekY = 1e-5d;
    		
    		kijkVlakkenNaCB.setSelected(false);
    		kijkVlakkenNaCB.setEnabled(false);
    		tvip.docentKleuren = null;
    		tvip.viewer.kijkNaPanel.setVisible(false);
    		tvip.vaktek.kijkNaPanel.setVisible(false);
    	
    	}
    }
    
    public void plaatsComponenten()
	{
		if (componentsCreated)
		{
/*			
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
*/	
			tabbedPane.setBounds(tvipBreedte, 0, editWidth, getSize().height);
		}
	}
    
    public void viewerMuisLos()
    {
    	if (kijkDraaihoekNaCB.isSelected() && dezeDraaihoekRB.isSelected())
    	{
    		laatsteDraaiX = tvip.viewer.geefDraaiX();
    		laatsteDraaiY = tvip.viewer.geefDraaiY();
    	}
    }
    
    public void itemStateChanged(ItemEvent e)
    {

//    	if (e.getSource() == frontArrowCB)
//   	{
//    		tvip.toonVooraanzichtPijl(frontArrowCB.isSelected());
//    	}
    	if (e.getSource() == viewerOnlyCB)
    	{
    		
//System.out.println("viewerOnlyCB item");

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
    	else if (e.getSource() == teacherViewRB)
    	{
    		if (teacherViewRB.isSelected()) 
    			tvip.zetViewerPosition(TekenVeelvlakInteractiePanel.TEACHER);
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
    		//zetVlakkenKleurenOptiesEnabled(vlakkenKleurenCB.isSelected());
    		zetVlakkenKleurenEnabled(vlakkenKleurenCB.isSelected());
    		
    		moveableRB.setSelected(true);
    		zetViewerOptiesEnabled(!vlakkenKleurenCB.isSelected() && viewerOnlyCB.isSelected());
    		   		
    		 
    		
    	}
    	else if (e.getSource() == kijkDraaihoekNaCB)
    	{
    		tvip.zetKijkDraaihoekNa(kijkDraaihoekNaCB.isSelected());
    		
    		if (kijkDraaihoekNaCB.isSelected())
    		{
    			zetDraaihoekOptiesEnabled(true);
    			kijkVlakkenNaCB.setSelected(false);
    			zetVlakkenKleurenOptiesEnabled(false);
    			tvip.viewer.kijkNaPanel.setVisible(true);
    			//tvip.vaktek.kijkNaPanel.setVisible(true);
    		}
    		else
    		{
    			zetDraaihoekOptiesEnabled(false);
    			tvip.viewer.kijkNaPanel.setVisible(false);
    			tvip.vaktek.kijkNaPanel.setVisible(false);
    		}
    	}
    	else if (e.getSource() == dezeDraaihoekRB)
    	{
    		if (dezeDraaihoekRB.isSelected())
    		{	
//System.out.println("dezeDraaihoekRB");    			
    			zetDocentDraaihoek(laatsteDraaiX, laatsteDraaiY);    		
    			tvip.viewer.zetAfstand(1000);
				tvip.viewer.zetSchaduw(true);
				tvip.viewer.tekenOpnieuw();
				tvip.viewer.muisAan = true;
    		}
    	}
    	else if (e.getSource() == voorkantRB)
    	{
    		if (voorkantRB.isSelected())
    		{	
//System.out.println("voorkantRB");
    			if (tvip.viewer.muisAan)
    			{	laatsteDraaiX = tvip.viewer.geefDraaiX();
    				laatsteDraaiY = tvip.viewer.geefDraaiY();
    			}
    			
    			zetDocentDraaihoek(0,0);
				tvip.viewer.zetAfstand(100000);
				tvip.viewer.zetSchaduw(false);
				tvip.viewer.tekenOpnieuw();
				tvip.viewer.muisAan = false;
    		}	
    	}
    	else if (e.getSource() == achterkantRB)
    	{
			if (tvip.viewer.muisAan)
			{	laatsteDraaiX = tvip.viewer.geefDraaiX();
				laatsteDraaiY = tvip.viewer.geefDraaiY();
			}

    		if (achterkantRB.isSelected())
    		{	zetDocentDraaihoek(0,180);
				tvip.viewer.zetAfstand(100000);
				tvip.viewer.zetSchaduw(false);
				tvip.viewer.tekenOpnieuw();
				tvip.viewer.muisAan = false;
    		}	
    	}
    	else if (e.getSource() == bovenkantRB)
    	{
			if (tvip.viewer.muisAan)
			{	laatsteDraaiX = tvip.viewer.geefDraaiX();
				laatsteDraaiY = tvip.viewer.geefDraaiY();
			}
   		
    		if (bovenkantRB.isSelected())
    		{	    			
    			zetDocentDraaihoek(90,0);
				tvip.viewer.zetAfstand(100000);
				tvip.viewer.zetSchaduw(false);
				tvip.viewer.tekenOpnieuw();
				tvip.viewer.muisAan = false;
    		}	
    	}
    	else if (e.getSource() == onderkantRB)
    	{
			if (tvip.viewer.muisAan)
			{	laatsteDraaiX = tvip.viewer.geefDraaiX();
				laatsteDraaiY = tvip.viewer.geefDraaiY();
			}

    		if (onderkantRB.isSelected())
    		{	zetDocentDraaihoek(-90,0);
				tvip.viewer.zetAfstand(100000);
				tvip.viewer.zetSchaduw(false);
				tvip.viewer.tekenOpnieuw();
				tvip.viewer.muisAan = false;
    		}	
    	}
    	else if (e.getSource() == linkerkantRB)
    	{
			if (tvip.viewer.muisAan)
			{	laatsteDraaiX = tvip.viewer.geefDraaiX();
				laatsteDraaiY = tvip.viewer.geefDraaiY();
			}

    		if (linkerkantRB.isSelected())
    		{	zetDocentDraaihoek(0,90);
				tvip.viewer.zetAfstand(100000);
				tvip.viewer.zetSchaduw(false);
				tvip.viewer.tekenOpnieuw();
				tvip.viewer.muisAan = false;
    		}	
    	}
    	else if (e.getSource() == rechterkantRB)
    	{
			if (tvip.viewer.muisAan)
			{	laatsteDraaiX = tvip.viewer.geefDraaiX();
				laatsteDraaiY = tvip.viewer.geefDraaiY();
			}

    		if (rechterkantRB.isSelected())
    		{	zetDocentDraaihoek(0,-90);
				tvip.viewer.zetAfstand(100000);
				tvip.viewer.zetSchaduw(false);
				tvip.viewer.tekenOpnieuw();
				tvip.viewer.muisAan = false;
    		}	
    	}
    	else if (e.getSource() == kijkVlakkenNaCB)
    	{	
    		tvip.zetKijkVlakkenNa(kijkVlakkenNaCB.isSelected());

    		if (kijkVlakkenNaCB.isSelected())
    		{
    			zetVlakkenKleurenOptiesEnabled(true);
    			kijkDraaihoekNaCB.setSelected(false);
    			zetDraaihoekOptiesEnabled(false);
/*    			
    			// we hebben al een viewer
    			if (profielenKleurenRB.isSelected())
    			{
    				tvip.setProfilesOnly(true);
    				//tvip.zetProfielenKleurenOptie(true);
    				tvip.vaktek.zetDocentModus(true);
    				tvip.vaktek.zetKlikAan(true);
    				tvip.vaktek.kijkNaPanel.setVisible(true);
    				tvip.vaktek.updateViewerKleuren();
    				
    			}
    			else
    			{
*/    			
    				//tvip.zetProfielenKleurenOptie(false);
    				tvip.viewer.zetDocentModus(true);
    				tvip.viewer.zetKlikAan(true); 
    				tvip.viewer.kijkNaPanel.setVisible(true);
    				tvip.viewer.updateViewerKleuren();
//    			}
    			
    		}
    		else
    		{
  //  			zetVlakkenKleurenOptiesEnabled(false);
    			tvip.vaktek.zetDocentModus(false);
				tvip.vaktek.zetKlikAan(false);
				tvip.vaktek.resetColors();
    			tvip.viewer.zetDocentModus(false);
				tvip.viewer.zetKlikAan(false);
				tvip.viewer.resetColors();
				tvip.viewer.kijkNaPanel.setVisible(false);
				tvip.vaktek.kijkNaPanel.setVisible(false);
    		}
    	}
/*    	
    	else if (e.getSource() == profielenKleurenRB)
    	{
    		//tvip.zetProfielenKleurenOptie(profielenKleurenRB.isSelected());
    		tvip.setProfilesOnly(true);
    		tvip.setViewerOnly(false);
    		tvip.vaktek.zetDocentModus(true);
			tvip.vaktek.zetKlikAan(true);
    		tvip.viewer.zetDocentModus(false);
			tvip.viewer.zetKlikAan(false);
			tvip.viewer.resetColors();
			tvip.viewer.kijkNaPanel.setVisible(false);
			tvip.vaktek.kijkNaPanel.setVisible(true);

    	}
*/    	
/*    	
    	else if (e.getSource() == viewerKleurenRB)
    	{
    		//tvip.zetProfielenKleurenOptie(!viewerKleurenRB.isSelected());
    		tvip.setViewerOnly(true);
    		tvip.setProfilesOnly(false);
    		tvip.viewer.zetDocentModus(true);
			tvip.viewer.zetKlikAan(true);
    		tvip.vaktek.zetDocentModus(false);
			tvip.vaktek.zetKlikAan(false);
			tvip.vaktek.resetColors();
			tvip.viewer.kijkNaPanel.setVisible(true);
			tvip.vaktek.kijkNaPanel.setVisible(false);
			
    	}
*/    	
    	
    }

    public void zetDocentDraaihoek(double ddhX, double ddhY)
    {
    	tvip.zetDocentDraaihoek(ddhX, ddhY);
    	tvip.viewer.zetBeginHoeken(ddhX, ddhY);
    	//tvip.viewer.tekenOpnieuw();
    }
    
    public int isAanzicht(double ddhX, double ddhY)
    {	double tol = 5e-1d;
    	if ((Math.abs(ddhX) < tol) && (Math.abs(ddhY) < tol)) 
    		return TekenVeelvlakInteractiePanel.FRONTVIEW;
    	else if ((Math.abs(ddhX) < tol) && (Math.abs(ddhY - 180) < tol)) 
    		return TekenVeelvlakInteractiePanel.BACKVIEW;
    	else if ((Math.abs(ddhX - 90) < tol) && (Math.abs(ddhY) < tol)) 
    		return TekenVeelvlakInteractiePanel.TOPVIEW;
    	else if ((Math.abs(ddhX + 90) < tol) && (Math.abs(ddhY) < tol)) 
    		return TekenVeelvlakInteractiePanel.BOTTOMVIEW;
    	else if ((Math.abs(ddhX) < tol) && (Math.abs(ddhY - 90) < tol))
    		return TekenVeelvlakInteractiePanel.LEFTVIEW;
    	else if ((Math.abs(ddhX) < tol) && (Math.abs(ddhY + 90) < tol)) 
    		return TekenVeelvlakInteractiePanel.RIGHTVIEW;
    	else
    		return -1;
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

    	else if (e.getSource() == frontArrowCB)
    	{
    		tvip.toonVooraanzichtPijl(frontArrowCB.isSelected());
    	}

    	else if (e.getSource() == viewerOnlyCB)
    	{
//System.out.println("viewerOnlyCB action");

    		tvip.setViewerOnly(viewerOnlyCB.isSelected());
    		zetViewerOptiesEnabled(viewerOnlyCB.isSelected());
    		if (viewerOnlyCB.isSelected())
    		{	profilesOnlyCB.setSelected(false);
    			tvip.setProfilesOnly(false);
    		}
    	}

    	
    	else if (e.getSource() == profilesOnlyCB)
    	{
//System.out.println("profilesOnlyCB action");    		
    		tvip.setProfilesOnly(profilesOnlyCB.isSelected());
    		zetViewerOptiesEnabled(false);
    		if (profilesOnlyCB.isSelected())
    		{	viewerOnlyCB.setSelected(false);
    			tvip.setViewerOnly(false);
    		}
    		
    	}

/*    	
    	else if (e.getSource() == vlakkenKleurenCB)
    	{
    		tvip.zetVlakkenKleurenOptie(vlakkenKleurenCB.isSelected());
    		//zetVlakkenKleurenOptiesEnabled(vlakkenKleurenCB.isSelected());
    		
    		if (vlakkenKleurenCB.isSelected())
    		{	
    		}
    		else
    		{	//profilesOnlyCB.setEnabled(true);
    			//viewerOnlyCB.setEnabled(true);
    			zetVlakkenKleurenEnabled(false);
    		}

    	}
*/ 
    }
    
    public void nakijkOptiesNaarTekenVeelvlakOpties()
    {
    	
    	if (kijkDraaihoekNaCB.isSelected())
    	{
    		// laat een viewer zien (niet in docentstand) met nakijkKnop
    		
    		tvip.viewer.zetBeginHoeken(tvipDraaiX, tvipDraaiY);
    		tvip.viewer.kijkNaPanel.setVisible(true);
    		viewerOnlyCB.setSelected(true);
    		tvip.zetViewerPosition(TekenVeelvlakInteractiePanel.MOVEABLE);
    		tvip.viewer.zetSchaduw(true);
    		tvip.viewer.zetAfstand(1000);
    		moveableRB.setSelected(true);
    		
    	}
		else if (kijkVlakkenNaCB.isSelected())
		{
			tvip.viewer.zetDocentModus(false);
			tvip.vaktek.zetDocentModus(false);

			tvip.viewer.zetKleuren(tvip.viewerKleuren);
			tvip.vaktek.setVaktekKleuren(tvip.viewerKleuren);
			//viewerOnlyCB.setSelected(true);
			//profilesOnlyCB.setSelected(false);
			if (tvipViewerOnly)
				tvip.setViewerOnly(tvipViewerOnly);
			if (tvipProfilesOnly)
				tvip.setProfilesOnly(tvipProfilesOnly);
			
			tvip.vaktek.kijkNaPanel.setVisible(true);
			
		}
		else // geen van twee
		{
			if (tvipViewerOnly)
				tvip.setViewerOnly(tvipViewerOnly);
			if (tvipProfilesOnly)
				tvip.setProfilesOnly(tvipProfilesOnly);
			if (tvip.viewerOnly)
			{
				tvip.viewer.zetBeginHoeken(tvipDraaiX, tvipDraaiY);				
			}
			else
			{
				tvip.tekenVeelvlak.zetBeginHoeken(tvipDraaiX, tvipDraaiY);
			}
		}
    }

    public void tekenVeelvlakOptiesNaarNakijkOpties()
    {
    	tvipViewerOnly = tvip.viewerOnly;
    	tvipProfilesOnly = tvip.profilesOnly;
    	tvipViewerPosition = tvip.viewerPosition;
		if (tvip.viewerOnly)
		{	tvipDraaiX = tvip.viewer.geefDraaiX();
			tvipDraaiY = tvip.viewer.geefDraaiY();
		}
		else
		{
			tvipDraaiX = tvip.tekenVeelvlak.geefDraaiX();
			tvipDraaiY = tvip.tekenVeelvlak.geefDraaiY();
		}
		
		if (tvip.viewerKleuren == null)
			tvip.viewerKleuren = tvip.viewer.getKleuren();
		
		if (kijkDraaihoekNaCB.isSelected())
		{
			tvip.setViewerOnly(true);
			tvip.viewer.zetBeginHoeken(tvip.docentDraaihoekX, tvip.docentDraaihoekY);
			if (!dezeDraaihoekRB.isSelected())
			{
				tvip.viewer.zetAfstand(100000);
				tvip.viewer.zetSchaduw(false);
				tvip.viewer.muisAan = false;
			}
			else
			{
				tvip.viewer.muisAan = true;
			}
			
		}
		else if (kijkVlakkenNaCB.isSelected())
		{
			tvip.viewer.zetDocentModus(true);
			//tvip.vaktek.zetDocentModus(true);
			// dit is al zo
			//tvip.viewer.zetKlikAan(true);
			//tvip.vaktek.zetKlikAan(true);
			tvip.setViewerOnly(true);
			if (tvip.docentKleuren != null)
				tvip.viewer.zetKleuren(tvip.docentKleuren);

		}
		else // geen van twee
		{
		
			// doe maar een viewer
			tvip.setViewerOnly(true);
			laatsteDraaiX = tvip.viewer.geefDraaiX();
			laatsteDraaiY = tvip.viewer.geefDraaiY();
			tvip.viewer.resetColors();
			
			//zetDraaihoekOptiesEnabled(true);
			dezeDraaihoekRB.setSelected(true);
			
			//if (tvip)
			 
			
		}
    	
    }
    
	class TabbedPaneCL implements ChangeListener
	{
			
		public void stateChanged(ChangeEvent e)
		{
			noSetBounds = true;
			int index = tabbedPane.getSelectedIndex();
			// terug naar tekenVVOptionsPanel
			if (index == 0)
			{	
//System.out.println("nakijk -> tvv");
				nakijkOptiesNaarTekenVeelvlakOpties();
			}
			else // naar nakijkOptiesPanel
			{	
//System.out.println("tvv -> nakijk");				
				
				tekenVeelvlakOptiesNaarNakijkOpties();
				
			}
		}
	}	
    public void zetViewerOptiesEnabled(boolean b)
    {
    	if (!b)
    		moveableRB.setSelected(true);
    	moveableRB.setEnabled(b);
		frontViewRB.setEnabled(b);
		backViewRB.setEnabled(b);
		topViewRB.setEnabled(b);
		bottomViewRB.setEnabled(b);
		leftViewRB.setEnabled(b);
		rightViewRB.setEnabled(b);
		//teacherViewRB.setEnabled(b);
		
    	
    }

    public void zetDraaihoekOptiesEnabled(boolean b)
    {
    	if (!b)
    		dezeDraaihoekRB.setSelected(true);
    	dezeDraaihoekRB.setEnabled(b);
		voorkantRB.setEnabled(b);
		achterkantRB.setEnabled(b);
		bovenkantRB.setEnabled(b);
		onderkantRB.setEnabled(b);
		linkerkantRB.setEnabled(b);
		rechterkantRB.setEnabled(b);
    	
    }

    
    public void zetVlakkenKleurenOptiesEnabled(boolean b)
    {
    	//kijkVlakkenNaCB.setEnabled(b);
    	//kijkVlakkenNaLabel.setEnabled(b);
    	//profielenKleurenRB.setEnabled(b);
		//viewerKleurenRB.setEnabled(b);
    	
    }

    public void zetVlakkenKleurenEnabled(boolean b)
    {
    	if (b)
    	{	
    		kijkVlakkenNaCB.setEnabled(b);
    		kijkVlakkenNaLabel.setEnabled(b);
    		//zetVlakkenKleurenOptiesEnabled(b);
    	}
    	else
    	{	kijkVlakkenNaCB.setSelected(false);
    		kijkVlakkenNaCB.setEnabled(b);
			kijkVlakkenNaLabel.setEnabled(b);
			tvip.docentKleuren = null;
			
			//zetVlakkenKleurenOptiesEnabled(b);
			//tvip.viewer.resetColors();
			//tvip.vaktek.resetColors();
    		//profielenKleurenRB.setEnabled(b);
    		//viewerKleurenRB.setEnabled(b);
    	}
    	
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
		
		if(vlakkenKleurenCB.isSelected())
			h.put("scoreMax", new Integer(10));
		else
			h.put("scoreMax", new Integer(0));
        
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

    	// tekenVVOptiesPanel

    	boolean toonVooraanzichtPijl = false;
        boolean viewerOnly = false; 
        boolean profilesOnly = false;
        int viewerPosition = 0;
        int aantalHulppunten = 0;
        
        boolean vlakkenKleurenOptie = false;
        
        if(h.containsKey("toonVooraanzichtPijl"))
          	 toonVooraanzichtPijl = ((Boolean)h.get("toonVooraanzichtPijl")).booleanValue();
        
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

//        if (h.containsKey("profielenKleurenOptie"))
//        	profielenKleurenOptie = ((Boolean) h.get("profielenKleurenOptie")).booleanValue();
//        if (h.containsKey("viewerKleurenOptie"))
//        	viewerKleurenOptie = ((Boolean) h.get("viewerKleurenOptie")).booleanValue();

        frontArrowCB.setSelected(toonVooraanzichtPijl);
	    viewerOnlyCB.setSelected(viewerOnly);
	    profilesOnlyCB.setSelected(profilesOnly);
	    
	    vlakkenKleurenCB.setSelected(vlakkenKleurenOptie);
	    vlakkenKleurenCB.setEnabled(viewerOnly || profilesOnly);
	    
	    
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

	    // nakijkOptiesPanel
	    
	    boolean kijkDraaihoekNa = false;
	    double docentDraaihoekX = 20;
	    double docentDraaihoekY = -30;
	    	    
        if (h.containsKey("kijkDraaihoekNa"))
        	kijkDraaihoekNa = ((Boolean) h.get("kijkDraaihoekNa")).booleanValue();
        kijkDraaihoekNaCB.setSelected(kijkDraaihoekNa);
        
        zetDraaihoekOptiesEnabled(kijkDraaihoekNa);
        
        if (h.containsKey("docentDraaihoekX"))
        	docentDraaihoekX = ((Double) h.get("docentDraaihoekX")).doubleValue();
        if (h.containsKey("docentDraaihoekY"))
        	docentDraaihoekY = ((Double) h.get("docentDraaihoekY")).doubleValue();

        int aanzicht = isAanzicht(docentDraaihoekX, docentDraaihoekY);
        if (aanzicht == TekenVeelvlakInteractiePanel.FRONTVIEW)
        	voorkantRB.setSelected(true);
    	else if (aanzicht == TekenVeelvlakInteractiePanel.BACKVIEW)
    		achterkantRB.setSelected(true);
    	else if (aanzicht == TekenVeelvlakInteractiePanel.TOPVIEW)
    		bovenkantRB.setSelected(true);
    	else if (aanzicht == TekenVeelvlakInteractiePanel.BOTTOMVIEW)
    		onderkantRB.setSelected(true);
    	else if (aanzicht == TekenVeelvlakInteractiePanel.LEFTVIEW)
    		linkerkantRB.setSelected(true);
    	else if (aanzicht == TekenVeelvlakInteractiePanel.RIGHTVIEW)
    		rechterkantRB.setSelected(true);
    	else if (aanzicht == -1)
    	{
    		if ((docentDraaihoekX < 1000) && (docentDraaihoekY < 1000))
    			dezeDraaihoekRB.setSelected(true);
    	}
        
        
        
        boolean kijkVlakkenNa = false;
        
        if (h.containsKey("kijkVlakkenNa"))
        	kijkVlakkenNa = ((Boolean) h.get("kijkVlakkenNa")).booleanValue();
        
	
	    kijkVlakkenNaCB.setSelected(kijkVlakkenNa);
	    
	    if (viewerOnly || profilesOnly)
	    	tabbedPane.setEnabledAt(1,true);
	    
	    
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

