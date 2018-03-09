package fi.wiskopdr;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.*;

import javax.swing.*;

import fi.wiskopdr.WiskOpdr.StudentModel;
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
	private JButton importButton;
	
	JPanel objectivesPanel = new JPanel();
	JPanel bottomPanel = new JPanel();
	JScrollPane scrollPane;
	
	private String buttonLabel;
	private String rowLabel;
	private String columnLabel;
	private StudentModel studentModel;
		
	public ObjectiveSettingsButton(){	
		this(WiskOpdr.rb.getString("OPT_objectives"), WiskOpdr.rb.getString("OBJ_leerdoel"), WiskOpdr.rb.getString("OBJ_categorie"));
	}
	
	public ObjectiveSettingsButton(String buttonLabel, String rowLabel, String columnLabel){	
		super(buttonLabel);
		this.buttonLabel = buttonLabel;
		this.rowLabel = rowLabel;
		this.columnLabel = columnLabel;
		addActionListener(this);
	}
	
	public void setObjectives(String[][] objectives){   
		this.objectives = objectives;
	}
	
	public void setCategories(String[] categorieString){   
		this.categorieString = categorieString;
	}
	
	public void setStudentModelID(String id) {
		studentModel = null;
		if (id == null) return;
		for( StudentModel s: WiskOpdr.applet.getStudentModels()) {
			if( s != null && s.id.equals(id))
			{
				studentModel = s;
				break;
			}
		}
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
    		if(categorieString[i].equals(columnLabel + " "  + (i+1)))
    			categorieString[i] = "";
    	}	
    }
    
    
    public String[][] getObjectives(){   
    	return objectives;
    }
    
    public String[] getCategories(){
    	return categorieString;
    }
    
    public String getStudentModelID() {
    		if(studentModel != null) {
    			return studentModel.id;
    		}
    		return null;
    }
    
    public void makeTextFields()
    {	objectiveTextFields = new JTextField[maxCategories][maxObjectives];
	    objectiveLabels = new JLabel[maxObjectives];
	    categoryTextFields = new JTextField[maxCategories];
	    for(int j = 0; j < maxCategories; j++)
	    {	categoryTextFields[j] = new JTextField(columnLabel + " "  + (j+1));
	    	categoryTextFields[j].setPreferredSize(new Dimension(180,20));
	    }
	    for(int i=0 ; i<maxObjectives ; i++)
	    {	objectiveLabels[i] = new JLabel(rowLabel + " " +(i+1));
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
        
        importButton = new JButton("Import");
        importButton.addActionListener(this);
        if (WiskOpdr.isExperimental())
        		bottomPanel.add(importButton);
        
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
			boolean m = studentModel == null;
			makeGUI(aantalRijen, aantalKolommen);
			aantalKolommenKnop.setEnabled(m);
			aantalRijenKnop.setEnabled(m);
			for (int j = 0 ; objectives!=null &&  j < objectives.length; j++)
			for (int i = 0 ; objectives[j]!=null && i < objectives[j].length; i++){	
				objectiveTextFields[j][i].setText(objectives[j][i]);
				objectiveTextFields[j][i].setEnabled(m);
				categoryTextFields[j].setText(categorieString[j]);
				categoryTextFields[j].setEnabled(m);
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
		else if(e.getSource().equals(importButton)) {
			JComboBox<StudentModel> combo = new JComboBox<>(WiskOpdr.applet.getStudentModels());
			combo.setSelectedItem(studentModel);
			int result = JOptionPane.showConfirmDialog(importButton, combo, "Importeer model", JOptionPane.OK_CANCEL_OPTION);
			if(result == JOptionPane.OK_OPTION) {
				result = combo.getSelectedIndex();
				System.out.println("import model " + result);
				if(result <= 0) {
					studentModel = null;
					for(JTextField t: categoryTextFields) t.setEnabled(true);
					for(JTextField[] tt: objectiveTextFields) for(JTextField t: tt) t.setEnabled(true);
					aantalKolommenKnop.setEnabled(true);
					aantalRijenKnop.setEnabled(true);
					return;
				}
				studentModel = (StudentModel) combo.getSelectedItem();
				aantalKolommen = studentModel.categories.length;
				aantalRijen = studentModel.getMaxObjectives();
				makeGUI(aantalRijen, aantalKolommen);
				aantalKolommenKnop.setEnabled(false);
				aantalRijenKnop.setEnabled(false);
				for(int i = 0;i < aantalKolommen; i++) {
					categoryTextFields[i].setText(studentModel.categories[i].category);
					categoryTextFields[i].setEnabled(false);
					
					for (int j = 0; j < aantalRijen; j ++) {
						int n = studentModel.categories[i].objectives.length;
						String s = n < j ? "" : studentModel.categories[i].objectives[j];
						objectiveTextFields[i][j].setText(s);
						objectiveTextFields[i][j].setEnabled(false);
					}
				}
				frame.getContentPane().removeAll();
				frame.getContentPane().add(scrollPane);
				frame.getContentPane().add(bottomPanel, BorderLayout.SOUTH);
				frame.pack();
						
			} else {
				System.out.println("import canceled");
			}
		}
	}   
}