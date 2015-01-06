/*
 * IncomingView.java
 *
 * Created on 07.05.2011, 14:59:59
 */
package sales.incoming;

import com.toedter.calendar.JDateChooser;
import java.awt.Cursor;
import java.awt.Rectangle;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;
import javax.swing.GroupLayout;
import javax.swing.JFileChooser;
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
import sales.SalesApp;
import sales.SalesView;
import sales.catalogs.EmployeeView;
import sales.interfaces.IEmployee;
import sales.catalogs.NomenclatureView;
import sales.catalogs.SuppliersView;
import sales.entity.Employee;
import sales.entity.Incoming;
import sales.entity.Incomingtable;
import sales.entity.Nomenclature;
import sales.entity.Repricing;
import sales.entity.Repricingtable;
import sales.entity.Suppliers;
import sales.interfaces.IClose;
import sales.interfaces.ICommit;
import sales.interfaces.IFocus;
import sales.interfaces.IHashAndSave;
import sales.interfaces.IProductItem;
import sales.interfaces.ISupplier;
import sales.repricing.RepricingDocView;
import sales.util.HUtil;
import sales.util.Util;

public class IncomingDocView extends JInternalFrame
        implements ISupplier, IEmployee, IProductItem, IHashAndSave, IClose, ICommit, IFocus {

    static Logger logger = Logger.getLogger(SalesApp.class.getName());

    public IncomingDocView(
            SalesView salesView, IncomingView incomingView, Integer documentCode, Integer productCode) {

        initComponents();

        Util.initJIF(this, "Приход", incomingView, salesView);
        Util.initJTable(jtIncomingDoc);

        this.salesView = salesView;
        this.incomingView = incomingView;
        this.documentCode = documentCode;

        choosingCodes = false;

        jcbType.addItem("Приход");
        jcbType.addItem("Инвентаризация");
        jcbType.addItem("Ввод остатков");

        Session session = HUtil.getSession();

        jsTime.setModel(new SpinnerDateModel());
        JSpinner.DateEditor timeEditor = new JSpinner.DateEditor(jsTime, "HH:mm");
        jsTime.setEditor(timeEditor);

        if (documentCode != null) {
            Incoming i = (Incoming) HUtil.getElement("Incoming", documentCode, session);
            jtfNumber.setText(i.getNumber());
            date = new JDateChooser(i.getDatetime());
            jsTime.setValue(i.getDatetime());
            active = i.getActive();
            if (active == 1) {
                jcbActive.setSelected(true);
            } else if (active == 2) {
                jcbActive.setEnabled(false);
            }
            jcbType.setSelectedIndex(i.getType());
            if (i.getContractor() != null) {
                if (i.getContractor() != null) {
                    Suppliers s = (Suppliers) HUtil.getElement("Suppliers", i.getContractor(), session);
                    if (s != null) {
                        supplierCode = s.getCode();
                        jtfSupplier.setText(s.getName());
                    }
                }
                if (i.getResponsible() != null) {
                    Employee e = (Employee) HUtil.getElement("Employee", i.getResponsible(), session);
                    responsibleCode = e.getCode();
                    if (e != null) {
                        jtfResponsible.setText(e.getShortName());
                    }
                }
            }
            repricingCode = i.getRepricing();
            showRepricingDoc();
            jtfNote.setText(i.getNote());
            table = HUtil.executeHql(
                    "from Incomingtable it"
                    + " where it.documentCode = " + documentCode
                    + " order by it.line");
            lock = i.getLocked();
            if (lock == 1) {
                JOptionPane.showMessageDialog(
                        this,
                        "Элемент редактируется другим пользователем и будет открыт только для просмотра!",
                        "Блокировка", JOptionPane.PLAIN_MESSAGE);
                jtfNumber.setEditable(false);
                date.setEnabled(false);
                jcbType.setEnabled(false);
                jbSupplier.setEnabled(false);
                jbResponsible.setEnabled(false);
                jtfNote.setEditable(false);
                jcbActive.setEnabled(false);
                jbSave.setEnabled(false);
            } else {
                session.beginTransaction();
                i.setLocked(1);
                session.merge(i);
                session.getTransaction().commit();
            }

        } else {
            jtfNumber.setText("");
            jtfNote.setText("");
            Calendar cl = Calendar.getInstance();
            date = new JDateChooser(cl.getTime());
            jsTime.setValue(cl.getTime());
            jcbType.setSelectedIndex(0);
            table = new ArrayList();
            jbRepricing.setEnabled(false);
            active = 0;
            lock = 0;
        }

        GroupLayout gl = (GroupLayout) jpDate.getLayout();
        gl.setHorizontalGroup(gl.createParallelGroup().addGroup(gl.createSequentialGroup().addComponent(date)));
        gl.setVerticalGroup(gl.createParallelGroup().addGroup(gl.createSequentialGroup().addComponent(date)));

        jcbType.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    typeSelected();
                }
            }
        });

        typeSelected();

        jTextBox = new JTextField();
        jTextBox.addKeyListener(new KeyAdapter() {

            @Override
            public void keyTyped(KeyEvent e) {

                jTextBox.setEditable(
                        Character.isDigit(e.getKeyChar())
                        || e.getKeyChar() == KeyEvent.VK_BACK_SPACE
                        || e.getKeyChar() == '.'
                        && ((JTable) e.getComponent().getParent()).getSelectedColumn() == 10
                        && !((JTextField) e.getComponent()).getText().contains("."));
            }
        });

        session.close();

        jtIncomingDoc.addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent e) {
                int r = jtIncomingDoc.getSelectedRow();
                int c = jtIncomingDoc.getSelectedColumn();
                if (r != -1 && c != -1) {
                    jTextBox.setEditable(Character.isDigit(e.getKeyChar())
                            || e.getKeyChar() == KeyEvent.VK_BACK_SPACE
                            || e.getKeyChar() == '.'
                            && ((JTable) e.getComponent().getParent()).getSelectedColumn() == 10
                            && !((JTextField) e.getComponent()).getText().contains("."));
                }
            }
        });

        updateTable = true;
        showTable();

        if (productCode != null && table.size() > 0) {
            for (int i = 0; i < table.size(); i++) {
                if (((Incomingtable) table.get(i)).getProductCode().equals(productCode)) {
                    Rectangle rect = jtIncomingDoc.getCellRect(i, 1, true);
                    jtIncomingDoc.scrollRectToVisible(rect);
                    jtIncomingDoc.getSelectionModel().addSelectionInterval(i, i);
                    jtIncomingDoc.getColumnModel().getSelectionModel().setSelectionInterval(1, 1);
                    break;
                }
            }
        } else {
            Util.selectFirstRow(jtIncomingDoc);
        }

        scanEnter = false;

        //Util.setMoveRight(jtIncomingDoc);

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

            return (lock == 0 && active != 1 && (c > 1 && c != 3));

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
        jtIncomingDoc = new javax.swing.JTable();
        jbAddProduct = new javax.swing.JButton();
        jbDeleteItem = new javax.swing.JButton();
        jbCancel = new javax.swing.JButton();
        jcbActive = new javax.swing.JCheckBox();
        jlSum = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jtfNumber = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jcbType = new javax.swing.JComboBox();
        jLabel2 = new javax.swing.JLabel();
        jpDate = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jsTime = new javax.swing.JSpinner();
        jlSupplier = new javax.swing.JLabel();
        jtfSupplier = new javax.swing.JTextField();
        jbSupplier = new javax.swing.JButton();
        jlResponsible = new javax.swing.JLabel();
        jtfResponsible = new javax.swing.JTextField();
        jbResponsible = new javax.swing.JButton();
        jbRepricing = new javax.swing.JButton();
        jlRepricing = new javax.swing.JLabel();
        jbImport = new javax.swing.JButton();
        jlInSum = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jtfFilter = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jtfNote = new javax.swing.JTextField();
        jbSave = new javax.swing.JButton();
        jbRound = new javax.swing.JButton();
        jbRegister = new javax.swing.JButton();
        jbPriceList = new javax.swing.JButton();
        jbPriceListSelected = new javax.swing.JButton();
        jbUp = new javax.swing.JButton();
        jbDown = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance().getContext().getResourceMap(IncomingDocView.class);
        setBackground(resourceMap.getColor("Form.background")); // NOI18N
        setClosable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle(resourceMap.getString("Form.title")); // NOI18N
        setName("Form"); // NOI18N

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        jtIncomingDoc.setFont(resourceMap.getFont("jtIncomingDoc.font")); // NOI18N
        jtIncomingDoc.setModel(new javax.swing.table.DefaultTableModel(
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
        jtIncomingDoc.setCellSelectionEnabled(true);
        jtIncomingDoc.setFillsViewportHeight(true);
        jtIncomingDoc.setName("jtIncomingDoc"); // NOI18N
        jtIncomingDoc.setSurrendersFocusOnKeystroke(true);
        jtIncomingDoc.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jtIncomingDocMouseClicked(evt);
            }
        });
        jtIncomingDoc.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jtIncomingDocKeyTyped(evt);
            }
        });
        jScrollPane1.setViewportView(jtIncomingDoc);

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance().getContext().getActionMap(IncomingDocView.class, this);
        jbAddProduct.setAction(actionMap.get("addProduct")); // NOI18N
        jbAddProduct.setFont(resourceMap.getFont("jbAddProduct.font")); // NOI18N
        jbAddProduct.setText(resourceMap.getString("jbAddProduct.text")); // NOI18N
        jbAddProduct.setName("jbAddProduct"); // NOI18N

        jbDeleteItem.setAction(actionMap.get("deleteProducts")); // NOI18N
        jbDeleteItem.setFont(resourceMap.getFont("jbAddProduct.font")); // NOI18N
        jbDeleteItem.setText(resourceMap.getString("jbDeleteItem.text")); // NOI18N
        jbDeleteItem.setName("jbDeleteItem"); // NOI18N

        jbCancel.setAction(actionMap.get("closeDoc")); // NOI18N
        jbCancel.setFont(resourceMap.getFont("jbAddProduct.font")); // NOI18N
        jbCancel.setText(resourceMap.getString("jbCancel.text")); // NOI18N
        jbCancel.setName("jbCancel"); // NOI18N

        jcbActive.setBackground(resourceMap.getColor("jcbActive.background")); // NOI18N
        jcbActive.setText(resourceMap.getString("jcbActive.text")); // NOI18N
        jcbActive.setName("jcbActive"); // NOI18N

        jlSum.setFont(resourceMap.getFont("jlSum.font")); // NOI18N
        jlSum.setForeground(resourceMap.getColor("jlSum.foreground")); // NOI18N
        jlSum.setText(resourceMap.getString("jlSum.text")); // NOI18N
        jlSum.setName("jlSum"); // NOI18N

        jPanel1.setBackground(resourceMap.getColor("jPanel1.background")); // NOI18N
        jPanel1.setName("jPanel1"); // NOI18N

        jLabel1.setFont(resourceMap.getFont("jLabel1.font")); // NOI18N
        jLabel1.setText(resourceMap.getString("jLabel1.text")); // NOI18N
        jLabel1.setName("jLabel1"); // NOI18N

        jtfNumber.setFont(resourceMap.getFont("jLabel1.font")); // NOI18N
        jtfNumber.setText(resourceMap.getString("jtfNumber.text")); // NOI18N
        jtfNumber.setName("jtfNumber"); // NOI18N

        jLabel5.setText(resourceMap.getString("jLabel5.text")); // NOI18N
        jLabel5.setName("jLabel5"); // NOI18N

        jcbType.setName("jcbType"); // NOI18N
        jcbType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jcbTypeActionPerformed(evt);
            }
        });

        jLabel2.setFont(resourceMap.getFont("jLabel1.font")); // NOI18N
        jLabel2.setText(resourceMap.getString("jLabel2.text")); // NOI18N
        jLabel2.setName("jLabel2"); // NOI18N

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
            .addGap(0, 25, Short.MAX_VALUE)
        );

        jLabel6.setText(resourceMap.getString("jLabel6.text")); // NOI18N
        jLabel6.setName("jLabel6"); // NOI18N

        jsTime.setName("jsTime"); // NOI18N

        jlSupplier.setText(resourceMap.getString("jlSupplier.text")); // NOI18N
        jlSupplier.setName("jlSupplier"); // NOI18N

        jtfSupplier.setBackground(resourceMap.getColor("jtfSupplier.background")); // NOI18N
        jtfSupplier.setEditable(false);
        jtfSupplier.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        jtfSupplier.setAction(actionMap.get("changeContractor")); // NOI18N
        jtfSupplier.setName("jtfSupplier"); // NOI18N

        jbSupplier.setAction(actionMap.get("chooseSupplier")); // NOI18N
        jbSupplier.setText(resourceMap.getString("jbSupplier.text")); // NOI18N
        jbSupplier.setName("jbSupplier"); // NOI18N

        jlResponsible.setText(resourceMap.getString("jlResponsible.text")); // NOI18N
        jlResponsible.setName("jlResponsible"); // NOI18N

        jtfResponsible.setBackground(resourceMap.getColor("jtfResponsible.background")); // NOI18N
        jtfResponsible.setEditable(false);
        jtfResponsible.setText(resourceMap.getString("jtfResponsible.text")); // NOI18N
        jtfResponsible.setName("jtfResponsible"); // NOI18N

        jbResponsible.setAction(actionMap.get("chooseEmployee")); // NOI18N
        jbResponsible.setText(resourceMap.getString("jbResponsible.text")); // NOI18N
        jbResponsible.setName("jbResponsible"); // NOI18N

        jbRepricing.setAction(actionMap.get("createRepricing")); // NOI18N
        jbRepricing.setText(resourceMap.getString("jbRepricing.text")); // NOI18N
        jbRepricing.setName("jbRepricing"); // NOI18N

        jlRepricing.setFont(resourceMap.getFont("jlRepricing.font")); // NOI18N
        jlRepricing.setForeground(resourceMap.getColor("jlRepricing.foreground")); // NOI18N
        jlRepricing.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jlRepricing.setText(resourceMap.getString("jlRepricing.text")); // NOI18N
        jlRepricing.setName("jlRepricing"); // NOI18N

        jbImport.setAction(actionMap.get("importDoc")); // NOI18N
        jbImport.setText(resourceMap.getString("jbImport.text")); // NOI18N
        jbImport.setName("jbImport"); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jtfNumber, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jpDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jsTime, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jcbType, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(55, 55, 55)
                        .addComponent(jlRepricing, javax.swing.GroupLayout.DEFAULT_SIZE, 134, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jbImport, javax.swing.GroupLayout.DEFAULT_SIZE, 93, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jbRepricing))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jlSupplier)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jtfSupplier, javax.swing.GroupLayout.DEFAULT_SIZE, 416, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jbSupplier, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jlResponsible)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jtfResponsible, javax.swing.GroupLayout.DEFAULT_SIZE, 337, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jbResponsible, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jtfNumber, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jsTime, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel5)
                        .addComponent(jcbType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jbRepricing)
                        .addComponent(jlRepricing, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jbImport))
                    .addComponent(jpDate, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jtfResponsible, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jbResponsible))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jlSupplier, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jtfSupplier, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jbSupplier)
                        .addComponent(jlResponsible)))
                .addContainerGap())
        );

        jlInSum.setFont(resourceMap.getFont("jlSum.font")); // NOI18N
        jlInSum.setForeground(resourceMap.getColor("jlSum.foreground")); // NOI18N
        jlInSum.setText(resourceMap.getString("jlInSum.text")); // NOI18N
        jlInSum.setName("jlInSum"); // NOI18N

        jLabel3.setText(resourceMap.getString("jLabel3.text")); // NOI18N
        jLabel3.setName("jLabel3"); // NOI18N

        jtfFilter.setText(resourceMap.getString("jtfFilter.text")); // NOI18N
        jtfFilter.setName("jtfFilter"); // NOI18N
        jtfFilter.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jtfFilterKeyReleased(evt);
            }
        });

        jLabel4.setText(resourceMap.getString("jLabel4.text")); // NOI18N
        jLabel4.setName("jLabel4"); // NOI18N

        jtfNote.setText(resourceMap.getString("jtfNote.text")); // NOI18N
        jtfNote.setName("jtfNote"); // NOI18N

        jbSave.setAction(actionMap.get("saveDoc")); // NOI18N
        jbSave.setText(resourceMap.getString("jbSave.text")); // NOI18N
        jbSave.setName("jbSave"); // NOI18N

        jbRound.setAction(actionMap.get("roundPrices")); // NOI18N
        jbRound.setText(resourceMap.getString("jbRound.text")); // NOI18N
        jbRound.setName("jbRound"); // NOI18N

        jbRegister.setAction(actionMap.get("createRegister")); // NOI18N
        jbRegister.setText(resourceMap.getString("jbRegister.text")); // NOI18N
        jbRegister.setName("jbRegister"); // NOI18N

        jbPriceList.setAction(actionMap.get("showPriceListWhole")); // NOI18N
        jbPriceList.setText(resourceMap.getString("jbPriceList.text")); // NOI18N
        jbPriceList.setName("jbPriceList"); // NOI18N

        jbPriceListSelected.setAction(actionMap.get("showPriceListSelected")); // NOI18N
        jbPriceListSelected.setText(resourceMap.getString("jbPriceListSelected.text")); // NOI18N
        jbPriceListSelected.setName("jbPriceListSelected"); // NOI18N

        jbUp.setAction(actionMap.get("up")); // NOI18N
        jbUp.setText(resourceMap.getString("jbUp.text")); // NOI18N
        jbUp.setName("jbUp"); // NOI18N

        jbDown.setAction(actionMap.get("down")); // NOI18N
        jbDown.setText(resourceMap.getString("jbDown.text")); // NOI18N
        jbDown.setName("jbDown"); // NOI18N

        jButton1.setAction(actionMap.get("chooseCodes")); // NOI18N
        jButton1.setText(resourceMap.getString("jButton1.text")); // NOI18N
        jButton1.setName("jButton1"); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 983, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jbAddProduct, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jbDeleteItem)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 109, Short.MAX_VALUE)
                        .addComponent(jlSum, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jlInSum, javax.swing.GroupLayout.PREFERRED_SIZE, 237, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jcbActive)
                        .addGap(18, 18, 18)
                        .addComponent(jbSave)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jbCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jtfNote, javax.swing.GroupLayout.DEFAULT_SIZE, 918, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jbUp)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jbDown, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jtfFilter, javax.swing.GroupLayout.PREFERRED_SIZE, 216, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jbRegister)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jbPriceList)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jbPriceListSelected)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jbRound)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 442, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jbRound)
                    .addComponent(jbRegister)
                    .addComponent(jbPriceList)
                    .addComponent(jbPriceListSelected, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jbUp)
                    .addComponent(jtfFilter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(jButton1)
                    .addComponent(jbDown))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jtfNote, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jbCancel)
                    .addComponent(jbAddProduct)
                    .addComponent(jbDeleteItem)
                    .addComponent(jbSave)
                    .addComponent(jcbActive)
                    .addComponent(jlInSum)
                    .addComponent(jlSum))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    public void close() {

        if (lock == 0) {

            save();

            Session session = HUtil.getSession();
            Incoming x = (Incoming) HUtil.getElement("Incoming", documentCode, session);
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
        Util.closeJIF(this, incomingView, salesView);
        Util.closeJIFTab(this, salesView);
    }

    private void jtIncomingDocMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtIncomingDocMouseClicked
        mouseClicked(evt);
    }//GEN-LAST:event_jtIncomingDocMouseClicked

    private void jtIncomingDocKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtIncomingDocKeyTyped
        if (evt.getKeyCode() == 155) {
            addProduct();
        }
        if (evt.getKeyCode() == 127) {
            deleteProducts();
        }

        if (evt.getKeyChar() == '%' && scanEnter) {
            scanEnter = false;
            Session session = HUtil.getSession();
            String hql = "from Nomenclature n where n.scanCode = '" + scanCode + "'";
            List res = session.createQuery(hql).list();
            if (res.size() > 0) {
                Nomenclature n = (Nomenclature) res.get(0);
                boolean itemNotExists = true;
                for (int i = 0; i < table.size(); i++) {
                    Incomingtable it = (Incomingtable) table.get(i);
                    if (it.getProductCode() == n.getCode()) {
                        Object addQuantity =
                                JOptionPane.showInputDialog(
                                null,
                                "Введите дополнительное количество",
                                "Количество",
                                JOptionPane.QUESTION_MESSAGE,
                                null,
                                null,
                                1);
                        if (Util.checkNumber((String) addQuantity)) {
                            int newQuantity = it.getQuantity() + new Integer(addQuantity.toString());
                            it.setQuantity(newQuantity);
                            it.setInPrice(n.getInPrice());
                            it.setInAmount(n.getInPrice() * newQuantity);
                            it.setNds(20);
                            it.setNdsAmount(Math.round(n.getInPrice() * it.getNds() / 100 * newQuantity));
                            it.setNdsAndAmount(it.getInPrice() * it.getQuantity() + it.getNdsAmount());

                            Double priceNds = new Double(n.getInPrice()) * (100 + it.getNds()) / 100;

                            if (n.getInPrice() != 0) {
                                it.setPerSurcharge(new Double(n.getPrice() - priceNds) / priceNds * 100);
                            } else {
                                it.setPerSurcharge(0);
                            }

                            it.setSurcharge(n.getPrice() - (int) Math.round(priceNds));

                            it.setPrice(n.getPrice());
                            it.setAmount(n.getPrice() * newQuantity);
                            table.set(i, it);
                            showTable();
                            Rectangle rect = jtIncomingDoc.getCellRect(i, 1, true);
                            jtIncomingDoc.scrollRectToVisible(rect);
                            jtIncomingDoc.getSelectionModel().addSelectionInterval(i, i);
                            jtIncomingDoc.getColumnModel().getSelectionModel().setSelectionInterval(1, 1);
                            itemNotExists = false;
                            break;
                        }
                    }
                }
                if (itemNotExists) {
                    addProductItem(n.getCode());
                }
            }
        }

        if (scanEnter) {
            scanCode += evt.getKeyChar();
        }

        if (evt.getKeyChar() == '!') {
            scanEnter = true;
            scanCode = "";
        }

    }//GEN-LAST:event_jtIncomingDocKeyTyped

    private void jtfFilterKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtfFilterKeyReleased
        processFilter();
    }//GEN-LAST:event_jtfFilterKeyReleased

    private void jcbTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jcbTypeActionPerformed
        changeType();
    }//GEN-LAST:event_jcbTypeActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton jbAddProduct;
    private javax.swing.JButton jbCancel;
    private javax.swing.JButton jbDeleteItem;
    private javax.swing.JButton jbDown;
    private javax.swing.JButton jbImport;
    private javax.swing.JButton jbPriceList;
    private javax.swing.JButton jbPriceListSelected;
    private javax.swing.JButton jbRegister;
    private javax.swing.JButton jbRepricing;
    private javax.swing.JButton jbResponsible;
    private javax.swing.JButton jbRound;
    private javax.swing.JButton jbSave;
    private javax.swing.JButton jbSupplier;
    private javax.swing.JButton jbUp;
    private javax.swing.JCheckBox jcbActive;
    private javax.swing.JComboBox jcbType;
    private javax.swing.JLabel jlInSum;
    private javax.swing.JLabel jlRepricing;
    private javax.swing.JLabel jlResponsible;
    private javax.swing.JLabel jlSum;
    private javax.swing.JLabel jlSupplier;
    private javax.swing.JPanel jpDate;
    private javax.swing.JSpinner jsTime;
    private javax.swing.JTable jtIncomingDoc;
    private javax.swing.JTextField jtfFilter;
    private javax.swing.JTextField jtfNote;
    private javax.swing.JTextField jtfNumber;
    private javax.swing.JTextField jtfResponsible;
    private javax.swing.JTextField jtfSupplier;
    // End of variables declaration//GEN-END:variables
    private SalesView salesView;
    private IncomingView incomingView;
    private Integer documentCode;
    private Integer supplierCode;
    private Integer responsibleCode;
    private Integer repricingCode;
    private JDateChooser date;
    private List table;
    private boolean scanEnter;
    private String scanCode;
    private JTextField jTextBox;
    private Integer active;
    private Integer lock;
    private Integer hash;
    private boolean updateTable;
    private boolean choosingCodes;
    private MyTableModelListener tableListener;

    public void showTable() {

        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

        Vector<String> tableHeaders = new Vector<String>();
        Vector tableData = new Vector();
        tableHeaders.add("№");
        tableHeaders.add("Код");
        tableHeaders.add("Наименование");
        tableHeaders.add("Ед.изм.");
        tableHeaders.add("Кол-во");
        tableHeaders.add("Вх.цена");
        tableHeaders.add("Вх.сумма");
        tableHeaders.add("НДС");
        tableHeaders.add("Сумма НДС");
        tableHeaders.add("Сумма с НДС");
        tableHeaders.add("% наценки");
        tableHeaders.add("Наценка");
        tableHeaders.add("Цена");
        tableHeaders.add("Сумма");

        long inSum = 0;
        long sum = 0;
        if (table != null) {
            Session session = HUtil.getSession();
            for (int i = 0; i < table.size(); i++) {
                Incomingtable in = (Incomingtable) table.get(i);
                Vector<Object> oneRow = new Vector<Object>();
                oneRow.add(i + 1);
                oneRow.add(in.getProductCode());
                Nomenclature n = (Nomenclature) HUtil.getElement("Nomenclature", in.getProductCode(), session);
                oneRow.add(in.getProductName());
                if (n != null) {
                    oneRow.add(n.getUnit());
                } else {
                    oneRow.add("");
                }
                oneRow.add(in.getQuantity());
                oneRow.add(in.getInPrice());
                oneRow.add(in.getInAmount());
                oneRow.add(in.getNds());
                oneRow.add(in.getNdsAmount());
                oneRow.add(in.getNdsAndAmount());
                oneRow.add(new Double(Math.round(in.getPerSurcharge() * 100)) / 100);
                oneRow.add(in.getSurcharge());
                oneRow.add(in.getPrice());
                oneRow.add(in.getAmount());
                tableData.add(oneRow);
                inSum += in.getNdsAndAmount();
                sum += in.getAmount();
            }
            session.close();
        }

        updatePrice(sum, inSum);

        jtIncomingDoc.setModel(new MyTableModel(tableData, tableHeaders));
        tuneTable();

        setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

    }

    private void updatePrice(long sum, long inSum) {

        jlInSum.setText("Вх.сумма с НДС: " + (new DecimalFormat("#,###,###")).format(inSum));
        jlSum.setText("Сумма: " + (new DecimalFormat("#,###")).format(sum));

    }

    private class MyTableModelListener implements TableModelListener {

        @Override
        public void tableChanged(TableModelEvent e) {

            if (e.getType() == TableModelEvent.UPDATE) {

                if (updateTable) {

                    int r = e.getFirstRow();
                    int c = e.getColumn();
                    updateTable(r, c);
                }
            }
        }
    }

    private void tuneTable() {

        for (int i = 4; i < 14; i++) {
            jtIncomingDoc.getColumnModel().getColumn(i).setCellEditor(new MyCellEditor(jTextBox));
        }

        Util.autoResizeColWidth(jtIncomingDoc);
        jtIncomingDoc.getColumnModel().getColumn(0).setMinWidth(30);
        jtIncomingDoc.getColumnModel().getColumn(0).setMaxWidth(30);

        tableListener = new MyTableModelListener();
        jtIncomingDoc.getModel().addTableModelListener(tableListener);

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
        }
    }

    @Override
    public void addProductItem(Integer productCode) {

        try {

            Session session = HUtil.getSession();
            Incomingtable it = new Incomingtable();
            Nomenclature n = (Nomenclature) HUtil.getElement("Nomenclature", productCode, session);
            if (n == null) {
                return;
            }
            it.setProductCode(productCode);

            if (documentCode != null) {
                it.setDocumentCode(documentCode);
            }

            it.setProductName(n.getName());
            it.setQuantity(1);
            it.setInPrice(n.getInPrice());
            it.setInAmount(n.getInPrice());
            it.setNds(20);
            it.setNdsAmount(Math.round(n.getInPrice() * it.getNds() / 100));
            it.setNdsAndAmount(it.getInPrice() * it.getQuantity() + it.getNdsAmount());

            Double priceNds = new Double(n.getInPrice()) * (100 + it.getNds()) / 100;

            if (n.getInPrice() != 0) {
                it.setPerSurcharge(new Double(n.getPrice() - priceNds) / priceNds * 100);
            } else {
                it.setPerSurcharge(0);
            }

            it.setSurcharge(n.getPrice() - (int) Math.round(priceNds));

            it.setPrice(n.getPrice());
            it.setAmount(n.getPrice());
            table.add(it);

            session.close();
        } catch (Exception e) {
            logger.error(e);
        }
        showTable();
        Rectangle rect = jtIncomingDoc.getCellRect(jtIncomingDoc.getRowCount() - 1, 1, true);
        jtIncomingDoc.scrollRectToVisible(rect);
        jtIncomingDoc.getSelectionModel().addSelectionInterval(jtIncomingDoc.getRowCount() - 1, jtIncomingDoc.getRowCount() - 1);
        jtIncomingDoc.getColumnModel().getSelectionModel().setSelectionInterval(1, 1);
        jtIncomingDoc.requestFocus();
    }

    public void addProductItem(int productCode, int quantity) {

        try {

            Session session = HUtil.getSession();
            Incomingtable it = new Incomingtable();
            Nomenclature n = (Nomenclature) HUtil.getElement("Nomenclature", productCode, session);
            if (n == null) {
                return;
            }
            it.setProductCode(productCode);

            if (documentCode != null) {
                it.setDocumentCode(documentCode);
            }

            it.setProductName(n.getName());
            it.setQuantity(quantity);

            it.setInPrice(n.getInPrice());
            it.setInAmount(n.getInPrice() * quantity);
            it.setNds(20);
            it.setNdsAmount(Math.round(n.getInPrice() * it.getNds() / 100 * quantity));
            it.setNdsAndAmount(it.getInPrice() * it.getQuantity() + it.getNdsAmount());

            Double priceNds = new Double(n.getInPrice()) * (100 + it.getNds()) / 100;

            if (n.getInPrice() != 0) {
                it.setPerSurcharge(new Double(n.getPrice() - priceNds) / priceNds * 100);
            } else {
                it.setPerSurcharge(0);
            }

            it.setSurcharge(n.getPrice() - (int) Math.round(priceNds));

            it.setPrice(n.getPrice());
            it.setAmount(n.getPrice() * quantity);
            table.add(it);

            session.close();

        } catch (Exception e) {
            logger.error(e);
        }
    }

    @Override
    public void setProductItem(Integer productCode, int r) {

        try {

            Session session = HUtil.getSession();
            Incomingtable it = (Incomingtable) table.get(r);
            Nomenclature n = (Nomenclature) HUtil.getElement("Nomenclature", productCode, session);
            if (n == null) {
                return;
            }
            it.setProductCode(productCode);

            it.setProductName(n.getName());
            it.setInPrice(n.getInPrice());

            Double priceNds = new Double(n.getInPrice()) * (100 + it.getNds()) / 100;

            it.setNdsAmount(Math.round(n.getInPrice() * it.getNds() / 100 * it.getQuantity()));
            it.setNdsAndAmount(it.getInPrice() * it.getQuantity() + it.getNdsAmount());

            if (n.getInPrice() != 0) {
                it.setPerSurcharge(new Double(n.getPrice() - priceNds) / priceNds * 100);
            } else {
                it.setPerSurcharge(0);
            }

            it.setSurcharge(n.getPrice() - (int) Math.round(priceNds));
            it.setPrice(n.getPrice());
            it.setAmount(it.getPrice() * it.getQuantity());

            table.set(r, it);

            session.close();

        } catch (Exception e) {
            logger.error(e);
        }

        showTable();

        if (choosingCodes && lineNumber < table.size()) {
            chooseCodes();
        } else {
            choosingCodes = false;
        }
    }

    @Action
    public void setProductItemPart(Integer productCode, int r) {

        try {
            Session session = HUtil.getSession();
            Incomingtable it = (Incomingtable) table.get(r);
            Nomenclature n = (Nomenclature) HUtil.getElement("Nomenclature", productCode, session);
            if (n == null) {
                return;
            }
            it.setProductCode(productCode);
            it.setProductName(n.getName());

            table.set(r, it);

            session.close();
        } catch (Exception e) {
            logger.error(e);
        }
        showTable();
        if (choosingCodes && lineNumber < table.size()) {
            chooseCodes();
        } else {
            choosingCodes = false;
        }
    }

    @Action
    public void deleteProducts() {
        if (active == 1) {
            JOptionPane.showMessageDialog(
                    null, "Для изменения табличной части запишите документ непроведенным!",
                    "Изменение табличной части", JOptionPane.ERROR_MESSAGE);
        } else if (lock == 0) {
            DefaultTableModel dtm = (DefaultTableModel) jtIncomingDoc.getModel();
            int numRows = jtIncomingDoc.getSelectedRows().length;
            for (int i = 0; i < numRows; i++) {
                int row = jtIncomingDoc.getSelectedRow();
                dtm.removeRow(row);
                table.remove(row);
            }
            showTable();
        }
    }

    public void updateTable(Integer r, Integer c) {

        try {

            Incomingtable it = (Incomingtable) table.get(r);
            if (c == 2) {
                it.setProductName(jtIncomingDoc.getValueAt(r, 2).toString());
            }
            if (c == 4) {
                int q = new Integer(jtIncomingDoc.getValueAt(r, 4).toString());
                it.setQuantity(q);
                it.setInAmount(it.getInPrice() * q);
                it.setNdsAmount((int) Math.round(((double) it.getInPrice() * q * it.getNds()) / 100));
                it.setNdsAndAmount(it.getInAmount() + it.getNdsAmount());
                it.setAmount(it.getPrice() * q);
            }
            if (c == 5) {
                it.setInPrice(new Integer(jtIncomingDoc.getValueAt(r, 5).toString()));
                int q = it.getQuantity();
                it.setInAmount(it.getInPrice() * q);
                it.setNdsAmount((int) Math.round(((double) it.getInPrice() * q * it.getNds()) / 100));
                it.setNdsAndAmount(it.getInAmount() + it.getNdsAmount());
                Double priceNds = new Double(it.getInPrice()) * (100 + it.getNds()) / 100;
                it.setPerSurcharge(((double) (it.getPrice() - priceNds)) / priceNds * 100);
                it.setSurcharge(it.getPrice() - (int) Math.round(priceNds));
            }
            if (c == 6) {
                it.setInAmount(new Integer(jtIncomingDoc.getValueAt(r, 6).toString()));
                int q = it.getQuantity();
                it.setInPrice(it.getInAmount() / q);
                it.setNdsAmount((int) Math.round(((double) it.getInPrice() * q * it.getNds()) / 100));
                it.setNdsAndAmount(it.getInAmount() + it.getNdsAmount());
                Double priceNds = new Double(it.getInPrice()) * (100 + it.getNds()) / 100;
                it.setPerSurcharge(((double) (it.getPrice() - priceNds)) / priceNds * 100);
                it.setSurcharge(it.getPrice() - (int) Math.round(priceNds));
            }
            if (c == 7) {
                it.setNds(new Integer(jtIncomingDoc.getValueAt(r, 7).toString()));
                it.setNdsAmount((int) Math.round((((double) it.getInPrice() * it.getQuantity() * it.getNds()) / 100)));
                it.setNdsAndAmount(it.getInAmount() + it.getNdsAmount());
                Double priceNds = new Double(it.getInPrice()) * (100 + it.getNds()) / 100;
                it.setPerSurcharge(((double) (it.getPrice() - priceNds)) / priceNds * 100);
                it.setSurcharge(it.getPrice() - (int) Math.round(priceNds));
            }
            if (c == 8) {
                it.setNdsAmount((int) Math.round(new Double(jtIncomingDoc.getValueAt(r, 8).toString())));
                it.setNdsAndAmount(it.getInAmount() + it.getNdsAmount());
            }
            if (c == 9) {
                it.setNdsAndAmount(new Integer(jtIncomingDoc.getValueAt(r, 9).toString()));
            }
            if (c == 10) {
                it.setPerSurcharge(new Double(jtIncomingDoc.getValueAt(r, 10).toString()));
                Double priceNds = new Double(it.getInPrice()) * (100 + it.getNds()) / 100;
                it.setSurcharge((int) (((double) Math.round(priceNds) * it.getPerSurcharge()) / 100));
                it.setPrice(
                        (int) Math.round(((double) it.getInPrice() * (100 + it.getNds())) / 100 + it.getSurcharge()));
                it.setAmount(it.getPrice() * it.getQuantity());
            }
            if (c == 11) {
                it.setSurcharge(new Integer(jtIncomingDoc.getValueAt(r, 11).toString()));
                it.setPrice((int) Math.round((double) it.getInPrice() * (100 + it.getNds())) / 100 + it.getSurcharge());
                it.setAmount(it.getPrice() * it.getQuantity());
                Double priceNds = new Double(it.getInPrice()) * (100 + it.getNds()) / 100;
                it.setPerSurcharge(((double) (it.getPrice() - priceNds)) / priceNds * 100);
            }
            if (c == 12) {
                it.setPrice(new Integer(jtIncomingDoc.getValueAt(r, 12).toString()));
                Double priceNds = new Double(it.getInPrice()) * (100 + it.getNds()) / 100;
                it.setPerSurcharge(((double) (it.getPrice() - priceNds)) / priceNds * 100);
                it.setSurcharge(it.getPrice() - (int) Math.round(priceNds));
                it.setAmount(it.getPrice() * it.getQuantity());
            }
            if (c == 13) {
                it.setAmount(new Integer(jtIncomingDoc.getValueAt(r, 13).toString()));
                it.setPrice(it.getAmount() / it.getQuantity());
            }
            table.set(r, it);
            updateRow(r);

        } catch (Exception e) {
            logger.error(e);
        }
    }

    private void updateRow(int r) {

        jtIncomingDoc.getModel().removeTableModelListener(tableListener);

        Incomingtable in = (Incomingtable) table.get(r);

        Vector<Object> oneRow = new Vector<Object>();

        oneRow.add(r + 1);
        oneRow.add(in.getProductCode());
        Nomenclature n = (Nomenclature) HUtil.getElement("Nomenclature", in.getProductCode());
        oneRow.add(in.getProductName());
        if (n != null) {
            oneRow.add(n.getUnit());
        } else {
            oneRow.add("");
        }
        oneRow.add(in.getQuantity());
        oneRow.add(in.getInPrice());
        oneRow.add(in.getInAmount());
        oneRow.add(in.getNds());
        oneRow.add(in.getNdsAmount());
        oneRow.add(in.getNdsAndAmount());
        oneRow.add(new Double(Math.round(in.getPerSurcharge() * 100)) / 100);
        oneRow.add(in.getSurcharge());
        oneRow.add(in.getPrice());
        oneRow.add(in.getAmount());

        for (int i = 0; i < jtIncomingDoc.getColumnCount(); i++) {
            jtIncomingDoc.getModel().setValueAt(oneRow.get(i), r, i);
        }

        tableListener = new MyTableModelListener();
        jtIncomingDoc.getModel().addTableModelListener(tableListener);

        computePrice();

    }

    private void computePrice() {

        long inSum = 0;
        long sum = 0;
        if (table != null) {
            for (int i = 0; i < table.size(); i++) {
                Incomingtable in = (Incomingtable) table.get(i);
                inSum += in.getNdsAndAmount();
                sum += in.getAmount();
            }
        }
        updatePrice(sum, inSum);
    }

    @Action
    public void createRegister() {

        Util.checkDocSaved(this);

        try {
            File file = new File(Util.getAppPath() + "\\templates\\Register.ods");
            final Sheet sheet = SpreadSheet.createFromFile(file).getSheet(0);

            sheet.getCellAt("B2").setValue(HUtil.getConstant("name"));
            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
            sheet.getCellAt("A4").setValue(
                    "Реестр розничных цен № " + jtfNumber.getText() + " от " + sdf.format(date.getDate())
                    + " по ТН(ТТН) № " + jtfNumber.getText() + ", поставщик " + jtfSupplier.getText());

            Session session = HUtil.getSession();

            int ts = table.size();
            if (ts > 1) {
                sheet.duplicateRows(8, 1, ts - 1);
            }
            int amount = 0;
            int i;
            for (i = 0; i < ts; i++) {
                Incomingtable it = (Incomingtable) table.get(i);
                sheet.getCellAt("A" + (9 + i)).setValue(i + 1);
                Nomenclature n = (Nomenclature) HUtil.getElement("Nomenclature", it.getProductCode(), session);
                sheet.getCellAt("B" + (9 + i)).setValue(it.getProductName());
                if (n != null) {
                    sheet.getCellAt("C" + (9 + i)).setValue(n.getUnit());
                }
                sheet.getCellAt("D" + (9 + i)).setValue(((double) it.getInPrice() * (100 + it.getNds())) / 100);

                Double s = new Double(it.getSurcharge()) / ((new Double(it.getInPrice()) * (100 + it.getNds()) / 100)) * 100;

                sheet.getCellAt("E" + (9 + i)).setValue(new DecimalFormat("#.##").format(s) + "%");
                sheet.getCellAt("F" + (9 + i)).setValue(it.getSurcharge());
                sheet.getCellAt("G" + (9 + i)).setValue(it.getPrice());
                sheet.getCellAt("H" + (9 + i)).setValue(it.getQuantity());
                sheet.getCellAt("I" + (9 + i)).setValue(it.getAmount());
                sheet.getElement().setAttribute("height", "style: auto");
                amount += it.getAmount();
            }
            sheet.getCellAt("I" + (9 + i)).setValue(amount);

            Employee e = (Employee) HUtil.getElement("Employee", responsibleCode, session);
            if (e != null) {
                sheet.getCellAt("B" + (13 + i)).setValue("Реестр составил _________________ " + e.getShortName());
            }
            sheet.getCellAt("F" + (13 + i)).setValue("Директор _________________ " + HUtil.getShortNameByCode(HUtil.getIntConstant("director")));

            session.close();

            String r = " " + Math.round(Math.random() * 100000);
            File outputFile = new File(
                    "temp\\Реестр по приходной накладной № "
                    + jtfNumber.getText() + r + ".ods");
            sheet.getSpreadSheet().saveAs(outputFile);
            Util.openDoc(
                    "temp\\Реестр по приходной накладной № "
                    + jtfNumber.getText() + r + ".ods");

        } catch (Exception e) {
            logger.error(e);
        }
    }

    @Override
    public void setFocus() {
        jtIncomingDoc.setRequestFocusEnabled(true);
        jtIncomingDoc.requestFocusInWindow();
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
        this.responsibleCode = employeeCode;
        Session session = HUtil.getSession();
        jtfResponsible.setText(((Employee) HUtil.getElement("Employee", responsibleCode, session)).getName());
        session.close();
    }

    @Override
    public void setSupplier(Integer supplierCode) {
        try {
            this.supplierCode = supplierCode;
            Session session = HUtil.getSession();
            Suppliers s = (Suppliers) HUtil.getElement("Suppliers", supplierCode, session);
            if (s != null) {
                jtfSupplier.setText(s.getName());
            }
            session.close();
        } catch (Exception e) {
            logger.error(e);
        }
    }

    @Action
    public void chooseSupplier() {
        JInternalFrame sv = new SuppliersView(salesView, this);
        salesView.getJDesktopPane().add(sv, javax.swing.JLayeredPane.DEFAULT_LAYER);
        sv.setVisible(true);
        try {
            sv.setSelected(true);
        } catch (java.beans.PropertyVetoException e) {
            logger.error(e);
        }
    }

    private void showPriceList(List priceTable) {

        try {

            int NUM_PRICELIST = Util.getFinalPriceList();

            File file = new File(Util.getAppPath() + "\\templates\\PriceListStandard.ods");
            final Sheet sheet = SpreadSheet.createFromFile(file).getSheet(0);
            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
            Session session = HUtil.getSession();
            int ts = priceTable.size();
            sheet.duplicateRows(0, 5, (int) Math.ceil((ts - 1) / NUM_PRICELIST));
            String companyName = HUtil.getConstant("name");
            int j = 0;
            for (int i = 0; i < ts; i++) {
                Incomingtable it = (Incomingtable) priceTable.get(i);
                Nomenclature n = (Nomenclature) HUtil.getElement("Nomenclature", it.getProductCode(), session);
                String ean = "";
                if (n != null) {
                    String sc = n.getScanCode();
                    if (sc != null && sc.length() > 0) {
                        ean = Util.ean13(sc.substring(0, sc.length() - 1));
                    }
                    int c = i % NUM_PRICELIST * 4;
                    sheet.getCellAt("" + (char) (65 + c) + (j * 5 + 1)).setValue(companyName);
                    sheet.getCellAt("" + (char) (65 + c) + (j * 5 + 2)).setValue(ean);
                    sheet.getCellAt("" + (char) (65 + c) + (j * 5 + 3)).setValue(n.getName());
                    sheet.getCellAt("" + (char) (67 + c) + (j * 5 + 4)).setValue(n.getPrice());
                    sheet.getCellAt("" + (char) (65 + c) + (j * 5 + 5)).setValue(
                            "Прих.накладная №" + jtfNumber.getText() + " от " + sdf.format(date.getDate()));
                }
                if (i % NUM_PRICELIST == NUM_PRICELIST - 1) {
                    j++;
                }
            }

            session.close();

            String r = " " + Math.round(Math.random() * 100000);
            File outputFile = new File(
                    "temp\\Прайс-лист по приходной накладной № " + jtfNumber.getText()
                    + r + ".ods");
            sheet.getSpreadSheet().saveAs(outputFile);
            Util.openDoc(
                    "temp\\Прайс-лист по приходной накладной № " + jtfNumber.getText()
                    + r + ".ods");
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        }
    }

    @Action
    public void showPriceListWhole() {

        Util.checkDocSaved(this);

        showPriceList(table);
    }

    @Action
    public void showPriceListSelected() {

        int firstRow = jtIncomingDoc.getSelectionModel().getMinSelectionIndex();
        int lastRow = jtIncomingDoc.getSelectionModel().getMaxSelectionIndex();
        int firstColumn = jtIncomingDoc.getColumnModel().getSelectionModel().getMinSelectionIndex();
        int lastColumn = jtIncomingDoc.getColumnModel().getSelectionModel().getMaxSelectionIndex();
        ArrayList priceTable = new ArrayList();
        for (int r = firstRow; r <= lastRow; r++) {
            for (int c = firstColumn; c <= lastColumn; c++) {
                if (jtIncomingDoc.isCellSelected(r, c)) {
                    priceTable.add(table.get(r));
                    break;
                }
            }
        }

        showPriceList(priceTable);
    }

    private void typeSelected() {
        if (jcbType.getSelectedIndex() == 0) {
            jlSupplier.setEnabled(true);
            jtfSupplier.setEnabled(true);
            jbSupplier.setEnabled(true);
            jlResponsible.setEnabled(true);
            jtfResponsible.setEnabled(true);
            jbResponsible.setEnabled(true);
        } else {
            jlSupplier.setEnabled(false);
            jtfSupplier.setEnabled(false);
            jbSupplier.setEnabled(false);
            jlResponsible.setEnabled(false);
            jtfResponsible.setEnabled(false);
            jbResponsible.setEnabled(false);
        }
    }

    private void processFilter() {

        jtIncomingDoc.getSelectionModel().clearSelection();
        jtIncomingDoc.getColumnModel().getSelectionModel().clearSelection();

        if (!jtfFilter.getText().trim().isEmpty()) {

            StringTokenizer st = new StringTokenizer(jtfFilter.getText().trim());
            ArrayList ts = new ArrayList();
            while (st.hasMoreTokens()) {
                ts.add(st.nextElement().toString().toLowerCase());
            }

            boolean first = true;
            for (int i = 0; i < table.size(); i++) {
                boolean mark = true;
                String productName = ((Incomingtable) table.get(i)).getProductName().toLowerCase();
                String price = "" + ((Incomingtable) table.get(i)).getPrice();
                for (int j = 0; j < ts.size(); j++) {
                    if (!productName.contains((String) ts.get(j)) && !price.contains((String) ts.get(j))) {
                        mark = false;
                        break;
                    }
                }
                if (mark) {
                    jtIncomingDoc.getSelectionModel().addSelectionInterval(i, i);
                    jtIncomingDoc.getColumnModel().getSelectionModel().setSelectionInterval(0, 12);
                    if (first) {
                        Rectangle rect = jtIncomingDoc.getCellRect(i, 1, true);
                        jtIncomingDoc.scrollRectToVisible(rect);
                        first = false;
                    }
                }
            }
        }
    }

    public void commit() {

        try {
            if (jtIncomingDoc.getSelectedRow() >= 0) {
                updateTable(jtIncomingDoc.getSelectedRow(), jtIncomingDoc.getSelectedColumn());
            }

            Session session = HUtil.getSession();
            session.beginTransaction();

            Incoming i = null;
            if (documentCode != null) {
                i = (Incoming) HUtil.getElement("Incoming", documentCode, session);
            }
            if (i == null) {
                i = new Incoming();
            }

            i.setDatetime(Util.composeDate(date.getDate(), (Date) jsTime.getValue()));
            i.setNumber(jtfNumber.getText());
            i.setContractor(supplierCode);
            i.setResponsible(responsibleCode);
            i.setNote(jtfNote.getText());
            i.setType(jcbType.getSelectedIndex());
            if (lock == 0) {
                i.setLocked(0);
            }
            if (active != 2) {
                if (jcbActive.isSelected()) {
                    i.setActive(1);
                } else {
                    i.setActive(0);
                }
            }

            session.saveOrUpdate(i);

            session.getTransaction().commit();

            session.beginTransaction();

            documentCode = i.getCode();

            List objs = session.createQuery("from Incomingtable it where it.documentCode = " + documentCode).list();

            if (table != null) {
                for (int j = 0; j < table.size(); j++) {
                    Incomingtable it = (Incomingtable) table.get(j);
                    it.setDocumentCode(documentCode);
                    it.setLine(j + 1);
                    boolean newIn = true;
                    for (int k = 0; k < objs.size(); k++) {
                        if (((Incomingtable) objs.get(k)).getCode() == it.getCode()) {
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
                session.delete((Incomingtable) objs.get(j));
            }

            session.getTransaction().commit();

            if (i.getActive() == 1) {

                session.beginTransaction();
                for (int j = 0; j < table.size(); j++) {
                    Incomingtable it = (Incomingtable) table.get(j);
                    HUtil.updatePrice(it.getProductCode(), it.getInPrice(), it.getPrice(), date.getDate(), session);
                    HUtil.updateQuantity(it.getProductCode(), it.getQuantity(), session);

                }
                session.getTransaction().commit();

            } else if (i.getActive() == 0 && active == 1) {

                session.beginTransaction();
                for (int j = 0; j < table.size(); j++) {
                    Incomingtable it = (Incomingtable) table.get(j);
                    HUtil.updateQuantity(it.getProductCode(), -it.getQuantity(), session);
                    Integer[] prices = HUtil.getPriceBeforeDate(it.getProductCode(), date.getDate());
                    if (prices[0] > 0) {
                        HUtil.updatePrice(it.getProductCode(), prices[1], prices[0], date.getDate(), session);
                    }
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

        if (lock == 0) {

            TableCellEditor tce = jtIncomingDoc.getCellEditor();
            if (tce != null) {
                tce.stopCellEditing();
            }

            if (hash == null || !hash.equals(Util.hash(this))) {

                int a = JOptionPane.showOptionDialog(
                        this, "Сохранить документ?", "Сохранение", JOptionPane.YES_NO_CANCEL_OPTION,
                        JOptionPane.QUESTION_MESSAGE, null, new Object[]{"Да", "Нет"}, "Да");

                if (a == 0) {

                    commit();

                    Util.updateJournals(salesView, IncomingView.class);
                    Util.updateJournals(salesView, NomenclatureView.class);
                    salesView.getWorkplace().showNomenclature();
                    jbRepricing.setEnabled(true);
                    hash = Util.hash(this);
                }
            }
        }
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

    @Action
    public void roundPrices() {
        if (jtIncomingDoc.getSelectionModel().getMinSelectionIndex() != -1) {
            for (int i = 0; i < table.size(); i++) {
                if (jtIncomingDoc.getSelectionModel().isSelectedIndex(i)) {
                    Incomingtable it = (Incomingtable) table.get(i);
                    it.setPrice(Util.roundInt(it.getPrice()));
                    Double priceNds = new Double(it.getInPrice()) * (100 + it.getNds()) / 100;
                    it.setPerSurcharge(((double) (it.getPrice() - priceNds)) / priceNds * 100);
                    it.setSurcharge(it.getPrice() - (int) Math.round(priceNds));
                    it.setAmount(it.getPrice() * it.getQuantity());
                }
            }
            showTable();
            Util.selectFirstRow(jtIncomingDoc);
        }
    }

    private void changeType() {
        if (jcbType.getSelectedIndex() == 2 && jtfNumber.getText().isEmpty()) {
            jtfNumber.setText("Ввод остатков");
        }
    }

    private void createRepricingDoc() {

        Session session = HUtil.getSession();

        ArrayList rts = new ArrayList();

        for (int i = 0; i < table.size(); i++) {
            Incomingtable it = (Incomingtable) table.get(i);
            Integer[] beforePrices = HUtil.getPriceBeforeDate(it.getProductCode(), date.getDate());
            if (beforePrices[0] != 0 && it.getPrice() != beforePrices[0]) {
                Repricingtable rt = new Repricingtable();
                rt.setProductCode(it.getProductCode());
                rt.setQuantity(it.getQuantity());
                rt.setStartPrice(beforePrices[0]);
                rt.setNewPrice(it.getPrice());
                rt.setChangePrice((rt.getNewPrice() - rt.getStartPrice()) * rt.getQuantity());
                rts.add(rt);
            }
        }

        if (rts.size() > 0) {

            Repricing r = new Repricing();
            boolean notExists = true;
            if (repricingCode != null) {
                r = (Repricing) HUtil.getElement("Repricing", repricingCode, session);
                if (r != null) {
                    session.beginTransaction();
                    String hql = "from Repricingtable r where r.documentCode = " + r.getCode();
                    List rt = session.createQuery(hql).list();
                    for (int j = 0; j < rt.size(); j++) {
                        session.delete((Repricingtable) rt.get(j));
                    }
                    session.getTransaction().commit();
                    notExists = false;
                }
            }

            if (notExists) {
                session.beginTransaction();
                r.setDatetime(date.getDate());
                r.setNumber(HUtil.getNextDocNumber("Repricing", "", session));
                r.setActive(1);
                r.setLocked(0);
                session.save(r);
                session.getTransaction().commit();
            }

            session.beginTransaction();

            for (int i = 0; i < rts.size(); i++) {
                Repricingtable rt = (Repricingtable) rts.get(i);
                rt.setDocumentCode(r.getCode());
                session.save(rt);
            }

            session.getTransaction().commit();

            session.beginTransaction();

            repricingCode = r.getCode();
            Incoming i = (Incoming) HUtil.getElement("Incoming", documentCode, session);
            i.setRepricing(repricingCode);
            session.merge(i);

            session.getTransaction().commit();

            showRepricingDoc();

            JInternalFrame rdv =
                    new RepricingDocView(salesView, null, repricingCode, null);
            salesView.getJDesktopPane().add(rdv, javax.swing.JLayeredPane.DEFAULT_LAYER);
            rdv.setVisible(true);
            try {
                rdv.setSelected(true);
            } catch (Exception e) {
                logger.error(e);
            }

        } else {
            JOptionPane.showMessageDialog(this, "Нет продуктов для переоценки!", "Акт переоценки", JOptionPane.PLAIN_MESSAGE);
        }
        session.close();

    }

    @Action
    public void createRepricing() {

        Repricing r = (Repricing) HUtil.getElement("Repricing", repricingCode);
        if (r == null) {
            repricingCode = null;
        }

        if (repricingCode != null) {
            int a = JOptionPane.showOptionDialog(
                    this,
                    "Документ переоценки существует! Создать заново?",
                    "Переоценка",
                    JOptionPane.YES_NO_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    new Object[]{"Да", "Нет"}, "Нет");
            if (a == 0) {
                createRepricingDoc();
            }
        } else {
            createRepricingDoc();
        }
    }

    private void showRepricingDoc() {
        if (repricingCode != null) {
            Repricing r = (Repricing) HUtil.getElement("Repricing", repricingCode);
            if (r != null) {
                jlRepricing.setText(
                        "Акт переоценки № " + r.getNumber() + " от " + (new SimpleDateFormat("dd.MM.yy")).format(r.getDatetime()));
            }
        }
    }

    @Action
    public void importDoc() {

        JFileChooser fc = new JFileChooser();
        int returnVal = fc.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {

            File file = fc.getSelectedFile();

            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

            try {

                final Sheet sheet = SpreadSheet.createFromFile(file).getSheet(0);
                Session session = HUtil.getSession();
                if (table.size() > 0) {
                    int a = JOptionPane.showOptionDialog(
                            this, "Очистить документ?", "Импорт", JOptionPane.YES_NO_CANCEL_OPTION,
                            JOptionPane.QUESTION_MESSAGE, null, new Object[]{"Да", "Нет", "Отмена"}, "Нет");
                    if (a == 2) {
                        return;
                    }
                    if (a == 0) {
                        table.clear();
                    }
                }

                boolean rowsPresent = false;

                for (int j = 1; j <= sheet.getRowCount(); j++) {

                    String name = sheet.getCellAt("A" + j).getTextValue().trim();
                    String quantity = sheet.getCellAt("B" + j).getTextValue().trim();
                    String inPrice = sheet.getCellAt("C" + j).getTextValue().trim();

                    if (!name.isEmpty() || !quantity.isEmpty() || !inPrice.isEmpty()) {

                        rowsPresent = true;

                        if (!name.isEmpty()) {

                            Incomingtable it = new Incomingtable();

                            it.setProductName(name);
                            it.setQuantity(Util.str2int(quantity));
                            it.setInPrice(Util.str2int(inPrice));
                            it.setInAmount(it.getQuantity() * it.getInPrice());
                            it.setNds(Util.str2int(HUtil.getConstant("nds")));
                            it.setNdsAndAmount(it.getQuantity() * it.getInPrice() * (100 + it.getNds()) / 100);
                            it.setNdsAmount(it.getNdsAndAmount() - it.getInAmount());

                            String normalizedName = name.replaceAll("'", "%").replaceAll(" ", "%");

                            List ns = HUtil.executeHql(
                                    "from Nomenclature n where n.name like '" + normalizedName + "'", session);
                            if (ns.size() == 1) {
                                Nomenclature n = (Nomenclature) ns.get(0);
                                it.setProductCode(n.getCode());
                            }
                            double margin = HUtil.getConstantDouble("defaultMargin");
                            double priceWithNds = (double) it.getNdsAndAmount() / it.getQuantity();
                            it.setPrice((int) Math.round(priceWithNds * (100 + margin) / 100));
                            it.setAmount(it.getPrice() * it.getQuantity());
                            it.setSurcharge(it.getPrice() - (int) Math.round(priceWithNds));
                            it.setPerSurcharge(((double) it.getSurcharge()) * 100 / priceWithNds);
                            table.add(it);
                        }

                    } else {
                        break;
                    }
                }

                if (table.size() > 0) {
                    showTable();
                } else {
                    if (rowsPresent) {
                        JOptionPane.showMessageDialog(this, "Остутствуют строки с непустым полем имени товара!\nВозможен неверный формат файла.", "Импорт прихода", JOptionPane.INFORMATION_MESSAGE);
                    }
                }

            } catch (Exception e) {
                logger.error(e);
                if (e.getMessage().contains("invalid cell")) {
                    JOptionPane.showMessageDialog(this, "Неверный формат файла импорта!", "Импорт прихода", JOptionPane.ERROR_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Ошибка импорта!", "Импорт прихода", JOptionPane.ERROR_MESSAGE);
                }
            }

            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

            choosingCodes = true;
            chooseCodes();

        }
    }
    private int lineNumber;

    @Action
    public void up() {
        if (lock == 0) {
            int r = jtIncomingDoc.getSelectedRow();
            if (r != -1 && r > 0) {
                Collections.swap(table, r, r - 1);
                updateTable = false;
                Util.swapRows(jtIncomingDoc, r, r - 1);
                Util.move2row(jtIncomingDoc, r - 1);
                updateTable = true;
            }
        }
    }

    @Action
    public void down() {
        if (lock == 0) {
            int r = jtIncomingDoc.getSelectedRow();
            if (r != -1 && r < table.size() - 1) {
                Collections.swap(table, r, r + 1);
                updateTable = false;
                Util.swapRows(jtIncomingDoc, r, r + 1);
                Util.move2row(jtIncomingDoc, r + 1);
                updateTable = true;
            }
        }
    }

    private void mouseClicked(MouseEvent evt) {

        if (lock == 0 && evt.getClickCount() > 1) {

            if (jtIncomingDoc.columnAtPoint(evt.getPoint()) < 2) {

                if (active == 1) {

                    JOptionPane.showMessageDialog(
                            null, "Для изменения табличной части запишите документ непроведенным!",
                            "Изменение табличной части", JOptionPane.ERROR_MESSAGE);
                } else {

                    int r = jtIncomingDoc.getSelectedRow();
                    if (r != -1) {
                        Incomingtable it = (Incomingtable) table.get(r);
                        JInternalFrame nv;
                        if (it.getProductCode() != null) {
                            nv = new NomenclatureView(
                                    salesView, this,
                                    it.getProductCode(),
                                    jtIncomingDoc.getSelectedRow(), it.getProductName(), it.getProductName(), it.getInPrice(), false);
                        } else {
                            nv = new NomenclatureView(
                                    salesView, this,
                                    it.getProductCode(),
                                    jtIncomingDoc.getSelectedRow(), "", "", null, false);
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

            }
        }
    }

    @Action
    public void chooseCodes() {

        Incomingtable it = null;
        boolean l = true;
        while (l && lineNumber < table.size()) {
            it = (Incomingtable) table.get(lineNumber);
            l = it.getProductCode() != null;
            if (l) {
                lineNumber++;
            }
        }

        if (lineNumber < table.size()) {
            NomenclatureView nv = new NomenclatureView(
                    salesView, this, it.getProductCode(), lineNumber, it.getProductName(), it.getProductName(),
                    it.getInPrice(), true);

            salesView.getJDesktopPane().add(nv, javax.swing.JLayeredPane.DEFAULT_LAYER);
            nv.setVisible(true);
            try {
                nv.setSelected(true);
            } catch (java.beans.PropertyVetoException e) {
                logger.error(e);
            }

            lineNumber++;
        }
    }
}
