package GameState;

import java.awt.Color;
import java.awt.Graphics2D;
import Main.GamePanel;
import TileMap.Background;
import TileMap.TileMap;

public class Level1State extends GameState {

	private TileMap tileMap;
	private Background bg;

	public Level1State(GameStateManager gsm) {
		this.gsm = gsm;
		init();
	}

	public void init() {
		tileMap = new TileMap(30);
		tileMap.loadTiles(""); // input tile picture here
		tileMap.loadMap(""); // input map here
		tileMap.setPosition(0, 0);
		
		bg = new Background("", 0.1); // input background here
	}

	public void update() {
	}

	public void draw(Graphics2D g) {
		// Draw bg
		bg.draw(g);
		
		// Draw tile map
		tileMap.draw(g);
	}

	@Override
	public void keyPressed(int k) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyReleased(int k) {
		// TODO Auto-generated method stub

	}

}
