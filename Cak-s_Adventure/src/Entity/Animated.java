package Entity;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import TileMap.TileMap;

abstract public class Animated extends MapObject {
	
	public Animated(TileMap tm) {
		super(tm);
	}

	
	protected final ArrayList<BufferedImage[]> loadSprites(String spriteDir, int row, int[] numFrames, int width, int height) {
		ArrayList<BufferedImage[]> sprites = new ArrayList<BufferedImage[]>();
		try {
			BufferedImage spriteSheet = ImageIO.read(getClass().getResourceAsStream(spriteDir));
			for (int i = 0; i < numFrames.length; i++) { // rows
				BufferedImage[] bi = new BufferedImage[numFrames[i]];
				for (int j = 0; j < numFrames[i]; j++) { // column
					bi[j] = spriteSheet.getSubimage(j * width, i * height, width, height);
				}
				sprites.add(bi);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sprites;
	}
	
	protected void move() {
		if (left) {
			dx -= moveSpeed;
			if (dx < -maxSpeed) {
				dx = -maxSpeed;
			}
		} else if (right) {
			dx += moveSpeed;
			if (dx > maxSpeed) {
				dx = maxSpeed;
			}
		}
	}
}
