
/*
 * JournalView.java
 *
 * Created on 07.05.2011, 14:59:59
 */
package sales.register;

import com.toedter.calendar.JDateChooser;
import java.awt.Component;
import java.awt.Font;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Vector;
import javax.swing.GroupLayout;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import org.apache.log4j.Logger;
import org.hibernate.classic.Session;
import org.jdesktop.application.Action;
import sales.interfaces.IInit;
import sales.SalesApp;
import sales.SalesView;
import sales.entity.Employee;
import sales.entity.Register;
import sales.interfaces.IClose;
import sales.interfaces.IJournal;
import sales.util.Conn;
import sales.util.HUtil;
import sales.util.Init;
import sales.util.Util;

public class RegisterView extends javax.swing.JInternalFrame implements IClose, IJournal, IInit {
    
    static Logger logger = Logger.getLogger(SalesApp.class.getName());
    private static final Connection conn = Conn.getConn();
    private static final DecimalFormat df = new DecimalFormat("#,###,###");

    //creates the form
    public RegisterView(SalesView salesView) {
        initComponents();
        
        Util.initJIF(this, "Журнал кассовых ордеров", null, salesView);
        
        this.salesView = salesView;
        
        Calendar c = Calendar.getInstance();
        docFilter = "";

        //set the current date to show docs
        date = new JDateChooser(c.getTime());
        date.getDateEditor().addPropertyChangeListener(
                new PropertyChangeListener() {
                    
                    @Override
                    public void propertyChange(PropertyChangeEvent e) {
                        if ("date".equals(e.getPropertyName())) {
                            checkDate();
                            showTable();
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
                            showTable();
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
                            showTable();
                        }
                    }
                });
        gl = (GroupLayout) jpEndDate.getLayout();
        gl.setHorizontalGroup(gl.createParallelGroup().addGroup(gl.createSequentialGroup().addComponent(endDate)));
        gl.setVerticalGroup(gl.createParallelGroup().addGroup(gl.createSequentialGroup().addComponent(endDate)));

        initColumnsArray();
        checkPeriod();
        
        if (table != null && !table.isEmpty()) {
            cashierDate = new JDateChooser(((Register) table.get(0)).getDatetime());
        } else {
            cashierDate = new JDateChooser(c.getTime());
        }
        cashierDate.getDateEditor().addPropertyChangeListener(
                new PropertyChangeListener() {
                    
                    @Override
                    public void propertyChange(PropertyChangeEvent e) {
                        if ("date".equals(e.getPropertyName())) {
                            jtfPage.setText(HUtil.getPageNumber(cashierDate.getDate()));
                        }
                    }
                });
        gl = (GroupLayout) jpCashierDate.getLayout();
        gl.setHorizontalGroup(gl.createParallelGroup().addGroup(gl.createSequentialGroup().addComponent(cashierDate)));
        gl.setVerticalGroup(gl.createParallelGroup().addGroup(gl.createSequentialGroup().addComponent(cashierDate)));
        
        if (jtRegister.getRowCount() > 0) {
            jtRegister.getSelectionModel().setSelectionInterval(0, 0);
        }
        
        jtRegister.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            
            @Override
            public void valueChanged(ListSelectionEvent e) {
                int r = jtRegister.getSelectedRow();
                if (r != -1) {
                    cashierDate.setDate(((Register) table.get(r)).getDatetime());
                }
            }
        });
        
        jtfPage.setText(HUtil.getPageNumber(cashierDate.getDate()));
        
        jtpRegister.addTab("Приход", null);
        jtpRegister.addTab("Расход", null);
        jtpRegister.addChangeListener(new ChangeListener() {
            
            @Override
            public void stateChanged(ChangeEvent e) {
                tabSelected();
                showTable();
            }
        });
        
        trash = false;
        
        jcbActiveOnly.setSelected(true);
        jcbNotActiveOnly.setSelected(true);
        
        showTable();
        
        status = false;
        
    }
    
    private void initColumnsArray() {
        columns = new ArrayList<>();
        columns.add(new String[] {"Проведен", "string"});
        columns.add(new String[] {"Номер", "string"});
        columns.add(new String[] {"Тип", "string"});
        columns.add(new String[] {"Дата", "date"});
        columns.add(new String[] {"Сотрудник", "string"});
        columns.add(new String[] {"Сумма", "int"});
   }
    
    public class MyTableCellRenderer extends DefaultTableCellRenderer {
        
        @Override
        public Component getTableCellRendererComponent(JTable tbl,
                Object obj, boolean isSelected, boolean hasFocus, int row, int column) {
            Component cell = super.getTableCellRendererComponent(
                    tbl, obj, isSelected, hasFocus, row, column);
            if (table != null) {
                if (((Register) table.get(row)).getActive() == 1) {
                    cell.setFont(new Font(this.getFont().getName(), Font.ITALIC, this.getFont().getSize()));
                }
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
        jbPrintRegister = new javax.swing.JButton();
        jtfPage = new javax.swing.JTextField();
        jbTrash = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jpCashierDate = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jbUpdateDate = new javax.swing.JButton();
        jtpRegister = new javax.swing.JTabbedPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        jtRegister = new javax.swing.JTable();
        jcbActiveOnly = new javax.swing.JCheckBox();
        jcbNotActiveOnly = new javax.swing.JCheckBox();
        jlBalance = new javax.swing.JLabel();

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance().getContext().getResourceMap(RegisterView.class);
        setBackground(resourceMap.getColor("Form.background")); // NOI18N
        setClosable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle(resourceMap.getString("Form.title")); // NOI18N
        setName("Form"); // NOI18N

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance().getContext().getActionMap(RegisterView.class, this);
        jbCancel.setAction(actionMap.get("closeJournal")); // NOI18N
        jbCancel.setText(resourceMap.getString("jbCancel.text")); // NOI18N
        jbCancel.setName("jbCancel"); // NOI18N

        jbAddDoc.setAction(actionMap.get("addDoc")); // NOI18N
        jbAddDoc.setText(resourceMap.getString("jbAddDoc.text")); // NOI18N
        jbAddDoc.setName("jbAddDoc"); // NOI18N

        jLabel1.setText(resourceMap.getString("jLabel1.text")); // NOI18N
        jLabel1.setName("jLabel1"); // NOI18N

        jpStartDate.setBackground(resourceMap.getColor("Form.background")); // NOI18N
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

        jLabel2.setText(resourceMap.getString("jLabel2.text")); // NOI18N
        jLabel2.setName("jLabel2"); // NOI18N

        jpEndDate.setBackground(resourceMap.getColor("Form.background")); // NOI18N
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

        jpDate.setBackground(resourceMap.getColor("Form.background")); // NOI18N
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
        jcbDate.setBackground(resourceMap.getColor("Form.background")); // NOI18N
        jcbDate.setText(resourceMap.getString("jcbDate.text")); // NOI18N
        jcbDate.setName("jcbDate"); // NOI18N

        jcbPeriod.setAction(actionMap.get("checkPeriod")); // NOI18N
        jcbPeriod.setBackground(resourceMap.getColor("Form.background")); // NOI18N
        jcbPeriod.setText(resourceMap.getString("jcbPeriod.text")); // NOI18N
        jcbPeriod.setName("jcbPeriod"); // NOI18N

        jbDelete.setAction(actionMap.get("deleteDoc")); // NOI18N
        jbDelete.setText(resourceMap.getString("jbDelete.text")); // NOI18N
        jbDelete.setName("jbDelete"); // NOI18N

        jbPrintRegister.setAction(actionMap.get("printRegister")); // NOI18N
        jbPrintRegister.setText(resourceMap.getString("jbPrintRegister.text")); // NOI18N
        jbPrintRegister.setName("jbPrintRegister"); // NOI18N

        jtfPage.setText(resourceMap.getString("jtfPage.text")); // NOI18N
        jtfPage.setName("jtfPage"); // NOI18N

        jbTrash.setAction(actionMap.get("showTrash")); // NOI18N
        jbTrash.setText(resourceMap.getString("jbTrash.text")); // NOI18N
        jbTrash.setName("jbTrash"); // NOI18N

        jLabel4.setText(resourceMap.getString("jLabel4.text")); // NOI18N
        jLabel4.setName("jLabel4"); // NOI18N

        jpCashierDate.setBackground(resourceMap.getColor("Form.background")); // NOI18N
        jpCashierDate.setName("jpCashierDate"); // NOI18N

        javax.swing.GroupLayout jpCashierDateLayout = new javax.swing.GroupLayout(jpCashierDate);
        jpCashierDate.setLayout(jpCashierDateLayout);
        jpCashierDateLayout.setHorizontalGroup(
            jpCashierDateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jpCashierDateLayout.setVerticalGroup(
            jpCashierDateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 23, Short.MAX_VALUE)
        );

        jLabel5.setText(resourceMap.getString("jLabel5.text")); // NOI18N
        jLabel5.setName("jLabel5"); // NOI18N

        jbUpdateDate.setAction(actionMap.get("updatePageNumber")); // NOI18N
        jbUpdateDate.setText(resourceMap.getString("jbUpdateDate.text")); // NOI18N
        jbUpdateDate.setName("jbUpdateDate"); // NOI18N

        jtpRegister.setName("jtpRegister"); // NOI18N

        jScrollPane1.setName("jspRegister"); // NOI18N

        jtRegister.setModel(new javax.swing.table.DefaultTableModel(
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
        jtRegister.setFillsViewportHeight(true);
        jtRegister.setName("jtRegister"); // NOI18N
        jtRegister.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                openDoc(evt);
            }
        });
        jtRegister.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jtRegisterKeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(jtRegister);

        jtpRegister.addTab(resourceMap.getString("jspRegister.TabConstraints.tabTitle"), jScrollPane1); // NOI18N

        jcbActiveOnly.setAction(actionMap.get("showActiveOnly")); // NOI18N
        jcbActiveOnly.setBackground(resourceMap.getColor("jcbActiveOnly.background")); // NOI18N
        jcbActiveOnly.setText(resourceMap.getString("jcbActiveOnly.text")); // NOI18N
        jcbActiveOnly.setName("jcbActiveOnly"); // NOI18N

        jcbNotActiveOnly.setAction(actionMap.get("showNotActiveOnly")); // NOI18N
        jcbNotActiveOnly.setBackground(resourceMap.getColor("jcbNotActiveOnly.background")); // NOI18N
        jcbNotActiveOnly.setText(resourceMap.getString("jcbNotActiveOnly.text")); // NOI18N
        jcbNotActiveOnly.setName("jcbNotActiveOnly"); // NOI18N

        jlBalance.setFont(resourceMap.getFont("jlBalance.font")); // NOI18N
        jlBalance.setForeground(resourceMap.getColor("jlBalance.foreground")); // NOI18N
        jlBalance.setText(resourceMap.getString("jlBalance.text")); // NOI18N
        jlBalance.setName("jlBalance"); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jtpRegister, javax.swing.GroupLayout.PREFERRED_SIZE, 938, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jlBalance, javax.swing.GroupLayout.DEFAULT_SIZE, 280, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(jcbActiveOnly)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jcbNotActiveOnly)
                        .addGap(18, 18, 18)
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
                        .addComponent(jpEndDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jbAddDoc)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jbDelete)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jbTrash)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jbPrintRegister)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jpCashierDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jtfPage, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jbUpdateDate)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 34, Short.MAX_VALUE)
                        .addComponent(jbCancel)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(13, 13, 13)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jcbPeriod, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jpStartDate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jpEndDate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jpDate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jcbDate, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jcbActiveOnly)
                                .addComponent(jcbNotActiveOnly))))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jlBalance, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jtpRegister, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jbAddDoc)
                        .addComponent(jbDelete)
                        .addComponent(jbTrash)
                        .addComponent(jbCancel))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jbPrintRegister)
                        .addComponent(jLabel4)
                        .addComponent(jLabel5)
                        .addComponent(jtfPage, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jbUpdateDate))
                    .addComponent(jpCashierDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jtpRegister.getAccessibleContext().setAccessibleName(resourceMap.getString("jtpRegister.AccessibleContext.accessibleName")); // NOI18N

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void openDoc(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_openDoc
        if (evt.getClickCount() > 1) {
            int r = jtRegister.convertRowIndexToModel(jtRegister.getSelectedRow());
            if (r != -1) {
                JInternalFrame rdv =
                        new RegisterDocView(salesView, this, ((Register) table.get(r)).getCode(), 0);
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
    
    private void jtRegisterKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtRegisterKeyPressed
        
        if (evt.getKeyCode() == 155) {
            addDoc();
        }
        if (evt.getKeyCode() == 127) {
            deleteDoc();
        }
    }//GEN-LAST:event_jtRegisterKeyPressed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton jbAddDoc;
    private javax.swing.JButton jbCancel;
    private javax.swing.JButton jbDelete;
    private javax.swing.JButton jbPrintRegister;
    private javax.swing.JButton jbTrash;
    private javax.swing.JButton jbUpdateDate;
    private javax.swing.JCheckBox jcbActiveOnly;
    private javax.swing.JCheckBox jcbDate;
    private javax.swing.JCheckBox jcbNotActiveOnly;
    private javax.swing.JCheckBox jcbPeriod;
    private javax.swing.JLabel jlBalance;
    private javax.swing.JPanel jpCashierDate;
    private javax.swing.JPanel jpDate;
    private javax.swing.JPanel jpEndDate;
    private javax.swing.JPanel jpStartDate;
    private javax.swing.JTable jtRegister;
    private javax.swing.JTextField jtfPage;
    private javax.swing.JTabbedPane jtpRegister;
    // End of variables declaration//GEN-END:variables
    SalesView salesView;
    private JDateChooser cashierDate;
    private JDateChooser date;
    private JDateChooser startDate;
    private JDateChooser endDate;
    private List table;
    private boolean trash;
    private String docFilter;
    private boolean status;
    private static ArrayList<String[]> columns;
    
    public void showTable() {
        table = null;
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
            String df = "";
            if (!docFilter.isEmpty()) {
                df = " and " + docFilter;
            }
            if (jcbDate.isSelected()) {
                hql = "from Register x where "
                        + "DATE(x.datetime) = '" + sdf.format(date.getDate()) + "'"
                        + " and " + delStr
                        + df
                        + " order by x.datetime DESC";
            } else {
                hql = "from Register x"
                        + " where DATE(x.datetime) >= '" + sdf.format(startDate.getDate()) + "'"
                        + " and DATE(x.datetime) <= '" + sdf.format(endDate.getDate()) + "'"
                        + " and " + delStr
                        + df
                        + " order by x.datetime DESC";
            }
            table = session.createQuery(hql).list();
            
            Vector<String> tableHeaders = new Vector<String>();
            for(String[] col : columns) {
                tableHeaders.add(col[0]);
            }
            Vector tableData = new Vector();
            
            sdf = new SimpleDateFormat("dd.MM.yyyy");
            
            if (table != null) {
                for (Object o : table) {
                    Register r = (Register) o;
                    Vector<Object> oneRow = new Vector<Object>();
                    if (r.getActive() == 1) {
                        oneRow.add("V");
                    } else {
                        oneRow.add("");
                    }
                    oneRow.add(r.getNumber());
                    if (r.getDocumentType() == 0) {
                        oneRow.add("Приходный ордер");
                    } else {
                        oneRow.add("Расходный ордер");
                    }
                    oneRow.add(sdf.format(r.getDatetime()));
                    Employee e = (Employee) HUtil.getElement("Employee", r.getEmployeeCode(), session);
                    if (e != null) {
                        oneRow.add(e.getName());
                    } else {
                        oneRow.add("");
                    }
                    oneRow.add(r.getAmount());
                    tableData.add(oneRow);
                }
            }
            
            session.close();
            
            jtRegister.setModel(new DefaultTableModel(tableData, tableHeaders) {
                
                @Override
                public boolean isCellEditable(int r, int c) {
                    return false;
                }
            });
        } catch (Exception e) {
            logger.error(e);
        }
        
        
        for (int j = 0; j < jtRegister.getColumnCount(); j++) {
            jtRegister.getColumnModel().getColumn(j).setCellRenderer(new MyTableCellRenderer());
        }
        
        Util.autoResizeColWidth(jtRegister);
        jtRegister.getColumnModel().getColumn(0).setMinWidth(60);
        jtRegister.getColumnModel().getColumn(0).setMaxWidth(60);

        computeBalance();
        
    }
    
    private void computeBalance() {
        
        try {
            
            long balance = 0;
            String sql =
                    "select sum(r.amount) from Register r"
                    + " where r.documentType = 0"
                    + " and r.active = 1";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);

            if (rs.next()) {
               balance = rs.getLong(1);
            }
            
            rs.close();
            
            sql =
                    "select sum(r.amount) from Register r"
                    + " where r.documentType = 1"
                    + " and r.active = 1";
            
            rs = st.executeQuery(sql);

            if (rs.next()) {
               balance -= rs.getLong(1);
            }
            
            st.close();
            
            jlBalance.setText("Остаток в кассе: " + df.format(balance));

        } catch (Exception e) {
            logger.error(e);
        }
        
    }
    
    @Action
    public void addDoc() {
        Init init = new Init();
        init.init(this, "Register");
        if (status) {
            JInternalFrame rdv;
            if (jtpRegister.getSelectedIndex() > 0) {
                rdv = new RegisterDocView(salesView, this, null, jtpRegister.getSelectedIndex() - 1);
            } else {
                rdv = new RegisterDocView(salesView, this, null, 0);
            }
            salesView.getJDesktopPane().add(rdv, javax.swing.JLayeredPane.DEFAULT_LAYER);
            rdv.setVisible(true);
            try {
                rdv.setSelected(true);
            } catch (java.beans.PropertyVetoException e) {
                logger.error(e);
            }
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
        /*if (jtRegister.getSelectedRow() != -1) {
        int a = JOptionPane.showOptionDialog(
        this, "Удалить? Восстановление будет невозможно! Снимите \"Проведен\" для того чтобы документ не использовался.", "Удаление", JOptionPane.YES_NO_CANCEL_OPTION,
        JOptionPane.QUESTION_MESSAGE, null, new Object[]{"Да", "Нет"}, "Нет");
        if (a == 1) {
        return;
        }
        try {
        Session session = HUtil.getSession();
        session.beginTransaction();
        Register r = (Register) session.load(Register.class,
        ((Register) table.get(jtRegister.getSelectedRow())).getCode());
        session.delete(r);
        session.getTransaction().commit();
        session.close();
        } catch (Exception e) {
        }
        showTable();
        }*/
        if (!trash) {
            int r = jtRegister.getSelectedRow();
            if (r != -1) {
                int a = JOptionPane.showOptionDialog(
                        this, "Удалить документ № " + ((Register) table.get(r)).getNumber() + "?", "Удаление", JOptionPane.YES_NO_CANCEL_OPTION,
                        JOptionPane.QUESTION_MESSAGE, null, new Object[]{
                            "Да", "Нет"}, "Нет");
                if (a == 0) {
                    changeDelStatus(2, r);
                }
            }
        } else {
            int r = jtRegister.getSelectedRow();
            if (r != -1) {
                String number = ((Register) table.get(r)).getNumber();
                if (HUtil.checkEmpty("select r.id from Register r "
                        + " where r.number = '" + number + "'"
                        + " and r.documentType = " + ((Register) table.get(r)).getDocumentType()
                        + " and r.active != 2")) {
                    int a = JOptionPane.showOptionDialog(
                            this, "Восстановить документ № " + number + " из корзины?", "Восстановление", JOptionPane.YES_NO_CANCEL_OPTION,
                            JOptionPane.QUESTION_MESSAGE, null, new Object[]{
                                "Да", "Нет"}, "Нет");
                    if (a == 0) {
                        changeDelStatus(0, r);
                    }
                } else {
                    JOptionPane.showMessageDialog(
                            this,
                            "Документ с данным номером существует!",
                            "Неуникальный номер",
                            JOptionPane.ERROR_MESSAGE);
                    
                }
            }
        }
        
    }
    
    @Action
    public void printRegister() {
        Util.printCashBook(cashierDate.getDate(), jtfPage.getText());
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
            Register doc = (Register) HUtil.getElement("Register", ((Register) table.get(r)).getCode(), session);
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
    public void updatePageNumber() {
        
        int a = JOptionPane.showOptionDialog(
                this, "Изменить номер страницы на дату "
                + (new SimpleDateFormat("dd.MM.yy")).format(cashierDate.getDate())
                + " на № " + jtfPage.getText() + "?",
                "Изменение номера страницы",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                new Object[]{"Да", "Нет"}, "Нет");
        
        if (a == 0) {
            Session session = HUtil.getSession();
            session.beginTransaction();
            String hql =
                    "from Register r where DATE(r.datetime) = '"
                    + (new SimpleDateFormat("yyyy-MM-dd")).format(cashierDate.getDate()) + "'";
            List res = session.createQuery(hql).list();
            for (int j = 0; j < res.size(); j++) {
                Register r = (Register) res.get(j);
                r.setPage(jtfPage.getText());
                session.merge(r);
            }
            session.getTransaction().commit();
            session.close();
        }
    }
    
    private void tabSelected() {
        if (jtpRegister.getSelectedIndex() == 0) {
            docFilter = "";
        } else if (jtpRegister.getSelectedIndex() == 1) {
            docFilter = "x.documentType = 0";
        } else if (jtpRegister.getSelectedIndex() == 2) {
            docFilter = "x.documentType = 1";
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
    public void showNotActiveOnly() {
        if (!jcbActiveOnly.isSelected() && !jcbNotActiveOnly.isSelected()) {
            jcbNotActiveOnly.setSelected(true);
        }
        showTable();
    }
}
