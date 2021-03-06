package com.cliproads;

import javax.swing.*;
//import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.*;
import java.util.prefs.Preferences;
// import java.util.ArrayList; // to be useful if and when I make the gui/cells modifiable

public class Cliproads {
    private JTextField textField1_1;
    private JTextField textField2_1;
    private JTextField textField3_1;
    private JTextField textField4_1;
    private JTextField textField5_1;
    private JTextField textField6_1;
    private JTextField textField7_1;
    private JTextField textField8_1;
    private JTextField textField1_2;
    private JTextField textField2_2;
    private JTextField textField3_2;
    private JTextField textField4_2;
    private JTextField textField5_2;
    private JTextField textField6_2;
    private JTextField textField7_2;
    private JTextField textField8_2;
    private JTextField tx_separator;
    private JLabel lbl_separator;
    private JTextField textField1_3;
    private JTextField textField2_3;
    private JTextField textField3_3;
    private JTextField textField4_3;
    private JTextField textField5_3;
    private JTextField textField6_3;
    private JTextField textField7_3;
    private JTextField textField8_3;
    private JTextField textField1_4;
    private JTextField textField2_4;
    private JTextField textField3_4;
    private JTextField textField4_4;
    private JTextField textField5_4;
    private JTextField textField6_4;
    private JTextField textField7_4;
    private JTextField textField8_4;
    private JLabel clipboardContentsLabel;
    private JTextPane tx_info;
    private JButton saveButton;
    private JComboBox<String> cb_templates;
    private JButton deleteButton;
    private JPanel masterJPanel;

    // For easy looping through all the textfields aka cells
    final JTextField[] cellRow1 = {textField1_1,textField1_2,textField1_3,textField1_4};
    final JTextField[] cellRow2 = {textField2_1,textField2_2,textField2_3,textField2_4};
    final JTextField[] cellRow3 = {textField3_1,textField3_2,textField3_3,textField3_4};
    final JTextField[] cellRow4 = {textField4_1,textField4_2,textField4_3,textField4_4};
    final JTextField[] cellRow5 = {textField5_1,textField5_2,textField5_3,textField5_4};
    final JTextField[] cellRow6 = {textField6_1,textField6_2,textField6_3,textField6_4};
    final JTextField[] cellRow7 = {textField7_1,textField7_2,textField7_3,textField7_4};
    final JTextField[] cellRow8 = {textField8_1,textField8_2,textField8_3,textField8_4};
    final JTextField[][] cells = {cellRow1,cellRow2,cellRow3,cellRow4,cellRow5,cellRow6,cellRow7,cellRow8};

    String[] cellRow1Texts = {textField1_1.getText(),textField1_2.getText(),textField1_3.getText(),textField1_4.getText()};
    String[] cellRow2Texts = {textField2_1.getText(),textField2_2.getText(),textField2_3.getText(),textField2_4.getText()};
    String[] cellRow3Texts = {textField3_1.getText(),textField3_2.getText(),textField3_3.getText(),textField3_4.getText()};
    String[] cellRow4Texts = {textField4_1.getText(),textField4_2.getText(),textField4_3.getText(),textField4_4.getText()};
    String[] cellRow5Texts = {textField5_1.getText(),textField5_2.getText(),textField5_3.getText(),textField5_4.getText()};
    String[] cellRow6Texts = {textField6_1.getText(),textField6_2.getText(),textField6_3.getText(),textField6_4.getText()};
    String[] cellRow7Texts = {textField7_1.getText(),textField7_2.getText(),textField7_3.getText(),textField7_4.getText()};
    String[] cellRow8Texts = {textField8_1.getText(),textField8_2.getText(),textField8_3.getText(),textField8_4.getText()};
    String[][] tempCellTexts = {cellRow1Texts, cellRow2Texts, cellRow3Texts, cellRow4Texts, cellRow5Texts, cellRow6Texts, cellRow7Texts, cellRow8Texts};

    // For template saving purposes
    Preferences userPrefs = Preferences.userNodeForPackage(Cliproads.class);

    String template = userPrefs.get("template", "Default");
    String[][] cellTexts = userPrefs.get("cellTexts", tempCellTexts);
    String road = userPrefs.get("road", "client_brand2_logo_black_bgNone_v002.png"); // this should be replaced with the actual combined string from the active cells
    int[] tempActiveCells = {0,1,0,1,2,1,0,-1};

    private int[] activeCells = userPrefs.get("activeCells", tempActiveCells); // -1 = cell column not in use






    public Cliproads() {

        for (int col = 0; col < 8; col++) {
            for (int row = 0; row < 4; row++) {
                cells[col][row].addFocusListener(new cellClicked(col,row)); // https://stackoverflow.com/questions/10133366/how-to-clear-jtextfield-when-mouse-clicks-the-jtextfield
                cells[col][row].addKeyListener(new cellClicked(col,row));
            }
        }
        tx_separator.addFocusListener(new cellClicked(0,activeCells[0]));
        tx_separator.addKeyListener(new cellClicked(0,activeCells[0]));
    }


    private class cellClicked implements FocusListener, KeyListener {

        final int col;
        final int row;


        public cellClicked(int c, int r) {
            col = c;
            row = r;
        }

        @Override
        public void focusGained(FocusEvent e) { updateRoad(); }
        public void focusLost(FocusEvent e) {}
        public void keyTyped(KeyEvent e) {}
        public void keyPressed(KeyEvent e) {}
        public void keyReleased(KeyEvent e) { updateRoad(); }

        public void updateRoad() {
            activeCells[col] = row;
            road = "";

            // make background gray on current cell column textfields
            for (JTextField cell: cells[col]) {
                cell.setBackground(new Color(235,235,235));
            }

            for (int i = 0; i < 8; i++) {
                if (activeCells[i] != -1) {
                    String addString = cells[i][activeCells[i]].getText();
                    if (!addString.equals("")) {
                        cells[i][activeCells[i]].setBackground(new Color(255,255,255));
                        if (!addString.substring(0, 1).equals(".") && !road.equals("")) {
                            road += tx_separator.getText();
                        }
                        road += addString;
                    }
                }
            }

            // copy road to clipboard https://stackoverflow.com/questions/6710350/copying-text-to-the-clipboard-using-java
            Toolkit.getDefaultToolkit()
                    .getSystemClipboard()
                    .setContents(
                            new StringSelection(road),
                            null
                    );

            tx_info.setText(road);
        }

    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Cliproads");
        frame.setContentPane(new Cliproads().masterJPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
