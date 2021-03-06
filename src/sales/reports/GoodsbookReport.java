/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * BalanceReport.java
 *
 * Created on 02.06.2011, 10:08:33
 */
package sales.reports;

import com.toedter.calendar.JDateChooser;
import java.io.File;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.swing.GroupLayout;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;
import org.apache.log4j.Logger;
import org.jdesktop.application.Action;
import org.jopendocument.dom.spreadsheet.Sheet;
import org.jopendocument.dom.spreadsheet.SpreadSheet;
import sales.SalesApp;
import sales.SalesView;
import sales.interfaces.IClose;
import sales.util.HUtil;
import sales.util.Util;

/**
 *
 * @author Администратор
 */
public class GoodsbookReport extends javax.swing.JInternalFrame implements IClose {
    
    static Logger logger = Logger.getLogger(SalesApp.class.getName());
    private GoodsbookReportTask goodsbookReportTask;
    boolean goodsbookReportRun;

    /** Creates new form BalanceReport */
    public GoodsbookReport(SalesView salesView) {
        
        initComponents();
        
        Util.initJIF(this, "Остатки и обороты по партиям", null, salesView);
        this.salesView = salesView;

        //Fill intial and final dates
        Calendar c = Calendar.getInstance();
        if (c.get(Calendar.MONTH) < 3) {
            c.set(Calendar.MONTH, 0);
            c.set(Calendar.DATE, 1);
            date1 = new JDateChooser(c.getTime());
            c.set(Calendar.MONTH, 2);
            c.set(Calendar.DATE, c.getActualMaximum(Calendar.DATE));
            date2 = new JDateChooser(c.getTime());
        } else if (c.get(Calendar.MONTH) < 6) {
            c.set(Calendar.MONTH, 3);
            c.set(Calendar.DATE, 1);
            date1 = new JDateChooser(c.getTime());
            c.set(Calendar.MONTH, 5);
            c.set(Calendar.DATE, c.getActualMaximum(Calendar.DATE));
            date2 = new JDateChooser(c.getTime());
        } else if (c.get(Calendar.MONTH) < 9) {
            c.set(Calendar.MONTH, 6);
            c.set(Calendar.DATE, 1);
            date1 = new JDateChooser(c.getTime());
            c.set(Calendar.MONTH, 8);
            c.set(Calendar.DATE, c.getActualMaximum(Calendar.DATE));
            date2 = new JDateChooser(c.getTime());
        } else {
            c.set(Calendar.MONTH, 9);
            c.set(Calendar.DATE, 1);
            date1 = new JDateChooser(c.getTime());
            c.set(Calendar.MONTH, 11);
            c.set(Calendar.DATE, c.getActualMaximum(Calendar.DATE));
            date2 = new JDateChooser(c.getTime());
        }
        
        GroupLayout gl = (GroupLayout) jpStartDate.getLayout();
        gl.setHorizontalGroup(gl.createParallelGroup().addGroup(gl.createSequentialGroup().addComponent(date1)));
        gl.setVerticalGroup(gl.createParallelGroup().addGroup(gl.createSequentialGroup().addComponent(date1)));
        
        date2 = new JDateChooser(c.getTime());
        gl = (GroupLayout) jpEndDate.getLayout();
        gl.setHorizontalGroup(gl.createParallelGroup().addGroup(gl.createSequentialGroup().addComponent(date2)));
        gl.setVerticalGroup(gl.createParallelGroup().addGroup(gl.createSequentialGroup().addComponent(date2)));
        
        goodsbookReportRun = false;
        
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jbExe = new javax.swing.JButton();
        jbClose = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jpStartDate = new javax.swing.JPanel();
        jpEndDate = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jlMsgs = new javax.swing.JLabel();

        setClosable(true);
        setMaximizable(true);
        setResizable(true);
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(sales.SalesApp.class).getContext().getResourceMap(GoodsbookReport.class);
        setTitle(resourceMap.getString("Form.title")); // NOI18N
        setName("Form"); // NOI18N

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(sales.SalesApp.class).getContext().getActionMap(GoodsbookReport.class, this);
        jbExe.setAction(actionMap.get("goodsbookReport")); // NOI18N
        jbExe.setText(resourceMap.getString("jbExe.text")); // NOI18N
        jbExe.setName("jbExe"); // NOI18N

        jbClose.setAction(actionMap.get("closeReport")); // NOI18N
        jbClose.setText(resourceMap.getString("jbClose.text")); // NOI18N
        jbClose.setName("jbClose"); // NOI18N

        jLabel1.setText(resourceMap.getString("jLabel1.text")); // NOI18N
        jLabel1.setName("jLabel1"); // NOI18N

        jpStartDate.setName("jpStartDate"); // NOI18N

        javax.swing.GroupLayout jpStartDateLayout = new javax.swing.GroupLayout(jpStartDate);
        jpStartDate.setLayout(jpStartDateLayout);
        jpStartDateLayout.setHorizontalGroup(
            jpStartDateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 93, Short.MAX_VALUE)
        );
        jpStartDateLayout.setVerticalGroup(
            jpStartDateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 21, Short.MAX_VALUE)
        );

        jpEndDate.setName("jpEndDate"); // NOI18N

        javax.swing.GroupLayout jpEndDateLayout = new javax.swing.GroupLayout(jpEndDate);
        jpEndDate.setLayout(jpEndDateLayout);
        jpEndDateLayout.setHorizontalGroup(
            jpEndDateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 102, Short.MAX_VALUE)
        );
        jpEndDateLayout.setVerticalGroup(
            jpEndDateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 21, Short.MAX_VALUE)
        );

        jLabel2.setText(resourceMap.getString("jLabel2.text")); // NOI18N
        jLabel2.setName("jLabel2"); // NOI18N

        jlMsgs.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlMsgs.setText(resourceMap.getString("jlMsgs.text")); // NOI18N
        jlMsgs.setName("jlMsgs"); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jbExe, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jbClose, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jlMsgs, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 232, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jpStartDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jpEndDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(28, 28, 28))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jpStartDate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jpEndDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jlMsgs, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jbClose)
                    .addComponent(jbExe))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private class GoodsbookReportTask extends SwingWorker<Void, String> {
        
        private final SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yy");
        private final SimpleDateFormat sdfd = new SimpleDateFormat("yyyy-MM-dd");
        
        @Override
        protected Void doInBackground() {
            
            try {

                //Template
                File file = new File(Util.getAppPath() + "\\templates\\ShipmentSalesReport.ods");
                final Sheet sheet = SpreadSheet.createFromFile(file).getSheet(0);
                
                String d1 = sdfd.format(date1.getDate());
                String d2 = sdfd.format(date2.getDate());
                
                sheet.getCellAt("A1").setValue(
                        "Остатки и обороты по партиям c " + sdf.format(date1.getDate()) + " по " + sdf.format(date2.getDate()));
                
                publish("Запрос в базу данных");
                
                String sql = "select count(n.code) from Nomenclature as n where n.active != 2";
                Statement stmt = HUtil.getConnection().createStatement();
                ResultSet rs = stmt.executeQuery(sql);
                int nomNum = 0;
                if (rs.next()) {
                    nomNum = rs.getInt(1);
                }
                
                sql = "select n.code, n.name from Nomenclature as n where n.active != 2";
                rs = stmt.executeQuery(sql);
                
                if (isCancelled()) {
                    cancel();
                    return null;
                }
                
                ArrayList tbln = new ArrayList();
                
                int ns = 0;
                int num = 0;
                while (rs.next()) {
                    
                    boolean nl = true;
                    
                    int ncode = rs.getInt(1);
                    sql = "select i.code, i.type, i.number, i.datetime,"
                            + "(select sum(it.quantity)"
                            + " from Incomingtable as it inner join Incoming as i on it.documentCode = i.code"
                            + " where it.productCode = " + ncode
                            + " and it.documentCode = i.code"
                            + " and date(i.datetime) < '" + d2 + "'"
                            + " and i.active = 1),"
                            + "(select sum(ot.quantity)"
                            + " from Outcomingtable as ot inner join Outcoming as o on ot.documentCode = o.code"
                            + " where ot.productCode = " + ncode
                            + " and date(o.datetime) < '" + d1 + "'"
                            + " and o.active = 1)"
                            + " from Incomingtable as it inner join Incoming as i"
                            + " on it.documentCode = i.code"
                            + " where it.productCode = " + ncode
                            + " and date(i.datetime) <= '" + d2 + "'"
                            + " and i.active = 1"
                            + " group by i.code"
                            + " order by i.datetime";
                    
                    Statement stmti = HUtil.getConnection().createStatement();
                    ResultSet rsi = stmti.executeQuery(sql);
                    
                    if (isCancelled()) {
                        cancel();
                        return null;
                    }
                    
                    ArrayList tbli = new ArrayList();
                    
                    while (rsi.next()) {
                        
                        boolean nli = true;
                        int icode = rsi.getInt(1);
                        sql = "select o.code, o.documentType, o.number, o.datetime,"
                                + "(select sum(ot.quantity)"
                                + " from Outcomingtable ot"
                                + " where ot.productCode = " + ncode
                                + " and ot.documentCode = o.code),"
                                + "(select sum(ot.amount)"
                                + " from Outcomingtable ot"
                                + " where ot.productCode = " + ncode
                                + " and ot.documentCode = o.code)"
                                + " from Outcomingtable as ot inner join Outcoming as o"
                                + " on ot.documentCode = o.code"
                                + " where ot.productCode = " + ncode
                                + " and ot.incomingCode = " + icode
                                + " and date(o.datetime) >= '" + d1 + "'"
                                + " and date(o.datetime) <= '" + d2 + "'"
                                + " and o.active = 1"
                                + " group by o.code"
                                + " order by o.datetime";
                        
                        Statement stmto = HUtil.getConnection().createStatement();
                        ResultSet rso = stmto.executeQuery(sql);
                        
                        if (isCancelled()) {
                            cancel();
                            return null;
                        }
                        
                        ArrayList tblo = new ArrayList();
                        
                        while (rso.next()) {
                            
                            HashMap rowo = new HashMap();
                            rowo.put("type", rso.getInt(2));
                            rowo.put("number", rso.getString(3));
                            rowo.put("datetime", rso.getDate(4));
                            rowo.put("quantity", rso.getInt(5));
                            rowo.put("amount", rso.getInt(6));
                            tblo.add(rowo);
                            ns++;
                            nli = false;
                            nl = false;
                        }
                        
                        int q = rsi.getInt(5) - rsi.getInt(6);
                        if (q != 0 || tblo.size() > 0) {
                            HashMap rowi = new HashMap();
                            rowi.put("type", rsi.getInt(2));
                            rowi.put("number", rsi.getString(3));
                            rowi.put("datetime", rsi.getDate(4));
                            rowi.put("quantity", q);
                            rowi.put("amount", q * HUtil.getPriceOnDatePriceOnly(ncode, rsi.getTimestamp(4)));
                            rowi.put("tblo", tblo);
                            tbli.add(rowi);
                            if (nli) {
                                ns++;
                                nl = false;
                            }
                        }
                    }
                    
                    sql = "select o.code, o.documentType, o.number, o.datetime,"
                            + "(select sum(ot.quantity)"
                            + " from Outcomingtable ot"
                            + " where ot.productCode = " + ncode
                            + " and ot.documentCode = o.code),"
                            + "(select sum(ot.amount)"
                            + " from Outcomingtable ot"
                            + " where ot.productCode = " + ncode
                            + " and ot.documentCode = o.code)"
                            + " from Outcomingtable as ot inner join Outcoming as o"
                            + " on ot.documentCode = o.code"
                            + " where ot.productCode = " + ncode
                            + " and ot.incomingCode is null"
                            + " and date(o.datetime) >= '" + d1 + "'"
                            + " and date(o.datetime) <= '" + d2 + "'"
                            + " and o.active = 1"
                            + " group by o.code"
                            + " order by o.datetime";
                    
                    Statement stmto = HUtil.getConnection().createStatement();
                    ResultSet rso = stmto.executeQuery(sql);
                    
                    if (isCancelled()) {
                        cancel();
                        return null;
                    }
                    
                    ArrayList tblo = new ArrayList();
                    
                    while (rso.next()) {
                        
                        HashMap rowo = new HashMap();
                        rowo.put("type", rso.getInt(2));
                        rowo.put("number", rso.getString(3));
                        rowo.put("datetime", rso.getDate(4));
                        rowo.put("quantity", rso.getInt(5));
                        rowo.put("amount", rso.getInt(6));
                        tblo.add(rowo);
                        ns++;
                        nl = false;
                    }
                    
                    if (tbli.size() > 0 || tblo.size() > 0) {
                        HashMap rown = new HashMap();
                        rown.put("code", rs.getInt(1));
                        rown.put("name", rs.getString(2));
                        rown.put("tbli", tbli);
                        rown.put("tblo", tblo);
                        tbln.add(rown);
                        if (nl) {
                            ns++;
                        }
                    }
                    
                    if (nomNum != 0) {
                        publish(
                                "Запрос в базу данных, обработано " + (new DecimalFormat("#")).format(((double) num) * 100 / ((double) nomNum)) + "%");
                        num++;
                    }
                    
                }
                
                if (ns > 1) {
                    sheet.duplicateRows(4, 1, ns - 1);
                }
                
                publish("Создание отчета");
                long amount = 0;
                long amountOut = 0;
                int cs = 0;
                int l = 5;
                for (int i = 0; i < tbln.size(); i++) {
                    
                    HashMap rown = (HashMap) tbln.get(i);
                    int ncode = (int) rown.get("code");
                    ArrayList tbli = (ArrayList) rown.get("tbli");
                    ArrayList tblo = (ArrayList) rown.get("tblo");
                    
                    if (tbli.size() > 0 || tblo.size() > 0) {
                        
                        if (tbli.size() > 0) {
                            
                            Util.setCell("A", rown.get("code"), l, sheet);
                            Util.setCell("B", rown.get("name"), l, sheet);
                            
                            for (int j = 0; j < tbli.size(); j++) {
                                
                                HashMap rowi = (HashMap) tbli.get(j);
                                if (rowi.get("type") == 0) {
                                    Util.setCell("C", "Приход № " + rowi.get("number") + " от " + sdf.format((Date) rowi.get("datetime")), l, sheet);
                                } else if (rowi.get("type") == 1) {
                                    Util.setCell("C", "Приход (инвентаризация) № " + rowi.get("number") + " от " + sdf.format((Date) rowi.get("datetime")), l, sheet);
                                } else if (rowi.get("type") == 2) {
                                    Util.setCell("C", "Ввод остатков" + " от " + sdf.format((Date) rowi.get("datetime")), l, sheet);
                                }
                                
                                if (rowi.get("quantity") != 0 || rowi.get("amount") != 0) {
                                    Util.setCell("D", rowi.get("quantity"), l, sheet);
                                    Util.setCell("E", rowi.get("amount"), l, sheet);
                                    amount += (int) rowi.get("amount");
                                }
                                
                                long qOut = 0;
                                long aOut = 0;
                                ArrayList tblio = (ArrayList) rowi.get("tblo");
                                if (tblio.size() > 0) {
                                    for (int k = 0; k < tblio.size(); k++) {
                                        
                                        HashMap rowio = (HashMap) tblio.get(k);
                                        fillOutcomingDoc(rowio, l, sheet);
                                        qOut += (int) rowio.get("quantity");
                                        aOut += (int) rowio.get("amount");
                                        if (k == (tblio.size() - 1)) {
                                            long q = (int) rowi.get("quantity") - qOut;
                                            long a = ((int) rowi.get("quantity") - qOut) * HUtil.getPriceOnDatePriceOnly(ncode, date2.getDate());
                                            if (q != 0 || a != 0) {
                                                Util.setCell("I", q, l, sheet);
                                                Util.setCell("J", a, l, sheet);
                                            }
                                        }
                                        l++;
                                    }
                                    amountOut += aOut;
                                } else {
                                    long q = (int) rowi.get("quantity");
                                    long a = (int) rowi.get("amount");
                                    if (q != 0 || a != 0) {
                                        Util.setCell("I", q, l, sheet);
                                        Util.setCell("J", a, l, sheet);
                                    }
                                    l++;
                                }
                                
                            }
                            
                        }
                        
                        if (tblo.size() > 0) {
                            
                            Util.setCell("C", "Не указана", l, sheet);
                            for (int j = 0; j < tblo.size(); j++) {
                                
                                HashMap rowo = (HashMap) tblo.get(j);
                                fillOutcomingDoc(rowo, l, sheet);
                                amountOut += (int) rowo.get("amount");
                                l++;
                            }
                        }
                        
                    } else {
                        l++;
                    }
                    
                    publish("Создание отчета, выведено " + (new DecimalFormat("#")).format(((double) cs) * 100 / ((double) ns)) + "%");
                }
                
                Util.setCell("E", amount, l, sheet);
                Util.setCell("H", amountOut, l, sheet);
                Util.setCell("J", amount - amountOut, l, sheet);
                
                String r = " " + Math.round(Math.random() * 100000);
                File outputFile = new File(
                        "temp\\Остатки и обороты с " + d1 + " по " + d2
                        + r + ".ods");
                sheet.getSpreadSheet().saveAs(outputFile);
                Util.openDoc(
                        "temp\\Остатки и обороты с " + d1 + " по " + d2
                        + r + ".ods");
                
            } catch (Exception e) {
                logger.error(e);
            }
            
            return null;
        }
        
        @Override
        protected void process(List<String> msgs) {
            for (int i = 0; i < msgs.size(); i++) {
                jlMsgs.setText(msgs.get(i));
            }
        }
        
        @Override
        protected void done() {
            goodsbookReportRun = false;
            jlMsgs.setText("Выполнен");
            jbExe.setText("Сформировать");
            jbClose.setEnabled(true);
        }
        
        protected void cancel() {
            goodsbookReportRun = false;
            jlMsgs.setText("Остановлен");
            jbExe.setText("Сформировать");
            jbClose.setEnabled(true);
        }
        
        private void fillOutcomingDoc(HashMap rowo, int l, Sheet sheet) {
            
            if (rowo.get("type") == 0) {
                Util.setCell("F", "ТН № " + rowo.get("number") + " от " + sdf.format((Date) rowo.get("datetime")), l, sheet);
            } else if (rowo.get("type") == 6) {
                Util.setCell("F", "ТТН № " + rowo.get("number") + " от " + sdf.format((Date) rowo.get("datetime")), l, sheet);
            } else if (rowo.get("type") == 3) {
                Util.setCell("F", "Расход (инвентаризация) № " + rowo.get("number") + " от " + sdf.format((Date) rowo.get("datetime")), l, sheet);
            } else if (rowo.get("type") == 2 || rowo.get("type") == 4) {
                Util.setCell("F", "Касса № " + rowo.get("number") + " от " + sdf.format((Date) rowo.get("datetime")), l, sheet);
            } else if (rowo.get("type") == 1 || rowo.get("type") == 5) {
                Util.setCell("F", "Терминал № " + rowo.get("number") + " от " + sdf.format((Date) rowo.get("datetime")), l, sheet);
            }
            
            Util.setCell("G", rowo.get("quantity"), l, sheet);
            Util.setCell("H", rowo.get("amount"), l, sheet);
        }
    }

    //Create report
    @Action
    public void goodsbookReport() {
        
        if (goodsbookReportRun) {
            int a = JOptionPane.showOptionDialog(
                    this, "Остановить создание отчета?",
                    "Остатки и обороты по партиям", JOptionPane.YES_NO_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE, null, new Object[]{"Да", "Нет"}, "Да");
            if (a == 0) {
                if (goodsbookReportTask.cancel(true)) {
                    goodsbookReportRun = false;
                    jlMsgs.setText("");
                    jbExe.setText("Сформировать");
                    jbClose.setEnabled(true);
                } else {
                    JOptionPane.showMessageDialog(
                            this, "Невозможно прервать создание отчета!", "Остатки и обороты по партиям", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            goodsbookReportTask = new GoodsbookReportTask();
            goodsbookReportTask.execute();
            goodsbookReportRun = true;
            jbExe.setText("Отмена");
            jbClose.setEnabled(false);
        }
        
    }
    
    public void close() {
    }
    
    @Action
    public void closeReport() {
        Util.closeJIF(this, null, salesView);
        Util.closeJIFTab(this, salesView);
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JButton jbClose;
    private javax.swing.JButton jbExe;
    private javax.swing.JLabel jlMsgs;
    private javax.swing.JPanel jpEndDate;
    private javax.swing.JPanel jpStartDate;
    // End of variables declaration//GEN-END:variables
    private JDateChooser date1;
    private JDateChooser date2;
    private SalesView salesView;
}
