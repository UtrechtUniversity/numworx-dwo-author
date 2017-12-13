package fi.wiskopdr;

import java.awt.Color;
import java.util.Map;

public class StateToCss {

	public static String createCssFromAllStyles()
	{
		if(TekstVakPanel.styles == null)
			return "";
		
		String cssString = "svg|text {\n\tstroke: none;\n}\n\nsvg|rect {\n\tstroke:none;\n }\n\n";
		for (String key : TekstVakPanel.styles.keySet())
			cssString += createCssFromStyle(key, TekstVakPanel.styles.get(key));
			
		
		System.out.println(cssString);
		
		return cssString;
	}

	public static String createCssFromStyle(String styleString, Map<String, Object> style)
	{
		String cssOutput = "";
		
		String cssMain = "." + styleString + "-main {\n";
		int a = 0; int b = 0; int c = 0;
		if(style.containsKey("cellSpaceColumn")) a = ((Integer) style.get("cellSpaceColumn")).intValue();
		if(style.containsKey("cellSpaceRow")) b = ((Integer) style.get("cellSpaceRow")).intValue();
		if(style.containsKey("randDikte")) c = ((Integer) style.get("randDikte")).intValue();
		cssMain += "\tborder-spacing:" + a + "px " + b + "px;\n";
		cssMain += "\tmargin:" + (-(b+c)) + "px " + (-(a+c)) + "px;\n";
		cssMain += "}\n\n";
		cssOutput += cssMain;
		
		String cssMain2 = "." + styleString + "-main2 {\n";
		
		a = 0;
		if(style.containsKey("bgColorZichtbaar") && ((Boolean) style.get("bgColorZichtbaar")).booleanValue()) 
			a = 1;
		
		int r = 255; int g = 255; b = 255;
		if (style.containsKey("bgColor_red") ) 
		{
			r = ((Integer) style.get("bgColor_red")).intValue();
			if (style.containsKey("bgColor_green"))	g = ((Integer) style.get("bgColor_green")).intValue();
			if (style.containsKey("bgColor_blue")) b = ((Integer) style.get("bgColor_blue")).intValue();
		}
		else if(style.containsKey("bgColor"))
		{
			Color bgColor = (Color) style.get("bgColor");
			r = bgColor.getRed();
			g = bgColor.getGreen();
			b = bgColor.getBlue();
			
//			Hashtable colorMap = style.getObjectMap("bgColor");
//			if(colorMap != null) {
//				r = colorMap.getInt("red");
//				g = colorMap.getInt("green");
//				b = colorMap.getInt("blue");
//			}
		}
		cssMain2 += "\tbackground-color: rgba(" + r + "," + g +"," + b + "," + a + ");\n";
		
		a = 0;
		if(style.containsKey("randZichtbaar") && ((Boolean) style.get("randZichtbaar")).booleanValue())
			a = 1;
		if (style.containsKey("randColor_red")) 
		{	r = ((Integer) style.get("randColor_red")).intValue();
			if (style.containsKey("randColor_green")) g = ((Integer) style.get("randColor_green")).intValue();
			if (style.containsKey("randColor_blue")) b = ((Integer) style.get("randColor_blue")).intValue();
		}
		else if(style.containsKey("randColor"))
		{
			Color randColor = (Color) style.get("randColor");
			r = randColor.getRed();
			g = randColor.getGreen();
			b = randColor.getBlue();
			
//			ObjectMap colorMap = style.getObjectMap("randColor");
//			if(colorMap != null) {
//				r = colorMap.getInt("red");
//				g = colorMap.getInt("green");
//				b = colorMap.getInt("blue");
//			}
		}
		String randColor = "rgba(" + r + "," + g +"," + b + ",";
		cssMain2 += "\tborder-color:" + randColor + a + ");\n";
		
		if(style.containsKey("randDikte")) 
		{	a = ((Integer) style.get("randDikte")).intValue();
			cssMain2 += "\tborder-width: " + a + "px;\n";
		}
		cssMain2 += "\tborder-style:solid;\n";
		
		if(style.containsKey("ronding")) 
		{	a = ((Integer) style.get("ronding")).intValue();
			double d = a/2;
			cssMain2 += "\tborder-radius:" + d + "px;\n";
		}
		
		if(style.containsKey("hoek")) 
		{	a = ((Integer) style.get("hoek")).intValue();
			cssMain2 += "\ttransform:rotate(" + a + "deg);\n";
			cssMain2 += "\t-webkit-transform:rotate(" + a + "deg);\n";
		}
		cssMain2 += "}\n\n";
		cssOutput += cssMain2;
		
		String cssBorder = "." + styleString + "-border {\n";
		cssBorder += "\tborder-style: solid;\n";
		a = 0;
		if(style.containsKey("tableBorders") && ((Boolean) style.get("tableBorders")).booleanValue())
			a = 1;
		cssBorder += "\tborder-color:" + randColor + a + ");\n";
		cssBorder += "}\n\n";
		cssOutput += cssBorder;
		
		//Possibly replace horizontalBorders and verticalBorders by borders of the grid itself.
		//For now, these borders do not end up in exactly the same positions, so I leave it for later.
		/*
		String cssMainCell = "." + styleString + "-main-cell {\n";
		
		cssMainCell += "border-collapse:collapse;\n";
		
		if(style.containsKey("tableBorders") && style.getBoolean("tableBorders"))
		{
			cssMainCell += "border-style:solid;\n";
			cssMainCell += "border-color:" + randColor + ";\n";
			cssMainCell += "border-width: 1px;\n";
		}
		else
		{
			cssMainCell += "border-style:none;\n";
		}
		cssMainCell += "}\n";
		
		System.out.println(cssMainCell);
		*/
		
//		String cssTekstVak = "." + styleString + "-tekstvak {\n";
//		cssTekstVak += "}\n";
//		System.out.println(cssTekstVak);
//		
		String cssTekstRegel = "." + styleString + "-tekstregel svg {\n";
		r = 0; g = 0; b = 0;
		if (style.containsKey("fgColor_red")) 
		{	r = ((Integer) style.get("fgColor_red")).intValue();
			if (style.containsKey("fgColor_green")) g = ((Integer) style.get("fgColor_green")).intValue();
			if (style.containsKey("fgColor_blue")) b = ((Integer) style.get("fgColor_blue")).intValue();
		}
		if(style.containsKey("anderFont") && ((Boolean) style.get("anderFont")).booleanValue() && style.containsKey("fgColor"))
		{
			Color fgColor = (Color) style.get("fgColor");
			r = fgColor.getRed();
			g = fgColor.getGreen();
			b = fgColor.getBlue();
//			ObjectMap colorMap = style.getObjectMap("fgColor");
//			if(colorMap != null) {
//				r = colorMap.getInt("red");
//				g = colorMap.getInt("green");
//				b = colorMap.getInt("blue");
//			}
		}
		cssTekstRegel += "\tfill: rgb(" + r + "," + g +"," + b + ");\n";
		cssTekstRegel += "\tstroke: rgb(" + r + "," + g +"," + b + ");\n";		
		
		cssTekstRegel += "}\n";
		cssOutput += cssTekstRegel;
		
		return cssOutput;
	}
}
