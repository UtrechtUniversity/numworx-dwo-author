package fi.graphtool;

import java.awt.AWTEventMulticaster;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.Serializable;
import java.net.URL;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.ListCellRenderer;

import fi.wiskopdr.WiskOpdr;


public class TekenComponent extends JPanel {

	// cursor
	public static int NOCUR = 0; 
	public static int DRAW = 1;
	public static int DELETE = 2;
	public static int DRAG = 3;
	private int cursorMode = NOCUR;	
	
	// verbindingen	
	public static int NONE = 0;
	public static int LINES = 1;	
	public static int CURVE = 2;	
	public static int CURVE_EXTRA = 3;
	private int connectMode = NONE;
	
	private GraphToolToggleKnop drawButton, deleteButton, dragButton;
	private JToggleButton noneButton;
	private ButtonGroup cursorModeGroup;

	private GraphToolToggleKnop puntenButton, lijnenButton, krommeButton, extrapoleerButton;
	private ButtonGroup connectModeGroup;

	private JComboBox grKeuze;
	private GraphToolKnop resetButton;	

	private GraphToolInteractiePanel grafiekComponent;		
	
	private boolean frozen = false;
	private int aantalGrafieken = 3;
	
	private boolean lijnenZichtbaar = true;
	private boolean krommeZichtbaar = true;
	private boolean extrapoleerZichtbaar = true;
	
	
	public TekenComponent()
	{
		setLayout(null);
		setBackground(new Color(210,210,210));
		
		cursorModeGroup = new ButtonGroup();
		drawButton = new GraphToolToggleKnop("teken_penknop.gif");
		
		drawButton.setBounds(2, 2, 20, 20);
		add(drawButton);
		
		deleteButton = new GraphToolToggleKnop("teken_gumknop.gif");
		deleteButton.setBounds(24, 2, 20, 20);
		add(deleteButton);
		
		dragButton = new GraphToolToggleKnop("teken_cursorknop.gif");
		dragButton.setBounds(46, 2, 20, 20);
		add(dragButton);
		
		noneButton = new JToggleButton("NoneSelected",true);
		
		cursorModeGroup.add(drawButton);
		cursorModeGroup.add(deleteButton);
		cursorModeGroup.add(dragButton);
		cursorModeGroup.add(noneButton);
		
		CursorModeIL iListener = new CursorModeIL();
		drawButton.addItemListener(iListener);
		deleteButton.addItemListener(iListener);
		dragButton.addItemListener(iListener);
		
		DrawButtonsAL aListener = new DrawButtonsAL();
		drawButton.addActionListener(aListener);
		deleteButton.addActionListener(aListener);
		dragButton.addActionListener(aListener);

		connectModeGroup = new ButtonGroup();
		
		puntenButton = new GraphToolToggleKnop("teken_puntenknop.gif");
		puntenButton.setBounds(76, 2, 20, 20);
		add(puntenButton);
		puntenButton.setSelected(true);
		puntenButton.zetActief(true);
		
		lijnenButton = new GraphToolToggleKnop("teken_lijnenknop.gif");
		lijnenButton.setBounds(98, 2, 20, 20);
		add(lijnenButton);
		
		krommeButton = new GraphToolToggleKnop("teken_krommeknop.gif");
		krommeButton.setBounds(120, 2, 20, 20);
		add(krommeButton);
		
		extrapoleerButton = new GraphToolToggleKnop("teken_extrapoleerknop.gif");
		extrapoleerButton.setBounds(142, 2, 20, 20);
		add(extrapoleerButton);
		
		connectModeGroup.add(puntenButton);
		connectModeGroup.add(lijnenButton);
		connectModeGroup.add(krommeButton);
		connectModeGroup.add(extrapoleerButton);
		
		ConnectModeIL ciListener = new ConnectModeIL();
		puntenButton.addItemListener(ciListener);
		lijnenButton.addItemListener(ciListener);
		krommeButton.addItemListener(ciListener);
		extrapoleerButton.addItemListener(ciListener);
		
		ConnectModeAL caListener = new ConnectModeAL();
		puntenButton.addActionListener(caListener);
		lijnenButton.addActionListener(caListener);
		krommeButton.addActionListener(caListener);
		extrapoleerButton.addActionListener(caListener);
		
		grKeuze = new JComboBox(){
			public void paintComponent(Graphics g)
			{	for(int i = 0; i < 10; i++)
				{	g.setColor(new Color(200+5*(7*i+9)/9,200+5*(7*i+9)/9,200+5*(7*i+9)/9));
					g.fillRect(0,getHeight() - (i+1)*getHeight()/10, getWidth(),getHeight()/10+1);
				}
				g.setColor(getBackground().darker());
				g.drawLine(0,0,getSize().width-1,0);
				g.drawLine(0,0,0,getSize().height-1);
				g.drawLine(0,getSize().height-1,getSize().width-1,getSize().height-1);
				g.setColor(getForeground());
				g.drawString(getSelectedItem().toString(), 2, getHeight() - 5);
			}
		};
		grKeuze.setBounds(172, 2, 50, 20);
		grKeuze.setBackground(new Color(210,210,210));
		add(grKeuze);
		grKeuze.addItem("Gr 1");
		grKeuze.addItem("Gr 2");
		grKeuze.addItem("Gr 3");
		
		grKeuze.setRenderer(new GrKeuzeRenderer());
		grKeuze.addActionListener(new NumGraphAL());
		
		resetButton = new GraphToolKnop("teken_wisknop.gif", 0);
		resetButton.setBounds(100,2,20,20);
		add(resetButton);	
		resetButton.addActionListener(new ResetAL());
	}
	
	public ImageIcon maakImageIcon(String s)
	{
		URL imageURL = GraphTool.class.getResource(s);
		ImageIcon imageIcon = new ImageIcon();
		if (imageURL != null) 
		{
		imageIcon = new ImageIcon(imageURL);
		}
		else
		{
			System.out.println("Error reading " + s);
		}
		return imageIcon;
	}
	
	public void zetAantalGrafieken(int i)
	{	aantalGrafieken = i;
		if(i == 1)
		{	grKeuze.setVisible(false);
			return;
		}
		else
			grKeuze.setVisible(true);
		grKeuze.removeAllItems();
		grKeuze.addItem("Gr 1");
		grKeuze.addItem("Gr 2");
		if(i == 3)
			grKeuze.addItem("Gr 3");
		
	}
	
	public void zetLijnenKnoppen(boolean lijnen, boolean kromme, boolean extrapoleer)
	{
		lijnenZichtbaar = lijnen;
		krommeZichtbaar = kromme;
		extrapoleerZichtbaar = extrapoleer;
		lijnenButton.setVisible(lijnen);
		krommeButton.setVisible(kromme);
		extrapoleerButton.setVisible(extrapoleer);
		
		int xLocatie = 98;
		if(lijnen)
			xLocatie += 22;
		krommeButton.setLocation(xLocatie, 2);
		if(kromme)
			xLocatie += 22;
		extrapoleerButton.setLocation(xLocatie, 2);
		if(extrapoleer)
			xLocatie += 22;
		grKeuze.setLocation(xLocatie + 8, 2);
	}
	
	public void paintComponent(Graphics g)
	{	//if("GR".equals(WiskOpdr.deployVariant)) ;
		//if("MW".equals(WiskOpdr.deployVariant) || "GR".equals(WiskOpdr.deployVariant))super.paintComponent(g);
		//else					
		for(int i=0 ; i<10 ; i++)
		{	g.setColor(new Color(200+5*i,200+5*i,200+5*i));
			g.fillRect(0,getHeight() - (i+1)*getHeight()/10, getWidth(),getHeight()/10+1);
			
		}
		
		g.setColor(Color.lightGray);
		int knopBreedte = 22;
		int aantalLijnKnoppen = 1;
		if(lijnenZichtbaar)
			aantalLijnKnoppen++;
		if(krommeZichtbaar)
			aantalLijnKnoppen++;
		if(extrapoleerZichtbaar)
			aantalLijnKnoppen++;
		/*
		if(lijnenZichtbaar && krommeZichtbaar && extrapoleerZichtbaar)
			aantalLijnKnoppen = 4;
		else if(krommeZichtbaar || extrapoleerZichtbaar)
			aantalLijnKnoppen = 3;
			*/
		
		g.drawRect(0, 0, 3*knopBreedte + 2, getSize().height - 1);
		//74 = 0 + 68 + 6; 90 = 4*22 + 2, ofwel 4*20 + 5*2
		g.drawRect(3*knopBreedte + 8, 0, aantalLijnKnoppen * knopBreedte + 2, getSize().height - 1);
		g.drawRect((3 + aantalLijnKnoppen) * knopBreedte + 16, 0, getSize().width - ((3 + aantalLijnKnoppen) * knopBreedte + 17), getSize().height - 1);
		
		setBorder(BorderFactory.createLineBorder(Color.lightGray));
	}
	
	public void setBounds(int x, int y, int b, int h)
	{	super.setBounds(x, y, b, h);
		setLocations(b, h);
	}

	public void setSize(int b, int h)
	{	super.setSize(b, h);
		setLocations(b, h);
	}
	
	private void setLocations(int b, int h)
	{	resetButton.setLocation(getSize().width - 22, 2);
	}
	
	public int getCursorMode()
	{	return cursorMode;
	}

	public void setConnectMode(int mode)
	{	if ((mode < 0) || (mode > CURVE_EXTRA))
			connectMode = NONE;
		if (connectMode == NONE)
		{	puntenButton.setSelected(true);
			puntenButton.zetActief(true);
		}
		else if (connectMode == LINES)
		{	lijnenButton.setSelected(true); 
			lijnenButton.zetActief(true);
		}
		else if (connectMode == CURVE)
		{	krommeButton.setSelected(true);
			krommeButton.zetActief(true);
		}
		else if (connectMode == CURVE_EXTRA)
		{	extrapoleerButton.setSelected(true);
			extrapoleerButton.zetActief(true);
		}
	}

	public int getConnectMode()
	{	return connectMode;
	}
	
	public void zetGrafiekComponent(GraphToolInteractiePanel gc)
	{	grafiekComponent = gc;
		grKeuze.setForeground(grafiekComponent.getFormuleColor(0));
	}
	
	public Hashtable getState()
	{	int connectMode = NONE;		
		connectMode = this.connectMode;
		
		Hashtable h = new Hashtable();
		h.put("connectMode", new Integer(connectMode));		
		return h;
	}
	
	public void setState(Hashtable h)
    {	
		int connectMode = NONE;				
		
		if(h.containsKey("connectMode")) 
			connectMode = ((Integer)h.get("connectMode")).intValue();
    	
		this.connectMode = connectMode;						
		setConnectMode(connectMode);	
		
	}
	
	public void setFrozen(boolean b)
    {
    	frozen = b;
    	drawButton.setEnabled(!b);
    }
	
	public void zetSelectedIndexGrKeuze(int i)
	{	grKeuze.setSelectedIndex(i);
		
	}
	
	boolean cursorItemChanged = false;
	
	class CursorModeIL implements ItemListener
	{	public void itemStateChanged(ItemEvent e)
		{	
			if (frozen)
				return;
		
			cursorItemChanged = true;
			if (drawButton.isSelected())
			{	cursorMode = DRAW;
				deleteButton.zetActief(false);
				dragButton.zetActief(false);
			}
			else if (deleteButton.isSelected())
			{	cursorMode = DELETE;
				drawButton.zetActief(false);
				dragButton.zetActief(false);
			}
			else if (dragButton.isSelected())
			{	cursorMode = DRAG;
				drawButton.zetActief(false);
				deleteButton.zetActief(false);
			}		
			else 
			{	cursorMode = NOCUR;
				
			}
			grafiekComponent.repaint();
		}
	}
	
	class DrawButtonsAL implements ActionListener
	{	public void actionPerformed(ActionEvent e)
		{	if (frozen)
				return;
			
			if (!cursorItemChanged)
			{	noneButton.setSelected(true);
				
			}
			
			cursorItemChanged = false;
			
		}
	}

	class ConnectModeIL implements ItemListener
	{	public void itemStateChanged(ItemEvent e)
		{	
			if (frozen)
				return;
			
			if (puntenButton.isSelected())
			{	connectMode = NONE;
				lijnenButton.zetActief(false);
				krommeButton.zetActief(false);
				extrapoleerButton.zetActief(false);
			}
			else if (lijnenButton.isSelected())
			{	connectMode = LINES;
				puntenButton.zetActief(false);
				krommeButton.zetActief(false);
				extrapoleerButton.zetActief(false);
			}
			else if (krommeButton.isSelected())
			{	connectMode = CURVE;
				puntenButton.zetActief(false);
				lijnenButton.zetActief(false);
				extrapoleerButton.zetActief(false);
			}			
			else if (extrapoleerButton.isSelected())
			{	connectMode = CURVE_EXTRA;
				puntenButton.zetActief(false);
				lijnenButton.zetActief(false);
				krommeButton.zetActief(false);
			}			
			grafiekComponent.repaint();
		}
	}
	
	class ConnectModeAL implements ActionListener
	{	public void actionPerformed(ActionEvent e)
		{	if (frozen)
				return;
			
			if(puntenButton.isSelected())
				puntenButton.zetActief(true);
			if(lijnenButton.isSelected())
				lijnenButton.zetActief(true);
			if(krommeButton.isSelected())
				krommeButton.zetActief(true);
			if(extrapoleerButton.isSelected())
				extrapoleerButton.zetActief(true);
				
		}
	}
	
	class NumGraphAL implements ActionListener
	{	public void actionPerformed(ActionEvent e)
		{	
			int index = grKeuze.getSelectedIndex();
			if(index >= 0)
			{	//if(grafiekComponent.typeOpdracht > 0)
				grKeuze.setForeground(grafiekComponent.getFormuleColor(index));
				
				//grKeuze.setForeground(grafiekComponent.opdrachtKleuren[index]);
				//else
				//	grKeuze.setForeground(grafiekComponent.getColor(index));
				grafiekComponent.setActiveIndex(index + 1, false);
			}

		}
	}
	
	class ResetAL implements ActionListener
	{	public void actionPerformed(ActionEvent e)
		{	if (frozen)
			return;
			
			if(!e.getActionCommand().equals("focus"))
			{	grafiekComponent.removePoints(grafiekComponent.getActiveIndex(), false);
				grafiekComponent.repaint();
			}
			
		}
	}
	
	class GrKeuzeRenderer extends JLabel implements ListCellRenderer 
	{
     	public GrKeuzeRenderer()
     	{	setOpaque(true);
        }
     	public Component getListCellRendererComponent(
         					JList list,
         					Object value,
         					int index,
         					boolean isSelected,
         					boolean cellHasFocus)
     	{
         	setText(value.toString());
         	if (isSelected)
         		setBackground(Color.white);
         	else	
         		setBackground(new Color(210, 210, 210));
         	if (index >= 0)	
         		setForeground(grafiekComponent.getFormuleColor(index));
         	return this;
     	}
 	}
	
	
	//ActionProducer
	private ActionListener actionListener = null;
	
	public void addActionListener(ActionListener l) 
 	{	actionListener = AWTEventMulticaster.add(actionListener,l);
 	}
 	
 	public void removeActionListener(ActionListener l)
 	{	actionListener = AWTEventMulticaster.remove(actionListener, l);
 	}	
 	
 	public void produceAction(String command)
 	{	if (actionListener != null)
 		{	actionListener.actionPerformed( new ActionEvent(this, 0, command) );
 		}
 	}
 	//end ActionProducer
	}


