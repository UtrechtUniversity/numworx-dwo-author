package fi.wiskopdr;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.function.Supplier;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import fi.wiskopdr.domainmodel.StudentModel;
import fi.wiskopdr.opdrnav.PlusMinKnop;
import fi.beans.numworxlf.JScrollPane;
import fi.beans.numworxlf.JComboBox;
import fi.beans.numworxlf.JOptionPane;

@SuppressWarnings("serial")
public class ObjectiveSettingsButton extends WiskOpdrButton implements ActionListener
{
	private Font font = new Font("SansSerif",Font.PLAIN,13);
	
	private DialogFacade frame;
	private JTextField[][] objectiveTextFields;
	private JLabel[] objectiveLabels;
	private JTextField[] categoryTextFields;
	
	private String[] categorieString;
	
	private int maxObjectives  = 20;
	private int maxCategories = 10; // Sietske heeft er 7 in haar nieuwe domainmodel
	int aantalRijen = 4;
	int aantalKolommen = 1;
	PlusMinKnop aantalRijenKnop;
	PlusMinKnop aantalKolommenKnop;
	private String[][] objectives;
	private JButton okButton; 
	private JButton cancelButton;
	private JButton importButton;
	
	JPanel objectivesPanel = new JPanel();
	JPanel mainPanel = new JPanel();
	JPanel bottomPanel = new JPanel();
	JPanel topPanel = new JPanel();
	JScrollPane scrollPane;
	
	private JLabel titleLabel;
	private JRadioButton studentModelRB;
	private JRadioButton eigenLeerdoelenRB;
	private ButtonGroup buttonGroep;
	
	private String buttonLabel;
	private String rowLabel;
	private String columnLabel;
	private StudentModel studentModel;
	private String studentModelId;
	
	private JComboBox<StudentModel> leerdomeinCombobox = new JComboBox<>(/*WiskOpdr.applet.getStudentModels()*/);
	
		
	public ObjectiveSettingsButton(){	
		this(WiskOpdr.rb.getString("OPT_objectivesButton"), WiskOpdr.rb.getString("OBJ_leerdoel"), WiskOpdr.rb.getString("OBJ_categorie"));
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
		studentModelId = id;
	}
	
	private void requestStudentModel() {
	  String id = studentModelId;
	  if (id == null || studentModel != null) return;
		for( StudentModel s: WiskOpdr.applet.getStudentModels()) {
			if( s != null && s.id.equals(id))
			{
				studentModel = s;
				objectives = null;
				leerdomeinCombobox.setSelectedItem(studentModel);
				//leerdomeinCombobox.setVisible(true);
				//studentModelRB.setSelected(true);
				//eigenLeerdoelenRB.setSelected(true);
				break;
			}
	  }
	}
	
	private void makeObjects(){   
    	objectives = null;
    	for(int j=0 ; j<maxCategories ; j++)
    	{	String checkObject = objectiveTextFields[j][0].getText();
   			if(checkObject==null || "".equals(checkObject.trim()))
   			{	objectives = new String[j][];
   				break;
   			}
    	}
        if(objectives == null)
          objectives = new String[maxCategories][];
    	for(int j=0 ; j<objectives.length ; j++)
    	{	
// we stoppen nu bij het eerste lege veld,
// gewenst is dat we stopen bij het laatste lege veld
    	
    	  ArrayList<String> strings = new ArrayList<>(maxObjectives);
    	  for(int i=0 ; i<aantalRijen ; i++){   
	        	String checkObject = objectiveTextFields[j][i].getText();
	       		if(checkObject!=null && !"".equals(checkObject.trim())){	
	       			strings.add(checkObject);
	            }
	        }
	        if(objectives[j]==null){
	        	objectives[j] = new String[strings.size()];
	        }
	        strings.toArray(objectives[j]);
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
    		return studentModelId;
    }
    
    public void makeTextFields()
    {	objectiveTextFields = new JTextField[maxCategories][maxObjectives];
	    objectiveLabels = new JLabel[maxObjectives];
	    categoryTextFields = new JTextField[maxCategories];
	    for(int j = 0; j < maxCategories; j++)
	    {	categoryTextFields[j] = new JTextField(columnLabel + " "  + (j+1));
		    	categoryTextFields[j].setPreferredSize(new Dimension(180,20));
		    	categoryTextFields[j].setForeground(WiskOpdr.colorBlue1);
	    }
	    for(int i=0 ; i<maxObjectives ; i++)
	    {	objectiveLabels[i] = new JLabel(rowLabel + " " +(i+1));
	    		objectiveLabels[i].setForeground(WiskOpdr.colorBlue1);
	   		objectiveLabels[i].setPreferredSize(new Dimension(100,20));
	   		for(int j = 0; j<maxCategories; j++)
	        {	objectiveTextFields[j][i] = new JTextField("");
	        		objectiveTextFields[j][i].setForeground(WiskOpdr.colorBlue1);
	    			objectiveTextFields[j][i].setPreferredSize(new Dimension(180,20));
	        }
	    }
    }
    
    public void makeGUI(int aantalRijen, int aantalKolommen){
    		objectivesPanel = new JPanel();
    		objectivesPanel.setBackground(WiskOpdr.colorGray3);
    		objectivesPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 50, 30));
    		
    		mainPanel = new JPanel(new BorderLayout());
    		mainPanel.setBackground(WiskOpdr.colorGray3);
    		mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
    		
		bottomPanel = new JPanel(new BorderLayout());
		bottomPanel.setBackground(WiskOpdr.colorGray2);
		bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
		
		topPanel = new JPanel(new BorderLayout());
		topPanel.setBackground(WiskOpdr.colorBlue1);
		topPanel.setBorder(BorderFactory.createEmptyBorder(5, 20, 5, 20));
		
		titleLabel = new JLabel(WiskOpdr.rb.getString("OBJ_leerdoelInstellingen"));
		titleLabel.setFont(new Font("SansSerif",Font.PLAIN, 24));
		titleLabel.setForeground(WiskOpdr.colorGray3);
		
		Box headerBox = Box.createHorizontalBox();		
		headerBox.add(Box.createHorizontalGlue());
		headerBox.add(titleLabel);
		headerBox.add(Box.createHorizontalGlue());
		topPanel.add(headerBox, BorderLayout.NORTH);
		
        
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
        
        buttonGroep = new ButtonGroup();
        
        studentModelRB = new WiskOpdrRadioButton(WiskOpdr.rb.getString("OBJ_gebruikAanwezigLeerdomein"));
        studentModelRB.setFont(font);
        studentModelRB.setSelected(studentModelId!=null);
        studentModelRB.addActionListener(this);
        buttonGroep.add(studentModelRB);
        
        eigenLeerdoelenRB = new WiskOpdrRadioButton(WiskOpdr.rb.getString("OBJ_gebruikEigenLeerdoelen"));
        eigenLeerdoelenRB.setFont(font);
        eigenLeerdoelenRB.setSelected(studentModelId==null);
        eigenLeerdoelenRB.addActionListener(this);
        
        buttonGroep.add(eigenLeerdoelenRB);
        
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
        
        Box hb = Box.createHorizontalBox();
        hb.add(Box.createHorizontalGlue());
        
        okButton = new WiskOpdrButton("Ok");
        okButton.setPreferredSize(new Dimension(70,24));
		okButton.setBackground(WiskOpdr.colorBlue1);
		okButton.setForeground(WiskOpdr.colorGray3);
        okButton.addActionListener(this);
        hb.add(okButton);
        hb.add(Box.createHorizontalStrut(20));
        
        cancelButton = new WiskOpdrButton("Cancel");
        cancelButton.setPreferredSize(new Dimension(70,24));
		cancelButton.setBackground(WiskOpdr.colorBlue1);
		cancelButton.setForeground(WiskOpdr.colorGray3);
        cancelButton.addActionListener(this);
        hb.add(cancelButton);
         
        importButton = new WiskOpdrButton("Import");
        importButton.addActionListener(this);
        if (WiskOpdr.isPremium())
        		//hb.add(importButton);
        
        hb.add(Box.createHorizontalGlue());
        bottomPanel.add(hb);
        
		scrollPane = new JScrollPane(objectivesPanel);
		scrollPane.setBorder(BorderFactory.createEmptyBorder());
		scrollPane.setBackground(WiskOpdr.colorGray3);
		scrollPane.setVisible(studentModelId==null);
		
		leerdomeinCombobox.setVisible(studentModelId!=null);
		if (leerdomeinCombobox.isVisible())
		{
		  StudentModel[] studentModels = WiskOpdr.applet.getStudentModels();
		  for(StudentModel s: studentModels) {
		    if (s != null && s.id .equals(studentModelId)) { studentModel = s; break; }
		  }
          leerdomeinCombobox.setModel(new DefaultComboBoxModel<>(studentModels));
          leerdomeinCombobox.setSelectedItem(studentModel);
		}
		leerdomeinCombobox.setForeground(WiskOpdr.colorBlue1);
		leerdomeinCombobox.setPreferredSize(new Dimension(360,22));
		leerdomeinCombobox.setMaximumSize(new Dimension(360,22));
		leerdomeinCombobox.setMinimumSize(new Dimension(360,22));
		leerdomeinCombobox.addActionListener(this);
		Box vb = Box.createVerticalBox();
		if (WiskOpdr.isPremium()) {
			hb = Box.createHorizontalBox();
			hb.add(studentModelRB) ; hb.add(Box.createHorizontalGlue());
			vb.add(hb);
			hb = Box.createHorizontalBox();
			hb.add(eigenLeerdoelenRB) ; hb.add(Box.createHorizontalGlue());
			vb.add(hb);
			vb.add(Box.createVerticalStrut(20));
			hb = Box.createHorizontalBox();
			hb.add(leerdomeinCombobox) ; hb.add(Box.createHorizontalGlue());
			    //vb.add(Box.createVerticalStrut(20));
			    vb.add(hb);
		}
	    vb.add(Box.createVerticalStrut(20));
	    vb.add(scrollPane);
	    vb.add(Box.createVerticalGlue());
	    
	    mainPanel.add(vb);
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
        frame.getContentPane().add(mainPanel);
        frame.getContentPane().add(bottomPanel,BorderLayout.SOUTH);
        frame.getContentPane().add(topPanel,BorderLayout.NORTH);
       
       
		frame.pack();
		 Dimension screenSize = WiskOpdr.applet.getToolkit().getScreenSize();
	      int x = (screenSize.width-frame.getSize().width)/2;
	      int y = (screenSize.height-frame.getSize().height)/2;
	      frame.setLocation(x , y);
	    frame.setVisible(true);
	   
    }
	
	public void actionPerformed(ActionEvent e)
	{	if(e.getSource().equals(this) && frame==null)	
		{	makeTextFields();
		
			if(objectives != null && objectives.length > 0)
			{	aantalKolommen = objectives.length;
			    aantalRijen = 1;
				for(int i = 0; i < objectives.length; i++)
						if(objectives[i] != null && objectives[i].length > aantalRijen)
							aantalRijen = objectives[i].length;
			}
			boolean m = studentModelId == null;
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
				frame.getContentPane().add(mainPanel);
				frame.getContentPane().add(bottomPanel, BorderLayout.SOUTH);
				frame.getContentPane().add(topPanel, BorderLayout.NORTH);
				frame.pack();
			}
			if(e.getActionCommand().equals("min") && aantalRijen < maxObjectives)
			{	makeGUI(aantalRijen + 1, aantalKolommen);
				aantalRijen++;
				frame.getContentPane().removeAll();
				frame.getContentPane().add(mainPanel);
				frame.getContentPane().add(bottomPanel,BorderLayout.SOUTH);
				frame.getContentPane().add(topPanel, BorderLayout.NORTH);
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
				frame.getContentPane().add(mainPanel);
				frame.getContentPane().add(bottomPanel, BorderLayout.SOUTH);
				frame.getContentPane().add(topPanel, BorderLayout.NORTH);
				frame.pack();
			}
			if(e.getActionCommand().equals("plus") && aantalKolommen < maxCategories)
			{	makeGUI(aantalRijen, aantalKolommen + 1);
				aantalKolommen++;
				frame.getContentPane().removeAll();
				frame.getContentPane().add(mainPanel);
				frame.getContentPane().add(bottomPanel,BorderLayout.SOUTH);
				frame.getContentPane().add(topPanel, BorderLayout.NORTH);
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
		else if(e.getSource().equals(studentModelRB)) {
			scrollPane.setVisible(!studentModelRB.isSelected());
			leerdomeinCombobox.setModel(new DefaultComboBoxModel<>(WiskOpdr.applet.getStudentModels()));
			leerdomeinCombobox.setVisible(studentModelRB.isSelected());
			if(studentModelRB.isSelected())
				objectives = null;
			frame.pack();
		}
		else if(e.getSource().equals(eigenLeerdoelenRB)) {
			scrollPane.setVisible(eigenLeerdoelenRB.isSelected());
			leerdomeinCombobox.setVisible(!eigenLeerdoelenRB.isSelected());
			if(eigenLeerdoelenRB.isSelected()) {
				studentModel = null;
				studentModelId = null;
				aantalKolommenKnop.setEnabled(true);
				aantalRijenKnop.setEnabled(true);
				leerdomeinCombobox.setSelectedIndex(0);
			}
			frame.pack();
		}
		else if(e.getSource().equals(leerdomeinCombobox)) {
			studentModel = (StudentModel) leerdomeinCombobox.getSelectedItem();
			studentModelId = studentModel != null ? studentModel.id: null;
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
					studentModelId = null;
					for(JTextField t: categoryTextFields) t.setEnabled(true);
					for(JTextField[] tt: objectiveTextFields) for(JTextField t: tt) t.setEnabled(true);
					aantalKolommenKnop.setEnabled(true);
					aantalRijenKnop.setEnabled(true);
					return;
				}
				studentModel = (StudentModel) combo.getSelectedItem();
				studentModelId = studentModel.id;
				aantalKolommen = studentModel.categories.length;
				aantalKolommen = Math.min(maxCategories, aantalKolommen);
				aantalRijen = studentModel.getMaxObjectives();
				aantalRijen = Math.min(maxObjectives, aantalRijen);
				makeGUI(aantalRijen, aantalKolommen);
				aantalKolommenKnop.setEnabled(false);
				aantalRijenKnop.setEnabled(false);
				for(int i = 0;i < aantalKolommen; i++) {
					categoryTextFields[i].setText(studentModel.categories[i].category);
					categoryTextFields[i].setToolTipText(studentModel.categories[i].description);
					categoryTextFields[i].setEnabled(false);
					
					for (int j = 0; j < aantalRijen; j ++) {
						int n = studentModel.categories[i].objectives.length;
						String s = n <= j ? "" : studentModel.categories[i].objectives[j].objective;
						objectiveTextFields[i][j].setText(s);
						s = n <= j ? "" : studentModel.categories[i].objectives[j].description;
						objectiveTextFields[i][j].setToolTipText(s);
						objectiveTextFields[i][j].setEnabled(false);
					}
				}
				frame.getContentPane().removeAll();
				frame.getContentPane().add(mainPanel);
				frame.getContentPane().add(bottomPanel, BorderLayout.SOUTH);
				frame.getContentPane().add(topPanel, BorderLayout.NORTH);
				frame.pack();
						
			} else {
				System.out.println("import canceled");
			}
		}
	}

  public StudentModel getStudentModel() {
    requestStudentModel();
    return WiskOpdr.applet.expandStudentModel(studentModel);
  }   
  
  public Supplier<StudentModel> getStudentModelSupplier() {
    if (studentModelId == null) 
      return null;
    return this::getStudentModel;
  }
}