
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
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
    private JList originalFileList;
    private JList changedFileList;
    private JButton okButton;
    private JPanel mainPanel;
    private JButton clearButton;
    private JButton mergeButton;
    private JProgressBar spaceProgressBar;
    private JList nowFileList;
    private JLabel nextRefreshLabel;

    Dimension frameSize = new Dimension(500, 500);

    File path;
    int delay;
    File[] oriFiles;
    File[] nowFiles;
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
                if (MessageWindow.showMessageWindow(2, "Are you sure to exit ?").equals("0"))
                    System.exit(0);
            }
        });

        this.setTitle("GZT's FileChangeCheckTool");



        delaySpinner.setValue(1);
        spaceProgressBar.setStringPainted(true);
        spaceProgressBar.setString("???");
        spaceProgressBar.setBorderPainted(false);
        nextRefreshLabel.setForeground(new Color(222, 159, 0));
        clearButton.setEnabled(false);
        clearButton.setText("UselessButton");


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
                    originalFileList.setListData(oriFiles);
                    nowFileList.setListData(path.listFiles());
                    okButton.setEnabled(false);
                    spaceProgressBar.setMaximum((int)path.getTotalSpace());
                    UpdateSpaceProcessBar();
                    checker.start();
                }
            }
        });

        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (MessageWindow.showMessageWindow(2, "Are you sure to clear the list of new file ?").equals("0"))
                    changedFileList.setListData(new Vector());
            }
        });

        mergeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (MessageWindow.showMessageWindow(2, "Are you sure to merge these changes ?").equals("0")) {
                    oriFiles = nowFiles.clone();
                    originalFileList.setListData(oriFiles);
                    changedFileList.setListData(new Vector());
                }
            }
        });

        this.setMinimumSize(new Dimension(400, 300));
        this.setSize(frameSize);
        //this.setResizable(false);
        this.add(mainPanel);
        mainPanel.setBackground(new Color(255, 255, 255));

    }

    class CheckThread extends Thread {
        @Override
        public void run() {
            for (; ; ) {
                try {
                    for(int i=delay;i>=0;i--){
                        sleep(1000 );
                        nextRefreshLabel.setText("Next refresh : "+String.valueOf(i));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                nextRefreshLabel.setText("Refreshing.......");
                nowFiles = path.listFiles();
                if (!nowFiles.equals(oriFiles)) {
                    Vector<String> changedFile = new Vector<>();
                    for (int i = 0; i < nowFiles.length; i++) {
                        boolean checked = false;
                        for (int j = 0; j < oriFiles.length; j++) {
                            if (nowFiles[i].equals(oriFiles[j]))
                                checked = true;
                        }
                        if (checked) continue;
                        changedFile.add("New File : " + nowFiles[i].getPath() + nowFiles[i].getName());
                    }
                    for (int i = 0; i < oriFiles.length; i++) {
                        boolean checked = false;
                        for (int j = 0; j < nowFiles.length; j++) {
                            if(oriFiles[i].equals(nowFiles[j])){
                                checked=true;
                            }
                        }
                        if (checked) continue;
                        changedFile.add("Remove File : " + oriFiles[i].getPath() + oriFiles[i].getName());
                    }
                    changedFileList.setListData(changedFile);
                    nowFileList.setListData(path.listFiles());
                    UpdateSpaceProcessBar();
                }
            }
        }
    }

    void UpdateSpaceProcessBar(){
        spaceProgressBar.setString((path.getTotalSpace()-path.getUsableSpace())+" / "+path.getTotalSpace());
        spaceProgressBar.setValue((int)(path.getTotalSpace()-path.getUsableSpace()));
    }

}


