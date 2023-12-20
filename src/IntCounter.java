import java.util.Map;
import java.util.TreeMap;

public class IntCounter {
    static final String linesep = System.getProperty("line.separator");
    final int intToCount;
    int count;

    public IntCounter(int intToCount, int initialCount) {
        this.intToCount = intToCount;
        this.count = initialCount;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public static void countUp (IntCounter counter, int increment) {
        counter.setCount(counter.getCount() + increment);
    }

    @Override
    public String toString() {
        return intToCount + superscript(String.valueOf(count));
    }

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

    static String PrimeFactorization (int intToFactorize) {
        int i = intToFactorize;
        int factor = 2;
        Map<Integer, IntCounter> counterMap = new TreeMap<>();

        while (factor <= (intToFactorize / 2)) {
            while ((i % factor) == 0) {
                if (!counterMap.containsKey(factor)) {
                    counterMap.put(factor, new IntCounter(factor, 0));
                }

                IntCounter.countUp(counterMap.get(factor), 1);
                i /= factor;
            }

            if (factor == 2) {
                factor++;
            } else {
                factor += 2;
            }
        }
        StringBuilder builder1 = new StringBuilder("Factorized integer: ");
        StringBuilder builder2 = new StringBuilder();
        builder1.append(intToFactorize).append(linesep);



        for (int factors : counterMap.keySet()) {
            for (int j = 0; j < counterMap.get(factors).getCount(); j++) {
                builder1.append(factors).append("*");
            }

            builder2.append(counterMap.get(factors)).append(linesep);
        }

        if (builder2.isEmpty()) {
            builder2.append(intToFactorize).append(" is already prime!").append(linesep);

            return builder2.toString();
        }

        String output = builder1.toString();
        output = output.substring(0, output.length() - 1) + linesep + builder2;

        return output;
    }
}
