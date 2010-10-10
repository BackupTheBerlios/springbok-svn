package model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import event.IModelUpdateListener;

public class GenericModel implements IGenericModel {
	private List<IModelUpdateListener> listeners = new ArrayList<IModelUpdateListener>();

	private boolean eventsSuppressed = false;

	/**
	 * addUpdateListener
	 * 
	 * @see com.enactor.tools.core.model.IGenericModel#addUpdateListener(com.enactor.tools.core.model.tree.IModelUpdateListener)
	 */
	public void addUpdateListener(IModelUpdateListener listener) {
		if (listener == null)
			throw new NullPointerException(
					"Model Update listener cannot be null");
		listeners.add(listener);
	}

	/**
	 * removeUpdateListener
	 * 
	 * @see com.enactor.tools.core.model.IGenericModel#removeUpdateListener(com.enactor.tools.core.model.tree.IModelUpdateListener)
	 */
	public void removeUpdateListener(IModelUpdateListener listener) {
		listeners.remove(listener);
	}

	protected void notifyUpdate() {
		if (eventsSuppressed)
			return;

		for (IModelUpdateListener listener : listeners) {
			listener.modelChanged(""); //$NON-NLS-1$
		}
	}

	protected void notifyUpdate(Object change) {
		if (eventsSuppressed)
			return;

		// We create new list and iterate over it to prevent concurrent
		// modifications down in the listener hierarchy
		for (IModelUpdateListener listener : new ArrayList<IModelUpdateListener>(
				listeners)) {
			listener.modelChanged(change);
		}
	}

	/**
	 * @return the listeners
	 */
	public Collection<IModelUpdateListener> getListeners() {
		return Collections.unmodifiableList(listeners);
	}

	/**
	 * Returns whether the update events are suppressed or not.
	 * 
	 * @return
	 */
	public boolean isEventsSuppressed() {
		return eventsSuppressed;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setEventsSuppressed(boolean eventsSuppressed) {
		this.eventsSuppressed = eventsSuppressed;
	}
}
