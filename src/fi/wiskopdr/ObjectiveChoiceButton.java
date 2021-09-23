package fi.wiskopdr;

import fi.wiskopdr.domainmodel.StudentModel;
import fi.wiskopdr.domainmodel.StudentModelChoicePanel;
import fi.wiskopdr.domainmodel.StudentObjective;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

import javax.swing.*;
import javax.swing.tree.TreeModel;

public class ObjectiveChoiceButton extends WiskOpdrButton implements ActionListener
{
    private static final String[] NULSTRINGS = new String[0];

    class ObjectivesFacade implements ObjectiveChoices, PropertyChangeListener {
      
      JLabel titleLabel = new JLabel();

      public void setTitle(String title) {
        this.titleLabel.setText(title);
      }
      
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
        if(choices != null) // NPE op volgende regel
        for(int i = 0; i < choices.length; i++) {
          boolean choice[] = choices[i];
          StudentObjective[] objs = studentModel.get().categories[i].objectives;
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
           StudentObjective[] objs = studentModel.get().categories[j].objectives;
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
      JPanel titlePanel = new JPanel(new BorderLayout());
      titleLabel.setFont(new Font("SansSerif",Font.PLAIN, 24));
      titleLabel.setForeground(WiskOpdr.colorGray3);
      
      Box headerBox = Box.createHorizontalBox();
      headerBox.setOpaque(true);
      headerBox.setBackground(WiskOpdr.colorBlue1);
      headerBox.add(Box.createHorizontalGlue());
      headerBox.add(titleLabel);
      headerBox.add(Box.createHorizontalGlue());
      headerBox.setBorder(BorderFactory.createEmptyBorder(0,20,5,20));
      titlePanel.add(headerBox, BorderLayout.NORTH);

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
      titlePanel.addPropertyChangeListener("enabled", this);
      titlePanel.add(scrollPane, BorderLayout.CENTER);
      return titlePanel;

    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
      if ("enabled".equals(evt.getPropertyName())) {
        boolean value = evt.getNewValue().equals(Boolean.TRUE);
        for(int i = 0 ; i < choicesCBs.length; i++) {
          for (int j = 0; j < choicesCBs[i].length; j++) {
            choicesCBs[i][j].setEnabled(value);
          }
        }
      }
     }
    @Override
    public TreeModel getTreeModel() {
      return null;
    }
    }
  
    ObjectiveChoices strategy = new ObjectivesFacade();

    public boolean[][] getChoices(){   
      return strategy.getChoices();
    }
    public String[] getObjectives() {
      List<String> obj = strategy.getObjectives();
      if (obj != null) // can be null!
        return obj.toArray(NULSTRINGS);
      else
        return null;
    }

    public void setChoices (boolean[][] choices)
    {
      strategy.setChoices(choices);
    }

    public void setObjectives(String[] obj) {
      if (obj != null)
        strategy.setObjectives(Arrays.asList(obj));
      else
        strategy.setObjectives(Collections.emptyList());
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
	private Supplier<StudentModel> studentModel;
	private JButton okButton; 
	private JButton cancelButton;
	
	private JPanel objectivesPanel = new JPanel();
	private JPanel bottomPanel = new JPanel();
	private JPanel topPanel = new JPanel();
	private Component scrollPane;
	private String labelString;
	
	private WiskOpdrGlobalVarState globalVarState;
	
	public static boolean hasObjectiveChoices() {
	  return WiskOpdr.objectives !=  null || WiskOpdr.studentModelSupplier != null; 
	}

	/**
	 * Voor gebruik in widgets. 
	 * Geen referenties naar objectives of studentmodel.
	 * Jargon
	 * <pre>
	 * if (ObjectiveChoiceButton.hasObjectiveChoices() { button = new ObjectiveChoiceButton() }
	 * </pre>
	 */
	public ObjectiveChoiceButton() {
	  this(WiskOpdr.rb.getString("OPT_objectives"), WiskOpdr.objectives, WiskOpdr.categorieString, WiskOpdr.studentModelSupplier);
	}
	
	
//	// in oude geodefiner
//	@Deprecated
//	public ObjectiveChoiceButton(String[][] objectives, String[] categorieString) {	
//		this(WiskOpdr.rb.getString("OPT_objectives"), objectives, categorieString, WiskOpdr.studentModel);
//	}

	@Deprecated
	public ObjectiveChoiceButton(String[][] objectives, String[] categorieString, StudentModel model) {
	  this(WiskOpdr.rb.getString("OPT_objectives"), objectives, categorieString,model);
	}
 
	/**
	 * Voor gebruik bij misconcepties.
	 * @param labelString
	 * @param objectives
	 * @param categorieString
	 */
	public ObjectiveChoiceButton(String labelString, String[][] objectives, String[] categorieString) {
      this(labelString, objectives, categorieString, (Supplier<StudentModel>)null);
    }
	
	public ObjectiveChoiceButton(String labelString, String[][] objectives, String[] categorieString, Supplier<StudentModel> studentModelGetter) {
	  super(labelString);
      this.labelString = labelString;
      this.objectives = objectives;
      this.categorieString = categorieString;
      this.studentModel = studentModelGetter;
      addActionListener(this);
      if (studentModel != null) {
//        System.out.println("storeCurrentGlobalVars");
//        globalVarState = new WiskOpdrGlobalVarState();
//        globalVarState.storeCurrentGlobalVars();
          strategy = new StudentModelChoicePanel(studentModel);
      }
	}
	
	public ObjectiveChoiceButton(String labelString, String[][] objectives, String[] categorieString, StudentModel studentModel)
	{	super(labelString);
		this.labelString = labelString;
		this.objectives = objectives;
		this.categorieString = categorieString;
		this.studentModel = studentModel == null ? null : () -> studentModel;
		addActionListener(this);
		if (studentModel != null) {
//			System.out.println("storeCurrentGlobalVars");
//			globalVarState = new WiskOpdrGlobalVarState();
//			globalVarState.storeCurrentGlobalVars();
			strategy = new StudentModelChoicePanel(this.studentModel);
		}
	}
	
	
    

    
    
    public void makeGUI(){
    		bottomPanel = new JPanel(new BorderLayout());
		bottomPanel.setBackground(WiskOpdr.colorGray2);
		bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
		
		topPanel = new JPanel(new BorderLayout());
		topPanel.setBackground(WiskOpdr.colorBlue1);
		topPanel.setBorder(BorderFactory.createEmptyBorder(0, 5, 5, 20));
		
		strategy.setTitle(WiskOpdr.rb.getString("OBJ_koppelLeerdoelTitel"));
        scrollPane = strategy.makeGUI();
        
//        okButton = new JButton("Ok");
//        okButton.addActionListener(this);
//        bottomPanel.add(okButton);
//        
//        cancelButton = new JButton("Cancel");
//        cancelButton.addActionListener(this);
//        bottomPanel.add(cancelButton);
        
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
         
        hb.add(Box.createHorizontalGlue());
        bottomPanel.add(hb);
        
    }
    
    public void makeFrame(){
    	frame = DialogFacade.newInstance(this, "", true);
    	//Dimension preferredSize = new Dimension(400,320);
		//frame.setPreferredSize(preferredSize);
        frame.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        //frame.setSize(preferredSize);
        frame.getContentPane().setLayout(new BorderLayout());
        frame.getContentPane().add(scrollPane);
        frame.getContentPane().add(topPanel,BorderLayout.NORTH);
        frame.getContentPane().add(bottomPanel,BorderLayout.SOUTH);
       
		frame.pack();
		 Dimension screenSize = WiskOpdr.applet.getToolkit().getScreenSize();
	      int x = (screenSize.width-frame.getSize().width)/2;
	      int y = (screenSize.height-frame.getSize().height)/2;
	      frame.setLocation(x , y);	    
	    frame.addWindowListener(new WindowAdapter() {
	    		public void windowClosed(WindowEvent e) {
	    		  frame = null;
	    		}
	    });
        frame.setVisible(true);
	    
    }
	
	public void actionPerformed(ActionEvent e){
		if(e.getSource().equals(this) && frame==null){	
			makeGUI();
			makeFrame();
            strategy.close();			
		}
		else if(e.getSource().equals(okButton)) {   
			makeChoices();
        	frame.setVisible(false);
            frame.dispose();
            frame=null;
//            if(studentModel!=null && globalVarState != null   ) {
//            	System.out.println("setStoredGlobalVars");
//				globalVarState.setStoredGlobalVars();
//				globalVarState = null;
//            }
        }
		else if(e.getSource().equals(cancelButton)) {   
			frame.getContentPane().removeAll();
			frame.setVisible(false);
            frame.dispose();
            frame=null;
//            if(studentModel!=null && globalVarState != null) {
//				globalVarState.setStoredGlobalVars();
//				globalVarState = null;
//            }
        }
	}
  public String[] getDeselections() {
    List<String> deselections = strategy.getDeselections();
    return deselections.toArray(NULSTRINGS);
  }
  
  public void setDeselections(String[] deselections) {
    if (deselections == null) deselections = NULSTRINGS;
    strategy.setDeselections(Arrays.asList(deselections));
  }
}

