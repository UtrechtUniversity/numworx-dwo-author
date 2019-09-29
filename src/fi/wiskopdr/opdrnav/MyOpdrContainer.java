package fi.wiskopdr.opdrnav;

import java.awt.AWTEventMulticaster;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.cbook.cbookif.CBookEvent;

import fi.beans.base64code.StringCodeObject;
import fi.beans.scorm.SessionTime;
import fi.beans.wiskopdrbeans.InteractiePanel;
import fi.wiskopdr.HasUitwerking;
import fi.wiskopdr.InteractiePanelContainer;
import fi.wiskopdr.InteractiePanelContainerIF;
import fi.wiskopdr.TekstVakPanel;
import fi.wiskopdr.VariableCollection;
import fi.wiskopdr.WiskOpdr;
import fi.wiskopdr.formuleobjects.FormuleParser;
import fi.wiskopdr.tekstobjects.BasisTekstVak;
import fi.wiskopdr.tekstobjects.TekstArea;
import fi.wiskopdr.tekstobjects.TekstInteractiePanelVak;

public class MyOpdrContainer extends JPanel implements ActionListener
{

	private static final int OEFENEN = 0;
	private static final int OEFENEN_STRAFPUNTEN = 1;
	private static final int ZELFTOETS = 2;
	private static final int EINDTOETS = 3;

	private int score;
	private int scoreMax;
	private boolean correct;
	private boolean fout;
	private int mode;

	private SessionTime session = new SessionTime();

	protected JLabel titelLabel;
	protected TekstArea tekstArea, tekstArea2;
	private String codeString;

	private int margeLinks = 20;
	private int margeBoven = 15;

	private int scheidingX = 280;
	private int eindX = 770;

	private int titelX = -6 + margeLinks;
	private int titelY = -1 + margeBoven;
	private int titelB = 260;
	private int titelH = 30;
	private int tekstX = -6 + margeLinks;
	private int tekstY = margeBoven + 34;
	private int tekstB = 260;
	private int tekstH = 300;
	private int tekst2X = 10;
	private int tekst2Y = -1 + margeBoven;
	private int tekst2B = 260;
	private int tekst2H = 300;
	private int antwX = 20;
	private int antwY = 120;
	private int antwB = 370;
	private int antwH = 270;

	private boolean titelVisible = true;
	private boolean tekstVisible = true;
	private boolean hasAntwoordVak = true;
	private int focusNr = 5;

	private String[] RandomVarNamen;
	private Hashtable RandomVarWaarden;

	protected InteractiePanelContainerIF[] interactiePanelCs;
	protected int aantalInteractiePanelCs;
	private int maxAantalInteractiePanelCs = 475;

	private Font font = WiskOpdr.tekstFont;
	private JPanel scrollPaneBasisPanel;
	private JScrollPane scrollPane;
	public JPanel contentPane;
	private int controlPanelHeight;
	private XWidgetManager manager;

	public MyOpdrContainer(int x, int y, int b, int h)
	{
		setLayout(null);
		setBounds(x, y, b, h);
		setOpaque(false);

		initGuiConstants();
		makeGui();

		interactiePanelCs = new InteractiePanelContainerIF[maxAantalInteractiePanelCs];
		aantalInteractiePanelCs++;
		interactiePanelCs[0] = new InteractiePanelContainer();
		interactiePanelCs[0].setBounds(antwX, antwY, antwB, antwH);
		((Component) interactiePanelCs[0]).setBackground(getBackground());
		interactiePanelCs[0].addActionListener(this);
	}

	private void initGuiConstants()
	{
		if ("MW".equals(WiskOpdr.deployVariant))
		{
			titelX = 8;
			titelY = 10;
			tekstX = 8;
			tekstY = 35;
			tekst2Y = 10;
			tekstB = 380;
			tekstH = 200;
			antwX = 400;
			antwY = 5;
			antwB = 475;
			antwH = 330;
		}
		else if ("GR".equals(WiskOpdr.deployVariant))
		{
			margeBoven = 10;
			margeLinks = 18;

			titelX = -6 + margeLinks;
			titelY = -1 + margeBoven;
			tekstX = -6 + margeLinks;
			tekstY = 35;
			tekst2Y = -1 + margeBoven;
			tekstB = 380;
			tekstH = 200;
			antwX = 400;
			antwY = 5;
			antwB = 475;
			antwH = 330;
		}
		else
		{
			antwB = eindX - 5 - scheidingX;
			antwX = scheidingX + 5;
			tekst2B = eindX - 5 - scheidingX;
			tekst2X = scheidingX + 5;
		}
	}

	private void makeGui()
	{
		contentPane = new JPanel();
		contentPane.setBounds(getBounds());
		contentPane.setLayout(null);
		contentPane.setBackground(getBackground());

		scrollPaneBasisPanel = new JPanel();
		scrollPaneBasisPanel.setBounds(0, 0, getSize().width, getSize().height - 60);
		if (("MW".equals(WiskOpdr.deployVariant) || "GR".equals(WiskOpdr.deployVariant)) && !WiskOpdr.deployDwoGrading)
			scrollPaneBasisPanel.setBounds(0, 0, getSize().width, getSize().height - 30);
		scrollPaneBasisPanel.setLayout(new BorderLayout());
		scrollPaneBasisPanel.setBackground(getBackground());
		add(scrollPaneBasisPanel);

		scrollPane = new JScrollPane(contentPane, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		scrollPane.getVerticalScrollBar().setUnitIncrement(12);
		scrollPane.getHorizontalScrollBar().setUnitIncrement(12);
		scrollPane.setBackground(getBackground());
		scrollPane.setBorder(BorderFactory.createEmptyBorder());

		if (("MW".equals(WiskOpdr.deployVariant) || "GR".equals(WiskOpdr.deployVariant)) && !WiskOpdr.deployDwoGrading)
			scrollPaneBasisPanel.add(contentPane);
		else
			scrollPaneBasisPanel.add(scrollPane);

		titelLabel = new JLabel("");
		titelLabel.setBounds(titelX, titelY, titelB, titelH);
		titelLabel.setFont(WiskOpdr.titelFont); // (new
												// Font("SansSerif",Font.BOLD,16));
		titelLabel.setVisible(titelVisible);
		contentPane.add(titelLabel);

		BasisTekstVak basisTekstVak = new BasisTekstVak();
		this.manager = basisTekstVak.getXWidgetManager();
		tekstArea = new TekstArea(basisTekstVak);
		tekstArea.setBounds(tekstX, tekstY, tekstB, tekstH);
		tekstArea.setFont(font);
		tekstArea.setVisible(tekstVisible);
		tekstArea.addActionListener(this);
		contentPane.add(tekstArea);

		tekstArea2 = new TekstArea(new BasisTekstVak(manager));
		tekstArea2.setBounds(tekst2X, tekst2Y, tekst2B, tekst2H);
		tekstArea2.setFont(font);
		tekstArea2.setVisible(tekstVisible);
		contentPane.add(tekstArea2);

		tekstArea.resize();
		// tekstArea2.resize(); // waarom niet
	}

	public void zetMarges(int margeLinks, int margeBoven)
	{
		this.margeLinks = margeLinks;
		this.margeBoven = margeBoven;
		titelX = -6 + margeLinks;
		titelY = -1 + margeBoven;
		tekstX = -6 + margeLinks;
		tekstY = 34 + margeBoven;
	}

	public void paintComponent(Graphics g)
	{
		Color c = WiskOpdr.bgcolor;
		g.setColor(new Color(c.getRed() - 20, c.getGreen() - 20, c.getBlue() - 20));
		if ("MW".equals(WiskOpdr.deployVariant) || "GR".equals(WiskOpdr.deployVariant))
			g.setColor(Color.white);// new Color(236,245,246));
		else if (!WiskOpdr.zoefi)
		{
			int h = controlPanelHeight / 2;
//			for (int i = 0; i < 11; i++)
//			{
//				g.setColor(new Color(200 + 5 * i, 200 + 5 * i, 200 + 5 * i));
//				g.fillRect(0, getHeight() - 2 * h + h - (i) * h / 10, getWidth(), h / 10 + 1);
//
//			}
			g.setColor(Color.lightGray);
			//g.drawRect(0, getHeight() - 2 * h, getWidth() - 1, h);
		}
	}

	public void setControlPanelHeight(int h)
	{
		controlPanelHeight = h;
		scrollPaneBasisPanel.setBounds(0, 0, getSize().width, getSize().height - h);
		scrollPane.setBounds(0, 0, getSize().width, getSize().height - h);
		setNewScrollSize();
		repaint();
	}

	public void refreshFonts()
	{
		titelLabel.setFont(WiskOpdr.titelFont);
		tekstArea.setFont(WiskOpdr.tekstFont);
		tekstArea2.setFont(WiskOpdr.tekstFont);
	}

	public void increaseFont(int size)
	{
		Vector v = tekstArea.geefInteractiePanels();
		for (int i = 0; i < v.size(); i++)
		{
			Object object = v.elementAt(i);
			if (object instanceof TekstInteractiePanelVak)
				((TekstInteractiePanelVak) object).increaseFont(size);
		}
	}

	public void setBoundsTitelLabel(int x, int y, int b, int h)
	{
		titelLabel.setBounds(x, y, b, h);
	}

	public JPanel getPlainPane()
	{
		return tekstArea;
	}

	public void setNewScrollSize()
	{	setNewScrollSize(true);
	}
	
	public void setNewScrollSize(boolean scrollBack)
	{
		int maxb = 0;
		int maxh = 0;
		for (int i = 0; i < contentPane.getComponentCount(); i++)
		{
			Component c = contentPane.getComponent(i);
			int b = c.getLocation().x + c.getSize().width - 15;
			int h = c.getLocation().y + c.getSize().height;
			if (b > maxb)
				maxb = b;
			if (h > maxh)
				maxh = h;
		}
		contentPane.setPreferredSize(new Dimension(maxb, maxh));
		if ("MW".equals(WiskOpdr.deployVariant) && !WiskOpdr.deployDwoGrading)
			contentPane.setPreferredSize(new Dimension(scrollPane.getWidth(), maxh));
		if(scrollBack)contentPane.scrollRectToVisible(new Rectangle(0, 0, 10, maxh));
		contentPane.revalidate();
		contentPane.doLayout();
	}

	private Vector geefInteractiePanels()
	{
		Vector v = tekstArea.geefInteractiePanels();
		Vector v2 = tekstArea2.geefInteractiePanels();
		for (int i = 0; i < v2.size(); i++)
		{
			v.addElement(v2.elementAt(i));
		}
		return v;
	}

	public void zetOpdracht(String s)
	{
		zetOpdracht(s, true);
	}

	public void zetOpdracht(String s, boolean randomise)
	{
		if (s == null || s.equals(""))
			return;
		codeString = s;

		Object o = StringCodeObject.decodeStringToObject(s);
		if (o == null)
			return;
		Hashtable h = (Hashtable) o;
		manager.clear();
		String titel = "titel";
		String tekst = "tekst";
		String tekst2 = "";
		String randVarString = "";
		int scheidingX = 280;
		int eindX = 770;
		boolean hasTitle = true;
		boolean hasAntwoordVak = true;
		Hashtable[] interactiePanelLaunchData = null;

		if (h.containsKey("titel"))
			titel = (String) h.get("titel");
		if (h.containsKey("tekst"))
			tekst = (String) h.get("tekst");
		if (h.containsKey("tekst2"))
			tekst2 = (String) h.get("tekst2");
		if (h.containsKey("randVarString"))
			randVarString = (String) h.get("randVarString");
		if (h.containsKey("scheidingX"))
			scheidingX = ((Integer) h.get("scheidingX")).intValue();
		if (h.containsKey("eindX"))
			eindX = ((Integer) h.get("eindX")).intValue();
		if (h.containsKey("hasTitle"))
			hasTitle = ((Boolean) h.get("hasTitle")).booleanValue();
		if (h.containsKey("hasAntwoordVak"))
			hasAntwoordVak = ((Boolean) h.get("hasAntwoordVak")).booleanValue();
		if (h.containsKey("interactiePanelLaunchData"))
			interactiePanelLaunchData = (Hashtable[]) h.get("interactiePanelLaunchData");

		antwB = eindX - 5 - scheidingX;
		antwX = scheidingX + 5;
		//tekstB = scheidingX - 20;
		tekstB = MyOpdrEditContainer.defaultDocWidth;
		titelB = scheidingX - 20;
		tekst2B = eindX - 5 - scheidingX;
		tekst2X = scheidingX;

		wis();

		tekstArea.deleteStates();
		tekstArea2.deleteStates();

		tekstArea.setBounds(tekstX, hasTitle ? tekstY : titelY, tekstB, tekstH);
		tekstArea.setMargeX(MyOpdrEditContainer.defaultMarginX);
		tekstArea2.setBounds(tekst2X, tekst2Y, tekst2B, tekst2H);
		titelLabel.setBounds(titelX, titelY, titelB, titelH);

		VariableCollection vc = new VariableCollection();
		boolean wellSet = vc.setVariables(randVarString);

		String[] varnamen = null;
		Hashtable waarden = null;
		if (randomise)
		{
			try
			{
				//if(vc.checkBorders())
				{	varnamen = vc.getVariableNames();
					waarden = vc.getRandomValues();
					RandomVarNamen = varnamen;
					RandomVarWaarden = waarden;
				}
				//else
				//	JOptionPane.showMessageDialog(this, "Fout in definitie randomvariabelen.\n Geen correcte initialisatie.");
			}
			catch (Exception ex)
			{
				wellSet = false;
			}
		}
		else
		{
			varnamen = RandomVarNamen;
			waarden = RandomVarWaarden;
		}

		titelLabel.setText(titel.trim());
		titelLabel.setVisible(hasTitle);

		tekstArea.setVisible(false);
		tekstArea2.setVisible(false);

		try
		{
			tekst = FormuleParser.randomizeTekstVakString(tekst, varnamen, waarden);
		}
		catch (Exception e)
		{
			tekst = "???";
		}
		tekstArea.setText(tekst);
		//tekstArea.layoutTekst();
		tekstArea.resize();

		try
		{
			tekst2 = FormuleParser.randomizeTekstVakString(tekst2, varnamen, waarden);
		}
		catch (Exception e)
		{
			tekst2 = "???";
		}
		tekstArea2.setText(tekst2);
		//tekstArea2.layoutTekst();
		tekstArea2.resize();

		if (tekst2 == null || tekst2.trim().equals(""))
			antwY = tekst2Y;
		else
			antwY = tekst2Y + tekstArea2.getSize().height;
		antwH = 410 - antwY;
		if ("GR".equals(WiskOpdr.deployVariant))
		{
			antwY = 5;
			antwH = 335 - antwY;
		}

		Vector v = geefInteractiePanels();
		aantalInteractiePanelCs = 5 + v.size();
		for (int i = 5; i < aantalInteractiePanelCs; i++)
		{
			interactiePanelCs[i] = (InteractiePanelContainerIF) v.elementAt(i - 5);
			((Component) interactiePanelCs[i]).setBackground(getBackground());
			interactiePanelCs[i].addActionListener(this);
		}

		if (hasAntwoordVak)
			contentPane.add((Component) interactiePanelCs[0]);
		((Component) interactiePanelCs[0]).setVisible(hasAntwoordVak);
		((Component) interactiePanelCs[0]).setBackground(getBackground());
		this.hasAntwoordVak = hasAntwoordVak;

		//// voor backwards compatibility
		wis();
		interactiePanelCs[0].setBounds(antwX, antwY, antwB, antwH);
		if (interactiePanelLaunchData == null)
		{
			interactiePanelCs[0].setBounds(antwX, antwY, antwB, antwH);

			interactiePanelCs[0].zetOpdracht(h, varnamen, waarden);
			interactiePanelCs[0].zetMode(mode);
		}

		else
		////

		{
			for (int i = 0; i < interactiePanelLaunchData.length; i++)
			{
				if (interactiePanelCs[i] != null && interactiePanelLaunchData[i] != null)
				{
					interactiePanelCs[i].zetOpdracht(interactiePanelLaunchData[i], varnamen, waarden);
					interactiePanelCs[i].zetMode(mode);

				}
			}
			
			for (int i = 0; i < interactiePanelLaunchData.length; i++)
			{
				if (interactiePanelCs[i] != null && interactiePanelLaunchData[i] != null)
				{
					interactiePanelCs[i].initConnections(manager);
					
				}
			}

		}

		tekstArea.layoutTekst();
		//tekstArea2.layoutTekst();

		tekstArea.setVisible(true);
		if (!tekst2.trim().equals(""))
			tekstArea2.setVisible(true);
		tekstArea.setBackground(getBackground());
		tekstArea2.setBackground(getBackground());

		setNewScrollSize();
// Wim: always set state to something, shared state
		setStateNull();
	}

	private void setStateNull() {
		for (int i = 0; i < interactiePanelCs.length; i++)
		{
			if (interactiePanelCs[i] != null )
			{
				interactiePanelCs[i].setState(null);
			}
		}
	}

	public void zetMode(int mode)
	{
		this.mode = mode;
		for (int i = 0; i < aantalInteractiePanelCs; i++)
		{
			if (interactiePanelCs[i] != null)
				interactiePanelCs[i].zetMode(mode);
		}
	}

	public void zetNagekeken(boolean b)
	{
		for (int i = 0; i < aantalInteractiePanelCs; i++)
		{
			if (interactiePanelCs[i] != null)
				interactiePanelCs[i].zetNagekeken(b);
		}
	}

	public void setState(Hashtable h)
	{
		Hashtable[] interactiePanelStates = null;
		String[] RandomVarNamen = null;
		Hashtable RandomVarWaarden = null;

		if (h.containsKey("interactiePanelStates"))
			interactiePanelStates = (Hashtable[]) h.get("interactiePanelStates");
		if (h.containsKey("RandomVarNamen"))
			RandomVarNamen = (String[]) h.get("RandomVarNamen");
		if (h.containsKey("RandomVarWaarden"))
			RandomVarWaarden = (Hashtable) h.get("RandomVarWaarden");

		Vector v = geefInteractiePanels();
		for (int i = 0; i < v.size(); i++)
		{
			interactiePanelCs[i + 5] = (InteractiePanelContainerIF) v.elementAt(i);
			interactiePanelCs[i + 5].addActionListener(this);
		}
		aantalInteractiePanelCs = 5 + v.size();

		// // voor backwards compatibility
		if (interactiePanelStates == null)
		{
			String gewensteAntwoordString = null;
			String antwoordString = null;
			String startString = "$f@";
			Hashtable antwoordVakState = null;

			if (h.containsKey("gewensteAntwoordString"))
				gewensteAntwoordString = (String) h.get("gewensteAntwoordString");
			if (h.containsKey("antwoordString"))
				antwoordString = (String) h.get("antwoordString");
			if (h.containsKey("startString"))
				startString = (String) h.get("startString");
			if (h.containsKey("antwoordVakState"))
				antwoordVakState = (Hashtable) h.get("antwoordVakState");

			if (antwoordVakState != null && gewensteAntwoordString != null)
				antwoordVakState.put("gewensteAntwoordString", gewensteAntwoordString);
			if (antwoordVakState != null && antwoordString != null)
				antwoordVakState.put("antwoordString", antwoordString);
			if (antwoordVakState != null && startString != null)
				antwoordVakState.put("startString", startString);

			interactiePanelCs[0].setState(antwoordVakState);

		}
		else
		{
			for (int i = 0; i < interactiePanelStates.length; i++)
			{
				if (interactiePanelStates[i] != null)
					interactiePanelCs[i].setState(interactiePanelStates[i]);
			}
		}
		tekstArea.layoutTekst();
		tekstArea2.layoutTekst();

		this.RandomVarNamen = RandomVarNamen;
		this.RandomVarWaarden = RandomVarWaarden;

	}

	public void zetOpdrachtPlusState(String s, boolean randomise, Hashtable state)
	{
		if (s == null || s.equals(""))
			return;
		codeString = s;

		Object o = StringCodeObject.decodeStringToObject(s);
		if (o == null)
			return;
		manager.clear();
		Hashtable h = (Hashtable) o;

		String titel = "titel";
		String tekst = "tekst";
		String tekst2 = "";
		String randVarString = "";
		int scheidingX = 280;
		int eindX = 770;
		boolean hasTitle = true;
		boolean hasAntwoordVak = true;
		Hashtable[] interactiePanelLaunchData = null;

		if (h.containsKey("titel"))
			titel = (String) h.get("titel");
		if (h.containsKey("tekst"))
			tekst = (String) h.get("tekst");
		if (h.containsKey("tekst2"))
			tekst2 = (String) h.get("tekst2");
		if (h.containsKey("randVarString"))
			randVarString = (String) h.get("randVarString");
		if (h.containsKey("scheidingX"))
			scheidingX = ((Integer) h.get("scheidingX")).intValue();
		if (h.containsKey("eindX"))
			eindX = ((Integer) h.get("eindX")).intValue();
		if (h.containsKey("hasTitle"))
			hasTitle = ((Boolean) h.get("hasTitle")).booleanValue();
		if (h.containsKey("hasAntwoordVak"))
			hasAntwoordVak = ((Boolean) h.get("hasAntwoordVak")).booleanValue();
		if (h.containsKey("interactiePanelLaunchData"))
			interactiePanelLaunchData = (Hashtable[]) h.get("interactiePanelLaunchData");

		antwB = eindX - 5 - scheidingX;
		antwX = scheidingX + 5;
		//tekstB = scheidingX - 20;
		tekstB = MyOpdrEditContainer.defaultDocWidth+5;
		titelB = scheidingX - 20;
		tekst2B = eindX - 5 - scheidingX;
		tekst2X = scheidingX;

		wis();
		tekstArea.deleteStates();
		tekstArea2.deleteStates();

		tekstArea.setBounds(tekstX, hasTitle ? tekstY : titelY, tekstB, tekstH);
		tekstArea2.setBounds(tekst2X, tekst2Y, tekst2B, tekst2H);
		titelLabel.setBounds(titelX, titelY, titelB, titelH);

		String[] varnamen = null;
		Hashtable waarden = null;

		if (state.containsKey("RandomVarNamen"))
			RandomVarNamen = OpdrNavStruct.toStringArray(state.get("RandomVarNamen"));
		if (state.containsKey("RandomVarWaarden"))
			RandomVarWaarden = OpdrNavStruct.toHashtable(state.get("RandomVarWaarden"));

		varnamen = RandomVarNamen;
		waarden = RandomVarWaarden;

		titelLabel.setText(titel.trim());
		titelLabel.setVisible(hasTitle);

		tekstArea.setVisible(false);
		tekstArea2.setVisible(false);

		try
		{
			if (RandomVarNamen != null && RandomVarWaarden != null)
				tekst = FormuleParser.randomizeTekstVakString(tekst, varnamen, waarden);
		}
		catch (Exception e)
		{
			tekst = "???";
		}
		tekstArea.setText(tekst);
		//tekstArea.layoutTekst();
		tekstArea.resize();

		try
		{
			if (RandomVarNamen != null && RandomVarWaarden != null)
				tekst2 = FormuleParser.randomizeTekstVakString(tekst2, varnamen, waarden);
		}
		catch (Exception e)
		{
			tekst2 = "???";
		}
		tekstArea2.setText(tekst2);
		//tekstArea2.layoutTekst();
		tekstArea2.resize();

		if (tekst2 == null || tekst2.trim().equals(""))
			antwY = tekst2Y;
		else
			antwY = tekst2Y + tekstArea2.getSize().height;
		antwH = 410 - antwY;
		if ("GR".equals(WiskOpdr.deployVariant))
		{
			antwY = 5;
			antwH = 335 - antwY;
		}

		wis();
		((Component) interactiePanelCs[0]).setVisible(hasAntwoordVak);
		this.hasAntwoordVak = hasAntwoordVak;

		interactiePanelCs[0].setBounds(antwX, antwY, antwB, antwH);
		((Component) interactiePanelCs[0]).setBackground(getBackground());

		// // voor backwards compatibility

		if (interactiePanelLaunchData == null)
		{
			interactiePanelCs[0].setBounds(antwX, antwY, antwB, antwH);
			interactiePanelCs[0].zetOpdracht(h, varnamen, waarden);
			interactiePanelCs[0].zetMode(mode);
		}

		Hashtable grafiekToolState = null;
		Hashtable[] interactiePanelStates = null;
		String[] RandomVarNamen = null;
		Hashtable RandomVarWaarden = null;

		if (state.containsKey("grafiekToolState"))
			grafiekToolState = OpdrNavStruct.toHashtable(state.get("grafiekToolState"));
		if (state.containsKey("interactiePanelStates"))
			interactiePanelStates = OpdrNavStruct.toHashtableArray(state.get("interactiePanelStates"));
		if (state.containsKey("RandomVarNamen"))
			RandomVarNamen = OpdrNavStruct.toStringArray(state.get("RandomVarNamen"));
		if (state.containsKey("RandomVarWaarden"))
			RandomVarWaarden = OpdrNavStruct.toHashtable(state.get("RandomVarWaarden"));

		Vector v = geefInteractiePanels();
		for (int i = 0; i < v.size(); i++)
		{
			interactiePanelCs[i + 5] = (InteractiePanelContainerIF) v.elementAt(i);
			((Component) interactiePanelCs[i + 5]).setBackground(getBackground());
			interactiePanelCs[i + 5].addActionListener(this);
		}
		aantalInteractiePanelCs = 5 + v.size();

		// // voor backwards compatibility
		if (interactiePanelStates == null)
		{
			String gewensteAntwoordString = null;
			String antwoordString = null;
			String startString = "$f@";
			Hashtable antwoordVakState = null;

			if (state.containsKey("gewensteAntwoordString"))
				gewensteAntwoordString = (String) state.get("gewensteAntwoordString");
			if (state.containsKey("antwoordString"))
				antwoordString = (String) state.get("antwoordString");
			if (state.containsKey("startString"))
				startString = (String) state.get("startString");
			if (state.containsKey("antwoordVakState"))
				antwoordVakState = (Hashtable) state.get("antwoordVakState");

			if (antwoordVakState != null && gewensteAntwoordString != null)
				antwoordVakState.put("gewensteAntwoordString", gewensteAntwoordString);
			if (antwoordVakState != null && antwoordString != null)
				antwoordVakState.put("antwoordString", antwoordString);
			if (antwoordVakState != null && startString != null)
				antwoordVakState.put("startString", startString);

			interactiePanelCs[5].setState(antwoordVakState);

		}
		else
		{
			if (interactiePanelLaunchData != null)
				for (int i = 0; i < interactiePanelLaunchData.length; i++)
				{
					if (interactiePanelLaunchData[i] != null)
					{
						interactiePanelCs[i].zetOpdracht(interactiePanelLaunchData[i], RandomVarNamen, RandomVarWaarden);
						interactiePanelCs[i].zetMode(mode);
					}
				}
			if (interactiePanelLaunchData != null)
				for (int i = 0; i < interactiePanelLaunchData.length; i++)
				{
					if (interactiePanelLaunchData[i] != null)
					{
						interactiePanelCs[i].initConnections(manager);
					}
				}
			
			for (int i = 0; i < interactiePanelStates.length; i++)
			{
				if (interactiePanelStates[i] != null)
					interactiePanelCs[i].setState(interactiePanelStates[i]);
			}
		}
		tekstArea.layoutTekst();
		//tekstArea2.layoutTekst();

		this.RandomVarNamen = RandomVarNamen;
		this.RandomVarWaarden = RandomVarWaarden;

		tekstArea.setVisible(true);
		if (!tekst2.trim().equals(""))
			tekstArea2.setVisible(true);

		tekstArea.setBackground(getBackground());
		tekstArea2.setBackground(getBackground());
		setNewScrollSize();
	}

	public Hashtable getState()
	{
		Hashtable[] interactiePanelStates = null;
		String[] RandomVarNamen;
		Hashtable RandomVarWaarden;

		Vector v = geefInteractiePanels();
		for (int i = 0; i < v.size(); i++)
		{
			interactiePanelCs[i + 5] = (InteractiePanelContainerIF) v.elementAt(i);
		}
		aantalInteractiePanelCs = 5 + v.size();

		interactiePanelStates = new Hashtable[aantalInteractiePanelCs];
		for (int i = 0; i < interactiePanelStates.length; i++)
		{
			if (interactiePanelCs[i] != null)
				interactiePanelStates[i] = interactiePanelCs[i].getState();
		}
		RandomVarNamen = this.RandomVarNamen;
		RandomVarWaarden = this.RandomVarWaarden;

		Hashtable h = new Hashtable();
		h.put("interactiePanelStates", interactiePanelStates);
		if (RandomVarNamen != null)
			h.put("RandomVarNamen", RandomVarNamen);
		if (RandomVarWaarden != null)
			h.put("RandomVarWaarden", RandomVarWaarden);

		return h;
	}

	public int getScore()
	{
		score = 0;
		for (int i = 0; i < aantalInteractiePanelCs; i++)
		{
			if (interactiePanelCs[i] != null && (i != 0 || hasAntwoordVak))
			{
				score += interactiePanelCs[i].getScore();

			}
		}
		return score;
	}

	public int[][] getScoreObjectives()
	{
		if (WiskOpdr.objectives == null)
			return null;
		int[][] scoreObjectives = new int[WiskOpdr.objectives.length][];
		for (int i = 0; i < WiskOpdr.objectives.length; i++)
			scoreObjectives[i] = new int[WiskOpdr.objectives[i].length];
		for (int i = 0; i < aantalInteractiePanelCs; i++)
		{
			if (interactiePanelCs[i] != null && (i != 0 || hasAntwoordVak))
			{
				int[][] scoreObj = interactiePanelCs[i].getScoreObjectives();
				for (int j = 0; scoreObj != null && j < WiskOpdr.objectives.length && j < scoreObj.length; j++)
				{
					for (int k = 0; scoreObj[j] != null && k < WiskOpdr.objectives[j].length && k < scoreObj[j].length; k++)
						try{	scoreObjectives[j][k] += scoreObj[j][k];
						}
						catch(Exception e){}
					
				}
			}
		}
		return scoreObjectives;
	}
	
	public int[][] getPossibleMisconceptions()
	{
		if (WiskOpdr.misconceptions == null)
			return null;
		int[][] totalPossibleMisconceptions = new int[WiskOpdr.misconceptions.length][];
		for (int i = 0; i < WiskOpdr.misconceptions.length; i++)
			totalPossibleMisconceptions[i] = new int[WiskOpdr.misconceptions[i].length];
		for (int i = 0; i < aantalInteractiePanelCs; i++)
		{
			if (interactiePanelCs[i] != null && (i != 0 || hasAntwoordVak))
			{
				int[][] possibleMisconceptions = interactiePanelCs[i].getPossibleMisconceptions();
				for (int j = 0; possibleMisconceptions != null && j < WiskOpdr.misconceptions.length && j < possibleMisconceptions.length; j++)
				{
					for (int k = 0; possibleMisconceptions[j] != null && k < WiskOpdr.misconceptions[j].length && k < possibleMisconceptions[j].length; k++)
						try{	totalPossibleMisconceptions[j][k] += possibleMisconceptions[j][k];
						}
						catch(Exception e){}
				}
			}
		}
		return totalPossibleMisconceptions;
	}
	
	public int[][] getMeasuredMisconceptions()
	{
		if (WiskOpdr.misconceptions == null)
			return null;
		int[][] totalMeasuredMisconceptions = new int[WiskOpdr.misconceptions.length][];
		for (int i = 0; i < WiskOpdr.misconceptions.length; i++)
			totalMeasuredMisconceptions[i] = new int[WiskOpdr.misconceptions[i].length];
		for (int i = 0; i < aantalInteractiePanelCs; i++)
		{
			if (interactiePanelCs[i] != null && (i != 0 || hasAntwoordVak))
			{
				int[][] measuredMisconceptions = interactiePanelCs[i].getMeasuredMisconceptions();
				for (int j = 0; measuredMisconceptions != null && j < WiskOpdr.misconceptions.length && j < measuredMisconceptions.length; j++)
				{
					for (int k = 0; measuredMisconceptions[j] != null && k < WiskOpdr.misconceptions[j].length && k < measuredMisconceptions[j].length; k++)
						try{	totalMeasuredMisconceptions[j][k] += measuredMisconceptions[j][k];
						}
						catch(Exception e){}
				}
			}
		}
		return totalMeasuredMisconceptions;
	}

//	public int getScoreMaxComponentsChanged()
//	{
//		scoreMax = 0;
//        for (int i = 0; i < aantalInteractiePanelCs; i++)
//        {
//            if (interactiePanelCs[i] != null && (i != 0 || hasAntwoordVak) && interactiePanelCs[i] instanceof TekstInteractiePanelVak)
//            {
//                scoreMax += ((TekstInteractiePanelVak) interactiePanelCs[i]).getScoreMaxComponentsChanged();
//
//            }
//        }
//        return scoreMax;
//	}

	public boolean isFout()
	{
		return fout;
	}

	public boolean isCorrect()
	{
		correct = true;
		for (int i = 0; i < aantalInteractiePanelCs; i++)
		{
			if (interactiePanelCs[i] != null && (i != 0 || hasAntwoordVak))
			{
				correct = correct && interactiePanelCs[i].isCorrect();
			}
		}
		return correct;
	}

	public void wis()
	{
		for (int i = 0; i < aantalInteractiePanelCs; i++)
		{
			if (interactiePanelCs[i] != null)
				interactiePanelCs[i].wis();
		}
		correct = false;
		score = 0;
	}

	public void stop()
	{
		sessionStop();
		for (int i = 0; i < aantalInteractiePanelCs; i++)
		{
			if (interactiePanelCs[i] != null)
				interactiePanelCs[i].stop();
		}
	}

	public void closePopups()
	{
		for (int i = 0; i < aantalInteractiePanelCs; i++)
		{
			if (interactiePanelCs[i] != null)
				interactiePanelCs[i].closePopup();
		}
	}

	public void destroy()
	{
		for (int i = 0; i < aantalInteractiePanelCs; i++)
		{
			if (interactiePanelCs[i] != null)
			{
				remove((Component) interactiePanelCs[i]);
				interactiePanelCs[i].destroy();
				interactiePanelCs[i] = null;
			}
		}
		if (tekstArea != null)
		{
			remove(tekstArea);
			tekstArea.destroy();
			tekstArea = null;
		}
		if (tekstArea2 != null)
		{
			remove(tekstArea2);
			tekstArea2.destroy();
			tekstArea2 = null;
		}
	}

	public void sessionStart()
	{
		session.start();
	}

	public void sessionStop()
	{
		session.stop();
	}

	public String getSessionTime()
	{
		return session.toString();
	}

	public void setInitialTime(String stamp)
	{
		try
		{
			session.setInitialTime(stamp);
		}
		catch (IllegalArgumentException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void start()
	{
		if (hasAntwoordVak && interactiePanelCs[0] != null)
			interactiePanelCs[0].start();
		for (int i = 1; i < aantalInteractiePanelCs; i++)
		{
			if (interactiePanelCs[i] != null)
				interactiePanelCs[i].start();
		}
		contentPane.scrollRectToVisible(new Rectangle(0, 0, 10, 10));

		boolean focusGezet = false;
		if (interactiePanelCs[focusNr] != null)
			focusGezet = interactiePanelCs[focusNr].zetFocus();
		for (int i = 5; i < aantalInteractiePanelCs && !focusGezet; i++)
		{
			if (interactiePanelCs[i] != null)
				focusGezet = interactiePanelCs[i].zetFocus();
			if (focusGezet)
				break;
		}
		sessionStart();
	}

	public void verplaatsFocus()
	{
		boolean focusGezet = false;
		for (int i = 0; i < aantalInteractiePanelCs && !focusGezet; i++)
		{
			focusNr = (focusNr + 1) % aantalInteractiePanelCs;
			if (interactiePanelCs[focusNr] != null)
				focusGezet = interactiePanelCs[focusNr].zetFocus();
			if (focusGezet)
				break;
		}
		//System.out.println("tab");
	}

	public void kijkNa()
	{
		for (int i = 0; i < aantalInteractiePanelCs; i++)
		{
			if (interactiePanelCs[i] != null)
				interactiePanelCs[i].kijkNa();
		}
	}

	public void kijkNa(int nr)
	{
		for (int i = 0; i < aantalInteractiePanelCs; i++)
		{
			if (interactiePanelCs[i] != null)
				interactiePanelCs[i].kijkNa(nr);
		}
	}

	public void opnieuw()
	{
		score = 0;
		correct = false;
		for (int i = 0; i < aantalInteractiePanelCs; i++)
		{
			if (interactiePanelCs[i] != null)
				interactiePanelCs[i].opnieuw();
		}
		zetOpdracht(codeString);
		contentPane.scrollRectToVisible(new Rectangle(0, 0, 10, 10));
	}

	public void setBackground(Color c)
	{
		if (contentPane != null)
			contentPane.setBackground(c);
		super.setBackground(c);
	}

	public void zetUitlegTekst(String s)
	{
		tekstArea.setText(s);
		tekstArea.resize();
	}

	public void actionPerformed(ActionEvent e)
	{
		if (e.getActionCommand().equals("resizeTekstArea"))
		{
			setNewScrollSize(false);
		}
		if (!e.getActionCommand().equals("resize"))
		{
			score = 0;
			scoreMax = 0;
			correct = true;
			fout = false;
			for (int i = 0; i < aantalInteractiePanelCs; i++)
			{
				if (interactiePanelCs[i] != null && (i != 0 || hasAntwoordVak))
				{
					score += interactiePanelCs[i].getScore();
					scoreMax += interactiePanelCs[i].getScoreMax();
					correct = correct && interactiePanelCs[i].isCorrect();
					if (e.getSource() == interactiePanelCs[i])
						fout = interactiePanelCs[i].isFout();
				}
			}
		}
		produceAction(e.getActionCommand());

	}

	// ActionProducer
	private ActionListener actionListener = null;

	public void addActionListener(ActionListener l)
	{
		actionListener = AWTEventMulticaster.add(actionListener, l);
	}

	public void removeActionListener(ActionListener l)
	{
		actionListener = AWTEventMulticaster.remove(actionListener, l);
	}

	public void produceAction(String command)
	{
		if (actionListener != null)
		{
			actionListener.actionPerformed(new ActionEvent(this, 0, command));
		}
	}

	// end ActionProducer

	public JPanel getContentPane()
	{
		return contentPane;
	}
	
	
	public JPanel popups = new JPanel(new FlowLayout(FlowLayout.LEADING));
	public void prepareForPrint() {
		popups.removeAll();
		popups.setOpaque(false);
		Vector outer = geefInteractiePanels();
		while( !outer.isEmpty() ) {
			Iterator iterator = outer.iterator();	
		for (outer = new Vector(); iterator.hasNext();) {
			InteractiePanelContainerIF object = (InteractiePanelContainerIF) iterator.next();
			if(object instanceof TekstInteractiePanelVak)
			{
				
				TekstInteractiePanelVak vak = (TekstInteractiePanelVak) object;
				boolean popup = vak.isPopup();
				InteractiePanel panel = vak.getInteractiePanel();
				if(popup) {
					JComponent component = new JPanel(new BorderLayout());
					Component c = (Component)panel;
					Dimension size = c.getSize();
					c.invalidate();
					c.setPreferredSize(size);
					component.add(c, BorderLayout.CENTER);
					component.setBorder(BorderFactory.createTitledBorder("Popup"));
					component.setSize(component.getPreferredSize());
					component.setPreferredSize(component.getSize());
					component.doLayout();
					popups.add(component);
				}
				makeVisible(outer,panel, popups);
			} else {
				Vector inner = object.geefInteractiePanels();
				for (Iterator iterator2 = inner.iterator(); iterator2.hasNext();) {
					InteractiePanel panel = (InteractiePanel) iterator2.next();
					makeVisible(outer, panel, popups);
			}}
		}}
	}

	public void makeVisible(Vector outer, InteractiePanel panel, JPanel popups) {
		if(panel instanceof TekstVakPanel) {
			TekstVakPanel vak = (TekstVakPanel) panel;
			vak.prepareForPrint();
			outer.addAll(vak.geefInteractiePanels());
		} else if (panel instanceof HasUitwerking) {
			HasUitwerking hut = (HasUitwerking) panel;
			JComponent uitwerking = hut.prepareForPrint();
			if(uitwerking != null) popups.add(uitwerking);
		}
	}
}
