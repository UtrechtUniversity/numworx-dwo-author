package fi.wiskopdr.domainmodel;

import java.awt.Component;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.JTree.DynamicUtilTreeNode;
import javax.swing.tree.DefaultTreeModel;

import fi.wiskopdr.ObjectiveChoices;

public class StudentModelChoicePanel extends JPanel implements ObjectiveChoices {

  JTree tree;
  DefaultTreeModel model;
  DynamicUtilTreeNode root;
  JLabel title;
  JTextArea description;

  public StudentModelChoicePanel(StudentModel studentModel) {
    super(null);
    setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
    setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    NodeVector v = new NodeVector(studentModel);
    root = new DynamicUtilTreeNode(v, v);
    model = new DefaultTreeModel(root);   
    tree = new JTree(model);
    add(new JScrollPane(tree));
    Box rightBox = Box.createVerticalBox();
    add(rightBox);
    
    title = new JLabel(v.toString());
    description = new JTextArea(studentModel.description, 10, 30);
    description.setLineWrap(true);
    description.setWrapStyleWord(true);
    description.setEditable(false);
    
    rightBox.add(title);
    rightBox.add(Box.createVerticalStrut(10));
    rightBox.add(new JScrollPane(description));
  }

  boolean[][] choices;
  
  public List<String> getObjectives() {
    return null;
  }
  
  public boolean[][] getChoices() {
    return choices;
  }
  
  public void setObjectives(List<String> objectives) {
    
  }
  
  public void setChoices(boolean[][] choices) {
    this.choices = choices;
  }

  @Override
  public void makeChoices() {
    // TODO Auto-generated method stub
    
  }

  @Override
  public Component makeGUI() {
    return this;
  }
}
