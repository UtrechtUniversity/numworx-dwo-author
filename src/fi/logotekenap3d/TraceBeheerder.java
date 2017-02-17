package fi.logotekenap3d;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JTextField;

import fi.javalogoweb3d.JavaLogoSchuifVeld;
import fi.javalogoweb3d.JavaLogoWeb3d;
import fi.javalogoweb3d.VarSet;
import fi.javalogoweb3d.VardisplayPanel;

public class TraceBeheerder extends JPanel implements ActionListener, ItemListener
{
	private JButton stapKnop,terugKnop,beginKnop,skipKnop,traceKnop;
	private JCheckBox showVariables;
	private JTextField methodeVeld;
	private int maxAantalStappen,aantalStappen;
	private TekenApplet3D tb;
	private JavaLogoSchuifVeld jlsveld;
	private boolean traceAan;
	
	private boolean isVartracing = false;
	private VardisplayPanel vartracer = null;
	
	private int currentlevel;
	private boolean isSkipping;
	private int skipLevel;
	
	public TraceBeheerder(TekenApplet3D tb, JavaLogoSchuifVeld v)
	{	
		setLayout(null);
		setOpaque(false);
		makeGUI();
		aantalStappen = 0;
		maxAantalStappen = 0;
		isSkipping = false;
		skipLevel = 0;
		this.tb = tb;
		jlsveld = v;
		traceAan = false;
		setComponentVisibilty(false);
	}
	
	private void makeGUI()
	{
		beginKnop = new JButton(JavaLogoWeb3d.rb.getString("beginKnopLabel"));
		beginKnop.setBounds(0,5,50,23);
		beginKnop.setFont(JavaLogoWeb3d.boldfont);
		beginKnop.setMargin(new Insets(0,0,0,0));
		beginKnop.addActionListener(this);
		add(beginKnop);
		stapKnop = new JButton(JavaLogoWeb3d.rb.getString("stapKnopLabel"));
		stapKnop.setBounds(60,5,50,23);
		stapKnop.setFont(JavaLogoWeb3d.boldfont);
		stapKnop.setMargin(new Insets(0,0,0,0));
		stapKnop.addActionListener(this);
		add(stapKnop);
		terugKnop = new JButton(JavaLogoWeb3d.rb.getString("terugKnopLabel"));
		terugKnop.setBounds(120,5,50,23);
		terugKnop.setFont(JavaLogoWeb3d.boldfont);
		terugKnop.setMargin(new Insets(0,0,0,0));
		terugKnop.addActionListener(this);
		add(terugKnop);
		skipKnop = new JButton(JavaLogoWeb3d.rb.getString("skipKnopLabel"));
		skipKnop.setBounds(180,5,50,23);
		skipKnop.setFont(JavaLogoWeb3d.boldfont);
		skipKnop.setMargin(new Insets(0,0,0,0));
		skipKnop.addActionListener(this);
		add(skipKnop);
		showVariables = new JCheckBox(JavaLogoWeb3d.rb.getString("showVarLabel"));
		showVariables.setOpaque(false);
		showVariables.addItemListener(this);
		showVariables.setEnabled(true);
		showVariables.setSelected(false);
		showVariables.setBounds(240, 5, 160, 23);
		showVariables.setFont(JavaLogoWeb3d.boldfont);
		add(showVariables);
		
		traceKnop = new JButton(JavaLogoWeb3d.rb.getString("traceOnLabel"));
		traceKnop.setBounds(0,32,170,23);
		traceKnop.setFont(JavaLogoWeb3d.boldfont);
		traceKnop.setMargin(new Insets(0,0,0,0));
		traceKnop.addActionListener(this);
		add(traceKnop);
		methodeVeld = new JTextField("",15);
		methodeVeld.setBounds(180,32,160,23);
		methodeVeld.setFont(JavaLogoWeb3d.defaultfont);
		methodeVeld.setMargin(new Insets(0,0,0,0));
		add(methodeVeld);

		
		// handle vartracer panel here
		vartracer = new VardisplayPanel();
		vartracer.setBounds(JavaLogoSchuifVeld.ccx, JavaLogoSchuifVeld.ccy, 2*JavaLogoSchuifVeld.ccsw+10, 515);
	}
	
	private void setComponentVisibilty(boolean b)
	{	methodeVeld.setVisible(b);
		beginKnop.setVisible(b);
		stapKnop.setVisible(b);
		terugKnop.setVisible(b);
		skipKnop.setVisible(b);
		showVariables.setVisible(b);
	}

	//-------------------------------------------------------------------------------------------
	// Execution and tracing of programs 
	//-------------------------------------------------------------------------------------------
	
	/**
	 * Will execute and paint a program without tracing
	 */
	public void executeProgram()
	{
		traceAan = false;
		jlsveld.execute(this, tb);
	}
	
	public boolean isTraceAan()
	{
		return traceAan;
	}

	/**
	 * Will execute and paint a program when tracing, up to the number of steps
	 * indicated by the variable maxAantalStappen.
	 */
	public void traceProgram()
	{
		// clear oude tracekleur
		traceAan = true;
		aantalStappen = 0;
		if ( maxAantalStappen > 0 )
		{
			jlsveld.execute(this, tb);
			// zet laatste command in textfield; methodeVeld.setText(...);
		}
		jlsveld.repaint();				// show the pink trace color!
	}
	
	/**
	 * Callback method for execution of a program while tracing. CommandComponents will call
	 * this method when they have completed (or started: deeltaak, loop, if...)
	 * TraceBeheerder will signal the end of execution when maxAaantalStappen is reached.
	 * 
	 * @return	true, if execution of the program must stop at this point, false otherwise.
	 */
	public boolean commandExecuted(int commandLevel)
	{
		if ( !traceAan ) return false;
		aantalStappen++;
		if ( aantalStappen == maxAantalStappen )
		{
			if ( isSkipping )
			{											// check level of this command
				if ( commandLevel < skipLevel )
				{										// lower then skipLevel, return to normal tracing
					isSkipping = false;
					return true;
				} else
				{										// in skipped block				
					maxAantalStappen++;					// new max after skipping this command, increase max here
					return false;						// ... to make next command satify the first if
				}
			} else
			{											// not skipping, stop at this command
				return true;
			}
		} else
		{
			return false;
		}
	}

	/**
	 * Set text of 'methodeVeld' and (if var tracing is on) the varset in the vartracer.
	 * This method will be called from the execute-methods in the CC's, when trace is on
	 * and execution stops at that command.
	 * 
	 * @param varset	the current set of variables in tracing mode
	 */
	public void setCommandInfo(String actualCommand, VarSet varset)
	{
		currentlevel = varset.getLevel();
		methodeVeld.setText(actualCommand);
		if ( isVartracing )
		{
			vartracer.setContent(varset.toString());
		}
	}
	
	/**
	 * Switch tracing off and set the related vars, component visibility and tracebutton label
	 * Does not paint the program!!
	 * This method will be called when user clicks 'Run', which earlier on left TraceBeheerder in confused state.
	 */
	public void switchTracingOff()
	{
		traceAan = false;
		showVariables.setSelected(false);
		setVartracing(false);
		setComponentVisibilty(false);
		traceKnop.setText(JavaLogoWeb3d.rb.getString("traceOnLabel"));
	}
	
	//-------------------------------------------------------------------------------------------
	//afhandeling van de knopacties
	//-------------------------------------------------------------------------------------------
	@Override
	public void actionPerformed(ActionEvent e)
	{	if(e.getSource() == stapKnop)
		{	
			maxAantalStappen++;
			tb.paintDrawing(true);
		}
		if(e.getSource() == terugKnop)
		{	
			maxAantalStappen--;
			if(maxAantalStappen<0)maxAantalStappen=0;
			tb.paintDrawing(true);
		}
		if(e.getSource() == skipKnop)
		{	
			skipLevel = currentlevel;
			isSkipping = true;
			tb.paintDrawing(true);
		}
		if(e.getSource() == beginKnop)
		{	
			vartracer.setContent("");
			methodeVeld.setText("");
			isSkipping = false;					// previous trace may have stopped in skip
			maxAantalStappen = 0;
			tb.paintDrawing(true);
		}
		if(e.getSource() == traceKnop)
		{	
			if(!traceAan)
			{	
				traceAan = true;
				isSkipping = false;
				maxAantalStappen = 0;
				methodeVeld.setText("");
				tb.paintDrawing(true);
				traceKnop.setText(JavaLogoWeb3d.rb.getString("traceOffLabel"));
				setComponentVisibilty(true);
			}
			else
			{	
				switchTracingOff();
				tb.paintDrawing(false);
			}
			repaint();
		}
	}
	
	@Override
	public void itemStateChanged(ItemEvent e)
	{
		boolean b = ( e.getStateChange() == ItemEvent.SELECTED );
		setVartracing(b);
	}
	
	private void setVartracing(boolean b)
	{
		if ( b )
		{
			isVartracing = true;
			jlsveld.add(vartracer, 0);
		} else
		{
			isVartracing = false;
			jlsveld.remove(vartracer);
			vartracer.setContent("");
		}
		jlsveld.repaint();
	}
}