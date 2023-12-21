import javax.swing.*;
import java.awt.*;
import java.math.BigInteger;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UI implements ApplicationConstants {
    /**
     * Utility method that enables building the UI as an object.
     */
    static String inputText = "";
    // Gets updated only once after the first input and then remains "true" for the rest of the runtime.
    static boolean hasHistory = false;

    // Frames that contain all the content except of the dialog.
    static final JFrame mainFrame = new JFrame("Prime Factory");
    static final JFrame historyFrame = new JFrame("History");

    static final JPanel historyPanel = new JPanel();
    static final JPanel buttonPanel = new JPanel();
    static final JPanel outerPanel = new JPanel();
    static final JPanel innerPanel = new JPanel();

    // Dialog
    static JPanel dialogPanel = new JPanel(new GridLayout(5, 1));
    static JLabel dialogLabel1 = new JLabel("Enter numbers, each separated by a comma!");
    static JLabel dialogLabel2 = new JLabel("Note that numbers with large prime factors take longer to compute");
    static JLabel dialogLabel3 = new JLabel("Especially large numbers that are larger than 2147483647");
    static JLabel dialogLabel4 = new JLabel("");
    static JTextField dialogTextField = new JTextField();

    // "glue" component that takes all the remaining space.
    static final Component glueBox = Box.createGlue();

    static final JButton historyButton = new JButton("History");
    static final JButton button = new JButton("Generate new list");

    // name = null to get the default font family.
    static final Font font = new Font(null, Font.PLAIN, 20);

    //gridy = GridBagConstraints.RELATIVE to line up the components in the order they are added (in y-direction => up-down).
    static GridBagConstraints textAreaConstraints = new GridBagConstraints(0, GridBagConstraints.RELATIVE, 1, 1, 1, 0, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 3, 5), 0, 0);
    static GridBagConstraints glueBoxConstraints = new GridBagConstraints(0, GridBagConstraints.RELATIVE, 1, 1, 1, 1, GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(5, 5, 3, 5), 0, 0);

    static final ExecutorService executorService = Executors.newSingleThreadExecutor();


    /**
     * Constructor that sets up all components.
     */
    public UI() {
        mainFrame.setSize(1200, (int) (Toolkit.getDefaultToolkit().getScreenSize().getHeight() * 0.9d));
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        historyFrame.setSize(1200, (int) (Toolkit.getDefaultToolkit().getScreenSize().getHeight() * 0.9d));
        historyFrame.setLocationRelativeTo(null);

        historyPanel.setLayout(new GridBagLayout());
        JScrollPane historyScrollPane = new JScrollPane(historyPanel);
        historyFrame.add(historyScrollPane);

        historyPanel.add(makeTextArea("Nothing to see here." + linesep + "Generate something!", 3), textAreaConstraints);
        historyPanel.add(glueBox, glueBoxConstraints);

        historyButton.addActionListener(e -> historyFrame.setVisible(true));
        button.addActionListener(e -> openDialogBox(false));

        innerPanel.setLayout(new GridBagLayout());

        buttonPanel.setLayout(new GridBagLayout());
        buttonPanel.add(historyButton, new GridBagConstraints(0, 0, 1, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 3, 5), 0, 0));
        buttonPanel.add(button, new GridBagConstraints(1, 0, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 3, 5), 0, 0));
        buttonPanel.add(Box.createHorizontalGlue(), new GridBagConstraints(2, 0, 1, 1, 1, 0, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 3, 5), 0, 0));

        JScrollPane scrollPane = new JScrollPane(innerPanel);

        outerPanel.setLayout(new BorderLayout());
        outerPanel.add(buttonPanel, BorderLayout.PAGE_START);
        outerPanel.add(scrollPane, BorderLayout.CENTER);

        mainFrame.add(outerPanel);

        dialogPanel.add(dialogLabel1);
        dialogPanel.add(dialogLabel2);
        dialogPanel.add(dialogLabel3);
        dialogPanel.add(dialogLabel4);
        dialogPanel.add(dialogTextField);

        openDialogBox(true);

        mainFrame.setVisible(true);
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

            if (input.isEmpty()) {
                return;
            }

            if (inputText.isEmpty()) {
                inputText = "Your input: ";
            }

            inputText = inputText + input.replaceAll(",", ", ");

            innerPanel.removeAll();
            TextArea waitingText = new TextArea(inputText + linesep + linesep + "Calculating... please wait!", 20, 10, TextArea.SCROLLBARS_NONE);
            waitingText.setMaximumSize(new Dimension(Short.MAX_VALUE, Short.MAX_VALUE));
            waitingText.setFont(font);
            waitingText.setEditable(false);
            innerPanel.add(waitingText);
            mainFrame.setVisible(true);
            mainFrame.validate();

            String finalInput = input;
            executorService.submit(() -> {
                if (!hasHistory) {
                    historyPanel.removeAll();
                    hasHistory = true;
                }

                historyPanel.remove(glueBox);
                historyPanel.add(makeTextArea(finalInput.replaceAll(",", ", "), 3), textAreaConstraints);
                historyPanel.add(glueBox, glueBoxConstraints);
                historyFrame.validate();
                createOutput(parseInput(finalInput));
                inputText = "";
                innerPanel.remove(waitingText);
                mainFrame.validate();
            });
        } else {
            // Only true with initial call of openDialogBox(boolean isStartup).
            // Closes the application if the user presses "cancel" or closes the dialog.
            if (isStartup) {
                System.exit(0);
            }
        }
    }

    /**
     * Strips strings to make parsing easier.
     */
    static String legalizeString(String s) {
        String legalString = s.replaceAll("[^0-9,]", "");
        legalString = legalString.replaceAll(",{2,}", ",");
        legalString = legalString.replaceAll("^,", "");
        legalString = legalString.replaceAll(",$", "");

        if (legalString.isEmpty()) {
            return "";
        }

        return legalString;
    }

    /**
     * Parses the "legalized" String from legalizeString(String s) and returns the numbers as an array of BigIntegers.
     */
    static BigInteger[] parseInput(String input) {
        String s = input.replaceAll("[^0-9,]", "");
        String[] stringArray = s.split(",", -1);

        BigInteger[] intArray = new BigInteger[stringArray.length];

        for (int i = 0; i < intArray.length; i++) {
            intArray[i] = new BigInteger(stringArray[i]);
        }

        return intArray;
    }

    /**
     * Goes through the array of BigIntegers from ParseInput(String input).
     * Checks for each number if it can not be expressed as an int (Integer.MAX_VALUE = 2147483647) and chooses the correct method for computation.
     * This is important because calculations on BigIntegers are slower than on int.
     */
    static void createOutput(BigInteger[] bigIntsToFactorize) {
        innerPanel.add(makeTextArea(inputText, 3), textAreaConstraints);
        for (BigInteger i : bigIntsToFactorize) {
            String s;
            if (i.compareTo(BigInteger.valueOf(Integer.MAX_VALUE)) > 0) {
                s = BigIntCounter.primeFactorization(i);
            } else {
                s = IntCounter.primeFactorization(i.intValue());
            }

            int rows = s.split(linesep, -1).length + (s.length() / 100);
            innerPanel.add(makeTextArea(s, rows), textAreaConstraints);
        }
        innerPanel.add(glueBox, glueBoxConstraints);
    }

    /**
     * Utility method that returns the default TextArea for this application.
     */
    static TextArea makeTextArea(String text, int rows) {
        TextArea textArea = new TextArea(text, rows, 10, TextArea.SCROLLBARS_NONE);
        textArea.setMaximumSize(new Dimension(Short.MAX_VALUE, Short.MAX_VALUE));
        textArea.setFont(font);
        textArea.setEditable(false);

        return textArea;
    }
}
