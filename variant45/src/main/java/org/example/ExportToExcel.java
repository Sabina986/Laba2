package org.example;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


public class ExportToExcel {

    public static void exportTableToExcel(String filepath) {

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
                Workbook wb = new XSSFWorkbook();
                Sheet sheet = wb.createSheet("Stroki");
                Row row = sheet.createRow(0);
                row.createCell(0).setCellValue("Первая строка");
                row.createCell(1).setCellValue("Вторая строка");
                row.createCell(2).setCellValue("Обратная 1");
                row.createCell(3).setCellValue("Обратная 2");
                row.createCell(4).setCellValue("Объединённые строки");

                int rowUbdex = 1;
                System.out.printf("\n | %-100s | %-100s | %-100s | %-100s | %-200s |\n", "Первая строка", "Вторая строка", "Обратная 1",
                        "Обратная 2", "Объединённые строки");
                while (rs.next()) {
                    Row row1 = sheet.createRow(rowUbdex++);
                    row1.createCell(0).setCellValue(rs.getString("Str1")); String Str1 = rs.getString("Str1");
                    row1.createCell(1).setCellValue(rs.getString("Str2")); String Str2 = rs.getString("Str2");
                    row1.createCell(2).setCellValue(rs.getString("Rev1")); String R1 = rs.getString("Rev1");
                    row1.createCell(3).setCellValue(rs.getString("Rev2")); String R2 = rs.getString("Rev2");
                    row1.createCell(4).setCellValue(rs.getString("Sovm")); String V = rs.getString("Sovm");
                    System.out.printf(" | %-100s | %-100s | %-100s | %-100s | %-200s |\n", Str1, Str2, R1, R2, V);
                }
                System.out.print("\n");
                int columnCount = sheet.getRow(0).getPhysicalNumberOfCells();
                for (int i = 0; i < columnCount; i++) {
                    sheet.autoSizeColumn(i);
                }
                try (FileOutputStream fos = new FileOutputStream(filepath)) {
                    wb.write(fos);
                } catch (IOException e) {
                    System.out.println("Ошибка при записи Excel-файла: " + e);
                } finally {
                    wb.close();
                    System.out.println("Данные успешно экспортированы в Excel-файл: " + filepath);
                }
            } catch (SQLException e) {
                System.out.println("Ошибка при экспорте данных: " + e);
            }
        } catch (IOException | SQLException e) {
            System.out.println("Ошибка при закрытии Excel-файла: " + e);
        }
    }
}

