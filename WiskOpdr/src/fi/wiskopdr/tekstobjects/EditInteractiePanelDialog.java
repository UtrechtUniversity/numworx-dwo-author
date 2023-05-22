package fi.wiskopdr.tekstobjects;

import java.awt.AWTEventMulticaster;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Frame;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.Constructor;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.cbook.cbookif.CBookWidgetIF;
import org.cbook.cbookif.Constants;

//import fi.algebrapijlenopdr.AlgebraPijlenOpdr;
import fi.beans.iconan.Iconan;
import fi.beans.loader.Loader;
import fi.beans.wiskopdrbeans.InteractieEditPanel;
import fi.beans.wiskopdrbeans.WiskOpdrApplet;
//import fi.nabouwenaanzichten.NabouwenAanzichten;
import fi.wiskopdr.AntwoordFormuleVak;
import fi.wiskopdr.AntwoordFormuleVakEditPanel;
import fi.wiskopdr.AntwoordKeuzeVak;
import fi.wiskopdr.AntwoordTekstVak;
import fi.wiskopdr.AntwoordVergelijkingVak;
import fi.wiskopdr.AntwoordVergelijkingVakEditPanel;
import fi.wiskopdr.BerekeningVak;
import fi.wiskopdr.CheckButtonPanel;
import fi.wiskopdr.CheckSleepUnitPanel;
import fi.wiskopdr.CheckUnitPanel;
import fi.wiskopdr.CheckValueUnitPanel;
import fi.wiskopdr.Geogebra3Panel;
import fi.wiskopdr.GeogebraPanel;
import fi.wiskopdr.GetallenlijnSprongPanel;
import fi.wiskopdr.GrafiekPanel;
import fi.wiskopdr.HelpButton;
import fi.wiskopdr.HelpButtonPanelIF;
import fi.wiskopdr.LeerdoelWidget;
import fi.wiskopdr.ScoreWidget;
import fi.wiskopdr.SimpelAntwoordFormuleVak;
import fi.wiskopdr.SimpelAntwoordVergelijkingVak;
import fi.wiskopdr.TabletOwningLayeredPane;
import fi.wiskopdr.TekstVakEditPanel;
import fi.wiskopdr.TekstVakPanel;
import fi.wiskopdr.WiskOpdr;
import fi.wiskopdr.WiskOpdrButton;
import fi.wiskopdr.WiskOpdrCheckbox;
import fi.wiskopdr.WiskOpdrComboBox;
import fi.wiskopdr.WiskOpdrTextField;
import fi.wiskopdr.cbook.CBookInteractieEditPanel;
import fi.wiskopdr.cbook.CBookWrap;
import fi.wiskopdr.cbook.Service;
import fi.wiskopdr.formuleobjects.FormuleButton;
import fi.wiskopdr.opdrnav.OpdrNavStructEdit;
//import fi.tekenveelvlakopdr.TekenVeelvlakOpdr;
//import fi.mozarch.MozArch;
import fi.wiskopdr.opdrnav.XWidgetManager;
import fi.wiskopdr.samengesteldestappen.SamengesteldeStappenPanel;
import fi.wiskopdr.scheikundeobjects.ReactieVergelijkingVak;
import fi.wiskopdr.stelselsvergelijkingen.StelselAntwoordVak;
import fi.wiskopdr.strategievak.StrategieVakPanel;
import fi.wiskopdr.symbolen.SymboolPanel;


public class EditInteractiePanelDialog extends JDialog implements ActionListener,  WindowListener , FocusListener, XWidgetManager.HasWidgetManager
{
	private JPanel mainPanel;
	private JPanel bottomPanel;
	private InteractieEditPanel interactieEditPanel;
	private int huidigSoortInteractiePanel = -1;
	private JButton okButton;
    private JButton cancelButton;
    private JButton shareButton;
    JComboBox soortAntwoordVakKeuze;
    private JTextField breedteTF, hoogteTF;
    private JLabel breedteLabel, hoogteLabel;
    private JCheckBox volledigeBreedteCB, popupCB;
    private JLabel popupTitleLabel;
    private JTextField popupTitleTF;
    private FormuleButton imageButton;
    
    private Font font = new Font("SansSerif",Font.PLAIN,12);//WiskOpdr.tekstFont;
    private int widthEditPanel = 900;
    private int heightEditPanel = 600;
   
    private Dialog imageDialog;
    private Iconan iconman;
    private String popupImageString = "";
    private Image popupImage;
    boolean confirmed;
    
    int[] set;
    int setNr;
    
   // int[] set = {5,6,7,11,15,17,18,19,20,21,22,23,24,26,27,28,29,30,31,32,34,35,36,37,38,40,41,42,43,44,45,46,47,48,50,51,54,56,57,58,59};
    
    // Deze extra lijst maakt een selectie uit de set, 
    // want niet alle widgets zijn HTML5 compliant. 
    // Bovendien is de volgorde alfabetisch gemaakt
    //int[] widgetSelection = {21,0,1,12,4,31,3,38,8,37,40,36,39,27,26,14,10,28,34,17,33,25,18,19,13,7};
    
    
    //bij zwevend TeksVakPanel
    private int locationX;
    private int locationY;
    
    //private Dimension size;
	private String crossWidgetId;
	private XWidgetManager manager;
	Object subscriptions; // Not to loose. 
	
	HelpButton helpButton;
	private JPanel helpPanel;
    private Box helpBox;
    private Box helpTitelBox;
    private JButton hideHelpButton;

    public EditInteractiePanelDialog(Frame owner, String windowTitle, boolean modal, int setNr,  Hashtable launchData, XWidgetManager manager) {
    	super(owner, windowTitle, modal);
    	initEditInteractiePanelDialog(setNr, launchData, manager);
    }

    /**
     * Constuct an EditInsteractiePanelDialog met een Window.
     * @param owner Frame of Dialog
     * @param windowTitle
     * @param modal
     * @param setNr
     * @param launchData
     * @param manager
     * @since 1.6
     */
    public EditInteractiePanelDialog(Window owner, String windowTitle, boolean modal, int setNr,  Hashtable launchData, XWidgetManager manager) {
    	super(owner, windowTitle);setModal(modal);
    	initEditInteractiePanelDialog(setNr, launchData, manager);
    	
    }
    
    public EditInteractiePanelDialog(Window owner, String windowTitle, boolean modal, int setNr,  int soort, XWidgetManager manager) {
      super(owner, windowTitle);setModal(modal);
      initEditInteractiePanelDialog(setNr, null, soort, manager);
    }
    
//    public EditInteractiePanelDialog(Dialog owner, String windowTitle, boolean modal, int setNr,  Hashtable launchData, XWidgetManager manager) {
//    	super(owner, windowTitle, modal);
//    	initEditInteractiePanelDialog(setNr, launchData, manager);
//    }
    
    void initEditInteractiePanelDialog(int setNr,  Hashtable launchData, XWidgetManager manager) {
      initEditInteractiePanelDialog(setNr, launchData, 999, manager);
    }

    void initEditInteractiePanelDialog(int setNr,  Hashtable launchData, int soort, XWidgetManager manager) {
    	//Container content = getContentPane();
    	setLayeredPane(new TabletOwningLayeredPane());
    	setContentPane(getContentPane());
    	
    	getContentPane().setLayout(new BorderLayout());
        //this.setBackground(Color.red);//new Color(230,230,230));
        setBackground(WiskOpdr.colorGray3);
        
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBorder(BorderFactory.createEmptyBorder(5, 20, 5, 20));
        headerPanel.setBackground(WiskOpdr.colorBlue1);
        
        int soortInteractiePanel=-1;
        String title = soort==999 ? "" : (TekstInteractiePanelVak.interactiePanelDescriptions[TekstInteractiePanelVak.interactiePanelSets[setNr][soort]]).toLowerCase();
        if(title.equals("") && launchData!=null) {
	        soortInteractiePanel = ((Integer)launchData.get("soortInteractiePanel")).intValue();
	        title = TekstInteractiePanelVak.interactiePanelDescriptions[soortInteractiePanel];
        }
        JLabel headerTitle = new JLabel(WiskOpdr.rb.getString("settingsLabel") + " " + title);
        headerTitle.setForeground(WiskOpdr.colorGray3);
        headerTitle.setFont(new Font("SansSerif",Font.PLAIN, 24));
        //headerPanel.add(headerTitle);
        
        String HELP_URL1 = "https://app.dwo.nl/public/?header=less&hash=#s:670047";
		 helpButton = new HelpButton(HELP_URL1);
		//helpButton.setFont(new Font("SansSerif",Font.BOLD,14));
		helpButton.setPreferredSize(new Dimension(22,22));
		helpButton.setMinimumSize(new Dimension(22,22));
		helpButton.setMaximumSize(new Dimension(22,22));
		helpButton.addActionListener(this);
		//headerPanel.add(helpButton);
		helpPanel = new JPanel(new BorderLayout());
		helpPanel.setBorder(BorderFactory.createMatteBorder(0, 1, 0, 0, WiskOpdr.colorBlue4));
		helpPanel.setBackground(WiskOpdr.colorBlue5);
		helpPanel.setPreferredSize(new Dimension(300,400));
		JComponent bp = OpdrNavStructEdit.helpBrowser.getBrowserPanel();
    	bp.setPreferredSize(new Dimension(300,400));
    	
    	helpPanel.add(bp);
		
		helpBox = Box.createVerticalBox();
		helpBox.add(Box.createRigidArea(new Dimension(300,0)));
		helpBox.add(helpPanel);
		helpBox.setVisible(false);
		helpBox.setMaximumSize(new Dimension(300,800));
		
		JLabel helpTitleLabel = new JLabel(WiskOpdr.rb.getString("helpTitelLabel"));
		helpTitleLabel.setForeground(WiskOpdr.colorBlue5);
		helpTitleLabel.setFont(new Font("SansSerif",Font.PLAIN, 24));
		
		hideHelpButton = new WiskOpdrButton("\u276e");
		hideHelpButton.setBorder(BorderFactory.createLineBorder(WiskOpdr.colorBlue1));
		hideHelpButton.setBackground(WiskOpdr.colorBlue1);
		hideHelpButton.setForeground(WiskOpdr.colorBlue5);
		hideHelpButton.setPreferredSize(new Dimension(20,20));
		hideHelpButton.setFont(new Font("SansSerif",Font.PLAIN, 24));
		hideHelpButton.addActionListener(this);
	        
		helpTitelBox = Box.createVerticalBox();
		helpTitelBox.add(Box.createRigidArea(new Dimension(300,0)));
		Box helpheader = Box.createHorizontalBox();
		helpheader.add(Box.createRigidArea(new Dimension(140,0)));
		helpheader.add(helpTitleLabel);
		helpheader.add(Box.createRigidArea(new Dimension(80,0)));
		helpheader.add(hideHelpButton);;
		helpTitelBox.setPreferredSize(new Dimension(300,30));
		helpTitelBox.add(helpheader);
		helpTitelBox.setVisible(false);
		
		Box headerbox = Box.createHorizontalBox();
		headerbox.add(Box.createHorizontalGlue());
		headerbox.add(headerTitle);
		headerbox.add(Box.createHorizontalGlue());
		
		// Nog niet alle interactieEditPanel zijn klaar voor een helppanel
		boolean geschikt = setNr==0 && soort<13 && soort!=8	|| setNr==3 ||
				( soortInteractiePanel>-1 && 
						(soortInteractiePanel<5
								|| soortInteractiePanel==9
								|| soortInteractiePanel==12
								|| soortInteractiePanel==16
								|| soortInteractiePanel==33
								|| soortInteractiePanel==49
								|| soortInteractiePanel==13
								|| soortInteractiePanel==14
								|| soortInteractiePanel==53));
		if(geschikt) 
			headerbox.add(helpButton);
		headerbox.add(helpTitelBox);
		headerPanel.add(headerbox);
		
        
        bottomPanel = new JPanel();
        bottomPanel.setLayout(new BorderLayout());
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        bottomPanel.setBackground(WiskOpdr.colorGray2);
        
        mainPanel = new JPanel() {
//        	public void invalidate() {
//        		super.invalidate();
//        		pack();
//        	}
        };
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(WiskOpdr.colorGray3);
        
        Box hb = Box.createHorizontalBox();
        hb.add(mainPanel);
        hb.add(helpBox);
        
        getContentPane().add(hb);
		getContentPane().add(bottomPanel,BorderLayout.SOUTH);
		getContentPane().add(headerPanel,BorderLayout.NORTH);
               
        this.setNr = setNr;
        this.manager = manager;
        confirmed = false;
        
//        size = new Dimension(800,650); // Default value
//        if(setNr==4 || setNr==3) {
//        	size = new Dimension(1000,700);
//        } 
//        else if (setNr == 2) {  // GraphTool
//            size = new Dimension(900,700);
//        	
//        }


       
        Box boxh = Box.createHorizontalBox();

        okButton = new WiskOpdrButton(WiskOpdr.rb.getString("okKnopLabel"));
        okButton.setPreferredSize(new Dimension(75,24));
        okButton.setMaximumSize(new Dimension(75,24));
        okButton.setMinimumSize(new Dimension(75,24));
        okButton.setBounds(15,20,75,24);
        okButton.setBackground(WiskOpdr.colorBlue1);
        //okButton.setFont(font);
        okButton.addActionListener(this);
        boxh.add(okButton);
        boxh.add(Box.createHorizontalStrut(20));
        
        cancelButton = new WiskOpdrButton(WiskOpdr.rb.getString("annuleerKnopLabel"));
        cancelButton.setBackground(WiskOpdr.colorBlue1);
        cancelButton.setPreferredSize(new Dimension(75,24));
        cancelButton.setMaximumSize(new Dimension(75,24));
        cancelButton.setMinimumSize(new Dimension(75,24));
        cancelButton.setBounds(105,20,75,24);
        cancelButton.setMargin(new Insets(4,10,4,10));
       // cancelButton.setFont(font);
        cancelButton.addActionListener(this);
        boxh.add(cancelButton);
        boxh.add(Box.createHorizontalStrut(20));
        
        soortAntwoordVakKeuze = new WiskOpdrComboBox();
		soortAntwoordVakKeuze.setBounds(190,20,165,24);
		soortAntwoordVakKeuze.setPreferredSize(new Dimension(175,22));
		soortAntwoordVakKeuze.setMaximumSize(new Dimension(175,22));
		soortAntwoordVakKeuze.setFont(font);
		soortAntwoordVakKeuze.addActionListener(this);
//		if(setNr==0 && (soort==0 || soort==1 || soort==2 || soort==3)) {
//			boxh.add(soortAntwoordVakKeuze);
//			boxh.add(Box.createHorizontalStrut(20));
//		}
		
		initSet(setNr);
		
		breedteLabel = new JLabel(WiskOpdr.rb.getString("breedteLabel"));
		breedteLabel.setForeground(WiskOpdr.colorBlue1);
        breedteLabel.setBounds(370,20,40,22);
        breedteLabel.setFont(font);
        boxh.add(breedteLabel);
        boxh.add(Box.createHorizontalStrut(5));
        
        breedteTF = new WiskOpdrTextField("300");
        breedteTF.setBounds(420,20,40,22);
        breedteTF.setPreferredSize(new Dimension(40,22));
        breedteTF.setFont(font);
        breedteTF.addActionListener(this);
        breedteTF.addFocusListener(this);
        
        boxh.add(breedteTF);
        boxh.add(Box.createHorizontalStrut(10));
        
        hoogteLabel = new JLabel(WiskOpdr.rb.getString("hoogteLabel"));
        hoogteLabel.setForeground(WiskOpdr.colorBlue1);
        hoogteLabel.setBounds(465,20,50,22);
        hoogteLabel.setFont(font);
        boxh.add(hoogteLabel);
        boxh.add(Box.createHorizontalStrut(5));
        
        hoogteTF = new WiskOpdrTextField("250");
        hoogteTF.setBounds(510,20,40,22);
        hoogteTF.setPreferredSize(new Dimension(40,22));
        hoogteTF.setFont(font);
        hoogteTF.addActionListener(this);
        hoogteTF.addFocusListener(this);
        
        
        boxh.add(hoogteTF);
        boxh.add(Box.createHorizontalStrut(20));
        
        volledigeBreedteCB = new WiskOpdrCheckbox(WiskOpdr.rb.getString("volleBreedteLabel"));
        volledigeBreedteCB.setOpaque(false);
        volledigeBreedteCB.setFont(font);
        volledigeBreedteCB.setBounds(560,20,100,22);
        volledigeBreedteCB.addActionListener(this);
        //volledigeBreedteCB.setSelected(true);
        boxh.add(volledigeBreedteCB);
        boxh.add(Box.createHorizontalStrut(20));

        popupCB = new WiskOpdrCheckbox(WiskOpdr.rb.getString("popupLabel"));
		popupCB.setOpaque(false);
		popupCB.setFont(font);
		popupCB.setBounds(670,20,70,22);
		popupCB.addActionListener(this);
	    //volledigeBreedteCB.setSelected(true);
		boxh.add(popupCB);
		boxh.add(Box.createHorizontalStrut(5));
		
		imageButton = new FormuleButton("tekstvak");
		imageButton.setBounds(730,20,20,20);
		imageButton.setPreferredSize(new Dimension(22,22));
		imageButton.addActionListener(this);
		imageButton.setVisible(false);
		boxh.add(imageButton);
		boxh.add(Box.createHorizontalStrut(5));
		
		popupTitleLabel = new JLabel(WiskOpdr.rb.getString("popupTitelLabel"));
		popupTitleLabel.setForeground(WiskOpdr.colorBlue1);
		popupTitleLabel.setBounds(465,20,50,22);
		popupTitleLabel.setFont(font);
		popupTitleLabel.setVisible(false);
	    boxh.add(popupTitleLabel);
	    boxh.add(Box.createHorizontalStrut(5));
	    
	    popupTitleTF = new WiskOpdrTextField("Popup");
	    popupTitleTF.setBounds(510,20,40,22);
	    popupTitleTF.setPreferredSize(new Dimension(60,22));
	    popupTitleTF.setFont(font);
	    popupTitleTF.addActionListener(this);
	    popupTitleTF.addFocusListener(this);
	    popupTitleTF.setVisible(false);
	    boxh.add(popupTitleTF);
	    boxh.add(Box.createHorizontalStrut(5));
		
	    boxh.add(Box.createHorizontalGlue());
		shareButton = new JButton(shareAction);
		shareButton.setVisible(ShareAction.getSharingPossible() || ShareAction.getSharingIsUsed());
		//shareButton.setBounds((int)size.getWidth()-45,20,24,20);
		boxh.add(shareButton);
		
		bottomPanel.add(boxh);
		
		// nog nodig?
		if(soort!=999 && (setNr==0 && (soort==0 || soort==1 || soort==2 || soort==3)
				//|| TekstInteractiePanelVak.interactiePanelSets[setNr][soort]==6
				)) {
			launchData = shareAction.unwrap(launchData);
	        addInteractieEditPanel(launchData, soort);
	        
		}
		else {
			//getContentPane().add(bottomPanel,BorderLayout.SOUTH);
	        launchData = shareAction.unwrap(launchData);
	        addInteractieEditPanel(launchData, soort);
	        //this.setSize(Math.max(size.width, widthEditPanel), size.height);
	        doLayout();
		}
		
        
        this.addWindowListener(this);
        
        
        if (WiskOpdr.isExperimental()) {
          String t = getTitle();
          String id = getCrossWidgetId0();
          if (id != null) {
            t = t + " (" + id + ")";
            setTitle(t);
          }
        }
    }

	void initSet(int setNr) {
		this.setNr = setNr;
		set = TekstInteractiePanelVak.interactiePanelSets[setNr];
		soortAntwoordVakKeuze.addItem(WiskOpdr.rb.getString("interactieKeuzeLabel") + TekstInteractiePanelVak.interactiePanelSetNames[setNr]);
		

		if(setNr == TekstInteractiePanelVak.CBookSetNr)
		{
			Iterator<?> iter = Service.getWidgets(setNr).iterator();
			while(iter.hasNext())
				soortAntwoordVakKeuze.addItem(iter.next());
		}
		else if(setNr == TekstInteractiePanelVak.CindySetNr)
		{
			Iterator iter = Service.getWidgets(setNr).iterator();
			while(iter.hasNext())
				soortAntwoordVakKeuze.addItem(iter.next());
		}
		else if(setNr == TekstInteractiePanelVak.ESlateSetNr 
			 || setNr == TekstInteractiePanelVak.EpsilonSetNr)
		{
			Iterator iter = Service.getWidgets(setNr).iterator();
			while(iter.hasNext())
				soortAntwoordVakKeuze.addItem(iter.next());
		}
		else if(setNr == TekstInteractiePanelVak.AppletsSetNr)
		{	
		    for(int i=0 ; i<set.length ; i++)//i<widgetSelection.length
            {   //soortAntwoordVakKeuze.addItem(TekstInteractiePanelVak.interactiePanelDescriptions[set[widgetSelection[i]]]);
                soortAntwoordVakKeuze.addItem(TekstInteractiePanelVak.interactiePanelDescriptions[set[i]]);
            }
		}
		else 
        {   
            for(int i=0 ; i<set.length ; i++)
            { soortAntwoordVakKeuze.addItem(TekstInteractiePanelVak.interactiePanelDescriptions[set[i]]);
            }
            
        }
	}
    
    public void setBackground(Color c)
    {	super.setBackground(c);
    	//if(interactieEditPanel!=null)((Component)interactieEditPanel).setBackground(c);
    }

    public void addInteractieEditPanel(Hashtable launchData, int soort)
    
    {
    	if(launchData==null) 
    	{	
    	  if(soort!=999) {
    	    int soortInteractiePanel = TekstInteractiePanelVak.interactiePanelSets[setNr][soort];
            
    	    if(setNr == TekstInteractiePanelVak.AppletsSetNr)
              for(int i=0 ; i<set.length ; i++)
              {    //if(set[widgetSelection[i]]==soortInteractiePanel)soortAntwoordVakKeuze.setSelectedIndex(i+1);
                   if(set[i]==soortInteractiePanel)soortAntwoordVakKeuze.setSelectedIndex(i+1);
              }
            else
              for(int i=0 ; i<set.length ; i++)
              {    if(set[i]==soortInteractiePanel)soortAntwoordVakKeuze.setSelectedIndex(i+1);
                   
              }
    	    
    	    makeInteractieEditPanel(soortInteractiePanel);
            plaatsEditInteractiePanel();
            this.huidigSoortInteractiePanel = soortInteractiePanel;
    	  }
    	  
    	  else  if(soortAntwoordVakKeuze.getModel().getSize()==2)
    		{	soortAntwoordVakKeuze.setSelectedIndex(1);
			}
    		else if(setNr == 3) //Bij Tekstvak is nu keuze tussen tekstvak en symbool. Ik wil dat standaard tekstvak verschijnt, geen keuze.
    			soortAntwoordVakKeuze.setSelectedIndex(1);
    		return;
    	}
		
		int soortInteractiePanel = 0;
		Hashtable interactiePanelLaunchState = null;
        int breedte = 0;
        int hoogte = 0;
        int locationX = 0;
        int locationY = 0;

        boolean volledigeBreedte = false;
        boolean popup = false;
        String popupTitel = "Popup";
        int setNr = 0;
        String popupImageString = "";
        
        if(launchData.containsKey("soortInteractiePanel")) soortInteractiePanel = ((Integer)launchData.get("soortInteractiePanel")).intValue();
        if(launchData.containsKey("interactiePanelLaunchState")) interactiePanelLaunchState = (Hashtable)launchData.get("interactiePanelLaunchState");
        if(launchData.containsKey("breedte")) breedte = ((Integer)launchData.get("breedte")).intValue();
        if(launchData.containsKey("hoogte")) hoogte = ((Integer)launchData.get("hoogte")).intValue();
        if(launchData.containsKey("locationX")) locationX = ((Integer)launchData.get("locationX")).intValue();
        if(launchData.containsKey("locationY")) locationY = ((Integer)launchData.get("locationY")).intValue();
        if(launchData.containsKey("volledigeBreedte")) volledigeBreedte = ((Boolean)launchData.get("volledigeBreedte")).booleanValue();
        if(launchData.containsKey("popup")) popup = ((Boolean)launchData.get("popup")).booleanValue();
        if(launchData.containsKey("popupTitel")) popupTitel = (String)launchData.get("popupTitel");
        if(launchData.containsKey("setNr")) setNr = ((Integer)launchData.get("setNr")).intValue();
        if(launchData.containsKey("popupImageString")) popupImageString = (String)launchData.get("popupImageString");       
// WIM TODO is dit okay?
        if(launchData.containsKey("crossWidgetId")) setCrossWidgetId((String)launchData.get("crossWidgetId"));
        subscriptions = launchData.get("subscriptions"); // do not loose

       if(soortInteractiePanel != -2) 
       {   
         if(setNr == TekstInteractiePanelVak.AppletsSetNr)
           for(int i=0 ; i<set.length ; i++)
           {	//if(set[widgetSelection[i]]==soortInteractiePanel)soortAntwoordVakKeuze.setSelectedIndex(i+1);
                if(set[i]==soortInteractiePanel)soortAntwoordVakKeuze.setSelectedIndex(i+1);
           }
         else
           for(int i=0 ; i<set.length ; i++)
           {    if(set[i]==soortInteractiePanel)soortAntwoordVakKeuze.setSelectedIndex(i+1);
                
           }
         
	     if(interactieEditPanel==null)
	     {	makeInteractieEditPanel(soortInteractiePanel);
	       	plaatsEditInteractiePanel();
	       	this.huidigSoortInteractiePanel = soortInteractiePanel;
	     }
	       
       }
       else 
       { String o = (String)launchData.get(TekstInteractiePanelVak.CBOOKWIDGET_NAME);
        for(int i = set.length; i < soortAntwoordVakKeuze.getItemCount(); i++) {
        	Object item = soortAntwoordVakKeuze.getItemAt(i);
        	String className = item.getClass().getName();
        	if(item instanceof CBookWrap)
        		className = ((CBookWrap) item).getClassName();
        	if(! className.endsWith("]"))
        	{
        		int index = o.indexOf('[');
        		if(index >= 0) o = o.substring(0,index);
        	}
			if(className.equals(o))
        		soortAntwoordVakKeuze.setSelectedIndex(i);
        }}
        
       
        if(interactieEditPanel!=null)
		{	interactieEditPanel.setEditState(interactiePanelLaunchState);
		}
        
        breedteTF.setText(""+breedte);
        hoogteTF.setText(""+hoogte);
        volledigeBreedteCB.setSelected(volledigeBreedte);
        popupCB.setSelected(popup);
        imageButton.setVisible(popup);
	    	popupTitleLabel.setVisible(popup);
	    	popupTitleTF.setVisible(popup);
	    	popupTitleTF.setText(popupTitel);
        //breedteTF.setVisible(!volledigeBreedte);
    	//breedteLabel.setVisible(!volledigeBreedte);
    	breedteTF.setEnabled(!volledigeBreedte);
    	breedteLabel.setEnabled(!volledigeBreedte);
    	
    	this.popupImageString = popupImageString;
    	
    	imageButton.setPopupButtonImage(popupImage);
    	iconman = new Iconan(WiskOpdr.applet, this, TekstImageVak.getImageMap(), TekstImageVak.getImageCache());
    	if(popupImageString!=null && !"".equals(popupImageString)) {
    		popupImage = iconman.getImage(popupImageString);
    		imageButton.setPopupButtonImage(popupImage);
    	}
    	else {
    		imageButton.setCode(TekstInteractiePanelVak.interactiePanelSetNames[setNr]);
    	}
        
    	
    	this.locationX = locationX;
    	this.locationY = locationY;
    	
    	if(interactieEditPanel!=null)interactieEditPanel.zetBreedte(breedte);
    	if(interactieEditPanel!=null)interactieEditPanel.zetHoogte(hoogte);
      
    	pack();
    	
    }
    
    public void packWidth()
    {
    	if(interactieEditPanel!=null) {
    		
	    	int w = ((Component)interactieEditPanel).getWidth();
	    	int h = ((Component)interactieEditPanel).getPreferredSize().height;
	    	((Component)interactieEditPanel).setPreferredSize(new Dimension(w,h));
	    	super.pack();
	    	((Component)interactieEditPanel).setPreferredSize(null);
    	}
    }
    public void packWidth(int maxWidth)
    {
    	if(interactieEditPanel!=null) {
	    	int w = ((Component)interactieEditPanel).getWidth();
	    	int h = ((Component)interactieEditPanel).getPreferredSize().height;
	    	((Component)interactieEditPanel).setPreferredSize(new Dimension(w,h));
	    	this.setPreferredSize(new Dimension(Math.min(getPreferredSize().width, maxWidth), getPreferredSize().height));
	    	super.pack();
	    	((Component)interactieEditPanel).setPreferredSize(null);
	    	this.setPreferredSize(null);
	    	
    	}
    }
    
    public void pack()
    {
    		if(interactieEditPanel!=null) {
	    		int w =((Component)interactieEditPanel).getPreferredSize().width;
	    	 	int h = ((Component)interactieEditPanel).getPreferredSize().height;
	    		if(h==1) {
	    			if(setNr==3) {
	    				w=900; h=610;
	    			}
	    			else if(setNr==2) {
	    				w=880; h=500;
	    			}
	    			else {
	    				w=800; h=500;
	    			}
	    			((Component)interactieEditPanel).setPreferredSize(new Dimension(w,h));
	    		}
    		Dimension screenSize = WiskOpdr.applet.getToolkit().getScreenSize();
    		System.out.println(""+((Component)interactieEditPanel).getPreferredSize());
    		System.out.println(""+((Component)interactieEditPanel).getPreferredSize().width);
    	  	int wBottom = bottomPanel.getPreferredSize().width;
	    	w = Math.max(wBottom,w);
	    
	    //	System.out.println("insets:"+this.getInsets().top);
	    	int widthThis = 40+w+(helpBox.isVisible() ? 300 : 0);
	    	int heightThis = getInsets().top + 123 + h;
	    	
	    	widthThis = Math.min(widthThis, screenSize.width);
	    	heightThis = Math.min(heightThis, screenSize.height);
	    	
	    	//int helpWidth = Math.min((helpBox.isVisible() ? w+320 : w), screenSize.width);
	    	//((Component)interactieEditPanel).setPreferredSize(new Dimension(Math.min(screenSize.width-320,  helpWidth),h));
	    	
	    	super.pack();
	    	this.setSize(widthThis, heightThis);
	    	 int x = (screenSize.width-widthThis)/2;
             int y = (screenSize.height-heightThis)/2;
             this.setLocation(x , y);
             
	            ((Component)interactieEditPanel).setPreferredSize(null);
    	}
    	else
    		super.pack();
    }
    
    private void plaatsEditInteractiePanel()
    {
    	if(interactieEditPanel!=null)
		{	((Component)interactieEditPanel).setBackground(getBackground());
			
			mainPanel.removeAll();
			mainPanel.add((Component)interactieEditPanel);
			if(interactieEditPanel!=null && ((Component)interactieEditPanel).getPreferredSize().height<100) {
				((Component)interactieEditPanel).setPreferredSize(new Dimension(800,500));
				if(setNr==3)
					((Component)interactieEditPanel).setPreferredSize(new Dimension(900,610));
				if(setNr==2)
					((Component)interactieEditPanel).setPreferredSize(new Dimension(880,500));
			}
			((Component) interactieEditPanel).setBounds(-1,-1,getSize().width,getSize().height-110);
			
			if(interactieEditPanel!=null) (interactieEditPanel).start();
		    imageButton.setCode(TekstInteractiePanelVak.interactiePanelSetNames[setNr]);
		}
    	
    	pack();
    }
    
    public void makeInteractieEditPanel(int soortInteractiePanel)
    {  	FontMetrics fm = getFontMetrics(WiskOpdr.formuleFont0);
		int h = fm.getAscent()+fm.getDescent() + 8;
	
    	if(soortInteractiePanel == -1)
		{	interactieEditPanel = null;
		}
    	else if(soortInteractiePanel == 0)
		{	interactieEditPanel = (new AntwoordFormuleVak()).getEditPanel();
			getCrossWidgetId(); // in case of logging.
			breedteTF.setText("300");
	        hoogteTF.setText("250");
		}
		else if(soortInteractiePanel == 1)
		{	interactieEditPanel = (new AntwoordVergelijkingVak()).getEditPanel();
			getCrossWidgetId();
			breedteTF.setText("300");
			hoogteTF.setText("250");
		}
		else if(soortInteractiePanel == 2)
		{	interactieEditPanel = (new SimpelAntwoordFormuleVak()).getEditPanel();
			getCrossWidgetId(); // in case of logging.
			breedteTF.setText("50");
			hoogteTF.setText(""+h);
		}
		else if(soortInteractiePanel == 3)
		{	interactieEditPanel = (new SimpelAntwoordVergelijkingVak()).getEditPanel();
			getCrossWidgetId(); // in case of logging.
			breedteTF.setText("50");
			hoogteTF.setText(""+h);
		}
		else if(soortInteractiePanel == 4)
		{	interactieEditPanel = (new TekstEditor(true,true,true)).getEditPanel();
			getCrossWidgetId(); // in case of logging.
			breedteTF.setText("300");
			hoogteTF.setText("250");
			if(interactieEditPanel!=null)interactieEditPanel.zetBreedte(300);
	    	if(interactieEditPanel!=null)interactieEditPanel.zetHoogte(250);
		}
		else if(soortInteractiePanel == 8)
		{	interactieEditPanel = (new GrafiekPanel()).getEditPanel();
			breedteTF.setText("300");
			hoogteTF.setText("400");
			if(interactieEditPanel!=null)interactieEditPanel.zetBreedte(300);
	    	if(interactieEditPanel!=null)interactieEditPanel.zetHoogte(400);
		}
		else if(soortInteractiePanel == 9)
		{	interactieEditPanel = (new TekstVakPanel(manager)).getEditPanel();
			breedteTF.setText("50");
			hoogteTF.setText("16");
			if(interactieEditPanel!=null)interactieEditPanel.zetBreedte(50);
	    	if(interactieEditPanel!=null)interactieEditPanel.zetHoogte(16);
	    	((TekstVakEditPanel) interactieEditPanel).addActionListener(this);
	    	mainPanel.setBorder(BorderFactory.createEmptyBorder(5, 20, 20, 20));
		}
		else if(soortInteractiePanel == 10)
		{	interactieEditPanel = Geogebra3Panel.newEditPanel(getCrossWidgetId());
			breedteTF.setText("800");
			hoogteTF.setText("500");
			if(interactieEditPanel!=null)interactieEditPanel.zetBreedte(800);
	    	if(interactieEditPanel!=null)interactieEditPanel.zetHoogte(500);
		}
		else if(soortInteractiePanel == 39)
		{	interactieEditPanel = GeogebraPanel.newEditPanel(getCrossWidgetId());
			breedteTF.setText("800");
			hoogteTF.setText("500");
			if(interactieEditPanel!=null)interactieEditPanel.zetBreedte(800);
	    	if(interactieEditPanel!=null)interactieEditPanel.zetHoogte(500);
		}
		else if(soortInteractiePanel == 12)
		{	interactieEditPanel = (new CheckUnitPanel()).getEditPanel();
			breedteTF.setText("126");
			hoogteTF.setText("31");
			if(interactieEditPanel!=null)interactieEditPanel.zetBreedte(126);
	    	if(interactieEditPanel!=null)interactieEditPanel.zetHoogte(31);
		}
		else if(soortInteractiePanel == 13)
		{	interactieEditPanel = (new AntwoordTekstVak()).getEditPanel();
			getCrossWidgetId(); // in case of logging.
			breedteTF.setText("50");
			hoogteTF.setText(""+h);
			//if(interactieEditPanel!=null)interactieEditPanel.zetBreedte(50);
	    	//if(interactieEditPanel!=null)interactieEditPanel.zetHoogte(h);
		}
		else if(soortInteractiePanel == 14)
		{	interactieEditPanel = (new AntwoordKeuzeVak()).getEditPanel();
			breedteTF.setText("110");
			hoogteTF.setText("24");
			getCrossWidgetId(); // in case of logging.
			if(interactieEditPanel!=null)interactieEditPanel.zetBreedte(110);
	    	if(interactieEditPanel!=null)interactieEditPanel.zetHoogte(24);
		}
		else if(soortInteractiePanel == 16)
		{	interactieEditPanel = (new CheckSleepUnitPanel()).getEditPanel();
			breedteTF.setText("126");
	        hoogteTF.setText("31");
			if(interactieEditPanel!=null)interactieEditPanel.zetBreedte(126);
	    	if(interactieEditPanel!=null)interactieEditPanel.zetHoogte(31);
		}
		else if(soortInteractiePanel == 25)
		{	interactieEditPanel = (new GetallenlijnSprongPanel()).getEditPanel();
			breedteTF.setText("40");
			hoogteTF.setText("300");
			if(interactieEditPanel!=null)interactieEditPanel.zetBreedte(40);
	    	if(interactieEditPanel!=null)interactieEditPanel.zetHoogte(300);
	    }
		else if(soortInteractiePanel == 33)
        {   interactieEditPanel = (new CheckValueUnitPanel()).getEditPanel();
	        breedteTF.setText("126");
	        hoogteTF.setText("31");
            if(interactieEditPanel!=null)interactieEditPanel.zetBreedte(126);
            if(interactieEditPanel!=null)interactieEditPanel.zetHoogte(31);
        }
		else if(soortInteractiePanel == 49)
        {   interactieEditPanel = (new CheckButtonPanel()).getEditPanel();
            breedteTF.setText("126");
            hoogteTF.setText("31");
            if(interactieEditPanel!=null)interactieEditPanel.zetBreedte(126);
            if(interactieEditPanel!=null)interactieEditPanel.zetHoogte(31);
        }
		else if(soortInteractiePanel == 52)
        {   interactieEditPanel = (new ReactieVergelijkingVak()).getEditPanel();
            breedteTF.setText("300");
            hoogteTF.setText("150");
            if(interactieEditPanel!=null)interactieEditPanel.zetBreedte(300);
            if(interactieEditPanel!=null)interactieEditPanel.zetHoogte(150);
        }
    	
		else if(soortInteractiePanel == 53)
		{   interactieEditPanel = (new StelselAntwoordVak()).getEditPanel();
	        breedteTF.setText("300");
	        hoogteTF.setText("250");
	        if(interactieEditPanel!=null)interactieEditPanel.zetBreedte(300);
	        if(interactieEditPanel!=null)interactieEditPanel.zetHoogte(250);
	    }
		else if(soortInteractiePanel == 55)
		{
			interactieEditPanel = (new SymboolPanel()).getEditPanel();
			breedteTF.setText("100");
			hoogteTF.setText("100");
			if(interactieEditPanel != null)interactieEditPanel.zetBreedte(100);
			if(interactieEditPanel != null)interactieEditPanel.zetHoogte(100);
			
		}
		else if(soortInteractiePanel == 60)
		{
		  interactieEditPanel = (new SamengesteldeStappenPanel()).getEditPanel();
		  breedteTF.setText("400");
		  hoogteTF.setText("300");
		  if(interactieEditPanel != null) interactieEditPanel.zetBreedte(400);
		  if(interactieEditPanel != null) interactieEditPanel.zetHoogte(57);
		}
		else if(soortInteractiePanel == 63)
		{
		  interactieEditPanel = (new BerekeningVak()).getEditPanel();
		  breedteTF.setText("100");
		  hoogteTF.setText("24");
		  if(interactieEditPanel != null) interactieEditPanel.zetBreedte(100);
		  if(interactieEditPanel != null) interactieEditPanel.zetHoogte(24);
		}
		else if(soortInteractiePanel == 64)
		{
		  interactieEditPanel = (new StrategieVakPanel()).getEditPanel();
		  breedteTF.setText("400");
		  hoogteTF.setText("57");
		  if(interactieEditPanel != null) interactieEditPanel.zetBreedte(400);
		  if(interactieEditPanel != null) interactieEditPanel.zetHoogte(57);
		}
		else if(soortInteractiePanel == 66)
        {
          interactieEditPanel = (new ScoreWidget()).getEditPanel();
          breedteTF.setText("20");
          hoogteTF.setText("20");
          if(interactieEditPanel != null) interactieEditPanel.zetBreedte(20);
          if(interactieEditPanel != null) interactieEditPanel.zetHoogte(20);
        }
		else if(soortInteractiePanel == 67)
        {
          interactieEditPanel = (new LeerdoelWidget()).getEditPanel();
          breedteTF.setText("400");
          hoogteTF.setText("400");
          if(interactieEditPanel != null) interactieEditPanel.zetBreedte(400);
          if(interactieEditPanel != null) interactieEditPanel.zetHoogte(400);
        }
		else if(soortInteractiePanel == 45)//GraphTool
		{  		//if((""+soortInteractiePanel).equals(TekstInteractiePanelVak.wiskOpdrInteractiePanels[30][1]))
				{	interactieEditPanel = maakInteractieEditPanel(TekstInteractiePanelVak.wiskOpdrInteractiePanels[30][0], WiskOpdr.language);
					breedteTF.setText("300");
					hoogteTF.setText("400");
					if(interactieEditPanel != null)interactieEditPanel.zetBreedte(300);
					if(interactieEditPanel != null)interactieEditPanel.zetHoogte(400);
					
				}
				
			
			//interactieEditPanel.start();
		}
		else if(soortInteractiePanel > 4)
		{  	for(int i=0 ; i<TekstInteractiePanelVak.wiskOpdrInteractiePanels.length ; i++)
			{	System.out.println(""+i);
				System.out.println(""+soortInteractiePanel);
				//System.out.println(""+TekstInteractiePanelVak.interactiePanelSets[1][i]);
				if((""+soortInteractiePanel).equals(TekstInteractiePanelVak.wiskOpdrInteractiePanels[i][1]))
				{	interactieEditPanel = maakInteractieEditPanel(TekstInteractiePanelVak.wiskOpdrInteractiePanels[i][0], WiskOpdr.language);
					breedteTF.setText("500");
					hoogteTF.setText("450");
					break;
				}
				
			}
            switch(soortInteractiePanel) { 
              case 62: getCrossWidgetId(); break; // NEEDS XWIDGETID
            }
			//interactieEditPanel.start();
		}
    	if(interactieEditPanel!=null) 
    	{	((Component)interactieEditPanel).setBackground(getBackground());
    		widthEditPanel = ((Component)interactieEditPanel).getPreferredSize().width;
    	}
    	
    	
    }
    
    @SuppressWarnings("unchecked")
	private InteractieEditPanel maakInteractieEditPanel(String name, Locale language)
	{
		try
		{	
			Class<WiskOpdrApplet> c = TekstInteractiePanelVak.classMap.get(name);
			if( c == null)
			{	
				c = (Class<WiskOpdrApplet>) Loader.create(TekstInteractiePanelVak.jarOf(name), getClass().getClassLoader()).loadClass(name);
				TekstInteractiePanelVak.classMap.put(name, c);
			}
			Constructor<WiskOpdrApplet> cc = c.getDeclaredConstructor(new Class[] { Locale.class } );
		    WiskOpdrApplet o = cc.newInstance(new Object[] { language } );
		    Stub.setStub(o);
			return o.getInteractiePanel().getEditPanel();
		}
		catch(Exception e)
		{	e.printStackTrace(System.out);
			return null;
		}
	}
    
    /*public static Hashtable editInteractiePanel(Hashtable launchData) {
        return editInteractiePanel(null, launchData);
    }

    public static Hashtable editInteractiePanel(Component owner, Hashtable launchData)  {
        
                
    	EditInteractiePanelDialog asd = new EditInteractiePanelDialog(owner, "Edit", launchData);
        asd.show();
        if (asd.isConfirmed()) {
        	Hashtable newLaunchData = asd.getEditState();
            if(newLaunchData == null) { //something went wrong, reshow the dialog
            	newLaunchData = editInteractiePanel(owner, launchData);
            }
            return newLaunchData;
        } else { //action canceled
            return null;
        }
    }*/
    
    public Hashtable getEditState() {
    	
    	return shareAction.wrap(getEditState_int());
    }
    
    Hashtable getEditState_int()
    {  	
    	Hashtable interactiePanelLaunchState = null;
		int soortInteractiePanel = 0;
		int breedte = 0;
        int hoogte = 0;
        int locationX = 0;
        int locationY = 0;
		boolean volledigeBreedte = false;
		boolean popup = false;
		String popupTitel = "Popup";
		int setNr = 0;
		String popupImageString = "";
		
		if(interactieEditPanel==null) interactiePanelLaunchState = new Hashtable();
		else interactiePanelLaunchState = interactieEditPanel.getEditState();
		int selectNr = soortAntwoordVakKeuze.getSelectedIndex()-1;
		Object selectObj = soortAntwoordVakKeuze.getSelectedItem();
		soortInteractiePanel = -1;
		String soortInteractiePanelClass = "";
		if(selectNr >-1 ){
			if(selectObj instanceof CBookWidgetIF) {
				soortInteractiePanel = TekstInteractiePanelVak.CBOOKWIDGET;
				soortInteractiePanelClass = selectObj.getClass().getName();
				if(selectObj instanceof CBookWrap)
					soortInteractiePanelClass = ((CBookWrap) selectObj).getClassName();
				
			} 
			else if(this.setNr==TekstInteractiePanelVak.AppletsSetNr) {			
				//soortInteractiePanel = set[widgetSelection[selectNr]];
				soortInteractiePanel = set[selectNr];
				System.out.println("wasHere: nr="+soortInteractiePanel);
			}
			else
			    soortInteractiePanel = set[selectNr];
			  
		}
		else 
		  soortInteractiePanel = this.huidigSoortInteractiePanel;
		//System.out.println(soortInteractiePanel + " " + soortInteractiePanelClass);
		
		
		breedte = new Integer(breedteTF.getText()).intValue();
        hoogte = new Integer(hoogteTF.getText()).intValue();
        if(soortInteractiePanel == 9) //TekstVakPanel
        {	if(((TekstVakEditPanel)interactieEditPanel).breedteDwingend())
        	{	breedte = ((TekstVakEditPanel)interactieEditPanel).geefTekstVakPanelBreedte();
        		breedteTF.setText(""+breedte);
        	}
	        if(((TekstVakEditPanel)interactieEditPanel).hoogteDwingend())
        	{	hoogte = ((TekstVakEditPanel)interactieEditPanel).geefTekstVakPanelHoogte();
        		hoogteTF.setText(""+hoogte);
        	}
        }
        volledigeBreedte = volledigeBreedteCB.isSelected();
        popup = popupCB.isSelected();
        popupTitel = popupTitleTF.getText();
        setNr = this.setNr;
        locationX = this.locationX;
        locationY = this.locationY;
        popupImageString = this.popupImageString;
           	
    	Hashtable h = new Hashtable();
		h.put("interactiePanelLaunchState", interactiePanelLaunchState);
		h.put("soortInteractiePanel", new Integer(soortInteractiePanel));
		h.put(TekstInteractiePanelVak.CBOOKWIDGET_NAME, soortInteractiePanelClass);
        h.put("breedte", new Integer(breedte));
        h.put("hoogte", new Integer(hoogte));
        h.put("locationX", new Integer(locationX));
        h.put("locationY", new Integer(locationY));
        h.put("volledigeBreedte", new Boolean(volledigeBreedte));
        h.put("popup", new Boolean(popup));
        h.put("popupTitel", popupTitel);
        h.put("setNr", new Integer(setNr));
        h.put("popupImageString", popupImageString);
// if Logging, enable xwid for LA transport
// if StudentModel data, enable xwid for XAPI transport
        if(Boolean.TRUE.equals(interactiePanelLaunchState.get(Constants.LOGGING))
            || interactiePanelLaunchState.containsKey(fi.wiskopdr.domainmodel.Constants.OBJECTIVES))
        {
        	getCrossWidgetId();
        }
        if(crossWidgetId != null)
        	h.put("crossWidgetId", crossWidgetId);
        if(subscriptions != null) 
        	h.put("subscriptions", subscriptions);
        System.out.println("LaunchState: "+interactiePanelLaunchState.toString());
        
		return h;
    }

    public void editImage() {
    	if(iconman==null)
			iconman = new Iconan(WiskOpdr.applet, this, TekstImageVak.getImageMap(), TekstImageVak.getImageCache());
		iconman.editImage(popupImageString, this, this);
		
//        if(imageDialog == null)
//        {
//            //Frame f = JOptionPane.getFrameForComponent(this);
//            imageDialog = new Dialog(this,"title", true);
//            imageDialog.setLayout(new BorderLayout());
//            iconman = new Iconan(WiskOpdr.applet, (Component)this, (Hashtable)TekstImageVak.getImageMap());
//            imageDialog.add(iconman);
//            imageDialog.pack();
//            iconman.addActionListener(this);
//        }
//        iconman.select(popupImageString);
//        imageDialog.show();
    }
   
    /*
     * (non-Javadoc)
     * 
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == cancelButton) {
        	produceAction("cancel");
            this.setVisible(false);
            this.dispose();
            //OpdrNavStructEdit.helpBrowser.loadURL(null);
            
        } 
        else if (e.getSource() == okButton) {
        	confirmed = true;
        	produceAction("ok");
        	WiskOpdr.setLaunchDataChanged();
            this.setVisible(false);
            this.dispose();
            //OpdrNavStructEdit.helpBrowser.loadURL(null);
            
        }
        else if (e.getSource() == helpButton && interactieEditPanel instanceof HelpButtonPanelIF) {
        	//if(interactieEditPanel!=null && interactieEditPanel instanceof AntwoordVergelijkingVakEditPanel) 
			//{
        		//((AntwoordVergelijkingVakEditPanel)interactieEditPanel).showHelp(true);
        	if(interactieEditPanel!=null && interactieEditPanel instanceof HelpButtonPanelIF) 
        		((HelpButtonPanelIF)interactieEditPanel).showHelpButtons(!helpBox.isVisible());
        		
    		helpBox.setVisible(!helpBox.isVisible());
    		helpTitelBox.setVisible(helpBox.isVisible());
    		if(helpBox.isVisible()) {
    			 
    			helpBox.validate();
    			OpdrNavStructEdit.helpBrowser.loadURL(((HelpButtonPanelIF)interactieEditPanel).geefHelpURL());
            	
            	//packWidth(1100);
    			pack();
    		}
    		else {
    			helpBox.validate(); 
    			//OpdrNavStructEdit.helpBrowser.loadURL(null);
            	
    			pack();	
    		}	
			//}
            
        }
        else if (e.getSource() == volledigeBreedteCB) {
        	boolean b = volledigeBreedteCB.isSelected();
        	//breedteTF.setVisible(!b);
        	//breedteLabel.setVisible(!b);
        	breedteTF.setEnabled(!b);
        	breedteLabel.setEnabled(!b);
        	if(!b && interactieEditPanel != null)
        		interactieEditPanel.zetBreedte(Integer.parseInt(breedteTF.getText()));
        }
        else if(e.getSource()==soortAntwoordVakKeuze)
		{	Hashtable oldInteractiePanelLaunchState = null;
		 	int oldSoortInteractiePanel = huidigSoortInteractiePanel;
        	if(interactieEditPanel!=null) 
			{	Hashtable h = getEditState();
				if(h.containsKey("interactiePanelLaunchState")) oldInteractiePanelLaunchState = (Hashtable)h.get("interactiePanelLaunchState");
				
				getContentPane().remove((Component)interactieEditPanel);
			}
			int selectNr = soortAntwoordVakKeuze.getSelectedIndex()-1;
			Object o = soortAntwoordVakKeuze.getSelectedItem();
			int soortInteractiePanel = -1;
			if(o instanceof CBookWidgetIF)
			{
				makeInteractieEditPanel( (CBookWidgetIF) o, getCrossWidgetId());
			} 
			else if(setNr == TekstInteractiePanelVak.AppletsSetNr)
			{	
				//if(selectNr >-1 )soortInteractiePanel = set[widgetSelection[selectNr]];
				if(selectNr >-1 )soortInteractiePanel = set[selectNr];
				makeInteractieEditPanel(soortInteractiePanel);
			} 
			else 
            {   
                if(selectNr >-1 )soortInteractiePanel = set[selectNr];
                makeInteractieEditPanel(soortInteractiePanel);
            }
			plaatsEditInteractiePanel();
			huidigSoortInteractiePanel = soortInteractiePanel;
			if(oldInteractiePanelLaunchState!=null)
			{	
				System.out.println("oldSoortInteractiePanel: "+oldSoortInteractiePanel);
				System.out.println("soortInteractiePanel: "+soortInteractiePanel);
				Hashtable compatibleLaunchState = null;
				boolean compatibleFormuleVak = 	oldSoortInteractiePanel==0 && soortInteractiePanel==2 || oldSoortInteractiePanel==2 && soortInteractiePanel==0 ;
				boolean compatibleVergelijkingVak = oldSoortInteractiePanel==1 && soortInteractiePanel==3 || oldSoortInteractiePanel==3 && soortInteractiePanel==1 ;
				
				if(compatibleFormuleVak)
					compatibleLaunchState = ((AntwoordFormuleVakEditPanel)interactieEditPanel).changeToCompatibleEditState(oldInteractiePanelLaunchState);
				if(compatibleVergelijkingVak)
					compatibleLaunchState = ((AntwoordVergelijkingVakEditPanel)interactieEditPanel).changeToCompatibleEditState(oldInteractiePanelLaunchState);
				if(compatibleLaunchState!=null)	
				{	interactieEditPanel.setEditState(compatibleLaunchState);
					System.out.println("compatibleLaunchState: "+compatibleLaunchState.toString());
				}
			}
		}
        else if (e.getSource() == breedteTF) {
        	if(interactieEditPanel!=null) interactieEditPanel.zetBreedte(Integer.parseInt(breedteTF.getText()));
        }
        else if (e.getSource() == hoogteTF) {
        	if(interactieEditPanel!=null) interactieEditPanel.zetHoogte(Integer.parseInt(hoogteTF.getText()));
        }
        else if (e.getSource() == popupCB) {
	        	imageButton.setVisible(popupCB.isSelected());
	        	popupTitleLabel.setVisible(popupCB.isSelected());
	        	popupTitleTF.setVisible(popupCB.isSelected());
	        pack();
        }
        else if(e.getSource()==imageButton)
        {   editImage();
            
        }
        else if(e.getSource()==iconman)
        {
            String name = e.getActionCommand();
            if(!"".equals(name))
            {
                popupImageString = name;
                this.popupImage = iconman.getImage(name);
                imageButton.setPopupButtonImage(popupImage);
                repaint();
            }
        }
        else if(e.getSource()==interactieEditPanel && e.getActionCommand().equals("pasMaatAan"))
        {
        	interactieEditPanel.zetHoogte(Integer.parseInt(hoogteTF.getText()));
        	interactieEditPanel.zetBreedte(Integer.parseInt(breedteTF.getText()));
        	
        }
        if(e.getSource() == hideHelpButton) {
			helpBox.setVisible(false);
			helpTitelBox.setVisible(false);
			if(interactieEditPanel!=null && interactieEditPanel instanceof HelpButtonPanelIF) 
        		((HelpButtonPanelIF)interactieEditPanel).showHelpButtons(false);
			//OpdrNavStructEdit.helpBrowser.loadURL(null);
			pack();
		}

        if(imageDialog!=null)
            imageDialog.hide();
	}
        

    
    
    private void makeInteractieEditPanel(CBookWidgetIF o, String uuid) {
    	
		CBookInteractieEditPanel p;
		interactieEditPanel = p = new CBookInteractieEditPanel(o, WiskOpdr.language, uuid);
// set initial size of widget
		Dimension size = p.getInstanceSize();
		breedteTF.setText( Integer.toString(size.width));
		hoogteTF.setText(Integer.toString(size.height));
		
		p.addPropertyChangeListener("instanceSize", new PropertyChangeListener() {

			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				Dimension size = (Dimension) evt.getNewValue();
				breedteTF.setText( Integer.toString(size.width));
				hoogteTF.setText(Integer.toString(size.height));			
			}});
		
		
	}

	/*public void itemStateChanged(ItemEvent e)
	{
    	if(e.getSource()==soortAntwoordVakKeuze)
		{	if(interactieEditPanel!=null) remove((Component)interactieEditPanel);
    		int soortInteractiePanel = soortAntwoordVakKeuze.getSelectedIndex()-1;
			makeInteractieEditPanel(soortInteractiePanel);
			if(interactieEditPanel!=null)
			{	interactieEditPanel.setBounds(10,20,780,520);
				add((Component)interactieEditPanel);
				if(interactieEditPanel!=null)((Component)interactieEditPanel).doLayout();//if(interactieEditPanel!=null) interactieEditPanel.start();
			    if(interactieEditPanel!=null) ((Component)interactieEditPanel).repaint();
			    System.out.println("layout");
			}
			pack();
			setSize(800,620);
		}
	}*/

    public int geefHoogte()
    {
    	return Integer.parseInt(hoogteTF.getText());
    }
    
    public int geefBreedte()
    {
    	return Integer.parseInt(breedteTF.getText());
    }
    
    public void focusLost(FocusEvent e)
    {
    	if (e.getSource() == breedteTF) {
        	if(interactieEditPanel!=null) interactieEditPanel.zetBreedte(Integer.parseInt(breedteTF.getText()));
        }
        else if (e.getSource() == hoogteTF) {
        	if(interactieEditPanel!=null) interactieEditPanel.zetHoogte(Integer.parseInt(hoogteTF.getText()));
        }
    }
    
    public void focusGained(FocusEvent e)
    {
    	
    }
    /**
     * Invoked when the window is set to be the user's active window, which
     * means the window (or one of its subcomponents) will receive keyboard
     * events.
     * 
     * @param e
     *            The WindowEvent.
     * @see java.awt.event.WindowListener#windowActivated(java.awt.event.WindowEvent)
     */
    public void windowActivated(WindowEvent e) {
    }

    /**
     * Invoked when a window has been closed as the result of calling dispose on
     * the window.
     * 
     * @param e
     *            The WindowEvent.
     * @see java.awt.event.WindowListener#windowClosed(java.awt.event.WindowEvent)
     */
    public void windowClosed(WindowEvent e) {
    	 //OpdrNavStructEdit.helpBrowser.loadURL(null);
    }

    public void windowClosing(WindowEvent e) {
    	produceAction("cancel");
    	
        setVisible(false);
        dispose();
    }

    public void windowDeactivated(WindowEvent e) {
    }

    public void windowDeiconified(WindowEvent e) {
    }

    public void windowIconified(WindowEvent e) {
    }

    public void windowOpened(WindowEvent e) {
    }

    public boolean isConfirmed() {
        return confirmed;
    }
    
  //ActionProducer
	private ActionListener actionListener = null;
	private ShareAction shareAction = new ShareAction(new ImageIcon(WiskOpdr.loadImage("resources/unshare.png")),this);
	
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
 	//end ActionProducer

	@Override
	public XWidgetManager getXWidgetManager() {
		return manager;
	}

	public String getCrossWidgetId() {
		if(crossWidgetId == null && manager != null)
		{
			manager.newCrossWidgetId(this);
		}
		return crossWidgetId;
	}
	
	public String getCrossWidgetId0() {
		return crossWidgetId;
	}

	public void setCrossWidgetId(String crossWidgetId) {
		this.crossWidgetId = crossWidgetId;
	}

	public void stop() {
      if (iconman != null)
        iconman.dispose(); // cleanup
	  if (interactieEditPanel != null)
		interactieEditPanel.stop();
	}

  @Override
  public void dispose() {
    if (iconman != null) iconman.dispose();
    if (imageDialog != null) imageDialog.dispose();
    super.dispose();
  }
    
    
}