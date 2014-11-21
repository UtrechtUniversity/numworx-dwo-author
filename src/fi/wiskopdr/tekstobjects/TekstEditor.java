package fi.wiskopdr.tekstobjects;

import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Hashtable;
import java.util.Vector;
import javax.swing.*;
import javax.swing.border.*;

import fi.wiskopdr.InteractiePanelContainerIF;
import fi.wiskopdr.TekstEditorEditPanel;
import fi.wiskopdr.VariableCollection;
import fi.wiskopdr.WiskOpdr;
import fi.wiskopdr.formuleobjects.*;
import fi.beans.wiskopdrbeans.InteractieEditPanel;
import fi.beans.wiskopdrbeans.InteractiePanel;

public class TekstEditor extends JLayeredPane implements TabletOwner, InteractiePanel, ActionListener, MouseListener, AdjustmentListener, FormuleVakHouder
{	
	private boolean resized;
	
	private FormuleButton formuleKnop,  antwoordVakKnop, tekstVakKnop, grafiekKnop, appletKnop, linkKnop, plaatjeKnop, grafiekToolKnop, geogebraKnop;
	private FormuleButton tabletButton, wortelKnop, machtKnop, breukKnop, kwadraatKnop, ndewortelKnop, ndelogKnop,integraalKnop, prvKnop, haakjesKnop, absKnop, rmKnop;
	private FormuleButton cbookKnop, cindyKnop, eslateKnop, epsilonKnop;
	public FormuleButton crosswidgetKnop;
	protected TekstVak tekstVak, tekstVakActief;
	protected FormuleVak formuleVak;
	private boolean actief;
	private JScrollPane scrollPane;
	private boolean scrollbar;
	private EditorContentPanel contentPane; 
	private boolean formMode = false;
	private boolean rekenTool;
	private boolean grafTool;
	
	private int balkH = 23;
	private int rand = 10;
	private int sparing = 3;
	
	private FormuleButton resizeButton;
	private boolean resizeable;
	private boolean enlarged;
	public static final int defaultEnlargedWidth = 500;
	public static final int defaultEnlargedHeigth = 500;
	private int enlargedWidth = defaultEnlargedWidth;
	private int enlargedHeight = defaultEnlargedHeigth;
	
	private JPanel basisPanel;
	private JPanel headerPanel;
	
	private boolean tabletAan;
    private boolean headerAan = true;
    private boolean scrollHorizontal;
    private boolean crossWidgetOption = false;
    
    private Tablet tablet;
    private FormuleVakHouder tabletUser;
    private boolean tabletAdded;
    
    private boolean studentEditor;
	
    public TekstEditor()
	{	this(true,true, true);
	} 

    public TekstEditor(boolean scrollbar, boolean form)
	{	this(scrollbar, form, new TekstVak());
	}
   
	public TekstEditor(boolean scrollbar, boolean form, boolean beperkt, TekstVak tekstVak)
	{	this(scrollbar, form, tekstVak);
		studentEditor = beperkt;
	    if(beperkt)
	    {  	headerPanel.remove(linkKnop);
		    headerPanel.remove(grafiekKnop);
	    	headerPanel.remove(plaatjeKnop);
		    headerPanel.remove(antwoordVakKnop);
		    headerPanel.remove(appletKnop);
		    headerPanel.remove(tekstVakKnop);
		    headerPanel.remove(geogebraKnop);
// cbookKnop, cindyKnop, eslateKnop, epsilonKnop
		    headerPanel.remove(cbookKnop);
		    headerPanel.remove(cindyKnop);
		    headerPanel.remove(eslateKnop);
		    headerPanel.remove(epsilonKnop);
        }
    }
	public TekstEditor(boolean scrollbar, boolean form, boolean beperkt)
	{
		this(scrollbar,form, beperkt,new TekstVak());
	}
	
	public TekstEditor(boolean scrollbar, boolean form, TekstVak tekstVak)
	{	setLayout(null);
		setBackground(new Color(210,210,210));
		setOpaque(false);
		
		basisPanel = new JPanel();
		basisPanel.setLayout(new BorderLayout());
		basisPanel.setBackground(new Color(210,210,210));
		super.add(basisPanel);
		
		headerPanel = new JPanel(){
			public void paintComponent(Graphics g)
			{
				if("MW".equals(WiskOpdr.deployVariant))super.paintComponent(g);
				else
					for(int i=0 ; i<10 ; i++)
					{
						g.setColor(new Color(200+5*i,200+5*i,200+5*i));
						g.fillRect(0,getHeight()-(i+1)*getHeight()/10, getWidth(),getHeight()/10+1);
					}
			}
		};
		headerPanel.setLayout(null);
		headerPanel.setBackground(new Color(210,210,210));
		headerPanel.setBorder(BorderFactory.createLineBorder(Color.lightGray));
		super.add(headerPanel);
		
		contentPane = new EditorContentPanel(this);
		contentPane.setLayout(null);
		contentPane.setBackground(Color.white);
		contentPane.addMouseListener(this);
		
		
		this.scrollbar = scrollbar;
		if(scrollbar)scrollPane = new JScrollPane(contentPane,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		else scrollPane = new JScrollPane(contentPane,JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setBackground(Color.white);
		scrollPane.setBorder(BorderFactory.createLineBorder(Color.gray));
		basisPanel.add(scrollPane);
		
		//scrollPane.setColumnHeaderView(new JPanel());
		
		tabletButton = new FormuleButton("meer");
		tabletButton.setBounds(142,2,36,20);
		tabletButton.addActionListener(this);
		tabletButton.setVisible(false);
		headerPanel.add(tabletButton);
		
		resizeButton = new FormuleButton("resize");
		resizeButton.setBounds(2,2,16,16);
		resizeButton.addActionListener(this);
		headerPanel.add(resizeButton);
		resizeButton.setVisible(false);
		
		formuleKnop = new FormuleButton("formule");
		formuleKnop.setBounds(12,2,20,20);
		formuleKnop.addActionListener(this);
		if(form)headerPanel.add(formuleKnop);
		
		grafiekKnop = new FormuleButton("grafiekcomponent");
		grafiekKnop.setBounds(38,2,20,20);
		grafiekKnop.addActionListener(this);
		if(form)headerPanel.add(grafiekKnop);
		
		linkKnop = new FormuleButton("link");
		linkKnop.setBounds(64,2,20,20);
		linkKnop.addActionListener(this);
		if(form)headerPanel.add(linkKnop);
		
		plaatjeKnop = new FormuleButton("image");
		plaatjeKnop.setBounds(64+26,2,20,20);
		plaatjeKnop.addActionListener(this);
		if(form)headerPanel.add(plaatjeKnop);
		
		antwoordVakKnop = new FormuleButton("antwoordvak");
		antwoordVakKnop.setBounds(64+26+26,2,20,20);
		antwoordVakKnop.addActionListener(this);
		if(form)headerPanel.add(antwoordVakKnop);
		
		tekstVakKnop = new FormuleButton("tekstvak");
		tekstVakKnop.setBounds(64+26+26+26+26+26,2,20,20);
		tekstVakKnop.addActionListener(this);
		if(form)headerPanel.add(tekstVakKnop);
		
		rmKnop = new FormuleButton("rmvak");
		rmKnop.setBounds(38,2,20,20);
		rmKnop.addActionListener(this);
		rmKnop.setVisible(false);
		headerPanel.add(rmKnop);
		
		grafiekToolKnop = new FormuleButton("grafiektool");
		grafiekToolKnop.setBounds(64,2,20,20);
		grafiekToolKnop.addActionListener(this);
		grafiekToolKnop.setVisible(false);
		headerPanel.add(grafiekToolKnop);
		
		geogebraKnop = new FormuleButton("geogebra");
		geogebraKnop.setBounds(64+26+26+26+26,2,20,20);
		geogebraKnop.addActionListener(this);
		if(form)headerPanel.add(geogebraKnop);
		
		crosswidgetKnop = new FormuleButton("crosswidget");
		crosswidgetKnop.setBounds(64+26+26+26+26+26+26,2,20,20);
		crosswidgetKnop.setToggle(true);
		crosswidgetKnop.addActionListener(this);
		crosswidgetKnop.setVisible(false);
		if(form)headerPanel.add(crosswidgetKnop);
		
		cbookKnop = new FormuleButton("cbook");
		cbookKnop.setBounds(64+26+26+26+26+26+26+26,2,20,20);
		cbookKnop.addActionListener(this);
		//if(form)headerPanel.add(cbookKnop);
		
		cindyKnop = new FormuleButton("cindy");
		cindyKnop.setBounds(64+26+26+26+26+26+26+26+26,2,20,20);
		cindyKnop.addActionListener(this);
		//if(form)headerPanel.add(cindyKnop);
		
		eslateKnop = new FormuleButton("eslate");
		eslateKnop.setBounds(64+26+26+26+26+26+26+26+26+26,2,20,20);
		eslateKnop.addActionListener(this);
		//if(form)headerPanel.add(eslateKnop);

		epsilonKnop = new FormuleButton("epsilonwriter");
		epsilonKnop.setBounds(64+26+26+26+26+26+26+26+26+26+26,2,20,20);
		epsilonKnop.addActionListener(this);
		//if(form)headerPanel.add(epsilonKnop);

		appletKnop = new FormuleButton("interactiecomponent");
		appletKnop.setBounds(64+26+26+26,2,20,20);
		appletKnop.addActionListener(this);
		if(form)headerPanel.add(appletKnop);
		
		wortelKnop = new FormuleButton("wortel");
		wortelKnop.setBounds(12,2,20,20);
		wortelKnop.addActionListener(this);
		wortelKnop.setVisible(false);
		headerPanel.add(wortelKnop);
		
		machtKnop = new FormuleButton("macht");
		machtKnop.setBounds(38,2,20,20);
		machtKnop.addActionListener(this);
		machtKnop.setVisible(false);
		headerPanel.add(machtKnop);
		
		kwadraatKnop = new FormuleButton("kwadraat");
		kwadraatKnop.setBounds(64,2,20,20);
		kwadraatKnop.addActionListener(this);
		kwadraatKnop.setVisible(false);
		headerPanel.add(kwadraatKnop);
		
		breukKnop = new FormuleButton("breuk");
		breukKnop.setBounds(90,2,20,20);
		breukKnop.addActionListener(this);
		breukKnop.setVisible(false);
		headerPanel.add(breukKnop);
		
		haakjesKnop = new FormuleButton("haakjes");
		haakjesKnop.setBounds(116,2,20,20);
		haakjesKnop.addActionListener(this);
		haakjesKnop.setVisible(false);
		headerPanel.add(haakjesKnop);
		
		ndewortelKnop = new FormuleButton("ndewortel");
		ndewortelKnop.setBounds(142,2,20,20);
		ndewortelKnop.addActionListener(this);
		ndewortelKnop.setVisible(false);
		//headerPanel.add(ndewortelKnop);
		
		ndelogKnop = new FormuleButton("ndelog");
		ndelogKnop.setBounds(166,2,25,20);
		ndelogKnop.addActionListener(this);
		ndelogKnop.setVisible(false);
		//headerPanel.add(ndelogKnop);
		
		integraalKnop = new FormuleButton("integraal");
		integraalKnop.setBounds(195,2,20,20);
		integraalKnop.addActionListener(this);
		integraalKnop.setVisible(false);
		//headerPanel.add(integraalKnop);
		
		prvKnop = new FormuleButton("prv");
		prvKnop.setBounds(221,2,20,20);
		prvKnop.addActionListener(this);
		prvKnop.setVisible(false);
		//headerPanel.add(prvKnop);
		
		absKnop = new FormuleButton("abs");
		absKnop.setBounds(247,2,20,20);
		absKnop.addActionListener(this);
		absKnop.setVisible(false);
		//headerPanel.add(absKnop);
		
		
		
		this.tekstVak = tekstVak;
		tekstVak.setBackground(Color.white);
		tekstVak.setBounds(0,0, 240,40);
		tekstVak.addActionListener(this);
		//tekstVak.setBorder(BorderFactory.createLineBorder(Color.lightGray, 1));
		add(tekstVak);
		//tekstVak.requestFocus();
		
		tekstVakActief = tekstVak;
		
	}
	
	public void setCrossWidgetOption(boolean b)
	{
		crossWidgetOption = b;
		if(!formMode)crosswidgetKnop.setVisible(b);
	}
    
    public void setHeader(boolean b)
    {   headerAan = b;        
    }
	
    public void setScrollHorizontal(boolean b)
    {   scrollHorizontal = b;        
    }
	public FormuleVak geefFormuleVak()
	{	return formuleVak;
	}
	
	public void deleteStates()
	{	tekstVak.deleteStates();
	}
	
	public void setResizable(boolean b)
	{	resizeButton.setBounds(getSize().width-18,3,15,15);
		resizeButton.setVisible(b);
		enlarged = false;
	}
	
	public void setEnlargedWidth(int width)
	{   enlargedWidth = width;
	}
	
	public void setEnlargedHeight(int height)
    {   enlargedHeight = height;
    }
	
	public int getEnlargedWidth()
    {   return enlargedWidth;
    }
	
	public int getEnlargedHeight()
    {   return enlargedHeight;
    }
	
	public void setEnlargedSize()
    {   if(enlarged)setSize(enlargedWidth,enlargedHeight);
    }
    
    
	public void setFont(Font font)
	{	if(tekstVak==null)return;
		tekstVak.setFont(font);
	}
	
	public void destroy()
	{	remove(contentPane);
		contentPane = null;
		
	}
    
    public void setBounds(int x, int y, int b, int h)
    {   if(headerAan)
        {   basisPanel.setBounds(0,balkH+sparing,b,h-balkH-sparing);
            headerPanel.setBounds(0,0,b,balkH);
        }
        else
        {   
            basisPanel.setBounds(0,0,b,h);
            headerPanel.setBounds(0,0,0,0);
        }
        basisPanel.doLayout();
        super.setBounds(x,y,b,h);
        resizeButton.setBounds(getSize().width-18,3,15,15);
        tekstVak.setBounds(0,0,b-10,h-30);
        tekstVak.layoutTekst();
        resizeButton.setBounds(getSize().width-18,3,15,15);
        setNewScrollSize();
    }
	
    public void setSize(int b, int h)
    {   if(headerAan)
        {   basisPanel.setSize(b,h-balkH-sparing);
            headerPanel.setSize(b,balkH);
        }
        else
        {   
            basisPanel.setSize(b,h);
            headerPanel.setSize(0,0);
        }
        basisPanel.doLayout();
        super.setSize(b,h);
        resizeButton.setBounds(getSize().width-18,3,15,15);
        tekstVak.setSize(b-10,h-30);
        tekstVak.layoutTekst();
        resizeButton.setBounds(getSize().width-18,3,15,15);
        setNewScrollSize();
    }
	
	public String getText()
	{	return tekstVak.toString();
	}
	
	public String getCompleteText()
	{	return tekstVak.toCompleteString();
	}
	
	public Vector geefInteractiePanels()
	{	return tekstVak.geefInteractiePanels();
	}
	
	public void layoutTekst()
	{	tekstVak.layoutTekst();
	}
	
	public void zetTekst(String s)
	{	tekstVak.zetTekst(s);
	}
	
	public void zetOpBalk(Component c)
	{	super.add(c);
	}
	
	public void zetScrollBar()
	{	scrollPane.doLayout();
	}
	
	public Component add(Component c)
	{	Component comp = contentPane.add(c);
		setNewScrollSize();
		return comp;
	}
	
	public Component add(Component c,int n)
	{	Component comp = contentPane.add(c,n);
		setNewScrollSize();
		return comp;
	}
	
	public void remove(Component c)
	{	contentPane.remove(c);
		setNewScrollSize();
		contentPane.repaint();
	}
	
	public void setNewScrollSize()
	{	int max = 0; //scrollPane.getSize().height-20;
		for(int i=0 ; i<contentPane.getComponentCount() ; i++)
		{	Component c = contentPane.getComponent(i);
			int h = c.getLocation().y + c.getSize().height + 20;
			if(h>max) max = h;
		}
		contentPane.setPreferredSize(new Dimension(getSize().width, max));
		Rectangle r = tekstVak.geefActieveRegel().getBounds();
		contentPane.scrollRectToVisible(new Rectangle(r.x, r.y, r.width, r.height+20));//;
		contentPane.revalidate();
		contentPane.doLayout();
	}
	
		
	
	
	
	public TekstVak geefTekstVak()
	{	return tekstVak;
	}
	
	public void zetTabletAan(boolean b)
	{	tabletAan = b;
	}
		
	public void zetFormMode(boolean b)
	{	formMode = b;
		if(b)
		{	formuleKnop.setVisible(false);
			rmKnop.setVisible(false);
			grafiekToolKnop.setVisible(false);
			linkKnop.setVisible(false);
			plaatjeKnop.setVisible(false);
			grafiekKnop.setVisible(false);
			antwoordVakKnop.setVisible(false);
			tekstVakKnop.setVisible(false);
			geogebraKnop.setVisible(false);
			crosswidgetKnop.setVisible(false);
			cbookKnop.setVisible(false);
			cindyKnop.setVisible(false);
			eslateKnop.setVisible(false);
			epsilonKnop.setVisible(false);
			wortelKnop.setVisible(true);
			machtKnop.setVisible(true);
			kwadraatKnop.setVisible(true);
			tabletButton.setVisible(true);
			ndewortelKnop.setVisible(true);
			ndelogKnop.setVisible(true);
			integraalKnop.setVisible(true);
			prvKnop.setVisible(true);
			absKnop.setVisible(true);
			breukKnop.setVisible(true);
			haakjesKnop.setVisible(true);
			if(tabletAan)activateTablet();
		}
		else
		{	formuleKnop.setVisible(true);
			if(rekenTool)rmKnop.setVisible(true);
			if(grafTool)grafiekToolKnop.setVisible(true);
			linkKnop.setVisible(true);
			plaatjeKnop.setVisible(true);
			grafiekKnop.setVisible(true);
			antwoordVakKnop.setVisible(true);
			tekstVakKnop.setVisible(true);
			geogebraKnop.setVisible(true);
			if(crossWidgetOption)crosswidgetKnop.setVisible(true);
			cbookKnop.setVisible(true);
			cindyKnop.setVisible(true);
			eslateKnop.setVisible(true);
			epsilonKnop.setVisible(true);
			tabletButton.setVisible(false);
			wortelKnop.setVisible(false);
			machtKnop.setVisible(false);
			kwadraatKnop.setVisible(false);
			ndewortelKnop.setVisible(false);
			integraalKnop.setVisible(false);
			prvKnop.setVisible(false);
			absKnop.setVisible(false);
			ndelogKnop.setVisible(false);
			breukKnop.setVisible(false);
			haakjesKnop.setVisible(false);
			removeTablet();
		}
	}
	
	public void zetBalkZichtbaar(boolean b)
	{	setHeader(b);
		setBounds(getBounds());
		
	}
	
	public void zetRekenTool(boolean b)
	{	rmKnop.setVisible(b);
	}
	
	public void zetGrafTool(boolean b)
	{	grafiekToolKnop.setVisible(b);
	}
	
	public void zetFormuleKnop(boolean b)
	{
		
	}
	
	public void zetFormuleToolPopup(boolean b)
	{
		
	}
	
	public void setButton(boolean b)
	{
		
	}
	
	

	public void actionPerformed(ActionEvent e)
	{	if(e.getSource()==formuleKnop)
		{	zetFormMode(true);
			tekstVakActief.insertFormuleVak(0);
			formuleVak = tekstVakActief.geefFormuleVak();
		}
		else if(e.getSource()==rmKnop)
		{	zetFormMode(true);
			tekstVakActief.insertFormuleVak(1);
			formuleVak = tekstVakActief.geefFormuleVak();
		}
		else if(e.getSource()==grafiekKnop)
		{	tekstVakActief.insertTekstInteractiePanelVak(2);
		}
		else if(e.getSource()==grafiekToolKnop)
		{	if(studentEditor) tekstVakActief.insertStudentTekstInteractiePanelVak(8);
		}
		else if(e.getSource()==antwoordVakKnop)
		{	tekstVakActief.insertTekstInteractiePanelVak(0);
		}
		else if(e.getSource()==tekstVakKnop)
		{	tekstVakActief.insertTekstInteractiePanelVak(3);
		}
		else if(e.getSource()==linkKnop)
		{	tekstVakActief.insertLinkVak();
		}
		else if(e.getSource()==appletKnop)
		{	tekstVakActief.insertTekstInteractiePanelVak(1);
		}
		else if(e.getSource()==geogebraKnop)
		{	tekstVakActief.insertTekstInteractiePanelVak(4);
		}
		else if(e.getSource()==cbookKnop)
		{	tekstVakActief.insertTekstInteractiePanelVak(5);
		}
		else if(e.getSource()==cindyKnop)
		{	tekstVakActief.insertTekstInteractiePanelVak(6);
		}
		else if(e.getSource()==eslateKnop)
		{	tekstVakActief.insertTekstInteractiePanelVak(7);
		}
		else if(e.getSource()==epsilonKnop)
		{	tekstVakActief.insertTekstInteractiePanelVak(TekstInteractiePanelVak.EpsilonSetNr);
		}
		else if(e.getSource()==crosswidgetKnop)
		{	tekstVak.setCrossWidgetViewActief(crosswidgetKnop.isToggleAan());
		}
		else if(e.getSource()==wortelKnop)
		{	if(formuleVak!=null)
			{	formuleVak.zetWortelVak();
			}
		}
		else if(e.getSource()==machtKnop)
		{	if(formuleVak!=null)
			{	formuleVak.zetMachtVak();
			}
		}
		else if(e.getSource()==kwadraatKnop)
		{	if(formuleVak!=null)
			{	formuleVak.zetKwadraatVak();
			}
		}
		else if(e.getSource()==ndewortelKnop)
		{	if(formuleVak!=null)
			{	formuleVak.zetNdeWortelVak();
			}
		}
		else if(e.getSource()==ndelogKnop)
		{	if(formuleVak!=null)
			{	formuleVak.zetNdeLogVak();
			}
		}
		else if(e.getSource()==integraalKnop)
		{	if(formuleVak!=null)
			{	formuleVak.zetIntegraalVak();
			}
		}
		else if(e.getSource()==prvKnop)
		{	if(formuleVak!=null)
			{	formuleVak.zetPrvVak();
			}
		}
		else if(e.getSource()==breukKnop)
		{	if(formuleVak!=null)
			{	formuleVak.zetBreukVak();
			}
		}
		else if(e.getSource()==haakjesKnop)
		{	if(formuleVak!=null)
			{	formuleVak.zetHaakjesVak();
			}
		}
		else if(e.getSource()==absKnop)
		{	if(formuleVak!=null)
			{	formuleVak.zetAbsVak();
			}
		}
		
		/*else if(e.getSource()==tekstVak)
		{	if(e.getActionCommand().equals("formule"))
			{	zetFormMode(true);
				formuleVak = tekstVak.geefFormuleVak();
				//formuleVak.requestFocus();
			}
			else if(e.getActionCommand().equals("tekst"))
			{	zetFormMode(false);
				tekstVak.requestFocus();
			}
			else if(e.getActionCommand().equals("resize"))
			{	setNewScrollSize();
			}
		}*/
		else if(e.getSource()instanceof TekstVak)
		{	if(e.getActionCommand().equals("formule"))
			{	tekstVakActief = (TekstVak)e.getSource();
				zetFormMode(true);
				formuleVak = tekstVakActief.geefFormuleVak();
				//formuleVak.requestFocus();
			}
			else if(e.getActionCommand().equals("tekst"))
			{	tekstVakActief = (TekstVak)e.getSource();
				
				zetFormMode(false);
				tekstVakActief.requestFocus();
			}
			else if(e.getActionCommand().equals("resize"))
			{	setNewScrollSize();
			}
		}
		else if(e.getSource()==plaatjeKnop)
		{	tekstVakActief.insertImage();
		}
		else if(e.getSource()==tabletButton)
		{	activateTablet();
			tabletAan = true;
		}
		else if(e.getSource()==resizeButton)
		{	if(enlarged) 
			{	produceAction("verklein");
				enlarged = false;
			}
			else
			{	produceAction("vergroot");
				enlarged = true;
			}
		}
	}
	
	public void setEnlarged(boolean b)
	{	enlarged = b;
	}
	
	public boolean isEnlarged()
	{
		return enlarged;
	}
	
	public void activateTablet()
	{	Container parent = geefFormuleVak();
		int x = parent.getLocation().x;
		int y = parent.getLocation().y;
		int h = parent.getSize().height;
		for(int i=0 ; parent!=null && i<30 ; i++)
		{	if(parent instanceof TabletOwner) 
			{	((TabletOwner)parent).addTablet(this, x+20, y+40);
				break;
			}
			else if(parent!=null);
			{	parent = parent.getParent();
				if(parent==null)return;
				x += parent.getLocation().x;
				y += parent.getLocation().y;
			}
		}
	}
	
	
	
	
	public boolean isFocusTraversable()
	{	return true;
	}
	
	public void mousePressed(MouseEvent e)
	{	if(tekstVak!=null)tekstVak.setCaretPositionEnd();
		if(tekstVak!=null)tekstVak.requestFocus();
		if(tekstVak!=null)tekstVak.setSelected(false);
		
	}
	public void mouseReleased(MouseEvent e){;}
	public void mouseExited(MouseEvent e){;}
	public void mouseClicked(MouseEvent e){;}
	public void mouseEntered(MouseEvent e)
	{	//if(tekstVak!=null)tekstVak.requestFocus();
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
	
	public void zetOpdracht(Hashtable h, String[] randomVars, Hashtable randomValues)
	{
		
		String tekst = "";
		boolean balkZichtbaar = true;
		boolean rekenTool = false;
		boolean grafTool = false;
		boolean formuleKnop = true;
		boolean formuleToolPopup = true;
		Hashtable[] interactiePanelLaunchData = null;
		boolean boxMetRand = true;
		
				
		if(h.containsKey("tekst")) tekst = (String)h.get("tekst");
		if(h.containsKey("balkZichtbaar")) balkZichtbaar = ((Boolean)h.get("balkZichtbaar")).booleanValue();
		if(h.containsKey("rekenTool")) rekenTool = ((Boolean)h.get("rekenTool")).booleanValue();
		if(h.containsKey("grafTool")) grafTool = ((Boolean)h.get("grafTool")).booleanValue();
		if(h.containsKey("formuleKnop")) formuleKnop = ((Boolean)h.get("formuleKnop")).booleanValue();
		if(h.containsKey("formuleToolPopup")) formuleToolPopup = ((Boolean)h.get("formuleToolPopup")).booleanValue();
		if(h.containsKey("interactiePanelLaunchData")) interactiePanelLaunchData = (Hashtable[])h.get("interactiePanelLaunchData");
		if(h.containsKey("boxMetRand")) boxMetRand = ((Boolean)h.get("boxMetRand")).booleanValue();
		
		
		this.rekenTool = rekenTool;
		this.grafTool = grafTool;
		
		try         
        {   tekst = FormuleParser.randomizeTekstVakString(tekst, randomVars, randomValues);
        }
        catch(Exception e)
        {   tekst = "???";
        }
		zetTekst(tekst);
		zetBalkZichtbaar(balkZichtbaar);
		zetRekenTool(rekenTool);
		zetGrafTool(grafTool);
		zetFormuleKnop(formuleKnop);
		zetFormuleToolPopup(formuleToolPopup);
		zetMetRand(boxMetRand);
		
		if(interactiePanelLaunchData!=null)
		{	Vector v = geefInteractiePanels();
	        for(int i=0 ; i<v.size() ; i++)
	    	{	((InteractiePanelContainerIF)v.elementAt(i)).zetOpdracht(interactiePanelLaunchData[i], randomVars, randomValues);
	    	}	
	        tekstVak.layoutTekst();
		}  
       
		
	}
	
	public void zetMetRand(boolean b)
	{	Color c = b ? Color.gray : Color.white;
		scrollPane.setBorder(BorderFactory.createLineBorder(c));
	}
	
	public void setState(Hashtable h)
	{
		String tekst = "";
		Hashtable[] interactiePanelStates = null;
		Hashtable[] interactiePanelLaunchData = null;
		
		if(h.containsKey("tekst")) tekst = (String)h.get("tekst");
		if(h.containsKey("interactiePanelLaunchData")) interactiePanelLaunchData = (Hashtable[])h.get("interactiePanelLaunchData");
		if(h.containsKey("interactiePanelStates")) interactiePanelStates = (Hashtable[])h.get("interactiePanelStates");
		
		
		//if(antwoordVak.getText()==null || antwoordVak.getText().trim().equals("")) 
		zetTekst(tekst);
		Vector v = geefInteractiePanels();
	    for(int i=0 ; i<v.size() ; i++)
	    {  	InteractiePanelContainerIF ipc = ((InteractiePanelContainerIF)v.elementAt(i));
	    	ipc.setEditState(interactiePanelLaunchData[i]);
	    	ipc.setState(interactiePanelStates[i]);
	    	if(ipc instanceof TekstInteractiePanelVak)((TekstInteractiePanelVak)ipc).setEditMode(false);
	    	
	    }
	}
	
	public Hashtable getState()
	{	String tekst = "";
		Hashtable[] interactiePanelStates = null;
		Hashtable[] interactiePanelLaunchData = null;
		
		tekst = getText();
		
		Vector v = geefInteractiePanels();
		interactiePanelStates = new Hashtable[v.size()];
		interactiePanelLaunchData = new Hashtable[v.size()];
	    for(int i=0 ; i<v.size() ; i++)
		{	interactiePanelLaunchData [i] = ((InteractiePanelContainerIF)v.elementAt(i)).getEditState();
			interactiePanelStates [i] = ((InteractiePanelContainerIF)v.elementAt(i)).getState();
		}
	   
		Hashtable h = new Hashtable();
		h.put("tekst", tekst);
		h.put("interactiePanelStates", interactiePanelStates);
		h.put("interactiePanelLaunchData", interactiePanelLaunchData);
		
			
		return h;
	}
		
	public void setEditState(Hashtable h)
	{
		String tekst = "";
		boolean balkZichtbaar = true;
		boolean rekenTool = true;
		boolean grafTool = true;
		boolean formuleKnop = true;
		boolean formuleToolPopup = true;
		Hashtable[] interactiePanelLaunchData = null;
				
		if(h.containsKey("tekst")) tekst = (String)h.get("tekst");
		if(h.containsKey("balkZichtbaar")) balkZichtbaar = ((Boolean)h.get("balkZichtbaar")).booleanValue();
		if(h.containsKey("rekenTool")) rekenTool = ((Boolean)h.get("rekenTool")).booleanValue();
		if(h.containsKey("grafTool")) grafTool = ((Boolean)h.get("grafTool")).booleanValue();
		if(h.containsKey("formuleKnop")) formuleKnop = ((Boolean)h.get("formuleKnop")).booleanValue();
		if(h.containsKey("formuleToolPopup")) formuleToolPopup = ((Boolean)h.get("formuleToolPopup")).booleanValue();
		if(h.containsKey("interactiePanelLaunchData")) interactiePanelLaunchData = (Hashtable[])h.get("interactiePanelLaunchData");
		
		this.rekenTool = rekenTool;
		this.grafTool = grafTool;
		
		zetTekst(tekst);
		zetBalkZichtbaar(balkZichtbaar);
		zetRekenTool(rekenTool);
		zetGrafTool(grafTool);
		zetFormuleKnop(formuleKnop);
		zetFormuleToolPopup(formuleToolPopup);
		
		if(interactiePanelLaunchData!=null)
		{	Vector v = geefInteractiePanels();
	        for(int i=0 ; i<v.size() ; i++)
	    	{	InteractiePanelContainerIF ipc = (InteractiePanelContainerIF)v.elementAt(i);
	        	ipc.setEditState(interactiePanelLaunchData[i]);
	        	ipc.addActionListener(this);
	    		if(ipc instanceof TekstInteractiePanelVak)((TekstInteractiePanelVak)ipc).setEditMode(false);
	    	}	
	        tekstVak.layoutTekst();
		}
	}
	
	public Hashtable getEditState()
	{	String tekst = "";
		Hashtable[] interactiePanelLaunchData = null;
	
		//tekst = getText();
		tekst = getCompleteText();
		
		Vector v = geefInteractiePanels();
		interactiePanelLaunchData = new Hashtable[v.size()];
	    for(int i=0 ; i<v.size() ; i++)
		{	interactiePanelLaunchData [i] = ((InteractiePanelContainerIF)v.elementAt(i)).getEditState();
		}
	   
		Hashtable h = new Hashtable();
		h.put("tekst", tekst);
		h.put("interactiePanelLaunchData", interactiePanelLaunchData);
	
		return h;
	}
	
	/*public Hashtable getCompleteEditState()
	{	String tekst = "";
	
		tekst = getCompleteText();
	
		Hashtable h = new Hashtable();
		h.put("tekst", tekst);
	
		return h;
	}*/
	
	public InteractieEditPanel getEditPanel()
	{	return new TekstEditorEditPanel();
	}
		
	public void wis(){}
	
	public int geefAsHoogte(){return 0;}
	
	public int getIpId(){return 0;}
	
	public String getIpExpString(){return null;}
	
	public int getScore(){return 0;}
	
	public int[][] getScoreObjectives()
	{	return null;
	}
	
	public int getScoreMax(){return 0;}
	
	public boolean isCorrect(){return true;}
	
	public boolean isFout(){return false;}
	
	public void zetMode(int mode){}
	
	public void zetNagekeken(boolean b){}
	
    public void stop(){
    	tabletAan = false;
    	Vector v = geefInteractiePanels();
        for(int i=0 ; i<v.size() ; i++)
    	{	((InteractiePanelContainerIF)v.elementAt(i)).stop();
    	}
    }
    
    public void zetMaat(){}
	
 public void start(){}
     
    public void opnieuw(){}
    
    public void kijkNa(){}
    
    public void kijkNa(int stapNr){}
    
    public boolean isPopup()
	{	if(getParent()!=null 
			&& getParent().getParent()!=null 
			&& getParent().getParent().getParent()!=null 
			&& getParent().getParent().getParent().getParent() instanceof JDialog) 
		return true;
		return false;
	}
	
	// methoden TabletOwner
	
	public void zetTabletUser(FormuleVakHouder formuleVakHouder)
	{	if(!isPopup())
		{	Container parent = getParent();
			for(int i=0 ; parent!=null && i<40 ; i++)
			{	if(parent instanceof TabletOwner) 
				{	((TabletOwner)parent).zetTabletUser(formuleVakHouder);
					break;
				}
				else 
				{	parent = parent.getParent();
				}
			}
		}
		if(tablet==null) return;
		tablet.zetFormuleVakHouder(formuleVakHouder);
		tabletUser = formuleVakHouder;
	}
	
	public void zetTablet(FormuleVakHouder formuleVakHouder, int x, int y)
	{	if(!isPopup())
		{	Container parent = getParent();
			for(int i=0 ; parent!=null && i<40 ; i++)
			{	if(parent instanceof TabletOwner) 
				{	((TabletOwner)parent).zetTablet(formuleVakHouder, x, y);
					break;
				}
				else 
				{	parent = parent.getParent();
				}
			}
			return;
		}
		if(tablet==null) 
		{	tablet = new Tablet(formuleVakHouder);
			tablet.setLocation(x,y);
		}
		tablet.zetFormuleVakHouder(formuleVakHouder);
		tabletUser = formuleVakHouder;
	}
	
	public void addTablet(FormuleVakHouder formuleVakHouder, int xx, int yy)
	{	if(!isPopup())
		{	Container parent = getParent();
			int x = xx + getLocation().x;
			int y = yy + getLocation().y;
			int h = parent.getSize().height;
			for(int i=0 ; parent!=null && i<40 ; i++)
			{	if(parent instanceof TabletOwner) 
				{	((TabletOwner)parent).addTablet(formuleVakHouder, x, y);
					break;
				}
				else 
				{
					x += parent.getLocation().x;
					y += parent.getLocation().y;
					parent = parent.getParent();
					if(parent==null)return;
				}
			}
			return;
		}
		if(tablet==null) 
		{	tablet = new Tablet(formuleVakHouder);
		}
		if(!tabletAdded)
		{	this.setLayer(tablet, JLayeredPane.PALETTE_LAYER.intValue());
			super.add(tablet,0);
			
			//add(tablet,0);
			tablet.setLocation(xx,yy);
			tabletAdded = true;
            repaint();
		}
		tablet.zetFormuleVakHouder(formuleVakHouder);
	}
	
	public void removeTablet()
	{	if(!isPopup())
		{	Container parent = getParent();
			for(int i=0 ; parent!=null && i<30 ; i++)
			{	if(parent instanceof TabletOwner) 
				{	((TabletOwner)parent).removeTablet();
					break;
				}
				else 
				{	parent = parent.getParent();
				}
			}
			return;
		}
		if(tablet==null)return;
	    super.remove(tablet);
	    repaint();
		tabletAdded = false;
	}
	
	
	public Tablet getTablet()
	{	return tablet;
	}
	
	// einde methode TabletOwner
	
	
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
