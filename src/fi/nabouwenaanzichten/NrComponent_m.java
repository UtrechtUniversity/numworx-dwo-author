package fi.nabouwenaanzichten;

import java.awt.*;
import java.awt.event.*;
//import fi.beans.tooltip.*;

class NrComponent_m extends Component implements  MouseListener//ToolTipIF,
{	
	private String toolTip;
	private int nr;
	private Color kl;
	private boolean selected, half;
	private ActionListener actionListener;

	
	public NrComponent_m(int n)
	{	setBounds((n-1)*20, 0, 15, 18);
		addMouseListener(this);
		selected = false;
		half = false;
		nr = n;
		kl = Color.white;
		//setToolTip(NabouwenAanzichten.rb.getString("opdrachtToolTip"));
	}
	
	public void paint(Graphics g)
	{	
		g.setColor(kl);
		//if(half)
		//{	g.fillOval(0,0,20,20);
		//	g.setColor(new Color(0,150,0));
		//	g.fillOval(4,4,12,12);
		//	g.setColor(kl);
		//}
		//else 
		g.fillOval(0,0,15,15);
		g.setColor(Color.black);
		g.drawOval(0,0,14,14);
		if(selected)
		{	g.drawOval(1,1,12,12);
			//g.drawOval(2,2,15,15);
			g.drawLine(0,16,14,16);
			g.drawLine(0,17,14,17);
		}
		int cor;
		if(nr<10)cor = 2;
		else cor = -2;
		g.setFont(new Font("SansSerif",Font.PLAIN,10));
		g.drawString(Integer.toString(nr),3+cor,11);
	}
	public void setSelected(boolean b)
	{	selected = b;
		repaint();
	}
	public void zetGemaakt(boolean b)
	{	if(b)kl = new Color(0,150,0);
		else kl = new Color(255,150,150);
		repaint();
	}
	
	public boolean geefGoedFout()
	{	if(kl.equals(new Color(0,150,0)))return true;
		return false;
	}
	
	public void zetGemaaktHalf()
	{	kl = Color.yellow;
		repaint();
	}
	
	//public void zetGemaaktHalf()
	//{	half = true;
	//	repaint();
	//}
	
	public void maakSchoon()
	{	kl = Color.white;
		half = false;
		repaint();
	}
	
	public void addActionListener(ActionListener listener)
	{	actionListener = AWTEventMulticaster.add(actionListener, listener);
	}
	
	public void removeActionListener(ActionListener listener)
	{	actionListener = AWTEventMulticaster.remove(actionListener, listener);
	}
	
	public void mousePressed(MouseEvent e)
	{	if(actionListener!=null )
		{	actionListener.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, Integer.toString(nr)));
		}
	}
	public void mouseReleased(MouseEvent e){;}
	public void mouseExited(MouseEvent e){;}
	public void mouseClicked(MouseEvent e){;}
	public void mouseEntered(MouseEvent e){;}
	
	/*public void setToolTip(String toolTip)
	{	this.toolTip = toolTip;
       	ToolTipManager.registerComponent(this);
	}
	
	public String getToolTip() 
	{	return toolTip;
    }
    
    public Component getComponent() 
	{	return this;
    }*/
}