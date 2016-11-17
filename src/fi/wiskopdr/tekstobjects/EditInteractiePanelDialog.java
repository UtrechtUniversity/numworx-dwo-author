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
import fi.wiskopdr.CheckButtonPanel;
import fi.wiskopdr.CheckSleepUnitPanel;
import fi.wiskopdr.CheckUnitPanel;
import fi.wiskopdr.CheckValueUnitPanel;
import fi.wiskopdr.Geogebra3Panel;
import fi.wiskopdr.GeogebraPanel;
import fi.wiskopdr.GetallenlijnSprongPanel;
import fi.wiskopdr.GrafiekPanel;
import fi.wiskopdr.SimpelAntwoordFormuleVak;
import fi.wiskopdr.SimpelAntwoordVergelijkingVak;
import fi.wiskopdr.TabletOwningLayeredPane;
import fi.wiskopdr.TekstVakEditPanel;
import fi.wiskopdr.TekstVakPanel;
import fi.wiskopdr.WiskOpdr;
import fi.wiskopdr.cbook.CBookInteractieEditPanel;
import fi.wiskopdr.cbook.CBookWrap;
import fi.wiskopdr.cbook.Service;
import fi.wiskopdr.formuleobjects.FormuleButton;
//import fi.tekenveelvlakopdr.TekenVeelvlakOpdr;
//import fi.mozarch.MozArch;
import fi.wiskopdr.opdrnav.XWidgetManager;
import fi.wiskopdr.scheikundeobjects.ReactieVergelijkingVak;
import fi.wiskopdr.stelselsvergelijkingen.StelselAntwoordVak;
import fi.wiskopdr.symbolen.SymboolPanel;


public class EditInteractiePanelDialog extends JDialog implements ActionListener,  WindowListener , FocusListener, XWidgetManager.HasWidgetManager
{

	private InteractieEditPanel interactieEditPanel;
	private int huidigSoortInteractiePanel = -1;
	private JButton okButton;
    private JButton cancelButton;
    private JButton shareButton;
    JComboBox soortAntwoordVakKeuze;
    private JTextField breedteTF, hoogteTF;
    private JLabel breedteLabel, hoogteLabel;
    private JCheckBox volledigeBreedteCB, popupCB;
    
    private Font font = new Font("SansSerif",Font.PLAIN,12);//WiskOpdr.tekstFont;
    private int widthEditPanel = 0;
    
    private FormuleButton imageButton;
    private Dialog imageDialog;
    private Iconan iconman;
    private String popupImageString = "";
    private Image popupImage;
    boolean confirmed;
    
    int[] set;
    int setNr;
    
    //bij zwevend TeksVakPanel
    private int locationX;
    private int locationY;
    
    private Dimension size;
	private String crossWidgetId;
	private XWidgetManager manager;
	Object subscriptions; // Not to loose. 

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
    
//    public EditInteractiePanelDialog(Dialog owner, String windowTitle, boolean modal, int setNr,  Hashtable launchData, XWidgetManager manager) {
//    	super(owner, windowTitle, modal);
//    	initEditInteractiePanelDialog(setNr, launchData, manager);
//    }

    void initEditInteractiePanelDialog(int setNr,  Hashtable launchData, XWidgetManager manager) {
    	Container content = getContentPane();
    	setLayeredPane(new TabletOwningLayeredPane());
    	setContentPane(content);
    	
        content.setLayout(new BorderLayout());
        //this.setBackground(Color.red);//new Color(230,230,230));
        
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(null);
        bottomPanel.setPreferredSize(new Dimension(1000,100));
               
        this.setNr = setNr;
        this.manager = manager;
        confirmed = false;
        
        size = new Dimension(800,650); // Default value
        if(setNr==4 || setNr==3) {
        	size = new Dimension(1000,700);
        } 
        else {
        	if (setNr == 2) {  // GraphTool
            	size = new Dimension(900,700);
        	}
        }

        bottomPanel.setPreferredSize(new Dimension(1000,60));

        okButton = new JButton(WiskOpdr.rb.getString("okKnopLabel"));
        okButton.setBounds(30,10,65,20);
        okButton.setFont(font);
        okButton.addActionListener(this);
        bottomPanel.add(okButton);
        
        cancelButton = new JButton(WiskOpdr.rb.getString("annuleerKnopLabel"));
        cancelButton.setBounds(110,10,65,20);
        cancelButton.setMargin(new Insets(4,10,4,10));
        cancelButton.setFont(font);
        cancelButton.addActionListener(this);
        bottomPanel.add(cancelButton);
        
        soortAntwoordVakKeuze = new JComboBox();
		soortAntwoordVakKeuze.setBounds(190,10,165,24);
		soortAntwoordVakKeuze.setFont(font);
		soortAntwoordVakKeuze.addActionListener(this);
		bottomPanel.add(soortAntwoordVakKeuze);
		
		
		initSet(setNr);
		
		/*
		soortAntwoordVakKeuze.addItem("Kies soort interactievak");
		soortAntwoordVakKeuze.addItem("Formulevak met stappen");
		soortAntwoordVakKeuze.addItem("Vergelijkingvak met stappen");
		soortAntwoordVakKeuze.addItem("Simpel formulevak");
		soortAntwoordVakKeuze.addItem("Simpel vergelijkingvak");
		soortAntwoordVakKeuze.addItem("Tekstvak");
		
		
		for(int i=0 ; i<TekstInteractiePanelVak.wiskOpdrInteractiePanels.length ; i++)
		{	soortAntwoordVakKeuze.addItem(TekstInteractiePanelVak.wiskOpdrInteractiePanels[i][1]);
		}
		*/
		
		breedteLabel = new JLabel(WiskOpdr.rb.getString("breedteLabel"));
        breedteLabel.setBounds(370,10,50,20);
        breedteLabel.setFont(font);
        bottomPanel.add(breedteLabel,0);
        
        breedteTF = new JTextField("300");
        breedteTF.setBounds(420,10,40,20);
        breedteTF.setFont(font);
        breedteTF.addActionListener(this);
        breedteTF.addFocusListener(this);
        
        bottomPanel.add(breedteTF,0);
        
        hoogteLabel = new JLabel(WiskOpdr.rb.getString("hoogteLabel"));
        hoogteLabel.setBounds(465,10,50,20);
        hoogteLabel.setFont(font);
        bottomPanel.add(hoogteLabel,0);
        
        hoogteTF = new JTextField("250");
        hoogteTF.setBounds(515,10,40,20);
        hoogteTF.setFont(font);
        hoogteTF.addActionListener(this);
        hoogteTF.addFocusListener(this);
        
        bottomPanel.add(hoogteTF,0);
        
        volledigeBreedteCB = new JCheckBox(WiskOpdr.rb.getString("volleBreedteLabel"));
        volledigeBreedteCB.setOpaque(false);
        volledigeBreedteCB.setFont(font);
        volledigeBreedteCB.setBounds(560,13,100,15);
        volledigeBreedteCB.addActionListener(this);
        //volledigeBreedteCB.setSelected(true);
        bottomPanel.add(volledigeBreedteCB,0);
		
		popupCB = new JCheckBox(WiskOpdr.rb.getString("popupLabel"));
		popupCB.setOpaque(false);
		popupCB.setFont(font);
		popupCB.setBounds(670,13,70,15);
		popupCB.addActionListener(this);
	    //volledigeBreedteCB.setSelected(true);
		bottomPanel.add(popupCB,0);
		
		imageButton = new FormuleButton("tekstvak");
		imageButton.setBounds(740,10,20,20);
		imageButton.addActionListener(this);
		imageButton.setVisible(false);
		bottomPanel.add(imageButton);
		
		shareButton = new JButton(shareAction);
		shareButton.setBounds(762,10,24,20);
		bottomPanel.add(shareButton);
		
		getContentPane().add(bottomPanel,BorderLayout.SOUTH);
        launchData = shareAction.unwrap(launchData);
        addInteractieEditPanel(launchData);
        
        
        this.addWindowListener(this);
        this.setSize(Math.max(size.width, widthEditPanel), size.height);
        doLayout();
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
		else
		{	for(int i=0 ; i<set.length ; i++)
			{	soortAntwoordVakKeuze.addItem(TekstInteractiePanelVak.interactiePanelDescriptions[set[i]]);
			}
		}
	}
    
    public void setBackground(Color c)
    {	super.setBackground(c);
    	if(interactieEditPanel!=null)((Component)interactieEditPanel).setBackground(c);
    }

    public void addInteractieEditPanel(Hashtable launchData)
    {
    	if(launchData==null) 
    	{	if(soortAntwoordVakKeuze.getModel().getSize()==2)
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
        if(launchData.containsKey("setNr")) setNr = ((Integer)launchData.get("setNr")).intValue();
        if(launchData.containsKey("popupImageString")) popupImageString = (String)launchData.get("popupImageString");       
// WIM TODO is dit okay?
        if(launchData.containsKey("crossWidgetId")) setCrossWidgetId((String)launchData.get("crossWidgetId"));
        subscriptions = launchData.get("subscriptions"); // do not loose

       if(soortInteractiePanel != -2) 
       {   for(int i=0 ; i<set.length ; i++)
       		{	if(set[i]==soortInteractiePanel)soortAntwoordVakKeuze.setSelectedIndex(i+1);
				
			}
	       if(interactieEditPanel==null)
	       {	makeInteractieEditPanel(soortInteractiePanel);
	       		plaatsEditInteractiePanel();
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
        
       
        
       /* makeInteractieEditPanel(soortInteractiePanel);
        
         
        
        if(interactieEditPanel!=null)
		{	interactieEditPanel.setBounds(10,20,780,520);
			((JComponent)interactieEditPanel).setBorder(BorderFactory.createLineBorder(Color.gray));
			((Component)interactieEditPanel).setBackground(getBackground());
			interactieEditPanel.setEditState(interactiePanelLaunchState);
			add((Component)interactieEditPanel);
			
		}**/
        
        if(interactieEditPanel!=null)
		{	interactieEditPanel.setEditState(interactiePanelLaunchState);
		}
        
        breedteTF.setText(""+breedte);
        hoogteTF.setText(""+hoogte);
        volledigeBreedteCB.setSelected(volledigeBreedte);
        popupCB.setSelected(popup);
        imageButton.setVisible(popup);
        breedteTF.setVisible(!volledigeBreedte);
    	breedteLabel.setVisible(!volledigeBreedte);
    	
    	this.popupImageString = popupImageString;
    	
    	imageButton.setPopupButtonImage(popupImage);
    	iconman = new Iconan(WiskOpdr.applet, (Component)this, (Hashtable)TekstImageVak.getImageMap());
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
        
    }
    
    private void plaatsEditInteractiePanel()
    {
    	if(interactieEditPanel!=null)
		{	((Component) interactieEditPanel).setBounds(-1,-1,getSize().width,getSize().height-110);
			if(interactieEditPanel instanceof JComponent)((JComponent)interactieEditPanel).setBorder(BorderFactory.createLineBorder(Color.gray));
			((Component)interactieEditPanel).setBackground(getBackground());
			getContentPane().add((Component)interactieEditPanel);
			widthEditPanel = ((Component)interactieEditPanel).getPreferredSize().width;
			if(interactieEditPanel!=null)((Component)interactieEditPanel).doLayout();//if(interactieEditPanel!=null) interactieEditPanel.start();
		    if(interactieEditPanel!=null) ((Component)interactieEditPanel).repaint();
		    if(interactieEditPanel!=null) (interactieEditPanel).start();
		    imageButton.setCode(TekstInteractiePanelVak.interactiePanelSetNames[setNr]);
		    //System.out.println("layout");
		}
		pack();
		this.setSize(Math.max(size.width, widthEditPanel), size.height);
    }
    
    public void makeInteractieEditPanel(int soortInteractiePanel)
    {  	FontMetrics fm = getFontMetrics(WiskOpdr.formuleFont0);
		int h = fm.getAscent()+fm.getDescent() + 8;
	
    	if(soortInteractiePanel == -1)
		{	interactieEditPanel = null;
		}
    	else if(soortInteractiePanel == 0)
		{	interactieEditPanel = (new AntwoordFormuleVak()).getEditPanel();
			breedteTF.setText("300");
	        hoogteTF.setText("250");
		}
		else if(soortInteractiePanel == 1)
		{	interactieEditPanel = (new AntwoordVergelijkingVak()).getEditPanel();
			breedteTF.setText("300");
			hoogteTF.setText("250");
		}
		else if(soortInteractiePanel == 2)
		{	interactieEditPanel = (new SimpelAntwoordFormuleVak()).getEditPanel();
			breedteTF.setText("50");
			hoogteTF.setText(""+h);
		}
		else if(soortInteractiePanel == 3)
		{	interactieEditPanel = (new SimpelAntwoordVergelijkingVak()).getEditPanel();
			breedteTF.setText("50");
			hoogteTF.setText(""+h);
		}
		else if(soortInteractiePanel == 4)
		{	interactieEditPanel = (new TekstEditor(true,true,true)).getEditPanel();
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
			breedteTF.setText("110");
			hoogteTF.setText("35");
			if(interactieEditPanel!=null)interactieEditPanel.zetBreedte(110);
	    	if(interactieEditPanel!=null)interactieEditPanel.zetHoogte(35);
		}
		else if(soortInteractiePanel == 13)
		{	interactieEditPanel = (new AntwoordTekstVak()).getEditPanel();
			breedteTF.setText("50");
			hoogteTF.setText(""+h);
			//if(interactieEditPanel!=null)interactieEditPanel.zetBreedte(50);
	    	//if(interactieEditPanel!=null)interactieEditPanel.zetHoogte(h);
		}
		else if(soortInteractiePanel == 14)
		{	interactieEditPanel = (new AntwoordKeuzeVak()).getEditPanel();
			breedteTF.setText("110");
			hoogteTF.setText("24");
			if(interactieEditPanel!=null)interactieEditPanel.zetBreedte(110);
	    	if(interactieEditPanel!=null)interactieEditPanel.zetHoogte(24);
		}
		else if(soortInteractiePanel == 16)
		{	interactieEditPanel = (new CheckSleepUnitPanel()).getEditPanel();
			breedteTF.setText("110");
			hoogteTF.setText("35");
			if(interactieEditPanel!=null)interactieEditPanel.zetBreedte(110);
	    	if(interactieEditPanel!=null)interactieEditPanel.zetHoogte(35);
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
            breedteTF.setText("110");
            hoogteTF.setText("35");
            if(interactieEditPanel!=null)interactieEditPanel.zetBreedte(110);
            if(interactieEditPanel!=null)interactieEditPanel.zetHoogte(35);
        }
		else if(soortInteractiePanel == 49)
        {   interactieEditPanel = (new CheckButtonPanel()).getEditPanel();
            breedteTF.setText("110");
            hoogteTF.setText("35");
            if(interactieEditPanel!=null)interactieEditPanel.zetBreedte(110);
            if(interactieEditPanel!=null)interactieEditPanel.zetHoogte(35);
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
		else if(soortInteractiePanel == 45)//GraphTool
		{  		if(soortInteractiePanel == TekstInteractiePanelVak.interactiePanelSets[1][30])
				{	interactieEditPanel = maakInteractieEditPanel(TekstInteractiePanelVak.wiskOpdrInteractiePanels[30][0], WiskOpdr.language);
					breedteTF.setText("300");
					hoogteTF.setText("300");
					
				}
				
			
			//interactieEditPanel.start();
		}
		else if(soortInteractiePanel > 4)
		{  	for(int i=0 ; i<TekstInteractiePanelVak.wiskOpdrInteractiePanels.length ; i++)
			{	System.out.println(""+i);
				System.out.println(""+soortInteractiePanel);
				System.out.println(""+TekstInteractiePanelVak.interactiePanelSets[1][i]);
				if(soortInteractiePanel == TekstInteractiePanelVak.interactiePanelSets[1][i])
				{	interactieEditPanel = maakInteractieEditPanel(TekstInteractiePanelVak.wiskOpdrInteractiePanels[i][0], WiskOpdr.language);
					breedteTF.setText("500");
					hoogteTF.setText("450");
					break;
				}
				
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
			} else 			
				soortInteractiePanel = set[selectNr];
		}
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
        h.put("setNr", new Integer(setNr));
        h.put("popupImageString", popupImageString);
        if(crossWidgetId != null)
        	h.put("crossWidgetId", crossWidgetId);
        if(subscriptions != null) 
        	h.put("subscriptions", subscriptions);
        System.out.println("LaunchState: "+interactiePanelLaunchState.toString());
        
		return h;
    }

    public void editImage() {
        if(imageDialog == null)
        {
            //Frame f = JOptionPane.getFrameForComponent(this);
            imageDialog = new Dialog(this,"title", true);
            imageDialog.setLayout(new BorderLayout());
            iconman = new Iconan(WiskOpdr.applet, (Component)this, (Hashtable)TekstImageVak.getImageMap());
            imageDialog.add(iconman);
            imageDialog.pack();
            iconman.addActionListener(this);
        }
        iconman.select(popupImageString);
        imageDialog.show();
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
            
        } 
        else if (e.getSource() == okButton) {
        	confirmed = true;
        	produceAction("ok");
        	WiskOpdr.setLaunchDataChanged();
            this.setVisible(false);
            this.dispose();
            
        }
        else if (e.getSource() == volledigeBreedteCB) {
        	boolean b = volledigeBreedteCB.isSelected();
        	breedteTF.setVisible(!b);
        	breedteLabel.setVisible(!b);
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
			} else
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
		if(interactieEditPanel != null)
			interactieEditPanel.stop();
	}
    
    
}