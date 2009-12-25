package fi.doorziendwo;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

public class UitlegButton extends JButton implements ActionListener
{	
	private Image image;
	private String titel;
	private UitlegFrame uitlegFrame;
	private Color frameBackgroundColor;
	
	public UitlegButton (String titel,Image image) 
	{	super(titel);
		this.titel = titel;
		this.image = image;
		addActionListener(this);
	}
	
	public void actionPerformed(ActionEvent e)
	{	if(uitlegFrame==null)
		{	uitlegFrame = new UitlegFrame(titel,image);
			uitlegFrame.setSize(image.getWidth(null)+23,400);
			uitlegFrame.setBackground(frameBackgroundColor);
		}
		uitlegFrame.setVisible(true);
	} 
	
	public void setFrameBackground(Color c)
	{	frameBackgroundColor = c;
	}
	
	class UitlegFrame extends Frame
	{	
		private Image image;
		private String titel;
		
		private UitlegPanel uitlegPanel;
	
		public UitlegFrame(String titel, Image image)
		{	this.titel = titel;
			this.image = image;
			setTitle(titel);
			
			addWindowListener(new WL());
			addComponentListener(new CL());
			
			uitlegPanel = new UitlegPanel(true);
			uitlegPanel.setBackground(frameBackgroundColor);
			add(uitlegPanel);
		
			setSize(image.getWidth(null)+23,image.getHeight(null)+23);
		}
		
				
		class UitlegPanel extends Panel implements AdjustmentListener
		{	
			private Image im;
			private Graphics gIm;
			public boolean resized = true;
			
		
			private ScrollPane scrollPane;
			private boolean scrollbar;
			private BufferedPanel contentPane; 
			private Panel contentPaneNep; //truc om een buffered scrollpane te krijgen.
			private Panel p1,p2,p3,p4;
			
			private int startY;
			
			public UitlegPanel(boolean scrollbar)
			{	setLayout(null);
				//setBackground(Color.lightGray);
				
				addMouseListener(new ML());
				addMouseMotionListener(new MML());
				
				this.scrollbar = scrollbar;
				if(scrollbar)scrollPane = new ScrollPane(ScrollPane.SCROLLBARS_AS_NEEDED);
				else scrollPane = new ScrollPane(ScrollPane.SCROLLBARS_NEVER);
				scrollPane.setBackground(Color.white);
				add(scrollPane);
				scrollPane.getVAdjustable().addAdjustmentListener(this);
				
				contentPane = new BufferedPanel();
				contentPane.setLayout(null);
				contentPane.setBackground(Color.white);
				add(contentPane);
				
				contentPaneNep = new Panel();
				contentPaneNep.setLayout(null);
				scrollPane.add(contentPaneNep);
				
				
				
				p4 = new Panel();
				p4.setBackground(getBackground());
				super.add(p4,0);
				
				p3 = new Panel();
				p3.setBackground(Color.gray);
				super.add(p3,0);
				
				contentPane.setBounds(0,0,image.getWidth(null),image.getHeight(null));
				contentPaneNep.setBounds(0,0,image.getWidth(null),image.getHeight(null));
				contentPane.add(new ImageComponent(image));
				scrollPane.doLayout();
			}
			
			/*public void setBounds(int x, int y, int b, int h)
			{	//resized = false;
				if(scrollbar)scrollPane.setBounds(b-20,0,20,h+18);
				else scrollPane.setBounds(b,0,20,h);
				p3.setBounds(0,0,b,2);
				p4.setBounds(b-20,0,2,h);
				super.setBounds(x,y,b,h);
			}*/
			
			public void setBounds(int x, int y, int b, int h)
			{	if(scrollbar)scrollPane.setBounds(b-21,0,21,h+18);
				else scrollPane.setBounds(b,23,20,h-5);
				//contentPane.setBounds(10,25,b-20,h-37);
				//contentPaneNep.setBounds(0,2,0,h-7);
				//p1.setBounds(b-31,h-12,21,12);
				//p2.setBounds(b-31,h-2,21,2);
				p3.setBounds(0,0,b,2);
				p4.setBounds(b-21,0,2,h);
				super.setBounds(x,y,b,h);
			}
			
			
			public void adjustmentValueChanged(AdjustmentEvent e)
			{	int h = e.getValue();
				contentPane.setLocation(0,-h);
				repaint();
			}	
			
			public void paint(Graphics g)
			{	{ 	if(im==null)
					{	im = createImage(image.getWidth(null),image.getHeight(null));
		  				gIm = im.getGraphics();
		  				resized = false;
					}
					gIm.setColor(getBackground());
					gIm.fillRect(0,0,getSize().width,getSize().height);
					super.paint(gIm);
					g.drawImage(im, 0, 0, null);
		  		}
			}
			
			public void update(Graphics g)
			{	paint(g);
			}
			
			class ML extends MouseAdapter
			{	public void mousePressed(MouseEvent e)
				{	setCursor(new Cursor(Cursor.HAND_CURSOR));
					startY = e.getY();
				}
				public void mouseReleased(MouseEvent e)
				{	setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
				}
			}
			
			class MML extends MouseMotionAdapter
			{	public void mouseDragged(MouseEvent e)
				{	int dy = e.getY() - startY;
					int y = contentPane.getLocation().y + dy;
					if(y<getSize().height-image.getHeight(null))y=getSize().height-image.getHeight(null);
					if(y>0)y=0;
					contentPane.setLocation(0,y);
					startY = e.getY();
					scrollPane.setScrollPosition(0,-y);
					repaint();
				}
			}
		
		}
		
		
		class WL extends WindowAdapter
		{   public void windowClosing(WindowEvent e)
			{   hide();
			}
			
		}
		
		class CL extends ComponentAdapter
		{   public void componentResized(ComponentEvent e)
			{   //uitlegPanel.resized = true;
			}
		}
		
	}
}
