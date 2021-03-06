/*
 * jifNomenclature.java
 *
 * Created on 06.05.2011, 12:51:06
 */
package sales.catalogs;

import sales.interfaces.IEmployee;
import java.awt.Component;
import java.awt.Rectangle;
import java.util.List;
import java.util.Vector;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.text.JTextComponent;
import org.apache.log4j.Logger;
import org.hibernate.classic.Session;
import org.jdesktop.application.Action;
import sales.SalesApp;
import sales.SalesView;
import sales.entity.Employee;
import sales.interfaces.IClose;
import sales.util.HUtil;
import sales.util.Util;

public class EmployeeView extends javax.swing.JInternalFrame implements IClose {

    static Logger logger = Logger.getLogger(SalesApp.class.getName());

    /** Creates new form jifNomenclature */
    public EmployeeView(SalesView salesView, IEmployee parent, String name) {

        initComponents();

        Util.initJIF(this, "Сотрудники", parent, salesView);
        Util.initJTable(jtEmployee);

        this.salesView = salesView;
        this.parent = parent;
        this.name = name;

        if (parent == null) {
            jbOK.setVisible(false);
        }

        filter = "";

        showTable();

        Util.setMoveRight(jtEmployee);

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

        jspNomenclature = new javax.swing.JScrollPane();
        jtEmployee = new javax.swing.JTable() {

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
        jbClose = new javax.swing.JButton();
        jbTrash = new javax.swing.JButton();

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(sales.SalesApp.class).getContext().getResourceMap(EmployeeView.class);
        setBackground(resourceMap.getColor("Form.background")); // NOI18N
        setClosable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle(resourceMap.getString("Form.title")); // NOI18N
        setName("Form"); // NOI18N

        jspNomenclature.setName("jspNomenclature"); // NOI18N

        jtEmployee.setModel(new javax.swing.table.DefaultTableModel(
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
        jtEmployee.setCellSelectionEnabled(true);
        jtEmployee.setFillsViewportHeight(true);
        jtEmployee.setName("jtEmployee"); // NOI18N
        jtEmployee.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jtEmployeeMouseClicked(evt);
            }
        });
        jspNomenclature.setViewportView(jtEmployee);

        jLabel1.setText(resourceMap.getString("jLabel1.text")); // NOI18N
        jLabel1.setName("jLabel1"); // NOI18N

        jtfFilter.setText(resourceMap.getString("jtfFilter.text")); // NOI18N
        jtfFilter.setName("jtfFilter"); // NOI18N
        jtfFilter.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jtfFilterKeyReleased(evt);
            }
        });

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(sales.SalesApp.class).getContext().getActionMap(EmployeeView.class, this);
        jbAddItem.setAction(actionMap.get("addItem")); // NOI18N
        jbAddItem.setText(resourceMap.getString("jbAddItem.text")); // NOI18N
        jbAddItem.setName("jbAddItem"); // NOI18N

        jbDelete.setAction(actionMap.get("deleteItem")); // NOI18N
        jbDelete.setText(resourceMap.getString("jbDelete.text")); // NOI18N
        jbDelete.setName("jbDelete"); // NOI18N

        jbOK.setAction(actionMap.get("OK")); // NOI18N
        jbOK.setText(resourceMap.getString("jbOK.text")); // NOI18N
        jbOK.setName("jbOK"); // NOI18N

        jbClose.setAction(actionMap.get("closeCat")); // NOI18N
        jbClose.setText(resourceMap.getString("jbClose.text")); // NOI18N
        jbClose.setName("jbClose"); // NOI18N

        jbTrash.setAction(actionMap.get("showTrash")); // NOI18N
        jbTrash.setText(resourceMap.getString("jbTrash.text")); // NOI18N
        jbTrash.setName("jbTrash"); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jspNomenclature, javax.swing.GroupLayout.DEFAULT_SIZE, 708, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap(19, Short.MAX_VALUE)
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jtfFilter, javax.swing.GroupLayout.DEFAULT_SIZE, 657, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jbAddItem)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jbDelete)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jbTrash)
                        .addGap(293, 293, 293)
                        .addComponent(jbOK, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jbClose, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jtfFilter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jspNomenclature, javax.swing.GroupLayout.DEFAULT_SIZE, 364, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jbAddItem)
                    .addComponent(jbDelete)
                    .addComponent(jbClose)
                    .addComponent(jbOK)
                    .addComponent(jbTrash))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jtEmployeeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtEmployeeMouseClicked
        if (parent != null && evt.getClickCount() > 1) {
            OK();
        }
    }//GEN-LAST:event_jtEmployeeMouseClicked

    private void jtfFilterKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtfFilterKeyReleased
        filter = Util.getFilter(jtfFilter.getText(), new String[]{"name", "passportNumber", "passportDatePlace"});
        showTable();
    }//GEN-LAST:event_jtfFilterKeyReleased
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JButton jbAddItem;
    private javax.swing.JButton jbClose;
    private javax.swing.JButton jbDelete;
    private javax.swing.JButton jbOK;
    private javax.swing.JButton jbTrash;
    private javax.swing.JScrollPane jspNomenclature;
    private javax.swing.JTable jtEmployee;
    private javax.swing.JTextField jtfFilter;
    // End of variables declaration//GEN-END:variables
    private SalesView salesView;
    private IEmployee parent;
    private String name;
    private List table;
    private String filter;
    private boolean trash;

    private void showTable() {
        Session session = HUtil.getSession();
        String delStr = "x.active = 0";
        if (trash) {
            delStr = "x.active = 2";
        }
        table = HUtil.executeHql("from Employee x where " + delStr + filter + " order by x.code");
        session.close();
        Vector<String> tableHeaders = new Vector<String>();
        Vector tableData = new Vector();
        tableHeaders.add("Код");
        tableHeaders.add("Имя");
        tableHeaders.add("Фамилия И.О.");
        tableHeaders.add("№ пасспорта");
        tableHeaders.add("Дата и место выдачи");

        if (table != null) {
            for (int i = 0; i < table.size(); i++) {
                Employee e = (Employee) table.get(i);
                Vector<Object> oneRow = new Vector<Object>();
                oneRow.add(e.getCode());
                oneRow.add(e.getName());
                oneRow.add(e.getShortName());
                oneRow.add(e.getPassportNumber());
                oneRow.add(e.getPassportDatePlace());
                tableData.add(oneRow);
            }
        }

        jtEmployee.setModel(new MyTableModel(tableData, tableHeaders));
        Util.autoResizeColWidth(jtEmployee);
        jtEmployee.getColumnModel().getColumn(0).setMinWidth(40);
        jtEmployee.getColumnModel().getColumn(0).setMaxWidth(40);

        jtEmployee.getModel().addTableModelListener(new TableModelListener() {

            @Override
            public void tableChanged(TableModelEvent e) {
                int r = e.getFirstRow();
                int c = e.getColumn();
                updateTable(r, c);
                showTable();
                Util.moveCell(r, c, jtEmployee, true);
            }
        });

    }

    public void updateTable(Integer r, Integer c) {
        try {
            Session session = HUtil.getSession();
            session.beginTransaction();
            Employee em =
                    (Employee) session.get(Employee.class, new Integer(jtEmployee.getValueAt(r, 0).toString()));
            if (c == 1) {
                em.setName(jtEmployee.getValueAt(r, 1).toString());
            } else
            if (c == 2) {
                em.setShortName(jtEmployee.getValueAt(r, 2).toString());
            } else
            if (c == 3) {
                em.setPassportNumber(jtEmployee.getValueAt(r, 3).toString());
            } else
            if (c == 4) {
                em.setPassportDatePlace(jtEmployee.getValueAt(r, 4).toString());
            }
            session.update(em);
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
            Employee e = new Employee();
            e.setName("");
            e.setOccupation("");
            e.setPassportDatePlace("");
            e.setPassportNumber("");
            e.setShortName("");
            session.save(e);
            session.getTransaction().commit();
            session.close();
            showTable();
            Rectangle rect = jtEmployee.getCellRect(jtEmployee.getRowCount() - 1, 1, true);
            jtEmployee.scrollRectToVisible(rect);
            jtEmployee.getSelectionModel().addSelectionInterval(jtEmployee.getRowCount() - 1, jtEmployee.getRowCount() - 1);
            jtEmployee.getColumnModel().getSelectionModel().setSelectionInterval(1, 1);
            jtEmployee.editCellAt(jtEmployee.getRowCount() - 1, 1);
            jtEmployee.requestFocus();
        } catch (Exception e) {
            logger.error(e);
        }
    }

    @Action
    public void deleteItem() {

        if (!trash) {
            int r = jtEmployee.getSelectedRow();
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
            int r = jtEmployee.getSelectedRow();
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
        TableCellEditor tce = jtEmployee.getCellEditor();
        if (tce != null) {
            tce.stopCellEditing();
        }
        if (parent != null && jtEmployee.getSelectedRow() != -1) {
            parent.setEmployee(
                    ((Employee) table.get(jtEmployee.getSelectedRow())).getCode(), name);
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
            Employee el = (Employee) HUtil.getElement("Employee", ((Employee) table.get(r)).getCode(), session);
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