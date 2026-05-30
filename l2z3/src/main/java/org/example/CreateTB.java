package org.example;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

public final class CreateTB extends ArrayPI{
    public static String promtTableName(Scanner sc) {
        System.out.print("Введите имя таблицы: ");
        String tableName = sc.nextLine();
        if (!tableName.matches("\\w+")) {
            throw new IllegalArgumentException("Недопустимое имя базы данных!");
        }
        return tableName;
    }

    public static void createTable(Scanner sc) {

        try (Connection connection = MysqlConfig.getConnection()){
            String dbName = MysqlConfig.getDatabaseName();
            if (dbName == null || dbName.isEmpty()) {
                System.out.println("Ошибка! Сначала создайте базу данных!");
            }
            try (PreparedStatement psUse = connection.prepareStatement("USE " + dbName)) {
                psUse.executeUpdate();
            }

            String tableName = promtTableName(sc);
            MysqlConfig.setTable(tableName);
            String createTB = "CREATE TABLE IF NOT EXISTS " + tableName
                    + "(Mass VARCHAR(255) NOT NULL, " + "Mass2 VARCHAR(255), "
                    + "Mass3 VARCHAR(255))";
            try (PreparedStatement ps = connection.prepareStatement(createTB)) {
                ps.executeUpdate();
            }
            MysqlConfig.setTable(tableName);
            System.out.println("Таблица '" + tableName + "' успешно создана в базе данных '" + dbName + "'");
        } catch (SQLException e) {
            System.out.print("Ошибка при создании таблицы в базе данных: " + e);
        }
    }
}

