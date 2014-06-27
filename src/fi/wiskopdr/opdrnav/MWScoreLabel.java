package fi.wiskopdr.opdrnav;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class MWScoreLabel extends JPanel 
{
	private JLabel tekstLabel;
	private JLabel scoreLabel;
	private JLabel tussenLabel;
	private JLabel totaalLabel;
	private Font tekstFont = new Font("SansSerif", Font.PLAIN, 12);
	private Font numberFont = new Font("SansSerif", Font.BOLD, 12);
	
	public MWScoreLabel()
	{
		FlowLayout flowLayout = new FlowLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		setLayout(flowLayout);
		
		tekstLabel = new JLabel();
		tekstLabel.setFont(tekstFont);
		add(tekstLabel);
		
		scoreLabel = new JLabel();
		scoreLabel.setFont(numberFont);
		scoreLabel.setForeground(new Color(0,160,0));
		add(scoreLabel);
		
		tussenLabel = new JLabel();
		tussenLabel.setFont(tekstFont);
		add(tussenLabel);
		
		totaalLabel = new JLabel();
		totaalLabel.setFont(numberFont);
		totaalLabel.setForeground(new Color(0,160,0));
		add(totaalLabel);
	}
	
	public void setContent(String tekst, int score, String tussenTekst, int totaal)
	{
		tekstLabel.setText(tekst);
		scoreLabel.setText(""+score);
		tussenLabel.setText(tussenTekst);
		totaalLabel.setText(""+totaal);
	}
	

}
