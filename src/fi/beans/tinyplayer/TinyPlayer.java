// Decompiled by DJ v2.9.9.61 Copyright 2000 Atanas Neshkov  Date: 26-12-2005 19:32:35
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   TinyPlayer.java

package fi.beans.tinyplayer;

import java.applet.Applet;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.URL;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.*;

import fi.beans.appletutil.*;

public class TinyPlayer extends Panel
    implements ActionListener, Runnable
{
	
    public static final String AUDIO_PARAMETER = "audioURL";
    private Player player;
    private Thread playerThread;
    private String fileName;
    private String autoplay;
    
    private Button playButton;
    private boolean playing;
    private AppletUtil au;
    
    private String resourceName;

	Applet applet;
	
    public TinyPlayer(Applet applet)
    {	setLayout(null);
    	this.applet = applet;
    	au = new AppletUtil(applet);
    	
    	playButton = new Button("play");
    	playButton.setBounds(0,0,getSize().width, getSize().height);
    	playButton.addActionListener(this);
    	//add(playButton);
        
        player = null;
        playerThread = null;
        fileName = null;
        autoplay = "YES";
    }
    
    public TinyPlayer(Applet applet, String resourceName)
    {	setLayout(null);
    	this.applet = applet;
    	this.resourceName = resourceName;
    	au = new AppletUtil(applet);
    	
    	playButton = new Button("play");
    	playButton.setBounds(0,0,getSize().width, getSize().height);
    	playButton.addActionListener(this);
    	add(playButton);
    	
        player = null;
        playerThread = null;
        fileName = null;
        autoplay = "YES";
    }
    
    public void setBounds(int x, int y, int b, int h)
    {	super.setBounds(x,y,b,h);
    	playButton.setBounds(0,0,getSize().width, getSize().height);
    
    }
    
    public String getParameter(String s)
    {	return applet.getParameter(s);
    }

    protected AudioDevice getAudioDevice()
        throws JavaLayerException
    {
        return FactoryRegistry.systemRegistry().createAudioDevice();
    }

    protected void stopPlayer()
        throws JavaLayerException
    {	playing = false;
        if(player != null)
        {
            player.close();
            player = null;
            playerThread = null;
        }
    }

    protected void play(InputStream in, AudioDevice dev)
        throws JavaLayerException
    {	stopPlayer();
        if(in != null && dev != null)
        {	player = new Player(in, dev);
            playerThread = createPlayerThread();
            playerThread.start();
        }
    }

    protected Thread createPlayerThread()
    {
        return new Thread(this, "Audio player thread");
    }
    
    public void actionPerformed(ActionEvent e)
    {	if(e.getSource()== playButton)
        {	if(!playing)
        	{	startPlayer(resourceName);
        	}
        	else
        	{	try
	            {	stopPlayer();
	            }
	            catch(JavaLayerException ex)
	            {	System.err.println(ex);
	            }
        	}
        }
    }

    public void startPlayer(String resourceName)
    {	try
        {	InputStream in = au.getStream("resources/"+resourceName);
            AudioDevice dev = getAudioDevice();
            play(in, dev);
        }
        catch(JavaLayerException ex)
        {	synchronized(System.err)
            {
                System.err.println("Unable to play ".concat(String.valueOf(String.valueOf(resourceName))));
                ex.printStackTrace(System.err);
            }
        }
    }

    public void stop()
    {	try
        {	stopPlayer();
        }
        catch(JavaLayerException ex)
        {	System.err.println(ex);
        }
    }

    public void run()
    {	if(player != null)
            try
            {	player.play();
            }
            catch(JavaLayerException ex)
            {	System.err.println("Problem playing audio: ".concat(String.valueOf(String.valueOf(ex))));
            }
    }
}