package fi.algebrapijlenopdr.tekstobjects;

import java.util.*;

public class TekstBuffer
{
	private String tekst;
	private Vector formules;
	private TekstVak tekstVak; 
	
	//public TekstBuffer(String string)
	//{	tekst = string+'\n';
	//	formules = new Vector();
	//}
	
	public TekstBuffer(TekstVak tv, String completeString)
	{	
		formules = new Vector();
		tekstVak = tv; 
		
		int toevoegNr = formules.size()-1;
		for(int i=completeString.length()-1 ; i>-1; i--)
		{	if(completeString.charAt(i)=='@')
			{	int indexF = completeString.lastIndexOf("$f");
				int indexA = completeString.lastIndexOf("$A");
				int index = -1;
				TekstDeelVak tfv = null;
				if(indexF > indexA)
				{	tfv = new TekstFormuleVak(tekstVak);
					index = indexF;
				}
				else if(indexA > indexF)
				{	tfv = new TekstAntwoordVak(tekstVak);
					tfv.setEditable(true);
					index = indexA;
				}
				if(index==-1) break;
				String formString = completeString.substring(index,i+1);
				if(indexF > indexA)	tfv.vulVak(formString);
				else tfv.vulVak(formString.substring(2,formString.length()-1));
				completeString = ""+completeString.substring(0,index)+"@"+completeString.substring(i+1);
				formules.insertElementAt(tfv,0);
				i=index;
			}
		}
		if(completeString.length()>0 && completeString.charAt(completeString.length()-1) == '\n')tekst = completeString;
		else tekst = completeString+'\n';
	}
	
	public char charAt(int pos)
	{	return tekst.charAt(pos);
	}
	
	public void insert(int index, char c)
	{	tekst = ""+tekst.substring(0,index)+ c + tekst.substring(index);
	}
	
	public void insert(int index, String s)
	{	tekst = ""+tekst.substring(0,index)+ s + tekst.substring(index);
	}
	
	public String insertAndComplete(int index, String s)
	{	String newString = getSelection(0,index-1) + s + getSelection(index,tekst.length());
		return newString;
	}
	
	public void replace(int index, char c)
	{	deleteCharAt(index);
		insert(index,c);
	}
	
	public void insertFormuleVak(int pos, TekstFormuleVak tfv)
	{	int formNr = geefAantalFormules(pos);
		formules.insertElementAt(tfv,formNr);
	}
	
	public void insertAntwoordVak(int pos, TekstAntwoordVak tfv)
	{	int formNr = geefAantalFormules(pos);
		formules.insertElementAt(tfv,formNr);
	}
	
	public void deleteCharAt(int index)
	{	if(index>tekst.length()-2)return;
		if(tekst.charAt(index)=='@')
		{	int formNr = geefAantalFormules(index);
			formules.removeElementAt(formNr);
		}
		tekst = tekst.substring(0,index) + tekst.substring(index+1);
	}
	
	public String toString()
	{	return tekst;
	}
	
	public String toCompleteString()
	{	String completeString;
		completeString = new String(tekst);
		int toevoegNr = formules.size()-1;
		for(int i=completeString.length()-1 ; i>-1; i--)
		{	if(completeString.charAt(i)=='@')
			{	String formString = ((TekstDeelVak)formules.elementAt(toevoegNr)).toString();
				completeString = ""+completeString.substring(0,i)+ formString + completeString.substring(i+1);
				toevoegNr--;
			}
		}
		return completeString;
	}
	
	public int length()
	{	return tekst.length();
	}
	
	public void delete(int firstIndex, int lastIndex)
	{	if(lastIndex>tekst.length()-2)lastIndex = tekst.length()-2;
		for(int i=0 ; i<lastIndex-firstIndex+1; i++)
		{	deleteCharAt(firstIndex);
		}
		//tekst = tekst.substring(0,firstIndex) + tekst.substring(lastIndex+1);
	}
	
	public String getSelection(int firstIndex, int lastIndex)
	{	int teller = 0;
		String s = "";
		for(int i=0 ; i<tekst.length(); i++)
		{	if(i>=firstIndex && i<=lastIndex)
			{	if(tekst.charAt(i)=='@')
				{	s = s + ((TekstFormuleVak)formules.elementAt(teller)).toString();
				}
				else
				{	s = s + tekst.charAt(i);
				}
			}
			if(tekst.charAt(i)=='@')teller++;
		}
		return s;
	}
	
	public int geefAantalFormules(int pos)
	{	int teller = 0;
		for(int i=0 ; i<pos; i++)
	    {	if(tekst.charAt(i)==('@'))
		    {	teller++;	
		    }
	    }
	    return teller;
	}
	
	public TekstDeelVak geefDeelVak(int nr)
	{	return (TekstDeelVak)formules.elementAt(nr);
	}
	
	public int geefAantalFormules()
	{	return formules.size();
	}
}
