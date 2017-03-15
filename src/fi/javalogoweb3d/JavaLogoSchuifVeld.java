package fi.javalogoweb3d;

import fi.beans.stringutils.*;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JTextArea;

import fi.logotekenap3d.*;

/**
 * @author PBgv, changes made
 * 	19/2/2015 changed commandComponents ffrom array to ArrayList, deleted int: aantalCC
 *
 */
public class JavaLogoSchuifVeld extends JPanel implements  MouseListener, MouseMotionListener
{
	/**
	 * Number of deeltaken
	 */
	public static final int aantalDeeltaken = 5;
	
	/**
	 * Maximum number of parameters in a deeltaak
	 */
	public static final int maxParamCount = 4;		// temp: must go to JavaLogoWeb

	/**
	 * commCompLargeWidth: width of two-column component (in pile)
	 */
	public static final int cclw = 160;
	/**
	 * commCompSmallWidth: width of single column component (in pile)
	 */
	public static final int ccsw = cclw/2-5;
	/**
	 * deeltaakCompWidth: width of deeltaak call (in pile)
	 */
	public static final int dtcw = cclw-30;
	/**
	 * commCompLargeHeight: width of herhaal/keuze component (in pile)
	 */
	public static final int cclh = 50;
	/**
	 * commCompSmallHeight: height of simple component
	 */
	public static final int ccsh = 25;
	/**
	 * CommandComponentX: x-pos of left column
	 */
	public static final int ccx = 10;
	/**
	 * CommandComponentX2: x-pos of right column
	 */
	public static final int ccx2 = ccx+cclw/2+5;
	/**
	 * CommandComponentY: y-pos of TOP cc
	 */
	public static final int ccy = 10;
	
	/**
	 * ProgrammaPanelWidth
	 */
	public static final int ppw = 400;
	/**
	 * ProgrammaPanelHeight
	 */
	public static final int pph = 505;
	/**
	 * ProgrammaPanelX
	 */
	public static final int ppx = 190;
	/**
	 * ProgrammaPanelY
	 */
	public static final int ppy = 10;
		
	private JPanel programmaPanel;
	private ProgrammaComponent programmaComponent;
	private DeeltaakBodyComponent[] deeltaakComponenten;
	private TekenApplet3D uitvoerblad;
	// var tracing is the responsibility of the TraceBeheerder and will be handled there,
	// though TraceBeheerder will add it on top of the JavaLogoSchuifVeld
	private boolean gesloten;
	
	// turtle-graphics in xy-plane
	private CommandComponent vooruitCC;
	private CommandComponent stapCC;
	private CommandComponent linksCC;
	private CommandComponent rechtsCC;
	
	// 3d-graphics in xy-plane
	private CommandComponent stapxCC;
	private CommandComponent stapyCC;
	//private CommandComponent stapCC;
	private CommandComponent zDraaiCC;
	
	// 3d-drawing
	private CommandComponent stapzCC;
	private CommandComponent stap3dCC;
	private CommandComponent xDraaiCC;
	private CommandComponent yDraaiCC;
	
	// drawing
	private CommandComponent penAanCC;
	private CommandComponent penUitCC;
	private CommandComponent vulAanCC;
	private CommandComponent vulUitCC;
	
// deze even niet
//	private CommandComponent vulbladCC;
//	private CommandComponent printCC;
//	private CommandComponent printlCC;
	
	private CommandComponent varCC;

	private CommandComponent herhaalCC;
	private CommandComponent whileCC;
	private CommandComponent keuzeCC;
	private CommandComponent[] deeltaakCC;
	
	private Hashtable<String, Double> inputVars = new Hashtable<String, Double>();
	
	ImporterFrame imf;
	ExporterFrame exf;
		
	public JavaLogoSchuifVeld(int x, int y, int b, int h, TekenApplet3D tb)
	{	
		setLayout(null);
		setBounds(x,y,b,h);
		addMouseListener(this);
		addMouseMotionListener(this);
		uitvoerblad = tb;
	}
	
	public void setInputVar(String name, double value)
	{	inputVars.put("input"+name, new Double(value));
	}
	
	public void setInputVars(Hashtable<String, Double> inputVars)
	{	this.inputVars = inputVars;
	}
	
	public Hashtable<String, Double> getInputVars()
	{	return inputVars;
	}
	
	public void execute(TraceBeheerder trb, TekenApplet3D ub)
	{	
		VarSet varSet = new VarSet();
		Iterator iter = (inputVars.keySet()).iterator();
		while (iter.hasNext()) {
			String key = (String)iter.next();
			double value = ((Double)inputVars.get(key).doubleValue());
			varSet.setParameter(key, value);
		}
		programmaComponent.execute(trb, ub, varSet);
	}
	
	public void initialize()
	{	
		// programmaPanel bevat de 'programma's' waar we componenten op kunnen droppen
		programmaPanel = new JPanel();
		programmaPanel.setBounds(ppx, ppy, ppw, pph);
		programmaPanel.setBackground(Color.WHITE);
		programmaPanel.setLayout(null);
		add(programmaPanel,0);
		
		programmaComponent = new ProgrammaComponent(0, 0, ProgrammaComponent.pcsw, pph, JavaLogoWeb3d.rb.getString("Tekenalgoritme"), this);
		programmaComponent.zetVast(true);
		programmaPanel.add(programmaComponent);
		
		// turtle in xy-vlak
		int rijNum = 1;
		// links
		vooruitCC = new VooruitCComponent(ccx,ccy+(rijNum-1)*30,ccsw,ccsh, this);
		add(vooruitCC,0);
		// rechts
		stapCC = new StapCComponent(ccx2,ccy+(rijNum-1)*30,ccsw,ccsh, this);
		add(stapCC,0);
		
		rijNum = 2;
		// links		
		linksCC = new LinksCComponent(ccx,ccy+(rijNum-1)*30,ccsw,ccsh, this);
		add(linksCC,0);
		// rechts
		rechtsCC = new RechtsCComponent(ccx2,ccy+(rijNum-1)*30,ccsw,ccsh, this);
		add(rechtsCC,0);
		
		// 3d-drawing
		rijNum = 3;
		// links
		stapzCC = new StapZCComponent(ccx,ccy+(rijNum-1)*30,ccsw,ccsh, this);
		add(stapzCC,0);
		// rechts
		stap3dCC = new Stap3DCComponent(ccx2,ccy+(rijNum-1)*30,ccsw,ccsh, this);
		add(stap3dCC,0);
		
		rijNum = 4; 
		// links
		xDraaiCC = new XDraaiCComponent(ccx,ccy+(rijNum-1)*30,ccsw,ccsh, this);
		add(xDraaiCC,0);
		// rechts
		yDraaiCC = new YDraaiCComponent(ccx2,ccy+(rijNum-1)*30,ccsw,ccsh, this);
		add(yDraaiCC,0);
		
		// tekenen
		rijNum = 5;
		// links
		penAanCC = new PenAanCComponent(ccx,ccy+(rijNum-1)*30,ccsw,ccsh, this);
		add(penAanCC,0);
		// rechts	
		penUitCC = new PenUitCComponent(ccx2,ccy+(rijNum-1)*30,ccsw,ccsh, this);
		add(penUitCC,0);
		rijNum = 6;
		// links	
		vulAanCC = new VulAanCComponent(ccx,ccy+(rijNum-1)*30,ccsw,ccsh, this);
		add(vulAanCC,0);
		//rechts	
		vulUitCC = new VulUitCComponent(ccx2,ccy+(rijNum-1)*30,ccsw,ccsh, this);
		add(vulUitCC,0);
		
// deze even niet
//		vulbladCC = new VulBladCComponent(ccx,ccy+120,ccsw,ccsh, this);
//		add(vulbladCC,0);
			
//deze even niet		
//		printCC = new PrintCComponent(ccx,ccy+150,ccsw,ccsh, this);
//		add(printCC,0);

//deze even niet		
//		printlCC = new PrintlCComponent(ccx2,ccy+150,ccsw,ccsh, this);
//		add(printlCC,0);
	
		varCC = new VarCComponent(ccx,ccy+190,cclw,ccsh, this);
		add(varCC,0);
		
		herhaalCC = new ForLoopCommandComponent(ccx,ccy+230,cclw,ccsh+10, this);
		add(herhaalCC,0);
		
		whileCC = new WhileLoopCommandComponent(ccx,ccy+270,cclw,ccsh+10, this);
		add(whileCC,0);

		keuzeCC = new KeuzeCommandComponent(ccx,ccy+310,cclw,ccsh+10, this);
		add(keuzeCC,0);
        
		deeltaakComponenten = new DeeltaakBodyComponent[aantalDeeltaken];
		deeltaakCC = new DeeltaakCallCComponent[aantalDeeltaken];
		for(int i=0; i<aantalDeeltaken; i++)
		{
			deeltaakCC[i] = new DeeltaakCallCComponent(ccx,ccy+360+30*i,cclw,ccsh, i+1, this);
			add(deeltaakCC[i],0);

			// create with dummy location and height
			deeltaakComponenten[i] = new DeeltaakBodyComponent(0,0,ProgrammaComponent.pcsw,ProgrammaComponent.pcclosedh, JavaLogoWeb3d.rb.getString("deeltaak")+(i+1), this);
			deeltaakComponenten[i].zetVast(false);
			((DeeltaakCallCComponent)deeltaakCC[i]).setBody(deeltaakComponenten[i]);
			programmaPanel.add(deeltaakComponenten[i]);
		}
		// set location and height right, one by one...
		deeltaakComponenten[0].setLocation(ppw-ProgrammaComponent.pcsw, 0);
		deeltaakComponenten[0].changeHeight();			// was initialialized as closed, so this will open it.
		deeltaakComponenten[1].setLocation(ProgrammaComponent.pcsw+10, 180);
		deeltaakComponenten[1].changeHeight();
		deeltaakComponenten[2].setLocation(ProgrammaComponent.pcsw+20, 400);
		deeltaakComponenten[3].setLocation(ProgrammaComponent.pcsw+30, 415);
		deeltaakComponenten[4].setLocation(ProgrammaComponent.pcsw+40, 430);
	}
	
	void addToProgrammaPanel(CommandComponent c)
	{
		programmaPanel.add(c, 0);
	}
	
	/**
	 * Gets the Dimension of the ProgrammaPanel. Needed for locating deeltaakbody's when dragging or
	 * resizing them.
	 * // Fix PBgv, 2015-08-16: added, because this was not done correctly when resizing the applet was introduced
	 * 
	 * @return Dimension of the ProgrammaPanel
	 */
	Dimension getPPDimension()
	{
		return new Dimension(programmaPanel.getWidth(), programmaPanel.getHeight());
	}
	
	public void zetStapel(CommandComponent cc)
	{	int x = cc.getLocation().x;
		int y = cc.getLocation().y;
		int b = cc.getSize().width;
		int h = cc.getSize().height;
		
		CommandComponent currentCC;
// deze even niet		
//		if(cc == printCC)
//		{ 	printCC = new PrintCComponent(x,y,b,h, this);
//			add(printCC,0);
//		}
//		if(cc == vulbladCC)
//		{ 	vulbladCC = new VulBladCComponent(x,y,b,h, this);
//			add(vulbladCC,0);
//		}
//		if(cc == printlCC)
//		{ 	printlCC = new PrintlCComponent(x,y,b,h, this);
//			add(printlCC,0);
//		}
		if(cc == penAanCC)
		{ 	penAanCC = new PenAanCComponent(x,y,b,h, this);
			add(penAanCC,0);
		}
		if(cc == penUitCC)
		{ 	penUitCC = new PenUitCComponent(x,y,b,h, this);
			add(penUitCC,0);
		}		
		if(cc == vooruitCC)
		{ 	vooruitCC = new VooruitCComponent(x,y,b,h, this);
			add(vooruitCC,0);
		}
		if(cc == linksCC)
		{ 	linksCC = new LinksCComponent(x,y,b,h, this);
			add(linksCC,0);
		}
		if(cc == rechtsCC)
		{ 	rechtsCC = new RechtsCComponent(x,y,b,h, this);
			add(rechtsCC,0);
		}
		if(cc == vulAanCC)
		{ 	vulAanCC = new VulAanCComponent(x,y,b,h, this);
			add(vulAanCC,0);
		}
		if(cc == vulUitCC)
		{ 	vulUitCC = new VulUitCComponent(x,y,b,h, this);
			add(vulUitCC,0);
		}
		if(cc == stapCC)
		{ 	stapCC = new StapCComponent(x,y,b,h, this);
			add(stapCC,0);
		}
		if(cc == stapzCC)
		{ 	stapzCC = new StapZCComponent(x,y,b,h, this);
			add(stapzCC,0);
		}
		if(cc == stap3dCC)
		{ 	stap3dCC = new Stap3DCComponent(x,y,b,h, this);
			add(stap3dCC,0);
		}
		if(cc == xDraaiCC)
		{ 	xDraaiCC = new XDraaiCComponent(x,y,b,h, this);
			add(xDraaiCC,0);
		}
		if(cc == yDraaiCC)
		{ 	yDraaiCC = new YDraaiCComponent(x,y,b,h, this);
			add(yDraaiCC,0);
		}
		if(cc == herhaalCC)
		{ 	herhaalCC = new ForLoopCommandComponent(x,y,b,h, this);
			add(herhaalCC,0);
			cc.setSize(cc.getWidth(), cclh);
		}
		if(cc == whileCC)
		{ 	whileCC = new WhileLoopCommandComponent(x,y,b,h, this);
			add(whileCC,0);
			cc.setSize(cc.getWidth(), cclh);
		}
		if(cc == keuzeCC)
        {   keuzeCC = new KeuzeCommandComponent(x,y,b,h, this);
			add(keuzeCC,0);
			cc.setSize(cc.getWidth(), cclh);
        }
		
		if(cc == varCC)
		{ 	varCC = new VarCComponent(x,y,b,h, this);
			add(varCC,0);
		}
		
		for(int i=0; i<aantalDeeltaken; i++)
		{	if(cc == deeltaakCC[i])
			{ 	deeltaakCC[i] = new DeeltaakCallCComponent( (DeeltaakCallCComponent)cc, this);
				add(deeltaakCC[i],0);
			}
		}
	}
	
// veranderen	
	private void herschikStapel()
	{	int yLocation = ccy;
		
		if (vooruitCC.isVisible())
			yLocation += 190;
//even niet		
//		printCC.setLocation(printCC.getX(), yLocation);
//		printlCC.setLocation(printlCC.getX(), yLocation);
		
//		if(printCC.isVisible())
//			yLocation += 40;
//		else
//			yLocation += 10;
		
		
		varCC.setLocation(varCC.getX(), yLocation);
		yLocation += 40;
		herhaalCC.setLocation(herhaalCC.getX(), yLocation);
		yLocation += 40;
		whileCC.setLocation(whileCC.getX(), yLocation);
		if(whileCC.isVisible())
			yLocation += 40;
		keuzeCC.setLocation(keuzeCC.getX(), yLocation);
		if (keuzeCC.isVisible())
			yLocation += 50;
		else
			yLocation += 10;
		for(int i=0 ; i<5 ; i++)
		{	deeltaakCC[i].setLocation(ccx,yLocation);
			yLocation +=30;
		}
	}

	public void verwijder(CommandComponent cc)
	{	
		remove(cc);
		repaint();
	}
	
	public void paintComponent(Graphics g)
	{	Dimension dd = getSize();
		g.setColor(getBackground());
		g.fillRect(0,0,dd.width,dd.height);
		g.setColor(new Color(205,230,255));
		g.fillRect(4,4,172,dd.height-8);
		g.setColor(Color.gray);
	}
	
	public CommandContainer getCommandContainerAt(int x, int y)
	{	
		// find the deepest component in programmaPanel
		Component c = programmaPanel.findComponentAt(x-ppx,y-ppy);
		// if c is a CommandComponent, move to the CommandContainer that holds this object
		if ( c instanceof CommandComponent)
		{
			c = c.getParent();
		}
		if( c instanceof CommandContainer) 
		{	
			CommandContainer cc =  (CommandContainer)c;
			// don't add to the Commands that are in the piles, for pickup of new ones
			//if ( cc.getOwner().isStapel ) not needed anymore (find in programmaPanel)
			//{
			//	return null;
			//}
			return cc;
		}
		return null;
	}
	
	public void losSchuiver(CommandComponent sc, int x, int y)
	{	
		CommandContainer cc = getCommandContainerAt(x,y);
		if(cc != null )
		{	cc.addCComponent(sc);
			repaint();
		} 
	}
	
	public void zetSchuiver(CommandComponent sc)
	{	int newLx = sc.getAbsoluteLocation().x;
		int newLy = sc.getAbsoluteLocation().y;
		sc.setBounds(newLx,newLy,sc.getDragWidth(),sc.getSize().height);
		setComponentZOrder(sc, 0);
		if(sc instanceof CommandComponent)
		{
			CommandContainer cc = getCommandContainerAt(newLx,newLy);
			if (cc != null) 
				cc.reArrange();
		}
		
	}
	
	void traceComponent(CommandComponent sc, int ex, int ey)
	{	
		if ( !sc.isTraceable() ) return;
		// find component in programmaPanel, so nothing on the left side nor the dragged CC itself will be found
		Component c = programmaPanel.findComponentAt(ex-ppx,ey-ppy);
		// if c is a CommandComponent set Caret on that component
		if ( c instanceof CommandComponent )
		{
			((CommandComponent)c).setCaret(ey);
		}
		// if c is a CommandContainer, then it must be over the empty space, so set caret
		// to top of the container if it is empty, bottom of last component otherwise
		if( c instanceof CommandContainer) 
		{	
			((CommandContainer)c).setCaret(ey);
		}
	}
	
	void exportFrame(String contents) 
	{
/*		
		final TextArea area = new TextArea(contents, 0, 0, TextArea.SCROLLBARS_NONE);
		Frame f = new Frame("Code van het algoritme");
		f.setLayout(new BorderLayout());
		f.add(area,BorderLayout.CENTER);
		f.addWindowListener(new WindowAdapter() {
			public void windowOpened(WindowEvent e) {
				area.requestFocus();
				area.setCaretPosition(0);
			}
			public void windowActivated(WindowEvent e) {
				area.selectAll();
			}
			public void windowClosing(WindowEvent e) {
					e.getWindow().dispose();
			} });
		f.pack();
		f.setVisible(true);
		f.toFront();
*/
		
		if (exf != null)
		{	return;
		}

		if (imf != null)
		{	remove(imf);
			imf = null;
		}

		exf = new ExporterFrame(contents, this);
		exf.setBounds(0,0, ppw, getSize().height);
		add(exf, 0);
		exf.validate();
		repaint();

	}
	
	private void clearProgram()
	{
		programmaComponent.clearProgram();
		for ( int i=0; i<aantalDeeltaken; i++ )
		{
			deeltaakComponenten[i].clearProgram();
			deeltaakComponenten[i].setDeeltaakHeader("deeltaak"+(i+1), "");
		}
	}
	
	DeeltaakBodyComponent getDeeltaakBody(int i)
	{
		return deeltaakComponenten[i];
	}
	
	ProgrammaComponent getProgramma()
	{
		return programmaComponent;
	}
	
	void importeer(String s)
	{
		clearProgram();
		repaint();
	
		ProgrammaImporter pi = new ProgrammaImporter(this);
		pi.importProgramma(s);
	}
	
	void importFrame() 
	{
/*		
		try
		{
			ImporterFrame imf = new ImporterFrame("Importeer code", this);
			imf.pack();
			imf.setVisible(true);
			imf.toFront();
		} 
		catch ( Exception e )
		{ 
			System.out.println("Mis!  "+e.getMessage());
		}
*/		
		
		if (imf != null)
		{	return;
		}

		if (exf != null)
		{	remove(exf);
			exf = null;
		}

		imf = new ImporterFrame("Importeer code", this);
		imf.setBounds(0,0, ppw, getSize().height);
		add(imf, 0);
		imf.validate();
		repaint();

	}

	public String getCode()
	{	String s0 = programmaComponent.getCode("");
		for(int i=0 ; i<aantalDeeltaken ; i++)
		{
			s0 = s0 + deeltaakComponenten[i].getCode("");
		}
		return s0+"\n";
	}
	
	public boolean isGesloten()
	{	return gesloten;
	}
	
	public void zetGesloten(boolean b)
	{	gesloten = b;
	}

	
	public void zetDeeltaken(boolean b)
	{	
		for(int i=0; i<aantalDeeltaken; i++)
		{	deeltaakCC[i].setVisible(b);
			((DeeltaakCallCComponent)deeltaakCC[i]).getBody().setVisible(b);
		}
	}
	
	public void zetWhileLoopZichtbaar(boolean b)
	{	
		whileCC.setVisible(b);
		herschikStapel();
	}
	
	public void zetKeuzeCommandZichtbaar(boolean b)
	{	
		keuzeCC.setVisible(b);
		herschikStapel();
	}
	
	public void zetPrintCommandsZichtbaar(boolean b)
	{	
//even niet		
//		printCC.setVisible(b);
//		printlCC.setVisible(b);
//		herschikStapel();
	}
	
// veranderen	
	public void zetTekenCommandsZichtbaar(boolean b)
	{	vooruitCC.setVisible(b);
		stapCC.setVisible(b);
		linksCC.setVisible(b);
		rechtsCC.setVisible(b);
		stapzCC.setVisible(b);
		stap3dCC.setVisible(b);
		xDraaiCC.setVisible(b);
		yDraaiCC.setVisible(b);
		penAanCC.setVisible(b);
		penUitCC.setVisible(b);
		vulAanCC.setVisible(b);
		vulUitCC.setVisible(b);
//even niet		
//		vulbladCC.setVisible(b);
		herschikStapel();
	}
	
	public void setSize(int b, int h)
	{	
		if ((getSize().width == b) && (getSize().height == h))
			return;
		programmaPanel.setSize(b-ppx, h);
		programmaComponent.setSize(programmaComponent.getWidth(), h-20);
		super.setSize(b, h);
	
	}

	/* PBgv: Fix voor het probleem van het verlies van de MouseListeners in Java8.
	 * Outline:
	 * Listeners move to main Panel (this). At mousePressed the CC that's being clicked is locate, 
	 * and remembered. The event is passed on, just as the ensueing drag and release events. 
	 * Outline phase 2:
	 * For scrolling, we want the ProgrammaComponents to implement MouseWheelListener.
	 * Unfortunately, when you implement this interface, ALL mouseEvents will be passed to the
	 * ProgrammaComponent. So PC needs to implement MouseListener and MouseMotionListener. 
	 * But then, these events have wrong x,y: local to the ProgrammaComponent.
	 * This is solved by having methods like mousePressed(x, y, modifiers) which do the real work.
	 * The methods from the interfaces will compute the right (x,y) from the event and the CC's
	 * absolute position and call the 'work-methods'. Pfff, Bloody hell...
	 * Note: modifiers are as yet unused. Maybe in the future: shift-click to select & drag >1 CC!
	 */
	
	private CommandComponent mouseTargetComponent = null;
	
	public void mousePressed(int x, int y, int modifiers) 
	{
		Component c = this.findComponentAt(x, y);
		if ( c instanceof CommandComponent )
		{
			mouseTargetComponent = (CommandComponent)c;
			mouseTargetComponent.mousePressed(x, y, modifiers);
		} 
		else 
		{
			requestFocus();		// end possible editing of parameters, see ParameterTextField for details	
			if ( c instanceof CommandContainer)
			{	
				Component c2 = c.getParent();
				if ( c2 instanceof CompositeCommandComponent )
				{
					mouseTargetComponent = (CommandComponent)c2;
					mouseTargetComponent.mousePressed(x, y, modifiers);
				}
			}
		}
	}
	
	public void mouseReleased(int x, int y, int modifiers) 
	{
		if ( mouseTargetComponent != null )
		{
			mouseTargetComponent.mouseReleased(x, y, modifiers);
			mouseTargetComponent = null;
		}
	}
	
	public void mouseDragged(int x, int y, int modifiers) 
	{
		if ( mouseTargetComponent != null )
		{
			mouseTargetComponent.mouseDragged(x, y, modifiers);
		}
	}
	
	/*
	 * The methods form the mouse(Motion)Listener interfaces
	 */
	
	@Override
	public void mousePressed(MouseEvent e) 
	{
		mousePressed(e.getX(), e.getY(), e.getModifiersEx());
	}
	
	@Override
	public void mouseReleased(MouseEvent e) 
	{
		mouseReleased(e.getX(), e.getY(), e.getModifiersEx());
	}
	
	@Override
	public void mouseDragged(MouseEvent e) 
	{
		mouseDragged(e.getX(), e.getY(), e.getModifiersEx());
	}
	
	@Override
	public void mouseMoved(MouseEvent e) 
	{
		// unused		
	}
	
	@Override
	public void mouseClicked(MouseEvent e) 
	{
		// unused		
	}
	
	@Override
	public void mouseEntered(MouseEvent e) 
	{
		// unused
	}
	
	@Override
	public void mouseExited(MouseEvent e) 
	{
		// unused
	}

}
