package fi.wiskopdr;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JDialog;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.*;

import javax.swing.*;

import fi.wiskopdr.opdrnav.PlusMinKnop;

public class ObjectiveSettingsButton extends JButton implements ActionListener
{
	private DialogFacade frame;
	private JTextField[][] objectiveTextFields;
	private JLabel[] objectiveLabels;
	private JTextField[] categoryTextFields;
	
	private String[] categorieString;
	
	private int maxObjectives  = 20;
	private int maxCategories = 6;
	int aantalRijen = 4;
	int aantalKolommen = 1;
	PlusMinKnop aantalRijenKnop;
	PlusMinKnop aantalKolommenKnop;
	private String[][] objectives;
	private JButton okButton; 
	private JButton cancelButton;
	
	JPanel objectivesPanel = new JPanel();
	JPanel bottomPanel = new JPanel();
	JScrollPane scrollPane;
		
	public ObjectiveSettingsButton(){	
		super(WiskOpdr.rb.getString("OPT_objectives"));
		addActionListener(this);
	}
	
	public void setObjectives(String[][] objectives){   
		this.objectives = objectives;
	}
	
	public void setCategories(String[] categorieString){   
		this.categorieString = categorieString;
	}
	
	private void makeObjects(){   
    	objectives = null;
    	String[] newObjects = null;
    	for(int j=0 ; j<maxCategories ; j++)
    	{	String checkObject = objectiveTextFields[j][0].getText();
   			if(checkObject==null || "".equals(checkObject.trim()))
   			{	objectives = new String[j][];
   				break;
   			}
   			if(objectives == null)
   				objectives = new String[maxCategories][];
    	}
    	for(int j=0 ; j<objectives.length ; j++)
    	{	newObjects = new String[maxObjectives];
	        for(int i=0 ; i<maxObjectives ; i++){   
	        	String checkObject = objectiveTextFields[j][i].getText();
	       		if(checkObject!=null && !"".equals(checkObject.trim())){	
	       			newObjects[i] = checkObject;
	            }
	            else{   
	            	objectives[j] = new String[i];
	            	break;
	            }
	        }
	        if(objectives[j]==null){
	        	objectives[j] = new String[maxObjectives];
	        }
	        for(int i=0 ; i<objectives[j].length ; i++){
	        	objectives[j][i] = newObjects[i];
	        }
    	}
    	categorieString = null;
    	categorieString = new String[objectives.length];
    	for(int i = 0; i < objectives.length; i++)
    	{	categorieString[i] = categoryTextFields[i].getText();
    		if(categorieString[i].equals(WiskOpdr.rb.getString("OBJ_categorie")+ " "  + (i+1)))
    			categorieString[i] = "";
    	}	
    }
    
    
    public String[][] getObjectives(){   
    	return objectives;
    }
    
    public String[] getCategories(){
    	return categorieString;
    }
    
    public void makeTextFields()
    {	objectiveTextFields = new JTextField[maxCategories][maxObjectives];
	    objectiveLabels = new JLabel[maxObjectives];
	    categoryTextFields = new JTextField[maxCategories];
	    for(int j = 0; j < maxCategories; j++)
	    {	categoryTextFields[j] = new JTextField(WiskOpdr.rb.getString("OBJ_categorie")+ " "  + (j+1));
	    	categoryTextFields[j].setPreferredSize(new Dimension(180,20));
	    }
	    for(int i=0 ; i<maxObjectives ; i++)
	    {	objectiveLabels[i] = new JLabel(WiskOpdr.rb.getString("OBJ_leerdoel")+ " " +(i+1));
	   		objectiveLabels[i].setPreferredSize(new Dimension(100,20));
	   		for(int j = 0; j<maxCategories; j++)
	        {	objectiveTextFields[j][i] = new JTextField("");
	        	objectiveTextFields[j][i].setPreferredSize(new Dimension(180,20));
	        }
	    }
    }
    
    public void makeGUI(int aantalRijen, int aantalKolommen){
    	objectivesPanel = new JPanel();
		bottomPanel = new JPanel();
        
		Box boxh1 = Box.createHorizontalBox();
		Box boxv = Box.createVerticalBox();
        Box boxh = Box.createHorizontalBox();
        
        JLabel leegLabel = new JLabel("");
        leegLabel.setPreferredSize(new Dimension(100,20));
        boxh.add(leegLabel);
        
        for(int j = 0; j < aantalKolommen; j++)
        	boxh.add(categoryTextFields[j]);
        
        boxv.add(boxh);
        
        for(int i = 0; i < aantalRijen; i++)
        {	boxh = Box.createHorizontalBox();
        	boxh.add(objectiveLabels[i]);
        	for(int j = 0; j < aantalKolommen; j++)
        		boxh.add(objectiveTextFields[j][i]);
        	boxv.add(boxh);
        }
        
        boxh = Box.createHorizontalBox();
        boxh.add(Box.createHorizontalStrut(20));
        
        aantalRijenKnop = new PlusMinKnop(0, 0, 16, 20, PlusMinKnop.VERTIKAAL);
        aantalRijenKnop.setPreferredSize(new Dimension(16, 20));
        aantalRijenKnop.setSize(getPreferredSize());
        aantalRijenKnop.addActionListener(this);
        boxh.add(aantalRijenKnop);
        
        boxv.add(boxh);
        
        boxh1.add(boxv);
        aantalKolommenKnop = new PlusMinKnop(0, 0, 20, 16, PlusMinKnop.HORIZONTAAL);
        aantalKolommenKnop.setPreferredSize(new Dimension(20, 16));
        aantalKolommenKnop.setSize(getPreferredSize());
        aantalKolommenKnop.addActionListener(this);
        boxh1.add(aantalKolommenKnop);
        
        objectivesPanel.add(boxh1);
        
        okButton = new JButton("Ok");
        okButton.addActionListener(this);
        bottomPanel.add(okButton);
        
        cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(this);
        bottomPanel.add(cancelButton);
        
		scrollPane = new JScrollPane(objectivesPanel);
    }
    
    
        public void zetTextFieldsZichtbaar(boolean b){
    	
    	for(int i = 1; i < maxCategories; i++)
    	{	categoryTextFields[i].setVisible(b);
    		for(int j = 0; j < maxObjectives; j++)
    		{	objectiveTextFields[i][j].setText("");
    			objectiveTextFields[i][j].setVisible(b);
    		}
    	}
    }
    
    public void makeFrame(){
    	frame = DialogFacade.newInstance(this, "", true);
    	//Dimension preferredSize = new Dimension(400,320);
		//frame.setPreferredSize(preferredSize);
        frame.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        //frame.setSize(preferredSize);
        frame.getContentPane().setLayout(new BorderLayout());
        frame.getContentPane().add(scrollPane);
        frame.getContentPane().add(bottomPanel,BorderLayout.SOUTH);
		frame.pack();
	    frame.setVisible(true);
	    
    }
	
	public void actionPerformed(ActionEvent e)
	{	if(e.getSource().equals(this) && frame==null)	
		{	makeTextFields();
		
			if(objectives != null && objectives.length > 0)
			{	aantalKolommen = objectives.length;
				for(int i = 0; i < objectives.length; i++)
						if(objectives[i] != null && objectives[i].length > aantalRijen)
							aantalRijen = objectives[i].length;
			}
			
			makeGUI(aantalRijen, aantalKolommen);
			for (int j = 0 ; objectives!=null &&  j < objectives.length; j++)
			for (int i = 0 ; objectives[j]!=null && i < objectives[j].length; i++){	
				objectiveTextFields[j][i].setText(objectives[j][i]);
				categoryTextFields[j].setText(categorieString[j]);
			}
			
			makeFrame();
		}
		else if(e.getSource().equals(aantalRijenKnop))
		{	if(e.getActionCommand().equals("plus") && aantalRijen > 0)
			{	makeGUI(aantalRijen - 1, aantalKolommen);
				aantalRijen--;
				for(int j = 0; j < aantalKolommen; j++)
					objectiveTextFields[j][aantalRijen].setText("");
				
				frame.getContentPane().removeAll();
				frame.getContentPane().add(scrollPane);
				frame.getContentPane().add(bottomPanel, BorderLayout.SOUTH);
				frame.pack();
			}
			if(e.getActionCommand().equals("min") && aantalRijen < maxObjectives)
			{	makeGUI(aantalRijen + 1, aantalKolommen);
				aantalRijen++;
				frame.getContentPane().removeAll();
				frame.getContentPane().add(scrollPane);
				frame.getContentPane().add(bottomPanel,BorderLayout.SOUTH);
				frame.pack();
			}
		}
		else if(e.getSource().equals(aantalKolommenKnop))
		{	if(e.getActionCommand().equals("min") && aantalKolommen > 0)
			{	makeGUI(aantalRijen, aantalKolommen - 1);
				aantalKolommen--;
				for(int i = 0; i < aantalRijen; i++)
					objectiveTextFields[aantalKolommen][i].setText("");
				
				frame.getContentPane().removeAll();
				frame.getContentPane().add(scrollPane);
				frame.getContentPane().add(bottomPanel, BorderLayout.SOUTH);
				frame.pack();
			}
			if(e.getActionCommand().equals("plus") && aantalKolommen < maxCategories)
			{	makeGUI(aantalRijen, aantalKolommen + 1);
				aantalKolommen++;
				frame.getContentPane().removeAll();
				frame.getContentPane().add(scrollPane);
				frame.getContentPane().add(bottomPanel,BorderLayout.SOUTH);
				frame.pack();
			}
		}
		else if(e.getSource().equals(okButton)) {   
			makeObjects();
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