package com.txgui.elements;

import static org.lwjgl.nanovg.NanoVG.nvgBeginPath;
import static org.lwjgl.nanovg.NanoVG.nvgFill;
import static org.lwjgl.nanovg.NanoVG.nvgFillColor;
import static org.lwjgl.nanovg.NanoVG.nvgFontFace;
import static org.lwjgl.nanovg.NanoVG.nvgFontSize;
import static org.lwjgl.nanovg.NanoVG.nvgRect;
import static org.lwjgl.nanovg.NanoVG.nvgText;
import static org.lwjgl.nanovg.NanoVG.nvgTextAlign;

import java.util.HashMap;
import java.util.Map.Entry;

import com.txgui.core.TXElement;
import com.txgui.core.TXGUIManager;
import com.txgui.core.TXGUIManager.Align;
import com.txgui.core.TXUtils;

public class TXTabMenu extends TXElement {

	private HashMap<Integer, TXTab> tabs = new HashMap<Integer, TXTab>();
	private float tabBarHeight = 35;
	private float tabWidth = 0;
	private int fontSize = 12;
	private TXTab activeTab;
	
	public TXTabMenu(float x, float y, float width, float height, TXElement parent) {
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
			setContentY(getCurrentY() + tabBarHeight);
			setContentWidth(getCurrentWidth());
			setContentHeight(getCurrentHeight() - tabBarHeight);
			
			tabWidth = getCurrentWidth() / tabs.size();

			if (activeTab != null) {
				activeTab.update(delta);
			}
		}
	}

	public void render(long vg) {
		if (isEnabled()) {
			renderTabList(vg);

			if (activeTab != null) {
				activeTab.render(vg);
			}
			
			super.render(vg);
		}
	}
	
	private void renderTabList(long vg) {
		renderBackground(vg);
		renderTabs(vg);
	}

	private void renderBackground(long vg) {
		nvgBeginPath(vg);
		nvgRect(vg, getCurrentX(), getCurrentY(), getCurrentWidth(), tabBarHeight);
		nvgFillColor(vg, nvgBackGroundColor);
		nvgFill(vg);
	}
	
	private void renderTabs(long vg) {
		int id = 0;
		
		for (Entry<Integer, TXTab> entry : tabs.entrySet()) {
			if (entry != null) {
				float x = getCurrentX() + 1 + tabWidth * id;
				float y = getCurrentY() + 1;
				float w = tabWidth - 2;
				float h = tabBarHeight - 2;
				
				renderTab(vg, entry.getValue(), x, y , w, h);
				
				id++;
			}
		}
	}
	
	private void renderTab(long vg, TXTab guiTab, float x, float y, float w, float h) {
		if (guiTab != null) {
			renderTabBG(vg, guiTab, x, y, w, h);
			renderTabBorder(vg, x, y, w, h);
			renderTabText(vg, guiTab, x, y, w, h);
		}
	}

	private void renderTabBG(long vg, TXTab guiTab, float x, float y, float w, float h) {
		nvgBeginPath(vg);
		nvgRect(vg, x, y, w, h);
		
		if (activeTab != null) {
			if (activeTab.equals(guiTab)) {
				nvgFillColor(vg, nvgHighLightColor1);
			} else {
				nvgFillColor(vg, nvgBackGroundColor);
			}
		} else {
			nvgFillColor(vg, nvgBackGroundColor);
		}
		
		if (isCursorInside(x, y, w, h)) {
			nvgFillColor(vg, nvgHighLightColor2);
			
//			if (ClickSystem.isMouseLeftHold()) {
//				nvgFillColor(vg, nvgHighLightColor3);
//				
//				if (ClickSystem.isMouseLeftClicked()) {
//					setActiveTab(guiTab);
//				}
//			}
		}
		
		nvgFill(vg);
	}
	
	private void renderTabBorder(long vg, float x, float y, float w, float h) {
//		nvgBeginPath(vg);
//		
//		/** upper line */
//		nvgMoveTo(vg, x, y);
//		nvgLineTo(vg, x + w, y);
//		/** right line */
//		nvgMoveTo(vg, x + w, y);
//		nvgLineTo(vg, x + w, y + h);
//		/** bottom line */
//		nvgMoveTo(vg, x + w, y + h);
//		nvgLineTo(vg, x, y + h);
//		/** left line */
//		nvgMoveTo(vg, x, y + h);
//		nvgLineTo(vg, x, y);
//		
//		nvgFillColor(vg, nvgHighLightColor2);
//		nvgFill(vg);
	}
	
	private void renderTabText(long vg, TXTab guiTab, float x, float y, float w, float h) {
		nvgBeginPath(vg);
		nvgFontSize(vg, fontSize);
		nvgFontFace(vg, "default-bold");
		nvgFillColor(vg, nvgShadowgColor);
		
		nvgTextAlign(vg, TXUtils.convertAlign(Align.CENTER) | TXUtils.convertAlign(Align.MIDDLE));

		if (guiTab.getName() != null) {
			nvgText(vg, x + (w * 0.5f) + 1, y + (h * 0.5f) + 1, guiTab.getName());
			
			nvgFillColor(vg, nvgForGroundColor);
			nvgText(vg, x + (w * 0.5f), y + (h * 0.5f), guiTab.getName());
		}
	}

	public void setActiveTab(TXTab tab)  {
		for (Entry<Integer, TXTab> entry : tabs.entrySet()) {
			if (entry != null) {
				if (entry.getValue().equals(tab)) {
					entry.getValue().setActive(true);
					activeTab = tab;
				} else {
					entry.getValue().setActive(false);
				}
			}
		}
	}
	
	public void addTab(int id, TXTab tab) {
		if (tabs.get(id) == null) {
			tabs.put(id, tab);
			tab.setParent(this);
		}
	}
	
	public void addTab(TXTab tab) {
		if (!tabs.containsValue(tab)) {
			int id = getFreeID();
			tabs.put(id, tab);
			tab.setParent(this);
		}
	}
	
	public void removeTab(int id) {
		if (tabs.containsKey(id)) {
			this.removeChild(tabs.get(id));
			tabs.remove(id);
		}
	}

	public HashMap<Integer, TXTab> getTabs() {
		return tabs;
	}

	public void setTabs(HashMap<Integer, TXTab> tabs) {
		this.tabs = tabs;
	}
	
	private int getFreeID() {
		for (int i = 0; i < tabs.size(); i++) {
			if (tabs.get(i) == null) {
				return i;
			}
		}
		
		return childs.size() + 1;
	}

	public int getFontSize() {
		return fontSize;
	}

	public void setFontSize(int fontSize) {
		this.fontSize = fontSize;
	}

	public float getTabBarHeight() {
		return tabBarHeight;
	}

	public void setTabBarHeight(float tabBarHeight) {
		this.tabBarHeight = tabBarHeight;
	}

	public void terminate() {
		super.terminate();
		
		tabs.clear();
		TXGUIManager.removeGUI(this);
	}
}
