package fi.balansfruit;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;

public class BalansFruitInteractieEditPanel extends BalansFruitInteractiePanel
	implements ActionListener, FocusListener
{
	int aantalSoorten;
	private ImageComponent[] imageComponenten;
	private JTextField[] aantalFields;
	private JTextField[] gewichtFields;
	private String[] aantallen;
	private String[] gewichten;

	private JCheckBox eenhedenCB;

	private JCheckBox fixedOptieCB;
	private JCheckBox resetOptieCB;
	private JCheckBox bewaarOptieCB;
	private JCheckBox viewEquationCB;

	private JLabel aantalLabel, gewichtLabel;

	private int scrollSpeedUnit = 16;

	public BalansFruitInteractieEditPanel()
	{
		super();
		editMode = true;

		setLayout(null);
		aantalSoorten = imageNames.length - 4;

		aantallen = new String[aantalSoorten];
		gewichten = new String[aantalSoorten];

		imageComponenten = new ImageComponent[aantalSoorten];
		aantalFields = new JTextField[aantalSoorten];
		gewichtFields = new JTextField[aantalSoorten];

		eenhedenCB = new JCheckBox("Show units");
		eenhedenCB.setBounds(400, 400, 100, 20);
		eenhedenCB.addActionListener(this);
		eenhedenCB.setSelected(true);
		add(eenhedenCB);

		bewaarOptieCB = new JCheckBox("Store option");
		bewaarOptieCB.setBounds(20, 400, 180, 20);
		bewaarOptieCB.addActionListener(this);
		add(bewaarOptieCB);

		fixedOptieCB = new JCheckBox("Fixed");
		fixedOptieCB.setBounds(20, 430, 180, 20);
		fixedOptieCB.addActionListener(this);
		add(fixedOptieCB);

		resetOptieCB = new JCheckBox("Reset option");
		resetOptieCB.setBounds(20, 460, 180, 20);
		resetOptieCB.addActionListener(this);
		add(resetOptieCB);

		viewEquationCB = new JCheckBox("View Equation");
		viewEquationCB.setBounds(20, 490, 180, 20);
		viewEquationCB.addActionListener(this);
		add(viewEquationCB);

		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());

		// first row
		aantalLabel = new JLabel("Number");
		gewichtLabel = new JLabel("Weight");
		JLabel dummy = new JLabel("");
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(3, 3, 3, 3);
		c.fill = GridBagConstraints.BOTH;
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 1;
		panel.add(aantalLabel, c);
		c.gridx = 1;
		c.gridy = 0;
		c.gridwidth = 1;
		panel.add(dummy);
		c.gridx = 2;
		c.gridy = 0;
		c.gridwidth = 1;
		panel.add(gewichtLabel, c);

		// for each row add the components
		for (int i = 0; i < aantalSoorten; i++)
		{
			imageComponenten[i] = new ImageComponent(getImage(imageNames[i]));
			int hImage = imageComponenten[i].getHeight();
			int wImage = imageComponenten[i].getWidth();

			aantalFields[i] = new JTextField("0");
			aantalFields[i].addActionListener(this);
			aantalFields[i].addFocusListener(this);
			aantalFields[i].setSize(40, 20);
			c.fill = GridBagConstraints.HORIZONTAL;
			c.ipady = 0;
			c.ipadx = 0;
			c.gridx = 0;
			c.gridy = i + 1; // add 1 to account for the header row
			c.gridwidth = 1;
			panel.add(aantalFields[i], c);

			c.fill = GridBagConstraints.BOTH;
			c.ipady = hImage;
			c.ipadx = wImage;
			c.gridx = 1;
			c.gridy = i + 1;
			c.gridwidth = 1;
			panel.add(imageComponenten[i], c);

			gewichtFields[i] = new JTextField("0");
			gewichtFields[i].addActionListener(this);
			gewichtFields[i].addFocusListener(this);
			gewichtFields[i].setSize(40, 20);
			c.fill = GridBagConstraints.HORIZONTAL;
			c.ipady = 0;
			c.ipadx = 0;
			c.gridx = 2;
			c.gridy = i + 1;
			c.gridwidth = 1;
			panel.add(gewichtFields[i], c);
		}

		// add the scrollpanel containing the fruit settings
		JScrollPane scrollPane = new JScrollPane(panel);
		scrollPane.getVerticalScrollBar()
			.setUnitIncrement(this.scrollSpeedUnit);
		scrollPane.setBounds(550, 0, 230, 550);
		add(scrollPane);

		launchData = defaultParamValues;
		vulTextFields();

		loadParameters();

		run();
	}

	public void vulTextFields()
	{
		for (int i = 0; i < aantalSoorten; i++)
		{
			if (launchData.containsKey("aantal" + i))
				aantallen[i] = (String) launchData.get("aantal" + i);
			else
				aantallen[i] = "0";
			if (launchData.containsKey("gewicht" + i))
				gewichten[i] = (String) launchData.get("gewicht" + i);
			else
				gewichten[i] = "0";
		}

		for (int i = 0; i < aantalSoorten; i++)
		{
			aantalFields[i].setText(aantallen[i]);
			gewichtFields[i].setText(gewichten[i]);
		}
	}

	public Hashtable getEditState()
	{
		int aantalFruitObjects;
		int[] stukFruitX;
		int[] containerNr;
		boolean fixedOptie;
		boolean bewaarOptie;
		boolean resetOptie;
		boolean viewEquation;
		boolean variabelen;
		boolean showEenheden;

		aantalFruitObjects = this.aantalFruitObjects;

		stukFruitX = new int[aantalFruitObjects];
		containerNr = new int[aantalFruitObjects];
		for (int i = 0; i < aantalFruitObjects; i++)
		{
			stukFruitX[i] = fruitObjects[i].getLocation().x;
			int contNr = 0;
			boolean opLinks = fruitObjects[i].getParent() == links;
			boolean opRechts = fruitObjects[i].getParent() == rechts;
			if (opLinks)
				contNr = 1;
			if (opRechts)
				contNr = 2;
			containerNr[i] = contNr;
		}

		fixedOptie = fixedOptieCB.isSelected();
		bewaarOptie = bewaarOptieCB.isSelected();
		resetOptie = resetOptieCB.isSelected();
		viewEquation = viewEquationCB.isSelected();
		showEenheden = eenhedenCB.isSelected();

		launchData.put("aantalFruitObjects", new Integer(aantalFruitObjects));
		launchData.put("stukFruitX", stukFruitX);
		launchData.put("containerNr", containerNr);
		launchData.put("fixedOptie", new Boolean(fixedOptie));
		launchData.put("bewaarOptie", new Boolean(bewaarOptie));
		launchData.put("resetOptie", new Boolean(resetOptie));
		launchData.put("viewEquation", new Boolean(viewEquation));
		launchData.put("showEenheden", new Boolean(showEenheden));

		return launchData;
	}

	public void setEditState(Hashtable h)
	{
		launchData = h;
		vulTextFields();
		loadParameters();
		run();

		int aantalFruitObjects = 0;
		int[] stukFruitX = null;
		int[] containerNr = null;
		boolean fixedOptie = false;
		boolean bewaarOptie = false;
		boolean resetOptie = false;
		boolean viewEquation = false;
		boolean showEenheden = true;

		if (h.containsKey("aantalFruitObjects"))
			aantalFruitObjects = ((Integer) h.get("aantalFruitObjects"))
				.intValue();
		if (h.containsKey("stukFruitX"))
			stukFruitX = (int[]) h.get("stukFruitX");
		if (h.containsKey("containerNr"))
			containerNr = (int[]) h.get("containerNr");
		if (h.containsKey("fixedOptie"))
			fixedOptie = ((Boolean) h.get("fixedOptie")).booleanValue();
		if (h.containsKey("bewaarOptie"))
			bewaarOptie = ((Boolean) h.get("bewaarOptie")).booleanValue();
		if (h.containsKey("resetOptie"))
			resetOptie = ((Boolean) h.get("resetOptie")).booleanValue();
		if (h.containsKey("viewEquation"))
			viewEquation = ((Boolean) h.get("viewEquation")).booleanValue();
		if (h.containsKey("showEenheden"))
			showEenheden = ((Boolean) h.get("showEenheden")).booleanValue();

		this.aantalFruitObjects = aantalFruitObjects;

		zetEenheden(showEenheden);

		for (int i = 11; i < 17; i++)
			imageComponenten[i].setImage(getImage(imageNames[i]));

		for (int i = 0; i < aantalFruitObjects; i++)
		{
			if (containerNr[i] == 0)
				fruitObjects[i].setLocation(stukFruitX[i],
					fruitObjects[i].getLocation().y);
			if (containerNr[i] == 1)
			{
				links.addLWMComponent(fruitObjects[i], stukFruitX[i],
					fruitObjects[i].getLocation().y);
			}
			if (containerNr[i] == 2)
			{
				rechts.addLWMComponent(fruitObjects[i], stukFruitX[i],
					fruitObjects[i].getLocation().y);
			}
		}
		main.setBalance();

		fixedOptieCB.setSelected(fixedOptie);
		bewaarOptieCB.setSelected(bewaarOptie);
		resetOptieCB.setSelected(resetOptie);
		viewEquationCB.setSelected(viewEquation);
		eenhedenCB.setSelected(showEenheden);

		zetFixedOptie(fixedOptie);
		zetResetOptie(resetOptie);
		zetViewEquation(viewEquation);
	}

	public void verwerkInvoer(int nr)
	{
		aantallen[nr] = aantalFields[nr].getText();
		gewichten[nr] = gewichtFields[nr].getText();

		launchData.put("aantal" + nr, aantallen[nr]);
		launchData.put("gewicht" + nr, gewichten[nr]);

		for (int i = 0; i < aantalFruitObjects; i++)
		{
			voorraad.addLWMComponent(fruitObjects[i],
				fruitObjects[i].getLocation().x,
				fruitObjects[i].getLocation().y);

		}
		
		main.setBalance();

		loadParameters();

		run();
	}

	public void actionPerformed(ActionEvent e)
	{
		super.actionPerformed(e);

		for (int i = 0; i < aantalSoorten; i++)
		{
			if (aantalFields[i] == e.getSource()
				|| gewichtFields[i] == e.getSource())
				verwerkInvoer(i);
		}
		if (e.getSource() == eenhedenCB)
		{
			zetEenheden(eenhedenCB.isSelected());
			for (int i = 11; i < 17; i++)
				imageComponenten[i].setImage(getImage(imageNames[i]));

			repaint();
		}
		if (e.getSource() == resetOptieCB)
		{
			zetResetOptie(resetOptieCB.isSelected());
		}
		if (e.getSource() == fixedOptieCB)
		{
			zetFixedOptie(fixedOptieCB.isSelected());
		}
		if (e.getSource() == viewEquationCB)
		{
			zetViewEquation(viewEquationCB.isSelected());
		}

	}

	public void focusLost(FocusEvent e)
	{
		// geeft blijkbaar problemen...
		// for(int i=0 ; i<aantalSoorten ; i++)
		// {
		// if(aantalFields[i]==e.getSource() || gewichtFields[i]==e.getSource())
		// verwerkInvoer(i);
		// }
	}

	public void focusGained(FocusEvent e)
	{

	}
}
