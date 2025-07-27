package snakegame;

import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Alert;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;

public class Main extends MIDlet {
	
	SnakeCanvas snakeCanvas;
	boolean[] win = {false};
	
	public Main() {
		snakeCanvas = new SnakeCanvas(this.win);
	}

	protected void destroyApp(boolean arg0) throws MIDletStateChangeException {
		// TODO Auto-generated method stub

	}

	protected void pauseApp() {
		win[0] = false;
		snakeCanvas = new SnakeCanvas(this.win);
	}

	protected void startApp() throws MIDletStateChangeException {
		Display display = Display.getDisplay(this);
		Thread canvasThread = new Thread(snakeCanvas);
		canvasThread.start();
		display.setCurrent(snakeCanvas);
		try {
			canvasThread.join();
		}
		catch (InterruptedException e) {
			
		}
		System.out.println(win[0]);
		
		Alert endAlert = new Alert("Конец");
		endAlert.setString(win[0]?"Победа!":"Игра окончена!");
		display.setCurrent(endAlert);
	}

}
