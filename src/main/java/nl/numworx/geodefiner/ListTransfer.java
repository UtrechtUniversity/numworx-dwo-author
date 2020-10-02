package nl.numworx.geodefiner;

import javax.swing.JComponent;
import javax.swing.TransferHandler;

final class ListTransfer extends TransferHandler {
	ListTransfer(String property) {
		super(property);
	}

	@Override
	public int getSourceActions(JComponent c) {
		return COPY_OR_MOVE;
	}
}