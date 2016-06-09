package snake;

import java.awt.geom.Rectangle2D;
import java.awt.geom.RectangularShape;

public class SWall implements Entity {

    private Rectangle2D.Float sprite;
    private int xPos, yPos;
    private int cell = SFrame.CELL_SIZE;
    
    public SWall(int x, int y) {
        xPos = x;
        yPos = y;
        resetSprite();
    }
    
    public SWall() {
        this(0, 0);
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