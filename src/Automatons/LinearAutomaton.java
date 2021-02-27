package Automatons;

import Fields.*;

import java.io.*;
import java.util.Arrays;
import java.util.stream.Stream;

public class LinearAutomaton {
    private Field field;
    private int m, n, k;
    private int[][] A, B, C, D;
    private int[][][] hFunctionTable, fFunctionTable;

    public LinearAutomaton(File initialData) {
        try (BufferedReader reader = new BufferedReader(new FileReader(initialData))) {
            field = FieldBuilder.createField(Integer.parseInt(reader.readLine()));
            mnkInit(reader.readLine());
            matrixInit();
            checkEmptyLine(reader.readLine());
            fillMatrix(A, reader);
            fillMatrix(B, reader);
            fillMatrix(C, reader);
            fillMatrix(D, reader);
        } catch (IOException e) {
            e.printStackTrace();
        }
        initTables();
        fillTables();
    }

    private void mnkInit(String mnkLine) {
        String[] mnk = mnkLine.split(" ");
        if (mnk.length != 3)
            throw new IllegalArgumentException("Error in line with m, n, k parameters");
        m = Integer.parseInt(mnk[0]);
        n = Integer.parseInt(mnk[1]);
        k = Integer.parseInt(mnk[2]);
    }

    private void matrixInit() {
        A = new int[n][n];
        B = new int[m][n];
        C = new int[n][k];
        D = new int[m][k];
    }

    private void checkEmptyLine(String line) {
        if (line == null)
            return;
        if (!line.equals(""))
            throw new IllegalArgumentException("No empty line between matrices");
    }

    private void fillMatrix(int[][] matrix, BufferedReader reader) {
        try {
            for (int i = 0; i < matrix.length; i++)
                matrix[i] = Stream.of(reader.readLine().split(" "))
                        .mapToInt(Integer::parseInt)
                        .toArray();
            checkEmptyLine(reader.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initTables() {
        hFunctionTable = new int[(int) Math.pow(field.cardinality(), n)][(int) Math.pow(field.cardinality(), m)][];
        fFunctionTable = new int[(int) Math.pow(field.cardinality(), n)][(int) Math.pow(field.cardinality(), m)][];
    }

    private void fillTables() {
        int[] s = new int[n];
        int[] x = new int[m];

        for (int i = 0; i < hFunctionTable.length && i < fFunctionTable.length; i++) {
            for (int j = 0; j < hFunctionTable[i].length && j < fFunctionTable[i].length; j++) {
                hFunctionTable[i][j] = hFunction(s, x);
                fFunctionTable[i][j] = fFunction(s, x);
                increaseByOne(x);
            }
            increaseByOne(s);
        }
    }

    private int[] hFunction(int[] s, int[] x) {
        checkLengthOf2Vectors(s.length, x.length);
        return vectorSum(mulOfVectorAndMatrix(s, A), mulOfVectorAndMatrix(x, B));
    }

    private int[] fFunction(int[] s, int[] x) {
        checkLengthOf2Vectors(s.length, x.length);
        return vectorSum(mulOfVectorAndMatrix(s, C), mulOfVectorAndMatrix(x, D));
    }

    private void checkLengthOf2Vectors(int l1, int l2) {
        if (l1 != n && l2 != m)
            throw new IllegalArgumentException("Multiplication can't be supported because there are incorrect length of vectors");
    }

    private int[] mulOfVectorAndMatrix(int[] v, int[][] m) {
        if (v.length != m.length)
            throw new IllegalArgumentException();

        int[] res = new int[m[0].length];
        for (int j = 0; j < m[0].length; j++)
            for (int i = 0; i < m.length; i++)
                res[j] = field.sum(res[j], field.mul(v[i], m[i][j]));

        return res;
    }

    private int[] vectorSum(int[] a, int[] b) {
        if (a.length != b.length)
            throw new IllegalArgumentException();
        for (int i = 0; i < a.length; i++)
            a[i] = field.sum(a[i], b[i]);
        return a;
    }

    private void increaseByOne(int[] arr) {
        for (int i = arr.length - 1; i >= 0; i--) {
            arr[i] = field.sum(arr[i], 1);
            if (arr[i] != 0)
                break;
        }
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("h:\n");
        createTable(result, hFunctionTable);
        result.append("\nf:\n");
        createTable(result, fFunctionTable);
        return result.toString();
    }

    private void createTable(StringBuilder result, int[][][] src) {
        for (int[][] table : src) {
            for (int[] line : table)
                result.append(Arrays.toString(line)).append(" ");
            result.append("\n");
        }
    }
}
