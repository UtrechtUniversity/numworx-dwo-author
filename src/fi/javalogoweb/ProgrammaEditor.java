package fi.javalogoweb;
					   
import java.applet.*;
import java.awt.*;
import java.awt.event.*;

import javax.swing.JPanel;

import logotekenap.Tekenblad;


public class ProgrammaEditor extends JPanel implements  ActionListener, MouseListener
{	
	private Image im;
	private Graphics gIm;
	private boolean resized = false;
	
	
	public ProgrammaComponent contentPane; 
		
	private int balkH = 23;
	private int rand = 10;
	
	private boolean randVerhoging = true;
	
	private ScrollSlider scrollSlider;
	
	//private Tablet tablet;
	
	public ProgrammaEditor(int x, int y, int b, int h, JavaLogoSchuifVeld schuifveld)
	{	setLayout(null);
		addMouseListener(this);
		setBackground(new Color(240,240,240));
		
		
		
		contentPane = new ProgrammaComponent(0,0,200,300, schuifveld);
		contentPane.zetVast(true);
		contentPane.setLayout(null);
		contentPane.setBackground(new Color(240,240,240));
		super.add(contentPane);
				
		
		
		scrollSlider = new ScrollSlider(200,0,false);
		scrollSlider.zetStand(0);
		scrollSlider.setLocation(375,40);
		scrollSlider.addActionListener(this);
		super.add(scrollSlider);
		scrollSlider.setVisible(false);
		
		setBounds(x,y,b,h);
		
	}
	
	public void teken(Tekenblad tb, VarSet varSet)
	{	contentPane.teken(tb, varSet);
		
	}
	
	
	public void setBounds(int x, int y, int b, int h)
	{	
		contentPane.setBounds(rand,balkH+2,b-2*rand,h-balkH-rand-4);
		resized = true;
		super.setBounds(x,y,b,h);
	}
	                            
	public void zetOpBalk(Component c)
	{	super.add(c);
	}
	
	
	public Component add(Component c)
	{	Component comp = contentPane.add(c);
		
		contentPane.setSize(contentPane.getSize().width, geefBenodigdeHoogte());
		//contentPaneNep.setSize(contentPaneNep.getSize().width, geefBenodigdeHoogte());
		//scrollPane.doLayout();
		//contentPaneNep.setSize(contentPaneNep.getSize().width, geefBenodigdeHoogte());
		//scrollPane.setScrollPosition(0,contentPane.getSize().height-scrollPane.getSize().height+20);
		scroll();
		contentPane.repaint();
		return comp;
	}
	
	public Component add(Component c,int n)
	{	Component comp = contentPane.add(c,n);
		contentPane.setSize(contentPane.getSize().width, geefBenodigdeHoogte());
		//contentPaneNep.setSize(contentPaneNep.getSize().width, geefBenodigdeHoogte());
		//scrollPane.doLayout();
		//contentPaneNep.setSize(contentPaneNep.getSize().width, geefBenodigdeHoogte());
		//scrollPane.setScrollPosition(0,contentPane.getSize().height-scrollPane.getSize().height+20); 
		scroll();
		contentPane.repaint();
		return comp;
	}
	
	public void scroll()
	{	int extraHoogte = contentPane.getSize().height - getSize().height + balkH;
		if(extraHoogte > 0)
		{	contentPane.setLocation(0, balkH - extraHoogte);
			if(scrollSlider!=null) 
			{	scrollSlider.zetStand(extraHoogte);
				scrollSlider.setVisible(true);
			}
		}
		else 
		{	contentPane.setLocation(0, balkH);
			if(scrollSlider!=null) 
			{	scrollSlider.zetStand(0);
				scrollSlider.setVisible(false);
			}
		}
		
	}
	
	public void remove(Component c)
	{	contentPane.remove(c);
		contentPane.setSize(contentPane.getSize().width, geefBenodigdeHoogte());
		//contentPaneNep.setSize(contentPaneNep.getSize().width, geefBenodigdeHoogte());
		//scrollPane.doLayout();
		//contentPaneNep.setSize(contentPaneNep.getSize().width, geefBenodigdeHoogte());
		//scrollPane.setScrollPosition(0,contentPane.getSize().height-scrollPane.getSize().height+20);
		scroll();
		contentPane.repaint();
	}
	
	public void removeSoft(Component c)
	{	contentPane.remove(c);
		contentPane.setSize(contentPane.getSize().width, geefBenodigdeHoogte());
		scroll();
		contentPane.repaint();
	}
	
	public int geefBenodigdeHoogte()
	{	//int max = scrollPane.getSize().height-20;
		int max = 0;
		for(int i=0 ; i<contentPane.getComponentCount() ; i++)
		{	Component c = contentPane.getComponent(i);
			int h = c.getLocation().y + c.getSize().height + 50;
			if(h>max) max = h;
		}
		return max;
	}
		
	public void zetRandverhoging(boolean b)
	{	randVerhoging = b;
	}
	
	public void paintComponent(Graphics g)
	{	{ 	if(im==null || resized)
			{	im = createImage(getSize().width,getSize().height);
  				gIm = im.getGraphics();
  				resized = false;
			}
			gIm.setColor(getBackground());
			gIm.fillRect(0,0,getSize().width,getSize().height);
			paintBuffer(gIm);
			g.drawImage(im, 0, 0, null);
  		}
	}
	
	//public void update(Graphics g)
	//{	paint(g);
	//}
	public void paintBuffer(Graphics g)
	{	
		
		g.setColor(new Color(240,240,240));
		g.fillRect(0,9,getSize().width-4, getSize().height-9);
		g.setColor(Color.white);
		g.fillRect(getSize().width-4,9,4, getSize().height-9);
		//super.paint(g);
		g.fillRect(0,0,getSize().width, 9);
		g.setColor(new Color(240,240,240));
		g.fillRect(0,9,getSize().width-4, 30);
		//g.setColor(new Color(180,180,180));
		//g.drawRect(394,35,4,getSize().height-40);
		
	}
	
	public void actionPerformed(ActionEvent e)
	{	if(e.getSource()==scrollSlider)
		{
			contentPane.setLocation(0, balkH - scrollSlider.geefStand());
		}
	}
	
	public void mousePressed(MouseEvent e)
	{	
	}
	public void mouseReleased(MouseEvent e){;}
	public void mouseExited(MouseEvent e){;}
	public void mouseClicked(MouseEvent e){;}
	public void mouseEntered(MouseEvent e)
	{	//if(formuleVak!=null)formuleVak.requestFocus();
	}
	
	
	
	public void zetFormuleVak(String s)
	{	//remove(formuleVak);
		//formuleVak = new FormuleVak();
		//formuleVak.setLocation(5,5);
		//add(formuleVak);
		//functieVak.formuleVak1.requestFocus();
		//functieVak.formuleVak1.vulVak(s);
	}
}
