package sales;

import java.awt.Cursor;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;
import javax.swing.JOptionPane;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.log4j.Logger;
import org.hibernate.classic.Session;
import org.jdesktop.application.Action;
import sales.entity.Contractors;
import sales.interfaces.IClose;
import sales.outcoming.TNHeader;
import sales.util.HUtil;
import sales.util.Util;

public class MessageSend extends javax.swing.JInternalFrame implements IClose {

    static Logger logger = Logger.getLogger(SalesApp.class.getName());

    /** Creates new form MessageBody */
    public MessageSend(SalesView salesView, TNHeader parent, Integer contractorCode, 
            String file, String docName, String message, String sendMessage1, String sendMessage2) {
        initComponents();

        setLocation((salesView.getFrame().getWidth() - getWidth()) / 2,
                (salesView.getFrame().getHeight() - getHeight()) / 2);

        this.salesView = salesView;
        this.file = file;
        this.docName = docName;
        this.sendMessage1 = sendMessage1;
        this.sendMessage2 = sendMessage2;
        this.parent = parent;
        Session session = HUtil.getSession();
        Contractors c = (Contractors) HUtil.getElement("Contractors", contractorCode, session);
        session.close();
        if (c != null) {
            jtfAddress.setText(c.getEmail());
        }
        jtaMessage.setText(message);

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
        jtfAddress = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jtaMessage = new javax.swing.JTextArea();
        jbSend = new javax.swing.JButton();
        jbCancel = new javax.swing.JButton();

        setClosable(true);
        setMaximizable(true);
        setResizable(true);
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(sales.SalesApp.class).getContext().getResourceMap(MessageSend.class);
        setTitle(resourceMap.getString("Form.title")); // NOI18N
        setName("Form"); // NOI18N

        jLabel1.setText(resourceMap.getString("jLabel1.text")); // NOI18N
        jLabel1.setName("jLabel1"); // NOI18N

        jtfAddress.setText(resourceMap.getString("jtfAddress.text")); // NOI18N
        jtfAddress.setName("jtfAddress"); // NOI18N

        jLabel2.setText(resourceMap.getString("jLabel2.text")); // NOI18N
        jLabel2.setName("jLabel2"); // NOI18N

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        jtaMessage.setColumns(20);
        jtaMessage.setFont(resourceMap.getFont("jtaMessage.font")); // NOI18N
        jtaMessage.setRows(5);
        jtaMessage.setName("jtaMessage"); // NOI18N
        jScrollPane1.setViewportView(jtaMessage);

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(sales.SalesApp.class).getContext().getActionMap(MessageSend.class, this);
        jbSend.setAction(actionMap.get("send")); // NOI18N
        jbSend.setText(resourceMap.getString("jbSend.text")); // NOI18N
        jbSend.setName("jbSend"); // NOI18N

        jbCancel.setAction(actionMap.get("cancel")); // NOI18N
        jbCancel.setText(resourceMap.getString("jbCancel.text")); // NOI18N
        jbCancel.setName("jbCancel"); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 374, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jtfAddress, javax.swing.GroupLayout.DEFAULT_SIZE, 339, Short.MAX_VALUE))
                    .addComponent(jLabel2)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jbSend)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jbCancel)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jtfAddress, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 251, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jbCancel)
                    .addComponent(jbSend))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    @Action
    public void cancel() {
        dispose();
    }

    @Action
    public void send() {
        
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

        try {
            Properties props = new Properties();
            props.put("mail.transport.protocol", "smtp");
            props.put("mail.smtp.host", HUtil.getConstant("smtp"));
            props.put("mail.smtp.port", HUtil.getConstant("smtpPort"));
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.user", HUtil.getConstant("smtpLogin"));
            props.put("mail.smtp.password", HUtil.getConstant("smtpPassword"));
            props.put("mail.password", HUtil.getConstant("smtpPassword"));
            DefaultAuthenticator a =
                    new DefaultAuthenticator(HUtil.getConstant("smtpLogin"), HUtil.getConstant("smtpPassword"));
            javax.mail.Session s = javax.mail.Session.getDefaultInstance(props, a);
            Message msg = new MimeMessage(s);
            InternetAddress addressFrom = new InternetAddress(HUtil.getConstant("email"));
            msg.setFrom(addressFrom);
            InternetAddress addressTo = new InternetAddress(jtfAddress.getText());
            msg.setRecipient(Message.RecipientType.TO, addressTo);
            msg.setSubject(MimeUtility.encodeText(docName, "UTF-8", "B"));
            MimeBodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setText(jtaMessage.getText(), "UTF-8");
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);
            messageBodyPart = new MimeBodyPart();
            DataSource source = new FileDataSource(file);
            messageBodyPart.setDataHandler(new DataHandler(source));
            messageBodyPart.setFileName(source.getName());
            multipart.addBodyPart(messageBodyPart);
            msg.setContent(multipart, "multipart/mixed");
            Transport tr = s.getTransport("smtp");
            tr.connect(
                    HUtil.getConstant("smtp"),
                    Util.str2int(HUtil.getConstant("smtpPort")),
                    HUtil.getConstant("smtpUser"),
                    HUtil.getConstant("smtpPassword"));
            tr.send(msg, msg.getRecipients(Message.RecipientType.TO));
            tr.close();
            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            JOptionPane.showMessageDialog(
                    this,
                    sendMessage1 + " по адресу " + jtfAddress.getText() + sendMessage2,
                    "Отправка e-mail", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            logger.error(e);
            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            JOptionPane.showMessageDialog(
                    this,
                    "Ошибка! Отправка по адресу " + jtfAddress.getText() + " не удалась.",
                    "Отправка e-mail", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        Util.closeJIF(this, parent, salesView);
        Util.closeJIFTab(this, salesView);

    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton jbCancel;
    private javax.swing.JButton jbSend;
    private javax.swing.JTextArea jtaMessage;
    private javax.swing.JTextField jtfAddress;
    // End of variables declaration//GEN-END:variables
    private TNHeader parent;
    private SalesView salesView;
    private String file;
    private String docName;
    private String sendMessage1;
    private String sendMessage2;
    
    public void close() {
    }
}
