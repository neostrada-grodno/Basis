/*
 * TTNHeader.java
 *
 * Created on 03.03.2012, 12:20:24
 */
package sales.outcoming;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.swing.JOptionPane;
import org.apache.log4j.Logger;
import org.hibernate.classic.Session;
import org.jdesktop.application.Action;
import org.jopendocument.dom.spreadsheet.Sheet;
import org.jopendocument.dom.spreadsheet.SpreadSheet;
import sales.SalesApp;
import sales.SalesView;
import sales.catalogs.TransportsView;
import sales.entity.Contractors;
import sales.entity.Employee;
import sales.entity.Nomenclature;
import sales.entity.Outcoming;
import sales.entity.Outcomingtable;
import sales.entity.Transports;
import sales.interfaces.ITransport;
import sales.util.HUtil;
import sales.util.Util;
import sales.util.fwMoney;
import sales.util.fwNumber;

public class TTNHeader extends javax.swing.JPanel implements ITransport {

    static Logger logger = Logger.getLogger(SalesApp.class.getName());

    /** Creates new form TTNHeader */
    public TTNHeader(OutcomingDocView parent, SalesView salesView, TNHeader tnheader, Integer documentCode, Integer lock, List table) {

        initComponents();

        this.parent = parent;
        this.salesView = salesView;
        this.tnheader = tnheader;
        this.documentCode = documentCode;
        this.lock = lock;
        this.table = table;

        if (documentCode != null) {

            Session session = HUtil.getSession();
            Outcoming o = (Outcoming) HUtil.getElement("Outcoming", documentCode);
            jtfAuto.setText(o.getAuto());
            jtfTrailer.setText(o.getTrailer());
            jtfOwner.setText(o.getOwner());
            jtfDriver.setText(o.getDriver());
            jtfLoadingAddress.setText(o.getLoadingAddress());
            jtfUnloadingAddress.setText(o.getUnloadingAddress());
            jtfRecipient.setText(o.getRecepientName());
            jtfRecipientName.setText(o.getRecepient());
            jtfWarrant.setText(o.getWarrant());
            jtfWarrantOrganization.setText(o.getWarrantOrganization());
            session.close();

            if (lock == 1) {

                jtfAuto.setEditable(false);
                jtfTrailer.setEditable(false);
                jtfOwner.setEditable(false);
                jtfDriver.setEditable(false);
            }
        } else {
            jtfLoadingAddress.setText(HUtil.getConstant("address"));
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

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jtfAuto = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jtfOwner = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jtfTrailer = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jtfDriver = new javax.swing.JTextField();
        jbTTN = new javax.swing.JButton();
        jbTransport = new javax.swing.JButton();
        jtfLoadingAddress = new javax.swing.JTextField();
        jtfUnloadingAddress = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jtfRecipientName = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jtfRecipient = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jtfWarrant = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jtfWarrantOrganization = new javax.swing.JTextField();
        jcbAttachment = new javax.swing.JCheckBox();

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(sales.SalesApp.class).getContext().getResourceMap(TTNHeader.class);
        setBackground(resourceMap.getColor("Form.background")); // NOI18N
        setName("Form"); // NOI18N

        jPanel1.setBackground(resourceMap.getColor("jPanel1.background")); // NOI18N
        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel1.setName("jPanel1"); // NOI18N

        jLabel1.setText(resourceMap.getString("jLabel1.text")); // NOI18N
        jLabel1.setName("jLabel1"); // NOI18N

        jtfAuto.setText(resourceMap.getString("jtfAuto.text")); // NOI18N
        jtfAuto.setName("jtfAuto"); // NOI18N

        jLabel3.setText(resourceMap.getString("jLabel3.text")); // NOI18N
        jLabel3.setName("jLabel3"); // NOI18N

        jtfOwner.setText(resourceMap.getString("jtfOwner.text")); // NOI18N
        jtfOwner.setName("jtfOwner"); // NOI18N

        jLabel2.setText(resourceMap.getString("jLabel2.text")); // NOI18N
        jLabel2.setName("jLabel2"); // NOI18N

        jtfTrailer.setText(resourceMap.getString("jtfTrailer.text")); // NOI18N
        jtfTrailer.setName("jtfTrailer"); // NOI18N

        jLabel4.setText(resourceMap.getString("jLabel4.text")); // NOI18N
        jLabel4.setName("jLabel4"); // NOI18N

        jtfDriver.setText(resourceMap.getString("jtfDriver.text")); // NOI18N
        jtfDriver.setName("jtfDriver"); // NOI18N

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(sales.SalesApp.class).getContext().getActionMap(TTNHeader.class, this);
        jbTTN.setAction(actionMap.get("printTTN")); // NOI18N
        jbTTN.setText(resourceMap.getString("jbTTN.text")); // NOI18N
        jbTTN.setName("jbTTN"); // NOI18N

        jbTransport.setAction(actionMap.get("chooseTransport")); // NOI18N
        jbTransport.setText(resourceMap.getString("jbTransport.text")); // NOI18N
        jbTransport.setName("jbTransport"); // NOI18N

        jtfLoadingAddress.setText(resourceMap.getString("jtfLoadingAddress.text")); // NOI18N
        jtfLoadingAddress.setName("jtfLoadingAddress"); // NOI18N

        jtfUnloadingAddress.setText(resourceMap.getString("jtfUnloadingAddress.text")); // NOI18N
        jtfUnloadingAddress.setName("jtfUnloadingAddress"); // NOI18N

        jLabel5.setText(resourceMap.getString("jLabel5.text")); // NOI18N
        jLabel5.setName("jLabel5"); // NOI18N

        jLabel6.setText(resourceMap.getString("jLabel6.text")); // NOI18N
        jLabel6.setName("jLabel6"); // NOI18N

        jPanel2.setBackground(resourceMap.getColor("jPanel2.background")); // NOI18N
        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel2.setName("jPanel2"); // NOI18N

        jtfRecipientName.setText(resourceMap.getString("jtfRecipientName.text")); // NOI18N
        jtfRecipientName.setName("jtfRecipientName"); // NOI18N

        jLabel7.setText(resourceMap.getString("jLabel7.text")); // NOI18N
        jLabel7.setName("jLabel7"); // NOI18N

        jtfRecipient.setText(resourceMap.getString("jtfRecipient.text")); // NOI18N
        jtfRecipient.setName("jtfRecipient"); // NOI18N

        jLabel9.setText(resourceMap.getString("jLabel9.text")); // NOI18N
        jLabel9.setName("jLabel9"); // NOI18N

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel7)
                    .addComponent(jLabel9))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jtfRecipient, javax.swing.GroupLayout.DEFAULT_SIZE, 275, Short.MAX_VALUE)
                    .addComponent(jtfRecipientName, javax.swing.GroupLayout.DEFAULT_SIZE, 275, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(jtfRecipientName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jtfRecipient, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel3.setBackground(resourceMap.getColor("jPanel3.background")); // NOI18N
        jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel3.setName("jPanel3"); // NOI18N

        jLabel8.setText(resourceMap.getString("jLabel8.text")); // NOI18N
        jLabel8.setName("jLabel8"); // NOI18N

        jtfWarrant.setText(resourceMap.getString("jtfWarrant.text")); // NOI18N
        jtfWarrant.setName("jtfWarrant"); // NOI18N

        jLabel11.setText(resourceMap.getString("jLabel11.text")); // NOI18N
        jLabel11.setName("jLabel11"); // NOI18N

        jtfWarrantOrganization.setText(resourceMap.getString("jtfWarrantOrganization.text")); // NOI18N
        jtfWarrantOrganization.setName("jtfWarrantOrganization"); // NOI18N

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jtfWarrant, javax.swing.GroupLayout.DEFAULT_SIZE, 223, Short.MAX_VALUE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel11)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jtfWarrantOrganization, javax.swing.GroupLayout.DEFAULT_SIZE, 313, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jtfWarrant, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jtfWarrantOrganization, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jcbAttachment.setBackground(resourceMap.getColor("jcbAttachment.background")); // NOI18N
        jcbAttachment.setText(resourceMap.getString("jcbAttachment.text")); // NOI18N
        jcbAttachment.setName("jcbAttachment"); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(15, 15, 15)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel3)
                            .addComponent(jLabel1)
                            .addComponent(jLabel5))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jtfLoadingAddress, javax.swing.GroupLayout.DEFAULT_SIZE, 284, Short.MAX_VALUE)
                            .addComponent(jtfOwner, javax.swing.GroupLayout.DEFAULT_SIZE, 284, Short.MAX_VALUE)
                            .addComponent(jtfAuto, javax.swing.GroupLayout.DEFAULT_SIZE, 284, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel6)
                            .addComponent(jLabel2)
                            .addComponent(jLabel4))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jtfTrailer, javax.swing.GroupLayout.DEFAULT_SIZE, 292, Short.MAX_VALUE)
                            .addComponent(jtfDriver, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 292, Short.MAX_VALUE)
                            .addComponent(jtfUnloadingAddress, javax.swing.GroupLayout.DEFAULT_SIZE, 292, Short.MAX_VALUE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jbTransport)
                        .addComponent(jbTTN))
                    .addComponent(jcbAttachment))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jtfTrailer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel2))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jtfDriver, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel4))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jtfUnloadingAddress, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel6)))
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel1)
                                .addComponent(jtfAuto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel3)
                                .addComponent(jtfOwner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jtfLoadingAddress, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel5))))
                    .addComponent(jbTransport))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jcbAttachment)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jbTTN))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
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
    private javax.swing.JPanel jPanel3;
    private javax.swing.JButton jbTTN;
    private javax.swing.JButton jbTransport;
    private javax.swing.JCheckBox jcbAttachment;
    private javax.swing.JTextField jtfAuto;
    private javax.swing.JTextField jtfDriver;
    private javax.swing.JTextField jtfLoadingAddress;
    private javax.swing.JTextField jtfOwner;
    private javax.swing.JTextField jtfRecipient;
    private javax.swing.JTextField jtfRecipientName;
    private javax.swing.JTextField jtfTrailer;
    private javax.swing.JTextField jtfUnloadingAddress;
    private javax.swing.JTextField jtfWarrant;
    private javax.swing.JTextField jtfWarrantOrganization;
    // End of variables declaration//GEN-END:variables
    private SalesView salesView;
    private OutcomingDocView parent;
    private Integer documentCode;
    private Integer lock;
    private TNHeader tnheader;
    private List table;

    public String getAuto() {
        return jtfAuto.getText();
    }

    public String getTrailer() {
        return jtfTrailer.getText();
    }

    public String getOwner() {
        return jtfOwner.getText();
    }

    public String getDriver() {
        return jtfDriver.getText();
    }

    public String getLoadingAddress() {
        return jtfLoadingAddress.getText();
    }

    public String getUnloadingAddress() {
        return jtfUnloadingAddress.getText();
    }

    public void setUnloadingAddress(String unloadingAddress) {
        jtfUnloadingAddress.setText(unloadingAddress);
    }

    public String getRecepient() {
        return jtfRecipient.getText();
    }

    public String getRecepientName() {
        return jtfRecipientName.getText();
    }

    public String getWarrant() {
        return jtfWarrant.getText();
    }

    public String getWarrantOrganization() {
        return jtfWarrantOrganization.getText();
    }

    @Action
    public void printTTN() {

        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yy");

        Calendar cal = Calendar.getInstance();
        Calendar calParent = Calendar.getInstance();
        calParent.setTime(parent.getDate());
        if (calParent.get(Calendar.YEAR) != cal.get(Calendar.YEAR)
                || calParent.get(Calendar.MONTH) != cal.get(Calendar.MONTH)
                || calParent.get(Calendar.DATE) != cal.get(Calendar.DATE)
                || calParent.get(Calendar.HOUR) != cal.get(Calendar.HOUR)
                || calParent.get(Calendar.MINUTE) != cal.get(Calendar.MINUTE)) {
            int a = JOptionPane.showOptionDialog(
                    this, "Изменить дату и время документа на текущущие?", "Дата и время", JOptionPane.YES_NO_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE, null, new Object[]{"Да", "Нет"}, "Да");
            if (a == 0) {
                parent.setDate(cal.getTime());
            }
        }
        if (!parent.getActive()) {
            parent.setActive(true);
        }

        Util.checkDocSaved(parent);

        cal.setTime(parent.getDate());

        try {

            File file = new File(Util.getAppPath() + "\\templates\\TTNV.ods");
            final SpreadSheet spreadSheet = SpreadSheet.createFromFile(file);
            final Sheet sheet = spreadSheet.getSheet(0);

            sheet.getCellAt("Z5").setValue(HUtil.getConstant("unt"));

            sheet.getCellAt("C14").setValue(cal.get(Calendar.DATE));
            String months[] =
                    new String[]{"Января", "Февраля", "Марта", "Апреля", "Мая", "Июня", "Июля", "Августа", "Сентября", "Октября", "Ноября", "Декабря"};
            sheet.getCellAt("J14").setValue(months[cal.get(Calendar.MONTH)]);
            sheet.getCellAt("AJ14").setValue((new SimpleDateFormat("yy")).format(cal.getTime()));

            sheet.getCellAt("M16").setValue(jtfAuto.getText());
            sheet.getCellAt("BL16").setValue(jtfTrailer.getText());
            sheet.getCellAt("U18").setValue(jtfOwner.getText());
            sheet.getCellAt("CS18").setValue(jtfDriver.getText());

            Session session = HUtil.getSession();

            Integer contractorCode = tnheader.getContractor();
            if (contractorCode != null) {
                Contractors c = (Contractors) HUtil.getElement("Contractors", contractorCode, session);
                if (c != null) {
                    sheet.getCellAt("AS20").setValue(c.getName() + ", " + c.getAddress());
                    sheet.getCellAt("R25").setValue(c.getName() + ", " + c.getAddress());
                    if (c.getUnt() != null) {
                        sheet.getCellAt("AM5").setValue(c.getUnt());
                        sheet.getCellAt("AZ5").setValue(c.getUnt());
                    }
                    if (c.getAddress() != null) {
                        sheet.getCellAt("P31").setValue(c.getAddress());
                    }
                }
            }

            Date contractDate = tnheader.getContractDate();
            if (contractDate != null) {
                sheet.getCellAt("Q27").setValue(
                        "Договор № " + tnheader.getContractNumber() + " от " + sdf.format(contractDate));
            }

            sheet.getCellAt("R23").setValue(HUtil.getConstant("name") + ", " + HUtil.getConstant("address"));

            sheet.getCellAt("O29").setValue(HUtil.getConstant("address"));

            int ts = table.size();

            String ndss = HUtil.getConstant("nds");
            Double nds = HUtil.getConstantDouble("nds");
            long amount = 0;
            long ndsAndAmount = 0;
            int cargos = 0;
            long weight = 0;
            int i = 0;

            if (!jcbAttachment.isSelected()) {

                if (ts > 1) {
                    sheet.duplicateRows(38, 1, ts - 1);
                }

                for (; i < ts; i++) {
                    Outcomingtable ot = (Outcomingtable) table.get(i);
                    Nomenclature n = (Nomenclature) HUtil.getElement("Nomenclature", ot.getProductCode());
                    Util.setCell("A", n.getName(), 39 + i, sheet);
                    Util.setCell("AF", n.getUnit(), 39 + i, sheet);
                    Util.setCell("AO", ot.getQuantity(), 39 + i, sheet);
                    Util.setCell("AX", Math.round(ot.getPrice() * 100 / (100 + nds)), 39 + i, sheet);
                    long a = Math.round(ot.getAmount() * 100 / (100 + nds));
                    Util.setCell("BK", a, 39 + i, sheet);
                    Util.setCell("BW", ndss, 39 + i, sheet);
                    Util.setCell("CD", ot.getAmount() - a, 39 + i, sheet);
                    Util.setCell("CO", ot.getAmount(), 39 + i, sheet);
                    Util.setCell("DA", ot.getCargos(), 39 + i, sheet);
                    Util.setCell("DJ", ot.getWeight(), 39 + i, sheet);
                    Util.setCell("DT",
                            "Счет (протокол) № " + tnheader.getInvoiceNumber() + " от " + sdf.format(parent.getDate()),
                            39 + i, sheet);

                    amount += a;
                    ndsAndAmount += ot.getAmount();
                    cargos += ot.getCargos();
                    weight += ot.getWeight();
                }

                Util.setCell("BK", amount, 39 + i, sheet);
                Util.setCell("CD", ndsAndAmount - amount, 39 + i, sheet);
                Util.setCell("CO", ndsAndAmount, 39 + i, sheet);
                Util.setCell("DA", cargos, 39 + i, sheet);
                Util.setCell("DJ", weight, 39 + i, sheet);

            } else {

                final Sheet sheet1 = spreadSheet.getSheet(1);

                Util.setCell("I", tnheader.getTNNumber(), 2, sheet1);
                Util.setCell("I", sdf.format(parent.getDate()), 3, sheet1);

                if (ts > 1) {
                    sheet1.duplicateRows(6, 1, ts - 1);
                }

                for (; i < ts; i++) {
                    Outcomingtable ot = (Outcomingtable) table.get(i);
                    Nomenclature n = (Nomenclature) HUtil.getElement("Nomenclature", ot.getProductCode());
                    Util.setCell("A", n.getName(), 7 + i, sheet1);
                    Util.setCell("B", n.getUnit(), 7 + i, sheet1);
                    Util.setCell("C", ot.getQuantity(), 7 + i, sheet1);
                    Util.setCell("D", Math.round(ot.getPrice() * 100 / (100 + nds)), 7 + i, sheet1);
                    long a = Math.round(ot.getAmount() * 100 / (100 + nds));
                    Util.setCell("E", a, 7 + i, sheet1);
                    Util.setCell("F", ndss, 7 + i, sheet1);
                    Util.setCell("G", ot.getAmount() - a, 7 + i, sheet1);
                    Util.setCell("H", ot.getAmount(), 7 + i, sheet1);
                    Util.setCell("I", ot.getCargos(), 7 + i, sheet1);
                    Util.setCell("J", ot.getWeight(), 7 + i, sheet1);
                    Util.setCell("K",
                            "Счет (протокол) № " + tnheader.getInvoiceNumber() + " от " + sdf.format(parent.getDate()),
                            7 + i, sheet1);

                    amount += a;
                    ndsAndAmount += ot.getAmount();
                    cargos += ot.getCargos();
                    weight += ot.getWeight();
                }

                Util.setCell("E", amount, 7 + i, sheet1);
                Util.setCell("G", ndsAndAmount - amount, 7 + i, sheet1);
                Util.setCell("H", ndsAndAmount, 7 + i, sheet1);
                Util.setCell("I", cargos, 7 + i, sheet1);
                Util.setCell("J", weight, 7 + i, sheet1);
                
                i = 1;

            }

            fwMoney m = new fwMoney(ndsAndAmount - amount);
            Util.setCell("O", m.num2str(), i + 41, sheet);
            m = new fwMoney(amount);
            Util.setCell("S", m.num2str(), i + 43, sheet);
            fwNumber n = new fwNumber(weight);
            Util.setCell("O", n.num2str() + " кг.", i + 45, sheet);
            n = new fwNumber(cargos);
            Util.setCell("CI", n.num2str(), i + 45, sheet);

            Employee authorizer = (Employee) HUtil.getElement("Employee", tnheader.getAuthorizer(), session);
            if (authorizer != null) {
                Util.setCell("O", authorizer.getOccupation(), i + 47, sheet);
                Util.setCell("A", authorizer.getShortName(), i + 49, sheet);
            }

            Util.setCell("CE", jtfRecipient.getText(), i + 47, sheet);
            Util.setCell("BK", jtfRecipientName.getText(), i + 49, sheet);

            Employee employee = (Employee) HUtil.getElement("Employee", tnheader.getEmployee(), session);
            if (employee != null) {
                Util.setCell("S", authorizer.getOccupation(), i + 51, sheet);
                Util.setCell("A", authorizer.getShortName(), i + 53, sheet);
            }

            Util.setCell("BX", jtfWarrant.getText(), i + 51, sheet);
            Util.setCell("BS", jtfWarrantOrganization.getText(), i + 53, sheet);

            session.close();

            String num = tnheader.getTNNumber();
            String r = "-" + Math.round(Math.random() * 100000);
            File outputFile = new File("temp\\TTH №" + num + r + ".ods");
            spreadSheet.saveAs(outputFile);
            Util.openDoc("temp\\TTH №" + num + r + ".ods");
        } catch (Exception e) {
            logger.error(e);
        }
    }

    @Action
    public void chooseTransport() {

        TransportsView ev = new TransportsView(salesView, this);
        salesView.getJDesktopPane().add(ev, javax.swing.JLayeredPane.DEFAULT_LAYER);
        ev.setVisible(true);
        try {
            ev.setSelected(true);
        } catch (java.beans.PropertyVetoException e) {
            logger.error(e);
        }
    }

    public void setTransport(Integer transportCode) {

        Session session = HUtil.getSession();
        Transports t = (Transports) HUtil.getElement("Transports", transportCode, session);
        if (t != null) {
            jtfAuto.setText(t.getAuto());
            jtfTrailer.setText(t.getTrailer());
            jtfOwner.setText(t.getOwner());
            jtfDriver.setText(t.getDriver());
        }
        session.close();
    }
}
