package com.hotel.patterns;

import java.util.List;

/**
 * Observer interface for inventory notifications.
 * Classes implementing this interface will respond to low stock alerts.
 */
public interface InventoryObserver {

    /**
     * Method called when the observer is notified of low stock items.
     *
     * @param lowStockItems the list of items that are low in stock
     */
    void update(List<String> lowStockItems);
}