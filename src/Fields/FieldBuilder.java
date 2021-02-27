package Fields;

public class FieldBuilder {
    public static Field createField(int q) {
        if (isPrime(q))
            return new PrimeField(q);
        else if (isPowerOf2(q))
            return new PolynomialField(powerOfQ(q));
        else
            throw new IllegalArgumentException("Not valid q.");
    }

    private static boolean isPrime(int q) {
        for (int i = 2; i < Math.sqrt(q); i++)
            if (q % i == 0)
                return false;
        return true;
    }

    private static boolean isPowerOf2(int q) {
        while ((q & 1) == 0) {
            q /= 2;
            if (q == 1)
                return true;
        }
        return false;
    }

    private static int powerOfQ(int q) {
        for (byte i = 2; i <= 16; i++)
            if (q == Math.pow(2, i))
                return i;
        throw new IllegalArgumentException("Not valid q");
    }
}
