package sales.register;

import com.toedter.calendar.JDateChooser;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
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
import sales.catalogs.EmployeeView;
import sales.interfaces.IEmployee;
import sales.entity.Employee;
import sales.entity.Register;
import sales.interfaces.IClose;
import sales.interfaces.IHashAndSave;
import sales.util.HUtil;
import sales.util.Util;
import sales.util.fwMoney;

public class RegisterDocView extends JInternalFrame implements IEmployee, IHashAndSave, IClose {

    static Logger logger = Logger.getLogger(SalesApp.class.getName());

    public RegisterDocView(SalesView salesView, RegisterView registerView, Integer docCode, Integer docType) {
        initComponents();

        Util.initJIF(this, "Кассовый ордер", registerView, salesView);

        this.salesView = salesView;
        this.registerView = registerView;
        this.documentCode = docCode;

        jcbType.addItem("Приходный кассовый ордер");
        jcbType.addItem("Расходный кассовый ордер");
        jcbPaymentType.addItem("Торговая выручка для сдачи в банк");
        jcbPaymentType.addItem("Зарплата");
        jcbPaymentType.addItem("");

        jtfAmount.addKeyListener(new KeyAdapter() {

            @Override
            public void keyTyped(KeyEvent e) {
                jtfAmount.setEditable(Character.isDigit(e.getKeyChar()) || e.getKeyChar() == KeyEvent.VK_BACK_SPACE);
            }
        });

        Session session = HUtil.getSession();
        if (documentCode != null) {
            Register r = (Register) HUtil.getElement("Register", documentCode, session);
            jtfNumber.setText(r.getNumber());
            if (r.getDocumentType() == 1 && r.getSupplement() != null) {
                jtfSupplement.setText(r.getSupplement());
            }

            date = new JDateChooser(r.getDatetime());
            jcbType.setSelectedIndex(r.getDocumentType());
            jcbPaymentType.setSelectedItem(r.getPaymentType());
            active = r.getActive();
            if (active == 1) {
                jcbActive.setSelected(true);
            } else if (active == 2) {
                jcbActive.setEnabled(false);
            }
            employeeCode = r.getEmployeeCode();
            Employee e = (Employee) HUtil.getElement("Employee", employeeCode, session);
            if (e != null) {
                jtfEmployee.setText(e.getName());
            }
            jtfAmount.setText(r.getAmount().toString());
            jtfCorrAccount.setText(r.getCorrAccount());
            jtfAnnouncementNumber.setText(r.getAnnouncementNumber());
            jtfReportingCode.setText(r.getReportingCode());
            lock = r.getLocked();
            if (lock == 1) {
                JOptionPane.showMessageDialog(
                        this,
                        "Элемент редактируется другим пользователем и будет открыт только для просмотра!",
                        "Блокировка", JOptionPane.PLAIN_MESSAGE);
                jtfNumber.setEditable(false);
                date.setEnabled(false);
                jcbType.setEditable(false);
                jbEmployee.setEnabled(false);
                jtfAmount.setEditable(false);
                jtfCorrAccount.setEditable(false);
                jcbPaymentType.setEnabled(false);
                jtfSupplement.setEditable(false);
                jtfAnnouncementNumber.setEditable(false);
                jtfReportingCode.setEditable(false);
                jcbActive.setEnabled(false);
                jbSave.setEnabled(false);
            } else {
                session.beginTransaction();
                r.setLocked(1);
                session.merge(r);
                session.getTransaction().commit();
            }

        } else {
            Calendar cl = Calendar.getInstance();
            date = new JDateChooser(cl.getTime());
            jcbType.setSelectedIndex(docType);
            jcbPaymentType.setSelectedIndex(0);
            jtfCorrAccount.setText("51");
            List res = HUtil.executeHql("from Register x where x.documentType = 1 order by x.code desc");
            if (res.size() > 0) {
                Register r = (Register) res.get(0);
                jtfSupplement.setText(r.getSupplement());
                jtfAnnouncementNumber.setText(r.getAnnouncementNumber());
                jtfReportingCode.setText(r.getReportingCode());
            }
            lock = 0;
        }
        session.close();

        GroupLayout gl = (GroupLayout) jpDate.getLayout();
        gl.setHorizontalGroup(gl.createParallelGroup().addGroup(gl.createSequentialGroup().addComponent(date)));
        gl.setVerticalGroup(gl.createParallelGroup().addGroup(gl.createSequentialGroup().addComponent(date)));

        jcbType.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent evt) {
                if (evt.getStateChange() == ItemEvent.SELECTED) {
                    if (documentCode == null) {
                        int a = JOptionPane.showOptionDialog(
                                null, "Вычислить следующий номер документа?", "Восстановление", JOptionPane.YES_NO_CANCEL_OPTION,
                                JOptionPane.QUESTION_MESSAGE, null, new Object[]{"Да", "Нет"}, "Нет");
                        if (a == 0) {
                            typeSelected();
                        }
                    }
                }
            }
        });

        typeSelected();

        jcbPaymentType.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent evt) {
                if (evt.getStateChange() == ItemEvent.SELECTED) {
                    if (jcbPaymentType.getSelectedIndex() == 1) {
                        jtfCorrAccount.setText("70.1");
                    } else {
                        jtfCorrAccount.setText("51");
                    }
                }
            }
        });

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
        jcbType = new javax.swing.JComboBox();
        jLabel4 = new javax.swing.JLabel();
        jtfEmployee = new javax.swing.JTextField();
        jbEmployee = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        jtfAmount = new javax.swing.JTextField();
        jbSave = new javax.swing.JButton();
        jbCancel = new javax.swing.JButton();
        jcbActive = new javax.swing.JCheckBox();
        jbPrint = new javax.swing.JButton();
        jlReceipt = new javax.swing.JLabel();
        jtfSupplement = new javax.swing.JTextField();
        jcbPaymentType = new javax.swing.JComboBox();
        jlPaymentType = new javax.swing.JLabel();
        jbCashAnnouncement = new javax.swing.JButton();
        jpAnnouncement = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jtfAnnouncementNumber = new javax.swing.JTextField();
        jtfReportingCode = new javax.swing.JTextField();
        jlCorrAccount = new javax.swing.JLabel();
        jtfCorrAccount = new javax.swing.JTextField();
        jbPrintCashBook = new javax.swing.JButton();

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(sales.SalesApp.class).getContext().getResourceMap(RegisterDocView.class);
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

        jpDate.setBackground(resourceMap.getColor("Form.background")); // NOI18N
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

        jLabel3.setText(resourceMap.getString("jLabel3.text")); // NOI18N
        jLabel3.setName("jLabel3"); // NOI18N

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(sales.SalesApp.class).getContext().getActionMap(RegisterDocView.class, this);
        jcbType.setAction(actionMap.get("jcbTypePressed")); // NOI18N
        jcbType.setName("jcbType"); // NOI18N

        jLabel4.setText(resourceMap.getString("jLabel4.text")); // NOI18N
        jLabel4.setName("jLabel4"); // NOI18N

        jtfEmployee.setBackground(resourceMap.getColor("Form.background")); // NOI18N
        jtfEmployee.setEditable(false);
        jtfEmployee.setText(resourceMap.getString("jtfEmployee.text")); // NOI18N
        jtfEmployee.setName("jtfEmployee"); // NOI18N

        jbEmployee.setAction(actionMap.get("Employee")); // NOI18N
        jbEmployee.setText(resourceMap.getString("jbEmployee.text")); // NOI18N
        jbEmployee.setName("jbEmployee"); // NOI18N

        jLabel5.setText(resourceMap.getString("jLabel5.text")); // NOI18N
        jLabel5.setName("jLabel5"); // NOI18N

        jtfAmount.setText(resourceMap.getString("jtfAmount.text")); // NOI18N
        jtfAmount.setName("jtfAmount"); // NOI18N

        jbSave.setAction(actionMap.get("OK")); // NOI18N
        jbSave.setText(resourceMap.getString("jbSave.text")); // NOI18N
        jbSave.setName("jbSave"); // NOI18N

        jbCancel.setAction(actionMap.get("closeDoc")); // NOI18N
        jbCancel.setText(resourceMap.getString("jbCancel.text")); // NOI18N
        jbCancel.setName("jbCancel"); // NOI18N

        jcbActive.setBackground(resourceMap.getColor("Form.background")); // NOI18N
        jcbActive.setText(resourceMap.getString("jcbActive.text")); // NOI18N
        jcbActive.setName("jcbActive"); // NOI18N

        jbPrint.setAction(actionMap.get("print")); // NOI18N
        jbPrint.setText(resourceMap.getString("jbPrint.text")); // NOI18N
        jbPrint.setName("jbPrint"); // NOI18N

        jlReceipt.setText(resourceMap.getString("jlReceipt.text")); // NOI18N
        jlReceipt.setName("jlReceipt"); // NOI18N

        jtfSupplement.setText(resourceMap.getString("jtfSupplement.text")); // NOI18N
        jtfSupplement.setName("jtfSupplement"); // NOI18N

        jcbPaymentType.setEditable(true);
        jcbPaymentType.setName("jcbPaymentType"); // NOI18N

        jlPaymentType.setText(resourceMap.getString("jlPaymentType.text")); // NOI18N
        jlPaymentType.setName("jlPaymentType"); // NOI18N

        jbCashAnnouncement.setAction(actionMap.get("printCashAnnouncement")); // NOI18N
        jbCashAnnouncement.setText(resourceMap.getString("jbCashAnnouncement.text")); // NOI18N
        jbCashAnnouncement.setName("jbCashAnnouncement"); // NOI18N

        jpAnnouncement.setBackground(resourceMap.getColor("jpAnnouncement.background")); // NOI18N
        jpAnnouncement.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jpAnnouncement.setName("jpAnnouncement"); // NOI18N

        jLabel7.setText(resourceMap.getString("jLabel7.text")); // NOI18N
        jLabel7.setName("jLabel7"); // NOI18N

        jLabel8.setText(resourceMap.getString("jLabel8.text")); // NOI18N
        jLabel8.setName("jLabel8"); // NOI18N

        jLabel9.setText(resourceMap.getString("jLabel9.text")); // NOI18N
        jLabel9.setName("jLabel9"); // NOI18N

        jtfAnnouncementNumber.setText(resourceMap.getString("jtfAnnouncementNumber.text")); // NOI18N
        jtfAnnouncementNumber.setName("jtfAnnouncementNumber"); // NOI18N

        jtfReportingCode.setText(resourceMap.getString("jtfReportingCode.text")); // NOI18N
        jtfReportingCode.setName("jtfReportingCode"); // NOI18N

        javax.swing.GroupLayout jpAnnouncementLayout = new javax.swing.GroupLayout(jpAnnouncement);
        jpAnnouncement.setLayout(jpAnnouncementLayout);
        jpAnnouncementLayout.setHorizontalGroup(
            jpAnnouncementLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpAnnouncementLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jpAnnouncementLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jpAnnouncementLayout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel9))
                    .addComponent(jLabel7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jpAnnouncementLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jtfReportingCode, javax.swing.GroupLayout.DEFAULT_SIZE, 56, Short.MAX_VALUE)
                    .addComponent(jtfAnnouncementNumber, javax.swing.GroupLayout.DEFAULT_SIZE, 56, Short.MAX_VALUE))
                .addContainerGap())
        );
        jpAnnouncementLayout.setVerticalGroup(
            jpAnnouncementLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpAnnouncementLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jpAnnouncementLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(jtfAnnouncementNumber, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jpAnnouncementLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(jLabel9)
                    .addComponent(jtfReportingCode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jlCorrAccount.setText(resourceMap.getString("jlCorrAccount.text")); // NOI18N
        jlCorrAccount.setName("jlCorrAccount"); // NOI18N

        jtfCorrAccount.setText(resourceMap.getString("jtfCorrAccount.text")); // NOI18N
        jtfCorrAccount.setName("jtfCorrAccount"); // NOI18N

        jbPrintCashBook.setAction(actionMap.get("printCashBook")); // NOI18N
        jbPrintCashBook.setText(resourceMap.getString("jbPrintCashBook.text")); // NOI18N
        jbPrintCashBook.setName("jbPrintCashBook"); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(41, 41, 41)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jtfNumber, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(95, 95, 95))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel4)
                            .addComponent(jLabel3)
                            .addComponent(jLabel5)
                            .addComponent(jlPaymentType))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jcbPaymentType, 0, 233, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jlReceipt))
                            .addComponent(jcbType, 0, 300, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jtfEmployee, javax.swing.GroupLayout.DEFAULT_SIZE, 267, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jbEmployee, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jtfAmount, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jlCorrAccount)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jtfCorrAccount, javax.swing.GroupLayout.DEFAULT_SIZE, 145, Short.MAX_VALUE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jpAnnouncement, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jtfSupplement, javax.swing.GroupLayout.DEFAULT_SIZE, 169, Short.MAX_VALUE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jbPrint)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jbPrintCashBook)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jbCashAnnouncement)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jcbActive)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jbSave, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(7, 7, 7)
                        .addComponent(jbCancel)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jtfNumber, javax.swing.GroupLayout.DEFAULT_SIZE, 22, Short.MAX_VALUE)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jpDate, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(5, 5, 5)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jcbType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(jtfEmployee, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jbEmployee))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(jtfAmount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jlCorrAccount)
                            .addComponent(jtfCorrAccount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jpAnnouncement, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(14, 14, 14)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jlPaymentType)
                    .addComponent(jcbPaymentType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jtfSupplement, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jlReceipt))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jcbActive)
                    .addComponent(jbPrint)
                    .addComponent(jbCancel)
                    .addComponent(jbSave)
                    .addComponent(jbCashAnnouncement)
                    .addComponent(jbPrintCashBook))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    @Action
    public void OK() {
        save();
    }

    public void close() {

        if (lock == 0) {

            save();

            Session session = HUtil.getSession();
            Register x = (Register) HUtil.getElement("Register", documentCode, session);
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
        Util.closeJIF(this, registerView, salesView);
        Util.closeJIFTab(this, salesView);
    }

    @Action
    public void Employee() {
        JInternalFrame ev = new EmployeeView(salesView, this, "Employee");
        salesView.getJDesktopPane().add(ev, javax.swing.JLayeredPane.DEFAULT_LAYER);
        ev.setVisible(true);
        try {
            ev.setSelected(true);
        } catch (java.beans.PropertyVetoException e) {
        }
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JButton jbCancel;
    private javax.swing.JButton jbCashAnnouncement;
    private javax.swing.JButton jbEmployee;
    private javax.swing.JButton jbPrint;
    private javax.swing.JButton jbPrintCashBook;
    private javax.swing.JButton jbSave;
    private javax.swing.JCheckBox jcbActive;
    private javax.swing.JComboBox jcbPaymentType;
    private javax.swing.JComboBox jcbType;
    private javax.swing.JLabel jlCorrAccount;
    private javax.swing.JLabel jlPaymentType;
    private javax.swing.JLabel jlReceipt;
    private javax.swing.JPanel jpAnnouncement;
    private javax.swing.JPanel jpDate;
    private javax.swing.JTextField jtfAmount;
    private javax.swing.JTextField jtfAnnouncementNumber;
    private javax.swing.JTextField jtfCorrAccount;
    private javax.swing.JTextField jtfEmployee;
    private javax.swing.JTextField jtfNumber;
    private javax.swing.JTextField jtfReportingCode;
    private javax.swing.JTextField jtfSupplement;
    // End of variables declaration//GEN-END:variables
    private SalesView salesView;
    private RegisterView registerView;
    private Integer documentCode;
    private JDateChooser date;
    private Integer employeeCode;
    private Integer active;
    private Integer lock;
    private Integer hash;

    @Override
    public void setEmployee(Integer employeeCode, String name) {
        this.employeeCode = employeeCode;
        Session session = HUtil.getSession();
        Employee e = (Employee) HUtil.getElement("Employee", this.employeeCode, session);
        if (e != null) {
            jtfEmployee.setText(e.getName());
        }
        session.close();
    }

    @Action
    public void print() {

        Util.checkDocSaved(this);

        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        try {
            Session session = HUtil.getSession();
            if (jcbType.getSelectedIndex() == 0) {
                File file = new File(Util.getAppPath() + "\\templates\\InCashOrder.ods");
                final Sheet sheet = SpreadSheet.createFromFile(file).getSheet(0);
                sheet.getCellAt("A1").setValue(HUtil.getConstant("name"));
                sheet.getCellAt("M1").setValue(HUtil.getConstant("name"));
                String dateStr = Util.date2String(date.getDate());
                sheet.getCellAt("A6").setValue(dateStr);
                sheet.getCellAt("L6").setValue(dateStr);
                sheet.getCellAt("D9").setValue(Long.parseLong(jtfAmount.getText()));
                if (employeeCode != null) {
                    Employee e = (Employee) HUtil.getElement("Employee", employeeCode, session);
                    if (e != null) {
                        sheet.getCellAt("B11").setValue(e.getName());
                        sheet.getCellAt("M11").setValue("Принято от " + e.getName());
                    }
                    fwMoney m = new fwMoney(jtfAmount.getText());
                    String amountNdsStr = "Сумма с НДС " + m.num2str(true);
                    sheet.getCellAt("A16").setValue(amountNdsStr);
                    sheet.getCellAt("M18").setValue(amountNdsStr);
                }
                if(!HUtil.getConstant("st").equals("1")) {
                    int nds = 0;
                    long amount = 0;
                    try {
                        nds = Integer.parseInt(HUtil.getConstant("nds"));
                        amount = Long.parseLong(jtfAmount.getText());
                    } catch (Exception e) {
                        logger.error(e);
                    }
                    sheet.getCellAt("B14").setValue(nds);
                    sheet.getCellAt("N15").setValue(nds);
                    long amountNds = Math.round((double) amount * 100 / (100 + nds));
                    sheet.getCellAt("F14").setValue(amountNds);
                    sheet.getCellAt("N16").setValue(amountNds);
                } else {
                    sheet.getCellAt("B14").setValue("-  ");
                    sheet.getCellAt("N15").setValue("-  ");
                    sheet.getCellAt("F14").setValue("-  ");
                    sheet.getCellAt("N16").setValue("-  ");
                }
                String chiefAccountant = HUtil.getShortNameByCode(HUtil.getIntConstant("chiefAccountant"));
                sheet.getCellAt("F22").setValue(chiefAccountant);
                sheet.getCellAt("O22").setValue(chiefAccountant);
                String cashier = HUtil.getShortNameByCode(HUtil.getIntConstant("cashier"));
                sheet.getCellAt("F24").setValue(cashier);
                sheet.getCellAt("O24").setValue(cashier);

                String r = " " + Math.round(Math.random() * 100000);
                File outputFile = new File("temp\\Приходный кассовый ордер № " + jtfNumber.getText() + r + ".ods");
                sheet.getSpreadSheet().saveAs(outputFile);
                Util.openDoc("temp\\Приходный кассовый ордер № " + jtfNumber.getText() + r + ".ods");
                
            } else {
                
                if (jcbType.getSelectedIndex() == 1) {
                    File file = new File(Util.getAppPath() + "\\templates\\OutCashOrder.ods");
                    final Sheet sheet = SpreadSheet.createFromFile(file).getSheet(0);
                    sheet.getCellAt("B3").setValue(HUtil.getConstant("name"));
                    sheet.getCellAt("E5").setValue(jtfNumber.getText());
                    sheet.getCellAt("B7").setValue(Util.date2String(date.getDate()));
                    sheet.getCellAt("G10").setValue(Util.str2long(jtfAmount.getText()));
                    sheet.getCellAt("B10").setValue(jtfCorrAccount.getText());
                    if (employeeCode != null) {
                        Employee e = (Employee) HUtil.getElement("Employee", employeeCode, session);
                        if (e != null) {
                            sheet.getCellAt("C12").setValue(e.getName());
                            sheet.getCellAt("E27").setValue(
                                    "паспорт " + e.getPassportNumber() + ", выдан " + e.getPassportDatePlace());
                        }
                    }
                    if (jcbPaymentType.getSelectedIndex() == 0) {
                        sheet.getCellAt("D14").setValue("торговая выручка для сдачи в банк");
                    } else if (jcbPaymentType.getSelectedIndex() == 1) {
                        sheet.getCellAt("D14").setValue("зарплата");
                    }
                    fwMoney m = new fwMoney(jtfAmount.getText());
                    sheet.getCellAt("C16").setValue(m.num2str(true));
                    sheet.getCellAt("D18").setValue(jtfSupplement.getText());
                    sheet.getCellAt("H20").setValue(HUtil.getShortNameByCode(HUtil.getIntConstant("director")));
                    sheet.getCellAt("H22").setValue(HUtil.getShortNameByCode(HUtil.getIntConstant("chiefAccountant")));
                    sheet.getCellAt("H29").setValue(HUtil.getShortNameByCode(HUtil.getIntConstant("cashier")));

                    String r = " " + Math.round(Math.random() * 100000);
                    File outputFile = new File("temp\\Расходный кассовый ордер № " + jtfNumber.getText() + r + ".ods");
                    sheet.getSpreadSheet().saveAs(outputFile);
                    Util.openDoc("temp\\Расходный кассовый ордер № " + jtfNumber.getText() + r + ".ods");
                }
            }
            session.close();
        } catch (Exception e) {
            logger.error(e, e);
        }
    }

    private void typeSelected() {
        if (documentCode == null) {
            Session session = HUtil.getSession();

            jtfNumber.setText(
                    HUtil.getNextDocNumber("Register", "x.documentType = " + jcbType.getSelectedIndex(), session));
            session.close();
        }
        if (jcbType.getSelectedIndex() == 0) {
            jlReceipt.setVisible(false);
            jtfSupplement.setVisible(false);
            jlPaymentType.setVisible(false);
            jcbPaymentType.setVisible(false);
            jbCashAnnouncement.setVisible(false);
            jpAnnouncement.setVisible(false);
            jtfAnnouncementNumber.setVisible(false);
            jtfReportingCode.setVisible(false);
            jlCorrAccount.setVisible(false);
            jtfCorrAccount.setVisible(false);
        } else {
            jlReceipt.setVisible(true);
            jtfSupplement.setVisible(true);
            jlPaymentType.setVisible(true);
            jcbPaymentType.setVisible(true);
            jbCashAnnouncement.setVisible(true);
            jpAnnouncement.setVisible(true);
            jtfAnnouncementNumber.setVisible(true);
            jtfReportingCode.setVisible(true);
            jlCorrAccount.setVisible(true);
            jtfCorrAccount.setVisible(true);
        }
    }

    @Action
    public void printCashAnnouncement() {

        Util.checkDocSaved(this);

        try {
            Session session = HUtil.getSession();
            File file = new File(Util.getAppPath() + "\\templates\\CashAnnouncement.ods");
            final Sheet sheet = SpreadSheet.createFromFile(file).getSheet(0);

            fwMoney m = new fwMoney(jtfAmount.getText());

            sheet.getCellAt("F1").setValue(jtfAnnouncementNumber.getText());
            sheet.getCellAt("I15").setValue(jtfReportingCode.getText());
            sheet.getCellAt("B3").setValue(Util.date2String(date.getDate()));
            sheet.getCellAt("C5").setValue(HUtil.getConstant("name"));
            Employee e = (Employee) HUtil.getElement("Employee", employeeCode, session);
            if (e != null) {
                sheet.getCellAt("C7").setValue(e.getName());
            }
            sheet.getCellAt("C9").setValue(HUtil.getConstant("bank") + ", код " + HUtil.getConstant("bankCode"));
            sheet.getCellAt("C10").setValue(HUtil.getConstant("name"));
            sheet.getCellAt("C12").setValue(m.num2str());
            sheet.getCellAt("C15").setValue(jcbPaymentType.getSelectedItem());
            sheet.getCellAt("I4").setValue(HUtil.getConstant("account"));
            sheet.getCellAt("I6").setValue(jtfAmount.getText() + "=");

            sheet.getCellAt("F21").setValue(jtfAnnouncementNumber.getText());
            sheet.getCellAt("I35").setValue(jtfReportingCode.getText());
            sheet.getCellAt("B23").setValue(Util.date2String(date.getDate()));
            sheet.getCellAt("C24").setValue(HUtil.getConstant("name"));
            if (e != null) {
                sheet.getCellAt("C26").setValue(e.getName());
            }
            sheet.getCellAt("C29").setValue(HUtil.getConstant("bank") + ", код " + HUtil.getConstant("bankCode"));
            sheet.getCellAt("C30").setValue(HUtil.getConstant("name"));
            sheet.getCellAt("C32").setValue(m.num2str());
            sheet.getCellAt("C35").setValue(jcbPaymentType.getSelectedItem());
            sheet.getCellAt("I23").setValue(HUtil.getConstant("account"));
            sheet.getCellAt("I26").setValue(jtfAmount.getText() + "=");

            sheet.getCellAt("F40").setValue(jtfAnnouncementNumber.getText());
            sheet.getCellAt("H51").setValue(jtfReportingCode.getText());
            sheet.getCellAt("B41").setValue(Util.date2String(date.getDate()));
            sheet.getCellAt("C43").setValue(HUtil.getConstant("name"));
            if (e != null) {
                sheet.getCellAt("C45").setValue(e.getName());
            }
            sheet.getCellAt("C47").setValue(HUtil.getConstant("bank"));
            sheet.getCellAt("F47").setValue("Код " + HUtil.getConstant("bankCode"));
            sheet.getCellAt("C49").setValue(HUtil.getConstant("name"));
            sheet.getCellAt("C51").setValue(jcbPaymentType.getSelectedItem());
            sheet.getCellAt("G47").setValue("сч.№ " + HUtil.getConstant("account"));
            sheet.getCellAt("I43").setValue(jtfAmount.getText() + "=");

            String r = " " + Math.round(Math.random() * 100000);
            File outputFile = new File("temp\\Расходный кассовый ордер № " + jtfNumber.getText() + r + ".ods");
            sheet.getSpreadSheet().saveAs(outputFile);
            Util.openDoc("temp\\Расходный кассовый ордер № " + jtfNumber.getText() + r + ".ods");
            session.close();
        } catch (Exception e) {
            logger.error(e);
        }
    }

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
                        Register r = null;
                        if (documentCode != null) {
                            r = (Register) HUtil.getElement("Register", documentCode, session);
                        }
                        if (r == null) {
                            r = new Register();
                        }
                        r.setDatetime(date.getDate());
                        r.setDocumentType(jcbType.getSelectedIndex());
                        r.setNumber(jtfNumber.getText());
                        if (jcbType.getSelectedIndex() == 1) {
                            r.setSupplement(jtfSupplement.getText());
                        }

                        r.setEmployeeCode(employeeCode);
                        if (!jtfAmount.getText().isEmpty()) {
                            r.setAmount(new Integer(jtfAmount.getText()));
                        } else {
                            r.setAmount(0);
                        }
                        r.setPaymentType((String) jcbPaymentType.getSelectedItem());
                        if (jcbActive.isSelected()) {
                            r.setActive(1);
                        } else {
                            r.setActive(0);
                        }

                        r.setCorrAccount(jtfCorrAccount.getText());

                        r.setAnnouncementNumber(jtfAnnouncementNumber.getText());
                        r.setReportingCode(jtfReportingCode.getText());

                        r.setPage(HUtil.getPageNumber(date.getDate()));

                        if (documentCode == null) {
                            session.save(r);
                        } else {
                            session.update(r);
                        }
                        documentCode = r.getCode();
                        session.getTransaction().commit();
                        session.close();
                        Util.updateJournals(salesView, RegisterView.class);
                        hash = Util.hash(this);
                    } catch (Exception e) {
                        logger.error(e);
                    }
                }
            }
        }
    }

    public Integer getHash() {
        return hash;
    }

    public Integer getHashCode() {
        return Util.hash(this);
    }

    @Action
    public void printCashBook() {
        save();
        Util.printCashBook(date.getDate(), HUtil.getPageNumber(date.getDate()));
    }
}