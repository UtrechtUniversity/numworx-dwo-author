package fi.mozarch;

import java.applet.*;
import java.awt.*;
import java.awt.image.*;
import java.net.*;
import java.io.*;
import java.util.*;import java.awt.event.*;

public class BestandenFrame extends Frame implements WindowListener, ActionListener, AppletStub, AppletContext
{	
	private InzendingenLijst list;
	private MozArchViewer v;
	private Label bestandenLabel, naamLabel, emailLabel, leeftijdLabel;
	private TextField naamVeld, emailVeld, leeftijdVeld;
	private Button opslaanKnop, annulerenKnop, openKnop,verwijderKnop;
	private boolean opslaan;
	private String actieveFile;
	private ActionListener actionListener;
	private Panel invulPanel;
	
	public BestandenFrame()
	{	setLayout(null);		setBackground(new Color(255,255,200));
		addWindowListener(this);		
		opslaan = false;		setSize(500, 350);				v = new MozArchViewer();
		v.setStub(this);
		add(v);
		v.setBounds(200,21,300,300);
		v.init();
		v.start();		add(v);
						list = new InzendingenLijst(10,false);
		list.setBounds(20,60,150,190);
		list.addActionListener(this);
		add(list);
		
		invulPanel = new Panel();		invulPanel.setBounds(20,60,200,170);
		invulPanel.setLayout(null);
		invulPanel.setBackground(getBackground());		add(invulPanel);				bestandenLabel = new Label("Inzendingen");		bestandenLabel.setFont(new Font("SansSerif",Font.PLAIN,16));
		bestandenLabel.setAlignment(Label.CENTER);		bestandenLabel.setBounds(20,35,150,20);		add(bestandenLabel);				naamLabel = new Label("Naam");		naamLabel.setFont(new Font("SansSerif",Font.PLAIN,12));
		naamLabel.setBounds(0,0,150,20);		invulPanel.add(naamLabel);
		
		emailLabel = new Label("Email");		emailLabel.setFont(new Font("SansSerif",Font.PLAIN,12));
		emailLabel.setBounds(0,50,150,20);		invulPanel.add(emailLabel);
		
		leeftijdLabel = new Label("Leeftijd");		leeftijdLabel.setFont(new Font("SansSerif",Font.PLAIN,12));
		leeftijdLabel.setBounds(0,100,150,20);		invulPanel.add(leeftijdLabel);		
		naamVeld = new TextField();
		naamVeld.setBackground(Color.white);
		naamVeld.setBounds(0,20,150,20);
		invulPanel.add(naamVeld);
				emailVeld = new TextField();
		emailVeld.setBackground(Color.white);
		emailVeld.setBounds(0,70,150,20);
		invulPanel.add(emailVeld);
				leeftijdVeld = new TextField();
		leeftijdVeld.setBackground(Color.white);
		leeftijdVeld.setBounds(0,120,150,20);
		invulPanel.add(leeftijdVeld);
				opslaanKnop = new Button("Opsturen");
		opslaanKnop.addActionListener(this);
		opslaanKnop.setBounds(20,265,70,20);
		add(opslaanKnop);
		
		openKnop = new Button("Openen");
		openKnop.addActionListener(this);
		openKnop.setBounds(20,265,70,20);
		//add(openKnop);				annulerenKnop = new Button("Annuleren");
		annulerenKnop.addActionListener(this);
		annulerenKnop.setBounds(100,265,70,20);
		add(annulerenKnop);				verwijderKnop = new Button("Verwijder");
		verwijderKnop.addActionListener(this);
		verwijderKnop.setBounds(100,290,70,20);
		add(verwijderKnop);
	}
	
	public void zetVlakdelen(String s)
	{	
	}
	
	public void slaOp(String s)
	{	actieveFile = s;
		setTitle("Opsturen");		remove(list);
		remove(openKnop);		remove(verwijderKnop);		remove(bestandenLabel);
		add(opslaanKnop);
		add(invulPanel);		opslaan = true;
		v.zetVlakdelen(s);
		//tekenOpnieuw();
		repaint();	}
	
	public void bekijk()
	{	setTitle("Bestanden");		opslaan = false;
		remove(opslaanKnop);		remove(invulPanel);		add(bestandenLabel);		add(list);
		add(openKnop);
		add(verwijderKnop);		//list.select(0);		//actieveFile = list.geefActieveFile();		//v.zetVlakdelen(actieveFile);
		//v.tekenOpnieuw();	}		public String geefActieveFile()	{	return actieveFile;	}
	
	public void addActionListener(ActionListener listener)
	{	actionListener = AWTEventMulticaster.add(actionListener, listener);
	}
	
	public void removeActionListener(ActionListener listener)
	{	actionListener = AWTEventMulticaster.remove(actionListener, listener);
	}
			public void actionPerformed(ActionEvent e)
	{	if(e.getSource()==list && !opslaan)
		{	String file = list.geefActieveFile();
			actieveFile = file;
			if(file!=null)
			{	v.zetVlakdelen(file);
				v.tekenOpnieuw();
			}
		}
		else if(e.getSource()==opslaanKnop)
		{	String naam = naamVeld.getText().trim();
			if(naam.equals(""))return;
			list.voegInzendingToe(naam,actieveFile);
			dispose();
		}
		else if(e.getSource()==openKnop)
		{	if(actionListener!=null)
			{	actionListener.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, ""+this));
			}
		}
		else if(e.getSource()==annulerenKnop)
		{	dispose();
		}
		else if(e.getSource()==verwijderKnop)
		{	list.verwijderBouwsel();
		}
	}
    
	public void windowClosing(WindowEvent e)
	{	dispose();
		//System.exit(0);
	}
	public void windowOpened(WindowEvent e){}
	public void windowIconified(WindowEvent e){doLayout();}
	public void windowDeiconified(WindowEvent e){doLayout();}
	public void windowClosed(WindowEvent e){}
	public void windowActivated(WindowEvent e){}
	public void windowDeactivated(WindowEvent e){}
	
	public boolean isActive(){return true;}
    public URL getDocumentBase(){return null;}
	public URL getCodeBase(){return null;}
	public String getParameter(String name){return null;}
	public void appletResize( int width, int height ){}
    public AppletContext getAppletContext(){return this;}
    public AudioClip getAudioClip( URL url ){return null;}
	public Image getImage( URL url ){return null;}	public Applet getApplet( String name ){return null;}
    public Enumeration getApplets(){return null;}
    public void setStream(String s, InputStream is){}
    public InputStream getStream(String s){return null;}
    public Iterator getStreamKeys(){return null;}
    public void showDocument( URL url ){}
    public void showDocument( URL url, String target ){}
    public void showStatus( String status ){}
}
