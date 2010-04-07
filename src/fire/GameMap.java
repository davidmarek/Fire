package fire;

import java.awt.Image;
import java.awt.image.ImageObserver;
import java.awt.image.PixelGrabber;

/**
 *
 * @author David Marek <davidm@atrey.karlin.mff.cuni.cz>
 */
public class GameMap {

	private Image map;
	private int[] bitmap;
	private int w; 
	private int h;

	public GameMap(Image map, Image bitmapImg) {
		this.map = map;

		w = bitmapImg.getWidth(null);
		h = bitmapImg.getHeight(null);

		System.out.println("width: " + w + " height: " + h);

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

	private boolean isWhite(int pixel) {
		return (pixel & 0xffffff) == 0xffffff;
	}

	public void printPixels() {
		for (int i = 0; i < h; i++) {
			for (int j = 0; j < w; j++) {
				System.out.print(isWhite(bitmap[i*w + j]) ? 1 : 0);
			}
			System.out.println("");
		}
	}

	public boolean freePlace(int x, int y) {
		return isWhite(bitmap[y*w+x]);
	}

	public Image getMap() {
		return map;
	}
}
