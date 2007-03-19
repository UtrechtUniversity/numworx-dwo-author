package fi.mozarch;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.util.*;

public class InzendingenLijst extends java.awt.List implements ItemListener
{
	private int aantalInzendingen;
	private int aantalNuGemaakt;
	private Inzending[] inzendingen;
	private static String urlStringLijst = "http://www.fi.uu.nl/servlet/script/3";//"http://www.newpoints.net/blokken/";
	private static String urlStringSlaOp = "http://www.fi.uu.nl/servlet/script/1";//"http://www.newpoints.net/blokken/";
	private static String urlStringVerwijder = "http://www.fi.uu.nl/servlet/script/4";//"http://www.newpoints.net/blokken/";

	private String actieveFile;
	private ActionListener actionListener;
	
	public InzendingenLijst(int aantalRijen, boolean meervSel)
	{	super(aantalRijen, meervSel);
		addItemListener(this);
		inzendingen = new Inzending[5000];
		//haalInzendingen();
		aantalNuGemaakt = 0;
		aantalInzendingen = 0;
		
	}
	
	public void haalInzendingen() throws IOException 
	{	
		URL url = null;
		String[] lines = null;
		//try
		//{	
			try
			{  url = new URL(urlStringLijst);
			}
			catch (IOException exception)
			{ 
			}
		
			URLConnection connection = url.openConnection();
			connection.connect();

			BufferedReader in;
			try
			{  in = new BufferedReader(new
			      InputStreamReader(connection.getInputStream()));
			}
			catch (FileNotFoundException exception)
			{  //InputStream err = ((HttpURLConnection)connection).getErrorStream();
			   //if (err == null) throw exception;
			   InputStream err = null;
					 in = new BufferedReader(new InputStreamReader(err));
			}
			StringBuffer response = new StringBuffer();
			//in.readLine();// Lijkt nodig bij newpoinst.net?
			Vector buffer = new Vector();
		
				int i=0;
				String s;
				while ((s = in.readLine()) != null)
				{	buffer.addElement(s);
					i++;
				}
				in.close();
		
			lines = new String[buffer.size()];
			for(int j=0 ; j<buffer.size() ; j++)
			{	lines[j] = (String)buffer.elementAt(j);
				//System.out.println(lines[j]);//ter controle
			}
		//}
		//catch(IOException ioe)
		//{	((BestandenFrame)getParent()).zetFoutmelding("Er is geen verbinding");
		//}
		
		removeAll();
		aantalNuGemaakt = 0;
		if(lines!=null)
		{	for(int k=0 ; k<lines.length-1 ; k+=2)
			{	if(lines[k+1]!=null)
				{	lines[k+1] = lines[k+1].trim();
					//if(!lines[i+1].equals(""))
					{	voegInzendingToe(lines[k],lines[k+1], null);
					}
				}
			}
		}
	
	}
	
	public void voegInzendingToe(String bouwselnaam, String file) throws IOException 
	{	aantalNuGemaakt++;
		inzendingen[aantalInzendingen] = new Inzending(Integer.toString(-aantalNuGemaakt), bouwselnaam, file);
		add(bouwselnaam);
		aantalInzendingen++;
		
		
		//try
		//{	
			URL url = null;
			url = new URL(urlStringSlaOp);
		
			URLConnection connection = url.openConnection();
			connection.setDoOutput(true);

			PrintWriter out = new PrintWriter(connection.getOutputStream());
			out.print("bouwselnaam" + "=" + URLEncoder.encode(bouwselnaam) + "&" + "file" + "=" + URLEncoder.encode(file) + '\n');
			out.close();
			//System.out.println("bouwselnaam" + "=" + URLEncoder.encode(bouwselnaam) + "&" + "file" + "=" + URLEncoder.encode(file) + '\n');
		
			// evenkijken wat hij teruggeeft. Dat blijkt nodig om het opslaan ook met de javaplugin
			//goed te laten werken
		//********************************************************************
			BufferedReader in;
			try
			{  in = new BufferedReader(new
			     InputStreamReader(connection.getInputStream()));
			}
			catch (FileNotFoundException exception)
			{  //InputStream err = ((HttpURLConnection)connection).getErrorStream();
			   //if (err == null) throw exception;
				 InputStream err = null;
				 in = new BufferedReader(new InputStreamReader(err));
			}
			String line;
			StringBuffer response = new StringBuffer();
			while((line = in.readLine()) != null)
				response.append(line + "\n");
			System.out.println(response.toString());
			
		//********************************************************************	
		//}
		//catch (IOException exception)
		//{  ((BestandenFrame)getParent()).zetFoutmelding("Er is geen verbinding");
		//}
		      
		
			
	}
	
	public void verwijderBouwsel()
	{	int n = this.getSelectedIndex();
		remove(n);
		aantalNuGemaakt--;
		String id = inzendingen[n].geefSleutel();
		aantalInzendingen--;
		
		try
		{	URL url = null;
			url = new URL(urlStringVerwijder + "?id=" +id);
		
		
			URLConnection connection = url.openConnection();
			
		
			// evenkijken wat hij teruggeeft. Dat blijkt nodig om het opslaan ook met de javaplugin
			//goed te laten werken
		//********************************************************************
			BufferedReader in;
			try
			{  in = new BufferedReader(new
			     InputStreamReader(connection.getInputStream()));
			}
			catch (FileNotFoundException exception)
			{  //InputStream err = ((HttpURLConnection)connection).getErrorStream();
			   //if (err == null) throw exception;
				 InputStream err = null;
				 in = new BufferedReader(new InputStreamReader(err));
			}
		//********************************************************************	
		}
		catch (IOException exception)
		{  ((BestandenFrame)getParent()).zetFoutmelding("Er is geen verbinding");
		}
		      
		
			
	}
	
	public void voegInzendingToe(String sleutel, String bouwselnaam, String file)
	{	inzendingen[aantalInzendingen] = new Inzending(sleutel, bouwselnaam, file);
		add(sleutel + "   " + bouwselnaam);
		aantalInzendingen++;
	}
	
	public String geefActieveFile()
	{	int nr = getSelectedIndex();
		if(nr>-1)
		{	if(inzendingen[nr].geefFile()==null)
			{	try 
				{	inzendingen[nr].haalFile();
				}
				catch(IOException ioe)
				{	((BestandenFrame)getParent()).zetFoutmelding("Er is geen verbinding");
				}
			}
			actieveFile = inzendingen[nr].geefFile();
		}
		return actieveFile;
	}
		
	public void addActionListener(ActionListener listener)
	{	actionListener = AWTEventMulticaster.add(actionListener, listener);
	}
	
	public void removeActionListener(ActionListener listener)
	{	actionListener = AWTEventMulticaster.remove(actionListener, listener);
	}
	
	public void itemStateChanged(ItemEvent e)
	{	int nr = getSelectedIndex();
		if(nr>-1)
		{	if(inzendingen[nr].geefFile()==null)
			{	getParent().setCursor(new Cursor(Cursor.WAIT_CURSOR));
				try 
				{	inzendingen[nr].haalFile();
				}
				catch(IOException ioe)
				{	((BestandenFrame)getParent()).zetFoutmelding("Er is geen verbinding");
					getParent().setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
					return;
				}
				getParent().setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			}
			actieveFile = inzendingen[nr].geefFile();
		}
		else actieveFile = null;
		if(actionListener!=null)
		{	actionListener.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, ""+this));
		}
	}
	
	
}
