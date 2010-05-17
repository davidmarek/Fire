package fire;

import java.util.LinkedList;

/**
 *
 * @author David Marek <davidm@atrey.karlin.mff.cuni.cz>
 */
public class ObjectsList {

	LinkedList<GameObject> objects;

	public ObjectsList() {
		objects = new LinkedList<GameObject>();
	}

	public void add(GameObject go) {
		objects.add(go);
	}

	public void remove(GameObject go) {
		objects.remove(go);
	}

	public boolean somethingOnCoords(int x, int y) {
		for (GameObject o : objects) {
			if (o.getX() <= x && x <= o.getX()+o.getWidth() &&
				o.getY() <= y && y <= o.getY()+o.getHeight()) {
				return true;
			}
		}
		return false;
	}

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
