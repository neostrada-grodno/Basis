
/*
 * RepricingView.java
 *
 * Created on 07.05.2011, 14:59:59
 */
package sales.repricing;

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
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import org.apache.log4j.Logger;
import org.hibernate.classic.Session;
import org.jdesktop.application.Action;
import sales.SalesApp;
import sales.SalesView;
import sales.auxiliarly.SalesTableRowSorter;
import sales.entity.Repricing;
import sales.interfaces.IClose;
import sales.interfaces.IJournal;
import sales.util.HUtil;
import sales.util.Util;

public class RepricingView extends javax.swing.JInternalFrame implements IClose, IJournal {

    static Logger logger = Logger.getLogger(SalesApp.class.getName());

    public RepricingView(SalesView salesView) {
        initComponents();

        Util.initJIF(this, "Журнал актов переоценки", null, salesView);
        Util.initJTable(jtRepricing);

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
        gl = (GroupLayout) jpStartDate.getLayout();
        gl.setHorizontalGroup(gl.createParallelGroup().addGroup(gl.createSequentialGroup().addComponent(startDate)));
        gl.setVerticalGroup(gl.createParallelGroup().addGroup(gl.createSequentialGroup().addComponent(startDate)));

        gl = (GroupLayout) jpEndDate.getLayout();
        gl.setHorizontalGroup(gl.createParallelGroup().addGroup(gl.createSequentialGroup().addComponent(endDate)));
        gl.setVerticalGroup(gl.createParallelGroup().addGroup(gl.createSequentialGroup().addComponent(endDate)));

        jcbDate.setSelected(false);
        jcbPeriod.setSelected(true);

        jcbActiveOnly.setSelected(true);
        jcbNotActiveOnly.setSelected(true);

        initColumnsArray();
        showTable();

        if (jtRepricing.getRowCount() > 0) {
            jtRepricing.getSelectionModel().setSelectionInterval(0, 0);
        }

    }

    private void initColumnsArray() {
        columns = new ArrayList<>();
        columns.add(new String[]{"Проведен", "string"});
        columns.add(new String[]{"Номер", "string"});
        columns.add(new String[]{"Дата", "date"});
    }

    public class MyTableCellRenderer extends DefaultTableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(JTable tbl,
                Object obj, boolean isSelected, boolean hasFocus, int row, int column) {
            Component cell = super.getTableCellRendererComponent(
                    tbl, obj, isSelected, hasFocus, row, column);
            if (((Repricing) table.get(row)).getActive() == 1) {
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
        jtRepricing = new javax.swing.JTable();
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
        jcbActiveOnly = new javax.swing.JCheckBox();
        jcbNotActiveOnly = new javax.swing.JCheckBox();

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(sales.SalesApp.class).getContext().getResourceMap(RepricingView.class);
        setBackground(resourceMap.getColor("Form.background")); // NOI18N
        setClosable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle(resourceMap.getString("Form.title")); // NOI18N
        setName("Form"); // NOI18N

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        jtRepricing.setModel(new javax.swing.table.DefaultTableModel(
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
        jtRepricing.setFillsViewportHeight(true);
        jtRepricing.setName("jtRepricing"); // NOI18N
        jtRepricing.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                openDoc(evt);
            }
        });
        jtRepricing.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jtRepricingKeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(jtRepricing);

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(sales.SalesApp.class).getContext().getActionMap(RepricingView.class, this);
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

        jcbActiveOnly.setAction(actionMap.get("showActiveOnly")); // NOI18N
        jcbActiveOnly.setBackground(resourceMap.getColor("jcbActiveOnly.background")); // NOI18N
        jcbActiveOnly.setText(resourceMap.getString("jcbActiveOnly.text")); // NOI18N
        jcbActiveOnly.setName("jcbActiveOnly"); // NOI18N

        jcbNotActiveOnly.setAction(actionMap.get("showNotActiveOnly")); // NOI18N
        jcbNotActiveOnly.setBackground(resourceMap.getColor("jcbNotActiveOnly.background")); // NOI18N
        jcbNotActiveOnly.setText(resourceMap.getString("jcbNotActiveOnly.text")); // NOI18N
        jcbNotActiveOnly.setName("jcbNotActiveOnly"); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 673, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jbAddDoc)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jbDelete)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jbTrash)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 297, Short.MAX_VALUE)
                        .addComponent(jbCancel))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jcbActiveOnly)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jcbNotActiveOnly)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 22, Short.MAX_VALUE)
                        .addComponent(jcbDate)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jpDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(33, 33, 33)
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
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jcbPeriod, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jcbDate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jpStartDate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jpDate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jpEndDate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jcbActiveOnly)
                        .addComponent(jcbNotActiveOnly)))
                .addGap(4, 4, 4)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 319, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jbCancel)
                    .addComponent(jbAddDoc)
                    .addComponent(jbDelete)
                    .addComponent(jbTrash))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void openDoc(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_openDoc
        if (evt.getClickCount() > 1) {
            int r = jtRepricing.convertRowIndexToModel(jtRepricing.getSelectedRow());
            if (r != -1) {
                JInternalFrame rdv =
                        new RepricingDocView(salesView, this, ((Repricing) table.get(r)).getCode(), null);
                salesView.getJDesktopPane().add(rdv, javax.swing.JLayeredPane.DEFAULT_LAYER);
                rdv.setVisible(true);
                try {
                    rdv.setSelected(true);
                } catch (Exception e) {
                    logger.error(e);
                }
            }
        }
    }//GEN-LAST:event_openDoc

    private void jtRepricingKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtRepricingKeyPressed

        if (evt.getKeyCode() == 155) {
            addDoc();
        }
        if (evt.getKeyCode() == 127) {
            deleteDoc();
        }
    }//GEN-LAST:event_jtRepricingKeyPressed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton jbAddDoc;
    private javax.swing.JButton jbCancel;
    private javax.swing.JButton jbDelete;
    private javax.swing.JButton jbTrash;
    private javax.swing.JCheckBox jcbActiveOnly;
    private javax.swing.JCheckBox jcbDate;
    private javax.swing.JCheckBox jcbNotActiveOnly;
    private javax.swing.JCheckBox jcbPeriod;
    private javax.swing.JPanel jpDate;
    private javax.swing.JPanel jpEndDate;
    private javax.swing.JPanel jpStartDate;
    private javax.swing.JTable jtRepricing;
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
            String delStr = "rep.active != 2";
            if (trash) {
                delStr = "rep.active = 2";
            }
            if (jcbActiveOnly.isSelected() && !jcbNotActiveOnly.isSelected()) {
                delStr += " and rep.active = 1";
            } else if (jcbNotActiveOnly.isSelected() && !jcbActiveOnly.isSelected()) {
                delStr += " and rep.active = 0";
            }
            if (jcbDate.isSelected()) {
                hql = "from Repricing rep where "
                        + "DATE(rep.datetime) = '" + sdf.format(date.getDate()) + "'"
                        + " and " + delStr
                        + " order by rep.datetime DESC";
            }
            if (jcbPeriod.isSelected()) {
                hql = "from Repricing rep where "
                        + "rep.datetime >= '" + sdf.format(startDate.getDate()) + "' and "
                        + "rep.datetime < '" + sdf.format(endDate.getDate()) + "'"
                        + " and " + delStr
                        + " order by rep.datetime DESC";
            }
            session.beginTransaction();
            table = session.createQuery(hql).list();
            session.getTransaction().commit();
            session.close();
        } catch (Exception e) {
            logger.error(e);
        }

        Vector<String> tableHeaders = new Vector<String>();
        for (String[] col : columns) {
            tableHeaders.add(col[0]);
        }
        Vector tableData = new Vector();

        SimpleDateFormat sdfd = new SimpleDateFormat("dd.MM.yyyy");

        if (table != null) {
            for (Object o : table) {
                Repricing rep = (Repricing) o;
                Vector<Object> oneRow = new Vector<Object>();
                if (rep.getActive() == 1) {
                    oneRow.add("V");
                } else {
                    oneRow.add("");
                }
                oneRow.add(rep.getNumber());
                oneRow.add(sdfd.format(rep.getDatetime()));
                tableData.add(oneRow);
            }
        }

        jtRepricing.setModel(new DefaultTableModel(tableData, tableHeaders) {

            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        });

        for (int j = 0; j < jtRepricing.getColumnCount(); j++) {
            jtRepricing.getColumnModel().getColumn(j).setCellRenderer(new MyTableCellRenderer());
        }

        Util.autoResizeColWidth(jtRepricing);
        jtRepricing.getColumnModel().getColumn(0).setMinWidth(70);
        jtRepricing.getColumnModel().getColumn(0).setMaxWidth(70);
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
        /*if (jtRepricing.getSelectedRow() != -1) {
        int a = JOptionPane.showOptionDialog(
        this, "Удалить? Восстановление будет невозможно! Снимите \"Проведен\" для того чтобы документ не использовался.", "Удаление", JOptionPane.YES_NO_CANCEL_OPTION,
        JOptionPane.QUESTION_MESSAGE, null, new Object[]{"Да", "Нет"}, "Нет");
        if (a == 1) {
        return;
        }
        try {
        Session session = HUtil.getSession();
        session.beginTransaction();
        Repricing r = (Repricing) session.load(Incoming.class,
        ((Repricing) table.get(jtRepricing.getSelectedRow())).getCode());
        String hql = "from Repricingtable ot where ot.documentCode = " + r.getCode();
        List rt = session.createQuery(hql).list();
        for (int j = 0; j < rt.size(); j++) {
        session.delete((Repricingtable) rt.get(j));
        }
        session.delete(r);
        session.getTransaction().commit();
        session.close();
        } catch (Exception e) {
        logger.error(e);
        }
        showTable();
        }*/
        if (!trash) {
            int r = jtRepricing.getSelectedRow();
            if (r != -1) {
                int a = JOptionPane.showOptionDialog(
                        this, "Удалить документ № " + ((Repricing) table.get(r)).getNumber() + "?", "Удаление", JOptionPane.YES_NO_CANCEL_OPTION,
                        JOptionPane.QUESTION_MESSAGE, null, new Object[]{
                            "Да", "Нет"}, "Нет");
                if (a == 0) {
                    changeDelStatus(2, r);
                }
            }
        } else {
            int r = jtRepricing.getSelectedRow();
            if (r != -1) {
                int a = JOptionPane.showOptionDialog(
                        this, "Восстановить документ № " + ((Repricing) table.get(r)).getNumber() + " из корзины?", "Восстановление", JOptionPane.YES_NO_CANCEL_OPTION,
                        JOptionPane.QUESTION_MESSAGE, null, new Object[]{
                            "Да", "Нет"}, "Нет");
                if (a == 0) {
                    changeDelStatus(0, r);
                }
            }
        }

    }

    @Action
    public void addDoc() {
        RepricingDocView rdv = new RepricingDocView(salesView, this, null, null);
        salesView.getJDesktopPane().add(rdv, javax.swing.JLayeredPane.DEFAULT_LAYER);
        rdv.setVisible(true);
        try {
            rdv.setSelected(true);
        } catch (java.beans.PropertyVetoException e) {
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
            Repricing doc = (Repricing) HUtil.getElement("Repricing", ((Repricing) table.get(r)).getCode(), session);
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
    public void showActiveOnly() {
        if (!jcbActiveOnly.isSelected() && !jcbNotActiveOnly.isSelected()) {
            jcbActiveOnly.setSelected(true);
        }
        showTable();
    }

    @Action
    public void showNotActiveOnly() {
        if (!jcbActiveOnly.isSelected() && !jcbNotActiveOnly.isSelected()) {
            jcbNotActiveOnly.setSelected(true);
        }
        showTable();
    }
}