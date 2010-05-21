package fire;

import java.util.LinkedList;

/** Struktura pro zjišťování, jestli na daných souřadnicých je nějaký objekt.
 *
 * Pro více objektů by to chtělo například quad tree, ale takhle je to zatím
 * dostačující.
 *
 * @author David Marek <davidm@atrey.karlin.mff.cuni.cz>
 */
public class ObjectsList {

	/** Seznam objektů */
	LinkedList<GameObject> objects;

	public ObjectsList() {
		objects = new LinkedList<GameObject>();
	}

	/** Přidání nového objektu, který by mohl překážet.
	 * 
	 * @param go Herní objekt.
	 */
	public void add(GameObject go) {
		objects.add(go);
	}

	/** Odstranění objektu ze hry.
	 *
	 * @param go Herní objekt.
	 */
	public void remove(GameObject go) {
		objects.remove(go);
	}

	/** Je něco na zadaných souřadnicích?
	 * 
	 * @param x X-ová souřadnice.
	 * @param y Y-ová souřadnice.
	 * @return Je něco na zadaných souřadnicích?
	 */
	public boolean somethingOnCoords(int x, int y) {
		for (GameObject o : objects) {
			if (o.getX() <= x && x <= o.getX()+o.getWidth() &&
				o.getY() <= y && y <= o.getY()+o.getHeight()) {
				return true;
			}
		}
		return false;
	}

	/** Který objekt je na zadaných souřadnicých?
	 * 
	 * @param x X-ová souřadnice.
	 * @param y Y-ová souřadnice.
	 * @return Objekt, který leží na zadaných souřadnicích nebo null.
	 */
	public GameObject getObjectOnCoords(int x, int y) {
		for (GameObject o : objects) {
			if (o.getX() <= x && x <= o.getX()+o.getWidth() &&
				o.getY() <= y && y <= o.getY()+o.getHeight()) {
				return o;
			}
		}
		return null;
	}

}
