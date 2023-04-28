package fi.nabouwenaanzichten;

import java.awt.*;
import java.awt.event.*;

class OpdrachtNrRij_m extends OpdrachtNrRij implements ActionListener
{	
	private NrComponent_m[] opdrachtNrs;
	private ScoreComponent_m[] scores;
	private int aantalOpdr;
	private ActionListener actionListener;

	
	public OpdrachtNrRij_m(int n, int x, int y)
	{	setLayout(null);
		setBackground(Color.white);
		setBounds(x, y, n*20, 40);
		aantalOpdr = n;
		opdrachtNrs = new NrComponent_m[n+1];
		scores = new ScoreComponent_m[n+1];
		for(int i=1 ; i<n+1 ; i++)
		{	opdrachtNrs[i] = new NrComponent_m(i);
			opdrachtNrs[i].addActionListener(this);
			opdrachtNrs[i].zetGemaakt(false);
			add(opdrachtNrs[i]);
			
			scores[i] = new ScoreComponent_m();
			scores[i].setBounds((i-1)*20, 20, 15, 13);
			add(scores[i],0);
		}
	}
	
	public void setScoresVisible(boolean b)
	{	for(int i=1 ; i<aantalOpdr+1 ; i++)
		{	scores[i].setVisible(b);
		}
	}
	
	public void setSelected(int n)
	{	if(n>aantalOpdr || n<1)return;
		else 
		{	deselectAll();
			opdrachtNrs[n].setSelected(true);
		}
	}
	
	public void deselectAll()
	{	for(int i=1 ; i<aantalOpdr+1 ; i++)
		{	opdrachtNrs[i].setSelected(false);
		}
	}
	public void zetScore(int nr, int score)
	{	scores[nr].zetScore(score);
		repaint();
	}
	public int geefScore(int nr)
	{	return scores[nr].geefScore();
	}
	public int geefScoreTotaal()
	{	int scoreTotaal = 0;
		for(int i=1 ; i<aantalOpdr+1 ; i++)
		{	scoreTotaal += scores[i].geefScore();
		}
		return scoreTotaal;
	}
	public boolean geefGoedFout(int nr)
	{	return opdrachtNrs[nr].geefGoedFout();
	}
	public void zetGemaakt(int n,boolean b)
	{	opdrachtNrs[n].zetGemaakt(b);
	}
	public void zetGemaaktHalf(int n)
	{	opdrachtNrs[n].zetGemaaktHalf();
	}
	public void maakSchoon(int n)
	{	opdrachtNrs[n].maakSchoon();
    }
	
	public void addActionListener(ActionListener listener)
	{	actionListener = AWTEventMulticaster.add(actionListener, listener);
	}
	
	public void removeActionListener(ActionListener listener)
	{	actionListener = AWTEventMulticaster.remove(actionListener, listener);
	}
	
	public void actionPerformed(ActionEvent e)
	{	int nr = Integer.parseInt(e.getActionCommand());
		setSelected(nr);
		if(actionListener!=null )
		{	actionListener.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, e.getActionCommand()));
		}
	}
}
		