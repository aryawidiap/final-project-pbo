package GameState;

import TileMap.Background;
import java.awt.*;
import java.awt.event.KeyEvent; 

public class GameOverState extends GameState{
	
	private Background bg;
	private int currentChoice = 0;
	private String[] option = {
			"Restart",
			"Quit"
	};
	
	private Color titleColor;
	private Font titleFont;
	private Font font;
	public GameOverState(GameStateManager gsm) {
		this.gsm = gsm;
		
		try {
			bg = new Background("/Background/GameOpening.gif", 1);
			bg.setVector(-0.1, 0);
			titleColor = new Color(128,0,0);
			titleFont =  new Font("Century Gothic", Font.PLAIN, 28);
			font = new Font("Arial", Font.PLAIN, 12);
		} catch (Exception e) {
			e.printStackTrace();  
		}
	}
	
	public void init() {
		
	}
	public void update() {
		bg.update();
	}
	public void draw(Graphics2D g) {
		bg.draw(g);
		
		g.setColor(titleColor);
		g.setFont(titleFont);
		g.drawString("Game Over", 30, 70);
		
		g.setFont(font);
		for(int i = 0; i<option.length; i++) {
			if(i==currentChoice) {
				g.setColor(Color.BLACK);
			} else {
				g.setColor(Color.RED);
			}
			g.drawString(option[i], 145, 140+i*15);
		}
		
	}
	
	private void select() {
		if(currentChoice==0) {
			/*Return to menu page*/
			gsm.setState(GameStateManager.MENUSTATE);
		}
		if(currentChoice==1) {
			System.exit(0);
		}
	}
	public void keyPressed(int k) {
		if(k == KeyEvent.VK_ENTER) {
			select();
		}
		if(k == KeyEvent.VK_UP) {
			currentChoice--;
			if(currentChoice==-1) {
				currentChoice = option.length - 1;
			}
		}
		if(k == KeyEvent.VK_DOWN) {
			currentChoice++;
			if(currentChoice == option.length) {
				currentChoice = 0;
			}
		}
	}
	public void keyReleased(int k) {
		
	}


}
