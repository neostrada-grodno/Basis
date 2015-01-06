package sales.auxiliarly;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JTable;
import sales.util.Util;

public class CellMoveAbstractAction extends AbstractAction {

    private JTable jTable;
    private boolean isEnter;

    public CellMoveAbstractAction(JTable jTable, boolean isEnter) {
        
        this.jTable = jTable;
        this.isEnter = isEnter;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

            int r = jTable.getSelectedRow();
            int c = jTable.getSelectedColumn();
            if (r != -1) {
                if (c < jTable.getColumnCount() - 1) {
                    c++;
                } else {
                    if (r < jTable.getRowCount() - 1) {
                        c = 1;
                        r++;
                    }
                }
                Util.moveCell(r, c, jTable, isEnter);
            }
    }
}
