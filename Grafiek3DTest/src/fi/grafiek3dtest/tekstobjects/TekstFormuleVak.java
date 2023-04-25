package fi.grafiek3dtest.tekstobjects;

import java.awt.*;
import java.awt.event.*;

import javax.swing.BorderFactory;

import fi.grafiek3dtest.Grafiek3DTest;
import fi.grafiek3dtest.formuleobjects.*;
import fi.grafiek3dtest.tekstobjects.*;
import fi.grafiek3dtest.expressies.*;
import fi.beans.stringutils.*;

public class TekstFormuleVak extends TekstDeelVak implements ActionListener, MouseListener
{
	private FormuleVak formuleVak;
	private FormuleVak rmAntwoordVak;
	//private Font font = new Font("TimesRoman",Font.PLAIN,14);
	//private Font fontGR = new Font("TimesRoman",Font.PLAIN,13);
	private FontMetrics fm;
	private boolean selected = false;
	
	private boolean rekenVak;
	private FormuleButton rmButton;
	boolean afgerondOp3;
	
	public TekstFormuleVak(TekstVak tv, boolean rekenVak)
	{	super(tv);
		
		this.rekenVak = rekenVak;
		
		setOpaque(true);
		setBackgroundRekenVak(new Color(225,225,225));
		addMouseListener(this);
			
		formuleVak = new FormuleVak();
		formuleVak.setLocation(2,0);
		formuleVak.setBackground(new Color(225,225,225));
		formuleVak.addActionListener(this);
		Font f = new Font("TimesRoman", tv.getFont().getStyle(), tv.getFont().getSize()+2);
		fm = getFontMetrics(f);
		if(!Grafiek3DTest.formTimes || Grafiek3DTest.mac || Grafiek3DTest.zoefi) f = tv.getFont();
		setFont(f);
		add(formuleVak);
		setSize(formuleVak.getSize().width+4,formuleVak.getSize().height+2);
		
		if(rekenVak)
		{	formuleVak.setLocation(3,2);
			
			rmAntwoordVak = new FormuleVak();
			rmAntwoordVak.setLocation(formuleVak.getSize().width +20,2);
			rmAntwoordVak.setBackground(new Color(225,225,225));
			rmAntwoordVak.setFont(f);
			rmAntwoordVak.setEditable(false);
			add(rmAntwoordVak);
			
			rmButton = new FormuleButton("rmvakklein");
			rmButton.setBounds(formuleVak.getSize().width+5,2,16,16);
			rmButton.setBackground(new Color(225,225,225));
			//rmButton.setBorder(BorderFactory.createLineBorder(Color.gray));
			rmButton.addActionListener(this);
			add(rmButton);
			
			setSize(formuleVak.getSize().width+rmAntwoordVak.getSize().width+25,formuleVak.getSize().height+3);
			setBorder(BorderFactory.createLineBorder(Color.gray));
		}
				
		ashoogte = formuleVak.ashoogte+(fm.getAscent()-1)/2+1;
		setOpaque(false);
	}
	
	public TekstFormuleVak(TekstVak tv)
	{	this(tv,false);
		
	}
	
	public void setBackground(Color c)
	{	if(rekenVak)return;
		super.setBackground(c);
		Component[] components = getComponents();
		for(int i=0 ; i<components.length ; i++)
		{
			components[i].setBackground(c);
		}
	}
	
	public void setFGColor(Color c)
	{	formuleVak.setFGColor(c);
	}
	
	public void setForeground(Color c)
	{	super.setForeground(c);
		if(formuleVak!=null)formuleVak.setFGColor(c);
	}
	
	public void setOpaque(boolean b)
	{	if(!b && rekenVak || tekstVak!=null && tekstVak.isEditable())return;
		super.setOpaque(b);
	}
	
	public void setBackgroundRekenVak(Color c)
	{	super.setBackground(c);
		Component[] components = getComponents();
		for(int i=0 ; i<components.length ; i++)
		{
			components[i].setBackground(c);
		}
	}
	
	public void setFont(Font font)
	{	super.setFont(font);
		Font f = new Font("TimesRoman", font.getStyle(), font.getSize()+2);
		if("TimesRoman".equals(font.getName()) || !Grafiek3DTest.formTimes || Grafiek3DTest.mac || Grafiek3DTest.zoefi) f = font;
		if(formuleVak==null) return;
		formuleVak.setFont(f);
		fm = getFontMetrics(f);
		ashoogte = formuleVak.ashoogte+(fm.getAscent()-1)/2+1;
	}
	
	public void vulVak(String s)
	{	formuleVak.vulVak(s);
		if(rekenVak) bereken();
	}
	
	public FormuleVak geefFormuleVak()
	{	return formuleVak;
	}
	
	public void setEditable(boolean b)
	{	formuleVak.setEditable(b);
		if(!b)setBackground(getParent().getBackground());
		else setBackground(new Color(225,225,225));
	}
	
	public void setSelectable(boolean b)
	{	formuleVak.setSelectable(b);
	}
	
	public void setSelected(boolean b)
	{	if(selected!=b)
		{	selected = b;
			formuleVak.setSelected(b);
			repaint();
		}
	}
	
	public boolean isSelected()
	{	return selected;
	}
	
	public void zetMaat()
	{	
		if(!rekenVak)
		{	setSize(formuleVak.getSize().width+4, formuleVak.getSize().height);
			formuleVak.setLocation(2,0);
			ashoogte = formuleVak.ashoogte+(fm.getAscent()-1)/2+1;
			if(getParent()instanceof FormuleElement)((FormuleElement)getParent()).zetMaat();
			if(getParent()instanceof TekstElement)((TekstElement)getParent()).zetMaat();
		}
		else
		{	int b = formuleVak.getSize().width + rmAntwoordVak.getSize().width + 25;
		
			int h1 = formuleVak.ashoogte;
			if(rmAntwoordVak.ashoogte>h1)h1=rmAntwoordVak.ashoogte;
			int h2 = formuleVak.getSize().height - formuleVak.ashoogte;
			if(rmAntwoordVak.getSize().height - rmAntwoordVak.ashoogte>h2)h2=rmAntwoordVak.getSize().height - rmAntwoordVak.ashoogte;
			
			setSize(b, h1+h2+3);
			ashoogte = h1+1;
			
			formuleVak.setLocation(3,ashoogte-formuleVak.ashoogte);
			rmButton.setLocation(formuleVak.getSize().width+5,ashoogte-7);
			rmAntwoordVak.setLocation(formuleVak.getSize().width +20,ashoogte-rmAntwoordVak.ashoogte);
			if(getParent()instanceof FormuleElement)((FormuleElement)getParent()).zetMaat();
			if(getParent()instanceof TekstElement)((TekstElement)getParent()).zetMaat();
			
			ashoogte = h1+(fm.getAscent()+1)/2+1;
		}
	}
	
	public String toString()
	{	if(rekenVak)return "$R"+ formuleVak.toString().substring(2);
		return formuleVak.toString();
	}
	
	public void zetRekenVak(boolean b)
	{	rekenVak = b;
	}
	
	public void paint(Graphics g)
	{	g.setColor(getBackground());
		//g.fillRect(1,0,getSize().width-2, getSize().height);
		super.paint(g);
	}
	
	public void neemFocus(String richting,FormuleElement fe)
	{	if(getParent() instanceof TekstElement)
		{	((TekstElement)getParent()).neemFocus(richting,this);
		}
	}
	
	public void neemFocus(String richting)
	{	formuleVak.neemFocus(richting);
	}
	
	public void bereken()
	{	afgerondOp3 = !afgerondOp3;
		Expressie exp = formuleVak.geefExpressie();
		if(exp!=null && !Double.isNaN(exp.geefWaarde()) && !(exp instanceof BasisExpressie))
		{	double d = exp.geefWaarde();
			Expressie expAfgerond = new DecRound(exp, new BasisExpressie(3));
			double dAfgerond = expAfgerond.geefWaarde();
			boolean isAfronding = !Algebra.isGelijkDouble(d, dAfgerond, 0.00000000000000001);
			String s1 = formuleVak.toString();
			s1 = s1.substring(2,s1.length()-1);
			String s2 = Expressie.df3.format(dAfgerond);
			if(afgerondOp3)
			{	if(isAfronding) s2 = "\u2248" + s2;
				else s2 = "=" + s2;
			}
			else
			{
				String s = Double.toString(d);
				String[] delen = StringUtils.split(s,"E");
				if(delen.length>1) s2 = delen[0] + "*10$m" + delen[1] + "@";
				else s2 = delen[0];
				if(isAfronding) s2 = "\u2248" + s2;
				else s2 = "=" + s2;
			}
			rmAntwoordVak.vulVak("$f" + s2 + "@");
		}
	}
	
	public void actionPerformed(ActionEvent e)
	{	if(e.getActionCommand().equals("focus")) tekstVak.zetFormuleVak(formuleVak);
		if(e.getActionCommand().equals("ingevuld") && rekenVak)
		{	bereken();
		}
		else if(rekenVak && e.getSource()==formuleVak && e.getActionCommand().equals("formChanged"))
		{	rmAntwoordVak.vulVak("$f@");
			formuleVak.requestFocus();
		}
		else if(rekenVak && e.getSource()==rmButton)
		{	bereken();
		}
	}
	
	public void mousePressed(MouseEvent e)
	{	formuleVak.requestFocus();
		if(e.getX()> getSize().width-10)formuleVak.zetOpEind();
		if(e.getX()< 10)formuleVak.zetOpBegin();
	}
	public void mouseReleased(MouseEvent e){;}
	public void mouseExited(MouseEvent e){;}
	public void mouseClicked(MouseEvent e){;}
	public void mouseEntered(MouseEvent e)
	{	
	}
	
}
