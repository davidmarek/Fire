package fire;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/** Hlavní budova, která skrývá vlajku.
 *
 * @author David Marek <davidm@atrey.karlin.mff.cuni.cz>
 */
public class Base implements GameObject {

	/**
	 * 
	 */
	enum State {
		STANDING, DESTROYED_FLAG, DESTROYED;
	}

	private int health;
	private final int MAX_HEALTH = 200;
	private State state;

	private int x;
	private int y;

	private Image baseImage;
	private Image destroyedImage;
	private Image destroyedFlagImage;

	private Player enemy;
	private Player friend;
	private final int RADIUS = 40;

	/** Vytvoř budovu.
	 *
	 * @param x X-ová souřadnice.
	 * @param y Y-ová souřadnice.
	 * @param f Přátelský hráč.
	 * @param e Nepřátelský hráč.
	 * @throws IOException
	 */
	public Base(int x, int y, Player f, Player e) throws IOException {
		health = MAX_HEALTH;
		state = State.STANDING;
		baseImage = ImageIO.read(new File("resources/base.png"));
		destroyedImage = ImageIO.read(new File("resources/destroyed.png"));
		destroyedFlagImage = ImageIO.read(new File("resources/destroyedFlag.png"));

		this.x = x;
		this.y = y;
		friend = f;
		enemy = e;
	}

	/** Aktualizuj objekt.
	 *
	 * Budova si kontroluje jestli je někdo v její přítomnosti, popřípadě s ním
	 * interaguje.
	 *
	 * @param elapsedTime Uběhlý čas.
	 */
	public void update(long elapsedTime) {
		if (state == State.DESTROYED_FLAG &&
			(Math.abs(enemy.getX()+enemy.getWidth()/2 - x - getWidth()/2) < RADIUS) &&
			(Math.abs(enemy.getY()+enemy.getHeight()/2 - y - getWidth()/2) < RADIUS)) {
			enemy.giveFlag();
			state = State.DESTROYED;
		} else if (friend.hasFlag() &&
			(Math.abs(friend.getX()+friend.getWidth()/2 - x - getWidth()/2) < RADIUS) &&
			(Math.abs(friend.getY()+friend.getHeight()/2 - y - getWidth()/2) < RADIUS)) {
			enemy.loose();
		}
	}

	/** Zjisti, jestli je naživu.
	 *
	 * Budova z mapy nemizí, pouze se může změnit na zříceninu.
	 *
	 * @return Vždy true.
	 */
	public boolean isAlive() {
		return true;
	}

	/** Získej aktuální obrázek.
	 *
	 * @return Aktuální obrázek.
	 */
	public Image getSprite() {
		switch (state) {
			case DESTROYED:
				return destroyedImage;
			case DESTROYED_FLAG:
				return destroyedFlagImage;
			default:
			case STANDING:
				return baseImage;
		}
	}

	/** Získej X-ovou souřadnici.
	 *
	 * @return X-ová souřadnice.
	 */
	public int getX() {
		return x;
	}

	/** Získej Y-ovou souřadnici,
	 *
	 * @return Y-ová souřadnice.
	 */
	public int getY() {
		return y;
	}

	/** Získej šířku.
	 *
	 * @return Šířka objektu.
	 */
	public double getWidth() {
		return baseImage.getWidth(null);
	}

	/** Získej výšku.
	 *
	 * @return Výška objektu.
	 */
	public double getHeight() {
		return baseImage.getHeight(null);
	}

	/** Získej natočení.
	 *
	 * Budova se nehýbe, takže nemusíme řešit natočení.
	 *
	 * @return Natočení budovy.
	 */
	public int getHeading() {
		return 0;
	}

	/** Získej počet životů.
	 *
	 * @return Počet životů.
	 */
	public int getHealth() {
		return health;
	}

	/** Získej maximální počet životů.
	 *
	 * @return Maximální počet životů.
	 */
	public int getMaxHealth() {
		return MAX_HEALTH;
	}

	/** Způsob zranění.
	 *
	 * @param dmg Způsobené poškození.
	 */
	public void hurt(int dmg) {
		if (health < 0) {
			state = State.DESTROYED_FLAG;
		} else {
			health -= dmg;
		}
	}

}
