package fi.javalogoweb3d;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import fi.javalogoweb3d.formuleobjects.FormuleButton;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JCheckBox;

import org.cbook.cbookif.CBookEvent;
import org.cbook.cbookif.CBookEventHandler;
import org.cbook.cbookif.CBookEventListener;

import fi.logotekenap3d.Tekenblad3D;
import fi.logotekenap3d.TraceBeheerder;
//import fi.logotekenap3d.Uitvoerblad;
import fi.logotekenap3d.TekenApplet3D;
import fi.beans.base64code.StringCodeObject;
import fi.beans.wiskopdrbeans.CBookAware;
import fi.beans.wiskopdrbeans.InteractieEditPanel;
import fi.beans.wiskopdrbeans.InteractiePanel;

public class JavaLogoInteractiePanel extends JPanel implements InteractiePanel, ActionListener, MouseMotionListener, MouseListener, CBookAware
{
	private JavaLogoSchuifVeld javaLogoSchuifVeld;
	//private Uitvoerblad uitvoerblad;
	private TekenApplet3D uitvoerblad;
	int ubb, ubh;
	public TraceBeheerder trb;
	
	private JButton runButton;
	private JButton exportButton;
	private JButton importButton;
	
	private int scheidingX = 615;
	private boolean draggingScheidingX;
	private int dragStartX;
	private int dragStartY;
	
	private boolean uitvoerVeldZichtbaar = true;
	private boolean transparantOptie = true;
	private boolean draadFiguurOptie = true;
	private boolean zoomOptie = true;
	private boolean programmaVeldZichtbaar = true;
	private boolean deeltakenZichtbaar = true;
	private boolean whileLoopZichtbaar = true;
	private boolean keuzeCommandZichtbaar = true;
	private boolean printCommandsZichtbaar = true;
	private boolean tekenCommandsZichtbaar = true;
	private boolean traceZichtbaar = true;
	private boolean codeIOZichtbaar = true;
	
	private CBookEventHandler cbookEventHandler = new CBookEventHandler(this);
	
	Image zoomInImage, zoomUitImage;
	ZoomKnop solidDraadKeuzeButton;
	ZoomKnop transparantOpaqueButton;
	
	ZoomKnop zoomInButton;
	ZoomKnop zoomUitButton;
	
	boolean transparant = false;
	boolean draadFiguur = false;
	double zoomFactor = 1;
	
	public JavaLogoInteractiePanel()
	{
		setLayout(null);
		addMouseListener(this);
		addMouseMotionListener(this);
		
		java.net.URL imageURL = JavaLogoWeb3d.class.getResource("resources/zoominknop.gif");
		if (imageURL != null)
		{	ImageIcon imageIcon = new ImageIcon(imageURL);
			zoomInImage = imageIcon.getImage();
		}	
		imageURL = JavaLogoWeb3d.class.getResource("resources/zoomuitknop.gif");
		if (imageURL != null)
		{	ImageIcon imageIcon = new ImageIcon(imageURL);
			zoomUitImage = imageIcon.getImage();
		}	
	
		uitvoerblad = new TekenApplet3D(this);
		add(uitvoerblad);
		
// muis-actie op het uitvoerblad?? init()		
		
		runButton = new JButton(JavaLogoWeb3d.rb.getString("runButtonLabel"));
		runButton.setBounds(155, 540, 80, 50);
		runButton.setFont(JavaLogoWeb3d.boldfont);
		runButton.setMargin(new Insets(3,5,3,5));
		runButton.addActionListener(this);
		add(runButton,0);
		
		importButton = new JButton(JavaLogoWeb3d.rb.getString("importButtonLabel"));
		importButton.setBounds(5, 540, 120, 23);
		importButton.setFont(JavaLogoWeb3d.boldfont);
		importButton.setMargin(new Insets(2,5,2,5));
		importButton.addActionListener(this);
		add(importButton,0);
		
		exportButton = new JButton(JavaLogoWeb3d.rb.getString("exportButtonLabel"));
		exportButton.setBounds(5, 567, 120, 23);
		exportButton.setFont(JavaLogoWeb3d.boldfont);
		exportButton.setMargin(new Insets(2,5,2,5));
		exportButton.addActionListener(this);
		add(exportButton,0);
		
		solidDraadKeuzeButton = new ZoomKnop("draad");
    	solidDraadKeuzeButton.addActionListener(this);
    	
		transparantOpaqueButton = new ZoomKnop("transparant");
		transparantOpaqueButton.addActionListener(this);
		
		zoomInButton = new ZoomKnop("zoomin", zoomInImage);
		zoomInButton.addActionListener(this);
		zoomUitButton = new ZoomKnop("zoomuit", zoomUitImage);
		zoomUitButton.addActionListener(this);


	}
	
	private void layoutGui()
	{
		runButton.setBounds(codeIOZichtbaar ? 155 : 5, getHeight()-60, 80, 50);
		importButton.setBounds(5, getHeight()-60, 120, 23);
		exportButton.setBounds(5, getHeight()-33, 120, 23);
	}
	
	public void paintComponent(Graphics g)
	{
		g.setColor(new Color(225,225,225));
		g.fillRect(0, 0, getWidth(), getHeight());
		if(programmaVeldZichtbaar && uitvoerVeldZichtbaar)
		{	g.setColor(Color.WHITE);
			g.fillRect(5, 5, scheidingX-5, getHeight()-78);
			g.setColor(Color.gray);
			g.drawRect(5, 5, scheidingX-5, getHeight()-78);
		}
		else if(programmaVeldZichtbaar && !uitvoerVeldZichtbaar)
		{
			g.setColor(Color.WHITE);
			g.fillRect(5, 5, getWidth()-10, getHeight()-78);
			g.setColor(Color.gray);
			g.drawRect(5, 5, getWidth()-10, getHeight()-78);
		}
		else
		{
			g.setColor(Color.WHITE);
			g.fillRect(5, 5, getWidth()-10, getHeight()-10);
			g.setColor(Color.gray);
			g.drawRect(5, 5, getWidth()-10, getHeight()-10);
		}
	}
	
	public void setBounds(int x, int y, int b, int h)
	{
		super.setBounds(x,y,b,h);
		layoutGui();
		int ubx = programmaVeldZichtbaar ? scheidingX+5 : 5;
		int uby = 5;
		ubb = getWidth()-(programmaVeldZichtbaar ? scheidingX+10 : 10);
		ubh = programmaVeldZichtbaar ? getHeight()-77 : getHeight()-10;
		
//System.out.println("sb b = " + b);
//System.out.println("ubb = " + ubb);
		
		// avoid ubb < 0
		if (ubb < 0) 
			ubb = 50;

		uitvoerblad.setBounds(ubx, uby, ubb, ubh);
		uitvoerblad.tb.setBounds(0, 0, ubb, ubh - 50);
						
		if (javaLogoSchuifVeld == null) 
		{	
			
//System.out.println("sb jls == null");			
			javaLogoSchuifVeld = new JavaLogoSchuifVeld(6, 6, (uitvoerVeldZichtbaar ? scheidingX-6 : getWidth() - 11), getHeight()-79, uitvoerblad);
			javaLogoSchuifVeld.setBackground(Color.white);
			javaLogoSchuifVeld.zetGesloten(true);
			add(javaLogoSchuifVeld);
			javaLogoSchuifVeld.initialize();
			
			trb = new TraceBeheerder(uitvoerblad, javaLogoSchuifVeld);
			trb.setBounds(codeIOZichtbaar ? 265 : 115, getHeight()-64, 340, 58);
			trb.setBackground(new Color(230,230,230));
			add(trb);
			
			transparantOpaqueButton.setBounds(5,ubh-25,21,21);
			solidDraadKeuzeButton.setBounds(5+21+5, ubh-25, 21, 21);
			uitvoerblad.add(transparantOpaqueButton);
			uitvoerblad.add(solidDraadKeuzeButton);
			zoomInButton.setBounds(5+21+5+21+5, ubh-25,23,23);
			zoomUitButton.setBounds(5+21+5+21+5+21+5, ubh-25,23,23);
			uitvoerblad.add(zoomInButton);
			uitvoerblad.add(zoomUitButton);
		}
		else
		{	
//System.out.println("sb jls != null");			
			int jsb = uitvoerVeldZichtbaar ? scheidingX-6 : getWidth() - 11;
			int jsh = getHeight()-79;
			javaLogoSchuifVeld.setSize(jsb, jsh);
			trb.setBounds(codeIOZichtbaar ? 265 : 115, getHeight()-64, 340, 58);
	
			layoutOptions();
	
/*			
			if (uitvoerblad.tb.getgIm() != null)
			{	
System.out.println("getgIm not null");
				uitvoerblad.tb.im = null;
				uitvoerblad.tb.repaint();
				
				//uitvoerblad.paintDrawing(trb.isTraceAan());
			
			}
*/			
			
			uitvoerblad.tb.repaint();
		}
	}

	public void layoutOptions()
	{
		if (transparantOptie && draadFiguurOptie && zoomOptie)
		{	transparantOpaqueButton.setVisible(true);
			solidDraadKeuzeButton.setVisible(true);
			zoomInButton.setVisible(true);
			zoomUitButton.setVisible(true);
			uitvoerblad.tb.setBounds(0, 0, ubb, ubh - 25);
			transparantOpaqueButton.setBounds(5,ubh-25,21,21);
			solidDraadKeuzeButton.setBounds(5+21+5, ubh-25, 21, 21);
			zoomInButton.setBounds(5+21+5+21+5, ubh-25,23,23);
			zoomUitButton.setBounds(5+21+5+21+5+21+5, ubh-25,23,23);
		}
		else if (transparantOptie && !draadFiguurOptie && zoomOptie)
		{	transparantOpaqueButton.setVisible(true);
			solidDraadKeuzeButton.setVisible(false);
			zoomInButton.setVisible(true);
			zoomUitButton.setVisible(true);
			uitvoerblad.tb.setBounds(0, 0, ubb, ubh - 25);
			transparantOpaqueButton.setBounds(5,ubh-25,21,21);
			zoomInButton.setBounds(5+21+5, ubh-25,23,23);
			zoomUitButton.setBounds(5+21+5+21+5, ubh-25,23,23);
			
		}
		else if (!transparantOptie && draadFiguurOptie && zoomOptie)
		{	transparantOpaqueButton.setVisible(false);
			solidDraadKeuzeButton.setVisible(true);
			zoomInButton.setVisible(true);
			zoomUitButton.setVisible(true);
			uitvoerblad.tb.setBounds(0, 0, ubb, ubh - 25);
			solidDraadKeuzeButton.setBounds(5, ubh-25, 21, 21);
			zoomInButton.setBounds(5+21+5, ubh-25,23,23);
			zoomUitButton.setBounds(5+21+5+21+5, ubh-25,23,23);
			
		}
		else if (transparantOptie && draadFiguurOptie && !zoomOptie)
		{	transparantOpaqueButton.setVisible(true);
			solidDraadKeuzeButton.setVisible(true);
			zoomInButton.setVisible(false);
			zoomUitButton.setVisible(false);
			uitvoerblad.tb.setBounds(0, 0, ubb, ubh - 25);
			transparantOpaqueButton.setBounds(5,ubh-25,21,21);
			solidDraadKeuzeButton.setBounds(5+21+5, ubh-25, 21, 21);
		}
		else if (!transparantOptie && !draadFiguurOptie && zoomOptie)
		{	transparantOpaqueButton.setVisible(false);
			solidDraadKeuzeButton.setVisible(false);
			zoomInButton.setVisible(true);
			zoomUitButton.setVisible(true);
			uitvoerblad.tb.setBounds(0, 0, ubb, ubh - 25);
			zoomInButton.setBounds(5, ubh-25,23,23);
			zoomUitButton.setBounds(5+21+5, ubh-25,23,23);
			
		}
		else if (!transparantOptie && draadFiguurOptie && !zoomOptie)
		{	transparantOpaqueButton.setVisible(false);
			solidDraadKeuzeButton.setVisible(true);
			zoomInButton.setVisible(false);
			zoomUitButton.setVisible(false);
			uitvoerblad.tb.setBounds(0, 0, ubb, ubh - 25);
			solidDraadKeuzeButton.setBounds(5, ubh-25, 21, 21);
			
		}
		else if (transparantOptie && !draadFiguurOptie && !zoomOptie)
		{	transparantOpaqueButton.setVisible(true);
			solidDraadKeuzeButton.setVisible(false);
			zoomInButton.setVisible(false);
			zoomUitButton.setVisible(false);
			uitvoerblad.tb.setBounds(0, 0, ubb, ubh - 25);
			transparantOpaqueButton.setBounds(5,ubh-25,21,21);
		}
		else if (!transparantOptie && !draadFiguurOptie && !zoomOptie)
		{	transparantOpaqueButton.setVisible(false);
			solidDraadKeuzeButton.setVisible(false);
			zoomInButton.setVisible(false);
			zoomUitButton.setVisible(false);
			uitvoerblad.tb.setBounds(0, 0, ubb, ubh);
		}
		
	}
	
	public void zetTransparantOptie(boolean b)
	{
		transparantOptie = b;
		layoutOptions();
	}

	public void zetDraadFiguurOptie(boolean b)
	{
		draadFiguurOptie = b;
		layoutOptions();
	}

	public void zetZoomOptie(boolean b)
	{
		zoomOptie = b;
		layoutOptions();
	}	
		
	public void actionPerformed(ActionEvent e)
	{	
    	if (e.getActionCommand().equals("knop1"))
    		return;

    	if(e.getSource()==runButton)
		{	
			if (trb.isTraceAan()) 
				trb.switchTracingOff();
			uitvoerblad.paintDrawing(false);
			javaLogoSchuifVeld.repaint();
			
			String code = javaLogoSchuifVeld.getCode();
			Hashtable<String, Double> inputVars = javaLogoSchuifVeld.getInputVars();
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("program", code);
			map.put("inputVars", inputVars);
			cbookEventHandler.fire("text.program",map);
		}
		else if(e.getSource()==importButton)
		{	javaLogoSchuifVeld.importFrame();
		}
		else if(e.getSource()==exportButton)
		{	javaLogoSchuifVeld.exportFrame(javaLogoSchuifVeld.getCode());
		}
		
    	else if (e.getSource() == solidDraadKeuzeButton)
    	{
    		if (solidDraadKeuzeButton.getCode().equals("solid"))
    		{	
    			
System.out.println("sdkb solid");    			
    			solidDraadKeuzeButton.setCode("draad");
    			uitvoerblad.zetWireFrame(false);
    			uitvoerblad.repaint();
    			draadFiguur = false;
    		}
    		else if (solidDraadKeuzeButton.getCode().equals("draad"))
    		{	
    			
System.out.println("sdkb draad");    			
    			solidDraadKeuzeButton.setCode("solid");
    			uitvoerblad.zetWireFrame(true);
    			uitvoerblad.repaint();
    			draadFiguur = true;
    		}
    	}

    	else if (e.getSource() == transparantOpaqueButton)
    	{
    		if (transparantOpaqueButton.getCode().equals("opaque"))
    		{	
    			
System.out.println("tob opaque");    			
				transparantOpaqueButton.setCode("transparant");
    			uitvoerblad.zetTransparant(false);
    			uitvoerblad.repaint();
    			transparant = false;
    		}
    		else if (transparantOpaqueButton.getCode().equals("transparant"))
    		{	
    			
System.out.println("tob transparant");    			
				transparantOpaqueButton.setCode("opaque");
    			uitvoerblad.zetTransparant(true);
    			uitvoerblad.repaint();
    			transparant = true;
    		}
    	}

    	else if (e.getSource() == zoomInButton)
    	{
    		zoomFactor *= 11e-1d;
    		uitvoerblad.zoomIn();
    	}
    	else if (e.getSource() == zoomUitButton)
    	{
    		zoomFactor *= 91e-2d;
    		uitvoerblad.zoomUit();
    	}

	}
	
	
	@Override
	public void zetOpdracht(Hashtable h, String[] randomVars,Hashtable randomValues) 
	{
		
System.out.println("jlip zetOpdracht");

		Hashtable state = null;
		boolean uitvoerVeldZichtbaar = true;
		boolean transparantOptie = true;
		boolean draadFiguurOptie = true;
		boolean zoomOptie = true;
		boolean programmaVeldZichtbaar = true;
		boolean deeltakenZichtbaar = true;
		boolean whileLoopZichtbaar = true;
		boolean keuzeCommandZichtbaar = true;
		boolean printCommandsZichtbaar = true;
		boolean tekenCommandsZichtbaar = true;
		boolean traceZichtbaar = true;
		boolean codeIOZichtbaar = true;
		
		if(h.containsKey("state")) state = (Hashtable) h.get("state");
		if(h.containsKey("uitvoerVeldZichtbaar")) uitvoerVeldZichtbaar = ((Boolean)h.get("uitvoerVeldZichtbaar"));
		if(h.containsKey("transparantOptie")) transparantOptie = ((Boolean)h.get("transparantOptie"));
		if(h.containsKey("draadFiguurOptie")) draadFiguurOptie = ((Boolean)h.get("draadFiguurOptie"));
		if(h.containsKey("zoomOptie")) draadFiguurOptie = ((Boolean)h.get("zoomOptie"));
		if(h.containsKey("programmaVeldZichtbaar")) programmaVeldZichtbaar = ((Boolean)h.get("programmaVeldZichtbaar"));
		if(h.containsKey("deeltakenZichtbaar"))	deeltakenZichtbaar = ((Boolean)h.get("deeltakenZichtbaar"));
		if(h.containsKey("whileLoopZichtbaar")) whileLoopZichtbaar = ((Boolean)h.get("whileLoopZichtbaar"));
		if(h.containsKey("keuzeCommandZichtbaar")) keuzeCommandZichtbaar = ((Boolean)h.get("keuzeCommandZichtbaar"));
		if(h.containsKey("printCommandsZichtbaar")) printCommandsZichtbaar = ((Boolean)h.get("printCommandsZichtbaar"));
		if(h.containsKey("tekenCommandsZichtbaar")) tekenCommandsZichtbaar = ((Boolean)h.get("tekenCommandsZichtbaar"));
		if(h.containsKey("traceZichtbaar")) traceZichtbaar = ((Boolean)h.get("traceZichtbaar"));
		if(h.containsKey("codeIOZichtbaar")) codeIOZichtbaar = ((Boolean)h.get("codeIOZichtbaar"));
				
		zetUitvoerVeldZichtbaar(uitvoerVeldZichtbaar);
		zetTransparantOptie(transparantOptie);
		zetDraadFiguurOptie(draadFiguurOptie);
		zetZoomOptie(zoomOptie);
		zetProgrammaVeldZichtbaar(programmaVeldZichtbaar);
		zetDeeltaken(deeltakenZichtbaar);
		zetWhileLoopZichtbaar(whileLoopZichtbaar);
		zetKeuzeCommandZichtbaar(keuzeCommandZichtbaar);
		zetPrintCommandsZichtbaar(printCommandsZichtbaar);
		zetTekenCommandsZichtbaar(tekenCommandsZichtbaar);
		zetTraceZichtbaar(traceZichtbaar);
		zetCodeIOZichtbaar(codeIOZichtbaar);
		setState(state);
	}

	@Override
	public void setState(Hashtable h)
	{	
		String code = "";
		int scheidingX = 615;
		Hashtable<String, Double> inputVars = null;
		double draaiX = 0;
		double draaiY = 0;
		
		if(h.containsKey("code")) 
			code = (String)h.get("code");
		if(h.containsKey("scheidingX")) 
			scheidingX = ((Integer)h.get("scheidingX")).intValue();
		if(h.containsKey("inputVars")) 
			inputVars = (Hashtable<String, Double>)h.get("inputVars");
		
		if(inputVars!=null)
			javaLogoSchuifVeld.setInputVars(inputVars);
		
		if(h.containsKey("draaiX")) 
			draaiX = ((Double)h.get("draaiX")).doubleValue();
		if(h.containsKey("draaiY")) 
			draaiY = ((Double)h.get("draaiY")).doubleValue();

		
		javaLogoSchuifVeld.importeer(code);
		this.scheidingX = scheidingX;
		
		uitvoerblad.zetBeginHoeken(draaiX,draaiY);
		setBounds(getBounds());

		if(h.containsKey("transparant")) 
			transparant = ((Boolean)h.get("transparant")).booleanValue();
		if(h.containsKey("draadFiguur")) 
			draadFiguur = ((Boolean)h.get("draadFiguur")).booleanValue();
		
		if (transparant)
			transparantOpaqueButton.setCode("opaque");
		else
			transparantOpaqueButton.setCode("transparant");
		if (draadFiguur)
			solidDraadKeuzeButton.setCode("solid");
		else
			solidDraadKeuzeButton.setCode("draad");
		
		if(h.containsKey("zoomFactor")) 
			zoomFactor = ((Double)h.get("zoomFactor")).doubleValue();

System.out.println("zF = " + zoomFactor);		
		
		uitvoerblad.zetTransparant(transparant);
		uitvoerblad.zetWireFrame(draadFiguur);
		//uitvoerblad.zoom(zoomFactor);
		//uitvoerblad.repaint();
		//uitvoerblad.tekenOpnieuw();
			
//		setBounds(getBounds());	
		
		uitvoerblad.zoom(zoomFactor);
	}
	
	@Override
	public void setEditState(Hashtable h) 
	{
System.out.println("jlip setEditState");

		Hashtable state = null;
		Hashtable antwoordModel = null;
		boolean uitvoerVeldZichtbaar = false;
		boolean transparantOptie = true;
		boolean draadFiguurOptie = true;
		boolean zoomOptie = true;
		boolean programmaVeldZichtbaar = false;
		int scoreMax = 0;
		
		if(h.containsKey("state")) state = (Hashtable) h.get("state");
		if(h.containsKey("uitvoerVeldZichtbaar")) uitvoerVeldZichtbaar = ((Boolean)h.get("uitvoerVeldZichtbaar"));
		if(h.containsKey("transparantOptie")) transparantOptie = ((Boolean)h.get("transparantOptie"));
		if(h.containsKey("draadFiguurOptie")) draadFiguurOptie = ((Boolean)h.get("draadFiguurOptie"));
		if(h.containsKey("zoomOptie")) draadFiguurOptie = ((Boolean)h.get("zoomOptie"));
		if(h.containsKey("programmaVeldZichtbaar")) programmaVeldZichtbaar = ((Boolean)h.get("programmaVeldZichtbaar"));
		if(h.containsKey("deeltakenZichtbaar"))	deeltakenZichtbaar = ((Boolean)h.get("deeltakenZichtbaar"));
		if(h.containsKey("whileLoopZichtbaar")) whileLoopZichtbaar = ((Boolean)h.get("whileLoopZichtbaar"));
		if(h.containsKey("keuzeCommandZichtbaar")) keuzeCommandZichtbaar = ((Boolean)h.get("keuzeCommandZichtbaar"));
		if(h.containsKey("printCommandsZichtbaar")) printCommandsZichtbaar = ((Boolean)h.get("printCommandsZichtbaar"));
		if(h.containsKey("tekenCommandsZichtbaar")) tekenCommandsZichtbaar = ((Boolean)h.get("tekenCommandsZichtbaar"));
		if(h.containsKey("traceZichtbaar")) traceZichtbaar = ((Boolean)h.get("traceZichtbaar"));
		if(h.containsKey("codeIOZichtbaar")) codeIOZichtbaar = ((Boolean)h.get("codeIOZichtbaar"));
				
		zetUitvoerVeldZichtbaar(uitvoerVeldZichtbaar);
		zetTransparantOptie(transparantOptie);
		zetDraadFiguurOptie(draadFiguurOptie);
		zetZoomOptie(zoomOptie);
		zetProgrammaVeldZichtbaar(programmaVeldZichtbaar);
		zetDeeltaken(deeltakenZichtbaar);
		zetWhileLoopZichtbaar(whileLoopZichtbaar);
		zetKeuzeCommandZichtbaar(keuzeCommandZichtbaar);
		zetPrintCommandsZichtbaar(printCommandsZichtbaar);
		zetTekenCommandsZichtbaar(tekenCommandsZichtbaar);
		zetTraceZichtbaar(traceZichtbaar);
		zetCodeIOZichtbaar(codeIOZichtbaar);
		setState(state);
		
	}

	@Override
	public Hashtable getState()
	{	String code = "";
		int scheidingX = 615;
	
		code = javaLogoSchuifVeld.getCode();
		scheidingX = this.scheidingX;
		Hashtable inputVars = javaLogoSchuifVeld.getInputVars();
		 
	    Hashtable h = new Hashtable();
	    h.put("code", code);
	    h.put("scheidingX", new Integer(scheidingX));
	    h.put("inputVars", inputVars);
	    
	    h.put("draaiX", new Double(uitvoerblad.geefDraaiX()));
	    h.put("draaiY", new Double(uitvoerblad.geefDraaiY()));
	    
	    h.put("transparant", new Boolean(transparant));
	    h.put("draadFiguur", new Boolean(draadFiguur));
	    h.put("zoomFactor", new Double(zoomFactor));
	    
	    return h;
	}
	
	

	@Override
	public Hashtable getEditState() 
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public InteractieEditPanel getEditPanel() 
	{
		return  new JavaLogoInteractieEditPanel();
	}

	@Override
	public void wis() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void zetMaat() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int geefAsHoogte() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getIpId() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getScore()
	{	return 0;
	}

	@Override
	public int getScoreMax() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean isCorrect() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isFout() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void zetMode(int mode) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void zetNagekeken(boolean b) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void start() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void opnieuw() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void kijkNa() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void kijkNa(int stapNr) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addActionListener(ActionListener al) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		
	}

	@Override
	public void mousePressed(MouseEvent e) 
	{
		if((new Rectangle(scheidingX, 5, 10, getHeight()-78)).contains(e.getX(), e.getY()))
			draggingScheidingX = true;
		else
			draggingScheidingX = false;
		dragStartX = e.getX();
		dragStartY = e.getY();
		
	}

	@Override
	public void mouseReleased(MouseEvent e) 
	{
		setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		draggingScheidingX = false;
		
	}

	@Override
	public void mouseDragged(MouseEvent e) 
	{
		if(draggingScheidingX)
		{
			int dx = e.getX() - dragStartX;
			scheidingX += dx;
			dragStartX = e.getX();
			setBounds(getBounds());
			//javaLogoSchuifVeld.setSize(javaLogoSchuifVeld.getWidth()+dx, javaLogoSchuifVeld.getHeight());
			//uitvoerblad.setBounds(scheidingX+5+dx, 5, uitvoerblad.getWidth()-dx, uitvoerblad.getHeight());
			//uitvoerblad.tekenOpnieuw();
			
			//repaint();
			
		}
		
	}

	@Override
	public void mouseMoved(MouseEvent e) 
	{
		if((new Rectangle(scheidingX, 5, 10, getHeight()-78)).contains(e.getX(), e.getY()) && !draggingScheidingX)
			setCursor(new Cursor(Cursor.W_RESIZE_CURSOR));
		else
			setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		
	}
	
	public void zetUitvoerVeldZichtbaar(boolean b)
	{	uitvoerVeldZichtbaar = b;
		uitvoerblad.setVisible(b);
		uitvoerblad.repaint();
	}
	
	public void zetProgrammaVeldZichtbaar(boolean b)
	{	programmaVeldZichtbaar = b;
		javaLogoSchuifVeld.setVisible(b);
		runButton.setVisible(b);
		exportButton.setVisible(b && codeIOZichtbaar);
		importButton.setVisible(b && codeIOZichtbaar);
		trb.setVisible(b);
	}
	
	public void zetDeeltaken(boolean b)
	{	deeltakenZichtbaar = b;
		javaLogoSchuifVeld.zetDeeltaken(b);
	}
	
	public void zetWhileLoopZichtbaar(boolean b)
	{	whileLoopZichtbaar = b;
		javaLogoSchuifVeld.zetWhileLoopZichtbaar(b);
	}
	
	public void zetKeuzeCommandZichtbaar(boolean b)
	{	keuzeCommandZichtbaar = b;
		javaLogoSchuifVeld.zetKeuzeCommandZichtbaar(b);
	}
	
	public void zetPrintCommandsZichtbaar(boolean b)
	{	printCommandsZichtbaar = b;
		javaLogoSchuifVeld.zetPrintCommandsZichtbaar(b);
	}
	
	public void zetTekenCommandsZichtbaar(boolean b)
	{	tekenCommandsZichtbaar = b;
		javaLogoSchuifVeld.zetTekenCommandsZichtbaar(b);
	}
	
	public void zetTraceZichtbaar(boolean b)
	{	traceZichtbaar = b;
		trb.setVisible(b && programmaVeldZichtbaar);
	}
	
	public void zetCodeIOZichtbaar(boolean b)
	{	codeIOZichtbaar = b;
		exportButton.setVisible(b && programmaVeldZichtbaar);
		importButton.setVisible(b && programmaVeldZichtbaar);
		if(b)
		{	runButton.setLocation(155,runButton.getY());
			trb.setLocation(265,trb.getY());
		}
		else
		{	runButton.setLocation(5,runButton.getY());
			trb.setLocation(115,trb.getY());
		}
	}
	
	@Override
	public void addCBookEventListener(CBookEventListener listener, String command) {
		cbookEventHandler.addCBookEventListener(listener, command);
		
	}

	@Override
	public void removeCBookEventListener(CBookEventListener listener,String command) {
		cbookEventHandler.removeCBookEventListener(listener, command);
		
	}

	@Override
	public String[] getSendCmds() {
		String[] commands = {"text.program"};
		return commands;
	}

	@Override
	public String[] getAcceptedCmds() {
		String[] commands = {"text.program", "double.input", "double.input1", "double.input2", "double.input3", "double.input4"};
		return commands;
	}

	@Override
	public void acceptCBookEvent(CBookEvent event) {
		String command = event.getCommand();
		if(command.startsWith("text"))
		{
			Map map = (Map)event.getParameters();
			if(map!=null)
			{	String code = (String)map.get("program");
				Hashtable<String,Double> inputVars = (Hashtable<String,Double>)map.get("inputVars");
				javaLogoSchuifVeld.setInputVars(inputVars);
				javaLogoSchuifVeld.importeer(code);	
				uitvoerblad.paintDrawing(false);
			}
		}
		if(command.startsWith("double"))
		{
			Map map = (Map)event.getParameters();
			if(map!=null && command.equals("double.input"))
			{	String name = (String)map.get("name");
				double waarde = ((Double)map.get("value")).doubleValue();
				javaLogoSchuifVeld.setInputVar(name, waarde);
				uitvoerblad.paintDrawing(false);
			}
			else if(map!=null && command.startsWith("double.input"))
			{	String name = command.substring(command.length()-1);
				double waarde = ((Double)map.get("value")).doubleValue();
				javaLogoSchuifVeld.setInputVar(name, waarde);
				uitvoerblad.paintDrawing(false);
			}
			else if(map==null && command.startsWith("double.input"))
			{	String message = event.getMessage();
				double waarde = Double.parseDouble(message);
				String name = command.substring(command.length()-1);
				javaLogoSchuifVeld.setInputVar(name, waarde);
				uitvoerblad.paintDrawing(false);
			}
			
			String code = javaLogoSchuifVeld.getCode();
			Hashtable<String, Double> inputVars = javaLogoSchuifVeld.getInputVars();
			Map<String,Object> map1 = new HashMap<String,Object>();
			map1.put("program", code);
			map1.put("inputVars", inputVars);
			cbookEventHandler.fire("text.program",map1);
		}
	}

	
	@Override
	public String getLocalizedCmd(String cmd) {
		return JavaLogoWeb3d.rb.getString(CBA_PREFIX + cmd);
	}
	
}
