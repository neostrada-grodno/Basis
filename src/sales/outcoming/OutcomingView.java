/*
 * OucomingView.java
 *
 * Created on 07.05.2011, 14:59:59
 */
package sales.outcoming;

import com.toedter.calendar.JDateChooser;
import java.awt.Component;
import java.awt.Font;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Vector;
import javax.swing.GroupLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import org.apache.log4j.Logger;
import org.hibernate.classic.Session;
import org.jdesktop.application.Action;
import sales.interfaces.IInit;
import sales.SalesApp;
import sales.SalesView;
import sales.auxiliarly.SalesTableRowSorter;
import sales.interfaces.IScannerView;
import sales.scanner.ScannerAbstractAction;
import sales.entity.Outcoming;
import sales.entity.Outcomingtable;
import sales.entity.Serialtable;
import sales.interfaces.IClose;
import sales.interfaces.IJournal;
import sales.util.HUtil;
import sales.util.Init;
import sales.util.Util;

public class OutcomingView extends javax.swing.JInternalFrame implements IScannerView, IClose, IJournal, IInit {

    private static Logger logger = Logger.getLogger(SalesApp.class.getName());

    public OutcomingView(SalesView salesView) {
        initComponents();

        Util.initJIF(this, "Журнал расходов", null, salesView);
        Util.initJTable(jtOutcoming);

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

        jcbPeriod.setSelected(true);

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

        jcbDate.setSelected(false);
        jcbPeriod.setSelected(true);

        jtpOutcoming.addTab("ТН", null);
        jtpOutcoming.addTab("ТТН", null);
        jtpOutcoming.addTab("Касса", null);
        jtpOutcoming.addTab("Терминал", null);
        jtpOutcoming.addTab("Инвентаризация", null);
        jtpOutcoming.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                tabSelected();
                showTable();
            }
        });
        docFilter = "";

        jcbActiveOnly.setSelected(true);
        jcbNotActiveOnly.setSelected(true);

        initTable();
        showTable();

        if (jtOutcoming.getRowCount() > 0) {
            jtOutcoming.getSelectionModel().setSelectionInterval(0, 0);
        }

        scanEnter = false;

        Util.addScannerKeys(this);

        trash = false;

        status = false;

    }

    private void initTable() {

        columns = new ArrayList<>();
        columns.add(new String[]{"", "string"});
        columns.add(new String[]{"Проведен", "string"});
        columns.add(new String[]{"Номер", "string"});
        columns.add(new String[]{"№ счет-фактуры", "string"});
        columns.add(new String[]{"№ ТН", "string"});
        columns.add(new String[]{"Дата", "date"});
        columns.add(new String[]{"Дата с/ф", "date"});
        columns.add(new String[]{"Тип док-та, контрагент", "string"});
        columns.add(new String[]{"Сумма", "int"});
        columns.add(new String[]{"Примечание", "string"});

        tableHeaders = new Vector<>();
        for (String[] col : columns) {
            tableHeaders.add(col[0]);
        }

        jtOutcoming.setModel(new OutcomingTableModel(new Vector<String>(), tableHeaders));

        List<RowSorter.SortKey> sortKeys = new ArrayList<>();
        sortKeys.add(new RowSorter.SortKey(1, SortOrder.ASCENDING));
        sortKeys.add(new RowSorter.SortKey(1, SortOrder.ASCENDING));

        SalesTableRowSorter sorter = new SalesTableRowSorter(jtOutcoming.getModel(), columns);
        sorter.setMaxSortKeys(2);
        sorter.setSortKeys(sortKeys);

        jtOutcoming.setRowSorter(sorter);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jbCancel = new javax.swing.JButton();
        jbAddDoc = new javax.swing.JButton();
        jbDelete = new javax.swing.JButton();
        jtfSerial = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jbFindByScancode = new javax.swing.JButton();
        jbTrash = new javax.swing.JButton();
        jtpOutcoming = new javax.swing.JTabbedPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        jtOutcoming = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        jcbFlag = new javax.swing.JCheckBox();
        jcbActiveOnly = new javax.swing.JCheckBox();
        jcbNotActiveOnly = new javax.swing.JCheckBox();
        jPanel2 = new javax.swing.JPanel();
        jcbDate = new javax.swing.JCheckBox();
        jLabel3 = new javax.swing.JLabel();
        jcbPeriod = new javax.swing.JCheckBox();
        jpStartDate = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jpEndDate = new javax.swing.JPanel();
        jpDate = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jlTotal = new javax.swing.JLabel();

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance().getContext().getResourceMap(OutcomingView.class);
        setBackground(resourceMap.getColor("Form.background")); // NOI18N
        setClosable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle(resourceMap.getString("Form.title")); // NOI18N
        setName("Form"); // NOI18N

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance().getContext().getActionMap(OutcomingView.class, this);
        jbCancel.setAction(actionMap.get("closeJournal")); // NOI18N
        jbCancel.setText(resourceMap.getString("jbCancel.text")); // NOI18N
        jbCancel.setName("jbCancel"); // NOI18N

        jbAddDoc.setAction(actionMap.get("addDoc")); // NOI18N
        jbAddDoc.setText(resourceMap.getString("jbAddDoc.text")); // NOI18N
        jbAddDoc.setName("jbAddDoc"); // NOI18N

        jbDelete.setAction(actionMap.get("deleteDoc")); // NOI18N
        jbDelete.setText(resourceMap.getString("jbDelete.text")); // NOI18N
        jbDelete.setName("jbDelete"); // NOI18N

        jtfSerial.setText(resourceMap.getString("jtfSerial.text")); // NOI18N
        jtfSerial.setName("jtfSerial"); // NOI18N

        jLabel4.setText(resourceMap.getString("jLabel4.text")); // NOI18N
        jLabel4.setName("jLabel4"); // NOI18N

        jbFindByScancode.setAction(actionMap.get("findByScancode")); // NOI18N
        jbFindByScancode.setText(resourceMap.getString("jbFindByScancode.text")); // NOI18N
        jbFindByScancode.setName("jbFindByScancode"); // NOI18N

        jbTrash.setAction(actionMap.get("showTrash")); // NOI18N
        jbTrash.setText(resourceMap.getString("jbTrash.text")); // NOI18N
        jbTrash.setName("jbTrash"); // NOI18N

        jtpOutcoming.setName("jtpOutcoming"); // NOI18N

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        jtOutcoming.setModel(new javax.swing.table.DefaultTableModel(
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
        jtOutcoming.setFillsViewportHeight(true);
        jtOutcoming.setName("jtOutcoming"); // NOI18N
        jtOutcoming.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                OutcomingView.this.mouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jtOutcoming);

        jtpOutcoming.addTab(resourceMap.getString("jScrollPane1.TabConstraints.tabTitle"), jScrollPane1); // NOI18N
        jScrollPane1.getAccessibleContext().setAccessibleName(resourceMap.getString("jScrollPane1.AccessibleContext.accessibleName")); // NOI18N

        jPanel1.setBackground(resourceMap.getColor("jPanel1.background")); // NOI18N
        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel1.setName("jPanel1"); // NOI18N

        jcbFlag.setAction(actionMap.get("showFlag")); // NOI18N
        jcbFlag.setBackground(resourceMap.getColor("jcbFlag.background")); // NOI18N
        jcbFlag.setText(resourceMap.getString("jcbFlag.text")); // NOI18N
        jcbFlag.setName("jcbFlag"); // NOI18N

        jcbActiveOnly.setAction(actionMap.get("showActiveOnly")); // NOI18N
        jcbActiveOnly.setBackground(resourceMap.getColor("jcbActiveOnly.background")); // NOI18N
        jcbActiveOnly.setText(resourceMap.getString("jcbActiveOnly.text")); // NOI18N
        jcbActiveOnly.setName("jcbActiveOnly"); // NOI18N

        jcbNotActiveOnly.setAction(actionMap.get("checkNotActiveOnly")); // NOI18N
        jcbNotActiveOnly.setBackground(resourceMap.getColor("jcbNotActiveOnly.background")); // NOI18N
        jcbNotActiveOnly.setText(resourceMap.getString("jcbNotActiveOnly.text")); // NOI18N
        jcbNotActiveOnly.setName("jcbNotActiveOnly"); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jcbFlag)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jcbNotActiveOnly)
                    .addComponent(jcbActiveOnly))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jcbFlag)
                    .addComponent(jcbActiveOnly))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jcbNotActiveOnly)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.setBackground(resourceMap.getColor("jPanel2.background")); // NOI18N
        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel2.setName("jPanel2"); // NOI18N

        jcbDate.setAction(actionMap.get("checkDate")); // NOI18N
        jcbDate.setBackground(resourceMap.getColor("jcbDate.background")); // NOI18N
        jcbDate.setText(resourceMap.getString("jcbDate.text")); // NOI18N
        jcbDate.setName("jcbDate"); // NOI18N

        jLabel3.setBackground(resourceMap.getColor("jLabel3.background")); // NOI18N
        jLabel3.setText(resourceMap.getString("jLabel3.text")); // NOI18N
        jLabel3.setName("jLabel3"); // NOI18N

        jcbPeriod.setAction(actionMap.get("checkPeriod")); // NOI18N
        jcbPeriod.setBackground(resourceMap.getColor("jcbPeriod.background")); // NOI18N
        jcbPeriod.setText(resourceMap.getString("jcbPeriod.text")); // NOI18N
        jcbPeriod.setName("jcbPeriod"); // NOI18N

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

        jLabel1.setBackground(resourceMap.getColor("jLabel1.background")); // NOI18N
        jLabel1.setText(resourceMap.getString("jLabel1.text")); // NOI18N
        jLabel1.setName("jLabel1"); // NOI18N

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jcbDate)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(8, 8, 8)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jcbPeriod)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpStartDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpEndDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jpEndDate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 21, Short.MAX_VALUE)
                    .addComponent(jpStartDate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jcbPeriod, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jpDate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jcbDate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 21, Short.MAX_VALUE))
                .addContainerGap())
        );

        jlTotal.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jlTotal.setText(resourceMap.getString("jlTotal.text")); // NOI18N
        jlTotal.setName("jlTotal"); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jtpOutcoming, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 743, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(jbAddDoc)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jbDelete)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jbTrash)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jtfSerial, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jbFindByScancode)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jbCancel))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 133, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jlTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jlTotal)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jtpOutcoming, javax.swing.GroupLayout.DEFAULT_SIZE, 382, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jbCancel)
                    .addComponent(jbAddDoc)
                    .addComponent(jbDelete)
                    .addComponent(jbFindByScancode)
                    .addComponent(jLabel4)
                    .addComponent(jtfSerial, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jbTrash))
                .addContainerGap())
        );

        jtpOutcoming.getAccessibleContext().setAccessibleName(resourceMap.getString("jtpOutcoming.AccessibleContext.accessibleName")); // NOI18N

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void mouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mouseClicked
        if (jtOutcoming.columnAtPoint(evt.getPoint()) > 0 && evt.getClickCount() > 1) {
            openDoc();
        } else if (jtOutcoming.columnAtPoint(evt.getPoint()) == 0) {
            changeFlag();
        }
    }//GEN-LAST:event_mouseClicked
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton jbAddDoc;
    private javax.swing.JButton jbCancel;
    private javax.swing.JButton jbDelete;
    private javax.swing.JButton jbFindByScancode;
    private javax.swing.JButton jbTrash;
    private javax.swing.JCheckBox jcbActiveOnly;
    private javax.swing.JCheckBox jcbDate;
    private javax.swing.JCheckBox jcbFlag;
    private javax.swing.JCheckBox jcbNotActiveOnly;
    private javax.swing.JCheckBox jcbPeriod;
    private javax.swing.JLabel jlTotal;
    private javax.swing.JPanel jpDate;
    private javax.swing.JPanel jpEndDate;
    private javax.swing.JPanel jpStartDate;
    private javax.swing.JTable jtOutcoming;
    private javax.swing.JTextField jtfSerial;
    private javax.swing.JTabbedPane jtpOutcoming;
    // End of variables declaration//GEN-END:variables
    SalesView salesView;
    private JDateChooser date;
    private JDateChooser startDate;
    private JDateChooser endDate;
    private List table;
    private boolean scanEnter;
    private String scanCode;
    private boolean trash;
    private String docFilter;
    private boolean status;
    private ArrayList<String[]> columns;
    private Vector<String> tableHeaders;

    public class MyTableCellRenderer extends DefaultTableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(JTable tbl,
                Object obj, boolean isSelected, boolean hasFocus, int row, int column) {

            Component cell = super.getTableCellRendererComponent(
                    tbl, obj, isSelected, hasFocus, row, column);

            if (table != null) {
                Outcoming o = ((Outcoming) table.get(row));
                if (column > 0 && o.getActive() == 1) {
                    cell.setFont(new Font(this.getFont().getName(), Font.ITALIC, this.getFont().getSize()));
                }
                if (column == 0) {
                    if (o.getFlag() == 1) {
                        ((JLabel) cell).setIcon(new ImageIcon("images/flag.jpg"));
                        ((JLabel) cell).setText("");
                    } else {
                        ((JLabel) cell).setIcon(new ImageIcon("images/dot.jpg"));
                        ((JLabel) cell).setText("");
                    }
                }
            }

            return cell;
        }
    }

    private class OutcomingTableModel extends DefaultTableModel {

        OutcomingTableModel(Vector tableData, Vector tableHeaders) {
            super(tableData, tableHeaders);
        }

        @Override
        public boolean isCellEditable(int r, int c) {
            return false;
        }

        public Class getColumnClass(int column) {

            Class columnClass = super.getColumnClass(column);

            if (column == 0) {
                columnClass = Icon.class;
            }

            return columnClass;
        }
    }

    public void showTable() {

        try {

            Session session = HUtil.getSession();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            String hql = "";
            String delStr = "x.active != 2";
            if (trash) {
                delStr = "x.active = 2";
            }
            if (jcbActiveOnly.isSelected() && !jcbNotActiveOnly.isSelected()) {
                delStr += " and x.active = 1";
            } else if (jcbNotActiveOnly.isSelected() && !jcbActiveOnly.isSelected()) {
                delStr += " and x.active = 0";
            }
            if (jcbFlag.isSelected()) {
                delStr += " and x.flag = 1";
            }
            String df = "";
            if (!docFilter.isEmpty()) {
                df = " and " + docFilter;
            }
            if (jcbDate.isSelected()) {
                hql = "from Outcoming x"
                        + " where DATE(x.datetime) = '" + sdf.format(date.getDate()) + "'"
                        + " and " + delStr
                        + df
                        + " order by x.datetime DESC";
            }
            if (jcbPeriod.isSelected()) {
                hql = "from Outcoming x"
                        + " where x.datetime >= '" + sdf.format(startDate.getDate()) + "'"
                        + " and x.datetime < '" + sdf.format(endDate.getDate()) + "'"
                        + " and " + delStr
                        + df
                        + " order by x.datetime DESC";
            }
            table = session.createQuery(hql).list();

            columns = new ArrayList<>();
            columns.add(new String[]{"", "string"});
            columns.add(new String[]{"Проведен", "string"});
            columns.add(new String[]{"Номер", "string"});
            if (jtpOutcoming.getSelectedIndex() < 3) {
                columns.add(new String[]{"№ счет-фактуры", "string"});
                columns.add(new String[]{"№ ТН", "string"});
            }
            columns.add(new String[]{"Дата", "date"});
            columns.add(new String[]{"Дата с/ф", "date"});
            if (jtpOutcoming.getSelectedIndex() == 0) {
                columns.add(new String[]{"Тип док-та, контрагент", "string"});
            } else if (jtpOutcoming.getSelectedIndex() == 1 || jtpOutcoming.getSelectedIndex() == 2) {
                columns.add(new String[]{"Контрагент", "string"});
            }
            columns.add(new String[]{"Сумма", "int"});
            columns.add(new String[]{"Примечание", "string"});

            tableHeaders = new Vector<>();
            for (String[] col : columns) {
                tableHeaders.add(col[0]);
            }

            long total = 0;

            Vector tableData = new Vector();
            if (table != null) {
                for (Object o : table) {
                    Outcoming out = (Outcoming) o;
                    Vector<Object> oneRow = new Vector<Object>();
                    if (out.getFlag() == 0) {
                        oneRow.add(new ImageIcon("images/dot.jpg"));
                    } else {
                        oneRow.add(new ImageIcon("images/flag.jpg"));
                    }
                    if (out.getActive() == 1) {
                        oneRow.add("V");
                    } else {
                        oneRow.add("");
                    }
                    oneRow.add(out.getNumber());
                    if (jtpOutcoming.getSelectedIndex() < 3) {
                        oneRow.add(out.getInvoiceNumber());
                        oneRow.add(out.getTnnumber());
                    }
                    oneRow.add(Util.date2str(out.getDatetime()));
                    oneRow.add(Util.date2str(out.getInvoiceDate()));
                    if (jtpOutcoming.getSelectedIndex() == 0) {
                        if (out.getDocumentType() == 0) {
                            oneRow.add("ТН, " + HUtil.getNameByCode(out.getContractor(), "Contractors"));
                        } else if (out.getDocumentType() == 1) {
                            oneRow.add("Терминал");
                        } else if (out.getDocumentType() == 2) {
                            oneRow.add("Касса");
                        } else if (out.getDocumentType() == 3) {
                            oneRow.add("Инвентаризация");
                        } else if (out.getDocumentType() == 4) {
                            oneRow.add("Касса за день");
                        } else if (out.getDocumentType() == 5) {
                            oneRow.add("Терминал за день");
                        } else if (out.getDocumentType() == 6) {
                            oneRow.add("ТТН, " + HUtil.getNameByCode(out.getContractor(), "Contractors"));
                        }
                    } else if (jtpOutcoming.getSelectedIndex() == 1 || jtpOutcoming.getSelectedIndex() == 2) {
                        oneRow.add(HUtil.getNameByCode(out.getContractor(), "Contractors"));
                    }
                    oneRow.add(HUtil.getDocFieldSum(out.getCode(), "Outcoming", "amount"));
                    oneRow.add(out.getNote());
                    tableData.add(oneRow);

                    String sql = "select sum(amount) from Outcomingtable where documentCode = " + out.getCode();
                    List res = session.createSQLQuery(sql).list();
                    total += Util.getLongObj(res.get(0));
                }
            }
            jlTotal.setText("Сумма: " + new DecimalFormat("#,###,###").format(total));

            SalesTableRowSorter sorter = (SalesTableRowSorter) jtOutcoming.getRowSorter();
            List<RowSorter.SortKey> sortKeys = sorter.getSortKeys();
            for (int j = 0; j < sortKeys.size(); j++) {
                if (sortKeys.get(j).getColumn() > tableHeaders.size()) {
                    sortKeys = new ArrayList<>();
                    sortKeys.add(new RowSorter.SortKey(1, SortOrder.ASCENDING));
                    sortKeys.add(new RowSorter.SortKey(1, SortOrder.ASCENDING));
                    break;
                }
            }
            jtOutcoming.setModel(new OutcomingTableModel(tableData, tableHeaders));
            sorter.setModel(jtOutcoming.getModel());
            sorter.setSortKeys(sortKeys);

            for (int j = 0; j < jtOutcoming.getColumnCount(); j++) {
                jtOutcoming.getColumnModel().getColumn(j).setCellRenderer(new MyTableCellRenderer());
            }

            Util.autoResizeColWidth(jtOutcoming);
            jtOutcoming.getColumnModel().getColumn(0).setMinWidth(17);
            jtOutcoming.getColumnModel().getColumn(0).setMaxWidth(17);
            jtOutcoming.getColumnModel().getColumn(1).setMinWidth(60);
            jtOutcoming.getColumnModel().getColumn(1).setMaxWidth(60);
            jtOutcoming.getColumnModel().getColumn(5).setMinWidth(50);
            jtOutcoming.getColumnModel().getColumn(5).setMaxWidth(50);
            jtOutcoming.getColumnModel().getColumn(6).setMinWidth(50);
            jtOutcoming.getColumnModel().getColumn(6).setMaxWidth(50);

            session.close();
            
        } catch (Exception e) {
            logger.error(e);
        }

    }

    @Action
    public void addDoc() {
        Init init = new Init();
        init.init(this, "Outcoming");
        if (status) {
            OutcomingDocView odv;
            if (jtpOutcoming.getSelectedIndex() > 0) {
                odv = new OutcomingDocView(salesView, this, null, jtpOutcoming.getSelectedIndex() - 1, false, null);
            } else {
                odv = new OutcomingDocView(salesView, this, null, 0, false, null);
            }
            salesView.getJDesktopPane().add(odv, javax.swing.JLayeredPane.DEFAULT_LAYER);
            odv.setVisible(true);
            try {
                odv.setSelected(true);
            } catch (java.beans.PropertyVetoException e) {
                logger.error(e);
            }
            odv.setFocus();
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
            int r = jtOutcoming.getSelectedRow();
            if (r != -1) {
                int a = JOptionPane.showOptionDialog(
                        this, "Удалить документ № " + ((Outcoming) table.get(r)).getNumber() + "?", "Удаление", JOptionPane.YES_NO_CANCEL_OPTION,
                        JOptionPane.QUESTION_MESSAGE, null, new Object[]{
                            "Да", "Нет"}, "Нет");
                if (a == 0) {
                    changeDelStatus(2, r);
                }
            }
        } else {
            int r = jtOutcoming.getSelectedRow();
            if (r != -1) {
                int a = JOptionPane.showOptionDialog(
                        this, "Восстановить документ № " + ((Outcoming) table.get(r)).getNumber() + " из корзины?", "Восстановление", JOptionPane.YES_NO_CANCEL_OPTION,
                        JOptionPane.QUESTION_MESSAGE, null, new Object[]{
                            "Да", "Нет"}, "Нет");
                if (a == 0) {
                    changeDelStatus(0, r);
                }
            }
        }
    }

    @Action
    @Override
    public void useScancode() {
        try {
            Session session = HUtil.getSession();
            String hql =
                    "from Serialtable where serial = " + Util.extractNumber(jtfSerial.getText());
            List res = session.createQuery(hql).list();
            if (res.size() > 0) {
                Serialtable st = (Serialtable) res.get(0);
                OutcomingDocView odv =
                        new OutcomingDocView(salesView, this, st.getDocumentCode(), 0, false, null);
                salesView.getJDesktopPane().add(odv, javax.swing.JLayeredPane.DEFAULT_LAYER);
                odv.setVisible(true);
                odv.setSelected(true);
                List odvTable = odv.getTable();
                JTable odvJTable = odv.getJTable();
                for (int i = 0; i < odvTable.size(); i++) {
                    if (((Outcomingtable) odvTable.get(i)).getProductCode() == st.getProductCode()) {
                        odvJTable.setRowSelectionInterval(i, i);
                        odvJTable.setColumnSelectionInterval(0, odvJTable.getColumnCount() - 1);
                        break;
                    }
                }
            }
        } catch (Exception e) {
            logger.error(e);
        }
    }

    public void addKey(char ch) {
        ScannerAbstractAction aa = new ScannerAbstractAction(this, ch);
        KeyStroke ks = KeyStroke.getKeyStroke(ch);
        getRootPane().getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(ks, ch);
        getRootPane().getActionMap().put(ch, aa);
    }

    @Override
    public boolean getScanEnter() {
        return scanEnter;
    }

    @Override
    public void setScanEnter(boolean scanEnter) {
        this.scanEnter = scanEnter;
    }

    @Override
    public String getScanCode() {
        return scanCode;
    }

    @Override
    public void setScanCode(String scanCode) {
        this.scanCode = scanCode;
        jtfSerial.setText(scanCode);
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
            Outcoming doc = (Outcoming) HUtil.getElement("Outcoming", ((Outcoming) table.get(r)).getCode(), session);
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

    private void tabSelected() {
        if (jtpOutcoming.getSelectedIndex() == 0) {
            docFilter = "";
        } else if (jtpOutcoming.getSelectedIndex() == 1) {
            docFilter = "x.documentType = 0";
        } else if (jtpOutcoming.getSelectedIndex() == 2) {
            docFilter = "x.documentType = 6";
        } else if (jtpOutcoming.getSelectedIndex() == 3) {
            docFilter = "(x.documentType = 2 or x.documentType = 4)";
        } else if (jtpOutcoming.getSelectedIndex() == 5) {
            docFilter = "(x.documentType = 1 or x.documentType = 5)";
        } else if (jtpOutcoming.getSelectedIndex() == 4) {
            docFilter = "x.documentType = 3";
        }
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    @Action
    public void showActiveOnly() {
        if (!jcbActiveOnly.isSelected() && !jcbNotActiveOnly.isSelected()) {
            jcbActiveOnly.setSelected(true);
        }
        showTable();
    }

    @Action
    public void checkNotActiveOnly() {
        if (!jcbActiveOnly.isSelected() && !jcbNotActiveOnly.isSelected()) {
            jcbNotActiveOnly.setSelected(true);
        }
        showTable();
    }

    @Action
    public void showFlag() {
        showTable();
    }

    public void openDoc() {
        int r = jtOutcoming.convertRowIndexToModel(jtOutcoming.getSelectedRow());
        if (r != -1) {
            OutcomingDocView odv =
                    new OutcomingDocView(salesView, this, ((Outcoming) table.get(r)).getCode(), 0, false, null);
            salesView.getJDesktopPane().add(odv, javax.swing.JLayeredPane.DEFAULT_LAYER);
            odv.setVisible(true);
            try {
                odv.setSelected(true);
            } catch (Exception e) {
                logger.error(e);
            }
            odv.setFocus();
        }
    }

    private void changeFlag() {

        int r = jtOutcoming.getSelectedRow();
        if (r != -1) {
            Session session = HUtil.getSession();
            session.beginTransaction();
            Outcoming o = (Outcoming) table.get(r);
            Outcoming out = (Outcoming) HUtil.getElement("Outcoming", o.getCode(), session);
            o.setFlag(1 - o.getFlag());
            out.setFlag(o.getFlag());
            session.update(out);
            session.getTransaction().commit();
            session.close();
            if (o.getFlag() == 1) {
                jtOutcoming.getModel().setValueAt(new ImageIcon("images/flag.jpg"), r, 0);
            } else {
                jtOutcoming.getModel().setValueAt(new ImageIcon("images/dot.jpg"), r, 0);
            }
        }
    }
}
