package fi.geodefull;

import java.awt.*;
import java.awt.event.*;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import fi.beans.grnuminput.*;


public class KleurKiezer extends JPanel implements ActionListener
{	
	GeodeKleurProg eigenaar;
	String lijnkleur,vulkleur;
	JButton lijnkleurKnop,vulkleurKnop;
	JPanel lijnkleurLabel,vulkleurLabel;
	boolean lijnActief = false;
	boolean vulActief = false;
	JButton[] kleurenV;
	Color[] kleuren = {Color.black,Color.white,Color.gray,Color.lightGray,Color.red,Color.orange,
					Color.yellow,Color.green,Color.cyan,Color.blue,Color.magenta,Color.pink,Color.white};
	String[] kleurnamen = {"zwart","wit","grijs","lichtgrijs","rood","oranje","geel","groen",
						   "cyaan","blauw","magenta","roze","transparant"};
	
	
	public KleurKiezer(GeodeKleurProg gp)
	{	
		setLayout(null);
		setPreferredSize(new Dimension(600,30));
		eigenaar = gp;
		lijnkleur = "zwart";
		vulkleur = "oranje";
		lijnkleurKnop = new JButton(" lijnkleur ");
		lijnkleurKnop.setBounds(5,5,100,20);
		lijnkleurKnop.setMargin(new Insets(4,10,4,10));
		lijnkleurKnop.addActionListener(this);
		vulkleurKnop = new JButton(" vulkleur ");
		vulkleurKnop.setBounds(135,5,100,20);
		vulkleurKnop.setMargin(new Insets(4,10,4,10));
		vulkleurKnop.addActionListener(this);
		lijnkleurLabel = new JPanel();
		lijnkleurLabel.setBounds(110,5,20,20);
		
		vulkleurLabel = new JPanel();
		vulkleurLabel.setBounds(240,5,20,20);
		lijnkleurLabel.setBackground(Color.black);
		vulkleurLabel.setBackground(Color.orange);
		kleurenV = new JButton[13];
		//setLayout(new FlowLayout(FlowLayout.LEFT));
		
		
		
		
		add(lijnkleurKnop);
		add(lijnkleurLabel);
		add(vulkleurKnop);
		add(vulkleurLabel);
		kleurenV[12] = new JButton("transparant");
		for(int i=0 ; i<13 ; i++)
		{	final Color k = kleuren[i];
			if(i<12)kleurenV[i] = new JButton("") {
				public void paintComponent(Graphics g){
					
					g.setColor(k);
					g.fillRect(0,0, 19, 19);
					g.setColor(Color.black);
					g.drawRect(0,0, 19, 19);
				}
				
			}
			;
			kleurenV[i].setBackground(kleuren[i]);
			kleurenV[i].setBounds(280+i*25,5,20,20);
			
			add(kleurenV[i]);
			kleurenV[i].addActionListener(this);
			kleurenV[i].setVisible(false);
		}

	}
	public void actionPerformed(ActionEvent e)
	{	if(e.getSource() == lijnkleurKnop)
		{	if(lijnkleurKnop.getLabel()==" lijnkleur ")
			{	kleurknoppenZichtbaar(true);
				lijnkleurKnop.setLabel("kies kleur");
				lijnActief = true;
			}
			else
			{	kleurknoppenZichtbaar(false);
				lijnkleurKnop.setLabel(" lijnkleur ");
				lijnActief = false;
			}
			eigenaar.onthoudKleur();
		}
		if(e.getSource() == vulkleurKnop)
		{	if(vulkleurKnop.getLabel()==" vulkleur ")
			{	kleurknoppenZichtbaar(true);
				vulkleurKnop.setLabel("kies kleur");
				vulActief = true;
			}
			else
			{	kleurknoppenZichtbaar(false);
				vulkleurKnop.setLabel(" vulkleur ");
				vulActief = false;
			}
			eigenaar.onthoudKleur();
		}
		if(vulActief || lijnActief)
		{	for(int i=0 ; i<13 ; i++)
			{	if(e.getSource() == kleurenV[i])
				{	if(lijnActief)
					{	lijnkleurLabel.setBackground(kleuren[i]);
						lijnkleur = kleurnamen[i];
						lijnActief = false;
						lijnkleurKnop.setLabel(" lijnkleur ");
						kleurknoppenZichtbaar(false);
					}
					if(vulActief)
					{	vulkleurLabel.setBackground(kleuren[i]);
						vulkleur = kleurnamen[i];
						vulActief = false;
						vulkleurKnop.setLabel(" vulkleur ");
						kleurknoppenZichtbaar(false);
					}
				}
			}
		}
		
		
	}
	void kleurknoppenZichtbaar(boolean b)
	{	for(int i=0 ; i<13 ; i++)
		{	kleurenV[i].setVisible(b);
		}
		doLayout();
	}
	public String geefLijnkleur()
	{	return lijnkleur;
	}
	public String geefVulkleur()
	{	return vulkleur;
	}

}

