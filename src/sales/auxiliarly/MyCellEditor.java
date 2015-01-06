package sales.auxiliarly;

import java.awt.Component;
import javax.swing.AbstractCellEditor;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.table.TableCellEditor;

public class MyCellEditor extends AbstractCellEditor implements TableCellEditor {

    private JTextField comp;

    public MyCellEditor(JTextField comp) {
        this.comp = comp;
    }

    public Component getTableCellEditorComponent(JTable table, Object value,
            boolean isSelected, int rowIndex, int vColIndex) {

        comp.setText(value.toString());
        comp.selectAll();
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                if (comp.getText().length() > 1) {
                    comp.selectAll();
                }
            }
        });
        return comp;
    }

    public Object getCellEditorValue() {
        return comp.getText();
    }
}