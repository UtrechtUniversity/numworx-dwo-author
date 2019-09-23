package fi.wiskopdr.tekstobjects;

import fi.wiskopdr.SimpleSwingBrowser;
import fi.wiskopdr.WiskOpdr;
import netscape.javascript.JSObject;

public class Link 
{
	private static final String GOTO_PREFIX = "goto:"; // # lag voor de hand, is illegaal symbool
	private static LinkIF wiskOpdr; // FIXME een INTERFACE hier nodig (en niet statisch!) 
	private String linkTekst;
	private String url;
	private String[] urls;
	private int[] grensScores;
	private boolean voorwaardelijk = false;
	private int width=400;
	private int height=400;
	private LinkType embedded = LinkType.FALSE;
	
	public static void setJSObjectOwner(LinkIF wiskOpdr)
	{	Link.wiskOpdr = wiskOpdr;
	}
	
	/*
	public Link (String linkTekst, String url, int width, int height)
	{	this.linkTekst = linkTekst;
		this.url = url;
		this.width = width;
		this.height = height;
		
		urls = null;
		urls = new String[1];
		urls[0] = url;
		grensScores = null;
		voorwaardelijk = false;
	}
	*/
	
	public Link (String linkTekst, String[] urls, int width, int height, boolean embedded, int[] grensScores)
	{
		this(linkTekst,urls,width,height,embedded?LinkType.TRUE:LinkType.FALSE,grensScores);
	}

	public Link (String linkTekst, String[] urls, int width, int height, LinkType embedded, int[] grensScores)
    {
        this.linkTekst = linkTekst;
        this.urls = urls;
        this.width = width;
        this.height = height;
        this.embedded = embedded;
        //this.url = urls[0];
        if(grensScores != null)
            voorwaardelijk = true;
    }
	
	//public void activate()
	//{
	//	activate(0);
	//}
	
	
	public void activate(int score)
	{
		url = "";
		if(grensScores == null || score <= grensScores[0])
			url = urls[0];
		else
		{	for(int i = 1; i < grensScores.length; i++)
				if(score > grensScores[i-1] && score <= grensScores[i])
					url = urls[i];
		}
		if(url.equals(""))
			url = urls[urls.length-1];
		
		
		if(url.startsWith(GOTO_PREFIX))
		{
			String rest = url.substring(GOTO_PREFIX.length());
			wiskOpdr.gotoScoNr(rest);
			return;
		}
		Object[] args = new Object[5];
        args[0] = url;
        args[1] = "name";
        args[2] = ""+width;
        args[3] = ""+height;
        args[4] = "yes";
        String result = null;
		if(wiskOpdr.getJSObject()!=null) result = (String)((JSObject) wiskOpdr.getJSObject()).call("NewPopUp", args);
		else
		{
			SimpleSwingBrowser browser = new SimpleSwingBrowser();
			browser.setSize(width, height);
			browser.setVisible(true);
			browser.loadURL(url);
		}
		
	}
	
	public String getLinkTekst()
	{	return linkTekst;
	}
	
	//public String getUrl()
	//{	return url;
	//}
	
	public String[] getUrlString()
	{	return urls;
	}
	
	public int[] getGrensScores()
	{
		return grensScores;
	}
	
	public int getWidth()
	{	return width;
	}
	
	public int getHeight()
	{	return height;
	}
	
	public LinkType getEmbedded()
	{	return embedded;
	}
	
	
	public String toString()
	{	if(grensScores == null)
		{	url = urls[0];
			return "$U" + url + "@$B" + width + "@$C" + height + "@$E" + embedded.display() + "@";
		}
		else
		{	String urlString = "";
			String grensString = "";
			urlString = urlString + "$U" + urls[0];
			grensString = grensString + "@$G" + grensScores[0];
			for(int i = 1; i < urls.length; i++ )
			{	urlString = urlString + "@$U" + urls[i];
				grensString = grensString + "@$G" + grensScores[i];
			}
			return urlString + grensString + "@$B" + width + "@$C" + height + "@$E" + embedded.display() + "@";
			
		}
	}
	
	
	
}
