package fi.wiskopdr;

import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

import java.util.*;

import fi.wiskopdr.WiskOpdr;
import fi.wiskopdr.expressies.*;

import java.io.Serializable;

public class GrafiekTekenEditor extends JPanel
{	// grafieken
	public static int maxGraphs = 3;
	
	private int numGraphs = maxGraphs;
	private int activeIndex = 1;
	private Vector graphPoints = new Vector();
	private boolean eenGrafiek = false;

	// tekenopties
	private static Color dullGreen = new Color(57, 191, 19);	
	private Color[] colors = 
		{Color.blue, new Color(0,200,0), new Color(255,50,50)};
	
	
	
	public static int PRAD = 2;

	// cursor
	public static int NOCUR = 0; 
	public static int DRAW = 1;
	public static int DELETE = 2;
	public static int DRAGG = 3;
	private int cursorMode = NOCUR;
	private int oldCursorMode = NOCUR;

	// verbindingen	
	public static int NONE = 0;
	public static int LINES = 1;	
	public static int CURVE = 2;	
	private int connectMode = NONE;
	private boolean alleenPunten = false;

	private JToggleButton drawButton, deleteButton, draggButton, noneButton;
	private ButtonGroup cursorModeGroup;

	private JToggleButton puntenButton, lijnenButton, krommeButton;
	private ButtonGroup connectModeGroup;

	private JComboBox grKeuze;

	private JButton resetButton;	

	// hier bewaren, alleen gebruikt in grafiekComponent	
	private String varNaam = "x";
	private GrafiekComponent grafiekComponent;		
	
	private boolean frozen = false;
	
	private static String[] imageNames = 
	{	
		"teken_penknop_default.gif",
		"teken_penknop_rollover.gif",
		"teken_penknop_selected.gif",
		"teken_gumknop_default.gif",
		"teken_gumknop_rollover.gif",
		"teken_gumknop_selected.gif",
		"teken_cursorknop_default.gif",
		"teken_cursorknop_rollover.gif",
		"teken_cursorknop_selected.gif",	
		"teken_wisknop_default.gif",
		"teken_wisknop_rollover.gif",
		"teken_wisknop_selected.gif",	
		"teken_puntenknop_default.gif",
		"teken_puntenknop_rollover.gif",
		"teken_puntenknop_selected.gif",
		"teken_lijnenknop_default.gif",
		"teken_lijnenknop_rollover.gif",
		"teken_lijnenknop_selected.gif",
		"teken_krommeknop_default.gif",
		"teken_krommeknop_rollover.gif",
		"teken_krommeknop_selected.gif",
		"tekencursor.gif",
		"gumcursor.gif",
	};
	private static Hashtable images;
	
	public GrafiekTekenEditor()
	{	
		setLayout(null);
//		setBackground(Color.white);
		//setBorder(BorderFactory.createLineBorder(Color.gray));
		setBackground(new Color(210,210,210));
		//setOpaque(false);
		
		if(images==null)
		{	images = new Hashtable();
			WiskOpdr.loadImages(images,imageNames);
		}
//System.out.println("im = " + images.size());		

		cursorModeGroup = new ButtonGroup();

		drawButton = new JToggleButton(new ImageIcon(getImage("teken_penknop_default.gif")), false);
		drawButton.setRolloverIcon(
			new ImageIcon(getImage("teken_penknop_rollover.gif")));
		drawButton.setSelectedIcon(
			new ImageIcon(getImage("teken_penknop_selected.gif")));
		drawButton.setBorder(null);
		drawButton.setBounds(2, 2, 20, 20);
		add(drawButton);		

		deleteButton = new JToggleButton(new ImageIcon(getImage("teken_gumknop_default.gif")), false);
		deleteButton.setRolloverIcon(
			new ImageIcon(getImage("teken_gumknop_rollover.gif")));
		deleteButton.setSelectedIcon(
			new ImageIcon(getImage("teken_gumknop_selected.gif")));
		deleteButton.setBorder(null);
		deleteButton.setBounds(24,2, 20, 20);
		add(deleteButton);				

		draggButton = new JToggleButton(new ImageIcon(getImage("teken_cursorknop_default.gif")), false);
		draggButton.setRolloverIcon(
			new ImageIcon(getImage("teken_cursorknop_rollover.gif")));
		draggButton.setSelectedIcon(
			new ImageIcon(getImage("teken_cursorknop_selected.gif")));
		draggButton.setBorder(null);
		draggButton.setBounds(46, 2, 20, 20);
		add(draggButton);		
		
		noneButton = new JToggleButton("NoneSelected",true);
				
		cursorModeGroup.add(drawButton);
		cursorModeGroup.add(deleteButton);
		cursorModeGroup.add(draggButton);
		cursorModeGroup.add(noneButton);

		CursorModeIL iListener = new CursorModeIL();
		drawButton.addItemListener(iListener);
		deleteButton.addItemListener(iListener);
		draggButton.addItemListener(iListener);
		
		DrawButtonsAL aListener = new DrawButtonsAL();
		drawButton.addActionListener(aListener);
		deleteButton.addActionListener(aListener);
		draggButton.addActionListener(aListener);
		
		connectModeGroup = new ButtonGroup();

		puntenButton = new JToggleButton(new ImageIcon(getImage("teken_puntenknop_default.gif")), true);
		puntenButton.setRolloverIcon(
			new ImageIcon(getImage("teken_puntenknop_rollover.gif")));
		puntenButton.setSelectedIcon(
			new ImageIcon(getImage("teken_puntenknop_selected.gif")));
		puntenButton.setBorder(null);
		puntenButton.setBounds(76, 2, 20, 20);
		add(puntenButton);

		lijnenButton = new JToggleButton(new ImageIcon(getImage("teken_lijnenknop_default.gif")), true);
		lijnenButton.setRolloverIcon(new ImageIcon(getImage("teken_lijnenknop_rollover.gif")));
		lijnenButton.setSelectedIcon(new ImageIcon(getImage("teken_lijnenknop_selected.gif")));
		lijnenButton.setBorder(null);
		lijnenButton.setBounds(98, 2, 20, 20);
		add(lijnenButton);

		krommeButton = new JToggleButton(new ImageIcon(getImage("teken_krommeknop_default.gif")), true);
		krommeButton.setRolloverIcon(
			new ImageIcon(getImage("teken_krommeknop_rollover.gif")));
		krommeButton.setSelectedIcon(
			new ImageIcon(getImage("teken_krommeknop_selected.gif")));
		krommeButton.setBorder(null);
		krommeButton.setBounds(120, 2, 20, 20);
		add(krommeButton);

		connectModeGroup.add(puntenButton);
		connectModeGroup.add(lijnenButton);
		connectModeGroup.add(krommeButton);

		ConnectModeIL cListener = new ConnectModeIL();
		puntenButton.addItemListener(cListener);
		lijnenButton.addItemListener(cListener);
		krommeButton.addItemListener(cListener);

		
		grKeuze = new JComboBox();
		grKeuze.setBounds(150, 2, 50, 20);
		grKeuze.setBackground(new Color(210,210,210));
		//grKeuze.setBackground(Color.white);
		grKeuze.setForeground(colors[0]);
		add(grKeuze);
		grKeuze.addItem("Gr 1");
		grKeuze.addItem("Gr 2");
		grKeuze.addItem("Gr 3");
		
		grKeuze.setRenderer(new GrKeuzeRenderer());
		grKeuze.addActionListener(new NumGraphAL());

		resetButton = new JButton(new ImageIcon(getImage("teken_wisknop_default.gif")));
		resetButton.setRolloverIcon(
			new ImageIcon(getImage("teken_wisknop_rollover.gif")));
		resetButton.setPressedIcon(
			new ImageIcon(getImage("teken_wisknop_selected.gif")));
		resetButton.setBorder(null);
		resetButton.setBounds(100,2,20,20);
		add(resetButton);	
		resetButton.addActionListener(new ResetAL());
		
	}

	public void setColor(int nr, Color c)
	{
		colors[nr] = c;
//		repaint();
	}
	
	// redefined
	public void setVisible(boolean b)
	{	super.setVisible(b);
		if (!b)
		{	oldCursorMode = cursorMode;
			setCursorMode(NOCUR);
		}
		else
		{	//cursorMode = oldCursorMode;
			//setCursorMode(cursorMode);
			cursorMode = NOCUR;
			setCursorMode(cursorMode);
		}
	}

	public void zetEenGrafiek(boolean b)
	{	eenGrafiek = b;
		grKeuze.setVisible(!b);
	}
	
	public void zetAlleenPunten(boolean b)
	{	alleenPunten = b;
		lijnenButton.setVisible(!b);
		krommeButton.setVisible(!b);		
	}
	
	public void paintComponent(Graphics g)
	{	g.setColor(new Color(210,210,210));
		g.fillRect(0, 0, 68, getSize().height);
		g.fillRect(74, 0, 68, getSize().height);
		g.fillRect(148, 0, getSize().width - 148, getSize().height);
		
		g.setColor(Color.gray);
		g.drawRect(0, 0, 68, getSize().height-1);
		g.drawRect(74,0, 68, getSize().height-1);
		g.drawRect(148, 0, getSize().width- 148 - 1, getSize().height-1);
		
	}
	
	public static Image getImage(String name)
	{	return(Image)images.get(name);
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

	public void setCursorMode(int mode)
	{	if ((mode <= 0) || (mode > DRAGG))
			cursorMode = NOCUR;
		if (cursorMode == DRAW)
			drawButton.setSelected(true);
		else if (cursorMode == DELETE)
			deleteButton.setSelected(true); 	
		else if (cursorMode == DRAGG)
			draggButton.setSelected(true);
	}
	
	public int getNumGraphs()
	{	return numGraphs;
	}
	
	public int getActiveIndex()
	{	return activeIndex;
	}

	public void setActiveIndex(int index)
	{	if ((index < 1) || (index > maxGraphs))
			activeIndex = 1;
		else
			activeIndex = index;
			
		grKeuze.setSelectedIndex(index - 1);	

		grafiekComponent.repaint();			
	}

	public void setConnectMode(int mode)
	{	if ((mode < 0) || (mode > CURVE))
			connectMode = NONE;
		if (connectMode == NONE)
			puntenButton.setSelected(true);
		else if (connectMode == LINES)
			lijnenButton.setSelected(true); 	
		else if (connectMode == CURVE)
			krommeButton.setSelected(true);
	}

	public int getConnectMode()
	{	return connectMode;
	}
	
	public Color getColor(int index)
	{	return colors[index - 1];
	}
	
	public Vector getPoints(int index)
	{	Vector points = new Vector();
		for (int pCnt = 0; pCnt < graphPoints.size(); pCnt++)
		{	RealPoint rp = (RealPoint) graphPoints.elementAt(pCnt);
			if (rp.index == index)
				points.addElement(rp);
		}
		return points;
	}

	public void setPoints(Vector pts)
	{	graphPoints = pts;
	}

	public void addInsert(RealPoint newRP, boolean fireEvent)
	{	int pIndex = -1;
		boolean firstFound = false;
		for (int pCnt = 0; pCnt < graphPoints.size(); pCnt++)
		{	RealPoint rp = (RealPoint) graphPoints.elementAt(pCnt);
			if (!firstFound && rp.hasLargerXThen(newRP))
			{	pIndex = pCnt;
				firstFound = true;
			}
		}
		if (pIndex == -1)
			graphPoints.addElement(newRP);
		else
			graphPoints.insertElementAt(newRP, pIndex);	
		if (fireEvent)
			produceAction("points changed");
			
	}
	
	public void removePoints(int index)
	{	Vector rPoints = getPoints(index);
		for (int rCnt = 0; rCnt < rPoints.size(); rCnt++)
		{	RealPoint rp = (RealPoint) rPoints.elementAt(rCnt);
			graphPoints.removeElement(rp);
		}
		produceAction("points changed");
		
	}
	
	public void removeAllPoints()
	{
		graphPoints.removeAllElements();
		produceAction("points changed");
	}
	
	public void removePoint(RealPoint rp, boolean fireEvent)
	{	graphPoints.removeElement(rp);
		if (fireEvent)
			produceAction("points changed");
	}
	
	public boolean hasPointWithSameXAs(RealPoint aRp)
	{	boolean found = false;
		Vector rPoints = getPoints(aRp.index);
		for (int rCnt = 0; rCnt < rPoints.size(); rCnt++)
		{	RealPoint rp = (RealPoint) rPoints.elementAt(rCnt);
			if (aRp.hasSameXAs(rp))
				found = true;
		}
		return found;
	}
	
	public void zetVarNaam(String s)
	{	varNaam = s;
		grafiekComponent.zetVarNaam(varNaam);
	}

	public void zetGrafiekComponent(GrafiekComponent gc)
	{	grafiekComponent = gc;
	}

	public Hashtable getState()
	{	
		int cursorMode = DRAW;
		int connectMode = NONE;		
		int activeIndex	= 1;	 
		Vector graphPoints = new Vector();
		boolean eenGrafiek = false;
				
		cursorMode = this.cursorMode;
		connectMode = this.connectMode;
		activeIndex = this.activeIndex;
		graphPoints = this.graphPoints;
		eenGrafiek = this.eenGrafiek;

		Hashtable h = grafiekComponent.getState();

		h.put("cursorMode", new Integer(cursorMode));
		h.put("type verbinding", new Integer(connectMode));		
		h.put("aktieve grafiek", new Integer(activeIndex));
		h.put("grafiekpunten", graphPoints);
		h.put("eenGrafiek", new Boolean(eenGrafiek));

	    return h;
	}
	
	public Hashtable getEditState()
	{	
		String varNaam = "x";
		int connectMode = NONE;		
		int activeIndex	= 1;	 
		Vector graphPoints = new Vector();
		boolean eenGrafiek = false;
				
		varNaam = this.varNaam;		
		connectMode = this.connectMode;				
		activeIndex = this.activeIndex;
		graphPoints = this.graphPoints;
		eenGrafiek = this.eenGrafiek;
		
		Hashtable h = grafiekComponent.getState();

	    h.put("varNaam", varNaam);
		h.put("type verbinding", new Integer(connectMode));				
		h.put("aktieve grafiek", new Integer(activeIndex));
		h.put("grafiekpunten", graphPoints);
		h.put("eenGrafiek", new Boolean(eenGrafiek));
	    
	    return h;
	}
	
	public void zetOpdracht(Hashtable h, String[] randomVars, Hashtable randomValues)
	{	
		String varNaam = "x";
		int connectMode = NONE;				
		int activeIndex	= 1;	 
		Vector graphPoints = new Vector();
		boolean eenGrafiek = false;
	
    	if (h.containsKey("varNaam")) 
    		varNaam = (String) h.get("varNaam");
		if(h.containsKey("type verbinding")) 
			connectMode = ((Integer)h.get("type verbinding")).intValue();
    	if (h.containsKey("aktieve grafiek")) 
    		activeIndex = ((Integer) h.get("aktieve grafiek")).intValue();
    	if (h.containsKey("grafiekpunten")) 
    		graphPoints = (Vector) h.get("grafiekpunten");
    	if (h.containsKey("eenGrafiek"))
    		eenGrafiek = ((Boolean) h.get("eenGrafiek")).booleanValue();	
    	
    	this.varNaam = varNaam;
		this.connectMode = connectMode;		
		this.activeIndex = activeIndex;
		this.graphPoints = graphPoints;
		this.eenGrafiek = eenGrafiek;
    	
		grafiekComponent.zetVarNaam(varNaam);
		setConnectMode(connectMode);		
		setActiveIndex(activeIndex);
		zetEenGrafiek(eenGrafiek);
		
		grafiekComponent.setState(h);
		grafiekComponent.repaint();
    }
	
	public void setEditState(Hashtable h)
	{	
		String varNaam = "x";
		int connectMode = NONE;		
		int activeIndex	= 1;	 
		Vector graphPoints = new Vector();
		boolean eenGrafiek = false;		
	
    	if (h.containsKey("varNaam")) 
    		varNaam = (String) h.get("varNaam");
		if(h.containsKey("type verbinding")) 
			connectMode = ((Integer)h.get("type verbinding")).intValue();
    	if (h.containsKey("aktieve grafiek")) 
    		activeIndex = ((Integer) h.get("aktieve grafiek")).intValue();
    	if (h.containsKey("grafiekpunten")) 
    		graphPoints = (Vector) h.get("grafiekpunten");
    	if (h.containsKey("eenGrafiek"))
    		eenGrafiek = ((Boolean) h.get("eenGrafiek")).booleanValue();	
    		
    	
    	this.varNaam = varNaam;
		this.connectMode = connectMode;				
		this.activeIndex = activeIndex;
		this.graphPoints = graphPoints;
		this.eenGrafiek = eenGrafiek;		
		
		grafiekComponent.zetVarNaam(varNaam);
		setConnectMode(connectMode);				
		setActiveIndex(activeIndex);
		zetEenGrafiek(eenGrafiek);		

		grafiekComponent.setState(h);
		grafiekComponent.repaint();
    }
	
    public void setState(Hashtable h)
    {	
		int connectMode = NONE;				
		int activeIndex	= 1;	 
		Vector graphPoints = new Vector();
		boolean eenGrafiek = false;		
	
		if(h.containsKey("type verbinding")) 
			connectMode = ((Integer)h.get("type verbinding")).intValue();
    	if (h.containsKey("aktieve grafiek")) 
    		activeIndex = ((Integer) h.get("aktieve grafiek")).intValue();
    	if (h.containsKey("grafiekpunten")) 
    		graphPoints = (Vector) h.get("grafiekpunten");
    	
    		
    	if (h.containsKey("eenGrafiek"))
    		eenGrafiek = ((Boolean) h.get("eenGrafiek")).booleanValue();	
    		
    	
		this.connectMode = connectMode;						
		this.activeIndex = activeIndex;
		this.graphPoints = graphPoints;
		this.eenGrafiek = eenGrafiek;		
		
		setConnectMode(connectMode);				
		setActiveIndex(activeIndex);
		zetEenGrafiek(eenGrafiek);

		grafiekComponent.setState(h);
		grafiekComponent.repaint();
    }

    public void setFrozen(boolean b)
    {
    	frozen = b;
    	drawButton.setEnabled(!b);
    	//deleteButton.setEnabled(!b);
    	//draggButton.setEnabled(!b);
    }
    
    
    
	boolean itemChanged = false;
	
	class CursorModeIL implements ItemListener
	{	public void itemStateChanged(ItemEvent e)
		{	
			if (frozen)
				return;
		
			itemChanged = true;
			if (drawButton.isSelected())
			{	cursorMode = DRAW;
			}
			else if (deleteButton.isSelected())
			{	cursorMode = DELETE;
			}
			else if (draggButton.isSelected())
			{	cursorMode = DRAGG;
			}		
			else 
			{	cursorMode = NOCUR;
				
			}
			grafiekComponent.repaint();
//System.out.println("cm IL");			
		}
	}
	
	class DrawButtonsAL implements ActionListener
	{	public void actionPerformed(ActionEvent e)
		{	
			if (frozen)
				return;
			
			if (!itemChanged)
			{	noneButton.setSelected(true);
			}
			
			itemChanged = false;
			
//System.out.println("cm AL");						
		}
	}

	class ConnectModeIL implements ItemListener
	{	public void itemStateChanged(ItemEvent e)
		{	
			if (frozen)
				return;
			
			if (puntenButton.isSelected())
			{	connectMode = NONE;
			}
			else if (lijnenButton.isSelected())
			{	connectMode = LINES;
			}
			else if (krommeButton.isSelected())
			{	connectMode = CURVE;
			}			
			grafiekComponent.repaint();
		}
	}
	
	class NumGraphAL implements ActionListener
	{	public void actionPerformed(ActionEvent e)
		{	
			int index = grKeuze.getSelectedIndex();
			grKeuze.setForeground(colors[index]);
			setActiveIndex(index + 1);
		}
	}
	
	class ResetAL implements ActionListener
	{	public void actionPerformed(ActionEvent e)
		{	
			if (frozen)
				return;
			removePoints(activeIndex);
			grafiekComponent.repaint();
			
		}
	}

	
	class GrKeuzeRenderer extends JLabel implements ListCellRenderer 
	{
     	public GrKeuzeRenderer()
     	{
        	setOpaque(true);
        	
        	
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
         	if ((index >= 0) && (index < colors.length))	
         		setForeground(colors[index]);
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

class RealPoint implements Serializable
{	public static final double NZERO = 1e-5d;

	public double x, y;
	public int index;
	// constructor 1
	public RealPoint(double x, double y)
	{	this.x = x;
		this.y = y;
	}
	// constructor 2
	public RealPoint(RealPoint rp)
	{	x = rp.x;
		y = rp.y;
		index = rp.index;
	}

    // redefine for method contains in Vector     
    // equality of this RealPoint and RealPoint u in Manhattan metric NZero
    public boolean equals(Object obj)
    {    if (obj instanceof RealPoint)
             return (((RealPoint) obj).index == index) &&
             		(Math.abs(x - ((RealPoint) obj).x) < NZERO) &&
    		   		(Math.abs(y - ((RealPoint) obj).y) < NZERO);
         return false;    
    }

	public boolean hasLargerXThen(RealPoint rp)
	{	return (index == rp.index) &&
			   (x > (rp.x + NZERO)); 	 
	}

	public boolean hasSameXAs(RealPoint rp)
	{	return (index == rp.index) &&
			   (Math.abs(x - rp.x) < NZERO); 	 
	}

	public RealPoint standarize()
	{	double length = Math.sqrt(x * x + y * y);
		if (length > NZERO)
			return new RealPoint(x / length, y / length);
		else 
			return new RealPoint(0, 0);	
	
	}
	
}