package fi.javalogoweb;

import java.awt.*;
import java.awt.event.*;
import fi.javalogoweb.expressies.*;
import fi.javalogoweb.formuleobjects.*;

public class GetalComponent extends Container implements ActionListener, FocusListener, MouseListener
{
	private Expressie waarde;
	private TextField beginWaardeTf;
	private boolean isTemp,instelbaar, bekend, leeg;
	private ActionListener actionListener;
	private int standaardBreedte = 35;
	private Font f;
	private FontMetrics fm;
	
	
	public GetalComponent(int x, int y, int b, int h)
	{	setBounds(x,y,b,h);
		waarde = new BasisExpressie(0);
		bekend = true;
		leeg = false;
		instelbaar = false;
		isTemp = false;
		beginWaardeTf = new TextField();
		beginWaardeTf.setBounds(2,2,b-4,h-4);
		beginWaardeTf.addActionListener(this);
		beginWaardeTf.addFocusListener(this);
		beginWaardeTf.setVisible(false);
		beginWaardeTf.setEnabled(false);
		//beginWaardeTf.setLocation(getLocation().x, getLocation().y);
		f = new Font("SansSerif", Font.PLAIN, (int)(3*getSize().height/5));
		fm = getFontMetrics(f);
		
	}
	
	public void paint(Graphics g)
	{	g.setColor(getForeground());
		g.setFont(f);
		String s;
		if(bekend)
		{	if(isTemp)s = waarde.toString()+"°C";
			//else s = waarde.toString();
		}
		else 
		{	if(leeg)s="";
			else if(isTemp)s = "...°C";
			else s = "...";
		}
		s = beginWaardeTf.getText();
		FontMetrics fm = g.getFontMetrics();
		int woordbreedte = fm.stringWidth(s);
		g.drawString(s,(getSize().width-woordbreedte)/2, (getSize().height + fm.getHeight())/2 - fm.getDescent()+1);
		super.paint(g);
	}
	public double geefWaarde()
	{	if(waarde!=null )return waarde.geefWaarde();
		else return Double.NaN;
	}
	public Expressie geefExpressie()
	{	return waarde;
	}
	
	public void zetWaarde(double d)
	{	zetBekend(true);
		waarde = new BasisExpressie(d);
		beginWaardeTf.setText(waarde.toString());
		repaint();
	}
	
	public void zetWaarde(Expressie e)
	{	waarde = e;
		beginWaardeTf.setText(waarde.toString());
		repaint();
	}
	public void zetBekend(boolean b)
	{	bekend = b;
		if(!b)beginWaardeTf.setText("");
		if(!b)waarde = null;
	}
	public void zetLeeg(boolean b)
	{	leeg = b;
	}
	public void zetAlsTemp(boolean b)
	{	isTemp = b;
	}
	public boolean isBekend()
	{	return bekend;
	}
	public boolean isInstelbaar()
	{	return instelbaar;
	}
	
	public void zetInstelbaar(boolean b)
	{	if(b && !instelbaar)addMouseListener(this);
		else if(!b && instelbaar) removeMouseListener(this);
		instelbaar = b;
	}
	
	public void verhoog()
	{	//waarde++;
		//repaint();
	}
	
	public void verlaag()
	{	//waarde--;
		//repaint();
	}
	
	public void verhoog(int d)
	{	//waarde+=d;
		//repaint();
	}
	
	public void verlaag(int d)
	{	//waarde-=d;
		//repaint();
	}
	
	public void addActionListener(ActionListener listener)
	{	actionListener = AWTEventMulticaster.add(actionListener, listener);
	}
	
	public void removeActionListener(ActionListener listener)
	{	actionListener = AWTEventMulticaster.remove(actionListener, listener);
	}
	
	public void vulIn()
	{	int breedte = Math.max(standaardBreedte, fm.stringWidth(beginWaardeTf.getText())+20);
		setSize(breedte,getSize().height);
		beginWaardeTf.setBounds(2,2,breedte-4 ,getSize().height-4);
		if(instelbaar)
		{	add(beginWaardeTf);
			beginWaardeTf.setVisible(true);
			beginWaardeTf.setEnabled(true);
			beginWaardeTf.selectAll();
			beginWaardeTf.requestFocus();
		}
		((CommandComponent)getParent()).zetMaat();
		((CommandComponent)getParent()).tekenOpnieuw();
		repaint();
	}
	
	public void mouseClicked(MouseEvent e){;}
	
	public void mousePressed(MouseEvent e){;}
	public void mouseReleased(MouseEvent e)
	{	vulIn();
	}
	public void mouseExited(MouseEvent e){;}
	public void mouseEntered(MouseEvent e){;}
	
	public void actionPerformed(ActionEvent e)
	{	String s = beginWaardeTf.getText();
		int w;
		try
		{	waarde = FormuleParser.parse(FormuleParser.schoon(FormuleParser.formuleString("$f"+s+"@")));
			zetBekend(true);
			//zetWaarde(w);
		}
		catch(NumberFormatException ex)
		{	beginWaardeTf.setText("");
			//zetBekend(false);
			//waarde = -999;
			repaint();
			
		}
		beginWaardeTf.setEnabled(false);
		remove(beginWaardeTf);
		if(beginWaardeTf.isVisible())
		{	beginWaardeTf.setVisible(false);
			repaint();
			if(actionListener!=null && bekend)
			{	actionListener.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, ""+this));
			}
		}
		
		setSize(fm.stringWidth(beginWaardeTf.getText()),getSize().height);
		((CommandComponent)getParent()).zetMaat();
		((CommandComponent)getParent()).tekenOpnieuw();
		
		
	}
	
	public void focusLost(FocusEvent e)
	{	String s = beginWaardeTf.getText();
		//int w;
		try
		{	//w = Integer.parseInt(s);
			waarde = FormuleParser.parse(FormuleParser.schoon(FormuleParser.formuleString("$f"+s+"@")));
			zetBekend(true);
			//etWaarde(w);
		}
		catch(NumberFormatException ex)
		{	beginWaardeTf.setText("");
			//zetBekend(false);
			//waarde = -999;
			repaint();
		}
		beginWaardeTf.setEnabled(false);
		remove(beginWaardeTf);
		if(beginWaardeTf.isVisible())
		{	beginWaardeTf.setVisible(false);
			repaint();
			if(actionListener!=null && bekend)
			{	actionListener.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, ""+this));
			}
		}
		setSize(fm.stringWidth(beginWaardeTf.getText()),getSize().height);
		((CommandComponent)getParent()).zetMaat();
		((CommandComponent)getParent()).tekenOpnieuw();
		
	}
	public void focusGained(FocusEvent e){;	}
}
