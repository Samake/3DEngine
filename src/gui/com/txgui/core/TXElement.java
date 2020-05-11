package com.txgui.core;

import java.util.ArrayList;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.nanovg.NVGColor;

import com.txgui.events.TXGUIEventHandler;

import samake.engine.config.PropertiesHandler;
import samake.engine.core.Engine;

public class TXElement extends TXGUIEventHandler {
	
	protected int screenWidth = PropertiesHandler.getWindowWidth();
	protected int screenHeight = PropertiesHandler.getWindowHeight();
	
	protected TXElement parent;
	protected float x = 0.0f;
	protected float y = 0.0f;
	protected float width = 0.0f;
	protected float height = 0.0f;
	protected float currentX = 0.0f;
	protected float currentY = 0.0f;
	protected float currentWidth = 0.0f;
	protected float currentHeight = 0.0f;
	protected float contentX = 0.0f;
	protected float contentY = 0.0f;
	protected float contentWidth = 0.0f;
	protected float contentHeight = 0.0f;
	protected float alpha = 255.0f;
	protected boolean enabled = true;
	
	protected Vector3f backGroundColor = TXGUIManager.getStyle().getBackGroundColor();
	protected NVGColor nvgBackGroundColor = NVGColor.create();
	protected Vector3f forGroundColor = TXGUIManager.getStyle().getForGroundColor();
	protected NVGColor nvgForGroundColor = NVGColor.create();
	protected Vector3f shadowColor = TXGUIManager.getStyle().getShadowColor();
	protected NVGColor nvgShadowgColor = NVGColor.create();
	protected Vector3f highLightColor1 = TXGUIManager.getStyle().getHighLightColor1();
	protected NVGColor nvgHighLightColor1 = NVGColor.create();
	protected Vector3f highLightColor2 = TXGUIManager.getStyle().getHighLightColor2();
	protected NVGColor nvgHighLightColor2 = NVGColor.create();
	protected Vector3f highLightColor3 = TXGUIManager.getStyle().getHighLightColor3();
	protected NVGColor nvgHighLightColor3 = NVGColor.create();

	protected ArrayList<TXElement> childs = new ArrayList<TXElement>();
	
	public void update(double delta) {
		if (parent != null) {
			setEnabled(getParent().isEnabled());
			alpha = parent.alpha;
		}
		
		if (isEnabled()) {
			if (parent == null) {
				setCurrentWidth(width * screenWidth);
				setCurrentHeight(height * screenHeight);
				setCurrentX(x * screenWidth);
				setCurrentY(y * screenHeight);
			} else {
				setCurrentWidth(width * parent.contentWidth);
				setCurrentHeight(height * parent.contentHeight);
				setCurrentX(parent.contentX + x * parent.contentWidth);
				setCurrentY(parent.contentY + y * parent.contentHeight);
			}
			
			nvgBackGroundColor.r(backGroundColor.x / 255.0f);
			nvgBackGroundColor.g(backGroundColor.y / 255.0f);
			nvgBackGroundColor.b(backGroundColor.z / 255.0f);
			nvgBackGroundColor.a(alpha / 255.0f);
			
			nvgForGroundColor.r(forGroundColor.x / 255.0f);
			nvgForGroundColor.g(forGroundColor.y / 255.0f);
			nvgForGroundColor.b(forGroundColor.z / 255.0f);
			nvgForGroundColor.a(alpha / 255.0f);
			
			nvgShadowgColor.r(shadowColor.x / 255.0f);
			nvgShadowgColor.g(shadowColor.y / 255.0f);
			nvgShadowgColor.b(shadowColor.z / 255.0f);
			nvgShadowgColor.a(alpha / 255.0f);
			
			nvgHighLightColor1.r(highLightColor1.x / 255.0f);
			nvgHighLightColor1.g(highLightColor1.y / 255.0f);
			nvgHighLightColor1.b(highLightColor1.z / 255.0f);
			nvgHighLightColor1.a(alpha / 255.0f);
			
			nvgHighLightColor2.r(highLightColor2.x / 255.0f);
			nvgHighLightColor2.g(highLightColor2.y / 255.0f);
			nvgHighLightColor2.b(highLightColor2.z / 255.0f);
			nvgHighLightColor2.a(alpha / 255.0f);
			
			nvgHighLightColor3.r(highLightColor3.x / 255.0f);
			nvgHighLightColor3.g(highLightColor3.y / 255.0f);
			nvgHighLightColor3.b(highLightColor3.z / 255.0f);
			nvgHighLightColor3.a(alpha / 255.0f);
			
			if (!childs.isEmpty()) {
				for (TXElement child : childs) {
					if (child != null) {
						child.update(delta);
					}
				}
			}
		}
	}
	
	public void render(long vg) {
		if (isEnabled()) {
			if (!childs.isEmpty()) {
				for (TXElement child : childs) {
					if (child != null) {
						child.render(vg);
					}
				}
			}
		}
	}
	
	public boolean isCursorInside() {
		return isCursorInside(getCurrentX(), getCurrentY(), getCurrentWidth(), getCurrentHeight());
	}
	
	public boolean isCursorInside(float x, float y, float width, float height) {
		Vector2f cursorPos = Engine.instance.getInput().getCursorPosition();
		
		if (cursorPos.x >= x && cursorPos.x <= x + width) {
			if (cursorPos.y >= y && cursorPos.y <= y + height) {
				TXGUIManager.setInteractWithGUI(true);
				return true;
			}
		}
		
		return false;
	}
	
	public int getScreenWidth() {
		return screenWidth;
	}
	
	public void setScreenWidth(int screenWidth) {
		this.screenWidth = screenWidth;
	}
	
	public int getScreenHeight() {
		return screenHeight;
	}
	
	public TXElement getParent() {
		return parent;
	}
	
	public void setParent(TXElement parent) {	
		if (parent != null) {
			parent.addChild(this);
		}
		
		this.parent = parent;
	}
	
	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public float getWidth() {
		return width;
	}

	public void setWidth(float width) {
		this.width = width;
	}

	public float getHeight() {
		return height;
	}

	public void setHeight(float height) {
		this.height = height;
	}
	
	public float getCurrentX() {
		return currentX;
	}
	
	public void setCurrentX(float currentX) {
		this.currentX = currentX;
	}
	
	public float getCurrentY() {
		return currentY;
	}
	
	public void setCurrentY(float currentY) {
		this.currentY = currentY;
	}
	
	public float getCurrentWidth() {
		return currentWidth;
	}
	
	public void setCurrentWidth(float currentWidth) {
		this.currentWidth = currentWidth;
	}
	
	public float getCurrentHeight() {
		return currentHeight;
	}
	
	public void setCurrentHeight(float currentHeight) {
		this.currentHeight = currentHeight;
	}
	
	public float getContentX() {
		return contentX;
	}
	
	public void setContentX(float contentX) {
		this.contentX = contentX;
	}
	
	public float getContentY() {
		return contentY;
	}
	
	public void setContentY(float contentY) {
		this.contentY = contentY;
	}
	
	public float getContentWidth() {
		return contentWidth;
	}
	
	public void setContentWidth(float contentWidth) {
		this.contentWidth = contentWidth;
	}
	
	public float getContentHeight() {
		return contentHeight;
	}
	
	public void setContentHeight(float contentHeight) {
		this.contentHeight = contentHeight;
	}
	
	public void setScreenHeight(int screenHeight) {
		this.screenHeight = screenHeight;
	}
	
	public float getAlpha() {
		return alpha;
	}
	
	public void setAlpha(float alpha) {
		this.alpha = alpha;
	}
	
	public boolean isEnabled() {
		return enabled;
	}
	
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
	public ArrayList<TXElement> getChilds() {
		return childs;
	}
	
	public void setChilds(ArrayList<TXElement> childs) {
		this.childs = childs;
	}
	
	public void addChild(TXElement child) {
		childs.add(child);
		TXGUIManager.removeGUI(child);
	}

	public void removeChild(TXElement child) {
		childs.remove(child);
	}
	
	public Vector3f getBackGroundColor() {
		return backGroundColor;
	}

	public void setBackGroundColor(Vector3f backGroundColor) {
		this.backGroundColor = backGroundColor;
	}

	public Vector3f getForGroundColor() {
		return forGroundColor;
	}

	public void setForGroundColor(Vector3f forGroundColor) {
		this.forGroundColor = forGroundColor;
	}

	public Vector3f getShadowColor() {
		return shadowColor;
	}

	public void setShadowColor(Vector3f shadowColor) {
		this.shadowColor = shadowColor;
	}

	public Vector3f getHighLightColor1() {
		return highLightColor1;
	}

	public void setHighLightColor1(Vector3f highLightColor1) {
		this.highLightColor1 = highLightColor1;
	}

	public Vector3f getHighLightColor2() {
		return highLightColor2;
	}

	public void setHighLightColor2(Vector3f highLightColor2) {
		this.highLightColor2 = highLightColor2;
	}

	public Vector3f getHighLightColor3() {
		return highLightColor3;
	}

	public void setHighLightColor3(Vector3f highLightColor3) {
		this.highLightColor3 = highLightColor3;
	}

	public void terminate() {
		if (!childs.isEmpty()) {
			for (TXElement child : childs) {
				if (child != null) {
					child.terminate();
				}
			}
			
			childs.clear();
		}
	}
}
