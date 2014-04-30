package fi.stroomdiagrammen;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;

import javax.swing.*;

import fi.beans.wiskopdrbeans.*;

public class SDInteractieEditPanel extends JPanel implements InteractieEditPanel,
																	ActionListener	
{	
	
	Stroomdiagrammen eigenaar;
	
	int editWidth = 190;
	int editHeight = 500; 
	int sdipBreedte = 500; // startbreedte sdip
	int sdipHoogte = 450; // starthoogte sdip
	
	Font theFont;
	FontMetrics theFM;
	Font theBoldFont;
	FontMetrics theBoldFM;
	
	int offset = 10;
	boolean componentsCreated = false;
	
	boolean noSetBounds = false;	

	protected SDInteractiePanel sdip;
	
	JCheckBox berekenMenuBox, breukenBox, stroomMenuBox, absoluutBox, optiesMenuBox, labelsBox, demoBox;
	JLabel bronnenLabel;
	JComboBox bronnenCombo;
	
	int aantalBronnen = 1;

	boolean bronnenComboEnabled = true;
	
	int scoreMax = 10;
	
	
	public SDInteractieEditPanel(Stroomdiagrammen eigenaar)
	{
		this.eigenaar = eigenaar;
		setLayout(null);
		sdip = new SDInteractiePanel(eigenaar);
		add(sdip);
		sdip.zetDocentModus();
		
		theFont = new Font("Dialog", Font.PLAIN, 12);
		theFM = getFontMetrics(theFont);
		theBoldFont = new Font("Dialog", Font.BOLD, 12);
		theBoldFM = getFontMetrics(theBoldFont);
		
		
		int width = editWidth - 3 * offset;
		int height = 3 * theFM.getHeight() / 2;
		int currentX = sdip.getSize().width + offset;
		int currentX2 = offset;
		int currentY = offset;
		
		berekenMenuBox = new JCheckBox(Stroomdiagrammen.rb.getString("berekenMenuText"), true);
		berekenMenuBox.setFont(theFont);
		berekenMenuBox.setBackground(Color.white);
		berekenMenuBox.setBounds(currentX, currentY, width, height);
		add(berekenMenuBox);
		berekenMenuBox.addActionListener(this);
		
		currentY += height + offset / 2;

		breukenBox = new JCheckBox(Stroomdiagrammen.rb.getString("berekenInBreukenText"), false);
		breukenBox.setFont(theFont);
		breukenBox.setBackground(Color.white);
		breukenBox.setBounds(currentX, currentY, width, height);
		add(breukenBox);
		breukenBox.addActionListener(this);
		
		currentY += height + 2 * offset;
		
		stroomMenuBox = new JCheckBox(Stroomdiagrammen.rb.getString("stroombreedteMenuText"), true);
		stroomMenuBox.setFont(theFont);
		stroomMenuBox.setBackground(Color.white);
		stroomMenuBox.setBounds(currentX, currentY, width, height);
		add(stroomMenuBox);
		stroomMenuBox.addActionListener(this);
		
		currentY += height + offset / 2;

		absoluutBox = new JCheckBox(Stroomdiagrammen.rb.getString("stroombreedteAbsoluutText"), false);
		absoluutBox.setFont(theFont);
		absoluutBox.setBackground(Color.white);
		absoluutBox.setBounds(currentX, currentY, width, height);
		add(absoluutBox);
		absoluutBox.addActionListener(this);
		
		currentY += height + 2 * offset;
		
		optiesMenuBox = new JCheckBox(Stroomdiagrammen.rb.getString("optiesMenuText"), true);
		optiesMenuBox.setFont(theFont);
		optiesMenuBox.setBackground(Color.white);
		optiesMenuBox.setBounds(currentX, currentY, width, height);
		add(optiesMenuBox);
		optiesMenuBox.addActionListener(this);
		
		currentY += height + offset / 2;

		labelsBox = new JCheckBox(Stroomdiagrammen.rb.getString("labelsText"), false);
		labelsBox.setFont(theFont);
		labelsBox.setBackground(Color.white);
		labelsBox.setBounds(currentX, currentY, width, height);
		add(labelsBox);
		labelsBox.addActionListener(this);
		
		currentY += height + offset;
		
		bronnenLabel = new JLabel(Stroomdiagrammen.rb.getString("aantalBronnenText"));
		bronnenLabel.setFont(theFont);
		bronnenLabel.setBackground(Color.white);
		width = theFM.stringWidth(bronnenLabel.getText());
		bronnenLabel.setBounds(currentX + offset, currentY + 3, width, theFM.getHeight());
		add(bronnenLabel);

		//currentY += height; // + offset;
		
		bronnenCombo = new JComboBox();
		bronnenCombo.setFont(theFont);
		bronnenCombo.setBackground(Color.white);
		width = theFM.stringWidth("XXXXXXXXX");
		bronnenCombo.addItem("1");
		bronnenCombo.addItem("2");
		bronnenCombo.addItem("3");
		bronnenCombo.addItem("4");
		bronnenCombo.setSelectedIndex(0);
		bronnenCombo.setBounds(//currentX + 2 * offset, currentY, width, height);
				bronnenLabel.getLocation().x + bronnenLabel.getSize().width + offset,
				currentY, width, height);
		add(bronnenCombo);
		bronnenCombo.addActionListener(this);
		
		currentY += height + 2 * offset;
		
		width = editWidth - 3 * offset;
		demoBox = new JCheckBox(Stroomdiagrammen.rb.getString("demoText"), false);
		demoBox.setFont(theFont);
		demoBox.setBackground(Color.white);
		demoBox.setBounds(currentX, currentY, width, height);
		add(demoBox);
		demoBox.addActionListener(this);
		
		componentsCreated = true;
	}	
	
	public void plaatsComponenten()
	{
		if (componentsCreated)
		{
			berekenMenuBox.setLocation(sdip.getSize().width + 2 * offset, berekenMenuBox.getLocation().y);
			breukenBox.setLocation(sdip.getSize().width + 2 * offset, breukenBox.getLocation().y);
			
			stroomMenuBox.setLocation(sdip.getSize().width + 2 * offset, stroomMenuBox.getLocation().y);
			absoluutBox.setLocation(sdip.getSize().width + 2 * offset, absoluutBox.getLocation().y);
			
			optiesMenuBox.setLocation(sdip.getSize().width + 2 * offset, optiesMenuBox.getLocation().y);
			labelsBox.setLocation(sdip.getSize().width + 2 * offset, labelsBox.getLocation().y);
			
			bronnenLabel.setLocation(sdip.getSize().width + 2 * offset, bronnenLabel.getLocation().y);

			bronnenCombo.setLocation(bronnenLabel.getLocation().x + bronnenLabel.getSize().width + offset, 
									 bronnenCombo.getLocation().y);
			demoBox.setLocation(sdip.getSize().width + 2 * offset, demoBox.getLocation().y);
		}
		
	}
	
	public void setEditState(Hashtable b)
	{
		
System.out.println("sdiep setEditState");
		boolean toonBerekeningenMenu = true;
		boolean berekenInBreuken = false;

		boolean toonStroombreedteMenu = true;
		boolean stroombreedteAbsoluut = false;

		boolean toonOptiesMenu = true;
		boolean toonLabels = false;
		int aantalBronnen = 1;
		
		boolean isDemo = false;

		if (b.containsKey("appletLaunchData"))
		{
System.out.println("aLD found");
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
			
			
		}
		
		berekenMenuBox.setSelected(toonBerekeningenMenu);
		breukenBox.setSelected(berekenInBreuken);
		stroomMenuBox.setSelected(toonStroombreedteMenu);
		absoluutBox.setSelected(stroombreedteAbsoluut);
		optiesMenuBox.setSelected(toonOptiesMenu);
		labelsBox.setSelected(toonLabels);
		bronnenComboEnabled = false;
		bronnenCombo.setSelectedIndex(aantalBronnen - 1);
		bronnenComboEnabled = true;
		demoBox.setSelected(isDemo);
		
		
		if (b.containsKey("sdipBreedte"))
			sdipBreedte = ((Integer) b.get("sdipBreedte")).intValue();
		if (b.containsKey("sdipHoogte"))
			sdipHoogte = ((Integer) b.get("sdipHoogte")).intValue();
		
		setBounds(getLocation().x, getLocation().y, sdipBreedte + editWidth, Math.max(sdipHoogte, editHeight));
		
		// HIER !!
		sdip.setEditState(b);		
		
	}
	
	public Hashtable getEditState()
	{
System.out.println("spiep getEditState");

		Hashtable h = sdip.getEditState(); 
		
		h.put("scoreMax", new Integer(scoreMax));
		
		h.put("sdipBreedte", new Integer(sdipBreedte));
		h.put("sdipHoogte", new Integer(sdipHoogte));
		
		return h;
		
	}
		
	public void setBounds(int x, int y, int b, int h)
	{
		if (noSetBounds)
		{	noSetBounds = false;
			return;
		}
//System.out.println("sdiep setBounds raw " + x + " " + y + " " + b + " " + h);

		if ((h <= 1) || (x < 0) || (b <= 1))
			return;
		
		super.setBounds(x, y, sdipBreedte + editWidth, Math.max(sdipHoogte, editHeight));
		
//System.out.println("sdiep setBounds " + x + " " + y + " " + (sdipBreedte + editWidth) + " " + 
//					Math.max(sdipHoogte, editHeight));
	
		if (sdip != null)
			sdip.setBounds(0, 0, sdipBreedte, sdipHoogte);
		
		plaatsComponenten();
		
//System.out.println("setBounds " + x + " " + y + " " + b + " " + h);		
		
	}
	
	public void zetBreedte(int b)
	{	
		sdipBreedte = b;
		
		setBounds(getLocation().x, getLocation().y, sdipBreedte + editWidth, Math.max(sdipHoogte, editHeight));		
		plaatsComponenten();
	}
	
	public void zetHoogte(int h)
	{	
		sdipHoogte = h;
		
		setBounds(getLocation().x, getLocation().y, sdipBreedte + editWidth, Math.max(sdipHoogte, editHeight));		
	}
	
	public void wis()
	{}
    
	public void zetMode(int mode)
	{}
	
    public void stop()
    {}
    
    public void start()
    {}
    
    public void addActionListener(ActionListener al)
    {}
    
	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == berekenMenuBox)
		{	
			sdip.zetToonBerekeningenMenu(berekenMenuBox.isSelected());
		}
		else if (e.getSource() == breukenBox)
		{
			sdip.zetBerekenInBreuken(breukenBox.isSelected());
		} 
		else if (e.getSource() == stroomMenuBox)
		{
			sdip.zetToonStroombreedteMenu(stroomMenuBox.isSelected());
		} 
		else if (e.getSource() == absoluutBox)
		{
			sdip.zetStroombreedteAbsoluut(absoluutBox.isSelected());
		} 
		else if (e.getSource() == optiesMenuBox)
		{
			sdip.zetToonOptiesMenu(optiesMenuBox.isSelected());
		} 
		else if (e.getSource() == labelsBox)
		{
			sdip.zetToonLabels(labelsBox.isSelected());
		} 
		else if (e.getSource() == bronnenCombo)
		{	if (!bronnenComboEnabled)
				return;
			
			sdip.zetAantalBronnen(bronnenCombo.getSelectedIndex() + 1);		
		} 
		
		else if (e.getSource() == demoBox)
		{
			sdip.zetIsDemo(demoBox.isSelected());
		} 

		
	}

}
