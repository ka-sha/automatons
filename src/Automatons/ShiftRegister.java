package Automatons;

import Fields.*;

import java.io.*;
import java.util.*;
import java.util.stream.Stream;

public class ShiftRegister {
    private Field field;
    private int n;
    private int[][] phiTable, psiTable;
    private int[][] fFunctionTable;
    private int[][][] hFunctionTable;

    public ShiftRegister(File initialData) {
        try (BufferedReader reader = new BufferedReader(new FileReader(initialData))) {
            field = FieldBuilder.createField(Integer.parseInt(reader.readLine()));
            n = Integer.parseInt(reader.readLine());
            checkN(n);
            initTables();
            checkEmptyLine(reader.readLine());
            if (isTableInit(reader)) {
                reader.reset();
                fillSRFunctions(phiTable, reader);
                checkEmptyLine(reader.readLine());
                fillSRFunctions(psiTable, reader);
            } else {
                reader.reset();
                //TODO: analytic init
            }
            fillAutomatonTables();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void checkN(int n) {
        int p = field.cardinality();
        if (p == 2 && !(n >= 2 && n <= 64))
            throw new IllegalArgumentException("incorrect n for p = 2");
        if (p == 3 && !(n >= 2 && n <= 32))
            throw new IllegalArgumentException("incorrect n for p = 3");
        if (p == 5 && !(n >= 2 && n <= 16))
            throw new IllegalArgumentException("incorrect n for p = 5");
        if ((p == 4 || p == 8 || p == 16 || p == 32 || p == 64 || p == 128 || p == 256) && !(n >= 1 && n <= 8))
            throw new IllegalArgumentException("incorrect n for p = 2^n");
    }

    private void initTables() {
        phiTable = new int[(int) Math.pow(field.cardinality(), n)][field.cardinality()];
        psiTable = new int[phiTable.length][field.cardinality()];
        hFunctionTable = new int[phiTable.length][field.cardinality()][];
        fFunctionTable = new int[phiTable.length][field.cardinality()];
    }

    private void checkEmptyLine(String line) {
        if (line == null)
            return;
        if (!line.equals(""))
            throw new IllegalArgumentException("No empty line between different types of data");
    }

    private boolean isTableInit(BufferedReader reader) throws IOException {
        reader.mark(256);
        return reader.readLine().matches("^(([\\d+\\s]){2,}([\\d+]))$");
    }

    private void fillSRFunctions(int[][] table, BufferedReader reader) throws IOException {
        for (int i = 0; i < table.length; i++)
            table[i] = Stream.of(reader.readLine().split(" "))
                    .mapToInt(Integer::parseInt)
                    .toArray();
    }

    private void fillAutomatonTables() {
        int[] s = new int[n];
        for (int i = 0; i < hFunctionTable.length && i < fFunctionTable.length; i++) {
            int x = 0;
            for (int j = 0; j < hFunctionTable[i].length && j < fFunctionTable[i].length; j++) {
                hFunctionTable[i][j] = hFunction(s, x);
                fFunctionTable[i][j] = fFunction(s, x);
                x = field.sum(x, 1);
            }
            increaseByOne(s);
        }
    }

    private int[] hFunction(int[] s, int x) {
        int[] res = new int[s.length];
        int index = functionLineIndex(s);
        System.arraycopy(s, 1, res, 0, s.length - 1);
        res[res.length - 1] = phiTable[index][x];
        return res;
    }

    private int fFunction(int[] s, int x) {
        int index = functionLineIndex(s);
        return psiTable[index][phiTable[index][x]];
    }

    private int functionLineIndex(int[] s) {
        int[] counter = new int[s.length];
        for (int i = 0; i < Math.pow(field.cardinality(), n); i++) {
            if (Arrays.equals(counter, s))
                return i;
            increaseByOne(counter);
        }
        return -1;
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
        for (int[][] table : hFunctionTable) {
            for (int[] line : table)
                result.append(Arrays.toString(line)).append(" ");
            result.append("\n");
        }
        result.append("\nf:\n");
        for (int[] line : fFunctionTable)
            result.append(Arrays.toString(line)).append("\n");
        return result.toString();
    }
}