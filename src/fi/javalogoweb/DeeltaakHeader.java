package fi.javalogoweb;

import java.awt.*;
import java.awt.event.*;

import fi.javalogoweb.expressies.BasisExpressie;

public class DeeltaakHeader extends Panel implements ActionListener
{
	private GetalComponent gc;
	private Button closeButton;
	private DeeltaakCComponent deeltaakCComponent;
	
	
	public DeeltaakHeader(int x, int y, int b, int h)
	{	setLayout(null);
		setBounds(x,y,b,h);
		gc = new GetalComponent(20,2,40,21);
		gc.setFont(new Font("SansSerif", Font.BOLD,12));
		gc.zetInstelbaar(true);
		gc.addActionListener(this);
		add(gc,0);
		
		closeButton = new Button ("X");
		closeButton.addActionListener(this);
		closeButton.setBounds(b-22,2,21,21);
		add(closeButton);
		
	}
	public void zetDeeltaakCComponent(DeeltaakCComponent dtc)
	{	deeltaakCComponent = dtc;
		gc.zetTekst(dtc.getCommandName());
	}
	public DeeltaakCComponent geefDeeltaakCComponent()
	{	return deeltaakCComponent;
		
	}
	
	public void paint(Graphics g)
	{	g.setColor(new Color(255,255,200));
		g.fillRect(0,0,getSize().width,getSize().height);
		g.setColor(Color.black);
		g.drawRect(0,0,getSize().width-1,getSize().height-1);
		g.drawRect(1,1,getSize().width-3,getSize().height-3);
		
		super.paint(g);
	}
	
	public void actionPerformed(ActionEvent e)
	{	if(e.getSource() == closeButton)
		{	produceAction("close");
			
		}
		if(e.getSource() == gc)
		{	produceAction(gc.geefTekst());
			
		}
		
	}
	
	//	ActionProducer
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
