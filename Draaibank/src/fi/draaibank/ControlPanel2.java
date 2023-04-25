package fi.draaibank;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

class ControlPanel2 extends JPanel implements ActionListener
{	
	JButton wisknop,terugknop,vergrootButton,verkleinButton;
	TekenPanel eigenaar;
	int offset = 10;
	
	public ControlPanel2(TekenPanel gv)
	{	
		eigenaar = gv;
		setBackground(Color.lightGray);
		
		setLayout(null);

		int currentX = offset;
		int currentY = 10;
		
		terugknop = new JButton(Draaibank.rb.getString("terugKnopLabel"));
		terugknop.setBounds(currentX,currentY,70,20);
		add(terugknop);
		terugknop.addActionListener(this);
		
		currentX += terugknop.getSize().width + offset;
		
		wisknop = new JButton(Draaibank.rb.getString("wisKnopLabel"));
		wisknop.setBounds(currentX,currentY,60,20);
		add(wisknop);
		wisknop.addActionListener(this);
		
		currentX += wisknop.getSize().width + offset;
		
		vergrootButton = new JButton(DBInteractiePanel.vergrootIcon);
		vergrootButton.setBounds(currentX, currentY, 20 , 20);
		vergrootButton.setVisible(false);
		add(vergrootButton);
		vergrootButton.addActionListener(this);
		
		currentX += vergrootButton.getSize().width + offset;
		
		verkleinButton = new JButton(DBInteractiePanel.verkleinIcon);
		verkleinButton.setBounds(currentX, currentY, 20 , 20);
		verkleinButton.setVisible(false);
		add(verkleinButton);
		verkleinButton.addActionListener(this);
		
		currentX += verkleinButton.getSize().width + offset;

		
	}
	
	public void actionPerformed(ActionEvent e)
	{	if(e.getSource() == wisknop)
		{	eigenaar.wis();
		}
		else if(e.getSource() == terugknop)
		{	eigenaar.tekenStapTerug();
		}
		else if(e.getSource() == vergrootButton)
		{	eigenaar.zoomIn();
		}
		else if(e.getSource() == verkleinButton)
		{	eigenaar.zoomUit();
		}
		
	}
}