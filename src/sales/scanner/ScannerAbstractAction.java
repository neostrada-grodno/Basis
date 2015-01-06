package sales.scanner;

import sales.interfaces.IScannerView;
import java.awt.event.ActionEvent;

public class ScannerAbstractAction extends javax.swing.AbstractAction {

    public ScannerAbstractAction(IScannerView scannerView, char ch) {
        super();
        this.scannerView = scannerView; 
        this.ch = ch;
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (scannerView.getScanEnter() && ch == '%') {
            scannerView.setScanEnter(false);
            scannerView.useScancode();
            return;
        }
        if (scannerView.getScanEnter()) {
            scannerView.setScanCode(scannerView.getScanCode() + ch);
        }
        if (ch == '!') {
            scannerView.setScanEnter(true);
            scannerView.setScanCode("");
        }
    }
    
    private IScannerView scannerView;
    private char ch;
}