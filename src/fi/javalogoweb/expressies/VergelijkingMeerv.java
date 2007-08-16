package fi.javalogoweb.expressies;

import java.util.Vector;

//import fi.javalogoweb.WiskOpdr;


public class VergelijkingMeerv 
{	
	Vergelijking[] vergelijkingen;
	//OngelijkheidObject[] ongelijkheidObjecten;
	
	public VergelijkingMeerv(Vergelijking[] v)
	{	vergelijkingen = v;
	}
	
	public VergelijkingMeerv bewerkVergelijking(String operator, Expressie en)
	{	Vergelijking[] vergelijkingenNieuw = new Vergelijking[vergelijkingen.length];
		for(int j=0 ; j<vergelijkingen.length ; j++)
		{	vergelijkingenNieuw[j] = vergelijkingen[j].bewerkVergelijking(operator,en);
		}
		return new VergelijkingMeerv(vergelijkingenNieuw);
	}
	
	public boolean isOngelijkheid()
	{	for(int i=0 ; i<vergelijkingen.length ; i++)
		{ 	if(vergelijkingen[i].isOngelijkheid()) return true;
		}
		return false;
	}
	
	public Vergelijking geefVergelijking(int nr)
	{	return vergelijkingen[nr];
	}
	
	public String[] geefVergTekens()
	{	String[] vergTekens = new String[vergelijkingen.length];
		for(int i=0 ; i<vergelijkingen.length ; i++)
		{	vergTekens[i] = vergelijkingen[i].geefVergTeken();
		}
		return vergTekens;
	}
	
	public boolean isOplossing(double subst)
	{	String[] varNamen = geefVarNamen();
		if(varNamen.length > 1) return false;
		boolean isOplossing = false;
		for(int j=0 ; j<vergelijkingen.length ; j++)
		{	if(!isOplossing)
			isOplossing = vergelijkingen[j].isOplossing(subst);
		}
		return isOplossing;
	}
	
	public boolean isOplossing(double subst, String vergTeken)
	{	String[] varNamen = geefVarNamen();
		if(varNamen.length > 1) return false;
		boolean isOplossing = false;
		for(int j=0 ; j<vergelijkingen.length ; j++)
		{	if(!isOplossing)
			isOplossing = vergelijkingen[j].isOplossing(subst, vergTeken);
		}
		return isOplossing;
	}
	
	public boolean isOplossing(Expressie subst, String var)
	{	String[] varNamen = geefVarNamen();
		boolean isOplossing = false;
		for(int j=0 ; j<vergelijkingen.length ; j++)
		{	if(!isOplossing)
			isOplossing = vergelijkingen[j].isOplossing(subst, var);
		}
		return isOplossing;
	}
	
	public boolean isOplossing(Expressie subst, String var, String vergTeken)
	{	String[] varNamen = geefVarNamen();
		boolean isOplossing = false;
		for(int j=0 ; j<vergelijkingen.length ; j++)
		{	if(!isOplossing)
			isOplossing = vergelijkingen[j].isOplossing(subst, var, vergTeken);
		}
		return isOplossing;
	}
	
	/*public boolean bevatFouteOplossing(VergelijkingMeerv antw)
	{	boolean isOplossing = true;
		for(int j=0 ; j<vergelijkingen.length ; j++)
		{	if(vergelijkingen[j].isEindOplossing())
			{	isOplossing = antw.isOplossing(vergelijkingen[j].geefEindOplossing());
				if(!isOplossing)return true;
			}
		}
		return false;
	}*/
	
	public boolean bevatFouteOplossing(VergelijkingMeerv antw, String var)
	{	for(int j=0 ; j<vergelijkingen.length ; j++)
		{	if(!vergelijkingen[j].bevatOplossing(antw.geefEindOplossing(var), var))
			{	return true;
			}
		}
		return false;
	}
	
	public boolean bevatFouteOplossing(VergelijkingMeerv antw, String var, String[] vergTekens)
	{	for(int j=0 ; j<vergelijkingen.length ; j++)
		{	if(!vergelijkingen[j].bevatOplossing(antw.geefEindOplossing(var), var, vergTekens))
			{	return true;
			}
		}
		return false;
	}
	public boolean isOplossing(double[] subst)
	{	String[] varNamen = geefVarNamen();
		if(varNamen.length > 1) return false;
		for(int i=0 ; i<subst.length ; i++)
		{	if(!isOplossing(subst[i]))return false;
		}
		return true;
	}
	
	public boolean isOplossing(double[] subst, String[] vergTekens)
	{	String[] varNamen = geefVarNamen();
		if(varNamen.length > 1) return false;
		for(int i=0 ; i<subst.length ; i++)
		{	if(!isOplossing(subst[i], vergTekens[i]))return false;
		}
		return true;
	}
	
	public boolean isOplossing(Expressie[] subst, String var)
	{	for(int i=0 ; i<subst.length ; i++)
		{	if(!isOplossing(subst[i],var))return false;
		}
		return true;
	}
	
	public boolean isOplossing(Expressie[] subst, String var, String[] vergTekens)
	{	for(int i=0 ; i<subst.length ; i++)
		{	if(!isOplossing(subst[i],var, vergTekens[i]))return false;
		}
		return true;
	}
	
	public boolean isDeelOplossing(double[] subst)
	{	String[] varNamen = geefVarNamen();
		if(varNamen.length > 1) return false;
		for(int i=0 ; i<subst.length ; i++)
		{	if(isOplossing(subst[i]))return true;
		}
		return false;
	}
	
	public boolean isDeelOplossing(Expressie[] subst, String var)
	{	for(int i=0 ; i<subst.length ; i++)
		{	if(isOplossing(subst[i], var))return true;
		}
		return false;
	}
	
	public boolean isDeelOplossing(Expressie[] subst, String var, String[] vergTekens)
	{	for(int i=0 ; i<subst.length ; i++)
		{	if(isOplossing(subst[i], var, vergTekens[i]))return true;
		}
		return false;
	}
	
	public boolean checkDiscriminant(int discriminant, String varNaam)
	{	for (int i = 0; i < vergelijkingen.length; i++) 
		{	boolean isGeen = vergelijkingen[i].checkDiscriminant(discriminant, varNaam);
			if(isGeen)return true;
		}
		return false;
	}
	
	public Vector geefVarN()
	{	Vector v = new Vector();
		for(int i=0 ; i<vergelijkingen.length ; i++)
		{	Vector vNieuw = vergelijkingen[i].geefVarN();
		
			int lengte = v.size();
			for(int j=0 ; j<vNieuw.size() ; j++)
			{	boolean anders = true;
				for(int k=0 ; k<lengte ; k++)
				{	if(((String)v.elementAt(k)).equals(((String)vNieuw.elementAt(j))))
					{	anders = false;
					}
				}
				if(anders)v.addElement(vNieuw.elementAt(j));
			}
		}
		return v;
	}
	
	public String[] geefVarNamen()
	{	Vector varn = geefVarN();
		String[] varNamen = new String[varn.size()];
		for(int i=0 ; i<varn.size() ; i++)
		{	varNamen[i] = (String)varn.elementAt(i);
		}
		return varNamen;
	}
	
	public String toString()
	{	String s = vergelijkingen[0].toString();
		for(int i=1 ; i<vergelijkingen.length ; i++)
		{	//s = s + "  " + WiskOpdr.rb.getString("ofLabel") + "  " + vergelijkingen[i].toString();
		}	
		
		return s;
	}
	
	/*public boolean isEindOplossing()
	{	for(int i=0 ; i<vergelijkingen.length ; i++)
		{	if(!vergelijkingen[i].isEindOplossing())return false;
		}
		return true;
	}*/
	
	public boolean isEindOplossing(String var)
	{	for(int i=0 ; i<vergelijkingen.length ; i++)
		{	if(!vergelijkingen[i].isEindOplossing(var))return false;
		}
		return true;
	}
	/*
	public double[] geefEindOplossing()
	{	double[] oplossingen = new double[vergelijkingen.length];
		if(isEindOplossing())
		{	for(int i=0 ; i<vergelijkingen.length ; i++)
			{	oplossingen[i] = vergelijkingen[i].geefEindOplossing();
			}
		}
		return oplossingen;
	}*/
	
	public Expressie[] geefEindOplossing(String var)
	{	Expressie[] oplossingen = new Expressie[vergelijkingen.length];
		if(isEindOplossing(var))
		{	for(int i=0 ; i<vergelijkingen.length ; i++)
			{	oplossingen[i] = vergelijkingen[i].geefEindOplossing(var);
			}
		}
		return oplossingen;
	}
	
	public String geefVergelijkingVar()
	{	String var = vergelijkingen[0].geefVergelijkingVar();
		for(int i=1 ; i<vergelijkingen.length ; i++)
		{	String varNieuw = vergelijkingen[i].geefVergelijkingVar();
			if(!var.equals(varNieuw))return null;
		}
		return var;
	}
	
	public VergelijkingMeerv substitueer(Expressie subst, String var)
	{	Vergelijking[] vergelijkingenNieuw = new Vergelijking[vergelijkingen.length];
		for(int i=0 ; i<vergelijkingen.length ; i++)
		{	vergelijkingenNieuw[i] = vergelijkingen[i].substitueer(subst, var);
		}
		return new VergelijkingMeerv(vergelijkingenNieuw);
	}
	
}
