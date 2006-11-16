package fi.tekenveelvlakopdr;

import java.applet.*;
import java.awt.*;
import java.awt.image.*;
import java.io.*;
import java.awt.event.*;
import fi.beans.mainframe.*;

public class TekenVeelvlakFrame extends MainFrame implements ActionListener
{
	TekenVeelvlakOpdr tekenveelvlakopdr;
	FileDialog openDial, saveDial;
	
	public TekenVeelvlakFrame(Applet applet, int width, int height )	{	super( applet, width, height );
		this.setBackground(Color.white);		tekenveelvlakopdr = (TekenVeelvlakOpdr)applet;
		setTitle("TekenVeelvlak");
		
		openDial = new FileDialog(this, "openen", FileDialog.LOAD);
		openDial.setDirectory(System.getProperty("user.dir","."));
		saveDial = new FileDialog(this, "opslaan", FileDialog.SAVE);
		saveDial.setDirectory(System.getProperty("user.dir","."));
		
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
		bestandMenu.add(mi);
		
		show();
		setSize(width,height);
	}
	
	public void nieuw()
	{	/*tekenveelvlakopdr.aantalPuntenRood=0;
		tekenveelvlakopdr.tv.aantalLijnen = 0;
		tekenveelvlakopdr.aantalHpNieuw = 0;
		tekenveelvlakopdr.wisTrefpunten();
		tekenveelvlakopdr.tv.wisVlakken();
		tekenveelvlakopdr.tekenOpnieuw();*/
		
		//tekenveelvlakopdr.nieuw();
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
				{	//tekenveelvlakopdr.print(pg);
					pg.dispose();
				}
			}
		}
		catch(SecurityException e)
		{	
		}
	}
	
	void leesFile(String naam)
	{	DataInputStream invoer;
		try
		{	
			invoer = new DataInputStream(new FileInputStream(new File(naam)));
			int aantalRijP = invoer.readShort();
			double[] hp = new double[aantalRijP];
			
			for(int i=0 ; i<aantalRijP ; i++)
			{	hp[i] = invoer.readDouble();
			}
			int aantalRijV = invoer.readShort();
			int[] vl = new int[aantalRijV];
			for(int i=0 ; i<aantalRijV ; i++)
			{	vl[i] = invoer.readShort();
			}
			invoer.close();
			//tekenveelvlakopdr.tv = new Veelvlak(hp, vl);
		}
		catch(IOException io){}
		//tekenveelvlakopdr.begin = true;
		//tekenveelvlakopdr.tekenOpnieuw();
	}
	void schrijfFile(String naam)
	{	DataOutputStream uitvoer;
		try
		{	
			uitvoer = new DataOutputStream(new FileOutputStream(new File(naam)));
			/*uitvoer.writeShort((short)tekenveelvlakopdr.tv.hpRijAantal);
			for(int i=0 ; i<tekenveelvlakopdr.tv.hpRijAantal ; i++)
			{	uitvoer.writeDouble(tekenveelvlakopdr.tv.hpRij[i]);
			}
			uitvoer.writeShort((short)tekenveelvlakopdr.tv.vlRijAantal);
			for(int i=0 ; i<tekenveelvlakopdr.tv.vlRijAantal ; i++)
			{	uitvoer.writeShort((short)tekenveelvlakopdr.tv.vlRij[i]);
			}*/
			uitvoer.close();
		}
		catch(IOException io){}
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
