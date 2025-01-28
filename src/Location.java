import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantLock;

public class Location {
    private final ReentrantLock lock = new ReentrantLock();
    private final Point coordinatesOfLocation;
    private final List<Herbivorous> herbivores = new CopyOnWriteArrayList<>();
    private final List<Predator> predators = new CopyOnWriteArrayList<>();
    private final List<Plant> plants = new CopyOnWriteArrayList<>();
    private final Island island;
    private volatile boolean isInitialized = false;
    private final ExecutorService executor = Executors.newFixedThreadPool(3);

    public Location(Island island, Point coordinatesOfLocation) {
        this.coordinatesOfLocation = coordinatesOfLocation;
        this.island = island;
    }

    public ReentrantLock getLock() {
        return lock;
    }

    public Point getCoordinatesOfLocation() {
        return this.coordinatesOfLocation;
    }

    public List<Plant> getPlants() {
        return plants;
    }

    public List<Predator> getPredators() {
        return predators;
    }

    public List<Herbivorous> getHerbivores() {
        return herbivores;
    }

    public void init() {
        if (isInitialized) return; // Якщо вже ініціалізовано, вийти.

        Properties prop = new Properties();
        Map<String, Integer> maxAmounts = new HashMap<>();

        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("animalconfig.properties")) {
            if (inputStream == null) {
                throw new IOException("Configuration file not found!");
            }

            prop.load(inputStream);

            // Зчитуємо максимальну кількість кожної тварини чи рослини з конфігурації
            maxAmounts.put("wolfMaxAmountOnLocation", parseConfig(prop, "wolfMaxAmountOnLocation"));
            maxAmounts.put("sheepMaxAmountOnLocation", parseConfig(prop, "sheepMaxAmountOnLocation"));
            maxAmounts.put("plantMaxAmountOnLocation", parseConfig(prop, "plantMaxAmountOnLocation"));
            maxAmounts.put("foxMaxAmountOnLocation", parseConfig(prop, "foxMaxAmountOnLocation"));
            maxAmounts.put("bearMaxAmountOnLocation", parseConfig(prop, "bearMaxAmountOnLocation"));
            maxAmounts.put("boaMaxAmountOnLocation", parseConfig(prop, "boaMaxAmountOnLocation"));
            maxAmounts.put("boarMaxAmountOnLocation", parseConfig(prop, "boarMaxAmountOnLocation"));
            maxAmounts.put("buffaloMaxAmountOnLocation", parseConfig(prop, "buffaloMaxAmountOnLocation"));
            maxAmounts.put("caterpillarMaxAmountOnLocation", parseConfig(prop, "caterpillarMaxAmountOnLocation"));
            maxAmounts.put("deerMaxAmountOnLocation", parseConfig(prop, "deerMaxAmountOnLocation"));
            maxAmounts.put("duckMaxAmountOnLocation", parseConfig(prop, "duckMaxAmountOnLocation"));
            maxAmounts.put("eagleMaxAmountOnLocation", parseConfig(prop, "eagleMaxAmountOnLocation"));
            maxAmounts.put("goatMaxAmountOnLocation", parseConfig(prop, "goatMaxAmountOnLocation"));
            maxAmounts.put("horseMaxAmountOnLocation", parseConfig(prop, "horseMaxAmountOnLocation"));
            maxAmounts.put("mouseMaxAmountOnLocation", parseConfig(prop, "mouseMaxAmountOnLocation"));
            maxAmounts.put("rabbitMaxAmountOnLocation", parseConfig(prop, "rabbitMaxAmountOnLocation"));

        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        // Ініціалізація сутностей
        populateEntities(predators, maxAmounts.get("wolfMaxAmountOnLocation"), () -> new Wolf(island, this));
        populateEntities(herbivores, maxAmounts.get("sheepMaxAmountOnLocation"), () -> new Sheep(island, this));
        populateEntities(plants, maxAmounts.get("plantMaxAmountOnLocation"), () -> new Plant(island, this));
        populateEntities(predators, maxAmounts.get("foxMaxAmountOnLocation"), () -> new Fox(island, this));
        populateEntities(predators, maxAmounts.get("bearMaxAmountOnLocation"), () -> new Bear(island, this));
        populateEntities(predators, maxAmounts.get("boaMaxAmountOnLocation"), () -> new Boa(island, this));
        populateEntities(herbivores, maxAmounts.get("boarMaxAmountOnLocation"), () -> new Boar(island, this));
        populateEntities(herbivores, maxAmounts.get("buffaloMaxAmountOnLocation"), () -> new Buffalo(island, this));
        populateEntities(herbivores, maxAmounts.get("caterpillarMaxAmountOnLocation"), () -> new Caterpillar(island, this));
        populateEntities(herbivores, maxAmounts.get("deerMaxAmountOnLocation"), () -> new Deer(island, this));
        populateEntities(herbivores, maxAmounts.get("duckMaxAmountOnLocation"), () -> new Duck(island, this));
        populateEntities(predators, maxAmounts.get("eagleMaxAmountOnLocation"), () -> new Eagle(island, this));
        populateEntities(herbivores, maxAmounts.get("goatMaxAmountOnLocation"), () -> new Goat(island, this));
        populateEntities(herbivores, maxAmounts.get("horseMaxAmountOnLocation"), () -> new Horse(island, this));
        populateEntities(herbivores, maxAmounts.get("mouseMaxAmountOnLocation"), () -> new Mouse(island, this));
        populateEntities(herbivores, maxAmounts.get("rabbitMaxAmountOnLocation"), () -> new Rabbit(island, this));

        isInitialized = true;
    }

    private int parseConfig(Properties prop, String key) {
        try {
            return Integer.parseInt(prop.getProperty(key, "0"));
        } catch (NumberFormatException e) {
            System.err.println("Invalid value for " + key + " in configuration file. Using default 0.");
            return 0;
        }
    }

    private <T> void populateEntities(List<T> list, int maxAmount, EntityFactory<T> factory) {
        for (int i = 0; i < ThreadLocalRandom.current().nextInt(1, maxAmount); i++) {
            list.add(factory.create());
        }
    }

    public void shutdown() {
        executor.shutdown(); // Завершуємо роботу потоків
        try {
            if (!executor.awaitTermination(1, TimeUnit.MILLISECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
        }
    }
    public void simulateLife() {
        int simulationDurationMillis = 500; // Максимальний час для симуляції на одній локації (в мілісекундах)

        Callable<Void> predatorTask = () -> {
            predators.forEach(predator -> {
                predator.eat();
                predator.reproduce();
                predator.move();
            });
            return null;
        };

        Callable<Void> herbivorousTask = () -> {
            herbivores.forEach(herbivorous -> {
                herbivorous.eat();
                herbivorous.reproduce();
                herbivorous.move();
            });
            return null;
        };

        Callable<Void> plantTask = () -> {
            plants.forEach(Plant::reproduce);
            return null;
        };

        // Створюємо список завдань для симуляції життя
        List<Callable<Void>> tasks = List.of(predatorTask, herbivorousTask, plantTask);

        try {
            // Виконуємо всі завдання паралельно з обмеженням часу
            List<Future<Void>> futures = executor.invokeAll(tasks, simulationDurationMillis, TimeUnit.MILLISECONDS);

            // Перевіряємо, чи всі завдання завершились успішно
            for (Future<Void> future : futures) {
                if (!future.isDone()) {
                    System.out.println("A task in location " + coordinatesOfLocation + " was not completed in time.");
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Відновлюємо статус перерваного потоку
        }
    }

    @FunctionalInterface
    private interface EntityFactory<T> {
        T create();
    }
}
