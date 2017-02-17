package fi.javalogoweb3d.parameters;

import java.awt.Color;

import fi.beans.stringutils.StringUtils;
import fi.javalogoweb3d.VarSet;
import fi.javalogoweb3d.expressies.*;
import fi.javalogoweb3d.formuleobjects.*;

/**
 * KleurParameter is the class for an input expression representing a Color.
 * Both text ("rood") and RGB-values can be given.
 * Used for parameters in CC's, or the number of repetitions in 'Herhaal'
 * 
 * @author berge020
 */
public class ColorParameter extends TAParameter
{
	private boolean isColorByName;		// true if parameter was given as a Color name (rood, groen...)
	
	/**
	 * Three expressions for the color rgb-components
	 * Note: this was an Expressie[3] first, but then there were 'ArrayStoreExceptions' when 
	 * assigning them with a subclass of Expressie! WtF!!
	 */
	private Expressie redExpression;			// for each of the color components R, G & B
	private Expressie greenExpression;			// for each of the color components R, G & B
	private Expressie blueExpression;			// for each of the color components R, G & B
	
	int[] rgb = new int[3];

	/**
	 * true if '...EV' is a syntactically correct expression, given that all variables exist
	 */
	private boolean redExpressionValid;
	private boolean greenExpressionValid;
	private boolean blueExpressionValid;
	/**
	 * true if 'waarde' can be calculated, that is: all its variables exist and have valid numerical values, 
	 * no division by zero, etc. When color is indicated by name, it is true if the name is in the list
	 * of standard colors.
	 * NOTE: at this moment this can only determined at execution time.
	 */
	// private boolean isCorrect; defined in superclass
	
	private Color theColor;
	
	public ColorParameter()
	{
		setDefaultColor();
	}
	
	/**
	 * Set the Color to black, the default when no input is given.
	 */
	private void setDefaultColor()
	{
		parameterText = "";
		isColorByName = true;
		theColor = Color.BLACK;
		isCorrect = true;
	}
	
	private void setAllValid(boolean b)
	{
		redExpressionValid = b;
		greenExpressionValid = b;
		blueExpressionValid = b;
	}
	
	private boolean allValid()
	{
		return redExpressionValid && greenExpressionValid && blueExpressionValid;
	}
	
	/**
	 * Sets the text and determines if the expression is valid.
	 * 
	 * @param text		the input string to be parsed
	 */
	@Override
	public void setParameter(String text)
	{
		parameterText = text.trim();
		if ( parameterText.equals("") )
		{
			setDefaultColor();
			return;
		}
		String[] args = StringUtils.split(parameterText, ",");
		if ( args.length == 1 )
		{
			parseColorName(args[0]);
		} else
		{
			parseRGB(args);
			isCorrect = allValid(); 	// we assume variables in r,g,b-expressions to be ok until the program runs
		}
	}
	
	private void parseColorName(String s)
	{
		isColorByName = true;
		isCorrect = true;			// until proven differently...
		s = s.trim();
		if ( s.equals("rood")) theColor = Color.RED;
		else if ( s.equals("groen")) theColor = Color.GREEN;
		else if ( s.equals("blauw")) theColor = Color.BLUE;
		else if ( s.equals("geel")) theColor = Color.YELLOW;
		else if ( s.equals("cyaan")) theColor = Color.CYAN;
		else if ( s.equals("roze")) theColor = Color.PINK;
		else if ( s.equals("zwart")) theColor = Color.BLACK;
		else if ( s.equals("grijs")) theColor = Color.GRAY;
		else if ( s.equals("lichtgrijs")) theColor = Color.LIGHT_GRAY;
		else if ( s.equals("magenta")) theColor = Color.MAGENTA;
		else if ( s.equals("wit")) theColor = Color.WHITE;
		else if ( s.equals("oranje")) theColor = Color.ORANGE;
		
		else if ( s.equals("red")) theColor = Color.RED;
		else if ( s.equals("green")) theColor = Color.GREEN;
		else if ( s.equals("blue")) theColor = Color.BLUE;
		else if ( s.equals("yellow")) theColor = Color.YELLOW;
		else if ( s.equals("cyan")) theColor = Color.CYAN;
		else if ( s.equals("pink")) theColor = Color.PINK;
		else if ( s.equals("black")) theColor = Color.BLACK;
		else if ( s.equals("gray")) theColor = Color.GRAY;
		else if ( s.equals("lightGray")) theColor = Color.LIGHT_GRAY;
		else if ( s.equals("magenta")) theColor = Color.MAGENTA;
		else if ( s.equals("white")) theColor = Color.WHITE;
		else if ( s.equals("orange")) theColor = Color.ORANGE;
		
		else isCorrect = false;		// ... here
	}

	private void parseRGB(String s[])
	{
		isColorByName = false;
		setAllValid(true);
		if ( s.length != 3 )
		{
			setAllValid(false);		
			return;
		}
		// rood
		s[0] = s[0].trim();
		try
		{	
			redExpression = FormuleParser.geefExpressie("$f"+s[0]+"@");
		}
		catch(NumberFormatException ex) { redExpressionValid = false; }
		if ( redExpression == null ) { redExpressionValid = false; }
		// groen
		s[1] = s[1].trim();
		try
		{	
			greenExpression = FormuleParser.geefExpressie("$f"+s[1]+"@");
		}
		catch(NumberFormatException ex) { greenExpressionValid = false; }
		if ( greenExpression == null ) { greenExpressionValid = false; }
		// blauw
		s[2] = s[2].trim();
		try
		{	
			blueExpression = FormuleParser.geefExpressie("$f"+s[2]+"@");
		}
		catch(NumberFormatException ex) { blueExpressionValid = false; }
		if ( blueExpression == null ) { blueExpressionValid = false; }
	}
	
	/**
	 * Return the text of this ColorParameter. When the parameter is not valid or constant,
	 * the original text is returned (for editing)
	 * 
	 * @return		text
	 *
	@Override
	public String getParameterText()
	{
		if ( !isColorByName )
		{
			if ( allValid() )
			{	
				return redExpression.toString()+", "+greenExpression.toString()+", "+blueExpression.toString();
			}
		} 
		return parameterText;
	} */
	
	/**
	 * Calculate a single colorcomponent r/g/b with given VarSet
	 * @param e
	 * @param varSet
	 * @return			int, one of r,g,b
	 */
	private int calculateColorValue(Expressie e, VarSet varSet)
	{
		// value of colorcomponent RED
		double value = e.geefWaarde();
		if(Double.isNaN(value))
			value = varSet.getExpressionValue(e);
		if ( Double.isNaN(value))
		{	
			isCorrect = false;
			return 0;						// irrelevant, because of 'isCorrect'
		} else
		{
			return Math.max(0, Math.min((int)value, 255));	//just limit... is not ok!!
		}
	}
	
	/**
	 * At execution time, test the parameter expressions with the given VarSet.
	 * Also generate the Color that goes with the RGB-values.
	 * Note: when the color is constant, indicated by name, this will return true if the name is ok,
	 * which has been estabished earlier.
	 * 
	 * @param varSet	current VarSet in the running program
	 * @return			true, if a valid color can be generated from teh VarSet
	 */
	@Override
	public boolean isCorrect(VarSet varSet)
	{
		if ( isColorByName )
		{
			return isCorrect;			// varSet is irrelevant when color is constant, indicated by name.
		} else
		{
			if ( !allValid() )
			{
				isCorrect = false;
				return isCorrect;
			}
			isCorrect = true;			// will be set to false if an error occurs
			rgb[0] = calculateColorValue(redExpression, varSet);
			rgb[1] = calculateColorValue(greenExpression, varSet);
			rgb[2] = calculateColorValue(blueExpression, varSet);
			if ( isCorrect )
			{
				theColor = new Color(rgb[0], rgb[1], rgb[2]);
			}
			return isCorrect;
		}
	}	
		
	/**
	 * Get the Color of this ColorParameter. Only call this method at execution time after testing
	 * is the color parameter is correct
	 * 
	 * @return the Color
	 */
	public Color getColor()
	{
		return theColor;
	}

	@Override
	public boolean isCorrect()
	{
		return isCorrect;
	}

	@Override
	public String getValueText()
	{
		if ( isColorByName )
		{
			return getParameterText();
		} else
		{
			return rgb[0] + ", " + rgb[1] + ", " + rgb[2];
		}
	}
}
