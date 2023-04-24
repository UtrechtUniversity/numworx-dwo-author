package fi.doorziendwo;

import java.awt.Color;

public class DrawConstants 
{
    // defined colors
    // #299C39
    public static final Color darkGreen = new Color(41, 156, 57);
    // #ADDE63
    public static final Color mediumGreen = new Color(173, 222, 99);
    // #D60000
    public static final Color brownRed = new Color(214, 0, 0);
    // #FF8429
    public static final Color lightRed = new Color(255, 156, 74);
    // #63C6DE
    public static final Color mediumBlue = new Color(99, 198, 222);    
    // #94D6E7
    public static final Color lightBlue = new Color(148, 214, 231);
    // #FFE7C6
    public static final Color lightOrange = new Color(255, 231, 198);
    // #EFE8AD
    public static final Color lightYellow = new Color(239, 232, 173);
	
    public static Color objectColor = Color.yellow;
    public static Color outlineColor = Color.black;
    public static Color lineColor = Color.blue;
    public static Color planeColor = Color.yellow; 
    public static Color planeOutlineColor = brownRed;    
    public static Color pointColor = darkGreen;
    public static Color tickColor = darkGreen;
    public static Color hiddenTickColor = mediumGreen;    
    
    public static Color flatButtonColor = mediumGreen;

	public static Color[] edgeColors =
    	{outlineColor, lineColor, planeOutlineColor, pointColor};
	
    // color indices in edgeColors   
    public static int lineColorIndex = 1;
    public static int planeOutlineColorIndex = 2;
    public static int pointColorIndex = 3;
	
	public static int hiddenOutlineMode = 0;
	
	public static boolean letters = false;
	
    public static int TICKNUM = 0;    
    public static boolean TICKSVISIBLE = false;
	
    public static double llFactor = 0;
    
    public static void reset()
    {
    	letters = false;
    	TICKNUM = 0;
    	TICKSVISIBLE = false;
    	llFactor = 0;
    }
}
