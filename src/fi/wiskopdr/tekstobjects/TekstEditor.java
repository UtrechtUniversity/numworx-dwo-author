package fi.wiskopdr.tekstobjects;

import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Vector;

import javax.swing.*;
import javax.swing.border.*;

import org.cbook.cbookif.CBookEvent;
import org.cbook.cbookif.CBookEventHandler;
import org.cbook.cbookif.CBookEventListener;

import fi.wiskopdr.InteractiePanelContainerIF;
import fi.wiskopdr.TekstEditorEditPanel;
import fi.wiskopdr.TekstVakPanel;
import fi.wiskopdr.VariableCollection;
import fi.wiskopdr.WiskOpdr;
import fi.wiskopdr.formuleobjects.*;
import fi.wiskopdr.opdrnav.OpdrNavStruct;
import fi.wiskopdr.templatecomponents.TComponentGenerator;
import fi.wiskopdr.templatecomponents.TComponentGeneratorFactory;
import fi.beans.wiskopdrbeans.CBookAware;
import fi.beans.wiskopdrbeans.InteractieEditPanel;
import fi.beans.wiskopdrbeans.InteractiePanel;

public class TekstEditor extends JLayeredPane implements TabletOwner, InteractiePanel, ActionListener, MouseListener, AdjustmentListener, FormuleVakHouder, CBookAware
{	
	private static final Integer _0 = Integer.valueOf(0);

	private boolean resized;
	
	private FormuleButton formuleKnop,  antwoordVakKnop, tekstVakKnop, grafiekKnop, appletKnop, linkKnop, plaatjeKnop, grafiekToolKnop, geogebraKnop;
	private FormuleButton tabletButton, wortelKnop, machtKnop, breukKnop, kwadraatKnop, ndewortelKnop, ndelogKnop,integraalKnop, prvKnop, haakjesKnop, absKnop, rmKnop;
	private FormuleButton cbookKnop, cindyKnop, eslateKnop, epsilonKnop;
	public FormuleButton crosswidgetKnop; 
	
	private JPopupMenu antwoordVakKnopJMenu;
	private JPopupMenu appletKnopJMenu;
	
	public FormuleButton templateChoiceKnop, componentChoiceKnop, standardComponentChoiceKnop;
	public JPopupMenu templateChoice, componentChoice, standardComponentChoice;
	JMenuItem[] templateItems;
	JMenuItem[] componentItems;
	String[] componentKeys;
	String[] templateKeys;
	JMenuItem[] standardComponentItems;
	
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
	private int sparing = -1;
	
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
    private boolean templateOption = false;
    private boolean standardComponentOption = false;
    
    private Tablet tablet;
    private FormuleVakHouder tabletUser;
    private boolean tabletAdded;
    
    private boolean studentEditor;
    private CBookEventHandler cbookEventHandler = new CBookEventHandler(this);	
	private JButton sendCommandButton;
	
	private boolean editable = true;
	
	private boolean logOption;
	private String logID;
	private Vector<String> attempts = new Vector<String>();
	
	private boolean toolbarLeft = false;
	private boolean mainEditor;

	
    public TekstEditor()
	{	this(true,true, true, new TekstVak());
	} 

	static final String LOGGING = "logOption";
    static class LoggingTekstVak extends TekstVak {

		private void fireLogging() {
    		if(editable)
    			produceAction(LOGGING);
    	}

		/* (non-Javadoc)
		 * @see fi.wiskopdr.tekstobjects.TekstVak#keyTyped(java.awt.event.KeyEvent)
		 */
		@Override
		public void keyTyped(KeyEvent e) {
			super.keyTyped(e);
			if ( e.getKeyChar() == KeyEvent.VK_ENTER)
				fireLogging();
		}

		@Override
		public void focusLost(FocusEvent e) {
			super.focusLost(e);
			fireLogging();
		}
    
    	
    	
    
    }
    
    
    
    
    
    public TekstEditor(boolean scrollbar, boolean form)
	{	this(scrollbar, form, new TekstVak(), false);
	}
   
	public TekstEditor(boolean scrollbar, boolean form, boolean beperkt, TekstVak tekstVak)
	{	this(scrollbar, form, tekstVak, false);
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
		this(scrollbar,form, beperkt,new LoggingTekstVak());
	}
	
	public TekstEditor( boolean scrollbar, boolean form, TekstVak tekstVak)
    {
	  this(scrollbar,form, new LoggingTekstVak(),false);
    }
	
	public TekstEditor( boolean scrollbar, boolean form, TekstVak tekstVak, boolean toolbarLeft)
	{	setLayout(null);
		setBackground(new Color(210,210,210));
		setOpaque(false);
		
		this.toolbarLeft = toolbarLeft;
		if(toolbarLeft)
		  balkH = 33;
	   
		
		basisPanel = new JPanel();
		basisPanel.setLayout(new BorderLayout());
		basisPanel.setBackground(new Color(210,210,210));
		super.add(basisPanel);
		
		headerPanel = new JPanel(){
//			public void paintComponent(Graphics g)
//			{
//				if("MW".equals(WiskOpdr.deployVariant))super.paintComponent(g);
//				else
//					for(int i=0 ; i<10 ; i++)
//					{
//						g.setColor(new Color(200+5*i,200+5*i,200+5*i));
//						g.fillRect(0,getHeight()-(i+1)*getHeight()/10, getWidth(),getHeight()/10+1);
//					}
//			}
		};
		headerPanel.setLayout(null);
		headerPanel.setBackground(WiskOpdr.colorGray2);
		headerPanel.setBorder(BorderFactory.createLineBorder(Color.lightGray));
		super.add(headerPanel);
		
		contentPane = new EditorContentPanel(this);
		contentPane.setLayout(null);
		contentPane.setBackground(Color.white);
		contentPane.addMouseListener(this);
		
		
		this.scrollbar = scrollbar;
		if(scrollbar)scrollPane = new JScrollPane(contentPane,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, (toolbarLeft?JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED:JScrollPane.HORIZONTAL_SCROLLBAR_NEVER));
		else scrollPane = new JScrollPane(contentPane,JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setBackground(Color.white);
		scrollPane.setBorder(BorderFactory.createLineBorder(Color.lightGray));
		basisPanel.add(scrollPane);
		
		//scrollPane.setColumnHeaderView(new JPanel());
		
		tabletButton = new FormuleButton("meer");
		tabletButton.setBounds(toolbarLeft,142,balkH/2-10,36,20);
		tabletButton.addActionListener(this);
		tabletButton.setVisible(false);
		headerPanel.add(tabletButton);
		
		resizeButton = new FormuleButton("resize");
		resizeButton.setBounds(toolbarLeft,2,balkH/2-10,16,16);
		resizeButton.addActionListener(this);
		headerPanel.add(resizeButton);
		resizeButton.setVisible(false);
		
		formuleKnop = new FormuleButton("formule");
		formuleKnop.setBounds(toolbarLeft,12,balkH/2-10,20,20);
		formuleKnop.addActionListener(this);
		if(form)headerPanel.add(formuleKnop);
		
		grafiekKnop = new FormuleButton("grafiekcomponent");
		grafiekKnop.setBounds(toolbarLeft,38,balkH/2-10,20,20);
		grafiekKnop.addActionListener(this);
		if(form)headerPanel.add(grafiekKnop);
		
		linkKnop = new FormuleButton("link");
		linkKnop.setBounds(toolbarLeft,64,balkH/2-10,20,20);
		linkKnop.addActionListener(this);
		if(form)headerPanel.add(linkKnop);
		
		plaatjeKnop = new FormuleButton("image");
		plaatjeKnop.setBounds(toolbarLeft,64+26,balkH/2-10,20,20);
		plaatjeKnop.addActionListener(this);
		if(form)headerPanel.add(plaatjeKnop);
		
		antwoordVakKnop = new FormuleButton("antwoordvak");
		antwoordVakKnop.setBounds(toolbarLeft,64+26+26,balkH/2-10,20,20);
		antwoordVakKnop.addActionListener(this);
		if(form)headerPanel.add(antwoordVakKnop);
		antwoordVakKnopJMenu = new JPopupMenu();
		
        TekstInteractiePanelVak.makeJMenu(this,"Antwoordtypes",antwoordVakKnopJMenu, 0);
		
		
		
		tekstVakKnop = new FormuleButton("tekstvak");
		tekstVakKnop.setBounds(toolbarLeft,64+26+26+26+26+26,balkH/2-10,20,20);
		tekstVakKnop.addActionListener(this);
		if(form)headerPanel.add(tekstVakKnop);
		
		rmKnop = new FormuleButton("rmvak");
		rmKnop.setBounds(toolbarLeft,38,balkH/2-10,20,20);
		rmKnop.addActionListener(this);
		rmKnop.setVisible(false);
		headerPanel.add(rmKnop);
		
		grafiekToolKnop = new FormuleButton("grafiektool");
		grafiekToolKnop.setBounds(toolbarLeft,64,balkH/2-10,20,20);
		grafiekToolKnop.addActionListener(this);
		grafiekToolKnop.setVisible(false);
		headerPanel.add(grafiekToolKnop);
		
		geogebraKnop = new FormuleButton("geogebra");
		geogebraKnop.setBounds(toolbarLeft,64+26+26+26+26,balkH/2-10,20,20);
		geogebraKnop.addActionListener(this);
		if(form)headerPanel.add(geogebraKnop);
		
		crosswidgetKnop = new FormuleButton("crosswidget");
		crosswidgetKnop.setBounds(toolbarLeft,64+26+26+26+26+26+26,balkH/2-10,20,20);
		crosswidgetKnop.setToggle(true);
		crosswidgetKnop.addActionListener(this);
		crosswidgetKnop.setVisible(false);
		if(form)headerPanel.add(crosswidgetKnop);
		
		
		standardComponentChoiceKnop = new FormuleButton("sknop");
		standardComponentChoiceKnop.setBounds(toolbarLeft,64+26+26+26+26+26+26+26,balkH/2-10,22,22);
		standardComponentChoiceKnop.addActionListener(this);
		standardComponentChoiceKnop.setVisible(false);
		if(form)headerPanel.add(standardComponentChoiceKnop);
		
		templateChoiceKnop = new FormuleButton("tknop");
		templateChoiceKnop.setBounds(toolbarLeft,64+26+26+26+26+26+26+26+26,balkH/2-10,22,22);
		templateChoiceKnop.addActionListener(this);
		templateChoiceKnop.setVisible(false);
		if(form)headerPanel.add(templateChoiceKnop);
		
		componentChoiceKnop = new FormuleButton("cknop");
		componentChoiceKnop.setBounds(toolbarLeft,64+26+26+26+26+26+26+26+26+26,balkH/2-10,22,22);
		componentChoiceKnop.addActionListener(this);
		componentChoiceKnop.setVisible(false);
		if(form)headerPanel.add(componentChoiceKnop);
		
		templateChoice = new JPopupMenu();
		componentChoice = new JPopupMenu();
		standardComponentChoice = new JPopupMenu();
		
		if(TekstVakPanel.templatePages != null) {
			templateItems = new JMenuItem[TekstVakPanel.templatePages.keySet().size()];
			templateKeys = new String[TekstVakPanel.templatePages.keySet().size()];
			//System.out.println("templatePages:"+TekstVakPanel.templatePages.keySet().size());
			int teller = 0;
			//for (String key : TekstVakPanel.templatePages.keySet()) 
			for (int i=0 ; i<TekstVakPanel.templatePagesKeys.size() ; i++) 
			{	String key = TekstVakPanel.templatePagesKeys.get(i);
				templateItems[teller] = new JMenuItem(key.substring(8));
				templateItems[teller].addActionListener(this);
				templateKeys[teller] = "DWOTEMP_"+templateItems[teller].getText();
				templateChoice.add(templateItems[teller]);
				teller++;
			}
		}
		/*
		ArrayList<ArrayList<String>> componentList = new ArrayList<ArrayList<String>>();
		ArrayList<String> subMenuList = new ArrayList<String>();
		ArrayList<String> componentItemList = new ArrayList<String>();
		if(TekstVakPanel.templateComponents != null) {
			componentItems = new JMenuItem[TekstVakPanel.templateComponents.keySet().size()];
			componentKeys = new String[TekstVakPanel.templateComponents.keySet().size()];
			for (int i=0 ; i<TekstVakPanel.templateComponentsKeys.size() ; i++) 
			{	
				String key = TekstVakPanel.templateComponentsKeys.get(i);
				String[] parts = key.split("_");
		
				if(parts.length==2) {
					componentItemList.add(parts[1]);
				}
				else if(parts.length==3) {
					if(subMenuList.contains(parts[1])) {
						componentList.get(subMenuList.indexOf(parts[1])).add(parts[2]);
					}
					else {
						subMenuList.add(parts[1]);
						ArrayList<String> subMenu = new ArrayList<String>();
						subMenu.add(parts[2]);
						componentList.add(subMenu);
					}
				}
			}
			int teller = 0;
			for (int i=0 ; i<componentItemList.size() ; i++) {
				componentItems[teller] = new JMenuItem(componentItemList.get(i));
				componentItems[teller].addActionListener(this);
				componentKeys[teller] = "DWOCOMP_"+componentItems[teller].getText();
				componentChoice.add(componentItems[teller]);
				teller++;
			}
			componentChoice.addSeparator();
			for (int i=0 ; i<componentList.size() ; i++) {
				JMenu subMenu = new JMenu(subMenuList.get(i));
				for (int j=0 ; j<componentList.get(i).size() ; j++) {
					JMenuItem subMenutem = new JMenuItem(componentList.get(i).get(j));
					subMenu.add(subMenutem);
					componentItems[teller] = subMenutem;
					componentItems[teller].addActionListener(this);
					componentKeys[teller] = "DWOCOMP_"+ subMenuList.get(i)+"_"+componentList.get(i).get(j);
					System.out.println("Key: "+componentKeys[teller]);
					teller++;
				}
				componentChoice.add(subMenu);
				
			}
		}
		*/
		
		
		ArrayList<ArrayList<String>> componentList = new ArrayList<ArrayList<String>>();
		ArrayList<String> componentItemList = new ArrayList<String>();
		if(TekstVakPanel.templateComponents != null) {
			componentItems = new JMenuItem[TekstVakPanel.templateComponents.keySet().size()];
			componentKeys = new String[TekstVakPanel.templateComponents.keySet().size()];
			for (int i=0 ; i<TekstVakPanel.templateComponentsKeys.size() ; i++) 
			{	
				String key = TekstVakPanel.templateComponentsKeys.get(i);
				String[] parts = key.split("_");
		
				if(parts.length==2) {
					componentItemList.add(parts[1]);
					componentList.add(new ArrayList<String>());
				}
				else if(parts.length==3) {
					if(componentItemList.contains(parts[1])) {
						componentList.get(componentItemList.indexOf(parts[1])).add(parts[2]);
					}
					else {
						componentItemList.add(parts[1]);
						ArrayList<String> subMenu = new ArrayList<String>();
						subMenu.add(parts[2]);
						componentList.add(subMenu);
					}
				}
			}
			int teller = 0;
			
			for (int i=0 ; i<componentList.size() ; i++) {
				
				if(!componentList.get(i).isEmpty()) {
					JMenu subMenu = new JMenu(componentItemList.get(i));
					for (int j=0 ; j<componentList.get(i).size() ; j++) {
						JMenuItem subMenutem = new JMenuItem(componentList.get(i).get(j));
						subMenu.add(subMenutem);
						componentItems[teller] = subMenutem;
						componentItems[teller].addActionListener(this);
						componentKeys[teller] = "DWOCOMP_"+ componentItemList.get(i)+"_"+componentList.get(i).get(j);
						teller++;
					}
					componentChoice.add(subMenu);
				}
				else {
					if(componentItemList.get(i).startsWith("separator")) {
						componentChoice.addSeparator();
					}
					else if(componentItemList.get(i).startsWith("STND")) {
					  String type = componentItemList.get(i).substring(4);
					  String typeName = TComponentGeneratorFactory.getComponentTypeName(type);
					  componentItems[teller] = new JMenuItem(new TComponentAction(typeName,type));
					  componentKeys[teller] = "DWOCOMP_STND"+type;
                      componentChoice.add(componentItems[teller]);
					  teller++;
					}
					else {
						componentItems[teller] = new JMenuItem(componentItemList.get(i));
						componentItems[teller].addActionListener(this);
						componentKeys[teller] = "DWOCOMP_"+componentItems[teller].getText();
						componentChoice.add(componentItems[teller]);
						teller++;
					}
				}
				
			}
		} 
		
		String[] cTypes = TComponentGeneratorFactory.getComponentTypeList();
		String[] cTypeNames = TComponentGeneratorFactory.getComponentTypeNameList();
		standardComponentItems = new JMenuItem[cTypes.length];
		for(int i=0 ; i<cTypes.length ; i++) {
			//standardComponentItems[i] = new JMenuItem(cTypes[i]);new JMenuItem()
			//standardComponentItems[i].addActionListener(this);
		  standardComponentItems[i] = new JMenuItem(new TComponentAction(cTypeNames[i], cTypes[i]));
		  standardComponentChoice.add(standardComponentItems[i]);
		  if(i==0)
		    standardComponentChoice.addSeparator();
		}
		
			
		
		cbookKnop = new FormuleButton("cbook");
		cbookKnop.setBounds(toolbarLeft,64+26+26+26+26+26+26+26,balkH/2-10,20,20);
		cbookKnop.addActionListener(this);
		//if(form)headerPanel.add(cbookKnop);
		
		cindyKnop = new FormuleButton("cindy");
		cindyKnop.setBounds(toolbarLeft,64+26+26+26+26+26+26+26+26,balkH/2-10,20,20);
		cindyKnop.addActionListener(this);
		//if(form)headerPanel.add(cindyKnop);
		
		eslateKnop = new FormuleButton("eslate");
		eslateKnop.setBounds(toolbarLeft,64+26+26+26+26+26+26+26+26+26,balkH/2-10,20,20);
		eslateKnop.addActionListener(this);
		//if(form)headerPanel.add(eslateKnop);

		epsilonKnop = new FormuleButton("epsilonwriter");
		epsilonKnop.setBounds(toolbarLeft,64+26+26+26+26+26+26+26+26+26+26,balkH/2-10,20,20);
		epsilonKnop.addActionListener(this);
		//if(form)headerPanel.add(epsilonKnop);

		appletKnop = new FormuleButton("interactiecomponent");
		appletKnop.setBounds(toolbarLeft,64+26+26+26,balkH/2-10,20,20);
		appletKnop.addActionListener(this);
		if(form)headerPanel.add(appletKnop);
		appletKnopJMenu = new JPopupMenu();
        TekstInteractiePanelVak.makeJMenu(this,"Widgets", appletKnopJMenu, 1);
		
		wortelKnop = new FormuleButton("wortel");
		wortelKnop.setBounds(toolbarLeft,12,balkH/2-10,20,20);
		wortelKnop.addActionListener(this);
		wortelKnop.setVisible(false);
		headerPanel.add(wortelKnop);
		
		machtKnop = new FormuleButton("macht");
		machtKnop.setBounds(toolbarLeft,38,balkH/2-10,20,20);
		machtKnop.addActionListener(this);
		machtKnop.setVisible(false);
		headerPanel.add(machtKnop);
		
		kwadraatKnop = new FormuleButton("kwadraat");
		kwadraatKnop.setBounds(toolbarLeft,64,balkH/2-10,20,20);
		kwadraatKnop.addActionListener(this);
		kwadraatKnop.setVisible(false);
		headerPanel.add(kwadraatKnop);
		
		breukKnop = new FormuleButton("breuk");
		breukKnop.setBounds(toolbarLeft,90,balkH/2-10,20,20);
		breukKnop.addActionListener(this);
		breukKnop.setVisible(false);
		headerPanel.add(breukKnop);
		
		haakjesKnop = new FormuleButton("haakjes");
		haakjesKnop.setBounds(toolbarLeft,116,balkH/2-10,20,20);
		haakjesKnop.addActionListener(this);
		haakjesKnop.setVisible(false);
		headerPanel.add(haakjesKnop);
		
		ndewortelKnop = new FormuleButton("ndewortel");
		ndewortelKnop.setBounds(toolbarLeft,142,balkH/2-10,20,20);
		ndewortelKnop.addActionListener(this);
		ndewortelKnop.setVisible(false);
		//headerPanel.add(ndewortelKnop);
		
		ndelogKnop = new FormuleButton("ndelog");
		ndelogKnop.setBounds(toolbarLeft,166,balkH/2-10,25,20);
		ndelogKnop.addActionListener(this);
		ndelogKnop.setVisible(false);
		//headerPanel.add(ndelogKnop);
		
		integraalKnop = new FormuleButton("integraal");
		integraalKnop.setBounds(toolbarLeft,195,balkH/2-10,20,20);
		integraalKnop.addActionListener(this);
		integraalKnop.setVisible(false);
		//headerPanel.add(integraalKnop);
		
		prvKnop = new FormuleButton("prv");
		prvKnop.setBounds(toolbarLeft,221,balkH/2-10,20,20);
		prvKnop.addActionListener(this);
		prvKnop.setVisible(false);
		//headerPanel.add(prvKnop);
		
		absKnop = new FormuleButton("abs");
		absKnop.setBounds(toolbarLeft,247,balkH/2-10,20,20);
		absKnop.addActionListener(this);
		absKnop.setVisible(false);
		//headerPanel.add(absKnop);
		
		
		
		this.tekstVak = tekstVak;
		
		tekstVak.setBounds(5,5, 240,40);
		tekstVak.addActionListener(this);
		//tekstVak.setBorder(BorderFactory.createLineBorder(Color.lightGray, 1));
		add(tekstVak);
		//tekstVak.requestFocus();
		
		tekstVakActief = tekstVak;
		
	}
	
	public void setMainEditor(int marginX, int marginY, int docWidth, int docHeight)
	{
	  mainEditor = true;
	  
	  headerPanel.setBorder(BorderFactory.createLineBorder(Color.lightGray));//new Color(120,150,202)));
	  scrollPane.setBorder(BorderFactory.createLineBorder(Color.lightGray));//new Color(120,150,202)));
	 
	  
	  tekstVak.setOpaque(true);
      tekstVak.setBackground(Color.white);
      tekstVak.setBorder(BorderFactory.createLineBorder(Color.gray));
     
      tekstVak.zetMarge(marginX);
      tekstVak.zetBovenMarge(marginY);
      int x = Math.max(10, (getWidth() - balkH -docWidth)/2);
      tekstVak.setBounds(x,10, docWidth, docHeight);
      tekstVak.zetMinimumHoogte(docHeight);
      tekstVak.layoutTekst();
      
      setNewScrollSize();
      contentPane.setShadow(true);
      contentPane.setBackground(new Color(206,207,208));//Color.lightGray);//Color.lightGray);//new Color(206,207,208));
	}
	
	
	
	public void setCrossWidgetOption(boolean b)
	{
		crossWidgetOption = b;
		if(!formMode)crosswidgetKnop.setVisible(b);
	}
	
	public void setTemplateOption(boolean b)
	{
		
		if(!formMode && templateItems!=null && templateItems.length>0){
		    templateOption = b;
			templateChoiceKnop.setVisible(b);
			componentChoiceKnop.setVisible(b);
			standardComponentChoiceKnop.setVisible(!b);
			standardComponentOption = !b;
		}
		else {
		  standardComponentChoiceKnop.setVisible(!b);
		  standardComponentOption = !b;
		}
		
	}
	
	public void setStandardComponentOption(boolean b)
	{   standardComponentOption = !templateOption && b;
		standardComponentChoiceKnop.setVisible(!templateOption && b);
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
        {   
            if(toolbarLeft)
            {   basisPanel.setBounds(balkH+sparing,0,b-balkH-sparing,h);
                headerPanel.setBounds(0,0,balkH,h);
            }
            else
            {   basisPanel.setBounds(0,balkH+sparing,b,h-balkH-sparing);
                headerPanel.setBounds(0,0,b,balkH);
            }
        }
        else
        {   
            basisPanel.setBounds(0,0,b,h);
            headerPanel.setBounds(0,0,0,0);
        }
        basisPanel.doLayout();
        super.setBounds(x,y,b,h);
        resizeButton.setBounds(getSize().width-18,3,15,15);
        if(mainEditor)
        {
          //tekstVak.setBounds(5,5,b-10-(toolbarLeft?33:0),h-30);
        }
        else
        {
          //tekstVak.setBounds(5,5,b-10-(toolbarLeft?33:0),h-30);
        }
       
        //tekstVak.layoutTekst();
        resizeButton.setBounds(getSize().width-18,3,15,15);
        setNewScrollSize();
    }
	
    public void setSize(int b, int h)
    {   if(headerAan)
        {   
            if(toolbarLeft)
            {   basisPanel.setBounds(balkH+sparing,0,b-balkH-sparing,h);
                headerPanel.setBounds(0,0,balkH,h);
            }
            else
            {   basisPanel.setSize(b,h-balkH-sparing);
                headerPanel.setSize(b,balkH);
            }
        }
        else
        {   
            basisPanel.setSize(b,h);
            headerPanel.setSize(0,0);
        }
        basisPanel.doLayout();
        super.setSize(b,h);
        resizeButton.setBounds(getSize().width-18,3,15,15);
        if(mainEditor)
        {
          //tekstVak.setSize(b-10-(toolbarLeft?33:0),h-30);
        }
        else
        {
          //tekstVak.setSize(b-10-(toolbarLeft?33:0),h-30);
        }
        //tekstVak.setSize(b-10-(toolbarLeft?33:0),h-30);
        //tekstVak.layoutTekst();
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
	{	int maxw = 0;
	    int maxh = 0; //scrollPane.getSize().height-20;
		for(int i=0 ; i<contentPane.getComponentCount() ; i++)
		{	Component c = contentPane.getComponent(i);
			int h = c.getLocation().y + c.getSize().height + 20;
			int w = c.getLocation().x + c.getSize().width + 5;
			if(h>maxh) maxh = h;
			if(w>maxw) maxw = w;
		}
		if(mainEditor)
		  contentPane.setPreferredSize(new Dimension(maxw, maxh));
		else
		  contentPane.setPreferredSize(new Dimension(getSize().width-(toolbarLeft?55:50), maxh));
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
			templateChoiceKnop.setVisible(false);
			componentChoiceKnop.setVisible(false);
			standardComponentChoiceKnop.setVisible(false);
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
			if(templateOption){
				templateChoiceKnop.setVisible(templateItems!=null && templateItems.length>0);
				componentChoiceKnop.setVisible(componentItems!=null && componentItems.length>0);
			}
			if(standardComponentOption){
				standardComponentChoiceKnop.setVisible(true);
			}
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
	
	public void setToolbarLeft(boolean b)
	{
	    toolbarLeft = true;
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
		{	//tekstVakActief.insertTekstInteractiePanelVak(0);
		  antwoordVakKnopJMenu.setVisible(true);
		  antwoordVakKnopJMenu.show(this,antwoordVakKnop.getLocation().x, antwoordVakKnop.getLocation().y+antwoordVakKnop.getHeight());
	        
		}
		else if(e.getSource()==tekstVakKnop)
		{	tekstVakActief.insertTekstInteractiePanelVak(3);
		}
		else if(e.getSource()==linkKnop)
		{	tekstVakActief.insertLinkVak();
		}
		else if(e.getSource()==appletKnop)
		{	//tekstVakActief.insertTekstInteractiePanelVak(1);
		  appletKnopJMenu.setVisible(true);
		  appletKnopJMenu.show(this,appletKnop.getLocation().x, appletKnop.getLocation().y+appletKnop.getHeight());
         
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
		else if(e.getSource()==standardComponentChoiceKnop)
		{	standardComponentChoice.show(this,standardComponentChoiceKnop.getLocation().x, standardComponentChoiceKnop.getLocation().y+standardComponentChoiceKnop.getHeight());
		}
		else if(e.getSource()==templateChoiceKnop)
		{	templateChoice.show(this,templateChoiceKnop.getLocation().x, templateChoiceKnop.getLocation().y+templateChoiceKnop.getHeight());
		}
		else if(e.getSource()==componentChoiceKnop)
		{	componentChoice.show(this,componentChoiceKnop.getLocation().x, componentChoiceKnop.getLocation().y+componentChoiceKnop.getHeight());
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
			} else if (LOGGING.equals(e.getActionCommand()))
			{
				setAttempt();
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
		else if(e.getSource()==sendCommandButton && cbookEventHandler.hasListeners("text"))
		{
			String text = getText();
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("content", text);
			cbookEventHandler.fire("text",map);
		}
		
		else if(templateItems!=null || componentItems!=null || standardComponentItems!=null)
		{
			for(int i=0 ;standardComponentItems!=null && i<standardComponentItems.length ; i++)
			{
				if(e.getSource()==standardComponentItems[i])
					TComponentGeneratorFactory.getComponentGenerator(standardComponentItems[i].getText()).generateComponent( tekstVakActief);
				
			}
			for(int i=0 ; templateItems!=null && i<templateItems.length ; i++)
			{
				if(e.getSource()==templateItems[i] && tekstVakActief instanceof BasisTekstVak )
					tekstVakActief.insertDups(TekstVakPanel.templatePages.get(templateKeys[i]));
				//System.out.println("Key-template: "+templateItems[i].getText());
			}
			for(int i=0 ;componentItems!=null && i<componentItems.length ; i++)
			{
				if(e.getSource()==componentItems[i]) {
					tekstVakActief.insertDups(TekstVakPanel.templateComponents.get(componentKeys[i]));
					//System.out.println("Key-component: "+componentItems[i].getText());
					//System.out.println("Key-component: "+componentKeys[i]);
				}
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
		boolean logOption = false;
		String logID = "";
				
		if(h.containsKey("tekst")) tekst = (String)h.get("tekst");
		if(h.containsKey("balkZichtbaar")) balkZichtbaar = ((Boolean)h.get("balkZichtbaar")).booleanValue();
		if(h.containsKey("rekenTool")) rekenTool = ((Boolean)h.get("rekenTool")).booleanValue();
		if(h.containsKey("grafTool")) grafTool = ((Boolean)h.get("grafTool")).booleanValue();
		if(h.containsKey("formuleKnop")) formuleKnop = ((Boolean)h.get("formuleKnop")).booleanValue();
		if(h.containsKey("formuleToolPopup")) formuleToolPopup = ((Boolean)h.get("formuleToolPopup")).booleanValue();
		if(h.containsKey("interactiePanelLaunchData")) interactiePanelLaunchData = (Hashtable[])h.get("interactiePanelLaunchData");
		if(h.containsKey("boxMetRand")) boxMetRand = ((Boolean)h.get("boxMetRand")).booleanValue();
		
		if (h.containsKey("logOption"))
			logOption = ((Boolean) h.get("logOption")).booleanValue();
		if (h.containsKey("logID"))
			logID = (String) h.get("logID");
		
		this.rekenTool = rekenTool;
		this.grafTool = grafTool;
		this.logID = logID;
		this.logOption = logOption;
		
		try         
        {   tekst = FormuleParser.randomizeTekstVakString(tekst, randomVars, randomValues);
        }
        catch(Exception e)
        {   tekst = "???";
        }
		zetTekst(tekst);
		layoutTekst();
		zetBalkZichtbaar(balkZichtbaar);
		zetRekenTool(rekenTool);
		zetGrafTool(grafTool);
		zetFormuleKnop(formuleKnop);
		zetFormuleToolPopup(formuleToolPopup);
		zetMetRand(boxMetRand);
		setOpaque(boxMetRand);
		basisPanel.setOpaque(boxMetRand);
		scrollPane.setOpaque(boxMetRand);
		scrollPane.getViewport().setOpaque(boxMetRand);
		contentPane.setOpaque(boxMetRand);
		//tekstVak.setOpaque(boxMetRand);
		//tekstVakActief.setOpaque(boxMetRand);
		
		
		if(interactiePanelLaunchData!=null)
		{	Vector v = geefInteractiePanels();
	        for(int i=0 ; i<v.size() ; i++)
	    	{	((InteractiePanelContainerIF)v.elementAt(i)).zetOpdracht(interactiePanelLaunchData[i], randomVars, randomValues);
	    	}	
	        tekstVak.layoutTekst();
		}  
       
		
	}
	
	public void zetMetRand(boolean b)
	{	//Color c = b ? Color.gray : Color.white;
		Color c = Color.gray;
		if(b)
			scrollPane.setBorder(BorderFactory.createLineBorder(c));
		else
			scrollPane.setBorder(BorderFactory.createEmptyBorder());
	}
	
	@SuppressWarnings("unchecked")
	public void setState(Hashtable h)
	{
		String tekst = "";
		Hashtable[] interactiePanelStates = null;
		Hashtable[] interactiePanelLaunchData = null;
		boolean editable = true;
		
		if(h.containsKey("tekst")) tekst = (String)h.get("tekst");
		if(h.containsKey("interactiePanelLaunchData")) interactiePanelLaunchData = OpdrNavStruct.toHashtableArray(h.get("interactiePanelLaunchData"));
		if(h.containsKey("interactiePanelStates")) interactiePanelStates = OpdrNavStruct.toHashtableArray(h.get("interactiePanelStates"));
		if(h.containsKey("editable")) editable = ((Boolean)h.get("editable")).booleanValue();
		if(h.containsKey("attempts")) attempts = OpdrNavStruct.toVector( h.get("attempts") );
		
		//if(antwoordVak.getText()==null || antwoordVak.getText().trim().equals("")) 
		zetTekst(tekst);
		layoutTekst();
		Vector v = geefInteractiePanels();
	    for(int i=0 ; i<v.size() ; i++)
	    {  	InteractiePanelContainerIF ipc = ((InteractiePanelContainerIF)v.elementAt(i));
	    	ipc.setEditState(interactiePanelLaunchData[i]);
	    	ipc.setState(interactiePanelStates[i]);
	    	if(ipc instanceof TekstInteractiePanelVak)((TekstInteractiePanelVak)ipc).setEditMode(false);
	    	
	    }
	    this.editable = editable;
	    tekstVak.setEditable(editable);
	    if(editable)
			tekstVak.setForeground(Color.black);
		else
			tekstVak.setForeground(Color.gray);
	}
	
	public Hashtable getState()
	{	String tekst = "";
		Hashtable[] interactiePanelStates = null;
		Hashtable[] interactiePanelLaunchData = null;
		boolean editable = true;
		
		tekst = getText();
		editable = this.editable;
		
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
		h.put("editable", new Boolean(editable));
		
		if (logOption)
		{
			Hashtable logMap = new Hashtable();
			setAttempt();
			String logString = getAttempt();
			
			logMap.put("logAnswer", logString);
			logMap.put("logScore", _0);
			logMap.put("logMaxScore", _0);
			logMap.put("logErrorCount", _0);
			logMap.put("logAttemptsCount", attempts.size());
			logMap.put("logAttempts", attempts);
			h.put("attempts", attempts);
			WiskOpdr.setLog(logID, logMap);
		}

			
		return h;
	}

	/**
	 * bouw de "attempt" string.
	 * Eén regel, geen puntkomma; 
	 * @return string
	 */
	private String getAttempt() {
		String attempt =  getCompleteText();
		attempt = attempt.replace('\n', ' ');
		attempt = attempt.replace(";",".,");
		return attempt.trim();
	}
		
	private void setAttempt() {
		if (logOption) {
		String current = getAttempt();
		String last = "";
		if( attempts.size() > 0) {
			last = attempts.lastElement();
			int i = last.indexOf(';');
			if(i >= 2) last = last.substring(0, i-2);
		}
		if( ! last.equals( current))
		{
			attempts.add(
				current + "  ;    ;    ;  " + new Date()
			);
		}}
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
	
	@SuppressWarnings("rawtypes")
    public Hashtable getEditState()
	{	String tekst = "";
		Hashtable[] interactiePanelLaunchData = null;
		boolean premium;
		//tekst = getText();
		tekst = getCompleteText();
		premium = FormuleVak.detectPremium(tekst);
		Vector v = geefInteractiePanels();
		interactiePanelLaunchData = new Hashtable[v.size()];
	    for(int i=0 ; i<v.size() ; i++)
		{	interactiePanelLaunchData [i] = ((InteractiePanelContainerIF)v.elementAt(i)).getEditState();
		    Hashtable launchData = interactiePanelLaunchData[i];
            Object interactiePremium = launchData.get("premium");
		    if (Boolean.TRUE.equals(interactiePremium))
		        premium = true;
		}
	   
		Hashtable<String,Object> h = new Hashtable<>();
		h.put("tekst", tekst);
		h.put("interactiePanelLaunchData", interactiePanelLaunchData);
		if(premium)
		  h.put("premium", Boolean.TRUE);
	
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
	{
		Container parent = getParent();
		int x = xx + getLocation().x;
		int y = yy + getLocation().y;
		int h = parent.getSize().height;
		for (int i = 0; parent != null && i < 40; i++)
		{
			if (parent instanceof TabletOwner)
			{
				((TabletOwner) parent).addTablet(formuleVakHouder, x, y);
				break;
			} else
			{
				x += parent.getLocation().x;
				y += parent.getLocation().y;
				parent = parent.getParent();
				if (parent == null)
					return;
			}
		}
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
 	
 	@Override
	public void acceptCBookEvent(CBookEvent event) {
		String command = event.getCommand();
		if(command.startsWith("text"))
		{
			Map map = (Map)event.getParameters();
			if(map!=null)
			{	
				String textString = (String)map.get("content");
				zetTekst(textString);
				layoutTekst();
			}
		}
		else if(command.startsWith("action.setNotEditable"))
		{	editable = false;
			tekstVak.setEditable(editable);
			if(editable)
				tekstVak.setForeground(Color.black);
			else
				tekstVak.setForeground(Color.gray);
		}
		
	}

	@Override
	public void addCBookEventListener(CBookEventListener listener, String command) {
		cbookEventHandler.addCBookEventListener(listener, command);
		if(sendCommandButton==null 
				&& cbookEventHandler.hasListeners("text") // Alleen als er 'text' messages kunnen worden ontvangen
		)
		{
			sendCommandButton = new JButton(WiskOpdr.rb.getString("executeLabel"));
			sendCommandButton.setBounds(basisPanel.getWidth()-80,basisPanel.getHeight()-25, 75, 20 );
			sendCommandButton.addActionListener(this);
			basisPanel.add(sendCommandButton,BorderLayout.SOUTH);
			
		}
		
	}

	@Override
	public void removeCBookEventListener(CBookEventListener listener,String command) {
		cbookEventHandler.removeCBookEventListener(listener, command);
		
	}

	@Override
	public String[] getSendCmds() {
//		if(cbookEventHandler.hasListeners()) Peter, dit kan nooit werken! Wim
//		{
//			
//		}
		String[] commands = {"text"};
		return commands;
	}

	@Override
	public String[] getAcceptedCmds() {
		String[] commands = {
				"text",
				"action.setNotEditable"};
		return commands;
	}
	
	@Override
	public String getLocalizedCmd(String cmd) {
		String localizedCmd = WiskOpdr.rb.getString(CBA_PREFIX + cmd);
		if(localizedCmd==null)
			return cmd;
		return localizedCmd;
	}
	
	public class TComponentAction extends AbstractAction {
      public TComponentAction(String text, String desc) {
          super(text, null);
          putValue("TCOMP_KEY", desc);
      }
      public void actionPerformed(ActionEvent e) {
          TComponentGeneratorFactory.getComponentGenerator((String)getValue("TCOMP_KEY")).generateComponent( tekstVakActief);
        
      }
	}
	
	public class AntwoordvakKeuzeAction extends AbstractAction {
      public AntwoordvakKeuzeAction(String text, int setNr, int soort) {
          super(text, null);
          putValue("setNr", setNr);
          putValue("soort", soort);
      }
      public void actionPerformed(ActionEvent e) {
          tekstVakActief.insertTekstInteractiePanelVak((Integer)getValue("setNr"),(Integer)getValue("soort"));
      }
    }
}
