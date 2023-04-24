package fi.binomverdeling;

import java.util.Observable;

public class BVInteractiePanelModel extends Observable{
	private double p; //succeskans
	private int n; //aantal herhalingen
	private int successen;
	
	/**
	 * Constructor
	 * @param p succeskans
	 * @param n aantal herhalingen
	 * @param successen de grens voor berekenKansCumulatief
	 */
	public BVInteractiePanelModel(double p, int n, int successen) {
		this.p = p;
		this.n = n;
		this.successen = successen;
	}
	
	/**
	 * Aantal mogelijkheden voor een k-greep uit n
	 * @return C(n,k)
	 */
	public static double binom(int n, int k)
	{
		double[] b = new double[n+1];
		b[0] = 1;
		for(int i=1 ; i<n+1 ; i++)
		{	b[i] = 1;
			for(int j=i-1 ; j>0 ; j--)
			{	b[j] += b[j-1];
			}
		}
		return b[k];
	}
	
	/**
	 * komt neer op BinomPDF
	 * @param k het aantal te behalen successen
	 * @return P(X=k)
	 */
	public double berekenKansK(int k) {
		//DEBUG
		/*
		System.out.println("Debug:\n" + n + " " + k);
		System.out.println((Factorial.fac(n)));
		System.out.println((double)(Factorial.fac(k)*(double)Factorial.fac(n-k)));
		System.out.println((Factorial.fac(n))/(double)(Factorial.fac(k)*(double)Factorial.fac(n-k)));
		System.out.println((double)(Factorial.fac(n))/(double)(Factorial.fac(k)*(double)Factorial.fac(n-k))* (double)Math.pow(p,k) * (double)Math.pow(1-p, n-k));
		System.out.println("");
		*/
		
		return BVInteractiePanelModel.binom(n, k) * (double)Math.pow(p,k) * (double)Math.pow(1-p, n-k);
	}
	
	/**
	 * Komt neer op BinomCDF
	 * @return P(X<=this.successen)
	 */
	public double berekenKansCumulatief() {
		if(this.successen >= this.n) { //als successen >= n, dan telt alles mee, dus is de som 1
			return 1.0;
		}
		else {
			double som = 0;
			for (int count = 0; count <= Math.min(this.successen, this.n); count++) {
				som += this.berekenKansK(count);
			}
			return som;
		}
	}
	
	/**
	 * Zet een nieuwe waarde voor p
	 * @param p de nieuwe succeskans
	 */
	public void setP(double p) {
		if(p >= 0 && p <= 1) {
			this.p = p;
		}
		this.setChanged();
		this.notifyObservers();
	}
	
	/**
	 * @return de succeskans
	 */
	public double getP() {
		return this.p;
	}
	
	/**
	 * Zet een nieuwe waarde voor n
	 * @param n het nieuwe aantal herhalingen
	 */
	public void setN(int n) {
		if(n >= 0) {
			this.n = n;
		}
		this.setChanged();
		this.notifyObservers();
	}
	
	/**
	 * @return het aantal herhalingen
	 */
	public int getN(){
		return this.n;
	}
	
	public void setSuccessen(int successen) {
		this.successen = successen;
		this.setChanged();
		this.notifyObservers();
	}
	
	public int getSuccessen() {
		return this.successen;
	}
}
