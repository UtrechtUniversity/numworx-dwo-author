package fi.wiskopdr.formuleobjects;

import java.awt.*;
import java.util.Iterator;
import java.util.Vector;

public class MatrixVak extends RegelVak
{
	Vector<Vector<FormuleRegel>> kinderen;
	int aantalRijen;
	int aantalKolommen;

	public MatrixVak(FormuleVak fv)
	{
		formuleVak = fv;
		kinderen = new Vector<Vector<FormuleRegel>>();
		setLayout(null);

		super.setFont(fv.getFont());
		fm = getFontMetrics(getFont());

		// default 3x3 kinderen. In vulVak() worden deze weeggehaald.
		maakDefaultMatrix();
		maakMaat();
		kind1 = kinderen.get(0).get(0); // kind 1 moet niet null zijn... consistent met andere vakken

		setOpaque(false);
	}

	public void setFont(Font f)
	{
		super.setFont(f);
		fm = getFontMetrics(getFont());
		
		if (kinderen == null || kinderen.size() == 0)
			return;
		
		for (int i = 0; i < kinderen.size(); i++) // rijen
		{
			for (int j = 0; j < kinderen.get(0).size(); j++) // kolommen
			{
				kinderen.get(i).get(j).setFont(f);				
			}
		}

		maakMaat();
	}

	/**
	 * De foreground van de kinderen zetten. Werkt voor
	 * matrix anders dan de standaard kind1 t/m kind4 van 
	 * RegelVak.setFGColor(), dus hier implementeren.
	 */
	public void setFGColor(Color c)
	{
		fgColor = c;

		kind1.setFGColor(c); // kind1 is er altijd

		if (kinderen == null || kinderen.get(0) == null)
			return;
		
		for (int i = 0; i < kinderen.size(); i++) // rijen
		{
			for (int j = 0; j < aantalKolommen; j++) // kolommen
			{
				kinderen.get(i).get(j).setFGColor(c);
			}
		}
	}

	public void paint(Graphics g)
	{
		maakMaat();
		((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		((Graphics2D) g).setStroke(new BasicStroke(1.2f));

		if (selected)
		{
			g.setColor(Color.black);
			g.fillRect(0, 0, getSize().width, getSize().height);
		}
		if (selected)
			g.setColor(Color.white);
		else
			g.setColor(fgColor);

		// haak ervoor
		g.drawArc(5, 1, 10, 10, 90, 90); // eerste bochtje
		g.drawLine(5, 6, 5, getSize().height - 6); // 1 lange lijn
		g.drawArc(5, getSize().height - 12, 10, 10, 180, 90); // laatste bochtje 
		
		// haak erna
		g.drawArc(getSize().width - 12, 1, 10, 10, 90, -90); // eerste bochtje
		g.drawLine(getSize().width - 2, 6, getSize().width - 2, getSize().height - 6); // 1 lange lijn
		g.drawArc(getSize().width - 12, getSize().height - 12, 10, 10, 0, -90); // laatste bochtje 


		((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
		((Graphics2D) g).setStroke(new BasicStroke(0.7f));

		super.paint(g);
	}

	private void maakMaat()
	{
		super.zetMaat();

		height = 5;
		width = 10;
		int[] maxKolomBreedte = new int[aantalKolommen];
		int[] maxRijAshoogte = new int[aantalRijen];
		int[] maxRijHoogte = new int[aantalRijen];
		int rijBreedte = 0;
		
		for (int i = 0; i < kinderen.size(); i++) // rijen
		{
			int rijHoogte = 0;
			
			for (int j = 0; j < kinderen.get(i).size(); j++) // kolommen
			{
				rijHoogte = Math.max(rijHoogte, kinderen.get(i).get(j).getSize().height + 5);
				maxRijAshoogte[i] = Math.max(maxRijAshoogte[i], kinderen.get(i).get(j).ashoogte);
				maxRijHoogte[i] = Math.max(maxRijHoogte[i], kinderen.get(i).get(j).getSize().height);
				maxKolomBreedte[j] = Math.max(maxKolomBreedte[j], kinderen.get(i).get(j).getSize().width + 10);
			}

			height += rijHoogte;
//			System.out.println("MatrixVak.maakMaat(): i = " + i + ", rijBreedte = " + rijBreedte + ", width = " + width);
		}

		for (int j = 0; j < aantalKolommen; j++) // kolommen
		{
			rijBreedte += maxKolomBreedte[j];
		}

//		System.out.println("MatrixVak.maakMaat(): rijBreedte = " + rijBreedte);
		width = rijBreedte;

		int[] xPosities = new int[aantalKolommen];
		for (int i = 0; i < aantalKolommen; i++)
		{
			if (i == 0)
				xPosities[i] = 10;
			else
				xPosities[i] = xPosities[i - 1] + maxKolomBreedte[i - 1]; 
		}
		
		// extra breedte voor afsluitende haak
		width = width + 15;

		setSize(width, height);
//		System.out.println("MatrixVak.maakMaat(): na setSize(width, height): width = " + width + ", getSize().width = " + getSize().width);

		ashoogte = height / 2 - fm.getDescent();
		
//		System.out.println("MatrixVak.maakMaat(): setSize(" + width + ", " + height + "), ashoogte = " + ashoogte);

		int kindY = 5;
		for (int i = 0; i < kinderen.size(); i++) // rijen
		{
			for (int j = 0; j < kinderen.get(i).size(); j++) // kolommen
			{
				int x = (int) (xPosities[j] + 0.5 * maxKolomBreedte[j] - 0.5 * kinderen.get(i).get(j).getSize().width);
				int y = kindY + (maxRijAshoogte[i] - kinderen.get(i).get(j).ashoogte);

				kinderen.get(i).get(j).setLocation(x, y);
			}

			kindY += maxRijHoogte[i] + 5;
		}
	}

	public int[] bepaalKindMetFocus()
	{
		int[] kindMetFocus = {0, 0};
		
		for (int i = 0; i < kinderen.size(); i++) // rijen
		{
			for (int j = 0; j < kinderen.get(i).size(); j++) // kolommen
			{
				if (kinderen.get(i).get(j).hasFocus())
				{
					kindMetFocus[0] = i;
					kindMetFocus[1] = j;
					break;
				}
			}
		}
		return kindMetFocus;
	}

	public void focusKindOmhoog()
	{
		int[] kindMetFocus = bepaalKindMetFocus();
		if (kindMetFocus[0] > 0)
			kinderen.get(kindMetFocus[0] - 1).get(kindMetFocus[1]).neemFocus("rechts");
	}

	public void focusKindOmlaag()
	{
		int[] kindMetFocus = bepaalKindMetFocus();
		if (kindMetFocus[0] == kinderen.size() - 1) // onderste rij
		{
			maakNieuweRij();
		}
		kinderen.get(kindMetFocus[0] + 1).get(kindMetFocus[1]).neemFocus("rechts");
	}

	public void focusKindLinks()
	{
		int[] kindMetFocus = bepaalKindMetFocus();
		if (kindMetFocus[1] > 0)
			kinderen.get(kindMetFocus[0]).get(kindMetFocus[1] - 1).neemFocus("rechts");
	}

	public void focusKindRechts()
	{
		int[] kindMetFocus = bepaalKindMetFocus();
		if (kindMetFocus[1] == kinderen.get(kindMetFocus[0]).size() - 1) // laatste kolom
		{
			maakNieuweKolom();
			zetMaat();
			repaint();
		}
			kinderen.get(kindMetFocus[0]).get(kindMetFocus[1] + 1).neemFocus("rechts");
	}

	/**
	 * Maak default 3x3 matrix.
	 */
	public void maakDefaultMatrix()
	{
		aantalKolommen = 3;
		
		for (int rij = 0; rij < 3; rij++)
		{
			maakNieuweRij();
		}
	}

	/**
	 * I.p.v. maakNieuwKind()
	 */
	public void maakNieuweRij()
	{
		Vector<FormuleRegel> rij = new Vector<FormuleRegel>();
		
		for (int kolom = 0; kolom < aantalKolommen; kolom++)
		{
			FormuleRegel kind = new FormuleRegel(formuleVak);
			kind.setFont(getFont());
			rij.add(kind);
			add(kind);
		}
		kinderen.add(rij);
		aantalRijen++;

		zetMaat();
		repaint();
	}
	
	/**
	 * Gebruikt door vulVak(). Maar een nieuwe rij met de gegeven string.
	 * 
	 * @param s
	 */
	private void maakNieuweRij(String s)
	{
		Vector<FormuleRegel> rij = new Vector<FormuleRegel>();
		
		while (s.length() > 0)
		{
			char ch0 = s.charAt(0);
			if (ch0 == '@')
			{
				break;
			}
			else if (ch0 == '$')
			{
				int niv = 1;
				int eind = 0;
				String sz = s.substring(2);
				while (niv > 0)
				{
					int eindB = sz.indexOf("$");
					int eindE = sz.indexOf("@");
					if (eindB < eindE && eindB != -1)
					{
						eind = eindB;
						niv++;
					}
					else
					{
						eind = eindE;
						niv--;
					}
					sz = sz.substring(eind + 1);
				}
				eind = s.length() - sz.length();
				char ch1 = s.charAt(1);
				if (ch1 == 'k')
				{
					FormuleRegel kind = new FormuleRegel(formuleVak);
					kind.setFont(getFont());
					kind.insert(s.substring(2, eind));
					rij.add(kind);
					add(kind);
					s = s.substring(eind);
				}
			}
		}
		
		kinderen.add(rij);
		aantalRijen++;
		aantalKolommen = rij.size();
	}

	/**
	 * I.p.v. maakNieuwKind()
	 */
	public void maakNieuweKolom()
	{
		for (int rij = 0; rij < kinderen.size(); rij++)
		{
			FormuleRegel kind = new FormuleRegel(formuleVak);
			kind.setFont(getFont());
			kinderen.get(rij).addElement(kind);
			add(kind);
		}
		aantalKolommen++;
		
		zetMaat();
		repaint();
	}

	/**
	 * Verwijder de rij die focus heeft en 
	 * verplaats de focus.
	 * 
	 */
	public void deleteRij()
	{
		int[] kindMetFocus = bepaalKindMetFocus();

		for (int j = 0; j < aantalKolommen; j++)
		{
			FormuleRegel kind = (FormuleRegel) kinderen.get(kindMetFocus[0]).get(j);
			kind.removeAll();
			remove(kind);
		}
		
    	kinderen.remove(kindMetFocus[0]);
		aantalRijen--;
		
		// verplaats focus naar boven
		if (kindMetFocus[0] > 0)
			kinderen.get(kindMetFocus[0] - 1).get(kindMetFocus[1]).neemFocus("rechts");
		else
			kinderen.get(0).get(kindMetFocus[1]).neemFocus("rechts");
		
		zetMaat();
		repaint();
	}
	
	/**
	 * Verwijder de rij met de gegeven index en 
	 * verplaats de focus.
	 * 
	 * @param rijIndex
	 */
	public void deleteRij(int rijIndex)
	{
		int[] kindMetFocus = bepaalKindMetFocus();

		for (int j = 0; j < aantalKolommen; j++)
		{
			FormuleRegel kind = (FormuleRegel) kinderen.get(rijIndex).get(j);
			kind.removeAll();
			remove(kind);
		}
		
    	kinderen.remove(rijIndex);
		
		aantalRijen--;
		
		// verplaats focus naar boven
		if (kindMetFocus[0] > 0)
			kinderen.get(kindMetFocus[0] - 1).get(kindMetFocus[1]).neemFocus("rechts");
		else
			kinderen.get(0).get(kindMetFocus[1]).neemFocus("rechts");
		
		zetMaat();
		repaint();
	}
	
	/**
	 * Verwijder de kolom die focus heeft en 
	 * verplaats de focus.
	 *  
	 */
	public void deleteKolom()
	{
		int[] kindMetFocus = bepaalKindMetFocus();

		for (int i = 0; i < aantalRijen; i++)
		{
			FormuleRegel kind = (FormuleRegel) kinderen.get(i).get(kindMetFocus[1]);
			kind.removeAll();
			remove(kind);
			kinderen.get(i).remove(kindMetFocus[1]);
		}
		
		aantalKolommen--;
		
		// verplaats focus naar links
		if (kindMetFocus[1] > 0)
			kinderen.get(kindMetFocus[0]).get(kindMetFocus[1] - 1).neemFocus("rechts");
		else
			kinderen.get(kindMetFocus[0]).get(0).neemFocus("rechts");
		
		zetMaat();
		repaint();
	}
	
	/**
	 * Verwijder de kolom met de gegeven index en 
	 * verplaats de focus.
	 *  
	 * @param kolomIndex
	 */
	public void deleteKolom(int kolomIndex)
	{
		int[] kindMetFocus = bepaalKindMetFocus();

		for (int i = 0; i < aantalRijen; i++)
		{
			FormuleRegel kind = (FormuleRegel) kinderen.get(i).get(kolomIndex);
			kind.removeAll();
			remove(kind);
			kinderen.get(i).remove(kolomIndex);
		}
		
		aantalKolommen--;
		
		// verplaats focus naar links
		if (kindMetFocus[1] > 0)
			kinderen.get(kindMetFocus[0]).get(kindMetFocus[1] - 1).neemFocus("rechts");
		else
			kinderen.get(kindMetFocus[0]).get(0).neemFocus("rechts");
		
		zetMaat();
		repaint();
	}
	
	/**
	 * Wordt aangeroepen door FormuleRegel.backspace(). 
	 * Als focus in laatste kolom en alle kinderen in die 
	 * kolom leeg, dan wordt de laatste kolom verwijderd.
	 * Als focus in eerste kolom van laatste rij, dan
	 * wordt de laatste rij verwijderd.  
	 * 
	 * Een matrix moet minimaal 2 rijen en 2 kolommen hebben.
	 */
	public void delete()
	{
		if (isFocusInLaatsteKolom() && isLegeKolom(aantalKolommen - 1) && aantalKolommen > 2)
			deleteKolom(aantalKolommen - 1);
		else if (isFocusInEersteKolomLaatsteRij() && isLegeRij(aantalRijen - 1) && aantalRijen > 2)
			deleteRij(aantalRijen - 1);
	}

	/**
	 * Retourneert true als alle kinderen in de kolom met de gegeven index
	 * leeg zijn, anders false.
	 *  
	 * @param kolomIndex
	 * @return
	 */
	private boolean isLegeKolom(int kolomIndex)
	{
		boolean isLeeg = true;

		for (int i = 0; i < kinderen.size(); i++) // rijen
		{
			FormuleRegel kind = kinderen.get(i).get(kolomIndex);
			if (kind.toString().length() > 0)
			{
				isLeeg = false;
				break;
			}
		}
		
		return isLeeg;
	}

	/**
	 * Retourneert true als alle kinderen in de rij met de gegeven index
	 * leeg zijn, anders false.
	 *  
	 * @param rijIndex
	 * @return
	 */
	private boolean isLegeRij(int rijIndex)
	{
		boolean isLeeg = true;

		for (int i = 0; i < aantalKolommen; i++) // loop door de kolommen
		{
			FormuleRegel kind = kinderen.get(rijIndex).get(i);
			if (kind.toString().length() > 0)
			{
				isLeeg = false;
				break;
			}
		}
		
		return isLeeg;
	}

	/**
	 * 
	 * @return
	 */
	private boolean isFocusInLaatsteKolom()
	{
		boolean b = false;
		
		if (bepaalKindMetFocus()[1] == aantalKolommen - 1)
			b = true;
		
		return b;
	}

	/**
	 * 
	 * @return
	 */
	private boolean isFocusInEersteKolomLaatsteRij()
	{
		boolean b = false;
		
		if (bepaalKindMetFocus()[1] == 0 && bepaalKindMetFocus()[0] == aantalRijen - 1)
			b = true;
		
		return b;
	}

	public void zetMaat()
	{
		maakMaat();
		if (getParent() instanceof FormuleElement)
			((FormuleElement) getParent()).zetMaat();
	}

	public void setEditable(boolean b)
	{
		for (int i = 0; i < aantalRijen; i++)
		{
			for (int j = 0; j < aantalKolommen; j++)
			{
				kinderen.get(i).get(j).setEditable(b);
			}
		}
	}

	public void vulVak(String s)
	{
		// hier komt altijd een string in waarin de rijen zijn gescheiden
		// door $n en daarbinnen de kolommen door $k.
		
		// verwijder eerst de default kinderen
		removeKinderen();
		
		kinderen = new Vector<Vector<FormuleRegel>>();

		while (s.length() > 0)
		{
			char ch0 = s.charAt(0);
			if (ch0 == '@')
			{
				break;
			}
			else if (ch0 == '$')
			{
				int niv = 1;
				int eind = 0;
				String sz = s.substring(2);
				while (niv > 0)
				{
					int eindB = sz.indexOf("$");
					int eindE = sz.indexOf("@");
					if (eindB < eindE && eindB != -1)
					{
						eind = eindB;
						niv++;
					}
					else
					{
						eind = eindE;
						niv--;
					}
					sz = sz.substring(eind + 1);
				}
				eind = s.length() - sz.length();
				char ch1 = s.charAt(1);
				if (ch1 == 'n')
				{
					maakNieuweRij(s.substring(2, eind));
					s = s.substring(eind);
				}
			}
		}
		
		for (int i = 0; i < kinderen.size(); i++)
		{
			for (int j = 0; j < aantalKolommen; j++)
			{
				kinderen.get(i).get(j).zetMaat();
			}
		}
	}

	/**
	 * Verwijder alle kinderen.
	 */
	private void removeKinderen()
	{
		Iterator i = kinderen.iterator();
		
	    while (i.hasNext())
	    {
	    	Iterator i2 = ((Vector) i.next()).iterator();
	    	while (i2.hasNext())
	    	{
		    	FormuleRegel kind = (FormuleRegel) i2.next();
		    	kind.removeAll();
		    	remove(kind);
	    	}
	    }
	    
	    aantalRijen = 0;
	    aantalKolommen = 0;
	}

	public String toString()
	{
		String string = "$M";
		if (kinderen.size() > 0)
		{
			for (int i = 0; i < kinderen.size(); i++) // rijen
			{
				string = string + "$n"; // begin rij
				for (int j = 0; j < aantalKolommen; j++)
				{
					string = string + "$k" + kinderen.get(i).get(j).toString() + "@";
				}
				string = string + "@"; // eind rij
			}
		}
		
		string = string + "@";

		//System.out.println("MatrixVak.toString(): " + string);
		
		return string;
	}
	
	public void neemFocus(String richting)
	{
		if (kinderen != null && !kinderen.isEmpty())
			kinderen.get(0).get(0).neemFocus(richting);
	}
}
