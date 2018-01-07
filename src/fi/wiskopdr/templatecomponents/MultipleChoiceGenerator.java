package fi.wiskopdr.templatecomponents;

import java.awt.AWTEventMulticaster;
import java.awt.Color;
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

public class MultipleChoiceGenerator implements TComponentGenerator, ActionListener {

	private TekstInteractiePanelVak tipvEdit;
	private MultipleChoiceEditor mcEditor;
	
	private JPopupMenu editChoice;
	private JMenuItem[] editChoiceItems;
	String[] editChoiceStrings = {"Edit", "Add Item", "Remove Item", "Decompose"};
	
	public static int initialItemCount = 4;
	public static int initialListNumberType = 4;
	public static int initialTabWidth = 20;
	public static int initialRowSpace = 2;
	public static boolean initialHasPrefix = true;
	
	private Hashtable<String,Object> initPreferences;
	private boolean decompose = false;
	
	public static String[][] listNumbers = 
	{
			{"a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z"},
			{"a.","b.","c.","d.","e.","f.","g.","h.","i.","j.","k.","l.","m.","n.","o.","p.","q.","r.","s.","t.","u.","v.","w.","x.","y.","z."},
			{"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26"},
			{"1.","2.","3.","4.","5.","6.","7.","8.","9.","10.","11.","12.","13.","14.","15.","16.","17.","18.","19.","20.","21.","22.","23.","24.","25.","26."},
			{"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"}
	};
	
	private int columnCount = 2;
	private int oldColumnCount = 2;
	
	
	public MultipleChoiceGenerator() {
		int itemCount = initialItemCount;
		int listNumberType = initialListNumberType;
		int tabWidth = initialTabWidth;
		int rowSpace = initialRowSpace;
		boolean hasPrefix = initialHasPrefix;
		
		initPreferences = new Hashtable<String,Object>();
		initPreferences.put("itemCount", new Integer(itemCount));
		initPreferences.put("listNumberType", new Integer(listNumberType));
		initPreferences.put("tabWidth", new Integer(tabWidth));
		initPreferences.put("rowSpace", new Integer(rowSpace));
		initPreferences.put("hasPrefix", new Boolean(hasPrefix));
		
		columnCount = hasPrefix ? 2 : 1	;
		
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
		
		TekstVak tComponentTV = ((TekstVakPanel)tComponent.getInteractiePanel()).geefTekstVak(0, 0);		
		TekstInteractiePanelVak list = new TekstInteractiePanelVak(tComponentTV, makeListLD(initPreferences));
		
		for(int i=0 ; i<initialItemCount ; i++) {
			TekstVak listTV1 = ((TekstVakPanel)list.getInteractiePanel()).geefTekstVak(i, 0);
			TekstInteractiePanelVak listNr = new TekstInteractiePanelVak(listTV1, makeListNrLD(initPreferences,i));
			listTV1.insert(listNr.toCompleteString());
			
			TekstVak listTV2 = ((TekstVakPanel)list.getInteractiePanel()).geefTekstVak(i, 1);
			TekstInteractiePanelVak listItem = new TekstInteractiePanelVak(listTV2, makeListItemLD(i));
			listTV2.insert(listItem.toCompleteString());
		}
		TekstInteractiePanelVak checkUnit = new TekstInteractiePanelVak(tComponentTV, makeCheckUnitLD(initPreferences));
		tComponentTV.insert(list.toCompleteString());
		tComponentTV.insert("\n\n"+checkUnit.toCompleteString());
		tekstVak.insert(tComponent.toCompleteString());
	}
	
	public void editComponent(TekstInteractiePanelVak oldComponent, Hashtable<String,Object> preferences) {
		boolean hasPrefix = ((Boolean)preferences.get("hasPrefix")).booleanValue();
		oldColumnCount = columnCount;
		columnCount = hasPrefix ? 2 : 1	;
		
		TekstVak tekstVak = oldComponent.getTekstVak();
		
		TekstInteractiePanelVak tComponent = new TekstInteractiePanelVak(tekstVak, makeTComponentLD(preferences));
		
		TekstVak tComponentTV = ((TekstVakPanel)tComponent.getInteractiePanel()).geefTekstVak(0, 0);		
		TekstInteractiePanelVak list = new TekstInteractiePanelVak(tComponentTV, makeListLD(preferences));
		
		int itemCount = ((Integer)preferences.get("itemCount")).intValue();
		for(int i=0 ; i<itemCount ; i++) {
			if(columnCount > 1)	{			
				TekstVak listTV1 = ((TekstVakPanel)list.getInteractiePanel()).geefTekstVak(i, 0);
				TekstInteractiePanelVak listNr = new TekstInteractiePanelVak(listTV1, makeListNrLD(preferences,i));
				listTV1.insert(listNr.toCompleteString());
			}
			TekstVak listTV2 = ((TekstVakPanel)list.getInteractiePanel()).geefTekstVak(i, columnCount-1);
			TekstInteractiePanelVak listItem = new TekstInteractiePanelVak(listTV2, makeListItemLD(i));
			
			
			TekstVakPanel oldComponentTVP = (TekstVakPanel)oldComponent.getInteractiePanel();
			TekstInteractiePanelVak oldList = (TekstInteractiePanelVak)oldComponentTVP.geefInteractiePanels().elementAt(0);
			TekstVak oldItemTV = ((TekstVakPanel)oldList.getInteractiePanel()).geefTekstVak(i, oldColumnCount-1);
			if(oldItemTV!=null) {
				TekstInteractiePanelVak oldItemTVP = (TekstInteractiePanelVak)oldItemTV.geefInteractiePanels().elementAt(0);
				TekstVak tvOld = ((TekstVakPanel)oldItemTVP.getInteractiePanel()).geefTekstVak(0,0);
				String s = "";
				s = tvOld.toCompleteString();
				if(s.charAt(s.length()-1)=='\n')
					s = s.substring(0,s.length()-1);
				TekstVak tv = ((TekstVakPanel)listItem.getInteractiePanel()).geefTekstVak(0,0);
				tv.insert(s);
			}
			listTV2.insert(listItem.toCompleteString());
		}
		TekstInteractiePanelVak checkUnit = new TekstInteractiePanelVak(tComponentTV, makeCheckUnitLD(preferences));
		tComponentTV.insert(list.toCompleteString());
		tComponentTV.insert("\n\n"+checkUnit.toCompleteString());
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
		Hashtable<String,Object> ipLaunchState = new Hashtable<String,Object>();
		ipLaunchState.put("tekst", "");
		ipLaunchState.put("pasAanH", new Boolean(true));
		if(!decompose) ipLaunchState.put("templateModeEdit", new Boolean(true));
		
		Hashtable<String,Object> launchData = new Hashtable<String,Object>();
		launchData.put("soortInteractiePanel", new Integer(9));
		launchData.put("setNr", new Integer(3));
		launchData.put("interactiePanelLaunchState", ipLaunchState);
		launchData.put("volledigeBreedte", new Boolean(true));
		if(!decompose) launchData.put("TComponent", "MultipleChoice");
		if(!decompose) launchData.put("TComponentPreferences", preferences);
		
		return launchData;
	}
	
	private Hashtable<String,Object> makeListLD(Hashtable<String,Object> preferences) {
		int itemCount = ((Integer)preferences.get("itemCount")).intValue();
		int tabWidth = ((Integer)preferences.get("tabWidth")).intValue();
		int rowSpace = ((Integer)preferences.get("rowSpace")).intValue();
		
		
		double[] breedtes = new double[columnCount];
		for(int i=0 ; i<columnCount ; i++) {
			breedtes[i] = tabWidth;
		} 
		
		double[] hoogtes = new double[itemCount];
		for(int i=0 ; i<itemCount ; i++) {
			hoogtes[0] = 1; // maakt niet uit
		}
		
		String[][] teksten = new String[itemCount][columnCount];
		for(int i=0 ; i<itemCount ; i++) {
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
		if(!decompose) ipLaunchState.put("templateModeEdit", new Boolean(true));
				
		Hashtable<String,Object> launchData = new Hashtable<String,Object>();
		launchData.put("soortInteractiePanel", new Integer(9));
		launchData.put("setNr", new Integer(3));
		launchData.put("interactiePanelLaunchState", ipLaunchState);
		launchData.put("volledigeBreedte", new Boolean(true));
		
		return launchData;
	}
	
	private Hashtable<String,Object> makeListNrLD(Hashtable<String,Object> preferences, int i) {
		int listNumberType = ((Integer)preferences.get("listNumberType")).intValue();
		
		Hashtable<String,Object> ipLaunchState = new Hashtable<String,Object>();
		ipLaunchState.put("tekst", listNumbers[listNumberType][i]);
		ipLaunchState.put("bovenMarge", new Integer(5));
		ipLaunchState.put("pasAanH", new Boolean(true));
		ipLaunchState.put("styleString", "mc-prefix");
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
	
	private Hashtable<String,Object> makeListItemLD(int i) {
		
		Hashtable<String,Object> ipLaunchState = new Hashtable<String,Object>();
		ipLaunchState.put("tekst", "");
		ipLaunchState.put("cellMarge", new Integer(10));
		ipLaunchState.put("bovenMarge", new Integer(5));
		ipLaunchState.put("pasAanH", new Boolean(true));
		ipLaunchState.put("styleString", "mc-item");
		if(!decompose) ipLaunchState.put("templateModeEdit", new Boolean(true));
		ipLaunchState.put("selectable", new Boolean(true));
		ipLaunchState.put("colorSelection", new Boolean(true));
		ipLaunchState.put("selectieColor", new Color(230,230,230));
		ipLaunchState.put("ipId", new Integer(i+1));
		
		Hashtable<String,Object> launchData = new Hashtable<String,Object>();
		launchData.put("soortInteractiePanel", new Integer(9));
		launchData.put("setNr", new Integer(3));
		launchData.put("interactiePanelLaunchState", ipLaunchState);
		launchData.put("volledigeBreedte", new Boolean(true));
		return launchData;
	}
	
	private Hashtable<String,Object> makeCheckUnitLD(Hashtable<String,Object> preferences) {
		int itemCount = ((Integer)preferences.get("itemCount")).intValue();
		
		boolean[] juisteSelecties = new boolean[itemCount];
		
		Hashtable<String,Object> ipLaunchState = new Hashtable<String,Object>();
		ipLaunchState.put("juisteSelecties", juisteSelecties);
		ipLaunchState.put("knopImageString", "controleerknop");
				
		Hashtable<String,Object> launchData = new Hashtable<String,Object>();
		launchData.put("soortInteractiePanel", new Integer(12));
		launchData.put("interactiePanelLaunchState", ipLaunchState);
		launchData.put("breedte", new Integer(100));
		launchData.put("hoogte", new Integer(30));
		launchData.put("volledigeBreedte", new Boolean(true));
		return launchData;
	}
	
	public void edit(TekstInteractiePanelVak tipv) {
		tipvEdit = tipv;
		if(mcEditor==null) {
			mcEditor = new MultipleChoiceEditor(tipv.getTekstVak());
			mcEditor.addActionListener(this);
		}
		else {
			mcEditor.setTekstVak(tipv.getTekstVak());
		}
		Hashtable<String,Object> preferences = (Hashtable<String,Object>)tipv.getEditState().get("TComponentPreferences");
		mcEditor.setPreferences(preferences);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(mcEditor) && e.getActionCommand().equals("ok")) {
			editComponent(tipvEdit, mcEditor.getPreferences());
		}
		if(e.getSource().equals(editChoiceItems[0])) {
			edit(tipvEdit);
		}
		
		if(e.getSource().equals(editChoiceItems[1])) {
			Hashtable<String,Object> preferences = (Hashtable<String,Object>)tipvEdit.getEditState().get("TComponentPreferences");
			int itemCount = (Integer)preferences.get("itemCount");
			itemCount++;
			preferences.put("itemCount", new Integer(itemCount));
			editComponent(tipvEdit, preferences);
		}
		if(e.getSource().equals(editChoiceItems[2])) {
			Hashtable<String,Object> preferences = (Hashtable<String,Object>)tipvEdit.getEditState().get("TComponentPreferences");
			int itemCount = (Integer)preferences.get("itemCount");
			if(itemCount>1)itemCount--;
			preferences.put("itemCount", new Integer(itemCount));
			editComponent(tipvEdit, preferences);
		}
		if(e.getSource().equals(editChoiceItems[3])) {
			decompose(tipvEdit);
		}
		
	}

	public void editMenu(TekstInteractiePanelVak tipv) {
		editChoice.show(tipv,tipv.getWidth(),tipv.getHeight());
		tipvEdit = tipv;
	}
	
	public void closeEditMenu() {
		//editChoice.setVisible(false);
	}
	
	
	
	
}
