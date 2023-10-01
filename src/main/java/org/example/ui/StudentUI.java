package org.example.ui;


import static org.example.Main.scannerInt;
import static org.example.Main.wrongCommand;

public class StudentUI {
    public void start() {
        boolean isExit = false;
        while (!isExit) {
            System.out.print("""
                    1. Start test
                    2. My solved tests
                                        
                    0. Back
                    >. \s""");
            int command = scannerInt.nextInt();
            switch (command) {
                case 1 -> startTest();
                case 2 -> mySolvedTests();
                case 0 -> isExit = true;
                default -> wrongCommand();
            }
        }
    }

    private void startTest() {
    }

    private void mySolvedTests() {
    }
}
