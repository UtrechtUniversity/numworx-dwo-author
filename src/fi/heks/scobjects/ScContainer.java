package fi.heks.scobjects;

import java.awt.*;

public class ScContainer extends Container implements ScObject
{	
	public double schaal;
	public double relx,rely,relb,relh;
	public boolean resized;
	
	public ScContainer()
	{	setLayout(null);
		schaal = 1;
	}
	
	public ScContainer(int x, int y, int b, int h)
	{	setLayout(null);
		schaal = 1;
		relx = x;
		rely = y; 
		relb = b;
		relh = h;
		setBounds(x,y,b,h);
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
		resized = true;
		
		int n = getComponentCount();
		for(int i=0 ; i<n ; i++)
		{	Component c = getComponent(i);
			ScObject scc = null;
			try
			{	scc = (ScObject)c;
			}
			catch(ClassCastException ce)
			{}
			if(scc!=null)scc.schaal(schaal);
			
		}
	}
}
