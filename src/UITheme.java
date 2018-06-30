import javax.swing.*;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;
import javax.tools.Tool;
import java.awt.*;
import java.util.Locale;

public class UITheme {

    public static Color recordPanelColor;
    public static Color mainPanelColr;
    public static Font tagFont;

    public UITheme() {

        //restdefaultfont();
    }

    public static void restdefaultfont() {
        Font f = new Font("Ubuntu", 0, 15);
        String names[] = {"Label", "CheckBox", "PopupMenu", "MenuItem", "CheckBoxMenuItem",
                "JRadioButtonMenuItem", "ComboBox", "Button", "Tree", "ScrollPane",
                "TabbedPane", "EditorPane", "TitledBorder", "Menu", "TextArea",
                "OptionPane", "MenuBar", "ToolBar", "ToggleButton", "ToolTip",
                "ProgressBar", "TableHeader", "Panel", "List", "ColorChooser",
                "PasswordField", "TextField", "Table", "Label", "Viewport",
                "RadioButtonMenuItem", "RadioButton", "DesktopPane", "InternalFrame"
        };
        for (String item : names) {
            UIManager.put(item + ".font", f);
        }
    }
}
