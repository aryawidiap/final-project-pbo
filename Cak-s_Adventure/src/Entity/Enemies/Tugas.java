package Entity.Enemies;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import Entity.Animation;
import Entity.Enemy;
import TileMap.TileMap;

public class Tugas extends Enemy{
	
	private ArrayList<BufferedImage[]> sprites; 
	public Tugas(TileMap tm) {
		super(tm, 10, 32, 32, 32, 32, 0.3, 0.3, 0.2, 10.0, 1);
		
		damage = 1;
		
		//load sprites
		sprites = loadSprites("/Sprites/Enemy/Tugas.gif", 1, new int[] {3}, width, height);
		
		animation = new Animation();
		animation.setFrames(sprites.get(0));
		animation.setDelay(300);
		
		right = true;
		facingRight = true;
	}
	
	private void getNextPosition() {
		// Movement
		move();
		
		// falling
		if(falling) {
			dy += fallSpeed;
		}
	}
	
	public void update() {
		
		// update position
		getNextPosition();
		checkTileMapCollision();
		setPosition(xtemp, ytemp);
		
		// check flinching
		if(flinching) {
			long elapsed =
					(System.nanoTime()-flinchTimer)/1000000;
			if(elapsed > 400) {
				flinching = false;
			}
		}
		
		// if it hits a wall, go other direction
		if(right && dx == 0) {
			right = false;
			left = true;
			facingRight = false;
		} else if (left && dx == 0) {
			right = true;
			left = false; 
			facingRight = true;
		}
		
		// update animation
		animation.update();
	}
	
	public void draw(Graphics2D g) {
		
		//if(notOnScreen()) return;
		
		setMapPosition();
		
		super.draw(g);
	}
}
