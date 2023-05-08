package fi.euclides.model;

import fi.euclides.model.Segment;
import fi.euclides.model.math.Numbers;

public interface SegmentVisitor {
	public void visitSegment(Segment s);
// rectangle of allowable segments
	public Numbers clipTop();
	public Numbers clipBottom();
	public Numbers clipLeft();
	public Numbers clipRight();
}
