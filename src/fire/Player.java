package fire;
// TODO: Dodelat hrace

import java.awt.Image;
import java.util.EnumMap;
import java.util.Map;


/**
 *
 * @author David Marek <davidm@atrey.karlin.mff.cuni.cz>
 */
public class Player implements GameObject {


	//DEBUG
	public int newXp;
	public int newYp;

	public int newXLp;
	public int newYLp;
	//END OF DEBUG

	private enum State {
		STANDING, MOVING;
	}

	private final int FORWARD = 1;
	private final int LEFT = 1 << 1;
	private final int RIGHT = 1 << 2;
	private final int BACK = 1 << 3;

	private final int MAX_SPEED = 6;
	private final double ACCELERATION = 0.005;
	private final double DECCELERATION = 0.004;
	private final double EPSILON = 0.1;
	private final int ROTATE_SPEED = 3;

	private int moving;

	/** Souřadnice hráče. */
	private double x, y;
	/** Rychlost hráče. */
	private double speed;
	/** Natočení hráče. */
	private int rotation;
	/** Zdraví hráče. */
	private int health;

	private GameMap map;
	private ObjectsList objectList;

	/** Stav hrace */
	State state;
	/** Jednotlive sprity pro ruzne stavy. */
	private Map<State, Sprite> sprites = new EnumMap<State, Sprite>(State.class);

	/** Maximalni pocet zivotu. */
	private static final int MAX_HEALTH = 100;

	/** Vytvoř nového hráče
	 * 
	 * @param x Startovní x-ová pozice hráče
	 * @param y Startovní y-ová pozice hráče
	 */
	public Player(int x, int y, GameMap map, ObjectsList objectList) {
		loadSprites();
		this.state = State.STANDING;
		this.x = x;
		this.y = y;
		this.moving = 0;
		this.speed = 0;
		this.rotation = 90;
		this.map = map;
		this.health = 100;
		this.objectList = objectList;
	}

	/** Načtení spritů
	 * 
	 * Načte všechny sprity příslušné jednotlivých stavům hráče.
	 */
	private void loadSprites() {
		Sprite s = new Sprite(Sprite.Behavior.PLAY_ONCE);
		s.addFrame(Sprite.loadImage("resources/mujtank_1.gif"), 50);
		sprites.put(State.STANDING, s);

		s = new Sprite(Sprite.Behavior.PLAY_IN_LOOP);
		s.addFrame(Sprite.loadImage("resources/mujtank_1.gif"), 50);
		s.addFrame(Sprite.loadImage("resources/mujtank_2.gif"), 50);
		s.addFrame(Sprite.loadImage("resources/mujtank_3.gif"), 50);
		s.addFrame(Sprite.loadImage("resources/mujtank_4.gif"), 50);
		sprites.put(State.MOVING, s);
	}

	/** Vrať aktuální sprite.
	 * 
	 * Spočti, který sprite má být právě zobrazen a v jaké fázi a vrať jej.
	 * 
	 * @return Aktuální sprite.
	 */
	public Image getSprite() {
		Image ret = sprites.get(state).getImage();
		return ret;
	}

	/** Vrať x-ovou souřadnici.
	 * 
	 * @return x-ová souřadnice hráče.
	 */
	public int getX() {
		return (int)x;
	}

	/** Vrať y-ovou souřadnici.
	 * 
	 * @return y-ová souřadnice hráče.
	 */
	public int getY() {
		return (int)y;
	}

	public int getHeading() {
		return rotation;
	}

	public double getWidth() {
		return getSprite().getWidth(null);
	}

	public double getHeight() {
		return getSprite().getHeight(null);
	}

	/** Aktualizovat hráče.
	 * 
	 * @param elapsedTime Uběhlý čas od poslední aktualizace.
	 */
	public void update(long elapsedTime) {
		double accel;
		if ((moving & FORWARD) > 0) {
			if (speed > 0) {
				accel = ACCELERATION;
			} else {
				accel = DECCELERATION;
			}
			speed = (speed + accel* elapsedTime);
		} else if ((moving & BACK) > 0) {
			if (speed > 0) {
				accel = DECCELERATION;
			} else {
				accel = ACCELERATION;
			}
			speed = (speed - accel* elapsedTime);
		} else {
			speed = Math.signum(speed) * ((Math.abs(speed) - DECCELERATION * elapsedTime));
			if (Math.abs(speed) < EPSILON) { speed = 0; }
		}

		if ((moving & LEFT) > 0) {
			rotation -= ROTATE_SPEED;
			if (rotation < 0) { rotation = 360; }
			moving &= ~LEFT;
		} else if ((moving & RIGHT) > 0) {
			rotation += ROTATE_SPEED;
			if (rotation >= 360) { rotation = 0; }
			moving &= ~RIGHT;
		}

		if (Math.abs(speed) > EPSILON) { state = State.MOVING; }
		else {state = State.STANDING; }

		if (Math.abs(speed) > MAX_SPEED) { speed = Math.signum(speed)*MAX_SPEED; }
		double newx = x - MathFuncs.cos(rotation)*speed;
		double newy = y - MathFuncs.sin(rotation)*speed;

		newXp = (int) (x + getWidth()/2  - (getWidth()/2 + 17) * MathFuncs.cos(rotation));
		newYp = (int) (y + getHeight()/2 - (getHeight()/2 + 17) * MathFuncs.sin(rotation));

		newXLp = (int) (x + getWidth()/2  - (getWidth()/2 - 5) * MathFuncs.cos(rotation-90 > 0 ? rotation-90 : 360 + rotation - 90));
		newYLp = (int) (y + getHeight()/2 - (getHeight()/2 - 5) * MathFuncs.sin(rotation-90 > 0 ? rotation-90 : 360 + rotation - 90));

		if (map.freePlace((int)newx, (int)newy) && !objectList.somethingOnCoords(newXp, newYp) &&
			(!objectList.somethingOnCoords(newXLp, newYLp) || objectList.getObjectOnCoords(newXLp, newYLp).equals(this))) {
			x = newx;
			y = newy;
		} else {
			speed = 0;
		}

		// Slouzi k zjisteni, ze uz nejaka akce skoncila (napr. exploze)
		boolean endOfSprite = sprites.get(state).update(elapsedTime);
	}

	public void forward() {
		moving &= ~BACK;
		moving |= FORWARD;
	}

	public void back() {
		moving &= ~FORWARD;
		moving |= BACK;
	}

	public void left() {
		moving &= ~RIGHT;
		moving |= LEFT;
	}

	public void right() {
		moving &= ~LEFT;
		moving |= RIGHT;
	}

	public void dontMove() {
		moving &= ~(FORWARD | BACK);
	}
	
	public void dontSteer() {
		moving &= ~(LEFT | RIGHT);
	}

	public boolean isAlive() {
		return health > 0;
	}

	public Missile shoot() {
		Missile m = new Missile(newXp, newYp, this.rotation, this.map, this.objectList);
		return m;
	}
}
