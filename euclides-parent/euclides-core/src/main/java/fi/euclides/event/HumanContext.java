package fi.euclides.event;

public interface HumanContext {
	boolean isShiftDown();
	boolean isControlDown();
	long getTimestamp();
}
