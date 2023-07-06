package fi.balansfruit;

import java.awt.*;

public class ImageComponent extends Component
{
	private Image image, imageKlein;
	private boolean klein;
	
	public ImageComponent (Image im)
	{	image = im;
		MediaTracker tr = new MediaTracker(this);
		tr.addImage(image,0);
		try{tr.waitForAll();} catch(Exception e) {}
		setSize(image.getWidth(null),image.getHeight(null));
		
	}
	
	public void setImage (Image im)
	{	image = im;
		MediaTracker tr = new MediaTracker(this);
		tr.addImage(image,0);
		try{tr.waitForAll();} catch(Exception e) {}
		setSize(image.getWidth(null),image.getHeight(null));
		
	}
	
	public void paint(Graphics g)
	{	if(image!=null) 
		{	g.drawImage(image,0,0,null);
		}
	}
}
