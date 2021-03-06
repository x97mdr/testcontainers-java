package org.testcontainers.containers;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.junit.Test;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.rnorth.visibleassertions.VisibleAssertions.assertEquals;

public class VirtuosoJDBCDriverTest {

    @Test
    public void testVirtuosoWithNoSpecifiedVersion() throws SQLException {
        performSimpleTest("jdbc:tc:virtuoso://hostname/databasename");
    }


    private void performSimpleTest(String jdbcUrl) throws SQLException {
        HikariDataSource dataSource = getDataSource(jdbcUrl, 1);
        new QueryRunner(dataSource).query("SELECT 1", new ResultSetHandler<Object>() {
            @Override
            public Object handle(ResultSet rs) throws SQLException {
                rs.next();
                int resultSetInt = rs.getInt(1);
                assertEquals("A basic SELECT query succeeds", 1, resultSetInt);
                return true;
            }
        });
        dataSource.close();
    }

    private HikariDataSource getDataSource(String jdbcUrl, int poolSize) {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(jdbcUrl);
        hikariConfig.setConnectionTestQuery("SELECT 1");
        hikariConfig.setMinimumIdle(1);
        hikariConfig.setMaximumPoolSize(poolSize);

        return new HikariDataSource(hikariConfig);
    }
}
