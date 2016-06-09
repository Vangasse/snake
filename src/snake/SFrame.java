package snake;

import java.awt.*;
import java.awt.event.*;


public class SFrame extends Frame {
    
    int fieldWidth, fieldHeight;
    SPanel game;
    
    public static final int CELL_SIZE = 16;
    
    public SFrame(String title, int fieldWidth, int fieldHeight) {
        super(title);
        this.fieldWidth = fieldWidth;
        this.fieldHeight = fieldHeight;
        addWindowListener(new WindowListener() {

            @Override
            public void windowOpened(WindowEvent e) {}

            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }

            @Override
            public void windowClosed(WindowEvent e) {}

            @Override
            public void windowIconified(WindowEvent e) {}

            @Override
            public void windowDeiconified(WindowEvent e) {}

            @Override
            public void windowActivated(WindowEvent e) {}

            @Override
            public void windowDeactivated(WindowEvent e) {}
            
        });
        game = new SPanel(fieldWidth, fieldHeight);
        add(game);
        pack();
        setBackground(new Color(155, 210, 125));
        setResizable(false);
        setVisible(true);
    }
    
    @Override
    public Insets getInsets() {
        return new Insets(35, 13, 3, 3);
    }

}
