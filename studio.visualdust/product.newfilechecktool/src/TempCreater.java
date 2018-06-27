import javax.swing.*;
import java.awt.*;

public class TempCreater {
    public static JLabel CreateLabel(Color bg,Color fg, Font font,String text) {
        JLabel tempLabel = new JLabel(text,JLabel.CENTER);
        tempLabel.setBackground(bg);
        tempLabel.setForeground(fg);
        tempLabel.setFont(font);
        return tempLabel;
    }
}
