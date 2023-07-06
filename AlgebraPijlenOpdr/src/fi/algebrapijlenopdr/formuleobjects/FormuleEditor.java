package fi.algebrapijlenopdr.formuleobjects;
					   
import java.awt.*;
import java.awt.event.*;

public class FormuleEditor extends Panel implements ActionListener, MouseListener, AdjustmentListener
{	
	private Image im;
	private Graphics gIm;
	
	private FormuleButton wortelKnop, machtKnop, kwadraatKnop, breukKnop, haakjesKnop, ndewortelKnop;
	protected FormuleVak formuleVak;
	private boolean actief;
	private ScrollPane scrollPane;
	private boolean scrollbar;
	private BufferedPanel contentPane; 
	private Panel contentPaneNep; //truc om een buffered scrollpane te krijgen.
	private Panel p1,p2,p3,p4;
	private boolean formMode = true;
	
	public FormuleEditor(boolean scrollbar)
	{	setLayout(null);
		addMouseListener(this);
		setBackground(Color.lightGray);
		
		this.scrollbar = scrollbar;
		if(scrollbar)scrollPane = new ScrollPane(ScrollPane.SCROLLBARS_ALWAYS);
		else scrollPane = new ScrollPane(ScrollPane.SCROLLBARS_NEVER);
		scrollPane.setBackground(Color.white);
		super.add(scrollPane);
		scrollPane.getVAdjustable().addAdjustmentListener(this);
		
		contentPane = new BufferedPanel();
		contentPane.setLayout(null);
		contentPane.setBackground(Color.white);
		super.add(contentPane);
		
		contentPaneNep = new Panel();
		contentPaneNep.setLayout(null);
		scrollPane.add(contentPaneNep);
		
		wortelKnop = new FormuleButton("wortel");
		wortelKnop.setBounds(12,2,20,20);
		wortelKnop.addActionListener(this);
		super.add(wortelKnop);
		
		machtKnop = new FormuleButton("macht");
		machtKnop.setBounds(38,2,20,20);
		machtKnop.addActionListener(this);
		super.add(machtKnop);
		
		kwadraatKnop = new FormuleButton("kwadraat");
		kwadraatKnop.setBounds(64,2,20,20);
		kwadraatKnop.addActionListener(this);
		super.add(kwadraatKnop);
		
		breukKnop = new FormuleButton("breuk");
		breukKnop.setBounds(90,2,20,20);
		breukKnop.addActionListener(this);
		super.add(breukKnop);
		
		haakjesKnop = new FormuleButton("haakjes");
		haakjesKnop.setBounds(116,2,20,20);
		haakjesKnop.addActionListener(this);
		super.add(haakjesKnop);
		
		ndewortelKnop = new FormuleButton("ndewortel");
		ndewortelKnop.setBounds(142,2,20,20);
		ndewortelKnop.addActionListener(this);
		super.add(ndewortelKnop);
		
		formuleVak = new FormuleVak();
		formuleVak.setLocation(10,20);
		add(formuleVak);
		
		p1 = new Panel();
		p1.setBackground(new Color(210,210,210));
		super.add(p1,0);
		
		p2 = new Panel();
		p2.setBackground(Color.gray.darker());
		super.add(p2,0);
		
		p3 = new Panel();
		p3.setBackground(Color.white);
		super.add(p3,0);
		
		p4 = new Panel();
		p4.setBackground(Color.white);
		super.add(p4,0);
	}
	
	public void zetFormMode(boolean b)
	{	if(b)
		{	formMode = b;
			wortelKnop.setVisible(true);
			machtKnop.setVisible(true);
			kwadraatKnop.setVisible(true);
			ndewortelKnop.setVisible(true);
			breukKnop.setVisible(true);
			haakjesKnop.setVisible(true);
		}
		else
		{	formMode = b;
			wortelKnop.setVisible(false);
			machtKnop.setVisible(false);
			kwadraatKnop.setVisible(false);
			ndewortelKnop.setVisible(false);
			breukKnop.setVisible(false);
			haakjesKnop.setVisible(false);
		}
	}
	
	public void zetScrollOptie(boolean scrollbar)
	{	super.remove(scrollPane);
		this.scrollbar = scrollbar;
		if(scrollbar)scrollPane = new ScrollPane(ScrollPane.SCROLLBARS_AS_NEEDED);
		else scrollPane = new ScrollPane(ScrollPane.SCROLLBARS_NEVER);
		scrollPane.setBackground(Color.white);
		super.add(scrollPane);
		scrollPane.getVAdjustable().addAdjustmentListener(this);
		
		scrollPane.add(contentPaneNep);
		
		setBounds(getBounds().x, getBounds().y, getBounds().width,getBounds().height);
	}
	
	public void destroy()
	{	remove(contentPane);
		contentPane.destroy();
		contentPane = null;
		if(gIm!=null)
		{	gIm.dispose();
			gIm = null;
		}
	}
	
	public void setBounds(int x, int y, int b, int h)
	{	if(scrollbar)scrollPane.setBounds(b-30,23,20,h-17);
		else scrollPane.setBounds(b,23,20,h-5);
		contentPane.setBounds(10,25,b-20,h-37);
		contentPaneNep.setBounds(10,2,b-40,h-37);
		p1.setBounds(b-30,h-12,20,12);
		p2.setBounds(b-30,h-2,20,2);
		p3.setBounds(b-30,h-11,20,1);
		p4.setBounds(b-30,25,2,h-37);
		super.setBounds(x,y,b,h);
	}
	
	public void zetOpBalk(Component c)
	{	super.add(c);
	}
	
	public void zetScrollBar()
	{	scrollPane.doLayout();
	}
	
	public Component add(Component c)
	{	Component comp = contentPane.add(c);
		contentPane.setSize(getSize().width, geefBenodigdeHoogte());
		contentPaneNep.setSize(getSize().width, geefBenodigdeHoogte());
		scrollPane.doLayout();
		contentPaneNep.setSize(getSize().width, geefBenodigdeHoogte());
		scrollPane.setScrollPosition(0,contentPane.getSize().height-scrollPane.getSize().height+20); 
		contentPane.repaint();
		return comp;
	}
	
	public Component add(Component c,int n)
	{	Component comp = contentPane.add(c,n);
		contentPane.setSize(contentPane.getSize().width, geefBenodigdeHoogte());
		contentPaneNep.setSize(contentPaneNep.getSize().width, geefBenodigdeHoogte());
		scrollPane.doLayout();
		contentPaneNep.setSize(contentPaneNep.getSize().width, geefBenodigdeHoogte());
		scrollPane.setScrollPosition(0,contentPane.getSize().height-scrollPane.getSize().height+20); 
		contentPane.repaint();
		return comp;
	}
	
	public void remove(Component c)
	{	contentPane.remove(c);
		contentPane.setSize(contentPane.getSize().width, geefBenodigdeHoogte());
		contentPaneNep.setSize(contentPaneNep.getSize().width, geefBenodigdeHoogte());
		scrollPane.doLayout();
		contentPaneNep.setSize(contentPaneNep.getSize().width, geefBenodigdeHoogte());
		//scrollPane.setScrollPosition(0,contentPane.getSize().height-scrollPane.getSize().height+20); 
		contentPane.repaint();
	}
	
	public int geefBenodigdeHoogte()
	{	int max = scrollPane.getSize().height-20;
		for(int i=0 ; i<contentPane.getComponentCount() ; i++)
		{	Component c = contentPane.getComponent(i);
			int h = c.getLocation().y + c.getSize().height + 50;
			if(h>max) max = h;
		}
		return max;
	}
		
	
	public void paint(Graphics g)
	{	{ 	if(im==null)
			{	im = createImage(getSize().width,getSize().height);
  				gIm = im.getGraphics();
			}
			gIm.setColor(getBackground());
			gIm.fillRect(0,0,getSize().width,getSize().height);
			paintBuffer(gIm);
			g.drawImage(im, 0, 0, null);
  		}
	}
	
	public void update(Graphics g)
	{	paint(g);
	}
	public void paintBuffer(Graphics g)
	{	g.setColor(Color.white);
		g.fillRect(0,23,getSize().width-1, getSize().height-23);
		
		super.paint(g);
		
		g.setColor(new Color(210,210,210));
		g.fillRect(0,0,getSize().width, 23);
		g.fillRect(0,0,10,getSize().height);
		g.fillRect(getSize().width-12,0,12,getSize().height);
		g.fillRect(0,getSize().height-12,getSize().width, 12);
		
		g.setColor(Color.white);
		g.drawLine(1,1,getSize().width-1,1);
		g.drawLine(1,1,1,getSize().height-1);
		g.setColor(Color.gray.darker());
		g.drawLine(1,getSize().height-2,getSize().width-2,getSize().height-2);
		g.drawLine(0,getSize().height-1,getSize().width-1,getSize().height-1);
		g.drawLine(getSize().width-1,0,getSize().width-1,getSize().height-1);
		g.drawLine(getSize().width-2,1,getSize().width-2,getSize().height-2);
		
		g.setColor(Color.black);
		//g.drawRect(0,0,getSize().width-1, getSize().height-1);
		//g.drawRect(0,23,getSize().width-1, getSize().height-24);
		
		if(formMode)
		{	g.setColor(Color.lightGray.darker());
			g.drawLine(34,2,34,21);
			g.drawLine(60,2,60,21);
			g.drawLine(86,2,86,21);
			g.drawLine(112,2,112,21);
			g.setColor(Color.lightGray.brighter());
			g.drawLine(35,2,35,21);
			g.drawLine(61,2,61,21);
			g.drawLine(87,2,87,21);
			g.drawLine(113,2,113,21);
		}
		
		
		g.setColor(Color.gray.darker());
		g.drawLine(10,23,getSize().width-11,23);
		g.drawLine(10,23,10,getSize().height-11);
		g.drawLine(11,24,getSize().width-13,24);
		g.drawLine(11,24,11,getSize().height-12);
		g.setColor(Color.white);
		g.drawLine(11,getSize().height-11,getSize().width-21,getSize().height-11);
		g.drawLine(getSize().width-11,23,getSize().width-11,getSize().height-11);
		
		//g.drawLine(getSize().width-1,0,getSize().width-1,23);
		//g.drawLine(getSize().width-2,0,getSize().width-2,23);
		
		//g.setColor(new Color(180,180,180));
		//g.drawLine(getSize().width-2,24,getSize().width-2,getSize().height-2);
		//g.drawLine(1,getSize().height-2,getSize().width-2,getSize().height-2);
		//g.setColor(new Color(230,230,230));
		//g.drawLine(0,0,getSize().width-1,0);
		//g.drawLine(0,0,0,23);
		//g.drawLine(1,1,getSize().width-2,1);
		//g.drawLine(1,1,1,23);
		//g.drawLine(getSize().width-1,24,getSize().width-1,getSize().height-1);
		//g.drawLine(0,getSize().height-1,getSize().width-1,getSize().height-1);
		
		//g.setColor(Color.black);
		
		
		
	}
	
	public FormuleVak geefFormuleVak()
	{	return formuleVak;
	}
	
	public void actionPerformed(ActionEvent e)
	{	if(e.getSource()==wortelKnop)
		{	if(formuleVak!=null)formuleVak.zetWortelVak();
		}
		else if(e.getSource()==machtKnop)
		{	if(formuleVak!=null)formuleVak.zetMachtVak();
		}
		else if(e.getSource()==kwadraatKnop)
		{	if(formuleVak!=null)formuleVak.zetKwadraatVak();
		}
		else if(e.getSource()==breukKnop)
		{	if(formuleVak!=null)formuleVak.zetBreukVak();
		}
		else if(e.getSource()==haakjesKnop)
		{	if(formuleVak!=null)formuleVak.zetHaakjesVak();
		}
		else if(e.getSource()==ndewortelKnop)
		{	if(formuleVak!=null)formuleVak.zetNdeWortelVak();
		}
		
	}
	
	public boolean isFocusTraversable()
	{	return true;
	}
	
	public void mousePressed(MouseEvent e)
	{	if(formuleVak!=null)formuleVak.requestFocus();
		if(formuleVak!=null)formuleVak.setSelected(false);
	}
	public void mouseReleased(MouseEvent e){;}
	public void mouseExited(MouseEvent e){;}
	public void mouseClicked(MouseEvent e){;}
	public void mouseEntered(MouseEvent e)
	{	//if(formuleVak!=null)formuleVak.requestFocus();
	}
	
	public void adjustmentValueChanged(AdjustmentEvent e)
	{	int h = e.getValue();
		contentPane.setLocation(10,25-h);
		repaint();
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
