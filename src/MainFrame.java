import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import java.awt.*;
import java.awt.event.*;
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
    private JPanel listsPanel;
    private JLabel originFileCountLabel;
    private JLabel changedFileCountLabel;
    private JLabel nowFileCountLabel;
    private JCheckBox checkSubDictionariesCheckBox;
    private JButton refreshNowButton;
    private JProgressBar workingProcessBar;
    private JLabel workingLabel;
    private JPanel workingPanel;

    private Dimension frameSize = new Dimension(500, 500);
    private double spaceDis = 0.1;

    private File path;
    private int delay;
    private File[] oriFiles;
    private File[] nowFiles;
    private Vector<File> changedFiles = new Vector<>();
    private Vector<String> fileChangeDetile = new Vector<>();

    private CheckThread checker = new CheckThread();

    MainFrame() {
        UITheme myTheme = new UITheme();

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
        listsPanel.setBackground(new Color(255, 255, 255));
        this.getContentPane().setBackground(new Color(255, 255, 255));
        spaceProgressBar.setStringPainted(true);
        spaceProgressBar.setString("???");
        spaceProgressBar.setBorderPainted(false);
        nextRefreshLabel.setForeground(new Color(222, 159, 0));
        mergeButton.setForeground(new Color(255, 55, 55));
        clearButton.setEnabled(false);
        clearButton.setText("UselessButton");
        refreshNowButton.setEnabled(false);
        mergeButton.setEnabled(false);
        workingPanel.setVisible(false);

        checkSubDictionariesCheckBox.setEnabled(false);


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
        }); //TODO It doesn't work . fix it .

        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                path = new File(pathTextFiled.getText(), "");
                delay = Integer.valueOf(String.valueOf(delaySpinner.getValue()));
                if (!path.isDirectory() || pathTextFiled.getText().equals("")) {
                    MessageWindow.showMessageWindow(0, "The file path \"" + path.getPath() + "\" doesn't exist");
                } else if (delay <= 0 || delaySpinner.getValue().equals(null)) {
                    delaySpinner.setValue(1);
                    MessageWindow.showMessageWindow(0, "Check delay can only be 1 or bigger");
                } else {
                    oriFiles = path.listFiles();
//                    for (int i = 0; i < oriFiles.length; i++) {
//                        if (oriFiles[i].isDirectory()) {
//                            File[] subPath = oriFiles[i].listFiles();
//                            for (int j = 0; j < subPath.length; i++) {
//                                oriFiles[oriFiles.length] = subPath[j];
//                            }
//                        }
//                    } //TestCode
                    originalFileList.setListData(GetFileNameVector(oriFiles));
                    originFileCountLabel.setText(String.valueOf(oriFiles.length) + " Files ");
                    nowFileList.setListData(GetFileNameVector(oriFiles));
                    nowFileCountLabel.setText(String.valueOf(oriFiles.length) + " Files ");
                    changedFileCountLabel.setText("0 Changes ");
                    okButton.setEnabled(false);
                    spaceProgressBar.setMaximum((int) (path.getTotalSpace() * spaceDis));
                    spaceProgressBar.setValue((int) ((path.getTotalSpace() - path.getUsableSpace()) * spaceDis));
                    UpdateSpaceProcessBar();
                    checker.start();
                    refreshNowButton.setEnabled(true);
                }
            }
        });

        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (MessageWindow.showMessageWindow(2, "Are you sure to clear the list of changed file ?").equals("0"))
                    changedFileList.setListData(new Vector());
            }
        });

        mergeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (MessageWindow.showMessageWindow(2, "Are you sure to merge these changes ?").equals("0")) {
                    oriFiles = nowFiles.clone();
                    originalFileList.setListData(GetFileNameVector(oriFiles));
                    changedFileList.setListData(new Vector());
                    originFileCountLabel.setText(String.valueOf(oriFiles.length) + " Files ");
                    mergeButton.setEnabled(false);
                }
            }
        });

        refreshNowButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                RefreshFileList();
            }
        });

        changedFileList.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (e.getKeyChar() == 'd' || e.getKeyChar() == 'D')
                    if (fileChangeDetile.elementAt(changedFileList.getSelectedIndex()).charAt(0) == 'N')
                        (new FileDelThread()).start();
            }
        });

        ////////////////////////INIT///////////////////////
        this.setMinimumSize(new Dimension(400, 300));
        this.setSize(frameSize);
        //this.setResizable(false);
        this.add(mainPanel);
        mainPanel.setBackground(new Color(255, 255, 255));
    }


    ////////////////////////////////Class type sssssssssss////////////////////////////////

    class FileDelThread extends Thread {
        @Override
        public void run() {
            workingPanel.setVisible(true);
            workingLabel.setText("Deleting Files");
            changedFileList.setEnabled(false);
            workingProcessBar.setMaximum(changedFileList.getSelectedIndices().length);
            for (int i = 0; i < changedFileList.getSelectedIndices().length; i++) {
                try {
                    if (changedFiles.elementAt(i).exists()&&(!changedFiles.elementAt(i).delete()))
                        MessageWindow.showMessageWindow(0, "Some thing wrone when deleting the file .");

                } catch (Exception e1) {
                    e1.printStackTrace();
                }
                workingProcessBar.setValue(i);
            }
            workingPanel.setVisible(false);
            changedFileList.setEnabled(true);
            RefreshFileList();
        }
    }

    class CheckThread extends Thread {
        @Override
        public void run() {
            for (; ; ) {
                try {
                    for (int i = delay; i >= 0; i--) {
                        sleep(1000);
                        nextRefreshLabel.setText("Next refresh : " + String.valueOf(i));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                RefreshFileList();
            }
        }
    }

    void UpdateSpaceProcessBar() {
        spaceProgressBar.setString((path.getTotalSpace() - path.getUsableSpace()) + " / " + path.getTotalSpace());
        spaceProgressBar.setValue((int) ((path.getTotalSpace() - path.getUsableSpace()) * spaceDis));
    }

    void RefreshFileList() {
        nextRefreshLabel.setText("Refreshing.......");
        nowFiles = path.listFiles();
        if (!nowFiles.equals(oriFiles)) { //TODO  it dosent work , fix it .
            (new RefreshThread()).start();
        }
    }

    Vector<String> GetFileNameVector(File[] tmpFile) {
        Vector<String> tmpVector = new Vector<>();
        for (int i = 0; i < tmpFile.length; i++)
            tmpVector.add(tmpFile[i].getName());
        return tmpVector;
    }

    public class RefreshThread extends Thread {
        @Override
        public void run() {
            refreshNowButton.setEnabled(false);
            changedFiles.removeAllElements();
            fileChangeDetile.removeAllElements();
            for (int i = 0; i < nowFiles.length; i++) {
                boolean checked = false;
                for (int j = 0; j < oriFiles.length; j++) {
                    if (nowFiles[i].equals(oriFiles[j]))
                        checked = true;
                }
                if (checked) continue;
                changedFiles.add(nowFiles[i]);
                fileChangeDetile.add("New File : " + nowFiles[i].getName());
                mergeButton.setEnabled(true);
            }
            for (int i = 0; i < oriFiles.length; i++) {
                boolean checked = false;
                for (int j = 0; j < nowFiles.length; j++) {
                    if (oriFiles[i].equals(nowFiles[j])) {
                        checked = true;
                    }
                }
                if (checked) continue;
                changedFiles.add(oriFiles[i]);
                fileChangeDetile.add("Remove File : "+oriFiles[i].getName());
                mergeButton.setEnabled(true);
            }
            changedFileList.setListData(fileChangeDetile);
            changedFileCountLabel.setText(String.valueOf(changedFiles.toArray().length) + " Changes ");
            nowFileList.setListData(GetFileNameVector(path.listFiles()));
            nowFileCountLabel.setText(String.valueOf(nowFiles.length) + " Files ");
            UpdateSpaceProcessBar();
            refreshNowButton.setEnabled(true);
        }
    }
}