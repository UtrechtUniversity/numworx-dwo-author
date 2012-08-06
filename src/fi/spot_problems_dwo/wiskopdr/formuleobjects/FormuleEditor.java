package fi.spot_problems_dwo.wiskopdr.formuleobjects;
					   
import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

import fi.spot_problems_dwo.wiskopdr.expressies.*;
import fi.spot_problems_dwo.wiskopdr.tekstobjects.*;

public class FormuleEditor extends JLayeredPane implements  ActionListener, MouseListener, AdjustmentListener
{	
		
	protected FormuleButton wortelKnop, machtKnop, kwadraatKnop, breukKnop, haakjesKnop, ndewortelKnop, ndelogKnop, integraalKnop, prvKnop, absKnop;
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
	private int sparing = 3;
	
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
    
   private boolean tabletAdded;
    
    protected FormuleButton nieuweRegelKnop;
    protected FormuleButton verwijderRegelKnop;
	private FormuleVak[] formuleRegels;
	private int aantalFormuleRegels;
	private int maxAantalFormuleRegels = 20;
	
	boolean grafiekOfEdit;
    
	
	
	public FormuleEditor(boolean scrollbar)
	{	setLayout(null);
		setBackground(new Color(210,210,210));
		
		
		
		basisPanel = new JPanel();
		basisPanel.setLayout(new BorderLayout());
		basisPanel.setBackground(new Color(210,210,210));
		super.add(basisPanel);
		
		headerPanel = new JPanel(){
			public void paintComponent(Graphics g)
			{
				for(int i=0 ; i<10 ; i++)
					{	g.setColor(new Color(200+5*i,200+5*i,200+5*i));
						g.fillRect(0,getHeight() - (i+1)*getHeight()/10, getWidth(),getHeight()/10+1);
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
			scrollPane = new JScrollPane(contentPane,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		}
		else scrollPane = new JScrollPane(contentPane,JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setBackground(Color.white);
		scrollPane.setBorder(BorderFactory.createLineBorder(Color.gray));
		
		basisPanel.add(scrollPane);
		scrollPane.getVerticalScrollBar().setSize(10,100);
		
		//tabletButton = new FormuleButton("tablet");
		//tabletButton.setBounds(2,2,16,16);
        tabletButton = new FormuleButton("meer");
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
		
		nieuweRegelKnop = new FormuleButton("gelijkwaardig");
		nieuweRegelKnop.setBounds(157,2,20,20);
		nieuweRegelKnop.addActionListener(this);
		headerPanel.add(nieuweRegelKnop);
		nieuweRegelKnop.setVisible(false);
		
		verwijderRegelKnop = new FormuleButton("terug");
		verwijderRegelKnop.setBounds(180,2,20,20);
		verwijderRegelKnop.addActionListener(this);
		headerPanel.add(verwijderRegelKnop);
		verwijderRegelKnop.setVisible(false);
		
		
			
		
		
		
	}
	
	public void zetGrafiekOfEdit(boolean b)
	{	
		grafiekOfEdit=b;
		contentPane.zetGrafiekOfEdit(b);
		nieuweRegelKnop.setVisible(b);
		verwijderRegelKnop.setVisible(b);
		
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
		
		setNewScrollSize();
		
	}
	 
	public void setSize(int b, int h)
	{	basisPanel.setSize(b,h-balkH-sparing);
		basisPanel.doLayout();
		headerPanel.setSize(b,balkH);
		super.setSize(b,h);
		resizeButton.setBounds(getSize().width-18,3,15,15);
		nieuweRegelKnop.setLocation(getSize().width-(resizeable?63:43),2);
		verwijderRegelKnop.setLocation(getSize().width-(resizeable?40:20),2);
		
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
    
    //public void zetMaat()
    //{   setNewScrollSize();
    //    repaint();
    //}
    
    public void setNewScrollSize()
    {   int maxX = 0; 
        int maxY = 0; 
        for(int i=0 ; i<contentPane.getComponentCount() ; i++)
        {   Component c = contentPane.getComponent(i);
            int b = c.getLocation().x + c.getSize().width;
            if(b>maxX) maxX = b;
            int h = c.getLocation().y + c.getSize().height + 20;
            if(h>maxY) maxY = h;
        }
        if(scrollHorizontal)contentPane.setPreferredSize(new Dimension(maxX,maxY));
        else contentPane.setPreferredSize(new Dimension(contentPane.getSize().width-20, maxY));
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
	
	
	
	
	
	
	
	
	
	public boolean isPopup()
	{	if(getParent()!=null 
			&& getParent().getParent()!=null 
			&& getParent().getParent().getParent()!=null 
			&& getParent().getParent().getParent().getParent() instanceof JDialog) 
		return true;
		return false;
	}
	
	
	
	public boolean isFocusTraversable()
	{	return true;
	}
	
	public void mousePressed(MouseEvent e)
	{	//System.out.println("ja");
		if(formuleVak!=null)formuleVak.requestFocus();
		if(formuleVak!=null)formuleVak.setSelected(false);
		if(e.getX() < formuleVak.getLocation().x)
		{	//formuleVak.kind1.zetOpBegin();
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
