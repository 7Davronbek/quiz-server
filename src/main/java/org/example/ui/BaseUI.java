package org.example.ui;


import static org.example.Main.scannerInt;
import static org.example.Main.wrongCommand;

public class BaseUI {
    public void quizStart() {
        boolean isExit = false;
        while (!isExit) {
            System.out.print("""
                    1. Sign up
                    2. Log in
                                        
                    0. Exit
                    >> \s""");
            int command = scannerInt.nextInt();
            switch (command) {
                case 1 -> signUp();
                case 2 -> logIn();
                case 0 -> isExit = true;
                default -> wrongCommand();
            }
        }
    }

    private void logIn() {
        new StudentUI().start();
    }

    private void signUp() {

    }
}
