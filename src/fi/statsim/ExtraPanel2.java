package fi.statsim;

import java.awt.Button;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import fi.beans.wiskopdrbeans.InteractieEditPanel;
import fi.beans.wiskopdrbeans.InteractiePanel;
//import fi.statistiek.Statistiek;

public class ExtraPanel2 extends JPanel implements ActionListener, FocusListener, Runnable {	
	private Thread animatie;
	private boolean stopAnimatie = false;
	private int aantalGegooid = 0;
	private int[] resultaatDS;
	private int[] resultaatDSCumulatief;
	
	private JLabel aantalDSLabel;
	private JTextField aantalDSTextField;
	private int aantalDS;
	
	private JLabel aantalKeerGooienLabel;
	private JTextField aantalKeerGooienTextField;
	private int aantalKeerGooien;
	private int aantalKeerGooienCumulatief;
	
	private JButton gooienButton;
	private JButton opnieuwButton;
	
	private DSFrequentieGrafiek grafiek;
	private DSFrequentieGrafiek grafiekCumulatief;
	
	private Font labelFont = new Font("SansSerif", Font.PLAIN, 12);
	
	StatSimInteractiePanel ssip;
	
	public ExtraPanel2(StatSimInteractiePanel ssip) {
		setLayout(null);
		this.ssip=ssip;
		setSize(500,360);
		setOpaque(true);
		setBackground(Color.white);
		
		aantalDSLabel = new JLabel("Aantal dobbelstenen =");
		aantalDSLabel.setFont(labelFont);
		aantalDSLabel.setBounds(10,10,130,20);
		add(aantalDSLabel);
		
		aantalDS = 2;
		aantalDSTextField = new JTextField(""+aantalDS);
		aantalDSTextField.setFont(labelFont);
		aantalDSTextField.addActionListener(this);
		aantalDSTextField.addFocusListener(this);
		aantalDSTextField.addKeyListener(new KeyAdapter()
		{	public void keyReleased(KeyEvent e)
			{	try {
				aantalDS = Integer.parseInt(aantalDSTextField.getText());
	    		}
	    		catch(Exception ex) {
	    			aantalDS = 1;
	    			//aantalMuntenTextField.setText(""+aantalMunten);
	    		}
	    		if(aantalDS<1) {
	    			aantalDS = 1;
	    			aantalDSTextField.setText(""+aantalDS);
	    		}
	    		if(aantalDS>4) {
	    			aantalDS = 4;
	    			aantalDSTextField.setText(""+aantalDS);
	    		}
	    		grafiek.zetAantalDS(aantalDS);
	    		grafiekCumulatief.zetAantalDS(aantalDS);
	    		for(int i=0 ; i<19 ; i++) {
	    			resultaatDSCumulatief[i] = 0;
	    		}
	    		aantalKeerGooienCumulatief = 0;
			}
		});
		aantalDSTextField.setBounds(140,10,40,20);
		add(aantalDSTextField);
		
		aantalKeerGooienLabel = new JLabel("Aantal keer gooien =");
		aantalKeerGooienLabel.setFont(labelFont);
		aantalKeerGooienLabel.setBounds(200,10,130,20);
		add(aantalKeerGooienLabel);
		
		aantalKeerGooien = 10;
		aantalKeerGooienTextField = new JTextField(""+aantalKeerGooien);
		aantalKeerGooienTextField.setFont(labelFont);
		aantalKeerGooienTextField.addActionListener(this);
		aantalKeerGooienTextField.addKeyListener(new KeyAdapter()
		{	public void keyReleased(KeyEvent e)
			{	String s = aantalKeerGooienTextField.getText();
	    		try {
	    			aantalKeerGooien = Integer.parseInt(s);
	    		}
	    		catch(Exception ex) {
	    			aantalKeerGooien = 10;
	    			//aantalKeerGooienTextField.setText(""+aantalKeerGooien);
	    		}
	    		if(aantalKeerGooien<1) {
	    			aantalKeerGooien = 1;
	    			aantalKeerGooienTextField.setText(""+aantalKeerGooien);
	    		}
	    		if(aantalKeerGooien>1000) {
	    			aantalKeerGooien = 1000;
	    			aantalKeerGooienTextField.setText(""+aantalKeerGooien);
	    		}
	    		grafiek.zetAantalKeerGooien(aantalKeerGooien);
	    		grafiekCumulatief.zetAantalKeerGooien(aantalKeerGooien);
	    		for(int i=0 ; i<19 ; i++) {
	    			resultaatDSCumulatief[i] = 0;
	    		}
	    		aantalKeerGooienCumulatief = 0;
			}
		});
		aantalKeerGooienTextField.setBounds(320,10,40,20);
		add(aantalKeerGooienTextField);
		
		gooienButton = new JButton ("gooien");
		gooienButton.setMargin(new Insets(4, 0, 4, 0));
		gooienButton.setFont(labelFont);
		gooienButton.addActionListener(this);
		gooienButton.setBounds(380,10,80,20);	
		add(gooienButton);
		
		opnieuwButton = new JButton ("opnieuw");
		opnieuwButton.setMargin(new Insets(4, 0, 4, 0));
		opnieuwButton.setFont(labelFont);
		opnieuwButton.addActionListener(this);
		opnieuwButton.setBounds(480,10,80,20);	
		add(opnieuwButton);
		
		grafiek = new DSFrequentieGrafiek();
		grafiek.setBounds(10,60,240,280);
		grafiek.zetYtekst("Aantal keren gegooid");
		add(grafiek);
		
		grafiekCumulatief = new DSFrequentieGrafiek();
		grafiekCumulatief.setBounds(250,60,240,280);
		grafiekCumulatief.zetYtekst("Totaal aantal keren gegooid");
		add(grafiekCumulatief);
		
		resultaatDS = new int[19];
		resultaatDSCumulatief = new int[19];
	}
	
	public void setPanelNr(int nr) {
		
	}
	
	public void setZichtbaar() {
		
	}
	
	
	public void setSize(int width, int height) {
		super.setSize(width, height);
		
		int grafiekWidth = (width-30)/2;
		if(grafiek!=null)grafiek.setBounds(10,60,grafiekWidth,height-60);
		if(grafiekCumulatief!=null)grafiekCumulatief.setBounds(grafiekWidth+20,60,grafiekWidth,height-60);
		repaint();
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
	}

	public void doeStap() {
		Random generator = new Random();
		
		
		int ogen = 0;
		for(int i=0 ; i<aantalDS ; i++) {
			double r = generator.nextDouble();
			if(r<1.0/6) {
				ogen+=1;
			}
			else if(r<2.0/6) {
				ogen+=2;
			}
			else if(r<3.0/6) {
				ogen+=3;
			}
			else if(r<4.0/6) {
				ogen+=4;
			}
			else if(r<5.0/6) {
				ogen+=5;
			}
			else {
				ogen+=6;
			}
		}
		//System.out.println("Ogen:"+ogen);
		resultaatDS[ogen]++;
		//System.out.println("OgenRij:"+resultaatDS.toString());
		resultaatDSCumulatief[ogen]++;
		aantalGegooid++;
		grafiek.zetGegooid(resultaatDS);
		grafiekCumulatief.zetGegooid(resultaatDSCumulatief);
	}
	   
	 
    public void run()
	{ 	while (animatie!=null && aantalGegooid<aantalKeerGooien && !stopAnimatie) {
			this.doeStap();
			try{ Thread.sleep(200); } catch (Exception e) {}
		}
		gooienButton.setEnabled(true);
		aantalDSTextField.setEnabled(true);
		aantalKeerGooienTextField.setEnabled(true);
	}
    
    private void procesMuntenField() {
    	String s = aantalDSTextField.getText();
		try {
			aantalDS = Integer.parseInt(s);
		}
		catch(Exception ex) {
			aantalDS = 1;
			aantalDSTextField.setText(""+aantalDS);
		}
		if(aantalDS<1) {
			aantalDS = 1;
			aantalDSTextField.setText(""+aantalDS);
		}
		if(aantalDS>4) {
			aantalDS = 4;
			aantalDSTextField.setText(""+aantalDS);
		}
		grafiek.zetAantalDS(aantalDS);
		grafiekCumulatief.zetAantalDS(aantalDS);
		for(int i=0 ; i<19 ; i++) {
			resultaatDSCumulatief[i] = 0;
		}
    }
    
    private void procesAantalGooienField() {
    	String s = aantalKeerGooienTextField.getText();
		try {
			aantalKeerGooien = Integer.parseInt(s);
		}
		catch(Exception ex) {
			aantalKeerGooien = 10;
			aantalKeerGooienTextField.setText(""+aantalKeerGooien);
		}
		if(aantalKeerGooien<1) {
			aantalKeerGooien = 1;
			aantalKeerGooienTextField.setText(""+aantalKeerGooien);
		}
		if(aantalKeerGooien>1000) {
			aantalKeerGooien = 1000;
			aantalKeerGooienTextField.setText(""+aantalKeerGooien);
		}
		grafiek.zetAantalKeerGooien(aantalKeerGooien);
		grafiekCumulatief.zetAantalKeerGooien(aantalKeerGooien);
		for(int i=0 ; i<19 ; i++) {
			resultaatDSCumulatief[i] = 0;
		}
		aantalKeerGooienCumulatief = 0;
    }
 
    public void actionPerformed(ActionEvent e) {
    	if(e.getSource()==gooienButton) {
    		gooienButton.setEnabled(false);
    		aantalDSTextField.setEnabled(false);
    		aantalKeerGooienTextField.setEnabled(false);
    		for(int i=0 ; i<19 ; i++) {
    			resultaatDS[i] = 0;
    		}
    		aantalGegooid = 0;
    		
    		aantalKeerGooienCumulatief += aantalKeerGooien;
    		grafiekCumulatief.zetAantalKeerGooien(aantalKeerGooienCumulatief);
    		
    		stopAnimatie = false;
    		animatie=new Thread(this);
  		   	animatie.start(); 
    	}
    	if(e.getSource()==opnieuwButton) {
    		stopAnimatie = true;
    		for(int i=0 ; i<19 ; i++) {
    			resultaatDS[i] = 0;
    		}
    		for(int i=0 ; i<19 ; i++) {
    			resultaatDSCumulatief[i] = 0;
    		}
    		
    		aantalKeerGooienCumulatief = 0;
    		aantalGegooid = 0;
    		
    		grafiek.zetAantalKeerGooien(aantalKeerGooien);
    		grafiekCumulatief.zetAantalKeerGooien(aantalKeerGooien);
    	}
    	if(e.getSource()==aantalDSTextField) {
    		procesMuntenField();
    	}
    	if(e.getSource()==aantalKeerGooienTextField) {
    		procesAantalGooienField();
    	}
    }

	@Override
	public void focusGained(FocusEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void focusLost(FocusEvent e) {
		if(e.getSource()==aantalDSTextField) {
    		procesMuntenField();
    	}
    	if(e.getSource()==aantalKeerGooienTextField) {
    		procesAantalGooienField();
    	}
		
	}
	  
}