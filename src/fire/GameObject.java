/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package fire;

import java.awt.Image;

/**
 *
 * @author david
 */
public interface GameObject {

	public void update(long elapsedTime);

	public boolean isAlive();

	public Image getSprite();

	public int getX();

	public int getY();

	public double getWidth();

	public double getHeight();

	public double getHeading();

}
