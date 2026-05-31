package org.example;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Revers {
    public static void update(Scanner sc) {
        try (Connection connection = MysqlConfig.getConnection()) {
            String dbName = MysqlConfig.getDatabaseName();
            if (dbName == null || dbName.isEmpty()) {
                System.out.println("Ошибка! Сначала создайте базу данных!");
                return;
            }
            try (PreparedStatement psUse = connection.prepareStatement("USE " + dbName)) {
                psUse.executeUpdate();
            }
            String tbName = MysqlConfig.getTable();
            if (tbName == null || tbName.isEmpty()) {
                System.out.println("Ошибка! Сначала создайте таблицу в базе данных!");
                return;
            }
            String checkTableSQL = "SHOW TABLES LIKE ?";
            try (PreparedStatement psCheck = connection.prepareStatement(checkTableSQL)) {
                psCheck.setString(1, tbName);
                try (ResultSet rs = psCheck.executeQuery()) {
                    if (!rs.next()) {
                        System.out.println("Таблицы '" + tbName + "' не существует. Сначала создайте таблицу");
                        return;
                    }
                }
            }

            String printSQL = "SELECT * FROM " + tbName;
            try (PreparedStatement psInsert = connection.prepareStatement(printSQL)) {
                ResultSet rs = psInsert.executeQuery();
                while (rs.next()) {
                    String updateSQL = "UPDATE " + tbName + " SET Rev1 = ?, Rev2 = ? WHERE Str1 = ?";
                    try (PreparedStatement psUpdate = connection.prepareStatement(updateSQL)) {
                        String num = rs.getString("Str1");
                        StringBuffer Prov11 = new StringBuffer(num); Prov11.reverse();
                        StringBuffer Prov21 = new StringBuffer(rs.getString("Str2")); Prov21.reverse();
                        psUpdate.setString(1, Prov11.toString());
                        psUpdate.setString(2, Prov21.toString());
                        psUpdate.setString(3, num);
                        int rowsAffected = psUpdate.executeUpdate();
                        if (rowsAffected > 0) {
                            System.out.println("Обратные для строк '" + num + "'\n             и '" + rs.getString("Str2") + "'\nнайдены");
                            System.out.println("               '" + Prov11 + "'\n             и '" + Prov21 + "'\n");
                        } else {
                            System.out.println("Строки не найдены.");
                        }
                    } catch (SQLException e) {
                        System.out.println("Ошибка : " + e);
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Ошибка при выводе строк из базы данных: " + e);

        }
    }
}

