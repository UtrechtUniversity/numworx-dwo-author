package fi.wiskopdr;

import java.awt.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;

/* A JPanel implementation that has Rounded Edges. */

public class RoundedPanel extends JLayeredPane {

	int curvature = 60;
	Color panelColor = WiskOpdr.bgcolor;//UIManager.getColor("Panel.background");
	Color borderColor = null;
	int borderThickness = 0;
	private boolean transparant = false;
	
	int callOutX0 = 0;
	int callOutY0 = 0;
	int callOutX1 = 0;
	int callOutY1 = 0;
	
	
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
	
	public void setCallOutBox(int x0, int y0, int x1, int y1)
	{
		callOutX0 = x0;
		callOutY0 = y0;
		callOutX1 = x1;
		callOutY1 = y1;
		
	}
	
	public void paintComponent(Graphics g) {			
		//super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
								RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setColor( borderColor);
		int macC = 0;
		String jVersion =  System.getProperty("java.specification.version");
		if(WiskOpdr.mac && (jVersion.equals("1.3") || jVersion.equals("1.4") || jVersion.equals("1.5"))) macC = 1;
		if(!transparant && borderThickness > 0) g2.fillRoundRect(callOutX0 + -macC, callOutY0 + -macC, getWidth()+macC-callOutX0-callOutX1, getHeight()+macC-callOutY0-callOutY1, curvature, curvature);
		else if(borderThickness>0) 
			for(int i=0 ; i<borderThickness ; i++)
			{	if(curvature>0)
				{	g2.drawRoundRect(callOutX0 + i, callOutY0 + i, getWidth()-1-2*i-callOutX0-callOutX1, getHeight()-1-2*i-callOutY0-callOutY1, curvature-2*i, curvature-2*i);
					g2.drawRoundRect(callOutX0 + i, callOutY0 + i, getWidth()-1-2*i-callOutX0-callOutX1, getHeight()-1-2*i-callOutY0-callOutY1, curvature-2*i+1, curvature-2*i+1);
				}
				else g2.drawRoundRect(callOutX0 + i, callOutY0 + i, getWidth()-1-2*i-callOutX0-callOutX1, getHeight()-1-2*i-callOutY0-callOutY1, 0, 0);
			}
		g2.setColor(panelColor);
		if(!transparant)
		{	
			
			if(curvature>0) g2.fillRoundRect(callOutX0 + borderThickness-macC, callOutY0 + borderThickness-macC, getWidth() - 2*borderThickness+macC-callOutX0-callOutX1, getHeight() - 2*borderThickness+macC-callOutY0-callOutY1, curvature-2*borderThickness-2*macC, curvature-2*borderThickness-2*macC);
			else g2.fillRoundRect(callOutX0 + borderThickness-macC, callOutY0 + borderThickness-macC, getWidth() - 2*borderThickness+macC-callOutX0-callOutX1, getHeight() - 2*borderThickness+macC-callOutY0-callOutY1, 0, 0);
		
		}
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
		super.paintComponent(g);
	}
}