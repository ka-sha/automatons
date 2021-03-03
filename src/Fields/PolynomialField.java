package Fields;

public class PolynomialField implements Field {
    private final int primePolynomial;
    private final int mask;
    private final int cardinality;

    public PolynomialField(int pow) {
        if (pow == 4) {
            primePolynomial = 0b10011;
            mask = 0xfffffff0;
        }
        else if (pow == 8) {
            primePolynomial = 0b100011011;
            mask = 0xffffff00;
        }
        else {
            primePolynomial = 0b10110111101100011;
            mask = 0xffff0000;
        }
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
            if ((b & 1) == 1) {
                res ^= a;
                if (res >= primePolynomial) {
                    res ^= primePolynomial;
                    res ^= (res & mask);
                }
            }
            if (((a & 0x40000000) == 0x40000000 && a > 0) || (((a & 0x40000000) == 0) && a < 0))
                a ^= 0x80000000;
            a = a << 1;
            b = b >>> 1;
        }
        return res;
    }

    @Override
    public int cardinality() {
        return cardinality;
    }
}
