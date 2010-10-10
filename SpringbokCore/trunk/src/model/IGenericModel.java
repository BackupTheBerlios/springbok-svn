package model;

import event.IModelUpdateListener;

public interface IGenericModel {
	/**
	 * Adds the given update listener.
	 * 
	 * @param listener
	 */
	public void addUpdateListener(IModelUpdateListener listener);

	/**
	 * Removes the given update listener.
	 * This call has no effect if the given listener is not already present in the model.
	 * 
	 * @param listener
	 */
	public void removeUpdateListener(IModelUpdateListener listener);
	
	/**
	 * Sets the flag indicating the model update events firing should be stopped.
	 * 
	 * @param eventsSuppressed
	 */
	public void setEventsSuppressed(boolean eventsSuppressed);

}
