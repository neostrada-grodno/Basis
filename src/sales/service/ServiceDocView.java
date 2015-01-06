/*
 * ServiceDocView.java
 *
 * Created on 08.07.2011, 17:17:40
 */
package sales.service;

import com.toedter.calendar.JDateChooser;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Vector;
import javax.swing.GroupLayout;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
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
import sales.catalogs.ContractorsView;
import sales.catalogs.EmployeeView;
import sales.interfaces.IEmployee;
import sales.catalogs.NomenclatureView;
import sales.catalogs.ServicesView;
import sales.entity.Contractors;
import sales.entity.Employee;
import sales.entity.Inventorytable;
import sales.entity.Nomenclature;
import sales.entity.Service;
import sales.entity.Servicematerialtable;
import sales.entity.Services;
import sales.entity.Servicetable;
import sales.interfaces.IClose;
import sales.interfaces.IContractor;
import sales.interfaces.IFocus;
import sales.interfaces.IProductItem;
import sales.interfaces.IServiceItem;
import sales.util.HUtil;
import sales.util.Util;

public class ServiceDocView extends javax.swing.JInternalFrame
        implements IEmployee, IContractor, IServiceItem, IProductItem, IFocus, IClose {

    static Logger logger = Logger.getLogger(SalesApp.class.getName());

    public ServiceDocView(SalesView salesView, ServiceView serviceView, Integer documentCode) {
        initComponents();

        Util.initJIF(this, "Акт оказанных услуг", serviceView, salesView);
        Util.initJTable(jtServicesDoc);

        this.salesView = salesView;
        this.serviceView = serviceView;
        this.documentCode = documentCode;

        Session session = HUtil.getSession();

        if (documentCode != null) {
            Service x = (Service) HUtil.getElement("Service", documentCode, session);
            jtfNumber.setText(x.getNumber());
            date = new JDateChooser(x.getDatetime());
            if (x.getEmployeeCode() != null) {
                Employee e = (Employee) HUtil.getElement("Employee", x.getEmployeeCode(), session);
                employeeCode = e.getCode();
                if (e != null) {
                    jtfEmployee.setText(e.getShortName());
                }
            }
            if (x.getContractorCode() != null) {
                Contractors c = (Contractors) HUtil.getElement("Contractors", x.getContractorCode(), session);
                contractorCode = c.getCode();
                if (c != null) {
                    jtfContractor.setText(c.getName());
                }
            }
            active = x.getActive();
            lock = x.getLocked();
            if (lock == 1) {
                JOptionPane.showMessageDialog(
                        this,
                        "Элемент редактируется другим пользователем и будет открыт только для просмотра!",
                        "Блокировка", JOptionPane.PLAIN_MESSAGE);
                jtfNumber.setEditable(false);
                date.setEnabled(false);
                jbChooseEmployee.setEnabled(false);
                jbChooseContractor.setEnabled(false);
                jbSave.setEnabled(false);
            } else {
                session.beginTransaction();
                x.setLocked(1);
                session.merge(x);
                session.getTransaction().commit();
            }
            table = HUtil.executeHql("from Servicetable st where st.documentCode = " + documentCode);
            mtable = HUtil.executeHql("from Servicematerialtable st where st.documentCode = " + documentCode);

        } else {
            jtfNumber.setText(HUtil.getNextDocNumber("Service", "", session));
            Calendar cl = Calendar.getInstance();
            date = new JDateChooser(cl.getTime());
            active = 0;
            lock = 0;
            table = new ArrayList();
            mtable = new ArrayList();
        }

        GroupLayout gl = (GroupLayout) jpDate.getLayout();
        gl.setHorizontalGroup(gl.createParallelGroup().addGroup(gl.createSequentialGroup().addComponent(date)));
        gl.setVerticalGroup(gl.createParallelGroup().addGroup(gl.createSequentialGroup().addComponent(date)));

        session.close();

        jTextBox = new JTextField();
        jTextBox.addKeyListener(new KeyAdapter() {

            @Override
            public void keyTyped(KeyEvent e) {
                int c = ((JTable) ((JTextField) e.getComponent()).getParent()).getSelectedColumn();
                jTextBox.setEditable(
                        (Character.isDigit(e.getKeyChar()) || e.getKeyChar() == KeyEvent.VK_BACK_SPACE)
                        && (c == 3 || c == 4));
            }
        });

        jtServicesDoc.addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent e) {
                int r = jtServicesDoc.getSelectedRow();
                int c = jtServicesDoc.getSelectedColumn();
                if (r != -1 && c != -1) {
                    jTextBox.setEditable(
                            Character.isDigit(e.getKeyChar()) || e.getKeyChar() == KeyEvent.VK_BACK_SPACE);
                }
            }
        });

        jtMaterialsDoc.addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent e) {
                int r = jtMaterialsDoc.getSelectedRow();
                int c = jtMaterialsDoc.getSelectedColumn();
                if (r != -1 && c != -1) {
                    jTextBox.setEditable(
                            Character.isDigit(e.getKeyChar()) || e.getKeyChar() == KeyEvent.VK_BACK_SPACE);
                }
            }
        });

        showTable();
        showMaterialTable();

        Util.selectFirstRow(jtServicesDoc);

        //set move one cell down on enter
        Util.setMoveDown(jtServicesDoc);

        if (documentCode != null) {
            hash = Util.hash(this);
        } else {
            hash = null;
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

        jLabel1 = new javax.swing.JLabel();
        jtfNumber = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jpDate = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jtServicesDoc = new javax.swing.JTable() {

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
        jbAdd = new javax.swing.JButton();
        jbDelete = new javax.swing.JButton();
        jbCancel = new javax.swing.JButton();
        jbSave = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jtMaterialsDoc = new javax.swing.JTable();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jbAddMaterial = new javax.swing.JButton();
        jbDeleteMaterial = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        jtfEmployee = new javax.swing.JTextField();
        jbChooseEmployee = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        jtfContractor = new javax.swing.JTextField();
        jbChooseContractor = new javax.swing.JButton();

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance().getContext().getResourceMap(ServiceDocView.class);
        setBackground(resourceMap.getColor("Form.background")); // NOI18N
        setClosable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle(resourceMap.getString("Form.title")); // NOI18N
        setName("Form"); // NOI18N

        jLabel1.setText(resourceMap.getString("jLabel1.text")); // NOI18N
        jLabel1.setName("jLabel1"); // NOI18N

        jtfNumber.setText(resourceMap.getString("jtfNumber.text")); // NOI18N
        jtfNumber.setName("jtfNumber"); // NOI18N

        jLabel2.setText(resourceMap.getString("jLabel2.text")); // NOI18N
        jLabel2.setName("jLabel2"); // NOI18N

        jpDate.setBackground(resourceMap.getColor("jpDate.background")); // NOI18N
        jpDate.setName("jpDate"); // NOI18N

        javax.swing.GroupLayout jpDateLayout = new javax.swing.GroupLayout(jpDate);
        jpDate.setLayout(jpDateLayout);
        jpDateLayout.setHorizontalGroup(
            jpDateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 101, Short.MAX_VALUE)
        );
        jpDateLayout.setVerticalGroup(
            jpDateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 23, Short.MAX_VALUE)
        );

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        jtServicesDoc.setModel(new javax.swing.table.DefaultTableModel(
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
        jtServicesDoc.setCellSelectionEnabled(true);
        jtServicesDoc.setFillsViewportHeight(true);
        jtServicesDoc.setName("jtServicesDoc"); // NOI18N
        jtServicesDoc.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jtServicesDocMouseClicked(evt);
            }
        });
        jtServicesDoc.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jtServicesDocKeyTyped(evt);
            }
        });
        jScrollPane1.setViewportView(jtServicesDoc);

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance().getContext().getActionMap(ServiceDocView.class, this);
        jbAdd.setAction(actionMap.get("add")); // NOI18N
        jbAdd.setText(resourceMap.getString("jbAdd.text")); // NOI18N
        jbAdd.setName("jbAdd"); // NOI18N

        jbDelete.setAction(actionMap.get("del")); // NOI18N
        jbDelete.setText(resourceMap.getString("jbDelete.text")); // NOI18N
        jbDelete.setName("jbDelete"); // NOI18N

        jbCancel.setAction(actionMap.get("closeDoc")); // NOI18N
        jbCancel.setText(resourceMap.getString("jbCancel.text")); // NOI18N
        jbCancel.setName("jbCancel"); // NOI18N

        jbSave.setAction(actionMap.get("save")); // NOI18N
        jbSave.setText(resourceMap.getString("jbSave.text")); // NOI18N
        jbSave.setName("jbSave"); // NOI18N

        jButton2.setAction(actionMap.get("showPrint")); // NOI18N
        jButton2.setText(resourceMap.getString("jButton2.text")); // NOI18N
        jButton2.setName("jButton2"); // NOI18N

        jScrollPane2.setName("jScrollPane2"); // NOI18N

        jtMaterialsDoc.setModel(new javax.swing.table.DefaultTableModel(
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
        jtMaterialsDoc.setName("jtMaterialsDoc"); // NOI18N
        jtMaterialsDoc.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jtMaterialsDocMouseClicked(evt);
            }
        });
        jtMaterialsDoc.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jtMaterialsDocKeyTyped(evt);
            }
        });
        jScrollPane2.setViewportView(jtMaterialsDoc);

        jLabel3.setText(resourceMap.getString("jLabel3.text")); // NOI18N
        jLabel3.setName("jLabel3"); // NOI18N

        jLabel4.setText(resourceMap.getString("jLabel4.text")); // NOI18N
        jLabel4.setName("jLabel4"); // NOI18N

        jbAddMaterial.setAction(actionMap.get("addProduct")); // NOI18N
        jbAddMaterial.setText(resourceMap.getString("jbAddMaterial.text")); // NOI18N
        jbAddMaterial.setName("jbAddMaterial"); // NOI18N

        jbDeleteMaterial.setAction(actionMap.get("delProduct")); // NOI18N
        jbDeleteMaterial.setText(resourceMap.getString("jbDeleteMaterial.text")); // NOI18N
        jbDeleteMaterial.setName("jbDeleteMaterial"); // NOI18N

        jLabel5.setText(resourceMap.getString("jLabel5.text")); // NOI18N
        jLabel5.setName("jLabel5"); // NOI18N

        jtfEmployee.setText(resourceMap.getString("jtfEmployee.text")); // NOI18N
        jtfEmployee.setName("jtfEmployee"); // NOI18N

        jbChooseEmployee.setAction(actionMap.get("chooseEmployee")); // NOI18N
        jbChooseEmployee.setText(resourceMap.getString("jbChooseEmployee.text")); // NOI18N
        jbChooseEmployee.setName("jbChooseEmployee"); // NOI18N

        jLabel6.setText(resourceMap.getString("jLabel6.text")); // NOI18N
        jLabel6.setName("jLabel6"); // NOI18N

        jtfContractor.setText(resourceMap.getString("jtfContractor.text")); // NOI18N
        jtfContractor.setName("jtfContractor"); // NOI18N

        jbChooseContractor.setAction(actionMap.get("chooseContractor")); // NOI18N
        jbChooseContractor.setText(resourceMap.getString("jbChooseContractor.text")); // NOI18N
        jbChooseContractor.setName("jbChooseContractor"); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 649, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jtfNumber, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jpDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel3))
                        .addGap(22, 22, 22)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel5)
                            .addComponent(jLabel6))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jtfContractor, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 197, Short.MAX_VALUE)
                            .addComponent(jtfEmployee, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 197, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jbChooseEmployee, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jbChooseContractor, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 649, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jbAddMaterial)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jbDeleteMaterial)
                        .addGap(115, 115, 115)
                        .addComponent(jButton2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 79, Short.MAX_VALUE)
                        .addComponent(jbSave)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jbCancel))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jbAdd)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jbDelete))
                    .addComponent(jLabel4))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jtfEmployee, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jbChooseEmployee))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jpDate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jtfNumber))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jtfContractor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jbChooseContractor)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 154, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jbAdd)
                    .addComponent(jbDelete))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 123, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jbCancel)
                    .addComponent(jbSave)
                    .addComponent(jButton2)
                    .addComponent(jbAddMaterial)
                    .addComponent(jbDeleteMaterial))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jtServicesDocMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtServicesDocMouseClicked

        if (evt.getClickCount() > 1) {
            int r = jtServicesDoc.getSelectedRow();
            if (r != -1) {
                if (jtServicesDoc.columnAtPoint(evt.getPoint()) < 2) {
                    ServicesView sv =
                            new ServicesView(salesView, this, r);
                    salesView.getJDesktopPane().add(sv, javax.swing.JLayeredPane.DEFAULT_LAYER);
                    sv.setVisible(true);
                    try {
                        sv.setSelected(true);
                    } catch (java.beans.PropertyVetoException e) {
                        logger.error(e);
                    }
                }
            }
        }
    }//GEN-LAST:event_jtServicesDocMouseClicked

    private void jtServicesDocKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtServicesDocKeyTyped

        if (evt.getKeyCode() == 155) {
            add();
        }
        if (evt.getKeyCode() == 127) {
            del();
        }

    }//GEN-LAST:event_jtServicesDocKeyTyped

    private void jtMaterialsDocMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtMaterialsDocMouseClicked
        
        if (evt.getClickCount() > 1) {
            int r = jtMaterialsDoc.getSelectedRow();
            if (r != -1) {
                if (jtMaterialsDoc.columnAtPoint(evt.getPoint()) < 2) {
                    NomenclatureView sv =
                            new NomenclatureView(salesView, this, r, -1, "", "", null, false);
                    salesView.getJDesktopPane().add(sv, javax.swing.JLayeredPane.DEFAULT_LAYER);
                    sv.setVisible(true);
                    try {
                        sv.setSelected(true);
                    } catch (java.beans.PropertyVetoException e) {
                        logger.error(e);
                    }
                }
            }
        }
        
    }//GEN-LAST:event_jtMaterialsDocMouseClicked

    private void jtMaterialsDocKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtMaterialsDocKeyTyped

        if (evt.getKeyCode() == 155) {
            add();
        }
        if (evt.getKeyCode() == 127) {
            del();
        }
    }//GEN-LAST:event_jtMaterialsDocKeyTyped

    @Action
    public void add() {
        ServicesView nv = new ServicesView(salesView, this, -1);
        salesView.getJDesktopPane().add(nv, javax.swing.JLayeredPane.DEFAULT_LAYER);
        nv.setVisible(true);
        try {
            nv.setSelected(true);
        } catch (java.beans.PropertyVetoException e) {
            logger.error(e);
        }
    }

    @Action
    public void del() {
        if (jtServicesDoc.getSelectedRow() >= 0) {
            int i = jtServicesDoc.getSelectionModel().getMinSelectionIndex();
            while (i <= jtServicesDoc.getSelectionModel().getMaxSelectionIndex()) {
                if (jtServicesDoc.getSelectionModel().isSelectedIndex(i)) {
                    table.remove(i);
                    ((DefaultTableModel) jtServicesDoc.getModel()).removeRow(i);
                } else {
                    i++;
                }
            }
        }
    }

    @Override
    public void setProductItem(Integer productCode, int r) {

        try {

            Session session = HUtil.getSession();
            Servicematerialtable st = (Servicematerialtable) mtable.get(r);
            Nomenclature n = (Nomenclature) HUtil.getElement("Nomenclature", productCode, session);
            if (n == null) {
                return;
            }
            st.setProductCode(productCode);
            st.setPrice(n.getPrice());
            st.setQuantity(1);
            st.setAmount(n.getPrice());

            mtable.set(r, st);

            session.close();

        } catch (Exception e) {
            logger.error(e);
        }

        showMaterialTable();

    }

    @Action
    public void setProductItemPart(Integer productCode, int row) {
        setProductItem(productCode, row);
    }

    public void addProductItem(Integer productCode) {

        try {

            Session session = HUtil.getSession();
            Servicematerialtable st = new Servicematerialtable();
            Nomenclature n = (Nomenclature) HUtil.getElement("Nomenclature", productCode, session);
            if (n == null) {
                return;
            }
            st.setProductCode(productCode);

            if (documentCode != null) {
                st.setDocumentCode(documentCode);
            }

            st.setPrice(n.getPrice());
            st.setQuantity(1);
            st.setAmount(n.getPrice());
            mtable.add(st);

            session.close();

            showMaterialTable();
            
        } catch (Exception e) {
            logger.error(e);
        }
    }

    @Action
    public void addProduct() {
        NomenclatureView nv = new NomenclatureView(salesView, this, -1, -1, "", "", null, false);
        salesView.getJDesktopPane().add(nv, javax.swing.JLayeredPane.DEFAULT_LAYER);
        nv.setVisible(true);
        try {
            nv.setSelected(true);
        } catch (java.beans.PropertyVetoException e) {
            logger.error(e);
        }
    }

    @Action
    public void delProduct() {
        if (jtMaterialsDoc.getSelectedRow() >= 0) {
            int i = jtMaterialsDoc.getSelectionModel().getMinSelectionIndex();
            while (i <= jtMaterialsDoc.getSelectionModel().getMaxSelectionIndex()) {
                if (jtMaterialsDoc.getSelectionModel().isSelectedIndex(i)) {
                    mtable.remove(i);
                    ((DefaultTableModel) jtMaterialsDoc.getModel()).removeRow(i);
                } else {
                    i++;
                }
            }
        }
    }

    @Action
    public void save() {

        if (lock == 0) {

            TableCellEditor tce = jtServicesDoc.getCellEditor();
            if (tce != null) {
                tce.stopCellEditing();
            }

            TableCellEditor mtce = jtMaterialsDoc.getCellEditor();
            if (mtce != null) {
                mtce.stopCellEditing();
            }

            if (hash == null || !hash.equals(Util.hash(this))) {

                int a = JOptionPane.showOptionDialog(
                        this, "Сохранить документ?", "Сохранение", JOptionPane.YES_NO_CANCEL_OPTION,
                        JOptionPane.QUESTION_MESSAGE, null, new Object[]{"Да", "Нет"}, "Да");

                if (a == 0) {
                    try {

                        Session session = HUtil.getSession();
                        session.beginTransaction();

                        Service s = null;
                        if (documentCode != null) {
                            s = (Service) HUtil.getElement("Service", documentCode, session);
                        }
                        if (s == null) {
                            s = new Service();
                        }

                        s.setDatetime(date.getDate());
                        s.setNumber(jtfNumber.getText());
                        if (employeeCode != null) {
                            s.setEmployeeCode(employeeCode);
                        }
                        if (contractorCode != null) {
                            s.setContractorCode(contractorCode);
                        }
                        s.setActive(active);

                        if (documentCode != null) {
                            session.merge(s);
                        } else {
                            session.save(s);
                        }

                        session.getTransaction().commit();

                        session.beginTransaction();

                        documentCode = s.getCode();

                        List objs = session.createQuery("from Servicetable st where st.documentCode = " + documentCode).list();

                        if (table != null) {
                            for (int j = 0; j < table.size(); j++) {
                                Servicetable st = (Servicetable) table.get(j);
                                st.setDocumentCode(documentCode);
                                boolean newIn = true;
                                for (int k = 0; k < objs.size(); k++) {
                                    if (((Servicetable) objs.get(k)).getCode() == st.getCode()) {
                                        newIn = false;
                                        objs.remove(k);
                                        break;
                                    }
                                }
                                if (newIn) {
                                    session.save(st);
                                } else {
                                    session.merge(st);
                                }
                            }
                        }

                        for (int j = 0; j < objs.size(); j++) {
                            session.delete((Servicetable) objs.get(j));
                        }

                        List mobjs = session.createQuery("from Servicematerialtable st where st.documentCode = " + documentCode).list();

                        if (mtable != null) {
                            for (int j = 0; j < mtable.size(); j++) {
                                Servicematerialtable st = (Servicematerialtable) mtable.get(j);
                                st.setDocumentCode(documentCode);
                                boolean newIn = true;
                                for (int k = 0; k < mobjs.size(); k++) {
                                    if (((Servicematerialtable) mobjs.get(k)).getCode() == st.getCode()) {
                                        newIn = false;
                                        mobjs.remove(k);
                                        break;
                                    }
                                }
                                if (newIn) {
                                    session.save(st);
                                } else {
                                    session.merge(st);
                                }
                            }
                        }

                        for (int j = 0; j < mobjs.size(); j++) {
                            session.delete((Servicematerialtable) mobjs.get(j));
                        }

                        session.getTransaction().commit();

                        session.close();

                    } catch (Exception e) {
                        logger.error(e);
                    }
                    Util.updateJournals(salesView, ServiceView.class);
                    hash = Util.hash(this);
                }
            }
        }
    }

    public void close() {

        if (lock == 0) {

            save();

            Session session = HUtil.getSession();
            Service x = (Service) HUtil.getElement("Service", documentCode, session);
            if (x != null) {
                session.beginTransaction();
                x.setLocked(0);
                session.merge(x);
                session.getTransaction().commit();
            }
            session.close();
        }
    }

    @Action
    public void closeDoc() {
        close();
        Util.closeJIF(this, serviceView, salesView);
        Util.closeJIFTab(this, salesView);
    }

    @Action
    public void chooseEmployee() {
        EmployeeView ev = new EmployeeView(salesView, this, "Employee");
        salesView.getJDesktopPane().add(ev, javax.swing.JLayeredPane.DEFAULT_LAYER);
        ev.setVisible(true);
        try {
            ev.setSelected(true);
        } catch (java.beans.PropertyVetoException e) {
            logger.error(e);
        }
    }

    @Action
    public void chooseContractor() {
        ContractorsView cc = new ContractorsView(contractorCode, salesView, this);
        salesView.getJDesktopPane().add(cc, javax.swing.JLayeredPane.DEFAULT_LAYER);
        cc.setVisible(true);
        try {
            cc.setSelected(true);
        } catch (java.beans.PropertyVetoException e) {
            logger.error(e);
        }
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JButton jbAdd;
    private javax.swing.JButton jbAddMaterial;
    private javax.swing.JButton jbCancel;
    private javax.swing.JButton jbChooseContractor;
    private javax.swing.JButton jbChooseEmployee;
    private javax.swing.JButton jbDelete;
    private javax.swing.JButton jbDeleteMaterial;
    private javax.swing.JButton jbSave;
    private javax.swing.JPanel jpDate;
    private javax.swing.JTable jtMaterialsDoc;
    private javax.swing.JTable jtServicesDoc;
    private javax.swing.JTextField jtfContractor;
    private javax.swing.JTextField jtfEmployee;
    private javax.swing.JTextField jtfNumber;
    // End of variables declaration//GEN-END:variables
    private SalesView salesView;
    private ServiceView serviceView;
    private Integer documentCode;
    private JDateChooser date;
    private List table;
    private List mtable;
    private JTextField jTextBox;
    private Integer active;
    private Timer elapse;
    private boolean scanEnter;
    private Integer employeeCode;
    private Integer contractorCode;
    private Integer lock;
    private Integer hash;

    private class MyTableModel extends DefaultTableModel {

        public MyTableModel(Vector table, Vector header) {
            super(table, header);
        }

        @Override
        public boolean isCellEditable(int r, int c) {
            return c == 3 || c == 4;
        }
    }

    public void showTable() {

        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

        Vector<String> tableHeaders = new Vector<String>();
        Vector tableData = new Vector();
        tableHeaders.add("№");
        tableHeaders.add("Наименование");
        tableHeaders.add("Ед.изм.");
        tableHeaders.add("Цена");
        tableHeaders.add("Кол-во");
        tableHeaders.add("Сумма");

        Session session = HUtil.getSession();
        for (int i = 0; i < table.size(); i++) {
            Servicetable st = (Servicetable) table.get(i);
            Vector<Object> oneRow = new Vector<Object>();
            oneRow.add(i + 1);
            Services s = (Services) HUtil.getElement("Services", st.getServiceCode(), session);
            if (s != null) {
                oneRow.add(s.getName());
                oneRow.add(s.getUnit());
            } else {
                oneRow.add("");
                oneRow.add("");
            }
            oneRow.add(st.getPrice());
            int q = st.getQuantity();
            oneRow.add(q);
            oneRow.add(q * st.getPrice());
            tableData.add(oneRow);
        }
        session.close();

        jtServicesDoc.setModel(new MyTableModel(tableData, tableHeaders));
        tuneTable();

        setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

    }

    private void tuneTable() {

        Util.autoResizeColWidth(jtServicesDoc);

        jtServicesDoc.getColumnModel().getColumn(3).setCellEditor(new MyCellEditor(jTextBox));
        jtServicesDoc.getColumnModel().getColumn(4).setCellEditor(new MyCellEditor(jTextBox));
        jtServicesDoc.getColumnModel().getColumn(5).setCellEditor(new MyCellEditor(jTextBox));

        jtServicesDoc.getModel().addTableModelListener(new TableModelListener() {

            @Override
            public void tableChanged(TableModelEvent e) {
                int r = e.getFirstRow();
                int c = e.getColumn();
                if (r != -1) {
                    if (c == 3) {
                        Servicetable st = (Servicetable) table.get(r);
                        int price = Util.str2int(jtServicesDoc.getValueAt(r, 3).toString());
                        int quantity = Util.str2int(jtServicesDoc.getValueAt(r, 4).toString());
                        st.setPrice(price);
                        st.setAmount(price * quantity);
                        table.set(r, st);
                        showTable();
                        Util.moveCell(r, c, jtServicesDoc, true);
                    } else if (c == 4) {
                        Servicetable st = (Servicetable) table.get(r);
                        int price = Util.str2int(jtServicesDoc.getValueAt(r, 3).toString());
                        int quantity = Util.str2int(jtServicesDoc.getValueAt(r, 4).toString());
                        st.setQuantity(quantity);
                        st.setAmount(price * quantity);
                        table.set(r, st);
                        showTable();
                        Util.moveCell(r, c, jtServicesDoc, true);
                    }
                }
            }
        });
    }

    public void showMaterialTable() {

        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

        Vector<String> tableHeaders = new Vector<String>();
        Vector tableData = new Vector();
        tableHeaders.add("№");
        tableHeaders.add("Наименование");
        tableHeaders.add("Ед.изм.");
        tableHeaders.add("Цена");
        tableHeaders.add("Кол-во");
        tableHeaders.add("Сумма");

        Session session = HUtil.getSession();
        for (int i = 0; i < mtable.size(); i++) {
            Servicematerialtable st = (Servicematerialtable) mtable.get(i);
            Vector<Object> oneRow = new Vector<Object>();
            oneRow.add(i + 1);
            Nomenclature n = (Nomenclature) HUtil.getElement("Nomenclature", st.getProductCode(), session);
            if (n != null) {
                oneRow.add(n.getName());
                oneRow.add(n.getUnit());
            } else {
                oneRow.add("");
                oneRow.add("");
            }
            oneRow.add(st.getPrice());
            int q = st.getQuantity();
            oneRow.add(q);
            oneRow.add(q * st.getPrice());
            tableData.add(oneRow);
        }
        session.close();

        jtMaterialsDoc.setModel(new MyTableModel(tableData, tableHeaders));
        tuneMaterialTable();

        setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

    }

    private void tuneMaterialTable() {

        Util.autoResizeColWidth(jtMaterialsDoc);

        jtMaterialsDoc.getColumnModel().getColumn(3).setCellEditor(new MyCellEditor(jTextBox));
        jtMaterialsDoc.getColumnModel().getColumn(4).setCellEditor(new MyCellEditor(jTextBox));

        jtMaterialsDoc.getModel().addTableModelListener(new TableModelListener() {

            @Override
            public void tableChanged(TableModelEvent e) {
                int r = e.getFirstRow();
                int c = e.getColumn();
                if (r != -1) {
                    if (c == 3) {
                        Servicematerialtable st = (Servicematerialtable) mtable.get(r);
                        int price = Util.str2int(jtMaterialsDoc.getValueAt(r, 3).toString());
                        int quantity = Util.str2int(jtMaterialsDoc.getValueAt(r, 4).toString());
                        st.setPrice(price);
                        st.setAmount(price * quantity);
                        mtable.set(r, st);
                        showMaterialTable();
                        Util.moveCell(r, c, jtMaterialsDoc, true);
                    } else if (c == 4) {
                        Servicematerialtable st = (Servicematerialtable) mtable.get(r);
                        int price = Util.str2int(jtMaterialsDoc.getValueAt(r, 3).toString());
                        int quantity = Util.str2int(jtMaterialsDoc.getValueAt(r, 4).toString());
                        st.setQuantity(quantity);
                        st.setAmount(price * quantity);
                        mtable.set(r, st);
                        showMaterialTable();
                        Util.moveCell(r, c, jtMaterialsDoc, true);
                    }
                }
            }
        });
    }

    @Override
    public void addServiceItem(Integer serviceCode) {

        for (int i = 0; i < table.size(); i++) {
            if (((Servicetable) table.get(i)).getServiceCode() == serviceCode) {
                Util.go2rect(jtServicesDoc, i);
                return;
            }
        }

        try {

            Session session = HUtil.getSession();
            Services s = (Services) HUtil.getElement("Services", serviceCode, session);
            if (s != null) {

                Object oQuantity =
                        JOptionPane.showInputDialog(
                        null,
                        "Введите количество",
                        "Количество",
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        null,
                        1);

                if (Util.checkNumber((String) oQuantity)) {

                    int quantity = Util.str2int(oQuantity.toString());

                    if (quantity > 0) {

                        Servicetable st = new Servicetable();

                        st.setServiceCode(serviceCode);

                        if (documentCode != null) {
                            st.setDocumentCode(documentCode);
                        }

                        st.setQuantity(quantity);
                        st.setPrice(s.getPrice());
                        st.setAmount(s.getPrice() * quantity);

                        table.add(st);

                        showTable();
                        Util.go2rect(jtServicesDoc, jtServicesDoc.getRowCount() - 1);

                    }
                }

                session.close();

            }

        } catch (Exception e) {
            logger.error(e);
        }

    }

    @Override
    public void setServiceItem(Integer serviceCode, int r) {

        for (int i = 0; i < table.size(); i++) {
            if (((Servicetable) table.get(i)).getServiceCode() == serviceCode) {
                Util.go2rect(jtServicesDoc, i);
                return;
            }
        }

        try {
            Session session = HUtil.getSession();
            Servicetable st = (Servicetable) table.get(r);
            Services s = (Services) HUtil.getElement("Services", serviceCode, session);
            if (s == null) {
                return;
            }

            st.setServiceCode(serviceCode);
            st.setPrice(s.getPrice());
            st.setQuantity(HUtil.getBalance(serviceCode));

            table.set(r, st);

            session.close();
        } catch (Exception e) {
            logger.error(e);
        }
        showTable();
    }

    @Override
    public void setFocus() {
        jtServicesDoc.setRequestFocusEnabled(true);
        jtServicesDoc.requestFocusInWindow();
    }

    @Override
    public void setEmployee(Integer employeeCode, String name) {
        this.employeeCode = employeeCode;
        Session session = HUtil.getSession();
        jtfEmployee.setText(((Employee) HUtil.getElement("Employee", employeeCode, session)).getName());
        session.close();
    }

    public void setContractor(Integer contractorCode) {
        this.contractorCode = contractorCode;
        Session session = HUtil.getSession();
        Contractors c = (Contractors) HUtil.getElement("Contractors", contractorCode, session);
        if (c != null) {
            jtfContractor.setText(c.getName());
        }
        session.close();
    }

    @Action
    public void showPrint() {

        try {

            File file = new File(Util.getAppPath() + "\\templates\\Service.ods");
            final Sheet sheet = SpreadSheet.createFromFile(file).getSheet(0);

            Session session = HUtil.getSession();
            
            String name = Util.getStr(HUtil.getConstant("name"));
            String acc = Util.getStr(HUtil.getConstant("account")) + " " + Util.getStr(HUtil.getConstant("bank"));
            String dName = Util.getStr(HUtil.getShortNameByCode(HUtil.getIntConstant("director")));
            String eName = Util.getStr(HUtil.getFieldByCode(employeeCode, "shortName", "Employee"));
            String cName = Util.getStr(HUtil.getFieldByCode(contractorCode, "name", "Contractors"));
            String chPos = Util.getStr(HUtil.getFieldByCode(contractorCode, "headPositionName", "Contractors"));
            String chName = Util.getStr(HUtil.getFieldByCode(contractorCode, "head", "Contractors"));
            String sDate = Util.date2String(date.getDate());
            
            sheet.getCellAt("A1").setValue("Акт выполненных работ №" + jtfNumber.getText());
            sheet.getCellAt("D3").setValue(sDate);
            sheet.getCellAt("A5").setValue("Мы, нижеподписавшиеся, представитель Исполнителя Директор " + dName);
            sheet.getCellAt("A6").setValue("с одной стороны и представитель Заказчика " + chPos + " " + chName);
            sheet.getCellAt("A8").setValue("обслуживанию компьютерного оборудования по договору №" + jtfNumber.getText() + " от " + sDate);

            int ts = table.size();
            if (ts > 0) {
                sheet.duplicateRows(13, 1, ts - 1);
            }

            int amount = 0;
            int i;
            for (i = 0; i < ts; i++) {

                Servicetable st = (Servicetable) table.get(i);
                Services s = (Services) HUtil.getElement("Services", st.getServiceCode(), session);

                sheet.getCellAt("A" + (14 + i)).setValue(i + 1);
                sheet.getCellAt("B" + (14 + i)).setValue(s.getName());
                sheet.getCellAt("C" + (14 + i)).setValue(st.getPrice());
                sheet.getCellAt("D" + (14 + i)).setValue(st.getQuantity());
                sheet.getCellAt("E" + (14 + i)).setValue(st.getAmount());
                amount += st.getAmount();
            }

            sheet.getCellAt("E" + (15 + i)).setValue(amount);

            sheet.getCellAt("B" + (18 + i)).setValue(amount);
            sheet.getCellAt("C" + (18 + i)).setValue(Util.money2str(amount));
            
            int amountMat = 0;
            for(int j = 0; j < mtable.size(); j++) {
                amountMat += ((Servicematerialtable) mtable.get(j)).getAmount();
            }
            
            sheet.getCellAt("B" + (22 + i)).setValue(amountMat);
            sheet.getCellAt("C" + (22 + i)).setValue(Util.money2str(amountMat));
            sheet.getCellAt("B" + (24 + i)).setValue(amount + amountMat);
            sheet.getCellAt("C" + (24 + i)).setValue(Util.money2str(amount + amountMat));

            sheet.getCellAt("A" + (32 + i)).setValue(cName);
            sheet.getCellAt("A" + (35 + i)).setValue(chPos + " " + chName);
            sheet.getCellAt("B" + (37 + i)).setValue(chName);
            sheet.getCellAt("A" + (39 + i)).setValue(sDate);

            sheet.getCellAt("E" + (31 + i)).setValue("Исполнитель: " + name);
            sheet.getCellAt("E" + (33 + i)).setValue(acc);
            sheet.getCellAt("E" + (35 + i)).setValue("Директор " + name);
            sheet.getCellAt("E" + (37 + i)).setValue(dName);
            sheet.getCellAt("E" + (39 + i)).setValue(sDate);

            sheet.getCellAt("C" + (41 + i)).setValue("Работы выполнил " + eName);

            session.close();

            String r = " " + Math.round(Math.random() * 100000);
            String fileName = "temp\\Акт выполненных работ №" + jtfNumber.getText() + r + ".ods";
            File outputFile = new File(fileName);
            sheet.getSpreadSheet().saveAs(outputFile);
            Util.openDoc("temp\\Акт выполненных работ №" + jtfNumber.getText() + r + ".ods");

        } catch (Exception e) {
            System.err.println(e);
            e.printStackTrace();
            logger.error(e);
        }
    }


}