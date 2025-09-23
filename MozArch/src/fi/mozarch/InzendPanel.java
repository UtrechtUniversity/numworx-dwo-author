package fi.mozarch;

import java.applet.AudioClip;
import java.awt.*;
import java.awt.event.*;
import java.net.*;
import java.io.*;import java.util.*;

import fi.beans.mainframe.AppletContext;
import fi.beans.mainframe.AppletStub;


public class InzendPanel extends Panel implements ActionListener, AppletStub, AppletContext
{
	private Button okKnop;
	private TextField voornaamVeld;
	private TextField achternaamVeld;
	private TextField bouwselnaamVeld;
	private Label voornaamLabel;
	private Label achternaamLabel;
	private Label bouwselnaamLabel;
	private Label emailLabel;
	private TextField emailVeld;
	private MozArchViewer viewer;
	
	private ActionListener actionListener;

	
	public InzendPanel()
	{	setLayout(null);
		setBackground(new Color(255,255,200));
		
		viewer = new MozArchViewer();
		viewer.setStub(this);
		add(viewer);
		viewer.setBounds(250,1,520,470);
		
		viewer.init();
		viewer.start();
		
		
		bouwselnaamLabel = new Label("Naam van het blokkenbouwsel");
		bouwselnaamLabel.setBounds(20,20,150,20);
		add(bouwselnaamLabel);
		
		bouwselnaamVeld = new TextField();
		bouwselnaamVeld.setBackground(Color.white);
		bouwselnaamVeld.setBounds(20,40,150,20);
		add(bouwselnaamVeld);
		
		voornaamLabel = new Label("Voornaam");
		voornaamLabel.setBounds(20,80,150,20);
		add(voornaamLabel);
		
		voornaamVeld = new TextField();
		voornaamVeld.setBackground(Color.white);
		voornaamVeld.setBounds(20,100,150,20);
		add(voornaamVeld);
		
		achternaamLabel = new Label("Achternaam");
		achternaamLabel.setBounds(20,140,150,20);
		add(achternaamLabel);
		
		achternaamVeld = new TextField();
		achternaamVeld.setBackground(Color.white);
		achternaamVeld.setBounds(20,160,150,20);
		add(achternaamVeld);
		
		emailLabel = new Label("Email adres");
		emailLabel.setBounds(20,200,150,20);
		add(emailLabel);
		
		emailVeld = new TextField();
		emailVeld.setBackground(Color.white);
		emailVeld.setBounds(20,220,150,20);
		add(emailVeld);
		
		okKnop = new Button("OK");
		okKnop.setBounds(80,250,40,20);
		okKnop.addActionListener(this);
		add(okKnop);
	}
	
	public void paint(Graphics g)
	{	g.setColor(Color.black);
		g.drawRect(0,0,getSize().width-1,getSize().height-1);
				
		super.paint(g);
	}
	
	public void zetVlakdelen(String s)
	{	viewer.zetVlakdelen(s);
		viewer.tekenOpnieuw();
	}
	
	public String geefBouwselNaam()
	{	return bouwselnaamVeld.getText();
	}
	
	public void addActionListener(ActionListener listener)
	{	actionListener = AWTEventMulticaster.add(actionListener, listener);
	}
	
	public void removeActionListener(ActionListener listener)
	{	actionListener = AWTEventMulticaster.remove(actionListener, listener);
	}
	
	public void actionPerformed(ActionEvent e)
	{	if(e.getSource()==okKnop)
		{	if(actionListener!=null)
			{	actionListener.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, ""+this));
			}
			bouwselnaamVeld.setText("");
			setVisible(false);
		}
		transferFocus();
	}
	
	public boolean isActive(){return true;}
    public URL getDocumentBase(){return null;}
	public URL getCodeBase(){return null;}
	public String getParameter(String name){return null;}
	public void appletResize( int width, int height ){}
    public AppletContext getAppletContext(){return this;}
    public AudioClip getAudioClip( URL url ){return null;}
	public Image getImage( URL url ){return null;}    public void setStream(String s, InputStream is){}
    public InputStream getStream(String s){return null;}
    public Iterator getStreamKeys(){return null;}
    public void showDocument( URL url ){}
    public void showDocument( URL url, String target ){}
    public void showStatus( String status ){}
	
}
