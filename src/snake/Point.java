/* File: Point.java
 * Author: Duncan Tilley
 * The Point class represents the point or pellet that should be eaten to grow.
 */

package snake;

import java.awt.*;
import java.util.LinkedList;

public class Point {

	private int x, y;
	private int width, height;
	private int hScale, vScale;

	public Point(LinkedList<Body> body, Head head, int width, int height, int hScale, int vScale) {
		this.hScale = hScale;
		this.vScale = vScale;
		this.width = width;
		this.height = height;
		jump(body, head);
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public void jump(LinkedList<Body> body, Head head) {
		int x, y;
		do {
			x = (int)Math.floor((Math.random() * width));
			y = (int)Math.floor((Math.random() * height));
		} while (checkCollision(x, y, body, head));
		this.x = x;
		this.y = y;
	}

	private boolean checkCollision(int x, int y, LinkedList<Body> body, Head head) {
		// TODO debug this
		if (x == head.getX() && y == head.getY()) {
			return true;
		}
		for (Body bod : body) {
			if (x == bod.getX() && y == bod.getY()) {
				return true;
			}
		}
		return false;
	}

	public void draw(Graphics2D g) {
		g.setColor(new Color(176, 163, 68));
		g.fillOval(x * hScale, y * vScale, hScale, vScale);
	}

}
