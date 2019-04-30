package fi.wiskopdr;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JTable;

import fi.wiskopdr.domainmodel.StudentModel;
import fi.wiskopdr.domainmodel.StudentModelChoicePanel;
import fi.wiskopdr.domainmodel.StudentObjective;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Window;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;

import javax.swing.*;

public class ObjectiveChoiceButton extends JButton implements ActionListener
{
    class ObjectivesFacade implements ObjectiveChoices {
      public boolean[][] getChoices(){   
        return choices;
      }
      public void setChoices (boolean[][] choices)
      {
        ObjectiveChoiceButton.this.choices = choices;
      }
     public List<String> getObjectives() {
        if (studentModel == null) return null;
        List<String> ids = new ArrayList<>();
        // 2D -> Flat model:
        for(int i = 0; i < choices.length; i++) {
          boolean choice[] = choices[i];
          StudentObjective[] objs = studentModel.categories[i].objectives;
          for (int j = 0; j < objs.length; j++) {
            if (choice[j])
              ids.add(objs[j].id);
          }
        }
       return ids;
      }

     public void setObjectives(List<String> obj) {
       if (studentModel == null) return;
       choices = new boolean[objectives.length][];
       for(int j=0 ; j<objectives.length ; j++)
       {   choices[j] = new boolean[objectives[j].length];
           StudentObjective[] objs = studentModel.categories[j].objectives;
           for(int i=0; i<objectives[j].length; i++){   
           choices[j][i] = obj.contains(objs[i].id);
           }
       }
     }
     public void makeChoices(){   
       if(objectives==null) return;
       choices = new boolean[objectives.length][];
       for(int j=0 ; j<objectives.length ; j++)
       {   choices[j] = new boolean[objectives[j].length];
           for(int i=0; i<objectives[j].length; i++){   
           choices[j][i] = choicesCBs[j][i].isSelected();
           }
       }
    }
    
    public Component makeGUI() {
      objectivesPanel = new JPanel();
      
      Box boxv = Box.createVerticalBox();
      
      Box boxh = Box.createHorizontalBox();
      boxh.add(Box.createHorizontalStrut(10));
      
     
      JLabel label = new JLabel(labelString);
      boxh.add(label);
      
      boxv.add(boxh);
      boxv.add(Box.createVerticalStrut(10));
      
      if(objectives!=null)
      {   categorieLabels = new JLabel[objectives.length];
          choicesCBs = new JCheckBox[objectives.length][];
          int maxLength = 0;
          for (int j = 0; j < objectives.length; j++)
              if(objectives[j].length > maxLength)
                  maxLength = objectives[j].length;
          Box boxh2 = Box.createHorizontalBox();
          for(int j = 0; j < objectives.length; j++)
          {   Box boxv2 = Box.createVerticalBox();
              categorieLabels[j] = new JLabel(categorieString[j]);
              categorieLabels[j].setPreferredSize(new Dimension(160,20));
              boxv2.add(categorieLabels[j]);
              choicesCBs[j] = new JCheckBox[objectives[j].length];
              for(int i=0 ; i<objectives[j].length ; i++){
                  choicesCBs[j][i] = new JCheckBox(objectives[j][i]);
                  if(choices!=null && choices.length>j && choices[j].length>i )choicesCBs[j][i].setSelected(choices[j][i]);
                  choicesCBs[j][i].setPreferredSize(new Dimension(160,20));
                  boxv2.add(choicesCBs[j][i]);
              }
              boxv2.add(Box.createHorizontalGlue());
              boxv2.add(Box.createVerticalStrut(25 * maxLength - 25 * objectives[j].length));
              boxh2.add(boxv2);
          }
          boxv.add(boxh2);
          objectivesPanel.add(boxv);
      }
      scrollPane = new JScrollPane(objectivesPanel);
      return scrollPane;

    }
    }
  
    ObjectiveChoices strategy = new ObjectivesFacade();

    public boolean[][] getChoices(){   
      return strategy.getChoices();
    }
    public String[] getObjectives() {
      return strategy.getObjectives().toArray(new String[0]);
    }

    public void setChoices (boolean[][] choices)
    {
      strategy.setChoices(choices);
    }

    public void setObjectives(String[] obj) {
      if (obj != null)
        strategy.setObjectives(Arrays.asList(obj));
    }

    private void makeChoices() {
      strategy.makeChoices();
    }
	private DialogFacade frame;
	private JCheckBox[][] choicesCBs;
	private JLabel[] categorieLabels;
	private boolean[][] choices;
	
	//private int maxObjectives = 20;
	
	private String[][] objectives;
	private String[] categorieString;
	private StudentModel studentModel;
	private JButton okButton; 
	private JButton cancelButton;
	
	private JPanel objectivesPanel = new JPanel();
	private JPanel bottomPanel = new JPanel();
	private Component scrollPane;
	private String labelString;
	
	public ObjectiveChoiceButton(String[][] objectives, String[] categorieString) {	
		this(WiskOpdr.rb.getString("OPT_objectives"), objectives, categorieString, WiskOpdr.studentModel);
	}

	public ObjectiveChoiceButton(String[][] objectives, String[] categorieString, StudentModel model) {
	  this(WiskOpdr.rb.getString("OPT_objectives"), objectives, categorieString,model);
	}
    public ObjectiveChoiceButton(String labelString, String[][] objectives, String[] categorieString) {
      this(labelString, objectives, categorieString, null);
    }
	
	public ObjectiveChoiceButton(String labelString, String[][] objectives, String[] categorieString, StudentModel studentModel)
	{	super(labelString);
		this.labelString = labelString;
		this.objectives = objectives;
		this.categorieString = categorieString;
		this.studentModel = studentModel;
		addActionListener(this);
		if (studentModel != null) {
		  strategy = new StudentModelChoicePanel(studentModel);
		}
	}
	
	
    

    
    
    public void makeGUI(){
        bottomPanel = new JPanel();

        scrollPane = strategy.makeGUI();
        okButton = new JButton("Ok");
        okButton.addActionListener(this);
        bottomPanel.add(okButton);
        
        cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(this);
        bottomPanel.add(cancelButton);
        
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
	
	public void actionPerformed(ActionEvent e){
		if(e.getSource().equals(this) && frame==null){	
			makeGUI();
			makeFrame();
		}
		else if(e.getSource().equals(okButton)) {   
			makeChoices();
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

