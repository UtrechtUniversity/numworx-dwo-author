package fi.wiskopdr.templatecomponents;

import java.awt.AWTEventMulticaster;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import fi.wiskopdr.InteractiePanelContainerIF;
import fi.wiskopdr.TekstVakPanel;
import fi.wiskopdr.tekstobjects.TekstInteractiePanelVak;
import fi.wiskopdr.tekstobjects.TekstVak;

public class ListGenerator implements TComponentGenerator, ActionListener {

	private TekstInteractiePanelVak tipvEdit;
	private ListEditor listEditor;
	
	private JPopupMenu editChoice;
	private JMenuItem[] editChoiceItems;
	String[] editChoiceStrings = {"Edit", "Add Row", "Remove Row", "Decompose"};
	
	public static int initialRowCount = 3;
	public static int initialListNumberType = 0;
	public static int initialTabWidth = 30;
	public static int initialRowSpace = 10;
	
	private Hashtable<String,Object> initPreferences;
	private boolean decompose = false;
	
	public static String[][] listNumbers = 
		{
				{"a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z"},
				{"a.","b.","c.","d.","e.","f.","g.","h.","i.","j.","k.","l.","m.","n.","o.","p.","q.","r.","s.","t.","u.","v.","w.","x.","y.","z."},
				{"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26"},
				{"1.","2.","3.","4.","5.","6.","7.","8.","9.","10.","11.","12.","13.","14.","15.","16.","17.","18.","19.","20.","21.","22.","23.","24.","25.","26."},
				{"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"},
				{"•","•","•","•","•","•","•","•","•","•","•","•","•","•","•","•","•","•","•","•","•","•","•","•","•","•"}
		};
	private int columnCount = 2;
	
	
	
	public ListGenerator() {
		int rowCount = initialRowCount;
		int listNumberType = initialListNumberType;
		int tabWidth = initialTabWidth;
		int rowSpace = initialRowSpace;
		
		initPreferences = new Hashtable<String,Object>();
		initPreferences.put("rowCount", new Integer(rowCount));
		initPreferences.put("listNumberType", new Integer(listNumberType));
		initPreferences.put("tabWidth", new Integer(tabWidth));
		initPreferences.put("rowSpace", new Integer(rowSpace));
		
		editChoice = new JPopupMenu();
		editChoiceItems = new JMenuItem[editChoiceStrings.length];
		for(int i=0 ; i<editChoiceStrings.length ; i++) {
			editChoiceItems[i] = new JMenuItem(editChoiceStrings[i]);
			editChoiceItems[i].addActionListener(this);
			editChoice.add(editChoiceItems[i]);
		}
	}
	
	public void generateComponent(TekstVak tekstVak) {
		
		TekstInteractiePanelVak tComponent = new TekstInteractiePanelVak(tekstVak, makeTComponentLD(initPreferences));
		
		for(int i=0 ; i<initialRowCount ; i++) {
			TekstVak tv = ((TekstVakPanel)tComponent.getInteractiePanel()).geefTekstVak(i, 0);
			TekstInteractiePanelVak listNr = new TekstInteractiePanelVak(tv, makeListNrLD(initPreferences, i));
			tv.insert(listNr.toCompleteString());
		}
		tekstVak.insert(tComponent.toCompleteString());
	}
		
	public void editComponent(TekstInteractiePanelVak oldComponent, Hashtable preferences) {
		TekstVak tekstVak = oldComponent.getTekstVak();
		
		TekstInteractiePanelVak tComponent = new TekstInteractiePanelVak(tekstVak, makeTComponentLD(preferences));
		
		int rowCount = ((Integer)preferences.get("rowCount")).intValue();
		for(int i=0 ; i<rowCount ; i++) {
			TekstVak tvNumber = ((TekstVakPanel)tComponent.getInteractiePanel()).geefTekstVak(i, 0);
			TekstInteractiePanelVak listNr = new TekstInteractiePanelVak(tvNumber, makeListNrLD(preferences,i));
			tvNumber.insert(listNr.toCompleteString());
			
			TekstVak tvOld = ((TekstVakPanel)oldComponent.getInteractiePanel()).geefTekstVak(i, 1);
			String s = "";
			if(tvOld!=null) {
				s = tvOld.toCompleteString();
				if(s.charAt(s.length()-1)=='\n')
					s = s.substring(0,s.length()-1);
			}
			TekstVak tv = ((TekstVakPanel)tComponent.getInteractiePanel()).geefTekstVak(i, 1);
			tv.insert(s);
		}
		oldComponent.setSelected(true);
		tekstVak.deleteSelection();
		tekstVak.insert(tComponent.toCompleteString());
	}
	
	public void decompose(TekstInteractiePanelVak oldComponent) {
		decompose = true;
		editComponent(oldComponent, (Hashtable<String,Object>)oldComponent.getEditState().get("TComponentPreferences"));
		decompose = false;
	}
	
	private Hashtable<String,Object> makeTComponentLD(Hashtable<String,Object> preferences) {
		int rowCount = ((Integer)preferences.get("rowCount")).intValue();
		int tabWidth = ((Integer)preferences.get("tabWidth")).intValue();
		int rowSpace = ((Integer)preferences.get("rowSpace")).intValue();
		
		double[] breedtes = new double[columnCount];
		for(int i=0 ; i<columnCount ; i++) {
			breedtes[i] = tabWidth;
		}
		
		double[] hoogtes = new double[rowCount];
		for(int i=0 ; i<rowCount ; i++) {
			hoogtes[1] = 1; // maakt niet uit
		}
		
		String[][] teksten = new String[rowCount][columnCount];
		for(int i=0 ; i<rowCount ; i++) {
			for(int j=0 ; j<columnCount ; j++) {
				teksten[i][j] = "";
			}
		}
		
		Hashtable<String,Object> ipLaunchState = new Hashtable<String,Object>();
		ipLaunchState.put("breedtes", breedtes);
		ipLaunchState.put("hoogtes", hoogtes);
		ipLaunchState.put("teksten", teksten);
		ipLaunchState.put("pasAanH", new Boolean(true));
		ipLaunchState.put("cellSpaceRow", new Integer(rowSpace));
		ipLaunchState.put("styleString", "list");
		if(!decompose) ipLaunchState.put("templateModeEdit", new Boolean(true));
				
		Hashtable<String,Object> launchData = new Hashtable<String,Object>();
		launchData.put("soortInteractiePanel", new Integer(9));
		launchData.put("setNr", new Integer(3));
		launchData.put("interactiePanelLaunchState", ipLaunchState);
		launchData.put("volledigeBreedte", new Boolean(true));
		if(!decompose) launchData.put("TComponent", "List");
		if(!decompose) launchData.put("TComponentPreferences", preferences);
		
		return launchData;
	}
	
	private Hashtable<String,Object> makeListNrLD(Hashtable<String,Object> preferences, int i) {
		int listNumberType = ((Integer)preferences.get("listNumberType")).intValue();
		
		Hashtable<String,Object> ipLaunchState = new Hashtable<String,Object>();
		ipLaunchState.put("tekst", listNumbers[listNumberType][i]);
		ipLaunchState.put("pasAanH", new Boolean(true));
		ipLaunchState.put("styleString", "sub-task-number");
		if(!decompose) ipLaunchState.put("templateModeEdit", new Boolean(true));
		if(!decompose) ipLaunchState.put("templateModeFill", new Boolean(true));
		ipLaunchState.put("anderFont", new Boolean(true));
		ipLaunchState.put("font", new Font("SansSerif", Font.BOLD, 14));
		
		Hashtable<String,Object> launchData = new Hashtable<String,Object>();
		launchData.put("soortInteractiePanel", new Integer(9));
		launchData.put("setNr", new Integer(3));
		launchData.put("interactiePanelLaunchState", ipLaunchState);
		launchData.put("volledigeBreedte", new Boolean(true));
		
		return launchData;
	}
	
	public void editMenu(TekstInteractiePanelVak tipv) {
		editChoice.show(tipv,tipv.getWidth(),tipv.getHeight());
		tipvEdit = tipv;
	}
	
	public void closeEditMenu() {
		//editChoice.setVisible(false);
	}
	
	public void edit(TekstInteractiePanelVak tipv) {
		tipvEdit = tipv;
		if(listEditor==null) {
			listEditor = new ListEditor(tipv.getTekstVak());
			listEditor.addActionListener(this);
		}
		else {
			listEditor.setTekstVak(tipv.getTekstVak());
		}
		Hashtable<String,Object> preferences = (Hashtable<String,Object>)tipv.getEditState().get("TComponentPreferences");
		listEditor.setPreferences(preferences);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(listEditor) && e.getActionCommand().equals("ok")) {
			editComponent(tipvEdit, listEditor.getPreferences());
		}
		
		if(e.getSource().equals(editChoiceItems[0])) {
			edit(tipvEdit);
		}
		
		if(e.getSource().equals(editChoiceItems[1])) {
			Hashtable<String,Object> preferences = (Hashtable<String,Object>)tipvEdit.getEditState().get("TComponentPreferences");
			int rowCount = (Integer)preferences.get("rowCount");
			rowCount++;
			preferences.put("rowCount", new Integer(rowCount));
			editComponent(tipvEdit, preferences);
		}
		if(e.getSource().equals(editChoiceItems[2])) {
			Hashtable<String,Object> preferences = (Hashtable<String,Object>)tipvEdit.getEditState().get("TComponentPreferences");
			int rowCount = (Integer)preferences.get("rowCount");
			if(rowCount>1)rowCount--;
			preferences.put("rowCount", new Integer(rowCount));
			editComponent(tipvEdit, preferences);
		}
		if(e.getSource().equals(editChoiceItems[3])) {
			decompose(tipvEdit);
		}
		
	}
	
}
