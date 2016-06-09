package snake;

import java.awt.geom.*;

public class SHead implements Entity {
    
    private Rectangle2D.Float sprite;
    private int xPos, yPos;
    private int direction;
    private int cell = SFrame.CELL_SIZE;
    
    public static final int RIGHT =   0;
    public static final int UP    =  90;
    public static final int LEFT  = 180;
    public static final int DOWN  = 270;
    
    public SHead(int x, int y, int dir) {
        xPos = x;
        yPos = y;
        direction = dir;
            //  incorrect direction is not tested for, as
            //  this method is internally controlled by the programmer
            //  and incorrect parameters are easily avoidable
        resetSprite();
    }
    
    public SHead() {
        this(0, 0, RIGHT);
    }
    
    public void faceDirection(int dir) {
        switch (dir) {
            case RIGHT :
                direction = RIGHT;
                break;
            case UP :
                direction = UP;
                break;
            case LEFT :
                direction = LEFT;
                break;
            case DOWN :
                direction = DOWN;
                break;
        }
    }
    
    public int getDirection() {
        return direction;
    }
    
    public void move() {
        switch (direction) {
            case RIGHT :
                ++xPos;
                break;
            case UP :
                --yPos;
                break;
            case LEFT :
                --xPos;
                break;
            case DOWN :
                ++yPos;
                break;
        }
    }
    
    @Override
    public void moveTo(int x, int y) {
        xPos = x;
        yPos = y;
    }

    @Override
    public RectangularShape sprite() {
        resetSprite();
        return sprite;
    }

    @Override
    public int getX() {
        return xPos;
    }

    @Override
    public int getY() {
        return yPos;
    }
    
    private void resetSprite() {
        sprite = new Rectangle2D.Float(cell*xPos, cell*yPos, cell, cell);
    }
    
}