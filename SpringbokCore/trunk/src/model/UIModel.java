package model;

import event.IModelUpdateListener;

public class UIModel extends GenericModel implements IModelUpdateListener {

	private UIMode uiMode = UIMode.SQL;
	private Alias alias = null;

	public UIMode getUiMode() {
		return uiMode;
	}

	public void setUiMode(UIMode uiMode) {
		this.uiMode = uiMode;
		notifyUpdate(uiMode);
	}

	public Alias getAlias() {
		return alias;
	}

	public void setAlias(Alias alias) {
		this.alias = alias;
		notifyUpdate(alias);
	}

	@Override
	public void modelChanged(Object change) {
		notifyUpdate(change);
	}

}
