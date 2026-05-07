package com.pao.laboratory07.exercise1;

import com.pao.laboratory07.exercise1.exceptions.*;
import java.util.Stack;

public class Order {
    private OrderState currentState;
    private final Stack<OrderState> history;

    public Order(OrderState initialState) {
        this.currentState = initialState;
        this.history = new Stack<>();
    }

    public OrderState getCurrentState() {
        return currentState;
    }

    public void nextState() throws OrderIsAlreadyFinalException {
        if (currentState.isFinal()) {
            throw new OrderIsAlreadyFinalException();
        }
        // Salvăm starea înainte de a o modifica
        history.push(currentState);

        switch (currentState) {
            case PLACED -> currentState = OrderState.PROCESSED;
            case PROCESSED -> currentState = OrderState.SHIPPED;
            case SHIPPED -> currentState = OrderState.DELIVERED;
        }
    }

    public void cancel() throws CannotCancelFinalOrderException {
        if (currentState.isFinal()) {
            throw new CannotCancelFinalOrderException();
        }
        history.push(currentState);
        currentState = OrderState.CANCELED;
    }

    public void undoState() throws CannotRevertInitialOrderStateException {
        if (history.isEmpty()) {
            throw new CannotRevertInitialOrderStateException();
        }
        // Scoatem ultima stare din istoric și o setăm ca stare curentă
        currentState = history.pop();
    }
}