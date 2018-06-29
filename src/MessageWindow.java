import javax.swing.*;

public class MessageWindow {
    private static UITheme theme = new UITheme();
    private static JFrame showFrame = NFCTLauncher.mainFrame;

    public MessageWindow() {

    }

    public static String showMessageWindow(int style, String messages) {
        if (style == 0)/*Just show a message*/ JOptionPane.showMessageDialog(showFrame, messages);
        if (style == 1)/*Input a String with message showing*/
            return JOptionPane.showInputDialog(showFrame, messages);
        if (style == 2) { /*switch a button with a message*/
            switch (JOptionPane.showConfirmDialog(showFrame, messages)) {
                case 0:
                    return "0"; //"yes" button
                case 1:
                    return "1"; //"no" button
                case 2:
                    return "2"; //"cancel" button
            }
        }
        return "9"; //if nothing to be return...
    }
}
