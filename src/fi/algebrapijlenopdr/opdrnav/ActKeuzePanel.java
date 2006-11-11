package fi.algebrapijlenopdr.opdrnav;

import java.awt.*;
import java.awt.event.*;

public class ActKeuzePanel extends Panel implements ItemListener, ActionListener
{
	private CheckboxGroup g;
	private Checkbox cGeen;
	private Checkbox[] checkboxen;
	private int aantalCheckboxen;
	private int keuze;
	private ActionListener actionListener;
	
	private Button[] editButtons;
	private TextField[] editFields;
	private boolean editMode;

	
	public ActKeuzePanel(String[] items, int x, int y, int b, int h)
	{	setLayout(null);
		setBackground(Color.white);
		setBounds(x,y,b,h);
		g = new CheckboxGroup();
		aantalCheckboxen = 0;
		checkboxen = new Checkbox[items.length];
		for(int i=0 ; i<items.length ; i++)
		{	addCheckbox(items[i],0,20*i,b,20);
		}
		keuze = 1;
		checkboxen[0].setState(true);
		cGeen = new Checkbox("", g, false);
	}
	
	public ActKeuzePanel(String[] items, int x, int y, int b, int h, boolean editMode)
	{	setLayout(null);
		setBackground(Color.white);
		setBounds(x,y,b,h);
		g = new CheckboxGroup();
		aantalCheckboxen = 0;
		checkboxen = new Checkbox[items.length];
		for(int i=0 ; i<items.length ; i++)
		{	addCheckbox(items[i],0,20*i,b,20);
		}
		keuze = 1;
		checkboxen[0].setState(true);
		cGeen = new Checkbox("", g, false);
		
		this.editMode = editMode;
		editButtons = new Button[aantalCheckboxen];
		for (int i = 0; i<aantalCheckboxen; i++) 
		{	editButtons[i] = new Button("edit");
			editButtons[i].setBounds(b-40,20*i+2,40,16);
			editButtons[i].addActionListener(this);
			add(editButtons[i],0);
	    }
	    editFields = new TextField[aantalCheckboxen];
		for (int i = 0; i<aantalCheckboxen; i++) 
		{	editFields[i] = new TextField();
			editFields[i].setBounds(20,20*i,b-20,20);
			editFields[i].addActionListener(this);
			editFields[i].setVisible(false);
			add(editFields[i],0);
	    }
		
	}
		
	private void addCheckbox(String naam, int x, int y, int b, int h)
	{	checkboxen[aantalCheckboxen] = new Checkbox(naam, g, false);
		checkboxen[aantalCheckboxen].setBounds(x,y,b,h);
		checkboxen[aantalCheckboxen].setFont(new Font("SansSerif",Font.PLAIN,14));
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
	
	public String getlabel(int nr)
	{	return checkboxen[nr].getLabel();
	}
	
	public void actionPerformed(ActionEvent e)
	{	for (int i = 0; i<aantalCheckboxen; i++) 
		{	if(e.getSource()==editButtons[i])
			{	editFields[i].setVisible(true);
				editFields[i].setText(checkboxen[i].getLabel());
			}
		}
		for (int i = 0; i<aantalCheckboxen; i++) 
		{	if(e.getSource()==editFields[i])
			{	checkboxen[i].setLabel(editFields[i].getText());
				editFields[i].setVisible(false);
				if(actionListener!=null)
				{	actionListener.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "editLabel"));
				}
			}
		}
	}
	
	public void addActionListener(ActionListener listener)
	{	actionListener = AWTEventMulticaster.add(actionListener, listener);
	}
	
	public void removeActionListener(ActionListener listener)
	{	actionListener = AWTEventMulticaster.remove(actionListener, listener);
	}
	
}
