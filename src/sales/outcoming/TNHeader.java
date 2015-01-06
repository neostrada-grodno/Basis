/*
 * TNHeader.java
 *
 * Created on 14.01.2012, 10:39:09
 */
package sales.outcoming;

import org.jdesktop.application.Action;
import com.artofsolving.jodconverter.DocumentConverter;
import com.artofsolving.jodconverter.openoffice.connection.OpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.connection.SocketOpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.converter.OpenOfficeDocumentConverter;
import com.toedter.calendar.JDateChooser;
import java.awt.Cursor;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.swing.GroupLayout;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import org.apache.log4j.Logger;
import org.hibernate.classic.Session;
import org.jopendocument.dom.spreadsheet.Sheet;
import org.jopendocument.dom.spreadsheet.SpreadSheet;
import sales.MessageSend;
import sales.SalesApp;
import sales.SalesView;
import sales.catalogs.ContractorsView;
import sales.catalogs.EmployeeView;
import sales.entity.Banks;
import sales.entity.Contractors;
import sales.entity.Employee;
import sales.entity.Incoming;
import sales.entity.Nomenclature;
import sales.entity.Outcoming;
import sales.entity.Outcomingpayments;
import sales.entity.Outcomingtable;
import sales.interfaces.IContractor;
import sales.interfaces.IEmployee;
import sales.interfaces.IPaymentsView;
import sales.util.HUtil;
import sales.util.Util;
import sales.util.fwMoney;

public class TNHeader extends javax.swing.JPanel implements IContractor, IEmployee, IPaymentsView {

    static Logger logger = Logger.getLogger(SalesApp.class.getName());

    public TNHeader(OutcomingDocView parent, SalesView salesView, Integer documentCode, Integer lock, List table) {

        initComponents();

        this.parent = parent;
        this.salesView = salesView;
        this.documentCode = documentCode;
        this.lock = lock;
        this.table = table;

        if (documentCode == null) {

            Calendar cl = Calendar.getInstance();
            contractDate = new JDateChooser(cl.getTime());
            invoiceDate = new JDateChooser(cl.getTime());
            payments = new ArrayList();
            jtfInvoiceNumber.setText("");
            jtfTNNumber.setText("");
            jtfContractNumber.setText("");


        } else {

            Session session = HUtil.getSession();

            Outcoming o = (Outcoming) HUtil.getElement("Outcoming", documentCode);

            jtfInvoiceNumber.setText(o.getInvoiceNumber());
            jtfTNNumber.setText(o.getTnnumber());
            jtfContractNumber.setText(o.getContractNumber());
            contractDate = new JDateChooser(o.getContractDate());
            invoiceDate = new JDateChooser(o.getInvoiceDate());

            payments = HUtil.executeHql("from Outcomingpayments op where op.documentCode = " + o.getCode());

            if (o.getContractor() != null) {
                Contractors c = (Contractors) HUtil.getElement("Contractors", o.getContractor(), session);
                contractorCode = c.getCode();
                jtfContractor.setText(c.getName());
            }

            if (o.getEmployee() != null) {
                Employee e = (Employee) HUtil.getElement("Employee", o.getEmployee(), session);
                employeeCode = e.getCode();
                jtfShipper.setText(e.getShortName());
            }

            if (o.getAthorizer() != null) {
                Employee e = (Employee) HUtil.getElement("Employee", o.getAthorizer(), session);
                authorizerCode = e.getCode();
                jtfAuthorizer.setText(e.getShortName());
            }

            if (lock == 1) {

                jtfInvoiceNumber.setEditable(false);
                jtfTNNumber.setEditable(false);
                jtfContractNumber.setEditable(false);
                contractDate.setEnabled(false);
                invoiceDate.setEnabled(false);
                jbContractor.setEnabled(false);
                jbAuthorizer.setEnabled(false);
                jbResponsible.setEnabled(false);
            }

            session.close();
        }

        GroupLayout gl = (GroupLayout) jpContractDate.getLayout();
        gl.setHorizontalGroup(gl.createParallelGroup().addGroup(gl.createSequentialGroup().addComponent(contractDate)));
        gl.setVerticalGroup(gl.createParallelGroup().addGroup(gl.createSequentialGroup().addComponent(contractDate)));

        gl = (GroupLayout) jpInvoiceDate.getLayout();
        gl.setHorizontalGroup(gl.createParallelGroup().addGroup(gl.createSequentialGroup().addComponent(invoiceDate)));
        gl.setVerticalGroup(gl.createParallelGroup().addGroup(gl.createSequentialGroup().addComponent(invoiceDate)));

        showPaymentsSum();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel3 = new javax.swing.JPanel();
        jlInvoiceNumber = new javax.swing.JLabel();
        jtfInvoiceNumber = new javax.swing.JTextField();
        jbInvoice = new javax.swing.JButton();
        jbEmail = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jpInvoiceDate = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jlTNNumber = new javax.swing.JLabel();
        jtfTNNumber = new javax.swing.JTextField();
        jbTN = new javax.swing.JButton();
        jbNextTNNumber = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jbPayments = new javax.swing.JButton();
        jlPaymentsAmount = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jlContractNumber = new javax.swing.JLabel();
        jtfContractNumber = new javax.swing.JTextField();
        jlContractDate = new javax.swing.JLabel();
        jpContractDate = new javax.swing.JPanel();
        jbContract = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jlContractor = new javax.swing.JLabel();
        jtfContractor = new javax.swing.JTextField();
        jbContractor = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jlResponsible = new javax.swing.JLabel();
        jtfShipper = new javax.swing.JTextField();
        jbResponsible = new javax.swing.JButton();
        jtfAuthorizer = new javax.swing.JTextField();
        jbAuthorizer = new javax.swing.JButton();

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance().getContext().getResourceMap(TNHeader.class);
        setBackground(resourceMap.getColor("Form.background")); // NOI18N
        setName("Form"); // NOI18N

        jPanel3.setBackground(resourceMap.getColor("jPanel3.background")); // NOI18N
        jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel3.setName("jPanel3"); // NOI18N

        jlInvoiceNumber.setText(resourceMap.getString("jlInvoiceNumber.text")); // NOI18N
        jlInvoiceNumber.setName("jlInvoiceNumber"); // NOI18N

        jtfInvoiceNumber.setName("jtfInvoiceNumber"); // NOI18N

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance().getContext().getActionMap(TNHeader.class, this);
        jbInvoice.setAction(actionMap.get("printInvoice")); // NOI18N
        jbInvoice.setText(resourceMap.getString("jbInvoice.text")); // NOI18N
        jbInvoice.setName("jbInvoice"); // NOI18N

        jbEmail.setAction(actionMap.get("sendEmail")); // NOI18N
        jbEmail.setText(resourceMap.getString("jbEmail.text")); // NOI18N
        jbEmail.setName("jbEmail"); // NOI18N

        jLabel2.setText(resourceMap.getString("jLabel2.text")); // NOI18N
        jLabel2.setName("jLabel2"); // NOI18N

        jpInvoiceDate.setBackground(resourceMap.getColor("jpInvoiceDate.background")); // NOI18N
        jpInvoiceDate.setName("jpInvoiceDate"); // NOI18N

        javax.swing.GroupLayout jpInvoiceDateLayout = new javax.swing.GroupLayout(jpInvoiceDate);
        jpInvoiceDate.setLayout(jpInvoiceDateLayout);
        jpInvoiceDateLayout.setHorizontalGroup(
            jpInvoiceDateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jpInvoiceDateLayout.setVerticalGroup(
            jpInvoiceDateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 19, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jlInvoiceNumber)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jtfInvoiceNumber, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpInvoiceDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jbInvoice)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jbEmail)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jbEmail)
                        .addComponent(jbInvoice, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jpInvoiceDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jtfInvoiceNumber)
                        .addComponent(jlInvoiceNumber, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel2)))
                .addContainerGap())
        );

        jPanel4.setBackground(resourceMap.getColor("jPanel4.background")); // NOI18N
        jPanel4.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel4.setName("jPanel4"); // NOI18N

        jlTNNumber.setText(resourceMap.getString("jlTNNumber.text")); // NOI18N
        jlTNNumber.setName("jlTNNumber"); // NOI18N

        jtfTNNumber.setName("jtfTNNumber"); // NOI18N

        jbTN.setAction(actionMap.get("printTN")); // NOI18N
        jbTN.setText(resourceMap.getString("jbTN.text")); // NOI18N
        jbTN.setName("jbTN"); // NOI18N

        jbNextTNNumber.setAction(actionMap.get("nextTNNumber")); // NOI18N
        jbNextTNNumber.setText(resourceMap.getString("jbNextTNNumber.text")); // NOI18N
        jbNextTNNumber.setName("jbNextTNNumber"); // NOI18N

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jlTNNumber)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jtfTNNumber, javax.swing.GroupLayout.DEFAULT_SIZE, 162, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jbNextTNNumber)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jbTN)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jtfTNNumber)
                    .addComponent(jlTNNumber)
                    .addComponent(jbTN, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jbNextTNNumber))
                .addContainerGap())
        );

        jPanel5.setBackground(resourceMap.getColor("jPanel5.background")); // NOI18N
        jPanel5.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel5.setName("jPanel5"); // NOI18N

        jbPayments.setAction(actionMap.get("showPayments")); // NOI18N
        jbPayments.setText(resourceMap.getString("jbPayments.text")); // NOI18N
        jbPayments.setName("jbPayments"); // NOI18N

        jlPaymentsAmount.setFont(resourceMap.getFont("jlPaymentsAmount.font")); // NOI18N
        jlPaymentsAmount.setForeground(resourceMap.getColor("jlPaymentsAmount.foreground")); // NOI18N
        jlPaymentsAmount.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jlPaymentsAmount.setText(resourceMap.getString("jlPaymentsAmount.text")); // NOI18N
        jlPaymentsAmount.setName("jlPaymentsAmount"); // NOI18N

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jlPaymentsAmount, javax.swing.GroupLayout.DEFAULT_SIZE, 232, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jbPayments, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jlPaymentsAmount, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jbPayments))
                .addContainerGap())
        );

        jPanel6.setBackground(resourceMap.getColor("jPanel6.background")); // NOI18N
        jPanel6.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel6.setName("jPanel6"); // NOI18N

        jlContractNumber.setText(resourceMap.getString("jlContractNumber.text")); // NOI18N
        jlContractNumber.setName("jlContractNumber"); // NOI18N

        jtfContractNumber.setName("jtfContractNumber"); // NOI18N

        jlContractDate.setText(resourceMap.getString("jlContractDate.text")); // NOI18N
        jlContractDate.setName("jlContractDate"); // NOI18N

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
            .addGap(0, 23, Short.MAX_VALUE)
        );

        jbContract.setAction(actionMap.get("printContract")); // NOI18N
        jbContract.setText(resourceMap.getString("jbContract.text")); // NOI18N
        jbContract.setName("jbContract"); // NOI18N

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(jlContractNumber)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jtfContractNumber, javax.swing.GroupLayout.DEFAULT_SIZE, 110, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jlContractDate)
                .addGap(6, 6, 6)
                .addComponent(jpContractDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jbContract)
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jbContract, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jpContractDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jlContractNumber, javax.swing.GroupLayout.DEFAULT_SIZE, 23, Short.MAX_VALUE)
                        .addComponent(jtfContractNumber, javax.swing.GroupLayout.DEFAULT_SIZE, 23, Short.MAX_VALUE)
                        .addComponent(jlContractDate, javax.swing.GroupLayout.DEFAULT_SIZE, 23, Short.MAX_VALUE)))
                .addContainerGap())
        );

        jPanel1.setBackground(resourceMap.getColor("jPanel1.background")); // NOI18N
        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel1.setName("jPanel1"); // NOI18N

        jlContractor.setText(resourceMap.getString("jlContractor.text")); // NOI18N
        jlContractor.setName("jlContractor"); // NOI18N

        jtfContractor.setBackground(resourceMap.getColor("jtfContractor.background")); // NOI18N
        jtfContractor.setEditable(false);
        jtfContractor.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        jtfContractor.setName("jtfContractor"); // NOI18N

        jbContractor.setAction(actionMap.get("chooseContractor")); // NOI18N
        jbContractor.setText(resourceMap.getString("jbContractor.text")); // NOI18N
        jbContractor.setName("jbContractor"); // NOI18N

        jLabel1.setText(resourceMap.getString("jLabel1.text")); // NOI18N
        jLabel1.setName("jLabel1"); // NOI18N

        jlResponsible.setText(resourceMap.getString("jlResponsible.text")); // NOI18N
        jlResponsible.setName("jlResponsible"); // NOI18N

        jtfShipper.setBackground(resourceMap.getColor("jtfShipper.background")); // NOI18N
        jtfShipper.setEditable(false);
        jtfShipper.setName("jtfShipper"); // NOI18N

        jbResponsible.setAction(actionMap.get("chooseEmployee")); // NOI18N
        jbResponsible.setText(resourceMap.getString("jbResponsible.text")); // NOI18N
        jbResponsible.setName("jbResponsible"); // NOI18N

        jtfAuthorizer.setBackground(resourceMap.getColor("jtfAuthorizer.background")); // NOI18N
        jtfAuthorizer.setText(resourceMap.getString("jtfAuthorizer.text")); // NOI18N
        jtfAuthorizer.setName("jtfAuthorizer"); // NOI18N

        jbAuthorizer.setAction(actionMap.get("chooseAthorizer")); // NOI18N
        jbAuthorizer.setText(resourceMap.getString("jbAuthorizer.text")); // NOI18N
        jbAuthorizer.setName("jbAuthorizer"); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jlContractor, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jtfAuthorizer, javax.swing.GroupLayout.DEFAULT_SIZE, 288, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jbAuthorizer, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jlResponsible)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jtfShipper, javax.swing.GroupLayout.PREFERRED_SIZE, 234, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jtfContractor, javax.swing.GroupLayout.DEFAULT_SIZE, 687, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jbContractor, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jbResponsible, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jtfContractor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jbContractor, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jlContractor))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jtfAuthorizer)
                    .addComponent(jtfShipper, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jlResponsible)
                    .addComponent(jbAuthorizer, 0, 0, Short.MAX_VALUE)
                    .addComponent(jbResponsible))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JButton jbAuthorizer;
    private javax.swing.JButton jbContract;
    private javax.swing.JButton jbContractor;
    private javax.swing.JButton jbEmail;
    private javax.swing.JButton jbInvoice;
    private javax.swing.JButton jbNextTNNumber;
    private javax.swing.JButton jbPayments;
    private javax.swing.JButton jbResponsible;
    private javax.swing.JButton jbTN;
    private javax.swing.JLabel jlContractDate;
    private javax.swing.JLabel jlContractNumber;
    private javax.swing.JLabel jlContractor;
    private javax.swing.JLabel jlInvoiceNumber;
    private javax.swing.JLabel jlPaymentsAmount;
    private javax.swing.JLabel jlResponsible;
    private javax.swing.JLabel jlTNNumber;
    private javax.swing.JPanel jpContractDate;
    private javax.swing.JPanel jpInvoiceDate;
    private javax.swing.JTextField jtfAuthorizer;
    private javax.swing.JTextField jtfContractNumber;
    private javax.swing.JTextField jtfContractor;
    private javax.swing.JTextField jtfInvoiceNumber;
    private javax.swing.JTextField jtfShipper;
    private javax.swing.JTextField jtfTNNumber;
    // End of variables declaration//GEN-END:variables
    private SalesView salesView;
    private OutcomingDocView parent;
    private Integer documentCode;
    private Integer lock;
    private List payments;
    private JDateChooser contractDate;
    private JDateChooser invoiceDate;
    private Integer contractorCode;
    private Integer employeeCode;
    private Integer authorizerCode;
    private List table;

    public Date getContractDate() {
        return contractDate.getDate();
    }

    public Date getInvoiceDate() {
        return invoiceDate.getDate();
    }

    public Integer getContractor() {
        return contractorCode;
    }

    public void setContractor(Integer contractorCode) {
        try {
            this.contractorCode = contractorCode;
            Session session = HUtil.getSession();
            Contractors c = (Contractors) HUtil.getElement("Contractors", contractorCode, session);
            if (c != null) {
                jtfContractor.setText(c.getName());
                jtfContractNumber.setText(c.getContractNumber());
                contractDate.setDate(c.getContractDate());
                parent.setUnloadingAddress(c.getUnloadingAddress());
            }
            session.close();
        } catch (Exception e) {
            logger.error(e);
        }
    }

    public Integer getEmployee() {
        return employeeCode;
    }

    public Integer getAuthorizer() {
        return authorizerCode;
    }

    public void setEmployee(Integer code, String name) {

        Session session = HUtil.getSession();
        Employee e = (Employee) HUtil.getElement("Employee", code, session);
        if (e != null) {
            if (name.equals("Employee")) {
                this.employeeCode = code;
                jtfShipper.setText(e.getShortName());
            } else if (name.equals("Authorizer")) {
                this.authorizerCode = code;
                jtfAuthorizer.setText(e.getShortName());
            }
        }
        session.close();
    }

    public String getInvoiceNumber() {
        return jtfInvoiceNumber.getText();
    }

    public String getTNNumber() {
        return jtfTNNumber.getText();
    }

    public String getContractNumber() {
        return jtfContractNumber.getText();
    }

    public List getPayments() {
        return payments;
    }

    @Override
    public void setPayments(List payments) {
        this.payments = payments;
        showPaymentsSum();
    }

    @Action
    public void showPayments() {
        PaymentsView x =
                new PaymentsView(
                salesView,
                this,
                documentCode,
                payments,
                lock);
        salesView.getJDesktopPane().add(x, javax.swing.JLayeredPane.DEFAULT_LAYER);
        x.setVisible(true);
        try {
            x.setSelected(true);
        } catch (java.beans.PropertyVetoException e) {
            logger.error(e);
        }
    }

    private void showPaymentsSum() {
        int sum = 0;
        for (int i = 0; i < payments.size(); i++) {
            sum += ((Outcomingpayments) payments.get(i)).getAmount();
        }
        jlPaymentsAmount.setText("Сумма платежей: " + sum);
    }

    @Action
    public void nextTNNumber() {
        Session session = HUtil.getSession();
        jtfTNNumber.setText(
                HUtil.getNextMax("Outcoming", "tnnumber", "x.documentType = 0", session));
        session.close();
    }

    public void nextInvoiceNumber() {
        Session session = HUtil.getSession();
        jtfInvoiceNumber.setText(
                HUtil.getNextMax("Outcoming", "invoiceNumber", "x.documentType = 0", session));
        session.close();
    }

    private String createInvoice() {

        String fileName = "";

        Util.checkDocSaved(parent);

        try {

            int st = Integer.parseInt(HUtil.getConstant("st"));
            int nds = Integer.parseInt(HUtil.getConstant("nds"));

            File file = new File(Util.getAppPath() + "\\templates\\Invoice.ods");
            final Sheet sheet = SpreadSheet.createFromFile(file).getSheet(0);
            sheet.getCellAt("A1").setValue(HUtil.getConstant("name") + ", УНП: " + HUtil.getConstant("unt"));
            sheet.getCellAt("A2").setValue("Адрес: " + HUtil.getConstant("address"));
            sheet.getCellAt("A3").setValue(
                    "Р/сч: " + HUtil.getConstant("account")
                    + " в " + HUtil.getConstant("bank")
                    + ", код " + HUtil.getConstant("bankCode"));
            sheet.getCellAt("A6").setValue(
                    "СЧЕТ (ПРОТОКОЛ СОГЛАСОВАНИЯ ЦЕН) № " + jtfInvoiceNumber.getText() + " от " + Util.date2String(invoiceDate.getDate()));
            sheet.getCellAt("B20").setValue(
                    "Директор_______________" + HUtil.getShortNameByCode(HUtil.getIntConstant("director")));
            
            Session session = HUtil.getSession();
            
            if (contractorCode != null) {
                Contractors c = (Contractors) HUtil.getElement("Contractors", contractorCode, session);
                if (c != null) {
                    sheet.getCellAt("A9").setValue("Заказчик: " + c.getName() + ", " + c.getAddress());
                    sheet.getCellAt("A10").setValue("Плательщик: " + c.getName() + ", " + c.getAddress());
                    Banks b = (Banks) HUtil.getElement("Banks", c.getBank(), session);
                    if (b != null) {
                        sheet.getCellAt("A11").setValue(
                                "Р/сч: " + c.getAccount() + ", "
                                + b.getName() + ", "
                                + b.getAddress() + ", "
                                + "код: " + b.getBankCode());
                    } else {
                        sheet.getCellAt("A11").setValue("Р/сч: " + c.getAccount());
                    }
                    sheet.getCellAt("A12").setValue("УНН: " + c.getUnt());
                    String headPositionName = "";
                    if (c.getHeadPositionName() != null) {
                        headPositionName = c.getHeadPositionName();
                    }
                    String head = "";
                    if (c.getHead() != null) {
                        head = c.getHead();
                    }
                    sheet.getCellAt("E20").setValue(
                        headPositionName + "_______________" + head);
                }
            }

            int ts = table.size();
            if (ts > 1) {
                sheet.duplicateRows(13, 1, ts - 1);
            }
            long amount = 0;
            long amountNds = 0;
            int i;
            for (i = 0; i < ts; i++) {

                Outcomingtable ot = (Outcomingtable) table.get(i);
                
                sheet.getCellAt("A" + (14 + i)).setValue(i + 1);
                Nomenclature n = (Nomenclature) HUtil.getElement("Nomenclature", ot.getProductCode(), session);
                sheet.getCellAt("B" + (14 + i)).setValue(n.getName());
                sheet.getCellAt("D" + (14 + i)).setValue(ot.getQuantity());
                Integer price = ot.getPrice();
                if (price > 0) {
                    sheet.getCellAt("E" + (14 + i)).setValue(price);
                }
                sheet.getCellAt("I" + (14 + i)).setValue(ot.getAmount());
                if (st == 0) {
                    sheet.getCellAt("F" + (14 + i)).setValue(ot.getAmount() - ot.getAmount() * nds / 100);
                    sheet.getCellAt("G" + (14 + i)).setValue(nds);
                    sheet.getCellAt("H" + (14 + i)).setValue(ot.getAmount() * nds / 100);
                    amountNds += ot.getAmount() * nds / 100;
                } else {
                    sheet.getCellAt("F" + (14 + i)).setValue(ot.getAmount());
                }
                amount += (Integer) ot.getAmount();
            }
            if (st == 0) {
                fwMoney mo = new fwMoney(amountNds);
                sheet.getCellAt("B" + (15 + i)).setValue("Сумма НДС: " + mo.num2str(true));
                sheet.getCellAt("F" + (14 + i)).setValue(amount - amount * nds / 100);
                sheet.getCellAt("G" + (14 + i)).setValue(nds);
                sheet.getCellAt("H" + (14 + i)).setValue(amount * nds / 100);
            } else {
                sheet.getCellAt("F" + (14 + i)).setValue(amount);
            }
            sheet.getCellAt("I" + (14 + i)).setValue(amount);

            fwMoney mo = new fwMoney(amount);
            sheet.getCellAt("B" + (17 + i)).setValue("Всего к оплате на сумму с НДС: " + mo.num2str(true));

            session.close();

            String r = " " + Math.round(Math.random() * 100000);
            fileName = "temp\\Cчет-фактура № " + jtfInvoiceNumber.getText() + r + ".ods";
            File outputFile = new File(fileName);
            sheet.getSpreadSheet().saveAs(outputFile);

        } catch (Exception e) {
            logger.error(e);
        }

        return fileName;
    }

    @Action
    public void printInvoice() {
        String fileName = createInvoice();
        if (!fileName.isEmpty()) {
            Util.openDoc(fileName);
        }
    }

    @Action
    public void printTN() {

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

        try {
            File file = new File(Util.getAppPath() + "\\templates\\TN.ods");
            final Sheet sheet = SpreadSheet.createFromFile(file).getSheet(0);
            sheet.getCellAt("E17").setValue(HUtil.getConstant("name") + ", " + HUtil.getConstant("address"));
            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yy");
            Session session = HUtil.getSession();
            if (contractorCode != null) {
                Contractors c = (Contractors) HUtil.getElement("Contractors", contractorCode, session);
                if (c != null) {
                    String cName = c.getName() + ", " + c.getAddress();
                    sheet.getCellAt("E19").setValue(cName);
                    sheet.getCellAt("M42").setValue(cName);
                    if (c.getUnt() != null) {
                        sheet.getCellAt("O6").setValue(c.getUnt());
                    }
                }
            }
            if (contractDate.getDate() != null) {
                sheet.getCellAt("E21").setValue(
                        "Договор № " + jtfContractNumber.getText() + " от " + sdf.format(contractDate.getDate()));
            }

            sheet.getCellAt("A15").setValue(Util.date2String(Calendar.getInstance().getTime()));
            sheet.getCellAt("F42").setValue(sdf.format(Calendar.getInstance().getTime()));
            sheet.getCellAt("L6").setValue(HUtil.getConstant("unt"));

            int ts = table.size();
            if (ts > 1) {
                sheet.duplicateRows(26, 1, ts - 1);
            }

            int amount = 0;
            int i;
            for (i = 0; i < ts; i++) {
                Outcomingtable ot = (Outcomingtable) table.get(i);
                Nomenclature n = (Nomenclature) HUtil.getElement("Nomenclature", ot.getProductCode());
                sheet.getCellAt("A" + (27 + i)).setValue(n.getName());
                sheet.getCellAt("J" + (27 + i)).setValue(ot.getQuantity());
                sheet.getCellAt("L" + (27 + i)).setValue(ot.getPrice());
                sheet.getCellAt("N" + (27 + i)).setValue(ot.getAmount());
                sheet.getCellAt("T" + (27 + i)).setValue(ot.getAmount());

                String note = "Счет (протокол) № " + jtfInvoiceNumber.getText() + " от " + Util.date2str(invoiceDate.getDate());
                Incoming in = (Incoming) HUtil.getElement("Incoming", ot.getIncomingCode(), session);
                if (in != null) {
                    note += ", Реестр розн.цен № " + in.getNumber() + " от " + Util.date2str(in.getDatetime());
                }
                    sheet.getCellAt("V" + (27 + i)).setValue(note);

                amount += (Integer) ot.getAmount();
            }

            sheet.getCellAt("N" + (27 + i)).setValue(amount);
            sheet.getCellAt("T" + (27 + i)).setValue(amount);
            fwMoney mo = new fwMoney(amount);
            sheet.getCellAt("E" + (32 + i)).setValue(mo.num2str(true));

            Employee e = (Employee) HUtil.getElement("Employee", authorizerCode, session);
            if (e != null) {
                sheet.getCellAt("D" + (35 + i)).setValue(
                        "  " + e.getOccupation() + "                                                  " + e.getShortName());
            }
            e = (Employee) HUtil.getElement("Employee", employeeCode, session);
            if (e != null) {
                sheet.getCellAt("E" + (37 + i)).setValue(
                        "  " + e.getOccupation() + "                                                  " + e.getShortName());
            }

            String r = "-" + Math.round(Math.random() * 100000);
            File outputFile = new File("temp\\TH №" + jtfTNNumber.getText() + r + ".ods");
            sheet.getSpreadSheet().saveAs(outputFile);
            Util.openDoc("temp\\TH №" + jtfTNNumber.getText() + r + ".ods");
        } catch (Exception e) {
            logger.error(e);
        }
    }

    @Action
    public void printContract() {

        Util.checkDocSaved(parent);

        try {
            File file = new File(Util.getAppPath() + "\\templates\\Contract.ods");
            final Sheet sheet = SpreadSheet.createFromFile(file).getSheet(0);
            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yy");
            Session session = HUtil.getSession();
            sheet.getCellAt("A1").setValue("ДОГОВОР № " + jtfContractNumber.getText());
            if (contractDate.getDate() != null) {
                sheet.getCellAt("J4").setValue(sdf.format(contractDate.getDate()));
            }
            if (contractorCode != null) {
                Contractors c = (Contractors) HUtil.getElement("Contractors", contractorCode, session);
                if (c != null) {
                    sheet.getCellAt("A6").setValue(
                            "\"" + c.getName() + "\" в дальнейшем “Покупатель”, в лице " + c.getHeadPositionName() + "а" + " " + c.getHead());
                    sheet.getCellAt("A7").setValue(
                            "действующего на основании " + c.getPrimaryDocument() + ", с другой стороны, заключили настоящий Договор о нижеследующем:");
                    sheet.getCellAt("F39").setValue(c.getName());
                    sheet.getCellAt("F40").setValue(c.getAddress());
                    sheet.getCellAt("F41").setValue("УНП " + c.getUnt());
                    Banks b = (Banks) HUtil.getElement("Banks", c.getBank(), session);
                    if (c.getAccount() != null && b != null) {
                        sheet.getCellAt("F42").setValue(
                                "Банк: " + b.getName() + ", " + b.getAddress()
                                + ", р/с " + c.getAccount()
                                + ", код банка " + b.getBankCode());
                    }
                }
            }
            sheet.getCellAt("A39").setValue(HUtil.getConstant("name"));
            sheet.getCellAt("A40").setValue(HUtil.getConstant("address"));
            sheet.getCellAt("A41").setValue("УНП: " + HUtil.getConstant("unt"));
            sheet.getCellAt("A42").setValue(
                    "Банк: " + HUtil.getConstant("bank")
                    + ", р/с " + HUtil.getConstant("account")
                    + ", код банка " + HUtil.getConstant("bankCode"));
            sheet.getCellAt("A43").setValue("директор_____________________ " + HUtil.getShortNameByCode(HUtil.getIntConstant("director")));

            String r = " " + Math.round(Math.random() * 100000);
            File outputFile = new File("temp\\Договор № " + jtfTNNumber.getText() + r + ".ods");
            sheet.getSpreadSheet().saveAs(outputFile);
            Util.openDoc("temp\\Договор № " + jtfTNNumber.getText() + r + ".ods");
        } catch (Exception e) {
            logger.error(e);
        }
    }

    @Action
    public void sendEmail() {

        String fileName = createInvoice();

        if (!fileName.isEmpty()) {
            try {
                
                
                setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        
                String docName = "Cчет-фактура № " + jtfInvoiceNumber.getText();
                File inputFile = new File(fileName);
                File outputFile = new File("temp\\" + docName + ".xls");
                OpenOfficeConnection connection = new SocketOpenOfficeConnection(8100);
                connection.connect();
                DocumentConverter converter = new OpenOfficeDocumentConverter(connection);
                converter.convert(inputFile, outputFile);
                connection.disconnect();
                
                setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

                JInternalFrame mb = new MessageSend(
                        salesView, this, contractorCode,
                        "temp\\" + docName + ".xls", docName,
                        "Здравствуйте,\n\nК письму присоединена " + docName + ".\n\n"
                        + "С уважением,\n" + HUtil.getConstant("name"),
                        docName + " успешно отправлена", "");
                salesView.getJDesktopPane().add(mb, javax.swing.JLayeredPane.DEFAULT_LAYER);
                mb.setVisible(true);
                try {
                    mb.setSelected(true);
                } catch (java.beans.PropertyVetoException e) {
                    logger.error(e);
                }
            } catch (Exception e) {
                logger.error(e);
            } finally {
                setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            }
        }
    }

    @Action
    public void chooseContractor() {
        JInternalFrame cc = new ContractorsView(contractorCode, salesView, this);
        salesView.getJDesktopPane().add(cc, javax.swing.JLayeredPane.DEFAULT_LAYER);
        cc.setVisible(true);
        try {
            cc.setSelected(true);
        } catch (java.beans.PropertyVetoException e) {
            logger.error(e);
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

    @Action
    public void chooseAthorizer() {
        EmployeeView ev = new EmployeeView(salesView, this, "Authorizer");
        salesView.getJDesktopPane().add(ev, javax.swing.JLayeredPane.DEFAULT_LAYER);
        ev.setVisible(true);
        try {
            ev.setSelected(true);
        } catch (java.beans.PropertyVetoException e) {
            logger.error(e);
        }
    }

    public void setTNButtonVisible(boolean visible) {
        jbTN.setEnabled(visible);
    }
}
