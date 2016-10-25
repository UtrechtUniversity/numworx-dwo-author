package fi.wiskopdr.opdrnav;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import fi.wiskopdr.WiskOpdr;
//import fi.beans.tooltip.*;

public class NrComponent extends JComponent implements MouseListener //ToolTipIF 
{	
	private int nr;
	private Color kl;
	private boolean selected, half;
	private ActionListener actionListener;
    private Font f= new Font("SansSerif", Font.PLAIN,11);
    private Font fGR= new Font("SansSerif", Font.BOLD,12);
	//private String toolTip;
    private boolean tab = false;
    private boolean letters;
    private String[] opdrLetters = {"a","b","c","d","e","f","g","h","i","j","k","l","m","o","p","q","r","s","t","u"};
    private boolean enabled = true;
    private boolean noScore;
    private int size = 25;
    
    private static int GOED = 1;
    private static int FOUT = 0;
    
    private int staat = FOUT;

	
    public NrComponent(int n)
    {	this(n,25);
    	
    }
    
	public NrComponent(int n, int size)
	{	this.size = size;
		f = new Font("SansSerif", Font.PLAIN,(size-1)/2-1);
		//if("GR".equals(WiskOpdr.deployVariant)) f = new Font("SansSerif", Font.PLAIN,(size-1)/2);
		
		setBounds((n-1)*size, 0, size-3, size-1);
		addMouseListener(this);
		selected = false;
		half = false;
		nr = n;
		kl = Color.white;
		setOpaque(false);
		//setToolTip(LinVerg.rb.getString("opdrachtToolTip"));
	}
	
	public void setTab(boolean b)
	{	tab = b;
	}
	
	public void setEnabled(boolean b)
	{	enabled = b;
	}
	
	public boolean getEnabled()
	{	return enabled ;
	}
	
	public void zetLetter(boolean b)
	{	letters = b;
	}
	
	public void paintComponent(Graphics gr)
	{	Graphics g;
        //if(WiskOpdr.deployVariant!=null && WiskOpdr.deployVariant.equals("GR"))
		g = gr;
		if(g instanceof Graphics2D)
        {
              ((Graphics2D)g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        }
        //else 
		//g=gr;
         
        g.setFont(f);
        if(WiskOpdr.deployVariant!=null && WiskOpdr.deployVariant.equals("GR")) g.setFont(fGR);
		if(tab)
		{
			g.setColor(Color.white);//new Color(210,210,210));
			if(!selected)g.setColor(new Color(230,230,230));
			g.fillRect(0,0,20,22);
			g.setColor(Color.gray.darker());
			g.drawRect(0,0,20,22);
			if(selected)
			{	g.drawRect(1,1,18,22);
				//g.drawRect(2,2,16,16);
				g.setColor(Color.white);//new Color(210,210,210));
				g.fillRect(2,18,17,6);
				
			}
			else
			{
				g.setColor(Color.gray);
				//g.drawLine(0,getSize().height-5,getSize().width-1,getSize().height-5);
				g.drawLine(0,getSize().height-2,getSize().width,getSize().height-2);
			}
			int cor;
			if(nr<10)cor = 3;
			else cor = 0;
			g.setColor(Color.black);
			g.drawString(Integer.toString(nr),5+cor,15);
		}
		else
		{
			int diam = size-5;
	        g.setColor(kl);
	        if(noScore)g.setColor(Color.lightGray);
	        if(!enabled)g.setColor(getBackground());
			if(half)
			{	g.fillOval(0,0,diam,diam);
				g.setColor(new Color(0,150,0));
				g.fillOval(4,4,diam-8,diam-8);
				g.setColor(kl);
			}
			else 
			{	
				//g.fillOval(0,0,diam,diam);
				if("GR".equals(WiskOpdr.deployVariant)) {
					if(selected)g.setColor(kl);
					else g.setColor(Color.white);
					g.fillRect(0,0,diam,diam);
				}
				else {
					Color c = g.getColor();
					for(int i=0 ; i<10 ; i++)
					{
						int red = c.getRed();
						int green = c.getGreen();
						int blue = c.getBlue();
						red = red+i*(255-red)/13;
						green = green+i*(255-green)/13;
						blue = blue+i*(255-blue)/13;
						g.setColor(new Color(red,green,blue));
						g.fillOval(i+i/3,i/3,diam-2*i,diam-2*i);
						
					}
				}
			}
			
			if("GR".equals(WiskOpdr.deployVariant)) {
				if(staat==FOUT )g.setColor(new Color(70,117,186));
				else g.setColor(kl);
				g.drawRect(0,0,diam-1,diam-1);
				//g.drawRect(1,1,diam-3,diam-3);
			}
			else {
				g.setColor(Color.gray);
				g.drawOval(0,0,diam,diam);
			}
			if(selected)
			{	
				if("GR".equals(WiskOpdr.deployVariant)) {
					
					if(staat==GOED)g.setColor(kl);
					//else g.setColor(Color.white);
					//g.setColor(new Color(70,117,186));
					g.fillRect(0,0,diam,diam);
				}
				else {
					g.setColor(Color.black);
					g.drawOval(0,0,diam,diam);
					g.drawOval(1,1,diam-2,diam-2);
					//g.drawOval(2,2,16,16);
					g.drawLine(0,diam+2,diam,diam+2);
					g.drawLine(0,diam+3,diam,diam+3);
				}
				
			}
			
			String tekst = Integer.toString(nr);
			if(letters) tekst = opdrLetters[nr-1];
			
			FontMetrics fm = g.getFontMetrics();
			int tekstLengte = fm.stringWidth(tekst);
			g.setColor(Color.black);
			if("GR".equals(WiskOpdr.deployVariant))
			{	if(selected)g.setColor(Color.white);
				else if(staat==GOED)g.setColor(kl);
				else g.setColor(new Color(70,117,186));
				tekstLengte+=2;
			}
			g.drawString(tekst,(int)Math.rint((double)diam-tekstLengte+2)/2,size-2*size/5);
			
			
			/*int cor;
			if(nr<10 || letters)cor = size/8;
			else cor = 0;
			g.setColor(Color.black);
			if(letters)g.drawString(opdrLetters[nr-1],size/5+cor,size-2*size/5);
			else g.drawString(Integer.toString(nr),size/5+cor,size-2*size/5);*/
		}
	}
	public void setSelected(boolean b)
	{	selected = b;
		repaint();
	}
	public void zetGemaakt(boolean b)
	{	if(b) {
			staat = GOED;
			kl = new Color(0,150,0);
			if("GR".equals(WiskOpdr.deployVariant))kl = new Color(142,190,67);
		}
		else {
			staat = FOUT;
			kl = new Color(255,150,150);
			if("GR".equals(WiskOpdr.deployVariant))kl = Color.white; //kl = new Color(224,8,29);
		}
		repaint();
	}
	public void zetNoScore(boolean b)
	{	noScore = b;
		repaint();
	}
	public boolean geefNoScore()
	{	return noScore;
	}
	public boolean geefGoedFout()
	{	if(kl.equals(new Color(0,150,0)) || kl.equals(new Color(218,232,205)))return true;
		return false;
	}
	
	public void zetGemaaktHalf()
	{	half = true;
		repaint();
	}
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
		{	
			if(enabled && e.getModifiers()== e.BUTTON3_MASK || e.isControlDown())
			{
				actionListener.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "popup-"+Integer.toString(nr)));
			}
			else if(enabled)
			{
				actionListener.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, Integer.toString(nr)));
			}
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