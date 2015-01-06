
/*
 * JournalView.java
 *
 * Created on 07.05.2011, 14:59:59
 */
package sales.paymentorder;

import com.toedter.calendar.JDateChooser;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Font;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import javax.swing.GroupLayout;
import javax.swing.JFileChooser;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import org.apache.log4j.Logger;
import org.hibernate.classic.Session;
import org.jdesktop.application.Action;
import org.jopendocument.dom.OOUtils;
import org.jopendocument.dom.spreadsheet.Sheet;
import org.jopendocument.dom.spreadsheet.SpreadSheet;
import sales.SalesApp;
import sales.SalesView;
import sales.entity.Contractors;
import sales.entity.Employee;
import sales.entity.Paymentorder;
import sales.entity.Paymentorder;
import sales.entity.Register;
import sales.entity.Suppliers;
import sales.interfaces.IClose;
import sales.interfaces.IJournal;
import sales.util.HUtil;
import sales.util.Util;
import sales.util.fwNumber;

public class PaymentOrderView extends javax.swing.JInternalFrame implements IClose, IJournal {

    static Logger logger = Logger.getLogger(SalesApp.class.getName());
    static final private SimpleDateFormat sdfi = new SimpleDateFormat("dd/MM/yyyy");

    public PaymentOrderView(SalesView salesView) {

        initComponents();

        Util.initJIF(this, "Журнал платежных поручений", null, salesView);

        this.salesView = salesView;

        Calendar c = Calendar.getInstance();

        date = new JDateChooser(c.getTime());
        date.getDateEditor().addPropertyChangeListener(
                new PropertyChangeListener() {

                    @Override
                    public void propertyChange(PropertyChangeEvent e) {
                        if ("date".equals(e.getPropertyName())) {
                            checkDate();
                        }
                    }
                });
        GroupLayout gl = (GroupLayout) jpDate.getLayout();
        gl.setHorizontalGroup(gl.createParallelGroup().addGroup(gl.createSequentialGroup().addComponent(date)));
        gl.setVerticalGroup(gl.createParallelGroup().addGroup(gl.createSequentialGroup().addComponent(date)));
        jcbDate.setSelected(true);

        gl = (GroupLayout) jpDate.getLayout();
        gl.setHorizontalGroup(gl.createParallelGroup().addGroup(gl.createSequentialGroup().addComponent(date)));
        gl.setVerticalGroup(gl.createParallelGroup().addGroup(gl.createSequentialGroup().addComponent(date)));
        jcbDate.setSelected(true);

        Calendar c1 = Calendar.getInstance();
        c1.add(Calendar.DATE, - 30);
        startDate = new JDateChooser(c1.getTime());
        startDate.getDateEditor().addPropertyChangeListener(
                new PropertyChangeListener() {

                    @Override
                    public void propertyChange(PropertyChangeEvent e) {
                        if ("date".equals(e.getPropertyName())) {
                            checkPeriod();
                        }
                    }
                });
        gl = (GroupLayout) jpStartDate.getLayout();
        gl.setHorizontalGroup(gl.createParallelGroup().addGroup(gl.createSequentialGroup().addComponent(startDate)));
        gl.setVerticalGroup(gl.createParallelGroup().addGroup(gl.createSequentialGroup().addComponent(startDate)));

        Calendar c2 = Calendar.getInstance();
        c2.add(Calendar.MONTH, 1);
        c2.set(Calendar.DATE, 1);
        endDate = new JDateChooser(c2.getTime());
        endDate.getDateEditor().addPropertyChangeListener(
                new PropertyChangeListener() {

                    @Override
                    public void propertyChange(PropertyChangeEvent e) {
                        if ("date".equals(e.getPropertyName())) {
                            checkPeriod();
                        }
                    }
                });
        gl = (GroupLayout) jpEndDate.getLayout();
        gl.setHorizontalGroup(gl.createParallelGroup().addGroup(gl.createSequentialGroup().addComponent(endDate)));
        gl.setVerticalGroup(gl.createParallelGroup().addGroup(gl.createSequentialGroup().addComponent(endDate)));

        initColumnsArray();
        checkPeriod();

        if (jtPaymentOrder.getRowCount() > 0) {
            jtPaymentOrder.getSelectionModel().setSelectionInterval(0, 0);
        }

        trash = false;

    }

    private void initColumnsArray() {
        columns = new ArrayList<>();
        columns.add(new String[]{"Номер", "string"});
        columns.add(new String[]{"Дата", "date"});
        columns.add(new String[]{"Контрагент", "string"});
        columns.add(new String[]{"Сумма", "int"});
    }

    public class MyTableCellRenderer extends DefaultTableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(JTable tbl,
                Object obj, boolean isSelected, boolean hasFocus, int row, int column) {
            Component cell = super.getTableCellRendererComponent(
                    tbl, obj, isSelected, hasFocus, row, column);
            if (((Paymentorder) table.get(row)).getActive() == 1) {
                cell.setFont(new Font(this.getFont().getName(), Font.ITALIC, this.getFont().getSize()));
            }
            return cell;
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

        jScrollPane1 = new javax.swing.JScrollPane();
        jtPaymentOrder = new javax.swing.JTable();
        jbCancel = new javax.swing.JButton();
        jbAddDoc = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jpStartDate = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jpEndDate = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jpDate = new javax.swing.JPanel();
        jcbDate = new javax.swing.JCheckBox();
        jcbPeriod = new javax.swing.JCheckBox();
        jbDelete = new javax.swing.JButton();
        jbTrash = new javax.swing.JButton();
        jbImport = new javax.swing.JButton();

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance().getContext().getResourceMap(PaymentOrderView.class);
        setBackground(resourceMap.getColor("Form.background")); // NOI18N
        setClosable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle(resourceMap.getString("Form.title")); // NOI18N
        setName("Form"); // NOI18N

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        jtPaymentOrder.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jtPaymentOrder.setFillsViewportHeight(true);
        jtPaymentOrder.setName("jtPaymentOrder"); // NOI18N
        jtPaymentOrder.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jtPaymentOrderMouseClicked(evt);
            }
        });
        jtPaymentOrder.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jtPaymentOrderKeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(jtPaymentOrder);

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance().getContext().getActionMap(PaymentOrderView.class, this);
        jbCancel.setAction(actionMap.get("closeJournal")); // NOI18N
        jbCancel.setText(resourceMap.getString("jbCancel.text")); // NOI18N
        jbCancel.setName("jbCancel"); // NOI18N

        jbAddDoc.setAction(actionMap.get("addDoc")); // NOI18N
        jbAddDoc.setText(resourceMap.getString("jbAddDoc.text")); // NOI18N
        jbAddDoc.setName("jbAddDoc"); // NOI18N

        jLabel1.setBackground(resourceMap.getColor("jLabel1.background")); // NOI18N
        jLabel1.setText(resourceMap.getString("jLabel1.text")); // NOI18N
        jLabel1.setName("jLabel1"); // NOI18N

        jpStartDate.setBackground(resourceMap.getColor("jpStartDate.background")); // NOI18N
        jpStartDate.setName("jpStartDate"); // NOI18N

        javax.swing.GroupLayout jpStartDateLayout = new javax.swing.GroupLayout(jpStartDate);
        jpStartDate.setLayout(jpStartDateLayout);
        jpStartDateLayout.setHorizontalGroup(
            jpStartDateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 93, Short.MAX_VALUE)
        );
        jpStartDateLayout.setVerticalGroup(
            jpStartDateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 21, Short.MAX_VALUE)
        );

        jLabel2.setBackground(resourceMap.getColor("jLabel2.background")); // NOI18N
        jLabel2.setText(resourceMap.getString("jLabel2.text")); // NOI18N
        jLabel2.setName("jLabel2"); // NOI18N

        jpEndDate.setBackground(resourceMap.getColor("jpEndDate.background")); // NOI18N
        jpEndDate.setName("jpEndDate"); // NOI18N

        javax.swing.GroupLayout jpEndDateLayout = new javax.swing.GroupLayout(jpEndDate);
        jpEndDate.setLayout(jpEndDateLayout);
        jpEndDateLayout.setHorizontalGroup(
            jpEndDateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 102, Short.MAX_VALUE)
        );
        jpEndDateLayout.setVerticalGroup(
            jpEndDateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 21, Short.MAX_VALUE)
        );

        jLabel3.setBackground(resourceMap.getColor("jLabel3.background")); // NOI18N
        jLabel3.setText(resourceMap.getString("jLabel3.text")); // NOI18N
        jLabel3.setName("jLabel3"); // NOI18N

        jpDate.setBackground(resourceMap.getColor("jpDate.background")); // NOI18N
        jpDate.setName("jpDate"); // NOI18N

        javax.swing.GroupLayout jpDateLayout = new javax.swing.GroupLayout(jpDate);
        jpDate.setLayout(jpDateLayout);
        jpDateLayout.setHorizontalGroup(
            jpDateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jpDateLayout.setVerticalGroup(
            jpDateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 21, Short.MAX_VALUE)
        );

        jcbDate.setAction(actionMap.get("checkDate")); // NOI18N
        jcbDate.setBackground(resourceMap.getColor("jcbDate.background")); // NOI18N
        jcbDate.setText(resourceMap.getString("jcbDate.text")); // NOI18N
        jcbDate.setName("jcbDate"); // NOI18N

        jcbPeriod.setAction(actionMap.get("checkPeriod")); // NOI18N
        jcbPeriod.setBackground(resourceMap.getColor("jcbPeriod.background")); // NOI18N
        jcbPeriod.setText(resourceMap.getString("jcbPeriod.text")); // NOI18N
        jcbPeriod.setName("jcbPeriod"); // NOI18N

        jbDelete.setAction(actionMap.get("deleteDoc")); // NOI18N
        jbDelete.setText(resourceMap.getString("jbDelete.text")); // NOI18N
        jbDelete.setName("jbDelete"); // NOI18N

        jbTrash.setAction(actionMap.get("showTrash")); // NOI18N
        jbTrash.setText(resourceMap.getString("jbTrash.text")); // NOI18N
        jbTrash.setName("jbTrash"); // NOI18N

        jbImport.setAction(actionMap.get("importDocs")); // NOI18N
        jbImport.setText(resourceMap.getString("jbImport.text")); // NOI18N
        jbImport.setName("jbImport"); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 579, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jbAddDoc)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jbDelete)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jbTrash)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jbImport)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 128, Short.MAX_VALUE)
                        .addComponent(jbCancel))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jcbDate)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jpDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jcbPeriod)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jpStartDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jpEndDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jcbPeriod, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jpStartDate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jpEndDate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jpDate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jcbDate, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 278, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jbCancel)
                    .addComponent(jbAddDoc)
                    .addComponent(jbDelete)
                    .addComponent(jbTrash)
                    .addComponent(jbImport))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jtPaymentOrderKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtPaymentOrderKeyPressed

        if (evt.getKeyCode() == 155) {
            addDoc();
        }
        if (evt.getKeyCode() == 127) {
            deleteDoc();
        }
    }//GEN-LAST:event_jtPaymentOrderKeyPressed

    private void jtPaymentOrderMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtPaymentOrderMouseClicked
        if (evt.getClickCount() > 1) {
            int r = jtPaymentOrder.convertRowIndexToModel(jtPaymentOrder.getSelectedRow());
            if (r != -1) {
                JInternalFrame podv =
                        new PaymentOrderDocView(salesView, this, ((Paymentorder) table.get(r)).getCode());
                salesView.getJDesktopPane().add(podv, javax.swing.JLayeredPane.DEFAULT_LAYER);
                podv.setVisible(true);
                try {
                    podv.setSelected(true);
                } catch (Exception e) {
                    logger.error(e);
                }
            }
        }
    }//GEN-LAST:event_jtPaymentOrderMouseClicked
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton jbAddDoc;
    private javax.swing.JButton jbCancel;
    private javax.swing.JButton jbDelete;
    private javax.swing.JButton jbImport;
    private javax.swing.JButton jbTrash;
    private javax.swing.JCheckBox jcbDate;
    private javax.swing.JCheckBox jcbPeriod;
    private javax.swing.JPanel jpDate;
    private javax.swing.JPanel jpEndDate;
    private javax.swing.JPanel jpStartDate;
    private javax.swing.JTable jtPaymentOrder;
    // End of variables declaration//GEN-END:variables
    SalesView salesView;
    private JDateChooser date;
    private JDateChooser startDate;
    private JDateChooser endDate;
    private List table;
    private boolean trash;
    private static ArrayList<String[]> columns;

    public void showTable() {

        table = null;
        try {

            Session session = HUtil.getSession();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String hql = "";
            String delStr = "po.active != 2";
            if (trash) {
                delStr = "po.active = 2";
            }
            if (jcbDate.isSelected()) {
                hql = "from Paymentorder po where "
                        + "DATE(po.datetime) = '" + sdf.format(date.getDate()) + "'"
                        + " and " + delStr
                        + " order by po.datetime DESC";
            } else {
                hql = "from Paymentorder po"
                        + " where DATE(po.datetime) >= '" + sdf.format(startDate.getDate()) + "'"
                        + " and DATE(po.datetime) <= '" + sdf.format(endDate.getDate()) + "'"
                        + " and " + delStr
                        + " order by po.datetime DESC";
            }
            table = session.createQuery(hql).list();

            Vector<String> tableHeaders = new Vector<String>();
            for (String[] col : columns) {
                tableHeaders.add(col[0]);
            }

            Vector tableData = new Vector();

            sdf = new SimpleDateFormat("dd.MM.yyyy");

            if (table != null) {
                for (Object o : table) {
                    Paymentorder po = (Paymentorder) o;
                    Vector<Object> oneRow = new Vector<Object>();
                    oneRow.add(po.getNumber());
                    oneRow.add(sdf.format(po.getDatetime()));
                    Suppliers s = (Suppliers) HUtil.getElement("Suppliers", po.getReceiver(), session);
                    if (s != null) {
                        oneRow.add(s.getName());
                    } else {
                        oneRow.add("");
                    }
                    oneRow.add(po.getAmount());
                    tableData.add(oneRow);
                }
            }

            session.close();

            jtPaymentOrder.setModel(new DefaultTableModel(tableData, tableHeaders) {

                @Override
                public boolean isCellEditable(int r, int c) {
                    return false;
                }
            });
        } catch (Exception e) {
            logger.error(e);
        }


        for (int j = 0; j < jtPaymentOrder.getColumnCount(); j++) {
            jtPaymentOrder.getColumnModel().getColumn(j).setCellRenderer(new MyTableCellRenderer());
        }

        Util.autoResizeColWidth(jtPaymentOrder);

    }

    @Action
    public void addDoc() {
        PaymentOrderDocView doc = new PaymentOrderDocView(salesView, this, null);
        salesView.getJDesktopPane().add(doc, javax.swing.JLayeredPane.DEFAULT_LAYER);
        doc.setVisible(true);
        try {
            doc.setSelected(true);
        } catch (java.beans.PropertyVetoException e) {
            logger.error(e);
        }
    }

    @Action
    public void checkDate() {
        jcbDate.setSelected(true);
        jcbPeriod.setSelected(false);
        showTable();
    }

    @Action
    public void checkPeriod() {
        jcbDate.setSelected(false);
        jcbPeriod.setSelected(true);
        showTable();
    }

    @Action
    public void deleteDoc() {
        if (!trash) {
            int r = jtPaymentOrder.getSelectedRow();
            if (r != -1) {
                int a = JOptionPane.showOptionDialog(
                        this, "Удалить документ № " + ((Paymentorder) table.get(r)).getNumber() + "?", "Удаление", JOptionPane.YES_NO_CANCEL_OPTION,
                        JOptionPane.QUESTION_MESSAGE, null, new Object[]{
                            "Да", "Нет"}, "Нет");
                if (a == 0) {
                    changeDelStatus(2, r);
                }
            }
        } else {
            int r = jtPaymentOrder.getSelectedRow();
            if (r != -1) {
                int a = JOptionPane.showOptionDialog(
                        this, "Восстановить документ № " + ((Paymentorder) table.get(r)).getNumber() + " из корзины?", "Восстановление", JOptionPane.YES_NO_CANCEL_OPTION,
                        JOptionPane.QUESTION_MESSAGE, null, new Object[]{
                            "Да", "Нет"}, "Нет");
                if (a == 0) {
                    changeDelStatus(0, r);
                }
            }
        }

    }

    @Action
    public void printRegister() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Session session = HUtil.getSession();
            session.beginTransaction();
            String hql =
                    "from Register r where DATE(r.datetime) = '" + sdf.format(date.getDate()) + "'";
            List res = session.createQuery(hql).list();
            for (int j = 0; j < res.size(); j++) {
                Register r = (Register) res.get(j);
                session.merge(r);
            }
            session.getTransaction().commit();

            File file = new File(Util.getAppPath() + "\\templates\\CashBook.ods");
            final Sheet sheet = SpreadSheet.createFromFile(file).getSheet(0);

            int amount = 0;
            hql =
                    "from Register r where DATE(r.datetime) = '" + sdf.format(date.getDate()) + "'";
            res = session.createQuery(hql).list();
            int ts = res.size();
            if (ts > 1) {
                sheet.duplicateRows(8, 1, ts - 1);
            }
            int inDocs = 0;
            int outDocs = 0;
            int j;
            for (j = 0; j < res.size(); j++) {
                Register r = (Register) res.get(j);
                if (r.getEmployeeCode() != null) {
                    sheet.getCellAt("A" + (9 + j)).setValue(r.getNumber());
                    sheet.getCellAt("H" + (9 + j)).setValue(r.getNumber());
                    Employee e = (Employee) HUtil.getElement("Employee", r.getEmployeeCode(), session);
                    if (r.getDocumentType() == 0) {
                        if (e != null) {
                            sheet.getCellAt("B" + (9 + j)).setValue(
                                    "Принято от " + e.getName() + " торговая выручка");
                            sheet.getCellAt("I" + (9 + j)).setValue(
                                    "Принято от " + e.getName() + " торговая выручка");
                        }
                        sheet.getCellAt("F" + (9 + j)).setValue("" + r.getAmount() + "=");
                        sheet.getCellAt("M" + (9 + j)).setValue("" + r.getAmount() + "=");
                        amount += r.getAmount();
                        inDocs++;
                    } else {
                        if (r.getDocumentType() == 1) {
                            if (e != null) {
                                sheet.getCellAt("B" + (9 + j)).setValue(
                                        "Выдано " + e.getName() + " торговая выручка для сдачи в банк");
                                sheet.getCellAt("I" + (9 + j)).setValue(
                                        "Выдано " + e.getName() + " торговая выручка для сдачи в банк");
                            }
                            sheet.getCellAt("G" + (9 + j)).setValue("" + r.getAmount() + "=");
                            sheet.getCellAt("N" + (9 + j)).setValue("" + r.getAmount() + "=");
                            amount -= r.getAmount();
                            outDocs++;
                        }
                    }
                }
            }
            if (j == 0) {
                j = 1;
            }

            sheet.getCellAt("F" + (9 + j)).setValue(amount + "=");
            sheet.getCellAt("F" + (10 + j)).setValue(amount + "=");
            sheet.getCellAt("M" + (9 + j)).setValue(amount + "=");
            sheet.getCellAt("M" + (10 + j)).setValue(amount + "=");
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
            sheet.getCellAt("A" + (18 + j)).setValue(s);
            sheet.getCellAt("H" + (18 + j)).setValue(s);

            session.close();

            String r = " " + Math.round(Math.random() * 100000);
            File outputFile = new File("temp\\Касса за " + sdf.format(date.getDate()) + r + ".ods");
            OOUtils.open(sheet.getSpreadSheet().saveAs(outputFile));
        } catch (Exception e) {
        }
    }

    public void close() {
    }

    @Action
    public void closeJournal() {
        Util.closeJIF(this, null, salesView);
        Util.closeJIFTab(this, salesView);
    }

    private void changeDelStatus(int status, int r) {
        try {
            Session session = HUtil.getSession();
            session.beginTransaction();
            Paymentorder doc = (Paymentorder) HUtil.getElement("Paymentorder", ((Paymentorder) table.get(r)).getCode(), session);
            if (doc != null) {
                doc.setActive(status);
                session.save(doc);
            }
            session.getTransaction().commit();
            session.close();
        } catch (Exception e) {
            logger.error(e);
        }
        showTable();
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
    public void print() {
    }

    @Action
    public void importDocs() {

        JFileChooser fc = new JFileChooser();
        int returnVal = fc.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {

            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

            try {

                Session session = HUtil.getSession();
                session.beginTransaction();

                BufferedReader bf = new BufferedReader(new FileReader(fc.getSelectedFile()));
                int state = 0;
                String line = "";
                Date d = null;
                String num = "";
                String acc = "";
                String purpose = "";
                int db = 0;

                while ((line = bf.readLine()) != null) {

                    if (state == 0 && Util.substr(line, 8).equals("^DocDate")) {
                        try {
                            d = sdfi.parse(line.substring(9, line.length() - 1));
                            state = 1;
                        } catch (Exception e) {
                            d = null;
                        }
                    } else if (state == 1 && Util.substr(line, 4).equals("^Num")) {
                        num = line.substring(5, line.length() - 1);
                        state = 2;
                    } else if (state == 2 && Util.substr(line, 4).equals("^Acc")) {
                        acc = line.substring(5, line.length() - 1);
                        state = 3;
                    } else if (state == 3 && Util.substr(line, 3).equals("^Db")) {
                        db = Util.str2int(line.substring(4, line.length() - 4));
                        if (db > 0) {
                            state = 4;
                        } else {
                            state = 0;
                        }
                    } else if (state == 4 && Util.substr(line, 5).equals("^Nazn")) {
                        purpose = line.substring(6, line.length() - 1);
                        Integer supplierCode = null;
                        List res = session.createQuery("from Suppliers where account = '" + acc + "'").list();
                        if (res.size() > 0) {
                            supplierCode = ((Suppliers) res.get(0)).getCode();
                        }
                        res = session.createQuery("from Paymentorder where number = '" + num + "'").list();
                        Paymentorder po;
                        if (res.isEmpty()) {
                            po = new Paymentorder();
                        } else {
                            po = (Paymentorder) res.get(0);
                        }
                        po.setDatetime(d);
                        po.setNumber(num);
                        po.setReceiver(supplierCode);
                        po.setAmount(db);
                        po.setPurpose(purpose);
                        po.setPaymentScheme(0);
                        po.setPaymentCode("");
                        po.setPaymentOrder("22");
                        session.saveOrUpdate(po);
                        state = 0;
                    }
                }

                bf.close();
                
                session.getTransaction().commit();
                session.close();

                showTable();

            } catch (Exception e) {
                e.printStackTrace();
                //logger.error(e);
            }

            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

        }
    }
}
