package com.hotel.patterns;

import java.util.ArrayList;
import java.util.List;

public class InventoryNotifier {

    // List of observers subscribed to inventory updates
    private static final List<InventoryObserver> observers = new ArrayList<>();

    /**
     * Adds an observer to the notification list.
     *
     * @param observer the observer to be added
     */
    public static void addObserver(InventoryObserver observer) {
        observers.add(observer);
    }

    /**
     * Notifies all subscribed observers with the list of low stock items.
     *
     * @param lowStockItems the list of items that are low in stock
     */
    public static void notifyObservers(List<String> lowStockItems) {
        for (InventoryObserver observer : observers) {
            observer.update(lowStockItems);
        }
    }
}