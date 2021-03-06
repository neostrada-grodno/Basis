package sales.modules;

import java.io.File;
import java.util.List;
import java.util.Vector;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.apache.log4j.Logger;
import org.hibernate.classic.Session;
import org.jdesktop.application.Action;
import org.jopendocument.util.FileUtils;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import sales.auxiliarly.AppStartUpPath;
import sales.SalesApp;
import sales.SalesView;
import sales.entity.Modules;
import sales.interfaces.IClose;
import sales.util.HUtil;
import sales.util.Util;

public class ModulesView extends javax.swing.JInternalFrame implements IClose {

    static Logger logger = Logger.getLogger(SalesApp.class.getName());

    /** Creates new form Modules */
    public ModulesView(SalesView salesView) {
        initComponents();

        this.salesView = salesView;

        setLocation(((salesView.getFrame().getWidth()) - getWidth()) / 2, ((salesView.getFrame().getHeight()) - getHeight()) / 2);

        showTable();
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
        jtModules = new javax.swing.JTable();
        jbClose = new javax.swing.JButton();
        jbRun = new javax.swing.JButton();
        jbInstall = new javax.swing.JButton();
        jbUninstall = new javax.swing.JButton();

        setClosable(true);
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(sales.SalesApp.class).getContext().getResourceMap(ModulesView.class);
        setTitle(resourceMap.getString("Form.title")); // NOI18N
        setName("Form"); // NOI18N

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        jtModules.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jtModules.setName("jtModules"); // NOI18N
        jtModules.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jtModulesMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jtModules);

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(sales.SalesApp.class).getContext().getActionMap(ModulesView.class, this);
        jbClose.setAction(actionMap.get("close")); // NOI18N
        jbClose.setText(resourceMap.getString("jbClose.text")); // NOI18N
        jbClose.setName("jbClose"); // NOI18N

        jbRun.setText(resourceMap.getString("jbRun.text")); // NOI18N
        jbRun.setName("jbRun"); // NOI18N

        jbInstall.setAction(actionMap.get("install")); // NOI18N
        jbInstall.setText(resourceMap.getString("jbInstall.text")); // NOI18N
        jbInstall.setName("jbInstall"); // NOI18N

        jbUninstall.setAction(actionMap.get("uninstall")); // NOI18N
        jbUninstall.setText(resourceMap.getString("jbUninstall.text")); // NOI18N
        jbUninstall.setName("jbUninstall"); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 412, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jbRun)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jbInstall)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jbUninstall, javax.swing.GroupLayout.DEFAULT_SIZE, 83, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(jbClose)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 320, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jbClose)
                    .addComponent(jbRun)
                    .addComponent(jbInstall)
                    .addComponent(jbUninstall, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jtModulesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtModulesMouseClicked
        if (evt.getClickCount() > 1) {
            openModule(jtModules.getSelectedRow());
        }

    }//GEN-LAST:event_jtModulesMouseClicked
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton jbClose;
    private javax.swing.JButton jbInstall;
    private javax.swing.JButton jbRun;
    private javax.swing.JButton jbUninstall;
    private javax.swing.JTable jtModules;
    // End of variables declaration//GEN-END:variables
    private List table;
    private SalesView salesView;

    private class MyTableModel extends DefaultTableModel {

        public MyTableModel(Vector table, Vector header) {
            super(table, header);
        }

        @Override
        public boolean isCellEditable(int r, int c) {
            return false;
        }
    }

    public void showTable() {

        Vector<String> tableHeaders = new Vector<String>();
        Vector tableData = new Vector();
        tableHeaders.add("Наименование");

        table = HUtil.executeHql("from Modules");

        for (int i = 0; i < table.size(); i++) {
            Vector<String> oneRow = new Vector<String>();
            oneRow.add(((Modules) table.get(i)).getName());
            tableData.add(oneRow);
        }

        jtModules.setModel(new MyTableModel(tableData, tableHeaders));
    }

    public void openModule(int row) {

        try {

            Session session = HUtil.getSession();
            Modules m = (Modules) HUtil.getElement("Modules", ((Modules) table.get(row)).getCode(), session);
            String res = Util.resModule(m.getJarVersion());
            if (m.getResult() == null || !res.equals(m.getResult())) {
                String a = JOptionPane.showInputDialog(this, "Введите код активации модуля", "Активация", JOptionPane.QUESTION_MESSAGE);
                if (a.equals(res)) {
                    session.beginTransaction();
                    m.setResult(res);
                    session.merge(m);
                    session.getTransaction().commit();
                    JOptionPane.showMessageDialog(this, "Спасибо за регистрацию модуля!");
                } else {
                    JOptionPane.showMessageDialog(this, "Неверный код активации", "Ошибка активации", JOptionPane.ERROR_MESSAGE);
                }
            }
            session.close();

            String modulesPath = Util.getAppPath() + "\\modules\\";

            Runtime.getRuntime().exec(
                    "javaw -splash:\"" + modulesPath + m.getPath() + "\\loading.gif\""
                    + " -jar \"" + modulesPath + m.getPath() + "\\" + m.getJar() + "\"",
                    null, new File(modulesPath + m.getPath()));

        } catch (Exception e) {
            logger.error(e);
        }
    }

    @Action
    public void close() {
        Util.closeJIF(this, salesView, salesView);
        Util.closeJIFTab(this, salesView);
    }

    private void setHost(String salesPath, String modulesPath, String moduleFolder, String confFileName) {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(new File(salesPath + "\\sales-hibernate-configuration.cfg.xml"));
            doc.getDocumentElement().normalize();

            String host = Util.getConfItem("hibernate.connection.url", doc);
            
            host = host.substring(13);
            int p = host.indexOf(":");
            host = host.substring(0, p);
            
            p = confFileName.indexOf("-");
            String moduleHibernatePrefix = confFileName.substring(0, p);

            File confFile = new File(modulesPath + "\\" + moduleFolder + "\\" + confFileName);
            doc = db.parse(confFile);
            doc.getDocumentElement().normalize();

            boolean found = false;
            Node node = null;
            NodeList nodeList = doc.getElementsByTagName("property");
            for (int i = 0; i < nodeList.getLength(); i++) {
                node = nodeList.item(i);
                NamedNodeMap attrs = nodeList.item(i).getAttributes();
                if (attrs != null) {
                    Node attrNode = attrs.getNamedItem("name");
                    if (attrNode != null) {
                        if (attrNode.getTextContent().equals("hibernate.connection.url")) {
                            found = true;
                            break;
                        }
                    }
                }
            }

            if (found) {
                node.setTextContent(
                        "jdbc:mysql://" + host + ":3306/" + moduleHibernatePrefix + "?useUnicode=true&characterEncoding=UTF-8");
            } else {
                JOptionPane.showMessageDialog(
                        this, "Неверный файл " + confFile + "!", "Ошибка установки модуля", JOptionPane.ERROR_MESSAGE);
            }

            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer t = tf.newTransformer();
            t.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, "hibernate-configuration-3.0.dtd");
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(confFile);
            t.transform(source, result);

        } catch (Exception e) {
            logger.error(e);
        }
    }
    
    @Action
    public void install() {
        try {
            JFileChooser fc = new JFileChooser();
            fc.showDialog(this, "Установить");
            String salesPath = Util.getAppPath();
            String modulesPath = salesPath + "\\modules";
            File d = new File(modulesPath);
            if (!d.exists()) {
                d.mkdir();
            }

            Util.unzip(fc.getSelectedFile(), modulesPath);
            
            String moduleFolder = fc.getSelectedFile().getName();
            moduleFolder = moduleFolder.substring(0, moduleFolder.length() - 4);

            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(new File(modulesPath + "\\" + moduleFolder + "\\module-configuration.cfg"));
            doc.getDocumentElement().normalize();

            String name = Util.getConfItem("name", doc);
            String jar = Util.getConfItem("jar", doc);
            String version = Util.getConfItem("version", doc);

            setHost(salesPath, modulesPath, moduleFolder, "foreignincoming-hibernate-configuration.cfg.xml");
            setHost(salesPath, modulesPath, moduleFolder, "foreignincoming-hibernate-configuration2.cfg.xml");

            Session session = HUtil.getSession();
            session.beginTransaction();
            List ms = HUtil.executeHql("from Modules m where m.name = '" + name + "'", session);
            if (ms.size() > 0) {
                Modules m = (Modules) ms.get(0);
                m.setJar(jar);
                m.setPath(moduleFolder);
                m.setJarVersion(version);
                session.merge(m);
            } else {
                Modules m = new Modules();
                m.setName(name);
                m.setJar(jar);
                m.setPath(moduleFolder);
                m.setJarVersion(version);
                session.save(m);
            }
            session.getTransaction().commit();
            session.close();

            int a = JOptionPane.showOptionDialog(
                    this, "Установка модуля на сервер базы данных?", 
                    "Установка баз модуля", JOptionPane.YES_NO_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE, null, new Object[]{"Да", "Нет"}, "Да");
            if (a == 0) {
                File tmpSales = new File("C:\\Sales");
                tmpSales.mkdir();
                File tmp = new File("C:\\Sales\\temp");
                tmp.mkdir();
                File tmpModule = new File(tmp.getAbsolutePath() + "\\module.sql");
                FileUtils.copyFile(
                        new File(modulesPath + "\\" + moduleFolder + "\\module.sql"), tmpModule);
                Process process = Runtime.getRuntime().exec(
                        "cmd /c \"C:\\Program Files\\MySQL\\MySQL Server 5.5\\bin\\mysql\""
                        + " -u root < " + tmpModule.getAbsolutePath());
                process.waitFor();
                tmpModule.delete();
                tmp.delete();
                tmpSales.delete();
            }
            
            showTable();

        } catch (Exception e) {
            logger.error(e);
        }
    }

    @Action
    public void uninstall() {
        try {
            int r = jtModules.getSelectedRow();
            if (r != -1) {
                int a = JOptionPane.showOptionDialog(
                        this, "Удалить модуль?", "Удаление модуля", JOptionPane.YES_NO_CANCEL_OPTION,
                        JOptionPane.QUESTION_MESSAGE, null, new Object[]{"Да", "Нет", "Отмена"}, "Да");
                if (a == 0) {
                    Session session = HUtil.getSession();
                    session.beginTransaction();
                    Modules m = (Modules) HUtil.getElement("Modules", ((Modules) table.get(r)).getCode(), session);
                    session.delete(m);
                    session.getTransaction().commit();
                    session.close();
                    showTable();
                }
            }

        } catch (Exception e) {
            logger.error(e);
        }
    }
}