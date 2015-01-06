package sales.util;

import java.security.MessageDigest;
import java.util.List;
import org.apache.log4j.Logger;
import org.hyperic.sigar.CpuInfo;
import org.hyperic.sigar.Sigar;
import sales.interfaces.IInit;
import sales.SalesApp;
import sales.entity.Registertable;

public class Init {
    
    static Logger logger = Logger.getLogger(SalesApp.class.getName());

    IInit init;

    public void init(IInit init, String journalName) {
        
        try {
            String str = "";
            Sigar s = new Sigar();
            CpuInfo[] infos = s.getCpuInfoList();
            for (int i = 0; i < infos.length; i++) {
                CpuInfo info = infos[i];
                str += info.getCacheSize();
                str += info.getVendor();
                str += info.getModel();
                str += info.getTotalCores();
            }
            str = Util.removeSpacesStr(str);

            String c = "";
            byte[] d = MessageDigest.getInstance("MD5").digest(str.getBytes());

            byte[] dt = new byte[8];
            for (int i = 0; i < 8; i++) {
                int sm = 0;
                for (int j = 0; j < 1; j++) {
                    sm += d[i * 2 + j];
                }
                dt[i] = (byte) (sm % 10);
                if (dt[i] < 0) {
                    dt[i] = (byte) -dt[i];
                }
                c += dt[i];
            }

            String u = Util.removeSpacesStr(HUtil.getConstant("unt"));
            byte[] du = u.getBytes();

            byte[] d2 = new byte[8 + du.length];
            for (int i = 0; i < 8; i++) {
                d2[i] = dt[i];
            }
            for (int i = 0; i < du.length; i++) {
                d2[8 + i] = du[i];
            }

            byte[] d3 = MessageDigest.getInstance("MD5").digest(d2);

            boolean needOrder = false;
            List res = HUtil.executeHql("from Registertable r where r.unt = '" + c + "' order by r.number");
            if (res == null || res.size() < 16) {
                needOrder = true;
            } else {
                for (int i = 0; i < 16; i++) {
                    if (d3[i] != ((Registertable) res.get(i)).getProductCode()) {
                        needOrder = true;
                    }
                }
            }
            
            if (needOrder) {
                InitView r = new InitView(c, init, journalName);
                r.setVisible(true);
            } else {
                init.setStatus(true);
            }

        } catch (Exception e) {
            logger.error(e);
            //System.exit(1);
        }
    }
}
