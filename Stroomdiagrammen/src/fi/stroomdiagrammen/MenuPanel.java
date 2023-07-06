package fi.stroomdiagrammen;

import java.awt.*;

import javax.swing.*;

public class MenuPanel extends JPanel 
{
	
	public void paintComponent(Graphics g)
	{
		for (int i = 0; i < 10; i++)
		{
			g.setColor(new Color(200 + 5 * i, 200 + 5 * i, 200 + 5 * i));
			g.fillRect(0, getHeight() - (i + 1) * getHeight() / 10, getWidth(), getHeight() / 10 + 1);
		}

	}
}
