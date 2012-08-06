package fi.spot_problems_dwo.wiskopdr;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import fi.beans.base64code.*;
import fi.spot_problems_dwo.wiskopdr.tekstobjects.*;
import fi.spot_problems_dwo.wiskopdr.formuleobjects.*;
import fi.spot_problems_dwo.wiskopdr.expressies.*;
import fi.spot_problems_dwo.wiskopdr.opdrnav.*;

public class MyOpdrContainer extends OpdrContainer implements ActionListener 
{
	private Label titelLabel;
	private TekstArea tekstArea;
	private AntwoordFormuleVak antwoordVak;
	private String codeString;
	private String opdrachtString;
	private String gewensteAntwoordString;
	
	
	
	
	public MyOpdrContainer(int x, int y, int b, int h)
	{	setLayout(null);
		setBounds(x,y,b,h);
		
		titelLabel = new Label("");
		titelLabel.setBounds(30,30,250,30);
		titelLabel.setFont(new Font("SansSerif",Font.PLAIN,20));
		add(titelLabel);
				
		tekstArea = new TekstArea();
		tekstArea.setBounds(30,80,250,300);
		add(tekstArea);
				
		tekstArea.resize();
		
		antwoordVak = new AntwoordFormuleVak();
		antwoordVak.setBounds(280,80,460,300);
		antwoordVak.addActionListener(this);
		add(antwoordVak,0);
		
		
	}
	
	public void setBoundsAntwoordVak(int x, int y, int b, int h)
	{	antwoordVak.setBounds(x,y,b,h);
	}
	
	public void setBoundsTekstArea(int x, int y, int b, int h)
	{	tekstArea.setBounds(x,y,b,h);
		tekstArea.setText(tekstArea.getText(""));
		tekstArea.resize();
	}
	
	public void setBoundsTitelLabel(int x, int y, int b, int h)
	{	titelLabel.setBounds(x,y,b,h);
	}
	
	public void zetAntwoordFormuleOptie(boolean b)
	{	antwoordVak.zetFormMode(b);
	}
	
	public void zetAntwoordScrollOptie(boolean b)
	{	antwoordVak.zetScrollOptie(b);
	}
	
	public void zetOpdracht(String s)
	{	setEditModeState(s);
		
	}
	
	public void zetStappenAntwoordVak(boolean b)
	{	antwoordVak.zetStappen(b);
	}
	
	public void setEditModeState(String s)
	{	if(s==null || s.equals(""))return;
		codeString = s;
		
		Object o = StringCodeObject.decodeStringToObject(s);
		if(o==null)return;
		Hashtable h = (Hashtable)o;
		
		String titel = "titel";
		String tekst = "tekst";
		String randVarString = "";
		String antwoordString = "$f@";
		boolean herleiding = false;
		boolean exact = false;
		boolean stappen = true;
		int soortHerleiding = 0;
		int puntenGelijkwaardig = 10;
		int puntenHerleiding = 0;
		int puntenExact = 0;
		
		
		if(h.containsKey("titel")) titel = (String)h.get("titel");
		if(h.containsKey("tekst")) tekst = (String)h.get("tekst");
		if(h.containsKey("randVarString")) randVarString = (String)h.get("randVarString");
		if(h.containsKey("antwoordString")) antwoordString = (String)h.get("antwoordString");
		if(h.containsKey("herleiding")) herleiding = ((Boolean)h.get("herleiding")).booleanValue();
		if(h.containsKey("exact")) exact = ((Boolean)h.get("exact")).booleanValue();
		if(h.containsKey("stappen")) stappen = ((Boolean)h.get("stappen")).booleanValue();
		if(h.containsKey("soortHerleiding")) soortHerleiding = ((Integer)h.get("soortHerleiding")).intValue();
		if(h.containsKey("puntenGelijkwaardig")) puntenGelijkwaardig = ((Integer)h.get("puntenGelijkwaardig")).intValue();
		if(h.containsKey("puntenHerleiding")) puntenHerleiding = ((Integer)h.get("puntenHerleiding")).intValue();
		if(h.containsKey("puntenExact")) puntenExact = ((Integer)h.get("puntenExact")).intValue();
		
		antwoordVak.setScoreData(herleiding,exact,soortHerleiding, puntenGelijkwaardig, puntenHerleiding, puntenExact);
		antwoordVak.zetStappen(stappen);
		
		titelLabel.setText(titel.trim());
		VariableCollection vc = new VariableCollection();
		vc.setVariables(randVarString);
		String[] varnamen = vc.getVariableNames();
		Hashtable waarden = vc.getRandomValues();
				
		for(int i=tekst.length()-1 ; i>-1; i--)
		{	if(tekst.charAt(i)=='@')
			{	int index = tekst.substring(0,i).lastIndexOf("$f");
				String formString = tekst.substring(index,i+1);
				for(int j=formString.length()-1 ; j>-1; j--)
				{	if(formString.charAt(j)=='#')
					{	int index1 = formString.substring(0,j).lastIndexOf("#");
						String parseString = formString.substring(index1+1,j);
						parseString = SubstitueerRandom(parseString, varnamen, waarden);
						formString = ""+formString.substring(0,index1)+parseString+formString.substring(j+1);
						j=index1;
					}	
				}		
				tekst = ""+tekst.substring(0,index)+formString+tekst.substring(i+1);
				i=index;
			}
		}
		for(int i=antwoordString.length()-1 ; i>-1; i--)
		{	if(antwoordString.charAt(i)=='@')
			{	int index = antwoordString.substring(0,i).lastIndexOf("$f");
				String formString = antwoordString.substring(index,i+1);
				for(int j=formString.length()-1 ; j>-1; j--)
				{	if(formString.charAt(j)=='#')
					{	int index1 = formString.substring(0,j).lastIndexOf("#");
						String parseString = formString.substring(index1+1,j);
						parseString = SubstitueerRandom(parseString, varnamen, waarden);
						formString = ""+formString.substring(0,index1)+parseString+formString.substring(j+1);
						j=index1;
					}	
				}		
				antwoordString = ""+antwoordString.substring(0,index)+formString+antwoordString.substring(i+1);
				i=index;
			}
		}
		/*for(int i=antwoordString.length()-1 ; i>-1; i--)
		{	if(antwoordString.charAt(i)=='@')
			{	int index = antwoordString.substring(0,i).lastIndexOf("$f");
				String formString = antwoordString.substring(index,i+1);
				formString = SubstitueerRandom(formString, varnamen, waarden);
				antwoordString = ""+antwoordString.substring(0,index)+formString+antwoordString.substring(i+1);
				i=index;
			}
		}*/
		this.opdrachtString = tekst;
		tekstArea.setText(tekst);
		
		this.gewensteAntwoordString = antwoordString;
		antwoordVak.zetJuisteAntwoord(antwoordString);
		
		tekstArea.resize();
	}
	
	private String SubstitueerRandom(String formString, String[] varnamen, Hashtable waarden)
	{	String sNieuw = null;
		String s1Nieuw = null;
		String s2Nieuw = null;
		Expressie e = null;
		Expressie e1 = null;
		Expressie e2 = null;
		boolean parseable = true;
		int n = formString.indexOf("=");
		if(n>-1)
		{	FormuleParser p = new FormuleParser();
			String s1 = formString.substring(0,n);
			e1 = p.parse(p.schoon(p.formuleString("$f" + s1 + "@")));
			if(e1==null)parseable = false;
			if(parseable)
			{	for(int j=0 ; j<varnamen.length; j++)
				{	int value = ((Integer)waarden.get(varnamen[j])).intValue();
					e1 = e1.substitueer(value,varnamen[j]);
				}
				e1 = Algebra.herleidMild(e1);
				s1Nieuw = e1.toString();
			}
			else 
			{	s1Nieuw = s1;
			}
			
			parseable = true;
			String s2 = formString.substring(n+1);
			e2 = p.parse(p.schoon(p.formuleString("$f" + s2 + "@")));
			if(e2==null)parseable = false;
			if(parseable)
			{	for(int j=0 ; j<varnamen.length; j++)
				{	int value = ((Integer)waarden.get(varnamen[j])).intValue();
					e2 = e2.substitueer(value,varnamen[j]);
				}
				e2 = Algebra.herleidMild(e2);			
				s2Nieuw = e2.toString();
			}
			else 
			{	s2Nieuw = s2;
			}
			sNieuw = s1Nieuw + "=" + s2Nieuw;
		}
		else 
		{	String s = formString;
			FormuleParser p = new FormuleParser();
			e = p.parse(p.schoon(p.formuleString("$f" + s + "@")));
			if(e==null)parseable = false;
			if(parseable)
			{	for(int j=0 ; j<varnamen.length; j++)
				{	int value = ((Integer)waarden.get(varnamen[j])).intValue();
					e = e.substitueer(value,varnamen[j]);
				}
				e = Algebra.herleidMild(e);
				sNieuw = e.toString();
			}
			else 
			{	sNieuw = s;
			}
		}
		return sNieuw;
	}
	
	
	public void setState(Hashtable h)
	{	String opdrachtString = null;
		String gewensteAntwoordString = null;
		String antwoordString = null;
		boolean ingevuld = false;
		Hashtable antwoordVakState = null;
	
		if(h.containsKey("opdrachtString")) opdrachtString = (String)h.get("opdrachtString");
		if(h.containsKey("gewensteAntwoordString")) gewensteAntwoordString = (String)h.get("gewensteAntwoordString");
		if(h.containsKey("antwoordString")) antwoordString = (String)h.get("antwoordString");
		if(h.containsKey("ingevuld")) ingevuld = ((Boolean)h.get("ingevuld")).booleanValue();
		if(h.containsKey("antwoordVakState")) antwoordVakState = (Hashtable)h.get("antwoordVakState");
		
		this.opdrachtString = opdrachtString;
		this.gewensteAntwoordString = gewensteAntwoordString;
		
		tekstArea.setText(opdrachtString);
		antwoordVak.zetJuisteAntwoord(gewensteAntwoordString);
		if(ingevuld)
		{	antwoordVak.vulVak(antwoordString);
			kijkNa();
		}
		antwoordVak.setState(antwoordVakState);
	}
	
	public Hashtable getState()
	{	String opdrachtString=null;
		String gewensteAntwoordString=null;
		String antwoordString=null;
		boolean ingevuld = true;
		Hashtable antwoordVakState = null;
		
		opdrachtString = this.opdrachtString;
		gewensteAntwoordString = this.gewensteAntwoordString;
		antwoordString = antwoordVak.toString();
		ingevuld = antwoordVak.geefExpressie()!=null;
		antwoordVakState = antwoordVak.getState();
			    
	    			    
	    Hashtable h = new Hashtable();
	    h.put("opdrachtString", opdrachtString);
	    h.put("gewensteAntwoordString", gewensteAntwoordString);
	    h.put("antwoordString", antwoordString);
	    h.put("ingevuld", new Boolean(ingevuld));
	    h.put("antwoordVakState", antwoordVakState);
	    
	    return h;
	}

	public void stop()
	{	antwoordVak.stop();
	}
	
	public void destroy()
	{	
		remove(antwoordVak);
		antwoordVak.destroy();
		antwoordVak = null;
		
		
		remove(tekstArea);
		tekstArea.destroy();
		tekstArea = null;
	}

	public void start()
	{	antwoordVak.start();
	}

	public void kijkNa()
	{	antwoordVak.kijkNa();
	}

	public void opnieuw()
	{	score = 0;
		correct = false;
		
	}

	public void zetMode(int mode)
	{	this.mode = mode;
		
	}
	
	public void setBackground(Color c)
	{	
		super.setBackground(c);	
	}
	
	public void zetUitlegTekst(String s)
	{	tekstArea.setText(s);
		tekstArea.resize();
	}

	
	public void actionPerformed(ActionEvent e)
	{	if(e.getSource()==antwoordVak)
		{	if(e.getActionCommand().equals("changed"))
			{	score = antwoordVak.getScore();
				correct = antwoordVak.isCorrect();
				produceAction("changed");
			}
			
			/*if(e.getActionCommand().equals("goed"))
			{	score = 10;
				correct = true;
				produceAction("changed");
			}
			else if(e.getActionCommand().equals("fout"))
			{	score = 0;
				correct = false;
				produceAction("changed");
			}
			if(e.getActionCommand().equals("blijktgoed"))
			{	score = 10;
				boolean correctOud = correct;
				correct = true;
				if(correct!=correctOud)produceAction("changed");
			}
			else if(e.getActionCommand().equals("blijktfout"))
			{	score = 0;
				boolean correctOud = correct;
				correct = false;
				if(correct!=correctOud)produceAction("changed");
			}*/
		}
	}
	
	//ActionProducer
	private ActionListener actionListener = null;
	
	public void addActionListener(ActionListener l) 
 	{	actionListener = AWTEventMulticaster.add(actionListener,l);
 	}
 	
 	public void removeActionListener(ActionListener l)
 	{	actionListener = AWTEventMulticaster.remove(actionListener, l);
 	}	
 	
 	public void produceAction(String command)
 	{	if (actionListener != null)
 		{	actionListener.actionPerformed( new ActionEvent(this, 0, command) );
 		}
 	}
 	//end ActionProducer
}
