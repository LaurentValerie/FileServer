package impl.utils;

import java.util.Scanner;

public class ConsoleReader {
    private static final Scanner scanner = new Scanner(System.in);


    public static RequestType getRequest() {
        int num = 0;
        String answer = scanner.nextLine();
        if (!answer.equals("exit")) {
            num = Integer.parseInt(answer);
        }
        return switch (num) {
            case 1 -> RequestType.GET;
            case 2 -> RequestType.PUT;
            case 3 -> RequestType.DELETE;
            case 0 -> RequestType.EXIT;
            default -> getRequest();  // Endless loop until right input
//            default -> throw new IllegalArgumentException("Only 1, 2 or 3 are valid answers!");
        };
    }


    public static RequestType byNameOrId() {
        int getBy = 0;
        String answer = scanner.nextLine();
        if (!answer.equals("exit")) {
            getBy = Integer.parseInt(answer);
        }
        return switch (getBy) {
            case 1 -> RequestType.BY_NAME;
            case 2 -> RequestType.BY_ID;
            default -> byNameOrId();  // Endless loop until right input
//            default -> throw new IllegalArgumentException("Only 1 or 2 are valid answers!");
        };
    }

    public static String readString(){
        return scanner.nextLine();
    }

    public static int readInt(){
        return Integer.parseInt(scanner.nextLine());
    }
}
