package fi.heks;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;

import javax.swing.*;

import fi.beans.wiskopdrbeans.*;

public class HeksInteractieEditPanel extends JPanel implements InteractieEditPanel,
																	ActionListener	
{	
	
	protected static Color bgColor = new Color(238, 238, 238);
	
	int editWidth = 190;
	int editHeight = 450; 
	int heipBreedte = 500; // startbreedte spip
	int heipHoogte = 450; // starthoogte spip
	
	Font theFont;
	FontMetrics theFM;
	Font theBoldFont;
	FontMetrics theBoldFM;
	
	int offset = 10;
	boolean componentsCreated = false;
	
	boolean noSetBounds = false;	

	protected HeksInteractiePanel heip;

	int scoreMax = 0;
	
	ButtonGroup paginaGroep;
	JRadioButton pagina21Button, pagina22Button, pagina23Button, pagina24Button;
	ButtonGroup emmerGroep;
	JRadioButton keuzeButton, erinButton, eruitButton;
	
	int paginaNummer;
	
	public HeksInteractieEditPanel()
	{
		setLayout(null);
		setBackground(getBackground());
		//setBackground(bgColor);
System.out.println("constr heeip bg = " + getBackground().toString());		
		setOpaque(true);

//System.out.println("kliep " + getBackground().toString());

		heip = new HeksInteractiePanel();
		add(heip);
		
		theFont = new Font("Dialog", Font.PLAIN, 12);
		theFM = getFontMetrics(theFont);
		theBoldFont = new Font("Dialog", Font.BOLD, 12);
		theBoldFM = getFontMetrics(theBoldFont);
		
		
		int width = editWidth - 2 * offset;
		int height = 3 * theFM.getHeight() / 2;
		int currentX = heip.getSize().width + offset;
		int currentX2 = offset;
		int currentY = offset;
	
		paginaGroep = new ButtonGroup();
		
		pagina21Button = new JRadioButton(Heks.rb.getString("pagina21"), true);
		pagina21Button.setFont(theFont);
		//pagina21Button.setBackground(Color.white);
		pagina21Button.setOpaque(false);
		pagina21Button.setBounds(currentX, currentY, width, height);
		add(pagina21Button);
		pagina21Button.addActionListener(this);
		paginaGroep.add(pagina21Button);
		
		currentY += height + offset / 3;		
		
		pagina22Button = new JRadioButton(Heks.rb.getString("pagina22"), false);
		pagina22Button.setFont(theFont);
		//pagina22Button.setBackground(Color.white);
		pagina22Button.setOpaque(false);
		pagina22Button.setBounds(currentX, currentY, width, height);
		add(pagina22Button);
		pagina22Button.addActionListener(this);
		paginaGroep.add(pagina22Button);
		
		currentY += height + offset / 3;		
		
		pagina23Button = new JRadioButton(Heks.rb.getString("pagina23"), false);
		pagina23Button.setFont(theFont);
		//pagina23Button.setBackground(getBackground());
		pagina23Button.setOpaque(false);
		pagina23Button.setBounds(currentX, currentY, width, height);
		add(pagina23Button);
		pagina23Button.addActionListener(this);
		paginaGroep.add(pagina23Button);
		
		currentY += height + offset / 3;
		
		emmerGroep = new ButtonGroup();
		
		keuzeButton = new JRadioButton(Heks.rb.getString("keuzeerineruit"), true);
		keuzeButton.setFont(theFont);
		//keuzeButton.setBackground(getBackground());
		keuzeButton.setOpaque(false);
		keuzeButton.setBounds(currentX+30, currentY, width-30, height);
		add(keuzeButton);
		keuzeButton.addActionListener(this);
		emmerGroep.add(keuzeButton);
		
		currentY += height + offset / 3;
		
		erinButton = new JRadioButton(Heks.rb.getString("alleenerin"), true);
		erinButton.setFont(theFont);
		//erinButton.setBackground(getBackground());
		erinButton.setOpaque(false);
		erinButton.setBounds(currentX+30, currentY, width-30, height);
		add(erinButton);
		erinButton.addActionListener(this);
		emmerGroep.add(erinButton);
		
		currentY += height + offset / 3;
		
		eruitButton = new JRadioButton(Heks.rb.getString("alleeneruit"), true);
		eruitButton.setFont(theFont);
		//eruitButton.setBackground(getBackground());
		eruitButton.setOpaque(false);
		eruitButton.setBounds(currentX+30, currentY, width-30, height);
		add(eruitButton);
		eruitButton.addActionListener(this);
		emmerGroep.add(eruitButton);
		
		currentY += height + offset / 3;
		
		enableKeuze(false);
		
		pagina24Button = new JRadioButton(Heks.rb.getString("pagina24"), false);
		pagina24Button.setFont(theFont);
		//pagina24Button.setBackground(getBackground());
		pagina24Button.setOpaque(false);
		pagina24Button.setBounds(currentX, currentY, width, height);
		add(pagina24Button);
		pagina24Button.addActionListener(this);
		paginaGroep.add(pagina24Button);
		
		currentY += height + offset / 3;
		
		
		
		componentsCreated = true;
	}	
	
	public void plaatsComponenten()
	{
		if (componentsCreated)
		{
			pagina21Button.setLocation(heip.getSize().width + offset, pagina21Button.getLocation().y);
			pagina22Button.setLocation(heip.getSize().width + offset, pagina22Button.getLocation().y);
			pagina23Button.setLocation(heip.getSize().width + offset, pagina23Button.getLocation().y);
			pagina24Button.setLocation(heip.getSize().width + offset, pagina24Button.getLocation().y);
			keuzeButton.setLocation(heip.getSize().width + offset + 30, keuzeButton.getLocation().y);
			erinButton.setLocation(heip.getSize().width + offset + 30, erinButton.getLocation().y);
			eruitButton.setLocation(heip.getSize().width + offset + 30, eruitButton.getLocation().y);
		}
	}
	
	public void enableKeuze(boolean b)
	{
		keuzeButton.setEnabled(b);
		erinButton.setEnabled(b);
		eruitButton.setEnabled(b);
	}
	
/*	
	public void zetPagina(int nummer)
	{
		paginaNummer = nummer;
		// hier wisselen
		heip.zetPagina(nummer);
		
	}
*/
	public void setEditState(Hashtable b)
	{
		
//System.out.println("heiep setEditState");

		int paginaNummer = 1;
		if (b.containsKey("paginanummer"))
			paginaNummer = ((Integer) b.get("paginanummer")).intValue();
		boolean alleenErin = false;
		if (b.containsKey("alleenerin"))
			alleenErin = ((Boolean) b.get("alleenerin")).booleanValue();
		boolean alleenEruit = false;
		if (b.containsKey("alleeneruit"))
			alleenEruit = ((Boolean) b.get("alleeneruit")).booleanValue();

		if (paginaNummer == 1)
		{	//zetPagina(1);
			pagina21Button.setSelected(true);
		}
		else if (paginaNummer == 2)
		{	//zetPagina(2);
			pagina22Button.setSelected(true);
		}
		else if (paginaNummer == 3)
		{	//zetPagina(3);
			pagina23Button.setSelected(true);
			enableKeuze(true);
			if (alleenErin)
				erinButton.setSelected(true);
			else if (alleenEruit)
				eruitButton.setSelected(true);	
		}
		else if (paginaNummer == 4)
		{	//zetPagina(4);
			pagina24Button.setSelected(true);
		}
		
		if (b.containsKey("heipBreedte"))
			heipBreedte = ((Integer) b.get("heipBreedte")).intValue();
		if (b.containsKey("heipHoogte"))
			heipHoogte = ((Integer) b.get("heipHoogte")).intValue();
		
		setBounds(getLocation().x, getLocation().y, heipBreedte + editWidth, Math.max(heipHoogte, editHeight));
		
		// HIER !!
		heip.setEditState(b);		
		
	}
	
	public Hashtable getEditState()
	{
System.out.println("heiep getEditState");

		Hashtable h = heip.getEditState();
		
		h.put("scoreMax", new Integer(scoreMax));
		
		h.put("heipBreedte", new Integer(heipBreedte));
		h.put("heipHoogte", new Integer(heipHoogte));
		
		return h;
		
	}
		
	public void setBounds(int x, int y, int b, int h)
	{
		if (noSetBounds)
		{	noSetBounds = false;
			return;
		}
//System.out.println("spiep setBounds raw " + x + " " + y + " " + b + " " + h);

		if ((h <= 1) || (x < 0) || (b <= 1))
			return;
		
		super.setBounds(x, y, heipBreedte + editWidth, Math.max(heipHoogte, editHeight));
		
//System.out.println("spiep setBounds " + x + " " + y + " " + (spipBreedte + editWidth) + " " + 
//					Math.max(spipHoogte, editHeight));
	
		if (heip != null)
			heip.setBounds(0, 0, heipBreedte, heipHoogte);
		
		plaatsComponenten();
		
//System.out.println("setBounds " + x + " " + y + " " + b + " " + h);		
		
	}
	
	public void zetBreedte(int b)
	{	
		heipBreedte = b;
		
		setBounds(getLocation().x, getLocation().y, heipBreedte + editWidth, Math.max(heipHoogte, editHeight));		
		plaatsComponenten();
	}
	
	public void zetHoogte(int h)
	{	
		heipHoogte = h;
		
		setBounds(getLocation().x, getLocation().y, heipBreedte + editWidth, Math.max(heipHoogte, editHeight));		
	}
	
	public void wis()
	{}
    
	public void zetMode(int mode)
	{}
	
    public void stop()
    {}
    
    public void start()
    {}
    
    public void addActionListener(ActionListener al)
    {}
    
	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == pagina21Button)
		{
			enableKeuze(false);
			heip.zetPagina(1);
		}
		else if (e.getSource() == pagina22Button)
		{
			enableKeuze(false);
			heip.zetPagina(2);
		}
		else if (e.getSource() == pagina23Button)
		{
			enableKeuze(true);
			heip.zetPagina(3);
		}
		else if (e.getSource() == pagina24Button)
		{
			enableKeuze(false);
			heip.zetPagina(4);
		}
		else if (e.getSource() == keuzeButton)
		{
			heip.zetKeuzeErinEruit();
		}
		else if (e.getSource() == erinButton)
		{
			heip.zetAlleenErin();
		}
		else if (e.getSource() == eruitButton)
		{
			heip.zetAlleenEruit();
		}

	}

}
