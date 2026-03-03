
package fi.algebrapijlenopdr;

import java.awt.*;
import java.awt.image.*;
import java.net.*;
import java.io.*;
import java.util.*;
import java.awt.event.*;

import fi.beans.mainframe.AppletContext;
import fi.beans.mainframe.AppletStub;
import fi.beans.mainframe.JApplet;
import fi.beans.scorm.*;


public class MainFrame extends Frame   implements WindowListener, AppletStub, AppletContext
{
    private JApplet applet;

    public MainFrame( JApplet applet, int width, int height )
	{	this.applet = applet;
		addWindowListener(this);
		applet.setStub( this );
		setLayout( new BorderLayout() );
		add( "Center", applet );
		applet.setSize( width, height );
		applet.init();
		applet.start();
	}
        
	public void windowClosing(WindowEvent e)
	{	applet.stop();
		dispose();
		System.exit(0);
	}
	public void windowOpened(WindowEvent e){}
	public void windowIconified(WindowEvent e){doLayout();}
	public void windowDeiconified(WindowEvent e){doLayout();}
	public void windowClosed(WindowEvent e){}
	public void windowActivated(WindowEvent e){}
	public void windowDeactivated(WindowEvent e){}
    
    // AppletStub methodes
    public boolean isActive(){return true;}
    public URL getDocumentBase(){return null;}
	public URL getCodeBase(){return null;}
	public String getParameter(String name){return null;}
    public void appletResize( int width, int height ){}
    public AppletContext getAppletContext(){return this;}
    	
    // AppletContext methodes
    public Image getImage( URL url )
    {	Toolkit tk = Toolkit.getDefaultToolkit();
		try
		{	ImageProducer prod = (ImageProducer) url.getContent();
		    return tk.createImage( prod );
		}
		catch ( IOException e )
		{
			return null;
		}
	}
    public void setStream(String s, InputStream is){}
    public InputStream getStream(String s){return null;}
    public Iterator getStreamKeys(){return null;}
	public void showDocument( URL url ){}
    public void showDocument( URL url, String target ){}
    public void showStatus( String status ){}
}