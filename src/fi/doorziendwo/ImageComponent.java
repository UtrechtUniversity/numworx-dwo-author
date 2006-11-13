package fi.doorziendwo;

import java.awt.*;

public class ImageComponent extends Component
{
	private Image image;
	
	public ImageComponent ()
	{	setSize(20,20);
		//setSize(image.getWidth(null),image.getHeight(null));
	}
	
	public ImageComponent (Image im)
	{	image = im;
		setSize(image.getWidth(null),image.getHeight(null));
	}
	
	public void paint(Graphics g)
	{	if(image!=null)
		{	
			g.drawImage(image,0,0,null);
		}
	}
	
	public void zetImage(Image im)
	{	image = im;
		repaint();
	}
}
