package com.txgui.core;

import static org.lwjgl.nanovg.NanoVG.NVG_ALIGN_BASELINE;
import static org.lwjgl.nanovg.NanoVG.NVG_ALIGN_BOTTOM;
import static org.lwjgl.nanovg.NanoVG.NVG_ALIGN_CENTER;
import static org.lwjgl.nanovg.NanoVG.NVG_ALIGN_LEFT;
import static org.lwjgl.nanovg.NanoVG.NVG_ALIGN_MIDDLE;
import static org.lwjgl.nanovg.NanoVG.NVG_ALIGN_RIGHT;
import static org.lwjgl.nanovg.NanoVG.NVG_ALIGN_TOP;
import static org.lwjgl.nanovg.NanoVGGL3.nvglCreateImageFromHandle;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;

import com.txgui.core.TXGUIManager.Align;

import samake.engine.logging.Console;
import samake.engine.logging.Console.LOGTYPE;
import samake.engine.utils.Utils;

public class TXUtils {
	
	private static final String FONT_FOLDER = "/resources/fonts/";

	public static int convertAlign(Align align) {
		switch(align){ 
        case LEFT: 
        	return NVG_ALIGN_LEFT;
        case CENTER: 
        	return NVG_ALIGN_CENTER;
        case RIGHT: 
        	return NVG_ALIGN_RIGHT;
        case TOP:
        	return NVG_ALIGN_TOP;
        case MIDDLE:
        	return NVG_ALIGN_MIDDLE;
		case BOTTOM:
			return NVG_ALIGN_BOTTOM;
		case BASELINE:
			return NVG_ALIGN_BASELINE;
		}
		
		return 0;
	}
	
	public static int generateImageFromTexture(long vg, int texID, int w, int h, int flags) {
		return nvglCreateImageFromHandle(vg, texID, w, h, flags);
	}
	
	public static ByteBuffer loadFont(String fontName) {
		String filePath = new String(System.getProperty("user.dir") + FONT_FOLDER + fontName).replace("/", "\\");
		File fontFile = new File(filePath);
		
		if (fontFile.exists()) {
			try {
				InputStream inputStream = new FileInputStream(fontFile);
				return Utils.resourceToByteBuffer(inputStream, 150 * 1024);
			} catch (Exception ex) {
				Console.print(ex.toString(), LOGTYPE.ERROR, false);
			}
		} else {
			Console.print("FONT not found: " + filePath + System.lineSeparator(), LOGTYPE.ERROR, false);
		}
		
		return null;
	}
}
