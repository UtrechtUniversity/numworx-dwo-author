package fi.algebrapijlenopdr;

import java.awt.*;
import javax.swing.*;

public class CheckComponent extends JPanel 
{
	static final int NEUTRAL = 0;
	static final int CORRECT = 1;
	static final int WRONG = 2;
	int status = NEUTRAL;
	
	
	public CheckComponent(int x, int y, int w, int h)
	{
		setBounds(x, y, w, h);
	}

	public void setNeutral()
	{	status = NEUTRAL;
		repaint();
	}

	public void setCorrect()
	{	status = CORRECT;
		repaint();
	}
	
	public void setWrong()
	{	status = WRONG;
		repaint();
	}
	
	public void paintComponent(Graphics g)
	{	
		
		//g.setColor(Color.orange);
		//g.fillRect(0, 0, getSize().width, getSize().height);
		
		if (status == CORRECT)
		{	g.setColor(Color.green);
			g.fillOval(2, 2, getSize().width - 4, getSize().height - 4);		
		}
		else if (status == WRONG)
		{	g.setColor(Color.red);
			g.fillOval(2, 2, getSize().width - 4, getSize().height - 4);
		}
		
		
		g.setColor(Color.black);
		g.drawOval(2, 2, getSize().width - 4, getSize().height - 4);
	}
	
}
