package com.txgui.core;

import static org.lwjgl.nanovg.NanoVG.nvgBeginFrame;
import static org.lwjgl.nanovg.NanoVG.nvgCreateFontMem;
import static org.lwjgl.nanovg.NanoVG.nvgEndFrame;
import static org.lwjgl.nanovg.NanoVGGL3.NVG_ANTIALIAS;
import static org.lwjgl.nanovg.NanoVGGL3.NVG_STENCIL_STROKES;
import static org.lwjgl.nanovg.NanoVGGL3.nvgCreate;
import static org.lwjgl.nanovg.NanoVGGL3.nvgDelete;
import static org.lwjgl.system.MemoryUtil.NULL;

import java.util.ArrayList;

import samake.engine.logging.Console;
import samake.engine.logging.Console.LOGTYPE;

public class TXGUIManager {
	
	private static long vg;
	private static int fontDefault;
	private static int fontBold;
	private static TXStyle style;
	
	private static int windowWidth;
	private static int windowHeight;
	
	private static boolean interactWithGUI = false;
	
	private static ArrayList<TXElement> guiElements = new ArrayList<TXElement>();
	private static ArrayList<TXElement> renderedGUIElements = new ArrayList<TXElement>();
	
	public enum Align {
		LEFT, CENTER, RIGHT, TOP, MIDDLE, BOTTOM, BASELINE;
	}

	public static void init(int windowWidth, int windowHeight) {

		setWindowWidth(windowWidth);
		setWindowHeight(windowHeight);
		
		vg = nvgCreate(NVG_ANTIALIAS | NVG_STENCIL_STROKES);

        if (vg == NULL) {
        	Console.print("Could not init nanoVG.", LOGTYPE.ERROR, false);
        }
        
        loadStyle();
        loadFonts();
        
        Console.print("ToxicGUI initialzed.", LOGTYPE.ERROR, false);
	}
	
	private static void loadStyle() {
		setStyle(new TXStyle("dark.xml"));
	}
	
	private static void loadFonts() {
		fontDefault = nvgCreateFontMem(vg, "default", TXFonts.TOXICFONT, 0);
		
        if (fontDefault == -1) {
        	Console.print("Font TOXICFONT could'nt be loaded!" + System.lineSeparator(), LOGTYPE.ERROR, false);
        }
        
        fontBold = nvgCreateFontMem(vg, "default-bold", TXFonts.TOXICFONT, 0);
		
        if (fontBold == -1) {
        	Console.print("Font TOXICFONT could'nt be loaded!" + System.lineSeparator(), LOGTYPE.ERROR, false);
        }
	}
	
	public static void addGUI(TXElement element) {
		if (!guiElements.contains(element)) {
			guiElements.add(element);
		}
	}
	
	public static void removeGUI(TXElement element) {
		if (guiElements.contains(element)) {
			guiElements.remove(element);
		}
	}
	
	public static void update(double delta) {
		interactWithGUI = false;
		renderedGUIElements.clear();

		for (TXElement gui : guiElements) {
			if (gui != null) {
				if (gui.getParent() == null) {
					gui.update(delta);
					renderedGUIElements.add(gui);
				}
			}
		}
	}
	
	public static void render() {
		for (TXElement gui : renderedGUIElements) {
			if (gui != null) {
				nvgBeginFrame(vg, windowWidth, windowHeight, windowWidth / windowHeight);
				
				gui.render(vg);
				
				nvgEndFrame(vg);
			}
		}
	}
	
	public static long getHandle() {
		return vg;
	}
	
	public static int getWindowWidth() {
		return windowWidth;
	}

	public static void setWindowWidth(int windowWidth) {
		TXGUIManager.windowWidth = windowWidth;
	}
	
	public static int getWindowHeight() {
		return windowHeight;
	}

	public static void setWindowHeight(int windowHeight) {
		TXGUIManager.windowHeight = windowHeight;
	}

	public static ArrayList<TXElement> getGuiElements() {
		return guiElements;
	}

	public static TXStyle getStyle() {
		return style;
	}

	public static void setStyle(TXStyle style) {
		TXGUIManager.style = style;
	}

	public static int getFontDefault() {
		return fontDefault;
	}

	public static int getFontBold() {
		return fontBold;
	}

	public static boolean isInteractWithGUI() {
		return interactWithGUI;
	}

	public static void setInteractWithGUI(boolean interactWithGUI) {
		TXGUIManager.interactWithGUI = interactWithGUI;
	}

	public static void reset() {
		guiElements.clear();
		renderedGUIElements.clear();
	}
	
	public static void terminate() {
		reset();
		nvgDelete(vg);
	}
}
