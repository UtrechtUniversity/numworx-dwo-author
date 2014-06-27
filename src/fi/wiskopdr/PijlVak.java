package fi.wiskopdr;

import java.awt.*;
import java.awt.event.*;
import java.util.Vector;

import javax.swing.BorderFactory;

import fi.wiskopdr.formuleobjects.*;
import fi.wiskopdr.tekstobjects.TekstArea;
import fi.wiskopdr.expressies.*;

public class PijlVak extends FormuleElement implements ActionListener
{	
	private Font font = new Font("Serif",Font.PLAIN,14);
	private FontMetrics fm;
	private String operator;
	
	private ImageComponent goedIC, foutIC, halfIC, huidigIC;
	

	FormuleVak formuleVak, prefixVak, formuleVakHaakjeL, formuleVakHaakjeR;
	TekstArea pijlTekst;
	
	public PijlVak(String op)
	{	setLayout(null);
		operator = op;
		
		super.setFont(font);
		fm = getFontMetrics(getFont());
					
		setSize((fm.getAscent() + fm.getDescent())/2 + fm.getAscent()/4 + fm.stringWidth("  "+operator+"   "),5*(fm.getAscent() + fm.getDescent())/2);
		if(op.equals("abc") || op.equals("sub")) setSize(110,5*(fm.getAscent() + fm.getDescent())/2);
		ashoogte = 5*(fm.getAscent() + fm.getDescent())/4;
		
		if(op.equals(""))
		{	ashoogte = 15;
			setSize(30,30);
		}
		
		prefixVak = new FormuleVak();
		prefixVak.setEditable(false);
		prefixVak.setLocation((fm.getAscent() + fm.getDescent())/2 + fm.stringWidth("  "+operator+" "),(fm.getAscent() + fm.getDescent())/2);
		
		formuleVak = new FormuleVak();
		formuleVak.addActionListener(this);
		formuleVak.setLocation((fm.getAscent() + fm.getDescent())/2 + fm.stringWidth("  "+operator+" "),(fm.getAscent() + fm.getDescent())/2);
		if(operator.equals("abc") || operator.equals("sub"))
		{	
			if(operator.equals("abc"))prefixVak.vulVak("$fD=@");
			if(operator.equals("sub"))prefixVak.vulVak("$fp=@");
			prefixVak.setLocation(10,ashoogte);
			prefixVak.setBackground(new Color(255,255,200));
			add(prefixVak);
			formuleVak.setLocation(40,ashoogte);
			formuleVak.setBackground(new Color(255,255,200));
		}
		
		if(!op.equals("") && !op.equals("haakjes") && !op.equals("herleid") && !op.equals("gelijkwaardig") && !op.equals("ontbind") && !op.equals("splits") && !op.equals("wortel")  && !op.equals("implicatie"))
			add(formuleVak);
		
		/*formuleVakHaakjeL = new FormuleVak();
		formuleVakHaakjeL.setLocation((fm.getAscent() + fm.getDescent())/2 + fm.stringWidth("  "+operator+" ")-5,(fm.getAscent() + fm.getDescent())/2);
		if(!op.equals("haakjes") && !op.equals("herleid") && !op.equals("gelijkwaardig") && !op.equals("ontbind") && !op.equals("splits") && !op.equals("wortel"))
			add(formuleVakHaakjeL);
		
		
		formuleVakHaakjeR = new FormuleVak();
		formuleVakHaakjeR.setLocation((fm.getAscent() + fm.getDescent())/2 + fm.stringWidth("  "+operator+" ")+15,(fm.getAscent() + fm.getDescent())/2);
		if(!op.equals("haakjes") && !op.equals("herleid") && !op.equals("gelijkwaardig") && !op.equals("ontbind") && !op.equals("splits") && !op.equals("wortel"))
			add(formuleVakHaakjeR);
		
		formuleVakHaakjeL.vulVak("$f(@");
		formuleVakHaakjeR.vulVak("$f)@");
		*/
		
		goedIC = new ImageComponent(WiskOpdr.GOEDKRUL);
		goedIC.setLocation(getSize().width-30,getSize().height-30);
		goedIC.setVisible(false);
		add(goedIC,0);
		
		foutIC = new ImageComponent(WiskOpdr.FOUTKRUIS);
		foutIC.setLocation(getSize().width-30,getSize().height-30);
		foutIC.setVisible(false);
		add(foutIC,0);
		
		halfIC = new ImageComponent(WiskOpdr.HALFKRUL);
		halfIC.setLocation(getSize().width-30,getSize().height-30);
		halfIC.setVisible(false);
		add(halfIC,0);
		
		setOpaque(false);
		
		pijlTekst = new TekstArea();
		pijlTekst.setSize(220,20);
		if("GR".equals(WiskOpdr.deployVariant))pijlTekst.setSize(170,20);
		pijlTekst.setBackground(new Color(255,255,200));
		if("GR".equals(WiskOpdr.deployVariant))pijlTekst.setBackground(new Color(255,255,255));
		pijlTekst.setBorders(true);
		if("GR".equals(WiskOpdr.deployVariant))pijlTekst.setBorder(BorderFactory.createLineBorder(new Color(70,117,186)));
		pijlTekst.setCloseable(true);
		pijlTekst.addActionListener(this);
	}
	
	public void zetPijlTekst(String tekst, boolean closeable)
	{	pijlTekst.setText("");
		pijlTekst.setCloseable(closeable);
		pijlTekst.setSize(220,20);
		if("GR".equals(WiskOpdr.deployVariant))pijlTekst.setSize(170,20);
		pijlTekst.setLocation(30,0);
		pijlTekst.setText(tekst);
		pijlTekst.resize();
		if(tekst.equals(""))remove(pijlTekst);
		else 
		{	add(pijlTekst,0);
			setSize(getSize().width+21,Math.max(pijlTekst.getHeight(), 5*(fm.getAscent() + fm.getDescent())/2));
		}
	}
	
	public void setSubVar(String s)
	{
		if(operator.equals("sub"))prefixVak.vulVak("$f"+s+"=@");
	}
	
	public void setFont(Font f)
	{	super.setFont(f);
		fm = getFontMetrics(getFont());
		if(formuleVak==null)return;
		formuleVak.setFont(f);
	}
	
	public void paintComponent(Graphics g)
	{	//Graphics g;
        //if(WiskOpdr.deployVariant!=null && WiskOpdr.deployVariant.equals("GR"))
        {     g = (Graphics2D)g;
              ((Graphics2D)g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        }
        //else 
        //g=gr;
        
        //zetMaat();
        super.paintComponent(g);
        
		g.setColor(Color.black);
		if("GR".equals(WiskOpdr.deployVariant))g.setColor(new Color(70,117,186));
		g.setFont(getFont());
		if(!operator.equals("abc"))
		{	int h = ashoogte;
			g.drawArc(-3*h/2,0,2*h,2*h,(int)(180*Math.PI/4),(int)(-180*Math.PI/2));
	        Polygon p = new Polygon();
	        p.addPoint(0,2*h-2);
	        p.addPoint(2,2*h-10);
	        p.addPoint(7,2*h-2);
	        g.fillPolygon(p);
	        g.drawPolygon(p);
		}
		//g.drawLine(0,getSize().height-4,2,getSize().height-12);
		//g.drawLine(0,getSize().height-4,7,getSize().height-4);
		g.setColor(Color.black);
		if(operator.equals("*"))
		{	g.drawLine((fm.getAscent() + fm.getDescent())/2+10,ashoogte-fm.getAscent()/4+3,(7*fm.getAscent()/4 + fm.getDescent())/2+10,ashoogte+fm.getAscent()/4+2);
			g.drawLine((fm.getAscent() + fm.getDescent())/2+10,ashoogte+fm.getAscent()/4+2,(7*fm.getAscent()/4 + fm.getDescent())/2+10,ashoogte-fm.getAscent()/4+3);
		
		}
		else if(operator.equals(":"))
		{	int b=fm.getAscent() + fm.getDescent();
			g.fillRect(b/2+10,ashoogte-b/4+2,2,2);
			g.fillRect(b/2+10,ashoogte+b/4+1,2,2);
			g.drawLine(b/4+11,ashoogte+2,3*b/4+10,ashoogte+2);
		}
		else if(operator.equals("haakjes"))
		{	g.drawString(WiskOpdr.rb.getString("haakjesLabel0"),(fm.getAscent() + fm.getDescent())/2 ,ashoogte-fm.getDescent());
			g.drawString(WiskOpdr.rb.getString("haakjesLabel1"),(fm.getAscent() + fm.getDescent())/2 ,ashoogte+fm.getAscent());
		
		}
		else if(operator.equals("herleid"))
		{	g.drawString(WiskOpdr.rb.getString("herleidLabel0"),(fm.getAscent() + fm.getDescent())/2 ,ashoogte-fm.getDescent()+10);
			g.drawString(WiskOpdr.rb.getString("herleidLabel1"),(fm.getAscent() + fm.getDescent())/2 ,ashoogte+fm.getAscent());
		
		}
		else if(operator.equals("ontbind"))
		{	g.drawString(WiskOpdr.rb.getString("ontbindLabel0"),(fm.getAscent() + fm.getDescent())/2 ,ashoogte-fm.getDescent()+10);
			g.drawString("",(fm.getAscent() + fm.getDescent())/2 ,ashoogte+fm.getAscent());
		
		}
		else if(operator.equals("splits"))
		{	g.drawString(WiskOpdr.rb.getString("splitsLabel0"),(fm.getAscent() + fm.getDescent())/2 ,ashoogte-fm.getDescent()+10);
			g.drawString("",(fm.getAscent() + fm.getDescent())/2 ,ashoogte+fm.getAscent());
		
		}
		else if(operator.equals("wortel"))
		{	g.drawString(WiskOpdr.rb.getString("wortelLabel0"),(fm.getAscent() + fm.getDescent())/2 ,ashoogte-fm.getDescent()+10);
			g.drawString("",(fm.getAscent() + fm.getDescent())/2 ,ashoogte+fm.getAscent());
		
		}
		else if(operator.equals("gelijkwaardig"))
		{	g.drawString(WiskOpdr.rb.getString("gelijkwaardigLabel0"),(fm.getAscent() + fm.getDescent())/2 ,ashoogte-fm.getDescent());
			g.drawString(WiskOpdr.rb.getString("gelijkwaardigLabel1"),(fm.getAscent() + fm.getDescent())/2 ,ashoogte+fm.getAscent());
		
		}
		else if(operator.equals("implicatie"))
		{	//g.drawString(WiskOpdr.rb.getString("gelijkwaardigLabel0"),(fm.getAscent() + fm.getDescent())/2 ,ashoogte-fm.getDescent());
			//g.drawString(WiskOpdr.rb.getString("gelijkwaardigLabel1"),(fm.getAscent() + fm.getDescent())/2 ,ashoogte+fm.getAscent());
		
		}
		else if(operator.equals("abc"))
		{	g.setColor(new Color(255,255,200));
			if("GR".equals(WiskOpdr.deployVariant))g.setColor(new Color(255,255,255));
			g.fillRect(0,0,getSize().width,getSize().height);
			g.setColor(Color.black);
			if("GR".equals(WiskOpdr.deployVariant))g.setColor(new Color(246,127,142));
			g.drawRect(0,0,getSize().width-1,getSize().height-1);
			g.setColor(Color.black);
			g.drawString("Discriminant",5 ,ashoogte-fm.getDescent());
			//g.drawString("D = ",15 ,ashoogte+fm.getAscent());
		}
		else if(operator.equals("sub"))
		{	g.setColor(new Color(255,255,200));
			if("GR".equals(WiskOpdr.deployVariant))g.setColor(new Color(255,255,255));
			g.fillRect(0,0,getSize().width,getSize().height);
			g.setColor(Color.black);
			if("GR".equals(WiskOpdr.deployVariant))g.setColor(new Color(246,127,142));
			g.drawRect(0,0,getSize().width-1,getSize().height-1);
			g.setColor(Color.black);
			g.drawString(WiskOpdr.rb.getString("subLabel"),5 ,ashoogte-fm.getDescent());
			//g.drawString("p = ",15 ,ashoogte+fm.getAscent());
		}
		else
		{	g.drawString("  "+operator+" ",(fm.getAscent() + fm.getDescent())/2 ,ashoogte+fm.getAscent()/2);
		}
		
	}
	
	public void zetExpressie(String s)
	{	formuleVak.vulVak("$f" + s + "@");
	}
	
	public void zetMaat()
	{	int b = (fm.getAscent() + fm.getDescent())/2 + fm.stringWidth("  "+operator+"   ") + formuleVak.getSize().width;
		if(operator.equals("abc") || operator.equals("sub")) b = Math.max(110, getSize().width);
		setSize(b, getSize().height);
		//formuleVakHaakjeL.setLocation((fm.getAscent() + fm.getDescent())/2 + fm.stringWidth("  "+operator+" "),ashoogte-formuleVak.ashoogte-fm.getDescent()/2);
		formuleVak.setLocation((fm.getAscent() + fm.getDescent())/2 + fm.stringWidth("  "+operator+" "),ashoogte-formuleVak.ashoogte-fm.getDescent()/2);
		//formuleVakHaakjeR.setLocation((fm.getAscent() + fm.getDescent())/2 + fm.stringWidth("  "+operator+" ") + formuleVakHaakjeL.getSize().width + formuleVak.getSize().width,ashoogte-formuleVak.ashoogte-fm.getDescent()/2);
		if(operator.equals("abc") || operator.equals("sub"))formuleVak.setLocation(40,ashoogte);
	}
	
	public String geefExpressieString()
	{	Expressie e = formuleVak.geefExpressie();
		if(e==null)return null;
		String s = e.toStringStrikt();
		return s;
	}
	
	public void requestFocus()
	{	formuleVak.requestFocus();
	}
	
	public String geefOperator()
	{	return operator;
	}
	
	public Expressie geefSubstitutie()
	{	return formuleVak.geefExpressie();
	}
	
	public void actionPerformed(ActionEvent e)
	{	if(e.getSource()==formuleVak && e.getActionCommand().equals("zetMaat") && prefixVak!=null)
		{	int h2 = formuleVak.getLocation().y;		
			int dh = formuleVak.ashoogte-prefixVak.ashoogte;
			prefixVak.setLocation(10,h2+dh);
			setSize(Math.max(formuleVak.getSize().width+50, 110),formuleVak.getSize().height+30);
		}
		if(e.getActionCommand().equals("ingevuld")) 
		{	if(operator.equals("abc"))
			{	VergelijkingMeerv verg = ((AntwoordVergelijkingVak)getParent().getParent().getParent().getParent().getParent()).geefHuidigeVergelijking();
				System.out.println(verg.toString());
				double d = Algebra.geefDiscriminant(verg.geefVergelijking(0));
				double dAnt = formuleVak.geefExpressie().geefWaarde();
				boolean goed = Algebra.isGelijkDouble(d, dAnt);
				goedIC.setVisible(goed);
				foutIC.setVisible(!goed);
				if(goed)produceAction("discriminant");
			}
			else if(operator.equals("sub"))
			{	produceAction("substitutie");
			}
			else
			{	if(!operator.equals("haakjes") && !operator.equals("herleid") && !operator.equals("gelijkwaardig") && !operator.equals("ontbind") && !operator.equals("splits") && !operator.equals("wortel")  && !operator.equals("implicatie"))
				{	if(formuleVak.geefExpressie() == null) return;
				}
				produceAction("");
				Expressie exp = formuleVak.geefExpressie();
		 		if(exp!=null && Algebra.geefTermen(exp,new Vector()).size()>1)
		 		{	formuleVak.vulVak("$f$h" + exp.toString() + "@@");
		 			
		 		}
			}
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
 	//
	
}

