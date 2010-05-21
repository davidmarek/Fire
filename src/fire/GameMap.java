package fire;

import java.awt.Image;
import java.awt.image.ImageObserver;
import java.awt.image.PixelGrabber;

/** Herní mapa.
 *
 * Slouží pro zjišťování informací o terénu, zda-li je nebo není průchozí.
 *
 * @author David Marek <davidm@atrey.karlin.mff.cuni.cz>
 */
public class GameMap {

	private Image map;
	private int[] bitmap;
	private int w; 
	private int h;

	/** Vytvoření mapy.
	 *
	 * Vytvoří si pole hodnot určujících pro každý pixel, zda-li
	 * je nebo není průchozí.
	 * 
	 * @param map Standardní mapa, která se vykresluje.
	 * @param bitmapImg Maska mapy.
	 */
	public GameMap(Image map, Image bitmapImg) {
		this.map = map;

		w = bitmapImg.getWidth(null);
		h = bitmapImg.getHeight(null);

		bitmap = new int[w*h];
		PixelGrabber pg = new PixelGrabber(bitmapImg, 0, 0, w, h, bitmap, 0, w);
		
		boolean done = false;
		while (!done) {
			try {
				pg.grabPixels();
			} catch (InterruptedException e) {
				continue;
			}
			done = true;
		}

		if ((pg.getStatus() & ImageObserver.ABORT) != 0) {
			System.err.println("image fetch aborted or errored");
			return;
		}
	}

	/** Zjištění, zda-li je daný pixel bílý (průchozí).
	 * 
	 * @param pixel Pixel, který se má zkoumat.
	 * @return Je zadaný pixel bílý(průchozí)?
	 */
	private boolean isWhite(int pixel) {
		return (pixel & 0xffffff) == 0xffffff;
	}

	/** Zobrazení bitové mapy.
	 *
	 */
	public void printPixels() {
		for (int i = 0; i < h; i++) {
			for (int j = 0; j < w; j++) {
				System.out.print(isWhite(bitmap[i*w + j]) ? 1 : 0);
			}
			System.out.println("");
		}
	}

	/** Kontrola, jestli jsou zadané souřadnice průchozí.
	 *
	 * @param x X-ová hodnota souřadnice.
	 * @param y Y-ová hodnota souřadnice.
	 * @return Je souřadnice průchozí?
	 */
	public boolean freePlace(int x, int y) {
		return isWhite(bitmap[y*w+x]);
	}

	/** Získání mapy pro vykreslení.
	 *
	 * @return Herní mapa.
	 */
	public Image getMap() {
		return map;
	}
}
