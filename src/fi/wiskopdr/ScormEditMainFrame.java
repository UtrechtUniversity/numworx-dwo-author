package fi.wiskopdr;

import java.applet.*;
//import java.awt.*;
import java.io.*;
import java.net.URI;
import java.text.MessageFormat;
import java.util.*;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.event.*;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;

import fi.beans.base64code.StringCodeObject;
import fi.beans.dwomaccess.JSONEncoder;
import fi.beans.mainframe.*;
import fi.beans.scorm.*;
import fi.wiskopdr.opdrnav.OpdrNavStruct;

import java.util.zip.*;

import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.StreamPrintService;
import javax.print.StreamPrintServiceFactory;
import javax.print.attribute.AttributeSet;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.standard.Destination;
import javax.print.attribute.standard.PrinterName;
import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.w3c.dom.DocumentFragment;

public class ScormEditMainFrame extends MainFrame implements ActionListener
{
	@Override
  public String getParameter(String name) {
    if ("dwo_env".equals(name)) return "test";
    return super.getParameter(name);
  }


  private static final String SAVE_AS_FACET = "opslaan als FACET Item (CI)";
	ScormEditComponentIF scormEditComponent;
	FileDialog openDial, saveDial;
	String titel;
	private int opdr;
	private int act;
	
	public ScormEditMainFrame(Applet applet,int width, int height )
	{	super( applet, width, height );
		this.setBackground(Color.white);
		
		setTitle("wiskopdr");
		
		openDial = new FileDialog(this, "openen", FileDialog.LOAD);
		openDial.setDirectory(System.getProperty("user.dir","."));
		saveDial = new FileDialog(this, "opslaan", FileDialog.SAVE);
		saveDial.setDirectory(System.getProperty("user.dir","."));
		saveDial.setName("*.htm");
		
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
		mi = new MenuItem("opslaan als JSON");
		mi.addActionListener(this);
		bestandMenu.add(mi);
		
		mi = new MenuItem(SAVE_AS_FACET);
		mi.addActionListener(this);
		mi.setActionCommand(SAVE_AS_FACET);
		bestandMenu.add(mi);

		mi = new MenuItem("print");
		mi.addActionListener(this);
		bestandMenu.add(mi);
	}
	
	public void setScormEditComponent(ScormEditComponentIF scormEditComponent)
	{	this.scormEditComponent = scormEditComponent;
	}
	
	public void nieuw()
	{	
	}
	
	public void open()
	{	String directory,naam;
		openDial.show();
		directory = openDial.getDirectory();
		naam = openDial.getFile();
		if(naam!=null)//this.leesFile(directory+naam);
		{	readZip(directory + naam);
		}
	}
	
	public void save()
	{	String directory,naam;
		saveDial.show();
		directory = saveDial.getDirectory();
		naam = saveDial.getFile();
		if(naam!=null)//this.schrijfFile(directory+naam);
		{	if(naam.indexOf(".")>-1)naam = naam.substring(0,naam.indexOf("."));
			titel = naam;
			createZip(directory+naam);
			schrijfTestFile(directory+"test.htm");
		}
	}
	
	public void saveJSON() throws IOException {
		saveDial.show();
		String naam = saveDial.getFile();
		if(naam != null ) {
			File directory = new File(saveDial.getDirectory());
			File json = new File(directory, naam);
			FileWriter out = new FileWriter(json);
			Hashtable launchData = scormEditComponent.getLaunchData();
			JSONEncoder.encode(launchData, out);
			out.close();
		}
	}

	public void saveFacet() throws IOException {
		saveDial.show();
		String naam = saveDial.getFile();
		if(naam != null ) {
			
			File directory = new File(saveDial.getDirectory());
			File file = new File(directory, naam);
			ZipOutputStream ci = new ZipOutputStream(new FileOutputStream(file));
			// directories
			createDirectories(new String[] { "ref", "ref/html", "ref/json", "ref/script", "ref/style"}, ci);
			
			ZipEntry entry = new ZipEntry("ref/html/"+naam+".json");
			ci.putNextEntry(entry);
			Hashtable launchData = scormEditComponent.getLaunchData();
			Writer out  = new OutputStreamWriter(ci);
			JSONEncoder.encode(launchData, out);
			out.flush();
			ci.closeEntry();
			String[] resources = { "ref/html/player.txt", "ref/html/player.html", "ref/script/player.js", "ref/style/player.css"};
			copyResources(resources, ci, new byte[10240]);
			String manifest = "{ (0) }";
			entry = new ZipEntry("ref/json/" +naam +".manifest.json");
			ci.putNextEntry(entry);
			out.write(MessageFormat.format(manifest, naam));
			out.flush();
			ci.closeEntry();
			ci.close();
		}
	}
	
	
	private void createDirectories(String[] strings, ZipOutputStream ci) throws IOException {
		for (String string : strings) {
			if( !string.endsWith("/")) string += "/";
			ci.putNextEntry(new ZipEntry(string));
		}
	}

	public void print()
	{	
		if (WiskOpdr.applet.ons == null) return;
		opdr = WiskOpdr.applet.ons.geefOpdrachtNr();
		act = WiskOpdr.applet.ons.geefActiviteitNr();
		try
		{
			PrinterJob job;
			job = PrinterJob.getPrinterJob();
			PageFormat format = job.defaultPage();
			boolean doPrint = true;
			job.setPrintable(WiskOpdr.applet, format );
			doPrint = job.printDialog();
			if (doPrint) {
			    try {
			        job.print();
			    } catch (PrinterException e) {
			        // The job did not successfully
			        // complete
			    	e.printStackTrace();
			    }
			    WiskOpdr.applet.ons.kiesOpdracht(opdr, act);
			}
		}
		catch(SecurityException e)
		{	
		}
	}

	void schrijfFile(String naam)
	{	try
		{	PrintWriter out = new PrintWriter(new FileWriter(naam));
			printScormHTML(out);
			out.close();
		}
		catch(IOException ie)
		{
		}
	}
	
	void schrijfTestFile(String naam)
	{	try
		{	PrintWriter out = new PrintWriter(new FileWriter(naam));
			printTestHTML(out);
			out.close();
		}
		catch(IOException ie)
		{
		}
	}
	
	public void printScormHTML(PrintWriter out)
	{	Hashtable launchData = scormEditComponent.getLaunchData();
		//String editModeState = (String)launchData.get("editModeState");
		out.println("<HTML>");
		out.println("<HEAD>");
		
		out.println("	<TITLE>WiskOpdr</TITLE>");
		out.println("	<SCRIPT type=\"text/javascript\" src=\"script/FiScoScript.js\"></SCRIPT>");
		out.println("	<SCRIPT>");
		out.println("		var exitPageStatus;");
		out.println("		");
		out.println("		function quit()");
		out.println("		{	if (exitPageStatus != true)");
		out.println("			{	document.applets[0].stopSco();");
		out.println("				exit();");
		out.println("				exitPageStatus=true;");
		out.println("			}");
		out.println("		}");
		out.println("	</SCRIPT>");
		
		out.println("</HEAD>");
		out.println("<BODY bgcolor=\"#DDEEFF\" onload=\"javascript:init();\" onbeforeunload=\"javascript:quit();\" onunload=\"javascript:quit();\">");
		out.println("<center>");
		out.println("<h1>"+titel+"</h1>");
		out.println("<APPLET");
		out.println("	id		= \"wiskopdr\"");
		out.println("	name	= \"wiskopdr\"");
		out.println("	codebase = \"http://ws.fisme.science.uu.nl/javaclasses/\"");
		out.println("	code	= \"fi.wiskopdr.WiskOpdr.class\"");
		out.println("	archive	= \"jars/wiskopdr.jar\"");
		out.println("	width	= \"770\"");
		out.println("	height	= \"470\"");
		out.println("	mayscript=\"mayscript\">");
		out.println("");
		out.println("	<PARAM NAME=\"API\" VALUE=\"fi.beans.scorm.JSScormAPI\"/>");
		out.println("	<PARAM NAME=\"language\" VALUE=\"" + launchData.get("language") + "\"/>");
		out.println("	<PARAM NAME=\"bgcolor\" VALUE=\"" + launchData.get("bgcolor") + "\"/>");
		out.println("");
		/*int aantalActiviteiten = Integer.parseInt((String)launchData.get("aantalActiviteiten"));
		out.println("	<PARAM NAME=\"aantalActiviteiten\" VALUE=\"" + aantalActiviteiten + "\"/>");
		out.println("");
		for (int i = 0; i<aantalActiviteiten; i++) 
		{	out.println("	<PARAM NAME=\"activiteit_"+(i+1)+"\" VALUE=\"" + (String)launchData.get("activiteit_"+(i+1)) + "\"/>");
			int aantalOpdrachten = Integer.parseInt((String)launchData.get("aantalOpdrachten_"+(i+1)));
			out.println("	<PARAM NAME=\"aantalOpdrachten_"+(i+1)+"\" VALUE=\"" + aantalOpdrachten + "\"/>");
			for (int j = 0; j<aantalOpdrachten; j++) 
			{	out.println("	<PARAM NAME=\"opdracht_"+(i+1)+"_"+(j+1)+"\" VALUE=\"" + (String)launchData.get("opdracht_"+(i+1)+"_"+(j+1)) + "\"/>");
		    }
		    out.println("");
	    }
		*/
		String launchDataString = StringCodeObject.encodeObjectToString(launchData);
		out.println("	<PARAM NAME=\"launchData\" VALUE=\"" + launchDataString + "\"/>");
		
		out.println("");
		out.println("</APPLET>");
		out.println("</BODY>");
		out.println("</HTML>");
		
		
	}

	public void printTestHTML(PrintWriter out)
	{	Hashtable launchData = scormEditComponent.getLaunchData();
		//String editModeState = (String)launchData.get("editModeState");
		out.println("<HTML>");
		out.println("<HEAD>");
		out.println("</HEAD>");
		out.println("<BODY bgcolor=\"#DDEEFF\">");
		out.println("<center>");
		out.println("<h1>"+titel+"</h1>");
		out.println("<APPLET");
		out.println("	id		= \"wiskopdr\"");
		out.println("	name	= \"wiskopdr\"");
		out.println("	code	= \"fi.wiskopdr.WiskOpdr.class\"");
		out.println("	archive	= \"wiskopdr.jar\"");
		out.println("	width	= \"770\"");
		out.println("	height	= \"470\"");
		out.println("	mayscript=\"mayscript\">");
		out.println("");
		out.println("	<PARAM NAME=\"API\" VALUE=\"fi.beans.scorm.JSScormAPI\"/>");
		out.println("	<PARAM NAME=\"language\" VALUE=\"" + launchData.get("language") + "\"/>");
		out.println("	<PARAM NAME=\"bgcolor\" VALUE=\"" + launchData.get("bgcolor") + "\"/>");
		out.println("");
		/*int aantalActiviteiten = Integer.parseInt((String)launchData.get("aantalActiviteiten"));
		out.println("	<PARAM NAME=\"aantalActiviteiten\" VALUE=\"" + aantalActiviteiten + "\"/>");
		out.println("");
		for (int i = 0; i<aantalActiviteiten; i++) 
		{	out.println("	<PARAM NAME=\"activiteit_"+(i+1)+"\" VALUE=\"" + (String)launchData.get("activiteit_"+(i+1)) + "\"/>");
			int aantalOpdrachten = Integer.parseInt((String)launchData.get("aantalOpdrachten_"+(i+1)));
			out.println("	<PARAM NAME=\"aantalOpdrachten_"+(i+1)+"\" VALUE=\"" + aantalOpdrachten + "\"/>");
			for (int j = 0; j<aantalOpdrachten; j++) 
			{	out.println("	<PARAM NAME=\"opdracht_"+(i+1)+"_"+(j+1)+"\" VALUE=\"" + (String)launchData.get("opdracht_"+(i+1)+"_"+(j+1)) + "\"/>");
		    }
		    out.println("");
	    }
		*/
		String launchDataString = StringCodeObject.encodeObjectToString(launchData);
		out.println("	<PARAM NAME=\"launchData\" VALUE=\"" + launchDataString + "\"/>");
		
		out.println("");
		out.println("</APPLET>");
		out.println("</BODY>");
		out.println("</HTML>");
		
		
	}
	
	void leesFile(String name)
	{	Hashtable params;
		
		/*String s = null;
		try
		{	BufferedReader in = new BufferedReader(new FileReader(naam));
			s = in.readLine();
			System.out.println(s);
		}
		catch(IOException ie)
		{	
		}*/
		
		BufferedReader in = null;
		String string = "";
		try
		{  	in = new BufferedReader(new FileReader(name));
			String s = "";
			try
			{	while ((s = in.readLine()) != null)
				{	string = string + s;
				}
			}
			catch (Exception exception)
			{  
			}
			in.close();
		}
		catch (Exception exception)
		{  
		}
		params = new Hashtable();
		int start = string.indexOf("<APPLET");
		int end = string.indexOf("</APPLET>");
		string = string.substring(start+7,end);
		start = string.indexOf("<PARAM");
		end = string.indexOf("/>");
		while(start>0)
		{	String param = string.substring(start+6,end);
			
			int naamBegin = param.indexOf("NAME=\"");
			int naamEind = param.indexOf("\"",naamBegin+6);
			String naam = param.substring(naamBegin+6,naamEind);
			
			int waardeBegin = param.indexOf("VALUE=\"");
			int waardeEind = param.indexOf("\"",waardeBegin+7);
			String waarde = param.substring(waardeBegin+7,waardeEind);
			
			params.put(naam,waarde);
			string = string.substring(end+2);
			start = string.indexOf("<PARAM");
			end = string.indexOf("/>");
		}
		scormEditComponent.setState(params);
		scormEditComponent.getComponent().repaint();
	}
	
	public void createZip(String zipName) 
	{
		try 
    {
		String filename;
		File dataFile = File.createTempFile("data", "txt");
		filename = dataFile.getAbsolutePath();
		schrijfFile(filename);
		
	    byte[] buf = new byte[1024];
	    
	        String outFilename = zipName + ".zip";
	        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(outFilename));
	        
	        FileInputStream fin = new FileInputStream(filename);
	    	out.putNextEntry(new ZipEntry("sco/WiskOpdr.htm"));
	    	int len;
	        while ((len = fin.read(buf)) > 0) 
	        {	out.write(buf, 0, len);
	        }
	        fin.close();
	        out.closeEntry();
	        String scriptname = "sco/script/FiScoScript.js";
	        String[] scormFileNames = {"adlcp_rootv1p2.xsd","ims_xml.xsd","imscp_rootv1p1p2.xsd","imsmanifest.xml","imsmd_rootv1p2p1.xsd",scriptname};
	        copyResources(scormFileNames, out, buf);
	        out.close();
			dataFile.delete();
	    } 
	    catch (IOException e) 
	    {   }
	}

	private void copyResources(String[] scormFileNames, ZipOutputStream out,
			byte[] buf) throws IOException {
		int len;
		for (int i=0; i<scormFileNames.length; i++) 
		{
		    String filename = scormFileNames[i];
			InputStream in = getResource(filename);
		    if(in == null) continue;
		    out.putNextEntry(new ZipEntry(filename));
		    while ((len = in.read(buf)) > 0) {
		        out.write(buf, 0, len);
		    }
		    out.closeEntry();
		    in.close();
		}
	}

	private InputStream getResource(String filename) {
		return getClass().getResourceAsStream(filename);
	}
	
	public void readZip(String zipName)
	{	File dataFile = null;
		try 
		{	ZipFile zipFile = new ZipFile(zipName);
			ZipEntry entry = zipFile.getEntry("sco/WiskOpdr.htm");
			
			//om compatible te blijven:
			if(entry==null) entry = zipFile.getEntry("sco\\WiskOpdr.htm");
			if(entry==null) entry = zipFile.getEntry("sco/Sco.htm");
			//
			
			dataFile = File.createTempFile("data","txt");
		    
		    InputStream in =  zipFile.getInputStream(entry);
		    OutputStream out = new BufferedOutputStream(new FileOutputStream(dataFile));
		     
		    byte[] buffer = new byte[1024];
		    int len;
		    while((len = in.read(buffer)) >= 0) out.write(buffer, 0, len);
		    in.close();
		    out.close();
		    zipFile.close();
		    
		    
		} 
		catch (IOException ioe) 
		{	System.err.println("Unhandled exception:");
		    ioe.printStackTrace();
		    return;
		}
		leesFile(dataFile.getAbsolutePath());
		dataFile.delete();
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
		else if(keuze.equals("opslaan als JSON")){
			try {
				saveJSON();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		else if(keuze.equals("print"))this.print();
		else if(keuze.equals(SAVE_AS_FACET))
			try {
				this.saveFacet();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	}
}
