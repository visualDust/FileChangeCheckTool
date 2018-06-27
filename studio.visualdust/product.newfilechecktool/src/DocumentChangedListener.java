import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public interface DocumentChangedListener extends DocumentListener {
    @Override
    default void insertUpdate(DocumentEvent e) {
        anythingChanged(e);
    }

    @Override
    default void removeUpdate(DocumentEvent e) {
        anythingChanged(e);
    }

    @Override
    default void changedUpdate(DocumentEvent e) {
        anythingChanged(e);
    }

    void anythingChanged(DocumentEvent event);
}
