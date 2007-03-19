package fi.mozarch;

import java.io.*;
import java.net.*;

public class Inzending
{	
	private String sleutel;
	private String bouwselnaam;
	private String file;
	private static String urlString = "http://www.fi.uu.nl/servlet/script/2"; //localhost/blokken/blokkenfile.php" ;//"http://www.newpoints.net/blokken/blokkenfile.php";
	
	public Inzending(String sleutel, String bouwselnaam, String file)
	{	this.sleutel = sleutel;
		this.bouwselnaam = bouwselnaam;
		this.file = file;
	}
	
	public String geefSleutel()
	{	return sleutel;
	}
	
	public String geefBouwselnaam()
	{	return bouwselnaam;
	}
	
	public String geefFile()
	{	return file;
	}
	
	public void haalFile() throws IOException 
	{	//try
		{	URL url = null;
			try
			{  url = new URL(urlString + "?id=" + sleutel);
			}
			catch (IOException exception)
			{ 
			}
		
			URLConnection connection = url.openConnection();
			//connection.setDoOutput(true);

			//PrintWriter out = new PrintWriter(connection.getOutputStream());
			//out.print("id" + "=" + URLEncoder.encode(sleutel) + '\n');
			//System.out.println("id" + "=" + URLEncoder.encode(sleutel) + '\n');
			//out.close();

			BufferedReader in;
			try
			{  in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			}
			catch (FileNotFoundException exception)
			{  //InputStream err = ((HttpURLConnection)connection).getErrorStream();
			   //if (err == null) throw exception;
				 InputStream err = null;
				 in = new BufferedReader(new InputStreamReader(err));
			}
			//extra readline? Lijkt nodig bij newpoinst.net?
			//String s = in.readLine();
			//System.out.println(s);
			file = in.readLine();
			//System.out.println(file);
		}
		//catch(IOException ioe)
		//{	getParent().zetFoutmelding("Er is geen verbinding");
		//}
	}
}
