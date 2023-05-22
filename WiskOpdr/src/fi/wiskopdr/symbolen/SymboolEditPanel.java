package fi.wiskopdr.symbolen;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.Hashtable;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import fi.beans.wiskopdrbeans.InteractieEditPanel;
import fi.wiskopdr.WiskOpdr;


public class SymboolEditPanel extends JPanel implements InteractieEditPanel, ActionListener, FocusListener{

	SymboolPanel symboolPanel;
	int symboolBreedte = 100;
	int symboolHoogte = 100;
	int editHeight = 400;
	
	Font theFont;
	FontMetrics theFM;
	
	int width = 200;
	int height = 20;
	
	int offset = 10;
	
	JComboBox symboolKeuzeBox;
	JComboBox richtingKeuzeBox;
	JLabel dikteLabel;
	JTextField dikteField;
	JButton kleurKnop;
	JCheckBox vulHoogteCB;
	
	public SymboolEditPanel()
	{
		setLayout(null);
		
		theFont = new Font("Dialog", Font.PLAIN, 12);
		theFM = getFontMetrics(theFont);
		
		symboolPanel = new SymboolPanel();
		symboolPanel.setBounds(offset, offset, symboolBreedte, symboolHoogte);
		add(symboolPanel);
		
		int currentX = symboolBreedte + 2 * offset;
		int currentY = offset;
		
		String[] symboolString = {WiskOpdr.rb.getString("SYM_kiesSymbool"), 
				WiskOpdr.rb.getString("SYM_lijn"), WiskOpdr.rb.getString("SYM_pijl"),
				WiskOpdr.rb.getString("SYM_accolade"), WiskOpdr.rb.getString("SYM_ellips"), WiskOpdr.rb.getString("SYM_haak")};
		symboolKeuzeBox = new JComboBox(symboolString);
		symboolKeuzeBox.setBounds(currentX, currentY, width, height);
		symboolKeuzeBox.setFont(theFont);
		add(symboolKeuzeBox);
		symboolKeuzeBox.addActionListener(this);
		
		currentY += height + offset;
		
		String[] richtingString = {WiskOpdr.rb.getString("SYM_kiesEerstSymbool")};
		richtingKeuzeBox  = new JComboBox(richtingString);
		richtingKeuzeBox.setBounds(currentX, currentY, width, height);
		richtingKeuzeBox.setFont(theFont);
		add(richtingKeuzeBox);
		richtingKeuzeBox.addActionListener(this);
		
		currentY += height + offset;
		
		dikteLabel = new JLabel(WiskOpdr.rb.getString("SYM_dikte"));
		dikteLabel.setBounds(currentX, currentY, width / 2, height);
		dikteLabel.setFont(theFont);
		add(dikteLabel);
		currentX += width / 2 + offset;
		
		dikteField = new JTextField("1");
		dikteField.setBounds(currentX, currentY, width / 2 - offset, height);
		add(dikteField);
		dikteField.addActionListener(this);
		dikteField.addFocusListener(this);
		
		currentX -= width / 2 + offset;
		currentY += height + offset;
		
		kleurKnop = new JButton(WiskOpdr.rb.getString("SYM_kleur"));
		kleurKnop.setBounds(currentX, currentY, width / 2, height);
		kleurKnop.setFont(theFont);
		add(kleurKnop);
		kleurKnop.addActionListener(this);
		
		currentY += height + offset;
		
		vulHoogteCB = new JCheckBox(WiskOpdr.rb.getString("SYM_vulHoogte"));
		vulHoogteCB.setBounds(currentX, currentY, width, height);
		vulHoogteCB.setOpaque(false);
		vulHoogteCB.setFont(theFont);
		add(vulHoogteCB);
		vulHoogteCB.addActionListener(this);
		
	}

	public void plaatsComponenten()
	{
		if(symboolKeuzeBox == null)
			return;
		int indent = symboolPanel.getWidth() + 2 * offset;
		symboolKeuzeBox.setLocation(indent, symboolKeuzeBox.getLocation().y);
		richtingKeuzeBox.setLocation(indent, richtingKeuzeBox.getLocation().y);
		dikteLabel.setLocation(indent, dikteLabel.getLocation().y);
		dikteField.setLocation(indent + width / 2 + offset, dikteField.getLocation().y);
		kleurKnop.setLocation(indent, kleurKnop.getLocation().y);
		vulHoogteCB.setLocation(indent, vulHoogteCB.getLocation().y);
	}
	
	public void zetRichtingKeuzes(int symboolKeuze)
	{
		String[] richtingString = {};
		//in plaats van teksten zouden plaatjes mooier zijn. 
		if(symboolKeuze == Symbool.GEEN)
		{	richtingString = new String[1];
			richtingString[0] = WiskOpdr.rb.getString("SYM_kiesEerstSymbool");
		}
		else if(symboolKeuze == Symbool.LIJN)
		{	richtingString = new String[4];
			richtingString[0] = WiskOpdr.rb.getString("SYM_horizontaal");
			richtingString[1] = WiskOpdr.rb.getString("SYM_verticaal");
			richtingString[2] = WiskOpdr.rb.getString("SYM_diagonaalOmhoog");
			richtingString[3] = WiskOpdr.rb.getString("SYM_diagonaalOmlaag");
		}
		else if(symboolKeuze == Symbool.PIJL)
		{
			richtingString = new String[8];
			richtingString[0] = WiskOpdr.rb.getString("SYM_links");
			richtingString[1] = WiskOpdr.rb.getString("SYM_rechts");
			richtingString[2] = WiskOpdr.rb.getString("SYM_boven");
			richtingString[3] = WiskOpdr.rb.getString("SYM_beneden");
			richtingString[4] = WiskOpdr.rb.getString("SYM_rechtsboven");
			richtingString[5] = WiskOpdr.rb.getString("SYM_rechtsonder");
			richtingString[6] = WiskOpdr.rb.getString("SYM_linksonder");
			richtingString[7] = WiskOpdr.rb.getString("SYM_linksboven");
		}
		else if(symboolKeuze == Symbool.ACCOLADE)
		{
			richtingString = new String[4];
			richtingString[0] = WiskOpdr.rb.getString("SYM_links");
			richtingString[1] = WiskOpdr.rb.getString("SYM_rechts");
			richtingString[2] = WiskOpdr.rb.getString("SYM_boven");
			richtingString[3] = WiskOpdr.rb.getString("SYM_beneden");
		}
		else if(symboolKeuze == Symbool.ELLIPS)
		{
			richtingString = new String[1];
			richtingString[0] = WiskOpdr.rb.getString("SYM_geenRichting");
			//nog kijken wat ik hiermee doe. Schuine ellipsen mogelijk maken? 
			//anders is richting kiezen hier vrij zinloos. 
		}
		else if(symboolKeuze == Symbool.HAAK)
		{
			richtingString = new String[2];
			richtingString[0] = WiskOpdr.rb.getString("SYM_links");
			richtingString[1] = WiskOpdr.rb.getString("SYM_rechts");
		}
		//ook nog optie bouwen voor als geen symboolkeuze?
		//nog testen of onderstaande allemaal nodig. richtingkeuzebox opnieuw initialiseren.
		int x = richtingKeuzeBox.getLocation().x;
		int y = richtingKeuzeBox.getLocation().y;
		remove(richtingKeuzeBox);
		richtingKeuzeBox = new JComboBox(richtingString);
		richtingKeuzeBox.setBounds(x, y, 200, height);
		richtingKeuzeBox.setFont(theFont);
		add(richtingKeuzeBox);
		richtingKeuzeBox.addActionListener(this);
		this.revalidate(); //om te zorgen dat pijltje in richting-combobox verschijnt.
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == symboolKeuzeBox)
		{
			symboolPanel.zetRichtingKeuze(0);
			int keuze = symboolKeuzeBox.getSelectedIndex();
			symboolPanel.zetSymboolKeuze(keuze);
			zetRichtingKeuzes(keuze);
		}
		else if(e.getSource() == richtingKeuzeBox)
		{
			symboolPanel.zetRichtingKeuze(richtingKeuzeBox.getSelectedIndex());
		}
		else if(e.getSource() == dikteField)
		{
			symboolPanel.zetDikte(Integer.parseInt(dikteField.getText()));
		}
		else if(e.getSource() == kleurKnop)
		{
			Color kleur = JColorChooser.showDialog(this, WiskOpdr.rb.getString("SYM_kiesKleur"), symboolPanel.getKleur());
			if(kleur != null)
				symboolPanel.zetKleur(kleur);
		}
		else if(e.getSource() == vulHoogteCB)
		{
			symboolPanel.zetVulHoogte(vulHoogteCB.isSelected());
		}
	}

	@Override
	public void focusGained(FocusEvent arg0) {
		
	}

	@Override
	public void focusLost(FocusEvent e) {
		if(e.getSource() == dikteField)
		{
			symboolPanel.zetDikte(Integer.parseInt(dikteField.getText()));
		}
		
	}

	@Override
	public void setEditState(Hashtable h) {
		int dikte = 1;
		int richting = 0;
		int type = 0;
		boolean vulHoogte = false;
		
		if(h.containsKey("dikte"))
			dikte = ((Integer) h.get("dikte")).intValue();
		if(h.containsKey("richting"))
			richting = ((Integer) h.get("richting")).intValue();
		if(h.containsKey("type"))
			type = ((Integer) h.get("type")).intValue();
		if(h.containsKey("vulHoogte"))
			vulHoogte = ((Boolean) h.get("vulHoogte")).booleanValue();
		
		symboolKeuzeBox.setSelectedIndex(type);
		if(type == Symbool.GEEN)
			richting = 0;
		else if(type == Symbool.LIJN)
		{	if(richting < 2)
				richting = 0;
			else if(richting < 4)
				richting = 1;
			else if(richting < 6)
				richting = 2;
			else
				richting = 3;
		}
		
//		else if(type == Symbool.ELLIPS)
//		{
//			//nog kijken wat ik hiermee doe. Schuine ellipsen mogelijk maken? 
//			//anders is richting kiezen hier vrij zinloos. 
//		}
		
		richtingKeuzeBox.setSelectedIndex(richting);
		dikteField.setText("" + dikte);
		vulHoogteCB.setSelected(vulHoogte);
		symboolPanel.zetOpdracht(h, null, null);
	}

	@Override
	public Hashtable getEditState() {
		
		
		Hashtable h = symboolPanel.getEditState();
		
		return h;
	}

	@Override
	public void zetBreedte(int b) {
		symboolBreedte = b;
		symboolPanel.zetBreedte(b);
		setBounds(getLocation().x, getLocation().y, symboolBreedte + width + 3 * offset, Math.max(symboolHoogte, editHeight));		
		plaatsComponenten();
		
	}

	@Override
	public void zetHoogte(int h) {
		symboolHoogte = h;
		symboolPanel.zetHoogte(h);
		setBounds(getLocation().x, getLocation().y, symboolBreedte + width + 3 * offset, Math.max(symboolHoogte, editHeight));		
		
	}

	@Override
	public void stop() {
		
	}

	@Override
	public void start() {
		
	}
	
	
}
