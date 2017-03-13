package thin.framework.util;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by yangyu on 2017/2/12.
 */
public final class DataSourceUtil {

    private static DataSource dataSource;

    private static ThreadLocal<Connection> connectionThreadLocal = new ThreadLocal<Connection>();

    public static Connection getConnection() throws SQLException {
        Connection connection = connectionThreadLocal.get();
        if (connection == null){
            connection = dataSource.getConnection();
            connectionThreadLocal.set(connection);
        }
        return connection;
    }

    public static void removeConnection(){
        connectionThreadLocal.remove();
    }

    public static void setDataSource(DataSource dataSource) {
        DataSourceUtil.dataSource = dataSource;
    }
}
