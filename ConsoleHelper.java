import java.util.Scanner;

public class ConsoleHelper {
    private static final Scanner scanner = new Scanner(System.in);

    public static void waitForEnter() {
        System.out.println("Press Enter to continue...\n");
        scanner.nextLine();
    }

    public static String readLine(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }
}
