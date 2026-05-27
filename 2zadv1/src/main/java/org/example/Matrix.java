package org.example;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public final class Matrix extends ArrayPI {
    public void multi (Scanner sc) {
        int[][] matrixM = new int[7][7];
        String[] mat1 = new String[7];
        String[] mat2 = new String[7];

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

            String printAll = "SELECT * FROM " + tbName;
            try (PreparedStatement psPrint = connection.prepareStatement(printAll); ResultSet rs = psPrint.executeQuery()) {
                while (rs.next()) {
                    String []Mat1 = rs.getString("Matrix1").split("\n");
                    String []Mat2 = rs.getString("Matrix2").split("\n");
                    for (int i = 0; i<7; i++) {
                        mat1 = Mat1[i].split(" "); mat2 = Mat2[i].split(" ");
                        for (int j = 0; j<7; j++) {
                            mas1[i][j] = Integer.parseInt(mat1[j]); mas2[i][j] = Integer.parseInt(mat2[j]);
                        }
                    }
                }
            } catch (SQLException e) { System.out.println("Ошибка при экспорте данных: " + e); }

            for (int i = 0; i < 7; i++) {
                for (int j = 0; j < 7; j++) {
                    matrixM[i][j] = 0;
                    for (int k = 0; k < 7; k++) {
                        matrixM[i][j] += mas1[i][k] * mas2[k][j];
                    }
                }
            }

            String printSQL = "SELECT * FROM " + tbName;
            try (PreparedStatement psInsert = connection.prepareStatement(printSQL)) {
                ResultSet rs = psInsert.executeQuery();
                while (rs.next()) {
                    String updateSQL = "UPDATE " + tbName + " SET SUMMatrix = ? WHERE Matrix1 = ?";
                    try (PreparedStatement psUpdate = connection.prepareStatement(updateSQL)) {
                        String StrAll = ""; String num = rs.getString("Matrix1");
                        System.out.print(num+"\n");
                        System.out.println("\nПеремноженная матрица: \n");
                        for (int i=0; i<7; i++){ // проход по строкам
                            for (int j=0; j<7; j++){ // проход по столбцам
                                System.out.print(" | " + matrixM[i][j]);
                                StrAll = StrAll + matrixM[i][j] + " ";
                            }
                            System.out.print(" |\n"); StrAll = StrAll + "\n";
                        }
                        psUpdate.setString(1, StrAll);
                        psUpdate.setString(2, num);
                        int rowsAffected = psUpdate.executeUpdate();
                        if (rowsAffected > 0) {
                            System.out.println("\nМатрица успешно добавлена в базу данных!");
                        } else {
                            System.out.println("Матрицы не найдены.");
                        }
                    } catch (SQLException e) {
                        System.out.println("Ошибка при добавлении матрицы в базу данных: " + e);
                    }
                }
            }
        } catch (SQLException e) { System.out.println("\nОшибка при добавлении в базу данных: " + e); }
    }
}
