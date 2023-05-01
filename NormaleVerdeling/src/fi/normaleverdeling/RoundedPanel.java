package fi.normaleverdeling;

import java.awt.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;

/* A JPanel implementation that has Rounded Edges. */

public class RoundedPanel extends JLayeredPane {

	int curvature = 60;
	Color panelColor = Color.white;
	Color borderColor = null;
	int borderThickness = 0;
	private boolean transparant = false;
	
	public RoundedPanel() {
		this( new FlowLayout(FlowLayout.LEFT), 60 );
	}
	
	public RoundedPanel(LayoutManager layout) {
		this(layout, 20);			
	}
	
	public RoundedPanel(LayoutManager layout, int curvature) {
		setLayout(layout);
		setCurvature(curvature);
		setBorder( new EmptyBorder(1,5,1,5) );
		setOpaque(false);
	}
	
	public void zetTransparant(boolean b)
	{
		transparant = b;
	}
	
	public boolean isTransparant()
	{
		return transparant;
	}
	
	public RoundedPanel(int curvature) {
		this( new FlowLayout(FlowLayout.LEFT), curvature );		
	}
	
	public void setBackground(Color c) {
		setPanelColor(c);	
	}
	
	public void setPanelColor(Color c) {
		panelColor = c;
		borderColor = c;
		repaint();
	}
	
	public void setBorder(Color c, int thickness)
	{	borderColor = c;
		borderThickness = thickness;
	}
	
	public void setBorder(Color c)
	{	borderColor = c;
		borderThickness = 1;
	}
	
	public void setCurvature(int curvature) {
		this.curvature = curvature;
		repaint();
	}
	
	public void paintComponent(Graphics g) {			
		//super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
								RenderingHints.VALUE_ANTIALIAS_ON);
		//g.clearRect(0, 0, getPreferredSize().width, getPreferredSize().height);
		g2.setColor( borderColor);
		//g2.drawRoundRect(0, 0, getPreferredSize().width, getPreferredSize().height,
		//curvature, curvature);
		if(!transparant) g2.fillRoundRect(0, 0, getWidth(), getHeight(), curvature, curvature);
		else if(borderThickness>0) 
			for(int i=0 ; i<borderThickness ; i++)
			{	if(curvature>0)g2.drawRoundRect(i, i, getWidth()-1-2*i, getHeight()-1-2*i, curvature-2*i, curvature-2*i);
				else g2.drawRoundRect(i, i, getWidth()-1-2*i, getHeight()-1-2*i, 0, 0);
			}
		//g2.draw3DRect(0, 0, getPreferredSize().width, getPreferredSize().height, false);
		
		g2.setColor(panelColor);
		//g2.fillRoundRect(1, 1, getPreferredSize().width - 2, getPreferredSize().height - 2,
		//curvature, curvature);
		if(!transparant)
		{	if(curvature>0) g2.fillRoundRect(borderThickness, borderThickness, getWidth() - 2*borderThickness, getHeight() - 2*borderThickness, curvature-2*borderThickness, curvature-2*borderThickness);
			else g2.fillRoundRect(borderThickness, borderThickness, getWidth() - 2*borderThickness, getHeight() - 2*borderThickness, 0, 0);
		
		}
		//g2.fill3DRect(2, 2, getPreferredSize().width, getPreferredSize().height, true);
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
		super.paintComponent(g);
	}
}