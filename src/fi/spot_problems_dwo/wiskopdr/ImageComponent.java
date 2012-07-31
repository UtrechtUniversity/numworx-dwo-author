package fi.spot_problems_dwo.wiskopdr;

import java.awt.*;

import javax.swing.*;

public class ImageComponent extends JComponent
{
	private Image image;
	
	public ImageComponent (Image im)
	{	image = im;
		setSize(30,30);
		//setSize(image.getWidth(null),image.getHeight(null));
	}
	
	public void paint(Graphics g)
	{	g.drawImage(image,0,0,null);
	}
}
