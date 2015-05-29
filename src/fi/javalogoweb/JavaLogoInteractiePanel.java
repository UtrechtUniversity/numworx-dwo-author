package fi.javalogoweb;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;

import javax.swing.JButton;
import javax.swing.JPanel;

import logotekenap.Tekenblad;
import logotekenap.TraceBeheerder;
import logotekenap.Uitvoerblad;
import fi.beans.base64code.StringCodeObject;
import fi.beans.wiskopdrbeans.InteractieEditPanel;
import fi.beans.wiskopdrbeans.InteractiePanel;

public class JavaLogoInteractiePanel extends JPanel implements InteractiePanel, ActionListener
{
	private JavaLogoSchuifVeld javaLogoSchuifVeld;
	private Uitvoerblad uitvoerblad;
	private TraceBeheerder trb;
	
	private JButton runButton;
	private JButton exportButton;
	private JButton importButton;
	
	public static final Font defaultfont = new Font("Calibri", Font.PLAIN, 12);
	public static final Font boldfont = new Font("Verdana", Font.BOLD, 12);
	
	public JavaLogoInteractiePanel()
	{
		setLayout(null);
		uitvoerblad = new Tekenblad(this);
		add(uitvoerblad);
		
		runButton = new JButton(JavaLogoWeb.rb.getString("runButtonLabel"));
		runButton.setBounds(240, 540, 80, 50);
		runButton.setFont(JavaLogoWeb.boldfont);
		runButton.setMargin(new Insets(3,5,3,5));
		runButton.addActionListener(this);
		add(runButton,0);
		
		importButton = new JButton(JavaLogoWeb.rb.getString("importButtonLabel"));
		importButton.setBounds(10, 540, 120, 23);
		importButton.setFont(JavaLogoWeb.boldfont);
		importButton.setMargin(new Insets(2,5,2,5));
		importButton.addActionListener(this);
		add(importButton,0);
		
		exportButton = new JButton(JavaLogoWeb.rb.getString("exportButtonLabel"));
		exportButton.setBounds(10, 567, 120, 23);
		exportButton.setFont(JavaLogoWeb.boldfont);
		exportButton.setMargin(new Insets(2,5,2,5));
		exportButton.addActionListener(this);
		add(exportButton,0);
	}
	
	public void execprogramma()
	{	javaLogoSchuifVeld.execute(uitvoerblad);
		
	}
	
	public void paintComponent(Graphics g)
	{
		g.setColor(new Color(230,230,230));
		g.fillRect(0, 530, getWidth(), 70);
		g.fillRect(610, 0, getWidth()-610, getHeight());
		g.setColor(Color.gray);
		g.drawLine(0, 530, 610, 530);
		g.drawLine(610, 530, 610, 0);
	}
	
	public void setBounds(int x, int y, int b, int h)
	{
		super.setBounds(x,y,b,h);
		uitvoerblad.setBounds(620, 5, getWidth()-625, getHeight()-75);
		
		
		if (javaLogoSchuifVeld == null) 
		{	
			javaLogoSchuifVeld = new JavaLogoSchuifVeld(0, 0, 610, getHeight()-71, uitvoerblad);
			javaLogoSchuifVeld.setBackground(Color.white);
			add(javaLogoSchuifVeld);
			javaLogoSchuifVeld.initialize();
			
			trb = new TraceBeheerder( (Tekenblad)uitvoerblad, javaLogoSchuifVeld);
			trb.setBounds(620, getHeight()-65, getWidth()-631, 58);
			trb.setBackground(new Color(230,230,230));
			trb.addActionListener(this);
			add(trb);
			
			uitvoerblad.meldTraceBeheerder(trb);
		}
		else
		{	javaLogoSchuifVeld.setSize(b, h);
					
		}
		javaLogoSchuifVeld.tekenOpnieuw();
	}
	
	public void actionPerformed(ActionEvent e)
	{	
		if(e.getSource()==runButton)
		{	//uitvoerblad.setVisible(true);
			uitvoerblad.tekenOpnieuw();
		}
		else if(e.getSource()==importButton)
		{	javaLogoSchuifVeld.importFrame();
		}
		else if(e.getSource()==exportButton)
		{	javaLogoSchuifVeld.exportFrame(javaLogoSchuifVeld.getCode());
		}
		javaLogoSchuifVeld.tekenOpnieuw();
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
		
		//javaLogoSchuifVeld.setCode(code);
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

}
