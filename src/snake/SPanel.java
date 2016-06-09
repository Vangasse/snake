package snake;

import java.awt.*;
import java.awt.event.*;
import java.util.Vector;

public class SPanel extends Canvas implements Runnable {
    
    private SHead head;
    private Vector<SBody> body;
    private Vector<SWall> walls;
    private SPill pill;
    
    private int fWidth, fHeight;
    private int prevDir;
    private int fpsSleep;
    
    private int score;
    
    private final int       BODY_START = 3;
    private final int       FPS        = 10;
    private final boolean   BOXED      = true;
    
    Thread game;
    boolean gameIsAlive;
    
    public SPanel(int fieldWidth, int fieldHeight) {
		setFocusable(true);
		requestFocus();
        fWidth = fieldWidth;
        fHeight = fieldHeight;
        setSize(SFrame.CELL_SIZE*fWidth, SFrame.CELL_SIZE*fHeight);
        score = 0;
        prevDir = SHead.RIGHT;
        fpsSleep = Math.round(1000 / FPS);
        head = new SHead(4, 4, SHead.RIGHT);
        body = new Vector<>();
        for (int i = 0; i < BODY_START; i++)
            body.add(new SBody(4, 4));
        walls = new Vector<>();
        if (BOXED) {
            for (int i = 0; i < fWidth; i++) {
                walls.add(new SWall(i, 0));
                walls.add(new SWall(i, fHeight-1));
            }
            for (int i = 1; i < fHeight-1; i++) {
                walls.add(new SWall(0, i));
                walls.add(new SWall(fWidth-1, i));
            }
        }
        pill = new SPill();
        moveToRandom(pill);
        setBackground(new Color(200, 255, 170));
        addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent e) {}

            @Override
            public void keyPressed(KeyEvent e) {
                int key = e.getKeyCode();
                if (key == KeyEvent.VK_RIGHT && prevDir != SHead.LEFT)
                    head.faceDirection(SHead.RIGHT);
                else if (key == KeyEvent.VK_UP && prevDir != SHead.DOWN)
                    head.faceDirection(SHead.UP);
                else if (key == KeyEvent.VK_LEFT && prevDir != SHead.RIGHT)
                    head.faceDirection(SHead.LEFT);
                else if (key == KeyEvent.VK_DOWN && prevDir != SHead.UP)
                    head.faceDirection(SHead.DOWN);
            }

            @Override
            public void keyReleased(KeyEvent e) {}
            
        });
        addFocusListener(new FocusListener() {

            @Override
            public void focusGained(FocusEvent e) {}

            @Override
            public void focusLost(FocusEvent e) {}
            
        });
        game = new Thread(this);
        game.start();
    }
    
    @Override
    public void update(Graphics g) {
        Graphics bufferG;
        Image buffer;
        Dimension dim = getSize();
        buffer = createImage(dim.width, dim.height);
        bufferG = buffer.getGraphics();
        bufferG.setColor(getBackground());
        bufferG.fillRect(0, 0, dim.width, dim.height);
        bufferG.setColor(getForeground());
        paint(bufferG);
        g.drawImage(buffer, 0, 0, this);
    }
        
    @Override
    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(new Color(20, 20, 100));       // HEAD FILL COLOR
        g2d.fill(head.sprite());
        g2d.setColor(new Color(100, 100, 180));     // BODY FILL COLOR
        for (SBody segment : body) {
            g2d.fill(segment.sprite());
        }
        g2d.setColor(new Color(20, 20, 20));        // WALL FILL COLOR
        for (SWall wall : walls) {
            g2d.fill(wall.sprite());
        }
        g2d.setColor(new Color(200, 100, 20));      // PILL FILL COLOR
        g2d.fill(pill.sprite());
        g2d.setColor(new Color(180, 80, 0));        // PILL BORDER COLOR
        g2d.draw(pill.sprite());
        g2d.setColor(new Color(80, 80, 160));       // BODY BORDER COLOR
        for (SBody segment : body) {
            g2d.draw(segment.sprite());
        }
        g2d.setColor(new Color(0, 0, 0));           // WALL BORDER COLOR
        for (SWall wall : walls) {
            g2d.draw(wall.sprite());
        }
        g2d.setColor(new Color(0, 0, 80));          // HEAD BORDER COLOR
        g2d.draw(head.sprite());
    }

    @Override
    public void run() {
        gameIsAlive = true;
        while (gameIsAlive) {
            try {
                Thread.sleep(fpsSleep);
            } catch (InterruptedException ie) {}
            gameStep();
            repaint();
        }
    }
    
    private void gameStep() {
        int bodyLength = body.size() - 1;
        for (int i = bodyLength; i >= 0; i--) {
            if (i == 0) {
                body.elementAt(i).moveTo(head.getX(), head.getY());
            } else {
                int x = body.elementAt(i - 1).getX();
                int y = body.elementAt(i - 1).getY();
                body.elementAt(i).moveTo(x, y);
            }
        }
        if (head.getDirection() == SHead.RIGHT && head.getX()+2 > fWidth)
            head.moveTo(0, head.getY());
        else if (head.getDirection() == SHead.UP && head.getY()-1 < 0)
            head.moveTo(head.getX(), fHeight-1);
        else if (head.getDirection() == SHead.LEFT && head.getX()-1 < 0)
            head.moveTo(fWidth-1, head.getY());
        else if (head.getDirection() == SHead.DOWN && head.getY()+2 > fHeight)
            head.moveTo(head.getX(), 0);
        else
            head.move();
        Entity collision = getCollisionOf(head);
        if (collision == pill) {
            moveToRandom(pill);
            body.add( new SBody(
                    body.lastElement().getX(), body.lastElement().getY()) );
            score += 6;
            updateScore();
        } else if (collision instanceof SBody) {
            killSnake();
        } else if (collision instanceof SWall) {
            killSnake();
        }
        prevDir = Math.abs(head.getDirection());
    }
    
    private Entity getCollisionOf(Entity object) {
        int x = object.getX();
        int y = object.getY();
        if (x == pill.getX() && y == pill.getY()
                && !object.equals(pill))
            return pill;
        for (SBody segment : body) {
            if (x == segment.getX() && y == segment.getY()
                    && !object.equals(segment))
                return segment;
        }
        for (SWall wall : walls) {
            if (x == wall.getX() && y == wall.getY()
                    && !object.equals(wall))
                return wall;
        }
        return null;
    }
    
    private void moveToRandom(Entity object) {
        do {
            object.moveTo( (int) Math.round(Math.random()*(fWidth-1))
                         , (int) Math.round(Math.random()*(fHeight-1)) );
        } while (getCollisionOf(object) != null);
    }
    
    private void updateScore() {
        System.out.println("" + score);
    }
    
    private void killSnake() {
        endGame();
    }
    
    private void endGame() {
        gameIsAlive = false;
    }
    
}
