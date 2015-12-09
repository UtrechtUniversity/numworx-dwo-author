package fi.graphtool;

class RoosterDefinitie {
	// Deze klasse groepeert alle parameters met respect tot de rooster definitie
	// het doel hiervan is om op lange termijn de betreffende globale variabelen in zijn geheel te elimineren
	
	private double beginx=0; // positie (in pixels) van oorsprong - X in grafiekveld
	private double beginy=0; // positie (in pixels) van oorsprong - Y in grafiekveld
	
	// let op! eenheid is gebaseerd op fijne schaling, grove schaling is * 2
	private int eenheidX=16; // eenheid in pixels
	private int eenheidY=16;
	private double eenheidXD=16; // double precisie variant (origineel) van eenheid
	private double eenheidYD=16;

	private boolean manualScalingX=false; // manueel instelbare schaling modus
	private boolean manualScalingY=false;
	private boolean xAsLog=false; // Logaritmische modus
	private boolean yAsLog=false;
	
	private double eenheidxValue=2; // eenheid in "waarde", = werkelijke coordinatenstelsel
	private double eenheidyValue=2; // alleen geldig in "manualScaling" modus

	private double schaalFactorX=1; // ingestelde schaalfactor, alleen geldig buiten "manualScaling" modus
	private double schaalFactorY=1; // 
	
	private int manScalingMultiplyX=1; // multiplier, wordt gebruikt om overbodige roosterlijnen te elimineren wanneer er wordt ingezoomd 
	private int manScalingMultiplyY=1; // alleen geldig in manualschaling modus
	                                  // originele eenheid in manual mode is deze factor * de eenheid	
	
	RoosterDefinitie (	double beginx, double beginy, int eenheidx, int eenheidy, double eenheidXD, double eenheidYD, 
						boolean manualScalingX, boolean manualScalingY, boolean xAsLog, boolean yAsLog, 
						double eenheidxValue, double eenheidyValue, double schaalFactorX, double schaalFactorY,
						int manScalingMultiplyX, int manScalingMultiplyY) {
		// constructor, met alle parameters, eenheid? wordt afgeleid van eenheid?D
		this.beginx = beginx;
		this.beginy = beginy;
		this.eenheidX = eenheidx;
		this.eenheidY = eenheidy;
		this.eenheidXD = eenheidXD;
		this.eenheidYD = eenheidYD;
		this.manualScalingX = manualScalingX;
		this.manualScalingY = manualScalingY;
		this.xAsLog = xAsLog;
		this.yAsLog = yAsLog;
		this.eenheidxValue = eenheidxValue;
		this.eenheidyValue = eenheidyValue;
		this.schaalFactorX = schaalFactorX;
		this.schaalFactorY = schaalFactorY;
		this.manScalingMultiplyX = manScalingMultiplyX;
		this.manScalingMultiplyY = manScalingMultiplyY;
	}
	
	/* Get and set functions */
	public double 	getBeginX() 									{ return beginx; }
	public void		setbeginX(double beginx) 						{ this.beginx = beginx; }
	public double 	getBeginY() 									{ return beginy; }
	public void		setbeginY(double beginy)						{ this.beginy = beginy; }
	
	public int 		getEenheidX() 									{ return eenheidX; }
	public void		setEenheidX(int eenheidX) 						{ this.eenheidX = eenheidX; }
	public int 		getEenheidY() 									{ return eenheidY; }
	public void		setEenheidY(int eenheidY) 						{ this.eenheidY = eenheidY; }
	
	public double	getEenheidXD() 									{ return eenheidXD; }
	public void		setEenheidXD(double eenheidXD) 					{ this.eenheidXD = eenheidXD; }
	public double 	getEenheidYD() 									{ return eenheidYD; }
	public void		setEenheidYD(double eenheidYD)					{ this.eenheidYD = eenheidYD; }
	
	public boolean	getManualScalingX() 							{ return manualScalingX; }
	public void		setManualScalingX(boolean manualScalingX)		{ this.manualScalingX = manualScalingX; }
	public boolean	getManualScalingY() 							{ return manualScalingY; }
	public void		setManualScalingY(boolean manualScalingY)		{ this.manualScalingY = manualScalingY; }

	public boolean	getXAsLog() 									{ return xAsLog; }
	public void		setXAsLog(boolean xAsLog)						{ this.xAsLog = xAsLog; }
	public boolean	getYAsLog() 									{ return yAsLog; }
	public void		setYAsLog(boolean yAsLog)						{ this.yAsLog = yAsLog; }
	
	public double 	getEenheidxValue() 								{ return eenheidxValue; }
	public void		setEenheidxValue(double eenheidxValue) 			{ this.eenheidxValue = eenheidxValue; }
	public double 	getEenheidyValue() 								{ return eenheidyValue; }
	public void		setEenheidyValue(double eenheidyValue) 			{ this.eenheidyValue = eenheidyValue; }

	public double 	getSchaalFactorX() 								{ return schaalFactorX; }
	public void		setSchaalFactorX(double schaalFactorX) 			{ this.schaalFactorX = schaalFactorX; }
	public double 	getSchaalFactorY() 								{ return schaalFactorY; }
	public void		setSchaalFactorY(double schaalFactorY) 			{ this.schaalFactorY = schaalFactorY; }

	public int 		getManScalingMultiplyX()						{ return manScalingMultiplyX; }
	public void		setManScalingMultiplyX(int manScalingMultiplyX)	{ this.manScalingMultiplyX = manScalingMultiplyX; }
	public int 		getManScalingMultiplyY()						{ return manScalingMultiplyY; }
	public void		setManScalingMultiplyY(int manScalingMultiplyY)	{ this.manScalingMultiplyY = manScalingMultiplyY; }	
}
