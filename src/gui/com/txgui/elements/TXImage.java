package com.txgui.elements;


import static org.lwjgl.nanovg.NanoVG.nvgBeginPath;
import static org.lwjgl.nanovg.NanoVG.nvgFill;
import static org.lwjgl.nanovg.NanoVG.nvgFillPaint;
import static org.lwjgl.nanovg.NanoVG.nvgImagePattern;
import static org.lwjgl.nanovg.NanoVG.nvgRect;
import static org.lwjgl.nanovg.NanoVG.nvgRestore;
import static org.lwjgl.nanovg.NanoVG.nvgSave;
import static org.lwjgl.nanovg.NanoVGGL3.NVG_ANTIALIAS;

import org.lwjgl.nanovg.NVGPaint;

import com.txgui.core.TXElement;
import com.txgui.core.TXGUIManager;
import com.txgui.core.TXUtils;

public class TXImage extends TXElement {

	private int image;
	private NVGPaint imagePaint;
	private int textureImage;
	
	public TXImage(int image, float x, float y, float width, float height, TXElement parent) {
		setX(x);
		setY(y);
		setWidth(width);
		setHeight(height);
		setParent(parent);
		
		this.image = image;
		this.imagePaint = NVGPaint.create();
		
		if (image != 0) {
			textureImage = TXUtils.generateImageFromTexture(TXGUIManager.getHandle(), image, (int) getCurrentWidth(), (int) getCurrentHeight(), NVG_ANTIALIAS);
		}

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
			nvgImagePattern(vg, getCurrentX(), getCurrentY(), getCurrentWidth(), getCurrentHeight(), 0, textureImage, alpha / 255.0f, imagePaint);
	        nvgSave(vg);

			nvgBeginPath(vg);
			nvgRect(vg, getCurrentX(), getCurrentY(), getCurrentWidth(), getCurrentHeight());
			nvgFillPaint(vg, imagePaint);
			nvgFill(vg);
	
			nvgRestore(vg);
			
			super.render(vg);
		}
	}

	public int getImage() {
		return image;
	}

	public void setImage(int image) {
		if (image != this.image) {
			this.image = image;
			textureImage = TXUtils.generateImageFromTexture(TXGUIManager.getHandle(), image, (int) getCurrentWidth(), (int) getCurrentHeight(), NVG_ANTIALIAS);
		}
	}

	public void terminate() {
		super.terminate();
		
		imagePaint.free();
		TXGUIManager.removeGUI(this);
	}
}
