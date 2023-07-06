package fi.algebraexpressies;

import java.awt.*;
import java.awt.event.*;
import fi.algebraexpressies.schuifobjects.*;
import fi.algebraexpressies.expressies.*;

import javax.swing.*;

public class InUitvoerLabel extends JPanel //Container 
                            implements MouseListener, MouseMotionListener, ActionListener, FocusListener
{	
	private JTextField tf;
	private String tekst;
	private Font f;
	private FontMetrics fm;
	private boolean muisrechts;
	
	
	public InUitvoerLabel()
	{	
		setOpaque(false);
		
		setBounds(0, 10, 40, 20);
		addMouseListener(this);
		addMouseMotionListener(this);
		
		muisrechts = false;
		
		f = new Font("SansSerrif",Font.PLAIN,12);
		fm = getFontMetrics(f);
		
		tekst = "";
		
		tf = new JTextField(tekst);
		tf.setBounds(2, 0, 35, 20);
		tf.addActionListener(this);
		tf.addFocusListener(this);
		
		add(tf);
		tf.setVisible(false);
	}
	
	public void paint(Graphics g)
  	{ 	super.paint(g);
				
		//g.setColor(Color.white);
		//g.fillRect(0,0,getSize().width-1,getSize().height-1);
		//g.setColor(Color.black);
		//g.drawRect(0,0,getSize().width-1,getSize().height-1);
		
		g.setColor(Color.white);
		g.setFont(f);
		int x = (getSize().width - fm.stringWidth(tekst))/2;
		int y = getSize().height-5;
		g.drawString(tekst, x, y);
	}
	
	public void setSize(int b, int h)
	{	tf.setSize(b - 5, h);
		super.setSize(b, h);
	}
	
	public String geefTekst()
	{	return tekst;
	}
	
	public int geefBreedte()
	{	int b = 32;
		b = fm.stringWidth(tekst);
		if(b > 32)
			b = b + 8;
		else 
			b = 40;
		return b;	
	}

	public void zetLabelTekst(String tekst)
	{	tf.setText(tekst);
		zetLabelTekst();
	}
	
	public void zetLabelTekst()
	{	tekst = tf.getText();
		if (getParent() != null)
		{	
			((AlgebraSchuifComponent) getParent()).zetMaat();
			tf.setVisible(false);
			tf.setEnabled(false);
			((AlgebraSchuifComponent) getParent()).zetVeranderd(20);
			((AlgebraSchuifComponent) getParent()).schuifveld.tekenOpnieuw();
		}
	}
	
	public void actionPerformed(ActionEvent e)
	{	zetLabelTekst();
	}
	public void focusLost(FocusEvent e)
	{	zetLabelTekst();
		
	}
	public void focusGained(FocusEvent e)
	{	
	}
	
	public void mousePressed(MouseEvent e)
	{	
		
		if (((AlgebraSchuifVeld)((UitvoerSchuifComponent)getParent()).schuifveld).isDemo)
			return;
		if (((AlgebraSchuifVeld)((UitvoerSchuifComponent)getParent()).schuifveld).frozen)
			return;
		
		requestFocus();
		muisrechts = false;
		if (e.getModifiers()== e.BUTTON3_MASK || e.isControlDown())
		{	muisrechts = true;
		}
		((SchuifComponent) getParent()).mousePressed(e);
	}	
	public void mouseClicked(MouseEvent e)
	{	
		if (((AlgebraSchuifVeld)((UitvoerSchuifComponent)getParent()).schuifveld).isDemo)
			return;
		if (((AlgebraSchuifVeld)((UitvoerSchuifComponent)getParent()).schuifveld).frozen)
			return;
		
		
		if (!muisrechts)
		{	tf.setVisible(true);
			tf.setEnabled(true);
			tf.selectAll();
			tf.requestFocus();
		}
		//((SchuifComponent)getParent()).mouseClicked(e);
	}
	public void mouseReleased(MouseEvent e)
	{	
		if (((AlgebraSchuifVeld)((UitvoerSchuifComponent)getParent()).schuifveld).isDemo)
			return;
		if (((AlgebraSchuifVeld)((UitvoerSchuifComponent)getParent()).schuifveld).frozen)
			return;
		
		((SchuifComponent) getParent()).mouseReleased(e);
	}
	public void mouseExited(MouseEvent e){;}
	public void mouseEntered(MouseEvent e){;}
	public void mouseDragged(MouseEvent e)
	{	((SchuifComponent) getParent()).mouseDragged(e);
	}
	
	public void mouseMoved(MouseEvent e){;}
	
	
}
