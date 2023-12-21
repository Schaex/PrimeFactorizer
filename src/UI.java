import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class UI extends ApplicationConstants {
    /**
     * Utility method that enables building the UI as an object.
     */
    // Gets updated only once after the first input and then remains "true" until the history is cleared.
    static boolean hasHistory = false;
    static int keyBindingCounter = 0;

    // Frames that contain all the content except of the dialog and the optional extra JFrames.
    static final JFrame mainFrame = new JFrame("Prime Factory");
    static final JFrame historyFrame = new JFrame("History");

    // List to store all the extra JFrames to make them accessible for removal.
    static final List<JFrame> allFrames = new ArrayList<>();

    static final JPanel listOuterPanel = new JPanel(new BorderLayout());
    static final JPanel listPanel = new JPanel(new GridBagLayout());
    static final JPanel listButtonPanel = new JPanel(new GridBagLayout());
    static final JPanel historyOuterPanel = new JPanel(new BorderLayout());
    static final JPanel historyPanel = new JPanel(new GridBagLayout());
    static final JPanel historyButtonPanel = new JPanel(new GridBagLayout());

    // Add scroll bars.
    static final JScrollPane listScrollPane = new JScrollPane(listPanel);
    static final JScrollPane historyScrollPane = new JScrollPane(historyPanel);

    // Lists to store all the TextAreas in the correct order.
    static List<TextArea> listTextAreasList = new ArrayList<>();
    static List<TextArea> historyTextAreasList = new ArrayList<>();

    // Dialog.
    static JPanel dialogPanel = new JPanel(new GridLayout(6, 1));
    static JLabel dialogLabel1 = new JLabel("Enter numbers, each separated by a comma!");
    static JLabel dialogLabel2 = new JLabel("Note that numbers with large prime factors take longer to compute,");
    static JLabel dialogLabel3 = new JLabel("Especially large numbers that are larger than 2147483647.");
    static JLabel dialogLabel4 = new JLabel("");
    static JCheckBox dialogCheckBox = new JCheckBox("Open result in new window");
    static JTextField dialogTextField = new JTextField();

    static final TextArea historyEmptyTextArea = makeTextArea("Nothing to see here." + linesep + "Generate something!", 3, hintOfYellow);

    // "glue" components that take all the remaining space.
    static final Component listGlueBox = Box.createGlue();
    static final Component historyGlueBox = Box.createGlue();

    // Buttons.
    static final JButton historyButton = new JButton("<html><u>H</u>istory</html>");
    static final JButton generatorButton = new JButton("<html><u>G</u>enerate new List</html>");
    static final JButton clearListButton = new JButton("<html><u>C</u>lear List</html>");
    static final JButton clearHistoryButton = new JButton("<html><u>C</u>lear History</html>");
    static final JButton clearAllButton = new JButton("<html>Clear <u>E</u>verything</html>");


    /**
     * Constructor that sets up all components.
     */
    public UI() {
        // Main frame setup.
        mainFrame.setSize((int) (Toolkit.getDefaultToolkit().getScreenSize().getWidth() / 3d), (int) (Toolkit.getDefaultToolkit().getScreenSize().getHeight() * 0.9d));
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        listButtonPanel.add(historyButton, new GridBagConstraints(0, 0, 1, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 3, 5), 0, 0));
        listButtonPanel.add(generatorButton, new GridBagConstraints(1, 0, 1, 1, 0, 0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 3, 5), 0, 0));
        listButtonPanel.add(Box.createHorizontalGlue(), new GridBagConstraints(2, 0, 1, 1, 1, 0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 3, 5), 0, 0));
        listButtonPanel.add(clearListButton, new GridBagConstraints(3, 0, 1, 1, 0, 0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 3, 5), 0, 0));
        listButtonPanel.add(clearAllButton, new GridBagConstraints(4, 0, 1, 1, 0, 0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 3, 5), 0, 0));

        listOuterPanel.add(listButtonPanel, BorderLayout.PAGE_START);
        listOuterPanel.add(listScrollPane, BorderLayout.CENTER);

        mainFrame.add(listOuterPanel);

        // History frame setup.
        historyFrame.setSize((int) (Toolkit.getDefaultToolkit().getScreenSize().getWidth() / 3d), (int) (Toolkit.getDefaultToolkit().getScreenSize().getHeight() * 0.9d));
        historyFrame.setLocationRelativeTo(null);
        historyFrame.setLocation(0, historyFrame.getY());

        historyPanel.add(historyEmptyTextArea, textAreaConstraints);
        historyPanel.add(historyGlueBox, glueBoxConstraints);

        historyButtonPanel.add(clearHistoryButton, new GridBagConstraints(0, 0, 1, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 3, 5), 0, 0));
        historyButtonPanel.add(Box.createHorizontalGlue(), new GridBagConstraints(1, 0, 1, 1, 1, 0, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 3, 5), 0, 0));

        historyOuterPanel.add(historyButtonPanel, BorderLayout.PAGE_START);
        historyOuterPanel.add(historyScrollPane, BorderLayout.CENTER);

        historyFrame.add(historyOuterPanel);

        // Builds the dialog panel.
        dialogPanel.add(dialogLabel1);
        dialogPanel.add(dialogLabel2);
        dialogPanel.add(dialogLabel3);
        dialogPanel.add(dialogLabel4);
        dialogPanel.add(dialogCheckBox);
        dialogPanel.add(dialogTextField);

        // Adds ActionListeners to the buttons so that they act when they are clicked.
        historyButton.addActionListener(e -> historyFrame.setVisible(true));
        generatorButton.addActionListener(e -> openDialogBox(false));
        clearListButton.addActionListener(e -> clearList());
        clearHistoryButton.addActionListener(e -> clearHistory());
        clearAllButton.addActionListener(e -> {
            clearList();
            clearHistory();
            clearExtraFrames();
        });

        // Adds key bindings to the JFrames.
        createKeyBinding(mainFrame, KeyEvent.VK_G, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openDialogBox(false);
            }
        });
        createKeyBinding(mainFrame, KeyEvent.VK_H, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                historyFrame.setVisible(true);
            }
        });
        createKeyBinding(mainFrame, KeyEvent.VK_C, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearList();
            }
        });
        createKeyBinding(mainFrame, KeyEvent.VK_E, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearList();
                clearHistory();
                clearExtraFrames();
            }
        });
        createKeyBinding(historyFrame, KeyEvent.VK_C, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearHistory();
            }
        });
        createKeyBinding(historyFrame, KeyEvent.VK_ESCAPE, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                historyFrame.setVisible(false);
            }
        });

        // Initial prompt. This is the only time "isStartup" is "true".
        openDialogBox(true);

        // Shows the main frame after openDialogBox(true) has finished, unless the application was terminated.
        mainFrame.setVisible(true);
    }

    /**
     * Utility method to create key bindings to JFrames if they are (in) the focused window.
     */
    static void createKeyBinding(JFrame frame, int keyEvent, AbstractAction abstractAction) {
        InputMap inputMap = frame.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = frame.getRootPane().getActionMap();

        KeyStroke keyStroke = KeyStroke.getKeyStroke(keyEvent, 0);
        inputMap.put(keyStroke, keyBindingCounter);
        actionMap.put(keyBindingCounter, abstractAction);

        keyBindingCounter++;
    }

    /**
     * Heart and soul of the application!
     * Opens a dialog box that prompts the user to enter a list of numbers.
     * and submits the task of creating an output to the ExecutorService.
     * Also updates the History before creating an output and validates both the historyFrame and mainFrame.
     */
    static void openDialogBox(boolean isStartup) {
        int result = JOptionPane.showConfirmDialog(null, dialogPanel, "Input", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            String input = dialogTextField.getText();
            input = legalizeString(input);

            // Resets the text field.
            dialogTextField.setText("");

            // Early return if "OK" has been clicked without providing any adequate input.
            if (input.isEmpty()) {
                return;
            }

            input = "Your input: " + input.replaceAll(",", ", ");
            String timestamp = LocalDateTime.now().format(formatter);

            updateHistory(input, timestamp);

            if (dialogCheckBox.isSelected()) {


                new NewWindowMiniUI(input, timestamp);

                return;
            }

            // Displays waiting message.
            listPanel.removeAll();
            TextArea waitingText = makeTextArea(input + linesep + linesep + "Calculating... please wait!", 20, null);
            listPanel.add(waitingText, textAreaConstraints);

            for (TextArea textArea : listTextAreasList) {
                listPanel.add(textArea, textAreaConstraints);
            }

            listPanel.add(listGlueBox, glueBoxConstraints);
            mainFrame.validate();

            // Passes the task to the ExecutorService to keep the application responsive.
            String finalInput = input;
            executorService.submit(() -> {
                // Heap of the computation.
                List<TextArea> newTextAreas = createOutput(finalInput);

                // Updates the list, making sure to apply the correct order.
                newTextAreas.addAll(listTextAreasList);
                listTextAreasList = newTextAreas;

                listPanel.removeAll();

                for (TextArea textArea : listTextAreasList) {
                    listPanel.add(textArea, textAreaConstraints);
                }

                listPanel.add(listGlueBox, glueBoxConstraints);

                mainFrame.validate();
            });
        } else {
            // Only true with initial call of openDialogBox(boolean isStartup).
            // Terminates the application if the user clicks "cancel" or closes the dialog.
            if (isStartup) {
                System.exit(0);
            }
        }
    }

    static void updateHistory(String input, String timestamp) {
        if (!hasHistory) {
            historyPanel.removeAll();
            hasHistory = true;
        }

        List<TextArea> newHistoryTextArea = new ArrayList<>();
        newHistoryTextArea.add(makeTextArea(timestamp + linesep + (input.split(",").length) + " numbers" + linesep + linesep + input, 4 + (input.length() / 90), hintOfYellow));
        newHistoryTextArea.addAll(historyTextAreasList);
        historyTextAreasList = newHistoryTextArea;

        for (TextArea textArea : historyTextAreasList) {
            historyPanel.add(textArea, textAreaConstraints);
        }

        historyPanel.add(historyGlueBox, glueBoxConstraints);
        historyPanel.validate();
    }

    static void clearList() {
        listTextAreasList.clear();
        listPanel.removeAll();
        listPanel.add(listGlueBox, glueBoxConstraints);
        mainFrame.validate();
    }

    static void clearHistory() {
        historyTextAreasList.clear();
        historyPanel.removeAll();
        hasHistory = false;
        historyPanel.add(historyEmptyTextArea, textAreaConstraints);
        historyPanel.add(historyGlueBox, glueBoxConstraints);
        historyFrame.validate();
    }

    static void clearExtraFrames() {
        for (JFrame frame : allFrames) {
            while (frame.isVisible() && frame.getComponents().length != 0) {
                frame.setVisible(false);
                frame.removeAll();
            }
            allFrames.remove(frame);
        }
    }
}