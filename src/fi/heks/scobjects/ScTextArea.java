package fi.heks.scobjects;

import java.awt.*;

public class ScTextArea extends TextArea implements ScObject
{	
	public double schaal;
	public double relx,rely,relb,relh;
	public boolean resized;
	
	public ScTextArea(int x, int y, int b, int h, int rows, int columns, int scrollbars, String str)
	{	super(str, rows, columns, scrollbars);
		schaal = 1;
		relx = x;
		rely = y; 
		relb = b;
		relh = h;
		setBounds(x,y,b,h);
		Font f = new Font("SansSerif", Font.PLAIN, 12);
		setFont(f);
	}
	
	public void setResized(boolean b)
	{	resized = b;
	}
	
	public void schaal(double s)
	{	schaal = s;
		int x = (int)(schaal*relx);
		int y = (int)(schaal*rely);
		int b = (int)(schaal*relb);
		int h = (int)(schaal*relh);
		setBounds(x,y,b,h);
		Font f = new Font("SansSerif", Font.PLAIN, 12);
		setFont(f);
	}
}
