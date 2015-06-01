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
import java.util.Hashtable;

import javax.swing.JButton;
import javax.swing.JPanel;

import logotekenap.Tekenblad;
import logotekenap.TraceBeheerder;
import logotekenap.Uitvoerblad;
import fi.beans.base64code.StringCodeObject;
import fi.beans.wiskopdrbeans.InteractieEditPanel;
import fi.beans.wiskopdrbeans.InteractiePanel;

public class JavaLogoInteractiePanel extends JPanel implements InteractiePanel, ActionListener, MouseMotionListener, MouseListener
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
		runButton.setBounds(155, getHeight()-60, 80, 50);
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
		g.setColor(Color.WHITE);
		g.fillRect(5, 5, scheidingX-5, getHeight()-78);
		g.setColor(Color.gray);
		g.drawRect(5, 5, scheidingX-5, getHeight()-78);
	}
	
	public void setBounds(int x, int y, int b, int h)
	{
		super.setBounds(x,y,b,h);
		layoutGui();
		uitvoerblad.setBounds(scheidingX+5, 5, getWidth()-scheidingX-10, getHeight()-77);
						
		if (javaLogoSchuifVeld == null) 
		{	
			javaLogoSchuifVeld = new JavaLogoSchuifVeld(6, 6, scheidingX-6, getHeight()-79, uitvoerblad);
			javaLogoSchuifVeld.setBackground(Color.white);
			javaLogoSchuifVeld.zetGesloten(true);
			add(javaLogoSchuifVeld);
			javaLogoSchuifVeld.initialize();
			
			trb = new TraceBeheerder( (Tekenblad)uitvoerblad, javaLogoSchuifVeld);
			trb.setBounds(265, getHeight()-64, 340, 58);
			trb.setBackground(new Color(230,230,230));
			trb.addActionListener(this);
			add(trb);
			
			uitvoerblad.meldTraceBeheerder(trb);
		}
		else
		{	javaLogoSchuifVeld.setSize(scheidingX-6, getHeight()-79);
			trb.setBounds(265, getHeight()-64, 340, 58);
			uitvoerblad.repaint();
		}
	}
	
	public void actionPerformed(ActionEvent e)
	{	
		if(e.getSource()==runButton)
		{	
			uitvoerblad.tekenOpnieuw();
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
	public void zetOpdracht(Hashtable b, String[] randomVars,
			Hashtable randomValues) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setState(Hashtable h)
	{	
		String code = "";
		
		if(h.containsKey("code")) code = (String)h.get("code");
	}
	
	@Override
	public void setEditState(Hashtable b) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Hashtable getState()
	{	String code = "";
	
		code = javaLogoSchuifVeld.getCode();
		 
	    Hashtable h = new Hashtable();
	    h.put("code", code);
	    
	    return h;
	}
	
	

	@Override
	public Hashtable getEditState() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public InteractieEditPanel getEditPanel() {
		// TODO Auto-generated method stub
		return null;
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
			javaLogoSchuifVeld.setSize(javaLogoSchuifVeld.getWidth()+dx, javaLogoSchuifVeld.getHeight());
			uitvoerblad.setBounds(scheidingX+5+dx, 5, uitvoerblad.getWidth()-dx, uitvoerblad.getHeight());
			//uitvoerblad.tekenOpnieuw();
			repaint();
			dragStartX = e.getX();
		}
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		if((new Rectangle(scheidingX, 5, 10, getHeight()-78)).contains(e.getX(), e.getY()) && !draggingScheidingX)
			setCursor(new Cursor(Cursor.W_RESIZE_CURSOR));
		else
			setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		
	}

}
