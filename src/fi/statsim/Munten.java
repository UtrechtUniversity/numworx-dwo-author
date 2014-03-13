package fi.statsim;

import java.awt.Button;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import fi.beans.wiskopdrbeans.InteractieEditPanel;
import fi.beans.wiskopdrbeans.InteractiePanel;
//import fi.statistiek.Statistiek;

public class Munten extends JPanel implements ActionListener, Runnable {	
	JPanel buttonPanel;
	Thread animatie;
	Button volgende;
	Button start;
	Button stop;
	PaintPanel paintPanel;
	FrequentieClass frequentieClass;
	JTable table;
	DefaultTableModel model;
	JTable table1;
	DefaultTableModel model1;
	JLabel aantalWorpenLabel;
	JLabel kansOpKopLabel;
	JTextField aantalWorpenText;
	JTextField kansOpKopText;
	Boolean[] munt;
	double[] percentageMunt;
	int muntCount;
	Boolean stopCounting;
	int experiment;
	int totaalmunt;
	int maxCount;
	JScrollPane pane;
	JScrollPane pane1;
	int geenKop;
	int eenKop;
	int tweeKop;
	JRadioButton aantalKop;
	JRadioButton percentageKop;
	JLabel gemiddeldeLabel;
	JLabel gemiddeldeText;
	JLabel minimumLabel;
	JLabel minimumText;
	JLabel maximumLabel;
	JLabel maximumText;
	JLabel gemiddeldeLabel1;
	JLabel minimumLabel1;
	JLabel maximumLabel1;
	JLabel gemiddeldeText1;
	JLabel gemiddeldeText2;
	JLabel gemiddeldeText3;
	JLabel minimumText1;
	JLabel minimumText2;
	JLabel minimumText3;
	JLabel maximumText1;
	JLabel maximumText2;
	JLabel maximumText3;
	JLabel geenKopLabel;
	JLabel eenKopLabel;
	JLabel tweeKopLabel;
	
	Border border1;
	JPanel panel1;
	Border border2;
	JPanel panel2;
	JRadioButton eenMuntRadio;
	JRadioButton tweeMuntenRadio;
	
	public Munten() {
		setBackground(Color.white);
		setBorder(BorderFactory.createLineBorder(new Color(202,228,255), 7));
		
		maxCount=100;
		
		munt= new Boolean[10001];
		percentageMunt=new double[10001];
		
		this.setLayout(null);
		buttonPanel=new JPanel();
		add(buttonPanel);
		buttonPanel.setLayout(null);
		buttonPanel.setBackground(Color.white);
		buttonPanel.setLocation(0,0);
		buttonPanel.setSize(790,100);
		
		paintPanel=new PaintPanel(this);
		paintPanel.setSize(590,350);
		paintPanel.setPreferredSize(paintPanel.getSize());
		add(paintPanel);
		paintPanel.setLocation(200,100);
		
		frequentieClass = new FrequentieClass(this);
		frequentieClass.setSize(200,100);
		frequentieClass.setPreferredSize(frequentieClass.getSize());
		add(frequentieClass);
		frequentieClass.setLocation(0,100);
		
		border1=BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
		panel1=new JPanel();
		panel1.setLayout(null);
		panel1.setBackground(Color.white);
		buttonPanel.add(panel1);
		panel1.setSize(310,95);
		panel1.setLocation(0,0);
		panel1.setBorder(BorderFactory.createTitledBorder(border1,StatSim.rb.getString("settings"),TitledBorder.CENTER,TitledBorder.TOP));
		
		eenMuntRadio = new JRadioButton(StatSim.rb.getString("oneCoin"));
		eenMuntRadio.setBackground(Color.white);
		eenMuntRadio.setSelected(true);
		eenMuntRadio.setLocation(30,20);
		eenMuntRadio.setSize(80,20);
		eenMuntRadio.addActionListener(this);
		tweeMuntenRadio = new JRadioButton(StatSim.rb.getString("twoCoins"));
		tweeMuntenRadio.setBackground(Color.white);
		tweeMuntenRadio.setLocation(120,20);
		tweeMuntenRadio.setSize(150,20);
		tweeMuntenRadio.addActionListener(this);
		panel1.add(eenMuntRadio);
		panel1.add(tweeMuntenRadio);
		
		ButtonGroup buttonGroup1=new ButtonGroup();
		buttonGroup1.add(eenMuntRadio);
		buttonGroup1.add(tweeMuntenRadio);
		
		aantalWorpenLabel=new JLabel(StatSim.rb.getString("numberOfRounds"));
		aantalWorpenLabel.setLocation(10,50);
		aantalWorpenLabel.setSize(150,20);
		kansOpKopLabel=new JLabel(StatSim.rb.getString("chanceOfHead"));
		kansOpKopLabel.setLocation(160,50);
		kansOpKopLabel.setSize(150,20);
		aantalWorpenText=new JTextField();
		aantalWorpenText.setSize(50, 20);
		aantalWorpenText.setLocation(100,50);
		aantalWorpenText.setColumns(5);
		aantalWorpenText.setText("100");
		kansOpKopText=new JTextField();
		kansOpKopText.setColumns(5);
		kansOpKopText.setSize(50,20);
		kansOpKopText.setLocation(240,50);
		kansOpKopText.setText("0.5");
		panel1.add(aantalWorpenLabel);
		panel1.add(aantalWorpenText);
		panel1.add(kansOpKopLabel);
		panel1.add(kansOpKopText);
		
		start=new Button(StatSim.rb.getString("start"));
		buttonPanel.add(start);
		start.setLocation(315,0);
		start.setSize(100,20);
		start.addActionListener(this);
		
		volgende = new Button(StatSim.rb.getString("next"));
		buttonPanel.add(volgende);
		volgende.setLocation(315,30);
		volgende.setSize(100,20);
		volgende.addActionListener(this);
		volgende.setEnabled(false);
		
		stop = new Button(StatSim.rb.getString("stop"));
		buttonPanel.add(stop);
		stop.setLocation(315,60);
		stop.setSize(100,20);
		stop.addActionListener(this);
		stop.setEnabled(false);
		
		border2=BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
		panel2=new JPanel();
		panel2.setLayout(null);
		panel2.setBackground(Color.white);
		buttonPanel.add(panel2);
		panel2.setSize(360,95);
		panel2.setLocation(425,0);
		panel2.setBorder(BorderFactory.createTitledBorder(border2,StatSim.rb.getString("results"),TitledBorder.CENTER,TitledBorder.TOP));
		
		gemiddeldeLabel=new JLabel(StatSim.rb.getString("mean"));
		gemiddeldeLabel.setSize(100,20);
		gemiddeldeLabel.setLocation(200,20);
		panel2.add(gemiddeldeLabel);
		gemiddeldeText=new JLabel("0.0");
		gemiddeldeText.setSize(40,20);
		gemiddeldeText.setLocation(300,20);
		panel2.add(gemiddeldeText);
		minimumLabel=new JLabel(StatSim.rb.getString("minimum"));
		minimumLabel.setSize(100,20);
		minimumLabel.setLocation(200,40);
		panel2.add(minimumLabel);
		minimumText=new JLabel("0.0");
		minimumText.setSize(40,20);
		minimumText.setLocation(300,40);
		panel2.add(minimumText);
		maximumLabel=new JLabel(StatSim.rb.getString("maximum"));
		maximumLabel.setSize(100,20);
		maximumLabel.setLocation(200,60);
		panel2.add(maximumLabel);
		maximumText=new JLabel("0.0");
		maximumText.setSize(40,20);
		maximumText.setLocation(300,60);
		panel2.add(maximumText);
		
		aantalKop = new JRadioButton(StatSim.rb.getString("numberOfHeads"));
		aantalKop.setBackground(Color.white);
		aantalKop.setSize(100,20);
		aantalKop.setLocation(10,20);
		aantalKop.setSelected(true);
		panel2.add(aantalKop);
		aantalKop.addActionListener(this);
		
		percentageKop=new JRadioButton(StatSim.rb.getString("percentageHeads"));
		percentageKop.setBackground(Color.white);
		percentageKop.setSize(150,20);
		percentageKop.setLocation(10,50);
		panel2.add(percentageKop);
		percentageKop.addActionListener(this);
		
		ButtonGroup buttonGroup2=new ButtonGroup();
		buttonGroup2.add(aantalKop);
		buttonGroup2.add(percentageKop);
		
		gemiddeldeLabel1=new JLabel(StatSim.rb.getString("mean"));
		gemiddeldeLabel1.setSize(100,20);
		gemiddeldeLabel1.setLocation(10,25);
		gemiddeldeLabel1.setVisible(false);
		panel2.add(gemiddeldeLabel1);
		
		minimumLabel1=new JLabel(StatSim.rb.getString("minimum"));
		minimumLabel1.setSize(100,20);
		minimumLabel1.setLocation(10,45);
		minimumLabel1.setVisible(false);
		panel2.add(minimumLabel1);
		
		maximumLabel1=new JLabel(StatSim.rb.getString("maximum"));
		maximumLabel1.setSize(100,20);
		maximumLabel1.setLocation(10,65);
		maximumLabel1.setVisible(false);
		panel2.add(maximumLabel1);
		
		geenKopLabel=new JLabel(StatSim.rb.getString("noHeads"));
		geenKopLabel.setSize(80,20);
		geenKopLabel.setLocation(110,10);
		geenKopLabel.setVisible(false);
		panel2.add(geenKopLabel);
		
		gemiddeldeText1=new JLabel("0.0");
		gemiddeldeText1.setSize(40,20);
		gemiddeldeText1.setLocation(110,25);
		gemiddeldeText1.setVisible(false);
		panel2.add(gemiddeldeText1);
		
		minimumText1=new JLabel("0.0");
		minimumText1.setSize(40,20);
		minimumText1.setLocation(110, 45);
		minimumText1.setVisible(false);
		panel2.add(minimumText1);
		
		maximumText1=new JLabel("0.0");
		maximumText1.setSize(40,20);
		maximumText1.setLocation(110,65);
		maximumText1.setVisible(false);
		panel2.add(maximumText1);
		
		eenKopLabel=new JLabel(StatSim.rb.getString("oneHeads"));
		eenKopLabel.setSize(80,20);
		eenKopLabel.setLocation(190,10);
		eenKopLabel.setVisible(false);
		panel2.add(eenKopLabel);
		
		gemiddeldeText2=new JLabel("0.0");
		gemiddeldeText2.setSize(40,20);
		gemiddeldeText2.setLocation(190,25);
		gemiddeldeText2.setVisible(false);
		panel2.add(gemiddeldeText2);
		
		minimumText2=new JLabel("0.0");
		minimumText2.setSize(40,20);
		minimumText2.setLocation(190,45);
		minimumText2.setVisible(false);
		panel2.add(minimumText2);
		
		maximumText2=new JLabel("0.0");
		maximumText2.setSize(40,20);
		maximumText2.setLocation(190,65);
		maximumText2.setVisible(false);
		panel2.add(maximumText2);
				
		tweeKopLabel=new JLabel(StatSim.rb.getString("twoHeads"));
		tweeKopLabel.setSize(80,20);
		tweeKopLabel.setLocation(270,10);
		tweeKopLabel.setVisible(false);
		panel2.add(tweeKopLabel);
		
		gemiddeldeText3=new JLabel("0.0");
		gemiddeldeText3.setSize(40,20);
		gemiddeldeText3.setLocation(270,25);
		gemiddeldeText3.setVisible(false);
		panel2.add(gemiddeldeText3);
		
		minimumText3=new JLabel("0.0");
		minimumText3.setSize(40,20);
		minimumText3.setLocation(270,45);
		minimumText3.setVisible(false);
		panel2.add(minimumText3);
		
		maximumText3=new JLabel("0.0");
		maximumText3.setSize(40,20);
		maximumText3.setLocation(270,65);
		maximumText3.setVisible(false);
		panel2.add(maximumText3);
	
		String col[]=new String[3];
		col[0]=StatSim.rb.getString("exp");
		col[1]=StatSim.rb.getString("numberOfHeads");
		col[2]=StatSim.rb.getString("numberOfTails");
		
		String col1[] = new String[4];
		col1[0]=StatSim.rb.getString("exp");
		col1[1]=StatSim.rb.getString("noHeads");
		col1[2]=StatSim.rb.getString("oneHeads");
		col1[3]=StatSim.rb.getString("twoHeads");
		
	     model = new DefaultTableModel(col,100); 
	        table=new JTable(model){@Override
	        public boolean isCellEditable(int arg0, int arg1) {
	         
	            return false;
	        }};
	        
	    TableColumn column = null;
	    column = table.getColumnModel().getColumn(0);
	    column.setPreferredWidth(33); //third column is bigger
	    pane = new JScrollPane(table);
	   
	    add(pane);
	    pane.setLocation(0,200);
	    pane.setSize(200,250);

	     model1 = new DefaultTableModel(col1,100); 
	        table1=new JTable(model1){@Override
	        public boolean isCellEditable(int arg0, int arg1) {
	         
	            return false;
	        }};
	        
	    TableColumn column1 = null;
	    column1 = table1.getColumnModel().getColumn(0);
	    column1.setPreferredWidth(33); //third column is bigger
	    pane1 = new JScrollPane(table1);
	   
	    add(pane1);
	    pane1.setLocation(0,100);
	    pane1.setSize(250,350);
	    pane1.setVisible(false);
	    
	}
	
	public void setResults () {
		if (eenMuntRadio.isSelected()==true) {
			if (aantalKop.isSelected()) {	
				double aa=Math.round(gemiddeldeKop*10);
				gemiddeldeText.setText(Double.toString(aa/10));
				maximumText.setText(Integer.toString(maximumKop));
				minimumText.setText(Integer.toString(minimumKop));
			} else {
				double aa=Math.round((gemiddeldeKop/maxCount)*1000);
				gemiddeldeText.setText(Double.toString(aa/10)+"%");
				aa=Math.round(((double)maximumKop/maxCount)*1000);
				maximumText.setText(Double.toString(aa/10)+"%");
				aa=Math.round(((double)minimumKop/maxCount)*1000);
				minimumText.setText(Double.toString(aa/10)+"%");
			}
		} else {
			double aa=Math.round(gemiddeldeGeenKop*10);
			gemiddeldeText1.setText(Double.toString(aa/10));
			maximumText1.setText(Integer.toString(maximumGeenKop));
			minimumText1.setText(Integer.toString(minimumGeenKop));
			aa=Math.round(gemiddeldeEenKop*10);
			gemiddeldeText2.setText(Double.toString(aa/10));
			maximumText2.setText(Integer.toString(maximumEenKop));
			minimumText2.setText(Integer.toString(minimumEenKop));
			aa=Math.round(gemiddeldeTweeKop*10);
			gemiddeldeText3.setText(Double.toString(aa/10));
			maximumText3.setText(Integer.toString(maximumTweeKop));
			minimumText3.setText(Integer.toString(minimumTweeKop));			
		}
	}
	
	public void setEenMuntTweeMunten () {
		if (eenMuntRadio.isSelected()==true) {
			pane.setVisible(true);
			frequentieClass.setVisible(true);
			pane1.setVisible(false);
			aantalKop.setVisible(true);
			percentageKop.setVisible(true);
			gemiddeldeLabel.setVisible(true);
			minimumLabel.setVisible(true);
			maximumLabel.setVisible(true);
			gemiddeldeText.setVisible(true);
			minimumText.setVisible(true);
			maximumText.setVisible(true);
			gemiddeldeLabel1.setVisible(false);
			minimumLabel1.setVisible(false);
			maximumLabel1.setVisible(false);
			geenKopLabel.setVisible(false);
			eenKopLabel.setVisible(false);
			tweeKopLabel.setVisible(false);
			gemiddeldeText1.setVisible(false);
			minimumText1.setVisible(false);
			maximumText1.setVisible(false);
			gemiddeldeText2.setVisible(false);
			minimumText2.setVisible(false);
			maximumText2.setVisible(false);
			gemiddeldeText3.setVisible(false);
			minimumText3.setVisible(false);
			maximumText3.setVisible(false);
			paintPanel.setLocation(200,100);
			paintPanel.setSize(590,350);
			paintPanel.repaint();
		} else {
			pane.setVisible(false);
			frequentieClass.setVisible(false);
			pane1.setVisible(true);
			aantalKop.setVisible(false);
			percentageKop.setVisible(false);
			gemiddeldeLabel.setVisible(false);
			minimumLabel.setVisible(false);
			maximumLabel.setVisible(false);
			gemiddeldeText.setVisible(false);
			minimumText.setVisible(false);
			maximumText.setVisible(false);
			gemiddeldeLabel1.setVisible(true);
			minimumLabel1.setVisible(true);
			maximumLabel1.setVisible(true);
			geenKopLabel.setVisible(true);
			eenKopLabel.setVisible(true);
			tweeKopLabel.setVisible(true);
			gemiddeldeText1.setVisible(true);
			minimumText1.setVisible(true);
			maximumText1.setVisible(true);
			gemiddeldeText2.setVisible(true);
			minimumText2.setVisible(true);
			maximumText2.setVisible(true);
			gemiddeldeText3.setVisible(true);
			minimumText3.setVisible(true);
			maximumText3.setVisible(true);
			paintPanel.setLocation(250,100);
			paintPanel.setSize(540,350);
			paintPanel.repaint();
		}
	}
  
	public void setStartStop() {
		if (start.isEnabled()==false) {
			eenMuntRadio.setEnabled(false);
			tweeMuntenRadio.setEnabled(false);
			aantalWorpenText.setEnabled(false);
			kansOpKopText.setEnabled(false);
		} else {
			eenMuntRadio.setEnabled(true);
			tweeMuntenRadio.setEnabled(true);
			aantalWorpenText.setEnabled(true);
			kansOpKopText.setEnabled(true);
		}
		
	}
	
   public void actionPerformed(ActionEvent e)
   {
	   if (e.getSource()==start) {
		   for (int i=0;i<100;i++) {
			   table.setValueAt("",i,0);
			   table.setValueAt("",i,1);
			   table.setValueAt("",i,2);
			   table1.setValueAt("",i,0);
			   table1.setValueAt("",i,1);
			   table1.setValueAt("",i,2);
			   table1.setValueAt("",i,3);
		   }
		   start.setEnabled(false);
		   stop.setEnabled(true);
		   volgende.setEnabled(true);
		   setStartStop();
		   stopCounting=false;
		   experiment=0;
		   muntCount=0;
		   totaalmunt=0;
		   maxCount=Integer.parseInt(aantalWorpenText.getText());
		   geenKop=0;
		   eenKop=0;
		   tweeKop=0;
		   animatie=new Thread(this);
		   animatie.start();   
		   
	   }
	   if (e.getSource()==stop) {
		   start.setEnabled(true);
		   stop.setEnabled(false);
		   volgende.setEnabled(false);
		   setStartStop();
	   }
	   if (e.getSource()==volgende) {
		   stopCounting=false;
		   muntCount=0;
		   totaalmunt=0;
		   maxCount=Integer.parseInt(aantalWorpenText.getText());
		   geenKop=0;
		   eenKop=0;
		   tweeKop=0;
		   animatie=new Thread(this);
		   animatie.start();
	   }
	   if (e.getSource()==eenMuntRadio) {
		   setEenMuntTweeMunten();
	   }
	   if (e.getSource()==tweeMuntenRadio) {
		   setEenMuntTweeMunten();	   
	   }
	   if (e.getSource()==aantalKop) {
		   setResults();
	   }
	   if (e.getSource()==percentageKop) {
		   setResults();
	   }
   }
   
   double gemiddeldeKop;
   int maximumKop;
   int minimumKop;
   double gemiddeldeGeenKop;
   double gemiddeldeEenKop;
   double gemiddeldeTweeKop;
   int minimumGeenKop;
   int minimumEenKop;
   int minimumTweeKop;
   int maximumGeenKop;
   int maximumEenKop;
   int maximumTweeKop;
   public void doeStap() {
	   Random generator = new Random();
	   double r = generator.nextDouble();
	   
	   if (eenMuntRadio.isSelected()==true) {
		   if (r>1-Double.parseDouble(kansOpKopText.getText())) {
			   munt[muntCount]=true;
			   totaalmunt=totaalmunt+1;
			   if (muntCount>0)
				   percentageMunt[muntCount]=(percentageMunt[muntCount-1]*(muntCount)+1)/(muntCount+1);
			   else
				   percentageMunt[muntCount]=1;
		   } else
		   {
			   munt[muntCount]=false;
			   if (muntCount>0)
				   percentageMunt[muntCount]=(percentageMunt[muntCount-1]*(muntCount))/(muntCount+1);
			   else
				   percentageMunt[muntCount]=0;
		   }
		   
		   muntCount++;
		 
		   table.setValueAt(experiment+1,experiment,0);
		   table.setValueAt(muntCount-totaalmunt,experiment,1);
		   table.setValueAt(totaalmunt,experiment,2);	   
	   } else {
		   double s = generator.nextDouble();
		   
		   if (r>1-Double.parseDouble(kansOpKopText.getText()) && s>1-Double.parseDouble(kansOpKopText.getText()))
			   tweeKop++;
		   if (r>1-Double.parseDouble(kansOpKopText.getText()) && s<=1-Double.parseDouble(kansOpKopText.getText()) || r<=1-Double.parseDouble(kansOpKopText.getText()) && s>1-Double.parseDouble(kansOpKopText.getText()))
			   eenKop++;
		   if (r<=1-Double.parseDouble(kansOpKopText.getText()) && s<=1-Double.parseDouble(kansOpKopText.getText()))
			   geenKop++;
		   
		   muntCount++;
		   
		   table1.setValueAt(experiment+1,experiment,0);
		   table1.setValueAt(geenKop,experiment,1);
		   table1.setValueAt(eenKop,experiment,2);
		   table1.setValueAt(tweeKop,experiment,3);
	   }
		   
	   if (muntCount>=maxCount) {
		   stopCounting=true;
		   if (eenMuntRadio.isSelected()==true) {
			   gemiddeldeKop=(gemiddeldeKop*(experiment)+muntCount-totaalmunt)/(experiment+1);
			   if (muntCount-totaalmunt>maximumKop)
				   maximumKop=muntCount-totaalmunt;
			   if (muntCount-totaalmunt<minimumKop)
				   minimumKop=muntCount-totaalmunt;
			   if (experiment==0) {
				   maximumKop=muntCount-totaalmunt;
				   minimumKop=muntCount-totaalmunt;
			   }
		   } else {
			   gemiddeldeGeenKop=(gemiddeldeGeenKop*(experiment)+geenKop)/(experiment+1);
			   gemiddeldeEenKop=(gemiddeldeEenKop*(experiment)+eenKop)/(experiment+1);
			   gemiddeldeTweeKop=(gemiddeldeTweeKop*(experiment)+tweeKop)/(experiment+1);
			   if (geenKop>maximumGeenKop)
				   maximumGeenKop=geenKop;
			   if (geenKop<minimumGeenKop)
				   minimumGeenKop=geenKop;
			   if (eenKop>maximumEenKop)
				   maximumEenKop=eenKop;
			   if (eenKop<minimumEenKop)
				   minimumEenKop=eenKop;
			   if (tweeKop>maximumTweeKop)
				   maximumTweeKop=tweeKop;
			   if (tweeKop<minimumTweeKop)
				   minimumTweeKop=tweeKop;
			   if (experiment==0) {
				   maximumGeenKop=geenKop;
				   minimumGeenKop=geenKop;
				   maximumEenKop=eenKop;
				   minimumEenKop=eenKop;
				   maximumTweeKop=tweeKop;
				   minimumTweeKop=tweeKop;
			   }
		   }
		   setResults();
	
		   experiment++;
	   }
	   paintPanel.repaint();
	   frequentieClass.repaint();
   }
   
   public void run()
   { 	while (animatie!=null && stopCounting==false)
   		{
	   		this.doeStap();
	   		try{ Thread.sleep(10); } catch (Exception e) {}
   		}
   }
	
	public void paintComponent(Graphics g) {	
		super.paintComponent(g);
	}
	
}