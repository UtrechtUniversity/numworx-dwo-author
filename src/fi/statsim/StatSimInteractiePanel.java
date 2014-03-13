package fi.statsim;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;
import javax.swing.JPanel;
import fi.beans.wiskopdrbeans.InteractieEditPanel;
import fi.beans.wiskopdrbeans.InteractiePanel;

public class StatSimInteractiePanel extends JPanel implements InteractiePanel {
	Munten munten;
	Dobbelstenen dobbelstenen;
	
	public StatSimInteractiePanel () {
		setLayout(null);
		munten = new Munten();
		munten.setLocation(0,0);
		munten.setSize(790,450);
		add(munten);
		
		munten.setVisible(false);
		dobbelstenen = new Dobbelstenen();
		dobbelstenen.setLocation(0,0);
		dobbelstenen.setSize(790,450);
		add(dobbelstenen);
	}
	public void setState(Hashtable h) {

		String[] column11 = new String[100];
		String[] column12 = new String[100];
		String[] column13 = new String[100];
		if(h.containsKey("column11")) column11 =  (String[])h.get("column11");
		if(h.containsKey("column12")) column12 =  (String[])h.get("column12");
		if(h.containsKey("column13")) column13 =  (String[])h.get("column13");
		for (int i=0;i<100;i++) {
			if (column11[i]=="null") munten.table.setValueAt("",i, 0); else munten.table.setValueAt(column11[i],i, 0);
			if (column12[i]=="null") munten.table.setValueAt("",i, 1); else munten.table.setValueAt(column12[i],i, 1);
			if (column13[i]=="null") munten.table.setValueAt("",i, 2); else munten.table.setValueAt(column13[i],i, 2);
		}
		
		String[] column21 = new String[100];
		String[] column22 = new String[100];
		String[] column23 = new String[100];
		String[] column24 = new String[100];
		if(h.containsKey("column21")) column21 =  (String[])h.get("column21");
		if(h.containsKey("column22")) column22 =  (String[])h.get("column22");
		if(h.containsKey("column23")) column23 =  (String[])h.get("column23");
		if(h.containsKey("column24")) column24 =  (String[])h.get("column24");
		for (int i=0;i<100;i++) {
			if (column21[i]=="null") munten.table1.setValueAt("",i, 0); else munten.table1.setValueAt(column21[i],i, 0);
			if (column22[i]=="null") munten.table1.setValueAt("",i, 1); else munten.table1.setValueAt(column22[i],i, 1);
			if (column23[i]=="null") munten.table1.setValueAt("",i, 2); else munten.table1.setValueAt(column23[i],i, 2);
			if (column24[i]=="null") munten.table1.setValueAt("",i, 3); else munten.table1.setValueAt(column24[i],i, 3);
		}
		if(h.containsKey("experiment")) munten.experiment= ((Integer)h.get("experiment")).intValue();
		Boolean eenMunt=false;
		if(h.containsKey("eenMuntRadio")) eenMunt= ((Boolean)h.get("eenMuntRadio")).booleanValue();
		if (eenMunt==true) {
			munten.eenMuntRadio.setSelected(true);
			munten.tweeMuntenRadio.setSelected(false);
		} else {
			munten.eenMuntRadio.setSelected(false);
			munten.tweeMuntenRadio.setSelected(true);
		}
		munten.setEenMuntTweeMunten();
		Boolean startSelected=false;
		if(h.containsKey("startSelected")) startSelected= ((Boolean)h.get("startSelected")).booleanValue();
		if (startSelected==true) {
			munten.start.setEnabled(true);
			munten.volgende.setEnabled(false);
			munten.stop.setEnabled(false);
		} else {
			munten.start.setEnabled(false);
			munten.volgende.setEnabled(true);
			munten.stop.setEnabled(true);
		}
		munten.setStartStop();
		if(h.containsKey("gemiddeldeKop")) munten.gemiddeldeKop= ((Double)h.get("gemiddeldeKop")).doubleValue();
		if(h.containsKey("minimumKop")) munten.minimumKop= ((Integer)h.get("minimumKop")).intValue();
		if(h.containsKey("maximumKop")) munten.maximumKop= ((Integer)h.get("maximumKop")).intValue();
		if(h.containsKey("gemiddeldeGeenKop")) munten.gemiddeldeGeenKop= ((Double)h.get("gemiddeldeGeenKop")).doubleValue();
		if(h.containsKey("minimumGeenKop")) munten.minimumGeenKop= ((Integer)h.get("minimumGeenKop")).intValue();
		if(h.containsKey("maximumGeenKop")) munten.maximumGeenKop= ((Integer)h.get("maximumGeenKop")).intValue();
		if(h.containsKey("gemiddeldeEenKop")) munten.gemiddeldeEenKop= ((Double)h.get("gemiddeldeEenKop")).doubleValue();
		if(h.containsKey("minimumEenKop")) munten.minimumEenKop= ((Integer)h.get("minimumEenKop")).intValue();
		if(h.containsKey("maximumEenKop")) munten.maximumEenKop= ((Integer)h.get("maximumEenKop")).intValue();
		if(h.containsKey("gemiddeldeTweeKop")) munten.gemiddeldeTweeKop= ((Double)h.get("gemiddeldeTweeKop")).doubleValue();
		if(h.containsKey("minimumTweeKop")) munten.minimumTweeKop= ((Integer)h.get("minimumTweeKop")).intValue();
		if(h.containsKey("maximumTweeKop")) munten.maximumTweeKop= ((Integer)h.get("maximumTweeKop")).intValue();
		munten.setResults();
	}
	
	public Hashtable getState() {	
		Hashtable h = new Hashtable();
		
		String[] column11 = new String[100];
		String[] column12 = new String[100];
		String[] column13 = new String[100];
		for (int i=0;i<100;i++) {
			column11[i]=String.valueOf(munten.table.getValueAt(i, 0));
			column12[i]=String.valueOf(munten.table.getValueAt(i, 1));
			column13[i]=String.valueOf(munten.table.getValueAt(i, 2));
		}
		h.put("column11", column11);
		h.put("column12", column12);
		h.put("column13", column13);
		String[] column21 = new String[100];
		String[] column22 = new String[100];
		String[] column23 = new String[100];
		String[] column24 = new String[100];
		for (int i=0;i<100;i++) {
			column21[i]=String.valueOf(munten.table1.getValueAt(i, 0));
			column22[i]=String.valueOf(munten.table1.getValueAt(i, 1));
			column23[i]=String.valueOf(munten.table1.getValueAt(i, 2));
			column24[i]=String.valueOf(munten.table1.getValueAt(i, 3));
		}
		h.put("column21", column21);
		h.put("column22", column22);
		h.put("column23", column23);
		h.put("column24", column24);
		h.put("experiment", new Integer(munten.experiment));
		h.put("eenMuntRadio", new Boolean(munten.eenMuntRadio.isSelected()));
		h.put("startSelected", new Boolean(munten.start.isEnabled()));
		h.put("gemiddeldeKop", new Double(munten.gemiddeldeKop));
		h.put("minimumKop", new Integer(munten.minimumKop));
		h.put("maximumKop", new Integer(munten.maximumKop));
		h.put("gemiddeldeGeenKop", new Double(munten.gemiddeldeGeenKop));
		h.put("minimumGeenKop", new Integer(munten.minimumGeenKop));
		h.put("maximumGeenKop", new Integer(munten.maximumGeenKop));
		h.put("gemiddeldeEenKop", new Double(munten.gemiddeldeEenKop));
		h.put("minimumEenKop", new Integer(munten.minimumEenKop));
		h.put("maximumEenKop", new Integer(munten.maximumEenKop));
		h.put("gemiddeldeTweeKop", new Double(munten.gemiddeldeTweeKop));
		h.put("minimumTweeKop", new Integer(munten.minimumTweeKop));
		h.put("maximumTweeKop", new Integer(munten.maximumTweeKop));
	    return h;
	}
	
	
	public void zetOpdracht(Hashtable h, String[] randomVars, Hashtable randomValues) {
		Boolean muntenRadioBool=false;
		if(h.containsKey("muntenRadio")) muntenRadioBool= ((Boolean)h.get("muntenRadio")).booleanValue();
		munten.setVisible(muntenRadioBool);
		Boolean dobbelstenenRadioBool=false;
		if(h.containsKey("dobbelstenenRadio")) dobbelstenenRadioBool= ((Boolean)h.get("dobbelstenenRadio")).booleanValue();
		dobbelstenen.setVisible(dobbelstenenRadioBool);
	}

	public void setEditState(Hashtable h) {
	}

	public Hashtable getEditState() {
		return getState();
	}

	public InteractieEditPanel getEditPanel() {
		return new StatSimInteractieEditPanel();
	}

	public void wis() {
		
	}

	public void zetMaat() {
		
	}

	public int geefAsHoogte() {
		return 0;
	}

	public int getIpId() {
		return 0;
	}

	public int getScore() {
		return 0;
	}

	public int[][] getScoreObjectives() {
		return null;
	}

	public int getScoreMax() {
		return 0;
	}

	public boolean isCorrect() {
		return false;
	}

	public boolean isFout() {
		return false;
	}

	public void zetMode(int mode) {
		
	}

	public void zetNagekeken(boolean b) {
		
	}

	public void stop() {
		
	}

	public void start() {
		
	}

	public void destroy() {
		
	}

	public void opnieuw() {
		
	}

	public void kijkNa() {
		
	}

	public void kijkNa(int stapNr) {
		
	}

	public void addActionListener(ActionListener al) {
		
	}

}
