package org.example;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class ArrayPI {
    int N;
    int[][] mas1  = new int[7][7];
    int[][] mas2  = new int[7][7];

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

            for (int i=0; i<7; i++){ // проход по строкам
                for (int j=0; j<7; j++){ // проход по столбцам
                    // Ввод чисел с проверкой
                    while (true) {
                        System.out.print("Введите элемент первой матрицы ["+ (i+1) +"][" + (j+1) + "] (целые числа): ");
                        if (sc.hasNextInt()) {
                            N = sc.nextInt();
                            mas1 [i][j] = N;
                            break; // Корректный ввод
                        } else {
                            System.out.println("Ошибка: введите число."); sc.next();
                        }
                    }
                }
            } System.out.println("\n");
            for (int i1=0; i1<7; i1++){ // проход по строкам
                for (int j=0; j<7; j++){ // проход по столбцам
                    // Ввод чисел с проверкой
                    while (true) {
                        System.out.print("Введите элемент второй матрицы ["+ (i1+1) +"][" + (j+1) + "] (целые числа): ");
                        if (sc.hasNextInt()) {
                            N = sc.nextInt();
                            mas2 [i1][j] = N;
                            break; // Корректный ввод
                        } else {
                            System.out.println("Ошибка: введите число."); sc.next();
                        }
                    }
                }
            }

            String insertSQL = "INSERT INTO " + tbName + " (Matrix1, Matrix2) VALUES (?, ?)";
            try (PreparedStatement psInsert = connection.prepareStatement(insertSQL)) {
                String Str1 = "", Str2 = "";
                System.out.println("\nПервая матрица: \n");
                for (int i=0; i<7; i++){ // проход по строкам
                    for (int j=0; j<7; j++){ // проход по столбцам
                        System.out.print(" | " + mas1[i][j]);
                        Str1 = Str1 + mas1[i][j] + " ";
                    }
                    System.out.print(" |\n"); Str1 = Str1 + "\n";
                }
                System.out.println("\nВторая матрица: \n");
                for (int i=0; i<7; i++){ // проход по строкам
                    for (int j=0; j<7; j++){ // проход по столбцам
                        System.out.print(" | " + mas2[i][j]);
                        Str2 = Str2 + mas2[i][j] + " ";
                    }
                    System.out.print(" |\n"); Str2 = Str2 + "\n";
                }
                psInsert.setString(1, Str1);
                psInsert.setString(2, Str2);
                psInsert.executeUpdate();
                System.out.println("\nМатрицы успешно добавлены в базу данных!"); sc.nextLine();
            }
        } catch (SQLException e) { System.out.println("\nОшибка при добавлении строк в базу данных: " + e); }
    }
}

