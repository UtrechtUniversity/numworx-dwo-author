package fi.statsim;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import javax.swing.JPanel;

import org.cbook.cbookif.CBookEvent;
import org.cbook.cbookif.CBookEventHandler;
import org.cbook.cbookif.CBookEventListener;

import fi.beans.wiskopdrbeans.CBookAware;
import fi.beans.wiskopdrbeans.InteractieEditPanel;
import fi.beans.wiskopdrbeans.InteractiePanel;

public class StatSimInteractiePanel extends JPanel implements InteractiePanel, CBookAware {
	Munten munten;
	Dobbelstenen dobbelstenen;
	BinomTrekking binomTrekking;
	Steekproef steekproef;
	
	
	boolean muntenRadioBool=false;
	boolean dobbelstenenRadioBool=false;
	boolean binomTrekkingRadioBool=false;
	boolean steekproefRadioBool=false;
	        
    private CBookEventHandler cbookEventHandler = new CBookEventHandler(this);
	
	public StatSimInteractiePanel () {
		setLayout(null);
		munten = new Munten(this);
		munten.setLocation(0,0);
		munten.setSize(this.getWidth(),this.getHeight());
		
		add(munten);
		munten.setVisible(true);
		
		dobbelstenen = new Dobbelstenen(this);
		dobbelstenen.setLocation(0,0);
		//dobbelstenen.setSize(790,450);
		dobbelstenen.setVisible(false);
		add(dobbelstenen);
		
		binomTrekking = new BinomTrekking(this);
		binomTrekking.setLocation(0,0);
		//binomTrekking.setSize(790,450);
		add(binomTrekking);
		binomTrekking.setVisible(false);
		
		steekproef = new Steekproef(this);
		steekproef.setLocation(0,0);
		add(steekproef);
		steekproef.setVisible(false);
		
	}
	
	@Override
	public void acceptCBookEvent(CBookEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addCBookEventListener(CBookEventListener listener,
			String command) {
		// TODO Auto-generated method stub
		cbookEventHandler.addCBookEventListener(listener, command);

	}

	@Override
	public void removeCBookEventListener(CBookEventListener listener,
			String command) {
		// TODO Auto-generated method stub
		cbookEventHandler.removeCBookEventListener(listener, command);		
	}

	@Override
	public String[] getSendCmds() {
		// TODO Auto-generated method stub
		String[] commands=null;
		if (steekproefRadioBool==true) {
			commands = new String[2];
			commands[0] = "text.sample";
			commands[1] = "text.sampleCollection";
		}
		else if (binomTrekkingRadioBool==true) {
			commands = new String[1];
			commands[0] = "text.sample";
		} 
		else if (dobbelstenenRadioBool==true) {
			commands = new String[1];
			commands[0] = "text.sample";
		}
		else if (muntenRadioBool==true) {
			commands = new String[1];
			commands[0] = "text.sample";
		}
		return commands;

	}

	@Override
	public String[] getAcceptedCmds() {
		// TODO Auto-generated method stub
		String[] commands = {"text"};
		return commands;
	}

	@Override
	public String getLocalizedCmd(String cmd) {
		// TODO Auto-generated method stub
		String localizedCmd = StatSim.rb.getString(CBA_PREFIX + cmd);
		if(localizedCmd==null)
			return cmd;
		return localizedCmd;
	}

	
	public void fireCBookSteekproef(String arg1, String arg2) {
		
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("content", arg1);
		Map<String,Object> map1 = new HashMap<String,Object>();
		map1.put("content", arg2);
	
		cbookEventHandler.fire("text.sample",map);
		cbookEventHandler.fire("text.sampleCollection",map1);
	}
	
	public void fireCBookBinomTrekking(String arg1) {
		
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("content", arg1);
		
		cbookEventHandler.fire("text.sample",map);
	}
	
	public void fireCBookDobbelstenen(String arg1) {
		
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("content", arg1);
		
		cbookEventHandler.fire("text.sample",map);
	}
	
	public void setBounds(int a, int b, int c, int d) {
		super.setBounds(a,b,c,d);
		munten.setSize(this.getWidth(),this.getHeight());
		dobbelstenen.setSize(this.getWidth(),this.getHeight());
		binomTrekking.setSize(this.getWidth(),this.getHeight());
		steekproef.setSize(this.getWidth(),this.getHeight());
	}
	
	public void setState(Hashtable h) {
        // ***** Munten *******
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
			munten.wis.setEnabled(true);
		} else {
			munten.wis.setEnabled(false);
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
		int maxCount=0;
		if(h.containsKey("maxCount")) maxCount= ((Integer)h.get("maxCount")).intValue();
		munten.maxCount=maxCount;
		munten.aantalWorpenText.setText(Integer.toString(maxCount));
		String kansOpKopText="";
		if(h.containsKey("kansOpKopText")) kansOpKopText =  (String)h.get("kansOpKopText");
		munten.kansOpKopText.setText(kansOpKopText);
		
		// ******* Dobbelstenen *********
		String[][] column31 = new String[7][100];
		String[][] column32 = new String[12][100];
		String[][] column33 = new String[17][100];
		if(h.containsKey("column31")) column31 =  (String[][])h.get("column31");
		if(h.containsKey("column32")) column32 =  (String[][])h.get("column32");
		if(h.containsKey("column33")) column33 =  (String[][])h.get("column33");
		for (int i=0;i<100;i++) {
			for (int j=0;j<7;j++) {
				if (column31[j][i]=="null") dobbelstenen.table.setValueAt("",i, j); else dobbelstenen.table.setValueAt(column31[j][i],i, j);	
			}
			for (int j=0;j<12;j++) {
				if (column32[j][i]=="null") dobbelstenen.table1.setValueAt("",i, j); else dobbelstenen.table1.setValueAt(column32[j][i],i, j);	
			}
			for (int j=0;j<17;j++) {
				if (column33[j][i]=="null") dobbelstenen.table2.setValueAt("",i, j); else dobbelstenen.table2.setValueAt(column33[j][i],i, j);	
			}
		}
		String[] column41 = new String[6];
		String[] column42 = new String[11];
		String[] column43 = new String[16];
		if(h.containsKey("column41")) column41 =  (String[])h.get("column41");
		if(h.containsKey("column42")) column42 =  (String[])h.get("column42");
		if(h.containsKey("column43")) column43 =  (String[])h.get("column43");
		for (int j=0;j<6;j++) {
			if (column41[j]=="null") dobbelstenen.table3.setValueAt("",0, j+1); else dobbelstenen.table3.setValueAt(column41[j],0, j+1);	
		}
		for (int j=0;j<11;j++) {
			if (column42[j]=="null") dobbelstenen.table4.setValueAt("",0, j+1); else dobbelstenen.table4.setValueAt(column42[j],0, j+1);	
		}
		for (int j=0;j<16;j++) {
			if (column43[j]=="null") dobbelstenen.table5.setValueAt("",0, j+1); else dobbelstenen.table5.setValueAt(column43[j],0, j+1);	
		}
		if(h.containsKey("ogenGemiddeld")) dobbelstenen.ogenGemiddeld =  (double[])h.get("ogenGemiddeld");
		if(h.containsKey("ogenSom")) dobbelstenen.ogenSom =  (int[])h.get("ogenSom");
		if(h.containsKey("experiment1")) dobbelstenen.experiment= ((Integer)h.get("experiment1")).intValue();
		Boolean eenDobbelsteen=false;
		if(h.containsKey("eenDobbelsteenRadio")) eenDobbelsteen= ((Boolean)h.get("eenDobbelsteenRadio")).booleanValue();
		dobbelstenen.eenDobbelsteenRadio.setSelected(eenDobbelsteen);
		Boolean TweeDobbelstenen=false;
		if(h.containsKey("TweeDobbelstenenRadio")) TweeDobbelstenen= ((Boolean)h.get("TweeDobbelstenenRadio")).booleanValue();
		dobbelstenen.tweeDobbelstenenRadio.setSelected(TweeDobbelstenen);
		Boolean DrieDobbelstenen=false;
		if(h.containsKey("DrieDobbelstenenRadio")) DrieDobbelstenen= ((Boolean)h.get("DrieDobbelstenenRadio")).booleanValue();
		dobbelstenen.drieDobbelstenenRadio.setSelected(DrieDobbelstenen);
		dobbelstenen.setAantalDobbelstenen();
		Boolean startSelected1=false;
		if(h.containsKey("startSelected1")) startSelected1= ((Boolean)h.get("startSelected1")).booleanValue();
		if (startSelected1==true) {
			dobbelstenen.wis.setEnabled(true);
		} else {
			dobbelstenen.wis.setEnabled(false);
		}
		dobbelstenen.setStartStop();
		Boolean toonSom=false;
		if(h.containsKey("toonSom")) toonSom= ((Boolean)h.get("toonSom")).booleanValue();
		dobbelstenen.toonSomCheckBox.setSelected(toonSom);
		dobbelstenen.setZichtbaar();
		int maxCount1=0;
		if(h.containsKey("maxCount1")) maxCount1= ((Integer)h.get("maxCount1")).intValue();
		dobbelstenen.maxCount=maxCount1;
		dobbelstenen.aantalWorpenText.setText(Integer.toString(maxCount1));

		// ******* Binominale trekking *********
		String[] column51 = new String[1000];
		String[] column52 = new String[1000];
		if(h.containsKey("column51")) column51 =  (String[])h.get("column51");
		if(h.containsKey("column52")) column52 =  (String[])h.get("column52");
		for (int i=0;i<1000;i++) {
			if (column51[i]=="null") binomTrekking.table.setValueAt("",i, 0); else binomTrekking.table.setValueAt(column51[i],i, 0);	
			if (column52[i]=="null") binomTrekking.table.setValueAt("",i, 1); else binomTrekking.table.setValueAt(column52[i],i, 1);	
		}
		int[] column61 = new int[1000];
		if(h.containsKey("column61")) column61 =  (int[])h.get("column61");
		binomTrekking.trekkingen=column61;
		
		if(h.containsKey("experiment2")) binomTrekking.experiment= ((Integer)h.get("experiment2")).intValue();
		Boolean startSelected2=false;
		if(h.containsKey("startSelected2")) startSelected2= ((Boolean)h.get("startSelected2")).booleanValue();
		if (startSelected2==true) {
			binomTrekking.wis.setEnabled(true);
		} else {
			binomTrekking.wis.setEnabled(false);
		}
		binomTrekking.setStartStop();
		int maxCount2=0;
		if(h.containsKey("maxCount2")) maxCount2= ((Integer)h.get("maxCount2")).intValue();
		binomTrekking.maxCount=maxCount2;
		binomTrekking.aantalTrekkingenText.setText(Integer.toString(maxCount2));
		
		String kans="";
		if(h.containsKey("kans")) kans= ((String)h.get("kans"));
		binomTrekking.kansText.setText(kans);
		
		
		// ******* Steekproef *********
		String[] column71 = new String[1000];
		String[] column72 = new String[1000];
		if(h.containsKey("column71")) column71 =  (String[])h.get("column71");
		if(h.containsKey("column72")) column72 =  (String[])h.get("column72");
		for (int i=0;i<1000;i++) {
			if (column71[i]=="null") steekproef.table.setValueAt("",i, 0); else steekproef.table.setValueAt(column71[i],i, 0);	
			if (column72[i]=="null") steekproef.table.setValueAt("",i, 1); else steekproef.table.setValueAt(column72[i],i, 1);	
		}
		String[] column81 = new String[1000];
		String[] column82 = new String[1000];
		String[] column83 = new String[1000];
		if(h.containsKey("column81")) column81 =  (String[])h.get("column81");
		if(h.containsKey("column82")) column82 =  (String[])h.get("column82");
		if(h.containsKey("column83")) column83 =  (String[])h.get("column83");
		for (int i=0;i<1000;i++) {
			if (column81[i]=="null") steekproef.table1.setValueAt("",i, 0); else steekproef.table1.setValueAt(column81[i],i, 0);	
			if (column82[i]=="null") steekproef.table1.setValueAt("",i, 1); else steekproef.table1.setValueAt(column82[i],i, 1);	
			if (column83[i]=="null") steekproef.table1.setValueAt("",i, 2); else steekproef.table1.setValueAt(column83[i],i, 2);
		}
		if(h.containsKey("experiment3")) steekproef.experiment= ((Integer)h.get("experiment3")).intValue();
		String mu="";
		if(h.containsKey("mu")) mu= ((String)h.get("mu"));
		steekproef.muText.setText(mu);

		String sigma="";
		if(h.containsKey("sigma")) sigma= ((String)h.get("sigma"));
		steekproef.sigmaText.setText(sigma);
	}
	
	public Hashtable getState() {	
		
		//  ****** Munten *********
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
		h.put("startSelected", new Boolean(munten.wis.isEnabled()));
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
		h.put("maxCount", new Integer(munten.maxCount));
		h.put("kansOpKopText", new String(munten.kansOpKopText.getText()));
		
		// ********** Dobbelstenen **********
		
		String[][] column31 = new String[7][100];
		String[][] column32 = new String[12][100];
		String[][] column33 = new String[17][100];
		for (int i=0;i<100;i++) {
			for (int j=0;j<7;j++) {
				column31[j][i]=String.valueOf(dobbelstenen.table.getValueAt(i, j));	
			}
			for (int j=0;j<12;j++) {
				column32[j][i]=String.valueOf(dobbelstenen.table1.getValueAt(i, j));	
			}
			for (int j=0;j<17;j++) {
				column33[j][i]=String.valueOf(dobbelstenen.table2.getValueAt(i, j));	
			}	
		}
		h.put("column31", column31);
		h.put("column32", column32);
		h.put("column33", column33);
		String[] column41 = new String[6];
		String[] column42 = new String[11];
		String[] column43 = new String[16];
		for (int j=0;j<6;j++) {
			column41[j]=String.valueOf(dobbelstenen.table3.getValueAt(0, j+1));	
		}
		for (int j=0;j<11;j++) {
			column42[j]=String.valueOf(dobbelstenen.table4.getValueAt(0, j+1));	
		}
		for (int j=0;j<16;j++) {
			column43[j]=String.valueOf(dobbelstenen.table5.getValueAt(0, j+1));	
		}
		h.put("column41", column41);
		h.put("column42", column42);
		h.put("column43", column43);
		h.put("ogenGemiddeld", dobbelstenen.ogenGemiddeld);
		h.put("ogenSom", dobbelstenen.ogenSom);
		h.put("experiment1", new Integer(dobbelstenen.experiment));
		h.put("eenDobbelsteenRadio", new Boolean(dobbelstenen.eenDobbelsteenRadio.isSelected()));
		h.put("TweeDobbelstenenRadio", new Boolean(dobbelstenen.tweeDobbelstenenRadio.isSelected()));
		h.put("DrieDobbelstenenRadio", new Boolean(dobbelstenen.drieDobbelstenenRadio.isSelected()));
		h.put("toonSom", new Boolean(dobbelstenen.toonSomCheckBox.isSelected()));
		h.put("startSelected1", new Boolean(dobbelstenen.wis.isEnabled()));
		h.put("maxCount1", new Integer(dobbelstenen.maxCount));
		
		// ********** Binominale trekking **********
		
		String[] column51 = new String[1000];
		String[] column52 = new String[1000];
		
		for (int i=0;i<1000;i++) {
			column51[i]=String.valueOf(binomTrekking.table.getValueAt(i, 0));	
			column52[i]=String.valueOf(binomTrekking.table.getValueAt(i, 1));	
		}
		h.put("column51", column51);
		h.put("column52", column52);
		h.put("column61", binomTrekking.trekkingen);
		h.put("experiment2", new Integer(binomTrekking.experiment));
		h.put("startSelected2", new Boolean(binomTrekking.wis.isEnabled()));
		h.put("maxCount2", new Integer(binomTrekking.maxCount));
		h.put("kans", new String(binomTrekking.kansText.getText()));
		
		
		//  ****** Steekproef *********

		String[] column71 = new String[1000];
		String[] column72 = new String[1000];
		
		for (int i=0;i<1000;i++) {
			column71[i]=String.valueOf(steekproef.table.getValueAt(i, 0));	
			column72[i]=String.valueOf(steekproef.table.getValueAt(i, 1));	
		}
		h.put("column71", column71);
		h.put("column72", column72);
		
		String[] column81 = new String[1000];
		String[] column82 = new String[1000];
		String[] column83 = new String[1000];
		
		for (int i=0;i<1000;i++) {
			column81[i]=String.valueOf(steekproef.table1.getValueAt(i, 0));	
			column82[i]=String.valueOf(steekproef.table1.getValueAt(i, 1));	
			column83[i]=String.valueOf(steekproef.table1.getValueAt(i, 2));
		}
		h.put("column81", column81);
		h.put("column82", column82);
		h.put("column83", column83);
		
		h.put("experiment3", new Integer(steekproef.experiment));
		h.put("mu", new String(steekproef.muText.getText()));
		h.put("sigma", new String(steekproef.sigmaText.getText()));
		return h;
	}
	
	
	public void zetOpdracht(Hashtable h, String[] randomVars, Hashtable randomValues) {
		
		if(h.containsKey("muntenRadio")) muntenRadioBool= ((Boolean)h.get("muntenRadio")).booleanValue();
		munten.setVisible(muntenRadioBool);
		if(h.containsKey("muntenInstellingen")) munten.showInstellingen= ((Boolean)h.get("muntenInstellingen")).booleanValue();
		if(h.containsKey("muntenResultaten")) munten.showResultaten= ((Boolean)h.get("muntenResultaten")).booleanValue();
		if(h.containsKey("muntenGrafiek")) munten.showGrafiek= ((Boolean)h.get("muntenGrafiek")).booleanValue();
		if(h.containsKey("muntenTabel")) munten.showTabel= ((Boolean)h.get("muntenTabel")).booleanValue();
		if(h.containsKey("muntenFrequentie")) munten.showFrequentie= ((Boolean)h.get("muntenFrequentie")).booleanValue();
		munten.setZichtbaar();
		
		
		if(h.containsKey("dobbelstenenRadio")) dobbelstenenRadioBool= ((Boolean)h.get("dobbelstenenRadio")).booleanValue();
		dobbelstenen.setVisible(dobbelstenenRadioBool);
		if(h.containsKey("dobbelstenenInstellingen")) dobbelstenen.showInstellingen= ((Boolean)h.get("dobbelstenenInstellingen")).booleanValue();
		if(h.containsKey("dobbelstenenResultaten")) dobbelstenen.showResultaten= ((Boolean)h.get("dobbelstenenResultaten")).booleanValue();
		if(h.containsKey("dobbelstenenGrafiek")) dobbelstenen.showGrafiek= ((Boolean)h.get("dobbelstenenGrafiek")).booleanValue();
		if(h.containsKey("dobbelstenenTabel")) dobbelstenen.showTabel= ((Boolean)h.get("dobbelstenenTabel")).booleanValue();
		dobbelstenen.setZichtbaar();
		
		if(h.containsKey("binomTrekkingRadio")) binomTrekkingRadioBool= ((Boolean)h.get("binomTrekkingRadio")).booleanValue();
		binomTrekking.setVisible(binomTrekkingRadioBool);
		if(h.containsKey("binomTrekkingInstellingen")) binomTrekking.showInstellingen= ((Boolean)h.get("binomTrekkingInstellingen")).booleanValue();
		if(h.containsKey("binomTrekkingGrafiek")) binomTrekking.showGrafiek= ((Boolean)h.get("binomTrekkingGrafiek")).booleanValue();
		if(h.containsKey("binomTrekkingTabel")) binomTrekking.showTabel= ((Boolean)h.get("binomTrekkingTabel")).booleanValue();
		if(h.containsKey("binomTrekkingFrequentie")) binomTrekking.showFrequentie= ((Boolean)h.get("binomTrekkingFrequentie")).booleanValue();
		if(h.containsKey("binomTrekkingRooster")) binomTrekking.showRooster= ((Boolean)h.get("binomTrekkingRooster")).booleanValue();
		binomTrekking.setZichtbaar();

		if(h.containsKey("steekproefLinkerTabel")) steekproef.showLinkerTabel= ((Boolean)h.get("steekproefLinkerTabel")).booleanValue();
		if(h.containsKey("steekproefRechterTabel")) steekproef.showRechterTabel= ((Boolean)h.get("steekproefRechterTabel")).booleanValue();
		steekproef.setZichtbaar();
		
		if(h.containsKey("steekproefRadio")) steekproefRadioBool= ((Boolean)h.get("steekproefRadio")).booleanValue();
		steekproef.setVisible(steekproefRadioBool);
		
		
		Boolean eenMuntTweeMunt=true;	
		if(h.containsKey("eenMuntTweeMunt")) eenMuntTweeMunt= ((Boolean)h.get("eenMuntTweeMunt")).booleanValue();
		munten.eenMuntRadio.setSelected(eenMuntTweeMunt);
		munten.tweeMuntenRadio.setSelected(!eenMuntTweeMunt);
		if(h.containsKey("aantalWorpen")) munten.aantalWorpenText.setText(((String)h.get("aantalWorpen")));
		if(h.containsKey("kansOpMunt")) munten.kansOpKopText.setText(((String)h.get("kansOpMunt")));
		munten.setZichtbaar();
		
		if(h.containsKey("eenDobbelsteenRadio")) dobbelstenen.eenDobbelsteenRadio.setSelected(((Boolean)h.get("eenDobbelsteenRadio")).booleanValue());
		if(h.containsKey("tweeDobbelstenenRadio")) dobbelstenen.tweeDobbelstenenRadio.setSelected(((Boolean)h.get("tweeDobbelstenenRadio")).booleanValue());
		if(h.containsKey("drieDobbelstenenRadio")) dobbelstenen.drieDobbelstenenRadio.setSelected(((Boolean)h.get("drieDobbelstenenRadio")).booleanValue());
		if(h.containsKey("aantalWorpenDobbelsteen")) dobbelstenen.aantalWorpenText.setText(((String)h.get("aantalWorpenDobbelsteen")));
		if(h.containsKey("toonSom")) dobbelstenen.toonSomCheckBox.setSelected(((Boolean)h.get("toonSom")).booleanValue());
		dobbelstenen.setZichtbaar();
		
		if(h.containsKey("kans")) binomTrekking.kansText.setText(((String)h.get("kans")));
		if(h.containsKey("aantalTrekkingen")) binomTrekking.aantalTrekkingenText.setText(((String)h.get("aantalTrekkingen")));
		if(h.containsKey("aantalKeer")) binomTrekking.aantalKeer.setText(((String)h.get("aantalKeer")));
		binomTrekking.setZichtbaar();
		
		if(h.containsKey("mu")) steekproef.muText.setText(((String)h.get("mu")));
		if(h.containsKey("sigma")) steekproef.sigmaText.setText(((String)h.get("sigma")));
		steekproef.setZichtbaar();
	}

	public void setEditState(Hashtable h) {
		
		if(h.containsKey("muntenRadio")) muntenRadioBool= ((Boolean)h.get("muntenRadio")).booleanValue();
		munten.setVisible(muntenRadioBool);
		if(h.containsKey("muntenInstellingen")) munten.showInstellingen= ((Boolean)h.get("muntenInstellingen")).booleanValue();
		if(h.containsKey("muntenResultaten")) munten.showResultaten= ((Boolean)h.get("muntenResultaten")).booleanValue();
		if(h.containsKey("muntenGrafiek")) munten.showGrafiek= ((Boolean)h.get("muntenGrafiek")).booleanValue();
		if(h.containsKey("muntenTabel")) munten.showTabel= ((Boolean)h.get("muntenTabel")).booleanValue();
		if(h.containsKey("muntenFrequentie")) munten.showFrequentie= ((Boolean)h.get("muntenFrequentie")).booleanValue();
		munten.setZichtbaar();
		
		if(h.containsKey("dobbelstenenRadio")) dobbelstenenRadioBool= ((Boolean)h.get("dobbelstenenRadio")).booleanValue();
		dobbelstenen.setVisible(dobbelstenenRadioBool);
		if(h.containsKey("dobbelstenenInstellingen")) dobbelstenen.showInstellingen= ((Boolean)h.get("dobbelstenenInstellingen")).booleanValue();
		if(h.containsKey("dobbelstenenResultaten")) dobbelstenen.showResultaten= ((Boolean)h.get("dobbelstenenResultaten")).booleanValue();
		if(h.containsKey("dobbelstenenGrafiek")) dobbelstenen.showGrafiek= ((Boolean)h.get("dobbelstenenGrafiek")).booleanValue();
		if(h.containsKey("dobbelstenenTabel")) dobbelstenen.showTabel= ((Boolean)h.get("dobbelstenenTabel")).booleanValue();
		dobbelstenen.setZichtbaar();
		
		if(h.containsKey("binomTrekkingRadio")) binomTrekkingRadioBool= ((Boolean)h.get("binomTrekkingRadio")).booleanValue();
		binomTrekking.setVisible(binomTrekkingRadioBool);
		if(h.containsKey("binomTrekkingInstellingen")) binomTrekking.showInstellingen= ((Boolean)h.get("binomTrekkingInstellingen")).booleanValue();
		if(h.containsKey("binomTrekkingGrafiek")) binomTrekking.showGrafiek= ((Boolean)h.get("binomTrekkingGrafiek")).booleanValue();
		if(h.containsKey("binomTrekkingTabel")) binomTrekking.showTabel= ((Boolean)h.get("binomTrekkingTabel")).booleanValue();
		if(h.containsKey("binomTrekkingFrequentie")) binomTrekking.showFrequentie= ((Boolean)h.get("binomTrekkingFrequentie")).booleanValue();
		if(h.containsKey("binomTrekkingRooster")) binomTrekking.showRooster= ((Boolean)h.get("binomTrekkingRooster")).booleanValue();
		binomTrekking.setZichtbaar();
		
		
		if(h.containsKey("steekproefRadio")) steekproefRadioBool= ((Boolean)h.get("steekproefRadio")).booleanValue();
		steekproef.setVisible(steekproefRadioBool);
		
		if(h.containsKey("steekproefLinkerTabel")) steekproef.showLinkerTabel= ((Boolean)h.get("steekproefLinkerTabel")).booleanValue();
		if(h.containsKey("steekproefRechterTabel")) steekproef.showRechterTabel= ((Boolean)h.get("steekproefRechterTabel")).booleanValue();
		steekproef.setZichtbaar();
		
		Boolean eenMuntTweeMunt=true;
		if(h.containsKey("eenMuntTweeMunt")) eenMuntTweeMunt= ((Boolean)h.get("eenMuntTweeMunt")).booleanValue();
		munten.eenMuntRadio.setSelected(eenMuntTweeMunt);
		munten.tweeMuntenRadio.setSelected(!eenMuntTweeMunt);
		if(h.containsKey("aantalWorpen")) munten.aantalWorpenText.setText(((String)h.get("aantalWorpen")));
		if(h.containsKey("kansOpMunt")) munten.kansOpKopText.setText(((String)h.get("kansOpMunt")));
		munten.setZichtbaar();
		
		if(h.containsKey("eenDobbelsteenRadio")) dobbelstenen.eenDobbelsteenRadio.setSelected(((Boolean)h.get("eenDobbelsteenRadio")).booleanValue());
		if(h.containsKey("tweeDobbelstenenRadio")) dobbelstenen.tweeDobbelstenenRadio.setSelected(((Boolean)h.get("tweeDobbelstenenRadio")).booleanValue());
		if(h.containsKey("drieDobbelstenenRadio")) dobbelstenen.drieDobbelstenenRadio.setSelected(((Boolean)h.get("drieDobbelstenenRadio")).booleanValue());
		if(h.containsKey("aantalWorpenDobbelsteen")) dobbelstenen.aantalWorpenText.setText(((String)h.get("aantalWorpenDobbelsteen")));
		if(h.containsKey("toonSom")) dobbelstenen.toonSomCheckBox.setSelected(((Boolean)h.get("toonSom")).booleanValue());
		dobbelstenen.setZichtbaar();
		
		if(h.containsKey("kans")) binomTrekking.kansText.setText(((String)h.get("kans")));
		if(h.containsKey("aantalTrekkingen")) binomTrekking.aantalTrekkingenText.setText(((String)h.get("aantalTrekkingen")));
		if(h.containsKey("aantalKeer")) binomTrekking.aantalKeer.setText(((String)h.get("aantalKeer")));
		binomTrekking.setZichtbaar();
		
		if(h.containsKey("mu")) steekproef.muText.setText(((String)h.get("mu")));
		if(h.containsKey("sigma")) steekproef.sigmaText.setText(((String)h.get("sigma")));
		steekproef.setZichtbaar();
	}

	public Hashtable getEditState() {
		Hashtable h = new Hashtable();
		h.put("eenMuntTweeMunt", new Boolean(munten.eenMuntRadio.isSelected()));
		h.put("aantalWorpen", new String(munten.aantalWorpenText.getText()));
		h.put("kansOpMunt", new String(munten.kansOpKopText.getText()));
		
		h.put("eenDobbelsteenRadio", new Boolean(dobbelstenen.eenDobbelsteenRadio.isSelected()));
		h.put("tweeDobbelstenenRadio", new Boolean(dobbelstenen.tweeDobbelstenenRadio.isSelected()));
		h.put("drieDobbelstenenRadio", new Boolean(dobbelstenen.drieDobbelstenenRadio.isSelected()));
		h.put("aantalWorpenDobbelsteen", new String(dobbelstenen.aantalWorpenText.getText()));
		h.put("toonSom", new Boolean(dobbelstenen.toonSomCheckBox.isSelected()));
		
		h.put("kans", new String(binomTrekking.kansText.getText()));
		h.put("aantalTrekkingen", new String(binomTrekking.aantalTrekkingenText.getText()));
		h.put("aantalKeer", new String(binomTrekking.aantalKeer.getText()));
		
		h.put("steekproefLinkerTabel", new Boolean(steekproef.showLinkerTabel));
		h.put("steekproefRechterTabel", new Boolean(steekproef.showRechterTabel));
		h.put("mu", new String(steekproef.muText.getText()));
		h.put("sigma", new String(steekproef.sigmaText.getText()));
		return h;
		//return getState();
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
