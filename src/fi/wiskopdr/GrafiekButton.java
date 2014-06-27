package fi.wiskopdr;

import java.awt.*;
import java.util.*;
import java.awt.event.*;
import javax.swing.*;

import fi.wiskopdr.formuleobjects.*;
import fi.beans.wiskopdrbeans.*;

public class GrafiekButton extends JButton implements ActionListener, InteractiePanel, InteractieEditPanel, WiskOpdrApplet
{	
	private Image image;
	private String titel;
	private GrafiekFrame grafiekFrame;
	private Color frameBackgroundColor = new Color(230,240,255);
	private Hashtable state, editState;
	private FunctieEditor fe;
	private String[] randomVars;
	private Hashtable randomValues;
	private boolean edit = false;
	private boolean formulesZichtbaar = true;
	private String varNaam  = "x";
	
	private GrafiekComponent grafiekComponent;
	
	
	public GrafiekButton () 
	{	super("Grafiekentool");
		this.titel = "Grafiekentool";
		addActionListener(this);
	}
	
	public GrafiekButton (Locale language) 
	{	super("Grafiekentool");
		this.titel = "Grafiekentool";
		addActionListener(this);
	}
	
	public GrafiekButton (String titel) 
	{	super(titel);
		this.titel = titel;
		addActionListener(this);
	}
	
	public void zetBreedte(int b)
	{	//grafiekPanel.setSize(b,grafiekPanel.getSize().height);
	}
	public void zetHoogte(int h)
	{	//grafiekPanel.setSize(grafiekPanel.getSize().width, h);
	}
	
	public void zetEditmode(boolean b)
	{	edit = b;

	}
	
	public void zetOpdracht(Hashtable h, String[] randomVars, Hashtable randomValues)
	{	state = null;
		editState = h;
		
		if(editState!=null && editState.containsKey("formulesZichtbaar")) 
		{	formulesZichtbaar = ((Boolean)editState.get("formulesZichtbaar")).booleanValue();
			
		}
		else formulesZichtbaar = true;
		
		if(editState!=null && editState.containsKey("varNaam")) 
		{	varNaam = (String)editState.get("varNaam");
			
		}
		else varNaam = "x";
		
		
		this.randomVars = randomVars;
		this.randomValues = randomValues;
	}
	
	public void setEditState(Hashtable h)
	{	editState = h;
		if(editState!=null && editState.containsKey("formulesZichtbaar")) 
		{	formulesZichtbaar = ((Boolean)editState.get("formulesZichtbaar")).booleanValue();
		}
		else formulesZichtbaar = true;
		
		if(editState!=null && editState.containsKey("varNaam")) 
		{	varNaam = (String)editState.get("varNaam");
			
		}
		else varNaam = "x";
	}
	
	public void setState(Hashtable h)
	{	state = h;
	}
	
	public Hashtable getState()
	{	return state;
	}
	
	public Hashtable getEditState()
	{	if(editState!=null)
		{	editState.put("formulesZichtbaar", new Boolean(formulesZichtbaar));
			editState.put("varNaam", new String(varNaam));
			return editState;
		}
		else return new Hashtable();
	}
	
	public void actionPerformed(ActionEvent e)
	{	if(grafiekFrame==null)
		{	grafiekFrame = new GrafiekFrame(titel,image);
			grafiekFrame.getContentPane().setBackground(frameBackgroundColor);
			grafiekFrame.zetEditmode(edit);
		}
		grafiekFrame.setVisible(true);
		if(formulesZichtbaar)grafiekFrame.setSize(300,500);
		else grafiekFrame.setSize(300,350);
	} 
	public void closeFrame()
	{	if(grafiekFrame!=null)
		{	if(edit) editState = fe.getEditState();
			else state = fe.getState();
			grafiekFrame.setVisible(false);
			grafiekFrame.dispose();
			grafiekFrame=null;
			fe = null;
		}
	}
	
	public void setFrameBackground(Color c)
	{	frameBackgroundColor = c;
	}
	
	public InteractiePanel getInteractiePanel()
	{
		return this;
	}
	
	public InteractieEditPanel getEditPanel()
	{	zetEditmode(true);
		return this;
	}
	
	public void wis(){}
	
	public void zetMaat(){}
	
	public int geefAsHoogte()
	{	return 0;
	}
	
	public int getIpId(){return 0;}
	
	public String getIpExpString(){return null;}
	
	public int getScore()
	{	return 0;
	}
	
	public int[][] getScoreObjectives()
	{	return null;
	}
	
	public int getScoreMax()
	{	return 0;
	}
	
	public boolean isCorrect()
	{	return true;
	}
	
	public boolean isFout()
	{	return false;
	}
	
	public void zetMode(int mode){}
	
	public void zetNagekeken(boolean b){}
	
    public void stop()
    {  	if(grafiekFrame!=null)
		{	if(edit) editState = fe.getState();
			else state = fe.getState();
			grafiekFrame.setVisible(false);
			grafiekFrame.dispose();
			grafiekFrame=null;
			fe = null;
		}
    }
    
    public void start(){}
    
    public void destroy(){}
    
    public void opnieuw(){}
    
    public void kijkNa(){}
    
    public void kijkNa(int stapNr){}
    
    //public void addActionListener(ActionListener al){}
    
	
	
	class GrafiekFrame extends JDialog
	{	
		private Image image;
		private String titel;
		
		private GrafiekPanel grafiekPanel;
	
		public GrafiekFrame(String titel, Image image)
		{	this.titel = titel;
			this.image = image;
			setTitle(titel);
			
			
			addWindowListener(new WL());
			addComponentListener(new CL());
			
			grafiekPanel = new GrafiekPanel();
			grafiekPanel.setBackground(frameBackgroundColor);
			add(grafiekPanel);
			
			setSize(300,400);
			//setSize(image.getWidth(null)+23,image.getHeight(null)+23);
		}
		public void setFrameSize(int b, int h)
		{
			setSize(b,h);
		}
		
		
		
		public void zetEditmode(boolean b)
		{	grafiekPanel.zetEditmode(b);
		}
		
				
		class GrafiekPanel extends JPanel implements ItemListener, TabletOwner, ActionListener
		{	
			public boolean resized = true;
			
		
			private ScrollPane scrollPane;
			private EditorContentPanel contentPane; 
			
			private int startY;
			 
			private FunctieEditor functieEditor;
			private GrafiekTekenEditor grafiekTekenEditor;
			
			private JCheckBox formuleVisibleCheckbox;
			private Font font = WiskOpdr.tekstFont; //new Font("SansSerif", Font.PLAIN,12);
			private Tablet tablet;
			private boolean tabletAdded;
			private FormuleVakHouder tabletUser;
			
			private JTextField varField;
			
			public GrafiekPanel()
			{	setLayout(null);
			
				grafiekComponent = new GrafiekComponent(-10,0,270,270);
				grafiekComponent.setBackground(frameBackgroundColor);
				add(grafiekComponent);
								
				functieEditor = new FunctieEditor(true);
				functieEditor.setBounds(0,269,getSize().width, 150);
				functieEditor.zetRandverhoging(false);
				add(functieEditor,0);
				functieEditor.zetFuncties();
				functieEditor.zetGrafiekComponent(grafiekComponent);
				if(edit && editState!=null) functieEditor.setEditState(editState);
				else if(editState!=null)functieEditor.zetOpdracht(editState, randomVars, randomValues);
				if(state!=null)functieEditor.setState(state);
				functieEditor.requestFocus();
				fe = functieEditor;
				
				grafiekTekenEditor = new GrafiekTekenEditor();
				grafiekTekenEditor.setBounds(50,270,getSize().width-50, 24);
				//basisPanel.add(grafiekTekenEditor,0);
				grafiekTekenEditor.zetGrafiekComponent(grafiekComponent);
				grafiekTekenEditor.setVisible(false);
				grafiekComponent.zetGrafiekTekenEditor(grafiekTekenEditor);		
				
				formuleVisibleCheckbox = new JCheckBox("formules");
				formuleVisibleCheckbox.setFont(font);
				formuleVisibleCheckbox.setBounds(220,5,80,20);
				formuleVisibleCheckbox.setBackground(new Color(220,220,220));
				formuleVisibleCheckbox.addItemListener(this);
				formuleVisibleCheckbox.setSelected(formulesZichtbaar);
				grafiekComponent.add(formuleVisibleCheckbox,0);
				formuleVisibleCheckbox.setVisible(false);
				
				varField = new JTextField(varNaam);
				varField.setBounds(grafiekComponent.getSize().width-20,grafiekComponent.getSize().height-30,15,15);
				varField.setFont(new Font("Serif", Font.ITALIC,12));
				varField.addActionListener(this);
				grafiekComponent.add(varField,0);
				varField.setVisible(false);
			}
			
			public void zetEditmode(boolean b)
			{	formuleVisibleCheckbox.setVisible(b);
				varField.setVisible(b);
				formuleVisibleCheckbox.setSelected(formulesZichtbaar);
			}
			
			public void itemStateChanged(ItemEvent e)
			{	boolean b = formuleVisibleCheckbox.isSelected();
				functieEditor.setVisible(b);
				//int tabletSpace = 0;
				//if(tabletAdded) tabletSpace = 100;
				if(b)
				{	setFrameSize(300,500);
					grafiekComponent.setSize(getSize().width+15,getSize().height-150);
				}
				else 
				{	setFrameSize(300,350);
					grafiekComponent.setSize(getSize().width+15,getSize().height);
				}
				formulesZichtbaar = b;
			}
			
			public void resize()
			{	//doLayout();
				if(formulesZichtbaar)
				{	grafiekComponent.setSize(getSize().width+15,getSize().height-150);
					functieEditor.setBounds(0,getSize().height-151,getSize().width, 151);
				}
				else
				{	grafiekComponent.setSize(getSize().width+15,getSize().height);
					//functieEditor.setBounds(0,getSize().height-151,getSize().width, 151);
				}
				varField.setBounds(grafiekComponent.getSize().width-20,grafiekComponent.getSize().height-25,13,17);
				//grafiekComponent.add(varField,0);
			}
			
			/*public void paint(Graphics g)
			{	{ 	if(im==null)
					{	//im = createImage(image.getWidth(null),image.getHeight(null));
						im = createImage(getSize().width,getSize().height);
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
			}*/
			
			public void zetTabletUser(FormuleVakHouder formuleVakHouder)
			{	if(tablet==null) return;
				tablet.zetFormuleVakHouder(formuleVakHouder);
				tabletUser = formuleVakHouder;
				
			}
			
			public void zetTablet(FormuleVakHouder formuleVakHouder, int x, int y)
			{	if(tablet==null) 
				{	tablet = new Tablet(formuleVakHouder);
					tablet.setLocation(x,y);
					
				}
				tablet.zetFormuleVakHouder(formuleVakHouder);
				tabletUser = formuleVakHouder;
				
				
			}
			
			public void addTablet(FormuleVakHouder formuleVakHouder, int x, int y)
			{	if(tablet==null) 
				{	tablet = new Tablet(formuleVakHouder);
					
					
				}
				if(!tabletAdded)
				{	add(tablet,0);
					tablet.setLocation(x,y);
					tabletAdded = true;
					//resize();
		            repaint();
				}
				tablet.zetFormuleVakHouder(formuleVakHouder);
			}
			
			public void actionPerformed(ActionEvent e)
			{	varNaam = varField.getText();
				functieEditor.zetVarNaam(varNaam);
			}
			
			public void removeTablet()
			{	if(tablet==null)return;
		        remove(tablet);
		        //resize();
		        repaint();
				tabletAdded = false;
			}
			
			public Tablet getTablet()
			{	return tablet;
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
			{   closeFrame();
			}
			
		}
		
		class CL extends ComponentAdapter
		{   public void componentResized(ComponentEvent e)
			{   
				grafiekPanel.resize();
			}
		}
		
	}
}
