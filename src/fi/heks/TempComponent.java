package fi.heks;

import java.awt.*;
import java.awt.event.*;

import fi.heks.scobjects.*;

public class TempComponent extends ScContainer implements ActionListener, FocusListener, MouseListener
{
	private int temp;
	private ScTextField beginTempTf;
	private boolean instelbaar, bekend;
	private ActionListener actionListener;
	
	public TempComponent(int x, int y, int b, int h)
	{	super(x,y,b,h);
		temp = 0;
		bekend = true;
		instelbaar = false;
		beginTempTf = new ScTextField(5,0,b-10,h,"0");
		beginTempTf.addActionListener(this);
		beginTempTf.addFocusListener(this);
		
		
		//beginTempTf.setVisible(false);
	}
	
	public void paint(Graphics g)
	{	Font f = new Font("SansSerif", Font.PLAIN, (int)(schaal*relh));
		g.setColor(Color.black);
		g.setFont(f);
		String s;
		if(bekend) s = Integer.toString(temp)+"°";
		else s = "...°";
		FontMetrics fm = g.getFontMetrics();
		int woordbreedte = fm.stringWidth(s);
		g.drawString(s,(getSize().width - woordbreedte)/2, getSize().height);
		super.paint(g);
	}
	public int geefTemp()
	{	return temp;
	}
	
	public void zetTemp(int t)
	{	temp = t;
		repaint();
	}
	public void zetBekend(boolean b)
	{	bekend = b;
	}
	public boolean isBekend()
	{	return bekend;
	}
	
	public void zetInstelbaar(boolean b)
	{	if(b)addMouseListener(this);
		else removeMouseListener(this);
		instelbaar = b;
	}
	
	public void tempPlus()
	{	temp++;
		repaint();
	}
	
	public void tempMin()
	{	temp--;
		repaint();
	}
	
	public void addActionListener(ActionListener listener)
	{	actionListener = AWTEventMulticaster.add(actionListener, listener);
	}
	
	public void removeActionListener(ActionListener listener)
	{	actionListener = AWTEventMulticaster.remove(actionListener, listener);
	}
	
	public void mouseClicked(MouseEvent e){;}
	public void mousePressed(MouseEvent e){;}
	public void mouseReleased(MouseEvent e)
	{	if(instelbaar)
		{	beginTempTf.schaal(schaal);
			add(beginTempTf);
			beginTempTf.selectAll();
			beginTempTf.requestFocus();
		}
	}
	
	public void mouseExited(MouseEvent e){;}
	public void mouseEntered(MouseEvent e){;}
	
	public void actionPerformed(ActionEvent e)
	{	String s = beginTempTf.getText();
		int temp;
		try
		{	temp = Integer.parseInt(s);
			zetBekend(true);
			zetTemp(temp);
		}
		catch(NumberFormatException ex)
		{	beginTempTf.setText("...°");
			zetBekend(false);
		}
		remove(beginTempTf);
		if(actionListener!=null)
		{	actionListener.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, ""+this));
		}
	}
	
	public void focusLost(FocusEvent e)
	{	String s = beginTempTf.getText();
		int temp;
		try
		{	temp = Integer.parseInt(s);
			zetBekend(true);
			zetTemp(temp);
		}
		catch(NumberFormatException ex)
		{	beginTempTf.setText("...°");
			zetBekend(false);
		}
		remove(beginTempTf);
		if(actionListener!=null)
		{	actionListener.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, ""+this));
		}
	}
	public void focusGained(FocusEvent e){;	}
}
