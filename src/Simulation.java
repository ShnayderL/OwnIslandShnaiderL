import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class Simulation {
    private static final int ISLAND_WIDTH = 100;
    private static final int ISLAND_HEIGHT = 20;
    private static final int MAX_THREADS = 12;

    public static void main(String[] args) throws InterruptedException {
        // Створюємо острів
        Island island = new Island(ISLAND_WIDTH, ISLAND_HEIGHT);

        // Додаємо локації
        for (int x = 0; x < ISLAND_WIDTH; x++) {
            for (int y = 0; y < ISLAND_HEIGHT; y++) {
                island.addLocation(x, y, new Location(island, new Point(x, y)));
            }
        }
        for (Location location : island.getLocations().values()) {
            location.init();
        }
        System.out.println("Island initialized");
        System.out.println(island.showPopulation());
        // Створюємо пул потоків для паралельного виконання
        ExecutorService executor = Executors.newFixedThreadPool(MAX_THREADS);

        try {
            // Виконуємо симуляцію
            int daysToSimulate = 40;
            for (int day = 1; day <= daysToSimulate; day++) {
                System.out.println("День " + day);

                // Обробляємо локації паралельно
                List<Future<?>> futures = new ArrayList<>();
                for (Location location : island.getLocations().values()) {
                    futures.add(executor.submit(location::update));
                }

                // Чекаємо завершення всіх потоків
                for (Future<?> future : futures) {
                    try {
                        future.get(); // Очікуємо завершення конкретного завдання
                    } catch (InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                    }
                }

                Thread.sleep(10000); // Пауза між днями
                // Виводимо статистику
                System.out.println(island.showPopulation());
            }
        } finally {
            // Завершуємо роботу пулу потоків
            System.out.println("Кінець симуляції, очистка даних...");
            for (Location location : island.getLocations().values()) {
                location.shutdown();
            }
            executor.shutdown();
            if (!executor.awaitTermination(2000, TimeUnit.MILLISECONDS)) {
                System.err.println("Потоки не завершились вчасно, завершення примусово.");
                executor.shutdownNow();
            }
        }
        System.out.println("Симуляція завершена.");
    }
}
