package fi.euclides.gwt;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

import com.vaadin.pointerevents.client.PointerCancelEvent;
import com.vaadin.pointerevents.client.PointerCancelHandler;
import com.vaadin.pointerevents.client.PointerDownEvent;
import com.vaadin.pointerevents.client.PointerDownHandler;
import com.vaadin.pointerevents.client.PointerEvent;
import com.vaadin.pointerevents.client.PointerMoveEvent;
import com.vaadin.pointerevents.client.PointerMoveHandler;
import com.vaadin.pointerevents.client.PointerUpEvent;
import com.vaadin.pointerevents.client.PointerUpHandler;

public class GWTPointerHandler
		implements PointerCancelHandler, PointerDownHandler, PointerMoveHandler, PointerUpHandler {

	final private static Logger LOG = Logger.getLogger("GWTPointerHandler");

	final MouseConsumer viewer;
	final Set<Integer> bitset = new HashSet<>();

	private static class CTX implements MouseContext {

		final PointerEvent<?> ev;
		final long stamp;
		
		private CTX(PointerEvent<?> ev) {
			this.ev = ev;
	        stamp = System.currentTimeMillis();
		}

		@Override
		public int getID() {
			return ev.getPointerId();
		}

		@Override
		public int getX() {
			return ev.getX();
		}

		@Override
		public int getY() {
			return ev.getClientY();
		}

		@Override
		public int getScreenX() {
			return ev.getScreenX();
		}

		@Override
		public int getScreenY() {
			return ev.getScreenY();
		}

		@Override
		public int getClientX() {
			return ev.getClientX();
		}

		@Override
		public int getClientY() {
			return ev.getClientY();
		}

		@Override
		public long getTimestamp() {
			return stamp;
		}
	
	}
	
	public GWTPointerHandler(MouseConsumer viewer) {
		this.viewer = viewer;
	}

	@Override
	public void onPointerUp(PointerUpEvent event) {
		LOG.info("up " + event+ " " + bitset);
		CTX ctx = new CTX(event);
		viewer.processMouseUp(ctx);
		bitset.remove(event.getPointerId());
	}

	@Override
	public void onPointerMove(PointerMoveEvent event) {
		LOG.info("move " + event + " " + bitset);
		CTX ctx = new CTX(event);
		if (bitset.contains(event.getPointerId()))
				viewer.processMouseDrag(ctx);
	}

	@Override
	public void onPointerDown(PointerDownEvent event) {
		LOG.info("down " + event + " " + bitset);
		CTX ctx = new CTX(event);
		bitset.add(event.getPointerId());
		viewer.processMouseDown(ctx);
	}

	@Override
	public void onPointerCancel(PointerCancelEvent event) {
		LOG.info("cancel " + event + " " + bitset);
		bitset.remove(event.getPointerId());
	}

}
