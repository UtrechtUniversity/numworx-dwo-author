package fi.euclides.gwt;

public interface MouseConsumer {
  void processMouseDown(int x, int y, int id);
  
  default void processMouseDown(MouseContext ctx) {
    processMouseDown(ctx.getX(), ctx.getY(), ctx.getID());
  }

  void processMouseUp(int x, int y, int id);
  
  default void processMouseUp(MouseContext ctx) {
    processMouseUp(ctx.getX(),ctx.getY(), ctx.getID());
  }
  

  void processMouseDrag(int x, int y, int id);
  
  default void processMouseDrag(MouseContext ctx) {
    processMouseDrag(ctx.getX(), ctx.getY(), ctx.getID());
  }

}
