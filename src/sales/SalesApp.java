/*
 * SalesApp.java
 */
package sales;

import sales.auxiliarly.CPPopupEventQueue;
import java.awt.Component;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.sql.DriverManager;
import java.util.ArrayList;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.hibernate.classic.Session;
import org.jdesktop.application.Application;
import org.jdesktop.application.SingleFrameApplication;
import org.jopendocument.util.FileUtils;
import org.w3c.dom.Document;
import sales.interfaces.IClose;
import sales.util.Conn;
import sales.util.HUtil;
import sales.util.Util;

/**
 * The main class of the application.
 */
public class SalesApp extends SingleFrameApplication {

    static Logger logger = Logger.getLogger(SalesApp.class.getName());
    private static SalesView sv;

    /**
     * At startup create and show the main frame of the application.
     */
    @Override
    protected void startup() {
        Util.openedWins = 0;
        Util.wins = new ArrayList();
        sv = new SalesView(this);
        show(sv);
        sv.setWorkplace();
        sv.startListener();
    }

    /**
     * This method is to initialize the specified window by injecting resources.
     * Windows shown in our application come fully initialized from the GUI
     * builder, so this additional configuration is not needed.
     */
    @Override
    protected void configureWindow(java.awt.Window root) {

        root.addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {

                JRootPane rp = ((JFrame) e.getSource()).getRootPane();
                for (Component c : rp.getComponents()) {
                    if (c instanceof JLayeredPane) {
                        for (Component c2 : ((JLayeredPane) c).getComponents()) {
                            if (c2 instanceof JPanel) {
                                for (Component c3 : ((JPanel) c2).getComponents()) {
                                    if (c3 instanceof JPanel && c3.getName().equals("mainPanel")) {
                                        for (Component c4 : ((JPanel) c3).getComponents()) {
                                            if (c4 instanceof JDesktopPane) {
                                                for (JInternalFrame jif : ((JDesktopPane) c4).getAllFrames()) {
                                                    if (!jif.getName().equals("Workplace")) {
                                                        jif.moveToFront();
                                                        ((IClose) jif).close();
                                                    }
                                                }
                                                break;
                                            }
                                        }
                                        break;
                                    }
                                }
                                break;
                            }
                        }
                        break;
                    }
                }
                
                try {
                    HUtil.getConnection().close();
                } catch (Exception ex) {
                    logger.error(ex);
                }

                String[] fs = (new File("temp")).list();
                for (int i = 0; i < fs.length; i++) {
                    (new File("temp\\" + fs[i])).delete();
                }
                
                sv.stopListener();
                
                System.exit(0);
            }
        });
    }

    /**
     * A convenient static getter for the application instance.
     * @return the instance of SalesApp
     */
    public static SalesApp getApplication() {
        return Application.getInstance(SalesApp.class);
    }

    /**
     * Main method launching the application.
     */
    public static void main(String[] args) {

        Util.initAppPath();

        PropertyConfigurator.configure("sales_log4j.properties");

        if(Util.getAppPath().equals("D:\\netbeans\\Sales\\build")) {
            try {
                FileUtils.copyFile(
                        new File("D:\\netbeans\\Sales\\sales-hibernate-configuration.cfg.xml"),
                        new File("D:\\netbeans\\Sales\\build\\sales-hibernate-configuration.cfg.xml"));
                FileUtils.copyFile(
                        new File("D:\\netbeans\\Sales\\hibernate-configuration-3.0.dtd"),
                        new File("D:\\netbeans\\Sales\\build\\hibernate-configuration-3.0.dtd"));
                FileUtils.copyFile(
                        new File("D:\\netbeans\\Sales\\sales_log4j.properties"),
                        new File("D:\\netbeans\\Sales\\build\\sales_log4j.properties"));
                FileUtils.copyDirectory(
                        new File("D:\\netbeans\\Sales\\templates"),
                        new File("D:\\netbeans\\Sales\\build\\templates"));
                FileUtils.copyDirectory(
                        new File("D:\\netbeans\\Sales\\images"),
                        new File("D:\\netbeans\\Sales\\build\\images"));
                FileUtils.copyDirectory(
                        new File("D:\\netbeans\\Sales\\modules"),
                        new File("D:\\netbeans\\Sales\\build\\modules"));

            } catch (Exception e) {
                logger.error(e);
            }
        }

        try {
            File file =
                    new File(Util.getAppPath() + "\\sales_log4j.properties");
            if (!file.exists()) {
                JOptionPane.showMessageDialog(null, "Файл конфигурации log4j не найден!", "Ошибка файла конфигурации log4j", JOptionPane.ERROR_MESSAGE);
                return;
            }
            PropertyConfigurator.configure(Util.getAppPath() + "\\sales_log4j.properties");

        } catch (Exception e) {
            logger.error(e);
            JOptionPane.showMessageDialog(null, e + "!", "Ошибка log4j", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            File file = new File(Util.getAppPath() + "\\sales-hibernate-configuration.cfg.xml");
            if (!file.exists()) {
                JOptionPane.showMessageDialog(null, "Файл конфигурации не найден!", "Ошибка файла конфигурации", JOptionPane.ERROR_MESSAGE);
                return;
            }
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(file);
            doc.getDocumentElement().normalize();

            Class.forName(Util.getConfItem("hibernate.connection.driver_class", doc));
            HUtil.setConnection(DriverManager.getConnection(
                    Util.getConfItem("hibernate.connection.url", doc),
                    Util.getConfItem("hibernate.connection.username", doc),
                    Util.getConfItem("hibernate.connection.password", doc)));

            Session session = HUtil.getSession();
            session.close();

        } catch (Exception e) {
            logger.error(e);
            JOptionPane.showMessageDialog(null, e + "!", "Ошибка Hibernate", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (Conn.getConn() == null) {
            System.exit(0);
        }

        try {
            (new File("temp")).mkdir();
        } catch (Exception e) {
            logger.error(e);
        }

        try {

            if (!Util.startOffice("OpenOffice")) {
                if (!Util.startOffice("OpenOffice.org")) {
                    Util.startOffice("LibreOffice");
                }
            }

        } catch (Exception e) {
            logger.error(e);
        }
        
        Toolkit.getDefaultToolkit().getSystemEventQueue().push(new CPPopupEventQueue());

        launch(SalesApp.class, args);

    }
}