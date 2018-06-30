import javax.swing.*;

public class NFCTLauncher {

    public static MainFrame mainFrame;

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        mainFrame = new MainFrame();
        mainFrame.setVisible(true);
    }
}
