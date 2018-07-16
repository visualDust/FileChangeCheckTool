import studio.visualdust.product.filechangechecktool.Resource.DataResource;

import javax.swing.*;

public class NFCTLauncher {

    public static MainFrame mainFrame;

    public static void main(String[] args) {
        mainFrame = new MainFrame();
        ImageIcon icon = null;
        try {
            icon = new ImageIcon(DataResource.class.getResource("WindowIcon.png").toURI().toURL());
            mainFrame.setIconImage(icon.getImage());
        } catch (Exception e) {
            EventRW.Write(e);
        }
        mainFrame.setVisible(true);
    }
}
