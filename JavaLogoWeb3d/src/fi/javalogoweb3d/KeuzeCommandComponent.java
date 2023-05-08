package fi.javalogoweb3d;

import java.awt.Color;
import java.awt.Component;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JToggleButton;

import fi.javalogoweb3d.parameters.BooleanParameter;
import fi.logotekenap3d.TraceBeheerder;
import fi.logotekenap3d.TekenApplet3D;

public class KeuzeCommandComponent extends CompositeCommandComponent implements ParameterEditorListener, ActionListener
{
	private BooleanParameter condition;
	
	private String alsString = JavaLogoWeb3d.rb.getString("alsLabel");
	private String andersString = JavaLogoWeb3d.rb.getString("andersLabel");
	public static final int blockX = 25;
	// note: elseBlockX is variable, given by a method
	public static final int ifBlockY = 25;
	
	private boolean inIfBlock = true;
	
	private CommandContainer ifBlock;
	private CommandContainer elseBlock;
	
	private boolean isEditing = false;
	private ParameterTextField conditionEditor;
	private int conditionEditorX;
	
	private boolean elseVisible = false;
	private JToggleButton elseVisibleButton;
	
	public KeuzeCommandComponent(int x, int y, int b, int h, JavaLogoSchuifVeld sv)
	{	
		super(x,y,b,h,sv);
		commandName = "Keuze";
		condition = new BooleanParameter();
		
		ifBlock = new CommandContainer( blockX, ifBlockY, blockWidth(), JavaLogoSchuifVeld.ccsh, this);
		add(ifBlock);
		elseBlock = new CommandContainer(blockX, elseBlockY(), blockWidth(), JavaLogoSchuifVeld.ccsh, this);
		if(elseVisible)
			add(elseBlock);
		
		FontMetrics fm = getFontMetrics(JavaLogoWeb3d.defaultfont);
		conditionEditorX = 10+fm.stringWidth(commandNameTranslated+" ");
		conditionEditor = new ParameterTextField(conditionEditorX, 4, 80, 17, this);
		add(conditionEditor);
		
		elseVisibleButton = new JToggleButton();
		elseVisibleButton.setOpaque(false);
		elseVisibleButton.setIcon(new ImageIcon(JavaLogoWeb3d.class.getResource("resources/klapuit1.png")));
		elseVisibleButton.setSelectedIcon(new ImageIcon(JavaLogoWeb3d.class.getResource("resources/klapuit2.png")));
		elseVisibleButton.setBounds(5, elseBlockY()-ifBlockY, 15, 15);
		elseVisibleButton.addActionListener(this);
		elseVisibleButton.setBackground(Color.orange);
		elseVisibleButton.setBorder(BorderFactory.createEmptyBorder());
		elseVisibleButton.setMargin(new Insets(0,0,0,0));
		add(elseVisibleButton);
	}
	
	public void setElseVisible(boolean b)
	{
		elseVisible = b;
		if(elseVisible)
			add(elseBlock);
		else
			remove(elseBlock);
		elseVisibleButton.setSelected(b);
	}
	
	/**
	 * xpos of elseBlock cannot be a constant
	 * 
	 * @return	elseBlockX
	 */
		
	public final int elseBlockY()
	{
		return 2*ifBlockY + ifBlock.getHeight();
	}
	
	public final int blockWidth()
	{
		return getWidth()-blockX+1;
	}
	
	
		
	/**
	 * PBgv: set the boolean expression (from programImporter).
	 * 
	 * @param expression	the boolean expression as a String
	 */
	public void setBoolExpression (String expression)
	{
		condition.setParameter(expression);
	}

	/**
	 * Select a container for future adding of components. Only relevant for scripting (ProgrammaImporter)
	 * 
	 * @param inIfBlock		true/false for obvious container selection
	 */
	public void setInIfBlock(boolean inIfBlock)
	{
		this.inIfBlock = inIfBlock;
	}

	/**
	 * Add a CommandComponent from source code, for instance by the ProgarammaImporter
	 * Note: Drag&drop will add a CC directly into one of the CommandContainers
	 * 
	 * @param cc			CommandComponent to be added
	 * @param inIfBlock		in if- or elseBlock
	 */
	@Override
	void addCComponent(CommandComponent cc)
	{	
		if ( inIfBlock )
		{
			ifBlock.addCComponent(cc);					
		} else
		{	
			elseBlock.addCComponent(cc);		
		}
	}
	
	@Override
	public void setSize(int w, int h)
	{	
		super.setSize(w,h);
		ifBlock.setWidth(w-blockX);
		// also move elseBlock to the middle
		elseBlock.setLocation(blockX, elseBlockY());
		elseBlock.setWidth(w-blockX);
		
		elseVisibleButton.setBounds(5, elseBlockY()-ifBlockY-18, 15, 15);
	}
	
	@Override
	public void setBounds(int x, int y, int w, int h)
	{	
		super.setBounds(x,y,w,h);
		if ( ifBlock != null && elseBlock != null)		// constructor will call setBounds before containers are made
		{
			ifBlock.setWidth(w-blockX);
			elseBlock.setLocation(blockX, elseBlockY());
			elseBlock.setWidth(w-blockX);
			
			elseVisibleButton.setBounds(5, elseBlockY()-ifBlockY-18, 15, 15);
		}
		
	}
	
	@Override
	void containerHeightChanged(int h)
	{
		if(elseVisible)
			super.setSize(getWidth(), 2*ifBlockY + ifBlock.getHeight() + elseBlock.getHeight());
		else
			super.setSize(getWidth(), ifBlockY + ifBlock.getHeight());
		((CommandContainer)getParent()).reArrange();
	}
	
	@Override
	public void parameterEdited(String text)
	{
		condition.setParameter(text);
		isEditing = false;
		schuifveld.repaint();
	}

	@Override
	public void parameterComponentClicked(int x, int y)
	{
		if ( isEditing )
		{
			condition.setParameter(conditionEditor.getText());
			conditionEditor.setVisible(false);
			conditionEditor.setEditable(false);
			isEditing = false;
		} else
		{	if ( y < ifBlockY )
			{
				isEditing = true;
				conditionEditor.vulIn(condition.getParameterText());
				conditionEditor.setLocation(conditionEditorX, 4);
			}
		}
		schuifveld.repaint();
	}

	@Override
	protected void paintBackground(Graphics g)
	{
		g.setColor(Color.orange);
		if(traceKleur)g.setColor(traceActiveColor);
		g.fillRect(0, 0, getSize().width-1, getSize().height-1);
		g.setColor(Color.black);
		g.drawRect(0, 0, getSize().width-1, getSize().height-1);
		g.drawRect(1, 1, getSize().width-3, getSize().height-3);
		//g.drawLine(0, 0, blockWidth(), blockY);
		//g.drawLine(0, 1, blockWidth(), blockY+1);
		//g.drawLine(getSize().width, 0, blockWidth(), blockY);
		//g.drawLine(getSize().width, 1, blockWidth(), blockY+1);
		g.setFont(JavaLogoWeb3d.defaultfont);
		g.drawString(alsString, 10, 18);
		g.drawString(andersString, 10, ifBlockY+ifBlock.getHeight() +18);		
	}

	@Override
	protected void paintCommand(Graphics g)
	{
		if ( isEditing ) return;			// nothing to paint, only the TextField
		g.setFont(JavaLogoWeb3d.defaultfont);
		if ( condition.isCorrect() )
		{
			g.setColor(Color.black);			
		} else
		{
			g.setColor(Color.RED);
		}
		g.drawString(condition.getParameterText(),conditionEditorX,18);
		
	}
	
	public boolean executeContent(TraceBeheerder trb, TekenApplet3D ub, VarSet varSet)
	{	
		boolean value = condition.getValue();
		traceKleur = trb.commandExecuted(varSet.getLevel());
		if ( traceKleur ) 
		{
			trb.setCommandInfo(alsString+" "+condition.getValueText(), varSet);
			return traceKleur;
		}
		if(value)
		{	for(int j=0 ; j<ifBlock.getComponentCount() ; j++)
			{	Component c = ifBlock.getComponent(j);
				if(c instanceof CommandComponent)
				{	boolean tracekleur = ((CommandComponent)c).execute(trb, ub, varSet);
					if(tracekleur) return true;
				}
			}
		}
		else
		{	for(int j=0 ; j<elseBlock.getComponentCount() ; j++)
			{	Component c = elseBlock.getComponent(j);
				if(c instanceof CommandComponent)
				{	boolean tracekleur = ((CommandComponent)c).execute(trb, ub, varSet);
					if(tracekleur) return true;
				}
			}
		}
		return false;
	}
	
	@Override
	public boolean execute(TraceBeheerder trb, TekenApplet3D ub, VarSet varSet)
	{	
		if ( !condition.isCorrect(varSet) ) return false; 
		varSet.increaseLevel("-- "+alsString+" "+condition.getValueText(), false);
		boolean b = executeContent(trb, ub, varSet);
		varSet.decreaseLevel();
		return b;
	}
		
		
	@Override
	public String getCode(String tab)
	{	//String s = tab + "Keuze: Als "+condition.getParameterText()+" Dan\n" + tab +"{\n";
		String s = tab + JavaLogoWeb3d.rb.getString("keuze") + " " + JavaLogoWeb3d.rb.getString("alsLabel") + " " + 
				   condition.getParameterText() + " " + JavaLogoWeb3d.rb.getString("danLabel") + "\n" + tab +"{\n";
		String tabNieuw = tab + "    ";
		s = s + ifBlock.getCode(tabNieuw);
		s = s + tab + "}\n";
		if ( elseBlock.getComponentCount() > 0 )
		{
			s = s + tab + JavaLogoWeb3d.rb.getString("andersLabel") + "\n" + tab +"{\n";
			s = s + elseBlock.getCode(tabNieuw);
			s = s + tab + "}\n";
		}
		return s;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==elseVisibleButton)
		{	elseVisible = elseVisibleButton.isSelected();
			if(elseVisible)
				add(elseBlock);
			else
			{	remove(elseBlock);
				elseBlock.removeAll();
			}
			containerHeightChanged(0);
		}
		
		
	}

}
