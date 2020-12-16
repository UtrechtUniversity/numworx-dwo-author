package fi.wiskopdr;

import java.awt.Color;
import java.awt.Component;
import java.util.Hashtable;

import javax.swing.JPanel;
import javax.swing.UIManager;

import fi.wiskopdr.opdrnav.OpdrNavStruct;
import fi.wiskopdr.tekstobjects.LinkIF;
import fi.wiskopdr.tekstobjects.TekstImageVak;
	

public class WiskOpdrPanel extends JPanel
{
	private Hashtable launchData;
	private OpdrNavStruct ons;
	private fi.wiskopdr.opdrnav.MyOpdrContainer opdrContainer;
	private WiskOpdr owner;
		
	public WiskOpdrPanel(Hashtable launchData, WiskOpdr applet)
	{	setLayout(null);
		setOpaque(false);
		//super.setSize(700,300); 
		this.owner = applet;
		this.launchData = launchData;
		TekstImageVak.setImageMapString((String)launchData.get(TekstImageVak.IMAGE_MAP));
		
//		String langArg = getParameter("language");
//        if ( langArg == null || langArg.equals("")) langArg = "en";
//        Locale language = new Locale (langArg, "");
//        WiskOpdr.rb = ResourceBundle.getBundle("fi.wiskopdr.text.Text",language);
        
	    if(WiskOpdr.lookAndFeel==null) WiskOpdr.lookAndFeel = UIManager.getLookAndFeel();
	    opdrContainer = new  fi.wiskopdr.opdrnav.MyOpdrContainer(0,0,800, 350);  
		ons = new OpdrNavStruct(null, opdrContainer,0,0,800, 350, null, launchData);
		//opdrContainer.setNewScrollSize();
		
		JPanel contentPane = opdrContainer.getPlainPane();
		contentPane.setBackground(Color.white);
		contentPane.setLocation(0,0);
		super.setSize(contentPane.getSize());
		setPreferredSize(contentPane.getSize());
		add(contentPane);
		
	}
	
	public void setJSObjectOwner(LinkIF applet)
	{
		owner.setJSObjectOwner(applet);
	}
	
	//TODO
	public void setSize(int b, int h)
	{	super.setSize(b,h);
		//if (opdrContainer!=null)opdrContainer.getPlainPane().setSize(b,h);
	}
	
	public String getParameter(String name)
	{	String value = (String)launchData.get(name);
		return value;
	}
	
	
	public Component getComponent()
	{   return this;
	} 
	
	
	
	public void end()
    {
	  if(ons!=null) {
          opdrContainer.closePopups();
          ons.destroy();
        }
	}
	
    public void reset()
    {   if(ons!=null)ons.stop();
	}	
    
   
	
}
