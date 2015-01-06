package sales.auxiliarly;

import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import sales.SalesView;
import sales.interfaces.IClose;
import sales.util.Util;

public class MyInternalFrameAdapter extends InternalFrameAdapter {

    private IClose win;
    private Object parent;
    private SalesView salesView;

    public MyInternalFrameAdapter(IClose win, Object parent, SalesView salesView) {

        this.win = win;
        this.parent = parent;
        this.salesView = salesView;
    }

    public void internalFrameClosing(InternalFrameEvent e) {
        win.close();
        Util.closeJIFTab(win, salesView);
        Util.requestFocusInMain(parent, salesView);
        Util.openedWins--;
    }
    
    /*public void internalFrameIconified(InternalFrameEvent e) {
        ((JInternalFrame) e.getSource()).moveToFront();
    }*/
}
