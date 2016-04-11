package fi.wiskopdr;

import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Hashtable;

import javax.swing.*;

import org.cbook.cbookif.CBookEvent;
import org.cbook.cbookif.CBookEventHandler;
import org.cbook.cbookif.CBookEventListener;

import fi.wiskopdr.formuleobjects.FormuleButton;
import fi.wiskopdr.formuleobjects.FormuleVak;
import fi.wiskopdr.formuleobjects.FormuleElement;
import fi.wiskopdr.formuleobjects.TabletOwner;
import fi.wiskopdr.formuleobjects.FormuleVakHouder;
import fi.wiskopdr.opdrnav.MyOpdrContainer;
import fi.wiskopdr.opdrnav.OpdrNavStruct;
import fi.wiskopdr.tekstobjects.EditInteractiePanelDialog;
import fi.wiskopdr.tekstobjects.TekstElement;
import fi.wiskopdr.tekstobjects.TekstInteractiePanelVak;
import fi.wiskopdr.tekstobjects.TekstVak;
import fi.beans.wiskopdrbeans.CBookAware;
import fi.beans.wiskopdrbeans.InteractieEditPanel;
import fi.beans.wiskopdrbeans.InteractiePanel;

public class SimpelAntwoordVergelijkingVak extends JPanel implements InteractiePanel, ActionListener, MouseListener, FormuleVakHouder, CBookAware
{
	private AntwoordVergelijkingVak antwoordVergelijkingVak;
	private Component formuleComponent, scoreGoedComponent, scoreFoutComponent, scoreHalfComponent;
	private Component feedbackComponent;
	private ImageComponent scoreToetsComponent;
	private int ashoogte;
	private int minBreedte;
	private int mode;
	private boolean tabletAan;
	private boolean formuleToolBijFocus;
	
	private FormuleButton feedbackButton;
	private JPanel feedbackPanel;
	
	
	private Font formuleVakFont = WiskOpdr.mac ? WiskOpdr.formuleFont1Mac : WiskOpdr.formuleFont1; //new Font("TimesRoman",Font.PLAIN,16);
	
	private DialogFacade popupFrame;
	private FormuleButton popupButton;
	
	private boolean antwoordVergelijkingVakActief = false;
	private boolean vakUitwerking = false;
	private boolean boxMetRand = true;
	
	private boolean uitklapVak;
	
	private CBookEventHandler cbookEventHandler = new CBookEventHandler(this);
		
	static boolean fontOvererving;
	
	public static void zetFontOverervingForm(boolean b)
	{	fontOvererving = b;
	}
	
	public SimpelAntwoordVergelijkingVak()
	{
		setLayout(null);
		
		addMouseListener(this);
		
		if(!WiskOpdr.formTimes) formuleVakFont = WiskOpdr.tekstFont;
		
		
		antwoordVergelijkingVak = new AntwoordVergelijkingVak();
		antwoordVergelijkingVak.addActionListener(this);
		antwoordVergelijkingVak.setBounds(0,0,400,300);
		//antwoordVergelijkingVak.zetStappen(false);
		
		formuleComponent = antwoordVergelijkingVak.getComponentSimpel();
		formuleComponent.setFont(formuleVakFont);
		((FormuleVak)formuleComponent).setBorder(false);
		((FormuleVak)formuleComponent).addActionListener(this);
		formuleComponent.setLocation(0,0);
		add(formuleComponent);
		
		scoreGoedComponent = antwoordVergelijkingVak.getGoedIC();
		scoreGoedComponent.setLocation(20,0);
		((ImageComponent)scoreGoedComponent).zetKlein(true);
		add(scoreGoedComponent);
		scoreFoutComponent = antwoordVergelijkingVak.getFoutIC();
		scoreFoutComponent.setLocation(20,0);
		((ImageComponent)scoreFoutComponent).zetKlein(true);
		add(scoreFoutComponent);
		scoreHalfComponent = antwoordVergelijkingVak.getHalfIC();
		scoreHalfComponent.setLocation(20,0);
		((ImageComponent)scoreHalfComponent).zetKlein(true);
		add(scoreHalfComponent);
		
		feedbackComponent =antwoordVergelijkingVak.getFeedbackIC();
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
		String title =  WiskOpdr.rb.getString("TVEP_uitwerkingenPopup");
		popupFrame = DialogFacade.newInstance(this, title);
		
			popupFrame.getContentPane().setLayout(null);
			if(antwoordVergelijkingVak!=null)popupFrame.getContentPane().add((Component)antwoordVergelijkingVak,0);
		  
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
				antwoordVergelijkingVak.setBounds(x,y,b,h);
				//resize();
			}
		});
	    
	    antwoordVergelijkingVak.zetStappen(true);
	    
	    //((FormuleVak)formuleComponent).setBorder(true);
		//((FormuleVak)formuleComponent).addActionListener(this);
		//formuleComponent.setLocation(0,0);
		//antwoordFormuleVak.add(formuleComponent);
	  
	    
	}
	
	public void closePopup()
	{	antwoordVergelijkingVak.stapLeegTerug();
		formuleComponent = antwoordVergelijkingVak.getComponentSimpel();
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
		
		antwoordVergelijkingVakActief = false;
		
		zetMaat();
		if(mode==0 || mode==1 || antwoordVergelijkingVak.nagekeken)kijkNa();
		((FormuleVak)formuleComponent).setEditable(true);
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
			antwoordVergelijkingVak.getComponentSimpel().setFont(formuleVakFont);
		}
		antwoordVergelijkingVak.zetOpdracht(h, randomVars, randomValues);
		scoreGoedComponent.setLocation(getSize().width-18,0);
		scoreFoutComponent.setLocation(getSize().width-18,0);
		scoreHalfComponent.setLocation(getSize().width-18,0);
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
		antwoordVergelijkingVak.setEditState(h);
		antwoordVergelijkingVak.zetStartString("$f@"); // hierdoor komt het simpel antwoordvak netjes in de regel te liggen in de editor (forceert zetMaat())
		requestFocus(); //want antwoordFormuleVak moet geen focus krijgen in de editor
		
		if(h.containsKey("uitw")) vakUitwerking = ((Boolean)h.get("uitw")).booleanValue();
		if(h.containsKey("boxMetRand")) boxMetRand = ((Boolean)h.get("boxMetRand")).booleanValue();
		
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
	{	return antwoordVergelijkingVak.getEditState();
	}
	
	public void setState(Hashtable h) 
	{	remove(formuleComponent);
		antwoordVergelijkingVak.setState(h);
		formuleComponent = antwoordVergelijkingVak.getComponentSimpel();
		((FormuleVak)formuleComponent).zetStippels(!boxMetRand);
		formuleComponent.setFont(formuleVakFont);
		((FormuleVak)formuleComponent).setBorder(false);
		((FormuleVak)formuleComponent).addActionListener(this);
		formuleComponent.setLocation(0,0);
		add(formuleComponent);
		zetMaat();
		
		int uitw = vakUitwerking?20:0;
		scoreGoedComponent.setLocation(getSize().width-18-uitw,0);
		scoreFoutComponent.setLocation(getSize().width-18-uitw,0);
		scoreHalfComponent.setLocation(getSize().width-18-uitw,0);
		
		if(scoreToetsComponent!=null) scoreToetsComponent.setLocation(getSize().width-18-uitw,0);
		((FormuleVak)formuleComponent).setEditable(true);
	}
	
	public Hashtable getState() 
	{	return antwoordVergelijkingVak.getState();
	}
	
	public void paintComponent(Graphics g)
	{	//g.setColor(Color.white);
		//g.fillRect(1,2,getSize().width-2, getSize().height-4);
		//g.setColor(Color.gray);
		//g.drawRect(1,2,getSize().width-2, getSize().height-4);
		int uitw = (vakUitwerking && !uitklapVak)?20:0;
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
		else if(boxMetRand)g.drawRect(1,2,getSize().width-2-uitw, getSize().height-4);
		
	}
	
	public void zetMaat()
	{	
		int uitw = (vakUitwerking && !uitklapVak)?20:0;
		setSize(Math.max(minBreedte+uitw, formuleComponent.getSize().width+24+uitw), formuleComponent.getSize().height+8);
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
			{	if(antwoordVergelijkingVak!=null)
				{	uitklapTvp.addActionListener(this);
					int x = uitklapTvp.geefTekstVak(1, 0).geefMarge();
					int y = uitklapTvp.geefTekstVak(1, 0).getY() + uitklapTvp.geefTekstVak(1, 0).geefMargeBoven();//uitklapTvp.cellMarge;
					int b = uitklapTvp.geefTekstVak(1, 0).getWidth() - 2*uitklapTvp.geefTekstVak(1, 0).geefMarge();
					int h = uitklapTvp.geefUitklapHoogte(1) - 2*uitklapTvp.geefTekstVak(1, 0).geefMargeBoven();
					antwoordVergelijkingVak.setBounds(x,y,b,h);
					uitklapTvp.add(antwoordVergelijkingVak,0);
					antwoordVergelijkingVak.zetStappen(true);
					return true;
				}
			}
		}
		return false;
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
	
	public void actionPerformed(ActionEvent e) 
	{	if(e.getSource()==antwoordVergelijkingVak && !antwoordVergelijkingVakActief)
		{	
		
			if(e.getActionCommand().equals("feedback"))
			{	if(antwoordVergelijkingVak.hasFeedback() || !antwoordVergelijkingVak.isCorrect())
				{	feedbackButton.setVisible(true);
				}
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
			//if(e.getActionCommand().equals("feedbackWeg"))
			//{	if(scoreToetsComponent!=null)remove(scoreToetsComponent);
			//	repaint();
			//	return;
			//}
		
			produceAction(e.getActionCommand());
			
			int uitw = vakUitwerking?20:0;
			scoreGoedComponent.setLocation(getSize().width-18-uitw,0);
			scoreFoutComponent.setLocation(getSize().width-18-uitw,0);
			scoreHalfComponent.setLocation(getSize().width-18-uitw,0);
			
			if((mode==2 || mode==3) && (e.getActionCommand().equals("changed")|| e.getActionCommand().equals("checked")))
			{	if(scoreToetsComponent!=null) remove(scoreToetsComponent);
				if(antwoordVergelijkingVak.isCorrect()) scoreToetsComponent = new ImageComponent(WiskOpdr.GOEDKRUL);
				else if(antwoordVergelijkingVak.isFout()) scoreToetsComponent = new ImageComponent(WiskOpdr.FOUTKRUIS);
				else scoreToetsComponent = new ImageComponent(WiskOpdr.HALFKRUL);
				scoreToetsComponent.setLocation(getSize().width-18-uitw,0);
				((ImageComponent)scoreToetsComponent).zetKlein(true);
				add(scoreToetsComponent);
				repaint();
				
			}
			
			if(antwoordVergelijkingVak.isCorrect())cbookEventHandler.fire("action.correct");
			if(antwoordVergelijkingVak.isFout())cbookEventHandler.fire("action.false");
			if(antwoordVergelijkingVak.isFout() && antwoordVergelijkingVak.getErrorCount()>1)cbookEventHandler.fire("action.false_2");
			
			//((FormuleVak)formuleComponent).setEditable(true);
		}
		else if(e.getSource()==formuleComponent && e.getActionCommand().equals("focus"))
		{	zetTabletUser();
		}
		else if(e.getSource()==feedbackButton)
		{	Component c = antwoordVergelijkingVak.getFeedbackComponent();
			if("MW".equals(WiskOpdr.deployVariant) || "GR".equals(WiskOpdr.deployVariant)) c = antwoordVergelijkingVak.getMWFeedbackComponent();
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
					int width = ((Component)antwoordVergelijkingVak).getSize().width + popupFrame.getInsets().left + popupFrame.getInsets().right;
					int height = ((Component)antwoordVergelijkingVak).getSize().height + popupFrame.getInsets().top + popupFrame.getInsets().bottom;
					
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
				
			if(feedbackPanel.getParent()!=null)
			{	Container c = feedbackPanel.getParent();
				c.remove(feedbackPanel);
				c.repaint();
			}
			
			feedbackButton.setVisible(false);
            if(scoreToetsComponent!=null)remove(scoreToetsComponent);
            repaint();
            
			antwoordVergelijkingVakActief = true;
			antwoordVergelijkingVak.zetSimpelFormuleVak((FormuleVak)formuleComponent);
			antwoordVergelijkingVak.formuleVak.requestFocus();
		}
		else if(e.getSource()==popupButton || e.getActionCommand().equals("tvpKlapIn"))
		{	closePopup();
		
		}
	}
		

	public FormuleVak geefFormuleVak()
	{	return (FormuleVak)formuleComponent;
	}
	
	public void zetTabletAan(boolean b)
	{	tabletAan = b;
	}

	public void destroy() {
		// TODO Auto-generated method stub

	}

	public InteractieEditPanel getEditPanel()
	{	return new AntwoordVergelijkingVakEditPanel(3);
	}

	public int getIpId(){return 0;}
	
	public String getIpExpString(){return null;}

	public int getScore() 
	{	return antwoordVergelijkingVak.getScore();
	}
	
	public int[][] getScoreObjectives()
	{	return antwoordVergelijkingVak.getScoreObjectives();
	}

	public int getScoreMax() 
	{	return antwoordVergelijkingVak.getScoreMax();
	}

	public boolean isCorrect() 
	{	return antwoordVergelijkingVak.isCorrect();
	}

	public boolean isFout() 
	{	return antwoordVergelijkingVak.isFout();
	}

	public void kijkNa() 
	{	antwoordVergelijkingVak.kijkNa();
		((FormuleVak)formuleComponent).setEditable(true);
	}

	public void kijkNa(int stapNr) 
	{	antwoordVergelijkingVak.kijkNa(stapNr);
		((FormuleVak)formuleComponent).setEditable(true);
	}

	public void opnieuw() 
	{	antwoordVergelijkingVak.opnieuw();
	}

	

	
	public void start() 
	{	antwoordVergelijkingVak.start();
	}
	
	public void stop() 
	{	if(popupFrame!=null)
		{	popupFrame.setVisible(false);
			closePopup();
			antwoordVergelijkingVak.stop();
			popupFrame.dispose();
		}
		else antwoordVergelijkingVak.stop();
	}

	public void wis() 
	{	antwoordVergelijkingVak.wis();
	}

	public void zetMode(int mode) 
	{	antwoordVergelijkingVak.zetMode(mode);
		this.mode = mode;
	}
	
	public void zetNagekeken(boolean b) 
	{	antwoordVergelijkingVak.zetNagekeken(b);
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
	
	public void activateTablet()
	{	
		Container parent = getParent();
		int x = parent.getLocation().x;
		int y = parent.getLocation().y;
		int h = parent.getSize().height;
		for(int i=0 ; parent!=null && i<40 ; i++)
		{	if(parent instanceof TabletOwner) 
			{	((TabletOwner)parent).addTablet(this, x+20, y+h+20);
				break;
			}
			else 
			{	parent = parent.getParent();
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
		// TODO Auto-generated method stub
		
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
		String[] s = {};
		return s;
	}

	@Override
	public String[] getSendCmds() {
		String[] s = {				
				"action.correct",
				"action.false",
				"action.false_2"};
		return s;
	}

	@Override
	public String getLocalizedCmd(String cmd) {
		return WiskOpdr.rb.getString(CBA_PREFIX + cmd);
	}

}
