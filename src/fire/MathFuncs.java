package fire;

/**
 *
 * @author David Marek <davidm@atrey.karlin.mff.cuni.cz>
 */
public class MathFuncs {

	public static double[] sinTable;
	public static double[] cosTable;

	static {
		sinTable = new double[365];
		cosTable = new double[365];

		for (int i = 0; i < 365; i++) {
			sinTable[i] = Math.sin(Math.toRadians(i));
			cosTable[i] = Math.cos(Math.toRadians(i));
		}
	}

	public static double sin(int i) {
		return sinTable[i];
	}

	public static double cos(int i) {
		return cosTable[i];
	}

}
