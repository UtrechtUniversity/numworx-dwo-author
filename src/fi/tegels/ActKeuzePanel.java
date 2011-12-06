package fi.tegels;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

public class ActKeuzePanel extends JPanel implements ActionListener //ItemListener
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

	Font font;
	FontMetrics fm;
	int height;
	
	public ActKeuzePanel(String[] items, int x, int y, int b, int h, Color bgColor)
	{	setLayout(null);
		setBackground(bgColor);
		setBounds(x, y, b, h);
		
		font = new Font("Dialog", Font.PLAIN, 12);
		fm = getFontMetrics(font);
		height = 3 * fm.getHeight() / 2;
		
		//g = new CheckboxGroup();
		g = new ButtonGroup();
		aantalCheckboxen = 0;
		//checkboxen = new Checkbox[items.length];
		checkboxen = new JRadioButton[items.length];
		for (int i = 0; i < items.length; i++)
		{	//addCheckbox(items[i], 0, height * i, b, height);
			addCheckbox(items[i], (b / items.length) * i, 0, (b / items.length), height);
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
		checkboxen[aantalCheckboxen].setFont(font);
		checkboxen[aantalCheckboxen].setBackground(getBackground());
		checkboxen[aantalCheckboxen].setBounds(x, y, b, h);
		//checkboxen[aantalCheckboxen].setFont(new Font("SansSerif",Font.PLAIN,14));
		//checkboxen[aantalCheckboxen].addItemListener(this);
		checkboxen[aantalCheckboxen].addActionListener(this);
		add(checkboxen[aantalCheckboxen]);
		aantalCheckboxen++;
	}
	
	//public void itemStateChanged(ItemEvent e)
	public void actionPerformed(ActionEvent e)
	{	//Checkbox cSelect = g.getSelectedCheckbox();
		//for (int i = 0; i < aantalCheckboxen; i++)
		//{	if (cSelect == checkboxen[i])
		//		keuze = i + 1;
		//}
		
		for (int i = 0; i < aantalCheckboxen; i++)
		{	if (checkboxen[i].isSelected())
				keuze = i + 1;
		}
		
		if (actionListener != null)
		{	actionListener.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, ""));
		}
	}
	
	public int geefKeuze()
	{	return keuze;
	}
	
	public void setItem(int n)
	{	for (int i = 0; i < aantalCheckboxen; i++)
		{	if (n == i) 
			{	//checkboxen[i].setState(true);
				checkboxen[i].setSelected(true);
			}
			// dit is niet nodig
			else 
			{	//checkboxen[i].setState(false);
				checkboxen[i].setSelected(false);
			}
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
