
package fi.draaibank;

import java.applet.*;
import java.awt.*;
import java.awt.image.*;
import java.net.*;
import java.io.*;
import java.util.*;import java.awt.event.*;
public class MainFrame extends Frame   implements WindowListener, ComponentListener, AppletStub, AppletContext
{	private double beginBreedte, beginHoogte;
    private String name;
    private Applet applet;


    public MainFrame( Applet applet, int width, int height )
	{	beginBreedte = width;		beginHoogte = height;
		addWindowListener(this);		addComponentListener(this);
		this.applet = applet;
		applet.setStub(this);
		name = applet.getClass().getName();
		setTitle(name);
		setLayout( new BorderLayout() );
		add( "Center", applet );
		applet.setSize( width, height-27);
		applet.init();
		applet.start();		setSize(width, height);
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
	public Applet getApplet( String name ){return null;}
    public Enumeration getApplets(){return null;}
		   
    public void showDocument( URL url ){}
    public void showDocument( URL url, String target ){}
    public void showStatus( String status ){}

	@Override
	public InputStream getStream(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterator<String> getStreamKeys() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setStream(String arg0, InputStream arg1) throws IOException {
		// TODO Auto-generated method stub
		
	}
}