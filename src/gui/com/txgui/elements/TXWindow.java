package com.txgui.elements;

import static org.lwjgl.nanovg.NanoVG.nvgBeginPath;
import static org.lwjgl.nanovg.NanoVG.nvgFill;
import static org.lwjgl.nanovg.NanoVG.nvgFillColor;
import static org.lwjgl.nanovg.NanoVG.nvgFontFace;
import static org.lwjgl.nanovg.NanoVG.nvgFontSize;
import static org.lwjgl.nanovg.NanoVG.nvgRect;
import static org.lwjgl.nanovg.NanoVG.nvgText;
import static org.lwjgl.nanovg.NanoVG.nvgTextAlign;

import com.txgui.core.TXElement;
import com.txgui.core.TXGUIManager;
import com.txgui.core.TXGUIManager.Align;
import com.txgui.core.TXUtils;

public class TXWindow extends TXElement {

	private String title;
	private int fontSize = 14;
	private float titleBarHeight = 25;
	
	public TXWindow(float x, float y, float width, float height, TXElement parent) {
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
			setContentY(getCurrentY() + titleBarHeight);
			setContentWidth(getCurrentWidth());
			setContentHeight(getCurrentHeight() - titleBarHeight);
		}
	}

	public void render(long vg) {
		if (isEnabled()) {
			renderBG(vg);
			renderBorder(vg);
			renderTitleBar(vg);
			renderButton(vg);
			
			super.render(vg);
		}
	}

	private void renderBG(long vg) {
		nvgBeginPath(vg);
		nvgRect(vg, getCurrentX(), getCurrentY(), getCurrentWidth(), getCurrentHeight());
		nvgFillColor(vg, nvgBackGroundColor);
		nvgFill(vg);
	}
	
	private void renderBorder(long vg) {
		
	}
	
	private void renderTitleBar(long vg) {
		nvgBeginPath(vg);
		nvgRect(vg, getCurrentX(), getCurrentY(), getCurrentWidth(), titleBarHeight);
		nvgFillColor(vg, nvgHighLightColor1);
		nvgFill(vg);
		
		nvgBeginPath(vg);
		nvgFontSize(vg, fontSize);
        nvgFontFace(vg, "default-bold");
        nvgFillColor(vg, nvgShadowgColor);
        
        nvgTextAlign(vg, TXUtils.convertAlign(Align.CENTER) | TXUtils.convertAlign(Align.MIDDLE));

        if (title != null) {
        	nvgText(vg, getCurrentX() + (getCurrentWidth() * 0.5f) + 1, getCurrentY() + (titleBarHeight * 0.5f) + 1, title);
        	
        	nvgFillColor(vg, nvgForGroundColor);
        	nvgText(vg, getCurrentX() + (getCurrentWidth() * 0.5f), getCurrentY() + (titleBarHeight * 0.5f), title);
        }
	}
	
	private void renderButton(long vg) {
		
	}
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getFontSize() {
		return fontSize;
	}

	public void setFontSize(int fontSize) {
		this.fontSize = fontSize;
	}

	public float getTitleBarHeight() {
		return titleBarHeight;
	}

	public void setTitleBarHeight(float titleBarHeight) {
		this.titleBarHeight = titleBarHeight;
	}

	public void terminate() {
		super.terminate();
		
		TXGUIManager.removeGUI(this);
	}
}
