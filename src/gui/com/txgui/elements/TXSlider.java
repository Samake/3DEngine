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

public class TXSlider extends TXElement {

	private String title = "UNKNOWN";
	private int fontSize = 12;
	private float minValue = 0.0f;
	private float maxValue = 1.0f;
	private float value = 0.0f;
	private int precision = 2;
	
	private float sliderX;
	
	public TXSlider(String title, float x, float y, float width, float height, TXElement parent) {
		setX(x);
		setY(y);
		setWidth(width);
		setHeight(height);
		setParent(parent);
		
		this.title = title;
		
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
			
			float minX = getCurrentX();
			float maxX = getCurrentX() + getCurrentWidth() - (getCurrentHeight() * 0.5f);
			float range = maxX - minX;
			float valRange = maxValue - minValue;
			
			sliderX = getCurrentX() + ((range / valRange) * (value - minValue));
			
			if (sliderX < minX) {
				sliderX = minX;
			}
			
			if (sliderX > maxX) {
				sliderX = maxX;
			}
			
			if (value < minValue) {
				value = minValue;
			}
			
			if (value > maxValue) {
				value = maxValue;
			}
			
			if (isCursorInside()) {
//				if (ClickSystem.isMouseLeftHold()) {
//					Vector2f cursorPos = Engine.getInput().getCursorPosition();
//					
//					if (cursorPos.getX() >= minX && cursorPos.getX() <= maxX) {
//						sliderX = cursorPos.getX();
//						
//						value = minValue + ((valRange / range) * (sliderX - getCurrentX()));
//					}
//				}
			}
		}
	}
	
	public void render(long vg) {
		if (isEnabled()) {
			float halfHeight = getCurrentHeight() * 0.5f;
			float part1Y = getCurrentY();
			float part2Y = getCurrentY() + halfHeight;

			renderBackGround(vg, getCurrentX(), part2Y, getCurrentWidth(), halfHeight);
			renderBar(vg, getCurrentX(), part2Y, getCurrentWidth(), halfHeight);
			renderSlider(vg, getCurrentX(), part2Y, getCurrentWidth(), halfHeight);
			renderText(vg, getCurrentX(), part1Y, getCurrentWidth(), halfHeight);
			
			super.render(vg);
		}
	}

	private void renderBackGround(long vg, float x, float y, float width, float height) {
		nvgBeginPath(vg);
		nvgRect(vg, x, y, width, height);
		nvgFillColor(vg, nvgHighLightColor1);
		nvgFill(vg);
	}
	
	private void renderBar(long vg, float x, float y, float width, float height) {
		float Width;
		
		if (sliderX > x) {
			Width = sliderX - x;
		} else {
			Width = 0;
		}
		
		if (Width > 0) {
			nvgBeginPath(vg);
			nvgRect(vg, x, y, Width, height);
			nvgFillColor(vg, nvgHighLightColor2);
			nvgFill(vg);
		}
	}
	
	private void renderSlider(long vg, float x, float y, float width, float height) {
		nvgBeginPath(vg);
		nvgRect(vg, sliderX, y, height, height);
		nvgFillColor(vg, nvgHighLightColor3);
		nvgFill(vg);
	}
	
	private void renderText(long vg, float x, float y, float width, float height) {
		nvgBeginPath(vg);
		nvgFontSize(vg, fontSize);
        nvgFontFace(vg, "default-bold");
        nvgFillColor(vg, nvgShadowgColor);
        
        nvgTextAlign(vg, TXUtils.convertAlign(Align.LEFT) | TXUtils.convertAlign(Align.MIDDLE));
        nvgText(vg, x + 1, y + 1, String.format("%.0f", minValue));

        nvgTextAlign(vg, TXUtils.convertAlign(Align.CENTER) | TXUtils.convertAlign(Align.MIDDLE));
    	nvgText(vg, x + (width * 0.5f) + 1, y + 1, title + ": " + String.format("%." + precision + "f", value));
    	
    	nvgTextAlign(vg, TXUtils.convertAlign(Align.RIGHT) | TXUtils.convertAlign(Align.MIDDLE));
        nvgText(vg, x + width + 1, y + 1, String.format("%.0f", maxValue));
    	
    	nvgFillColor(vg, nvgForGroundColor);
    	
        nvgTextAlign(vg, TXUtils.convertAlign(Align.LEFT) | TXUtils.convertAlign(Align.MIDDLE));
        nvgText(vg, x, y, String.format("%.0f", minValue));

        nvgTextAlign(vg, TXUtils.convertAlign(Align.CENTER) | TXUtils.convertAlign(Align.MIDDLE));
    	nvgText(vg, x + (width * 0.5f), y, title + ": " + String.format("%." + precision + "f", value));
    	
    	nvgTextAlign(vg, TXUtils.convertAlign(Align.RIGHT) | TXUtils.convertAlign(Align.MIDDLE));
        nvgText(vg, x + width, y, String.format("%.0f", maxValue));
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

	public float getMinValue() {
		return minValue;
	}

	public void setMinValue(float minValue) {
		this.minValue = minValue;
	}

	public float getMaxValue() {
		return maxValue;
	}

	public void setMaxValue(float maxValue) {
		this.maxValue = maxValue;
	}

	public float getValue() {
		return value;
	}

	public void setValue(float value) {
		this.value = value;
	}

	public int getPrecision() {
		return precision;
	}

	public void setPrecision(int precision) {
		this.precision = precision;
	}

	public void terminate() {
		super.terminate();
		
		TXGUIManager.removeGUI(this);
	}
}