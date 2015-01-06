/*
 * InventoryDocView.java
 *
 * Created on 08.07.2011, 17:17:40
 */
package sales.inventory;

import com.toedter.calendar.JDateChooser;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;
import javax.swing.GroupLayout;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SpinnerDateModel;
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
import sales.catalogs.EmployeeView;
import sales.interfaces.IEmployee;
import sales.catalogs.NomenclatureView;
import sales.entity.Employee;
import sales.entity.Incoming;
import sales.entity.Incomingtable;
import sales.entity.Inventory;
import sales.entity.Inventorytable;
import sales.entity.Nomenclature;
import sales.entity.Outcoming;
import sales.entity.Outcomingtable;
import sales.incoming.IncomingDocView;
import sales.incoming.IncomingView;
import sales.interfaces.IClose;
import sales.interfaces.IFocus;
import sales.interfaces.IProductItem;
import sales.outcoming.OutcomingDocView;
import sales.outcoming.OutcomingView;
import sales.util.HUtil;
import sales.util.Util;

public class InventoryDocView extends javax.swing.JInternalFrame implements IEmployee, IProductItem, IFocus, IClose {

    private static final int AUTO_KEY_PRESS_TIMEOUT = 100;
    private static final String path = "\\\\k3n\\Work\\nomenclature.txt";
    private static final int KEY_PRESS_TIMEOUT = 500;
    static Logger logger = Logger.getLogger(SalesApp.class.getName());

    public InventoryDocView(SalesView salesView, InventoryView inventoryView, Integer documentCode, Integer productCode) {
        initComponents();

        Util.initJIF(this, "Инвентаризация", inventoryView, salesView);
        Util.initJTable(jtInventoryDoc);

        this.salesView = salesView;
        this.inventoryView = inventoryView;
        this.documentCode = documentCode;

        jsTime.setModel(new SpinnerDateModel());
        JSpinner.DateEditor timeEditor = new JSpinner.DateEditor(jsTime, "HH:mm");
        jsTime.setEditor(timeEditor);

        Session session = HUtil.getSession();

        if (documentCode != null) {
            Inventory i = (Inventory) HUtil.getElement("Inventory", documentCode, session);
            jtfNumber.setText(i.getNumber());
            date = new JDateChooser(i.getDatetime());
            jsTime.setValue(i.getDatetime());
            if (i.getEmployeeCode() != null) {
                Employee e = (Employee) HUtil.getElement("Employee", i.getEmployeeCode(), session);
                if (e != null) {
                    jtfEmployee.setText(e.getName());
                }
            }
            incomingDoc = i.getIncomingCode();
            outcomingDoc = i.getOutcomingCode();
            showDocs(session);
            if (i.getEmployeeCode() != null) {
                Employee e = (Employee) HUtil.getElement("Employee", i.getEmployeeCode(), session);
                employeeCode = e.getCode();
                if (e != null) {
                    jtfEmployee.setText(e.getShortName());
                }
            }
            active = i.getActive();
            lock = i.getLocked();
            if (lock == 1) {
                JOptionPane.showMessageDialog(
                        this,
                        "Элемент редактируется другим пользователем и будет открыт только для просмотра!",
                        "Блокировка", JOptionPane.PLAIN_MESSAGE);
                jtfNumber.setEditable(false);
                date.setEnabled(false);
                jbChooseEmployee.setEnabled(false);
                jbSave.setEnabled(false);
            } else {
                session.beginTransaction();
                i.setLocked(1);
                session.merge(i);
                session.getTransaction().commit();
            }
            table = HUtil.executeHql("from Inventorytable it where it.documentCode = " + documentCode + " order by it.line");

        } else {
            jtfNumber.setText(HUtil.getNextDocNumber("Inventory", "", session));
            Calendar cl = Calendar.getInstance();
            date = new JDateChooser(cl.getTime());
            jsTime.setValue(cl.getTime());
            active = 0;
            lock = 0;
            table = new ArrayList();
        }

        GroupLayout gl = (GroupLayout) jpDate.getLayout();
        gl.setHorizontalGroup(gl.createParallelGroup().addGroup(gl.createSequentialGroup().addComponent(date)));
        gl.setVerticalGroup(gl.createParallelGroup().addGroup(gl.createSequentialGroup().addComponent(date)));

        session.close();

        jTextBox = new JTextField();
        jTextBox.addKeyListener(new KeyAdapter() {

            @Override
            public void keyTyped(KeyEvent e) {
                jTextBox.setEditable(Character.isDigit(e.getKeyChar())
                        || e.getKeyChar() == KeyEvent.VK_BACK_SPACE
                        && ((JTable) ((JTextField) e.getComponent()).getParent()).getSelectedColumn() == 6);
            }
        });

        jtInventoryDoc.addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent e) {
                int r = jtInventoryDoc.getSelectedRow();
                int c = jtInventoryDoc.getSelectedColumn();
                if (r != -1 && c != -1) {
                    jTextBox.setEditable(Character.isDigit(e.getKeyChar())
                            || e.getKeyChar() == KeyEvent.VK_BACK_SPACE);
                }
            }
        });

        showTable();

        if (productCode != null && table.size() > 0) {
            for (int i = 0; i < table.size(); i++) {
                if (((Inventorytable) table.get(i)).getProductCode() == productCode) {
                    Rectangle rect = jtInventoryDoc.getCellRect(i, 1, true);
                    jtInventoryDoc.scrollRectToVisible(rect);
                    jtInventoryDoc.getSelectionModel().addSelectionInterval(i, i);
                    jtInventoryDoc.getColumnModel().getSelectionModel().setSelectionInterval(1, 1);
                    break;
                }
            }
        } else {
            Util.selectFirstRow(jtInventoryDoc);
        }

        //set move one cell down on enter
        Util.setMoveDown(jtInventoryDoc);

        ActionListener taskPerformer = new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                processScan();
                scanCode = "";
                scanEnter = false;
                ((Timer) evt.getSource()).stop();
            }
        };

        elapse = new Timer(AUTO_KEY_PRESS_TIMEOUT, taskPerformer);
        scanEnter = false;
        scanCode = "";

        ActionListener filterTaskPerformer = new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                processFilter();
                filterEnter = false;
                ((Timer) evt.getSource()).stop();
            }
        };

        elapseFilter = new Timer(KEY_PRESS_TIMEOUT, filterTaskPerformer);
        filterEnter = false;

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
        jLabel3 = new javax.swing.JLabel();
        jtfEmployee = new javax.swing.JTextField();
        jbChooseEmployee = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jtInventoryDoc = new javax.swing.JTable() {

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
        jlIncomingDoc = new javax.swing.JLabel();
        jlOutcomingDoc = new javax.swing.JLabel();
        jbCreateDocs = new javax.swing.JButton();
        jbIncomingDoc = new javax.swing.JButton();
        jbOutcomingDoc = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jtfFilter = new javax.swing.JTextField();
        jbUp = new javax.swing.JButton();
        jbDown = new javax.swing.JButton();
        jbRecompute = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        jsTime = new javax.swing.JSpinner();

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance().getContext().getResourceMap(InventoryDocView.class);
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
            .addGap(0, 20, Short.MAX_VALUE)
        );

        jLabel3.setText(resourceMap.getString("jLabel3.text")); // NOI18N
        jLabel3.setName("jLabel3"); // NOI18N

        jtfEmployee.setBackground(resourceMap.getColor("jtfEmployee.background")); // NOI18N
        jtfEmployee.setEditable(false);
        jtfEmployee.setText(resourceMap.getString("jtfEmployee.text")); // NOI18N
        jtfEmployee.setName("jtfEmployee"); // NOI18N

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance().getContext().getActionMap(InventoryDocView.class, this);
        jbChooseEmployee.setAction(actionMap.get("chooseEmployee")); // NOI18N
        jbChooseEmployee.setText(resourceMap.getString("jbChooseEmployee.text")); // NOI18N
        jbChooseEmployee.setName("jbChooseEmployee"); // NOI18N

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        jtInventoryDoc.setModel(new javax.swing.table.DefaultTableModel(
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
        jtInventoryDoc.setCellSelectionEnabled(true);
        jtInventoryDoc.setFillsViewportHeight(true);
        jtInventoryDoc.setName("jtInventoryDoc"); // NOI18N
        jtInventoryDoc.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jtInventoryDocMouseClicked(evt);
            }
        });
        jtInventoryDoc.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jtInventoryDocKeyTyped(evt);
            }
        });
        jScrollPane1.setViewportView(jtInventoryDoc);

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

        jlIncomingDoc.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jlIncomingDoc.setText(resourceMap.getString("jlIncomingDoc.text")); // NOI18N
        jlIncomingDoc.setName("jlIncomingDoc"); // NOI18N

        jlOutcomingDoc.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jlOutcomingDoc.setText(resourceMap.getString("jlOutcomingDoc.text")); // NOI18N
        jlOutcomingDoc.setName("jlOutcomingDoc"); // NOI18N

        jbCreateDocs.setAction(actionMap.get("createDocs")); // NOI18N
        jbCreateDocs.setText(resourceMap.getString("jbCreateDocs.text")); // NOI18N
        jbCreateDocs.setName("jbCreateDocs"); // NOI18N

        jbIncomingDoc.setAction(actionMap.get("showIncomingDoc")); // NOI18N
        jbIncomingDoc.setText(resourceMap.getString("jbIncomingDoc.text")); // NOI18N
        jbIncomingDoc.setName("jbIncomingDoc"); // NOI18N

        jbOutcomingDoc.setAction(actionMap.get("showOutcomingDoc")); // NOI18N
        jbOutcomingDoc.setText(resourceMap.getString("jbOutcomingDoc.text")); // NOI18N
        jbOutcomingDoc.setName("jbOutcomingDoc"); // NOI18N

        jButton1.setAction(actionMap.get("addElements")); // NOI18N
        jButton1.setText(resourceMap.getString("jButton1.text")); // NOI18N
        jButton1.setName("jButton1"); // NOI18N

        jButton2.setAction(actionMap.get("showPrint")); // NOI18N
        jButton2.setText(resourceMap.getString("jButton2.text")); // NOI18N
        jButton2.setName("jButton2"); // NOI18N

        jLabel4.setText(resourceMap.getString("jLabel4.text")); // NOI18N
        jLabel4.setName("jLabel4"); // NOI18N

        jtfFilter.setText(resourceMap.getString("jtfFilter.text")); // NOI18N
        jtfFilter.setName("jtfFilter"); // NOI18N
        jtfFilter.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jtfFilterKeyReleased(evt);
            }
        });

        jbUp.setAction(actionMap.get("up")); // NOI18N
        jbUp.setText(resourceMap.getString("jbUp.text")); // NOI18N
        jbUp.setName("jbUp"); // NOI18N

        jbDown.setAction(actionMap.get("down")); // NOI18N
        jbDown.setText(resourceMap.getString("jbDown.text")); // NOI18N
        jbDown.setName("jbDown"); // NOI18N

        jbRecompute.setAction(actionMap.get("recompute")); // NOI18N
        jbRecompute.setText(resourceMap.getString("jbRecompute.text")); // NOI18N
        jbRecompute.setName("jbRecompute"); // NOI18N

        jLabel5.setText(resourceMap.getString("jLabel5.text")); // NOI18N
        jLabel5.setName("jLabel5"); // NOI18N

        jsTime.setName("jsTime"); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 831, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(jbAdd)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jbDelete)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 20, Short.MAX_VALUE)
                        .addComponent(jbCreateDocs)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jbSave)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jbCancel))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jtfNumber, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jpDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel5))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(31, 31, 31)
                                .addComponent(jlIncomingDoc, javax.swing.GroupLayout.PREFERRED_SIZE, 330, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addComponent(jsTime, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jtfEmployee, javax.swing.GroupLayout.DEFAULT_SIZE, 244, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addComponent(jbIncomingDoc, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jlOutcomingDoc, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jbOutcomingDoc, 0, 0, Short.MAX_VALUE)
                            .addComponent(jbChooseEmployee, javax.swing.GroupLayout.PREFERRED_SIZE, 26, Short.MAX_VALUE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(jbUp)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jbDown)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jtfFilter, javax.swing.GroupLayout.DEFAULT_SIZE, 490, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jbRecompute)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jbChooseEmployee)
                        .addComponent(jtfEmployee, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel5)
                        .addComponent(jsTime, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel3))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jpDate, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jtfNumber, javax.swing.GroupLayout.Alignment.LEADING)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jbIncomingDoc)
                        .addComponent(jlIncomingDoc, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jlOutcomingDoc, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jbOutcomingDoc, javax.swing.GroupLayout.Alignment.LEADING)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 476, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jtfFilter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jbUp)
                    .addComponent(jbDown)
                    .addComponent(jLabel4)
                    .addComponent(jbRecompute))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jbCancel)
                        .addComponent(jbSave)
                        .addComponent(jbAdd)
                        .addComponent(jbDelete)
                        .addComponent(jbCreateDocs)
                        .addComponent(jButton2))
                    .addComponent(jButton1))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jtInventoryDocMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtInventoryDocMouseClicked

        if (evt.getClickCount() > 1) {
            int r = jtInventoryDoc.getSelectedRow();
            if (r != -1) {
                if (jtInventoryDoc.columnAtPoint(evt.getPoint()) < 2) {
                    NomenclatureView nv =
                            new NomenclatureView(
                            salesView, this,
                            ((Inventorytable) table.get(r)).getProductCode(),
                            jtInventoryDoc.getSelectedRow(), "", "", null, false);
                    salesView.getJDesktopPane().add(nv, javax.swing.JLayeredPane.DEFAULT_LAYER);
                    nv.setVisible(true);
                    try {
                        nv.setSelected(true);
                    } catch (java.beans.PropertyVetoException e) {
                        logger.error(e);
                    }
                }
            }
        }
    }//GEN-LAST:event_jtInventoryDocMouseClicked

    private void processScan() {
        Session session = HUtil.getSession();
        String hql = "from Nomenclature n where n.scanCode = '" + scanCode + "'";
        List res = session.createQuery(hql).list();
        if (res.size() > 0) {
            Nomenclature n = (Nomenclature) res.get(0);
            addProductItem(n.getCode());
            try {
                BufferedWriter fw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path), "cp1251"));
                fw.write(n.getPrice().toString() + " ");
                Integer inPrice = (int) Math.round((double) n.getInPrice()
                        * (100 + HUtil.getIntConstant("nds")) / 100 * 1.06);
                fw.write(inPrice.toString() + " ");
                fw.close();
            } catch (Exception e) {
                e.printStackTrace();
                logger.error(e);
            }
        }
        session.close();
    }

    private void jtInventoryDocKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtInventoryDocKeyTyped

        if (evt.getKeyCode() == 155) {
            add();
        }
        if (evt.getKeyCode() == 127) {
            del();
        }

        if (scanEnter) {
            scanCode += evt.getKeyChar();
            if (elapse.isRunning()) {
                elapse.restart();
            } else {
                elapse.start();
            }
        } else {
            scanCode = "" + evt.getKeyChar();
            scanEnter = true;
        }

    }//GEN-LAST:event_jtInventoryDocKeyTyped

    private void jtfFilterKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtfFilterKeyReleased

        if (filterEnter) {
            if (elapseFilter.isRunning()) {
                elapseFilter.restart();
            } else {
                elapseFilter.start();
            }
        } else {
            filterEnter = true;
        }

    }//GEN-LAST:event_jtfFilterKeyReleased

    @Action
    public void add() {
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
    public void del() {
        if (jtInventoryDoc.getSelectedRow() >= 0) {
            int i = jtInventoryDoc.getSelectionModel().getMinSelectionIndex();
            while (i <= jtInventoryDoc.getSelectionModel().getMaxSelectionIndex()) {
                if (jtInventoryDoc.getSelectionModel().isSelectedIndex(i)) {
                    table.remove(i);
                    ((DefaultTableModel) jtInventoryDoc.getModel()).removeRow(i);
                } else {
                    i++;
                }
            }
        }
    }

    @Action
    public void save() {

        if (lock == 0) {

            TableCellEditor tce = jtInventoryDoc.getCellEditor();
            if (tce != null) {
                tce.stopCellEditing();
            }

            if (hash == null || !hash.equals(Util.hash(this))) {
                int a = JOptionPane.showOptionDialog(
                        this, "Сохранить документ?", "Сохранение", JOptionPane.YES_NO_CANCEL_OPTION,
                        JOptionPane.QUESTION_MESSAGE, null, new Object[]{"Да", "Нет"}, "Да");
                if (a == 0) {
                    try {
                        Session session = HUtil.getSession();
                        session.beginTransaction();

                        Inventory i = null;
                        if (documentCode != null) {
                            i = (Inventory) HUtil.getElement("Inventory", documentCode, session);
                        }
                        if (i == null) {
                            i = new Inventory();
                        }

                        i.setDatetime(Util.composeDate(date.getDate(), (Date) jsTime.getValue()));
                        i.setNumber(jtfNumber.getText());
                        if (employeeCode != null) {
                            i.setEmployeeCode(employeeCode);
                        }
                        i.setIncomingCode(incomingDoc);
                        i.setOutcomingCode(outcomingDoc);
                        i.setActive(active);

                        if (documentCode != null) {
                            session.merge(i);
                        } else {
                            session.save(i);
                        }

                        session.getTransaction().commit();

                        session.beginTransaction();

                        documentCode = i.getCode();

                        List objs = session.createQuery("from Inventorytable it where it.documentCode = " + documentCode).list();

                        if (table != null) {
                            for (int j = 0; j < table.size(); j++) {
                                Inventorytable it = (Inventorytable) table.get(j);
                                it.setDocumentCode(documentCode);
                                it.setLine(j + 1);
                                boolean newIn = true;
                                for (int k = 0; k < objs.size(); k++) {
                                    if (((Inventorytable) objs.get(k)).getCode() == it.getCode()) {
                                        newIn = false;
                                        objs.remove(k);
                                        break;
                                    }
                                }
                                if (newIn) {
                                    session.save(it);
                                } else {
                                    session.merge(it);
                                }
                            }
                        }

                        for (int j = 0; j < objs.size(); j++) {
                            session.delete((Inventorytable) objs.get(j));
                        }

                        session.getTransaction().commit();

                        session.close();

                    } catch (Exception e) {
                        logger.error(e);
                    }
                    Util.updateJournals(salesView, InventoryView.class);
                    hash = Util.hash(this);
                }
            }
        }
    }

    public void close() {

        if (lock == 0) {

            save();

            Session session = HUtil.getSession();
            Inventory x = (Inventory) HUtil.getElement("Inventory", documentCode, session);
            if (x != null) {
                session.beginTransaction();
                x.setIncomingCode(incomingDoc);
                x.setOutcomingCode(outcomingDoc);
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
        Util.closeJIF(this, inventoryView, salesView);
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
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton jbAdd;
    private javax.swing.JButton jbCancel;
    private javax.swing.JButton jbChooseEmployee;
    private javax.swing.JButton jbCreateDocs;
    private javax.swing.JButton jbDelete;
    private javax.swing.JButton jbDown;
    private javax.swing.JButton jbIncomingDoc;
    private javax.swing.JButton jbOutcomingDoc;
    private javax.swing.JButton jbRecompute;
    private javax.swing.JButton jbSave;
    private javax.swing.JButton jbUp;
    private javax.swing.JLabel jlIncomingDoc;
    private javax.swing.JLabel jlOutcomingDoc;
    private javax.swing.JPanel jpDate;
    private javax.swing.JSpinner jsTime;
    private javax.swing.JTable jtInventoryDoc;
    private javax.swing.JTextField jtfEmployee;
    private javax.swing.JTextField jtfFilter;
    private javax.swing.JTextField jtfNumber;
    // End of variables declaration//GEN-END:variables
    private SalesView salesView;
    private InventoryView inventoryView;
    private Integer documentCode;
    private JDateChooser date;
    private List table;
    private JTextField jTextBox;
    private Integer active;
    private Timer elapse;
    private String scanCode;
    private boolean scanEnter;
    private Timer elapseFilter;
    private boolean filterEnter;
    private Integer incomingDoc;
    private Integer outcomingDoc;
    private Integer employeeCode;
    private Integer lock;
    private Integer hash;

    private class MyTableModel extends DefaultTableModel {

        public MyTableModel(Vector table, Vector header) {
            super(table, header);
        }

        @Override
        public boolean isCellEditable(int r, int c) {
            return c == 6;
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
        tableHeaders.add("Новое кол-во");
        tableHeaders.add("Новая сумма");
        tableHeaders.add("Недостача кол-во");
        tableHeaders.add("Недостача сумма");
        tableHeaders.add("Избыток кол-во");
        tableHeaders.add("Избыток сумма");

        Integer sum = 0;
        Session session = HUtil.getSession();
        for (int i = 0; i < table.size(); i++) {
            Inventorytable in = (Inventorytable) table.get(i);
            Vector<Object> oneRow = new Vector<Object>();
            oneRow.add(i + 1);
            Nomenclature n = (Nomenclature) HUtil.getElement("Nomenclature", in.getProductCode(), session);
            if (n != null) {
                oneRow.add(n.getName());
                oneRow.add(n.getUnit());
            } else {
                oneRow.add("");
                oneRow.add("");
            }
            oneRow.add(in.getPrice());
            int q = in.getQuantity();
            oneRow.add(q);
            oneRow.add(q * in.getPrice());
            int nq = in.getNewQuantity();
            oneRow.add(nq);
            oneRow.add(nq * in.getPrice());
            if (q > nq) {
                oneRow.add(q - nq);
                oneRow.add((q - nq) * in.getPrice());
            } else if (q < nq) {
                oneRow.add("");
                oneRow.add("");
                oneRow.add(nq - q);
                oneRow.add((nq - q) * in.getPrice());
            }
            tableData.add(oneRow);
        }
        session.close();

        jtInventoryDoc.setModel(new MyTableModel(tableData, tableHeaders));
        tuneTable();

        setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

    }

    private void tuneTable() {

        Util.autoResizeColWidth(jtInventoryDoc);

        jtInventoryDoc.getColumnModel().getColumn(6).setCellEditor(new MyCellEditor(jTextBox));

        jtInventoryDoc.getModel().addTableModelListener(new TableModelListener() {

            @Override
            public void tableChanged(TableModelEvent e) {
                int r = e.getFirstRow();
                int c = e.getColumn();
                if (r != -1) {
                    if (e.getColumn() == 6) {
                        Inventorytable it = (Inventorytable) table.get(r);
                        it.setNewQuantity(new Integer(jtInventoryDoc.getValueAt(r, 6).toString()));
                        table.set(r, it);
                        showTable();
                        Util.moveCell(r, c, jtInventoryDoc, true);
                    }
                }
            }
        });
    }

    @Override
    public void addProductItem(Integer productCode) {

        for (int i = 0; i < table.size(); i++) {
            if (((Inventorytable) table.get(i)).getProductCode() == productCode) {
                Util.go2rect(jtInventoryDoc, i);
                return;
            }
        }

        try {

            Session session = HUtil.getSession();
            Nomenclature n = (Nomenclature) HUtil.getElement("Nomenclature", productCode, session);
            if (n != null) {

                int quantity = HUtil.getBalance(productCode);

                Object oNewQuantity =
                        JOptionPane.showInputDialog(
                        null,
                        "Введите количество по факту",
                        "Количество",
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        null,
                        quantity);

                if (Util.checkNumber((String) oNewQuantity)) {

                    int newQuantity = Util.str2int(oNewQuantity.toString());

                    if (quantity > 0 || newQuantity > 0) {

                        Inventorytable it = new Inventorytable();

                        it.setProductCode(productCode);

                        if (documentCode != null) {
                            it.setDocumentCode(documentCode);
                        }

                        it.setProductCode(productCode);
                        it.setQuantity(quantity);
                        it.setNewQuantity(newQuantity);
                        it.setPrice(n.getPrice());

                        table.add(it);

                        showTable();
                        Util.go2rect(jtInventoryDoc, jtInventoryDoc.getRowCount() - 1);
                        //jtInventoryDoc.requestFocus();

                    }
                }

                session.close();

            }

        } catch (Exception e) {
            logger.error(e);
        }

    }

    @Override
    public void setProductItem(Integer productCode, int r) {

        for (int i = 0; i < table.size(); i++) {
            if (((Inventorytable) table.get(i)).getProductCode() == productCode) {
                Util.go2rect(jtInventoryDoc, i);
                return;
            }
        }

        try {
            Session session = HUtil.getSession();
            Inventorytable it = (Inventorytable) table.get(r);
            Nomenclature n = (Nomenclature) HUtil.getElement("Nomenclature", productCode, session);
            if (n == null) {
                return;
            }

            it.setProductCode(productCode);
            it.setPrice(n.getPrice());
            it.setQuantity(HUtil.getBalance(productCode));

            table.set(r, it);

            session.close();
        } catch (Exception e) {
            logger.error(e);
        }
        showTable();
    }

    @Override
    public void setProductItemPart(Integer productCode, int row) {
        setProductItem(productCode, row);
    }

    @Override
    public void setFocus() {
        jtInventoryDoc.setRequestFocusEnabled(true);
        jtInventoryDoc.requestFocusInWindow();
    }

    public void createIncomingDoc() {
        Session session = HUtil.getSession();
        Incoming in = new Incoming();
        boolean notExists = true;
        if (incomingDoc != null) {
            in = (Incoming) HUtil.getElement("Incoming", incomingDoc, session);
            if (in != null) {
                session.beginTransaction();
                String hql = "from Incomingtable it where it.documentCode = " + in.getCode();
                List it = session.createQuery(hql).list();
                for (int j = 0; j < it.size(); j++) {
                    session.delete((Incomingtable) it.get(j));
                }
                session.getTransaction().commit();
                notExists = false;
            }
        }

        if (notExists) {
            session.beginTransaction();
            in = new Incoming();
            in.setDatetime(date.getDate());
            in.setNumber(HUtil.getNextDocNumber("Incoming", "x.type = 1", session));
            in.setType(1);
            in.setActive(1);
            session.save(in);
            session.getTransaction().commit();
        }

        incomingDoc = in.getCode();

        session.beginTransaction();

        for (int i = 0; i < table.size(); i++) {
            Inventorytable invt = (Inventorytable) table.get(i);
            if (invt.getQuantity() < invt.getNewQuantity()) {
                Incomingtable it = new Incomingtable();
                it.setProductCode(invt.getProductCode());
                it.setDocumentCode(incomingDoc);
                Nomenclature n = (Nomenclature) HUtil.getElement("Nomenclature", invt.getProductCode(), session);
                it.setProductName(n.getName());
                it.setQuantity(invt.getNewQuantity() - invt.getQuantity());
                n.setQuantity(invt.getNewQuantity());
                it.setInPrice(n.getInPrice());
                it.setInAmount(it.getInPrice() * it.getQuantity());
                it.setNds(HUtil.getConstantDouble("nds").intValue());
                it.setNdsAmount(it.getInAmount() * it.getNds() / 100);
                it.setNdsAndAmount(it.getInAmount() + it.getNdsAmount());
                it.setPrice(invt.getPrice());
                it.setAmount(invt.getPrice() * it.getQuantity());
                it.setSurcharge(it.getPrice() - it.getInPrice() * (100 + it.getNds()) / 100);
                if (it.getInPrice() * (100 + it.getNds()) != 0) {
                    it.setPerSurcharge(((double) it.getSurcharge() * 100) / (it.getInPrice() * (100 + it.getNds())));
                }
                session.saveOrUpdate(it);
                session.update(n);
            }
        }

        in.setActive(1);
        session.update(in);

        session.getTransaction().commit();

        showDocs(session);

        session.close();

    }

    public void createOutcomingDoc() {
        Session session = HUtil.getSession();
        Outcoming out = new Outcoming();
        boolean notExists = true;
        if (outcomingDoc != null) {
            out = (Outcoming) HUtil.getElement("Outcoming", outcomingDoc, session);
            if (out != null) {
                session.beginTransaction();
                String hql = "from Outcomingtable ot where ot.documentCode = " + out.getCode();
                List ot = session.createQuery(hql).list();
                for (int j = 0; j < ot.size(); j++) {
                    session.delete((Outcomingtable) ot.get(j));
                }
                session.getTransaction().commit();
                notExists = false;
            }
        }

        if (notExists) {
            session.beginTransaction();
            out = new Outcoming();
            out.setDatetime(date.getDate());
            out.setNumber(HUtil.getNextDocNumber("Outcoming", "x.documentType = 3", session));
            out.setDocumentType(3);
            out.setActive(1);
            session.save(out);
            session.getTransaction().commit();
        }

        outcomingDoc = out.getCode();

        session.beginTransaction();

        for (int i = 0; i < table.size(); i++) {
            Inventorytable invt = (Inventorytable) table.get(i);
            if (invt.getQuantity() > invt.getNewQuantity()) {
                Outcomingtable ot = new Outcomingtable();
                ot.setProductCode(invt.getProductCode());
                ot.setDocumentCode(outcomingDoc);
                Nomenclature n = (Nomenclature) HUtil.getElement("Nomenclature", invt.getProductCode(), session);
                ot.setQuantity(invt.getQuantity() - invt.getNewQuantity());
                n.setQuantity(invt.getNewQuantity());
                ot.setInPrice(n.getInPrice());
                ot.setPrice(invt.getPrice());
                ot.setAmount(ot.getPrice() * ot.getQuantity());
                ot.setSurcharge(ot.getPrice() - ot.getInPrice());
                session.saveOrUpdate(ot);
                session.update(n);
            }
        }

        out.setActive(1);
        session.update(out);

        session.getTransaction().commit();

        showDocs(session);

        session.close();

    }

    @Action
    public void createDocs() {

        Incoming i = (Incoming) HUtil.getElement("Incoming", incomingDoc);
        if (i == null) {
            incomingDoc = null;
        }

        if (incomingDoc != null) {
            int a = JOptionPane.showOptionDialog(
                    this,
                    "Документ прихода существует! Создать заново?",
                    "Приход",
                    JOptionPane.YES_NO_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    new Object[]{"Да", "Нет"}, "Нет");
            if (a == 0) {
                createIncomingDoc();
                Util.updateJournals(salesView, IncomingView.class);
            }
        } else {
            createIncomingDoc();
            Util.updateJournals(salesView, IncomingView.class);
        }

        Outcoming o = (Outcoming) HUtil.getElement("Outcoming", outcomingDoc);
        if (o == null) {
            outcomingDoc = null;
        }

        if (outcomingDoc != null) {
            int a = JOptionPane.showOptionDialog(
                    this,
                    "Документ расхода существует! Создать заново?",
                    "Расход",
                    JOptionPane.YES_NO_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    new Object[]{"Да", "Нет"}, "Нет");
            if (a == 0) {
                createOutcomingDoc();
                Util.updateJournals(salesView, OutcomingView.class);
            }
        } else {
            createOutcomingDoc();
            Util.updateJournals(salesView, OutcomingView.class);
        }
    }

    @Override
    public void setEmployee(Integer employeeCode, String name) {
        this.employeeCode = employeeCode;
        Session session = HUtil.getSession();
        jtfEmployee.setText(((Employee) HUtil.getElement("Employee", employeeCode, session)).getName());
        session.close();
    }

    private void showDocs(Session session) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yy");
        if (incomingDoc != null) {
            Incoming in = (Incoming) HUtil.getElement("Incoming", incomingDoc, session);
            if (in != null) {
                jlIncomingDoc.setText("Приход № " + in.getNumber() + " от " + sdf.format(in.getDatetime()));
            }
        } else {
            jlIncomingDoc.setText("");
        }
        if (outcomingDoc != null) {
            Outcoming out = (Outcoming) HUtil.getElement("Outcoming", outcomingDoc, session);
            if (out != null) {
                jlOutcomingDoc.setText("Расход № " + out.getNumber() + " от " + sdf.format(out.getDatetime()));
            }
        } else {
            jlOutcomingDoc.setText("");
        }
    }

    @Action
    public void showIncomingDoc() {
        if (incomingDoc != null) {
            IncomingDocView doc =
                    new IncomingDocView(salesView, null, incomingDoc, null);
            salesView.getJDesktopPane().add(doc, javax.swing.JLayeredPane.DEFAULT_LAYER);
            doc.setVisible(true);
            try {
                doc.setSelected(true);
            } catch (Exception e) {
                logger.error(e);
            }
            doc.setFocus();
        }
    }

    @Action
    public void showOutcomingDoc() {
        if (outcomingDoc != null) {
            OutcomingDocView doc =
                    new OutcomingDocView(salesView, null, outcomingDoc, 3, false, null);
            salesView.getJDesktopPane().add(doc, javax.swing.JLayeredPane.DEFAULT_LAYER);
            doc.setVisible(true);
            try {
                doc.setSelected(true);
            } catch (Exception e) {
                logger.error(e);
            }
            doc.setFocus();
        }
    }

    @Action
    public void addElements() {

        List res = HUtil.executeHql("from Nomenclature x where x.active = 0");

        int firstSel = 0;
        boolean setFirstSel = true;

        for (int i = 0; i < res.size(); i++) {

            Nomenclature n = (Nomenclature) res.get(i);
            Integer balance = HUtil.getWholeBalanceAndProduct(
                    Util.composeDate(date.getDate(), (Date) jsTime.getValue()), true, n.getCode());

            if (balance != 0) {

                boolean found = false;
                for (int j = 0; j < table.size(); j++) {
                    if (((Inventorytable) table.get(j)).getProductCode() == n.getCode()) {
                        found = true;
                        break;
                    }
                }

                if (!found) {
                    Inventorytable it = new Inventorytable();
                    it.setProductCode(n.getCode());
                    it.setPrice(n.getPrice());
                    it.setQuantity(balance);
                    table.add(it);
                    if (setFirstSel) {
                        firstSel = table.size();
                        setFirstSel = false;
                    }
                }
            }
        }
        showTable();
        if (!setFirstSel) {
            Rectangle rect = jtInventoryDoc.getCellRect(table.size() - 1, 1, true);
            jtInventoryDoc.scrollRectToVisible(rect);
            jtInventoryDoc.getSelectionModel().setSelectionInterval(firstSel - 1, table.size() - 1);
            jtInventoryDoc.getColumnModel().getSelectionModel().setSelectionInterval(1, 1);
        }

    }

    @Action
    public void showPrint() {

        try {

            File file = new File(Util.getAppPath() + "\\templates\\Inventory.ods");
            final Sheet sheet = SpreadSheet.createFromFile(file).getSheet(0);

            sheet.getCellAt("A3").setValue(
                    "Мы, комиссия в составе: директор " + HUtil.getShortNameByCode(HUtil.getIntConstant("director"))
                    + ", гл. бух. " + HUtil.getShortNameByCode(HUtil.getIntConstant("chiefAccountant"))
                    + " произвели инвентаризацию товара:");

            int ts = table.size();
            if (ts > 0) {
                sheet.duplicateRows(7, 1, ts - 1);
            }

            //List tableIn = HUtil.getBalanceNomenclature(date.getDate(), date.getDate());

            Session session = HUtil.getSession();

            int amount = 0;
            int newAmount = 0;
            int lackAmount = 0;
            int overstockAmount = 0;
            int i;
            for (i = 0; i < ts; i++) {

                Inventorytable it = (Inventorytable) table.get(i);
                Nomenclature n = (Nomenclature) HUtil.getElement("Nomenclature", it.getProductCode(), session);

                /*
                boolean found = false;
                for(int j = 0; j < tableIn.size(); j++) {
                Object[] row = (Object[]) tableIn.get(j);
                if (Util.getIntObj(row[0]) == n.getCode()) {
                found = true;
                int balance = Util.getIntObj(row[3]) - Util.getIntObj(row[4]) + Util.getIntObj(row[5]) - Util.getIntObj(row[6]);
                if(balance != it.getNewQuantity()) {
                System.out.println(n.getName() + " " + balance + " " + it.getNewQuantity());
                }
                }
                }
                if(!found) {
                System.out.println(n.getName() + " not found!!!");
                }
                 */

                sheet.getCellAt("A" + (8 + i)).setValue(i + 1);
                sheet.getCellAt("B" + (8 + i)).setValue(n.getName());
                sheet.getCellAt("C" + (8 + i)).setValue(n.getUnit());
                sheet.getCellAt("D" + (8 + i)).setValue(it.getPrice());
                sheet.getCellAt("E" + (8 + i)).setValue(it.getQuantity());
                sheet.getCellAt("F" + (8 + i)).setValue(it.getPrice() * it.getQuantity());
                amount += it.getPrice() * it.getQuantity();
                sheet.getCellAt("G" + (8 + i)).setValue(it.getNewQuantity());
                sheet.getCellAt("H" + (8 + i)).setValue(it.getPrice() * it.getNewQuantity());
                newAmount += it.getPrice() * it.getNewQuantity();
                int diff = it.getNewQuantity() - it.getQuantity();
                if (diff < 0) {
                    sheet.getCellAt("I" + (8 + i)).setValue(-diff);
                    sheet.getCellAt("J" + (8 + i)).setValue(-diff * it.getPrice());
                    lackAmount -= diff * it.getPrice();
                } else if (diff > 0) {
                    sheet.getCellAt("K" + (8 + i)).setValue(diff);
                    sheet.getCellAt("L" + (8 + i)).setValue(diff * it.getPrice());
                    overstockAmount += diff * it.getPrice();
                }
            }


            session.close();

            sheet.getCellAt("F" + (8 + i)).setValue(amount);
            sheet.getCellAt("H" + (8 + i)).setValue(newAmount);
            sheet.getCellAt("J" + (8 + i)).setValue(lackAmount);
            sheet.getCellAt("L" + (8 + i)).setValue(overstockAmount);
            sheet.getCellAt("B" + (10 + i)).setValue("Сумма переоценки составила: " + Util.money2str(newAmount));
            sheet.getCellAt("D" + (12 + i)).setValue(
                    "_____________________ /" + HUtil.getShortNameByCode(HUtil.getIntConstant("director")) + "/");
            sheet.getCellAt("D" + (13 + i)).setValue(
                    "_____________________ /" + HUtil.getShortNameByCode(HUtil.getIntConstant("chiefAccountant")) + "/");


            String r = " " + Math.round(Math.random() * 100000);
            String fileName = "temp\\Инвентаризация № " + jtfNumber.getText() + r + ".ods";
            File outputFile = new File(fileName);
            sheet.getSpreadSheet().saveAs(outputFile);
            Util.openDoc("temp\\Инвентаризация № " + jtfNumber.getText() + r + ".ods");

        } catch (Exception e) {
            System.err.println(e);
            e.printStackTrace();
            logger.error(e);
        }
    }

    public void processFilter() {

        jtInventoryDoc.getSelectionModel().clearSelection();
        jtInventoryDoc.getColumnModel().getSelectionModel().clearSelection();

        if (!jtfFilter.getText().trim().isEmpty()) {

            StringTokenizer st = new StringTokenizer(jtfFilter.getText().trim());
            ArrayList<String> ts = new ArrayList<>();
            while (st.hasMoreTokens()) {
                ts.add(st.nextElement().toString().toLowerCase());
            }

            jtInventoryDoc.getColumnModel().getSelectionModel().setSelectionInterval(0, 12);
            boolean first = true;
            for (int i = 0; i < table.size(); i++) {
                Nomenclature n = (Nomenclature) HUtil.getElement("Nomenclature", ((Inventorytable) table.get(i)).getProductCode());
                String productName = n.getName().toLowerCase();
                String price = "" + ((Inventorytable) table.get(i)).getPrice();
                String sCode = n.getScanCode();
                if (sCode == null) {
                    sCode = "";
                }
                boolean mark = true;
                for (int j = 0; j < ts.size(); j++) {
                    String tss = ts.get(j);
                    if (!productName.contains(tss.toLowerCase()) && !price.contains(tss) && !sCode.contains(tss)) {
                        mark = false;
                        break;
                    }
                }
                if (mark) {
                    jtInventoryDoc.getSelectionModel().addSelectionInterval(i, i);
                    if (first) {
                        Rectangle rect = jtInventoryDoc.getCellRect(i, 1, true);
                        jtInventoryDoc.scrollRectToVisible(rect);
                        first = false;
                    }
                }
            }
        }
    }

    @Action
    public void up() {
        if (lock == 0) {
            int r = jtInventoryDoc.getSelectedRow();
            if (r != -1 && r > 0) {
                Collections.swap(table, r, r - 1);
                Util.swapRows(jtInventoryDoc, r, r - 1);
                tuneTable();
                Util.move2row(jtInventoryDoc, r - 1);
            }
        }
    }

    @Action
    public void down() {
        if (lock == 0) {
            int r = jtInventoryDoc.getSelectedRow();
            if (r != -1 && r < table.size() - 1) {
                Collections.swap(table, r, r + 1);
                Util.swapRows(jtInventoryDoc, r, r + 1);
                tuneTable();
                Util.move2row(jtInventoryDoc, r + 1);
            }
        }
    }

    @Action
    public void recompute() {

        Session session = HUtil.getSession();

        Incoming in = (Incoming) HUtil.getElement("Incoming", incomingDoc);
        Outcoming out = (Outcoming) HUtil.getElement("Outcoming", outcomingDoc);

        if (in != null) {
            session.beginTransaction();
            in.setActive(0);
            session.update(in);
            session.getTransaction().commit();
        }
        if (out != null) {
            session.beginTransaction();
            out.setActive(0);
            session.update(out);
            session.getTransaction().commit();
        }

        for (int i = 0; i < table.size(); i++) {

            Inventorytable it = (Inventorytable) table.get(i);
            Nomenclature n = (Nomenclature) HUtil.getElement("Nomenclature", it.getProductCode());
            int q = HUtil.getWholeBalanceAndProduct(
                    Util.composeDate(date.getDate(), (Date) jsTime.getValue()), true, n.getCode());
            it.setQuantity(q);
            table.set(i, it);
        }

        if (in != null) {
            session.beginTransaction();
            in.setActive(1);
            session.update(in);
            session.getTransaction().commit();
        }
        if (out != null) {
            session.beginTransaction();
            out.setActive(1);
            session.update(out);
            session.getTransaction().commit();
        }

        showTable();

        JOptionPane.showMessageDialog(this, "Прересчет начальных остатков выполнен!");

    }
}