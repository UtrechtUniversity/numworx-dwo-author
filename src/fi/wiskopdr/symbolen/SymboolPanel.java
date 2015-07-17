package fi.wiskopdr.symbolen;

import java.awt.Color;
import java.awt.event.ActionListener;
import java.util.Hashtable;

import javax.swing.JPanel;

import fi.beans.wiskopdrbeans.InteractieEditPanel;
import fi.beans.wiskopdrbeans.InteractiePanel;

public class SymboolPanel extends JPanel implements InteractiePanel{

	Symbool symbool;
	boolean vulHoogte, vulBreedte;
	int hoogte, breedte;
	int richting = 0;
	
	public SymboolPanel()
	{
		setLayout(null);
		setOpaque(false);
		symbool = new Symbool(richting);
		symbool.setBounds(0, 0, this.getWidth(), this.getHeight());
		add(symbool);
	}
	
	public void zetSymboolKeuze(int keuze)
	{
		remove(symbool);
		if(keuze == Symbool.GEEN)
			symbool = new Symbool(richting);
		else if(keuze == Symbool.LIJN)
			symbool = new Lijn(richting);
		else if(keuze == Symbool.PIJL)
			symbool = new Pijl(richting);
		else if(keuze == Symbool.ACCOLADE)
			symbool = new Accolade(richting);
		else if(keuze == Symbool.ELLIPS)
			symbool = new Ellips(richting);
		symbool.setBounds(0,0,this.getWidth(), this.getHeight());
		add(symbool);
		symbool.repaint(); 
	}
	
	public void zetRichtingKeuze(int keuze)
	{
		if(symbool instanceof Lijn)
		{
			if(keuze == 0)
				richting = Symbool.RICHTING_LINKS;
			else if(keuze == 1)
				richting = Symbool.RICHTING_BOVEN;
			else if(keuze == 2)
				richting = Symbool.RICHTING_LINKSONDER;
			else
				richting = Symbool.RICHTING_LINKSBOVEN;
		}
		else if(symbool instanceof Pijl)
		{
			richting = keuze;
		}
		else if(symbool instanceof Accolade)
		{
			if(keuze == 0)
				richting = Symbool.RICHTING_LINKS;
			else if(keuze == 1)
				richting = Symbool.RICHTING_RECHTS;
			else if(keuze == 2)
				richting = Symbool.RICHTING_BOVEN;
			else if(keuze == 3)
				richting = Symbool.RICHTING_BENEDEN;
		}
		else if(symbool instanceof Ellips)
		{	//nog invullen
		}
		symbool.zetRichting(richting);
		symbool.repaint();
	}
	
	public void zetVulHoogte(boolean vulH)
	{
		vulHoogte = vulH;
	}
	
	public void zetVulBreedte(boolean vulB)
	{
		vulBreedte = vulB;
	}
	
	public void zetDikte(int dikte)
	{
		symbool.zetDikte(dikte);
	}
	
	public void zetKleur(Color kleur)
	{
		symbool.zetKleur(kleur);
	}

	@Override
	public void zetOpdracht(Hashtable b, String[] randomVars,
			Hashtable randomValues) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setState(Hashtable b) {
		
	}

	@Override
	public void setEditState(Hashtable b) {
		
	}

	@Override
	public Hashtable getState() {
		return null;
	}

	@Override
	public Hashtable getEditState() {
		int dikte = 1;
		int richting = 0;
		int type = 0;
		Color kleur = Color.black;
		
		dikte = symbool.dikte;
		richting = symbool.richting;
		type = symbool.geefType();
		
		Hashtable h = new Hashtable();
		h.put("dikte", new Integer(dikte));
		h.put("richting", new Integer(richting));
		h.put("type", new Integer(type));
		return h;
	}

	@Override
	public InteractieEditPanel getEditPanel() {
		return new SymboolEditPanel();
	}

	@Override
	public void wis() {
		
	}

	@Override
	public void zetMaat() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getIpId() {
		return 0;
	}

	@Override
	public int getScore() {
		return 0;
	}

	@Override
	public int[][] getScoreObjectives() {
		return null;
	}

	@Override
	public int getScoreMax() {
		return 0;
	}

	@Override
	public boolean isCorrect() {
		return false;
	}

	@Override
	public boolean isFout() {
		return false;
	}

	@Override
	public void zetMode(int mode) {
		
	}

	@Override
	public void zetNagekeken(boolean b) {
		
	}

	@Override
	public void stop() {
		
	}

	@Override
	public void start() {
		
	}

	@Override
	public void destroy() {
		
	}

	@Override
	public void opnieuw() {
		
	}

	@Override
	public void kijkNa() {
		
	}

	@Override
	public void kijkNa(int stapNr) {
		
	}

	@Override
	public void addActionListener(ActionListener al) {
		
	}
	
	
}
