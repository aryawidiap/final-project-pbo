package Entity;

import TileMap.TileMap;

public abstract class Enemy extends Actor {
	
	protected int damage;
	
	
	public Enemy(TileMap tm, int maxHealth, int width, int height, int cwidth, int cheight, double moveSpeed, double maxSpeed, double fallSpeed, double maxFallSpeed, int damage) {
		super(tm, maxHealth, width, height, cwidth, cheight, moveSpeed, maxSpeed, fallSpeed, maxFallSpeed);
		this.damage = damage;
	}
	
	public int getDamage() {
		return damage;
	}
	
	
	public void update() {
		
	}
}
