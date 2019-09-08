package fi.wiskopdr.opdrnav;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import fi.wiskopdr.WiskOpdr;
import fi.wiskopdr.WiskOpdrRadioButton;

public class ActKeuzePanel extends JPanel implements ItemListener, ActionListener, MouseListener, FocusListener
{
	private ButtonGroup g;
	private JRadioButton cGeen;
	private JRadioButton[] checkboxen;
	private int aantalCheckboxen;
	private int keuze;
	private ActionListener actionListener;
	
	private JButton[] editButtons;
	private JTextField[] editFields;
	private boolean editMode;
    

	
	public ActKeuzePanel(String[] items, int x, int y, int b, int h)
	{	setLayout(null);
		setBackground(Color.white);
		setBounds(x,y,b,h);
		g = new ButtonGroup();
		aantalCheckboxen = 0;
		checkboxen = new WiskOpdrRadioButton[items.length];
		for(int i=0 ; i<items.length ; i++)
		{	addCheckbox(items[i],0,20*i,b,20);
		}
		keuze = 1;
		checkboxen[0].setSelected(true);
		cGeen = new WiskOpdrRadioButton("", false);
		g.add(cGeen);
	}
	
	public ActKeuzePanel(String[] items, int x, int y, int b, int h, boolean editMode)
	{	setLayout(null);
		setBackground(Color.white);
		setBounds(x,y,b,h);
		g = new ButtonGroup();
		aantalCheckboxen = 0;
		checkboxen = new WiskOpdrRadioButton[items.length];
		for(int i=0 ; i<items.length ; i++)
		{	addCheckbox(items[i],0,20*i,b,20);
			
		}
		keuze = 1;
		checkboxen[0].setSelected(true);
		cGeen = new JRadioButton("", false);
		
		this.editMode = editMode;
		editButtons = new JButton[aantalCheckboxen];
		for (int i = 0; i<aantalCheckboxen; i++) 
		{	editButtons[i] = new JButton("edit");
			editButtons[i].setBounds(b-40,20*i+2,40,16);
			editButtons[i].addActionListener(this);
			//add(editButtons[i],0);
	    }
	    editFields = new JTextField[aantalCheckboxen];
		for (int i = 0; i<aantalCheckboxen; i++) 
		{	editFields[i] = new JTextField();
			editFields[i].setBounds(20,0,b-20,20);
			editFields[i].addActionListener(this);
			editFields[i].addFocusListener(this);
			editFields[i].setVisible(false);
			checkboxen[i].add(editFields[i],0);//add(editFields[i],0);
	    }
		
	}
		
	public void setBackground(Color c)
	{
		Component[] components = getComponents();
		for(int i=0 ; i<components.length ; i++)
		{
			components[i].setBackground(c);
		}
		
	}
		
	private void addCheckbox(String naam, int x, int y, int b, int h)
	{	checkboxen[aantalCheckboxen] = new WiskOpdrRadioButton(naam, false);
		checkboxen[aantalCheckboxen].setLayout(null);
		checkboxen[aantalCheckboxen].setBounds(x,y,b,h);
		checkboxen[aantalCheckboxen].setBackground(getBackground());
		checkboxen[aantalCheckboxen].setFont(new Font("SansSerif",Font.PLAIN,12));
        if(WiskOpdr.deployVariant!=null && WiskOpdr.deployVariant.equals("GR"))checkboxen[aantalCheckboxen].setFont(new Font("SansSerif",Font.PLAIN,12));
		checkboxen[aantalCheckboxen].addItemListener(this);
		add(checkboxen[aantalCheckboxen],0);
		g.add(checkboxen[aantalCheckboxen]);
		checkboxen[aantalCheckboxen].addMouseListener(this);
		aantalCheckboxen++;
		repaint();
	}
	
	public void itemStateChanged(ItemEvent e)
	{	JRadioButton cSelect = (JRadioButton)e.getSource();
		for(int i=0 ; i<aantalCheckboxen ; i++)
		{	if (cSelect==checkboxen[i])keuze = i+1;
		}
		if(actionListener!=null)
		{	actionListener.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, ""));
		}
		repaint();
	}
	
	public int geefKeuze()
	{	return keuze;
	}
	
	public void setItem(int n)
	{	for(int i=0 ; i<aantalCheckboxen; i++)
		{	if(n==i)checkboxen[i].setSelected(true);
			else checkboxen[i].setSelected(false);
		}
		keuze = n+1;
	}
	
	public void setNames(String[] names)
	{	for(int i=0 ; i<aantalCheckboxen; i++)
		{	checkboxen[i].setLabel(names[i]);
		}
	}
	
	public void maakLeeg()
	{	cGeen.setSelected(true);
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
	
	public void mousePressed(MouseEvent e)
	{	if(e.getModifiers()== InputEvent.BUTTON3_MASK || e.isControlDown())
		{	for (int i = 0; i<aantalCheckboxen; i++) 
			{	if(e.getSource()==checkboxen[i])
				{	editFields[i].setVisible(true);
					editFields[i].setText(checkboxen[i].getLabel());
					editFields[i].requestFocus();
				}
			}
		}
	}
	public void mouseReleased(MouseEvent e){;}
	public void mouseExited(MouseEvent e){;}
	public void mouseClicked(MouseEvent e){;}
	public void mouseEntered(MouseEvent e)
	{	//if(tekstVak!=null)tekstVak.requestFocus();
	}
	
	public void focusLost(FocusEvent e)
	{	
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
	public void focusGained(FocusEvent e)
	{
		
	}
	
	public void addActionListener(ActionListener listener)
	{	actionListener = AWTEventMulticaster.add(actionListener, listener);
	}
	
	public void removeActionListener(ActionListener listener)
	{	actionListener = AWTEventMulticaster.remove(actionListener, listener);
	}
	
}
