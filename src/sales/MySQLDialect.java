package sales;

import java.sql.Types;
import org.hibernate.Hibernate;

public class MySQLDialect extends org.hibernate.dialect.MySQL5Dialect {

    public MySQLDialect() {
        super();
        // register additional hibernate types for default use in scalar sqlquery type auto detection
        registerHibernateType(Types.LONGVARCHAR, Hibernate.TEXT.getName());
    }
}
