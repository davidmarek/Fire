package fire;

/**
 *
 * @author David Marek <davidm@atrey.karlin.mff.cuni.cz>
 */
public class Missile implements GameObject{

	private final int SPEED = 8;

	private double x;
	private double y;
	private double heading;

	private GameMap map;

	private boolean alive;

	public Missile(double x, double y, double heading, GameMap map) {
		this.x = x;
		this.y = y;
		this.heading = heading;

		this.map = map;

		this.alive = true;
	}

	public void update(long elapsedTime) {
	
	}

	public boolean isAlive() {
		return this.alive;
	}

}
