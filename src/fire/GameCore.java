package fire;

import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/** Hlavní smyčka aplikace.
 *
 * @author David Marek <davidm@atrey.karlin.mff.cuni.cz>
 */
public class GameCore {

	private Screen screen;
	private InputManager input;

	private GameMap map;
	private Player[] players;

	private GameAction end;

	private GameAction player1Forward;
	private GameAction player1Back;
	private GameAction player1Left;
	private GameAction player1Right;
	private GameAction player1Shoot;

	private boolean done;

	/** Vykreslení na obrazovku.
	 * 
	 * @param g Graphics objekt okna.
	 */
	private void draw(Graphics2D g) {
		int w = screen.getWidth();
		int h = screen.getHeight();
		g.clearRect(0, 0, w, h);
		g.drawImage(map.getMap(), 0, 0, w, h,
				players[0].getX()-w, players[0].getY()-h,
				players[0].getX()+w, players[0].getY()+h, screen);

		for (Player p : players) {
			AffineTransform transform = new AffineTransform();
			transform.setToTranslation(w/2, h/2);
			transform.translate(p.getWidth()/2, p.getHeight()/2);
			transform.rotate(Math.toRadians(p.getRotation()));
			transform.translate(-p.getWidth()/2, -p.getHeight()/2);
			g.drawImage(p.getSprite(), transform, screen);
		}
		// TODO: Predelat kresleni
	}

	/** Aktualizace hry.
	 * 
	 * @param elapsedTime Uběhlý čas od poslední aktualizace.
	 */
	private void update(long elapsedTime) {
		processGameActions();
		for (Player p : players) {
			p.update(elapsedTime);
		}
	}

	private void initGameActions() {
		end = new GameAction("Konec hry");
		input.mapToKey(end, KeyEvent.VK_ESCAPE);

		player1Forward = new GameAction("Player 1 forward");
		input.mapToKey(player1Forward, KeyEvent.VK_UP);

		player1Back = new GameAction("Player 1 back");
		input.mapToKey(player1Back, KeyEvent.VK_DOWN);

		player1Left = new GameAction("Player 1 left");
		input.mapToKey(player1Left, KeyEvent.VK_LEFT);

		player1Right = new GameAction("Player 1 right");
		input.mapToKey(player1Right, KeyEvent.VK_RIGHT);

		player1Shoot = new GameAction("Player 1 shoot");
		input.mapToKey(player1Shoot, KeyEvent.VK_CONTROL);
		// TODO: Dodelat herni akce a jejich zpracovani
	}

	private void processGameActions() {
		if (end.isPressed()) {
			done = true;
			end.reset();
		}

		boolean pressedMove = false, pressedSteering = false;
		if (player1Forward.isPressed()) { 
			players[0].forward();
			pressedMove = true;
		} 

		if (player1Back.isPressed()) {
			players[0].back();
			pressedMove = true;
		} 

		if (player1Left.isPressed()) {
			players[0].left();
			pressedSteering = true;
		}

		if (player1Right.isPressed()) {
			players[0].right();
			pressedSteering = true;
		}	

		if (!pressedMove) { players[0].dontMove(); }
		if (!pressedSteering) { players[0].dontSteer(); }
	}


	/** Hlavní smyčka hry.
	 * 
	 */
	public void loop() {
		long startTime = System.currentTimeMillis();
		long currTime = startTime;
		BufferStrategy strategy = screen.getBufferStrategy();

		while (!done) {

			long elapsedTime = System.currentTimeMillis() - currTime;
			currTime += elapsedTime;

			update(elapsedTime);

			do {
				do {
					Graphics2D g = (Graphics2D) strategy.getDrawGraphics();
					draw(g);
					g.dispose();
				} while (strategy.contentsRestored());
				strategy.show();
			} while (strategy.contentsLost());
			Toolkit.getDefaultToolkit().sync();

			try {
				Thread.sleep(20);
			} catch (InterruptedException ex) {
				// Neodchytavam, protoze me nezajima.
			}

		}
	}

	public GameCore(Screen s) throws IOException {
		done = false;
		screen = s;
		input = new InputManager(s);
		initGameActions();

		players = new Player[1];
		players[0] = new Player(screen.getWidth(), screen.getHeight());

		BufferedImage gmap = ImageIO.read(new File("maps/first_map.jpg"));
		BufferedImage bitmap = ImageIO.read(new File("maps/first_map_obst.gif"));
		map = new GameMap(gmap, bitmap);
	}

	public static void main(String[] args) {
		Screen w = new Screen(800, 600, false);
		GameCore gc = null;
		try {
			gc = new GameCore(w);
		} catch (IOException e) {
			System.err.println("Resources not found");
			System.exit(1);
		}
		// TODO: Otestovat Sprite
		try {
			gc.loop();
		} finally {
			w.restoreScreen();
		}
	}

}
