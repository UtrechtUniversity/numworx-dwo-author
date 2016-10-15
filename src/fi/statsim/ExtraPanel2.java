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
	private int aantalKeerGooien = 10;
	private int aantalKeerGooienCumulatief;
	
	private JButton gooienButton;
	private JButton opnieuwButton;
	
	private DSFrequentieGrafiek grafiek;
	private DSFrequentieGrafiek grafiekCumulatief;
	
	private Font labelFont = new Font("SansSerif", Font.PLAIN, 12);
	
	private JPanel dsOperationPanel;
	private JPanel dsInstellingenPanel;
	private Color panelColor = new Color(220,220,220);
	
	private JRadioButton somOgenRadioButton;
	private JRadioButton verschilOgenRadioButton;
	
	private JLabel ogenLabel;
	private JTextField[] ogenTextFields;
	private int[] aantalOgen;
	
	private Slider speedSlider;
	private int speed = 100;
	
	StatSimInteractiePanel ssip;
	
	public ExtraPanel2(StatSimInteractiePanel ssip) {
		setLayout(null);
		this.ssip=ssip;
		setSize(500,360);
		setOpaque(true);
		setBackground(Color.white);
		
		dsOperationPanel = new JPanel();
		dsOperationPanel.setBounds(0,0,500,40);
		dsOperationPanel.setLayout(null);
		dsOperationPanel.setOpaque(true);
		dsOperationPanel.setBackground(panelColor);
		dsOperationPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		add(dsOperationPanel);
		
		speedSlider = new Slider(100, 50);
		speedSlider.setBounds(375,29,110,10);
		speedSlider.addActionListener(this);
		dsOperationPanel.add(speedSlider);
		
		aantalDSLabel = new JLabel(StatSim.rb.getString("extraAantalDobbelstenenLabel"));
		aantalDSLabel.setFont(labelFont);
		aantalDSLabel.setBounds(10,10,140,20);
		dsOperationPanel.add(aantalDSLabel);
		
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
	    		if(aantalDS>3) {
	    			aantalDS = 3;
	    			aantalDSTextField.setText(""+aantalDS);
	    		}
	    		grafiek.zetAantalDS(aantalDS);
	    		grafiekCumulatief.zetAantalDS(aantalDS);
	    		for(int i=0 ; i<19 ; i++) {
	    			resultaatDSCumulatief[i] = 0;
	    		}
	    		aantalKeerGooienCumulatief = 0;
	    		
	    		resetSomVerschilButtons();
			}
		});
		aantalDSTextField.setBounds(150,10,40,20);
		dsOperationPanel.add(aantalDSTextField);
		
		aantalKeerGooienLabel = new JLabel(StatSim.rb.getString("extraAantalKeerGooienLabel"));
		aantalKeerGooienLabel.setFont(labelFont);
		aantalKeerGooienLabel.setBounds(200,10,130,20);
		dsOperationPanel.add(aantalKeerGooienLabel);
		
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
		dsOperationPanel.add(aantalKeerGooienTextField);
		
		gooienButton = new JButton (StatSim.rb.getString("extraGooienLabel"));
		gooienButton.setMargin(new Insets(4, 0, 4, 0));
		gooienButton.setFont(labelFont);
		gooienButton.addActionListener(this);
		gooienButton.setBounds(380,10,100,19);	
		dsOperationPanel.add(gooienButton);
		
		opnieuwButton = new JButton (StatSim.rb.getString("extraOpnieuwLabel"));
		opnieuwButton.setMargin(new Insets(4, 0, 4, 0));
		opnieuwButton.setFont(labelFont);
		opnieuwButton.addActionListener(this);
		opnieuwButton.setBounds(500,10,80,20);	
		dsOperationPanel.add(opnieuwButton);
		
		grafiek = new DSFrequentieGrafiek();
		grafiek.setBounds(10,60,240,280);
		grafiek.zetYtekst(StatSim.rb.getString("extraAantalKerenGegooidLabel"));
		add(grafiek);
		
		grafiekCumulatief = new DSFrequentieGrafiek();
		grafiekCumulatief.setBounds(250,60,240,280);
		grafiekCumulatief.zetYtekst(StatSim.rb.getString("extraTotaalGegooidLabel"));
		add(grafiekCumulatief);
		
		dsInstellingenPanel = new JPanel();
		dsInstellingenPanel.setBounds(0,280,500,40);
		dsInstellingenPanel.setLayout(null);
		dsInstellingenPanel.setOpaque(true);
		dsInstellingenPanel.setBackground(panelColor);
		dsInstellingenPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		add(dsInstellingenPanel);
		
		somOgenRadioButton = new JRadioButton(StatSim.rb.getString("extraTotaalGetalLabel"));
		somOgenRadioButton.setOpaque(false);
		somOgenRadioButton.setFont(labelFont);
		somOgenRadioButton.addActionListener(this);
		somOgenRadioButton.setBounds(10,10,120,20);
		somOgenRadioButton.setSelected(true);
		dsInstellingenPanel.add(somOgenRadioButton);
		
		verschilOgenRadioButton = new JRadioButton(StatSim.rb.getString("extraVerschilGetalLabel"));
		verschilOgenRadioButton.setOpaque(false);
		verschilOgenRadioButton.setFont(labelFont);
		verschilOgenRadioButton.addActionListener(this);
		verschilOgenRadioButton.setBounds(130,10,120,20);
		dsInstellingenPanel.add(verschilOgenRadioButton);
		
		ButtonGroup buttonGroup=new ButtonGroup();
		buttonGroup.add(somOgenRadioButton);
		buttonGroup.add(verschilOgenRadioButton);
		
		ogenLabel = new JLabel(StatSim.rb.getString("extraDobbelsteenLabel"));
		ogenLabel.setFont(labelFont);
		ogenLabel.setBounds(260,10,80,20);
		dsInstellingenPanel.add(ogenLabel);
		
		ogenTextFields = new JTextField[6];
		aantalOgen = new int[6];
		for(int i=0 ; i<6 ;i++) {
			aantalOgen[i] = i+1;
			ogenTextFields[i] = new JTextField(""+aantalOgen[i]);
			ogenTextFields[i].addActionListener(this);
			ogenTextFields[i].setBounds(340+i*40,10,40,20);
			final int teller = i;
			ogenTextFields[i].addKeyListener(new KeyAdapter()
			{	public void keyReleased(KeyEvent e)	{	
					try {
						String text = ogenTextFields[teller].getText();
						aantalOgen[teller] = Integer.parseInt(text);
		    		}
		    		catch(Exception ex) {
		    			ogenTextFields[teller].setText(""+aantalOgen[teller]);
		    		}
		    		if(aantalOgen[teller]<1) {
		    			aantalOgen[teller] = 1;
		    			ogenTextFields[teller].setText(""+aantalOgen[teller]);
		    		}
		    		if(aantalOgen[teller]>6) {
		    			aantalOgen[teller] = 6;
		    			ogenTextFields[teller].setText(""+aantalOgen[teller]);
		    		}
		    		
		    		for(int i=0 ; i<19 ; i++) {
		    			resultaatDSCumulatief[i] = 0;
		    		}
		    		aantalKeerGooienCumulatief = 0;
				}
			});
			dsInstellingenPanel.add(ogenTextFields[i]);
		}
		
		resultaatDS = new int[19];
		resultaatDSCumulatief = new int[19];
	}
	
	public void wisResultaten() {
		for(int i=0 ; i<19 ; i++) {
			resultaatDS[i] = 0;
		}
		aantalGegooid = 0;
		for(int i=0 ; i<19 ; i++) {
			resultaatDSCumulatief[i] = 0;
		}
		aantalKeerGooienCumulatief = 0;
		grafiek.zetGegooid(resultaatDS);
		grafiekCumulatief.zetGegooid(resultaatDSCumulatief);
	}
	
	public void setPanelNr(int nr) {
		
	}
	
	public void setZichtbaar() {
		
	}
	
	
	public void setSize(int width, int height) {
		super.setSize(width, height);
		
		int grafiekWidth = (width-30)/2;
		if(grafiek!=null)grafiek.setBounds(10,70,grafiekWidth,height-110);
		if(grafiekCumulatief!=null)grafiekCumulatief.setBounds(grafiekWidth+20,70,grafiekWidth,height-110);
		if(dsOperationPanel!=null)dsOperationPanel.setBounds(0,0,width,40);
		if(dsInstellingenPanel!=null)dsInstellingenPanel.setBounds(0,height-40,width,40);
		if(gooienButton!=null)gooienButton.setBounds(width-220,10,100,19);
		if(speedSlider!=null)speedSlider.setBounds(width-225,29,110,10);
		if(opnieuwButton!=null)opnieuwButton.setBounds(width-100,10,80,20);
		if(ogenLabel!=null)ogenLabel.setBounds(width-340,10,80,20);
		for(int i=0 ; i<6 ;i++) {
			if(ogenTextFields!=null)ogenTextFields[i].setBounds(width-260+i*40,10,40,20);
		}
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
	}

	public void doeStap() {
		Random generator = new Random();
		
		int ogen = 0;
		for(int i=0 ; i<aantalDS ; i++) {
			double r = generator.nextDouble();
			boolean trekAf = (aantalDS==2 && verschilOgenRadioButton.isSelected() && i==0);
			
			for(int j=0 ; j<6 ; j++) {
				if(r<1.0*(j+1)/6) {
					ogen+=aantalOgen[j];
					if(trekAf)
						ogen-=2*aantalOgen[j];
					break;
				}
			}
		}
		ogen = Math.abs(ogen);
		resultaatDS[ogen]++;
		resultaatDSCumulatief[ogen]++;
		aantalGegooid++;
		grafiek.zetGegooid(resultaatDS);
		grafiekCumulatief.zetGegooid(resultaatDSCumulatief);
	}
	   
	 
    public void run()
	{ 	while (animatie!=null && aantalGegooid<aantalKeerGooien && !stopAnimatie) {
			this.doeStap();
			try{ Thread.sleep(speed); } catch (Exception e) {}
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
		wisResultaten();
		resetSomVerschilButtons();
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
		wisResultaten();
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
    	if(e.getSource()==speedSlider) {
    		speed = 201-2*speedSlider.geefStand();
    	}
    	if(e.getSource()==opnieuwButton) {
    		stopAnimatie = true;
    		for(int i=0 ; i<19 ; i++) {
    			resultaatDS[i] = 0;
    		}
    		wisResultaten();
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
    	if(e.getSource()==somOgenRadioButton) {
    		//if(somOgenRadioButton.isSelected())
    		//	return;
    		grafiek.zetSomVerschil(1);
    		grafiekCumulatief.zetSomVerschil(1);
    		wisResultaten();
    	}
    	if(e.getSource()==verschilOgenRadioButton) {
    		//if(verschilOgenRadioButton.isSelected())
    		//	return;
    		grafiek.zetSomVerschil(0);
    		grafiekCumulatief.zetSomVerschil(0);
    		wisResultaten();
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

	private void resetSomVerschilButtons() {
		
		if(aantalDS!=2) {
			somOgenRadioButton.setSelected(true);
			grafiek.zetSomVerschil(1);
			grafiekCumulatief.zetSomVerschil(1);
		}
		verschilOgenRadioButton.setVisible(aantalDS==2 ? true : false);
		somOgenRadioButton.setVisible(aantalDS!=1 ? true : false);
	}
	  
}