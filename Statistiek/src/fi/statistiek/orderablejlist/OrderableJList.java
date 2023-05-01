package fi.statistiek.orderablejlist;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragGestureRecognizer;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceEvent;
import java.awt.dnd.DragSourceListener;
import java.util.ArrayList;

import javax.swing.DropMode;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.TransferHandler;

/**
 * 
 * Extended JList that is orderable by drag&drop
 * @author Manu Drijvers
 *
 */
public class OrderableJList extends JList {
	private MyDragListener dragListener;
	
	/**
	 * Constructor
	 * @param model List model
	 */
	public OrderableJList(OrderableJListModel model) {
		super(model);
		
		this.dragListener = new MyDragListener(this);
		
		super.setDragEnabled(true);
		super.setDropMode(DropMode.INSERT);
		super.setTransferHandler(new MyTransferHandler(this, this.dragListener));
		
	}
	
	/**
	 * 
	 * Class for handling transferring data
	 * @author Manu Drijvers
	 * 
	 */
	private class MyTransferHandler extends TransferHandler {
		private JList list;
		private MyDragListener dragListener;
		
		/**
		 * Constructor
		 * @param list The reorderable list
		 * @param dragListener draglistener that gives information about mouse actions
		 */
		public MyTransferHandler(JList list, MyDragListener dragListener) {
			this.list = list;
			this.dragListener = dragListener;
		}
		
		/**
		 * Override canImport
		 * Determine whether data can be imported or not
		 */
		public boolean canImport(TransferHandler.TransferSupport support) {
			Transferable t = support.getTransferable();
			try {
				//try to interpret the data as string,
				//and check whether the dropped string was in the list already to make sure you can only reorder elements
				return t.getTransferData(DataFlavor.stringFlavor).equals(this.list.getModel().getElementAt(this.dragListener.getDraggedObjectIndex()).toString());
			} catch (Exception e) {
				return false;
			}
		}
		
		/**
		 * override importData
		 * Adds dropped data to the list
		 */
		public boolean importData(TransferHandler.TransferSupport support) {
			//find new index
			JList.DropLocation dl = (JList.DropLocation) support.getDropLocation();
		    int dropTargetIndex = dl.getIndex();
		    
		    //find old index
		    int oldIndex = this.dragListener.getDraggedObjectIndex();
		    
		    //reorder list
			((OrderableJListModel)this.list.getModel()).reorder(oldIndex, dropTargetIndex);
			if(oldIndex < dropTargetIndex) {
				dropTargetIndex--;
			}
			
			//set the selection to the dropped item
			this.list.setSelectedIndex(dropTargetIndex);
			
			return true;
		}
	}
	
	/**
	 * Class handling the mouse input
	 * @author Manu Drijvers
	 *
	 */
	private class MyDragListener implements DragGestureListener, DragSourceListener  {
		private DragSource dragSource;
		private DragGestureRecognizer dgr;
		
		private int draggedObjectIndex;
		private JList list;
		
		private MyDragListener(JList list) {
			this.list = list;
			this.dragSource = new DragSource();
			this.dragSource.addDragSourceListener(this);
			this.dgr = this.dragSource.createDefaultDragGestureRecognizer(list, DnDConstants.ACTION_MOVE, this);
		}
		
		public void dragDropEnd(DragSourceDropEvent arg0) {
			
		}


		public void dragEnter(DragSourceDragEvent arg0) {
			
		}


		public void dragExit(DragSourceEvent arg0) {
			
		}


		public void dragOver(DragSourceDragEvent arg0) {
			
		}


		public void dropActionChanged(DragSourceDragEvent arg0) {
			
		}
		
		public void dragGestureRecognized(DragGestureEvent arg0) {
			this.draggedObjectIndex = list.locationToIndex(arg0.getDragOrigin());
			StringSelection transferable = new StringSelection(list.getModel().getElementAt(draggedObjectIndex).toString());
			this.dragSource.startDrag(arg0, DragSource.DefaultMoveDrop, transferable, this);
		}
		
		/**
		 * @return The index of the object that's being dragged
		 */
		public int getDraggedObjectIndex() {
			return this.draggedObjectIndex;
		}
	}
	
	
}
