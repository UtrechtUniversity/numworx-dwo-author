package fi.wiskopdr.domainmodel.graph;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Hashtable;

import fi.beans.numworxlf.JCheckBox;

public class VoorkennisDeselector implements ActionListener{

	private ArrayList<ArrayList<JCheckBox>> checkBoxes;
	private Graph graph;
	private GraphNode voorkennisTreeNode;
	private Hashtable<JCheckBox, GraphNode> checkBoxNodes;
	
	public VoorkennisDeselector(Graph graph) {
		this.graph = graph;
		checkBoxes = new ArrayList<ArrayList<JCheckBox>>();
		checkBoxNodes = new Hashtable<JCheckBox, GraphNode>();
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
				checkBoxes.get(i).add(new JCheckBox());
				int x = -20+i%2*40 + 100 + (j+1)*(graph.getWidth()-200)/(gnList.size()+1);
				int y = -7*gnList.size()+15*j + (voorkennisNodes.size() - (i))*(graph.getHeight()-50)/(voorkennisNodes.size());
				checkBoxes.get(i).get(j).setBounds(x-12, y+12,24,17);
				checkBoxes.get(i).get(j).setSelected(true);
				checkBoxes.get(i).get(j).addActionListener(this);
				graph.add(checkBoxes.get(i).get(j));
				checkBoxNodes.put(checkBoxes.get(i).get(j), gn);
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
		voorkennisTreeNode = null;
	}
	
	public ArrayList<String> getDeselections() {
		ArrayList<String> deselections = new ArrayList<String>();
		for(int i=0 ; i<checkBoxes.size() ; i++) {
			ArrayList<JCheckBox> cbList = checkBoxes.get(i);
			for(int j=0 ; j<cbList.size() ; j++) {
				JCheckBox checkBox = cbList.get(j);
				if(!checkBox.isSelected())
					deselections.add(checkBoxNodes.get(checkBox).getID());
			}
		}
		return deselections;
	}
	
	public void setDeselections(Iterable<String> deselections) {
		if(deselections==null)
			return;
		for(String ID : deselections) {
			for(int i=0 ; i<checkBoxes.size() ; i++) {
				ArrayList<JCheckBox> cbList = checkBoxes.get(i);
				for(int j=0 ; j<cbList.size() ; j++) {
					JCheckBox checkBox = cbList.get(j);
					if(ID.equals(checkBoxNodes.get(checkBox).getID())) {
						checkBox.setSelected(false);
					}
						
				}
			}
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		for(int i=0 ; i<checkBoxes.size() ; i++) {
			ArrayList<JCheckBox> cbList = checkBoxes.get(i);
			for(int j=0 ; j<cbList.size() ; j++) {
				JCheckBox checkBox = cbList.get(j);
				if(e.getSource()==checkBox) {
					checkBoxNodes.get(checkBox).setBlur(!checkBox.isSelected());
				}
			}
		}
		graph.repaint();
		
	}
	
	
	
}
