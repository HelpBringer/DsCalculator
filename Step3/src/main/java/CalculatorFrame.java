import javax.swing.*;
import java.awt.*;

class CalculatorFrame extends JFrame {
    public CalculatorFrame() {
        setTitle("недоКалькулятор");
        CalculatorPanel panel = new CalculatorPanel();
        add(panel);
        pack();
        int width = 650;
        int height = 350;
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        int screenWidth = gd.getDisplayMode().getWidth();
        int screenHeight = gd.getDisplayMode().getHeight();
        setBounds(screenWidth / 2 - width / 2, screenHeight / 2 - height / 2, width, height);
    }
}