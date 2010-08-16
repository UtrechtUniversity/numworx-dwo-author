package fi.binomverdeling;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.Hashtable;

import oefening.BVModel;
import oefening.BVView;
import fi.beans.wiskopdrbeans.InteractieEditPanel;
import fi.beans.wiskopdrbeans.InteractiePanel;

/**
 * Controller van het BVInteractiePanel gedeelte
 */
public class BVInteractiePanel implements InteractiePanel, ActionListener {
	private BVInteractiePanelModel model;
	private BVInteractiePanelView view;

	public BVInteractiePanel() {
		this.model = new BVInteractiePanelModel(0.3, 30, 5);
		this.view = new BVInteractiePanelView(this.model);
		//this.view.addComponentListener(this);
		this.view.addNListener(this);
		this.view.addKListener(this);
		this.view.addPListener(this);
	}
	
	/* resize gedeelte, niet meer nodig omdat het JFrame niet meer bij dit deel hoort.
	@Override
	public void componentHidden(ComponentEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void componentMoved(ComponentEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void componentResized(ComponentEvent arg0) {
		this.view.repaint();

	}

	@Override
	public void componentShown(ComponentEvent arg0) {
		// TODO Auto-generated method stub

	}
	*/

	@Override
	public void actionPerformed(ActionEvent arg0) {
		if (arg0.getActionCommand().equals("ntextupdate")) {
			int n;
			try {
				n = Integer.parseInt(this.view.getNText());
			}
			catch (NumberFormatException e) {
				n = this.model.getN();
			}
			this.model.setN(n);
		}
		if (arg0.getActionCommand().equals("ptextupdate")) {
			double p;
			try {
				p = Double.parseDouble(this.view.getPText());
			}
			catch (NumberFormatException e) {
				p = this.model.getP();
			}
			this.model.setP(p);
		}
		if (arg0.getActionCommand().equals("ktextupdate")) {
			int k;
			try {
				k = Integer.parseInt(this.view.getKText());
			}
			catch (NumberFormatException e) {
				k = this.model.getSuccessen();
			}
			this.model.setSuccessen(k);
		}
	}
	
	
	
	//============================================================================
	//Interface implementatie
	//============================================================================
	public void zetOpdracht(Hashtable h, String[] randomVars, Hashtable randomValues)
	{
		
	}
	
	public void setState(Hashtable h)
	{
		
	}
	
	
	public void setEditState(Hashtable h)
	{
		
	}
	
	public Hashtable getState()
	{
		Hashtable h = new Hashtable();
		return h;
	}
	
	public Hashtable getEditState()
	{
		Hashtable h = new Hashtable();
		return h;
	}
	
	public InteractieEditPanel getEditPanel()
	{
		return new BVInteractieEditPanel();
	}
	public void setBounds(int x, int y, int b, int h)
	{	
		//super.setBounds(x,y,b,h);
	}
	public void wis()
	{
		
	}
	public void zetMaat()
	{
	
	}
	public int geefAsHoogte()
	{
		return 0;
	}
	public int getIpId()
	{
		return 0;
	}
	
	public int getScore()
	{
		return 0;
	}
	public int getScoreMax()
	{
		return 0;
	}
	public boolean isCorrect()
	{
		return true;
	}
	public boolean isFout()
	{
		return false;
	}
	public void zetMode(int mode)
	{
	
	}
	public void zetNagekeken(boolean b)
	{
	
	}
    public void stop()
	{
	
	}
    public void start()
	{
    	
	}
   
    public void destroy()
	{
	
	}
    public void opnieuw()
	{
	
	}
    public void kijkNa()
	{
	
	}
    public void kijkNa(int stapNr)
	{
	
	}
    public void addActionListener(ActionListener al)
	{
	
	}
	
	
	
}
