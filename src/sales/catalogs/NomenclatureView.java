package sales.catalogs;

import java.awt.Component;
import sales.incoming.IncomingDocView;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.beans.PropertyVetoException;
import java.io.File;
import java.math.BigInteger;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.Vector;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.text.JTextComponent;
import org.apache.log4j.Logger;
import org.hibernate.classic.Session;
import org.jdesktop.application.Action;
import org.jopendocument.dom.spreadsheet.Sheet;
import org.jopendocument.dom.spreadsheet.SpreadSheet;
import sales.auxiliarly.MyCellEditor;
import sales.SalesApp;
import sales.SalesView;
import sales.entity.Incoming;
import sales.entity.Nomenclature;
import sales.entity.Repricing;
import sales.interfaces.IClose;
import sales.interfaces.IJournal;
import sales.interfaces.IProductItem;
import sales.outcoming.BalanceView;
import sales.util.HUtil;
import sales.util.Util;

public class NomenclatureView extends javax.swing.JInternalFrame implements IClose, IJournal {

    static Logger logger = Logger.getLogger(SalesApp.class.getName());
    public static final int GEN_SCANCODE_LENGTH = 10;
    private static final Random random = new Random(new Date().getTime());

    public NomenclatureView(
            SalesView salesView, IProductItem parent, Integer productCode, Integer row, String filter, String name,
            Integer inPrice, boolean chooseCodes) {

        initComponents();

        Util.initJIF(this, "Номенклатура", parent, salesView);
        Util.initJTable(jtNomenclature);

        this.salesView = salesView;
        this.parent = parent;
        this.row = row;
        this.name = name;
        if (inPrice != null) {
            this.inPrice = inPrice;
        } else {
            this.inPrice = 0;
        }
        this.chooseCodes = chooseCodes;

        if (filter != null && !filter.isEmpty()) {
            jtfFilter.setText(filter);
            this.filter = filter = Util.getFilter(jtfFilter.getText(), new String[]{"name"});
        }
        filterToRestore = "";
        if (!name.isEmpty()) {
            setTitle("Выберите элемент соответствующий " + name);
        }

        if (parent == null) {
            jbOK.setVisible(false);
        }

        jNumBox = new JTextField();
        jNumBox.addKeyListener(new KeyAdapter() {

            @Override
            public void keyTyped(KeyEvent e) {
                jNumBox.setEditable(
                        Character.isDigit(e.getKeyChar())
                        || e.getKeyChar() == KeyEvent.VK_BACK_SPACE
                        || e.getKeyChar() == '.'
                        && (((JTable) e.getComponent().getParent()).getSelectedColumn() == 7 && jcbInPrice.isSelected()
                        || ((JTable) e.getComponent().getParent()).getSelectedColumn() == 6 && !jcbInPrice.isSelected())
                        && !((JTextField) e.getComponent()).getText().contains("."));
            }
        });

        priceTable = new ArrayList();

        showTable();

        scanEnter = false;

        if (table.size() > 0) {
            if (chooseCodes) {
                jtNomenclature.getSelectionModel().addSelectionInterval(0, 0);
                jtNomenclature.getColumnModel().getSelectionModel().setSelectionInterval(0, jtNomenclature.getColumnModel().getColumnCount() - 1);
            } else {
                if (productCode != null && productCode > 0) {
                    for (int i = 0; i < table.size(); i++) {
                        if (((Nomenclature) table.get(i)).getCode().equals(productCode)) {
                            Rectangle rect = jtNomenclature.getCellRect(i, 1, true);
                            jtNomenclature.scrollRectToVisible(rect);
                            jtNomenclature.getSelectionModel().addSelectionInterval(i, i);
                            jtNomenclature.getColumnModel().getSelectionModel().setSelectionInterval(1, 1);
                            break;
                        }
                    }
                }
            }
        }

        trash = false;

        if (parent == null) {
            jbOK.setEnabled(false);
        }

        //set move to the right cell on enter
        Util.setMoveRight(jtNomenclature);

    }

    private class MyTableModel extends DefaultTableModel {

        public MyTableModel(Vector table, Vector header) {
            super(table, header);
        }

        @Override
        public boolean isCellEditable(int r, int c) {
            return c > 0 && c != 4;
        }
    }

    private class MyPriceTableModel extends DefaultTableModel {

        public MyPriceTableModel(Vector table, Vector header) {
            super(table, header);
        }

        @Override
        public boolean isCellEditable(int r, int c) {
            return false;
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jspNomenclature = new javax.swing.JScrollPane();
        jtNomenclature = new javax.swing.JTable() {

            public Component prepareEditor(TableCellEditor editor,int row,int col)
            {
                Component c = super.prepareEditor(editor, row, col);
                if (c instanceof JTextComponent) {
                    ((JTextComponent) c).selectAll();
                    SwingUtilities.invokeLater(new Util.SelectLater((JTextComponent) c));
                }
                return c;
            }
        };
        jLabel1 = new javax.swing.JLabel();
        jtfFilter = new javax.swing.JTextField();
        jbAddItem = new javax.swing.JButton();
        jbDelete = new javax.swing.JButton();
        jbOK = new javax.swing.JButton();
        jbImport = new javax.swing.JButton();
        jlQuantity = new javax.swing.JLabel();
        jcbInPrice = new javax.swing.JCheckBox();
        jbClose = new javax.swing.JButton();
        jbTrash = new javax.swing.JButton();
        jbPricelist = new javax.swing.JButton();
        jbBalance = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jtPricelist = new javax.swing.JTable();
        jbAddPricelist = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jbDelPricelist = new javax.swing.JButton();
        jbClear = new javax.swing.JButton();
        jbFilterBySelected = new javax.swing.JButton();
        jbRestoreFilter = new javax.swing.JButton();
        jbRandomScanCode = new javax.swing.JButton();
        jbPriceListStandard = new javax.swing.JButton();

        setClosable(true);
        setMaximizable(true);
        setResizable(true);
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance().getContext().getResourceMap(NomenclatureView.class);
        setTitle(resourceMap.getString("Form.title")); // NOI18N
        setName("Form"); // NOI18N

        jspNomenclature.setName("jspNomenclature"); // NOI18N

        jtNomenclature.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jtNomenclature.setToolTipText(resourceMap.getString("jtNomenclature.toolTipText")); // NOI18N
        jtNomenclature.setCellSelectionEnabled(true);
        jtNomenclature.setName("jtNomenclature"); // NOI18N
        jtNomenclature.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jtNomenclatureMouseClicked(evt);
            }
        });
        jtNomenclature.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jtNomenclatureKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jtNomenclatureKeyTyped(evt);
            }
        });
        jspNomenclature.setViewportView(jtNomenclature);

        jLabel1.setText(resourceMap.getString("jLabel1.text")); // NOI18N
        jLabel1.setName("jLabel1"); // NOI18N

        jtfFilter.setText(resourceMap.getString("jtfFilter.text")); // NOI18N
        jtfFilter.setName("jtfFilter"); // NOI18N
        jtfFilter.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jtfFilterKeyReleased(evt);
            }
        });

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance().getContext().getActionMap(NomenclatureView.class, this);
        jbAddItem.setAction(actionMap.get("addItem")); // NOI18N
        jbAddItem.setText(resourceMap.getString("jbAddItem.text")); // NOI18N
        jbAddItem.setName("jbAddItem"); // NOI18N

        jbDelete.setAction(actionMap.get("deleteItem")); // NOI18N
        jbDelete.setText(resourceMap.getString("jbDelete.text")); // NOI18N
        jbDelete.setName("jbDelete"); // NOI18N

        jbOK.setAction(actionMap.get("OK")); // NOI18N
        jbOK.setText(resourceMap.getString("jbOK.text")); // NOI18N
        jbOK.setName("jbOK"); // NOI18N

        jbImport.setAction(actionMap.get("importNomenclature")); // NOI18N
        jbImport.setText(resourceMap.getString("jbImport.text")); // NOI18N
        jbImport.setName("jbImport"); // NOI18N

        jlQuantity.setText(resourceMap.getString("jlQuantity.text")); // NOI18N
        jlQuantity.setName("jlQuantity"); // NOI18N

        jcbInPrice.setAction(actionMap.get("showInPrice")); // NOI18N
        jcbInPrice.setText(resourceMap.getString("jcbInPrice.text")); // NOI18N
        jcbInPrice.setName("jcbInPrice"); // NOI18N

        jbClose.setAction(actionMap.get("closeCat")); // NOI18N
        jbClose.setText(resourceMap.getString("jbClose.text")); // NOI18N
        jbClose.setName("jbClose"); // NOI18N

        jbTrash.setAction(actionMap.get("showTrash")); // NOI18N
        jbTrash.setText(resourceMap.getString("jbTrash.text")); // NOI18N
        jbTrash.setName("jbTrash"); // NOI18N

        jbPricelist.setAction(actionMap.get("printPriceListCredit")); // NOI18N
        jbPricelist.setText(resourceMap.getString("jbPricelist.text")); // NOI18N
        jbPricelist.setName("jbPricelist"); // NOI18N

        jbBalance.setAction(actionMap.get("showBalance")); // NOI18N
        jbBalance.setText(resourceMap.getString("jbBalance.text")); // NOI18N
        jbBalance.setName("jbBalance"); // NOI18N

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        jtPricelist.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jtPricelist.setName("jtPricelist"); // NOI18N
        jScrollPane1.setViewportView(jtPricelist);

        jbAddPricelist.setAction(actionMap.get("addToPricelist")); // NOI18N
        jbAddPricelist.setText(resourceMap.getString("jbAddPricelist.text")); // NOI18N
        jbAddPricelist.setName("jbAddPricelist"); // NOI18N

        jLabel2.setText(resourceMap.getString("jLabel2.text")); // NOI18N
        jLabel2.setName("jLabel2"); // NOI18N

        jbDelPricelist.setAction(actionMap.get("delFromPricelist")); // NOI18N
        jbDelPricelist.setText(resourceMap.getString("jbDelPricelist.text")); // NOI18N
        jbDelPricelist.setName("jbDelPricelist"); // NOI18N

        jbClear.setAction(actionMap.get("clearPricelist")); // NOI18N
        jbClear.setText(resourceMap.getString("jbClear.text")); // NOI18N
        jbClear.setName("jbClear"); // NOI18N

        jbFilterBySelected.setAction(actionMap.get("filterBySelected")); // NOI18N
        jbFilterBySelected.setText(resourceMap.getString("jbFilterBySelected.text")); // NOI18N
        jbFilterBySelected.setName("jbFilterBySelected"); // NOI18N

        jbRestoreFilter.setAction(actionMap.get("restoreFilter")); // NOI18N
        jbRestoreFilter.setText(resourceMap.getString("jbRestoreFilter.text")); // NOI18N
        jbRestoreFilter.setName("jbRestoreFilter"); // NOI18N

        jbRandomScanCode.setAction(actionMap.get("createRandomScanCode")); // NOI18N
        jbRandomScanCode.setText(resourceMap.getString("jbRandomScanCode.text")); // NOI18N
        jbRandomScanCode.setName("jbRandomScanCode"); // NOI18N

        jbPriceListStandard.setAction(actionMap.get("printPriceList")); // NOI18N
        jbPriceListStandard.setText(resourceMap.getString("jbPriceListStandard.text")); // NOI18N
        jbPriceListStandard.setName("jbPriceListStandard"); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 939, Short.MAX_VALUE)
                    .addComponent(jspNomenclature, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 939, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jtfFilter, javax.swing.GroupLayout.DEFAULT_SIZE, 581, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jbFilterBySelected)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jbRestoreFilter))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jbPriceListStandard)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jbPricelist)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jbDelPricelist)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jbClear)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 181, Short.MAX_VALUE)
                        .addComponent(jbOK)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jbClose))
                    .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(jlQuantity, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jbBalance)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jcbInPrice)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jbRandomScanCode, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(jbImport)
                        .addGap(18, 18, 18)
                        .addComponent(jbAddItem)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jbAddPricelist)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jbDelete)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jbTrash)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jtfFilter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1)
                    .addComponent(jbRestoreFilter)
                    .addComponent(jbFilterBySelected))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jspNomenclature, javax.swing.GroupLayout.PREFERRED_SIZE, 278, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jcbInPrice)
                    .addComponent(jbBalance, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jbAddItem)
                    .addComponent(jbDelete)
                    .addComponent(jbTrash)
                    .addComponent(jbImport)
                    .addComponent(jbAddPricelist)
                    .addComponent(jlQuantity)
                    .addComponent(jbRandomScanCode))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 185, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jbClose)
                    .addComponent(jbOK)
                    .addComponent(jbPriceListStandard)
                    .addComponent(jbPricelist)
                    .addComponent(jbDelPricelist)
                    .addComponent(jbClear))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jtfFilterKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtfFilterKeyReleased
        filter = Util.getFilter(jtfFilter.getText(), new String[]{"name", "price", "scanCode"});
        showTable();
    }//GEN-LAST:event_jtfFilterKeyReleased

    private void jtNomenclatureMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtNomenclatureMouseClicked
        if (parent != null && evt.getClickCount() > 1) {
            OK();
        }
        int r = jtNomenclature.getSelectedRow();
        if (r != -1) {
            jlQuantity.setText("Остаток: " + HUtil.getBalance(((Nomenclature) table.get(r)).getCode()).toString());
        }
    }//GEN-LAST:event_jtNomenclatureMouseClicked

    private void jtNomenclatureKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtNomenclatureKeyReleased
        if (evt.getKeyCode() == 38 || evt.getKeyCode() == 40) {
            int r = jtNomenclature.getSelectedRow();
            if (r != -1) {
                jlQuantity.setText("Остаток: " + HUtil.getBalance(((Nomenclature) table.get(r)).getCode()).toString());
            }
        }
    }//GEN-LAST:event_jtNomenclatureKeyReleased

    private void jtNomenclatureKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtNomenclatureKeyTyped
        if (scanEnter) {
            scanCode += evt.getKeyChar();
        }
        if (evt.getKeyChar() == '!') {
            scanEnter = true;
            scanCode = "";
        }
        if (evt.getKeyChar() == '%') {
            scanEnter = false;
            scanCode = scanCode.replaceAll("!", "").replaceAll("%", "");
            int c = jtNomenclature.getSelectedColumn();
            if (jcbInPrice.isSelected() && c == 6 || !jcbInPrice.isSelected() && c == 5) {
                int r = jtNomenclature.getSelectedRow();
                int a = JOptionPane.showOptionDialog(
                        this,
                        "Заменить штрих-код у " + ((Nomenclature) table.get(r)).getName() + "?", "Штрих-код",
                        JOptionPane.YES_NO_CANCEL_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        new Object[]{
                            "Да", "Нет"}, "Нет");
                if (a == 0) {
                    Session session = HUtil.getSession();
                    session.beginTransaction();
                    Nomenclature n = (Nomenclature) HUtil.getElement("Nomenclature", ((Nomenclature) table.get(r)).getCode(), session);
                    n.setScanCode(scanCode);
                    session.save(n);
                    session.getTransaction().commit();
                    session.close();
                    showTable();
                }
            } else {
                for (int i = 0; i < table.size(); i++) {
                    String sc = ((Nomenclature) table.get(i)).getScanCode();
                    if (sc != null && sc.trim().equals(scanCode)) {
                        jtNomenclature.clearSelection();
                        Rectangle rect = jtNomenclature.getCellRect(i, 1, true);
                        jtNomenclature.scrollRectToVisible(rect);
                        jtNomenclature.getSelectionModel().addSelectionInterval(i, i);
                        jtNomenclature.getColumnModel().getSelectionModel().setSelectionInterval(1, 1);
                        break;
                    }
                }
            }
        }

    }//GEN-LAST:event_jtNomenclatureKeyTyped
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton jbAddItem;
    private javax.swing.JButton jbAddPricelist;
    private javax.swing.JButton jbBalance;
    private javax.swing.JButton jbClear;
    private javax.swing.JButton jbClose;
    private javax.swing.JButton jbDelPricelist;
    private javax.swing.JButton jbDelete;
    private javax.swing.JButton jbFilterBySelected;
    private javax.swing.JButton jbImport;
    private javax.swing.JButton jbOK;
    private javax.swing.JButton jbPriceListStandard;
    private javax.swing.JButton jbPricelist;
    private javax.swing.JButton jbRandomScanCode;
    private javax.swing.JButton jbRestoreFilter;
    private javax.swing.JButton jbTrash;
    private javax.swing.JCheckBox jcbInPrice;
    private javax.swing.JLabel jlQuantity;
    private javax.swing.JScrollPane jspNomenclature;
    private javax.swing.JTable jtNomenclature;
    private javax.swing.JTable jtPricelist;
    private javax.swing.JTextField jtfFilter;
    // End of variables declaration//GEN-END:variables
    private SalesView salesView;
    private IProductItem parent;
    private Integer inPrice;
    private boolean chooseCodes;
    private int row;
    private List table;
    private List priceTable;
    private String filter;
    private boolean scanEnter;
    private String scanCode;
    private JTextField jNumBox;
    private boolean trash;
    private String name;
    private String filterToRestore;

    public void showTable() {

        Vector<String> tableHeaders = new Vector<String>();
        Vector tableData = new Vector();
        tableHeaders.add("Код");
        tableHeaders.add("Наименование");
        tableHeaders.add("Ед.изм.");
        tableHeaders.add("Гарантия");
        tableHeaders.add("Кол-во");
        tableHeaders.add("Цена");
        if (jcbInPrice.isSelected()) {
            tableHeaders.add("Вх.цена");
        }
        tableHeaders.add("Масса");
        tableHeaders.add("Штрих код");
        if (filter == null || filter.isEmpty()) {
            filter = "";
        }

        try {
            String delStr = "x.active = 0";
            if (trash) {
                delStr = "x.active = 2";
            }
            table = HUtil.executeHql("from Nomenclature x where " + delStr + filter + " order by x.code");
            if (table != null) {
                for (int j = 0; j < table.size(); j++) {
                    Nomenclature n = (Nomenclature) table.get(j);
                    Vector<Object> oneRow = new Vector<Object>();
                    oneRow.add(n.getCode());
                    oneRow.add(n.getName());
                    oneRow.add(n.getUnit());
                    oneRow.add(n.getWarranty());
                    if (n.getQuantity() != null && n.getQuantity() > 0) {
                        oneRow.add(n.getQuantity());
                    } else {
                        oneRow.add("");
                    }
                    if (n.getPrice() != null && n.getPrice() > 0) {
                        oneRow.add(n.getPrice());
                    } else {
                        oneRow.add("");
                    }
                    if (jcbInPrice.isSelected()) {
                        if (n.getInPrice() != null && n.getInPrice() > 0) {
                            oneRow.add(n.getInPrice());
                        } else {
                            oneRow.add("");
                        }
                    }
                    oneRow.add(n.getWeight());
                    oneRow.add(n.getScanCode());
                    tableData.add(oneRow);
                }
            }
        } catch (Exception e) {
            logger.error(e);
        }

        jtNomenclature.setModel(new MyTableModel(tableData, tableHeaders));

        Util.autoResizeColWidth(jtNomenclature);

        jtNomenclature.getColumnModel().getColumn(0).setMinWidth(50);
        jtNomenclature.getColumnModel().getColumn(0).setMaxWidth(50);
        jtNomenclature.getColumnModel().getColumn(1).setMinWidth(500);
        jtNomenclature.getColumnModel().getColumn(2).setMinWidth(50);
        jtNomenclature.getColumnModel().getColumn(2).setMaxWidth(50);
        jtNomenclature.getColumnModel().getColumn(3).setMinWidth(55);
        jtNomenclature.getColumnModel().getColumn(4).setMinWidth(50);
        jtNomenclature.getColumnModel().getColumn(4).setMaxWidth(50);
        jtNomenclature.getColumnModel().getColumn(5).setMaxWidth(50);
        jtNomenclature.getColumnModel().getColumn(5).setMinWidth(50);
        if (jcbInPrice.isSelected()) {
            jtNomenclature.getColumnModel().getColumn(6).setMinWidth(70);
            jtNomenclature.getColumnModel().getColumn(7).setMinWidth(50);
            jtNomenclature.getColumnModel().getColumn(8).setMinWidth(90);
        } else {
            jtNomenclature.getColumnModel().getColumn(6).setMinWidth(50);
            jtNomenclature.getColumnModel().getColumn(7).setMinWidth(90);
        }


        JComboBox jcb =
                new JComboBox(new String[]{"1 мес.", "3 мес.", "6 мес.", "12 мес.", "24 мес.", "36 мес.", "нет"});
        jcb.setEditable(true);
        jcb.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                jtNomenclature.requestFocus();
            }
        ;
        });
        
        jtNomenclature.getColumnModel().getColumn(3).setCellEditor(new DefaultCellEditor(jcb));
        jtNomenclature.getColumnModel().getColumn(5).setCellEditor(new MyCellEditor(jNumBox));
        if (jcbInPrice.isSelected()) {
            jtNomenclature.getColumnModel().getColumn(6).setCellEditor(new MyCellEditor(jNumBox));
            jtNomenclature.getColumnModel().getColumn(7).setCellEditor(new MyCellEditor(jNumBox));
        } else {
            jtNomenclature.getColumnModel().getColumn(6).setCellEditor(new MyCellEditor(jNumBox));
        }

        jtNomenclature.getModel().addTableModelListener(new TableModelListener() {

            @Override
            public void tableChanged(TableModelEvent e) {
                int r = e.getFirstRow();
                int c = e.getColumn();
                updateTable(r, c);
                showTable();
                Util.moveCell(r, c, jtNomenclature, true);
            }
        });

        showPriceTable();
    }

    public void showPriceTable() {
        Vector<String> tableHeaders = new Vector<String>();
        Vector tableData = new Vector();
        tableHeaders.add("Код");
        tableHeaders.add("Наименование");
        tableHeaders.add("Ед.изм.");
        tableHeaders.add("Гарантия");
        tableHeaders.add("Цена");
        if (jcbInPrice.isSelected()) {
            tableHeaders.add("Вх.цена");
        }
        tableHeaders.add("Штрих код");

        for (int j = 0; j < priceTable.size(); j++) {
            Nomenclature n = (Nomenclature) priceTable.get(j);
            Vector<Object> oneRow = new Vector<Object>();
            oneRow.add(n.getCode());
            oneRow.add(n.getName());
            oneRow.add(n.getUnit());
            oneRow.add(n.getWarranty());
            if (n.getPrice() != null && n.getPrice() > 0) {
                oneRow.add(n.getPrice());
            } else {
                oneRow.add("");
            }
            if (jcbInPrice.isSelected()) {
                if (n.getInPrice() != null && n.getInPrice() > 0) {
                    oneRow.add(n.getInPrice());
                } else {
                    oneRow.add("");
                }
            }
            oneRow.add(n.getScanCode());
            tableData.add(oneRow);
        }
        jtPricelist.setModel(new MyPriceTableModel(tableData, tableHeaders));

        Util.autoResizeColWidth(jtPricelist);

        jtPricelist.getColumnModel().getColumn(0).setMinWidth(50);
        jtPricelist.getColumnModel().getColumn(0).setMaxWidth(50);
        jtPricelist.getColumnModel().getColumn(2).setMinWidth(50);
        jtPricelist.getColumnModel().getColumn(2).setMaxWidth(50);
        jtPricelist.getColumnModel().getColumn(3).setMinWidth(55);
        jtPricelist.getColumnModel().getColumn(4).setMaxWidth(50);
        jtPricelist.getColumnModel().getColumn(4).setMinWidth(50);
        if (jcbInPrice.isSelected()) {
            jtPricelist.getColumnModel().getColumn(5).setMinWidth(70);
            jtPricelist.getColumnModel().getColumn(6).setMinWidth(90);
        } else {
            jtPricelist.getColumnModel().getColumn(5).setMinWidth(90);
        }
    }

    public void updateTable(Integer r, Integer c) {
        Integer id = new Integer(jtNomenclature.getValueAt(r, 0).toString());
        try {
            Session session = HUtil.getSessionFactory().openSession();
            session.beginTransaction();
            Nomenclature n = (Nomenclature) session.get(Nomenclature.class, id);
            if (c == 1) {
                n.setName(jtNomenclature.getValueAt(r, 1).toString());
            } else if (c == 2) {
                n.setUnit(jtNomenclature.getValueAt(r, 2).toString());
            } else if (c == 3) {
                n.setWarranty(jtNomenclature.getValueAt(r, 3).toString());
            } else if (c == 5) {
                n.setPrice(new Integer(jtNomenclature.getValueAt(r, 5).toString()));
            } else if (c == 6) {
                if (jcbInPrice.isSelected()) {
                    n.setInPrice(new Integer(jtNomenclature.getValueAt(r, 6).toString()));
                } else {
                    n.setWeight(
                            new Double(Math.round(new Double(jtNomenclature.getValueAt(r, 6).toString()) * 1000)) / 1000);
                }
            } else if (c == 7) {
                if (jcbInPrice.isSelected()) {
                    n.setWeight(new Double(jtNomenclature.getValueAt(r, 7).toString()));
                } else {
                    n.setScanCode(jtNomenclature.getValueAt(r, 7).toString().replaceAll("!", "").replaceAll("%", ""));
                }
            } else if (c == 8) {
                n.setScanCode(jtNomenclature.getValueAt(r, 8).toString().replaceAll("!", "").replaceAll("%", ""));
            }
            table.set(r, n);
            session.update(n);
            session.getTransaction().commit();
            session.close();
            salesView.getWorkplace().showNomenclature();
        } catch (Exception e) {
            logger.error(e);
        }
    }

    @Action
    public void addItem() {
        try {
            Session session = HUtil.getSession();
            session.beginTransaction();
            Nomenclature n = new Nomenclature();
            if (name != null && !name.isEmpty()) {
                n.setName(name);
            } else {
                n.setName("");
            }
            n.setUnit("шт.");
            n.setWarranty("12 мес.");
            n.setInPrice(inPrice);
            n.setPrice((int) Math.round(
                    (double) (inPrice * (100 + HUtil.getConstantDouble("nds")) * (100 + HUtil.getConstantDouble("defaultMargin"))) / 10000));
            n.setScanCode("");
            session.save(n);
            session.getTransaction().commit();
            session.close();
            filter = "";
            showTable();
            salesView.getWorkplace().showNomenclature();
            Rectangle rect = jtNomenclature.getCellRect(jtNomenclature.getRowCount() - 1, 1, true);
            jtNomenclature.scrollRectToVisible(rect);
            jtNomenclature.getSelectionModel().addSelectionInterval(jtNomenclature.getRowCount() - 1, jtNomenclature.getRowCount() - 1);
            jtNomenclature.getColumnModel().getSelectionModel().setSelectionInterval(1, 1);
            jtNomenclature.editCellAt(jtNomenclature.getRowCount() - 1, 1);
            jtNomenclature.requestFocus();
        } catch (Exception e) {
            logger.error(e);
        }
    }

    @Action
    public void deleteItem() {
        if (!trash) {
            int r = jtNomenclature.getSelectedRow();
            if (r != -1) {
                int a = JOptionPane.showOptionDialog(
                        this, "Удалить элемент " + ((Nomenclature) table.get(r)).getName() + "?", "Удаление", JOptionPane.YES_NO_CANCEL_OPTION,
                        JOptionPane.QUESTION_MESSAGE, null, new Object[]{
                            "Да", "Нет"}, "Нет");
                if (a == 0) {
                    changeDelStatus(2, r);
                }
            }
        } else {
            int r = jtNomenclature.getSelectedRow();
            if (r != -1) {
                int a = JOptionPane.showOptionDialog(
                        this, "Восстановить элемент " + ((Nomenclature) table.get(r)).getName() + " из корзины?", "Восстановление", JOptionPane.YES_NO_CANCEL_OPTION,
                        JOptionPane.QUESTION_MESSAGE, null, new Object[]{
                            "Да", "Нет"}, "Нет");
                if (a == 0) {
                    changeDelStatus(0, r);
                }
            }
        }

    }

    @Action
    public void OK() {
        TableCellEditor tce = jtNomenclature.getCellEditor();
        if (tce != null) {
            tce.stopCellEditing();
        }
        if (parent != null && jtNomenclature.getSelectedRow() != -1) {
            if (row < 0) {
                parent.addProductItem(
                        new Integer(jtNomenclature.getModel().getValueAt(jtNomenclature.getSelectedRow(), 0).toString()));
            } else {
                if (chooseCodes) {
                    parent.setProductItemPart(
                            new Integer(jtNomenclature.getModel().getValueAt(jtNomenclature.getSelectedRow(), 0).toString()), row);
                } else {
                    parent.setProductItem(
                            new Integer(jtNomenclature.getModel().getValueAt(jtNomenclature.getSelectedRow(), 0).toString()), row);
                }
            }
        }
        if (!scanEnter) {
            Util.closeJIF(this, parent, salesView);
            Util.closeJIFTab(this, salesView);
        }
    }

    @Action
    public void importNomenclature() {
        JFileChooser fc = new JFileChooser();
        int returnVal = fc.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            try {
                final Sheet sheet = SpreadSheet.createFromFile(file).getSheet(0);
                Session session = HUtil.getSession();
                int a = JOptionPane.showOptionDialog(
                        this, "Очистить номенклатуру?", "Импорт", JOptionPane.YES_NO_CANCEL_OPTION,
                        JOptionPane.QUESTION_MESSAGE, null, new Object[]{"Да", "Нет", "Отмена"}, "Нет");
                if (a == 2) {
                    return;
                }
                if (a == 0) {
                    session.createSQLQuery("truncate table nomenclature").executeUpdate();
                }

                a = JOptionPane.showOptionDialog(
                        this, "Создать документ прихода?", "Импорт", JOptionPane.YES_NO_CANCEL_OPTION,
                        JOptionPane.QUESTION_MESSAGE, null, new Object[]{"Да", "Нет", "Отмена"}, "Да");
                if (a == 2) {
                    return;
                }
                if (a == 1) {
                    session.beginTransaction();
                    for (int j = 0; j < sheet.getRowCount(); j++) {
                        String name = sheet.getCellAt("B" + (j + 1)).getTextValue();
                        if (!name.equals("")) {
                            Nomenclature n = new Nomenclature();
                            n.setName(name);
                            n.setWarranty("12 мес.");
                            n.setUnit("шт.");
                            n.setPrice(Util.str2int(sheet.getCellAt("D" + (j + 1)).getTextValue()));
                            n.setInPrice(Util.str2int(sheet.getCellAt("F" + (j + 1)).getTextValue()));
                            session.save(n);
                        }
                    }
                    session.getTransaction().commit();
                } else {
                    if (a == 0) {
                        IncomingDocView idv = new IncomingDocView(salesView, null, null, null);
                        salesView.getJDesktopPane().add(idv, javax.swing.JLayeredPane.DEFAULT_LAYER);
                        try {
                            idv.setSelected(true);
                        } catch (PropertyVetoException pve) {
                            System.err.println(pve);
                        }
                        for (int j = 1; j < sheet.getRowCount(); j++) {
                            session.beginTransaction();
                            String name = sheet.getCellAt("B" + (j + 1)).getTextValue();
                            if (!name.equals("")) {
                                Nomenclature n = new Nomenclature();
                                n.setName(name);
                                n.setWarranty("12 мес.");
                                n.setUnit("шт.");
                                n.setPrice(Util.str2int(sheet.getCellAt("D" + (j + 1)).getTextValue()));
                                n.setInPrice(Util.str2int(sheet.getCellAt("F" + (j + 1)).getTextValue()));
                                session.save(n);
                                int q = Util.str2int(sheet.getCellAt("C" + (j + 1)).getTextValue());
                                if (q > 0) {
                                    idv.addProductItem(n.getCode(), q);
                                }
                            }
                            session.getTransaction().commit();
                        }
                        idv.showTable();
                    }
                }
                session.close();
            } catch (Exception e) {
                logger.error(e);
            }
        }
        showTable();
    }

    @Action
    public void showInPrice() {
        showTable();
    }

    public void close() {
    }

    @Action
    public void closeCat() {

        if (chooseCodes) {

            int a = JOptionPane.showOptionDialog(
                    this,
                    "Отменить выбор соответствия элементов", "Выбор соответствия элементов",
                    JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE,
                    null, new Object[]{"Да", "Нет"}, "Нет");
            if (a == 1) {
                return;
            }
        }
        Util.closeJIF(this, parent, salesView);
        Util.closeJIFTab(this, salesView);
    }

    private void changeDelStatus(int status, int r) {
        try {
            Session session = HUtil.getSession();
            session.beginTransaction();
            Nomenclature el = (Nomenclature) HUtil.getElement("Nomenclature", ((Nomenclature) table.get(r)).getCode(), session);
            if (el != null) {
                el.setActive(status);
                session.save(el);
            }
            session.getTransaction().commit();
            session.close();
        } catch (Exception e) {
            logger.error(e);
        }
        showTable();
        salesView.getWorkplace().showNomenclature();
    }

    @Action
    public void showTrash() {
        if (!trash) {
            jbDelete.setText("Восстановить");
            jbTrash.setText("Журнал");
            trash = true;

        } else {
            jbDelete.setText("Удалить");
            jbTrash.setText("Корзина");
            trash = false;
        }
        showTable();
    }

    @Action
    public void printPriceListCredit() {

        try {

            int NUM_PRICELIST = Util.getFinalPriceList();

            File file = new File(Util.getAppPath() + "\\templates\\PriceList.ods");
            final Sheet sheet = SpreadSheet.createFromFile(file).getSheet(0);
            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
            Session session = HUtil.getSession();
            int ts = priceTable.size();
            if (ts > 0) {
                sheet.duplicateRows(0, 6, (int) Math.ceil((ts - 1) / NUM_PRICELIST));
            }
            String companyName = HUtil.getConstant("name");
            int j = 0;
            for (int i = 0; i < ts; i++) {
                Nomenclature n = (Nomenclature) priceTable.get(i);
                String ean = "";
                if (n != null) {
                    String sc = n.getScanCode();
                    if (sc != null && sc.length() > 0) {
                        ean = Util.ean13(sc.substring(0, sc.length() - 1));
                    }
                }
                String ref = "";
                Object[] doc = HUtil.getDocChangePrice(n.getCode());
                if (doc != null) {
                    if (doc[0] != null) {
                        if (((BigInteger) doc[1]).intValue() == 0) {
                            Incoming in = (Incoming) HUtil.getElement("Incoming", (Integer) doc[0]);
                            ref = "ТН(ТТН) № " + in.getNumber() + " от " + sdf.format(in.getDatetime());
                        } else if (((BigInteger) doc[1]).intValue() == 1) {
                            Repricing r = (Repricing) HUtil.getElement("Repricing", (Integer) doc[0]);
                            ref = "Акт переоценки № " + r.getNumber() + " от " + sdf.format(r.getDatetime());
                        }
                    }
                }
                int c = i % NUM_PRICELIST * 4;
                sheet.getCellAt("" + (char) (65 + c) + (j * 6 + 1)).setValue(companyName);
                sheet.getCellAt("" + (char) (65 + c) + (j * 6 + 2)).setValue(ean);
                sheet.getCellAt("" + (char) (65 + c) + (j * 6 + 3)).setValue(n.getName());
                sheet.getCellAt("" + (char) (67 + c) + (j * 6 + 4)).setValue(
                        n.getPrice() * (HUtil.getConstantDouble("creditInterest") + 100) / 100);
                sheet.getCellAt("" + (char) (67 + c) + (j * 6 + 5)).setValue(n.getPrice());
                sheet.getCellAt("" + (char) (65 + c) + (j * 6 + 6)).setValue(ref);
                if (i % NUM_PRICELIST == NUM_PRICELIST - 1) {
                    j++;
                }
            }

            session.close();

            String r = " " + Math.round(Math.random() * 100000);
            File outputFile = new File("temp\\Прайс-лист по номенклатуре" + r + ".ods");
            sheet.getSpreadSheet().saveAs(outputFile);
            Util.openDoc("temp\\Прайс-лист по номенклатуре" + r + ".ods");
            sheet.detach();
            
        } catch (Exception e) {
            logger.error(e);
        }
    }

    @Action
    public void printPriceList() {

        try {

            int NUM_PRICELIST = Util.getFinalPriceList();

            File file = new File(Util.getAppPath() + "\\templates\\PriceListStandard.ods");
            final Sheet sheet = SpreadSheet.createFromFile(file).getSheet(0);
            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
            Session session = HUtil.getSession();
            int ts = priceTable.size();
            if (ts > 0) {
                sheet.duplicateRows(0, 5, (int) Math.ceil((ts - 1) / NUM_PRICELIST));
            }
            String companyName = HUtil.getConstant("name");
            int j = 0;
            for (int i = 0; i < ts; i++) {
                Nomenclature n = (Nomenclature) priceTable.get(i);
                String ean = "";
                if (n != null) {
                    String sc = n.getScanCode();
                    if (sc != null && sc.length() > 0) {
                        ean = Util.ean13(sc.substring(0, sc.length() - 1));
                    }
                }
                String ref = "";
                Object[] doc = HUtil.getDocChangePrice(n.getCode());
                if (doc != null) {
                    if (doc[0] != null) {
                        if (((BigInteger) doc[1]).intValue() == 0) {
                            Incoming in = (Incoming) HUtil.getElement("Incoming", (Integer) doc[0]);
                            ref = "ТН(ТТН) № " + in.getNumber() + " от " + sdf.format(in.getDatetime());
                        } else if (((BigInteger) doc[1]).intValue() == 1) {
                            Repricing r = (Repricing) HUtil.getElement("Repricing", (Integer) doc[0]);
                            ref = "Акт переоценки № " + r.getNumber() + " от " + sdf.format(r.getDatetime());
                        }
                    }
                }
                int c = i % NUM_PRICELIST * 4;
                sheet.getCellAt("" + (char) (65 + c) + (j * 5 + 1)).setValue(companyName);
                sheet.getCellAt("" + (char) (65 + c) + (j * 5 + 2)).setValue(ean);
                sheet.getCellAt("" + (char) (65 + c) + (j * 5 + 3)).setValue(n.getName());
                sheet.getCellAt("" + (char) (67 + c) + (j * 5 + 4)).setValue(n.getPrice());
                sheet.getCellAt("" + (char) (65 + c) + (j * 5 + 5)).setValue(ref);
                if (i % NUM_PRICELIST == NUM_PRICELIST - 1) {
                    j++;
                }
            }

            session.close();

            String r = " " + Math.round(Math.random() * 100000);
            File outputFile = new File("temp\\Прайс-лист по номенклатуре" + r + ".ods");
            sheet.getSpreadSheet().saveAs(outputFile);
            Util.openDoc("temp\\Прайс-лист по номенклатуре" + r + ".ods");
            sheet.detach();
            
        } catch (Exception e) {
            logger.error(e);
        }
    }

    @Action
    public void showBalance() {
        int r = jtNomenclature.getSelectedRow();
        if (r != -1) {
            JInternalFrame x = new BalanceView(((Nomenclature) table.get(r)).getCode(), this, salesView);
            salesView.getJDesktopPane().add(x, javax.swing.JLayeredPane.DEFAULT_LAYER);
            x.setVisible(true);
            try {
                x.setSelected(true);
            } catch (java.beans.PropertyVetoException e) {
                logger.error(e);
            }
        }
    }

    @Action
    public void addToPricelist() {
        int firstRow = jtNomenclature.getSelectionModel().getMinSelectionIndex();
        int lastRow = jtNomenclature.getSelectionModel().getMaxSelectionIndex();
        int firstColumn = jtNomenclature.getColumnModel().getSelectionModel().getMinSelectionIndex();
        int lastColumn = jtNomenclature.getColumnModel().getSelectionModel().getMaxSelectionIndex();
        for (int r = firstRow; r <= lastRow; r++) {
            for (int c = firstColumn; c <= lastColumn; c++) {
                if (jtNomenclature.isCellSelected(r, c)) {
                    if (!priceTable.contains(table.get(r))) {
                        priceTable.add(table.get(r));
                    }
                }
            }
        }
        showPriceTable();
    }

    @Action
    public void delFromPricelist() {
        int r = jtPricelist.getSelectedRow();
        if (r != -1) {
            priceTable.remove(r);
            showPriceTable();
        }
    }

    @Action
    public void clearPricelist() {
        int a = JOptionPane.showOptionDialog(
                this, "Очистить таблицу ценников?", "Очистка таблицы ценников", JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE, null, new Object[]{"Да", "Нет"}, "Нет");
        if (a == 0) {
            priceTable.clear();
            showPriceTable();
        }
    }

    @Action
    public void filterBySelected() {
        filterToRestore = jtfFilter.getText();
        jtfFilter.setText(jtfFilter.getSelectedText());
        filter = Util.getFilter(jtfFilter.getText(), new String[]{"name"});
        showTable();
    }

    @Action
    public void restoreFilter() {
        if (!filterToRestore.isEmpty()) {
            jtfFilter.setText(filterToRestore);
            filter = Util.getFilter(filterToRestore, new String[]{"name"});
            showTable();
        }
    }

    @Action
    public void createRandomScanCode() {

        int r = jtNomenclature.getSelectedRow();
        if (r != -1) {
            while (true) {
                String sCode = "";
                for (int i = 0; i < GEN_SCANCODE_LENGTH; i++) {
                    sCode += random.nextInt(10);
                }
                try {
                    Statement st = HUtil.getConnection().createStatement();
                    if (!st.executeQuery("select code from Nomenclature where scanCode = '" + sCode + "'").next()) {
                        if (jcbInPrice.isSelected()) {
                            jtNomenclature.setValueAt(sCode, r, 8);
                            updateTable(r, 8);
                        } else {
                            jtNomenclature.setValueAt(sCode, r, 7);
                            updateTable(r, 7);
                        }
                        break;
                    }
                } catch (Exception e) {
                    logger.error(e);
                }
            }
        }
    }
}