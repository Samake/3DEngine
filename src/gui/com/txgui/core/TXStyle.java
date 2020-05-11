package com.txgui.core;

import java.awt.Color;
import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.joml.Vector3f;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import samake.engine.logging.Console;
import samake.engine.logging.Console.LOGTYPE;

public class TXStyle {
	
	protected final String STYLES_FOLDER = "/settings/styles/";
	
	private Vector3f backGroundColor;
	private Vector3f forGroundColor;
	private Vector3f shadowColor;
	private Vector3f highLightColor1;
	private Vector3f highLightColor2;
	private Vector3f highLightColor3;
	
	public TXStyle(String fileName) {
		String filePath = new String(System.getProperty("user.dir") + STYLES_FOLDER + fileName).replace("/", "\\");
		
		try {
			parseStyleFile(filePath);
		} catch (Exception e) {
			Console.print("Could not parse style: " + filePath, LOGTYPE.ERROR, false);
		}
	}
	
	protected void parseStyleFile(String filePath) throws Exception {
		File xmlFile = new File(filePath);
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(xmlFile);
		doc.getDocumentElement().normalize();
	
		parseFonts(doc);
		parseColors(doc);
	}

	private void parseFonts(Document doc) {
		NodeList fontList = doc.getElementsByTagName("fonts");
		
		for (int i = 0; i < fontList.getLength(); i++) {
			Node fontBlockRoot = fontList.item(i);
			
			if (fontBlockRoot.hasChildNodes()) {
				NodeList fontBlockChilds = fontBlockRoot.getChildNodes();
				
				for (int j = 0; j < fontBlockChilds.getLength(); j++) {
					Node fontNode = fontBlockChilds.item(j);
					
					if (fontNode.getNodeType() == Node.ELEMENT_NODE) {
						Element element = (Element) fontNode;
						
						String fontName = element.getElementsByTagName("font").item(0).getTextContent();
						String fontSize = element.getElementsByTagName("size").item(0).getTextContent();
						String fontBold= element.getElementsByTagName("bold").item(0).getTextContent();
						
						System.out.println(fontName + ", " + fontSize + ", " + fontBold);
					}
				}
			}
		}
	}
	
	private void parseColors(Document doc) {
		NodeList colorList = doc.getElementsByTagName("colors");
		
		for (int i = 0; i < colorList.getLength(); i++) {
			Node colorBlockRoot = colorList.item(i);
			
			if (colorBlockRoot.hasChildNodes()) {
				NodeList colorBlockChilds = colorBlockRoot.getChildNodes();
				
				for (int j = 0; j < colorBlockChilds.getLength(); j++) {
					Node colorNode = colorBlockChilds.item(j);
					
					if (colorNode.getNodeType() == Node.ELEMENT_NODE) {
						String name = colorNode.getNodeName();
						String value = colorNode.getTextContent().trim();

						if (name.equals("background")) { setBackGroundColor(convertColor(value)); }
						if (name.equals("forGround")) { setForGroundColor(convertColor(value)); }
						if (name.equals("shadow")) { setShadowColor(convertColor(value)); }
						if (name.equals("highlight1")) { setHighLightColor1(convertColor(value)); }
						if (name.equals("highlight2")) { setHighLightColor2(convertColor(value)); }
						if (name.equals("highlight3")) { setHighLightColor3(convertColor(value)); }
					}
				}
				
			}
		}
	}
	
	private Vector3f convertColor(String hexValue) {
		Color color = Color.decode(hexValue);
		return new Vector3f(color.getRed(), color.getGreen(), color.getBlue());
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
}
