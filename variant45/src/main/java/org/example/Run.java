package org.example;

import java.util.Scanner;

public class Run {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        boolean exit = false;

        while (!exit) {
            System.out.println("\n1. Вывести все таблицы из MySQL\n" + "2. Создать таблицу в БД в MySQL\n" +
                    "3. Изменить порядок символов строки на обратный, результат сохранить в MySQL с последующим выводом в консоль\n" +
                    "4. Добавить одну строку в другую, результат сохранить в MySQL с последующим выводом в консоль\n" +
                    "5. Сохранить все данные из MySQL в Excel и вывести на экран\n" + "6. Выход\n");
            System.out.print("Выберите пункт меню: ");
            String input = sc.nextLine();
            switch (input) {
                case "1":
                    CreateDB.createDatabase(sc);
                    break;
                case "2":
                    CreateTB.createTable(sc);
                    break;
                case "3":
                    NewStr.insertStr(sc);
                    Revers.update(sc);
                    break;
                case "4":
                    Union.podstr(sc);
                    break;
                case "5":
                    System.out.print("Введите путь для сохранения Excel-файла: ");
                    String filepath = sc.nextLine();
                    ExportToExcel.exportTableToExcel(filepath);
                    break;
                case "6":
                    exit = true;
                    System.out.println("Выход из программы");
                    break;
                default:
                    System.out.println("Неверное значение пункта меню! Попробуйте снова.");
            }
        }
        MysqlConfig.shutdown();
        sc.close();
    }
}
