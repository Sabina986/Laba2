package org.example;

import java.sql.*;

//import java.mysql.cj.jdbc.Driver;
import java.util.Scanner;

public final class CreateDB extends ArrayPI{
    public static String promtDatabaseName(Scanner sc) {
        System.out.print("Введите имя базы данных: ");
        String dbname = sc.nextLine();
        if (!dbname.matches("\\w+")) {
            throw new IllegalArgumentException("Недопустимое имя базы данных!");
        }
        return dbname;
    }

    public static void createDatabase(Scanner sc) {
        try (Connection connection = MysqlConfig.getConnection()) {
            String dbname = promtDatabaseName(sc);
            String createDB = "CREATE DATABASE IF NOT EXISTS " + dbname;
            try (PreparedStatement ps = connection.prepareStatement(createDB)) {
                ps.executeUpdate();
            }
            MysqlConfig.setDatabaseName(dbname);
            System.out.println("База данных '" + dbname + "' успешно создана/подключена!");

            try (Statement stmt = connection.createStatement()) {
                String doDB = "SHOW TABLES FROM " + dbname;
                ResultSet rs = stmt.executeQuery(doDB);
                System.out.println("=Таблицы из базы данных " + dbname + "=\n");
                while(rs.next()){
                    System.out.println(rs.getString(1));
                }
                System.out.print("\n");
            }

        } catch (SQLException e) {
            System.out.print("Ошибка при создании/подключении к базе данных: " + e);
        }
    }
}

