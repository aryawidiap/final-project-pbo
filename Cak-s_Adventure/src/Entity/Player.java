package Entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;

import javax.imageio.ImageIO;

import Audio.AudioPlayer;
import TileMap.TileMap;

public class Player extends MapObject {

	// Attributes
	private int health;
	private int maxHealth;
	private boolean dead;
	private boolean flinching;
	private long flinchTimer;

	// Scratch
	private boolean scratching;
	private int scratchDamage;
	private int scratchRange;

	// Gliding
	// private boolean gliding;

	// Animation
	private ArrayList<BufferedImage[]> sprites;
	private final int[] numFrames = { // numbers of player sprite for each action / animation
			1, 7, 7, 3 };

	// Animation Actions / State
	private static final int IDLE = 0;
	private static final int WALKING = 1;
	private static final int JUMPING = 2;
	private static final int SCRATCHING = 3;

	private HashMap<String, AudioPlayer> sfx;

	public Player(TileMap tm) {
		super(tm);
		width = 32;
		height = 32;
		cwidth = 10;
		cheight = 32;

		moveSpeed = 0.5;
		maxSpeed = 1.6;
		stopSpeed = 0.4;
		fallSpeed = 0.15;
		maxFallSpeed = 4.0;
		jumpStart = -4.8;
		facingRight = true;
		health = maxHealth = 5;

		scratchDamage = 8;
		scratchRange = 40;

		// Load sprites
		try {
			BufferedImage spriteSheet = ImageIO.read(getClass().getResourceAsStream("/Sprites/Player/CaksSprite.gif"));
			sprites = new ArrayList<BufferedImage[]>();
			for (int i = 0; i < 4; i++) { // rows
				BufferedImage[] bi = new BufferedImage[numFrames[i]];
				for (int j = 0; j < numFrames[i]; j++) { // column
					bi[j] = spriteSheet.getSubimage(j * width, i * height, width, height);
				}
				sprites.add(bi);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		animation = new Animation();
		currentAction = IDLE;
		animation.setFrames(sprites.get(IDLE));
		animation.setDelay(400);

		sfx = new HashMap<String, AudioPlayer>();

		sfx.put("jump", new AudioPlayer("/Audio/jump.wav")); // isi lokasi music
		sfx.put("scratch", new AudioPlayer("/Audio/punch.wav")); // isi lokasi music
	}

	public int getHealth() {
		return health;
	}

	public int getMaxHealth() {
		return maxHealth;
	}

	public void setScratching() {
		scratching = true;
	}
	
	public boolean isDead() {
		return dead;
	}

	/*
	 * public void setGliding(boolean b) { gliding = b; }
	 */

	// Check attack
	public void checkAttack(ArrayList<Enemy> enemies) {
		// Loop through enemies
		for (int i = 0; i < enemies.size(); i++) {
			Enemy e = enemies.get(i);

			// Scratch attack
			if (scratching) {
				if (facingRight) {
					if (e.getx() > x && // Enemy is at right of Player
							e.getx() < x + scratchRange && // Enemy is in range
							e.gety() > y - height / 2 && // Enemy is reachable vertically
							e.gety() < y + height / 2) {
						e.hit(scratchDamage);
					}
				} else {
					if (e.getx() < x && // Enemy is at left of Player
							e.getx() > x - scratchRange && // Enemy is in range
							e.gety() > y - height / 2 && // Enemy is reachable vertically
							e.gety() < y + height / 2) {
						e.hit(scratchDamage);
					}
				}
			}

			// Check enemy collision
			if (intersects(e)) {
				hit(e.getDamage());
			}
		}
	}

	public void checkHealth() {
		if (health < 0)
			health = 0;
		if (health == 0)
			dead = true;
	}

	private void hit(int damage) {
		if (flinching)
			return;
		health -= damage;
		checkHealth();
		flinching = true;
		flinchTimer = System.nanoTime();
	}

	private void checkJumping() {
		if (jumping && !falling) {
			sfx.get("jump").play();
			dy = jumpStart;
			falling = true;
		}
	}

	private void checkFalling() {
		if (falling) {

			if (dy > 0)
				dy += fallSpeed * 0.1;
			else
				dy += fallSpeed;

			if (dy > 0)
				jumping = false;

			if (dy < 0 && !jumping)
				dy += stopJumpSpeed;

			if (dy > maxFallSpeed)
				dy = maxFallSpeed;

		}
	}

	private void getNextPosition() {

		// Movement
		if (left) {
			dx -= moveSpeed;
			if (dx < -maxSpeed) {
				dx = maxSpeed;
			}
		} else if (right) {
			dx += moveSpeed;
			if (dx > maxSpeed) {
				dx = maxSpeed;
			}
		} else { // if not going left or right, stop
			if (dx > 0) {
				dx -= stopSpeed;
				if (dx < 0) {
					dx = 0;
				}
			} else if (dx < 0) {
				dx += stopSpeed;
				if (dx > 0) {
					dx = 0;
				}
			}
		}

		// can not move while attacking, except in air
		if ((currentAction == SCRATCHING) && !(jumping || falling)) {
			dx = 0;
		}

		// jumping
		checkJumping();

		// falling
		checkFalling();

	}

	private void configAnimation(int currentAction, long delay, int width) {
		this.currentAction = currentAction;
		animation.setFrames(sprites.get(currentAction));
		animation.setDelay(delay);
		this.width = width;
	}

	public void update() {
		// Update position
		getNextPosition();
		checkTileMapCollision();
		setPosition(xtemp, ytemp);

		// Check attack has stopped
		if (currentAction == SCRATCHING) {
			if (animation.hasPlayedOnce())
				scratching = false;
		}

		// Check done flinching
		if (flinching) {
			long elapsed = (System.nanoTime() - flinchTimer) / 1000000;
			if (elapsed > 1000) {
				flinching = false;
			}
		}

		// Set animation
		if (scratching) {
			if (currentAction != SCRATCHING) {
				sfx.get("scratch").play();
				currentAction = SCRATCHING;
				animation.setFrames(sprites.get(SCRATCHING));
				animation.setDelay(50);
				width = 32;
			}
		} else if (dy > 0) {
			/*
			 * if (gliding) { if (currentAction != GLIDING) { currentAction = GLIDING;
			 * animation.setFrames(sprites.get(GLIDING)); animation.setDelay(100); width =
			 * 30; } else if (currentAction != FALLING) { currentAction = FALLING;
			 * animation.setFrames(sprites.get(FALLING)); animation.setDelay(100); width =
			 * 30; } }
			 */
		} else if (dy < 0) {
			if (currentAction != JUMPING) {
				configAnimation(JUMPING, 150, 32);
			}
		} else if (left || right) {
			if (currentAction != WALKING) {
				configAnimation(WALKING, 40, 32);
			}
		} else {
			if (currentAction != IDLE) {
				configAnimation(IDLE, 40, 32);
			}
		}

		animation.update();

		// Set direction
		if (currentAction != SCRATCHING) {
			if (right)
				facingRight = true;
			if (left)
				facingRight = false;
		}
	}

	public void draw(Graphics2D g) {
		setMapPosition();

		// Draw player
		if (flinching) {
			long elapsed = (System.nanoTime() - flinchTimer) / 1000000;
			if (elapsed / 100 % 2 == 0) { // blinking every 100ms
				return;
			}
		}

		super.draw(g);
	}
}
