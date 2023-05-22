package fi.wiskopdr.domainmodel.graph;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.swing.AbstractButton;

import fi.beans.numworxlf.JCheckBox;

public class VoorkennisDeselector implements ActionListener{

	private ArrayList<ArrayList<JCheckBox>> checkBoxes;
	private Graph graph;
	private GraphNode voorkennisTreeNode;
	private Hashtable<JCheckBox, GraphNode> checkBoxNodes;
	private Hashtable<GraphNode, JCheckBox> nodesCheckBox;
	
	public VoorkennisDeselector(Graph graph) {
		this.graph = graph;
		checkBoxes = new ArrayList<ArrayList<JCheckBox>>();
		checkBoxNodes = new Hashtable<JCheckBox, GraphNode>();
		nodesCheckBox = new Hashtable<>();
	}
	
	public void initiate(GraphNode graphNode, ArrayList<ArrayList<GraphNode>> voorkennisNodes) {
		//finalize();
		voorkennisTreeNode = graphNode;
		for(int i=0 ; i<voorkennisNodes.size() ; i++) {
			ArrayList<GraphNode> gnList = voorkennisNodes.get(i);
			checkBoxes.add(new ArrayList<JCheckBox>());
			for(int j=0 ; j<gnList.size() ; j++) {
				GraphNode gn = gnList.get(j);
				if(graphNode==gn)
					break;
				JCheckBox cb = nodesCheckBox.computeIfAbsent(gn, k -> new JCheckBox());
				checkBoxes.get(i).add(cb);
				int x = -20+i%2*40 + 100 + (j+1)*(graph.getWidth()-200)/(gnList.size()+1);
				int y = -7*gnList.size()+15*j + (voorkennisNodes.size() - (i))*(graph.getHeight()-50)/(voorkennisNodes.size());
				cb.setBounds(x-12, y+12,24,17);
				cb.setSelected(true);
				cb.addActionListener(this);
				graph.add(cb);
				checkBoxNodes.put(cb, gn);
			}
		}
		setDeselections(voorkennisTreeNode.getDeselections());
	}
	
	public void end() {
		if(voorkennisTreeNode!=null)
			voorkennisTreeNode.updateDeselections(getDeselections());
		System.out.println(getDeselections());
		for(int i=0 ; i<checkBoxes.size() ; i++) {
			ArrayList<JCheckBox> cbList = checkBoxes.get(i);
			for(int j=0 ; j<cbList.size() ; j++) {
				graph.remove(cbList.get(j));
			}
		}
		checkBoxes.clear();
		checkBoxNodes.clear();
		nodesCheckBox.clear();
		voorkennisTreeNode = null;
	}
	
	public ArrayList<String> getDeselections() {
		ArrayList<String> deselections = new ArrayList<String>();
	    for (Map.Entry<GraphNode, JCheckBox> entry: nodesCheckBox.entrySet()) {
	      if (!entry.getValue().isSelected())
	        deselections.add(entry.getKey().getID());
	    }
		return deselections;
	}
	public List<String> getSelections() {
	  return nodesCheckBox
	      .entrySet()
	      .stream()
	      .filter(e -> e.getValue().isSelected())
	      .map(e -> e.getKey().getID())
	      .collect(Collectors.toList());
	}
	
	
	public void setDeselections(Collection<String> deselections) {
		if(deselections==null)
			return;
		for (Map.Entry<GraphNode, JCheckBox> entry: nodesCheckBox.entrySet()) {
		  if (deselections.contains(entry.getKey().getID()))
		      entry.getValue().setSelected(false);
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
	    GraphNode node = checkBoxNodes.get(e.getSource());
	    if (node != null) node.setBlur(!((AbstractButton) e.getSource()).isSelected());
		graph.repaint();
		
	}
	
	
	
}
