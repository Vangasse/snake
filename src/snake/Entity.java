package snake;

import java.awt.geom.RectangularShape;

public interface Entity {
    
    public RectangularShape sprite();
    public int getX();
    public int getY();
    public void moveTo(int x, int y);
}
