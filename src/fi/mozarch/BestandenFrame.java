package fi.mozarch;

import java.applet.*;
import java.awt.*;
import java.awt.image.*;
import java.net.*;
import java.io.*;
import java.util.*;import java.awt.event.*;

public class BestandenFrame extends Frame implements WindowListener, ActionListener, AppletStub, AppletContext, ComponentListener
{	
	private InzendingenLijst list;
	private MozArchViewer v;
	private Label bestandenLabel, naamLabel, geslachtLabel, emailLabel, leeftijdLabel, foutmeldingLabel;
	private TextField naamVeld, geslachtVeld, emailVeld, leeftijdVeld;
	private int opdrachtNr = 0;
	private Button opslaanKnop, annulerenKnop, openKnop,verwijderKnop;
	private boolean opslaan;
	private String actieveFile;
	private ActionListener actionListener;
	private Panel invulPanel;
	
	
	public BestandenFrame()
	{	setLayout(null);		setBackground(new Color(255,255,200));
		addWindowListener(this);
		addComponentListener(this);		
		opslaan = false;		setSize(500, 350);				v = new MozArchViewer();
		v.setStub(this);
		add(v);
		v.setBounds(getSize().width-300,21,300,300);
		v.init();
		v.start();		add(v);
						list = new InzendingenLijst(10,false);
		list.setBounds(20,60,getSize().width - 350,getSize().height - 170);
		list.addActionListener(this);
		//add(list);
		
		invulPanel = new Panel();		invulPanel.setBounds(20,60,200,170);
		invulPanel.setLayout(null);
		invulPanel.setBackground(getBackground());		add(invulPanel);				bestandenLabel = new Label(MozArch.rb.getString("inzendingenLabel"));		bestandenLabel.setFont(new Font("SansSerif",Font.PLAIN,16));
		bestandenLabel.setAlignment(Label.CENTER);		bestandenLabel.setBounds(20,35,150,20);		add(bestandenLabel);
		
		naamLabel = new Label(MozArch.rb.getString("naamLabel"));		naamLabel.setFont(new Font("SansSerif",Font.PLAIN,12));
		naamLabel.setBounds(0,0,120,20);		invulPanel.add(naamLabel);				geslachtLabel = new Label(MozArch.rb.getString("geslachtLabel"));		geslachtLabel.setFont(new Font("SansSerif",Font.PLAIN,12));
		geslachtLabel.setBounds(130,0,30,20);		invulPanel.add(geslachtLabel);
		
		emailLabel = new Label(MozArch.rb.getString("emailLabel"));		emailLabel.setFont(new Font("SansSerif",Font.PLAIN,12));
		emailLabel.setBounds(0,50,150,20);		invulPanel.add(emailLabel);
		
		leeftijdLabel = new Label(MozArch.rb.getString("leeftijdLabel"));		leeftijdLabel.setFont(new Font("SansSerif",Font.PLAIN,12));
		leeftijdLabel.setBounds(0,100,150,20);		invulPanel.add(leeftijdLabel);		
		naamVeld = new TextField();
		naamVeld.setBackground(Color.white);
		naamVeld.setBounds(0,20,120,20);
		invulPanel.add(naamVeld);				geslachtVeld = new TextField();
		geslachtVeld.setBackground(Color.white);
		geslachtVeld.setBounds(130,20,20,20);
		invulPanel.add(geslachtVeld);
				emailVeld = new TextField();
		emailVeld.setBackground(Color.white);
		emailVeld.setBounds(0,70,150,20);
		invulPanel.add(emailVeld);
				leeftijdVeld = new TextField();
		leeftijdVeld.setBackground(Color.white);
		leeftijdVeld.setBounds(0,120,150,20);
		invulPanel.add(leeftijdVeld);
		
		foutmeldingLabel = new Label("");		foutmeldingLabel.setFont(new Font("SansSerif",Font.BOLD,12));		foutmeldingLabel.setForeground(Color.red);
		foutmeldingLabel.setBounds(20,getSize().height-105,150,20);		add(foutmeldingLabel,0);
				opslaanKnop = new Button(MozArch.rb.getString("opslaanKnopLabel"));
		opslaanKnop.addActionListener(this);
		opslaanKnop.setBounds(20,getSize().height-80,70,20);
		add(opslaanKnop);
		
		openKnop = new Button(MozArch.rb.getString("openKnopLabel"));
		openKnop.addActionListener(this);
		openKnop.setBounds(20,getSize().height-80,70,20);
		add(openKnop);				annulerenKnop = new Button(MozArch.rb.getString("annulerenKnopLabel"));
		annulerenKnop.addActionListener(this);
		annulerenKnop.setBounds(100,getSize().height-80,70,20);
		add(annulerenKnop);				verwijderKnop = new Button(MozArch.rb.getString("verwijderKnopLabel"));
		verwijderKnop.addActionListener(this);
		verwijderKnop.setBounds(100,getSize().height-55,70,20);
		add(verwijderKnop);
	}
	
	public void zetVlakdelen(String s)
	{	
	}		public void zetFoutmelding(String s)	{	foutmeldingLabel.setText(s);
		foutmeldingLabel.repaint();	}		public void zetOpdrachtNr(int opdrNr)	{	opdrachtNr = opdrNr;	}
	
	public void slaOp(String s)
	{	this.setResizable(false);		actieveFile = s;
		setTitle("Opsturen");		remove(list);
		remove(openKnop);		remove(verwijderKnop);		remove(bestandenLabel);
		add(opslaanKnop);
		add(invulPanel);		opslaan = true;
		v.zetVlakdelen(s);
		//tekenOpnieuw();
		repaint();	}
	
	public void bekijk()
	{	this.setResizable(true);
		setTitle("Bestanden");		opslaan = false;
		remove(opslaanKnop);		remove(invulPanel);		add(bestandenLabel);		add(list);
		add(openKnop);
		add(verwijderKnop);
		setCursor(new Cursor(Cursor.WAIT_CURSOR));
		Thread thread = new Thread()
		{	public void run()
			{	try
				{	list.haalInzendingen();
				}
				catch(IOException ioe)
				{	foutmeldingLabel.setText(MozArch.rb.getString("verbindingFoutmeldingLabel"));
				}				setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			}
		};
		thread.start();		
		//list.haalInzendingen();		//list.select(0);		//actieveFile = list.geefActieveFile();		//v.zetVlakdelen(actieveFile);
		//v.tekenOpnieuw();	}		public String geefActieveFile()	{	return actieveFile;	}
	
	public void addActionListener(ActionListener listener)
	{	actionListener = AWTEventMulticaster.add(actionListener, listener);
	}
	
	public void removeActionListener(ActionListener listener)
	{	actionListener = AWTEventMulticaster.remove(actionListener, listener);
	}
			public void actionPerformed(ActionEvent e)
	{	foutmeldingLabel.setText("");
		if(e.getSource()==list && !opslaan)
		{	String file = list.geefActieveFile();
			actieveFile = file;
			if(file!=null)
			{	v.zetVlakdelen(file);
				v.tekenOpnieuw();
			}
		}
		else if(e.getSource()==opslaanKnop)
		{	String naam = naamVeld.getText();
			String geslacht = geslachtVeld.getText().trim();
			String email = emailVeld.getText().trim();
			String leeftijd = leeftijdVeld.getText().trim();
			if(naam.equals("") || geslacht.equals("") || email.equals("") || leeftijd.equals("") )
			{	foutmeldingLabel.setText(MozArch.rb.getString("volledigheidsFoutmeldingLabel"));				return;
			}
			setCursor(new Cursor(Cursor.WAIT_CURSOR));
			
			final Frame outer = this;
			Thread thread = new Thread()
			{	public void run()
				{	String naam = naamVeld.getText();
					String geslacht = geslachtVeld.getText().trim();
					String email = emailVeld.getText().trim();
					String leeftijd = leeftijdVeld.getText().trim();
					String opdracht = "";
					if(opdrachtNr!=0)opdracht = Integer.toString(opdrachtNr);					try
					{	list.voegInzendingToe("" + opdracht + "    " + geslacht + "    " + naam + "    " + leeftijd + "    " + email ,actieveFile);						setCursor(new Cursor(Cursor.DEFAULT_CURSOR));						outer.dispose();
					}
					catch(IOException ioe)
					{	foutmeldingLabel.setText(MozArch.rb.getString("verbindingFoutmeldingLabel"));
					}					setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
									}
			};
			thread.start();
			//if(foutmeldingLabel.getText().equals(""))dispose();
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
	}		public void componentResized(ComponentEvent e)
	{	list.setBounds(20,60,getSize().width - 350,getSize().height - 170);
		openKnop.setBounds(20,getSize().height-80,70,20);
		annulerenKnop.setBounds(100,getSize().height-80,70,20);
		verwijderKnop.setBounds(100,getSize().height-55,70,20);
		foutmeldingLabel.setBounds(20,getSize().height-105,150,20);
		v.setBounds(getSize().width-300,21,300,300);
	}
	public void componentMoved(ComponentEvent e){}
	public void componentShown(ComponentEvent e){}
	public void componentHidden(ComponentEvent e){}	
    
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
