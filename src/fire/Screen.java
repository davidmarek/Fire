package fire;

import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;

/** Hlavní okno aplikace.
 *
 * Screen se stará o vytvoření hlavního okna.
 *
 * @author David Marek <davidm@atrey.karlin.mff.cuni.cz>
 */
public class Screen extends JFrame {

	private GraphicsDevice device;

	private int screenWidth;
	private int screenHeight;
	private boolean fullscreen;

	private static final String title = "Tunneler";

	/** Šířka okna.
	 * @return Šířka okna.
	 */
	public int getScreenWidth() {
		return screenWidth;
	}

	/** Výška okna.
	 * @return Výška okna.
	 */
	public int getScreenHeight() {
		return screenHeight;
	}

	/** Nastavení rozlišení.
	 * 
	 * Nastaví rozlišení a případně přepne z/do režimu přes celou obrazovku.
	 * 
	 * @param width Nová šířka.
	 * @param height Nová výška.
	 * @param fullscreen Přepnout do režimu celé obrazovky?
	 */
	public void setResolution(int width, int height, boolean fullscreen) {
		if (fullscreen) {
			setFullScreen(new DisplayMode(width, height, DisplayMode.BIT_DEPTH_MULTI, DisplayMode.REFRESH_RATE_UNKNOWN));
		} else {
			setWindowed(width, height);
		}

		this.screenWidth = width;
		this.screenHeight = height;
		this.fullscreen = fullscreen;
	}

	/** Nastavení okna.
	 * 
	 * @param width Nová šířka.
	 * @param height Nová výška.
	 */
	private void setWindowed(int width, int height) {
		if (this.fullscreen) {
			restoreScreen();
		}

		setSize(width, height);
		setUndecorated(false);
		setResizable(false);
		setVisible(true);
	}

	/** Nastavení přes celou obrazovku.
	 * 
	 * @param dm Nový mód rozlišení.
	 */
	private void setFullScreen(DisplayMode dm) {
		setUndecorated(true);
		setResizable(false);

		device.setFullScreenWindow(this);
		if (dm != null && device.isDisplayChangeSupported()) {
			try {
				device.setDisplayMode(dm);
			} catch (IllegalArgumentException e) {
				Logger.getLogger("Tunneler").log(Level.WARNING, "Ignorovane rozliseni");
			}
		}
		setVisible(true);
	}

	/** Vrácení okna do původního stavu.
	 *
	 */
	public void restoreScreen() {
		dispose();
		device.setFullScreenWindow(null);
	}

	/** Vytisknutí všech dostupných rozlišení.
	 * 
	 * @param device Grafické zařízení.
	 */
	public void printAvailableResolutions(GraphicsDevice device) {
		for (DisplayMode m : device.getDisplayModes()) {
			int w = m.getWidth();
			int h = m.getHeight();
			int rate = m.getRefreshRate();
			System.out.println(w + "x" + h + "@" + rate + "Hz");
		}
	}

	/** Vytvoření okna.
	 * 
	 * @param width Šířka
	 * @param height Výška
	 * @param fullscreen Má být v režimu přes celou obrazovku?
	 */
	public Screen(int width, int height, boolean fullscreen) {
		super();

		GraphicsEnvironment environment = GraphicsEnvironment.getLocalGraphicsEnvironment();
		device = environment.getDefaultScreenDevice();

		setIgnoreRepaint(true);

		this.screenWidth = width;
		this.screenHeight = height;
		this.fullscreen = fullscreen;
		if (fullscreen) {
			setFullScreen(new DisplayMode(width, height, DisplayMode.BIT_DEPTH_MULTI, DisplayMode.REFRESH_RATE_UNKNOWN));
		} else {
			setWindowed(width, height);
		}

		createBufferStrategy(2);
		setTitle(title);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

}