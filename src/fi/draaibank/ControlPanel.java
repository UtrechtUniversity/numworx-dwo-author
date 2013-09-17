package fi.draaibank;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

class ControlPanel extends JPanel implements ActionListener
{	
	private FIButton fiButton;
	JButton wisknop,terugknop;
	Draaibank eigenaar;
	
	public ControlPanel(Draaibank gv)
	{	
		eigenaar = gv;
		setBackground(Color.lightGray);
		
		fiButton = new FIButton("Info", new String[]{
											"Draaibank",
											"versie-info: 20020228",											"auteur: Peter Boon",
											"programmeur: Peter Boon",											"Freudenthal Instituut",											"www.fi.uu.nl",""});
		fiButton.setBounds(10,5,20,30);
		add(fiButton);
		
		wisknop = new JButton(Draaibank.rb.getString("wisKnopLabel"));
		wisknop.setBounds(160,10,100,20);
		add(wisknop);
		wisknop.addActionListener(this);
		
		terugknop = new JButton(Draaibank.rb.getString("terugKnopLabel"));
		terugknop.setBounds(40,10,100,20);
		add(terugknop);
		terugknop.addActionListener(this);
		
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