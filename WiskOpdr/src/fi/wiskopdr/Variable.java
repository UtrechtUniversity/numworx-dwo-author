package fi.wiskopdr;

import java.util.*;

import fi.beans.stringutils.StringUtils;
import fi.wiskopdr.expressies.*;
import fi.wiskopdr.formuleobjects.*;

public class Variable
{
	String name;
	Vector borders;
	Hashtable borderValues;
	Vector<Integer> values;
	boolean draw;
	
	public Variable(String name)
	{	this.name = name;
		borders = new Vector();
		borderValues = new Hashtable();
	}	
	
	public void setDraw(boolean d) {
	  this.draw = d;
	}
	
	public boolean isDraw() {
	  return draw;
	}
	
	public void setValues(String s)
	{	s = s.trim();
		StringTokenizer tokenizer = new StringTokenizer(s,",");
		while(tokenizer.hasMoreTokens())
		{	String tok = tokenizer.nextToken();
			tok = tok.trim();
			int index = tok.indexOf("..");
			if(index>0)
			{	String leftExprString = tok.substring(0,index);
				
				leftExprString = leftExprString.trim();
				//String[] nameParts = StringUtils.split(leftExprString, "_");
				//if(nameParts.length==2) leftExprString = nameParts[0] + "?(" + nameParts[1] + ")";
				
				Expressie leftExpr = FormuleParser.parse(FormuleParser.schoon(FormuleParser.formuleString("$f" + leftExprString + "@")));
				borders.addElement(leftExpr);
				
				String[] namenLeft = Algebra.geefVarNamen(leftExpr);
				for(int i=0 ; i<namenLeft.length; i++)
			    {	borderValues.put(namenLeft[i],"leeg");
			    }
			    
				String rightExprString = tok.substring(index+2);
				rightExprString = rightExprString.trim();
				//nameParts = StringUtils.split(rightExprString, "_");
				//if(nameParts.length==2) rightExprString = nameParts[0] + "?(" + nameParts[1] + ")";
				
				Expressie rightExpr = FormuleParser.parse(FormuleParser.schoon(FormuleParser.formuleString("$f" + rightExprString + "@")));
				borders.addElement(rightExpr);
				
				
				String[] namenRight = Algebra.geefVarNamen(rightExpr);
		    	for(int i=0 ; i<namenRight.length; i++)
			    {	borderValues.put(namenRight[i],"leeg");
			    }
			}
			else if(index==-1)
			{	
				String exprString = tok;
				//String[] nameParts = StringUtils.split(exprString, "_");
				//if(nameParts.length==2) exprString = nameParts[0] + "?(" + nameParts[1] + ")";
				
				Expressie expr = FormuleParser.parse(FormuleParser.schoon(FormuleParser.formuleString("$f" + exprString + "@")));
				borders.addElement(expr);
				borders.addElement(expr);
				
				String[] namen = Algebra.geefVarNamen(expr);
		    	for(int i=0 ; i<namen.length; i++)
			    {	borderValues.put(namen[i],"leeg");
			    }
			}	
		}
	}
	
	public boolean isUsedVar(String s)
	{	return borderValues.containsKey(s);
	}
	
	public void substitueer(int value, String varnaam)
	{	if(borderValues.containsKey(varnaam))borderValues.put(varnaam,new Integer(value));
	}
	
	public Vector substitueerBorders()
	{	Vector v = new Vector();
		for(int i=0 ; i<borders.size(); i++)
		{	v.addElement(((Expressie)borders.elementAt(i)).substitueer(0,"geen"));
		}
		for (Enumeration e = borderValues.keys() ; e.hasMoreElements() ;) 
		{	String key = (String)e.nextElement();
			int value = ((Integer)borderValues.get(key)).intValue();
			for(int i=0 ; i<v.size(); i++)
		    {	Expressie b = (Expressie)v.elementAt(i);
		    	b = b.substitueer(value,key);
		    	v.setElementAt(b,i);
		    	//System.out.println(key+"="+b.geefWaarde());
		    }
     	}
     	return v;

	}
	
	public void makeValues()
	{	values = new Vector<>();
		Vector expSub = substitueerBorders();
		for(int i=0 ; i<expSub.size(); i+=2)
		{	int leftBorder = (int)((Expressie)expSub.elementAt(i)).geefWaarde();
			int rightBorder = (int)((Expressie)expSub.elementAt(i+1)).geefWaarde();
			//if("MW".equals(WiskOpdr.deployVariant))
			//{	leftBorder = (int)Math.rint(((Expressie)expSub.elementAt(i)).geefWaarde());
			//	rightBorder = (int)Math.rint(((Expressie)expSub.elementAt(i+1)).geefWaarde());
			//}
			//System.out.println("r: "+rightBorder);
			for(int j=leftBorder ; j<=rightBorder; j++)
		    {	values.addElement(new Integer(j));
		    	//System.out.println("getallen: "+j);
		    }
		}
	}
	
	public boolean checkBorders()
	{	boolean bordersOK = true;
		for(int i=0 ; i<borders.size(); i+=2)
		{	double bl = ((Expressie)borders.elementAt(i)).geefWaarde();
			double br = ((Expressie)borders.elementAt(i+1)).geefWaarde();
			if(bl > br
				|| !Double.isNaN(bl) && bl < Integer.MIN_VALUE
				|| !Double.isNaN(bl) && bl > Integer.MAX_VALUE
				|| !Double.isNaN(br) && br < Integer.MIN_VALUE
				|| !Double.isNaN(br) && br > Integer.MAX_VALUE)
			{	bordersOK = false;
				break;
			}	
		}
		return bordersOK;
	}
	
	private Set<Integer> drawSet = new TreeSet<>();
	
    public void draw (int n) {
      if (isDraw())
        drawSet.add(n);
    }
	
	public int[] getValues()
	{	makeValues();
		List<Integer> all = new ArrayList<>(values);
		if (isDraw()) { 
		    all.removeAll(drawSet);
    		if (all.isEmpty()) {
    		  drawSet.clear();
    		  all.addAll(values);
    		}
		}
		int[] intValues = new int[all.size()];
		for(int i=0 ; i<intValues.length; i++)
	    {	Integer item = all.get(i);
            intValues[i] = item.intValue();
	    	//System.out.println("values: "+intValues[i]);
	    }
	    return intValues;
	}
	
	public String getName()
	{	return name;
	}
}
