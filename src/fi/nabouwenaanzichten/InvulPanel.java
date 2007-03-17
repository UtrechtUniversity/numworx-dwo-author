package fi.nabouwenaanzichten;

import java.awt.*;
import java.awt.event.*;

class InvulPanel extends Panel implements ItemListener
{
	private CheckboxGroup g;
	private Checkbox cGeen;
	private Checkbox[] checkboxen;
	private int aantalCheckboxen;
	private int keuze;
	private ActionListener actionListener;

	
	public InvulPanel(String[] items, int x, int y, int b, int h)
	{	setLayout(null);
		setBackground(Color.white);
		setBounds(x,y,b,h);
		g = new CheckboxGroup();
		aantalCheckboxen = 0;
		checkboxen = new Checkbox[items.length];
		for(int i=0 ; i<items.length ; i++)
		{	addCheckbox(items[i],0,h/2*i,b,h/2);
		}
		keuze = 1;
		checkboxen[0].setState(true);
		cGeen = new Checkbox("", g, false);
	}
		
	private void addCheckbox(String naam, int x, int y, int b, int h)
	{	checkboxen[aantalCheckboxen] = new Checkbox(naam, g, false);
		checkboxen[aantalCheckboxen].setBounds(x,y,b,h);
		checkboxen[aantalCheckboxen].setFont(new Font("SansSerif",Font.PLAIN,h/2+3));
		checkboxen[aantalCheckboxen].addItemListener(this);
		add(checkboxen[aantalCheckboxen]);
		aantalCheckboxen++;
	}
	
	public void itemStateChanged(ItemEvent e)
	{	Checkbox cSelect = g.getSelectedCheckbox();
		for(int i=0 ; i<aantalCheckboxen ; i++)
		{	if (cSelect==checkboxen[i])keuze = i+1;
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
		{	if(n==i)checkboxen[i].setState(true);
			else checkboxen[i].setState(false);
		}
	}
	
	public void maakLeeg()
	{	cGeen.setState(true);
		keuze = 0;
	}
	
	public void addActionListener(ActionListener listener)
	{	actionListener = AWTEventMulticaster.add(actionListener, listener);
	}
	
	public void removeActionListener(ActionListener listener)
	{	actionListener = AWTEventMulticaster.remove(actionListener, listener);
	}
	
}
