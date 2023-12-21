import java.awt.*;
import java.math.BigInteger;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ApplicationConstants {
    /**
     * Utility interface to prevent duplicate code.
     */
    static final String linesep = System.getProperty("line.separator");
    static final Color hintOfYellow = new Color(255, 255, 191);

    static final ExecutorService executorService = Executors.newCachedThreadPool();

    // gridy = GridBagConstraints.RELATIVE ensures that the component is always last.
    static final GridBagConstraints textAreaConstraints = new GridBagConstraints(0, GridBagConstraints.RELATIVE, 1, 1, 1, 0, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 3, 5), 0, 0);
    static final GridBagConstraints glueBoxConstraints = new GridBagConstraints(0, GridBagConstraints.RELATIVE, 1, 1, 1, 1, GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(5, 5, 3, 5), 0, 0);

    // name = null to get the default font family.
    static final Font font = new Font(null, Font.PLAIN, 20);

    static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd  HH:mm:ss");

    /**
     * Utility method.
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
     * Calls ParseInput(String input) and goes through the array of BigIntegers.
     * Checks for each number if it can not be expressed as an int (Integer.MAX_VALUE = 2147483647)
     * and chooses the correct method for computation.
     * This is important because calculations on BigIntegers are slower than on int.
     */
    static java.util.List<TextArea> createOutput(String input) {
        String output = input;
        BigInteger[] bigIntsToFactorize = parseInput(output);

        List<TextArea> newTextAreas = new ArrayList<>();
        newTextAreas.add(makeTextArea(output, 1 + (input.length() / 90), hintOfYellow));

        for (BigInteger i : bigIntsToFactorize) {
            if (i.compareTo(BigInteger.valueOf(Integer.MAX_VALUE)) > 0) {
                output = BigIntCounter.primeFactorization(i);
            } else {
                output = IntCounter.primeFactorization(i.intValue());
            }

            int rows = output.split(linesep, -1).length - 1 + (output.length() / 100);
            newTextAreas.add(makeTextArea(output, rows, null));
        }

        return newTextAreas;
    }

    /**
     * Utility method that returns the default TextArea for this application.
     */
    static TextArea makeTextArea(String text, int rows, Color color) {
        TextArea textArea = new TextArea(text, rows, 10, TextArea.SCROLLBARS_NONE);
        textArea.setMaximumSize(new Dimension(Short.MAX_VALUE, Short.MAX_VALUE));
        textArea.setFont(font);
        textArea.setEditable(false);

        if (color != null) {
            textArea.setBackground(color);
        }

        return textArea;
    }
}
