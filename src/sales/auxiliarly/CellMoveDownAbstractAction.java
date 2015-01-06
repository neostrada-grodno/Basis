package sales.auxiliarly;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JTable;
import sales.util.Util;

public class CellMoveDownAbstractAction extends AbstractAction {

    private JTable jTable;

    public CellMoveDownAbstractAction(JTable jTable) {
        this.jTable = jTable;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        int r = jTable.getSelectedRow();
        int c = jTable.getSelectedColumn();
        if (r != -1) {
            if (r < jTable.getRowCount() - 1) {
                r++;
            }
            Util.moveCell(r, c, jTable, true);
        }
    }
}
