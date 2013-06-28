package fi.draaibank;

import java.awt.Polygon;
import java.awt.*;
import java.util.*;
import java.awt.event.*;


class ControlPanel extends Panel implements ActionListener
{	private FIButton fiButton;
	Button wisknop,terugknop;
	Draaibank eigenaar;
	

	
	public ControlPanel(Draaibank gv)
	{	
		eigenaar = gv;
		
		setBackground(Color.lightGray);
		
		fiButton = new FIButton("AanzichtenRaden",new String[]{"","versie-info: 20020228",											"auteur: Peter Boon",
											"programmeur: Peter Boon",											"Freudenthal Instituut",											"www.fi.uu.nl",""});
		fiButton.setBounds(10,30,20,30);
		add(fiButton);
		
		wisknop = new Button(Draaibank.rb.getString("wisKnopLabel"));
		wisknop.setBounds(150,20,45,20);
		add(wisknop);
		wisknop.addActionListener(this);
		
		terugknop = new Button(Draaibank.rb.getString("terugKnopLabel"));
		terugknop.setBounds(40,20,100,20);
		add(terugknop);
		terugknop.addActionListener(this);
		//terugknop.setVisible(false);
		
	}
	
	public void actionPerformed(ActionEvent e)
	{	if(e.getSource() == wisknop)
		{	eigenaar.wis();
		}
		
		if(e.getSource() == terugknop)
		{	eigenaar.tekenStapTerug();
		}
		
	}
}