package fi.euclides.gwt;

public interface MouseConsumer {
  void processMouseDown(int x, int y, int id);

  void processMouseUp(int x, int y, int id);

  void processMouseDrag(int x, int y, int id);

}
