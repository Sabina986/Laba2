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
                Sheet sheet = wb.createSheet("Matrix");
                Row row = sheet.createRow(0);
                row.createCell(0).setCellValue("Первая матрица");
                row.createCell(1).setCellValue("Вторая матрица");
                row.createCell(2).setCellValue("Произведение матриц");

                int rowUbdex = 1;
                System.out.printf("\n| %-38s | %-38s | %-38s |\n", "Первая матрица", "Вторая матрица", "Произведение матриц");
                while (rs.next()) {
                    System.out.print("----------------------------------------------------------------------------------------------------------------------------\n");
                    String []Mat1 = rs.getString("Matrix1").split("\n");
                    String []Mat2 = rs.getString("Matrix2").split("\n");
                    if(rs.getString("SUMMatrix")==null){
                        for (int i=0; i<7; i++){
                            System.out.printf("| %-38s | %-38s | %-38s |\n", Mat1[i], Mat2[i], " ");
                        }
                    } else {
                        String []SumMat = rs.getString("SUMMatrix").split("\n");
                        for (int i=0; i<7; i++){
                            System.out.printf("| %-38s | %-38s | %-38s |\n", Mat1[i], Mat2[i], SumMat[i]);
                        }
                    }
                    Row row1 = sheet.createRow(rowUbdex++);
                    row1.createCell(0).setCellValue(rs.getString("Matrix1"));
                    row1.createCell(1).setCellValue(rs.getString("Matrix2"));
                    row1.createCell(2).setCellValue(rs.getString("SUMMatrix"));
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

