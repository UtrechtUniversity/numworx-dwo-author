package fi.wiskopdr.opdrnav;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import fi.wiskopdr.WiskOpdr;

public class OpdrachtNrRij extends JPanel implements ActionListener
{	
	private NrComponent[] opdrachtNrs;
	private ScoreComponent[] scores;
	private int aantalOpdr;
	private ActionListener actionListener;
	private boolean tab;
	private boolean rightClickPossible;
	
	private int size = 25;
	
	

	public OpdrachtNrRij()
	{
	}
	
	public OpdrachtNrRij(int n, int x, int y)
	{	this(n,x,y,25);
		
	}
	public OpdrachtNrRij(int n, int x, int y, int size)
	{	setLayout(null);
		setBackground(Color.white);
		setOpaque(false);
		
		this.size = size;
		setBounds(x, y, n*size, 2*size);
		aantalOpdr = n;
		opdrachtNrs = new NrComponent[n+1];
		scores = new ScoreComponent[n+1];
		for(int i=1 ; i<n+1 ; i++)
		{	opdrachtNrs[i] = new NrComponent(i,size);
			opdrachtNrs[i].addActionListener(this);
			opdrachtNrs[i].zetGemaakt(false);
			add(opdrachtNrs[i]);
			
			scores[i] = new ScoreComponent(size);
			scores[i].setBounds((i-1)*size, size, size*4/5, size-1);
			boolean gr = WiskOpdr.deployVariant!=null && WiskOpdr.deployVariant.equals("GR");
			if(!gr)add(scores[i]);
		}
		
	}
	
	public void setEnabled(boolean b)
	{	for(int i=1 ; i<aantalOpdr+1 ; i++)
		{	opdrachtNrs[i].setEnabled(b);
		}
	}
	
	public void setEnabled(boolean b, int nr)
	{	opdrachtNrs[nr].setEnabled(b);
	}
	
	public boolean getEnabled(int nr)
	{	return opdrachtNrs[nr].getEnabled() ;
	}
	
	public void zetLetters(boolean b)
	{	for(int i=1 ; i<aantalOpdr+1 ; i++)
		{	opdrachtNrs[i].zetLetter(b);
		}
		repaint();
	}
	
	
	public void setTab(boolean b)
	{	for(int i=1 ; i<aantalOpdr+1 ; i++)
		{	opdrachtNrs[i].setTab(b);
		}
		tab = b;
	}
	
	public void setScoresVisible(boolean b)
	{	for(int i=1 ; i<aantalOpdr+1 ; i++)
		{	scores[i].setVisible(b);
		}
	}
	
	public void setRightClickPossible(boolean b)
	{	rightClickPossible = b;
	}
	
	public void setSelected(int n)
	{	if(n>aantalOpdr || n<1)return;
		else 
		{	deselectAll();
			opdrachtNrs[n].setSelected(true);
		}
	}
	
	public void paint(Graphics g)
	{	if(tab)
		{	g.setColor(Color.gray.darker());
			g.drawLine(0,getSize().height-1,getSize().width-1,getSize().height-1);
			g.drawLine(0,getSize().height-2,getSize().width-2,getSize().height-2);
		}
		super.paint(g);
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
	public int geefScore()
	{	int score = 0;
		for(int i=1 ; i<aantalOpdr+1 ; i++)
		{	score += scores[i].geefScore();
		}
		return score;
	}
	public boolean geefGoedFout(int nr)
	{	return opdrachtNrs[nr].geefGoedFout();
	}
	public void zetGemaakt(int n,boolean b)
	{	opdrachtNrs[n].zetGemaakt(b);
	}
	public void zetNoScore(int n,boolean b)
	{	opdrachtNrs[n].zetNoScore(b);
	}
	public boolean geefNoScore(int n)
	{	return opdrachtNrs[n].geefNoScore();
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
	{	String command = e.getActionCommand();
		String commandNr = command.charAt(0)=='p' ? command.substring(6) : command;
		int nr = Integer.parseInt(commandNr);
		if(command.charAt(0)=='p' && !rightClickPossible)
		{ 	command = commandNr;
		}
		setSelected(nr);
		if(actionListener!=null )
		{	actionListener.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, command));
		}
	}
}
		