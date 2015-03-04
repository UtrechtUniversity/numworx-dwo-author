package fi.wiskopdr.scheikundeobjects;

import java.util.Vector;

public class SamengesteldIonDeel extends Molecuul
{

	int coefficient;
	
	public SamengesteldIonDeel(AtoomDeel[] delen, int coefficient)
	{
		super(delen, 0);
		this.coefficient = coefficient;
	}
	
	public SamengesteldIonDeel(SamengesteldIonDeel[] delen, int coefficient)
	{
		super(delen, 0);
		this.coefficient = coefficient;
	}
	
	public Vector<String> bepaalAtoomNamen()
	{
		Vector<String> v = new Vector<String>();
		if(this.samengesteldeIonDelen == null)
		{
			for(int i = 0; i < atoomDelen.length; i++)
			{	String s = atoomDelen[i].atoomNaam;
				if(!v.contains(s) && !s.equals("e"))
					v.add(s);
			}
		}
		else
		{
			for(int i = 0; i < samengesteldeIonDelen.length; i++)
			{
				Vector<String> v2 = samengesteldeIonDelen[i].bepaalAtoomNamen();
				for(int j = 0; j < v2.size(); j++)
				{
					String s = v2.elementAt(j);
					if(!v.contains(s) && !s.equals("e"))
						v.add(s);
				}
			}
		}
		return v;
	}
	
	public String toString()
	{
		String s = "";
		if(coefficient > 1)
			s = "(";
		for(int i = 0; i < atoomDelen.length; i++)
		{
			s = s + atoomDelen[i].toString();
		}
		if(coefficient > 1)
			s = s + ")$s" + coefficient + "@";
		return s;
	}
}
