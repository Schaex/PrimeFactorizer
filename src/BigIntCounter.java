import java.math.BigInteger;
import java.util.Map;
import java.util.TreeMap;

public class BigIntCounter extends ApplicationConstants {
    /**
     * Utility class for counting all the prime factors if a BigInteger.
     */
    final BigInteger bigIntToCount;
    BigInteger count;

    public BigIntCounter(BigInteger bigIntToCount, BigInteger count) {
        this.bigIntToCount = bigIntToCount;
        this.count = count;
    }

    public static void countUp(BigIntCounter counter, BigInteger increment) {
        counter.count = counter.count.add(increment);
    }

    @Override
    public String toString() {
        return bigIntToCount + superscript(count.toString());
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
     * Finds all prime factors of a BigInteger and returns them as a three-parts-String:
     * 1) The initial number.
     * 2) All the prime factors in a product line.
     * 3) All the prime factors with exponents in their own lines.
     * Except if the number is already a prime number. Then an alternative String is returned.

     * This is essentially the same as the primeFactorization method in the IntCounter class.
     * If you want to understand what's going on, check out the other class. This method essentially does the same, just with BigIntegers.
     */
    static String primeFactorization(BigInteger intToFactorize) {
        BigInteger i = intToFactorize;
        BigInteger factor = BigInteger.TWO;
        Map<BigInteger, BigIntCounter> counterMap = new TreeMap<>();

        // A prime factor of a number is at most half of that number.
        // a.compareTo(b) returns -1 (a < b), 0 (a = b) or 1 (a > b).
        // "a.compareTo(b) < 1" is equivalent to "a <= b".
        while (factor.compareTo(intToFactorize.divide(BigInteger.TWO)) < 1) {
            while ((i.mod(factor)).equals(BigInteger.ZERO)) {
                if (!counterMap.containsKey(factor)) {
                    counterMap.put(factor, new BigIntCounter(factor, BigInteger.ZERO));
                }

                BigIntCounter.countUp(counterMap.get(factor), BigInteger.ONE);
                i = i.divide(factor);
            }

            // 2 is the only even prime number which makes checking all the other even numbers is redundant.
            if (factor.equals(BigInteger.TWO)) {
                factor = factor.add(BigInteger.ONE);
            } else {
                factor = factor.add(BigInteger.TWO);
            }
        }

        // Sets up StringBuilders. builder1 already contains part 1 of the returned String.
        StringBuilder builder1 = new StringBuilder("Factorized integer: ");
        builder1.append(intToFactorize).append(linesep);
        StringBuilder builder2 = new StringBuilder();

        // Builds part 2 and 3 of the returned String.
        for (BigInteger factors : counterMap.keySet()) {
            for (BigInteger j = BigInteger.ZERO; j.compareTo(counterMap.get(factors).count) < 0; j = j.add(BigInteger.ONE)) {
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
