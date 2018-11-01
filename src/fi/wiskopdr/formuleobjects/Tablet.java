package fi.wiskopdr.formuleobjects;


import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import fi.beans.wnwidgets.NWButtonUI;
import fi.wiskopdr.WiskOpdr;

public class Tablet extends JPanel implements MouseListener, MouseMotionListener, ActionListener
{	
	private int aantalX = 7;
	private int aantalY = 5;
	private int eenheid = 20;
	
	private int startx;
	private int starty;
	
	private FormuleVakHouder formuleVakHouder;
	private JButton closeButton;
	
	private Image backgroundImageMW;
	private Image backgroundImageGR;
	
	private boolean shift = false;
	
	//private FormuleButton[][] buttons;
	private String[][] buttonCodes = 
	{	
		{"wortel","macht","kwadraat","breuk","haakjes","ndewortel","integraal","prv","ndelog","abs","subscript","bin","vector", "vectornotatie"},
		{"diff","limiet0","limiet1","limiet2","\u221e","primitieve","conjug", "stelsel","sigma","\u3008","\u3009","diff_partial","matrix"}, 
		{"x","y","(",")","1","2","3","/","back"},
		{"a","b","k","e","pi","4","5","6","maal","del"},
		{"p","q","t","<",">","7","8","9","min","enter"},
		{"ab..", "\u03b1\u03b2..",WiskOpdr.rb.getString("ofLabel"),"\u2248","0",".","=","plus","space"}
			
	};
	
	private double[][] buttonWidths = 
	{
		{ 1, 1, 1, 1, 1, 1, 1, 1, 1.3, 1, 1, 1, 1, 1 },
		{ 1, 1, 1, 1, 1, 1, 1, 1, 1.3, 1.2, 1.2, 1.3, 1.4 },
		{ 2, 1, 1, 1, 1, 1, 1, 1, 5.3 },
		{ 1, 1, 1, 1, 1, 1, 1, 1, 1, 5.3 },
		{ 1, 1, 1, 1, 1, 1, 1, 1, 1, 5.3 },
		{ 1.5, 1.5, 1, 1, 1, 1, 1, 1, 5.3 },
			
	};
	
	private String[][] buttonCodesGR = 
	{	
		{"wortel","macht","kwadraat","breuk","haakjes","ndewortel","integraal","prv","ndelog","abs","subscript","bin"},
		{"diff","limiet0","limiet1","limiet2","\u221e","primitieve","e", "pi","<",">","\u2228","\u2248", "\u03b1\u03b2.."}
		
			
	};
	
	private double[][] buttonWidthsGR = 
	{	
		{1  ,1  ,1  ,1  ,1  ,1  ,1  ,1  ,1 ,1  ,1  ,1},
		{1	,1	,1	,1	,1	,1	,1	,1	,1 ,1	,1	,1,1.1}
		
			
	};
	
	private String[][] buttonCodesMW = 
	{	
		{"haakjes",  "breuk",  		"kwadraat", "macht",  	"wortel", 	"ndewortel", "ndelog"},
		{"diff", 	"primitieve",   "integraal","prv", 	    "abs",		"subscript",	"bin"},
		{"limiet0",	"limiet1",		"limiet2",	"\u221e",	"�",		"\u2248",	"\u2260"},
		{"<",		"\u2264",		"\u2265",	">",		"\u2227",	"\u2228",	"\u2205"},
		{"[",		"]",		"\u3008",	"\u3009",		"\u2190",	"\u2192",	"\u00b0"},
		{"pi",		"e",		"\u03b1",	"\u03b2",		"\u03b3",	"\u03bc",	"\u03c3"}
		
		//{"�",      	"\u2248",  		"\u2260", 	"<",  	  	"\u2264", 	"\u2265",  	">",  		"\u2227", 	"\u2228"},
		//{"\u2205", 	"pi",      		"e",  		"\u03b1", 	"\u03b2",	"\u03b3",	"\u03bc",	"\u03c3",	"\u2218"},
		//{"\u221e",	"haakjes",		"breuk",	"kwadraat",	"macht",	"wortel",	"ndewortel","ndelog" , 	"diff"},
		//{"primitieve","integraal",	"prv",		"abs",		"subscript","bin",		"limiet0",	"limiet1",	"limiet2"}
		
			
	};
	
	private double[][] buttonWidthsMW = 
	{	
		{1  ,1  ,1  ,1  ,1  ,1  ,1},
		{1  ,1  ,1  ,1  ,1  ,1  ,1},
		{1  ,1  ,1  ,1  ,1  ,1  ,1},
		{1  ,1  ,1  ,1  ,1  ,1  ,1},
		{1  ,1  ,1  ,1  ,1  ,1  ,1},
		{1  ,1  ,1  ,1  ,1  ,1  ,1}
		
			
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
	
	private String[][] buttonCodesAlpha = 
	{		{"\u2190", "\u2192","[","]","\u3008","\u3009","\u211d",  "\u03a6","\u03a7","\u03a8","\u03a9","\u0393","\u0394"},
			{"\u03b1"  	,"\u03b2"  	,"\u03b3"  ,"\u03b4"  ,"\u03b5"  ,"\u03b6"  ,"\u03b7"  ,"\u03b8"  ,"\u03b9"  ,"\u03ba"  ,"\u03bb"  ,"\u03bc"	,"\u03bd" 	},
			{"\u03be"  ,"\u03A3"  ,"\u03c0"  ,"\u03c1"  ,"\u03c2"   ,"\u03c3"  	,"\u03c4"  ,"\u03c5"  ,"\u03c6"	,"\u03c7","\u03c8"	, "\u03c9","\u221e","\u21d2"      	},
			{"\u2264"  ,"\u2265"  ,"\u00b1"  ,"\u2260"  ,"\u00f7"   ,"\u00d7"  	,"\u00b0"  ,"\u2030"  ,"\u2202"	,"\u2206","\u2220"	, "\u2227", "\u2228", "p"+('\u0302')     	},
			{"123"  ,"\u2200"  ,"\u2203"  ,"\u2204"  ,"\u2205"   ,"\u00ac"  	,"\u2229"  ,"\u222a"  ,"\u2208"	,	"\u2209",	"\u2282"	, "\u2283", "\u00a9", "x"+('\u0304') }//"\u2284"      	},
			
			
			
	};

	
	
	
	private double[][] buttonWidthsAlpha = 
	{	{1  ,1  ,1  ,1  ,1  ,1  ,1 	,1	,1	,1	,1	,1	,1 },
		{1  ,1  ,1  ,1  ,1  ,1  ,1  ,1  ,1  ,1  ,1  ,1	,1	,1 ,1 },
		{1  ,1  ,1  ,1  ,1  ,1  ,1  ,1  ,1  ,1  ,1	,1  ,1	,1 },
		{1  ,1  ,1  ,1  ,1  ,1  ,1  ,1  ,1  ,1  ,1	,1  ,1	,1,1 },
		{1  ,1  ,1  ,1  ,1  ,1  ,1  ,1  ,1  ,1  ,1	,1  ,1	,1 ,1 },
		
			
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
		{"txt",WiskOpdr.rb.getString("ofLabel"),"breuk","ndewortel","0",".","=","plus","space"}
			
	};
	
	private double[][] buttonWidthsMobile = 
	{	
		
		{1  ,1  ,1  ,1  ,1  ,1  ,1  ,1  ,2.15      },
		{1  ,1  ,1  ,1  ,1  ,1  ,1  ,1  ,2.15      },
		{1  ,1  ,1  ,1  ,1  ,1  ,1  ,1  ,2.15      },
		{1  ,1  ,1  ,1  ,1  ,1  ,1  ,1  ,2.15      },
			
	};
	
	Color bgColor = Color.white;//	new Color(210,210,210);
	
	public Tablet(FormuleVakHouder formuleVakHouder)
	{	setLayout(null);
		addMouseListener(this);
		addMouseMotionListener(this);
        setVisible(true);
        
        setOpaque(false);
        
        this.formuleVakHouder = formuleVakHouder;
		
		closeButton = new JButton("X");
		closeButton.setMargin(new Insets(0,0,0,0));
		closeButton.setFont(WiskOpdr.tekstFont);
		closeButton.addActionListener(this);
		
		if("MW".equals(WiskOpdr.deployVariant))maakTabletMW();
		else if("GR".equals(WiskOpdr.deployVariant))maakTabletGR();
		else maakTablet();
	}
	
	public void zetFormuleVakHouder(FormuleVakHouder formuleVakHouder)
	{
		this.formuleVakHouder = formuleVakHouder;
	}
	
	public void cleanTablet()
	{	removeAll();
		
	}
	
	private void maakTablet()
	{
		cleanTablet();

		int buttonX = 15;
		int buttonY = 48;
		int inset = 2;
		for (int i = 0; i < buttonCodes.length; i++) // rij
		{
			if (i == 0)
				buttonY = 48;

			for (int j = 0; j < buttonCodes[i].length; j++) // kolom
			{
				FormuleButton fb = new FormuleButton(buttonCodes[i][j]);
				fb.addActionListener(this);
				fb.setBorder(BorderFactory.createLineBorder(Color.lightGray));
				// fb.setFocusable(false);
				if (j == 0)
					buttonX = 15;
				
				int buttonWidth = (int) (buttonWidths[i][j] * eenheid + inset * (buttonWidths[i][j] - 1));
				
				if (i == 0)
				{
					JLabel fLabel = new JLabel("F" + (j + 1));
					if (j < 9)
						fLabel = new JLabel("  F" + (j + 1));
					else if (j > 11)
						fLabel = new JLabel(" ");
					fLabel.setAlignmentX(JLabel.CENTER);
					fLabel.setBounds(buttonX, buttonY - 20, buttonWidth, eenheid);

					fLabel.setForeground(Color.gray);
					fLabel.setFont(new Font("SansSerif", Font.PLAIN, 11));
					add(fLabel);
				}
				
				fb.setBounds(buttonX, buttonY, buttonWidth, eenheid);
				String code = buttonCodes[i][j];
				
				if (Character.isDigit(code.charAt(0)) || code.equals("plus") || code.equals("min")
					|| code.equals("maal") || code.equals("/") || code.equals(".") || code.equals("="))
				{
					fb.bgColor = new Color(230, 230, 230);
				}
				if (code.equals("ab..") || code.equals("\u03b1\u03b2..")) // alpha beta
				{
					fb.bgColor = new Color(255, 150, 150);
				}
				if (code.equals("x"))
				{
					fb.setFont(new Font("TimesRoman", Font.ITALIC + Font.BOLD, 16));
					if (WiskOpdr.mac)
						fb.setFont(new Font("SansSerif", Font.ITALIC + Font.BOLD, 13));

				}

				if (code.equals("a") || code.equals("t") || code.equals("y") || code.equals("b") || code.equals("k")
					|| code.equals("p") || code.equals("q"))
				{
					fb.setFont(new Font("TimesRoman", Font.ITALIC, 16));
					if (WiskOpdr.mac)
						fb.setFont(new Font("SanSerif", Font.ITALIC, 13));
				}
				
				buttonX += buttonWidth + inset;
				add(fb);
			}
			buttonY = buttonY + eenheid + inset;
		}
		setSize(buttonX + inset + 15, buttonY + 2 * inset + 15);
		closeButton.setBounds(getSize().width - 28, 8, 13, 12);
		add(closeButton, 0);
	}
	
	private void maakTabletMW()
	{	cleanTablet();
	
		eenheid = 20;
		
		int buttonX = 20;
		int buttonY = 43;
		int inset = 4;
		for(int i=0 ; i<buttonCodesMW.length ; i++)
		{	if(i==0) buttonY = 43;
			for(int j=0 ; j<buttonCodesMW[i].length ; j++)
			{ 	
				
				FormuleButton fb = new FormuleButton(buttonCodesMW[i][j],FormuleButton.TABLETKNOP);
				fb.addActionListener(this);
				fb.setFocusable(false);
				if(j==0) buttonX = 20;
				int buttonWidth = (int)(buttonWidthsMW[i][j]*eenheid + inset*(buttonWidthsMW[i][j]-1));
				
				fb.setBounds(buttonX, buttonY, buttonWidth, eenheid+1);
				String code = buttonCodesMW[i][j];
				if(Character.isDigit(code.charAt(0)) || code.equals("plus") || code.equals("min") || code.equals("maal") || code.equals("/") || code.equals(".") || code.equals("="))
				{	fb.bgColor = new Color(230,230,230);
				}
				if(code.equals("ab..") || code.equals("\u03b1\u03b2.."))
				{	fb.bgColor = new Color(255,150,150);
				}
				if(code.equals("x")) 
				{	fb.setFont(new Font("TimesRoman", Font.ITALIC + Font.BOLD, 16));
					if(WiskOpdr.mac) fb.setFont(new Font("SansSerif", Font.ITALIC + Font.BOLD , 13));
				
				}
				
				if(code.equals("a") || code.equals("t") || code.equals("y") || code.equals("b") || code.equals("k")|| code.equals("p") || code.equals("q")) 
				{	fb.setFont(new Font("TimesRoman", Font.ITALIC, 16));
					if(WiskOpdr.mac) fb.setFont(new Font("SanSerif", Font.ITALIC, 13));
				}
				buttonX += buttonWidth + inset;
				add(fb);
				
			}
			buttonY = buttonY + eenheid + inset+1;
		}
		setSize(buttonX+inset+12, buttonY+inset+17);
		//setBorder(BorderFactory.createLineBorder(Color.lightGray));
		closeButton.setVisible(false);
	}
	
	private void maakTabletGR()
	{	cleanTablet();
	
		eenheid = 22;
		
		int buttonX = 20;
		int buttonY = 43;
		int inset = 4;
		for(int i=0 ; i<buttonCodesGR.length ; i++)
		{	if(i==0) buttonY = 43;
			for(int j=0 ; j<buttonCodesGR[i].length ; j++)
			{ 	
				
				FormuleButton fb = new FormuleButton(buttonCodesGR[i][j],FormuleButton.TABLETKNOP);
				String code = buttonCodesGR[i][j];
				if(code.equals("123") || code.equals("\u03b1\u03b2.."))fb = new FormuleButton(buttonCodesGR[i][j],FormuleButton.BEWERKINGSKNOP);
				
				fb.addActionListener(this);
				fb.setFocusable(false);
				if(j==0) buttonX = 20;
				int buttonWidth = (int)(buttonWidthsGR[i][j]*eenheid + inset*(buttonWidthsGR[i][j]-1));
				if(i==0)
				{
					JLabel fLabel = new JLabel("F"+(j+1));
					if (j<9)
						fLabel = new JLabel("  F"+(j+1));
					else if (j > 11)
						fLabel = new JLabel(" ");
					fLabel.setAlignmentX(JLabel.CENTER);
					fLabel.setBounds(buttonX, buttonY-20, buttonWidth, eenheid);
					
					fLabel.setForeground(Color.gray);
					fLabel.setFont(new Font("SansSerif",Font.PLAIN,11));
					add(fLabel);
				}
				fb.setBounds(buttonX, buttonY, buttonWidth, eenheid+1);
				
				if(Character.isDigit(code.charAt(0)) || code.equals("plus") || code.equals("min") || code.equals("maal") || code.equals("/") || code.equals(".") || code.equals("="))
				{	fb.bgColor = new Color(230,230,230);
				}
				if(code.equals("ab..") || code.equals("\u03b1\u03b2.."))
				{	fb.bgColor = new Color(255,150,150);
				}
				if(code.equals("x")) 
				{	fb.setFont(new Font("TimesRoman", Font.ITALIC + Font.BOLD, 16));
					if(WiskOpdr.mac) fb.setFont(new Font("SansSerif", Font.ITALIC + Font.BOLD , 13));
				
				}
				
				if(code.equals("a") || code.equals("t") || code.equals("y") || code.equals("b") || code.equals("k")|| code.equals("p") || code.equals("q")) 
				{	fb.setFont(new Font("TimesRoman", Font.ITALIC, 16));
					if(WiskOpdr.mac) fb.setFont(new Font("SanSerif", Font.ITALIC, 13));
				}
				buttonX += buttonWidth + inset;
				add(fb);
				
			}
			buttonY = buttonY + eenheid + inset+1;
		}
		setSize(buttonX+inset+12, buttonY+inset+17);
		//setBorder(BorderFactory.createLineBorder(Color.lightGray));
		closeButton.setVisible(false);
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
		closeButton.setBounds(getSize().width-2, 3, 13,12);
	}
	
	private void maakTabletAbc()
	{	cleanTablet();
		int startButtonX = 15;
		int startButtonY = 28;
		
		int buttonX = startButtonX;
		int buttonY = startButtonX;
		int inset = 2;
		for(int i=0 ; i<buttonCodesAbc.length ; i++)
		{	if(i==0) buttonY = startButtonY;
			for(int j=0 ; j<buttonCodesAbc[i].length ; j++)
			{ 	FormuleButton fb = new FormuleButton(buttonCodesAbc[i][j]);
				fb.addActionListener(this);
				//fb.setFocusable(false);
				if(j==0) buttonX = startButtonX;
				int buttonWidth = (int)(buttonWidthsAbc[i][j]*eenheid + inset*(buttonWidthsAbc[i][j]-1));
				fb.setBounds(buttonX, buttonY, buttonWidth, eenheid);
				buttonX += buttonWidth + inset;
				add(fb);
				if(buttonCodesAbc[i][j].trim().equals("123"))
				{	fb.bgColor = new Color(255,150,150);
				}
			}
			buttonY = buttonY + eenheid + inset;
		}
		setSize(buttonX+startButtonX, buttonY+2*inset+15);
		closeButton.setBounds(getSize().width-28, 8, 13,12);
	}
	
	private void maakTabletAbcShift()
	{	cleanTablet();
	
		int buttonX = 15;
		int buttonY = 28;
		int inset = 2;
		for(int i=0 ; i<buttonCodesAbcShift.length ; i++)
		{	if(i==0) buttonY = 28;
			for(int j=0 ; j<buttonCodesAbcShift[i].length ; j++)
			{ 	FormuleButton fb = new FormuleButton(buttonCodesAbcShift[i][j]);
				fb.addActionListener(this);
				//fb.setFocusable(false);
				if(j==0) buttonX = 15;
				int buttonWidth = (int)(buttonWidthsAbcShift[i][j]*eenheid + inset*(buttonWidthsAbcShift[i][j]-1));
				fb.setBounds(buttonX, buttonY, buttonWidth, eenheid);
				buttonX += buttonWidth + inset;
				add(fb);
			}
			buttonY = buttonY + eenheid + inset;
		}
		setSize(buttonX+2*inset+15, buttonY+2*inset+15);
		closeButton.setBounds(getSize().width-28, 8, 13,12);
	}
	
	private void maakTabletAlpha()
	{	cleanTablet();
		int startButtonX = 14;
		int startButtonY = 31;
		
		
		int buttonX = startButtonX;
		int buttonY = startButtonY;
		int inset = 2;
		for(int i=0 ; i<buttonCodesAlpha.length ; i++)
		{	if(i==0) buttonY = startButtonY;
			for(int j=0 ; j<buttonCodesAlpha[i].length ; j++)
			{ 	FormuleButton fb = new FormuleButton(buttonCodesAlpha[i][j]);
				fb.addActionListener(this);
				//fb.setFocusable(false);
				if(j==0) buttonX = startButtonX;
				int buttonWidth = (int)(buttonWidthsAlpha[i][j]*eenheid + inset*(buttonWidthsAlpha[i][j]-1));
				fb.setBounds(buttonX, buttonY, buttonWidth, eenheid);
				buttonX += buttonWidth + inset;
				add(fb);
				if(buttonCodesAlpha[i][j].trim().equals("123"))
				{	fb.bgColor = new Color(255,150,150);
				}
			}
			buttonY = buttonY + eenheid + inset;
		}
		setSize(buttonX+startButtonX+inset, buttonY+2*inset+15);
		closeButton.setBounds(getSize().width-28, 8, 13,12);
	}
	
	private void maakTabletAlphaGR()
	{	cleanTablet();
	
		eenheid = 22;
	
	
		int startButtonX = 20;
		int startButtonY = 28;
		
		int buttonX = startButtonX;
		int buttonY = startButtonY;
		int inset = 4;
		for(int i=0 ; i<buttonCodesAlpha.length ; i++)
		{	if(i==0) buttonY = startButtonY;
			for(int j=0 ; j<buttonCodesAlpha[i].length ; j++)
			{ 	FormuleButton fb = new FormuleButton(buttonCodesAlpha[i][j],FormuleButton.TABLETKNOP);
				String code = buttonCodesAlpha[i][j];
				if(code.equals("123"))fb = new FormuleButton(buttonCodesAlpha[i][j],FormuleButton.BEWERKINGSKNOP);
				
				fb.addActionListener(this);
				//fb.setFocusable(false);
				if(j==0) buttonX = startButtonX;
				int buttonWidth = (int)(buttonWidthsAlpha[i][j]*eenheid + inset*(buttonWidthsAlpha[i][j]-1));
				fb.setBounds(buttonX, buttonY, buttonWidth, eenheid);
				buttonX += buttonWidth + inset;
				add(fb);
				if(buttonCodesAlpha[i][j].trim().equals("123"))
				{	fb.bgColor = new Color(255,150,150);
				}
			}
			buttonY = buttonY + eenheid + inset;
		}
		setSize(buttonX+startButtonX, buttonY+2*inset+15);
		closeButton.setBounds(getSize().width-28, 8, 13,12);
	}
	
	public void paintComponent(Graphics g)
	{	
		if("MW".equals(WiskOpdr.deployVariant))
		{
			if(backgroundImageMW==null) 
			{	backgroundImageMW  = Toolkit.getDefaultToolkit().getImage((new NWButtonUI()).getClass().getResource("resources/meerpallet.png"));
				MediaTracker tr = new MediaTracker(this);
				tr.addImage(backgroundImageMW, 0);
				try{tr.waitForAll();} catch(Exception e) {};
			}
			g.drawImage(backgroundImageMW, 4,6,null);
		}
		else if("GR".equals(WiskOpdr.deployVariant))
		{
			if(backgroundImageMW==null) 
			{	backgroundImageMW  = Toolkit.getDefaultToolkit().getImage((new NWButtonUI()).getClass().getResource("resources/meerpalletgr.png"));
				MediaTracker tr = new MediaTracker(this);
				tr.addImage(backgroundImageMW, 0);
				try{tr.waitForAll();} catch(Exception e) {};
			}
			g.drawImage(backgroundImageMW, 4,6,null);
		}
		else
		{
			//g.setColor(bgColor);
			//g.fillRect(0,0,getSize().width,getSize().height);
			/*g.setColor(new Color(0,0,150));
			g.fillRect(3,3,getSize().width-6,12);
			g.setColor(bgColor.brighter());
			g.drawLine(0,0,getSize().width-1,0);
			g.drawLine(0,0,0,getSize().height-1);
			g.setColor(bgColor.darker());
			g.drawLine(getSize().width-1,0,getSize().width-1,getSize().height-1);
			g.drawLine(0,getSize().height-1,getSize().width-1,getSize().height-1);*/
			
			
		
			for(int i=0 ; i<10 ; i++)
			{	g.setColor(new Color(100,100,100,5*i));
				g.fillRoundRect(i,i,getWidth()-3*i, getHeight()-3*i,20, 5);
			}
			g.setColor(bgColor);
			g.fillRect(8,5,getSize().width-20,getSize().height-20);
			
			int h = 20;
			for(int i=0 ; i<10 ; i++)
			{
				g.setColor(new Color(200+5*i,200+5*i,200+5*i));
				g.fillRect(8,5+h - (i+1)*h/10, getWidth()-20,h/10+1);
			}
			g.setColor(new Color(200,200,200));
			g.drawRect(8,5, getWidth()-20,h);
			
			
		}
		//super.paint(g);
	}
	
	public void actionPerformed(ActionEvent e)
	{	if(e.getSource()==closeButton) 
		{	((TabletOwner)getParent()).removeTablet();
			if(formuleVakHouder!=null)formuleVakHouder.zetTabletAan(false);
			return;
		}
		
		((Component)formuleVakHouder).requestFocus();
		String code = ((FormuleButton)e.getSource()).getCode();
		
		if(code.equals("ab..")) 
		{	maakTabletAbc();
			return;
		}
		if(code.equals("\u03b1\u03b2..")) // kleine alpha, kleine beta
		{	if("GR".equals(WiskOpdr.deployVariant))
			{	maakTabletAlphaGR();
				backgroundImageMW  = Toolkit.getDefaultToolkit().getImage((new NWButtonUI()).getClass().getResource("resources/meerpalletgr-alpha.png"));
				MediaTracker tr = new MediaTracker(this);
				tr.addImage(backgroundImageMW, 0);
				try{tr.waitForAll();} catch(Exception ex) {};
			}
			else maakTabletAlpha();
			return;
		}
		
		if(code.equals("123")) 
		{	if("GR".equals(WiskOpdr.deployVariant))
			{	maakTabletGR();
				backgroundImageMW  = Toolkit.getDefaultToolkit().getImage((new NWButtonUI()).getClass().getResource("resources/meerpalletgr.png"));
				MediaTracker tr = new MediaTracker(this);
				tr.addImage(backgroundImageMW, 0);
				try{tr.waitForAll();} catch(Exception ex) {};
			}
			else maakTablet();
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
		
		if(formuleVakHouder==null || !formuleVakHouder.geefFormuleVak().isEditable()) return;
		else if(code.equals("wortel")) formuleVakHouder.geefFormuleVak().zetWortelVak();
		else if (code.equals("ndewortel")) formuleVakHouder.geefFormuleVak().zetNdeWortelVak();
		else if (code.equals("breuk")) formuleVakHouder.geefFormuleVak().zetBreukVak();
		else if (code.equals("haakjes")) formuleVakHouder.geefFormuleVak().zetHaakjesVak();
		else if (code.equals("macht")) formuleVakHouder.geefFormuleVak().zetMachtVak();
		else if (code.equals("kwadraat")) formuleVakHouder.geefFormuleVak().zetKwadraatVak();
		else if (code.equals("ndelog")) formuleVakHouder.geefFormuleVak().zetNdeLogVak();
		else if (code.equals("integraal")) formuleVakHouder.geefFormuleVak().zetIntegraalVak();
		else if (code.equals("prv")) formuleVakHouder.geefFormuleVak().zetPrvVak();
        else if (code.equals("abs")) formuleVakHouder.geefFormuleVak().zetAbsVak();
        else if (code.equals("subscript")) formuleVakHouder.geefFormuleVak().zetSubscriptVak();
        else if (code.equals("bin")) formuleVakHouder.geefFormuleVak().zetBinVak();
        else if (code.equals("diff")) formuleVakHouder.geefFormuleVak().zetDiffVak();
        else if (code.equals("diff_partial")) formuleVakHouder.geefFormuleVak().zetDiffPartialVak();
        else if (code.equals("limiet0")) formuleVakHouder.geefFormuleVak().zetLimietVak(0);
        else if (code.equals("limiet1")) formuleVakHouder.geefFormuleVak().zetLimietVak(1);
        else if (code.equals("limiet2")) formuleVakHouder.geefFormuleVak().zetLimietVak(2);
        else if (code.equals("primitieve")) formuleVakHouder.geefFormuleVak().zetPrimitieveVak();
        else if (code.equals("sigma")) formuleVakHouder.geefFormuleVak().zetSigmaVak();
        else if (code.equals("conjug")) formuleVakHouder.geefFormuleVak().zetConjugVak();
        else if(code.equals("stelsel")) formuleVakHouder.geefFormuleVak().zetStelselVak();
        else if(code.equals("vector")) formuleVakHouder.geefFormuleVak().zetVectorVak();
        else if(code.equals("vectornotatie")) formuleVakHouder.geefFormuleVak().zetVectorNotatieVak();
        else if(code.equals("matrix")) formuleVakHouder.geefFormuleVak().zetMatrixVak();
		
				
		else if (code.equals("del"))formuleVakHouder.geefFormuleVak().delete();
		else if (code.equals("back"))formuleVakHouder.geefFormuleVak().backspace();
		else if (code.equals("space"))formuleVakHouder.geefFormuleVak().insert(" ");
		else if (code.equals("tab"))formuleVakHouder.geefFormuleVak().insert("    ");
		else if (code.equals("enter"))formuleVakHouder.geefFormuleVak().finish();
		
		else if (code.equals("plus"))formuleVakHouder.geefFormuleVak().insert("+");
		else if (code.equals("min"))formuleVakHouder.geefFormuleVak().insert("-");
		else if (code.equals("maal"))formuleVakHouder.geefFormuleVak().insert("*");
		else if (code.equals(WiskOpdr.rb.getString("ofLabel")))formuleVakHouder.geefFormuleVak().insert(" "+WiskOpdr.rb.getString("ofLabel")+" ");
		else if (code.equals("\u2227"))formuleVakHouder.geefFormuleVak().insert(" "+WiskOpdr.rb.getString("enLabel")+" ");
		else if (code.equals("\u2228"))formuleVakHouder.geefFormuleVak().insert(" "+WiskOpdr.rb.getString("ofLabel")+" ");
		else if (code.equals("\u2205"))formuleVakHouder.geefFormuleVak().insert(WiskOpdr.rb.getString("geenOplossingen"));
		
		else formuleVakHouder.geefFormuleVak().insert(code);
		
		
	}
	
	public void mousePressed(MouseEvent e)
	{	
		if("GR".equals(WiskOpdr.deployVariant) && getWidth()-30<e.getX() && e.getX()<getWidth()-10 && e.getY()>5 && e.getY()<20)
		{
			((TabletOwner)getParent()).removeTablet();
			if(formuleVakHouder!=null)formuleVakHouder.zetTabletAan(false);
			return;
		}
		else if(!"GR".equals(WiskOpdr.deployVariant) && getWidth()-40<e.getX() && e.getX()<getWidth()-20 && e.getY()>15 && e.getY()<30)
		{
			((TabletOwner)getParent()).removeTablet();
			if(formuleVakHouder!=null)formuleVakHouder.zetTabletAan(false);
			return;
		}
		startx = e.getX();
		starty = e.getY();
		
	}
	
	public void mouseDragged(MouseEvent e)
	{	int dx = e.getX() - startx;
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
