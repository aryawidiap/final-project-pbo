package GameState;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import Audio.AudioPlayer;
import Entity.Enemy;
import Entity.Explosion;
import Entity.HUD;
import Entity.Player;
import Entity.Enemies.Tugas;
import Main.GamePanel;
import TileMap.Background;
import TileMap.TileMap;

public class Level1State extends GameState {

	private TileMap tileMap;
	private Background bg;

	private Player player;

	private ArrayList<Enemy> enemies;
	private ArrayList<Explosion> explosions;

	private HUD hud;
	
	private AudioPlayer bgMusic;
	public Level1State(GameStateManager gsm) {
		this.gsm = gsm;
		init();
	}

	public void init() {
		tileMap = new TileMap(32);
		tileMap.loadTiles("/Tilemaps/TileSet.gif"); // input tile picture here
		tileMap.loadMap("/Maps/level1-1.map"); // input map here
		tileMap.setPosition(0, 0);
		tileMap.setTween(1);

		bg = new Background("/Background/GameBackground.gif", 0.1); // input background here
		player = new Player(tileMap);
		player.setPosition(64, 205); // set player position to desired starting point

		populateEnemies();

		explosions = new ArrayList<Explosion>();

		hud = new HUD(player);
		bgMusic =  new AudioPlayer("/Audio/little-archemist.wav");//isi file bgmusik
		bgMusic.setVolume(-30);
		bgMusic.play();
}

	private void populateEnemies() {
		enemies = new ArrayList<Enemy>();
		Tugas tugas;

		Point[] points = new Point[] { // set enemies starting point
			new Point(200, 100),
			new Point(860, 100),
			new Point(1525, 120), 
			new Point(2400, 200), 
			new Point(2656, 190)
		};

		for (int i = 0; i < points.length; i++) {
			tugas = new Tugas(tileMap);
			tugas.setPosition(points[i].x, points[i].y);
			enemies.add(tugas);
		}
	}

	public void update() {
		// Update player
		player.update();
		tileMap.setPosition(GamePanel.WIDTH / 2 - player.getx(), GamePanel.HEIGHT / 2 - player.gety());

		// set background
		bg.setPosition(tileMap.getX(), tileMap.getY());

		// Attack enemies
		player.checkAttack(enemies);

		// Update All Enemies
		for (int i = 0; i < enemies.size(); i++) {
			Enemy e = enemies.get(i);
			e.update();
			if (e.isDead()) {
				enemies.remove(i);
				i--;
				explosions.add(new Explosion(e.getx(), e.gety()));
			}
		}

		// Update explosions
		for (int i = 0; i < explosions.size(); i++) {
			Explosion explosion = explosions.get(i);
			explosion.update();
			if (explosion.shouldRemove()) {
				explosions.remove(i);
				i--;
			}
		}
	}

	public void draw(Graphics2D g) {
		// Check if player is dead
		if(player.isDead()) {
			gsm.setState(GameStateManager.GAMEOVERSTATE);
			bgMusic.stop();
		}
		
		// Draw bg
		bg.draw(g);

		// Draw tile map
		tileMap.draw(g);

		// Draw player
		player.draw(g);

		// Draw enemies
		for (int i = 0; i < enemies.size(); i++) {
			enemies.get(i).draw(g);
		}

		// Draw explosions
		for (int i = 0; i < explosions.size(); i++) {
			explosions.get(i).setMapPosition((int) tileMap.getX(), (int) tileMap.getY());
			explosions.get(i).draw(g);
		}
		// draw HUD
		hud.draw(g);
	}

	@Override
	public void keyPressed(int k) {
		if (k == KeyEvent.VK_LEFT) {
			player.setLeft(true);
		}
		if (k == KeyEvent.VK_RIGHT) {
			player.setRight(true);
		}
		if (k == KeyEvent.VK_UP) {
			player.setUp(true);
		}
		if (k == KeyEvent.VK_DOWN) {
			player.setDown(true);
		}
		if (k == KeyEvent.VK_W) {
			player.setJumping(true);
		}
		/*if (k == KeyEvent.VK_E) {
			player.setGliding(true);
		}*/
		if (k == KeyEvent.VK_R) {
			player.setScratching();
		}
	}

	@Override
	public void keyReleased(int k) {
		if (k == KeyEvent.VK_LEFT) {
			player.setLeft(false);
		}
		if (k == KeyEvent.VK_RIGHT) {
			player.setRight(false);
		}
		if (k == KeyEvent.VK_UP) {
			player.setUp(false);
		}
		if (k == KeyEvent.VK_DOWN) {
			player.setDown(false);
		}
		if (k == KeyEvent.VK_W) {
			player.setJumping(false);
		}
		/*if (k == KeyEvent.VK_E) {
			player.setGliding(false);
		}*/
		if (k == KeyEvent.VK_R) {
			player.setScratching();
		}
	}

}
