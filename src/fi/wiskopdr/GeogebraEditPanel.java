package fi.wiskopdr;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;

import fi.beans.wiskopdrbeans.*;
import fi.wiskopdr.expressies.Expressie;
import fi.wiskopdr.formuleobjects.*;

public class GeogebraEditPanel extends JPanel implements InteractieEditPanel , ActionListener, TabletOwner
{
	private GeogebraPanel geogebraPanel;
	protected Rectangle defaultPanelBounds = new Rectangle(5,5,800,580);
	
	private JCheckBox showResetIconCB, bewaarOptieCB, borderCB, nakijkenCB;
	private JCheckBox nakijkenCheckDWOCB, nakijkenGemaakteObjectenCB;
	
	private String varNaam;
	private String yAsNaam;
	private String fileUrl;
	private JLabel varNaamLabel, yAsLabel, fileUrlLabel;
	private JTextField varNaamTF, yAsNaamTF, fileUrlTF;
	
	private boolean showResetIcon, 
					bewaarOptie, border, nakijken, nakijkenCheckDWO, nakijkenGemaakteObjecten;
	
	private Tablet tablet;
	private boolean tabletAdded;
	private FormuleVakHouder tabletUser;
	
	private Font font = new Font("SansSerif",Font.PLAIN,12);
	
	private JComponent component;
	
	private GeogebraParamButton geogebraParamButton;
	private GeogebraDesiredObjectsButton geogebraDesiredObjectsButton;
    private Hashtable geogebraParams;
	
	private JLabel maxScoreLabel;
	private JTextField maxScoreTF;
	
	protected int version() { return GeogebraParamButton.GEOGEBRA4; }
	
	
	public GeogebraEditPanel()
	{	
		addMouseListener(new MouseAdapter()
			{	public void mousePressed(MouseEvent e)
				{	requestFocus();
				}
			});
		component = this;
		
		setLayout(null);
		setBackground(WiskOpdr.bgcolor);
		createGeogebraPanel();
		
		showResetIcon = true;
		bewaarOptie = false; 
		border = false;
		nakijken = false;
		nakijkenCheckDWO = false;
		nakijkenGemaakteObjecten = false;
		
		showResetIconCB = maakCheckBox(WiskOpdr.rb.getString("GEP_resetIcon"), 500,110,160,20, showResetIcon);
		bewaarOptieCB	= maakCheckBox(WiskOpdr.rb.getString("GEP_bewaarOptie"), 500,20,160,20, bewaarOptie);
		borderCB	= maakCheckBox(WiskOpdr.rb.getString("GEP_rand"), 500,80,160,20, border);
		
		nakijkenCB  = maakCheckBox(WiskOpdr.rb.getString("nakijkKnopLabel"), 500,200,160,20, nakijken);
		nakijkenCheckDWOCB  = maakCheckBox(WiskOpdr.rb.getString("GEP_checkMetCheckDWO"), 520,220,160,20, nakijkenCheckDWO);
		nakijkenCheckDWOCB.setVisible(false);
		nakijkenGemaakteObjectenCB = maakCheckBox(WiskOpdr.rb.getString("GEP_checkMetObjecten"), 520,240,160,20, nakijkenGemaakteObjecten);
		nakijkenGemaakteObjectenCB.setVisible(false);
        
		geogebraParamButton = new GeogebraParamButton(version());
		geogebraParamButton.setBounds(500,150,150,20);
		add(geogebraParamButton);
		
		maxScoreLabel = new JLabel("Score");
		maxScoreLabel.setBounds(500,270,80,20);
		maxScoreLabel.setVisible(false);
		add(maxScoreLabel);
		
		maxScoreTF = new JTextField("0");
		maxScoreTF.setBounds(500,270,40,20);
		maxScoreTF.addActionListener(this);
		maxScoreTF.setVisible(false);
		add(maxScoreTF);
		
		geogebraDesiredObjectsButton = new GeogebraDesiredObjectsButton();
		geogebraDesiredObjectsButton.setBounds(500,270,150,20);
		geogebraDesiredObjectsButton.setVisible(false);
        add(geogebraDesiredObjectsButton);
        
		
	}


	/**
	 * 
	 */
	protected void createGeogebraPanel() {
		geogebraPanel = new GeogebraPanel(true);
		geogebraPanel.refreshGeogebra();
		geogebraPanel.buildGUI();
		add(geogebraPanel);
		setGeogebraBounds();
	}
	
	public void setBounds(int x, int y, int b, int h)
	{	super.setBounds(x,y,b,h);
	 	int br = 170; 
		showResetIconCB.setLocation(getSize().width-br, showResetIconCB.getLocation().y);
		bewaarOptieCB.setLocation(getSize().width-br, bewaarOptieCB.getLocation().y);
		borderCB.setLocation(getSize().width-br, borderCB.getLocation().y);
		nakijkenCB.setLocation(getSize().width-br, nakijkenCB.getLocation().y);
		nakijkenCheckDWOCB.setLocation(getSize().width-br+20, nakijkenCheckDWOCB.getLocation().y);
		nakijkenGemaakteObjectenCB.setLocation(getSize().width-br+20, nakijkenGemaakteObjectenCB.getLocation().y);
        maxScoreLabel.setLocation(getSize().width-br, maxScoreLabel.getLocation().y);
		maxScoreTF.setLocation(getSize().width-br+80, maxScoreTF.getLocation().y);
	       
		geogebraParamButton.setLocation(getSize().width-br, geogebraParamButton.getLocation().y);
		geogebraDesiredObjectsButton.setLocation(getSize().width-br, geogebraDesiredObjectsButton.getLocation().y);

		setGeogebraBounds();
	}
	
	private JCheckBox maakCheckBox(String s, int x, int y, int b, int h, boolean selected)
	{	JCheckBox checkbox = new JCheckBox(s);
		checkbox.setBounds(x,y,b,h);
		checkbox.setFont(font);
		checkbox.setBackground(getBackground());
		checkbox.setSelected(selected);
		checkbox.addActionListener(this);
		add(checkbox);
		
		return checkbox;
	}
	
	public Hashtable getEditState()
	{	
		boolean showResetIcon = true;
		boolean bewaarOptie = false; 
		boolean border = false;
		Hashtable geogebraParams = new Hashtable();
		boolean nakijken = false;
		boolean nakijkenGemaakteObjecten = false;
		int scoreMax = 0;
		String[] geogebraCheckObjects = null;
		int[] geogebraCheckScores = null;
        		
		showResetIcon = this.showResetIcon;
		bewaarOptie = this.bewaarOptie;
		border = this.border;
		geogebraParams = geogebraParamButton.getParams();
		nakijken = this.nakijken;
		nakijkenGemaakteObjecten = this.nakijkenGemaakteObjecten;
		scoreMax = Integer.parseInt(maxScoreTF.getText());
		if(nakijkenGemaakteObjecten){
		    scoreMax = geogebraDesiredObjectsButton.getScoreMax();
			geogebraCheckObjects = geogebraDesiredObjectsButton.getObjects();
			geogebraCheckScores = geogebraDesiredObjectsButton.getScores();
		}
	            
		Hashtable h = getGeoGebraEditState();
		h.put("showResetIcon", new Boolean(showResetIcon));
		h.put("bewaarOptie", new Boolean(bewaarOptie));
		h.put("border", new Boolean(border));
		h.put("geogebraParams", geogebraParams);
		h.put("nakijken", new Boolean(nakijken));
		if(nakijken){
		    h.put("scoreMax", new Integer(scoreMax));
		    h.put("nakijkenGemaakteObjecten", new Boolean(nakijkenGemaakteObjecten));
            if(nakijkenGemaakteObjecten){
	            h.put("geogebraCheckObjects", geogebraCheckObjects);
	            h.put("geogebraCheckScores", geogebraCheckScores);
	        }
		}
		
		
        
		return h;
	}


	protected Hashtable getGeoGebraEditState() {
		return geogebraPanel.getEditState();
	}
	
	public void setEditState(Hashtable h)
	{
		boolean showResetIcon = true;
		boolean bewaarOptie = false; 
        boolean alsTool = false;
        boolean border = false;
        boolean geogebraNieuw = true;
        Hashtable geogebraParams = new Hashtable();
        boolean nakijken = false;
        boolean nakijkenGemaakteObjecten = false;
        int scoreMax = 0;
        String[] geogebraCheckObjects = null;
        int[] geogebraCheckScores = null;
					
		if (h.containsKey("showResetIcon")) showResetIcon = ((Boolean) h.get("showResetIcon")).booleanValue();
		if (h.containsKey("bewaarOptie")) bewaarOptie = ((Boolean) h.get("bewaarOptie")).booleanValue();
		if (h.containsKey("alsTool")) alsTool = ((Boolean) h.get("alsTool")).booleanValue();
		if (h.containsKey("border")) border = ((Boolean) h.get("border")).booleanValue();
		if (h.containsKey("geogebraNieuw")) geogebraNieuw = ((Boolean) h.get("geogebraNieuw")).booleanValue();
		if (h.containsKey("geogebraParams")) geogebraParams = (Hashtable) h.get("geogebraParams");
		else geogebraParams = geogebraParamButton.getDefaultParams();
		if (h.containsKey("nakijken")) nakijken = ((Boolean) h.get("nakijken")).booleanValue();
		if (h.containsKey("nakijkenGemaakteObjecten")) nakijkenGemaakteObjecten = ((Boolean) h.get("nakijkenGemaakteObjecten")).booleanValue();
        if(h.containsKey("scoreMax")) scoreMax = ((Integer)h.get("scoreMax")).intValue();
		if (h.containsKey("geogebraCheckObjects")) geogebraCheckObjects = (String[]) h.get("geogebraCheckObjects");
		if (h.containsKey("geogebraCheckScores")) geogebraCheckScores = (int[]) h.get("geogebraCheckScores");
                	
		this.showResetIcon = showResetIcon;
		this.bewaarOptie = bewaarOptie;
		this.border = border;
		this.geogebraParams = geogebraParams;
		this.nakijken = nakijken;
		this.nakijkenGemaakteObjecten = nakijkenGemaakteObjecten;
		this.nakijkenCheckDWO = nakijken && !nakijkenGemaakteObjecten;
		
		updateNakijkOpties();
	    
		showResetIconCB.setSelected(showResetIcon);
		
		bewaarOptieCB.setSelected(bewaarOptie);
		borderCB.setSelected(border);
		geogebraParamButton.setParams(geogebraParams);
		nakijkenCB.setSelected(nakijken);
        
		setGeogebraEditState(h);
		if(!nakijkenGemaakteObjecten)maxScoreTF.setText(""+scoreMax);
        else{
            geogebraDesiredObjectsButton.setObjects(geogebraCheckObjects);
            geogebraDesiredObjectsButton.setScores(geogebraCheckScores);
            geogebraDesiredObjectsButton.setScoreMax(scoreMax);
        }
		setGeogebraBounds();
// FIXME Better not an internal frame
		Dialog d = (Dialog)WiskOpdr.getWindowForComponent(this);
		Dimension size = d.getSize();
		d.pack();
		d.setSize(size);
	
	}


	protected void setGeogebraBounds() {
		geogebraPanel.setBounds(defaultPanelBounds);
	}


	protected void setGeogebraEditState(Hashtable h) {
		geogebraPanel.setEditState(h);
	}
	
	
	public void zetBreedte(int b)
	{	geogebraPanel.setSize(b,geogebraPanel.getSize().height);
	}
	public void zetHoogte(int h)
	{	geogebraPanel.setSize(geogebraPanel.getSize().width, h);
	}
	
	public void wis(){}
    
	public void zetMode(int mode){}
	
    public void stop(){
    	if(geogebraPanel!=null)geogebraPanel.destroy();
    }
    
    public void start(){}
	
	public void actionPerformed(ActionEvent e)
	{	
		if(e.getSource().equals(showResetIconCB))
		{	showResetIcon = showResetIconCB.isSelected();
			setGeogebraEditState(getEditState());
			//geogebraPanel.setBounds(defaultPanelBounds);
		}
		if(e.getSource().equals(bewaarOptieCB))
		{	bewaarOptie = bewaarOptieCB.isSelected();
			return;
			//geogebraPanel.setEditState(getEditState());
			//geogebraPanel.setBounds(defaultPanelBounds);
		}
		if(e.getSource().equals(borderCB))
		{	border = borderCB.isSelected();
			return;
			//geogebraPanel.setEditState(getEditState());
			//geogebraPanel.setBounds(defaultPanelBounds);
		}
		if(e.getSource().equals(nakijkenCB))
        {   nakijken = nakijkenCB.isSelected();
            if(nakijken)
            {   nakijkenCheckDWO = true;
                nakijkenGemaakteObjecten = false;
            }
            else
            {   nakijkenCheckDWO = false;
                nakijkenGemaakteObjecten = false;
            }
            updateNakijkOpties();    
            return;
        
        }
		if(e.getSource().equals(nakijkenCheckDWOCB))
        {   nakijkenCheckDWO = nakijkenCheckDWOCB.isSelected();
		    nakijkenGemaakteObjecten = !nakijkenCheckDWO;
		    updateNakijkOpties();
		    return;
        }
		if(e.getSource().equals(nakijkenGemaakteObjectenCB))
        {   nakijkenGemaakteObjecten = nakijkenGemaakteObjectenCB.isSelected();
            nakijkenCheckDWO = !nakijkenGemaakteObjecten;
            updateNakijkOpties();
            return;
        }
		
		setGeogebraBounds();
// FIXME Better not an internal frame.		
		Dialog d = (Dialog)WiskOpdr.getWindowForComponent(this);
		Dimension size = d.getSize();
		d.pack();
		d.setSize(size);
//	
		//if(e.getSource().equals(formFcCB))
		//{	//formFc = formFcCB.isSelected();
			//grafiekPanel.zetFormalFunction(formFc);
		//}
	}
	
	private void updateNakijkOpties()
	{
	    nakijkenCheckDWOCB.setVisible(nakijken);
	    nakijkenGemaakteObjectenCB.setVisible(nakijken);
        nakijkenCheckDWOCB.setSelected(nakijken && nakijkenCheckDWO);
        nakijkenGemaakteObjectenCB.setSelected(nakijken && nakijkenGemaakteObjecten);
        maxScoreLabel.setVisible(nakijken && nakijkenCheckDWO);
        maxScoreTF.setVisible(nakijken && nakijkenCheckDWO);
        if(!nakijken || nakijkenGemaakteObjecten)maxScoreTF.setText("0");
        geogebraDesiredObjectsButton.setVisible(nakijkenGemaakteObjecten);
	}
	
	public void zetTabletUser(FormuleVakHouder formuleVakHouder)
	{	if(tablet==null) return;
		tablet.zetFormuleVakHouder(formuleVakHouder);
		tabletUser = formuleVakHouder;
		
	}
	
	public void zetTablet(FormuleVakHouder formuleVakHouder, int x, int y)
	{	if(tablet==null) 
		{	tablet = new Tablet(formuleVakHouder);
			tablet.setLocation(x,y);
			
		}
		tablet.zetFormuleVakHouder(formuleVakHouder);
		tabletUser = formuleVakHouder;
		
		
	}
	
	public void addTablet(FormuleVakHouder formuleVakHouder, int x, int y)
	{	if(tablet==null) 
		{	tablet = new Tablet(formuleVakHouder);
			
			
		}
		if(!tabletAdded)
		{	add(tablet,0);
			tablet.setLocation(x,y);
			tabletAdded = true;
			//resize();
            repaint();
		}
		tablet.zetFormuleVakHouder(formuleVakHouder);
	}
	
	public void removeTablet()
	{	if(tablet==null)return;
        remove(tablet);
        //resize();
        repaint();
		tabletAdded = false;
	}
	
	public Tablet getTablet()
	{	return tablet;
	}
	
	//ActionProducer
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
 	//end ActionProducer
	
	
}
