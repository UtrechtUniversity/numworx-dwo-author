package fi.grafiek3dtest.tekstobjects;

import java.awt.*;
import java.awt.event.*;
import java.util.Vector;
import javax.swing.*;

//import fi.wiskopdr.WiskOpdr;
import fi.grafiek3dtest.formuleobjects.*;


public class TekstArea extends JPanel implements ActionListener
{	
	private Image im;
	private Graphics gIm;
	private boolean resized = false;
	
	private boolean editable = false;
	private boolean selectable = true;
	
	private TekstVak tekstVak;
	private boolean borders;
	private boolean closeable;
	private FormuleButton closeButton;
	private boolean hasButton;
	private JButton tipButton;
	
	private int bovenMarge = 1;
	
	public TekstArea()
	{	setLayout(null);
		setBackground(getBackground());
		tekstVak = new TekstVak();
		tekstVak.setLocation(0,bovenMarge);
		add(tekstVak);
		tekstVak.setEditable(false);
		tekstVak.setSelectable(false);
		
	}
	
	public void setBackground(Color c)
	{	super.setBackground(c);
		Component[] components = getComponents();
		for(int i=0 ; i<components.length ; i++)
		{	if(!(components[i] instanceof JButton))
			components[i].setBackground(c);
		}
		
	}
	
	public void layoutTekst()
	{	tekstVak.layoutTekst();
	
	}
	public void setText(String s)
	{	tekstVak.zetTekst(s);
		tekstVak.setEditable(editable);
	    tekstVak.setSelectable(selectable);
	    if(tipButton!=null)
	    {	remove(tipButton);
	    	hasButton = false;
	    	tipButton = null;
	    }
	}
	
	public String getText()
	{	return tekstVak.toString();
	}
	
	public String getCompleteText()
	{	return tekstVak.toCompleteString();
	}
	
	public Vector geefInteractiePanels()
	{	return tekstVak.geefInteractiePanels();
	}
	
	public void setEditable(boolean b)
	{	editable = b;
		if(b)
		{	selectable = true;
			setBackground(Color.white);
		}
		tekstVak.setSelectable(b);
		tekstVak.setEditable(b);
	}
	
	public void destroy()
	{	if(gIm!=null)
		{	gIm.dispose();
			gIm = null;
		}
	}

	public void setFont(Font font)
	{	if(tekstVak==null)return;
		tekstVak.setFont(font);
	}
	
	/*public void paint(Graphics g)
	{	
		{ 	if(im==null || resized)
			{	if(resized && gIm!=null)
				{	gIm.dispose();
				}
				im = createImage(getSize().width,getSize().height);
  				gIm = im.getGraphics();
				resized = false;
			}
			gIm.setColor(getBackground());
			gIm.fillRect(0,0,getSize().width,getSize().height);
			if(editable || borders)
			{	gIm.setColor(Color.black);
				gIm.drawRect(0,0,getSize().width-1,getSize().height-1);
			}
			super.paint(gIm);
			g.drawImage(im, 0, 0, null);
  		}
	}*/
	
	public void setSize(int b, int h)
	{	resized = true;
		if(closeable)tekstVak.setSize(b-20,h);
		else tekstVak.setSize(b,h);
		super.setSize(b,h);
	}
	
	
	
	public void resize()
	{	resized = true;
		int buttonHeight = 0;
		if(hasButton) buttonHeight = 40;
		super.setSize(getSize().width, tekstVak.getSize().height + buttonHeight);
		
		if(getParent()instanceof TekstElement)((TekstElement)getParent()).zetMaat();
		
	}
	
	public void setBounds(int x, int y, int b, int h)
	{	resized = true;
		if(closeable)tekstVak.setBounds(0,bovenMarge,b-20,h);
		else tekstVak.setBounds(0,bovenMarge,b,h);
		super.setBounds(x,y,b,h);
	}
	
	public void setBorders(boolean b)
	{	borders = b;
		if(b)this.setBorder(BorderFactory.createLineBorder(Color.gray));
		else this.setBorder(BorderFactory.createEmptyBorder());
	}
	
	public void zetBovenMarge(int m)
	{
		bovenMarge = m;
	}
	
	public void setCloseable(boolean b)
	{	closeable = b;
		if(b)
		{ 	if(closeButton==null) 
			{	closeButton = new FormuleButton("close",FormuleButton.MEERKNOP);
				closeButton.addActionListener(this);
				closeButton.setBackground(new Color(255,255,200));
				closeButton.setBounds(getSize().width-15, 3, 12,12);
				
			}
			add(closeButton);
		}
		else if (closeButton!=null)remove(closeButton);
			
	}
	
	public void setButton(String s, JButton button)
	{	if(tipButton!=null)remove(tipButton);
		setText(s);
		hasButton = true;
		tipButton = button;
		button.setLocation((getWidth() - button.getWidth())/2, tekstVak.getHeight()+10);
		add(button,0);
			
	}
	
	//public void update(Graphics g)
	//{	paint(g);
	//}
	
	public void actionPerformed(ActionEvent e)
	{	if(e.getSource()==closeButton) 
		{	produceAction("closeFeedback");
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
 	//
}