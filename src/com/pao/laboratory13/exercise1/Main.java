package com.pao.laboratory13.exercise1;

import java.util.Scanner;

public class Main {

    // Definim starile explicite cerute de barem
    enum State {
        INIT, AUTH, OPEN, CLOSED
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Citim numarul de comenzi si curatam newline-ul
        if (!scanner.hasNextLine()) return;
        int q;
        try {
            q = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return; // Protectie daca primul rand nu e numar
        }

        State currentState = State.INIT;
        int historyCount = 0;
        int processedCommands = 0;

        // Procesam exact Q comenzi, ignorand liniile goale
        while (processedCommands < q && scanner.hasNextLine()) {
            String line = scanner.nextLine().trim();
            if (line.isEmpty()) {
                continue; // Liniile goale sunt ignorate la parsare
            }

            processedCommands++;

            // Separam token-urile prin unul sau mai multe spatii
            String[] tokens = line.split("\\s+");
            String command = tokens[0];

            switch (command) {
                case "AUTH":
                    // 1. Verificare sintaxa
                    if (tokens.length < 2) {
                        System.out.println("ERR E_PARSE AUTH");
                    }
                    // 2. Verificare stare
                    else if (currentState == State.CLOSED) {
                        System.out.println("ERR E_STATE CLOSED");
                    }
                    // 3. Succes (inclusiv re-AUTH din OPEN)
                    else {
                        currentState = State.AUTH;
                        historyCount = 0; // Se reseteaza history la fiecare AUTH
                        String user = tokens[1];
                        System.out.println("OK AUTH user=" + user);
                    }
                    break;

                case "OPEN":
                    if (tokens.length > 1) {
                        System.out.println("ERR E_PARSE OPEN");
                    } else if (currentState == State.CLOSED) {
                        System.out.println("ERR E_STATE CLOSED");
                    } else if (currentState == State.OPEN) {
                        System.out.println("ERR E_STATE ALREADY_OPEN");
                    } else if (currentState == State.INIT) {
                        System.out.println("ERR E_STATE NOT_OPEN");
                    } else {
                        currentState = State.OPEN;
                        System.out.println("OK OPEN");
                    }
                    break;

                case "SEND":
                    if (tokens.length < 2) {
                        System.out.println("ERR E_PARSE SEND");
                    } else if (currentState == State.CLOSED) {
                        System.out.println("ERR E_STATE CLOSED");
                    } else if (currentState != State.OPEN) {
                        System.out.println("ERR E_STATE NOT_OPEN");
                    } else {
                        historyCount++; // Incrementam doar la SEND reusit
                        System.out.println("OK OPEN sent");
                    }
                    break;

                case "BROADCAST":
                    if (tokens.length < 2) {
                        System.out.println("ERR E_PARSE BROADCAST");
                    } else if (currentState == State.CLOSED) {
                        System.out.println("ERR E_STATE CLOSED");
                    } else if (currentState != State.OPEN) {
                        System.out.println("ERR E_STATE NOT_OPEN");
                    } else {
                        historyCount++; // Incrementam doar la BROADCAST reusit
                        System.out.println("OK OPEN broadcast");
                    }
                    break;

                case "HISTORY":
                    if (tokens.length > 1) {
                        System.out.println("ERR E_PARSE HISTORY");
                    } else if (currentState == State.CLOSED) {
                        System.out.println("ERR E_STATE CLOSED");
                    } else if (currentState != State.OPEN) {
                        System.out.println("ERR E_STATE NOT_OPEN");
                    } else {
                        System.out.println("OK OPEN history=" + historyCount);
                    }
                    break;

                case "CLOSE":
                    if (tokens.length > 1) {
                        System.out.println("ERR E_PARSE CLOSE");
                    } else if (currentState == State.CLOSED) {
                        System.out.println("ERR E_STATE CLOSED");
                    } else if (currentState == State.INIT || currentState == State.AUTH) {
                        System.out.println("ERR E_STATE NOT_OPEN");
                    } else {
                        currentState = State.CLOSED;
                        System.out.println("OK CLOSED");
                    }
                    break;

                default:
                    // Orice alt token nerecunoscut
                    System.out.println("ERR E_PARSE UNKNOWN_COMMAND");
                    break;
            }
        }

        scanner.close();
    }
}