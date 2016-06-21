package fi.statsim;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import org.cbook.cbookif.CBookEvent;
import org.cbook.cbookif.CBookEventHandler;
import org.cbook.cbookif.CBookEventListener;

import fi.beans.wiskopdrbeans.CBookAware;

public class Steekproef extends JPanel implements ActionListener, FocusListener, Runnable {
	
	JPanel panel1, panel2;
	JPanel panelImage;
	JPanel panelImageLognorm;
	Border border1;
	JLabel muLabel;
	JTextField muText;
	JLabel sigmaLabel;
	JTextField sigmaText;
	JLabel sigmaLabel1;
	JLabel sigmaLabel2;
	JLabel muLabel1;
	JLabel steekproefGrootteLabel;
	JTextField steekproefGrootteText;
	JButton doeSteekproef;
	JButton doeSteekproef100Keer;
	JButton wisResultaten;
	
	JTable table;
	DefaultTableModel model;
	JScrollPane pane;
	String col[]={"no.","Uitkomst"};
	    
	JTable table1;
	DefaultTableModel model1;
	JScrollPane pane1;
	String col1[]={"no.","\u03BC","\u03C3"};
	
	int experiment=0;
	double[] steekproefResultaat;
	StatSimInteractiePanel ssip;
	
	boolean showLinkerTabel=true;
	boolean showRechterTabel=true;
	boolean showInstellingen=true;
	boolean scheveVerdeling = false;
	
	public Steekproef (StatSimInteractiePanel ssip) {
		setLayout(null);
		this.setBackground(Color.white);
		this.ssip=ssip;
		
		panel1 = new JPanel();
		panel1.setLayout(null);
		panel1.setSize(420,130);
		panel1.setOpaque(false);
		
		
		panel2 = new JPanel();
		panel2.setLayout(null);
		panel2.setBounds(0,130, 420,275);
		panel2.setOpaque(false);
			

        URL imageURL = StatSimInteractiePanel.class.getResource("resources/gaussian.gif");
        URL imageURLLognorm = StatSimInteractiePanel.class.getResource("resources/scheveVerdeling.png");
        ImageIcon image = new ImageIcon(imageURL);
        ImageIcon imageLognorm = new ImageIcon(imageURLLognorm);
        
		JLabel labelImage = new JLabel("", image, JLabel.CENTER);
		JLabel labelImageLognorm = new JLabel("", imageLognorm, JLabel.CENTER);
		
		panelImage = new JPanel(new BorderLayout());
		panelImage.add( labelImage, BorderLayout.CENTER );
		panelImage.setOpaque(true);
		panelImage.setBackground(Color.red);
		
		panelImageLognorm = new JPanel(new BorderLayout());
		panelImageLognorm.setOpaque(false);
		panelImageLognorm.setVisible(false);
		panelImageLognorm.add( labelImageLognorm, BorderLayout.CENTER );				
		
		panelImage.setLocation(85,0);
		panelImage.setSize(297,104);
		
		panelImageLognorm.setLocation(45,0);
		panelImageLognorm.setSize(307,144);
		
		panel1.add(panelImage);	
		panel1.add(panelImageLognorm);	
		
		muLabel=new JLabel("\u03BC =");
		muLabel.setSize(100,20);
		muLabel.setLocation(5,30);
		muLabel.setFont(new Font("SansSerif", Font.PLAIN, 12) );
		panel1.add(muLabel);
		
		muText=new JTextField("0");
		muText.setSize(50,20);
		muText.setLocation(35,30);
		muText.addActionListener(this);
		muText.addFocusListener(this);
		panel1.add(muText);
		
		sigmaLabel=new JLabel("\u03C3 =");
		sigmaLabel.setSize(100,20);
		sigmaLabel.setLocation(5,60);
		sigmaLabel.setFont(new Font("SansSerif", Font.PLAIN, 12) );
		panel1.add(sigmaLabel);
				
		sigmaText=new JTextField("1");
		sigmaText.setSize(50,20);
		sigmaText.setLocation(35,60);
		sigmaText.addActionListener(this);
		sigmaText.addFocusListener(this);
		panel1.add(sigmaText);


		muLabel1=new JLabel("0");
		muLabel1.setSize(100,20);
		muLabel1.setLocation(238,105);
		muLabel1.setFont(new Font("SansSerif", Font.PLAIN, 12) );
		panel1.add(muLabel1);
		
		sigmaLabel1=new JLabel("-2");
		sigmaLabel1.setSize(100,20);
		sigmaLabel1.setLocation(129,105);
		sigmaLabel1.setFont(new Font("SansSerif", Font.PLAIN, 12) );
		panel1.add(sigmaLabel1);
		
		sigmaLabel2=new JLabel("2");
		sigmaLabel2.setSize(100,20);
		sigmaLabel2.setLocation(338,105);
		sigmaLabel2.setFont(new Font("SansSerif", Font.PLAIN, 12) );
		panel1.add(sigmaLabel2);
		
		this.add(panel1);
		
		steekproefGrootteLabel=new JLabel(StatSim.rb.getString("sampleSize"));
		steekproefGrootteLabel.setSize(110,20);
		steekproefGrootteLabel.setLocation(0,10);
		steekproefGrootteLabel.setFont(new Font("SansSerif", Font.PLAIN, 12) );
		panel2.add(steekproefGrootteLabel);
		
		steekproefGrootteText=new JTextField("10");
		steekproefGrootteText.setSize(50,20);
		steekproefGrootteText.setLocation(110,10);
		steekproefGrootteText.addActionListener(this);
		steekproefGrootteText.addFocusListener(this);
		panel2.add(steekproefGrootteText);
		
		doeSteekproef=new JButton(StatSim.rb.getString("getSample"));
		doeSteekproef.setFont(new Font("SansSerif", Font.PLAIN, 12) );
	    doeSteekproef.setSize(100,20);
	    doeSteekproef.setLocation(170,10);
	    doeSteekproef.addActionListener(this);
	    panel2.add(doeSteekproef);

		doeSteekproef100Keer=new JButton(StatSim.rb.getString("getSample100Times"));
		doeSteekproef100Keer.setFont(new Font("SansSerif", Font.PLAIN, 12) );
	    doeSteekproef100Keer.setSize(130,20);
	    doeSteekproef100Keer.setLocation(280,10);
	    doeSteekproef100Keer.addActionListener(this);
	    panel2.add(doeSteekproef100Keer);
	    
		wisResultaten=new JButton(StatSim.rb.getString("erase"));
		wisResultaten.setFont(new Font("SansSerif", Font.PLAIN, 12) );
		wisResultaten.setSize(160,20);
		wisResultaten.setLocation(0,panel2.getHeight()-30);
		wisResultaten.addActionListener(this);
		wisResultaten.setEnabled(false);
		panel2.add(wisResultaten);
	    
		this.add(panel2);
	    
	    //col[0]=StatSim.rb.getString("exp");
	    col[1]=StatSim.rb.getString("value");
	    
	    model = new DefaultTableModel(col,1000); 
	    table=new JTable(model){@Override
	    	public boolean isCellEditable(int arg0, int arg1) {
	         
	            return false;
	        }};
	        
	    TableColumn column = null;
	    column = table.getColumnModel().getColumn(0);
	    column.setPreferredWidth(33); //third column is bigger
	    //for (int i=1;i<7;i++) {
	    //	column = table.getColumnModel().getColumn(i);
	    //	column.setPreferredWidth(24); //third column is bigger
	    //}
	    //table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
	    pane = new JScrollPane(table);
	   
	    panel2.add(pane);
	    pane.setLocation(0,40);
	    pane.setSize(200,235);
	    
	    model1 = new DefaultTableModel(col1,1000); 
	    table1=new JTable(model1){@Override
	    	public boolean isCellEditable(int arg0, int arg1) {
	         
	            return false;
	        }};
	        
	    TableColumn column1 = null;
	    column1 = table.getColumnModel().getColumn(0);
	    column1.setPreferredWidth(33); //third column is bigger
	    //for (int i=1;i<7;i++) {
	    //	column = table.getColumnModel().getColumn(i);
	    //	column.setPreferredWidth(24); //third column is bigger
	    //}
	    //table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
	    pane1 = new JScrollPane(table1);
	   
	    panel2.add(pane1);
	    pane1.setLocation(210,40);
	    pane1.setSize(200,235);
	    
	    steekproefResultaat = new double[1000];
	    
	    updateGraph();
	}
	
	public void setZichtbaar() {
		if (showLinkerTabel==true) {
			pane.setVisible(true);
			wisResultaten.setLocation(0,panel2.getHeight()-30);
			if (showRechterTabel==true) {
				pane1.setVisible(true);
				pane1.setLocation(210,40);
			} else {
				pane1.setVisible(false);
			}
		} else {
			pane.setVisible(false);
			if (showRechterTabel==true) {
				pane1.setVisible(true);
				pane1.setLocation(0,40);
				wisResultaten.setLocation(0,panel2.getHeight()-30);
			} else {
				wisResultaten.setLocation(0,40);
				pane1.setVisible(false);
			}
		}	
		
		panel1.setVisible(showInstellingen);
		panel2.setLocation(0, showInstellingen ? 130 : 0);
		
		if(scheveVerdeling)
		{
			panelImage.setVisible(false);
			panelImageLognorm.setVisible(true);
			muLabel.setVisible(false);
			muText.setVisible(false);
			sigmaLabel.setVisible(false);
			sigmaText.setVisible(false);
			muLabel1.setVisible(false);
			sigmaLabel1.setVisible(false);
			sigmaLabel2.setVisible(false);
		}
		else
		{
			panelImageLognorm.setVisible(false);
			panelImage.setVisible(true);
			muLabel.setVisible(true);
			muText.setVisible(true);
			sigmaLabel.setVisible(true);
			sigmaText.setVisible(true);
			muLabel1.setVisible(true);
			sigmaLabel1.setVisible(true);
			sigmaLabel2.setVisible(true);
		}
		panel1.repaint();
		
		updateGraph();
	}
	
	public void setSize(int width, int height) {
		super.setSize(width, height);
		pane.setSize(200,this.getHeight()-210);
		table.setSize(200,this.getHeight()-210);
		pane1.setSize(200,this.getHeight()-210);
		table1.setSize(200,this.getHeight()-210);
		panel1.setSize(width,130);
		panel2.setSize(width,getHeight()- (showInstellingen ? 130 : 0));
		wisResultaten.setLocation(0,panel2.getHeight()-30);	
		setZichtbaar();
	}
	

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void focusGained(FocusEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void focusLost(FocusEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	public void updateGraph() {
		muLabel1.setText(""+Double.parseDouble(muText.getText()));
		sigmaLabel1.setText(""+(Double.parseDouble(sigmaText.getText())*-2+Double.parseDouble(muText.getText())));
		sigmaLabel2.setText(""+(Double.parseDouble(sigmaText.getText())*2+Double.parseDouble(muText.getText())));
	}

	public void getSample() {
			for (int i=0;i<1000;i++) {
				table.setValueAt("",i,0);
				table.setValueAt("",i,1);				
			}
			
			Random generator = new Random();
			
			double muResultaat=0;
			
			int steekproefGrootte=Integer.parseInt(steekproefGrootteText.getText());
			
			for (int i=0;i<steekproefGrootte;i++) {
				double r = generator.nextGaussian();
				if(scheveVerdeling)
					r=Math.pow(1.7,r);
				r=r*Double.parseDouble(sigmaText.getText())+Double.parseDouble(muText.getText());
				
				steekproefResultaat[i]=r;
				muResultaat=muResultaat+r;

				r=((double)Math.round(r*100))/100;
				table.setValueAt(i+1,i,0);
				table.setValueAt(r,i,1);
				
			}
			muResultaat=muResultaat/steekproefGrootte;
			
			double dummy=0;
			for (int i=0;i<steekproefGrootte;i++) {
				dummy=dummy+Math.pow(steekproefResultaat[i]-muResultaat, 2);
			}
			dummy=dummy/steekproefGrootte;
			double sigmaResultaat=0;
			
			sigmaResultaat=Math.sqrt(dummy);
			
			sigmaResultaat=((double)Math.round(sigmaResultaat*100))/100;
			muResultaat=((double)Math.round(muResultaat*100))/100;
			table1.setValueAt(experiment+1,experiment,0);
			table1.setValueAt(muResultaat,experiment,1);
			table1.setValueAt(sigmaResultaat,experiment,2);
			
			experiment++;
			doeSteekproef.setEnabled(true);
			doeSteekproef100Keer.setEnabled(true);
			
		}
	
	public void fireCBook() {
		String string1="";
		for (int i=0;i<Integer.parseInt(steekproefGrootteText.getText());i++) {
			string1=string1+table.getValueAt(i, 1)+"\n";
		}
		String string2="";
		for (int i=0;i<experiment;i++) {
			string2=string2+table1.getValueAt(i, 1)+";"+table1.getValueAt(i, 2)+"\n";
		}
		
		
		ssip.fireCBookSteekproef(string1,string2);		
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (e.getSource()==doeSteekproef) {
			doeSteekproef.setEnabled(false);
			doeSteekproef100Keer.setEnabled(false);
			muText.setEnabled(false);
			sigmaText.setEnabled(false);
			
			wisResultaten.setEnabled(true);
			getSample();
			
			fireCBook();

		}
		if (e.getSource()==doeSteekproef100Keer) {
			doeSteekproef.setEnabled(false);
			doeSteekproef100Keer.setEnabled(false);
			muText.setEnabled(false);
			sigmaText.setEnabled(false);

			wisResultaten.setEnabled(true);
			for (int i=0;i<100;i++) {
				getSample();
			}
			fireCBook();
		}
		if (e.getSource()==wisResultaten) {
			doeSteekproef.setEnabled(true);
			doeSteekproef100Keer.setEnabled(true);
			muText.setEnabled(true);
			sigmaText.setEnabled(true);

			wisResultaten.setEnabled(false);
			experiment=0;
			for (int i=0;i<1000;i++) {
			   table.setValueAt("",i,0);
			   table.setValueAt("",i,1);
			   table1.setValueAt("",i,0);
			   table1.setValueAt("",i,1);
			   table1.setValueAt("",i,2);
			}
			fireCBook();
		}
		if (e.getSource()==muText) {
			updateGraph();
		}
		if (e.getSource()==sigmaText) {
			updateGraph();
		}

	}

}
