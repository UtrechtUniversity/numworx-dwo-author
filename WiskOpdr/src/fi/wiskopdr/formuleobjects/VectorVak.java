package fi.wiskopdr.formuleobjects;

import java.awt.*;
import java.util.Iterator;
import java.util.Vector;

public class VectorVak extends RegelVak
{
	public static final String NOTATIE = "$Y";
  Vector<FormuleRegel> kinderen;

	public VectorVak(FormuleVak fv)
	{
		formuleVak = fv;
		kinderen = new Vector<FormuleRegel>();
		setLayout(null);

		super.setFont(fv.getFont());
		fm = getFontMetrics(getFont());

		kind1 = new FormuleRegel(formuleVak);
		kind1.setLocation(10, 5);// iets met fm.getAscent en fm.getDescent
		kinderen.add(kind1);
		add(kind1);
		// default 2 kinderen. In vulVak() worden deze weeggehaald.
		maakNieuwKind();
		maakMaat();

		setOpaque(false);
	}

	public void setFont(Font f)
	{
		super.setFont(f);
		fm = getFontMetrics(getFont());
		
		if (kinderen == null || kinderen.get(0) == null)
			return;
		
		for (int i = 0; i < kinderen.size(); i++)
			kinderen.get(i).setFont(f);

		maakMaat();
	}

	/**
	 * De foreground van de kinderen zetten. Werkt voor
	 * vector anders dan de standaard kind1 t/m kind4 van 
	 * RegelVak.setFGColor(), dus hier implementeren.
	 */
	public void setFGColor(Color c)
	{
		fgColor = c;

		kind1.setFGColor(c); // kind1 is er altijd

		if (kinderen == null || kinderen.get(0) == null)
			return;
		
		for (int i = 0; i < kinderen.size(); i++)
			kinderen.get(i).setFGColor(c);
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
		for (int i = 0; i < kinderen.size(); i++)
		{
			height += kinderen.get(i).getSize().height + 5;
			width = Math.max(width, 10 + kinderen.get(i).getSize().width);
		}
		
		// extra breedte voor afsluitende haak
		width = width + 10;

		setSize(width, height);
		ashoogte = height / 2 - fm.getDescent();
//		System.out.println("VectorVak.maakMaat(): setSize(" + width + ", " + height + "), ashoogte = " + ashoogte);

		int kindHoogte = 5;
		for (int i = 0; i < kinderen.size(); i++)
		{
			kinderen.get(i).setLocation((int) (0.5 * width - 0.5 * kinderen.get(i).getSize().width + 2), kindHoogte);
			kindHoogte += kinderen.get(i).getSize().height + 5;
		}
	}

	public int bepaalKindMetFocus()
	{
		int kindMetFocus = 0;
		for (int i = 0; i < kinderen.size(); i++)
		{
			if (kinderen.get(i).hasFocus())
			{
				kindMetFocus = i;
				break;
			}
		}
		return kindMetFocus;
	}

	public void focusKindOmhoog()
	{
		int kindMetFocus = bepaalKindMetFocus();
		if (kindMetFocus > 0)
			kinderen.get(kindMetFocus - 1).neemFocus("rechts");
	}

	public void focusKindOmlaag()
	{
		int kindMetFocus = bepaalKindMetFocus();
		if (kindMetFocus == kinderen.size() - 1)
		{
			maakNieuwKind();
		}
		kinderen.get(kindMetFocus + 1).neemFocus("rechts");
		zetMaat();
		repaint();
	}

	public void maakNieuwKind()
	{
		FormuleRegel kind = new FormuleRegel(formuleVak);
		kind.setFont(getFont());
		kinderen.add(kind);
		add(kind);
	}

	public void deleteKind()
	{
		int kindMetFocus = bepaalKindMetFocus();
		FormuleRegel kind = kinderen.get(kindMetFocus);
		// testen of kind leeg is.
		// Als het kind leeg is: kind verwijderen.
		if (kind.toString().length() > 0)
			return;
		// laatste kind niet verwijderen
		if (kinderen.size() == 1)
			return;
		
		remove(kind);
		kinderen.remove(kindMetFocus);
		if (kindMetFocus < kinderen.size())
			kinderen.get(kindMetFocus).neemFocus("rechts");
		else
			kinderen.get(kindMetFocus - 1).neemFocus("rechts");
		zetMaat();
		repaint();
	}

	public void zetMaat()
	{
		maakMaat();
		if (getParent() instanceof FormuleElement)
			((FormuleElement) getParent()).zetMaat();
	}

	public void setEditable(boolean b)
	{
		for (int i = 0; i < kinderen.size(); i++)
			kinderen.get(i).setEditable(b);
	}

	public void vulVak(String s)
	{
		// hier komt altijd een string in waarin de elementen zijn gescheiden
		// door $n.
		
		// verwijder eerst de default kinderen
		removeKinderen();
		
		kinderen = new Vector<FormuleRegel>();

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
					maakNieuwKind();
					kinderen.get(kinderen.size() - 1).insert(s.substring(2, eind));
					s = s.substring(eind);
				}
			}
		}
		for (int i = 0; i < kinderen.size(); i++)
		{
			kinderen.get(i).zetMaat();
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
	    	FormuleRegel kind = (FormuleRegel) i.next();
	    	kind.removeAll();
	    	remove(kind);
	    	//kinderen.remove(kind);
	    }
	}

	public String toString()
	{
		String string = NOTATIE;
		if (kinderen.size() > 0)
		{
			for (int i = 0; i < kinderen.size(); i++)
				string = string + "$n" + kinderen.get(i).toString() + "@";
		}
		
		string = string + "@";

//		System.out.println("VectorVak.toString(): " + string);
		
		return string;
	}
}
