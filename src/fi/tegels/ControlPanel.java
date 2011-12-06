package fi.tegels;

import java.awt.*;
import java.awt.event.*;
import fi.beans.copyright.*;

import javax.swing.*;

class ControlPanel extends JPanel implements ActionListener
{	
	FIButton fiButton;
	JTextField codeveld;
	JButton draaiknop,wisknop,tekenknop,legknop,terugknop,wisTegelknop;
	Tegels eigenaar;
	JButton[] kleurenV;
	Color[] kleuren = {Color.black,Color.white,Color.gray,Color.lightGray,Color.red,Color.orange,
					Color.yellow,Color.green,Color.cyan,Color.blue,Color.magenta,Color.pink,Color.white};
	String[] kleurnamen = {"zwart","wit","grijs","lichtgrijs","rood","oranje","geel","groen",
						   "cyaan","blauw","magenta","roze"};
	ActKeuzePanel gridKeuze;

	Font font;
	FontMetrics fm;
	
	int offset = 10;
	int arrowButtonWidth = 20;
	LWArrowButton upButton, downButton;
	
	public ControlPanel(Tegels gv)
	{	
		eigenaar = gv;
		
		font = new Font("Dialog", Font.PLAIN, 12);
		fm = getFontMetrics(font);
		
		setBackground(Color.lightGray);
		
		fiButton = new FIButton("Tegels",new String[]{"","versie-info: 20110422",											"auteurs: Peter Boon, Frans van Galen",
											"programmeur: Peter Boon",											"Freudenthal Instituut",											"www.fi.uu.nl",""});
		//fiButton.setBounds(0,30,20,30);
		//add(fiButton);
		
		upButton = new LWArrowButton(0, new Color(230, 230, 230));
		upButton.setBounds(0, 0, arrowButtonWidth, arrowButtonWidth);
		upButton.setEnabled(false);
		upButton.addActionListener(this);
		add(upButton);
		
		downButton = new LWArrowButton(2, new Color(230, 230, 230));
		downButton.setBounds(0, eigenaar.controlHoogte - arrowButtonWidth, arrowButtonWidth, arrowButtonWidth);
		downButton.setEnabled(false);	
		downButton.addActionListener(this);
		add(downButton);
		
		
		int width = Math.max(fm.stringWidth(Tegels.rb.getString("draaiknopLabel")), 
				             fm.stringWidth(Tegels.rb.getString("wisTegelknopLabel"))) + 45;
		int height = 3 * fm.getHeight() / 2;
		
		draaiknop = new JButton(Tegels.rb.getString("draaiknopLabel"));
		draaiknop.setFont(font);
		//draaiknop.setBounds(30,20,80,20);
		draaiknop.setBounds(arrowButtonWidth + offset, offset, width, height);
		add(draaiknop);
		draaiknop.addActionListener(this);

		width = Math.max(fm.stringWidth(Tegels.rb.getString("wisknopLabel")), 
                         fm.stringWidth(Tegels.rb.getString("terugknopLabel"))) + 45;
		
		wisknop = new JButton(Tegels.rb.getString("wisknopLabel"));
		wisknop.setFont(font);
		//wisknop.setBounds(130,20,80,20);
		wisknop.setBounds(draaiknop.getLocation().x + 
				          draaiknop.getSize().width + offset,
				          offset, width, height);
		add(wisknop);
		wisknop.addActionListener(this);

		width = Math.max(fm.stringWidth(Tegels.rb.getString("tekenknopLabel")), 
                         fm.stringWidth(Tegels.rb.getString("legknopLabel"))) + 45;
		
		tekenknop = new JButton(Tegels.rb.getString("tekenknopLabel"));
		tekenknop.setFont(font);
		//tekenknop.setBounds(230,20,80,20);
		tekenknop.setBounds(wisknop.getLocation().x + 
							wisknop.getSize().width + offset,
							offset, width, height);
		add(tekenknop);
		tekenknop.addActionListener(this);
		
		legknop = new JButton(Tegels.rb.getString("legknopLabel"));
		legknop.setFont(font);		
		//legknop.setBounds(230,20,80,20);
		legknop.setBounds(tekenknop.getLocation().x, tekenknop.getLocation().y, 
						  tekenknop.getSize().width, height);
		add(legknop);
		legknop.addActionListener(this);
		legknop.setVisible(false);
		
		terugknop = new JButton(Tegels.rb.getString("terugknopLabel"));
		terugknop.setFont(font);
		//terugknop.setBounds(130,20,80,20);
		terugknop.setBounds(wisknop.getLocation().x, wisknop.getLocation().y, 
				  			wisknop.getSize().width, height);
		add(terugknop);
		terugknop.addActionListener(this);
		terugknop.setVisible(false);
		
		wisTegelknop = new JButton(Tegels.rb.getString("wisTegelknopLabel"));
		wisTegelknop.setFont(font);
		//wisTegelknop.setBounds(30,20,80,20);
		wisTegelknop.setBounds(draaiknop.getLocation().x, draaiknop.getLocation().y, 
	  						   draaiknop.getSize().width, height);
		add(wisTegelknop);
		wisTegelknop.addActionListener(this);
		wisTegelknop.setVisible(false);
	
		codeveld = new JTextField(150);
		codeveld.setFont(font);
		//codeveld.setBounds(330,20,185,20);
		codeveld.setBounds(legknop.getLocation().x, // + 
						   //legknop.getSize().width + 2 * offset,
						   legknop.getLocation().y + legknop.getSize().height + offset / 2,
						   185, height);
		if (!eigenaar.transVersion)
			add(codeveld);
		codeveld.setEditable(false);
		codeveld.setBackground(Color.white);
		codeveld.setVisible(false);
		
		if (eigenaar.transVersion)
		{	
			//fiButton.setBounds(legknop.getLocation().x + legknop.getSize().width - 20,
			//				   legknop.getLocation().y + legknop.getSize().height + offset / 2,
			//				   20, 30);

			fiButton.setBounds(legknop.getLocation().x + legknop.getSize().width + 20,
							   legknop.getLocation().y, // + legknop.getSize().height + offset / 2,
							   20, 30);
			
		}
		else
		{
			fiButton.setBounds(legknop.getLocation().x + legknop.getSize().width + 2 * offset,
					   legknop.getLocation().y,
					   20, 30);
			
		}
		add(fiButton);		
		
		kleurenV = new JButton[12];
		for (int i = 0; i < 12; i++)
		{	final Color buttonColor = kleuren[i];
			kleurenV[i] = new JButton()
			{	public void paintComponent(Graphics g)
				{	g.setColor(buttonColor);
					g.fillRect(0,0,getWidth(),getHeight());
				}
			};
			kleurenV[i].setBounds(arrowButtonWidth + offset + 20 * i, 
						          draaiknop.getLocation().y + draaiknop.getSize().height + offset, 20 , 20);
			add(kleurenV[i]);
			kleurenV[i].addActionListener(this);
			kleurenV[i].setVisible(true);
		}

		String[] items = {Tegels.rb.getString("fijnRasterTekst"), 
						  Tegels.rb.getString("grofRasterTekst")};

		//width = Math.max(fm.stringWidth(Tegels.rb.getString("fijnRasterTekst")), 
		//				 fm.stringWidth(Tegels.rb.getString("grofRasterTekst"))) + 55;
		width = fm.stringWidth(Tegels.rb.getString("fijnRasterTekst")) +  
				fm.stringWidth(Tegels.rb.getString("grofRasterTekst")) + offset + 60;
		
		
		//gridKeuze = new ActKeuzePanel(items, 400, 10, 200, 40);
		gridKeuze = new ActKeuzePanel(items, 
									  draaiknop.getLocation().x, // + legknop.getSize().width + offset, 
									  draaiknop.getLocation().y + draaiknop.getSize().height + offset / 2, 
									  width, height, getBackground());
		
		//gridKeuze.setBackground(Color.lightGray);
		gridKeuze.addActionListener(this);
		gridKeuze.setVisible(false);
		gridKeuze.setItem(1);
		if (eigenaar.transVersion)
			add(gridKeuze,0);
		
	}
	public void controlTekenen()
	{	draaiknop.setVisible(false);
		wisknop.setVisible(false);
		tekenknop.setVisible(false);
		terugknop.setVisible(true);
		legknop.setVisible(true);
		wisTegelknop.setVisible(true);
		for(int i=0 ; i<12 ; i++)
		{	kleurenV[i].setVisible(false);
		}
		codeveld.setVisible(true);
	}
	public void controlLeggen()
	{	draaiknop.setVisible(true);
		wisknop.setVisible(true);
		tekenknop.setVisible(true);
		terugknop.setVisible(false);
		legknop.setVisible(false);
		wisTegelknop.setVisible(false);
		for(int i=0 ; i<12 ; i++)
		{	kleurenV[i].setVisible(true);
		}
		codeveld.setVisible(false);
	}
	public void controlFoto()
	{	draaiknop.setVisible(false);
		wisknop.setVisible(true);
		tekenknop.setVisible(false);
		terugknop.setVisible(false);
		legknop.setVisible(false);
		wisTegelknop.setVisible(true);
	}
	public void actionPerformed(ActionEvent e)
	{	if (e.getSource() == upButton)
		{	eigenaar.vorigeBasisVorm();
		}
		if (e.getSource() == downButton)
		{	eigenaar.volgendeBasisVorm();
		}
		if (e.getSource() == draaiknop)
		{	eigenaar.draaiBasisvorm();
		}
		if (e.getSource() == wisknop)
		{	eigenaar.wisSs();
		}
		if (e.getSource() == tekenknop)
		{	controlTekenen();
			eigenaar.zetTekenen();
			gridKeuze.setVisible(true);			
		}
		if (e.getSource() == legknop)
		{	controlLeggen();
			eigenaar.zetLeggen();
			gridKeuze.setVisible(false);
		}
		if (e.getSource() == terugknop)
		{	eigenaar.tekenStapTerug();
		}
		if (e.getSource() == wisTegelknop)
		{	eigenaar.wisTegel();
		}
		for (int i = 0; i < 12; i++)
		{	if (e.getSource() == kleurenV[i])
			{	eigenaar.kleurBasisvorm(kleuren[i]);
			}
		}
		if (e.getSource() == gridKeuze)
		{	int factor = gridKeuze.geefKeuze();
			eigenaar.vermenigvuldigPunten((double)Trans.factor/factor);
			Trans.zetFactor(factor);
			eigenaar.tekenOpnieuw();
		}
		
	}
/*	
	public void paint(Graphics g)
	{	super.paint(g);
	}
*/		
}