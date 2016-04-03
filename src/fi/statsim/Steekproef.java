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
	
	JPanel panel1;
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
	
	public Steekproef (StatSimInteractiePanel ssip) {
		setLayout(null);
		this.setBackground(Color.white);
		this.ssip=ssip;
		

        URL imageURL = StatSimInteractiePanel.class.getResource("resources/gaussian.gif");
        ImageIcon image = new ImageIcon(imageURL);
		JLabel labelImage = new JLabel("", image, JLabel.CENTER);
		JPanel panelImage = new JPanel(new BorderLayout());
		panelImage.add( labelImage, BorderLayout.CENTER );
		
		this.add(panelImage);
		panelImage.setLocation(85,0);
		panelImage.setSize(297,104);
		
		
		muLabel=new JLabel("\u03BC =");
		muLabel.setSize(100,20);
		muLabel.setLocation(5,30);
		muLabel.setFont(new Font("SansSerif", Font.PLAIN, 12) );
		this.add(muLabel);
		
		muText=new JTextField("0");
		muText.setSize(50,20);
		muText.setLocation(35,30);
		muText.addActionListener(this);
		muText.addFocusListener(this);
		this.add(muText);
		
		sigmaLabel=new JLabel("\u03C3 =");
		sigmaLabel.setSize(100,20);
		sigmaLabel.setLocation(5,60);
		sigmaLabel.setFont(new Font("SansSerif", Font.PLAIN, 12) );
		this.add(sigmaLabel);
				
		sigmaText=new JTextField("1");
		sigmaText.setSize(50,20);
		sigmaText.setLocation(35,60);
		sigmaText.addActionListener(this);
		sigmaText.addFocusListener(this);
		this.add(sigmaText);


		muLabel1=new JLabel("0");
		muLabel1.setSize(100,20);
		muLabel1.setLocation(238,105);
		muLabel1.setFont(new Font("SansSerif", Font.PLAIN, 12) );
		this.add(muLabel1);
		
		sigmaLabel1=new JLabel("-2");
		sigmaLabel1.setSize(100,20);
		sigmaLabel1.setLocation(129,105);
		sigmaLabel1.setFont(new Font("SansSerif", Font.PLAIN, 12) );
		this.add(sigmaLabel1);
		
		sigmaLabel2=new JLabel("2");
		sigmaLabel2.setSize(100,20);
		sigmaLabel2.setLocation(338,105);
		sigmaLabel2.setFont(new Font("SansSerif", Font.PLAIN, 12) );
		this.add(sigmaLabel2);
		
		steekproefGrootteLabel=new JLabel(StatSim.rb.getString("sampleSize"));
		steekproefGrootteLabel.setSize(110,20);
		steekproefGrootteLabel.setLocation(0,140);
		steekproefGrootteLabel.setFont(new Font("SansSerif", Font.PLAIN, 12) );
		this.add(steekproefGrootteLabel);
		
		steekproefGrootteText=new JTextField("10");
		steekproefGrootteText.setSize(50,20);
		steekproefGrootteText.setLocation(110,140);
		steekproefGrootteText.addActionListener(this);
		steekproefGrootteText.addFocusListener(this);
		this.add(steekproefGrootteText);
		
		doeSteekproef=new JButton(StatSim.rb.getString("getSample"));
		doeSteekproef.setFont(new Font("SansSerif", Font.PLAIN, 12) );
	    doeSteekproef.setSize(100,20);
	    doeSteekproef.setLocation(170,140);
	    doeSteekproef.addActionListener(this);
	    this.add(doeSteekproef);

		doeSteekproef100Keer=new JButton(StatSim.rb.getString("getSample100Times"));
		doeSteekproef100Keer.setFont(new Font("SansSerif", Font.PLAIN, 12) );
	    doeSteekproef100Keer.setSize(130,20);
	    doeSteekproef100Keer.setLocation(280,140);
	    doeSteekproef100Keer.addActionListener(this);
	    this.add(doeSteekproef100Keer);
	    
		wisResultaten=new JButton(StatSim.rb.getString("erase"));
		wisResultaten.setFont(new Font("SansSerif", Font.PLAIN, 12) );
		wisResultaten.setSize(160,20);
		wisResultaten.setLocation(0,this.getHeight()-30);
		wisResultaten.addActionListener(this);
		wisResultaten.setEnabled(false);
	    this.add(wisResultaten);
	    
	    
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
	   
	    add(pane);
	    pane.setLocation(0,170);
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
	   
	    add(pane1);
	    pane1.setLocation(210,170);
	    pane1.setSize(200,235);
	    
	    steekproefResultaat = new double[1000];
	    
	    updateGraph();
	}
	
	public void setZichtbaar() {
		if (showLinkerTabel==true) {
			pane.setVisible(true);
			wisResultaten.setLocation(0,this.getHeight()-30);
			if (showRechterTabel==true) {
				pane1.setVisible(true);
				pane1.setLocation(210,170);
			} else {
				pane1.setVisible(false);
			}
		} else {
			pane.setVisible(false);
			if (showRechterTabel==true) {
				pane1.setVisible(true);
				pane1.setLocation(0,170);
				wisResultaten.setLocation(0,this.getHeight()-30);
			} else {
				wisResultaten.setLocation(0,170);
				pane1.setVisible(false);
			}
		}		
		updateGraph();
	}
	
	public void setSize(int width, int height) {
		super.setSize(width, height);
		pane.setSize(200,this.getHeight()-210);
		table.setSize(200,this.getHeight()-210);
		pane1.setSize(200,this.getHeight()-210);
		table1.setSize(200,this.getHeight()-210);
		
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
