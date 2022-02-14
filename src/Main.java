import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String itemStr;
        int item;
        MonthlyReport monthlyReport = new MonthlyReport();
        YearlyReport yearlyReport = new YearlyReport();

        do {
            System.out.println("Что вы хотите сделать:\n" +
                    "\t1 - Считать все месячные отчёты.\n" +
                    "\t2 - Считать годовой отчёт.\n" +
                    "\t3 - Сверить отчёты.\n" +
                    "\t4 - Вывести информацию о всех месячных отчётах.\n" +
                    "\t5 - Вывести информацию о годовом отчёте.\n" +
                    "\t0 - Закрыть программу.");

            //Флаг ввода допустимого значения
            boolean flag = true;

            do {
                System.out.print("Выберите доступное действие из списка: ");
                itemStr = scanner.nextLine();
                if (!itemStr.equals("1") && !itemStr.equals("2") && !itemStr.equals("3") &&
                        !itemStr.equals("4") && !itemStr.equals("5") && !itemStr.equals("0")) {
                    System.out.println("Вы ввели недопустимое значение.");
                } else flag = false;
            } while (flag);

            item = Integer.parseInt(itemStr);

            if (item == 1) {
                monthlyReport.addMonthlyTradeDeals();
            } else if (item == 2) {
                yearlyReport.addYearlyTradeDeals();
            } else if (item == 3) {
                yearlyReport.verifyReports(monthlyReport);
            } else if (item == 4) {
                monthlyReport.monthlyReportsOutput();
            } else if (item == 5) {
                yearlyReport.yearlyReportsOutput();
            }

        } while (item != 0);

        System.out.println("До свидания.");

        scanner.close();
    }
}

