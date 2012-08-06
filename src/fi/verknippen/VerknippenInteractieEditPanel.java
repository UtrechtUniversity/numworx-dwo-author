package fi.verknippen;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Hashtable;

import javax.swing.*;

import fi.beans.wiskopdrbeans.*;

public class VerknippenInteractieEditPanel extends JPanel implements InteractieEditPanel,
																	ActionListener	
{	
	int editWidth = 190;
	int editHeight = 500; 
	int vipBreedte = 500; // startbreedte vip
	int vipHoogte = 450; // starthoogte vip
	
	Font theFont;
	FontMetrics theFM;
	Font theBoldFont;
	FontMetrics theBoldFM;
	
	int offset = 10;
	boolean componentsCreated = false;
	
	boolean noSetBounds = false;	

	protected VerknippenInteractiePanel vip;

	JLabel taakNummerLabel, gridSizeLabel, oppervlakteRoodLabel, oppervlakteGrijsLabel, scoreMaxLabel;
	JTextField gridSizeVeld, oppervlakteRoodVeld, oppervlakteGrijsVeld, scoreMaxVeld;
	JCheckBox balkOnderaanBox, roosterZichtbaarBox, groteBalletjesBox, schaduwZichtbaarBox, afmetingenZichtbaarBox;
	JCheckBox tekenGumOptieBox;
	
	JComboBox taakNummerCombo;
	
	JButton maakRodeFiguurButton, maakGrijzeFiguurButton;

	int taakNummer = 1;
	int gridSize = 20;
	int minGrid = 16;
	int maxGrid = 50;
	String rodeFiguurInput = "";
	String grijzeFiguurInput = "";
	int oppervlakteRood = 64;
	int oppervlakteGrijs = 64;
	int scoreMax = 10;
	
	FiguurInputPanel rodeFiguurPanel, grijzeFiguurPanel;
	
	boolean taakNummerComboEnabled = true; 
	
	public VerknippenInteractieEditPanel()
	{
		setLayout(null);
		vip = new VerknippenInteractiePanel();
		add(vip);
		
		theFont = new Font("Dialog", Font.PLAIN, 12);
		theFM = getFontMetrics(theFont);
		theBoldFont = new Font("Dialog", Font.BOLD, 12);
		theBoldFM = getFontMetrics(theBoldFont);
		
		rodeFiguurInput = vip.rodeFiguurString;
		rodeFiguurPanel = new FiguurInputPanel(10, 10, 360, 69, true);
		rodeFiguurPanel.viep = this;
		rodeFiguurPanel.zetFiguurString(rodeFiguurInput);
		rodeFiguurPanel.setVisible(false);
		//add(rodeFiguurPanel, 0);
		rodeFiguurPanel.closeButton.addActionListener(new CloseFiguurInputAL(true));
		
		grijzeFiguurInput = vip.grijzeFiguurString;
		grijzeFiguurPanel = new FiguurInputPanel(10, 10, 360, 69, false);
		grijzeFiguurPanel.viep = this;		
		grijzeFiguurPanel.zetFiguurString(grijzeFiguurInput);
		grijzeFiguurPanel.setVisible(false);
		//add(grijzeFiguurPanel, 0);
		grijzeFiguurPanel.closeButton.addActionListener(new CloseFiguurInputAL(false));
		
		
		int width = editWidth - 3 * offset;
		int height = 3 * theFM.getHeight() / 2;
		int currentX = vip.getSize().width + offset;
		int currentX2 = offset;
		int currentY = offset;
		
		taakNummerLabel = new JLabel(Verknippen.rb.getString("taakNummerTekst"));
		taakNummerLabel.setFont(theFont);
		taakNummerLabel.setBackground(Color.white);
		width = theFM.stringWidth(taakNummerLabel.getText());
		taakNummerLabel.setBounds(currentX + offset, currentY + 3, width, theFM.getHeight());
		add(taakNummerLabel);

		//currentY += height; // + offset;
		
		taakNummerCombo = new JComboBox();
		taakNummerCombo.setFont(theFont);
		taakNummerCombo.setBackground(Color.white);
		width = theFM.stringWidth("XXXXXXXXX");
		taakNummerCombo.addItem("0");
		taakNummerCombo.addItem("1");
		taakNummerCombo.addItem("2");
		taakNummerCombo.addItem("3");
		taakNummerCombo.addItem("4");
		taakNummerCombo.setSelectedIndex(1);
		taakNummerCombo.setBounds(//currentX + 2 * offset, currentY, width, height);
				taakNummerLabel.getLocation().x + taakNummerLabel.getSize().width + offset,
				currentY, width, height);
		add(taakNummerCombo);
		taakNummerCombo.addActionListener(this);

/*		
		taakNummerVeld = new JTextField("" + taakNummer);
		taakNummerVeld.setFont(theFont);
		taakNummerVeld.setBackground(Color.white);
		width = theFM.stringWidth("XXXXXX");
		taakNummerVeld.setBounds(//currentX + 2 * offset, currentY, width, height);
				taakNummerLabel.getLocation().x + taakNummerLabel.getSize().width + offset,
				currentY, width, height);
		add(taakNummerVeld);
		
		taakNummerVeld.addKeyListener(new InputKL2(taakNummerVeld));
		taakNummerVeld.addActionListener(new TextAL2(taakNummerVeld));
		taakNummerVeld.addFocusListener(new TextFL2(taakNummerVeld));
*/
		currentY += height + offset;
		
		width = editWidth - 3 * offset;
		balkOnderaanBox = new JCheckBox(Verknippen.rb.getString("balkOnderaanTekst"), true);
		balkOnderaanBox.setFont(theFont);
		balkOnderaanBox.setBackground(Color.white);
		balkOnderaanBox.setBounds(currentX, currentY, width, height);
		add(balkOnderaanBox);
		balkOnderaanBox.addActionListener(this);
		
		currentY += height + offset / 2;

		roosterZichtbaarBox = new JCheckBox(Verknippen.rb.getString("roosterZichtbaarTekst"), false);
		roosterZichtbaarBox.setFont(theFont);
		roosterZichtbaarBox.setBackground(Color.white);
		roosterZichtbaarBox.setBounds(currentX, currentY, width, height);
		add(roosterZichtbaarBox);
		roosterZichtbaarBox.addActionListener(this);
		
		currentY += height + offset / 2;

		groteBalletjesBox = new JCheckBox(Verknippen.rb.getString("groteBalletjesTekst"), false);
		groteBalletjesBox.setFont(theFont);
		groteBalletjesBox.setBackground(Color.white);
		groteBalletjesBox.setBounds(currentX, currentY, width, height);
		add(groteBalletjesBox);
		groteBalletjesBox.addActionListener(this);
		
		currentY += height + offset / 2;
		
		schaduwZichtbaarBox = new JCheckBox(Verknippen.rb.getString("schaduwZichtbaarTekst"), false);
		schaduwZichtbaarBox.setFont(theFont);
		schaduwZichtbaarBox.setBackground(Color.white);
		schaduwZichtbaarBox.setBounds(currentX, currentY, width, height);
		add(schaduwZichtbaarBox);
		schaduwZichtbaarBox.addActionListener(this);
		
		currentY += height + offset / 2;
		
		afmetingenZichtbaarBox = new JCheckBox(Verknippen.rb.getString("afmetingenZichtbaarTekst"), false);
		afmetingenZichtbaarBox.setFont(theFont);
		afmetingenZichtbaarBox.setBackground(Color.white);
		afmetingenZichtbaarBox.setBounds(currentX, currentY, width, height);
		add(afmetingenZichtbaarBox);
		afmetingenZichtbaarBox.addActionListener(this);
		
		currentY += height + offset;

		tekenGumOptieBox = new JCheckBox(Verknippen.rb.getString("rechthoekenTekenenTekst"), false);
		tekenGumOptieBox.setFont(theFont);
		tekenGumOptieBox.setBackground(Color.white);
		tekenGumOptieBox.setBounds(currentX, currentY, width, height);
		add(tekenGumOptieBox);
		tekenGumOptieBox.addActionListener(this);
		
		currentY += height + offset;
		
		gridSizeLabel = new JLabel(Verknippen.rb.getString("gridInPixelsTekst"));
		gridSizeLabel.setFont(theFont);
		gridSizeLabel.setBackground(Color.white);
		width = theFM.stringWidth(gridSizeLabel.getText());
		gridSizeLabel.setBounds(currentX + offset, currentY + 3, width, theFM.getHeight());
		add(gridSizeLabel);

		//currentY += height; // + offset;
		
		gridSizeVeld = new JTextField("" + gridSize);
		gridSizeVeld.setFont(theFont);
		gridSizeVeld.setBackground(Color.white);
		width = theFM.stringWidth("XXXXXX");
		gridSizeVeld.setBounds(//currentX + 2 * offset, currentY, width, height);
				gridSizeLabel.getLocation().x + gridSizeLabel.getSize().width + offset,
				currentY, width, height);
		add(gridSizeVeld);
		
		gridSizeVeld.addKeyListener(new InputKL2(gridSizeVeld));
		gridSizeVeld.addActionListener(new TextAL2(gridSizeVeld));
		gridSizeVeld.addFocusListener(new TextFL2(gridSizeVeld));
		
		currentY += height + 3 * offset / 2;

		width = editWidth - 3 * offset;
		maakRodeFiguurButton = new JButton(Verknippen.rb.getString("maakRodeFiguurTekst"));
		maakRodeFiguurButton.setFont(theFont);
		//maakRodeFiguurButton.setBackground(Color.white);
		maakRodeFiguurButton.setBounds(currentX, currentY, width, height);
		add(maakRodeFiguurButton);
		maakRodeFiguurButton.addActionListener(this);
		
		currentY += height + 3 * offset / 2;
		
		oppervlakteRoodLabel = new JLabel(Verknippen.rb.getString("oppervlakteRoodTekst"));
		oppervlakteRoodLabel.setFont(theFont);
		oppervlakteRoodLabel.setBackground(Color.white);
		width = theFM.stringWidth(oppervlakteRoodLabel.getText());
		oppervlakteRoodLabel.setBounds(currentX + offset, currentY + 3, width, theFM.getHeight());
		oppervlakteRoodLabel.setEnabled(false);
		add(oppervlakteRoodLabel);

		//currentY += height; // + offset;
		
		oppervlakteRoodVeld = new JTextField("" + oppervlakteRood);
		oppervlakteRoodVeld.setFont(theFont);
		oppervlakteRoodVeld.setBackground(Color.white);
		width = theFM.stringWidth("XXXXXX");
		oppervlakteRoodVeld.setBounds(//currentX + 2 * offset, currentY, width, height);
				oppervlakteRoodLabel.getLocation().x + oppervlakteRoodLabel.getSize().width + offset,
				currentY, width, height);
		oppervlakteRoodVeld.setEnabled(false);
		add(oppervlakteRoodVeld);
		
		oppervlakteRoodVeld.addKeyListener(new InputKL2(oppervlakteRoodVeld));
		oppervlakteRoodVeld.addActionListener(new TextAL2(oppervlakteRoodVeld));
		oppervlakteRoodVeld.addFocusListener(new TextFL2(oppervlakteRoodVeld));
		
		currentY += height + 3 * offset / 2;

		width = editWidth - 3 * offset;
		maakGrijzeFiguurButton = new JButton(Verknippen.rb.getString("maakGrijzeFiguurTekst"));
		maakGrijzeFiguurButton.setFont(theFont);
		maakGrijzeFiguurButton.setBounds(currentX, currentY, width, height);
		maakGrijzeFiguurButton.setEnabled(false);
		add(maakGrijzeFiguurButton);
		maakGrijzeFiguurButton.addActionListener(this);
		
		currentY += height + 3 * offset / 2;
		
		oppervlakteGrijsLabel = new JLabel(Verknippen.rb.getString("oppervlakteGrijsTekst"));
		oppervlakteGrijsLabel.setFont(theFont);
		oppervlakteGrijsLabel.setBackground(Color.white);
		width = theFM.stringWidth(oppervlakteGrijsLabel.getText());
		oppervlakteGrijsLabel.setBounds(currentX + offset, currentY + 3, width, theFM.getHeight());
		oppervlakteGrijsLabel.setEnabled(false);
		add(oppervlakteGrijsLabel);

		//currentY += height; // + offset;
		
		oppervlakteGrijsVeld = new JTextField("" + oppervlakteGrijs);
		oppervlakteGrijsVeld.setFont(theFont);
		oppervlakteGrijsVeld.setBackground(Color.white);
		width = theFM.stringWidth("XXXXXX");
		oppervlakteGrijsVeld.setBounds(//currentX + 2 * offset, currentY, width, height);
				oppervlakteGrijsLabel.getLocation().x + oppervlakteGrijsLabel.getSize().width + offset,
				currentY, width, height);
		oppervlakteGrijsVeld.setEnabled(false);
		add(oppervlakteGrijsVeld);
		
		oppervlakteGrijsVeld.addKeyListener(new InputKL2(oppervlakteGrijsVeld));
		oppervlakteGrijsVeld.addActionListener(new TextAL2(oppervlakteGrijsVeld));
		oppervlakteGrijsVeld.addFocusListener(new TextFL2(oppervlakteGrijsVeld));
		
		currentY += height + offset;
		
		scoreMaxLabel = new JLabel(Verknippen.rb.getString("scoreMaxTekst"));
		scoreMaxLabel.setFont(theFont);
		scoreMaxLabel.setBackground(Color.white);
		width = theFM.stringWidth(scoreMaxLabel.getText());
		scoreMaxLabel.setBounds(currentX + offset, currentY + 3, width, theFM.getHeight());
		//maxScoreLabel.setVisible(false);
		add(scoreMaxLabel);
		
		currentY += height; // + offset;
		
		scoreMaxVeld = new JTextField("" + scoreMax);
		scoreMaxVeld.setFont(theFont);
		scoreMaxVeld.setBackground(Color.white);
		width = theFM.stringWidth("XXXXXX");
		scoreMaxVeld.setBounds(currentX + 2 * offset, currentY, width, height);
				//maxScoreLabel.getLocation().x + maxScoreLabel.getSize().width + offset,
				//currentY, width, height);
		add(scoreMaxVeld);
		//maxScoreVeld.setVisible(false);

		scoreMaxVeld.addKeyListener(new InputKL2(scoreMaxVeld));
		scoreMaxVeld.addActionListener(new TextAL2(scoreMaxVeld));
		scoreMaxVeld.addFocusListener(new TextFL2(scoreMaxVeld));
		
		componentsCreated = true;
		
	}	
	
	public void plaatsComponenten()
	{
		if (componentsCreated)
		{	
			taakNummerLabel.setLocation(vip.getSize().width + 2 * offset, taakNummerLabel.getLocation().y);

			taakNummerCombo.setLocation(taakNummerLabel.getLocation().x + taakNummerLabel.getSize().width + offset, 
									    taakNummerCombo.getLocation().y);

//			taakNummerVeld.setLocation(taakNummerLabel.getLocation().x + taakNummerLabel.getSize().width + offset, 
//			   taakNummerVeld.getLocation().y);
			
			balkOnderaanBox.setLocation(vip.getSize().width + 2 * offset, balkOnderaanBox.getLocation().y);
			roosterZichtbaarBox.setLocation(vip.getSize().width + 2 * offset, roosterZichtbaarBox.getLocation().y);
			groteBalletjesBox.setLocation(vip.getSize().width + 2 * offset, groteBalletjesBox.getLocation().y);
			schaduwZichtbaarBox.setLocation(vip.getSize().width + 2 * offset, schaduwZichtbaarBox.getLocation().y);
			afmetingenZichtbaarBox.setLocation(vip.getSize().width + 2 * offset, afmetingenZichtbaarBox.getLocation().y);
			tekenGumOptieBox.setLocation(vip.getSize().width + 2 * offset, tekenGumOptieBox.getLocation().y);

			gridSizeLabel.setLocation(vip.getSize().width + 2 * offset, gridSizeLabel.getLocation().y);
			//gridSizeVeld.setLocation(vip.getSize().width + 3 * offset, gridSizeVeld.getLocation().y);
			gridSizeVeld.setLocation(gridSizeLabel.getLocation().x + gridSizeLabel.getSize().width + offset, 
									 gridSizeVeld.getLocation().y);

			maakRodeFiguurButton.setLocation(vip.getSize().width + 2 * offset, maakRodeFiguurButton.getLocation().y);
			
			oppervlakteRoodLabel.setLocation(vip.getSize().width + 2 * offset, oppervlakteRoodLabel.getLocation().y);
			//oppervlakteRoodVeld.setLocation(vip.getSize().width + 3 * offset, oppervlakteRood.getLocation().y);
			oppervlakteRoodVeld.setLocation(oppervlakteRoodLabel.getLocation().x + oppervlakteRoodLabel.getSize().width + offset, 
											oppervlakteRoodVeld.getLocation().y);

			maakGrijzeFiguurButton.setLocation(vip.getSize().width + 2 * offset, maakGrijzeFiguurButton.getLocation().y);
			
			oppervlakteGrijsLabel.setLocation(vip.getSize().width + 2 * offset, oppervlakteGrijsLabel.getLocation().y);
			//oppervlakteGrijsVeld.setLocation(vip.getSize().width + 3 * offset, oppervlakteGrijs.getLocation().y);
			oppervlakteGrijsVeld.setLocation(oppervlakteGrijsLabel.getLocation().x + oppervlakteGrijsLabel.getSize().width + offset, 
											 oppervlakteGrijsVeld.getLocation().y);
			
			scoreMaxLabel.setLocation(vip.getSize().width + 2 * offset, scoreMaxLabel.getLocation().y);
			scoreMaxVeld.setLocation(vip.getSize().width + 3 * offset, scoreMaxVeld.getLocation().y);

			
			repaint();
						
		}

	}
	
	public void zetTaakNummer(int taakNum)
	{	taakNummer = taakNum;
		vip.zetTaakNummer(taakNummer, true);
	}

	
	public void zetGridSize(int gSize)
	{	gridSize = gSize;
		vip.zetGridSize(gridSize);
	}
	
	public void maakRodeFiguur()
	{	String rodeFiguurString = rodeFiguurPanel.getCorrectFiguurString();
		if (!rodeFiguurString.equals(""))
		{	vip.zetRodeFiguur(rodeFiguurString);
		}
		
	}

	public void maakGrijzeFiguur()
	{
		String grijzeFiguurString = grijzeFiguurPanel.getCorrectFiguurString();
		if (!grijzeFiguurString.equals(""))
		{	vip.zetGrijzeFiguur(grijzeFiguurString);
		}
		
	}
	
	public void zetOppervlakteRood(int oRood)
	{	oppervlakteRood = oRood;
		vip.zetOppervlakteRood(oRood);
	}
	
	public void zetOppervlakteGrijs(int oGrijs)
	{	oppervlakteRood = oGrijs;
		vip.zetOppervlakteGrijs(oGrijs);
	}
	
	public void setEditState(Hashtable b)
	{
		
System.out.println("viep setEditState");

		int taakNummer = 1;
		boolean balkOnderaan = true;
		boolean roosterZichtbaar = false;
		boolean groteBalletjes = false;
		boolean schaduwZichtbaar = false;
		boolean afmetingenZichtbaar = false;
		boolean tekenGumOptie = false; 
		int gridSize = 20;		
	   	String rodeFiguurInput =  "2,0|10,0|8,8|0,8";
		int oppervlakteRood = 64;    	
		String grijzeFiguurInput = "0,0|8,0|8,8|0,8";
		int oppervlakteGrijs = 64;
		int scoreMax = 10;
		
		if (b.containsKey("appletLaunchData"))
		{
System.out.println("aLD found");
			Hashtable appletLaunchData = (Hashtable) b.get("appletLaunchData");
			
			String balkOnderaanString = "true";
			String roosterZichtbaarString = "false";
			String groteBalletjesString = "false";
			String schaduwZichtbaarString = "false";
			String afmetingenZichtbaarString = "false";

			String grid1String = "";
			String figuur1String = "";
			String oppervlakte1String = "";
			String figuurgrijs1String = "";
			String oppervlaktegrijs1String = "";
			String scoreMaxString = "";
			
			boolean error;
			
			if (appletLaunchData.containsKey("taaknummer"))
				taakNummer = Integer.parseInt((String) appletLaunchData.get("taaknummer"));
			if (appletLaunchData.containsKey("toonbalk"))
				balkOnderaanString = (String) appletLaunchData.get("toonbalk");
			if (balkOnderaanString.equals("false") || balkOnderaanString.equals("no"))
				balkOnderaan = false;
			if (appletLaunchData.containsKey("toonrooster"))
				roosterZichtbaarString = (String) appletLaunchData.get("toonrooster");
			if (roosterZichtbaarString.equals("true") || roosterZichtbaarString.equals("yes"))
				roosterZichtbaar = true;
			if (appletLaunchData.containsKey("groteballetjes"))
				groteBalletjesString = (String) appletLaunchData.get("groteballetjes");
			if (groteBalletjesString.equals("true") || groteBalletjesString.equals("yes"))
				groteBalletjes = true;
			if (appletLaunchData.containsKey("toonschaduw"))
				schaduwZichtbaarString = (String) appletLaunchData.get("toonschaduw");
			if (schaduwZichtbaarString.equals("true") || schaduwZichtbaarString.equals("yes"))
				schaduwZichtbaar = true;
			if (appletLaunchData.containsKey("toonafmetingen"))
				afmetingenZichtbaarString = (String) appletLaunchData.get("toonafmetingen");
			if (afmetingenZichtbaarString.equals("true") || afmetingenZichtbaarString.equals("yes"))
				afmetingenZichtbaar = true;
			if (appletLaunchData.containsKey("grid1"))
				gridSize = Integer.parseInt((String) appletLaunchData.get("grid1"));
			
			if ((gridSize < minGrid) || (gridSize > maxGrid))
				gridSize = 20;

			if (appletLaunchData.containsKey("figuur1"))
				rodeFiguurInput = (String) appletLaunchData.get("figuur1");

			if (appletLaunchData.containsKey("oppervlakte1"))
				oppervlakte1String = (String) appletLaunchData.get("oppervlakte1");
			int oppervlakte1 = 0;
			error = false;
			try
			{	oppervlakte1 = Integer.parseInt(oppervlakte1String);
			}
			catch (NumberFormatException nfe)
			{	error = true;
			}
			if (!error)
				oppervlakteRood = oppervlakte1;
			
			if (appletLaunchData.containsKey("figuurgrijs1"))
				grijzeFiguurInput = (String) appletLaunchData.get("figuurgrijs1");
			
			if (appletLaunchData.containsKey("oppervlaktegrijs1"))
				oppervlaktegrijs1String = (String) appletLaunchData.get("oppervlaktegrijs1");
			int oppervlaktegrijs1 = 0;
			error = false;
			try
			{	oppervlaktegrijs1 = Integer.parseInt(oppervlaktegrijs1String);
			}
			catch (NumberFormatException nfe)
			{	error = true;
			}
			if (!error)
				oppervlakteGrijs = oppervlaktegrijs1;
			
			if (appletLaunchData.containsKey("scoreMax"))
				scoreMaxString = (String) appletLaunchData.get("scoreMax");
			int scMax = 0;
			error = false;
			try
			{	scMax = Integer.parseInt(scoreMaxString);
			}
			catch (NumberFormatException nfe)
			{	error = true;
			}
			if (!error)
				scoreMax = scMax;
			
			
		}
		else
		{

			if (b.containsKey("taakNummer"))
				taakNummer = ((Integer) b.get("taakNummer")).intValue();
			if (b.containsKey("balkOnderaan"))
				balkOnderaan = ((Boolean) b.get("balkOnderaan")).booleanValue();
			if (b.containsKey("roosterZichtbaar"))
				roosterZichtbaar = ((Boolean) b.get("roosterZichtbaar")).booleanValue();
			if (b.containsKey("groteBalletjes"))
				groteBalletjes = ((Boolean) b.get("groteBalletjes")).booleanValue();
			if (b.containsKey("schaduwZichtbaar"))
				schaduwZichtbaar = ((Boolean) b.get("schaduwZichtbaar")).booleanValue();
			if (b.containsKey("afmetingenZichtbaar"))
				afmetingenZichtbaar = ((Boolean) b.get("afmetingenZichtbaar")).booleanValue();
			if (b.containsKey("tekenGumOptie"))
				tekenGumOptie = ((Boolean) b.get("tekenGumOptie")).booleanValue();
			if (b.containsKey("gridSize"))
				gridSize = ((Integer) b.get("gridSize")).intValue();
			if (b.containsKey("rodeFiguurInput"))
				rodeFiguurInput = (String) b.get("rodeFiguurInput");
			if (b.containsKey("oppervlakteRood"))
				oppervlakteRood = ((Integer) b.get("oppervlakteRood")).intValue();
			if (b.containsKey("grijzeFiguurInput"))
				grijzeFiguurInput = (String) b.get("grijzeFiguurInput");
			if (b.containsKey("oppervlakteGrijs"))
				oppervlakteGrijs = ((Integer) b.get("oppervlakteGrijs")).intValue();
			if (b.containsKey("scoreMax"))
				scoreMax = ((Integer) b.get("scoreMax")).intValue();
			
		}
		this.taakNummer = taakNummer;
		//taakNummerVeld.setText("" + taakNummer);
		taakNummerComboEnabled = false;
		taakNummerCombo.setSelectedIndex(taakNummer);
		taakNummerComboEnabled = true;
		balkOnderaanBox.setSelected(balkOnderaan);
		roosterZichtbaarBox.setSelected(roosterZichtbaar);
		groteBalletjesBox.setSelected(groteBalletjes);
		schaduwZichtbaarBox.setSelected(schaduwZichtbaar);
		afmetingenZichtbaarBox.setSelected(afmetingenZichtbaar);
		tekenGumOptieBox.setSelected(tekenGumOptie);
		this.gridSize = gridSize;
		gridSizeVeld.setText("" + gridSize);
		this.rodeFiguurInput = rodeFiguurInput;
		rodeFiguurPanel.zetFiguurString(rodeFiguurInput);
		this.oppervlakteRood = oppervlakteRood; 
		oppervlakteRoodVeld.setText("" + oppervlakteRood);
		this.grijzeFiguurInput = grijzeFiguurInput;
		grijzeFiguurPanel.zetFiguurString(grijzeFiguurInput);
		this.oppervlakteGrijs = oppervlakteGrijs; 
		oppervlakteGrijsVeld.setText("" + oppervlakteGrijs);
		this.scoreMax = scoreMax;
		scoreMaxVeld.setText("" + scoreMax);
		
		
		
		if (b.containsKey("vipBreedte"))
			vipBreedte = ((Integer) b.get("vipBreedte")).intValue();
		if (b.containsKey("vipHoogte"))
			vipHoogte = ((Integer) b.get("vipHoogte")).intValue();
		
		setBounds(getLocation().x, getLocation().y, vipBreedte + editWidth, Math.max(vipHoogte, editHeight));

		if (taakNummer <= 1)
		{	oppervlakteRoodLabel.setEnabled(false);
			oppervlakteRoodVeld.setEnabled(false);
			maakGrijzeFiguurButton.setEnabled(false);
			oppervlakteGrijsLabel.setEnabled(false);
			oppervlakteGrijsVeld.setEnabled(false);
			grijzeFiguurPanel.setVisible(false);
		}
		else if ((taakNummer == 2) || (taakNummer == 3))
		{
			oppervlakteRoodLabel.setEnabled(true);
			oppervlakteRoodVeld.setEnabled(true);
			maakGrijzeFiguurButton.setEnabled(false);
			oppervlakteGrijsLabel.setEnabled(false);
			oppervlakteGrijsVeld.setEnabled(false);
			grijzeFiguurPanel.setVisible(false);
		}
		else
		{	oppervlakteRoodLabel.setEnabled(true);
			oppervlakteRoodVeld.setEnabled(true);
			maakGrijzeFiguurButton.setEnabled(true);
			oppervlakteGrijsLabel.setEnabled(true);
			oppervlakteGrijsVeld.setEnabled(true);
		}
		
		boolean toonRodeFiguurInput = false;
		if (b.containsKey("toonRodeFiguurInput"))
			toonRodeFiguurInput = ((Boolean) b.get("toonRodeFiguurInput")).booleanValue();
		boolean toonGrijzeFiguurInput = false;
		if (b.containsKey("toonGrijzeFiguurInput"))
			toonGrijzeFiguurInput = ((Boolean) b.get("toonGrijzeFiguurInput")).booleanValue();
		rodeFiguurPanel.setVisible(toonRodeFiguurInput);
		grijzeFiguurPanel.setVisible(toonGrijzeFiguurInput);
		
		
		// HIER !!
		vip.setEditState(b);		
		
	}
	
	public Hashtable getEditState()
	{
System.out.println("viep getEditState");

		Hashtable h = vip.getEditState(); 
		
		h.put("toonRodeFiguurInput", new Boolean(rodeFiguurPanel.isVisible()));
		h.put("toonGrijzeFiguurInput", new Boolean(grijzeFiguurPanel.isVisible()));

		h.put("rodeFiguurInput", rodeFiguurInput);
		h.put("grijzeFiguurInput", grijzeFiguurInput);
		
		h.put("scoreMax", new Integer(scoreMax));
		
		h.put("vipBreedte", new Integer(vipBreedte));
		h.put("vipHoogte", new Integer(vipHoogte));
		
		return h;
		
	}
		
	
	public void setBounds(int x, int y, int b, int h)
	{
		if (noSetBounds)
		{	noSetBounds = false;
			return;
		}
//System.out.println("giep setBounds raw " + x + " " + y + " " + b + " " + h);

		if ((h <= 1) || (x < 0) || (b <= 1))
			return;
		
		super.setBounds(x, y, vipBreedte + editWidth, Math.max(vipHoogte, editHeight));
		
//System.out.println("giep setBounds " + x + " " + y + " " + (gipBreedte + editWidth) + " " + 
//					Math.max(gipHoogte, editHeight));
	
		if (vip != null)
		{	vip.setBounds(0, 0, vipBreedte, vipHoogte);
			vip.drawingPanel2.add(rodeFiguurPanel, 0);
			vip.drawingPanel2.add(grijzeFiguurPanel, 0);
		}
		
		plaatsComponenten();
		
//System.out.println("setBounds " + x + " " + y + " " + b + " " + h);		
		
	}
	
	public void zetBreedte(int b)
	{	
		vipBreedte = b;
		
		setBounds(getLocation().x, getLocation().y, vipBreedte + editWidth, Math.max(vipHoogte, editHeight));		
		plaatsComponenten();
	}
	
	public void zetHoogte(int h)
	{	
		vipHoogte = h;
		
		setBounds(getLocation().x, getLocation().y, vipBreedte + editWidth, Math.max(vipHoogte, editHeight));		
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
		if (e.getSource() == taakNummerCombo)
		{	if (!taakNummerComboEnabled)
				return;
			zetTaakNummer(taakNummerCombo.getSelectedIndex());
			if (taakNummer <= 1)
			{	oppervlakteRoodLabel.setEnabled(false);
				oppervlakteRoodVeld.setEnabled(false);
				maakGrijzeFiguurButton.setEnabled(false);
				oppervlakteGrijsLabel.setEnabled(false);
				oppervlakteGrijsVeld.setEnabled(false);
				grijzeFiguurPanel.setVisible(false);
			}
			else if ((taakNummer == 2) || (taakNummer == 3))
			{
				oppervlakteRoodLabel.setEnabled(true);
				oppervlakteRoodVeld.setEnabled(true);
				maakGrijzeFiguurButton.setEnabled(false);
				oppervlakteGrijsLabel.setEnabled(false);
				oppervlakteGrijsVeld.setEnabled(false);
				grijzeFiguurPanel.setVisible(false);
			}
			else
			{	oppervlakteRoodLabel.setEnabled(true);
				oppervlakteRoodVeld.setEnabled(true);
				maakGrijzeFiguurButton.setEnabled(true);
				oppervlakteGrijsLabel.setEnabled(true);
				oppervlakteGrijsVeld.setEnabled(true);
			}
			
		}
		else if (e.getSource() == balkOnderaanBox)
		{	vip.zetBalkOnderaan(balkOnderaanBox.isSelected());
		}
		else if (e.getSource() == roosterZichtbaarBox)
		{	vip.zetRoosterZichtbaar(roosterZichtbaarBox.isSelected());
		}
		else if (e.getSource() == groteBalletjesBox)
		{	vip.zetGroteBalletjes(groteBalletjesBox.isSelected());
		}
		else if (e.getSource() == schaduwZichtbaarBox)
		{	vip.zetSchaduwZichtbaar(schaduwZichtbaarBox.isSelected());
		}
		else if (e.getSource() == afmetingenZichtbaarBox)
		{	vip.zetAfmetingenZichtbaar(afmetingenZichtbaarBox.isSelected());
		}
		else if (e.getSource() == tekenGumOptieBox)
		{	vip.zetTekenGumOptie(tekenGumOptieBox.isSelected());
			if (tekenGumOptieBox.isSelected())
				roosterZichtbaarBox.setSelected(true);
		}
		else if (e.getSource() == maakRodeFiguurButton)
		{	if (!rodeFiguurPanel.isVisible())
			{
				vip.drawingPanel2.frozen = true;
				rodeFiguurPanel.setVisible(true);
				
				// actie in grijs?
				grijzeFiguurPanel.setVisible(false);
			}
		}
		else if (e.getSource() == maakGrijzeFiguurButton)
		{	if (!grijzeFiguurPanel.isVisible())
			{	
				vip.drawingPanel2.frozen = true;
				grijzeFiguurPanel.setVisible(true);
			
				// actie in rood?
				rodeFiguurPanel.setVisible(false);
			}
		}
		
		

	}

	class TextFL2 implements FocusListener
	{		
		JTextField inputTextField;
		
		public TextFL2(JTextField input)
		{	inputTextField = input;
		}

		public void focusGained(FocusEvent e)
		{
		}
		public void focusLost(FocusEvent e)
		{	// invoer user
			String text = inputTextField.getText();
			String oldText = "";
			//if (inputTextField == taakNummerVeld)
			//	oldText = "" + taakNummer;
			if (inputTextField == gridSizeVeld)
				oldText = "" + gridSize;
			else if (inputTextField == oppervlakteRoodVeld)
				oldText = "" + oppervlakteRood;
			else if (inputTextField == oppervlakteGrijsVeld)
				oldText = "" + oppervlakteGrijs;
			else if (inputTextField == scoreMaxVeld)
				oldText = "" + scoreMax;
			
			inputTextField.setText(text);				

			String format = new String(text);		
			format = format.replace(',', '.');		

			double userInput = 0;
			boolean error = false;
			try
			{	userInput = Double.parseDouble(format);
			}
			catch (NumberFormatException nfe)
			{	error = true;
//System.out.println("nfe");			
			}
			// dit zou niet moeten gebeuren
			// Peter: nu wel bij de definitie van een random variabele ipv een double			
			if (error)
				return;

			if (inputTextField == gridSizeVeld)
			{	int gScore = (int) userInput;
				if ((gScore >= minGrid) && (gScore <= maxGrid))
				{	gridSize = gScore;
					zetGridSize(gridSize);
				}
				else
				{	inputTextField.setText(oldText);
				}
			}
			if (inputTextField == oppervlakteRoodVeld)
			{	int orScore = (int) userInput;
				if ((orScore >= 1) && (orScore <= 10000))
				{	oppervlakteRood = orScore;
				}
				else
				{	inputTextField.setText(oldText);
				}
			}
			if (inputTextField == oppervlakteGrijsVeld)
			{	int ogScore = (int) userInput;
				if ((ogScore >= 1) && (ogScore <= 10000))
				{	oppervlakteGrijs = ogScore;
				}
				else
				{	inputTextField.setText(oldText);
				}
			}
			if (inputTextField == scoreMaxVeld)
			{	int mScore = (int) userInput;
				if ((mScore >= 1) && (mScore <= 1500))
				{	scoreMax = mScore;
				}
				else
				{	inputTextField.setText(oldText);
				}
			}
			
		} // focusLost
	}


	class TextAL2 implements ActionListener
	{	
		JTextField inputTextField;
		
		public TextAL2(JTextField input)
		{	inputTextField = input;
		}
		
		public void actionPerformed(ActionEvent e)
		{	
			String text = inputTextField.getText();
			String oldText = "";
			//if (inputTextField == taakNummerVeld)
			//	oldText = "" + taakNummer;
			if (inputTextField == gridSizeVeld)
				oldText = "" + gridSize;
			else if (inputTextField == oppervlakteRoodVeld)
				oldText = "" + oppervlakteRood;
			else if (inputTextField == oppervlakteGrijsVeld)
				oldText = "" + oppervlakteGrijs;
			else if (inputTextField == scoreMaxVeld)
				oldText = "" + scoreMax;
			
			inputTextField.setText(text);				

			String format = new String(text);		
			format = format.replace(',', '.');		

			double userInput = 0;
			boolean error = false;
			try
			{	userInput = Double.parseDouble(format);
			}
			catch (NumberFormatException nfe)
			{	error = true;
			}
			// dit zou niet moeten gebeuren  
			// Peter: nu wel bij de definitie van een random variabele ipv een double
			if (error)
			{	return;
			}

			if (inputTextField == gridSizeVeld)
			{	int gScore = (int) userInput;
				if ((gScore >= minGrid) && (gScore <= maxGrid))
				{	gridSize = gScore;
					zetGridSize(gridSize);
				}
				else
				{	inputTextField.setText(oldText);
				}
			}
			if (inputTextField == oppervlakteRoodVeld)
			{	int orScore = (int) userInput;
				if ((orScore >= 1) && (orScore <= 10000))
				{	oppervlakteRood = orScore;
					zetOppervlakteRood(oppervlakteRood);
				}
				else
				{	inputTextField.setText(oldText);
				}
			}
			if (inputTextField == oppervlakteGrijsVeld)
			{	int ogScore = (int) userInput;
				if ((ogScore >= 1) && (ogScore <= 10000))
				{	oppervlakteGrijs = ogScore;
					zetOppervlakteGrijs(oppervlakteGrijs);
				}
				else
				{	inputTextField.setText(oldText);
				}
			}
			if (inputTextField == scoreMaxVeld)
			{	int mScore = (int) userInput;
				if ((mScore >= 1) && (mScore <= 1500))
				{	scoreMax = mScore;
				}
				else
				{	inputTextField.setText(oldText);
				}
			}
			
			
		} // actionPerformed
	}
	
	public String trimTrailingZeros(String s, char decSep)
	{	String txt = new String(s);
		if (txt.indexOf(decSep) < 0)
			return txt;
		char c = txt.charAt(txt.length() - 1);
		while (c == '0')
		{	txt = removeCharAt(txt, txt.length() - 1);
			c = txt.charAt(txt.length() - 1);
		}	
		c = txt.charAt(txt.length() - 1);
		if (c == decSep)
			txt = removeCharAt(txt, txt.length() - 1);
		return txt;		
	}				
		
	public String removeCharAt(String s, int index)
	{	String txt = new String(s);
		// eerste
		if (index == 0)
			txt = txt.substring(1);
		// laatste	
		else if (index == (txt.length() - 1))
			txt = txt.substring(0, txt.length() - 1);
		// middenin	
		else
		{	String txt1 = txt.substring(0, index);
			String txt2 = txt.substring(index + 1);
			txt = txt1 + txt2;
		}
		return txt;
	}		

	class InputKL2 extends KeyAdapter
	{	
		JTextField inputTextField;
		
		public InputKL2(JTextField input)
		{	inputTextField = input;
		}
		
		public void keyReleased(KeyEvent e)
		{	
			inputTextField.setForeground(Color.black);
		
			String txt = inputTextField.getText();
			
//System.out.println(txt);
				
			boolean corrected = false;

			// kijk of txt illegale characters bevat
			// dit zou er maximaal 1 moeten zijn
			int index = -1;
			for (int cCnt = 0; cCnt < txt.length(); cCnt++)
			{	char c = txt.charAt(cCnt);
				if (!isLegal(c))
				{	index = cCnt;
//System.out.println("illegal " + index);				
				}
			}	
			// verwijder illegaal karakter
			if (index >= 0)
			{	txt = removeCharAt(txt, index);
				corrected = true;
//System.out.println("corr " + txt);							
			}
			
//System.out.println(txt);			
			
			// leading zeros, leiden niet tot een NumberFormatException	
			// geen minteken
			if ((txt.indexOf('-') < 0) && (txt.length() >= 2) &&
				(txt.charAt(0) == '0') && Character.isDigit(txt.charAt(1)))
			{	txt = removeCharAt(txt, 0);
				corrected = true;
			}
			
			if (corrected)
			{	
//System.out.println("corr " + txt);							
				inputTextField.setText(txt);
			
			}
			
		}
		public boolean isLegal(char c)
		{	return Character.isDigit(c);
		}
	}	

	class CloseFiguurInputAL implements ActionListener
	{
		boolean rodeFiguur;
		
		public CloseFiguurInputAL(boolean rood)
		{
			rodeFiguur = rood;
		}
		
		public void actionPerformed(ActionEvent e)
		{
			if (rodeFiguur)
			{
				rodeFiguurPanel.procesInput();
				rodeFiguurPanel.setVisible(false);
				//maakRodeFiguur();
				vip.drawingPanel2.frozen = false;
			}
			else
			{
				grijzeFiguurPanel.procesInput();
				grijzeFiguurPanel.setVisible(false);
				//maakGrijzeFiguur();
				vip.drawingPanel2.frozen = false;
			}
			
			//beginExpressiePanel.setVisible(false);
			//maakBeginExpressie();
			//apoip.disableElements(false);
		}
		
	}

}
