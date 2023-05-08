package fi.javalogoweb.formuleobjects;


import java.awt.*;
import java.awt.event.*;
//import fi.wiskopdr.WiskOpdr;

public class Tablet extends Panel implements MouseListener, MouseMotionListener, ActionListener
{	
	private int aantalX = 7;
	private int aantalY = 5;
	private int eenheid = 20;
	
	private int startx;
	private int starty;
	
	private FormuleEditor formuleEditor;
	private Button closeButton;
	
	private boolean shift = false;
	
	//private FormuleButton[][] buttons;
	private String[][] buttonCodes = 
	{	
		{"a","x","t","wortel","macht","kwadraat","haakjes","breuk","ndewortel"},
		{"b","(",")","1","2","3","/","back"},
		{"p","e","pi","4","5","6","maal","del"},
		{"q","<",">","7","8","9","min","enter"},
		//{"txt",WiskOpdr.rb.getString("ofLabel"),"\u2248","0",".","=","plus","space"}
			
	};
	
	private double[][] buttonWidths = 
	{	
		{1  ,1  ,1  ,1  ,1  ,1  ,1  ,1  ,1  },
		{1  ,1  ,1  ,1  ,1  ,1  ,1  ,2      },
		{1  ,1  ,1  ,1  ,1  ,1  ,1  ,2      },
		{1  ,1  ,1  ,1  ,1  ,1  ,1  ,2      },
		{1  ,1  ,1  ,1  ,1  ,1  ,1  ,2      },
			
	};
	
	private String[][] buttonCodesAbc = 
	{	
			{"@"  	,"q"  	,"w"  ,"e"  ,"r"  ,"t"  ,"y"  ,"u"  	,"i"  ,"o"  ,"p"  ,"back"  	},
			{"tab"  ,"a"  	,"s"  ,"d"  ,"f"  ,"g"  ,"h"   ,"j"  	,"k"  ,"l"  ,"enter"       	},
			{"shift"	,"z"  ,"x"  ,"c"  ,"v"  ,"b"  ,"n"  ,"m"  	,";"  ,"'"  ,"del"      	},
			{"123"  ,"{"  ,"}"  ,"\\" ," "  					,","  ,"."  ,"/"  ,"%"      }
			
			
	};
	
	private double[][] buttonWidthsAbc = 
	{	
		{1     ,1  ,1  ,1  ,1  ,1  ,1  ,1  ,1  ,1  ,1  ,1 },
		{1.34  ,1  ,1  ,1  ,1  ,1  ,1  ,1  ,1  ,1  ,1.7   },
		{1.67     ,1  ,1  ,1  ,1  ,1  ,1  ,1  ,1  ,1  ,1.4      },
		{1     ,1  ,1  ,1  ,4  ,1  ,1  ,1  ,1     },
		
			
	};
	
	private String[][] buttonCodesAbcShift = 
	{	
			{"@"  	,"Q"  	,"W"  ,"E"  ,"R"  ,"T"  ,"Y"  ,"U"  	,"I"  ,"O"  ,"P"  ,"back"  	},
			{"tab"  ,"A"  	,"S"  ,"D"  ,"F"  ,"G"  ,"H"   ,"J"  	,"K"  ,"L"  ,"enter"       	},
			{"shift"	,"Z"  ,"X"  ,"C"  ,"V"  ,"B"  ,"N"  ,"M"  	,":"  ,"\""  ,"del"      	},
			{"123"  ,"{"  ,"}"  ,"|" ," "  					,"<"  ,">"  ,"?"  ,"^"      }
			
			
	};
	
	private double[][] buttonWidthsAbcShift = 
	{	
		{1     ,1  ,1  ,1  ,1  ,1  ,1  ,1  ,1  ,1  ,1  ,1 },
		{1.34  ,1  ,1  ,1  ,1  ,1  ,1  ,1  ,1  ,1  ,1.7   },
		{1.67     ,1  ,1  ,1  ,1  ,1  ,1  ,1  ,1  ,1  ,1.4      },
		{1     ,1  ,1  ,1  ,4  ,1  ,1  ,1  ,1     },
		
			
	};
	
	private String[][] buttonCodesMobile = 
	{	
		
		{"(",")","x","t","1","2","3","/","back"},
		{"e","pi","wortel","macht","4","5","6","maal","del"},
		{"<",">","kwadraat","haakjes","7","8","9","min","enter"},
		//{"txt",WiskOpdr.rb.getString("ofLabel"),"breuk","ndewortel","0",".","=","plus","space"}
			
	};
	
	private double[][] buttonWidthsMobile = 
	{	
		
		{1  ,1  ,1  ,1  ,1  ,1  ,1  ,1  ,2.15      },
		{1  ,1  ,1  ,1  ,1  ,1  ,1  ,1  ,2.15      },
		{1  ,1  ,1  ,1  ,1  ,1  ,1  ,1  ,2.15      },
		{1  ,1  ,1  ,1  ,1  ,1  ,1  ,1  ,2.15      },
			
	};
	
	Color bgColor = new Color(210,210,210);
	
	public Tablet(FormuleEditor formuleEditor)
	{	setLayout(null);
		addMouseListener(this);
		addMouseMotionListener(this);
		
		this.formuleEditor = formuleEditor;
		
		closeButton = new Button("X");
		closeButton.addActionListener(this);
		
		//if(WiskOpdr.mobileVersion)
		{	eenheid = 20;
			maakTabletMobile();
		}
		//else 
			maakTablet();
	}
	
	public void zetFormuleEditor(FormuleEditor formuleEditor)
	{
		this.formuleEditor = formuleEditor;
	}
	
	public void cleanTablet()
	{	removeAll();
		//if(!WiskOpdr.mobileVersion) add(closeButton);
	}
	
	private void maakTablet()
	{	cleanTablet();
		
		int buttonX = 5;
		int buttonY = 20;
		int inset = 2;
		for(int i=0 ; i<buttonCodes.length ; i++)
		{	if(i==0) buttonY = 20;
			for(int j=0 ; j<buttonCodes[i].length ; j++)
			{ 	FormuleButton fb = new FormuleButton(buttonCodes[i][j]);
				fb.addActionListener(this);
				fb.setFocusable(false);
				if(j==0) buttonX = 5;
				int buttonWidth = (int)(buttonWidths[i][j]*eenheid + inset*(buttonWidths[i][j]-1));
				fb.setBounds(buttonX, buttonY, buttonWidth, eenheid);
				String code = buttonCodes[i][j];
				if(Character.isDigit(code.charAt(0)) || code.equals("plus") || code.equals("min") || code.equals("maal") || code.equals(" /") || code.equals(".") || code.equals("="))
				{	fb.bgColor = new Color(230,230,230);
				}
				if(code.equals("x") || code.equals("t")) fb.setFont(new Font("TimesRoman", Font.ITALIC + Font.BOLD, 16));
				if(code.equals("a") || code.equals("b") || code.equals("p") || code.equals("q")) fb.setFont(new Font("TimesRoman", Font.ITALIC, 16));
				buttonX += buttonWidth + inset;
				add(fb);
			}
			buttonY = buttonY + eenheid + inset;
		}
		setSize(buttonX+inset, buttonY+inset);
		closeButton.setBounds(getSize().width-15, 3, 12,12);
	}
	
	private void maakTabletMobile()
	{	cleanTablet();
		eenheid = 20;
		int buttonX = 5;
		int buttonY = 2;
		int inset = 2;
		for(int i=0 ; i<buttonCodesMobile.length ; i++)
		{	if(i==0) buttonY = 2;
			for(int j=0 ; j<buttonCodesMobile[i].length ; j++)
			{ 	FormuleButton fb = new FormuleButton(buttonCodesMobile[i][j]);
				//fb.setFont(new Font("SansSerif", Font.PLAIN, 10));
				fb.addActionListener(this);
				fb.setFocusable(false);
				if(j==0) buttonX = 5;
				int buttonWidth = (int)(buttonWidthsMobile[i][j]*eenheid + inset*(buttonWidthsMobile[i][j]-1));
				fb.setBounds(buttonX, buttonY, buttonWidth, eenheid);
				String code = buttonCodesMobile[i][j];
				if(Character.isDigit(code.charAt(0)) || code.equals("plus") || code.equals("min") || code.equals("maal") || code.equals(" /") || code.equals(".") || code.equals("="))
				{	fb.bgColor = new Color(230,230,230);
				}
				//if(code.equals("x") || code.equals("t")) fb.setFont(new Font("TimesRoman", Font.ITALIC + Font.BOLD, 16));
				buttonX += buttonWidth + inset;
				add(fb);
			}
			buttonY = buttonY + eenheid + inset;
		}
		setSize(buttonX+inset, buttonY+inset);
		closeButton.setBounds(getSize().width-15, 3, 12,12);
	}
	
	private void maakTabletAbc()
	{	cleanTablet();
		int startButtonX = 5;
		int startButtonY = 20;
		//if(WiskOpdr.mobileVersion)
		{	startButtonY = 2;
			startButtonX = 7;
			eenheid = 16;
		}
		
		int buttonX = startButtonX;
		int buttonY = startButtonX;
		int inset = 2;
		for(int i=0 ; i<buttonCodesAbc.length ; i++)
		{	if(i==0) buttonY = startButtonY;
			for(int j=0 ; j<buttonCodesAbc[i].length ; j++)
			{ 	FormuleButton fb = new FormuleButton(buttonCodesAbc[i][j]);
				//if(WiskOpdr.mobileVersion) fb.setFont(new Font("SansSerif", Font.PLAIN, 12));
				fb.addActionListener(this);
				fb.setFocusable(false);
				if(j==0) buttonX = startButtonX;
				int buttonWidth = (int)(buttonWidthsAbc[i][j]*eenheid + inset*(buttonWidthsAbc[i][j]-1));
				fb.setBounds(buttonX, buttonY, buttonWidth, eenheid);
				buttonX += buttonWidth + inset;
				add(fb);
			}
			buttonY = buttonY + eenheid + inset;
		}
		setSize(buttonX+startButtonX, buttonY+2*inset);
		closeButton.setBounds(getSize().width-15, 3, 12,12);
	}
	
	private void maakTabletAbcShift()
	{	cleanTablet();
	
		int buttonX = 5;
		int buttonY = 20;
		int inset = 2;
		for(int i=0 ; i<buttonCodesAbcShift.length ; i++)
		{	if(i==0) buttonY = 20;
			for(int j=0 ; j<buttonCodesAbcShift[i].length ; j++)
			{ 	FormuleButton fb = new FormuleButton(buttonCodesAbcShift[i][j]);
				fb.addActionListener(this);
				fb.setFocusable(false);
				if(j==0) buttonX = 5;
				int buttonWidth = (int)(buttonWidthsAbcShift[i][j]*eenheid + inset*(buttonWidthsAbcShift[i][j]-1));
				fb.setBounds(buttonX, buttonY, buttonWidth, eenheid);
				buttonX += buttonWidth + inset;
				add(fb);
			}
			buttonY = buttonY + eenheid + inset;
		}
		setSize(buttonX+2*inset, buttonY+2*inset);
		closeButton.setBounds(getSize().width-15, 3, 12,12);
	}
	
	public void paint(Graphics g)
	{	g.setColor(bgColor);
		g.fillRect(0,0,getSize().width,getSize().height);
		//if(WiskOpdr.mobileVersion)
		{	g.setColor(Color.white);
			//g.drawLine(1,1,getSize().width-1,1);
			g.drawLine(1,1,1,getSize().height-1);
			g.setColor(Color.gray.darker());
			g.drawLine(1,getSize().height-2,getSize().width-2,getSize().height-2);
			g.drawLine(0,getSize().height-1,getSize().width-1,getSize().height-1);
			g.drawLine(getSize().width-1,0,getSize().width-1,getSize().height-1);
			g.drawLine(getSize().width-2,1,getSize().width-2,getSize().height-2);
		}
		//else
		{
			g.setColor(new Color(0,0,150));
			g.fillRect(3,3,getSize().width-6,12);
			g.setColor(bgColor.brighter());
			g.drawLine(0,0,getSize().width-1,0);
			g.drawLine(0,0,0,getSize().height-1);
			g.setColor(bgColor.darker());
			g.drawLine(getSize().width-1,0,getSize().width-1,getSize().height-1);
			g.drawLine(0,getSize().height-1,getSize().width-1,getSize().height-1);
		}
		super.paint(g);
	}
	
	public void actionPerformed(ActionEvent e)
	{	if(e.getSource()==closeButton) 
		{	((TabletOwner)getParent()).removeTablet();
			return;
		}
		
		formuleEditor.requestFocus();
		String code = ((FormuleButton)e.getSource()).getCode();
		
		if(code.equals("txt")) 
		{	maakTabletAbc();
			return;
		}
		//if(code.equals("123") && WiskOpdr.mobileVersion) 
		//{	maakTabletMobile();
		//	return;
		//}
		if(code.equals("123")) 
		{	maakTablet();
			return;
		}
		if(code.equals("shift") && !shift) 
		{	maakTabletAbcShift();
			shift = true;
			return;
		}
		if(code.equals("shift") && shift) 
		{	maakTabletAbc();
			shift = false;
			return;
		}
		
		if(!formuleEditor.formuleVak.isEditable()) return;
		else if(code.equals("wortel")) formuleEditor.formuleVak.zetWortelVak();
		else if (code.equals("ndewortel")) formuleEditor.formuleVak.zetNdeWortelVak();
		else if (code.equals("breuk")) formuleEditor.formuleVak.zetBreukVak();
		else if (code.equals("haakjes")) formuleEditor.formuleVak.zetHaakjesVak();
		else if (code.equals("macht")) formuleEditor.formuleVak.zetMachtVak();
		else if (code.equals("kwadraat")) formuleEditor.formuleVak.zetKwadraatVak();
				
		else if (code.equals("del"))formuleEditor.formuleVak.delete();
		else if (code.equals("back"))formuleEditor.formuleVak.backspace();
		else if (code.equals("space"))formuleEditor.formuleVak.insert(" ");
		else if (code.equals("tab"))formuleEditor.formuleVak.insert("    ");
		else if (code.equals("enter"))formuleEditor.formuleVak.finish();
		
		else if (code.equals("plus"))formuleEditor.formuleVak.insert("+");
		else if (code.equals("min"))formuleEditor.formuleVak.insert("-");
		else if (code.equals("maal"))formuleEditor.formuleVak.insert("*");
		//else if (code.equals(WiskOpdr.rb.getString("ofLabel")))formuleEditor.formuleVak.insert(" "+WiskOpdr.rb.getString("ofLabel")+" ");
		
		else formuleEditor.formuleVak.insert(code);
		
		
	}
	
	public void mousePressed(MouseEvent e)
	{	startx = e.getX();
		starty = e.getY();
		
	}
	
	public void mouseDragged(MouseEvent e)
	{	//if(WiskOpdr.mobileVersion)return;
		int dx = e.getX() - startx;
		int dy =  e.getY() - starty;
		int x = getLocation().x + dx;
		int y = getLocation().y + dy;
		
		int b = getSize().width;
		int h = getSize().height;
		int bp = getParent().getSize().width;
		int hp = getParent().getSize().height;
		if(x < 0)x = 0;
		if(x > bp-b)x = bp-b;
		if(y < 0)y = 0;
		if(y > hp-h)y = hp-h;
		
		setLocation(x,y);
	}
	
	public void mouseReleased(MouseEvent e)
	{	
	}
	public void mouseMoved(MouseEvent e){;}
	public void mouseExited(MouseEvent e){;}
	public void mouseClicked(MouseEvent e){;}
	public void mouseEntered(MouseEvent e){;}
	

}
