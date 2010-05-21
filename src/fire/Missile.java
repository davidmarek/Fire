package fire;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import javax.imageio.ImageIO;

/** Raketa vystřelená hráčem.
 *
 * @author David Marek <davidm@atrey.karlin.mff.cuni.cz>
 */
public class Missile implements GameObject{

	/** Stav rakety (letí, vybuchuje).
	 * 
	 */
	private enum State {
		MISSILE, EXPLOSION;
	}

	private final int SPEED = 15;
	private final int DMG = 20;

	private double x;
	private double y;
	private double diffX;
	private double diffY;

	private int heading;

	private GameMap map;
	private ObjectsList objectList;

	private boolean alive;

	private static LinkedList<BufferedImage> images;
	private static BufferedImage mis;

	private Sprite explosion;
	private Sprite missile;

	private State state;

	static {
		images = new LinkedList<BufferedImage>();
		try {
			images.add(ImageIO.read(new File("resources/explosion/ex1.png")));
			images.add(ImageIO.read(new File("resources/explosion/ex2.png")));
			images.add(ImageIO.read(new File("resources/explosion/ex3.png")));
			images.add(ImageIO.read(new File("resources/explosion/ex4.png")));
			images.add(ImageIO.read(new File("resources/explosion/ex5.png")));
			images.add(ImageIO.read(new File("resources/explosion/ex6.png")));
			images.add(ImageIO.read(new File("resources/explosion/ex7.png")));
			images.add(ImageIO.read(new File("resources/explosion/ex8.png")));
			images.add(ImageIO.read(new File("resources/explosion/ex9.png")));
			images.add(ImageIO.read(new File("resources/explosion/ex10.png")));
			images.add(ImageIO.read(new File("resources/explosion/ex11.png")));
			images.add(ImageIO.read(new File("resources/explosion/ex12.png")));

			mis = ImageIO.read(new File("resources/missile.png"));

		} catch(IOException e) {
			System.err.println("Can't find resources.");
		}
	}

	/** Vytvoření rakety.
	 *
	 * @param x X-ová souřadnice vypálení.
	 * @param y Y-ová souřadnice vypálení.
	 * @param heading Natočení rakety.
	 * @param map Herní mapa.
	 * @param objectsList Seznam překážek v mapě.
	 */
	public Missile(double x, double y, int heading, GameMap map, ObjectsList objectsList) {
		this.x = x;
		this.y = y;

		this.diffX = MathFuncs.cos(heading)*SPEED;
		this.diffY = MathFuncs.sin(heading)*SPEED;

		this.heading = heading;

		this.map = map;
		this.objectList = objectsList;

		this.alive = true;
		this.explosion = new Sprite(Sprite.Behavior.PLAY_ONCE);
		for (BufferedImage img : images) {
			explosion.addFrame(img, 100);
		}
		this.missile = new Sprite(Sprite.Behavior.PLAY_ONCE);
		this.missile.addFrame(mis, 10000);

		this.state = State.MISSILE;
	}

	/** Aktualizace střely.
	 *
	 * @param elapsedTime
	 */
	public void update(long elapsedTime) {
		switch (state) {
			case EXPLOSION:
				boolean endOfSprite = explosion.update(elapsedTime);
				if (endOfSprite) {
					alive = false;
				}
				break;

			case MISSILE:
				missile.update(elapsedTime);
				x -= diffX;
				y -= diffY;
				if (!map.freePlace((int)x, (int)y) || objectList.somethingOnCoords((int)x, (int)y)) {
					state = State.EXPLOSION;
					GameObject o = objectList.getObjectOnCoords((int)x, (int)y);
					if (o != null) {
						x = o.getX();
						y = o.getY();
						o.hurt(DMG);
					}
				}
				break;
		}
	}

	/** Získání obrázku rakety.
	 *
	 * @return Obrázek rakety.
	 */
	public Image getSprite() {
		switch (state) {
			case EXPLOSION:
				return explosion.getImage();
			case MISSILE:
			default:
				return missile.getImage();
		}
	}

	/** Zjisti, jestli raketa už vybouchla.
	 *
	 * @return Raketa ještě nevybouchla?
	 */
	public boolean isAlive() {
		return this.alive;
	}

	/** Vrať X-ovou souřadnici.
	 *
	 * @return X-ová souřadnice.
	 */
	public int getX() {
		return (int)x;
	}

	/** Vrať Y-ovou souřadnici.
	 *
	 * @return Y-ová souřadnice.
	 */
	public int getY() {
		return (int)y;
	}

	/** Vrať směr rakety.
	 *
	 * @return Směr rakety.
	 */
	public int getHeading() {
		return heading;
	}

	/** Vrať šířku rakety.
	 *
	 * @return Šířka rakety.
	 */
	public double getWidth() {
		return getSprite().getWidth(null);
	}

	/** Vrať výšku rakety.
	 *
	 * @return Výška rakety.
	 */
	public double getHeight() {
		return getSprite().getHeight(null);
	}

	/** Vrať zdraví rakety.
	 *
	 * Raketa nejde zničit, dokud sama do něčeho nenarazí a neexploduje, proto
	 * vždy vrací stejné zdraví.
	 *
	 * return Zdraví rakety.
	 */
	public int getHealth() {
		return 1;
	}

	/** Vrať maximální hodnotu zdraví.
	 *
	 * Raketa nejde zničit, takže vrací vždy stejně jako je aktuální zdraví.
	 *
	 * @return Maximální zdraví rakety.
	 */
	public int getMaxHealth() {
		return 1;
	}

	/** Způsob poškození.
	 *
	 * Raketa nejde zničit, takže tato metoda nic nedělá.
	 *
	 * @param dmg Velikost poškození.
	 */
	public void hurt(int dmg) {
	}

}
