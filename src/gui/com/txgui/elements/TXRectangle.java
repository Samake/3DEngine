package com.txgui.elements;

import static org.lwjgl.nanovg.NanoVG.nvgBeginPath;
import static org.lwjgl.nanovg.NanoVG.nvgFill;
import static org.lwjgl.nanovg.NanoVG.nvgFillColor;
import static org.lwjgl.nanovg.NanoVG.nvgRect;

import com.txgui.core.TXElement;
import com.txgui.core.TXGUIManager;

public class TXRectangle extends TXElement {

	public TXRectangle(float x, float y, float width, float height, TXElement parent) {
		setX(x);
		setY(y);
		setWidth(width);
		setHeight(height);
		setParent(parent);
		
		if (parent == null) {
			TXGUIManager.addGUI(this);
		}
	}

	public void update(double delta) {
		super.update(delta);
		
		if (isEnabled()) {
			setContentX(getCurrentX());
			setContentY(getCurrentY());
			setContentWidth(getCurrentWidth());
			setContentHeight(getCurrentHeight());
		}
	}

	public void render(long vg) {
		if (isEnabled()) {
			nvgBeginPath(vg);
			nvgRect(vg, getCurrentX(), getCurrentY(), getCurrentWidth(), getCurrentHeight());
			nvgFillColor(vg, nvgBackGroundColor);
			nvgFill(vg);
			
			super.render(vg);
		}
	}

	public void terminate() {
		super.terminate();
		
		TXGUIManager.removeGUI(this);
	}
}
