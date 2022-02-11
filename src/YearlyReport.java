import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class YearlyReport {

    //Содержимое файла
    private String yearlyReportList = "";

    //Текущий год
    private String year = "2021";

    //Списки всех операций по месяцам
    HashMap<String, ArrayList<String[]>> yearMonthTradeDeals = new HashMap<>();

    //Число прошедших месяцев года
    private int monthes = 3;

    //Метод для считывания данных из файла и заполнения списка операций по каждому месяцу
    public void addYearlyTradeDeals() {
        //Очистка списка для случая повторного вызова метода
        yearMonthTradeDeals.clear();

        //Адрес файла с годовым отчётом
        String path = "resources/y." + year + ".csv";

        //Считывание данных из файла и заполнение списка операций по каждому месяцу
        yearlyReportList = readFileContentsOrNull(path);

        //Разбиение содержимого файла на строки
        String[] monthTradeDeals = new String[0];
        if (yearlyReportList != null) {
            monthTradeDeals = yearlyReportList.split("\r\n");
        }

        for (int i = 0; i < monthes; i++) {
            //Для случаев однозначного и двузначного числа месяцев
            String month = "";
            if (i < 9) month = ("0" + (i + 1));
            else month = ("" + (i + 1));

            //Добавление месяца с пустым списком
            yearMonthTradeDeals.put(month, new ArrayList<>());

            //Разбиение строк на слова и заполнение списков
            for (int j = 1; j < monthTradeDeals.length; j++) {
                String[] yearTradeDeals = monthTradeDeals[j].split(",");
                if (yearTradeDeals[0].equals(month)) {
                    yearMonthTradeDeals.get(month).add(Arrays.copyOfRange(yearTradeDeals,
                            1, yearTradeDeals.length));
                }
            }
        }
    }

    //Сверка годового и помесячных отчётов
    public void verifyReports(MonthlyReport monthlyReport) {
        //Флаг соответствия в помесячных и годовом отчётах
        boolean flag = true;

        //Условие при необработанных файлах
        if (yearMonthTradeDeals.isEmpty() && monthlyReport.monthTradeDeals.isEmpty()) {
            System.out.println("Вы не обработали данные годового и помесячных отчётов.\nОбработайте их.");
        } else if (yearMonthTradeDeals.isEmpty()) {
            System.out.println("Вы не обработали данные годового отчёта.\nОбработайте их.");
        } else if (monthlyReport.monthTradeDeals.isEmpty()) {
            System.out.println("Вы не обработали данные помесячных отчётов.\nОбработайте их.");
        } else {
            //Обработка каждого месяца
            for (int i = 0; i < monthes; i++) {
                //Для случаев однозначного и двузначного числа месяцев
                String month = "";
                if (i < 9) month = ("0" + (i + 1));
                else month = ("" + (i + 1));

                //Месячные доходы из годового отчёта
                int yearMonthlyIncome = 0;

                //Месячные расходы из годового отчёта
                int yearMonthlyExpenses = 0;

                //Определение, является операция расходом(true) или доходом(false)
                if (yearMonthTradeDeals.get(month).get(0).length == 2 &&
                        yearMonthTradeDeals.get(month).get(1).length == 2) {
                    if (Boolean.parseBoolean(yearMonthTradeDeals.get(month).get(0)[1])) {
                        yearMonthlyExpenses += Integer.parseInt(yearMonthTradeDeals.get(month).get(0)[0]);
                        yearMonthlyIncome += Integer.parseInt(yearMonthTradeDeals.get(month).get(1)[0]);
                    } else {
                        yearMonthlyExpenses += Integer.parseInt(yearMonthTradeDeals.get(month).get(1)[0]);
                        yearMonthlyIncome += Integer.parseInt(yearMonthTradeDeals.get(month).get(0)[0]);
                    }
                }

                //Месячные доходы из помесячных отчётов
                int monthlyIncome = 0;

                //Месячные расходы из помесячных отчётов
                int monthlyExpenses = 0;

                //Определение месячных доходов и расходов из помесячных отчётов
                for (String[] monthTradeDeals : monthlyReport.monthTradeDeals.get(month)) {
                    if (Boolean.parseBoolean(monthTradeDeals[1])) {
                        monthlyExpenses += (Integer.parseInt(monthTradeDeals[2]) *
                                Integer.parseInt(monthTradeDeals[3]));
                    } else {
                        monthlyIncome += (Integer.parseInt(monthTradeDeals[2]) *
                                Integer.parseInt(monthTradeDeals[3]));
                    }
                }

                //Проверка сходимости отчётов
                if ((yearMonthlyIncome != monthlyIncome) || (yearMonthlyExpenses != monthlyExpenses)) {
                    System.out.printf("Есть несоответствие в месяце - %s.\n", month);
                    flag = false;
                }
            }

            if (flag) System.out.println("Несоответствий нет");
        }
    }

    //Метод для вывода информации по году
    public void yearlyReportsOutput() {
        //Средние доходы по месяцам
        double middleIncome = 0;

        //Средние расходы по месяцам
        double middleExpenses = 0;

        //Условие при необработанных файлах
        if (yearMonthTradeDeals.isEmpty()) {
            System.out.println("Вы не обработали данные годового отчёта.\nОбработайте их.");
        } else {
            System.out.printf("Год %s.\n", year);

            for (String month : yearMonthTradeDeals.keySet()) {
                //Месячные доходы
                int monthIncome = 0;

                //месячные расходы
                int monthExpenses = 0;

                //Определение, является операция расходом(true) или доходом(false)
                if (Boolean.parseBoolean(yearMonthTradeDeals.get(month).get(0)[1])) {
                    monthExpenses = Integer.parseInt(yearMonthTradeDeals.get(month).get(0)[0]);
                    monthIncome = Integer.parseInt(yearMonthTradeDeals.get(month).get(1)[0]);
                } else {
                    monthExpenses = Integer.parseInt(yearMonthTradeDeals.get(month).get(1)[0]);
                    monthIncome = Integer.parseInt(yearMonthTradeDeals.get(month).get(0)[0]);
                }

                System.out.printf("Месяц - %s.\n", month);
                System.out.printf("Прибыль в месяце - %d.\n", (monthIncome - monthExpenses));

                //Определение, является операция расходом(true) или доходом(false)
                if (Boolean.parseBoolean(yearMonthTradeDeals.get(month).get(0)[1])) {
                    middleExpenses += Double.parseDouble(yearMonthTradeDeals.get(month).get(0)[0]) / monthes;
                    middleIncome += Double.parseDouble(yearMonthTradeDeals.get(month).get(1)[0]) / monthes;
                } else {
                    middleExpenses += Double.parseDouble(yearMonthTradeDeals.get(month).get(1)[0]) / monthes;
                    middleIncome += Double.parseDouble(yearMonthTradeDeals.get(month).get(0)[0]) / monthes;
                }
            }

            System.out.printf("Средний расход за все месяцы в году - %.2f\n", middleExpenses);
            System.out.printf("Средний доход за все месяцы в году - %.2f\n", middleIncome);
        }
    }

    //Метод для считывания файлов
    private String readFileContentsOrNull(String path) {
        try {
            return Files.readString(Path.of(path));
        } catch (IOException e) {
            System.out.println("Невозможно прочитать файл с годовым отчётом. " +
                    "Возможно, файл не находится в нужной директории.");
            return null;
        }
    }
}
