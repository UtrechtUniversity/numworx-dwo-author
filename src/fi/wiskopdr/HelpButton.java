package fi.wiskopdr;

import java.awt.AWTEventMulticaster;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Window;
import java.awt.event.*;

import javax.swing.*;

import fi.wiskopdr.opdrnav.OpdrNavStructEdit;


public class HelpButton extends JButton implements MouseListener, ActionListener
{
	private String url;
	private FontMetrics fm;
	private String text = "?";
	private boolean withFrame = false;
	
	static JDialog frame;
	private JPanel helpPanel;
	private JButton hideHelpButton;
	
	
	public HelpButton(String url, boolean withFrame){
		this(url);
		this.withFrame = withFrame;
		
		
		
	}
	
	public HelpButton(String url){	
		super("?");
		this.url = url;
		this.setUI(null);
		this.setBorder(BorderFactory.createEmptyBorder(1, 0, 1, 0));
		addMouseListener(this);
		setFont(new Font("SansSerif",Font.BOLD,16));
		setHorizontalAlignment(JLabel.CENTER);
	    setVerticalAlignment(JLabel.CENTER);
	    setForeground(new Color(50,72,111));
	}
	
	private void makeFrame() {
		helpPanel = new JPanel(new BorderLayout());
		helpPanel.setBackground(WiskOpdr.colorBlue5);
		helpPanel.setPreferredSize(new Dimension(400,400));
		
		JPanel helpHeader = new JPanel(new BorderLayout());
		helpHeader.setBackground(WiskOpdr.colorBlue1);
		helpHeader.setPreferredSize(new Dimension(400,50));
		helpPanel.add(helpHeader,BorderLayout.NORTH);
		
		JLabel helpLabel = new JLabel("HELP");
		helpLabel.setForeground(WiskOpdr.colorGray3);
		helpLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		helpLabel.setFont(new Font("SansSerif",Font.PLAIN, 24));
		
		hideHelpButton = new WiskOpdrButton("\u2715");
		hideHelpButton.setBorder(BorderFactory.createLineBorder(WiskOpdr.colorBlue1));
		hideHelpButton.setBackground(WiskOpdr.colorBlue1);
		hideHelpButton.setForeground(WiskOpdr.colorBlue5);
		hideHelpButton.setPreferredSize(new Dimension(20,20));
		hideHelpButton.setFont(new Font("SansSerif",Font.PLAIN, 24));
		hideHelpButton.addActionListener(this);
		
		Box hb = Box.createHorizontalBox();
		hb.add(Box.createRigidArea(new Dimension(30,0)));
		hb.add(Box.createHorizontalGlue());
		hb.add(helpLabel);
		hb.add(Box.createHorizontalGlue());
		hb.add(hideHelpButton);
		hb.add(Box.createRigidArea(new Dimension(10,0)));
		helpHeader.add(hb);
		
		
    		Window w = (Window)(WiskOpdr.getWindowForComponent(this));
		frame = new JDialog(w,Dialog.ModalityType.MODELESS);
		//frame.setDefaultCloseOperation(Operation.HIDE_ON_CLOSE);
	}
	
	public String getURL() {
		return url;
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
	  if(withFrame) {
		  if(frame==null)
			  makeFrame();
		  
		  JComponent bp = OpdrNavStructEdit.helpBrowser.getBrowserPanel();
		  bp.setPreferredSize(new Dimension(400,400));
		  helpPanel.add(bp);
	    		
		  Component src = WiskOpdr.getWindowForComponent(this);
		  Dimension preferredSize = new Dimension(400,src.getHeight());
		  frame.setPreferredSize(preferredSize);
		  frame.getContentPane().setLayout(new BorderLayout());
		  frame.getContentPane().add(helpPanel);
		  
		  int x = src.getLocationOnScreen().x + src.getWidth() - 400;
		  int y = src.getLocationOnScreen().y;
		  frame.setLocation(x,y); 
		  
		  frame.pack();
		  frame.setVisible(true);
		  OpdrNavStructEdit.helpBrowser.loadURL(url);
	  }
		
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

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==hideHelpButton) {
			frame.setVisible(false);
		}
		
	}
}