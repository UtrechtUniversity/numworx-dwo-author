package fi.heks.scobjects;

import java.awt.*;
import javax.swing.*;

public class ScComponent extends JComponent implements ScObject 
{
	public double schaal;
	public double relx, rely, relb, relh;
	public boolean resized;

	public ScComponent(int x, int y, int b, int h) 
	{
		schaal = 1;
		relx = x;
		rely = y;
		relb = b;
		relh = h;
		setBounds(x, y, b, h);
	}

	public void setResized(boolean b) 
	{
		resized = b;
	}

	public void schaal(double s) 
	{
		schaal = s;
		int x = (int) (schaal * relx);
		int y = (int) (schaal * rely);
		int b = (int) (schaal * relb);
		int h = (int) (schaal * relh);
		setBounds(x, y, b, h);
		resized = true;
	}
}
