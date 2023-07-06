package fi.graphtool;

import java.util.ArrayList;

public class VectorVeldType {
	
	private ArrayList<String> veldGrafiekTypeStrings;

	int aValueIndex;
	
	public VectorVeldType() {
		aValueIndex = 0; // Default waarde (eerste string in lijst) 
		veldGrafiekTypeStrings = new ArrayList<String>();
		veldGrafiekTypeStrings.add("QUIVER");
		veldGrafiekTypeStrings.add("STREAMLINE");
	}
	
	public ArrayList<String> getPossibleStrings() {
		return veldGrafiekTypeStrings;
	}
	
	public void setValueIndex(int index) {
		aValueIndex = index;
	}
	
	public int getValueIndex() {
		return aValueIndex;
	}

}
