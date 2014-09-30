package fi.kansbomen;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JTextField;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Window;
import java.awt.event.*;
import java.util.Hashtable;

import javax.swing.*;



public class NakijkModelButton extends JButton implements ActionListener
	{
		private JFrame frame; //was: DialogFacade (zie geogebraParams)
		//private JTextField[] objectTextFields;
		//private JTextField[] scoreTextFields;
	    //private JLabel[] objectLabels;
		
		//private int[] scores;
		private int scoreMax = 10;
		private JButton okButton; 
		private JButton cancelButton;
		
		JPanel paramPanel = new JPanel();
		JPanel bottomPanel = new JPanel();
		JScrollPane scrollPane;
		

		int offset=5;
		int width;
		int height;
		int currentX;
		int currentY;
		int editWidth = 170;

		Font theFont;
		FontMetrics theFM;
		Font theBoldFont;
		FontMetrics theBoldFM;
		
		JComboBox terugleggenBox;
		JComboBox trekkingenBox, optiesBox;
		JLabel aantalTrekkingen, aantalOptiesLabel, maxScoreLabel;
		JLabel[] aantalOptie = new JLabel[7];
		String[] naamOptieTekst = new String[]  
		          {"dummy", Kansbomen.rb.getString("naam1StringTekst"),
			Kansbomen.rb.getString("naam2StringTekst"),
			Kansbomen.rb.getString("naam3StringTekst"),
			Kansbomen.rb.getString("naam4StringTekst"),
			Kansbomen.rb.getString("naam5StringTekst"),
			Kansbomen.rb.getString("naam6StringTekst")};
		JTextField[] aantalOptieVeld;
		JTextField maxScoreVeld;
		
		boolean terugleggen = true;
		int terugleggenKeuze = 0;
		int trekkingen = 2;
		int aantalOpties = 2;
		int[] aantalInt = new int[]  {4,4,4,4,4,4,4};
		int[] aantalIntOud = new int[] {4,4,4,4,4,4,4};
		int breedteAantalVeld;
		
		int[] nakijkModel = new int[] {10, 4, 4, 4, 4, 4, 4, 0, 2, 2};
		boolean[] itemsEnabled = new boolean[] {true, true, true, true};
		
		
		public NakijkModelButton(){	
			super(Kansbomen.rb.getString("nakijkModelTekst"));
			addActionListener(this);
		}
		
		public void setScoreMax(int scoreMax){   
	        this.scoreMax = scoreMax;
	    }
	    
		public void maakNakijkModel()
		{ //haal huidige waarden uit invoervelden.
			scoreMax = Integer.parseInt(maxScoreVeld.getText());
			terugleggenKeuze = terugleggenBox.getSelectedIndex();
			trekkingen = trekkingenBox.getSelectedIndex();
			aantalOpties = optiesBox.getSelectedIndex();
			for(int i = 1; i < 7; i++)
				aantalInt[i] = Integer.parseInt(aantalOptieVeld[i].getText());
			updateNakijkModel();
		}
		
	    public int getScoreMax(){   
	    	return scoreMax;
	    }
	    
	    public int[] getNakijkModel(){
	    	return nakijkModel;
	    }
	    
	    public void makeGUI(){
	    	paramPanel = new JPanel();
			bottomPanel = new JPanel();
	        
			paramPanel.setLayout(null);
			
	        theFont = new Font("Dialog", Font.PLAIN, 12);
	    	theFM = getFontMetrics(theFont);
	    	theBoldFont = new Font("Dialog", Font.BOLD, 12);
	    	theBoldFM = getFontMetrics(theBoldFont);
	    	
	    	width = editWidth - 2 * offset;
	    	height = 3 * theFM.getHeight() / 2;
	    	currentX = offset;
	    	currentY = offset;
	    	
	    	String[] teruglegKeuzes = { Kansbomen.rb.getString("metTerugleggenTekst"), Kansbomen.rb.getString("zonderTerugleggenTekst")};
	    	terugleggenBox = new JComboBox(teruglegKeuzes);
	    	terugleggenBox.setSelectedIndex(terugleggenKeuze);
	    	terugleggenBox.setFont(theFont);
	    	terugleggenBox.setBounds(currentX, currentY, width, height);
	    	paramPanel.add(terugleggenBox);
	    	
	    	currentY += height + 2 * offset;
	    	
	    	aantalTrekkingen.setFont(theFont);
	    	aantalTrekkingen.setBounds(currentX, currentY, 100, height);
	    	paramPanel.add(aantalTrekkingen);
	    	
	    	currentX += aantalTrekkingen.getWidth()+ offset;
	    	
	    	String[] momentenKeuzes={"1","2","3","4","5","6"};
	    	trekkingenBox = new JComboBox(momentenKeuzes);
	    	trekkingenBox.setSelectedIndex(trekkingen);
	    	trekkingenBox.setFont(theFont);
	    	trekkingenBox.setBounds(currentX, currentY, width - aantalTrekkingen.getWidth() - offset, height);
	    	paramPanel.add(trekkingenBox);
	    	
	    	currentX -= aantalTrekkingen.getWidth()+ offset;
	    	currentY += height + 2 * offset;
	    	
	    	aantalOptiesLabel = new JLabel(Kansbomen.rb.getString("aantalOptiesTekst"));
	    	aantalOptiesLabel.setFont(theFont);
	    	aantalOptiesLabel.setBounds(currentX, currentY, 100, height);
	    	paramPanel.add(aantalOptiesLabel);
	    	
	    	currentX += aantalOptiesLabel.getWidth()+ offset;
	    	
	    	String[] optiesKeuzes={"2","3","4","5","6"};
	    	optiesBox = new JComboBox(optiesKeuzes);
	    	optiesBox.setSelectedIndex(aantalOpties);
	    	optiesBox.setFont(theFont);
	    	optiesBox.setBounds(currentX, currentY, width - aantalOptiesLabel.getWidth() - offset, height);
	    	paramPanel.add(optiesBox);
	    	optiesBox.addActionListener(this);
	    	
	    	currentX -= aantalOptiesLabel.getWidth()+ offset;
	    	currentY += height + offset;
	    	
	    	breedteAantalVeld = theFM.stringWidth("000")+ 2 * offset;
	    			                       
	    	aantalOptieVeld = new JTextField[7];
	    	for(int i=1; i<7; i++)
	    	{	aantalOptieVeld[i] = new JTextField(""+aantalInt[i]);
	    		aantalOptieVeld[i].setFont(theFont);
	    		aantalOptieVeld[i].setBounds(currentX + width - breedteAantalVeld, currentY, breedteAantalVeld, height);
	    		currentY += height + offset;
	    	}
	    		
	    	currentY -= 6 * height + 6 * offset;
	    	
	    	for (int i=1; i<7; i++)
	    	{	aantalOptie[i].setFont(theFont);
	    		aantalOptie[i].setBounds(currentX, currentY, width - breedteAantalVeld, height);
	    		currentY += height + offset;
	    	}
	    	for(int i=1; i<7; i++)
			{
				paramPanel.add(aantalOptie[i]);
				paramPanel.add(aantalOptieVeld[i]);
			}
					    	
	    	currentY += height + 2 * offset;
	    	
	    	maxScoreLabel = new JLabel(Kansbomen.rb.getString("maxScoreTekst"));
	    	maxScoreLabel.setFont(theFont);
	    	maxScoreLabel.setBounds(currentX, currentY, 100, height);
	    	paramPanel.add(maxScoreLabel);
	    	
	    	currentX += maxScoreLabel.getWidth()+ offset;
	    			
	    	maxScoreVeld = new JTextField("10");
	    	maxScoreVeld.setFont(theFont);
	    	maxScoreVeld.setBounds(currentX, currentY, width - 100 - offset, height);
    		paramPanel.add(maxScoreVeld);
    			    	
	    	okButton = new JButton("Ok");
	        okButton.addActionListener(this);
	        bottomPanel.add(okButton);
	        
	        cancelButton = new JButton("Cancel");
	        cancelButton.addActionListener(this);
	        bottomPanel.add(cancelButton);
	        
			scrollPane = new JScrollPane(paramPanel);
	    }
	    
		public void zetTeksten(String[] naamoptietekst, String trekkingMvTekst)
		{
			aantalTrekkingen = new JLabel(Kansbomen.rb.getString("aantalTekst") + trekkingMvTekst+":");
	    	for(int i = 1; i<7; i++)
			{
				aantalOptie[i] = new JLabel(Kansbomen.rb.getString("aantalTekst")+naamoptietekst[i]+":");
			}
		}
		
		public void zetAantalOpties(int j)
		{
			for(int i = 1; i < j+1; i++)
			{
				aantalOptie[i].setVisible(true);
				aantalOptieVeld[i].setVisible(true);
			}
			for(int i = j+1; i < 7; i++)
			{
				aantalOptie[i].setVisible(false);
				aantalOptieVeld[i].setVisible(false);
			}
		}

	    public void makeFrame(){
	    	frame = new JFrame();
	    	Dimension preferredSize = new Dimension(250,400);
			frame.setPreferredSize(preferredSize);
	        frame.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
	        frame.setSize(preferredSize);
	        frame.getContentPane().setLayout(new BorderLayout());
	        frame.getContentPane().add(scrollPane);
	        frame.getContentPane().add(bottomPanel,BorderLayout.SOUTH);
			frame.pack();
		    frame.setVisible(true);
		    
	    }
		
	    public void updateNakijkModel()
	    {
	       	nakijkModel[0] = scoreMax;
	    	for(int i = 1; i < 7; i++)
	    		nakijkModel[i] = aantalInt[i];
	    	nakijkModel[7] = terugleggenKeuze;
	    	nakijkModel[8] = trekkingen;
	    	nakijkModel[9] = aantalOpties;
	    }
	    
	    public void updateBeginwaarden(int[] nakijkmodel)
	    {
	    	scoreMax = nakijkmodel[0];
	    	for(int i = 1; i < 7; i++)
	    		aantalInt[i] = nakijkModel[i];
	    	
	    	terugleggenKeuze = nakijkmodel[7];
	    	trekkingen = nakijkmodel[8];
	    	aantalOpties = nakijkmodel[9];
	    }
	    
	    
		public void actionPerformed(ActionEvent e){
			if(e.getSource().equals(this) && frame==null){	
				makeGUI();
				terugleggenBox.setSelectedIndex(terugleggenKeuze);
				trekkingenBox.setSelectedIndex(trekkingen);
				optiesBox.setSelectedIndex(aantalOpties);
				for(int i = 1; i < 7; i++)
				{	aantalOptieVeld[i].setText(""+aantalInt[i]);
					aantalOptieVeld[i].setEnabled(itemsEnabled[3]);
				}	
				terugleggenBox.setEnabled(itemsEnabled[0]);
				trekkingenBox.setEnabled(itemsEnabled[1]);
				optiesBox.setEnabled(itemsEnabled[2]);
				maxScoreVeld.setText(""+scoreMax);
				makeFrame();
			}
			else if(e.getSource().equals(optiesBox))
				zetAantalOpties(optiesBox.getSelectedIndex()+2);
			else if(e.getSource().equals(okButton)) {   
				maakNakijkModel();
	        	frame.setVisible(false);
	            frame.dispose();
	            frame=null;
	        }
			else if(e.getSource().equals(cancelButton)) {   
				frame.getContentPane().removeAll();
				frame.setVisible(false);
	            frame.dispose();
	            frame=null;
	        }
		}
	}
