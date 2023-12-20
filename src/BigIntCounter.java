import java.math.BigInteger;
import java.util.Map;
import java.util.TreeMap;

public class BigIntCounter {
    static final String linesep = System.getProperty("line.separator");
    final BigInteger bigIntToCount;
    BigInteger count;

    public BigIntCounter(BigInteger bigIntToCount, BigInteger count) {
        this.bigIntToCount = bigIntToCount;
        this.count = count;
    }

    public BigInteger getCount() {
        return count;
    }

    public void setCount(BigInteger count) {
        this.count = count;
    }

    public static void countUp (BigIntCounter counter, BigInteger increment) {
        counter.setCount(counter.getCount().add(increment));
    }

    @Override
    public String toString() {
        return bigIntToCount + superscript(count.toString());
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

    static String PrimeFactorization (BigInteger intToFactorize) {
        BigInteger i = intToFactorize;
        BigInteger factor = BigInteger.TWO;
        Map<BigInteger, BigIntCounter> counterMap = new TreeMap<>();

        while (factor.compareTo(intToFactorize.divide(BigInteger.TWO)) < 1) {
            while ((i.mod(factor)).equals(BigInteger.ZERO)) {
                if (!counterMap.containsKey(factor)) {
                    counterMap.put(factor, new BigIntCounter(factor, BigInteger.ZERO));
                }

                BigIntCounter.countUp(counterMap.get(factor), BigInteger.ONE);
                i = i.divide(factor);
            }

            if (factor.equals(BigInteger.TWO)) {
                factor = factor.add(BigInteger.ONE);
            } else {
                factor = factor.add(BigInteger.TWO);
            }
        }
        StringBuilder builder1 = new StringBuilder("Factorized integer: ");
        StringBuilder builder2 = new StringBuilder();
        builder1.append(intToFactorize).append(linesep);



        for (BigInteger factors : counterMap.keySet()) {
            for (BigInteger j = BigInteger.ZERO; j.compareTo(counterMap.get(factors).getCount()) < 0; j = j.add(BigInteger.ONE)) {
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
