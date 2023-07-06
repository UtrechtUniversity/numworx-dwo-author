package fi.nabouwenaanzichten;

import java.applet.*;
import java.awt.*;
import java.awt.image.*;
import java.io.*;
import java.awt.event.*;
import fi.beans.mainframe.MainFrame;

public class NabouwenFrame extends MainFrame implements ActionListener
{	
	NabouwenAanzichten nabouwenAanzichten;
	FileDialog openDial, saveDial;
	
	public NabouwenFrame(Applet applet,int width, int height )
	{	super( applet, width, height );
		this.setBackground(Color.white);
				
		nabouwenAanzichten = (NabouwenAanzichten)applet;
		setTitle("NabouwenAanzichten");
		
		openDial = new FileDialog(this, "openen", FileDialog.LOAD);
		openDial.setDirectory(System.getProperty("user.dir","."));
		saveDial = new FileDialog(this, "opslaan", FileDialog.SAVE);
		saveDial.setDirectory(System.getProperty("user.dir","."));
		MenuItem mi;
		MenuBar mbalk = new MenuBar();
		setMenuBar(mbalk);
		Menu bestandMenu = new Menu("bestand");
		//mbalk.add(bestandMenu);
		
		mi = new MenuItem("nieuw");
		mi.addActionListener(this);
		bestandMenu.add(mi);
		
		mi = new MenuItem("openen");
		mi.addActionListener(this);
		bestandMenu.add(mi);
		
		mi = new MenuItem("opslaan");
		mi.addActionListener(this);
		bestandMenu.add(mi);
		
		mi = new MenuItem("print");
		mi.addActionListener(this);
		bestandMenu.add(mi);		//setSize(width,height);	}
	
	public void nieuw()
	{	KubusRooster kr = new KubusRooster(4,1);
		nabouwenAanzichten.zetKubusRooster(kr);
	}
	public void open()
	{	String directory,naam;
		openDial.show();
		directory = openDial.getDirectory();
		naam = openDial.getFile();
		if(naam!=null)this.leesFile(directory+naam);
	}
	public void save()
	{	String directory,naam;
		saveDial.show();
		directory = saveDial.getDirectory();
		naam = saveDial.getFile();
		if(naam!=null)this.schrijfFile(directory+naam);
	}
	public void print()
	{	try
		{	PrintJob pjob = getToolkit().getPrintJob(this, "Printing Test", null);
			
			if(pjob != null)
			{	Graphics pg = pjob.getGraphics();
				if(pg != null)
				{	
					//nabrouwen.v.print(pg);
					
					pg.dispose();
				}
			}
		}
		catch(SecurityException e)
		{	
		}
	}
	
	public void teken(Graphics g)
	{	g.setColor(Color.red);
		g.fillOval(0,0,100,100);
		g.setColor(Color.black);
		g.drawOval(0,0,100,100);
	}
		
	
	void schrijfFile(String naam)
	{	KubusRooster kr = nabouwenAanzichten.geefKubusRooster();
		int aantalKubussen = 0;
		for(int i=0 ; i<kr.maxAantal ; i++)
		{	for(int j=0 ; j<kr.maxAantal ; j++)
			{	for(int k=0 ; k<kr.maxAantal ; k++)
				{	if(kr.kubussen[i][j][k] != null)aantalKubussen++;
				}
			}
		}
		DataOutputStream uitvoer;
		try
		{	
			uitvoer = new DataOutputStream(new FileOutputStream(new File(naam)));
			uitvoer.writeShort((short)(aantalKubussen));
			uitvoer.writeByte((byte)(kr.maxAantal));
			for(int i=0 ; i<kr.maxAantal ; i++)
			{	for(int j=0 ; j<kr.maxAantal ; j++)
				{	for(int k=0 ; k<kr.maxAantal ; k++)
					{	if(kr.kubussen[i][j][k] != null)
						{	uitvoer.writeByte((byte)(i));
							uitvoer.writeByte((byte)(j));
							uitvoer.writeByte((byte)(k));
						}
					}
				}
			}
			uitvoer.close();
		}
		catch(IOException io){}
	}
	
	void leesFile(String naam)
	{	KubusRooster kr = null;
		int aantalKubussen = 0;
		int maxAantal = 0;
		DataInputStream invoer;
		try
		{	
			invoer = new DataInputStream(new FileInputStream(new File(naam)));
			aantalKubussen = invoer.readShort();
			maxAantal = invoer.readByte();
			kr = new KubusRooster(maxAantal,1);
			for(int i=0 ; i<aantalKubussen ; i++)
			{	int x = invoer.readByte();
				int y = invoer.readByte();
				int z = invoer.readByte();
				kr.voegKubusToe(x,y,z);
			}
		}
		catch(IOException io){}
		nabouwenAanzichten.zetKubusRooster(kr);
	}
	
	public void actionPerformed(ActionEvent e)
	{	
		MenuItem item = null;
		String keuze = null;
		
		item = (MenuItem)(e.getSource());
		keuze = item.getLabel();
		
		if(keuze.equals("nieuw"))this.nieuw();
		else if(keuze.equals("openen"))this.open();
		else if(keuze.equals("opslaan"))this.save();
		else if(keuze.equals("print"))this.print();
	}
}
