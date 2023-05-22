package fi.wiskopdr;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import fi.balansfruit.BalansFruitInteractiePanel;
import fi.beans.wiskopdrbeans.InteractiePanel;
import fi.wiskopdr.expressies.Algebra;
import fi.wiskopdr.expressies.Expressie;
import fi.wiskopdr.expressies.Vergelijking;
import fi.wiskopdr.formuleobjects.FormuleParser;

public class BalansVergCom implements ActionListener
{
	private BalansFruitInteractiePanel balansFruitPanel;
	private AntwoordVergelijkingVak balansVergelijkingVak;
	
	public BalansVergCom(TekstVakPanel tekstVakPanel)
	{	
		InteractiePanel ip0 = tekstVakPanel.geefInteractiePanel(0,0);
		if(ip0 instanceof BalansFruitInteractiePanel)balansFruitPanel = (BalansFruitInteractiePanel)ip0;
		
		InteractiePanel ip1 = tekstVakPanel.geefInteractiePanel(0,1);
		if(ip1 instanceof AntwoordVergelijkingVak)balansVergelijkingVak = (AntwoordVergelijkingVak)ip1;
		
		if(balansFruitPanel!=null)balansFruitPanel.addActionListener(this);
		if(balansVergelijkingVak!=null)balansVergelijkingVak.addActionListener(this);
	}

	public void actionPerformed(ActionEvent e) 
	{	if(e.getActionCommand().equals("balansvergelijking"))
		{	if(e.getSource() == balansFruitPanel)
			{	String balansString = balansFruitPanel.geefBalansVergelijking();
				balansVergelijkingVak.zetBalansVergelijking(FormuleParser.parseVergelijking("$f" + balansString + "@"));
			}
	    	else 
	    		if(e.getSource() == balansVergelijkingVak)
	    	{	Vergelijking balansVerg = balansVergelijkingVak.geefBalansVergelijking();
	    		Expressie exp1 = Algebra.herleid(balansVerg.geefExpLinks());
	    		Expressie exp2 = Algebra.herleid(balansVerg.geefExpRechts());
	    		String sx1 = exp1.toString().charAt(0)=='x' ? "1" : "";
	    		String sx2 = exp2.toString().charAt(0)=='x' ? "1" : "";
	    		String balansString = sx1+exp1.toString()+"="+sx2+exp2.toString();
	    		balansFruitPanel.zetBalansVergelijking(balansString);
	    	}
	    	return;
		}
	    if(e.getActionCommand().equals("balansvergelijkinginit"))
		{	if(e.getSource() == balansVergelijkingVak)
	    	{	Vergelijking balansVerg = balansVergelijkingVak.geefInitBalansVergelijking();
	    		if(balansVerg==null) return;
	    		double[] coeff = Algebra.geefCoefficienten(balansVerg);
	    		if(coeff.length==2 && coeff[1]!=0)	balansFruitPanel.zetBalansWaardeX(-coeff[0]/coeff[1]);
	    		
	    		balansVerg = balansVergelijkingVak.geefBalansVergelijking();
	    		Expressie exp1 = Algebra.herleid(balansVerg.geefExpLinks());
	    		Expressie exp2 = Algebra.herleid(balansVerg.geefExpRechts());
	    		String sx1 = exp1.toString().charAt(0)=='x' ? "1" : "";
	    		String sx2 = exp2.toString().charAt(0)=='x' ? "1" : "";
	    		String balansString = sx1+exp1.toString()+"="+sx2+exp2.toString();
	    		balansFruitPanel.zetBalansVergelijking(balansString);
	    	}
	    	return;
		}
	    if(e.getActionCommand().equals("balansstap") || e.getSource() == balansFruitPanel)
		{	balansVergelijkingVak.maakBalansStap();
		}
	}
}
