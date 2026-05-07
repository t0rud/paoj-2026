package com.pao.laboratory07.exercise1;

import com.pao.laboratory07.exercise1.exceptions.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        if (!scanner.hasNext()) return;

        // Part A: load initial state
        OrderState initialState = OrderState.valueOf(scanner.next());
        Order order = new Order(initialState);
        System.out.println(initialState); // Cerința cere doar starea, fără "Initial order state: "

        while (scanner.hasNext()) {
            OrderCommand orderCommand = OrderCommand.valueOf(scanner.next());
            switch (orderCommand) {
                case next -> {
                    try {
                        order.nextState();
                        System.out.println(order.getCurrentState());
                    } catch (OrderIsAlreadyFinalException e) {
                        System.out.println("Comanda este in stare finala.");
                    }
                }
                case cancel -> {
                    try {
                        order.cancel();
                        System.out.println(order.getCurrentState());
                    } catch (CannotCancelFinalOrderException e) {
                        System.out.println("Comanda este in stare finala.");
                    }
                }
                case undo -> {
                    try {
                        order.undoState();
                        System.out.println(order.getCurrentState());
                    } catch (CannotRevertInitialOrderStateException e) {
                        System.out.println("Nu există stare anterioară pentru undo.");
                    }
                }
                case QUIT -> {
                    System.out.println("User quit the program.");
                    return;
                }
            }
        }
    }
}