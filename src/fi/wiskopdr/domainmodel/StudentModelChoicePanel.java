package fi.wiskopdr.domainmodel;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.EventObject;
import java.util.List;

import javax.swing.AbstractCellEditor;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JToggleButton;
import javax.swing.JTree;
import javax.swing.JTree.DynamicUtilTreeNode;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeCellEditor;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreePath;

import fi.wiskopdr.ObjectiveChoices;
import fi.wiskopdr.WiskOpdr;
import fi.wiskopdr.WiskOpdrPanel;
import fi.wiskopdr.tekstobjects.TekstImageVak;

public class StudentModelChoicePanel extends JPanel implements ObjectiveChoices, TreeSelectionListener {
  private class LeafNodeEditor extends AbstractCellEditor implements TreeCellEditor {

    private static final int XWIDTH = 20; // positie [x]
    private  ChoiceCellRenderer renderer = new ChoiceCellRenderer();
    private  ChangeEvent changeEvent = null;
    private  JTree tree;
    private  NodeLeaf leaf;

     public LeafNodeEditor(JTree tree) {
         this.tree = tree;
     }

     public Object getCellEditorValue() {
         JCheckBox checkbox = renderer.getLeafRenderer();
         leaf.setValue(checkbox.isSelected());
         return leaf;
     }

     @Override
     public boolean isCellEditable(EventObject event) {
         boolean returnValue = false;
         if (event instanceof MouseEvent) {
             MouseEvent mouseEvent = (MouseEvent) event;
             TreePath path = tree.getPathForLocation(mouseEvent.getX(),
                     mouseEvent.getY());
             if (path != null) {
                 Rectangle rect = tree.getPathBounds(path);
                 int pos = mouseEvent.getX() - rect.x;
                if (pos > XWIDTH) return false;
                 Object node = path.getLastPathComponent();
                 if ((node != null) && (node instanceof DefaultMutableTreeNode)) {
                     DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode) node;
                     Object userObject = treeNode.getUserObject();
                     returnValue = ((treeNode.isLeaf()) && (userObject instanceof NodeLeaf));
                 }
             }
         }
         return returnValue;
     }

     public Component getTreeCellEditorComponent(JTree tree, Object value,
             boolean selected, boolean expanded, boolean leaf, int row) {
         Component editor = renderer.getTreeCellRendererComponent(tree, value,
                 true, expanded, leaf, row, true);
         // editor always selected / focused
         ItemListener itemListener = new ItemListener() {

             public void itemStateChanged(ItemEvent itemEvent) {
                 if (stopCellEditing()) {
                     fireEditingStopped();
                     //model.nodeStructureChanged(root);
                     repaint();
                 }
             }  
         };
         if (editor instanceof JCheckBox) {
             ((JCheckBox) editor).addItemListener(itemListener);
         }
         this.leaf = (NodeLeaf) ((DefaultMutableTreeNode) value).getUserObject();
         return editor;
     }
 }

  public static class ChoiceCellRenderer implements TreeCellRenderer {

    private JCheckBox    leafRenderer = new JCheckBox();
    private JRadioButton nonLeafRenderer = new JRadioButton();
    private Color selectionBorderColor, selectionForeground, selectionBackground,
            textForeground, textBackground;

    protected JCheckBox getLeafRenderer() {
        return leafRenderer;
    }

    public ChoiceCellRenderer() {
        Font fontValue;
        fontValue = UIManager.getFont("Tree.font");
        if (fontValue != null) {
            leafRenderer.setFont(fontValue);
            nonLeafRenderer.setFont(fontValue);
        }
        Boolean booleanValue = (Boolean) UIManager.get("Tree.drawsFocusBorderAroundIcon");
        leafRenderer.setFocusPainted((booleanValue != null) && (booleanValue.booleanValue()));
        nonLeafRenderer.setFocusPainted((booleanValue != null) && (booleanValue.booleanValue()));
        selectionBorderColor = UIManager.getColor("Tree.selectionBorderColor");
        selectionForeground = UIManager.getColor("Tree.selectionForeground");
        selectionBackground = UIManager.getColor("Tree.selectionBackground");
        textForeground = UIManager.getColor("Tree.textForeground");
        textBackground = UIManager.getColor("Tree.textBackground");
    }

    public Component getTreeCellRendererComponent(JTree tree, Object value,
            boolean selected, boolean expanded, boolean leaf, int row,
            boolean hasFocus) {
        JToggleButton returnValue;
        if (leaf) {
          returnValue = leafRenderer;
        } else
          returnValue = nonLeafRenderer;
    
          String stringValue = tree.convertValueToText(value, selected,
            expanded, leaf, row, false);
          returnValue.setText(stringValue);
          returnValue.setSelected(false);
          returnValue.setEnabled(tree.isEnabled());
          if (selected) {
            returnValue.setForeground(selectionForeground);
            returnValue.setBackground(selectionBackground);
          } else {
            returnValue.setForeground(textForeground);
            returnValue.setBackground(textBackground);
          }
          if ((value != null) && (value instanceof DefaultMutableTreeNode)) {
            Object userObject = ((DefaultMutableTreeNode) value).getUserObject();
            if (userObject instanceof Node) {
              Node node = (Node) userObject;
              returnValue.setText(node.toString());
              returnValue.setSelected(node.isValue());
            }
          }
        return returnValue;
    }

  }

  JTree tree;
  DefaultTreeModel model;
  DynamicUtilTreeNode root;
  JLabel title;
  JTextArea description;
  final StudentModel studentModel;
  static final String WISKOPDR_SIG = "H4sIAAAAAA";

  public StudentModelChoicePanel(StudentModel studentModel) {
    super(null);
    this.studentModel = studentModel;
    setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
    setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    NodeVector v = new NodeVector(studentModel);
    root = new DynamicUtilTreeNode(v, v);
    model = new DefaultTreeModel(root);   
    tree = new JTree(model);
    //tree.setMinimumSize(new Dimension(200,100));
    //tree.setPreferredSize(tree.getMinimumSize());
    tree.setCellEditor(new LeafNodeEditor(tree));
    tree.setEditable(true);
    tree.setCellRenderer(new ChoiceCellRenderer());
    Box leftBox = Box.createVerticalBox();
    add(leftBox);
    leftBox.add(new JLabel(v.toString()));
    leftBox.add(Box.createVerticalStrut(10));
    JScrollPane sp = new JScrollPane(tree);
    leftBox.add(sp);
    sp.setMinimumSize(new Dimension(200,100));
    sp.setPreferredSize(sp.getMinimumSize());

    add(Box.createHorizontalStrut(10));
    Box rightBox = Box.createVerticalBox();
    add(rightBox);
    
    title = new JLabel(v.toString());
    String descr = v.getDescription();
    description = new JTextArea(descr, 10, 30);
    description.setLineWrap(true);
    description.setWrapStyleWord(true);
    description.setEditable(false);
    scroll = new JScrollPane(description);
    if (descr.startsWith(WISKOPDR_SIG))
    {
      WiskOpdrPanel panel = getWiskOpdrPanel(descr);
      scroll.setViewportView(panel);
    }
    
    
    rightBox.add(title);
    rightBox.add(Box.createVerticalStrut(10));
    rightBox.add(scroll);
    
    tree.addTreeSelectionListener(this);
  }

  private boolean[][] choices;
  private List<String> ids;
  private JScrollPane scroll;
  
  public List<String> getObjectives() {
    return ids;
  }
  
  private void getObjectives(Object v, List<String> ids) {
    if (v instanceof NodeLeaf) {
      NodeLeaf leaf = (NodeLeaf) v;
      if (leaf.isValue())
        ids.add(leaf.getId());
    } else if (v instanceof NodeVector) {
      NodeVector vector = (NodeVector) v;
      vector.stream().forEach(item -> getObjectives(item, ids));
    }
  }

  public boolean[][] getChoices() {
    return choices;
  }
  
  public void setObjectives(List<String> objectives) {
    ids = objectives;
  }
  
  public void setChoices(boolean[][] choices) {
    this.choices = choices;
    this.ids = null;  // if you forget setObjectives!
  }

  @Override
  public void makeChoices() {
// new style
    ids = new ArrayList<>();
    getObjectives(root.getUserObject(), ids);
// old style
    int x = studentModel.categories.length;
    int y = studentModel.getMaxObjectives();
    choices = new boolean[x][y];
    NodeVector v = (NodeVector) root.getUserObject();
    for (x = 0; x < v.size(); x ++) {
      NodeVector w = (NodeVector) v.get(x);
      for (y = 0; y < w.size(); y++) {
        Object e = w.get(y);
        if (e instanceof NodeLeaf) {
          choices[x][y] = ((NodeLeaf) e).isValue();
        }
        
      }
      
    }
    
  }

  @Override
  public Component makeGUI() {
// old style
    if (choices != null) {
      NodeVector v = (NodeVector) root.getUserObject();
      int maxx = Math.min(v.size(),choices.length);
      for (int x = 0; x < maxx; x ++) {
        NodeVector w = (NodeVector) v.get(x);
        int maxy = Math.min(w.size(), choices[x].length);
        for (int y = 0; y < maxy; y++) {
          Object e = w.get(y);
          if (e instanceof NodeLeaf) {
            ((NodeLeaf) e).setValue(choices[x][y]);
          }
        }
    }}
 // new style 
    if (ids != null) {
      @SuppressWarnings("unchecked")
      Enumeration<DefaultMutableTreeNode> all = root.depthFirstEnumeration();
      while (all.hasMoreElements()) {
        DefaultMutableTreeNode node = all.nextElement();
        Object u = node.getUserObject();
        if (u instanceof NodeLeaf) {
          NodeLeaf leaf = (NodeLeaf) u;
          leaf.setValue(ids.contains(leaf.getId()));
        }
      }
    }
    model.nodeStructureChanged(root);
    return this;
  }

  @Override
  public void valueChanged(TreeSelectionEvent e) {
    if (e.isAddedPath()) {
      TreePath path = tree.getSelectionPath();
      if (path == null) {
        title.setText("");
        description.setText("");
        scroll.setViewportView(description);
        return;
      }
      DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
      Object u = node.getUserObject();
      title.setText(u.toString());
      if (u instanceof Node) {
        String descr = ((Node) u).getDescription();
        if (descr == null) descr = "";
        if (descr.startsWith(WISKOPDR_SIG)) {
          WiskOpdrPanel panel = getWiskOpdrPanel(descr);
          scroll.setViewportView(panel);
        } else {
          description.setText(descr);
          scroll.setViewportView(description);
        }
      } else {
        description.setText("");
        scroll.setViewportView(description);
      }
    }   
    repaint();
  }

  private WiskOpdrPanel getWiskOpdrPanel(String descr) {
    Object save = TekstImageVak.getImageMap();
    try {
      WiskOpdrPanel panel = WiskOpdr.getWiskOpdrPanel(descr, WiskOpdr.language);
      panel.setBackground(Color.WHITE);
      return panel;
    } finally {
      TekstImageVak.setImageMap(save);
    }
  }
}
