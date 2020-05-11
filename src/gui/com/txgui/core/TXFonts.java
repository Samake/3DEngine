package com.txgui.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;

import samake.engine.logging.Console;
import samake.engine.logging.Console.LOGTYPE;
import samake.engine.utils.Utils;

public class TXFonts {

	private static final String FONT_FOLDER = "/resources/fonts/";
	
    public static final ByteBuffer TOXICFONT = loadFont("toxigenesis.ttf");
    
	private static ByteBuffer loadFont(String fontName) {
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
