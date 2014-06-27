package fi.beans.openmath;

import java.applet.Applet;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * 
 * @author wim
 * @version "$Rev$" 
 */
public class MathematicaLink
{

    private static final String UTF_8 = "UTF-8";
    private static final String MATHSHELL = "mathshell";
    private URL url;

    /**
     * Leg een link naar mathematica.
     * @param url typical http://ws.fisme.science.uu.nl/servlet/mathshell/
     */
    public MathematicaLink(URL url)
    {
        this.url = url;
    }
    
    /**
     * Leg een link naar mathematica. Gebruik de parameter "mathshell" om de standaard
     * url te overrulen. Let op de "/" aan het einde van de URL
     * @param applet de applet.
     * @throws MalformedURLException
     */
    public MathematicaLink(Applet applet) throws MalformedURLException 
    {
    
        String mathshell = "/servlet/mathshell/";
        if(null != applet.getParameter(MATHSHELL))
            mathshell = applet.getParameter(MATHSHELL);
        url =  new URL(applet.getCodeBase(), mathshell);
    }
    
    /**
     * Evalueer een Mathematica statement.
     * @param arg Mathematica input
     * @return Mathematica output
     * @throws IOException
     */
    public String eval(String arg) throws IOException
    {
        URLConnection con;
        URL u = new URL(url, "execute_native_native");
        con = u.openConnection();
        con.setDoOutput(true);
        con.setDoInput(true);
        con.setUseCaches(true);
        con.setAllowUserInteraction(false);
        byte[] asBytes = arg.getBytes(UTF_8);
        con.setRequestProperty("Content-Length", Integer.toString(asBytes.length));
        con.setRequestProperty("Content-Type", "text/plain");
        OutputStream out = con.getOutputStream();
        out.write(asBytes);
        out.flush();
        InputStream in = con.getInputStream();
        InputStreamReader reader = new InputStreamReader(in, UTF_8);
        char[] data=new char[128];
        StringBuffer sb = new StringBuffer();
        int len;
        while( (len = reader.read(data)) > 0)
                sb.append(data, 0, len);
        return sb.toString();
    }

}