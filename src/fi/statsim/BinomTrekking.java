package fi.statsim;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

public class BinomTrekking extends JPanel implements ActionListener, Runnable{
	
	JPanel panel1;
	Border border1;
	JLabel kansLabel;
	JTextField kansText;
	JLabel aantalTrekkingenLabel;
	JTextField aantalTrekkingenText;
	JButton start;
	JButton volgende;
	JButton stop;
	JTable table;
	DefaultTableModel model;
	JScrollPane pane;
	String col[]={"exp.","Uitkomst"};
	Thread animatie;
	Boolean stopCounting;
	int maxCount;
	int experiment;
	int trekkingCount;
	int totaal;
	BinomGrafiek binomGrafiek;
	int[] trekkingen;
	
	Boolean showTabel=true;
	Boolean showGrafiek=true;
	
	public BinomTrekking () {
		setLayout(null);
		this.setBackground(Color.white);
		
		border1=BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
		panel1=new JPanel();
		panel1.setLayout(null);
		panel1.setBackground(Color.white);
		add(panel1);
		panel1.setSize(230,115);
		panel1.setLocation(0,0);
		panel1.setBorder(BorderFactory.createTitledBorder(border1,StatSim.rb.getString("settings"),TitledBorder.CENTER,TitledBorder.TOP));
		kansLabel=new JLabel(StatSim.rb.getString("chance"));
		kansLabel.setSize(100,20);
		kansLabel.setLocation(20,30);
		panel1.add(kansLabel);
		
		kansText=new JTextField("0.2");
		kansText.setSize(50,20);
		kansText.setLocation(130,30);
		panel1.add(kansText);
		
		aantalTrekkingenLabel=new JLabel(StatSim.rb.getString("numberOfDraws"));
		aantalTrekkingenLabel.setSize(100,20);
		aantalTrekkingenLabel.setLocation(20,60);
		panel1.add(aantalTrekkingenLabel);
				
		aantalTrekkingenText=new JTextField("20");
		aantalTrekkingenText.setSize(50,20);
		aantalTrekkingenText.setLocation(130,60);
		panel1.add(aantalTrekkingenText);

	    start=new JButton(StatSim.rb.getString("start"));
	    start.setSize(100,20);
	    start.setLocation(240,0);
	    start.addActionListener(this);
	    add(start);
	    
	    volgende=new JButton(StatSim.rb.getString("next"));
	    volgende.setSize(100,20);
	    volgende.setLocation(240,30);
	    volgende.addActionListener(this);
	    volgende.setEnabled(false);
	    add(volgende);
	    
	    stop=new JButton(StatSim.rb.getString("stop"));
	    stop.setSize(100,20);
	    stop.setLocation(240,60);
	    stop.addActionListener(this);
	    stop.setEnabled(false);
	    add(stop);
	
	    col[0]=StatSim.rb.getString("exp");
	    col[1]=StatSim.rb.getString("outcome");
	    
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
	    pane.setLocation(0,115);
	    pane.setSize(200,335);
	    
	    binomGrafiek=new BinomGrafiek(this);
	    binomGrafiek.setLocation(200,115);
	    binomGrafiek.setBackground(Color.white);
	    binomGrafiek.setSize(590,335);
	    add(binomGrafiek);

	    trekkingen = new int[1000];
	    maxCount=Integer.parseInt(aantalTrekkingenText.getText());
	}
	
	public void setZichtbaar() {
		if (showTabel) {
			pane.setVisible(true);
			binomGrafiek.setLocation(200,115);
			binomGrafiek.setSize(this.getWidth()-200,this.getHeight()-115);
		} else {
			pane.setVisible(false);
			binomGrafiek.setLocation(0,115);
			binomGrafiek.setSize(this.getWidth(),this.getHeight()-115);
		}
		binomGrafiek.setVisible(showGrafiek);
	}
	
	public void setSize(int width, int height) {
		super.setSize(width, height);
		if (showTabel==false)
			binomGrafiek.setSize(this.getWidth(),this.getHeight()-115);
		else
			binomGrafiek.setSize(this.getWidth()-200,this.getHeight()-115);
	}
	
	public void setStartStop() {
		if (start.isEnabled()==false) {
			kansText.setEnabled(false);
			aantalTrekkingenText.setEnabled(false);
		} else {
			kansText.setEnabled(true);
			aantalTrekkingenText.setEnabled(true);
		}
	}
	
	
	public void actionPerformed(ActionEvent e) {
		if (e.getSource()==start) {
			for (int i=0;i<100;i++) {
			   table.setValueAt("",i,0);
			   table.setValueAt("",i,1);
			}
			start.setEnabled(false);
			volgende.setEnabled(true);
			stop.setEnabled(true);
			setStartStop();
			experiment=0;
			stopCounting=false;
			trekkingCount=0;
			totaal=0;
			maxCount=Integer.parseInt(aantalTrekkingenText.getText());
			animatie=new Thread(this);
			animatie.start();   
			   
		}
		if (e.getSource()==volgende) {
			stopCounting=false;
			trekkingCount=0;
			totaal=0;
			animatie=new Thread(this);
			animatie.start();
			
		}
		if (e.getSource()==stop) {
			start.setEnabled(true);
			volgende.setEnabled(false);
			stop.setEnabled(false);
			setStartStop();
		}
	}
	
	public String replaceComma(String oldString)
	{
		String newString=oldString.replace(",",".");				
		return newString;
	}
	
	public void doeStap() {
		Random generator = new Random();
		double r = generator.nextDouble();
		
		if (r<Double.parseDouble(replaceComma(kansText.getText()))) {
			totaal=totaal+1;
		}
		trekkingCount=trekkingCount+1;
		
		table.setValueAt(experiment+1,experiment,0);
		table.setValueAt(totaal,experiment,1);
		
		if (trekkingCount==maxCount) {
			stopCounting=true;
			trekkingen[experiment]=totaal;
			experiment++;
			binomGrafiek.repaint();
		}
	}
	
	public void run()
	{ 	while (animatie!=null && stopCounting==false)
   		{
	   		this.doeStap();
	   		try{ Thread.sleep(10); } catch (Exception e) {}
   		}
	}

}
