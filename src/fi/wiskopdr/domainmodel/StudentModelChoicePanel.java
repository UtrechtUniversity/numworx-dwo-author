package fi.wiskopdr.domainmodel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.EventObject;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.swing.AbstractCellEditor;
import javax.swing.BorderFactory;
import javax.swing.Box;
// import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JToggleButton;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.JTree.DynamicUtilTreeNode;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeCellEditor;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import fi.wiskopdr.ObjectiveChoices;
import fi.wiskopdr.ObjectivesViewAction;
import fi.wiskopdr.WiskOpdr;
import fi.wiskopdr.domainmodel.filter.FilterAction;
import fi.wiskopdr.domainmodel.graph.Graph;
import fi.beans.numworxlf.JScrollPane;
import fi.beans.numworxlf.JRadioButton;
import fi.beans.numworxlf.Constants;
import fi.beans.numworxlf.JButton;
import fi.beans.numworxlf.JCheckBox;

public class StudentModelChoicePanel extends JPanel
    implements
      ObjectiveChoices,
      TreeSelectionListener,
      AutoCloseable {

  private static final Logger LOG = Logger.getLogger(StudentModelChoicePanel.class.getName());


  static void insert(NodeVector vector, InvisibleNode node) {
    for (Object child : vector) {
      if (child instanceof NodeVector) {
        InvisibleNode parent = new InvisibleNode(child);
        insert((NodeVector) child, parent);
        node.add(parent);
      } else {
        node.add(new InvisibleNode(child, false, true));
      }
    }
  }


  public class FilterConsumer implements Consumer<Map<String, Map<String, Collection<Number>>>> {

    @Override
    public void accept(Map<String, Map<String, Collection<Number>>> t) {
      LOG.info("accept filter " + t);
      methodListener.filter(t);
      if (t.isEmpty()) {
        model.activateFilter(false);
        if (model.getRoot() != root) model.setRoot(root);
      } else {
        model.activateFilter(true);
        model.setRoot(filter(root, t, activeMethod));
      }
      model.nodeStructureChanged((TreeNode) model.getRoot());
    }

    boolean contains(Map<String, Map<String, Collection<Number>>> filter,
        Map<String, Map<String, Collection<Number>>> methodes, String activeMethod) {
      String currentKey = key(activeMethod); // XXX let op, is dit okay
      for (Map.Entry<String, Map<String, Collection<Number>>> entry : filter.entrySet()) {
        if (entry.getKey() == null) {
          // if (methodes.values().stream().allMatch(Map::isEmpty)) return true;
          if (methodes.entrySet().stream()
              .allMatch(e -> e.getValue().isEmpty() || !e.getKey().equals(currentKey)))
            return true;
          continue;
        }
        Map<String, Collection<Number>> map =
            methodes.getOrDefault(entry.getKey(), Collections.emptyMap());
        if (map.isEmpty()) {
          continue;
        }
        for (Map.Entry<String, Collection<Number>> m : entry.getValue().entrySet()) {
          Collection<Number> chapters =
              new TreeSet<>(map.getOrDefault(m.getKey(), Collections.emptySet()));
          // Integer(1) not equals Long(1L)
          Collection<Number> value = m.getValue();
          retainAllOf(chapters, value);
          if (!chapters.isEmpty()) return true;
        }
      }
      return false;
    }

    private void retainAllOf(Collection<Number> numberset, Collection<Number> value) {
      if (value.isEmpty()) {
        numberset.clear();
      }
      if (numberset.isEmpty()) return;

      Iterator<Number> iter = numberset.iterator();
      while (iter.hasNext()) {
        Number number = (Number) iter.next();
        int i = number.intValue();
        if (value.stream().allMatch(t -> t.intValue() != i)) iter.remove();

      }
    }

    InvisibleNode filter(InvisibleNode parent, Map<String, Map<String, Collection<Number>>> filter,
        String activeMethod) {
      InvisibleNode node;
      node = parent;
      @SuppressWarnings("unchecked")
      Enumeration<InvisibleNode> children = (Enumeration) node.children();
      while (children.hasMoreElements()) {
        InvisibleNode object = children.nextElement();
        filter(object, filter, activeMethod);
      }
      if (node.isLeaf() && !node.getAllowsChildren()) {
        NodeLeaf leaf = (NodeLeaf) node.getUserObject();
        Map<String, Map<String, Collection<Number>>> methodes = leaf.getMethode();
        node.setVisible(contains(filter, methodes, activeMethod));
      } else {
        int cnt = node.getChildCount(true);
        node.setVisible(cnt != 0);
      }

      return node;
    }

  }

  private class LeafNodeEditor extends AbstractCellEditor implements TreeCellEditor {

    private final class editorListener implements ItemListener {
      public void itemStateChanged(ItemEvent itemEvent) {
        if (stopCellEditing()) {
          fireEditingStopped();
          updateGraph();
          repaint();
        }
        itemEvent.getItemSelectable().removeItemListener(this);
      }
    }

    private static final int XWIDTH = 20; // positie [x]
    private ChoiceCellRenderer renderer = new ChoiceCellRenderer();
    private ChangeEvent changeEvent = null;
    private JTree tree;
    private NodeLeaf leaf;

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
        TreePath path = tree.getPathForLocation(mouseEvent.getX(), mouseEvent.getY());
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

    public Component getTreeCellEditorComponent(JTree tree, Object value, boolean selected,
        boolean expanded, boolean leaf, int row) {
      Component editor =
          renderer.getTreeCellRendererComponent(tree, value, true, expanded, leaf, row, true);
      // editor always selected / focused
      ItemListener itemListener = new editorListener();
      if (editor instanceof JCheckBox) {
        ((JCheckBox) editor).addItemListener(itemListener);
      }
      this.leaf = (NodeLeaf) ((DefaultMutableTreeNode) value).getUserObject();
      return editor;
    }
  }

  public class ChoiceCellRenderer implements TreeCellRenderer {

    private JCheckBox leafRenderer = new JCheckBox();
    private JRadioButton nonLeafRenderer = new JRadioButton();
    private Color selectionBorderColor, selectionForeground, selectionBackground, textForeground,
        textBackground;

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
      selectionForeground = Color.WHITE;// UIManager.getColor("Tree.selectionForeground");
      selectionBackground = WiskOpdr.colorBlue2;// UIManager.getColor("Tree.selectionBackground");
      textForeground = WiskOpdr.colorBlue1;// UIManager.getColor("Tree.textForeground");
      textBackground = UIManager.getColor("Tree.textBackground");
    }

    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected,
        boolean expanded, boolean leaf, int row, boolean hasFocus) {
      JToggleButton returnValue;
      if (leaf) {
        returnValue = leafRenderer;
      } else
        returnValue = nonLeafRenderer;

      String stringValue = tree.convertValueToText(value, selected, expanded, leaf, row, false);
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
      if (value instanceof DefaultMutableTreeNode) {
        Object userObject = ((DefaultMutableTreeNode) value).getUserObject();
        if (userObject instanceof Node) {
          Node node = (Node) userObject;
          String text = node.toString();
          Double factor = null;
          if (node instanceof NodeLeaf) factor = ids.get(((NodeLeaf) node).getId());
          if (factor == null) factor = 1.0;
          if (node.isValue() && node instanceof NodeLeaf && factor.doubleValue() <= 0.999)
            text += " " + factor;
          returnValue.setText(text);
          returnValue.setSelected(node.isValue());
        }
      }
      return returnValue;
    }

  }

  public static final String BEGRIPPEN_EN_VAKTAAL = "Begrippen en vaktaal";

  private class MethodListener implements ItemListener {

    InvisibleTreeModel methodModel;
    StudentMethod active = new StudentMethod();

    @Override
    public void itemStateChanged(ItemEvent e) {
      if (e.getStateChange() == ItemEvent.SELECTED) {
        if (methodModel == null) {
          Map<String, InvisibleNode> nodes = new HashMap<>();
          String method = active.getMethod();
          NodeVector uroot = new NodeVector(method);
          DefaultMutableTreeNode root = new InvisibleNode(uroot);
          List<String> books = active.getBooks();
          int bookcount = books.size();
          for (int i = 0; i < bookcount; i++) {
            NodeVector ubook = new NodeVector(books.get(i));
            InvisibleNode book = new InvisibleNode(ubook);
            root.add(book);
            uroot.add(ubook);
            List<String> chapters = active.getChapters().get(i);
            int chapsize = chapters.size();
            for (int j = 0; j < chapsize; j++) {
              NodeVector uchap = new NodeVector(chapters.get(j));
              InvisibleNode chap = new InvisibleNode(uchap);
              book.add(chap);
              ubook.add(uchap);
              String key = active.key() + "-" + book.toString() + "-" + (j + 1);
              nodes.put(key, chap);
              NodeVector ubenv = new NodeVector(BEGRIPPEN_EN_VAKTAAL);
              InvisibleNode benv = new InvisibleNode(ubenv);
              chap.add(benv);
              uchap.add(ubenv);
              key = key + "-W:";
              nodes.put(key, benv);
            }
          }
          Enumeration<DefaultMutableTreeNode> all =
              (Enumeration) ((DefaultMutableTreeNode) model.getRoot()).depthFirstEnumeration();
          while (all.hasMoreElements()) {
            Object o = all.nextElement().getUserObject();
            if (o instanceof NodeLeaf) {
              NodeLeaf nl = (NodeLeaf) o;
              List<DomStudentModelMethodInfo> methodeInfos = nl.getMethodeInfos();
              if (methodeInfos == null) continue;
              Set<String> infos = methodeInfos.stream().map(DomStudentModelMethodInfo::key)
                  .collect(Collectors.toSet());
              String title = nl.toString();
              for (String mi : infos) {
                if (title.startsWith("W:")) mi += "-W:";
                nodes.computeIfPresent(mi, (k, n) -> {
                  InvisibleNode node = new InvisibleNode(o, false, true);
                  insertMethod(n, node);
                  return n;
                });
              }
            }
          }



          methodModel = new InvisibleTreeModel(root);
          filterAction.doFilter();
        }

        tree.setModel(methodModel);
      } else {
        tree.setModel(model);
      }

    }

    public void filter(Map<String, Map<String, Collection<Number>>> t) {
      if (methodModel != null) {
        if (t.isEmpty()) {
          methodModel.activateFilter(false);
        } else {
          methodModel.activateFilter(true);
          InvisibleNode root = (InvisibleNode) methodModel.getRoot();
          Map<String, Collection<Number>> books =
              t.getOrDefault(key(activeMethod), Collections.emptyMap());
          int bookcount = root.getChildCount();
          for (int i = 0; i < bookcount; i++) {
            InvisibleNode book = (InvisibleNode) root.getChildAt(i);
            boolean showbook = books.containsKey(book.toString());
            book.setVisible(showbook);
            if (showbook) {
              Collection<Number> chapters = books.get(book.toString());
              int chaptercount = book.getChildCount();
              for (int j = 0; j < chaptercount; j++) {
                InvisibleNode node = (InvisibleNode) book.getChildAt(j);
                node.setVisible(false);
              }
              for (Number j : chapters) {
                ((InvisibleNode) book.getChildAt(j.intValue() - 1)).setVisible(true);
              }
            }
          }
        }
        methodModel.nodeStructureChanged((TreeNode) methodModel.getRoot());
      }

    }

    // private void insertMethod(InvisibleNode parent, InvisibleNode node) {
    // Node unode = (Node) node.getUserObject();
    // NodeVector uparent = (NodeVector) parent.getUserObject();
    // parent.add(node);
    // uparent.add(unode);
    // }

    private void insertMethod(InvisibleNode parent, InvisibleNode node) {
      int count = parent.getChildCount();
      String title = node.toString();
      for (int i = 0; i < count; i++) {
        TreeNode child = parent.getChildAt(i);
        if (compareMethod(title, child.toString()) < 0) {
          parent.insert(node, i);
          insertUO(parent, node);
          return;
        }
      }
      parent.add(node);
    }

    private void insertUO(InvisibleNode parent, InvisibleNode node) {
      Object po = parent.getUserObject();
      Object no = node.getUserObject();
      if (po instanceof NodeVector && no instanceof Node) {
        NodeVector vp = (NodeVector) po;
        vp.addElement((Node) no);
      }

    }

    private int compareMethod(String as, String bs) {
      boolean wa = as == BEGRIPPEN_EN_VAKTAAL;
      boolean wb = bs == BEGRIPPEN_EN_VAKTAAL;
      if (wa && !wb) return +1;
      if (!wa && wb) return -1;
      return as.compareTo(bs);
    }

    public StudentMethod getActive() {
      return active;
    }

    public void setActive(StudentMethod active) {
      this.active = active;
    }



  }



  JTree tree;
  InvisibleTreeModel model;
  InvisibleNode root;
  JLabel leerdoelTitelLabel, title;
  JTextArea description;
  JButton graphButton;
  JCheckBox methods;
  MethodListener methodListener = new MethodListener();
  Graph graph;
  String activeMethod;

  final Supplier<StudentModel> studentModel;
  private JButton filterBtn;
  private FilterAction filterAction;
  static final String WISKOPDR_SIG = "H4sIAAAAAA";
  static final String JSON_SIG = "{";
  private static final Font font = new Font("SansSerif", Font.PLAIN, 12);

  public StudentModelChoicePanel(Supplier<StudentModel> studentModel2) {
    super(new BorderLayout());
    this.studentModel = studentModel2;
    // North
    Box north = Box.createHorizontalBox();
    north.setBorder(BorderFactory.createEmptyBorder(5, 20, 5, 20));
    north.setOpaque(true);
    north.setBackground(Constants.COLOR15);
    graphButton = new JButton("Graph");
    title = new JLabel("title");
    title.setForeground(Constants.COLOR20);
    title.setFont(font.deriveFont(24f));
    north.add(Box.createHorizontalGlue());
    north.add(title);
    north.add(Box.createHorizontalGlue());
    north.add(graphButton);
    add(north, BorderLayout.NORTH);

    JSplitPane split = new JSplitPane();
    BasicSplitPaneUI sui = (BasicSplitPaneUI) BasicSplitPaneUI.createUI(split);
    split.setUI(sui);
    BasicSplitPaneDivider divider = sui.getDivider();
    divider.setBorder(BorderFactory.createEmptyBorder());
    divider.setBackground(Constants.COLOR20);
    split.setDividerSize(20);
    split.setResizeWeight(0.8);
    split.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    split.setBackground(Constants.COLOR20);
    add(split, BorderLayout.CENTER);

    tree = new JTree();
    tree.setCellEditor(new LeafNodeEditor(tree));
    tree.setEditable(true);
    tree.setCellRenderer(new ChoiceCellRenderer());
    methods = new JCheckBox("Geen methode");
    filterAction = new FilterAction(this, new FilterConsumer());
    filterBtn = new JButton(filterAction);
    methods.setEnabled(false);
    methods.addItemListener(methodListener);
    graph = new Graph();

    graph.addActionListener(new GraphTreeAction(tree));
    graph.addActionListener(this::updateGraph);

    JSplitPane leftBox = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
    leftBox.setBorder(BorderFactory.createEmptyBorder());
    leftBox.setResizeWeight(0.9);
    BasicSplitPaneUI suiLeft = (BasicSplitPaneUI) BasicSplitPaneUI.createUI(leftBox);
    leftBox.setUI(suiLeft);
    BasicSplitPaneDivider dividerLeft = sui.getDivider();
    dividerLeft.setBorder(BorderFactory.createEmptyBorder());
    dividerLeft.setBackground(Constants.COLOR20);
    leftBox.setDividerSize(20);
    JScrollPane sp = new JScrollPane(tree);
    sp.setBorder(BorderFactory.createLineBorder(WiskOpdr.colorBlue3));
    Box vbox = Box.createVerticalBox();
    Box hbox = Box.createHorizontalBox();
    vbox.add(sp);
    hbox.add(Box.createGlue());
    hbox.add(methods);
    hbox.add(filterBtn);
    hbox.add(Box.createGlue());
    vbox.add(hbox);
    leftBox.setTopComponent(vbox);
    sp.setMinimumSize(new Dimension(400, 300));
    sp.setPreferredSize(sp.getMinimumSize());

    split.setLeftComponent(leftBox);

    Box rightBox = Box.createVerticalBox();
    split.setRightComponent(rightBox);

    String descr = "";
    description = new JTextArea(descr, 10, 30);
    description.setLineWrap(true);
    description.setWrapStyleWord(true);
    description.setEditable(false);
    scroll = new JScrollPane(description);
    scroll.setBorder(BorderFactory.createLineBorder(WiskOpdr.colorBlue3));
    // scroll.setMinimumSize(new Dimension(450,300));
    scroll.setMaximumSize(new Dimension(450, 500));
    scroll.setPreferredSize(new Dimension(450, 300));
    if (descr != null && descr.startsWith(WISKOPDR_SIG)) {
      JLabel panel = new JLabel("Unsupported description");
      // panel.setPreferredSize(new Dimension(400,300));
      scroll.setViewportView(panel);
    } else if (descr != null && descr.startsWith(JSON_SIG)) {
      DescriptionBrowser b = getBrowser();
      scroll.setViewportView(b.getBrowserPanel());
      b.setDescription(descr);
    }
    slider = new JSlider(1, 10, 10);
    slider.setToolTipText("factor");
    slider.setMajorTickSpacing(3);
    Hashtable<Number, JLabel> dict = new Hashtable<>();
    dict.put(slider.getMinimum(), new JLabel("min"));
    dict.put(slider.getMaximum(), new JLabel("max"));
    slider.setLabelTable(dict);
    slider.setPaintLabels(true);
    slider.setPaintTicks(true);

    leerdoelTitelLabel = new JLabel(" ");
    leerdoelTitelLabel.setForeground(Color.WHITE);
    leerdoelTitelLabel.setBorder(BorderFactory.createEmptyBorder(4, 20, 4, 20));
    leerdoelTitelLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
    leerdoelTitelLabel.setMaximumSize(new Dimension(450, 30));

    Box hb = Box.createHorizontalBox();
    hb.setOpaque(true);
    hb.setBackground(WiskOpdr.colorBlue3);
    hb.setMinimumSize(new Dimension(450, 30));
    hb.setMaximumSize(new Dimension(450, 30));

    hb.add(leerdoelTitelLabel);
    hb.add(Box.createHorizontalGlue());

    rightBox.add(hb);
    rightBox.add(scroll);
    rightBox.add(slider);

    tree.addTreeSelectionListener(this);

    graphButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        if ("Graph".equals(graphButton.getText())) {
          graphButton.setText("Hide Graph");
          split.setResizeWeight(0);
          split.setRightComponent(graph);
          Dimension pref = sp.getPreferredSize();
          pref.width = 380;
          leftBox.setPreferredSize(pref);
          graph.setPreferredSize(new Dimension(1000, 650));
          leftBox.setBottomComponent(rightBox);

          packWindow();
        } else {
          graphButton.setText("Graph");
          split.setResizeWeight(0.5);
          Dimension pref = sp.getPreferredSize();
          pref.width = 580;
          leftBox.setPreferredSize(pref);
          split.setRightComponent(rightBox);

          packWindow();
        }

      }
    });
  }


  String key(String id) {
    if (id == null) return id;
    String[] split = id.split(";", 3);
    return split[2];
  }


  private void packWindow() {
    ((Window) SwingUtilities.getAncestorOfClass(Window.class, this)).pack();
  }

  private volatile DescriptionBrowser cache;

  private synchronized DescriptionBrowser getBrowser() {
    if (cache == null) cache = new DescriptionBrowser();
    return cache;
  }

  public synchronized void close() {
    if (cache != null) {
      cache.dispose();
      cache = null;
    }
  }

  public void setTitle(String title) {
    this.title.setText(title);
  }

  private boolean[][] choices;
  private Map<String, Double> ids;
  private JScrollPane scroll;
  private JSlider slider;
  private List<String> objectives;
  private List<String> deselections = Collections.emptyList(), deselections0 = deselections;
  private Collection<String> foreknowledge;

  public List<String> getObjectives() {
    return objectives;
  }

  private List<String> createObjectives() {
    if (ids == null) return null;
    return ids.entrySet().stream()
        .map(e -> e.getKey() + (e.getValue() != null ? ("/" + e.getValue()) : ""))
        .collect(Collectors.toList());
  }

  private void getObjectives(Object v, Map<String, Double> ids) {
    if (v instanceof NodeLeaf) {
      NodeLeaf leaf = (NodeLeaf) v;
      if (!leaf.isValue()) ids.remove(leaf.getId());
    } else if (v instanceof NodeVector) {
      NodeVector vector = (NodeVector) v;
      vector.stream().forEach(item -> getObjectives(item, ids));
    }
  }

  public boolean[][] getChoices() {
    return choices;
  }

  public void setObjectives(List<String> objectives) {
    this.objectives = objectives;
    // makeGUI();
    setObjectives();
  }

  @Override
  public void setDeselections(List<String> objectives) { // not null and copy
    if (objectives == null)
      objectives = new ArrayList<>();
    else
      objectives = new ArrayList<>(objectives);

    this.deselections = this.deselections0 = objectives;
  }

  @Override
  public List<String> getDeselections() {
    return this.deselections0;
  }


  private void setObjectives() {
    if (objectives != null) {
      ids = new HashMap<>();
      objectives.forEach(s -> {
        String[] split = s.split("/");
        ids.put(split[0], split.length > 1 ? Double.valueOf(split[1]) : null);
      });
    }
  }

  public void setChoices(boolean[][] choices) {
    this.choices = choices;
    this.ids = null; // if you forget setObjectives!
    this.objectives = null;
    this.deselections = this.deselections0 = Collections.emptyList();
  }

  @Override
  public void makeChoices() {
    // new style
    TreePath p = tree.getSelectionPath();
    if (p != null) savePath(p);
    // ids = new HashMap<>();
    getObjectives(root.getUserObject(), ids);
    objectives = createObjectives();
    deselections = deselections0 = graph.getDeselections();
    foreknowledge = calculateForeknowledge(deselections0);
    // old style
    int x = studentModel.get().categories.length;
    int y = studentModel.get().getMaxObjectives();
    choices = new boolean[x][y];
    NodeVector v = (NodeVector) root.getUserObject();
    for (x = 0; x < v.size(); x++) {
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
    tree.setSelectionPath(null);
    setObjectives();
    if (root == null) {
      StudentModel smodel = studentModel.get();
      activeMethod = smodel.activeMethod;
      NodeVector v = new NodeVector(smodel);
      root = new InvisibleNode(v);
      insert(v, root);
      model = new InvisibleTreeModel(root, true);
      tree.setModel(model);
      StudentMethod studentMethod = WiskOpdr.applet.getStudentMethod(activeMethod);
      methods.setText(studentMethod.getMethod());
      methods.setEnabled(activeMethod != null);
      methodListener.setActive(studentMethod);
      filterBtn.setEnabled(activeMethod != null);
      filterAction.setActiveMethod(activeMethod);
      graph.setModel(model, null, studentMethod);
    }
    // old style
    if (choices != null) {
      if (ids == null) ids = new HashMap<>();
      NodeVector v = (NodeVector) root.getUserObject();
      int maxx = Math.min(v.size(), choices.length);
      for (int x = 0; x < maxx; x++) {
        NodeVector w = (NodeVector) v.get(x);
        int maxy = Math.min(w.size(), choices[x].length);
        for (int y = 0; y < maxy; y++) {
          Object e = w.get(y);
          if (e instanceof NodeLeaf) {
            ((NodeLeaf) e).setValue(choices[x][y]);
            String id = ((NodeLeaf) e).getId();
            if (choices[x][y] && !ids.containsKey(id)) ids.put(id, null);
          }
        }
      }
    }
    // new style
    if (ids != null) {
      @SuppressWarnings("unchecked")
      Enumeration<DefaultMutableTreeNode> all = (Enumeration) root.depthFirstEnumeration();
      while (all.hasMoreElements()) {
        DefaultMutableTreeNode node = all.nextElement();
        Object u = node.getUserObject();
        if (u instanceof NodeLeaf) {
          NodeLeaf leaf = (NodeLeaf) u;
          leaf.setValue(ids.containsKey(leaf.getId()));
        }
      }
    } else
      ids = new HashMap<>(); // initial empty, expected not null

    updateGraph();
    graph.setDeselections(deselections);
    foreknowledge = calculateForeknowledge(deselections);
    model.nodeStructureChanged(root);

    @SuppressWarnings("unchecked")
    Enumeration<DefaultMutableTreeNode> all = (Enumeration) root.depthFirstEnumeration();
    while (all.hasMoreElements()) {
      DefaultMutableTreeNode node = all.nextElement();
      Object u = node.getUserObject();
      if (u instanceof NodeLeaf) {
        NodeLeaf leaf = (NodeLeaf) u;
        if (leaf.isValue()) {
          tree.makeVisible(new TreePath(node.getPath()));
        }
      }
    }
    return this;
  }

  private void updateGraph(ActionEvent e) {
    if ("deselections".equals(e.getActionCommand())) {
      deselections = graph.getDeselections();
      updateGraph();
    }
  }

  private void updateGraph() {

    // mogelijke optimalisatie: kennis tree eenmalig opbouwen uit ids.keyset en dan bijwerken.
    Set<String> kennis = calculateKennis();
    Set<String> voorkennis = ObjectivesViewAction.metVoorkennis(kennis, studentModel.get());
    voorkennis.removeAll(deselections);
    graph.graphNodes.forEach(n -> {
      boolean on = voorkennis.contains(n.getID());
      n.setSuccesFailScore(on ? 100.0 : null);
      if (on)
        n.setPartOfSelection(Boolean.valueOf(kennis.contains(n.getID())));
      else
        n.setPartOfSelection(null);
    });
    graph.repaint();
  }


  private Set<String> calculateKennis() {
    Set<String> kennis = new TreeSet<>();
    @SuppressWarnings("unchecked")
    Enumeration<DefaultMutableTreeNode> e = (Enumeration) root.depthFirstEnumeration();
    while (e.hasMoreElements()) {
      DefaultMutableTreeNode node = e.nextElement();
      if (!node.isLeaf()) continue;
      Object object = node.getUserObject();
      if (object instanceof NodeLeaf) {
        if (((NodeLeaf) object).isValue()) kennis.add(((NodeLeaf) object).getId());
      }
    }
    return kennis;
  }

  @Override
  public void valueChanged(TreeSelectionEvent e) {
    TreePath[] paths = e.getPaths();
    for (TreePath p : paths) {
      if (!e.isAddedPath(p)) {
        savePath(p);
      }
    }
    if (e.isAddedPath()) {
      TreePath path = tree.getSelectionPath();
      if (path == null) {
        leerdoelTitelLabel.setText("");
        description.setText("");
        scroll.setViewportView(description);
        return;
      }
      DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
      Object u = node.getUserObject();
      leerdoelTitelLabel.setText(u.toString());
      if (u instanceof Node) {
        Double factor = null;
        if (u instanceof NodeLeaf) factor = ids.get(((NodeLeaf) u).getId());
        if (factor == null) factor = 1.0;
        slider.setValue(Math.round(slider.getMaximum() * factor.floatValue()));

        String descr = ((Node) u).getDescription();
        if (descr == null) descr = "";
        if (descr.startsWith(WISKOPDR_SIG)) {
          // WiskOpdrPanel panel = getWiskOpdrPanel(descr);
          // scroll.setViewportView(panel);
          JLabel panel = new JLabel("Unsupported description");
          scroll.setViewportView(panel);
        } else if (descr.startsWith(JSON_SIG)) {
          DescriptionBrowser b = getBrowser();
          scroll.setViewportView(b.getBrowserPanel());
          b.setDescription(descr);

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

  private void savePath(TreePath p) {
    DefaultMutableTreeNode node = (DefaultMutableTreeNode) p.getLastPathComponent();
    Object u = node.getUserObject();
    if (u instanceof NodeLeaf) {
      ids.put(((NodeLeaf) u).getId(), (double) slider.getValue() / slider.getMaximum());
      model.nodeChanged(node);
    }
  }

  @Override
  public void setEnabled(boolean enabled) {
    super.setEnabled(enabled);
    tree.setEditable(enabled);
    slider.setEnabled(enabled);
  }

  @Override
  public TreeModel getTreeModel() {
    return model;
  }

  @Override
  public void setScore(Map<String, Double> map) {
    for (fi.wiskopdr.domainmodel.graph.GraphNode node : graph.graphNodes) {
      String id = node.getID();
      Double score = map.get(id);
      node.setSuccesFailScore(score);
    }
  }

  @Override
  public void setSelection(Map<String, Boolean> map) {
    for (fi.wiskopdr.domainmodel.graph.GraphNode node : graph.graphNodes) {
      String id = node.getID();
      Boolean selection = map.get(id);
      node.setPartOfSelection(selection);
    }

  }

  private Collection<String> calculateForeknowledge(List<String> deselections) {
    Set<String> kennis = calculateKennis();
    Set<String> voorkennis = ObjectivesViewAction.metVoorkennis(kennis, studentModel.get());
    voorkennis.removeAll(deselections);
    return voorkennis;
  }

  @Override
  public Collection<String> getForeknowledge() {
    return this.foreknowledge;
  }


  @Override
  public void setForeknowledge(Collection<String> foreknowledge) {
    this.foreknowledge = foreknowledge;
  }


}
