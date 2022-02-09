import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String Nstr = "";
        int N;
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
            do {
                System.out.print("Выберите доступное действие из списка: ");
                Nstr = scanner.nextLine();
            } while (!Nstr.equals("1") && !Nstr.equals("2") && !Nstr.equals("3") &&
                    !Nstr.equals("4") && !Nstr.equals("5") && !Nstr.equals("0"));

            N = Integer.parseInt(Nstr);

            if (N == 1) {

                monthlyReport.addMonthlyTradeDeals();

            } else if (N == 2) {

                yearlyReport.addYearlyTradeDeals();

            } else if (N == 3) {

                yearlyReport.verifyReports(monthlyReport);

            } else if (N == 4) {

                monthlyReport.monthlyReportsOutput();

            } else if (N == 5) {

                yearlyReport.yearlyReportsOutput();

            }
        } while (!Nstr.equals("0"));

        System.out.println("До свидания.");
    }
}

