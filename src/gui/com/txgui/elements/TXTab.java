package com.txgui.elements;

import com.txgui.core.TXElement;

public class TXTab extends TXElement {

	private String name;
	private boolean active = false;
	
	public TXTab(String name) {
		setName(name);
	}
	
	public void update(double delta) {
		if (getParent() != null) {
			setEnabled(getParent().isEnabled());

			if (isEnabled() && active) {
				super.update(delta);
				
				setContentX(getParent().getContentX());
				setContentY(getParent().getContentY());
				setContentWidth(getParent().getContentWidth());
				setContentHeight(getParent().getContentHeight());
			}
		}
	}
	
	public void render(long vg) {
		if (isEnabled() && active) {
			super.render(vg);
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void addContent(TXElement content) {
		content.setParent(this);
		addChild(content);
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public void terminate() {
		super.terminate();
	}
}
