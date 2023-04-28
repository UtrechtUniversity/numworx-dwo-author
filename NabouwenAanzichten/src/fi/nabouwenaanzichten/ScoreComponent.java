package fi.nabouwenaanzichten;

import java.awt.*;
import java.awt.event.*;

class ScoreComponent extends Component
{	
	private int score = 0;
	
	public void paint(Graphics g)
	{	
		g.setColor(Color.black);
		int cor;
		if(score<10)cor = 2;
		else cor = -2;
		if(score>0)g.drawString(Integer.toString(score),5+cor,15);
	}
	
	public void zetScore(int sc)
	{	score = sc;
		repaint();
	}
	
	public int geefScore()
	{	return score;
	}
	
}