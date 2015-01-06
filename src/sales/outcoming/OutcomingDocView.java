/*
 * OutcomingView.java
 *
 * Created on 07.05.2011, 14:59:59
 */
package sales.outcoming;

import sales.interfaces.IOutcoming;
import com.toedter.calendar.JDateChooser;
import java.awt.Rectangle;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;
import javax.swing.GroupLayout;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SpinnerDateModel;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import org.apache.log4j.Logger;
import org.hibernate.classic.Session;
import org.jdesktop.application.Action;
import org.jopendocument.dom.spreadsheet.Sheet;
import org.jopendocument.dom.spreadsheet.SpreadSheet;
import sales.auxiliarly.MyCellEditor;
import sales.interfaces.ISerialsView;
import sales.SalesApp;
import sales.SalesView;
import sales.catalogs.EmployeeView;
import sales.catalogs.NomenclatureView;
import sales.entity.Employee;
import sales.entity.Incoming;
import sales.entity.Outcoming;
import sales.entity.Outcomingtable;
import sales.entity.Nomenclature;
import sales.entity.Outcomingpayments;
import sales.entity.Serialtable;
import sales.interfaces.IClose;
import sales.interfaces.ICommit;
import sales.interfaces.IEmployee;
import sales.interfaces.IFocus;
import sales.interfaces.IHashAndSave;
import sales.interfaces.IProductItem;
import sales.util.HUtil;
import sales.util.Util;

public class OutcomingDocView extends JInternalFrame
        implements IProductItem, IOutcoming, ISerialsView, IEmployee, IHashAndSave, IClose, ICommit, IFocus {

    static final int TN = 0;
    static final int TTN = 1;
    static final int INVENTORY = 2;
    static final int RESISTER = 3;
    static final int TERMINAL = 4;
    static final int RESISTER_PER_DAY = 5;
    static final int TERMINAL_PER_DAY = 6;
    static Logger logger = Logger.getLogger(SalesApp.class.getName());
    static public int[] types = new int[]{0, 4, 3, 2, 5, 6, 1};

    public OutcomingDocView(SalesView salesView, OutcomingView outcomingView,
            Integer docCode, Integer docType, boolean silent, Integer productCode) {
        initComponents();

        this.salesView = salesView;
        this.outcomingView = outcomingView;
        this.documentCode = docCode;
        this.silent = silent;

        if (!silent) {
            Util.initJIF(this, "Расход", outcomingView, salesView);
            Util.initJTable(jtOutcomingDoc);

        }

        jcbType.addItem("ТН");
        jcbType.addItem("ТТН");
        jcbType.addItem("Инвентаризация");
        jcbType.addItem("Касса");
        jcbType.addItem("Терминал");
        jcbType.addItem("Касса за день");
        jcbType.addItem("Терминал за день");

        jsTime.setModel(new SpinnerDateModel());
        JSpinner.DateEditor timeEditor = new JSpinner.DateEditor(jsTime, "HH:mm");
        jsTime.setEditor(timeEditor);

        jcbType.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent evt) {
                if (evt.getStateChange() == ItemEvent.SELECTED) {
                    typeChanged();
                }
            }
        });

        Session session = HUtil.getSession();

        if (documentCode != null) {

            Outcoming o = (Outcoming) HUtil.getSession().load(Outcoming.class, documentCode);
            active = o.getActive();
            lock = o.getLocked();
            date = new JDateChooser(o.getDatetime());
            jsTime.setValue(o.getDatetime());

            jtfNumber.setText(o.getNumber());
            if (active == 1) {
                jcbActive.setSelected(true);
            } else if (active == 2) {
                jcbActive.setEnabled(false);
            }
            
            employeeCode = o.getEmployee();
            jtfEmployee.setText(HUtil.getShortNameByCode(employeeCode));

            jtfNote.setText(o.getNote());

            table = HUtil.executeHql(
                    "from Outcomingtable out where out.documentCode = " + documentCode + " order by out.line", session);
            List res = HUtil.executeHql(
                    "from Serialtable s where s.documentCode = " + documentCode, session);
            serials = new ArrayList();
            for (int i = 0; i < table.size(); i++) {
                List s = new ArrayList();
                for (int j = 0; j < res.size(); j++) {
                    Serialtable st = (Serialtable) res.get(j);
                    if (st.getPosition() == i) {
                        s.add(st);
                    }
                }
                serials.add(s);
            }

            type = types[o.getDocumentType()];

            jtpHeaders.setVisible(type == 1);

            tnheader = new TNHeader(this, salesView, documentCode, lock, table);
            ttnheader = new TTNHeader(this, salesView, tnheader, documentCode, lock, table);

            if (type == 0 || type == 1) {
                jpMain.add(tnheader, 0);
            }

            tnheader.setTNButtonVisible(type == 0);
            jcbType.setSelectedIndex(type);

            if (!silent) {
                if (lock == 1) {
                    lock = o.getLocked();
                    JOptionPane.showMessageDialog(
                            this,
                            "Элемент редактируется другим пользователем и будет открыт только для просмотра!",
                            "Блокировка", JOptionPane.PLAIN_MESSAGE);
                    jtfNumber.setEditable(false);
                    date.setEnabled(false);
                    jcbActive.setEnabled(false);
                    jbSave.setEnabled(false);
                } else if (!silent) {
                    session.beginTransaction();
                    o.setLocked(1);
                    session.update(o);
                    session.getTransaction().commit();
                }
            } else {
                lock = 0;
            }

        } else {
            jtfNumber.setText(HUtil.getNextDocNumber("Outcoming", "x.documentType = 0", session));
            Calendar cl = Calendar.getInstance();
            date = new JDateChooser(cl.getTime());
            jsTime.setValue(cl.getTime());
            employeeCode = HUtil.getIntConstant("cashier");
            jtfEmployee.setText(HUtil.getShortNameByCode(employeeCode));
            jtfNote.setText("");
            table = new Vector();
            serials = new ArrayList();
            tnheader = new TNHeader(this, salesView, null, 0, table);
            tnheader.nextTNNumber();
            tnheader.nextInvoiceNumber();
            jpMain.add(tnheader, 0);
            jtpHeaders.setVisible(false);
            ttnheader = new TTNHeader(this, salesView, tnheader, null, 0, table);
            active = 0;
            lock = 0;
        }

        session.close();

        jtpHeaders.add("Главная", null);
        jtpHeaders.add("ТТН", null);

        if (!silent) {
            GroupLayout gl = (GroupLayout) jpDate.getLayout();
            gl.setHorizontalGroup(gl.createParallelGroup().addGroup(gl.createSequentialGroup().addComponent(date)));
            gl.setVerticalGroup(gl.createParallelGroup().addGroup(gl.createSequentialGroup().addComponent(date)));

            if (jtOutcomingDoc.getModel().getRowCount() > 0) {
                jtOutcomingDoc.getSelectionModel().setSelectionInterval(0, 0);
            }

            Util.setMoveRight(jtOutcomingDoc);

            jTextBox = new JTextField();
            jTextBox.addKeyListener(new KeyAdapter() {

                @Override
                public void keyTyped(KeyEvent e) {
                    jTextBox.setEditable(
                            Character.isDigit(e.getKeyChar())
                            || e.getKeyChar() == KeyEvent.VK_BACK_SPACE
                            || e.getKeyChar() == '.'
                            && ((JTable) e.getComponent().getParent()).getSelectedColumn() == 7
                            && !((JTextField) e.getComponent()).getText().contains("."));
                }
            });

            jtOutcomingDoc.addKeyListener(new KeyAdapter() {

                @Override
                public void keyPressed(KeyEvent e) {
                    int r = jtOutcomingDoc.getSelectedRow();
                    int c = jtOutcomingDoc.getSelectedColumn();
                    if (r != -1 && c != -1) {
                        jTextBox.setEditable(Character.isDigit(e.getKeyChar())
                                || e.getKeyChar() == KeyEvent.VK_BACK_SPACE
                                || e.getKeyChar() == '.'
                                && ((JTable) e.getComponent().getParent()).getSelectedColumn() == 7
                                && !((JTextField) e.getComponent()).getText().contains("."));
                    }
                }
            });

            scanEnter = false;

            updateTable = true;
            showTable();

            if (productCode != null && table.size() > 0) {
                for (int i = 0; i < table.size(); i++) {
                    if (((Outcomingtable) table.get(i)).getProductCode().equals(productCode)) {
                        Rectangle rect = jtOutcomingDoc.getCellRect(i, 1, true);
                        jtOutcomingDoc.scrollRectToVisible(rect);
                        jtOutcomingDoc.getSelectionModel().addSelectionInterval(i, i);
                        jtOutcomingDoc.getColumnModel().getSelectionModel().setSelectionInterval(1, 1);
                        break;
                    }
                }
            } else {
                Util.selectFirstRow(jtOutcomingDoc);
            }
        }
        
        

        if (documentCode != null) {
            hash = Util.hash(this);
        } else {
            hash = null;
        }

    }

    private class MyTableModel extends DefaultTableModel {

        public MyTableModel(Vector table, Vector header) {
            super(table, header);
        }

        @Override
        public boolean isCellEditable(int r, int c) {

            return (lock == 0 && active != 1 && (c == 3 || c == 4 || type == OutcomingDocView.TTN && (c == 6 || c == 7)));

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

        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jpDate = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        jtfNumber = new javax.swing.JTextField();
        jsTime = new javax.swing.JSpinner();
        jLabel3 = new javax.swing.JLabel();
        jbAddProduct = new javax.swing.JButton();
        jbDeleteItem = new javax.swing.JButton();
        jbFillShipments = new javax.swing.JButton();
        jbSerials = new javax.swing.JButton();
        jbSave = new javax.swing.JButton();
        jbCancel = new javax.swing.JButton();
        jbWarranty = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jtfNote = new javax.swing.JTextField();
        jPanel7 = new javax.swing.JPanel();
        jcbActive = new javax.swing.JCheckBox();
        jbFillShipmentsAll = new javax.swing.JButton();
        jbUp = new javax.swing.JButton();
        jbDown = new javax.swing.JButton();
        jpMain = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jtOutcomingDoc = new javax.swing.JTable();
        jlSum = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jcbType = new javax.swing.JComboBox();
        jtpHeaders = new javax.swing.JTabbedPane();
        jLabel5 = new javax.swing.JLabel();
        jtfEmployee = new javax.swing.JTextField();
        jbEmployee = new javax.swing.JButton();

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance().getContext().getResourceMap(OutcomingDocView.class);
        setBackground(resourceMap.getColor("Form.background")); // NOI18N
        setClosable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle(resourceMap.getString("Form.title")); // NOI18N
        setName("Form"); // NOI18N

        jPanel2.setBackground(resourceMap.getColor("jPanel2.background")); // NOI18N
        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel2.setName("jPanel2"); // NOI18N

        jLabel2.setText(resourceMap.getString("jLabel2.text")); // NOI18N
        jLabel2.setName("jLabel2"); // NOI18N

        jpDate.setBackground(resourceMap.getColor("jpDate.background")); // NOI18N
        jpDate.setName("jpDate"); // NOI18N

        javax.swing.GroupLayout jpDateLayout = new javax.swing.GroupLayout(jpDate);
        jpDate.setLayout(jpDateLayout);
        jpDateLayout.setHorizontalGroup(
            jpDateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 94, Short.MAX_VALUE)
        );
        jpDateLayout.setVerticalGroup(
            jpDateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 18, Short.MAX_VALUE)
        );

        jLabel12.setText(resourceMap.getString("jLabel12.text")); // NOI18N
        jLabel12.setName("jLabel12"); // NOI18N

        jtfNumber.setText(resourceMap.getString("jtfNumber.text")); // NOI18N
        jtfNumber.setName("jtfNumber"); // NOI18N

        jsTime.setName("jsTime"); // NOI18N

        jLabel3.setText(resourceMap.getString("jLabel3.text")); // NOI18N
        jLabel3.setName("jLabel3"); // NOI18N

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel12)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jtfNumber, javax.swing.GroupLayout.DEFAULT_SIZE, 161, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jsTime, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE, false)
                        .addComponent(jLabel12)
                        .addComponent(jtfNumber, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jpDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jsTime, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel3)))
                .addContainerGap())
        );

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance().getContext().getActionMap(OutcomingDocView.class, this);
        jbAddProduct.setAction(actionMap.get("addProduct")); // NOI18N
        jbAddProduct.setText(resourceMap.getString("jbAddProduct.text")); // NOI18N
        jbAddProduct.setName("jbAddProduct"); // NOI18N

        jbDeleteItem.setAction(actionMap.get("deleteProduct")); // NOI18N
        jbDeleteItem.setText(resourceMap.getString("jbDeleteItem.text")); // NOI18N
        jbDeleteItem.setName("jbDeleteItem"); // NOI18N

        jbFillShipments.setAction(actionMap.get("fillShipments")); // NOI18N
        jbFillShipments.setText(resourceMap.getString("jbFillShipments.text")); // NOI18N
        jbFillShipments.setName("jbFillShipments"); // NOI18N

        jbSerials.setAction(actionMap.get("showSerials")); // NOI18N
        jbSerials.setText(resourceMap.getString("jbSerials.text")); // NOI18N
        jbSerials.setName("jbSerials"); // NOI18N

        jbSave.setAction(actionMap.get("saveDoc")); // NOI18N
        jbSave.setText(resourceMap.getString("jbSave.text")); // NOI18N
        jbSave.setName("jbSave"); // NOI18N

        jbCancel.setAction(actionMap.get("closeDoc")); // NOI18N
        jbCancel.setText(resourceMap.getString("jbCancel.text")); // NOI18N
        jbCancel.setName("jbCancel"); // NOI18N

        jbWarranty.setAction(actionMap.get("printWarranty")); // NOI18N
        jbWarranty.setText(resourceMap.getString("jbWarranty.text")); // NOI18N
        jbWarranty.setName("jbWarranty"); // NOI18N

        jLabel1.setText(resourceMap.getString("jLabel1.text")); // NOI18N
        jLabel1.setName("jLabel1"); // NOI18N

        jtfNote.setText(resourceMap.getString("jtfNote.text")); // NOI18N
        jtfNote.setName("jtfNote"); // NOI18N

        jPanel7.setBackground(resourceMap.getColor("jPanel7.background")); // NOI18N
        jPanel7.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel7.setName("jPanel7"); // NOI18N

        jcbActive.setAction(actionMap.get("checkActive")); // NOI18N
        jcbActive.setBackground(resourceMap.getColor("jcbActive.background")); // NOI18N
        jcbActive.setText(resourceMap.getString("jcbActive.text")); // NOI18N
        jcbActive.setName("jcbActive"); // NOI18N

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jcbActive)
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jcbActive, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(4, Short.MAX_VALUE))
        );

        jbFillShipmentsAll.setAction(actionMap.get("fillShipmentsAll")); // NOI18N
        jbFillShipmentsAll.setText(resourceMap.getString("jbFillShipmentsAll.text")); // NOI18N
        jbFillShipmentsAll.setName("jbFillShipmentsAll"); // NOI18N

        jbUp.setAction(actionMap.get("up")); // NOI18N
        jbUp.setText(resourceMap.getString("jbUp.text")); // NOI18N
        jbUp.setName("jbUp"); // NOI18N

        jbDown.setAction(actionMap.get("down")); // NOI18N
        jbDown.setText(resourceMap.getString("jbDown.text")); // NOI18N
        jbDown.setName("jbDown"); // NOI18N

        jpMain.setBackground(resourceMap.getColor("jpMain.background")); // NOI18N
        jpMain.setName("jpMain"); // NOI18N
        jpMain.setLayout(new javax.swing.BoxLayout(jpMain, javax.swing.BoxLayout.LINE_AXIS));

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        jtOutcomingDoc.setModel(new javax.swing.table.DefaultTableModel(
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
        jtOutcomingDoc.setCellSelectionEnabled(true);
        jtOutcomingDoc.setFillsViewportHeight(true);
        jtOutcomingDoc.setName("jtOutcomingDoc"); // NOI18N
        jtOutcomingDoc.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jtOutcomingDocMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jtOutcomingDoc);

        jpMain.add(jScrollPane1);

        jlSum.setBackground(resourceMap.getColor("jlSum.background")); // NOI18N
        jlSum.setFont(resourceMap.getFont("jlSum.font")); // NOI18N
        jlSum.setForeground(resourceMap.getColor("jlSum.foreground")); // NOI18N
        jlSum.setText(resourceMap.getString("jlSum.text")); // NOI18N
        jlSum.setName("jlSum"); // NOI18N

        jLabel4.setText(resourceMap.getString("jLabel4.text")); // NOI18N
        jLabel4.setName("jLabel4"); // NOI18N

        jcbType.setName("jcbType"); // NOI18N

        jtpHeaders.setBackground(resourceMap.getColor("jtpHeaders.background")); // NOI18N
        jtpHeaders.setName("jtpHeaders"); // NOI18N
        jtpHeaders.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jtpHeadersStateChanged(evt);
            }
        });

        jLabel5.setText(resourceMap.getString("jLabel5.text")); // NOI18N
        jLabel5.setName("jLabel5"); // NOI18N

        jtfEmployee.setText(resourceMap.getString("jtfEmployee.text")); // NOI18N
        jtfEmployee.setName("jtfEmployee"); // NOI18N

        jbEmployee.setAction(actionMap.get("chooseEmployee")); // NOI18N
        jbEmployee.setText(resourceMap.getString("jbEmployee.text")); // NOI18N
        jbEmployee.setName("jbEmployee"); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jpMain, javax.swing.GroupLayout.DEFAULT_SIZE, 872, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jcbType, javax.swing.GroupLayout.PREFERRED_SIZE, 223, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jtfEmployee, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jbEmployee, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jtpHeaders, javax.swing.GroupLayout.PREFERRED_SIZE, 295, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jlSum, javax.swing.GroupLayout.DEFAULT_SIZE, 337, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(10, 10, 10))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                        .addComponent(jbUp)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jbDown)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel1)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jtfNote, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                        .addComponent(jbAddProduct)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jbDeleteItem)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jbFillShipments)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jbFillShipmentsAll)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 32, Short.MAX_VALUE)
                                        .addComponent(jbWarranty)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jbSerials)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jbSave, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jbCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(8, 8, 8)))
                        .addGap(0, 0, 0))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel4)
                        .addComponent(jcbType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel5)
                        .addComponent(jtfEmployee, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jbEmployee))
                    .addComponent(jtpHeaders, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jlSum, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel7, 0, 46, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpMain, javax.swing.GroupLayout.PREFERRED_SIZE, 317, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 27, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jtfNote, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jbUp)
                    .addComponent(jbDown)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jbAddProduct)
                    .addComponent(jbDeleteItem)
                    .addComponent(jbFillShipments)
                    .addComponent(jbFillShipmentsAll)
                    .addComponent(jbWarranty)
                    .addComponent(jbSerials)
                    .addComponent(jbSave, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jbCancel))
                .addContainerGap())
        );

        jpMain.setLayout(new javax.swing.BoxLayout(jpMain, javax.swing.BoxLayout.PAGE_AXIS));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jtpHeadersStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jtpHeadersStateChanged
        tabChanged();
    }//GEN-LAST:event_jtpHeadersStateChanged

private void jtOutcomingDocMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtOutcomingDocMouseClicked
    if (evt.getClickCount() > 1) {
        tableClicked();
    }
}//GEN-LAST:event_jtOutcomingDocMouseClicked
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton jbAddProduct;
    private javax.swing.JButton jbCancel;
    private javax.swing.JButton jbDeleteItem;
    private javax.swing.JButton jbDown;
    private javax.swing.JButton jbEmployee;
    private javax.swing.JButton jbFillShipments;
    private javax.swing.JButton jbFillShipmentsAll;
    private javax.swing.JButton jbSave;
    private javax.swing.JButton jbSerials;
    private javax.swing.JButton jbUp;
    private javax.swing.JButton jbWarranty;
    private javax.swing.JCheckBox jcbActive;
    private javax.swing.JComboBox jcbType;
    private javax.swing.JLabel jlSum;
    private javax.swing.JPanel jpDate;
    private javax.swing.JPanel jpMain;
    private javax.swing.JSpinner jsTime;
    private javax.swing.JTable jtOutcomingDoc;
    private javax.swing.JTextField jtfEmployee;
    private javax.swing.JTextField jtfNote;
    private javax.swing.JTextField jtfNumber;
    private javax.swing.JTabbedPane jtpHeaders;
    // End of variables declaration//GEN-END:variables
    private SalesView salesView;
    private OutcomingView outcomingView;
    private Integer documentCode;
    private JDateChooser date;
    private List table;
    private List serials;
    private Integer employeeCode;
    private boolean scanEnter;
    private String scanCode;
    private JTextField jTextBox;
    private Integer active;
    private Integer lock;
    private Integer hash;
    private boolean silent;
    private boolean updateTable;
    private TNHeader tnheader;
    private TTNHeader ttnheader;
    private int type;

    public void showTable() {

        Vector<String> tableHeaders = new Vector<String>();
        Vector tableData = new Vector();
        tableHeaders.add("№");
        tableHeaders.add("Код");
        tableHeaders.add("Наименование");
        tableHeaders.add("Кол-во");
        tableHeaders.add("Цена");
        tableHeaders.add("Сумма");
        if (type == OutcomingDocView.TTN) {
            tableHeaders.add("Места");
            tableHeaders.add("Масса");
        }
        tableHeaders.add("Партия");

        if (table != null) {

            SimpleDateFormat sdfd = new SimpleDateFormat("dd.MM.yy");

            Session session = HUtil.getSession();
            for (int i = 0; i < table.size(); i++) {
                Outcomingtable ot = (Outcomingtable) table.get(i);
                Vector<Object> oneRow = new Vector<Object>();
                oneRow.add(i + 1);
                Nomenclature n = (Nomenclature) HUtil.getElement("Nomenclature", ot.getProductCode(), session);
                oneRow.add(n.getCode());
                if (n != null) {
                    oneRow.add(n.getName());
                } else {
                    oneRow.add("");
                }
                oneRow.add(ot.getQuantity());
                oneRow.add(ot.getPrice());
                oneRow.add(ot.getAmount());
                if (type == OutcomingDocView.TTN) {
                    oneRow.add(ot.getCargos());
                    oneRow.add(ot.getWeight());
                }
                if (ot.getIncomingCode() != null) {
                    Incoming in = (Incoming) HUtil.getElement("Incoming", ot.getIncomingCode(), session);
                    if (in != null) {
                        oneRow.add("Прих.накл.№ " + in.getNumber() + " от " + sdfd.format(in.getDatetime()));
                    } else {
                        oneRow.add("");
                    }
                } else {
                    oneRow.add("");
                }
                tableData.add(oneRow);
            }
            session.close();
        }

        jtOutcomingDoc.setModel(new MyTableModel(tableData, tableHeaders));
        tuneTable();

        Integer sum = 0;
        for (int i = 0; i < table.size(); i++) {
            sum += ((Outcomingtable) table.get(i)).getAmount();
        }
        jlSum.setText("Сумма: " + (new DecimalFormat("#,###,###")).format(sum));

    }

    private void tuneTable() {

        jtOutcomingDoc.getColumnModel().getColumn(3).setCellEditor(new MyCellEditor(jTextBox));
        jtOutcomingDoc.getColumnModel().getColumn(4).setCellEditor(new MyCellEditor(jTextBox));
        jtOutcomingDoc.getColumnModel().getColumn(5).setCellEditor(new MyCellEditor(jTextBox));
        if (type == OutcomingDocView.TTN) {
            jtOutcomingDoc.getColumnModel().getColumn(6).setCellEditor(new MyCellEditor(jTextBox));
            jtOutcomingDoc.getColumnModel().getColumn(7).setCellEditor(new MyCellEditor(jTextBox));
        }

        Util.autoResizeColWidth(jtOutcomingDoc);

        jtOutcomingDoc.getModel().addTableModelListener(new TableModelListener() {

            @Override
            public void tableChanged(TableModelEvent e) {
                
                int r = e.getFirstRow();
                int c = e.getColumn();
                updateTable(r, c);
                showTable();
                Util.moveCell(r, c, jtOutcomingDoc, true);
            }
        });

    }

    @Action
    public void addProduct() {

        if (active == 1) {
            JOptionPane.showMessageDialog(
                    null, "Для изменения табличной части запишите документ непроведенным!",
                    "Изменение табличной части", JOptionPane.ERROR_MESSAGE);
        } else if (lock == 0) {
            JInternalFrame nv = new NomenclatureView(salesView, this, -1, -1, "", "", null, false);
            salesView.getJDesktopPane().add(nv, javax.swing.JLayeredPane.DEFAULT_LAYER);
            nv.setVisible(true);
            try {
                nv.setSelected(true);
            } catch (java.beans.PropertyVetoException e) {
                logger.error(e);
            }
            Rectangle rect = jtOutcomingDoc.getCellRect(jtOutcomingDoc.getRowCount() - 1, 1, true);
            jtOutcomingDoc.scrollRectToVisible(rect);
            jtOutcomingDoc.getSelectionModel().addSelectionInterval(jtOutcomingDoc.getRowCount() - 1, jtOutcomingDoc.getRowCount() - 1);
            jtOutcomingDoc.getColumnModel().getSelectionModel().setSelectionInterval(1, 1);
        }
    }

    public Outcomingtable newOutcomingtableItem(int productCode) {
        Outcomingtable ot = new Outcomingtable();
        try {
            Session session = HUtil.getSession();
            Nomenclature n = (Nomenclature) HUtil.getElement("Nomenclature", productCode, session);
            ot.setProductCode(productCode);
            if (documentCode != null) {
                ot.setDocumentCode(documentCode);
            }
            if (n != null) {
                ot.setPrice(n.getPrice());
                ot.setWeight(n.getWeight());
            }
            session.close();
        } catch (Exception e) {
            logger.error(e);
        }
        return ot;
    }

    @Override
    public void addProductItem(Integer productCode) {
        try {
            int quantity = HUtil.getBalance(productCode);
            Outcomingtable ot = newOutcomingtableItem(productCode);
            Object newQuantity =
                    JOptionPane.showInputDialog(
                    null,
                    "Введите количество, остаток: " + quantity,
                    "Количество",
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    null,
                    1);
            if (!((String) newQuantity).isEmpty()) {
                quantity = Integer.parseInt((String) newQuantity);
            }
            ot.setQuantity(quantity);
            ot.setAmount(ot.getPrice() * quantity);
            ot.setWeight(ot.getWeight() * quantity);
            table.add(ot);
            showTable();
        } catch (Exception e) {
            logger.error(e);
        }
    }

    @Override
    public void setProductItem(Integer productCode, int r) {
        try {
            Session session = HUtil.getSession();
            Outcomingtable ot = (Outcomingtable) table.get(r);
            Nomenclature n = (Nomenclature) HUtil.getElement("Nomenclature", productCode, session);
            ot.setProductCode(productCode);
            if (n != null) {
                ot.setPrice(n.getPrice());
                ot.setAmount(ot.getPrice() * ot.getQuantity());
            }
            table.set(r, ot);
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
    public void setIncomingItem(int incomingCode, int r) {
        try {
            ((Outcomingtable) table.get(r)).setIncomingCode(incomingCode);
        } catch (Exception e) {
            logger.error(e);
        }
        showTable();
    }

    @Action
    public void deleteProduct() {

        if (active == 1) {
            JOptionPane.showMessageDialog(
                    null, "Для изменения табличной части запишите документ непроведенным!",
                    "Изменение табличной части", JOptionPane.ERROR_MESSAGE);
        } else if (lock == 0 && jtOutcomingDoc.getSelectedRow() >= 0) {
            table.remove(jtOutcomingDoc.getSelectedRow());
            showTable();
        }
    }

    public void updateTable(Integer r, Integer c) {
        try {
            Outcomingtable ot = (Outcomingtable) table.get(r);
            if (c == 3 || c == 4) {
                if (c == 3) {
                    ot.setQuantity(new Integer(jtOutcomingDoc.getValueAt(r, 3).toString()));
                    if (ot.getIncomingCode() != null) {
                        fixQuantity(ot.getProductCode(), ot.getIncomingCode(), r);
                    }
                } else if (c == 4) {
                    ot.setPrice(new Integer(jtOutcomingDoc.getValueAt(r, 4).toString()));
                    if (ot.getQuantity() == 0) {
                        ot.setQuantity(1);
                    }
                }
                ot.setAmount(ot.getQuantity() * ot.getPrice());
                Nomenclature n = (Nomenclature) HUtil.getElement("Nomenclature", ot.getProductCode());
                if (n != null) {
                    ot.setWeight(ot.getQuantity() * n.getWeight());
                }
            } else if (c == 6) {
                ot.setCargos(new Integer(jtOutcomingDoc.getValueAt(r, 6).toString()));
            } else if (c == 7) {
                ot.setWeight(new Double(Math.round(new Double(jtOutcomingDoc.getValueAt(r, 7).toString()) * 1000)) / 1000);
            }
            table.set(r, ot);
        } catch (Exception e) {
            logger.error(e);
        }
    }

    @Override
    public void fixQuantity(int productCode, int incomingCode, int row) {
        Integer av = HUtil.getBalanceForIncoming(productCode, incomingCode);
        if (av <= 0) {
            JOptionPane.showMessageDialog(this, "Нет доступного товара!");
        } else {
            if (av < ((Outcomingtable) table.get(row)).getQuantity()) {
                JOptionPane.showMessageDialog(this, "Доступно " + av + " единиц товара!");
                ((Outcomingtable) table.get(row)).setQuantity(av);
                ((Outcomingtable) table.get(row)).setAmount(av * ((Outcomingtable) table.get(row)).getPrice());
                Nomenclature n = (Nomenclature) HUtil.getElement("Nomenclature", productCode);
                if (n != null) {
                    ((Outcomingtable) table.get(row)).setWeight(n.getWeight() * av);
                }
                showTable();
            }
        }
    }

    private Integer fillShipmentsCommit(Integer r) {
        Outcomingtable ot = (Outcomingtable) table.get(r);
        List ss = HUtil.getShipments(ot.getProductCode(), date.getDate());
        if (ss.size() > 0) {
            int pn = ot.getQuantity();
            int j = 0;
            while (pn != 0 && j < ss.size()) {
                HashMap se = (HashMap) ss.get(j);
                boolean notLast = true;
                if (pn > (Integer) se.get("balance")) {
                    ((Outcomingtable) table.get(r)).setQuantity((Integer) se.get("balance"));
                    pn -= (Integer) se.get("balance");
                    Outcomingtable otNew = newOutcomingtableItem(ot.getProductCode());
                    if (j < ss.size() - 1) {
                        if (r < table.size()) {
                            table.add(r + 1, otNew);
                        } else {
                            table.add(otNew);
                        }
                    }
                } else {
                    notLast = false;
                    ((Outcomingtable) table.get(r)).setQuantity(pn);
                    pn = 0;
                }
                ((Outcomingtable) table.get(r)).setAmount(
                        ((Outcomingtable) table.get(r)).getQuantity()
                        * ((Outcomingtable) table.get(r)).getPrice());
                ((Outcomingtable) table.get(r)).setIncomingCode((Integer) se.get("incomingCode"));
                j++;
                if (notLast) {
                    r++;
                }
            }
        }
        return r;
    }

    @Action
    public void fillShipments() {
        int r = jtOutcomingDoc.getSelectedRow();
        if (r != -1) {
            fillShipmentsCommit(r);
        }
        showTable();
    }

    @Action
    public void fillShipmentsAll() {

        int a = 0;
        if (!silent) {
            a = JOptionPane.showOptionDialog(
                    this, "Заполнить все партии?", "Партии", JOptionPane.YES_NO_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE, null, new Object[]{"Да", "Нет"}, "Да");
        }
        if (a == 0) {
            for (int i = 0; i < table.size(); i++) {
                i = fillShipmentsCommit(i);
            }
        }
        if (!silent) {
            showTable();
        }
    }

    @Action
    public void showSerials() {
        int r = jtOutcomingDoc.getSelectedRow();
        if (r != -1) {
            SerialsView sv =
                    new SerialsView(
                    salesView,
                    this,
                    ((Outcomingtable) table.get(r)).getProductCode(),
                    (ArrayList) serials.get(r),
                    lock,
                    r);
            salesView.getJDesktopPane().add(sv, javax.swing.JLayeredPane.DEFAULT_LAYER);
            sv.setVisible(true);
            try {
                sv.setSelected(true);
            } catch (java.beans.PropertyVetoException e) {
                logger.error(e);
            }
        }
    }

    private void showShipments() {
        int r = jtOutcomingDoc.getSelectedRow();
        if (r != -1) {
            ShipmentsView sv =
                    new ShipmentsView(this, salesView, ((Outcomingtable) table.get(r)).getProductCode(), r);
            salesView.getJDesktopPane().add(sv, javax.swing.JLayeredPane.DEFAULT_LAYER);
            sv.setVisible(true);
            try {
                sv.setSelected(true);
            } catch (java.beans.PropertyVetoException e) {
                logger.error(e);
            }
        }
    }

    @Override
    public void setFocus() {
        jtOutcomingDoc.setRequestFocusEnabled(true);
        jtOutcomingDoc.requestFocusInWindow();
    }

    public List getTable() {
        return table;
    }

    public JTable getJTable() {
        return jtOutcomingDoc;
    }

    public void close() {

        if (lock == 0) {

            save();

            Session session = HUtil.getSession();
            Outcoming x = (Outcoming) HUtil.getElement("Outcoming", documentCode, session);
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

        if (HUtil.checkExists("Outcoming", "number", jtfNumber.getText(), documentCode)) {
            int a = JOptionPane.showOptionDialog(
                    this, "Документ с таким номером уже существует! Продолжить?", "Сохранение", JOptionPane.YES_NO_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE, null, new Object[]{"Да", "Нет"}, "Да");
            if (a == 1) {
                return;
            }
        }
        if (type == 0 || type == 1) {
            if (HUtil.checkExists("Outcoming", "invoiceNumber", tnheader.getInvoiceNumber(), documentCode)) {
                int a = JOptionPane.showOptionDialog(
                        this, "Документ с таким номером счет-фактуры уже существует! Продолжить?", "Сохранение", JOptionPane.YES_NO_CANCEL_OPTION,
                        JOptionPane.QUESTION_MESSAGE, null, new Object[]{"Да", "Нет"}, "Да");
                if (a == 1) {
                    return;
                }
            }
        }
        close();
        Util.closeJIF(this, outcomingView, salesView);
        Util.closeJIFTab(this, salesView);
    }

    @Action
    public void printWarranty() {

        Util.checkDocSaved(this);

        try {
            File file = new File(Util.getAppPath() + "\\templates\\Warranty.ods");
            final Sheet sheet = SpreadSheet.createFromFile(file).getSheet(0);
            sheet.getCellAt("B4").setValue(" ГАРАНТИЙНЫЙ ТАЛОН  ОТ  " + Util.date2String(date.getDate()));
            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yy");
            Session session = HUtil.getSession();
            ArrayList print = new ArrayList();
            for (int i = 0; i < table.size(); i++) {
                Outcomingtable ot = (Outcomingtable) table.get(i);
                Nomenclature n = (Nomenclature) HUtil.getElement("Nomenclature", ot.getProductCode(), session);
                ArrayList st = (ArrayList) serials.get(i);
                if (st.size() > 0) {
                    for (int j = 0; j < st.size(); j++) {
                        ArrayList row = new ArrayList();
                        if (n != null) {
                            row.add(n.getName());
                        } else {
                            row.add("");
                        }
                        row.add(((Serialtable) st.get(j)).getSerial());
                        if (n != null && n.getWarranty() != null) {
                            row.add(n.getWarranty());
                        } else {
                            row.add("");
                        }
                        print.add(row);
                    }
                }
                for (int j = 0; j < ot.getQuantity() - st.size(); j++) {
                    ArrayList row = new ArrayList();
                    if (n != null) {
                        row.add(n.getName());
                    } else {
                        row.add("");
                    }
                    row.add("");
                    if (n != null && n.getWarranty() != null) {
                        row.add(n.getWarranty());
                    } else {
                        row.add("");
                    }
                    print.add(row);
                }
            }

            int ts = print.size();
            if (ts > 1) {
                sheet.duplicateRows(6, 1, ts - 1);
            }
            for (int i = 0; i < ts; i++) {
                ArrayList row = (ArrayList) print.get(i);
                sheet.getCellAt("B" + (i + 7)).setValue(row.get(0));
                sheet.getCellAt("D" + (i + 7)).setValue(row.get(1));
                sheet.getCellAt("E" + (i + 7)).setValue(row.get(2));
            }
            sheet.getCellAt("D" + (15 + ts)).setValue(sdf.format(date.getDate()));

            String r = " " + Math.round(Math.random() * 100000);
            File outputFile = new File("temp\\Гарантия № " + tnheader.getTNNumber() + r + ".ods");
            sheet.getSpreadSheet().saveAs(outputFile);
            Util.openDoc("temp\\Гарантия № " + tnheader.getTNNumber() + r + ".ods");
        } catch (Exception e) {
            logger.error(e);
        }
    }

    public void commit() {

        try {
            if (jtOutcomingDoc.getSelectedRow() >= 0) {
                updateTable(jtOutcomingDoc.getSelectedRow(), jtOutcomingDoc.getSelectedColumn());
            }

            Session session = HUtil.getSession();
            session.beginTransaction();

            Outcoming o = null;
            if (documentCode != null) {
                o = (Outcoming) HUtil.getElement("Outcoming", documentCode, session);;
            }
            if (o == null) {
                o = new Outcoming();
            }

            o.setDatetime(Util.composeDate(date.getDate(), (Date) jsTime.getValue()));
            o.setNumber(jtfNumber.getText());
            o.setDocumentType(Util.types[type]);
            o.setNote(jtfNote.getText());
            o.setEmployee(employeeCode);
            if (type == 0 || type == 1) {
                o.setInvoiceNumber(tnheader.getInvoiceNumber());
                o.setTnnumber(tnheader.getTNNumber());
                o.setContractNumber(tnheader.getContractNumber());
                o.setContractDate(tnheader.getContractDate());
                o.setInvoiceDate(tnheader.getInvoiceDate());
                o.setContractor(tnheader.getContractor());
                o.setEmployee(tnheader.getEmployee());
                o.setAthorizer(tnheader.getAuthorizer());
                if (type == 1) {
                    o.setAuto(ttnheader.getAuto());
                    o.setTrailer(ttnheader.getTrailer());
                    o.setOwner(ttnheader.getOwner());
                    o.setDriver(ttnheader.getDriver());
                    o.setLoadingAddress(ttnheader.getLoadingAddress());
                    o.setUnloadingAddress(ttnheader.getUnloadingAddress());
                    o.setRecepient(ttnheader.getRecepient());
                    o.setRecepientName(ttnheader.getRecepientName());
                    o.setWarrant(ttnheader.getWarrant());
                    o.setWarrantOrganization(ttnheader.getWarrantOrganization());
                }
            }
            if (active != 2) {
                if (jcbActive.isSelected()) {
                    o.setActive(1);
                } else {
                    o.setActive(0);
                }
            }

            if (lock == 0) {
                o.setLocked(0);
            }

            if (documentCode != null) {
                session.update(o);
            } else {
                session.save(o);
            }

            session.getTransaction().commit();
            session.close();

            session = HUtil.getSession();
            session.beginTransaction();
            documentCode = o.getCode();

            List objs = session.createQuery("from Outcomingtable it where it.documentCode = " + documentCode).list();

            for (int j = 0; j < table.size(); j++) {
                Outcomingtable ot = (Outcomingtable) table.get(j);
                ot.setDocumentCode(documentCode);
                ot.setLine(j + 1);
                boolean newIn = true;
                for (int k = 0; k < objs.size(); k++) {
                    if (((Outcomingtable) objs.get(k)).getCode() == ot.getCode()) {
                        newIn = false;
                        objs.remove(k);
                        break;
                    }
                }
                if (newIn) {
                    session.save(ot);
                } else {
                    session.merge(ot);
                }
            }

            for (int j = 0; j < objs.size(); j++) {
                session.delete((Outcomingtable) objs.get(j));
            }

            session.getTransaction().commit();

            session.beginTransaction();
            String hql = "from Serialtable st where st.documentCode = " + documentCode;
            List res = session.createQuery(hql).list();
            for (int i = 0; i < res.size(); i++) {
                session.delete((Serialtable) res.get(i));
            }
            session.getTransaction().commit();

            session.beginTransaction();
            for (int i = 0; i < serials.size(); i++) {
                ArrayList s = (ArrayList) serials.get(i);
                for (int j = 0; j < s.size(); j++) {
                    Serialtable st = (Serialtable) s.get(j);
                    st.setDocumentCode(documentCode);
                    session.save(st);
                }
            }

            session.getTransaction().commit();

            session.beginTransaction();

            List pms = session.createQuery(
                    "from Outcomingpayments op where op.documentCode = " + documentCode).list();

            List payments = tnheader.getPayments();
            for (int j = 0; j < payments.size(); j++) {
                Outcomingpayments op = (Outcomingpayments) payments.get(j);
                op.setDocumentCode(documentCode);
                boolean newObj = true;
                for (int k = 0; k < pms.size(); k++) {
                    if (((Outcomingpayments) pms.get(k)).getCode() == op.getCode()) {
                        newObj = false;
                        pms.remove(k);
                        break;
                    }
                }
                if (newObj) {
                    session.save(op);
                } else {
                    session.merge(op);
                }
            }

            for (int j = 0; j < pms.size(); j++) {
                session.delete((Outcomingpayments) pms.get(j));
            }

            session.getTransaction().commit();

            if (o.getActive() == 1) {

                session.beginTransaction();
                for (int j = 0; j < table.size(); j++) {
                    Outcomingtable ot = (Outcomingtable) table.get(j);
                    HUtil.updateQuantity(ot.getProductCode(), -ot.getQuantity(), session);
                }
                session.getTransaction().commit();

            } else if (o.getActive() == 0 && active == 1) {

                session.beginTransaction();
                for (int j = 0; j < table.size(); j++) {
                    Outcomingtable ot = (Outcomingtable) table.get(j);
                    HUtil.updateQuantity(ot.getProductCode(), ot.getQuantity(), session);
                }
                session.getTransaction().commit();
            }

            session.close();

            if (active != 2) {
                if (jcbActive.isSelected()) {
                    active = 1;
                } else {
                    active = 0;
                }
            }

        } catch (Exception e) {
            logger.error(e);
        }
    }

    public void save() {

        if (lock == 0 || silent) {

            TableCellEditor tce = jtOutcomingDoc.getCellEditor();
            if (tce != null) {
                tce.stopCellEditing();
            }

            if (hash == null || !hash.equals(Util.hash(this)) || silent) {

                int a = 0;
                if (!silent) {
                    a = JOptionPane.showOptionDialog(
                            this, "Сохранить документ?", "Сохранение", JOptionPane.YES_NO_CANCEL_OPTION,
                            JOptionPane.QUESTION_MESSAGE, null, new Object[]{"Да", "Нет"}, "Да");
                }
                if (a == 0) {

                    commit();

                    Util.updateJournals(salesView, OutcomingView.class);
                    salesView.getWorkplace().showNomenclature();
                    hash = Util.hash(this);
                }
            }
        }
    }

    @Override
    public void setSerials(Integer row, ArrayList s) {
        serials.set(row, s);
    }

    @Action
    public void checkActive() {
        if (jcbActive.isSelected()) {
            /*            if (payments.size() == 0 && jcbDocType.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(
            this, "Нет оплат!", "Пустая оплата", JOptionPane.ERROR_MESSAGE);
            jcbActive.setSelected(false);
            }
             */        }
    }

    @Action
    public void saveDoc() {
        save();
    }

    public Integer getHash() {
        return hash;
    }

    public Integer getHashCode() {
        return Util.hash(this);
    }

    public List getSerials() {
        return serials;
    }

    @Action
    public void up() {
        if (lock == 0) {
            int r = jtOutcomingDoc.getSelectedRow();
            if (r != -1 && r > 0) {
                Collections.swap(table, r, r - 1);
                updateTable = false;
                Util.swapRows(jtOutcomingDoc, r, r - 1);
                tuneTable();
                Util.move2row(jtOutcomingDoc, r - 1);
                updateTable = true;
            }
        }
    }

    @Action
    public void down() {
        if (lock == 0) {
            int r = jtOutcomingDoc.getSelectedRow();
            if (r != -1 && r < table.size() - 1) {
                Collections.swap(table, r, r + 1);
                updateTable = false;
                Util.swapRows(jtOutcomingDoc, r, r + 1);
                tuneTable();
                Util.move2row(jtOutcomingDoc, r + 1);
                updateTable = true;
            }
        }
    }

    public Date getDate() {
        return date.getDate();
    }

    public void setDate(Date date) {
        this.date.getDateEditor().setDate(date);
        jsTime.setValue(date);
    }

    public void setActive(boolean active) {
        jcbActive.setSelected(active);
    }

    public List getPayments() {

        List payments = new ArrayList();
        if (tnheader != null) {
            payments = tnheader.getPayments();
        }
        return payments;
    }

    private void typeChanged() {

        int si = jcbType.getSelectedIndex();

        if (si == 0) {
            tnheader.setTNButtonVisible(true);
            if (type > 1) {
                if (tnheader == null) {
                    tnheader = new TNHeader(this, salesView, documentCode, lock, table);
                }
                jpMain.add(tnheader, 0);
                jpMain.revalidate();
                type = 0;
            } else if (type == 1) {
                jtpHeaders.setVisible(false);
                type = 0;
                showTable();
            }
        } else if (si == 1) {
            tnheader.setTNButtonVisible(false);
            if (type > 1) {
                if (tnheader == null) {
                    tnheader = new TNHeader(this, salesView, documentCode, lock, table);
                }
                jpMain.add(tnheader, 0);
            }
            if (type == 0 || type > 1) {
                if (ttnheader == null) {
                    ttnheader = new TTNHeader(this, salesView, tnheader, documentCode, lock, table);
                }
            }
            if (type != 1) {
                jtpHeaders.setVisible(true);
                jpMain.revalidate();
                type = 1;
                showTable();
            }
        } else {
            if (type == 0) {
                jpMain.remove(0);
                jpMain.revalidate();
                type = jcbType.getSelectedIndex();
            } else if (type == 1) {
                jpMain.remove(0);
                jtpHeaders.setVisible(false);
                jpMain.revalidate();
                type = jcbType.getSelectedIndex();
                showTable();
            } else {
                type = jcbType.getSelectedIndex();
            }
        }
    }

    public int getType() {
        return type;
    }

    public TNHeader getTNHeader() {
        return tnheader;
    }

    public TTNHeader getTTNHeader() {
        return ttnheader;
    }

    public boolean getActive() {
        return jcbActive.isSelected();
    }

    public void setUnloadingAddress(String unloadingAddress) {
        ttnheader.setUnloadingAddress(unloadingAddress);
    }

    private void tabChanged() {

        if (jcbType.getSelectedIndex() == 1) {
            if (jtpHeaders.getSelectedIndex() == 0) {
                jpMain.remove(0);
                jpMain.add(tnheader, 0);
                jpMain.updateUI();
            } else {
                jpMain.remove(0);
                jpMain.add(ttnheader, 0);
                jpMain.updateUI();
            }
        }
    }

    private void tableClicked() {

        int col = jtOutcomingDoc.getSelectedColumn();
        
        if (col < 3) {
            
            if (active == 1) {

                JOptionPane.showMessageDialog(
                        null, "Для изменения табличной части запишите документ непроведенным!",
                        "Изменение табличной части", JOptionPane.ERROR_MESSAGE);
            } else {

                int r = jtOutcomingDoc.getSelectedRow();
                if (r != -1) {
                    Outcomingtable ot = (Outcomingtable) table.get(r);
                    JInternalFrame nv;
                    Nomenclature n = (Nomenclature) HUtil.getElement("Nomenclature", ot.getProductCode());
                    if (n != null) {
                        nv = new NomenclatureView(
                                salesView, this,
                                ot.getProductCode(),
                                jtOutcomingDoc.getSelectedRow(), n.getName(), n.getName(), n.getInPrice(), false);
                    } else {
                        nv = new NomenclatureView(
                                salesView, this,
                                ot.getProductCode(),
                                jtOutcomingDoc.getSelectedRow(), "", "", null, false);
                    }

                    salesView.getJDesktopPane().add(nv, javax.swing.JLayeredPane.DEFAULT_LAYER);
                    nv.setVisible(true);
                    try {
                        nv.setSelected(true);
                    } catch (java.beans.PropertyVetoException e) {
                        logger.error(e);
                    }
                }
            }

        } else if ((type == OutcomingDocView.TTN && col == 8 || type != OutcomingDocView.TTN && col == 6)) {
            showShipments();
        }
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

    @Override
    public void setEmployee(Integer employeeCode, String name) {
        this.employeeCode = employeeCode;
        Session session = HUtil.getSession();
        Employee e = (Employee) HUtil.getElement("Employee", employeeCode, session);
        if (e != null) {
            jtfEmployee.setText(e.getShortName());
        }
        session.close();

    }
    
}
