package fi.javalogoweb;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
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

import javax.swing.JButton;
import javax.swing.JPanel;

import org.cbook.cbookif.CBookEvent;
import org.cbook.cbookif.CBookEventHandler;
import org.cbook.cbookif.CBookEventListener;

import logotekenap.Tekenblad;
import logotekenap.TraceBeheerder;
import logotekenap.Uitvoerblad;
import fi.beans.wiskopdrbeans.CBookAware;
import fi.beans.wiskopdrbeans.InteractieEditPanel;
import fi.beans.wiskopdrbeans.InteractiePanel;

public class JavaLogoInteractiePanel extends JPanel implements InteractiePanel, ActionListener, MouseMotionListener, MouseListener,  CBookAware
{
	private JavaLogoSchuifVeld javaLogoSchuifVeld;
	private Uitvoerblad uitvoerblad;
	private TraceBeheerder trb;
	
	private JButton runButton;
	private JButton exportButton;
	private JButton importButton;
	
	public static final Font defaultfont = new Font("Calibri", Font.PLAIN, 12);
	public static final Font boldfont = new Font("Verdana", Font.BOLD, 12);
	
	private int scheidingX = 615;
	private boolean draggingScheidingX;
	private int dragStartX;
	private int dragStartY;
	
	private boolean uitvoerVeldZichtbaar = true;
	private boolean programmaVeldZichtbaar = true;
	private boolean deeltakenZichtbaar = true;
	private boolean whileLoopZichtbaar = true;
	private boolean printCommandsZichtbaar = true;
	private boolean tekenCommandsZichtbaar = true;
	private boolean traceZichtbaar = true;
	private boolean runZichtbaar = true;
	private boolean codeIOZichtbaar = true;
	
	private CBookEventHandler cbookEventHandler = new CBookEventHandler(this);	
	
	
	public JavaLogoInteractiePanel()
	{
		setLayout(null);
		addMouseListener(this);
		addMouseMotionListener(this);
		
		uitvoerblad = new Tekenblad(this);
		add(uitvoerblad);
		
		runButton = new JButton(JavaLogoWeb.rb.getString("runButtonLabel"));
		runButton.setBounds(155, 540, 80, 50);
		runButton.setFont(JavaLogoWeb.boldfont);
		runButton.setMargin(new Insets(3,5,3,5));
		runButton.addActionListener(this);
		add(runButton,0);
		
		importButton = new JButton(JavaLogoWeb.rb.getString("importButtonLabel"));
		importButton.setBounds(5, 540, 120, 23);
		importButton.setFont(JavaLogoWeb.boldfont);
		importButton.setMargin(new Insets(2,5,2,5));
		importButton.addActionListener(this);
		add(importButton,0);
		
		exportButton = new JButton(JavaLogoWeb.rb.getString("exportButtonLabel"));
		exportButton.setBounds(5, 567, 120, 23);
		exportButton.setFont(JavaLogoWeb.boldfont);
		exportButton.setMargin(new Insets(2,5,2,5));
		exportButton.addActionListener(this);
		add(exportButton,0);
	}
	
	private void layoutGui()
	{
		runButton.setBounds(codeIOZichtbaar ? 155 : 5, getHeight()-60, 80, 50);
		importButton.setBounds(5, getHeight()-60, 120, 23);
		exportButton.setBounds(5, getHeight()-33, 120, 23);
	}
	
	public void execprogramma()
	{	javaLogoSchuifVeld.execute(uitvoerblad);
		
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
		int ubb = getWidth()-(programmaVeldZichtbaar ? scheidingX+10 : 10);
		int ubh = programmaVeldZichtbaar ? getHeight()-77 : getHeight()-10;
		
		uitvoerblad.setBounds(ubx, uby, ubb, ubh);
						
		if (javaLogoSchuifVeld == null) 
		{	
			javaLogoSchuifVeld = new JavaLogoSchuifVeld(6, 6, (uitvoerVeldZichtbaar ? scheidingX-6 : getWidth() - 11), getHeight()-79, uitvoerblad);
			javaLogoSchuifVeld.setBackground(Color.white);
			javaLogoSchuifVeld.zetGesloten(true);
			add(javaLogoSchuifVeld);
			javaLogoSchuifVeld.initialize();
			
			trb = new TraceBeheerder( (Tekenblad)uitvoerblad, javaLogoSchuifVeld);
			trb.setBounds(codeIOZichtbaar ? 265 : 115, getHeight()-64, 340, 58);
			trb.setBackground(new Color(230,230,230));
			trb.addActionListener(this);
			add(trb);
			
			uitvoerblad.meldTraceBeheerder(trb);
		}
		else
		{	
			int jsb = uitvoerVeldZichtbaar ? scheidingX-6 : getWidth() - 11;
			int jsh = getHeight()-79;
			javaLogoSchuifVeld.setSize(jsb, jsh);
			trb.setBounds(codeIOZichtbaar ? 265 : 115, getHeight()-64, 340, 58);
			uitvoerblad.repaint();
		}
	}
	
	public void actionPerformed(ActionEvent e)
	{	
		if(e.getSource()==runButton)
		{	
			uitvoerblad.tekenOpnieuw();
			
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
		javaLogoSchuifVeld.repaint();
	}
	
	
	@Override
	public void zetOpdracht(Hashtable h, String[] randomVars,Hashtable randomValues) {
		Hashtable state = null;
		boolean uitvoerVeldZichtbaar = true;
		boolean programmaVeldZichtbaar = true;
		boolean deeltakenZichtbaar = true;
		boolean whileLoopZichtbaar = true;
		boolean printCommandsZichtbaar = true;
		boolean tekenCommandsZichtbaar = true;
		boolean traceZichtbaar = true;
		boolean codeIOZichtbaar = true;
		
		if(h.containsKey("state")) state = (Hashtable) h.get("state");
		if(h.containsKey("uitvoerVeldZichtbaar")) uitvoerVeldZichtbaar = ((Boolean)h.get("uitvoerVeldZichtbaar"));
		if(h.containsKey("programmaVeldZichtbaar")) programmaVeldZichtbaar = ((Boolean)h.get("programmaVeldZichtbaar"));
		if(h.containsKey("deeltakenZichtbaar"))	deeltakenZichtbaar = ((Boolean)h.get("deeltakenZichtbaar"));
		if(h.containsKey("whileLoopZichtbaar")) whileLoopZichtbaar = ((Boolean)h.get("whileLoopZichtbaar"));
		if(h.containsKey("printCommandsZichtbaar")) printCommandsZichtbaar = ((Boolean)h.get("printCommandsZichtbaar"));
		if(h.containsKey("tekenCommandsZichtbaar")) tekenCommandsZichtbaar = ((Boolean)h.get("tekenCommandsZichtbaar"));
		if(h.containsKey("traceZichtbaar")) traceZichtbaar = ((Boolean)h.get("traceZichtbaar"));
		if(h.containsKey("codeIOZichtbaar")) codeIOZichtbaar = ((Boolean)h.get("codeIOZichtbaar"));
				
		zetUitvoerVeldZichtbaar(uitvoerVeldZichtbaar);
		zetProgrammaVeldZichtbaar(programmaVeldZichtbaar);
		zetDeeltaken(deeltakenZichtbaar);
		zetWhileLoopZichtbaar(whileLoopZichtbaar);
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
		
		if(h.containsKey("code")) code = (String)h.get("code");
		if(h.containsKey("scheidingX")) scheidingX = ((Integer)h.get("scheidingX")).intValue();
		
		javaLogoSchuifVeld.importeer(code);
		this.scheidingX = scheidingX;
		setBounds(getBounds());
	}
	
	@Override
	public void setEditState(Hashtable h) {
		Hashtable state = null;
		Hashtable antwoordModel = null;
		boolean uitvoerVeldZichtbaar = false;
		boolean programmaVeldZichtbaar = false;
		int scoreMax = 0;
		
		if(h.containsKey("state")) state = (Hashtable) h.get("state");
		if(h.containsKey("uitvoerVeldZichtbaar")) uitvoerVeldZichtbaar = ((Boolean)h.get("uitvoerVeldZichtbaar"));
		if(h.containsKey("programmaVeldZichtbaar")) programmaVeldZichtbaar = ((Boolean)h.get("programmaVeldZichtbaar"));
		if(h.containsKey("deeltakenZichtbaar"))	deeltakenZichtbaar = ((Boolean)h.get("deeltakenZichtbaar"));
		if(h.containsKey("whileLoopZichtbaar")) whileLoopZichtbaar = ((Boolean)h.get("whileLoopZichtbaar"));
		if(h.containsKey("printCommandsZichtbaar")) printCommandsZichtbaar = ((Boolean)h.get("printCommandsZichtbaar"));
		if(h.containsKey("tekenCommandsZichtbaar")) tekenCommandsZichtbaar = ((Boolean)h.get("tekenCommandsZichtbaar"));
		if(h.containsKey("traceZichtbaar")) traceZichtbaar = ((Boolean)h.get("traceZichtbaar"));
		if(h.containsKey("runZichtbaar")) runZichtbaar = ((Boolean)h.get("runZichtbaar"));
		if(h.containsKey("codeIOZichtbaar")) codeIOZichtbaar = ((Boolean)h.get("codeIOZichtbaar"));
				
		zetUitvoerVeldZichtbaar(uitvoerVeldZichtbaar);
		zetProgrammaVeldZichtbaar(programmaVeldZichtbaar);
		zetDeeltaken(deeltakenZichtbaar);
		zetWhileLoopZichtbaar(whileLoopZichtbaar);
		zetPrintCommandsZichtbaar(printCommandsZichtbaar);
		zetTekenCommandsZichtbaar(tekenCommandsZichtbaar);
		zetTraceZichtbaar(traceZichtbaar);
		zetCodeIOZichtbaar(codeIOZichtbaar);
		zetRunZichtbaar(runZichtbaar);
		setState(state);
		
	}

	@Override
	public Hashtable getState()
	{	String code = "";
		int scheidingX = 615;
	
		code = javaLogoSchuifVeld.getCode();
		scheidingX = this.scheidingX;
		 
	    Hashtable h = new Hashtable();
	    h.put("code", code);
	    h.put("scheidingX", new Integer(scheidingX));
	    
	    return h;
	}
	
	

	@Override
	public Hashtable getEditState() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public InteractieEditPanel getEditPanel() {
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
	public void mousePressed(MouseEvent e) {
		if((new Rectangle(scheidingX, 5, 10, getHeight()-78)).contains(e.getX(), e.getY()))
			draggingScheidingX = true;
		else
			draggingScheidingX = false;
		dragStartX = e.getX();
		dragStartY = e.getY();
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		draggingScheidingX = false;
	}

	@Override
	public void mouseDragged(MouseEvent e) {
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
	public void mouseMoved(MouseEvent e) {
		if((new Rectangle(scheidingX, 5, 10, getHeight()-78)).contains(e.getX(), e.getY()) && !draggingScheidingX)
			setCursor(new Cursor(Cursor.W_RESIZE_CURSOR));
		else
			setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		
	}
	
	public void zetUitvoerVeldZichtbaar(boolean b)
	{	uitvoerVeldZichtbaar = b;
		uitvoerblad.setVisible(b);
	}
	
	public void zetProgrammaVeldZichtbaar(boolean b)
	{	programmaVeldZichtbaar = b;
		javaLogoSchuifVeld.setVisible(b);
		runButton.setVisible(b);
		exportButton.setVisible(b);
		importButton.setVisible(b);
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
		trb.setVisible(b);
	}
	
	public void zetCodeIOZichtbaar(boolean b)
	{	codeIOZichtbaar = b;
		exportButton.setVisible(b);
		importButton.setVisible(b);
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
				uitvoerblad.tekenOpnieuw();
				
				
			}
		}
		if(command.startsWith("double"))
		{
			Map map = (Map)event.getParameters();
			if(map!=null && command.equals("double.input"))
			{	String name = (String)map.get("name");
				double waarde = ((Double)map.get("value")).doubleValue();
				javaLogoSchuifVeld.setInputVar(name, waarde);
				uitvoerblad.tekenOpnieuw();
			}
			else if(map!=null && command.startsWith("double.input"))
			{	String name = command.substring(command.length()-1);
				double waarde = ((Double)map.get("value")).doubleValue();
				javaLogoSchuifVeld.setInputVar(name, waarde);
				uitvoerblad.tekenOpnieuw();
			}
			else if(map==null && command.startsWith("double.input"))
			{	String message = event.getMessage();
				double waarde = Double.parseDouble(message);
				String name = command.substring(command.length()-1);
				javaLogoSchuifVeld.setInputVar(name, waarde);
				uitvoerblad.tekenOpnieuw();
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
		return JavaLogoWeb.rb.getString(CBA_PREFIX + cmd);
	}

	@Override
	public int[][] getScoreObjectives() {
		// TODO Auto-generated method stub
		return null;
	}

	public void zetRunZichtbaar(boolean selected) {
		runZichtbaar = selected;
		runButton.setVisible(selected);
	}
	
}
