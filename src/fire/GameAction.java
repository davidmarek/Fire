package fire;

/** Herní akce
 *
 * @author David Marek <davidm@atrey.karlin.mff.cuni.cz>
 */
public class GameAction {

	/** Chování akce.
	 * 
	 */
	public enum Behavior {
		NORMAL, DETECT_INITIAL_PRESS_ONLY;
	}

	/** Stav akce.
	 * 
	 */
	private enum State {
		/** Klávesa zmáčknuta. */
		PRESSED, 
		/** Klávesa uvolněna. */
		RELEASED,
		/** Klávesa držena. */
		WAITING_FOR_RELEASE;
	}

	private String name;
	private Behavior behavior;
	private State state;
	private int amount;

	/** Vytvoření nové akce s normálním chováním.
	 * 
	 * @param name Jméno akce.
	 */
	public GameAction(String name) {
		this.name = name;
		this.behavior = Behavior.NORMAL;
	}

	/** Vytvoření nové akce.
	 * 
	 * @param name Jméno akce.
	 * @param behavior Chování akce.
	 */
	public GameAction(String name, Behavior behavior) {
		this.name = name;
		this.behavior = behavior;
	}

	/** Jméno akce.
	 * 
	 * @return Jméno akce.
	 */
	public String getName() {
		return name;
	}

	/** Zresetování akce.
	 * 
	 */
	public void reset() {
		state = State.RELEASED;
		amount = 0;
	}

	/** Stisk klávesy.
	 *
	 */
	public synchronized void press() {
		press(1);
	}

	/** Více stisků klávesy.
	 *
	 * @param amount Počet stisků.
	 */
	public synchronized void press(int amount) {
		if (state != State.WAITING_FOR_RELEASE) {
			this.amount += amount;
			state = State.PRESSED;
		}
	}

	/** Uvolnění klávesy.
	 * 
	 */
	public synchronized void release() {
		state = State.RELEASED;
	}

	/** Kontrola, zda-li je klávesa stisknuta.
	 * 
	 * @return Klávesa stisknuta?
	 */
	public synchronized boolean isPressed() {
		return getAmount() != 0;
	}

	/** Počet stisknutí klávesy od poslední kontroly.
	 * 
	 * @return Počet stisknutí.
	 */
	public synchronized int getAmount() {
		int retVal = amount;
		if (retVal != 0) {
			if (state == State.RELEASED) {
				amount = 0;
			} else if (behavior.equals(Behavior.DETECT_INITIAL_PRESS_ONLY)) {
				state = State.WAITING_FOR_RELEASE;
				amount = 0;
			}
		}
		return retVal;
	}

}
