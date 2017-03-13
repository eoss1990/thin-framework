package thin.framework.database;

import thin.framework.transaction.annotation.Transaction;
import thin.framework.util.DataSourceUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by yangyu on 2017/2/12.
 */
public abstract class BaseDao {

    @Transaction
    public int insertOrUpdateOrDelete(String sql) throws SQLException {
        Connection connection = getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        return preparedStatement.executeUpdate();
    }

    public ResultSet find(String sql) throws SQLException {
        Connection connection = getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        return preparedStatement.executeQuery();
    }

    public final Connection getConnection() throws SQLException {
        return DataSourceUtil.getConnection();
    }
}
