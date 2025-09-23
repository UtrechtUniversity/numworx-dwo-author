
package fi.mozarch;

import java.applet.AudioClip;
import java.awt.*;
import java.awt.image.*;
import java.net.*;
import java.io.*;
import java.util.*;

import fi.beans.mainframe.AppletContext;
import fi.beans.mainframe.AppletStub;
import fi.beans.mainframe.JApplet;

import java.awt.event.*;
public class MainFrame extends Frame   implements WindowListener, ComponentListener, AppletStub, AppletContext
    {
	private double beginBreedte, beginHoogte;
    private String name;
    private JApplet applet;


    public MainFrame( JApplet applet, int width, int height )
	{	beginBreedte = width;		beginHoogte = height;
		addWindowListener(this);		addComponentListener(this);		this.setResizable(false);
		this.applet = applet;
		applet.setStub(this);
		name = applet.getClass().getName();
		setTitle(name);
		//setLayout( new BorderLayout() );
		//add( "Center", applet );		setLayout(null);
		add(applet);
		applet.setBounds(0,40, 780, 500 );
		applet.init();
		applet.start();		setSize(width+20, height+40);
	}
    
	public void windowClosing(WindowEvent e)
	{	dispose();
		System.exit(0);
	}
	public void windowOpened(WindowEvent e){}
	public void windowIconified(WindowEvent e){doLayout();}
	public void windowDeiconified(WindowEvent e){doLayout();}
	public void windowClosed(WindowEvent e){}
	public void windowActivated(WindowEvent e){}
	public void windowDeactivated(WindowEvent e){}
    
    public void componentResized(ComponentEvent e)
	{	
	}
	public void componentMoved(ComponentEvent e){}
	public void componentShown(ComponentEvent e){}
	public void componentHidden(ComponentEvent e){}


    // Methods from AppletStub.

    public boolean isActive(){return true;}
    public URL getDocumentBase(){return null;}
	public URL getCodeBase(){return null;}
	public String getParameter(String name){return null;}
    public void appletResize( int width, int height ){}
    public AppletContext getAppletContext(){return this;}
    	public AudioClip getAudioClip( URL url ){return null;}
    public Image getImage( URL url )	{	Toolkit tk = Toolkit.getDefaultToolkit();
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