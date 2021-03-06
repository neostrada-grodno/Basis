/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * ShipmentsView.java
 *
 * Created on 19.05.2011, 11:11:06
 */
package sales.outcoming;

import java.util.ArrayList;
import java.util.Vector;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import org.apache.log4j.Logger;
import org.jdesktop.application.Action;
import sales.SalesApp;
import sales.interfaces.ISerialsView;
import sales.SalesView;
import sales.entity.Serialtable;
import sales.interfaces.IClose;
import sales.util.Util;

public class SerialsView extends javax.swing.JInternalFrame implements IClose {

    static Logger logger = Logger.getLogger(SalesApp.class.getName());

    /** Creates new form ShipmentsView */
    public SerialsView(SalesView salesView, ISerialsView parent, int productCode, ArrayList serials, Integer lock, int row) {
        initComponents();

        Util.initJIF(this, "Серийные номера", parent, salesView);
        Util.initJTable(jtSerials);

        this.parent = parent;
        this.salesView = salesView;
        this.productCode = productCode;
        this.row = row;
        this.table = serials;
        this.lock = lock;

        setLocation((salesView.getFrame().getWidth() - getWidth()) / 2,
                (salesView.getFrame().getHeight() - getHeight()) / 2);

        showTable();

        scanEnter = false;

    }

    private class MyTableModel extends DefaultTableModel {

        public MyTableModel(Vector table, Vector header) {
            super(table, header);
        }

        @Override
        public boolean isCellEditable(int r, int c) {
            return c > 0 && lock == 0;
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
        jtSerials = new javax.swing.JTable();
        jbOK = new javax.swing.JButton();
        jbCancel = new javax.swing.JButton();
        jbAdd = new javax.swing.JButton();
        jbDelete = new javax.swing.JButton();

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(sales.SalesApp.class).getContext().getResourceMap(SerialsView.class);
        setBackground(resourceMap.getColor("Form.background")); // NOI18N
        setClosable(true);
        setTitle(resourceMap.getString("Form.title")); // NOI18N
        setName("Form"); // NOI18N

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        jtSerials.setModel(new javax.swing.table.DefaultTableModel(
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
        jtSerials.setCellSelectionEnabled(true);
        jtSerials.setFillsViewportHeight(true);
        jtSerials.setName("jtSerials"); // NOI18N
        jtSerials.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jtSerialsMouseClicked(evt);
            }
        });
        jtSerials.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jtSerialsKeyTyped(evt);
            }
        });
        jScrollPane1.setViewportView(jtSerials);

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(sales.SalesApp.class).getContext().getActionMap(SerialsView.class, this);
        jbOK.setAction(actionMap.get("OK")); // NOI18N
        jbOK.setText(resourceMap.getString("jbOK.text")); // NOI18N
        jbOK.setName("jbOK"); // NOI18N

        jbCancel.setAction(actionMap.get("cancel")); // NOI18N
        jbCancel.setText(resourceMap.getString("jbCancel.text")); // NOI18N
        jbCancel.setName("jbCancel"); // NOI18N

        jbAdd.setAction(actionMap.get("addSerial")); // NOI18N
        jbAdd.setText(resourceMap.getString("jbAdd.text")); // NOI18N
        jbAdd.setName("jbAdd"); // NOI18N

        jbDelete.setAction(actionMap.get("deleteSerial")); // NOI18N
        jbDelete.setText(resourceMap.getString("jbDelete.text")); // NOI18N
        jbDelete.setName("jbDelete"); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jbAdd)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jbDelete)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jbOK, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jbCancel))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 385, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 354, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jbOK)
                    .addComponent(jbCancel)
                    .addComponent(jbAdd)
                    .addComponent(jbDelete))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jtSerialsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtSerialsMouseClicked
    }//GEN-LAST:event_jtSerialsMouseClicked

    private void jtSerialsKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtSerialsKeyTyped
        if ((evt.getKeyChar() < '0' || evt.getKeyChar() > '9') && scanEnter) {
            scanEnter = false;
            Serialtable st = new Serialtable();
            st.setProductCode(productCode);
            st.setSerial(scanCode);
            table.add(st);
            showTable();
        }
        if (scanEnter) {
            scanCode += evt.getKeyChar();
        }
        if (evt.getKeyChar() == '!') {
            scanEnter = true;
            scanCode = "";
        }
    }//GEN-LAST:event_jtSerialsKeyTyped
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton jbAdd;
    private javax.swing.JButton jbCancel;
    private javax.swing.JButton jbDelete;
    private javax.swing.JButton jbOK;
    private javax.swing.JTable jtSerials;
    // End of variables declaration//GEN-END:variables
    private ISerialsView parent;
    private SalesView salesView;
    private int productCode;
    private int row;
    private boolean scanEnter;
    private String scanCode;
    private ArrayList table;
    private Integer lock;

    private void showTable() {
        Vector<String> tableHeaders = new Vector<String>();
        Vector tableData = new Vector();
        tableHeaders.add("№");
        tableHeaders.add("Серийный номер");

        if (table.size() > 0) {
            for (int j = 0; j < table.size(); j++) {
                Vector<Object> oneRow = new Vector<Object>();
                oneRow.add(j + 1);
                oneRow.add(((Serialtable) table.get(j)).getSerial());
                tableData.add(oneRow);
            }
        }

        jtSerials.setModel(new MyTableModel(tableData, tableHeaders));
        Util.autoResizeColWidth(jtSerials);

        jtSerials.getModel().addTableModelListener(new TableModelListener() {

            @Override
            public void tableChanged(TableModelEvent e) {
                if (e.getType() == javax.swing.event.TableModelEvent.UPDATE) {
                    int c = e.getColumn();
                    int r = e.getFirstRow();
                    if (c == 1) {
                        Serialtable st = (Serialtable) table.get(r);
                        st.setSerial(jtSerials.getValueAt(r, 1).toString());
                        table.set(r, st);
                    }
                }
            }
        });
    }

    @Action
    public void addSerial() {
        if (lock == 0) {
            TableCellEditor tce = jtSerials.getCellEditor();
            if (tce != null) {
                tce.stopCellEditing();
            }
            Serialtable st = new Serialtable();
            st.setProductCode(productCode);
            st.setPosition(row);
            table.add(st);
            showTable();
            jtSerials.requestFocusInWindow();
            jtSerials.editCellAt(jtSerials.getRowCount() - 1, 1);
        }
    }

    @Action
    public void deleteSerial() {
        int r = jtSerials.getSelectedRow();
        if (lock == 0 && r != -1) {
            table.remove(r);
            showTable();
        }
    }

    @Action
    public void OK() {
        if (lock == 0) {
            TableCellEditor tce = jtSerials.getCellEditor();
            if (tce != null) {
                tce.stopCellEditing();
            }
            parent.setSerials(row, table);
            showTable();
        }
        Util.closeJIF(this, parent, salesView);
        Util.closeJIFTab(this, salesView);
    }

    public void close() {
    }

    @Action
    public void cancel() {
        Util.closeJIF(this, parent, salesView);
        Util.closeJIFTab(this, salesView);
    }
}
