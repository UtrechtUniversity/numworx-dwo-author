package fi.beans.wnwidgets;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

public class PopupPanel extends JPanel implements MouseListener, MouseMotionListener
{	
	Image image;
	JTextArea tekstArea;
	boolean closable = true;
	
	
	public PopupPanel(Image image, int x, int y)
	{
		//super(null);
		setLayout(null);
		setOpaque(false);
		this.image = image;
		if(image!=null)
		{	setBounds(x,y,image.getWidth(null), image.getHeight(null));
			tekstArea = new JTextArea();
			tekstArea.setWrapStyleWord(true);
			tekstArea.setBounds(60,67,getWidth()-120-10,60);
			//tekstArea.setBackground(Color.RED);
			//tekstArea.setOpaque(true);
			tekstArea.setFont(new Font("Verdana",Font.PLAIN,13)); // Let op tikfout!
			add(tekstArea);
			tekstArea.setEditable(false);
		}
		closeRect = new Rectangle(getWidth()-45, 10, 22, 32);
		addMouseListener(this);
		addMouseMotionListener(this);
	}
	
	public void setClosable(boolean b)
	{	closable = b;
		
	}
	public void setText(String s)
	{
		tekstArea.setText(s);
	}
	/*
	 * no repaint!
	 */
	public void setImage(Image image)
	{
		this.image = image;
	}
	
	//public void paint(Graphics g)
	//{		
	//	paintBackground(g);
	//	super.paint(g);
	//}

	/**
	 * @param g
	 */
	protected void paintBackground(Graphics g) {
		g.drawImage(image, 0,0, this);
	}
	
	public void mouseMoved( MouseEvent me ) 
	{ 
		setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
	}
	public void mouseDragged( MouseEvent me ) {
		if(last != null)
		{		
			Point hiero = getLocation();
			int dx =  me.getX()-last.x;
			int dy =  me.getY()-last.y;
			hiero.x += dx;
			hiero.y += dy;
			Point prel = me.getPoint();
			prel.translate(getX(), getY());
			if(getParent().getBounds().contains(prel))
			{	setLocation(hiero);
			}
		}
	}
	
	
	public void mouseReleased( MouseEvent me ) {
		mouseDragged(me);
		last = null;
	}
	public void mouseClicked( MouseEvent me ) { }
	public void mouseEntered( MouseEvent me ) { }
	public void mouseExited( MouseEvent me )
	{
		//last = null;
	}

	protected Rectangle closeRect;
	
	private Point last;
	public void mousePressed( MouseEvent me )
	{ 
//System.out.println("me= " + me.getX() + "cl= " + closeRect.x + " end = " + getWidth());
//closable=false;
//System.out.println(closeRect.contains(me.getPoint()));
		if(closable 
				&& closeRect.contains(me.getPoint()))
		{	setVisible(false);
			produceAction("close");
			last = null;
		} else {
			last = me.getPoint();
		}
	}   
	
	//ActionProducer
	private ActionListener actionListener = null;
	
	public void addActionListener(ActionListener l) 
 	{	actionListener = AWTEventMulticaster.add(actionListener,l);
 	}
 	
 	public void removeActionListener(ActionListener l)
 	{	actionListener = AWTEventMulticaster.remove(actionListener, l);
 	}	
 	
 	public void produceAction(String command)
 	{	if (actionListener != null)
 		{	actionListener.actionPerformed( new ActionEvent(this, 0, command) );
 		}
 	}
 	//

}
