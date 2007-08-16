package fi.javalogoweb;

import java.awt.*;
import java.awt.event.*;
import java.util.Vector;
import fi.javalogoweb.formuleobjects.*;
import fi.javalogoweb.expressies.*;

public class CommandInvulVak extends FormuleElement
{	
	private Font font = new Font("TimesRoman",Font.PLAIN,13);
	private FontMetrics fm;
	private Label goedFoutLabel;
	
	FormuleVak formuleVak1;
	FormuleVak formuleVak2;
	
	String kOfG = "\u2264";
	String gOfG = "\u2265";
	
	String commandString;
	String kommaString;
	String haakjeString;
	
	public CommandInvulVak(String cs, String ks, String hs, String number1, String number2)
	{	setLayout(null);
	
		commandString = cs;
		kommaString = ks;
		haakjeString = hs;
		
		super.setFont(font);
		fm = getFontMetrics(getFont());
					
		setSize(fm.getAscent()/2 + fm.stringWidth(" = "),fm.getAscent() + fm.getDescent());
		ashoogte = fm.getAscent()/2;
		
		formuleVak1 = new FormuleVak();
		formuleVak1.vulVak("$f"+number1+"@");
		formuleVak1.setLocation(0,0);
		formuleVak1.setEditable(true);
		add(formuleVak1);
		
		formuleVak2 = new FormuleVak();
		formuleVak2.vulVak("$f"+number2+"@");
		formuleVak2.setLocation(fm.getAscent()/2 + fm.stringWidth(" = "),0);
		formuleVak1.setEditable(true);
		if(ks!=null) add(formuleVak2);
		
		
	}
	
	public void setFont(Font f)
	{	super.setFont(f);
		fm = getFontMetrics(getFont());
		formuleVak1.setFont(f);
		formuleVak2.setFont(f);
	}
	
	public void paint(Graphics g)
	{	zetMaat();
		g.setColor(Color.black);
		g.setFont(new Font("SansSerif",Font.PLAIN,12));
		
		int csWidth = fm.stringWidth(commandString);
		int ksWidth = kommaString!=null ? fm.stringWidth(kommaString) : 0;
		
		g.drawString(commandString, 0, ashoogte+fm.getAscent()/2+ fm.getDescent()/2);
		if(kommaString!=null)
		{	g.drawString(kommaString, csWidth + formuleVak1.getSize().width,ashoogte+fm.getAscent()/2+ fm.getDescent()/2);
			g.drawString(haakjeString, csWidth + formuleVak1.getSize().width + ksWidth + formuleVak2.getSize().width, ashoogte+fm.getAscent()/2+ fm.getDescent()/2);
		}
		else g.drawString(haakjeString, csWidth + formuleVak1.getSize().width, ashoogte+fm.getAscent()/2+ fm.getDescent()/2);
		
		super.paint(g);
		//((CommandComponent)getParent()).schuifveld.tekenOpnieuw();
		
	}
	
	public void zetMaat()
	{	int csWidth = fm.stringWidth(commandString);
		int ksWidth = kommaString!=null ? fm.stringWidth(kommaString) : 0;
		int hsWidth = fm.stringWidth(haakjeString);
		int b = csWidth + formuleVak1.getSize().width + ksWidth + formuleVak2.getSize().width + hsWidth;
		
		int h1 = formuleVak1.ashoogte;
		if(formuleVak2.ashoogte>h1)h1=formuleVak2.ashoogte;
		int h2 = formuleVak1.getSize().height - formuleVak1.ashoogte;
		if(formuleVak2.getSize().height - formuleVak2.ashoogte>h2)h2=formuleVak2.getSize().height - formuleVak2.ashoogte;
		
		setSize(b, h1+h2);
		ashoogte = h1;
		
		formuleVak1.setLocation(csWidth,ashoogte-formuleVak1.ashoogte);
		formuleVak2.setLocation(csWidth+formuleVak1.getSize().width + ksWidth,ashoogte-formuleVak2.ashoogte);
		//((CommandComponent)getParent()).zetMaat();
	}
	
	public boolean zetVergelijking(String s)
	{	if(s==null)return false;
		s = kiesRandom(s);
		int n = s.indexOf("=");
		if(n>-1)
		{	String s1 = s.substring(0,n);
			String s2 = s.substring(n+1);
			FormuleParser p = new FormuleParser();
			Expressie e1 = p.parse(p.schoon(p.formuleString("$f" + s1 + "@")));
			Expressie e2 = p.parse(p.schoon(p.formuleString("$f" + s2 + "@")));
			
			if(e1!=null && e2!=null)
			{	s1 = "$f" + e1.toString() + "@";
				s2 = "$f" + e2.toString() + "@";
				formuleVak1.vulVak(s1);
				formuleVak2.vulVak(s2);
				return true;
			}
			return false;
		}
		return false;
	}
	
	public String geefVergelijking()
	{	Expressie e1 = formuleVak1.geefExpressie();
		Expressie e2 = formuleVak2.geefExpressie();
		if(e1==null || e2==null)return null;
		String s1 = e1.toStringStrikt();
		String s2 = e2.toStringStrikt();
		String s = s1 + "=" + s2;
		return s;
	}
	
	public String kiesRandom(String s)
	{	Vector v = new Vector();
		String keuzeString = "";
		boolean keuzeProcesAan = false;
		for(int i=0 ; i<s.length() ; i++)
		{	char c = s.charAt(i);
			if(c=='}')
			{	keuzeProcesAan = false;
				int keuzeGetal = (int)(v.size()*Math.random());
				keuzeString = keuzeString + (String)v.elementAt(keuzeGetal);
				v.removeAllElements();
			}
			else if(keuzeProcesAan)
			{	int index = s.indexOf(";",i);
				if(index!=-1 && index<s.indexOf("}",i))
				{	String getalStr = s.substring(i,index);
					v.addElement( getalStr);
					i = i + getalStr.length();
				}
				else 
				{	index = s.indexOf("}",i);
					String getalStr = s.substring(i,index);
					v.addElement( getalStr);
					i = i + getalStr.length()-1;
				}
			}
			else if(c=='{')
			{	keuzeProcesAan = true;
			}
						
			else keuzeString = keuzeString + c;
		}
		return keuzeString;
	}
	
	public void zetActief(boolean b)
	{	formuleVak1.requestFocus();
	}
	
	
	
}

