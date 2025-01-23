import org.w3c.dom.ls.LSOutput;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantLock;

public class Location {
    private final ReentrantLock lock = new ReentrantLock();
    private final Point coordinatesOfLocation;
    private final List<Animal> herbivores = new CopyOnWriteArrayList<>();
    private final List<Animal> predators = new CopyOnWriteArrayList<>();
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

    public List<Animal> getPredators() {
        return predators;
    }

    public List<Animal> getHerbivores() {
        return herbivores;
    }

    public void init() {
        lock.lock();
        try {
            if (isInitialized) return; // Якщо вже ініціалізовано, вийти.

            Properties prop = new Properties();
            int wolfMaxAmountOnLocation = 0;
            int sheepMaxAmountOnLocation = 0;
            int plantMaxAmountOnLocation = 0;

            try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("animalconfig.properties")) {
                if (inputStream == null) {
                    throw new IOException("Configuration file not found!");
                }
                prop.load(inputStream);
                wolfMaxAmountOnLocation = parseConfig(prop, "wolfMaxAmountOnLocation");
                sheepMaxAmountOnLocation = parseConfig(prop, "sheepMaxAmountOnLocation");
                plantMaxAmountOnLocation = parseConfig(prop, "plantMaxAmountOnLocation");
            } catch (IOException e) {
                e.printStackTrace();
            }

            populateEntities(predators, wolfMaxAmountOnLocation, () -> new Wolf(island, this));
            populateEntities(herbivores, sheepMaxAmountOnLocation, () -> new Sheep(island, this));
            populateEntities(plants, plantMaxAmountOnLocation, () -> new Plant(island, this));

            isInitialized = true;
        } finally {
            lock.unlock();
        }
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
    public void update() {
        if(this.getPlants().size() + this.getPredators().size() + this.getHerbivores().size() > 2545){
            executor.submit(() -> predators.forEach(Creature::die));

            executor.submit(() -> herbivores.forEach(Creature::die));

            executor.submit(() -> plants.forEach(Plant::die));
        }
        executor.submit(() -> predators.forEach(predator -> {
            predator.eat();
            predator.reproduce();
            predator.move();
        }));

        executor.submit(() -> herbivores.forEach(herbivore -> {
            herbivore.eat();
            herbivore.reproduce();
            herbivore.move();
        }));

        executor.submit(() -> plants.forEach(Plant::reproduce));
    }

    @FunctionalInterface
    private interface EntityFactory<T> {
        T create();
    }
}
