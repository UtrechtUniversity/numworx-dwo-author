package fi.wiskopdr.formuleobjects;
					   
import java.applet.*;
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.*;

import fi.beans.wnwidgets.MWScrollBarUI;
import fi.wiskopdr.WiskOpdr;
import fi.wiskopdr.expressies.*;

public class FormuleEditor extends JLayeredPane implements TabletOwner, ActionListener, MouseListener, AdjustmentListener, FormuleVakHouder
{	
		
	protected FormuleButton wortelKnop, machtKnop, kwadraatKnop, breukKnop, haakjesKnop, ndewortelKnop, ndelogKnop, integraalKnop, prvKnop, absKnop;
	protected FormuleButton subscriptKnop, enkelePijlKnop, dubbelePijlKnop;
	protected FormuleButton tabletButton;
	public FormuleVak formuleVak;
	private boolean actief;
	private JScrollPane scrollPane;
	private boolean scrollbar;
	private EditorContentPanel contentPane; 
	private boolean formMode = true;
	private boolean multiLine = false;
	
	private int balkH = 23;
	private int rand = 10;
	private int sparing = -1;
	
	private boolean randVerhoging = true;
	private boolean geenAntwoord;
	private boolean randomFout;
	
	private FormuleButton resizeButton;
	private boolean resizeable;
	private boolean enlarged;
	
	protected JPanel basisPanel;
	protected JPanel headerPanel;
	
	private boolean tabletAan;
	private boolean headerAan = true;
    private boolean scrollHorizontal;
    
    private Tablet tablet;
    private FormuleVakHouder tabletUser;
    private boolean tabletAdded;
    
    protected FormuleButton nieuweRegelKnop;
    protected FormuleButton verwijderRegelKnop;
	private FormuleVak[] formuleRegels;
	private int aantalFormuleRegels;
	private int maxAantalFormuleRegels = 50;
	
	boolean grafiekOfEdit;
    
	
	
	public FormuleEditor(boolean scrollbar)
	{	setLayout(null);
		setBackground(new Color(210,210,210));
		
		
		basisPanel = new JPanel();
		basisPanel.setLayout(new BorderLayout());
		basisPanel.setBackground(new Color(210,210,210));
		super.add(basisPanel);
		
		headerPanel = new JPanel(){
//			public void paintComponent(Graphics g)
//			{
//				if("MW".equals(WiskOpdr.deployVariant) || "GR".equals(WiskOpdr.deployVariant))
//				{	g.setColor(Color.white);
//					g.fillRect(0,0,getWidth(),getHeight());
//				
//				}
//				else
//					for(int i=0 ; i<10 ; i++)
//					{	g.setColor(new Color(200+5*i,200+5*i,200+5*i));
//						g.fillRect(0,getHeight() - (i+1)*getHeight()/10, getWidth(),getHeight()/10+1);
//					}
//				
//			}
		};
		headerPanel.setLayout(null);
		headerPanel.setBackground(new Color(221,222,225));
		headerPanel.setBorder(BorderFactory.createLineBorder(Color.lightGray));
		if("MW".equals(WiskOpdr.deployVariant) || "GR".equals(WiskOpdr.deployVariant))
		{	setBackground(Color.white);
			headerPanel.setBackground(Color.white);//(new Color(195,213,229));
			basisPanel.setBackground(Color.white);
			headerPanel.setBorder(BorderFactory.createEmptyBorder());
			
		}
		super.add(headerPanel);
		
		contentPane = new EditorContentPanel(this);
		contentPane.setLayout(null);
		contentPane.setBackground(Color.white);
		/*if("MW".equals(WiskOpdr.deployVariant))
		{
			contentPane.setBackground(new Color(240,240,240));
			JPanel redLine = new JPanel();
			redLine.setBounds(30,0,1,300);
			redLine.setBackground(Color.red);
			contentPane.add(redLine);
		}*/
		contentPane.addMouseListener(this);
		
		
		this.scrollbar = scrollbar;
		if(scrollbar)
		{
			if("MW".equals(WiskOpdr.deployVariant) || "GR".equals(WiskOpdr.deployVariant))scrollPane = new JScrollPane(contentPane,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
			else scrollPane = new JScrollPane(contentPane,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		}
		else scrollPane = new JScrollPane(contentPane,JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setBackground(Color.white);
		scrollPane.setBorder(BorderFactory.createLineBorder(Color.lightGray));
		if("MW".equals(WiskOpdr.deployVariant))// || "GR".equals(WiskOpdr.deployVariant)
		{	scrollPane.getVerticalScrollBar().setUI(new MWScrollBarUI());
			setScrollHorizontal(false);
			scrollPane.setBorder(BorderFactory.createLineBorder(new Color(210,210,210),2));
		}
		if("GR".equals(WiskOpdr.deployVariant))// || "GR".equals(WiskOpdr.deployVariant)
        {   setScrollHorizontal(false);
            scrollPane.setBorder(BorderFactory.createLineBorder(new Color(210,210,210),2));
        }
		basisPanel.add(scrollPane);
		scrollPane.getVerticalScrollBar().setSize(10,100);
		
		//tabletButton = new FormuleButton("tablet");
		//tabletButton.setBounds(2,2,16,16);
        tabletButton = new FormuleButton("meer", FormuleButton.MEERKNOP);
        tabletButton.setBounds(112,2,36,20);
		tabletButton.addActionListener(this);
		headerPanel.add(tabletButton);
		
		resizeButton = new FormuleButton("resize");
		resizeButton.setBounds(2,2,16,16);
		resizeButton.addActionListener(this);
		headerPanel.add(resizeButton);
		resizeButton.setVisible(false);
		
				
		wortelKnop = new FormuleButton("wortel");
		wortelKnop.setBounds(2,2,20,20);
		wortelKnop.addActionListener(this);
		headerPanel.add(wortelKnop);
		
		machtKnop = new FormuleButton("macht");
		machtKnop.setBounds(24,2,20,20);
		machtKnop.addActionListener(this);
		headerPanel.add(machtKnop);
		
		kwadraatKnop = new FormuleButton("kwadraat");
		kwadraatKnop.setBounds(46,2,20,20);
		kwadraatKnop.addActionListener(this);
		headerPanel.add(kwadraatKnop);
		
		breukKnop = new FormuleButton("breuk");
		breukKnop.setBounds(68,2,20,20);
		breukKnop.addActionListener(this);
		headerPanel.add(breukKnop);
		
		haakjesKnop = new FormuleButton("haakjes");
		haakjesKnop.setBounds(90,2,20,20);
		haakjesKnop.addActionListener(this);
		headerPanel.add(haakjesKnop);
		
		ndewortelKnop = new FormuleButton("ndewortel");
		ndewortelKnop.setBounds(152,2,20,20);
		ndewortelKnop.addActionListener(this);
		//headerPanel.add(ndewortelKnop);
		
		ndelogKnop = new FormuleButton("ndelog");
		ndelogKnop.setBounds(178,2,25,20);
		ndelogKnop.addActionListener(this);
		//headerPanel.add(ndelogKnop);
		
		integraalKnop = new FormuleButton("integraal");
		integraalKnop.setBounds(209,2,20,20);
		integraalKnop.addActionListener(this);
		
		prvKnop = new FormuleButton("prv");
		prvKnop.setBounds(235,2,20,20);
		prvKnop.addActionListener(this);
				
		formuleVak = new FormuleVak();
		formuleVak.setLocation(10,10);
		formuleVak.addActionListener(this);
		add(formuleVak);
		
		nieuweRegelKnop = new FormuleButton("gelijkwaardig", FormuleButton.NAVIGATIEKNOP);
		nieuweRegelKnop.setBounds(157,2,20,20);
		nieuweRegelKnop.addActionListener(this);
		headerPanel.add(nieuweRegelKnop);
		nieuweRegelKnop.setVisible(false);
		
		verwijderRegelKnop = new FormuleButton("terug",FormuleButton.NAVIGATIEKNOP);
		verwijderRegelKnop.setBounds(180,2,20,20);
		verwijderRegelKnop.addActionListener(this);
		headerPanel.add(verwijderRegelKnop);
		verwijderRegelKnop.setVisible(false);
		
		
		if("MW".equals(WiskOpdr.deployVariant))
		{	
			headerPanel.add(ndewortelKnop);
			
			haakjesKnop.setBounds(42,0,22,23);
			breukKnop.setBounds(66,0,22,23);
			kwadraatKnop.setBounds(90,0,22,23);
			machtKnop.setBounds(114,0,22,23);
			wortelKnop.setBounds(138,0,22,23);
			ndewortelKnop.setBounds(162,0,22,23);
			tabletButton.setBounds(186,0,36,23);
			
			nieuweRegelKnop.setBounds(160,2,21,22);
			verwijderRegelKnop.setBounds(180,2,21,22);
		}
		if("GR".equals(WiskOpdr.deployVariant))
		{	
			wortelKnop.setBounds(2,0,22,23);
			machtKnop.setBounds(26,0,22,23);
			kwadraatKnop.setBounds(50,0,22,23);
			breukKnop.setBounds(74,0,22,23);
			haakjesKnop.setBounds(98,0,22,23);
			tabletButton.setBounds(122,0,36,23);
			
			nieuweRegelKnop.setBounds(160,2,16,22);
			verwijderRegelKnop.setBounds(180,2,16,22);
		}
			
		
		
		
	}
	
	public void zetReactieVergelijkingMode()
	{
		headerPanel.remove(wortelKnop);
		headerPanel.remove(kwadraatKnop);
		headerPanel.remove(breukKnop);
		headerPanel.remove(haakjesKnop);
		headerPanel.remove(tabletButton);
		
		subscriptKnop = new FormuleButton("subscript");
		subscriptKnop.setBounds(2,2,20,20);
		subscriptKnop.addActionListener(this);
		headerPanel.add(subscriptKnop);

		enkelePijlKnop = new FormuleButton("\u2192");
		enkelePijlKnop.setBounds(46, 2, 20, 20);
		enkelePijlKnop.addActionListener(this);
		headerPanel.add(enkelePijlKnop);

		dubbelePijlKnop = new FormuleButton("\u21c4");//pijl andersom is u21c6. Als veranderd, dan ook bij actionlistener en in parser.
		dubbelePijlKnop.setBounds(68, 2, 20, 20);
		dubbelePijlKnop.addActionListener(this);
		headerPanel.add(dubbelePijlKnop);
		formuleVak.geefActieveRegel().zetReactieVergelijking(true);
	}
	
	public void zetGrafiekOfEdit(boolean b)
	{	
		grafiekOfEdit=b;
		contentPane.zetGrafiekOfEdit(b);
		nieuweRegelKnop.setVisible(b);
		verwijderRegelKnop.setVisible(b);
		if(("MW".equals(WiskOpdr.deployVariant) || "GR".equals(WiskOpdr.deployVariant))&& grafiekOfEdit)
		{	
			headerPanel.add(ndewortelKnop);
			headerPanel.remove(WiskOpdr.fiButtonPanel); 
			headerPanel.setBackground(new Color(240,240,240));
			
			haakjesKnop.setBounds(2,0,22,23);
			breukKnop.setBounds(26,0,22,23);
			kwadraatKnop.setBounds(50,0,22,23);
			machtKnop.setBounds(74,0,22,23);
			wortelKnop.setBounds(98,0,22,23);
			ndewortelKnop.setBounds(122,0,22,23);
			tabletButton.setBounds(146,0,36,23);
			
			haakjesKnop.setBackground(new Color(240,240,240));
			breukKnop.setBackground(new Color(240,240,240));
			kwadraatKnop.setBackground(new Color(240,240,240));
			machtKnop.setBackground(new Color(240,240,240));
			wortelKnop.setBackground(new Color(240,240,240));
			ndewortelKnop.setBackground(new Color(240,240,240));
			tabletButton.setBackground(new Color(240,240,240));
			
			
			nieuweRegelKnop.setBounds(160,0,21,23);
			if("GR".equals(WiskOpdr.deployVariant))nieuweRegelKnop.setBounds(172,0,16,23);
			verwijderRegelKnop.setBounds(180,0,21,23);
			if("GR".equals(WiskOpdr.deployVariant))verwijderRegelKnop.setBounds(186,0,16,23);
			
			scrollPane.setBorder(BorderFactory.createLineBorder(Color.gray,1));
		}
	}
	
	
	public void setMultiLine(boolean b)
	{	multiLine = b;
		nieuweRegelKnop.setLocation(getSize().width-(resizeable?63:43),2);
		verwijderRegelKnop.setLocation(getSize().width-(resizeable?40:20),2);
		nieuweRegelKnop.setVisible(b);
		verwijderRegelKnop.setVisible(b);
		if(b &&  formuleRegels == null)
		{	formuleRegels = new FormuleVak[maxAantalFormuleRegels];
			for(int i=0 ; i<maxAantalFormuleRegels ; i++)
			{
				formuleRegels[i] = new FormuleVak();
				formuleRegels[i].setLocation(10,20+i*40);
				formuleRegels[i].addActionListener(this);
				
			}
			add(formuleRegels[0]);
			remove(formuleVak);
			formuleVak = formuleRegels[0];
			aantalFormuleRegels = 1;
		}
	}
	
	public String[] geefRegels()
	{
		String[] regelStrings = new String[aantalFormuleRegels];
		for(int i=0 ; i<aantalFormuleRegels ; i++)
		{	regelStrings[i] = formuleRegels[i].toString();
		}
		return regelStrings;
	}
	
	public void zetRegels(String[] regelStrings)
	{	if(regelStrings==null) return;
		setMultiLine(true);
		aantalFormuleRegels = regelStrings.length;
		for(int i=0 ; i<aantalFormuleRegels ; i++)
		{	if(i>0)add(formuleRegels[i]);
			formuleRegels[i].vulVak(regelStrings[i]);
		}
		setNewScrollSize();
	}
	
	public void verwijderRegels()
	{	while(aantalFormuleRegels>1)
		{	remove(formuleRegels[aantalFormuleRegels-1]);
			formuleRegels[aantalFormuleRegels-1].vulVak("$f@");
			aantalFormuleRegels--;
		}
		formuleRegels[0].vulVak("$f@");
	}
    
    public void setHeader(boolean b)
    {   headerAan = b;        
    }
    
    public void setScrollHorizontal(boolean b)
    {   scrollHorizontal = b;        
    }
		
	public void setResizable(boolean b)
	{	resizeable = b;
		resizeButton.setBounds(getSize().width-18,3,15,15);
		resizeButton.setVisible(b);
		nieuweRegelKnop.setLocation(getSize().width-(resizeable?63:43),2);
		verwijderRegelKnop.setLocation(getSize().width-(resizeable?40:20),2);
		enlarged = false;
		
	}
	
	public void zetTabletAan(boolean b)
	{	tabletAan = b;
	}
	
	public void zetFormMode(boolean b)
	{	if(b)
		{	formMode = b;
			wortelKnop.setVisible(true);
			machtKnop.setVisible(true);
			kwadraatKnop.setVisible(true);
			ndewortelKnop.setVisible(true);
			ndelogKnop.setVisible(true);
			integraalKnop.setVisible(true);
			prvKnop.setVisible(true);
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
			integraalKnop.setVisible(false);
			prvKnop.setVisible(false);
			breukKnop.setVisible(false);
			haakjesKnop.setVisible(false);
		}
	}
	
	public void zetScrollOptie(boolean scrollbar)
	{	super.remove(scrollPane);
		this.scrollbar = scrollbar;
		if(scrollbar)scrollPane = new JScrollPane(contentPane,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		else scrollPane = new JScrollPane(contentPane,JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		
		scrollPane.setBackground(Color.white);
		scrollPane.setBorder(BorderFactory.createLineBorder(Color.gray));
		basisPanel.add(scrollPane);
		
		setBounds(getBounds().x, getBounds().y, getBounds().width,getBounds().height);
	}
	
	public void destroy()
	{	//remove(contentPane);
		//contentPane = null;
		
	}
	
	public void setBackgroundContentPane(Color c)
	{
		if(contentPane!=null)contentPane.setBackground(c);
	}
	public void setBounds(int x, int y, int b, int h)
	{	if(headerAan)
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
		
		nieuweRegelKnop.setLocation(getSize().width-(resizeable?63:43),2);
		verwijderRegelKnop.setLocation(getSize().width-(resizeable?40:20),2);
		//nieuweRegelKnop.setLocation(getSize().width-44,2);
		//verwijderRegelKnop.setLocation(getSize().width-21,2);
		if("MW".equals(WiskOpdr.deployVariant))
		{	nieuweRegelKnop.setLocation(getSize().width-44,0);
			verwijderRegelKnop.setLocation(getSize().width-21,0);
		}
		if("GR".equals(WiskOpdr.deployVariant))
		{	nieuweRegelKnop.setLocation(getSize().width-35,0);
			verwijderRegelKnop.setLocation(getSize().width-16,0);
		}
		setNewScrollSize();
		if("MW".equals(WiskOpdr.deployVariant)&&!grafiekOfEdit)
		{	WiskOpdr.fiButtonPanel.setLocation(5,-5);
			WiskOpdr.fiButtonPanel.setBackground(Color.white);
			headerPanel.add(WiskOpdr.fiButtonPanel); 
			WiskOpdr.fiButtonPanel.setVisible(true);
			nieuweRegelKnop.setLocation(getSize().width-44,0);
			verwijderRegelKnop.setLocation(getSize().width-21,0);
		}
		if("GR".equals(WiskOpdr.deployVariant)&&!grafiekOfEdit)
        {   nieuweRegelKnop.setLocation(getSize().width-44,0);
            verwijderRegelKnop.setLocation(getSize().width-15,0);
        }
	}
	 
	public void setSize(int b, int h)
	{	basisPanel.setSize(b,h-balkH-sparing);
		basisPanel.doLayout();
		headerPanel.setSize(b,balkH);
		super.setSize(b,h);
		resizeButton.setBounds(getSize().width-18,3,15,15);
		nieuweRegelKnop.setLocation(getSize().width-(resizeable?63:43),2);
		verwijderRegelKnop.setLocation(getSize().width-(resizeable?40:20),2);
		if("MW".equals(WiskOpdr.deployVariant) || "GR".equals(WiskOpdr.deployVariant))
		{	nieuweRegelKnop.setLocation(getSize().width-44,0);
			verwijderRegelKnop.setLocation(getSize().width-21,0);
		}
		setNewScrollSize();
		
	}
	
	public void zetOpRoot(Component c)
	{	super.add(c,0);
	}
	
	public void removeFromRoot(Component c)
	{	super.remove(c);
	}
	
	public void zetOpBalk(Component c)
	{	headerPanel.add(c,0);
	}
	
	public void zetScrollBar()
	{	scrollPane.doLayout();
	}
	
	public boolean getVerticalScrollBarVisible()
	{	
		return scrollPane.getVerticalScrollBar().isVisible();
		
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
	{	if(contentPane!=null)contentPane.remove(c);
        setNewScrollSize();
        repaint();
	}
    
//    public void zetMaat()
//    {   setNewScrollSize();
//        repaint();
//    }
    
    public void setNewScrollSize()
    {   
    	int maxX = 0; 
        int maxY = 0; 
        for(int i=0 ; i<contentPane.getComponentCount() ; i++)
        {   Component c = contentPane.getComponent(i);
            int b = c.getLocation().x + c.getSize().width;
            if(b>maxX) maxX = b;
            int h = c.getLocation().y + c.getSize().height + 20;
            if(h>maxY) maxY = h;
        }
        if(scrollHorizontal)
        {	contentPane.setPreferredSize(new Dimension(maxX,maxY));
        }
        else 
        {	contentPane.setPreferredSize(new Dimension(contentPane.getSize().width-20, maxY));
        }
        contentPane.scrollRectToVisible(new Rectangle(0,maxY-10, contentPane.getSize().width, maxY));
        contentPane.revalidate();
        contentPane.doLayout();
        
    }
	
	public void removeSoft(Component c)
	{	contentPane.remove(c);
        setNewScrollSize();
        repaint();
	}
	
	public void zetMetRand(boolean b)
	{	Color c = b ? Color.gray : Color.white;
		scrollPane.setBorder(BorderFactory.createLineBorder(c));
	}
	
	public void zetLinkerRand()
	{
		scrollPane.setBorder(BorderFactory.createMatteBorder(0, 1, 0, 0, Color.LIGHT_GRAY));
	}
		
	public void zetRandverhoging(boolean b)
	{	randVerhoging = b;
	}
	
	public void zetGeenAntwoord(boolean b)
	{ 	geenAntwoord = b;
	}
	public void zetRandomFout(boolean b)
	{ 	randomFout = b;
	}
	
	public FormuleVak geefFormuleVak()
	{	return formuleVak;
	}
	
	public void setEnlarged(boolean b)
	{	enlarged = b;
	}
	
	public void actionPerformed(ActionEvent e)
	{	if(e.getSource()==wortelKnop)
		{	if(formuleVak!=null && formuleVak.isEditable())formuleVak.zetWortelVak();
		}
		else if(e.getSource()==machtKnop)
		{	if(formuleVak!=null && formuleVak.isEditable())formuleVak.zetMachtVak();
		}
		else if(e.getSource()==kwadraatKnop)
		{	if(formuleVak!=null && formuleVak.isEditable())formuleVak.zetKwadraatVak();
		}
		else if(e.getSource()==breukKnop)
		{	if(formuleVak!=null && formuleVak.isEditable())formuleVak.zetBreukVak();
		}
		else if(e.getSource()==haakjesKnop)
		{	if(formuleVak!=null && formuleVak.isEditable())formuleVak.zetHaakjesVak();
		}
		else if(e.getSource()==ndewortelKnop)
		{	if(formuleVak!=null && formuleVak.isEditable())formuleVak.zetNdeWortelVak();
		}
		else if(e.getSource()==ndelogKnop)
		{	if(formuleVak!=null && formuleVak.isEditable())formuleVak.zetNdeLogVak();
		}
		else if(e.getSource()==integraalKnop)
		{	if(formuleVak!=null && formuleVak.isEditable())formuleVak.zetIntegraalVak();
		}
		else if(e.getSource()==prvKnop)
		{	if(formuleVak!=null && formuleVak.isEditable())
			{	formuleVak.zetPrvVak();
			}
		}
		else if(e.getSource()==absKnop)
		{	if(formuleVak!=null && formuleVak.isEditable())
			{	formuleVak.zetAbsVak();
			}
		}
		else if(e.getSource()==subscriptKnop)
		{	if(formuleVak!=null && formuleVak.isEditable())formuleVak.zetSubscriptVak();
		}
		else if(e.getSource() == enkelePijlKnop)
		{
			if(formuleVak != null && formuleVak.isEditable())formuleVak.insert(" \u2192 ");
		}
		else if(e.getSource() == dubbelePijlKnop)
		{
			if(formuleVak != null && formuleVak.isEditable())formuleVak.insert(" \u21c4 ");
		}
		else if(e.getSource()==tabletButton)
		{	activateTablet();
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
		
		else if(e.getSource()==formuleVak && e.getActionCommand().equals("focus")) zetTabletUser();
	
		else if(e.getSource()==nieuweRegelKnop && aantalFormuleRegels<maxAantalFormuleRegels)
		{	add(formuleRegels[aantalFormuleRegels],0);
			formuleRegels[aantalFormuleRegels].requestFocus();
			aantalFormuleRegels++;
			
		}
		else if(e.getSource()==verwijderRegelKnop && aantalFormuleRegels>1)
		{	remove(formuleRegels[aantalFormuleRegels-1]);
			aantalFormuleRegels--;
			
			
		}
		else if(e.getSource() instanceof FormuleVak && e.getActionCommand().equals("focus"))
		{	FormuleVak fv = (FormuleVak)e.getSource();
			if(fv != formuleVak) formuleVak = fv;
		}
	}
	
	/*public void activateTablet()
	{	if(getParent().getParent()instanceof TabletOwner)((TabletOwner)getParent().getParent()).addTablet(this,getLocation().x+20, getLocation().y+getSize().height-120);
		else if(getParent().getParent().getParent() instanceof TabletOwner)((TabletOwner)getParent().getParent().getParent()).addTablet(this,getLocation().x+20, getLocation().y+getSize().height-120);
		else if(getParent().getParent().getParent().getParent() instanceof TabletOwner)((TabletOwner)getParent().getParent().getParent().getParent()).addTablet(this,getLocation().x+20,getLocation().y+getSize().height-120);
		else if(getParent().getParent().getParent().getParent().getParent() instanceof TabletOwner)((TabletOwner)getParent().getParent().getParent().getParent().getParent()).addTablet(this,getLocation().x+20, getLocation().y+getSize().height-120);
		else if(getParent().getParent().getParent().getParent().getParent().getParent() instanceof TabletOwner)((TabletOwner)getParent().getParent().getParent().getParent().getParent().getParent()).addTablet(this,getLocation().x+20, getLocation().y+getSize().height-120);
	
	}*/
	
	
	
	public void activateTablet()
	{	Container parent = geefFormuleVak();
		if("MW".equals(WiskOpdr.deployVariant) || "GR".equals(WiskOpdr.deployVariant))parent = this;
		int x = parent.getLocation().x;
		int y = parent.getLocation().y;
		int h = parent.getSize().height;
		for(int i=0 ; parent!=null && i<40 ; i++)
		{	if(parent instanceof TabletOwner) 
			{	
				if("MW".equals(WiskOpdr.deployVariant) || "GR".equals(WiskOpdr.deployVariant))
				{
					((TabletOwner)parent).addTablet(this, x+getWidth()/2-120, y+getHeight()/2-80); 
				}
				else((TabletOwner)parent).addTablet(this, x+20, y+h+40);
				break;
			}
			else if(parent!=null);
			{	parent = parent.getParent();
			
				x += parent.getLocation().x;
				y += parent.getLocation().y;
			}
		}
		geefFormuleVak().requestFocus();
	}
	
	
	
	public void zetTabletUser()
	{	Container parent = this;
		for(int i=0 ; parent!=null && i<40 ; i++)
		{	if(parent instanceof TabletOwner) 
			{	((TabletOwner)parent).zetTabletUser(this);
				break;
			}
			else 
			{	parent = parent.getParent();
			}
		}
	}
	
	/**
	 * Retourneert true als het huidige window het hoogste niveau van de popup is.
	 * Ter info: als je op de meer-knop klikt van een graphtool in een popup, is isPopup() false, omdat
	 * de meer-knop op een child-panel staat van de popup-graphtool...
	 * 
	 * @return
	 */
	public boolean isPopup()
	{
		if(getParent()!=null 
			&& getParent().getParent()!=null 
			&& getParent().getParent().getParent()!=null 
			&& getParent().getParent().getParent().getParent() instanceof JDialog)

		return true;
		return false;
	}
	
	public JPanel getHeaderPanel()
	{
		return headerPanel;
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
	{	
		Container parent = getParent();
		int x = xx + parent.getLocation().x;
		int y = yy + parent.getLocation().y;
		int h = parent.getSize().height;
		for (int i = 0 ;parent != null && i < 40; i++)
		{	
			if (parent instanceof TabletOwner) 
			{	
				((TabletOwner) parent).addTablet(formuleVakHouder, x, y);
				break;
			}
			else 
			{	
				parent = parent.getParent();
				if (parent == null)
					return;
				x += parent.getLocation().x;
				y += parent.getLocation().y;
			}
		}
	}
	
	public void removeTablet()
	{	if(tablet==null)return;
        super.remove(tablet);
        repaint();
		tabletAdded = false;
		
	}
	
	public Tablet getTablet()
	{	return tablet;
	}
	
	// einde methode TabletOwner
	
	public boolean isFocusTraversable()
	{	return true;
	}
	
	public void mousePressed(MouseEvent e)
	{	//System.out.println("ja");
		if(formuleVak!=null)formuleVak.requestFocus();
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
