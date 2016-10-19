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
	
	static boolean roteren = true;
	static boolean schalen = true;
	
	boolean handleAction = false;
	boolean groupHandleAction = false;
	boolean scalingTopRight = false;
	boolean scalingTopLeft = false;
	boolean scalingBottomRight = false;
	boolean scalingBottomLeft = false;
	boolean rotatingEast = false;
	boolean rotatingWest = false;
	
	int lineDistance = 20;
	int gridSize = 20;
	
	static Color veryLightBlue = new Color(220, 220, 255);
	static Color lightBlue = new Color(148, 148, 255);
	Color lijnenKleur = new Color(150, 150, 255);
	Color ruitjesKleur = new Color(210, 210, 210);
	static Color bbColor = lightBlue; 
	static Color hbColor = Color.blue;
	
	static int minHandleBoxSize = 50;
	
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
	
	final int GAUSSIAN = 0;
	final int AVERAGE = 1;
	final int AVERAGE2 = 2;
	int smoothType = AVERAGE2;
	//Vector draggPoints = new Vector();
	ArrayList<DoublePoint> draggDoublePoints = new ArrayList<DoublePoint>();
	//Vector gumPunten = new Vector();
	//int gumGrootte = 7; // oneven	
	Point figuurStart = null;
	Point lijnEinde = null;
	Rectangle tekenRechthoek = null;
//	Rectangle tekstRechthoek = null;
	Rectangle selecteerRechthoek = null;
	TekstElement tekstEdited = null;
	
	Polygon topRightHandle, bottomRightHandle, topLeftHandle, bottomLeftHandle;
	Rectangle topRightRect, bottomRightRect, topLeftRect, bottomLeftRect;
	Rectangle rotateEastHandle, rotateWestHandle;
	int hbFactor = 4;

	// backwards compatibility
	ColorBytes[][] pixels = null;
	int breedte, hoogte;
	Vector streepVector = new Vector();
	Vector lijnVector = new Vector();
	Vector rechthoekVector = new Vector();
	Vector ellipsVector = new Vector();
	Vector tekstElementVector = new Vector();
	
	Vector<Streep> docentStreepVector = new Vector<Streep>();
	Vector<Lijn> docentLijnVector = new Vector<Lijn>();
	Vector<Rechthoek> docentRechthoekVector = new Vector<Rechthoek>();
	Vector<Ellips> docentEllipsVector = new Vector<Ellips>();
	Vector<TekstElement> docentTekstElementVector = new Vector<TekstElement>();

	
	//Image offScreen = null;
	//Graphics offGraphics = null;
	
	int maxHistories = 5;
	int numHistories = 0;
	Hashtable[] histories = new Hashtable[maxHistories + 1];

	boolean shiftPressed = false;
	
	Cursor selectCursor = null;
	boolean sleepSelectie = false;
	boolean objectMoved = false;
	boolean objectHandled = false;
	//Vector sleepPoints = new Vector();
	
	Streep selectedStreep = null;
	Lijn selectedLijn = null;
	Rechthoek selectedRechthoek = null;
	Ellips selectedEllips = null;
	TekstElement selectedTekstElement = null;
	TekstElement oldSelectedTekstElement = null;
	Vector objectsSelected = new Vector();
	
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
	
	double rotateStep = Math.PI / 18; // 10 degrees in radians
	double angleSum = 0;
	double scaleUpStep = 105e-2d;
	double scaleDownStep = 1 / 105e-2d;
	
	boolean undo = false;
	
	boolean initialState = false;
	
	//Graphics kvGraphics = null;
	
	
	public KladjeVeld(int w, int h)
	{
		breedte = w;
		hoogte = h;
		setSize(breedte, hoogte);
		
		setOpaque(false);
		
		tekstFont = new Font("Sanserif", Font.BOLD, 14);
		//tekenTekstFont = new Font("Sanserif", Font.PLAIN, 14);
		tekstFM = getFontMetrics(tekstFont);
		//tekstHoogte = 4 * tekstFM.getHeight() / 3;
		tekstHoogte = tekstFM.getHeight() + 10;

		tekstVeld = new JTextField();
		tekstVeld.setFont(tekstFont);
		tekstVeld.setBackground(veryLightBlue);
		tekstVeld.setSize(tekstBreedte, tekstHoogte);
		tekstVeld.setVisible(false);
		add(tekstVeld);
		tekstVeld.addActionListener(new TekstAL());
		tekstVeld.addKeyListener(new TekstKL());
		
		MLMML listener = new MLMML();
		
		addMouseListener(listener);
		addMouseMotionListener(listener);
		
	}

	public void hideTekstVeld(boolean empty)
	{
		tekstString = tekstVeld.getText();
		tekstX = tekstVeld.getLocation().x + 2;
		tekstY = tekstVeld.getLocation().y;// + tekstFM.getAscent();
		//tekstRechthoek = null;
		
		tekstVeld.setVisible(false);

		if (!tekstString.equals("") && (tekstEdited == null))
		{
			TekstElement tekstElement = 
				new TekstElement(drawingColor, tekstString, tekstX, tekstY);
			tekstElementVector.addElement(tekstElement);
			addToHistory();
			repaint();
		}
		else if (!tekstString.equals("") && (tekstEdited != null))
		{
			tekstEdited.zetTekst(tekstString);
			//tekstEdited.tekst = tekstString;
			addToHistory();
			repaint();
			tekstEdited = null;
		}
		else if (tekstString.equals("") && (tekstEdited != null))
		{
			tekstElementVector.removeElement(tekstEdited);
			addToHistory();
			repaint();
		}
		
		//drawTekstString();
	
		if (empty)
		{	
			tekstString = "";
			tekstVeld.setText("");
		
		}
		
	}
	
	void addToHistory()
	{
		
//System.out.println("ath = " + numHistories);		

		Hashtable stateTable = getState();
		
		histories[numHistories] = stateTable;
		numHistories++;
		
		if (numHistories > maxHistories)
		{	for (int i = 0; i < numHistories - 1; i++)
			{	histories[i] = histories[i + 1];
			}
			numHistories--;
		}
		
//System.out.println("ath " + numHistories);		
	}
	
	public Hashtable getFromHistory()
	{	//if (numHistories <= 1)
		//	return null;
//System.out.println("initialState " + initialState);

		Hashtable hTable = null;
		
		if (numHistories >= 2)
		{
			hTable = histories[numHistories - 2];
			numHistories--;
		}
		else if ((numHistories == 1) && initialState)
		{
			hTable = histories[0];
			//numHistories--;
		}
		
			
/*		
		if (numHistories > 0)
			numHistories--;
		
//System.out.println("gfh " + numHistories);

		if (numHistories > 0)
		{	
System.out.println("returned " + (numHistories - 1));			
			return histories[numHistories - 1];
		
		}
		else
		{	numHistories = 0;
//System.out.println("returned null " + numHistories);		
		
			return null;
		}
*/		
		return hTable;
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

	public Hashtable getState()
	{
		
//System.out.println("kv getState");

		Hashtable h = new Hashtable();
		
		// backwards compatibility
//		Vector gwtStateVector = getGWTState();
//		if (gwtStateVector.size() > 0)
//		{	h.put("gwtpixels", gwtStateVector);
//System.out.println("put gwtpixels");		
//		}
		
		//if (!full)
		//	removeUndeletableStrepen();
		Hashtable[] strepen = new Hashtable[streepVector.size()];
		for (int sCnt = 0; sCnt < streepVector.size(); sCnt++)
		{	Streep streep = (Streep) streepVector.elementAt(sCnt);
			strepen[sCnt] = streep.getState();
		}
		h.put("strepen", strepen);

		//if (!full)
		//	removeUndeletableLijnen();
		Hashtable[] lijnenHash = new Hashtable[lijnVector.size()];
		for (int lCnt = 0; lCnt < lijnVector.size(); lCnt++)
		{	Lijn lijn = (Lijn) lijnVector.elementAt(lCnt);
			lijnenHash[lCnt] = lijn.getState();
		}
		h.put("lijnenhash", lijnenHash);
		
		//if (!full)
		//	removeUndeletableRechthoeken();
		Hashtable[] rechthoeken = new Hashtable[rechthoekVector.size()];
		for (int rCnt = 0; rCnt < rechthoekVector.size(); rCnt++)
		{	Rechthoek rechthoek = (Rechthoek) rechthoekVector.elementAt(rCnt);
			rechthoeken[rCnt] = rechthoek.getState();
		}
		h.put("rechthoeken", rechthoeken);
		
		//if (!full)
		//	removeUndeletableEllipsen();
		Hashtable[] ellipsen = new Hashtable[ellipsVector.size()];
		for (int eCnt = 0; eCnt < ellipsVector.size(); eCnt++)
		{	Ellips ellips = (Ellips) ellipsVector.elementAt(eCnt);
			ellipsen[eCnt] = ellips.getState();
		}
		h.put("ellipsen", ellipsen);
		
		//if (!full)
		//	removeUndeletableTekstElementen();
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

	public void setState(Hashtable h, boolean init)
	{
		
//System.out.println("kv setState");

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
		if (init)
		{	streepVector.removeAllElements();
			docentStreepVector.removeAllElements();
		}
		else
			streepVector.removeAllElements();
		Hashtable[] strepen = new Hashtable[0];
		if (h.containsKey("strepen"))
			strepen = (Hashtable[]) h.get("strepen");
		for (int sCnt = 0; sCnt < strepen.length; sCnt++)
		{	Streep streep = Streep.setState(strepen[sCnt]);
			//streep.deletable = !init;
			if (init)
				docentStreepVector.addElement(streep);
			else
				streepVector.addElement(streep);
		}

		if (init)
		{	lijnVector.removeAllElements();
			docentLijnVector.removeAllElements();
		}
		else
			lijnVector.removeAllElements();
		Hashtable[] lijnenHash = new Hashtable[0];
		if (h.containsKey("lijnenhash"))
			lijnenHash = (Hashtable[]) h.get("lijnenhash");
		for (int lCnt = 0; lCnt < lijnenHash.length; lCnt++)
		{	Lijn lijn = Lijn.setState(lijnenHash[lCnt]);
			//lijn.deletable = !init;
			if (init)
				docentLijnVector.addElement(lijn);
			else
				lijnVector.addElement(lijn);
		}

		if (init)
		{	rechthoekVector.removeAllElements();
			docentRechthoekVector.removeAllElements();
		}
		else
			rechthoekVector.removeAllElements();
		Hashtable[] rechthoeken = new Hashtable[0];
		if (h.containsKey("rechthoeken"))
			rechthoeken = (Hashtable[]) h.get("rechthoeken");
		for (int rCnt = 0; rCnt < rechthoeken.length; rCnt++)
		{	Rechthoek rechthoek = Rechthoek.setState(rechthoeken[rCnt]);
			//rechthoek.deletable = !init;
			if (init)
				docentRechthoekVector.addElement(rechthoek);
			else
				rechthoekVector.addElement(rechthoek);
		}

		if (init)
		{	ellipsVector.removeAllElements();
			docentEllipsVector.removeAllElements();
		}
		else
			ellipsVector.removeAllElements();
		Hashtable[] ellipsen = new Hashtable[0];
		if (h.containsKey("ellipsen"))
			ellipsen = (Hashtable[]) h.get("ellipsen");
		for (int eCnt = 0; eCnt < ellipsen.length; eCnt++)
		{	Ellips ellips = Ellips.setState(ellipsen[eCnt]);
			//ellips.deletable = !init;
			if (init)
				docentEllipsVector.addElement(ellips);
			else
				ellipsVector.addElement(ellips);
				
		}

		if (init)
		{	tekstElementVector.removeAllElements();
			docentTekstElementVector.removeAllElements();
		}
		else
			tekstElementVector.removeAllElements();
		Hashtable[] tekstElementen = new Hashtable[0];
		if (h.containsKey("tekstElementen"))
			tekstElementen = (Hashtable[]) h.get("tekstElementen");
		for (int tCnt = 0; tCnt < tekstElementen.length; tCnt++)
		{	TekstElement tekstElement = TekstElement.setState(tekstElementen[tCnt]);
			//tekstElement.deletable = !init;
			if (init)
				docentTekstElementVector.addElement(tekstElement);
			else
				tekstElementVector.addElement(tekstElement);
		}
		
/*		
		if (!undo && !initialState)
		{	addToHistory();
			initialState = true;
		}
*/		
		//paint();
	}
	
	public void setOldState(Vector stateVector)
	{
//System.out.println("kladjeVeld setState " + stateVector.size());

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
//System.out.println("kladjeVeld setGWTState " + gwtStateVector.size());

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
	{	lineDistance = 20;
		lijnen = b;
		repaint();
	}

	public void zetRuitjes(boolean b, int size)
	{	lineDistance = size;	
		ruitjes = b;
		repaint();
	}
	
	public void zetSchalen(boolean b)
	{	schalen = b;
		
		for (int sCnt = 0; sCnt < streepVector.size(); sCnt++)
		{	Streep streep = (Streep) streepVector.elementAt(sCnt);
			if (b)
				streep.makeScaleHandles();
			else
				streep.killScaleHandles();
		}
		for (int lCnt = 0; lCnt < lijnVector.size(); lCnt++)
		{	Lijn lijn = (Lijn) lijnVector.elementAt(lCnt);
			if (b)
				lijn.makeScaleHandles();
			else	
				lijn.killScaleHandles();
		}
		for (int rCnt = 0; rCnt < rechthoekVector.size(); rCnt++)
		{	Rechthoek rechthoek = (Rechthoek) rechthoekVector.elementAt(rCnt);
			if (b)
				rechthoek.makeScaleHandles();
			else	
				rechthoek.killScaleHandles();
		}
		for (int eCnt = 0; eCnt < ellipsVector.size(); eCnt++)
		{	Ellips ellips = (Ellips) ellipsVector.elementAt(eCnt);
			if (b)
				ellips.makeScaleHandles();
			else	
				ellips.killScaleHandles();
		}
		for (int tCnt = 0; tCnt < tekstElementVector.size(); tCnt++)
		{	TekstElement tekstElement = (TekstElement) tekstElementVector.elementAt(tCnt);
			if (b)
				tekstElement.makeScaleHandles();
			else
				tekstElement.killScaleHandles();
		}
		
		if (b)
			makeScaleHandles();
		else
			killScaleHandles();
		
		repaint();
	}

	public void zetRoteren(boolean b)
	{
		roteren = b;
		
		for (int sCnt = 0; sCnt < streepVector.size(); sCnt++)
		{	Streep streep = (Streep) streepVector.elementAt(sCnt);
			if (b)
				streep.makeRotateHandles();
			else
				streep.killRotateHandles();
		}
		for (int lCnt = 0; lCnt < lijnVector.size(); lCnt++)
		{	Lijn lijn = (Lijn) lijnVector.elementAt(lCnt);
			if (b)
				lijn.makeRotateHandles();
			else	
				lijn.killRotateHandles();
		}
		for (int rCnt = 0; rCnt < rechthoekVector.size(); rCnt++)
		{	Rechthoek rechthoek = (Rechthoek) rechthoekVector.elementAt(rCnt);
			if (b)
				rechthoek.makeRotateHandles();
			else	
				rechthoek.killRotateHandles();
		}
		for (int eCnt = 0; eCnt < ellipsVector.size(); eCnt++)
		{	Ellips ellips = (Ellips) ellipsVector.elementAt(eCnt);
			if (b)
				ellips.makeRotateHandles();
			else	
				ellips.killRotateHandles();
		}
		for (int tCnt = 0; tCnt < tekstElementVector.size(); tCnt++)
		{	TekstElement tekstElement = (TekstElement) tekstElementVector.elementAt(tCnt);
			if (b)
				tekstElement.makeRotateHandles();
			else
				tekstElement.killRotateHandles();
		}
		
		if (b)
			makeRotateHandles();
		else 
			killRotateHandles();
		
		repaint();
	}
	
	public ArrayList<DoublePoint> gaussianSmooth(ArrayList<DoublePoint> doublePoints)
	{
		if (doublePoints.size() < 3) 
			return doublePoints;
		ArrayList<DoublePoint> pointsNew = new ArrayList<DoublePoint>();
		pointsNew.add(doublePoints.get(0));		
		for (int i = 1; i < doublePoints.size() - 1; i++)
		{
			DoublePoint pOld0 = doublePoints.get(i-1);
			DoublePoint pOld1 = doublePoints.get(i);
			DoublePoint pOld2 = doublePoints.get(i+1);
			DoublePoint smoothedPoint = new DoublePoint(pOld0.x / 4 + pOld1.x / 2 + pOld2.x / 4,
														pOld0.y / 4 + pOld1.y / 2 + pOld2.y / 4);
			pointsNew.add(smoothedPoint);
		}
		pointsNew.add(doublePoints.get(doublePoints.size() - 1));
		
		return pointsNew;
		
	}

	public ArrayList<DoublePoint> averageSmooth(ArrayList<DoublePoint> doublePoints)
	{
		if (doublePoints.size() < 5) 
			return doublePoints;
		ArrayList<DoublePoint> pointsNew = new ArrayList<DoublePoint>();
		pointsNew.add(doublePoints.get(0));		
		pointsNew.add(doublePoints.get(1));
		for (int i = 2; i < doublePoints.size() - 2; i++)
		{
			DoublePoint pOld0 = doublePoints.get(i-2);
			DoublePoint pOld1 = doublePoints.get(i-1);
			DoublePoint pOld2 = doublePoints.get(i);
			DoublePoint pOld3 = doublePoints.get(i+1);
			DoublePoint pOld4 = doublePoints.get(i+2);
			
			DoublePoint smoothedPoint = new DoublePoint(pOld0.x/5 + pOld1.x/5 + pOld2.x/5 + pOld3.x/5 + pOld4.x/5,
														pOld0.y/5 + pOld1.y/5 + pOld2.y/5 + pOld3.y/5 + pOld4.y/5);
			pointsNew.add(smoothedPoint);
		}
		pointsNew.add(doublePoints.get(doublePoints.size() - 1));
		
		return pointsNew;
		
	}

	public ArrayList<DoublePoint> smooth(ArrayList<DoublePoint> doublePoints, int smoothType)
	{
		if (smoothType == GAUSSIAN)
			return gaussianSmooth(doublePoints);
		else if (smoothType == AVERAGE)
			return averageSmooth(doublePoints);
		else if (smoothType == AVERAGE2)
		{	ArrayList<DoublePoint> oneSmooth = averageSmooth(doublePoints);
			return averageSmooth(oneSmooth);			
		}
		else
			return doublePoints;
	}
	
///	public void paint()
//	{
//		if (kvGraphics != null)
//		{	paintComponent(kvGraphics);
//System.out.println("paint");		
//		}
//	}
	
	public void paintComponent(Graphics g)
	{
		boolean firstPaint = false;
		
		//if (kvGraphics == null)
		//{	kvGraphics = getGraphics();
		//	firstPaint = true;
		//}
		
		Graphics2D g2D = (Graphics2D) g;		

		//g2D.setColor(backgroundColor);
		//g2D.fillRect(0, 0, getSize().width, getSize().height);
		
		if (lijnen)
		{
			g2D.setColor(lijnenKleur);
			int steps = getSize().height / lineDistance;
			for (int lCnt = 1; lCnt <= steps; lCnt++)
			{
				g2D.drawLine(0, lCnt * lineDistance, getSize().width - 1, lCnt * lineDistance);
			}
			
		}
		if (ruitjes)
		{
			g2D.setColor(ruitjesKleur);
			int vSteps = getSize().height / lineDistance;
			for (int vCnt = 1; vCnt <= vSteps; vCnt++)
			{
				g2D.drawLine(0, vCnt * lineDistance, getSize().width - 1, vCnt * lineDistance);
			}
			int hSteps = getSize().width / lineDistance;
			for (int hCnt = 1; hCnt <= hSteps; hCnt++)
			{
				g2D.drawLine(hCnt * lineDistance, 0, hCnt * lineDistance, getSize().height - 5);
			}
			
		}

		g2D.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL,RenderingHints.VALUE_STROKE_NORMALIZE);
		g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		BasicStroke stroke = new BasicStroke(1.5f);
		
		g2D.setStroke(new BasicStroke(1.5f));
		
		tekenProgramma(g2D, false);
		
		//if (firstPaint)
		//	paintComponent(g);
	}
	
	
	void tekenProgramma(Graphics2D g, boolean wis)
	{
		
//System.out.println("tekenProgramma");

//wis is altijd false		
		if (wis)
		{	
			g.setColor(backgroundColor);
			g.fillRect(0, 0, getSize().width, getSize().height);
		
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
		
		// elementen docent
		for (int sCnt = 0; sCnt < docentStreepVector.size(); sCnt++)
		{	Streep streep = (Streep) docentStreepVector.elementAt(sCnt);
			streep.teken(g);
		}
		for (int lCnt = 0; lCnt < docentLijnVector.size(); lCnt++)
		{	Lijn lijn = (Lijn) docentLijnVector.elementAt(lCnt);
			lijn.teken(g);
		}
		for (int rCnt = 0; rCnt < docentRechthoekVector.size(); rCnt++)
		{	Rechthoek rechthoek = (Rechthoek) docentRechthoekVector.elementAt(rCnt);
			rechthoek.teken(g);
		}
		for (int eCnt = 0; eCnt < docentEllipsVector.size(); eCnt++)
		{	Ellips ellips = (Ellips) docentEllipsVector.elementAt(eCnt);
			ellips.teken(g);
		}
		for (int tCnt = 0; tCnt < docentTekstElementVector.size(); tCnt++)
		{	TekstElement tekstElement = (TekstElement) docentTekstElementVector.elementAt(tCnt);
			tekstElement.teken(g);
		}

		// elementen leerling
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

/*		
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
*/		
		
		if (draggDoublePoints.size() == 1)
		{	Point p = draggDoublePoints.get(0).getPoint();
			g.drawLine(p.x, p.y, p.x, p.y);
		}
		if (draggDoublePoints.size() > 1)
		{	
			ArrayList<DoublePoint> smoothedDraggDoublePoints = smooth(draggDoublePoints, smoothType);
									
			Point p1 = smoothedDraggDoublePoints.get(0).getPoint();
			for (int pCnt = 1; pCnt < smoothedDraggDoublePoints.size(); pCnt++)
			{	Point p2 = smoothedDraggDoublePoints.get(pCnt).getPoint();
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
		
		
		// alleen op het scherm
		if ((mouseMode == selecteren) && (selecteerRechthoek != null))// && !sleepSelectie && !wis)
		{
			Graphics2D g2D = (Graphics2D) g;
			float[] dash = new float[2];
			dash[0] = 5;
			dash[1] = 5;
			g2D.setStroke(new BasicStroke(1.2f, 2, 0, 10.0f, dash, 0.0f));
			g.setColor(Color.blue);
			g.drawRect(selecteerRechthoek.x, selecteerRechthoek.y, selecteerRechthoek.width, selecteerRechthoek.height);
			g2D.setStroke(new BasicStroke(1.5f, 2, 0, 10.0f, null, 0.0f));
			
			if (schalen)
			{	
				if (topRightHandle != null)
				{	g.setColor(hbColor);
					g.drawPolygon(topRightHandle);
	//g.setColor(Color.red);
	//g.drawRect(topRightRect.x,topRightRect.y,topRightRect.width,topRightRect.height);
				}
				if (topLeftHandle != null)
				{	g.setColor(hbColor);
					g.drawPolygon(topLeftHandle);
	//g.setColor(Color.red);
	//g.drawRect(topLeftRect.x,topLeftRect.y,topLeftRect.width,topLeftRect.height);
				
				}
				if (bottomRightHandle != null)
				{	g.setColor(hbColor);
					g.drawPolygon(bottomRightHandle);
	//g.setColor(Color.red);
	//g.drawRect(bottomRightRect.x,bottomRightRect.y,bottomRightRect.width,bottomRightRect.height);
				
				}
				if (bottomLeftHandle != null)
				{	g.setColor(hbColor);
					g.drawPolygon(bottomLeftHandle);
	//g.setColor(Color.red);
	//g.drawRect(bottomLeftRect.x,bottomLeftRect.y,bottomLeftRect.width,bottomLeftRect.height);
				
				}
			} // if schalen
			if (roteren)
			{
				if (rotateEastHandle != null)
				{	g.setColor(KladjeVeld.hbColor);
					g.drawOval(selecteerRechthoek.x + selecteerRechthoek.width, // - 2 * hbFactor,
							   selecteerRechthoek.y + selecteerRechthoek.height/2 - 2 * hbFactor, 
							   4 * hbFactor, 4 * hbFactor);
		//g.setColor(Color.red);			
		//g.drawRect(rotateEastHandle.x, rotateEastHandle.y, rotateEastHandle.width, rotateEastHandle.height);
				}
				if (rotateWestHandle != null)
				{	g.setColor(KladjeVeld.hbColor);
					g.drawOval(selecteerRechthoek.x - 4 * hbFactor, 
							   selecteerRechthoek.y + selecteerRechthoek.height/2 - 2 * hbFactor, 
							   4 * hbFactor, 4 * hbFactor);
		//g.setColor(Color.red);			
		//g.drawRect(rotateWestHandle.x, rotateWestHandle.y, rotateWestHandle.width, rotateWestHandle.height);
				}

			}
			
		}

		if (mouseMode == selecteren)
		{
			if (selectedStreep != null)
			{	selectedStreep.tekenHandleBox(g);
				//selectedStreep.tekenBB(g);
			}
			if (selectedLijn != null)
			{	//selectedLijn.tekenBB(g);
				selectedLijn.tekenHandleBox(g);
			}
			if (selectedRechthoek != null)
			{	//selectedRechthoek.tekenBB(g);
				selectedRechthoek.tekenHandleBox(g);
			}
			if (selectedEllips != null)
			{	//selectedEllips.tekenBB(g);
				selectedEllips.tekenHandleBox(g);
			}
			if (selectedTekstElement != null)
			{	selectedTekstElement.tekenBB(g);
				selectedTekstElement.tekenHandleBox(g);
			}
			
			for (int oCnt = 0; oCnt < objectsSelected.size(); oCnt++)
			{
				Object o = (Object) objectsSelected.elementAt(oCnt);
				if (o instanceof Streep)
					((Streep) o).tekenBB(g);
				else if (o instanceof Lijn)
					((Lijn) o).tekenBB(g);
				else if (o instanceof Rechthoek)
					((Rechthoek) o).tekenBB(g);
				else if (o instanceof Ellips)
					((Ellips) o).tekenBB(g);
				else if (o instanceof TekstElement)
					((TekstElement) o).tekenBB(g);
				
			}
		}
	}
	
	
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
		{	
			undo = true;
			setState(lastState, false);
			undo = false;
		}

		repaint();
	}
	
	void wis(boolean complete)
	{	
		if ((mouseMode == selecteren) && objectSelected())
		{
			wisObjectSelected();
			
		}
		else if ((mouseMode == selecteren) && (selecteerRechthoek != null))
		{
			wisObjectsSelected();
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
			//removeDeletableStrepen();
			//removeDeletableLijnen();
			//removeDeletableRechthoeken();
			//removeDeletableEllipsen();
			//removeDeletableTekstElementen();

			
			if (complete)
				numHistories = 0;
		}	
		repaint();
	}
	
	public void removeDeletableStrepen()
	{
		for (int cnt = streepVector.size()-1; cnt >= 0; cnt--)
		{
			Streep streep = (Streep) streepVector.elementAt(cnt);
			if (streep.deletable)
				streepVector.removeElementAt(cnt);
		}
	}
	
	public void removeDeletableLijnen()
	{
		for (int cnt = lijnVector.size()-1; cnt >= 0; cnt--)
		{
			Lijn lijn = (Lijn) lijnVector.elementAt(cnt);
			if (lijn.deletable)
				lijnVector.removeElementAt(cnt);
		}
	}

	public void removeDeletableRechthoeken()
	{
		for (int cnt = rechthoekVector.size()-1; cnt >= 0; cnt--)
		{
			Rechthoek rechthoek = (Rechthoek) rechthoekVector.elementAt(cnt);
			if (rechthoek.deletable)
				rechthoekVector.removeElementAt(cnt);
		}
	}

	public void removeDeletableEllipsen()
	{
		for (int cnt = ellipsVector.size()-1; cnt >= 0; cnt--)
		{
			Ellips ellips = (Ellips) ellipsVector.elementAt(cnt);
			if (ellips.deletable)
				ellipsVector.removeElementAt(cnt);
		}
	}

	public void removeDeletableTekstElementen()
	{
		for (int cnt = tekstElementVector.size()-1; cnt >= 0; cnt--)
		{
			TekstElement tekstElement = (TekstElement) tekstElementVector.elementAt(cnt);
			if (tekstElement.deletable)
				tekstElementVector.removeElementAt(cnt);
		}
	}

	public void removeUndeletableStrepen()
	{
		for (int cnt = streepVector.size()-1; cnt >= 0; cnt--)
		{
			Streep streep = (Streep) streepVector.elementAt(cnt);
			if (!streep.deletable)
				streepVector.removeElementAt(cnt);
		}
	}
	
	public void removeUndeletableLijnen()
	{
		for (int cnt = lijnVector.size()-1; cnt >= 0; cnt--)
		{
			Lijn lijn = (Lijn) lijnVector.elementAt(cnt);
			if (!lijn.deletable)
				lijnVector.removeElementAt(cnt);
		}
	}

	public void removeUndeletableRechthoeken()
	{
		for (int cnt = rechthoekVector.size()-1; cnt >= 0; cnt--)
		{
			Rechthoek rechthoek = (Rechthoek) rechthoekVector.elementAt(cnt);
			if (!rechthoek.deletable)
				rechthoekVector.removeElementAt(cnt);
		}
	}

	public void removeUndeletableEllipsen()
	{
		for (int cnt = ellipsVector.size()-1; cnt >= 0; cnt--)
		{
			Ellips ellips = (Ellips) ellipsVector.elementAt(cnt);
			if (!ellips.deletable)
				ellipsVector.removeElementAt(cnt);
		}
	}

	public void removeUndeletableTekstElementen()
	{
		for (int cnt = tekstElementVector.size()-1; cnt >= 0; cnt--)
		{
			TekstElement tekstElement = (TekstElement) tekstElementVector.elementAt(cnt);
			if (!tekstElement.deletable)
				tekstElementVector.removeElementAt(cnt);
		}
	}

	
	public void updateSelecteerRechthoek()
	{
		if (selecteerRechthoek == null)
			return;
		
		int minX = 1000;
		int maxX = -100;
		int minY = 1000;
		int maxY = -100;
		
		for (int cnt = 0; cnt < objectsSelected.size(); cnt++)
		{
			Object ob = objectsSelected.elementAt(cnt);
			if (ob instanceof Streep)
			{	Rectangle r = ((Streep) ob).handleBox;
				if (r.x < minX)
					minX = r.x;
				if ((r.x + r.width) > maxX)
					maxX = r.x + r.width;
				if (r.y < minY)
					minY = r.y;
				if ((r.y + r.height) > maxY)
					maxY = r.y + r.height;
			}
			else if (ob instanceof Lijn)
			{	Rectangle r = ((Lijn) ob).handleBox;
				if (r.x < minX)
					minX = r.x;
				if ((r.x + r.width) > maxX)
					maxX = r.x + r.width;
				if (r.y < minY)
					minY = r.y;
				if ((r.y + r.height) > maxY)
					maxY = r.y + r.height;
			}
			if (ob instanceof Rechthoek)
			{	Rectangle r = ((Rechthoek) ob).handleBox;
				if (r.x < minX)
					minX = r.x;
				if ((r.x + r.width) > maxX)
					maxX = r.x + r.width;
				if (r.y < minY)
					minY = r.y;
				if ((r.y + r.height) > maxY)
					maxY = r.y + r.height;
			}
			if (ob instanceof Ellips)
			{	Rectangle r = ((Ellips) ob).handleBox;
				if (r.x < minX)
					minX = r.x;
				if ((r.x + r.width) > maxX)
					maxX = r.x + r.width;
				if (r.y < minY)
					minY = r.y;
				if ((r.y + r.height) > maxY)
					maxY = r.y + r.height;
			}
			if (ob instanceof TekstElement)
			{	Rectangle r = ((TekstElement) ob).handleBox;
				if (r.x < minX)
					minX = r.x;
				if ((r.x + r.width) > maxX)
					maxX = r.x + r.width;
				if (r.y < minY)
					minY = r.y;
				if ((r.y + r.height) > maxY)
					maxY = r.y + r.height;
			}
		} // for
		int w = maxX - minX + 2 * hbFactor;
		int h = maxY - minY + 2 * hbFactor;
		selecteerRechthoek = new Rectangle(minX - hbFactor, minY - hbFactor, w, h);
		if (schalen)
			makeScaleHandles();
		if (roteren)
			makeRotateHandles();

		
	}
	
	public void makeScaleHandles()
	{
		if (selecteerRechthoek == null)
			return;
		
		topRightHandle = new Polygon();
		topRightHandle.addPoint(selecteerRechthoek.x + selecteerRechthoek.width + hbFactor,
								selecteerRechthoek.y - hbFactor);
		topRightHandle.addPoint(selecteerRechthoek.x + selecteerRechthoek.width - 3 * hbFactor,
								selecteerRechthoek.y - hbFactor);
		topRightHandle.addPoint(selecteerRechthoek.x + selecteerRechthoek.width + hbFactor,
								selecteerRechthoek.y + 3 * hbFactor);
		topRightRect = new Rectangle(selecteerRechthoek.x + selecteerRechthoek.width - 3 * hbFactor,
									 selecteerRechthoek.y - hbFactor, 4 * hbFactor, 4 * hbFactor);
		
		topLeftHandle = new Polygon();
		topLeftHandle.addPoint(selecteerRechthoek.x - hbFactor, selecteerRechthoek.y - hbFactor);
		topLeftHandle.addPoint(selecteerRechthoek.x + 3 * hbFactor, selecteerRechthoek.y - hbFactor);
		topLeftHandle.addPoint(selecteerRechthoek.x - hbFactor, selecteerRechthoek.y + 3 * hbFactor);
		topLeftRect = new Rectangle(selecteerRechthoek.x - hbFactor, selecteerRechthoek.y - hbFactor, 4 * hbFactor, 4 * hbFactor);
		
		bottomRightHandle = new Polygon();
		bottomRightHandle.addPoint(selecteerRechthoek.x + selecteerRechthoek.width + hbFactor,
								   selecteerRechthoek.y + selecteerRechthoek.height + hbFactor);
		bottomRightHandle.addPoint(selecteerRechthoek.x + selecteerRechthoek.width - 3 * hbFactor,
								   selecteerRechthoek.y + selecteerRechthoek.height + hbFactor);
		bottomRightHandle.addPoint(selecteerRechthoek.x + selecteerRechthoek.width + hbFactor,
								   selecteerRechthoek.y + selecteerRechthoek.height - 3 * hbFactor);
		bottomRightRect = new Rectangle(selecteerRechthoek.x + selecteerRechthoek.width - 3 * hbFactor,
				   						selecteerRechthoek.y + selecteerRechthoek.height - 3 * hbFactor, 4 * hbFactor, 4 * hbFactor);		
		
		bottomLeftHandle = new Polygon();
		bottomLeftHandle.addPoint(selecteerRechthoek.x - hbFactor, selecteerRechthoek.y + selecteerRechthoek.height + hbFactor);
		bottomLeftHandle.addPoint(selecteerRechthoek.x + 3 * hbFactor, selecteerRechthoek.y + selecteerRechthoek.height + hbFactor);
		bottomLeftHandle.addPoint(selecteerRechthoek.x - hbFactor, selecteerRechthoek.y + selecteerRechthoek.height - 3 * hbFactor);
		bottomLeftRect = new Rectangle(selecteerRechthoek.x - hbFactor, selecteerRechthoek.y + selecteerRechthoek.height - 3 * hbFactor, 
									   4 * hbFactor, 4 * hbFactor);		
		
	}
	
	public void killScaleHandles()
	{
		topRightHandle = null; 
		bottomRightHandle = null; 
		topLeftHandle = null;
		bottomLeftHandle = null;
		topRightRect = null;
		bottomRightRect = null;
		topLeftRect = null;
		bottomLeftRect = null;
		
	}
	
	public void makeRotateHandles()
	{
		
		if (selecteerRechthoek == null)
			return;
		
		rotateEastHandle = new Rectangle(selecteerRechthoek.x + selecteerRechthoek.width,// - 2 * hbFactor,
										 selecteerRechthoek.y + selecteerRechthoek.height/2 - 2 * hbFactor,
										 4 * hbFactor, 4 * hbFactor);
		rotateWestHandle = new Rectangle(selecteerRechthoek.x - 4 * hbFactor, 
										 selecteerRechthoek.y + selecteerRechthoek.height/2 - 2 * hbFactor,
										 4 * hbFactor, 4 * hbFactor);
	}

	public void killRotateHandles()
	{
		rotateEastHandle = null; 
		rotateWestHandle = null;
	}

	public void resetSelectedObject()
	{
		selectedStreep = null;
		selectedLijn = null;
		selectedRechthoek = null;
		selectedEllips = null;
		selectedTekstElement = null;
	}
	
	public void resetSelectedObjects()
	{
		objectsSelected.removeAllElements();
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
	
	public TekstElement getClickedTekstElement(int x, int y)
	{
		TekstElement result = null;
		for (int tCnt = 0; tCnt < tekstElementVector.size(); tCnt++)
		{	TekstElement tekstElement = (TekstElement) tekstElementVector.elementAt(tCnt);
			if (tekstElement.bbContains(x, y))
			{	result = tekstElement;
			}
		}
		return result;
	}
	
	public boolean findObjectsSelected(Rectangle r)
	{	boolean found = false;
		objectsSelected.removeAllElements();
	
		for (int sCnt = 0; sCnt < streepVector.size(); sCnt++)
		{	Streep streep = (Streep) streepVector.elementAt(sCnt);
			if (streep.isContainedIn(r))
			{	objectsSelected.addElement(streep);
				found = true; 
			}
		}
		for (int lCnt = 0; lCnt < lijnVector.size(); lCnt++)
		{	Lijn lijn = (Lijn) lijnVector.elementAt(lCnt);
			if (lijn.isContainedIn(r))
			{	objectsSelected.addElement(lijn);
				found = true; 
			}
		}
		for (int rCnt = 0; rCnt < rechthoekVector.size(); rCnt++)
		{	Rechthoek rechthoek = (Rechthoek) rechthoekVector.elementAt(rCnt);
			if (rechthoek.isContainedIn(r))
			{	objectsSelected.addElement(rechthoek);			
				found = true; 
			}
		}
		for (int eCnt = 0; eCnt < ellipsVector.size(); eCnt++)
		{	Ellips ellips = (Ellips) ellipsVector.elementAt(eCnt);
			if (ellips.isContainedIn(r))
			{	objectsSelected.addElement(ellips);
				found = true; 
			}
		}
		for (int tCnt = 0; tCnt < tekstElementVector.size(); tCnt++)
		{	TekstElement tekstElement = (TekstElement) tekstElementVector.elementAt(tCnt);
			if (tekstElement.isContainedIn(r))
			{	objectsSelected.addElement(tekstElement);
				found = true; 
			}
		}
	
		return found;
	}
	
	
	public void wisObjectSelected()
	{ 
		boolean gewist = false;
		if (selectedStreep != null && selectedStreep.deletable)
		{	streepVector.removeElement(selectedStreep);
			selectedStreep = null;
			gewist = true;
		}
		if (selectedLijn != null && selectedLijn.deletable)  
		{	lijnVector.removeElement(selectedLijn);
			selectedLijn = null;
			gewist = true;
		}
		if (selectedRechthoek != null && selectedRechthoek.deletable)  
		{	rechthoekVector.removeElement(selectedRechthoek);
			selectedRechthoek = null;
			gewist = true;
		}
		if (selectedEllips != null && selectedEllips.deletable) 
		{	ellipsVector.removeElement(selectedEllips);
			selectedEllips = null;
			gewist = true;
		}
		if (selectedTekstElement != null && selectedTekstElement.deletable)
		{	tekstElementVector.removeElement(selectedTekstElement);
			selectedTekstElement = null;
			gewist = true;
		}
		
		sleepSelectie = false;
		
		if (gewist)
			addToHistory();
		repaint();
	}

	public void wisObjectsSelected()
	{ 
		boolean gewist = false;
		for (int oCnt = 0; oCnt < objectsSelected.size(); oCnt++)
		{
			Object o = (Object) objectsSelected.elementAt(oCnt);
			if (o instanceof Streep && ((Streep) o).deletable)
			{	streepVector.removeElement((Streep) o);
				gewist = true;
			}
			else if (o instanceof Lijn && ((Lijn) o).deletable)
			{	lijnVector.removeElement((Lijn) o);
				gewist = true;
			}
			else if (o instanceof Rechthoek && ((Rechthoek) o).deletable)
			{	rechthoekVector.removeElement((Rechthoek) o);
				gewist = true;	
			}
			else if (o instanceof Ellips && ((Ellips) o).deletable)
			{	ellipsVector.removeElement((Ellips) o);
				gewist = true;
			}
			else if (o instanceof TekstElement && ((TekstElement) o).deletable)
			{	tekstElementVector.removeElement((TekstElement) o);
				gewist = true;
			}
			
			//gewist = true;
		}
		
		sleepSelectie = false;
		objectsSelected.removeAllElements();
		selecteerRechthoek = null;
		killScaleHandles();
		killRotateHandles();
		
		if (gewist)
			addToHistory();
		repaint();
	}
	
	public void rotateObjectSelected(double rotateStep)
	{ 
		if (selectedStreep != null)
			selectedStreep.rotate(rotateStep);
		if (selectedLijn != null)  
			selectedLijn.rotate(rotateStep);		   
		if (selectedRechthoek != null)  
			selectedRechthoek.rotate(rotateStep);
		if (selectedEllips != null) 
			selectedEllips.rotate(rotateStep);		
		if  (selectedTekstElement != null)
			selectedTekstElement.rotate(rotateStep);
	
		repaint();
	}

	public void scaleObjectSelected(double scaleStep)
	{ 
		if (selectedStreep != null)
			selectedStreep.scale(scaleStep);
		if (selectedLijn != null)  
			selectedLijn.scale(scaleStep);		   
		if (selectedRechthoek != null)  
			selectedRechthoek.scale(scaleStep);
		if (selectedEllips != null) 
			selectedEllips.scale(scaleStep);		
		if  (selectedTekstElement != null)
			selectedTekstElement.scale(scaleStep);
		
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

	public void translateObjectsSelected(int dx, int dy)
	{ 
		for (int oCnt = 0; oCnt < objectsSelected.size(); oCnt++)
		{
			Object o = (Object) objectsSelected.elementAt(oCnt);
			if (o instanceof Streep)
				((Streep) o).translate(dx, dy);
			else if (o instanceof Lijn)
				((Lijn) o).translate(dx, dy);
			else if (o instanceof Rechthoek)
				((Rechthoek) o).translate(dx, dy);
			else if (o instanceof Ellips)
				((Ellips) o).translate(dx, dy);
			else if (o instanceof TekstElement)
				((TekstElement) o).translate(dx, dy);
			
		}
		
	}
	
	public boolean objectSelectedContains(int x, int y)
	{ 
		return ((selectedStreep != null) && 
				 //selectedStreep.bbContains(x, y)) ||
				 selectedStreep.handleBox.contains(x, y)) ||
			   ((selectedLijn != null) && 
				 //selectedLijn.bbContains(x, y)) ||
			     selectedLijn.handleBox.contains(x, y)) ||	   
			   ((selectedRechthoek != null) && 
				 //selectedRechthoek.bbContains(x, y)) ||
				 selectedRechthoek.handleBox.contains(x, y)) ||	   
			   ((selectedEllips != null) && 
				 //selectedEllips.bbContains(x, y)) ||
				 selectedEllips.handleBox.contains(x, y)) ||	   
			   ((selectedTekstElement != null) && 
				 //selectedTekstElement.bbContains(x, y));
			     selectedTekstElement.handleBox.contains(x, y));	   
	}


	
	public void processSelecteerRechthoekHandleAction(int dx, int dy)
	{
		int crx = selecteerRechthoek.x + selecteerRechthoek.width / 2;
		int cry = selecteerRechthoek.y + selecteerRechthoek.height / 2;
		
		if (scalingTopRight)
		{
			double aspectDirX = selecteerRechthoek.x + selecteerRechthoek.width - 
								crx;
			double aspectDirY = selecteerRechthoek.y - cry;
			double dxDouble = (double) dx;
			double dyDouble = (double) dy;
			double aa = aspectDirX * aspectDirX + aspectDirY * aspectDirY;
			double s = (aspectDirX * dxDouble + aspectDirY * dyDouble) / aa;
			double asXDouble = s * aspectDirX;
			double asYDouble = s * aspectDirY;
			double oldWidth = (double) selecteerRechthoek.width / 2;
			double oldHeight = (double) selecteerRechthoek.height / 2;
			double newWidth = oldWidth + asXDouble;
			double newHeight = oldHeight - asYDouble;
			double sc = ((double) newWidth) / oldWidth;
			for (int cnt = 0; cnt < objectsSelected.size(); cnt++)
			{
				Object ob = objectsSelected.elementAt(cnt);
				if (ob instanceof Streep)
					((Streep) ob).scale(sc, crx, cry);
				else if (ob instanceof Lijn)
					((Lijn) ob).scale(sc, crx, cry);
				else if (ob instanceof Rechthoek)
					((Rechthoek) ob).scale(sc, crx, cry);
				else if (ob instanceof Ellips)
					((Ellips) ob).scale(sc, crx, cry);
				else if (ob instanceof TekstElement)
					((TekstElement) ob).scale(sc, crx, cry);
				
			}

			updateSelecteerRechthoek();
			
			
		}
		else if (scalingTopLeft)
		{
			double dxDouble = (double) dx;
			double dyDouble = (double) dy;
			
			double oldWidth = (double) selecteerRechthoek.width / 2;
			double oldHeight = (double) selecteerRechthoek.height / 2;

			double newWidth = oldWidth - dx;
			double newHeight = oldHeight - dy;
			double sx = newWidth / oldWidth;
			double sy = newHeight / oldHeight;

			for (int cnt = 0; cnt < objectsSelected.size(); cnt++)
			{
				Object ob = objectsSelected.elementAt(cnt);
				if (ob instanceof Streep)
					((Streep) ob).scale(sx, sy, crx, cry);
				else if (ob instanceof Lijn)
					((Lijn) ob).scale(sx, sy, crx, cry);
				else if (ob instanceof Rechthoek)
					((Rechthoek) ob).scale(sx, sy, crx, cry);
				else if (ob instanceof Ellips)
					((Ellips) ob).scale(sx, sy, crx, cry);
				else if (ob instanceof TekstElement)
					((TekstElement) ob).scale(sx, sy, crx, cry);
				
			}

			updateSelecteerRechthoek();
			

		}
		else if (scalingBottomLeft)
		{
			double dxDouble = (double) dx;
			double dyDouble = (double) dy;
			
			double oldWidth = (double) selecteerRechthoek.width / 2;
			double oldHeight = (double) selecteerRechthoek.height / 2;

			double newWidth = oldWidth - dx;
			double newHeight = oldHeight + dy;
			double sx = newWidth / oldWidth;
			double sy = newHeight / oldHeight;

			for (int cnt = 0; cnt < objectsSelected.size(); cnt++)
			{
				Object ob = objectsSelected.elementAt(cnt);
				if (ob instanceof Streep)
					((Streep) ob).scale(sx, sy, crx, cry);
				else if (ob instanceof Lijn)
					((Lijn) ob).scale(sx, sy, crx, cry);
				else if (ob instanceof Rechthoek)
					((Rechthoek) ob).scale(sx, sy, crx, cry);
				else if (ob instanceof Ellips)
					((Ellips) ob).scale(sx, sy, crx, cry);
				else if (ob instanceof TekstElement)
					((TekstElement) ob).scale(sx, sy, crx, cry);
				
			}
			
			updateSelecteerRechthoek();
			

		}
		else if (scalingBottomRight)
		{
			double aspectDirX = selecteerRechthoek.x + selecteerRechthoek.width - crx; 
			double aspectDirY = selecteerRechthoek.y + selecteerRechthoek.height - cry;
			double dxDouble = (double) dx;
			double dyDouble = (double) dy;
			double aa = aspectDirX * aspectDirX + aspectDirY * aspectDirY;
			double s = (aspectDirX * dxDouble + aspectDirY * dyDouble) / aa;
			double asXDouble = s * aspectDirX;
			double asYDouble = s * aspectDirY;
			double oldWidth = (double) selecteerRechthoek.width / 2;
			double oldHeight = (double) selecteerRechthoek.height / 2;
			double newWidth = oldWidth + asXDouble;
			double newHeight = oldHeight + asYDouble;
			double sc = ((double) newWidth) / oldWidth;
			
			for (int cnt = 0; cnt < objectsSelected.size(); cnt++)
			{
				Object ob = objectsSelected.elementAt(cnt);
				if (ob instanceof Streep)
					((Streep) ob).scale(sc, crx, cry);
				else if (ob instanceof Lijn)
					((Lijn) ob).scale(sc, crx, cry);
				else if (ob instanceof Rechthoek)
					((Rechthoek) ob).scale(sc, crx, cry);
				else if (ob instanceof Ellips)
					((Ellips) ob).scale(sc, crx, cry);
				else if (ob instanceof TekstElement)
					((TekstElement) ob).scale(sc, crx, cry);
				
			}

			updateSelecteerRechthoek();
			

		}
		
		else if (rotatingEast)
		{
			// hier is alleen dy van belang
			double angle = Math.atan(((double) dy) / (selecteerRechthoek.width/2));
			//selectedStreep.rotate(angle);
			for (int cnt = 0; cnt < objectsSelected.size(); cnt++)
			{
				Object ob = objectsSelected.elementAt(cnt);
				if (ob instanceof Streep)
					((Streep) ob).rotate(angle, crx, cry);
				else if (ob instanceof Lijn)
					((Lijn) ob).rotate(angle, crx, cry);
				else if (ob instanceof Rechthoek)
					((Rechthoek) ob).rotate(angle, crx, cry);
				else if (ob instanceof Ellips)
					((Ellips) ob).rotate(angle, crx, cry);
				else if (ob instanceof TekstElement)
					((TekstElement) ob).rotate(angle, crx, cry);
				
			}
			
			updateSelecteerRechthoek();

			
		}
		else if (rotatingWest)
		{
			// hier is alleen dy van belang
			double angle = - Math.atan(((double) dy) / (selecteerRechthoek.width/2));
			angleSum += angle; 
			int rotateSteps = (int) Math.round(angleSum / rotateStep);
			angleSum -= rotateSteps * rotateStep;
			//selectedStreep.rotate(rotateSteps * rotateStep);
			
			for (int cnt = 0; cnt < objectsSelected.size(); cnt++)
			{
				Object ob = objectsSelected.elementAt(cnt);
				if (ob instanceof Streep)
					((Streep) ob).rotate(rotateSteps * rotateStep, crx, cry);
				else if (ob instanceof Lijn)
					((Lijn) ob).rotate(rotateSteps * rotateStep, crx, cry);
				else if (ob instanceof Rechthoek)
					((Rechthoek) ob).rotate(rotateSteps * rotateStep, crx, cry);
				else if (ob instanceof Ellips)
					((Ellips) ob).rotate(rotateSteps * rotateStep, crx, cry);
				else if (ob instanceof TekstElement)
					((TekstElement) ob).rotate(rotateSteps * rotateStep, crx, cry);
				
			}
			
			updateSelecteerRechthoek();
		}

		
	}
	
	public void processHandleAction(int dx, int dy)
	{
		if (selectedStreep != null)
		{
			if (scalingTopRight)
			{
				double aspectDirX = selectedStreep.handleBox.x + selectedStreep.handleBox. width - 
									selectedStreep.cx;
				double aspectDirY = selectedStreep.handleBox.y - selectedStreep.cy;
				double dxDouble = (double) dx;
				double dyDouble = (double) dy;
				double aa = aspectDirX * aspectDirX + aspectDirY * aspectDirY;
				double s = (aspectDirX * dxDouble + aspectDirY * dyDouble) / aa;
				double asXDouble = s * aspectDirX;
				double asYDouble = s * aspectDirY;
				double oldWidth = (double) selectedStreep.handleBox.width / 2;
				double oldHeight = (double) selectedStreep.handleBox.height / 2;
				double newWidth = oldWidth + asXDouble;
				double newHeight = oldHeight - asYDouble;
				double sc = ((double) newWidth) / oldWidth;
				selectedStreep.scale(sc);
			}
			else if (scalingTopLeft)
			{
				double dxDouble = (double) dx;
				double dyDouble = (double) dy;
				
				double oldWidth = (double) selectedStreep.handleBox.width / 2;
				double oldHeight = (double) selectedStreep.handleBox.height / 2;

				double newWidth = oldWidth - dx;
				double newHeight = oldHeight - dy;
				double sx = newWidth / oldWidth;
				double sy = newHeight / oldHeight;
				selectedStreep.scale(sx,sy);
			}
			else if (scalingBottomLeft)
			{
				double dxDouble = (double) dx;
				double dyDouble = (double) dy;
				
				double oldWidth = (double) selectedStreep.handleBox.width / 2;
				double oldHeight = (double) selectedStreep.handleBox.height / 2;

				double newWidth = oldWidth - dx;
				double newHeight = oldHeight + dy;
				double sx = newWidth / oldWidth;
				double sy = newHeight / oldHeight;
				selectedStreep.scale(sx,sy);
			}
			else if (scalingBottomRight)
			{
				double aspectDirX = selectedStreep.handleBox.x + selectedStreep.handleBox.width - 
								    selectedStreep.cx;
				double aspectDirY = selectedStreep.handleBox.y + selectedStreep.handleBox.height - selectedStreep.cy;
				double dxDouble = (double) dx;
				double dyDouble = (double) dy;
				double aa = aspectDirX * aspectDirX + aspectDirY * aspectDirY;
				double s = (aspectDirX * dxDouble + aspectDirY * dyDouble) / aa;
				double asXDouble = s * aspectDirX;
				double asYDouble = s * aspectDirY;
				double oldWidth = (double) selectedStreep.handleBox.width / 2;
				double oldHeight = (double) selectedStreep.handleBox.height / 2;
				double newWidth = oldWidth + asXDouble;
				double newHeight = oldHeight + asYDouble;
				double sc = ((double) newWidth) / oldWidth;
				selectedStreep.scale(sc);
			}
			else if (rotatingEast)
			{
				// hier is alleen dy van belang
				double angle = Math.atan(((double) dy) / (selectedStreep.handleBox.width/2));
				selectedStreep.rotate(angle);
				
			}
			else if (rotatingWest)
			{
				// hier is alleen dy van belang
				double angle = - Math.atan(((double) dy) / (selectedStreep.handleBox.width/2));
				angleSum += angle; 
				int rotateSteps = (int) Math.round(angleSum / rotateStep);
				angleSum -= rotateSteps * rotateStep;
				selectedStreep.rotate(rotateSteps * rotateStep);
				
			}
			
		}
		else if (selectedLijn != null)
		{
			if (scalingTopRight)
			{
				double aspectDirX = selectedLijn.handleBox.x + selectedLijn.handleBox. width - 
									selectedLijn.cx;
				double aspectDirY = selectedLijn.handleBox.y - selectedLijn.cy;
				double dxDouble = (double) dx;
				double dyDouble = (double) dy;
				double aa = aspectDirX * aspectDirX + aspectDirY * aspectDirY;
				double s = (aspectDirX * dxDouble + aspectDirY * dyDouble) / aa;
				double asXDouble = s * aspectDirX;
				double asYDouble = s * aspectDirY;
				double oldWidth = (double) selectedLijn.handleBox.width / 2;
				double oldHeight = (double) selectedLijn.handleBox.height / 2;
				double newWidth = oldWidth + asXDouble;
				double newHeight = oldHeight - asYDouble;
				double sc = ((double) newWidth) / oldWidth;
				selectedLijn.scale(sc);
			}
			else if (scalingTopLeft)
			{
				double dxDouble = (double) dx;
				double dyDouble = (double) dy;
				
				double oldWidth = (double) selectedLijn.handleBox.width / 2;
				double oldHeight = (double) selectedLijn.handleBox.height / 2;

				double newWidth = oldWidth - dx;
				double newHeight = oldHeight - dy;
				double sx = newWidth / oldWidth;
				double sy = newHeight / oldHeight;
				
				selectedLijn.scale(sx,sy);
			}
			else if (scalingBottomLeft)
			{
				double dxDouble = (double) dx;
				double dyDouble = (double) dy;
				
				double oldWidth = (double) selectedLijn.handleBox.width / 2;
				double oldHeight = (double) selectedLijn.handleBox.height / 2;

				double newWidth = oldWidth - dx;
				double newHeight = oldHeight + dy;
				double sx = newWidth / oldWidth;
				double sy = newHeight / oldHeight;
				selectedLijn.scale(sx,sy);
			}
			else if (scalingBottomRight)
			{
				double aspectDirX = selectedLijn.handleBox.x + selectedLijn.handleBox.width - 
								    selectedLijn.cx;
				double aspectDirY = selectedLijn.handleBox.y + selectedLijn.handleBox.height - 
									selectedLijn.cy;
				double dxDouble = (double) dx;
				double dyDouble = (double) dy;
				double aa = aspectDirX * aspectDirX + aspectDirY * aspectDirY;
				double s = (aspectDirX * dxDouble + aspectDirY * dyDouble) / aa;
				double asXDouble = s * aspectDirX;
				double asYDouble = s * aspectDirY;
				double oldWidth = (double) selectedLijn.handleBox.width / 2;
				double oldHeight = (double) selectedLijn.handleBox.height / 2;
				double newWidth = oldWidth + asXDouble;
				double newHeight = oldHeight + asYDouble;
				double sc = ((double) newWidth) / oldWidth;
				selectedLijn.scale(sc);
			}
			else if (rotatingEast)
			{
				// hier is alleen dy van belang
				double angle = Math.atan(((double) dy) / (selectedLijn.handleBox.width/2));
				selectedLijn.rotate(angle);
				
			}
			else if (rotatingWest)
			{
				// hier is alleen dy van belang
				double angle = - Math.atan(((double) dy) / (selectedLijn.handleBox.width/2));
				angleSum += angle; 
				int rotateSteps = (int) Math.round(angleSum / rotateStep);
				angleSum -= rotateSteps * rotateStep;
				selectedLijn.rotate(rotateSteps * rotateStep);
				
				
			}

			
		}
		else if (selectedRechthoek != null)
		{
			if (scalingTopRight)
			{
				double aspectDirX = selectedRechthoek.handleBox.x + selectedRechthoek.handleBox. width - 
									selectedRechthoek.cx;
				double aspectDirY = selectedRechthoek.handleBox.y - selectedRechthoek.cy;
				double dxDouble = (double) dx;
				double dyDouble = (double) dy;
				double aa = aspectDirX * aspectDirX + aspectDirY * aspectDirY;
				double s = (aspectDirX * dxDouble + aspectDirY * dyDouble) / aa;
				double asXDouble = s * aspectDirX;
				double asYDouble = s * aspectDirY;
				double oldWidth = (double) selectedRechthoek.handleBox.width / 2;
				double oldHeight = (double) selectedRechthoek.handleBox.height / 2;
				double newWidth = oldWidth + asXDouble;
				double newHeight = oldHeight - asYDouble;
				double sc = ((double) newWidth) / oldWidth;
				selectedRechthoek.scale(sc);
			}
			else if (scalingTopLeft)
			{
				double dxDouble = (double) dx;
				double dyDouble = (double) dy;
				
				double oldWidth = (double) selectedRechthoek.handleBox.width / 2;
				double oldHeight = (double) selectedRechthoek.handleBox.height / 2;
				
				double newWidth = oldWidth - dx;
				double newHeight = oldHeight - dy;
				double sx = newWidth / oldWidth;
				double sy = newHeight / oldHeight;
				
				selectedRechthoek.scale(sx,sy);
			}
			else if (scalingBottomLeft)
			{
				
				double dxDouble = (double) dx;
				double dyDouble = (double) dy;

				double oldWidth = (double) selectedRechthoek.handleBox.width / 2;
				double oldHeight = (double) selectedRechthoek.handleBox.height / 2;
				
				double newWidth = oldWidth - dx;
				double newHeight = oldHeight + dy;
				double sx = newWidth / oldWidth;
				double sy = newHeight / oldHeight;
				
				selectedRechthoek.scale(sx,sy);
			}
			else if (scalingBottomRight)
			{
				double aspectDirX = selectedRechthoek.handleBox.x + selectedRechthoek.handleBox.width - 
								    selectedRechthoek.cx;
				double aspectDirY = selectedRechthoek.handleBox.y + selectedRechthoek.handleBox.height - 
									selectedRechthoek.cy;
				double dxDouble = (double) dx;
				double dyDouble = (double) dy;
				double aa = aspectDirX * aspectDirX + aspectDirY * aspectDirY;
				double s = (aspectDirX * dxDouble + aspectDirY * dyDouble) / aa;
				double asXDouble = s * aspectDirX;
				double asYDouble = s * aspectDirY;
				double oldWidth = (double) selectedRechthoek.handleBox.width / 2;
				double oldHeight = (double) selectedRechthoek.handleBox.height / 2;
				double newWidth = oldWidth + asXDouble;
				double newHeight = oldHeight + asYDouble;
				double sc = ((double) newWidth) / oldWidth;
				selectedRechthoek.scale(sc);
			}
			else if (rotatingEast)
			{
				// hier is alleen dy van belang
				double angle = Math.atan(((double) dy) / (selectedRechthoek.handleBox.width/2));
				selectedRechthoek.rotate(angle);
				
			}
			else if (rotatingWest)
			{
				// hier is alleen dy van belang
				double angle = - Math.atan(((double) dy) / (selectedRechthoek.handleBox.width/2));
				angleSum += angle; 
				int rotateSteps = (int) Math.round(angleSum / rotateStep);
				angleSum -= rotateSteps * rotateStep;
				selectedRechthoek.rotate(rotateSteps * rotateStep);
				
				
			}

			
		}
		else if (selectedEllips != null)
		{
			if (scalingTopRight)
			{
				double aspectDirX = selectedEllips.handleBox.x + selectedEllips.handleBox. width - 
									selectedEllips.cx;
				double aspectDirY = selectedEllips.handleBox.y - selectedEllips.cy;
				double dxDouble = (double) dx;
				double dyDouble = (double) dy;
				double aa = aspectDirX * aspectDirX + aspectDirY * aspectDirY;
				double s = (aspectDirX * dxDouble + aspectDirY * dyDouble) / aa;
				double asXDouble = s * aspectDirX;
				double asYDouble = s * aspectDirY;
				double oldWidth = (double) selectedEllips.handleBox.width / 2;
				double oldHeight = (double) selectedEllips.handleBox.height / 2;
				double newWidth = oldWidth + asXDouble;
				double newHeight = oldHeight - asYDouble;
				double sc = ((double) newWidth) / oldWidth;
				selectedEllips.scale(sc);
			}
			else if (scalingTopLeft)
			{
				double dxDouble = (double) dx;
				double dyDouble = (double) dy;

				double oldWidth = (double) selectedEllips.handleBox.width / 2;
				double oldHeight = (double) selectedEllips.handleBox.height / 2;

				double newWidth = oldWidth - dx;
				double newHeight = oldHeight - dy;
				double sx = newWidth / oldWidth;
				double sy = newHeight / oldHeight;
				selectedEllips.scale(sx,sy);
			}
			else if (scalingBottomLeft)
			{
				double dxDouble = (double) dx;
				double dyDouble = (double) dy;
				
				double oldWidth = (double) selectedEllips.handleBox.width / 2;
				double oldHeight = (double) selectedEllips.handleBox.height / 2;

				double newWidth = oldWidth - dx;
				double newHeight = oldHeight + dy;
				double sx = newWidth / oldWidth;
				double sy = newHeight / oldHeight;
				
				selectedEllips.scale(sx,sy);
			}
			else if (scalingBottomRight)
			{
				double aspectDirX = selectedEllips.handleBox.x + selectedEllips.handleBox.width - 
								    selectedEllips.cx;
				double aspectDirY = selectedEllips.handleBox.y + selectedEllips.handleBox.height - 
									selectedEllips.cy;
				double dxDouble = (double) dx;
				double dyDouble = (double) dy;
				double aa = aspectDirX * aspectDirX + aspectDirY * aspectDirY;
				double s = (aspectDirX * dxDouble + aspectDirY * dyDouble) / aa;
				double asXDouble = s * aspectDirX;
				double asYDouble = s * aspectDirY;
				double oldWidth = (double) selectedEllips.handleBox.width / 2;
				double oldHeight = (double) selectedEllips.handleBox.height / 2;
				double newWidth = oldWidth + asXDouble;
				double newHeight = oldHeight + asYDouble;
				double sc = ((double) newWidth) / oldWidth;
				selectedEllips.scale(sc);
			}
			else if (rotatingEast)
			{
				// hier is alleen dy van belang
				double angle = Math.atan(((double) dy) / (selectedEllips.handleBox.width/2));
				selectedEllips.rotate(angle);
				
			}
			else if (rotatingWest)
			{
				// hier is alleen dy van belang
				double angle = - Math.atan(((double) dy) / (selectedEllips.handleBox.width/2));
				angleSum += angle; 
				int rotateSteps = (int) Math.round(angleSum / rotateStep);
				angleSum -= rotateSteps * rotateStep;
				selectedEllips.rotate(rotateSteps * rotateStep);
				
				
			}
			
			
		}
		else if (selectedTekstElement != null)
		{
// wordt niet gebruikt
			if (scalingTopRight)
			{
				double aspectDirX = selectedTekstElement.handleBox.x + selectedTekstElement.handleBox. width - 
									selectedTekstElement.cx;
				double aspectDirY = selectedTekstElement.handleBox.y - selectedTekstElement.cy;
				double dxDouble = (double) dx;
				double dyDouble = (double) dy;
				double aa = aspectDirX * aspectDirX + aspectDirY * aspectDirY;
				double s = (aspectDirX * dxDouble + aspectDirY * dyDouble) / aa;
				double asXDouble = s * aspectDirX;
				double asYDouble = s * aspectDirY;
				double oldWidth = (double) selectedTekstElement.handleBox.width / 2;
				double oldHeight = (double) selectedTekstElement.handleBox.height / 2;
				double newWidth = oldWidth + asXDouble;
				double newHeight = oldHeight - asYDouble;
				double sc = ((double) newWidth) / oldWidth;
				selectedTekstElement.scale(sc);
			}
// wordt niet gebruikt			
			else if (scalingTopLeft)
			{
				double dxDouble = (double) dx;
				double dyDouble = (double) dy;
				
				double oldWidth = (double) selectedTekstElement.handleBox.width / 2;
				double oldHeight = (double) selectedTekstElement.handleBox.height / 2;

				double newWidth = oldWidth - dx;
				double newHeight = oldHeight - dy;
				double sx = newWidth / oldWidth;
				double sy = newHeight / oldHeight;
				selectedTekstElement.scale(sx,sy);
			}
// wordt niet gebruikt			
			else if (scalingBottomLeft)
			{
				double dxDouble = (double) dx;
				double dyDouble = (double) dy;

				double oldWidth = (double) selectedTekstElement.handleBox.width / 2;
				double oldHeight = (double) selectedTekstElement.handleBox.height / 2;

				double newWidth = oldWidth - dx;
				double newHeight = oldHeight + dy;
				double sx = newWidth / oldWidth;
				double sy = newHeight / oldHeight;
				selectedTekstElement.scale(sx,sy);
			}
			
			else if (scalingBottomRight)
			{
				double aspectDirX = selectedTekstElement.handleBox.x + selectedTekstElement.handleBox.width - 
								    selectedTekstElement.cx;
				double aspectDirY = selectedTekstElement.handleBox.y + selectedTekstElement.handleBox.height - 
									selectedTekstElement.cy;
				
				double dxDouble = (double) dx;
				double dyDouble = (double) dy;
				double aa = aspectDirX * aspectDirX + aspectDirY * aspectDirY;
				double s = (aspectDirX * dxDouble + aspectDirY * dyDouble) / aa;
				double asXDouble = s * aspectDirX;
				double asYDouble = s * aspectDirY;
				double oldWidth = (double) selectedTekstElement.handleBox.width / 2;
				double oldHeight = (double) selectedTekstElement.handleBox.height / 2;
				double newWidth = oldWidth + asXDouble;
				double newHeight = oldHeight + asYDouble;
				double sc = ((double) newWidth) / oldWidth;
				
				//if (sc < 1)
				//	selectedTekstElement.scale(scaleDownStep);
				//else
				//	selectedTekstElement.scale(scaleUpStep);
//System.out.println("te scbr " + sc);
				selectedTekstElement.scale(sc);
				
				updateAction2();
				

				
			}
			else if (rotatingEast)
			{
				// hier is alleen dy van belang
				double angle = Math.atan(((double) dy) / (selectedTekstElement.handleBox.width/2));
				selectedTekstElement.rotate(angle);
				
				updateAction2();				
				

				
			}
// wordt niet gebruikt			
			else if (rotatingWest)
			{
				// hier is alleen dy van belang
				double angle = - Math.atan(((double) dy) / (selectedTekstElement.handleBox.width/2));
				angleSum += angle; 
				int rotateSteps = (int) Math.round(angleSum / rotateStep);
				angleSum -= rotateSteps * rotateStep;
				selectedTekstElement.rotate(rotateSteps * rotateStep);
				
				
			}
			
		}

	}
	
	public boolean selecteerRechthoekHandlesContain(int x, int y)
	{
				
		if ((topRightRect != null) && topRightRect.contains(x,y))
		{	groupHandleAction = true;
			scalingTopRight = true;
		}
		else if ((topLeftRect != null) && topLeftRect.contains(x,y))
		{	groupHandleAction = true;
			scalingTopLeft = true;
		}
		else if ((bottomRightRect != null) && bottomRightRect.contains(x,y))
		{	groupHandleAction = true;
			scalingBottomRight = true;
		}
		else if ((bottomLeftRect != null) && bottomLeftRect.contains(x,y))
		{	groupHandleAction = true;
			scalingBottomLeft = true;
		}
		else if ((rotateEastHandle != null) && rotateEastHandle.contains(x,y))
		{	groupHandleAction = true;
			rotatingEast = true;
		}
		else if ((rotateWestHandle != null) && rotateWestHandle.contains(x,y))
		{	groupHandleAction = true;
			rotatingWest = true;
		}
		
		return groupHandleAction;
	}
	
	public boolean objectSelectedHandlesContain(int x, int y)
	{
		if (selectedStreep != null)
		{
			if ((selectedStreep.topRightRect != null) && selectedStreep.topRightRect.contains(x,y))
			{	handleAction = true;
				scalingTopRight = true;
			}
			else if ((selectedStreep.topLeftRect != null) && selectedStreep.topLeftRect.contains(x,y))
			{	handleAction = true;
				scalingTopLeft = true;
			}
			else if ((selectedStreep.bottomRightRect != null) && selectedStreep.bottomRightRect.contains(x,y))
			{	handleAction = true;
				scalingBottomRight = true;
			}
			else if ((selectedStreep.bottomLeftRect != null) && selectedStreep.bottomLeftRect.contains(x,y))
			{	handleAction = true;
				scalingBottomLeft = true;
			}
			else if ((selectedStreep.rotateEastHandle != null) && selectedStreep.rotateEastHandle.contains(x,y))
			{	handleAction = true;
				rotatingEast = true;
			}
			else if ((selectedStreep.rotateWestHandle != null) && selectedStreep.rotateWestHandle.contains(x,y))
			{	handleAction = true;
				rotatingWest = true;
			}
						

		}
		else if (selectedLijn != null)
		{
			if ((selectedLijn.topRightRect != null) && selectedLijn.topRightRect.contains(x,y))
			{	handleAction = true;
				scalingTopRight = true;
			}
			else if ((selectedLijn.topLeftRect != null) && selectedLijn.topLeftRect.contains(x,y))
			{	handleAction = true;
				scalingTopLeft = true;
			}
			else if ((selectedLijn.bottomRightRect != null) && selectedLijn.bottomRightRect.contains(x,y))
			{	handleAction = true;
				scalingBottomRight = true;
			}
			else if ((selectedLijn.bottomLeftRect != null) && selectedLijn.bottomLeftRect.contains(x,y))
			{	handleAction = true;
				scalingBottomLeft = true;
			}
			else if ((selectedLijn.rotateEastHandle != null) && selectedLijn.rotateEastHandle.contains(x,y))
			{	handleAction = true;
				rotatingEast = true;
			}
			else if ((selectedLijn.rotateWestHandle != null) && selectedLijn.rotateWestHandle.contains(x,y))
			{	handleAction = true;
				rotatingWest = true;
			}
			

		}
		else if (selectedRechthoek != null)
		{
			if ((selectedRechthoek.topRightRect != null) && selectedRechthoek.topRightRect.contains(x,y))
			{	handleAction = true;
				scalingTopRight = true;
			}
			else if ((selectedRechthoek.topLeftRect != null) && selectedRechthoek.topLeftRect.contains(x,y))
			{	handleAction = true;
				scalingTopLeft = true;
			}
			else if ((selectedRechthoek.bottomRightRect != null) && selectedRechthoek.bottomRightRect.contains(x,y))
			{	handleAction = true;
				scalingBottomRight = true;
			}
			else if ((selectedRechthoek.bottomLeftRect != null) && selectedRechthoek.bottomLeftRect.contains(x,y))
			{	handleAction = true;
				scalingBottomLeft = true;
			}
			else if ((selectedRechthoek.rotateEastHandle != null) && selectedRechthoek.rotateEastHandle.contains(x,y))
			{	handleAction = true;
				rotatingEast = true;
			}
			else if ((selectedRechthoek.rotateWestHandle != null) && selectedRechthoek.rotateWestHandle.contains(x,y))
			{	handleAction = true;
				rotatingWest = true;
			}

		}
		else if (selectedEllips != null)
		{
			if ((selectedEllips.topRightRect != null) && selectedEllips.topRightRect.contains(x,y))
			{	handleAction = true;
				scalingTopRight = true;
			}
			else if ((selectedEllips.topLeftRect != null) && selectedEllips.topLeftRect.contains(x,y))
			{	handleAction = true;
				scalingTopLeft = true;
			}
			else if ((selectedEllips.bottomRightRect != null) && selectedEllips.bottomRightRect.contains(x,y))
			{	handleAction = true;
				scalingBottomRight = true;
			}
			else if ((selectedEllips.bottomLeftRect != null) && selectedEllips.bottomLeftRect.contains(x,y))
			{	handleAction = true;
				scalingBottomLeft = true;
			}
			else if ((selectedEllips.rotateEastHandle != null) && selectedEllips.rotateEastHandle.contains(x,y))
			{	handleAction = true;
				rotatingEast = true;
			}
			else if ((selectedEllips.rotateWestHandle != null) && selectedEllips.rotateWestHandle.contains(x,y))
			{	handleAction = true;
				rotatingWest = true;
			}


		}
		else if (selectedTekstElement != null)
		{
			if ((selectedTekstElement.topRightRect != null) && selectedTekstElement.topRightRect.contains(x,y))
			{	handleAction = true;
				scalingTopRight = true;
			}
			else if ((selectedTekstElement.topLeftRect != null) && selectedTekstElement.topLeftRect.contains(x,y))
			{	handleAction = true;
				scalingTopLeft = true;
			}
			else if ((selectedTekstElement.bottomRightRect != null) && selectedTekstElement.bottomRightRect.contains(x,y))
			{	handleAction = true;
				scalingBottomRight = true;
			}
			else if ((selectedTekstElement.bottomLeftRect != null) && selectedTekstElement.bottomLeftRect.contains(x,y))
			{	handleAction = true;
				scalingBottomLeft = true;
			}
			else if ((selectedTekstElement.rotateEastHandle != null) && selectedTekstElement.rotateEastHandle.contains(x,y))
			{	handleAction = true;
				rotatingEast = true;
			}
			else if ((selectedTekstElement.rotateWestHandle != null) && selectedTekstElement.rotateWestHandle.contains(x,y))
			{	handleAction = true;
				rotatingWest = true;
			}
			

		}
		
		return handleAction;
	}
	
	public void updateAction()
	{
		if (selectedTekstElement != null )
		{

//System.out.println("ua");

			for (int tCnt = 0; tCnt < tekstElementVector.size(); tCnt++)
			{	TekstElement tekstElement = (TekstElement) tekstElementVector.elementAt(tCnt);

				if (oldSelectedTekstElement.isEqualTo(tekstElement))
				{	
					selectedTekstElement = selectedTekstElement.updateState();
					// extra want selectedTekstElement wordt niet getekend
					selectedTekstElement.makeBB();
					selectedTekstElement.setCenter();
					selectedTekstElement.bb2.rotate(selectedTekstElement.rotation, selectedTekstElement.cx, selectedTekstElement.cy);
					selectedTekstElement.makeHandleBox();
					tekstElement = tekstElement.updateState(selectedTekstElement);
					tekstElementVector.setElementAt(tekstElement, tCnt);
//System.out.println(selectedTekstElement.printTekst());
//System.out.println(tekstElement.printTekst());
				}	
			}
		}
		
	}
	
	public void updateAction2()
	{
		if (selectedTekstElement != null )
		{
			selectedTekstElement = selectedTekstElement.updateState();
			// extra want selectedTekstElement wordt niet getekend
			selectedTekstElement.makeBB();
			selectedTekstElement.setCenter();
			selectedTekstElement.bb2.rotate(selectedTekstElement.rotation, selectedTekstElement.cx, selectedTekstElement.cy);
			selectedTekstElement.makeHandleBox();
		}
				
	}
	
	class MLMML extends MouseAdapter implements MouseMotionListener
	{
		int startX, startY;
		
		public void mousePressed(MouseEvent e)
		{
			if (mouseMode == tekenen)
			{
				//draggPoints.addElement(new Point(e.getX(), e.getY()));
				draggDoublePoints.add(new DoublePoint(e.getX(), e.getY()));
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

				if (tekstVeld.isVisible())
					hideTekstVeld(true);
				tekstEdited = getClickedTekstElement(e.getX(), e.getY());
				//tekstVeld.setLocation(e.getX(), e.getY());
				if (tekstEdited != null)
				{	tekstVeld.setText(tekstEdited.tekst);
					int currentTekstBreedte = tekstFM.stringWidth(tekstEdited.tekst);
					tekstVeld.setLocation(tekstEdited.bb.x, tekstEdited.bb.y);
					tekstVeld.setSize(Math.max(tekstBreedte, currentTekstBreedte + 20), tekstVeld.getSize().height);
				}
				else	
				{	tekstVeld.setText("");
					tekstVeld.setLocation(e.getX(), e.getY());
					tekstVeld.setSize(tekstBreedte, tekstVeld.getSize().height);
					
				}
				tekstVeld.setVisible(true);
				tekstVeld.requestFocus();

			}
			else if (mouseMode == selecteren)
			{

				if (selecteerRechthoekHandlesContain(e.getX(), e.getY()))
				{
					startX = e.getX();
					startY = e.getY();
//System.out.println("mp oshc");			
					
					objectHandled = false;
				}
				else if ((selecteerRechthoek != null) && selecteerRechthoek.contains(e.getX(), e.getY()))
				{
					resetSelectedObject();
					sleepSelectie = true;
					startX = e.getX();
					startY = e.getY();
				}
				else if (objectSelectedHandlesContain(e.getX(), e.getY()))
				{
					startX = e.getX();
					startY = e.getY();
//System.out.println("mp oshc");			
//System.out.println("mp ha " + handleAction);
//System.out.println("mp ta " + scalingBottomRight);

					objectHandled = false;
					
					if (selectedTekstElement != null)
					{	oldSelectedTekstElement = selectedTekstElement;
//System.out.println("old = " + oldSelectedTekstElement.printTekst());					
					}
				}
/*				
				else if (selecteerRechthoekHandlesContain(e.getX(), e.getY()))
				{
					startX = e.getX();
					startY = e.getY();
//System.out.println("mp oshc");			
					
					objectHandled = false;
				}
*/				
				// individueel object aangeklikt, was mogelijk al geselecteerd
				else if (setSelectedObject(e.getX(), e.getY()) || objectSelectedContains(e.getX(), e.getY()))
				{
					sleepSelectie = true;
					startX = e.getX();
					startY = e.getY();
					selecteerRechthoek = null;
					killScaleHandles();
					killRotateHandles();
					resetSelectedObjects();

					objectMoved = false;
				}
/*				
				else if ((selecteerRechthoek != null) && selecteerRechthoek.contains(e.getX(), e.getY()))
				{
					resetSelectedObject();
					sleepSelectie = true;
					startX = e.getX();
					startY = e.getY();
				}
*/				
				else
				{
					sleepSelectie = false;
					resetSelectedObject();
					resetSelectedObjects();
					figuurStart = new Point(e.getX(), e.getY());
					selecteerRechthoek = null;
					killScaleHandles();
					killRotateHandles();
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
				//draggPoints.addElement(new Point(e.getX(), e.getY()));
				draggDoublePoints.add(new DoublePoint(e.getX(), e.getY()));
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
			}
			else if (mouseMode == selecteren)
			{
				if (handleAction)
				{
					int dx = e.getX() - startX;
					int dy = e.getY() - startY;

					processHandleAction(dx,dy);
					
//System.out.println("md ha");

					startX = e.getX();
					startY = e.getY();
					
					objectHandled = true;
					
				}
				else if (groupHandleAction)
				{
					int dx = e.getX() - startX;
					int dy = e.getY() - startY;

					processSelecteerRechthoekHandleAction(dx,dy);
					
					startX = e.getX();
					startY = e.getY();
					
					objectHandled = true;
					
				}
				
				else if (sleepSelectie) // verplaats de selecteerRechthoek met inhoud!!
				{	
					int dx = e.getX() - startX;
					int dy = e.getY() - startY;
					
					if (selecteerRechthoek != null)
					{	selecteerRechthoek.translate(dx, dy);
						if (schalen)
						{
							topRightHandle.translate(dx, dy); 
							bottomRightHandle.translate(dx, dy); 
							topLeftHandle.translate(dx, dy);
							bottomLeftHandle.translate(dx, dy);
							topRightRect.translate(dx, dy);
							bottomRightRect.translate(dx, dy);
							topLeftRect.translate(dx, dy);
							bottomLeftRect.translate(dx, dy);
						}
						if (roteren)
						{
							rotateEastHandle.translate(dx, dy);
							rotateWestHandle.translate(dx, dy);
						}
					}
					
					translateObjectSelected(dx, dy);
					
					translateObjectsSelected(dx, dy);

					startX = e.getX();
					startY = e.getY();
					
					objectMoved = true;
					
				}
				
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
					
					//if (schalen)
					//	makeScaleHandles();
					//if (roteren)
					//	makeRotateHandles();
					findObjectsSelected(selecteerRechthoek);
				}
				
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
				
				ArrayList<DoublePoint> smoothedDraggDoublePoints = smooth(draggDoublePoints, smoothType);
	
				Streep streep = new Streep(drawingColor, smoothedDraggDoublePoints);
				streepVector.addElement(streep);
				if (draggDoublePoints.size() > 1)
					addToHistory();
				//draggPoints.removeAllElements();
				draggDoublePoints.clear();
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
				if (sleepSelectie)
				{	

					sleepSelectie = false;
					//resetSelectedObject();
					if (objectMoved)
						addToHistory();
					objectMoved = false;
					//sleepSelectie = false;
					repaint();
				}
				
				if (objectsSelected.size() == 0)
				{
					selecteerRechthoek = null;
					killScaleHandles();
					killRotateHandles();
					
					repaint();
				}
				else if (objectsSelected.size() == 1)
				{	
					
//System.out.println("oss = 1");					
					Object objectSelected = objectsSelected.elementAt(0);
					if (objectSelected instanceof Streep)
						selectedStreep = (Streep) objectSelected;
					if (objectSelected instanceof Lijn)
						selectedLijn = (Lijn) objectSelected;
					if (objectSelected instanceof Rechthoek)
						selectedRechthoek = (Rechthoek) objectSelected;
					if (objectSelected instanceof Ellips)
						selectedEllips = (Ellips) objectSelected;
					if (objectSelected instanceof TekstElement)
						selectedTekstElement = (TekstElement) objectSelected;
					selecteerRechthoek = null;
					killScaleHandles();
					killRotateHandles();
					resetSelectedObjects();
					
					repaint();
					
					
				}
				else // objectsSelected.size() >= 2
				{
					if (schalen)
						makeScaleHandles();
					if (roteren)
						makeRotateHandles();
					
					repaint();
				}
				
				if (objectHandled)
				{
					updateAction();
					addToHistory();
				}
				objectHandled = false;
				handleAction = false;
				groupHandleAction = false;
				scalingTopRight = false;
				scalingTopLeft = false;
				scalingBottomRight = false;
				scalingBottomLeft = false;
				rotatingEast = false;
				rotatingWest = false;
				angleSum = 0;
				
				repaint();

			}

		}
		
		
		public void mouseMoved(MouseEvent e)
		{
			if (mouseMode == tekstTekenen)
			{
				
			}
			
			if (mouseMode == selecteren)
			{
				
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
			hideTekstVeld(true);
		}
	}
	class TekstKL extends KeyAdapter
	{
		public void keyTyped(KeyEvent e)
		{
			String tekst = tekstVeld.getText();
			int currentTekstBreedte = tekstFM.stringWidth(tekst);
			tekstVeld.setSize(Math.max(tekstBreedte, currentTekstBreedte + 20), tekstVeld.getSize().height);
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