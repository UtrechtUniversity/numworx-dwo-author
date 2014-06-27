package fi.wiskopdr.tekstobjects;

import java.awt.*;
import java.awt.event.*;
import java.util.Hashtable;

import fi.wiskopdr.formuleobjects.*;
import fi.wiskopdr.tekstobjects.*;
import fi.wiskopdr.AntwoordFormuleVak;
import fi.wiskopdr.AntwoordVakEditPanel;

import fi.beans.wiskopdrbeans.InteractieEditPanel;
import fi.beans.wiskopdrbeans.InteractiePanel;

public class TekstAntwoordFVak extends TekstDeelVak implements ActionListener, InteractiePanel, MouseListener
{
	private AntwoordFormuleVak antwoordFormuleVak;
	Component formuleComponent;
	//private Font font = new Font("TimesRoman",Font.PLAIN,16);
	private FontMetrics fm;
	private boolean selected = false;
	
	private Hashtable launchData;
	private boolean editMode;
	
	public TekstAntwoordFVak(TekstVak tv)
	{	super(tv);
		setBackground(Color.white);
		
		addMouseListener(this);
		
		
	
		antwoordFormuleVak = new AntwoordFormuleVak();
		formuleComponent = antwoordFormuleVak.getComponentSimpel();	
		formuleComponent.setLocation(4,4);
		//add(formuleComponent);
		
		Font f = new Font("TimesRoman", tv.getFont().getStyle(), tv.getFont().getSize()+2);
		setFont(f);
		
		setSize(formuleComponent.getSize().width+34, formuleComponent.getSize().height+8);
		ashoogte = ((FormuleElement)formuleComponent).ashoogte+4;
		
		launchData = new Hashtable();
		launchData.put("soortInteractiePanel", new Integer(5));
		launchData.put("interactiePanelLaunchState", new Hashtable());
	}
	
	public void setFont(Font font)
	{	super.setFont(font);
		if(formuleComponent==null) return;
		Font f = new Font("TimesRoman", font.getStyle(), font.getSize()+2);
		((FormuleElement)formuleComponent).setFont(f);
		ashoogte = ((FormuleElement)formuleComponent).ashoogte+4;
	}
	
	public void setEditMode(boolean b)
	{	editMode = b;
        if(b)setBackground(Color.lightGray);
		
	}
	
	public InteractieEditPanel getEditPanel()
	{	return null;
	}
		
	public void setEditState(Hashtable h)
	{
		launchData = h;
		int soortInteractiePanel = 5;
		Hashtable interactiePanelLaunchState = null;
        int breedte = 0;
        int hoogte = 0;
		
		if(h.containsKey("soortInteractiePanel")) soortInteractiePanel = ((Integer)h.get("soortInteractiePanel")).intValue();
		if(h.containsKey("interactiePanelLaunchState")) interactiePanelLaunchState = (Hashtable)h.get("interactiePanelLaunchState");
        if(h.containsKey("breedte")) breedte = ((Integer)h.get("breedte")).intValue();
        if(h.containsKey("hoogte")) hoogte = ((Integer)h.get("hoogte")).intValue();
        
		
		
	}
	
	public Hashtable getEditState()
	{	
		return launchData;
	}
	
	public void zetOpdracht(Hashtable h, String[] randomVars, Hashtable randomValues)
	{	
		launchData = h;
		
		int soortInteractiePanel = 5;
		Hashtable interactiePanelLaunchState = null;
        int breedte = 0;
        int hoogte = 0;
        
        if(h.containsKey("soortInteractiePanel")) soortInteractiePanel = ((Integer)h.get("soortInteractiePanel")).intValue();
        if(h.containsKey("interactiePanelLaunchState")) interactiePanelLaunchState = (Hashtable)h.get("interactiePanelLaunchState");
        if(h.containsKey("breedte")) breedte = ((Integer)h.get("breedte")).intValue();
        if(h.containsKey("hoogte")) hoogte = ((Integer)h.get("hoogte")).intValue();
        
        add(formuleComponent,0);
		antwoordFormuleVak.zetOpdracht(interactiePanelLaunchState, randomVars, randomValues);
		zetMaat();
		
		
	}
	
	public void setState(Hashtable h)
	{
		
		
	}
	
	public Hashtable getState()
	{	
	
		return null;
	}
	
		
	public int geefAsHoogte()
	{
		return ashoogte;
	}
	
	public void paint(Graphics g)
	{	g.setColor(getBackground());
		g.fillRect(1,0,getSize().width-2, getSize().height);
		super.paint(g);
	}
	
	
	public void zetMode(int mode)
	{	
	}
	
	public void zetNagekeken(boolean b)
	{	
	}
	
	
	public void wis()
    {   
    }
	
	public int getIpId(){return 0;}
	
	public String getIpExpString(){return null;}
	
	public int getScore()
	{	return 0;
	}
	
	public int[][] getScoreObjectives()
	{	return null;
	}
	
	public int getScoreMax()
	{	return 0;
	}

	public boolean isCorrect()
	{	return true;
	}
	
	public boolean isFout()
	{	return false;
	}
    
    public void stop()
    {   
    }
    
    public void start()
    {   
    }
    
    public void destroy()
    {
        //interactiePanel.destroy();
    }
    
    public void opnieuw()
    {   
    }
    
    public void kijkNa()
    {   
    }
    
    public void kijkNa(int stapNr)
    {   
    }
	
	public void vulVak(String s)
	{	
	}
	
	
	
	public void setEditable(boolean b)
	{	
	}
	
	public void setSelected(boolean b)
	{	selected = b;
	}
	
	public boolean isSelected()
	{	return selected;
	}
	
	public void zetMaat()
	{	
		setSize(formuleComponent.getSize().width+34, formuleComponent.getSize().height+8);
		formuleComponent.setLocation(4,4);
		ashoogte = ((FormuleElement)formuleComponent).ashoogte+4;
		if(getParent()instanceof FormuleElement)((FormuleElement)getParent()).zetMaat();
		if(getParent()instanceof TekstElement)((TekstElement)getParent()).zetMaat();
	}
	
	public String toString()
	{	return "$V@";// + antwoordVak.getText() + "@";
	}
	
	public void actionPerformed(ActionEvent e)
	{	if(e.getActionCommand().equals("focus"))requestFocus();//tekstVak.zetAntwoordVak(antwoordVak);
	}
	
	public void mousePressed(MouseEvent e)
	{	if(!editMode)return;
		//launchData = EditInteractiePanelDialog.editInteractiePanel(launchData);
		setEditState(launchData);
	}
	
	public void mouseClicked(MouseEvent e){;}
	public void mouseReleased(MouseEvent e){;}
	public void mouseEntered(MouseEvent e){;}
	public void mouseExited(MouseEvent e){;}
	
}

/*
package fi.wiskopdr.tekstobjects;

import java.awt.*;
import java.awt.event.*;
import java.util.Hashtable;

import fi.wiskopdr.formuleobjects.*;
import fi.wiskopdr.tekstobjects.*;
import fi.wiskopdr.InteractieEditPanel;
import fi.wiskopdr.InteractiePanel;
import fi.wiskopdr.AntwoordVakEditPanel;
import fi.wiskopdr.AntwoordFormuleVak;
import fi.wiskopdr.AntwoordVergelijkingVak;

public class TekstAntwoordVak extends TekstDeelVak implements ActionListener, InteractiePanel
{
	private TekstArea antwoordVak;
	private Font font = new Font("SansSerif",Font.PLAIN,12);
	private FontMetrics fm;
	private boolean selected = false;
	
	private FormuleButton editButton;
	private InteractiePanel interactiePanel;
	private Hashtable launchData;
    
    private boolean editMode;
	
	public TekstAntwoordVak(TekstVak tv)
	{	super(tv);
		setFont(font);
		fm = getFontMetrics(getFont());
	
		antwoordVak = new TekstArea();
		antwoordVak.setBounds(0,0,tv.getSize().width-10,20);
		antwoordVak.setEditable(true);
		
		editButton = new FormuleButton("ed");
		editButton.setBounds(tv.getSize().width-30,1,19,18);
		editButton.addActionListener(this);
		add(editButton,0);
		editButton.setVisible(false);
		
		add(antwoordVak);
				
		setSize(antwoordVak.getSize().width,antwoordVak.getSize().height);
		
		launchData = new Hashtable();
		launchData.put("soortInteractiePanel", new Integer(5));
		launchData.put("interactiePanelLaunchState", new Hashtable());
	}
	
	public void setEditMode(boolean b)
	{	editMode = b;
        editButton.setBounds(getSize().width-20,1,19,18);
		editButton.setVisible(b);
		
	}
	
	public InteractieEditPanel getEditPanel()
	{	return new AntwoordVakEditPanel();
	}
		
	public Hashtable getEditState()
	{	*//*
		String tekst = "";
		
		tekst = antwoordVak.getText();
	
		Hashtable interactiePanelLaunchState = new Hashtable();
		interactiePanelLaunchState.put("tekst", tekst);
		
		Hashtable h = new Hashtable();
		h.put("interactiePanelLaunchState", interactiePanelLaunchState);
		h.put("soortInteractiePanel", new Integer(5));
		
		return h;
		*//*
		
		int soortInteractiePanel = 5;
		Hashtable interactiePanelLaunchState = null;
		
		if(launchData.containsKey("soortInteractiePanel")) soortInteractiePanel = ((Integer)launchData.get("soortInteractiePanel")).intValue();
		
		if(soortInteractiePanel==5)
		{
			String tekst = "";
			
			tekst = antwoordVak.getText();
		
			interactiePanelLaunchState = new Hashtable();
			interactiePanelLaunchState.put("tekst", tekst);
			
			launchData.put("soortInteractiePanel", new Integer(5));
			launchData.put("interactiePanelLaunchState", interactiePanelLaunchState);
			
		}
		
		
		return launchData;
	}
	
	public void setEditState(Hashtable h)
	{
		launchData = h;
		int soortInteractiePanel = 5;
		Hashtable interactiePanelLaunchState = null;
        int breedte = 0;
        int hoogte = 0;
		
		if(h.containsKey("soortInteractiePanel")) soortInteractiePanel = ((Integer)h.get("soortInteractiePanel")).intValue();
		if(h.containsKey("interactiePanelLaunchState")) interactiePanelLaunchState = (Hashtable)h.get("interactiePanelLaunchState");
        if(h.containsKey("breedte")) breedte = ((Integer)h.get("breedte")).intValue();
        if(h.containsKey("hoogte")) hoogte = ((Integer)h.get("hoogte")).intValue();
        
		if(soortInteractiePanel==5)
		{	interactiePanel = this;
			String tekst = "";
			if(interactiePanelLaunchState.containsKey("tekst")) tekst = (String)interactiePanelLaunchState.get("tekst");
			if(antwoordVak.getText()==null || antwoordVak.getText().trim().equals("")) antwoordVak.setText(tekst);
		}
		else if(soortInteractiePanel==7)
		{
			interactiePanel = new AntwoordFormuleVak();
			interactiePanel.setBounds(0,0,breedte,hoogte);
			antwoordVak.setBounds(0,0,((Component)interactiePanel).getSize().width,((Component)interactiePanel).getSize().height);
            if(!editMode)add((Component)interactiePanel,0);
			interactiePanel.setEditState(interactiePanelLaunchState);
			zetMaat();
			tekstVak.layoutTekst();
		}
		
	}
	
	public void zetOpdracht(Hashtable h, String[] randomVars, Hashtable randomValues)
	{	
		launchData = h;
		System.out.println(h.toString());
		
		int soortInteractiePanel = 5;
		Hashtable interactiePanelLaunchState = null;
        int breedte = 0;
        int hoogte = 0;
        
        if(h.containsKey("soortInteractiePanel")) soortInteractiePanel = ((Integer)h.get("soortInteractiePanel")).intValue();
        if(h.containsKey("interactiePanelLaunchState")) interactiePanelLaunchState = (Hashtable)h.get("interactiePanelLaunchState");
        if(h.containsKey("breedte")) breedte = ((Integer)h.get("breedte")).intValue();
        if(h.containsKey("hoogte")) hoogte = ((Integer)h.get("hoogte")).intValue();
        
        breedte = Math.min(breedte, tekstVak.getSize().width+3);
        
		System.out.println(""+soortInteractiePanel);
		
		if(soortInteractiePanel==5)
		{	interactiePanel = this;
			String tekst = "";
			if(interactiePanelLaunchState.containsKey("tekst")) tekst = (String)interactiePanelLaunchState.get("tekst");
			try         
	        {   tekst = FormuleParser.randomizeTekstVakString(tekst, randomVars, randomValues);
	        }
	        catch(Exception e)
	        {   tekst = "???";
	        }
			if(antwoordVak.getText()==null || antwoordVak.getText().trim().equals("")) antwoordVak.setText(tekst);
		}
		else if(soortInteractiePanel==7)
		{	interactiePanel = new AntwoordFormuleVak();
			interactiePanel.setBounds(-6,0,breedte,hoogte);
			antwoordVak.setBounds(0,0,((Component)interactiePanel).getSize().width,((Component)interactiePanel).getSize().height);
			add((Component)interactiePanel,0);
			interactiePanel.zetOpdracht(interactiePanelLaunchState, randomVars, randomValues);
			zetMaat();
		}
		
		
		
	}
	
	public void setState(Hashtable h)
	{
		int soortInteractiePanel = 5;
		Hashtable interactiePanelLaunchState = null;
		
		if(launchData.containsKey("soortInteractiePanel")) soortInteractiePanel = ((Integer)launchData.get("soortInteractiePanel")).intValue();
		if(launchData.containsKey("interactiePanelLaunchState")) interactiePanelLaunchState = (Hashtable)launchData.get("interactiePanelLaunchState");
		
		if(soortInteractiePanel==5)
		{
			String tekst = "";
			
			if(h.containsKey("tekst")) tekst = (String)h.get("tekst");
			
			if(antwoordVak.getText()==null || antwoordVak.getText().trim().equals("")) antwoordVak.setText(tekst);
		}
		else
		{
			if(h!=null) interactiePanel.setState(h);
		}
		
			
		
	}
	
	public Hashtable getState()
	{	
		int soortInteractiePanel = 5;
		Hashtable interactiePanelLaunchState = null;
		
		if(launchData.containsKey("soortInteractiePanel")) soortInteractiePanel = ((Integer)launchData.get("soortInteractiePanel")).intValue();
		
		if(soortInteractiePanel==5)
		{	
		
			String tekst = "";
		
			tekst = antwoordVak.getText();
		
			Hashtable h = new Hashtable();
			h.put("tekst", tekst);
		
			return h;
		}
		else
		{	return interactiePanel.getState();
			
		}
	}
	
	
	
	public void zetMode(int mode)
	{	
	}
	
	
	
	
	public void wis()
    {   
    }
	
	public int getScore()
	{	return interactiePanel.getScore();
	}

	public boolean isCorrect()
	{	return interactiePanel.isCorrect();
	}
	
	public boolean isFout()
	{	return false;
	}
    
    public void stop()
    {   
    }
    
    public void start()
    {   
    }
    
    public void destroy()
    {
        //interactiePanel.destroy();
    }
    
    public void opnieuw()
    {   
    }
    
    public void kijkNa()
    {   
    }
    
    public void kijkNa(int stapNr)
    {   
    }
	
	public void vulVak(String s)
	{	antwoordVak.setText(s);
	}
	
	public TekstArea geefAntwoordVak()
	{	return antwoordVak;
	}
	
	public void setEditable(boolean b)
	{	antwoordVak.setEditable(true);
	}
	
	public void setSelected(boolean b)
	{	selected = b;
	}
	
	public boolean isSelected()
	{	return selected;
	}
	
	public void zetMaat()
	{	if(interactiePanel!=null)
		{	Component c = ((AntwoordFormuleVak)interactiePanel).getComponentSimpel();
			setSize(c.getSize().width+30, c.getSize().height);
		}
		if(getParent()instanceof TekstElement)((TekstElement)getParent()).zetMaat();
	}
	
	public String toString()
	{	return "$A@";// + antwoordVak.getText() + "@";
	}
	
	public void actionPerformed(ActionEvent e)
	{	if(e.getActionCommand().equals("focus"))requestFocus();//tekstVak.zetAntwoordVak(antwoordVak);
		if(e.getSource()==editButton)
		{
			launchData = EditInteractiePanelDialog.editInteractiePanel(this);
			setEditState(launchData);
			
		}
	}
	
	
	
}
*/