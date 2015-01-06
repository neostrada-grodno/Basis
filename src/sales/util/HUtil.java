package sales.util;

import java.io.File;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import org.apache.log4j.Logger;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.SessionFactory;
import org.hibernate.classic.Session;
import sales.SalesApp;
import sales.entity.Constants;
import sales.entity.Nomenclature;

//Global function related to the database handling
public class HUtil {

    static Logger logger = Logger.getLogger(SalesApp.class.getName());
    private static final SessionFactory sessionFactory;
    private static Connection conn;

    static {
        try {
            // Create the SessionFactory from standard (hibernate.cfg.xml) 
            // config file.
            sessionFactory = new AnnotationConfiguration().configure(
                    new File(Util.getAppPath() + "\\sales-hibernate-configuration.cfg.xml")).buildSessionFactory();
        } catch (Throwable ex) {
            // Log the exception. 
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }
    
    public static Connection getConnection() {
        return conn;
    }
    
    public static void setConnection(Connection c) {
        conn = c;
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static Session getSession() {
        return getSessionFactory().openSession();
    }

    //Simple hql instruction
    public static List executeHql(String hql) {
        List resultList = null;
        try {
            Session session = getSession();
            resultList = session.createQuery(hql).list();
            session.close();
        } catch (Exception e) {
            logger.error(e);
        }
        return resultList;
    }

    //Simple hql instruction within the given session
    public static List executeHql(String hql, Session session) {
        List resultList = null;
        try {
            resultList = session.createQuery(hql).list();
        } catch (Exception e) {
            logger.error(e);
        }
        return resultList;
    }

    //get shipments for the given product and date
    public static ArrayList getShipments(int productCode, Date d) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        //get the list of incoming docs with the product code given
        //for each incoming doc we compute the balance
        Session session = HUtil.getSession();
        String sql =
                //Shipment is the incoming doc
                //get incoming and outcoming quanitites 
                //along with the appropriate info for each shipment
                "SELECT it.documentCode, sum(it.quantity),"
                + " (SELECT sum(ot.quantity)"
                + " FROM Outcomingtable AS ot, Outcoming AS o "
                + " WHERE ot.productCode = " + productCode
                + " AND ot.incomingCode = i.code"
                + " AND o.code = ot.documentCode"
                + " AND o.datetime < '" + sdf.format(d) + "'"
                + " AND o.active = 1"
                + " ),"
                + " it.price"
                + " FROM Incomingtable AS it, Incoming AS i"
                + " WHERE it.productCode = " + productCode
                + " AND i.code = it.documentCode"
                + " AND i.datetime < '" + sdf.format(d) + "'"
                + " AND i.active = 1"
                + " GROUP BY it.documentCode, it.price"
                + " ORDER BY i.datetime";
        List ss = session.createSQLQuery(sql).list();
        session.close();
        //return only positive balances
        ArrayList res = new ArrayList();
        for (int j = 0; j < ss.size(); j++) {
            HashMap oneRow = new HashMap();
            Object[] se = (Object[]) ss.get(j);
            int iv = 0;
            int ov = 0;
            if (se[1] != null) {
                iv = ((BigDecimal) se[1]).intValue();
            }
            if (se[2] != null) {
                ov = ((BigDecimal) se[2]).intValue();
            }
            if (iv > ov) {
                oneRow.put("incomingCode", (Integer) se[0]);
                oneRow.put("balance", iv - ov);
                oneRow.put("price", (Integer) se[3]);
                res.add(oneRow);
            }
        }
        return res;
    }

    //compute balance for product
    public static Integer getBalance(int productCode) {

        //compute incoming quanity 
        Session session = getSession();
        Integer balance = 0;
        String sql =
                "SELECT SUM(it.quantity)"
                + " FROM Incomingtable AS it, Incoming AS i"
                + " WHERE it.productCode = " + productCode
                + " AND it.documentCode = i.code"
                + " AND i.active = 1";
        List res = session.createSQLQuery(sql).list();
        if (res.size() > 0) {
            if (res.get(0) != null) {
                balance = ((BigDecimal) res.get(0)).intValue();
            }
        }
        //compute outcoming quantity and substract it
        sql =
                "SELECT SUM(ot.quantity)"
                + " FROM Outcomingtable AS ot, Outcoming AS o"
                + " WHERE ot.productCode = " + productCode
                + " AND ot.documentCode = o.code"
                + " AND o.active = 1";
        res = session.createSQLQuery(sql).list();
        if (res.size() > 0) {
            if (res.get(0) != null) {
                balance -= ((BigDecimal) res.get(0)).intValue();
            }
        }
        session.close();

        return balance;
    }

    //Get the balance for the given shipment
    public static int getBalanceForIncoming(int productCode, int incomingCode) {

        Session session = HUtil.getSession();
        //Get incoming quantity
        String hql =
                "select sum(it.quantity) from Incomingtable it, Incoming i"
                + " where it.productCode = " + productCode
                + " and i.code = it.documentCode"
                + " and i.code = " + incomingCode
                + " and i.active = 1";
        List ss = session.createQuery(hql).list();
        int iv = 0;
        if (ss.size() > 0) {
            if (ss.get(0) != null) {
                iv = new Integer(ss.get(0).toString());
            }
        }
        //Get outcoming quantity
        hql =
                "select sum(ot.quantity) from Outcomingtable ot, Outcoming o"
                + " where ot.productCode = " + productCode
                + " and ot.incomingCode = " + incomingCode
                + " and o.code = ot.documentCode"
                + " and o.active = 1";
        ss = session.createQuery(hql).list();
        int ov = 0;
        if (ss.size() > 0) {
            if (ss.get(0) != null) {
                ov = new Integer(ss.get(0).toString());
            }
        }
        session.close();
        return iv - ov;
    }

    //Get the 'name' field for the given table
    static public String getNameByCode(Integer code, String tableName) {
        String name = "";
        if (code != null) {
            try {
                Session session = getSession();
                List res = session.createQuery("select t.name from " + tableName + " t where t.code = " + code).list();
                if (res.get(0) != null) {
                    name = res.get(0).toString();
                }
                session.close();
            } catch (Exception e) {
                logger.error(e);
            }
        }
        return name;
    }

    //Get the 'name' field for the given table
    static public String getShortNameByCode(Integer code) {
        String name = "";
        if (code != null) {
            try {
                Session session = getSession();
                List res = session.createQuery("select e.shortName from Employee e where e.code = " + code).list();
                if (res.get(0) != null) {
                    name = res.get(0).toString();
                }
                session.close();
            } catch (Exception e) {
                System.err.println(e);
                logger.error(e);
            }
        }
        return name;
    }

    //Get the field for the given table
    static public String getFieldByCode(Integer code, String fieldName, String tableName) {
        String name = "";
        if (code != null) {
            try {
                Session session = getSession();
                List res = session.createQuery("select t." + fieldName + " from " + tableName + " t where t.code = " + code).list();
                if (res.get(0) != null) {
                    name = res.get(0).toString();
                }
                session.close();
            } catch (Exception e) {
                logger.error(e);
            }
        }
        return name;
    }

    //Get the sum for the given field of the given table related to a doc
    static public Integer getDocFieldSum(Integer code, String docName, String field) {
        Integer sum = 0;
        try {
            Session session = getSession();
            if (code != null) {
                List res =
                        session.createQuery("select sum(doc." + field + ") from "
                        + docName + "table doc where doc.documentCode = " + code).list();
                if (res.get(0) != null) {
                    sum = new Integer(res.get(0).toString());
                }
            }
        } catch (Exception e) {
            logger.error(e);
        }
        return sum;
    }

    //get balance change for the given period for the whole nomenclature
    static public List getBalanceNomenclature(Date date1, Date date2) {

        SimpleDateFormat sdfd = new SimpleDateFormat("yyyy-MM-dd");

        //compute the list of
        //initial incoming and outcoming quanity sums in quantity and amount
        //balance change for that values during the period
        String sql =
                "SELECT n.name, n.price,"
                //Incoming quantity before d1
                + " (SELECT SUM(it.quantity) FROM Incoming AS i, Incomingtable AS it"
                + " WHERE it.productCode = n.code"
                + " AND i.code = it.documentCode"
                + " AND DATE(i.datetime) < '" + sdfd.format(date1) + "'"
                + " AND i.active = 1),"
                //Outcoming quantity before d1
                + " (SELECT SUM(ot.quantity) FROM Outcoming AS o, Outcomingtable AS ot"
                + " WHERE ot.productCode = n.code"
                + " AND o.code = ot.documentCode"
                + " AND DATE(o.datetime) < '" + sdfd.format(date1) + "'"
                + " AND o.active = 1),"
                //Incoming quantity between d1 and d2
                + " (SELECT SUM(it.quantity) FROM Incoming AS i, Incomingtable AS it"
                + " WHERE it.productCode = n.code"
                + " AND i.code = it.documentCode"
                + " AND DATE(i.datetime) >= '" + sdfd.format(date1) + "'"
                + " AND DATE(i.datetime) <= '" + sdfd.format(date2) + "'"
                + " AND i.active = 1),"
                //Outcoming quantity between d1 and d2
                + " (SELECT SUM(ot.quantity) FROM Outcoming AS o, Outcomingtable AS ot"
                + " WHERE ot.productCode = n.code"
                + " AND o.code = ot.documentCode"
                + " AND DATE(o.datetime) >= '" + sdfd.format(date1) + "'"
                + " AND DATE(o.datetime) <= '" + sdfd.format(date2) + "'"
                + " AND o.active = 1)"
                + " FROM Nomenclature AS n"
                + " WHERE n.active = 0";
        
        Session session = getSession();
        List res = session.createSQLQuery(sql).list();
        session.close();
        return res;
    }

    //get balance change for the given period for the whole nomenclature
    static public List getBalance(String field, Date date1, Date date2) {

        SimpleDateFormat sdfd = new SimpleDateFormat("yyyy-MM-dd");

        //compute the list of
        //initial incoming and outcoming quanity sums in quantity and amount
        //balance change for that values during the period
        String sql =
                "SELECT n." + field + ","
                //Incoming quantity before d1
                + " (SELECT SUM(it.quantity) FROM Incoming AS i, Incomingtable AS it"
                + " WHERE it.productCode = n.code"
                + " AND i.code = it.documentCode"
                + " AND DATE(i.datetime) < '" + sdfd.format(date1) + "'"
                + " AND i.active = 1),"
                //Outcoming quantity before d1
                + " (SELECT SUM(ot.quantity) FROM Outcoming AS o, Outcomingtable AS ot"
                + " WHERE ot.productCode = n.code"
                + " AND o.code = ot.documentCode"
                + " AND DATE(o.datetime) < '" + sdfd.format(date1) + "'"
                + " AND o.active = 1),"
                //Incoming amount before d1
                + " (SELECT SUM(it.amount) FROM Incoming AS i, Incomingtable AS it"
                + " WHERE it.productCode = n.code"
                + " AND i.code = it.documentCode"
                + " AND DATE(i.datetime) < '" + sdfd.format(date1) + "'"
                + " AND i.active = 1),"
                //Outcoming amount before d1
                + " (SELECT SUM(ot.amount) FROM Outcoming AS o, Outcomingtable AS ot"
                + " WHERE ot.productCode = n.code"
                + " AND o.code = ot.documentCode"
                + " AND DATE(o.datetime) < '" + sdfd.format(date1) + "'"
                + " AND o.active = 1),"
                //Incoming quantity between d1 and d2
                + " (SELECT SUM(it.quantity) FROM Incoming AS i, Incomingtable AS it"
                + " WHERE it.productCode = n.code"
                + " AND i.code = it.documentCode"
                + " AND DATE(i.datetime) >= '" + sdfd.format(date1) + "'"
                + " AND DATE(i.datetime) <= '" + sdfd.format(date2) + "'"
                + " AND i.active = 1),"
                //Incoming amount between d1 and d2
                + " (SELECT SUM(ot.quantity) FROM Outcoming AS o, Outcomingtable AS ot"
                + " WHERE ot.productCode = n.code"
                + " AND o.code = ot.documentCode"
                + " AND DATE(o.datetime) >= '" + sdfd.format(date1) + "'"
                + " AND DATE(o.datetime) <= '" + sdfd.format(date2) + "'"
                + " AND o.active = 1),"
                //Outcoming quantity between d1 and d2
                + " (SELECT SUM(it.amount) FROM Incoming AS i, Incomingtable AS it"
                + " WHERE it.productCode = n.code"
                + " AND i.code = it.documentCode"
                + " AND DATE(i.datetime) >= '" + sdfd.format(date1) + "'"
                + " AND DATE(i.datetime) <= '" + sdfd.format(date2) + "'"
                + " AND i.active = 1),"
                //Outcoming amount between d1 and d2
                + " (SELECT SUM(ot.amount) FROM Outcoming AS o, Outcomingtable AS ot"
                + " WHERE ot.productCode = n.code"
                + " AND o.code = ot.documentCode"
                + " AND DATE(o.datetime) >= '" + sdfd.format(date1) + "'"
                + " AND DATE(o.datetime) <= '" + sdfd.format(date2) + "'"
                + " AND o.active = 1)"
                + " FROM Nomenclature AS n"
                + " WHERE n.active != 2";
        Session session = getSession();
        List res = session.createSQLQuery(sql).list();
        session.close();
        return res;
    }

    //Balance for the date
    static public List getBalance(String field, Date date) {

        SimpleDateFormat sdfd = new SimpleDateFormat("yyyy-MM-dd");

        String sql =
                //Incoming quantity for the date
                "SELECT n." + field + ","
                + " (SELECT SUM(it.quantity) FROM Incoming AS i, Incomingtable AS it"
                + " WHERE it.productCode = n.code"
                + " AND i.code = it.documentCode"
                + " AND DATE(i.datetime) <= '" + sdfd.format(date) + "'"
                + " AND i.active = 1),"
                //Outcoming quantity for the date
                + " (SELECT SUM(ot.quantity) FROM Outcoming AS o, Outcomingtable AS ot"
                + " WHERE ot.productCode = n.code"
                + " AND o.code = ot.documentCode"
                + " AND DATE(o.datetime) <= '" + sdfd.format(date) + "'"
                + " AND o.active = 1)"
                + " FROM Nomenclature AS n"
                + " WHERE n.active != 2";
        Session session = getSession();
        List res = session.createSQLQuery(sql).list();
        session.close();
        return res;
    }

    //Balance including all records in the database
    static public List getBalanceNow() {

        String sql =
                "SELECT n.code, n.price,"
                + " (SELECT SUM(it.quantity) FROM Incoming AS i, Incomingtable AS it"
                + " WHERE it.productCode = n.code"
                + " AND i.code = it.documentCode"
                + " AND i.active = 1),"
                + " (SELECT SUM(ot.quantity) FROM Outcoming AS o, Outcomingtable AS ot"
                + " WHERE ot.productCode = n.code"
                + " AND o.code = ot.documentCode"
                + " AND o.active = 1)"
                + " FROM Nomenclature AS n";
        Session session = getSession();
        List res = session.createSQLQuery(sql).list();
        session.close();
        return res;
    }

    //Whole balance for the date
    static public Long getWholeBalance(Date date, boolean includeDate) {

        long balance = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        String compDate = "<";
        if (includeDate) {
            compDate = "<=";
        }

        Session session = getSession();
        //Incoming quantity
        List res = session.createSQLQuery(
                "SELECT SUM(it.ndsAndAmount)"
                + " FROM Incoming AS i, Incomingtable AS it"
                + " WHERE i.code = it.documentCode"
                + " AND i.datetime " + compDate + " '" + sdf.format(date) + "'"
                + " AND i.active = 1").list();
        if (res.size() > 0) {
            if (res.get(0) != null) {
                balance += ((BigDecimal) res.get(0)).longValue();
            }
        }
        //Outcoming quantity
        res = session.createSQLQuery(
                "SELECT SUM(ot.amount)"
                + " FROM Outcoming AS o, Outcomingtable AS ot"
                + " WHERE o.code = ot.documentCode"
                + " AND o.datetime " + compDate + " '" + sdf.format(date) + "'"
                + " AND o.active = 1").list();
        if (res.size() > 0) {
            if (res.get(0) != null) {
                balance -= ((BigDecimal) res.get(0)).longValue();
            }
        }

        session.close();

        return balance;

    }

    //Balance for the date for the given product
    static public Integer getWholeBalanceAndProduct(Date date, boolean includeDate, Integer productCode) {

        Integer balance = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:00");

        String compDate = "<";
        if (includeDate) {
            compDate = "<=";
        }

        Session session = getSession();
        //Incoming balance
        List res = session.createSQLQuery(
                "SELECT SUM(it.quantity)"
                + " FROM Incoming AS i, Incomingtable AS it"
                + " WHERE it.productCode = " + productCode
                + " AND i.code = it.documentCode"
                + " AND i.datetime " + compDate + " '" + sdf.format(date) + "'"
                + " AND i.active = 1").list();
        if (res.size() > 0) {
            if (res.get(0) != null) {
                balance += ((BigDecimal) res.get(0)).intValue();
            }
        }
        //Outcoming balance
        res = session.createSQLQuery(
                "SELECT SUM(ot.quantity)"
                + " FROM Outcoming AS o, Outcomingtable AS ot"
                + " WHERE ot.productCode = " + productCode
                + " AND o.code = ot.documentCode"
                + " AND o.datetime " + compDate + " '" + sdf.format(date) + "'"
                + " AND o.active = 1").list();
        if (res.size() > 0) {
            if (res.get(0) != null) {
                balance -= ((BigDecimal) res.get(0)).intValue();
            }
        }

        session.close();

        return balance;

    }

    //create the next value for a field in the table, hql is produced elsewhere to get the appropriate selection
    static public String getNextMaxHQL(String hql, String table, String field, Session session) {

        List r = session.createQuery(hql).list();

        //Cut the part in the beginning that is not the ending number
        //and the part that is the ending number
        String np = "";
        int s = 1;
        if (r.size() > 0 && r.get(0) != null) {
            String ln = r.get(0).toString();
            int i;
            String ns = "";
            for (i = 0; i < ln.length(); i++) {
                char ch = ln.charAt(ln.length() - i - 1);
                if (ch >= '0' && ch <= '9') {
                    ns = ch + ns;
                } else {
                    break;
                }
            }
            if (!ns.isEmpty()) {
                //Increment the ending number part
                s = new Integer(ns) + 1;
            }
            //Not number part
            np = ln.substring(0, ln.length() - i);
        }
        //Increment the number part until we find a nonexistent one
        while (session.createQuery(
                "select x.code from " + table + " x"
                + " where x." + field + " = '" + np + s + "'"
                + " and x.active != 2").list().size() > 0) {
            s++;
        }
        return np + s;
    }

    //Next available number of the field
    static public String getNextMax(String table, String field, Session session) {
        return getNextMaxHQL(
                "select x." + field + " from " + table + " x"
                + " where x.active != 2 order by x.code desc limit 1",
                table, field, session);
    }

    //Next available number of the field
    static public String getNextMax(String table, String field, String filter, Session session) {
        if (!filter.isEmpty()) {
            filter = " and " + filter;
        }
        return getNextMaxHQL(
                "select x." + field + " from " + table + " x"
                + " where x.active != 2" + filter + " order by x.code desc limit 1",
                table, field, session);
    }

    //Next available doc number
    static public String getNextDocNumber(String table, String filter, Session session) {
        if (!filter.isEmpty()) {
            filter = " and " + filter;
        }
        return getNextMaxHQL(
                "select x.number from " + table + " x"
                + " where x.active != 2" + filter + " order by x.datetime desc limit 1",
                table, "number", session);
    }

    //Get the value of the constant
    static public String getConstant(String name) {
        String c = "";
        Session session = getSession();
        List res = session.createQuery("from Constants where name = '" + name + "'").list();
        if (res.size() > 0) {
            if (res.get(0) != null) {
                c = ((Constants) res.get(0)).getValue();
            }
        }
        return c;
    }

    //Get the integer constant
    static public Integer getIntConstant(String name) {
        Integer c = null;
        try {
            c = Integer.parseInt(getConstant(name));
        } catch (Exception e) {
        }
        return c;
    }

    //Set constants
    static public void setConstants(ArrayList values) {
        //We provide the list of Constats objects to update
        Session session = getSession();
        session.beginTransaction();
        List consts = session.createQuery("from Constants").list();
        for (int i = 0; i < values.size(); i++) {
            Constants nc = (Constants) values.get(i);
            for (int j = 0; j < consts.size(); j++) {
                Constants c = (Constants) consts.get(j);
                if (c.getName().equals(nc.getName())) {
                    c.setValue(nc.getValue());
                    session.update(c);
                    break;
                }
            }
        }
        session.getTransaction().commit();
        session.close();
    }

    //Get the element for the given code in the given table, session provided
    static public Object getElement(String table, Integer code, Session session) {
        Object r = null;
        if (code != null) {
            List res = session.createQuery("from " + table + " where code = " + code).list();
            if (res.size() > 0) {
                r = res.get(0);
            }
        }
        return r;
    }

    //Get the element for the given code in the given table
    static public Object getElement(String table, Integer code) {

        Session session = getSession();
        Object res = getElement(table, code, session);
        session.close();
        return res;
    }

    //get cash register page number on the given date
    static public String getPageNumber(Date date) {

        Session session = getSession();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        String p = null;
        //get the page number on the given date if exists
        String hql = "select r.page from Register r where DATE(r.datetime) = '" + sdf.format(date) + "'";
        List res = session.createQuery(hql).list();
        if (res.size() > 0) {
            if (res.get(0) != null) {
                p = res.get(0).toString();
            }
        }

        //if the page number doesn't exist, create it
        if (p == null || p.trim().equals("")) {
            List pg = session.createQuery(
                    "from Register r where DATE(r.datetime) > '" + sdf.format(date) + "'").list();
            if (pg.isEmpty()) {
                //if register doens't exist on the given date, produce the next page number
                p = HUtil.getNextMaxHQL(
                        "select x.page from Register x"
                        + " where DATE(x.datetime) < '" + sdf.format(date) + "'"
                        + " and x.active != 2"
                        + " order by x.code desc limit 1",
                        "Register", "page", session);
            } else {
                //if register exists, leave it empty
                p = "";
            }
        }
        session.close();
        return p;
    }

    //Check if hql returns empty result
    static public boolean checkEmpty(String hql) {
        Session session = HUtil.getSession();
        List res = session.createQuery(hql).list();
        session.close();
        return res.size() == 0;
    }

    //Check if the doc exists
    static public boolean checkDocExistence(String table, String number) {
        String hql =
                "select x.id from " + table + " x where x.number = '" + number + "'";
        return checkEmpty(hql);
    }

    //Get constant that contains a Double
    static public Double getConstantDouble(String name) {
        String scd = getConstant(name);
        Double cd = new Double(0);
        boolean df = false;
        boolean f = true;
        for (int i = 0; i < scd.length(); i++) {
            char ch = scd.charAt(i);
            //Sanity check
            if ((ch > '9' || ch < '0') && (ch != '.')) {
                f = false;
                break;
            }
            if (ch == '.') {
                if (!df) {
                    df = true;
                } else {
                    f = false;
                    break;
                }
            }
        }
        //If the value is OK create Double
        if (f) {
            cd = new Double(scd);
        }
        return cd;
    }

    //get nomeclature element if doesn't appear in the docs after the given date
    static public Nomenclature getNomenclatureDate(Integer productCode, Date datetime, Session session) {

        Nomenclature n = null;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        //check incoming docs
        List ri = session.createSQLQuery(
                "SELECT it.code FROM Incoming AS i, Incomingtable AS it"
                + " WHERE it.productCode = " + productCode
                + " AND i.code = it.documentCode"
                + " AND i.active = 1"
                + " AND i.datetime > '" + sdf.format(datetime) + "'"
                + " LIMIT 1").list();
        if (ri.isEmpty()) {
            //check repricing docs
            List rr = session.createSQLQuery(
                    "SELECT rt.code FROM Repricing AS r, Repricingtable AS rt"
                    + " WHERE rt.productCode = " + productCode
                    + " AND rt.code = rt.documentCode"
                    + " AND r.active = 1"
                    + " AND r.datetime > '" + sdf.format(datetime) + "'"
                    + " LIMIT 1").list();
            if (rr.isEmpty()) {
                //doesn't appear anywhere, take it
                n = (Nomenclature) getElement("Nomenclature", productCode, session);
            }
        }
        return n;
    }

    //update price if doens't appear in docs after the given date
    static public void updatePrice(Integer productCode, Integer newPrice, Date datetime, Session session) {

        Nomenclature n = getNomenclatureDate(productCode, datetime, session);
        if (n != null) {
            n.setPrice(newPrice);
            session.update(n);
        }
    }

    //update price and incoming prices if doesn't appear in docs after the given date
    static public void updatePrice(Integer productCode, Integer newInPrice, Integer newPrice, Date datetime, Session session) {

        Nomenclature n = getNomenclatureDate(productCode, datetime, session);
        if (n != null) {
            n.setInPrice(newInPrice);
            n.setPrice(newPrice);
            session.update(n);
        }
    }

    //update quantity if doens't appear after the given date
    static public void updateQuantity(Integer productCode, Integer amount, Session session) {

        Nomenclature n = (Nomenclature) getElement("Nomenclature", productCode, session);
        if (n != null) {
            //update quantity if it's not null that means zero or set it otherwise
            if (n.getQuantity() != null) {
                n.setQuantity(n.getQuantity() + amount);
            } else {
                n.setQuantity(amount);
            }
            session.update(n);
        }
    }

    //Get the price for the given product
    //dateComparison is "<" or "<=", the first to get the price prior the datetime, the second on the datetime
    static public Integer[] getPriceWithDate(Integer productCode, Date datetime, String dateComparison) {

        Session session = getSession();

        Integer[] result = {0, 0};

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        //take the union of prices that appear in the active incoming and repricing documents
        //and take the latest one
        List res = session.createSQLQuery(
                "(SELECT it.price, it.inPrice, i.datetime AS dt FROM Incoming AS i, Incomingtable AS it"
                + " WHERE it.productCode = " + productCode
                + " AND i.code = it.documentCode"
                + " AND i.active = 1"
                + " AND i.datetime " + dateComparison + " '" + sdf.format(datetime) + "')"
                + " UNION"
                + " (SELECT rt.newPrice,"
                + " (SELECT n.inPrice FROM Nomenclature AS n WHERE n.code = rt.productCode),"
                + " r.datetime AS dt FROM Repricing AS r, Repricingtable AS rt"
                + " WHERE rt.productCode = " + productCode
                + " AND r.code = rt.documentCode"
                + " AND r.active = 1"
                + " AND r.datetime " + dateComparison + " '" + sdf.format(datetime) + "')"
                + " ORDER BY dt DESC LIMIT 1").list();

        //get the result as an array
        if (res.size() > 0) {
            Object[] rp = (Object[]) res.get(0);
            result[0] = (Integer) rp[0];
            result[1] = (Integer) rp[1];
        }

        return result;
    }

    //Find the latest doc changing the product price
    static public Object[] getDocChangePrice(Integer productCode) {

        Session session = getSession();

        Object[] row = null;

        List res = session.createSQLQuery(
                //Incoming docs
                "(SELECT i.code, 0, i.datetime AS dt FROM Incoming AS i, Incomingtable AS it"
                + " WHERE it.productCode = " + productCode
                + " AND i.code = it.documentCode"
                + " AND i.type = 0"
                + " AND i.active = 1)"
                + " UNION"
                //Repricing docs
                + " (SELECT r.code, 1, r.datetime as dt FROM Repricing AS r, Repricingtable AS rt"
                + " WHERE rt.productCode = " + productCode
                + " AND r.code = rt.documentCode"
                + " AND r.active = 1)"
                + " ORDER BY dt DESC LIMIT 1").list();
        if (res.size() > 0) {
            row = (Object[]) res.get(0);
        }

        return row;
    }

    //Product price on the date including the date
    static public Integer[] getPriceOnDate(Integer productCode, Date datetime) {
        return getPriceWithDate(productCode, datetime, "<=");
    }

    //Product price on the date not includint the date
    static public Integer[] getPriceBeforeDate(Integer productCode, Date datetime) {
        return getPriceWithDate(productCode, datetime, "<");
    }

    //get price on the given timepoint
    static public int getPriceOnDatePriceOnly(Integer productCode, Date datetime) {

        int result = 0;

        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        //take the union of prices that appear in the active incoming and repricing documents
        //and take the latest one

        String sql =
                "(select it.price, i.datetime as dt"
                + " from Incomingtable as it inner join Incoming as i"
                + " on it.documentCode = i.code"
                + " where it.productCode = " + productCode
                + " and i.datetime <= '" + sdf.format(datetime) + "'"
                + " and i.active = 1)"
                + " union"
                + " (select rt.newPrice, r.datetime AS dt"
                + " from Repricing AS r inner join Repricingtable AS rt"
                + " on rt.documentCode = r.code"
                + " where rt.productCode = " + productCode
                + " and r.datetime <= '" + sdf.format(datetime) + "'"
                + " and r.active = 1)"
                + " order by dt desc limit 1";

        try {

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            if (rs.next()) {
                result = rs.getInt(1);
            }

        } catch (Exception e) {
            logger.error(e);
        }

        return result;
    }

    //check if there are locked docs in the datebase
    static public boolean lockedDocs(String[] docs, Session session) {

        for (int i = 0; i < docs.length; i++) {
            List res = executeHql("from " + docs[i] + " x where x.locked = 1", session);
            if (res.size() > 0) {
                return true;
            }
        }
        return false;
    }

    //check if there is a doc in the table with the given value of a field but with a different code
    static public boolean checkExists(String table, String field, String value, Integer code) {

        boolean e = false;

        //sanity check
        if (value != null && !value.isEmpty() && code != null) {

            Session session = getSession();

            //doc must have different code, not deleted (active field)
            List res = session.createSQLQuery(
                    "SELECT x.code FROM " + table + " AS x"
                    + " WHERE x." + field + " = '" + value + "'"
                    + " AND x.code != " + code
                    + " AND x.active != 2 LIMIT 1").list();

            session.close();

            e = res.size() > 0;
        }

        return e;

    }
}
