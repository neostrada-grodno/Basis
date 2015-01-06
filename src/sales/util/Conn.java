package sales.util;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import javax.swing.JOptionPane;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import sales.SalesApp;

public final class Conn {

    static Logger logger = Logger.getLogger(SalesApp.class.getName());
    private static Connection conn;

    static {
        Util.initAppPath();
    }

    private Conn() {
    }

    public static synchronized Connection getConn() {

        if (conn == null) {

            try {

                File file = new File(Util.getAppPath() + "\\sales-hibernate-configuration.cfg.xml");
                if (!file.exists()) {
                    JOptionPane.showMessageDialog(null, "Файл конфигурации не найден!", "Ошибка файла конфигурации", JOptionPane.ERROR_MESSAGE);
                    return null;
                }
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder();
                Document doc = db.parse(file);
                doc.getDocumentElement().normalize();

                Class.forName(Util.getConfItem("hibernate.connection.driver_class", doc));
                conn = DriverManager.getConnection(
                        Util.getConfItem("hibernate.connection.url", doc),
                        Util.getConfItem("hibernate.connection.username", doc),
                        Util.getConfItem("hibernate.connection.password", doc));

            } catch (Exception e) {
                logger.error(e);
                JOptionPane.showMessageDialog(null, e + "!", "Ошибка подключения к БД", JOptionPane.ERROR_MESSAGE);
                return null;
            }

        }
        return conn;
    }

    @Override
    public void finalize() {

        try {
            conn.close();
            conn = null;
            super.finalize();
        } catch (Throwable e) {
            logger.error(e);
        }

    }
}
