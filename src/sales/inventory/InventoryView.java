
/*
 * JournalView.java
 *
 * Created on 07.05.2011, 14:59:59
 */
package sales.inventory;

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
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import org.apache.log4j.Logger;
import org.hibernate.classic.Session;
import org.jdesktop.application.Action;
import sales.SalesApp;
import sales.SalesView;
import sales.entity.Incoming;
import sales.entity.Inventory;
import sales.entity.Outcoming;
import sales.incoming.IncomingView;
import sales.interfaces.IClose;
import sales.interfaces.IJournal;
import sales.outcoming.OutcomingView;
import sales.util.HUtil;
import sales.util.Util;

public class InventoryView extends javax.swing.JInternalFrame implements IClose, IJournal {

    static Logger logger = Logger.getLogger(SalesApp.class.getName());

    public InventoryView(SalesView salesView) {

        initComponents();

        Util.initJIF(this, "Журнал инвентаризаций", null, salesView);
        Util.initJTable(jtInventory);

        this.salesView = salesView;

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

        trash = false;

        initColumnsArray();
        showTable();
        if (jtInventory.getRowCount() > 0) {
            jtInventory.getSelectionModel().setSelectionInterval(0, 0);
        }

    }

    private void initColumnsArray() {
        columns = new ArrayList<>();
        columns.add(new String[] {"Номер", "string"});
        columns.add(new String[] {"Дата", "date"});
    }
    
    public class MyTableCellRenderer extends DefaultTableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(JTable tbl,
                Object obj, boolean isSelected, boolean hasFocus, int row, int column) {
            Component cell = super.getTableCellRendererComponent(
                    tbl, obj, isSelected, hasFocus, row, column);
            if (((Inventory) table.get(row)).getActive() == 1) {
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
        jtInventory = new javax.swing.JTable();
        jbCancel = new javax.swing.JButton();
        jbAddDoc = new javax.swing.JButton();
        jpStartDate = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jpEndDate = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jpDate = new javax.swing.JPanel();
        jcbDate = new javax.swing.JCheckBox();
        jcbPeriod = new javax.swing.JCheckBox();
        jbDelete = new javax.swing.JButton();
        jbJournalOrTrash = new javax.swing.JButton();

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(sales.SalesApp.class).getContext().getResourceMap(InventoryView.class);
        setBackground(resourceMap.getColor("Form.background")); // NOI18N
        setClosable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle(resourceMap.getString("Form.title")); // NOI18N
        setAutoscrolls(true);
        setName("Form"); // NOI18N

        jScrollPane1.setBackground(resourceMap.getColor("jScrollPane1.background")); // NOI18N
        jScrollPane1.setName("jScrollPane1"); // NOI18N

        jtInventory.setModel(new javax.swing.table.DefaultTableModel(
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
        jtInventory.setFillsViewportHeight(true);
        jtInventory.setName("jtInventory"); // NOI18N
        jtInventory.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                openDoc(evt);
            }
        });
        jtInventory.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jtInventoryKeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(jtInventory);

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(sales.SalesApp.class).getContext().getActionMap(InventoryView.class, this);
        jbCancel.setAction(actionMap.get("closeJournal")); // NOI18N
        jbCancel.setText(resourceMap.getString("jbCancel.text")); // NOI18N
        jbCancel.setName("jbCancel"); // NOI18N

        jbAddDoc.setAction(actionMap.get("addDoc")); // NOI18N
        jbAddDoc.setText(resourceMap.getString("jbAddDoc.text")); // NOI18N
        jbAddDoc.setName("jbAddDoc"); // NOI18N

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

        jbJournalOrTrash.setAction(actionMap.get("showJournalOrTrash")); // NOI18N
        jbJournalOrTrash.setText(resourceMap.getString("jbJournalOrTrash.text")); // NOI18N
        jbJournalOrTrash.setName("jbJournalOrTrash"); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 397, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jcbDate)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jpDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jcbPeriod)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 7, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jpStartDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jpEndDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jbAddDoc)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jbDelete)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jbJournalOrTrash)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 10, Short.MAX_VALUE)
                        .addComponent(jbCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jpEndDate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jcbPeriod, 0, 0, Short.MAX_VALUE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jpStartDate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jcbDate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jpDate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(6, 6, 6)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 311, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jbCancel)
                    .addComponent(jbAddDoc, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jbDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jbJournalOrTrash, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void openDoc(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_openDoc
        if (evt.getClickCount() > 1) {
            int r = jtInventory.convertRowIndexToModel(jtInventory.getSelectedRow());
            if (r != -1) {
                InventoryDocView idv =
                        new InventoryDocView(salesView, this, ((Inventory) table.get(r)).getCode(), null);
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
    }//GEN-LAST:event_openDoc

    private void jtInventoryKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtInventoryKeyPressed

        if (evt.getKeyCode() == 155) {
            addDoc();
        }
        if (evt.getKeyCode() == 127) {
            deleteDoc();
        }
    }//GEN-LAST:event_jtInventoryKeyPressed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton jbAddDoc;
    private javax.swing.JButton jbCancel;
    private javax.swing.JButton jbDelete;
    private javax.swing.JButton jbJournalOrTrash;
    private javax.swing.JCheckBox jcbDate;
    private javax.swing.JCheckBox jcbPeriod;
    private javax.swing.JPanel jpDate;
    private javax.swing.JPanel jpEndDate;
    private javax.swing.JPanel jpStartDate;
    private javax.swing.JTable jtInventory;
    // End of variables declaration//GEN-END:variables
    private SalesView salesView;
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
            String delStr = "i.active != 2";
            if (trash) {
                delStr = "i.active = 2";
            }
            if (jcbDate.isSelected()) {
                hql = "from Inventory i"
                        + " where DATE(i.datetime) = '" + sdf.format(date.getDate()) + "'"
                        + " and " + delStr
                        + " order by i.datetime DESC";
            }
            if (jcbPeriod.isSelected()) {
                hql = "from Inventory i"
                        + " where DATE(i.datetime) >= '" + sdf.format(startDate.getDate()) + "' and"
                        + " DATE(i.datetime) <='" + sdf.format(endDate.getDate()) + "'"
                        + " and " + delStr
                        + " order by i.datetime DESC";
            }
            table = session.createQuery(hql).list();

            Vector<String> tableHeaders = new Vector<String>();
            Vector tableData = new Vector();
            for(String[] col : columns) {
                tableHeaders.add(col[0]);
            }

            SimpleDateFormat sdfd = new SimpleDateFormat("dd.MM.yyyy");

            if (table != null) {
                for (Object o : table) {
                    Inventory in = (Inventory) o;
                    Vector<Object> oneRow = new Vector<Object>();
                    oneRow.add(in.getNumber());
                    oneRow.add(sdfd.format(in.getDatetime()));
                    tableData.add(oneRow);
                }
            }
            session.close();

            jtInventory.setModel(new DefaultTableModel(tableData, tableHeaders) {

                @Override
                public boolean isCellEditable(int r, int c) {
                    return false;
                }
            });

        } catch (Exception e) {
            logger.error(e);
        }

        for (int j = 0; j < jtInventory.getColumnCount(); j++) {
            jtInventory.getColumnModel().getColumn(j).setCellRenderer(new MyTableCellRenderer());
        }
        
        Util.autoResizeColWidth(jtInventory);
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

    private void changeDelStatus(int status, int r) {
        try {
            Session session = HUtil.getSession();
            session.beginTransaction();
            Inventory i = (Inventory) HUtil.getElement("Inventory", ((Inventory) table.get(r)).getCode(), session);
            if (i != null) {
                i.setActive(status);
                session.merge(i);
            }
            if(i.getIncomingCode() != null) {
                Incoming in = (Incoming) HUtil.getElement("Incoming", i.getIncomingCode());
                in.setActive(status);
                session.merge(in);
            }
            if(i.getOutcomingCode() != null) {
                Outcoming o = (Outcoming) HUtil.getElement("Outcoming", i.getOutcomingCode());
                o.setActive(status);
                session.merge(o);
            }
            session.getTransaction().commit();
            session.close();
            
            if(i.getIncomingCode() != null) {
                Util.updateJournals(salesView, IncomingView.class);
            }
            if(i.getOutcomingCode() != null) {
                Util.updateJournals(salesView, OutcomingView.class);
            }
        } catch (Exception e) {
            logger.error(e);
        }
        showTable();
    }

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

    @Action
    public void addDoc() {
        InventoryDocView idv = new InventoryDocView(salesView, this, null, null);
        salesView.getJDesktopPane().add(idv, javax.swing.JLayeredPane.DEFAULT_LAYER);
        idv.setVisible(true);
        try {
            idv.setSelected(true);
        } catch (java.beans.PropertyVetoException e) {
        }
        idv.setFocus();
    }

    @Action
    public void deleteDoc() {
        if (!trash) {
            int r = jtInventory.getSelectedRow();
            if (r != -1) {
                int a = JOptionPane.showOptionDialog(
                        this, "Удалить документ № " + ((Inventory) table.get(r)).getNumber() + "?", "Удаление", JOptionPane.YES_NO_CANCEL_OPTION,
                        JOptionPane.QUESTION_MESSAGE, null, new Object[]{
                            "Да", "Нет"}, "Нет");
                if (a == 0) {
                    changeDelStatus(2, r);
                }
            }
        } else {
            int r = jtInventory.getSelectedRow();
            if (r != -1) {
                int a = JOptionPane.showOptionDialog(
                        this, "Восстановить документ № " + ((Inventory) table.get(r)).getNumber() + " из корзины?", "Восстановление", JOptionPane.YES_NO_CANCEL_OPTION,
                        JOptionPane.QUESTION_MESSAGE, null, new Object[]{
                            "Да", "Нет"}, "Нет");
                if (a == 0) {
                    changeDelStatus(0, r);
                }
            }
        }
    }
}
