package fi.geomalgebra;

import java.awt.Polygon;
import java.awt.*;
import java.util.*;
import java.awt.event.*;
import fi.geomalgebra.text.*;

import javax.swing.*;

class ControlPanel extends JPanel implements ActionListener , ItemListener
{	
	AlgebraVeld eigenaar;
	// constantKnoppen worden niet gebruikt
	Button[] constantKnoppen;
	// minknop en volgendeknop worden niet gebruikt
	Button minknop, volgendeKnop;
	//Button minknop,xknop,yknop,zknop,wisknop,vorigeKnop, volgendeKnop;
	JButton xknop, yknop, zknop, wisknop, vorigeKnop;
	JCheckBox dop;
	//NumberSlider ns;

	
	public ControlPanel(AlgebraVeld av)
	{	
		eigenaar = av;
		
		setBackground(new Color(208, 228, 255));//(new Color(150, 150, 150));
		
		//ns = new NumberSlider(0,400,150,0,"","");
		//ns.setValue(150);
		//ns.setBounds(500,10,100,20);
		//add(ns);
		//ns.addNumberListener(this);
		
		
		
		minknop = new Button(GeomAlgebra.rb.getString("minknopLabel"));
		minknop.setBounds(20,10,50,20);
		//add(minknop);
		minknop.addActionListener(this);
		
		
		wisknop = new JButton(GeomAlgebra.rb.getString("wisknopLabel"));
		wisknop.setBounds(10,10,70,20);
		add(wisknop);
		wisknop.addActionListener(this);
		
		vorigeKnop = new JButton(GeomAlgebra.rb.getString("terugknopLabel"));
		vorigeKnop.setBounds(95,10,70,20);
		add(vorigeKnop);
		vorigeKnop.addActionListener(this);
		
		xknop = new JButton("x");
		xknop.setBounds(345,10,45,20);
		add(xknop);
		xknop.addActionListener(this);
		
		yknop = new JButton("y");
		yknop.setBounds(395,10,45,20);
		add(yknop);
		yknop.addActionListener(this);
		
		zknop = new JButton("z");
		zknop.setBounds(445,10,45,20);
		add(zknop);
		zknop.addActionListener(this);
		
		constantKnoppen = new Button[11];
		for(int i=1 ; i<11 ; i++)
		{	constantKnoppen[i] = new Button(Integer.toString(i));
			constantKnoppen[i].setBounds(80+25*i,10,20,20);
			//add(constantKnoppen[i]);
			constantKnoppen[i].addActionListener(this);
			constantKnoppen[i].setVisible(true);
		}
		
		dop = new JCheckBox(GeomAlgebra.rb.getString("DScheckboxLabel"));
		dop.setBackground(getBackground());
		dop.addItemListener(this);
		dop.setBounds(180,10,150,20);
		add(dop);
	}
	
	public void actionPerformed(ActionEvent e)
	{	if(e.getSource() == minknop)
		{	eigenaar.maakBasisNegatief();
		}
		
		if(e.getSource() == wisknop)
		{	eigenaar.wis();
		}
		
		if(e.getSource() == vorigeKnop)
		{	eigenaar.maakOngedaan();
		}
			
		for(int i=1 ; i<11 ; i++)
		{	if(e.getSource() == constantKnoppen[i])
			{	eigenaar.zetBasis(i);
			}
		}
		
		if(e.getSource() == xknop)
		{	eigenaar.zetVarBasis(1);
		}
		if(e.getSource() == yknop)
		{	eigenaar.zetVarBasis(2);
		}
		if(e.getSource() == zknop)
		{	eigenaar.zetVarBasis(3);
		}
	}
	public void itemStateChanged(ItemEvent e)
	{	//eigenaar.zetDirectOptellen(dop.getState());
		eigenaar.zetDirectOptellen(dop.isSelected());
	}
}