package fi.kladje;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.awt.image.*;
import java.io.Serializable;

public class KladjeVeld extends JPanel 
{
	static double NZERO = 1e-5d;
	
	boolean lijnen = false;
	boolean ruitjes = false;
	
	int lineDistance = 20;
	int gridSize = 20;
	
	Color lijnenKleur = new Color(150, 150, 255);
	Color ruitjesKleur = new Color(210, 210, 210);
	static Color bbColor = Color.blue; 
	
	Color drawingColor = Color.black;
	Color backgroundColor = Color.white;
	
	Color[] kleuren = {Color.black, Color.lightGray, Color.red, Color.orange,
	   		           Color.green, Color.cyan, Color.blue, Color.magenta};

	final int tekenen = 0;
	//final int gummen = 1;
	final int lijnTekenen = 2;
	final int rechthoekTekenen = 3;
	final int cirkelTekenen = 4;
	final int tekstTekenen = 5;
	final int selecteren = 6;
	int mouseMode = tekenen;
	
	Vector draggPoints = new Vector();
	//Vector gumPunten = new Vector();
	//int gumGrootte = 7; // oneven	
	Point figuurStart = null;
	Point lijnEinde = null;
	Rectangle tekenRechthoek = null;
//	Rectangle tekstRechthoek = null;
//	Rectangle selecteerRechthoek = null;

	// backwards compatibility
	ColorBytes[][] pixels = null;
	int breedte, hoogte;
	Vector streepVector = new Vector();
	Vector lijnVector = new Vector();
	Vector rechthoekVector = new Vector();
	Vector ellipsVector = new Vector();
	Vector tekstElementVector = new Vector();
	
	//Image offScreen = null;
	//Graphics offGraphics = null;
	
	int maxHistories = 5;
	int numHistories = 0;
	Hashtable[] histories = new Hashtable[maxHistories + 1];

	boolean shiftPressed = false;
	
	Cursor selectCursor = null;
	boolean sleepSelectie = false;
	boolean objectMoved = false;
	//Vector sleepPoints = new Vector();
	
	Streep selectedStreep = null;
	Lijn selectedLijn = null;
	Rechthoek selectedRechthoek = null;
	Ellips selectedEllips = null;
	TekstElement selectedTekstElement = null;
	
	Cursor textCursor = null;
	JTextField tekstVeld;
	static Font tekstFont;
	//Font tekenTekstFont;
	static FontMetrics tekstFM;
	int tekstBreedte = 50;
	int tekstHoogte;
	//boolean sleepTekst;
	//int tekstRand = 5;
	String tekstString = "";
	int tekstX = 0;
	int tekstY = 0;
	
	public KladjeVeld(int w, int h)
	{
		breedte = w;
		hoogte = h;
		setSize(breedte, hoogte);
		
		tekstFont = new Font("Sanserif", Font.BOLD, 14);
		//tekenTekstFont = new Font("Sanserif", Font.PLAIN, 14);
		tekstFM = getFontMetrics(tekstFont);
		//tekstHoogte = 4 * tekstFM.getHeight() / 3;
		tekstHoogte = tekstFM.getHeight();

		tekstVeld = new JTextField();
		tekstVeld.setFont(tekstFont);
		tekstVeld.setSize(tekstBreedte, tekstHoogte);
		tekstVeld.setVisible(false);
		add(tekstVeld);
		tekstVeld.addActionListener(new TekstAL());
		
		MLMML listener = new MLMML();
		
		addMouseListener(listener);
		addMouseMotionListener(listener);
		
	}

	public void hideTekstVeld(boolean update)
	{
		tekstString = tekstVeld.getText();
		tekstX = tekstVeld.getLocation().x + 2;
		tekstY = tekstVeld.getLocation().y + tekstFM.getAscent();// + tekstFM.getHeight() / 6;
		//tekstRechthoek = null;
		
		tekstVeld.setVisible(false);

		if (!tekstString.equals(""))
		{
			TekstElement tekstElement = 
				new TekstElement(drawingColor, tekstString, tekstX, tekstY);
			tekstElementVector.addElement(tekstElement);
			addToHistory();
		}
		
		//drawTekstString();
	
		if (update)
		{	
			tekstString = "";
		
		}
		
	}
	
	void addToHistory()
	{
		//Vector stateVector = getState();
		Hashtable stateTable = getState();
		
		histories[numHistories] = stateTable;
		numHistories++;
		
		if (numHistories > maxHistories)
		{	for (int i = 0; i < numHistories - 1; i++)
			{	histories[i] = histories[i + 1];
			}
			numHistories--;
		}
		
System.out.println("ath " + numHistories);		
	}
	
	public Hashtable getFromHistory()
	{	//if (numHistories <= 1)
		//	return null;
		
		if (numHistories > 0)
			numHistories--;
		
System.out.println("gfh " + numHistories);

		if (numHistories > 0)
		{	
System.out.println("returned " + numHistories);			
			return histories[numHistories - 1];
		
		}
		else
		{	numHistories = 0;
System.out.println("returned null " + numHistories);		
		
			return null;
		}
	}
	
	public void setSize(int w, int h)
	{
//System.out.println("kladjeVeld.setSize");		
		
		int oudeBreedte = breedte;
		int oudeHoogte = hoogte;
		breedte = w;
		hoogte = h;
		if (pixels != null)
		{
//System.out.println("pixels != null");

			ColorBytes[][] oudePixels = new ColorBytes[oudeBreedte][oudeHoogte];
			for (int hCnt = 0; hCnt < oudeBreedte; hCnt++)
				for (int vCnt = 0; vCnt < oudeHoogte; vCnt++)
				{
					oudePixels[hCnt][vCnt] = pixels[hCnt][vCnt];
				}

			pixels = new ColorBytes[breedte][hoogte];
			for (int hCnt = 0; hCnt < breedte; hCnt++)
				for (int vCnt = 0; vCnt < hoogte; vCnt++)
				{	if ((hCnt < Math.min(breedte, oudeBreedte)) && 
						(vCnt < Math.min(hoogte, oudeHoogte)))
						pixels[hCnt][vCnt] = oudePixels[hCnt][vCnt];
					else
						pixels[hCnt][vCnt] = new ColorBytes(hCnt, vCnt,backgroundColor);
				}

			//offScreen = null;
			
		}
		else
		{
//System.out.println("pixels == null");			
			pixels = new ColorBytes[breedte][hoogte];
			for (int hCnt = 0; hCnt < breedte; hCnt++)
				for (int vCnt = 0; vCnt < hoogte; vCnt++)
				{
					pixels[hCnt][vCnt] = new ColorBytes(hCnt, vCnt, backgroundColor);
				}
			
		}
		
	
		super.setSize(breedte, hoogte);
		
		repaint();
		
	}

/*	
	public Vector copyRectangle(Rectangle r)
	{
		Vector rVector = new Vector();
		for (int hCnt = r.x; hCnt < Math.min(breedte, r.x + r.width); hCnt++)
			for (int vCnt = r.y; vCnt < Math.min(hoogte, r.y + r.height); vCnt++)
			{
				Color c = pixels[hCnt][vCnt].makeColor();
				if (!c.equals(backgroundColor))
				{
					ColorBytes newCB = new ColorBytes(pixels[hCnt][vCnt].x, pixels[hCnt][vCnt].y, 
							                          pixels[hCnt][vCnt].red, pixels[hCnt][vCnt].green, pixels[hCnt][vCnt].blue);
					//stateVector.addElement(pixels[hCnt][vCnt]);
					rVector.addElement(newCB);
				}
			}
		
		
		return rVector;
	}
*/
/*	
	public void wisRectangle(Rectangle r)
	{
		int cnt = 0;
		for (int xCnt = r.x; xCnt < Math.min(breedte, r.x + r.width); xCnt++)
			for (int yCnt = r.y; yCnt < Math.min(hoogte, r.y + r.height); yCnt++)
			{
				pixels[xCnt][yCnt].zetColor(backgroundColor);
				cnt++;
			}
		
//System.out.println("wisrect = " + cnt);		
	}
*/	
	public Hashtable getState()
	{
		Hashtable h = new Hashtable();
		
		// backwards compatibility
		Vector gwtStateVector = getGWTState();
		if (gwtStateVector.size() > 0)
			h.put("gwtpixels", gwtStateVector);
		
		Hashtable[] strepen = new Hashtable[streepVector.size()];
		for (int sCnt = 0; sCnt < streepVector.size(); sCnt++)
		{	Streep streep = (Streep) streepVector.elementAt(sCnt);
			strepen[sCnt] = streep.getState();
		}
		h.put("strepen", strepen);
		
		Hashtable[] lijnenHash = new Hashtable[lijnVector.size()];
		for (int lCnt = 0; lCnt < lijnVector.size(); lCnt++)
		{	Lijn lijn = (Lijn) lijnVector.elementAt(lCnt);
			lijnenHash[lCnt] = lijn.getState();
		}
		h.put("lijnenhash", lijnenHash);
		
		Hashtable[] rechthoeken = new Hashtable[rechthoekVector.size()];
		for (int rCnt = 0; rCnt < rechthoekVector.size(); rCnt++)
		{	Rechthoek rechthoek = (Rechthoek) rechthoekVector.elementAt(rCnt);
			rechthoeken[rCnt] = rechthoek.getState();
		}
		h.put("rechthoeken", rechthoeken);
		
		Hashtable[] ellipsen = new Hashtable[ellipsVector.size()];
		for (int eCnt = 0; eCnt < ellipsVector.size(); eCnt++)
		{	Ellips ellips = (Ellips) ellipsVector.elementAt(eCnt);
			ellipsen[eCnt] = ellips.getState();
		}
		h.put("ellipsen", ellipsen);
		
		Hashtable[] tekstElementen = new Hashtable[tekstElementVector.size()];
		for (int tCnt = 0; tCnt < tekstElementVector.size(); tCnt++)
		{	TekstElement tekstElement = (TekstElement) tekstElementVector.elementAt(tCnt);
			tekstElementen[tCnt] = tekstElement.getState();
		}
		h.put("tekstElementen", tekstElementen);
		
		
		return h;
	}
	
	public Vector getGWTState()
	{	Vector gwtStateVector = new Vector();
		for (int hCnt = 0; hCnt < breedte; hCnt++)
			for (int vCnt = 0; vCnt < hoogte; vCnt++)
			{	Color c = pixels[hCnt][vCnt].makeColor();
				if (!c.equals(backgroundColor))
				{	short[] gwtPixels = new short[5];
					gwtPixels[0] = (short) pixels[hCnt][vCnt].x;
					gwtPixels[1] = (short) pixels[hCnt][vCnt].y;
					gwtPixels[2] = pixels[hCnt][vCnt].red;
					gwtPixels[3] = pixels[hCnt][vCnt].green;			
					gwtPixels[4] = pixels[hCnt][vCnt].blue;						
					gwtStateVector.addElement(gwtPixels);
				}
			}
//System.out.println("kladjeVeld getGWTState " + gwtStateVector.size());
		return gwtStateVector;
	}

	public void setState(Hashtable h)
	{
		// backwards-compatibility
		Vector stateVector = new Vector();
		Vector gwtStateVector = new Vector();
		if (h.containsKey("gwtpixels"))
		{	gwtStateVector = (Vector) h.get("gwtpixels");
			if (gwtStateVector.size() > 0)
				setOldGWTState(gwtStateVector);
		}	
		else if (h.containsKey("pixels"))
		{	stateVector = (Vector) h.get("pixels");
			if (stateVector.size() > 0)
				setOldState(stateVector);
		}	

		// hier de rest
		streepVector.removeAllElements();
		Hashtable[] strepen = new Hashtable[0];
		if (h.containsKey("strepen"))
			strepen = (Hashtable[]) h.get("strepen");
		for (int sCnt = 0; sCnt < strepen.length; sCnt++)
		{	Streep streep = Streep.setState(strepen[sCnt]);
			streepVector.addElement(streep);
		}
		
		lijnVector.removeAllElements();
		Hashtable[] lijnenHash = new Hashtable[0];
		if (h.containsKey("lijnenhash"))
			lijnenHash = (Hashtable[]) h.get("lijnenhash");
		for (int lCnt = 0; lCnt < lijnenHash.length; lCnt++)
		{	Lijn lijn = Lijn.setState(lijnenHash[lCnt]);
			lijnVector.addElement(lijn);
		}

		rechthoekVector.removeAllElements();
		Hashtable[] rechthoeken = new Hashtable[0];
		if (h.containsKey("rechthoeken"))
			rechthoeken = (Hashtable[]) h.get("rechthoeken");
		for (int rCnt = 0; rCnt < rechthoeken.length; rCnt++)
		{	Rechthoek rechthoek = Rechthoek.setState(rechthoeken[rCnt]);
			rechthoekVector.addElement(rechthoek);
		}

		ellipsVector.removeAllElements();
		Hashtable[] ellipsen = new Hashtable[0];
		if (h.containsKey("ellipsen"))
			ellipsen = (Hashtable[]) h.get("ellipsen");
		for (int eCnt = 0; eCnt < ellipsen.length; eCnt++)
		{	Ellips ellips = Ellips.setState(ellipsen[eCnt]);
			ellipsVector.addElement(ellips);
		}

		tekstElementVector.removeAllElements();
		Hashtable[] tekstElementen = new Hashtable[0];
		if (h.containsKey("tekstElementen"))
			tekstElementen = (Hashtable[]) h.get("tekstElementen");
		for (int tCnt = 0; tCnt < tekstElementen.length; tCnt++)
		{	TekstElement tekstElement = TekstElement.setState(tekstElementen[tCnt]);
			tekstElementVector.addElement(tekstElement);
		}
		
	}
	
	public void setOldState(Vector stateVector)
	{
System.out.println("kladjeVeld setState " + stateVector.size());

		int cnt = 0;
		for (int pCnt = 0; pCnt < stateVector.size(); pCnt++)
		{	ColorBytes cb = (ColorBytes) stateVector.elementAt(pCnt);
			if ((cb.x < breedte) && (cb.y < hoogte))
			{	pixels[cb.x][cb.y] = cb;
				Color c = pixels[cb.x][cb.y].makeColor();
				if (!c.equals(backgroundColor))
					cnt++;
			}
		}
//		repaint();
	}

	public void setOldGWTState(Vector gwtStateVector)
	{
System.out.println("kladjeVeld setGWTState " + gwtStateVector.size());

		int cnt = 0;
		for (int pCnt = 0; pCnt < gwtStateVector.size(); pCnt++)
		{
			short[] gwtPixels = new short[5];
			gwtPixels = (short[]) gwtStateVector.elementAt(pCnt);
			if ((gwtPixels[0] < breedte) && (gwtPixels[1] < hoogte))
			{	pixels[gwtPixels[0]][gwtPixels[1]] = 
					new ColorBytes(gwtPixels[0], gwtPixels[1], gwtPixels[2], gwtPixels[3], gwtPixels[4]);
			}
		}
//		repaint();
	}
	
	
	public void zetDrawingColor(int code)
	{
		drawingColor = kleuren[code];
		
	}
	
	public void zetLijnen(boolean b)
	{	lijnen = b;
		repaint();
	}

	public void zetRuitjes(boolean b)
	{	ruitjes = b;
		repaint();
	}
	
/*	
	public void drawTekstString()
	{
		if (offGraphics != null)
		{
			if (tekstString.equals(""))
				return;
			
			offGraphics.setColor(Color.black);
			offGraphics.setFont(tekenTekstFont);
			offGraphics.drawString(tekstString, tekstX, tekstY);
			
			updatePixelArray();
			addToHistory();
			tekstString = "";
			
		}
	}
*/	
	
	public void paintComponent(Graphics g)
	{
/*		
		if (offScreen == null)
		{
			offScreen = createImage(getSize().width, getSize().height);
			offGraphics = offScreen.getGraphics();
		}
*/		
		
		//tekenProgramma(offGraphics, true);
		

		g.setColor(backgroundColor);
		g.fillRect(0, 0, getSize().width, getSize().height);
		
		//g.setColor(Color.black);
		//g.drawRect(0, 0, getSize().width - 1, getSize().height - 1);
		
		if (lijnen)
		{
			g.setColor(lijnenKleur);
			int steps = getSize().height / lineDistance;
			for (int lCnt = 1; lCnt <= steps; lCnt++)
			{
				g.drawLine(0, lCnt * lineDistance, getSize().width - 1, lCnt * lineDistance);
			}
			
		}
		if (ruitjes)
		{
			g.setColor(ruitjesKleur);
			int vSteps = getSize().height / lineDistance;
			for (int vCnt = 1; vCnt <= vSteps; vCnt++)
			{
				g.drawLine(0, vCnt * lineDistance, getSize().width - 1, vCnt * lineDistance);
			}
			int hSteps = getSize().width / lineDistance;
			for (int hCnt = 1; hCnt <= hSteps; hCnt++)
			{
				g.drawLine(hCnt * lineDistance, 0, hCnt * lineDistance, getSize().height - 5);
			}
			
		}

		Graphics2D g2D = (Graphics2D) g;
		g2D.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL,RenderingHints.VALUE_STROKE_NORMALIZE);
		g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		BasicStroke stroke = new BasicStroke(1.5f);
		
//System.out.println("width = " + stroke.getLineWidth());
//System.out.println("cap = " + stroke.getEndCap());
//System.out.println("join = " + stroke.getLineJoin());
//System.out.println("miter = " + stroke.getMiterLimit());
//System.out.println("dash = " + stroke.getDashArray());
//System.out.println("dash_phase = " + stroke.getDashPhase());

		g2D.setStroke(new BasicStroke(1.5f));
		
		tekenProgramma(g2D, false);
		
	}
	
	
	void tekenProgramma(Graphics g, boolean wis)
	{
		if (wis)
		{	
			g.setColor(backgroundColor);
			g.fillRect(0, 0, getSize().width, getSize().height);
		
			//g.setColor(Color.black);
			//g.drawRect(0, 0, getSize().width - 1, getSize().height - 1);
		}
		
		int tpCnt = 0;
		
		// backwards compatibility
		for (int hCnt = 1; hCnt < getSize().width - 1; hCnt++)
			for (int vCnt = 1; vCnt < getSize().height - 1; vCnt++)
			{
				Color c = pixels[hCnt][vCnt].makeColor();
				if (!c.equals(backgroundColor))
				{
					tekenPunt(g, hCnt, vCnt);
					tpCnt++;
				}
			}
		
//System.out.println("wis = " + wis + " tp = " + tpCnt);
		
		// elementen
		for (int sCnt = 0; sCnt < streepVector.size(); sCnt++)
		{	Streep streep = (Streep) streepVector.elementAt(sCnt);
			streep.teken(g);
		}
		for (int lCnt = 0; lCnt < lijnVector.size(); lCnt++)
		{	Lijn lijn = (Lijn) lijnVector.elementAt(lCnt);
			lijn.teken(g);
		}
		for (int rCnt = 0; rCnt < rechthoekVector.size(); rCnt++)
		{	Rechthoek rechthoek = (Rechthoek) rechthoekVector.elementAt(rCnt);
			rechthoek.teken(g);
		}
		for (int eCnt = 0; eCnt < ellipsVector.size(); eCnt++)
		{	Ellips ellips = (Ellips) ellipsVector.elementAt(eCnt);
			ellips.teken(g);
		}
		for (int tCnt = 0; tCnt < tekstElementVector.size(); tCnt++)
		{	TekstElement tekstElement = (TekstElement) tekstElementVector.elementAt(tCnt);
			tekstElement.teken(g);
		}
			
		
		
		g.setColor(drawingColor);		
		
		if (draggPoints.size() == 1)
		{	Point p = (Point) draggPoints.elementAt(0);
			g.drawLine(p.x, p.y, p.x, p.y);
		}
		if (draggPoints.size() > 1)
		{	Point p1 = (Point) draggPoints.elementAt(0);
			for (int pCnt = 1; pCnt < draggPoints.size(); pCnt++)
			{	Point p2 = (Point) draggPoints.elementAt(pCnt);
				g.drawLine(p1.x, p1.y, p2.x, p2.y);
				p1 = p2;
			}
			
		}
		
		if ((mouseMode == lijnTekenen) && (figuurStart != null) && (lijnEinde != null))
		{	
			g.drawLine(figuurStart.x, figuurStart.y, lijnEinde.x, lijnEinde.y);
		}
		
		if ((mouseMode == rechthoekTekenen) && (tekenRechthoek != null))
		{
			g.drawRect(tekenRechthoek.x, tekenRechthoek.y, tekenRechthoek.width, tekenRechthoek.height);
		}
		
		if ((mouseMode == cirkelTekenen) && (tekenRechthoek != null))
		{
			g.drawOval(tekenRechthoek.x, tekenRechthoek.y, tekenRechthoek.width, tekenRechthoek.height);
		}
		
/*		
		// alleen op het scherm
		if ((mouseMode == tekstTekenen) && (tekstRechthoek != null) && !wis)
		{
			
//System.out.println("drawing trh at " + tekstRechthoek.x + "," + tekstRechthoek.y);

			Graphics2D g2D = (Graphics2D) g;
			float[] dash = new float[2];
			dash[0] = 1;
			dash[1] = 3;
			g2D.setStroke(new BasicStroke(1.2f, 2, 0, 10.0f, dash, 0.0f));
			g.setColor(Color.black);
			g.drawRect(tekstRechthoek.x, tekstRechthoek.y, tekstRechthoek.width, tekstRechthoek.height);
			g2D.setStroke(new BasicStroke(1.5f, 2, 0, 10.0f, null, 0.0f));
		}
*/		

/*		
		if ((mouseMode == tekstTekenen) && !tekstString.equals("") && wis)
		{
			
//System.out.println("ds " + tekstString + " at " + tekstX + "," + tekstY + " w = " + wis); 


//Graphics2D g2D = (Graphics2D) g;
//g2D.setStroke(new BasicStroke(1.0f));
			g.setColor(Color.black);
			g.setFont(tekenTekstFont);
			g.drawString(tekstString, tekstX, tekstY);
			
			updatePixelArray();
			tekstString = "";
			
//g2D.setStroke(new BasicStroke(1.5f));			
		}
*/
/*		
		// alleen op het scherm
		if ((mouseMode == selecteren) && (selecteerRechthoek != null) && !sleepSelectie && !wis)
		{
			Graphics2D g2D = (Graphics2D) g;
			float[] dash = new float[2];
			dash[0] = 5;
			dash[1] = 5;
			g2D.setStroke(new BasicStroke(1.2f, 2, 0, 10.0f, dash, 0.0f));
			g.setColor(Color.blue);
			g.drawRect(selecteerRechthoek.x, selecteerRechthoek.y, selecteerRechthoek.width, selecteerRechthoek.height);
			g2D.setStroke(new BasicStroke(1.5f, 2, 0, 10.0f, null, 0.0f));
			
		}
*/
/*		
		// alleen op het scherm
		if ((mouseMode == selecteren) && (selecteerRechthoek != null) && sleepSelectie && !wis)
		{	
			
			Graphics2D g2D = (Graphics2D) g;
			float[] dash = new float[2];
			dash[0] = 5;
			dash[1] = 5;
			g2D.setStroke(new BasicStroke(1.2f, 2, 0, 10.0f, dash, 0.0f));
			g.setColor(Color.blue);
			g.drawRect(selecteerRechthoek.x, selecteerRechthoek.y, selecteerRechthoek.width, selecteerRechthoek.height);
			g2D.setStroke(new BasicStroke(1.5f, 2, 0, 10.0f, null, 0.0f));
			for (int pCnt = 0; pCnt < sleepPoints.size(); pCnt++)
			{	ColorBytes cb = (ColorBytes) sleepPoints.elementAt(pCnt);
				g.setColor(cb.makeColor());
				g.drawLine(cb.x, cb.y, cb.x, cb.y);
			}
			
		}
*/	
		
		if (mouseMode == selecteren)
		{
			if (selectedStreep != null)
				selectedStreep.tekenBB(g);
			if (selectedLijn != null)
				selectedLijn.tekenBB(g);
			if (selectedRechthoek != null)
				selectedRechthoek.tekenBB(g);
			if (selectedEllips != null)
				selectedEllips.tekenBB(g);
			if (selectedTekstElement != null)
				selectedTekstElement.tekenBB(g);
		}
	}
	
/*	
	void updatePixelArray()
	{	int w = getSize().width;
		int h = getSize().height;
		
		int[] tempPixels = new int[w * h];
		PixelGrabber pg = new PixelGrabber(offScreen, 0, 0,  w, h, tempPixels, 0, w);
		try 
		{    pg.grabPixels();
		}
		catch (InterruptedException e) 
		{   System.err.println("interrupted waiting for pixels!");
		    return;
		}
		if ((pg.getStatus() & ImageObserver.ABORT) != 0) 
		{   System.err.println("image fetch aborted or errored");
		    return;
		}
		for (int j = 0; j < h; j++) 
		{   for (int i = 0; i < w; i++) 
			{
				pixels[i][j] = handlesinglepixel(i, j, tempPixels[j * w + i]);
		    }
		}
		
		
	}	
*/		
	
/*	
	public ColorBytes handlesinglepixel(int x, int y, int pixel) 
	{
		int alpha = (pixel >> 24) & 0xff;
		int red   = (pixel >> 16) & 0xff;
		int green = (pixel >>  8) & 0xff;
		int blue  = (pixel      ) & 0xff;
			
		return new ColorBytes(x, y, red, green, blue);
	}
*/
		
	
	void tekenPunt(Graphics g, int x, int y)
	{
		g.setColor(pixels[x][y].makeColor());
		
		g.drawLine(x, y, x, y);
		
	}
/*	
	void gumPunt(int x, int y)
	{
		for (int xCnt = x - gumGrootte / 2; xCnt <= x + gumGrootte / 2; xCnt++)
			for (int yCnt = y - gumGrootte / 2; yCnt <= y + gumGrootte / 2; yCnt++)
			{
				if ((xCnt >= 0) && (xCnt < getSize().width - 1) &&
					(yCnt >= 0) && (yCnt < getSize().height - 1))
				{	pixels[xCnt][yCnt].zetColor(backgroundColor);
				
				}
				
			}
	}
*/
	void undo()
	{
		wis(false);
		Hashtable lastState = getFromHistory();
		if (lastState != null)
		{	setState(lastState);
		}

		repaint();
	}
	
	void wis(boolean complete)
	{	
/*		
		if ((mouseMode == selecteren) && (selecteerRechthoek != null))
		{
			wisRectangle(selecteerRechthoek);
		}
*/
		if ((mouseMode == selecteren) && objectSelected())
		{
			wisObjectSelected();
			
		}
		else
		{
			for (int xCnt = 0; xCnt < breedte; xCnt++)
				for (int yCnt = 0; yCnt < hoogte; yCnt++)
				{	pixels[xCnt][yCnt].zetColor(backgroundColor);
				}
			
			streepVector.removeAllElements();
			lijnVector.removeAllElements();
			rechthoekVector.removeAllElements();
			ellipsVector.removeAllElements();
			tekstElementVector.removeAllElements();
			
			if (complete)
				numHistories = 0;
		}	
		repaint();
	}
	
	public void resetSelectedObject()
	{
		selectedStreep = null;
		selectedLijn = null;
		selectedRechthoek = null;
		selectedEllips = null;
		selectedTekstElement = null;
		
	}
	
	public boolean objectSelected()
	{
		return (selectedStreep != null)|| 
		   (selectedLijn != null) || 
		   (selectedRechthoek != null) || 
		   (selectedEllips != null) ||
		   (selectedTekstElement != null);		
	}
	
	public boolean setSelectedObject(int x, int y)
	{	boolean found = false;
	
		for (int sCnt = 0; sCnt < streepVector.size(); sCnt++)
		{	Streep streep = (Streep) streepVector.elementAt(sCnt);
			if (streep.bbContains(x, y))
			{	selectedStreep = streep;
				selectedLijn = null;
				selectedRechthoek  = null;
				selectedEllips  = null;
				selectedTekstElement  = null;
				return true; 
			}
		}
		for (int lCnt = 0; lCnt < lijnVector.size(); lCnt++)
		{	Lijn lijn = (Lijn) lijnVector.elementAt(lCnt);
			if (lijn.bbContains(x, y))
			{	selectedLijn = lijn;
				selectedStreep = null;
				selectedRechthoek  = null;
				selectedEllips  = null;
				selectedTekstElement  = null;
				return true; 
			}
		}
		for (int rCnt = 0; rCnt < rechthoekVector.size(); rCnt++)
		{	Rechthoek rechthoek = (Rechthoek) rechthoekVector.elementAt(rCnt);
			if (rechthoek.bbContains(x, y))
			{	selectedRechthoek = rechthoek;
				selectedStreep = null;
				selectedLijn  = null;
				selectedEllips  = null;
				selectedTekstElement  = null;
			
				return true; 
			}
		}
		for (int eCnt = 0; eCnt < ellipsVector.size(); eCnt++)
		{	Ellips ellips = (Ellips) ellipsVector.elementAt(eCnt);
			if (ellips.bbContains(x, y))
			{	selectedEllips = ellips;
				selectedStreep = null;
				selectedLijn  = null;
				selectedRechthoek  = null;
				selectedTekstElement  = null;
			
				return true; 
			}
		}
		for (int tCnt = 0; tCnt < tekstElementVector.size(); tCnt++)
		{	TekstElement tekstElement = (TekstElement) tekstElementVector.elementAt(tCnt);
			if (tekstElement.bbContains(x, y))
			{	selectedTekstElement = tekstElement;
				selectedStreep = null;
				selectedLijn  = null;
				selectedRechthoek  = null;
				selectedEllips  = null;
				return true; 
			}
		}
	
		return found;
	}
	
	
	
	public void wisObjectSelected()
	{ 
		boolean gewist = false;
		if (selectedStreep != null)
		{	streepVector.removeElement(selectedStreep);
			selectedStreep = null;
			gewist = true;
		}
		if (selectedLijn != null)  
		{	lijnVector.removeElement(selectedLijn);
			selectedLijn = null;
			gewist = true;
		}
		if (selectedRechthoek != null)  
		{	rechthoekVector.removeElement(selectedRechthoek);
			selectedRechthoek = null;
			gewist = true;
		}
		if (selectedEllips != null) 
		{	ellipsVector.removeElement(selectedEllips);
			selectedEllips = null;
			gewist = true;
		}
		if (selectedTekstElement != null)
		{	tekstElementVector.removeElement(selectedTekstElement);
			selectedTekstElement = null;
			gewist = true;
		}
		
		sleepSelectie = false;
		
		if (gewist)
			addToHistory();
		repaint();
	}
	
	
	public void translateObjectSelected(int dx, int dy)
	{ 
		if (selectedStreep != null)
			selectedStreep.translate(dx, dy);
		if (selectedLijn != null)  
			selectedLijn.translate(dx, dy);		   
		if (selectedRechthoek != null)  
			selectedRechthoek.translate(dx, dy);
		if (selectedEllips != null) 
			selectedEllips.translate(dx, dy);		
		if  (selectedTekstElement != null)
			selectedTekstElement.translate(dx, dy);
		
	}
	
	public boolean objectSelectedContains(int x, int y)
	{ 
		return ((selectedStreep != null) && selectedStreep.bbContains(x, y)) || 
			   ((selectedLijn != null) && selectedLijn.bbContains(x, y)) || 
			   ((selectedRechthoek != null) && selectedRechthoek.bbContains(x, y)) || 
			   ((selectedEllips != null) && selectedEllips.bbContains(x, y)) ||
			   ((selectedTekstElement != null) && selectedTekstElement.bbContains(x, y));
	}
	
	
	class MLMML extends MouseAdapter implements MouseMotionListener
	{
		int startX, startY;
		
		public void mousePressed(MouseEvent e)
		{
			if (mouseMode == tekenen)
			{
				//pixels[e.getX()][e.getY()] = (byte) drawingColorCode;
				draggPoints.addElement(new Point(e.getX(), e.getY()));
			}
/*			
			else if (mouseMode == gummen)
			{
				gumPunt(e.getX(), e.getY());
			}
*/			
			else if ((mouseMode == lijnTekenen) ||
					 (mouseMode == rechthoekTekenen) ||
					 (mouseMode == cirkelTekenen))
			{
				figuurStart = new Point(e.getX(), e.getY());
			}
			else if (mouseMode == tekstTekenen)
			{

/*				
				if ((tekstRechthoek != null) && tekstRechthoek.contains(e.getX(), e.getY()))
				{
					sleepTekst = true;
					startX = e.getX();
					startY = e.getY();
				}
				else
				{
*/					
					//sleepTekst = false;
				
					if (tekstVeld.isVisible())
						hideTekstVeld(false);
					tekstVeld.setLocation(e.getX(), e.getY());
					tekstVeld.setText("");
					tekstVeld.setVisible(true);
					tekstVeld.requestFocus();
					//tekstRechthoek = new Rectangle(e.getX() - tekstRand, e.getY() - tekstRand, tekstBreedte + 2 * tekstRand - 2, 
//							                                           tekstHoogte + 2 * tekstRand - 2);
				//}
			}
			else if (mouseMode == selecteren)
			{
				//if ((selecteerRechthoek != null) && selecteerRechthoek.contains(e.getX(), e.getY()))
				if (setSelectedObject(e.getX(), e.getY()) || objectSelectedContains(e.getX(), e.getY()))
				{
					sleepSelectie = true;
					startX = e.getX();
					startY = e.getY();
					//sleepPoints = copyRectangle(selecteerRechthoek);
					//wisRectangle(selecteerRechthoek);
//System.out.println("sr = " + selecteerRechthoek.toString());
					objectMoved = false;
				}
				else
				{
					sleepSelectie = false;
					resetSelectedObject();
					//figuurStart = new Point(e.getX(), e.getY());
					//selecteerRechthoek = null;
				}
			}
			repaint();
			
//			startX = e.getX();
//			startY = e.getY();
			
		}
		
		public void mouseDragged(MouseEvent e)
		{
			if (mouseMode == tekenen)
			{
				//pixels[e.getX()][e.getY()] = (byte) drawingColorCode;
				draggPoints.addElement(new Point(e.getX(), e.getY()));
			}
/*			
			else if (mouseMode == gummen)
			{
				gumPunt(e.getX(), e.getY());	
			}
*/			
			else if (mouseMode == lijnTekenen)
			{
				if (e.isShiftDown())
				{
					if ((e.getX() > figuurStart.x) && (e.getY() > figuurStart.y))
					{	
						double xZijde = (double) e.getX() - figuurStart.x;
						double yZijde = (double) e.getY() - figuurStart.y;
						int min = Math.min(e.getX() - figuurStart.x, e.getY() - figuurStart.y);
						if (yZijde > xZijde - NZERO)
						{
							if (xZijde < yZijde / 2 + NZERO)
							{
								lijnEinde = new Point(figuurStart.x, e.getY());
							}
							else
							{
								lijnEinde = new Point(figuurStart.x + min, figuurStart.y + min);
							}
						}
						else
						{
							if (yZijde > xZijde / 2 - NZERO)
							{
								lijnEinde = new Point(figuurStart.x + min, figuurStart.y + min);
							}
							else
							{
								lijnEinde = new Point(e.getX(), figuurStart.y);
							}
						}

					}	
					else if ((e.getX() > figuurStart.x) && (e.getY() < figuurStart.y))
					{	
						double xZijde = (double) e.getX() - figuurStart.x;
						double yZijde = (double) figuurStart.y - e.getY();
						int min = Math.min(e.getX() - figuurStart.x, figuurStart.y - e.getY());
						if (yZijde > xZijde + NZERO)
						{
							if (xZijde < yZijde / 2 + NZERO)
							{
								lijnEinde = new Point(figuurStart.x, e.getY());
							}
							else
							{
								lijnEinde = new Point(figuurStart.x + min, figuurStart.y - min);
							}
						}
						else
						{
							if (yZijde > xZijde / 2 - NZERO)
							{
								lijnEinde = new Point(figuurStart.x + min, figuurStart.y - min);
							}
							else
							{
								lijnEinde = new Point(e.getX(), figuurStart.y);
							}
						}

 

					}	
					else if ((e.getX() < figuurStart.x) && (e.getY() > figuurStart.y))
					{	
						double xZijde = (double) figuurStart.x - e.getX();
						double yZijde = (double) e.getY() - figuurStart.y;
						int min = Math.min(figuurStart.x - e.getX(), e.getY() - figuurStart.y);
						if (yZijde > xZijde + NZERO)
						{
							if (xZijde < yZijde / 2 + NZERO)
							{
								lijnEinde = new Point(figuurStart.x, e.getY());
							}
							else
							{
								lijnEinde = new Point(figuurStart.x - min, figuurStart.y + min);
							}
						}
						else
						{
							if (yZijde > xZijde / 2 - NZERO)
							{
								lijnEinde = new Point(figuurStart.x - min, figuurStart.y + min);
							}
							else
							{
								lijnEinde = new Point(e.getX(), figuurStart.y);
							}
						}
						
						
					}	
					else if ((e.getX() < figuurStart.x) && (e.getY() < figuurStart.y))
					{	
						int xZijde = figuurStart.x - e.getX();
						int yZijde = figuurStart.y - e.getY();
						int min = Math.min(figuurStart.x - e.getX(), figuurStart.y - e.getY());
						if (yZijde > xZijde + NZERO)
						{
							if (xZijde < yZijde / 2 + NZERO)
							{
								lijnEinde = new Point(figuurStart.x, e.getY());
							}
							else
							{
								lijnEinde = new Point(figuurStart.x - min, figuurStart.y - min);
							}
						}
						else
						{
							if (yZijde > xZijde / 2 - NZERO)
							{
								lijnEinde = new Point(figuurStart.x - min, figuurStart.y - min);
							}
							else
							{
								lijnEinde = new Point(e.getX(), figuurStart.y);
							}
						}
						        
					}
					
				}
				else
				{	
					lijnEinde = new Point(e.getX(), e.getY());
				}	
			}
			else if ((mouseMode == rechthoekTekenen) || (mouseMode == cirkelTekenen))
			{
				if (figuurStart != null)
				{
					
					if (e.isShiftDown())
					{
//System.out.println("ShiftDown");
						if ((e.getX() > figuurStart.x) && (e.getY() > figuurStart.y))
						{	
							int zijde = Math.min(e.getX() - figuurStart.x, e.getY() - figuurStart.y);
							tekenRechthoek = new Rectangle(figuurStart.x, figuurStart.y, zijde, zijde); 

						}	
						else if ((e.getX() > figuurStart.x) && (e.getY() < figuurStart.y))
						{	
							int zijde = Math.min(e.getX() - figuurStart.x, figuurStart.y - e.getY());
							tekenRechthoek = new Rectangle(figuurStart.x, e.getY(), zijde, zijde); 
 
						}	
						else if ((e.getX() < figuurStart.x) && (e.getY() > figuurStart.y))
						{	
							int zijde = Math.min(figuurStart.x - e.getX(), e.getY() - figuurStart.y);
							tekenRechthoek = new Rectangle(e.getX(), figuurStart.y, zijde, zijde);
							
						}	
						else if ((e.getX() < figuurStart.x) && (e.getY() < figuurStart.y))
						{	
							int zijde = Math.min(figuurStart.x - e.getX(), figuurStart.y - e.getY());
							tekenRechthoek = new Rectangle(e.getX(), e.getY(), zijde, zijde); 
							        
						}
					}
					else
					{	
						if ((e.getX() > figuurStart.x) && (e.getY() > figuurStart.y))
						{	tekenRechthoek = new Rectangle(figuurStart.x, figuurStart.y, 
							                           	   e.getX() - figuurStart.x, e.getY() - figuurStart.y); 
						}	
						else if ((e.getX() > figuurStart.x) && (e.getY() < figuurStart.y))
						{	tekenRechthoek = new Rectangle(figuurStart.x, e.getY(), 
								                           e.getX() - figuurStart.x, figuurStart.y - e.getY()); 
						}	
						else if ((e.getX() < figuurStart.x) && (e.getY() > figuurStart.y))
						{	tekenRechthoek = new Rectangle(e.getX(), figuurStart.y, 
													       figuurStart.x - e.getX(), e.getY() - figuurStart.y); 
						}	
						else if ((e.getX() < figuurStart.x) && (e.getY() < figuurStart.y))
						{	tekenRechthoek = new Rectangle(e.getX(), e.getY(), 
													       figuurStart.x - e.getX(), figuurStart.y - e.getY()); 
						}
					}
				}	
				
			}
			else if (mouseMode == tekstTekenen)
			{
/*				
				if (sleepTekst) // verplaats de tekstRechthoek en het tekstVeld!!
				{	
				
					int dx = e.getX() - startX;
					int dy = e.getY() - startY;
					tekstRechthoek.translate(dx, dy);
					tekstVeld.setLocation(tekstVeld.getLocation().x + dx, tekstVeld.getLocation().y + dy);
					
					startX = e.getX();
					startY = e.getY();
				
				}
*/				
/*				
				if ((e.getX() > figuurStart.x) && (e.getY() > figuurStart.y))
				{	tekstRechthoek = new Rectangle(figuurStart.x, figuurStart.y, 
					                           	   e.getX() - figuurStart.x, e.getY() - figuurStart.y); 
				}	
				else if ((e.getX() > figuurStart.x) && (e.getY() < figuurStart.y))
				{	tekstRechthoek = new Rectangle(figuurStart.x, e.getY(), 
						                           e.getX() - figuurStart.x, figuurStart.y - e.getY()); 
				}	
				else if ((e.getX() < figuurStart.x) && (e.getY() > figuurStart.y))
				{	tekstRechthoek = new Rectangle(e.getX(), figuurStart.y, 
											       figuurStart.x - e.getX(), e.getY() - figuurStart.y); 
				}	
				else if ((e.getX() < figuurStart.x) && (e.getY() < figuurStart.y))
				{	tekstRechthoek = new Rectangle(e.getX(), e.getY(), 
											       figuurStart.x - e.getX(), figuurStart.y - e.getY()); 
				}
*/				
			}
			else if (mouseMode == selecteren)
			{
				if (sleepSelectie) // verplaats de selecteerRechthoek met inhoud!!
				{	
					int dx = e.getX() - startX;
					int dy = e.getY() - startY;
					//selecteerRechthoek.translate(dx, dy);
					//figuurStart.translate(e.getX() - figuurStart.x, figuurStart.y - e.getY());
					translateObjectSelected(dx, dy);
/*					
					for (int pCnt = 0; pCnt < sleepPoints.size(); pCnt++)
					{	ColorBytes cb = (ColorBytes) sleepPoints.elementAt(pCnt);
						translateColorBytes(cb, dx, dy);
					}
*/						
					startX = e.getX();
					startY = e.getY();
					
					objectMoved = true;
					
				}
/*				
				else // sleepSelectie, vorm de selecteerRechthoek
				{	
					if ((e.getX() > figuurStart.x) && (e.getY() > figuurStart.y))
					{	selecteerRechthoek = new Rectangle(figuurStart.x, figuurStart.y, 
							e.getX() - figuurStart.x, e.getY() - figuurStart.y); 
					}	
					else if ((e.getX() > figuurStart.x) && (e.getY() < figuurStart.y))
					{	selecteerRechthoek = new Rectangle(figuurStart.x, e.getY(), 
					        e.getX() - figuurStart.x, figuurStart.y - e.getY()); 
					}	
					else if ((e.getX() < figuurStart.x) && (e.getY() > figuurStart.y))
					{	selecteerRechthoek = new Rectangle(e.getX(), figuurStart.y, 
					       figuurStart.x - e.getX(), e.getY() - figuurStart.y); 
					}	
					else if ((e.getX() < figuurStart.x) && (e.getY() < figuurStart.y))
					{	selecteerRechthoek = new Rectangle(e.getX(), e.getY(), 
					       figuurStart.x - e.getX(), figuurStart.y - e.getY()); 
					}
				}
*/				
			}
			
			repaint();
			
//			startX = e.getX();
//			startY = e.getY();
			
		}
		
		public void mouseReleased(MouseEvent e)
		{
			if (mouseMode == tekenen)
			{	
				//updatePixelArray();
				Streep streep = new Streep(drawingColor, draggPoints);
				streepVector.addElement(streep);
				if (draggPoints.size() > 1)
					addToHistory();
				draggPoints.removeAllElements();
				repaint();
			}
/*			
			else if (mouseMode == gummen)
			{
				updatePixelArray();
				addToHistory();
			}
*/			
			else if (mouseMode == lijnTekenen)
			{	
				//updatePixelArray();
				if (lijnEinde != null)
				{	
					Lijn lijn = new Lijn(drawingColor, figuurStart.x, figuurStart.y, lijnEinde.x, lijnEinde.y);
					lijnVector.addElement(lijn);
				}
				figuurStart = null;
				lijnEinde = null;
				tekenRechthoek = null;
				
				addToHistory();
				repaint();
			}
			else if (mouseMode == rechthoekTekenen)
			{	
				//updatePixelArray();
				if (tekenRechthoek != null)
				{	
					Rechthoek rechthoek = new Rechthoek(drawingColor, 
													tekenRechthoek.x, tekenRechthoek.y,
													tekenRechthoek.width, tekenRechthoek.height);
					rechthoekVector.addElement(rechthoek);
				}
				
				figuurStart = null;
				lijnEinde = null;
				tekenRechthoek = null;
				
				addToHistory();
				repaint();				
			}
			else if (mouseMode == cirkelTekenen)
			{	
				//updatePixelArray();
				
				if (tekenRechthoek != null)
				{
					Ellips ellips = new Ellips(drawingColor, 
									       tekenRechthoek.x, tekenRechthoek.y,
										   tekenRechthoek.width, tekenRechthoek.height);
					ellipsVector.addElement(ellips);				
				}
				figuurStart = null;
				lijnEinde = null;
				tekenRechthoek = null;
				
				addToHistory();
				repaint();				
			}
			
			else if (mouseMode == tekstTekenen)
			{
				
			}
			else if (mouseMode == selecteren)
			{
				//updatePixelArray();
				if (sleepSelectie)
				{	//sleepSelectie = false;
					//updatePixelArray();
/*					
					for (int pCnt = 0; pCnt < sleepPoints.size(); pCnt++)
					{	ColorBytes cb = (ColorBytes) sleepPoints.elementAt(pCnt);
						if ((cb.x < breedte) && (cb.y < hoogte))
							pixels[cb.x][cb.y] = cb;
					}
					
					sleepPoints.removeAllElements();
*/					

					sleepSelectie = false;
					//resetSelectedObject();
					if (objectMoved)
						addToHistory();
					objectMoved = false;
					//sleepSelectie = false;
					repaint();
				}
			}
			//addToHistory();
		}
		
		public void mouseMoved(MouseEvent e)
		{
			if (mouseMode == tekstTekenen)
			{
/*				
				if ((tekstRechthoek != null) && tekstRechthoek.contains(e.getX(), e.getY()))
				{
					setCursor(new Cursor(Cursor.MOVE_CURSOR));
				}
				else
				{
					if (textCursor != null)
						setCursor(textCursor);
					else
						setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
				}
*/				
				
			}
			
			if (mouseMode == selecteren)
			{
				
/*				
				if ((selecteerRechthoek != null) && selecteerRechthoek.contains(e.getX(), e.getY()))
				{
					setCursor(new Cursor(Cursor.MOVE_CURSOR));
				}
				else
				{
					if (selectCursor != null)
						setCursor(selectCursor);
					else
						setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
				}
*/				
				
			}
				
		}
		
		void translateColorBytes(ColorBytes cb, int dx, int dy)
		{
			cb.x += dx;
			cb.y += dy;
		}
	} //MLMML
	
	class TekstAL implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			hideTekstVeld(false);
		}
	}
}


class ColorBytes implements Serializable
{	
	int x, y;
	byte red, green, blue;
	
	public ColorBytes(int x, int y, int r, int g, int b)
	{	
		this.x = x;
		this.y = y;
		
		red = new Integer(r).byteValue(); 
		green = new Integer(g).byteValue();
		blue = new Integer(b).byteValue();;
	}
	
	public ColorBytes(int x, int y, Color c)
	{
		this.x = x;
		this.y = y;
		
		red = new Integer(c.getRed()).byteValue(); 
		green = new Integer(c.getGreen()).byteValue();
		blue = new Integer(c.getBlue()).byteValue();;
	}

	public Color makeColor()
	{	int intRed = red;
		if (intRed < 0)
			intRed += 256;
		int intGreen = green;
		if (intGreen < 0)
			intGreen += 256;
		int intBlue = blue;
		if (intBlue < 0)
			intBlue += 256;
		return new Color(intRed, intGreen, intBlue);
	}
	
	public void zetColor(Color c)
	{
		red = new Integer(c.getRed()).byteValue(); 
		green = new Integer(c.getGreen()).byteValue();
		blue = new Integer(c.getBlue()).byteValue();;
		
	}
}