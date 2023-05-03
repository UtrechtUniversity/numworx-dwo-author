package fi.stroomdiagrammen;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

public class CapacityPanel extends JPanel 
{
	Color veryLightBlue = new Color(198, 239, 247);
	JTextField capacityTextField;
	Color bgColor = veryLightBlue;
	
	public CapacityPanel(int w, int h)
	{
		setLayout(null);
		setSize(w, h);
		capacityTextField = new JTextField();
		capacityTextField.setBounds(
				6, 2,
				getSize().width - 12, getSize().height - 4);
		add(capacityTextField);
		
	}

	public void setText(String t)
	{
		capacityTextField.setText(t);
	}
	
	public String getText()
	{
		return capacityTextField.getText();
	}
	
	
	public void paintComponent(Graphics g)
    {   g.setColor(bgColor);
        g.fillRoundRect(0, 0, getSize().width, getSize().height, DrawingPanel.roundWidth, DrawingPanel.roundHeight);            
        g.setColor(Color.black);
        // outline
        g.drawRoundRect(0, 0, getSize().width - 1, getSize().height - 1, DrawingPanel.roundWidth, DrawingPanel.roundHeight);            

    }
}
