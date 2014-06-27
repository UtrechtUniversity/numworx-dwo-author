package fi.wiskopdr.tekstobjects;

import java.awt.*;
import java.awt.event.*;
import fi.wiskopdr.formuleobjects.*;
import fi.wiskopdr.tekstobjects.*;
import fi.wiskopdr.WiskOpdr;

public class TekstLinkVak extends TekstDeelVak implements ActionListener
{
	private LinkRegel linkRegel;
	//private Font font = new Font("SansSerif",Font.BOLD,12);
	private FontMetrics fm;
	private boolean selected = false;
	
	public TekstLinkVak(TekstVak tv)
	{	super(tv);
		setFont(WiskOpdr.tekstFontBold);
		if("GR".equals(WiskOpdr.deployVariant))setFont(new Font("Arial",Font.PLAIN,13));
		fm = getFontMetrics(getFont());
	
		linkRegel = new LinkRegel(this);
		//linkRegel.addKeyListener(linkRegel);
		//linkRegel.addFocusListener(linkRegel);
		linkRegel.setFont(WiskOpdr.tekstFontBold);
		linkRegel.setForeground(new Color(70,116,183));
		linkRegel.setLocation(0,0);
		linkRegel.setEditable(true);
		linkRegel.setSelectable(false);
		
		add(linkRegel,0);
				
		setSize(linkRegel.getSize().width,linkRegel.getSize().height);
		ashoogte = linkRegel.getAsHoogte();
		
	}
	
	public void setBackground(Color c)
	{	super.setBackground(c);
		Component[] components = getComponents();
		for(int i=0 ; i<components.length ; i++)
		{
			components[i].setBackground(c);
		}
		
	}
	
	
	
	public void setFont(Font font)
	{	super.setFont(font);
		Font f = new Font(font.getFontName(), Font.BOLD, font.getSize());
		if(WiskOpdr.mac || WiskOpdr.zoefi) f = font;
		if(linkRegel==null) return;
		
		if("GR".equals(WiskOpdr.deployVariant))linkRegel.setFont(new Font("Arial",Font.PLAIN,13));
		else linkRegel.setFont(f);
		setSize(linkRegel.getSize().width, linkRegel.getSize().height);
		ashoogte = linkRegel.getAsHoogte();
	}
	public void editLink()
	{	linkRegel.editLink();
	}
	
	public void requestFocus()
	{	linkRegel.requestFocus();
	}
	
	public void vulVak(String s)
	{	int[] grensScores;
		
		for(int i=0 ; i<s.length()&& i<s.indexOf("$") ; i++) 
		linkRegel.insert(new TekstTeken(s.charAt(i)));
		
		int linkTekstStart = 0;
		int linkTekstEnd = s.indexOf("$");
		String linkTekst = s.substring(linkTekstStart,linkTekstEnd);
		
		int j = 0;
		int teller = 0;
		while(s.indexOf("$U", j) >= 0)
		{	teller++;
			j=s.indexOf("$U", j)+2;
		}
		
		//int urlStart = s.indexOf("$U")+2;
		//int urlEnd = s.indexOf("@",urlStart);
		//String url = s.substring(urlStart,urlEnd);
		
		String[] urls = new String[teller];
		
		int urlStart = s.indexOf("$U")+2;
		int urlEnd = s.indexOf("@",urlStart);
		urls[0] = s.substring(urlStart,urlEnd);
		
		for(int i = 1; i < teller; i++)
		{	urlStart = urlEnd + 3;
			urlEnd = s.indexOf("@", urlStart);
			urls[i] = s.substring(urlStart, urlEnd);
		}
		
		j = 0;
		teller = 0;
		while(s.indexOf("$G", j) >= 0)
		{	teller++;
			j=s.indexOf("$G", j)+2;
		}
		
		
		if(teller>0)
		{	grensScores = new int[teller];
			
			int grensStart = s.indexOf("$G")+2;
			int grensEnd = s.indexOf("@",grensStart);
			grensScores[0] = Integer.parseInt(s.substring(grensStart,grensEnd));
			
			for(int i = 1; i < teller; i++)
			{	grensStart = grensEnd + 3;
				grensEnd = s.indexOf("@", grensStart);
				grensScores[i] = Integer.parseInt(s.substring(grensStart, grensEnd));
			}
		}
		else
			grensScores = null;
		
		int widthStart = s.indexOf("$B")+2;
		int widthEnd = s.indexOf("@",widthStart);
		int width = Integer.parseInt(s.substring(widthStart,widthEnd));
		
		int heightStart = s.indexOf("$C")+2;
		int heightEnd = s.indexOf("@",heightStart);
		int height = Integer.parseInt(s.substring(heightStart,heightEnd));
		
		//linkRegel.setLink(new Link(linkTekst,url,width,height));
		linkRegel.setLink(new Link(linkTekst, urls, width, height, grensScores));
		linkRegel.setUnderlined(true);
	
	}
	
	public LinkRegel geefTekstVak()
	{	return linkRegel;
	}
	
	public void setEditable(boolean b)
	{	linkRegel.setEditable(b);
	}
	
	public void setSelectable(boolean b)
	{	linkRegel.setSelectable(b);
	}
	
	public void setSelected(boolean b)
	{	linkRegel.setSelected(b);
		selected = b;
	}
	
	public boolean isSelected()
	{	return selected;
	}
	
	public void zetMaat()
	{	setSize(linkRegel.getSize().width, linkRegel.getSize().height);
		linkRegel.setLocation(0,0);
		ashoogte = linkRegel.getAsHoogte();
		if(getParent()instanceof TekstElement)((TekstElement)getParent()).zetMaat();
		
	}
	
	public String toString()
	{	return "$H" + linkRegel.toString() + "@";
	}
	
	//public String getUrl()
	//{	return linkRegel.getLink().getUrl();
	//}
	
	public String[] getUrlString()
	{
		return linkRegel.getLink().getUrlString();
	}
	
	public int[] getGrensScores()
	{
		return linkRegel.getLink().getGrensScores();
	}
	
	public int getFrameWidth()
	{	return linkRegel.getLink().getWidth();
	}
	
	public int getFrameHeight()
	{	return linkRegel.getLink().getHeight();
	}
	
	public void actionPerformed(ActionEvent e)
	{	if(e.getActionCommand().equals("focus"))requestFocus();//tekstVak.zetAntwoordVak(antwoordVak);
	}
	
}
