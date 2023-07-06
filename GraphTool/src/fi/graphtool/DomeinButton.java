package fi.graphtool;

import java.awt.AWTEventMulticaster;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

import javax.swing.Box;
import javax.swing.JDialog;
import javax.swing.JPanel;

import fi.beans.numworxlf.JButton;
import fi.beans.numworxlf.JLabel;
import fi.beans.numworxlf.JScrollPane;
import fi.wiskopdr.DialogFacade;
import fi.wiskopdr.WiskOpdr;
import fi.wiskopdr.formuleobjects.FormuleParser;
import fi.wiskopdr.formuleobjects.FormuleVak;
import fi.wiskopdr.formuleobjects.FormuleVakHouder;
import fi.wiskopdr.formuleobjects.Tablet;
import fi.wiskopdr.formuleobjects.TabletOwner;

public class DomeinButton extends JButton implements ActionListener, TabletOwner {

	private DialogFacade frame;
	private DomeinVak xMinVak, xMaxVak;
	//private double[] domein;
	private String[] domeinString;
	
	private boolean tabletAan;
	private Tablet tablet;
    private FormuleVakHouder tabletUser;
    private boolean tabletAdded;
    
	
	private Font formuleVakFont = (!WiskOpdr.formTimes || WiskOpdr.mac) ? WiskOpdr.formuleFont0Mac : WiskOpdr.formuleFont0; //new Font("TimesRoman",Font.PLAIN,16);
	
	private JButton okButton; 
	private JButton cancelButton;
	
	JPanel domeinPanel = new JPanel();
	JPanel bottomPanel = new JPanel();
	JScrollPane scrollPane;
	
	static DecimalFormatSymbols dfs;
	public static DecimalFormat df;
	
	public DomeinButton()
	{
		//zet tekst: "D":
		super("D");
		
		dfs = new DecimalFormatSymbols();
		if(WiskOpdr.language.toString().equals("nl")) dfs.setDecimalSeparator(',');
		else dfs.setDecimalSeparator('.');
		if(WiskOpdr.language.toString().equals("nl")) dfs.setGroupingSeparator(' ');
		else dfs.setGroupingSeparator(' ');
		df = new DecimalFormat("0.##########", dfs);
		
		setMargin(new Insets(0,0,0,0));
		setSize(17,17);
		setOpaque(false);
		super.addActionListener(this);
		
		
        
		
        
	}
	
	/*
	public void zetDomein (double[] domein)
	{
		this.domein = domein;
	}
	*/
	public void zetDomeinString(String[] domeinString)
	{
		if(domeinString == null)
			this.domeinString = null;
		else
		{	this.domeinString = new String[2];
			this.domeinString[0] = domeinString[0];
			this.domeinString[1] = domeinString[1];
		}
	}
	
	/*
	private void makeDomein(){   
    	double[] oudDomein = new double[2];
		if(domein != null)
		{	oudDomein[0] = domein[0];
			oudDomein[1] = domein[1];
		}
		domein = new double[2];
    	if(xMinVak.geefFormuleVak().geefExpressie() != null)
    		domein[0] = xMinVak.geefFormuleVak().geefExpressie().geefWaarde();
    	else
    		domein[0] = Double.NEGATIVE_INFINITY;
    	if(xMaxVak.geefFormuleVak().geefExpressie() != null)
    		domein[1] = xMaxVak.geefFormuleVak().geefExpressie().geefWaarde();
    	else
    		domein[1] = Double.POSITIVE_INFINITY;
    	
    	produceAction("maak Domein");
	}
	*/
	
	private void makeDomeinString(){   
    	String[] oudDomeinString = new String[2];
		if(domeinString != null)
		{	oudDomeinString[0] = domeinString[0];
			oudDomeinString[1] = domeinString[1];
		}
		domeinString = new String[2];
    	domeinString[0] = xMinVak.geefFormuleVak().toString();
    	domeinString[1] = xMaxVak.geefFormuleVak().toString();
    	if(domeinString[0].equals("$f@"))
    		domeinString[0] = "$f" + Double.NEGATIVE_INFINITY + "@";
    	if(domeinString[1].equals("$f@"))
    		domeinString[1] = "$f" + Double.POSITIVE_INFINITY + "@";
    	
    	
		produceAction("maak Domein");
	}
	
	/*
	public double[] getDomein()
	{
		return domein;
	}
	*/
	
	public String[] getDomeinString()
	{
		return domeinString;
	}
	
	public void makeGUI(){
    	domeinPanel = new JPanel();
		bottomPanel = new JPanel();
        
        Box boxv = Box.createVerticalBox();
        
        Box boxh = Box.createHorizontalBox();
        boxh.add(Box.createHorizontalStrut(10));
        String xMinTekst = "";
        String xMaxTekst = "";
        if(domeinString[0].equals("$f" + Double.NEGATIVE_INFINITY + "@"))
        	xMinTekst = Double.toString(Double.NEGATIVE_INFINITY);
        else if(FormuleParser.geefExpressie(domeinString[0]) != null)
        	xMinTekst = df.format(FormuleParser.geefExpressie(domeinString[0]).geefWaarde());
        else
        	xMinTekst = (String) domeinString[0].subSequence(2, domeinString[0].length() - 1);
        if(domeinString[1].equals("$f" + Double.POSITIVE_INFINITY + "@"))
        	xMaxTekst = Double.toString(Double.POSITIVE_INFINITY);
        else if(FormuleParser.geefExpressie(domeinString[1]) != null)
        	xMaxTekst = df.format(FormuleParser.geefExpressie(domeinString[1]).geefWaarde());
        else
        	xMaxTekst = (String) domeinString[1].subSequence(2, domeinString[1].length() - 1);
       
        JLabel label = new JLabel(GraphTool.rb.getString("fc_huidigDomein") + " [" + 
        		xMinTekst + ";" + xMaxTekst + "]");
        boxh.add(label);
        
        boxv.add(boxh);
        boxv.add(Box.createVerticalStrut(10));
        
        Box boxh2 = Box.createHorizontalBox();
        JLabel nieuwDomein = new JLabel(GraphTool.rb.getString("fc_nieuwDomein"));
        boxh2.add(nieuwDomein);
        JLabel haakLinks = new JLabel("[");
        boxh2.add(haakLinks);
      
        xMinVak = new DomeinVak();
		//xMinVak.addActionListener(this);
        xMinVak.setPreferredSize(new Dimension(50, 25));
        boxh2.add(xMinVak);
       
        JLabel komma = new JLabel(",");
        boxh2.add(komma);
        xMaxVak = new DomeinVak();
        //xMaxVak.addActionListener(this);
        xMaxVak.setPreferredSize(new Dimension(50, 25));
        boxh2.add(xMaxVak);
      
        JLabel haakRechts = new JLabel("]");
        boxh2.add(haakRechts);
        boxv.add(boxh2);
        domeinPanel.add(boxv);
        
       
        // lijkt vrij zinloos:
        xMinVak.zetMinBreedte(50);
        xMinVak.zetMaat();
        xMaxVak.zetMinBreedte(50);
        xMaxVak.zetMaat();
        
        okButton = new JButton("Ok");
        okButton.addActionListener(this);
        bottomPanel.add(okButton);
        
        cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(this);
        bottomPanel.add(cancelButton);
        
		scrollPane = new JScrollPane(domeinPanel);
    }
    
    public void makeFrame(){
    	frame = DialogFacade.newInstance(this, "", true);
    	frame.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        frame.getContentPane().setLayout(new BorderLayout());
        frame.getContentPane().add(scrollPane);
        frame.getContentPane().add(bottomPanel,BorderLayout.SOUTH);
		frame.pack();
	    frame.setVisible(true);
	    
    }
    
    public void zetTabletAan(boolean b)
	{	tabletAan = b;
	}
    
    public void actionPerformed(ActionEvent e){
		if(e.getSource().equals(this) && frame==null){	
			makeGUI();
			makeFrame();
		}
		else if(e.getSource().equals(okButton)) {   
			makeDomeinString();
        	frame.setVisible(false);
            frame.dispose();
            frame=null;
        }
		else if(e.getSource().equals(cancelButton)) {   
			frame.getContentPane().removeAll();
			frame.setVisible(false);
            frame.dispose();
            frame=null;
        }
		else if(e.getSource().equals(xMinVak) || e.getSource().equals(xMaxVak))
		{	domeinPanel.removeAll();
			Box boxv = Box.createVerticalBox();
	        
	        Box boxh = Box.createHorizontalBox();
	        boxh.add(Box.createHorizontalStrut(10));
	        String xMinTekst = "";
	        String xMaxTekst = "";
	        if(domeinString[0].equals("$f" + Double.NEGATIVE_INFINITY + "@"))
	        	xMinTekst = Double.toString(Double.NEGATIVE_INFINITY);
	        else if(FormuleParser.geefExpressie(domeinString[0]) != null)
	        	xMinTekst = df.format(FormuleParser.geefExpressie(domeinString[0]).geefWaarde());
	        else
	        	xMinTekst = (String) domeinString[0].subSequence(2, domeinString[0].length() - 1);
	        if(domeinString[1].equals("$f" + Double.POSITIVE_INFINITY + "@"))
	        	xMaxTekst = Double.toString(Double.POSITIVE_INFINITY);
	        else if(FormuleParser.geefExpressie(domeinString[1]) != null)
	        	xMaxTekst = df.format(FormuleParser.geefExpressie(domeinString[1]).geefWaarde());
	        else
	        	xMaxTekst = (String) domeinString[1].subSequence(2, domeinString[1].length() - 1);
       
	        JLabel label = new JLabel(GraphTool.rb.getString("fc_huidigDomein") + " [" + 
	        		xMinTekst + "; " + xMaxTekst + "]");
	        boxh.add(label);
	        
	        boxv.add(boxh);
	        boxv.add(Box.createVerticalStrut(10));
	        
	        Box boxh2 = Box.createHorizontalBox();
	        JLabel nieuwDomein = new JLabel(GraphTool.rb.getString("fc_nieuwDomein"));
	        boxh2.add(nieuwDomein);
	        JLabel haakLinks = new JLabel("[");
	        boxh2.add(haakLinks);
	      
	        //xMinVak = new DomeinVak();
	        //xMinVak.setPreferredSize(new Dimension(50, 25));
	        //xMinVak.zetDomeinButton(this);
	        boxh2.add(xMinVak);
	       
	        JLabel komma = new JLabel(",");
	        boxh2.add(komma);
	       // xMaxVak = new DomeinVak();
	       // xMaxVak.zetDomeinButton(this);
	        //xMaxVak.addActionListener(this);
	        //xMaxVak.setPreferredSize(new Dimension(50, 25));
	        boxh2.add(xMaxVak);
	      
	        JLabel haakRechts = new JLabel("]");
	        boxh2.add(haakRechts);
	        boxv.add(boxh2);
	        domeinPanel.add(boxv);
		}
	}  
	
	 public boolean isPopup()
		{	if(getParent()!=null 
				&& getParent().getParent()!=null 
				&& getParent().getParent().getParent()!=null 
				&& getParent().getParent().getParent().getParent() instanceof JDialog) 
			return true;
			return false;
		}
	
	//methoden tabletOwner
	
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
	{	if(tablet==null) 
		{	tablet = new Tablet(formuleVakHouder);
			tablet.setLocation(x,y);
		}
		tablet.zetFormuleVakHouder(formuleVakHouder);
		tabletUser = formuleVakHouder;
	}
	
	public void addTablet(FormuleVakHouder formuleVakHouder, int xx, int yy)
	{	if(tablet==null) 
		{	tablet = new Tablet(formuleVakHouder);
		}
		if(!tabletAdded)
		{	add(tablet,0);
			tablet.setLocation(xx,yy);
			tabletAdded = true;
            repaint();
		}
		tablet.zetFormuleVakHouder(formuleVakHouder);
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
