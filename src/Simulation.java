import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class Simulation {
    private static int width;
    private static int height;
    private static int days;
    private static TimeUnit timeUnit;
    private static int length;

    public static void readDataFromUser(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter island width: ");
        width = scanner.nextInt();
        System.out.println("Enter island height: ");
        height = scanner.nextInt();
        System.out.println("Amount of days: ");
        days = scanner.nextInt();
        System.out.println("Time unit sec/millis/minutes: ");
        String unit = scanner.next();
        getCorrectUnit(unit);
        System.out.println("Length of 1 day: ");
        length = scanner.nextInt();
    }
    public static void getCorrectUnit(String unit){
        timeUnit = switch (unit){
            case "sec" -> TimeUnit.SECONDS;
            case "millis" -> TimeUnit.MILLISECONDS;
            case "minutes" -> TimeUnit.MINUTES;
            default -> throw new IllegalStateException("Unexpected value: " + unit);
        };
    }
    public static void main(String[] args) {
        readDataFromUser();
        Island island = new Island(width, height);
        island.simulate(days, timeUnit, length);
    }
}
