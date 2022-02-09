import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;

public class MonthlyReport {

    //Список содержимого всех файлов
    ArrayList<String> monthlyReportList = new ArrayList<>();

    //Списки всех операций по месяцам
    HashMap<String, ArrayList<String[]>> monthTradeDeals = new HashMap<>();

    //Число прошедших месяцев года
    int month = 3;

    //Метод для считывания данных из файлов и заполнения списков содержимого всех файлов
    public void addMonthlyReportList() {
        //Очистка списка для случая повторного вызова метода
        monthlyReportList.clear();

        for (int i = 1; i <= month; i++) {
            //Для случаев однозначного и двузначного числа месяцев
            String path = "", month = "";
            if (i < 10) month = "0" + i;
            else month += i;

            //Адреса файлов с отчётами по каждому месяцу года
            path = "resources/m.2021" + month + ".csv";

            //Считывание данных из файлов и заполнение списка содержимого всех файлов
            String file = readFileContentsOrNull(path);
            monthlyReportList.add(file);
        }
    }

    //Метод для заполнения списков всех операций по месяцам
    public void addMonthlyTradeDeals() {
        //Очистка списков для случая повторного вызова метода
        monthTradeDeals.clear();

        //Вызов метода addMonthlyReportList
        addMonthlyReportList();

        for (int i = 1; i <= month; i++) {
            //Для случаев однозначного и двузначного числа месяцев
            String N;
            if (month < 10) N = ("0" + i);
            else N = ("" + i);

            //Добавление месяца с пустым списком
            monthTradeDeals.put(N, new ArrayList<>());

            //Разбиение содержимого файла на строки
            String[] tradeDeals = monthlyReportList.get(i - 1).split("\r\n");

            //Разбиение строк на слова и заполнение списков
            for (int j = 1; j < tradeDeals.length; j++) monthTradeDeals.get(N).add(tradeDeals[j].split(","));
        }
    }

    //Метод для вывода информации по всем месяцам
    public void monthlyReportsOutput() {
        //Условие при необработанных файлах
        if (monthTradeDeals.isEmpty()) {
            System.out.println("Вы не обработали данные помесячных отчётов.\nОбработайте их.");
        } else {
            //Обработка каждого месяца
            for (String month : monthTradeDeals.keySet()) {
                //Самый доходный товар
                String mostProfitableCommodity = "";
                //Самая большая выручка
                int maxMonthIncome = 0;
                //Самая большая трата
                String biggestSpend = "";
                //Размер самой большой траты
                int maxMonthExpense = 0;

                //Обработка всех продаж за месяц
                for (String[] operation : monthTradeDeals.get(month)) {
                    //Проверка. Является ли операция продажей
                    if (!Boolean.parseBoolean(operation[1])) {
                        //Выяснение. Является ли товар самым прибыльным
                        if ((Integer.parseInt(operation[2]) * Integer.parseInt(operation[3])) > maxMonthIncome) {
                            mostProfitableCommodity = operation[0];
                            maxMonthIncome = (Integer.parseInt(operation[2]) * Integer.parseInt(operation[3]));
                        }
                    }
                }

                //Обработка всех продаж за месяц
                for (String[] operation : monthTradeDeals.get(month)) {
                    //Проверка. Является ли операция покупкой
                    if (Boolean.parseBoolean(operation[1])) {
                        //Выяснение. Является ли покупка самой дорогой
                        if ((Integer.parseInt(operation[2]) * Integer.parseInt(operation[3])) > maxMonthExpense) {
                            biggestSpend = operation[0];
                            maxMonthExpense = (Integer.parseInt(operation[2]) * Integer.parseInt(operation[3]));
                        }
                    }
                }

                System.out.println("Месяц " + month + ".");
                System.out.println("Самый прибыльный товар - " + mostProfitableCommodity + ".");
                System.out.println("Выручка от самого прибыльного товара - " + maxMonthIncome + ".");
                System.out.println("Самый большая трата - " + biggestSpend + ".");
                System.out.println("Величина самой большой траты - " + maxMonthExpense + ".");
            }
        }
    }

    //Метод для считывания файлов
    private String readFileContentsOrNull(String path) {
        try {
            return Files.readString(Path.of(path));
        } catch (IOException e) {
            System.out.println("Невозможно прочитать файл с месячным отчётом. " +
                    "Возможно, файл не находится в нужной директории.");
            return null;
        }
    }
}
