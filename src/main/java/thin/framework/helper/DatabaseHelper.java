package thin.framework.helper;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.commons.lang3.StringUtils;
import thin.framework.util.DataSourceUtil;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by yangyu on 2017/2/12.
 */
public final class DatabaseHelper {

    public static void init(){
        String driver = ConfigHelper.getJdbcDriver();
        String url = ConfigHelper.getJdbcUrl();
        if (StringUtils.isNotBlank(driver)&&StringUtils.isNotBlank(url)){
            DruidDataSource druidDataSource = new DruidDataSource();
            druidDataSource.setDriverClassName(driver);
            druidDataSource.setUrl(url);
            druidDataSource.setUsername(ConfigHelper.getJdbcUsername());
            druidDataSource.setPassword(ConfigHelper.getJdbcPassword());
            DataSourceUtil.setDataSource(druidDataSource);
        }
    }

    /**
     * 开启事务
     */
    public static void beginTransaction(){
        try {
            Connection connection = DataSourceUtil.getConnection();
            if (connection != null){
                connection.setAutoCommit(false);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

    }

    /**
     * 提交事务
     */
    public static void commitTransaction(){
        try {
            Connection connection = DataSourceUtil.getConnection();
            if (connection != null){
                connection.commit();
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }finally {
            DataSourceUtil.removeConnection();
        }
    }

    /**
     * 回滚事务
     */
    public static void rollbackTransaction(){
        Connection connection;
        try {
            connection = DataSourceUtil.getConnection();
            connection.rollback();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }finally {
            DataSourceUtil.removeConnection();
        }
    }



}
