package fi.algebrapijlenopdr;

import java.awt.*;
import java.awt.event.*;
import fi.algebrapijlenopdr.expressies_ap.*;

import javax.swing.*;

import java.util.Hashtable;

public class BewerkingSchuifComponent extends AlgebraSchuifComponent implements ActionListener, FocusListener
{	
	BasisExpressie beginw;
	JTextField tf;
	Font f;
	FontMetrics fm;
	protected PlusMinKnop plusMinKnop;
	protected boolean scrollable;
	
	
	public BewerkingSchuifComponent(AlgebraSchuifVeld asv,int x, int y, int b, int h)
	{	super(2, asv, x, y, b, h);
		f = new Font("SansSerrif",Font.PLAIN,14);
		fm = getFontMetrics(f);
		
		beginw = new BasisExpressie("3");
		
		tf = new JTextField();
		if (!links)
			tf.setBounds(20,1,19,18);
		else 
			tf.setBounds(10,1,19,18);
		tf.addActionListener(this);
		tf.addFocusListener(this);
		tf.setVisible(false);
		tf.setEnabled(false);
		
		if(!links)
		{	plusMinKnop = new PlusMinKnop(b-12,2,10,h-4, PlusMinKnop.VERTIKAAL);
		}
		else
		{	plusMinKnop = new PlusMinKnop(b-22,2,10,h-4, PlusMinKnop.VERTIKAAL);
		}
		plusMinKnop.addActionListener(this);
		plusMinKnop.setColor(new Color(255,150,0));
	}
	
	public void setScrollable(boolean b)
	{	scrollable = b;
		if(b)
			add(plusMinKnop,0);
		else 
			remove(plusMinKnop);
	}
	
	public Hashtable getState()
	{	String basisExp  = null;
				
		basisExp = this.beginw.basisString;
				
		Hashtable h = super.getState();
	    h.put("basisExp", basisExp);
	    
	    return h;
	}

    public void setState(Hashtable h)
    {	String basisExp = (String)h.get("basisExp");
    
				
		beginw = new BasisExpressie(basisExp);
		beginw.zetMaat(fm);
		
		
		super.setState(h);
		
		zetMaat();
		
    }
    
	public void zetLinks(boolean b)
	{	links = b;
		if(!links)tf.setBounds(30,1,19,18);
		else tf.setBounds(20,1,19,18);
		for(int i=0 ; i<aantalPu ; i++)
		{	pijlUit[i].zetLinks(b);
			if(!links)pijlUit[i].zetPlaats(getLocation().x + getSize().width+9 ,getLocation().y + 10 );
			else pijlUit[i].zetPlaats(getLocation().x - 10 ,getLocation().y + 10 );
		}
		if(!links)
		{	plusMinKnop.setLocation(getSize().width-12,1);
		}
		else
		{	plusMinKnop.setLocation(getSize().width-22,1);
		}
		schuifveld.tekenOpnieuw();
	}
	
	public void paint(Graphics g)
  	{ 	
		if (!links)
		{	g.setColor(Color.orange);
			g.fillRoundRect(10, 0, getSize().width - 11, getSize().height - 1, 8, 8);
			g.setColor(Color.black);
			g.drawRoundRect(10,0,getSize().width-11,getSize().height-1,8,8);
		}
		else
		{	g.setColor(Color.orange);
			g.fillRoundRect(0,0,getSize().width-11,getSize().height-1,8,8);
			g.setColor(Color.black);
			g.drawRoundRect(0,0,getSize().width-11,getSize().height-1,8,8);
		}	
		super.paint(g);
	}
	
	public void zetMaat()
	{	int b = 50;
		int h = 20;
		int corr = 0;
		if (beginw != null)
		{	b = beginw.breedte;
			if (b > 10)
				b = b+40;
			else 
				b = 50;
			
		}
		
		setSize(b,h);
		tf.setBounds(20,1,b-31,18);
				
		int sccrollCorr = 0;
		if (scrollable) 
			sccrollCorr = 10;
		if(!links)
		{	tf.setBounds(30-sccrollCorr,1,b-31,18);
			plusMinKnop.setLocation(b-12,2);
		}
		else
		{	tf.setBounds(20-sccrollCorr,1,b-31,18);
			plusMinKnop.setLocation(b-22,2);
		}	

	}
	public void mouseClicked(MouseEvent e)
	{	if (((AlgebraSchuifVeld)getParent()).fixed)
			return;
		if (((AlgebraSchuifVeld)getParent()).isDemo)
			return;
		if (((AlgebraSchuifVeld)getParent()).frozen)
			return;
		
//System.out.println("clicked");		
		
		add(tf);
		tf.setVisible(true)	;
		tf.setEnabled(true);
		tf.selectAll();
		tf.requestFocus();
	}
	public void zetInvulWaarde()
	{	boolean isGeldigeInvoer = true;
		{	try
			{	String s = tf.getText();
				s = s.replace(',','.');
				tf.setText(s);
				Double w = Double.valueOf(tf.getText());
			}
			catch(NumberFormatException ex)
			{	isGeldigeInvoer = false;
				tf.setText(Expressie.df.format(beginw.geefWaarde()));
			}
		}
		if (isGeldigeInvoer)
		{	beginw = new BasisExpressie(tf.getText());
			beginw.zetMaat(fm);
		}
		else
		{	beginw = new BasisExpressie("3");
			beginw.zetMaat(fm);
		}
		zetMaat();
		zetVeranderd(20);
		
		tf.setEnabled(false);
		remove(tf);
		tf.setVisible(false);
		
		schuifveld.tekenOpnieuw();
	}
	public void actionPerformed(ActionEvent e)
	{	if(e.getSource()==tf)
		{	zetInvulWaarde();
		}
	else if(e.getSource()==plusMinKnop)
	{	if(beginw!=null && !Double.isNaN(beginw.geefWaarde().doubleValue()))
		{	double w = beginw.geefWaarde().doubleValue();
			if(e.getActionCommand().equals("min"))w -= 1;
			if(e.getActionCommand().equals("plus"))w += 1;
			String waardeString = Expressie.df.format(w);
			beginw = new BasisExpressie(waardeString);
			tf.setText(waardeString);
			zetVeranderd(20);
			beginw.zetMaat(fm);
			zetMaat();
			schuifveld.tekenOpnieuw();
		}
	}
		/*BasisExpressie ex = new BasisExpressie( tf.getText());
		if(ex.isWaarde)beginw = ex;
		else tf.setText("");
		tf.setEnabled(false);
		remove(tf);
		tf.setVisible(false);
		getParent().repaint();*/
	}
	public void focusLost(FocusEvent e)
	{	zetInvulWaarde();
		
//System.out.println("tf focusLost");	
		/*BasisExpressie ex = new BasisExpressie( tf.getText());
		if(ex.isWaarde)beginw = ex;
		else tf.setText("");
		tf.setEnabled(false);
		remove(tf);
		tf.setVisible(false);
		getParent().repaint();*/
	}
	public void focusGained(FocusEvent e){;	}
}
