package fi.javalogoweb;

import fi.javalogoweb.schuifobjects.*;
import fi.beans.stringutils.*;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JTextArea;

import logotekenap.*;

/**
 * @author PBgv, changes made
 * 	19/2/2015 changed commandComponents ffrom array to ArrayList, deleted int: aantalCC
 *
 */
public class JavaLogoSchuifVeld extends SchuifVeld implements  MouseListener, MouseMotionListener
{
	/**
	 * 
	 */
	public static final int aantalDeeltaken = 5;
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
	//public static final int
	//public static final int
	
	
	
	private JPanel programmaPanel;
	private ProgrammaComponent programmaComponent;
	private DeeltaakBodyComponent[] deeltaakComponenten;
	private Uitvoerblad uitvoerblad;
	
	private VardisplayPanel vartracer = null;
	private boolean isVartracing = false;
	
	public JavaLogoSchuifVeld(int x, int y, int b, int h, Uitvoerblad tb)
	{	
		super(x,y,b,h);
		addMouseListener(this);
		addMouseMotionListener(this);
		uitvoerblad = tb;
	}
	
	public void execute(Uitvoerblad ub)
	{	
		programmaComponent.execute(ub, new VarSet());
	}
	
	public void initialize()
	{	
		// programmaPanel bevat de 'programma's' waar we componenten op kunnen droppen
		programmaPanel = new JPanel();
		programmaPanel.setBounds(ppx, ppy, ppw, pph);
		programmaPanel.setBackground(Color.WHITE);
		programmaPanel.setLayout(null);
		add(programmaPanel,0);
		
		programmaComponent = new ProgrammaComponent(0, 0, ProgrammaComponent.pcsw, pph, JavaLogoWeb.rb.getString("Tekenalgoritme"), this);
		programmaComponent.zetVast(true);
		programmaPanel.add(programmaComponent);
		
		vartracer = new VardisplayPanel();
		vartracer.setBounds(ccx, ccy, 2*ccsw+10, 515);
				
		CommandComponent currentCC;
		
		currentCC = new VooruitCComponent(ccx,ccy,ccsw,ccsh, this);
		add(currentCC,0);
		
		currentCC = new StapCComponent(ccx2,ccy,ccsw,ccsh, this);
		add(currentCC,0);
	
		currentCC = new LinksCComponent(ccx,ccy+30,ccsw,ccsh, this);
		add(currentCC,0);
		
		currentCC = new RechtsCComponent(ccx2,ccy+30,ccsw,ccsh, this);
		add(currentCC,0);
			
		currentCC = new PenAanCComponent(ccx,ccy+60,ccsw,ccsh, this);
		add(currentCC,0);
			
		currentCC = new PenUitCComponent(ccx2,ccy+60,ccsw,ccsh, this);
		add(currentCC,0);
			
		currentCC = new VulAanCComponent(ccx,ccy+90,ccsw,ccsh, this);
		add(currentCC,0);
			
		currentCC = new VulUitCComponent(ccx2,ccy+90,ccsw,ccsh, this);
		add(currentCC,0);

		currentCC = new VulBladCComponent(ccx,ccy+120,ccsw,ccsh, this);
		add(currentCC,0);
			
		currentCC = new PrintCComponent(ccx,ccy+150,ccsw,ccsh, this);
		add(currentCC,0);
			
		currentCC = new PrintlCComponent(ccx2,ccy+150,ccsw,ccsh, this);
		add(currentCC,0);
		
		currentCC = new HerhaalCommandComponent(ccx,ccy+200,cclw,cclh, this);
		add(currentCC,0);
		
		currentCC = new KeuzeCommandComponent(ccx,ccy+260,cclw,cclh, this);
		add(currentCC,0);
        
		currentCC = new VarCComponent(ccx,ccy+320,cclw,ccsh, this);
		add(currentCC,0);
		
		deeltaakComponenten = new DeeltaakBodyComponent[aantalDeeltaken];
		for(int i=0; i<aantalDeeltaken; i++)
		{
			DeeltaakCallCComponent dtc= new DeeltaakCallCComponent(ccx,ccy+360+30*i,cclw,ccsh, i+1, this);
			add(dtc,0);
			// create with dummy location and height
			deeltaakComponenten[i] = new DeeltaakBodyComponent(0,0,ProgrammaComponent.pcsw,ProgrammaComponent.pcclosedh, JavaLogoWeb.rb.getString("deeltaak")+(i+1), this);
			deeltaakComponenten[i].zetVast(false);
			dtc.setBody(deeltaakComponenten[i]);
			programmaPanel.add(deeltaakComponenten[i]);
		}
		// set location and height right, one by one...
		deeltaakComponenten[0].setLocation(ppw-ProgrammaComponent.pcsw, 0);
		deeltaakComponenten[0].changeHeight();			// was initialialized as closed, so this will open it.
		deeltaakComponenten[1].setLocation(ProgrammaComponent.pcsw+10, 180);
		deeltaakComponenten[1].changeHeight();
		deeltaakComponenten[2].setLocation(ProgrammaComponent.pcsw+20, 440);
		deeltaakComponenten[3].setLocation(ProgrammaComponent.pcsw+30, 455);
		deeltaakComponenten[4].setLocation(ProgrammaComponent.pcsw+40, 470);
		
		
	}
	
	
	void addToProgrammaPanel(CommandComponent c)
	{
		programmaPanel.add(c, 0);
	}
	
	public void zetStapel(CommandComponent cc)
	{	int x = cc.getLocation().x;
		int y = cc.getLocation().y;
		int b = cc.getSize().width;
		int h = cc.getSize().height;
		//if(asc instanceof InvoerSchuifComponent)
		//{ schuifcomponenten[aantalSc] = new InvoerSchuifComponent(this ,x,y,b,h);
		//}
		CommandComponent currentCC;
		if(cc instanceof PrintCComponent)
		{ 	currentCC = new PrintCComponent(x,y,b,h, this);
			add(currentCC,0);
		}
		if(cc instanceof VulBladCComponent)
		{ 	currentCC = new VulBladCComponent(x,y,b,h, this);
			add(currentCC,0);
		}
		if(cc instanceof PrintlCComponent)
		{ 	currentCC = new PrintlCComponent(x,y,b,h, this);
			add(currentCC,0);
		}
		if(cc instanceof PrintCComponent)
		{ 	currentCC = new PrintCComponent(x,y,b,h, this);
			add(currentCC,0);
		}
		if(cc instanceof InvoerCComponent)
		{ 	currentCC = new InvoerCComponent(x,y,b,h, this);
			add(currentCC,0);
		}
		if(cc instanceof PenAanCComponent)
		{ 	currentCC = new PenAanCComponent(x,y,b,h, this);
			add(currentCC,0);
		}
		if(cc instanceof PenUitCComponent)
		{ 	currentCC = new PenUitCComponent(x,y,b,h, this);
			add(currentCC,0);
		}
		
		if(cc instanceof VooruitCComponent)
		{ 	currentCC = new VooruitCComponent(x,y,b,h, this);
			add(currentCC,0);
		}
		if(cc instanceof LinksCComponent)
		{ 	currentCC = new LinksCComponent(x,y,b,h, this);
			add(currentCC,0);
		}
		if(cc instanceof RechtsCComponent)
		{ 	currentCC = new RechtsCComponent(x,y,b,h, this);
			add(currentCC,0);
		}
		if(cc instanceof VulAanCComponent)
		{ 	currentCC = new VulAanCComponent(x,y,b,h, this);
			add(currentCC,0);
		}
		if(cc instanceof VulUitCComponent)
		{ 	currentCC = new VulUitCComponent(x,y,b,h, this);
			add(currentCC,0);
		}
		if(cc instanceof StapCComponent)
		{ 	currentCC = new StapCComponent(x,y,b,h, this);
			add(currentCC,0);
		}
		if(cc instanceof HerhaalCommandComponent)
		{ 	currentCC = new HerhaalCommandComponent(x,y,b,h, this);
			add(currentCC,0);
		}
		if(cc instanceof KeuzeCommandComponent)
        {   currentCC = new KeuzeCommandComponent(x,y,b,h, this);
			add(currentCC,0);
        }
		if(cc instanceof VarCComponent)
		{ 	currentCC = new VarCComponent(x,y,b,h, this);
			add(currentCC,0);
		}
		if(cc instanceof DeeltaakCallCComponent)
		{ 	currentCC = new DeeltaakCallCComponent( (DeeltaakCallCComponent)cc, this);
			add(currentCC,0);
		}
	}
	
	public void verwijder(CommandComponent cc)
	{	
		remove(cc);
		tekenOpnieuw();
	}
	
	public void paintComponent(Graphics g)
	{	Dimension dd = getSize();
		g.setColor(getBackground());
		g.fillRect(0,0,dd.width,dd.height);
		g.setColor(new Color(230,240,255));
		g.fillRect(0,0,180,600);
		g.setColor(Color.gray);
		//g.drawLine(180, 0, 180, getHeight());
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
	
	/* unused
	public CommandComponent getCommandComponentAt(int x, int y)
	{	CommandComponent cc = null;
		Component c = getComponentAt(x,y);
		if(c!=this && c!=null && c instanceof CommandComponent) 
		{	cc = (CommandComponent)c;
			return cc.getCommandComponentAt(x - cc.getLocation().x,y - cc.getLocation().y);
		}
		return null;
	} */
	
	public void losSchuiver(CommandComponent sc, int x, int y)
	{	
		CommandContainer cc = getCommandContainerAt(x,y);
		if(cc != null )
		{	cc.addCComponent(sc);
			tekenOpnieuw();
		} else
		{
			super.losSchuiver(sc);
		}
	}
	
	public void zetSchuiver(CommandComponent sc)
	{	int newLx = sc.getAbsoluteLocation().x;
		int newLy = sc.getAbsoluteLocation().y;
		sc.setBounds(newLx,newLy,sc.getDragWidth(),sc.getSize().height);
		super.zetSchuiver(sc);
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
	
	void exportFrame(String contents) {
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
		tekenOpnieuw();
		ProgrammaImporter pi = new ProgrammaImporter(this);
		pi.importProgramma(s);
	}
	
	void importFrame() 
	{
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
	}

	public String getCode()
	{	String s0 = programmaComponent.getCode("");
		for(int i=0 ; i<aantalDeeltaken ; i++)
		{	
			s0 = s0 + deeltaakComponenten[i].getCode("");
		}
		return s0+"\n";
	}

	public void setVartracing(boolean vt)
	{
		if ( vt )
		{
			add(vartracer, 0);
			isVartracing = true;
		} else
		{
			isVartracing = false;
			this.remove(vartracer);
			vartracer.setContent("");
		}
		tekenOpnieuw();
	}
	
	/**
	 * Reapint this component and update the component holding the trace of the variables.
	 * This method will be called from the execute-methods in the CC's, when trace is on.
	 * 
	 * @param varset	the current set of variables in tracing mode
	 */
	void updateView(VarSet varset)
	{
		if ( isVartracing )
		{
			vartracer.setContent(varset.toString());
		}
		tekenOpnieuw();
	}
	
	public void setSize(int b, int h)
	{	
		if ((getSize().width == b) && (getSize().height == h))
			return;
		
		
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
		//System.out.println("MuisPressed: "+x+", "y);
		Component c = this.findComponentAt(x, y);
		//System.out.println(c.getClass().getName()+" - "+c.getX()+" - "+c.getY());
		if ( c instanceof CommandComponent )
		{
			mouseTargetComponent = (CommandComponent)c;
			mouseTargetComponent.mousePressed(x, y, modifiers);
		} else 
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
