package Entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import TileMap.TileMap;

public class FireBall extends MapObject {
	// Long range attack

	private boolean hit;
	private boolean remove;
	private BufferedImage[] sprites;
	private BufferedImage[] hitSprites;

	public FireBall(TileMap tm, boolean right) {

		super(tm);
		
		facingRight = right;
		moveSpeed = 3.8;
		if (right)
			dx = moveSpeed;
		else
			dx = -moveSpeed;

		width = 30;
		height = 30;
		cwidth = 14;
		cheight = 14;

		// Load sprites
		try {
			BufferedImage spriteSheet = ImageIO.read(getClass().getResourceAsStream(""));

			// Load normal sprites to array
			sprites = new BufferedImage[4];
			for (int i = 0; i < sprites.length; i++) {
				sprites[i] = spriteSheet.getSubimage(i * width, 0, width, height);
			}

			// Load hitting sprites to array
			hitSprites = new BufferedImage[3];
			for (int i = 0; i < hitSprites.length; i++) {
				hitSprites[i] = spriteSheet.getSubimage(i * width, height, width, height);
			}

			// Setup animation
			animation = new Animation();
			animation.setFrames(sprites);
			animation.setDelay(70);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void setHit() { // whether the fireball hits something or not
		if (hit)
			return;
		hit = true;
		animation.setFrames(hitSprites);
		animation.setDelay(70);
		dx = 0;
	}

	public boolean shouldRemove() {
		return remove;
	}

	public void update() {
		checkTileMapCollision();
		setPosition(xtemp, ytemp);
		
		if(dx == 0 && !hit) { //dx will be set to 0 when colliding
			setHit();
		}
		
		animation.update();
		if (hit && animation.hasPlayedOnce()) {
			remove = true;
		}
	}

	public void draw(Graphics2D g) {
		setMapPosition();
		super.draw(g);
	}
}
