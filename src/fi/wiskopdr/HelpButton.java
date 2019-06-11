package fi.wiskopdr;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.*;

import javax.swing.*;

import fi.wiskopdr.opdrnav.OpdrNavStructEdit;
import javafx.embed.swing.JFXPanel;

public class HelpButton extends JLabel implements  MouseListener
{
	String url;
	
	public HelpButton(String url){	
		super("?");
		this.url = url;
		addMouseListener(this);
		setFont(new Font("SansSerif",Font.BOLD,16));
		setHorizontalAlignment(JLabel.CENTER);
	    setVerticalAlignment(JLabel.CENTER);
	    setForeground(new Color(50,72,111));
	}
	
	public void paintComponent(Graphics gr) {
	  Graphics2D g = (Graphics2D)gr;
	  g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	  g.setColor(new Color(211,229,244));
	  g.fillOval(0,0,getWidth(),getHeight());
	  super.paintComponent(g);  
	  g.setColor(new Color(50,72,111));
	    g.setFont(new Font("SansSerif",Font.BOLD,14));
	    g.drawOval(0,0,getWidth()-1,getHeight()-1);
	}

  @Override
  public void mouseClicked(MouseEvent e) {
  }

  @Override
  public void mousePressed(MouseEvent e) {
    if(OpdrNavStructEdit.helpBrowser!=null)
      OpdrNavStructEdit.helpBrowser.loadURL(url);
  }

  @Override
  public void mouseReleased(MouseEvent e) {
  }

  @Override
  public void mouseEntered(MouseEvent e) {
  }

  @Override
  public void mouseExited(MouseEvent e) {
  }   
}