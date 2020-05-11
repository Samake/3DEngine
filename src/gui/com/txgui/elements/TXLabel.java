package com.txgui.elements;

import static org.lwjgl.nanovg.NanoVG.nvgBeginPath;
import static org.lwjgl.nanovg.NanoVG.nvgFillColor;
import static org.lwjgl.nanovg.NanoVG.nvgFontFace;
import static org.lwjgl.nanovg.NanoVG.nvgFontSize;
import static org.lwjgl.nanovg.NanoVG.nvgText;
import static org.lwjgl.nanovg.NanoVG.nvgTextAlign;

import com.txgui.core.TXElement;
import com.txgui.core.TXGUIManager;
import com.txgui.core.TXGUIManager.Align;
import com.txgui.core.TXUtils;

public class TXLabel extends TXElement {

	private String text;
	private int fontSize;
	private Align hAlign;
	private Align vAlign;
	
	public TXLabel(String text, float x, float y, float width, float height, TXElement parent) {
		setX(x);
		setY(y);
		setWidth(width);
		setHeight(height);
		setParent(parent);
		
		this.text = text;
		this.fontSize = 12;
		this.hAlign = Align.LEFT;
		this.vAlign = Align.MIDDLE;
		
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
			nvgFontSize(vg, fontSize);
	        nvgFontFace(vg, "default-bold");
	        nvgFillColor(vg, nvgShadowgColor);
	        
	        nvgTextAlign(vg, TXUtils.convertAlign(hAlign) | TXUtils.convertAlign(vAlign));
	
	        if (text != null) {
	        	nvgText(vg, getCurrentX() + 1, getCurrentY() + 1, text);
	        	
	        	nvgFillColor(vg, nvgForGroundColor);
	        	nvgText(vg, getCurrentX(), getCurrentY(), text);
	        }
	        
	        super.render(vg);
		}
	}
	
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public int getFontSize() {
		return fontSize;
	}

	public void setFontSize(int fontSize) {
		this.fontSize = fontSize;
	}
	
	public void setHAlign(Align hAlign) {
		this.hAlign = hAlign;
	}
	
	public Align getHAlign() {
		return hAlign;
	}

	public Align getVAlign() {
		return vAlign;
	}

	public void setVAlign(Align vAlign) {
		this.vAlign = vAlign;
	}

	public void terminate() {
		super.terminate();
		
		TXGUIManager.removeGUI(this);
	}
}
