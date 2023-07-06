package fi.doorziendwo;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;

import javax.swing.*;

import fi.beans.wiskopdrbeans.*;


public class DoorzienInteractieEditPanel extends JPanel implements InteractieEditPanel, ActionListener
{
	
	int editWidth = 190;
	int editHeight = 500; 
	int dzipBreedte = 500; // startbreedte DoorzienInteractiePanel
	int dzipHoogte = 450; // starthoogte DoorzienInteractiePanel
	
	Font theFont;
	FontMetrics theFM;
	Font theBoldFont;
	FontMetrics theBoldFM;
	
	int offset = 10;
	boolean componentsCreated = false;
	
	boolean noSetBounds = false;	

	DoorzienInteractiePanel dzip;
	
	JLabel modusLabel, viewerOptiesLabel, doorzienOptiesLabel;
	JRadioButton viewerRadioButton, doorzienRadioButton;
	ButtonGroup modusGroup;
	
	// viewer
	JCheckBox rotateOptionCB, borderOptionCB, designOptionCB, resetOptionCB, foldOptionCB;
	// doorzien
	JCheckBox demoOptieCB, 
			  figurenMenuOptieCB, optiesMenuOptieCB, helpBarOptieCB, 
			  lijnTekenOptieCB, lijnVerlengOptieCB,
			  vlakTekenOptieCB, evenwijdigVlakOptieCB, toonDoorsnedeOptieCB, splitsFiguurOptieCB, 
			  bouwplaatOptieCB, previewOptieCB;
	
	;
	
	boolean viewerModus = false;
	
	// defaults at startup
	// worden niet gewijzigd
	// viewer
	boolean rotateOption = true;
	boolean borderOption = false;
	boolean designOption = false;
	boolean resetOption = false;
	boolean foldOption = false;

	// doorzien
	boolean demo = false;
	
	boolean figurenMenuOptie = true;
	boolean optiesMenuOptie = true;
	boolean helpBarOptie = true;
	
	boolean lijnTekenOptie = true;
	boolean lijnVerlengOptie = true;
	
	boolean vlakTekenOptie = true;
	boolean evenwijdigVlakOptie = true;
	boolean toonDoorsnedeOptie = true;
	boolean splitsFiguurOptie = true;
	
	boolean bouwplaatOptie = true;
	
	boolean previewOptie = false;
	
	public DoorzienInteractieEditPanel()
	{
		this(0, 0, 250, 250);
		
	}	
	public DoorzienInteractieEditPanel(int x, int y, int b, int h)
	{	
		setLayout(null);
	
		dzip = new DoorzienInteractiePanel();
		add(dzip);
		
		theFont = new Font("Dialog", Font.PLAIN, 12);
		theFM = getFontMetrics(theFont);
		theBoldFont = new Font("Dialog", Font.BOLD, 12);
		theBoldFM = getFontMetrics(theBoldFont);
		
		int width = editWidth - 2 * offset;
		int height = theFM.getHeight(); //3 * theFM.getHeight() / 2;
		int height2 = 3 * theBoldFM.getHeight() / 2;
		int currentX = dzip.getSize().width + offset;
		int currentY = offset / 2;
		
/*		
		modusLabel = new JLabel(Table.lookUp("EditPanel_modus"));
		modusLabel.setFont(theBoldFont);
		modusLabel.setBackground(getBackground());
		modusLabel.setBounds(currentX, currentY, width, height2);
		add(modusLabel);
		
		currentY += height2 + offset / 2;
*/
/*		
		modusGroup = new ButtonGroup();
		viewerRadioButton = new JRadioButton(Table.lookUp("EditPanel_viewer"), viewerModus);
		viewerRadioButton.setFont(theFont);
		viewerRadioButton.setBackground(getBackground());
		viewerRadioButton.setOpaque(false);
		viewerRadioButton.setBounds(currentX, currentY, width, height);
		modusGroup.add(viewerRadioButton);
		add(viewerRadioButton);
		viewerRadioButton.addActionListener(this);
		
		currentY += height + offset / 2;
*/
/*		
		doorzienRadioButton = new JRadioButton(Table.lookUp("EditPanel_doorzien"), !viewerModus);
		doorzienRadioButton.setFont(theFont);
		doorzienRadioButton.setBackground(getBackground());
		doorzienRadioButton.setOpaque(false);
		doorzienRadioButton.setBounds(currentX, currentY, width, height);
		modusGroup.add(doorzienRadioButton);
		add(doorzienRadioButton);
		doorzienRadioButton.addActionListener(this);
		
		currentY += height + offset / 2;
*/		
		
		viewerOptiesLabel = new JLabel(DoorzienDWO.rb.getString("EditPanel_vieweropties"));
		viewerOptiesLabel.setFont(theBoldFont);
		viewerOptiesLabel.setBackground(getBackground());
		viewerOptiesLabel.setBounds(currentX, currentY, width, height2);
		add(viewerOptiesLabel);
		
		currentY += height2 + offset / 2;
		
		rotateOptionCB = maakCheckBox(DoorzienDWO.rb.getString("EditPanel_draaibaar"), 
		        currentX, currentY, width, height, rotateOption);
		
		currentY += height + offset / 2;
		
		borderOptionCB = maakCheckBox(DoorzienDWO.rb.getString("EditPanel_Rand"), 
				currentX, currentY, width, height, borderOption);
		
		currentY += height + offset / 2;
		
		designOptionCB = maakCheckBox(DoorzienDWO.rb.getString("EditPanel_ontwerpmogelijkheid"), 
				currentX, currentY, width, height, designOption);
		designOptionCB.setEnabled(viewerModus);
		
		currentY += height + offset / 2;
		
		resetOptionCB = maakCheckBox(DoorzienDWO.rb.getString("EditPanel_reset-optie"), 
				currentX, currentY, width, height, resetOption);
		resetOptionCB.setEnabled(viewerModus);
		
		currentY += height + offset / 2;
		
		foldOptionCB = maakCheckBox(DoorzienDWO.rb.getString("EditPanel_vouwslider"), 
				currentX, currentY, width, height, foldOption);
		
		currentY += height + offset / 2;

		doorzienOptiesLabel = new JLabel(DoorzienDWO.rb.getString("EditPanel_doorzienopties"));
		doorzienOptiesLabel.setFont(theBoldFont);
		doorzienOptiesLabel.setBackground(getBackground());
		doorzienOptiesLabel.setBounds(currentX, currentY, width, height2);
		add(doorzienOptiesLabel);
		
		currentY += height2 + offset / 2;

		demoOptieCB = maakCheckBox(DoorzienDWO.rb.getString("EditPanel_demoOptie"), 
				  				   currentX, currentY, width, height, demo);
		currentY += height + offset;// / 2;
		
		figurenMenuOptieCB = maakCheckBox(DoorzienDWO.rb.getString("EditPanel_figurenMenuOptie"), 
										  currentX, currentY, width, height, figurenMenuOptie);
		currentY += height + offset / 2;
		
		optiesMenuOptieCB = maakCheckBox(DoorzienDWO.rb.getString("EditPanel_optiesMenuOptie"), 
				  currentX, currentY, width, height, optiesMenuOptie);
		currentY += height + offset / 2;
		
		helpBarOptieCB = maakCheckBox(DoorzienDWO.rb.getString("EditPanel_helpBarOptie"), 
				  currentX, currentY, width, height, helpBarOptie);
		currentY += height + offset;// / 2;

		lijnTekenOptieCB = maakCheckBox(DoorzienDWO.rb.getString("EditPanel_lijnTekenOptie"), 
				  currentX, currentY, width, height, lijnTekenOptie);
		currentY += height + offset / 2;

		lijnVerlengOptieCB = maakCheckBox(DoorzienDWO.rb.getString("EditPanel_lijnVerlengOptie"), 
				  currentX, currentY, width, height, lijnVerlengOptie);
		currentY += height + offset / 2;
		
		vlakTekenOptieCB = maakCheckBox(DoorzienDWO.rb.getString("EditPanel_vlakTekenOptie"), 
				  currentX, currentY, width, height, vlakTekenOptie);
		currentY += height + offset / 2;
		
		evenwijdigVlakOptieCB = maakCheckBox(DoorzienDWO.rb.getString("EditPanel_evenwijdigVlakOptie"), 
				  currentX, currentY, width, height, evenwijdigVlakOptie);
		currentY += height + offset / 2;

		toonDoorsnedeOptieCB = maakCheckBox(DoorzienDWO.rb.getString("EditPanel_toonDoorsnedeOptie"), 
				  currentX, currentY, width, height, toonDoorsnedeOptie);
		currentY += height + offset / 2;
		
		splitsFiguurOptieCB = maakCheckBox(DoorzienDWO.rb.getString("EditPanel_splitsFiguurOptie"), 
				  currentX, currentY, width, height, splitsFiguurOptie);
		currentY += height + offset / 2;

		bouwplaatOptieCB = maakCheckBox(DoorzienDWO.rb.getString("EditPanel_bouwplaatOptie"), 
				  currentX, currentY, width, height, bouwplaatOptie);
		currentY += height + offset;
		
		previewOptieCB = maakCheckBox(DoorzienDWO.rb.getString("EditPanel_previewOptie"), 
				  currentX, currentY, width, height, previewOptie);
		currentY += height + offset / 2;

		
		componentsCreated = true;
		
		plaatsComponenten();
		
		if (!viewerModus && !demo)
		{
			rotateOptionCB.setEnabled(false);
			borderOptionCB.setEnabled(false);
			designOptionCB.setEnabled(false);
			resetOptionCB.setEnabled(false);
			foldOptionCB.setEnabled(false);
			
		}
	}
	
	private JCheckBox maakCheckBox(String s, int x, int y, int b, int h, boolean selected)
	{	JCheckBox checkbox = new JCheckBox(s);
		checkbox.setBounds(x,y,b,h);
		checkbox.setOpaque(false);
		checkbox.setBackground(getBackground());
		checkbox.setFont(theFont);
		checkbox.setSelected(selected);
		checkbox.addActionListener(this);
		add(checkbox);
		
		return checkbox;
	}
	
	public void plaatsComponenten()
	{
		if (componentsCreated)
		{
			
			//modusLabel.setLocation(dzip.getSize().width + offset, modusLabel.getLocation().y);
			//viewerRadioButton.setLocation(dzip.getSize().width + offset, viewerRadioButton.getLocation().y);
			//doorzienRadioButton.setLocation(dzip.getSize().width + offset, doorzienRadioButton.getLocation().y);
			
			viewerOptiesLabel.setLocation(dzip.getSize().width + offset, viewerOptiesLabel.getLocation().y);
			rotateOptionCB.setLocation(dzip.getSize().width + offset, rotateOptionCB.getLocation().y);
			borderOptionCB.setLocation(dzip.getSize().width + offset, borderOptionCB.getLocation().y);
			designOptionCB.setLocation(dzip.getSize().width + offset, designOptionCB.getLocation().y);
			resetOptionCB.setLocation(dzip.getSize().width + offset, resetOptionCB.getLocation().y);
			foldOptionCB.setLocation(dzip.getSize().width + offset, foldOptionCB.getLocation().y);
			
			doorzienOptiesLabel.setLocation(dzip.getSize().width + offset, doorzienOptiesLabel.getLocation().y);
			
			demoOptieCB.setLocation(dzip.getSize().width + offset, demoOptieCB.getLocation().y);
			
			figurenMenuOptieCB.setLocation(dzip.getSize().width + offset, figurenMenuOptieCB.getLocation().y);
			optiesMenuOptieCB.setLocation(dzip.getSize().width + offset, optiesMenuOptieCB.getLocation().y);
			helpBarOptieCB.setLocation(dzip.getSize().width + offset, helpBarOptieCB.getLocation().y);
			
			lijnTekenOptieCB.setLocation(dzip.getSize().width + offset, lijnTekenOptieCB.getLocation().y);
			lijnVerlengOptieCB.setLocation(dzip.getSize().width + offset, lijnVerlengOptieCB.getLocation().y);
			vlakTekenOptieCB.setLocation(dzip.getSize().width + offset, vlakTekenOptieCB.getLocation().y);
			evenwijdigVlakOptieCB.setLocation(dzip.getSize().width + offset, evenwijdigVlakOptieCB.getLocation().y);
			toonDoorsnedeOptieCB.setLocation(dzip.getSize().width + offset, toonDoorsnedeOptieCB.getLocation().y);
			splitsFiguurOptieCB.setLocation(dzip.getSize().width + offset, splitsFiguurOptieCB.getLocation().y);
			bouwplaatOptieCB.setLocation(dzip.getSize().width + offset, bouwplaatOptieCB.getLocation().y);
			
			previewOptieCB.setLocation(dzip.getSize().width + offset, previewOptieCB.getLocation().y);
			
		}
	}
	
	public void resetViewerDemoOptions()
	{
		rotateOptionCB.setSelected(rotateOption);
		borderOptionCB.setSelected(borderOption);
		designOptionCB.setSelected(designOption);
		resetOptionCB.setSelected(resetOption);
		foldOptionCB.setSelected(foldOption);
		
		demoOptieCB.setSelected(false);
		dzip.zetDemo(false);
		
		dzip.setRotateOption(rotateOption);
		dzip.setBorderOption(borderOption);
		dzip.setDesignOption(designOption);
		dzip.setResetOption(resetOption);
		dzip.setFoldOption(foldOption);
	}
	
	public void setEditState(Hashtable h)
	{
//		String startFiguurString = null;
		boolean viewerModus = false;
		
		boolean rotateOption = true;
		boolean borderOption = false;
		boolean designOption = false;
		boolean resetOption = false;
		boolean foldOption = false;
		
		boolean demo = false;
		
		boolean figurenMenuOptie = true;
		boolean optiesMenuOptie = true;
		boolean helpBarOptie = true;
		
		boolean lijnTekenOptie = true;
		boolean lijnVerlengOptie = true;
		
		boolean vlakTekenOptie = true;
		boolean evenwijdigVlakOptie = true;
		boolean toonDoorsnedeOptie = true;
		boolean splitsFiguurOptie = true;
		
		boolean bouwplaatOptie = true;
		
		boolean previewOptie = false;
		
		if (h.containsKey("viewerModus")) 
			viewerModus = ((Boolean) h.get("viewerModus")).booleanValue();

		if (h.containsKey("rotateOption")) 
			rotateOption = ((Boolean)h.get("rotateOption")).booleanValue();
		if (h.containsKey("borderOption")) 
			borderOption = ((Boolean)h.get("borderOption")).booleanValue();
		if (h.containsKey("designOption")) 
			designOption = ((Boolean)h.get("designOption")).booleanValue();
		if (h.containsKey("resetOption")) 
			resetOption = ((Boolean)h.get("resetOption")).booleanValue();
		if (h.containsKey("foldOption")) 
			foldOption = ((Boolean)h.get("foldOption")).booleanValue();
	    
		if (h.containsKey("demo"))
			demo = ((Boolean) h.get("demo")).booleanValue();
		
		if (h.containsKey("figurenMenuOptie"))
			figurenMenuOptie = ((Boolean) h.get("figurenMenuOptie")).booleanValue();
		if (h.containsKey("optiesMenuOptie"))
			optiesMenuOptie = ((Boolean) h.get("optiesMenuOptie")).booleanValue();
		if (h.containsKey("helpBarOptie"))
			helpBarOptie = ((Boolean) h.get("helpBarOptie")).booleanValue();

		if (h.containsKey("lijnTekenOptie"))
			lijnTekenOptie = ((Boolean) h.get("lijnTekenOptie")).booleanValue();
		if (h.containsKey("lijnVerlengOptie"))
			lijnVerlengOptie = ((Boolean) h.get("lijnVerlengOptie")).booleanValue();

		if (h.containsKey("vlakTekenOptie"))
			vlakTekenOptie = ((Boolean) h.get("vlakTekenOptie")).booleanValue();
		if (h.containsKey("evenwijdigVlakOptie"))
			evenwijdigVlakOptie = ((Boolean) h.get("evenwijdigVlakOptie")).booleanValue();
		if (h.containsKey("toonDoorsnedeOptie"))
			toonDoorsnedeOptie = ((Boolean) h.get("toonDoorsnedeOptie")).booleanValue();
		if (h.containsKey("splitsFiguurOptie"))
			splitsFiguurOptie = ((Boolean) h.get("splitsFiguurOptie")).booleanValue();
		
		if (h.containsKey("bouwplaatOptie"))
			bouwplaatOptie = ((Boolean) h.get("bouwplaatOptie")).booleanValue();
		
		if (h.containsKey("previewOptie"))
			previewOptie = ((Boolean) h.get("previewOptie")).booleanValue();
		
/*		
		if (viewerModus)
		{	viewerRadioButton.setSelected(true);

			rotateOptionCB.setEnabled(true);
			borderOptionCB.setEnabled(true);
			designOptionCB.setEnabled(true);
			resetOptionCB.setEnabled(true);
			foldOptionCB.setEnabled(true);

		}
		else
		{	doorzienRadioButton.setSelected(true);
*/		
			rotateOptionCB.setEnabled(demo);
			borderOptionCB.setEnabled(demo);
			designOptionCB.setEnabled(demo);
			resetOptionCB.setEnabled(demo);
			foldOptionCB.setEnabled(demo);
			

//		}
		
		
		rotateOptionCB.setSelected(rotateOption);
		borderOptionCB.setSelected(borderOption);
		designOptionCB.setSelected(designOption);
		resetOptionCB.setSelected(resetOption);
		foldOptionCB.setSelected(foldOption);
		
		demoOptieCB.setSelected(demo);
		
		figurenMenuOptieCB.setSelected(figurenMenuOptie);
		optiesMenuOptieCB.setSelected(optiesMenuOptie);
		helpBarOptieCB.setSelected(helpBarOptie);
		
		lijnTekenOptieCB.setSelected(lijnTekenOptie);
		lijnVerlengOptieCB.setSelected(lijnVerlengOptie);
		lijnVerlengOptieCB.setEnabled(lijnTekenOptieCB.isSelected());
		if (!lijnTekenOptieCB.isSelected())
			lijnVerlengOptieCB.setSelected(false);
		
		vlakTekenOptieCB.setSelected(vlakTekenOptie);
		evenwijdigVlakOptieCB.setSelected(evenwijdigVlakOptie);
		toonDoorsnedeOptieCB.setSelected(toonDoorsnedeOptie);
		splitsFiguurOptieCB.setSelected(splitsFiguurOptie);

		evenwijdigVlakOptieCB.setEnabled(vlakTekenOptieCB.isSelected());
		toonDoorsnedeOptieCB.setEnabled(vlakTekenOptieCB.isSelected());
		splitsFiguurOptieCB.setEnabled(vlakTekenOptieCB.isSelected());
		if (!vlakTekenOptieCB.isSelected())
		{
			evenwijdigVlakOptieCB.setSelected(false);
			toonDoorsnedeOptieCB.setSelected(false);
			splitsFiguurOptieCB.setSelected(false);
		}
		
		bouwplaatOptieCB.setSelected(bouwplaatOptie);
		
		previewOptieCB.setSelected(previewOptie);
		
		
		if (h.containsKey("dzipBreedte"))
			dzipBreedte = ((Integer) h.get("dzipBreedte")).intValue();
		if (h.containsKey("dzipHoogte"))
			dzipHoogte = ((Integer) h.get("dzipHoogte")).intValue();
		
		setBounds(getLocation().x, getLocation().y, dzipBreedte + editWidth, Math.max(dzipHoogte, editHeight));
		
		// HIER !!
		dzip.setEditState(h);		
		
	}
	
	public Hashtable getEditState()
	{
		
		Hashtable h = dzip.getEditState();
		
		h.put("dzipBreedte", new Integer(dzipBreedte));
		h.put("dzipHoogte", new Integer(dzipHoogte));

		
		return h;
	}
		
	public void setBounds(int x, int y, int b, int h)
	{	
		if (noSetBounds)
		{	noSetBounds = false;
			return;
		}
//System.out.println("spiep setBounds raw " + x + " " + y + " " + b + " " + h);

		if ((h <= 1) || (x < 0) || (b <= 1))
			return;
		
		super.setBounds(x, y, dzipBreedte + editWidth, Math.max(dzipHoogte, editHeight));
		
//System.out.println("spiep setBounds " + x + " " + y + " " + (spipBreedte + editWidth) + " " + 
//					Math.max(spipHoogte, editHeight));
	
		if (dzip != null)
			dzip.setBounds(0, 0, dzipBreedte, dzipHoogte);
		
		plaatsComponenten();
		
//System.out.println("setBounds " + x + " " + y + " " + b + " " + h);		

	}
	
	public void zetBreedte(int b)
	{
		dzipBreedte = b;
		
		setBounds(getLocation().x, getLocation().y, dzipBreedte + editWidth, Math.max(dzipHoogte, editHeight));		
		plaatsComponenten();

	}
	
	public void zetHoogte(int h)
	{
		dzipHoogte = h;
		
		setBounds(getLocation().x, getLocation().y, dzipBreedte + editWidth, Math.max(dzipHoogte, editHeight));		

	}
	
	public void wis(){}
    
	public void zetMode(int mode){}
	
    public void stop(){}
    
    public void start(){}
    
    public void addActionListener(ActionListener al){}
    
	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource().equals(rotateOptionCB))
		{	
			dzip.setRotateOption(rotateOptionCB.isSelected());
		}
		if (e.getSource().equals(borderOptionCB))
		{	
			dzip.setBorderOption(borderOptionCB.isSelected());
		}
		if (e.getSource().equals(designOptionCB))
		{	
			dzip.setDesignOption(designOptionCB.isSelected());
		}
		if (e.getSource().equals(resetOptionCB))
		{	
			dzip.setResetOption(resetOptionCB.isSelected());
		}
		if (e.getSource().equals(foldOptionCB))
		{	
			dzip.setFoldOption(foldOptionCB.isSelected());
		}
		
		if (e.getSource().equals(viewerRadioButton))
		{	
			dzip.setViewerModus(viewerRadioButton.isSelected(), true);
			
			resetViewerDemoOptions();
			
			rotateOptionCB.setEnabled(true);
			borderOptionCB.setEnabled(true);
			designOptionCB.setEnabled(true);
			resetOptionCB.setEnabled(true);
			foldOptionCB.setEnabled(true);
			
		}
		if (e.getSource().equals(doorzienRadioButton))
		{	
			dzip.setViewerModus(!doorzienRadioButton.isSelected(), true);
			
// tijdelijk
//dzip.viewerObjectNaarDoorzien();			
			
			resetViewerDemoOptions();
			
			
			boolean demoSelected = demoOptieCB.isSelected();
			
			rotateOptionCB.setEnabled(demoSelected);
			borderOptionCB.setEnabled(demoSelected);
			designOptionCB.setEnabled(demoSelected);
			resetOptionCB.setEnabled(demoSelected);				
			foldOptionCB.setEnabled(demoSelected);
				

		}

		if (e.getSource().equals(demoOptieCB))
		{	dzip.zetDemo(demoOptieCB.isSelected());
		
			boolean demoSelected = demoOptieCB.isSelected();
		
			rotateOptionCB.setEnabled(demoSelected);
			borderOptionCB.setEnabled(demoSelected);
			designOptionCB.setEnabled(demoSelected);
			resetOptionCB.setEnabled(demoSelected);				
			foldOptionCB.setEnabled(demoSelected);
		
			
		}
		
		if (e.getSource().equals(figurenMenuOptieCB))
		{	
			dzip.zetFigurenMenuOptie(figurenMenuOptieCB.isSelected());
		}
		if (e.getSource().equals(optiesMenuOptieCB))
		{	
			dzip.zetOptiesMenuOptie(optiesMenuOptieCB.isSelected());
		}
		if (e.getSource().equals(helpBarOptieCB))
		{	
			dzip.zetHelpBarOptie(helpBarOptieCB.isSelected());
		}
		if (e.getSource().equals(helpBarOptieCB))
		{	
			dzip.zetHelpBarOptie(helpBarOptieCB.isSelected());
		}

		if (e.getSource().equals(lijnTekenOptieCB))
		{
			lijnVerlengOptieCB.setEnabled(lijnTekenOptieCB.isSelected());
			dzip.zetLijnTekenOptie(lijnTekenOptieCB.isSelected());
			if (!lijnTekenOptieCB.isSelected())
				lijnVerlengOptieCB.setSelected(false);
		}
		if (e.getSource().equals(lijnVerlengOptieCB))
		{	
			dzip.zetLijnVerlengOptie(lijnVerlengOptieCB.isSelected());
		}
		
		if (e.getSource().equals(vlakTekenOptieCB))
		{	
			evenwijdigVlakOptieCB.setEnabled(vlakTekenOptieCB.isSelected());
			toonDoorsnedeOptieCB.setEnabled(vlakTekenOptieCB.isSelected());
			splitsFiguurOptieCB.setEnabled(vlakTekenOptieCB.isSelected());
			dzip.zetVlakTekenOptie(vlakTekenOptieCB.isSelected());
			if (!vlakTekenOptieCB.isSelected())
			{
				evenwijdigVlakOptieCB.setSelected(false);
				toonDoorsnedeOptieCB.setSelected(false);
				splitsFiguurOptieCB.setSelected(false);
			}
		}
		if (e.getSource().equals(evenwijdigVlakOptieCB))
		{	
			dzip.zetEvenwijdigVlakOptie(evenwijdigVlakOptieCB.isSelected());
		}
		if (e.getSource().equals(toonDoorsnedeOptieCB))
		{	
			dzip.zetToonDoorsnedeOptie(toonDoorsnedeOptieCB.isSelected());
		}
		if (e.getSource().equals(splitsFiguurOptieCB))
		{	
			dzip.zetSplitsFiguurOptie(splitsFiguurOptieCB.isSelected());
		}

		if (e.getSource().equals(bouwplaatOptieCB))
		{	
			dzip.zetBouwplaatOptie(bouwplaatOptieCB.isSelected());
		}
		if (e.getSource().equals(previewOptieCB))
		{	
			dzip.zetPreviewOptie(previewOptieCB.isSelected());
		}
		
		
		
		
	}
}
