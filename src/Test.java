import Automatons.*;
import Fields.Field;
import Fields.PolynomialField;

import java.io.File;
import static java.lang.System.out;

public class Test {
    public static void main(String[] args) {
        String path = "C:\\Users\\Вячеслав\\IdeaProjects\\avtomati\\LR1\\InputFiles\\";
        LinearAutomaton automaton = new LinearAutomaton(new File(path + "LA\\Input.txt"));
        out.println(automaton);
        /*ShiftRegister rg = new ShiftRegister(new File(path + "RG\\TableInput.txt"));
        out.println(rg);*/
    }
}
