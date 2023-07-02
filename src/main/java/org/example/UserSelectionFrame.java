package org.example;

import javax.swing.*;

public class UserSelectionFrame {
    private JList<String> list1;
    private JList<String> list2;
    private JButton mergeButton1;
    private JList<String> list3;
    private JButton generateFileButton;
    private JTextField a05TextField;
    private JPanel thirdRootPanel;

    private JFrame mainFrame; // Reference to the main JFrame


    public UserSelectionFrame(JFrame mainFrame) {
        this.mainFrame = mainFrame;
        list1.setModel(Globals.listModel1);
        list2.setModel(Globals.listModel2);
        list3.setModel(Globals.listModel3);
    }

    public JPanel getThirdRootPanel() {
        return thirdRootPanel;
    }


}
