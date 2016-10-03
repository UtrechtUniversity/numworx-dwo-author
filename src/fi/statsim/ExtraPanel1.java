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

public class ExtraPanel1 extends JPanel implements ActionListener, FocusListener, Runnable {	
	private Thread animatie;
	private boolean stopAnimatie = false;
	private int aantalGegooid = 0;
	private int[] resultaatMunt;
	private int[] resultaatMuntCumulatief;
	
	private JLabel aantalMuntenLabel;
	private JTextField aantalMuntenTextField;
	private int aantalMunten;
	
	private JLabel aantalKeerGooienLabel;
	private JTextField aantalKeerGooienTextField;
	private int aantalKeerGooien;
	private int aantalKeerGooienCumulatief;
	
	private JButton gooienButton;
	private JButton opnieuwButton;
	
	private MuntenFrequentieGrafiek grafiek;
	private MuntenFrequentieGrafiek grafiekCumulatief;
	
	private Font labelFont = new Font("SansSerif", Font.PLAIN, 12);
	
	StatSimInteractiePanel ssip;
	
	public ExtraPanel1(StatSimInteractiePanel ssip) {
		setLayout(null);
		this.ssip=ssip;
		setSize(500,360);
		setOpaque(true);
		setBackground(Color.white);
		
		aantalMuntenLabel = new JLabel("Aantal munten =");
		aantalMuntenLabel.setFont(labelFont);
		aantalMuntenLabel.setBounds(10,10,100,20);
		add(aantalMuntenLabel);
		
		aantalMunten = 2;
		aantalMuntenTextField = new JTextField(""+aantalMunten);
		aantalMuntenTextField.setFont(labelFont);
		aantalMuntenTextField.addActionListener(this);
		aantalMuntenTextField.addFocusListener(this);
		aantalMuntenTextField.addKeyListener(new KeyAdapter()
		{	public void keyReleased(KeyEvent e)
			{	try {
    				aantalMunten = Integer.parseInt(aantalMuntenTextField.getText());
	    		}
	    		catch(Exception ex) {
	    			aantalMunten = 1;
	    			//aantalMuntenTextField.setText(""+aantalMunten);
	    		}
	    		if(aantalMunten<1) {
	    			aantalMunten = 1;
	    			aantalMuntenTextField.setText(""+aantalMunten);
	    		}
	    		if(aantalMunten>4) {
	    			aantalMunten = 4;
	    			aantalMuntenTextField.setText(""+aantalMunten);
	    		}
	    		grafiek.zetAantalMunten(aantalMunten);
	    		grafiekCumulatief.zetAantalMunten(aantalMunten);
	    		resultaatMuntCumulatief[0] = 0;
	    		resultaatMuntCumulatief[1] = 0;
	    		resultaatMuntCumulatief[2] = 0;
	    		resultaatMuntCumulatief[3] = 0;
	    		resultaatMuntCumulatief[4] = 0;
	    		aantalKeerGooienCumulatief = 0;
			}
		});
		aantalMuntenTextField.setBounds(110,10,40,20);
		add(aantalMuntenTextField);
		
		aantalKeerGooienLabel = new JLabel("Aantal keer gooien =");
		aantalKeerGooienLabel.setFont(labelFont);
		aantalKeerGooienLabel.setBounds(180,10,130,20);
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
	    		resultaatMuntCumulatief[0] = 0;
	    		resultaatMuntCumulatief[1] = 0;
	    		resultaatMuntCumulatief[2] = 0;
	    		resultaatMuntCumulatief[3] = 0;
	    		resultaatMuntCumulatief[4] = 0;
	    		aantalKeerGooienCumulatief = 0;
			}
		});
		aantalKeerGooienTextField.setBounds(300,10,40,20);
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
		
		grafiek = new MuntenFrequentieGrafiek();
		grafiek.setBounds(10,60,240,280);
		grafiek.zetYtekst("Aantal keren gegooid");
		add(grafiek);
		
		grafiekCumulatief = new MuntenFrequentieGrafiek();
		grafiekCumulatief.setBounds(250,60,240,280);
		grafiekCumulatief.zetYtekst("Totaal aantal keren gegooid");
		add(grafiekCumulatief);
		
		resultaatMunt = new int[5];
		resultaatMuntCumulatief = new int[5];
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
		
		
		int munt = 0;
		for(int i=0 ; i<aantalMunten ; i++) {
			double r = generator.nextDouble();
			if(r<0.5) {
				munt++;
			}
		}
		resultaatMunt[munt]++;
		resultaatMuntCumulatief[munt]++;
		aantalGegooid++;
		grafiek.zetGegooid(resultaatMunt);
		grafiekCumulatief.zetGegooid(resultaatMuntCumulatief);
	}
	   
	 
    public void run()
	{ 	while (animatie!=null && aantalGegooid<aantalKeerGooien && !stopAnimatie) {
			this.doeStap();
			try{ Thread.sleep(200); } catch (Exception e) {}
		}
		gooienButton.setEnabled(true);
		aantalMuntenTextField.setEnabled(true);
		aantalKeerGooienTextField.setEnabled(true);
	}
    
    private void procesMuntenField() {
    	String s = aantalMuntenTextField.getText();
		try {
			aantalMunten = Integer.parseInt(s);
		}
		catch(Exception ex) {
			aantalMunten = 1;
			aantalMuntenTextField.setText(""+aantalMunten);
		}
		if(aantalMunten<1) {
			aantalMunten = 1;
			aantalMuntenTextField.setText(""+aantalMunten);
		}
		if(aantalMunten>4) {
			aantalMunten = 4;
			aantalMuntenTextField.setText(""+aantalMunten);
		}
		grafiek.zetAantalMunten(aantalMunten);
		grafiekCumulatief.zetAantalMunten(aantalMunten);
		resultaatMuntCumulatief[0] = 0;
		resultaatMuntCumulatief[1] = 0;
		resultaatMuntCumulatief[2] = 0;
		resultaatMuntCumulatief[3] = 0;
		resultaatMuntCumulatief[4] = 0;
		aantalKeerGooienCumulatief = 0;
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
		resultaatMuntCumulatief[0] = 0;
		resultaatMuntCumulatief[1] = 0;
		resultaatMuntCumulatief[2] = 0;
		resultaatMuntCumulatief[3] = 0;
		resultaatMuntCumulatief[4] = 0;
		aantalKeerGooienCumulatief = 0;
    }
 
    public void actionPerformed(ActionEvent e) {
    	if(e.getSource()==gooienButton) {
    		gooienButton.setEnabled(false);
    		aantalMuntenTextField.setEnabled(false);
    		aantalKeerGooienTextField.setEnabled(false);
    		
    		resultaatMunt[0] = 0;
    		resultaatMunt[1] = 0;
    		resultaatMunt[2] = 0;
    		resultaatMunt[3] = 0;
    		resultaatMunt[4] = 0;
    		
    		aantalGegooid = 0;
    		
    		aantalKeerGooienCumulatief += aantalKeerGooien;
    		grafiekCumulatief.zetAantalKeerGooien(aantalKeerGooienCumulatief);
    		
    		stopAnimatie = false;
    		animatie=new Thread(this);
  		   	animatie.start(); 
    	}
    	if(e.getSource()==opnieuwButton) {
    		stopAnimatie = true;
    		resultaatMunt[0] = 0;
    		resultaatMunt[1] = 0;
    		resultaatMunt[2] = 0;
    		resultaatMunt[3] = 0;
    		resultaatMunt[4] = 0;
    		
    		resultaatMuntCumulatief[0] = 0;
    		resultaatMuntCumulatief[1] = 0;
    		resultaatMuntCumulatief[2] = 0;
    		resultaatMuntCumulatief[3] = 0;
    		resultaatMuntCumulatief[4] = 0;
    		
    		aantalKeerGooienCumulatief = 0;
    		aantalGegooid = 0;
    		
    		grafiek.zetAantalKeerGooien(aantalKeerGooien);
    		grafiekCumulatief.zetAantalKeerGooien(aantalKeerGooien);
    	}
    	if(e.getSource()==aantalMuntenTextField) {
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
		if(e.getSource()==aantalMuntenTextField) {
    		procesMuntenField();
    	}
    	if(e.getSource()==aantalKeerGooienTextField) {
    		procesAantalGooienField();
    	}
		
	}
	  
}