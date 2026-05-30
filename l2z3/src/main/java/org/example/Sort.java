package org.example;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public final class Sort extends ArrayPI {
    public void sorting(Scanner sc) {
        int[] sor1 = new int[35]; int[] sor2 = new int[35];
        String[] mat1 =  new String[35];

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

            String printSQL = "SELECT * FROM " + tbName;
            try (PreparedStatement psInsert = connection.prepareStatement(printSQL)) {
                ResultSet rs = psInsert.executeQuery();
                while (rs.next()) {
                    mat1 = rs.getString("Mass").split(" ");
                    for (int i = 0; i<35; i++) {
                        mas1[i] = Integer.parseInt(mat1[i]);
                        sor1[i] = mas1[i]; sor2[i] = mas1[i];
                    }
                    // сортировка
                    for (int i = 34; i > 0; i--) {
                        for (int j = 0; j < i; j++) {
                            if (sor1[j] > sor1[j + 1]) {
                                int temp1 = sor1[j];
                                sor1[j] = sor1[j + 1];
                                sor1[j + 1] = temp1;
                            }
                        }
                    }
                    for (int i = 0; i < 34; i++) {
                        for (int j = 0; j < 34-i; j++) {
                            if (sor2[j] < sor2[j + 1]) {
                                int temp2 = sor2[j];
                                sor2[j] = sor2[j + 1];
                                sor2[j + 1] = temp2;
                            }
                        }
                    }
                    for (int i = 0; i <35; i++){
                        System.out.print(sor1[i] + " " + sor2[i] + "\n");
                    }

                    String updateSQL = "UPDATE " + tbName + " SET Mass2 = ?, Mass3 = ? WHERE Mass = ?";
                    try (PreparedStatement psUpdate = connection.prepareStatement(updateSQL)) {
                        String StrAll = "", StrSum = "";
                        String num = rs.getString("Mass");
                        System.out.println("\nИсходный массив: \n" + num);
                        System.out.print("\nОтсортированные массивы \nПо возрастанию:\n| ");
                        for (int i=0; i<35; i++){
                            System.out.print(sor1[i] + " ");
                            StrAll = StrAll +  sor1[i] + " ";
                        }
                        System.out.println("\nПо убыванию: \n");
                        for (int i=0; i<35; i++){
                            System.out.print(sor2[i] + " ");
                            StrSum = StrSum + sor2[i] + " ";
                        }
                        System.out.print("\n");
                        psUpdate.setString(1, StrAll); psUpdate.setString(2, StrSum);
                        psUpdate.setString(3, num);
                        int rowsAffected = psUpdate.executeUpdate();
                        if (rowsAffected > 0) {
                            System.out.println("\nМассивы успешно добавлены в базу данных!");
                        }  else {
                            System.out.println("Массив не найден.");
                        }
                    } catch (SQLException e) {
                        System.out.println("Ошибка при добавлении массивов в базу данных: " + e);
                    }
                }
            }
        } catch (SQLException e) { System.out.println("\nОшибка при добавлении в базу данных: " + e); }
    }
}

