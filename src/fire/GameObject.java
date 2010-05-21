package fire;

import java.awt.Image;

/** Herní objekt
 *
 * @author David Marek <davidm@atrey.karlin.mff.cuni.cz>
 */
public interface GameObject {

	/** Aktualizace herního objektu
	 * 
	 * @param elapsedTime Uběhlý čas od poslední aktualizace.
	 */
	public void update(long elapsedTime);

	/** Je objekt naživu?
	 * 
	 * @return Je objekt naživu?
	 */
	public boolean isAlive();

	/** Získat sprite objektu
	 * 
	 * @return Obrázek, jak vypadá objekt.
	 */
	public Image getSprite();

	/** Získat X-ovou souřadnici.
	 * 
	 * @return X-ová souřadnice.
	 */
	public int getX();

	/** Získat Y-ovou souřadnici.
	 * 
	 * @return Y-ová souřadnice.
	 */
	public int getY();

	/** Získat šířku objektu.
	 * 
	 * @return Šířka objektu.
	 */
	public double getWidth();

	/** Získat výšku objektu.
	 * 
	 * @return Výška objektu.
	 */
	public double getHeight();

	/** Získat natočení objektu.
	 * 
	 * @return Natočení objektu.
	 */
	public int getHeading();

	/** Získat počet životů.
	 * 
	 * @return Počet životů objektu.
	 */
	public int getHealth();

	/** Získat maximální počet životů.
	 * 
	 * @return Maximální počet životů.
	 */
	public int getMaxHealth();

	/** Ublížit objektu.
	 * 
	 * @param dmg Zranění způsobené objektu.
	 */
	public void hurt(int dmg);

}
