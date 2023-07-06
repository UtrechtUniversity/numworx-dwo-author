package fi.algebrapijlenopdr.tekstobjects;

public class Link 
{
	private String linkTekst;
	private String url;
	private int width;
	private int height;
	
	public Link (String linkTekst, String url, int width, int height)
	{	this.linkTekst = linkTekst;
		this.url = url;
		this.width = width;
		this.height = height;
	}
	
	public String getLinkTekst()
	{	return linkTekst;
	}
	
	public String getUrl()
	{	return url;
	}
	
	public int getWidth()
	{	return width;
	}
	
	public int getHeight()
	{	return height;
	}
	
	public String toString()
	{	return "$U" + url + "@$W" + width + "@$H" + height + "@";
	}
}
