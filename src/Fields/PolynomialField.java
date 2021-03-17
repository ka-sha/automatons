package Fields;

public class PolynomialField implements Field {
    private final int primePolynomial;
    private final int cardinality;

    public PolynomialField(int pow) {
        if (pow == 4)
            primePolynomial = 0b1_0011;
        else if (pow == 8)
            primePolynomial = 0b1_0001_1011;
        else
            primePolynomial = 0b1_0110_1111_0110_0011;
        cardinality = (int) Math.pow(2, pow);
    }

    @Override
    public int sum(int a, int b) {
        return a ^ b;
    }

    @Override
    public int mul(int a, int b) {
        int res = 0;
        while (b != 0) {
            if ((b & 1) == 1)
                res ^= a;
            a <<= 1;

            if ((a & cardinality) != 0)
                a ^= primePolynomial;
            b >>>= 1;
        }
        return res;
    }

    @Override
    public int cardinality() {
        return cardinality;
    }
}
