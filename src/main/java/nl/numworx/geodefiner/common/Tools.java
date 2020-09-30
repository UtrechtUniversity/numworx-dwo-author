package nl.numworx.geodefiner.common;

public interface Tools {
	int SELECTOR = 0;
	int POINT = 1;

	int LINE = 2;
	int HALFLINE = 3;
	int SEGMENT = 4;
	
	int PERPENDICULAR = 5;
	int PARALLEL = 6;
	
	int CIRCLE = 7;
	int ARC = 8;
	int TRIANGLE = 9;
	
	int MIDPOINT = 10;
	int BISECTRICE = 11;
	int MIRROR = 12;
	
	int CONIC_SECTION = 13;
	int FOCUS = 14;
	
	int TANGENT = 15;
	int POLELINE = 16;
	
	int LOCUS = 17;
	
	int DISTANCE = 18;
	int AREA = 19;
	int ANGLE = 20;
	int VECTOR = 21;
	
	int TRAIL = 22;
	int TEXT  = 23;
	int FORMULA = 24;

	int PAN = 25;
	int DESTROY = 26;
	int RESET = 27;
	
	int CIRCLE_WITH_RADIUS = 28;
	int COLOR_PALETTE = 29;
	int LINE_PALETTE = 30;
	
	int ANGLE_POINT = 31;
// NOT YET
	int UNDO = 32;
	int REDO = 33;
		
	int TOOL_SIZE = 32;
}
