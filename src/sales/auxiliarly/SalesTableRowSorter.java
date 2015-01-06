package sales.auxiliarly;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import org.apache.log4j.Logger;
import sales.SalesApp;

public class SalesTableRowSorter extends TableRowSorter {

    static Logger logger = Logger.getLogger(SalesApp.class.getName());
    private ArrayList<String[]> columns;

    public SalesTableRowSorter(TableModel tm, ArrayList<String[]> columns) {
        super(tm);
        this.columns = columns;
    }

    @Override
    public Comparator<?> getComparator(int column) {

        String type = columns.get(column)[1];
        if (type.equals("int")) {
            return new Comparator<String>() {

                @Override
                public int compare(String s1, String s2) {
                    return Integer.parseInt(s1) - Integer.parseInt(s2);
                }
            };
        } else if (type.equals("date")) {
            return new Comparator<String>() {

                @Override
                public int compare(String s1, String s2) {

                    int res = 0;
                    try {
                        final SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yy");
                        Date dt1 = sdf.parse(s1);
                        Date dt2 = sdf.parse(s2);
                        res = (int) ((dt1.getTime() - dt2.getTime()) / 86400000);
                    } catch (Exception e) {
                        logger.error(e);
                        res = s1.compareTo(s2);
                    }
                    return res;
                }
            };
        }
        return super.getComparator(column);
    }
}