package fi.tegels;

import java.awt.Polygon;
import java.awt.*;
import java.util.*;
import java.awt.event.*;
import fi.tegels.text.*;
import fi.beans.copyright.*;


class ControlPanel extends Panel implements ActionListener
{	
	private FIButton fiButton;
	TextField codeveld;
	Button draaiknop,wisknop,tekenknop,legknop,terugknop,wisTegelknop;
	Tegels eigenaar;
	Button[] kleurenV;
	Color[] kleuren = {Color.black,Color.white,Color.gray,Color.lightGray,Color.red,Color.orange,
					Color.yellow,Color.green,Color.cyan,Color.blue,Color.magenta,Color.pink,Color.white};
	String[] kleurnamen = {"zwart","wit","grijs","lichtgrijs","rood","oranje","geel","groen",
						   "cyaan","blauw","magenta","roze"};

	
	public ControlPanel(Tegels gv)
	{	
		eigenaar = gv;
		
		setBackground(Color.lightGray);
		
		fiButton = new FIButton("Tegels",new String[]{"","versie-info: 20060609",											"auteurs: Peter Boon, Frans van Galen",
											"programmeur: Peter Boon",											"Freudenthal Instituut",											"www.fi.uu.nl",""});
		fiButton.setBounds(0,30,20,30);
		add(fiButton);
		
		draaiknop = new Button(Tegels.rb.getString("draaiknopLabel"));
		draaiknop.setBounds(30,20,80,20);
		add(draaiknop);
		draaiknop.addActionListener(this);
		
		wisknop = new Button(Tegels.rb.getString("wisknopLabel"));
		wisknop.setBounds(130,20,80,20);
		add(wisknop);
		wisknop.addActionListener(this);
		
		tekenknop = new Button(Tegels.rb.getString("tekenknopLabel"));
		tekenknop.setBounds(230,20,80,20);
		add(tekenknop);
		tekenknop.addActionListener(this);
		
		legknop = new Button(Tegels.rb.getString("legknopLabel"));
		legknop.setBounds(230,20,80,20);
		add(legknop);
		legknop.addActionListener(this);
		legknop.setVisible(false);
		
		terugknop = new Button(Tegels.rb.getString("terugknopLabel"));
		terugknop.setBounds(130,20,80,20);
		add(terugknop);
		terugknop.addActionListener(this);
		terugknop.setVisible(false);
		
		wisTegelknop = new Button(Tegels.rb.getString("wisTegelknopLabel"));
		wisTegelknop.setBounds(30,20,80,20);
		add(wisTegelknop);
		wisTegelknop.addActionListener(this);
		wisTegelknop.setVisible(false);
	
		codeveld = new TextField(150);
		codeveld.setBounds(330,20,185,20);
		add(codeveld);
		codeveld.setEditable(false);
		codeveld.setBackground(Color.white);
		codeveld.setVisible(false);
		
		kleurenV = new Button[12];
		for(int i=0 ; i<12 ; i++)
		{	kleurenV[i] = new Button("  ");
			kleurenV[i].setBackground(kleuren[i]);
			kleurenV[i].setBounds(330+15*i,20,15,20);
			add(kleurenV[i]);
			kleurenV[i].addActionListener(this);
			kleurenV[i].setVisible(true);
		}
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
	{	if(e.getSource() == draaiknop)
		{	eigenaar.draaiBasisvorm();
		}
		if(e.getSource() == wisknop)
		{	eigenaar.wisSs();
		}
		if(e.getSource() == tekenknop)
		{	controlTekenen();
			eigenaar.zetTekenen();
		}
		if(e.getSource() == legknop)
		{	controlLeggen();
			eigenaar.zetLeggen();
		}
		if(e.getSource() == terugknop)
		{	eigenaar.tekenStapTerug();
		}
		if(e.getSource() == wisTegelknop)
		{	eigenaar.wisTegel();
		}
		for(int i=0 ; i<12 ; i++)
		{	if(e.getSource() == kleurenV[i])
			{	eigenaar.kleurBasisvorm(kleuren[i]);
			}
		}
	}
}