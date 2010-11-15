package fi.tekenveelvlakopdr;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import fi.beans.base64code.*;
import fi.beans.tekstobjects.*;

import fi.tekenveelvlakopdr.opdrnav.*;

public class MyOpdrContainer extends OpdrContainer implements ItemListener
{
	private Hashtable veelvlakState, basisFiguurState, voorbeeldState;
	private TekstArea tekstArea;
	private TekenVeelvlak tekenveelvlak;
	private Viewer3d viewer;
	private int basisFiguur;
	private int aantalHulpPunten;
	private Checkbox klaarCB;
	private boolean klaar;
	
	public MyOpdrContainer(int x, int y, int b, int h, TekenVeelvlak tvv, Viewer3d v)
	{	setLayout(null);
		setBounds(x,y,b,h);
		setOpaque(false);
        //setBackground(getBackground());
		
		tekenveelvlak = tvv;
		viewer = v;
		
		tekstArea = new TekstArea();
		tekstArea.setBounds(10,200,180,150);
		add(tekstArea);
		
		klaarCB = new Checkbox("Klaar");
		klaarCB.setBounds(580,getSize().height-70,90,20);
		klaarCB.addItemListener(this);
		add(klaarCB,0);
		
	}
	
	public void zetOpdracht(String s)
	{	if(s==null || s.equals(""))return;
		Object o = StringCodeObject.decodeStringToObject(s);
		if(o==null)return;
		Hashtable h = (Hashtable)o;
		
		Hashtable voorbeeldState = null;
		Hashtable basisFiguurState = null;
		String tekst = null;
		int basisFiguur = 0;
		int aantalHulpPunten = 0;
				
		if(h.containsKey("voorbeeldState")) voorbeeldState = (Hashtable)h.get("voorbeeldState");
		if(h.containsKey("basisFiguurState")) basisFiguurState = (Hashtable)h.get("basisFiguurState");
		if(h.containsKey("tekst")) tekst = (String)h.get("tekst");
		if(h.containsKey("basisFiguur")) basisFiguur = ((Integer)h.get("basisFiguur")).intValue();
		if(h.containsKey("aantalHulpPunten")) aantalHulpPunten = ((Integer)h.get("aantalHulpPunten")).intValue();
		
		this.voorbeeldState = voorbeeldState;
		this.basisFiguurState = basisFiguurState;
		this.veelvlakState = basisFiguurState;
		tekstArea.setText(tekst);
		tekstArea.resize();

		viewer.setState(voorbeeldState);
		
		this.basisFiguur = basisFiguur;
		this.aantalHulpPunten = aantalHulpPunten;
		tekenveelvlak.zetBasis(basisFiguur,aantalHulpPunten);
		
		this.klaar = klaar;
		klaarCB.setState(klaar);
	}

	public Hashtable geefvoorbeeldState()
	{	return voorbeeldState;
	}

	public void setState(Hashtable h)
	{	Hashtable veelvlakState = null;
		boolean klaar = false;
		
		if(h.containsKey("veelvlakState")) veelvlakState = (Hashtable)h.get("veelvlakState");
		if(h.containsKey("klaar")) klaar = ((Boolean)h.get("klaar")).booleanValue();
		
		this.veelvlakState = veelvlakState;
		this.klaar = klaar;
		klaarCB.setState(klaar);

		//tekenveelvlak.setState(veelvlakState);
	}

	public Hashtable getState()
	{	Hashtable h = new Hashtable();
		
		Hashtable veelvlakState = null;
		boolean klaar = false;
		
		veelvlakState = this.veelvlakState;
		klaar = this.klaar;
		
		h.put("veelvlakState",veelvlakState);
		h.put("klaar",new Boolean(klaar));
		
		return h;
	}

	public void stop()
	{	veelvlakState = tekenveelvlak.getState();
	}
	
	public void start()
	{	tekenveelvlak.zetBasis(basisFiguur,aantalHulpPunten);
		tekenveelvlak.setState(veelvlakState);
		tekenveelvlak.begin = true;
		tekenveelvlak.tekenOpnieuw();
		viewer.setState(voorbeeldState);
		viewer.zetBeginHoeken(20,-20);
		viewer.tekenOpnieuw();
	}
	
	public void itemStateChanged(ItemEvent e)
	{	if(e.getSource()==klaarCB)
		{	klaar = klaarCB.getState();
			if(klaar)
			{	score = 10;
				correct = true;
			}
			else
			{	score = 0;
				correct = false;
			}
			produceAction("changed");
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
 	//

}
