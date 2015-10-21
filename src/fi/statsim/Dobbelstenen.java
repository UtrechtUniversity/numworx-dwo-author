package fi.statsim;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JButton;
import javax.swing.ButtonGroup;

import java.awt.Button;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JCheckBox;
import javax.swing.BorderFactory;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.JRadioButton;

public class Dobbelstenen extends JPanel implements ActionListener, Runnable {
	JTable table;
	DefaultTableModel model;
	JTable table1;
	DefaultTableModel model1;
	JTable table2;
	DefaultTableModel model2;
	JTable table3;
	DefaultTableModel model3;
	JTable table4;
	DefaultTableModel model4;
	JTable table5;
	DefaultTableModel model5;
	String col[]={"exp.","1", "2", "3","4","5","6"};
	String col1[]={"exp.", "2", "3","4","5","6","7","8","9","10", "11","12"};
	String col2[]={"exp.", "3","4","5","6","7","8","9","10", "11","12", "13","14","15","16","17","18"};
	String col3[]={"Ogen","1", "2", "3","4","5","6"};
	String col4[]={"Som ogen", "2", "3","4","5","6","7","8","9","10", "11","12"};
	String col5[]={"Som ogen", "3","4","5","6","7","8","9","10", "11","12", "13","14","15","16","17","18"};
	JScrollPane pane;
	JScrollPane pane1;
	JScrollPane pane2;
	JScrollPane pane3;
	JScrollPane pane4;
	JScrollPane pane5;
	JButton voeruit;
	JButton wis;
	Thread animatie;
	Boolean stopCounting;
	Border border1;
	Border border2;
	int ogen[];
	int ogenSom[];
	double ogenGemiddeld[];
	JPanel panel1;
	JPanel panel2;
	JRadioButton eenDobbelsteenRadio;
	JRadioButton tweeDobbelstenenRadio;
	JRadioButton drieDobbelstenenRadio;
	DobbelstenenGrafiek dobbelstenenGrafiek;
	DobbelstenenGrafiek dobbelstenenSomGrafiek;
	JLabel aantalWorpenLabel;
	JTextField aantalWorpenText;
	JLabel toonSomLabel;
	JCheckBox toonSomCheckBox;
	int maxCount;
	
	Boolean showInstellingen=true;
	Boolean showTabel=true;
	Boolean showGrafiek=true;
	Boolean showResultaten=true;
	
	StatSimInteractiePanel ssip;
	
	public Dobbelstenen (StatSimInteractiePanel ssip) {
		this.ssip=ssip;
		
		setLayout(null);
		this.setBackground(Color.white);
		
		border1=BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
		panel1=new JPanel();
		panel1.setLayout(null);
		panel1.setBackground(Color.white);
		add(panel1);
		panel1.setSize(230,115);
		panel1.setLocation(0,0);
		panel1.setBorder(BorderFactory.createTitledBorder(border1,StatSim.rb.getString("settings"),TitledBorder.CENTER,TitledBorder.TOP,new Font("SansSerif", Font.PLAIN, 12)));
				
		border2=BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
		panel2=new JPanel();
		panel2.setLayout(null);
		panel2.setBackground(Color.white);
		panel1.add(panel2);
		panel2.setSize(200,40);
		panel2.setLocation(10,15);
		panel2.setBorder(BorderFactory.createTitledBorder(border2,StatSim.rb.getString("numberOfDices"),TitledBorder.LEFT,TitledBorder.TOP,new Font("SansSerif", Font.PLAIN, 12)));
		
		eenDobbelsteenRadio=new JRadioButton(StatSim.rb.getString("one"));
		eenDobbelsteenRadio.setBackground(Color.white);
		eenDobbelsteenRadio.setSize(50,20);
		eenDobbelsteenRadio.setLocation(10,15);
		eenDobbelsteenRadio.setSelected(true);
		eenDobbelsteenRadio.addActionListener(this);
		eenDobbelsteenRadio.setFont(new Font("SansSerif", Font.PLAIN, 12) );
		panel2.add(eenDobbelsteenRadio);
		
		tweeDobbelstenenRadio=new JRadioButton(StatSim.rb.getString("two"));
		tweeDobbelstenenRadio.setBackground(Color.white);
		tweeDobbelstenenRadio.setSize(60,20);
		tweeDobbelstenenRadio.setLocation(60,15);
		tweeDobbelstenenRadio.addActionListener(this);
		tweeDobbelstenenRadio.setFont(new Font("SansSerif", Font.PLAIN, 12) );
		panel2.add(tweeDobbelstenenRadio);
		
		drieDobbelstenenRadio=new JRadioButton(StatSim.rb.getString("three"));
		drieDobbelstenenRadio.setBackground(Color.white);
		drieDobbelstenenRadio.setSize(60,20);
		drieDobbelstenenRadio.setLocation(120,15);
		drieDobbelstenenRadio.addActionListener(this);
		drieDobbelstenenRadio.setFont(new Font("SansSerif", Font.PLAIN, 12) );
		panel2.add(drieDobbelstenenRadio);
		
		ButtonGroup buttonGroup1=new ButtonGroup();
		buttonGroup1.add(eenDobbelsteenRadio);
		buttonGroup1.add(tweeDobbelstenenRadio);
		buttonGroup1.add(drieDobbelstenenRadio);
		
		col[0]=StatSim.rb.getString("exp");
		col1[0]=StatSim.rb.getString("exp");
		col2[0]=StatSim.rb.getString("exp");
		col3[0]=StatSim.rb.getString("eyes");
		col4[0]=StatSim.rb.getString("sumEyes");
		col5[0]=StatSim.rb.getString("sumEyes");
		
	    model = new DefaultTableModel(col,100); 
	    table=new JTable(model){@Override
	    	public boolean isCellEditable(int arg0, int arg1) {
	         
	            return false;
	        }};
	        
	    TableColumn column = null;
	    column = table.getColumnModel().getColumn(0);
	    column.setPreferredWidth(33); //third column is bigger
	    for (int i=1;i<7;i++) {
	    	column = table.getColumnModel().getColumn(i);
	    	column.setPreferredWidth(24); //third column is bigger
	    }
	    table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
	    pane = new JScrollPane(table);
	   
	    add(pane);
	    pane.setLocation(0,125);
	    pane.setSize(200,325);

	    model1 = new DefaultTableModel(col1,100); 
	    table1=new JTable(model1){@Override
	    	public boolean isCellEditable(int arg0, int arg1) {
	         
	            return false;
	        }};
	        
	    TableColumn column1 = null;
	    column1 = table1.getColumnModel().getColumn(0);
	    column1.setPreferredWidth(33); //third column is bigger
	    for (int i=1;i<12;i++) {
	    	column1 = table1.getColumnModel().getColumn(i);
	    	column1.setPreferredWidth(24); //third column is bigger
	    }
	    table1.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
	    pane1 = new JScrollPane(table1);
	   
	    add(pane1);
	    pane1.setLocation(0,125);
	    pane1.setSize(200,325);

	    model2 = new DefaultTableModel(col2,100); 
	    table2=new JTable(model2){@Override
	    	public boolean isCellEditable(int arg0, int arg1) {
	         
	            return false;
	        }};
	        
	    TableColumn column2 = null;
	    column2 = table2.getColumnModel().getColumn(0);
	    column2.setPreferredWidth(33); //third column is bigger
	    for (int i=1;i<17;i++) {
	    	column2 = table2.getColumnModel().getColumn(i);
	    	column2.setPreferredWidth(24); //third column is bigger
	    }
	    table2.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
	    pane2 = new JScrollPane(table2);
	   
	    add(pane2);
	    pane2.setLocation(0,125);
	    pane2.setSize(200,325);
	    
	    pane1.setVisible(false);
	    pane2.setVisible(false);
	    
	    voeruit=new JButton(StatSim.rb.getString("execute"));
	    voeruit.setSize(120,20);
	    voeruit.setLocation(240,7);
	    voeruit.addActionListener(this);
	    add(voeruit);
	    
	    wis=new JButton(StatSim.rb.getString("erase"));
	    wis.setSize(120,20);
	    wis.setLocation(240,32);
	    wis.addActionListener(this);
	    wis.setEnabled(false);
	    add(wis);
	    
	    dobbelstenenGrafiek = new DobbelstenenGrafiek(this);
	    //dobbelstenenGrafiek.setSize(590,325);
	    dobbelstenenGrafiek.setLocation(200,125);
	    dobbelstenenGrafiek.displaySom=false;
	    add(dobbelstenenGrafiek);

	    dobbelstenenSomGrafiek = new DobbelstenenGrafiek(this);
	    dobbelstenenSomGrafiek.setSize(590,160);
	    dobbelstenenSomGrafiek.setLocation(200,285);
	    dobbelstenenSomGrafiek.setVisible(false);
	    dobbelstenenSomGrafiek.displaySom=true;
	    add(dobbelstenenSomGrafiek);
	    
	    aantalWorpenLabel = new JLabel(StatSim.rb.getString("numberOfRounds"));
	    aantalWorpenLabel.setSize(100,20);
	    aantalWorpenLabel.setLocation(10,60);
	    aantalWorpenLabel.setFont(new Font("SansSerif", Font.PLAIN, 12) );
	    panel1.add(aantalWorpenLabel);
	    
	    aantalWorpenText = new JTextField("30");
	    aantalWorpenText.setSize(50,20);
	    aantalWorpenText.setLocation(120,60);
	    panel1.add(aantalWorpenText);
	    
	    toonSomLabel = new JLabel(StatSim.rb.getString("showSum"));
	    toonSomLabel.setSize(100,20);
	    toonSomLabel.setLocation(10,80);
	    toonSomLabel.setFont(new Font("SansSerif", Font.PLAIN, 12) );
	    panel1.add(toonSomLabel);
	    
	    toonSomCheckBox = new JCheckBox();
	    toonSomCheckBox.setBackground(Color.white);
	    toonSomCheckBox.setSize(50,20);
	    toonSomCheckBox.setLocation(120,80);
	    toonSomCheckBox.addActionListener(this);
	    panel1.add(toonSomCheckBox);
	   
	    model3 = new DefaultTableModel(col3,1); 
	    table3=new JTable(model3){@Override
	    	public boolean isCellEditable(int arg0, int arg1) {
	         
	            return false;
	        }};
	        
	    TableColumn column3 = null;
	    column3 = table3.getColumnModel().getColumn(0);
	    column3.setPreferredWidth(100); //third column is bigger
	    for (int i=1;i<7;i++) {
	    	column3 = table3.getColumnModel().getColumn(i);
	    	column3.setPreferredWidth(50); //third column is bigger
	    }
	    table3.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
	    pane3 = new JScrollPane(table3);
	   
	    add(pane3);
	    pane3.setLocation(370,0);
	    pane3.setSize(420,115);
	   
	    model4 = new DefaultTableModel(col4,1); 
	    table4=new JTable(model4){@Override
	    	public boolean isCellEditable(int arg0, int arg1) {
	         
	            return false;
	        }};
	        
	    TableColumn column4 = null;
	    column4 = table4.getColumnModel().getColumn(0);
	    column4.setPreferredWidth(100); //third column is bigger
	    for (int i=1;i<12;i++) {
	    	column4 = table4.getColumnModel().getColumn(i);
	    	column4.setPreferredWidth(50); //third column is bigger
	    }
	    table4.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
	    pane4 = new JScrollPane(table4);
	   
	    add(pane4);
	    pane4.setLocation(370,0);
	    pane4.setSize(420,115);
	    
	    pane4.setVisible(false);
	    
	    
	    model5 = new DefaultTableModel(col5,1); 
	    table5=new JTable(model5){@Override
	    	public boolean isCellEditable(int arg0, int arg1) {
	         
	            return false;
	        }};
	        
	    TableColumn column5 = null;
	    column5 = table5.getColumnModel().getColumn(0);
	    column5.setPreferredWidth(100); //third column is bigger
	    for (int i=1;i<17;i++) {
	    	column5 = table5.getColumnModel().getColumn(i);
	    	column5.setPreferredWidth(50); //third column is bigger
	    }
	    table5.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
	    pane5 = new JScrollPane(table5);
	   
	    add(pane5);
	    pane5.setLocation(70,0);
	    pane5.setSize(420,115);
	    
	    pane5.setVisible(false);
	    
	    table3.setValueAt(StatSim.rb.getString("mean"),0,0);
	    table4.setValueAt(StatSim.rb.getString("mean"),0,0);
	    table5.setValueAt(StatSim.rb.getString("mean"),0,0);
	    
	    ogen = new int[19];
	    ogenSom = new int[19];
	    ogenGemiddeld = new double[19];
	    maxCount=30;
	}
	
	public void setZichtbaar() {
		if (showInstellingen) {
			panel1.setVisible(showInstellingen);
			voeruit.setLocation(240,7);
			wis.setLocation(240,32);
			pane3.setLocation(370,0);
			pane4.setLocation(370,0);
			pane5.setLocation(370,0);
		} else {
			panel1.setVisible(showInstellingen);
			voeruit.setLocation(10,7);
			wis.setLocation(10,32);
			pane3.setLocation(140,0);
			pane4.setLocation(140,0);
			pane5.setLocation(140,0);
		}
		if (showResultaten==false) {
			pane3.setVisible(false);
			pane4.setVisible(false);
			pane5.setVisible(false);
		} else {
			if (eenDobbelsteenRadio.isSelected()==true) {
				pane3.setVisible(true);
				pane4.setVisible(false);
				pane5.setVisible(false);
			}
			if (tweeDobbelstenenRadio.isSelected()==true) {
				pane3.setVisible(false);
				pane4.setVisible(true);
				pane5.setVisible(false);
			}
			if (drieDobbelstenenRadio.isSelected()==true) {
				pane3.setVisible(false);
				pane4.setVisible(false);
				pane5.setVisible(true);
			}
		}
		if (showTabel==false) {
			pane.setVisible(false);
			pane1.setVisible(false);
			pane2.setVisible(false);
			dobbelstenenGrafiek.setLocation(0,125);
			if (toonSomCheckBox.isSelected()==true) 
				dobbelstenenGrafiek.setSize(this.getWidth(),(this.getHeight()-125)/2);
			else
				dobbelstenenGrafiek.setSize(this.getWidth(),this.getHeight()-125);
			dobbelstenenSomGrafiek.setLocation(0,125+(this.getHeight()-125)/2);
			dobbelstenenSomGrafiek.setSize(this.getWidth(),(this.getHeight()-125)/2);
		} else {
			if (eenDobbelsteenRadio.isSelected()==true) {
				pane.setVisible(true);
				pane1.setVisible(false);
				pane2.setVisible(false);
			}
			if (tweeDobbelstenenRadio.isSelected()==true) {
				pane.setVisible(false);
				pane1.setVisible(true);
				pane2.setVisible(false);
			}
			if (drieDobbelstenenRadio.isSelected()==true) {
				pane.setVisible(false);
				pane1.setVisible(false);
				pane2.setVisible(true);
			}			
			dobbelstenenGrafiek.setLocation(230,125);
			if (toonSomCheckBox.isSelected()==true) 
				dobbelstenenGrafiek.setSize(this.getWidth()-230,(this.getHeight()-125)/2);
			else
				dobbelstenenGrafiek.setSize(this.getWidth()-230,this.getHeight()-125);
			dobbelstenenSomGrafiek.setLocation(230,125+(this.getHeight()-125)/2);
			dobbelstenenSomGrafiek.setSize(this.getWidth()-230,(this.getHeight()-125)/2);
		}
		dobbelstenenGrafiek.setVisible(showGrafiek);
		if (toonSomCheckBox.isSelected()==true) {
			if (showTabel)
				dobbelstenenGrafiek.setSize(this.getWidth()-230,(this.getHeight()-125)/2);
			else
				dobbelstenenGrafiek.setSize(this.getWidth(),(this.getHeight()-125)/2);
			dobbelstenenSomGrafiek.setVisible(true);
		} else {
			if (showTabel)
		   		dobbelstenenGrafiek.setSize(this.getWidth()-230,this.getHeight()-125);
		   	else
		   		dobbelstenenGrafiek.setSize(this.getWidth(),this.getHeight()-125);
		   	dobbelstenenSomGrafiek.setVisible(false);
		}
	}
	
	public void setSize(int width, int height) {
		super.setSize(width, height);
		if (showTabel==false)
			dobbelstenenGrafiek.setSize(this.getWidth(),this.getHeight()-125);
		else
			dobbelstenenGrafiek.setSize(this.getWidth()-230,this.getHeight()-125);
		pane.setSize(230,this.getHeight()-125);
		table.setSize(230,this.getHeight()-125);
		pane1.setSize(230,this.getHeight()-125);
		table1.setSize(230,this.getHeight()-125);
		pane2.setSize(230,this.getHeight()-125);
		table2.setSize(230,this.getHeight()-125);
		
		pane3.setSize(this.getWidth()-370,115);
		table3.setSize(this.getWidth()-370,115);
		pane4.setSize(this.getWidth()-370,115);
		table4.setSize(this.getWidth()-370,115);
		pane5.setSize(this.getWidth()-370,115);
		table5.setSize(this.getWidth()-370,115);
	    
		setZichtbaar();
	}
	
	public void setAantalDobbelstenen() {
		if (eenDobbelsteenRadio.isSelected()==true) {
			if (showTabel)
				pane.setVisible(true);
			pane1.setVisible(false);
			pane2.setVisible(false);
			if (showResultaten)
				pane3.setVisible(true);
			pane4.setVisible(false);
			pane5.setVisible(false);
		}
		if (tweeDobbelstenenRadio.isSelected()==true) {
			pane.setVisible(false);
			if (showTabel)
				pane1.setVisible(true);
			pane2.setVisible(false);
			pane3.setVisible(false);
			if (showResultaten)
				pane4.setVisible(true);
			pane5.setVisible(false);
		}
		if (drieDobbelstenenRadio.isSelected()==true) {
			pane.setVisible(false);
			pane1.setVisible(false);
			if (showTabel)
				pane2.setVisible(true);
			pane3.setVisible(false);
			pane4.setVisible(false);
			if (showResultaten)
				pane5.setVisible(true);
		}
		dobbelstenenGrafiek.repaint();
		dobbelstenenSomGrafiek.repaint();
	}

	public void setStartStop() {
		if (wis.isEnabled()==true) {
			eenDobbelsteenRadio.setEnabled(false);
			tweeDobbelstenenRadio.setEnabled(false);
			drieDobbelstenenRadio.setEnabled(false);
			aantalWorpenText.setEnabled(false);
		} else {
			eenDobbelsteenRadio.setEnabled(true);
			tweeDobbelstenenRadio.setEnabled(true);
			drieDobbelstenenRadio.setEnabled(true);
			aantalWorpenText.setEnabled(true);
		}
	}
	

	public void fireCBook() {
		String string1="";
		
		if (eenDobbelsteenRadio.isSelected()==true) {
			for (int i=0;i<experiment;i++) {
				for (int j=0;j<6;j++) {
					string1=string1+table.getValueAt(i, j+1);
					if (j<5)
						string1=string1+";";							
				}
				if (i<experiment-1)
					string1=string1+"\n";
			}					
		}
		if (tweeDobbelstenenRadio.isSelected()==true) {
			for (int i=0;i<experiment;i++) {
				for (int j=0;j<11;j++) {
					string1=string1+table1.getValueAt(i, j+1);
					if (j<10)
						string1=string1+";";							
				}
				if (i<experiment-1)
					string1=string1+"\n";
			}
		}
		if (drieDobbelstenenRadio.isSelected()==true) {
			for (int i=0;i<experiment;i++) {
				for (int j=0;j<16;j++) {
					string1=string1+table2.getValueAt(i, j+1);
					if (j<15)
						string1=string1+";";							
				}
				if (i<experiment-1)
					string1=string1+"\n";
			}
		}
				
		ssip.fireCBookDobbelstenen(string1);		
	}
		
	public void actionPerformed(ActionEvent e) {
		   if (e.getSource()==voeruit) {
			   voeruit.setEnabled(false);
			   wis.setEnabled(true);
			   setStartStop();
			   maxCount=Integer.parseInt(aantalWorpenText.getText());
			   stopCounting=false;
			   for (int i=0;i<19;i++) {
				   ogen[i]=0;
			   }
			   dobbelsteenCount=0;
			   animatie=new Thread(this);
			   animatie.start();   
		   }
		   if (e.getSource()==wis) {
		   	   voeruit.setEnabled(true);
			   wis.setEnabled(false);
			   setStartStop();
			   for (int i=0;i<100;i++) {
				   table.setValueAt("",i,0);
				   for (int j=0;j<6;j++) {
					   table.setValueAt("",i,j+1);
				   }
				   table1.setValueAt("",i,0);
				   for (int j=0;j<11;j++) {
					   table1.setValueAt("",i,j+1);
				   }
				   table2.setValueAt("",i,0);
				   for (int j=0;j<16;j++) {
					   table2.setValueAt("",i,j+1);
				   }
			   }
			   for (int i=0;i<19;i++) {
				   ogen[i]=0;
			   }
			   for (int i=0;i<19;i++) {
				   ogenGemiddeld[i]=0;
				   ogenSom[i]=0;
			   }
			   experiment=0;
			   dobbelsteenCount=0;
			   fireCBook();
			   dobbelstenenGrafiek.repaint();
		   }
		   if (e.getSource()==eenDobbelsteenRadio) {
			   setAantalDobbelstenen();
		   }
		   if (e.getSource()==tweeDobbelstenenRadio) {
			   setAantalDobbelstenen();
		   }
		   if (e.getSource()==drieDobbelstenenRadio) {
			   setAantalDobbelstenen();
		   }
		   if (e.getSource()==toonSomCheckBox) {
			   if (toonSomCheckBox.isSelected()==true) {
				   if (showTabel)
					   dobbelstenenGrafiek.setSize(this.getWidth()-230,(this.getHeight()-125)/2);
				   else
					   dobbelstenenGrafiek.setSize(this.getWidth(),(this.getHeight()-125)/2);
				   dobbelstenenSomGrafiek.setVisible(true);
			   } else {
				   if (showTabel)
					   dobbelstenenGrafiek.setSize(this.getWidth()-230,this.getHeight()-125);
				   else
					   dobbelstenenGrafiek.setSize(this.getWidth(),this.getHeight()-125);
				   dobbelstenenSomGrafiek.setVisible(false);
			   }
		   }
	}
	
	
	int dobbelsteenCount;
	int experiment;
	public void doeStap() {
		Random generator = new Random();
		double r = generator.nextDouble();
		double r2 = generator.nextDouble();
		double r4 = generator.nextDouble();
		
		int r1=(int)(r*6)+1;
		int r3=(int)(r2*6)+1;
		int r5=(int)(r4*6)+1;
		
		if (eenDobbelsteenRadio.isSelected()==true) {
			ogen[r1]++;
			table.setValueAt(experiment+1, experiment, 0);
			for (int i=0;i<6;i++) {
				table.setValueAt(ogen[i+1], experiment, i+1);
			}
		}
		if (tweeDobbelstenenRadio.isSelected()==true) {
			ogen[r1+r3]++;
			table1.setValueAt(experiment+1, experiment, 0);
			for (int i=0;i<11;i++) {
				table1.setValueAt(ogen[i+2], experiment, i+1);
			}
		}
		if (drieDobbelstenenRadio.isSelected()==true) {
			ogen[r1+r3+r5]++;
			table2.setValueAt(experiment+1, experiment, 0);
			for (int i=0;i<16;i++) {
				table2.setValueAt(ogen[i+3], experiment, i+1);
			}
		}
		
		dobbelsteenCount++;
		dobbelstenenGrafiek.repaint();
		dobbelstenenSomGrafiek.repaint();
		
		if (dobbelsteenCount==maxCount) {
			stopCounting=true;
			for (int i=0;i<19;i++) {
				ogenGemiddeld[i]=(ogenGemiddeld[i]*experiment+ogen[i])/(experiment+1);
				ogenSom[i]=ogenSom[i]+ogen[i];
			}
			if (eenDobbelsteenRadio.isSelected()==true) {
				for (int i=0;i<6;i++) {
					double dummy=Math.round(ogenGemiddeld[i+1]*100);
					table3.setValueAt(Double.toString(dummy/100), 0,i+1);
				}
			}
			if (tweeDobbelstenenRadio.isSelected()==true) {
				for (int i=0;i<11;i++) {
					double dummy=Math.round(ogenGemiddeld[i+2]*100);
					table4.setValueAt(Double.toString(dummy/100), 0,i+1);
				}
			}
			if (drieDobbelstenenRadio.isSelected()==true) {
				for (int i=0;i<16;i++) {
					double dummy=Math.round(ogenGemiddeld[i+3]*100);
					table5.setValueAt(Double.toString(dummy/100), 0,i+1);
				}
			}
			experiment++;
			fireCBook();
			voeruit.setEnabled(true);
		}
	}
	
	public void run() {
	 	while (animatie!=null && stopCounting==false)
   		{
	   		this.doeStap();
	   		try{ Thread.sleep(10); } catch (Exception e) {}
   		}
	}
}
