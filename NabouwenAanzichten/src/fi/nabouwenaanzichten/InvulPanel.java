package fi.nabouwenaanzichten;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

class InvulPanel extends JPanel implements ActionListener //ItemListener
{
	//private CheckboxGroup g;
	private ButtonGroup g;
	//private Checkbox cGeen;
	private JRadioButton cGeen;
	//private Checkbox[] checkboxen;
	private JRadioButton[] checkboxen;
	private int aantalCheckboxen;
	private int keuze;
	private ActionListener actionListener;

	
	public InvulPanel(String[] items, int x, int y, int b, int h)
	{	setLayout(null);
		setBackground(Color.white);
		setBounds(x,y,b,h);
		//g = new CheckboxGroup();
		g = new ButtonGroup();
		aantalCheckboxen = 0;
		//checkboxen = new Checkbox[items.length];
		checkboxen = new JRadioButton[items.length];
		for(int i=0 ; i<items.length ; i++)
		{	addCheckbox(items[i],0,h/2*i,b,h/2);
		}
		keuze = 1;
		//checkboxen[0].setState(true);
		checkboxen[0].setSelected(true);
		//cGeen = new Checkbox("", g, false);
		cGeen = new JRadioButton("");
		g.add(cGeen);
	}
		
	private void addCheckbox(String naam, int x, int y, int b, int h)
	{	//checkboxen[aantalCheckboxen] = new Checkbox(naam, g, false);
		checkboxen[aantalCheckboxen] = new JRadioButton(naam);
		g.add(checkboxen[aantalCheckboxen]);
		checkboxen[aantalCheckboxen].setBounds(x,y,b,h);
		checkboxen[aantalCheckboxen].setFont(new Font("SansSerif",Font.PLAIN,h/2+3));
		//checkboxen[aantalCheckboxen].addItemListener(this);
		checkboxen[aantalCheckboxen].addActionListener(this);
		checkboxen[aantalCheckboxen].setOpaque(false);
        
		add(checkboxen[aantalCheckboxen]);
		aantalCheckboxen++;
	}
	
	//public void itemStateChanged(ItemEvent e)
	public void actionPerformed(ActionEvent e)
	{	//Checkbox cSelect = g.getSelectedCheckbox();
		//for(int i=0 ; i<aantalCheckboxen ; i++)
		//{	if (cSelect==checkboxen[i])keuze = i+1;
		//}
		
		for (int i = 0; i < aantalCheckboxen;  i++)
		{	if (checkboxen[i].isSelected())
				keuze = i+1;
		}
		
		
		if(actionListener!=null)
		{	actionListener.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, ""));
		}
	}
	
	public int geefKeuze()
	{	return keuze;
	}
	
	public void setItem(int n)
	{	for(int i=0 ; i<aantalCheckboxen; i++)
		{	if (n == i)
				checkboxen[i].setSelected(true);
				//checkboxen[i].setState(true);
			else 
				checkboxen[i].setSelected(false);
				//checkboxen[i].setState(false);
		}
	}
	
	public void maakLeeg()
	{	//cGeen.setState(true);
		cGeen.setSelected(true);
		keuze = 0;
	}
	
	public void addActionListener(ActionListener listener)
	{	actionListener = AWTEventMulticaster.add(actionListener, listener);
	}
	
	public void removeActionListener(ActionListener listener)
	{	actionListener = AWTEventMulticaster.remove(actionListener, listener);
	}
	
}
