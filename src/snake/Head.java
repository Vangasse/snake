/* File: Head.java
 * Author: Duncan Tilley
 * The Head class represents the head of the snake.
 */

package snake;

import java.awt.*;
import java.util.LinkedList;

public class Head {

	public static final int NONE = 0;
	public static final int GROW = 1;
	public static final int CRASH = 2;

	public static final int RIGHT = 0;
	public static final int UP = 1;
	public static final int LEFT = 2;
	public static final int DOWN = 3;

	private int x, y;
	private int direction;
	private int width, height;
	private int hScale, vScale;

	public Head(int x, int y, int width, int height, int hScale, int vScale) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.hScale = hScale;
		this.vScale = vScale;
		this.direction = RIGHT;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getDirection() {
		return direction;
	}

	public void setDirection(int direction) {
		if (direction >= 0 && direction <= 3) {
			this.direction = direction;
		}
	}

	public int move(LinkedList<Body> body, Point point) {
		if (direction == RIGHT) {
			x++;
			if (x >= width) {
				x = 0;
			}
		} else if (direction == LEFT) {
			x--;
			if (x < 0) {
				x = width - 1;
			}
		} else if (direction == UP) {
			y--;
			if (y < 0) {
				y = height - 1;
			}
		} else if (direction == DOWN) {
			y++;
			if (y >= height) {
				y = 0;
			}
		}
		if (x == point.getX() && y == point.getY()) {
			return GROW;
		} else if (checkBodyCollision(body)) {
			return CRASH;
		} else {
			return NONE;
		}
	}

	private boolean checkBodyCollision(LinkedList<Body> body) {
		for (Body bod : body) {
			if (x == bod.getX() && y == bod.getY()) {
				return true;
			}
		}
		return false;
	}

	public void draw(Graphics2D g) {
		g.setColor(new Color(172, 97, 84));
		g.fillOval(x * hScale, y * vScale, hScale, vScale);
	}

}
