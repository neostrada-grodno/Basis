/*
 * ContractorsElView.java
 *
 * Created on 01.08.2011, 11:47:33
 */
package sales.catalogs;

import com.toedter.calendar.JDateChooser;
import java.util.Calendar;
import javax.swing.GroupLayout;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import org.apache.log4j.Logger;
import org.hibernate.classic.Session;
import org.jdesktop.application.Action;
import sales.SalesApp;
import sales.interfaces.IBankView;
import sales.SalesView;
import sales.entity.Banks;
import sales.entity.Contractors;
import sales.interfaces.IClose;
import sales.util.HUtil;
import sales.util.Util;

public class ContractorsElView extends JInternalFrame implements IBankView, IClose {

    static Logger logger = Logger.getLogger(SalesApp.class.getName());

    /** Creates new form ContractorsElView */
    public ContractorsElView(SalesView salesView, ContractorsView parent, Integer elementCode) {
        initComponents();
        Util.initJIF(this, "Покупатель", parent, salesView);

        this.salesView = salesView;
        this.parent = parent;
        this.elementCode = elementCode;

        Session session = HUtil.getSession();

        Contractors x = null;
        if (elementCode != null) {
            x = (Contractors) HUtil.getElement("Contractors", elementCode, session);
        }

        if (x != null) {
            jlCode.setText("Код: " + x.getCode());
            jtfName.setText(x.getName());
            jtfName.setText(x.getName());
            jtfUnt.setText(x.getUnt());
            jtaAddress.setText(x.getAddress());
            jtaUnloadingAddress.setText(x.getUnloadingAddress());
            jtfEmail.setText(x.getEmail());
            jtfContractNumber.setText(x.getContractNumber());
            contractDate = new JDateChooser(x.getContractDate());
            jtfAccount.setText(x.getAccount());
            bankCode = x.getBank();
            Banks b = (Banks) HUtil.getElement("Banks", bankCode, session);
            if (b != null) {
                jtfBank.setText(b.getName());
            }
            jtfHead.setText(x.getHead());
            jtfHeadPositionName.setText(x.getHeadPositionName());
            jtfPrimaryDocument.setText(x.getPrimaryDocument());
            jtaNote.setText(x.getNote());
            lock = x.getLocked();
            if (lock == 1) {
                JOptionPane.showMessageDialog(
                        this,
                        "Элемент редактируется другим пользователем и будет открыт только для просмотра!",
                        "Блокировка", JOptionPane.PLAIN_MESSAGE);
                jtfName.setEditable(false);
                jtfUnt.setEditable(false);
                jtaAddress.setEditable(false);
                jtaUnloadingAddress.setEditable(false);
                jtfEmail.setEditable(false);
                jtfAccount.setEditable(false);
                jtfContractNumber.setEditable(false);
                contractDate.setEnabled(false);
                jbBank.setEnabled(false);
                jtaNote.setEditable(false);
            } else {
                session.beginTransaction();
                x.setLocked(1);
                session.merge(x);
                session.getTransaction().commit();
            }

        } else {
            jlCode.setText("Код: " + HUtil.getNextMax("Contractors", "code", session));
            jtfContractNumber.setText(HUtil.getNextMax("Contractors", "contractNumber", session));
            contractDate = new JDateChooser(Calendar.getInstance().getTime());
            jtfHeadPositionName.setText("Директор");
            jtfPrimaryDocument.setText("Устава");
            lock = 0;
        }

        GroupLayout gl = (GroupLayout) jpContractDate.getLayout();
        gl.setHorizontalGroup(gl.createParallelGroup().addGroup(gl.createSequentialGroup().addComponent(contractDate)));
        gl.setVerticalGroup(gl.createParallelGroup().addGroup(gl.createSequentialGroup().addComponent(contractDate)));

        session.close();

        if(x != null) {
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

        jlCode = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jtfName = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jtaAddress = new javax.swing.JTextArea();
        jLabel3 = new javax.swing.JLabel();
        jtfEmail = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jtfUnt = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jtfBank = new javax.swing.JTextField();
        jbBank = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jtfContractNumber = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jpContractDate = new javax.swing.JPanel();
        jbClose = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jtaNote = new javax.swing.JTextArea();
        jLabel9 = new javax.swing.JLabel();
        jtfAccount = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jtaUnloadingAddress = new javax.swing.JTextArea();
        jPanel2 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jtfHead = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        jtfHeadPositionName = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        jtfPrimaryDocument = new javax.swing.JTextField();

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance().getContext().getResourceMap(ContractorsElView.class);
        setBackground(resourceMap.getColor("Form.background")); // NOI18N
        setClosable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle(resourceMap.getString("Form.title")); // NOI18N
        setName("Form"); // NOI18N

        jlCode.setText(resourceMap.getString("jlCode.text")); // NOI18N
        jlCode.setName("jlCode"); // NOI18N

        jLabel1.setText(resourceMap.getString("jLabel1.text")); // NOI18N
        jLabel1.setName("jLabel1"); // NOI18N

        jtfName.setText(resourceMap.getString("jtfName.text")); // NOI18N
        jtfName.setName("jtfName"); // NOI18N

        jLabel2.setText(resourceMap.getString("jLabel2.text")); // NOI18N
        jLabel2.setName("jLabel2"); // NOI18N

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        jtaAddress.setColumns(20);
        jtaAddress.setFont(resourceMap.getFont("jtaAddress.font")); // NOI18N
        jtaAddress.setRows(5);
        jtaAddress.setName("jtaAddress"); // NOI18N
        jScrollPane1.setViewportView(jtaAddress);

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel3.setText(resourceMap.getString("jLabel3.text")); // NOI18N
        jLabel3.setName("jLabel3"); // NOI18N

        jtfEmail.setText(resourceMap.getString("jtfEmail.text")); // NOI18N
        jtfEmail.setName("jtfEmail"); // NOI18N

        jLabel4.setText(resourceMap.getString("jLabel4.text")); // NOI18N
        jLabel4.setName("jLabel4"); // NOI18N

        jtfUnt.setText(resourceMap.getString("jtfUnt.text")); // NOI18N
        jtfUnt.setName("jtfUnt"); // NOI18N

        jLabel5.setText(resourceMap.getString("jLabel5.text")); // NOI18N
        jLabel5.setName("jLabel5"); // NOI18N

        jtfBank.setBackground(resourceMap.getColor("jtfBank.background")); // NOI18N
        jtfBank.setEditable(false);
        jtfBank.setText(resourceMap.getString("jtfBank.text")); // NOI18N
        jtfBank.setName("jtfBank"); // NOI18N

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance().getContext().getActionMap(ContractorsElView.class, this);
        jbBank.setAction(actionMap.get("chooseBank")); // NOI18N
        jbBank.setText(resourceMap.getString("jbBank.text")); // NOI18N
        jbBank.setName("jbBank"); // NOI18N

        jPanel1.setBackground(resourceMap.getColor("jPanel1.background")); // NOI18N
        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel1.setName("jPanel1"); // NOI18N

        jLabel6.setText(resourceMap.getString("jLabel6.text")); // NOI18N
        jLabel6.setName("jLabel6"); // NOI18N

        jtfContractNumber.setText(resourceMap.getString("jtfContractNumber.text")); // NOI18N
        jtfContractNumber.setName("jtfContractNumber"); // NOI18N

        jLabel7.setText(resourceMap.getString("jLabel7.text")); // NOI18N
        jLabel7.setName("jLabel7"); // NOI18N

        jpContractDate.setBackground(resourceMap.getColor("jpContractDate.background")); // NOI18N
        jpContractDate.setName("jpContractDate"); // NOI18N

        javax.swing.GroupLayout jpContractDateLayout = new javax.swing.GroupLayout(jpContractDate);
        jpContractDate.setLayout(jpContractDateLayout);
        jpContractDateLayout.setHorizontalGroup(
            jpContractDateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jpContractDateLayout.setVerticalGroup(
            jpContractDateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 19, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jtfContractNumber, javax.swing.GroupLayout.DEFAULT_SIZE, 137, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpContractDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jpContractDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel6)
                        .addComponent(jtfContractNumber, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel7)))
                .addContainerGap(11, Short.MAX_VALUE))
        );

        jbClose.setAction(actionMap.get("closeEl")); // NOI18N
        jbClose.setText(resourceMap.getString("jbClose.text")); // NOI18N
        jbClose.setName("jbClose"); // NOI18N

        jLabel8.setText(resourceMap.getString("jLabel8.text")); // NOI18N
        jLabel8.setName("jLabel8"); // NOI18N

        jScrollPane2.setName("jScrollPane2"); // NOI18N

        jtaNote.setColumns(20);
        jtaNote.setFont(resourceMap.getFont("jtaNote.font")); // NOI18N
        jtaNote.setRows(5);
        jtaNote.setName("jtaNote"); // NOI18N
        jScrollPane2.setViewportView(jtaNote);

        jLabel9.setText(resourceMap.getString("jLabel9.text")); // NOI18N
        jLabel9.setName("jLabel9"); // NOI18N

        jtfAccount.setText(resourceMap.getString("jtfAccount.text")); // NOI18N
        jtfAccount.setName("jtfAccount"); // NOI18N

        jLabel10.setText(resourceMap.getString("jLabel10.text")); // NOI18N
        jLabel10.setName("jLabel10"); // NOI18N

        jScrollPane3.setName("jScrollPane3"); // NOI18N

        jtaUnloadingAddress.setColumns(20);
        jtaUnloadingAddress.setFont(resourceMap.getFont("jtaUnloadingAddress.font")); // NOI18N
        jtaUnloadingAddress.setRows(5);
        jtaUnloadingAddress.setName("jtaUnloadingAddress"); // NOI18N
        jScrollPane3.setViewportView(jtaUnloadingAddress);

        jPanel2.setBackground(resourceMap.getColor("jPanel2.background")); // NOI18N
        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel2.setName("jPanel2"); // NOI18N

        jLabel11.setText(resourceMap.getString("jLabel11.text")); // NOI18N
        jLabel11.setName("jLabel11"); // NOI18N

        jtfHead.setText(resourceMap.getString("jtfHead.text")); // NOI18N
        jtfHead.setName("jtfHead"); // NOI18N

        jLabel12.setText(resourceMap.getString("jLabel12.text")); // NOI18N
        jLabel12.setName("jLabel12"); // NOI18N

        jtfHeadPositionName.setText(resourceMap.getString("jtfHeadPositionName.text")); // NOI18N
        jtfHeadPositionName.setName("jtfHeadPositionName"); // NOI18N

        jLabel13.setText(resourceMap.getString("jLabel13.text")); // NOI18N
        jLabel13.setName("jLabel13"); // NOI18N

        jtfPrimaryDocument.setText(resourceMap.getString("jtfPrimaryDocument.text")); // NOI18N
        jtfPrimaryDocument.setName("jtfPrimaryDocument"); // NOI18N

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel11)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jtfHead, javax.swing.GroupLayout.DEFAULT_SIZE, 416, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel12)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jtfHeadPositionName, javax.swing.GroupLayout.DEFAULT_SIZE, 319, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel13)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jtfPrimaryDocument, javax.swing.GroupLayout.DEFAULT_SIZE, 401, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(jtfHead, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(jtfHeadPositionName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(jtfPrimaryDocument, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel10, javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addComponent(jLabel8)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 558, Short.MAX_VALUE))
                            .addComponent(jbClose)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                        .addComponent(jLabel9)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jtfAccount, javax.swing.GroupLayout.DEFAULT_SIZE, 536, Short.MAX_VALUE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(jLabel4)
                                            .addGroup(layout.createSequentialGroup()
                                                .addComponent(jlCode, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jLabel1)))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(layout.createSequentialGroup()
                                                .addComponent(jtfUnt, javax.swing.GroupLayout.DEFAULT_SIZE, 177, Short.MAX_VALUE)
                                                .addGap(250, 250, 250))
                                            .addComponent(jtfName, javax.swing.GroupLayout.DEFAULT_SIZE, 427, Short.MAX_VALUE)))
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                        .addComponent(jLabel2)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 555, Short.MAX_VALUE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                        .addGap(6, 6, 6)
                                        .addComponent(jLabel5)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jtfBank, javax.swing.GroupLayout.DEFAULT_SIZE, 556, Short.MAX_VALUE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 555, Short.MAX_VALUE)
                                            .addComponent(jtfEmail, javax.swing.GroupLayout.DEFAULT_SIZE, 555, Short.MAX_VALUE)
                                            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jbBank, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(46, 46, 46)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jlCode, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1)
                    .addComponent(jtfName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jtfUnt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jtfEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jtfBank, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5)
                    .addComponent(jbBank))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(jtfAccount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel8)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jbClose)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JButton jbBank;
    private javax.swing.JButton jbClose;
    private javax.swing.JLabel jlCode;
    private javax.swing.JPanel jpContractDate;
    private javax.swing.JTextArea jtaAddress;
    private javax.swing.JTextArea jtaNote;
    private javax.swing.JTextArea jtaUnloadingAddress;
    private javax.swing.JTextField jtfAccount;
    private javax.swing.JTextField jtfBank;
    private javax.swing.JTextField jtfContractNumber;
    private javax.swing.JTextField jtfEmail;
    private javax.swing.JTextField jtfHead;
    private javax.swing.JTextField jtfHeadPositionName;
    private javax.swing.JTextField jtfName;
    private javax.swing.JTextField jtfPrimaryDocument;
    private javax.swing.JTextField jtfUnt;
    // End of variables declaration//GEN-END:variables
    private SalesView salesView;
    private ContractorsView parent;
    private Integer elementCode;
    private JDateChooser contractDate;
    private Integer bankCode;
    private Integer lock;
    private Integer hash;

    private void save() {

        try {
            Session session = HUtil.getSession();
            Contractors x = (Contractors) HUtil.getElement("Contractors", elementCode, session);
            session.beginTransaction();
            if (x == null) {
                x = new Contractors();
                x.setName(jtfName.getText());
                x.setUnt(jtfUnt.getText());
                x.setAddress(jtaAddress.getText());
                x.setUnloadingAddress(jtaUnloadingAddress.getText());
                x.setEmail(jtfEmail.getText());
                x.setBank(bankCode);
                x.setAccount(jtfAccount.getText());
                x.setContractNumber(jtfContractNumber.getText());
                x.setContractDate(contractDate.getDate());
                x.setHead(jtfHead.getText());
                x.setHeadPositionName(jtfHeadPositionName.getText());
                x.setPrimaryDocument(jtfPrimaryDocument.getText());
                x.setNote(jtaNote.getText());
                x.setLocked(0);
                session.save(x);
            } else if (lock == 0) {
                x.setName(jtfName.getText());
                x.setUnt(jtfUnt.getText());
                x.setAddress(jtaAddress.getText());
                x.setUnloadingAddress(jtaUnloadingAddress.getText());
                x.setEmail(jtfEmail.getText());
                x.setBank(bankCode);
                x.setAccount(jtfAccount.getText());
                x.setContractNumber(jtfContractNumber.getText());
                x.setContractDate(contractDate.getDate());
                x.setHead(jtfHead.getText());
                x.setHeadPositionName(jtfHeadPositionName.getText());
                x.setPrimaryDocument(jtfPrimaryDocument.getText());
                x.setNote(jtaNote.getText());
                x.setLocked(0);
                session.merge(x);
            }
            session.getTransaction().commit();
            session.close();
            Util.updateJournals(salesView, ContractorsView.class);
        } catch (Exception e) {
            logger.error(e);
        }
    }

    public void close() {

        if (lock == 0) {
            if (hash == null || !hash.equals(Util.hash(this))) {
                int a = JOptionPane.showOptionDialog(
                        this, "Сохранить элемент?", "Сохранение", JOptionPane.YES_NO_CANCEL_OPTION,
                        JOptionPane.QUESTION_MESSAGE, null, new Object[]{"Да", "Нет"}, "Да");
                if (a == 0) {
                    save();
                }
            }
            Session session = HUtil.getSession();
            Contractors x = (Contractors) HUtil.getElement("Contractors", elementCode, session);
            if (x != null && lock == 0) {
                session.beginTransaction();
                x.setLocked(0);
                session.merge(x);
                session.getTransaction().commit();
            }
            session.close();
        }
    }

    @Action
    public void closeEl() {
        close();
        Util.closeJIF(this, parent, salesView);
        Util.closeJIFTab(this, salesView);
    }

    @Action
    public void chooseBank() {
        JInternalFrame x = new BanksView(salesView, this);
        salesView.getJDesktopPane().add(x, javax.swing.JLayeredPane.DEFAULT_LAYER);
        x.setVisible(true);
        try {
            x.setSelected(true);
        } catch (java.beans.PropertyVetoException e) {
            logger.error(e);
        }
    }

    @Override
    public void changeBank(Integer bankCode) {
        this.bankCode = bankCode;
        Session session = HUtil.getSession();
        Banks b = (Banks) HUtil.getElement("Banks", bankCode, session);
        if (b != null) {
            jtfBank.setText(b.getName());
        } else {
            jtfBank.setText("");
        }
        session.close();
    }
}