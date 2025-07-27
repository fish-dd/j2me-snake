package snakegame;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.game.GameCanvas;
import java.util.Vector;
import java.util.Random;

public class SnakeCanvas extends GameCanvas implements Runnable{
	
	int sleepMillis = 1000 / 5;
	
	int[][] dirs = {{-1, 0}, {0, 1}, {1, 0}, {0, -1}};
	int currentDir = 0;
	int[] pos = {0, 0};
	int[] applePos = {0, 0};
	int grid = 10;
	int snakeSize = 1;
	boolean ateApple = false;
	boolean[] win;
	Vector tails = new Vector();
	Random random = new Random();
	
	protected SnakeCanvas(boolean[] win) {
		super(true);
		this.win = win;
	}
	
	public void start() {
		System.out.println(getWidth() + " " + getHeight());
	}

	public void run() {
		Graphics g = getGraphics();
		int tileSize = getWidth() / grid;
		
		tails.trimToSize();
		placeApple();
		
		while (true) {
			checkPresses();
			
			if (tails.size() + 1 == grid * grid) {
				win[0] = true;
				break;
			}
			
			if (currentDir != 0) {
				int[] posClone = new int[2];
				System.arraycopy(pos, 0, posClone, 0, 2);
				
				tails.insertElementAt(posClone, 0);
				if (!ateApple) {
					tails.removeElementAt(tails.size() - 1);
				}
				ateApple = false;
				
				pos[0] += dirs[currentDir - 1][0];
				pos[1] -= dirs[currentDir - 1][1];
				
				boolean snakeOverlap = false;
				for (int i = 0; i < tails.size(); i++) {
					int[] tail = (int[]) tails.elementAt(i);
					if (tail[0] == pos[0] && tail[1] == pos[1]) {
						snakeOverlap = true;
						break;
					}
				}
				if (pos[0] < 0 || pos[0] > grid - 1 || pos[1] < 0 || pos[1] > grid - 1 || snakeOverlap) {
					break;
				}
			}
			
			if (pos[0] == applePos[0] && pos[1] == applePos[1]) {
				ateApple = true;
				placeApple();
			} 
				
			g.setColor(0x000000);
			g.fillRect(0, 0, getWidth(), getHeight());
			g.setColor(0xCA3535);
			g.fillRect(tileSize * applePos[0], tileSize * applePos[1], tileSize, tileSize);
			g.setColor(0x7FD42C);
			g.fillRect(tileSize * pos[0], tileSize * pos[1], tileSize, tileSize);
			g.setColor(0x6BC026);
			for (int i = 0; i < tails.size(); i++) {
				int[] tail = (int[]) tails.elementAt(i);
				g.fillRect(tileSize * tail[0], tileSize * tail[1], tileSize, tileSize);
			}
			
			flushGraphics();
			
			System.out.println(pos[0] + " " + pos[1]);
			
			try {
				Thread.sleep(sleepMillis);
			}
			catch (InterruptedException e) {
				
			}
		}
		System.out.println("Игра окончена. " + win[0]);
	}
	
	void checkPresses() {
		int keyState = getKeyStates();
	    if ((keyState & LEFT_PRESSED) != 0) {
	        currentDir = 1;
	    }
	    else if ((keyState & UP_PRESSED) != 0) {
	    	currentDir = 2;
	    }
	    else if ((keyState & RIGHT_PRESSED) != 0) {
	    	currentDir = 3;
	    }
	    else if ((keyState & DOWN_PRESSED) != 0) {
	    	currentDir = 4;
	    }
	}
	
	void placeApple() {
		boolean overlapsSnake = true;
		while (applePos == pos || overlapsSnake) {
			applePos[0] = random.nextInt(grid);
			applePos[1] = random.nextInt(grid);
			
			overlapsSnake = false;
			for (int i = 0; i < tails.size(); i++) {
				int[] tail = (int[]) tails.elementAt(i);
				if (tail[0] == applePos[0] && tail[1] == applePos[1]) {
					overlapsSnake = true;
					break;
				}
			}
		}
	}
	
}
