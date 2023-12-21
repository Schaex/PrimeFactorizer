import java.util.Map;
import java.util.TreeMap;

public class IntCounter implements ApplicationConstants {
    /**
     * Utility class for counting all the prime factors if an int
     */
    final int intToCount;
    int count;

    public IntCounter(int intToCount, int initialCount) {
        this.intToCount = intToCount;
        this.count = initialCount;
    }

    public static void countUp(IntCounter counter, int increment) {
        counter.count += increment;
    }

    @Override
    public String toString() {
        return intToCount + superscript(String.valueOf(count));
    }

    /**
     * Utility method that returns the exponent (count) in superscript.
     * superscripts may not be available for all fonts.
     */
    public static String superscript(String str) {
        str = str.replaceAll("0", "⁰");
        str = str.replaceAll("1", "¹");
        str = str.replaceAll("2", "²");
        str = str.replaceAll("3", "³");
        str = str.replaceAll("4", "⁴");
        str = str.replaceAll("5", "⁵");
        str = str.replaceAll("6", "⁶");
        str = str.replaceAll("7", "⁷");
        str = str.replaceAll("8", "⁸");
        str = str.replaceAll("9", "⁹");
        return str;
    }

    /**
     * Finds all prime factors of an int and returns them as a three-parts-String:
     * 1) The initial number.
     * 2) All the prime factors in a product line.
     * 3) All the prime factors with exponents in their own lines.
     * Except if the number is already a prime number. Then an alternative String is returned.
     */
    static String primeFactorization(int intToFactorize) {
        int i = intToFactorize;
        int factor = 2;
        Map<Integer, IntCounter> counterMap = new TreeMap<>();

        // A prime factor of a number is at most half of that number.
        while (factor <= (intToFactorize / 2)) {
            while ((i % factor) == 0) {
                if (!counterMap.containsKey(factor)) {
                    counterMap.put(factor, new IntCounter(factor, 0));
                }

                IntCounter.countUp(counterMap.get(factor), 1);
                i /= factor;
            }

            // 2 is the only even prime number which makes checking all the other even numbers is redundant.
            if (factor == 2) {
                factor++;
            } else {
                factor += 2;
            }
        }

        // Sets up StringBuilders. builder1 already contains part 1 of the returned String.
        StringBuilder builder1 = new StringBuilder("Factorized integer: ");
        builder1.append(intToFactorize).append(linesep);
        StringBuilder builder2 = new StringBuilder();

        // Builds part 2 and 3 of the returned String.
        for (int factors : counterMap.keySet()) {
            for (int j = 0; j < counterMap.get(factors).count; j++) {
                builder1.append(factors).append("*");
            }

            builder2.append(counterMap.get(factors)).append(linesep);
        }

        // Builds and returns the alternative String if the previous for-loop did not catch. builder1 gets discarded.
        if (builder2.isEmpty()) {
            builder2.append(intToFactorize).append(" is already prime!").append(linesep);

            return builder2.toString();
        }

        String output = builder1.toString();
        output = output.substring(0, output.length() - 1) + linesep + builder2;

        return output;
    }
}
