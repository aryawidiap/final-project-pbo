package Entity;

import TileMap.TileMap;

public abstract class Actor extends Animated {
	
	protected int healthPoints;
	protected int maxHealth;
	protected boolean dead;	
	protected boolean flinching;
	protected long flinchTimer;
	
	

	public Actor(TileMap tm, int maxHealth, int width, int height, int cwidth, int cheight, double moveSpeed, double maxSpeed, double fallSpeed, double maxFallSpeed) {
		super(tm);
		this.healthPoints = this.maxHealth = maxHealth;
		this.dead = false;
		
		this.width = width;
		this.height = height;
		this.cwidth = cwidth;
		this.cheight = cheight;

		this.moveSpeed = moveSpeed;
		this.maxSpeed = maxSpeed;
		this.fallSpeed = fallSpeed;
		this.maxFallSpeed = maxFallSpeed;
		
		flinching = false;
		flinchTimer = 0;
	}

	public int getHealthPoints() {
		return healthPoints;
	}

	public void setHealthPoints(int healthPoints) {
		this.healthPoints = healthPoints;
	}

	public int getMaxHealth() {
		return maxHealth;
	}

	public void setMaxHealth(int maxHealth) {
		this.maxHealth = maxHealth;
	}

	public boolean isDead() {
		return dead;
	}

	public void setDead(boolean dead) {
		this.dead = dead;
	}

	public boolean isFlinching() {
		return flinching;
	}

	public void setFlinching(boolean flinching) {
		this.flinching = flinching;
	}

	public long getFlinchTimer() {
		return flinchTimer;
	}

	public void setFlinchTimer(long flinchTimer) {
		this.flinchTimer = flinchTimer;
	}
	
	protected void hit(int damage) {
		if(dead || flinching) return;
		healthPoints -= damage;
		if(healthPoints < 0) healthPoints=0;
		if(healthPoints == 0) dead = true;
		flinching = true;
		flinchTimer = System.nanoTime();
	}
	
	

}
