package com.fangfa.util;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author green
 * @date 2018/10/29/029
 */
public class JDBCUtil {

    private static Connection connect;
    private static String driverClassName = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    private static String URL = "jdbc:sqlserver://10.89.65.160:1433;DatabaseName=jl";
    private static String username = "sa";
//    private static String password = "gsw123"; //Sqcsqlpas1234567
//    private static String password = "Jlz123!@#"; //Sqcsqlpas1234567
    private static String password = "Sqcsqlpas1234567"; //Sqcsqlpas1234567

    /**
     * 类加载时加载数据库驱动
     */
    static {
        try {
            Class.forName(driverClassName);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取数据库连接的方法
     *
     * @return 数据库连接对象conn
     */
    private static Connection getConnection() {
        if(connect == null){
            try {
                connect = DriverManager.getConnection(URL, username, password);
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("获取连接失败");
            }
        }
        return connect;
    }

    /**
     * 执行更新操作（插入、修改、删除）
     *
     * @param sql    要执行的SQL语句
     * @param params SQL语句预编译参数（如无可省略）
     * @return rows 影响的行数
     */
    public static int executeUpdate(String sql, Object... params) throws Exception {
        Connection conn = getConnection();
        conn.setAutoCommit(false);
        PreparedStatement ps = null;
        int rows = 0;
        try {
            ps = getConnection().prepareStatement(sql);
            if (params != null && params.length > 0) {
                for (int i = 0; i < params.length; i++) {
                    ps.setObject(i + 1, params[i]);
                }
            }
            rows = ps.executeUpdate();
            conn.commit();
        } catch (SQLException e) {
           e.printStackTrace();
           conn.rollback();
        } finally {
           closeResource(conn, ps, null);
        }
        return rows;
    }


    /**
     * 执行查询操作
     *
     * @param sql    要执行的查询sql语句
     * @param params SQL语句预编译参数（如无可省略）
     * @return list 结果集，每一条结果为所有查询的字段名和字段值为键值对的Map集合
     */
    public static List<Map<String, Object>> executeQuery(String sql, Object... params) throws Exception {
        Connection conn = getConnection();
        conn.setAutoCommit(false);
        PreparedStatement ps = null;
        ResultSet set = null;
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        try {
            ps = conn.prepareStatement(sql);
            if (params != null && params.length > 0) {
                for (int i = 0; i < params.length; i++) {
                    ps.setObject(i + 1, params[i]);
                }
            }
            set = ps.executeQuery();
            conn.commit();
            ResultSetMetaData rsmd = set.getMetaData();
            int columnCount = rsmd.getColumnCount();
            while (set != null && set.next()) {
                Map<String, Object> map = new HashMap<String, Object>();
                for (int i = 1; i <= columnCount; i++) {
                    map.put(rsmd.getColumnName(i), set.getObject(i));
                }
                list.add(map);
            }
        } catch (SQLException e) {
             e.printStackTrace();
             conn.rollback();
        } finally {
             closeResource(conn, ps, set);
        }
        return list;
    }

    //这里不关闭连接，一直占用一个
    private static void closeResource(Connection conn, PreparedStatement ps, ResultSet rs) throws SQLException {
        if (rs != null) {
            rs.close();
        }
        if (ps != null) {
            ps.close();
        }
//        if (conn != null) {
//            conn.close();
//        }
    }

}
