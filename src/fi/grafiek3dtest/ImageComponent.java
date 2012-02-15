package fi.grafiek3dtest;

import java.awt.*;

public class ImageComponent extends Component
{
	private Image image, imageKlein;
	private boolean klein;
	
	public ImageComponent (Image im)
	{	image = im;
		if(image!=null)imageKlein = image.getScaledInstance(20, 20, Image.SCALE_DEFAULT);
		MediaTracker tr = new MediaTracker(this);
		tr.addImage(imageKlein,0);
		try{tr.waitForAll();} catch(Exception e) {}
		setSize(25,25);
		
	}
	
	public void zetKlein(boolean b)
	{	klein = b;
		//if(b)setSize(15,15);
		//else setSize(25,25);
	}
	
	public void paint(Graphics g)
	{	if(image!=null) 
		{	
			if(klein)
			{	if("MW".equals(Grafiek3DTest.deployVariant) || "GR".equals(Grafiek3DTest.deployVariant))g.drawImage(image,0,0,13,15,0,8,13,23,null);
				else g.drawImage(imageKlein,0,0,null);
			}
			else g.drawImage(image,0,0,null);
		}
	}
}
