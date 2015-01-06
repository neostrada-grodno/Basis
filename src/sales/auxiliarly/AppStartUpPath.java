package sales.auxiliarly;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import org.apache.log4j.Logger;
import sales.SalesApp;

public class AppStartUpPath {

    static Logger logger = Logger.getLogger(SalesApp.class.getName());

    public String getAppStartUp() throws UnsupportedEncodingException {
        String strPath = getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
        try {
            URL url = new URL(strPath);
            strPath = url.getPath();
        } catch (MalformedURLException malformedUrlException) {
        }

        File f = new File(strPath);
        String strResult = URLDecoder.decode(f.getParent(), "UTF-8");
        return strResult;
    }
}
