/* File: Body.java
 * Author: Duncan Tilley
 * The Body class represents a single cell of the snake's body.
 */

package snake;

import java.awt.*;

public class Body {

	private int x, y;
	private int hScale, vScale;

	public Body(int x, int y, int hScale, int vScale) {
		this.x = x;
		this.y = y;
		this.hScale = hScale;
		this.vScale = vScale;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public void draw(Graphics2D g) {
		g.setColor(new Color(208, 178, 123));
		g.fillOval(x * hScale, y * vScale, hScale, vScale);
	}

}
