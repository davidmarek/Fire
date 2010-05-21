package fire;

/** Předpočítané matematické funkce.
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

	/** Předpočítaný sinus.
	 * 
	 * @param i Úhel ve stupních.
	 * @return Sinus zadaného úhlu.
	 */
	public static double sin(int i) {
		return sinTable[i];
	}

	/** Předpočítaný cosinus.
	 *
	 * @param i Úhel ve stupních.
	 * @return Cosinus zadaného úhlu.
	 */
	public static double cos(int i) {
		return cosTable[i];
	}

}
