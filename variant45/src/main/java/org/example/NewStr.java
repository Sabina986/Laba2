package org.example;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class NewStr {
    public static void insertStr(Scanner sc) {

        try (Connection connection = MysqlConfig.getConnection()) {
            String dbName = MysqlConfig.getDatabaseName();
            if (dbName == null || dbName.isEmpty()) {
                System.out.println("Ошибка! Сначала создайте/подключитесь к базе данных!");
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

            String Str1, Str2;
            // Ввод с проверкой
            while (true) {
                System.out.print("Введите первую строку не менее 50 символов: ");
                if (sc.hasNextLine()) {
                    Str1 = sc.nextLine();
                    if (Str1.length() >= 50) {
                        break; // Корректный ввод
                    } else {
                        System.out.println("Ошибка: строка должна содержать больше 50 символов");
                    }
                } else {
                    System.out.println("Ошибка: введите целое число.");
                    sc.next(); // Очистка неверного ввода
                }
            }
            while (true) {
                System.out.print("Введите вторую строку не менее 50 символов: ");
                if (sc.hasNextLine()) {
                    Str2 = sc.nextLine();
                    if (Str2.length() >= 50) {
                        break; // Корректный ввод
                    } else {
                        System.out.println("Ошибка: строка должна содержать больше 50 символов");
                    }
                } else {
                    System.out.println("Ошибка: введите целое число.");
                    sc.next(); // Очистка неверного ввода
                }
            }

            String insertSQL = "INSERT INTO " + tbName + " (Str1, Str2) VALUES (?, ?)";
            try (PreparedStatement psInsert = connection.prepareStatement(insertSQL)) {
                psInsert.setString(1, Str1);
                psInsert.setString(2, Str2);
                psInsert.executeUpdate();
                System.out.println("Строки '" + Str1 + "'\n     и '" + Str2 + "' \nуспешно добавлены в базу данных!");
            }
        } catch (SQLException e) {
            System.out.println("Ошибка при добавлении строк в базу данных: " + e);

        }
    }
}

