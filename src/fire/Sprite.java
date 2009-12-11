package fire;

import java.awt.Image;
import java.util.ArrayList;

/** Grafický prvek, který je možné animovat.
 *
 * @author David Marek <davidm@atrey.karlin.mff.cuni.cz>
 */
public class Sprite {

	/** Jeden snímek animace.
	 * 
	 */
	private class Frame {
		private Image image;
		private long endTime;

		public Frame(Image img, long end) {
			image = img;
			endTime = end;
		}

		/** Obrázek snímku.
		 * 
		 * @return Obrázek snímku.
		 */
		public Image getImage() {
			return image;
		}
		
		/** Celková délka animace i s tímto snímkem.
		 * 
		 * @return Celková délka po tento snímek.
		 */
		public long getEndTime() {
			return endTime;
		}
	}

	/** Chování animace.
	 * 
	 */
	public enum Behavior {
		/** Přehrávat ve smyčce. */
		PLAY_IN_LOOP,
		/** Přehrát jen jednou. */
		PLAY_ONCE;
	}

	private Behavior behavior;

	private ArrayList<Frame> frames;
	private int currentFrameIndex;

	private long totalTime;
	private long animTime;

	/** Vytvoření spritu.
	 * 
	 * @param b Chování spritu.
	 */
	public Sprite(Behavior b) {
		totalTime = 0;
		frames = new ArrayList<Frame>();
		behavior = b;
	}

	/** Přidat snímek do animace.
	 * 
	 * @param image Obrázek.
	 * @param duration Jak dlouho má být zobrazen.
	 */
	public void addFrame(Image image, long duration) {
		frames.add(new Frame(image, totalTime+duration));
		totalTime += duration;
	}

	/** Spustit animaci.
	 * 
	 */
	public void start() {
		animTime = 0;
		currentFrameIndex = 0;
	}

	/** Aktualizuje animaci.
	 * 
	 * Vybere se následující snímek. Pokud již animace doběhla a
	 * je vybrané chování, že se má animace přehrát jen jednou,
	 * tak se vrátí true. Jinak se animace přehrává stále dokola.
	 * 
	 * @param elapsedTime Již uběhlý čas.
	 * @return Byl dosažen konec a je nastavené,
	 *	že se má animace přehrát jen jednou.
	 */
	public synchronized boolean update(long elapsedTime) {
		if (frames.size() > 2) {
			animTime += elapsedTime;

			// Pokud jsem na konci animace, tak buď jedu odznova, anebo končím.
			if (animTime >= totalTime) {
				animTime %= totalTime;
				currentFrameIndex = 0;
				if (behavior == Behavior.PLAY_ONCE) {
					return true;
				}
			}

			// Najdu snímek, který má být zobrazen.
			while (animTime > frames.get(currentFrameIndex).getEndTime()) {
				currentFrameIndex++;
			}

		}
		return false;
	}

	/** Obrázek, který má právě být zobrazen.
	 * 
	 * @return Aktuální obrázek v animaci.
	 */
	public synchronized Image getImage() {
		if (frames.size() == 0) {
			return null;
		} else {
			return frames.get(currentFrameIndex).getImage();
		}
	}

}
