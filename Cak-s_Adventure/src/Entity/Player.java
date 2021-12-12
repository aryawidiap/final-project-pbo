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
	//private int fire;
	//private int maxFire;
	private boolean dead;
	private boolean flinching;
	private long flinchTimer;

	// Attack
	// private boolean firing;
	private int attackCost;
	private int attackDamage;
	private ArrayList<FireBall> fireBalls;

	// Scratch
	private boolean scratching;
	private int scratchDamage;
	private int scratchRange;

	// Gliding
	//private boolean gliding;

	// Animation
	private ArrayList<BufferedImage[]> sprites;
	private final int[] numFrames = { // numbers of player sprite for each action / animation
			1, 7, 7, 3};

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
		cwidth = 20;
		cheight = 20;

		moveSpeed = 0.3;
		maxSpeed = 1.6;
		stopSpeed = 0.4;
		fallSpeed = 0.15;
		maxFallSpeed = 4.0;
		jumpStart = -4.8;
		facingRight = true;
		health = maxHealth = 5;
		// fire = maxFire = 2500;

		// attackCost = 200;
		// attackDamage = 5;
		// fireBalls = new ArrayList<FireBall>();

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

	/*public int getFire() {
		return fire;
	}

	public int getMaxFire() {
		return maxFire;
	}

	public void setFiring() {
		firing = true;
	}*/

	public void setScratching() {
		scratching = true;
	}

	/*public void setGliding(boolean b) {
		gliding = b;
	}*/

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

			// Fireballs
			// for (int j = 0; j < fireBalls.size(); j++) {
			// 	if (fireBalls.get(j).intersects(e)) {
			// 		e.hit(attackDamage);
			// 		fireBalls.get(j).setHit();
			// 		break;
			// 	}
			// }

			// Check enemy collision
			if (intersects(e)) {
				hit(e.getDamage());
			}
		}
	}

	public void hit(int damage) {
		if (flinching)
			return;
		health -= damage;
		if (health < 0)
			health = 0;
		if (health == 0)
			dead = true;
		flinching = true;
		flinchTimer = System.nanoTime();
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
		if ((currentAction == SCRATCHING /*|| currentAction == FIREBALL*/) && !(jumping || falling)) {
			dx = 0;
		}

		// jumping
		if (jumping && !falling) {
			sfx.get("jump").play();
			dy = jumpStart;
			falling = true;
		}

		// falling
		if (falling) {
			if (dy > 0 )
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

		/*if (currentAction == FIREBALL) {
			if (animation.hasPlayedOnce())
				firing = false;
		}*/

		// Fire ball attack
		/*fire += 1;
		if (fire > maxFire)
			fire = maxFire;
		if (firing && currentAction != FIREBALL) {
			if (fire > attackCost) {
				fire -= attackCost;
				FireBall fb = new FireBall(tileMap, facingRight);
				fireBalls.add(fb);
			}
		}

		// Update fireballs
		for (int i = 0; i < fireBalls.size(); i++) {
			fireBalls.get(i).update();
			if (fireBalls.get(i).shouldRemove()) {
				fireBalls.remove(i);
				i--;
			}
		}*/

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
				width = 60;
			}
		} /*else if (firing) {
			if (currentAction != FIREBALL) {
				currentAction = FIREBALL;
				animation.setFrames(sprites.get(FIREBALL));
				animation.setDelay(100);
				width = 30;
			}
		}*/ else if (dy > 0) {
			/*if (gliding) {
				if (currentAction != GLIDING) {
					currentAction = GLIDING;
					animation.setFrames(sprites.get(GLIDING));
					animation.setDelay(100);
					width = 30;
				} else if (currentAction != FALLING) {
					currentAction = FALLING;
					animation.setFrames(sprites.get(FALLING));
					animation.setDelay(100);
					width = 30;
				}
			}*/
		} else if (dy < 0) {
			if (currentAction != JUMPING) {
				currentAction = JUMPING;
				animation.setFrames(sprites.get(JUMPING));
				animation.setDelay(-1);
				width = 30;
			}
		} else if (left || right) {
			if (currentAction != WALKING) {
				currentAction = WALKING;
				animation.setFrames(sprites.get(WALKING));
				animation.setDelay(40);
				width = 30;
			}
		} else {
			if (currentAction != IDLE) {
				currentAction = IDLE;
				animation.setFrames(sprites.get(IDLE));
				animation.setDelay(400);
				width = 30;
			}
		}

		animation.update();

		// Set direction
		if (currentAction != SCRATCHING /*&& currentAction != FIREBALL*/) {
			if (right)
				facingRight = true;
			if (left)
				facingRight = false;
		}
	}

	public void draw(Graphics2D g) {
		setMapPosition();

		// Draw fireBalls
		// for (int i = 0; i < fireBalls.size(); i++) {
		// 	fireBalls.get(i).draw(g);
		// }

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
