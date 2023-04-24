package fi.algebrapijlenopdr.expressies_ap;

import java.text.DecimalFormat;
import java.util.*;
import java.awt.*;

//import fi.wiskopdr.WiskOpdr;
//import fi.algebrapijlenopdr.formuleobjects.*;

public class Algebra
{	
	private static double tryValuesStart = 0;
	private static double tryValuesWidthfactor = 1;
	private static double absPrecision = 0.000000001;
	private static double relPrecision = 0.000000001;
	
	public static void setTestValues(double start, double end)
	{	tryValuesStart = start;
		tryValuesWidthfactor = (end - start)/5;
	}
	
	public static void setDefaultTestValues()
	{	tryValuesStart = 0;
		tryValuesWidthfactor = 1;
	}
	
	public static void setAbsPrecision(double d)
	{	if(d<0.000000001)setDefaultAbsPrecision();
		else absPrecision = d+0.0000000000001;
	}
	
	public static void setDefaultAbsPrecision()
	{	absPrecision = 0.000000001;
	}
	
	public static boolean checkGelijkwaardig(Expressie e1, Expressie e2, String[] varNamen, double[] tryValues)
	{	double[] defaultTryIntValues = {0.101, 1.102, 2.103, 3.104, 7.105};
		if (tryValues == null) 
			tryValues = defaultTryIntValues;
/*		
System.out.println("e1 = " + e1.toString());
System.out.println("e2 = " + e2.toString());
Double e1D = e1.geefWaarde();
Double e2D = e2.geefWaarde();
if (e1D == null)
System.out.println("e1D null");
if (e2D == null)
System.out.println("e2D null");
*/

		double e1Waarde = e1.geefWaarde().doubleValue();
		double e2Waarde = e2.geefWaarde().doubleValue();
		if (varNamen.length == 0  || (!Double.isNaN(e1Waarde) && !Double.isNaN(e2Waarde)))
		{	boolean nan1 = (Double.isInfinite (e1Waarde) || Double.isNaN(e1Waarde));
			boolean nan2 = (Double.isInfinite (e2Waarde) || Double.isNaN(e2Waarde));
			boolean ongelijk = Math.abs(e1Waarde - e2Waarde)>absPrecision &&  Math.abs(e1Waarde/e2Waarde-1)>relPrecision;
			//System.out.println(e1Waarde +"  "+ e1.toString() +"  "+ e2Waarde+"  "+ e2.toString());
			if(nan1 && !nan2 || !nan1 &&nan2 || (ongelijk && !(nan1 && nan2)))
			{	return false;
			}
			return true;
		}
		else
		{	String[] varNamenNieuw = new String[varNamen.length - 1];
			for (int i = 0; i < varNamen.length - 1; i++)
			{	varNamenNieuw[i] = varNamen[i+1];
			}
			for (int i = 0; i < tryValues.length; i++)
			{	double value = tryValues[i];
				//System.out.println(""+value);
				Expressie ee1 = e1.substitueer(value, varNamen[0]);
                Expressie ee2 = e2.substitueer(value, varNamen[0]);
                double[] tryValuesNieuw = new double[tryValues.length];
                for (int j = 0; j < tryValues.length; j++) 
                	tryValuesNieuw[j] = tryValues[j]+0.012*tryValuesWidthfactor;
                boolean gelijkwaardig = checkGelijkwaardig(ee1, ee2, varNamenNieuw, tryValuesNieuw);
				//System.out.println(""+i);
				if (!gelijkwaardig)
					return false;
			}
			return true;
		}
	}
	
	public static boolean domainTryValuesOK(Expressie e1, Expressie e2, String[] varNamen, double[] tryValues)
	{	double[] defaultTryIntValues = {0.101, 1.102, 2.103, 3.104, 7.105};
		if (tryValues == null) 
			tryValues = defaultTryIntValues;
		double e1Waarde = e1.geefWaarde().doubleValue();
		double e2Waarde = e2.geefWaarde().doubleValue();
		if (varNamen.length == 0  || (!Double.isNaN(e1Waarde) && !Double.isNaN(e2Waarde)))
		{	boolean nan1 = (Double.isInfinite(e1Waarde) || Double.isNaN(e1Waarde));
			boolean nan2 = (Double.isInfinite(e2Waarde) || Double.isNaN(e2Waarde));
			//boolean ongelijk = Math.abs(e1Waarde - e2Waarde)>0.000000001 &&  Math.abs(e1Waarde/e2Waarde-1)>0.000000001;
			if (!(nan1 && nan2))
			{	return true;
			}
			return false;
		}
		else
		{	String[] varNamenNieuw = new String[varNamen.length-1];
			for(int i=0 ; i<varNamen.length-1 ; i++)
			{	varNamenNieuw[i] = varNamen[i+1];
			}
			for(int i=0 ; i<tryValues.length ; i++)
			{	double value = tryValues[i];
				Expressie ee1 = e1.substitueer(value, varNamen[0]);
                Expressie ee2 = e2.substitueer(value, varNamen[0]);
                double[] tryValuesNieuw = new double[tryValues.length];
                for(int j=0 ; j<tryValues.length ; j++) tryValuesNieuw[j] = tryValues[j]+0.012*tryValuesWidthfactor;
                boolean domainTryValuesOK = domainTryValuesOK(ee1,ee2,varNamenNieuw,tryValuesNieuw);
				if(domainTryValuesOK)return true;
			}
			return false;
		}
	}
	
	
	/*Bepaalt of twee expressies gelijkwaardig zijn.
	 *Dit gebeurt door voor de aanwezige variabelen een tiental waarden
	 *in te vullen. Gaat alleen goed voor expressies met minder dan 7
	 *variabelen. Bij meer variabelen wordt de terugkeerwaarde false.
	 *Bovendien gaat het fout indien beide expressies een domein hebben
	 *waar deze tien testwaarden niet in voorkomen. In dat geval is de
	 *terugkeerwaarde true.
	 */
	
	public static boolean isGelijkwaardig(Expressie e1, Expressie e2)
	{	String[] vars = geefVarNamen((new Optelling(e1, e2)));
		String[] varsNieuw = null;
		if (vars.length > 0) 
			varsNieuw = new String[vars.length - 1];
		boolean complex = false;
		int teller = 0;
		for (int i = 0; i < vars.length; i++)
		{	if (!complex && vars[i].equals("i")) 
			{	complex = true;
			}
			else 
			{	teller++;
				if (teller - 1 < varsNieuw.length)
					varsNieuw[teller - 1] = vars[i];
			}
		}
		double[] tryIntValues = { 
			    tryValuesStart+0.101*tryValuesWidthfactor, 
				tryValuesStart+1.102*tryValuesWidthfactor, 
				tryValuesStart+2.103*tryValuesWidthfactor, 
				tryValuesStart+3.104*tryValuesWidthfactor, 
				tryValuesStart+4.105*tryValuesWidthfactor };
		double[] tryIntValuesBeperkt = {
				tryValuesStart+0.101*tryValuesWidthfactor, 
				tryValuesStart+2.102*tryValuesWidthfactor, 
				tryValuesStart+4.103*tryValuesWidthfactor };
		if (vars.length > 3)
			tryIntValues = tryIntValuesBeperkt;
//		if (complex) 
//			return checkGelijkwaardigComplex(e1, e2, varsNieuw, tryIntValues);
//		if (!domainTryValuesOK(e1, e2, vars, null)) 
//			return checkGelijkwaardigComplex(e1, e2, vars, tryIntValues);
		return checkGelijkwaardig(e1, e2, vars, tryIntValues);
	}
	
	
	
	public static boolean isGelijkDouble(double d1, double d2)
	{	return Math.abs(d1-d2)<0.000000001;
		
	}
	
	public static boolean isGelijkDouble(double d1, double d2, double marge)
	{	return Math.abs(d1-d2)<marge;
		
	}
	
	
	
	public static boolean gelijkGevormd(Expressie e1, Expressie e2)
	{	return zijnGelijk(e1,e2,true);
	}
	
	public static boolean zijnGelijk(Expressie e1, Expressie e2)
	{	return zijnGelijk(e1,e2,false);
	}
	
	public static boolean zijnGelijk(Expressie e1, Expressie e2, boolean vorm)
	{	if(e1==null || e2==null) 
			return false;
	
		if( vorm && e2 instanceof BasisExpressie && e2.toString().equals("G")
				&& !Double.isNaN(e1.geefWaarde().doubleValue())) 
			return true;
		if( vorm && e2 instanceof BasisExpressie && e2.toString().equals("Q")) 
			return true;
	
		if(e1.toStringStrikt().equals(e2.toStringStrikt())) 
			return true;
		//else if(e1 instanceof BasisExpressie && e2 instanceof BasisExpressie)
		//{	if(e1.toStringStrikt().equals(e2.toStringStrikt())) return true;
		//	else return false;
		//}
		else if(e1 instanceof BasisExpressie && e2 instanceof BasisExpressie)
		{	return false;
		}
		else if(e1 instanceof BasisExpressie || e2 instanceof BasisExpressie)
		{	return false;
		}
		else if(e1 instanceof Wortel && e2 instanceof Wortel)
		{	return zijnGelijk(e1.kind1,e2.kind1,vorm);
		}
		else if(e1 instanceof Wortel || e2 instanceof Wortel)
		{	return false;
		}
		else if(e1 instanceof Deling && e2 instanceof Deling && e1.kind1 instanceof BasisExpressie && 
				isGelijkDouble(e1.kind1.geefWaarde().doubleValue(), 1)  && e2.kind1 instanceof BasisExpressie && 
				isGelijkDouble(e2.kind1.geefWaarde().doubleValue(), 1))
		{	return zijnGelijk(e1.kind2,e2.kind2,vorm);
		}
		//else if(e1 instanceof Deling && e2 instanceof Deling && e1.kind1 instanceof BasisExpressie && e2.kind1 instanceof BasisExpressie && isGelijkDouble(e1.kind1.geefWaarde(), e2.kind1.geefWaarde()))
		//{	return zijnGelijk(e1.kind2,e2.kind2);
		//}
		else if(e1 instanceof Macht && e2 instanceof Macht)
		{	return zijnGelijk(e1.kind1,e2.kind1,vorm) && zijnGelijk(e1.kind2,e2.kind2,vorm);
		}
		else if(e1 instanceof Macht || e2 instanceof Macht)
		{	return false;
		}
		else if(e1 instanceof Aftrekking && e2 instanceof Aftrekking && 
				e1.kind1 instanceof BasisExpressie && e1.kind1.geefWaarde().doubleValue()==0 && 
				e2.kind1 instanceof BasisExpressie && e2.kind1.geefWaarde().doubleValue()==0)
		{	return zijnGelijk(e1.kind2,e2.kind2,vorm);
		}
		Vector v1 = geefTermen(e1,new Vector());
		Vector v2 = geefTermen(e2,new Vector());
		if(v1.size()==1 && v2.size()==1)
		{	e1 = (Expressie)v1.elementAt(0);
			e2 = (Expressie)v2.elementAt(0);
			v1 = geefFactorenBeperkt(e1,new Vector());
			v2 = geefFactorenBeperkt(e2,new Vector());

/*			
			if(vorm)
			{	Vector v3 = new Vector();
				Expressie eGetal = new BasisExpressie("1");
				boolean vervang = false;
				for(int i=0 ; i<v1.size() ; i++)
				{	Expressie ee = (Expressie)v1.elementAt(i);
					if(eval(ee)!=null)
					{	eGetal = evalueerGetalsExpressie(new Vermenigvuldiging(eGetal,ee));
						vervang = true;
					}
					else v3.addElement(ee);
				}
				if(vervang)v3.addElement(eGetal);
				v1 = v3;
			}
*/			
			if(v1.size()==1 && v2.size()==1)
			{	e1 = (Expressie)v1.elementAt(0);
				e2 = (Expressie)v2.elementAt(0);
				//System.out.println(e1.toStringStrikt());
				//System.out.println(e2.toStringStrikt());
				if((isBreukPlusGetal(e1) || isBreukPlusGetal(e2)) && !e1.toStringStrikt().equals(e2.toStringStrikt())) return false;
				else if(e1.toStringStrikt().length()>0 && e2.toStringStrikt().length()>0 && !e1.toStringStrikt().substring(0,1).equals(e2.toStringStrikt().substring(0,1))) return false;
				else if(e1.toStringStrikt().length()>1 && e2.toStringStrikt().length()>1 && !e1.toStringStrikt().substring(0,2).equals(e2.toStringStrikt().substring(0,2))) return false;
				else if(e1.toStringStrikt().length()>3 && e2.toStringStrikt().length()>3 && e1.toStringStrikt().substring(0,3).equals("arc") && !e1.toStringStrikt().substring(0,4).equals(e2.toStringStrikt().substring(0,4))) return false;
				return zijnGelijk(e1,e2,vorm);
			}
			else return zijnGelijk(v1,v2,vorm);
		}
		else return zijnGelijk(v1,v2,vorm);
		
	
	}
	
	public static boolean zijnGelijk(Vector v1, Vector v2, boolean vorm)
	{	
		if(vorm)
		{	Vector v4 = new Vector();
			for(int j=0 ; j<v2.size(); j++)
			{	Expressie e2 = (Expressie)v2.elementAt(j);
				if(vorm && e2 instanceof BasisExpressie && e2.toString().equals("G")) 
				{ 	v4.addElement(e2);
				}
				else if(vorm && e2 instanceof BasisExpressie && e2.toString().equals("Q")) 
				{ 	v4.addElement(e2);
				}
				else v4.add(0,e2);
			}
			v2 = v4;
			
		}
		int aantal1 = v1.size();
		int aantal2 = v2.size();
		//System.out.println("v1 "+v1.toString());
		//System.out.println("v2 "+v2.toString());
		if(aantal1 != aantal2) return false;
		boolean zijnGelijk = true;
		for(int i=0 ; i<aantal1 ; i++)
		{	boolean bevat = false;
			Expressie e1 = (Expressie)v1.elementAt(i);
			for(int j=0 ; j<v2.size() ; j++)
			{	
				//System.out.println(v2.toString());
				Expressie e2 = (Expressie)v2.elementAt(j);
				if(zijnGelijk(e1,e2, vorm)) 
				{ 	bevat = true;
					v2.removeElementAt(j);
					break;
				}
			}
			if(!bevat) 
			{	zijnGelijk = false;
				break;
			}
		}
		return zijnGelijk;
	}
	
	/*Geeft de variabele namen van een expressie
	 *Geeft een vector met Strings terug.
	 */
	public static Vector geefVarN(Expressie e)
	{	Vector v;
		if (e instanceof BasisExpressie)
		{	Vector v0 = new Vector();
			if(e.geefVarNaam()!=null)v0.addElement(e.geefVarNaam());
			return v0;
		}
		
		v=geefVarN(e.kind1);
		if(e.kind2 !=null)
		{	Vector v2 = geefVarN(e.kind2);
			int lengte = v.size();
			for(int i=0 ; i<v2.size() ; i++)
			{	boolean anders = true;
				for(int j=0 ; j<lengte ; j++)
				{	if(((String)v.elementAt(j)).equals(((String)v2.elementAt(i))))
					{	anders = false;
					}
				}
				if(anders)v.addElement(v2.elementAt(i));
			}
		}
/*		
		if(e.kind3 !=null)
		{	Vector v2 = geefVarN(e.kind3);
			int lengte = v.size();
			for(int i=0 ; i<v2.size() ; i++)
			{	boolean anders = true;
				for(int j=0 ; j<lengte ; j++)
				{	if(((String)v.elementAt(j)).equals(((String)v2.elementAt(i))))
					{	anders = false;
					}
				}
				if(anders)v.addElement(v2.elementAt(i));
			}
		}
*/
/*		
		if(e.kind4 !=null)
		{	Vector v2 = geefVarN(e.kind4);
			int lengte = v.size();
			for(int i=0 ; i<v2.size() ; i++)
			{	boolean anders = true;
				for(int j=0 ; j<lengte ; j++)
				{	if(((String)v.elementAt(j)).equals(((String)v2.elementAt(i))))
					{	anders = false;
					}
				}
				if(anders)v.addElement(v2.elementAt(i));
			}
		}
*/		
		return v;
	}
	/*Geeft de variabele namen van een expressie
	 *Geeft een array met Strings terug.
	 */
	public static String[] geefVarNamen(Expressie e)
	{	Vector varn = Algebra.geefVarN(e);
		String[] varNamen = new String[varn.size()];
		for (int i = 0; i < varn.size(); i++)
		{	varNamen[i] = (String) varn.elementAt(i);
		}
		return varNamen;
	}
	
	public static boolean bevatVarNaam(Expressie e, String s)
	{	String[] varNamen = geefVarNamen(e) ;
		for(int i=0 ; i<varNamen.length; i++)
	    {	if(s.equals(varNamen[i]))return true;
	    }
	    return false;
	}
	/*Geeft de termen van een expressie en stopt ze in een meegegeven vector.
	 */
	public static Vector geefTermen(Expressie e, Vector v)
	{	if((e instanceof Optelling || e instanceof Aftrekking)
			&& !
				  (e instanceof Optelling
				   && e.kind1 instanceof BasisExpressie
				   && !Double.isNaN(e.kind1.geefWaarde().doubleValue())
				   && e.kind2 instanceof Deling
				   && e.kind2.kind1 instanceof BasisExpressie
				   && !Double.isNaN(e.kind2.kind1.geefWaarde().doubleValue())
				   && e.kind2.kind2 instanceof BasisExpressie
				   && !Double.isNaN(e.kind2.kind2.geefWaarde().doubleValue())
					)
				)
		{	
			if(e instanceof Optelling 
			   && (e.kind2 instanceof Optelling || e.kind2 instanceof Aftrekking))
			{	v = geefTermen(e.kind2,v);
			}
			else
			{	if(e instanceof Optelling)
				{	v.insertElementAt(e.kind2,0);
				}
				else 
				{	Expressie en = new Aftrekking(new BasisExpressie("0"),e.kind2);
					v.insertElementAt(en,0);
				}
			}
			v = geefTermen(e.kind1,v);
		}
		else
		{	if(!(e instanceof BasisExpressie && e.geefWaarde().doubleValue()==0))v.insertElementAt(e,0);
		}
		return v;
	}
	/*Maakt een somexpressie van de expressies in de meegegeven vector
	 */
	public static Expressie maakTermenExpressie(Vector v)
	{	Expressie e = null;
		if(v.size()>0)e = (Expressie)v.elementAt(0);
		else return new BasisExpressie("0");
		for(int i=1 ; i<v.size() ; i++)
		{	Expressie ee = (Expressie)v.elementAt(i);
			if(ee instanceof Aftrekking && ee.kind1.geefWaarde().doubleValue()==0)
			{	e = new Aftrekking(e,ee.kind2);
			}
			else
			{	e = new Optelling(e,ee);
			}
		}
		return e;
	}
	/*Geeft de factoren van de expressie en stopt ze in een meegegeven vector.
	 *Bij gebroken expressies worden een factor f in de noemen als 1/f 
	 *toegevoegd aan de vector
	 */
	public static Vector geefFactoren(Expressie e, Vector v)
	{	if(e instanceof Aftrekking && e.kind1.geefWaarde().doubleValue()==0)
		{	v = Algebra.geefFactoren(e.kind2,v);
			v.addElement(new BasisExpressie("-1"));
		}
		else if(e instanceof Deling)
		{	//if(!(e.kind1 instanceof BasisExpressie && e.kind1.geefWaarde()==1)) 
			v = Algebra.geefFactoren(e.kind1,v);
			Vector u = Algebra.geefFactoren(e.kind2,new Vector());
			for (int i=0 ; i<u.size() ; i++)
			{	Expressie ee = (Expressie)u.elementAt(i);
				if(ee instanceof Deling)
				{	v = Algebra.geefFactoren(ee.kind2,v);
				}
				else
				{	v.addElement(new Deling(new BasisExpressie("1"),(Expressie)u.elementAt(i)));
				}
			}
		}
		else if(e instanceof Vermenigvuldiging)
		{	v = Algebra.geefFactoren(e.kind1,v);
			v = Algebra.geefFactoren(e.kind2,v);
		}
		else if(e instanceof Macht && !Double.isNaN(e.kind2.geefWaarde().doubleValue())&& 
				(e.kind2.geefWaarde().doubleValue() - (int)(e.kind2.geefWaarde().doubleValue()))==0)
		{	if(e.kind2.geefWaarde().doubleValue()>0)
			{	for (int i=0 ; i<e.kind2.geefWaarde().doubleValue() ; i++)
				{	v = Algebra.geefFactoren(e.kind1,v);
				}
			}
			else
			{	for (int i=0 ; i<-e.kind2.geefWaarde().doubleValue() ; i++)
				{	Vector u = Algebra.geefFactoren(e.kind1,new Vector());
					for (int j=0 ; j<u.size() ; j++)
					{	v.addElement(new Deling(new BasisExpressie("1"),(Expressie)u.elementAt(j)));
					}
					//v.addElement(new Deling(new BasisExpressie(1),e.kind1));
				}
			}
		}
		else
		{	v.addElement(e);
		}
		return v;
	}
	
	/*toevoeging voor "Herleiden" 
	 *
	 */
	public static int geefAantalFactorenTermen(Expressie e)
	{	int aantal = 0;
		Vector v = geefTermen(e, new Vector());
		for(int i=0 ; i<v.size() ; i++)
		{	Expressie exp = (Expressie)v.elementAt(i);
			Vector u = geefFactorenBeperkt(exp, new Vector());
			aantal+=u.size();
		}
		return aantal;
	} 
	
	/*toevoeging voor "Herleiden" 
	 *Geeft het aantal factoren in alle termen samen. Machten worden als één factor geteld.
	 */
	public static Vector geefFactorenBeperkt(Expressie e, Vector v)
	{	if(e instanceof Aftrekking && e.kind1.geefWaarde().doubleValue()==0)
		{	v = Algebra.geefFactorenBeperkt(e.kind2,v);
			v.addElement(new BasisExpressie("-1"));
		}
		else if(e instanceof Deling)
		{	v = Algebra.geefFactorenBeperkt(e.kind1,v);
			Vector u = Algebra.geefFactorenBeperkt(e.kind2,new Vector()); // veranderd. was geefFactoren(
			for (int i=0 ; i<u.size() ; i++)
			{	Expressie ee = (Expressie)u.elementAt(i);
				if(ee instanceof Deling)
				{	v = Algebra.geefFactorenBeperkt(ee.kind2,v);
				}
				else
				{	v.addElement(new Deling(new BasisExpressie("1"),(Expressie)u.elementAt(i)));
				}
			}
		}
		else if(e instanceof Vermenigvuldiging)
		{	v = Algebra.geefFactorenBeperkt(e.kind1,v);
			v = Algebra.geefFactorenBeperkt(e.kind2,v);
		}
		else
		{	v.addElement(e);
		}
		return v;
	}
	/*toevoeging voor "Herleiden" 
	 *
	 */
	public static int geefAantalOperatoren(Expressie e)
	{	int aantal = 0;
		if(e.kind1!=null)aantal += geefAantalOperatoren(e.kind1);
		if(e.kind2!=null)aantal += geefAantalOperatoren(e.kind2);
		if(e.kind1!=null || e.kind2!=null)aantal++;
		return aantal;
	}
	/*toevoeging voor "Herleiden" 
	 *
	 */
	public static int geefAantalBreukPlusGetal(Expressie e)
	{	int aantal = 0;
		if(e.kind1!=null)aantal += geefAantalBreukPlusGetal(e.kind1);
		if(e.kind2!=null)aantal += geefAantalBreukPlusGetal(e.kind2);
		if(e instanceof Optelling
				   && e.kind1 instanceof BasisExpressie
				   && !Double.isNaN(e.kind1.geefWaarde().doubleValue())
				   && e.kind2 instanceof Deling
				   && e.kind2.kind1 instanceof BasisExpressie
				   && !Double.isNaN(e.kind2.kind1.geefWaarde().doubleValue())
				   && e.kind2.kind2 instanceof BasisExpressie
				   && !Double.isNaN(e.kind2.kind2.geefWaarde().doubleValue())
					)aantal++;
		return aantal;
	}
	/*toevoeging voor "Herleiden" 
	 *
	 */
	public static Vector geefMachten(Expressie e, Vector v)
	{	if(e instanceof Macht)v.addElement(e);
		if(e.kind1!=null)Algebra.geefMachten(e.kind1,v);
		if(e.kind2!=null)Algebra.geefMachten(e.kind2,v);
		return v;
	}
	
	public static Vector geefDelingen(Expressie e, Vector v)
	{	if(e instanceof Deling)v.addElement(e);
		if(e.kind1!=null)Algebra.geefDelingen(e.kind1,v);
		if(e.kind2!=null)Algebra.geefDelingen(e.kind2,v);
		return v;
	}
	
	public static Vector geefAftrekkingen(Expressie e, Vector v)
	{	if(e instanceof Aftrekking)v.addElement(e);
		if(e.kind1!=null)Algebra.geefAftrekkingen(e.kind1,v);
		if(e.kind2!=null)Algebra.geefAftrekkingen(e.kind2,v);
		return v;
	}
	
	
	public static boolean isBreukPlusGetal(Expressie e)
	{	if(e instanceof Optelling
				   && e.kind1 instanceof BasisExpressie
				   && !Double.isNaN(e.kind1.geefWaarde().doubleValue())
				   && e.kind2 instanceof Deling
				   && e.kind2.kind1 instanceof BasisExpressie
				   && !Double.isNaN(e.kind2.kind1.geefWaarde().doubleValue())
				   && e.kind2.kind2 instanceof BasisExpressie
				   && !Double.isNaN(e.kind2.kind2.geefWaarde().doubleValue())
					)
		{	return true;
		}
		else if(e instanceof Aftrekking && e.kind1.isBasis && e.kind1.geefWaarde().doubleValue()==0)
		{	if(e.kind2 instanceof Optelling
					&& e.kind2.kind1 instanceof BasisExpressie
				   && !Double.isNaN(e.kind2.kind1.geefWaarde().doubleValue())
				   && e.kind2.kind2 instanceof Deling
				   && e.kind2.kind2.kind1 instanceof BasisExpressie
				   && !Double.isNaN(e.kind2.kind2.kind1.geefWaarde().doubleValue())
				   && e.kind2.kind2.kind2 instanceof BasisExpressie
				   && !Double.isNaN(e.kind2.kind2.kind2.geefWaarde().doubleValue())
					)
			{	return true;
			}
			else return false;
		}
		else return false;
	}
	public static Expressie bijBreukPlusGetalGeefBreuk(Expressie e)
	{	if(e instanceof Optelling
				   && e.kind1 instanceof BasisExpressie
				   && !Double.isNaN(e.kind1.geefWaarde().doubleValue())
				   && e.kind2 instanceof Deling
				   && e.kind2.kind1 instanceof BasisExpressie
				   && !Double.isNaN(e.kind2.kind1.geefWaarde().doubleValue())
				   && e.kind2.kind2 instanceof BasisExpressie
				   && !Double.isNaN(e.kind2.kind2.geefWaarde().doubleValue())
					)
		{	return new Deling(new BasisExpressie("" + 
				(e.kind1.geefWaarde().doubleValue() * e.kind2.kind2.geefWaarde().doubleValue() + 
				 e.kind2.kind1.geefWaarde().doubleValue())),e.kind2.kind2);
		}
		else return null;
	}
	
	/*toevoeging voor "Herleiden" 
	 *
	 */
	public static int geefAantalMachten(Expressie e)
	{	int aantal = 0;
		if(e.kind1!=null)aantal += geefAantalMachten(e.kind1);
		if(e.kind2!=null)aantal += geefAantalMachten(e.kind2);
		if(e instanceof Macht)aantal++;
		return aantal;
	}
	
	/* Bepaalt of een expressie zonder haakjes is geschreven
	 */
	public static boolean expanded(Expressie e)
	{	Vector v = Algebra.geefTermen(e,new Vector());
		for(int i=0 ; i<v.size() ; i++)
		{	Expressie ee = (Expressie)v.elementAt(i);
			Vector u = geefFactoren(ee, new Vector());
			for(int j=0 ; j<u.size() ; j++)
			{	Expressie eee = (Expressie)u.elementAt(j);
				if(Double.isNaN(eee.geefWaarde().doubleValue())
				   && !(eee instanceof BasisExpressie))
				{	return false;
				}
			}
		}
		return true;
	}
	
	
	/*Werkt alle haakjes weg en stopt alle termen (ongesorteerd) in een vector.
	 */
	public static Vector expand(Expressie e, Vector v)
	{	
		if(e instanceof Optelling)
		{	
			v = expand(e.kind1,v);
			v = expand(e.kind2,v);
		}
		else if((e instanceof Aftrekking))
		{	
			v = expand(e.kind1,v);
			v = expand(new Vermenigvuldiging(new BasisExpressie("-1"),e.kind2),v);
		}
		else
		{	if(e instanceof Vermenigvuldiging)
			{	Vector u1 = expand(e.kind1,new Vector());
				Vector u2 = expand(e.kind2,new Vector());
				for(int i=0 ; i<u1.size() ; i++)
				{	for(int j=0 ; j<u2.size() ; j++)
					{	Expressie e1 = (Expressie)u1.elementAt(i);
						Expressie e2 = (Expressie)u2.elementAt(j);
						v.addElement(new Vermenigvuldiging(e1,e2));
					}
				}
			}
			else if(e instanceof Deling)
			{	Vector u1 = expand(e.kind1,new Vector());
				for(int i=0 ; i<u1.size() ; i++)
				{	Expressie e1 = (Expressie)u1.elementAt(i);
					v.addElement(new Deling(e1,e.kind2));
				}
			}
			else if(e instanceof Macht && !Double.isNaN(e.kind2.geefWaarde().doubleValue()))//nog geen controle op exponenten als gehele getallen
			{	if(e.kind2.geefWaarde().doubleValue()==1)
				{	v = expand(e.kind1,v);
				}
				else if(e.kind2.geefWaarde().doubleValue()==-1)
				{	v = expand(new Deling(new BasisExpressie("1"),e.kind1),v);
				}
				
				
				Vector u1 = new Vector();
				Vector u2 = new Vector();
				if(e.kind2.geefWaarde().doubleValue()==2)
				{	u1 = expand(e.kind1,new Vector());
					u2 = expand(e.kind1,new Vector());
				}
				else if(e.kind2.geefWaarde().doubleValue()>2)
				{	u1 = expand(e.kind1,new Vector());
					Expressie exp = new Macht(e.kind1, new BasisExpressie("" + (e.kind2.geefWaarde().doubleValue()-1)));
					u2 = expand(exp,new Vector());
				}
				else if(e.kind2.geefWaarde().doubleValue()==-2)
				{	u1 = expand(new Deling(new BasisExpressie("1"),e.kind1),new Vector());
					u2 = expand(new Deling(new BasisExpressie("1"),e.kind1),new Vector());
				}
				else if(e.kind2.geefWaarde().doubleValue()<-2)
				{	u1 = expand(new Deling(new BasisExpressie("1"),e.kind1),new Vector());
					Expressie exp = new Macht(e.kind1, new BasisExpressie("" + (e.kind2.geefWaarde().doubleValue()+1)));
					u2 = expand(exp,new Vector());
				}
				for(int i=0 ; i<u1.size() ; i++)
				{	for(int j=0 ; j<u2.size() ; j++)
					{	Expressie e1 = (Expressie)u1.elementAt(i);
						Expressie e2 = (Expressie)u2.elementAt(j);
						v.addElement(new Vermenigvuldiging(e1,e2));
					}
				}
			}
			else
			{	if(e.geefWaarde().doubleValue()!=0)
					v.addElement(e);
			}
		}
		
		/**/
		
		return v;
	}
	
	/*Werkt de haakjes weg.
	 *Doet dat per term. Herleid ook elke term.
	 *Kan daarna eventueel als geheel herleid worden met herleid();
	 */
/*	
	public static Expressie verwijderHaakjes(Expressie e)
	{	Vector v = geefTermen(e, new Vector());
		Vector w = new Vector();
		for(int i=0 ; i<v.size() ; i++)
		{	Expressie exp = (Expressie)v.elementAt(i);
			Vector u = expand(exp,new Vector());
			u = sorteerTermen(u);
			for(int j=0 ; j<u.size() ; j++)
			{	w.addElement((Expressie)u.elementAt(j));
			}
		}
		return Algebra.maakTermenExpressie(w);
	}
*/	
	/*Geeft de (indien mogelijk herleide)wortel van een expressie
	 *Herleiding vind plaats als de exponenten van de variabele factoren even zijn
	 *en bij een getalsexpressie indien het kwadraat van een rationaal getal is.
	 */
	
/*	
	public static Expressie geefWortel(Expressie e)
	{	String[] varNamen = geefVarNamen(e);
		Vector t = new Vector();
		Vector extraTermen = new Vector();
		int[] exponenten = new int[varNamen.length];
		int[] exponentenExtra = new int[100]; // die 100 is arbitrair
		
		for(int i=0 ; i<varNamen.length ; i++)
		{	exponenten[i]=0;
		}
		for(int i=0 ; i<100 ; i++)
		{	exponentenExtra[i]=0;
		}
	
		Vector u;
		u = geefFactoren(e,new Vector());
		for(int k=0 ; k<u.size() ; k++)
		{	Expressie exp = (Expressie)u.elementAt(k);
			for(int i=0 ; i<varNamen.length ; i++)
			{	if(exp instanceof BasisExpressie  && ((BasisExpressie)exp).basisString!=null  && ((BasisExpressie)exp).basisString.equals(varNamen[i]))
				{	exponenten[i]++;
					u.setElementAt(new BasisExpressie("1"),k);
				}
				else if(exp instanceof Deling && exp.kind1.geefWaarde().doubleValue()==1  && exp.kind2 instanceof BasisExpressie  && ((BasisExpressie)exp.kind2).basisString!=null  && ((BasisExpressie)exp.kind2).basisString.equals(varNamen[i]))
				{	exponenten[i]--;
					u.setElementAt(new BasisExpressie("1"),k);
				} 
			}				
			if(eval(exp)==null && !(exp instanceof BasisExpressie) && !(exp instanceof Deling && exp.kind1.geefWaarde().doubleValue()==1  && exp.kind2 instanceof BasisExpressie)) 
			{	boolean kwamAlVoor = false;
				for(int i=0 ; i<extraTermen.size() ; i++)
				{	if(isGelijkwaardig(exp,(Expressie)extraTermen.elementAt(i)))
					{	exponentenExtra[i]++;
						u.setElementAt(new BasisExpressie("1"),k);
						kwamAlVoor = true;
					}
					else if(exp instanceof Deling && exp.kind1.geefWaarde().doubleValue()==1 && isGelijkwaardig(exp.kind2,(Expressie)extraTermen.elementAt(i)))
					{	exponentenExtra[i]--;
						u.setElementAt(new BasisExpressie("1"),k);
						kwamAlVoor = true;
					}
				}
				if(!kwamAlVoor)
				{	if(exp instanceof Deling && exp.kind1.geefWaarde().doubleValue()==1)
					{	extraTermen.addElement(exp.kind2);
						exponentenExtra[extraTermen.size()-1]--;
						u.setElementAt(new BasisExpressie("1"),k);
					}
					else
					{	extraTermen.addElement(exp);
						exponentenExtra[extraTermen.size()-1]++;
						u.setElementAt(new BasisExpressie("1"),k);
					}
				}
				
			}	
				
		}
		Expressie getalsExpressie = maakFactorenExpressie(u);
		Expressie wortelGetal = evalueerGetalsExpressie(new Wortel(getalsExpressie));
		
		boolean isKwadraat = true;
		if(wortelGetal instanceof Wortel)isKwadraat = false;
		for(int i=0 ; i<varNamen.length ; i++)
		{	if(exponenten[i]%2==1)isKwadraat = false;
		}
		for(int i=0 ; i<100 ; i++)
		{	if(exponentenExtra[i]%2==1)isKwadraat = false;
		}
		
		if(isKwadraat)
		{	Vector expVector = new Vector();
			expVector.addElement(wortelGetal);
			for(int i=0 ; i<varNamen.length ; i++)
			{	if(exponenten[i]/2==1)
				{	expVector.insertElementAt(new BasisExpressie(varNamen[i]),0);
				}
				else if(exponenten[i]/2>1)
				{	for(int k=0;k<exponenten[i]/2;k++)
					{	expVector.insertElementAt(new BasisExpressie(varNamen[i]),0);
					}
				}
				else if(exponenten[i]==0)
				{	expVector.insertElementAt(new BasisExpressie(1),0);
				}
				else if(exponenten[i]/2==-1)
				{	expVector.insertElementAt(new Deling(new BasisExpressie(1),new BasisExpressie(varNamen[i])),0);
				}
				else if(exponenten[i]/2<-1)
				{	for(int k=0;k<-exponenten[i]/2;k++)
					{	expVector.insertElementAt(new Deling(new BasisExpressie(1),new BasisExpressie(varNamen[i])),0);
					}
				}
			}
			for(int i=0 ; i<extraTermen.size() ; i++)
			{	if(exponentenExtra[i]/2==1)
				{	expVector.insertElementAt((Expressie)extraTermen.elementAt(i),0);
				}
				else if(exponentenExtra[i]>1)
				{	for(int k=0;k<exponentenExtra[i]/2;k++)
					{	expVector.insertElementAt((Expressie)extraTermen.elementAt(i),0);
					}
				}
				else if(exponentenExtra[i]==0)
				{	expVector.insertElementAt(new BasisExpressie(1),0);
				}
				else if(exponentenExtra[i]/2==-1)
				{	expVector.insertElementAt(new Deling(new BasisExpressie(1),(Expressie)extraTermen.elementAt(i)),0);
				}
				else if(exponentenExtra[i]/2<-1)
				{	for(int k=0;k<-exponentenExtra[i]/2;k++)
					{	expVector.insertElementAt(new Deling(new BasisExpressie(1),(Expressie)extraTermen.elementAt(i)),0);
					}
				}
			}
			return maakFactorenExpressie(expVector);
		}
		else return new Wortel(e);
		
	
	}
*/	
	
	/*Ontbind een 2e graads expressie.met 1 variabele.
	 *Lukt alleen als e=0 rationale oplossingen heeft
	 *expressies met meer variabelen en/of van een andere graad
	 *worden ontbonden met ontbindExtra(e)
	 */
/*	
	public static Expressie ontbindExtra(Expressie e)
	{	String[] varnamen = geefVarNamen(e);
	
		Vector v = geefTermen(e, new Vector());
		if(v.size()==2 && (geefCoefficienten(e)==null || geefCoefficienten(e).length>3))
		{	Expressie e1 = (Expressie)v.elementAt(0);
			Expressie e2 = (Expressie)v.elementAt(1);
			if(e1 instanceof Aftrekking && e1.kind1.geefWaarde().doubleValue()==0 && !(geefWortel(e1.kind2)instanceof Wortel) && !(geefWortel(e2)instanceof Wortel))
			{	return new Vermenigvuldiging(new Optelling(geefWortel(e2),geefWortel(e1.kind2)),new Aftrekking(geefWortel(e2),geefWortel(e1.kind2)));
			}
			else if(e2 instanceof Aftrekking && e2.kind1.geefWaarde().doubleValue()==0 && !(geefWortel(e2.kind2)instanceof Wortel) && !(geefWortel(e1)instanceof Wortel))
			{	return new Vermenigvuldiging(new Optelling(geefWortel(e1),geefWortel(e2.kind2)),new Aftrekking(geefWortel(e1),geefWortel(e2.kind2)));
			}
		}
		else if(v.size()==3 && (geefCoefficienten(e)==null || geefCoefficienten(e).length>3))
		{	Expressie e1 = (Expressie)v.elementAt(0);
			Expressie e2 = (Expressie)v.elementAt(1);
			Expressie e3 = (Expressie)v.elementAt(2);
			Expressie e1w = geefWortel(e1);
			Expressie e2w = geefWortel(e2);
			Expressie e3w = geefWortel(e3);
			if(!(e1w instanceof Wortel) && !(e2w instanceof Wortel))
			{	if(isGelijkwaardig(e3,new Vermenigvuldiging(new BasisExpressie("2"),new Vermenigvuldiging(e1w,e2w))))
				{	return new Macht(new Optelling(e1w,e2w),new BasisExpressie("2"));
				}
				else if(isGelijkwaardig(e3,new Vermenigvuldiging(new BasisExpressie("-2"),new Vermenigvuldiging(e1w,e2w))))
				{	return new Macht(new Aftrekking(e1w,e2w),new BasisExpressie("2"));
				}				
			}
			else if(!(e1w instanceof Wortel) && !(e3w instanceof Wortel))
			{	if(isGelijkwaardig(e2,new Vermenigvuldiging(new BasisExpressie("2"),new Vermenigvuldiging(e1w,e3w))))
				{	return new Macht(new Optelling(e1w,e3w),new BasisExpressie("2"));
				}
				else if(isGelijkwaardig(e2,new Vermenigvuldiging(new BasisExpressie("-2"),new Vermenigvuldiging(e1w,e3w))))
				{	return new Macht(new Aftrekking(e1w,e3w),new BasisExpressie("2"));
				}				
			}
			else if(!(e2w instanceof Wortel) && !(e3w instanceof Wortel))
			{	if(isGelijkwaardig(e1,new Vermenigvuldiging(new BasisExpressie("2"),new Vermenigvuldiging(e2w,e3w))))
				{	return new Macht(new Optelling(e2w,e3w),new BasisExpressie("2"));
				}
				else if(isGelijkwaardig(e1,new Vermenigvuldiging(new BasisExpressie("-2"),new Vermenigvuldiging(e2w,e3w))))
				{	return new Macht(new Aftrekking(e2w,e3w),new BasisExpressie("2"));
				}				
			}
			else
			{	e = vermenigvuldig(e,new BasisExpressie("-1"));
				v = geefTermen(e, new Vector());
				e1 = (Expressie)v.elementAt(0);
				e2 = (Expressie)v.elementAt(1);
				e3 = (Expressie)v.elementAt(2);
				e1w = geefWortel(e1);
				e2w = geefWortel(e2);
				e3w = geefWortel(e3);
				if(!(e1w instanceof Wortel) && !(e2w instanceof Wortel))
				{	if(isGelijkwaardig(e3,new Vermenigvuldiging(new BasisExpressie("2"),new Vermenigvuldiging(e1w,e2w))))
					{	return new Aftrekking(new BasisExpressie("0"),new Macht(new Optelling(e1w,e2w),new BasisExpressie("2")));
					}
					else if(isGelijkwaardig(e3,new Vermenigvuldiging(new BasisExpressie("-2"),new Vermenigvuldiging(e1w,e2w))))
					{	return new Aftrekking(new BasisExpressie("0"),new Macht(new Aftrekking(e1w,e2w),new BasisExpressie("2")));
					}				
				}
				else if(!(e1w instanceof Wortel) && !(e3w instanceof Wortel))
				{	if(isGelijkwaardig(e2,new Vermenigvuldiging(new BasisExpressie("2"),new Vermenigvuldiging(e1w,e3w))))
					{	return new Aftrekking(new BasisExpressie("0"),new Macht(new Optelling(e1w,e3w),new BasisExpressie("2")));
					}
					else if(isGelijkwaardig(e2,new Vermenigvuldiging(new BasisExpressie("-2"),new Vermenigvuldiging(e1w,e3w))))
					{	return new Aftrekking(new BasisExpressie("0"),new Macht(new Aftrekking(e1w,e3w),new BasisExpressie("2")));
					}				
				}
				else if(!(e2w instanceof Wortel) && !(e3w instanceof Wortel))
				{	if(isGelijkwaardig(e1,new Vermenigvuldiging(new BasisExpressie("2"),new Vermenigvuldiging(e2w,e3w))))
					{	return new Aftrekking(new BasisExpressie("0"),new Macht(new Optelling(e2w,e3w),new BasisExpressie("2")));
					}
					else if(isGelijkwaardig(e1,new Vermenigvuldiging(new BasisExpressie("-2"),new Vermenigvuldiging(e2w,e3w))))
					{	return new Aftrekking(new BasisExpressie("0"),new Macht(new Aftrekking(e2w,e3w),new BasisExpressie("2")));
					}				
				}
				e = vermenigvuldig(e,new BasisExpressie("-1"));
			}
			
			
		}
		
		//controle op aantal variabelen
		if(varnamen.length>1 || varnamen.length<1)
		{	return(e);
			//return(ontbindExtra(e));
		}
		
		//varnaam vastleggen
		String varnaam = varnamen[0];
		
		//coefficienten bepalen
		Expressie[] exp = geefCoefficientenExpressies(herleid(verwijderHaakjes(e)));
		if(exp==null)
		{	return(e);
			//return(ontbindExtra(e));
		}
		double[] coeff = new double[exp.length];
		for(int i=0 ; i<exp.length ; i++)
		{	coeff[i] = exp[i].geefWaarde().doubleValue();
		}
		//graad ongelijk aan 2
		if(coeff.length!=3)
		{	return(e);
			//return(ontbindExtra(e));
		}
		
		
		
		//coefficienten noemen we a,b en c
		double a = coeff[2];
		double b = coeff[1];
		double c = coeff[0];
		
		// de ggd van a,b en c: factor,  wordt uitgedeeld 
		double factor = 1;
		if(a<0)
		{	a = -a;
			b = -b;
			c = -c;
			exp[2] = vermenigvuldig(exp[2], new BasisExpressie("-1"));
			exp[1] = vermenigvuldig(exp[1], new BasisExpressie("-1"));
			exp[0] = vermenigvuldig(exp[0], new BasisExpressie("-1"));
			factor = -1;
		}
		if(Math.rint(a)-a==0 && Math.rint(b)-b==0 && Math.rint(c)-c==0)
		{	int ggd = (int)ggd((int)a,ggd((int)b,(int)c));
			a/=ggd; 
			b/=ggd; 
			c/=ggd;
			exp[2] = vermenigvuldig(exp[2], new Deling(new BasisExpressie("1"),new BasisExpressie("" + ggd) ));
			exp[1] = vermenigvuldig(exp[1], new Deling(new BasisExpressie("1"),new BasisExpressie("" + ggd) ));
			exp[0] = vermenigvuldig(exp[0], new Deling(new BasisExpressie("1"),new BasisExpressie("" + ggd) ));
			factor=factor*ggd;
		}
		
		//D<0
		double d = b*b-4*a*c;
		if(d<0) 
		{	return(e);
			//return(ontbindExtra(e));
		}
		
		//D=0 Schrijf als p(x+q)^2 (
		if(d<0.000000001)
		{	Expressie factorExpressie = vermenigvuldig(exp[2],new BasisExpressie("" + factor));
			exp[1] = evalueerGetalsExpressie(new Vermenigvuldiging(exp[1],new Deling(new BasisExpressie("1"), exp[2])));
			exp[0] = evalueerGetalsExpressie(new Vermenigvuldiging(exp[0],new Deling(new BasisExpressie("1"), exp[2])));
			Expressie e1 = new Optelling(new BasisExpressie(varnaam),evalueerGetalsExpressie(new Vermenigvuldiging(exp[1],new Deling(new BasisExpressie(1), new BasisExpressie(2)))));
			e1 = herleid(e1);
			Expressie e2 = new Macht(e1, new BasisExpressie(2));
			Expressie ee = vermenigvuldig(e2,factorExpressie);
			return ee;
		}
		
		//als de coefficienten rationaal zijn,dan eerst een factor of breuk 
		//buiten haakjes halen. (nieuwe coefficienten geheel)
		int kgv = 1;
		if(eval(exp[0])!=null && eval(exp[1])!=null && eval(exp[2])!=null)
		{	int d0 = (int)eval(exp[0]).y;
			int d1 = (int)eval(exp[1]).y;
			int d2 = (int)eval(exp[2]).y;
			kgv = (int)(d0*d1/ggd(d0,d1));
			kgv = (int)(kgv*d2/ggd(kgv,d2));
		}
		else
		{	return(e);
			//return(ontbindExtra(e));
		}
		exp[2] = vermenigvuldig(exp[2], new BasisExpressie(kgv));
		exp[1] = vermenigvuldig(exp[1], new BasisExpressie(kgv));
		exp[0] = vermenigvuldig(exp[0], new BasisExpressie(kgv));
		Expressie factorExpressie = new Deling(new BasisExpressie(factor),new BasisExpressie(kgv));
		
		//als sqrt(D) rationaal is, dan ontbinden.
		Expressie discr = new Aftrekking(new Vermenigvuldiging(exp[1],exp[1]),new Vermenigvuldiging(new BasisExpressie(4),new Vermenigvuldiging(exp[2], exp[0])));
		if(eval(new Wortel(discr))==null)
		{	return(e);
			//return(ontbindExtra(e));
		}
		else
		{	Expressie expX1 = evalueerGetalsExpressie(new Optelling(evalueerGetalsExpressie(new Vermenigvuldiging(new BasisExpressie(-1),exp[1])),new Wortel(discr)));
			expX1 = evalueerGetalsExpressie(new Vermenigvuldiging(expX1,new Deling(new BasisExpressie(1), new Vermenigvuldiging(new BasisExpressie(2),exp[2]))));
			Expressie expX2 = evalueerGetalsExpressie(new Aftrekking(evalueerGetalsExpressie(new Vermenigvuldiging(new BasisExpressie(-1),exp[1])),new Wortel(discr)));
			expX2 = evalueerGetalsExpressie(new Vermenigvuldiging(expX2,new Deling(new BasisExpressie(1), new Vermenigvuldiging(new BasisExpressie(2),exp[2]))));
			
			int deler1 = (int)eval(expX1).y;
			Expressie e1 = new Aftrekking(new Vermenigvuldiging(new BasisExpressie(deler1),new BasisExpressie(varnaam)),evalueerGetalsExpressie(new Vermenigvuldiging(expX1,new BasisExpressie(deler1))));
			e1 = herleid(e1);
			
			int deler2 = (int)eval(expX2).y;
			Expressie e2 = new Aftrekking(new Vermenigvuldiging(new BasisExpressie(deler2),new BasisExpressie(varnaam)),evalueerGetalsExpressie(new Vermenigvuldiging(expX2,new BasisExpressie(deler2))));
			e2 = herleid(e2);
			
			Expressie ee = new Vermenigvuldiging(e1,e2);
			ee = vermenigvuldig(ee,factorExpressie);
			ee = herleid(ee);
			return ee;
		}
	}
*/	
	
	/*Brengt factoren buiten haakjes van een willekeurige expressie evt met 
	 *meer variabelen. Brengt ook onder één noemer indien nodig.
	 */
	
/*	
	public static Expressie ontbind(Expressie e)
	{	//Voor alle termen wordt bekeken uit welke bouwstenen ze zijn opgebouwd.
		//Daartoe worden van de termen de aanwezige factoren bepaald met
		//geefFactoren(..). De factoren worden per soort per term geteld,
		//deze gegevens worden opgeslagen in exponenten[][] (de variabelen)
		//en in exponentenExtra[][] (andere niet getals expressies die 
		//als factor voorkomen)
		//Deze andere voorkomende factoren worden bovendien geinventariseerd
		//en opgeslagen in de Vector extraTermen.
		//Na te zijn geteld worden de factoren in de termen vervangen door 1,
		//zodat aan het eind alleen nog een getalsexpressie overblijft
		
		String[] varNamen = geefVarNamen(e);
		Vector v = geefTermen(e,new Vector());
		Vector t = new Vector();
		Vector extraTermen = new Vector();
		int[][] exponenten = new int[varNamen.length][v.size()];
		int[][] exponentenExtra = new int[100][v.size()]; // die 100 is arbitrair
		int[] factorExponenten = new int[varNamen.length];
		int[] factorExponentenExtra = new int[100];
		for(int i=0 ; i<varNamen.length ; i++)
		{	for(int j=0 ; j<v.size() ; j++)
			{	exponenten[i][j]=0;
			}
		}
		for(int i=0 ; i<100 ; i++)
		{	for(int j=0 ; j<v.size() ; j++)
			{	exponentenExtra[i][j]=0;
			}
		}
		for(int i=0 ; i<varNamen.length ; i++)
		{	factorExponenten[i]=0;
		}
		for(int i=0 ; i<100 ; i++)
		{	factorExponentenExtra[i]=0;
		}
		for(int j=0 ; j<v.size() ; j++)
		{	Vector u;
			u = geefFactoren((Expressie)v.elementAt(j),new Vector());
			for(int k=0 ; k<u.size() ; k++)
			{	Expressie exp = (Expressie)u.elementAt(k);
				for(int i=0 ; i<varNamen.length ; i++)
				{	if(exp instanceof BasisExpressie && ((BasisExpressie)exp).basisString!=null  && ((BasisExpressie)exp).basisString.equals(varNamen[i]))
					{	exponenten[i][j]++;
						u.setElementAt(new BasisExpressie("1"),k);
					}
					else if(exp instanceof Deling && exp.kind1.geefWaarde().doubleValue()==1  && exp.kind2 instanceof BasisExpressie  && ((BasisExpressie)exp.kind2).basisString!=null  && ((BasisExpressie)exp.kind2).basisString.equals(varNamen[i]))
					{	exponenten[i][j]--;
						u.setElementAt(new BasisExpressie("1"),k);
					} 
				}
				
				if(eval(exp)==null && !(exp instanceof BasisExpressie) && !(exp instanceof Deling && exp.kind1.geefWaarde().doubleValue()==1  && exp.kind2 instanceof BasisExpressie)) 
				{	boolean kwamAlVoor = false;
					for(int i=0 ; i<extraTermen.size() ; i++)
					{	if(isGelijkwaardig(exp,(Expressie)extraTermen.elementAt(i)))
						{	exponentenExtra[i][j]++;
							u.setElementAt(new BasisExpressie("1"),k);
							kwamAlVoor = true;
						}
						else if(exp instanceof Deling && exp.kind1.geefWaarde().doubleValue()==1 && isGelijkwaardig(exp.kind2,(Expressie)extraTermen.elementAt(i)))
						{	exponentenExtra[i][j]--;
							u.setElementAt(new BasisExpressie("1"),k);
							kwamAlVoor = true;
						}
					}
					if(!kwamAlVoor)
					{	if(exp instanceof Deling && exp.kind1.geefWaarde().doubleValue()==1)
						{	extraTermen.addElement(exp.kind2);
							exponentenExtra[extraTermen.size()-1][j]--;
							u.setElementAt(new BasisExpressie("1"),k);
						}
						else
						{	extraTermen.addElement(exp);
							exponentenExtra[extraTermen.size()-1][j]++;
							u.setElementAt(new BasisExpressie("1"),k);
						}
					}
				
				}	
				
			}
			v.setElementAt(maakFactorenExpressie(u),j);
		}
		
		//Voor elke variabele wordt kleinste exponent die onder de termen 
		//voorkomt bepaald. Deze wordt per variabele opgeslagen in 
		//factorExponenten[] (zoveel kan straks buiten haakjes gehaald worden)
		//exponenten[][] wordt verlaagt met deze waarde.
		//Hetzelfde gebeurt met de extraTermen.
		
		for(int i=0 ; i<varNamen.length ; i++)
		{	int minAantalF = 1000;
			for(int j=0 ; j<v.size() ; j++)
			{	if(exponenten[i][j]<minAantalF)
				{	minAantalF = exponenten[i][j];
				}
			}
			factorExponenten[i] = minAantalF;
			for(int j=0 ; j<v.size() ; j++)
			{	exponenten[i][j] = exponenten[i][j] - minAantalF;
			}
		}
		for(int i=0 ; i<extraTermen.size() ; i++)
		{	int minAantalF = 1000;
			for(int j=0 ; j<v.size() ; j++)
			{	if(exponentenExtra[i][j]<minAantalF)
				{	minAantalF = exponentenExtra[i][j];
				}
			}
			factorExponentenExtra[i] = minAantalF;
			for(int j=0 ; j<v.size() ; j++)
			{	exponentenExtra[i][j] = exponentenExtra[i][j] - minAantalF;
			}
		}
		
		
		
		//de termen (het deel binnen de 'haakjes')worden opnieuw opgebouwd 
		//mbv de gegevens van exponenten[][] en exponentenExtra[][]
		//en tijdelijk opgeslagen in
		//expVector. De nieuwe termen worden opgeslagen in vector t
		//en vervolgens samengevoegd tot de expressie e2
		
		int ggdTeller = 0;
		int ggdNoemer = 0;
		int kgvNoemer = 1;
		boolean heeftGGD = true;
		for(int j=0 ; j<v.size() ; j++)
		{	if(((Expressie)v.elementAt(j)).geefWaarde().doubleValue() != 0)
			{	Vector expVector = new Vector();
				//Eerst wordt de overgebleven getalsexpressie toegevoegd
				expVector.addElement(v.elementAt(j));
				for(int i=0 ; i<varNamen.length ; i++)
				{	if(exponenten[i][j]==1)
					{	expVector.insertElementAt(new BasisExpressie(varNamen[i]),0);
					}
					else if(exponenten[i][j]>1)
					{	for(int k=0;k<exponenten[i][j];k++)
						{	expVector.insertElementAt(new BasisExpressie(varNamen[i]),0);
						}
					}
					else if(exponenten[i][j]==0)
					{	expVector.insertElementAt(new BasisExpressie("1"),0);
					}
					else if(exponenten[i][j]==-1)
					{	expVector.insertElementAt(new Deling(new BasisExpressie("1"),new BasisExpressie(varNamen[i])),0);
					}
					else if(exponenten[i][j]<-1)
					{	for(int k=0;k<-exponenten[i][j];k++)
						{	expVector.insertElementAt(new Deling(new BasisExpressie("1"),new BasisExpressie(varNamen[i])),0);
						}
					}
				}
				for(int i=0 ; i<extraTermen.size() ; i++)
				{	if(exponentenExtra[i][j]==1)
					{	expVector.insertElementAt((Expressie)extraTermen.elementAt(i),0);
					}
					else if(exponentenExtra[i][j]>1)
					{	for(int k=0;k<exponentenExtra[i][j];k++)
						{	expVector.insertElementAt((Expressie)extraTermen.elementAt(i),0);
						}
					}
					else if(exponentenExtra[i][j]==0)
					{	expVector.insertElementAt(new BasisExpressie("1"),0);
					}
					else if(exponentenExtra[i][j]==-1)
					{	expVector.insertElementAt(new Deling(new BasisExpressie("1"),(Expressie)extraTermen.elementAt(i)),0);
					}
					else if(exponentenExtra[i][j]<-1)
					{	for(int k=0;k<-exponentenExtra[i][j];k++)
						{	expVector.insertElementAt(new Deling(new BasisExpressie("1"),(Expressie)extraTermen.elementAt(i)),0);
						}
					}
				}
				
				if(eval((Expressie)v.elementAt(j))==null)
				{	heeftGGD = false;
				}		
				else
				{	int teller = (int)eval((Expressie)v.elementAt(j)).x;
					int noemer = (int)eval((Expressie)v.elementAt(j)).y;
				
					if(ggdTeller==0)ggdTeller = teller;
					ggdTeller = (int)ggd(ggdTeller,teller);
					if(ggdNoemer==0)ggdNoemer = noemer;
					ggdNoemer = (int)ggd(kgvNoemer,noemer);
					kgvNoemer = kgvNoemer*noemer/ggdNoemer;
				}
				
				Expressie exp = maakFactorenExpressie(expVector);
				t.addElement(exp);
			}
		}
		Expressie e2 = maakTermenExpressie(t);
		
		
		//De gemeenschappelijke factor die buiten haakjes gehaald is wordt 
		//opnieuw opgebouwd mbv de gegevens van factorExponenten[]
		//en factorExponentenExtra[] en tijdelijk opgeslagen in expVector.
		//en vervolgens gcombineerd met e2 tot de gevraagde ontbinding
		
		Vector expVector = new Vector();
		Expressie exp = null;
		for(int i=0 ; i<varNamen.length ; i++)
		{	if(factorExponenten[i]==1)
			{	expVector.insertElementAt(new BasisExpressie(varNamen[i]),0);
			}
			else if(factorExponenten[i]>1)
			{	for(int k=0 ; k<factorExponenten[i] ; k++)
				{	expVector.insertElementAt(new BasisExpressie(varNamen[i]),0);
				}
			}
			else if(factorExponenten[i]==0)
			{	expVector.insertElementAt(new BasisExpressie("1"),0);
			}
			else if(factorExponenten[i]==-1)
			{	expVector.insertElementAt(new Deling(new BasisExpressie("1"),new BasisExpressie(varNamen[i])),0);
			}
			else if(factorExponenten[i]<-1)
			{	for(int k=0 ; k<-factorExponenten[i] ; k++)
				{	expVector.insertElementAt(new Deling(new BasisExpressie("1"),new BasisExpressie(varNamen[i])),0);
				}
			}
		}
		for(int i=0 ; i<extraTermen.size() ; i++)
		{	if(factorExponentenExtra[i]==1)
			{	expVector.insertElementAt((Expressie)extraTermen.elementAt(i),0);
			}
			else if(factorExponentenExtra[i]>1)
			{	for(int k=0 ; k<factorExponentenExtra[i] ; k++)
				{	expVector.insertElementAt((Expressie)extraTermen.elementAt(i),0);
				}
			}
			else if(factorExponentenExtra[i]==0)
			{	expVector.insertElementAt(new BasisExpressie("1"),0);
			}
			else if(factorExponentenExtra[i]==-1)
			{	expVector.insertElementAt(new Deling(new BasisExpressie("1"),(Expressie)extraTermen.elementAt(i)),0);
			}
			else if(factorExponentenExtra[i]<-1)
			{	for(int k=0 ; k<-factorExponentenExtra[i] ; k++)
				{	expVector.insertElementAt(new Deling(new BasisExpressie("1"),(Expressie)extraTermen.elementAt(i)),0);
				}
			}
		}
		
		if(heeftGGD)
		{	expVector.insertElementAt(new Deling(new BasisExpressie("" + ggdTeller),new BasisExpressie("" + kgvNoemer)),0);
			e2 = vermenigvuldig(e2,new Deling(new BasisExpressie("" + kgvNoemer),new BasisExpressie("" + ggdTeller)));
		}	
		e2 = ontbindExtra(e2);
		Vector e2Termen = geefTermen(e2,new Vector());
		if(e2Termen.size()>1)
		{	Vector waardeVector = new Vector();
			for(int i=0 ; i<expVector.size() ; i++)
			{	waardeVector.insertElementAt(expVector.elementAt(i),0);
			}
			double waarde = maakFactorenExpressie(waardeVector).geefWaarde().doubleValue();
			Expressie eerste = (Expressie)e2Termen.elementAt(0);
			if(eerste instanceof Aftrekking && eerste.kind1.geefWaarde().doubleValue()==0 && !(waarde==1))
			{	e2 = vermenigvuldig(e2,new BasisExpressie("-1"));
				expVector.insertElementAt(new BasisExpressie("-1"),0);
			}
		
		}
		Vector e2Vector = geefFactoren(e2,new Vector());
		for(int i=0 ; i<e2Vector.size() ; i++)
		{	expVector.insertElementAt(e2Vector.elementAt(i),0);
		}
		return maakFactorenExpressie(expVector);
	}
	
*/	
	/*Sorteert de expressies in de meegegeven vector en voegt samen waar 
	 *mogelijk. Het resultaat in een nieuwe vector met expressies.
	 */
/*	
	public static Vector sorteerTermen(Vector w)
	{	String[] varNamen = geefVarNamen(maakTermenExpressie(w));
		Vector t = new Vector();
		Vector v = new Vector();
		Vector extraTermen = new Vector();
		for(int j=0 ; j<w.size() ; j++)
		{	v.addElement(w.elementAt(j));
		}
		int[][] exponenten = new int[varNamen.length][v.size()];
		int[][] exponentenExtra = new int[100][v.size()]; // die 100 is arbitrair
		for(int i=0 ; i<varNamen.length ; i++)
		{	for(int j=0 ; j<v.size() ; j++)
			{	exponenten[i][j]=0;
			}
		}
		for(int i=0 ; i<100 ; i++)
		{	for(int j=0 ; j<v.size() ; j++)
			{	exponentenExtra[i][j]=0;
			}
		}
		for(int j=0 ; j<v.size() ; j++)
		{	Vector u;
			u = geefFactoren((Expressie)v.elementAt(j),new Vector());
			for(int k=0 ; k<u.size() ; k++)
			{	Expressie exp = (Expressie)u.elementAt(k);
				for(int i=0 ; i<varNamen.length ; i++)
				{	if(exp instanceof BasisExpressie && ((BasisExpressie)exp).basisString!=null  && ((BasisExpressie)exp).basisString.equals(varNamen[i]))
					{	exponenten[i][j]++;
						u.setElementAt(new BasisExpressie("1"),k);
					}
					else if(exp instanceof Deling && exp.kind1.geefWaarde().doubleValue()==1  && exp.kind2 instanceof BasisExpressie && ((BasisExpressie)exp.kind2).basisString!=null  && ((BasisExpressie)exp.kind2).basisString.equals(varNamen[i]))
					{	exponenten[i][j]--;
						u.setElementAt(new BasisExpressie("1"),k);
					}
				}
				
				if(eval(exp)==null && !(exp instanceof BasisExpressie) && !(exp instanceof Deling && exp.kind1.geefWaarde().doubleValue()==1  && exp.kind2 instanceof BasisExpressie)) 
				{	boolean kwamAlVoor = false;
					for(int i=0 ; i<extraTermen.size() ; i++)
					{	if(isGelijkwaardig(exp,(Expressie)extraTermen.elementAt(i)))
						{	exponentenExtra[i][j]++;
							u.setElementAt(new BasisExpressie("1"),k);
							kwamAlVoor = true;
						}
						else if(exp instanceof Deling && exp.kind1.geefWaarde().doubleValue()==1 && isGelijkwaardig(exp.kind2,(Expressie)extraTermen.elementAt(i)))
						{	exponentenExtra[i][j]--;
							u.setElementAt(new BasisExpressie("1"),k);
							kwamAlVoor = true;
						}
					}
					if(!kwamAlVoor)
					{	if(exp instanceof Deling && exp.kind1.geefWaarde().doubleValue()==1)
						{	exp.kind2 = herleid(exp.kind2);
							extraTermen.addElement(exp.kind2);
							exponentenExtra[extraTermen.size()-1][j]--;
							u.setElementAt(new BasisExpressie("1"),k);
						}
						else
						{	exp = herleid(exp);
							extraTermen.addElement(exp);
							exponentenExtra[extraTermen.size()-1][j]++;
							u.setElementAt(new BasisExpressie("1"),k);
						}
					}
				
				}	
				
			}
			v.setElementAt(maakFactorenExpressie(u),j);
		}
		
		for(int i=varNamen.length-1 ; i>-1 ; i--)
		{	for(int j=0 ; j<v.size()-1 ; j++)
			{	int max= 0;
				int plaats = j;
				for(int k=j ; k<v.size() ; k++)
				{	if(max < exponenten[i][k])
					{	max = exponenten[i][k];
						plaats = k;
					}
				}
				if(plaats>j)
				{	for(int m=0 ; m<varNamen.length ; m++)
					{	int res = exponenten[m][plaats];
						for(int k=plaats ; k>j ; k--)
						{	exponenten[m][k] = exponenten[m][k-1];
							
						}
						exponenten[m][j] = res;
					}
					for(int m=0 ; m<extraTermen.size() ; m++)
					{	int res = exponentenExtra[m][plaats];
						for(int k=plaats ; k>j ; k--)
						{	exponentenExtra[m][k] = exponentenExtra[m][k-1];
							
						}
						exponentenExtra[m][j] = res;
					}
					Object res = v.elementAt(plaats);
					v.removeElementAt(plaats);
					v.insertElementAt(res,j);
					
					res = w.elementAt(plaats);
					w.removeElementAt(plaats);
					w.insertElementAt(res,j);
				}
				
			}
		}
		
		for(int j=0 ; j<v.size()-1 ; j++)
		{	if(eval((Expressie)v.elementAt(j))!=null && ((Expressie)v.elementAt(j)).geefWaarde().doubleValue() != 0)
			{	for(int k=j+1 ; k<v.size() ; k++)
				{	if(eval((Expressie)v.elementAt(k))!=null && ((Expressie)v.elementAt(k)).geefWaarde().doubleValue() != 0)
					{	boolean komtOvereen1 = true;
						for(int i=0 ; i<varNamen.length ; i++)
						{	if(exponenten[i][k] != exponenten[i][j])
							{	komtOvereen1 = false;
								break;
							}
						}
						boolean komtOvereen2 = true;
						for(int i=0 ; i<extraTermen.size() ; i++)
						{	if(exponentenExtra[i][k] != exponentenExtra[i][j])
							{	komtOvereen2 = false;
								break;
							}
						}
					
						if(komtOvereen1 && komtOvereen2)
						{	Expressie exp1 = (Expressie)v.elementAt(j);
							Expressie exp2 = (Expressie)v.elementAt(k);
							v.setElementAt(evalueerGetalsExpressie(new Optelling(exp1,exp2)),j);
							v.setElementAt(new BasisExpressie(0),k);
						}
					}
				}
			}
		}
		
		for(int j=0 ; j<v.size() ; j++)
		{	if(((Expressie)v.elementAt(j)).geefWaarde() != 0)
			{	Vector expVector = new Vector();
				expVector.addElement(v.elementAt(j));
				Expressie exp = null;
				for(int i=0 ; i<varNamen.length ; i++)
				{	if(exponenten[i][j]==1)
					{	expVector.insertElementAt(new BasisExpressie(varNamen[i]),0);
					}
					else if(exponenten[i][j]>1)
					{	for(int k=0;k<exponenten[i][j];k++)
						{	expVector.insertElementAt(new BasisExpressie(varNamen[i]),0);
						}
					}
					else if(exponenten[i][j]==0)
					{	expVector.insertElementAt(new BasisExpressie(1),0);
					}
					else if(exponenten[i][j]==-1)
					{	expVector.insertElementAt(new Deling(new BasisExpressie(1),new BasisExpressie(varNamen[i])),0);
					}
					else if(exponenten[i][j]<-1)
					{	for(int k=0;k<-exponenten[i][j];k++)
						{	expVector.insertElementAt(new Deling(new BasisExpressie(1),new BasisExpressie(varNamen[i])),0);
						}
					}
				}
				for(int i=0 ; i<extraTermen.size() ; i++)
				{	if(exponentenExtra[i][j]==1)
					{	expVector.insertElementAt((Expressie)extraTermen.elementAt(i),0);
					}
					else if(exponentenExtra[i][j]>1)
					{	for(int k=0;k<exponentenExtra[i][j];k++)
						{	expVector.insertElementAt((Expressie)extraTermen.elementAt(i),0);
						}
					}
					else if(exponentenExtra[i][j]==0)
					{	expVector.insertElementAt(new BasisExpressie(1),0);
					}
					else if(exponentenExtra[i][j]==-1)
					{	expVector.insertElementAt(new Deling(new BasisExpressie(1),(Expressie)extraTermen.elementAt(i)),0);
					}
					else if(exponentenExtra[i][j]<-1)
					{	for(int k=0;k<-exponentenExtra[i][j];k++)
						{	expVector.insertElementAt(new Deling(new BasisExpressie(1),(Expressie)extraTermen.elementAt(i)),0);
						}
					}
				}
				exp = maakFactorenExpressie(expVector);
				t.addElement(exp);
			}
		}
		return t;
	}
*/	
	/*Benadert een wortelexpressie met een BasisExpressie van een double.
	 */	
	public static Expressie benaderWortels(Expressie e)
	{	if(e instanceof Wortel && !Double.isNaN(e.geefWaarde().doubleValue()))
		{	double w = e.geefWaarde().doubleValue();
			e = new BasisExpressie("" + w);
		}
		else if(!(e instanceof BasisExpressie))
		{	e.kind1 = benaderWortels(e.kind1);
			if(e.kind2 != null)e.kind2 = benaderWortels(e.kind2);
		}
		return e;
	}
	/*Vereenvoudigt de breuk x/y
	 */
	public static PointLong vereenvoudigBreuk(PointLong p)
    {	if(p.x==0)return p;
        long ggd = ggd(p.x, p.y);
        p.x = p.x / ggd;
        p.y = p.y / ggd;
        return new PointLong(p.x, p.y);
    }
	/*Bepaalt de ggd van de twee gegeven getallen
	 */
	public static long ggd(long m, long n)
    {   long hlp;
        if(m<0)m  =-m;
		if(n<0)n  =-n;
		if(m < n)
        {
            hlp = n;
            n = m;
            m = hlp;
        }
        if(n==0)return m;
        hlp = m % n;
        if(hlp == 0) return n;
        else return ggd(n, hlp);
    }
	/*Evalueert een expressie tot een breuk indien dat mogelijk is.
	 *Anders is de terugkeerwaarde null.
	 */
	
	public static boolean withinLongRange(long num)
	{
		if(num<-100000000000000000L || num>100000000000000000L)return false;
		return true;
	}
	
	public static PointLong eval(Expressie e)
	{	//if(e instanceof E) return null;
		//if(e instanceof PI) return null;
		//if(e instanceof Bin)return eval(new BasisExpressie(e.geefWaarde()));
		//if(e instanceof Faculteit)return eval(new BasisExpressie(e.geefWaarde()));
		//if(e instanceof GCD)return eval(new BasisExpressie(e.geefWaarde()));
		//if(e instanceof Min)return eval(new BasisExpressie(e.geefWaarde()));
		//if(e instanceof Max)return eval(new BasisExpressie(e.geefWaarde()));
		if(e instanceof BasisExpressie && !Double.isNaN(e.geefWaarde().doubleValue()))
		{	
			if(Math.rint(e.geefWaarde().doubleValue())-e.geefWaarde().doubleValue()!=0)
			{	/**/
				long x=0;
				long y=0;
				boolean isBreuk = false;
				for(int i=0 ; i<7 ; i++)
				{	double w = e.geefWaarde().doubleValue()*Math.pow(10,i);
					if(Math.rint(w)-w==0)
					{	isBreuk = true;
						x = (long)w;
						y = (long)Math.pow(10,i);
						break;
					}
					//else if(i==6)
					//{	isBreuk = true;
					//	x = (long)Math.rint(w);
					//	y = (long)Math.pow(10,i);
					//}
				}
				if(isBreuk)
				{	PointLong p = new PointLong(x,y);
					//if(x==9223372036854775807L  || y==9223372036854775807L) return null;
					if(!withinLongRange(x)  || !withinLongRange(y)) return null;
					return vereenvoudigBreuk(p);
				}
				
				return null;
			}
			else if(!withinLongRange((long)e.geefWaarde().doubleValue())) return null;
			else return new PointLong((long)e.geefWaarde().longValue(),1);
		}
		else
		if(e instanceof Optelling)
		{	PointLong p1 = eval(e.kind1);
			PointLong p2 = eval(e.kind2);
			if(p1==null || p2==null)return null;
			long x1 = p1.x;
			long y1 = p1.y;
			long x2 = p2.x;
			long y2 = p2.y;
			long x = x1*y2+x2*y1;
			long y = y1*y2;
			if(y<0)
			{	y=-y;
				x=-x;
			}
			PointLong p = new PointLong(x,y);
			//if(x==9223372036854775807L  || y==9223372036854775807L) return null;
			if(!withinLongRange(x)  || !withinLongRange(y)) return null;
			return vereenvoudigBreuk(p);
		}
		else if(e instanceof Aftrekking)
		{	PointLong p1 = eval(e.kind1);
			PointLong p2 = eval(e.kind2);
			if(p1==null || p2==null)return null;
			long x1 = p1.x;
			long y1 = p1.y;
			long x2 = p2.x;
			long y2 = p2.y;
			long x = x1*y2-x2*y1;
			long y = y1*y2;
			if(y<0)
			{	y=-y;
				x=-x;
			}
			PointLong p = new PointLong(x,y);
			//if(x==9223372036854775807L  || y==9223372036854775807L) return null;
			if(!withinLongRange(x)  || !withinLongRange(y)) return null;
			return vereenvoudigBreuk(p);
		}
		else if(e instanceof Vermenigvuldiging)
		{	PointLong p1 = eval(e.kind1);
			PointLong p2 = eval(e.kind2);
			if(p1==null || p2==null)return null;
			long x1 = p1.x;
			long y1 = p1.y;
			long x2 = p2.x;
			long y2 = p2.y;
			long x = x1*x2;
			long y = y1*y2;
			if(y<0)
			{	y=-y;
				x=-x;
			}
			PointLong p = new PointLong(x,y);
			//if(x==9223372036854775807L  || y==9223372036854775807L) return null;
			if(!withinLongRange(x)  || !withinLongRange(y)) return null;
			return vereenvoudigBreuk(p);
		}
		else if(e instanceof Deling)
		{	PointLong p1 = eval(e.kind1);
			PointLong p2 = eval(e.kind2);
			if(p1==null || p2==null)return null;
			long x1 = p1.x;
			long y1 = p1.y;
			long x2 = p2.x;
			long y2 = p2.y;
			long x = x1*y2;
			long y = y1*x2;
			if(y<0)
			{	y=-y;
				x=-x;
			}
			PointLong p = new PointLong(x,y);
			//if(x==9223372036854775807L  || y==9223372036854775807L) return null;
			if(!withinLongRange(x)  || !withinLongRange(y)) return null;
			return vereenvoudigBreuk(p);
		}
		else if(e instanceof Macht)
		{	PointLong p1 = eval(e.kind1);
			PointLong p2 = eval(e.kind2);
			if(p1==null || p2==null)return null;
			long x1 = p1.x;
			long y1 = p1.y;
			long x2 = p2.x;
			long y2 = p2.y;
			if(y2!=1)return null;
			long x = (long)Math.pow(x1,x2);
			long y = (long)Math.pow(y1,x2);
			if(x2<0)
			{	x = (long)Math.pow(y1,-x2);//1; lelijke bug!!!!
				y = (long)Math.pow(x1,-x2);
			}
			if(y<0)
			{	y=-y;
				x=-x;
			}
			PointLong p = new PointLong(x,y);
			//if(x==9223372036854775807L  || y==9223372036854775807L) return null;
			if(!withinLongRange(x)  || !withinLongRange(y)) return null;
			return vereenvoudigBreuk(p);
		}
		else if(e instanceof Wortel)
		{	PointLong p1 = eval(e.kind1);
			if(p1==null)return null;
			p1 = vereenvoudigBreuk(p1);
			long x1 = p1.x;
			long y1 = p1.y;
			boolean tellerIsKwadraat = false;
			boolean noemerIsKwadraat = false;
			/*for(int i=1 ; i<x1+1 ; i++)
			{	if(i*i==x1)
				{	tellerIsKwadraat = true;
					x1 = i;
					break;
				}
			}
			for(int i=1 ; i<y1+1 ; i++)
			{	if(i*i==y1)
				{	noemerIsKwadraat = true;
					y1 = i;
					break;
				}
			}*/
			double wx1 = Math.rint(Math.sqrt((double)x1));
			double wy1 = Math.rint(Math.sqrt((double)y1));
			if(isGelijkDouble(wx1*wx1,(double)x1)) tellerIsKwadraat = true;
			if(isGelijkDouble(wy1*wy1,(double)y1)) noemerIsKwadraat = true;
			
			if(tellerIsKwadraat && noemerIsKwadraat)
			{	PointLong p = new PointLong((long)wx1,(long)wy1);
				//if((long)wx1==9223372036854775807L  || (long)wy1==9223372036854775807L) return null;
				if(!withinLongRange((long)wx1)  || !withinLongRange((long)wy1)) return null;
				return p;
			}
			else return null;
		}
/*	
		else if(e instanceof NdeWortel)
		{	PointLong p1 = eval(e.kind1);
			PointLong p2 = eval(e.kind2);
			if(p1==null)return null;
			p1 = vereenvoudigBreuk(p1);
			p2 = vereenvoudigBreuk(p2);
			if(p2.y!=1 || p2.x==0)return null;
			long x1 = p1.x;
			long y1 = p1.y;
			long x2 = p2.x;
			boolean tellerIsNdeMacht = false;
			boolean noemerIsNdeMacht = false;
			
			double wx1 = Math.rint(Math.pow((double)x1,1.0/x2));
			double wy1 = Math.rint(Math.pow((double)y1,1.0/x2));
			if(isGelijkDouble(Math.pow((double)wx1,x2),(double)x1)) tellerIsNdeMacht = true;
			if(isGelijkDouble(Math.pow((double)wy1,x2),(double)y1)) noemerIsNdeMacht = true;
			if(tellerIsNdeMacht && noemerIsNdeMacht)
			{	PointLong p = new PointLong((long)wx1,(long)wy1);
				//if((long)wx1==9223372036854775807L  || (long)wy1==9223372036854775807L) return null;
				if(!withinLongRange((long)wx1)  || !withinLongRange((long)wy1)) return null;
				return p;
			}
			else return null;
		}
*/
/*		
		else if(e instanceof NdeLog)
		{	PointLong p1 = eval(e.kind1);
			PointLong p2 = eval(e.kind2);
			if(p1==null)return null;
			p1 = vereenvoudigBreuk(p1);
			p2 = vereenvoudigBreuk(p2);
			if(p2.x==0 || p2.x==1 && p2.y==1)return null;
			long x1 = p1.x;
			long y1 = p1.y;
			long x2 = p2.x;
			long y2 = p2.y;
			boolean tellerIsNdeLog = false;
			boolean noemerIsNdeLog = false;
			
			double wx1 = Math.rint(Math.log((double)x1)/Math.log((double)x2/(double)y2));
			double wy1 = Math.rint(Math.log((double)y1)/Math.log((double)x2/(double)y2));
			if(isGelijkDouble(Math.pow((double)x2/(double)y2,(double)wx1),(double)x1)) tellerIsNdeLog = true;
			if(isGelijkDouble(Math.pow((double)x2/(double)y2,(double)wy1),(double)y1)) noemerIsNdeLog = true;
			
			//System.out.println(""+wx1);
			//System.out.println(""+wy1);
			
			if(tellerIsNdeLog && noemerIsNdeLog)
			{	PointLong p = new PointLong((long)wx1-(long)wy1,1);
				//if((long)wx1==9223372036854775807L  || (long)wy1==9223372036854775807L) return null;
				if(!withinLongRange((long)wx1)  || !withinLongRange((long)wy1)) return null;
				return p;
			}
			else return null;
		}
*/		
		/*
		
		double waarde = e.geefWaarde();
		for(int i=1 ; i<1000 ; i++)
		{	double mogelijkeTeller = i*waarde;
			if(Math.rint(mogelijkeTeller)-mogelijkeTeller==0)
			{	long x1 = (long)mogelijkeTeller;
				long y1 = i;
				PointLong p = new PointLong(x1,y1);
				return p;
			}
		}*/
		return null;
	}
	
/*	
	public static Expressie evalueerGetalsExpressie(Expressie exp)
	{
		return evalueerGetalsExpressie(exp, true);
		
	}
*/	
	/*getalsexpressie wordt eerst geevalueerd tot een breuk en vervolgens wordt
	 *een expressie gebouwd die die breuk correct weergeeft.
	 *Indien een getalsexpressie niet door een breuk kan worden weergegeven,
	 *dan wordt de beginexpressie zelf teruggegeven
	 */
/*	
	public static Expressie evalueerGetalsExpressie(Expressie exp, boolean breukenGemengd)
	{	if(exp instanceof DecRound)
		{	
    	    //if(exp.kind1.geefWaarde()<0.000000001)
            //{   return exp = new Aftrekking(new BasisExpressie(0),new BasisExpressie(-exp.geefWaarde()));
            //}
            //else 
                return new BasisExpressie(exp.geefWaarde());
		}
		if(exp instanceof SigRound)
		{   int macht = (int)exp.kind2.geefWaarde();
			int signf = (int)exp.kind3.geefWaarde(); 
			BasisExpressie b = new BasisExpressie(exp.geefWaarde());
			b.setScientificNotation(true, macht, signf);
			return b;
		}
		if(exp instanceof DecRoundStrict)
		{	
			if(exp.kind2.geefWaarde()>0.000000001)
			{	String formatString = "0.";
				for(int i=0 ; i<exp.kind2.geefWaarde() ; i++)
				{
					formatString = formatString + "0";
				}
				DecimalFormat df = new DecimalFormat(formatString, Expressie.dfs);
				if(exp.kind1.geefWaarde()<0.000000001)
		        {   return exp = new Aftrekking(new BasisExpressie(0),new BasisExpressie(df.format(-exp.geefWaarde())));
		        }
				else return new BasisExpressie(df.format(exp.geefWaarde()));
			}
			else 
			return new BasisExpressie(exp.geefWaarde());
		}
		
		
		PointLong p = eval(exp);
				
		if(p==null)
		{	if(exp.isWaarde() && !withinLongRange((long)exp.geefWaarde()))return new BasisExpressie(exp.geefWaarde());
			return exp;
		}
		//if(isWortelBenadering(exp))return new BasisExpressie(exp.geefWaarde());
		
		
		
		long teller = p.x;
		long noemer = p.y;
		
		if(teller>0 && ((long)Math.abs(teller)<(long)Math.abs(noemer) || (!breukenGemengd && (long)Math.abs(teller)%(long)Math.abs(noemer)!=0)))
		{	exp = new Deling(new BasisExpressie(teller),new BasisExpressie(noemer));
		}
		else if(teller>0 && (long)Math.abs(teller)>(long)Math.abs(noemer))
		{	long helen = teller/noemer;
			long delen = teller%noemer;
			exp = new Optelling(new BasisExpressie(helen),new Deling(new BasisExpressie(delen),new BasisExpressie(noemer)));
			if(delen==0)exp = new BasisExpressie(helen);
		}
		else if(teller<0 && ((long)Math.abs(teller)<(long)Math.abs(noemer)  || (!breukenGemengd && (long)Math.abs(teller)%(long)Math.abs(noemer)!=0)))
		{	exp = new Aftrekking(new BasisExpressie(0),new Deling(new BasisExpressie(-teller),new BasisExpressie(noemer)));
		}
		else if(teller<0 && (long)Math.abs(teller)>(long)Math.abs(noemer))
		{	long helen = (long)Math.abs(teller)/(long)Math.abs(noemer);
			long delen = (long)Math.abs(teller)%(long)Math.abs(noemer);
			exp = new Aftrekking(new BasisExpressie(0),new Optelling(new BasisExpressie(helen),new Deling(new BasisExpressie(delen),new BasisExpressie(noemer))));
			if(delen==0)exp = new Aftrekking(new BasisExpressie(0),new BasisExpressie(helen));
		}
		else if(teller==0)
		{	exp = new BasisExpressie(0);
		}
		else if(teller==noemer)
		{	exp = new BasisExpressie(1);
		}
		else if(teller==-noemer)
		{	exp = new Aftrekking(new BasisExpressie(0),new BasisExpressie(1));
		}
		return exp;
	}
*/	
}

