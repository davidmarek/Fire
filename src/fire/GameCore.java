package fire;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.ListIterator;
import javax.imageio.ImageIO;

/** Hlavní smyčka aplikace.
 *
 * @author David Marek <davidm@atrey.karlin.mff.cuni.cz>
 */
public class GameCore {

	private Screen screen;
	private InputManager input;

	private GameMap map;
	private ObjectsList objectsList;

	private Player[] players;
	private LinkedList<GameObject> gameObjects;

	private GameAction end;

	private GameAction player1Forward;
	private GameAction player1Back;
	private GameAction player1Left;
	private GameAction player1Right;
	private GameAction player1Shoot;

	private GameAction player2Forward;
	private GameAction player2Back;
	private GameAction player2Left;
	private GameAction player2Right;
	private GameAction player2Shoot;

	private long milliSec1;
	private long milliSec2;
	private boolean shot1;
	private boolean shot2;


	private boolean done;

	/** Vykreslení na obrazovku.
	 * 
	 * @param g Graphics objekt okna.
	 */
	private void draw(Graphics2D g) {
		int w = screen.getWidth();
		int h = screen.getHeight();

		Player p = players[0];
		g.setClip(0,0,w/2,h);
		g.clearRect(0, 0, w, h);
		g.drawImage(map.getMap(), 0, 0, w/2, h,
				players[0].getX()-w/4, players[0].getY()-h/2,
				players[0].getX()+w/4, players[0].getY()+h/2, screen);

		AffineTransform transform = new AffineTransform();
		transform.setToTranslation(w/4, h/2);

		transform.translate(p.getWidth()/2, p.getHeight()/2);
		transform.rotate(Math.toRadians((double)p.getHeading()));
		transform.translate(-p.getWidth()/2, -p.getHeight()/2);
		g.drawImage(p.getSprite(), transform, screen);

		for (GameObject o : gameObjects) {
			if (o != p) {
				transform.setToTranslation(o.getX()-p.getX()+w/4, o.getY()-p.getY()+h/2);

				transform.translate(o.getWidth()/2, o.getHeight()/2);
				transform.rotate(Math.toRadians(o.getHeading()));
				transform.translate(-o.getWidth()/2, -o.getHeight()/2);

				g.drawImage(o.getSprite(), transform, screen);
			}
		}

		// TODO: Predelat kresleni
		p = players[1];
		g.setClip(w/2,0,w,h);
		g.clearRect(w/2, 0, w, h);
		g.drawImage(map.getMap(), w/2, 0, w, h,
				p.getX()-w/4, p.getY()-h/2,
				p.getX()+w/4, p.getY()+h/2, screen);

		transform = new AffineTransform();
		transform.setToTranslation(3*w/4, h/2);

		transform.translate(p.getWidth()/2, p.getHeight()/2);
		transform.rotate(Math.toRadians(p.getHeading()));
		transform.translate(-p.getWidth()/2, -p.getHeight()/2);
		g.drawImage(p.getSprite(), transform, screen);
		g.drawRect(3*w/4, h/2, (int)p.getWidth(), (int)p.getHeight());
		g.setColor(Color.red);
		g.drawRect(3*w/4 + (int)p.getWidth()/2, h/2 + (int)p.getHeight()/2, 1, 1);
		g.setColor(Color.blue);
		g.drawRect(p.newXp - p.getX() + 3*w/4, p.newYp - p.getY() + h/2, 1, 1);
		g.setColor(Color.yellow);
		g.drawRect(p.newXLp - p.getX() + 3*w/4, p.newYLp - p.getY() + h/2, 1, 1);
		g.setColor(Color.black);

		for (GameObject o : gameObjects) {
			if (o != p) {
				transform.setToTranslation(o.getX()-p.getX()+3*w/4, o.getY()-p.getY()+h/2);

				transform.translate(o.getWidth()/2, o.getHeight()/2);
				transform.rotate(Math.toRadians(o.getHeading()));
				transform.translate(-o.getWidth()/2, -o.getHeight()/2);

				g.drawImage(o.getSprite(), transform, screen);
				g.drawRect((int)o.getX()-p.getX()+3*w/4, (int)o.getY()-p.getY()+h/2, (int)o.getWidth(), (int)o.getHeight());
			}
		}
	}

	/** Aktualizace hry.
	 * 
	 * @param elapsedTime Uběhlý čas od poslední aktualizace.
	 */
	private void update(long elapsedTime) {
		processGameActions();
		ListIterator<GameObject> li = gameObjects.listIterator();
		while (li.hasNext()) {
			GameObject o = li.next();
			o.update(elapsedTime);
			if (!o.isAlive()) {
				li.remove();
			}
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

		player1Shoot = new GameAction("Player 1 shoot");//, GameAction.Behavior.DETECT_INITIAL_PRESS_ONLY);
		input.mapToKey(player1Shoot, KeyEvent.VK_SLASH);

		player2Forward = new GameAction("Player 2 forward");
		input.mapToKey(player2Forward, KeyEvent.VK_W);

		player2Back = new GameAction("Player 2 back");
		input.mapToKey(player2Back, KeyEvent.VK_S);

		player2Left = new GameAction("Player 2 left");
		input.mapToKey(player2Left, KeyEvent.VK_A);

		player2Right = new GameAction("Player 2 right");
		input.mapToKey(player2Right, KeyEvent.VK_D);

		player2Shoot = new GameAction("Player 2 shoot");//, GameAction.Behavior.DETECT_INITIAL_PRESS_ONLY);
		input.mapToKey(player2Shoot, KeyEvent.VK_Q);
	}

	private void processGameActions() {
		if (end.isPressed()) {
			done = true;
			end.reset();
		}

		boolean pressed1Move = false, pressed1Steering = false;
		if (player1Forward.isPressed()) { 
			players[0].forward();
			pressed1Move = true;
		} 

		if (player1Back.isPressed()) {
			players[0].back();
			pressed1Move = true;
		} 

		if (player1Left.isPressed()) {
			players[0].left();
			pressed1Steering = true;
		}

		if (player1Right.isPressed()) {
			players[0].right();
			pressed1Steering = true;
		}	

		if (!pressed1Move) { players[0].dontMove(); }
		if (!pressed1Steering) { players[0].dontSteer(); }

		if (player1Shoot.isPressed() && System.currentTimeMillis() >= 300+milliSec1) {
			Missile m = players[0].shoot();
			gameObjects.add(m);
			milliSec1 = System.currentTimeMillis();
			shot1 = true;
		}

		if (player1Shoot.isReleased()) {
			shot1 = false;
		}

		// Druhy hrac
		boolean pressed2Move = false, pressed2Steering = false;
		if (player2Forward.isPressed()) {
			players[1].forward();
			pressed2Move = true;
		}

		if (player2Back.isPressed()) {
			players[1].back();
			pressed2Move = true;
		}

		if (player2Left.isPressed()) {
			players[1].left();
			pressed2Steering = true;
		}

		if (player2Right.isPressed()) {
			players[1].right();
			pressed2Steering = true;
		}

		if (!pressed2Move) { players[1].dontMove(); }
		if (!pressed2Steering) { players[1].dontSteer(); }

		if (player2Shoot.isPressed() && System.currentTimeMillis() >= 300+milliSec2) {
			Missile m = players[1].shoot();
			gameObjects.add(m);
			milliSec2 = System.currentTimeMillis();
			shot2 = true;
		}

		if (player2Shoot.isReleased()) {
			shot2 = false;
		}

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

		BufferedImage gmap = ImageIO.read(new File("maps/first_map.jpg"));
		BufferedImage bitmap = ImageIO.read(new File("maps/first_map_obst.gif"));
		map = new GameMap(gmap, bitmap);

		objectsList = new ObjectsList();

		players = new Player[2];
		players[0] = new Player(screen.getWidth(), screen.getHeight(), map, objectsList);
		players[1] = new Player(screen.getWidth()+10, screen.getHeight()+10, map, objectsList);

		gameObjects = new LinkedList<GameObject>();
		gameObjects.add(players[0]);
		gameObjects.add(players[1]);

		objectsList.add(players[0]);
		objectsList.add(players[1]);

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
