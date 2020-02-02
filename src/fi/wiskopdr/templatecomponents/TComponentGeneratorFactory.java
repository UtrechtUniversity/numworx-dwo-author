package fi.wiskopdr.templatecomponents;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;

import fi.wiskopdr.WiskOpdr;
import fi.wiskopdr.tekstobjects.TekstVak;

public class TComponentGeneratorFactory {

	private static String[] ComponentTypeList = {"List", "MultipleChoice", "MultipleChoice_1", "DragDrop", "DragDrop_1"};
	private static String[] ComponentTypeNameList = {WiskOpdr.rb.getString("TCOMP_list"), 
	                                                 WiskOpdr.rb.getString("TCOMP_multip_0"),
	                                                 WiskOpdr.rb.getString("TCOMP_multip_1"),
	                                                 WiskOpdr.rb.getString("TCOMP_drag_0"),
	                                                 WiskOpdr.rb.getString("TCOMP_drag_1")};
	
	private static ListGenerator listGenerator;
	private static MultipleChoiceGenerator mcGenerator;
	private static MultipleChoiceGenerator_1 mcGenerator_1;
	private static DragDropGenerator ddGenerator;
	private static DragDropGenerator_1 ddGenerator_1;
	
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
		if(type.equals("MultipleChoice_1")) {
			if(mcGenerator_1==null)
				mcGenerator_1 = new MultipleChoiceGenerator_1();
			return mcGenerator_1;
		}
		if(type.equals("DragDrop")) {
			if(ddGenerator==null)
				ddGenerator = new DragDropGenerator();
			return ddGenerator;
		}
		if(type.equals("DragDrop_1")) {
			if(ddGenerator_1==null)
				ddGenerator_1 = new DragDropGenerator_1();
			return ddGenerator_1;
		}
		return null;
	}
	
	public static String[] getComponentTypeList() {
		return ComponentTypeList;
	}
	
	public static String[] getComponentTypeNameList() {
	   return ComponentTypeNameList;
    }
	
	public static String getComponentTypeName(String type) {
	  for(int i=0 ; i<ComponentTypeList.length ; i++) {
	    if(ComponentTypeList[i].equals(type))
	      return ComponentTypeNameList[i];
	  }
      return "";
   }
	
}
