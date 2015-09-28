package fi.wiskopdr.symbolen;

import java.awt.Color;
import java.awt.event.ActionListener;
import java.util.Hashtable;

import javax.swing.JPanel;

import fi.beans.wiskopdrbeans.InteractieEditPanel;
import fi.beans.wiskopdrbeans.InteractiePanel;
import fi.wiskopdr.tekstobjects.TekstInteractiePanelVak;
import fi.wiskopdr.tekstobjects.TekstRegel;

public class SymboolPanel extends JPanel implements InteractiePanel{

	Symbool symbool;
	boolean vulHoogte, vulBreedte;
	//int hoogte, breedte;
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
	
	public Color getKleur()
	{
		return symbool.kleur;
	}
	
	public void zetBreedte(int b)
	{
		this.setSize(b, this.getHeight());
		symbool.setSize(b, this.getHeight());
	}
	
	public void zetHoogte(int h)
	{
		this.setSize(this.getWidth(), h);
		symbool.setSize(this.getWidth(), h);
	}

	@Override
	public void zetOpdracht(Hashtable h, String[] randomVars,
			Hashtable randomValues) {
		
		int dikte = 1;
		int richting = 0;
		int type = 0;
		int kleurR = 0;
		int kleurG = 0;
		int kleurB = 0;
		boolean vulHoogte = false;
		
		
		if(h.containsKey("dikte"))
			dikte = ((Integer) h.get("dikte")).intValue();
		if(h.containsKey("richting"))
			richting = ((Integer) h.get("richting")).intValue();
		if(h.containsKey("type"))
			type = ((Integer) h.get("type")).intValue();
		if(h.containsKey("kleurR"))
			kleurR = ((Integer) h.get("kleurR")).intValue();
		if(h.containsKey("kleurG"))
			kleurG = ((Integer) h.get("kleurG")).intValue();
		if(h.containsKey("kleurB"))
			kleurB = ((Integer) h.get("kleurB")).intValue();
		if(h.containsKey("vulHoogte"))
			vulHoogte = ((Boolean) h.get("vulHoogte")).booleanValue();
		
		zetSymboolKeuze(type);
		symbool.zetDikte(dikte);
		this.richting = richting;
		symbool.zetRichting(richting);
		symbool.zetKleur(new Color(kleurR, kleurG, kleurB));
		this.vulHoogte = vulHoogte;
		if(this.vulHoogte)
		{	int hoogte = this.getHeight();
			if(getParent() instanceof TekstInteractiePanelVak)
			{	TekstInteractiePanelVak parent = (TekstInteractiePanelVak) getParent();
				if(parent.getParent() instanceof TekstRegel)
				{	TekstRegel regelParent = (TekstRegel) parent.getParent();
					hoogte = regelParent.getTekstVak().getHeight();
				}
			}
			zetHoogte(hoogte);
		
		}
	}

	@Override
	public void setState(Hashtable b) {
		
	}

	@Override
	public void setEditState(Hashtable b) {
		zetOpdracht(b, null, null);
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
		int kleurR = 0;
		int kleurG = 0;
		int kleurB = 0;
		boolean vulHoogte = false;
		
		dikte = symbool.dikte;
		richting = symbool.richting;
		type = symbool.geefType();
		kleurR = symbool.kleur.getRed();
		kleurG = symbool.kleur.getGreen();
		kleurB = symbool.kleur.getBlue();
		vulHoogte = this.vulHoogte;
		
		Hashtable h = new Hashtable();
		h.put("dikte", new Integer(dikte));
		h.put("richting", new Integer(richting));
		h.put("type", new Integer(type));
		h.put("kleurR", new Integer(kleurR));
		h.put("kleurG", new Integer(kleurG));
		h.put("kleurB", new Integer(kleurB));
		h.put("vulHoogte", new Boolean(vulHoogte));
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
