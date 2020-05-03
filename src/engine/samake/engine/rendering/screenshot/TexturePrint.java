package samake.engine.rendering.screenshot;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import samake.engine.config.Configuration;

public class TexturePrint {

	public static void saveTerrainHeightMap(int width, int height, float[][] pixels) {
		String filePath = new String(System.getProperty("user.dir") + "\\" + Configuration.SCREENSHOTS + "terrain_heightMap.png").replace("/", "\\");
		File file = new File(filePath);
		String format = "PNG";
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		   
		for(int x = 0; x < width; x++) {
		    for(int y = 0; y < height; y++) {
		        int r = (int) (255 * pixels[y][x]) & 0xFF;
		        int g = (int) (255 * pixels[y][x]) & 0xFF;
		        int b = (int) (255 * pixels[y][x]) & 0xFF;
		        image.setRGB(x, height - (y + 1), (0xFF << 24) | (r << 16) | (g << 8) | b);
		    }
		}
		   
		try {
		    ImageIO.write(image, format, file);
		} catch (IOException e) { e.printStackTrace(); }
	}
}
