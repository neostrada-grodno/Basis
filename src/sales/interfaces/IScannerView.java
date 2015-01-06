package sales.interfaces;

import javax.swing.RootPaneContainer;

public interface IScannerView extends RootPaneContainer {
    public boolean getScanEnter();
    public void setScanEnter(boolean scanEnter);
    public String getScanCode();
    public void setScanCode(String scanCode);
    public void useScancode();
}
