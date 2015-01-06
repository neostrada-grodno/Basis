package sales.paymentorder;

import com.toedter.calendar.JDateChooser;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import javax.swing.GroupLayout;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import org.apache.log4j.Logger;
import org.hibernate.classic.Session;
import org.jdesktop.application.Action;
import org.jopendocument.dom.spreadsheet.Sheet;
import org.jopendocument.dom.spreadsheet.SpreadSheet;
import sales.SalesApp;
import sales.SalesView;
import sales.catalogs.SuppliersView;
import sales.entity.Banks;
import sales.entity.Paymentorder;
import sales.entity.Suppliers;
import sales.interfaces.IClose;
import sales.interfaces.ISupplier;
import sales.util.HUtil;
import sales.util.Util;
import sales.util.fwMoney;

public class PaymentOrderDocView extends JInternalFrame implements ISupplier, IClose {

    static Logger logger = Logger.getLogger(SalesApp.class.getName());

    public PaymentOrderDocView(SalesView salesView, PaymentOrderView paymentOrderView, Integer documentCode) {
        initComponents();

        Util.initJIF(this, "Платежное поручение", paymentOrderView, salesView);

        this.salesView = salesView;
        this.paymentOrderView = paymentOrderView;
        this.documentCode = documentCode;

        jcbPaymentScheme.addItem("По умолчанию");
        jcbPaymentScheme.addItem("Подоходный налог");
        jcbPaymentScheme.addItem("ФЗН");
        jcbPaymentScheme.addItem("Белгосстрах");
        jcbPaymentScheme.addItem("Налог");
        jcbPaymentScheme.addItem("Произвольная");

        jtfAmount.addKeyListener(new KeyAdapter() {

            @Override
            public void keyTyped(KeyEvent e) {
                jtfAmount.setEditable(Character.isDigit(e.getKeyChar()) || e.getKeyChar() == KeyEvent.VK_BACK_SPACE);
            }
        });

        Session session = HUtil.getSession();
        if (documentCode != null) {
            Paymentorder po = (Paymentorder) HUtil.getElement("Paymentorder", documentCode, session);
            if (po != null) {
                supplierCode = po.getReceiver();
                if (supplierCode != null) {
                    Suppliers s = (Suppliers) HUtil.getElement("Suppliers", supplierCode, session);
                    if (s != null) {
                        jtfSupplier.setText(s.getName());
                    }
                }
                jtfNumber.setText(po.getNumber());
                date = new JDateChooser(po.getDatetime());
                jepPurpose.setText(po.getPurpose());
                jtfAmount.setText(po.getAmount().toString());
                jcbPaymentScheme.setSelectedIndex(po.getPaymentScheme());
                jtfPaymentCode.setText(po.getPaymentCode());
                jtfPaymentOrder.setText(po.getPaymentOrder());
                lock = po.getLocked();
                if (lock == 1) {
                    JOptionPane.showMessageDialog(
                            this,
                            "Элемент редактируется другим пользователем и будет открыт только для просмотра!",
                            "Блокировка", JOptionPane.PLAIN_MESSAGE);
                    jtfNumber.setEditable(false);
                    date.setEnabled(false);
                    jbEmployee.setEnabled(false);
                    jepPurpose.setEditable(false);
                    jtfAmount.setEditable(false);
                    jcbPaymentScheme.setEnabled(false);
                    jtfPaymentCode.setEditable(false);
                    jtfPaymentOrder.setEditable(false);
                    jbSave.setEnabled(false);
                } else {
                    session.beginTransaction();
                    po.setLocked(0);
                    session.merge(po);
                    session.getTransaction().commit();
                }
            }

        } else {
            jtfNumber.setText(HUtil.getNextDocNumber("Paymentorder", "", session));
            Calendar cl = Calendar.getInstance();
            date = new JDateChooser(cl.getTime());
            jcbPaymentScheme.setSelectedIndex(0);
            jtfPaymentCode.setText("");
            jtfPaymentOrder.setText("22");
            lock = 0;
        }
        session.close();

        GroupLayout gl = (GroupLayout) jpDate.getLayout();
        gl.setHorizontalGroup(gl.createParallelGroup().addGroup(gl.createSequentialGroup().addComponent(date)));
        gl.setVerticalGroup(gl.createParallelGroup().addGroup(gl.createSequentialGroup().addComponent(date)));

        jcbPaymentScheme.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent evt) {
                if (evt.getStateChange() == ItemEvent.SELECTED) {
                    paymentSchemeSelected();
                }
            }
        });

        if(documentCode != null) {
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
        jLabel4 = new javax.swing.JLabel();
        jtfSupplier = new javax.swing.JTextField();
        jbEmployee = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        jtfAmount = new javax.swing.JTextField();
        jbSave = new javax.swing.JButton();
        jbCancel = new javax.swing.JButton();
        jbPrint = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jepPurpose = new javax.swing.JEditorPane();
        jLabel3 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jpPayment = new javax.swing.JPanel();
        jlPaymentScheme = new javax.swing.JLabel();
        jcbPaymentScheme = new javax.swing.JComboBox();
        jlPaymentCode = new javax.swing.JLabel();
        jlPaymentOrder = new javax.swing.JLabel();
        jtfPaymentCode = new javax.swing.JTextField();
        jtfPaymentOrder = new javax.swing.JTextField();

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(sales.SalesApp.class).getContext().getResourceMap(PaymentOrderDocView.class);
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
            .addGap(0, 93, Short.MAX_VALUE)
        );
        jpDateLayout.setVerticalGroup(
            jpDateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 20, Short.MAX_VALUE)
        );

        jLabel4.setText(resourceMap.getString("jLabel4.text")); // NOI18N
        jLabel4.setName("jLabel4"); // NOI18N

        jtfSupplier.setBackground(resourceMap.getColor("jtfSupplier.background")); // NOI18N
        jtfSupplier.setEditable(false);
        jtfSupplier.setText(resourceMap.getString("jtfSupplier.text")); // NOI18N
        jtfSupplier.setName("jtfSupplier"); // NOI18N

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(sales.SalesApp.class).getContext().getActionMap(PaymentOrderDocView.class, this);
        jbEmployee.setAction(actionMap.get("chooseSupplier")); // NOI18N
        jbEmployee.setText(resourceMap.getString("jbEmployee.text")); // NOI18N
        jbEmployee.setName("jbEmployee"); // NOI18N

        jLabel5.setText(resourceMap.getString("jLabel5.text")); // NOI18N
        jLabel5.setName("jLabel5"); // NOI18N

        jtfAmount.setText(resourceMap.getString("jtfAmount.text")); // NOI18N
        jtfAmount.setName("jtfAmount"); // NOI18N

        jbSave.setAction(actionMap.get("saveDoc")); // NOI18N
        jbSave.setText(resourceMap.getString("jbSave.text")); // NOI18N
        jbSave.setName("jbSave"); // NOI18N

        jbCancel.setAction(actionMap.get("closeDoc")); // NOI18N
        jbCancel.setText(resourceMap.getString("jbCancel.text")); // NOI18N
        jbCancel.setName("jbCancel"); // NOI18N

        jbPrint.setAction(actionMap.get("print")); // NOI18N
        jbPrint.setText(resourceMap.getString("jbPrint.text")); // NOI18N
        jbPrint.setName("jbPrint"); // NOI18N

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        jepPurpose.setName("jepPurpose"); // NOI18N
        jScrollPane1.setViewportView(jepPurpose);

        jLabel3.setText(resourceMap.getString("jLabel3.text")); // NOI18N
        jLabel3.setName("jLabel3"); // NOI18N

        jLabel6.setText(resourceMap.getString("jLabel6.text")); // NOI18N
        jLabel6.setName("jLabel6"); // NOI18N

        jpPayment.setBackground(resourceMap.getColor("jpPayment.background")); // NOI18N
        jpPayment.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jpPayment.setName("jpPayment"); // NOI18N

        jlPaymentScheme.setText(resourceMap.getString("jlPaymentScheme.text")); // NOI18N
        jlPaymentScheme.setName("jlPaymentScheme"); // NOI18N

        jcbPaymentScheme.setName("jcbPaymentScheme"); // NOI18N

        jlPaymentCode.setText(resourceMap.getString("jlPaymentCode.text")); // NOI18N
        jlPaymentCode.setName("jlPaymentCode"); // NOI18N

        jlPaymentOrder.setText(resourceMap.getString("jlPaymentOrder.text")); // NOI18N
        jlPaymentOrder.setName("jlPaymentOrder"); // NOI18N

        jtfPaymentCode.setName("jtfPaymentCode"); // NOI18N

        jtfPaymentOrder.setName("jtfPaymentOrder"); // NOI18N

        javax.swing.GroupLayout jpPaymentLayout = new javax.swing.GroupLayout(jpPayment);
        jpPayment.setLayout(jpPaymentLayout);
        jpPaymentLayout.setHorizontalGroup(
            jpPaymentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpPaymentLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jpPaymentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jlPaymentOrder)
                    .addGroup(jpPaymentLayout.createSequentialGroup()
                        .addComponent(jlPaymentScheme)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jcbPaymentScheme, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jlPaymentCode)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jpPaymentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jtfPaymentOrder, javax.swing.GroupLayout.DEFAULT_SIZE, 126, Short.MAX_VALUE)
                    .addComponent(jtfPaymentCode, javax.swing.GroupLayout.DEFAULT_SIZE, 126, Short.MAX_VALUE))
                .addContainerGap())
        );
        jpPaymentLayout.setVerticalGroup(
            jpPaymentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpPaymentLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jpPaymentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jlPaymentScheme)
                    .addComponent(jcbPaymentScheme, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jlPaymentCode)
                    .addComponent(jtfPaymentCode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jpPaymentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jlPaymentOrder)
                    .addComponent(jtfPaymentOrder, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(9, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(67, 67, 67)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jtfNumber, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(96, 96, 96))
            .addGroup(layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4)
                            .addComponent(jLabel3))
                        .addGap(4, 4, 4))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 387, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jtfSupplier, javax.swing.GroupLayout.DEFAULT_SIZE, 354, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jbEmployee, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jtfAmount, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(263, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addComponent(jpPayment, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(216, Short.MAX_VALUE)
                .addComponent(jbPrint)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jbSave)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jbCancel)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel1)
                        .addComponent(jLabel2))
                    .addComponent(jtfNumber)
                    .addComponent(jpDate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jtfSupplier, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jbEmployee))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel6))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 122, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jtfAmount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpPayment, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jbPrint)
                    .addComponent(jbSave)
                    .addComponent(jbCancel))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    public void save() {

        if (lock == 0) {
            if (hash == null || !hash.equals(Util.hash(this))) {
                int a = JOptionPane.showOptionDialog(
                        this, "Сохранить документ?", "Сохранение", JOptionPane.YES_NO_CANCEL_OPTION,
                        JOptionPane.QUESTION_MESSAGE, null, new Object[]{"Да", "Нет"}, "Да");
                if (a == 0) {
                    try {
                        Session session = HUtil.getSession();
                        session.beginTransaction();
                        Paymentorder po = null;
                        if (documentCode != null) {
                            po = (Paymentorder) HUtil.getElement("Paymentorder", documentCode, session);
                        }
                        if (po == null) {
                            po = new Paymentorder();
                        }
                        po.setDatetime(date.getDate());
                        po.setNumber(jtfNumber.getText());
                        po.setReceiver(supplierCode);
                        po.setPurpose(jepPurpose.getText());
                        po.setAmount(new Integer(jtfAmount.getText()));
                        po.setPaymentScheme(jcbPaymentScheme.getSelectedIndex());
                        po.setPaymentCode(jtfPaymentCode.getText());
                        po.setPaymentOrder(jtfPaymentOrder.getText());

                        if (documentCode == null) {
                            session.save(po);
                        } else {
                            session.merge(po);
                        }
                        documentCode = po.getCode();
                        session.getTransaction().commit();
                        session.close();
                        Util.updateJournals(salesView, PaymentOrderView.class);
                        hash = Util.hash(this);
                    } catch (Exception e) {
                        logger.error(e);
                    }
                }
            }
        }
    }

    public void close() {

        if (lock == 0) {

            save();

            Session session = HUtil.getSession();
            Paymentorder x = (Paymentorder) HUtil.getElement("Paymentorder", documentCode, session);
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
        Util.closeJIF(this, paymentOrderView, salesView);
        Util.closeJIFTab(this, salesView);
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
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton jbCancel;
    private javax.swing.JButton jbEmployee;
    private javax.swing.JButton jbPrint;
    private javax.swing.JButton jbSave;
    private javax.swing.JComboBox jcbPaymentScheme;
    private javax.swing.JEditorPane jepPurpose;
    private javax.swing.JLabel jlPaymentCode;
    private javax.swing.JLabel jlPaymentOrder;
    private javax.swing.JLabel jlPaymentScheme;
    private javax.swing.JPanel jpDate;
    private javax.swing.JPanel jpPayment;
    private javax.swing.JTextField jtfAmount;
    private javax.swing.JTextField jtfNumber;
    private javax.swing.JTextField jtfPaymentCode;
    private javax.swing.JTextField jtfPaymentOrder;
    private javax.swing.JTextField jtfSupplier;
    // End of variables declaration//GEN-END:variables
    private SalesView salesView;
    private PaymentOrderView paymentOrderView;
    private Integer documentCode;
    private JDateChooser date;
    private Integer supplierCode;
    private Integer lock;
    private Integer hash;

    @Action
    public void print() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        try {
            Session session = HUtil.getSession();

            File file = new File(Util.getAppPath() + "\\templates\\PaymentOrder.ods");
            final Sheet sheet = SpreadSheet.createFromFile(file).getSheet(0);

            sheet.getCellAt("B1").setValue("ПЛАТЕЖНОЕ ПОРУЧЕНИЕ № " + jtfNumber.getText());
            sheet.getCellAt("U1").setValue(sdf.format(date.getDate()));
            fwMoney m = new fwMoney(jtfAmount.getText());
            sheet.getCellAt("B3").setValue("Сумма и валюта: " + m.num2str());
            sheet.getCellAt("AB4").setValue(jtfAmount.getText());
            sheet.getCellAt("E5").setValue(HUtil.getConstant("name") + ", " + HUtil.getConstant("legalAddress"));
            sheet.getCellAt("AB7").setValue(HUtil.getConstant("account"));
            sheet.getCellAt("G8").setValue(HUtil.getConstant("bank"));
            sheet.getCellAt("AB9").setValue(HUtil.getConstant("bankCode"));

            Suppliers s = (Suppliers) HUtil.getElement("Suppliers", supplierCode, session);
            if (s != null) {
                Banks b = (Banks) HUtil.getElement("Banks", s.getBank(), session);
                if (b != null) {
                    sheet.getCellAt("G10").setValue(b.getName() + ", " + b.getAddress());
                    sheet.getCellAt("AB11").setValue(b.getBankCode());
                    sheet.getCellAt("E12").setValue(s.getName() + ", " + s.getAddress());
                    sheet.getCellAt("AB14").setValue(s.getAccount());
                    sheet.getCellAt("I17").setValue(s.getUnt());
                }
            }

            session.close();

            sheet.getCellAt("B15").setValue("Назначение платежа: " + jepPurpose.getText());
            sheet.getCellAt("B17").setValue(HUtil.getConstant("unt"));

            sheet.getCellAt("Y17").setValue(jtfPaymentCode.getText());
            sheet.getCellAt("AG17").setValue(jtfPaymentOrder.getText());

            String r = " " + Math.round(Math.random() * 100000);
            File outputFile = new File("temp\\Платежное поручение № " + jtfNumber.getText() + r + ".ods");
            sheet.getSpreadSheet().saveAs(outputFile);
            Util.openDoc("temp\\Платежное поручение № " + jtfNumber.getText() + r + ".ods");

        } catch (Exception e) {
            logger.error(e);
        }
    }

    @Override
    public void setSupplier(Integer supplierCode) {
        this.supplierCode = supplierCode;
        Session session = HUtil.getSession();
        Suppliers s = (Suppliers) HUtil.getElement("Suppliers", supplierCode, session);
        if (s != null) {
            jtfSupplier.setText(s.getName());
        }
        session.close();
    }

    private void paymentSchemeSelected() {
        if (jcbPaymentScheme.getSelectedIndex() == 0) {
            jtfPaymentCode.setText("");
            jtfPaymentOrder.setText("22");
        } else if (jcbPaymentScheme.getSelectedIndex() == 1) {
            jtfPaymentCode.setText("00101");
            jtfPaymentOrder.setText("13");
        } else if (jcbPaymentScheme.getSelectedIndex() == 2) {
            jtfPaymentCode.setText("03511");
            jtfPaymentOrder.setText("13");
        } else if (jcbPaymentScheme.getSelectedIndex() == 3) {
            jtfPaymentCode.setText("");
            jtfPaymentOrder.setText("22");
        } else if (jcbPaymentScheme.getSelectedIndex() == 4) {
            jtfPaymentCode.setText("01201");
            jtfPaymentOrder.setText("13");
        }
    }

    @Action
    public void saveDoc() {
        save();
    }
}
