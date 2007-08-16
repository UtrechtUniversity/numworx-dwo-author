package fi.javalogoweb.formuleobjects;
					   
import java.applet.*;
import java.awt.*;
import java.awt.event.*;

//mport fi.wiskopdr.WiskOpdr;
import fi.javalogoweb.expressies.*;

public class FormuleEditor extends Panel implements ActionListener, MouseListener, AdjustmentListener
{	
	private Image im;
	private Graphics gIm;
	private boolean resized = false;
	
	private FormuleButton wortelKnop, machtKnop, kwadraatKnop, breukKnop, haakjesKnop, ndewortelKnop, ndelogKnop;
	private FormuleButton tabletButton;
	public FormuleVak formuleVak;
	private boolean actief;
	private ScrollPane scrollPane;
	private boolean scrollbar;
	private BufferedPanel contentPane; 
	private Panel contentPaneNep; //truc om een buffered scrollpane te krijgen.
	private Panel p1,p2,p3,p4;
	private boolean formMode = true;
	
	private int balkH = 23;
	private int rand = 10;
	
	private boolean randVerhoging = true;
	
	//private Tablet tablet;
	
	public FormuleEditor(boolean scrollbar)
	{	setLayout(null);
		addMouseListener(this);
		setBackground(Color.lightGray);
		
		/*if(WiskOpdr.mobileVersion)
		{	balkH = 18;
			rand = 3;
		}*/
		
		this.scrollbar = scrollbar;
		if(scrollbar)scrollPane = new ScrollPane(ScrollPane.SCROLLBARS_AS_NEEDED);
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
		
		tabletButton = new FormuleButton("tablet");
		tabletButton.setBounds(2,2,16,16);
		tabletButton.addActionListener(this);
		super.add(tabletButton);
		
		wortelKnop = new FormuleButton("wortel");
		wortelKnop.setBounds(22,2,20,20);
		wortelKnop.addActionListener(this);
		super.add(wortelKnop);
		
		machtKnop = new FormuleButton("macht");
		machtKnop.setBounds(48,2,20,20);
		machtKnop.addActionListener(this);
		super.add(machtKnop);
		
		kwadraatKnop = new FormuleButton("kwadraat");
		kwadraatKnop.setBounds(74,2,20,20);
		kwadraatKnop.addActionListener(this);
		super.add(kwadraatKnop);
		
		breukKnop = new FormuleButton("breuk");
		breukKnop.setBounds(100,2,20,20);
		breukKnop.addActionListener(this);
		super.add(breukKnop);
		
		haakjesKnop = new FormuleButton("haakjes");
		haakjesKnop.setBounds(126,2,20,20);
		haakjesKnop.addActionListener(this);
		super.add(haakjesKnop);
		
		ndewortelKnop = new FormuleButton("ndewortel");
		ndewortelKnop.setBounds(152,2,20,20);
		ndewortelKnop.addActionListener(this);
		super.add(ndewortelKnop);
		
		ndelogKnop = new FormuleButton("ndelog");
		ndelogKnop.setBounds(178,2,25,20);
		ndelogKnop.addActionListener(this);
		super.add(ndelogKnop);
		
		formuleVak = new FormuleVak();
		formuleVak.setLocation(10,10);
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
		
		/*if(WiskOpdr.mobileVersion)
		{	tabletButton.setVisible(false);
			wortelKnop.setVisible(false);
			machtKnop.setVisible(false);
			kwadraatKnop.setVisible(false);
			breukKnop.setVisible(false);
			haakjesKnop.setVisible(false);
			ndewortelKnop.setVisible(false);
			ndelogKnop.setVisible(false);
		}*/
	}
	
	public void zetFormMode(boolean b)
	{	if(b)
		{	formMode = b;
			wortelKnop.setVisible(true);
			machtKnop.setVisible(true);
			kwadraatKnop.setVisible(true);
			ndewortelKnop.setVisible(true);
			ndelogKnop.setVisible(true);
			breukKnop.setVisible(true);
			haakjesKnop.setVisible(true);
		}
		else
		{	formMode = b;
			wortelKnop.setVisible(false);
			machtKnop.setVisible(false);
			kwadraatKnop.setVisible(false);
			ndewortelKnop.setVisible(false);
			ndelogKnop.setVisible(false);
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
	{	if(scrollbar)scrollPane.setBounds(b-21-rand,balkH,21,h-rand-7);
		else scrollPane.setBounds(b,balkH,20,h-5);
		contentPane.setBounds(rand,balkH+2,b-2*rand,h-balkH-rand-4);
		contentPaneNep.setBounds(rand,2,b-2*rand-20,h-balkH-rand-4);
		p1.setBounds(b-21-rand,h-rand-2,21,rand+2);
		p2.setBounds(b-21-rand,h-2,21,2);
		p3.setBounds(b-21-rand,h-rand-1,21,1);
		p4.setBounds(b-21-rand,balkH+2,2,h-balkH-rand-4);
		resized = true;
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
		
		contentPane.setSize(contentPane.getSize().width, geefBenodigdeHoogte());
		contentPaneNep.setSize(contentPaneNep.getSize().width, geefBenodigdeHoogte());
		scrollPane.doLayout();
		//contentPaneNep.setSize(contentPaneNep.getSize().width, geefBenodigdeHoogte());
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
		scrollPane.setScrollPosition(0,contentPane.getSize().height-scrollPane.getSize().height+20); 
		contentPane.repaint();
	}
	
	public void removeSoft(Component c)
	{	contentPane.remove(c);
		scrollPane.setScrollPosition(0,contentPane.getSize().height-scrollPane.getSize().height+20); 
		contentPane.repaint();
	}
	
	public int geefBenodigdeHoogte()
	{	//int max = scrollPane.getSize().height-20;
		int max = 0;
		for(int i=0 ; i<contentPane.getComponentCount() ; i++)
		{	Component c = contentPane.getComponent(i);
			int h = c.getLocation().y + c.getSize().height + 50;
			//if(WiskOpdr.mobileVersion)h = h-10;
			if(h>max) max = h;
		}
		return max;
	}
		
	public void zetRandverhoging(boolean b)
	{	randVerhoging = b;
	}
	
	public void paint(Graphics g)
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
	
	public void update(Graphics g)
	{	paint(g);
	}
	public void paintBuffer(Graphics g)
	{	g.setColor(Color.white);
		g.fillRect(0,balkH,getSize().width-1, getSize().height-balkH);
		
		super.paint(g);
		
		g.setColor(new Color(210,210,210));
		g.fillRect(0,0,getSize().width, balkH);
		g.fillRect(0,0,rand,getSize().height);
		g.fillRect(getSize().width-rand-2,0,rand+2,getSize().height);
		g.fillRect(0,getSize().height-rand-2,getSize().width, rand+2);
		
		if(randVerhoging)
		{	g.setColor(Color.white);
			g.drawLine(1,1,getSize().width-1,1);
			g.drawLine(1,1,1,getSize().height-1);
			g.setColor(Color.gray.darker());
			g.drawLine(1,getSize().height-2,getSize().width-2,getSize().height-2);
			g.drawLine(0,getSize().height-1,getSize().width-1,getSize().height-1);
			g.drawLine(getSize().width-1,0,getSize().width-1,getSize().height-1);
			g.drawLine(getSize().width-2,1,getSize().width-2,getSize().height-2);
		}
		g.setColor(Color.black);
		//g.drawRect(0,0,getSize().width-1, getSize().height-1);
		//g.drawRect(0,23,getSize().width-1, getSize().height-24);
		
		if(formMode)
		{	/*if(!WiskOpdr.mobileVersion)
			{	g.setColor(Color.lightGray.darker());
				g.drawLine(44,2,44,21);
				g.drawLine(70,2,70,21);
				g.drawLine(96,2,96,21);
				g.drawLine(122,2,122,21);
				g.drawLine(148,2,148,21);
				g.drawLine(174,2,174,21);
				g.setColor(Color.lightGray.brighter());
				g.drawLine(45,2,45,21);
				g.drawLine(71,2,71,21);
				g.drawLine(97,2,97,21);
				g.drawLine(123,2,123,21);
				g.drawLine(149,2,149,21);
				g.drawLine(175,2,175,21);
			}
            if(WiskOpdr.deployVariant!=null && WiskOpdr.deployVariant.equals("GR")) {
                g.setColor(Color.lightGray.darker());
                g.drawLine(385,2,385,21);
                g.setColor(Color.lightGray.brighter());
                g.drawLine(386,2,386,21);
            }*/
		}
		
		
		g.setColor(Color.gray.darker());
		g.drawLine(rand,balkH,getSize().width-rand-1,balkH);
		g.drawLine(rand,balkH,rand,getSize().height-rand-1);
		g.drawLine(rand+1,balkH+1,getSize().width-rand-3,balkH+1);
		g.drawLine(rand+1,balkH+1,rand+1,getSize().height-rand-2);
		g.setColor(Color.white);
		g.drawLine(rand+1,getSize().height-rand-1,getSize().width-2*rand-1,getSize().height-rand-1);
		g.drawLine(getSize().width-rand-1,balkH,getSize().width-rand-1,getSize().height-rand-1);
		
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
		else if(e.getSource()==ndelogKnop)
		{	if(formuleVak!=null)formuleVak.zetNdeLogVak();
		}
		else if(e.getSource()==tabletButton)
		{	((TabletOwner)getParent().getParent()).addTablet(this,getLocation().x+20, getLocation().y+getSize().height-120);
		}
	}
	
	public boolean isFocusTraversable()
	{	return true;
	}
	
	public void mousePressed(MouseEvent e)
	{	if(formuleVak!=null)formuleVak.requestFocus();
		if(formuleVak!=null)formuleVak.setSelected(false);
		if(e.getX() < formuleVak.getLocation().x)
		{	formuleVak.kind1.zetOpBegin();
		}
		if(e.getX() > formuleVak.getLocation().x + formuleVak.getSize().width)
		{	formuleVak.kind1.zetOpEind();
		}
	}
	public void mouseReleased(MouseEvent e){;}
	public void mouseExited(MouseEvent e){;}
	public void mouseClicked(MouseEvent e){;}
	public void mouseEntered(MouseEvent e)
	{	//if(formuleVak!=null)formuleVak.requestFocus();
	}
	
	public void adjustmentValueChanged(AdjustmentEvent e)
	{	int h = e.getValue();
		contentPane.setLocation(rand,25-h);
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
