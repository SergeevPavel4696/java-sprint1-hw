import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

class MonthlyReport {

    //Списки всех операций по месяцам
    private HashMap<String, ArrayList<String[]>> monthTradeDeals = new HashMap<>();

    //Число прошедших месяцев года
    private int months = 3;

    //Метод для считывания данных из файлов и заполнения списков всех операций по месяцам
    void addMonthlyTradeDeals() {
        //Очистка списков для случая повторного вызова метода
        monthTradeDeals.clear();

        for (int i = 0; i < months; i++) {
            //Для случаев однозначного и двузначного числа месяцев
            String month = "";
            if (i < 9) {
                month = "0" + (i + 1);
            } else {
                month += (i + 1);
            }

            //Адреса файлов с отчётами по каждому месяцу года
            String path = "resources/m.2021" + month + ".csv";

            //Считывание данных из файлов и заполнение списка содержимого всех файлов
            List<String> file = readFileContentsOrNull(path);
            if (file == null) {
                monthTradeDeals.clear();
                return;
            }

            //Добавление месяца с пустым списком
            monthTradeDeals.put(month, new ArrayList<>());

            //Разбиение содержимого файла на строки
            String[] tradeDeals = file.toArray(new String[0]);

            if (tradeDeals.length < 2) {
                System.out.println("В файле нет необходимой информации.");
                monthTradeDeals.clear();
                return;
            }

            //Разбиение строк на слова и заполнение списков словами
            for (int j = 1; j < tradeDeals.length; j++) {
                monthTradeDeals.get(month).add(tradeDeals[j].split(","));
                //Удаление пробелов перед словом и после слова
                for (int k = 0; k < monthTradeDeals.get(month).get(j - 1).length; k++) {
                    monthTradeDeals.get(month).get(j - 1)[k] = monthTradeDeals.get(month).get(j - 1)[k].trim();
                }
            }
        }
    }

    //Метод для вывода информации по всем месяцам
    void monthlyReportsOutput() {
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
                        if (Integer.parseInt(operation[2]) * Integer.parseInt(operation[3]) > maxMonthIncome) {
                            mostProfitableCommodity = operation[0];
                            maxMonthIncome = (Integer.parseInt(operation[2]) * Integer.parseInt(operation[3]));
                        }
                    } else {
                        //Выяснение. Является ли покупка самой дорогой
                        if (Integer.parseInt(operation[2]) * Integer.parseInt(operation[3]) > maxMonthExpense) {
                            biggestSpend = operation[0];
                            maxMonthExpense = (Integer.parseInt(operation[2]) * Integer.parseInt(operation[3]));
                        }
                    }
                }

                System.out.printf("Месяц %s.\n", month);

                if (maxMonthIncome == 0) {
                    System.out.println("В этом месяце ничего не было продано.");
                } else {
                    System.out.printf("Самый прибыльный товар - %s.\n", mostProfitableCommodity);
                    System.out.printf("Выручка от самого прибыльного товара - %s.\n", maxMonthIncome);
                }

                if (maxMonthExpense == 0) {
                    System.out.println("В этом месяце не было трат.");
                } else {
                    System.out.printf("Самый большая трата - %s.\n", biggestSpend);
                    System.out.printf("Величина самой большой траты - %s.\n", maxMonthExpense);
                }
            }
        }
    }

    //Получение числа месяцев помесячных отчётов
    int getMonths() {
        return months;
    }

    //Получение списка всех операций по месяцам
    HashMap<String, ArrayList<String[]>> getMonthTradeDeals() {
        return monthTradeDeals;
    }

    //Метод для считывания файлов
    private List<String> readFileContentsOrNull(String path) {
        try {
            return Files.readAllLines(Path.of(path));
        } catch (IOException e) {
            System.out.println("Невозможно прочитать файл с месячным отчётом. " +
                    "Возможно, файл не находится в нужной директории.");
            return null;
        }
    }
}
