package Fields;

public class PrimeField implements Field {
    private final int q;

    public PrimeField(int q) {
        this.q = q;
    }

    @Override
    public int sum(int a, int b) {
        return (a + b) % q;
    }

    @Override
    public int mul(int a, int b) {
        return (a * b) % q;
    }

    @Override
    public int cardinality() {
        return q;
    }
}
