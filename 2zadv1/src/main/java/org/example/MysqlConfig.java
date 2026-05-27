package org.example;

//Импорт классов для работы с пулом соединений
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

//Импорт классов для работы с SQL-соединениями
import java.sql.Connection;
import java.sql.SQLException;

record MysqlConfig() {

    //Параметры подключения
    static final String MYSQL_URL = "jdbc:mysql://localhost:3306/";
    static final String LOGIN = "root";
    static final String PASSWORD = "6767";
    static final HikariDataSource DATA_SOURCE;

    //Названия БД и таблиц
    private static String databaseName;
    private static String table;

    //Конфигурация для подключения через HikariCP
    static {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(MYSQL_URL);
        config.setUsername(LOGIN);
        config.setPassword(PASSWORD);
        config.setMaximumPoolSize(10);
        DATA_SOURCE = new HikariDataSource(config);
    }

    //Метод для получения соединения с БД
    public static Connection getConnection() throws SQLException {
        // Возвращаем соединение с БД из пула соединений
        return DATA_SOURCE.getConnection();
    }

    // Закрытие соединений с БД (используем в самом конце работы программы)
    public static void shutdown() {
        DATA_SOURCE.close();
    }

    // Получение значения нашей БД
    public static String getDatabaseName() {
        return databaseName;
    }

    // Устанавливаем значение БД
    public static void setDatabaseName(String dbname) {
        databaseName = dbname;
    }

    // Получение значения таблицы в нашей БД databaseName
    public static String getTable() {
        return table;
    }

    // Установка значения таблицы в нашей БД databaseName
    public static void setTable(String tableName) {
        table = tableName;
    }
}

