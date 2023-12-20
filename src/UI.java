import javax.swing.*;
import java.awt.*;
import java.math.BigInteger;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UI {
    static final String linesep = System.getProperty("line.separator");
    static final JFrame frame = new JFrame("Prime Factory");
    static final JPanel outerPanel = new JPanel();
    static final JPanel buttonPanel = new JPanel();
    static final JPanel innerPanel = new JPanel();
    static final JButton button = new JButton("Generate new list");
    static final Font font = new Font(null, Font.PLAIN, 20);
    static final ExecutorService executorService = Executors.newSingleThreadExecutor();

    public UI() {
        frame.setSize(1200, (int) (Toolkit.getDefaultToolkit().getScreenSize().getHeight() * 0.9d));
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        button.addActionListener(e -> openDialogBox(false));

        innerPanel.setLayout(new BoxLayout(innerPanel, BoxLayout.Y_AXIS));
        innerPanel.add(Box.createHorizontalStrut(100));


        buttonPanel.setLayout(new GridBagLayout());
        buttonPanel.add(Box.createGlue(), new GridBagConstraints(0, 0, 1, 1, 1, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 3, 5), 0, 0));
        buttonPanel.add(button, new GridBagConstraints(1, 0, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 3, 5), 0, 0));
        buttonPanel.add(Box.createGlue(), new GridBagConstraints(2, 0, 1, 1, 1, 0, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 3, 5), 0, 0));

        JScrollPane scrollPane = new JScrollPane(innerPanel);

        outerPanel.setLayout(new BorderLayout());
        outerPanel.add(buttonPanel, BorderLayout.PAGE_START);
        outerPanel.add(scrollPane, BorderLayout.CENTER);

        frame.add(outerPanel);

        openDialogBox(true);

        frame.setVisible(true);
    }

    static void openDialogBox(boolean isStartup) {
        JPanel dialogPanel = new JPanel(new GridLayout(5, 1));
        JLabel dialogLabel1 = new JLabel("Enter numbers, each separated by a comma!");
        JLabel dialogLabel2 = new JLabel("Note that numbers with large prime factors take longer to compute");
        JLabel dialogLabel3 = new JLabel("Especially large numbers that are larger than 2147483647");
        JLabel dialogLabel4 = new JLabel("");
        JTextField dialogTextField = new JTextField();

        dialogPanel.add(dialogLabel1);
        dialogPanel.add(dialogLabel2);
        dialogPanel.add(dialogLabel3);
        dialogPanel.add(dialogLabel4);
        dialogPanel.add(dialogTextField);

        int result = JOptionPane.showConfirmDialog(null, dialogPanel, "Input", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            String input = dialogTextField.getText();
            input = legalizeString(input);

            if (input.isEmpty()) {
                return;
            }

            innerPanel.removeAll();
            TextArea waitingText = new TextArea("Calculating... please wait!", 1, 10, TextArea.SCROLLBARS_NONE);
            waitingText.setEditable(false);
            innerPanel.add(waitingText);
            frame.setVisible(true);
            frame.validate();

            String finalInput = input;
            executorService.submit(() -> {
                CreateOutput(ParseInput(finalInput));
                innerPanel.remove(waitingText);
                frame.validate();
            });
        } else {
            if (isStartup) {
                System.exit(0);
            }
            return;
        }

        frame.validate();
    }

    static String legalizeString (String s) {
        String legalString = s.replaceAll("[^0-9,]", "");
        legalString = legalString.replaceAll(",{2,}", ",");
        legalString = legalString.replaceAll("^,", "");
        legalString = legalString.replaceAll(",$", "");


        if (legalString.isEmpty()) {
            return "";
        }

        return legalString;
    }

    static BigInteger[] ParseInput (String input) {
        String s = input.replaceAll("[^0-9,]", "");
        String[] stringArray = s.split(",", -1);

        BigInteger[] intArray = new BigInteger[stringArray.length];

        for (int i = 0; i < intArray.length; i++) {
            intArray[i] = new BigInteger(stringArray[i]);
        }

        return intArray;
    }

    static void CreateOutput (BigInteger[] bigIntsToFactorize) {
        for (BigInteger i : bigIntsToFactorize) {
            String s;
            if (i.compareTo(BigInteger.valueOf(Integer.MAX_VALUE)) > 0) {
                s = BigIntCounter.PrimeFactorization(i);
            } else {
                s = IntCounter.PrimeFactorization(i.intValue());
            }

            int rows = s.split(linesep, -1).length + (s.length() / 100);
            TextArea textArea = new TextArea(s, rows, 10, TextArea.SCROLLBARS_NONE);
            textArea.setMaximumSize(new Dimension(Short.MAX_VALUE, Short.MAX_VALUE));
            textArea.setFont(font);
            textArea.setEditable(false);

            innerPanel.add(textArea);

            Component spacer = Box.createHorizontalStrut(10);
            innerPanel.add(spacer);
        }
    }
}
