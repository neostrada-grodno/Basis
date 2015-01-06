/*
 * FixDatabase.java
 *
 * Created on 13.08.2011, 12:45:40
 */
package sales;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;
import org.hibernate.classic.Session;
import org.jdesktop.application.Action;
import sales.entity.Constants;
import sales.entity.Nomenclature;
import sales.entity.Outcoming;
import sales.interfaces.IClose;
import sales.outcoming.OutcomingDocView;
import sales.util.HUtil;
import sales.util.Util;

public class FixDatabase extends javax.swing.JInternalFrame implements IClose {

//    CommitDocsTask commitDocsTask;
//    boolean commitDocsRun;
    RecomputePartiesTask recomputePartiesTask;
    boolean recomputePartiesRun;
    RecomputeBalanceTask recomputeBalanceTask;
    boolean recomputeBalanceRun;

    /** Creates new form FixDatabase */
    public FixDatabase(SalesView salesView) {

        initComponents();

        this.salesView = salesView;
        //commitDocsRun = false;
        recomputePartiesRun = false;
        recomputeBalanceRun = false;

        Util.initJIF(this, salesView, salesView);
        
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
        jbCommit = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jtaChecking = new javax.swing.JTextArea();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jtaFixing = new javax.swing.JTextArea();
        jButton1 = new javax.swing.JButton();
        jbRecomputeParties = new javax.swing.JButton();
        jbRecomputeBalance = new javax.swing.JButton();

        setClosable(true);
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(sales.SalesApp.class).getContext().getResourceMap(FixDatabase.class);
        setForeground(resourceMap.getColor("Form.foreground")); // NOI18N
        setMaximizable(true);
        setResizable(true);
        setTitle(resourceMap.getString("Form.title")); // NOI18N
        setName("Form"); // NOI18N

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(sales.SalesApp.class).getContext().getActionMap(FixDatabase.class, this);
        jbClose.setAction(actionMap.get("closeService")); // NOI18N
        jbClose.setText(resourceMap.getString("jbClose.text")); // NOI18N
        jbClose.setName("jbClose"); // NOI18N

        jbCommit.setAction(actionMap.get("check")); // NOI18N
        jbCommit.setText(resourceMap.getString("jbCommit.text")); // NOI18N
        jbCommit.setName("jbCommit"); // NOI18N

        jLabel1.setText(resourceMap.getString("jLabel1.text")); // NOI18N
        jLabel1.setName("jLabel1"); // NOI18N

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        jtaChecking.setColumns(20);
        jtaChecking.setFont(resourceMap.getFont("jtaChecking.font")); // NOI18N
        jtaChecking.setForeground(resourceMap.getColor("jtaChecking.foreground")); // NOI18N
        jtaChecking.setRows(5);
        jtaChecking.setName("jtaChecking"); // NOI18N
        jScrollPane1.setViewportView(jtaChecking);

        jLabel2.setText(resourceMap.getString("jLabel2.text")); // NOI18N
        jLabel2.setName("jLabel2"); // NOI18N

        jScrollPane2.setName("jspFixing"); // NOI18N

        jtaFixing.setColumns(20);
        jtaFixing.setFont(resourceMap.getFont("jtaFixing.font")); // NOI18N
        jtaFixing.setForeground(resourceMap.getColor("jtaFixing.foreground")); // NOI18N
        jtaFixing.setRows(5);
        jtaFixing.setName("jtaFixing"); // NOI18N
        jScrollPane2.setViewportView(jtaFixing);

        jButton1.setAction(actionMap.get("fix")); // NOI18N
        jButton1.setText(resourceMap.getString("jButton1.text")); // NOI18N
        jButton1.setName("jButton1"); // NOI18N

        jbRecomputeParties.setAction(actionMap.get("recomputeParties")); // NOI18N
        jbRecomputeParties.setText(resourceMap.getString("jbRecomputeParties.text")); // NOI18N
        jbRecomputeParties.setName("jbRecomputeParties"); // NOI18N

        jbRecomputeBalance.setAction(actionMap.get("recomputeBalance")); // NOI18N
        jbRecomputeBalance.setText(resourceMap.getString("jbRecomputeBalance.text")); // NOI18N
        jbRecomputeBalance.setName("jbRecomputeBalance"); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 586, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 586, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jbRecomputeParties)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jbRecomputeBalance)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 10, Short.MAX_VALUE)
                        .addComponent(jbClose))
                    .addComponent(jLabel1)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 350, Short.MAX_VALUE)
                        .addComponent(jbCommit, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 203, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel2)
                    .addComponent(jbCommit))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 179, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jbRecomputeParties)
                    .addComponent(jbRecomputeBalance)
                    .addComponent(jButton1)
                    .addComponent(jbClose))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JButton jbClose;
    private javax.swing.JButton jbCommit;
    private javax.swing.JButton jbRecomputeBalance;
    private javax.swing.JButton jbRecomputeParties;
    private javax.swing.JTextArea jtaChecking;
    private javax.swing.JTextArea jtaFixing;
    // End of variables declaration//GEN-END:variables
    private SalesView salesView;
    private boolean constsLocked;
    private List banks;
    private List contractors;
    private List suppliers;
    private List incomings;
    private List outcomings;
    private List inventories;
    private List paymentOrders;
    private List registers;
    private List repricings;
    private List salesReports;
    private List services;

    public void close() {
    }

    @Action
    public void closeService() {
        Util.closeJIF(this, null, salesView);
        Util.closeJIFTab(this, salesView);
    }

    private List checkObjects(String table, String tableName, String name, String objName, Session session) {
        jtaChecking.append("\nТаблица " + tableName);
        jtaChecking.append("\n---------------------------------------------------");
        List res = session.createSQLQuery("select x.code, x." + name + " from " + table + " x where x.locked = 1").list();
        for (int i = 0; i < res.size(); i++) {
            Object[] row = (Object[]) res.get(i);
            jtaChecking.append("\nЭлемент " + (objName.equals("") ? "" : objName + " ") + row[1] + " - заблокирован");
        }
        jtaChecking.append("\n---------------------------------------------------");
        return res;
    }

    @Action
    public void check() {
        Session session = HUtil.getSession();
        jtaChecking.setText("---------------------------------------------------");
        jtaChecking.append("\nПроверка блокированных элементов");
        jtaChecking.append("\n---------------------------------------------------");
        String lck = HUtil.getConstant("lock");
        constsLocked = !(lck == null || !lck.equals("locked"));
        jtaChecking.append("\nТаблица констант");
        if(constsLocked) {
            jtaChecking.append(" - заблокирована");
        }
        jtaChecking.append("\n---------------------------------------------------");
        banks = checkObjects("Banks", "банков", "name", "", session);
        contractors = checkObjects("Contractors", "покупателей", "name", "", session);
        suppliers = checkObjects("Suppliers", "поставщиков", "name", "", session);
        incomings = checkObjects("Incoming", "приходов", "number", "Приход № ", session);
        outcomings = checkObjects("Outcoming", "расходов", "number", "Расход № ", session);
        inventories = checkObjects("Inventory", "инвентаризаций", "number", "Инвентаризация № ", session);
        paymentOrders = checkObjects("Paymentorder", "платежных поручений", "number", "Платежное поручение № ", session);
        registers = checkObjects("Register", "кассовых ордеров", "number", "Кассовый ордер № ", session);
        repricings = checkObjects("Repricing", "переоценок", "number", "Переоценка № ", session);
        salesReports = checkObjects("Salesreport", "товарных отчетов", "number", "Товарный отчет № ", session);
        services = checkObjects("Service", "актов выполненных работ", "number", "Акт выполненных работ № ", session);
        session.close();
    }

    private void fixObjects(String table, String tableName, String objName, List objList, Session session) {
        jtaFixing.append("\n---------------------------------------------------");
        jtaFixing.append("\nТаблица " + tableName);
        jtaFixing.append("\n---------------------------------------------------");
        for (int i = 0; i < objList.size(); i++) {
            Object[] row = (Object[]) objList.get(i);
            session.createSQLQuery("UPDATE " + table + " x SET x.locked = 0 WHERE x.code = " + row[0]).executeUpdate();
            jtaFixing.append("\nЭлемент " + (objName.equals("") ? "" : objName + " ") + row[1] + " - разблокирован");
        }
    }

    @Action
    public void fix() {
        int a = JOptionPane.showOptionDialog(
                this, "Снять блокировки?\nУбедитесть что с базой не работают пользователи!", "Исправление", JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE, null, new Object[]{"Да", "Нет"}, "Да");
        if (a == 0) {
            Session session = HUtil.getSession();
            jtaFixing.setText("---------------------------------------------------");
            jtaFixing.append("\nРазблокирование блокированных элементов");
            jtaFixing.append("\n---------------------------------------------------");
            if(constsLocked) {
                jtaFixing.append("\nТаблица констант");
                ArrayList consts = new ArrayList();
                consts.add(new Constants("lock", "", ""));
                HUtil.setConstants(consts);
                jtaFixing.append(" - разблокирована");
                jtaFixing.append("\n---------------------------------------------------");
            }
            fixObjects("Banks", "банков", "", banks, session);
            fixObjects("Contractors", "покупателей", "", contractors, session);
            fixObjects("Suppliers", "поставщиков", "", suppliers, session);
            fixObjects("Incoming", "приходов", "Приход № ", incomings, session);
            fixObjects("Outcoming", "расходов", "Расход № ", outcomings, session);
            fixObjects("Inventory", "инвентаризаций", "Инвентаризация № ", inventories, session);
            fixObjects("Paymentorder", "платежных поручений", "Платежное поручение № ", paymentOrders, session);
            fixObjects("Register", "кассовых ордеров", "Кассовый ордер № ", registers, session);
            fixObjects("Repricing", "переоценок", "Переоценка № ", repricings, session);
            fixObjects("Salesreport", "товарных отчетов", "Товарный отчет № ", salesReports, session);
            fixObjects("Service", "актов выполненных работ", "Акт выполненных работ № ", services, session);
            session.close();
        }
    }

    private class RecomputePartiesTask extends SwingWorker<Void, String> {

        Session session;

        @Override
        protected Void doInBackground() {

            jbRecomputeParties.setEnabled(false);

            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yy");

            publish("---------------------------------------------------\n");
            publish("Перерасчет партий\n");
            publish("---------------------------------------------------\n");

            session = HUtil.getSession();

            session.createSQLQuery("UPDATE Outcoming AS x SET x.locked = 1").executeUpdate();

            List outs = HUtil.executeHql("from Outcoming x where x.active = 1 order by x.datetime", session);
            for (int i = 0; i < outs.size(); i++) {
                Outcoming o = (Outcoming) outs.get(i);
                publish("Идет перерасчет партий для расхода № " + o.getNumber() + " от " + sdf.format(o.getDatetime()) + "...");
                OutcomingDocView x = new OutcomingDocView(salesView, null, o.getCode(), o.getDocumentType(), true, null);
                x.fillShipmentsAll();
                x.save();
                x.dispose();
                publish(" Выполнен\n");
                if(isCancelled()) {
                    publish(" Прервано пользователем!\n");
                    return null;
                }
            }

            return null;
        }

        @Override
        protected void process(List<String> msgs) {
            for (int i = 0; i < msgs.size(); i++) {
                jtaFixing.append(msgs.get(i));
            }
        }

        @Override
        protected void done() {

            session.createSQLQuery("UPDATE Outcoming AS x SET x.locked = 0").executeUpdate();

            JOptionPane.showMessageDialog(null, "Перерасчет партий завершен!", "Перерасчет партий", JOptionPane.PLAIN_MESSAGE);

            session.close();

            jbRecomputeParties.setText("Перерасчитать партии");
            jbRecomputeParties.setEnabled(true);

        }
    }

    @Action
    public void recomputeParties() {

        if (recomputePartiesRun) {
            int a = JOptionPane.showOptionDialog(
                    this, "Остановить перерасчет партий документов?",
                    "Перерасчет партий", JOptionPane.YES_NO_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE, null, new Object[]{"Да", "Нет"}, "Да");
            if (a == 0) {
                recomputePartiesTask.cancel(true);
                recomputePartiesRun = false;
                jbRecomputeParties.setText("Перерасчитать партии");
            }
        } else {
            Session session = HUtil.getSession();
            if (HUtil.lockedDocs(new String[]{"Outcoming"}, session)) {
                JOptionPane.showMessageDialog(this, "В базе имеются заблокированные элементы!", "Перерасчет партий", JOptionPane.ERROR_MESSAGE);
            } else {
                int a = JOptionPane.showOptionDialog(
                        this, "Перерасчитать партии?", "Партии", JOptionPane.YES_NO_CANCEL_OPTION,
                        JOptionPane.QUESTION_MESSAGE, null, new Object[]{"Да", "Нет"}, "Да");
                if (a == 0) {
                    jtaFixing.setText("");
                    recomputePartiesTask = new RecomputePartiesTask();
                    recomputePartiesTask.execute();
                    recomputePartiesRun = true;
                    jbRecomputeParties.setText("Остановить перерасчет партий");
                }
            }
            session.close();
        }
    }

    private class RecomputeBalanceTask extends SwingWorker<Void, String> {
        
        Session session;
        int num = 0;

        @Override
        protected Void doInBackground() {

            jbRecomputeBalance.setEnabled(false);

            publish("Производится пересчет остатков по номенклатуре!\nПожалуйста подождите...\n");

            Calendar c = Calendar.getInstance();

            session = HUtil.getSession();

            List balance = HUtil.getBalance("code", c.getTime());

            session.beginTransaction();
            num = 100;
            int i;
            for (i = 0; i < balance.size(); i++) {
                
                Object[] row = (Object[]) balance.get(i);
                Nomenclature n = (Nomenclature) HUtil.getElement("Nomenclature", Util.getIntObj(row[0]));
                n.setQuantity(Util.getIntObj(row[1]) - Util.getIntObj(row[2]));
                session.update(n);
                if (i == num) {                
                    session.getTransaction().commit();
                    publish("Рассчитано " + i + " элементов...\n");
                    num += 100;
                    session.beginTransaction();
                }
                if(isCancelled()) {
                    session.getTransaction().commit();
                    publish(" Прервано пользователем!\n");
                    return null;
                }
            }
            session.getTransaction().commit();
            num = i;

            return null;
        }

        @Override
        protected void process(List<String> msgs) {
            for (int i = 0; i < msgs.size(); i++) {
                jtaFixing.append(msgs.get(i));
            }
        }

        @Override
        protected void done() {

            session.close();
            
            publish("Пересчет окончен! Рассчитано " + num + " элементов.");

            JOptionPane.showMessageDialog(null, "Перерасчет остатков завершен!", "Перерасчет партий", JOptionPane.PLAIN_MESSAGE);

            jbRecomputeBalance.setText("Перерасчитать остатки в номенклатуре");
            jbRecomputeBalance.setEnabled(true);
        }
    }

    @Action
    public void recomputeBalance() {
        
        if (recomputePartiesRun) {
            int a = JOptionPane.showOptionDialog(
                    this, "Остановить перерасчет остатков в номенклатуре?",
                    "Перерасчет остатков в номенклатуре", JOptionPane.YES_NO_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE, null, new Object[]{"Да", "Нет"}, "Да");
            if (a == 0) {
                recomputeBalanceTask.cancel(true);
                recomputeBalanceRun = false;
                jbRecomputeBalance.setText("Перерасчитать остатки в номенклатуре");
            }
        } else {
            int a = JOptionPane.showOptionDialog(
                    this, "Перерасчитать остатки в номенклатуре?", "Перерасчет остатков", JOptionPane.YES_NO_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE, null, new Object[]{"Да", "Нет"}, "Да");
            if (a == 0) {
                jtaFixing.setText("");
                recomputeBalanceTask = new RecomputeBalanceTask();
                recomputeBalanceTask.execute();
                recomputeBalanceRun = true;
                jbRecomputeBalance.setText("Остановить перерасчет остатков");
            }
        }
    }
}