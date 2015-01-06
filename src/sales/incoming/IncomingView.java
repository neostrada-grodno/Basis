/*
 * JournalView.java
 *
 * Created on 07.05.2011, 14:59:59
 */
package sales.incoming;

import com.toedter.calendar.JDateChooser;
import java.awt.Component;
import java.awt.Font;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Vector;
import javax.swing.GroupLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
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
import sales.entity.Incoming;
import sales.interfaces.IClose;
import sales.interfaces.IJournal;
import sales.util.HUtil;
import sales.util.Init;
import sales.util.Util;

public class IncomingView extends javax.swing.JInternalFrame implements IClose, IJournal, IInit {

    static Logger logger = Logger.getLogger(SalesApp.class.getName());
    private static final SimpleDateFormat sdfd = new SimpleDateFormat("dd.MM.yyyy");

    //create the form
    public IncomingView(SalesView salesView) {

        initComponents();

        Util.initJIF(this, "Журнал приходов", null, salesView);
        Util.initJTable(jtIncoming);

        this.salesView = salesView;

        //set the current date to show docs
        Calendar c = Calendar.getInstance();

        date = new JDateChooser(c.getTime());
        date.getDateEditor().addPropertyChangeListener(new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent e) {
                if ("date".equals(e.getPropertyName())) {
                    checkDate();
                }
            }
        });
        jcbDate.setSelected(false);
        jcbPeriod.setSelected(true);

        GroupLayout gl = (GroupLayout) jpDate.getLayout();
        gl.setHorizontalGroup(gl.createParallelGroup().addGroup(gl.createSequentialGroup().addComponent(date)));
        gl.setVerticalGroup(gl.createParallelGroup().addGroup(gl.createSequentialGroup().addComponent(date)));
        jcbDate.setSelected(true);

        //set the initial date to show docs
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

        //set the final date to show docs
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

        trash = false;

        //set the filter on document types
        jtpIncoming.addTab("Приход", null);
        jtpIncoming.addTab("Инвентаризация", null);
        jtpIncoming.addTab("Ввод остатков", null);
        jtpIncoming.addChangeListener(new ChangeListener() {

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
        if (jtIncoming.getRowCount() > 0) {
            jtIncoming.getSelectionModel().setSelectionInterval(0, 0);
        }

        status = false;

    }

    private void initTable() {

        columns = new ArrayList<>();
        columns.add(new String[]{"", "icon"});
        columns.add(new String[]{"Проведен", "string"});
        columns.add(new String[]{"Номер", "string"});
        columns.add(new String[]{"Дата", "date"});
        columns.add(new String[]{"Поставщик или тип док-та", "string"});
        columns.add(new String[]{"Вх.сумма с НДС", "int"});
        columns.add(new String[]{"Примечание", "string"});
        
        tableHeaders = new Vector<>();
        for (String[] col : columns) {
            tableHeaders.add(col[0]);
        }
        
        jtIncoming.setModel(new IncomingTableModel(new Vector<String>(), tableHeaders));
        
        List<RowSorter.SortKey> sortKeys = new ArrayList<>();
        sortKeys.add(new RowSorter.SortKey(1, SortOrder.ASCENDING));
        sortKeys.add(new RowSorter.SortKey(1, SortOrder.ASCENDING));
        
        SalesTableRowSorter sorter = new SalesTableRowSorter(jtIncoming.getModel(), columns);
        sorter.setMaxSortKeys(2);
        sorter.setSortKeys(sortKeys);
        
        jtIncoming.setRowSorter(sorter);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jbCancel = new javax.swing.JButton();
        jbAddDoc = new javax.swing.JButton();
        jbDelete = new javax.swing.JButton();
        jbJournalOrTrash = new javax.swing.JButton();
        jtpIncoming = new javax.swing.JTabbedPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        jtIncoming = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        jcbFlag = new javax.swing.JCheckBox();
        jcbActiveOnly = new javax.swing.JCheckBox();
        jcbNotActiveOnly = new javax.swing.JCheckBox();
        jPanel2 = new javax.swing.JPanel();
        jcbDate = new javax.swing.JCheckBox();
        jlDate = new javax.swing.JLabel();
        jpDate = new javax.swing.JPanel();
        jcbPeriod = new javax.swing.JCheckBox();
        jlPeriod = new javax.swing.JLabel();
        jpStartDate = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jpEndDate = new javax.swing.JPanel();

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(sales.SalesApp.class).getContext().getResourceMap(IncomingView.class);
        setBackground(resourceMap.getColor("Form.background")); // NOI18N
        setClosable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle(resourceMap.getString("Form.title")); // NOI18N
        setAutoscrolls(true);
        setName("Form"); // NOI18N

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(sales.SalesApp.class).getContext().getActionMap(IncomingView.class, this);
        jbCancel.setAction(actionMap.get("closeJournal")); // NOI18N
        jbCancel.setText(resourceMap.getString("jbCancel.text")); // NOI18N
        jbCancel.setName("jbCancel"); // NOI18N

        jbAddDoc.setAction(actionMap.get("addDoc")); // NOI18N
        jbAddDoc.setText(resourceMap.getString("jbAddDoc.text")); // NOI18N
        jbAddDoc.setName("jbAddDoc"); // NOI18N

        jbDelete.setAction(actionMap.get("deleteDoc")); // NOI18N
        jbDelete.setText(resourceMap.getString("jbDelete.text")); // NOI18N
        jbDelete.setName("jbDelete"); // NOI18N

        jbJournalOrTrash.setAction(actionMap.get("showJournalOrTrash")); // NOI18N
        jbJournalOrTrash.setText(resourceMap.getString("jbJournalOrTrash.text")); // NOI18N
        jbJournalOrTrash.setName("jbJournalOrTrash"); // NOI18N

        jtpIncoming.setBackground(resourceMap.getColor("jtpIncoming.background")); // NOI18N
        jtpIncoming.setName("jtpIncoming"); // NOI18N

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        jtIncoming.setModel(new javax.swing.table.DefaultTableModel(
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
        jtIncoming.setFillsViewportHeight(true);
        jtIncoming.setName("jtIncoming"); // NOI18N
        jtIncoming.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                IncomingView.this.mouseClicked(evt);
            }
        });
        jtIncoming.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jtIncomingKeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(jtIncoming);

        jtpIncoming.addTab(resourceMap.getString("jScrollPane1.TabConstraints.tabTitle"), jScrollPane1); // NOI18N

        jPanel1.setBackground(resourceMap.getColor("jPanel1.background")); // NOI18N
        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel1.setName("jPanel1"); // NOI18N

        jcbFlag.setAction(actionMap.get("showFlag")); // NOI18N
        jcbFlag.setBackground(resourceMap.getColor("jcbFlag.background")); // NOI18N
        jcbFlag.setText(resourceMap.getString("jcbFlag.text")); // NOI18N
        jcbFlag.setName("jcbFlag"); // NOI18N

        jcbActiveOnly.setAction(actionMap.get("changeActiveOnly")); // NOI18N
        jcbActiveOnly.setBackground(resourceMap.getColor("jcbActiveOnly.background")); // NOI18N
        jcbActiveOnly.setText(resourceMap.getString("jcbActiveOnly.text")); // NOI18N
        jcbActiveOnly.setName("jcbActiveOnly"); // NOI18N

        jcbNotActiveOnly.setAction(actionMap.get("changeNotActiveOnly")); // NOI18N
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

        jlDate.setBackground(resourceMap.getColor("jlDate.background")); // NOI18N
        jlDate.setText(resourceMap.getString("jlDate.text")); // NOI18N
        jlDate.setName("jlDate"); // NOI18N

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
            .addGap(0, 23, Short.MAX_VALUE)
        );

        jcbPeriod.setAction(actionMap.get("checkPeriod")); // NOI18N
        jcbPeriod.setBackground(resourceMap.getColor("jcbPeriod.background")); // NOI18N
        jcbPeriod.setText(resourceMap.getString("jcbPeriod.text")); // NOI18N
        jcbPeriod.setName("jcbPeriod"); // NOI18N

        jlPeriod.setBackground(resourceMap.getColor("jlPeriod.background")); // NOI18N
        jlPeriod.setText(resourceMap.getString("jlPeriod.text")); // NOI18N
        jlPeriod.setName("jlPeriod"); // NOI18N

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
            .addGap(0, 23, Short.MAX_VALUE)
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
            .addGap(0, 23, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jcbDate)
                .addGap(6, 6, 6)
                .addComponent(jlDate)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(33, 33, 33)
                .addComponent(jcbPeriod)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jlPeriod)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpStartDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
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
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jcbPeriod, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jlPeriod, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jpStartDate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jlDate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jpDate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jpEndDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(13, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jcbDate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(15, 15, 15))))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jtpIncoming, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 730, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jbAddDoc)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jbDelete)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jbJournalOrTrash)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 354, Short.MAX_VALUE)
                        .addComponent(jbCancel))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 87, Short.MAX_VALUE)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jtpIncoming, javax.swing.GroupLayout.DEFAULT_SIZE, 327, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jbCancel)
                    .addComponent(jbAddDoc)
                    .addComponent(jbDelete)
                    .addComponent(jbJournalOrTrash))
                .addContainerGap())
        );

        jtpIncoming.getAccessibleContext().setAccessibleName(resourceMap.getString("jtpIncoming.AccessibleContext.accessibleName")); // NOI18N

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void mouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mouseClicked
        if (jtIncoming.columnAtPoint(evt.getPoint()) > 0 && evt.getClickCount() > 1) {
            openDoc();
        } else if (jtIncoming.columnAtPoint(evt.getPoint()) == 0) {
            changeFlag();
        }

    }//GEN-LAST:event_mouseClicked

    private void jtIncomingKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtIncomingKeyPressed

        if (evt.getKeyCode() == 155) {
            addDoc();
        }
        if (evt.getKeyCode() == 127) {
            deleteDoc();
        }
    }//GEN-LAST:event_jtIncomingKeyPressed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton jbAddDoc;
    private javax.swing.JButton jbCancel;
    private javax.swing.JButton jbDelete;
    private javax.swing.JButton jbJournalOrTrash;
    private javax.swing.JCheckBox jcbActiveOnly;
    private javax.swing.JCheckBox jcbDate;
    private javax.swing.JCheckBox jcbFlag;
    private javax.swing.JCheckBox jcbNotActiveOnly;
    private javax.swing.JCheckBox jcbPeriod;
    private javax.swing.JLabel jlDate;
    private javax.swing.JLabel jlPeriod;
    private javax.swing.JPanel jpDate;
    private javax.swing.JPanel jpEndDate;
    private javax.swing.JPanel jpStartDate;
    private javax.swing.JTable jtIncoming;
    private javax.swing.JTabbedPane jtpIncoming;
    // End of variables declaration//GEN-END:variables
    private SalesView salesView;
    private JDateChooser date;
    private JDateChooser startDate;
    private JDateChooser endDate;
    private List table;
    private boolean trash;
    private String docFilter;
    private boolean status;
    private static ArrayList<String[]> columns;
    private Vector<String> tableHeaders;

    //custom cell appearance
    public class MyTableCellRenderer extends DefaultTableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(JTable tbl,
                Object obj, boolean isSelected, boolean hasFocus, int row, int column) {

            Component cell = super.getTableCellRendererComponent(
                    tbl, obj, isSelected, hasFocus, row, column);

            if (table != null) {
                Incoming i = ((Incoming) table.get(row));
                if (column > 0 && i.getActive() == 1) {
                    cell.setFont(new Font(this.getFont().getName(), Font.ITALIC, this.getFont().getSize()));
                }
                //if it is a cell with flag
                if (column == 0) {
                    if (i.getFlag() == 1) {
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

    private class IncomingTableModel extends DefaultTableModel {

        IncomingTableModel(Vector tableData, Vector tableHeaders) {
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
    
    //list docs
    public void showTable() {

        table = null;
        try {

            //get the list of appropriate docs
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
                if (jtpIncoming.getSelectedIndex() != 3) {
                    hql = "from Incoming x"
                            + " where DATE(x.datetime) = '" + sdf.format(date.getDate()) + "'"
                            + " and " + delStr
                            + df
                            + " order by x.datetime DESC";
                } else {
                    hql = "from Incoming x"
                            + " where " + delStr
                            + df
                            + " order by x.datetime DESC";
                }
            } else {
                if (jtpIncoming.getSelectedIndex() != 3) {
                    hql = "from Incoming x"
                            + " where DATE(x.datetime) >= '" + sdf.format(startDate.getDate()) + "' and"
                            + " DATE(x.datetime) <='" + sdf.format(endDate.getDate()) + "'"
                            + " and " + delStr
                            + df
                            + " order by x.datetime DESC";
                } else {
                    hql = "from Incoming x"
                            + " where " + delStr
                            + df
                            + " order by x.datetime DESC";
                }
            }
            table = session.createQuery(hql).list();

            Vector tableData = new Vector();

            //fill the JTable
            //((DefaultTableModel) jtIncoming.getModel()).getDataVector().removeAllElements();
            //System.out.println("rn=" + ((DefaultTableModel) jtIncoming.getModel()).getRowCount());
            if (table != null) {
                for (Object o : table) {
                    Incoming in = (Incoming) o;
                    Vector<Object> oneRow = new Vector<Object>();
                    if (in.getFlag() == 0) {
                        oneRow.add(new ImageIcon("images/dot.jpg"));
                    } else {
                        oneRow.add(new ImageIcon("images/flag.jpg"));
                    }
                    if (in.getActive() == 1) {
                        oneRow.add("V");
                    } else {
                        oneRow.add("");
                    }
                    oneRow.add(in.getNumber());
                    oneRow.add(sdfd.format(in.getDatetime()));
                    if (in.getType() == 0) {
                        oneRow.add(HUtil.getNameByCode(in.getContractor(), "Suppliers"));
                    } else if (in.getType() == 1) {
                        oneRow.add("Инвентаризация");
                    } else if (in.getType() == 2) {
                        oneRow.add("Ввод остатков");
                    }
                    oneRow.add(HUtil.getDocFieldSum(in.getCode(), "Incoming", "ndsAndAmount"));
                    oneRow.add(in.getNote());
                    tableData.add(oneRow);
                }
            }
            session.close();

            SalesTableRowSorter sorter = (SalesTableRowSorter) jtIncoming.getRowSorter();
            List<RowSorter.SortKey> sortKeys = sorter.getSortKeys();
            for(int j = 0; j < sortKeys.size(); j++) {
                if (sortKeys.get(j).getColumn() > tableHeaders.size()) {
                    sortKeys = new ArrayList<>();
                    sortKeys.add(new RowSorter.SortKey(1, SortOrder.ASCENDING));
                    sortKeys.add(new RowSorter.SortKey(1, SortOrder.ASCENDING));
                    break;
                }
            }
            jtIncoming.setModel(new IncomingTableModel(tableData, tableHeaders));
            sorter.setModel(jtIncoming.getModel());
            sorter.setSortKeys(sortKeys);

        } catch (Exception e) {
            logger.error(e);
        }

        for (int j = 0; j < jtIncoming.getColumnCount(); j++) {
            jtIncoming.getColumnModel().getColumn(j).setCellRenderer(new MyTableCellRenderer());
        }
        
        Util.autoResizeColWidth(jtIncoming);
        jtIncoming.getColumnModel().getColumn(0).setMinWidth(17);
        jtIncoming.getColumnModel().getColumn(0).setMaxWidth(17);
        jtIncoming.getColumnModel().getColumn(1).setMinWidth(70);
        jtIncoming.getColumnModel().getColumn(1).setMaxWidth(70);

    }

    @Action
    public void addDoc() {
        Init init = new Init();
        init.init(this, "Incoming");
        if (status) {
            IncomingDocView idv = new IncomingDocView(salesView, this, null, null);
            salesView.getJDesktopPane().add(idv, javax.swing.JLayeredPane.DEFAULT_LAYER);
            idv.setVisible(true);
            try {
                idv.setSelected(true);
            } catch (java.beans.PropertyVetoException e) {
                logger.error(e);
            }
            idv.setFocus();
        }
    }

    public void close() {
    }

    @Action
    public void closeJournal() {
        Util.closeJIF(this, null, salesView);
        Util.closeJIFTab(this, salesView);
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

    //delete/undelete a doc in the given row
    private void changeDelStatus(int status, int r) {
        try {
            Session session = HUtil.getSession();
            session.beginTransaction();
            Incoming i = (Incoming) HUtil.getElement("Incoming", ((Incoming) table.get(r)).getCode(), session);
            if (i != null) {
                i.setActive(status);
                session.save(i);
            }
            session.getTransaction().commit();
            session.close();
        } catch (Exception e) {
            logger.error(e);
        }
        showTable();
    }

    @Action
    public void deleteDoc() {
        if (!trash) {
            int r = jtIncoming.getSelectedRow();
            if (r != -1) {
                int a = JOptionPane.showOptionDialog(
                        this, "Удалить документ № " + ((Incoming) table.get(r)).getNumber() + "?", "Удаление", JOptionPane.YES_NO_CANCEL_OPTION,
                        JOptionPane.QUESTION_MESSAGE, null, new Object[]{
                            "Да", "Нет"}, "Нет");
                if (a == 0) {
                    changeDelStatus(2, r);
                }
            }
        } else {
            int r = jtIncoming.getSelectedRow();
            if (r != -1) {
                int a = JOptionPane.showOptionDialog(
                        this, "Восстановить документ № " + ((Incoming) table.get(r)).getNumber() + " из корзины?", "Восстановление", JOptionPane.YES_NO_CANCEL_OPTION,
                        JOptionPane.QUESTION_MESSAGE, null, new Object[]{
                            "Да", "Нет"}, "Нет");
                if (a == 0) {
                    changeDelStatus(0, r);
                }
            }
        }
    }

    //switch between journal/trash
    @Action
    public void showJournalOrTrash() {
        if (!trash) {
            jbDelete.setText("Восстановить");
            jbJournalOrTrash.setText("Журнал");
            trash = true;

        } else {
            jbDelete.setText("Удалить");
            jbJournalOrTrash.setText("Корзина");
            trash = false;
        }
        showTable();
    }

    //filter on docs depends on the doc type selected
    private void tabSelected() {
        boolean datesEnabled = true;
        if (jtpIncoming.getSelectedIndex() == 0) {
            docFilter = "";
        } else if (jtpIncoming.getSelectedIndex() == 1) {
            docFilter = "x.type = 0";
        } else if (jtpIncoming.getSelectedIndex() == 2) {
            docFilter = "x.type = 1";
        } else if (jtpIncoming.getSelectedIndex() == 3) {
            datesEnabled = false;
            docFilter = "x.type = 2";
        }
        jcbDate.setEnabled(datesEnabled);
        date.setEnabled(datesEnabled);
        jcbPeriod.setEnabled(datesEnabled);
        startDate.setEnabled(datesEnabled);
        endDate.setEnabled(datesEnabled);
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    @Action
    public void changeActiveOnly() {
        if (!jcbActiveOnly.isSelected() && !jcbNotActiveOnly.isSelected()) {
            jcbActiveOnly.setSelected(true);
        }
        showTable();
    }

    @Action
    public void changeNotActiveOnly() {
        if (!jcbActiveOnly.isSelected() && !jcbNotActiveOnly.isSelected()) {
            jcbNotActiveOnly.setSelected(true);
        }
        showTable();
    }

    private void openDoc() {

        int r = jtIncoming.convertRowIndexToModel(jtIncoming.getSelectedRow());
        if (r != -1) {
            IncomingDocView idv =
                    new IncomingDocView(salesView, this, ((Incoming) table.get(r)).getCode(), null);
            salesView.getJDesktopPane().add(idv, javax.swing.JLayeredPane.DEFAULT_LAYER);
            idv.setVisible(true);
            try {
                idv.setSelected(true);
            } catch (Exception e) {
                logger.error(e);
            }
            idv.setFocus();
        }
    }

    private void changeFlag() {

        int r = jtIncoming.getSelectedRow();
        if (r != -1) {
            Session session = HUtil.getSession();
            session.beginTransaction();
            Incoming i = (Incoming) table.get(r);
            Incoming in = (Incoming) HUtil.getElement("Incoming", i.getCode(), session);
            i.setFlag(1 - i.getFlag());
            in.setFlag(i.getFlag());
            session.update(in);
            session.getTransaction().commit();
            session.close();
            if (i.getFlag() == 1) {
                jtIncoming.getModel().setValueAt(new ImageIcon("images/flag.jpg"), r, 0);
            } else {
                jtIncoming.getModel().setValueAt(new ImageIcon("images/dot.jpg"), r, 0);
            }
        }
    }

    @Action
    public void showFlag() {
        showTable();
    }
}