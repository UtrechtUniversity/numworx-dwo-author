package fi.wiskopdr;

import java.awt.AWTEventMulticaster;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.*;

import javax.swing.*;

import fi.wiskopdr.opdrnav.OpdrNavStructEdit;
import javafx.embed.swing.JFXPanel;

public class HelpButton extends JButton implements  MouseListener
{
	String url;
	FontMetrics fm;
	String text = "?";
	
	public HelpButton(String url){
		this(url,null);
	}
	
	public HelpButton(String url, Action action){	
		super("?");
		if(action!=null)
			setAction(action);
		super.setUI(null);
		this.url = url;
		this.setBorder(BorderFactory.createEmptyBorder(1, 0, 1, 0));
		addMouseListener(this);
		setFont(new Font("SansSerif",Font.BOLD,16));
		setHorizontalAlignment(JLabel.CENTER);
	    setVerticalAlignment(JLabel.CENTER);
	    setForeground(new Color(50,72,111));
	}
	
	public void paintComponent(Graphics gr) {
	  Graphics2D g = (Graphics2D)gr;
	  g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	  g.setColor(WiskOpdr.colorBlue5);
	  g.fillOval(1,1,getSize().width-2,getSize().height-2);
	  //super.paintComponent(g); 
	  
	  
	  g.setColor(new Color(50,72,111));
	  g.drawOval(1,1,getSize().width-2,getSize().height-2);
	  
	  fm = this.getFontMetrics(getFont());
      int stringwidth = fm.stringWidth(text);
      int stringheight = fm.getAscent()-fm.getDescent();
      g.setColor(new Color(50,72,111));
      //g.setFont(getFont());
      g.drawString(text,(getWidth()-stringwidth+1)/2 , getHeight()/2 + (stringheight)/2);
      
	}

  @Override
  public void mouseClicked(MouseEvent e) {
  }

  @Override
  public void mousePressed(MouseEvent e) {
    //if(OpdrNavStructEdit.helpBrowser!=null)
    //  OpdrNavStructEdit.helpBrowser;
	  produceAction("help");
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
  
//ActionProducer
  private ActionListener actionListener = null;
  
  public void addActionListener(ActionListener l) 
  {   actionListener = AWTEventMulticaster.add(actionListener,l);
  }
  
  public void removeActionListener(ActionListener l)
  {   actionListener = AWTEventMulticaster.remove(actionListener, l);
  }   
  
  public void produceAction(String command)
  {   if (actionListener != null)
      {   actionListener.actionPerformed( new ActionEvent(this, 0, command) );
      }
  }
  //end ActionProducer
}