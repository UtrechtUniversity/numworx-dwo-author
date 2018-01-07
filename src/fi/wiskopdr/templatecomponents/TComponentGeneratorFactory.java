package fi.wiskopdr.templatecomponents;

import fi.wiskopdr.tekstobjects.TekstVak;

public class TComponentGeneratorFactory {

	private static String[] ComponentTypeList = {"List", "MultipleChoice", "DragDrop"};
	
	private static ListGenerator listGenerator;
	private static MultipleChoiceGenerator mcGenerator;
	private static DragDropGenerator ddGenerator;
	
	public static TComponentGenerator getComponentGenerator(String type) {
		
		if(type.equals("List")) {
			if(listGenerator==null)
				listGenerator = new ListGenerator();
			return listGenerator;
		}
		if(type.equals("MultipleChoice")) {
			if(mcGenerator==null)
				mcGenerator = new MultipleChoiceGenerator();
			return mcGenerator;
		}
		if(type.equals("DragDrop")) {
			if(ddGenerator==null)
				ddGenerator = new DragDropGenerator();
			return ddGenerator;
		}
		return null;
	}
	
	public static String[] getComponentTypeList() {
		return ComponentTypeList;
	}
}
