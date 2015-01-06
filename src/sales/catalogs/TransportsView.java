/*
 * Transports.java
 *
 * Created on 10.02.2012, 15:19:07
 */
package sales.catalogs;

import java.awt.Rectangle;
import java.util.List;
import java.util.Vector;
import javax.swing.JOptionPane;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import org.apache.log4j.Logger;
import org.hibernate.classic.Session;
import org.jdesktop.application.Action;
import sales.SalesApp;
import sales.SalesView;
import sales.entity.Employee;
import sales.entity.Transports;
import sales.interfaces.IClose;
import sales.interfaces.ITransport;
import sales.util.HUtil;
import sales.util.Util;

public class TransportsView extends javax.swing.JInternalFrame implements IClose {

    static Logger logger = Logger.getLogger(SalesApp.class.getName());

    /** Creates new form Transports */
    public TransportsView(SalesView salesView, ITransport parent) {

        initComponents();
        Util.initJIF(this, "Транспорт", parent, salesView);
        Util.initJTable(jtTransports);

        this.salesView = salesView;
        this.parent = parent;

        if (parent == null) {
            jbOK.setVisible(false);
        }

        filter = "";

        showTable();

        Util.setMoveRight(jtTransports);

        trash = false;

    }

    private class MyTableModel extends DefaultTableModel {

        public MyTableModel(Vector table, Vector header) {
            super(table, header);
        }

        @Override
        public boolean isCellEditable(int r, int c) {
            return c > 0;
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

        jbClose = new javax.swing.JButton();
        jbOK = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jtTransports = new javax.swing.JTable();
        jbAdd = new javax.swing.JButton();
        jbDelete = new javax.swing.JButton();
        jbTrash = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jtfFilter = new javax.swing.JTextField();

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(sales.SalesApp.class).getContext().getResourceMap(TransportsView.class);
        setBackground(resourceMap.getColor("Form.background")); // NOI18N
        setClosable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle(resourceMap.getString("Form.title")); // NOI18N
        setName("Form"); // NOI18N

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(sales.SalesApp.class).getContext().getActionMap(TransportsView.class, this);
        jbClose.setAction(actionMap.get("closeCat")); // NOI18N
        jbClose.setText(resourceMap.getString("jbClose.text")); // NOI18N
        jbClose.setName("jbClose"); // NOI18N

        jbOK.setAction(actionMap.get("OK")); // NOI18N
        jbOK.setText(resourceMap.getString("jbOK.text")); // NOI18N
        jbOK.setName("jbOK"); // NOI18N

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        jtTransports.setModel(new javax.swing.table.DefaultTableModel(
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
        jtTransports.setCellSelectionEnabled(true);
        jtTransports.setFillsViewportHeight(true);
        jtTransports.setName("jtTransports"); // NOI18N
        jtTransports.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jtTransportsMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jtTransports);

        jbAdd.setAction(actionMap.get("addItem")); // NOI18N
        jbAdd.setText(resourceMap.getString("jbAdd.text")); // NOI18N
        jbAdd.setName("jbAdd"); // NOI18N

        jbDelete.setAction(actionMap.get("deleteItem")); // NOI18N
        jbDelete.setText(resourceMap.getString("jbDelete.text")); // NOI18N
        jbDelete.setName("jbDelete"); // NOI18N

        jbTrash.setAction(actionMap.get("showTrash")); // NOI18N
        jbTrash.setText(resourceMap.getString("jbTrash.text")); // NOI18N
        jbTrash.setName("jbTrash"); // NOI18N

        jLabel1.setText(resourceMap.getString("jLabel1.text")); // NOI18N
        jLabel1.setName("jLabel1"); // NOI18N

        jtfFilter.setText(resourceMap.getString("jtfFilter.text")); // NOI18N
        jtfFilter.setName("jtfFilter"); // NOI18N
        jtfFilter.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jtfFilterKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 845, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jbAdd)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jbDelete)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jbTrash)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 438, Short.MAX_VALUE)
                        .addComponent(jbOK)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jbClose))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jtfFilter, javax.swing.GroupLayout.DEFAULT_SIZE, 803, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jtfFilter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 343, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jbClose)
                    .addComponent(jbOK)
                    .addComponent(jbAdd)
                    .addComponent(jbDelete)
                    .addComponent(jbTrash))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

private void jtTransportsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtTransportsMouseClicked
    if (parent != null && evt.getClickCount() > 1) {
        OK();
    }
}//GEN-LAST:event_jtTransportsMouseClicked

private void jtfFilterKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtfFilterKeyReleased
    filter = Util.getFilter(jtfFilter.getText(), new String[]{"auto", "trailer", "owner", "driver"});
    showTable();
}//GEN-LAST:event_jtfFilterKeyReleased

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton jbAdd;
    private javax.swing.JButton jbClose;
    private javax.swing.JButton jbDelete;
    private javax.swing.JButton jbOK;
    private javax.swing.JButton jbTrash;
    private javax.swing.JTable jtTransports;
    private javax.swing.JTextField jtfFilter;
    // End of variables declaration//GEN-END:variables
    private SalesView salesView;
    private ITransport parent;
    private List table;
    private String filter;
    private boolean trash;

    private void showTable() {
        Session session = HUtil.getSession();
        String delStr = "x.active = 0";
        if (trash) {
            delStr = "x.active = 2";
        }
        table = HUtil.executeHql("from Transports x where " + delStr + filter + " order by x.code");
        session.close();
        Vector<String> tableHeaders = new Vector<String>();
        Vector tableData = new Vector();
        tableHeaders.add("Код");
        tableHeaders.add("Автомобиль");
        tableHeaders.add("Прицеп");
        tableHeaders.add("Владелец");
        tableHeaders.add("Водитель");

        if (table != null) {
            for (int i = 0; i < table.size(); i++) {
                Transports t = (Transports) table.get(i);
                Vector<Object> oneRow = new Vector<Object>();
                oneRow.add(t.getCode());
                oneRow.add(t.getAuto());
                oneRow.add(t.getTrailer());
                oneRow.add(t.getOwner());
                oneRow.add(t.getDriver());
                tableData.add(oneRow);
            }
        }

        jtTransports.setModel(new MyTableModel(tableData, tableHeaders));
        Util.autoResizeColWidth(jtTransports);
        jtTransports.getColumnModel().getColumn(0).setMinWidth(40);
        jtTransports.getColumnModel().getColumn(0).setMaxWidth(40);

        jtTransports.getModel().addTableModelListener(new TableModelListener() {

            @Override
            public void tableChanged(TableModelEvent e) {

                int r = e.getFirstRow();
                int c = e.getColumn();
                updateTable(r, c);
                showTable();
                Util.moveCell(r, c, jtTransports, true);
            }
        });

    }

    public void updateTable(Integer r, Integer c) {
        try {
            Session session = HUtil.getSession();
            session.beginTransaction();
            Transports t =
                    (Transports) session.get(Transports.class, new Integer(jtTransports.getValueAt(r, 0).toString()));
            if (c == 1) {
                t.setAuto(jtTransports.getValueAt(r, 1).toString());
            } else if (c == 2) {
                t.setTrailer(jtTransports.getValueAt(r, 2).toString());
            } else if (c == 3) {
                t.setOwner(jtTransports.getValueAt(r, 3).toString());
            } else if (c == 4) {
                t.setDriver(jtTransports.getValueAt(r, 4).toString());
            }
            session.update(t);
            session.getTransaction().commit();
            session.close();
        } catch (Exception e) {
            logger.error(e);
        }
    }

    @Action
    public void addItem() {
        try {
            Session session = HUtil.getSession();
            session.beginTransaction();
            Transports t = new Transports();
            t.setAuto("");
            t.setTrailer("");
            t.setOwner("");
            t.setDriver("");
            session.save(t);
            session.getTransaction().commit();
            session.close();
            showTable();
            Rectangle rect = jtTransports.getCellRect(jtTransports.getRowCount() - 1, 1, true);
            jtTransports.scrollRectToVisible(rect);
            jtTransports.getSelectionModel().addSelectionInterval(jtTransports.getRowCount() - 1, jtTransports.getRowCount() - 1);
            jtTransports.getColumnModel().getSelectionModel().setSelectionInterval(1, 1);
            jtTransports.editCellAt(jtTransports.getRowCount() - 1, 1);
            jtTransports.requestFocus();
        } catch (Exception e) {
            logger.error(e);
        }
    }

    @Action
    public void deleteItem() {
        if (!trash) {
            int r = jtTransports.getSelectedRow();
            if (r != -1) {
                int a = JOptionPane.showOptionDialog(
                        this, "Удалить элемент " + ((Employee) table.get(r)).getName() + "?", "Удаление", JOptionPane.YES_NO_CANCEL_OPTION,
                        JOptionPane.QUESTION_MESSAGE, null, new Object[]{
                            "Да", "Нет"}, "Нет");
                if (a == 0) {
                    changeDelStatus(2, r);
                }
            }
        } else {
            int r = jtTransports.getSelectedRow();
            if (r != -1) {
                int a = JOptionPane.showOptionDialog(
                        this, "Восстановить элемент " + ((Employee) table.get(r)).getName() + " из корзины?", "Восстановление", JOptionPane.YES_NO_CANCEL_OPTION,
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
        TableCellEditor tce = jtTransports.getCellEditor();
        if (tce != null) {
            tce.stopCellEditing();
        }
        if (parent != null && jtTransports.getSelectedRow() != -1) {
            parent.setTransport(
                    ((Transports) table.get(jtTransports.getSelectedRow())).getCode());
        }
        Util.closeJIF(this, parent, salesView);
        Util.closeJIFTab(this, salesView);
    }

    public void close() {
    }

    @Action
    public void closeCat() {
        Util.closeJIF(this, parent, salesView);
        Util.closeJIFTab(this, salesView);
    }

    private void changeDelStatus(int status, int r) {
        try {
            Session session = HUtil.getSession();
            session.beginTransaction();
            Transports t = (Transports) HUtil.getElement("Transports", ((Transports) table.get(r)).getCode(), session);
            if (t != null) {
                t.setActive(status);
                session.save(t);
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
}
