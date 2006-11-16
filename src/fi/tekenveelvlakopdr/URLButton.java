package fi.tekenveelvlakopdr;

import java.awt.*;
import java.applet.*;
import java.awt.event.*;

import netscape.javascript.*;

public class URLButton extends Button implements ActionListener
{
	private String url = "about:blank";
	private Applet applet;
	private boolean popUpVisible;
	private int popUpWidth, popUpHeight;
	private JSObject window;
	
	public URLButton(Applet applet, String url, String label)
	{
		super(label);
		this.url = url;
		addActionListener(this);
				
		AppletContext ac = applet.getAppletContext();
		Applet ap = ac.getApplet("API");

		try
	    {	window = JSObject.getWindow(ap);
	    } 
	    catch( Exception e )
    	{	e.printStackTrace(); 
    	}
    	
    	/*if(window==null)
    	{
    		try
		    {	window = JSObject.getWindow(applet);
		    } 
		    catch( Exception e )
	    	{	e.printStackTrace(); 
	    	}
    	}*/
	}
	
	public void setURL(String url)
	{
		this.url = url;
	}
	
	public void setPopUpSize(int width, int height)
	{
		popUpWidth = width;
		popUpHeight = height;
	}
	
	public void stop()
	{
		Object[] args = new Object[1];
		args[0] = url;
		String result = null;
		if(window!=null) result = (String) window.call("ClosePopUp", args);
		popUpVisible = false;	
	}
	
	public void actionPerformed(ActionEvent e)
	{	
		//if(popUpVisible)
		//{	stop();
		//}
		//else
		{	Object[] args = new Object[5];
	        args[0] = url;
	        args[1] = "name";
	        args[2] = ""+popUpWidth;
	        args[3] = ""+popUpHeight;
	        args[4] = "yes";
	        String result = null;
			if(window!=null) result = (String) window.call("NewPopUp", args);
	        popUpVisible = true;
		}
		
	}
}
