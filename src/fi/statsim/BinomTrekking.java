package fi.statsim;

import java.awt.Color;

import javax.swing.JRadioButton;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
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

import org.cbook.cbookif.CBookEvent;
import org.cbook.cbookif.CBookEventHandler;
import org.cbook.cbookif.CBookEventListener;

import fi.beans.wiskopdrbeans.CBookAware;

public class BinomTrekking extends JPanel implements ActionListener, FocusListener, Runnable{
	
    JPanel panel1;
	Border border1;
	JLabel kansLabel;
	JTextField kansText;
	JLabel aantalTrekkingenLabel;
	JTextField aantalTrekkingenText;
	JLabel voerLabel;
	JTextField aantalKeer;
	JButton voeruit;
	JButton keer;
	JButton stap;
	JButton wis;
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
	Boolean[] trekkingenGeschiedenis;
	
	Boolean showTabel=true;
	Boolean showGrafiek=true;
	Boolean showFrequentie=true;
	Boolean showRooster=true;
	Boolean showInstellingen=true;
	
	
	Boolean showKans=true;
	Boolean showPopulatieProportie=false;
	Boolean multipleTimes=false;
	int numberOfTimes;
	Boolean stapStarted=false;
	Boolean stapStarted1=false;
	
	StatSimInteractiePanel ssip;
	
	public BinomTrekking (StatSimInteractiePanel ssip) {
		
		this.ssip=ssip;
		
		setLayout(null);
		this.setBackground(Color.white);
		
		border1=BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
		panel1=new JPanel();
		panel1.setLayout(null);
		panel1.setBackground(Color.white);
		add(panel1);
		panel1.setSize(250,105);
		panel1.setLocation(0,0);
		panel1.setBorder(BorderFactory.createTitledBorder(border1,StatSim.rb.getString("settings"),TitledBorder.CENTER,TitledBorder.TOP,new Font("SansSerif", Font.PLAIN, 12)));
		
		kansLabel=new JLabel(StatSim.rb.getString("chance"));
		kansLabel.setSize(150,20);
		kansLabel.setLocation(20,30);
		kansLabel.setFont(new Font("SansSerif", Font.PLAIN, 12) );
		panel1.add(kansLabel);
		
		kansText=new JTextField("0.2");
		kansText.setSize(50,20);
		kansText.setLocation(180,30);
		panel1.add(kansText);
		
		aantalTrekkingenLabel=new JLabel(StatSim.rb.getString("numberOfDraws"));
		aantalTrekkingenLabel.setSize(100,20);
		aantalTrekkingenLabel.setLocation(20,60);
		aantalTrekkingenLabel.setFont(new Font("SansSerif", Font.PLAIN, 12) );
		panel1.add(aantalTrekkingenLabel);
				
		aantalTrekkingenText=new JTextField("20");
		aantalTrekkingenText.setSize(50,20);
		aantalTrekkingenText.setLocation(180,60);
		aantalTrekkingenText.addActionListener(this);
		aantalTrekkingenText.addFocusListener(this);
		panel1.add(aantalTrekkingenText);

	    voeruit=new JButton(StatSim.rb.getString("execute"));
	    voeruit.setSize(80,20);
	    voeruit.setLocation(260,5);
	    voeruit.addActionListener(this);
	    add(voeruit);
	    
	    stap=new JButton(StatSim.rb.getString("step"));
	    stap.setSize(80,20);
	    stap.setLocation(345,5);
	    stap.addActionListener(this);
	    add(stap);
	    
	    voerLabel = new JLabel(StatSim.rb.getString("execute1"));
	    voerLabel.setBackground(Color.white);
	    voerLabel.setSize(55,20);
	    voerLabel.setLocation(260,30);
	    add(voerLabel);
	    
	    aantalKeer = new JTextField("20");
	    aantalKeer.setSize(25,20);
	    aantalKeer.setLocation(315,30);
	    add(aantalKeer);

	    keer=new JButton(StatSim.rb.getString("times"));
	    keer.setSize(80,20);
	    keer.setLocation(345,30);
	    keer.addActionListener(this);
	    add(keer);
	    
	    wis=new JButton(StatSim.rb.getString("erase"));
	    wis.setSize(165,20);
	    wis.setLocation(260,55);
	    wis.addActionListener(this);
	    wis.setEnabled(false);
	    add(wis);
	    
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
	    binomGrafiek.setVisible(true);
	    add(binomGrafiek);
	    
	    binomRooster=new BinomRooster(this);
	    binomRooster.setLocation(200,115);
	    binomRooster.setBackground(Color.white);
	    binomRooster.setSize(590,335);
	    binomRooster.setVisible(false);
	    add(binomRooster);

	    trekkingen = new int[1000];
	    maxCount=Integer.parseInt(aantalTrekkingenText.getText());
	    trekkingenGeschiedenis = new Boolean[1000];
	}
	
	public void setZichtbaar() {
		if (showKans)
			kansLabel.setText(StatSim.rb.getString("chance"));
		if (showPopulatieProportie)
			kansLabel.setText(StatSim.rb.getString("populationProportion"));
		if (showInstellingen) {
			panel1.setVisible(showInstellingen);
			voeruit.setLocation(260,5);
			stap.setLocation(345,5);
			voerLabel.setLocation(260,30);
			aantalKeer.setLocation(315,30);
			keer.setLocation(345,30);
			wis.setLocation(260,55);
		} else {
			panel1.setVisible(showInstellingen);
			voeruit.setLocation(10,5);
			stap.setLocation(95,5);
			voerLabel.setLocation(10,30);
			aantalKeer.setLocation(65,30);
			keer.setLocation(95,30);
			wis.setLocation(10,55);
		}
		if (showTabel) {
			pane.setVisible(true);
		} else {
			pane.setVisible(false);
		}
		if (showGrafiek) {
			binomGrafiek.setVisible(showGrafiek);
			if (showRooster) {
				if (showTabel || showFrequentie) {
					binomGrafiek.setLocation((this.getWidth()-200)/2+200,115);
					binomGrafiek.setSize((this.getWidth()-200)/2,this.getHeight()-115);
					binomRooster.setLocation(200,115);
					binomRooster.setSize((this.getWidth()-200)/2,this.getHeight()-115);
				} else {
					binomGrafiek.setLocation(this.getWidth()/2,115);
					binomGrafiek.setSize(this.getWidth()/2,this.getHeight()-115);
					binomRooster.setLocation(0,115);
					binomRooster.setSize(this.getWidth()/2,this.getHeight()-115);
				}
			} else {
				if (showTabel || showFrequentie) {
					binomGrafiek.setLocation(200,115);
					binomGrafiek.setSize(this.getWidth()-200,this.getHeight()-115);
				} else {
					binomGrafiek.setLocation(0,115);
					binomGrafiek.setSize(this.getWidth(),this.getHeight()-115);
				}
			}
		} else {
			binomGrafiek.setVisible(showGrafiek);
		}
		if (showRooster) {
			if (!showGrafiek) {
				if (showTabel || showFrequentie) {
					binomRooster.setLocation(200,115);
					binomRooster.setSize(this.getWidth()-200,this.getHeight()-115);
				} else {
					binomRooster.setLocation(0,115);
					binomRooster.setSize(this.getWidth(),this.getHeight()-115);
				}		
			}
			binomRooster.setVisible(showRooster);
		} else {
			binomRooster.setVisible(showRooster);
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
	
	public void fireCBook() {
		String string1="";
		for (int i=0;i<experiment;i++) {
			string1=string1+table.getValueAt(i, 1)+"\n";
		}
				
		ssip.fireCBookBinomTrekking(string1);		
	}
	
	public void setStartStop() {
		if (wis.isEnabled()==true) {
			kansText.setEnabled(false);
			aantalTrekkingenText.setEnabled(false);
		} else {
			kansText.setEnabled(true);
			aantalTrekkingenText.setEnabled(true);
		}
	}
	
	
	public void actionPerformed(ActionEvent e) {
		if (e.getSource()==voeruit) {
			voeruit.setEnabled(false);
			keer.setEnabled(false);
			stap.setEnabled(false);
			wis.setEnabled(true);
			setStartStop();
			stopCounting=false;
			if (!stapStarted1) {
				trekkingCount=0;
				totaal=0;
			} else {
				stapStarted1=false;
			}
			stapStarted=true;
			maxCount=Integer.parseInt(aantalTrekkingenText.getText());
			animatie=new Thread(this);
			animatie.start();   
			
		}
		if (e.getSource()==stap) {
			
			
			if (!stapStarted1) {
				voeruit.setEnabled(true);
				keer.setEnabled(false);
				wis.setEnabled(true);
				setStartStop();
				stapStarted1=true;
				trekkingCount=0;
				totaal=0;
			}
			doeStap();
			if (trekkingCount==maxCount) {
				stapStarted1=false;
			}
			
		}
		if (e.getSource()==keer) {
			stopCounting=false;
			trekkingCount=0;
			totaal=0;
			keer.setEnabled(false);
			voeruit.setEnabled(false);
			numberOfTimes=Integer.parseInt(aantalKeer.getText());
			maxCount=Integer.parseInt(aantalTrekkingenText.getText());
			multipleTimes=true;
			animatie=new Thread(this);
			animatie.start();
			
		}
		if (e.getSource()==wis) {
			wis.setEnabled(false);
			voeruit.setEnabled(true);
			keer.setEnabled(true);
			setStartStop();
			experiment=0;
			for (int i=0;i<1000;i++) {
			   table.setValueAt("",i,0);
			   table.setValueAt("",i,1);
			}
			stapStarted=false;
			trekkingCount=0;
			totaal=0;
			binomFrequentie.repaint();
			binomGrafiek.repaint();
			binomRooster.repaint();
			fireCBook();
		}
		if (e.getSource()==aantalTrekkingenText) {
			maxCount=Integer.parseInt(aantalTrekkingenText.getText());
			binomFrequentie.repaint();
			binomGrafiek.repaint();
			binomRooster.repaint();
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
			trekkingenGeschiedenis[trekkingCount]=true;
		} else {
			trekkingenGeschiedenis[trekkingCount]=false;
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
				fireCBook();
				keer.setEnabled(true);
				voeruit.setEnabled(true);
				stap.setEnabled(true);
			} else {
				numberOfTimes--;
				if (numberOfTimes==0) {
					stopCounting=true;
					multipleTimes=false;
					fireCBook();
					keer.setEnabled(true);
					voeruit.setEnabled(true);
					stap.setEnabled(true);
				}
				trekkingCount=0;
				totaal=0;
			}
		}
		
		binomFrequentie.repaint();
		binomRooster.repaint();
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

	@Override
	public void focusGained(FocusEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void focusLost(FocusEvent arg0) {
		if (arg0.getSource()==aantalTrekkingenText) {
			maxCount=Integer.parseInt(aantalTrekkingenText.getText());
			binomFrequentie.repaint();
			binomGrafiek.repaint();
			binomRooster.repaint();
		}
	}

}
