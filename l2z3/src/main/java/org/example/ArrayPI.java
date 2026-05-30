package org.example;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class ArrayPI {
    int[] mas1  = new int[35];

    public void insertMat(Scanner sc) {
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
            int N;
            for (int i=0; i<35; i++){ // проход по строкам
                // Ввод чисел с проверкой
                while (true) {
                    System.out.print("Введите "+ (i+1)+ " элемент массива (целое число): ");
                    if (sc.hasNextInt()) {
                        N = sc.nextInt();
                        mas1 [i] = N;
                        break; // Корректный ввод
                    } else {
                        System.out.println("Ошибка: введите число."); sc.next();
                    }
                }
            }

            String insertSQL = "INSERT INTO " + tbName + " (Mass) VALUES (?)";
            try (PreparedStatement psInsert = connection.prepareStatement(insertSQL)) {
                String Str1 = "";
                System.out.println("\nМассив: \n");
                for (int i=0; i<35; i++){ // проход по строкам
                    System.out.print("[" + mas1[i] + "] ");
                    Str1 = Str1 + mas1[i] + " ";
                }
                psInsert.setString(1, Str1);
                psInsert.executeUpdate();
                System.out.println("\nМассив успешно добавлен в базу данных!"); sc.nextLine();
            }
        } catch (SQLException e) { System.out.println("\nОшибка при добавлении строк в базу данных: " + e); }
    }
}

