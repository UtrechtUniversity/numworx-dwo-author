package fi.mozarch;

import java.applet.*;
import java.awt.*;
import java.awt.image.*;
import java.io.*;
import java.awt.event.*;
import fi.beans.mainframe.*;

public class MozArchFrame extends MainFrame implements ActionListener
{	
	MozArch mozarch;
	FileDialog openDial, saveDial;
	Dialog dialog;
	
	public MozArchFrame(Applet applet, int width, int height )
	{	super( applet, width, height );
		this.setBackground(Color.white);		
		mozarch = (MozArch)applet;
		//mozarch.zetOpsturenMogelijk(true);
		//mozarch.zetDataBekijkenMogelijk(true);		setTitle("Mozaik");
		
		openDial = new FileDialog(this, "openen", FileDialog.LOAD);
		openDial.setDirectory(System.getProperty("user.dir","."));
		saveDial = new FileDialog(this, "opslaan", FileDialog.SAVE);
		saveDial.setDirectory(System.getProperty("user.dir","."));
		
		//dialog = new Dialog(this);
		//dialog.setVisible(true);
		
		MenuItem mi;
		MenuBar mbalk = new MenuBar();
		setMenuBar(mbalk);
		Menu bestandMenu = new Menu("bestand");
		mbalk.add(bestandMenu);
		
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
		bestandMenu.add(mi);		show();
		setSize(width,height);
			}
	
	public void nieuw()
	{	mozarch.wis();
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
				{	mozarch.print(pg);
					pg.dispose();
				}
			}
		}
		catch(SecurityException e)
		{	
		}
	}
	
	void schrijfFile(String naam)
	{	Vlakdeel[] vd = mozarch.geefVlakdelen();
		int aantalVd = mozarch.geefAantalVlakdelen();
		
		Base64OutputStream uitvoer;
		try
		{	uitvoer = new Base64OutputStream(new FileOutputStream(new File(naam)));
			
			uitvoer.writeShort((short)(aantalVd));
			for(int i=0 ; i<aantalVd ; i++)
			{	uitvoer.writeShort((short)(vd[i].aantalPunten));
				uitvoer.writeDouble(vd[i].draaipunt.x);
				uitvoer.writeDouble(vd[i].draaipunt.y);
				uitvoer.writeDouble(vd[i].orientatie);
				uitvoer.writeBoolean(vd[i].nieuw);
				uitvoer.writeShort((short)(vd[i].beginnummer));
				uitvoer.writeByte((byte)(vd[i].kleur.getRed()-128));
				uitvoer.writeByte((byte)(vd[i].kleur.getGreen()-128));
				uitvoer.writeByte((byte)(vd[i].kleur.getBlue()-128));
				uitvoer.writeShort((short)(mozarch.volgorde[i]));
				for(int j=0 ; j<vd[i].aantalPunten+1 ; j++)
				{	uitvoer.writeDouble(vd[i].hoekpunten[j].x);
					uitvoer.writeDouble(vd[i].hoekpunten[j].y);
				}
			}	
			uitvoer.close();
		}
		catch(IOException io){}
	}
	
	void leesFile(String naam)
	{	Vlakdeel[] vd = null;
		int[] volgorde = null;
		int aantalVd = 0;
		Base64InputStream invoer;
		try
		{	invoer = new Base64InputStream(new FileInputStream(new File(naam)));
			aantalVd = invoer.readShort();
			vd = new Vlakdeel[1000];
			volgorde = new int[1000];
			for(int i=0 ; i<aantalVd ; i++)
			{	int aantalPunten = invoer.readShort();
				double posx = invoer.readDouble();
				double posy = invoer.readDouble();
				double orientatie = invoer.readDouble();
				boolean nieuw = invoer.readBoolean();
				int beginnummer = invoer.readShort();
				int rood = invoer.readByte()+128;
				int groen = invoer.readByte()+128;
				int blauw = invoer.readByte()+128;
				volgorde[i] = invoer.readShort();
				Color c = new Color(rood,groen,blauw);
				vd[i] = new Vlakdeel(mozarch,aantalPunten,posx,posy,c);
				vd[i].orientatie = orientatie;
				vd[i].beginnummer = beginnummer;
				vd[i].nieuw = nieuw;
				for(int j=0 ; j<aantalPunten+1 ; j++)
				{	double x = invoer.readDouble();
					double y = invoer.readDouble();
					vd[i].hoekpunten[j] = new HoekpuntMoz(x,y);
				}
			}
		}
		catch(IOException io){}
		mozarch.zetVlakdelen(vd,volgorde,aantalVd);
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
