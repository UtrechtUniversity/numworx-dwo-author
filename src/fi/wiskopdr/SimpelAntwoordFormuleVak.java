package fi.wiskopdr;

import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Hashtable;
import java.util.Map;

import javax.swing.*;

import org.cbook.cbookif.AssessmentMode;
import org.cbook.cbookif.CBookEvent;
import org.cbook.cbookif.CBookEventHandler;
import org.cbook.cbookif.CBookEventListener;
import org.cbook.cbookif.CBookWidgetInstanceIF;
import org.cbook.cbookif.SuccessStatus;

import fi.wiskopdr.expressies.Expressie;
import fi.wiskopdr.formuleobjects.FormuleVak;
import fi.wiskopdr.formuleobjects.FormuleElement;
import fi.wiskopdr.formuleobjects.TabletOwner;
import fi.wiskopdr.formuleobjects.FormuleVakHouder;
import fi.wiskopdr.formuleobjects.FormuleButton;
import fi.wiskopdr.formuleobjects.Tablet;
import fi.wiskopdr.tekstobjects.EditInteractiePanelDialog;
import fi.wiskopdr.tekstobjects.TekstElement;
import fi.wiskopdr.tekstobjects.TekstInteractiePanelVak;
import fi.wiskopdr.tekstobjects.TekstVak;
import fi.wiskopdr.opdrnav.MyOpdrContainer;
import fi.wiskopdr.opdrnav.OpdrNavStruct;
import fi.beans.wiskopdrbeans.CBookAware;
import fi.beans.wiskopdrbeans.InteractieEditPanel;
import fi.beans.wiskopdrbeans.InteractiePanel;


public class SimpelAntwoordFormuleVak extends JPanel implements InteractiePanel, ActionListener, MouseListener, FormuleVakHouder, CBookAware
{
	private AntwoordFormuleVak antwoordFormuleVak;
	private Component formuleComponent, scoreGoedComponent, scoreFoutComponent, scoreHalfComponent;
	private Component feedbackComponent;
	private ImageComponent scoreToetsComponent;
	private int ashoogte;
	private int minBreedte;
	private int mode;
	
	private boolean formuleToolBijFocus;
	private FormuleButton feedbackButton;
	private JPanel feedbackPanel;
	private JPanel pijlPanel;
	
	private boolean tabletAan;
	
	private Font formuleVakFont = (WiskOpdr.mac||WiskOpdr.zoefi) ? WiskOpdr.formuleFont1Mac : WiskOpdr.formuleFont1; //new Font("TimesRoman",Font.PLAIN,16);
	
	private boolean sizeFixed = false;
	
	private DialogFacade popupFrame;
	private FormuleButton popupButton;
	
	private boolean antwoordFormuleVakActief = false;
	private boolean vakUitwerking = false;
	private boolean boxMetRand = true;
	
	private boolean uitklapVak;
	
	private CBookEventHandler cbookEventHandler = new CBookEventHandler(this);
	
	static boolean fontOvererving;
	
	public static void zetFontOverervingForm(boolean b)
	{	fontOvererving = b;
	}
	
	public SimpelAntwoordFormuleVak()
	{
		setLayout(null);
		
		addMouseListener(this);
		
		if(!WiskOpdr.formTimes) formuleVakFont = WiskOpdr.tekstFont;
		
		antwoordFormuleVak = new AntwoordFormuleVak();
		antwoordFormuleVak.addActionListener(this);
		antwoordFormuleVak.setBounds(0,0,400,300);
		//antwoordFormuleVak.zetStappen(false);
		
		formuleComponent = antwoordFormuleVak.getComponentSimpel();
		formuleComponent.setFont(formuleVakFont);
		((FormuleVak)formuleComponent).setBorder(false);
		((FormuleVak)formuleComponent).addActionListener(this);
		formuleComponent.setLocation(0,0);
		add(formuleComponent);
		
		
		scoreGoedComponent = antwoordFormuleVak.getGoedIC();
		scoreGoedComponent.setLocation(20,0);
		((ImageComponent)scoreGoedComponent).zetKlein(true);
		add(scoreGoedComponent);
		scoreFoutComponent = antwoordFormuleVak.getFoutIC();
		scoreFoutComponent.setLocation(20,0);
		((ImageComponent)scoreFoutComponent).zetKlein(true);
		add(scoreFoutComponent);
		scoreHalfComponent = antwoordFormuleVak.getHalfIC();
		scoreHalfComponent.setLocation(20,0);
		((ImageComponent)scoreHalfComponent).zetKlein(true);
		add(scoreHalfComponent);
		
		feedbackComponent = antwoordFormuleVak.getFeedbackIC();
		feedbackComponent.setLocation(2,3);
		add(feedbackComponent);
		
		feedbackButton = new FormuleButton("?");
		feedbackButton.addActionListener(this);
		feedbackButton.setBackground(new Color(215,215,215));
		feedbackButton.setVisible(false);
		add(feedbackButton);
		
		feedbackPanel = new JPanel();
		feedbackPanel.setLayout(null);
		
		setOpaque(false);
		
		
		
		
	}
	
	public void maakPopupFrame()
	{	
		String title = WiskOpdr.rb.getString("TVEP_uitwerkingenPopup");
		popupFrame = DialogFacade.newInstance(this, title);
			popupFrame.getContentPane().setLayout(null);
			if(antwoordFormuleVak!=null)popupFrame.getContentPane().add((Component)antwoordFormuleVak,0);
		  
	    popupFrame.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e)
			{   closePopup();
				
				
			}
		});
	    popupFrame.addComponentListener(new ComponentAdapter(){
			public void componentResized(ComponentEvent e)
			{   int x = 0;//frame.getInsets().left;
				int y = 0;//frame.getInsets().top;
				int b = popupFrame.getSize().width - popupFrame.getInsets().left - popupFrame.getInsets().right;
				int h = popupFrame.getSize().height - popupFrame.getInsets().top - popupFrame.getInsets().bottom;
				antwoordFormuleVak.setBounds(x,y,b,h);
				//resize();
			}
		});
	    
	    antwoordFormuleVak.zetStappen(true);
	    
	    //((FormuleVak)formuleComponent).setBorder(true);
		//((FormuleVak)formuleComponent).addActionListener(this);
		//formuleComponent.setLocation(0,0);
		//antwoordFormuleVak.add(formuleComponent);
	  
	    
	}
	
	public void closePopup()
	{	antwoordFormuleVak.stapLeegTerug();
		formuleComponent = antwoordFormuleVak.getComponentSimpel();
		formuleComponent.setLocation(4,4);
		((FormuleVak)formuleComponent).setBorder(false);
		((FormuleVak)formuleComponent).setEditable(true);
		add(formuleComponent);
		
		((ImageComponent)scoreGoedComponent).zetKlein(true);
		add(scoreGoedComponent);
		((ImageComponent)scoreFoutComponent).zetKlein(true);
		add(scoreFoutComponent);
		((ImageComponent)scoreHalfComponent).zetKlein(true);
		add(scoreHalfComponent);
		
		scoreGoedComponent.setLocation(getSize().width-17,0);
		scoreFoutComponent.setLocation(getSize().width-17,0);
		scoreHalfComponent.setLocation(getSize().width-17,0);
		
		antwoordFormuleVakActief = false;
		
		zetMaat();
		if(mode==0 || mode==1 || antwoordFormuleVak.nagekeken)kijkNa();
		((FormuleVak)formuleComponent).setEditable(true);
	}
    
    public int getAsHoogte()
    {
        return ((FormuleElement)formuleComponent).ashoogte + (getFontMetrics(formuleVakFont)).getAscent()/2;
    }
	
	public FormuleVak geefFormuleVak()
	{	return (FormuleVak)formuleComponent;
	}
	
	public void zetTabletAan(boolean b)
	{	tabletAan = b;
	}
	
	public void paintComponent(Graphics g)
	{	int uitw = (vakUitwerking && !uitklapVak)?20:0;
		g.setColor(Color.white);
		if("GR".equals(WiskOpdr.deployVariant)) {
			for(int i=0 ; i<12 ; i++)
			{	g.setColor(new Color(230+i,230+i,230+i));
				g.fillRect(i*getWidth()/12, 2, getWidth()/12+1,getHeight()-4);
			}
		}
		else if(boxMetRand)
			g.fillRect(1,2,getSize().width-2, getSize().height-4);
		
		g.setColor(Color.gray);
		if("GR".equals(WiskOpdr.deployVariant))g.setColor(new Color(153,153,153));
		else if(boxMetRand)	g.drawRect(1,2,getSize().width-2-uitw, getSize().height-4);
		
	}
	
	public void zetMinBreedte(int b)
	{	minBreedte = b;	
	}
	public void zetOpdracht(Hashtable h, String[] randomVars, Hashtable randomValues) 
	{	
		if(fontOvererving && getParent() instanceof TekstInteractiePanelVak)
		{	Font geerftFont = ((TekstInteractiePanelVak)getParent()).getTekstVak().getFont();
			if (!geerftFont.getName().equals("TimesRoman") && WiskOpdr.formTimes && !WiskOpdr.mac) {
				geerftFont = new Font("TimesRoman", geerftFont.getStyle(), geerftFont.getSize() * 6 / 5);
			}
			formuleVakFont = geerftFont;
			formuleComponent.setFont(formuleVakFont);
			antwoordFormuleVak.getComponentSimpel().setFont(formuleVakFont);
		}
		
		antwoordFormuleVak.zetOpdracht(h, randomVars, randomValues);
	
		scoreGoedComponent.setLocation(getSize().width-17,0);
		scoreFoutComponent.setLocation(getSize().width-17,0);
		scoreHalfComponent.setLocation(getSize().width-17,0);
		if(scoreToetsComponent!=null) scoreToetsComponent.setLocation(getSize().width-18,0);
		if(h.containsKey("formuleToolBijFocus")) formuleToolBijFocus = ((Boolean)h.get("formuleToolBijFocus")).booleanValue();
		if(h.containsKey("uitw")) vakUitwerking = ((Boolean)h.get("uitw")).booleanValue();
		if(h.containsKey("boxMetRand")) boxMetRand = ((Boolean)h.get("boxMetRand")).booleanValue();
		
		uitklapVak = checkUitklapMogelijkheid();
		
		if(!uitklapVak && vakUitwerking && popupButton==null) {
			popupButton = new FormuleButton("uitwerking");
			popupButton.setBounds(0,0,5,20);
			popupButton.addActionListener(this);
			add(popupButton,0);
			zetMaat();
		}
		((FormuleVak)formuleComponent).zetStippels(!boxMetRand);
		
		//antwoordFormuleVak.zetSimpelFormuleVak(((FormuleVak)formuleComponent));
		//closePopup();
		
		
		
	}
	
	public void setEditState(Hashtable h) 
	{	
		if(fontOvererving && getParent() instanceof TekstInteractiePanelVak)
		{	Font geerftFont = ((TekstInteractiePanelVak)getParent()).getTekstVak().getFont();
			if (!geerftFont.getName().equals("TimesRoman") && WiskOpdr.formTimes && !WiskOpdr.mac) {
				geerftFont = new Font("TimesRoman", geerftFont.getStyle(), geerftFont.getSize() * 6 / 5);
			}
			formuleVakFont = geerftFont;
			formuleComponent.setFont(formuleVakFont);
		}
		
		antwoordFormuleVak.setEditState(h);
	
		antwoordFormuleVak.zetStartString("$f@"); // hierdoor komt het simpel antwoordvak netjes in de regel te liggen in de editor (forceert zetMaat())
		requestFocus(); //want antwoordFormuleVak moet geen focus krijgen in de editor
		if(h.containsKey("uitw")) vakUitwerking = ((Boolean)h.get("uitw")).booleanValue();
		if(h.containsKey("boxMetRand")) boxMetRand = ((Boolean)h.get("boxMetRand")).booleanValue();
		
		//uitklapVak = checkUitklapMogelijkheid();
		
		if(!uitklapVak && vakUitwerking && popupButton==null) {
			popupButton = new FormuleButton("uitwerking");
			popupButton.setBounds(0,0,5,20);
			popupButton.addActionListener(this);
			add(popupButton,0);
			zetMaat();
		}
		((FormuleVak)formuleComponent).zetStippels(!boxMetRand);
	}
	
	public Hashtable getEditState() 
	{	return antwoordFormuleVak.getEditState();
	}
	
	public void setState(Hashtable h) 
	{	remove(formuleComponent);
		antwoordFormuleVak.setState(h);
		formuleComponent = antwoordFormuleVak.getComponentSimpel();
		((FormuleVak)formuleComponent).zetStippels(!boxMetRand);
		formuleComponent.setFont(formuleVakFont);
		((FormuleVak)formuleComponent).setBorder(false);
		((FormuleVak)formuleComponent).addActionListener(this);
		formuleComponent.setLocation(0,0);
		add(formuleComponent);
		zetMaat();
		int uitw = (vakUitwerking && !uitklapVak)?20:0;
		scoreGoedComponent.setLocation(getSize().width-17-uitw,0);
		scoreFoutComponent.setLocation(getSize().width-17-uitw,0);
		scoreHalfComponent.setLocation(getSize().width-17-uitw,0);
		if(scoreToetsComponent!=null) scoreToetsComponent.setLocation(getSize().width-18-uitw,0);
	}
	
	public Hashtable getState() 
	{	return antwoordFormuleVak.getState();
	}
	
	public void zetMaat()
	{	int uitw = (vakUitwerking && !uitklapVak)?20:0;
		if(!sizeFixed)setSize(Math.max(minBreedte+uitw, formuleComponent.getSize().width+20+uitw), formuleComponent.getSize().height+8);
		else setSize(minBreedte+uitw+20, formuleComponent.getSize().height+8);
		formuleComponent.setLocation(4,4);
		feedbackButton.setBounds(getSize().width-15-uitw, getSize().height-12, 15,15);
		if(popupButton!=null)popupButton.setBounds(getSize().width-uitw,2,20,formuleComponent.getSize().height+5);
		if("MW".equals(WiskOpdr.deployVariant) || "GR".equals(WiskOpdr.deployVariant))feedbackButton.setBounds(getSize().width-15-uitw, getSize().height-13, 13,13);
		ashoogte = ((FormuleElement)formuleComponent).ashoogte+3;
		if(getParent()instanceof FormuleElement)((FormuleElement)getParent()).zetMaat();
		if(getParent()instanceof TekstElement)((TekstElement)getParent()).zetMaat();
		
	}
	
	public int geefAsHoogte()
	{
		return ashoogte + (getFontMetrics(formuleVakFont)).getAscent()/2;
	}
	
	public boolean checkUitklapMogelijkheid()
	{
		if(!vakUitwerking)
			return false;
		TekstVakPanel uitklapTvp = null;
		TekstInteractiePanelVak tip = null;
		if(getParent() instanceof TekstInteractiePanelVak)
			tip = (TekstInteractiePanelVak)getParent();
		if(tip!=null)
		{
			TekstVak tv = tip.getTekstVak();
			if(tv.getParent() instanceof TekstVakPanel)
				uitklapTvp = (TekstVakPanel)(tv.getParent());
			else
				return false;
			if(uitklapTvp.isInklapbaar() && tv==uitklapTvp.geefTekstVak(0, 0) && uitklapTvp.geefTekstVak(1, 0)!=null)
			{	if(antwoordFormuleVak!=null)
				{	uitklapTvp.addActionListener(this);
					int x = uitklapTvp.geefTekstVak(1, 0).geefMarge();
					int y = uitklapTvp.geefTekstVak(1, 0).getY() + uitklapTvp.geefTekstVak(1, 0).geefMargeBoven();//uitklapTvp.cellMarge;
					int b = uitklapTvp.geefTekstVak(1, 0).getWidth() - 2*uitklapTvp.geefTekstVak(1, 0).geefMarge();
					int h = uitklapTvp.geefUitklapHoogte(1) - 2*uitklapTvp.geefTekstVak(1, 0).geefMargeBoven();
					antwoordFormuleVak.setBounds(x,y,b,h);
					uitklapTvp.add(antwoordFormuleVak,0);
					antwoordFormuleVak.zetStappen(true);
					return true;
				}
			}
		}
		return false;
	}

	public void actionPerformed(ActionEvent e) 
	{	if(e.getSource()==antwoordFormuleVak && !antwoordFormuleVakActief)
		{	if(e.getActionCommand().equals("feedback"))
			{	feedbackButton.setVisible(true);
				if(feedbackPanel.getParent()!=null)
				{	Container c = feedbackPanel.getParent();
					c.remove(feedbackPanel);
					c.repaint();
				}
				/*Component c = antwoordFormuleVak.getFeedbackComponent();
				c.setLocation(0,0);
				feedbackPanel.setSize(c.getSize().width, c.getSize().height);
				feedbackPanel.add(c);
				this.activateFeedback(feedbackPanel);
				feedbackButton.setVisible(false);*/
				return;
			}
			if(e.getActionCommand().equals("feedbackWeg"))
			{	feedbackButton.setVisible(false);
				if(scoreToetsComponent!=null)remove(scoreToetsComponent);
				repaint();
				if(feedbackPanel.getParent()!=null)
				{	Container c = feedbackPanel.getParent();
					c.remove(feedbackPanel);
					c.repaint();
				}
				return;
			}
			if(e.getActionCommand().equals("closeFeedback"))
			{	feedbackButton.setVisible(true);
				if(feedbackPanel.getParent()!=null)
				{	Container c = feedbackPanel.getParent();
					c.remove(feedbackPanel);
					c.repaint();
				}
				return;
			}
			/*else if(e.getSource()==formuleComponent && e.getActionCommand().equals("formChanged"))
			{	scoreGoedComponent.setLocation(getSize().width-17,0);
				scoreFoutComponent.setLocation(getSize().width-17,0);
				scoreHalfComponent.setLocation(getSize().width-17,0);
				feedbackButton.setVisible(false);
				repaint();
			}*/
			
			produceAction(e.getActionCommand());
			int uitw = (vakUitwerking && !uitklapVak)?20:0;
			scoreGoedComponent.setLocation(getSize().width-17-uitw,0);
			scoreFoutComponent.setLocation(getSize().width-17-uitw,0);
			scoreHalfComponent.setLocation(getSize().width-17-uitw,0);
			repaint();
			
			if((mode==2 || mode==3) && (e.getActionCommand().equals("changed")|| e.getActionCommand().equals("checked")))
			{	if(scoreToetsComponent!=null) remove(scoreToetsComponent);
				if(antwoordFormuleVak.isCorrect()) scoreToetsComponent = new ImageComponent(WiskOpdr.GOEDKRUL);
				else if(antwoordFormuleVak.isFout()) scoreToetsComponent = new ImageComponent(WiskOpdr.FOUTKRUIS);
				else scoreToetsComponent = new ImageComponent(WiskOpdr.HALFKRUL);
				scoreToetsComponent.setLocation(getSize().width-18-uitw,0);
				((ImageComponent)scoreToetsComponent).zetKlein(true);
				if(antwoordFormuleVak.hasCheck())add(scoreToetsComponent);
				repaint();
			}
		}
		else if(e.getSource()==formuleComponent && e.getActionCommand().equals("focus"))
		{	if(formuleToolBijFocus)zetTabletUser();
		}
		else if(e.getSource()==formuleComponent && e.getActionCommand().equals("ingevuld"))
		{	
			String inputString = formuleComponent.toString();
			inputString = inputString.substring(2,inputString.length()-1);
			cbookEventHandler.fire("input",formuleComponent.toString());
			cbookEventHandler.fire("expression",formuleComponent.toString());
			
			double d = Double.NaN;
			try 
			{	d = Double.parseDouble(inputString);
			}
			catch(NumberFormatException nfe)
			{	d = Double.NaN;
			}
			if(!Double.isNaN(d))
			{	cbookEventHandler.fire("double",inputString);
			}
		}
		else if(e.getSource()==feedbackButton)
		{	Component c = antwoordFormuleVak.getFeedbackComponent();
			if("MW".equals(WiskOpdr.deployVariant) || "GR".equals(WiskOpdr.deployVariant)) c = antwoordFormuleVak.getMWFeedbackComponent();
			c.setLocation(0,0);
			feedbackPanel.setSize(c.getSize().width, c.getSize().height);
			feedbackPanel.add(c);
			this.activateFeedback(feedbackPanel);
			feedbackButton.setVisible(false);
		}
		else if(e.getSource()==popupButton || e.getActionCommand().equals("tvpKlapUit"))
		{	
			if(!uitklapVak)
			{	if(popupFrame==null) 
				{	maakPopupFrame();
					popupFrame.setVisible(true);
					int width = ((Component)antwoordFormuleVak).getSize().width + popupFrame.getInsets().left + popupFrame.getInsets().right;
					int height = ((Component)antwoordFormuleVak).getSize().height + popupFrame.getInsets().top + popupFrame.getInsets().bottom;
					
					popupFrame.setSize(width,height);
					
					Dimension screenSize = WiskOpdr.applet.getToolkit().getScreenSize();
					int x = getLocationOnScreen().x + Math.min(0,screenSize.width - (getLocationOnScreen().x + width));
					int y = getLocationOnScreen().y + Math.min(0,screenSize.height - (getLocationOnScreen().y + height));
					popupFrame.setLocation(x,y);
				}
				else
				{	popupFrame.setVisible(true);
					
				}
			}
    		feedbackButton.setVisible(false);
            if(scoreToetsComponent!=null)remove(scoreToetsComponent);
            repaint();
			if(feedbackPanel.getParent()!=null)
			{	Container c = feedbackPanel.getParent();
				c.remove(feedbackPanel);
				c.repaint();
			}
			antwoordFormuleVakActief = true;
			antwoordFormuleVak.zetSimpelFormuleVak((FormuleVak)formuleComponent);
			antwoordFormuleVak.formuleVak.requestFocus();
			
			
			
			
			
			
			/*((FormuleVak)formuleComponent).setLocation(30,10);
			((FormuleVak)formuleComponent).setBorder(true);
			
			if(antwoordFormuleVak.stapNr>0)
			{	antwoordFormuleVak.stapTerug();
				antwoordFormuleVak.disconnectFormuleVakSimpel();
				antwoordFormuleVak.maakStapSimpel();
			}
			FormuleVak fs = (FormuleVak)antwoordFormuleVak.getComponentSimpel();
			int fsx = fs.getX();
			int fsy = fs.getY();
			
			
			antwoordFormuleVak.remove(fs);
			fs = (FormuleVak)formuleComponent;
			fs.setLocation(fsx,fsy);
			antwoordFormuleVak.zetSimpelFormuleVak(fs);
			
			scoreGoedComponent.setVisible(false);
			((ImageComponent)scoreGoedComponent).zetKlein(false);
			antwoordFormuleVak.add(scoreGoedComponent);
			scoreFoutComponent.setVisible(false);
			((ImageComponent)scoreFoutComponent).zetKlein(false);
			antwoordFormuleVak.add(scoreFoutComponent);
			scoreHalfComponent.setVisible(false);
			((ImageComponent)scoreHalfComponent).zetKlein(false);
			antwoordFormuleVak.add(scoreHalfComponent);
			
			
			antwoordFormuleVak.kijkNa();*/
		}
		else if(e.getSource()==popupButton || e.getActionCommand().equals("tvpKlapIn"))
		{	closePopup();
		
		}
		else if(e.getActionCommand().equals("tvpUitklapResize"))
		{	
			if(!vakUitwerking)
				return;
			TekstVakPanel uitklapTvp = null;
			TekstInteractiePanelVak tip = null;
			if(getParent() instanceof TekstInteractiePanelVak)
				tip = (TekstInteractiePanelVak)getParent();
			if(tip!=null)
			{
				TekstVak tv = tip.getTekstVak();
				if(tv.getParent() instanceof TekstVakPanel)
					uitklapTvp = (TekstVakPanel)(tv.getParent());
				else
					return;
				if(uitklapTvp.isInklapbaar() && tv==uitklapTvp.geefTekstVak(0, 0) && uitklapTvp.geefTekstVak(1, 0)!=null)
				{	if(antwoordFormuleVak!=null)
					{	int x = uitklapTvp.geefTekstVak(1, 0).geefMarge();
						int y = uitklapTvp.geefTekstVak(1, 0).getY() + uitklapTvp.geefTekstVak(1, 0).geefMargeBoven();//uitklapTvp.cellMarge;
						int b = uitklapTvp.geefTekstVak(1, 0).getWidth() - 2*uitklapTvp.geefTekstVak(1, 0).geefMarge();
						int h = uitklapTvp.geefUitklapHoogte(1) - 2*uitklapTvp.geefTekstVak(1, 0).geefMargeBoven();
						antwoordFormuleVak.setBounds(x,y,b,h);
						uitklapTvp.add(antwoordFormuleVak,0);
						antwoordFormuleVak.zetStappen(true);
					}
				}
			}
			
		}
	}

	

	public void destroy() {
		// TODO Auto-generated method stub

	}

	public InteractieEditPanel getEditPanel()
	{	return new AntwoordFormuleVakEditPanel(2);
	}

	public int getIpId(){return 0;}
	
	public String getIpExpString(){return null;}

	public int getScore() 
	{	return antwoordFormuleVak.getScore();
	}
	
	public int[][] getScoreObjectives()
	{	return antwoordFormuleVak.getScoreObjectives();
	}
	
	public int getScoreMax() 
	{	return antwoordFormuleVak.getScoreMax();
	}	

	public boolean isCorrect() 
	{	return antwoordFormuleVak.isCorrect();
	}
	
	public boolean isCorrectStrikt() 
	{	return antwoordFormuleVak.isCorrectStrikt();
	}

	public boolean isFout() 
	{	return antwoordFormuleVak.isFout();
	}

	public void kijkNa() 
	{	antwoordFormuleVak.kijkNa();
	}

	public void kijkNa(int stapNr) 
	{	antwoordFormuleVak.kijkNa(stapNr);
	}

	public void opnieuw() 
	{	antwoordFormuleVak.opnieuw();
	}

	

	
	public void start() 
	{	antwoordFormuleVak.start();
	}
	
	public void stop() 
	{	if(popupFrame!=null)
		{	popupFrame.setVisible(false);
			closePopup();
			antwoordFormuleVak.stop();
			popupFrame.dispose();
		}
		else antwoordFormuleVak.stop();
		
	}

	public void wis() 
	{	antwoordFormuleVak.wis();
	}

	public void zetMode(int mode) 
	{	antwoordFormuleVak.zetMode(mode);
		this.mode = mode;
	}
	
	public void zetNagekeken(boolean b) 
	{	antwoordFormuleVak.zetNagekeken(b);
	}
	
	public void activateFeedback(Component c)
	{	
		Container parent = getParent();
		int x = parent.getLocation().x;
		int y = parent.getLocation().y;
		int h = parent.getSize().height;
		for(int i=0 ; parent!=null && i<40 ; i++)
		{	if(parent instanceof OpdrNavStruct) 
			{	
				int cx = Math.min(parent.getSize().width-c.getSize().width, x+10);
				int cy = y+h+10>parent.getSize().height ? y-c.getSize().height-10 : y+h+10;
				c.setLocation(cx, cy);
				((OpdrNavStruct)parent).add(c,0);
				parent.repaint();
				break;
			}
			else if(parent instanceof TekstVakPanel && parent.getParent().getParent().getParent().getParent()instanceof JDialog ) 
			{	
				int cx = Math.min(parent.getSize().width-c.getSize().width, x+10);
				int cy = y+h+10>parent.getSize().height ? y-c.getSize().height-10 : y+h+10;
				c.setLocation(cx, cy);
				((TekstVakPanel)parent).add(c,0);
				parent.repaint();
				break;
			}
			else 
			{	parent = parent.getParent();
				if(parent!=null)
				{	x += parent.getLocation().x;
					y += parent.getLocation().y;
				}
			}
		}
		
		//if(getParent().getParent()instanceof TabletOwner)((TabletOwner)getParent().getParent()).addTablet(antwoordFormuleVak,getLocation().x+20, getLocation().y+getSize().height-120);
		//else if(getParent().getParent().getParent() instanceof TabletOwner)((TabletOwner)getParent().getParent().getParent()).addTablet(antwoordFormuleVak,getLocation().x+20, getLocation().y+getSize().height+50);
		//else if(getParent().getParent().getParent().getParent() instanceof TabletOwner)((TabletOwner)getParent().getParent().getParent().getParent()).addTablet(antwoordFormuleVak,getLocation().x+20,getLocation().y+getSize().height+70);
		//else if(getParent().getParent().getParent().getParent().getParent() instanceof TabletOwner)((TabletOwner)getParent().getParent().getParent().getParent().getParent()).addTablet(antwoordFormuleVak,getLocation().x+20, getParent().getParent().getLocation().y+getSize().height+70);
		//else if(getParent().getParent().getParent().getParent().getParent().getParent() instanceof TabletOwner)((TabletOwner)getParent().getParent().getParent().getParent().getParent().getParent()).addTablet(antwoordFormuleVak,getLocation().x+20, getParent().getParent().getLocation().y+getSize().height+70);
	}
	
	public void activateTablet()
	{	
		Container parent = getParent();
		int x = parent.getLocation().x;
		int y = parent.getLocation().y;
		int h = parent.getSize().height;
		for(int i=0 ; parent!=null && i<40 ; i++)
		{	if(parent instanceof TabletOwner) 
			{	((TabletOwner)parent).addTablet(this, x+20, y+h+20);
				Tablet tablet = ((TabletOwner)parent).getTablet();
				if(tablet==null) break;
				int tx = Math.min(parent.getSize().width-tablet.getSize().width, x+20);
				int ty = y+h+20+tablet.getSize().height>parent.getSize().height ? y-tablet.getSize().height-10 : y+h+20;
				tablet.setLocation(tx, ty);
				break;
			}
			else 
			{	parent = parent.getParent();
				if(parent==null)return;
				x += parent.getLocation().x;
				y += parent.getLocation().y;
			}
		}
		
		
		
		//if(getParent().getParent()instanceof TabletOwner)((TabletOwner)getParent().getParent()).addTablet(antwoordFormuleVak,getLocation().x+20, getLocation().y+getSize().height-120);
		//else if(getParent().getParent().getParent() instanceof TabletOwner)((TabletOwner)getParent().getParent().getParent()).addTablet(antwoordFormuleVak,getLocation().x+20, getLocation().y+getSize().height+50);
		//else if(getParent().getParent().getParent().getParent() instanceof TabletOwner)((TabletOwner)getParent().getParent().getParent().getParent()).addTablet(antwoordFormuleVak,getLocation().x+20,getLocation().y+getSize().height+70);
		//else if(getParent().getParent().getParent().getParent().getParent() instanceof TabletOwner)((TabletOwner)getParent().getParent().getParent().getParent().getParent()).addTablet(antwoordFormuleVak,getLocation().x+20, getParent().getParent().getLocation().y+getSize().height+70);
		//else if(getParent().getParent().getParent().getParent().getParent().getParent() instanceof TabletOwner)((TabletOwner)getParent().getParent().getParent().getParent().getParent().getParent()).addTablet(antwoordFormuleVak,getLocation().x+20, getParent().getParent().getLocation().y+getSize().height+70);
	}
	
	public void zetTabletUser()
	{	Container parent = getParent();
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
	
	public void verplaatsFocus()
	{	Container parent = getParent();
		for(int i=0 ; parent!=null && i<40 ; i++)
		{	if(parent instanceof TekstVakPanel) 
			{	((TekstVakPanel)parent).verplaatsFocus();
				break;
			}
			else if(parent instanceof MyOpdrContainer) 
			{	((MyOpdrContainer)parent).verplaatsFocus();
				break;
			}
			else 
			{	parent = parent.getParent();
			}
		}
		//System.out.println("tab");
	}

	public void mousePressed(MouseEvent e)
	{	formuleComponent.requestFocus();
		((FormuleVak)formuleComponent).zetOpEind();
		if(formuleToolBijFocus)activateTablet();
	}
	
	public void mouseClicked(MouseEvent e){;}
	public void mouseReleased(MouseEvent e){;}
	public void mouseEntered(MouseEvent e){;}
	public void mouseExited(MouseEvent e){;}
	
	
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

 	@Override
 	public void acceptCBookEvent(CBookEvent event) {
		String command = event.getCommand();
		System.out.println("accepted " + command);
		if(command.startsWith("input") || command.startsWith("expression"))
		{
	 		String formuleString = (String)event.getMessage();
	 		if(formuleString.charAt(0)!='$') formuleString = "$f" + formuleString + "@";
			((FormuleVak)formuleComponent).vulVak(formuleString);
			
		}
		if(command.startsWith("double"))
		{
			Map map = (Map)event.getParameters();
			if(map!=null)
			{	double waarde = ((Double)map.get("value")).doubleValue();
				new Expressie();
				String doubleString = Expressie.df.format(waarde);	
				doubleString = "$f" + doubleString + "@";
				((FormuleVak)formuleComponent).vulVak(doubleString);
			}
			else
			{	String message = event.getMessage();
				String doubleString = "$f" + message + "@";
				((FormuleVak)formuleComponent).vulVak(doubleString);
			}
		}
		
	}


	@Override
	public void addCBookEventListener(CBookEventListener listener,
			String command) {
		System.out.println("addCBookEventListener: "+listener.toString() +"+"+command);
		cbookEventHandler.addCBookEventListener(listener, command);
		
	}

	@Override
	public void removeCBookEventListener(CBookEventListener listener,
			String command) {
		cbookEventHandler.removeCBookEventListener(listener, command);
		
	}
	@Override
	public String[] getAcceptedCmds() {
		String[] s = {org.cbook.cbookif.Constants.USER_INPUT, "double", "expression"};
		return s;
	}

	@Override
	public String[] getSendCmds() {
		String[] s = {org.cbook.cbookif.Constants.USER_INPUT , "double", "expression"};
		return s;
	}

	@Override
	public String getLocalizedCmd(String cmd) {
		return WiskOpdr.rb.getString(CBA_PREFIX + cmd);
	}

}
