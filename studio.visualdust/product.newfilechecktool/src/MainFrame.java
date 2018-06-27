import com.sun.jdi.event.ExceptionEvent;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.Vector;

public class MainFrame extends JFrame {
    private JTextField pathTextFiled;
    private JSpinner delaySpinner;
    private JList nowFileList;
    private JList changedFileList;
    private JButton okButton;
    private JPanel mainPanel;
    private JButton clearButton;
    private JButton mergeButton;

    Dimension frameSize = new Dimension(500, 500);

    File path;
    int delay;
    File[] oriFiles;
    File[] nowFile;
    CheckThread checker = new CheckThread();

    MainFrame() {
        UITheme myTheme = new UITheme();
        try {
            UIManager.setLookAndFeel(myTheme.lookAndFeel);
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if(MessageWindow.showMessageWindow(2,"Are you sure to exit ?").equals("0"))
                    System.exit(0);
            }
        });

        this.setTitle("GZT's FileChangeCheckTool");

        delaySpinner.setValue(1);

        class StringChangeListener implements DocumentChangedListener {
            @Override
            public void anythingChanged(DocumentEvent event) {
                okButton.setEnabled(true);
            }
        }

        pathTextFiled.getDocument().addDocumentListener(new StringChangeListener());
        delaySpinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                okButton.setEnabled(true);
            }
        });

        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                path = new File(pathTextFiled.getText(), "");
                delay = Integer.valueOf(String.valueOf(delaySpinner.getValue()));
                if (!path.isDirectory() || pathTextFiled.getText().equals("")) {
                    MessageWindow.showMessageWindow(0, "This file path doesn't exist");
                } else if (delay <= 0 || delaySpinner.getValue().equals(null)) {
                    delaySpinner.setValue(1);
                    MessageWindow.showMessageWindow(0, "Check delay can only be 1 or bigger");
                } else {
                    oriFiles = path.listFiles();
                    nowFileList.setListData(oriFiles);
                    okButton.setEnabled(false);
                    checker.start();
                }
            }
        });

        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (MessageWindow.showMessageWindow(2, "Are you sure to clear the list of new file ?").equals("0"))
                    changedFileList.removeAll();
            }
        });

        mergeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(MessageWindow.showMessageWindow(2,"Are you sure to merge these changes ?").equals("0")){
                    oriFiles = nowFile.clone();
                }
            }
        });

        this.setMinimumSize(frameSize);
        this.setSize(frameSize);
        this.setResizable(false);
        this.add(mainPanel);

    }

    class CheckThread extends Thread {
        @Override
        public void run() {
            for (; ; ) {
                try {
                    sleep(5000 * delay);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                nowFile = path.listFiles();
                if (!nowFile.equals(oriFiles)) {
                    Vector<File> changedFile = new Vector<>();
                    for (int i = 0; i < nowFile.length; i++) {
                        boolean checked = false;
                        for (int j = 0; j < oriFiles.length; j++) {
                            if (nowFile[i].equals(oriFiles[j]))
                                checked = true;
                        }
                        if (checked) continue;
                        changedFile.add(nowFile[i]);
                    }
                    changedFileList.setListData(changedFile);
                }
            }
        }
    }

}


