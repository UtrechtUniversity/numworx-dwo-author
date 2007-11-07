package fi.algebrapijlenopdr;

public class ZoomState 
{
	private double schaalFactorY=1;
	private int factorRijNummerY=99;
	private double schaalFactorX=1;
	private int factorRijNummerX=99;
	private int beginwaarde=0;
	private int selectnummer=999; 
	
	public void setSchaalFactorX(double schaalFactorX)
	{	this.schaalFactorX = schaalFactorX;
	}
	
	public void setSchaalFactorY(double schaalFactorY)
	{	this.schaalFactorY = schaalFactorY;
	}
	
	public void setFactorRijNummerX(int factorRijNummerX)
	{	this.factorRijNummerX = factorRijNummerX;
	}
	
	public void setFactorRijNummerY(int factorRijNummerY)
	{	this.factorRijNummerY = factorRijNummerY;
	}
	
	public void setBeginwaarde(int beginwaarde)
	{	this.beginwaarde = beginwaarde;
	}
	
	public void setSelectnummer(int selectnummer)
	{	this.selectnummer = selectnummer;
	}
	
	public double getSchaalFactorX()
	{	return schaalFactorX;
	}
	
	public double getSchaalFactorY()
	{	return schaalFactorY;
	}
	
	public int getFactorRijNummerX()
	{	return factorRijNummerX;
	}
	
	public int getFactorRijNummerY()
	{	return factorRijNummerY;
	}
	
	public int getBeginwaarde()
	{	return beginwaarde;
	}
	
	public int getSelectnummer()
	{	return selectnummer;
	}

}
