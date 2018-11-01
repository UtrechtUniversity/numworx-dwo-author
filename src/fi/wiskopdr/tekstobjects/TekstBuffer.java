package fi.wiskopdr.tekstobjects;

import java.util.*;

import fi.beans.wiskopdrbeans.InteractiePanel;
import fi.wiskopdr.tekstobjects.*;
import fi.wiskopdr.InteractiePanelContainerIF;
import fi.wiskopdr.WiskOpdr;

public class TekstBuffer
{
	private String tekst;
	private Vector tekstDeelVakken;
	private TekstVak tekstVak; 
	
	//public TekstBuffer(String string)
	//{	tekst = string+'\n';
	//	formules = new Vector();
	//}
	
	public TekstBuffer(TekstVak tv, String completeString)
	{	this(tv, completeString, false);
	}
	
	public TekstBuffer(TekstVak tv, String completeString, boolean setEdit)
	{	
		tekstDeelVakken = new Vector();
		tekstVak = tv; 
		
		int toevoegNr = tekstDeelVakken.size()-1;
		/*for(int i=completeString.length()-1 ; i>-1; i--)
		{	if(completeString.charAt(i)=='@')
			{	int index = completeString.lastIndexOf("$f");
				TekstFormuleVak tfv = new TekstFormuleVak(tekstVak);
				String formString = completeString.substring(index,i+1);
				tfv.vulVak(formString);
				completeString = ""+completeString.substring(0,index)+"@"+completeString.substring(i+1);
				formules.insertElementAt(tfv,0);
				i=index;
			}
		}*/
		for(int i=completeString.length()-1 ; i>-1; i--)
		{	if(completeString.charAt(i)=='@')
			{	int indexF = completeString.lastIndexOf("$f");
				int indexR = completeString.lastIndexOf("$R");
				int indexA = completeString.lastIndexOf("$A");
				int indexV = completeString.lastIndexOf("$V");
				int indexH = completeString.lastIndexOf("$H");
				int indexI = completeString.lastIndexOf("$I");
				int index = -1;
				
				index = Math.max(indexF, indexA);
				index = Math.max(index, indexR);
				index = Math.max(index, indexH);
				index = Math.max(index, indexV);
				index = Math.max(index, indexI);
				if(index==-1) break;
				
				TekstDeelVak tfv = null;
				if(index == indexI)
				{	tfv = new TekstImageVak(tekstVak);
					((TekstImageVak)tfv).setEditMode(tekstVak.isEditable());
				} 
				else if(indexF == index)
				{	tfv = new TekstFormuleVak(tekstVak);
					tfv.setForeground(tekstVak.getForeground());
					index = indexF;
				}
				else if(indexR == index)
				{	tfv = new TekstFormuleVak(tekstVak,true);
					tfv.setForeground(tekstVak.getForeground());
					//tfv.setFont(tekstVak.getFont());
					index = indexR;
				}
				else if(indexA == index)
				{	tfv = new TekstAntwoordVak(tekstVak);
					((TekstAntwoordVak)tfv).setEditMode(tekstVak.isEditable());
					tfv.setEditable(true);
					index = indexA;
				}
				else if(indexV == index)
				{	tfv = new TekstInteractiePanelVak(tekstVak);
					tfv.addActionListener(tekstVak);
					((TekstInteractiePanelVak)tfv).setEditMode(tekstVak.isEditable());
					tfv.setEditable(true);
					index = indexV;
				}
				else if(indexH == index)
				{	tfv = new TekstLinkVak(tekstVak);
					tfv.setEditable(true);
					index = indexH;
				}
				String formString = completeString.substring(index,i+1);
				if (indexF == index || indexR == index)
					tfv.vulVak(formString);
				else if(indexV != index || setEdit) tfv.vulVak(formString.substring(2,formString.length()-1));
				completeString = ""+completeString.substring(0,index)+"@"+completeString.substring(i+1);
				tekstDeelVakken.insertElementAt(tfv,0);
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
	{	
		if(c!='\n')tekstVak.addState();
		tekst = ""+tekst.substring(0,index)+ c + tekst.substring(index);
		WiskOpdr.setLaunchDataChanged();
	}
	
	public void insert(int index, String s)
	{	tekst = ""+tekst.substring(0,index)+ s + tekst.substring(index);
		WiskOpdr.setLaunchDataChanged();
	}
	
	public String insertAndComplete(int index, String s)
	{	String newString = getSelection(0,index-1) + s + getSelection(index,tekst.length());
		WiskOpdr.setLaunchDataChanged();
		return newString;
	}
	
	public void replace(int index, char c)
	{	deleteCharAt(index);
		insert(index,c);
	}
	
	/*public void insertFormule(int pos, TekstFormuleVak tfv)
	{	int formNr = geefAantalFormules(pos);
		formules.insertElementAt(tfv,formNr);
	}*/
	
	public void insertFormuleVak(int pos, TekstFormuleVak tfv)
	{	int formNr = geefAantalFormules(pos);
		tekstDeelVakken.insertElementAt(tfv,formNr);
	}
	
	public void insertAntwoordVak(int pos, TekstAntwoordVak tfv)
	{	insert(pos+1, '\n');
		int formNr = geefAantalFormules(pos);
		tekstDeelVakken.insertElementAt(tfv,formNr);
	
	}
	
	public void insertTekstInteractiePanelVak(int pos, TekstInteractiePanelVak tfv)
	{	int formNr = geefAantalFormules(pos);
		tekstDeelVakken.insertElementAt(tfv,formNr);
	
	}
	
	public void insertLinkVak(int pos, TekstLinkVak tfv)
	{	int formNr = geefAantalFormules(pos);
		tekstDeelVakken.insertElementAt(tfv,formNr);
	
	}
	
	public void insertImageVak(int pos, TekstImageVak plv) {
		insert(pos+1, '\n');
		int formNr = geefAantalFormules(pos);
		tekstDeelVakken.insertElementAt(plv,formNr);
	}
	
	public void deleteCharAt(int index)
	{	
		if(index>tekst.length()-2)return;
		if(tekst.charAt(index)=='@')
		{	int formNr = geefAantalFormules(index);
			tekstDeelVakken.removeElementAt(formNr);
		}
		tekst = tekst.substring(0,index) + tekst.substring(index+1);
		WiskOpdr.setLaunchDataChanged();
		
	}
	
	public String toString()
	{	return tekst;
	}
	
	/*public String toCompleteString()
	{	String completeString;
		completeString = new String(tekst);
		int toevoegNr = formules.size()-1;
		for(int i=completeString.length()-1 ; i>-1; i--)
		{	if(completeString.charAt(i)=='@')
			{	String formString = ((TekstFormuleVak)formules.elementAt(toevoegNr)).toString();
				completeString = ""+completeString.substring(0,i)+ formString + completeString.substring(i+1);
				toevoegNr--;
			}
		}
		return completeString;
	}*/
	
	public String toCompleteString()
	{	String completeString;
		completeString = new String(tekst);
		int toevoegNr = tekstDeelVakken.size()-1;
		for(int i=completeString.length()-1 ; i>-1; i--)
		{	if(completeString.charAt(i)=='@')
			{	String formString = ((TekstDeelVak)tekstDeelVakken.elementAt(toevoegNr)).toString();
				completeString = ""+completeString.substring(0,i)+ formString + completeString.substring(i+1);
				toevoegNr--;
			}
		}
		return completeString;
	}
	
	public String toCompleteEditString()
	{	String completeString;
		completeString = new String(tekst);
		int toevoegNr = tekstDeelVakken.size()-1;
		for(int i=completeString.length()-1 ; i>-1; i--)
		{	if(completeString.charAt(i)=='@')
			{	String formString = ((TekstDeelVak)tekstDeelVakken.elementAt(toevoegNr)).toCompleteString();
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
	{	tekstVak.addState();
		if(lastIndex>tekst.length()-2)lastIndex = tekst.length()-2;
		for(int i=0 ; i<lastIndex-firstIndex+1; i++)
		{	deleteCharAt(firstIndex);
		}
		WiskOpdr.setLaunchDataChanged();
		//tekst = tekst.substring(0,firstIndex) + tekst.substring(lastIndex+1);
	}
	
	public String getSelection(int firstIndex, int lastIndex)
	{	int teller = 0;
		String s = "";
		for(int i=0 ; i<tekst.length(); i++)
		{	if(i>=firstIndex && i<=lastIndex)
			{	if(tekst.charAt(i)=='@')
				{	s = s + ((TekstDeelVak)tekstDeelVakken.elementAt(teller)).toCompleteString();
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
	
	/*public TekstFormuleVak geefFormule(int nr)
	{	return (TekstFormuleVak)formules.elementAt(nr);
	}*/
	
	public Vector geefInteractiePanels()
	{	return geefInteractiePanels(new Vector());
	}
	
	public boolean hasOneDeelVak()
	{	return tekstDeelVakken.size() == 1;
	}
	
	public TekstInteractiePanelVak getWidgetContainer(String crossWidgetId)
	{	TekstInteractiePanelVak tipv = null;
		for( int i=0 ; i< tekstDeelVakken.size() ; i++)
		{	if(tekstDeelVakken.elementAt(i) instanceof TekstInteractiePanelVak)
			{	tipv = ((TekstInteractiePanelVak)tekstDeelVakken.elementAt(i)).getWidgetContainer(crossWidgetId);
				if(tipv!=null)
					return tipv;
			}
		}
		return tipv;
	}
	
	public Vector geefInteractiePanels(Vector v)
	{	for( int i=0 ; i< tekstDeelVakken.size() ; i++)
		{	if(tekstDeelVakken.elementAt(i) instanceof InteractiePanelContainerIF)
			{	InteractiePanelContainerIF ipc = (InteractiePanelContainerIF)tekstDeelVakken.elementAt(i);
				v.addElement(ipc);
			}
		}
		return v;
	}
	
	public InteractiePanel zoekInteractiePanel(int ID)
	{
		for( int i=0 ; i< tekstDeelVakken.size() ; i++)
		{	if(tekstDeelVakken.elementAt(i) instanceof InteractiePanelContainerIF)
			{	InteractiePanel ip = ((InteractiePanelContainerIF)tekstDeelVakken.elementAt(i)).getInteractiePanel(ID);
				if(ip!=null) return ip;
			}
		}
		return null;
	}
	
	public TekstDeelVak geefDeelVak(int nr)
	{	return (TekstDeelVak)tekstDeelVakken.elementAt(nr);
	}
	
	public Vector geefDeelVakken()
	{	return tekstDeelVakken;
	}
	
	public TekstFormuleVak geefTekstFormuleVak(int textIndex)
	{	int tekstDeelVakNr=0;
		int index=0;
		for(int i=0 ; i<tekstDeelVakken.size() ; i++)
		{	int indexNieuw = tekst.indexOf('@',index);
			TekstDeelVak tdv = (TekstDeelVak)tekstDeelVakken.elementAt(tekstDeelVakNr);
			if(indexNieuw == textIndex && tdv instanceof TekstFormuleVak) return (TekstFormuleVak)tdv;
			tekstDeelVakNr++;
			index = indexNieuw+1;
		}
		return null;
	}
	
	public int geefAantalFormules()
	{	return tekstDeelVakken.size();
	}
}
