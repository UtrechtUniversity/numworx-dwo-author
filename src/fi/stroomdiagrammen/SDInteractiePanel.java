package fi.stroomdiagrammen;

import java.awt.*;
import java.awt.event.*;
import java.util.Hashtable;

import javax.swing.*;

import fi.beans.base64code.StringCodeObject;
import fi.beans.wiskopdrbeans.WiskOpdrApplet;
// deze moet vanwege interface WiskOpdrApplet
import fi.beans.wiskopdrbeans.InteractiePanel;
// deze moet vanwege interface InteractiePanel
import fi.beans.wiskopdrbeans.InteractieEditPanel;
import fi.stroomdiagrammen.Stroomdiagrammen.MenuListener;

public class SDInteractiePanel extends JPanel implements InteractiePanel, InteractieEditPanel,
							         ActionListener
									
{
	
	Stroomdiagrammen eigenaar;
	
	Font theFont;
	FontMetrics theFM;
	Font theBoldFont;
	FontMetrics theBoldFM;
	
	JPanel sdPanel;
	
	MenuPanel menuPanel;
	int menuHeight = 30;

	DrawingPanel drawingPanel;
	BottomPanel bottomPanel;
	
	// menu-gebeuren
	JMenuBar menuBar;
	JMenu berekeningenMenu, stroombreedteMenu, optiesMenu;
	
	JButton berekeningenButton, stroombreedteButton, optiesButton;
	int berekeningenButtonWidth, stroombreedteButtonWidth, optiesButtonWidth;
	//JPopupMenu berekeningenMenu, stroombreedteMenu, optiesMenu;
	
	JRadioButtonMenuItem rbDecimaalItem, rbBreukenItem;
	JRadioButtonMenuItem rbRelatiefItem, rbAbsoluutItem;
	JCheckBoxMenuItem cbLabelsItem;
	JMenuItem addRootItem;
	
   // parametrisatie
    boolean toonBerekeningenMenu = true;
    boolean berekenInBreuken = false;
    
    boolean toonStroombreedteMenu = true;
    boolean stroombreedteAbsoluut = false;
    
    boolean toonOptiesMenu = true;
    boolean toonLabels = false;
    int aantalBronnen = 1;
    
    boolean isDemo = false;

	int score = 0;
	int scoreMax = 0;
    
	boolean noSetBounds = false;
	
	public SDInteractiePanel(Stroomdiagrammen eigenaar)
	{
		this.eigenaar = eigenaar;
		
		setLayout(null);
		// echte initiatie vind pas plaats na setBounds

		theFont = new Font("Dialog", Font.PLAIN, 12);
		theFM = getFontMetrics(theFont);
		theBoldFont = new Font("Dialog", Font.BOLD, 12);
		theBoldFM = getFontMetrics(theBoldFont);
		
		// menubalk
		menuBar = new JMenuBar();
		MenuListener menuListener = new MenuListener();
		
		menuPanel = new MenuPanel();
		menuPanel.setLayout(null);
		menuHeight = 3 * theBoldFM.getHeight() / 2 + 1;
		
		// menu berekeningen
		berekeningenButton = new JButton(Stroomdiagrammen.rb.getString("represText"));
		berekeningenButton.setFont(theBoldFont);
		berekeningenButtonWidth = theBoldFM.stringWidth(Stroomdiagrammen.rb.getString("represText")) + 40;
		berekeningenButton.setBounds(0, 0, berekeningenButtonWidth, 3 * theBoldFM.getHeight() / 2);
		menuPanel.add(berekeningenButton);
		berekeningenButton.addActionListener(menuListener);
		
		berekeningenMenu = new JMenu(Stroomdiagrammen.rb.getString("represText"));
		//berekeningenMenu = new JPopupMenu();
		ButtonGroup berekeningenGroup = new ButtonGroup();
		rbDecimaalItem = new JRadioButtonMenuItem(Stroomdiagrammen.rb.getString("decimalText"));
		if (!berekenInBreuken)
			rbDecimaalItem.setSelected(true);
		berekeningenGroup.add(rbDecimaalItem);
		berekeningenMenu.add(rbDecimaalItem);
		rbDecimaalItem.addActionListener(menuListener);
		
		rbBreukenItem = new JRadioButtonMenuItem(Stroomdiagrammen.rb.getString("fractionText"));
		if (berekenInBreuken)
			rbBreukenItem.setSelected(true);
		berekeningenGroup.add(rbBreukenItem);
		berekeningenMenu.add(rbBreukenItem);
		rbBreukenItem.addActionListener(menuListener);
		
		//menu stroombreedte
		stroombreedteButton = new JButton(Stroomdiagrammen.rb.getString("thicknessText"));
		stroombreedteButton.setFont(theBoldFont);
		stroombreedteButtonWidth = theBoldFM.stringWidth(Stroomdiagrammen.rb.getString("thicknessText")) + 40;
		stroombreedteButton.setBounds(berekeningenButton.getLocation().x + berekeningenButton.getSize().width, 0, 
									  stroombreedteButtonWidth, 3 * theBoldFM.getHeight() / 2);
		menuPanel.add(stroombreedteButton);
		stroombreedteButton.addActionListener(menuListener);
		
		stroombreedteMenu = new JMenu(Stroomdiagrammen.rb.getString("thicknessText"));
		//stroombreedteMenu = new JPopupMenu();
		ButtonGroup stroombreedteGroup = new ButtonGroup();
		rbRelatiefItem = new JRadioButtonMenuItem(Stroomdiagrammen.rb.getString("relativeText"));
		if (!stroombreedteAbsoluut)
			rbRelatiefItem.setSelected(true);
		stroombreedteGroup.add(rbRelatiefItem);
		stroombreedteMenu.add(rbRelatiefItem);
		rbRelatiefItem.addActionListener(menuListener);
		
		rbAbsoluutItem = new JRadioButtonMenuItem(Stroomdiagrammen.rb.getString("absoluteText"));
		if (stroombreedteAbsoluut)
			rbAbsoluutItem.setSelected(true);
		stroombreedteGroup.add(rbAbsoluutItem);
		stroombreedteMenu.add(rbAbsoluutItem);
		rbAbsoluutItem.addActionListener(menuListener);
		
		// optiesMenu
		optiesButton = new JButton(Stroomdiagrammen.rb.getString("optionsText"));
		optiesButton.setFont(theBoldFont);
		optiesButtonWidth = theBoldFM.stringWidth(Stroomdiagrammen.rb.getString("optionsText")) + 40;
		optiesButton.setBounds(stroombreedteButton.getLocation().x + stroombreedteButton.getSize().width, 0, 
				 			   optiesButtonWidth, 3 * theBoldFM.getHeight() / 2);
		menuPanel.add(optiesButton);
		optiesButton.addActionListener(menuListener);
		
		optiesMenu = new JMenu(Stroomdiagrammen.rb.getString("optionsText"));
		//optiesMenu = new JPopupMenu();
		cbLabelsItem = new JCheckBoxMenuItem(Stroomdiagrammen.rb.getString("labelsText"));
		cbLabelsItem.setSelected(toonLabels);
		optiesMenu.add(cbLabelsItem);
		cbLabelsItem.addActionListener(menuListener);
		addRootItem = new JMenuItem(Stroomdiagrammen.rb.getString("addRootText"));
		optiesMenu.add(addRootItem);
		addRootItem.addActionListener(menuListener);

		
		if (toonBerekeningenMenu)
		{	menuBar.add(berekeningenMenu);
		}
		if (toonStroombreedteMenu)
		{	menuBar.add(stroombreedteMenu);
		}
		if (toonOptiesMenu)
		{	menuBar.add(optiesMenu);
		}
		
		//setJMenuBar(menuBar);
		
		
	}


	public void zetDocentModus()
	{
		rbDecimaalItem.setEnabled(false);
		rbBreukenItem.setEnabled(false);
		rbRelatiefItem.setEnabled(false);
		rbAbsoluutItem.setEnabled(false);
		cbLabelsItem.setEnabled(false);
		addRootItem.setEnabled(false);
		
	}

	public void zetOpdracht(Hashtable b, String[] randomVars, Hashtable randomValues)
	{
System.out.println("sdip zetOpdracht");

		boolean toonBerekeningenMenu = true;
		boolean berekenInBreuken = false;

		boolean toonStroombreedteMenu = true;
		boolean stroombreedteAbsoluut = false;

		boolean toonOptiesMenu = true;
		boolean toonLabels = false;
		int aantalBronnen = 1;
		
		boolean isDemo = false;
		
		int sdipBreedte = 500; 
		int sdipHoogte = 450; 

		if (b.containsKey("appletLaunchData"))
		{
//System.out.println("aLD found");
			Hashtable appletLaunchData = (Hashtable) b.get("appletLaunchData");

			String berekenMenuString = "true";
			String breukenString = "false";
			String stroomMenuString = "true";
			String absoluutString = "false";
			String optiesMenuString = "true";
			String labelsString = "false";
			String bronnenString = "1";

			if (appletLaunchData.containsKey("berekenmenu"))
				berekenMenuString = (String) appletLaunchData.get("berekenmenu");
			if (berekenMenuString.equals("false") || berekenMenuString.equals("no"))
				toonBerekeningenMenu = false;
			if (appletLaunchData.containsKey("breuken"))
				breukenString = (String) appletLaunchData.get("breuken");
			if (breukenString.equals("true") || breukenString.equals("yes"))
				berekenInBreuken = true;
			if (appletLaunchData.containsKey("stroommenu"))
				stroomMenuString = (String) appletLaunchData.get("stroommenu");
			if (stroomMenuString.equals("false") || stroomMenuString.equals("no"))
				toonStroombreedteMenu = false;
			if (appletLaunchData.containsKey("absoluut"))
				absoluutString = (String) appletLaunchData.get("absoluut");
			if (absoluutString.equals("true") || absoluutString.equals("yes"))
				stroombreedteAbsoluut = true;
			if (appletLaunchData.containsKey("optiesmenu"))
				optiesMenuString = (String) appletLaunchData.get("optiesmenu");
			if (optiesMenuString.equals("false") || optiesMenuString.equals("no"))
				toonOptiesMenu = false;
			if (appletLaunchData.containsKey("labels"))
				labelsString = (String) appletLaunchData.get("labels");
			if (labelsString.equals("true") || labelsString.equals("yes"))
				toonLabels = true;
			if (appletLaunchData.containsKey("bronnen"))
				aantalBronnen = Integer.parseInt((String) appletLaunchData.get("bronnen"));
			if ((aantalBronnen < 1) || (aantalBronnen > 4))
				aantalBronnen = 1;
			
			
			
		}
		else
		{
			if (b.containsKey("toonBerekeningenMenu"))
				toonBerekeningenMenu = ((Boolean) b.get("toonBerekeningenMenu")).booleanValue();
			if (b.containsKey("berekenInBreuken"))
				berekenInBreuken = ((Boolean) b.get("berekenInBreuken")).booleanValue();
			if (b.containsKey("toonStroombreedteMenu"))
				toonStroombreedteMenu = ((Boolean) b.get("toonStroombreedteMenu")).booleanValue();
			if (b.containsKey("stroombreedteAbsoluut"))
				stroombreedteAbsoluut = ((Boolean) b.get("stroombreedteAbsoluut")).booleanValue();
			if (b.containsKey("toonOptiesMenu"))
				toonOptiesMenu = ((Boolean) b.get("toonOptiesMenu")).booleanValue();
			if (b.containsKey("toonLabels"))
				toonLabels = ((Boolean) b.get("toonLabels")).booleanValue();
			if (b.containsKey("aantalBronnen"))
				aantalBronnen = ((Integer) b.get("aantalBronnen")).intValue();
			
			if (b.containsKey("isDemo"))
				isDemo = ((Boolean) b.get("isDemo")).booleanValue();
			
			if (b.containsKey("sdipBreedte"))
				sdipBreedte = ((Integer) b.get("sdipBreedte")).intValue();
			if (b.containsKey("sdipHoogte"))
				sdipHoogte = ((Integer) b.get("sdipHoogte")).intValue();

			
			
		}
		
		zetToonBerekeningenMenu(toonBerekeningenMenu);
		zetBerekenInBreuken(berekenInBreuken);
		zetToonStroombreedteMenu(toonStroombreedteMenu);
		zetStroombreedteAbsoluut(stroombreedteAbsoluut);
		zetToonOptiesMenu(toonOptiesMenu);
		zetToonLabels(toonLabels);
		zetAantalBronnen(aantalBronnen);
		
		zetIsDemo(isDemo);

		setBounds(0,0,sdipBreedte,sdipHoogte);
		
		//DiagramCopy diagramCopy = null;
		if (b.containsKey("appletEditState"))
		{
//System.out.println("aES found");
			String appletEditState = (String) b.get("appletEditState");
			
			// decodeer de string
			Object o = StringCodeObject.decodeStringToObject(appletEditState);

			// cast
			Hashtable h = (Hashtable) o;
			
			// haal de stateString uit de hashtable
			String stateString = "";
			if (h.containsKey("state"))
			{	stateString = (String) h.get("state");

	//System.out.println("string found");		
			
			}
			// decodeer de string		
			if ((stateString != null) && !stateString.equals(""))
			{	
				Object ob = StringCodeObject.decodeStringToObject(stateString);
				eigenaar.diagramCopy = (DiagramCopy) ob;
				
	//System.out.println("string decoded");
				drawingPanel.initialize();	
			}
			else 
				eigenaar.diagramCopy = null;	
			
		}	
		else
		{
			String stateString = ""; 
			if (b.containsKey("state"))
			{	stateString = (String) b.get("state");

	//System.out.println("string found");		
			
			}
			// decodeer de string		
			if ((stateString != null) && !stateString.equals(""))
			{	
				Object ob = StringCodeObject.decodeStringToObject(stateString);
				eigenaar.diagramCopy = (DiagramCopy) ob;
				
	//System.out.println("string decoded");
				drawingPanel.initialize();	
			}
			else 
				eigenaar.diagramCopy = null;	
				
		}
		
	}
	
	public void setState(Hashtable b)
	{
		if (b.containsKey("appletState"))
		{
//System.out.println("aS found");
			String appletState = (String) b.get("appletState");
			
			// decodeer de string
			Object o = StringCodeObject.decodeStringToObject(appletState);
			
			// cast
			Hashtable h = (Hashtable) o;
			
			// haal de stateString uit de hashtable
			String stateString = "";
			if (h.containsKey("state"))
			{	stateString = (String) h.get("state");

	//System.out.println("string found");		
			
			}
			// decodeer de string		
			if ((stateString != null) && !stateString.equals(""))
			{	
				Object ob = StringCodeObject.decodeStringToObject(stateString);
				eigenaar.diagramCopy = (DiagramCopy) ob;
				
	//System.out.println("string decoded");
				drawingPanel.initialize();	
			}
			else 
				eigenaar.diagramCopy = null;	
			
		}
		else
		{
			String stateString = ""; 
			if (b.containsKey("state"))
			{	stateString = (String) b.get("state");

	//System.out.println("string found");		
			
			}
			// decodeer de string		
			if ((stateString != null) && !stateString.equals(""))
			{	
				Object ob = StringCodeObject.decodeStringToObject(stateString);
				eigenaar.diagramCopy = (DiagramCopy) ob;
				
	//System.out.println("string decoded");
				drawingPanel.initialize();	
			}
			else 
				eigenaar.diagramCopy = null;	
				
		}

	}
	
	public void setEditState(Hashtable b)
	{
System.out.println("sdip setEditState");

		boolean toonBerekeningenMenu = true;
		boolean berekenInBreuken = false;

		boolean toonStroombreedteMenu = true;
		boolean stroombreedteAbsoluut = false;

		boolean toonOptiesMenu = true;
		boolean toonLabels = false;
		int aantalBronnen = 1;

		boolean isDemo = false;
		
		int sdipBreedte = 500; 
		int sdipHoogte = 450; 
		
		
		if (b.containsKey("appletLaunchData"))
		{
//System.out.println("aLD found");
			Hashtable appletLaunchData = (Hashtable) b.get("appletLaunchData");

			String berekenMenuString = "true";
			String breukenString = "false";
			String stroomMenuString = "true";
			String absoluutString = "false";
			String optiesMenuString = "true";
			String labelsString = "false";
			String bronnenString = "1";

			if (appletLaunchData.containsKey("berekenmenu"))
				berekenMenuString = (String) appletLaunchData.get("berekenmenu");
			if (berekenMenuString.equals("false") || berekenMenuString.equals("no"))
				toonBerekeningenMenu = false;
			if (appletLaunchData.containsKey("breuken"))
				breukenString = (String) appletLaunchData.get("breuken");
			if (breukenString.equals("true") || breukenString.equals("yes"))
				berekenInBreuken = true;
			if (appletLaunchData.containsKey("stroommenu"))
				stroomMenuString = (String) appletLaunchData.get("stroommenu");
			if (stroomMenuString.equals("false") || stroomMenuString.equals("no"))
				toonStroombreedteMenu = false;
			if (appletLaunchData.containsKey("absoluut"))
				absoluutString = (String) appletLaunchData.get("absoluut");
			if (absoluutString.equals("true") || absoluutString.equals("yes"))
				stroombreedteAbsoluut = true;
			if (appletLaunchData.containsKey("optiesmenu"))
				optiesMenuString = (String) appletLaunchData.get("optiesmenu");
			if (optiesMenuString.equals("false") || optiesMenuString.equals("no"))
				toonOptiesMenu = false;
			if (appletLaunchData.containsKey("labels"))
				labelsString = (String) appletLaunchData.get("labels");
			if (labelsString.equals("true") || labelsString.equals("yes"))
				toonLabels = true;
			if (appletLaunchData.containsKey("bronnen"))
				aantalBronnen = Integer.parseInt((String) appletLaunchData.get("bronnen"));
			if ((aantalBronnen < 1) || (aantalBronnen > 4))
				aantalBronnen = 1;
			
			
			
		}
		else
		{
			if (b.containsKey("toonBerekeningenMenu"))
				toonBerekeningenMenu = ((Boolean) b.get("toonBerekeningenMenu")).booleanValue();
			if (b.containsKey("berekenInBreuken"))
				berekenInBreuken = ((Boolean) b.get("berekenInBreuken")).booleanValue();
			if (b.containsKey("toonStroombreedteMenu"))
				toonStroombreedteMenu = ((Boolean) b.get("toonStroombreedteMenu")).booleanValue();
			if (b.containsKey("stroombreedteAbsoluut"))
				stroombreedteAbsoluut = ((Boolean) b.get("stroombreedteAbsoluut")).booleanValue();
			if (b.containsKey("toonOptiesMenu"))
				toonOptiesMenu = ((Boolean) b.get("toonOptiesMenu")).booleanValue();
			if (b.containsKey("toonLabels"))
				toonLabels = ((Boolean) b.get("toonLabels")).booleanValue();
			if (b.containsKey("aantalBronnen"))
				aantalBronnen = ((Integer) b.get("aantalBronnen")).intValue();
			
			if (b.containsKey("isDemo"))
				isDemo = ((Boolean) b.get("isDemo")).booleanValue();
			
			if (b.containsKey("sdipBreedte"))
				sdipBreedte = ((Integer) b.get("sdipBreedte")).intValue();
			if (b.containsKey("sdipHoogte"))
				sdipHoogte = ((Integer) b.get("sdipHoogte")).intValue();
			
		}

		
		
		zetToonBerekeningenMenu(toonBerekeningenMenu);
		zetBerekenInBreuken(berekenInBreuken);
		zetToonStroombreedteMenu(toonStroombreedteMenu);
		zetStroombreedteAbsoluut(stroombreedteAbsoluut);
		zetToonOptiesMenu(toonOptiesMenu);
		zetToonLabels(toonLabels);
		zetAantalBronnen(aantalBronnen);
		
		zetIsDemo(isDemo);		
		
		setBounds(0,0,sdipBreedte,sdipHoogte);
		
		if (b.containsKey("appletEditState"))
		{
//System.out.println("aES found");
			String appletEditState = (String) b.get("appletEditState");
			
			// decodeer de string
			Object o = StringCodeObject.decodeStringToObject(appletEditState);
			
			// cast
			Hashtable h = (Hashtable) o;
			
			// haal de stateString uit de hashtable
			String stateString = "";
			if (h.containsKey("state"))
			{	stateString = (String) h.get("state");

	//System.out.println("string found");		
			
			}
			// decodeer de string		
			if ((stateString != null) && !stateString.equals(""))
			{	
				Object ob = StringCodeObject.decodeStringToObject(stateString);
				eigenaar.diagramCopy = (DiagramCopy) ob;
				
	//System.out.println("string decoded");
				drawingPanel.initialize();	
			}
			else 
				eigenaar.diagramCopy = null;	
			

		}
		else
		{
			String stateString = ""; 
			if (b.containsKey("state"))
			{	stateString = (String) b.get("state");

	//System.out.println("string found");		
			
			}
			// decodeer de string		
			if ((stateString != null) && !stateString.equals(""))
			{	
				Object ob = StringCodeObject.decodeStringToObject(stateString);
				eigenaar.diagramCopy = (DiagramCopy) ob;
				
	//System.out.println("string decoded");
				drawingPanel.initialize();	
			}
			else 
				eigenaar.diagramCopy = null;	
				
		}
		
	}
	
	public Hashtable getState()
	{
		Hashtable h = new Hashtable();
		
		// codeer de diagramCopy tot string
		DiagramCopy diagramCopy = drawingPanel.diagramManager.copyDiagram();		
		
		String stateString = "";
		if (diagramCopy != null)
		{	stateString = StringCodeObject.encodeObjectToString(diagramCopy);
	    	
System.out.println("diagramcopy encoded");	    	

		}	
		// stop de gecodeerde string in de hashtable
		h.put("state", stateString);
		
		return h;
		
	}
	
	public Hashtable getEditState()
	{
		Hashtable h = new Hashtable();
		
		h.put("toonBerekeningenMenu", new Boolean(toonBerekeningenMenu));
		h.put("berekenInBreuken", new Boolean(berekenInBreuken));
		
		h.put("toonStroombreedteMenu", new Boolean(toonStroombreedteMenu));
		h.put("stroombreedteAbsoluut", new Boolean(stroombreedteAbsoluut));
		
		h.put("toonOptiesMenu", new Boolean(toonOptiesMenu));
		h.put("toonLabels", new Boolean(toonLabels));
		h.put("aantalBronnen", new Integer(aantalBronnen));
		
		h.put("isDemo", new Boolean(isDemo));

		// codeer de diagramCopy tot string
		DiagramCopy diagramCopy = drawingPanel.diagramManager.copyDiagram();		
		
		String stateString = "";
		if (diagramCopy != null)
		{	stateString = StringCodeObject.encodeObjectToString(diagramCopy);
	    	
System.out.println("diagramcopy encoded");	    	

		}	
		// stop de gecodeerde string in de hashtable
		h.put("state", stateString);
		
		return h;
	}
	
	public void zetToonBerekeningenMenu(boolean b)
	{	toonBerekeningenMenu = b;
		layoutMenuBar();
	}
	
	public void zetBerekenInBreuken(boolean b)
	{	berekenInBreuken = b;
		rbBreukenItem.setSelected(berekenInBreuken);
		if (berekenInBreuken)
			drawingPanel.diagramManager.setFlowMode(DrawingPanel.fracMode);
		else
			drawingPanel.diagramManager.setFlowMode(DrawingPanel.decMode);
		
	}

	
	public void zetToonStroombreedteMenu(boolean b)
	{	toonStroombreedteMenu = b;
		layoutMenuBar();
	}

	public void zetStroombreedteAbsoluut(boolean b)
	{	stroombreedteAbsoluut = b;
		rbAbsoluutItem.setSelected(stroombreedteAbsoluut);
		if (stroombreedteAbsoluut)
			drawingPanel.diagramManager.setEdgeThicknessMode(DrawingPanel.absMode);
		else
			drawingPanel.diagramManager.setEdgeThicknessMode(DrawingPanel.relMode);
	}
	
	public void zetToonOptiesMenu(boolean b)
	{	toonOptiesMenu = b;
		layoutMenuBar();
	}

	public void zetToonLabels(boolean b)
	{	toonLabels = b;
		cbLabelsItem.setSelected(toonLabels);
		drawingPanel.diagramManager.setVertexLabels(toonLabels);
	}

	public void zetAantalBronnen(int num)
	{	aantalBronnen = num;
		if (drawingPanel.roots.size() < aantalBronnen)
		{	int rootsWanted = aantalBronnen - drawingPanel.roots.size();
			for (int rCnt = 0; rCnt < rootsWanted; rCnt++)
				drawingPanel.addNewRoot(true);
			
		}
		else if (drawingPanel.roots.size() > aantalBronnen)
		{	drawingPanel.diagramManager.clearDiagram(true);
			int rootsWanted = aantalBronnen - 1;
			for (int rCnt = 0; rCnt < rootsWanted; rCnt++)
				drawingPanel.addNewRoot(true);
			
		}
			
	
	}
	
	public void zetIsDemo(boolean b)
	{
		isDemo = b;
		
//System.out.println("dp height before = " + drawingPanel.getSize().height);		
		
		menuBar.setVisible(!isDemo);
		bottomPanel.setVisible(!isDemo);
		
		sdPanel.validate();
		drawingPanel.defineSpaces(true);
		drawingPanel.updateWork();
		drawingPanel.repaint();
	
		drawingPanel.zetIsDemo(isDemo);
//System.out.println("dp height after = " + drawingPanel.getSize().height);		
		
	}

	public void layoutMenuBar()
	{
		berekeningenMenu.setVisible(toonBerekeningenMenu);
		stroombreedteMenu.setVisible(toonStroombreedteMenu);
		optiesMenu.setVisible(toonOptiesMenu);
		
		setBounds(0,0,getSize().width,getSize().height);
		
	}
	
	public void layoutMenuPanel()
	{
		berekeningenButton.setVisible(toonBerekeningenMenu);
		stroombreedteButton.setVisible(toonStroombreedteMenu);
		optiesButton.setVisible(toonOptiesMenu);

		boolean noneVisible = false;
		
		if (toonBerekeningenMenu && toonStroombreedteMenu && toonOptiesMenu)
		{	
			berekeningenButton.setLocation(0, 0);
			stroombreedteButton.setLocation(berekeningenButtonWidth, 0);
			optiesButton.setLocation(berekeningenButtonWidth + stroombreedteButtonWidth, 0);
			
		}
		else if (!toonBerekeningenMenu && toonStroombreedteMenu && toonOptiesMenu)
		{
			stroombreedteButton.setLocation(0, 0);
			optiesButton.setLocation(stroombreedteButtonWidth, 0);
			
		}
		else if (toonBerekeningenMenu && !toonStroombreedteMenu && toonOptiesMenu)
		{
			berekeningenButton.setLocation(0, 0);
			optiesButton.setLocation(berekeningenButtonWidth, 0);
		}
		else if (toonBerekeningenMenu && toonStroombreedteMenu && !toonOptiesMenu)
		{
			berekeningenButton.setLocation(0, 0);
			stroombreedteButton.setLocation(berekeningenButtonWidth, 0);
		}
		else if (!toonBerekeningenMenu && !toonStroombreedteMenu && toonOptiesMenu)
		{
			optiesButton.setLocation(0, 0);
		}
		else if (!toonBerekeningenMenu && toonStroombreedteMenu && !toonOptiesMenu)
		{
			stroombreedteButton.setLocation(0, 0);
		}
		else if (toonBerekeningenMenu && !toonStroombreedteMenu && !toonOptiesMenu)
		{
			berekeningenButton.setLocation(0, 0);
		}
		else if (!toonBerekeningenMenu && !toonStroombreedteMenu && !toonOptiesMenu)
		{
			noneVisible = true;
		}
		if (!noneVisible)
		{	drawingPanel.setBounds(0, menuHeight, getSize().width, getSize().height - menuHeight - eigenaar.bottomHeight);
			menuPanel.setVisible(true);
		}
		else
		{	drawingPanel.setBounds(0, 0, getSize().width, getSize().height - eigenaar.bottomHeight);
			menuPanel.setVisible(false);
//System.out.println("noneVisible");		
//System.out.println("dp y = " + drawingPanel.getLocation().y);
		}
		drawingPanel.defineSpaces(true);
		drawingPanel.updateWork();
		drawingPanel.repaint();

	}
	
	public InteractieEditPanel getEditPanel()
	{
		return new SDInteractieEditPanel(eigenaar);
	}
		
	public void setBounds(int x, int y, int b, int h)
	{
		
//System.out.println("sdip set bounds " + b + " " + h);
		
		if (h == 1)
			return;
		
		super.setBounds(x, y, b, h);
		
		
		if (sdPanel == null) 
		{	

			sdPanel = new JPanel();
			sdPanel.setLayout(null);
			//sdPanel.setLayout(new BorderLayout());
//sdPanel.setBackground(Color.orange);			
			sdPanel.setBounds(0, 0, b, h);
			add(sdPanel);
			
			menuBar.setBounds(0, 0, b, menuHeight);
			menuBar.setVisible(false);
			sdPanel.add(menuBar);
			if (toonBerekeningenMenu || toonStroombreedteMenu || toonOptiesMenu)
				menuBar.setVisible(true);
			
			//sdPanel.add(menuBar, BorderLayout.NORTH);

			drawingPanel = new DrawingPanel(eigenaar, true, this);
			if (menuBar.isVisible())
				drawingPanel.setBounds(0, menuHeight, b, h - menuHeight - eigenaar.bottomHeight);
			else
				drawingPanel.setBounds(0, 0, b, h - eigenaar.bottomHeight);
			sdPanel.add(drawingPanel);
			//drawingPanel.initialize();
			
			//sdPanel.add(drawingPanel, BorderLayout.CENTER);
			
			
			bottomPanel = new BottomPanel(eigenaar, true, this);
			bottomPanel.setBounds(0, h - eigenaar.bottomHeight, b, eigenaar.bottomHeight);
			//bottomPanel.initialize();
			sdPanel.add(bottomPanel);
			//sdPanel.add(bottomPanel, BorderLayout.SOUTH);
			
			//sdPanel.validate();
			
			drawingPanel.initialize();
			bottomPanel.initialize();
			
			
//System.out.println("sdPanel created");	
//System.out.println("sdPanel cc = " + sdPanel.getComponentCount());
//System.out.println("bMenu enabled = " + berekeningenMenu.isEnabled());


		}
		else
		{	sdPanel.setSize(b, h);
			
		//menuPanel.setSize(b, menuHeight);

			//sdPanel.validate();

			if (toonBerekeningenMenu || toonStroombreedteMenu || toonOptiesMenu)
				menuBar.setVisible(true);
			else
				menuBar.setVisible(false);
		
			if (menuBar.isVisible())
				drawingPanel.setBounds(0, menuHeight, b, h - menuHeight - eigenaar.bottomHeight);
			else
				drawingPanel.setBounds(0, 0, b, h - eigenaar.bottomHeight);
			
			drawingPanel.defineSpaces(true);
			drawingPanel.updateWork();
			drawingPanel.repaint();
			
			bottomPanel.setBounds(0, h - eigenaar.bottomHeight, b, eigenaar.bottomHeight);
			bottomPanel.initialize();
//System.out.println("sdPanel sized");		
		}
		
	}
	
	public void wis()
	{}
	
	public void zetMaat()
	{}
	
	public int geefAsHoogte()
	{	return 0;
	}
	
	public int getIpId()
	{	return 0;
	}
	
	public String getIpExpString()
	{	return null;
	}
	
	public int getScore()
	{	return score;
	}
	
	public int getScoreMax()
	{	return scoreMax;
	}
	
	public boolean isCorrect()
	{	return true;
	}
	
	public boolean isFout()
	{	return false;
	}
	
	public void zetMode(int mode)
	{}
	
	public void zetNagekeken(boolean b)
	{}
	
    public void stop()
    {}
    
    public void start()
    {}
    
    public void destroy()
    {}
    
    public void opnieuw()
    {}
    
    public void kijkNa()
    {}
    
    public void kijkNa(int stapNr)
    {}
    
    public void addActionListener(ActionListener al)
    {}

	public void zetBreedte(int b)
	{}
	
	public void zetHoogte(int h)
	{}
    
	public void actionPerformed(ActionEvent e)
	{}
	
	
	
	class MenuListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{	
			if (e.getSource() == berekeningenButton)
			{
/*				
				berekeningenMenu.show(sdPanel, 
									  berekeningenButton.getLocation().x, 
									  berekeningenButton.getLocation().y + 
									  berekeningenButton.getSize().height);
				
				// HIER !!
				berekeningenMenu.setPopupSize(berekeningenButton.getSize().width, 
					                          berekeningenMenu.getSize().height);
*/				
			}
			// berekeningenMenu
			else if (e.getSource() == rbDecimaalItem)
			{	if (rbDecimaalItem.isSelected())
				{	drawingPanel.diagramManager.setFlowMode(DrawingPanel.decMode);
				}
			}
			else if (e.getSource() == rbBreukenItem)
			{	if (rbBreukenItem.isSelected())
				{	drawingPanel.diagramManager.setFlowMode(DrawingPanel.fracMode);
				}
			}
			else if (e.getSource() == stroombreedteButton)
			{
/*				
				stroombreedteMenu.show(sdPanel, 
									  stroombreedteButton.getLocation().x, 
									  stroombreedteButton.getLocation().y + 
									  stroombreedteButton.getSize().height);
	
				// HIER !!
				stroombreedteMenu.setPopupSize(stroombreedteButton.getSize().width, 
											   stroombreedteMenu.getSize().height);
*/				
			}
			// stroombreedteMenu
			else if (e.getSource() == rbRelatiefItem)
			{	if (rbRelatiefItem.isSelected())
				{	drawingPanel.diagramManager.setEdgeThicknessMode(DrawingPanel.relMode);
				}
			}
			else if (e.getSource() == rbAbsoluutItem)
			{	if (rbAbsoluutItem.isSelected())
				{	drawingPanel.diagramManager.setEdgeThicknessMode(DrawingPanel.absMode);
				}
			}
			else if (e.getSource() == optiesButton)
			{
/*				
				optiesMenu.show(sdPanel, 
								optiesButton.getLocation().x, 
								optiesButton.getLocation().y + 
								optiesButton.getSize().height);

				// HIER !!
				//optiesMenu.setPopupSize(optiesButton.getSize().width, 
				//						optiesMenu.getSize().height);
*/								
			}
			// optiesMenu
			else if (e.getSource() == cbLabelsItem)
			{	if (cbLabelsItem.isSelected())
				{	drawingPanel.diagramManager.setVertexLabels(true);
				}
				else
				{	drawingPanel.diagramManager.setVertexLabels(false);
				}
			}
			else if (e.getSource() == addRootItem)
			{	drawingPanel.addNewRoot(true);
			}
		}
	}
	
}
