package pl.coderslab;

import java.sql.*;

public class DbUtil {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/workshop2?useSSL=false&characterEncoding=utf8&serverTimezone=UTC" ; //bazkę warto zmienic :-)
    private static final String DB_USER = "root" ;
    private static final String DB_PASSWORD = "coderslab" ;

    private static final String DELETE_QUERY = "DELETE FROM tableName where id = ?";

    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    public static void insert(Connection conn, String query, String... params) {
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            for (int i = 0; i < params.length; i++) {
                statement.setString(i + 1, params[i]);
            }
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void printData(Connection conn, String query, String... columnNames) throws SQLException {
        try (PreparedStatement statement = conn.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                for (String columnName : columnNames) {
                    System.out.println(resultSet.getString(columnName));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void remove(Connection conn, String tableName, int id) {
        try (PreparedStatement statement = conn.prepareStatement(DELETE_QUERY.replace("tableName", tableName));) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void printDataValue(Connection conn, String query, String val, String... columnNames) throws SQLException {
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setString(1, val);
            ResultSet resultSet = statement.executeQuery();
            {
                while (resultSet.next()) {
                    for (String columnName : columnNames) {
                        System.out.println(resultSet.getString(columnName));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        try (Connection connect = DbUtil.connect()) {
            System.out.println("done");

        } catch (Exception e) {
            e.printStackTrace();
        }



    }
}
