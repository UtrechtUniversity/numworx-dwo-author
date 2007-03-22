package fi.algebrapijlenopdr.tekstobjects;

public class Link 
{
	private String url;
	private int width;
	private int height;
	
	public Link (String url, int width, int height)
	{	this.url = url;
		this.width = width;
		this.height = height;
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
	{	return "$U" + url + "," + width + "," + height + "@";
	}
}
