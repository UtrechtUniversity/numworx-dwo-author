package fi.statsim;

import java.awt.Color;
import java.awt.Font;
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
	JTextField aantalKeer;
	JButton start;
	JButton keer;
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
	BinomFrequentie binomFrequentie;
	BinomRooster binomRooster;
	int[] trekkingen;
	
	Boolean showTabel=true;
	Boolean showGrafiek=true;
	Boolean showFrequentie=true;
	
	Boolean multipleTimes=false;
	int numberOfTimes;
	
	public BinomTrekking () {
		setLayout(null);
		this.setBackground(Color.white);
		
		border1=BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
		panel1=new JPanel();
		panel1.setLayout(null);
		panel1.setBackground(Color.white);
		add(panel1);
		panel1.setSize(200,105);
		panel1.setLocation(0,0);
		panel1.setBorder(BorderFactory.createTitledBorder(border1,StatSim.rb.getString("settings"),TitledBorder.CENTER,TitledBorder.TOP,new Font("SansSerif", Font.PLAIN, 12)));
		
		kansLabel=new JLabel(StatSim.rb.getString("chance"));
		kansLabel.setSize(100,20);
		kansLabel.setLocation(20,30);
		kansLabel.setFont(new Font("SansSerif", Font.PLAIN, 12) );
		panel1.add(kansLabel);
		
		kansText=new JTextField("0.2");
		kansText.setSize(50,20);
		kansText.setLocation(130,30);
		panel1.add(kansText);
		
		aantalTrekkingenLabel=new JLabel(StatSim.rb.getString("numberOfDraws"));
		aantalTrekkingenLabel.setSize(100,20);
		aantalTrekkingenLabel.setLocation(20,60);
		aantalTrekkingenLabel.setFont(new Font("SansSerif", Font.PLAIN, 12) );
		panel1.add(aantalTrekkingenLabel);
				
		aantalTrekkingenText=new JTextField("20");
		aantalTrekkingenText.setSize(50,20);
		aantalTrekkingenText.setLocation(130,60);
		panel1.add(aantalTrekkingenText);

	    start=new JButton(StatSim.rb.getString("start"));
	    start.setSize(100,20);
	    start.setLocation(210,5);
	    start.addActionListener(this);
	    add(start);
	    
	    aantalKeer = new JTextField("20");
	    aantalKeer.setSize(25,20);
	    aantalKeer.setLocation(210,30);
	    add(aantalKeer);

	    keer=new JButton(StatSim.rb.getString("times"));
	    keer.setSize(75,20);
	    keer.setLocation(235,30);
	    keer.addActionListener(this);
	    add(keer);
	    
	    volgende=new JButton(StatSim.rb.getString("next"));
	    volgende.setSize(100,20);
	    volgende.setLocation(210,55);
	    volgende.addActionListener(this);
	    volgende.setEnabled(false);
	    add(volgende);
	    
	    stop=new JButton(StatSim.rb.getString("stop"));
	    stop.setSize(100,20);
	    stop.setLocation(210,80);
	    stop.addActionListener(this);
	    stop.setEnabled(false);
	    add(stop);
	    
	    binomFrequentie = new BinomFrequentie(this);
	    binomFrequentie.setSize(200,100);
	    binomFrequentie.setLocation(0,115);
	    add(binomFrequentie);
	
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
	    pane.setLocation(0,215);
	    pane.setSize(200,235);
	    
	    binomGrafiek=new BinomGrafiek(this);
	    binomGrafiek.setLocation(200,115);
	    binomGrafiek.setBackground(Color.white);
	    binomGrafiek.setSize(590,335);
	    binomGrafiek.setVisible(false);
	    add(binomGrafiek);
	    
	    binomRooster=new BinomRooster(this);
	    binomRooster.setLocation(200,115);
	    binomRooster.setBackground(Color.white);
	    binomRooster.setSize(590,335);
	    add(binomRooster);

	    trekkingen = new int[1000];
	    maxCount=Integer.parseInt(aantalTrekkingenText.getText());
	}
	
	public void setZichtbaar() {
		if (showTabel) {
			pane.setVisible(true);
		} else {
			pane.setVisible(false);
		}
		if (showTabel || showFrequentie) {
			binomGrafiek.setLocation(200,115);
			binomGrafiek.setSize(this.getWidth()-200,this.getHeight()-115);
		} else {
			binomGrafiek.setLocation(0,115);
			binomGrafiek.setSize(this.getWidth(),this.getHeight()-115);
		}
		if (showFrequentie) {
			binomFrequentie.setVisible(true);
			pane.setLocation(0,215);
			pane.setSize(200,this.getHeight()-215);
			table.setSize(200,this.getHeight()-215);
		} else {
			binomFrequentie.setVisible(false);
			pane.setLocation(0,115);
			pane.setSize(200,this.getHeight()-115);
			table.setSize(200,this.getHeight()-115);	
		}
		//binomGrafiek.setVisible(showGrafiek);
	}
	
	public void setSize(int width, int height) {
		super.setSize(width, height);
		if (showTabel==false) {
			binomGrafiek.setSize(this.getWidth(),this.getHeight()-115);
			binomRooster.setSize(this.getWidth(),this.getHeight()-115);
		} else {
			binomGrafiek.setSize(this.getWidth()-200,this.getHeight()-115);
			binomRooster.setSize(this.getWidth()-200,this.getHeight()-115);
		}
		setZichtbaar();
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
			for (int i=0;i<1000;i++) {
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
		if (e.getSource()==keer) {
			if (start.isEnabled()) {
				experiment=0;
				for (int i=0;i<1000;i++) {
				   table.setValueAt("",i,0);
				   table.setValueAt("",i,1);
				}
			}
			stopCounting=false;
			trekkingCount=0;
			totaal=0;
			keer.setEnabled(false);
			volgende.setEnabled(false);
			start.setEnabled(false);
			stop.setEnabled(true);
			numberOfTimes=Integer.parseInt(aantalKeer.getText());
			maxCount=Integer.parseInt(aantalTrekkingenText.getText());
			multipleTimes=true;
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
			trekkingen[experiment]=totaal;
			experiment++;
			binomGrafiek.repaint();
			if (multipleTimes==false) {
				stopCounting=true;
			} else {
				numberOfTimes--;
				if (numberOfTimes==0) {
					stopCounting=true;
					multipleTimes=false;
					keer.setEnabled(true);
					volgende.setEnabled(true);
				}
				trekkingCount=0;
				totaal=0;
			}
		}
		binomFrequentie.repaint();
	}
	int frameSkip;
	public void run()
	{ 	while (animatie!=null && stopCounting==false)
   		{
	   		this.doeStap();
	   		try{ 
	   			double waitTime;
	   			if (multipleTimes) 
	   				waitTime=200/(Double.parseDouble(aantalTrekkingenText.getText())*Double.parseDouble(aantalKeer.getText()));
	   			else
	   				waitTime=200/(Double.parseDouble(aantalTrekkingenText.getText()));
	   			if (waitTime<1) {
	   				double dummy=1/waitTime;
	   				frameSkip=frameSkip+1;
	   				if (frameSkip>dummy) {
	   					Thread.sleep(1);
	   					frameSkip=0;
	   				}
	   			} else {
	   				Thread.sleep((int)waitTime);
	   			}
	   		} catch (Exception e) {}
   		}
	}

}
