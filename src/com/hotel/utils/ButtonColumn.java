package com.hotel.utils;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;

/**
 * ButtonColumn enables the addition of clickable buttons inside JTable cells.
 * Handles both rendering and editing events for table cell buttons.
 */
public class ButtonColumn extends AbstractCellEditor
        implements TableCellRenderer, TableCellEditor, ActionListener {

    private JTable table;
    private Action action;
    private JButton renderButton;
    private JButton editorButton;
    private String text;

    /**
     * Constructor to set up the button renderer and editor for the given column.
     *
     * @param table  the JTable where the button will be placed
     * @param action the action to be performed when the button is clicked
     * @param column the index of the column to place the button in
     */
    public ButtonColumn(JTable table, Action action, int column) {
        this.table = table;
        this.action = action;

        renderButton = new JButton();
        editorButton = new JButton();
        editorButton.setFocusPainted(false);
        editorButton.addActionListener(this);

        TableColumnModel columnModel = table.getColumnModel();
        columnModel.getColumn(column).setCellRenderer(this);
        columnModel.getColumn(column).setCellEditor(this);
    }

    /**
     * Returns the component used for drawing the cell in the table.
     */
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean isSelected, boolean hasFocus, int row, int column) {
        renderButton.setText((value == null) ? "" : value.toString());
        return renderButton;
    }

    /**
     * Returns the component used for editing the cell value.
     */
    @Override
    public Component getTableCellEditorComponent(JTable table, Object value,
                                                 boolean isSelected, int row, int column) {
        text = (value == null) ? "" : value.toString();
        editorButton.setText(text);
        return editorButton;
    }

    /**
     * Returns the value contained in the editor.
     */
    @Override
    public Object getCellEditorValue() {
        return text;
    }

    /**
     * Handles the button click event inside the table cell.
     * Converts the row index from view to model and triggers the associated action.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        fireEditingStopped();

        int viewRow = table.getEditingRow();
        if (viewRow < 0) {
            System.out.println("No editing row selected.");
            return;
        }

        int modelRow = table.convertRowIndexToModel(viewRow);
        System.out.println("✅ Button clicked at view row: " + viewRow + ", model row: " + modelRow);

        ActionEvent event = new ActionEvent(table, ActionEvent.ACTION_PERFORMED, String.valueOf(modelRow));
        action.actionPerformed(event);
    }
}
