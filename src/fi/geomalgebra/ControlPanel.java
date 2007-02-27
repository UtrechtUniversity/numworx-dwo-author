package fi.geomalgebra;

import java.awt.Polygon;
import java.awt.*;
import java.util.*;
import java.awt.event.*;
import fi.geomalgebra.text.*;

class ControlPanel extends Panel implements ActionListener , ItemListener
{	
	AlgebraVeld eigenaar;
	Button[] constantKnoppen;
	Button minknop,xknop,yknop,zknop,wisknop,vorigeKnop, volgendeKnop;
	Checkbox dop;
	//NumberSlider ns;

	
	public ControlPanel(AlgebraVeld av)
	{	
		eigenaar = av;
		
		setBackground(new Color(150,150,150));
		
		//ns = new NumberSlider(0,400,150,0,"","");
		//ns.setValue(150);
		//ns.setBounds(500,10,100,20);
		//add(ns);
		//ns.addNumberListener(this);
		
		
		
		minknop = new Button(GeomAlgebra.rb.getString("minknopLabel"));
		minknop.setBounds(20,10,50,20);
		//add(minknop);
		minknop.addActionListener(this);
		
		
		wisknop = new Button(GeomAlgebra.rb.getString("wisknopLabel"));
		wisknop.setBounds(700,10,70,20);
		add(wisknop);
		wisknop.addActionListener(this);
		
		vorigeKnop = new Button(GeomAlgebra.rb.getString("terugknopLabel"));
		vorigeKnop.setBounds(620,10,70,20);
		add(vorigeKnop);
		vorigeKnop.addActionListener(this);
		
		xknop = new Button("x");
		xknop.setBounds(400,10,20,20);
		add(xknop);
		xknop.addActionListener(this);
		
		yknop = new Button("y");
		yknop.setBounds(425,10,20,20);
		add(yknop);
		yknop.addActionListener(this);
		
		zknop = new Button("z");
		zknop.setBounds(450,10,20,20);
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
		
		dop = new Checkbox(GeomAlgebra.rb.getString("DScheckboxLabel"));
		dop.addItemListener(this);
		dop.setBounds(500,10,140,20);
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
	{	eigenaar.zetDirectOptellen(dop.getState());
	}
}