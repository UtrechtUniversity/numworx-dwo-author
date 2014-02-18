package fi.geomalgebra;

import java.awt.Point;
import java.util.Hashtable;
import java.util.HashMap;
import java.util.ArrayList;

public class NoSer 
{
	
	public static HashMap<String,Object> getLijnstukState(Lijnstuk ls)
	{
		HashMap<String,Object> h = new HashMap<String,Object>();
		
		h.put("schaal", new Integer(ls.schaal));
		h.put("stand", new Integer(ls.stand));
		ArrayList<Integer> lengte = new ArrayList<Integer>();
		lengte.add(new Integer(ls.lengte[0]));
		lengte.add(new Integer(ls.lengte[1]));
		lengte.add(new Integer(ls.lengte[2]));
		lengte.add(new Integer(ls.lengte[3]));
		h.put("lengte", lengte);
		ArrayList<Integer> varD = new ArrayList<Integer>();
		varD.add(new Integer(ls.varD[0]));
		varD.add(new Integer(ls.varD[1]));
		varD.add(new Integer(ls.varD[2]));
		varD.add(new Integer(ls.varD[3]));
		h.put("varD", varD);
		h.put("dee", new Integer(ls.d));
		h.put("positieX", new Integer(ls.positie.x));
		h.put("positieY", new Integer(ls.positie.y));
		h.put("isVar", new Boolean(ls.isVar));
		h.put("varNaam", new String(ls.varNaam));
		
		
		return h;
	}

	public static ArrayList<Integer> getLijnstukState2(Lijnstuk ls)
	{
		ArrayList<Integer> a = new ArrayList<Integer>();
		
		a.add(new Integer(ls.schaal));
		a.add(new Integer(ls.stand));
		a.add(new Integer(ls.lengte[0]));
		a.add(new Integer(ls.lengte[1]));
		a.add(new Integer(ls.lengte[2]));
		a.add(new Integer(ls.lengte[3]));
		a.add(new Integer(ls.varD[0]));
		a.add(new Integer(ls.varD[1]));
		a.add(new Integer(ls.varD[2]));
		a.add(new Integer(ls.varD[3]));
		a.add(new Integer(ls.d));
		a.add(new Integer(ls.positie.x));
		a.add(new Integer(ls.positie.y));
		
		//h.put("isVar", new Boolean(ls.isVar));
		//h.put("varNaam", new String(ls.varNaam));
		
		return a;
	}
	
	public static Lijnstuk setLijnstukState(HashMap h)
	{
		int schaal = 24;
		if (h.containsKey("schaal"))
			schaal = ((Integer) h.get("schaal")).intValue();
		int stand = 0;
		if (h.containsKey("stand"))
			stand = ((Integer) h.get("stand")).intValue();
		int[] lengte = new int[4];
		ArrayList<Integer> lengteAL = new ArrayList<Integer>();
		if (h.containsKey("lengte"))
			lengteAL = (ArrayList<Integer>) h.get("lengte");
		for (int cnt = 0; cnt < 4; cnt++)
		{	int l = ((Integer) lengteAL.get(cnt)).intValue();
			lengte[cnt] = l;
		}
		int[] varD = new int[4];
		ArrayList<Integer> varDAL = new ArrayList<Integer>();
		if (h.containsKey("varD"))
			varDAL = (ArrayList<Integer>) h.get("varD");
		for (int cnt = 0; cnt < 4; cnt++)
		{	int v = ((Integer) varDAL.get(cnt)).intValue();
			varD[cnt] = v;
		}
		int d = 0;
		if (h.containsKey("dee"))
			d = ((Integer) h.get("dee")).intValue();
		int positieX = 0;
		int positieY = 0;
		if (h.containsKey("positieX"))
			positieX = ((Integer) h.get("positieX")).intValue();
		if (h.containsKey("positieY"))
			positieY = ((Integer) h.get("positieY")).intValue();
		boolean isVar = false;
		if (h.containsKey("isVar"))
			isVar = ((Boolean) h.get("isVar")).booleanValue();
		String varNaam = "";
		if (h.containsKey("varNaam"))
			varNaam = (String) h.get("varNaam");
		
		// constructor var, l, st, x, y
		Lijnstuk ls = new Lijnstuk(0, 0, stand, positieX, positieY);
		ls.schaal = schaal;
		ls.lengte = lengte;
		ls.varD = varD;
		ls.d = d;
		ls.isVar = isVar;
		ls.varNaam = varNaam;
		
		return ls;
	}
/*	
	ok aantalx = f.aantalx;
	ok aantaly = f.aantaly;
	lsx = new Lijnstuk[20];
	lsy = new Lijnstuk[20];
	positie = new Point(f.positie.x,f.positie.y);
	posx = new Point(f.posx.x,f.posx.y);
	posy = new Point(f.posy.x,f.posy.y);
	for(int i=0 ; i<aantalx ; i++)
	{	lsx[i] = new Lijnstuk(f.lsx[i]);
	}
	for(int i=0 ; i<aantaly ; i++)
	{	lsy[i] = new Lijnstuk(f.lsy[i]);
	}	
*/
	
	
	public static HashMap<String,Object> getFiguurState(Figuur fig)
	{
		HashMap<String,Object> h = new HashMap<String,Object>();
		
		h.put("aantalx", new Integer(fig.aantalx));
		h.put("aantaly", new Integer(fig.aantaly));
		h.put("positieX", new Integer(fig.positie.x));
		h.put("positieY", new Integer(fig.positie.y));
		
		ArrayList<HashMap<String,Object>> lsxHash = new ArrayList<HashMap<String,Object>>(); 
		
		for (int i = 0; i < fig.aantalx ; i++)
		{	HashMap<String,Object> lsxh = getLijnstukState(fig.lsx[i]);
			lsxHash.add(lsxh);

		}
		h.put("lsx", lsxHash);
		
		ArrayList<HashMap<String,Object>> lsyHash = new ArrayList<HashMap<String,Object>>();

		for (int i = 0; i < fig.aantaly ; i++)
		{	HashMap<String,Object> lsyh = getLijnstukState(fig.lsy[i]);
			lsyHash.add(lsyh);

		}
		h.put("lsy", lsyHash);
		
		return h;
	}
	
	public static HashMap<String,Object> getFiguurState2(Figuur fig)
	{
		HashMap<String,Object> h = new HashMap<String,Object>();
		
		h.put("aantalx", new Integer(fig.aantalx));
		h.put("aantaly", new Integer(fig.aantaly));
		h.put("positieX", new Integer(fig.positie.x));
		h.put("positieY", new Integer(fig.positie.y));
		for (int xCnt = 0; xCnt < fig.aantalx; xCnt++)
		{	String name1 = "lsx" + xCnt;
			h.put(name1, getLijnstukState2(fig.lsx[xCnt]));
			String name2 = "xisvar" + xCnt;
			h.put(name2, new Boolean(fig.lsx[xCnt].isVar));
			String name3 = "xvarnaam" + xCnt;
			h.put(name3, fig.lsx[xCnt].varNaam);
			
		}
		for (int yCnt = 0; yCnt < fig.aantaly; yCnt++)
		{	String name1 = "lsy" + yCnt;
			h.put(name1, getLijnstukState2(fig.lsy[yCnt]));
			String name2 = "yisvar" + yCnt;
			h.put(name2, new Boolean(fig.lsy[yCnt].isVar));
			String name3 = "yvarnaam" + yCnt;
			h.put(name3, fig.lsy[yCnt].varNaam);
			
		}
		
		return h;
	}
	
	public static Figuur setFiguurState(HashMap<String,Object> h)
	{
		int aantalx = 0;
		if (h.containsKey("aantalx"))
			aantalx = ((Integer) h.get("aantalx")).intValue();
		int aantaly = 0;
		if (h.containsKey("aantaly"))
			aantaly = ((Integer) h.get("aantaly")).intValue();
		int positieX = 0;
		int positieY = 0;
		if (h.containsKey("positieX"))
			positieX = ((Integer) h.get("positieX")).intValue();
		if (h.containsKey("positieY"))
			positieY = ((Integer) h.get("positieY")).intValue();
//		Point positie = new Point(0, 0);
//		if (h.containsKey("positie"))
//			positie = (Point) h.get("positie");
		//Hashtable[] lsxHash = new Hashtable[aantalx];
		ArrayList<HashMap> lsxHash = new ArrayList<HashMap>();
		if (h.containsKey("lsx"))
			lsxHash = (ArrayList<HashMap>) h.get("lsx");
		//Hashtable[] lsyHash = new Hashtable[aantaly];
		ArrayList<HashMap> lsyHash = new ArrayList<HashMap>();
		if (h.containsKey("lsy"))
			lsyHash = (ArrayList<HashMap>) h.get("lsy");

		
		Figuur fig = new Figuur(positieX, positieY);
		fig.aantalx = aantalx;
		fig.aantaly = aantaly;
		for (int i = 0; i < fig.aantalx ; i++)
		{	//fig.lsx[i] = setLijnstukState(lsxHash[i]);
			fig.lsx[i] = setLijnstukState(lsxHash.get(i));
		}
		for (int i = 0; i < fig.aantaly ; i++)
		{	fig.lsy[i] = setLijnstukState(lsyHash.get(i));
		
		}
		
		
		return fig;
	}
	
	//private Figuur[] figurenrij;
	//private int aantalFg;
	//private int varx,vary,varz;
	
	
	public static HashMap<String,Object> getStateState(State s)
	{
		HashMap<String,Object> h = new HashMap<String,Object>();
		int aantalFg = s.geefAantalFiguren();
		h.put("aantalFg", new Integer(aantalFg));
		
		ArrayList<HashMap<String,Object>> figurenHash = new ArrayList<HashMap<String,Object>>(); 
		Figuur[] figurenrij = s.geefFigurenRij();
		for (int i = 0; i < aantalFg; i++)
		{	
	
//HashMap<String,Object> figuurState2 = getFiguurState2(figurenrij[i]);
//if (figuurState2 == null)
//System.out.println("figState null");

			figurenHash.add(getFiguurState2(figurenrij[i]));				

		}
		h.put("figurenrij", figurenHash);
		
		int[] var = s.geefVars();
		
		ArrayList<Integer> varList = new ArrayList<Integer>();
		for (int vCnt = 0; vCnt < var.length; vCnt++)
			varList.add(new Integer(var[vCnt]));
		h.put("varList", varList);
		
		
		return h;
	}
	
	public static State setStateState(HashMap h)
	{
		int aantalFg = 0;
		if (h.containsKey("aantalFg"))
			aantalFg = ((Integer) h.get("aantalFg")).intValue();
		//Hashtable[] figurenHash = new Hashtable[aantalFg];
		ArrayList<HashMap> figurenHash = new ArrayList<HashMap>();		
		if (h.containsKey("figurenrij"))
			figurenHash = (ArrayList<HashMap>) h.get("figurenrij");
		Figuur[] figurenrij = new Figuur[aantalFg];
		for (int i = 0; i < aantalFg; i++)
		{	//figurenrij[i] = setFiguurState(figurenHash[i]);
			figurenrij[i] = setFiguurState(figurenHash.get(i));
		}
		int[] var = new int[4];
		ArrayList<Integer> varList = new ArrayList<Integer>(); 
		
		// backwards compatibility
		if (h.containsKey("var"))
			var = (int[]) h.get("var");
		 
		if (h.containsKey("varList"))
		{	varList = (ArrayList<Integer>) h.get("varList");
			for (int vCnt = 0; vCnt < varList.size(); vCnt++)
				var[vCnt] = ((Integer) varList.get(vCnt)).intValue();
		}
		
		State s = new State(aantalFg, figurenrij, var);
		
		return s;
		
		
	}
	
	
}


