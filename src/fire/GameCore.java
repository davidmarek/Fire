package fire;

import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.image.BufferStrategy;

/** Hlavní smyčka aplikace.
 *
 * @author David Marek <davidm@atrey.karlin.mff.cuni.cz>
 */
public class GameCore {

	private Screen screen;
	private InputManager input;

	private GameAction end;

	private boolean done;

	/** Vykreslení na obrazovku.
	 * 
	 * @param g Graphics objekt okna.
	 */
	private void draw(Graphics2D g) {
		g.drawOval(screen.getScreenWidth()/2, screen.getScreenHeight()/2, 100, 100);
		// TODO: Predelat kresleni
	}

	/** Aktualizace hry.
	 * 
	 * @param elapsedTime Uběhlý čas od poslední aktualizace.
	 */
	private void update(long elapsedTime) {
		// TODO: Dodelat update
	}

	private void initGameActions() {
		end = new GameAction("Konec hry");
		input.mapToKey(end, KeyEvent.VK_ESCAPE);
		// TODO: Dodelat herni akce a jejich zpracovani
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

	public GameCore(Screen s) {
		done = false;
		screen = s;
		input = new InputManager(s);
	}

	public static void main(String[] args) {
		Screen w = new Screen(800, 600, false);
		GameCore gc = new GameCore(w);
		// TODO: Otestovat Sprite
		try {
			gc.loop();
		} finally {
			w.restoreScreen();
		}
	}

}
