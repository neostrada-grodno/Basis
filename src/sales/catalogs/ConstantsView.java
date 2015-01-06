package sales.catalogs;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import org.apache.log4j.Logger;
import org.hibernate.classic.Session;
import org.jdesktop.application.Action;
import sales.SalesApp;
import sales.SalesView;
import sales.entity.Constants;
import sales.entity.Employee;
import sales.interfaces.IClose;
import sales.interfaces.IEmployee;
import sales.util.HUtil;
import sales.util.Util;

public class ConstantsView extends javax.swing.JInternalFrame implements IClose, IEmployee {

    static Logger logger = Logger.getLogger(SalesApp.class.getName());

    public ConstantsView(SalesView salesView) {

        initComponents();

        Util.initJIF(this, "Константы", null, salesView);

        this.salesView = salesView;

        jtfNds.addKeyListener(new KeyAdapter() {

            @Override
            public void keyTyped(KeyEvent e) {
                jtfNds.setEditable(
                        Character.isDigit(e.getKeyChar()) || e.getKeyChar() == KeyEvent.VK_BACK_SPACE);
            }
        });

        jtfDefaultMargin.addKeyListener(new DigitKeyAdapter(jtfDefaultMargin));
        jtfCreditInterest.addKeyListener(new DigitKeyAdapter(jtfCreditInterest));        

        jtfRound.addKeyListener(new KeyAdapter() {

            @Override
            public void keyTyped(KeyEvent e) {
                jtfRound.setEditable(
                        jtfRound.getCaretPosition() == 0 && (e.getKeyChar() == '1' || e.getKeyChar() == '5')
                        || jtfRound.getCaretPosition() > 0 && (e.getKeyChar() == '0' || e.getKeyChar() == KeyEvent.VK_BACK_SPACE));
            }
        });

        jtfName.setText(HUtil.getConstant("name"));
        jtfUnt.setText(HUtil.getConstant("unt"));
        jtfAddress.setText(HUtil.getConstant("address"));
        jtfLegalAddress.setText(HUtil.getConstant("legalAddress"));
        jtfBank.setText(HUtil.getConstant("bank"));
        jtfBankCode.setText(HUtil.getConstant("bankCode"));
        jtfAccount.setText(HUtil.getConstant("account"));
        directorCode = HUtil.getIntConstant("director");
        jtfDirector.setText(HUtil.getNameByCode(directorCode, "Employee"));
        chiefAccountantCode = HUtil.getIntConstant("chiefAccountant");
        jtfChiefAccountant.setText(HUtil.getNameByCode(chiefAccountantCode, "Employee"));
        cashierCode = HUtil.getIntConstant("cashier");
        jtfCashier.setText(HUtil.getNameByCode(cashierCode, "Employee"));
        jtfNds.setText(HUtil.getConstant("nds"));
        if (HUtil.getConstant("st").trim().equals("1")) {
            jcbST.setSelected(true);
        }
        jtfDefaultMargin.setText(HUtil.getConstant("defaultMargin"));
        jtfRound.setText(HUtil.getConstant("round"));
        jtfCreditInterest.setText(HUtil.getConstant("creditInterest"));
        jtfEmail.setText(HUtil.getConstant("email"));
        jtfSmtp.setText(HUtil.getConstant("smtp"));
        jtfSmtpPort.setText(HUtil.getConstant("smtpPort"));
        jtfSmtpLogin.setText(HUtil.getConstant("smtpLogin"));
        jtfSmtpPassword.setText(HUtil.getConstant("smtpPassword"));

        String lck = HUtil.getConstant("lock");
        if (lck == null || !lck.equals("locked")) {
            lock = 0;
        } else {
            lock = 1;
            JOptionPane.showMessageDialog(
                    this,
                    "Константы редактируются другим пользователем и будут открыты только для просмотра!",
                    "Блокировка", JOptionPane.PLAIN_MESSAGE);

            jtfName.setEditable(false);
            jtfUnt.setEditable(false);
            jtfAddress.setEditable(false);
            jtfLegalAddress.setEditable(false);
            jtfBank.setEditable(false);
            jtfBankCode.setEditable(false);
            jtfAccount.setEditable(false);
            jtfDirector.setEditable(false);
            jtfChiefAccountant.setEditable(false);
            jtfCashier.setEditable(false);
            jtfNds.setEditable(false);
            jcbST.setEnabled(false);
            jtfRound.setEditable(false);
            jtfCreditInterest.setEditable(false);
            jtfEmail.setEditable(false);
            jtfSmtp.setEditable(false);
            jtfSmtpPort.setEditable(false);
            jtfSmtpLogin.setEditable(false);
            jtfSmtpPassword.setEditable(false);

        }
        ArrayList consts = new ArrayList();
        consts.add(new Constants("lock", "", "locked"));
        HUtil.setConstants(consts);

        hash = Util.hash(this);
    }

    private class DigitKeyAdapter extends KeyAdapter {

        private JTextField jtf;

        public DigitKeyAdapter(JTextField jtf) {
            super();
            this.jtf = jtf;
        }

        @Override
        public void keyTyped(KeyEvent e) {
            jtf.setEditable(
                    Character.isDigit(e.getKeyChar()) || e.getKeyChar() == KeyEvent.VK_BACK_SPACE);
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

        jbClose = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jtfName = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jtfUnt = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jlAddress = new javax.swing.JLabel();
        jtfAddress = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jtfLegalAddress = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jtfBank = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jtfBankCode = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jtfAccount = new javax.swing.JTextField();
        jPanel4 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jtfDirector = new javax.swing.JTextField();
        jtfChiefAccountant = new javax.swing.JTextField();
        jtfCashier = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jbDirector = new javax.swing.JButton();
        jbChiefAccountant = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        jtfNds = new javax.swing.JTextField();
        jcbST = new javax.swing.JCheckBox();
        jPanel6 = new javax.swing.JPanel();
        jtfSmtpPort = new javax.swing.JTextField();
        jtfSmtp = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jtfSmtpPassword = new javax.swing.JTextField();
        jtfEmail = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        jtfSmtpLogin = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jtfDefaultMargin = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        jtfRound = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        jtfCreditInterest = new javax.swing.JTextField();
        jLabel20 = new javax.swing.JLabel();

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance().getContext().getResourceMap(ConstantsView.class);
        setBackground(resourceMap.getColor("Form.background")); // NOI18N
        setClosable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle(resourceMap.getString("Form.title")); // NOI18N
        setName("Form"); // NOI18N

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance().getContext().getActionMap(ConstantsView.class, this);
        jbClose.setAction(actionMap.get("closeCat")); // NOI18N
        jbClose.setText(resourceMap.getString("jbClose.text")); // NOI18N
        jbClose.setName("jbClose"); // NOI18N

        jPanel1.setBackground(resourceMap.getColor("jPanel1.background")); // NOI18N
        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel1.setName("jPanel1"); // NOI18N

        jLabel1.setText(resourceMap.getString("jLabel1.text")); // NOI18N
        jLabel1.setName("jLabel1"); // NOI18N

        jtfName.setText(resourceMap.getString("jtfName.text")); // NOI18N
        jtfName.setName("jtfName"); // NOI18N

        jLabel4.setText(resourceMap.getString("jLabel4.text")); // NOI18N
        jLabel4.setName("jLabel4"); // NOI18N

        jtfUnt.setText(resourceMap.getString("jtfUnt.text")); // NOI18N
        jtfUnt.setName("jtfUnt"); // NOI18N

        jLabel17.setForeground(resourceMap.getColor("jLabel17.foreground")); // NOI18N
        jLabel17.setText(resourceMap.getString("jLabel17.text")); // NOI18N
        jLabel17.setName("jLabel17"); // NOI18N

        jLabel3.setForeground(resourceMap.getColor("jLabel3.foreground")); // NOI18N
        jLabel3.setText(resourceMap.getString("jLabel3.text")); // NOI18N
        jLabel3.setName("jLabel3"); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel4)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jtfUnt, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel17)
                        .addGap(398, 398, 398))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jtfName, javax.swing.GroupLayout.DEFAULT_SIZE, 556, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel3)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jtfName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jtfUnt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel17))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.setBackground(resourceMap.getColor("jPanel2.background")); // NOI18N
        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel2.setName("jPanel2"); // NOI18N

        jlAddress.setText(resourceMap.getString("jlAddress.text")); // NOI18N
        jlAddress.setName("jlAddress"); // NOI18N

        jtfAddress.setText(resourceMap.getString("jtfAddress.text")); // NOI18N
        jtfAddress.setName("jtfAddress"); // NOI18N

        jLabel2.setText(resourceMap.getString("jLabel2.text")); // NOI18N
        jLabel2.setName("jLabel2"); // NOI18N

        jtfLegalAddress.setText(resourceMap.getString("jtfLegalAddress.text")); // NOI18N
        jtfLegalAddress.setName("jtfLegalAddress"); // NOI18N

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jlAddress, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jtfAddress, javax.swing.GroupLayout.DEFAULT_SIZE, 566, Short.MAX_VALUE)
                    .addComponent(jtfLegalAddress, javax.swing.GroupLayout.DEFAULT_SIZE, 566, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jtfLegalAddress, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jtfAddress, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jlAddress))
                .addGap(37, 37, 37))
        );

        jPanel3.setBackground(resourceMap.getColor("jPanel3.background")); // NOI18N
        jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel3.setName("jPanel3"); // NOI18N

        jLabel5.setText(resourceMap.getString("jLabel5.text")); // NOI18N
        jLabel5.setName("jLabel5"); // NOI18N

        jtfBank.setText(resourceMap.getString("jtfBank.text")); // NOI18N
        jtfBank.setName("jtfBank"); // NOI18N

        jLabel6.setText(resourceMap.getString("jLabel6.text")); // NOI18N
        jLabel6.setName("jLabel6"); // NOI18N

        jtfBankCode.setText(resourceMap.getString("jtfBankCode.text")); // NOI18N
        jtfBankCode.setName("jtfBankCode"); // NOI18N

        jLabel7.setText(resourceMap.getString("jLabel7.text")); // NOI18N
        jLabel7.setName("jLabel7"); // NOI18N

        jtfAccount.setText(resourceMap.getString("jtfAccount.text")); // NOI18N
        jtfAccount.setName("jtfAccount"); // NOI18N

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel6)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addGap(16, 16, 16)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jtfBankCode, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(58, 58, 58)
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jtfAccount, javax.swing.GroupLayout.PREFERRED_SIZE, 293, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(115, 115, 115))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jtfBank, javax.swing.GroupLayout.DEFAULT_SIZE, 653, Short.MAX_VALUE)
                        .addContainerGap())))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jtfBank, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(jtfBankCode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jtfAccount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel4.setBackground(resourceMap.getColor("jPanel4.background")); // NOI18N
        jPanel4.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel4.setName("jPanel4"); // NOI18N

        jLabel8.setText(resourceMap.getString("jLabel8.text")); // NOI18N
        jLabel8.setName("jLabel8"); // NOI18N

        jLabel9.setText(resourceMap.getString("jLabel9.text")); // NOI18N
        jLabel9.setName("jLabel9"); // NOI18N

        jLabel10.setText(resourceMap.getString("jLabel10.text")); // NOI18N
        jLabel10.setName("jLabel10"); // NOI18N

        jtfDirector.setText(resourceMap.getString("jtfDirector.text")); // NOI18N
        jtfDirector.setName("jtfDirector"); // NOI18N

        jtfChiefAccountant.setText(resourceMap.getString("jtfChiefAccountant.text")); // NOI18N
        jtfChiefAccountant.setName("jtfChiefAccountant"); // NOI18N

        jtfCashier.setText(resourceMap.getString("jtfCashier.text")); // NOI18N
        jtfCashier.setName("jtfCashier"); // NOI18N

        jButton1.setAction(actionMap.get("chooseCashier")); // NOI18N
        jButton1.setText(resourceMap.getString("jButton1.text")); // NOI18N
        jButton1.setName("jButton1"); // NOI18N

        jbDirector.setAction(actionMap.get("chooseDirector")); // NOI18N
        jbDirector.setText(resourceMap.getString("jbDirector.text")); // NOI18N
        jbDirector.setName("jbDirector"); // NOI18N

        jbChiefAccountant.setAction(actionMap.get("chooseChiefAccountant")); // NOI18N
        jbChiefAccountant.setText(resourceMap.getString("jbChiefAccountant.text")); // NOI18N
        jbChiefAccountant.setName("jbChiefAccountant"); // NOI18N

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel10)
                    .addComponent(jLabel9)
                    .addComponent(jLabel8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jtfChiefAccountant, javax.swing.GroupLayout.DEFAULT_SIZE, 322, Short.MAX_VALUE)
                    .addComponent(jtfDirector, javax.swing.GroupLayout.DEFAULT_SIZE, 322, Short.MAX_VALUE)
                    .addComponent(jtfCashier, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 322, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jbDirector, 0, 0, Short.MAX_VALUE)
                    .addComponent(jbChiefAccountant, 0, 0, Short.MAX_VALUE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 27, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(jtfDirector, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jbDirector))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(jtfChiefAccountant, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jbChiefAccountant))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(jtfCashier, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1))
                .addContainerGap(17, Short.MAX_VALUE))
        );

        jPanel5.setBackground(resourceMap.getColor("jPanel5.background")); // NOI18N
        jPanel5.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel5.setName("jPanel5"); // NOI18N

        jLabel16.setText(resourceMap.getString("jLabel16.text")); // NOI18N
        jLabel16.setName("jLabel16"); // NOI18N

        jtfNds.setText(resourceMap.getString("jtfNds.text")); // NOI18N
        jtfNds.setName("jtfNds"); // NOI18N

        jcbST.setBackground(resourceMap.getColor("jcbST.background")); // NOI18N
        jcbST.setText(resourceMap.getString("jcbST.text")); // NOI18N
        jcbST.setName("jcbST"); // NOI18N

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jcbST)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel16)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jtfNds, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jtfNds, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel16))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jcbST))
        );

        jPanel6.setBackground(resourceMap.getColor("jPanel6.background")); // NOI18N
        jPanel6.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel6.setName("jPanel6"); // NOI18N

        jtfSmtpPort.setText(resourceMap.getString("jtfSmtpPort.text")); // NOI18N
        jtfSmtpPort.setName("jtfSmtpPort"); // NOI18N

        jtfSmtp.setText(resourceMap.getString("jtfSmtp.text")); // NOI18N
        jtfSmtp.setName("jtfSmtp"); // NOI18N

        jLabel15.setText(resourceMap.getString("jLabel15.text")); // NOI18N
        jLabel15.setName("jLabel15"); // NOI18N

        jLabel12.setText(resourceMap.getString("jLabel12.text")); // NOI18N
        jLabel12.setName("jLabel12"); // NOI18N

        jLabel11.setText(resourceMap.getString("jLabel11.text")); // NOI18N
        jLabel11.setName("jLabel11"); // NOI18N

        jtfSmtpPassword.setText(resourceMap.getString("jtfSmtpPassword.text")); // NOI18N
        jtfSmtpPassword.setName("jtfSmtpPassword"); // NOI18N

        jtfEmail.setText(resourceMap.getString("jtfEmail.text")); // NOI18N
        jtfEmail.setName("jtfEmail"); // NOI18N

        jLabel13.setText(resourceMap.getString("jLabel13.text")); // NOI18N
        jLabel13.setName("jLabel13"); // NOI18N

        jtfSmtpLogin.setText(resourceMap.getString("jtfSmtpLogin.text")); // NOI18N
        jtfSmtpLogin.setName("jtfSmtpLogin"); // NOI18N

        jLabel14.setText(resourceMap.getString("jLabel14.text")); // NOI18N
        jLabel14.setName("jLabel14"); // NOI18N

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel15)
                    .addComponent(jLabel14)
                    .addComponent(jLabel13)
                    .addComponent(jLabel12)
                    .addComponent(jLabel11))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jtfSmtp, javax.swing.GroupLayout.DEFAULT_SIZE, 596, Short.MAX_VALUE)
                    .addComponent(jtfEmail, javax.swing.GroupLayout.DEFAULT_SIZE, 596, Short.MAX_VALUE)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jtfSmtpPort, javax.swing.GroupLayout.DEFAULT_SIZE, 148, Short.MAX_VALUE)
                        .addGap(448, 448, 448))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jtfSmtpPassword, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 410, Short.MAX_VALUE)
                            .addComponent(jtfSmtpLogin, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 410, Short.MAX_VALUE))
                        .addGap(186, 186, 186)))
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(jtfEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(jtfSmtp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(jtfSmtpPort, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(jtfSmtpLogin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(jtfSmtpPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel7.setBackground(resourceMap.getColor("jPanel7.background")); // NOI18N
        jPanel7.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel7.setName("jPanel7"); // NOI18N

        jtfDefaultMargin.setText(resourceMap.getString("jtfDefaultMargin.text")); // NOI18N
        jtfDefaultMargin.setName("jtfDefaultMargin"); // NOI18N

        jLabel18.setText(resourceMap.getString("jLabel18.text")); // NOI18N
        jLabel18.setName("jLabel18"); // NOI18N

        jtfRound.setText(resourceMap.getString("jtfRound.text")); // NOI18N
        jtfRound.setName("jtfRound"); // NOI18N

        jLabel19.setText(resourceMap.getString("jLabel19.text")); // NOI18N
        jLabel19.setName("jLabel19"); // NOI18N

        jtfCreditInterest.setText(resourceMap.getString("jtfCreditInterest.text")); // NOI18N
        jtfCreditInterest.setName("jtfCreditInterest"); // NOI18N

        jLabel20.setText(resourceMap.getString("jLabel20.text")); // NOI18N
        jLabel20.setName("jLabel20"); // NOI18N

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel19)
                    .addComponent(jLabel18)
                    .addComponent(jLabel20))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jtfCreditInterest, javax.swing.GroupLayout.DEFAULT_SIZE, 60, Short.MAX_VALUE)
                    .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jtfRound, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jtfDefaultMargin, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 60, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel19, javax.swing.GroupLayout.DEFAULT_SIZE, 20, Short.MAX_VALUE)
                    .addComponent(jtfDefaultMargin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jtfRound, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel18))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jtfCreditInterest, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel20))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jbClose, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jbClose)
                .addGap(23, 23, 23))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JButton jbChiefAccountant;
    private javax.swing.JButton jbClose;
    private javax.swing.JButton jbDirector;
    private javax.swing.JCheckBox jcbST;
    private javax.swing.JLabel jlAddress;
    private javax.swing.JTextField jtfAccount;
    private javax.swing.JTextField jtfAddress;
    private javax.swing.JTextField jtfBank;
    private javax.swing.JTextField jtfBankCode;
    private javax.swing.JTextField jtfCashier;
    private javax.swing.JTextField jtfChiefAccountant;
    private javax.swing.JTextField jtfCreditInterest;
    private javax.swing.JTextField jtfDefaultMargin;
    private javax.swing.JTextField jtfDirector;
    private javax.swing.JTextField jtfEmail;
    private javax.swing.JTextField jtfLegalAddress;
    private javax.swing.JTextField jtfName;
    private javax.swing.JTextField jtfNds;
    private javax.swing.JTextField jtfRound;
    private javax.swing.JTextField jtfSmtp;
    private javax.swing.JTextField jtfSmtpLogin;
    private javax.swing.JTextField jtfSmtpPassword;
    private javax.swing.JTextField jtfSmtpPort;
    private javax.swing.JTextField jtfUnt;
    // End of variables declaration//GEN-END:variables
    private SalesView salesView;
    private Integer directorCode;
    private Integer chiefAccountantCode;
    private Integer cashierCode;
    private int employeeToChoose;
    private Integer hash;
    private Integer lock;

    private void save() {
        ArrayList consts = new ArrayList();
        if (lock == 0 && (hash == null || !hash.equals(Util.hash(this)))) {
            consts.add(new Constants("name", "", jtfName.getText()));
            consts.add(new Constants("unt", "", jtfUnt.getText()));
            consts.add(new Constants("address", "", jtfAddress.getText()));
            consts.add(new Constants("legalAddress", "", jtfLegalAddress.getText()));
            consts.add(new Constants("bank", "", jtfBank.getText()));
            consts.add(new Constants("bankCode", "", jtfBankCode.getText()));
            consts.add(new Constants("account", "", jtfAccount.getText()));
            consts.add(new Constants("director", "", directorCode.toString()));
            consts.add(new Constants("chiefAccountant", "", chiefAccountantCode.toString()));
            consts.add(new Constants("cashier", "", cashierCode.toString()));
            consts.add(new Constants("nds", "", jtfNds.getText()));
            if (jcbST.isSelected()) {
                consts.add(new Constants("st", "", "1"));
            } else {
                consts.add(new Constants("st", "", "0"));
            }
            consts.add(new Constants("defaultMargin", "", jtfDefaultMargin.getText()));
            consts.add(new Constants("round", "", jtfRound.getText()));
            consts.add(new Constants("creditInterest", "", jtfCreditInterest.getText()));
            consts.add(new Constants("email", "", jtfEmail.getText()));
            consts.add(new Constants("smtp", "", jtfSmtp.getText()));
            consts.add(new Constants("smtpPort", "", jtfSmtpPort.getText()));
            consts.add(new Constants("smtpLogin", "", jtfSmtpLogin.getText()));
            consts.add(new Constants("smtpPassword", "", jtfSmtpPassword.getText()));
        }
        HUtil.setConstants(consts);
    }

    public void close() {

        if (lock == 0) {
            ArrayList consts = new ArrayList();
            consts.add(new Constants("lock", "", ""));
            HUtil.setConstants(consts);
        }
    }

    @Action
    public void closeCat() {
        if (lock == 0 && (hash == null || !hash.equals(Util.hash(this)))) {
            int a = JOptionPane.showOptionDialog(
                    this, "Сохранить константы?", "Сохранение", JOptionPane.YES_NO_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE, null, new Object[]{"Да", "Нет", "Отмена"}, "Да");
            if (a == 0) {
                if (!jtfName.getText().trim().isEmpty() && !jtfUnt.getText().trim().isEmpty()) {
                    save();
                } else {
                    JOptionPane.showMessageDialog(
                            this,
                            "Наименование организации и УНН не могут быть пустыми!",
                            "Ошибка сохранения констант",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } else if (a == 2) {
                return;
            }
        }
        close();
        Util.closeJIF(this, salesView, salesView);
        Util.closeJIFTab(this, salesView);
    }

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
    public void chooseDirector() {
        employeeToChoose = 0;
        chooseEmployee();
    }

    @Action
    public void chooseChiefAccountant() {
        employeeToChoose = 1;
        chooseEmployee();
    }

    @Action
    public void chooseCashier() {
        employeeToChoose = 2;
        chooseEmployee();
    }

    @Override
    public void setEmployee(Integer employeeCode, String name) {
        if (employeeToChoose == 0) {
            directorCode = employeeCode;
        } else if (employeeToChoose == 1) {
            chiefAccountantCode = employeeCode;
        } else if (employeeToChoose == 2) {
            cashierCode = employeeCode;
        }
        Session session = HUtil.getSession();
        Employee e = (Employee) HUtil.getElement("Employee", employeeCode, session);
        if (e != null) {
            if (employeeToChoose == 0) {
                jtfDirector.setText(e.getName());
            } else if (employeeToChoose == 1) {
                jtfChiefAccountant.setText(e.getName());
            } else if (employeeToChoose == 2) {
                jtfCashier.setText(e.getName());
            }
        }
        session.close();

    }
}