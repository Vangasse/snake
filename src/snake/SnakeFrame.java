/* File: SnakeFrame.java
 * Author: Duncan Tilley
 * A JFrame that is responsible for drawing and updating the game. This is where everything happens.
 */

package snake;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferStrategy;
import java.util.LinkedList;

public class SnakeFrame extends JFrame implements Runnable {

	// animation and update loop constants
	private static final int FPS = 8;
	private static final long PERIOD_NS = 1000000000L / FPS;
	private static final double PERIOD_S = 1.0 / FPS;
	private static final int NO_DELAYS_PER_YIELD = 16;
	private static final int MAX_FRAME_SKIPS = 4;
	private static final int NUM_BUFFERS = 2;

	// animation and update loop variables
	private Thread animator;
	private volatile boolean running;
	private Graphics2D g;
	private GraphicsDevice gd;
	private BufferStrategy bufferStrat;
	private int width, height;

	// game constants
	private static final int F_WIDTH = 48; // columns on the field
	private static final int F_HEIGHT = 27; // rows on the field
	private static final int INITIAL_LENGTH = 5;

	// game variables
	private int hScale, vScale;
	private int nextDirection;
	private Head head;
	private LinkedList<Body> body;
	private Point point;
	private int bodyLength;
	private int actualLength;
	private boolean firstFrame;
	private boolean gameOver;

	public SnakeFrame() {
		initFullscreen();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		running = false;
		addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				keyPress(e);
			}
		});
		start();
	}

	private void initFullscreen() {
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		gd = ge.getDefaultScreenDevice();
		if (gd.isFullScreenSupported()) {
			setUndecorated(true);
			setIgnoreRepaint(true);
			setResizable(false);
			gd.setFullScreenWindow(this);
			width = getBounds().width;
			height = getBounds().height;
			setupBuffer();
		} else {
			System.out.println("Error: fullscreen not supported");
		}
	}

	private void setupBuffer() {
		createBufferStrategy(NUM_BUFFERS);
		bufferStrat = getBufferStrategy();
	}

	public void start() {
		if (!running || animator == null) {
			hScale = width / F_WIDTH;
			vScale = height / F_HEIGHT;
			head = new Head(F_WIDTH / 2, F_HEIGHT / 2, F_WIDTH, F_HEIGHT, hScale, vScale);
			body = new LinkedList<>();
			point = new Point(body, head, F_WIDTH, F_HEIGHT, hScale, vScale);
			bodyLength = 0;
			actualLength = INITIAL_LENGTH;
			firstFrame = true;
			gameOver = false;
			animator = new Thread(this);
			animator.start();
		}
	}

	public void stop() {
		running = false;
	}

	@Override
	public void run() {
		long sleepTime, startTime, endTime, timeDiff;
		long overSleep = 0L;
		long excess = 0L;
		int noDelays = 0;
		running = true;

		startTime = System.nanoTime();
		while (running) {
			updateGame();
			updateScreen();
			endTime = System.nanoTime();
			timeDiff = endTime - startTime;
			sleepTime = PERIOD_NS - timeDiff - overSleep;
			if (sleepTime > 0) {
				try {
					Thread.sleep(sleepTime / 1000000); // convert ns to ms
				} catch (InterruptedException ie) {}
				overSleep = (System.nanoTime() - endTime) - sleepTime;
			} else {
				// frame took too long, save the excess time
				excess -= sleepTime;
				overSleep = 0L;
				noDelays++;
				// if too many frames were drawn without sleep, yeild a bit
				if (noDelays >= NO_DELAYS_PER_YIELD) {
					Thread.yield();
					noDelays = 0;
				}
			}
			// catch up any lost frames
			int skips = 0;
			while (excess >= PERIOD_NS && skips < MAX_FRAME_SKIPS) {
				updateGame();
				excess -= PERIOD_NS;
				skips++;
			}
			startTime = System.nanoTime();
		}
		if (gd != null) {
			gd.setFullScreenWindow(null);
		}
		System.exit(0);
	}

	private void updateGame() {
		if (!gameOver) {
			if (nextDirection != ((head.getDirection() + 2) % 4)) {
				head.setDirection(nextDirection);
			}
			int hPrevX = head.getX();
			int hPrevY = head.getY();
			int bPrevX, bPrevY;
			if (bodyLength > 0) {
				bPrevX = body.getFirst().getX();
				bPrevY = body.getFirst().getY();
			} else {
				bPrevX = hPrevX;
				bPrevY = hPrevY;
			}
			if (!firstFrame) {
				if (bodyLength > 0) {
					body.removeFirst();
					body.addLast(new Body(hPrevX, hPrevY, hScale, vScale));
				}
				int result = head.move(body, point);
				if (result == Head.GROW) {
					actualLength++;
					point.jump(body, head);
				} else if (result == Head.CRASH) {
					gameOver = true;
				}
			} else {
				firstFrame = false;
			}
			if (bodyLength < actualLength) {
				body.addFirst(new Body(bPrevX, bPrevY, hScale, vScale));
				bodyLength++;
			}
		}
	}

	private void updateScreen() {
		try {
			g = (Graphics2D)bufferStrat.getDrawGraphics();
			render(g);
			g.dispose();
			if (!bufferStrat.contentsLost()) {
				bufferStrat.show();
			} else {
				System.out.println("Warning: buffer contents lost");
			}
		} catch (Exception e) {
			e.printStackTrace();
			running = false;
		}
	}

	private void render(Graphics2D g) {
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setColor(new Color(166, 205, 208));
		g.fillRect(0, 0, width, height);
		for (Body bod : body) {
			bod.draw(g);
		}
		point.draw(g);
		head.draw(g);
	}

	private void keyPress(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			nextDirection = Head.RIGHT;
		} else if (e.getKeyCode() == KeyEvent.VK_UP) {
			nextDirection = Head.UP;
		} else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			nextDirection = Head.LEFT;
		} else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
			nextDirection = Head.DOWN;
		} else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			running = false;
		}
	}

}
