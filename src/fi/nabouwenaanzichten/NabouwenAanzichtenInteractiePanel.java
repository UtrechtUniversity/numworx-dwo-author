package fi.nabouwenaanzichten;

import java.applet.Applet;
import java.awt.event.*;
import java.awt.*;
import java.util.*;

import javax.swing.*;

import fi.beans.scorm.*;
import fi.beans.copyright.*;
import fi.beans.base64code.*;

import fi.beans.wiskopdrbeans.InteractieEditPanel;
import fi.beans.wiskopdrbeans.InteractiePanel;

/**
 * @author Peter Boon
 */

public class NabouwenAanzichtenInteractiePanel extends JPanel implements InteractiePanel, InteractieEditPanel, NabouwenAanzichtenIF, NumberListener, ActionListener
{	
	protected SCORM12APIInterface api;
	
	private FIButton fiButton;
	protected static ResourceBundle rb;
	private String langArg;
	private String bgColorArg;
	private Button volLeegKnop, aanzichtenKnop;
	 Viewer3d v;
	private VaktekPanel vp;
	private KubusRooster kr;
	private NumberArrow na;
	private Label aantalKLabel;
	private boolean aanzichten ;
	InvulKeuzePanel ip;
	private Color bgcolor = Color.white;
    private boolean mobileVersion;
	
	
	public NabouwenAanzichtenInteractiePanel()
	{	setLayout(null);
		super.setBounds(0,0,300,300);
		//setBackground(new Color(230,240,255));
		kr = new KubusRooster(4,1);
		
		aanzichten = true;

        ip = new InvulKeuzePanel(40,350,120,50);
		//ip.setBackground(bgcolor);
		//add(ip);
		
		aantalKLabel = new Label(NabouwenAanzichten.rb.getString("aantalKLabel")+ kr.geefAantalK());
		aantalKLabel.setBounds(40,460,200,20);
		aantalKLabel.setFont(new Font("SansSerif",Font.PLAIN,18));
		//add(aantalKLabel);
		
        v = new Viewer3d(kr, 0, 0, 300, 300, this);
        v.zetBeginHoeken(30,-30);
		add(v);
		
		
		
        if(mobileVersion) vp = new VaktekPanel(kr,0,175,130,130,3, this);
        else vp = new VaktekPanel(kr,0,20,240,240,3, this);
        vp.zetKlikAan(false);
		vp.zetPijlAan(false);
		vp.zetAchtergrond(bgcolor);
		//add(vp);
		
		
		na = new NumberArrow(2,15,4,1,0,"","");
		na.setBounds(120,400,100,50);
		na.setColumns(1);
		na.addNumberListener(this);
        //if(!mobileVersion) add(na);
		
		volLeegKnop = new Button(NabouwenAanzichten.rb.getString("volLeegKnopLabel1"));
        volLeegKnop.addActionListener(this);
        volLeegKnop.setBounds(40,415,80,24);
        
        if(mobileVersion) 
        {  volLeegKnop.setFont(new Font("SansSerif", Font.PLAIN, 10));
           volLeegKnop.setBounds(145,180,55,15);
        }
		//add(volLeegKnop);
		
		
	}
	
	public void setBounds(int x, int y, int b, int h)
	{	if(v!=null)remove(v);
		v = new Viewer3d(kr, 0, 0, b, h, this);
		v.zetAchtergrond(bgcolor);
	    v.zetBeginHoeken(30,-30);
		add(v);
		super.setBounds(x,y,b,h);
	}
	
	public void setBackground(Color color)
	{	bgcolor = color;
		if(v!=null)v.zetAchtergrond(color);
		if(vp!=null)vp.zetAchtergrond(color);
		if(ip!=null)ip.setBackground(color);
		super.setBackground(color);
	}
	
	
	public void setState(Hashtable h)
	{	String state = null;
		
		if(h.containsKey("state")) state = (String)h.get("state");
		
		if(state==null)return;
			
		Object o = StringCodeObject.decodeStringToObject(state);
		boolean[][][][] booleanKRs = (boolean[][][][])o;
		
		for(int i=0 ; i<booleanKRs.length; i++)
	    {	kr = new KubusRooster(booleanKRs[i],1); //later uitbreiden naar meer kubusroosters
	    }
	    v.zetKubusRooster(kr);
	    vp.zetKubusRooster(kr);
	    na.setValue(kr.maxAantal);
	    
	}
	
	public void zetOpdracht(Hashtable h , String[] variables, Hashtable values)
	{	
		String state = null;
		
		if(h.containsKey("state")) state = (String)h.get("state");
		
		if(state==null)return;
		
		Object o = StringCodeObject.decodeStringToObject(state);
		boolean[][][][] booleanKRs = (boolean[][][][])o;
		
		for(int i=0 ; i<booleanKRs.length; i++)
	    {	kr = new KubusRooster(booleanKRs[i],1); //later uitbreiden naar meer kubusroosters
	    }
	    v.zetKubusRooster(kr);
	    vp.zetKubusRooster(kr);
	    na.setValue(kr.maxAantal);
	    v.zetAchtergrond(getBackground());
	    
	}
	
	public Hashtable getState()
	{	int aantalKR = 0;
		boolean[][][][] booleanKRs = null;
		
		aantalKR = 1;
		booleanKRs = new boolean[aantalKR][][][];
		for(int i=0 ; i<aantalKR; i++)
	    {	booleanKRs[i] = kr.geefBooleanRooster(); //later uitbreiden naar meer kubusroosters
	    }
	
	    String state = StringCodeObject.encodeObjectToString(booleanKRs);
	    Hashtable h = new Hashtable();
	    h.put("state", state);
	    return h;
	}
	
	
	public int getScore()
	{	//hier wordt de score berekend
		return 0;
	}
	
	public int getScoreMax()
	{	//hier wordt de score berekend
		return 0;
	}
    
    public boolean hasEditMode()
    {   return false;
    }
    
    public ScormEditComponentIF getEditComponent(Hashtable launchData)
    {   return null;
    }
    
    public Parameter[] getEditableParameters()
    {   return null;
    }
    
    public Parameter[] getAllParameters()
    {   return null;
    }
    public boolean isFocusTraversable()
    {   return false;
    }
    
	
	public void zetVeranderd()
	{	v.tekenOpnieuw();
		aantalKLabel.setText(NabouwenAanzichten.rb.getString("aantalKLabel")+ kr.geefAantalK());
		if(aanzichten)
		{	vp.ra.tekenOpnieuw();
			vp.ba.tekenOpnieuw();
			vp.va.tekenOpnieuw();
		}
	}
	
	
	public boolean isBouwen()
	{	return ip.isBouwen();		
	}
	
	public KubusRooster geefKubusRooster()
	{	return kr;
	}
	public void zetKubusRooster(KubusRooster k)
	{	kr = k;
		v.zetKubusRooster(kr);
		vp.zetKubusRooster(kr);
		volLeegKnop.setLabel(NabouwenAanzichten.rb.getString("volLeegKnopLabel1"));
		zetVeranderd();
		na.setValue(kr.maxAantal);
	}
	public void numberChanged(String name, double val)
	{	na.transferFocus();
		kr = new KubusRooster((int)val,1);
		v.zetKubusRooster(kr);
		vp.zetKubusRooster(kr);
		volLeegKnop.setLabel(NabouwenAanzichten.rb.getString("volLeegKnopLabel1"));
		zetVeranderd();

	}
	
	public void actionPerformed(ActionEvent e)
	{	if(e.getSource()==volLeegKnop)
		{	volLeegKnop.transferFocus(); 
			if(volLeegKnop.getLabel().equals(NabouwenAanzichten.rb.getString("volLeegKnopLabel1")))
			{	kr.maakVol();
				volLeegKnop.setLabel(NabouwenAanzichten.rb.getString("volLeegKnopLabel2"));
				zetVeranderd();
			}
			else
			{	kr.maakLeeg();
				volLeegKnop.setLabel(NabouwenAanzichten.rb.getString("volLeegKnopLabel1"));
				zetVeranderd();
			}
		}
		if(e.getSource()==aanzichtenKnop)
		{	aanzichtenKnop.transferFocus(); 
			if(aanzichtenKnop.getLabel().equals(NabouwenAanzichten.rb.getString("aanzichtenKnopLabel1")))
			{	vp.setVisible(true);
				aanzichten = true;
				aanzichtenKnop.setLabel(NabouwenAanzichten.rb.getString("aanzichtenKnopLabel2"));
				zetVeranderd();
			}
			else
			{	vp.setVisible(false);
				aanzichten = false;
				aanzichtenKnop.setLabel(NabouwenAanzichten.rb.getString("aanzichtenKnopLabel1"));
				zetVeranderd();
			}
		}
	}
	
	public void setEditState(Hashtable h)
	{	String state = null;
		
		if(h.containsKey("state")) state = (String)h.get("state");
		
		if(state==null)return;
			
		Object o = StringCodeObject.decodeStringToObject(state);
		boolean[][][][] booleanKRs = (boolean[][][][])o;
		
		for(int i=0 ; i<booleanKRs.length; i++)
	    {	kr = new KubusRooster(booleanKRs[i],1); //later uitbreiden naar meer kubusroosters
	    }
	    v.zetKubusRooster(kr);
	    vp.zetKubusRooster(kr);
	    na.setValue(kr.maxAantal);
	}
	
	public Hashtable getEditState()
	{	int aantalKR = 0;
		boolean[][][][] booleanKRs = null;
		
		aantalKR = 1;
		booleanKRs = new boolean[aantalKR][][][];
		for(int i=0 ; i<aantalKR; i++)
	    {	booleanKRs[i] = kr.geefBooleanRooster(); //later uitbreiden naar meer kubusroosters
	    }
	
	    String state = StringCodeObject.encodeObjectToString(booleanKRs);
	    Hashtable h = new Hashtable();
	    h.put("state", state);
	    return h;
	}
	
	public InteractieEditPanel getEditPanel(){return new NabouwenAanzichtenInteractiePanel();}
		
	public void wis(){}
	
	public int geefAsHoogte(){return 0;}
	
	public boolean isCorrect(){return true;}
	
	public boolean isFout(){return false;}
	
	public void zetMode(int mode){}
	
	public void zetNagekeken(boolean b){}
	
    public void stop(){}
    
    public void destroy(){}
    
    public void zetMaat(){}
	
    public void start(){}
     
    public void opnieuw(){}
    
    public void kijkNa(){}
    
    public void kijkNa(int stapNr){}
    
    public void addActionListener(ActionListener al){}
	
}