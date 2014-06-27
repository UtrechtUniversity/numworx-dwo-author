package fi.wiskopdr;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


public class TimerPanel extends JPanel implements  ActionListener, MouseListener, FocusListener
{	
	private Image im;
	private Graphics gIm;
	
	private int tijdMax;
	private TijdDraad tijdDraad;
	private int tijd;
	
	private TextField textField;
	private boolean instelbaar;
	
	private JLabel restTijdLabel;
	
	public TimerPanel(int b, int h)
	{	setLayout(null);
		setSize(b,h);
		addMouseListener(this);
		
		tijdMax = 8000;
		
		textField = new TextField();
		textField.setBounds(2,h-22,b-4,20);
		textField.setFont(new Font("SansSerif",Font.PLAIN,14));
		textField.addActionListener(this);
		textField.addFocusListener(this);
		textField.setVisible(false);
		textField.setText(""+(tijdMax/1000));
		//add(textField);
		
		restTijdLabel = new JLabel(""+(tijdMax/1000)+" sec)");
		restTijdLabel.setBounds(5,h-15,b-5,15);
		restTijdLabel.setFont(new Font("SansSerif",Font.PLAIN,12));
		restTijdLabel.setAlignmentX(JLabel.RIGHT);
		add(restTijdLabel,0);
	
	}
	
	
	
	public void zetTijdMax(int sec)
	{	tijdMax = 1000*sec;
		restTijdLabel.setText(""+(tijdMax/1000)+" sec)");
	
	}
	
	public void zetInstelbaar(boolean b)
	{	instelbaar = b;
	}
	/*
	public void paint(Graphics g)
	{	{ 	if(im==null)
			{	im = createImage(getSize().width,getSize().height);
  				gIm = im.getGraphics();
			}
			gIm.setColor(getBackground());
			gIm.fillRect(0,0,getSize().width,getSize().height);
			paintBuffer(gIm);
			g.drawImage(im, 0, 0, null);
  		}
	}
	
	public void update(Graphics g)
	{	paint(g);
	}
	*/
	public void paintComponent(Graphics gr)
	{	Graphics2D g;
	    //if(WiskOpdr.deployVariant!=null && WiskOpdr.deployVariant.equals("GR"))
	    {     g = (Graphics2D)gr;
	          ((Graphics2D)g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	    }
	    //else 
		//g=gr;
		
		super.paintComponent(g);
		//g.setColor(new Color(220,220,220));
		//g.fillRect(0,0,getSize().width, getSize().height);
		//g.setColor(Color.black);
		//g.drawRect(0,0,getSize().width-1, getSize().height-1);
		g.setColor(new Color(255,150,150));
		g.fillOval(5,5,getSize().width-10,getSize().width-10);
		g.setColor(new Color(0,180,0));
		g.fillArc(5,5,getSize().width-10,getSize().width-10,90,360-360*tijd/tijdMax);
		g.setColor(Color.gray);
		g.drawOval(5,5,getSize().width-10,getSize().width-10);
		//g.drawString(""+tijdMax/1000+" sec",10,getSize().height-5);
		
	}
	
	public void start()
	{	if(tijdDraad!=null) 
		{	tijdDraad.maakDood();
			tijdDraad = null;
		}
		tijd = 0;
		tijdDraad = new TijdDraad();
		tijdDraad.start();
		restTijdLabel.setText(""+(tijdMax/1000)+" sec)");
	}
	
	
	public void stop()
	{	if(tijdDraad!=null)tijdDraad.maakDood();
		
	}
	
	public void reset()
	{	if(tijdDraad!=null)tijdDraad.maakDood();
		tijd = 0;
	}
	
	public void actionPerformed(ActionEvent e)
	{	
		try
		{	tijdMax = 1000*Integer.parseInt(textField.getText());
		}
    	catch(Exception ex)    // geen ;
		{   }
		
		textField.setVisible(false);
	}
	
	public void mousePressed(MouseEvent e)
	{	if(instelbaar)
		{	textField.setVisible(true);
			textField.requestFocus();
			textField.selectAll();
		}
	}
	
	public void mouseEntered(MouseEvent e)
	{	setCursor(new Cursor(Cursor.HAND_CURSOR ));
		//repaint();
	}
	
	public void mouseExited(MouseEvent e)
	{	setCursor(new Cursor(Cursor.DEFAULT_CURSOR ));
		//repaint();
	}
	
	public void mouseReleased(MouseEvent e){;}
	public void mouseClicked(MouseEvent e){;}
	
	public void focusLost(FocusEvent e)
	{	try
		{	tijdMax = 1000*Integer.parseInt(textField.getText());
		}
    	catch(Exception ex)    // geen ;
		{   }
		
		textField.setVisible(false);
	}
	public void focusGained(FocusEvent e){;	}
	
	
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
 	//end ActionProducer
 	
	class TijdDraad extends Thread 
	{	boolean dood = false;
		public void run()
		{	while(!dood && tijd<tijdMax)
			{	instelbaar = false;
				int delay = 1000;
				long t = System.currentTimeMillis();
				tijd += delay;
				if(tijd%1000==0)restTijdLabel.setText(""+(tijdMax-tijd)/1000 + " sec");
				repaint();
				try
				{	t = t+delay;
					sleep(Math.max(1, t-System.currentTimeMillis()));
				}
    			catch(InterruptedException e)    // geen ;
				{   };
				
				
				
				repaint();
				if(tijd >=tijdMax)produceAction("telaat");
				instelbaar = true;
			}
		}
		public void maakDood()
		{	dood = true;
		}
	}
}

