package sales.util;

import com.ice.jni.registry.Registry;
import com.ice.jni.registry.RegistryKey;
import com.toedter.calendar.JDateChooser;
import com.toedter.calendar.JTextFieldDateEditor;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.FontMetrics;
import java.awt.Rectangle;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.StringTokenizer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import javax.swing.AbstractCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDesktopPane;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import javax.swing.text.JTextComponent;
import org.apache.log4j.Logger;
import org.apache.maven.artifact.versioning.DefaultArtifactVersion;
import org.hibernate.classic.Session;
import org.jopendocument.dom.spreadsheet.Sheet;
import org.jopendocument.dom.spreadsheet.SpreadSheet;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import sales.auxiliarly.AppStartUpPath;
import sales.auxiliarly.CellMoveAbstractAction;
import sales.auxiliarly.CellMoveDownAbstractAction;
import sales.auxiliarly.MyInternalFrameAdapter;
import sales.SalesApp;
import sales.SalesView;
import sales.entity.Employee;
import sales.entity.Outcomingpayments;
import sales.entity.Register;
import sales.entity.Serialtable;
import sales.interfaces.IClose;
import sales.interfaces.IHashAndSave;
import sales.interfaces.IJournal;
import sales.scanner.ScannerAbstractAction;
import sales.interfaces.IScannerView;
import sales.outcoming.OutcomingDocView;
import sales.outcoming.TNHeader;
import sales.outcoming.TTNHeader;

public class Util {

    static Logger logger = Logger.getLogger(SalesApp.class.getName());
    private static final SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yy");
    static final private int NUM_PRICELIST = 5;//the number of price items per line

    public static void autoResizeColWidth(JTable table) {

        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        final int margin = 5;
        //int maxWidth = (int) (table.getPreferredSize().getWidth() * 0.5);

        for (int i = 0; i < table.getColumnCount(); i++) {

            FontMetrics fm = table.getFontMetrics(table.getFont());
            DefaultTableColumnModel colModel = (DefaultTableColumnModel) table.getColumnModel();
            TableColumn col = colModel.getColumn(i);
            int width = fm.stringWidth(col.getHeaderValue().toString()) + 10;

            TableModel tm = table.getModel();
            for (int j = 0; j < tm.getRowCount(); j++) {
                if (tm.getValueAt(j, i) != null) {
                    width = Math.max(fm.stringWidth(tm.getValueAt(j, i).toString()), width);
                }
            }
            width += 2 * margin;
            /*if (width > maxWidth) {
                col.setPreferredWidth(width);
            } else {
                col.setPreferredWidth(width);
                col.setMaxWidth(width);
            }*/
            //width = Math.min(width, maxWidth);
            col.setPreferredWidth(width);
            ((DefaultTableCellRenderer) table.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(SwingConstants.LEFT);
        }

        table.getTableHeader().setReorderingAllowed(false);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
    }
    
    public static int getFinalPriceList() {
        return NUM_PRICELIST;
    }

    public static void autoResizeColWidth1(JTable table) {

        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        int margin = 5;

        for (int i = 0; i < table.getColumnCount(); i++) {
            int vColIndex = i;
            DefaultTableColumnModel colModel = (DefaultTableColumnModel) table.getColumnModel();
            TableColumn col = colModel.getColumn(vColIndex);
            int width = 0;

            // Get width of column header
            TableCellRenderer renderer = col.getHeaderRenderer();

            if (renderer == null) {
                renderer = table.getTableHeader().getDefaultRenderer();
            }

            Component comp = renderer.getTableCellRendererComponent(table, col.getHeaderValue(), false, false, 0, 0);

            width = comp.getPreferredSize().width;

            // Get maximum width of column data
            for (int r = 0; r < table.getRowCount(); r++) {
                renderer = table.getCellRenderer(r, vColIndex);
                comp = renderer.getTableCellRendererComponent(table, table.getValueAt(r, vColIndex), false, false,
                        r, vColIndex);
                width = Math.max(width, comp.getPreferredSize().width);
            }

            // Add margin
            width += 2 * margin;

            // Set the width
            col.setPreferredWidth(width);
        }

        ((DefaultTableCellRenderer) table.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(
                SwingConstants.LEFT);

        table.getTableHeader().setReorderingAllowed(false);

        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
    }
    static double widthSpan = 0.5;
    static double heightSpan = 0.5;
    static int downMargin = 54;

    public static void setFrameLocation(JInternalFrame jif, SalesView salesView) {
        jif.setLocation((int) ((salesView.getFrame().getWidth() - jif.getWidth()) * widthSpan * Math.random()),
                (int) ((salesView.getFrame().getHeight() - jif.getHeight() - downMargin) * heightSpan * Math.random()));
    }
    public static int openedWins;

    public static void initJIF(JInternalFrame win, Object parent, SalesView salesView) {
        win.setLocation((int) ((salesView.getFrame().getWidth() - win.getWidth()) * widthSpan * Math.random()),
                (int) ((salesView.getFrame().getHeight() - win.getHeight() - downMargin) * heightSpan * Math.random()));
        win.setVisible(true);
        try {
            win.setSelected(true);
        } catch (Exception e) {
            logger.error(e);
        }
        win.addInternalFrameListener(new MyInternalFrameAdapter((IClose) win, parent, salesView));
        openedWins++;
    }
    public static List wins;

    public static void initJIF(JInternalFrame win, String winName, Object parent, SalesView salesView) {
        initJIF(win, parent, salesView);
        salesView.getTabs().add(winName, null);
        wins.add(win);
        salesView.getTabs().setSelectedIndex(wins.size());
    }

    public static void requestFocusInMain(Object parent, SalesView salesView) {
        if (openedWins == 0) {
            salesView.getWorkplace().setFocus();
        }
    }

    public static void closeJIF(JInternalFrame win, Object parent, SalesView salesView) {

        win.dispose();

        requestFocusInMain(parent, salesView);
    }

    public static void closeJIFTab(Object win, SalesView salesView) {
        for (int i = 0; i < wins.size(); i++) {
            if (wins.get(i) == win) {
                salesView.getTabs().remove(i + 1);
                wins.remove(i);
                break;
            }
        }

    }

    public static void initJTable(JTable jTable) {
        if (jTable.getRowCount() > 0) {
            jTable.setRowSelectionInterval(0, 0);
            jTable.setColumnSelectionInterval(0, jTable.getColumnCount() - 1);
        }
    }

    public static String removeSpaces(String s) {
        String t = "";
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) >= '0' && s.charAt(i) <= '9' || s.charAt(i) == ',') {
                t += s.charAt(i);
            }
        }
        return t;
    }

    public static String removeSpacesStr(String s) {
        String t = "";
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) != ' ') {
                t += s.charAt(i);
            }
        }
        return t;
    }

    //convert string to integer
    public static Integer str2int(String s) {
        Integer res = 0;
        try {
            s = removeSpaces(s);
            s = s.replaceAll(",", ".");
            if (!s.equals("")) {
                res = new Integer((new Double(s)).intValue());
            }
        } catch (Exception e) {
            logger.error(e);
        }
        return res;
    }

    //convert string to long
    public static long str2long(String s) {
        long res = 0;
        try {
            s = removeSpaces(s);
            s = s.replaceAll(",", "");
            if (!s.equals("")) {
                res = Long.parseLong(s);
            }
        } catch (Exception e) {
            logger.error(e);
        }
        return res;
    }

    //get int value from unknown type of the given object
    public static int getIntObj(Object o) {

        int res = 0;

        if (o != null) {

            if (o instanceof BigDecimal) {
                res = ((BigDecimal) o).intValue();

            } else if (o instanceof BigInteger) {

                res = ((BigInteger) o).intValue();

            } else if (o instanceof Integer) {
                res = (Integer) o;
            }
        }

        return res;
    }

    //get long from an object
    public static long getLongObj(Object o) {
        long res = 0;
        if (o != null) {
            res = ((BigDecimal) o).longValue();
        }
        return res;
    }

    public static String extractNumber(String s) {
        String t = "";
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) >= '0' && s.charAt(i) <= '9') {
                t += s.charAt(i);
            }
        }
        return t;
    }
    
    public static String getStr(String s) {
        if(s == null) {
            s = "";
        }
        return s;
    }

    public static void addKey(char ch, IScannerView scannerView) {
        ScannerAbstractAction aa = new ScannerAbstractAction(scannerView, ch);
        KeyStroke ks = KeyStroke.getKeyStroke(ch);
        scannerView.getRootPane().getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(ks, ch);
        scannerView.getRootPane().getActionMap().put(ch, aa);
    }

    public static void addScannerKeys(IScannerView scannerView) {
        addKey('!', scannerView);
        for (int i = 0; i < 10; i++) {
            addKey(Character.forDigit(i, 10), scannerView);
        }
        addKey('%', scannerView);
    }

    public static void setMoveRight(JTable jTable) {
        CellMoveAbstractAction handleEnter = new CellMoveAbstractAction(jTable, true);
        jTable.getInputMap().put(KeyStroke.getKeyStroke("ENTER"), "selectNextColumnCell");
        jTable.getActionMap().put("selectNextColumnCell", handleEnter);

        CellMoveAbstractAction handleTab = new CellMoveAbstractAction(jTable, false);
        jTable.getInputMap().put(KeyStroke.getKeyStroke("TAB"), "moveNextColumnCell");
        jTable.getActionMap().put("moveNextColumnCell", handleTab);
    }

    public static void setMoveDown(JTable jTable) {
        CellMoveDownAbstractAction handleEnter = new CellMoveDownAbstractAction(jTable);
        jTable.getInputMap().put(KeyStroke.getKeyStroke("ENTER"), "selectNextRowCell");
        jTable.getActionMap().put("selectNextRowCell", handleEnter);

    }

    //create filter substring for an sql query for the given value over a set fields
    static private String createValueFilter(String[] fields, String value) {
        String sf = "";
        for (int i = 0; i < fields.length; i++) {
            sf += "(x." + fields[i] + " like '%" + value + "%')";
            if (i < fields.length - 1) {
                sf += " or ";
            }
        }
        return sf;
    }

    //create filter substring for an sql query over the given fields
    //provided a string of tokens (entered by a user)
    public static String getFilter(String tokens, String[] fields) {

        String filter = "";

        StringTokenizer st = new StringTokenizer(tokens.trim());
        ArrayList ts = new ArrayList();
        while (st.hasMoreTokens()) {
            ts.add(st.nextElement());
        }
        if (ts.size() > 0) {
            filter += " and ";
            for (int i = 0; i < ts.size(); i++) {
                if (i == 0) {
                    filter += "(";
                }
                filter += createValueFilter(fields, (String) ts.get(i));
                if (i < ts.size() - 1) {
                    filter += ") and (";
                }
            }
        }
        if (!filter.isEmpty()) {
            filter += ")";
        }
        return filter;
    }

    //check correctness of a number
    public static boolean checkNumber(String num) {
        for (int i = 0; i < num.length(); i++) {
            if (num.charAt(i) < '0' || num.charAt(i) > '9') {
                return false;
            }
        }
        return true;
    }

    //create a barcode given a string of corresponding characters
    public static String ean13(String chain) {
        String barcode = "";
        //sanity check
        if (chain.length() != 12) {
            return "";
        }
        for (int i = 0; i < 12; i++) {
            if (chain.charAt(i) < '0' || chain.charAt(i) > '9') {
                return "";
            }
        }
        //following the algorithm
        int checksum = 0;
        for (int i = 1; i < 12; i += 2) {
            checksum += Integer.parseInt("" + chain.charAt(i));
        }
        checksum *= 3;
        for (int i = 0; i < 12; i += 2) {
            checksum += Integer.parseInt("" + chain.charAt(i));
        }
        chain += (10 - checksum % 10) % 10;
        barcode = "" + chain.charAt(0) + (char) (65 + Integer.parseInt("" + chain.charAt(1)));
        int f = Integer.parseInt("" + chain.charAt(0));
        for (int i = 2; i < 7; i++) {
            boolean ta = false;
            if (i == 2) {
                ta = f >= 0 && f <= 3;
            }
            if (i == 3) {
                ta = f == 0 || f == 4 || f == 7 || f == 8;
            }
            if (i == 4) {
                ta = f == 0 || f == 1 || f == 4 || f == 5 || f == 9;
            }
            if (i == 5) {
                ta = f == 0 || f == 2 || f == 5 || f == 6 || f == 7;
            }
            if (i == 6) {
                ta = f == 0 || f == 3 || f == 6 || f == 8 || f == 9;
            }
            if (ta) {
                barcode += (char) (65 + Integer.parseInt("" + chain.charAt(i)));
            } else {
                barcode += (char) (75 + Integer.parseInt("" + chain.charAt(i)));
            }
        }
        barcode += "*";
        for (int i = 7; i < 13; i++) {
            barcode += (char) (97 + Integer.parseInt("" + chain.charAt(i)));
        }
        barcode += "+";
        return barcode;
    }

    public static void moveCell(int r, int c, JTable jTable, boolean isEnter) {

        if (r != -1) {

            AbstractCellEditor ed = (AbstractCellEditor) jTable.getCellEditor();
            if (ed != null) {
                ed.stopCellEditing();
            }
            jTable.setRowSelectionInterval(r, r);
            jTable.setColumnSelectionInterval(c, c);
        }
    }
    
    public static String date2str(Date date) {
        
        String str = "";
        
        if (date != null) {
            str = sdf.format(date);
        }
        
        return str;
    }

    public static String date2String(Date date) {

        String str = "";

        if (date != null) {
            String months[] =
                    new String[]{"Января", "Февраля", "Марта", "Апреля", "Мая", "Июня", "Июля", "Августа", "Сентября", "Октября", "Ноября", "Декабря"};
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            str = "" + c.get(Calendar.DATE) + " " + months[c.get(Calendar.MONTH)] + " " + c.get(Calendar.YEAR) + " г.";
        }

        return str;
    }

    public static void openDoc(String doc) {
        
        try {            
            Runtime.getRuntime().exec("cmd start \"taskkill /f /IM soffice.exe\"");
            Desktop.getDesktop().open(new File(doc));
            
        } catch (Exception e) {
            logger.error(e);
        }
    }

    public static void runBatch(String command) {
        try {
            File f = new File("x.bat");
            if (!f.exists()) {
                f.createNewFile();
            }
            FileWriter fw = new FileWriter(f);
            fw.write(command);
            fw.close();
            Runtime.getRuntime().exec("cmd start /c x.bat");
        } catch (Exception e) {
            logger.error(e);
        }
    }

    public static class SelectLater implements Runnable {

        JTextComponent comp;

        public SelectLater(JTextComponent c) {
            comp = c;
        }

        @Override
        public void run() {
            comp.selectAll();
        }
    }

    public static void checkDocSaved(IHashAndSave doc) {

        if (doc.getHash() == null || !doc.getHash().equals(doc.getHashCode())) {
            doc.save();
        }
    }

    public static void checkDocSaved(OutcomingDocView doc) {
        
        if (doc.getHash() == null || !doc.getHash().equals(doc.getHashCode())) {
            doc.save();
        }
    }

    //compute hash code recursively through components
    static private ArrayList hashCodeRec(Component c, ArrayList hashList) {

        //compute hash depending on the type of the component
        if (c instanceof JTextField && !(c instanceof JTextFieldDateEditor)) {
            //don't need to compute hash for the filter string
            if (!((JTextField) c).getName().equals("jtfFilter")) {
                hashList.add(((JTextField) c).getText().hashCode());
            }
        } else if (c instanceof JDateChooser) {
            if (((JDateChooser) c).getDate() != null) {
                hashList.add(((JDateChooser) c).getDate());
            }
        } else if (c instanceof JSpinner) {
            hashList.add(((JSpinner) c).getValue());
        } else if (c instanceof JCheckBox) {
            hashList.add(((JCheckBox) c).isSelected() ? 1 : 0);
        } else if (c instanceof JComboBox) {
            hashList.add(((JComboBox) c).getSelectedIndex());
        } else if (c instanceof JTextArea) {
            hashList.add(((JTextArea) c).getText().hashCode());
        } else if (c instanceof JScrollPane) {
            Component c2 = ((JScrollPane) c).getViewport().getView();
            if (c2 instanceof JTextArea) {
                hashList.add(((JTextArea) c2).getText().hashCode());
            } else if (c2 instanceof JEditorPane) {
                hashList.add(((JEditorPane) c2).getText().hashCode());
            } else if (c2 instanceof JTable) {
                for (int i = 0; i < ((JTable) c2).getRowCount(); i++) {
                    for (int j = 0; j < ((JTable) c2).getColumnCount(); j++) {
                        if (((JTable) c2).getValueAt(i, j) != null) {
                            hashList.add(((JTable) c2).getValueAt(i, j).hashCode());
                        }
                    }
                }
            }
        } else if (c instanceof JPanel) {
            //in case of JPanel compute the hash of all its children
            for (Component c2 : ((JPanel) c).getComponents()) {
                hashList = hashCodeRec(c2, hashList);
            }
        }
        return hashList;
    }

    //compute hash for the given frame recursively
    public static int hash(JInternalFrame frame) {
        ArrayList hashList = new ArrayList();
        for (Component c : frame.getContentPane().getComponents()) {
            hashList = hashCodeRec(c, hashList);
        }
        return hashList.hashCode();
    }

    //compute hash for the given dialog recursively
    public static int hash(JDialog dialog) {
        ArrayList hashList = new ArrayList();
        for (Component c : dialog.getContentPane().getComponents()) {
            hashList = hashCodeRec(c, hashList);
        }
        return hashList.hashCode();
    }
    //compute hash for the given panel recursively

    public static int hash(JPanel panel) {
        ArrayList hashList = new ArrayList();
        for (Component c : panel.getComponents()) {
            hashList = hashCodeRec(c, hashList);
        }
        return hashList.hashCode();
    }
    //the map of types between database and the outcoming doc
    public static int[] types = new int[]{0, 6, 3, 2, 1, 4, 5};

    //compute hash for an outcoming doc
    public static int hash(OutcomingDocView outcomingDocView) {

        //hash for the form
        ArrayList hashList = new ArrayList();
        for (Component c : outcomingDocView.getContentPane().getComponents()) {
            hashList = hashCodeRec(c, hashList);
        }
        //hash for the associated to the doc serials
        List serials = outcomingDocView.getSerials();
        for (int i = 0; i < serials.size(); i++) {
            ArrayList s = (ArrayList) serials.get(i);
            for (int j = 0; j < s.size(); j++) {
                Serialtable st = (Serialtable) s.get(j);
                hashList.add(st.getPosition());
                hashList.add(st.getProductCode());
                hashList.add(st.getSerial());
            }
        }
        int type = outcomingDocView.getType();
        if (type == 0 || type == 1) {
            //for TN we need to process the subform TNHeader
            TNHeader tnheader = outcomingDocView.getTNHeader();
            if (tnheader != null) {
                hashList.add(hash(tnheader));
            }
            //if the doc is TN we have the list of payments
            List payments = outcomingDocView.getPayments();
            for (int i = 0; i < payments.size(); i++) {
                Outcomingpayments op = (Outcomingpayments) payments.get(i);
                hashList.add(op.getDatetime());
                hashList.add(op.getAmount());
                hashList.add(op.getNote().hashCode());
            }
            if (type == 1) {
                //if the doc is TTN we process it's subheader
                TTNHeader ttnheader = outcomingDocView.getTTNHeader();
                if (ttnheader != null) {
                    hashList.add(hash(ttnheader));
                }
            }
        }

        return hashList.hashCode();
    }

    public static void updateJournals(SalesView salesView, Class journalClass) {
        JDesktopPane jdp = salesView.getJDesktopPane();
        for (JInternalFrame frame : jdp.getAllFrames()) {
            if (frame.getClass() == journalClass) {
                ((IJournal) frame).showTable();
            }
        }
    }

    public static Integer roundInt(Integer x) {
        String rs = HUtil.getConstant("round");
        Integer r = Integer.parseInt(rs);
        boolean r5 = rs.substring(0, 1).equals("5");
        if (r5) {
            x *= 2;
            r *= 2;
        }
        Integer y = (int) Math.round(((double) x) / r) * r;
        if (r5) {
            y = y / 2;
        }
        return y;
    }

    public static void selectFirstRow(JTable jTable) {
        if (jTable.getRowCount() > 0) {
            jTable.getSelectionModel().addSelectionInterval(0, 0);
            jTable.getColumnModel().getSelectionModel().setSelectionInterval(1, 1);
        }
    }

    public static Date composeDate(Date date, Date time) {

        Date rd = null;

        String strDate = (new SimpleDateFormat("yy-MM-dd")).format(date) + " "
                + (new SimpleDateFormat("HH:mm")).format(time);
        try {
            rd = (new SimpleDateFormat("yy-MM-dd HH:mm")).parse(strDate);
        } catch (Exception e) {
            logger.error(e);
        }
        return rd;
    }

    public static String getConfItem(String name, Document doc) {
        String item = "";
        NodeList nodes = doc.getElementsByTagName("property");
        for (int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);
            if (node.getAttributes().getNamedItem("name").getNodeValue().equals(name)) {
                if (node.getFirstChild() != null) {
                    item = node.getFirstChild().getNodeValue();
                }
                break;
            }
        }
        return item;
    }

    //unzip a file
    public static void unzip(File f, String destPath) {

        //open the file
        try (ZipFile zipFile = new ZipFile(f)) {
            Enumeration entries = zipFile.entries();

            //go throgh entries
            while (entries.hasMoreElements()) {
                ZipEntry entry = (ZipEntry) entries.nextElement();

                //create directory if it is so
                if (entry.isDirectory()) {
                    (new File(destPath + "\\" + entry.getName())).mkdir();
                    continue;
                }

                //unzip the current file
                byte[] buffer = new byte[100000];
                InputStream is = zipFile.getInputStream(entry);
                BufferedOutputStream bos =
                        new BufferedOutputStream(
                        new FileOutputStream(destPath + "\\" + entry.getName()));
                while (true) {
                    int length = is.read(buffer);
                    if (length < 0) {
                        break;
                    }
                    bos.write(buffer, 0, length);
                }
                is.close();
                bos.close();
            }
        } catch (Exception e) {
            logger.error(e);
        }
    }

    public static String resModule(String version) {

        String c = "";
        try {
            byte[] d = MessageDigest.getInstance("MD5").digest((version + HUtil.getConstant("unt")).getBytes());
            for (int i = 0; i < 8; i++) {
                int sm = 0;
                for (int j = 0; j < 1; j++) {
                    sm += d[i * 2 + j];
                }
                byte dt = (byte) (sm % 10);
                if (dt < 0) {
                    dt = (byte) -dt;
                }
                c += dt;
            }

        } catch (Exception e) {
            logger.error(e);
        }

        return c;
    }

    //read a key from the windows registry
    public static String readRegistry(String location, String key) {
        try {
            // Run reg query, then read output with StreamReader (internal class)
            Process process = Runtime.getRuntime().exec("reg query "
                    + '"' + location + "\" /v " + key);

            InputStream is = process.getInputStream();
            ArrayList bs = new ArrayList();

            try {
                int c;
                while ((c = is.read()) != -1) {
                    bs.add(c);
                }
            } catch (IOException e) {
                logger.error(e);
            }

            byte[] bss = new byte[bs.size()];
            for (int j = 0; j < bs.size(); j++) {
                bss[j] = ((Integer) bs.get(j)).byteValue();

            }

            String output = new String(bss, "cp1251");

            // Output has the following format:
            // \n<Version information>\n\n<key>    <registry type>    <value>\r\n\r\n
            int i = output.indexOf("REG_SZ");
            if (i == -1) {
                return null;
            }

            StringBuilder sw = new StringBuilder();
            i += 6; // skip REG_SZ

            // skip spaces or tabs
            for (;;) {
                if (i > output.length()) {
                    break;
                }
                char c = output.charAt(i);
                if (c != ' ' && c != '\t') {
                    break;
                }
                ++i;
            }

            // take everything until end of line
            for (;;) {
                if (i > output.length()) {
                    break;
                }
                char c = output.charAt(i);
                if (c == '\r' || c == '\n') {
                    break;
                }
                sw.append(c);
                ++i;
            }

            return sw.toString();

        } catch (Exception e) {
            logger.error(e);
            return null;
        }
    }

    //swap rows in JTable
    public static void swapRows(JTable jTable, int r1, int r2) {
        TableModel model = jTable.getModel();
        for (int i = 1; i < model.getColumnCount(); i++) {
            Object temp = model.getValueAt(r1, i);
            model.setValueAt(model.getValueAt(r2, i), r1, i);
            model.setValueAt(temp, r2, i);
        }
        jTable.setModel(model);
    }

    //go to the given row in a JTable
    public static void move2row(JTable jTable, int row) {

        Rectangle rect = jTable.getCellRect(row, 1, true);
        jTable.scrollRectToVisible(rect);
        jTable.getSelectionModel().setSelectionInterval(row, row);
        jTable.getColumnModel().getSelectionModel().setSelectionInterval(0, jTable.getModel().getColumnCount() - 1);
    }

    public static String money2str(Integer amount) {

        fwMoney mo = new fwMoney(amount);
        return mo.num2str(true);
    }

    //print the cashbook for the given date
    public static void printCashBook(Date date, String page) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            //set the page number for all docs on the given date
            Session session = HUtil.getSession();
            session.beginTransaction();
            String hql =
                    "from Register r"
                    + " where DATE(r.datetime) = '" + sdf.format(date) + "'";
            List res = session.createQuery(hql).list();
            for (int j = 0; j < res.size(); j++) {
                Register r = (Register) res.get(j);
                r.setPage(page);
                session.update(r);
            }
            session.getTransaction().commit();

            //open the template
            File file = new File(Util.getAppPath() + "\\templates\\CashBook.ods");
            final Sheet sheet = SpreadSheet.createFromFile(file).getSheet(0);

            //fill common values
            sheet.getCellAt("B2").setValue("Касса от " + Util.date2String(date));
            sheet.getCellAt("I2").setValue("Касса от " + Util.date2String(date));
            sheet.getCellAt("G2").setValue("Лист " + page);
            sheet.getCellAt("N2").setValue("Лист " + page);

            //compute the initial incoming amount
            int inBalance = 0;
            hql =
                    "select sum(r.amount) from Register r"
                    + " where r.documentType = 0"
                    + " and DATE(r.datetime) < '" + sdf.format(date) + "'"
                    + " and r.active = 1";
            res = session.createQuery(hql).list();
            if (res.size() > 0) {
                if (res.get(0) != null) {
                    inBalance = new Integer(res.get(0).toString());
                }
            }
            //subtract the initial outcoming amount
            hql =
                    "select sum(r.amount) from Register r"
                    + " where r.documentType = 1"
                    + " and DATE(r.datetime) < '" + sdf.format(date) + "'"
                    + " and r.active = 1";
            res = session.createQuery(hql).list();
            if (res.size() > 0) {
                if (res.get(0) != null) {
                    inBalance -= new Integer(res.get(0).toString());
                }
            }
            //we have the initial balance
            sheet.getCellAt("F7").setValue("" + inBalance + "=");
            sheet.getCellAt("M7").setValue("" + inBalance + "=");

            //get all docs on the given date
            int amountIn = 0;
            int amountOut = 0;
            hql =
                    "from Register r"
                    + " where DATE(r.datetime) = '" + sdf.format(date) + "'"
                    + " and r.active = 1"
                    + " order by r.datetime, r.documentType";
            res = session.createQuery(hql).list();
            int ts = res.size();
            //multiply rows in the template for docs to fill
            if (ts > 1) {
                sheet.duplicateRows(7, 1, ts - 1);
            }
            int inDocs = 0;
            int outDocs = 0;
            int j;
            //insert docs in the rows
            for (j = 0; j < res.size(); j++) {
                Register r = (Register) res.get(j);
                String employeeName = "";
                if (r.getEmployeeCode() != null) {
                    Employee e = (Employee) HUtil.getElement("Employee", r.getEmployeeCode(), session);
                    if (e != null) {
                        employeeName = e.getName();
                    }
                }
                sheet.getCellAt("A" + (8 + j)).setValue(r.getNumber());
                sheet.getCellAt("H" + (8 + j)).setValue(r.getNumber());
                //two cases: incoming and outcoming docs
                if (r.getDocumentType() == 0) {
                    sheet.getCellAt("B" + (8 + j)).setValue("Принято от " + employeeName
                            + "\nТорговая выручка");
                    sheet.getCellAt("I" + (8 + j)).setValue("Принято от " + employeeName
                            + "\nТорговая выручка");
                    sheet.getCellAt("F" + (8 + j)).setValue("" + r.getAmount() + "=");
                    sheet.getCellAt("M" + (8 + j)).setValue("" + r.getAmount() + "=");
                    amountIn += r.getAmount();
                    inDocs++;
                } else if (r.getDocumentType() == 1) {
                    if (r.getPaymentType() != null) {
                        sheet.getCellAt("B" + (8 + j)).setValue(
                                "Выдано " + employeeName + "\n" + r.getPaymentType());
                        sheet.getCellAt("I" + (8 + j)).setValue(
                                "Выдано " + employeeName + "\n" + r.getPaymentType());
                    } else {
                        sheet.getCellAt("B" + (8 + j)).setValue("Выдано " + employeeName);
                        sheet.getCellAt("I" + (8 + j)).setValue("Выдано " + employeeName);
                    }
                    sheet.getCellAt("G" + (8 + j)).setValue("" + r.getAmount() + "=");
                    sheet.getCellAt("N" + (8 + j)).setValue("" + r.getAmount() + "=");
                    amountOut += r.getAmount();
                }
                outDocs++;
            }

            if (j == 0) {
                j = 1;
            }

            //fill total amounts
            if (amountIn > 0) {
                sheet.getCellAt("F" + (8 + j)).setValue(amountIn + "=");
                sheet.getCellAt("F" + (9 + j)).setValue(amountIn + "=");
                sheet.getCellAt("M" + (8 + j)).setValue(amountIn + "=");
                sheet.getCellAt("M" + (9 + j)).setValue(amountIn + "=");
            }

            if (amountOut > 0) {
                sheet.getCellAt("G" + (8 + j)).setValue(amountOut + "=");
                sheet.getCellAt("G" + (9 + j)).setValue(amountOut + "=");
                sheet.getCellAt("N" + (8 + j)).setValue(amountOut + "=");
                sheet.getCellAt("N" + (9 + j)).setValue(amountOut + "=");
            }

            sheet.getCellAt("F" + (10 + j)).setValue((inBalance + amountIn - amountOut) + "=");
            sheet.getCellAt("M" + (10 + j)).setValue((inBalance + amountIn - amountOut) + "=");

            //fill common info
            sheet.getCellAt("D" + (13 + j)).setValue(HUtil.getShortNameByCode(HUtil.getIntConstant("cashier")));
            sheet.getCellAt("K" + (13 + j)).setValue(HUtil.getShortNameByCode(HUtil.getIntConstant("cashier")));
            sheet.getCellAt("D" + (19 + j)).setValue(HUtil.getShortNameByCode(HUtil.getIntConstant("chiefAccountant")));
            sheet.getCellAt("K" + (19 + j)).setValue(HUtil.getShortNameByCode(HUtil.getIntConstant("chiefAccountant")));

            //we need numbers in words
            fwNumber fwInDocs = new fwNumber(inDocs);
            fwNumber fwOutDocs = new fwNumber(outDocs);
            String s = "";
            if (inDocs > 0) {
                s += fwInDocs.num2str();
            } else {
                s += "-";
            }
            s += " приходных и ";
            if (outDocs > 0) {
                s += fwOutDocs.num2str();
            } else {
                s += "-";
            }
            s += " расходных получил.";
            sheet.getCellAt("A" + (17 + j)).setValue(s);
            sheet.getCellAt("H" + (17 + j)).setValue(s);

            session.close();

            //make sure doc name is going to be unique and save it
            String r = " " + Math.round(Math.random() * 100000);
            File outputFile = new File("temp\\Касса за " + sdf.format(date) + r + ".ods");
            sheet.getSpreadSheet().saveAs(outputFile);
            Util.openDoc("temp\\Касса за " + sdf.format(date) + r + ".ods");
        } catch (Exception e) {
            logger.error(e);
        }
    }
    private static String appPath;

    public static void initAppPath() {

        try {
            appPath = (new AppStartUpPath()).getAppStartUp();
        } catch (Exception e) {
            logger.error(e);
        }
    }

    public static String getAppPath() {
        return appPath != null ? appPath : "";
    }

    public static void setCell(String cellAddress, Object value, int line, Sheet sheet) {
        if (value != null) {
            sheet.getCellAt(cellAddress + line).setValue(value);
        }
    }

    public static boolean startOffice(String folderName) {

        boolean started = false;

        try {
            RegistryKey hlmKey = Registry.HKEY_LOCAL_MACHINE;
            String keyPath = "Software\\";
            if(System.getProperty("sun.arch.data.model").equals("64")) {
                keyPath += "Wow6432Node\\";
            }
            keyPath += folderName + "\\" + folderName;
            RegistryKey ooFolder = hlmKey.openSubKey(keyPath);
            Enumeration keys = ooFolder.keyElements();
            DefaultArtifactVersion ver = new DefaultArtifactVersion("0");
            String folder = "";
            while (keys.hasMoreElements()) {
                String nextFolder = (String) keys.nextElement();
                DefaultArtifactVersion nextVer = new DefaultArtifactVersion(nextFolder);
                if (nextVer.compareTo(ver) > 0) {
                    ver = nextVer;
                    folder = nextFolder;
                }
            }
            if (!folder.isEmpty()) {
                RegistryKey ooKey = ooFolder.openSubKey(folder);
                String ooPath = ooKey.getStringValue("Path");
                if (ooPath != null) {
                    Runtime.getRuntime().exec(ooPath + " -headless -accept=\"socket,host=127.0.0.1,port=8100;urp;\" -nofirststartwizard");
                    started = true;
                }
            }

        } catch (Exception e) {
        }
   
        return started;

    }
    
    private static String ooPath;
    
    public static String getOOPath() {
        return ooPath;
    }

    public static boolean initOfficeVars(String folderName) {

        boolean init = false;

        try {
            RegistryKey hlmKey = Registry.HKEY_LOCAL_MACHINE;
            String keyPath = "Software\\";
            if(System.getProperty("sun.arch.data.model").equals("64")) {
                keyPath += "Wow6432Node\\";
            }
            keyPath += folderName + "\\" + folderName;
            RegistryKey ooFolder = hlmKey.openSubKey(keyPath);
            Enumeration keys = ooFolder.keyElements();
            DefaultArtifactVersion ver = new DefaultArtifactVersion("0");
            String folder = "";
            while (keys.hasMoreElements()) {
                String nextFolder = (String) keys.nextElement();
                DefaultArtifactVersion nextVer = new DefaultArtifactVersion(nextFolder);
                if (nextVer.compareTo(ver) > 0) {
                    ver = nextVer;
                    folder = nextFolder;
                }
            }
            if (!folder.isEmpty()) {
                RegistryKey ooKey = ooFolder.openSubKey(folder);
                String ooPath = ooKey.getStringValue("Path");
                init = ooPath != null;
            }

        } catch (Exception e) {
        }
   
        return init;

    }

    private static final SimpleDateFormat sdfDB = new SimpleDateFormat("yyyy-MM-dd");

    public static String formatDateDB(Date date) {

        String str = "null";
        if (date != null) {
            str = "'" + sdfDB.format(date) + "'";
        }
        return str;
    }

    public static String formatDate(Date date) {

        String str = "";
        if (date != null) {
            str = sdf.format(date);
        }
        return str;
    }
    
    public static String substr(String s, int p) {
        String ss = "";
        if (p < s.length()) {
            ss = s.substring(0, p);
        }
        return ss;
    }
    
    public static void go2rect(JTable jTable, int row) {
        jTable.scrollRectToVisible(jTable.getCellRect(row, 0, true));
        jTable.getSelectionModel().setSelectionInterval(row, row);
        TableColumnModel tcm = jTable.getColumnModel();
        tcm.getSelectionModel().setSelectionInterval(0, tcm.getColumnCount() - 1);
    }
    
}
