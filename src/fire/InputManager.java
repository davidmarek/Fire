package fire;

import java.awt.Component;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.LinkedList;

/** Správce vstupu.
 *
 * Zpracovává události stisků kláves.
 *
 * @author David Marek <davidm@atrey.karlin.mff.cuni.cz>
 */
public class InputManager implements KeyListener {

	private static final int NUM_KEY_CODES = 600;

	private GameAction[] keyActions = new GameAction[NUM_KEY_CODES];

	/** Vytvoření InputManageru.
	 * 
	 * @param comp Komponenta rozhrání, jejíž zprávy mají být odchytávány.
	 */
	public InputManager(Component comp) {
		comp.addKeyListener(this);
		comp.setFocusTraversalKeysEnabled(false);
	}

	/** Přiřazení herní akce ke klávese.
	 * 
	 * @param action Herní akce.
	 * @param keyCode Klávesa.
	 */
	public void mapToKey(GameAction action, int keyCode) {
		keyActions[keyCode] = action;
	}

	/** Odstranění všech kláves přiřazených k herní akci.
	 * 
	 * @param action Herní akce.
	 */
	public void clearMap(GameAction action) {
		for (int i = 0; i < keyActions.length; i++) {
			if (keyActions[i] == action) {
				keyActions[i] = null;
			}
		}
		action.reset();
	}

	/** Získání všech kláves, které jsou přiřazeny herní akci. 
	 * 
	 * @param action Herní akce, jejíž přiřazené klávesy hledáme.
	 * @return Seznam kláves.
	 */
	public LinkedList<String> getMaps(GameAction action) {
		LinkedList<String> list = new LinkedList<String>();

		for (int i = 0; i < keyActions.length; i++) {
			if (keyActions[i] == action) {
				list.add(getKeyName(i));
			}
		}

		return list;
	}

	/** Vynulování všech herních akcí.
	 * 
	 */
	public void resetAllGameActions() {
		for (int i = 0; i < keyActions.length; i++) {
			if (keyActions[i] != null) {
				keyActions[i].reset();
			}
		}
	}

	/** Získání herní akce pro stisklou klávesu.
	 *
	 * @param e Stisknutá klávesa.
	 * @return Herní akce přiřazená stisknuté klávese.
	 */
	private GameAction getKeyAction(KeyEvent e) {
		int keyCode = e.getKeyCode();
		if (keyCode < keyActions.length) {
			return keyActions[keyCode];
		} else {
			return null;
		}
	}

	/** Získání jména stisknuté klávesy.
	 * 
	 * @param keyCode Stisknutá klávesa.
	 * @return Jméno stisknuté klávesy.
	 */
	public static String getKeyName(int keyCode) {
		return KeyEvent.getKeyText(keyCode);
	}

	/** Reakce na stisknutou klávesu.
	 * 
	 * @param e Stisknutá klávesa.
	 */
	public void keyTyped(KeyEvent e) {
		e.consume();
	}

	/** Reakce na stisk klávesy.
	 * 
	 * @param e Stisklá klávesa.
	 */
	public void keyPressed(KeyEvent e) {
		GameAction action = getKeyAction(e);
		if (action != null) {
			action.press();
		}
		e.consume();
	}

	/** Reakce na uvolnění klávesy.
	 * 
	 * @param e Uvolněná klávesa.
	 */
	public void keyReleased(KeyEvent e) {
		GameAction action = getKeyAction(e);
		if (action != null) {
			action.release();
		}
		e.consume();
	}

}
